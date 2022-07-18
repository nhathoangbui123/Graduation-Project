package com.example.testrestapi;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amplifyframework.api.rest.RestOptions;
import com.amplifyframework.core.Amplify;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class AdminFragment extends Fragment {
    private final String TAG = AdminFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private CardviewAdapter cardviewAdapter;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private String username1, username2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rcview);
        //List<InfoCardview> device = createList();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),1);
        recyclerView.setLayoutManager(gridLayoutManager);

//        cardviewAdapter = new CardviewAdapter(getContext(),device,getActivity());
//        recyclerView.setAdapter(cardviewAdapter);
        mHandler.post(runnable);
        return view;
    }
    private final Runnable runnable =
            new Runnable() {
                public void run() {
                    get_user();
                    List<InfoCardview> device = createList();
                    cardviewAdapter = new CardviewAdapter(getContext(),device,getActivity());
                    recyclerView.setAdapter(cardviewAdapter);
                    mHandler.postDelayed(this, 1000);
                }
            };
    private void get_user() {
        RestOptions options = RestOptions.builder()
                .addPath("/todo/user")
                .build();
        Amplify.API.get(options,
                restResponse -> {
                    try {
                        //get info
                        Log.i(TAG, "GET succeeded: " + restResponse.getData().asJSONObject().getJSONArray("users").getJSONObject(1).getString("Username"));
                        username1=restResponse.getData().asJSONObject().getJSONArray("users").getJSONObject(1).getString("Username");
                        username2=restResponse.getData().asJSONObject().getJSONArray("users").getJSONObject(2).getString("Username");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                apiFailure -> Log.e(TAG, "GET failed.", apiFailure)
        );
    }
    private List<InfoCardview> createList() {
        List<InfoCardview> list = new ArrayList<InfoCardview>();
        InfoCardview User1 = new InfoCardview(username1,"user",3);
        InfoCardview User2 = new InfoCardview(username2,"user",3);

        list.add(User1);
        list.add(User2);
        return list;
    }
}