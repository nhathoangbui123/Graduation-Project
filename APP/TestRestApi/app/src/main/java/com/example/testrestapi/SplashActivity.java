package com.example.testrestapi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.AuthChannelEventName;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.InitializationStatus;
import com.amplifyframework.hub.HubChannel;

public class SplashActivity extends AppCompatActivity {
    private final String TAG = SplashActivity.class.getSimpleName();
    private ImageView logo;
    private static int splashTimeOut=1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        logo=(ImageView)findViewById(R.id.logo);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Amplify.addPlugin(new AWSApiPlugin());
                    Amplify.addPlugin(new AWSCognitoAuthPlugin());
                    Amplify.configure(getApplicationContext());
                    Log.i(TAG, "Initialized Amplify.");
                } catch (AmplifyException error) {
                    Log.e(TAG, "Could not initialize Amplify.", error);
                }
                Amplify.Auth.fetchAuthSession(
                        result -> {
                            if(result.isSignedIn()){
                                String username = Amplify.Auth.getCurrentUser().getUsername();
                                Log.i(TAG, "User name: "+ username);
                                if(username.equals("admin")){
                                    startActivity(new Intent(SplashActivity.this , AdminActivity.class));
                                }else {
                                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                }
                            }
                            else{
                                startActivity(new Intent(SplashActivity.this , LoginActivity.class));
                            }
                        },
                        error -> Log.e(TAG, error.toString())
                );
                //finish();
            }
        },splashTimeOut);
        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.mysplashanimation);
        logo.startAnimation(myanim);
    }
}