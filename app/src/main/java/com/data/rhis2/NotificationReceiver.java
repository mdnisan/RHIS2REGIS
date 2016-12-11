package com.data.rhis2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import DataSync.SyncScheduler;

/**
 * Created by Nisan on 7/26/2016.
 */
public class NotificationReceiver extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                //Cry about not being clicked on
            } else if (extras.getBoolean("NotiClick")) {
                showAllert();
            }

        }


    }

    private void showAllert() {
        AlertDialog.Builder adb = new AlertDialog.Builder(NotificationReceiver.this);

        adb.setTitle("ডাটা সিঙ্ক");
        adb.setMessage("আপনি কি ডাটা সিঙ্ক বন্ধ করতে চান?");
        adb.setNegativeButton("না", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // cancelNotification(0);
                finish();

            }
        });
        adb.setPositiveButton("হ্যাঁ", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                cancelNotification(0);
                finish();

            }
        });
        adb.show();
    }

    public void cancelNotification(int notificationId) {

        if (Context.NOTIFICATION_SERVICE != null) {
            SyncScheduler.stop(getApplicationContext());
            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);

            Notification Notif = new Notification.Builder(this)
                    .setContentText("সিঙ্ক বন্ধ হয়েছে।")
                    .setSmallIcon(R.drawable.rhislogo_bit)
                    .build();
            nMgr.notify(0, Notif);
            // nMgr.cancel(notificationId);
        }
    }
}
