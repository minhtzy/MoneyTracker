package com.example.t2m.moneytracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.t2m.moneytracker.account.LoginActivity;
import com.example.t2m.moneytracker.changelanguage.ChangeLanguageActivity;
import com.example.t2m.moneytracker.wallet.AddWalletActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.example.t2m.moneytracker.utils.LanguageUtils.initCurrentLanguage;

public class WelcomeActivity extends AppCompatActivity {

    public static String DATABASE_NAME = "money_tracker.db";
    public static final String DB_PATH_SUFFIX = "/databases/";
    public static SQLiteDatabase database = null;
    public static final String DB_PREFS = "MoneyTrackerSharedPrefs";
    public static final String KEY_IS_FIRST_TIME_INIT_DATABASE = "moneytracker.db.sharedprefs.key.is_first_time";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        setupApplication();

    }

    private void setupApplication() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(DB_PREFS,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Kiểm tra lần đầu khởi tạo
        if(sharedPreferences.getBoolean(KEY_IS_FIRST_TIME_INIT_DATABASE,true)) {
            CopySqlitetoSystemMobile();
            editor.putBoolean(KEY_IS_FIRST_TIME_INIT_DATABASE,false);
            editor.commit();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startApplication();
            }
        }, 1000);
    }
    private void startApplication() {
        // Kiểm tra nếu chưa đăng nhập thì đăng nhập
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        }
        // Kiểm tra nếu chưa có ví
        else if(false) {
            Intent intent = new Intent(this,AddWalletActivity.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
        finish();
    }

    private void CopyDatabasefromAssetToSystem() {
        try {
            // luồng đọc file
            InputStream inputStream = getAssets().open(DATABASE_NAME);
            String outFilename = LayDuongDan();
            // if the path doesn't exist first, create it
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists()){
                f.mkdir();
            }
            // Open the empty db as the output stream
            // Tạo một luồng ký tự đầu ra với mục đích ghi thông tin vào file
            OutputStream outputStream = new FileOutputStream(outFilename);
            // transfer bytes from the inputfile to the outputfile
            // Tạo một mảng byte ,ta sẽ ghi các byte này vào file nói trên .
            byte[] buffer = new byte[1024];
            int length;
            // Ghi lần lượt các ký tự vào luồng
            while ((length = inputStream.read(buffer)) > 0){
                outputStream.write(buffer,0,length);
            }
            // Close the streams
            outputStream.flush();// những cái gì còn lại cta tống ra hết
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String LayDuongDan() {

        return getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME ;
    }

    private void CopySqlitetoSystemMobile() {
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()){
            try {
                CopyDatabasefromAssetToSystem();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
}
