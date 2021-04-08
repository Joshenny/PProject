package com.example.project;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.project.Beacon.BeaconDetect;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class UImain extends FirebaseMessagingService {
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.i("MyFirebaseService","token "+s);
    }

   private void sendNotification(String message){
       Intent intent=new Intent(this, BeaconDetect.class);
       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
       PendingIntent pendingIntent=PendingIntent.getActivity(this, 0,intent,PendingIntent.FLAG_ONE_SHOT);

       String channelId="default_notification_channel_id";
       Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
       NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder(this, channelId)
               .setSmallIcon(R.drawable.ic_launcher_background)
               .setContentTitle("Hello")
               .setContentText(message)
               .setAutoCancel(true)
               .setSound(defaultSoundUri)
               .setContentIntent(pendingIntent);

       NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
       notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
       // Since android Oreo notification channel is needed.
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           NotificationChannel channel = new NotificationChannel(channelId,
                   "Channel human readable title",
                   NotificationManager.IMPORTANCE_DEFAULT);
           notificationManager.createNotificationChannel(channel);
       }
   }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getNotification() != null) {
            Log.i("MyFirebaseService","title "+remoteMessage.getNotification().getTitle());
            Log.i("MyFirebaseService","body "+remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getTitle());
        }
    }
}

