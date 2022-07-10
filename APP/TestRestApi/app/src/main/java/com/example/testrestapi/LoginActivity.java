package com.example.testrestapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;

public class LoginActivity extends AppCompatActivity {
    private final String TAG = LoginActivity.class.getSimpleName();
    private EditText Email, Password;
    private Button Login,Signup;
    private TextView passwordreset;
    private EditText passwordresetemail;
    private ProgressBar progressBar;
    private ProgressDialog processDialog;
    boolean check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Email = findViewById(R.id.user);
        Password = findViewById(R.id.password);
        Login = findViewById(R.id.Login);
        Signup = findViewById(R.id.signup);

        passwordreset = findViewById(R.id.forgotpassword);
        passwordresetemail = findViewById(R.id.user);

        progressBar = (ProgressBar) findViewById(R.id.progressbars);
        progressBar.setVisibility(View.GONE);

        processDialog = new ProgressDialog(this);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                loginUserAccount(Email.getText().toString(), Password.getText().toString());
            }
        });
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resgisterAccount();
            }
        });
        passwordreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetpasword();
            }
        });
    }
    private void resgisterAccount() {
        Intent intentres = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intentres);
    }
    public void loginUserAccount(String userEmail, String userPassword){
        if (TextUtils.isEmpty(userEmail)) {
            Email.setError("It's empty");
            Email.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(userPassword)) {
            Password.setError("It's empty");
            Password.requestFocus();
            return;
        }
        processDialog.setMessage("Please Wait For Login");
        processDialog.show();
        String user = Email.getText().toString().replace(" ","");
        String pass = Password.getText().toString().replace(" ","");

        Amplify.Auth.signIn(user, pass, result->{
                    if(result.isSignInComplete()){
                        Intent i;
                        if(user.equals("admin")){
                            //Toast.makeText(getApplicationContext(),"Login Success",Toast.LENGTH_SHORT).show();
                            i = new Intent(getApplicationContext(),AdminActivity.class);
                            //startActivity(i);
                        }else {
                            //Toast.makeText(getApplicationContext(),"Login Success",Toast.LENGTH_SHORT).show();
                            i = new Intent(getApplicationContext(),MainActivity.class);
                            //startActivity(i);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),"Login Success",Toast.LENGTH_SHORT).show();
                                startActivity(i);
                            }
                        });
                    }
                },
                error-> {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e(TAG,error.toString());
                            processDialog.cancel();
                            Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_SHORT).show();
                        }
                    });
                });
    }
    public void resetpasword(){
        final String resetemail = passwordresetemail.getText().toString();

        if (resetemail.isEmpty()) {
            passwordresetemail.setError("It's empty");
            passwordresetemail.requestFocus();
            return;
        }
        String username = passwordresetemail.getText().toString().replace(" ","");
        progressBar.setVisibility(View.VISIBLE);
        Amplify.Auth.resetPassword(username,
                result->{
                    progressBar.setVisibility(View.GONE);
                    Log.i(TAG, result.toString());

                },
                error-> {
                    progressBar.setVisibility(View.GONE);
                    Log.e(TAG, error.toString());
                });
//        mAuth.sendPasswordResetEmail(resetemail).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(LoginActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(LoginActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
//                        }
//                        progressBar.setVisibility(View.GONE);
//                    }
//                });
    }
}