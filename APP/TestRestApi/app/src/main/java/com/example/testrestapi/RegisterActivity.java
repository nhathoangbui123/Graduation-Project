package com.example.testrestapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {
    private final String TAG = RegisterActivity.class.getSimpleName();
    private EditText editTextName, editTextEmail, editTextPassword, editTextPhone,editTextcPassword;
    public Button UserRegisterBtn;
    private ProgressBar progressBar;
    private ProgressDialog processDialog;
    private String CodeConfirm = "";
    boolean check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextName=findViewById(R.id.name);
        editTextPhone = findViewById(R.id.phone);
        editTextEmail = findViewById(R.id.emailRegister);
        editTextPassword = findViewById(R.id.passwordRegister);
        editTextcPassword= findViewById(R.id.confirmPassword);
        UserRegisterBtn= findViewById(R.id.button_register);

        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        processDialog = new ProgressDialog(this);

        UserRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }
    private void registerUser() {
        String name = editTextName.getText().toString().replace(" ","");
        String email = editTextEmail.getText().toString().replace(" ","");
        String phone = editTextPhone.getText().toString().replace(" ","");
        String password = editTextPassword.getText().toString().replace(" ","");
        String cpassword = editTextcPassword.getText().toString().replace(" ","");
        if (email.isEmpty()) {
            editTextEmail.setError("It's empty");
            editTextEmail.requestFocus();
            return;
        }
        if (name.isEmpty()) {
            editTextName.setError("It's Empty");
            editTextName.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Not a valid emailaddress");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Its empty");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editTextPassword.setError("Less length");
            editTextPassword.requestFocus();
            return;
        }
        if(!password.equals(cpassword)){
            editTextcPassword.setError("Password Do not Match");
            editTextcPassword.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            editTextPhone.setError("Its empty");
            editTextPhone.requestFocus();
            return;
        }

        if (phone.length()<8) {
            editTextPhone.setError("Less length");
            editTextPhone.requestFocus();
            return;
        }
        processDialog.setMessage("Please Wait For Signup");
        processDialog.show();

        progressBar.setVisibility(View.VISIBLE);

        ArrayList<AuthUserAttribute> attributes = new ArrayList<>();
        attributes.add(new AuthUserAttribute(AuthUserAttributeKey.name(),name));
        attributes.add(new AuthUserAttribute(AuthUserAttributeKey.email(),email));
        attributes.add(new AuthUserAttribute(AuthUserAttributeKey.phoneNumber(),phone));
        AuthSignUpOptions options =AuthSignUpOptions.builder().userAttributes(attributes).build();

        Amplify.Auth.signUp(name,password,options,
                result->{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Sign Up Successfully",Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                            progressBar.setVisibility(View.GONE);
                            startActivity(i);
                        }
                    });
                },
                error-> {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Sign Up Failed",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            processDialog.cancel();
                            Log.e(TAG,error.toString());
                        }
                    });
                });
//            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
//            builder.setCancelable(true);
//            final EditText input = new EditText(RegisterActivity.this);
//            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//            builder.setView(input);
//            builder.setTitle("Confirm Sign Up");
//            builder.setMessage("Code confirm send to email!");
//            builder.setPositiveButton("Confirm",
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            CodeConfirm = input.getText().toString();
//                            Log.i(TAG,"Code Confirm"+CodeConfirm);
//                            Amplify.Auth.confirmSignUp(name,CodeConfirm,
//                                    result->{
//                                        Intent i = new Intent(getApplicationContext(),LoginActivity.class);
//                                        startActivity(i);
//                                    },
//                                    error -> Log.e(TAG, error.toString())
//                            );
//                        }
//                    });
//            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.cancel();
//                }
//            });
//
//            AlertDialog dialog = builder.create();
//            dialog.show();
    }
}