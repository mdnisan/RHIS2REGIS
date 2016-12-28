package com.data.rhis2;

        import android.app.Notification;
        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.app.ProgressDialog;
        import android.app.Service;
        import android.content.Intent;
        import android.net.ConnectivityManager;
        import android.os.AsyncTask;
        import android.os.IBinder;
        import android.os.PowerManager;

        import Common.Connection;
        import Common.Global;

/*
 * Created by TanvirHossain on 08/03/2015.
 */
public class SyncHealthID_Service extends Service
{
    //Connection C;
    Global g;
    private NotificationManager mManager;

    @Override
    public IBinder onBind(Intent arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*@Override
    public void onCreate()
    {
        // TODO Auto-generated method stub
        super.onCreate();
        C=new Connection(this);
    }*/

    private void handleIntent(Intent intent) {
        // obtain the wake lock
        /*
                PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
                mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, Const.TAG);
                mWakeLock.acquire();
        */

        // check the global background data setting
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (!cm.getBackgroundDataSetting()) {
            stopSelf();
            return;
        }

        //C = new Connection(this);
        g = Global.getInstance();
        // do the actual work, in a separate thread
        new DataSyncTask().execute(g.getProvType()+"-"+g.getProvCode());
    }

    private class DataSyncTask extends AsyncTask<String, Void, Void> {
        String ProviderType = "";
        String ProviderCode = "";

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(String... params) {
            final String[] P = params[0].toString().split("-");
            ProviderType = P[0];
            ProviderCode = P[1];

            try {
                new Thread() {
                    public void run() {
                        try {

                            Connection.Sync_HealthID(ProviderType, ProviderCode);
                        } catch (Exception e) {

                        }
                    }
                }.start();
                //Sync Database

            } catch (Exception e) {

            }

            // do stuff!
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // handle your data
            stopSelf();
        }
    }

    //@SuppressWarnings("static-access")
    @Override
    public void onStart(Intent intent, int startId)
    {
        handleIntent(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleIntent(intent);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
        //mWakeLock.release();
    }

}

