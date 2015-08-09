package DataService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.data.rhis2.DataSyncService;
import Common.Connection;
import static DataSync.Log.logInfo;

/**
 * Created by TanvirHossain on 08/03/2015.
 */

public class SyncReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent syncService = new Intent(context, SyncRebuildDatabase.class);
        context.startService(syncService);
   }
}