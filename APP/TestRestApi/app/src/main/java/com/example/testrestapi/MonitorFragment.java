package com.example.testrestapi;

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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.api.rest.RestOptions;
import com.amplifyframework.core.Amplify;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MonitorFragment extends Fragment {
    private final String TAG = MonitorFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private CardviewAdapter cardviewAdapter;
    private String I,E,U,F,Cost;
    private String UString, IString, EString, FString;
    private TextView cost;
    private ImageView imageCost;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monitor, container, false);
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
                    if(Cost==null){
                        cost.setText("0"+" VND");
                    }else{
                        cost.setText(Cost+" VND");
                    }
                    cardviewAdapter = new CardviewAdapter(getContext(),device,getActivity());
                    recyclerView.setAdapter(cardviewAdapter);
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

                        Log.i(TAG, "GET succeeded: U " + restResponse.getData().asJSONObject().getString("U"));
                        U=restResponse.getData().asJSONObject().getString("U");
                        Double UParsed = Double.parseDouble(U);
                        UString = String.format("%,.2f", UParsed);

                        Log.i(TAG, "GET succeeded: F " + restResponse.getData().asJSONObject().getString("F"));
                        F=restResponse.getData().asJSONObject().getString("F");
                        Double FParsed = Double.parseDouble(F);
                        FString = String.format("%,.2f", FParsed);

                        Log.i(TAG, "GET succeeded: Energy " + restResponse.getData().asJSONObject().getString("E"));
                        E=restResponse.getData().asJSONObject().getString("E");
                        Double EParsed = Double.parseDouble(E);
                        EString = String.format("%,.2f", EParsed);

                        //get current
                        Log.i(TAG, "GET succeeded: I " + restResponse.getData().asJSONObject().getString("I"));
                        I=restResponse.getData().asJSONObject().getString("I");
                        Double IParsed = Double.parseDouble(I);
                        IString = String.format("%,.2f", IParsed);

                        Log.i(TAG, "GET succeeded: Cost " + restResponse.getData().asJSONObject().getString("Cost"));
                        Cost=restResponse.getData().asJSONObject().getString("Cost");

                        Log.i(TAG, "GET succeeded: ts " + restResponse.getData().asJSONObject().getString("ts"));
                        Cost=restResponse.getData().asJSONObject().getString("ts");


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                apiFailure -> Log.e(TAG, "GET failed.", apiFailure)
        );
    }

}