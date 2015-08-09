package com.data.rhis2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.data.rhis2.DataSyncService;

import Common.Connection;

import static DataSync.Log.logInfo;

/**
 * Created by TanvirHossain on 08/03/2015.
 */

public class SyncBroadcastReceiver extends BroadcastReceiver {
    Connection C;
    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(getApplicationContext(), "msg msg", Toast.LENGTH_SHORT).show();
        logInfo("Sync alarm triggered. Trying to Sync.");
        //C= new Connection(context);

        Intent syncService = new Intent(context, DataSyncService.class);
        context.startService(syncService);

        /*
        String TableName = "";
        String VariableList = "";
        String ColumnList = "";
        String UniqueField = "";

        String ResponseString="Status:";
        String response;

        try {
            TableName = "Household";
            VariableList = "Div, Dist, Upz, UN, wardNew, wardOld, Mouza, FWAUnit, Vill, EPIBlock, PAddr, PermaAddress, HHNo, Religion, VGFCard, StartTime, EndTime, Lat, Lon, UserId, EnDt, Upload";
            String[] H = C.GenerateArrayList(VariableList, TableName);
            response = C.UploadData(H, TableName , VariableList , "Div, Dist, Upz, UN, Mouza, Vill, HHNo");

            TableName = "Member";
            VariableList = "Dist, Upz, UN, Mouza, Vill, HHNo, SNo, HealthID, NameEng, NameBang, Rth, HaveNID, NID, NIDStatus, HaveBR, BRID, BRIDStatus, MobileNo1, MobileNo2, DOB, Age, DOBSource, BPlace, FNo, Father, MNo, Mother, Sex, MS, SPNO1, SPNO2, SPNO3, SPNO4, ELCONo, ELCODontKnow, EDU, Rel, Nationality, OCP, StartTime, EnType, EnDate, ExType, ExDate, EndTime, Lat, Lon, UserId, EnDt, Upload";
            String[] M = C.GenerateArrayList(VariableList, TableName);
            response = C.UploadData(M, TableName , VariableList , "Dist, Upz, UN, Mouza, Vill, HHNo, SNo");

            //Sync Database

        } catch (Exception e) {

        }
        */


        /*UpdateActionsTask updateActionsTask = new UpdateActionsTask(
                context,
                org.ei.drishti.Context.getInstance().actionService(),
                org.ei.drishti.Context.getInstance().formSubmissionSyncService(), new SyncProgressIndicator());

        updateActionsTask.updateFromServer(new SyncAfterFetchListener());*/
    }
}