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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import Common.Connection;
import Common.Global;

public class WomanImmunizationnew extends Activity {

    String VariableID;
    public String dateSet = "";
    private int hour;
    private int minute;
    private int mDay;
    private int mMonth;
    private int mYear;
    static final int DATE_DIALOG = 1;
    static final int TIME_DIALOG = 2;
    Calendar c = Calendar.getInstance();

    SimpleAdapter mAdapter;
    SimpleAdapter eList;

    ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();

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
    private static String vdate;

    TextView txtHealthId;
    RadioGroup rdogrpPAddr;
    RadioButton rdoPAddr1;
    RadioButton rdoPAddr2;
    EditText txtPermaAddress;
    Spinner spnReligion;

    RadioGroup rdogrpVGFCard;
    RadioButton rdoVGFCard1;
    RadioButton rdoVGFCard2;

    LinearLayout secPAddr;
    LinearLayout secPermaAddress;
    LinearLayout secReligion;
    String StartTime;

    TextView FPMethod;
    TextView LMP;
    TextView EDD;
    String block, imuDate, bcgtime, mrtime, humtime;

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mnuclose, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder adb = new AlertDialog.Builder(WomanImmunizationnew.this);
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
            setContentView(R.layout.childimmunizationnew);
            C = new Connection(this);
            g = Global.getInstance();

            StartTime = g.CurrentTime24();
            con = this;
            ((EditText) findViewById(R.id.txtRegNo)).requestFocus();
            IDbundle = getIntent().getExtras();

            if (IDbundle != null) {
               /* if (IDbundle.getString("search").equalsIgnoreCase("search")) {

                } else {*/


                block = IDbundle.getString("block");
                imuDate = IDbundle.getString("imuDate");
                bcgtime = IDbundle.getString("bcgtime");
                mrtime = IDbundle.getString("mrtime");
                humtime = IDbundle.getString("humtime");
                LoadData();
                //}
            }
            /*else {
                block = IDbundle.getString("block");
                imuDate = IDbundle.getString("imuDate");
                bcgtime =  IDbundle.getString("bcgtime");
                mrtime = IDbundle.getString("mrtime");
                humtime = IDbundle.getString("humtime");
               LoadData();
            }*/
        } catch (Exception ex) {
            Connection.MessageBox(WomanImmunizationnew.this, ex.getMessage());
            return;
        }
    }

    private void LoadData() {
        try {

            // FindLocation();


            ((ImageButton) findViewById(R.id.btnRegDT)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btnRegDT";
                    showDialog(DATE_DIALOG);
                }
            });

            ((ImageButton) findViewById(R.id.btnBR)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btnBR";
                    showDialog(DATE_DIALOG);
                }
            });


            ((Button) findViewById(R.id.cmdSaveEpi)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String sql = "select * from epiMaster WHERE  HealthId = '" + g.getHealthID() + "'";
                    if (!C.Existence(sql)) {

                        sql = "Insert Into epiMaster(healthId , providerId , regNo , regDate , houseNo ,brDate,  brCertificateNo , remarks , systemEntryDate ,  upload,  notpossible,  block, imuDate, bcgtime, mrtime, humtime)";
                        sql = sql + " VALUES ('" + txtHealthId.getText().toString() + "', '" + g.getProvCode() + "','" + ((TextView) findViewById(R.id.txtRegNo)).getText().toString() + "','" +
                                ((TextView) findViewById(R.id.txtRegDate)).getText().toString() + "','" + ((TextView) findViewById(R.id.txtHouse)).getText().toString() + "','" + ((TextView) findViewById(R.id.txtBR)).getText().toString() + "','" +
                                ((EditText) findViewById(R.id.txtcert)).getText().toString() + "','" + ((EditText) findViewById(R.id.txtremarks)).getText().toString() + "','" + Global.DateTimeNowYMDHMS() + "','2', '" + ((EditText) findViewById(R.id.txtEPICenterName)).getText().toString() +
                                "','" + ((EditText) findViewById(R.id.txtpossible)).getText().toString() + "','" + block + "','" + imuDate + "','" + bcgtime + "','" + mrtime + "','" + humtime + "')";


                        C.Save(sql);
                        Connection.MessageBox(WomanImmunizationnew.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
                    } else {
                        sql = " UPDATE epiMaster SET regNo = '" + ((TextView) findViewById(R.id.txtRegNo)).getText().toString() + "', regDate = '" +
                                ((TextView) findViewById(R.id.txtRegDate)).getText().toString() + "' , houseNo = '" + ((TextView) findViewById(R.id.txtHouse)).getText().toString() + "', brCertificateNo = '" +
                                ((EditText) findViewById(R.id.txtcert)).getText().toString() + "', remarks = '" + ((EditText) findViewById(R.id.txtremarks)).getText().toString() + "', modifyDate ='" +
                                Global.DateTimeNowYMDHMS() + "', upload = '2', centername = '" + ((EditText) findViewById(R.id.txtEPICenterName)).getText().toString() + "', notpossible = '" +
                                ((EditText) findViewById(R.id.txtpossible)).getText().toString() + ", brDate = '" + ((TextView) findViewById(R.id.txtBR)).getText().toString() + "' WHERE HealthId = '" + g.getHealthID() + "'";
                        C.Save(sql);
                        Connection.MessageBox(WomanImmunizationnew.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");

                    }
                }
            });
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Activity.KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
            lock.disableKeyguard();


            txtHealthId = (TextView) findViewById(R.id.txtHealthID);

            txtHealthId.setText(g.getHealthID());

            //DataSearch();
            ((ImageButton) findViewById(R.id.btnShow)).setVisibility(View.GONE);
            ((ImageButton) findViewById(R.id.btnHide)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.seclayouttoshowhide)).setVisibility(View.GONE);
            ((ImageButton) findViewById(R.id.btnHide)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ((LinearLayout) findViewById(R.id.seclayouttoshowhide)).setVisibility(View.VISIBLE);
                    ((ImageButton) findViewById(R.id.btnShow)).setVisibility(View.VISIBLE);
                    ((ImageButton) findViewById(R.id.btnHide)).setVisibility(View.GONE);
                }
            });
            ((ImageButton) findViewById(R.id.btnShow)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ((LinearLayout) findViewById(R.id.seclayouttoshowhide)).setVisibility(View.GONE);
                    ((ImageButton) findViewById(R.id.btnHide)).setVisibility(View.VISIBLE);
                    ((ImageButton) findViewById(R.id.btnShow)).setVisibility(View.GONE);
                }
            });
        } catch (Exception ex) {
            Connection.MessageBox(WomanImmunizationnew.this, ex.getMessage());
            return;
        }
    }

    private void DataSave() {
        ListView list = (ListView) findViewById(R.id.lstimu);
        Integer mCount = list.getCount();
        String sql = "";
        try {

            for (int i = 0; i < mCount; i++) {
                View curr = mAdapter.getView(i, null, null);

                TextView txtImuCode = (TextView) curr.findViewById(R.id.imuCode);
                TextView txtImudose = (TextView) curr.findViewById(R.id.numOfDose);
                TextView txtdateGiven = (TextView) curr.findViewById(R.id.dategiven);
                if (txtImuCode != null) {
                    sql = "INSERT INTO childImmunization (healthId, providerId, slNo, regDate, houseNo, imuCode, imuDate, systemEntryDate)";
                    sql = sql + "VALUES ('" + txtHealthId.getText().toString() + "', '" + g.getProvCode() + "','" + ((TextView) findViewById(R.id.txtSNo)).getText().toString() + "','" +
                            ((TextView) findViewById(R.id.txtRegDate)).getText().toString() + "','" + ((TextView) findViewById(R.id.txtHouse)).getText().toString() + "','" +
                            String.valueOf(txtImuCode.getText()) + "','" + String.valueOf(txtdateGiven.getText()) + "','" + Global.DateTimeNowYMDHMS() + "')";
                    C.Save(sql);
                }
            }
        } catch (Exception ex) {
            Connection.MessageBox(WomanImmunizationnew.this, ex.getMessage());
            return;
        }


    }

    private void DataSearch() {
        //  DataRetrieve();
        // String date = ((TextView)findViewById(R.id.txtDOB)).getText().toString();
        // ((TextView)findViewById(R.id.txtMotherVitA)).setText(Global.addDays(date, 42));
        //  ((TextView)findViewById(R.id.txtPenta)).setText(Global.addDays(date, 42));
        //((TextView)findViewById(R.id.txtHum)).setText(Global.addDays(date, 70));
    }

    private void getImmunizationHeader(String HealthId) {
        String sql = "select regNo,regDate,houseNo,brCertificateNo, brDate, remarks, notpossible FROM epiMaster Where HealthId = '" + HealthId + "'";

        Cursor cur1 = C.ReadData(sql);

        cur1.moveToFirst();
        while (!cur1.isAfterLast()) {
            ((TextView) findViewById(R.id.txtRegNo)).setText(cur1.getString(cur1.getColumnIndex("regNo")));
            ((TextView) findViewById(R.id.txtRegDate)).setText(cur1.getString(cur1.getColumnIndex("regDate")));

            ((TextView) findViewById(R.id.txtHouse)).setText(cur1.getString(cur1.getColumnIndex("houseNo")));
            ((EditText) findViewById(R.id.txtcert)).setText(cur1.getString(cur1.getColumnIndex("brCertificateNo")));
            ((EditText) findViewById(R.id.txtBR)).setText(cur1.getString(cur1.getColumnIndex("brDate")));
            ((EditText) findViewById(R.id.txtremarks)).setText(cur1.getString(cur1.getColumnIndex("remarks")));
            //((EditText)findViewById(R.id.txtEPICenterName)).setText(cur1.getString(cur1.getColumnIndex("centername")));
            ((EditText) findViewById(R.id.txtpossible)).setText(cur1.getString(cur1.getColumnIndex("notpossible")));
            cur1.moveToNext();

        }
        cur1.close();
    }

    //
    //Retrieve
    //***************************************************************************************************************************
    public void DataRetrieve() {
        //getImmunizationHeader(g.getHealthID());
        try {
            String SQL = "select M.NameEng as NameEng, M.Sex as Sex, ifnull(MobileNo1,'') as MobileNo1,ifnull(MobileNo2,'') as MobileNo2," +
                    " ifnull(DOB,'') as DOB,D.ZillaName as zillaname, u.UPAZILANAME as upazilaname, " +
                    " uu.UNIONNAME as unionname, v.VILLAGENAME as villagename from Member M" +
                    " INNER JOIN Zilla D ON M.Dist = D.Zillaid" +
                    " inner join upazila u on u.upazilaid = M.Upz" +
                    " inner join Unions uu on uu.UNIONID = M.UN" +
                    " inner join Village v on v.VILLAGEID = M.Vill " +
                    " WHERE v.MOUZAID = M.Mouza and HealthId = '" + g.getHealthID() + "'";


            Cursor cur1 = C.ReadData(SQL);

            cur1.moveToFirst();
            while (!cur1.isAfterLast()) {
                ((TextView) findViewById(R.id.txtName)).setText(cur1.getString(cur1.getColumnIndex("NameEng")));

                if (cur1.getString(cur1.getColumnIndex("Sex")).equals("1"))
                    ((TextView) findViewById(R.id.txtSex)).setText("ছেলে");
                else if (cur1.getString(cur1.getColumnIndex("Sex")).equals("2"))
                    ((TextView) findViewById(R.id.txtSex)).setText("মেয়ে");


                ((TextView) findViewById(R.id.txtDOB)).setText(Global.DateConvertDMY(cur1.getString(cur1.getColumnIndex("DOB"))));
                ((TextView) findViewById(R.id.txtFName)).setText(Global.GetFatherName(C, g.getHealthID()));
                ((TextView) findViewById(R.id.txtMName)).setText(Global.GetMotherName(C, g.getHealthID()));

                if (cur1.getString(cur1.getColumnIndex("MobileNo1")).length() <= 0)
                    ((TextView) findViewById(R.id.txtMNo)).setText(cur1.getString(cur1.getColumnIndex("MobileNo2")));
                else
                    ((TextView) findViewById(R.id.txtMNo)).setText(cur1.getString(cur1.getColumnIndex("MobileNo1")));


                ((TextView) findViewById(R.id.txtvillage)).setText(cur1.getString(cur1.getColumnIndex("villagename")));
                ((TextView) findViewById(R.id.txtunion)).setText(cur1.getString(cur1.getColumnIndex("unionname")));
                ((TextView) findViewById(R.id.txtupazilla)).setText(cur1.getString(cur1.getColumnIndex("upazilaname")));
                ((TextView) findViewById(R.id.txtzilla)).setText(cur1.getString(cur1.getColumnIndex("zillaname")));

                cur1.moveToNext();

            }
            cur1.close();
            RefreshGrid(true);


        } catch (Exception e) {
            Connection.MessageBox(WomanImmunizationnew.this, e.getMessage());
        }
    }

    private void RefreshGrid(Boolean heading) {


        String sqlString = "select A.imuCode AS imuCode, A.imuName AS imuName, A.numOfDose AS numOfDose, B.imuDate AS imuDate FROM immunization A" +
                " LEFT JOIN immunizationHistory B ON A.imuCode=B.imuCode and B.HealthId = '" + g.getHealthID() + "' where A.forChild='1'";


        Cursor cur = C.ReadData(sqlString);

        cur.moveToFirst();

        int i = 0;
        String previousImmunizationName = "";
        while (!cur.isAfterLast()) {
            HashMap<String, String> map = new HashMap<String, String>();

       /* if(i==0)
          {
                View header = getLayoutInflater().inflate(R.layout.childimulistheading, null);
                list.addHeaderView(header);
          }*/

            map.put("imuCode", cur.getString(cur.getColumnIndex("imuCode")));
            Integer imuCode = Integer.parseInt(cur.getString(cur.getColumnIndex("imuCode")));

            switch (imuCode) {

                case 1:
                    map.put("datetobegiven", Global.addDays(((TextView) findViewById(R.id.txtDOB)).getText().toString(), 42));
                    break;
                case 2:
                    map.put("datetobegiven", Global.addDays(((TextView) findViewById(R.id.txtDOB)).getText().toString(), 42));
                    break;
                case 3:
                    map.put("datetobegiven", Global.addDays(((TextView) findViewById(R.id.txtDOB)).getText().toString(), 70));
                    break;
                case 4:
                    map.put("datetobegiven", Global.addDays(((TextView) findViewById(R.id.txtDOB)).getText().toString(), 98));
                    break;
                case 5:
                    map.put("datetobegiven", Global.addDays(((TextView) findViewById(R.id.txtDOB)).getText().toString(), 42));
                    break;
                case 6:
                    map.put("datetobegiven", Global.addDays(((TextView) findViewById(R.id.txtDOB)).getText().toString(), 70));
                    break;
                case 7:
                    map.put("datetobegiven", Global.addDays(((TextView) findViewById(R.id.txtDOB)).getText().toString(), 126));
                    break;
                case 8:
                    map.put("datetobegiven", Global.addDays(((TextView) findViewById(R.id.txtDOB)).getText().toString(), 14));
                    break;
                case 9:
                    map.put("datetobegiven", Global.addDays(((TextView) findViewById(R.id.txtDOB)).getText().toString(), 42));
                    break;
                case 10:
                    map.put("datetobegiven", Global.addDays(((TextView) findViewById(R.id.txtDOB)).getText().toString(), 70));
                    break;
                case 11:
                    map.put("datetobegiven", Global.addDays(((TextView) findViewById(R.id.txtDOB)).getText().toString(), 98));
                    break;
                case 12:
                    map.put("datetobegiven", Global.addDays(((TextView) findViewById(R.id.txtDOB)).getText().toString(), 98));
                    break;
                case 13:
                    map.put("datetobegiven", Global.addDays(((TextView) findViewById(R.id.txtDOB)).getText().toString(), 63));
                    break;
                case 14:
                    map.put("datetobegiven", Global.addDays(((TextView) findViewById(R.id.txtDOB)).getText().toString(), 105));
                    break;
                case 15:
                    map.put("datetobegiven", Global.addDays(((TextView) findViewById(R.id.txtDOB)).getText().toString(), 42));
                    break;
                case 16:
                    map.put("datetobegiven", Global.addDays(((TextView) findViewById(R.id.txtDOB)).getText().toString(), 84));
                    break;

            }

            map.put("imuName", cur.getString(cur.getColumnIndex("imuName")));
            map.put("numOfDose", cur.getString(cur.getColumnIndex("numOfDose")));
            map.put("dategiven", cur.getString(cur.getColumnIndex("imuDate")));

            mylist.add(map);

        /*mAdapter = new SimpleAdapter(ChildImmunization.this, mylist, R.layout.childimulistrow,new String[] {"imuCode"},
                new int[] {R.id.imuCode});*/
            //list.setAdapter(new ChildImuListAdapter(this));


            i += 1;
            cur.moveToNext();
        }
        cur.close();
    }

    String previousImuName = "";

    public class ChildImuListAdapter extends BaseAdapter {
        private Context context;


        public void setDateSet(String dSet) {
            dateSet = dSet;
        }

        public ChildImuListAdapter(Context c) {
            context = c;
        }

        public int getCount() {
            return mAdapter.getCount();
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
                convertView = inflater.inflate(R.layout.childimulistrow, parent, false);
            }

            try {

                TextView imuCode = (TextView) convertView.findViewById(R.id.imuCode);
                TextView imuName = (TextView) convertView.findViewById(R.id.imuName);
               /* Button firsttime = (Button) convertView.findViewById(R.id.firsttime);
                Button secondtime = (Button) convertView.findViewById(R.id.secondtime);
                Button thirdtime = (Button) convertView.findViewById(R.id.thirdtime);
                Button fourthtime = (Button) convertView.findViewById(R.id.fourthtime);
                Button fifthtime = (Button) convertView.findViewById(R.id.fifthtime);
                Button sixthtime = (Button) convertView.findViewById(R.id.sixthtime);*/
             /*   TextView datetobegiven = (TextView) convertView.findViewById(R.id.datetobegiven);
                TextView dategiven = (TextView) convertView.findViewById(R.id.dategiven);*/

                ImageButton btndategiven = (ImageButton) convertView.findViewById(R.id.btndate);


                final HashMap<String, String> o = (HashMap<String, String>) mAdapter.getItem(position);
                imuCode.setText(o.get("imuCode"));

              /*  if(previousImuName.equalsIgnoreCase(o.get("imuName")))
                {
                    imuName.setText("");
                }
                else {*/
                imuName.setText(o.get("imuName"));
                   /* previousImuName = o.get("imuName");
                }*/


                /*numOfDose.setText(o.get("numOfDose"));
                datetobegiven.setText(o.get("datetobegiven"));*/
           /*     makeBackColorWhite(firsttime);
                makeBackColorWhite(secondtime);
                makeBackColorWhite(thirdtime);
                makeBackColorWhite(fourthtime);
                makeBackColorWhite(fifthtime);
                makeBackColorWhite(sixthtime);*/

                /*if(o.get("imuCode").equalsIgnoreCase("1"))
                {
                    if(o.get("dategiven")!=null)
                        firsttime.setText(o.get("dategiven"));
                    else
                        firsttime.setText("...");

                    makeBackColorWhite(firsttime);
                    makeBackColorGrey(secondtime);
                    makeBackColorGrey(thirdtime);
                    makeBackColorGrey(fourthtime);
                    makeBackColorGrey(fifthtime);
                    makeBackColorGrey(sixthtime);

                }
                else if(o.get("imuCode").equalsIgnoreCase("2"))
                {
                    if(o.get("dategiven")!=null)
                        firsttime.setText(o.get("dategiven"));
                    else
                        firsttime.setText("...");

                    makeBackColorWhite(firsttime);
                    makeBackColorWhite(secondtime);
                    makeBackColorWhite(thirdtime);
                    makeBackColorGrey(fourthtime);
                    makeBackColorGrey(fifthtime);
                    makeBackColorGrey(sixthtime);

                }

                else if(o.get("imuCode").equalsIgnoreCase("3"))
                {

                    if(o.get("dategiven")!=null)
                        secondtime.setText(o.get("dategiven"));
                    else
                        secondtime.setText("...");
                    makeBackColorWhite(firsttime);
                    makeBackColorWhite(secondtime);
                    makeBackColorWhite(thirdtime);
                    makeBackColorGrey(fourthtime);
                    makeBackColorGrey(fifthtime);
                    makeBackColorGrey(sixthtime);

                }
                else if(o.get("imuCode").equalsIgnoreCase("4"))
                {

                    if(o.get("dategiven")!=null)
                        thirdtime.setText(o.get("dategiven"));
                    else
                        thirdtime.setText("...");

                    makeBackColorWhite(firsttime);
                    makeBackColorWhite(secondtime);
                    makeBackColorGrey(thirdtime);
                    makeBackColorGrey(fourthtime);
                    makeBackColorGrey(fifthtime);
                    makeBackColorGrey(sixthtime);
                }

                else if(o.get("imuCode").equalsIgnoreCase("5"))
                {
                    firsttime.setText(o.get("dategiven"));

                    makeBackColorWhite(firsttime);
                    makeBackColorWhite(secondtime);
                    makeBackColorGrey(thirdtime);
                    makeBackColorWhite(fourthtime);
                    makeBackColorGrey(fifthtime);
                    makeBackColorGrey(sixthtime);
                }

                else if(o.get("imuCode").equalsIgnoreCase("6")) {
                    secondtime.setText(o.get("dategiven"));
                } else if(o.get("imuCode").equalsIgnoreCase("7"))
                {
                    fourthtime.setText(o.get("dategiven"));

                   *//* makeBackColorGrey(thirdtime);

                    makeBackColorGrey(fifthtime);
                    makeBackColorGrey(sixthtime);*//*
                }

//opv
                else if(o.get("imuCode").equalsIgnoreCase("8"))
                {
                    firsttime.setText(o.get("dategiven"));
                }

                else if(o.get("imuCode").equalsIgnoreCase("9"))
                {
                    secondtime.setText(o.get("dategiven"));
                }

                else if(o.get("imuCode").equalsIgnoreCase("10"))
                {
                    thirdtime.setText(o.get("dategiven"));
                }

                else if(o.get("imuCode").equalsIgnoreCase("11"))
                {
                    fourthtime.setText(o.get("dategiven"));
                }
                else if(o.get("imuCode").equalsIgnoreCase("12"))
                {
                    thirdtime.setText(o.get("dategiven"));
                }

                else if(o.get("imuCode").equalsIgnoreCase("13"))
                {
                    fifthtime.setText(o.get("dategiven"));
                }

                else if(o.get("imuCode").equalsIgnoreCase("14"))
                {
                    sixthtime.setText(o.get("dategiven"));
                }*/

                // dategiven.setText(o.get("dategiven"));

                /*((ImageButton) convertView.findViewById(R.id.btndate)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        DiaplayPopup(g.getHealthID(), o.get("imuCode"), o.get("imuName"),o.get("numOfDose"), o.get("datetobegiven"), o.get("dategiven"));
                    }
                });*/
            } catch (Exception ex) {

            }
            return convertView;
        }
    }

    private void makeBackColorGrey(Button tv) {
        tv.setBackgroundColor(Color.GRAY);
    }

    private void makeBackColorWhite(Button tv) {
        tv.setBackgroundColor(Color.WHITE);
    }

    Dialog popupView = null;

    private void DiaplayPopup(String healthId, final String imuCode, String imuName, String dose, String datetobegiven, String dategiven) {
        popupView = new Dialog(WomanImmunizationnew.this);
        popupView.setTitle(imuName);
        popupView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popupView.setContentView(R.layout.datepopup);
        popupView.setCancelable(true);
        popupView.setCanceledOnTouchOutside(false);

        ((TextView) popupView.findViewById(R.id.datetobegiven)).setText(datetobegiven);
        ((TextView) popupView.findViewById(R.id.popupDate)).setText(dategiven);
        ((TextView) popupView.findViewById(R.id.imuDose)).setText(dose);
        ((TextView) popupView.findViewById(R.id.imuName)).setText(imuName);
        Button btnCancel = (Button) popupView.findViewById(R.id.Cancel);
        btnCancel.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                popupView.dismiss();
            }
        });

        Button btnSave = (Button) popupView.findViewById(R.id.Save);
        btnSave.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                String sql = "";
                try {

                    if (String.valueOf(((TextView) popupView.findViewById(R.id.popupDate)).getText().toString()).length() <= 0) {
                        Connection.MessageBox(WomanImmunizationnew.this, "তারিখ লিখুন");
                        return;
                    }

                    if (Global.DateDifferenceDays(String.valueOf(((TextView) popupView.findViewById(R.id.popupDate)).getText().toString()), String.valueOf(((TextView) popupView.findViewById(R.id.datetobegiven)).getText().toString())) > 0) {
                        Connection.MessageBox(WomanImmunizationnew.this, "সঠিক তারিখ লিখুন");
                        return;
                    }
                    if (imuCode != null) {
                        //Add facility to update Header: EpiMaster
                        // and then the following
                        sql = "select * from immunizationHistory WHERE  HealthId = '" + g.getHealthID() + "' AND imuCode = '" + String.valueOf(imuCode) + "'";
                        if (!C.Existence(sql)) {
/*

                                sql = "Insert Into epiMaster(healthId , providerId , regNo , regDate , houseNo , brCertificateNo , remarks , systemEntryDate ,  upload) ";
                                sql = sql + "VALUES ('" + txtHealthId.getText().toString() + "', '" + g.getProvCode() + "','" + ((TextView) findViewById(R.id.txtRegNo)).getText().toString() + "','" +
                                        ((TextView) findViewById(R.id.txtRegDate)).getText().toString() + "','" + ((TextView) findViewById(R.id.txtHouse)).getText().toString() + "','" +
                                        ((TextView) findViewById(R.id.txtcert)).getText().toString() + "','" + ((TextView) findViewById(R.id.txtremarks)).getText().toString() + "','" + Global.DateTimeNowYMDHMS() + "','2'" +")";
*/

                            sql = "INSERT INTO immunizationHistory (healthId, providerId, imuCode, imuDose, imuDate, systemEntryDate, upload)";
                            sql = sql + "VALUES ('" + txtHealthId.getText().toString() + "', '" + g.getProvCode() + "','" +
                                    String.valueOf(imuCode) + "','" + Global.GetImmunizationDose(C, String.valueOf(imuCode)) + "','" + String.valueOf(((TextView) popupView.findViewById(R.id.popupDate)).getText().toString()) + "','" + Global.DateTimeNowYMDHMS() + "','2')";

                            C.Save(sql);
                        } else {
                            sql = "UPDATE immunizationHistory SET imuDate = '" + String.valueOf(((TextView) popupView.findViewById(R.id.popupDate)).getText().toString()) +
                                    "' , upload = '2' , modifyDate = '" + Global.DateTimeNowYMDHMS() + "' WHERE healthId = '" + g.getHealthID() + "' AND imuCode = '" + imuCode + "'";


                            C.Save(sql);
                        }

                        popupView.dismiss();
                        RefreshGrid(false);
                    }

                } catch (Exception ex) {
                    Connection.MessageBox(WomanImmunizationnew.this, ex.getMessage());
                    return;
                }

            }
        });

        ((ImageButton) popupView.findViewById(R.id.btndate)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VariableID = "btndate";
                showDialog(DATE_DIALOG);

            }
        });

        popupView.show();


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
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        Integer Y = g.mYear;
        Integer M = g.mMonth;
        Integer D = g.mDay;

        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mDateSetListener, Y, M - 1, D);
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
            TextView dtpDate;

            dtpDate = (TextView) findViewById(R.id.txtRegDate);

            if (VariableID.equals("btnRegDT")) {
                dtpDate = (TextView) findViewById(R.id.txtRegDate);
            }
            if (VariableID.equals("btndate")) {
                dtpDate = (TextView) popupView.findViewById(R.id.popupDate);
            }

            if (VariableID.equals("btnBR")) {
                dtpDate = (TextView) findViewById(R.id.txtBR);
            }


            dtpDate.setText(new StringBuilder()
                    .append(Global.Right("00" + mDay, 2)).append("/")
                    .append(Global.Right("00" + mMonth, 2)).append("/")
                    .append(mYear));
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = sdf.format(c.getTime());
                Date date1 = sdf.parse(formattedDate);

                Date date2 = sdf.parse(dtpDate.getText().toString());

                if (date2.after(date1)) {

                    if (VariableID.equals("btnRegDT")) {
                        Connection.MessageBox(WomanImmunizationnew.this, "তারিখ আজকের তারিখের সমান অথবা কম হতে হবে।");
                        return;
                    }

                }


            } catch (ParseException ex) {
                ex.printStackTrace();
            }

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
