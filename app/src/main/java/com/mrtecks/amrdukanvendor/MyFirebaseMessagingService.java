package com.mrtecks.amrdukanvendor;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {

        SharePreferenceUtils.getInstance().saveString("token", s);

        Log.d("toekn", s);


        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d("asdasd", remoteMessage.getData().toString());

        Intent registrationComplete = new Intent("count");

        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);


        JSONObject object = null;
        object = new JSONObject(remoteMessage.getData());

        try {
            handleNotification(object.getString("message"));
            Intent intent = new Intent("cancelintent");
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onMessageReceived(remoteMessage);
    }

    private void handleNotification(String message) {

        Log.d("notificationData", message);
        String idChannel = "southman messages";
        Intent mainIntent;

        mainIntent = new Intent(Bean.getContext(), Splash.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(Bean.getContext(), 0, mainIntent, 0);

        NotificationManager mNotificationManager = (NotificationManager) Bean.getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel mChannel = null;
        // The id of the channel.

        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationCompat.Builder builder;

        if (isAppRunning(this)) {
            builder = new NotificationCompat.Builder(Bean.getContext(), idChannel);
            builder.setContentTitle(Bean.getContext().getString(R.string.app_name))
                    .setSmallIcon(R.drawable.logo)
                    .setAutoCancel(true)
                    //.setSound(Uri.parse("android.resource://"
                    //        + getPackageName() + "/" + R.raw.sound))
                    .setContentIntent(pendingIntent)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(Html.fromHtml(message)))
                    .setContentText(Html.fromHtml(message));
        } else {
            builder = new NotificationCompat.Builder(Bean.getContext(), idChannel);
            builder.setContentTitle(Bean.getContext().getString(R.string.app_name))
                    .setSmallIcon(R.drawable.logo)
                    .setContentIntent(pendingIntent)
                    //.setSound(Uri.parse("android.resource://"
                    //        + getPackageName() + "/" + R.raw.sound))
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(Html.fromHtml(message)))
                    .setAutoCancel(true)
                    .setContentText(Html.fromHtml(message));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Uri soundUri = Uri.parse(
                    "android.resource://" +
                            getApplicationContext().getPackageName() +
                            "/" +
                            R.raw.sound);

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();

            mChannel = new NotificationChannel(idChannel, Bean.getContext().getString(R.string.app_name), importance);
            // Configure the notification channel.
            mChannel.setDescription(Bean.getContext().getString(R.string.alarm_notification));
            mChannel.enableLights(true);
            //mChannel.setSound(soundUri, audioAttributes);
            mChannel.setLightColor(Color.RED);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(mChannel);
            }
        } else {
            builder.setContentTitle(Bean.getContext().getString(R.string.app_name))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(ContextCompat.getColor(Bean.getContext(), R.color.transparent))
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setLights(Color.YELLOW, 500, 5000)
                    //.setSound(Uri.parse("android.resource://"
                    //        + getPackageName() + "/" + R.raw.sound))
                    .setAutoCancel(true);
        }
        if (mNotificationManager != null) {
            mNotificationManager.notify(1, builder.build());
        }


    }


    public static boolean isAppRunning(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_PERCEPTIBLE) {
                    return true;
                }
            }
        }
        return false;
    }
}
