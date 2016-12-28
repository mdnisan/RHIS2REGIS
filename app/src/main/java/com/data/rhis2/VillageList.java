package com.data.rhis2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import Common.Connection;
import Common.Global;
import DataSync.SyncScheduler;

//import Common.ConnectionOrig;

public class VillageList extends Activity {
    private static final int UPDATEDONE = 900;
    private ProgressDialog pDialog;
    Connection C;
    Global g;
    private ProgressDialog progress;
    String message = "";
    int jumpTime = 0;

    private boolean IsUserAHI() {
        if (g.getProvType().equalsIgnoreCase("11"))
            return true;
        else
            return false;


    }

    private boolean IsUserHI() {
        if (g.getProvType().equalsIgnoreCase("12"))
            return true;
        else
            return false;


    }

    private int mDay;
    private int mMonth;
    private int mYear;
    static final int DATE_DIALOG = 1;
    static final int TIME_DIALOG = 2;
    int DateID = 0;

    //Top menu
    //--------------------------------------------------------------------------------------------------
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (g.getProvCode().equals("12"))
            inflater.inflate(R.menu.mnuexit1, menu);
        else
            inflater.inflate(R.menu.mnuexit, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder adb = new AlertDialog.Builder(VillageList.this);
        if (IsUserHI()) {
            switch (item.getItemId()) {


                case R.id.menuarea:

                    if (Connection.haveNetworkConnection(this)) {
                        Intent intent = new Intent(getApplicationContext(), Aeaadownloaddata.class);
                        startActivity(intent);
                        netwoekAvailable = true;
                    } else {

                        Connection.MessageBox(VillageList.this, "Internet connection is not available.");
                        return true;
                    }
                    return true;


                case R.id.menuaddpeople:

                    if (Connection.haveNetworkConnection(this)) {
                        Intent intent = new Intent(getApplicationContext(), Villagepopup.class);
                        startActivity(intent);
                        netwoekAvailable = true;
                    } else {

                        Connection.MessageBox(VillageList.this, "Internet connection is not available.");
                        return true;
                    }
                    return true;

                case R.id.menuExit:
                    adb.setTitle("Exit");
                    adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান?");
                    adb.setNegativeButton("না", null);
                    adb.setPositiveButton("হাঁ", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            System.exit(0);
                        }
                    });
                    adb.show();
                    return true;
                case R.id.menuSync:
                    try {
                        //Check for Internet connectivity
                        //*******************************************************************************
                        boolean netwoekAvailable = false;
                        if (Connection.haveNetworkConnection(this)) {
                            netwoekAvailable = true;

                        } else {
                            netwoekAvailable = false;
                            Connection.MessageBox(VillageList.this, "Internet connection is not available.");
                            return true;
                        }


                        String ResponseString = "Status:";

                        // final ProgressDialog progDailog = ProgressDialog.show(VillageList.this, "", "অপেক্ষা করুন ...", true);

                        //  new Thread() {
                        //  public void run() {
                        progress = new ProgressDialog(VillageList.this);
                        progress.setMessage("Downloading");
                        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progress.setIcon(R.drawable.rhislogo2);
                        progress.setIndeterminate(false);
                        progress.setCancelable(false);
                        progress.setProgress(0);

                        progress.show();

                        final Thread t = new Thread() {
                            @Override
                            public void run() {

                                String TableName = "";
                                String VariableList = "";
                                String ColumnList = "";
                                String UniqueField = "";

                                String ResponseString = "Status:";
                                String response;

                                try {

                                    //**ready to use and need to check before use: 12 apr 2015
                                /*
                                String[] TableList={"household","member","visits"};
                                //Upload data to server
                                C.DatabaseTableDataSync(TableList);

                                //Update data on local database

                                //data sync between local and server
                                */
                                    message = "Data Syncing. Please Wait...";
                                    jumpTime = 1;
                                    progressHandler.sendMessage(progressHandler.obtainMessage());
                                    sleep(2);
                                    Upload(progress, message, progressHandler, jumpTime);
                                    Download(progress, message, progressHandler, jumpTime, true);
                                    //Download(progress, message, progressHandler, jumpTime);
                                    progress.dismiss();
                                    //MCH SERVICE

                                    //***************************************************************************************************************************
                                    //C.UploadJSON("Zilla", "ZillaId,ZillaName", "ZillaId");
                                    //***************************************************************************************************************************
                                    //C.DownloadJSON("Select \"ZillaId\",\"ZillaName\" from \"Zilla\"","\"Zilla\"", "\"ZillaId\",\"ZillaName\"", "\"ZillaId\"");
                                    //***************************************************************************************************************************


                                    //Data Update on local database from Server
                                    //                               String SQLStr = "";

                                    //Member Table
                                /*
                                SQLStr  = " Select h.Dist, h.Upz, h.UN, h.Mouza, h.Vill, h.ProvType, h.ProvCode, h.HHNo, h.SNo,h.HealthID, h.NameEng, h.NameBang, h.Rth, h.HaveNID, h.NID, h.NIDStatus, h.HaveBR,";
                                SQLStr += " h.BRID, h.BRIDStatus, h.MobileNo1, h.MobileNo2, h.DOB, h.Age, h.DOBSource, h.BPlace, h.FNo, h.Father, h.FDontKnow, h.MNo, h.Mother, h.MDontKnow, h.Sex, h.MS, h.SPNO1,";
                                SQLStr += " h.SPNO2, h.SPNO3, h.SPNO4, h.ELCONo, h.ELCODontKnow, h.EDU, h.Rel, h.Nationality, h.OCP, h.StartTime, h.EnType, h.EnDate, h.ExType, h.ExDate";
                                SQLStr += " from Village v";
                                SQLStr += " inner join ProviderArea a on v.ZILLAID+v.UPAZILAID+v.UNIONID+v.MOUZAID+v.VILLAGEID=a.zillaid+a.upazilaid+a.unionid+a.mouzaid+a.villageid";
                                SQLStr += " inner join ProviderAssignArea aa on a.ZILLAID+a.UPAZILAID+a.UNIONID+a.areacode=aa.zillaid+aa.upazilaid+aa.unionid+aa.areacode";
                                SQLStr += " inner join Member h on a.zillaid+a.upazilaid+a.unionid+a.mouzaid+a.villageid=h.dist+h.upz+h.un+h.Mouza+h.Vill";
                                SQLStr += " where h.Upload='3' and aa.provtype='"+ g.getProvType() +"' and aa.provcode='"+ g.getProvCode() +"' and v.ZillaID='"+ g.getDistrict() +"' and v.Upazilaid='"+ g.getUpazila() +"' and v.Unionid='"+ g.getUnion() +"'";
                                response = C.DataUpdate(SQLStr,"Member","Dist, Upz, UN, Mouza, Vill, ProvType, ProvCode, HHNo, SNo,HealthID, NameEng, NameBang, Rth, HaveNID, NID, NIDStatus, HaveBR, BRID, BRIDStatus, MobileNo1, MobileNo2, DOB, Age, DOBSource, BPlace, FNo, Father, FDontKnow, MNo, Mother, MDontKnow, Sex, MS, SPNO1, SPNO2, SPNO3, SPNO4, ELCONo, ELCODontKnow, EDU, Rel, Nationality, OCP, StartTime, EnType, EnDate, ExType, ExDate","Dist, Upz, UN, Mouza, Vill, ProvType,ProvCode, HHNo, SNo");
                                */

                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                //}
                                // Looper.loop();
                            }

                        };
                        t.start();
                        //Connection.MessageBox(VillageList.this,"তথ্য সফলভাবে ডাটাবেজ সার্ভারে আপলোড হয়েছে।");

                    } catch (Exception ex) {
                        Connection.MessageBox(VillageList.this, ex.getMessage());
                    }


                    return true;


            }

        } else {
            switch (item.getItemId()) {


            /*    case R.id.menuarea:

                    if (Connection.haveNetworkConnection(this)) {
                        Intent intent = new Intent(getApplicationContext(), Aeaadownloaddata.class);
                        startActivity(intent);
                        netwoekAvailable = true;
                    } else {

                        Connection.MessageBox(VillageList.this, "Internet connection is not available.");
                        return true;
                    }
                    return true;
                    */

                case R.id.menuaddpeople:

                    if (Connection.haveNetworkConnection(this)) {
                        Intent intent = new Intent(getApplicationContext(), Villagepopup.class);
                        startActivity(intent);
                        netwoekAvailable = true;
                    } else {

                        Connection.MessageBox(VillageList.this, "Internet connection is not available.");
                        return true;
                    }
                    return true;

                case R.id.menuExit:
                    adb.setTitle("Exit");
                    adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান?");
                    adb.setNegativeButton("না", null);
                    adb.setPositiveButton("হাঁ", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            System.exit(0);
                        }
                    });
                    adb.show();
                    return true;
                case R.id.menuSync:
                    try {
                        //Check for Internet connectivity
                        //*******************************************************************************
                        boolean netwoekAvailable = false;
                        if (Connection.haveNetworkConnection(this)) {
                            netwoekAvailable = true;

                        } else {
                            netwoekAvailable = false;
                            Connection.MessageBox(VillageList.this, "Internet connection is not available.");
                            return true;
                        }


                        String ResponseString = "Status:";

                        // final ProgressDialog progDailog = ProgressDialog.show(VillageList.this, "", "অপেক্ষা করুন ...", true);

                        //  new Thread() {
                        //  public void run() {
                        progress = new ProgressDialog(VillageList.this);
                        progress.setMessage("Downloading");
                        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progress.setIcon(R.drawable.rhislogo2);
                        progress.setIndeterminate(false);
                        progress.setCancelable(false);
                        progress.setProgress(0);

                        progress.show();

                        final Thread t = new Thread() {
                            @Override
                            public void run() {

                                String TableName = "";
                                String VariableList = "";
                                String ColumnList = "";
                                String UniqueField = "";

                                String ResponseString = "Status:";
                                String response;

                                try {

                                    //**ready to use and need to check before use: 12 apr 2015
                                /*
                                String[] TableList={"household","member","visits"};
                                //Upload data to server
                                C.DatabaseTableDataSync(TableList);

                                //Update data on local database

                                //data sync between local and server
                                */
                                    message = "Data Syncing. Please Wait...";
                                    jumpTime = 1;
                                    progressHandler.sendMessage(progressHandler.obtainMessage());
                                    sleep(2);
                                    Upload(progress, message, progressHandler, jumpTime);
                                    Download(progress, message, progressHandler, jumpTime, true);
                                    progress.dismiss();
                                    //MCH SERVICE

                                    //***************************************************************************************************************************
                                    //C.UploadJSON("Zilla", "ZillaId,ZillaName", "ZillaId");
                                    //***************************************************************************************************************************
                                    //C.DownloadJSON("Select \"ZillaId\",\"ZillaName\" from \"Zilla\"","\"Zilla\"", "\"ZillaId\",\"ZillaName\"", "\"ZillaId\"");
                                    //***************************************************************************************************************************


                                    //Data Update on local database from Server
                                    //                               String SQLStr = "";

                                    //Member Table
                                /*
                                SQLStr  = " Select h.Dist, h.Upz, h.UN, h.Mouza, h.Vill, h.ProvType, h.ProvCode, h.HHNo, h.SNo,h.HealthID, h.NameEng, h.NameBang, h.Rth, h.HaveNID, h.NID, h.NIDStatus, h.HaveBR,";
                                SQLStr += " h.BRID, h.BRIDStatus, h.MobileNo1, h.MobileNo2, h.DOB, h.Age, h.DOBSource, h.BPlace, h.FNo, h.Father, h.FDontKnow, h.MNo, h.Mother, h.MDontKnow, h.Sex, h.MS, h.SPNO1,";
                                SQLStr += " h.SPNO2, h.SPNO3, h.SPNO4, h.ELCONo, h.ELCODontKnow, h.EDU, h.Rel, h.Nationality, h.OCP, h.StartTime, h.EnType, h.EnDate, h.ExType, h.ExDate";
                                SQLStr += " from Village v";
                                SQLStr += " inner join ProviderArea a on v.ZILLAID+v.UPAZILAID+v.UNIONID+v.MOUZAID+v.VILLAGEID=a.zillaid+a.upazilaid+a.unionid+a.mouzaid+a.villageid";
                                SQLStr += " inner join ProviderAssignArea aa on a.ZILLAID+a.UPAZILAID+a.UNIONID+a.areacode=aa.zillaid+aa.upazilaid+aa.unionid+aa.areacode";
                                SQLStr +ie= " inner join Member h on a.zillaid+a.upazilaid+a.unionid+a.mouzaid+a.villageid=h.dist+h.upz+h.un+h.Mouza+h.Vill";
                                SQLStr += " where h.Upload='3' and aa.provtype='"+ g.getProvType() +"' and aa.provcode='"+ g.getProvCode() +"' and v.ZillaID='"+ g.getDistrict() +"' and v.Upazilaid='"+ g.getUpazila() +"' and v.Unionid='"+ g.getUnion() +"'";
                                response = C.DataUpdate(SQLStr,"Member","Dist, Upz, UN, Mouza, Vill, ProvType, ProvCode, HHNo, SNo,HealthID, NameEng, NameBang, Rth, HaveNID, NID, NIDStatus, HaveBR, BRID, BRIDStatus, MobileNo1, MobileNo2, DOB, Age, DOBSource, BPlace, FNo, Father, FDontKnow, MNo, Mother, MDontKnow, Sex, MS, SPNO1, SPNO2, SPNO3, SPNO4, ELCONo, ELCODontKnow, EDU, Rel, Nationality, OCP, StartTime, EnType, EnDate, ExType, ExDate","Dist, Upz, UN, Mouza, Vill, ProvType,ProvCode, HHNo, SNo");
                                */

                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                //}
                                // Looper.loop();
                            }

                        };
                        t.start();
                        //Connection.MessageBox(VillageList.this,"তথ্য সফলভাবে ডাটাবেজ সার্ভারে আপলোড হয়েছে।");

                    } catch (Exception ex) {
                        Connection.MessageBox(VillageList.this, ex.getMessage());
                    }


                    return true;


            }
        }
        return false;
    }

    android.os.Handler progressHandler = new android.os.Handler() {
        public void handleMessage(Message msg) {

            progress.incrementProgressBy(jumpTime);
            progress.setMessage(message);

        }

    };
           /* case R.id.menuSync:
                try
                {
                    //Check for Internet connectivity
                    /*//*******************************************************************************
     boolean netwoekAvailable=false;
     if (Connection.haveNetworkConnection(this)) {
     netwoekAvailable=true;

     } else {
     netwoekAvailable=false;
     Connection.MessageBox(VillageList.this, "Internet connection is not available.");
     return true;
     }


     String ResponseString="Status:";

     final ProgressDialog progDailog = ProgressDialog.show(
     VillageList.this, "", "অপেক্ষা করুন ...", true);

     new Thread() {
     public void run() {
     String TableName = "";
     String VariableList = "";
     String ColumnList = "";
     String UniqueField = "";

     String ResponseString="Status:";
     String response;

     try {

     /*//**ready to use and need to check before use: 12 apr 2015
     *//*
                                String[] TableList={"household","member","visits"};
                                //Upload data to server
                                C.DatabaseTableDataSync(TableList);

                                //Update data on local database

                                //data sync between local and server
                                *//*


                                //Data upload to central server
                                TableName = "Household";
                                VariableList = "Dist, Upz, UN, Mouza, Vill, PAddr, PermaAddress, ProvType, ProvCode, HHNo, Religion, VGFCard, subBlock, unit, HHHead, totalMem, StartTime, EndTime, Lat, Lon, UserId, EnDt, Upload";
                                String[] H = C.GenerateArrayList(VariableList, TableName);
                                *//*response = C.UploadData(H, TableName , VariableList , "Dist, Upz, UN, Mouza, Vill,ProvType,ProvCode, HHNo");*//*
                                response = C.UploadData(H, TableName , VariableList , "Dist, Upz, UN, Mouza, Vill, HHNo");

                                TableName = "Member";
                                VariableList = "Dist, Upz, UN, Mouza, Vill, ProvType,ProvCode, HHNo, SNo, HealthID, NameEng, NameBang, Rth, HaveNID, NID, NIDStatus, HaveBR, BRID, BRIDStatus, MobileNo1, MobileNo2, MobileYN, DOB, Age, DOBSource, BPlace, FNo, Father, FDontKnow, MNo, Mother, MDontKnow, Sex, MS, SPNO1, SPNO2, SPNO3, SPNO4, ELCONo, ELCODontKnow, EDU, Rel, Nationality, OCP, StartTime, EnType, EnDate, ExType, ExDate, EndTime, Lat, Lon, UserId, EnDt, Upload";
                                String[] M = C.GenerateArrayList(VariableList, TableName);
                                *//*response = C.UploadData(M, TableName , VariableList , "Dist, Upz, UN, Mouza, Vill, ProvType,ProvCode, HHNo, SNo");*//*
                                response = C.UploadData(M, TableName , VariableList , "Dist, Upz, UN, Mouza, Vill, HHNo, SNo");

                                TableName = "Visits";
                                VariableList = "Dist, Upz, UN, Mouza, Vill,ProvType,ProvCode, HHNo, VDate, VStatus, StartTime, EndTime, Lat, Lon, UserId, EnDt, Upload";
                                String[] V = C.GenerateArrayList(VariableList, TableName);
                               *//* response = C.UploadData(V, TableName , VariableList , "Dist, Upz, UN, Mouza, Vill,ProvType,ProvCode, HHNo, VDate");*//*
                                response = C.UploadData(V, TableName , VariableList , "Dist, Upz, UN, Mouza, Vill, HHNo, VDate");

                                TableName = "Ses";
                                VariableList = "Dist, Upz, UN, Mouza, Vill, ProvType, ProvCode, HHNo, status, Q1, Q11, Q2, Q21, Q3a, Q3b, Q3c, Q3d, Q3e, Q3f, Q3g, Q3h, Q3i, Q3j, Q3k, Q3l, Q3m, Q3n, Q3o, Q3p, Q4, Q41, Q5, Q51, Q6, Q61, Q7, Q71, Q8a, Q8b, Q8c, Q8d, Q8e, StartTime, EndTime, UserId, EnDt, Upload";
                                String[] S = C.GenerateArrayList(VariableList, TableName);
                                *//*response = C.UploadData(S, TableName , VariableList , "Dist, Upz, UN, Mouza, Vill,ProvType,ProvCode, HHNo");*//*
                                response = C.UploadData(S, TableName , VariableList , "Dist, Upz, UN, Mouza, Vill, HHNo");

                                //MCH SERVICE

                                /*//***************************************************************************************************************************
     //C.UploadJSON("Zilla", "ZillaId,ZillaName", "ZillaId");
     /*//***************************************************************************************************************************
     //C.DownloadJSON("Select \"ZillaId\",\"ZillaName\" from \"Zilla\"","\"Zilla\"", "\"ZillaId\",\"ZillaName\"", "\"ZillaId\"");
     /*/
    /***************************************************************************************************************************
     * // Upload();
     * Download();
     * //Data Update on local database from Server
     * String SQLStr = "";
     * <p>
     * //Member Table
     *//*
                                SQLStr  = " Select h.Dist, h.Upz, h.UN, h.Mouza, h.Vill, h.ProvType, h.ProvCode, h.HHNo, h.SNo,h.HealthID, h.NameEng, h.NameBang, h.Rth, h.HaveNID, h.NID, h.NIDStatus, h.HaveBR,";
                                SQLStr += " h.BRID, h.BRIDStatus, h.MobileNo1, h.MobileNo2, h.DOB, h.Age, h.DOBSource, h.BPlace, h.FNo, h.Father, h.FDontKnow, h.MNo, h.Mother, h.MDontKnow, h.Sex, h.MS, h.SPNO1,";
                                SQLStr += " h.SPNO2, h.SPNO3, h.SPNO4, h.ELCONo, h.ELCODontKnow, h.EDU, h.Rel, h.Nationality, h.OCP, h.StartTime, h.EnType, h.EnDate, h.ExType, h.ExDate";
                                SQLStr += " from Village v";
                                SQLStr += " inner join ProviderArea a on v.ZILLAID+v.UPAZILAID+v.UNIONID+v.MOUZAID+v.VILLAGEID=a.zillaid+a.upazilaid+a.unionid+a.mouzaid+a.villageid";
                                SQLStr += " inner join ProviderAssignArea aa on a.ZILLAID+a.UPAZILAID+a.UNIONID+a.areacode=aa.zillaid+aa.upazilaid+aa.unionid+aa.areacode";
                                SQLStr += " inner join Member h on a.zillaid+a.upazilaid+a.unionid+a.mouzaid+a.villageid=h.dist+h.upz+h.un+h.Mouza+h.Vill";
                                SQLStr += " where h.Upload='3' and aa.provtype='"+ g.getProvType() +"' and aa.provcode='"+ g.getProvCode() +"' and v.ZillaID='"+ g.getDistrict() +"' and v.Upazilaid='"+ g.getUpazila() +"' and v.Unionid='"+ g.getUnion() +"'";
                                response = C.DataUpdate(SQLStr,"Member","Dist, Upz, UN, Mouza, Vill, ProvType, ProvCode, HHNo, SNo,HealthID, NameEng, NameBang, Rth, HaveNID, NID, NIDStatus, HaveBR, BRID, BRIDStatus, MobileNo1, MobileNo2, DOB, Age, DOBSource, BPlace, FNo, Father, FDontKnow, MNo, Mother, MDontKnow, Sex, MS, SPNO1, SPNO2, SPNO3, SPNO4, ELCONo, ELCODontKnow, EDU, Rel, Nationality, OCP, StartTime, EnType, EnDate, ExType, ExDate","Dist, Upz, UN, Mouza, Vill, ProvType,ProvCode, HHNo, SNo");
                                *//*

                            } catch (Exception e) {

                            }
                            progDailog.dismiss();

                        }
                    }.start();
                    //Connection.MessageBox(VillageList.this,"তথ্য সফলভাবে ডাটাবেজ সার্ভারে আপলোড হয়েছে।");

                }
                catch(Exception ex)
                {
                    Connection.MessageBox(VillageList.this, ex.getMessage());
                }

                return true;
        }
        return false;
    }*/
    public Handler pDialogHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATEDONE:
                    pDialog.dismiss();
                    break;
            }

        }

        public void sendMessage(Message msg) {
            switch (msg.what) {
                case UPDATEDONE:
                    pDialog.dismiss();
                    break;
            }

        }

        @Override
        public void close() {
            pDialog.dismiss();
        }

        @Override
        public void flush() {

        }

        @Override
        public void publish(LogRecord logRecord) {

        }
    };


    private void Download(ProgressDialog progress, String message, android.os.Handler progressHandler, int jumpTime, boolean DowloadOnlyAppropriateRecords) {
        Global.getInstance().setProvType(g.getProvType());
        Global.getInstance().setProvCode(g.getProvCode());
        //35
        message = "Downloading...";
        jumpTime = 1;
        Global.getInstance().setProgressMessage(message);
        progressHandler.sendMessage(progressHandler.obtainMessage());
        final String ProvCode = g.getProvCode();

        final String Dist = g.getDistrict();
        final String Upz = g.getUpazila();
        final String UN = g.getUnion();
        final String ProvType = g.getProvType();

        String PrType = "";

        if (ProvType.equalsIgnoreCase("10")) {
            PrType = "3";
        }

        if (ProvType.equalsIgnoreCase("11")) {
            PrType = "2";
        }


        if (ProvType.equalsIgnoreCase("12")) {
            PrType = "2";
        }

        message = "Downloading Provider DB";
        jumpTime += 1;
        Global.getInstance().setProgressMessage(message);
        progressHandler.sendMessage(progressHandler.obtainMessage());

        // prType

       /* String SQLStr = "Select zillaid,upazilaid,unionid,\"ProvType\",\"ProvCode\",\"ProvName\",\"EnDate\",\"ExDate\",\"Active\",\"DeviceSetting\",\"supervisorCode\" from \"ProviderDB\" where ";
        SQLStr += " zillaid='" + Dist + "' and";
        SQLStr += " upazilaid='" + Upz + "' and";
        SQLStr += " unionid='" + UN + "' and";
        SQLStr += " \"ProvType\"='" + ProvType + "' and";
        SQLStr += " \"ProvCode\"='" + ProvCode + "' and";
        SQLStr += " \"Active\"='1'";
        String Res = C.DownloadJSON(SQLStr, "ProviderDB", "zillaid,upazilaid,unionid,ProvType,ProvCode,ProvName,EnDate,ExDate,Active,DeviceSetting,supervisorCode", "zillaid,upazilaid,unionid,ProvType,ProvCode");
*/
        String SQLStr = "Select zillaid,upazilaid,unionid,\"ProvType\",\"ProvCode\",\"ProvName\",\"EnDate\",\"ExDate\",\"Active\",\"DeviceSetting\" from \"ProviderDB\" where ";
        SQLStr += " zillaid='" + Dist + "' and";
        SQLStr += " upazilaid='" + Upz + "' and";
        SQLStr += " unionid='" + UN + "' and";
        SQLStr += " \"ProvType\"='" + PrType + "' and";
        SQLStr += " \"supervisorCode\"='" + ProvCode + "' and";
        SQLStr += " \"Active\"='1' UNION ALL Select DISTINCT zillaid,upazilaid,unionid,\"ProvType\" AS \"ProvType\"\n" +
                "\t,\"ProvCode\" AS \"ProvCode\"\n" +
                "\t,\"ProvName\" AS \"ProvName\",\"EnDate\",\"ExDate\",\"Active\",\"DeviceSetting\" from \"ProviderDB\" where ";
        SQLStr += " zillaid='" + Dist + "' and";
        SQLStr += " upazilaid='" + Upz + "' and";
        SQLStr += " unionid='" + UN + "' and";
        SQLStr += " \"ProvType\"='" + ProvType + "' and";
        SQLStr += " \"ProvCode\"='" + ProvCode + "' and \"Active\"='1'";


        String Res = C.DownloadJSON(SQLStr, "ProviderDB", "zillaid,upazilaid,unionid,ProvType,ProvCode,ProvName,EnDate,ExDate,Active,DeviceSetting", "zillaid,upazilaid,unionid,ProvType,ProvCode");

        message = "Downloading Provider Area";
        jumpTime += 1;
        Global.getInstance().setProgressMessage(message);
        progressHandler.sendMessage(progressHandler.obtainMessage());
        //Service Provider Area
       /* SQLStr = "Select a.zillaid, a.upazilaid, a.unionid, a.mouzaid, a.villageid, a.\"FWAUnit\", a.\"Ward\", a.\"WardNew\", a.\"Block\",a.\"provType\",a.\"provCode\"";
        SQLStr += " from \"Village\" v";
        SQLStr += " inner join \"ProviderArea\" a on v.\"ZILLAID\"=a.zillaid and v.\"UPAZILAID\"=a.upazilaid and v.\"UNIONID\"=a.unionid and v.\"MOUZAID\"=a.mouzaid and v.\"VILLAGEID\"=a.villageid";
        SQLStr += " Where a.zillaid='" + Dist + "' and";
        SQLStr += " a.upazilaid='" + Upz + "' and";
        SQLStr += " a.unionid='" + UN + "' and";
        SQLStr += " a.\"provType\"='" + ProvType + "' and";
        SQLStr += " a.\"provCode\"='" + ProvCode + "'";

        Res = C.DownloadJSON(SQLStr, "ProviderArea", "zillaid, upazilaid, unionid, mouzaid, villageid, FWAUnit, Ward, WardNew, Block,provType,provCode", "zillaid, upazilaid, unionid, mouzaid, villageid");

*/
        //Service Provider Area
        SQLStr = "Select a.zillaid, a.upazilaid, a.unionid, a.mouzaid, a.villageid, a.\"FWAUnit\", a.\"Ward\", a.\"WardNew\", a.\"Block\",a.\"provType\",a.\"provCode\"";
        SQLStr += " from \"Village\" v";
        SQLStr += " inner join \"ProviderArea\" a on v.\"ZILLAID\"=a.zillaid and v.\"UPAZILAID\"=a.upazilaid and v.\"UNIONID\"=a.unionid and v.\"MOUZAID\"=a.mouzaid and v.\"VILLAGEID\"=a.villageid";
        SQLStr += " INNER JOIN \"ProviderDB\" PDB ON PDB.\"ProvCode\" = a.\"provCode\" ";
        SQLStr += " Where a.zillaid='" + Dist + "' and";
        SQLStr += " a.upazilaid='" + Upz + "' and";
        SQLStr += " a.unionid='" + UN + "' and";
        SQLStr += " PDB.\"ProvType\"='" + PrType + "'";// and";
        // SQLStr += " a.\"provCode\"='" + ProvCode + "'";

        Res = C.DownloadJSON(SQLStr, "ProviderArea", "zillaid, upazilaid, unionid, mouzaid, villageid, FWAUnit, Ward, WardNew, Block,provType,provCode", "zillaid, upazilaid, unionid, mouzaid, villageid,ProvCode");


        message = "Downloading Service Tables";
        jumpTime += 1;
        progressHandler.sendMessage(progressHandler.obtainMessage());
        C.DownloadServiceTables(ProvCode, progress, message, progressHandler, jumpTime, DowloadOnlyAppropriateRecords);



        // AHI
        message = "Downloading epiSchedulerUpdate";
        jumpTime += 1;
        Global.getInstance().setProgressMessage(message);
        progressHandler.sendMessage(progressHandler.obtainMessage());

        SQLStr = "SELECT \"Div\"\n" +
                ",\"Dist\"\n" +
                ",\"Upz\"\n" +
                ",\"UN\"\n" +
                ",\"wordOld\"\n" +
                ",\"subBlockId\"\n" +
                ",\"scheduleYear\"\n" +
                ",\"schedulerId\"\n" +
                ",\"scheduleDate\"\n" +
                ",\"centerName\"\n" +
                ",\"centerType\"\n" +
                ",\"KhanaNoFrom\"\n" +
                ",\"KhanaNoTo\"\n" +
                ",\"KhanaTotal\"\n" +
                ",\"systemEntryDate\"\n" +
                ",\"modifyDate\"\n" +
                ",\"upload\"\n" +
                "FROM \"public\".\"epiSchedulerUpdate\"\n" +
                "where \"Dist\"='" + Dist + "' and \"Upz\"='" + Upz + "'" +
                // " and \"UN\"='" + UN + "'" +
                "and  \"UN\" in(Select distinct \"unionid\" from \"ProviderDB\" where \"supervisorCode\"='" + ProvCode + "') ";
        //  "and \"wordOld\" in(select distinct cast(\"Ward\" as integer) from \"ProviderArea\"\n" +
        //"where \"provCode\"='" + ProvCode + "')";
        // "where  \"provCode\" in(Select distinct \"ProvCode\" from \"ProviderDB\" where \"supervisorCode\"='" + ProvCode + "')";
        //"where \"provCode\"='" + ProvCode + "')";
        Res = C.DownloadJSON(SQLStr, "epiSchedulerUpdate", "Div,Dist,Upz,UN,wordOld,subBlockId,scheduleYear,schedulerId,scheduleDate,centerName,centerType,KhanaNoFrom,KhanaNoTo,KhanaTotal,systemEntryDate,modifyDate,upload", "Dist,Upz,UN,wordOld,subBlockId,scheduleYear,schedulerId");

// HI
        message = "Downloading epiSchedulerUpdate";
        jumpTime += 1;
        Global.getInstance().setProgressMessage(message);
        progressHandler.sendMessage(progressHandler.obtainMessage());

        SQLStr = "SELECT \"Div\"\n" +
                ",\"Dist\"\n" +
                ",\"Upz\"\n" +
                ",\"UN\"\n" +
                ",\"wordOld\"\n" +
                ",\"subBlockId\"\n" +
                ",\"scheduleYear\"\n" +
                ",\"schedulerId\"\n" +
                ",\"scheduleDate\"\n" +
                ",\"centerName\"\n" +
                ",\"centerType\"\n" +
                ",\"KhanaNoFrom\"\n" +
                ",\"KhanaNoTo\"\n" +
                ",\"KhanaTotal\"\n" +
                ",\"systemEntryDate\"\n" +
                ",\"modifyDate\"\n" +
                ",\"upload\"\n" +
                " FROM \"public\".\"epiSchedulerUpdate\"\n" +
                " where \"Dist\"='" + Dist + "' and \"Upz\"='" + Upz + "'" +
                " and  \"UN\" in(Select distinct \"unionid\" from \"ProviderDB\"\twhere \"supervisorCode\" in (Select \"ProvCode\" from \"ProviderDB\" where \"supervisorCode\" ='" + ProvCode + "')) ";
        // " and \"UN\"='" + UN + "'" +
        // "and  \"UN\" in(Select distinct \"unionid\" from \"ProviderDB\" where \"supervisorCode\"='" + ProvCode + "') ";
        //  "and \"wordOld\" in(select distinct cast(\"Ward\" as integer) from \"ProviderArea\"\n" +
        //"where \"provCode\"='" + ProvCode + "')";
        // "where  \"provCode\" in(Select distinct \"ProvCode\" from \"ProviderDB\" where \"supervisorCode\"='" + ProvCode + "')";
        //"where \"provCode\"='" + ProvCode + "')";
        Res = C.DownloadJSON(SQLStr, "epiSchedulerUpdate", "Div,Dist,Upz,UN,wordOld,subBlockId,scheduleYear,schedulerId,scheduleDate,centerName,centerType,KhanaNoFrom,KhanaNoTo,KhanaTotal,systemEntryDate,modifyDate,upload", "Dist,Upz,UN,wordOld,subBlockId,scheduleYear,schedulerId");



//ElcoEvent
        message = "Downloading Elco event";
        jumpTime += 1;
        Global.getInstance().setProgressMessage(message);
        progressHandler.sendMessage(progressHandler.obtainMessage());

        SQLStr = "select \"EVCode\", \"EVName\" from \"ElcoEvent\"";
        Res = C.DownloadJSON(SQLStr, "ElcoEvent", "EVCode, EVName", "EVCode");


        //month
        SQLStr = "SELECT * FROM \"month\"";
        Res = C.DownloadJSON(SQLStr, "month", "id, mName", "id");


//fpaItem
        message = "Downloading fpaItem";
        jumpTime += 1;
        Global.getInstance().setProgressMessage(message);
        progressHandler.sendMessage(progressHandler.obtainMessage());
        SQLStr = "SELECT * FROM \"fpaItem\"";
        Res = C.DownloadJSON(SQLStr, "fpaItem", "itemCode, itemDes,type", "itemCode,type");

        //ccInfo
        SQLStr = "SELECT * FROM \"ccInfo\"";
        Res = C.DownloadJSON(SQLStr, "ccInfo", "zilaid, upazilaid, unionid, unionname, wardid, ward, ccid, ccname, hprovdername, mobailno", "ccid");


        //DeviceNo
        //Save("Delete from DeviceNo");
        //Save("Insert into DeviceNo(DeviceNo)Values('" + (ProvType + ProvCode) + "')");

        //Login

        message = "Downloading Login Credential";
        jumpTime += 1;
        Global.getInstance().setProgressMessage(message);
        progressHandler.sendMessage(progressHandler.obtainMessage());
        SQLStr = "Select \"ProvCode\",\"ProvName\",''Pass from \"ProviderDB\" Where ";
        SQLStr += " zillaid='" + Dist + "' and";
        SQLStr += " upazilaid='" + Upz + "' and";
        SQLStr += " unionid='" + UN + "' and";
        SQLStr += " \"ProvType\"='" + ProvType + "' and";
        SQLStr += " \"ProvCode\"='" + ProvCode + "' and";
        SQLStr += " \"Active\"='1'";
        Res = C.DownloadJSON(SQLStr, "Login", "UserId,UserName,Pass", "UserId");


        message = "Downloading Division";
        jumpTime += 1;
        Global.getInstance().setProgressMessage(message);
        progressHandler.sendMessage(progressHandler.obtainMessage());
        //Division
        SQLStr = "SELECT * FROM \"Division\"";
        Res = C.DownloadJSON(SQLStr, "Division", "id, division", "id");


        message = "Downloading Districts";
        jumpTime += 1;
        Global.getInstance().setProgressMessage(message);
        progressHandler.sendMessage(progressHandler.obtainMessage());
        //District
        SQLStr = "Select \"DIVID\", \"ZILLAID\", \"ZILLANAMEENG\", \"ZILLANAME\" from \"Zilla\"";
        Res = C.DownloadJSON(SQLStr, "Zilla", "DIVID, ZILLAID, ZILLANAMEENG, ZILLANAME", "ZillaID");

        message = "Downloading Upazilla";
        jumpTime += 1;
        Global.getInstance().setProgressMessage(message);
        progressHandler.sendMessage(progressHandler.obtainMessage());
        //Upazila
        SQLStr = "Select \"ZILLAID\", \"UPAZILAID\", \"UPAZILANAMEENG\", \"UPAZILANAME\" from \"Upazila\" where \"ZILLAID\"='" + Dist + "' and \"UPAZILAID\"='" + Upz + "'";
        Res = C.DownloadJSON(SQLStr, "Upazila", "ZILLAID, UPAZILAID, UPAZILANAMEENG, UPAZILANAME", "ZillaID,UPAZILAID");

        message = "Downloading Union";
        jumpTime += 1;
        Global.getInstance().setProgressMessage(message);
        progressHandler.sendMessage(progressHandler.obtainMessage());
        //Unions
        SQLStr = "Select \"ZILLAID\", \"UPAZILAID\", \"MUNICIPALITYID\", \"UNIONID\", \"UNIONNAMEENG\", \"UNIONNAME\" from \"Unions\" where \"ZILLAID\"='" + Dist + "' and \"UPAZILAID\"='" + Upz + "' and \"UNIONID\"='" + UN + "'";
        Res = C.DownloadJSON(SQLStr, "Unions", "ZILLAID, UPAZILAID, MUNICIPALITYID, UNIONID, UNIONNAMEENG, UNIONNAME", "ZillaID,UPAZILAID,UnionId");

        message = "Downloading Mouza";
        jumpTime += 1;
        Global.getInstance().setProgressMessage(message);
        progressHandler.sendMessage(progressHandler.obtainMessage());
        //Mouza
      /*  SQLStr = "Select m.\"ZILLAID\", m.\"UPAZILAID\", m.\"MUNICIPALITYID\", m.\"UNIONID\", m.\"MOUZAID\", m.\"RMO\", m.\"MOUZANAMEENG\", m.\"MOUZANAME\" from \"Mouza\" m";
        SQLStr += " inner join \"ProviderArea\" a on m.\"ZILLAID\"=a.zillaid and m.\"UPAZILAID\"=a.upazilaid and m.\"UNIONID\"=a.unionid and m.\"MOUZAID\"=a.mouzaid";
        SQLStr += " where a.\"provType\"='" + PrType + "' and a.\"provCode\"='" + ProvCode + "' and m.\"ZILLAID\"='" + Dist + "' and m.\"UPAZILAID\"='" + Upz + "' and m.\"UNIONID\"='" + UN + "'";
        Res = C.DownloadJSON(SQLStr, "Mouza", "ZILLAID, UPAZILAID, MUNICIPALITYID, UNIONID, MOUZAID, RMO, MOUZANAMEENG, MOUZANAME", "ZillaID,UPAZILAID,UnionId,MOUZAID");
*/
        SQLStr = "Select m.\"ZILLAID\", m.\"UPAZILAID\", m.\"MUNICIPALITYID\", m.\"UNIONID\", m.\"MOUZAID\", m.\"RMO\", m.\"MOUZANAMEENG\", m.\"MOUZANAME\" from \"Mouza\" m";
        SQLStr += " inner join \"ProviderArea\" a on m.\"ZILLAID\"=a.zillaid and m.\"UPAZILAID\"=a.upazilaid and m.\"UNIONID\"=a.unionid and m.\"MOUZAID\"=a.mouzaid";
        SQLStr += " where a.\"provType\"='" + PrType + "' and m.\"ZILLAID\"='" + Dist + "' and m.\"UPAZILAID\"='" + Upz + "' and m.\"UNIONID\"='" + UN + "'";
        Res = C.DownloadJSON(SQLStr, "Mouza", "ZILLAID, UPAZILAID, MUNICIPALITYID, UNIONID, MOUZAID, RMO, MOUZANAMEENG, MOUZANAME", "ZillaID,UPAZILAID,UnionId,MOUZAID");


        message = "Downloading Village";
        jumpTime += 1;
        Global.getInstance().setProgressMessage(message);
        progressHandler.sendMessage(progressHandler.obtainMessage());
        //Village
        /*SQLStr = "Select v.\"ZILLAID\", v.\"UPAZILAID\", v.\"UNIONID\", v.\"MOUZAID\", v.\"VILLAGEID\", v.\"RMO\", v.\"VILLAGENAMEENG\", v.\"VILLAGENAME\", coalesce(v.\"CRRVILLAGENAME\",'')CRRVILLAGENAME,v.close,v.ward,v.fwaunit from \"Village\" v";
        SQLStr += " inner join \"ProviderArea\" a on v.\"ZILLAID\"=a.zillaid and v.\"UPAZILAID\"=a.upazilaid and v.\"UNIONID\"=a.unionid and v.\"MOUZAID\"=a.mouzaid and v.\"VILLAGEID\"=a.villageid";
        SQLStr += " where a.\"provType\"='" + PrType + "' and a.\"provCode\"='" + ProvCode + "' and v.\"ZILLAID\"='" + Dist + "' and v.\"UPAZILAID\"='" + Upz + "' and v.\"UNIONID\"='" + UN + "'";
        Res = C.DownloadJSON(SQLStr, "Village", "ZILLAID, UPAZILAID, UNIONID, MOUZAID, VILLAGEID, RMO, VILLAGENAMEENG, VILLAGENAME, CRRVILLAGENAME,close,ward,fwaunit", "ZillaID,UPAZILAID,UnionId,MOUZAID,VillageID");
*/
        SQLStr = "Select v.\"ZILLAID\", v.\"UPAZILAID\", v.\"UNIONID\", v.\"MOUZAID\", v.\"VILLAGEID\", v.\"RMO\", v.\"VILLAGENAMEENG\", v.\"VILLAGENAME\", coalesce(v.\"CRRVILLAGENAME\",'')CRRVILLAGENAME,v.close,v.ward,v.fwaunit from \"Village\" v";
        SQLStr += " inner join \"ProviderArea\" a on v.\"ZILLAID\"=a.zillaid and v.\"UPAZILAID\"=a.upazilaid and v.\"UNIONID\"=a.unionid and v.\"MOUZAID\"=a.mouzaid and v.\"VILLAGEID\"=a.villageid";
        SQLStr += " where a.\"provType\"='" + PrType + "' and v.\"ZILLAID\"='" + Dist + "' and v.\"UPAZILAID\"='" + Upz + "' and v.\"UNIONID\"='" + UN + "'";
        Res = C.DownloadJSON(SQLStr, "Village", "ZILLAID, UPAZILAID, UNIONID, MOUZAID, VILLAGEID, RMO, VILLAGENAMEENG, VILLAGENAME, CRRVILLAGENAME,close,ward,fwaunit", "ZillaID,UPAZILAID,UnionId,MOUZAID,VillageID");


        //Update Device Setting Status in Server DB
            /*
            SQLStr  = "Update \"ProviderDB\" Set \"DeviceSetting\"='2' Where ";
            SQLStr += " zillaid='"+ Dist +"' and";
            SQLStr += " upazilaid='"+ Upz +"' and";
            SQLStr += " unionid='"+ UN +"' and";
            SQLStr += " \"ProvType\"='"+ ProvType +"' and";
            SQLStr += " \"ProvCode\"='"+ ProvCode +"' and";
            SQLStr += " \"Active\"='1'";
            ExecuteCommandOnServer(SQLStr);
            */
//ses
        /*message = "Downloading Socio Economic Status. This might take a while";
        jumpTime += 1;
        Global.getInstance().setProgressMessage(message);
        progressHandler.sendMessage(progressHandler.obtainMessage());
        SQLStr = "Select C.*\n" +
                "from \"Village\" v\n" +
                "INNER JOIN \"ProviderArea\" a on v.\"ZILLAID\" = a.zillaid\n" +
                "\tand v.\"UPAZILAID\" = a.upazilaid\n" +
                "\tand v.\"UNIONID\" = a.unionid\n" +
                "\tand v.\"MOUZAID\" = a.mouzaid\n" +
                "\tand v.\"VILLAGEID\" = a.villageid\n" +
                "INNER JOIN \"ses\" C on a.zillaid = CAST(C.\"dist\" as Integer)\n" +
                "\tand a.upazilaid = CAST(C.\"upz\" as Integer)\n" +
                "\tand a.unionid = CAST(C.\"un\" as Integer)\n" +
                "\tand a.mouzaid = CAST(C.\"mouza\" as Integer)\n" +
                "\tand a.villageid = CAST(C.\"vill\" as Integer)\n" +
                "where a.\"provType\" = '" + ProvType + "'\n" +
                "\tand a.\"provCode\" = '" + ProvCode + "'\n" +
                "\tand v.\"ZILLAID\" = '" + Dist + "'\n" +
                "\tand v.\"UPAZILAID\" = '" + Upz + "'\n" +
                "\tand v.\"UNIONID\" = '" + UN + "'\n";

        if (DowloadOnlyAppropriateRecords) {
            SQLStr = C.JoinQueryForAppropriateRecords(SQLStr, "ses", ProvCode);
        }
        Res = C.DownloadJSON(SQLStr, "ses", "dist, upz, un, mouza, vill, provType,provCode,hhNo,status,q1,q11,q2 ,q21, q3a,q3b,q3c,q3d, q3e, q3f,q3g,q3h, q3i, q3j, q3k, q3l, q3m, q3n, q3o, q3p, q4, q41, q5, q51, q6, q61, q7, q71, q8a, q8b, q8c, q8d, q8e, Q9, Q10,Q11a, startTime, endTime, userId, enDt, upload", "dist, upz, un, mouza, vill, hhNo");

        message = "Downloading Household. This might take a while";
        jumpTime += 1;
        Global.getInstance().setProgressMessage(message);
        progressHandler.sendMessage(progressHandler.obtainMessage());
        //Household data
        SQLStr = "Select h.\"Dist\", h.\"Upz\", h.\"UN\", h.\"Mouza\", h.\"Vill\", h.\"PAddr\", h.\"PermaAddress\", h.\"ProvType\", h.\"ProvCode\", h.\"HHNo\", h.\"Religion\", h.\"VGFCard\",";
        SQLStr += " h.\"subBlock\",h.\"unit\", h.\"StartTime\", h.\"EndTime\", h.\"Lat\", h.\"Lon\", h.\"UserId\", h.\"EnDt\", '1' Upload";
        SQLStr += " from \"Village\" v";
        SQLStr += " inner join \"ProviderArea\" a on v.\"ZILLAID\"=a.zillaid and v.\"UPAZILAID\"=a.upazilaid and v.\"UNIONID\"=a.unionid and v.\"MOUZAID\"=a.mouzaid and v.\"VILLAGEID\"=a.villageid";
        SQLStr += " inner join \"Household\" h on a.zillaid=h.\"Dist\" and a.upazilaid=h.\"Upz\" and a.unionid=h.\"UN\" and a.mouzaid=h.\"Mouza\" and a.villageid=h.\"Vill\"";
        SQLStr += " where a.\"provType\"='" + ProvType + "' and a.\"provCode\"='" + ProvCode + "' and v.\"ZILLAID\"='" + Dist + "' and v.\"UPAZILAID\"='" + Upz + "' and v.\"UNIONID\"='" + UN + "'";

        if (DowloadOnlyAppropriateRecords) {
            SQLStr = C.JoinQueryForAppropriateRecords(SQLStr, "Household", ProvCode);
        }
        Res = C.DownloadJSON(SQLStr, "Household", "Dist, Upz, UN, Mouza, Vill, PAddr, PermaAddress, ProvType, ProvCode, HHNo, Religion, VGFCard,subBlock,unit, StartTime, EndTime, Lat, Lon, UserId, EnDt, Upload", "Dist, Upz, UN, Mouza, Vill, HHNo");

        message = "Downloading Household Members. This might take a while";
        jumpTime += 1;
        Global.getInstance().setProgressMessage(message);
        progressHandler.sendMessage(progressHandler.obtainMessage());
        //Member Data
        SQLStr = " Select h.\"Dist\", h.\"Upz\", h.\"UN\", h.\"Mouza\", h.\"Vill\", h.\"ProvType\", h.\"ProvCode\", h.\"HHNo\", h.\"SNo\", h.\"HealthID\", h.\"NameEng\", h.\"NameBang\", h.\"Rth\", h.\"HaveNID\", h.\"NID\", h.\"NIDStatus\", h.\"HaveBR\",";
        SQLStr += " h.\"BRID\", h.\"BRIDStatus\", h.\"MobileNo1\", h.\"MobileNo2\",h.mobileyn, h.\"DOB\", h.\"Age\", h.\"DOBSource\", h.\"BPlace\", h.\"FNo\", h.\"Father\", h.\"FDontKnow\", h.\"MNo\", h.\"Mother\", h.\"MDontKnow\", h.\"Sex\", h.\"MS\", h.\"SPNO1\",";
        SQLStr += " h.\"SPNO2\", h.\"SPNO3\", h.\"SPNO4\", h.\"ELCONo\", h.\"ELCODontKnow\", h.\"EDU\", h.\"Rel\", h.\"Nationality\", h.\"OCP\", h.\"StartTime\", h.\"EnType\", h.\"EnDate\", coalesce(h.\"ExType\", '')  AS \"ExType\", h.\"ExDate\", h.\"EndTime\", h.\"Lat\", h.\"Lon\", h.\"UserId\", h.\"EnDt\",  h.\"hidDistributed\",  h.\"hidDistributionDate\", '1' Upload";
        SQLStr += " from \"Village\" v";
        SQLStr += " inner join \"ProviderArea\" a on v.\"ZILLAID\"=a.zillaid and v.\"UPAZILAID\"=a.upazilaid and v.\"UNIONID\"=a.unionid and v.\"MOUZAID\"=a.mouzaid and v.\"VILLAGEID\"=a.villageid";
        SQLStr += " inner join \"Member\" h on a.zillaid=h.\"Dist\" and a.upazilaid=h.\"Upz\" and a.unionid=h.\"UN\" and a.mouzaid=h.\"Mouza\" and a.villageid=h.\"Vill\"";
        SQLStr += " where a.\"provType\"='" + ProvType + "' and a.\"provCode\"='" + ProvCode + "' and v.\"ZILLAID\"='" + Dist + "' and v.\"UPAZILAID\"='" + Upz + "' and v.\"UNIONID\"='" + UN + "'";

        if (DowloadOnlyAppropriateRecords) {
            SQLStr = C.JoinQueryForAppropriateRecords(SQLStr, "Member", ProvCode);
        }
        Res = C.DownloadJSON(SQLStr, "Member", "Dist, Upz, UN, Mouza, Vill, ProvType, ProvCode, HHNo, SNo, HealthID, NameEng, NameBang, Rth, HaveNID, NID, NIDStatus, HaveBR, BRID, BRIDStatus, MobileNo1, MobileNo2,MobileYN, DOB, Age, DOBSource, BPlace, FNo, Father, FDontKnow, MNo, Mother, MDontKnow, Sex, MS, SPNO1, SPNO2, SPNO3, SPNO4, ELCONo, ELCODontKnow, EDU, Rel, Nationality, OCP, StartTime, EnType, EnDate, ExType, ExDate, EndTime, Lat, Lon, UserId, EnDt,hidDistributed,hidDistributionDate, Upload", "Dist, Upz, UN, Mouza, Vill, HHNo, SNo");

        message = "Downloading Visits. This might take a while";
        jumpTime += 1;
        Global.getInstance().setProgressMessage(message);
        progressHandler.sendMessage(progressHandler.obtainMessage());
        //Visits Data
        SQLStr = " Select dist, upz, un, mouza, vill, h.\"provType\", h.\"provCode\", \"hhNo\", \"vDate\", \"vStatus\", \"startTime\", \"endTime\", \"lat\", \"lon\", \"userId\", \"enDt\",'1' Upload";
        SQLStr += " from \"Village\" v";
        SQLStr += " inner join \"ProviderArea\" a on v.\"ZILLAID\"=a.zillaid and v.\"UPAZILAID\"=a.upazilaid and v.\"UNIONID\"=a.unionid and v.\"MOUZAID\"=a.mouzaid and v.\"VILLAGEID\"=a.villageid";
        SQLStr += " inner join \"visits\" h on a.zillaid=h.dist and a.upazilaid=h.upz and a.unionid=h.un and a.mouzaid=h.mouza and a.villageid=h.vill";
        SQLStr += " where a.\"provType\"='" + ProvType + "' and a.\"provCode\"='" + ProvCode + "' and v.ZillaID='" + Dist + "' and v.Upazilaid='" + Upz + "' and v.Unionid='" + UN + "'";

        if (DowloadOnlyAppropriateRecords) {
            SQLStr = C.JoinQueryForAppropriateRecords(SQLStr, "Visits", ProvCode);
        }
        Res = C.DownloadJSON(SQLStr, "Visits", "Dist, Upz, UN, Mouza, Vill, ProvType, ProvCode, HHNo, VDate, VStatus, StartTime, EndTime, Lat, Lon, UserId, EnDt, Upload", "Dist, Upz, UN, Mouza, Vill, HHNo, VDate");
*/

        message = "Downloading OCP Code List";
        jumpTime += 1;
        Global.getInstance().setProgressMessage(message);
        progressHandler.sendMessage(progressHandler.obtainMessage());

        //Download OCP Code List
        SQLStr = "select \"ocpCode\", \"ocpName\", \"dCode\", \"upz\", \"ocpSequence\" from \"ocpList\" order by \"ocpSequence\"";
        Res = C.DownloadJSON(SQLStr, "ocplist", "ocpCode, ocpName, DCode, Upz, ocpSequence", "ocpCode");


        message = "Downloading Brands";
        jumpTime += 1;
        Global.getInstance().setProgressMessage(message);
        progressHandler.sendMessage(progressHandler.obtainMessage());
        SQLStr = "SELECT * FROM \"BrandMethod\"";
        Res = C.DownloadJSON(SQLStr, "BrandMethod", "brandCode, brandName", "brandCode");


        message = "Downloading Attendant Designation";
        jumpTime += 1;
        Global.getInstance().setProgressMessage(message);
        progressHandler.sendMessage(progressHandler.obtainMessage());
        SQLStr = "SELECT * FROM \"AttendantDesignation\"";
        Res = C.DownloadJSON(SQLStr, "AttendantDesignation", "attendantCode, attendantDesig", "attendantCode");

        //CodeList
        message = "Downloading Code list";
        jumpTime += 1;
        Global.getInstance().setProgressMessage(message);
        progressHandler.sendMessage(progressHandler.obtainMessage());
        SQLStr = "SELECT * FROM \"CodeList\"";
        Res = C.DownloadJSON(SQLStr, "CodeList", "TypeName, Code, CName", "TypeName, Code");

        //ProviderType
        message = "Downloading Provider Type";
        jumpTime += 1;
        Global.getInstance().setProgressMessage(message);
        progressHandler.sendMessage(progressHandler.obtainMessage());
        SQLStr = "SELECT * FROM \"ProviderType\"";
        Res = C.DownloadJSON(SQLStr, "ProviderType", "ProvType, TypeName", "TypeName");

//FWA Unit
        message = "Downloading FWA Unit";
        jumpTime += 1;
        Global.getInstance().setProgressMessage(message);
        progressHandler.sendMessage(progressHandler.obtainMessage());
        SQLStr = "select \"UCode\",\"UName\",\"UNameBan\" from \"FWAUnit\"";
        Res = C.DownloadJSON(SQLStr, "FWAUnit", "UCode,UName,UNameBan", "UCode");

        //Block
        message = "Downloading Block";
        jumpTime += 1;
        Global.getInstance().setProgressMessage(message);
        progressHandler.sendMessage(progressHandler.obtainMessage());
        SQLStr = "select \"BCode\",\"BName\",\"BNameBan\" from \"HABlock\"";
        Res = C.DownloadJSON(SQLStr, "HABlock", "BCode,BName,BNameBan", "BCode");


        //item
        message = "Downloading Item";
        jumpTime += 1;
        Global.getInstance().setProgressMessage(message);
        progressHandler.sendMessage(progressHandler.obtainMessage());
        SQLStr = "select \"itemCode\",\"itemName\", \"brand\", \"unit\", \"status\" from item";
        Res = C.DownloadJSON(SQLStr, "item", "itemCode, itemName, brand, unit, status", "itemCode");

        //currentStock
        message = "Downloading Current Stock";
        jumpTime += 1;
        Global.getInstance().setProgressMessage(message);
        progressHandler.sendMessage(progressHandler.obtainMessage());
        SQLStr = "select \"providerId\",\"itemCode\",\"stockQty\", \"systemEntryDate\", \"modifyDate\", \"upload\" from \"currentStock\" where \"providerId\"='" + ProvCode + "'";
        Res = C.DownloadJSON(SQLStr, "currentStock", "providerId, itemCode, stockQty, systemEntryDate, modifyDate, upload", "providerId,itemCode");

        //classfication
   /*     message = "Downloading Classification";
        jumpTime += 1;
        Global.getInstance().setProgressMessage(message);
        progressHandler.sendMessage(progressHandler.obtainMessage());
        SQLStr = "select \"classficationCode\", \"classficationName\" from classfication";
        Res = C.DownloadJSON(SQLStr, "classfication", "classficationCode, classficationName", "classficationCode");

        //symtom
        message = "Downloading Symptom";
        jumpTime += 1;
        Global.getInstance().setProgressMessage(message);
        progressHandler.sendMessage(progressHandler.obtainMessage());
        SQLStr = "select \"symtomCode\", \"symtomDes\" from symtom";
        Res = C.DownloadJSON(SQLStr, "symtom", "symtomCode, symtomDes", "symtomCode");


        //treatment
        message = "Downloading Treatment";
        jumpTime += 1;
        Global.getInstance().setProgressMessage(message);
        progressHandler.sendMessage(progressHandler.obtainMessage());
        SQLStr = "select \"treatmentCode\", \"tretmentDes\" from treatment";
        Res = C.DownloadJSON(SQLStr, "treatment", "treatmentCode, tretmentDes", "treatmentCode");


        //adoSymtom
        message = "Downloading adoSymptom";
        jumpTime += 1;
        Global.getInstance().setProgressMessage(message);
        progressHandler.sendMessage(progressHandler.obtainMessage());
        SQLStr = "select \"problemCode\", \"problemDes\" from \"adoSymtom\"";
        Res = C.DownloadJSON(SQLStr, "adoSymtom", "problemCode, problemDes", "problemCode");

        message = "Downloading Immunization";
        jumpTime += 1;
        Global.getInstance().setProgressMessage(message);
        progressHandler.sendMessage(progressHandler.obtainMessage());
        SQLStr = "select * from \"immunization\"";
        Res = C.DownloadJSON(SQLStr, "immunization", "imuCode, imuName, numOfDose,forChild,forWoman", "imuCode");
*/
      /*  message = "Downloading EPI Schedule for Woman";
        jumpTime += 1;
        Global.getInstance().setProgressMessage(message);
        progressHandler.sendMessage(progressHandler.obtainMessage());


        SQLStr = "select  *  FROM \"epiSchedulerWoman\" where \"providerId\" =" + ProvCode;
        Res = C.DownloadJSON(SQLStr, "epiSchedulerWoman", "schedulerId, scheduleDate, providerId, subBlockId, centerName, systemEntryDate, modifyDate, upload", "schedulerId, providerId");
*/
        /*message = "Downloading EPI Schedule for Child and Woman";
        jumpTime += 1;
        Global.getInstance().setProgressMessage(message);
        progressHandler.sendMessage(progressHandler.obtainMessage());

        SQLStr = "select  *  FROM \"epiScheduler\" where \"providerId\" =" + ProvCode;
        Res = C.DownloadJSON(SQLStr, "epiScheduler", "schedulerId, scheduleDate, providerId, subBlockId, centerName, systemEntryDate, modifyDate, upload", "schedulerId, providerId");
*/

        //deathReason
 /*       message = "Downloading Death Reasons";
        jumpTime += 1;
        Global.getInstance().setProgressMessage(message);
        progressHandler.sendMessage(progressHandler.obtainMessage());
        SQLStr = "select \"deathReasonId\", \"code\", \"detail\" from \"deathReason\"";
        Res = C.DownloadJSON(SQLStr, "deathReason", "deathReasonId, code, detail", "deathReasonId");*/

       /* message = "Downloading Health Id";
        jumpTime += 1;
        Global.getInstance().setProgressMessage(message);
        progressHandler.sendMessage(progressHandler.obtainMessage());

        //Download Health ID
        //Service Provider
        SQLStr = "sp_HealthIDDownload '" + Dist + "','" + Upz + "','" + UN + "','" + ProvType + "','" + ProvCode + "'";
        Res = C.DownloadJSON(SQLStr, "HealthIDRepository", "ZillaID, UpazilaID, UnionID, ProvType, ProvCode, AreaCode, HealthID,Status", "HealthID");
*/
      /*  message = "Updating System tables. Please wait.";
        jumpTime += 1;
        Global.getInstance().setProgressMessage(message);
        progressHandler.sendMessage(progressHandler.obtainMessage());

        //Update household table based on the data of member table
        //String SQL = "";
        String SQL = "Create table totalmem as";
        SQL += " select dist,upz,un,mouza,vill,provtype,provcode,hhno,count(*)totalmem from Member";
        SQL += " group by dist,upz,un,mouza,vill,provtype,provcode,hhno";
        C.Save(SQL);

        SQL = "Create table headName as";
        SQL += " select dist,upz,un,mouza,vill,provtype,provcode,hhno,nameeng headname from Member where rth='01' and length(extype)=0";
        C.Save(SQL);

        SQL = "update household set HHHead=(select headname from headname h where h.dist=household.dist and h.upz=household.upz and h.un=household.un and h.mouza=household.mouza and h.vill=household.vill and h.provtype=household.provtype and h.provcode=household.provcode and h.hhno=household.hhno)";
        C.Save(SQL);

        SQL = "update household set TotalMem=(select totalmem from totalmem h where h.dist=household.dist and h.upz=household.upz and h.un=household.un and h.mouza=household.mouza and h.vill=household.vill and h.provtype=household.provtype and h.provcode=household.provcode and h.hhno=household.hhno)";
        C.Save(SQL);

        C.Save("drop table totalmem");
        C.Save("drop table headName");

        SQL = "Create table totalmem as";
        SQL += " select dist,upz,un,mouza,vill,hhno,count(*)totalmem from Member";
        SQL += " group by dist,upz,un,mouza,vill,hhno";

        C.Save(SQL);

        SQL = "Create table headName as";
        SQL += " select dist,upz,un,mouza,vill,provtype,provcode,hhno,nameeng headname from Member where rth='01' and length(extype)=0";
        C.Save(SQL);

        SQL = "update household set HHHead=(select headname from headname h where h.dist=household.dist and h.upz=household.upz and h.un=household.un and h.mouza=household.mouza and h.vill=household.vill and  h.hhno=household.hhno)";
        C.Save(SQL);

        SQL = "update household set TotalMem=(select totalmem from totalmem h where h.dist=household.dist and h.upz=household.upz and h.un=household.un and h.mouza=household.mouza and h.vill=household.vill  and h.hhno=household.hhno)";
        C.Save(SQL);

        C.Save("drop table totalmem");
        C.Save("drop table headName");
        message = "Generating Elco";
        jumpTime += 1;
        Global.getInstance().setProgressMessage(message);
        progressHandler.sendMessage(progressHandler.obtainMessage());
        C.GenerateElco();*/


    }

    /*private void UploadDataworkPlanDetail()
    {

        if (C.Existence("Select * FROM workPlanDetail WHERE status='2' and upload='2'")) {
            String TableName = "";
            String VariableList = "";
            TableName = "workPlanDetail";
            VariableList = "workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,leaveType,providerId,systemEntryDate,modifyDate,otherDec,remarks,upload,status";
            C.UploadJSON(TableName, VariableList, "workPlanId, item, workPlanDate, providerId");
        }

        else
        {

        }

    }
*/
    private void Upload(ProgressDialog progress, String message, android.os.Handler progressHandler, int jumpTime) {

        String TableName = "";
        String VariableList = "";


        // message = "Uploading workPlanMaster";
        // jumpTime += 1;
        // progressHandler.sendMessage(progressHandler.obtainMessage());


        // ----------start----
        TableName = "workPlanMaster";
        VariableList = "workPlanId,workAreaId,providerId,month,status,systemEntryDate,modifyDate,upload";
        C.UploadJSONAproved(TableName, VariableList, "workPlanId, workAreaId, providerId");
      //  UploadDataworkPlanDetail();
        // message = "Uploading workPlanDetail";
        // jumpTime += 1;
        // progressHandler.sendMessage(progressHandler.obtainMessage());
      /*  TableName = "workPlanDetail";
        VariableList = "workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,ipcUN,ipcWord,ipcMouza,ipcVill,ipcPara,ipcBariFrom,ipcBariTo,epiproviderId,epischedulerId,ccWard,ccID,leaveType,natProgramType,providerId,systemEntryDate,modifyDate,otherDec,remarks,upload";
        C.UploadJSON(TableName, VariableList, "workPlanId, item, workPlanDate, providerId");*/

        message = "Uploading fpiMonitoring";
        jumpTime += 1;
        progressHandler.sendMessage(progressHandler.obtainMessage());
        TableName = "fpiMonitoring";
        VariableList = "vDate,fpaCode,fpaUnit,fpaVill,fpaAdvance,needItems1,needItems2,needItems3,needItems4,needItems5,needItems6,needItems7,needItems8,startTime,endTime,userId,enDt,upload";
        C.UploadJSON(TableName, VariableList, "vDate, fpaCode, fpaUnit");

        TableName = "HouseholdFPI";
        VariableList = "Dist,Upz,UN,Mouza,Vill,ProvType,ProvCode,HHNo,houseHoldStatus,causeOfHouseHoldStatus,subBlockStatus,pAddrStatus,permaAddressStatus,religionStatus,StartTime,EndTime,Lat,Lon,UserId,EnDt,Upload";
        C.UploadJSON(TableName, VariableList, "Dist,  Upz, UN,Mouza,Vill,HHNo");

        message = "Uploading MemberFPI ";
        // jumpTime += 1;
        // Global.getInstance().setProgressMessage(message);
        //  progressHandler.sendMessage(progressHandler.obtainMessage());
        TableName = "memberfpi";
        VariableList = "dist,upz,un,mouza,vill,provtype,provcode,hhno,sno,healthid,nameengstatus,rthstatus,havenidstatus,nidstatus,havebrstatus,bridstatus,mobileno1status,mobileno2status,dobstatus,agestatus,dobsourcestatus,bplacestatus,fnostatus,fatherstatus,mnostatus,motherstatus,sexstatus,msstatus,spno1status,spno2status,spno3status,spno4status,edustatus,relstatus,nationalitystatus,ocpstatus,starttime,entype,endate,extype,exdate,endtime,lat,lon,userid,endt,upload";
        C.UploadJSON(TableName, VariableList, "dist,upz,un,mouza,vill, hhno, sno");


        message = "Uploading sesFPI ";
        jumpTime += 1;
        Global.getInstance().setProgressMessage(message);
        progressHandler.sendMessage(progressHandler.obtainMessage());
        TableName = "sesfpi";
        VariableList = "dist,upz,un,mouza,vill,provtype,provcode,hhno,sesstatus,q1status,q11status,q2status,q21status,q3astatus,q3bstatus,q3cstatus,q3dstatus,q3estatus,q3fstatus,q3gstatus,q3hstatus,q3istatus,q3jstatus,q3kstatus,q3lstatus,q3mstatus,q3nstatus,q3ostatus,q3pstatus,q4status,q41status,q5status,q51status,q6status,q61status,q7status,q71status,q8astatus,q8bstatus,q8cstatus,q8dstatus,q8estatus,q9status,q10status,q11astatus,starttime,endtime,userid,endt,upload";
        C.UploadJSON(TableName, VariableList, "dist,upz,un,mouza,vill, hhno");

        // done

        message = "Uploading Elco FPI";
        jumpTime += 1;
        progressHandler.sendMessage(progressHandler.obtainMessage());
        TableName = "elcoFPI";
        VariableList = "healthId, providerId, hhStatus, haHHNo, elcoNo, husbandName,husbandAge,domSource, marrDate, marrAge, son, dau, regDT, systemEntryDate,modifyDate,tt1,tt2,tt3,tt4,tt5, upload";
        C.UploadJSON(TableName, VariableList, "healthId");


        message = "Uploading Elco Visit FPI";
        jumpTime += 1;
        progressHandler.sendMessage(progressHandler.obtainMessage());
        TableName = "elcoVisitFPI";
        VariableList = "healthId,pregNo,providerId,transactionId,visit,vDate,visitStatus,currStatus,newOld,mDate,sSource,qty,unit,brand,validity,dayMonYear,referPlace,syrinsQty,mrSource,MRDate,MRAge,systemEntryDate,modifyDate,upload";
        C.UploadJSON(TableName, VariableList, "healthId,visit");

        // done

/*        message = "Uploading Pregnancy refer";
        jumpTime += 1;
        progressHandler.sendMessage(progressHandler.obtainMessage());
        TableName = "PregRefer";
        VariableList = "healthId, providerId, pregNo,serviceId,referCenter,systemEntryDate,modifyDate, upload";
        C.UploadJSON(TableName, VariableList, "healthId, pregNo,serviceId");*/

        message = "Uploading Pregnant Woman FPI";
        jumpTime += 1;
        progressHandler.sendMessage(progressHandler.obtainMessage());
        TableName = "pregWomenFPI";
        VariableList = "healthId, pregNo, providerId,LMP, EDD, para, gravida, lastChildAge, riskHistoryNote,pregRefer, systemEntryDate, upload";
        C.UploadJSON(TableName, VariableList, "healthId, pregNo");


        message = "Uploading ANC Service FPI";
        jumpTime += 1;
        progressHandler.sendMessage(progressHandler.obtainMessage());
        TableName = "ancServiceFPI";
        VariableList = "healthId,pregNo,serviceId,providerId,visitSource,visitDate,visitMonth,ironFolStatus,misoStatus,systemEntryDate,upload";
        C.UploadJSON(TableName, VariableList, "healthId, pregNo, serviceId");


        message = "Uploading Delivery FPI";
        jumpTime += 1;
        progressHandler.sendMessage(progressHandler.obtainMessage());
        TableName = "deliveryFPI";
        VariableList = "healthId, pregNo, providerId, outcomePlace, outcomeDate,  outcomeType,liveBirth, stillBirth, abortion, misoprostol, attendantDesignation,systemEntryDate, upload";
        C.UploadJSON(TableName, VariableList, "healthId, pregNo");


        message = "Uploading New Born FPI";
        jumpTime += 1;
        progressHandler.sendMessage(progressHandler.obtainMessage());
        TableName = "newBornFPI";
        VariableList = "healthId, pregNo, childNo, providerId, birthWeight, immatureBirth,dryingAfterBirth, resassitation, stimulation, bagNMask, chlorehexidin,skinTouch, breastFeed,bathThreeDays, systemEntryDate, modifyDate, upload";
        C.UploadJSON(TableName, VariableList, "healthId, pregNo, childNo");

        message = "Uploading PNC Service Child FPI";
        jumpTime += 1;
        progressHandler.sendMessage(progressHandler.obtainMessage());
        TableName = "pncServiceChildFPI";
        VariableList = "healthId, pregNo, childNo, serviceId,providerId,visitSource,visitDate,visitMonth, systemEntryDate, modifyDate, upload";
        C.UploadJSON(TableName, VariableList, "healthId, pregNo, childNo, serviceId");


        message = "Uploading PNC Service Mother FPI";
        jumpTime += 1;
        progressHandler.sendMessage(progressHandler.obtainMessage());
        TableName = "pncServiceMother";
        VariableList = "healthId, pregNo, serviceId, providerId,visitSource,visitDate,visitMonth,systemEntryDate, modifyDate, upload";
        C.UploadJSON(TableName, VariableList, "healthId, pregNo, serviceId");


        message = "Uploading EPI Bari Visit";
        jumpTime += 1;
        progressHandler.sendMessage(progressHandler.obtainMessage());
        TableName = "epiBariVisit";
        VariableList = "dist,upz,un,mouza,vill,provType,provCode,hHNo,vDate,qBHHNo,qBHEndDate,qBPVisitEPI1,qBPVisitEPI2,qBPVisitEPI3,qBPVisitEPI4,qBPVisitEPI5,qBNextDoss,qB1stDoss1,qB1stDoss2,qB1stDoss3,qB1stDoss4,qB1stDoss5,qBCNoSessiondossY,qBCNoSessiondoss,qBWNoSessiondossY,qBWNoSessiondoss,qBVitmA,qBChildCard,qBWomenCard,startTime,endTime,userId,enDt,upload";
        C.UploadJSON(TableName, VariableList, "dist,upz,un,mouza,vill,provCode,hHNo,vDate");


        message = "Uploading EPI Session Visit";
        jumpTime += 1;
        progressHandler.sendMessage(progressHandler.obtainMessage());
        TableName = "epiSessionVisit";
        VariableList = "subBlockId,schedulerId,providerId,vDate,qVHA,qVFWA,qVN,qVOth,qVChReg,qVWReg,qVChCard,qVWCard,qVTalBook,qVFIBook,qVVac,qVASerice,qVMSerice,qVSBox,qVVatARed,qVVatABlue,qVIICPac,qVBCG,qVPenta,qVPolio,qVPcv,qVIPV,qVMR,qVTT,qVSbcg,qVSPenta,qVSPolio,qVSPcv,qVSIPV,qVSMR,qVSTT,qVNToubcg,qVNToupant,qVNTouPolio,qVNToupcv,qVNTouIPV,qVNTouMR,qVNTouTT,qVRootbcg,qVNotwhy,qVRootPenta,qVRootMR,qVRootTT,qVSRemoved,qVFormbcg,qVFormpenta,qVFormPolio,qVFormpcv,qVFormipv,qVFormmr,qVFormtt,qVCardregBook,qVCardVac,qVTTresearched,qVProtectors,qVDateOfVac,qVCard,qVAFP,qVMeasles,qVNewborntetanus,qVOther,qVTodySession,qVProblem1,qVProblem2,qVProblem3,qVProblem4,qVProblem5,qVSolve1,qVSolve2,qVSolve3,qVSolve4,qVSolve5,startTime,endTime,userId,enDt,upload";
        C.UploadJSON(TableName, VariableList, "subBlockId,schedulerId,providerId,vDate");


    }
   /* private void Download() {
        final String ProvCode = g.getProvCode();

        final String Dist = g.getDistrict();
        final String Upz = g.getUpazila();
        final String UN = g.getUnion();
        final String ProvType = g.getProvType();

       C.DownloadServiceTables(ProvCode);
        String SQLStr="";
        String Res="";

        SQLStr = "select \"workPlanId\", \"workAreaId\", \"providerId\", \"month\", \"status\", \"systemEntryDate\", \"modifyDate\", \"upload\" from \"fpaWorkPlanMaster\" where ";
        SQLStr += " providerId='" +Global.Left(spnProvider.getSelectedItem().toString(),5)+ "'";
        Res = C.DownloadJSON(SQLStr, "fpaWorkPlanMaster", "workPlanId,workAreaId,providerId,month,status,systemEntryDate,modifyDate,upload", "workPlanId, workAreaId, providerId");

        SQLStr = "select \"workPlanId\", \"fpaItem\", \"workPlanDate\", \"unitNo\", \"village\", \"elcoFrom\", \"elcoTo\", \"leaveType\", \"providerId\", \"systemEntryDate\", \"modifyDate\", \"otherDec\", \"remarks\", \"upload\"  from \"fpaWorkPlanDetail\" where ";
        SQLStr += " providerId='" +Global.Left(spnProvider.getSelectedItem().toString(),5)+ "'";
        Res = C.DownloadJSON(SQLStr, "fpaWorkPlanDetail", "workPlanId,fpaItem,workPlanDate,unitNo,village,elcoFrom,elcoTo,leaveType,providerId,systemEntryDate,modifyDate,otherDec,remarks,upload", "workPlanId, fpaItem, providerId");

        // Res = C.DownloadJSON(SQLStr, "providerId", "workPlanId,workAreaId,providerId,month,status,systemEntryDate,modifyDate,upload", "providerId");

    }
*/

    private void Download() {
        final String ProvCode = g.getProvCode();

        final String Dist = g.getDistrict();
        final String Upz = g.getUpazila();
        final String UN = g.getUnion();
        final String ProvType = g.getProvType();

        // C.DownloadServiceTables(ProvCode);
        String SQLStr = "";
        String Res = "";

        SQLStr = "SELECT * FROM \"workPlanMaster\" where ";
        SQLStr += " \"providerId\"='" + Global.Left(spnProvider.getSelectedItem().toString(), 5) + "'";
        Res = C.DownloadJSON(SQLStr, "fpaWorkPlanMaster", "workPlanId,workAreaId,providerId,month,status,systemEntryDate,modifyDate,upload", "workPlanId, workAreaId, providerId");

        SQLStr = "SELECT * FROM \"workPlanDetail\" where ";
        SQLStr += " \"providerId\"='" + Global.Left(spnProvider.getSelectedItem().toString(), 5) + "'";
        Res = C.DownloadJSON(SQLStr, "fpaWorkPlanDetail", "workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,leaveType,providerId,systemEntryDate,modifyDate,otherDec,remarks,upload", "workPlanId, fpaItem, workPlanDate, providerId");


    }


    boolean netwoekAvailable = false;
    Location currentLocation;
    double currentLatitude, currentLongitude;

    ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
    SimpleAdapter mSchedule;
    LinearLayout secfwaname;
    LinearLayout linearLayoutepi;
    LinearLayout linearLayoutprsMch;
    LinearLayout linearLayoutElo;
    LinearLayout vList;
    TextView txtUpazila;
    TextView txtUnion;
    Spinner spnMouza;
    Spinner spnWardNew;
    Spinner spnWardOld;
    Spinner spnFWAUnit;
    Spinner spnEPIBlock;
    TextView VlblRegister;
    Spinner spnProvider;
    View viewa;
    View viewb;
    Integer TI = 0;
    Integer TD = 0;
    Context con;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            C = new Connection(this);
            g = Global.getInstance();
            //setContentView(R.layout.villagelist);
            if (IsUserAHI()) {
                setContentView(R.layout.villagelist1);
            } else if (IsUserHI()) {
                setContentView(R.layout.villagelist2);
            } else {
                setContentView(R.layout.villagelist);
            }

            vList = (LinearLayout) findViewById(R.id.vList);
            viewa = (View) findViewById(R.id.viewa);
            viewb = (View) findViewById(R.id.viewb);
            linearLayoutElo = (LinearLayout) findViewById(R.id.linearLayoutElo);
            secfwaname = (LinearLayout) findViewById(R.id.secfwaname);
            linearLayoutepi = (LinearLayout) findViewById(R.id.linearLayoutepi);
            linearLayoutprsMch = (LinearLayout) findViewById(R.id.linearLayoutprsMch);
            turnGPSOn();

            //GPS Location
            FindLocation();


            //Start Data Sync Scheduler
            try {
                TextView lblAreaText = (TextView) findViewById(R.id.lblAreaText);
                TextView lblAreaName = (TextView) findViewById(R.id.lblAreaName);

                if (g.getProvType().equals("2")) {
                    lblAreaText.setVisibility(View.GONE);
                    //lblAreaText.setText("ওয়ার্ড");
                    //lblAreaName.setText(": "+ ProvArea(g.getProvType(), g.getProvCode()).replace("null",""));
                } else if (g.getProvType().equals("3")) {
                    // lblAreaText.setText("ইউনিট");
                    //  lblAreaName.setText(": "+ ProvArea(g.getProvType(), g.getProvCode()).replace("null",""));
                    // g.setFWAUnit(ProvArea(g.getProvType(), g.getProvCode()));
                    lblAreaText.setVisibility(View.GONE);
                } else {
                    lblAreaText.setVisibility(View.GONE);
                    lblAreaText.setText("");
                }


                SyncScheduler.startOnlyIfConnectedToNetwork(getApplicationContext());
            } catch (Exception ex) {
                Connection.MessageBox(VillageList.this, ex.getMessage());
                return;
            }


            txtUpazila = (TextView) findViewById(R.id.txtUpazila);
            txtUpazila.setText(": " + C.ReturnSingleValue("select cast(upazilaid as varchar(2))||','||upazilaname from upazila where zillaid='" + g.getDistrict() + "' and upazilaid='" + g.getUpazila() + "'"));
            txtUnion = (TextView) findViewById(R.id.txtUnion);
            if (IsUserHI())
            {
                txtUnion.setText(":"+ProvAreaS("11"));
            }
            else {
                txtUnion.setText(": " + C.ReturnSingleValue("select cast(unionid as varchar(2))||','||unionname from Unions where zillaid='" + g.getDistrict() + "' and upazilaid='" + g.getUpazila() + "' and unionid='" + g.getUnion() + "'"));
            }
            g.setFWAUnit(C.ReturnSingleValue("select cast(unionid as varchar(2))||','||unionname from Unions where zillaid='" + g.getDistrict() + "' and upazilaid='" + g.getUpazila() + "' and unionid='" + g.getUnion() + "'"));
            spnMouza = (Spinner) findViewById(R.id.spnMouza);
            //spnMouza.setAdapter( C.getArrayAdapter( "Select '.All' union select mouzaid||'-'||mouzaname from Mouza where zillaid='"+ g.getDistrict() +"' and upazilaid='"+ g.getUpazila() +"' and unionid='"+ g.getUnion() +"'" ) );


            spnWardNew = (Spinner) findViewById(R.id.spnWardNew);
            List<String> wardList = new ArrayList<String>();

            wardList.add("");
            wardList.add("01");
            wardList.add("02");
            wardList.add("03");
            wardList.add("04");
            wardList.add("05");
            wardList.add("06");
            wardList.add("07");
            wardList.add("08");
            wardList.add("09");

            ArrayAdapter<String> adptrWard = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, wardList);
            spnWardNew.setAdapter(adptrWard);

            spnWardOld = (Spinner) findViewById(R.id.spnWardOld);
            List<String> wardListOld = new ArrayList<String>();

            wardListOld.add("");
            wardListOld.add("01");
            wardListOld.add("02");
            wardListOld.add("03");

            ArrayAdapter<String> adptrWardOld = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, wardListOld);
            spnWardOld.setAdapter(adptrWardOld);

            spnFWAUnit = (Spinner) findViewById(R.id.spnFWAUnit);
            //spnFWAUnit.setAdapter(C.getArrayAdapter("select distinct ucode||UNameBan from ProviderArea a,FWAUnit u where a.fwaunit=u.ucode"));
            spnFWAUnit.setAdapter(C.getArrayAdapter("select distinct ucode||'-'||UNameBan from FWAUnit"));


            List<String> listFWAUnit = new ArrayList<String>();

            listFWAUnit.add("");
            listFWAUnit.add("11-১/ক");
            listFWAUnit.add("12-১/খ");
            listFWAUnit.add("13-১/গ");
            listFWAUnit.add("14-১/ঘ");
            listFWAUnit.add("21-২/ক");
            listFWAUnit.add("22-২/খ");
            listFWAUnit.add("23-২/গ");
            listFWAUnit.add("24-২/ঘ");
            listFWAUnit.add("31-৩/ক");
            listFWAUnit.add("32-৩/খ");
            listFWAUnit.add("33-৩/গ");
            listFWAUnit.add("34-৩/ঘ");

            ArrayAdapter<String> adptrFWAUnit = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listFWAUnit);
            spnFWAUnit.setAdapter(adptrFWAUnit);


            spnEPIBlock = (Spinner) findViewById(R.id.spnEPIBlock);
            spnEPIBlock.setAdapter(C.getArrayAdapter("select BCode||'-'||BNameBan from HABlock"));


            List<String> listEPIBlock = new ArrayList<String>();

            listEPIBlock.add("");
            listEPIBlock.add("01-ক-১");
            listEPIBlock.add("02-ক-২");
            listEPIBlock.add("03-খ-১");
            listEPIBlock.add("04-খ-২");
            listEPIBlock.add("05-গ-১");
            listEPIBlock.add("06-গ-২");
            listEPIBlock.add("07-ঘ-১");
            listEPIBlock.add("08-ঘ-২");
            ArrayAdapter<String> adptrEPIBlock = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listEPIBlock);
            spnEPIBlock.setAdapter(adptrEPIBlock);


            //AreaList();

            spnProvider = (Spinner) findViewById(R.id.spnProvider);
            String Type = "";
            if (g.getProvType().equals("10")) {
                Type = "3";
            } else if (g.getProvType().equals("11")) {
                Type = "2";
            } else if (g.getProvType().equals("12")) {
                Type = "2";
            }

            ((Spinner) findViewById(R.id.spnProvider)).setAdapter(C.getArrayAdapter("Select '-' provName from ProviderDb Union Select provCode||'-'||provName from ProviderDb WHERE provType='" + Type + "'"));

            if (IsUserAHI()) {
                spnProvider.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                        if (g.getCallFrom().equals("1")) {
                            if (spnProvider.getSelectedItemPosition() == 0) {

                                GridView g1 = (GridView) findViewById(R.id.gridMenu);
                                vList.setVisibility(View.GONE);
                                g1.setVisibility(View.GONE);
                                //AreaList();

                            } else if (spnProvider.getSelectedItemPosition() > 0) {

                                GridView g1 = (GridView) findViewById(R.id.gridMenu);
                                vList.setVisibility(View.VISIBLE);
                                g1.setVisibility(View.VISIBLE);
                                AreaList();
                                g.setFWAProvCode(Global.Left(spnProvider.getSelectedItem().toString(), 5));
                                //  g.setCallFrom("1");
                            }
                        } else if (g.getCallFrom().equals("2")) {
                            if (spnProvider.getSelectedItemPosition() == 0) {
                                ((Button) findViewById(R.id.cmdahiepi)).setBackgroundColor(Color.parseColor("#D7D7D7"));
                                GridView g1 = (GridView) findViewById(R.id.gridMenu);
                                g1.setVisibility(View.GONE);
                                vList.setVisibility(View.GONE);
                                linearLayoutprsMch.setVisibility(View.GONE);
                                viewa.setVisibility(View.GONE);
                                viewb.setVisibility(View.GONE);
                            } else if (spnProvider.getSelectedItemPosition() > 0) {
                                linearLayoutprsMch.setVisibility(View.VISIBLE);
                                viewa.setVisibility(View.VISIBLE);
                                viewb.setVisibility(View.VISIBLE);
                                vList.setVisibility(View.GONE);
                                //   g.setCallFrom("2");
                            }
                        }


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                    }
                });

            } else if (IsUserHI()) {
                spnProvider.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


                        if (spnProvider.getSelectedItemPosition() == 0) {

                            GridView g1 = (GridView) findViewById(R.id.gridMenu);
                            vList.setVisibility(View.GONE);
                            g1.setVisibility(View.GONE);
                            //AreaList();

                        } else if (spnProvider.getSelectedItemPosition() > 0) {

                            GridView g1 = (GridView) findViewById(R.id.gridMenu);
                            vList.setVisibility(View.VISIBLE);
                            g1.setVisibility(View.VISIBLE);
                            AreaList();
                            g.setFWAProvCode(Global.Left(spnProvider.getSelectedItem().toString(), 5));
                            //  g.setCallFrom("1");
                        }
                    }


                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                    }
                });
            } else {
                spnProvider.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        if (spnProvider.getSelectedItemPosition() == 0) {
                            ((Button) findViewById(R.id.cmdFPIworkplan2)).setBackgroundColor(Color.parseColor("#D7D7D7"));
                            GridView g1 = (GridView) findViewById(R.id.gridMenu);
                            g1.setVisibility(View.GONE);
                            vList.setVisibility(View.GONE);
                            linearLayoutElo.setVisibility(View.GONE);
                            viewa.setVisibility(View.GONE);
                            viewb.setVisibility(View.GONE);
                        } else if (spnProvider.getSelectedItemPosition() > 0) {
                            linearLayoutElo.setVisibility(View.VISIBLE);
                            viewa.setVisibility(View.VISIBLE);
                            viewb.setVisibility(View.VISIBLE);
                            vList.setVisibility(View.GONE);
                        }


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                    }
                });
            }
            if (IsUserAHI()) {

                ((Button) findViewById(R.id.cmdahiepi)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        //((Button)findViewById(R.id.cmdahiepi)).setBackgroundColor(Color.parseColor("#D7D7D7"));
                        g.setCallFrom("1");
                        linearLayoutepi.setVisibility(View.VISIBLE);
                        secfwaname.setVisibility(View.GONE);
                        linearLayoutprsMch.setVisibility(View.GONE);
                        ((Button) findViewById(R.id.cmdahiepi)).setBackgroundColor(Color.parseColor("#99cc33"));
                        ((Button) findViewById(R.id.cmdahiverify)).setBackgroundColor(Color.parseColor("#D7D7D7"));

                        C.Save("delete from FormCall");
                        C.Save("Insert into FormCall (fCall)Values('1')");

                    }
                });

                ((Button) findViewById(R.id.cmdahiverify)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        ((Button) findViewById(R.id.cmdahiepi)).setBackgroundColor(Color.parseColor("#D7D7D7"));

                        secfwaname.setVisibility(View.VISIBLE);
                        linearLayoutepi.setVisibility(View.GONE);
                        ((Button) findViewById(R.id.cmdahiverify)).setBackgroundColor(Color.parseColor("#99cc33"));

                        g.setCallFrom("2");
                        C.Save("delete from FormCall");
                        C.Save("Insert into FormCall (fCall)Values('2')");


                    }
                });

                ((Button) findViewById(R.id.cmdPrs)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {


                        GridView g1 = (GridView) findViewById(R.id.gridMenu);
                        vList.setVisibility(View.VISIBLE);
                        g1.setVisibility(View.VISIBLE);
                        AreaList();
                    }
                });


                ((Button) findViewById(R.id.cmdmch)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {


                        ((Button) findViewById(R.id.cmdmch)).setBackgroundColor(Color.parseColor("#D7D7D7"));
                        vList.setVisibility(View.GONE);
                        g.setFWAProvCode(Global.Left(spnProvider.getSelectedItem().toString(), 5));
                        Intent f1 = new Intent(getApplicationContext(), PregRegView.class);
                        startActivity(f1);
                    }
                });
                ((Button) findViewById(R.id.cmdahiworkplan)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        g.setCallFrom("");
                        ((Button) findViewById(R.id.cmdahiworkplan)).setBackgroundColor(Color.parseColor("#D7D7D7"));
                        vList.setVisibility(View.GONE);
                        Intent f1 = new Intent(getApplicationContext(), HAWorkPlaningView.class);
                        startActivity(f1);
                    }
                });


                ((Button) findViewById(R.id.cmdhhVisit)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        ((Button) findViewById(R.id.cmdSession)).setBackgroundColor(Color.parseColor("#D7D7D7"));
                        ((Button) findViewById(R.id.cmdhhVisit)).setBackgroundColor(Color.parseColor("#99cc33"));
                        secfwaname.setVisibility(View.VISIBLE);


                        //((Button)findViewById(R.id.cmdhhVisit)).setBackgroundColor(Color.parseColor("#D7D7D7"));
                        // vList.setVisibility(View.GONE);
                        // Intent f1 = new Intent(getApplicationContext(), EPIBARIVisit.class);
                        //startActivity(f1);
                    }
                });

                ((Button) findViewById(R.id.cmdSession)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        ((Button) findViewById(R.id.cmdhhVisit)).setBackgroundColor(Color.parseColor("#D7D7D7"));
                        ((Button) findViewById(R.id.cmdSession)).setBackgroundColor(Color.parseColor("#99cc33"));
                        secfwaname.setVisibility(View.GONE);
                         vList.setVisibility(View.GONE);
                        Intent f1 = new Intent(getApplicationContext(), EPISESSIONVisit.class);
                        startActivity(f1);
                    }
                });

                ((Button) findViewById(R.id.cmdAHIworkplan1)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        g.setCallFrom("");


                        // ((Button)findViewById(R.id.cmdahiworkplan)).setBackgroundColor(Color.parseColor("#D7D7D7"));
                        vList.setVisibility(View.GONE);
                        Intent f1 = new Intent(getApplicationContext(), AHIWorkPlaning.class);
                        //Intent f1 = new Intent(getApplicationContext(), HAWorkPlaning.class);
                        startActivity(f1);
                    }
                });

            } else if (IsUserHI()) {
                ((Button) findViewById(R.id.cmdahiepi)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        //((Button)findViewById(R.id.cmdahiepi)).setBackgroundColor(Color.parseColor("#D7D7D7"));

                        linearLayoutepi.setVisibility(View.VISIBLE);

                        ((Button) findViewById(R.id.cmdahiepi)).setBackgroundColor(Color.parseColor("#99cc33"));


                    }
                });


                ((Button) findViewById(R.id.cmdhhVisit)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {


                        secfwaname.setVisibility(View.VISIBLE);

                        C.Save("delete from FormCall");
                        C.Save("Insert into FormCall (fCall)Values('1')");


                    }
                });

                ((Button) findViewById(R.id.cmdSession)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {


                        Intent f1 = new Intent(getApplicationContext(), EPISESSIONVisit.class);
                        startActivity(f1);
                    }
                });

                ((Button) findViewById(R.id.cmdFPIworkplan)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        g.setCallFrom("");


                        // ((Button)findViewById(R.id.cmdahiworkplan)).setBackgroundColor(Color.parseColor("#D7D7D7"));
                        vList.setVisibility(View.GONE);
                        Intent f1 = new Intent(getApplicationContext(), HIWorkPlaning.class);
                        startActivity(f1);
                    }
                });

                ((Button) findViewById(R.id.cmdahiworkplan)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        g.setCallFrom("");


                        // ((Button)findViewById(R.id.cmdahiworkplan)).setBackgroundColor(Color.parseColor("#D7D7D7"));
                        vList.setVisibility(View.GONE);
                        Intent f1 = new Intent(getApplicationContext(), AHIWorkPlaningViewAproved.class);
                        startActivity(f1);
                    }
                });
            } else {


                ((Button) findViewById(R.id.cmdpaworkplan)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        // g.setCallFrom("1");
                        Intent f1 = new Intent(getApplicationContext(), FpaWorkPlaning.class);
                        startActivity(f1);
                    }
                });

                ((Button) findViewById(R.id.cmdfpaworkplan)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        ((Button) findViewById(R.id.cmdFPIworkplan2)).setBackgroundColor(Color.parseColor("#D7D7D7"));
                        vList.setVisibility(View.GONE);
                        secfwaname.setVisibility(View.GONE);
                        linearLayoutElo.setVisibility(View.GONE);
                        viewa.setVisibility(View.GONE);
                        viewb.setVisibility(View.GONE);
                        Intent f1 = new Intent(getApplicationContext(), FpaWorkPlaningView.class);
                        startActivity(f1);
                    }
                });

                ((Button) findViewById(R.id.cmdFPIworkplan)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        // g.setCallFrom("1");
                        Intent f1 = new Intent(getApplicationContext(), FpiWorkPlaning.class);
                        startActivity(f1);
                    }
                });

                ((Button) findViewById(R.id.cmdFPIworkplanview)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        //g.setCallFrom("2");
                        Intent f1 = new Intent(getApplicationContext(), FpiWorkPlaningView.class);
                        startActivity(f1);
                    }
                });

                ((Button) findViewById(R.id.cmdFPIworkplan1)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {


                        ((Button) findViewById(R.id.cmdFPIworkplan2)).setBackgroundColor(Color.parseColor("#D7D7D7"));
                        vList.setVisibility(View.GONE);
                        secfwaname.setVisibility(View.GONE);
                        linearLayoutElo.setVisibility(View.GONE);
                        viewa.setVisibility(View.GONE);
                        viewb.setVisibility(View.GONE);
                        Intent f1 = new Intent(getApplicationContext(), FPIMonitoring.class);
                        startActivity(f1);
                    }
                });

                ((Button) findViewById(R.id.cmdFPIworkplan2)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        secfwaname.setVisibility(View.VISIBLE);


                        ((Button) findViewById(R.id.cmdFPIworkplan2)).setBackgroundColor(Color.parseColor("#99cc33"));
                        vList.setBackgroundColor(Color.GRAY);


                    }
                });
                ((Button) findViewById(R.id.cmdPrs)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {


                        GridView g1 = (GridView) findViewById(R.id.gridMenu);
                        vList.setVisibility(View.VISIBLE);
                        g1.setVisibility(View.VISIBLE);
                        AreaList();
                    }
                });
                ((Button) findViewById(R.id.cmdElco)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {


                        ((Button) findViewById(R.id.cmdElco)).setBackgroundColor(Color.parseColor("#D7D7D7"));
                        vList.setVisibility(View.GONE);
                        // secfwaname.setVisibility(View.GONE);
                        // Intent f1 = new Intent(getApplicationContext(), ELCOForm.class);

                        g.setFWAProvCode(Global.Left(spnProvider.getSelectedItem().toString(), 5));
                        Intent f1 = new Intent(getApplicationContext(), Elcoview.class);
                        startActivity(f1);
                    }
                });

                ((Button) findViewById(R.id.cmdmch)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {


                        ((Button) findViewById(R.id.cmdmch)).setBackgroundColor(Color.parseColor("#D7D7D7"));
                        vList.setVisibility(View.GONE);
                        g.setFWAProvCode(Global.Left(spnProvider.getSelectedItem().toString(), 5));
                        Intent f1 = new Intent(getApplicationContext(), PregRegView.class);
                        startActivity(f1);
                    }
                });

            }


            VlblRegister = (TextView) findViewById(R.id.VlblRegister);
            if (IsUserAHI()) {
                //setContentView(R.layout.villagelist1);
                // VlblRegister.setText("  স্বাস্থ্য সহকারী রেজিস্টারের অংশ সমূহ");
                // ((Button) findViewById(R.id.cmdPreg)).setVisibility(View.VISIBLE);
                //((Button) findViewById(R.id.cmdPreg)).setVisibility(View.VISIBLE);
                //((Button)findViewById(R.id.cmdu5Child)).setVisibility(View.GONE);
                //((Button)findViewById(R.id.cmdadolescent)).setVisibility(View.GONE);
                //((Button) findViewById(R.id.cmdstock)).setVisibility(View.GONE);
                //((Button) findViewById(R.id.cmdWInjectable)).setVisibility(View.GONE);
                //((Button) findViewById(R.id.cmdStockAdjustment)).setVisibility(View.GONE);
                //((Button)findViewById(R.id.cmdnrecqu)).setVisibility(View.GONE);
            } else {
                // VlblRegister.setText("  পরিবার কল্যাণ পরিদর্শকের মডিউলের অংশ সমূহ");
                /*((Button) findViewById(R.id.cmdelco)).setVisibility(View.VISIBLE);
                ((Button) findViewById(R.id.cmdPreg)).setVisibility(View.VISIBLE);
                ((Button) findViewById(R.id.cmdWInjectable)).setVisibility(View.VISIBLE);
                ((Button) findViewById(R.id.cmdstock)).setVisibility(View.VISIBLE);
                ((Button) findViewById(R.id.cmdStockAdjustment)).setVisibility(View.VISIBLE);
                ((Button)findViewById(R.id.cmdu5Child)).setVisibility(View.VISIBLE);
                ((Button)findViewById(R.id.cmdadolescent)).setVisibility(View.VISIBLE);*/
            }

        } catch (Exception ex) {
            Connection.MessageBox(VillageList.this, ex.getMessage());
        }
    }

    private String ProvAreaS(String ProvType) {
        String SQL = "";
        //SQL = "select distinct fwaunit from ProviderArea WHERE provCode = '"+ ProvCode +"'";
        SQL = " select UNIONID||'-'||UNIONNAME as UNIONNAME from Unions where UNIONID in (Select UNIONID from ProviderDB where ProvType<>'99' and ProvType='" + ProvType + "')";

        Cursor cur = C.ReadData(SQL);
        String retValue = "";
        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            //retValue=cur.getString(0);

            /*if(ProvCode.equals("02"))
                retValue=cur.getString(0);
            else if(ProvCode.equals("03"))
                retValue=cur.getString(0);
            */
            retValue = retValue.length() > 0 ? retValue + ", " + cur.getString(0) : cur.getString(0);



            cur.moveToNext();
        }
        cur.close();

        return retValue;
    }
    private String ProvArea(String ProvType, String ProvCode) {
        String SQL = "";
        if (ProvType.equals("2"))
            SQL = "select distinct ward from ProviderArea WHERE provCode = '" + ProvCode + "'";
        else if (ProvType.equals("3"))
            SQL = "select distinct fwaunit from ProviderArea WHERE provCode = '" + ProvCode + "'";

        Cursor cur = C.ReadData(SQL);
        String retValue = "";
        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            //retValue=cur.getString(0);

            /*if(ProvCode.equals("02"))
                retValue=cur.getString(0);
            else if(ProvCode.equals("03"))
                retValue=cur.getString(0);
            */

            retValue = retValue.length() > 0 ? retValue + ", " + cur.getString(0) : cur.getString(0);

            cur.moveToNext();
        }
        cur.close();

        return retValue;
    }

    public void onResume() {
        super.onResume();

        /*
        //Start Data Sync Scheduler
        try {
            SyncScheduler.startOnlyIfConnectedToNetwork(getApplicationContext());
        }
        catch(Exception ex)
        {
            Connection.MessageBox(VillageList.this,ex.getMessage());
            return;
        }*/

    }


    TextView txtVDate;
    ImageButton btnVDate;
    String VariableID;
    TextView txtHHNo;
    TextView txtPresent;
    TextView txtAbsent;
    TextView txtTotalMem;

    private void ReportForm() {
        final Dialog dialog = new Dialog(VillageList.this);
        dialog.setTitle("Report Form");
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.visitedstatus);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        txtHHNo = (TextView) dialog.findViewById(R.id.txtTotalHH);
        txtPresent = (TextView) dialog.findViewById(R.id.txtPresent);
        txtAbsent = (TextView) dialog.findViewById(R.id.txtAbsent);
        txtTotalMem = (TextView) dialog.findViewById(R.id.txtTotalMem);

        txtVDate = (TextView) dialog.findViewById(R.id.txtVDate);
        txtVDate.setText(Global.DateNowDMY());
        btnVDate = (ImageButton) dialog.findViewById(R.id.btnVDate);
        btnVDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariableID = "btnVDate";
                showDialog(DATE_DIALOG);
            }
        });


        Button cmdPrev = (Button) dialog.findViewById(R.id.cmdPrev);
        cmdPrev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        Button cmdNext = (Button) dialog.findViewById(R.id.cmdNext);
        cmdNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        VisitedStatus(Global.DateConvertYMD(txtVDate.getText().toString()));


        dialog.show();
    }

    private void VisitedStatus(String VisitDate) {
        txtHHNo.setText(C.ReturnSingleValue("Select Count(*)Total from Household  where strftime('%Y-%m-%d',EnDt)='" + VisitDate + "'"));
        txtTotalMem.setText(C.ReturnSingleValue("Select Count(*)Total from Member  where strftime('%Y-%m-%d',EnDt)='" + VisitDate + "'"));
        //txtPresent.setText(C.ReturnSingleValue("Select count(distinct dist||upz||un||mouza||vill||hhno) from Member  where strftime('%Y-%m-%d',EnDt)='"+ VisitDate +"'"));
        //txtAbsent.setText(C.ReturnSingleValue("select count(*)Total from Household h where not exists(select dist from Member where dist||upz||un||mouza||vill||hhno=h.dist||h.upz||h.un||h.mouza||h.vill||h.hhno) and strftime('%Y-%m-%d',h.EnDt)='"+ VisitDate +"'"));


        /*Cursor cur = C.ReadData("");
        cur.moveToFirst();

        while(!cur.isAfterLast())
        {
            txtHHNo.setText(C.ReturnSingleValue("Select Count(*)Total from Household  where strftime('%Y-%m-%d',EnDt)='"+ VisitDate +"'"));
            txtTotalMem.setText(C.ReturnSingleValue("Select Count(*)Total from Member  where strftime('%Y-%m-%d',EnDt)='"+ VisitDate +"'"));

            //vcode[0][i]= cur.getString(cur.getColumnIndex("dist"));
            //i +=1;
            cur.moveToNext();
        }
        cur.close();*/
    }


    public void AreaList() {

        GridView g1 = (GridView) findViewById(R.id.gridMenu);
        g1.setAdapter(new GridAdapter(this));


        // String S = "select count(villageid)TotalVill from Village where zillaid='" + g.getDistrict() + "' and upazilaid='" + g.getUpazila() + "' and unionid='" + g.getUnion() + "'";

       /* int total = Integer.parseInt(C.ReturnSingleValue(S));
        if (total > 0) {
            if (total > 4) {

                int height = ((GridView) findViewById(R.id.gridMenu)).getHeight();
                ((LinearLayout) findViewById(R.id.vList)).setLayoutParams(new LinearLayout.LayoutParams(((LinearLayout) findViewById(R.id.vList)).getWidth(), 800));
                //((LinearLayout) findViewById(R.id.vList)).getLayoutParams().height = 1000;
                //((LinearLayout)findViewById(R.id.vList)).getLayoutParams().width = 200;
                ((LinearLayout) findViewById(R.id.vList)).requestLayout();


            }
        }*/
    }


   /* public class GridAdapter extends BaseAdapter {
        private Context mContext;
        String[][] vcode;
        String MouzaCode;
        Integer totalRec;

        public GridAdapter(Context c) {
            mContext = c;
            //MouzaCode = Mouza;
        }

        public int getCount() {
            //String M = Global.Left(spnMouza.getSelectedItem().toString(), 3).equalsIgnoreCase(".al")?"%":Global.Left(spnMouza.getSelectedItem().toString(), 3);
            //String S = "select count(villageid)TotalVill from Village where zillaid='"+ g.getDistrict() +"' and upazilaid='"+ g.getUpazila() +"' and unionid='"+ g.getUnion() +"' and Mouzaid like('"+ M +"')";
            String S = "select count(villageid)TotalVill from Village where zillaid='" + g.getDistrict() + "' and upazilaid='" + g.getUpazila() + "' and unionid='" + g.getUnion() + "'";



            return Integer.parseInt(C.ReturnSingleValue(S));
        }

        public Object getItem(int position){
            return null;
        }

        public long getItemId(int position){
            return 0;
        }

        //Create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            View MyView = convertView;
            if (convertView == null) {
                LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                MyView = li.inflate(R.layout.single_grid_item, null);

                String SQL  = "";
                String SQL1 = "";
                //String M1 = Global.Left(spnMouza.getSelectedItem().toString(), 3).equalsIgnoreCase(".al")?"%":Global.Left(spnMouza.getSelectedItem().toString(), 3);

                SQL  = "SELECT v.zillaid AS dist, v.upazilaid AS upazila,v.unionid AS unions,v.mouzaid AS mouza,v.villageid AS village,v.villagename AS villname,count( h.dist ) AS totalhh," +
                        "M.MouzaName AS mouzaname from Village v";
                SQL  += " LEFT outer join Household h on v.zillaid=h.dist and v.upazilaid=h.upz and v.unionid=h.un and v.mouzaid=h.mouza and v.villageid=h.vill";
                SQL +=" LEFT JOIN Mouza M ON M.upazilaid = h.upz AND M.unionid = h.un AND M.mouzaid = h.mouza ";

                SQL  += " where v.zillaid='"+ g.getDistrict() +"' and v.upazilaid='"+ g.getUpazila() +"' and v.unionid='"+ g.getUnion() +"'";
                SQL  += " group by v.zillaid,v.upazilaid,v.unionid,v.mouzaid,v.villageid,v.villagename";

                Cursor cur = C.ReadData(SQL);
                cur.moveToFirst();

                totalRec = getCount();
                vcode=new String[8][totalRec];
                int i=0;
                while(!cur.isAfterLast())
                {
                    vcode[0][i]= cur.getString(cur.getColumnIndex("dist"));
                    vcode[1][i]= cur.getString(cur.getColumnIndex("upazila"));
                    vcode[2][i]= cur.getString(cur.getColumnIndex("unions"));
                    vcode[3][i]= cur.getString(cur.getColumnIndex("mouza"));
                    vcode[4][i]= cur.getString(cur.getColumnIndex("village"));
                    vcode[5][i]= cur.getString(cur.getColumnIndex("villname"));
                    vcode[6][i]= cur.getString(cur.getColumnIndex("totalhh"));
                    vcode[7][i]= cur.getString(cur.getColumnIndex("mouzaname"));

                    i +=1;
                    cur.moveToNext();
                }
                *//*if(i>7)
                {

                    int height=((LinearLayout)findViewById(R.id.vList)).getHeight();
                    ((LinearLayout)findViewById(R.id.vList)).getLayoutParams().height = height*2;
                    //((LinearLayout)findViewById(R.id.vList)).getLayoutParams().width = 200;
                    ((LinearLayout)findViewById(R.id.vList)).requestLayout();


                }*//*
                cur.close();

                Button tv = (Button)MyView.findViewById(R.id.image_name);
                tv.setTextSize(14);
                tv.setText("মৌজা: " + vcode[3][position]+ "-" +vcode[7][position] + "\n" +
                        " গ্রাম: "+vcode[4][position]+"-"+vcode[5][position]+"\n("+vcode[6][position]+")");
                final Integer p = position;
                tv.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        finish();

                        final ProgressDialog progDailog = ProgressDialog.show(
                                VillageList.this, "", "অপেক্ষা করুন ...", true);
                        new Thread() {
                            public void run() {
                                g.setMouza(vcode[3][p].toString());
                                g.setVillage(vcode[4][p].toString());
                                g.setVillageName(vcode[5][p].toString());
                                // g.setMouzaName(vcode[7][p].toString());
                                Intent f1 = new Intent(getApplicationContext(),HouseholdIndex.class);
                                startActivity(f1);

                                progDailog.dismiss();

                            }
                        }.start();




                    }
                });
            }
            return MyView;
        }

    }
*/

    public class GridAdapter extends BaseAdapter {
        private Context mContext;
        String[][] vcode;
        String MouzaCode;
        Integer totalRec;

        public GridAdapter(Context c) {
            mContext = c;
            //MouzaCode = Mouza;
        }

        public int getCount() {
            //String M = Global.Left(spnMouza.getSelectedItem().toString(), 3).equalsIgnoreCase(".al")?"%":Global.Left(spnMouza.getSelectedItem().toString(), 3);
            //String S = "select count(villageid)TotalVill from Village where zillaid='"+ g.getDistrict() +"' and upazilaid='"+ g.getUpazila() +"' and unionid='"+ g.getUnion() +"' and Mouzaid like('"+ M +"')";
            //  String S = "select count(villageid)TotalVill from Village where zillaid='" + g.getDistrict() + "' and upazilaid='" + g.getUpazila() + "' and unionid='" + g.getUnion() + "'";

            String ProvType = C.ReturnSingleValue("SELECT ProvType FROM ProviderDb WHERE ProvCode ='" + Connection.split(((Spinner) findViewById(R.id.spnProvider)).getSelectedItem().toString(), '-')[0] + "'");
            String S = "SELECT COUNT(*) FROM ProviderArea WHERE ProvCode = '" +
                    Connection.split(((Spinner) findViewById(R.id.spnProvider)).getSelectedItem().toString(), '-')[0] + "' AND ProvType ='" + ProvType + "'";

            return Integer.parseInt(C.ReturnSingleValue(S));
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        //Create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            View MyView = convertView;
            if (convertView == null) {
                LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                MyView = li.inflate(R.layout.single_grid_item, null);

                String SQL = "";
                String SQL1 = "";
                //String M1 = Global.Left(spnMouza.getSelectedItem().toString(), 3).equalsIgnoreCase(".al")?"%":Global.Left(spnMouza.getSelectedItem().toString(), 3);

                String ProvType = C.ReturnSingleValue("SELECT ProvType FROM ProviderDb WHERE ProvCode ='" + Connection.split(((Spinner) findViewById(R.id.spnProvider)).getSelectedItem().toString(), '-')[0] + "'");
                String Union = C.ReturnSingleValue("SELECT unionid FROM ProviderDb WHERE ProvCode ='" + Connection.split(((Spinner) findViewById(R.id.spnProvider)).getSelectedItem().toString(), '-')[0] + "'");

                SQL = "SELECT v.zillaid AS dist, v.upazilaid AS upazila,v.unionid AS unions,v.mouzaid AS mouza,v.villageid AS village," +
                        "v.villagename AS villname,count( h.dist ) AS totalhh," +
                        "M.MouzaName AS mouzaname from Village v";
                SQL += " LEFT outer join Household h on v.zillaid=h.dist and v.upazilaid=h.upz and v.unionid=h.un and" +
                        " v.mouzaid=h.mouza and v.villageid=h.vill";
                SQL += " LEFT JOIN Mouza M ON M.upazilaid = h.upz AND M.unionid = h.un AND M.mouzaid = h.mouza ";

                SQL += "INNER JOIN ProviderArea PA ON PA.zillaid = v.zillaid " +
                        "AND PA.upazilaid = v.UPAZILAID " +
                        "AND PA.unionid = v.UNIONID " +
                        "AND PA.mouzaid = v.MOUZAID " +
                        "AND PA.villageid = v.VILLAGEID " +
                        "AND PA.ProvCode = '" + Connection.split(((Spinner) findViewById(R.id.spnProvider)).getSelectedItem().toString(), '-')[0] +
                        "' AND PA.ProvType ='" + ProvType + "'";

                SQL += " where v.zillaid='" + g.getDistrict() + "' and v.upazilaid='" + g.getUpazila() +/* "'";*/
                        "' and v.unionid='" + Union + "'";
                SQL += " group by v.zillaid,v.upazilaid,v.unionid,v.mouzaid,v.villageid,v.villagename";

                Cursor cur = C.ReadData(SQL);
                cur.moveToFirst();

                totalRec = getCount();
                vcode = new String[8][totalRec];
                int i = 0;
                while (!cur.isAfterLast()) {
                    vcode[0][i] = cur.getString(cur.getColumnIndex("dist"));
                    vcode[1][i] = cur.getString(cur.getColumnIndex("upazila"));
                    vcode[2][i] = cur.getString(cur.getColumnIndex("unions"));
                    vcode[3][i] = cur.getString(cur.getColumnIndex("mouza"));
                    vcode[4][i] = cur.getString(cur.getColumnIndex("village"));
                    vcode[5][i] = cur.getString(cur.getColumnIndex("villname"));

                    if (cur.getString(cur.getColumnIndex("totalhh")).equalsIgnoreCase("0")) {
                        vcode[6][i] = "খানা/সদস্যের তথ্য ডাউনলোড করতে হবে";/*ডেটা*/
                    } else {
                        vcode[6][i] = cur.getString(cur.getColumnIndex("totalhh"));
                    }
                    //vcode[6][i] = if(cur.getString(cur.getColumnIndex("totalhh")).equalsIgnoreCase("0")) {"Click here to download data"};
                    vcode[7][i] = cur.getString(cur.getColumnIndex("mouzaname"));
//.replace("null","")
                    i += 1;
                    cur.moveToNext();
                }
                /*if(i>7)
                {

                    int height=((LinearLayout)findViewById(R.id.vList)).getHeight();
                    ((LinearLayout)findViewById(R.id.vList)).getLayoutParams().height = height*2;
                    //((LinearLayout)findViewById(R.id.vList)).getLayoutParams().width = 200;
                    ((LinearLayout)findViewById(R.id.vList)).requestLayout();


                }*/
                cur.close();

                Button tv = (Button) MyView.findViewById(R.id.image_name);
                tv.setTextSize(14);
                tv.setText("মৌজা: " + vcode[3][position] + "-" + vcode[7][position] + "\n" +
                        " গ্রাম: " + vcode[4][position] + "-" + vcode[5][position] + "\n(" + vcode[6][position] + ")");
                final Integer p = position;
                tv.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        finish();

                        final ProgressDialog progDailog = ProgressDialog.show(
                                VillageList.this, "", "অপেক্ষা করুন ...", true);
                        new Thread() {
                            public void run() {
                                g.setMouza(vcode[3][p].toString());
                                g.setVillage(vcode[4][p].toString());
                                g.setVillageName(vcode[5][p].toString());
                                // g.setMouzaName(vcode[7][p].toString());
                                g.setUnion(vcode[2][p].toString());
                                Intent f1 = new Intent(getApplicationContext(), HouseholdIndex.class);
                                startActivity(f1);

                                progDailog.dismiss();

                            }
                        }.start();


                    }
                });
            }
            return MyView;
        }

    }


    //GPS Reading
    //.....................................................................................................
    public void FindLocation() {
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                updateLocation(location);
            }

            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    void updateLocation(Location location) {
        currentLocation = location;
        currentLatitude = currentLocation.getLatitude();
        currentLongitude = currentLocation.getLongitude();
    }

    //Method to turn on GPS
    public void turnGPSOn() {
        try {
            String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

            if (!provider.contains("gps")) { //if gps is disabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                sendBroadcast(poke);
            }
        } catch (Exception e) {

        }
    }

    //Method to turn off the GPS
    public void turnGPSOff() {
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (provider.contains("gps")) { //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }

    //turning off the GPS if its in on state. to avoid the battery drain.
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        turnGPSOff();
    }


    protected Dialog onCreateDialog(int id) {
        final Calendar c = Calendar.getInstance();
        Integer Y = g.mYear;
        Integer M = g.mMonth;
        Integer D = g.mDay;

        if (txtVDate.getText().length() > 0) {
            Y = Integer.valueOf(Global.Right(txtVDate.getText().toString(), 4));
            M = Integer.valueOf(txtVDate.getText().toString().substring(4, 5));
            D = Integer.valueOf(Global.Left(txtVDate.getText().toString(), 2));
        }
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mDateSetListener, Y, M - 1, D);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear + 1;
            mDay = dayOfMonth;
            txtVDate.setText(new StringBuilder()
                    .append(Global.Right("00" + mDay, 2)).append("/")
                    .append(Global.Right("00" + mMonth, 2)).append("/")
                    .append(mYear));

            VisitedStatus(Global.DateConvertYMD(txtVDate.getText().toString()));
        }
    };
}