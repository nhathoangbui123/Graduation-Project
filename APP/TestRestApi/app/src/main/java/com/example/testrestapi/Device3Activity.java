package com.example.testrestapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

public class Device3Activity extends AppCompatActivity {
    private final String TAG = Device3Activity.class.getSimpleName();
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private CardviewAdapter cardviewAdapter;
    private String I3,E,U,F,Cost,P3,T3;
    private String UString, I3String, EString, FString, P3String, T3String, C3String;
    private TextView cost;
    private ImageView imageCost;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device3);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Device 3");

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
                Intent intent = new Intent(Device3Activity.this, MainActivity.class);
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
                    if(C3String==null){
                        cost.setText("0"+" VND");
                    }else{
                        cost.setText(C3String+" VND");
                    }
                    cardviewAdapter = new CardviewAdapter(Device3Activity.this,device,Device3Activity.this);
                    recyclerView.setAdapter(cardviewAdapter);
                    mHandler.postDelayed(this, 1000);
                }
            };
    private List<InfoCardview> createList() {
        List<InfoCardview> list = new ArrayList<InfoCardview>();
        InfoCardview Energy = new InfoCardview("Energy","electric",4, EString);
        InfoCardview Power = new InfoCardview("Power","electric",4, P3String);
        InfoCardview Voltage = new InfoCardview("Voltage","electric",4, UString);
        InfoCardview Current = new InfoCardview("Current","electric",4, I3String);
        InfoCardview Frequency = new InfoCardview("Frequency","electric",4, FString);
        InfoCardview Threshold = new InfoCardview("Threshold","electric",4, T3String);

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

                        Log.i(TAG, "GET succeeded: Energy " + restResponse.getData().asJSONObject().getString("E3"));
                        E=restResponse.getData().asJSONObject().getString("E3");
                        Double EParsed = Double.parseDouble(E);
                        EString = String.format("%,.2f", EParsed);

                        //get current
                        Log.i(TAG, "GET succeeded: I3 " + restResponse.getData().asJSONObject().getString("I3"));
                        I3=restResponse.getData().asJSONObject().getString("I3");
                        Double I3Parsed = Double.parseDouble(I3);
                        I3String = String.format("%,.2f", I3Parsed);

                        Log.i(TAG, "GET succeeded: P3 " + restResponse.getData().asJSONObject().getString("P3"));
                        P3=restResponse.getData().asJSONObject().getString("P3");
                        Double P3Parsed = Double.parseDouble(P3);
                        P3String = String.format("%,.2f", P3Parsed);

                        Log.i(TAG, "GET succeeded: T3 " + restResponse.getData().asJSONObject().getString("T3"));
                        T3=restResponse.getData().asJSONObject().getString("T3");
                        Double T3Parsed = Double.parseDouble(T3);
                        T3String = String.format("%,.2f", T3Parsed);

                        Log.i(TAG, "GET succeeded: Cost device 3 " + restResponse.getData().asJSONObject().getString("C3"));
                        Cost=restResponse.getData().asJSONObject().getString("C3");
                        Double C3Parsed = Double.parseDouble(Cost);
                        C3String = String.format("%,.2f", C3Parsed);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                apiFailure -> Log.e(TAG, "GET failed.", apiFailure)
        );
    }
}