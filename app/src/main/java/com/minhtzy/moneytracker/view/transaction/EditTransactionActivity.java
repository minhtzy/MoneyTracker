package com.minhtzy.moneytracker.view.transaction;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.dataaccess.LocationDAOImpl;
import com.minhtzy.moneytracker.entity.EventEntity;
import com.minhtzy.moneytracker.entity.LocationEntity;
import com.minhtzy.moneytracker.view.event.SelectEventActivity;
import com.minhtzy.moneytracker.view.wallet.adapter.WalletListAdapter;
import com.minhtzy.moneytracker.dataaccess.IWalletsDAO;
import com.minhtzy.moneytracker.dataaccess.WalletsDAOImpl;
import com.minhtzy.moneytracker.entity.CategoryEntity;
import com.minhtzy.moneytracker.entity.TransactionEntity;
import com.minhtzy.moneytracker.entity.WalletEntity;
import com.minhtzy.moneytracker.model.MTDate;
import com.minhtzy.moneytracker.utilities.ResourceUtils;
import com.minhtzy.moneytracker.utilities.CategoryManager;
import com.minhtzy.moneytracker.utilities.TransactionsManager;
import com.minhtzy.moneytracker.utilities.WalletsManager;
import com.minhtzy.moneytracker.view.CurrencyEditText;
import com.minhtzy.moneytracker.view.category.SelectCategoryActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class EditTransactionActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_CATEGORY = 1;
    private static final int REQUEST_CODE_GALLERY = 2;
    private static final int REQUEST_CODE_CAMERA = 3 ;
    public static final int REQUEST_CODE_EVENT = 4;
    public static final int REQUEST_PLACE_PICKER = 5;
    public static final String EXTRA_TRANSACTION = "com.minhtzy.moneytracker.extra.transaction";
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 6;

    private CurrencyEditText mTextMoney;
    private EditText mTextCategory;
    private EditText mTextNote;
    private EditText mTextDate;
    private EditText mTextWallet;
    private ImageView mImgCamera;
    private ImageView mImgPicture;
    private ImageView mImgPreView;
    private Calendar mCalendar;
    private TextView mTextPlace;

    private FirebaseUser mCurrentUser;
    private IWalletsDAO iWalletsDAO;
    private List<WalletEntity> mListWallet;
    private CategoryEntity mCurrentCategory =null;
    private WalletEntity mCurrentWallet = null;
    private Bitmap mCurrentImage = null;
    private EventEntity mEvent = null;

    private TextView mTextEvent;
    private ImageView mImgEvent;

    Place mCurrentPlace = null;
    TransactionEntity oldTransaction = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addControls();
        addEvents();
        loadTransaction();
        updateLabelDate();
    }

    private void loadTransaction() {
        oldTransaction= (TransactionEntity) Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_TRANSACTION));
        if(oldTransaction != null) {
            mCurrentWallet = WalletsManager.getInstance(this).getWalletById(oldTransaction.getWalletId());
            mCurrentCategory = CategoryManager.getInstance().getCategoryById(oldTransaction.getCategoryId());
            updateUI();
            updateImagePreView(oldTransaction.getMediaUri());
            mTextMoney.setText(String.valueOf(Math.abs(oldTransaction.getTransactionAmount())));
            mTextNote.setText(oldTransaction.getTransactionNote());
            mCalendar.setTime(oldTransaction.getTransactionTime());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save: {
                onClickedAdd();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onClickedCancel();
        return true;
    }

    private void addControls() {

        mTextMoney = findViewById(R.id.text_transaction_money);
        mTextCategory = findViewById(R.id.text_transaction_category);
        mTextNote = findViewById(R.id.text_transaction_note);
        mTextDate = findViewById(R.id.text_transaction_date);
        mTextWallet = findViewById(R.id.text_transaction_wallet);
        mImgCamera = findViewById(R.id.image_capture_picture);
        mImgPicture = findViewById(R.id.image_choose_picture);
        mImgPreView = findViewById(R.id.image_preview);
        mImgEvent = findViewById(R.id.icon_event);
        mTextEvent = findViewById(R.id.event_name);
        mCalendar = Calendar.getInstance();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        iWalletsDAO = new WalletsDAOImpl(this);
        mListWallet = iWalletsDAO.getAllWalletByUser(mCurrentUser.getUid());
        mCurrentWallet = WalletsManager.getInstance(this).getCurrentWallet();
        mTextPlace = findViewById(R.id.location_name);
    }

    private void addEvents() {
        mTextCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickedCategory(v);
            }
        });
        mTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickedDate(v);
            }
        });
        mTextWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickedWallet(v);
            }
        });
        mImgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhotoFromCamera();
            }
        });
        mImgPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhotoFromGallary();
            }
        });

        findViewById(R.id.layout_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickEvent();
            }
        });
        findViewById(R.id.clear_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClearEvent();
            }
        });
        findViewById(R.id.layout_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLocation();
            }
        });

        findViewById(R.id.clear_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClearLocation();
            }
        });
    }

    private void onClearLocation() {
        mCurrentPlace = null;
        mTextPlace.setText("");
        findViewById(R.id.clear_location).setVisibility(View.INVISIBLE);
    }

    private void onClickLocation() {
        if(!Places.isInitialized())
        {
            Places.initialize(getApplicationContext(),getString(R.string.google_api_key));
        }

        List<Place.Field> fields = Arrays.asList(Place.Field.ID,Place.Field.NAME);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN,fields).build(this);
        startActivityForResult(intent,REQUEST_PLACE_PICKER);
    }

    private void onClearEvent() {
        mTextEvent.setText("");
        mImgEvent.setImageResource(R.drawable.ic_event_black_24dp);
        mEvent = null;
        findViewById(R.id.clear_event).setVisibility(View.INVISIBLE);
    }

    private void onClickEvent() {
        Intent intent = new Intent(EditTransactionActivity.this, SelectEventActivity.class);
        if(mCurrentWallet != null)
        {
            intent.putExtra(SelectEventActivity.EXTRA_WALLET,mCurrentWallet.getWalletId());
        }
        startActivityForResult(intent,REQUEST_CODE_EVENT);
    }


    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    private void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
    }

    private void onClickedAdd() {
        if (mCurrentWallet == null) {
            mTextWallet.setError("Chọn ví của bạn");
            mTextWallet.requestFocus();
            return;
        }
        if (mCurrentCategory == null) {
            mTextCategory.setError("Chọn kiểu giao dịch");
            mTextCategory.requestFocus();
            return;
        }
        String mMediaUri = null;
        if (mCurrentImage != null) {
            mMediaUri = ResourceUtils.saveImage(this, mCurrentImage);
        }
        float money = (float) ((CurrencyEditText) mTextMoney).getCleanDoubleValue() * mCurrentCategory.getRate();
        String note = mTextNote.getText().toString();
        Date date = mCalendar.getTime();
        TransactionEntity entity = new TransactionEntity();
        entity.setTransactionId(oldTransaction.getTransactionId());
        entity.setTransactionTime(new MTDate(date));
        entity.setCategoryId(mCurrentCategory.getCategoryId());
        entity.setWalletId(mCurrentWallet.getWalletId());
        entity.setTransactionAmount(money);
        entity.setTransactionNote(note);
        entity.setMediaUri(mMediaUri);

        if(mCurrentPlace != null)
        {
            entity.setLocationId(mCurrentPlace.getId());

            LocationEntity location = new LocationEntity();
            location.setId(mCurrentPlace.getId());
            location.setName(mCurrentPlace.getName());
            new LocationDAOImpl(this).insertLocation(location);
        }
        if(mEvent != null)
        {
            entity.setEventId(mEvent.getEventId());
        }


        boolean updated = TransactionsManager.getInstance(this).updateTransaction(entity);

        if(!updated)
        {
            Toast.makeText(this,"Cập nhập giao dịch thất bại" ,Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this,"Cập nhập giao dịch thành công" ,Toast.LENGTH_SHORT).show();

        Intent intent = new Intent();
        intent.putExtra(EXTRA_TRANSACTION, Parcels.wrap(entity));
        setResult(RESULT_OK, intent);
        finish();
    }

    private void onClickedCancel() {

        setResult(RESULT_CANCELED);
        finish();
    }

    private void onClickedCategory(View v) {
        Intent intent = new Intent(this, SelectCategoryActivity.class);
        startActivityForResult(intent, REQUEST_CODE_CATEGORY);
    }

    private void onClickedDate(View v) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mCalendar.set(year, month, dayOfMonth);
                updateLabelDate();
            }
        };
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int date = mCalendar.get(Calendar.DATE);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,dateSetListener,year,month,date);
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();
    }

    private void onClickedWallet(View v) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setIcon(R.drawable.ic_account_balance_wallet_black_24dp);
        builderSingle.setTitle("Chọn ví của bạn");

        final ArrayAdapter arrayAdapter = new WalletListAdapter(this, R.layout.custom_item_wallet, mListWallet);


        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCurrentWallet = mListWallet.get(which);
                updateUI();
                dialog.dismiss();
            }
        });
        builderSingle.show();
    }

    private void updateLabelDate() {
        Date date = mCalendar.getTime();
        if (DateUtils.isToday(date.getTime())) {
            mTextDate.setText(getResources().getString(R.string.today));
        } else {
            String strDate = new MTDate(mCalendar).toIsoDateShortTimeString();
            mTextDate.setText(strDate);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_CATEGORY) {
                mCurrentCategory = (CategoryEntity) Parcels.unwrap(data.getParcelableExtra(SelectCategoryActivity.EXTRA_CATEGORY));
                updateUI();
            } else if (requestCode == REQUEST_CODE_GALLERY) {
                if (data != null) {
                    Uri contentURI = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                        mCurrentImage = bitmap;
                        updateImagePreView(bitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(EditTransactionActivity.this, "Load Image Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (requestCode == REQUEST_CODE_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                mCurrentImage = thumbnail;
                updateImagePreView(thumbnail);
            } else if (requestCode == REQUEST_CODE_EVENT) {
                mEvent = Parcels.unwrap(data.getParcelableExtra(SelectEventActivity.EXTRA_EVENT));
                if (mEvent != null) {
                    mTextEvent.setText(mEvent.getEventName());
                    mImgEvent.setImageDrawable(ResourceUtils.getCategoryIcon(mEvent.getEventIcon()));
                    findViewById(R.id.clear_event).setVisibility(View.VISIBLE);
                }
            } else if (requestCode == REQUEST_PLACE_PICKER) {
                mCurrentPlace = Autocomplete.getPlaceFromIntent(data);
                mTextPlace.setText(mCurrentPlace.getName());
                findViewById(R.id.clear_location).setVisibility(View.VISIBLE);
            }
        }

    }

    private void updateImagePreView(Bitmap bitmap) {
        if (bitmap != null) {
            int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
            mImgPreView.setImageBitmap(scaled);
        }
    }
    private void updateImagePreView(String uri) {
        Bitmap bitmap = ResourceUtils.loadImageFromStorage(uri);
        mCurrentImage = bitmap;
        if(bitmap != null) {
            int nh = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()) );
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
            mImgPreView.setImageBitmap(scaled);
            mImgPreView.setVisibility(View.VISIBLE);
        }
        else {
            mImgPreView.setVisibility(View.INVISIBLE);
        }
    }
    private void updateUI() {
        if (mCurrentCategory != null) {
            mTextCategory.setText(mCurrentCategory.getCategoryName());
            ImageView imageView = findViewById(R.id.image_transaction_category);
            imageView.setImageDrawable(ResourceUtils.getCategoryIcon(mCurrentCategory.getCategoryIcon()));
        }

        if (mCurrentWallet != null) {
            mTextMoney.setCurrencyCode(mCurrentWallet.getCurrencyCode());
            mTextWallet.setText(mCurrentWallet.getName());
        }
    }
}
