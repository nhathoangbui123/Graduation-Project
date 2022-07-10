package com.example.testrestapi;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class HelpFragment extends Fragment {
    private final String TAG = HelpFragment.class.getSimpleName();
    private TextView phonead1, phonead2, email1, email2;
    private ImageView ad1, ad2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_help, container, false);
        phonead1 = view.findViewById(R.id.phoneadmin1);
        ad1 = view.findViewById(R.id.imageadmin1);
        email1 = view.findViewById(R.id.emailadmin1);

        phonead2 = view.findViewById(R.id.phoneadmin2);
        ad2 = view.findViewById(R.id.imageadmin2);
        email2 = view.findViewById(R.id.emailadmin2);

        phonead1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = "+84399846623";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        });
        ad1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/trungdo18122000"));
                startActivity(facebookIntent);
            }
        });
        email1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND );
                intent.putExtra(Intent.EXTRA_EMAIL , new String[]{"do@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT , "Support Home Energy App");
                intent.putExtra(Intent.EXTRA_TEXT , "Please enter information");
                intent.setType("message/rfc822");
                startActivity(Intent.createChooser(intent , "Choose Your Account : "));
            }
        });
        phonead2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = "+84797818548";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        });
        ad2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/nhathoang151020"));
                startActivity(facebookIntent);
            }
        });
        email2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND );
                intent.putExtra(Intent.EXTRA_EMAIL , new String[]{"nhathoangbui123@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT , "Support Home Energy App");
                intent.putExtra(Intent.EXTRA_TEXT , "Please enter information");
                intent.setType("message/rfc822");
                startActivity(Intent.createChooser(intent , "Choose Your Account : "));
            }
        });
        return view;
    }
}