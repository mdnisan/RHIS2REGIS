package com.data.rhis2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.provider.Settings;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import Common.Connection;
import Common.Global;

public class EpiListWoman extends Activity {
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mnuclose, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder adb = new AlertDialog.Builder(EpiListWoman.this);
        switch (item.getItemId()) {
            case R.id.menuClose:
                adb.setTitle("Close");
                adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান[হাঁ/না]?");
                adb.setNegativeButton("না", null);
                adb.setPositiveButton("হাঁ", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        finish();
                        /*Intent f1 = new Intent(getApplicationContext(),EpiList.class);
                        startActivity(f1);*/
                    }
                });
                adb.show();
                return true;

        }
        return false;
    }


    ImageButton btnVDate;
    EditText VisitDate;
    String VariableID;
    public String dateSet = "";
    private int hour;
    private int minute;
    private int mDay;
    private int mMonth;
    private int mYear;
    static final int DATE_DIALOG = 1;
    static final int DATE_DIALOG1 = 3;
    static final int TIME_DIALOG = 2;
    Calendar c = Calendar.getInstance();

    SimpleAdapter mSchedule;
    SimpleAdapter eList;

    ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> evmylist = new ArrayList<HashMap<String, String>>();
    Connection C;
    Global g;
    private static final int UPDATEDONE = 900;
    private ProgressDialog pDialog;
    private static String vill;
    private static String bari;
    private static String hhno;
    private static String hhhead;
    private static String totalmember;
    private String ErrMsg;

    private String EB;
    private static String vdate;
    private String TableName;
    private String TableName1;
    // private String schedulerId;
    Spinner spnEPIBlock;
    TextView lblTCount;
    EditText txtSearch;
    ListView list;
    //TextView txtdate;
    //TextView txtCenterName;
    TextView VlblSH3;
    TextView VlblSH4;
    //TextView txtEPIBlock0;
    //  TextView lblTCount;
/*    TextView txtHHNo;
    RadioGroup rdogrpPAddr;
    RadioButton rdoPAddr1;
    RadioButton rdoPAddr2;
    EditText txtPermaAddress;


    RadioGroup rdogrpVGFCard;
    RadioButton rdoVGFCard1;
    RadioButton rdoVGFCard2;

    LinearLayout secPAddr;
    LinearLayout secPermaAddress;
    LinearLayout secReligion;


    TextView FPMethod;
    TextView LMP;
    TextView EDD;*/
    String StartTime;


    Location currentLocation;
    double currentLatitude, currentLongitude;
    Bundle IDbundle = new Bundle();
    Context con;


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

    //***************************************************************************************************************************
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.epilistwoman);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();
            con = this;

            TableName = "epiMasterWoman";
            TableName1 = "epiSchedulerWoman";


            lblTCount = (TextView) findViewById(R.id.lblTCount);
            VlblSH3 = (TextView) findViewById(R.id.VlblSH3);
            VlblSH4 = (TextView) findViewById(R.id.VlblSH4);
            VlblSH3.setBackgroundColor(Color.parseColor("#99cc33"));
            VlblSH4.setBackgroundColor(Color.parseColor("#F0F0F0"));

            spnEPIBlock = (Spinner) findViewById(R.id.spnEPIBlock);

            txtSearch = (EditText) findViewById(R.id.txtSearch);
            list = (ListView) findViewById(R.id.listepiIndex);

            final Button cmdSchedul = (Button) findViewById(R.id.cmdSchedul);

            final ImageButton cmdSearch = (ImageButton) findViewById(R.id.cmdSearch);
            cmdSearch.setBackgroundResource(R.drawable.search);

            spnEPIBlock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    String val = spnEPIBlock.getSelectedItemPosition() == 0 ? "" : Global.Left(spnEPIBlock.getSelectedItem().toString(), 2);
                    if (val.length() > 0) {
                        LoadDataMemberList();

                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            cmdSchedul.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    g.setepiSubBlockId(Global.Mid(spnEPIBlock.getSelectedItem().toString(), 1, 2));
                    Intent f1 = new Intent(getApplicationContext(), epischdulwoman.class);
                    startActivity(f1);


                }
            });

            LoadDataMemberList();

        } catch (Exception ex) {
            Connection.MessageBox(EpiListWoman.this, ex.getMessage());
            return;
        }
    }


    private void DisplayEpiReg() {
        final Dialog popupView = new Dialog(EpiListWoman.this);
        popupView.setTitle("EPI Registration");

        popupView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popupView.setContentView(R.layout.epiregwoman);
        popupView.setCancelable(true);
        popupView.setCanceledOnTouchOutside(true);


        final EditText txtregisNo = (EditText) popupView.findViewById(R.id.txtregisNo);
        final TextView txtName = (TextView) popupView.findViewById(R.id.txtName);
        final TextView txtelcono = (TextView) popupView.findViewById(R.id.txtelcono);
        final TextView txtmName = (TextView) popupView.findViewById(R.id.txtmName);
        final TextView txtfName = (TextView) popupView.findViewById(R.id.txtfName);
        final TextView txtDoB = (TextView) popupView.findViewById(R.id.txtDoB);

        final LinearLayout secregisNo = (LinearLayout) popupView.findViewById(R.id.secregisNo);
        final LinearLayout secregDate = (LinearLayout) popupView.findViewById(R.id.secregDate);
        // int id;
        popupView.show();


        VisitDate = (EditText) popupView.findViewById(R.id.dtpregDate);
        // VisitDate.setText(Global.DateNowDMY());
        btnVDate = (ImageButton) popupView.findViewById(R.id.btnregDate);
        btnVDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG);
            }
        });
        txtName.setText(g.getepiName());


        txtmName.setText(g.getepimName());
        txtfName.setText(g.getepifName());
        txtDoB.setText(g.getDOB());
        txtelcono.setText(g.getELCONo());
        //g.setELCONo(o.get("elcono"));
        String SQL = "Select * from epiMasterWoman Where healthId='" + g.getHealthID() + "'";

        if (C.Existence(SQL)) {
            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();

            while (!cur.isAfterLast()) {
                txtregisNo.setText(cur.getString(cur.getColumnIndex("regNo")));
                VisitDate.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("regDate"))));
                //txtelcono.setText(cur.getString(cur.getColumnIndex("elcono")));
                cur.moveToNext();
            }
            cur.close();
        } else {
            Button cmdSave = (Button) popupView.findViewById(R.id.cmdSave);
            cmdSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    TableName = "epiMasterWoman";
                    String SQL = "";

                    if (txtregisNo.getText().toString().length() == 0 & secregisNo.isShown()) {
                        Connection.MessageBox(EpiListWoman.this, "Required field: Registration no..");
                        txtregisNo.requestFocus();
                        return;
                    }

                    if (VisitDate.getText().toString().length() == 0 & secregDate.isShown()) {
                        Connection.MessageBox(EpiListWoman.this, "Required field: Registration Date..");
                        VisitDate.requestFocus();
                        return;
                    }

                    if (!C.Existence("Select schedulerId,healthId,providerId,upload from " + TableName + "  Where schedulerId='" + g.getepiSchedulerId() + "' and healthId='" + g.getHealthID() + "'and providerId='" + g.getProvCode() + "'")) {
                        SQL = "Insert into " + TableName + "(schedulerId,healthId,providerId,upload)Values('" + g.getepiSchedulerId() + "', '" + g.getHealthID() + "', '" + g.getProvCode() + "','2')";
                        C.Save(SQL);
                    }


                    SQL = "Update " + TableName + " Set ";
                    SQL += "houseNo='',";
                    SQL += "regNo = '" + txtregisNo.getText().toString() + "',";
                    SQL += "regDate = '" + Global.DateConvertYMD(VisitDate.getText().toString()) + "',";
                    SQL += "remarks ='',";
                    SQL += "systemEntryDate = '" + Global.DateTimeNowYMDHMS() + "',";
                    SQL += "upload ='2'";
                    SQL += "  Where schedulerId='" + g.getepiSchedulerId() + "' and healthId='" + g.getHealthID() + "'and providerId='" + g.getProvCode() + "'";
                    C.Save(SQL);
                    Connection.MessageBox(EpiListWoman.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
                    finish();
                    Intent f1 = new Intent(getApplicationContext(), EpiListWoman.class);
                    startActivity(f1);

                }
            });


        }
        Button cmdClose = (Button) popupView.findViewById(R.id.cmdClose);
        cmdClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                popupView.cancel();
            }
        });

    }


    private void DataSave() {
        try {

            String SQL = "";

            if (!C.Existence("Select schedulerId,healthId,providerId,upload from " + TableName + "  Where schedulerId='" + g.getepiSchedulerId() + "' and healthId='" + g.getHealthID() + "'and providerId='" + g.getProvCode() + "'")) {
                SQL = "Insert into " + TableName + "(schedulerId,healthId,providerId,upload)Values('" + g.getepiSchedulerId() + "', '" + g.getHealthID() + "', '" + g.getProvCode() + "','2')";
                C.Save(SQL);
            }

            SQL = "Update " + TableName + " Set ";
            SQL += "schedulerId= '" + g.getepiSchedulerId() + "',";
            SQL += "healthId = '" + g.getHealthID() + "',";
            SQL += "providerId = '" + g.getProvCode() + "',";
            SQL += "houseNo='',";
            SQL += "remarks ='',";
            SQL += "systemEntryDate = '" + Global.DateTimeNowYMDHMS() + "',";
            SQL += "upload ='2'";
            SQL += "  Where schedulerId='" + g.getepiSchedulerId() + "' and healthId='" + g.getHealthID() + "'and providerId='" + g.getProvCode() + "'";
            C.Save(SQL);
            Connection.MessageBox(EpiListWoman.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
            finish();
        } catch (Exception e) {
            Connection.MessageBox(EpiListWoman.this, e.getMessage());
            return;
        }
    }

    private void LoadDataMemberList() {
        try {

            FindLocation();

            StartTime = g.CurrentTime24();


            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Activity.KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
            lock.disableKeyguard();

            final ListView list = (ListView) findViewById(R.id.listepiIndex);

            View header = getLayoutInflater().inflate(R.layout.epilistheadingwoman, null);
            list.addHeaderView(header);

            spnEPIBlock.setAdapter(C.getArrayAdapter("Select ' সকল সাব ব্লক'as BCode union Select BCode||'-'||BNameBan as BCode from HABlock order by BCode asc"));


            spnEPIBlock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    String val = spnEPIBlock.getSelectedItemPosition() == 0 ? "" : Global.Left(spnEPIBlock.getSelectedItem().toString(), 2);
                    if (val.length() >= 0) {
                        totalCount = 0;

                        DataRetrieve("", list, "active", Global.Left(spnEPIBlock.getSelectedItem().toString(), 2), txtSearch.getText().toString());
                        lblTCount.setText("(সর্ব মোট:" + String.valueOf(totalCount) + ")");

                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            final ImageButton cmdSearch = (ImageButton) findViewById(R.id.cmdSearch);
            cmdSearch.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    totalCount = 0;
                    if (txtSearch.length() == 0) {
                        Connection.MessageBox(EpiListWoman.this, "Required field:Search..");
                        txtSearch.requestFocus();
                        return;
                    }
                    DataRetrieve("", list, "active", Global.Left(spnEPIBlock.getSelectedItem().toString(), 2), txtSearch.getText().toString());
                    lblTCount.setText("(সর্ব মোট:" + String.valueOf(totalCount) + ")");

                }
            });


            DataRetrieve("", list, "active", Global.Left(spnEPIBlock.getSelectedItem().toString(), 2), "");

        } catch (Exception ex) {
            Connection.MessageBox(EpiListWoman.this, ex.getMessage());
            return;
        }
    }


    int totalCount = 0;

    public void DataRetrieve(String HH, ListView list, String ActiveOrAll, String EPIBlock, String healthId) {
        try {
            String SQLStr = "";

// :EPIBlock Search

            if (spnEPIBlock.getSelectedItemPosition() == 0 & txtSearch.length() == 0) {
                /*SQLStr = " Select ifnull(e.elcoNo,'')  as elcoNo, epimaster.healthId As epiHealthId,epimaster.regNo AS regNo, (case when epimaster.regNo is not null then '2' else '1' end) as regC, strftime('%d/%m/%Y', date(epimaster.regDate)) AS regDate,epimaster.houseNo AS houseNo, "+
                " strftime('%d/%m/%Y', date(ExDate)) AS ExDate, "+
                " Dist, Upz, UN, Mouza, Vill, (select VILLAGENAME FROM Village WHERE VILLAGEID IN(SELECT Vill FROM Member)) VillageName, ProvType as provtype,ProvCode as provcode, HHNo, SNo as SNo, "+
                " ifnull(member.HealthID,'') as HealthID, ifnull(NameEng,'') as NameEng, "+
                " ifnull(NameBang,'') as NameBang, ifnull(Rth,'') as Rth, ifnull(HaveNID,'') as HaveNID, ifnull(NID,'') as NID, "+
                " ifnull(NIDStatus,'') as NIDStatus, ifnull(HaveBR,'') as HaveBR, ifnull(BRID,'') as BRID, "+
                " ifnull(BRIDStatus,'') as BRIDStatus, ifnull(MobileNo1,'') as MobileNo1, "+
                " ifnull(MobileNo2,'') as MobileNo2,ifnull(MobileYN,'')as MobileYN,strftime('%d/%m/%Y', date(ifnull(DOB,''))) AS DOB, "+
                " Cast(((julianday(date('now'))-julianday(DOB))/365.25) as int) as Age, "+
                " Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) as AgeM, ifnull(DOBSource,'') as DOBSource, "+
                " ifnull(BPlace,'') as BPlace, ifnull(FNo,'') as FNo, ifnull(MNo,'') as MNo, ifnull(Sex,'') as Sex, ifnull(MS,'') as MS, ifnull(SPNO1,'') as SPNO1,ifnull(SPNO2,'') as SPNO2, "+
                " ifnull(SPNO3,'') as SPNO3,ifnull(SPNO4,'') as SPNO4, "+// ifnull(ELCONo,'') as ELCONo,
                " ifnull(ELCODontKnow,'') as ELCODontKnow, ifnull(EDU,'') as EDU, ifnull(Rel,'') as Rel, "+
                " ifnull(Nationality,'') as Nationality, ifnull(OCP,'') as OCP, "+
                " ifnull(ExType,'')as ExType "+
                " from Member "+
                " LEFT JOIN epiMasterWoman epimaster ON epimaster.healthId = Member.healthid "+
                " LEFT JOIN elco e ON e.healthId = Member.healthid "+
                " WHERE Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) >=15 "+
                " & Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) <= 49 and Member.Sex=2";
*/
                /*SQLStr = " Select member.ElcoNo as elcoNo, epimaster.healthId As epiHealthId,epimaster.regNo AS regNo, (case when epimaster.regNo is not null then '2' else '1' end) as regC, strftime('%d/%m/%Y', date(epimaster.regDate)) AS regDate,epimaster.houseNo AS houseNo, "+
                " strftime('%d/%m/%Y', date(ExDate)) AS ExDate, "+
                " Dist, Upz, UN, Mouza, Vill, (select VILLAGENAME FROM Village WHERE VILLAGEID IN(SELECT Vill FROM Member)) VillageName, ProvType as provtype,ProvCode as provcode, HHNo, SNo as SNo, "+
                " ifnull(member.HealthID,'') as HealthID, ifnull(NameEng,'') as NameEng, "+
                " ifnull(NameBang,'') as NameBang, ifnull(Rth,'') as Rth, ifnull(HaveNID,'') as HaveNID, ifnull(NID,'') as NID, "+
                " ifnull(NIDStatus,'') as NIDStatus, ifnull(HaveBR,'') as HaveBR, ifnull(BRID,'') as BRID, "+
                " ifnull(BRIDStatus,'') as BRIDStatus, ifnull(MobileNo1,'') as MobileNo1, "+
                " ifnull(MobileNo2,'') as MobileNo2,ifnull(MobileYN,'')as MobileYN,strftime('%d/%m/%Y', date(ifnull(DOB,''))) AS DOB, "+
                " Cast(((julianday(date('now'))-julianday(DOB))/365.25) as int) as Age, "+
                " Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) as AgeM, ifnull(DOBSource,'') as DOBSource, "+
                " ifnull(BPlace,'') as BPlace, ifnull(FNo,'') as FNo, ifnull(MNo,'') as MNo, ifnull(Sex,'') as Sex, ifnull(MS,'') as MS, ifnull(SPNO1,'') as SPNO1,ifnull(SPNO2,'') as SPNO2, "+
                " ifnull(SPNO3,'') as SPNO3,ifnull(SPNO4,'') as SPNO4, ifnull(ELCONo,'') as ELCONo, "+
                " ifnull(ELCODontKnow,'') as ELCODontKnow, ifnull(EDU,'') as EDU, ifnull(Rel,'') as Rel, "+
                " ifnull(Nationality,'') as Nationality, ifnull(OCP,'') as OCP, "+
                " ifnull(ExType,'')as ExType "+
                " from Member "+
                " LEFT JOIN epiMasterWoman epimaster ON epimaster.healthId = Member.healthid "+
                " WHERE Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) >=15 "+
                " & Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) <= 49 and Member.Sex=2";*/

                SQLStr = "SELECT ifnull ( member . HealthID , '' ) AS HealthID , epimaster . regNo AS regNo , \n" +
                        "ifnull(e.elcoNo,'')  as elcoNo,  ( CASE WHEN epimaster . regNo IS NOT NULL THEN '2' ELSE '1' END ) AS regC , \n" +
                        "strftime ( '%d/%m/%Y' , date ( epimaster . regDate ) ) AS regDate ,\n" +
                        "ifnull ( NameEng , '' ) AS NameEng , ifnull ( NameBang , '' ) AS NameBang , \n" +
                        "ifnull ( Sex , '' ) AS Sex , strftime ( '%d/%m/%Y' , date ( ifnull ( DOB , '' ) ) ) AS DOB , \n" +
                        "( SELECT CASE \n" +
                        "WHEN CAST ( E.MNo AS int ) = 55 THEN ifnull ( E.Mother , '' ) \n" +
                        "WHEN CAST ( E.MNo AS int ) = 77 THEN ifnull ( E.Mother , '' ) \n" +
                        "WHEN CAST ( E.MNo AS int ) = 88 THEN ifnull ( E.Mother , '' ) \n" +
                        "ELSE ( SELECT NameEng FROM member A\n" +
                        "WHERE A.ProvCode = ( SELECT B.ProvCode FROM member B WHERE B.healthid = member . HealthID ) \n" +
                        "AND A.HHNo = ( SELECT C.HHNo FROM member C WHERE C.healthid = member . HealthID ) \n" +
                        "AND A.SNo = ( SELECT D.MNo FROM member  D WHERE D.HealthID = member . HealthID ) ) \n" +
                        "END AS Mother FROM member E WHERE E.healthId = member . HealthID ) AS MotherName,\n" +
                        "(SELECT \n" +
                        "CASE WHEN cast(E.FNo as int) = 55 THEN ifnull(E.Father,'') \n" +
                        "WHEN cast(E.FNo as int) = 77 THEN ifnull(E.Father,'')\n" +
                        "WHEN cast(E.FNo as int) = 88 THEN ifnull(E.Father,'') \n" +
                        "ELSE (select  A.NameEng  from member A\n" +
                        "where  A.ProvCode=(select  B.ProvCode  from member B Where  B.healthid =member.HealthID) \n" +
                        "and HHNo=(select  HHNo  from member  C Where  C.healthid  =member.HealthID) and \n" +
                        "SNo=(select  FNo  from member D Where  D.healthid = member.HealthID)) END AS  Father \n" +
                        "FROM member E WHERE E.healthId =member.HealthID) AS FatherName, \n" +
                        "( SELECT VILLAGENAME FROM Village WHERE VILLAGEID IN ( SELECT Vill FROM Member ) ) VillageName ,\n" +
                        "ifnull ( MobileNo1 , '' ) AS MobileNo1 , ifnull ( MobileNo2 , '' ) AS MobileNo2 ,\n" +
                        "epimaster . houseNo AS houseNo ,\n" +
                        "\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 17) AS tt1,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 18) AS tt2,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 19) AS tt3,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 20) AS tt4,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 21) AS tt5\n" +
                        "FROM \n" +
                        "Member \n" +
                        "LEFT JOIN epiMasterWoman epimaster ON epimaster.healthId = Member.healthid\n" +
                        "LEFT JOIN elco e ON e.healthId = Member.healthid    \n" +
                        "WHERE Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int)/12 >=15  \n" +
                        " and Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int)/12 <= 49 \n" +
                        " \n" +
                        "and Member.Sex=2\n" +
                        "\n" +
                        " UNION ALL SELECT ifnull ( clientMap . generatedId , '' ) AS HealthID , e . regNo AS regNo , '' as elcoNo, \n" +
                        "( CASE WHEN e . regNo IS NOT NULL THEN '2' ELSE '1' END ) AS regC , strftime ( '%d/%m/%Y' , date ( e . regDate ) ) AS regDate , \n" +
                        " ifnull ( clientMap . name , '' ) AS NameEng , \n" +
                        " '' AS NameBang , ifnull ( gender , '' ) AS Sex , strftime ( '%d/%m/%Y' , date ( ifnull ( DOB , '' ) ) ) AS DOB , \n" +
                        " clientMap.motherName, clientMap.fatherName,   ( SELECT VILLAGENAME FROM Village \n" +
                        " WHERE VILLAGEID IN ( SELECT Vill FROM Member ) ) VillageName , ifnull ( mobileno , '' ) AS MobileNo1 , '' AS MobileNo2 ,\n" +
                        " e . houseNo AS houseNo ,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 17) AS tt1,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.HealthID AND ih.imuCode = 18) AS tt2,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 19) AS tt3,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 20) AS tt4,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 21) AS tt5\n" +
                        " FROM clientMap \n" +
                        " LEFT JOIN epiMasterWoman e ON e . healthId = clientMap . generatedId \n" +
                        " WHERE Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int)/12 >=15  \n" +
                        " and Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int)/12 <= 49 \n" +
                        " and clientMap.gender=2";

            } else if (spnEPIBlock.getSelectedItemPosition() > 0 & txtSearch.length() == 0) {


               /* SQLStr = "Select ifnull(e.elcoNo,'')  as elcoNo, epimaster.healthId As epiHealthId,epimaster.regNo AS regNo, (case when epimaster.regNo is not null then '2' else '1' end) as regC,strftime('%d/%m/%Y', date(epimaster.regDate)) AS regDate,epimaster.houseNo AS houseNo," +
                        " strftime('%d/%m/%Y', date(ExDate)) AS ExDate, " +
                        " Member.Dist As Dist, Member.Upz as Upz, Member.UN as UN, Member.Mouza as Mouza, Member.Vill as Vill, (select VILLAGENAME FROM Village WHERE VILLAGEID IN(SELECT Vill FROM Member)) VillageName, Member.ProvType as provtype,Member.ProvCode as provcode, Member.HHNo As HHNo, SNo as SNo, " +
                        " ifnull(member.HealthID,'') as HealthID, ifnull(NameEng,'') as NameEng," +
                        " ifnull(NameBang,'') as NameBang, ifnull(Rth,'') as Rth, ifnull(HaveNID,'') as HaveNID, ifnull(NID,'') as NID, " +
                        " ifnull(NIDStatus,'') as NIDStatus, ifnull(HaveBR,'') as HaveBR, ifnull(BRID,'') as BRID," +
                        "  ifnull(BRIDStatus,'') as BRIDStatus, ifnull(MobileNo1,'') as MobileNo1," +
                        " ifnull(MobileNo2,'') as MobileNo2,ifnull(MobileYN,'')as MobileYN, strftime('%d/%m/%Y', date(ifnull(DOB,''))) AS DOB, " +
                        " Cast(((julianday(date('now'))-julianday(DOB))/365.25) as int) as Age," +
                        " Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) as AgeM, ifnull(DOBSource,'') as DOBSource, " +
                        " ifnull(BPlace,'') as BPlace, ifnull(FNo,'') as FNo, ifnull(MNo,'') as MNo, " +
                        " ifnull(Sex,'') as Sex, ifnull(MS,'') as MS, ifnull(SPNO1,'') as SPNO1,ifnull(SPNO2,'') as SPNO2," +
                        " ifnull(SPNO3,'') as SPNO3,ifnull(SPNO4,'') as SPNO4, " +// ifnull(ELCONo,'') as ELCONo,
                        " ifnull(ELCODontKnow,'') as ELCODontKnow, ifnull(EDU,'') as EDU, ifnull(Rel,'') as Rel, " +
                        " ifnull(Nationality,'') as Nationality, ifnull(OCP,'') as OCP," +
                        " ifnull(ExType,'')as ExType " +
                        " from Member " +
                        " LEFT JOIN Household h ON h.Dist= Member.Dist and h.Upz= Member.Upz  and h.UN= Member.UN and h.Mouza= Member.Mouza  and h.Vill= Member.Vill" +
                        " inner join HABlock b on b.BCode=h.subBlock" +
                        " LEFT JOIN epiMasterWoman epimaster ON epimaster.healthId = Member.healthid" +
                        " LEFT JOIN elco e ON e.healthId = Member.healthid "+
                        " WHERE Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) >=15 "+
                        " & Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) <= 49 and h.subBlock='" +EPIBlock+ "' and Member.Sex=2";*/

                SQLStr = "SELECT distinct ifnull ( member . HealthID , '' ) AS HealthID , epimaster . regNo AS regNo , \n" +
                        " ifnull(e.elcoNo,'')  as elcoNo,  ( CASE WHEN epimaster . regNo IS NOT NULL THEN '2' ELSE '1' END ) AS regC , \n" +
                        " strftime ( '%d/%m/%Y' , date ( epimaster . regDate ) ) AS regDate ,\n" +
                        " ifnull ( NameEng , '' ) AS NameEng , ifnull ( NameBang , '' ) AS NameBang , \n" +
                        " ifnull ( Sex , '' ) AS Sex , strftime ( '%d/%m/%Y' , date ( ifnull ( DOB , '' ) ) ) AS DOB , \n" +
                        " ( SELECT CASE \n" +
                        " WHEN CAST ( E.MNo AS int ) = 55 THEN ifnull ( E.Mother , '' ) \n" +
                        " WHEN CAST ( E.MNo AS int ) = 77 THEN ifnull ( E.Mother , '' ) \n" +
                        " WHEN CAST ( E.MNo AS int ) = 88 THEN ifnull ( E.Mother , '' ) \n" +
                        " ELSE ( SELECT NameEng FROM member A\n" +
                        " WHERE A.ProvCode = ( SELECT B.ProvCode FROM member B WHERE B.healthid = member . HealthID ) \n" +
                        " AND A.HHNo = ( SELECT C.HHNo FROM member C WHERE C.healthid = member . HealthID ) \n" +
                        " AND A.SNo = ( SELECT D.MNo FROM member  D WHERE D.HealthID = member . HealthID ) ) \n" +
                        " END AS Mother FROM member E WHERE E.healthId = member . HealthID ) AS MotherName,\n" +
                        " (SELECT \n" +
                        " CASE WHEN cast(E.FNo as int) = 55 THEN ifnull(E.Father,'') \n" +
                        " WHEN cast(E.FNo as int) = 77 THEN ifnull(E.Father,'')\n" +
                        " WHEN cast(E.FNo as int) = 88 THEN ifnull(E.Father,'') \n" +
                        " ELSE (select  A.NameEng  from member A\n" +
                        " where  A.ProvCode=(select  B.ProvCode  from member B Where  B.healthid =member.HealthID) \n" +
                        " and HHNo=(select  HHNo  from member  C Where  C.healthid  =member.HealthID) and \n" +
                        " SNo=(select  FNo  from member D Where  D.healthid = member.HealthID)) END AS  Father \n" +
                        " FROM member E WHERE E.healthId =member.HealthID) AS FatherName, \n" +
                        " ( SELECT VILLAGENAME FROM Village WHERE VILLAGEID IN ( SELECT Vill FROM Member ) ) VillageName ,\n" +
                        " ifnull ( MobileNo1 , '' ) AS MobileNo1 , ifnull ( MobileNo2 , '' ) AS MobileNo2 ,\n" +
                        " epimaster . houseNo AS houseNo ,\n" +
                        "\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 17) AS tt1,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 18) AS tt2,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 19) AS tt3,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 20) AS tt4,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 21) AS tt5\n" +
                        " FROM \n" +
                        " Member \n" +
                        " LEFT JOIN Household h ON h.HHNo = Member.HHNo and h.Dist= Member.Dist and h.Upz= Member.Upz  and h.UN= Member.UN and h.Mouza= Member.Mouza  and h.Vill= Member.Vill\n" +
                        " LEFT join HABlock b on b.BCode=h.subBlock\n" +
                        " LEFT JOIN epiMasterWoman epimaster ON epimaster.healthId = Member.healthid\n" +
                        " LEFT JOIN elco e ON e.healthId = Member.healthid    \n" +
                        " WHERE Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int)/12 >=15  \n" +
                        " and Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int)/12 <= 49 \n" +
                        " and Member.Sex=2 and  h.subBlock='" + EPIBlock + "'" +
                        "\n" +
                        " UNION ALL SELECT ifnull ( clientMap . generatedId , '' ) AS HealthID , e . regNo AS regNo , '' as elcoNo, \n" +
                        " ( CASE WHEN e . regNo IS NOT NULL THEN '2' ELSE '1' END ) AS regC , strftime ( '%d/%m/%Y' , date ( e . regDate ) ) AS regDate , \n" +
                        " ifnull ( clientMap . name , '' ) AS NameEng , \n" +
                        " '' AS NameBang , ifnull ( gender , '' ) AS Sex , strftime ( '%d/%m/%Y' , date ( ifnull ( DOB , '' ) ) ) AS DOB , \n" +
                        " clientMap.motherName, clientMap.fatherName,   ( SELECT VILLAGENAME FROM Village \n" +
                        " WHERE VILLAGEID IN ( SELECT Vill FROM Member ) ) VillageName , ifnull ( mobileno , '' ) AS MobileNo1 , '' AS MobileNo2 ,\n" +
                        " e . houseNo AS houseNo ,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 17) AS tt1,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.HealthID AND ih.imuCode = 18) AS tt2,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 19) AS tt3,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 20) AS tt4,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 21) AS tt5\n" +
                        "\n" +
                        " FROM clientMap \n" +
                        " LEFT JOIN Household h ON h.Dist= clientMap.zillaId and h.Upz= clientMap.upazilaId  and h.UN= clientMap.unionId \n" +
                        " and h.Mouza= clientMap.mouzaId  and h.Vill= clientMap.villageId \n" +
                        " left join HABlock b on b.BCode=h.subBlock\n" +
                        " LEFT JOIN epiMasterWoman e ON e . healthId = clientMap . generatedId \n" +
                        " WHERE Cast(((julianday(date('now'))-julianday(DOB))/30.4)/12 as int)/12 >=15  \n" +
                        " and Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int)/12 <= 49 and clientMap.gender=2 and  h.subBlock='" + EPIBlock + "'";
            } else if (spnEPIBlock.getSelectedItemPosition() > 0 & txtSearch.length() > 0) {
                /*SQLStr = "Select ifnull(e.elcoNo,'')  as elcoNo, epimaster.healthId As epiHealthId,epimaster.regNo AS regNo, (case when epimaster.regNo is not null then '2' else '1' end) as regC,strftime('%d/%m/%Y', date(epimaster.regDate)) AS regDate,epimaster.houseNo AS houseNo," +
                        " strftime('%d/%m/%Y', date(ExDate)) AS ExDate, " +
                        " Member.Dist As Dist, Member.Upz as Upz, Member.UN as UN, Member.Mouza as Mouza, Member.Vill as Vill, (select VILLAGENAME FROM Village WHERE VILLAGEID IN(SELECT Vill FROM Member)) VillageName, Member.ProvType as provtype,Member.ProvCode as provcode, Member.HHNo As HHNo, SNo as SNo, " +
                        " ifnull(member.HealthID,'') as HealthID, ifnull(NameEng,'') as NameEng," +
                        " ifnull(NameBang,'') as NameBang, ifnull(Rth,'') as Rth, ifnull(HaveNID,'') as HaveNID, ifnull(NID,'') as NID, " +
                        " ifnull(NIDStatus,'') as NIDStatus, ifnull(HaveBR,'') as HaveBR, ifnull(BRID,'') as BRID," +
                        "  ifnull(BRIDStatus,'') as BRIDStatus, ifnull(MobileNo1,'') as MobileNo1," +
                        " ifnull(MobileNo2,'') as MobileNo2,ifnull(MobileYN,'')as MobileYN,strftime('%d/%m/%Y', date(ifnull(DOB,''))) AS DOB, " +
                        " Cast(((julianday(date('now'))-julianday(DOB))/365.25) as int) as Age," +
                        " Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) as AgeM, ifnull(DOBSource,'') as DOBSource, " +
                        " ifnull(BPlace,'') as BPlace, ifnull(FNo,'') as FNo, ifnull(MNo,'') as MNo, " +
                        " ifnull(Sex,'') as Sex, ifnull(MS,'') as MS, ifnull(SPNO1,'') as SPNO1,ifnull(SPNO2,'') as SPNO2," +
                        " ifnull(SPNO3,'') as SPNO3,ifnull(SPNO4,'') as SPNO4, " +// ifnull(ELCONo,'') as ELCONo,
                        " ifnull(ELCODontKnow,'') as ELCODontKnow, ifnull(EDU,'') as EDU, ifnull(Rel,'') as Rel, " +
                        " ifnull(Nationality,'') as Nationality, ifnull(OCP,'') as OCP," +
                        " ifnull(ExType,'')as ExType " +
                        " from Member " +
                        " LEFT JOIN Household h ON h.Dist= Member.Dist and h.Upz= Member.Upz  and h.UN= Member.UN and h.Mouza= Member.Mouza  and h.Vill= Member.Vill" +
                        " inner join HABlock b on b.BCode=h.subBlock" +
                        " LEFT JOIN epiMasterWoman epimaster ON epimaster.healthId = Member.healthid" +
                        " LEFT JOIN elco e ON e.healthId = Member.healthid "+
                        " WHERE Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) >=15 "+
                        " & Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) <= 49 and h.subBlock='"+ EPIBlock+"' and member.HealthID='" +healthId+ "' and Member.Sex=2";
*/
                SQLStr = "SELECT distinct ifnull ( member . HealthID , '' ) AS HealthID , epimaster . regNo AS regNo , \n" +
                        "ifnull(e.elcoNo,'')  as elcoNo,  ( CASE WHEN epimaster . regNo IS NOT NULL THEN '2' ELSE '1' END ) AS regC , \n" +
                        "strftime ( '%d/%m/%Y' , date ( epimaster . regDate ) ) AS regDate ,\n" +
                        "ifnull ( NameEng , '' ) AS NameEng , ifnull ( NameBang , '' ) AS NameBang , \n" +
                        "ifnull ( Sex , '' ) AS Sex , strftime ( '%d/%m/%Y' , date ( ifnull ( DOB , '' ) ) ) AS DOB , \n" +
                        "( SELECT CASE \n" +
                        "WHEN CAST ( E.MNo AS int ) = 55 THEN ifnull ( E.Mother , '' ) \n" +
                        "WHEN CAST ( E.MNo AS int ) = 77 THEN ifnull ( E.Mother , '' ) \n" +
                        "WHEN CAST ( E.MNo AS int ) = 88 THEN ifnull ( E.Mother , '' ) \n" +
                        "ELSE ( SELECT NameEng FROM member A\n" +
                        "WHERE A.ProvCode = ( SELECT B.ProvCode FROM member B WHERE B.healthid = member . HealthID ) \n" +
                        "AND A.HHNo = ( SELECT C.HHNo FROM member C WHERE C.healthid = member . HealthID ) \n" +
                        "AND A.SNo = ( SELECT D.MNo FROM member  D WHERE D.HealthID = member . HealthID ) ) \n" +
                        "END AS Mother FROM member E WHERE E.healthId = member . HealthID ) AS MotherName,\n" +
                        "(SELECT \n" +
                        "CASE WHEN cast(E.FNo as int) = 55 THEN ifnull(E.Father,'') \n" +
                        "WHEN cast(E.FNo as int) = 77 THEN ifnull(E.Father,'')\n" +
                        "WHEN cast(E.FNo as int) = 88 THEN ifnull(E.Father,'') \n" +
                        "ELSE (select  A.NameEng  from member A\n" +
                        "where  A.ProvCode=(select  B.ProvCode  from member B Where  B.healthid =member.HealthID) \n" +
                        "and HHNo=(select  HHNo  from member  C Where  C.healthid  =member.HealthID) and \n" +
                        "SNo=(select  FNo  from member D Where  D.healthid = member.HealthID)) END AS  Father \n" +
                        "FROM member E WHERE E.healthId =member.HealthID) AS FatherName, \n" +
                        "( SELECT VILLAGENAME FROM Village WHERE VILLAGEID IN ( SELECT Vill FROM Member ) ) VillageName ,\n" +
                        "ifnull ( MobileNo1 , '' ) AS MobileNo1 , ifnull ( MobileNo2 , '' ) AS MobileNo2 ,\n" +
                        "epimaster . houseNo AS houseNo ,\n" +
                        "\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 17) AS tt1,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 18) AS tt2,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 19) AS tt3,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 20) AS tt4,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 21) AS tt5\n" +
                        "FROM \n" +
                        "Member \n" +
                        "LEFT JOIN Household h ON h.HHNo = Member.HHNo and h.Dist= Member.Dist and h.Upz= Member.Upz  and h.UN= Member.UN and h.Mouza= Member.Mouza  and h.Vill= Member.Vill\n" +
                        "LEFT join HABlock b on b.BCode=h.subBlock\n" +
                        "LEFT JOIN epiMasterWoman epimaster ON epimaster.healthId = Member.healthid\n" +
                        "LEFT JOIN elco e ON e.healthId = Member.healthid    \n" +
                        "WHERE Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int)/12 >=15  \n" +
                        " and Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int)/12 <= 49 \n" +
                        " and Member.Sex=2 \" and h.subBlock='" + EPIBlock + "'  and Member.HealthID= '" + healthId + "'" +
                        "\n" +
                        " UNION ALL SELECT ifnull ( clientMap . generatedId , '' ) AS HealthID , e . regNo AS regNo , '' as elcoNo, \n" +
                        " ( CASE WHEN e . regNo IS NOT NULL THEN '2' ELSE '1' END ) AS regC , strftime ( '%d/%m/%Y' , date ( e . regDate ) ) AS regDate , \n" +
                        " ifnull ( clientMap . name , '' ) AS NameEng , \n" +
                        " '' AS NameBang , ifnull ( gender , '' ) AS Sex , strftime ( '%d/%m/%Y' , date ( ifnull ( DOB , '' ) ) ) AS DOB , \n" +
                        " clientMap.motherName, clientMap.fatherName,   ( SELECT VILLAGENAME FROM Village \n" +
                        " WHERE VILLAGEID IN ( SELECT Vill FROM Member ) ) VillageName , ifnull ( mobileno , '' ) AS MobileNo1 , '' AS MobileNo2 ,\n" +
                        " e . houseNo AS houseNo ,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 17) AS tt1,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.HealthID AND ih.imuCode = 18) AS tt2,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 19) AS tt3,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 20) AS tt4,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 21) AS tt5\n" +
                        " \n" +
                        " FROM clientMap \n" +
                        " LEFT JOIN Household h ON h.Dist= clientMap.zillaId and h.Upz= clientMap.upazilaId  and h.UN= clientMap.unionId \n" +
                        " and h.Mouza= clientMap.mouzaId  and h.Vill= clientMap.villageId \n" +
                        " inner join HABlock b on b.BCode=h.subBlock\n" +
                        " LEFT JOIN epiMasterWoman e ON e . healthId = clientMap . generatedId \n" +
                        " WHERE Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int)/12 >=15  \n" +
                        " and Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int)/12 <= 49 and clientMap.gender=2 and h.subBlock='" + EPIBlock + "'  and clientMap.HealthID= '" + healthId + "'";
            } else if (spnEPIBlock.getSelectedItemPosition() == 0 & txtSearch.length() > 0) {
               /* SQLStr = "Select distinct ifnull(e.elcoNo,'')  as elcoNo, epimaster.healthId As epiHealthId,epimaster.regNo AS regNo, (case when epimaster.regNo is not null then '2' else '1' end) as regC,strftime('%d/%m/%Y', date(epimaster.regDate)) AS regDate,epimaster.houseNo AS houseNo," +
                        " strftime('%d/%m/%Y', date(ExDate)) AS ExDate, " +
                        " Member.Dist As Dist, Member.Upz as Upz, Member.UN as UN, Member.Mouza as Mouza, Member.Vill as Vill, (select VILLAGENAME FROM Village WHERE VILLAGEID IN(SELECT Vill FROM Member)) VillageName, Member.ProvType as provtype,Member.ProvCode as provcode, Member.HHNo As HHNo, SNo as SNo, " +
                        " ifnull(member.HealthID,'') as HealthID, ifnull(NameEng,'') as NameEng," +
                        " ifnull(NameBang,'') as NameBang, ifnull(Rth,'') as Rth, ifnull(HaveNID,'') as HaveNID, ifnull(NID,'') as NID, " +
                        " ifnull(NIDStatus,'') as NIDStatus, ifnull(HaveBR,'') as HaveBR, ifnull(BRID,'') as BRID," +
                        "  ifnull(BRIDStatus,'') as BRIDStatus, ifnull(MobileNo1,'') as MobileNo1," +
                        " ifnull(MobileNo2,'') as MobileNo2,ifnull(MobileYN,'')as MobileYN,strftime('%d/%m/%Y', date(ifnull(DOB,''))) AS DOB, " +
                        " Cast(((julianday(date('now'))-julianday(DOB))/365.25) as int) as Age," +
                        " Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) as AgeM, ifnull(DOBSource,'') as DOBSource, " +
                        " ifnull(BPlace,'') as BPlace, ifnull(FNo,'') as FNo, ifnull(MNo,'') as MNo, " +
                        " ifnull(Sex,'') as Sex, ifnull(MS,'') as MS, ifnull(SPNO1,'') as SPNO1,ifnull(SPNO2,'') as SPNO2," +
                        " ifnull(SPNO3,'') as SPNO3,ifnull(SPNO4,'') as SPNO4, " +// ifnull(ELCONo,'') as ELCONo,
                        " ifnull(ELCODontKnow,'') as ELCODontKnow, ifnull(EDU,'') as EDU, ifnull(Rel,'') as Rel, " +
                        " ifnull(Nationality,'') as Nationality, ifnull(OCP,'') as OCP," +
                        " ifnull(ExType,'')as ExType " +
                        " from Member " +
                        " LEFT JOIN Household h ON h.Dist= Member.Dist and h.Upz= Member.Upz  and h.UN= Member.UN and h.Mouza= Member.Mouza  and h.Vill= Member.Vill" +
                        " inner join HABlock b on b.BCode=h.subBlock" +
                        " LEFT JOIN epiMasterWoman epimaster ON epimaster.healthId = Member.healthid" +
                        " LEFT JOIN elco e ON e.healthId = Member.healthid "+
                        " WHERE Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) >=15 "+
                        " & Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) <= 49 and h.subBlock<>'0' and member.HealthID='" +healthId+ "' and Member.Sex=2";*/

                SQLStr = "SELECT distinct ifnull ( member . HealthID , '' ) AS HealthID , epimaster . regNo AS regNo , \n" +
                        "ifnull(e.elcoNo,'')  as elcoNo,  ( CASE WHEN epimaster . regNo IS NOT NULL THEN '2' ELSE '1' END ) AS regC , \n" +
                        "strftime ( '%d/%m/%Y' , date ( epimaster . regDate ) ) AS regDate ,\n" +
                        "ifnull ( NameEng , '' ) AS NameEng , ifnull ( NameBang , '' ) AS NameBang , \n" +
                        "ifnull ( Sex , '' ) AS Sex , strftime ( '%d/%m/%Y' , date ( ifnull ( DOB , '' ) ) ) AS DOB , \n" +
                        "( SELECT CASE \n" +
                        "WHEN CAST ( E.MNo AS int ) = 55 THEN ifnull ( E.Mother , '' ) \n" +
                        "WHEN CAST ( E.MNo AS int ) = 77 THEN ifnull ( E.Mother , '' ) \n" +
                        "WHEN CAST ( E.MNo AS int ) = 88 THEN ifnull ( E.Mother , '' ) \n" +
                        "ELSE ( SELECT NameEng FROM member A\n" +
                        "WHERE A.ProvCode = ( SELECT B.ProvCode FROM member B WHERE B.healthid = member . HealthID ) \n" +
                        "AND A.HHNo = ( SELECT C.HHNo FROM member C WHERE C.healthid = member . HealthID ) \n" +
                        "AND A.SNo = ( SELECT D.MNo FROM member  D WHERE D.HealthID = member . HealthID ) ) \n" +
                        "END AS Mother FROM member E WHERE E.healthId = member . HealthID ) AS MotherName,\n" +
                        "(SELECT \n" +
                        "CASE WHEN cast(E.FNo as int) = 55 THEN ifnull(E.Father,'') \n" +
                        "WHEN cast(E.FNo as int) = 77 THEN ifnull(E.Father,'')\n" +
                        "WHEN cast(E.FNo as int) = 88 THEN ifnull(E.Father,'') \n" +
                        "ELSE (select  A.NameEng  from member A\n" +
                        "where  A.ProvCode=(select  B.ProvCode  from member B Where  B.healthid =member.HealthID) \n" +
                        "and HHNo=(select  HHNo  from member  C Where  C.healthid  =member.HealthID) and \n" +
                        "SNo=(select  FNo  from member D Where  D.healthid = member.HealthID)) END AS  Father \n" +
                        "FROM member E WHERE E.healthId =member.HealthID) AS FatherName, \n" +
                        "( SELECT VILLAGENAME FROM Village WHERE VILLAGEID IN ( SELECT Vill FROM Member ) ) VillageName ,\n" +
                        "ifnull ( MobileNo1 , '' ) AS MobileNo1 , ifnull ( MobileNo2 , '' ) AS MobileNo2 ,\n" +
                        "epimaster . houseNo AS houseNo ,\n" +
                        "\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 17) AS tt1,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 18) AS tt2,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 19) AS tt3,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 20) AS tt4,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 21) AS tt5\n" +
                        "FROM \n" +
                        "Member \n" +
                        "LEFT JOIN Household h ON h.HHNo = Member.HHNo and h.Dist= Member.Dist and h.Upz= Member.Upz  and h.UN= Member.UN and h.Mouza= Member.Mouza  and h.Vill= Member.Vill\n" +
                        "inner join HABlock b on b.BCode=h.subBlock\n" +
                        "LEFT JOIN epiMasterWoman epimaster ON epimaster.healthId = Member.healthid\n" +
                        "LEFT JOIN elco e ON e.healthId = Member.healthid    \n" +
                        "WHERE Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int)/12 >=15  \n" +
                        " and Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int)/12 <= 49 \n" +
                        " and Member.Sex=2 \" and h.subBlock<>'0'  and Member.HealthID= '" + healthId + "'" +
                        "\n" +
                        " UNION ALL SELECT ifnull ( clientMap . generatedId , '' ) AS HealthID , e . regNo AS regNo , '' as elcoNo, \n" +
                        " ( CASE WHEN e . regNo IS NOT NULL THEN '2' ELSE '1' END ) AS regC , strftime ( '%d/%m/%Y' , date ( e . regDate ) ) AS regDate , \n" +
                        " ifnull ( clientMap . name , '' ) AS NameEng , \n" +
                        " '' AS NameBang , ifnull ( gender , '' ) AS Sex , strftime ( '%d/%m/%Y' , date ( ifnull ( DOB , '' ) ) ) AS DOB , \n" +
                        " clientMap.motherName, clientMap.fatherName,   ( SELECT VILLAGENAME FROM Village \n" +
                        " WHERE VILLAGEID IN ( SELECT Vill FROM Member ) ) VillageName , ifnull ( mobileno , '' ) AS MobileNo1 , '' AS MobileNo2 ,\n" +
                        " e . houseNo AS houseNo ,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 17) AS tt1,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.HealthID AND ih.imuCode = 18) AS tt2,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 19) AS tt3,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 20) AS tt4,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 21) AS tt5\n" +
                        " \n" +
                        " FROM clientMap \n" +
                        " LEFT JOIN Household h ON h.Dist= clientMap.zillaId and h.Upz= clientMap.upazilaId  and h.UN= clientMap.unionId \n" +
                        " and h.Mouza= clientMap.mouzaId  and h.Vill= clientMap.villageId \n" +
                        " inner join HABlock b on b.BCode=h.subBlock\n" +
                        " LEFT JOIN epiMasterWoman e ON e . healthId = clientMap . generatedId \n" +
                        " WHERE Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int)/12 >=15  \n" +
                        " and Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int)/12 <= 49 and clientMap.gender=2 and h.subBlock<>'0'  and clientMap.HealthID= '" + healthId + "'";

            }


            Cursor cur1 = C.ReadData(SQLStr);

            cur1.moveToFirst();
            mylist.clear();


            int i = 0;
            while (!cur1.isAfterLast()) {
                HashMap<String, String> map = new HashMap<String, String>();


                String HealthId = cur1.getString(cur1.getColumnIndex("HealthID"));

                map.put("HealthId", cur1.getString(cur1.getColumnIndex("HealthID")));
                map.put("regNo", cur1.getString(cur1.getColumnIndex("regNo")));
                map.put("elcoNo", cur1.getString(cur1.getColumnIndex("elcoNo")));
                map.put("regc", cur1.getString(cur1.getColumnIndex("regC")));
                map.put("regDate", cur1.getString(cur1.getColumnIndex("regDate")));
                map.put("nameeng", cur1.getString(cur1.getColumnIndex("NameEng")));
                map.put("sex", cur1.getString(cur1.getColumnIndex("Sex")));
                map.put("dob", cur1.getString(cur1.getColumnIndex("DOB")));
                map.put("mother", cur1.getString(cur1.getColumnIndex("MotherName")));
                map.put("father", cur1.getString(cur1.getColumnIndex("FatherName")));
                map.put("guardian", "");
                map.put("vill", cur1.getString(cur1.getColumnIndex("VillageName")));
                map.put("mobileno1", cur1.getString(cur1.getColumnIndex("MobileNo1")));
                map.put("mobileno2", cur1.getString(cur1.getColumnIndex("MobileNo2")));
                map.put("houseNo", cur1.getString(cur1.getColumnIndex("houseNo")));


                //Mapping Immunization

                map.put("tt1", cur1.getString(cur1.getColumnIndex("tt1")));
                map.put("tt2", cur1.getString(cur1.getColumnIndex("tt2")));
                map.put("tt3", cur1.getString(cur1.getColumnIndex("tt3")));
                map.put("tt4", cur1.getString(cur1.getColumnIndex("tt4")));
                map.put("tt5", cur1.getString(cur1.getColumnIndex("tt5")));


                mylist.add(map);
                mSchedule = new SimpleAdapter(EpiListWoman.this, mylist, R.layout.epilistrowwoman, new String[]{"HealthID"},
                        new int[]{R.id.HealthID});
                list.setAdapter(new MemberListAdapter(this));

                i += 1;
                cur1.moveToNext();
                totalCount += 1;

            }
            cur1.close();


        } catch (Exception e) {
            Connection.MessageBox(EpiListWoman.this, e.getMessage());
        }
    }


    public class MemberListAdapter extends BaseAdapter {
        private Context context;

        public MemberListAdapter(Context c) {
            context = c;
        }

        public int getCount() {
            return mSchedule.getCount();
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
                convertView = inflater.inflate(R.layout.epilistrowwoman, null);
            }

            try {

                final TableLayout memtab = (TableLayout) convertView.findViewById(R.id.memtab);
                TableRow memtabrow = (TableRow) convertView.findViewById(R.id.memtabrow);
                TextView HealthID = (TextView) convertView.findViewById(R.id.HealthID);
                TextView name = (TextView) convertView.findViewById(R.id.name);
                TextView elcono = (TextView) convertView.findViewById(R.id.elcono);
                TextView dob = (TextView) convertView.findViewById(R.id.dob);
                TextView mname = (TextView) convertView.findViewById(R.id.mname);
                TextView fname = (TextView) convertView.findViewById(R.id.fname);
                TextView regno = (TextView) convertView.findViewById(R.id.regno);
                TextView regdate = (TextView) convertView.findViewById(R.id.regdate);
                TextView oname = (TextView) convertView.findViewById(R.id.oname);
                TextView vill = (TextView) convertView.findViewById(R.id.vill);
                TextView mobile = (TextView) convertView.findViewById(R.id.mobile);


                TextView bari = (TextView) convertView.findViewById(R.id.bari);
                TextView tt1 = (TextView) convertView.findViewById(R.id.tt1);
                TextView tt2 = (TextView) convertView.findViewById(R.id.tt2);
                TextView tt3 = (TextView) convertView.findViewById(R.id.tt3);
                TextView tt4 = (TextView) convertView.findViewById(R.id.tt4);
                TextView tt5 = (TextView) convertView.findViewById(R.id.tt5);


                TextView remarks = (TextView) convertView.findViewById(R.id.remarks);


                final HashMap<String, String> o = (HashMap<String, String>) mSchedule.getItem(position);


                HealthID.setText(o.get("HealthId"));
                regno.setText(o.get("regNo"));
                regdate.setText(o.get("regDate"));
                name.setText(o.get("nameeng"));
                if (o.get("elcoNo").length() > 0)
                    elcono.setText(o.get("elcoNo"));
                else
                    elcono.setText("");

                dob.setText(o.get("dob"));
                mname.setText(o.get("mother"));
                fname.setText(o.get("father"));
                oname.setText("");
                vill.setText(o.get("vill"));
                mobile.setText(o.get("mobileno1"));
                bari.setText(o.get("houseNo"));
                tt1.setText(o.get("tt1"));
                tt2.setText(o.get("tt2"));
                tt3.setText(o.get("tt3"));
                tt4.setText(o.get("tt4"));
                tt5.setText(o.get("tt5"));

                if (o.get("regc").equals("2")) {
                    memtabrow.setBackgroundColor(Color.parseColor("#99cc33"));
                }

                if (o.get("regc").equals("1")) {
                    memtabrow.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }


                //Integer agemonth = Integer.valueOf(o.get("agem"));
                remarks.setText("");


                memtab.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        g.setHealthID(o.get("HealthId"));
                        g.setepiName(o.get("nameeng"));
                        g.setepimName(o.get("mother"));
                        g.setepifName(o.get("father"));
                        g.setDOB(o.get("dob"));
                        g.setELCONo(o.get("elcoNo"));
                        DisplayEpiReg();


                    }
                });

            } catch (Exception ex) {

            }
            return convertView;
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

    // Method to turn on GPS
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

    // Method to turn off the GPS
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

    // turning off the GPS if its in on state. to avoid the battery drain.
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        turnGPSOff();
    }

    protected Dialog onCreateDialog(int id) {
        final Calendar c = Calendar.getInstance();
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mDateSetListener, g.mYear, g.mMonth - 1, g.mDay);
            // case DATE_DIALOG1:
            //   return new DatePickerDialog(this, mDateSetListener1,g.mYear,g.mMonth-1,g.mDay);


        }
        return null;
    }


   /* private DatePickerDialog.OnDateSetListener mDateSetListener1 = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year; mMonth = monthOfYear+1; mDay = dayOfMonth;

            TextView dtpDate;


            dtpDate = (TextView) findViewById(R.id.txtdate);


            if (VariableID.equals("btndate")) {
                dtpDate = (TextView) findViewById(R.id.txtdate);

                dtpDate.setText(new StringBuilder()
                        .append(Global.Right("00" + mDay, 2)).append("/")
                        .append(Global.Right("00" + mMonth, 2)).append("/")
                        .append(mYear));
            }

            if (VariableID.equals("btndate")) {

                ((TextView)findViewById(R.id.txtbar)).setText(g.getDay(dtpDate.getText().toString()));
            }
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = sdf.format(c.getTime());
                Date date1 = sdf.parse(formattedDate);

                Date date2 = sdf.parse(dtpDate.getText().toString());

                if (date2.after(date1))
                {

                    if (VariableID.equals("txtdate"))
                    {
                        Connection.MessageBox(EpiList.this, "তারিখ আজকের তারিখের সমান অথবা কম হতে হবে।");
                        return;
                    }

                }


            }
            catch(ParseException ex)
            {
                ex.printStackTrace();
            }

        }
    };*/

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear + 1;
            mDay = dayOfMonth;


            VisitDate.setText(new StringBuilder()
                    .append(Global.Right("00" + mDay, 2)).append("/")
                    .append(Global.Right("00" + mMonth, 2)).append("/")
                    .append(mYear).append(" "));

        }
    };
    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
            hour = selectedHour;
            minute = selectedMinute;
            EditText tpTime;

            /*tpTime = (EditText) findViewById(R.id.txtBcgtime);

            if (VariableID.equals("btnBcgtime") || VariableID.equals("btnMrtime") || VariableID.equals("btnHumtime")) {
                tpTime = (EditText) findViewById(R.id.txtBcgtime);
            }
            if (VariableID.equals("btnMrtime")) {
                tpTime = (EditText) findViewById(R.id.txtMrtime);
            }
            if (VariableID.equals("btnHumtime")) {
                tpTime = (EditText) findViewById(R.id.txtHumtime);
            }

            String am_pm = (hour < 12) ? "AM" : "PM";
            tpTime.setText(new StringBuilder()
                    .append(Global.Right("00" + hour, 2)).append(":")
                    .append(Global.Right("00" + minute, 2)).append(" ")
                    .append(am_pm));*/

        }
    };
}