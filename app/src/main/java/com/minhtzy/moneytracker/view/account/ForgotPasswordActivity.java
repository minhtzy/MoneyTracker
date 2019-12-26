package com.minhtzy.moneytracker.view.account;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.minhtzy.moneytracker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText txt_reset_email;
    Button btn_reset_password;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize Firebase Auth
        FirebaseApp.initializeApp(ForgotPasswordActivity.this);
        mAuth = FirebaseAuth.getInstance();

        addControls();
        addEvents();
    }

    private void addControls() {
        txt_reset_email = (EditText)findViewById(R.id.txt_reset_email);
        btn_reset_password = (Button)findViewById(R.id.btn_reset_password);
    }

    private void addEvents() {
        btn_reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkConnected()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Bạn cần kết nối Internet", Toast.LENGTH_LONG).show();
                }
                else {
                    String email = txt_reset_email.getText().toString();
                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(getApplicationContext(), "Nhập email của bạn!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                        txt_reset_email.setError("Định dạng email không hợp lệ");
                        txt_reset_email.requestFocus();
                        return;
                    }

                    mAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ForgotPasswordActivity.this, "Kiểm tra email của bạn để hoàn thành thay đổi mật khẩu!", Toast.LENGTH_LONG).show();
                                    } else {
                                        try {
                                            throw task.getException();
                                        } catch (FirebaseAuthInvalidUserException e) {
                                            txt_reset_email.setError("Email chưa đăng ký tài khoản");
                                            txt_reset_email.requestFocus();
                                        } catch (Exception e) {
                                           // Toast.makeText(ForgotPasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });

                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        setResult(RESULT_CANCELED);
        finish();
        return true;
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
