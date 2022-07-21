package com.example.testrestapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.api.rest.RestOptions;
import com.amplifyframework.core.Amplify;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class Device1Activity extends AppCompatActivity {
    private final String TAG = Device1Activity.class.getSimpleName();
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private CardviewAdapter cardviewAdapter;
    private String I1,E,U,F,Cost,P1,T1;
    private String UString, I1String, EString, FString, P1String, T1String, C1String;
    private TextView cost;
    private ImageView imageCost;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device1);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Device 1");

        cost=findViewById(R.id.cost);
        imageCost=findViewById(R.id.imagecost);
        imageCost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cost_chart();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.rcview);

        //List<InfoCardview> device = createList();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);

//        cardviewAdapter = new CardviewAdapter(getContext(),device,getActivity());
//        recyclerView.setAdapter(cardviewAdapter);
        mHandler.post(runnable);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(Device1Activity.this, MainActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private final Runnable runnable =
            new Runnable() {
                public void run() {
                    get_data();
                    List<InfoCardview> device = createList();
                    if(C1String==null){
                        cost.setText("0"+" VND");
                    }else{
                        cost.setText(C1String+" VND");
                    }
                    cardviewAdapter = new CardviewAdapter(Device1Activity.this,device,Device1Activity.this);
                    recyclerView.setAdapter(cardviewAdapter);
                    mHandler.postDelayed(this, 1000);
                }
            };
    private List<InfoCardview> createList() {
        List<InfoCardview> list = new ArrayList<InfoCardview>();
        InfoCardview Energy = new InfoCardview("Energy","electric",4, EString);
        InfoCardview Power = new InfoCardview("Power","electric",4, P1String);
        InfoCardview Voltage = new InfoCardview("Voltage","electric",4, UString);
        InfoCardview Current = new InfoCardview("Current","electric",4, I1String);
        InfoCardview Frequency = new InfoCardview("Frequency","electric",4, FString);
        InfoCardview Threshold = new InfoCardview("Threshold","electric",4, T1String);

        list.add(Energy);
        list.add(Power);
        list.add(Voltage);
        list.add(Current);
        list.add(Frequency);
        list.add(Threshold);
        return list;
    }
    private void get_data() {
        RestOptions options = RestOptions.builder()
                .addPath("/todo")
                .build();
        Amplify.API.get(options,
                restResponse -> {
                    try {
                        Log.i(TAG, "GET succeeded: U " + restResponse.getData().asJSONObject().getString("U"));
                        U=restResponse.getData().asJSONObject().getString("U");
                        Double UParsed = Double.parseDouble(U);
                        UString = String.format("%,.2f", UParsed);

                        Log.i(TAG, "GET succeeded: F " + restResponse.getData().asJSONObject().getString("F"));
                        F=restResponse.getData().asJSONObject().getString("F");
                        Double FParsed = Double.parseDouble(F);
                        FString = String.format("%,.2f", FParsed);

                        Log.i(TAG, "GET succeeded: Energy " + restResponse.getData().asJSONObject().getString("E1"));
                        E=restResponse.getData().asJSONObject().getString("E1");
                        Double EParsed = Double.parseDouble(E);
                        EString = String.format("%,.2f", EParsed);

                        //get current
                        Log.i(TAG, "GET succeeded: I1 " + restResponse.getData().asJSONObject().getString("I1"));
                        I1=restResponse.getData().asJSONObject().getString("I1");
                        Double I1Parsed = Double.parseDouble(I1);
                        I1String = String.format("%,.2f", I1Parsed);

                        Log.i(TAG, "GET succeeded: P1 " + restResponse.getData().asJSONObject().getString("P1"));
                        P1=restResponse.getData().asJSONObject().getString("P1");
                        Double P1Parsed = Double.parseDouble(P1);
                        P1String = String.format("%,.2f", P1Parsed);

                        Log.i(TAG, "GET succeeded: T1 " + restResponse.getData().asJSONObject().getString("T1"));
                        T1=restResponse.getData().asJSONObject().getString("T1");
                        Double T1Parsed = Double.parseDouble(T1);
                        T1String = String.format("%,.2f", T1Parsed);

                        Log.i(TAG, "GET succeeded: Cost device 1 " + restResponse.getData().asJSONObject().getString("C1"));
                        Cost=restResponse.getData().asJSONObject().getString("C1");
                        Double C1Parsed = Double.parseDouble(Cost);
                        C1String = String.format("%,.2f", C1Parsed);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                apiFailure -> Log.e(TAG, "GET failed.", apiFailure)
        );
    }
}