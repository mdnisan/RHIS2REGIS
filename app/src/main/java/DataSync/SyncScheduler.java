package DataSync;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;

import com.data.rhis2.NotificationReceiver;
import com.data.rhis2.R;
import com.data.rhis2.SyncBroadcastReceiver;
import static DataSync.Log.logInfo;
import static java.text.MessageFormat.format;

/**
 * Created by TanvirHossain on 08/03/2015.
 */
import static org.joda.time.DateTimeConstants.MILLIS_PER_MINUTE;
import static org.joda.time.DateTimeConstants.MILLIS_PER_SECOND;

public class SyncScheduler {
    public static final int SYNC_INTERVAL = 30 * MILLIS_PER_MINUTE;
    public static final int SYNC_START_DELAY = 5 * MILLIS_PER_SECOND;
    private static Listener<Boolean> logoutListener;

    public static void start(final Context context) {


        Intent intent = new Intent(context, NotificationReceiver.class);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        intent.putExtra("NotiClick", true);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rhislogo_bit);

            Notification Notif = new Notification.Builder(context)
                    .setContentTitle("RHIS")
                    .setContentText("ডাটা সিঙ্ক শুরু হয়েছে। ডাটা সিঙ্ক বন্ধ করার জন্য ক্লিক করুন")
                    .setSmallIcon(R.drawable.rhislogo_bit)
                            //.setLargeIcon(bitmap)
                    .setContentIntent(pIntent)
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .setSound(soundUri)
                    .build();

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, Notif);


            PendingIntent syncBroadcastReceiverIntent = PendingIntent.getBroadcast(context, 0, new Intent(context, SyncBroadcastReceiver.class), 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(
                    AlarmManager.RTC,
                    System.currentTimeMillis() + SYNC_START_DELAY,
                    SYNC_INTERVAL,
                    syncBroadcastReceiverIntent);

            logInfo(format("Scheduled to sync from server every {0} seconds.", SYNC_INTERVAL / 1000));

            attachListenerToStopSyncOnLogout(context);
        }
    }

    private static void attachListenerToStopSyncOnLogout(final Context context) {
        //ON_LOGOUT.removeListener(logoutListener);
        logoutListener = new Listener<Boolean>() {
            public void onEvent(Boolean data) {
                logInfo("User is logged out. Stopping RHIS Sync scheduler.");
                stop(context);
            }
        };
        //ON_LOGOUT.addListener(logoutListener);
    }

    public static void startOnlyIfConnectedToNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {


            start(context);
        } else {
            logInfo("Device not connected to network so not starting sync scheduler.");
        }
    }

    public static void stop(Context context) {
        PendingIntent syncBroadcastReceiverIntent = PendingIntent.getBroadcast(context, 0, new Intent(context, SyncBroadcastReceiver.class), 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(syncBroadcastReceiverIntent);

        logInfo("Unscheduled sync.");
    }
}
