package com.esiea.instafood;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;


public class NotiClass {

    //private int type;
    //private String message;

    public NotiClass(Context context, NotificationManager notificationManager, String message) {

        final NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(context, "ChannelID")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Notification")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager.notify(1, notiBuilder.build());
    }

    public NotiClass(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public NotiClass(Context context, String title, String message){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setTitle(title)
                .setIcon(R.mipmap.ic_launcher)
                .create().show();
    }
}
