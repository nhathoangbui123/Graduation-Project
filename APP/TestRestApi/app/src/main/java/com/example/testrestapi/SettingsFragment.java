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
    private String username;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        processDialog = new ProgressDialog(getContext());
        username = Amplify.Auth.getCurrentUser().getUsername();
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
                if(username.equals("hoang")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(true);
                    final EditText input = new EditText(getContext());
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
                    builder.setView(input);
                    builder.setTitle("Set Device 1 Threshold");
                    builder.setMessage("Please Enter Threshold for device 1 (W)");
                    builder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                processDialog.setMessage("Please Wait For Logout");
//                                processDialog.show();
                                    D1 = input.getText().toString();
                                    d1.setText("Device 1 Threshold: "+D1+" W");
                                    Log.i(TAG,"Device 1 Threshold: "+D1+" W");
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
                }else if(username.equals("admin")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(true);
                    final EditText input = new EditText(getContext());
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
                    builder.setView(input);
                    builder.setTitle("Set Device 1 Threshold");
                    builder.setMessage("Please Enter Threshold for device 1 (W)");
                    builder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                processDialog.setMessage("Please Wait For Logout");
//                                processDialog.show();
                                    D1 = input.getText().toString();
                                    d1.setText("Device 1 Threshold: "+D1+" W");
                                    Log.i(TAG,"Device 1 Threshold: "+D1+" W");
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
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(true);
                    builder.setTitle("Set Device 1 Threshold");
                    builder.setMessage("Current user not permit for set threshold device 1");
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
        d2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.equals("hoang")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(true);
                    final EditText input = new EditText(getContext());
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
                    builder.setView(input);
                    builder.setTitle("Set Device 2 Threshold");
                    builder.setMessage("Please Enter Threshold for device 2 (W)");
                    builder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                processDialog.setMessage("Please Wait For Logout");
//                                processDialog.show();
                                    D2 = input.getText().toString();
                                    d2.setText("Device 2 Threshold: "+D2+" W");
                                    Log.i(TAG,"Device 2 Threshold: "+D2+" W");
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
                }else if(username.equals("admin")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(true);
                    final EditText input = new EditText(getContext());
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
                    builder.setView(input);
                    builder.setTitle("Set Device 2 Threshold");
                    builder.setMessage("Please Enter Threshold for device 2 (W)");
                    builder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                processDialog.setMessage("Please Wait For Logout");
//                                processDialog.show();
                                    D2 = input.getText().toString();
                                    d2.setText("Device 2 Threshold: "+D2+" W");
                                    Log.i(TAG,"Device 2 Threshold: "+D2+" W");
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
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(true);
                    builder.setTitle("Set Device 2 Threshold");
                    builder.setMessage("Current user not permit for set threshold device 2");
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
        d3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.equals("do")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(true);
                    final EditText input = new EditText(getContext());
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
                    builder.setView(input);
                    builder.setTitle("Set Device 3 Threshold");
                    builder.setMessage("Please Enter Threshold for device 3 (W)");
                    builder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                processDialog.setMessage("Please Wait For Logout");
//                                processDialog.show();
                                    D3 = input.getText().toString();
                                    d3.setText("Device 3 Threshold: "+D3+" W");
                                    Log.i(TAG,"Device 3 Threshold: "+D3+" W");
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
                }else if(username.equals("admin")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(true);
                    final EditText input = new EditText(getContext());
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
                    builder.setView(input);
                    builder.setTitle("Set Device 3 Threshold");
                    builder.setMessage("Please Enter Threshold for device 3 (W)");
                    builder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                processDialog.setMessage("Please Wait For Logout");
//                                processDialog.show();
                                    D3 = input.getText().toString();
                                    d3.setText("Device 3 Threshold: "+D3+" W");
                                    Log.i(TAG,"Device 3 Threshold: "+D3+" W");
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
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(true);
                    builder.setTitle("Set Device 3 Threshold");
                    builder.setMessage("Current user not permit for set threshold device 3");
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
        d4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.equals("do")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(true);
                    final EditText input = new EditText(getContext());
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
                    builder.setView(input);
                    builder.setTitle("Set Device 4 Threshold");
                    builder.setMessage("Please Enter Threshold for device 4 (W)");
                    builder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                processDialog.setMessage("Please Wait For Logout");
//                                processDialog.show();
                                    D4 = input.getText().toString();
                                    d4.setText("Device 4 Threshold: "+D4+" W");
                                    Log.i(TAG,"Device 4 Threshold: "+D4+" W");
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
                }else if(username.equals("admin")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(true);
                    final EditText input = new EditText(getContext());
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
                    builder.setView(input);
                    builder.setTitle("Set Device 4 Threshold");
                    builder.setMessage("Please Enter Threshold for device 4 (W)");
                    builder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                processDialog.setMessage("Please Wait For Logout");
//                                processDialog.show();
                                    D4 = input.getText().toString();
                                    d4.setText("Device 4 Threshold: "+D4+" W");
                                    Log.i(TAG,"Device 4 Threshold: "+D4+" W");
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
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(true);
                    builder.setTitle("Set Device 4 Threshold");
                    builder.setMessage("Current user not permit for set threshold device 4");
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
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
                        d1.setText("Device 1 Threshold: 0 W");
                    }else{
                        d1.setText("Device 1 Threshold: "+D1+" W");
                    }

                    if(D2==null){
                        d2.setText("Device 2 Threshold: 0 W");
                    }else{
                        d2.setText("Device 2 Threshold: "+D2+" W");
                    }

                    if(D3==null){
                        d3.setText("Device 3 Threshold: 0 W");
                    }else{
                        d3.setText("Device 3 Threshold: "+D3+" W");
                    }

                    if(D4==null){
                        d4.setText("Device 4 Threshold: 0 W");
                    }else{
                        d4.setText("Device 4 Threshold: "+D4+" W");
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