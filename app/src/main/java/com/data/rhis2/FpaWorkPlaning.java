package com.data.rhis2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import Common.Connection;
import Common.Global;

/**
 * Created by Nisan on 3/13/2016.
 */
public class FpaWorkPlaning extends Activity {
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
        inflater.inflate(R.menu.mnuview, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder adb = new AlertDialog.Builder(FpaWorkPlaning.this);
        switch (item.getItemId()) {

            case R.id.menuview:
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
    TextView txtReqName;
    LinearLayout secReqToCode;
    TextView VlblReqToCode;
    TextView txtFpiWarea;


    //LinearLayout secItem;
    //TextView VlblItem;
    // Spinner spnItem;
    Spinner spnVillage1;
    // EditText dtpAgDT;
    // ImageButton btnAgDT;

    //EditText dtpItemDT;
    // ImageButton btnItemDT;


    String StartTime;
    String DeviceNo;
    ListView list;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.fpaworkplaning);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();

            list = (ListView) findViewById(R.id.lstData);
            //View header = getLayoutInflater().inflate(R.layout.fpaworkplanheading, null);
            //  list.addHeaderView(header);

            TableName = "fpaWorkPlanMaster";
            TableNameDetail = "fpaWorkPlanDetail";

            lblHS10 = (TextView) findViewById(R.id.lblHS10);
            secS10 = (LinearLayout) findViewById(R.id.secS10);
            secSlNo = (LinearLayout) findViewById(R.id.secSlNo);
            VlblSlNo = (TextView) findViewById(R.id.VlblSlNo);


            lblHS10.setText("পরিবার কল্যাণ সহকারীর মাসিক অগ্রিম কর্মসূচী");


            secFPIPMonth = (LinearLayout) findViewById(R.id.secFPIPMonth);
            VlblFPIPMonth = (TextView) findViewById(R.id.VlblFPIPMonth);
            spnFPIPMonth = (Spinner) findViewById(R.id.spnFPIPMonth);
            spnFPIPMonth.setAdapter(C.getArrayAdapterMultiline("SELECT strftime('%Y', date('now')) ||','||mName||':-'||substr('0' ||id, -2, 2) as ym from month  where id <=(SELECT strftime('%m','now')+1) order by id Desc"));

            spnFPIPMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    String val = spnFPIPMonth.getSelectedItemPosition() == 0 ? "" : Global.Right(spnFPIPMonth.getSelectedItem().toString(), 2);
                    if (val.length() >= 0) {

                        DataSearch("");

                    }


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


            DataSearch("");

        } catch (Exception e) {
            Connection.MessageBox(FpaWorkPlaning.this, e.getMessage());
            return;
        }
    }


    private void DataSearch(String month) {
        try {


            list.setAdapter(null);

            dataList.clear();

            int VDay = 0;
            if (String.valueOf(Global.Right(spnFPIPMonth.getSelectedItem().toString(), 2)).equalsIgnoreCase("01")) {

                VDay = 31;
            } else if (String.valueOf(Global.Right(spnFPIPMonth.getSelectedItem().toString(), 2)).equalsIgnoreCase("02")) {
                VDay = 29;
            } else if (String.valueOf(Global.Right(spnFPIPMonth.getSelectedItem().toString(), 2)).equalsIgnoreCase("03")) {
                VDay = 31;
            } else if (String.valueOf(Global.Right(spnFPIPMonth.getSelectedItem().toString(), 2)).equalsIgnoreCase("04")) {
                VDay = 30;
            } else if (String.valueOf(Global.Right(spnFPIPMonth.getSelectedItem().toString(), 2)).equalsIgnoreCase("05")) {
                VDay = 31;
            } else if (String.valueOf(Global.Right(spnFPIPMonth.getSelectedItem().toString(), 2)).equalsIgnoreCase("06")) {
                VDay = 30;
            } else if (String.valueOf(Global.Right(spnFPIPMonth.getSelectedItem().toString(), 2)).equalsIgnoreCase("07")) {
                VDay = 31;
            } else if (String.valueOf(Global.Right(spnFPIPMonth.getSelectedItem().toString(), 2)).equalsIgnoreCase("08")) {
                VDay = 31;
            } else if (String.valueOf(Global.Right(spnFPIPMonth.getSelectedItem().toString(), 2)).equalsIgnoreCase("09")) {
                VDay = 30;
            } else if (String.valueOf(Global.Right(spnFPIPMonth.getSelectedItem().toString(), 2)).equalsIgnoreCase("10")) {
                VDay = 31;
            } else if (String.valueOf(Global.Right(spnFPIPMonth.getSelectedItem().toString(), 2)).equalsIgnoreCase("11")) {
                VDay = 30;
            } else if (String.valueOf(Global.Right(spnFPIPMonth.getSelectedItem().toString(), 2)).equalsIgnoreCase("12")) {
                VDay = 31;
            }
            for (int i = 1; i <= VDay; i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                //w list = (ListView)findViewById(R.id.lstData);
                String day = "";
                if (String.valueOf(i).length() == 1) {
                    day = "0" + String.valueOf(i);
                } else if (String.valueOf(i).length() == 2) {
                    day = String.valueOf(i);
                }

                //  map.put("cWorkPlanId", String.valueOf(i));
                map.put("workPlanDate", day);
                map.put("Year", String.valueOf(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 4)));
                map.put("Month", String.valueOf(Global.Right(spnFPIPMonth.getSelectedItem().toString(), 2)));
                // map.put("WorkDate", String.valueOf(C.ReturnSingleValue("SELECT strftime('%d/%m/%Y', date(workPlanDate)) from fpaWorkPlanDetail where workPlanDate='"+String.valueOf(Global.Left(spnFPIPMonth.getSelectedItem().toString(),4))+"-"+String.valueOf(Global.Right(spnFPIPMonth.getSelectedItem().toString(),2))+"-"+String.valueOf(i)+"'")));
                map.put("itemdes", "");


                dataList.add(map);

                dataAdapter = new SimpleAdapter(FpaWorkPlaning.this, dataList, R.layout.fpaworkplanrow, new String[]{"edit", "delete"},
                        new int[]{R.id.cmdB1, R.id.cmdB2});

                list.setAdapter(new DataListAdapter(this));
            }
        } catch (Exception e) {
            Connection.MessageBox(FpaWorkPlaning.this, e.getMessage());
            return;
        }
    }

    private String GetParentWorkPlanId() {
        String SQL = "";

        SQL = "select (ifnull(MAX([workPlanId]),'')+1) AS workPlanId from fpaWorkPlanMaster";
        //SQL += " where healthId='"+ g.getGeneratedId()+"' and providerId='"+ g.getProvCode() +"'";
        String SNo = C.ReturnSingleValue(SQL);
        // Serial No auto increment
        return SNo;
    }

    private String GetParentWorkPlanId1() {
        String SQL = "";

        SQL = "select workPlanId from fpaWorkPlanMaster";
        SQL += " where month='" + g.getAreaDateM() + "' and providerId='" + g.getProvCode() + "'";//+ "' and workAreaId='" + g.getAreaUnitValue()
        String SNo = C.ReturnSingleValue(SQL);
        // Serial No auto increment
        return SNo;
    }

    /* private String GetChildWorkPlanId(String parentWorkPlanId)
     {
         String SQL = "";

         SQL = "select (ifnull(MAX([cWorkPlanId]),'')+1) AS workPlanId from fpaWorkPlanDetail WHERE workPlanId = '" + parentWorkPlanId +"'";
         //SQL += " where healthId='"+ g.getGeneratedId()+"' and providerId='"+ g.getProvCode() +"'";
         String SNo = C.ReturnSingleValue(SQL);

         return SNo;
     }*/
    private void GetAssignedArea() {
        //SELECT v.zillaid AS dist, v.upazilaid AS upazila,v.unionid AS unions,
        String SQL = "SELECT (v. mouzaid || v. villageid) ||'-'|| v . villagename AS villname " +
                " from Village v";
        SQL += " LEFT outer join Household h on v.zillaid=h.dist and v.upazilaid=h.upz and v.unionid=h.un and v.mouzaid=h.mouza and v.villageid=h.vill";
        SQL += " LEFT JOIN Mouza M ON M.upazilaid = h.upz AND M.unionid = h.un AND M.mouzaid = h.mouza ";

        SQL += " where v.zillaid='" + g.getDistrict() + "' and v.upazilaid='" + g.getUpazila() + "' and v.unionid='" + g.getUnion() + "'";
        SQL += " group by v.zillaid,v.upazilaid,v.unionid,v.mouzaid,v.villageid,v.villagename";

        // spnVillage.setAdapter(C.getArrayAdapterMultiline(SQL));

    }


    public void DeleteFPAItem(String fpaItem) {
        try {


            String SQL = "";

            if (C.Existence("Select workPlanId, providerId from fpaWorkPlanDetail where workPlanId='" + GetParentWorkPlanId1() + "' and providerId='" + g.getProvCode() + "' and workPlanDate='" + g.getAreaDate() + "' and fpaItem= '" + fpaItem + "'"))

            {

                SQL = "Delete from fpaWorkPlanDetail where workPlanId='" + GetParentWorkPlanId1() + "' and providerId='" + g.getProvCode() + "' and fpaItem= '" + fpaItem + "' and workPlanDate='" + g.getAreaDate() + "'";
                C.Save(SQL);

            } else {

            }


        } catch (Exception e) {
            Connection.MessageBox(FpaWorkPlaning.this, e.getMessage());
            return;
        }

    }


    private void DisplayFPAWorkPlaining() {
        final Dialog popupView = new Dialog(FpaWorkPlaning.this);
        popupView.setTitle("FWA ওয়ার্কপ্ল্যান");

        popupView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popupView.setContentView(R.layout.fpaworkplainingpopup);
        popupView.setCancelable(true);
        popupView.setCanceledOnTouchOutside(true);

        final TextView lblHlblepireg = (TextView) popupView.findViewById(R.id.lblHlblepireg);

        lblHlblepireg.setText(Global.DateConvertDMY(g.getAreaDate()) + ":-" + "কর্মসূচী সমূহ");

        final EditText txtUnitno = (EditText) popupView.findViewById(R.id.txtUnitno);

        txtUnitno.setText(g.getFWAUnit());
        final Spinner spnVill = (Spinner) popupView.findViewById(R.id.spnVill);


        spnVill.setAdapter(C.getArrayAdapterMultiline("Select '-' as VILLAGENAME union select MOUZAID||VILLAGEID||'-'||VILLAGENAME as VILLAGENAME\n" +
                "from Village"));
        List<String> leaves = new ArrayList<String>();

        leaves.add("");
        leaves.add("1-বাৎসরিক");
        leaves.add("2-অসুস্থতা জনিত");
        leaves.add("3-মাতৃত্বকালীন");


        final Spinner spnLeave = (Spinner) popupView.findViewById(R.id.spnLeave);
        ArrayAdapter<String> leaveadptr = new ArrayAdapter<String>(this, R.layout.multiline_spinner_dropdown_item, leaves);
        spnLeave.setAdapter(leaveadptr);

        final LinearLayout secUnit = (LinearLayout) popupView.findViewById(R.id.secUnit);
        final LinearLayout secVill = (LinearLayout) popupView.findViewById(R.id.secVill);
        final LinearLayout secElcono = (LinearLayout) popupView.findViewById(R.id.secElcono);
        final LinearLayout secLeave = (LinearLayout) popupView.findViewById(R.id.secLeave);
        final LinearLayout secOther = (LinearLayout) popupView.findViewById(R.id.secOther);
        final EditText txtElcono = (EditText) popupView.findViewById(R.id.txtElcono);
        final EditText txtElcono1 = (EditText) popupView.findViewById(R.id.txtElcono1);
        final EditText txtOther = (EditText) popupView.findViewById(R.id.txtOther);

        final CheckBox chkElco = (CheckBox) popupView.findViewById(R.id.chkElco);
        final CheckBox chkCC = (CheckBox) popupView.findViewById(R.id.chkCC);
        final CheckBox chkStC = (CheckBox) popupView.findViewById(R.id.chkStC);
        final CheckBox chkEpi = (CheckBox) popupView.findViewById(R.id.chkEpi);

        final CheckBox chkFpC = (CheckBox) popupView.findViewById(R.id.chkFpC);
        final CheckBox chkFDone = (CheckBox) popupView.findViewById(R.id.chkFDone);
        final CheckBox chkUnFMeeting = (CheckBox) popupView.findViewById(R.id.chkUnFMeeting);
        final CheckBox chkUFMeeting = (CheckBox) popupView.findViewById(R.id.chkUFMeeting);

        final CheckBox chkHDay = (CheckBox) popupView.findViewById(R.id.chkHDay);
        final CheckBox chkMreport = (CheckBox) popupView.findViewById(R.id.chkMreport);
        final CheckBox chkTraining = (CheckBox) popupView.findViewById(R.id.chkTraining);
        final CheckBox chkROther = (CheckBox) popupView.findViewById(R.id.chkROther);
        final Button cmdSave = (Button) popupView.findViewById(R.id.cmdSave);
        chkElco.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (chkElco.isChecked()) {
                    secUnit.setVisibility(View.VISIBLE);
                    secVill.setVisibility(View.VISIBLE);
                    secElcono.setVisibility(View.VISIBLE);

                } else {

                    secUnit.setVisibility(View.GONE);
                    secVill.setVisibility(View.GONE);
                    secElcono.setVisibility(View.GONE);


                }
            }
        });
        chkHDay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (chkHDay.isChecked()) {
                    secLeave.setVisibility(View.VISIBLE);


                } else {

                    secLeave.setVisibility(View.GONE);


                }
            }
        });

        chkROther.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (chkROther.isChecked()) {
                    secOther.setVisibility(View.VISIBLE);


                } else {

                    secOther.setVisibility(View.GONE);


                }
            }
        });
        popupView.show();
        secUnit.setVisibility(View.GONE);
        secVill.setVisibility(View.GONE);
        secElcono.setVisibility(View.GONE);
        secLeave.setVisibility(View.GONE);
        secOther.setVisibility(View.GONE);
        // Connection C=new Connection();
        String workplanId1 = GetParentWorkPlanId1();

        //String workplanId1="";
        //workplanId1 = "select workPlanId from fpaWorkPlanMaster";
        //workplanId1 += " where month='"+ g.getAreaDateM()+"' and providerId='"+ g.getProvCode() +"' and workAreaId='"+ g.getAreaUnitValue()+"'";
        //String SNo = C.ReturnSingleValue(workplanId1);
        // String workplanId1 =C.ReturnSingleValue("Select workPlanId from fpaWorkPlanMaster Where workAreaId='"+ g.getAreaUnitValue()+"' AND providerId = '" + g.getProvCode()+"' AND month = '" +g.getAreaDateM()+"'");
        if (C.Existence("Select workPlanId, providerId from fpaWorkPlanDetail where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and workPlanDate='" + g.getAreaDate() + "'")) {


            Cursor cur = C.ReadData("select " +
                    " ifnull(MAX(CASE WHEN fpaItem=1 THEN fpaItem END),'' )As P1,ifnull(MAX(CASE WHEN fpaItem=2 THEN fpaItem END),'' )As P2 ," +
                    " ifnull(MAX(CASE WHEN fpaItem=3 THEN fpaItem END),'' )As P3,ifnull(MAX(CASE WHEN fpaItem=4 THEN fpaItem END),'' )As P4 ," +
                    " ifnull(MAX(CASE WHEN fpaItem=5 THEN fpaItem END),'' )As P5,ifnull(MAX(CASE WHEN fpaItem=6 THEN fpaItem END),'' )As P6 ," +
                    " ifnull(MAX(CASE WHEN fpaItem=7 THEN fpaItem END),'' )As P7,ifnull(MAX(CASE WHEN fpaItem=8 THEN fpaItem END),'' )As P8 ," +
                    " ifnull(MAX(CASE WHEN fpaItem=9 THEN fpaItem END),'' )As P9,ifnull(MAX(CASE WHEN fpaItem=10 THEN fpaItem END),'' )As P10 ," +
                    " ifnull(MAX(CASE WHEN fpaItem=11 THEN fpaItem END),'' )As P11,ifnull(MAX(CASE WHEN fpaItem=12 THEN fpaItem END),'' )As P12 " +
                    " FROM fpaWorkPlanDetail where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and workPlanDate='" + g.getAreaDate() + "'");

            cur.moveToFirst();
            while (!cur.isAfterLast()) {

                if (cur.getString(cur.getColumnIndex("P1")).equals("1")) {
                    chkElco.setChecked(true);

                    if (chkElco.isChecked()) {
                        secUnit.setVisibility(View.VISIBLE);
                        secVill.setVisibility(View.VISIBLE);
                        secElcono.setVisibility(View.VISIBLE);

                    }
                    /*else
                    {

                        secUnit.setVisibility(View.GONE);
                        secVill.setVisibility(View.GONE);
                        secElcono.setVisibility(View.GONE);


                    }*/

                } else {
                    chkElco.setChecked(false);
                }

                if (cur.getString(cur.getColumnIndex("P2")).equals("2")) {
                    chkCC.setChecked(true);
                } else {
                    chkCC.setChecked(false);
                }
                if (cur.getString(cur.getColumnIndex("P3")).equals("3")) {
                    chkStC.setChecked(true);
                } else {
                    chkStC.setChecked(false);
                }

                if (cur.getString(cur.getColumnIndex("P4")).equals("4")) {
                    chkEpi.setChecked(true);
                } else {
                    chkEpi.setChecked(false);
                }

                if (cur.getString(cur.getColumnIndex("P5")).equals("5")) {
                    chkFpC.setChecked(true);
                } else {
                    chkFpC.setChecked(false);
                }

                if (cur.getString(cur.getColumnIndex("P6")).equals("6")) {
                    chkFDone.setChecked(true);
                } else {
                    chkFDone.setChecked(false);
                }
                if (cur.getString(cur.getColumnIndex("P7")).equals("7")) {
                    chkUnFMeeting.setChecked(true);
                } else {
                    chkUnFMeeting.setChecked(false);
                }

                if (cur.getString(cur.getColumnIndex("P8")).equals("8")) {
                    chkUFMeeting.setChecked(true);
                } else {
                    chkUFMeeting.setChecked(false);
                }

                if (cur.getString(cur.getColumnIndex("P9")).equals("9")) {
                    chkHDay.setChecked(true);

                    if (chkHDay.isChecked()) {

                        secLeave.setVisibility(View.VISIBLE);


                    }
                } else {
                    chkHDay.setChecked(false);
                }

                if (cur.getString(cur.getColumnIndex("P10")).equals("10")) {
                    chkMreport.setChecked(true);
                } else {
                    chkMreport.setChecked(false);
                }

                if (cur.getString(cur.getColumnIndex("P11")).equals("11")) {
                    chkTraining.setChecked(true);
                } else {
                    chkTraining.setChecked(false);
                }

                if (cur.getString(cur.getColumnIndex("P12")).equals("12")) {
                    chkROther.setChecked(true);

                    if (chkROther.isChecked()) {

                        secOther.setVisibility(View.VISIBLE);


                    }
                } else {
                    chkROther.setChecked(false);
                }
                cur.moveToNext();
            }
            cur.close();

            Cursor cur1 = C.ReadData("select  village,elcoFrom,elcoTo,leaveType,otherDec  FROM fpaWorkPlanDetail where fpaItem=1 and workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and workPlanDate='" + g.getAreaDate() + "'");
            cur1.moveToFirst();
            while (!cur1.isAfterLast()) {
                if (chkElco.isChecked()) {
                    secUnit.setVisibility(View.VISIBLE);
                    secVill.setVisibility(View.VISIBLE);
                    secElcono.setVisibility(View.VISIBLE);

                    // spnVill.setSelection(Global.SpinnerItemPosition(spnVill, 4 ,cur1.getString(cur1.getColumnIndex("village"))));
                    txtElcono.setText(cur1.getString(cur1.getColumnIndex("elcoFrom")));
                    txtElcono1.setText(cur1.getString(cur1.getColumnIndex("elcoTo")));


                }

                if (chkHDay.isChecked()) {

                    spnLeave.setSelection(Global.SpinnerItemPosition(spnLeave, 1, cur1.getString(cur1.getColumnIndex("leaveType"))));


                }
                if (chkROther.isChecked()) {
                    txtOther.setText(cur1.getString(cur1.getColumnIndex("otherDec")));
                }

                cur1.moveToNext();
            }
            cur1.close();


        }
        cmdSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String SQL = "";
                if (!C.Existence("Select workPlanId from " + TableName + "  Where  providerId = '" + g.getProvCode() + "' AND month = '" + Global.Left(g.getAreaDate(), 7) + "'")) {
                    String workplanId = GetParentWorkPlanId();

                    SQL = String.format("INSERT INTO %s ([workPlanId],[workAreaId], [providerId], [month],[status],[systemEntryDate],[upload]) VALUES " +
                                    "('%s','%s','%s','%s','%s','%s','%s')", TableName, workplanId, Global.Left(g.getAreaUnitValue(), 2), g.getProvCode(),
                            Global.Left(g.getAreaDate(), 7), '1', Global.DateTimeNowYMDHMS(), '2');

                    C.Save(SQL);
                } else {

                }


                if (chkElco.isChecked() == true) {
                    //SymtomValue.add(1);
                    FPAValue.add("1");
                } else if (chkElco.isChecked() == false) {
                    DeleteFPAItem("1");
                }


                if (chkCC.isChecked() == true) {
                    //SymtomValue.add(1);
                    FPAValue.add("2");
                } else if (chkCC.isChecked() == false) {
                    DeleteFPAItem("2");
                }
                if (chkStC.isChecked() == true) {
                    //SymtomValue.add(1);
                    FPAValue.add("3");
                } else if (chkStC.isChecked() == false) {
                    DeleteFPAItem("3");
                }

                if (chkEpi.isChecked() == true) {
                    //SymtomValue.add(1);
                    FPAValue.add("4");
                } else if (chkEpi.isChecked() == false) {
                    DeleteFPAItem("4");
                }

                if (chkFpC.isChecked() == true) {
                    //SymtomValue.add(1);
                    FPAValue.add("5");
                } else if (chkFpC.isChecked() == false) {
                    DeleteFPAItem("5");
                }


                if (chkFDone.isChecked() == true) {
                    //SymtomValue.add(1);
                    FPAValue.add("6");
                } else if (chkFDone.isChecked() == false) {
                    DeleteFPAItem("6");
                }

                if (chkUnFMeeting.isChecked() == true) {
                    //SymtomValue.add(1);
                    FPAValue.add("7");
                } else if (chkUnFMeeting.isChecked() == false) {
                    DeleteFPAItem("7");
                }
                if (chkUFMeeting.isChecked() == true) {
                    //SymtomValue.add(1);
                    FPAValue.add("8");
                } else if (chkUnFMeeting.isChecked() == false) {
                    DeleteFPAItem("8");
                }

                if (chkHDay.isChecked() == true) {
                    //SymtomValue.add(1);
                    FPAValue.add("9");
                } else if (chkHDay.isChecked() == false) {
                    DeleteFPAItem("9");
                }
                if (chkMreport.isChecked() == true) {
                    //SymtomValue.add(1);
                    FPAValue.add("10");
                } else if (chkMreport.isChecked() == false) {
                    DeleteFPAItem("10");
                }
                if (chkTraining.isChecked() == true) {
                    //SymtomValue.add(1);
                    FPAValue.add("11");
                } else if (chkTraining.isChecked() == false) {
                    DeleteFPAItem("11");
                }

                if (chkROther.isChecked() == true) {
                    //SymtomValue.add(1);
                    FPAValue.add("12");
                } else if (chkROther.isChecked() == false) {
                    DeleteFPAItem("12");
                }


                int i = 0;
                for (i = 0; i < FPAValue.size(); i++) {
                    for (String temp : FPAValue) {
                        // System.out.println(temp);
                        // SaveSymtom(temp);

                        String workplanId1 = C.ReturnSingleValue("Select workPlanId from fpaWorkPlanMaster Where  providerId = '" + g.getProvCode() + "' AND month = '" + Global.Left(g.getAreaDate(), 7) + "'");

                        if (!C.Existence("Select workPlanId, providerId, fpaItem from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and fpaItem = '" + temp + "'"))

                        {


                            SQL = "Insert into fpaWorkPlanDetail (workPlanId,fpaItem,workPlanDate,unitNo,village,elcoFrom,elcoTo,leaveType,providerId,systemEntryDate,modifyDate,\n" +
                                    "otherDec,remarks,upload)Values('" + workplanId1 + "','" + temp + "','" + g.getAreaDate() + "','" + Global.Left(txtUnitno.getText().toString(), 2) + "','" + (spnVill.getSelectedItemPosition() == 0 ? "" : Global.Left(spnVill.getSelectedItem().toString(), 4)) + "','" + txtElcono.getText().toString() + "','" + txtElcono1.getText().toString() + "','" + (spnLeave.getSelectedItemPosition() == 0 ? "" : Global.Left(spnLeave.getSelectedItem().toString(), 1)) + "','" + g.getProvCode() + "','" + Global.DateTimeNowYMDHMS() + "','" + txtOther.getText() + "','" + "" + "','" + "','2')";
                            C.Save(SQL);

                            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                            popupView.cancel();
                        } else {
                            popupView.cancel();
                        }
                    }


                }

            }
        });

        //  }
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
                convertView = inflater.inflate(R.layout.fpaworkplanrow, null);
            }
            Button cmdB1 = (Button) convertView.findViewById(R.id.cmdB1);
            Button cmdB2 = (Button) convertView.findViewById(R.id.cmdB2);

            //
            //  final TextView cWorkPlanId = (TextView)convertView.findViewById(R.id.cWorkPlanId);
            final TextView workPlanDate = (TextView) convertView.findViewById(R.id.workPlanDate);
            // final TextView itemdes = (TextView)convertView.findViewById(R.id.itemdes);


            final HashMap<String, String> o = (HashMap<String, String>) dataAdapter.getItem(position);
            // IdNo.setText(o.get("IdNo"));
            //  cWorkPlanId.setText(o.get("cWorkPlanId"));
            // RequestBy.setText(C.ReturnSingleValue("Select ProvName from ProviderDB where ProvCode='"+o.get("RequestBy")+"'"));
            //RequestBy.setText();
            //;
            //g.getDay(dtpDate.getText().toString())
            // workPlanDate.setText(o.get("workPlanDate")+"/03/2016");
            //workPlanDate.setText(o.get("workPlanDate")+"/"+C.ReturnSingleValue("SELECT id from month where id in (SELECT strftime('%m','now'))")+"/"+ Global.Left(spnFPIPMonth.getSelectedItem().toString(),4));


            workPlanDate.setText(o.get("workPlanDate") + '/' + o.get("Month") + '/' + o.get("Year") + "           " + g.getDay(o.get("workPlanDate") + '/' + o.get("Month") + '/' + o.get("Year")));
            //itemdes.setText(o.get("itemdes"));

            if (g.getDay(o.get("workPlanDate") + '/' + o.get("Month") + '/' + o.get("Year")).equalsIgnoreCase("শুক্রবার")) {
                cmdB2.setText("সাপ্তাহিক ছুটি");
                cmdB2.setEnabled(false);
                cmdB2.setBackgroundColor(Color.parseColor("#99cc33"));
                // cmdB2.setBackgroundColor(Color.BLUE);
            }
        /*   else if(g.getDay(o.get("workPlanDate")+'/'+o.get("Month")+'/'+o.get("Year")).equals(o.get("WorkDate")))
            {
                cmdB2.setText("Done");
                cmdB2.setEnabled(false);
                cmdB2.setBackgroundColor(Color.parseColor("#99cc33"));
                // cmdB2.setBackgroundColor(Color.BLUE);
            }*/
            else {
                //cmdB2.setBackgroundColor(Color.parseColor("#FFFFFF"));
                cmdB2.setBackgroundColor(Color.BLUE);
                cmdB2.setEnabled(true);
                cmdB2.setText("কর্মসূচী সংযোজন");
            }




       /*     if(!g.getDay(o.get("workPlanDate")+'/'+o.get("Month")+'/'+o.get("Year")).equalsIgnoreCase("শুক্রবার"))
            {

            }*/


            // Month
     /*      if(String.valueOf(C.ReturnSingleValue("Select TransactionType from "+ TableName + " where TransactionType='"+o.get("TransactionType")+"'"))=="3")
           {
               cmdB2.setEnabled(false);
           }*/

           /* if(String.valueOf(o.get("Upload")).equals("1") && o.get("RequestStatus").equals("0"))
            {
                cmdB2.setEnabled(false);
                cmdB1.setEnabled(false);
                cmdB2.setText("Done");
                cmdB2.setBackgroundColor(Color.BLUE);

            }

            if(String.valueOf(o.get("Upload")).equals("1") && o.get("RequestStatus").equals("1"))
            {
                cmdB2.setEnabled(false);
                cmdB1.setEnabled(false);
                cmdB2.setText("Approved");
                cmdB2.setBackgroundColor(Color.BLUE);

            }
*/
            final AlertDialog.Builder adb = new AlertDialog.Builder(FpaWorkPlaning.this);
           /* cmdB1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    adb.setTitle("Message:");
                    adb.setMessage("Do you want to update this information?[Yes/No]?");
                    adb.setNegativeButton("No", null);
                    adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Cursor cur=C.ReadData("Select * from "+ TableName + "  Where requestId='"+o.get("RequestId")+"'");
                            cur.moveToFirst();
                            while(!cur.isAfterLast())
                            {
                                //txtSlNo.setText(cur.getString(cur.getColumnIndex("requestId")));
                                txtReqName.setText(C.ReturnSingleValue("Select ProvName from ProviderDB where ProvCode='"+ cur.getString(cur.getColumnIndex("requestBy"))+"'"));
                                //txtReqToCode.setText(cur.getString(cur.getColumnIndex("requestTo")));
                                if(cur.getString(cur.getColumnIndex("itemCode")).equals("1"))
                                {
                                    spnItem.setSelection(1);
                                }
                                else if(cur.getString(cur.getColumnIndex("itemCode")).equals("2"))
                                {
                                    spnItem.setSelection(2);
                                }
                                else if(cur.getString(cur.getColumnIndex("itemCode")).equals("3"))
                                {
                                    spnItem.setSelection(3);
                                }
                                else     if(cur.getString(cur.getColumnIndex("itemCode")).equals("9"))
                                {
                                    spnItem.setSelection(4);
                                }
                                else if(cur.getString(cur.getColumnIndex("itemCode")).equals("5"))
                                {
                                    spnItem.setSelection(5);
                                }
                                else if(cur.getString(cur.getColumnIndex("itemCode")).equals("8"))
                                {
                                    spnItem.setSelection(6);
                                }

                              //  txtRequestQty.setText(cur.getString(cur.getColumnIndex("requestQty")));
                              //  txtRemarks.setText(cur.getString(cur.getColumnIndex("requestRemarks")));
                                cur.moveToNext();



                            }
                            cur.close();

                        }});
                    adb.show();
                }
            });*/


            cmdB2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    g.setAreaUnitValue(Global.Left(txtFpiWarea.getText().toString(), 2));
                    g.setAreaDate(o.get("Year") + "-" + o.get("Month") + '-' + o.get("workPlanDate"));
                    g.setAreaDateM(o.get("Year") + "-" + o.get("Month"));

                   // String WorkPlainvalue =C.ReturnSingleValue("Select ifnull(status, '' ) AS status FROM workPlanMaster where providerId='"+g.getProvCode()+"' and month='"+o.get("Year") + "-" + o.get("Month")+"'");
                    String WorkPlainvalue =C.ReturnSingleValue("Select ifnull(A.status, '' ) AS status FROM workPlanMaster A INNER JOIN workPlanDetail B ON A.workPlanId = B.workPlanId and B.providerId=A.providerId where A.providerId='"+g.getProvCode()+"' and A.month='"+o.get("Year") + "-" + o.get("Month")+"'");
                    if(WorkPlainvalue.equals("3")||WorkPlainvalue.equals(""))
                    {
                        DisplayFPAWorkPlaining();
                    }
                    else if(WorkPlainvalue.equals("2"))
                    {
                        adb.setTitle("Workplan");
                        adb.setMessage("Work plan  অনুমোদন হওয়ার  পর পরিবর্তন করে যাবে না ।");
                        adb.setNegativeButton("OK", null);
                        adb.show();
                    }
                    else
                    {
                        DisplayFPAWorkPlaining();
                    }


                }
            });
            return convertView;
        }
    }

    private void DeleteWorkPlan(String cWorkPlanId) {
        String sql = String.format("DELETE FROM '%s' WHERE cWorkPlanId = '%s'", TableNameDetail, cWorkPlanId);
        C.Save(sql);
        DataSearch("");
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