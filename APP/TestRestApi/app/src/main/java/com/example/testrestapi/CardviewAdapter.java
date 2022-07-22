package com.example.testrestapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.api.rest.RestOptions;
import com.amplifyframework.core.Amplify;

import java.util.List;

public class CardviewAdapter extends RecyclerView.Adapter<CardviewAdapter.CardviewViewHolder> {
    private final String TAG = CardviewAdapter.class.getSimpleName();
    private List<InfoCardview> cardviewInfoList;
    public ServerListener serverListener;
    public Context context;
    public Activity activity;
    private String Sdev1,Sdev2,Sdev3,Sdev4;
    public CardviewAdapter(Context context, List<InfoCardview> cardviewInfoList, Activity activity) {
        this.cardviewInfoList = cardviewInfoList;
        this.context = context;
        this.activity=activity;
    }
    @Override
    public int getItemViewType(int position){
        return position;
    }
    @Override
    public CardviewViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        int position=viewType;
        int type = cardviewInfoList.get(position).getType();
        View itemView;
        if(type ==1) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview1, parent, false);
        }else if(type==2){
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview2, parent, false);
        }else if(type==3){
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview3, parent, false);
        }else{
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview4, parent, false);
        }
        return new CardviewViewHolder(itemView,type);
    }
    @Override
    public void onBindViewHolder(CardviewViewHolder holder, int position) {
        InfoCardview device = cardviewInfoList.get(position);
        if(device.getType()==1){
            int imageResId = getDrawableResIdByName(device.getFlagName());
            holder.Title.setText(device.name + " ");
            if(device.name.equals("Energy")){
                //Log.i(TAG, "Set Text Energy" + E);
                if(device.getParameter()==null){
                    holder.paramater.setText("0"+" kWh");
                }else{
                    holder.paramater.setText(device.getParameter()+" kWh");
                }
            }
            if(device.name.equals("Voltage")){
                //Log.i(TAG, "Set Text Voltage" + U);
                if(device.getParameter()==null){
                    holder.paramater.setText("0"+" V");
                }else{
                    holder.paramater.setText(device.getParameter()+" V");
                }
            }
            if(device.name.equals("Current")){
                //Log.i(TAG, "Set Text Current" + I);
                if(device.getParameter()==null){
                    holder.paramater.setText("0"+" A");
                }else{
                    holder.paramater.setText(device.getParameter()+" A");
                }
            }
            if(device.name.equals("Frequency")){
                //Log.i(TAG, "Set Text Frequency" + F);
                if(device.getParameter()==null){
                    holder.paramater.setText("0"+ " Hz");
                }else{
                    holder.paramater.setText(device.getParameter()+ " Hz");
                }
            }
            holder.imageView.setImageResource(imageResId);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(position==0){
                        energy();
                    }else if(position==1){
                        voltage();
                    }else if(position==2){
                        current();
                    }else{
                        frequency();
                    }
                }
            });
        }else if(device.getType()==2){
            int imageResId = getDrawableResIdByName(device.getFlagName());
            holder.Title.setText(device.name + " ");
            holder.imageView.setImageResource(imageResId);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String username = Amplify.Auth.getCurrentUser().getUsername();

                    if(username.equals("hoang")) {
                        if(position==0){
                            device1();
                        }else{
                            device2();
                        }
                    }else if(username.equals("do")){
                        if(position==0){
                            device3();
                        }else{
                            device4();
                        }
                    }
                }
            });
            Log.i(TAG, "device.getParameter()" + device.getParameter());
            String status = device.getParameter();
            if(status!=null) {
                if (status.equals("1")) {
                    holder.sw1.setChecked(true);
                } else {
                    holder.sw1.setChecked(false);
                }
            }

            holder.sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    String username = Amplify.Auth.getCurrentUser().getUsername();
                    if(username.equals("hoang")) {
                        String enpoint = "Sdev" +String.valueOf( position + 1);
                        if(isChecked){
                            String data = "{\"command\":\"D"+String.valueOf( position + 1)+"ON\"}";
                            RestOptions options = RestOptions.builder()
                                    .addPath("/todo/"+enpoint)
                                    .addBody(data.getBytes())
                                    .build();

                            Amplify.API.post(options,
                                    response -> Log.i(TAG, "POST succeeded: " + response.getData().asString()),
                                    error -> Log.e(TAG, "POST failed.", error)
                            );
                            Toast.makeText(context,device.name+" ON",Toast.LENGTH_SHORT).show();
                        }else{
                            String data = "{\"command\":\"D"+String.valueOf( position + 1)+"OFF\"}";
                            RestOptions options = RestOptions.builder()
                                    .addPath("/todo/"+enpoint)
                                    .addBody(data.getBytes())
                                    .build();

                            Amplify.API.post(options,
                                    response -> Log.i(TAG, "POST succeeded: " + response.getData().asString()),
                                    error -> Log.e(TAG, "POST failed.", error)
                            );
                            //myData.child(resultemail).child("Control").child(device.name).setValue("0");
                            Toast.makeText(context,device.name+" OFF",Toast.LENGTH_SHORT).show();
                        }
                    }else if(username.equals("do")){
                        String enpoint = "Sdev" +String.valueOf( position + 3);
                        if(isChecked){
                            String data = "{\"command\":\"D"+String.valueOf( position + 3)+"ON\"}";
                            RestOptions options = RestOptions.builder()
                                    .addPath("/todo/"+enpoint)
                                    .addBody(data.getBytes())
                                    .build();

                            Amplify.API.post(options,
                                    response -> Log.i(TAG, "POST succeeded: " + response.getData().asString()),
                                    error -> Log.e(TAG, "POST failed.", error)
                            );
                            Toast.makeText(context,device.name+" ON",Toast.LENGTH_SHORT).show();
                        }else{
                            String data = "{\"command\":\"D"+String.valueOf( position + 3)+"OFF\"}";
                            RestOptions options = RestOptions.builder()
                                    .addPath("/todo/"+enpoint)
                                    .addBody(data.getBytes())
                                    .build();

                            Amplify.API.post(options,
                                    response -> Log.i(TAG, "POST succeeded: " + response.getData().asString()),
                                    error -> Log.e(TAG, "POST failed.", error)
                            );
                            //myData.child(resultemail).child("Control").child(device.name).setValue("0");
                            Toast.makeText(context,device.name+" OFF",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }else if(device.getType()==3){
            int imageResId = getDrawableResIdByName(device.getFlagName());
            if(position==0){
                holder.tvPhone.setText("+84399846623");
                holder.tvUsername.setText("trung do");
            }else{
                holder.tvPhone.setText("+84797818548");
                holder.tvUsername.setText("nhat hoang");
            }
            holder.Title.setText(device.name + " ");
            holder.imageView.setImageResource(imageResId);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(position==0){
                        gotouser1();
                    }else{
                        gotouser2();
                    }

                }
            });
        }else{
            int imageResId = getDrawableResIdByName(device.getFlagName());
            holder.Title.setText(device.name + " ");
            if(device.name.equals("Energy")){
                //Log.i(TAG, "Set Text Energy" + E);
                if(device.getParameter()==null){
                    holder.paramater.setText("0"+" kWh");
                }else{
                    holder.paramater.setText(device.getParameter()+" kWh");
                }
            }
            if(device.name.equals("Voltage")){
                //Log.i(TAG, "Set Text Voltage" + U);
                if(device.getParameter()==null){
                    holder.paramater.setText("0"+" V");
                }else{
                    holder.paramater.setText(device.getParameter()+" V");
                }
            }
            if(device.name.equals("Current")){
                //Log.i(TAG, "Set Text Current" + I);
                if(device.getParameter()==null){
                    holder.paramater.setText("0"+" A");
                }else{
                    holder.paramater.setText(device.getParameter()+" A");
                }
            }
            if(device.name.equals("Frequency")){
                //Log.i(TAG, "Set Text Frequency" + F);
                if(device.getParameter()==null){
                    holder.paramater.setText("0"+ " Hz");
                }else{
                    holder.paramater.setText(device.getParameter()+ " Hz");
                }
            }
            if(device.name.equals("Threshold")){
                //Log.i(TAG, "Set Text Frequency" + F);
                if(device.getParameter()==null){
                    holder.paramater.setText("0"+ " W");
                }else{
                    holder.paramater.setText(device.getParameter()+ " W");
                }
            }
            if(device.name.equals("Power")){
                //Log.i(TAG, "Set Text Frequency" + F);
                if(device.getParameter()==null){
                    holder.paramater.setText("0"+ " W");
                }else{
                    holder.paramater.setText(device.getParameter()+ " W");
                }
            }
            holder.imageView.setImageResource(imageResId);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(position==0){
                        //energy();
                    }else if(position==1){

                    }else if(position==2){
                        //current();
                        voltage();
                    }else if(position==3){
                        //frequency();
                    }else if(position==4){
                        frequency();
                    }else{

                    }
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return cardviewInfoList.size();
    }
    public int getDrawableResIdByName(String resName)  {
        String pkgName = context.getPackageName();
        int resID =context.getResources().getIdentifier(resName , "drawable", pkgName);
        //Log.i(MainActivity.LOG_TAG, "Res Name: "+ resName+"==> Res ID = "+ resID);
        return resID;
    }
    public void url(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        context.startActivity(intent);
    }
    protected void energy(){
        Intent intent =new Intent(context, ChartEnergyActivity.class);
        context.startActivity(intent);
    }
    protected void voltage(){
        Intent intent =new Intent(context, ChartVoltageActivity.class);
        context.startActivity(intent);
    }
    protected void current(){
        Intent intent =new Intent(context, ChartCurrentActivity.class);
        context.startActivity(intent);
    }
    protected void frequency(){
        Intent intent =new Intent(context, ChartFrequencyActivity.class);
        context.startActivity(intent);
    }

    protected void device1(){
        Intent intent =new Intent(context, Device1Activity.class);
        context.startActivity(intent);
    }
    protected void device2(){
        Intent intent =new Intent(context, Device2Activity.class);
        context.startActivity(intent);
    }
    protected void device3(){
        Intent intent =new Intent(context, Device3Activity.class);
        context.startActivity(intent);
    }
    protected void device4(){
        Intent intent =new Intent(context, Device4Activity.class);
        context.startActivity(intent);
    }
    protected void gotouser1(){
        Intent intent =new Intent(context, MainActivity.class);
        intent.putExtra("username","do");
        context.startActivity(intent);
    }
    protected void gotouser2(){
        Intent intent =new Intent(context, MainActivity.class);
        intent.putExtra("username","hoang");
        context.startActivity(intent);
    }

    public class CardviewViewHolder extends RecyclerView.ViewHolder {
        protected TextView Title,paramater,tvUsername,tvPhone;
        protected ImageView imageView;
        protected Switch sw1;
        public CardviewViewHolder(View itemView, int type) {
            super(itemView);
            switch (type) {
                case 1:
                    paramater = (TextView) itemView.findViewById(R.id.text);
                    Title = (TextView) itemView.findViewById(R.id.title_textview);
                    imageView = (ImageView) itemView.findViewById(R.id.flagdevice);
                    break;
                case 2:
                    sw1=itemView.findViewById(R.id.sw1);
                    Title = (TextView) itemView.findViewById(R.id.title_textview);
                    imageView = (ImageView) itemView.findViewById(R.id.flagdevice);
                    break;
                case 3:
                    Title = (TextView) itemView.findViewById(R.id.title_textview);
                    tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
                    tvPhone = (TextView) itemView.findViewById(R.id.tvPhone);
                    imageView = (ImageView) itemView.findViewById(R.id.flagdevice);
                    break;
                case 4:
                    paramater = (TextView) itemView.findViewById(R.id.text);
                    Title = (TextView) itemView.findViewById(R.id.title_textview);
                    imageView = (ImageView) itemView.findViewById(R.id.flagdevice);
                    break;
            }
        }
    }
    interface ServerListener{
        void resetAdapter(List <InfoCardview> cardviewInfoList);
    }
    public void setServerListener(ServerListener serverListener){
        this.serverListener=serverListener;
    }

}
