package com.data.rhis2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
 * Created by Nisan on 7/24/2016.
 */
public class Aeaadownloaddata extends Activity {
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
        AlertDialog.Builder adb = new AlertDialog.Builder(Aeaadownloaddata.this);
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
    //  LinearLayout secReqToCode;
    // TextView VlblReqToCode;
    TextView txtFpiWarea;
    //  EditText txtRemarks;
   /* LinearLayout secfpaVill;
    TextView VlblfpaVill;
    TextView txtfpaVill;*/

    // LinearLayout secRemarks;
    //LinearLayout secItem;
    //TextView VlblItem;
    // Spinner spnItem;
    // Spinner spnVillage1;
    // EditText dtpAgDT;
    // ImageButton btnAgDT;

    //EditText dtpItemDT;
    // ImageButton btnItemDT;

    Button cmdSync;
    Button cmdDownload;
    /* Button cmdApproved;
     Button cmdNotApproved;
     Button cmdRequest;*/
    String StartTime;
    String DeviceNo;
    ListView list;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.aeaadownloaddata);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();

            list = (ListView) findViewById(R.id.lstData);
            // View header = getLayoutInflater().inflate(R.layout.fpaworkplanheading, null);
            // list.addHeaderView(header);

            TableName = "workPlanMaster";
            TableNameDetail = "workPlanDetail";
            //  secRemarks=(LinearLayout) findViewById(R.id.secRemarks);
            lblHS10 = (TextView) findViewById(R.id.lblHS10);
            secS10 = (LinearLayout) findViewById(R.id.secS10);
            secSlNo = (LinearLayout) findViewById(R.id.secSlNo);
            VlblSlNo = (TextView) findViewById(R.id.VlblSlNo);
            lblHS10.setText("স্বাস্থ্য পরিদর্শকের কর্ম এলাকা ডাউনলোড ");
            //  secRemarks.setVisibility(View.GONE);
            //  txtRemarks=(EditText) findViewById(R.id.txtRemarks);
            cmdSync = (Button) findViewById(R.id.cmdSync);
            cmdDownload = (Button) findViewById(R.id.cmdDownload);
            /*cmdApproved=(Button)findViewById(R.id.cmdApproved);
            cmdNotApproved=(Button)findViewById(R.id.cmdNotApproved);
            cmdRequest=(Button)findViewById(R.id.cmdRequest);*/

           /* secFPIPMonth=(LinearLayout)findViewById(R.id.secFPIPMonth);
            VlblFPIPMonth=(TextView)findViewById(R.id.VlblFPIPMonth);
            spnFPIPMonth=(Spinner)findViewById(R.id.spnFPIPMonth);
            // spnFPIPMonth.setAdapter(C.getArrayAdapterMultiline("SELECT strftime('%Y', date('now')) ||','||mName||':-'||substr('0' ||id, -2, 2) as ym from month  where id <=(SELECT strftime('%m','now')+1) order by id Desc"));
            spnFPIPMonth.setAdapter(C.getArrayAdapterMultiline("SELECT '99-- সিলেক্ট মাস-' AS ym Union SELECT substr('0' ||id, -2, 2)||'-'||mName||','||strftime('%Y', date('now'))  as ym from month  where id <=(SELECT strftime('%m','now')+1) order by ym Desc"));

   */
            secReq = (LinearLayout) findViewById(R.id.secReq);
            VlblReqName = (TextView) findViewById(R.id.VlblReqName);
            spnfpaCode = (Spinner) findViewById(R.id.spnfpaCode);
           /* secfpaVill=(LinearLayout)findViewById(R.id.secfpaVill);
            VlblfpaVill=(TextView) findViewById(R.id.VlblfpaVill);
            txtfpaVill=(TextView) findViewById(R.id.txtfpaVill);*/
            // spnfpaVill=(Spinner) findViewById(R.id.spnfpaVill);
            spnfpaCode.setAdapter(C.getArrayAdapterMultiline("Select ProvCode||'-'||ProvName from ProviderDB where ProvType ='11'"));
            txtFpiWarea = (TextView) findViewById(R.id.txtFpiWarea);

            spnfpaCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    String val = Global.Left(spnfpaCode.getSelectedItem().toString(), 5);
                    if (val.length() >= 0) {
                        String union = C.ReturnSingleValue("Select unionid from ProviderDB where ProvCode ='" + val + "'");
                        txtFpiWarea.setText(C.ReturnSingleValue("select cast(unionid as varchar(2))||','||unionname from Unions where zillaid='" + g.getDistrict() + "' and upazilaid='" + g.getUpazila() + "' and unionid='" + union + "'"));
                        txtFpiWarea.setEnabled(false);


                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            // txtFpiWarea.setText(ProvArea("3",Global.Left(spnfpaCode.getSelectedItem().toString(),5)));
            // secReqToCode=(LinearLayout)findViewById(R.id.secReqToCode);
            // VlblReqToCode=(TextView) findViewById(R.id.VlblReqToCode);

            // DataSearch(Global.Right(spnFPIPMonth.getSelectedItem().toString(), 4)+"-"+Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2),Global.Left(spnfpaCode.getSelectedItem().toString(),5));


            cmdSync.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    //Check for Internet connectivity
                    //*******************************************************************************
                    boolean netwoekAvailable = false;
                    if (Connection.haveNetworkConnection(Aeaadownloaddata.this)) {
                        netwoekAvailable = true;
                        //  UploadFWA();
                        final ProgressDialog progDailog = ProgressDialog.show(
                                Aeaadownloaddata.this, "", "অপেক্ষা করুন ...", true);
                        new Thread() {
                            public void run() {

                                // UploadAHI();

                                progDailog.dismiss();

                            }
                        }.start();

                    } else {
                        netwoekAvailable = false;
                        Connection.MessageBox(Aeaadownloaddata.this, "Internet connection is not available.");
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
                    if (Connection.haveNetworkConnection(Aeaadownloaddata.this)) {
                        netwoekAvailable = true;

                        //DownloadFPItoFWA() ;
                        final ProgressDialog progDailog = ProgressDialog.show(
                                Aeaadownloaddata.this, "", "অপেক্ষা করুন ...", true);
                        new Thread() {
                            public void run() {

                                //  DownloadFPItoFWA() ;
                                DownloadFWAWWORKPlAINTables(g.getDistrict(), g.getUpazila(), Global.Left(txtFpiWarea.getText().toString(), 2), "2", Global.Left(spnfpaCode.getSelectedItem().toString(), 5));
                                // DownloadFWAWWORKPlAINTables(Global.Left(spnfpaCode.getSelectedItem().toString(),5));
                                progDailog.dismiss();

                            }
                        }.start();

                    } else {
                        netwoekAvailable = false;
                        Connection.MessageBox(Aeaadownloaddata.this, "Internet connection is not available.");
                        // return true;
                    }


                }
            });


        } catch (Exception e) {
            Connection.MessageBox(Aeaadownloaddata.this, e.getMessage());
            return;
        }
    }

    public void DownloadFWAWWORKPlAINTables(String Dist, String Upz, String UN, String ProvType, String ProvCode) {

        String Res = "";
        String SQLStr = "";
        try {
            //Service Provider Area
            SQLStr = "Select a.zillaid, a.upazilaid, a.unionid, a.mouzaid, a.villageid, a.\"FWAUnit\", a.\"Ward\", a.\"WardNew\", a.\"Block\",a.\"provType\",a.\"provCode\"";
            SQLStr += " from \"Village\" v";
            SQLStr += " inner join \"ProviderArea\" a on v.\"ZILLAID\"=a.zillaid and v.\"UPAZILAID\"=a.upazilaid and v.\"UNIONID\"=a.unionid and v.\"MOUZAID\"=a.mouzaid and v.\"VILLAGEID\"=a.villageid";
            SQLStr += " INNER JOIN \"ProviderDB\" PDB ON PDB.\"ProvCode\" = a.\"provCode\" ";
            SQLStr += " Where a.zillaid='" + Dist + "' and";
            SQLStr += " a.upazilaid='" + Upz + "' and";
            SQLStr += " a.unionid='" + UN + "' and";
            SQLStr += " PDB.\"ProvType\"='" + ProvType + "'";// and";
            // SQLStr += " a.\"provCode\"='" + ProvCode + "'";

            Res = C.DownloadJSON(SQLStr, "ProviderArea", "zillaid, upazilaid, unionid, mouzaid, villageid, FWAUnit, Ward, WardNew, Block,provType,provCode", "zillaid, upazilaid, unionid, mouzaid, villageid,provCode");


            //Mouza
            SQLStr = "Select m.\"ZILLAID\", m.\"UPAZILAID\", m.\"MUNICIPALITYID\", m.\"UNIONID\", m.\"MOUZAID\", m.\"RMO\", m.\"MOUZANAMEENG\", m.\"MOUZANAME\" from \"Mouza\" m";
            SQLStr += " inner join \"ProviderArea\" a on m.\"ZILLAID\"=a.zillaid and m.\"UPAZILAID\"=a.upazilaid and m.\"UNIONID\"=a.unionid and m.\"MOUZAID\"=a.mouzaid";
            SQLStr += " \tINNER JOIN \"ProviderDB\" PDB ON PDB.\"ProvCode\" = a.\"provCode\" ";
            SQLStr += " where PDB.\"ProvType\" = '" + ProvType + "'  AND PDB.\"supervisorCode\"='" + ProvCode + "' and m.\"ZILLAID\"='" + Dist + "' and m.\"UPAZILAID\"='" + Upz + "' and m.\"UNIONID\"='" + UN + "'";
            Res = C.DownloadJSON(SQLStr, "Mouza", "ZILLAID, UPAZILAID, MUNICIPALITYID, UNIONID, MOUZAID, RMO, MOUZANAMEENG, MOUZANAME", "ZillaID,UPAZILAID,UnionId,MOUZAID");

            //Village
            SQLStr = "Select v.\"ZILLAID\", v.\"UPAZILAID\", v.\"UNIONID\", v.\"MOUZAID\", v.\"VILLAGEID\", v.\"RMO\", v.\"VILLAGENAMEENG\", v.\"VILLAGENAME\", coalesce(v.\"CRRVILLAGENAME\",'')CRRVILLAGENAME from \"Village\" v";
            SQLStr += " inner join \"ProviderArea\" a on v.\"ZILLAID\"=a.zillaid and v.\"UPAZILAID\"=a.upazilaid and v.\"UNIONID\"=a.unionid and v.\"MOUZAID\"=a.mouzaid and v.\"VILLAGEID\"=a.villageid";
            SQLStr += " INNER JOIN \"ProviderDB\" PDB ON PDB.\"ProvCode\" = a.\"provCode\" ";
            SQLStr += " where PDB.\"ProvType\" = '" + ProvType + "' AND PDB.\"supervisorCode\"='" + ProvCode + "' and v.\"ZILLAID\"='" + Dist + "' and v.\"UPAZILAID\"='" + Upz + "' and v.\"UNIONID\"='" + UN + "'";
            Res = C.DownloadJSON(SQLStr, "Village", "ZILLAID, UPAZILAID, UNIONID, MOUZAID, VILLAGEID, RMO, VILLAGENAMEENG, VILLAGENAME, CRRVILLAGENAME", "ZillaID,UPAZILAID,UnionId,MOUZAID,VillageID");

            // epiScheduler
            SQLStr = "Select epis.\"schedulerId\",epis.\"scheduleDate\",epis.\"providerId\",epis.\"subBlockId\",epis.\"centerName\",epis.\"systemEntryDate\",epis.\"modifyDate\",epis.\"upload\"";
            SQLStr += " from \"Village\" v";
            SQLStr += " inner join \"ProviderArea\" a on v.\"ZILLAID\"=a.zillaid and v.\"UPAZILAID\"=a.upazilaid and v.\"UNIONID\"=a.unionid and v.\"MOUZAID\"=a.mouzaid and v.\"VILLAGEID\"=a.villageid";
            SQLStr += " INNER JOIN \"ProviderDB\" PDB ON PDB.\"ProvCode\" = a.\"provCode\" ";
            SQLStr += "  INNER JOIN \"epiScheduler\" epis ON epis.\"providerId\" = a.\"provCode\" ";
            SQLStr += " Where a.zillaid='" + Dist + "' and";
            SQLStr += " a.upazilaid='" + Upz + "' and";
            SQLStr += " a.unionid='" + UN + "' and";
            SQLStr += " PDB.\"ProvType\"='" + ProvType + "'";// and";
            // SQLStr += " a.\"provCode\"='" + ProvCode + "'";

            Res = C.DownloadJSON(SQLStr, "epiScheduler", "schedulerId, scheduleDate, providerId, subBlockId, centerName, systemEntryDate, modifyDate, upload", "schedulerId, providerId");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String ProvArea(String ProvType, String ProvCode) {
        String SQL = "";
        if (ProvType.equals("2"))
            SQL = "select distinct ward from ProviderArea WHERE provCode = '" + ProvCode + "'";
            //SQL="select distinct p.ward||' সাব ব্লক :'||p.Block||'-'||u.BNameBan from ProviderArea p,HABlock u WHERE u.BCode=p.block and p.provCode= '"+ ProvCode +"'";
        else if (ProvType.equals("3"))
            //SQL = "select distinct fwaunit from ProviderArea WHERE provCode = '"+ ProvCode +"'";
            SQL = "select distinct p.fwaunit||'-'||u.UNameBan from ProviderArea p,FWAUnit u where u.UCode=p.fwaunit and p.provCode ='" + ProvCode + "'";

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

    private String ProvVill(String ProvCode) {
        String SQL = "";
        // SQL="select MOUZAID||VILLAGEID||'-'||VILLAGENAME as VILLAGENAME from Village where mouzaid||VILLAGEID in (Select mouzaid||VILLAGEID from ProviderArea where ProvCode ='"+ ProvCode +"'";
        SQL = " select MOUZAID||VILLAGEID||'-'||VILLAGENAME as VILLAGENAME from Village where mouzaid||VILLAGEID in (Select mouzaid||VILLAGEID from ProviderArea where ProvCode='" + ProvCode + "')";

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

    private String ProvAreaS(String ProvType, String ProvCode) {
        String SQL = "";
        if (ProvType.equals("2"))
            SQL = "select distinct ward from ProviderArea WHERE provCode = '" + ProvCode + "'";
            //SQL="select distinct p.ward||' সাব ব্লক :'||p.Block||'-'||u.BNameBan from ProviderArea p,HABlock u WHERE u.BCode=p.block and p.provCode= '"+ ProvCode +"'";
        else if (ProvType.equals("3"))
            //SQL = "select distinct fwaunit from ProviderArea WHERE provCode = '"+ ProvCode +"'";
            SQL = "select distinct p.fwaunit from ProviderArea p,FWAUnit u where u.UCode=p.fwaunit and p.provCode ='" + ProvCode + "'";

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

            retValue = retValue.length() > 0 ? retValue + cur.getString(0) : cur.getString(0);

            cur.moveToNext();
        }
        cur.close();

        return retValue;
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