package edu.wit.karen.seniorprojectrev1;

import android.content.BroadcastReceiver;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationPublisher extends BroadcastReceiver {


    public void onReceive(Context context, Intent intent) {

        Log.e("NotificatonPublisher", "Notification Publisher Called");
    }
}
