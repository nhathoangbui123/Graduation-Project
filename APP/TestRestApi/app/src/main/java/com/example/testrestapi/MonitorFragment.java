package com.example.testrestapi;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.api.rest.RestOptions;
import com.amplifyframework.core.Amplify;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MonitorFragment extends Fragment {
    private final String TAG = MonitorFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private CardviewAdapter cardviewAdapter;
    private String I,E,U,F,Cost,Ts;
    private String UString, IString, EString, FString, CString;
    private TextView cost;
    private ImageView imageCost;
    private String username;
    private String Uname;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monitor, container, false);
        Uname = getArguments().getString("username");
        Log.i(TAG, Uname);

        cost=view.findViewById(R.id.cost);
        imageCost=view.findViewById(R.id.imagecost);
        imageCost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cost_chart();
            }
        });
        recyclerView = (RecyclerView) view.findViewById(R.id.rcview);

        //List<InfoCardview> device = createList();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);

//        cardviewAdapter = new CardviewAdapter(getContext(),device,getActivity());
//        recyclerView.setAdapter(cardviewAdapter);
        username = Amplify.Auth.getCurrentUser().getUsername();
        mHandler.post(runnable);

        return view;
    }
    private void cost_chart() {
        Intent intent =new Intent(getContext(), ChartCostActivity.class);
        startActivity(intent);
    }
    private final Runnable runnable =
            new Runnable() {
                public void run() {
                    get_data();
                    List<InfoCardview> device = createList();
                    if(CString==null){
                        cost.setText("0"+" VND");
                    }else{
                        cost.setText(CString+" VND");
                    }
                    cardviewAdapter = new CardviewAdapter(getContext(),device,getActivity());
                    recyclerView.setAdapter(cardviewAdapter);
                    SimpleDateFormat hour = new SimpleDateFormat("HH");
                    SimpleDateFormat day = new SimpleDateFormat("dd");
                    SimpleDateFormat month = new SimpleDateFormat("M");
                    if(Ts!=null){
                        String hourString = hour.format(new Date(Long.parseLong(Ts)));
                        String dayString = day.format(new Date(Long.parseLong(Ts)));
                        String monthString = month.format(new Date(Long.parseLong(Ts)));
                        Log.i(TAG, "hour: " + hourString);
                        Log.i(TAG, "day: " + dayString);
                        Log.i(TAG, "month: " + monthString);
                    }
                    mHandler.postDelayed(this, 1000);
                }
            };
    private List<InfoCardview> createList() {
        List<InfoCardview> list = new ArrayList<InfoCardview>();
        InfoCardview Energy = new InfoCardview("Energy","electric",1, EString);
        InfoCardview Voltage = new InfoCardview("Voltage","electric",1, UString);
        InfoCardview Current = new InfoCardview("Current","electric",1, IString);
        InfoCardview Frequency = new InfoCardview("Frequency","electric",1, FString);

        list.add(Energy);
        list.add(Voltage);
        list.add(Current);
        list.add(Frequency);
        return list;
    }
    private void get_data() {
        RestOptions options = RestOptions.builder()
                .addPath("/todo")
                .build();
        Amplify.API.get(options,
                restResponse -> {
                    try {
                        //get info
                        Log.i(TAG, "GET succeeded: Mac " + restResponse.getData().asJSONObject().getString("MAC"));
                        //get voltage
                        Log.i(TAG, "GET succeeded: U " + restResponse.getData().asJSONObject().getString("U"));
                        U=restResponse.getData().asJSONObject().getString("U");
                        Double UParsed = Double.parseDouble(U);
                        UString = String.format("%,.2f", UParsed);
                        //get frequency
                        Log.i(TAG, "GET succeeded: F " + restResponse.getData().asJSONObject().getString("F"));
                        F=restResponse.getData().asJSONObject().getString("F");
                        Double FParsed = Double.parseDouble(F);
                        FString = String.format("%,.2f", FParsed);
                        //get timestamp
                        Log.i(TAG, "GET succeeded: ts " + restResponse.getData().asJSONObject().getString("ts"));
                        Ts=restResponse.getData().asJSONObject().getString("ts");
                        if(username.equals("hoang")||Uname.equals("hoang")){
                            //get energy
                            Log.i(TAG, "GET succeeded: Energy of user 1 " + restResponse.getData().asJSONObject().getString("EU1"));
                            E=restResponse.getData().asJSONObject().getString("EU1");
                            Double EParsed = Double.parseDouble(E);
                            EString = String.format("%,.2f", EParsed);
                            //get current
                            Log.i(TAG, "GET succeeded: I of user 1" + restResponse.getData().asJSONObject().getString("IU1"));
                            I=restResponse.getData().asJSONObject().getString("IU1");
                            Double IParsed = Double.parseDouble(I);
                            IString = String.format("%,.2f", IParsed);
                            //get cost
                            Log.i(TAG, "GET succeeded: Cost total of user 1" + restResponse.getData().asJSONObject().getString("CU1"));
                            Cost=restResponse.getData().asJSONObject().getString("CU1");
                            Double CParsed = Double.parseDouble(Cost);
                            CString = String.format("%,.2f", CParsed);
                        }else if(username.equals("do")||Uname.equals("do")){
                            //get energy
                            Log.i(TAG, "GET succeeded: Energy of user 2 " + restResponse.getData().asJSONObject().getString("EU2"));
                            E=restResponse.getData().asJSONObject().getString("EU2");
                            Double EParsed = Double.parseDouble(E);
                            EString = String.format("%,.2f", EParsed);
                            //get current
                            Log.i(TAG, "GET succeeded: I of user 2" + restResponse.getData().asJSONObject().getString("IU2"));
                            I=restResponse.getData().asJSONObject().getString("IU2");
                            Double IParsed = Double.parseDouble(I);
                            IString = String.format("%,.2f", IParsed);
                            //get cost
                            Log.i(TAG, "GET succeeded: Cost total of user 2" + restResponse.getData().asJSONObject().getString("CU2"));
                            Cost=restResponse.getData().asJSONObject().getString("CU2");
                            Double CParsed = Double.parseDouble(Cost);
                            CString = String.format("%,.2f", CParsed);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                apiFailure -> Log.e(TAG, "GET failed.", apiFailure)
        );
    }
}