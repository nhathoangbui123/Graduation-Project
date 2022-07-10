package com.example.testrestapi;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.amplifyframework.api.rest.RestOptions;
import com.amplifyframework.core.Amplify;

import org.json.JSONException;

import java.util.List;

public class SettingsFragment extends Fragment {
    private final String TAG = SettingsFragment.class.getSimpleName();
    private TextView cost, wifiname, wifipass, d1, d2, d3, d4;
    private ProgressDialog processDialog;
    private String Cost = "";
    private String WifiName = "";
    private String WifiPass = "";
    private String D1 = "";
    private String D2 = "";
    private String D3 = "";
    private String D4 = "";
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        processDialog = new ProgressDialog(getContext());

        cost = view.findViewById(R.id.cost);
        wifiname = view.findViewById(R.id.wifiname);
        wifipass = view.findViewById(R.id.wifipass);
        d1 = view.findViewById(R.id.d1thresh);
        d2 = view.findViewById(R.id.d2thresh);
        d3 = view.findViewById(R.id.d3thresh);
        d4 = view.findViewById(R.id.d4thresh);
        mHandler.post(runnable);
        cost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);
                builder.setTitle("Set Electricity Price");
                builder.setMessage("Please Enter Electricity Price per kWh (VND/kWh)");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                processDialog.setMessage("Please Wait For Logout");
//                                processDialog.show();
                                Cost = input.getText().toString();
                                cost.setText("Electricity Price: "+Cost+"VND/kWh");
                                Log.i(TAG,"Electricity Price: "+Cost+"VND/kWh");
                                String enpoint = "EP";
                                String data = "{\"command\": \"EP\",\"body\": \""+Cost+"\"}";
                                RestOptions options = RestOptions.builder()
                                        .addPath("/todo/"+enpoint)
                                        .addBody(data.getBytes())
                                        .build();

                                Amplify.API.post(options,
                                        response -> Log.i(TAG, "POST succeeded: " + response.getData().asString()),
                                        error -> Log.e(TAG, "POST failed.", error)
                                );
                            }
                        });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        wifiname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setTitle("Set Wifi Name");
                builder.setMessage("Please Enter Wifi Name");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                processDialog.setMessage("Please Wait For Logout");
//                                processDialog.show();
                                WifiName = input.getText().toString();
                                wifiname.setText("Wifi Name: "+WifiName);
                                Log.i(TAG,"Wifi Name: "+WifiName);
                                String enpoint = "WN";
                                String data = "{\"command\":\"WN\",\"body\":\""+WifiName+"\"}";
                                RestOptions options = RestOptions.builder()
                                        .addPath("/todo/"+enpoint)
                                        .addBody(data.getBytes())
                                        .build();

                                Amplify.API.post(options,
                                        response -> Log.i(TAG, "POST succeeded: " + response.getData().asString()),
                                        error -> Log.e(TAG, "POST failed.", error)
                                );
                            }
                        });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        wifipass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setTitle("Set Wifi Pass");
                builder.setMessage("Please Enter Wifi Pass");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                processDialog.setMessage("Please Wait For Logout");
//                                processDialog.show();
                                WifiPass = input.getText().toString();
                                wifipass.setText("Wifi Pass: "+WifiPass);
                                Log.i(TAG,"Wifi Pass: "+WifiPass);
                                String enpoint = "WP";
                                String data = "{\"command\":\"WP\",\"body\":\""+WifiPass+"\"}";
                                RestOptions options = RestOptions.builder()
                                        .addPath("/todo/"+enpoint)
                                        .addBody(data.getBytes())
                                        .build();

                                Amplify.API.post(options,
                                        response -> Log.i(TAG, "POST succeeded: " + response.getData().asString()),
                                        error -> Log.e(TAG, "POST failed.", error)
                                );
                            }
                        });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        d1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);
                builder.setTitle("Set Device 1 Threshold");
                builder.setMessage("Please Enter Threshold for device 1 (kWh)");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                processDialog.setMessage("Please Wait For Logout");
//                                processDialog.show();
                                D1 = input.getText().toString();
                                d1.setText("Device 1 Threshold: "+D1+"kWh");
                                Log.i(TAG,"Electricity Price: "+D1+"kWh");
                                String enpoint = "T1";
                                String data = "{\"command\": \"T1\",\"body\": \""+D1+"\"}";
                                RestOptions options = RestOptions.builder()
                                        .addPath("/todo/"+enpoint)
                                        .addBody(data.getBytes())
                                        .build();

                                Amplify.API.post(options,
                                        response -> Log.i(TAG, "POST succeeded: " + response.getData().asString()),
                                        error -> Log.e(TAG, "POST failed.", error)
                                );
                            }
                        });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        d2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);
                builder.setTitle("Set Device 2 Threshold");
                builder.setMessage("Please Enter Threshold for device 2 (kWh)");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                processDialog.setMessage("Please Wait For Logout");
//                                processDialog.show();
                                D2 = input.getText().toString();
                                d2.setText("Device 1 Threshold: "+D2+"kWh");
                                Log.i(TAG,"Electricity Price: "+D2+"kWh");
                                String enpoint = "T2";
                                String data = "{\"command\": \"T2\",\"body\": \""+D2+"\"}";
                                RestOptions options = RestOptions.builder()
                                        .addPath("/todo/"+enpoint)
                                        .addBody(data.getBytes())
                                        .build();

                                Amplify.API.post(options,
                                        response -> Log.i(TAG, "POST succeeded: " + response.getData().asString()),
                                        error -> Log.e(TAG, "POST failed.", error)
                                );
                            }
                        });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        d3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);
                builder.setTitle("Set Device 3 Threshold");
                builder.setMessage("Please Enter Threshold for device 3 (kWh)");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                processDialog.setMessage("Please Wait For Logout");
//                                processDialog.show();
                                D3 = input.getText().toString();
                                d3.setText("Device 3 Threshold: "+D3+"kWh");
                                Log.i(TAG,"Electricity Price: "+D3+"kWh");
                                String enpoint = "T3";
                                String data = "{\"command\": \"T3\",\"body\": \""+D3+"\"}";
                                RestOptions options = RestOptions.builder()
                                        .addPath("/todo/"+enpoint)
                                        .addBody(data.getBytes())
                                        .build();

                                Amplify.API.post(options,
                                        response -> Log.i(TAG, "POST succeeded: " + response.getData().asString()),
                                        error -> Log.e(TAG, "POST failed.", error)
                                );
                            }
                        });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        d4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);
                builder.setTitle("Set Device 4 Threshold");
                builder.setMessage("Please Enter Threshold for device 4 (kWh)");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                processDialog.setMessage("Please Wait For Logout");
//                                processDialog.show();
                                D4 = input.getText().toString();
                                d4.setText("Device 4 Threshold: "+D4+"kWh");
                                Log.i(TAG,"Electricity Price: "+D4+"kWh");
                                String enpoint = "T4";
                                String data = "{\"command\": \"T4\",\"body\": \""+D4+"\"}";
                                RestOptions options = RestOptions.builder()
                                        .addPath("/todo/"+enpoint)
                                        .addBody(data.getBytes())
                                        .build();

                                Amplify.API.post(options,
                                        response -> Log.i(TAG, "POST succeeded: " + response.getData().asString()),
                                        error -> Log.e(TAG, "POST failed.", error)
                                );
                            }
                        });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        return view;
    }
    private final Runnable runnable =
            new Runnable() {
                public void run() {
                    get_data();
                    if(Cost==null){
                        cost.setText("Electricity Price: 0 VND/kWh");
                    }else{
                        cost.setText("Electricity Price: "+Cost+"VND/kWh");
                    }

                    if(WifiName==null){
                        wifiname.setText("Wifi Name: ");
                    }else{
                        wifiname.setText("Wifi Name: "+WifiName);
                    }

                    if(WifiPass==null){
                        wifipass.setText("Wifi Pass: ");
                    }else{
                        wifipass.setText("Wifi Pass: "+WifiPass);
                    }

                    if(D1==null){
                        d1.setText("Device 1 Threshold: 0kWh");
                    }else{
                        d1.setText("Device 1 Threshold: "+D1+"kWh");
                    }

                    if(D2==null){
                        d2.setText("Device 2 Threshold: 0kWh");
                    }else{
                        d2.setText("Device 2 Threshold: "+D2+"kWh");
                    }

                    if(D3==null){
                        d3.setText("Device 3 Threshold: 0kWh");
                    }else{
                        d3.setText("Device 3 Threshold: "+D3+"kWh");
                    }

                    if(D4==null){
                        d4.setText("Device 4 Threshold: 0kWh");
                    }else{
                        d4.setText("Device 4 Threshold: "+D4+"kWh");
                    }

                    mHandler.postDelayed(this, 1000);
                }
    };
    private void get_data() {
        RestOptions options = RestOptions.builder()
                .addPath("/todo")
                .build();
        Amplify.API.get(options,
                restResponse -> {
                    try {
                        Log.i(TAG, "GET succeeded: Electricity Price " + restResponse.getData().asJSONObject().getString("EP"));
                        Cost=restResponse.getData().asJSONObject().getString("EP");

                        Log.i(TAG, "GET succeeded: WifiName " + restResponse.getData().asJSONObject().getString("WN"));
                        WifiName=restResponse.getData().asJSONObject().getString("WN");

                        Log.i(TAG, "GET succeeded: WifiPass " + restResponse.getData().asJSONObject().getString("WP"));
                        WifiPass=restResponse.getData().asJSONObject().getString("WP");

                        Log.i(TAG, "GET succeeded: Device 1 Threshold " + restResponse.getData().asJSONObject().getString("T1"));
                        D1=restResponse.getData().asJSONObject().getString("T1");

                        Log.i(TAG, "GET succeeded: Device 2 Threshold " + restResponse.getData().asJSONObject().getString("T2"));
                        D2=restResponse.getData().asJSONObject().getString("T2");

                        Log.i(TAG, "GET succeeded: Device 3 Threshold " + restResponse.getData().asJSONObject().getString("T3"));
                        D3=restResponse.getData().asJSONObject().getString("T3");

                        Log.i(TAG, "GET succeeded: Device 4 Threshold " + restResponse.getData().asJSONObject().getString("T4"));
                        D4=restResponse.getData().asJSONObject().getString("T4");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                apiFailure -> Log.e(TAG, "GET failed.", apiFailure)
        );
    }

}