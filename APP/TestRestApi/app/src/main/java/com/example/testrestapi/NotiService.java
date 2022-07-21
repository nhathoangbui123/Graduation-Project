package com.example.testrestapi;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.amplifyframework.api.rest.RestOptions;
import com.amplifyframework.core.Amplify;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NotiService extends Service {
    private final String TAG = NotiService.class.getSimpleName();
    public static final  String CHANNEL_1_ID = "channel1";
    private final long[] pattern = { 100, 1000, 1000, 1000 };
    private NotificationManagerCompat notificationManagerCompat;
    private String T1F, T2F, T3F, T4F;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    public NotiService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onCreate(){
        super.onCreate();
        notificationManagerCompat = NotificationManagerCompat.from(this);
        mHandler.post(runnable);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        createNotificationChannels();
        return super.onStartCommand(intent, flags, startId);
    }



    // Destroy
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    private void sendNoti(String device, int notiid) {
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.home)
                .setContentTitle("Device threshold")
                .setContentText(device+" is over threshold")
                .setVibrate(pattern)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .build();

        notificationManagerCompat.notify(notiid, notification);
    }
    private void createNotificationChannels()  {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is channel 1");

            NotificationManager manager = this.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }
    private void get_data() {
        RestOptions options = RestOptions.builder()
                .addPath("/todo")
                .build();
        Amplify.API.get(options,
                restResponse -> {
                    try {
                        Log.i(TAG, "GET succeeded: T1F " + restResponse.getData().asJSONObject().getString("T1F"));
                        T1F=restResponse.getData().asJSONObject().getString("T1F");
                        Log.i(TAG, "GET succeeded: T2F " + restResponse.getData().asJSONObject().getString("T2F"));
                        T2F=restResponse.getData().asJSONObject().getString("T2F");
                        Log.i(TAG, "GET succeeded: T3F " + restResponse.getData().asJSONObject().getString("T3F"));
                        T3F=restResponse.getData().asJSONObject().getString("T3F");
                        Log.i(TAG, "GET succeeded: T4F " + restResponse.getData().asJSONObject().getString("T4F"));
                        T4F=restResponse.getData().asJSONObject().getString("T4F");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                apiFailure -> Log.e(TAG, "GET failed.", apiFailure)
        );
    }
    private final Runnable runnable =
            new Runnable() {
                public void run() {
                    get_data();
                    if(T1F!=null&&T1F.equals("1")){
                        sendNoti("Device 1",1);
                    }
                    if(T2F!=null&&T2F.equals("1")){
                        sendNoti("Device 2",2);
                    }
                    if(T3F!=null&&T3F.equals("1")){
                        sendNoti("Device 3",3);
                    }
                    if(T4F!=null&&T4F.equals("1")){
                        sendNoti("Device 4",4);
                    }
                    mHandler.postDelayed(this, 2000);
                }
            };
}