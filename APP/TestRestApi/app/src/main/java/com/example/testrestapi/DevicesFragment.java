package com.example.testrestapi;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.api.rest.RestOptions;
import com.amplifyframework.core.Amplify;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class DevicesFragment extends Fragment {
    private final String TAG = DevicesFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private CardviewAdapter cardviewAdapter;
    private String Sdev1,Sdev2,Sdev3,Sdev4;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_devices, container, false);

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
    private final Runnable runnable =
            new Runnable() {
                public void run() {
                    get_data();
                    List<InfoCardview> device = createList();
                    cardviewAdapter = new CardviewAdapter(getContext(),device,getActivity());
                    recyclerView.setAdapter(cardviewAdapter);
                    mHandler.postDelayed(this, 1000);
                }
            };
    private List<InfoCardview> createList() {
        List<InfoCardview> list = new ArrayList<InfoCardview>();
        InfoCardview dev1 = new InfoCardview("Device 1","unplugged",2,Sdev1);
        InfoCardview dev2 = new InfoCardview("Device 2","unplugged",2,Sdev2);
        InfoCardview dev3 = new InfoCardview("Device 3","unplugged",2,Sdev3);
        InfoCardview dev4 = new InfoCardview("Device 4","unplugged",2,Sdev4);
        list.add(dev1);
        list.add(dev2);
        list.add(dev3);
        list.add(dev4);
        return list;
    }
    private void get_data() {
        RestOptions options = RestOptions.builder()
                .addPath("/todo")
                .build();
        Amplify.API.get(options,
                restResponse -> {
                    try {
                        Log.i(TAG, "GET succeeded: Sdev1 " + restResponse.getData().asJSONObject().getString("Sdev1"));
                        Sdev1=restResponse.getData().asJSONObject().getString("Sdev1");
                        Log.i(TAG, "GET succeeded: Sdev2 " + restResponse.getData().asJSONObject().getString("Sdev2"));
                        Sdev2=restResponse.getData().asJSONObject().getString("Sdev2");
                        Log.i(TAG, "GET succeeded: Sdev3 " + restResponse.getData().asJSONObject().getString("Sdev3"));
                        Sdev3=restResponse.getData().asJSONObject().getString("Sdev3");
                        Log.i(TAG, "GET succeeded: Sdev4 " + restResponse.getData().asJSONObject().getString("Sdev4"));
                        Sdev4=restResponse.getData().asJSONObject().getString("Sdev4");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                apiFailure -> Log.e(TAG, "GET failed.", apiFailure)
        );
    }
}