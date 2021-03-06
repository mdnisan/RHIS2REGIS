package com.data.rhis2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import Common.Connection;
import Common.Global;

/**
 * Created by Nisan on 7/25/2016.
 */
public class AHIWorkPlaningViewAproved extends Activity {
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
   /* public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mnuclose, menu);
        return true;
    }*/

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mnuview1, menu);
        return true;
    }



    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder adb = new AlertDialog.Builder(AHIWorkPlaningViewAproved.this);
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
            case R.id.menuview:
                Intent intentforview = new Intent(getApplicationContext(), AHIWorkPlaningViewHINotAproved.class);
                startActivity(intentforview);
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
    // EditText txtSlNo;

    /*LinearLayout secUnit;
    LinearLayout secVill;
    LinearLayout secElcono;
    LinearLayout secName;
    LinearLayout secOther;*/


    LinearLayout secFPIPMonth;
    TextView VlblFPIPMonth;
    Spinner spnFPIPMonth;

    TextView lblHS10;
    LinearLayout secReq;
    TextView VlblReqName;
    // TextView txtReqName;
    Spinner spnfpaCode;
    LinearLayout secReqToCode;
    TextView VlblReqToCode;
    TextView txtFpiWarea;

    Button cmdSync;
    Button cmdDownload;
    Button cmdApproved;
    Button cmdNotApproved;
    Button cmdRequest;
    String StartTime;
    String DeviceNo;
    ListView list;
    LinearLayout secRemarks;
    LinearLayout secListRow1;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.ahiworkplaningviewaproved);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();

            list = (ListView) findViewById(R.id.lstData);
            // View header = getLayoutInflater().inflate(R.layout.fpaworkplanheading, null);
            // list.addHeaderView(header);

            TableName = "workPlanMaster";
            TableNameDetail = "workPlanDetail";
            secListRow1 = (LinearLayout) findViewById(R.id.secListRow1);
            secRemarks=(LinearLayout) findViewById(R.id.secRemarks);
            lblHS10 = (TextView) findViewById(R.id.lblHS10);
            secS10 = (LinearLayout) findViewById(R.id.secS10);
            secSlNo = (LinearLayout) findViewById(R.id.secSlNo);
            VlblSlNo = (TextView) findViewById(R.id.VlblSlNo);
            if (g.getProvType().equals("12"))
                lblHS10.setText("সহকারী স্বাস্থ্য পরিদর্শকের  মাসিক অগ্রিম কর্মসূচী অনুমোদন");
            secRemarks.setVisibility(View.GONE);
            secListRow1.setVisibility(View.GONE);
            //  txtRemarks=(EditText) findViewById(R.id.txtRemarks);
            cmdSync = (Button) findViewById(R.id.cmdSync);
            cmdDownload = (Button) findViewById(R.id.cmdDownload);
            cmdApproved = (Button) findViewById(R.id.cmdApproved);
            cmdNotApproved = (Button) findViewById(R.id.cmdNotApproved);
            cmdRequest = (Button) findViewById(R.id.cmdRequest);
          //  cmdRefresh= (Button) findViewById(R.id.cmdRefresh);
            secFPIPMonth = (LinearLayout) findViewById(R.id.secFPIPMonth);
            VlblFPIPMonth = (TextView) findViewById(R.id.VlblFPIPMonth);
            spnFPIPMonth = (Spinner) findViewById(R.id.spnFPIPMonth);
            // spnFPIPMonth.setAdapter(C.getArrayAdapterMultiline("SELECT strftime('%Y', date('now')) ||','||mName||':-'||substr('0' ||id, -2, 2) as ym from month  where id <=(SELECT strftime('%m','now')+1) order by id Desc"));
            //spnFPIPMonth.setAdapter(C.getArrayAdapterMultiline("SELECT '99-- সিলেক্ট মাস-' AS ym Union SELECT substr('0' ||id, -2, 2)||'-'||mName||','||strftime('%Y', date('now'))  as ym from month  where id <=(SELECT strftime('%m','now')+1) order by ym Desc"));
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
            secReq = (LinearLayout) findViewById(R.id.secReq);
            VlblReqName = (TextView) findViewById(R.id.VlblReqName);
            spnfpaCode = (Spinner) findViewById(R.id.spnfpaCode);

            spnfpaCode.setAdapter(C.getArrayAdapterMultiline("Select substr('0' ||ProvCode, -6, 6)||'-'||ProvName from ProviderDB where ProvType ='11'"));
            txtFpiWarea = (TextView) findViewById(R.id.txtFpiWarea);

            spnFPIPMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    buttonStatus();


                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            spnfpaCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    String val = Global.Left(spnfpaCode.getSelectedItem().toString(), 6);
                    if (val.length() >= 0) {
                        String union = C.ReturnSingleValue("Select unionid from ProviderDB where ProvCode ='" + val + "'");
                        txtFpiWarea.setText(C.ReturnSingleValue("select cast(unionid as varchar(2))||','||unionname from Unions where zillaid='" + g.getDistrict() + "' and upazilaid='" + g.getUpazila() + "' and unionid='" + union + "'"));
                        txtFpiWarea.setEnabled(false);
                        buttonStatus();

                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            // txtFpiWarea.setText(ProvArea("3",Global.Left(spnfpaCode.getSelectedItem().toString(),5)));
            secReqToCode = (LinearLayout) findViewById(R.id.secReqToCode);
            VlblReqToCode = (TextView) findViewById(R.id.VlblReqToCode);

            DataSearch(Global.Right(spnFPIPMonth.getSelectedItem().toString(), 4) + "-" + Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2), Global.Left(spnfpaCode.getSelectedItem().toString(), 6));


            cmdSync.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    //Check for Internet connectivity
                    //*******************************************************************************
                    boolean netwoekAvailable = false;
                    if (Connection.haveNetworkConnection(AHIWorkPlaningViewAproved.this)) {
                        netwoekAvailable = true;
                        //  UploadFWA();
                        final ProgressDialog progDailog = ProgressDialog.show(
                                AHIWorkPlaningViewAproved.this, "", "অপেক্ষা করুন ...", true);
                        new Thread() {
                            public void run() {

                                Upload();

                                progDailog.dismiss();

                            }
                        }.start();

                    } else {
                        netwoekAvailable = false;
                        Connection.MessageBox(AHIWorkPlaningViewAproved.this, "Internet connection is not available.");
                        // return true;
                    }



                   /* Connection.MessageBox(Adolescent.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
                    finish();

                    Intent f2 = new Intent(getApplicationContext(),MemberList.class);
                    startActivity(f2);*/
                }
            });


            cmdDownload.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {


                    //Check for Internet connectivity
                    //*******************************************************************************
                    boolean netwoekAvailable = false;
                    if (Connection.haveNetworkConnection(AHIWorkPlaningViewAproved.this)) {
                        netwoekAvailable = true;

                        //DownloadFPItoFWA() ;
                        final ProgressDialog progDailog = ProgressDialog.show(
                                AHIWorkPlaningViewAproved.this, "", "অপেক্ষা করুন ...", true);
                        new Thread() {
                            public void run() {

                                DownloadFWAWWORKPlAINTables(Global.Left(spnfpaCode.getSelectedItem().toString(), 6));
                                progDailog.dismiss();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //stuff that updates ui
                                        DownloadStatus();

                                    }
                                });

                            }
                        }.start();


                        // DataSearch(Global.Right(spnFPIPMonth.getSelectedItem().toString(), 4)+"-"+Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2),Global.Left(spnfpaCode.getSelectedItem().toString(),5));



                    }
                    else {
                        DownloadStatus();
                        netwoekAvailable = false;
                        Connection.MessageBox(AHIWorkPlaningViewAproved.this, "Internet connection is not available.");
                        // return true;
                    }

                }
            });

            cmdApproved.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(AHIWorkPlaningViewAproved.this);

                    adb.setTitle("অনুমোদন");
                    adb.setMessage("আপনি কি Work plan টি অনুমোদন করতে চান?");
                    // adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান[Yes/No]?");
                    adb.setNegativeButton("না", null);
                    adb.setPositiveButton("হ্যাঁ", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String SQL = "";
                            if (C.Existence("Select status from " + TableName + "  Where  month = '" + Global.Right(spnFPIPMonth.getSelectedItem().toString(), 4) + "-" + Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2) + "' AND  providerId  = '" + Global.Left(spnfpaCode.getSelectedItem().toString(), 6) + "'")) {


                                SQL = "Update workPlanMaster set upload='2',";
                                SQL += "modifyDate = '" + Global.DateNowYMD() + "',";
                                // SQL+="remarks = '"+ txtRemarks.getText()+"',";
                                SQL += "status ='2'";
                                SQL += "  Where month='" + Global.Right(spnFPIPMonth.getSelectedItem().toString(), 4) + "-" + Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2) + "' AND  providerId  = '" + Global.Left(spnfpaCode.getSelectedItem().toString(),6) + "'";
                                C.Save(SQL);
                                Connection.MessageBox(AHIWorkPlaningViewAproved.this, "অনুমোদিত সম্পন্ন হয়েছে।");
                                // cmdApproved.setText("অনুমোদিত");
                                DataSearch(Global.Right(spnFPIPMonth.getSelectedItem().toString(), 4) + "-" + Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2), Global.Left(spnfpaCode.getSelectedItem().toString(), 6));

                                boolean netwoekAvailable = false;
                                if (Connection.haveNetworkConnection(AHIWorkPlaningViewAproved.this)) {
                                    netwoekAvailable = true;

                                    //DownloadFPItoFWA() ;
                                    final ProgressDialog progDailog = ProgressDialog.show(
                                            AHIWorkPlaningViewAproved.this, "", "অপেক্ষা করুন ...", true);
                                    new Thread() {
                                        public void run() {
                                            Upload();

                                            progDailog.dismiss();

                                        }
                                    }.start();
                                }

                                else {
                                    netwoekAvailable = false;
                                    Connection.MessageBox(AHIWorkPlaningViewAproved.this, "Internet connection is not available.");
                                    // return true;
                                }

                            } else {
                                Connection.MessageBox(AHIWorkPlaningViewAproved.this, "তথ্য পাওয়া যায় না.");
                            }
                        }
                    });
                    adb.show();


                }

            });

            cmdNotApproved.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(AHIWorkPlaningViewAproved.this);

                    adb.setTitle("অননুমোদন");
                    adb.setMessage("আপনি কি Work plan টি অননুমোদন করতে চান?");
                    // adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান[Yes/No]?");
                    adb.setNegativeButton("না", null);
                    adb.setPositiveButton("হ্যাঁ", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String SQL = "";
                            String SQ = "Select status from " + TableName + "  Where  month = '" + Global.Right(spnFPIPMonth.getSelectedItem().toString(), 4) + "-" + Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2) + "' AND  providerId  = '" + Global.Left(spnfpaCode.getSelectedItem().toString(), 6) + "'";
                            if (C.Existence(SQ)) {


                                SQL = "Update workPlanMaster set upload='2',";
                                SQL += "modifyDate = '" + Global.DateNowYMD() + "',";
                                // SQL+="remarks = '"+ txtRemarks.getText()+"',";//Global.DateTimeNowYMDHMS()
                                SQL += "status ='3'";
                                SQL += "  Where month='" + Global.Right(spnFPIPMonth.getSelectedItem().toString(), 4) + "-" + Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2) + "' AND  providerId  = '" + Global.Left(spnfpaCode.getSelectedItem().toString(), 6) + "'";
                                C.Save(SQL);
                                Connection.MessageBox(AHIWorkPlaningViewAproved.this, "অননুমোদিত সম্পন্ন হয়েছে।");
                                // cmdNotApproved.setText("অননুমোদন");
                                DataSearch(Global.Right(spnFPIPMonth.getSelectedItem().toString(), 4) + "-" + Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2), Global.Left(spnfpaCode.getSelectedItem().toString(), 6));


                                boolean netwoekAvailable = false;
                                if (Connection.haveNetworkConnection(AHIWorkPlaningViewAproved.this)) {
                                    netwoekAvailable = true;

                                    //DownloadFPItoFWA() ;
                                    final ProgressDialog progDailog = ProgressDialog.show(
                                            AHIWorkPlaningViewAproved.this, "", "অপেক্ষা করুন ...", true);
                                    new Thread() {
                                        public void run() {
                                            Upload();
                                            // DownloadFWAWWORKPlAINTables(Global.Left(spnfpaCode.getSelectedItem().toString(), 5));
                                            progDailog.dismiss();

                                        }
                                    }.start();
                                }

                                else {
                                    netwoekAvailable = false;
                                    Connection.MessageBox(AHIWorkPlaningViewAproved.this, "Internet connection is not available.");
                                    // return true;
                                }

                            } else {
                                Connection.MessageBox(AHIWorkPlaningViewAproved.this, "তথ্য পাওয়া যায় না");
                            }
                        }
                    });
                    adb.show();

                }

            });

            /*cmdRefresh.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    //  Submit();

                    DownloadStatus();
                }
            });*/


        } catch (Exception e) {
            Connection.MessageBox(AHIWorkPlaningViewAproved.this, e.getMessage());
            return;
        }
    }


   public void DownloadFWAWWORKPlAINTables(String provCode) {

       //workPlanMaster
       String VariableList = "";
       String sql = "select  \"workPlanId\", \"workAreaId\", \"providerId\", \"month\", \"status\", \"systemEntryDate\", \"modifyDate\", 1 as upload\n" +
               "from \"workPlanMaster\" \n" +
               "where \"providerId\" =" + provCode + "";


       VariableList = "workPlanId,workAreaId,providerId,month,status,systemEntryDate,modifyDate,upload";
       C.DownloadJSON(sql, "workPlanMaster", VariableList, "workPlanId, providerId, month");

       //workPlanDetail
       sql = "select  \"workPlanId\", \"item\", \"workPlanDate\", \"unitNo\", \"village\", \"elcoFrom\", \"elcoTo\", \"ipcUN\",\"ipcWord\",\"ipcMouza\",\"ipcVill\",\"ipcPara\", \"ipcBariFrom\",\"ipcBariTo\",\"epiproviderId\",\"epischedulerId\",\"ccWard\",\"ccID\",\"natProgramType\",\"fpiOtherMeeting\",\"leaveType\", \"providerId\", \"systemEntryDate\", \"modifyDate\", \"otherDec\", \"remarks\", 1 as upload,status\n" +
               "from \"workPlanDetail\" \n" +
               "where status<>'2' and status<>'3' and \"providerId\" =" + provCode + "";


       VariableList = "workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,ipcUN,ipcWord,ipcMouza,ipcVill,ipcPara,ipcBariFrom,ipcBariTo,epiproviderId,epischedulerId,ccWard,ccID,natProgramType,fpiOtherMeeting,leaveType,providerId,systemEntryDate,modifyDate,otherDec,remarks,upload,status";
       C.DownloadJSON(sql, "workPlanDetail", VariableList, "workPlanId, item, workPlanDate, providerId");
   }

    private void Upload() {

        String TableName = "";
        String VariableList = "";
        TableName = "workPlanMaster";
        VariableList = "workPlanId,workAreaId,providerId,month,status,systemEntryDate,modifyDate,upload";
        C.UploadJSON(TableName, VariableList, "workPlanId, providerId, month");


    }


    private void DownloadStatus()
    {
        if (!C.Existence("Select * FROM workPlanDetail WHERE providerId= '" + Global.Left(spnfpaCode.getSelectedItem().toString(), 6) + "' AND substr( workPlanDate, 1, 7 )='" + Global.Right(spnFPIPMonth.getSelectedItem().toString(), 4) + "-" + Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2) + "'")) {
            secListRow1.setVisibility(View.GONE);
            list.setVisibility(View.GONE);
            cmdDownload.setText("");
            cmdDownload.setText("সহকারী স্বাস্থ্য পরিদর্শকের  কর্মসূচীটি Download  করতে  এখানে Click করুন ।");
            cmdDownload.setBackgroundColor(Color.parseColor("#C2E0EC"));
           // cmdApproved.setVisibility(View.GONE);
           // cmdNotApproved.setVisibility(View.GONE);
            cmdApproved.setBackgroundColor(Color.parseColor("#C2E0EC"));
            cmdNotApproved.setBackgroundColor(Color.parseColor("#C2E0EC"));
        } else if (C.Existence("Select * FROM workPlanDetail WHERE providerId= '" + Global.Left(spnfpaCode.getSelectedItem().toString(), 6) + "' AND substr( workPlanDate, 1, 7 )='" + Global.Right(spnFPIPMonth.getSelectedItem().toString(), 4) + "-" + Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2) + "'")) {
            secListRow1.setVisibility(View.VISIBLE);
            list.setVisibility(View.VISIBLE);
            cmdDownload.setText("");
            cmdDownload.setText("সহকারী স্বাস্থ্য পরিদর্শকের  কর্মসূচীটি Download  করতে  এখানে Click করুন ।");
            cmdDownload.setBackgroundColor(Color.parseColor("#99cc33"));
            cmdApproved.setVisibility(View.VISIBLE);
            cmdNotApproved.setVisibility(View.VISIBLE);
        }
        DataSearch(Global.Right(spnFPIPMonth.getSelectedItem().toString(), 4) + "-" + Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2), Global.Left(spnfpaCode.getSelectedItem().toString(),6));


    }

    private void buttonStatus()
    {
        String val = Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2);
        if (val.length() > 0) {

            //  DataSearch(Global.Right(spnFPIPMonth.getSelectedItem().toString(), 4)+"-"+Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2),Global.Left(spnfpaCode.getSelectedItem().toString(),5));
            if (val.equals("99")) {
                cmdDownload.setBackgroundColor(Color.parseColor("#C2E0EC"));

                list.setVisibility(View.GONE);

                cmdDownload.setVisibility(View.GONE);
                cmdApproved.setVisibility(View.GONE);
                cmdNotApproved.setVisibility(View.GONE);
                secListRow1.setVisibility(View.GONE);
                secRemarks.setVisibility(View.GONE);

                cmdNotApproved.setTextColor(Color.BLACK);
                cmdNotApproved.setText("অননুমোদিত");
                cmdApproved.setTextColor(Color.BLACK);
                cmdApproved.setText("অনুমোদিত");

            } else if (!C.Existence("Select * FROM workPlanDetail WHERE providerId= '" + Global.Left(spnfpaCode.getSelectedItem().toString(), 6) + "' AND substr( workPlanDate, 1, 7 )='" + Global.Right(spnFPIPMonth.getSelectedItem().toString(), 4) + "-" + Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2) + "'")) {
                cmdDownload.setBackgroundColor(Color.parseColor("#C2E0EC"));

                list.setVisibility(View.GONE);
                cmdDownload.setVisibility(View.VISIBLE);
               // cmdRefresh.setVisibility(View.VISIBLE);
                cmdApproved.setVisibility(View.GONE);
                cmdNotApproved.setVisibility(View.GONE);
                secListRow1.setVisibility(View.GONE);
                secRemarks.setVisibility(View.GONE);


                cmdNotApproved.setTextColor(Color.BLACK);
                cmdNotApproved.setText("অননুমোদিত");
                cmdApproved.setTextColor(Color.BLACK);
                cmdApproved.setText("অনুমোদিত");

            } else if (C.Existence("Select * FROM workPlanDetail WHERE providerId= '" + Global.Left(spnfpaCode.getSelectedItem().toString(), 6) + "' AND substr( workPlanDate, 1, 7 )='" + Global.Right(spnFPIPMonth.getSelectedItem().toString(), 4) + "-" + Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2) + "'")) {
                cmdDownload.setBackgroundColor(Color.parseColor("#C2E0EC"));
                // cmdRefresh.setBackgroundColor(Color.parseColor("#C2E0EC"));
                list.setVisibility(View.GONE);
                cmdDownload.setVisibility(View.VISIBLE);
                //cmdRefresh.setVisibility(View.VISIBLE);
                cmdApproved.setVisibility(View.GONE);
                cmdNotApproved.setVisibility(View.GONE);
                secListRow1.setVisibility(View.GONE);
                //secListRow1.setVisibility(View.VISIBLE);
                secRemarks.setVisibility(View.GONE);
                //  cmdSync.setEnabled(true);

            }
        }
    }


    private void DisplayNotApprovedItem() {
        final Dialog popupView = new Dialog(AHIWorkPlaningViewAproved.this);
        popupView.setTitle("Not Approved Item");

        popupView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popupView.setContentView(R.layout.ahipopup);
        popupView.setCancelable(true);
        popupView.setCanceledOnTouchOutside(true);

        final TextView txtName = (TextView) popupView.findViewById(R.id.txtName);
        final TextView txtWorkpDT = (TextView) popupView.findViewById(R.id.txtWorkpDT);
        final TextView txtDes = (TextView) popupView.findViewById(R.id.txtDes);
        txtName.setText(g.getWPProvCode());
        txtWorkpDT.setText(g.getWPDate());
        txtDes.setText(g.getWPDes());
        // int id;
        popupView.show();


        Button cmdSave = (Button) popupView.findViewById(R.id.cmdSave);
        cmdSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                TableName = "workPlanDetail";
                String SQL = "";
                //String DV = "";
                //DV = Global.DateValidate(VisitDate.getText().toString());
                // DV1 = Global.DateValidate(dtpdob.getText().toString());

                SQL = "Update " + TableName + " Set ";

                SQL += "status ='2',";
                SQL += "modifyDate = '" + Global.DateTimeNowYMDHMS() + "',";
                SQL += "upload ='2'";
                SQL += "  Where workPlanDate='" + Global.DateConvertYMD(txtWorkpDT.getText().toString()) + "'and providerId='" + Global.Left(txtName.getText().toString(), 6) + "'";
                // SQL+=" where Cluster='"+ cluster +"' and Block='"+ block +"' and HH='"+ hh +"' and StudyID='"+ txtStudyId.getText() +"'and Visit='"+ txtVisitNo.getText()+"'";
                C.Save(SQL);
                Connection.MessageBox(AHIWorkPlaningViewAproved.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
                popupView.cancel();
                DataSearch(Global.Right(spnFPIPMonth.getSelectedItem().toString(), 4) + "-" + Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2), Global.Left(spnfpaCode.getSelectedItem().toString(), 6));

                //finish();
                // Intent f1 = new Intent(getApplicationContext(),FpaWorkPlaningView.class);
                // startActivity(f1);

            }
        });


        Button cmdClose = (Button) popupView.findViewById(R.id.cmdClose);
        cmdClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                popupView.cancel();
            }
        });
    }
    private void DisplayFPALeave() {
        final Dialog popupView = new Dialog(AHIWorkPlaningViewAproved.this);
        popupView.setTitle("ছুটি");

        popupView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popupView.setContentView(R.layout.fpiremarkspopup);
        popupView.setCancelable(true);
        popupView.setCanceledOnTouchOutside(true);


        popupView.show();

        Button cmdSave = (Button) popupView.findViewById(R.id.cmdSave);
        cmdSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String SQL = "";

            }
        });

        //   }
        // else
        //  {

        // }
        Button cmdClose = (Button) popupView.findViewById(R.id.cmdClose);
        cmdClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                popupView.cancel();
            }
        });
    }

    private String GetStatus(String month) {
        String SQL = "";

        SQL = "Select status from workPlanMaster";
        SQL += " where month='" + month + "'";
        String mo = C.ReturnSingleValue(SQL);
        // Serial No auto increment
        return mo;
    }

    private void DataSearch(String month, String ProvCode) {
        try {

            list = (ListView) findViewById(R.id.lstData);
            list.setAdapter(null);
            Cursor cur = null;
            if (month.length() > 0) {
                cur = C.ReadData("SELECT A.status AS status,ifnull(B.status, '' ) AS dstatus,ifnull(A.modifyDate, '' ) AS modifyDate,ifnull(B.upload, '' ) AS upload,\n" +
                        " B.workPlanDate AS workPlanDate,\n" +
                        "group_concat(\n" +
                        "( CASE\n" +
                        "WHEN CAST ( C.itemcode AS int ) = 1 THEN C.itemdes || '-' || 'ইউনিয়ন:' ||( \n" +
                        "SELECT UNIONNAME\n" +
                        "FROM Unions\n" +
                        "WHERE UNIONID IN ( \n" +
                        "SELECT ipcUN\n" +
                        "FROM workPlanDetail where providerId= B.providerId and ipcUN= B.ipcUN and ipcWord= B.ipcWord and ipcMouza= B.ipcMouza and ipcVill= B.ipcVill and workPlanDate=B.workPlanDate\n" +
                        " ) \n" +
                        " \n" +
                        ")|| ',ওয়ার্ড:' ||B.ipcWord || ',গ্রাম:' ||( \n" +
                        "SELECT VILLAGENAME\n" +
                        "FROM Village\n" +
                        "WHERE MOUZAID IN ( \n" +
                        "SELECT ipcMouza\n" +
                        "FROM workPlanDetail where providerId= B.providerId and ipcUN= B.ipcUN and ipcWord= B.ipcWord and ipcMouza= B.ipcMouza and ipcVill= B.ipcVill and workPlanDate=B.workPlanDate\n" +
                        " ) \n" +
                        " \n" +
                        ") || ',পাড়া:' ||B.ipcPara \n" +
                        " || ' ,বাড়ি পরিদর্শন সংখ্যা :(' || B.ipcBariFrom || '-' || B.ipcBariTo  || ')'\n" +
                        " \n" +
                        " WHEN CAST ( C.itemcode AS int ) = 2 THEN C.itemdes || '-' || 'স্বাস্থ্য সহকারীর নাম:' ||( \n" +
                        "SELECT ProvCode||'-'||ProvName \n" +
                        "FROM ProviderDB\n" +
                        "WHERE ProvCode IN ( \n" +
                        "SELECT epiproviderId\n" +
                        "FROM workPlanDetail where epiproviderId= B.epiproviderId and  workPlanDate=B.workPlanDate\n" +
                        " ) \n" +
                        " \n" +
                        ") ||',ই পি আই সাব-ব্লক:' ||substr( B.epischedulerId, 1, 1 ) || '-' ||(Select BNameBan from HABlock where BCode='0'||cast(substr(B.epischedulerId , 1, 1 ) as varchar(2)))\n" +
                        "|| ',সেশন তারিখ:' ||( \n" +
                        "SELECT strftime('%d/%m/%Y', date(scheduleDate))\n" +
                        "FROM epiSchedulerUpdate\n" +
                        "WHERE schedulerId=substr(B.epischedulerId, 7, 2 ) and wordOld=substr(B.epischedulerId, 2, 1 ) and subBlockId=cast(substr(B.epischedulerId , 1, 1 ) as varchar(2)))\n" +
                        "\n" +
                        " ||',কেন্দ্রের নাম:' ||B.epischedulerId||'-'||( \n" +
                        "SELECT centerName\n" +
                        "FROM epiSchedulerUpdate\n" +
                        "WHERE  schedulerId=substr(B.epischedulerId, 7, 2 ) and wordOld=substr(B.epischedulerId, 2, 1 ) and subBlockId=cast(substr(B.epischedulerId , 1, 1 ) as varchar(2)))\n" +
                        " \n" +
                        "\n" +
                        " WHEN  CAST ( C.itemcode AS int ) = 11 \n" +
                        " THEN C.itemdes || B.otherDec \n" +
                        " WHEN  CAST ( C.itemcode AS int ) = 10 \n" +
                        "AND\n" +
                        "B.natProgramType = 1 THEN C.itemdes || ':জাতীয় টিকা দিবস' \n" +
                        " WHEN  CAST ( C.itemcode AS int ) = 10 \n" +
                        "AND\n" +
                        "B.natProgramType = 2 THEN C.itemdes || ':জাতীয় ক্রিমি সপ্তাহ' \n" +
                        " WHEN  CAST ( C.itemcode AS int ) = 10 \n" +
                        "AND\n" +
                        "B.natProgramType = 3 THEN C.itemdes || ':জাতীয় ভিটামিন এ প্লাস ক্যাম্পেইন' \n" +
                        "\n" +
                        "WHEN  CAST ( C.itemcode AS int ) = 6 \n" +
                        "AND\n" +
                        "B.leaveType = 1 THEN C.itemdes || ':বাৎসরিক' \n" +
                        "WHEN CAST ( C.itemcode AS int ) = 6\n" +
                        "AND\n" +
                        "B.leaveType = 2 THEN C.itemdes || ':অসুস্থতা জনিত' \n" +
                        "WHEN CAST ( C.itemcode AS int ) = 6\n" +
                        "AND\n" +
                        "B.leaveType = 3 THEN C.itemdes || ':মাতৃত্বকালীন' \n" +
                        "WHEN CAST ( C.itemcode AS int ) = 6\n" +
                        "AND\n" +
                        "B.leaveType = 4 THEN C.itemdes || ':পিতৃত্ব কালীন ' \n" +
                        "WHEN CAST ( C.itemcode AS int ) = 6\n" +
                        "AND\n" +
                        "B.leaveType = 5 THEN C.itemdes || ':নৈমিত্তিক' \n" +
                        "WHEN CAST ( C.itemcode AS int ) = 6\n" +
                        "AND\n" +
                        "B.leaveType = 6 THEN C.itemdes || ':শ্রান্তি ও বিনোদন' \n" +
                        "WHEN CAST ( C.itemcode AS int ) = 6\n" +
                        "AND\n" +
                        "B.leaveType = 7 THEN C.itemdes || ':ঐচ্ছিক ' \n" +
                        "WHEN CAST ( C.itemcode AS int ) = 6\n" +
                        "AND\n" +
                        "B.leaveType = 8 THEN C.itemdes || ':সরকারী ' \n" +
                        "WHEN CAST ( C.itemcode AS int ) = 6\n" +
                        "AND\n" +
                        "B.leaveType = 9 THEN C.itemdes || ':অন্যান্য ' \n" +
                        "ELSE ifnull( C.itemdes, '' ) \n" +
                        "END )) AS itemdes\n" +
                        "FROM workPlanMaster A\n" +
                        "INNER JOIN workPlanDetail B\n" +
                        "ON A.workPlanId = B.workPlanId and A.providerId=B.providerId and A.month=substr( B.workPlanDate, 1, 7 )\n" +
                        "INNER JOIN fpaItem C\n" +
                        "ON B.item = C.itemcode\n" +
                        " WHERE B.providerId= '" + ProvCode + "' And C.type = '4' And B.status<> '2'\n" +
                        "AND\n" +
                        "substr( B.workPlanDate, 1, 7 )='" + month + "' group by B.workPlanDate,B.status");
        } else {
                cur = C.ReadData("SELECT A.status AS status,ifnull(B.status, '' ) AS dstatus,ifnull(A.modifyDate, '' ) AS modifyDate,ifnull(B.upload, '' ) AS upload,\n" +
                        " B.workPlanDate AS workPlanDate,\n" +
                        "group_concat(\n" +
                        "( CASE\n" +
                        "WHEN CAST ( C.itemcode AS int ) = 1 THEN C.itemdes || '-' || 'ইউনিয়ন:' ||( \n" +
                        "SELECT UNIONNAME\n" +
                        "FROM Unions\n" +
                        "WHERE UNIONID IN ( \n" +
                        "SELECT ipcUN\n" +
                        "FROM workPlanDetail where providerId= B.providerId and ipcUN= B.ipcUN and ipcWord= B.ipcWord and ipcMouza= B.ipcMouza and ipcVill= B.ipcVill and workPlanDate=B.workPlanDate\n" +
                        " ) \n" +
                        " \n" +
                        ")|| ',ওয়ার্ড:' ||B.ipcWord || ',গ্রাম:' ||( \n" +
                        "SELECT VILLAGENAME\n" +
                        "FROM Village\n" +
                        "WHERE MOUZAID IN ( \n" +
                        "SELECT ipcMouza\n" +
                        "FROM workPlanDetail where providerId= B.providerId and ipcUN= B.ipcUN and ipcWord= B.ipcWord and ipcMouza= B.ipcMouza and ipcVill= B.ipcVill and workPlanDate=B.workPlanDate\n" +
                        " ) \n" +
                        " \n" +
                        ") || ',পাড়া:' ||B.ipcPara \n" +
                        " || ' ,বাড়ি পরিদর্শন সংখ্যা :(' || B.ipcBariFrom || '-' || B.ipcBariTo  || ')'\n" +
                        " \n" +
                        " WHEN CAST ( C.itemcode AS int ) = 2 THEN C.itemdes || '-' || 'স্বাস্থ্য সহকারীর নাম:' ||( \n" +
                        "SELECT ProvCode||'-'||ProvName \n" +
                        "FROM ProviderDB\n" +
                        "WHERE ProvCode IN ( \n" +
                        "SELECT epiproviderId\n" +
                        "FROM workPlanDetail where epiproviderId= B.epiproviderId and  workPlanDate=B.workPlanDate\n" +
                        " ) \n" +
                        " \n" +
                        ") ||',ই পি আই সাব-ব্লক:' ||substr( B.epischedulerId, 1, 1 ) || '-' ||(Select BNameBan from HABlock where BCode='0'||cast(substr(B.epischedulerId , 1, 1 ) as varchar(2)))\n" +
                        "|| ',সেশন তারিখ:' ||( \n" +
                        "SELECT strftime('%d/%m/%Y', date(scheduleDate))\n" +
                        "FROM epiSchedulerUpdate\n" +
                        "WHERE schedulerId=substr(B.epischedulerId, 7, 2 ) and wordOld=substr(B.epischedulerId, 2, 1 ) and subBlockId=cast(substr(B.epischedulerId , 1, 1 ) as varchar(2)))\n" +
                        "\n" +
                        " ||',কেন্দ্রের নাম:' ||B.epischedulerId||'-'||( \n" +
                        "SELECT centerName\n" +
                        "FROM epiSchedulerUpdate\n" +
                        "WHERE  schedulerId=substr(B.epischedulerId, 7, 2 ) and wordOld=substr(B.epischedulerId, 2, 1 ) and subBlockId=cast(substr(B.epischedulerId , 1, 1 ) as varchar(2)))\n" +
                        " \n" +
                        "\n" +
                        " WHEN  CAST ( C.itemcode AS int ) = 11 \n" +
                        " THEN C.itemdes || ':'||B.otherDec \n" +
                        " WHEN  CAST ( C.itemcode AS int ) = 10 \n" +
                        "AND\n" +
                        "B.natProgramType = 1 THEN C.itemdes || ':জাতীয় টিকা দিবস' \n" +
                        " WHEN  CAST ( C.itemcode AS int ) = 10 \n" +
                        "AND\n" +
                        "B.natProgramType = 2 THEN C.itemdes || ':জাতীয় ক্রিমি সপ্তাহ' \n" +
                        " WHEN  CAST ( C.itemcode AS int ) = 10 \n" +
                        "AND\n" +
                        "B.natProgramType = 3 THEN C.itemdes || ':জাতীয় ভিটামিন এ প্লাস ক্যাম্পেইন' \n" +
                        "\n" +
                        "WHEN  CAST ( C.itemcode AS int ) = 6 \n" +
                        "AND\n" +
                        "B.leaveType = 1 THEN C.itemdes || ':বাৎসরিক' \n" +
                        "WHEN CAST ( C.itemcode AS int ) = 6\n" +
                        "AND\n" +
                        "B.leaveType = 2 THEN C.itemdes || ':অসুস্থতা জনিত' \n" +
                        "WHEN CAST ( C.itemcode AS int ) = 6\n" +
                        "AND\n" +
                        "B.leaveType = 3 THEN C.itemdes || ':মাতৃত্বকালীন' \n" +
                        "WHEN CAST ( C.itemcode AS int ) = 6\n" +
                        "AND\n" +
                        "B.leaveType = 4 THEN C.itemdes || ':পিতৃত্ব কালীন ' \n" +
                        "WHEN CAST ( C.itemcode AS int ) = 6\n" +
                        "AND\n" +
                        "B.leaveType = 5 THEN C.itemdes || ':নৈমিত্তিক' \n" +
                        "WHEN CAST ( C.itemcode AS int ) = 6\n" +
                        "AND\n" +
                        "B.leaveType = 6 THEN C.itemdes || ':শ্রান্তি ও বিনোদন' \n" +
                        "WHEN CAST ( C.itemcode AS int ) = 6\n" +
                        "AND\n" +
                        "B.leaveType = 7 THEN C.itemdes || ':ঐচ্ছিক ' \n" +
                        "WHEN CAST ( C.itemcode AS int ) = 6\n" +
                        "AND\n" +
                        "B.leaveType = 8 THEN C.itemdes || ':সরকারী ' \n" +
                        "WHEN CAST ( C.itemcode AS int ) = 6\n" +
                        "AND\n" +
                        "B.leaveType = 9 THEN C.itemdes || ':অন্যান্য ' \n" +
                        "ELSE ifnull( C.itemdes, '' ) \n" +
                        "END )) AS itemdes\n" +
                        "FROM workPlanMaster A\n" +
                        "INNER JOIN workPlanDetail B\n" +
                        "ON A.workPlanId = B.workPlanId and A.providerId=B.providerId and A.month=substr( B.workPlanDate, 1, 7 )\n" +
                        "INNER JOIN fpaItem C\n" +
                        "ON B.item = C.itemcode\n" +
                        " WHERE B.providerId= '" + ProvCode + "' And C.type = '4' And B.status<> '2'\n" +
                        "AND\n" +
                        "substr( B.workPlanDate, 1, 7 )='" + month + "' group by B.workPlanDate,B.status");


            }

            cur.moveToFirst();
            dataList.clear();
            while (!cur.isAfterLast()) {
                HashMap<String, String> map = new HashMap<String, String>();
                //w list = (ListView)findViewById(R.id.lstData);

                map.put("workPlanDate", cur.getString(cur.getColumnIndex("workPlanDate")));
                map.put("itemdes", cur.getString(cur.getColumnIndex("itemdes")));
                map.put("Status", cur.getString(cur.getColumnIndex("status")));
                map.put("Dstatus", cur.getString(cur.getColumnIndex("dstatus")));
                map.put("ModifyDate", cur.getString(cur.getColumnIndex("modifyDate")));
                map.put("Upload", cur.getString(cur.getColumnIndex("upload")));

                dataList.add(map);

                dataAdapter = new SimpleAdapter(AHIWorkPlaningViewAproved.this, dataList, R.layout.fpaworkplanrowview, new String[]{"edit"},
                        new int[]{R.id.cmdB1});

                list.setAdapter(new DataListAdapter(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(AHIWorkPlaningViewAproved.this, e.getMessage());
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
                convertView = inflater.inflate(R.layout.fpaworkplanrowview, null);
            }
            Button cmdB1 = (Button) convertView.findViewById(R.id.cmdB1);

            final TableLayout memtab = (TableLayout) convertView.findViewById(R.id.memtab);
            final TextView workPlanDate = (TextView) convertView.findViewById(R.id.workPlanDate);
            final TextView itemdes = (TextView) convertView.findViewById(R.id.itemdes);
            final TextView status = (TextView) convertView.findViewById(R.id.status);

            final HashMap<String, String> o = (HashMap<String, String>) dataAdapter.getItem(position);

            workPlanDate.setText(Global.DateConvertDMY(o.get("workPlanDate")));
            itemdes.setText(o.get("itemdes"));


            if (o.get("Upload").equals("1")) {
                cmdSync.setBackgroundColor(Color.parseColor("#99cc33"));
            } else if (o.get("Upload").equals("2")) {
                //  cmdSync.setBackgroundColor(Color.parseColor(""));
            }

            if (o.get("Status").equals("1")) {
                //status.setText("অপেক্ষাধিন");

                cmdRequest.setTextColor(Color.BLACK);
                cmdRequest.setText("অপেক্ষাধিন");
                cmdNotApproved.setText("অননুমোদিত");
                cmdApproved.setText("অনুমোদিত");
                // cmdRequest.setBackgroundColor(Color.parseColor("#99cc33"));
                cmdNotApproved.setBackgroundColor(Color.parseColor("#C2E0EC"));
                cmdApproved.setBackgroundColor(Color.parseColor("#C2E0EC"));
                cmdApproved.setVisibility(View.VISIBLE);
                cmdNotApproved.setVisibility(View.VISIBLE);

            } else if (o.get("Status").equals("2")) {
                cmdNotApproved.setText("অননুমোদিত");
                // status.setText("অননুমোদিত");
                cmdApproved.setTextColor(Color.BLACK);
                cmdApproved.setText("অনুমোদিত\n" + Global.DateConvertDMY(o.get("ModifyDate")));
                cmdApproved.setBackgroundColor(Color.parseColor("#99cc33"));
                cmdNotApproved.setBackgroundColor(Color.parseColor("#C2E0EC"));
                cmdApproved.setVisibility(View.VISIBLE);
                cmdNotApproved.setVisibility(View.VISIBLE);
            } else if (o.get("Status").equals("3")) {
                cmdApproved.setText("অনুমোদিত");
                // status.setText("অনুমোদিত");
                cmdNotApproved.setTextColor(Color.BLACK);
                cmdNotApproved.setText("অননুমোদিত\n" + Global.DateConvertDMY(o.get("ModifyDate")));
                cmdNotApproved.setBackgroundColor(Color.parseColor("#99cc33"));
                //#C2E0EC
                cmdApproved.setBackgroundColor(Color.parseColor("#C2E0EC"));
                cmdApproved.setVisibility(View.VISIBLE);
                cmdNotApproved.setVisibility(View.VISIBLE);
            } else {
                cmdRequest.setBackgroundColor(Color.parseColor("#C2E0EC"));
                cmdNotApproved.setBackgroundColor(Color.parseColor("#C2E0EC"));
                cmdApproved.setBackgroundColor(Color.parseColor("#C2E0EC"));
                cmdApproved.setVisibility(View.VISIBLE);
                cmdNotApproved.setVisibility(View.VISIBLE);
            }

            if (o.get("Dstatus").equalsIgnoreCase("2")) {
                status.setVisibility(View.VISIBLE);
                memtab.setBackgroundColor(Color.YELLOW);
                status.setText("AHI অগ্রিম কর্মসূচী পরিবর্তন  করতে হবে");
                //workPlanDate.setBackgroundColor(Color.parseColor("#99cc33"));
                //itemdes.setBackgroundColor(Color.parseColor("#99cc33"));
            } else {
                status.setText("");
                memtab.setBackgroundColor(Color.WHITE);
                // memtab.setBackgroundColor(Color.parseColor(""));
                // itemdes.setBackgroundColor(Color.parseColor(""));

            }
            if (o.get("Status").equals("3")) {
                memtab.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        g.setWPProvCode(spnfpaCode.getSelectedItem().toString());
                        g.setWPDate(Global.DateConvertDMY(o.get("workPlanDate")));
                        g.setWPDes(o.get("itemdes"));
                        DisplayNotApprovedItem();


                    }
                });
            } else {

            }

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
