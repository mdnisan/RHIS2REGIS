package com.data.rhis2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
import android.widget.CompoundButton;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import Common.Connection;
import Common.Global;

/**
 * Created by Nisan on 6/5/2016.
 */
public class AHIWorkPlaning extends Activity {
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
        AlertDialog.Builder adb = new AlertDialog.Builder(AHIWorkPlaning.this);
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
                //g.setFWAUnit(txtFpiWarea.getText().toString());
                Intent intentforview = new Intent(getApplicationContext(), AHIWorkPlaningView.class);
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
    ArrayList<String> ItemValue = new ArrayList<String>();
    ArrayList<String> FPADate = new ArrayList<String>();
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


    String StartTime;
    String DeviceNo;
    ListView list;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.ahiworkplaining);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();

            list = (ListView) findViewById(R.id.lstData);
            //View header = getLayoutInflater().inflate(R.layout.fpaworkplanheading, null);
            //  list.addHeaderView(header);

            TableName = "workPlanMaster";
            TableNameDetail = "workPlanDetail";

            lblHS10 = (TextView) findViewById(R.id.lblHS10);
            secS10 = (LinearLayout) findViewById(R.id.secS10);
            secSlNo = (LinearLayout) findViewById(R.id.secSlNo);
            VlblSlNo = (TextView) findViewById(R.id.VlblSlNo);
            lblHS10.setText("সহকারী স্বাস্থ্য পরিদর্শকের মাসিক অগ্রিম কর্মসূচী");
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
            // spnFPIPMonth.setAdapter(C.getArrayAdapterMultiline("SELECT strftime('%Y', date('now')) ||','||mName||':-'||substr('0' ||id, -2, 2) as ym from month  where id <=(SELECT strftime('%m','now')+1) order by id Desc"));
          //  spnFPIPMonth.setAdapter(C.getArrayAdapterMultiline("SELECT substr('0' ||id, -2, 2)||'-'||mName||','||strftime('%Y', date('now'))  as ym from month  where id <=(SELECT strftime('%m','now')+1) order by id Desc"));

            //spnFPIPMonth.setAdapter(C.getArrayAdapterMultiline("SELECT strftime('%Y', date('now'))||','||mName  as ym from month  where id <=(SELECT strftime('%m','now')+1) order by id Desc"));
            spnFPIPMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    String val = spnFPIPMonth.getSelectedItemPosition() == 0 ? "" : Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2);
                    if (val.length() >= 0) {

                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));

                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            secReq = (LinearLayout) findViewById(R.id.secReq);
            VlblReqName = (TextView) findViewById(R.id.VlblReqName);
            txtReqName = (TextView) findViewById(R.id.txtReqName);
            txtReqName.setText(C.ReturnSingleValue("Select ProvName from ProviderDB where ProvCode='" + g.getProvCode() + "'"));
            txtFpiWarea = (TextView) findViewById(R.id.txtFpiWarea);
            txtFpiWarea.setText(g.getFWAUnit());
            secReqToCode = (LinearLayout) findViewById(R.id.secReqToCode);
            VlblReqToCode = (TextView) findViewById(R.id.VlblReqToCode);
            DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));

        } catch (Exception e) {
            Connection.MessageBox(AHIWorkPlaning.this, e.getMessage());
            return;
        }
    }

//Month of day
    private void DataSearch(String month) {
        try {


            list.setAdapter(null);

            dataList.clear();

            int VDay = 0;
            if (String.valueOf(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2)).equalsIgnoreCase("01")) {

                VDay = 31;
            } else if (String.valueOf(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2)).equalsIgnoreCase("02")) {
                VDay = 29;
            } else if (String.valueOf(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2)).equalsIgnoreCase("03")) {
                VDay = 31;
            } else if (String.valueOf(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2)).equalsIgnoreCase("04")) {
                VDay = 30;
            } else if (String.valueOf(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2)).equalsIgnoreCase("05")) {
                VDay = 31;
            } else if (String.valueOf(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2)).equalsIgnoreCase("06")) {
                VDay = 30;
            } else if (String.valueOf(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2)).equalsIgnoreCase("07")) {
                VDay = 31;
            } else if (String.valueOf(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2)).equalsIgnoreCase("08")) {
                VDay = 31;
            } else if (String.valueOf(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2)).equalsIgnoreCase("09")) {
                VDay = 30;
            } else if (String.valueOf(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2)).equalsIgnoreCase("10")) {
                VDay = 31;
            } else if (String.valueOf(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2)).equalsIgnoreCase("11")) {
                VDay = 30;
            } else if (String.valueOf(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2)).equalsIgnoreCase("12")) {
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
                map.put("Year", String.valueOf(Global.Right(spnFPIPMonth.getSelectedItem().toString(), 4)));
                map.put("Month", String.valueOf(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2)));
                // map.put("WorkDate", String.valueOf(C.ReturnSingleValue("SELECT strftime('%d/%m/%Y', date(workPlanDate)) from fpaWorkPlanDetail where workPlanDate='"+String.valueOf(Global.Left(spnFPIPMonth.getSelectedItem().toString(),4))+"-"+String.valueOf(Global.Right(spnFPIPMonth.getSelectedItem().toString(),2))+"-"+String.valueOf(i)+"'")));
                map.put("itemdes", "");


                dataList.add(map);

                dataAdapter = new SimpleAdapter(AHIWorkPlaning.this, dataList, R.layout.fpaworkplanrow, new String[]{"edit", "delete"},
                        new int[]{R.id.cmdB1, R.id.cmdB2});

                list.setAdapter(new DataListAdapter(this));
            }
        } catch (Exception e) {
            Connection.MessageBox(AHIWorkPlaning.this, e.getMessage());
            return;
        }
    }

    private String GetParentWorkPlanId() {
        String SQL = "";

        SQL = "select (ifnull(MAX([workPlanId]),'')+1) AS workPlanId from workPlanMaster";
        //SQL += " where healthId='"+ g.getGeneratedId()+"' and providerId='"+ g.getProvCode() +"'";
        String SNo = C.ReturnSingleValue(SQL);
        // Serial No auto increment
        return SNo;
    }

    private String GetParentWorkPlanId1() {
        String SQL = "";

        SQL = "select workPlanId from workPlanMaster";
        SQL += " where month='" + g.getAreaDateM() + "' and providerId='" + g.getProvCode() + "'";//+ "' and workAreaId='" + g.getAreaUnitValue()
        String SNo = C.ReturnSingleValue(SQL);
        // Serial No auto increment
        return SNo;
    }



    private void DisplayAHIWorkPlaining() {
        final Dialog popupView = new Dialog(AHIWorkPlaning.this);
        popupView.setTitle("AHI ওয়ার্কপ্ল্যান");

        popupView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popupView.setContentView(R.layout.ahiworkplainingpopup);
        popupView.setCancelable(true);
        popupView.setCanceledOnTouchOutside(true);

        final TextView lblHlblepireg = (TextView) popupView.findViewById(R.id.lblHlblepireg);

        lblHlblepireg.setText(Global.DateConvertDMY(g.getAreaDate()) + ":-" + "কর্মসূচী সমূহ");


        List<String> leaves = new ArrayList<String>();

        leaves.add("");
        leaves.add("1-বাৎসরিক");
        leaves.add("2-অসুস্থতা জনিত");
        leaves.add("3-মাতৃত্বকালীন");
        leaves.add("4-পিতৃত্ব কালীন ছুটি");
        leaves.add("5-নৈমিত্তিক ছুটি");
        leaves.add("6-শ্রান্তি ও বিনোদন");
        leaves.add("7-ঐচ্ছিক ছুটি ");
        leaves.add("8-সরকারী ছুটি ");
        leaves.add("9-অন্যান্য");


        final Spinner spnLeave = (Spinner) popupView.findViewById(R.id.spnLeave);
        ArrayAdapter<String> leaveadptr = new ArrayAdapter<String>(this, R.layout.multiline_spinner_dropdown_item, leaves);
        spnLeave.setAdapter(leaveadptr);
        final Spinner spnProvider = (Spinner) popupView.findViewById(R.id.spnProvider);

        final Spinner spnSessionDate = (Spinner) popupView.findViewById(R.id.spnSessionDate);

        final Spinner spnEpiSubBlock = (Spinner) popupView.findViewById(R.id.spnEpiSubBlock);

        final Spinner spnEPICenterName = (Spinner) popupView.findViewById(R.id.spnEPICenterName);

        final Spinner spnPWord = (Spinner) popupView.findViewById(R.id.spnPWord);
        List<String> Nprogram = new ArrayList<String>();

        Nprogram.add("");
        Nprogram.add("1-জাতীয় টিকা দিবস");
        Nprogram.add("2-জাতীয় ক্রিমি সপ্তাহ");
        Nprogram.add("3-জাতীয় ভিটামিন এ প্লাস ক্যাম্পেইন");


        final Spinner spnNProgram = (Spinner) popupView.findViewById(R.id.spnNProgram);
        ArrayAdapter<String> Nprogramadptr = new ArrayAdapter<String>(this, R.layout.multiline_spinner_dropdown_item, Nprogram);
        spnNProgram.setAdapter(Nprogramadptr);
        // final LinearLayout secF2=(LinearLayout) popupView.findViewById(R.id.secF2);
        final LinearLayout secLeave = (LinearLayout) popupView.findViewById(R.id.secLeave);
        final LinearLayout secUnion = (LinearLayout) popupView.findViewById(R.id.secUnion);
        //final LinearLayout secUnion1 = (LinearLayout) popupView.findViewById(R.id.secUnion1);
        final LinearLayout secMouza = (LinearLayout) popupView.findViewById(R.id.secMouza);
        //  final LinearLayout secVill=(LinearLayout) popupView.findViewById(R.id.secVill);
        final LinearLayout secPara = (LinearLayout) popupView.findViewById(R.id.secPara);
        final LinearLayout secBarivisit = (LinearLayout) popupView.findViewById(R.id.secBarivisit);

        final LinearLayout secWord = (LinearLayout) popupView.findViewById(R.id.secWord);
        final LinearLayout secname = (LinearLayout) popupView.findViewById(R.id.secname);
        final LinearLayout secSessionDate = (LinearLayout) popupView.findViewById(R.id.secSessionDate);
        final LinearLayout secHSubBlock = (LinearLayout) popupView.findViewById(R.id.secHSubBlock);
        final LinearLayout secEPICenterName = (LinearLayout) popupView.findViewById(R.id.secEPICenterName);
       // final LinearLayout secccWord = (LinearLayout) popupView.findViewById(R.id.secccWord);
        //final LinearLayout secccName = (LinearLayout) popupView.findViewById(R.id.secccName);
        final LinearLayout secNProgram = (LinearLayout) popupView.findViewById(R.id.secNProgram);

        final LinearLayout secOther = (LinearLayout) popupView.findViewById(R.id.secOther);
        //final Spinner SpnUnion1 = (Spinner) popupView.findViewById(R.id.SpnUnion1);
        final Spinner SpnUnion = (Spinner) popupView.findViewById(R.id.SpnUnion);
        final Spinner SpnWord = (Spinner) popupView.findViewById(R.id.SpnWord);
        final Spinner SpnMouza = (Spinner) popupView.findViewById(R.id.SpnMouza);
        final EditText txtOther = (EditText) popupView.findViewById(R.id.txtOther);

        final EditText txtPara = (EditText) popupView.findViewById(R.id.txtPara);
        final EditText txtBarivisit1 = (EditText) popupView.findViewById(R.id.txtBarivisit1);
        final EditText txtBarivisit2 = (EditText) popupView.findViewById(R.id.txtBarivisit2);
        //SpnUnion.setAdapter(C.getArrayAdapter("select ward from ProviderArea where provCode = '"+ g.getProvCode()+"'"));
        // SpnUnion.setAdapter(C.getArrayAdapter("select cast(unionid as varchar(2))||'-'||unionname from Unions where zillaid='"+ g.getDistrict() +"' and upazilaid='"+ g.getUpazila() +"' and unionid='"+ g.getUnion() +"'"));


        final Spinner SpnCCWord = (Spinner) popupView.findViewById(R.id.SpnCCWord);
        //  SpnCCWord.setAdapter(C.getArrayAdapter("select '-' wardId from ccInfo Union select distinct wardId from ccInfo where zillaid='"+ g.getDistrict() +"' and upazilaid='"+ g.getUpazila() +"' and unionid='"+ g.getUnion() +"'"));
        final Spinner spnccName = (Spinner) popupView.findViewById(R.id.spnccName);
       /* ArrayAdapter<String> ccNameadptr= new ArrayAdapter<String>(this, R.layout.multiline_spinner_dropdown_item, ccName);
        spnccName.setAdapter(ccNameadptr);*/

       /* SpnUnion1.setAdapter(C.getArrayAdapter("select '-' unionname from Unions Union select distinct substr('0' ||ifnull(p.unionid,'99'), -2, 2)||'-'||u.unionname from Unions u left outer join ProviderArea p on u.zillaid=p.zillaid and  u.upazilaid=p.upazilaid  and  u.unionid=p.unionid"));
        SpnUnion1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (SpnUnion1.getSelectedItemPosition() > 0) {

                    SpnCCWord.setAdapter(C.getArrayAdapter("select '-' wardId from ccInfo Union select distinct wardId from ccInfo where ZILAID='" + g.getDistrict() + "' and UPAZILAID='" + g.getUpazila() + "' and unionid='" + Global.Left(SpnUnion1.getSelectedItem().toString(), 2) + "'"));//+"' and unionid='"+ g.getUnion()

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });*/
        //SpnCCWord.setAdapter(C.getArrayAdapter("select '-' wardId from ccInfo Union select distinct wardId from ccInfo where ZILAID='"+ g.getDistrict() +"' and UPAZILAID='"+ g.getUpazila()  +"'"));//+"' and unionid='"+ g.getUnion()
        //final Spinner spnccName = (Spinner) popupView.findViewById(R.id.spnccName);
       /* ArrayAdapter<String> ccNameadptr= new ArrayAdapter<String>(this, R.layout.multiline_spinner_dropdown_item, ccName);
        spnccName.setAdapter(ccNameadptr);*/

        /*SpnCCWord.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (SpnCCWord.getSelectedItemPosition() > 0) {

                    spnccName.setAdapter(C.getArrayAdapterMultiline("select distinct CCID||'-'||CCNAME from ccInfo where ZILAID='" + g.getDistrict() + "' and UPAZILAID='" + g.getUpazila() + "' and unionid='" + Global.Left(SpnUnion1.getSelectedItem().toString(), 2) + "' and WARDID='" + Global.Left(SpnCCWord.getSelectedItem().toString(), 1) + "'"));

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });*/
        final CheckBox chkIPCVisit = (CheckBox) popupView.findViewById(R.id.chkIPCVisit);
        final CheckBox chkEPIVisit = (CheckBox) popupView.findViewById(R.id.chkEPIVisit);
        final CheckBox chkCCVisit = (CheckBox) popupView.findViewById(R.id.chkCCVisit);
        final CheckBox chkOfficeWork = (CheckBox) popupView.findViewById(R.id.chkOfficeWork);
        final CheckBox chkUHMMeeting = (CheckBox) popupView.findViewById(R.id.chkUHMMeeting);
        final CheckBox chkMonthlyReportColl = (CheckBox) popupView.findViewById(R.id.chkMonthlyReportColl);
       // final CheckBox chkMonthlyReportCol2 = (CheckBox) popupView.findViewById(R.id.chkMonthlyReportCol2);
        final CheckBox chkMonthlyWorkPlan = (CheckBox) popupView.findViewById(R.id.chkMonthlyWorkPlan);
        final CheckBox chkTrainingAttended = (CheckBox) popupView.findViewById(R.id.chkTrainingAttended);
        final CheckBox chkNationalProgramAttended = (CheckBox) popupView.findViewById(R.id.chkNationalProgramAttended);
        final CheckBox chkOtherProgram = (CheckBox) popupView.findViewById(R.id.chkOtherProgram);

        final CheckBox chkHDay = (CheckBox) popupView.findViewById(R.id.chkHDay);

        final Button cmdSave = (Button) popupView.findViewById(R.id.cmdSave);

        chkIPCVisit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (chkIPCVisit.isChecked()) {
                    secUnion.setVisibility(View.VISIBLE);
                    secWord.setVisibility(View.VISIBLE);
                    secMouza.setVisibility(View.VISIBLE);
                    //  secVill.setVisibility(View.VISIBLE);
                    secPara.setVisibility(View.VISIBLE);
                    secBarivisit.setVisibility(View.VISIBLE);
                    /*SpnUnion.setAdapter(C.getArrayAdapter("select distinct substr('0' ||p.unionid, -2, 2)||'-'||u.unionname from Unions u left outer join ProviderArea p on u.zillaid=p.zillaid and  u.upazilaid=p.upazilaid"));
                    SpnUnion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                            if (SpnUnion.getSelectedItemPosition() >0) {

                                SpnWord.setAdapter(C.getArrayAdapter(" select distinct substr('0' ||ifnull(ward, '0' ), -2, 2) from ProviderArea where unionid='" + Global.Left(SpnUnion.getSelectedItem().toString(), 2) + "'"));

                                SpnMouza.setAdapter(C.getArrayAdapter("Select '  ' VILLAGENAME from Village union select (CASE WHEN Length(v.MOUZAID)=1 THEN substr('00' ||v.MOUZAID, -3, 3)||v.VILLAGEID||'-'||v.VILLAGENAME WHEN Length(v.MOUZAID)=2 THEN substr('0' ||v.MOUZAID, -3, 3)||v.VILLAGEID||'-'||v.VILLAGENAME WHEN Length(v.MOUZAID)=3 THEN substr('0' ||v.MOUZAID, -3, 3)||v.VILLAGEID||'-'||v.VILLAGENAME ELSE '' END) as VILLAGENAME from Village v\n" +
                                        "left outer join ProviderArea p on v.ZILLAID=p.zillaid and  v.UPAZILAID=p.upazilaid and\n" +
                                        " v.MOUZAID||v.VILLAGEID=p.mouzaid||p.villageid where p.unionid='"+Integer.valueOf(Global.Left(SpnUnion.getSelectedItem().toString().trim(),2))+"'"));
                            }


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                        }
                    });*/
                    SpnUnion.setAdapter(C.getArrayAdapter("select distinct substr('0' ||p.unionid, -2, 2)||'-'||u.unionname from Unions u left outer join ProviderArea p on u.zillaid=p.zillaid and  u.upazilaid=p.upazilaid and p.unionid=u.unionid"));
                    SpnUnion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                            if (SpnUnion.getSelectedItemPosition() >= 0) {

                                SpnWord.setAdapter(C.getArrayAdapter(" select distinct substr('0' ||ifnull(ward, '0' ), -2, 2) from ProviderArea where unionid='" + Global.Left(SpnUnion.getSelectedItem().toString(), 2) + "'"));

                            }


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                        }
                    });

                    SpnWord.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                            if (SpnWord.getSelectedItemPosition() >= 0) {
                                SpnMouza.setAdapter(C.getArrayAdapter("Select '  ' VILLAGENAME from Village union select (CASE WHEN Length(v.MOUZAID)=1 THEN substr('00' ||v.MOUZAID, -3, 3)||v.VILLAGEID||'-'||v.VILLAGENAME WHEN Length(v.MOUZAID)=2 THEN substr('0' ||v.MOUZAID, -3, 3)||v.VILLAGEID||'-'||v.VILLAGENAME WHEN Length(v.MOUZAID)=3 THEN substr('0' ||v.MOUZAID, -3, 3)||v.VILLAGEID||'-'||v.VILLAGENAME ELSE '' END) as VILLAGENAME from Village v\n" +
                                        "left outer join ProviderArea p on v.ZILLAID=p.zillaid and  v.UPAZILAID=p.upazilaid and\n" +
                                        " v.MOUZAID||v.VILLAGEID=p.mouzaid||p.villageid where cast(p.unionid as Int)='"+Integer.valueOf(Global.Left(SpnUnion.getSelectedItem().toString().trim(),2))+"'"));

                               /* SpnMouza.setAdapter(C.getArrayAdapter("Select '  ' VILLAGENAME from Village union select (CASE WHEN Length(v.MOUZAID)=1 THEN substr('00' ||v.MOUZAID, -3, 3)||v.VILLAGEID||'-'||v.VILLAGENAME WHEN Length(v.MOUZAID)=2 THEN substr('0' ||v.MOUZAID, -3, 3)||v.VILLAGEID||'-'||v.VILLAGENAME WHEN Length(v.MOUZAID)=3 THEN substr('0' ||v.MOUZAID, -3, 3)||v.VILLAGEID||'-'||v.VILLAGENAME ELSE '' END) as VILLAGENAME from Village v\n" +
                                        "left outer join ProviderArea p on v.ZILLAID=p.zillaid and  v.UPAZILAID=p.upazilaid and\n" +
                                        " v.MOUZAID||v.VILLAGEID=p.mouzaid||p.villageid where p.Ward='"+Integer.valueOf(Global.Left(SpnWord.getSelectedItem().toString().trim(),2))+"'"));



                                SpnMouza.setAdapter(C.getArrayAdapter("Select '  ' VILLAGENAME from Village union select (CASE WHEN Length(v.MOUZAID)=1 THEN substr('00' ||v.MOUZAID, -3, 3)||v.VILLAGEID||'-'||v.VILLAGENAME WHEN Length(v.MOUZAID)=2 THEN substr('0' ||v.MOUZAID, -3, 3)||v.VILLAGEID||'-'||v.VILLAGENAME WHEN Length(v.MOUZAID)=3 THEN substr('0' ||v.MOUZAID, -3, 3)||v.VILLAGEID||'-'||v.VILLAGENAME ELSE '' END) as VILLAGENAME from Village v\n" +
                                        "left outer join ProviderArea p on v.ZILLAID=p.zillaid and  v.UPAZILAID=p.upazilaid and\n" +
                                        " v.MOUZAID||v.VILLAGEID=p.mouzaid||p.villageid"));

*/
                            }


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                        }
                    });


                } else {

                    secUnion.setVisibility(View.GONE);
                    secWord.setVisibility(View.GONE);
                    secMouza.setVisibility(View.GONE);
                    //  secVill.setVisibility(View.GONE);
                    secPara.setVisibility(View.GONE);
                    secBarivisit.setVisibility(View.GONE);

                    SpnUnion.setSelection(0);
                    SpnWord.setSelection(0);
                    SpnMouza.setSelection(0);
                    txtPara.setText("");
                    txtBarivisit1.setText("");
                    txtBarivisit2.setText("");


                }
            }
        });

        chkEPIVisit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (chkEPIVisit.isChecked()) {
                    secname.setVisibility(View.VISIBLE);
                    secSessionDate.setVisibility(View.VISIBLE);
                    secHSubBlock.setVisibility(View.VISIBLE);
                    secEPICenterName.setVisibility(View.VISIBLE);
                    String Type = "";
                    if (g.getProvType().equals("10")) {
                        Type = "3";
                    } else if (g.getProvType().equals("11")) {
                        Type = "2";
                    }
                    spnProvider.setAdapter(C.getArrayAdapter(" Select substr('0' ||ProvCode, -6, 6)||'-'||provName from ProviderDb WHERE provType='" + Type + "'"));


                    spnProvider.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                            if (spnProvider.getSelectedItemPosition() >= 0) {

                                spnPWord.setAdapter(C.getArrayAdapter("select distinct substr('0' ||ward, -2, 2)ward from ProviderArea where cast(provCode as INT)= '" +Global.Left(spnProvider.getSelectedItem().toString(),6)+ "'"));

                                spnEpiSubBlock.setAdapter(C.getArrayAdapter("SELECT DISTINCT sb.BCode || '-' || sb.BNameBan\n" +
                                        "           FROM HABlock sb\n" +
                                        "               inner JOIN epiSchedulerUpdate epib\n" +
                                        "                             ON sb.BCode = epib.subBlockId\n" +
                                        "                             inner JOIN ProviderDB pdb\n" +
                                        "                             ON epib.Dist = pdb.zillaid and  epib.Upz = pdb.upazilaid  and  epib.Un = pdb.unionid "));

                                //spnSessionDate.setAdapter(C.getArrayAdapter("select strftime('%d/%m/%Y', date(scheduleDate)) as scheduleDate from epiScheduler where  providerId='" + Global.Left(spnProvider.getSelectedItem().toString(), 5) + "' and strftime('%m', date(scheduleDate))=(SELECT strftime('%m','now'))  order by scheduleDate desc"));


                            }


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                        }
                    });

                    spnPWord.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            String val =Global.Left(spnEpiSubBlock.getSelectedItem().toString(), 2);
                            String word =Global.Left(spnPWord.getSelectedItem().toString(), 2);
                            if (spnPWord.getSelectedItemPosition() >= 0) {

                                spnSessionDate.setAdapter(C.getArrayAdapter("SELECT strftime( '%d/%m/%Y', date( scheduleDate )  ) AS scheduleDate\n" +
                                        "  FROM epiSchedulerUpdate where subBlockId='" + val + "' and wordOld='"+word+"' And scheduleYear = ( SELECT strftime ( '%Y' , 'now' ) ) order by scheduleDate desc"));

                            }


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                        }
                    });

                    spnEpiSubBlock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                            if (spnEpiSubBlock.getSelectedItemPosition() >= 0) {


                                // spnSessionDate
                                // spnEPICenterName.setAdapter(C.getArrayAdapter("select substr('0' ||schedulerId, -2, 2)||'-'||providerId||'-'||centerName from epiScheduler where subBlockId='" + Global.Left(spnEpiSubBlock.getSelectedItem().toString(), 2) + "' and providerId='" + Global.Left(spnProvider.getSelectedItem().toString(), 5) + "' and  strftime('%d/%m/%Y', date(scheduleDate))='" + Global.Left(spnSessionDate.getSelectedItem().toString(), 10) + "'"));
                                String val =Global.Left(spnEpiSubBlock.getSelectedItem().toString(), 2);
                                String word =Global.Left(spnPWord.getSelectedItem().toString(), 2);
                                if (val.length() >0) {
                                    spnSessionDate.setAdapter(C.getArrayAdapter("SELECT strftime( '%d/%m/%Y', date( scheduleDate )  ) AS scheduleDate\n" +
                                            "  FROM epiSchedulerUpdate where subBlockId='" + val + "' and wordOld='"+word+"' And scheduleYear = ( SELECT strftime ( '%Y' , 'now' ) ) order by scheduleDate desc"));
                                    //spnEPICenterName.setAdapter(C.getArrayAdapter("select substr('0' ||schedulerId, -2, 2)||'-'||centerName FROM epiSchedulerUpdate where subBlockId='" + val + "' and scheduleYear = ( SELECT strftime ( '%Y' , 'now' ) ) order by scheduleDate desc"));

                                }
                            }


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                        }
                    });

                } else {
                    secname.setVisibility(View.GONE);
                    secSessionDate.setVisibility(View.GONE);
                    secHSubBlock.setVisibility(View.GONE);
                    secEPICenterName.setVisibility(View.GONE);
                    // spnLeave.setSelection(0);
                    spnEpiSubBlock.setSelection(0);
                    spnEPICenterName.setSelection(0);


                }
            }
        });


        spnSessionDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String SubBlock =Global.Left(spnEpiSubBlock.getSelectedItem().toString(), 2);
                String val =Global.Left(spnSessionDate.getSelectedItem().toString(), 10);
                String word =Global.Left(spnPWord.getSelectedItem().toString(), 2);


                if (val.length() >0) {
                    String Y = "";
                    String M = "";
                    String D = "";

                    Y = Global.Right(spnSessionDate.getSelectedItem().toString(), 4);
                    D = Global.Left(spnSessionDate.getSelectedItem().toString(), 2);
                    M = Global.Mid(spnSessionDate.getSelectedItem().toString(), 3, 5);



                    //spnEPICenterName.setAdapter(C.getArrayAdapter("select substr('0' ||schedulerId, -2, 2)||'-'||centerName FROM epiSchedulerUpdate where subBlockId='" + val + "' and scheduleYear = ( SELECT strftime ( '%Y' , 'now' ) ) order by scheduleDate desc"));
                    spnEPICenterName.setAdapter(C.getArrayAdapter("select subBlockId||wordOld||scheduleYear||substr('0' ||schedulerId, -2, 2)||'-'||centerName FROM epiSchedulerUpdate where subBlockId='" + SubBlock + "' and wordOld='"+word+"' and scheduleDate='" +  Y + "-" + M + "-" + D +"'"));

                    spnEPICenterName.setEnabled(false);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });


        chkNationalProgramAttended.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (chkNationalProgramAttended.isChecked()) {
                    secNProgram.setVisibility(View.VISIBLE);


                } else {

                    secNProgram.setVisibility(View.GONE);
                    spnNProgram.setSelection(0);


                }
            }
        });

        chkHDay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (chkHDay.isChecked()) {
                    secLeave.setVisibility(View.VISIBLE);


                } else {

                    secLeave.setVisibility(View.GONE);
                    spnLeave.setSelection(0);


                }
            }
        });

        chkOtherProgram.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (chkOtherProgram.isChecked()) {
                    secOther.setVisibility(View.VISIBLE);


                } else {

                    secOther.setVisibility(View.GONE);


                }
            }
        });
        popupView.show();
        secBarivisit.setVisibility(View.GONE);
        secPara.setVisibility(View.GONE);
        //secVill.setVisibility(View.GONE);
       // secUnion1.setVisibility(View.GONE);
        secUnion.setVisibility(View.GONE);
        secWord.setVisibility(View.GONE);
        secMouza.setVisibility(View.GONE);
        secLeave.setVisibility(View.GONE);
        secname.setVisibility(View.GONE);
        secSessionDate.setVisibility(View.GONE);
        secHSubBlock.setVisibility(View.GONE);
        secEPICenterName.setVisibility(View.GONE);
        secOther.setVisibility(View.GONE);
        //secccWord.setVisibility(View.GONE);
        //secccName.setVisibility(View.GONE);
        secNProgram.setVisibility(View.GONE);
        String workplanId1 = GetParentWorkPlanId1();

        String SQL = "";
        SQL = "select " +
                " ifnull(MAX(CASE WHEN item=1 THEN item END),'' )As P1,ifnull(MAX(CASE WHEN item=2 THEN item END),'' )As P2 ," +
                " ifnull(MAX(CASE WHEN item=3 THEN item END),'' )As P3,ifnull(MAX(CASE WHEN item=4 THEN item END),'' )As P4 ," +
                " ifnull(MAX(CASE WHEN item=5 THEN item END),'' )As P5,ifnull(MAX(CASE WHEN item=6 THEN item END),'' )As P6 ," +
                " ifnull(MAX(CASE WHEN item=7 THEN item END),'' )As P7,ifnull(MAX(CASE WHEN item=8 THEN item END),'' )As P8 ," +
                " ifnull(MAX(CASE WHEN item=9 THEN item END),'' )As P9,ifnull(MAX(CASE WHEN item=10 THEN item END),'' )As P10 ," +
                " ifnull(MAX(CASE WHEN item=11 THEN item END),'' )As P11" +
                " FROM workPlanDetail where  providerId='" + g.getProvCode() + "' and workPlanDate='" + g.getAreaDate() + "'";

        if (C.Existence(SQL)) {
            Cursor cur = C.ReadData(SQL);

            cur.moveToFirst();
            while (!cur.isAfterLast()) {

                if (cur.getString(cur.getColumnIndex("P1")).equals("1")) {
                    chkIPCVisit.setChecked(true);

                    if (chkIPCVisit.isChecked()) {
                        secUnion.setVisibility(View.VISIBLE);
                        secWord.setVisibility(View.VISIBLE);
                        secMouza.setVisibility(View.VISIBLE);
                        //  secVill.setVisibility(View.VISIBLE);
                        secPara.setVisibility(View.VISIBLE);
                        secBarivisit.setVisibility(View.VISIBLE);
                        Cursor cur1 = C.ReadData("select ipcUN,ipcWord,ipcMouza,ipcVill,ipcPara,ipcBariFrom,ipcBariTo FROM workPlanDetail where item=1 and workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and workPlanDate='" + g.getAreaDate() + "'");
                        cur1.moveToFirst();
                        while (!cur1.isAfterLast()) {
                            SpnUnion.setAdapter(C.getArrayAdapter("select distinct substr('0' ||unionid, -2, 2)||'-'||unionname from Unions where unionid='"+cur1.getString(cur1.getColumnIndex("ipcUN"))+"'"));
                            SpnWord.setAdapter(C.getArrayAdapter("select distinct substr('0' ||ward, -2, 2) from ProviderArea where  unionid='"+cur1.getString(cur1.getColumnIndex("ipcUN"))+"' and cast(ward as int)='"+cur1.getString(cur1.getColumnIndex("ipcWord"))+"'"));
                            SpnMouza.setAdapter(C.getArrayAdapterMultiline("select (CASE WHEN Length(MOUZAID)=1 THEN substr('00' ||MOUZAID, -3, 3)||VILLAGEID||'-'||VILLAGENAME \n" +
                                    "WHEN Length(MOUZAID)=2 THEN substr('0' ||MOUZAID, -3, 3)||VILLAGEID||'-'||VILLAGENAME WHEN Length(MOUZAID)=3 THEN substr('0' ||MOUZAID, -3, 3)||VILLAGEID||'-'||VILLAGENAME                        \n" +
                                    " ELSE '' END) as VILLAGENAME\n" +
                                    "from Village where MOUZAID='" + cur1.getString(cur1.getColumnIndex("ipcMouza"))+ "'"));


                            txtPara.setText(cur1.getString(cur1.getColumnIndex("ipcPara")));
                            txtBarivisit1.setText(cur1.getString(cur1.getColumnIndex("ipcBariFrom")));
                            txtBarivisit2.setText(cur1.getString(cur1.getColumnIndex("ipcBariTo")));
                            cur1.moveToNext();
                        }
                        cur1.close();

                    }
                } else {
                    chkIPCVisit.setChecked(false);


                }

                if (cur.getString(cur.getColumnIndex("P2")).equals("2")) {
                    chkEPIVisit.setChecked(true);
                    if (chkEPIVisit.isChecked()) {
                        secname.setVisibility(View.VISIBLE);
                        secSessionDate.setVisibility(View.VISIBLE);
                        secHSubBlock.setVisibility(View.VISIBLE);
                        secEPICenterName.setVisibility(View.VISIBLE);

                        Cursor cur1 = C.ReadData("select epiproviderId,epischedulerId FROM workPlanDetail where item=2 and workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and workPlanDate='" + g.getAreaDate() + "'");
                        cur1.moveToFirst();
                        while (!cur1.isAfterLast()) {
                            String EpiSubBlock=Global.Left(cur1.getString(cur1.getColumnIndex("epischedulerId")),1);
                            String schedulerId=Global.Right(cur1.getString(cur1.getColumnIndex("epischedulerId")),2);
                            String ward=Global.Mid(cur1.getString(cur1.getColumnIndex("epischedulerId")),1,2);
                            spnProvider.setAdapter(C.getArrayAdapter("Select substr('0' ||ProvCode, -6, 6)||'-'||provName from ProviderDb WHERE cast(provCode as Int)='" + cur1.getString(cur1.getColumnIndex("epiproviderId")) + "'"));
                           spnPWord.setAdapter(C.getArrayAdapter("select distinct substr('0' ||wordOld, -2, 2)ward from epiSchedulerUpdate WHERE wordOld='" +ward+ "'"));
                            spnEpiSubBlock.setAdapter(C.getArrayAdapter("select distinct sb.BCode||'-'||sb.BNameBan from HABlock sb left outer join epiSchedulerUpdate epib on sb.BCode=epib.subBlockId where epib.subBlockId='" +EpiSubBlock+ "'"));
                            spnSessionDate.setAdapter(C.getArrayAdapter("select strftime('%d/%m/%Y', date(scheduleDate)) as scheduleDate from epiSchedulerUpdate where subBlockId='" +EpiSubBlock+"'And schedulerId='" +schedulerId+ "'"));
                            spnEPICenterName.setAdapter(C.getArrayAdapter("select substr('0' ||schedulerId, -2, 2)||'-'||centerName from epiSchedulerUpdate where subBlockId='" + EpiSubBlock+"'And schedulerId='" +schedulerId+ "'"));


                            cur1.moveToNext();
                        }
                        cur1.close();
                    }
                } else {
                    chkEPIVisit.setChecked(false);

                }

                if (cur.getString(cur.getColumnIndex("P3")).equals("3")) {
                    chkCCVisit.setChecked(true);
                } else {
                    chkCCVisit.setChecked(false);

                }
                if (cur.getString(cur.getColumnIndex("P4")).equals("4")) {
                    chkOfficeWork.setChecked(true);
                } else {
                    chkOfficeWork.setChecked(false);

                }

                if (cur.getString(cur.getColumnIndex("P5")).equals("5")) {
                    chkUHMMeeting.setChecked(true);
                } else {
                    chkUHMMeeting.setChecked(false);


                }

                if (cur.getString(cur.getColumnIndex("P6")).equals("6")) {
                    chkHDay.setChecked(true);

                    if (chkHDay.isChecked()) {

                        secLeave.setVisibility(View.VISIBLE);


                        Cursor cur1 = C.ReadData("select leaveType FROM workPlanDetail where item=6 and workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and workPlanDate='" + g.getAreaDate() + "'");
                        cur1.moveToFirst();
                        while (!cur1.isAfterLast()) {


                            spnLeave.setSelection(Global.SpinnerItemPosition(spnLeave, 1, cur1.getString(cur1.getColumnIndex("leaveType"))));


                            cur1.moveToNext();
                        }
                        cur1.close();
                    }
                } else {
                    chkHDay.setChecked(false);


                }

                if (cur.getString(cur.getColumnIndex("P7")).equals("7")) {
                    chkMonthlyReportColl.setChecked(true);
                } else {
                    chkMonthlyReportColl.setChecked(false);


                }

                if (cur.getString(cur.getColumnIndex("P8")).equals("8")) {
                    chkMonthlyWorkPlan.setChecked(true);


                } else {
                    chkMonthlyWorkPlan.setChecked(false);
                }
                if (cur.getString(cur.getColumnIndex("P9")).equals("9")) {
                    chkTrainingAttended.setChecked(true);
                } else {
                    chkTrainingAttended.setChecked(false);


                }

                if (cur.getString(cur.getColumnIndex("P10")).equals("10")) {
                    chkNationalProgramAttended.setChecked(true);

                    if (chkNationalProgramAttended.isChecked()) {

                        secNProgram.setVisibility(View.VISIBLE);


                        Cursor cur1 = C.ReadData("select natProgramType FROM workPlanDetail where item=10 and workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and workPlanDate='" + g.getAreaDate() + "'");
                        cur1.moveToFirst();
                        while (!cur1.isAfterLast()) {


                            spnNProgram.setSelection(Global.SpinnerItemPosition(spnNProgram, 1, cur1.getString(cur1.getColumnIndex("natProgramType"))));


                            cur1.moveToNext();
                        }
                        cur1.close();


                    }
                } else {
                    chkNationalProgramAttended.setChecked(false);


                }



                if (cur.getString(cur.getColumnIndex("P11")).equals("11")) {
                    chkOtherProgram.setChecked(true);

                    if (chkOtherProgram.isChecked()) {

                        secOther.setVisibility(View.VISIBLE);

                        Cursor cur1 = C.ReadData("select otherDec FROM workPlanDetail where item=11 and workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and workPlanDate='" + g.getAreaDate() + "'");
                        cur1.moveToFirst();
                        while (!cur1.isAfterLast()) {
                            txtOther.setText(cur1.getString(cur1.getColumnIndex("otherDec")));


                            cur1.moveToNext();
                        }
                        cur1.close();


                    }
                } else {
                    chkOtherProgram.setChecked(false);
                }


                cur.moveToNext();
            }
            cur.close();


        } else {

        }
        cmdSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String SQL = "";
                if (!C.Existence("Select workPlanId from " + TableName + "  Where  providerId = '" + g.getProvCode() + "' AND month = '" + Global.Left(g.getAreaDate(), 7) + "'")) {
                    String workplanId = GetParentWorkPlanId();

                    SQL = String.format("INSERT INTO %s ([workPlanId],[workAreaId], [providerId], [month],[status],[systemEntryDate],[upload]) VALUES " +
                                    "('%s','%s','%s','%s','%s','%s','%s')", TableName, workplanId, g.getAreaUnitValue(), g.getProvCode(),
                            Global.Left(g.getAreaDate(), 7), '1', Global.DateTimeNowYMDHMS(), '2');

                    C.Save(SQL);
                } else {

                }


                String SQ = "Select workPlanDate, providerId from workPlanDetail where  providerId='" + g.getProvCode() + "' and workPlanDate='" + g.getAreaDate() + "'";
                if (!C.Existence(SQ)) {
                    if (chkIPCVisit.isChecked() == true) {
                        if (SpnMouza.getSelectedItemPosition() == 0) {
                            Connection.MessageBox(AHIWorkPlaning.this, "মৌজা /গ্রাম সিলেক্ট করুন।");
                            // dtpDeathDT.requestFocus();
                            return;
                        } /*else   if (txtPara.getText().length() == 0) {
                            Connection.MessageBox(HAWorkPlaning.this, "IPC পরিদর্শন পাড়া লিখুন।");
                            // dtpDeathDT.requestFocus();
                            return;
                        }*/

                        else if (txtBarivisit1.getText().length() == 0) {
                            Connection.MessageBox(AHIWorkPlaning.this, "বাড়ি পরিদর্শন সংখ্যা  কত লিখুন।");
                            // dtpDeathDT.requestFocus();
                            return;
                        } else if (txtBarivisit2.getText().length() == 0) {
                            Connection.MessageBox(AHIWorkPlaning.this, "বাড়ি পরিদর্শন সংখ্যা  কত থেকে লিখুন।");
                            // dtpDeathDT.requestFocus();
                            return;
                        }else {
                            ItemValue.add("1");
                        }

                    }


                    if (chkEPIVisit.isChecked() == true) {

                        String Provider = Global.Left(spnProvider.getSelectedItem().toString(), 6);
                        String EpiSubBlock= Global.Left(spnEpiSubBlock.getSelectedItem().toString(), 2);
                        String SessionDate= Global.Left(spnSessionDate.getSelectedItem().toString(), 10);
                        if (Provider.length()==0) {
                            Connection.MessageBox(AHIWorkPlaning.this, "স্বাস্থ্য সহকারীর নাম সিলেক্ট করুন।");
                            // dtpDeathDT.requestFocus();
                            return;
                        }

                        else if (EpiSubBlock.length()==0) {
                            Connection.MessageBox(AHIWorkPlaning.this, "ই পি আই সাব-ব্লক সিলেক্ট করুন।");
                            // dtpDeathDT.requestFocus();
                            return;
                        }

                        else if (SessionDate.length()==0) {
                            Connection.MessageBox(AHIWorkPlaning.this, "সেশন তারিখ সিলেক্ট করুন।");
                            // dtpDeathDT.requestFocus();
                            return;
                        }

                        else {
                            ItemValue.add("2");
                        }
                    }
                    if (chkCCVisit.isChecked() == true) {

                        ItemValue.add("3");
                    }
                    if (chkOfficeWork.isChecked() == true) {

                        ItemValue.add("4");
                    }


                    if (chkUHMMeeting.isChecked() == true) {

                        ItemValue.add("5");
                    }
                    if (chkHDay.isChecked() == true) {
                        //SymtomValue.add(1);


                        if (spnLeave.getSelectedItemPosition() == 0) {
                            Connection.MessageBox(AHIWorkPlaning.this, "ছুটি :ধরন সিলেক্ট করুন।");
                            // dtpDeathDT.requestFocus();
                            return;
                        } else {
                            ItemValue.add("6");
                        }
                    }

                    if (chkMonthlyReportColl.isChecked() == true) {

                        ItemValue.add("7");
                    }


                    if (chkMonthlyWorkPlan.isChecked() == true) {

                        ItemValue.add("8");
                    }


                    if (chkTrainingAttended.isChecked() == true) {

                        ItemValue.add("9");
                    }


                    if (chkNationalProgramAttended.isChecked() == true) {

                        if (spnNProgram.getSelectedItemPosition() == 0) {
                            Connection.MessageBox(AHIWorkPlaning.this, "জাতীয় প্রোগ্রামে অংশগ্রহনের অবস্থা: সিলেক্ট করুন।");
                            // dtpDeathDT.requestFocus();
                            return;
                        } else {
                            ItemValue.add("10");
                        }

                    }


                    if (chkOtherProgram.isChecked() == true) {

                        ItemValue.add("11");
                    }
                    int i = 0;
                    for (i = 0; i < ItemValue.size(); i++) {
                        for (String temp : ItemValue) {
                            // System.out.println(temp);
                            // SaveSymtom(temp);

                            String workplanId1 = C.ReturnSingleValue("Select workPlanId from workPlanMaster Where  providerId = '" + g.getProvCode() + "' AND month = '" + Global.Left(g.getAreaDate(), 7) + "'");

                            if (!C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + temp + "' and workPlanDate='" + g.getAreaDate() + "'"))

                            {

                                //   SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epiproviderId,epischedulerId,ccWard,ccID,leaveType,natProgramType,providerId,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('"+workplanId1+"','"+temp+"','"+g.getAreaDate()+"','"+(SpnUnion.getSelectedItemPosition()==0?"":Global.Left(SpnUnion.getSelectedItem().toString(), 2))+"','"+(SpnWord.getSelectedItemPosition()==0?"":Global.Left(SpnWord.getSelectedItem().toString(), 1))+"','"+ (SpnMouza.getSelectedItemPosition()==0?"":Global.Left(SpnMouza.getSelectedItem().toString(), 3)) +"','"+ (SpnMouza.getSelectedItemPosition()==0?"":Global.Mid(SpnMouza.getSelectedItem().toString(), 3,4))+"','"+txtPara.getText()+"','"+txtBarivisit1.getText()+"','"+txtBarivisit2.getText()+"','"+Global.Left(spnEPICenterName.getSelectedItem().toString(), 2)+"','"+(spnProvider.getSelectedItemPosition()==0?"":Global.Left(spnProvider.getSelectedItem().toString(), 5))+"','"+(SpnCCWord.getSelectedItemPosition()==0?"":Global.Left(SpnCCWord.getSelectedItem().toString(), 1))+"','"+(spnccName.getSelectedItemPosition()==0?"":Global.Left(spnccName.getSelectedItem().toString(), 1))+"','"+ (spnLeave.getSelectedItemPosition()==0?"":Global.Left(spnLeave.getSelectedItem().toString(), 1))+"','"+(spnNProgram.getSelectedItemPosition()==0?"":Global.Left(spnNProgram.getSelectedItem().toString(), 1))+"','"+g.getProvCode()+"','"+ Global.DateTimeNowYMDHMS() +"','"+""+"','"+ ""+"','"+"','2')";
                                if (temp.equals("1")) {
                                   // SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,status,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('"+workplanId1+"','"+temp+"','"+g.getAreaDate()+"','"+""+"','"+""+"','"+""+"','"+""+"','"+""+"','"+Global.Left(SpnUnion.getSelectedItem().toString(), 2)+"','"+Global.Left(SpnWord.getSelectedItem().toString(), 2)+"','"+ (SpnMouza.getSelectedItemPosition()==0?"":Global.Left(SpnMouza.getSelectedItem().toString(), 3)) +"','"+ (SpnMouza.getSelectedItemPosition()==0?"":Global.Mid(SpnMouza.getSelectedItem().toString(), 3,4))+"','"+txtPara.getText()+"','"+txtBarivisit1.getText()+"','"+txtBarivisit2.getText()+"','"+""+"','"+""+"','"+""+"','"+""+"','"+ ""+"','"+""+"','"+g.getProvCode()+"','"+1+ "','"+ Global.DateTimeNowYMDHMS() +"','"+""+"','"+ ""+"','"+"','2')";
                                    SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epiproviderId,epischedulerId,ccWard,ccID,leaveType,natProgramType,providerId,systemEntryDate,modifyDate,otherDec,remarks,status,upload)Values('" + workplanId1 + "','" + temp + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" +  Global.Left(SpnUnion.getSelectedItem().toString(), 2) + "','" + Global.Left(SpnWord.getSelectedItem().toString(), 2) + "','" + (SpnMouza.getSelectedItemPosition() == 0 ? "" : Global.Left(SpnMouza.getSelectedItem().toString(), 3)) + "','" + (SpnMouza.getSelectedItemPosition() == 0 ? "" : Global.Mid(SpnMouza.getSelectedItem().toString(), 3, 4)) + "','" + txtPara.getText() + "','" + txtBarivisit1.getText() + "','" + txtBarivisit2.getText() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" +""+"','" +1+ "','2')";
                                    C.Save(SQL);
                                }/* else if (temp.equals("2")) {

                                    //SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,systemEntryDate,modifyDate,otherDec,remarks,status,upload)Values('" + workplanId1 + "','" + temp + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + Global.Left(spnEPICenterName.getSelectedItem().toString(), 2) + "','" + (spnProvider.getSelectedItemPosition() == 0 ? "" : Global.Left(spnProvider.getSelectedItem().toString(), 5)) + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" +""+"','" +1+ "','2')";
                                    SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiSubBlock,epiSessionDate,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,status,systemEntryDate,modifyDate,otherDec,remarks,status,upload)Values('" + workplanId1 + "','" + temp + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + (chkEPIVisit.isChecked() ?Global.Left(spnEPICenterName.getSelectedItem().toString(), 2): "")  + "','"+ (chkEPIVisit.isChecked() ?Global.Left(spnEpiSubBlock.getSelectedItem().toString(), 2) : "") + "','" +(chkEPIVisit.isChecked() ?Global.DateConvertYMD(Global.Left(spnSessionDate.getSelectedItem().toString(), 10)) : "") + "','"+ (chkEPIVisit.isChecked() ? Global.Left(spnProvider.getSelectedItem().toString(), 5) : "") + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + 1 + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "" + "','" + 1 + "','2')";

                                    C.Save(SQL);
                                }*/ /*else if (temp.equals("3")) {
                                    SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epiproviderId,epischedulerId,ccWard,ccID,leaveType,natProgramType,providerId,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + temp + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + (SpnCCWord.getSelectedItemPosition() == 0 ? "" : Global.Left(SpnCCWord.getSelectedItem().toString(), 1)) + "','" + (spnccName.getSelectedItemPosition() == 0 ? "" : Global.Left(spnccName.getSelectedItem().toString(), 1)) + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "','2')";
                                    C.Save(SQL);
                                }*/

                                else if (temp.equals("2")) {
                                    SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,status,systemEntryDate,modifyDate,otherDec,remarks,status,upload)Values('" + workplanId1 + "','" + temp + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + (chkEPIVisit.isChecked() ?Global.Left(spnEPICenterName.getSelectedItem().toString(), 8): "")   + "','"+ (chkEPIVisit.isChecked() ? Global.Left(spnProvider.getSelectedItem().toString(), 6) : "") + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + 1 + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "" + "','" + 1 + "','2')";
                                    C.Save(SQL);
                                }
                                else if (temp.equals("6")) {
                                    SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epiproviderId,epischedulerId,ccWard,ccID,leaveType,natProgramType,providerId,systemEntryDate,modifyDate,otherDec,remarks,status,upload)Values('" + workplanId1 + "','" + temp + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + (spnLeave.getSelectedItemPosition() == 0 ? "" : Global.Left(spnLeave.getSelectedItem().toString(), 1)) + "','" + "" + "','" + g.getProvCode() + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" +""+"','" +1+ "','2')";
                                    C.Save(SQL);
                                } else if (temp.equals("10")) {
                                    SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epiproviderId,epischedulerId,ccWard,ccID,leaveType,natProgramType,providerId,systemEntryDate,modifyDate,otherDec,remarks,status,upload)Values('" + workplanId1 + "','" + temp + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + (spnNProgram.getSelectedItemPosition() == 0 ? "" : Global.Left(spnNProgram.getSelectedItem().toString(), 1)) + "','" + g.getProvCode() + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" +""+"','" +1+ "','2')";
                                    C.Save(SQL);
                                }
                                else   if(temp.equals("11"))
                                {
                                    SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,status,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + temp + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + 1 + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + (chkOtherProgram.isChecked() ? (txtOther.getText().toString()) : "") + "','" + "','2')";
                                    C.Save(SQL);
                                }
                                else {
                                    SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epiproviderId,epischedulerId,ccWard,ccID,leaveType,natProgramType,providerId,systemEntryDate,modifyDate,otherDec,remarks,status,upload)Values('" + workplanId1 + "','" + temp + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" +""+"','" +1+"','2')";
                                    C.Save(SQL);
                                }

                                DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                                popupView.cancel();
                            } else {
                                popupView.cancel();
                            }
                        }


                    }

                    ItemValue.clear();


                } else if (C.Existence(SQ)) {
                    String workplanId1 = C.ReturnSingleValue("Select workPlanId from workPlanMaster Where  providerId = '" + g.getProvCode() + "' AND month = '" + Global.Left(g.getAreaDate(), 7) + "'");


                    //1 chkIPCVisit
                    if (chkIPCVisit.isChecked() == true && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 1 + "' and workPlanDate='" + g.getAreaDate() + "'")) {
                        if (txtBarivisit1.getText().length() == 0) {
                            Connection.MessageBox(AHIWorkPlaning.this, "বাড়ি পরিদর্শন সংখ্যা  কত লিখুন।");
                            // dtpDeathDT.requestFocus();
                            return;
                        } else if (txtBarivisit2.getText().length() == 0) {
                            Connection.MessageBox(AHIWorkPlaning.this, "বাড়ি পরিদর্শন সংখ্যা  কত থেকে লিখুন।");
                            // dtpDeathDT.requestFocus();
                            return;
                        }
                        //ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo
                        SQL = "Update workPlanDetail set ";
                        SQL += "ipcUN = '" + (chkIPCVisit.isChecked() ? Global.Left(SpnUnion.getSelectedItem().toString(), 2) : "") + "',";
                        SQL += "ipcWord = '" + (chkIPCVisit.isChecked() ? Global.Left(SpnWord.getSelectedItem().toString(), 2) : "") + "',";
                        SQL += "ipcMouza = '" + (chkIPCVisit.isChecked() ? Global.Left(SpnMouza.getSelectedItem().toString(), 3) : "") + "',";
                        SQL += "ipcVill = '" + (chkIPCVisit.isChecked() ? Global.Mid(SpnMouza.getSelectedItem().toString(), 3, 4) : "") + "',";
                        SQL += "ipcPara = '" + (chkIPCVisit.isChecked() ? (txtPara.getText().toString()) : "") + "',";
                        SQL += "ipcBariFrom = '" + (chkIPCVisit.isChecked() ? (txtBarivisit1.getText().toString()) : "") + "',";
                        SQL += "ipcBariTo = '" + (chkIPCVisit.isChecked() ? (txtBarivisit2.getText().toString()) : "") + "',";
                        SQL += "modifyDate = '" + Global.DateTimeNowYMDHMS() + "',";
                        SQL += "upload ='2'";
                        SQL += "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 1 + "' and workPlanDate='" + g.getAreaDate() + "'";
                        C.Save(SQL);
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkIPCVisit.isChecked() == true && !C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 1 + "' and workPlanDate='" + g.getAreaDate() + "'")) {
                        SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epiproviderId,epischedulerId,ccWard,ccID,leaveType,natProgramType,providerId,systemEntryDate,modifyDate,otherDec,remarks,status,upload)Values('" + workplanId1 + "','" + 1 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" +  Global.Left(SpnUnion.getSelectedItem().toString(), 2) + "','" + Global.Left(SpnWord.getSelectedItem().toString(), 2) + "','" + (SpnMouza.getSelectedItemPosition() == 0 ? "" : Global.Left(SpnMouza.getSelectedItem().toString(), 3)) + "','" + (SpnMouza.getSelectedItemPosition() == 0 ? "" : Global.Mid(SpnMouza.getSelectedItem().toString(), 3, 4)) + "','" + txtPara.getText() + "','" + txtBarivisit1.getText() + "','" + txtBarivisit2.getText() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" +""+"','" +1+ "','2')";
                       // SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + 1 + "','" + g.getAreaDate() + "','" + (SpnUnion.getSelectedItemPosition() == 0 ? "" : Global.Left(SpnUnion.getSelectedItem().toString(), 2)) + "','" + (SpnWord.getSelectedItemPosition() == 0 ? "" : Global.Left(SpnWord.getSelectedItem().toString(), 1)) + "','" + (SpnMouza.getSelectedItemPosition() == 0 ? "" : Global.Left(SpnMouza.getSelectedItem().toString(), 3)) + "','" + (SpnMouza.getSelectedItemPosition() == 0 ? "" : Global.Mid(SpnMouza.getSelectedItem().toString(), 3, 4)) + "','" + txtPara.getText() + "','" + txtBarivisit1.getText() + "','" + txtBarivisit2.getText() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "','2')";
                        C.Save(SQL);
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkIPCVisit.isChecked() == false && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 1 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        C.Save("Delete from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 1 + "' and workPlanDate='" + g.getAreaDate() + "'");
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    }
                    //2 chkEPIVisit
                    if (chkEPIVisit.isChecked() == true && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 2 + "' and workPlanDate='" + g.getAreaDate() + "'")) {
                        //epischedulerId,epiproviderId
                        SQL = "Update workPlanDetail set ";
                        // SQL += "epiSubBlock = '" + (chkEPIVisit.isChecked() ? Global.Left(spnEpiSubBlock.getSelectedItem().toString(), 2) : "") + "',";
                        SQL += "epischedulerId = '" + (chkEPIVisit.isChecked() ? Global.Left(spnEPICenterName.getSelectedItem().toString(), 8) : "") + "',";
                        //SQL += "epiSessionDate = '" + (chkEPIVisit.isChecked() ? Global.DateConvertYMD(Global.Left(spnSessionDate.getSelectedItem().toString(), 10)) : "") + "',";
                        SQL += "epiproviderId = '" + (chkEPIVisit.isChecked() ? Global.Left(spnProvider.getSelectedItem().toString(),6) : "") + "',";
                        SQL += "modifyDate = '" + Global.DateTimeNowYMDHMS() + "',";
                        SQL += "upload ='2'";
                        SQL += "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 2 + "' and workPlanDate='" + g.getAreaDate() + "'";
                        C.Save(SQL);
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkEPIVisit.isChecked() == true && !C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 2 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        // SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,status,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('"+workplanId1+"','"+2+"','"+g.getAreaDate()+"','"+""+"','"+""+"','"+ "" +"','"+ ""+"','"+""+"','"+""+"','"+""+"','"+Global.Left(spnEPICenterName.getSelectedItem().toString(), 2)+"','"+(spnProvider.getSelectedItemPosition()==0?"":Global.Left(spnProvider.getSelectedItem().toString(), 5))+"','"+""+"','"+""+"','"+ ""+"','"+""+"','"+g.getProvCode()+"','"+ 1+ "','"+ Global.DateTimeNowYMDHMS() +"','"+""+"','"+ ""+"','"+"','2')";
                        // SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,status,systemEntryDate,modifyDate,otherDec,remarks,status,upload)Values('" + workplanId1 + "','" + 2 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + Global.Left(spnEpiSubBlock.getSelectedItem().toString(), 2) + "','" +  (chkEPIVisit.isChecked() ? Global.Left(spnProvider.getSelectedItem().toString(), 5) : "") + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + 1 + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "" + "','" + 1 + "','2')";
                        //SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiSubBlock,epiSessionDate,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,status,systemEntryDate,modifyDate,otherDec,remarks,status,upload)Values('" + workplanId1 + "','" + 2 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + (chkEPIVisit.isChecked() ?Global.Left(spnEPICenterName.getSelectedItem().toString(), 2): "")  + "','"+ (chkEPIVisit.isChecked() ?Global.Left(spnEpiSubBlock.getSelectedItem().toString(), 2) : "") + "','" +(chkEPIVisit.isChecked() ?Global.DateConvertYMD(Global.Left(spnSessionDate.getSelectedItem().toString(), 10)) : "") + "','"+ (chkEPIVisit.isChecked() ? Global.Left(spnProvider.getSelectedItem().toString(), 5) : "") + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + 1 + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "" + "','" + 1 + "','2')";
                        SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,status,systemEntryDate,modifyDate,otherDec,remarks,status,upload)Values('" + workplanId1 + "','" + 2 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + (chkEPIVisit.isChecked() ?Global.Left(spnEPICenterName.getSelectedItem().toString(), 8): "")   + "','"+ (chkEPIVisit.isChecked() ? Global.Left(spnProvider.getSelectedItem().toString(), 6) : "") + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + 1 + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "" + "','" + 1 + "','2')";

                        C.Save(SQL);
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkEPIVisit.isChecked() == false && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 2 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        C.Save("Delete from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 2 + "' and workPlanDate='" + g.getAreaDate() + "'");
                        //C.Save(SQL);
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    }

                    //2 chkEPIVisit
                   /* if (chkEPIVisit.isChecked() == true && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 2 + "' and workPlanDate='" + g.getAreaDate() + "'")) {
                        SQL = "Update workPlanDetail set ";
                        SQL += "epischedulerId = '" + (chkEPIVisit.isChecked() ? Global.Left(spnEPICenterName.getSelectedItem().toString(), 2) : "") + "',";
                        SQL += "epiproviderId = '" + (chkEPIVisit.isChecked() ? Global.Left(spnProvider.getSelectedItem().toString(), 5) : "") + "',";
                        SQL += "modifyDate = '" + Global.DateTimeNowYMDHMS() + "',";
                        SQL += "upload ='2'";
                        SQL += "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 2 + "' and workPlanDate='" + g.getAreaDate() + "'";
                        C.Save(SQL);
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkEPIVisit.isChecked() == true && !C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 2 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        //SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + 2 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + Global.Left(spnEPICenterName.getSelectedItem().toString(), 2) + "','" + (spnProvider.getSelectedItemPosition() == 0 ? "" : Global.Left(spnProvider.getSelectedItem().toString(), 5)) + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "','2')";
                        SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epiproviderId,epischedulerId,ccWard,ccID,leaveType,natProgramType,providerId,systemEntryDate,modifyDate,otherDec,remarks,status,upload)Values('" + workplanId1 + "','" + 2 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + Global.Left(spnEPICenterName.getSelectedItem().toString(), 2) + "','" + (spnProvider.getSelectedItemPosition() == 0 ? "" : Global.Left(spnProvider.getSelectedItem().toString(), 5)) + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" +""+"','" +1+ "','2')";
                        C.Save(SQL);
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkEPIVisit.isChecked() == false && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 2 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        C.Save("Delete from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 2 + "' and workPlanDate='" + g.getAreaDate() + "'");
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    }*/

                    //3 chkCCVisit
                    if (chkCCVisit.isChecked() == true && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 3 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkCCVisit.isChecked() == true && !C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 3 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        //SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + 3 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + Global.Left(spnEPICenterName.getSelectedItem().toString(), 2) + "','" + (spnProvider.getSelectedItemPosition() == 0 ? "" : Global.Left(spnProvider.getSelectedItem().toString(), 5)) + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "','2')";
                        //SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + 3 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" +"" + "','" + ""+ "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "','2')";
                        SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epiproviderId,epischedulerId,ccWard,ccID,leaveType,natProgramType,providerId,systemEntryDate,modifyDate,otherDec,remarks,status,upload)Values('" + workplanId1 + "','" + 3 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" +""+"','" +1+"','2')";
                        C.Save(SQL);
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkCCVisit.isChecked() == false && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 3 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        C.Save("Delete from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 3 + "' and workPlanDate='" + g.getAreaDate() + "'");
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    }

                    //4 chkOfficeWork
                    if (chkOfficeWork.isChecked() == true && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 4 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkOfficeWork.isChecked() == true && !C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 4 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + 4 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "','2')";
                        C.Save(SQL);
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkOfficeWork.isChecked() == false && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 4 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        C.Save("Delete from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 4 + "' and workPlanDate='" + g.getAreaDate() + "'");
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    }

                    //5 chkUHMMeeting
                    if (chkUHMMeeting.isChecked() == true && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 5 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkUHMMeeting.isChecked() == true && !C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 5 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                       // SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + 5 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "','2')";
                        SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epiproviderId,epischedulerId,ccWard,ccID,leaveType,natProgramType,providerId,systemEntryDate,modifyDate,otherDec,remarks,status,upload)Values('" + workplanId1 + "','" + 5 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" +""+"','" +1+"','2')";
                        C.Save(SQL);
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkUHMMeeting.isChecked() == false && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 5 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        C.Save("Delete from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 5 + "' and workPlanDate='" + g.getAreaDate() + "'");
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    }

                    //6 chkHDay
                    if (chkHDay.isChecked() == true && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 6 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        SQL = "Update workPlanDetail set ";
                        SQL += "leaveType = '" + (spnLeave.getSelectedItemPosition() == 0 ? "" : Global.Left(spnLeave.getSelectedItem().toString(), 1)) + "',";
                        SQL += "modifyDate = '" + Global.DateTimeNowYMDHMS() + "',";
                        SQL += "upload ='2'";
                        SQL += "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 6 + "' and workPlanDate='" + g.getAreaDate() + "'";
                        // SQL+=" where Cluster='"+ cluster +"' and Block='"+ block +"' and HH='"+ hh +"' and StudyID='"+ txtStudyId.getText() +"'and Visit='"+ txtVisitNo.getText()+"'";

                        C.Save(SQL);
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkHDay.isChecked() == true && !C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 6 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        //SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + 6 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + (spnLeave.getSelectedItemPosition() == 0 ? "" : Global.Left(spnLeave.getSelectedItem().toString(), 1)) + "','" + "" + "','" + g.getProvCode() + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "','2')";
                        SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epiproviderId,epischedulerId,ccWard,ccID,leaveType,natProgramType,providerId,systemEntryDate,modifyDate,otherDec,remarks,status,upload)Values('" + workplanId1 + "','" + 6 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + (spnLeave.getSelectedItemPosition() == 0 ? "" : Global.Left(spnLeave.getSelectedItem().toString(), 1)) + "','" + "" + "','" + g.getProvCode() + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" +""+"','" +1+ "','2')";
                        C.Save(SQL);
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkHDay.isChecked() == false && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 6 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        C.Save("Delete from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 6 + "' and workPlanDate='" + g.getAreaDate() + "'");
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    }

                    //7 chkMonthlyReportColl
                    if (chkMonthlyReportColl.isChecked() == true && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 7 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkMonthlyReportColl.isChecked() == true && !C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 7 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                       // SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + 7 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "','2')";
                        SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epiproviderId,epischedulerId,ccWard,ccID,leaveType,natProgramType,providerId,systemEntryDate,modifyDate,otherDec,remarks,status,upload)Values('" + workplanId1 + "','" + 7 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" +""+"','" +1+"','2')";
                        C.Save(SQL);
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkMonthlyReportColl.isChecked() == false && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 7 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        C.Save("Delete from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 7 + "' and workPlanDate='" + g.getAreaDate() + "'");
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    }

                    //8 chkMonthlyWorkPlan
                    if (chkMonthlyWorkPlan.isChecked() == true && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 8 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkMonthlyWorkPlan.isChecked() == true && !C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 8 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        //SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + 8 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "','2')";
                        SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epiproviderId,epischedulerId,ccWard,ccID,leaveType,natProgramType,providerId,systemEntryDate,modifyDate,otherDec,remarks,status,upload)Values('" + workplanId1 + "','" + 8 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" +""+"','" +1+"','2')";
                        C.Save(SQL);
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkMonthlyWorkPlan.isChecked() == false && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 8 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        C.Save("Delete from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 8 + "' and workPlanDate='" + g.getAreaDate() + "'");
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    }

                    //9 chkTrainingAttended
                    if (chkTrainingAttended.isChecked() == true && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 9 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkTrainingAttended.isChecked() == true && !C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 9 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        //SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + 9 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "','2')";
                        SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epiproviderId,epischedulerId,ccWard,ccID,leaveType,natProgramType,providerId,systemEntryDate,modifyDate,otherDec,remarks,status,upload)Values('" + workplanId1 + "','" + 9 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" +""+"','" +1+"','2')";
                        C.Save(SQL);
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkTrainingAttended.isChecked() == false && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 9 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        C.Save("Delete from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 9 + "' and workPlanDate='" + g.getAreaDate() + "'");
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    }

                    //10 chkNationalProgramAttended
                    if (chkNationalProgramAttended.isChecked() == true && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 10 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        SQL = "Update workPlanDetail set ";
                        SQL += "natProgramType = '" + (spnNProgram.getSelectedItemPosition() == 0 ? "" : Global.Left(spnNProgram.getSelectedItem().toString(), 1)) + "',";
                        SQL += "modifyDate = '" + Global.DateTimeNowYMDHMS() + "',";
                        SQL += "upload ='2'";
                        SQL += "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 10 + "' and workPlanDate='" + g.getAreaDate() + "'";
                        // SQL+=" where Cluster='"+ cluster +"' and Block='"+ block +"' and HH='"+ hh +"' and StudyID='"+ txtStudyId.getText() +"'and Visit='"+ txtVisitNo.getText()+"'";

                        C.Save(SQL);

                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkNationalProgramAttended.isChecked() == true && !C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 10 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        //SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + 10 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + (spnNProgram.getSelectedItemPosition() == 0 ? "" : Global.Left(spnNProgram.getSelectedItem().toString(), 1)) + "','" + g.getProvCode() + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "','2')";
                        SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epiproviderId,epischedulerId,ccWard,ccID,leaveType,natProgramType,providerId,systemEntryDate,modifyDate,otherDec,remarks,status,upload)Values('" + workplanId1 + "','" + 10 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + (spnNProgram.getSelectedItemPosition() == 0 ? "" : Global.Left(spnNProgram.getSelectedItem().toString(), 1)) + "','" + g.getProvCode() + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" +""+"','" +1+ "','2')";
                        C.Save(SQL);
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkNationalProgramAttended.isChecked() == false && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 10 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        C.Save("Delete from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 10 + "' and workPlanDate='" + g.getAreaDate() + "'");
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    }

                    //11 chkOtherProgram
                    if (chkOtherProgram.isChecked() == true && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 11 + "' and workPlanDate='" + g.getAreaDate() + "'")) {
                        SQL = "Update workPlanDetail set ";
                        SQL += "otherDec = '" + (chkOtherProgram.isChecked() ? (txtOther.getText().toString()) : "") + "',";
                        SQL += "modifyDate = '" + Global.DateTimeNowYMDHMS() + "',";
                        SQL += "upload ='2'";
                        SQL += "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 11 + "' and workPlanDate='" + g.getAreaDate() + "'";
                        C.Save(SQL);
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkOtherProgram.isChecked() == true && !C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 11 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                       // SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + 11 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "','2')";
                        SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,status,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + 11 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + 1 + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + (chkOtherProgram.isChecked() ? (txtOther.getText().toString()) : "") + "','" + "','2')";
                        C.Save(SQL);
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkOtherProgram.isChecked() == false && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 11 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        C.Save("Delete from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 11 + "' and workPlanDate='" + g.getAreaDate() + "'");
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    }

                }


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
                cmdB2.setTextColor(Color.BLACK);
                cmdB2.setTextSize(20);
                cmdB2.setText("সাপ্তাহিক ছুটি");
                cmdB2.setEnabled(false);
                cmdB2.setBackgroundColor(Color.parseColor("#D7D7D7"));
                // cmdB2.setBackgroundColor(Color.parseColor("#ff00ff"));
                // workPlanDate.setBackgroundColor(Color.parseColor("#99cc33"));
                // cmdB2.setBackgroundColor(Color.BLUE);
            }
/*
            else  if(g.getDay(o.get("workPlanDate")+'/'+o.get("Month")+'/'+o.get("Year")).equalsIgnoreCase("শনিবার"))
            {
                cmdB2.setTextColor(Color.BLACK);
                cmdB2.setTextSize(20);
                cmdB2.setText("সাপ্তাহিক ছুটি");
                cmdB2.setEnabled(false);
                cmdB2.setBackgroundColor(Color.parseColor("#ff00ff"));
                // workPlanDate.setBackgroundColor(Color.parseColor("#99cc33"));
                // cmdB2.setBackgroundColor(Color.BLUE);
            }*/

            else {
                cmdB2.setTextColor(Color.BLACK);
                cmdB2.setTextSize(20);
                //cmdB2.setBackgroundColor(Color.parseColor("#FFFFFF"));
                cmdB2.setBackgroundColor(Color.BLUE);
                cmdB2.setEnabled(true);
                cmdB2.setText("কর্মসূচী সংযোজন");
            }

            FPADate.addAll(C.getDataList("Select strftime('%d/%m/%Y', date(workPlanDate)) wdate from workPlanDetail where  providerId='" + g.getProvCode() + "' and item<=11 group by workPlanDate"));
            //  }

            for (String temp : FPADate) {

                String a = o.get("workPlanDate") + '/' + o.get("Month") + '/' + o.get("Year");
                //map.put("cdate", temp);
                if (a.equals(temp)) {
                    cmdB2.setTextColor(Color.BLACK);
                    cmdB2.setTextSize(20);

                    cmdB2.setText("সম্পন্ন");
                    // cmdB2.setEnabled(false);
                    cmdB2.setBackgroundColor(Color.parseColor("#99cc33"));
                    // workPlanDate.setBackgroundColor(Color.parseColor("#99cc33"));
                    // cmdB2.setBackgroundColor(Color.BLUE);
                } else {

                }
            }


            final AlertDialog.Builder adb = new AlertDialog.Builder(AHIWorkPlaning.this);


            cmdB2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    g.setAreaUnitValue(Global.Left(txtFpiWarea.getText().toString(), 2));
                    g.setAreaDate(o.get("Year") + "-" + o.get("Month") + '-' + o.get("workPlanDate"));
                    g.setAreaDateM(o.get("Year") + "-" + o.get("Month"));


                   // String WorkPlainvalue =C.ReturnSingleValue("Select ifnull(status, '' ) AS status FROM workPlanMaster where providerId='"+g.getProvCode()+"' and month='"+o.get("Year") + "-" + o.get("Month")+"'");
                    String WorkPlainvalue =C.ReturnSingleValue("Select ifnull(A.status, '' ) AS status FROM workPlanMaster A INNER JOIN workPlanDetail B ON A.workPlanId = B.workPlanId and B.providerId=A.providerId where A.providerId='"+g.getProvCode()+"' and A.month='"+o.get("Year") + "-" + o.get("Month")+"'");
                    if(WorkPlainvalue.equals("3")||WorkPlainvalue.equals(""))
                    {
                        DisplayAHIWorkPlaining();
                    }
                    else if(WorkPlainvalue.equals("2"))
                    {
                        adb.setTitle("workplan");
                        adb.setMessage("Work plan  অনুমোদন হওয়ার  পর পরিবর্তন করে যাবে না ।");
                        adb.setNegativeButton("OK", null);
                        adb.show();
                    }
                    else
                    {
                        DisplayAHIWorkPlaining();
                    }

                    // DisplayFPIWorkPlain();


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