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
 * Created by Nisan on 7/21/2016.
 */
public class HIWorkPlaningView extends Activity {
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
        AlertDialog.Builder adb = new AlertDialog.Builder(HIWorkPlaningView.this);
        switch (item.getItemId()) {
            case R.id.menuClose:
                adb.setTitle("Close");
                adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান?");
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
    // TextView txtReqName;
    Spinner spnfpaCode;
    LinearLayout secReqToCode;
    TextView VlblReqToCode;
    TextView txtFpiWarea;
    EditText txtRemarks;
    // LinearLayout secfpaVill;
    // TextView VlblfpaVill;
    // TextView txtfpaVill;

    LinearLayout secRemarks;
    //LinearLayout secItem;
    //TextView VlblItem;
    // Spinner spnItem;
    Spinner spnVillage1;
    // EditText dtpAgDT;
    // ImageButton btnAgDT;

    //EditText dtpItemDT;
    // ImageButton btnItemDT;

    Button cmdSync;
    //Button cmdDownload;
    /*Button cmdApproved;
    Button cmdNotApproved;
    Button cmdRequest;*/
    String StartTime;
    String DeviceNo;
    ListView list;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.hiworkplaningview);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();

            list = (ListView) findViewById(R.id.lstData);
            // View header = getLayoutInflater().inflate(R.layout.fpaworkplanheading, null);
            // list.addHeaderView(header);

            TableName = "workPlanMaster";
            TableNameDetail = "workPlanDetail";
            secRemarks = (LinearLayout) findViewById(R.id.secRemarks);
            lblHS10 = (TextView) findViewById(R.id.lblHS10);
            secS10 = (LinearLayout) findViewById(R.id.secS10);
            secSlNo = (LinearLayout) findViewById(R.id.secSlNo);
            VlblSlNo = (TextView) findViewById(R.id.VlblSlNo);
            lblHS10.setText(" স্বাস্থ্য পরিদর্শকে  মাসিক অগ্রিম কর্মসূচী");
            secRemarks.setVisibility(View.GONE);
            txtRemarks = (EditText) findViewById(R.id.txtRemarks);
            cmdSync = (Button) findViewById(R.id.cmdSync);
           // cmdDownload = (Button) findViewById(R.id.cmdDownload);
            /*cmdApproved = (Button) findViewById(R.id.cmdApproved);
            cmdNotApproved = (Button) findViewById(R.id.cmdNotApproved);
            cmdRequest = (Button) findViewById(R.id.cmdRequest);*/

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
//            secfpaVill=(LinearLayout)findViewById(R.id.secfpaVill);
//            VlblfpaVill=(TextView) findViewById(R.id.VlblfpaVill);
//            txtfpaVill=(TextView) findViewById(R.id.txtfpaVill);
            // spnfpaVill=(Spinner) findViewById(R.id.spnfpaVill);
            spnfpaCode.setAdapter(C.getArrayAdapterMultiline("Select substr('0' ||ProvCode, -6, 6)||'-'||ProvName from ProviderDB where ProvType ='12'"));
            txtFpiWarea = (TextView) findViewById(R.id.txtFpiWarea);

            spnfpaCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    String val = Global.Left(spnfpaCode.getSelectedItem().toString(), 6);

                    if (val.length() >= 0) {
                        //String union = C.ReturnSingleValue("Select unionid from ProviderDB where ProvCode ='" + val + "'");
                        //txtFpiWarea.setText(C.ReturnSingleValue("select cast(unionid as varchar(2))||','||unionname from Unions where zillaid='" + g.getDistrict() + "' and upazilaid='" + g.getUpazila() + "' and unionid='" + union + "'"));

                        txtFpiWarea.setText(ProvAreaS("11"));
                        txtFpiWarea.setEnabled(false);

                        buttonStatus();
                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            // txtFpiWarea.setText(g.getFWAUnit());

            // txtFpiWarea.setText(ProvArea("3",Global.Left(spnfpaCode.getSelectedItem().toString(),5)));

            spnFPIPMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    String val = Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2);
                    if (val.length() > 0) {

                        DataSearch(Global.Right(spnFPIPMonth.getSelectedItem().toString(), 4) + "-" + Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2), Global.Left(spnfpaCode.getSelectedItem().toString(), 6));
                        buttonStatus();
                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });



            DataSearch(Global.Right(spnFPIPMonth.getSelectedItem().toString(), 4) + "-" + Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2), g.getProvCode());

            cmdSync.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(HIWorkPlaningView.this);

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
                                    if (Connection.haveNetworkConnection(HIWorkPlaningView.this)) {
                                        netwoekAvailable = true;

                                        final ProgressDialog progDailog = ProgressDialog.show(
                                                HIWorkPlaningView.this, "", "অপেক্ষা করুন ...", true);
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
                                        Connection.MessageBox(HIWorkPlaningView.this, "Internet connection is not available.");
                                        // return true;
                                    }





                                }
                            });
                            adb.show();


                        }






                    }


                }
            });

            /*cmdDownload.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    //Check for Internet connectivity
                    /*//*******************************************************************************
                    boolean netwoekAvailable = false;
                    if (Connection.haveNetworkConnection(HIWorkPlaningView.this)) {
                        netwoekAvailable = true;

                        //DownloadFPItoFWA() ;
                        final ProgressDialog progDailog = ProgressDialog.show(
                                HIWorkPlaningView.this, "", "অপেক্ষা করুন ...", true);
                        new Thread() {
                            public void run() {

                                //  DownloadFPItoFWA() ;
                                DownloadFWAWWORKPlAINTables(Global.Left(spnfpaCode.getSelectedItem().toString(), 5));
                                progDailog.dismiss();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        buttonStatus();

                                    }
                                });

                            }
                        }.start();

                    } else {
                        netwoekAvailable = false;
                        Connection.MessageBox(HIWorkPlaningView.this, "Internet connection is not available.");
                        // return true;
                    }


                }
            });
*/


        } catch (Exception e) {
            Connection.MessageBox(HIWorkPlaningView.this, e.getMessage());
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
            //cmdRefresh.setVisibility(View.GONE);
            //cmdDownload.setVisibility(View.GONE);
            cmdSync.setVisibility(View.GONE);
            // cmdRequest.setVisibility(View.GONE);
            // cmdApproved.setVisibility(View.GONE);
            // cmdNotApproved.setVisibility(View.GONE);

        } else if (!C.Existence("Select * FROM workPlanDetail WHERE providerId= '" + g.getProvCode() + "' AND substr( workPlanDate, 1, 7 )='" + Global.Right(spnFPIPMonth.getSelectedItem().toString(), 4) + "-" + Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2) + "'")) {
            //cmdDownload.setVisibility(View.GONE);
            // cmdRefresh.setVisibility(View.GONE);
            cmdSync.setVisibility(View.GONE);
            // cmdRequest.setVisibility(View.GONE);
            // cmdApproved.setVisibility(View.GONE);
            // cmdNotApproved.setVisibility(View.GONE);
        } else if (C.Existence("Select * FROM workPlanDetail WHERE providerId= '" + g.getProvCode() + "' AND substr( workPlanDate, 1, 7 )='" + Global.Right(spnFPIPMonth.getSelectedItem().toString(), 4) + "-" + Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2) + "'")) {
            DataSearch(Global.Right(spnFPIPMonth.getSelectedItem().toString(), 4) + "-" + Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2), g.getProvCode());
            // cmdDownload.setVisibility(View.VISIBLE);
            //cmdRefresh.setVisibility(View.VISIBLE);
            cmdSync.setVisibility(View.VISIBLE);
            // cmdRequest.setVisibility(View.GONE);
            //cmdApproved.setVisibility(View.GONE);
            // cmdNotApproved.setVisibility(View.GONE);
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

      /*  ipcUN,ipcWord,ipcMouza,ipcVill,ipcPara,ipcBariFrom,ipcBariTo,epiproviderId,epischedulerId,ccWard,ccID,leaveType,natProgramType*/

        //workPlanDetail
        sql = "select  \"workPlanId\", \"item\", \"workPlanDate\", \"unitNo\", \"village\", \"elcoFrom\", \"elcoTo\",\"ipcUN\",\"ipcWord\",\"ipcMouza\",\"ipcVill\",\"ipcPara\",\"ipcBariFrom\",\"ipcBariTo\",\"epiproviderId\",\"epischedulerId\",\"ccWard\",\"ccID\", \"leaveType\", \"natProgramType\", \"providerId\", \"systemEntryDate\", \"modifyDate\", \"otherDec\", \"remarks\", 1 as upload\n" +
                "from \"workPlanDetail\" \n" +
                "where \"providerId\" =" + provCode + "";


        VariableList = "workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,ipcUN,ipcWord,ipcMouza,ipcVill,ipcPara,ipcBariFrom,ipcBariTo,epiproviderId,epischedulerId,ccWard,ccID,leaveType,natProgramType,providerId,systemEntryDate,modifyDate,otherDec,remarks,upload";
        C.DownloadJSON(sql, "workPlanDetail", VariableList, "workPlanId, item, workPlanDate, providerId");
    }

    private void Upload() {

        String TableName = "";
        String VariableList = "";


        // message = "Uploading workPlanMaster";
        // jumpTime += 1;
        // progressHandler.sendMessage(progressHandler.obtainMessage());
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



    private String ProvAreaS(String ProvType) {
        String SQL = "";
               //SQL = "select distinct fwaunit from ProviderArea WHERE provCode = '"+ ProvCode +"'";
            SQL = " select UNIONID||'-'||UNIONNAME as UNIONNAME from Unions where UNIONID in (Select UNIONID from ProviderDB where ProvType<>'99' and ProvType='" + ProvType + "')";

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

    private void DisplayFPALeave() {
        final Dialog popupView = new Dialog(HIWorkPlaningView.this);
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
                        "  WHEN CAST ( C.itemcode AS int ) = 2 THEN C.itemdes || '-' || 'স্বাস্থ্য সহকারীর নাম:' ||( \n" +
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
                        " WHERE  schedulerId=substr(B.epischedulerId, 7, 2 ) and wordOld=substr(B.epischedulerId, 2, 1 ) and subBlockId=cast(substr(B.epischedulerId , 1, 1 ) as varchar(2)))\n" +
                        "\n" +
                        " \n" +
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
                        "WHEN CAST ( C.itemcode AS int ) = 6 \n" +
                        "AND\n" +
                        "B.leaveType = 8 THEN C.itemdes || ':সরকারী ' \n" +
                        "WHEN CAST ( C.itemcode AS int ) = 6\n" +
                        "AND\n" +
                        "B.leaveType = 9 THEN C.itemdes || ':অন্যান্য ' \n" +
                        "ELSE ifnull( C.itemdes, '' ) \n" +
                        "END )) AS itemdes\n" +
                        "FROM workPlanMaster A\n" +
                        "INNER JOIN workPlanDetail B\n" +
                        "ON A.workPlanId = B.workPlanId\n" +
                        "INNER JOIN fpaItem C\n" +
                        "ON B.item = C.itemcode and A.providerId=B.providerId and A.month=substr( B.workPlanDate, 1, 7 )\n" +
                        "WHERE C.type = '5'\n" +
                        "AND B.providerId='" + ProvCode + "'" +
                        "AND\n" +
                        "substr( B.workPlanDate, 1, 7 )='" + month + "' group by B.workPlanDate");
               /* cur = C.ReadData("select B.cWorkPlanId, B.workPlanDate,C.itemdes from workPlanMaster A " +
                        "INNER JOIN workPlanDetail B ON A.workPlanId = B.workPlanId " +
                        "INNER JOIN fpaItem C ON B.fpaItem = C.itemcode WHERE A.workAreaId = '" +Global.Left(txtFpiWarea.getText().toString(),2) + "' AND C.type='2' AND A.month = '" + month + "'");

 */
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
                        "  WHEN CAST ( C.itemcode AS int ) = 2 THEN C.itemdes || '-' || 'স্বাস্থ্য সহকারীর নাম:' ||( \n" +
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
                        " WHERE  schedulerId=substr(B.epischedulerId, 7, 2 ) and wordOld=substr(B.epischedulerId, 2, 1 ) and subBlockId=cast(substr(B.epischedulerId , 1, 1 ) as varchar(2)))\n" +
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
                        "ON B.item = C.itemcode \n" +
                        "WHERE C.type = '5'\n" +
                        "AND B.providerId='" + ProvCode + "'" +
                        "AND\n" +
                        "substr( B.workPlanDate, 1, 7 )='" + month + "' group by B.workPlanDate");

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

                dataAdapter = new SimpleAdapter(HIWorkPlaningView.this, dataList, R.layout.fpaworkplanrowview, new String[]{"edit"},
                        new int[]{R.id.cmdB1});

                list.setAdapter(new DataListAdapter(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(HIWorkPlaningView.this, e.getMessage());
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
                cmdSync.setText("Submitted");
                cmdSync.setBackgroundColor(Color.parseColor("#99cc33"));

            } else if (o.get("Upload").equals("2"))

            {
                cmdSync.setText("Submit");
                cmdSync.setTextColor(Color.BLACK);

                cmdSync.setBackgroundColor(Color.parseColor("#C2E0EC"));


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