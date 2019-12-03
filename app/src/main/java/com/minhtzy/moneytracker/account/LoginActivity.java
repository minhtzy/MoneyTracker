package com.minhtzy.moneytracker.account;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.minhtzy.moneytracker.MainActivity;
import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.dataaccess.IWalletsDAO;
import com.minhtzy.moneytracker.dataaccess.WalletsDAOImpl;
import com.minhtzy.moneytracker.sync.SyncActivity;
import com.minhtzy.moneytracker.utilities.SharedPrefs;
import com.minhtzy.moneytracker.utilities.WalletsManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.minhtzy.moneytracker.wallet.SelectWalletTypeActivity;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    EditText txtEmailLo,txtPassWordLo;
    Button btnSignupLo,btnForgotPasswordLo,btnSingInLo;
    com.google.android.gms.common.SignInButton btnGoogleSingin;
    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    public static final int RC_EMAIL_SIGN_IN = 11;
    public static final int RC_GOOGLE_SIGN_IN = 12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(LoginActivity.this);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        addControls();
        addEvents();
    }





    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void goToSyncActivity() {
        Intent intent = new Intent(LoginActivity.this,SyncActivity.class);
        startActivity(intent);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser !=null) {
            // check wallet
            IWalletsDAO iWalletsDAO = new WalletsDAOImpl(this);
            boolean not_synced = SharedPrefs.getInstance().get(SharedPrefs.KEY_PULL_TIME,0) == 0;
            if(iWalletsDAO.hasWallet(currentUser.getUid()) ) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
            else if (not_synced) {
                goToSyncActivity();
            }
            else {
                Intent intent = new Intent(LoginActivity.this, SelectWalletTypeActivity.class);
                startActivity(intent);
            }
            finish();
        }
    }

    private void addControls() {
        txtEmailLo =(EditText)findViewById(R.id.txtEmailLo);
        txtPassWordLo = (EditText)findViewById(R.id.txtPassWordLo);
        btnSingInLo = (Button)findViewById(R.id.btnSigninLo);
        btnSignupLo = (Button)findViewById(R.id.btnSignupLo);
        btnForgotPasswordLo = (Button)findViewById(R.id.btnForgotpasswordLo);
        btnGoogleSingin = findViewById(R.id.google_button);
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
    }

    private void addEvents() {
        btnForgotPasswordLo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
        btnSignupLo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivityForResult(intent,RC_EMAIL_SIGN_IN);
                txtEmailLo.setError(null);
                txtPassWordLo.setError(null);
                txtPassWordLo.setText("");
            }
        });
        btnSingInLo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String email = txtEmailLo.getText().toString();
                String password = txtPassWordLo.getText().toString();
                if (!isNetworkConnected()) {
                    Toast.makeText(LoginActivity.this, "Bạn cần kết nối Internet", Toast.LENGTH_LONG).show();
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

                    progressDialog.show();
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("LoginActivity", "signInWithEmail:success");
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
                                        Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không chính xác,vui lòng thử lại.",
                                                Toast.LENGTH_LONG).show();
                                        updateUI(null);
                                    }

                                    progressDialog.dismiss();
                                    // ...
                                }
                            });
                }
            }
        });
        btnGoogleSingin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_EMAIL_SIGN_IN && resultCode == RESULT_OK){
            txtEmailLo.setText(data.getStringExtra("username"));
            Toast.makeText(this, "Bạn đã đăng ký thành công tài khoản "+data.getStringExtra("username"), Toast.LENGTH_LONG).show();
        }

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
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

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        progressDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(),"Authentication Failed.",Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        progressDialog.dismiss();
                    }
                });
    }
}
