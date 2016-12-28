package com.data.rhis2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import Common.Connection;
import Common.Global;

/**
 * Created by Nisan on 4/7/2016.
 */
public class FpiWorkPlaningView extends Activity {
    Calendar calendar;
    boolean netwoekAvailable = false;
    Location currentLocation;
    double currentLatitude, currentLongitude;
    Location currentLocationNet;
    double currentLatitudeNet, currentLongitudeNet;

    //Disabled Back/Home key
    //--------------------------------------------------------------------------------------------------
    @Override
    public boolean onKeyDown(int iKeyCode, KeyEvent event) {
        if (iKeyCode == KeyEvent.KEYCODE_BACK || iKeyCode == KeyEvent.KEYCODE_HOME) {
            return false;
        } else {
            return true;
        }
    }

    //Top menu
    //--------------------------------------------------------------------------------------------------
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mnuclose, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder adb = new AlertDialog.Builder(FpiWorkPlaningView.this);
        switch (item.getItemId()) {
            case R.id.menuClose:
                adb.setTitle("Close");
                adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান?");
                // adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান[Yes/No]?");
                adb.setNegativeButton("না", null);
                adb.setPositiveButton("হ্যাঁ", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                adb.show();
                return true;
        }
        return false;
    }

    String VariableID;
    private int hour;
    private int minute;
    private int mDay;
    private int mMonth;
    private int mYear;
    static final int DATE_DIALOG = 1;
    static final int TIME_DIALOG = 2;
    public static String[] namesOfDays = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};

    Connection C;
    Global g;
    SimpleAdapter dataAdapter;
    ArrayList<String> FPAValue = new ArrayList<String>();
    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    private String TableName;
    private String TableNameDetail;
    //public static String workplanId1;
    LinearLayout secS10;
    LinearLayout secSlNo;
    TextView VlblSlNo;



    LinearLayout secFPIPMonth;
    TextView VlblFPIPMonth;
    Spinner spnFPIPMonth;

    TextView lblHS10;
    LinearLayout secReq;
    TextView VlblReqName;
    TextView txtReqName;
    LinearLayout secReqToCode;
    TextView VlblReqToCode;
    TextView txtFpiWarea;

    Spinner spnVillage1;

    Button cmdSync;
    String StartTime;
    String DeviceNo;
    ListView list;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.fpiworkplainingview);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();

            list = (ListView) findViewById(R.id.lstData);
            // View header = getLayoutInflater().inflate(R.layout.fpaworkplanheading, null);
            // list.addHeaderView(header);

            TableName = "workPlanMaster";
            TableNameDetail = "workPlanDetail";

            lblHS10 = (TextView) findViewById(R.id.lblHS10);
            secS10 = (LinearLayout) findViewById(R.id.secS10);
            secSlNo = (LinearLayout) findViewById(R.id.secSlNo);
            VlblSlNo = (TextView) findViewById(R.id.VlblSlNo);
          //  cmdRefresh= (Button) findViewById(R.id.cmdRefresh);
            cmdSync = (Button) findViewById(R.id.cmdSync);
            secFPIPMonth = (LinearLayout) findViewById(R.id.secFPIPMonth);
            VlblFPIPMonth = (TextView) findViewById(R.id.VlblFPIPMonth);
            spnFPIPMonth = (Spinner) findViewById(R.id.spnFPIPMonth);
            String Month=C.ReturnSingleValue("select strftime( '%m', 'now' ) as m");
            if(Month.equals("12"))
            {
                spnFPIPMonth.setAdapter(C.getArrayAdapterMultiline("SELECT '99-- সিলেক্ট মাস-' AS ym\n" +
                        " \n" +
                        "UNION SELECT ( CASE WHEN id=strftime( '%m', 'now' ) then '01-January,'||cast(strftime( '%Y', date( 'now' )  )+1 as int) else ''  END ) AS ym FROM month where id=12\n" +
                        " Union\n" +
                        "\n" +
                        "\n" +
                        "SELECT substr( '0' || id, -2, 2 )  || '-' || mName || ',' || strftime( '%Y', date( 'now' )  )  AS ym\n" +
                        "  FROM month\n" +
                        " WHERE id<>1 and  id<>2 and  id<>3 and  id<>4 and  id<>5 and  id<>6 and id <=( \n" +
                        "           SELECT strftime( '%m', 'now' ) + 1 \n" +
                        "       ) \n" +
                        "       \n" +
                        " ORDER BY ym DESC"));

            }

            else if(Month.equals("11"))
            {
                spnFPIPMonth.setAdapter(C.getArrayAdapterMultiline("SELECT substr('0' ||id, -2, 2)||'-'||mName||','||strftime('%Y', date('now'))  as ym from month  where id<>1 and  id<>2 and  id<>3 and  id<>4 and  id<>5  and id <=(SELECT strftime('%m','now')+1) order by id Desc"));


            }

            else if(Month.equals("10"))
            {
                spnFPIPMonth.setAdapter(C.getArrayAdapterMultiline("SELECT substr('0' ||id, -2, 2)||'-'||mName||','||strftime('%Y', date('now'))  as ym from month  where id<>1 and  id<>2 and  id<>3 and  id<>4  and id <=(SELECT strftime('%m','now')+1) order by id Desc"));


            }
            else if(Month.equals("09"))
            {
                spnFPIPMonth.setAdapter(C.getArrayAdapterMultiline("SELECT substr('0' ||id, -2, 2)||'-'||mName||','||strftime('%Y', date('now'))  as ym from month  where id<>1 and  id<>2 and  id<>3   and id <=(SELECT strftime('%m','now')+1) order by id Desc"));


            }

            else if(Month.equals("08"))
            {
                spnFPIPMonth.setAdapter(C.getArrayAdapterMultiline("SELECT substr('0' ||id, -2, 2)||'-'||mName||','||strftime('%Y', date('now'))  as ym from month  where id<>1 and  id<>2 and id <=(SELECT strftime('%m','now')+1) order by id Desc"));


            }
            else if(Month.equals("07"))
            {
                spnFPIPMonth.setAdapter(C.getArrayAdapterMultiline("SELECT substr('0' ||id, -2, 2)||'-'||mName||','||strftime('%Y', date('now'))  as ym from month  where id<>1 and id <=(SELECT strftime('%m','now')+1) order by id Desc"));


            }
            else if(Month.equals("06"))
            {
                spnFPIPMonth.setAdapter(C.getArrayAdapterMultiline("SELECT substr('0' ||id, -2, 2)||'-'||mName||','||strftime('%Y', date('now'))  as ym from month  where id <=(SELECT strftime('%m','now')+1) order by id Desc"));


            }

            else if(Month.equals("05"))
            {
                spnFPIPMonth.setAdapter(C.getArrayAdapterMultiline("SELECT '99-- সিলেক্ট মাস-' AS ym\n" +
                        " \n" +
                        " Union SELECT substr( '0' || id, -2, 2 )  || '-' || mName || ',' || cast(strftime( '%Y', date( 'now' )  )-1 as int)  AS ym  FROM month WHERE  id >=11\n" +
                        " Union\n" +
                        "\n" +
                        "\n" +
                        "SELECT substr( '0' || id, -2, 2 )  || '-' || mName || ',' || strftime( '%Y', date( 'now' )  )  AS ym\n" +
                        "  FROM month\n" +
                        " WHERE  id <=( \n" +
                        "           SELECT strftime( '%m', 'now' ) + 1 \n" +
                        "       ) \n" +
                        "       \n" +
                        " ORDER BY ym DESC"));

            }
            else if(Month.equals("04"))
            {
                spnFPIPMonth.setAdapter(C.getArrayAdapterMultiline("SELECT '99-- সিলেক্ট মাস-' AS ym\n" +
                        " \n" +
                        " Union SELECT substr( '0' || id, -2, 2 )  || '-' || mName || ',' || cast(strftime( '%Y', date( 'now' )  )-1 as int)  AS ym  FROM month WHERE  id >=10\n" +
                        " Union\n" +
                        "\n" +
                        "\n" +
                        "SELECT substr( '0' || id, -2, 2 )  || '-' || mName || ',' || strftime( '%Y', date( 'now' )  )  AS ym\n" +
                        "  FROM month\n" +
                        " WHERE  id <=( \n" +
                        "           SELECT strftime( '%m', 'now' ) + 1 \n" +
                        "       ) \n" +
                        "       \n" +
                        " ORDER BY ym DESC"));

            }

            else if(Month.equals("03"))
            {
                spnFPIPMonth.setAdapter(C.getArrayAdapterMultiline("SELECT '99-- সিলেক্ট মাস-' AS ym\n" +
                        " \n" +
                        "Union SELECT substr( '0' || id, -2, 2 )  || '-' || mName || ',' || cast(strftime( '%Y', date( 'now' )  )-1 as int)  AS ym  FROM month WHERE  id >=9\n" +
                        " Union\n" +
                        "\n" +
                        "\n" +
                        "SELECT substr( '0' || id, -2, 2 )  || '-' || mName || ',' || strftime( '%Y', date( 'now' )  )  AS ym\n" +
                        "  FROM month\n" +
                        " WHERE  id <=( \n" +
                        "           SELECT strftime( '%m', 'now' ) + 1 \n" +
                        "       ) \n" +
                        "       \n" +
                        " ORDER BY ym DESC"));

            }

            else if(Month.equals("02"))
            {
                spnFPIPMonth.setAdapter(C.getArrayAdapterMultiline("SELECT '99-- সিলেক্ট মাস-' AS ym\n" +
                        " \n" +
                        " Union SELECT substr( '0' || id, -2, 2 )  || '-' || mName || ',' || cast(strftime( '%Y', date( 'now' )  )-1 as int)  AS ym  FROM month WHERE  id >=8\n" +
                        " Union\n" +
                        "\n" +
                        "\n" +
                        "SELECT substr( '0' || id, -2, 2 )  || '-' || mName || ',' || strftime( '%Y', date( 'now' )  )  AS ym\n" +
                        "  FROM month\n" +
                        " WHERE  id <=( \n" +
                        "           SELECT strftime( '%m', 'now' ) + 1 \n" +
                        "       ) \n" +
                        "       \n" +
                        " ORDER BY ym DESC"));

            }

            else if(Month.equals("01"))
            {
                spnFPIPMonth.setAdapter(C.getArrayAdapterMultiline("SELECT '99-- সিলেক্ট মাস-' AS ym\n" +
                        " \n" +
                        "Union SELECT substr( '0' || id, -2, 2 )  || '-' || mName || ',' || cast(strftime( '%Y', date( 'now' )  )-1 as int)  AS ym  FROM month WHERE  id >=7\n" +
                        " Union\n" +
                        "\n" +
                        "\n" +
                        "SELECT substr( '0' || id, -2, 2 )  || '-' || mName || ',' || strftime( '%Y', date( 'now' )  )  AS ym\n" +
                        "  FROM month\n" +
                        " WHERE  id <=( \n" +
                        "           SELECT strftime( '%m', 'now' ) + 1 \n" +
                        "       ) \n" +
                        "       \n" +
                        " ORDER BY ym DESC"));

            }

            else
            {
                spnFPIPMonth.setAdapter(C.getArrayAdapterMultiline("SELECT substr('0' ||id, -2, 2)||'-'||mName||','||strftime('%Y', date('now'))  as ym from month  where id <=(SELECT strftime('%m','now')+1) order by id Desc"));

            }
            //spnFPIPMonth.setAdapter(C.getArrayAdapterMultiline("SELECT '99-- সিলেক্ট মাস-' AS ym Union SELECT substr('0' ||id, -2, 2)||'-'||mName||','||strftime('%Y', date('now'))  as ym from month  where id <=(SELECT strftime('%m','now')+1) order by ym Desc"));
            // spnFPIPMonth.setAdapter(C.getArrayAdapterMultiline("SELECT substr('0' ||id, -2, 2)||'-'||mName||','||strftime('%Y', date('now'))  as ym from month  where id <=(SELECT strftime('%m','now')+1) order by id Desc"));
            spnFPIPMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    buttonStatus();

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            //final Button cmdSave = (Button) findViewById(R.id.cmdSave);

            secReq = (LinearLayout) findViewById(R.id.secReq);
            VlblReqName = (TextView) findViewById(R.id.VlblReqName);

            txtReqName = (TextView) findViewById(R.id.txtReqName);
            txtReqName.setText(C.ReturnSingleValue("Select ProvName from ProviderDB where ProvCode='" + g.getProvCode() + "'"));
            txtFpiWarea = (TextView) findViewById(R.id.txtFpiWarea);
            txtFpiWarea.setText(g.getFWAUnit());
            secReqToCode = (LinearLayout) findViewById(R.id.secReqToCode);
            VlblReqToCode = (TextView) findViewById(R.id.VlblReqToCode);
            DataSearch(Global.Right(spnFPIPMonth.getSelectedItem().toString(), 4) + "-" + Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2), g.getProvCode());

            cmdSync.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(FpiWorkPlaningView.this);

                    String val = Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2);
                    if (val.length() > 0) {


                        Integer WorkPlainvalue = Integer.valueOf(C.ReturnSingleValue("Select count(*) FROM workPlanMaster A\n" +
                                "INNER JOIN workPlanDetail B\n" +
                                " ON A.workPlanId = B.workPlanId and B.providerId=A.providerId\n" +
                                " where B.providerId='" + g.getProvCode() + "' AND\n" +
                                " substr( B.workPlanDate, 1, 7 )='" + Global.Right(spnFPIPMonth.getSelectedItem().toString(), 4) + "-" + Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2) + "' group by A.month "));

                        if ((val.equalsIgnoreCase("01")||val.equalsIgnoreCase("03")||val.equalsIgnoreCase("04")||val.equalsIgnoreCase("05")||val.equalsIgnoreCase("06")||val.equalsIgnoreCase("07") ||val.equalsIgnoreCase("08")||val.equalsIgnoreCase("09")||val.equalsIgnoreCase("10")||val.equalsIgnoreCase("11")||val.equalsIgnoreCase("12"))& WorkPlainvalue < 25
                                ||val.equalsIgnoreCase("02") & WorkPlainvalue < 24

                                )
                        {
                            adb.setTitle("Submit");
                            adb.setMessage("পুরো মাসের কর্মসূচী Entry দেয়া হয়নি । Submit করার জন্য workplan টি  সম্পূর্ন করুন ।");
                            adb.setNegativeButton("OK", null);
                            adb.show();
                        }


                        else if ((val.equalsIgnoreCase("01")||val.equalsIgnoreCase("03")||val.equalsIgnoreCase("04")||val.equalsIgnoreCase("05")||val.equalsIgnoreCase("06")||val.equalsIgnoreCase("07") ||val.equalsIgnoreCase("08")||val.equalsIgnoreCase("09")||val.equalsIgnoreCase("10")||val.equalsIgnoreCase("11")||val.equalsIgnoreCase("12"))& WorkPlainvalue >= 25
                                ||val.equalsIgnoreCase("02") & WorkPlainvalue >= 24

                                ) {
                            adb.setTitle("Submit");
                            adb.setMessage("আপনি কি Work plan টি Submit করতে চান?");
                            // adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান[Yes/No]?");
                            adb.setNegativeButton("না", null);
                            adb.setPositiveButton("হ্যাঁ", new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    //Check for Internet connectivity
                                    //*******************************************************************************
                                    boolean netwoekAvailable = false;
                                    if (Connection.haveNetworkConnection(FpiWorkPlaningView.this)) {
                                        netwoekAvailable = true;

                                        final ProgressDialog progDailog = ProgressDialog.show(
                                                FpiWorkPlaningView.this, "", "অপেক্ষা করুন ...", true);
                                        new Thread() {
                                            public void run() {

                                                Upload();

                                                progDailog.dismiss();
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        //stuff that updates ui
                                                        buttonStatus();

                                                    }
                                                });
                                                //

                                            }
                                        }.start();

                                    }

                                    else {
                                        netwoekAvailable = false;
                                        Connection.MessageBox(FpiWorkPlaningView.this, "Internet connection is not available.");
                                        // return true;
                                    }





                                }
                            });
                            adb.show();


                        }






                    }


                }
            });

        } catch (Exception e) {
            Connection.MessageBox(FpiWorkPlaningView.this, e.getMessage());
            return;
        }
    }

    private  void buttonStatus()
    {

        String val = Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2);
        if (val.length() > 0) {

            DataSearch(Global.Right(spnFPIPMonth.getSelectedItem().toString(), 4) + "-" + Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2), g.getProvCode());


        }

        if (val.equalsIgnoreCase("99")) {

            cmdSync.setVisibility(View.GONE);


        } else if (!C.Existence("Select * FROM workPlanDetail WHERE providerId= '" + g.getProvCode() + "' AND substr( workPlanDate, 1, 7 )='" + Global.Right(spnFPIPMonth.getSelectedItem().toString(), 4) + "-" + Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2) + "'")) {

            cmdSync.setVisibility(View.GONE);

        } else if (C.Existence("Select * FROM workPlanDetail WHERE providerId= '" + g.getProvCode() + "' AND substr( workPlanDate, 1, 7 )='" + Global.Right(spnFPIPMonth.getSelectedItem().toString(), 4) + "-" + Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2) + "'")) {
            DataSearch(Global.Right(spnFPIPMonth.getSelectedItem().toString(), 4) + "-" + Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2), g.getProvCode());
            cmdSync.setVisibility(View.VISIBLE);

        }


    }


    private void Upload() {

        String TableName = "";
        String VariableList = "";


        // message = "Uploading workPlanMaster";
        // jumpTime += 1;
        // progressHandler.sendMessage(progressHandler.obtainMessage());
      /*  TableName = "workPlanMaster";
        VariableList = "workPlanId,workAreaId,providerId,month,status,systemEntryDate,modifyDate,upload";
        C.UploadJSON(TableName, VariableList, "workPlanId, workAreaId, providerId");*/

        TableName = "workPlanMaster";
        VariableList = "workPlanId,workAreaId,providerId,month,status,systemEntryDate,modifyDate,upload";
        C.UploadJSON(TableName, VariableList, "workPlanId, providerId, month");

        // message = "Uploading workPlanDetail";
        // jumpTime += 1;
        // progressHandler.sendMessage(progressHandler.obtainMessage());
        TableName = "workPlanDetail";
        VariableList = "workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,ipcUN,ipcWord,ipcMouza,ipcVill,ipcPara,ipcBariFrom,ipcBariTo,epiproviderId,epischedulerId,ccWard,ccID,leaveType,natProgramType,providerId,systemEntryDate,modifyDate,otherDec,remarks,upload,status";
        C.UploadJSON(TableName, VariableList, "workPlanId, item, workPlanDate, providerId");
    }



    private void DataSearch(String month, String ProvCode) {
        try {

            list = (ListView) findViewById(R.id.lstData);
            list.setAdapter(null);
            Cursor cur = null;
            if (month.length() > 0) {
                cur = C.ReadData("select  A.status as status,B.workPlanDate as workPlanDate,ifnull(B.upload, '' ) AS upload,\n" +
                        " group_concat(( CASE \n" +
                        "WHEN CAST ( C.itemcode AS int ) = 61 and  B.natProgramType=1\n" +
                        "THEN C.itemdes ||':তথ্য'\n" +
                        "WHEN CAST ( C.itemcode AS int ) = 61 and  B.natProgramType=2\n" +
                        "THEN C.itemdes ||':শিক্ষা ও যোগাযোগ বিষয়ক সভা'\n" +
                        "WHEN CAST ( C.itemcode AS int ) = 61 and  B.natProgramType=3\n" +
                        "THEN C.itemdes ||':স্বাস্থ্য শিক্ষা ও পুষ্টি বিষয়ক কার্যক্রম/ বৃক্ষরোপণ কার্যক্রম'\n" +
                        "WHEN CAST ( C.itemcode AS int ) = 61 and  B.natProgramType=4\n" +
                        "THEN C.itemdes ||':বৃক্ষরোপণ কার্যক্রম'\n" +
                        "WHEN CAST ( C.itemcode AS int ) = 61 and  B.natProgramType=5\n" +
                        "THEN C.itemdes ||':উদ্ভুদ্ধকরন কার্যক্রম'\n" +
                        "WHEN CAST ( C.itemcode AS int ) = 61 and  B.natProgramType=6\n" +
                        "THEN C.itemdes ||':সচেতনতা মূলক কার্যক্রম'\n" +
                        "WHEN CAST ( C.itemcode AS int ) = 61 and  B.natProgramType=7\n" +
                        "THEN C.itemdes ||':বিভিন্ন পর্যায় ও সংস্থার প্রতিনিধিদের সাথে সমন্বয় বিষয়ক কার্যক্রম'\n" +
                        "WHEN CAST ( C.itemcode AS int ) = 62 and  B.leaveType=1\n" +
                        "THEN C.itemdes ||':বাৎসরিক'\n" +
                        "WHEN CAST ( C.itemcode AS int ) = 62 and  B.leaveType=2\n" +
                        "THEN \n" +
                        "C.itemdes ||':অসুস্থতা জনিত'\n" +
                        "WHEN CAST ( C.itemcode AS int ) = 62 and  B.leaveType=3\n" +
                        "THEN \n" +
                        "C.itemdes ||':মাতৃত্বকালীন'\n" +
                        "WHEN CAST ( C.itemcode AS int ) = 62 and  B.leaveType=4\n" +
                        "THEN \n" +
                        "C.itemdes ||':পিতৃত্ব কালীন ছুটি'\n" +
                        "WHEN CAST ( C.itemcode AS int ) = 62 and  B.leaveType=5\n" +
                        "THEN \n" +
                        "C.itemdes ||':নৈমিত্তিক ছুটি'\n" +
                        "WHEN CAST ( C.itemcode AS int ) = 62 and  B.leaveType=6\n" +
                        "THEN \n" +
                        "C.itemdes ||':শ্রান্তি ও বিনোদন'\n" +
                        "WHEN CAST ( C.itemcode AS int ) = 62 and  B.leaveType=7\n" +
                        "THEN \n" +
                        "C.itemdes ||':ঐচ্ছিক ছুটি'\n" +
                        "WHEN CAST ( C.itemcode AS int ) = 62 and  B.leaveType=8\n" +
                        "THEN \n" +
                        "C.itemdes ||':সরকারী ছুটি'\n" +
                        "WHEN CAST ( C.itemcode AS int ) = 62 and  B.leaveType=9\n" +
                        "THEN \n" +
                        "C.itemdes ||':অন্যান্য'\n" +
                        "ELSE ifnull( C.itemdes, '' ) \n" +
                        "END \n" +
                        ")) AS itemdes from workPlanMaster A \n" +
                        "INNER JOIN workPlanDetail B ON A.workPlanId = B.workPlanId and B.providerId=A.providerId and A.month=substr( B.workPlanDate, 1, 7 )\n" +
                        "INNER JOIN fpaItem C ON B.item = C.itemcode where  B.providerId= '" + ProvCode + "' And C.type='2' And substr(B.workPlanDate, 1, 7)='" + month + "' group by B.workPlanDate");

            } else {

                cur = C.ReadData("select  A.status as status,B.workPlanDate as workPlanDate,ifnull(B.upload, '' ) AS upload,\n" +
                        " group_concat(( CASE \n" +
                        "WHEN CAST ( C.itemcode AS int ) = 61 and  B.natProgramType=1\n" +
                        "THEN C.itemdes ||':তথ্য'\n" +
                        "WHEN CAST ( C.itemcode AS int ) = 61 and  B.natProgramType=2\n" +
                        "THEN C.itemdes ||':শিক্ষা ও যোগাযোগ বিষয়ক সভা'\n" +
                        "WHEN CAST ( C.itemcode AS int ) = 61 and  B.natProgramType=3\n" +
                        "THEN C.itemdes ||':স্বাস্থ্য শিক্ষা ও পুষ্টি বিষয়ক কার্যক্রম/ বৃক্ষরোপণ কার্যক্রম'\n" +
                        "WHEN CAST ( C.itemcode AS int ) = 61 and  B.fpiOtherMeeting=4\n" +
                        "THEN C.itemdes ||':বৃক্ষরোপণ কার্যক্রম'\n" +
                        "WHEN CAST ( C.itemcode AS int ) = 61 and  B.natProgramType=5\n" +
                        "THEN C.itemdes ||':উদ্ভুদ্ধকরন কার্যক্রম'\n" +
                        "WHEN CAST ( C.itemcode AS int ) = 61 and  B.natProgramType=6\n" +
                        "THEN C.itemdes ||':সচেতনতা মূলক কার্যক্রম'\n" +
                        "WHEN CAST ( C.itemcode AS int ) = 61 and  B.natProgramType=7\n" +
                        "THEN C.itemdes ||':বিভিন্ন পর্যায় ও সংস্থার প্রতিনিধিদের সাথে সমন্বয় বিষয়ক কার্যক্রম'\n" +
                        "WHEN CAST ( C.itemcode AS int ) = 62 and  B.leaveType=1\n" +
                        "THEN C.itemdes ||':বাৎসরিক'\n" +
                        "WHEN CAST ( C.itemcode AS int ) = 62 and  B.leaveType=2\n" +
                        "THEN \n" +
                        "C.itemdes ||':অসুস্থতা জনিত'\n" +
                        "WHEN CAST ( C.itemcode AS int ) = 62 and  B.leaveType=3\n" +
                        "THEN \n" +
                        "C.itemdes ||':মাতৃত্বকালীন'\n" +
                        "WHEN CAST ( C.itemcode AS int ) = 62 and  B.leaveType=4\n" +
                        "THEN \n" +
                        "C.itemdes ||':পিতৃত্ব কালীন '\n" +
                        "WHEN CAST ( C.itemcode AS int ) = 62 and  B.leaveType=5\n" +
                        "THEN \n" +
                        "C.itemdes ||':নৈমিত্তিক '\n" +
                        "WHEN CAST ( C.itemcode AS int ) = 62 and  B.leaveType=6\n" +
                        "THEN \n" +
                        "C.itemdes ||':শ্রান্তি ও বিনোদন'\n" +
                        "WHEN CAST ( C.itemcode AS int ) = 62 and  B.leaveType=7\n" +
                        "THEN \n" +
                        "C.itemdes ||':ঐচ্ছিক'\n" +
                        "WHEN CAST ( C.itemcode AS int ) = 62 and  B.leaveType=8\n" +
                        "THEN \n" +
                        "C.itemdes ||':সরকারী '\n" +
                        "WHEN CAST ( C.itemcode AS int ) = 62 and  B.leaveType=9\n" +
                        "THEN \n" +
                        "C.itemdes ||':অন্যান্য'\n" +
                        "ELSE ifnull( C.itemdes, '' ) \n" +
                        "END \n" +
                        ")) AS itemdes from workPlanMaster A \n" +
                        "INNER JOIN workPlanDetail B ON A.workPlanId = B.workPlanId and B.providerId=A.providerId and A.month=substr( B.workPlanDate, 1, 7 )\n" +
                        "INNER JOIN fpaItem C ON B.item = C.itemcode where B.providerId= '" + ProvCode + "' And C.type='2' And substr(B.workPlanDate, 1, 7)='" + month + "' group by B.workPlanDate");


            }
            cur.moveToFirst();
            dataList.clear();
            while (!cur.isAfterLast()) {
                HashMap<String, String> map = new HashMap<String, String>();
                //w list = (ListView)findViewById(R.id.lstData);

                map.put("workPlanDate", cur.getString(cur.getColumnIndex("workPlanDate")));
                map.put("itemdes", cur.getString(cur.getColumnIndex("itemdes")));

                map.put("Status", cur.getString(cur.getColumnIndex("status")));
                map.put("Upload", cur.getString(cur.getColumnIndex("upload")));
                dataList.add(map);

                dataAdapter = new SimpleAdapter(FpiWorkPlaningView.this, dataList, R.layout.fpiworkplaningrowview, new String[]{"edit"},
                        new int[]{R.id.cmdB1});

                list.setAdapter(new DataListAdapter(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(FpiWorkPlaningView.this, e.getMessage());
            return;
        }
    }


    public class DataListAdapter extends BaseAdapter {
        private Context context;

        public DataListAdapter(Context c) {
            context = c;
        }

        public int getCount() {
            return dataAdapter.getCount();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.fpiworkplaningrowview, null);
            }
            Button cmdB1 = (Button) convertView.findViewById(R.id.cmdB1);
            // Button   cmdB2 = (Button)convertView.findViewById(R.id.cmdB2);

            //

            final TextView workPlanDate = (TextView) convertView.findViewById(R.id.workPlanDate);
            final TextView itemdes = (TextView) convertView.findViewById(R.id.itemdes);
            final TextView status = (TextView) convertView.findViewById(R.id.status);
            // final TextView itemdes = (TextView)convertView.findViewById(R.id.itemdes);

            //cmdB2.setVisibility(View.GONE);


            final HashMap<String, String> o = (HashMap<String, String>) dataAdapter.getItem(position);

            workPlanDate.setText(Global.DateConvertDMY(o.get("workPlanDate")));
            itemdes.setText(o.get("itemdes"));
            if (o.get("Upload").equals("1")) {
                cmdSync.setText("Submitted");
                cmdSync.setBackgroundColor(Color.parseColor("#99cc33"));

            } else if (o.get("Upload").equals("2"))

            {
                cmdSync.setText("Submit");
                cmdSync.setTextColor(Color.BLACK);

                cmdSync.setBackgroundColor(Color.parseColor("#C2E0EC"));


            }


/*
            if(o.get("Status").equals("1"))
            {
                status.setText("অপেক্ষাধিন");
            }

            else  if(o.get("Status").equals("2"))
            {
                status.setText("অননুমোদিত");
            }
            else  if(o.get("Status").equals("3"))
            {
                status.setText("অনুমোদিত");
            }*/


            return convertView;
        }
    }


    protected Dialog onCreateDialog(int id) {
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        Integer Y = g.mYear;
        Integer M = g.mMonth;
        Integer D = g.mDay;

        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mDateSetListener, Y, M - 1, 1);
            case TIME_DIALOG:
                return new TimePickerDialog(this, timePickerListener, hour, minute, false);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear + 1;
            mDay = dayOfMonth;
            EditText dtpDate;

           /* dtpDate = (EditText)findViewById(R.id.dtpAgDT);

            if (VariableID.equals("btnAgDT"))
            {
                dtpDate = (EditText)findViewById(R.id.dtpAgDT);
                dtpDate.setText(new StringBuilder()
                        .append(Global.Right("00" + mMonth,2)).append("/")
                        .append(mYear));
            }

            if (VariableID.equals("btnItemDT"))
            {
                dtpDate = (EditText)findViewById(R.id.dtpItemDT);
                dtpDate.setText(new StringBuilder()
                        .append(Global.Right("00"+mDay,2)).append("/")
                        .append(Global.Right("00"+mMonth,2)).append("/")
                        .append(mYear));
            }
            */


        }
    };
    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
            hour = selectedHour;
            minute = selectedMinute;
            EditText tpTime;

        }
    };
}