package com.example.t2m.moneytracker.account;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.t2m.moneytracker.MainActivity;
import com.example.t2m.moneytracker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Login extends AppCompatActivity {

    EditText txtEmailLo,txtPassWordLo;
    Button btnSignupLo,btnForgotPasswordLo,btnSingInLo;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    public static String DATABASE_NAME = "listchoose.sqlite";
    public static final String DB_PATH_SUFFIX = "/databases/";
    public static SQLiteDatabase database = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(Login.this);
        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
// [END initialize_auth]

        CopySqlitetoSystemMobile();
        addControls();
        addEvents();
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

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser !=null) {

            Intent intent = new Intent(Login.this, MainActivity.class);
            intent.putExtra("username", currentUser.getEmail());
            startActivity(intent);

        }
    }

    private void addControls() {
        txtEmailLo =(EditText)findViewById(R.id.txtEmailLo);
        txtPassWordLo = (EditText)findViewById(R.id.txtPassWordLo);
        btnSingInLo = (Button)findViewById(R.id.btnSiginpLo);
        btnSignupLo = (Button)findViewById(R.id.btnSingupLo);
        btnForgotPasswordLo = (Button)findViewById(R.id.btnForgotPasswordLo);

    }

    private void addEvents() {
        btnForgotPasswordLo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,ForgotPassword.class);
                startActivity(intent);
            }
        });
        btnSignupLo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Registration.class);
                startActivityForResult(intent,11);
            }
        });
        btnSingInLo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String email = txtEmailLo.getText().toString();
                String password = txtPassWordLo.getText().toString();
                if (!isNetworkConnected()) {
                    Toast.makeText(Login.this, "Bạn cần kết nối Internet", Toast.LENGTH_LONG).show();
                } else {
                    if (email.isEmpty()){
                        txtEmailLo.setError("Không được để trống");
                        txtEmailLo.requestFocus();
                        return;
                    }
                    if (password.isEmpty()){
                        txtPassWordLo.setError("Không được để trống");
                        txtPassWordLo.requestFocus();
                        return;
                    }

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("Login", "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("signInWithEmail:failure", task.getException());
                                        try {
                                            throw task.getException();
                                        } catch (FirebaseAuthInvalidUserException e) {
                                            txtEmailLo.setError("Email chưa đăng ký tài khoản");
                                            txtEmailLo.requestFocus();
                                            return;
                                        } catch (Exception e) {
                                        }
                                        Toast.makeText(Login.this, "Tài khoản hoặc mật khẩu không chính xác,vui lòng thử lại.",
                                                Toast.LENGTH_LONG).show();
                                        updateUI(null);
                                    }

                                    // ...
                                }
                            });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 11 && resultCode == 11){
                txtEmailLo.setText(data.getStringExtra("username"));
                progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Thông báo: ");
                progressDialog.setMessage("Bạn đã đăng ký thành công tài khoản: \n"+data.getStringExtra("username")+"\n"+"Đăng nhập ngay!");
                progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.show();
            }

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }
}
