package com.example.testrestapi;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amplifyframework.core.Amplify;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileFragment extends Fragment {
    private final String TAG = ProfileFragment.class.getSimpleName();
    private TextView username, phone, email;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        username = view.findViewById(R.id.username);
        phone = view.findViewById(R.id.phone);
        email = view.findViewById(R.id.email);
        Amplify.Auth.fetchUserAttributes(
                attributes -> {
                    Log.i(TAG, "User attributes = " + attributes.toString());

                    username.setText("User Name: "+attributes.get(2).getValue().toString());
                    Log.i(TAG, "attributes.get(2).getValue(); " + attributes.get(2).getValue());

                    phone.setText("Phone Number: "+attributes.get(4).getValue().toString());
                    Log.i(TAG, "attributes.get(4).getValue(); " + attributes.get(4).getValue());

                    email.setText("Email: "+ attributes.get(5).getValue().toString());
                    Log.i(TAG, "attributes.get(5).getValue(); " + attributes.get(5).getValue());
                },
                error -> Log.e(TAG, "Failed to fetch user attributes.", error)
        );
        return view;
    }
}