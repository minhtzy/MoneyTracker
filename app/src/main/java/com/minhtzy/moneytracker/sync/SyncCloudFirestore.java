package com.minhtzy.moneytracker.sync;

import android.content.ContentValues;
import android.content.Context;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.minhtzy.moneytracker.dataaccess.IWalletsDAO;
import com.minhtzy.moneytracker.dataaccess.TransactionsDAOImpl;
import com.minhtzy.moneytracker.dataaccess.WalletsDAOImpl;
import com.minhtzy.moneytracker.entity.EntityBase;
import com.minhtzy.moneytracker.entity.TransactionEntity;
import com.minhtzy.moneytracker.entity.WalletEntity;
import com.minhtzy.moneytracker.utilities.TransactionsManager;
import com.minhtzy.moneytracker.utils.SharedPrefs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.Date;
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


    public boolean onSync(WalletEntity wallet) {
        onPullTransactions(wallet);
        onPushSync(wallet);
        return true;
    }

    public void onPullSync(WalletEntity wallet) {
        onPullWallet(wallet);
        onPullTransactions(wallet);
    }

    public void onPullWallet(String uid) {
        final IWalletsDAO iWalletsDAO = new WalletsDAOImpl(context);
        db.collection("users")
                .document(uid)
                .collection("wallets")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG_LOG, document.getId() + " => " + document.getData());

                                iWalletsDAO.insertWallet(new WalletEntity(fromData(document)));
                                //onPullTransactions(wallet);
                            }
                            if(syncEvents != null) {
                                syncEvents.onPullWalletComplete();
                            }

                        } else {
                            Log.d(TAG_LOG, "Error getting documents: ", task.getException());
                            if(syncEvents != null) {
                                syncEvents.onPullWalletFailure();
                            }
                        }
                    }
                });
    }

    public void onPullTransactions(WalletEntity wallet) {

        long time_pull = SharedPrefs.getInstance().get(SharedPrefs.KEY_PULL_TIME,0);
        db.collection("users")
                .document(wallet.getUserId())
                .collection("wallets")
                .document("wallet_" + wallet.getWalletId())
                .collection("transactions")
                .whereGreaterThan("timestamp",new Timestamp(new Date(time_pull)))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG_LOG, document.getId() + " => " + document.getData());
                                TransactionEntity transaction = new TransactionEntity(fromData(document));
                                TransactionsManager.getInstance(context).addTransaction(transaction);
                            }
                            long timestamp = Timestamp.now().toDate().getTime();
                            SharedPrefs.getInstance().put(SharedPrefs.KEY_PULL_TIME,timestamp);
                            if(syncEvents != null) {
                                syncEvents.onPullTransactionComplete();
                            }
                        } else {
                            Log.d(TAG_LOG, "Error getting documents: ", task.getException());
                            if(syncEvents != null) {
                                syncEvents.onPullTransactionFailure();
                            }
                        }
                    }
                });
    }


    public void onPullWallet(WalletEntity wallet) {
        db.collection("users")
                .document(wallet.getUserId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG_LOG, "DocumentSnapshot data: " + document.getData());
                            } else {
                                Log.d(TAG_LOG, "No such document");
                            }
                        } else {
                            Log.d(TAG_LOG, "get failed with ", task.getException());
                        }
                    }
                });

    }

    public void onPushSync(WalletEntity wallet) {
        addWallet(wallet);
        addTransactions(wallet);


//        WalletsManager.getInstance(context).updateTimestamp(wallet.getWalletId(),timestamp);
//        for(Transaction transaction : transactions) {
//            TransactionsManager.getInstance(context).updateTimestamp(transaction.getTransactionId(), timestamp);
//        }
    }

    public void addWallet(WalletEntity wallet) {
        db.collection("users").document(wallet.getUserId())
                .collection("wallets")
                .document("wallet_"+ wallet.getWalletId())
                .set(fromContentValue(wallet.getContentValues()))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG_LOG,"Add wallet success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG_LOG,"Add wallet failure");
                        Log.d(TAG_LOG,e.getMessage());
                    }
                });
    }

    public void addTransactions(WalletEntity wallet) {

        long time_pull = SharedPrefs.getInstance().get(SharedPrefs.KEY_PUSH_TIME,0);
        List<TransactionEntity> transactions = new TransactionsDAOImpl(context).getAllSyncTransaction(wallet.getWalletId(), time_pull);
        WriteBatch writeBatch = db.batch();

        CollectionReference transactionsRef = db.collection("users")
                .document(wallet.getUserId())
                .collection("wallets")
                .document("wallet_" + wallet.getWalletId())
                .collection("transactions");

        for(TransactionEntity transaction : transactions) {
            DocumentReference documentRef = transactionsRef.document("transaction_" + transaction.getTransactionId());
            writeBatch.set(documentRef,fromContentValue(transaction.getContentValues()));
        }

        writeBatch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG_LOG,"Add transactions success");
                // update push time
                long timestamp = Timestamp.now().toDate().getTime();
                SharedPrefs.getInstance().put(SharedPrefs.KEY_PUSH_TIME,timestamp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG_LOG,"Add transactions failure");
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
