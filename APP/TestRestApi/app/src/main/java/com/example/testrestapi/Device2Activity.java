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

public class Device2Activity extends AppCompatActivity {
    private final String TAG = Device2Activity.class.getSimpleName();
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private CardviewAdapter cardviewAdapter;
    private String I2,E,U,F,Cost,P2,T2;
    private String UString, I2String, EString, FString, P2String, T2String;
    private TextView cost;
    private ImageView imageCost;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device2);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Device 2");

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
                Intent intent = new Intent(Device2Activity.this, MainActivity.class);
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
                    if(Cost==null){
                        cost.setText("0"+" VND");
                    }else{
                        cost.setText(Cost+" VND");
                    }
                    cardviewAdapter = new CardviewAdapter(Device2Activity.this,device,Device2Activity.this);
                    recyclerView.setAdapter(cardviewAdapter);
                    mHandler.postDelayed(this, 1000);
                }
            };
    private List<InfoCardview> createList() {
        List<InfoCardview> list = new ArrayList<InfoCardview>();
        InfoCardview Energy = new InfoCardview("Energy","electric",4, EString);
        InfoCardview Power = new InfoCardview("Power","electric",4, P2String);
        InfoCardview Voltage = new InfoCardview("Voltage","electric",4, UString);
        InfoCardview Current = new InfoCardview("Current","electric",4, I2String);
        InfoCardview Frequency = new InfoCardview("Frequency","electric",4, FString);
        InfoCardview Threshold = new InfoCardview("Threshold","electric",4, T2String);

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

                        Log.i(TAG, "GET succeeded: Energy " + restResponse.getData().asJSONObject().getString("E"));
                        E=restResponse.getData().asJSONObject().getString("E");
                        Double EParsed = Double.parseDouble(E);
                        EString = String.format("%,.2f", EParsed);

                        //get current
                        Log.i(TAG, "GET succeeded: I2 " + restResponse.getData().asJSONObject().getString("I2"));
                        I2=restResponse.getData().asJSONObject().getString("I2");
                        Double I2Parsed = Double.parseDouble(I2);
                        I2String = String.format("%,.2f", I2Parsed);

                        Log.i(TAG, "GET succeeded: P2 " + restResponse.getData().asJSONObject().getString("P2"));
                        P2=restResponse.getData().asJSONObject().getString("P2");
                        Double P2Parsed = Double.parseDouble(P2);
                        P2String = String.format("%,.2f", P2Parsed);

                        Log.i(TAG, "GET succeeded: T2 " + restResponse.getData().asJSONObject().getString("T2"));
                        T2=restResponse.getData().asJSONObject().getString("T2");
                        Double T2Parsed = Double.parseDouble(T2);
                        T2String = String.format("%,.2f", T2Parsed);

                        Log.i(TAG, "GET succeeded: Cost " + restResponse.getData().asJSONObject().getString("Cost"));
                        Cost=restResponse.getData().asJSONObject().getString("Cost");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                apiFailure -> Log.e(TAG, "GET failed.", apiFailure)
        );
    }
}