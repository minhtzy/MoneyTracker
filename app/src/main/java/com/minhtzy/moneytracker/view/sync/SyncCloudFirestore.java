package com.minhtzy.moneytracker.view.sync;

import android.content.ContentValues;
import android.content.Context;
import android.os.Parcel;
import androidx.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.minhtzy.moneytracker.dataaccess.IWalletsDAO;
import com.minhtzy.moneytracker.dataaccess.TransactionsDAOImpl;
import com.minhtzy.moneytracker.dataaccess.WalletsDAOImpl;
import com.minhtzy.moneytracker.entity.TransactionEntity;
import com.minhtzy.moneytracker.entity.WalletEntity;
import com.minhtzy.moneytracker.utilities.SharedPrefs;
import com.minhtzy.moneytracker.utilities.TransactionsManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyncCloudFirestore {

    FirebaseFirestore db;

    Context context;
    private String TAG_LOG = SyncCloudFirestore.class.getSimpleName();

    public void setSyncEvents(SyncEvents syncEvents) {
        this.syncEvents = syncEvents;
    }

    SyncEvents syncEvents;

    public SyncCloudFirestore(Context context) {
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
    }


    public boolean onSync() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        onPushSync(userId);
        onPullSync(userId);
        return true;
    }

    public void onPullSync(String userId) {
        onPullWallet(userId);
        //onPullTransactions(userId);
    }

    public void onPullWallet(final String uid) {

        long time_pull = SharedPrefs.getInstance().get(SharedPrefs.KEY_PULL_TIME,0L);
        final IWalletsDAO iWalletsDAO = new WalletsDAOImpl(context);
        db.collection("users")
                .document(uid)
                .collection("wallets")
                //.whereGreaterThan("timestamp",time_pull)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG_LOG, document.getId() + " => " + document.getData());

                                WalletEntity wallet = new WalletEntity(fromData(document));
                                if(iWalletsDAO.getWalletById(wallet.getWalletId()) == null)
                                {
                                    wallet.setCurrentBalance(0);
                                    iWalletsDAO.insertWallet(wallet);

                                }
                                else
                                {
                                    wallet.getContentValues().remove(WalletEntity.CURRENT_BALANCE);
                                    iWalletsDAO.updateWallet(wallet);
                                }
                            }
                            if(iWalletsDAO.hasWallet(uid))
                                onPullTransactions(uid);
                            else if (syncEvents != null)
                                syncEvents.onPullCompleted();

                        } else {
                            Log.d(TAG_LOG, "Error getting documents: ", task.getException());
                            if(syncEvents != null) {
                                syncEvents.onPullFailed();
                            }
                        }
                    }
                });
    }

    public void onPullTransactions(String userId) {

        long time_pull = SharedPrefs.getInstance().get(SharedPrefs.KEY_PULL_TIME,0L);
        db.collection("users")
                .document(userId)
                .collection("transactions")
                //.whereGreaterThan("timestamp",time_pull)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG_LOG, document.getId() + " => " + document.getData());
                                TransactionEntity transaction = new TransactionEntity(fromData(document));
                                TransactionsManager.getInstance(context).insertOrUpdate(transaction);
                            }
                            long timestamp = Timestamp.now().toDate().getTime();
                            SharedPrefs.getInstance().put(SharedPrefs.KEY_PULL_TIME,timestamp);
                            if(syncEvents != null) {
                                syncEvents.onPullCompleted();
                            }
                        } else {
                            Log.d(TAG_LOG, "Error getting documents: ", task.getException());
                            if(syncEvents != null) {
                                syncEvents.onPullFailed();
                            }
                        }
                    }
                });
    }

    public void onPushSync(String userId) {
        onPushWallets(userId);
        onPushTransactions(userId);
    }

    private void onPushTransactions(String userId) {

        Long time_pull = SharedPrefs.getInstance().get(SharedPrefs.KEY_PUSH_TIME,0L);
        List<TransactionEntity> transactions = new TransactionsDAOImpl(context).getAllSyncTransaction(userId, time_pull);
        WriteBatch writeBatch = db.batch();

        CollectionReference transactionsRef = db.collection("users")
                .document(userId)
                .collection("transactions");

        for(TransactionEntity transaction : transactions) {
            DocumentReference documentRef = transactionsRef.document("transaction_" + transaction.getTransactionId());
            writeBatch.set(documentRef,fromContentValue(transaction.getContentValues()));
        }

        writeBatch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG_LOG,"Add transactions success");

                // update time push
                SharedPrefs.getInstance().put(SharedPrefs.KEY_PUSH_TIME,Timestamp.now().toDate().getTime());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG_LOG,"Add transactions failure");
                Log.d(TAG_LOG,e.getMessage());
            }
        });
    }

    private void onPushWallets(String userId) {
        Long time_pull = SharedPrefs.getInstance().get(SharedPrefs.KEY_PUSH_TIME,0L);
        List<WalletEntity> listWallet = new WalletsDAOImpl(this.context).getAllWalletNeedSync(userId,time_pull);
        WriteBatch writeBatch = db.batch();

        CollectionReference transactionsRef = db.collection("users")
                .document(userId)
                .collection("wallets");

        for(WalletEntity walletEntity : listWallet) {
            DocumentReference documentRef = transactionsRef.document("wallet_" + walletEntity.getWalletId());
            walletEntity.getContentValues().remove(WalletEntity.CURRENT_BALANCE);
            writeBatch.set(documentRef,fromContentValue(walletEntity.getContentValues()));
        }

        writeBatch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG_LOG,"Add wallet success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG_LOG,"Add wallet failure");
                Log.d(TAG_LOG,e.getMessage());
            }
        });
    }

    Map<String,Object> fromContentValue (ContentValues data)
    {

        Map<String,Object> convert = new HashMap<>();
        for(String key : data.keySet())
        {
            convert.put(key,data.get(key));
        }
        return convert;
    }

    ContentValues fromData(QueryDocumentSnapshot document)
    {
        Parcel myParcel = Parcel.obtain();
        myParcel.writeMap(document.getData());
        myParcel.setDataPosition(0);
        ContentValues values = ContentValues.CREATOR.createFromParcel(myParcel);
        return values;
    }
}
