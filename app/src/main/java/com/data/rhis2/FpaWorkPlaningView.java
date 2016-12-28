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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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
 * Created by Nisan on 4/5/2016.
 */
public class FpaWorkPlaningView extends Activity {
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


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mnuview1, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder adb = new AlertDialog.Builder(FpaWorkPlaningView.this);
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
                Intent intentforview = new Intent(getApplicationContext(), FpaWorkPlaningViewFPINotAproved.class);
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

    LinearLayout secS10;
    LinearLayout secSlNo;
    TextView VlblSlNo;



    LinearLayout secFPIPMonth;
    TextView VlblFPIPMonth;
    Spinner spnFPIPMonth;

    TextView lblHS10;
    LinearLayout secReq;
    TextView VlblReqName;

    Spinner spnfpaCode;
    LinearLayout secReqToCode;
    TextView VlblReqToCode;
    TextView txtFpiWarea;
    EditText txtRemarks;
    LinearLayout secfpaVill;
    TextView VlblfpaVill;
    TextView txtfpaVill;


    LinearLayout secListRow1;

    Spinner spnVillage1;

    Button cmdDownload;
    Button cmdApproved;
    Button cmdNotApproved;
    Button cmdRequest;
   // Button cmdRefresh;
    String StartTime;
    String DeviceNo;
    ListView list;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.fpaworkplaningview);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();

            list = (ListView) findViewById(R.id.lstData);


            TableName = "workPlanMaster";
            TableNameDetail = "workPlanDetail";
            secListRow1 = (LinearLayout) findViewById(R.id.secListRow1);

            lblHS10 = (TextView) findViewById(R.id.lblHS10);
            secS10 = (LinearLayout) findViewById(R.id.secS10);
            secSlNo = (LinearLayout) findViewById(R.id.secSlNo);
            VlblSlNo = (TextView) findViewById(R.id.VlblSlNo);
            lblHS10.setText("পরিবার কল্যাণ সহকারীর মাসিক অগ্রিম কর্মসূচী অনুমোদন");

            txtRemarks = (EditText) findViewById(R.id.txtRemarks);

            cmdDownload = (Button) findViewById(R.id.cmdDownload);
            cmdApproved = (Button) findViewById(R.id.cmdApproved);
            cmdNotApproved = (Button) findViewById(R.id.cmdNotApproved);
            cmdRequest = (Button) findViewById(R.id.cmdRequest);

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
        secfpaVill = (LinearLayout) findViewById(R.id.secfpaVill);
        VlblfpaVill = (TextView) findViewById(R.id.VlblfpaVill);
        txtfpaVill = (TextView) findViewById(R.id.txtfpaVill);
            // spnfpaVill=(Spinner) findViewById(R.id.spnfpaVill);
            spnfpaCode.setAdapter(C.getArrayAdapterMultiline("Select substr('0' ||ProvCode, -6, 6)||'-'||ProvName from ProviderDB where ProvType ='3'"));
           // spnfpaCode.setAdapter(C.getArrayAdapterMultiline("Select ProvCode||'-'||ProvName from ProviderDB where ProvType ='3'"));
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

                        txtFpiWarea.setText(ProvArea("3", Global.Left(spnfpaCode.getSelectedItem().toString(), 6)));
                        txtFpiWarea.setEnabled(false);

                        txtfpaVill.setText(ProvVill(Global.Left(spnfpaCode.getSelectedItem().toString(),6)));
                        //  DataSearch(Global.Right(spnFPIPMonth.getSelectedItem().toString(), 4)+"-"+Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2),Global.Left(spnfpaCode.getSelectedItem().toString(),5));
                        buttonStatus();


                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            DisplaySearch(true);
            // txtFpiWarea.setText(g.getFWAUnit());

            //txtFpiWarea.setText(ProvArea("3", Global.Left(spnfpaCode.getSelectedItem().toString(), 6)));
            secReqToCode = (LinearLayout) findViewById(R.id.secReqToCode);
            VlblReqToCode = (TextView) findViewById(R.id.VlblReqToCode);
            // cmdApproved.setVisibility(View.GONE);
            // cmdNotApproved.setVisibility(View.GONE);
            // DataSearch(Global.Right(spnFPIPMonth.getSelectedItem().toString(), 4)+"-"+Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2),Global.Left(spnfpaCode.getSelectedItem().toString(),5));
            secListRow1.setVisibility(View.GONE);




            cmdDownload.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {


                    //Check for Internet connectivity
                    //*******************************************************************************
                    boolean netwoekAvailable = false;
                    if (Connection.haveNetworkConnection(FpaWorkPlaningView.this)) {
                        netwoekAvailable = true;

                        //DownloadFPItoFWA() ;
                        final ProgressDialog progDailog = ProgressDialog.show(
                                FpaWorkPlaningView.this, "", "অপেক্ষা করুন ...", true);
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
                        Connection.MessageBox(FpaWorkPlaningView.this, "Internet connection is not available.");
                        // return true;
                    }

                }
            });

            cmdApproved.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(FpaWorkPlaningView.this);

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

                                SQL += "status ='2'";
                                SQL += "  Where month='" + Global.Right(spnFPIPMonth.getSelectedItem().toString(), 4) + "-" + Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2) + "' AND  providerId  = '" + Global.Left(spnfpaCode.getSelectedItem().toString(), 6) + "'";
                                C.Save(SQL);
                                Connection.MessageBox(FpaWorkPlaningView.this, "অনুমোদিত সম্পন্ন হয়েছে।");
                                // cmdApproved.setText("অনুমোদিত");

                                boolean netwoekAvailable = false;
                                if (Connection.haveNetworkConnection(FpaWorkPlaningView.this)) {
                                    netwoekAvailable = true;

                                    //DownloadFPItoFWA() ;
                                    final ProgressDialog progDailog = ProgressDialog.show(
                                            FpaWorkPlaningView.this, "", "অপেক্ষা করুন ...", true);
                                    new Thread() {
                                        public void run() {
                                            UploadFWA();
                                            progDailog.dismiss();

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //stuff that updates ui
                                                    DataSearch(Global.Right(spnFPIPMonth.getSelectedItem().toString(), 4) + "-" + Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2), Global.Left(spnfpaCode.getSelectedItem().toString(), 6));


                                                }
                                            });

                                        }
                                    }.start();
                                }

                                else {
                                    netwoekAvailable = false;
                                    Connection.MessageBox(FpaWorkPlaningView.this, "Internet connection is not available.");
                                    // return true;
                                }

                            } else {
                                Connection.MessageBox(FpaWorkPlaningView.this, "তথ্য পাওয়া যায় না.");
                            }
                        }
                    });
                    adb.show();


                }

            });

            cmdNotApproved.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(FpaWorkPlaningView.this);

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
                                Connection.MessageBox(FpaWorkPlaningView.this, "অননুমোদিত সম্পন্ন হয়েছে।");
                                // cmdNotApproved.setText("অননুমোদন");
                              //  DataSearch(Global.Right(spnFPIPMonth.getSelectedItem().toString(), 4) + "-" + Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2), Global.Left(spnfpaCode.getSelectedItem().toString(), 5));


                                boolean netwoekAvailable = false;
                                if (Connection.haveNetworkConnection(FpaWorkPlaningView.this)) {
                                    netwoekAvailable = true;

                                    //DownloadFPItoFWA() ;
                                    final ProgressDialog progDailog = ProgressDialog.show(
                                            FpaWorkPlaningView.this, "", "অপেক্ষা করুন ...", true);
                                    new Thread() {
                                        public void run() {
                                            UploadFWA();
                                            // DownloadFWAWWORKPlAINTables(Global.Left(spnfpaCode.getSelectedItem().toString(), 5));
                                            progDailog.dismiss();
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //stuff that updates ui
                                                    DataSearch(Global.Right(spnFPIPMonth.getSelectedItem().toString(), 4) + "-" + Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2), Global.Left(spnfpaCode.getSelectedItem().toString(), 6));


                                                }
                                            });

                                        }
                                    }.start();
                                }

                                else {
                                    netwoekAvailable = false;
                                    Connection.MessageBox(FpaWorkPlaningView.this, "Internet connection is not available.");
                                    // return true;
                                }

                            } else {
                                Connection.MessageBox(FpaWorkPlaningView.this, "তথ্য পাওয়া যায় না");
                            }
                        }
                    });
                    adb.show();

                }

            });

        } catch (Exception e) {
            Connection.MessageBox(FpaWorkPlaningView.this, e.getMessage());
            return;
        }
    }

    private void DisplaySearch(boolean willdisplaysearch) {


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ((ImageButton) findViewById(R.id.btnMinus)).setVisibility(View.GONE);
        ((ImageButton) findViewById(R.id.btnPlus)).setVisibility(View.VISIBLE);
        ((LinearLayout) findViewById(R.id.seclblSearchGroup)).setVisibility(View.GONE);

        ((ImageButton) findViewById(R.id.btnPlus)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ((LinearLayout) findViewById(R.id.seclblSearchGroup)).setVisibility(View.VISIBLE);
                ((ImageButton) findViewById(R.id.btnPlus)).setVisibility(View.GONE);
                ((ImageButton) findViewById(R.id.btnMinus)).setVisibility(View.VISIBLE);
            }
        });
        ((ImageButton) findViewById(R.id.btnMinus)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ((LinearLayout) findViewById(R.id.seclblSearchGroup)).setVisibility(View.GONE);
                ((ImageButton) findViewById(R.id.btnMinus)).setVisibility(View.GONE);
                ((ImageButton) findViewById(R.id.btnPlus)).setVisibility(View.VISIBLE);
            }
        });

        final Boolean displayheader = true;

    }

    private void DownloadStatus()
    {
        if (!C.Existence("Select * FROM workPlanDetail WHERE providerId= '" + Global.Left(spnfpaCode.getSelectedItem().toString(), 6) + "' AND substr( workPlanDate, 1, 7 )='" + Global.Right(spnFPIPMonth.getSelectedItem().toString(), 4) + "-" + Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2) + "'")) {
            secListRow1.setVisibility(View.GONE);
            list.setVisibility(View.GONE);
            cmdDownload.setText("");
            cmdDownload.setText("পঃ কঃ  সহকারীর কর্মসূচীটি Download  করতে  এখানে Click করুন ।");
            cmdDownload.setBackgroundColor(Color.parseColor("#C2E0EC"));
            cmdApproved.setVisibility(View.GONE);
            cmdNotApproved.setVisibility(View.GONE);
            cmdApproved.setBackgroundColor(Color.parseColor("#C2E0EC"));
            cmdNotApproved.setBackgroundColor(Color.parseColor("#C2E0EC"));
            //cmdRefresh.setVisibility(View.VISIBLE);
        } else if (C.Existence("Select * FROM workPlanDetail WHERE providerId= '" + Global.Left(spnfpaCode.getSelectedItem().toString(), 6) + "' AND substr( workPlanDate, 1, 7 )='" + Global.Right(spnFPIPMonth.getSelectedItem().toString(), 4) + "-" + Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2) + "'")) {
            secListRow1.setVisibility(View.VISIBLE);
            list.setVisibility(View.VISIBLE);
            cmdDownload.setText("");
            cmdDownload.setText("পঃ কঃ  সহকারীর কর্মসূচীটি Downloaded  করতে  এখানে Click করুন ।");
            cmdDownload.setBackgroundColor(Color.parseColor("#99cc33"));
            cmdApproved.setVisibility(View.VISIBLE);
            cmdNotApproved.setVisibility(View.VISIBLE);
           // cmdRefresh.setVisibility(View.VISIBLE);
        }
        DataSearch(Global.Right(spnFPIPMonth.getSelectedItem().toString(), 4) + "-" + Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2), Global.Left(spnfpaCode.getSelectedItem().toString(), 6));



    }

    private void buttonStatus()
    {
        String val = Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2);
        if (val.length() > 0) {

            if (val.equals("99")) {
                cmdDownload.setBackgroundColor(Color.parseColor("#C2E0EC"));

                list.setVisibility(View.GONE);

                cmdDownload.setVisibility(View.GONE);
                cmdApproved.setVisibility(View.GONE);
                cmdNotApproved.setVisibility(View.GONE);
                secListRow1.setVisibility(View.GONE);

                cmdApproved.setEnabled(false);
                cmdNotApproved.setEnabled(false);
                cmdRequest.setEnabled(false);
                cmdNotApproved.setTextColor(Color.BLACK);
                cmdNotApproved.setText("অননুমোদিত");
                cmdApproved.setTextColor(Color.BLACK);
                cmdApproved.setText("অনুমোদিত");

            } else if (!C.Existence("Select * FROM workPlanDetail WHERE providerId= '" + Global.Left(spnfpaCode.getSelectedItem().toString(), 6) + "' AND substr( workPlanDate, 1, 7 )='" + Global.Right(spnFPIPMonth.getSelectedItem().toString(), 4) + "-" + Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2) + "'")) {
                cmdDownload.setBackgroundColor(Color.parseColor("#C2E0EC"));

                list.setVisibility(View.GONE);
                cmdDownload.setVisibility(View.VISIBLE);

                cmdApproved.setVisibility(View.GONE);
                cmdNotApproved.setVisibility(View.GONE);
                secListRow1.setVisibility(View.GONE);

                cmdApproved.setEnabled(true);
                cmdNotApproved.setEnabled(true);
                cmdRequest.setEnabled(true);

                cmdNotApproved.setTextColor(Color.BLACK);
                cmdNotApproved.setText("অননুমোদিত");
                cmdApproved.setTextColor(Color.BLACK);
                cmdApproved.setText("অনুমোদিত");

            } else if (C.Existence("Select * FROM workPlanDetail WHERE providerId= '" + Global.Left(spnfpaCode.getSelectedItem().toString(), 6) + "' AND substr( workPlanDate, 1, 7 )='" + Global.Right(spnFPIPMonth.getSelectedItem().toString(), 4) + "-" + Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2) + "'")) {
                cmdDownload.setBackgroundColor(Color.parseColor("#C2E0EC"));

                list.setVisibility(View.GONE);
                cmdDownload.setVisibility(View.VISIBLE);

                cmdApproved.setVisibility(View.GONE);
                cmdNotApproved.setVisibility(View.GONE);
                secListRow1.setVisibility(View.GONE);

                cmdApproved.setEnabled(true);
                cmdNotApproved.setEnabled(true);
                cmdRequest.setEnabled(true);

            }
        }
    }

    private void UploadFWA() {

        String TableName = "";
        String VariableList = "";
        TableName = "workPlanMaster";
        VariableList = "workPlanId,workAreaId,providerId,month,status,systemEntryDate,modifyDate,upload";
        C.UploadJSON(TableName, VariableList, "workPlanId, providerId, month");


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
        sql = "select  \"workPlanId\", \"item\", \"workPlanDate\", \"unitNo\", \"village\", \"elcoFrom\", \"elcoTo\", \"leaveType\", \"providerId\", \"systemEntryDate\", \"modifyDate\", \"otherDec\", \"remarks\", 1 as upload,status\n" +
                "from \"workPlanDetail\" \n" +
                "where status<>'2' and status<>'3' and \"providerId\" =" + provCode + "";


        VariableList = "workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,leaveType,providerId,systemEntryDate,modifyDate,otherDec,remarks,upload,status";
        C.DownloadJSON(sql, "workPlanDetail", VariableList, "workPlanId, item, workPlanDate, providerId");


    }

    private String ProvArea(String ProvType, String ProvCode) {
        String SQL = "";
        if (ProvType.equals("2"))
            SQL = "select distinct ward from ProviderArea WHERE Cast(provCode AS INT) = '" + ProvCode + "'";
            //SQL="select distinct p.ward||' সাব ব্লক :'||p.Block||'-'||u.BNameBan from ProviderArea p,HABlock u WHERE u.BCode=p.block and p.provCode= '"+ ProvCode +"'";
        else if (ProvType.equals("3"))
            //SQL = "select distinct fwaunit from ProviderArea WHERE provCode = '"+ ProvCode +"'";
            SQL = "select distinct p.fwaunit||'-'||u.UNameBan from ProviderArea p,FWAUnit u where u.UCode=p.fwaunit and Cast(p.provCode AS INT) ='" + ProvCode + "'";

        Cursor cur = C.ReadData(SQL);
        String retValue = "";
        cur.moveToFirst();
        while (!cur.isAfterLast()) {
        retValue = retValue.length() > 0 ? retValue + ", " + cur.getString(0) : cur.getString(0);

            cur.moveToNext();
        }
        cur.close();

        return retValue;
    }

    private String ProvVill(String ProvCode) {
        String SQL = "";
        // SQL="select MOUZAID||VILLAGEID||'-'||VILLAGENAME as VILLAGENAME from Village where mouzaid||VILLAGEID in (Select mouzaid||VILLAGEID from ProviderArea where ProvCode ='"+ ProvCode +"'";
        SQL = " select MOUZAID||VILLAGEID||'-'||VILLAGENAME as VILLAGENAME from Village where mouzaid||VILLAGEID in (Select mouzaid||VILLAGEID from ProviderArea where Cast(ProvCode AS INT)='" + ProvCode + "')";

        Cursor cur = C.ReadData(SQL);
        String retValue = "";
        cur.moveToFirst();
        while (!cur.isAfterLast()) {

            retValue = retValue.length() > 0 ? retValue + ", " + cur.getString(0) : cur.getString(0);

            cur.moveToNext();
        }
        cur.close();

        return retValue;
    }




    private void DisplayNotApprovedItem() {
        final Dialog popupView = new Dialog(FpaWorkPlaningView.this);
        popupView.setTitle("Not Approved Item");

        popupView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popupView.setContentView(R.layout.fwapopup);
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


                SQL = "Update " + TableName + " Set ";

                SQL += "status ='2',";
                SQL += "modifyDate = '" + Global.DateTimeNowYMDHMS() + "',";
                SQL += "upload ='2'";
                SQL += "  Where workPlanDate='" + Global.DateConvertYMD(txtWorkpDT.getText().toString()) + "'and providerId='" + Global.Left(txtName.getText().toString(), 6) + "'";

                C.Save(SQL);
                Connection.MessageBox(FpaWorkPlaningView.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
                popupView.cancel();
                DataSearch(Global.Right(spnFPIPMonth.getSelectedItem().toString(), 4) + "-" + Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2), Global.Left(spnfpaCode.getSelectedItem().toString(), 6));


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

    private void DataSearch(String month, String ProvCode) {
        try {

            list = (ListView) findViewById(R.id.lstData);
            list.setAdapter(null);
            Cursor cur = null;
            if (month.length() > 0) {
                cur = C.ReadData("SELECT A.status AS status,ifnull(B.status, '' ) AS dstatus,ifnull(A.modifyDate, '' ) AS modifyDate,ifnull(B.upload, '' ) AS upload,\n" +
                        "       B.workPlanDate AS workPlanDate,\n" +
                        "       group_concat(\n" +
                        "       ( CASE\n" +
                        "                WHEN CAST ( C.itemcode AS int ) = 1 THEN C.itemdes || ',-ইউনিট:' ||B.unitNo  ||'-'||(Select UNameBan from FWAUnit where UCode in (SELECT unitNo FROM workPlanDetail where unitNo = B.unitNo  and workPlanDate=B.workPlanDate )\n" +
                        " \n" +
                        ")|| ',গ্রাম:' ||( \n" +
                        "               SELECT VILLAGENAME\n" +
                        "                 FROM Village\n" +
                        "                WHERE MOUZAID IN ( \n" +
                        "                          SELECT substr(village, 1, 3 )\n" +
                        "                            FROM workPlanDetail where providerId= B.providerId and unitNo= B.unitNo and MOUZAID=substr(B.village, 1, 3 ) and VILLAGEID=substr(B.village, 4, 5 ) and workPlanDate=B.workPlanDate\n" +
                        "                      ) \n" +
                        "                       \n" +
                        "           ) \n" +
                        "           || ' ,দম্পতি নম্বর:(' || B.elcoFrom || '-' || B.elcoTo || ')' \n" +
                        "                WHEN CAST ( C.itemcode AS int ) = 16 THEN C.itemdes || ',-ইউনিট:' || B.unitNo  ||'-'||(Select UNameBan from FWAUnit where UCode in (SELECT unitNo FROM workPlanDetail where unitNo = B.unitNo  and workPlanDate=B.workPlanDate )\n" +
                        " \n" +
                        ")|| ',গ্রাম:' ||( \n" +
                        "               SELECT VILLAGENAME\n" +
                        "                 FROM Village\n" +
                        "                WHERE MOUZAID IN ( \n" +
                        "                          SELECT substr(village, 1, 3 )\n" +
                        "                            FROM workPlanDetail where providerId= B.providerId and unitNo= B.unitNo and MOUZAID=substr(B.village, 1, 3 ) and VILLAGEID=substr(B.village, 4, 5 ) and workPlanDate=B.workPlanDate\n" +
                        "                      ) \n" +
                        "                       \n" +
                        "           ) \n" +
                        "           || ' ,দম্পতি নম্বর:(' || B.elcoFrom || '-' || B.elcoTo || ')' \n" +
                        "                WHEN CAST ( C.itemcode AS int ) = 9 \n" +
                        "           AND\n" +
                        "           B.leaveType = 1 THEN C.itemdes || ':বাৎসরিক' \n" +
                        "                WHEN CAST ( C.itemcode AS int ) = 9 \n" +
                        "           AND\n" +
                        "           B.leaveType = 2 THEN C.itemdes || ':অসুস্থতা জনিত' \n" +
                        "                WHEN CAST ( C.itemcode AS int ) = 9 \n" +
                        "           AND\n" +
                        "           B.leaveType = 3 THEN C.itemdes || ':মাতৃত্বকালীন' \n" +
                        "                WHEN CAST ( C.itemcode AS int ) = 9 \n" +
                        "           AND\n" +
                        "           B.leaveType = 4 THEN C.itemdes || ':পিতৃত্ব কালীন ছুটি' \n" +
                        "                WHEN CAST ( C.itemcode AS int ) = 9 \n" +
                        "           AND\n" +
                        "           B.leaveType = 5 THEN C.itemdes || ':নৈমিত্তিক ছুটি' \n" +
                        "                WHEN CAST ( C.itemcode AS int ) = 9 \n" +
                        "           AND\n" +
                        "           B.leaveType = 6 THEN C.itemdes || ':শ্রান্তি ও বিনোদন' \n" +
                        "                WHEN CAST ( C.itemcode AS int ) = 9 \n" +
                        "           AND\n" +
                        "           B.leaveType = 7 THEN C.itemdes || ':ঐচ্ছিক ছুটি' \n" +
                        "                WHEN CAST ( C.itemcode AS int ) = 9 \n" +
                        "           AND\n" +
                        "           B.leaveType = 8 THEN C.itemdes || ':সরকারী ছুটি' \n" +
                        "                WHEN CAST ( C.itemcode AS int ) = 9 \n" +
                        "           AND\n" +
                        "           B.leaveType = 9 THEN C.itemdes || ':অন্যান্য' \n" +
                        "                ELSE ifnull( C.itemdes, '' ) \n" +
                        "       END )) AS itemdes\n" +
                        "  FROM workPlanMaster A\n" +
                        "       INNER JOIN workPlanDetail B\n" +
                        "               ON A.workPlanId = B.workPlanId and B.providerId=A.providerId and A.month=substr( B.workPlanDate, 1, 7 )\n" +
                        "       INNER JOIN fpaItem C\n" +
                        "               ON B.item = C.itemcode\n" +
                        " WHERE B.providerId= '" + ProvCode + "' And C.type = '1' And B.status<> '2'\n" +
                        "       AND\n" +
                        "       substr( B.workPlanDate, 1, 7 )='" + month + "' group by B.workPlanDate,B.status");
            } else {
                cur = C.ReadData("SELECT A.status AS status,ifnull(B.status, '' ) AS dstatus,ifnull(A.modifyDate, '' ) AS modifyDate,ifnull(B.upload, '' ) AS upload,\n" +
                        "       B.workPlanDate AS workPlanDate,\n" +
                        "       group_concat(\n" +
                        "       ( CASE\n" +
                        "                WHEN CAST ( C.itemcode AS int ) = 1 THEN C.itemdes || ',-ইউনিট:' || B.unitNo  ||'-'||(Select UNameBan from FWAUnit where UCode in (SELECT unitNo FROM workPlanDetail where unitNo = B.unitNo  and workPlanDate=B.workPlanDate )\n" +
                        " \n" +
                        ")|| ',গ্রাম:' ||( \n" +
                        "               SELECT VILLAGENAME\n" +
                        "                 FROM Village\n" +
                        "                WHERE MOUZAID IN ( \n" +
                        "                          SELECT substr(village, 1, 3 )\n" +
                        "                            FROM workPlanDetail where providerId= B.providerId and unitNo= B.unitNo and MOUZAID=substr(B.village, 1, 3 ) and VILLAGEID=substr(B.village, 4, 5 ) and workPlanDate=B.workPlanDate\n" +
                        "                      ) \n" +
                        "                       \n" +
                        "           ) \n" +
                        "           || ' ,দম্পতি নম্বর:(' || B.elcoFrom || '-' || B.elcoTo || ')' \n" +
                        "                WHEN CAST ( C.itemcode AS int ) = 16 THEN C.itemdes || ',-ইউনিট:' || B.unitNo  ||'-'||(Select UNameBan from FWAUnit where UCode in (SELECT unitNo FROM workPlanDetail where unitNo = B.unitNo  and workPlanDate=B.workPlanDate )\n" +
                        " \n" +
                        ")|| ',গ্রাম:' ||( \n" +
                        "               SELECT VILLAGENAME\n" +
                        "                 FROM Village\n" +
                        "                WHERE MOUZAID IN ( \n" +
                        "                          SELECT substr(village, 1, 3 )\n" +
                        "                            FROM workPlanDetail where providerId= B.providerId and unitNo= B.unitNo and MOUZAID=substr(B.village, 1, 3 ) and VILLAGEID=substr(B.village, 4, 5 ) and workPlanDate=B.workPlanDate\n" +
                        "                      ) \n" +
                        "                       \n" +
                        "           ) \n" +
                        "           || ' ,দম্পতি নম্বর:(' || B.elcoFrom || '-' || B.elcoTo || ')' \n" +
                        "                WHEN CAST ( C.itemcode AS int ) = 9 \n" +
                        "           AND\n" +
                        "           B.leaveType = 1 THEN C.itemdes || ':বাৎসরিক' \n" +
                        "                WHEN CAST ( C.itemcode AS int ) = 9 \n" +
                        "           AND\n" +
                        "           B.leaveType = 2 THEN C.itemdes || ':অসুস্থতা জনিত' \n" +
                        "                WHEN CAST ( C.itemcode AS int ) = 9 \n" +
                        "           AND\n" +
                        "           B.leaveType = 3 THEN C.itemdes || ':মাতৃত্বকালীন' \n" +
                        "                WHEN CAST ( C.itemcode AS int ) = 9 \n" +
                        "           AND\n" +
                        "           B.leaveType = 4 THEN C.itemdes || ':পিতৃত্ব কালীন ছুটি' \n" +
                        "                WHEN CAST ( C.itemcode AS int ) = 9 \n" +
                        "           AND\n" +
                        "           B.leaveType = 5 THEN C.itemdes || ':নৈমিত্তিক ছুটি' \n" +
                        "                WHEN CAST ( C.itemcode AS int ) = 9 \n" +
                        "           AND\n" +
                        "           B.leaveType = 6 THEN C.itemdes || ':শ্রান্তি ও বিনোদন' \n" +
                        "                WHEN CAST ( C.itemcode AS int ) = 9 \n" +
                        "           AND\n" +
                        "           B.leaveType = 7 THEN C.itemdes || ':ঐচ্ছিক ছুটি' \n" +
                        "                WHEN CAST ( C.itemcode AS int ) = 9 \n" +
                        "           AND\n" +
                        "           B.leaveType = 8 THEN C.itemdes || ':সরকারী ছুটি' \n" +
                        "                WHEN CAST ( C.itemcode AS int ) = 9 \n" +
                        "           AND\n" +
                        "           B.leaveType = 9 THEN C.itemdes || ':অন্যান্য' \n" +
                        "                ELSE ifnull( C.itemdes, '' ) \n" +
                        "       END )) AS itemdes\n" +
                        "  FROM workPlanMaster A\n" +
                        "       INNER JOIN workPlanDetail B\n" +
                        "               ON A.workPlanId = B.workPlanId and B.providerId=A.providerId and A.providerId=B.providerId and A.month=substr( B.workPlanDate, 1, 7 )\n" +
                        "       INNER JOIN fpaItem C\n" +
                        "               ON B.item = C.itemcode\n" +
                        " WHERE B.providerId='" + ProvCode + "' And C.type = '1'  And B.status<> '2' \n" +
                        "       AND\n" +
                        "       substr( B.workPlanDate, 1, 7 )='" + month + "' group by B.workPlanDate,B.status");

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

                dataAdapter = new SimpleAdapter(FpaWorkPlaningView.this, dataList, R.layout.fpaworkplanrowview, new String[]{"edit"},
                        new int[]{R.id.cmdB1});

                list.setAdapter(new DataListAdapter(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(FpaWorkPlaningView.this, e.getMessage());
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

            if (o.get("Status").equals("1")) {


                cmdRequest.setTextColor(Color.BLACK);
                cmdRequest.setText("অপেক্ষাধিন");
                cmdNotApproved.setText("অননুমোদিত");
                cmdApproved.setText("অনুমোদিত");

                cmdNotApproved.setBackgroundColor(Color.parseColor("#C2E0EC"));
                cmdApproved.setBackgroundColor(Color.parseColor("#C2E0EC"));
                cmdApproved.setVisibility(View.VISIBLE);
                cmdNotApproved.setVisibility(View.VISIBLE);

            } else if (o.get("Status").equals("2")) {
                cmdNotApproved.setText("অননুমোদিত");

                cmdApproved.setTextColor(Color.BLACK);
                cmdApproved.setText("অনুমোদিত\n" + Global.DateConvertDMY(o.get("ModifyDate")));
                cmdApproved.setBackgroundColor(Color.parseColor("#99cc33"));
                cmdNotApproved.setBackgroundColor(Color.parseColor("#C2E0EC"));
                cmdApproved.setVisibility(View.VISIBLE);
                cmdNotApproved.setVisibility(View.VISIBLE);
            } else if (o.get("Status").equals("3")) {
                cmdApproved.setText("অনুমোদিত");

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
            }


            if (o.get("Dstatus").equalsIgnoreCase("2")) {
                status.setVisibility(View.VISIBLE);
                memtab.setBackgroundColor(Color.YELLOW);
                status.setText("FWA অগ্রিম কর্মসূচী পরিবর্তন  করতে হবে");

            } else {
                status.setText("");
                memtab.setBackgroundColor(Color.WHITE);


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