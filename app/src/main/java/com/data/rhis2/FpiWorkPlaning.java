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
 * Created by Nisan on 4/6/2016.
 */
public class FpiWorkPlaning extends Activity {
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
        AlertDialog.Builder adb = new AlertDialog.Builder(FpiWorkPlaning.this);
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
                Intent intentforview = new Intent(getApplicationContext(), FpiWorkPlaningView.class);
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
    ArrayList<String> FPADate = new ArrayList<String>();
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
            setContentView(R.layout.fpiworkplaning);
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


            lblHS10.setText("পরিবার পরিকল্পনা পরিদর্শকের মাসিক অগ্রিম কর্মসূচী");


            secFPIPMonth = (LinearLayout) findViewById(R.id.secFPIPMonth);
            VlblFPIPMonth = (TextView) findViewById(R.id.VlblFPIPMonth);
            spnFPIPMonth = (Spinner) findViewById(R.id.spnFPIPMonth);
            // spnFPIPMonth.setAdapter(C.getArrayAdapterMultiline("SELECT strftime('%Y', date('now')) ||','||mName||':-'||substr('0' ||id, -2, 2) as ym from month  where id <=(SELECT strftime('%m','now')+1) order by id Desc"));
            //spnFPIPMonth.setAdapter(C.getArrayAdapterMultiline("SELECT substr('0' ||id, -2, 2)||'-'||mName||','||strftime('%Y', date('now'))  as ym from month  where id <=(SELECT strftime('%m','now')+1) order by id Desc"));
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
            Connection.MessageBox(FpiWorkPlaning.this, e.getMessage());
            return;
        }
    }


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
                // map.put("WorkDate", String.valueOf(C.ReturnSingleValue("SELECT strftime('%d/%m/%Y', date(workPlanDate)) from workPlanDetail where workPlanDate='"+String.valueOf(Global.Left(spnFPIPMonth.getSelectedItem().toString(),4))+"-"+String.valueOf(Global.Right(spnFPIPMonth.getSelectedItem().toString(),2))+"-"+String.valueOf(i)+"'")));
                map.put("itemdes", "");


                dataList.add(map);

                dataAdapter = new SimpleAdapter(FpiWorkPlaning.this, dataList, R.layout.fpaworkplanrow, new String[]{"edit", "delete"},
                        new int[]{R.id.cmdB1, R.id.cmdB2});

                list.setAdapter(new DataListAdapter(this));
            }
        } catch (Exception e) {
            Connection.MessageBox(FpiWorkPlaning.this, e.getMessage());
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
        SQL += " where month='" + g.getAreaDateM() + "' and providerId='" + g.getProvCode()  + "'";//+ "' and workAreaId='" + g.getAreaUnitValue()
        String SNo = C.ReturnSingleValue(SQL);
        // Serial No auto increment
        return SNo;
    }

    /* private String GetChildWorkPlanId(String parentWorkPlanId)
     {
         String SQL = "";

         SQL = "select (ifnull(MAX([cWorkPlanId]),'')+1) AS workPlanId from workPlanDetail WHERE workPlanId = '" + parentWorkPlanId +"'";
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


    public void DeleteFPAItem(String item) {
        try {


            String SQL = "";

            if (C.Existence("Select workPlanId, providerId,workPlanDate,item from workPlanDetail where workPlanId='" + GetParentWorkPlanId1() + "' and providerId='" + g.getProvCode() + "' and workPlanDate='" + g.getAreaDate() + "' and item= '" + item + "'"))

            {

                SQL = "Delete from workPlanDetail where workPlanId='" + GetParentWorkPlanId1() + "' and providerId='" + g.getProvCode() + "' and item= '" + item + "' and workPlanDate='" + g.getAreaDate() + "'";
                C.Save(SQL);

            } else {

            }


        } catch (Exception e) {
            Connection.MessageBox(FpiWorkPlaning.this, e.getMessage());
            return;
        }

    }

    OptionsFPI op = new OptionsFPI();

    private void DisplayFPIWorkPlain() {
        final Dialog dialog = new Dialog(FpiWorkPlaning.this);
        dialog.setTitle("FWI ওয়ার্কপ্ল্যান");
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fpimultiplechoice);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        final TextView lblHlblepireg = (TextView) dialog.findViewById(R.id.lblHlblepireg);

        lblHlblepireg.setText(Global.DateConvertDMY(g.getAreaDate()) + ":-" + "কর্মসূচী সমূহ");

        LinearLayout checkBoxHolder = (LinearLayout) dialog.findViewById(R.id.checkBoxHolder);
        checkBoxHolder.removeAllViews();
        LinearLayout.LayoutParams lnlParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams layoutParamForcheck = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        DisplayMetrics dm;
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

       /* LinearLayout.LayoutParams layoutParamForTextBox = new LinearLayout.LayoutParams(
                (dm.widthPixels / 3), LinearLayout.LayoutParams.WRAP_CONTENT);
*/

        final ArrayList<Integer> checkedOptions = new ArrayList<Integer>();


        String sql = "SELECT\n" +
                "                ifnull( itemCode, '' ) AS itemCode,\n" +
                "                ifnull( itemDes, '' ) AS itemDes, type \n" +
                "\n" +
                "                FROM fpaItem where \n" +
                "                type='2'";


       /* sql = sql + " UNION ALL\n" +
                "                SELECT\n" +
                "\n" +
                "                ifnull( cM.generatedId, '' ) AS HealthID,\n" +
                "                ifnull( cM.name, '' ) AS NameEng, '' AS hidDistributed\n" +
                "\n" +
                "                FROM clientMap cM\n" +
                "                LEFT JOIN ProviderDB pd\n" +
                "                ON cM.providerId = pd.ProvCode\n" +
                "                LEFT JOIN death d\n" +
                "                ON cM.generatedId = d.healthId\n" +
                "                WHERE cM.healthId NOT IN (\n" +
                "                        SELECT healthId\n" +
                "                        FROM Member\n" +
                "                        WHERE HHNo='" + g.getHouseholdNo() + "'\n" +
                "                )\n" +
                "\n" +
                "                and cM.zillaId='" + g.getDistrict() + "' and cM.upazilaId='" + g.getUpazila() + "' and cM.unionId='" + g.getUnion() +
                "' and cM.mouzaId='" + g.getMouza() + "' and cM.villageId='" + g.getVillage() + "' and cM.houseGrHoldingNo='" + g.getHouseholdNo() + "'";
*/
        Cursor cur = C.ReadData(sql);


        cur.moveToFirst();
        int index_of_hid = 0;
        op = new OptionsFPI();
        while (!cur.isAfterLast()) {

            op.codeList.add(index_of_hid);
            op.FPIItemList.add(cur.getString(cur.getColumnIndex("itemCode")));
            op.FPIItemDesList.add(cur.getString(cur.getColumnIndex("itemDes")));
            cur.moveToNext();
            index_of_hid = index_of_hid + 1;
        }

        cur.close();

        Collections.reverse(op.codeList);
        Collections.reverse(op.FPIItemDesList);
        Collections.reverse(op.FPIItemList);

        for (int i = 0; i < op.codeList.size(); i++) {
            checkedOptions.add(-1);
        }

        for (int i = 0; i < op.codeList.size(); i++) {
            final LinearLayout ln = new LinearLayout(this);
            final CheckBox checkButton = new CheckBox(this);
            ln.setOrientation(LinearLayout.HORIZONTAL);
            // ln.setShowDividers(LinearLayout.HORIZONTAL);
            ln.setPadding(0, 12, 0, 0);
            checkButton.setText(op.FPIItemDesList.get(i));
            String a = op.FPIItemList.get(i);
            //op.FPIItemList.get(i) + "   " +
            // checkButton.setText(op.capEngList.get(i) + "   " + op.HealthidList.get(i));
            checkButton.setId(op.codeList.get(i));
            ln.setId(i);
            ln.addView(checkButton, 0, layoutParamForcheck);
            checkBoxHolder.addView(ln, 0, lnlParams);

            checkButton
                    .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        public void onCheckedChanged(CompoundButton buttonView,
                                                     boolean isChecked) {
                            // TODO Auto-generated method stub
                            if (isChecked) {
                                checkedOptions.set(ln.getId(), 1);
                            } else {
                                checkedOptions.set(ln.getId(), -1);

                            }
                        }
                    });

        /*    String value_of_hid_distribution = C.ReturnSingleValue("SELECT hidDistributed FROM Member  WHERE  HealthId = '" + *//*op.HealthidList.get(i)*//* ""+ "'");
            if (!value_of_hid_distribution.equals("null") || !value_of_hid_distribution.equals(null)) {
                if(value_of_hid_distribution .equalsIgnoreCase("1"))
                    checkButton.setChecked(true);
            } else {
                checkButton.setChecked(false);
            }*/
        }


        Button saveNxtButton = (Button) dialog.findViewById(R.id.buttonSave);

        saveNxtButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                /*AlertDialog.Builder adb = new AlertDialog.Builder(MemberList.this);
                adb.setTitle("Close");
                adb.setMessage("আপনি কি নিশ্চিত?");
                adb.setNegativeButton("না", null);
                adb.setPositiveButton("হ্যাঁ", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
*/
                for (int i = 0; i < op.codeList.size(); i++) {


                    String sql = "";
                   /* if (checkedOptions.get(i) == 1) {
                        sql = "UPDATE Member SET Upload = '2',  hidDistributed = '1', hidDistributionDate = '" + Global.DateTimeNowYMDHMS() + "'  WHERE  HealthId = '";
                      //  sql += String.valueOf(op.HealthidList.get(i)) + "'";
                        C.Save(sql);
                    }
                    else if(checkedOptions.get(i) == -1) {
                        sql = "UPDATE Member SET Upload = '2',  hidDistributed = '2', hidDistributionDate = ''  WHERE  HealthId = '";
                       // sql += String.valueOf(op.HealthidList.get(i)) + "'";
                        C.Save(sql);
                    }*/
                }

                Toast.makeText(getApplicationContext(), "তথ্য সফলভাবে সংরক্ষণ হয়েছে।", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
              /*  });
                adb.show();*/

            // }


        });

        Button closeButton = (Button) dialog.findViewById(R.id.btnClose);

        closeButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                dialog.dismiss();
            }

        });

        dialog.show();
    }

    private void DisplayFPIWorkPlaining() {
        final Dialog popupView = new Dialog(FpiWorkPlaning.this);
        popupView.setTitle("FWI ওয়ার্কপ্ল্যান");

        popupView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popupView.setContentView(R.layout.fpiworkplainingpopup);
        popupView.setCancelable(true);
        popupView.setCanceledOnTouchOutside(true);

        final TextView lblHlblepireg = (TextView) popupView.findViewById(R.id.lblHlblepireg);

        lblHlblepireg.setText(Global.DateConvertDMY(g.getAreaDate()) + ":-" + "কর্মসূচী সমূহ");

        List<String> fpiothermeeting = new ArrayList<String>();

        fpiothermeeting.add("");
        fpiothermeeting.add("1-তথ্য");
        fpiothermeeting.add("2-শিক্ষা ও যোগাযোগ বিষয়ক সভা");
        fpiothermeeting.add("3-স্বাস্থ্য শিক্ষা ও পুষ্টি বিষয়ক কার্যক্রম");
        fpiothermeeting.add("4-বৃক্ষরোপণ কার্যক্রম");
        fpiothermeeting.add("5-উদ্ভুদ্ধকরন কার্যক্রম");
        fpiothermeeting.add("6-সচেতনতা মূলক কার্যক্রম");
        fpiothermeeting.add("7-বিভিন্ন পর্যায় ও সংস্থার প্রতিনিধিদের সাথে সমন্বয় বিষয়ক কার্যক্রম");


        final Spinner spnfpiothermeeting = (Spinner) popupView.findViewById(R.id.spnfpiothermeeting);
        ArrayAdapter<String> fpiothermeetingadptr = new ArrayAdapter<String>(this, R.layout.multiline_spinner_dropdown_item, fpiothermeeting);
        spnfpiothermeeting.setAdapter(fpiothermeetingadptr);

        List<String> leaves = new ArrayList<String>();


        leaves.add("");
        leaves.add("1-বাৎসরিক");
        leaves.add("2-অসুস্থতা জনিত");
        leaves.add("3-মাতৃত্বকালীন");
        leaves.add("4-পিতৃত্ব কালীন ");
        leaves.add("5-নৈমিত্তিক");
        leaves.add("6-শ্রান্তি ও বিনোদন");
        leaves.add("7-ঐচ্ছিক  ");
        leaves.add("8-সরকারী  ");
        leaves.add("9-অন্যান্য");


        final Spinner spnLeave = (Spinner) popupView.findViewById(R.id.spnLeave);
        ArrayAdapter<String> leaveadptr = new ArrayAdapter<String>(this, R.layout.multiline_spinner_dropdown_item, leaves);
        spnLeave.setAdapter(leaveadptr);
        final LinearLayout secF2 = (LinearLayout) popupView.findViewById(R.id.secF2);
        secF2.setVisibility(View.GONE);
        String Type = "";
        if (g.getProvType().equals("10")) {
            Type = "3";
        }

        final Spinner spnProvider = (Spinner) popupView.findViewById(R.id.spnProvider);
        spnProvider.setAdapter(C.getArrayAdapter("Select '-' provName from ProviderDb Union Select provCode||'-'||provName from ProviderDb WHERE provType='" + Type + "'"));
        final LinearLayout secF3 = (LinearLayout) popupView.findViewById(R.id.secF3);
        secF3.setVisibility(View.GONE);
        final Spinner spnWork = (Spinner) popupView.findViewById(R.id.spnWork);

        spnProvider.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (spnProvider.getSelectedItemPosition() > 0) {

                    spnWork.setAdapter(C.getArrayAdapterMultiline("SELECT \n" +
                            " --B.workPlanDate AS workPlanDate,\n" +
                            "group_concat(\n" +
                            "( CASE\n" +
                            "WHEN CAST ( C.itemcode AS int ) = 1 THEN C.itemdes || '-' || substr( B.unitNo, 4, 7 ) || '-গ্রাম:' ||( \n" +
                            "SELECT VILLAGENAME\n" +
                            "FROM Village\n" +
                            "WHERE MOUZAID IN ( \n" +
                            "SELECT substr( B.village, 1, 3 )\n" +
                            "FROM workPlanDetail \n" +
                            " ) \n" +
                            " \n" +
                            ") \n" +
                            " || ' ,দম্পতি নম্বর:(' || B.elcoFrom || '-' || B.elcoTo || ')'\n" +
                            "   WHEN CAST ( C.itemcode AS int ) = 9 \n" +
                            " AND\n" +
                            " B.leaveType = 1 THEN C.itemdes || ':বাৎসরিক' \n" +
                            " WHEN CAST ( C.itemcode AS int ) = 9 \n" +
                            " AND\n" +
                            "B.leaveType = 2 THEN C.itemdes || ':অসুস্থতা জনিত' \n" +
                            " WHEN CAST ( C.itemcode AS int ) = 9 \n" +
                            " AND\n" +
                            "B.leaveType = 3 THEN C.itemdes || ':মাতৃত্বকালীন' \n" +
                            " WHEN CAST ( C.itemcode AS int ) = 9 \n" +
                            " AND\n" +
                            "B.leaveType = 4 THEN C.itemdes || ':পিতৃত্ব কালীন ' \n" +
                            " WHEN CAST ( C.itemcode AS int ) = 9 \n" +
                            " AND\n" +
                            "B.leaveType = 5 THEN C.itemdes || ':নৈমিত্তিক ' \n" +
                            " WHEN CAST ( C.itemcode AS int ) = 9 \n" +
                            " AND\n" +
                            "B.leaveType = 6 THEN C.itemdes || ':শ্রান্তি ও বিনোদন' \n" +
                            " WHEN CAST ( C.itemcode AS int ) = 9 \n" +
                            " AND\n" +
                            "B.leaveType = 7 THEN C.itemdes || ':ঐচ্ছিক ' \n" +
                            "WHEN CAST ( C.itemcode AS int ) = 9 \n" +
                            " AND\n" +
                            "B.leaveType = 8 THEN C.itemdes || ':সরকারী ' \n" +
                            "\n" +
                            "ELSE ifnull( C.itemdes, '' ) \n" +
                            " END )) AS itemdes\n" +
                            "FROM workPlanMaster A\n" +
                            "INNER JOIN workPlanDetail B\n" +
                            "ON A.workPlanId = B.workPlanId\n" +
                            "INNER JOIN fpaItem C\n" +
                            "ON B.item = C.itemcode\n" +
                            "WHERE C.type = '1'\n" +
                            "AND B.providerId='" + Integer.valueOf(Global.Left(spnProvider.getSelectedItem().toString(), 5)) + "'\n" +
                            " AND\n" +
                            "strftime('%d/%m/%Y', date(B.workPlanDate))='" + Global.DateConvertDMY(g.getAreaDate()) + "'group by B.workPlanDate"));

                   /* spnWork.setAdapter(C.getArrayAdapterMultiline("Select '  ' VILLAGENAME from Village union select substr('0' ||v.MOUZAID, -3, 3)||v.VILLAGEID||'-'||v.VILLAGENAME as VILLAGENAME from Village v\n" +
                            "left outer join ProviderArea p on v.ZILLAID=p.zillaid and  v.UPAZILAID=p.upazilaid and\n" +
                            " v.MOUZAID||v.VILLAGEID=p.mouzaid||p.villageid where p.Ward='"+Global.Left(spnProvider.getSelectedItem().toString(),5)+"'"));
*/
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        final LinearLayout secLeave = (LinearLayout) popupView.findViewById(R.id.secLeave);
        final LinearLayout secOther = (LinearLayout) popupView.findViewById(R.id.secOther);
        final LinearLayout secfpiothermeeting = (LinearLayout) popupView.findViewById(R.id.secfpiothermeeting);
        final EditText txtOther = (EditText) popupView.findViewById(R.id.txtOther);
        final CheckBox chkfpiAdvprog = (CheckBox) popupView.findViewById(R.id.chkfpiAdvprog);
        final CheckBox chkfpiapproved = (CheckBox) popupView.findViewById(R.id.chkfpiapproved);
        final CheckBox chkfpiSatTime = (CheckBox) popupView.findViewById(R.id.chkfpiSatTime);
        final CheckBox chkfpicampTime = (CheckBox) popupView.findViewById(R.id.chkfpicampTime);
        final CheckBox chkfpihelpfpa = (CheckBox) popupView.findViewById(R.id.chkfpihelpfpa);
        final CheckBox chkfpiVisitfpa = (CheckBox) popupView.findViewById(R.id.chkfpiVisitfpa);
        final CheckBox chkfpiVerifydatafpa = (CheckBox) popupView.findViewById(R.id.chkfpiVerifydatafpa);
        final CheckBox chkfpiepihelp = (CheckBox) popupView.findViewById(R.id.chkfpiepihelp);
        final CheckBox chkfpiSatelait = (CheckBox) popupView.findViewById(R.id.chkfpiSatelait);
        final CheckBox chkfpiunmeeting = (CheckBox) popupView.findViewById(R.id.chkfpiunmeeting);
        final CheckBox chkfpiuzmeeting = (CheckBox) popupView.findViewById(R.id.chkfpiuzmeeting);
        final CheckBox chkfpiothermeeting = (CheckBox) popupView.findViewById(R.id.chkfpiothermeeting);
        final CheckBox chkfpiunmeeting1 = (CheckBox) popupView.findViewById(R.id.chkfpiunmeeting1);
        final CheckBox chkHDay = (CheckBox) popupView.findViewById(R.id.chkHDay);
        final CheckBox chkMreport = (CheckBox) popupView.findViewById(R.id.chkMreport);
        final CheckBox chkTraining = (CheckBox) popupView.findViewById(R.id.chkTraining);

        final CheckBox chkUFCMeeting = (CheckBox) popupView.findViewById(R.id.chkUFCMeeting);
        // final CheckBox chkUHFPMeeting1 = (CheckBox) popupView.findViewById(R.id.chkUHFPMeeting1);
        // final CheckBox chkUHFPMeeting2 = (CheckBox) popupView.findViewById(R.id.chkUHFPMeeting2);
        final CheckBox chkROther = (CheckBox) popupView.findViewById(R.id.chkROther);
        final Button cmdSave = (Button) popupView.findViewById(R.id.cmdSave);

        chkfpiothermeeting.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (chkfpiothermeeting.isChecked()) {
                    secfpiothermeeting.setVisibility(View.VISIBLE);


                } else {

                    secfpiothermeeting.setVisibility(View.GONE);
                    spnfpiothermeeting.setSelection(0);


                }
            }
        });

        chkfpiVisitfpa.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (chkfpiVisitfpa.isChecked()) {
                    secF2.setVisibility(View.VISIBLE);
                    secF3.setVisibility(View.VISIBLE);


                } else {

                    secF2.setVisibility(View.GONE);
                    secF3.setVisibility(View.GONE);

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

        chkROther.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (chkROther.isChecked()) {
                    secOther.setVisibility(View.VISIBLE);


                } else {

                    secOther.setVisibility(View.GONE);
                    txtOther.setText("");


                }
            }
        });
        popupView.show();

        secfpiothermeeting.setVisibility(View.GONE);
        secLeave.setVisibility(View.GONE);
        secOther.setVisibility(View.GONE);

        String workplanId1 = GetParentWorkPlanId1();

        String SQL = "";
        SQL = "select " +
                " ifnull(MAX(CASE WHEN item=50 THEN item END),'' )As P50,ifnull(MAX(CASE WHEN item=51 THEN item END),'' )As P51 ," +
                " ifnull(MAX(CASE WHEN item=52 THEN item END),'' )As P52,ifnull(MAX(CASE WHEN item=53 THEN item END),'' )As P53 ," +
                " ifnull(MAX(CASE WHEN item=54 THEN item END),'' )As P54,ifnull(MAX(CASE WHEN item=55 THEN item END),'' )As P55 ," +
                " ifnull(MAX(CASE WHEN item=56 THEN item END),'' )As P56,ifnull(MAX(CASE WHEN item=57 THEN item END),'' )As P57 ," +
                " ifnull(MAX(CASE WHEN item=58 THEN item END),'' )As P58,ifnull(MAX(CASE WHEN item=59 THEN item END),'' )As P59 ," +
                " ifnull(MAX(CASE WHEN item=60 THEN item END),'' )As P60,ifnull(MAX(CASE WHEN item=61 THEN item END),'' )As P61 ," +
                " ifnull(MAX(CASE WHEN item=62 THEN item END),'' )As P62,ifnull(MAX(CASE WHEN item=63 THEN item END),'' )As P63 ," +
                " ifnull(MAX(CASE WHEN item=64 THEN item END),'' )As P64,ifnull(MAX(CASE WHEN item=65 THEN item END),'' )As P65,ifnull(MAX(CASE WHEN item=66 THEN item END),'' )As P66 ," +
                " ifnull(MAX(CASE WHEN item=67 THEN item END),'' )As P67,ifnull(MAX(CASE WHEN item=68 THEN item END),'' )As P68,ifnull(MAX(CASE WHEN item=69 THEN item END),'' )As P69 " +
                " FROM workPlanDetail where  providerId='" + g.getProvCode() + "' and workPlanDate='" + g.getAreaDate() + "'";

        if (C.Existence(SQL)) {
            Cursor cur = C.ReadData(SQL);

            cur.moveToFirst();
            while (!cur.isAfterLast()) {

                if (cur.getString(cur.getColumnIndex("P50")).equals("50")) {
                    chkfpiAdvprog.setChecked(true);
                } else {
                    chkfpiAdvprog.setChecked(false);


                }

                if (cur.getString(cur.getColumnIndex("P51")).equals("51")) {
                    chkfpiapproved.setChecked(true);
                } else {
                    chkfpiapproved.setChecked(false);

                }

                if (cur.getString(cur.getColumnIndex("P52")).equals("52")) {
                    chkfpiSatTime.setChecked(true);
                } else {
                    chkfpiSatTime.setChecked(false);

                }

                if (cur.getString(cur.getColumnIndex("P53")).equals("53")) {
                    chkfpicampTime.setChecked(true);
                } else {
                    chkfpicampTime.setChecked(false);

                }

                if (cur.getString(cur.getColumnIndex("P54")).equals("54")) {
                    chkfpihelpfpa.setChecked(true);
                } else {
                    chkfpihelpfpa.setChecked(false);


                }
                if (cur.getString(cur.getColumnIndex("P55")).equals("55")) {
                    chkfpiVisitfpa.setChecked(true);
                } else {
                    chkfpiVisitfpa.setChecked(false);


                }

                if (cur.getString(cur.getColumnIndex("P56")).equals("56")) {
                    chkfpiVerifydatafpa.setChecked(true);


                } else {
                    chkfpiVerifydatafpa.setChecked(false);
                }
                if (cur.getString(cur.getColumnIndex("P57")).equals("57")) {
                    chkfpiepihelp.setChecked(true);
                } else {
                    chkfpiepihelp.setChecked(false);


                }

                if (cur.getString(cur.getColumnIndex("P58")).equals("58")) {
                    chkfpiSatelait.setChecked(true);
                } else {
                    chkfpiSatelait.setChecked(false);


                }

                if (cur.getString(cur.getColumnIndex("P59")).equals("59")) {
                    chkfpiunmeeting.setChecked(true);
                } else {
                    chkfpiunmeeting.setChecked(false);


                }


                //New Add

                if (cur.getString(cur.getColumnIndex("P66")).equals("66")) {
                    chkfpiunmeeting1.setChecked(true);
                } else {
                    chkfpiunmeeting1.setChecked(false);


                }
                if (cur.getString(cur.getColumnIndex("P60")).equals("60")) {
                    chkfpiuzmeeting.setChecked(true);
                } else {
                    chkfpiuzmeeting.setChecked(false);


                }

                if (cur.getString(cur.getColumnIndex("P61")).equals("61")) {
                    chkfpiothermeeting.setChecked(true);
                    if (chkfpiothermeeting.isChecked()) {

                        secfpiothermeeting.setVisibility(View.VISIBLE);
                        Cursor cur1 = C.ReadData("select natProgramType FROM workPlanDetail where item=61 and workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and workPlanDate='" + g.getAreaDate() + "'");
                        cur1.moveToFirst();
                        while (!cur1.isAfterLast()) {


                            spnfpiothermeeting.setSelection(Global.SpinnerItemPosition(spnfpiothermeeting, 1, cur1.getString(cur1.getColumnIndex("natProgramType"))));

                            cur1.moveToNext();
                        }
                        cur1.close();
                    }
                } else {
                    chkfpiothermeeting.setChecked(false);


                }
                if (cur.getString(cur.getColumnIndex("P62")).equals("62")) {
                    chkHDay.setChecked(true);

                    if (chkHDay.isChecked()) {

                        secLeave.setVisibility(View.VISIBLE);


                        Cursor cur1 = C.ReadData("select leaveType FROM workPlanDetail where item=62 and workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and workPlanDate='" + g.getAreaDate() + "'");
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

                if (cur.getString(cur.getColumnIndex("P63")).equals("63")) {
                    chkMreport.setChecked(true);
                } else {
                    chkMreport.setChecked(false);


                }

                if (cur.getString(cur.getColumnIndex("P64")).equals("64")) {
                    chkTraining.setChecked(true);
                } else {
                    chkTraining.setChecked(false);


                }

                if (cur.getString(cur.getColumnIndex("P65")).equals("65")) {
                    chkROther.setChecked(true);

                    if (chkROther.isChecked()) {

                        secOther.setVisibility(View.VISIBLE);

                        Cursor cur1 = C.ReadData("select otherDec  FROM workPlanDetail where item=65 and workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and workPlanDate='" + g.getAreaDate() + "'");
                        cur1.moveToFirst();
                        while (!cur1.isAfterLast()) {
                            txtOther.setText(cur1.getString(cur1.getColumnIndex("otherDec")));


                            cur1.moveToNext();
                        }
                        cur1.close();


                    }
                } else {
                    chkROther.setChecked(false);


                }

                if (cur.getString(cur.getColumnIndex("P67")).equals("67")) {
                    chkUFCMeeting.setChecked(true);

                } else {
                    chkUFCMeeting.setChecked(false);


                }
              /*  if (cur.getString(cur.getColumnIndex("P68")).equals("68")) {
                    chkUHFPMeeting1.setChecked(true);

                } else {
                    chkUHFPMeeting1.setChecked(false);


                }
                if (cur.getString(cur.getColumnIndex("P69")).equals("69")) {
                    chkUHFPMeeting2.setChecked(true);

                } else {
                    chkUHFPMeeting2.setChecked(false);


                }*/

                cur.moveToNext();
            }
            cur.close();


        } else {

        }
        cmdSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String SQL = "";
                //workAreaId='" + g.getAreaUnitValue() + "' AND
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
                    if (chkfpiAdvprog.isChecked() == true) {

                        FPAValue.add("50");
                    }


                    if (chkfpiapproved.isChecked() == true) {

                        FPAValue.add("51");
                    }
                    if (chkfpiSatTime.isChecked() == true) {

                        FPAValue.add("52");
                    }
                    if (chkfpicampTime.isChecked() == true) {

                        FPAValue.add("53");
                    }


                    if (chkfpihelpfpa.isChecked() == true) {

                        FPAValue.add("54");
                    }


                    if (chkfpiVisitfpa.isChecked() == true) {

                        FPAValue.add("55");
                    }


                    if (chkfpiVerifydatafpa.isChecked() == true) {

                        FPAValue.add("56");
                    }


                    if (chkfpiepihelp.isChecked() == true) {

                        FPAValue.add("57");
                    }


                    if (chkfpiSatelait.isChecked() == true) {

                        FPAValue.add("58");
                    }

                    if (chkfpiunmeeting.isChecked() == true) {

                        FPAValue.add("59");
                    }

                    //New Add
                    if (chkfpiunmeeting1.isChecked() == true) {

                        FPAValue.add("66");
                    }

                    if (chkfpiuzmeeting.isChecked() == true) {

                        FPAValue.add("60");
                    }


                    if (chkfpiothermeeting.isChecked() == true) {

                        FPAValue.add("61");
                    }


                    if (chkHDay.isChecked() == true) {
                        //SymtomValue.add(1);

                        if (spnLeave.getSelectedItemPosition() == 0) {
                            Connection.MessageBox(FpiWorkPlaning.this, "ছুটি :ধরন সিলেক্ট করুন।");
                            // dtpDeathDT.requestFocus();
                            return;
                        } else {
                            FPAValue.add("62");
                        }

                    }

                    if (chkMreport.isChecked() == true) {
                        //SymtomValue.add(1);
                        FPAValue.add("63");
                    }

                    if (chkTraining.isChecked() == true) {
                        //SymtomValue.add(1);
                        FPAValue.add("64");
                    }


                    if (chkROther.isChecked() == true) {
                        //SymtomValue.add(1);
                        FPAValue.add("65");
                    }


                    if (chkUFCMeeting.isChecked() == true) {
                        //SymtomValue.add(1);
                        FPAValue.add("67");
                    }

/*
                    if(chkUHFPMeeting1.isChecked()==true)
                    {
                        //SymtomValue.add(1);
                        FPAValue.add("68");
                    }

                    if(chkUHFPMeeting2.isChecked()==true)
                    {
                        //SymtomValue.add(1);
                        FPAValue.add("69");
                    }*/
                    int i = 0;
                    for (i = 0; i < FPAValue.size(); i++) {
                        for (String temp : FPAValue) {
                            // System.out.println(temp);
                            // SaveSymtom(temp);

                            String workplanId1 = C.ReturnSingleValue("Select workPlanId from workPlanMaster Where  providerId = '" + g.getProvCode() + "' AND month = '" + Global.Left(g.getAreaDate(), 7) + "'");//workAreaId='" + g.getAreaUnitValue() + "' AND

                            if (!C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + temp + "' and workPlanDate='" + g.getAreaDate() + "'"))

                            {


                              /*  SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo, fpiOtherMeeting,leaveType,providerId,systemEntryDate,modifyDate,\n" +
                                        "otherDec,remarks,upload)Values('"+workplanId1+"','"+temp+"','"+g.getAreaDate()+"','"+""+"','"+"" +"','"+ "" +"','"+ ""+"','"+(spnfpiothermeeting.getSelectedItemPosition()==0?"":Global.Left(spnfpiothermeeting.getSelectedItem().toString(), 1))+"','"+ (spnLeave.getSelectedItemPosition()==0?"":Global.Left(spnLeave.getSelectedItem().toString(), 1))+"','"+ g.getProvCode()+"','"+ Global.DateTimeNowYMDHMS() +"','"+ txtOther.getText()+"','"+ ""+"','"+"','2')";
                                C.Save(SQL);*/


                                if (temp.equals("61")) {
                                    SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,status,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + temp + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + (spnfpiothermeeting.getSelectedItemPosition() == 0 ? "" : Global.Left(spnfpiothermeeting.getSelectedItem().toString(), 1)) + "','" + g.getProvCode() + "','" + 1 + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "','2')";
                                    C.Save(SQL);
                                } else if (temp.equals("62")) {
                                    SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,status,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + temp + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + (spnLeave.getSelectedItemPosition() == 0 ? "" : Global.Left(spnLeave.getSelectedItem().toString(), 1)) + "','" + "" + "','" + g.getProvCode() + "','" + 1 + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "','2')";
                                    C.Save(SQL);
                                } else if (temp.equals("65")) {

                                    SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,status,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + temp + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + 1 + "','" + Global.DateTimeNowYMDHMS() + "','" + (chkROther.isChecked() ? (txtOther.getText().toString()) : "") + "','" + "" + "','" + "','2')";
                                    C.Save(SQL);


                                } else {
                                    SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,status,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + temp + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + 1 + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "','2')";
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

                    FPAValue.clear();
                } else if (C.Existence(SQ)) {
                    String workplanId1 = C.ReturnSingleValue("Select workPlanId from workPlanMaster Where  providerId = '" + g.getProvCode() + "' AND month = '" + Global.Left(g.getAreaDate(), 7) + "'");//workAreaId='" + g.getAreaUnitValue() + "' AND

                    //50 chkfpiAdvprog
                    if (chkfpiAdvprog.isChecked() == true && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 50 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();

                    } else if (chkfpiAdvprog.isChecked() == true && !C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 50 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,status,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + 50 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + 1 + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "','2')";
                        C.Save(SQL);
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkfpiAdvprog.isChecked() == false && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 50 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        C.Save("Delete from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 50 + "' and workPlanDate='" + g.getAreaDate() + "'");
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    }
                    //51 chkfpiapproved
                    if (chkfpiapproved.isChecked() == true && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 51 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();

                    } else if (chkfpiapproved.isChecked() == true && !C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 51 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,status,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + 51 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + 1 + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "','2')";
                        C.Save(SQL);
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkfpiapproved.isChecked() == false && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 51 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        C.Save("Delete from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 51 + "' and workPlanDate='" + g.getAreaDate() + "'");
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    }

                    //52 chkfpiSatTime
                    if (chkfpiSatTime.isChecked() == true && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 52 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();

                    } else if (chkfpiSatTime.isChecked() == true && !C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 52 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,status,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + 52 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + 1 + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "','2')";
                        C.Save(SQL);
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkfpiSatTime.isChecked() == false && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 52 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        C.Save("Delete from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 52 + "' and workPlanDate='" + g.getAreaDate() + "'");
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    }

                    //53 chkfpicampTime
                    if (chkfpicampTime.isChecked() == true && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 53 + "' and workPlanDate='" + g.getAreaDate() + "'")) {
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();


                    } else if (chkfpicampTime.isChecked() == true && !C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 53 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,status,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + 53 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + 1 + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "','2')";
                        C.Save(SQL);
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkfpicampTime.isChecked() == false && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 53 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        C.Save("Delete from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 53 + "' and workPlanDate='" + g.getAreaDate() + "'");
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    }

                    //54 chkfpihelpfpa
                    if (chkfpihelpfpa.isChecked() == true && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 54 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();

                    } else if (chkfpihelpfpa.isChecked() == true && !C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 54 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,status,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + 54 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + 1 + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "','2')";
                        C.Save(SQL);
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkfpihelpfpa.isChecked() == false && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 54 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        C.Save("Delete from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 54 + "' and workPlanDate='" + g.getAreaDate() + "'");
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    }

                    //55 chkfpiVisitfpa
                    if (chkfpiVisitfpa.isChecked() == true && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 55 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();

                    } else if (chkfpiVisitfpa.isChecked() == true && !C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 55 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,status,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + 55 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + 1 + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "','2')";
                        C.Save(SQL);
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkfpiVisitfpa.isChecked() == false && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 55 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        C.Save("Delete from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 55 + "' and workPlanDate='" + g.getAreaDate() + "'");
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    }

                    //56 chkfpiVerifydatafpa
                    if (chkfpiVerifydatafpa.isChecked() == true && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 56 + "' and workPlanDate='" + g.getAreaDate() + "'")) {
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();


                    } else if (chkfpiVerifydatafpa.isChecked() == true && !C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 56 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,status,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + 56 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + 1 + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "','2')";
                        C.Save(SQL);
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkfpiVerifydatafpa.isChecked() == false && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 56 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        C.Save("Delete from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 56 + "' and workPlanDate='" + g.getAreaDate() + "'");
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    }


                    //57 chkfpiepihelp
                    if (chkfpiepihelp.isChecked() == true && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 57 + "' and workPlanDate='" + g.getAreaDate() + "'")) {
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();


                    } else if (chkfpiepihelp.isChecked() == true && !C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 57 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,status,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + 57 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + 1 + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "','2')";
                        C.Save(SQL);
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkfpiepihelp.isChecked() == false && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 57 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        C.Save("Delete from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 57 + "' and workPlanDate='" + g.getAreaDate() + "'");
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    }

                    //58 chkfpiSatelait
                    if (chkfpiSatelait.isChecked() == true && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 58 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();

                    } else if (chkfpiSatelait.isChecked() == true && !C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 58 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,status,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + 58 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + 1 + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "','2')";
                        C.Save(SQL);
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkfpiSatelait.isChecked() == false && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 58 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        C.Save("Delete from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 58 + "' and workPlanDate='" + g.getAreaDate() + "'");
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    }

                    //59 chkfpiunmeeting
                    if (chkfpiunmeeting.isChecked() == true && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 59 + "' and workPlanDate='" + g.getAreaDate() + "'")) {
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();


                    } else if (chkfpiunmeeting.isChecked() == true && !C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 59 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,status,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + 59 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + 1 + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "','2')";
                        C.Save(SQL);
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkfpiunmeeting.isChecked() == false && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 59 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        C.Save("Delete from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 59 + "' and workPlanDate='" + g.getAreaDate() + "'");
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    }

                    //60 chkfpiuzmeeting
                    if (chkfpiuzmeeting.isChecked() == true && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 60 + "' and workPlanDate='" + g.getAreaDate() + "'")) {
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();


                    } else if (chkfpiuzmeeting.isChecked() == true && !C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 60 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,status,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + 60 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + 1 + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "','2')";
                        C.Save(SQL);
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkfpiuzmeeting.isChecked() == false && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 60 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        C.Save("Delete from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 60 + "' and workPlanDate='" + g.getAreaDate() + "'");
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    }

                    //61 chkfpiothermeeting
                    if (chkfpiothermeeting.isChecked() == true && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 61 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();

                    } else if (chkfpiothermeeting.isChecked() == true && !C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 61 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,status,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + 61 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + (spnfpiothermeeting.getSelectedItemPosition() == 0 ? "" : Global.Left(spnfpiothermeeting.getSelectedItem().toString(), 1)) + "','" + g.getProvCode() + "','" + 1 + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "','2')";
                        C.Save(SQL);
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkfpiothermeeting.isChecked() == false && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 61 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        C.Save("Delete from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 61 + "' and workPlanDate='" + g.getAreaDate() + "'");
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    }


                    //62 chkHDay
                    if (chkHDay.isChecked() == true && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 62 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();

                    } else if (chkHDay.isChecked() == true && !C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 62 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,status,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + 62 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + (spnLeave.getSelectedItemPosition() == 0 ? "" : Global.Left(spnLeave.getSelectedItem().toString(), 1)) + "','" + "" + "','" + g.getProvCode() + "','" + 1 + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "','2')";
                        C.Save(SQL);
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkHDay.isChecked() == false && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 62 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        C.Save("Delete from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 62 + "' and workPlanDate='" + g.getAreaDate() + "'");
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    }
                    //63 chkMreport
                    if (chkMreport.isChecked() == true && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 63 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();

                    } else if (chkMreport.isChecked() == true && !C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 63 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,status,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + 63 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + 1 + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "','2')";
                        C.Save(SQL);
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkMreport.isChecked() == false && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 63 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        C.Save("Delete from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 63 + "' and workPlanDate='" + g.getAreaDate() + "'");
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    }

                    //64 chkTraining
                    if (chkTraining.isChecked() == true && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 64 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();

                    } else if (chkTraining.isChecked() == true && !C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 64 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,status,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + 64 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + 1 + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "','2')";
                        C.Save(SQL);
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkTraining.isChecked() == false && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 64 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        C.Save("Delete from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 64 + "' and workPlanDate='" + g.getAreaDate() + "'");
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    }

                    //65 chkROther
                    if (chkROther.isChecked() == true && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 65 + "' and workPlanDate='" + g.getAreaDate() + "'")) {
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();


                    } else if (chkROther.isChecked() == true && !C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 65 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,status,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + 65 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + 1 + "','" + Global.DateTimeNowYMDHMS() + "','" + (chkROther.isChecked() ? (txtOther.getText().toString()) : "") + "','" + "" + "','" + "','2')";
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkROther.isChecked() == false && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 65 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        C.Save("Delete from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 65 + "' and workPlanDate='" + g.getAreaDate() + "'");
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    }

                    //66 chkfpiunmeeting1
                    if (chkfpiunmeeting1.isChecked() == true && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 66 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();

                    } else if (chkfpiunmeeting1.isChecked() == true && !C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 66 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,status,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + 66 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + 1 + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "','2')";
                        C.Save(SQL);
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkfpiunmeeting1.isChecked() == false && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 66 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        C.Save("Delete from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 66 + "' and workPlanDate='" + g.getAreaDate() + "'");
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    }


                    //67 chkUFCMeeting
                    if (chkUFCMeeting.isChecked() == true && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 67 + "' and workPlanDate='" + g.getAreaDate() + "'")) {
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();


                    } else if (chkUFCMeeting.isChecked() == true && !C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 67 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,fpiOtherMeeting,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epischedulerId,epiproviderId,ccWard,ccID,leaveType,natProgramType,providerId,status,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + 67 + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + 1 + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "','2')";
                        C.Save(SQL);
                        DataSearch(Global.Left(spnFPIPMonth.getSelectedItem().toString(), 2));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        popupView.cancel();
                    } else if (chkUFCMeeting.isChecked() == false && C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 67 + "' and workPlanDate='" + g.getAreaDate() + "'")) {

                        C.Save("Delete from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + 67 + "' and workPlanDate='" + g.getAreaDate() + "'");
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

            workPlanDate.setText(o.get("workPlanDate") + '/' + o.get("Month") + '/' + o.get("Year") + "           " + g.getDay(o.get("workPlanDate") + '/' + o.get("Month") + '/' + o.get("Year")));
            //itemdes.setText(o.get("itemdes"));

            if (g.getDay(o.get("workPlanDate") + '/' + o.get("Month") + '/' + o.get("Year")).equalsIgnoreCase("শুক্রবার")) {
                cmdB2.setTextColor(Color.BLACK);
                cmdB2.setTextSize(20);
                cmdB2.setText("সাপ্তাহিক ছুটি");
                cmdB2.setEnabled(false);
                // cmdB2.setBackgroundColor(Color.parseColor("#ff00ff"));
                cmdB2.setBackgroundColor(Color.parseColor("#D7D7D7"));
                // workPlanDate.setBackgroundColor(Color.parseColor("#99cc33"));
                // cmdB2.setBackgroundColor(Color.BLUE);
            }



            else {
                cmdB2.setTextColor(Color.BLACK);
                cmdB2.setTextSize(20);
                //cmdB2.setBackgroundColor(Color.parseColor("#FFFFFF"));
                cmdB2.setBackgroundColor(Color.BLUE);
                cmdB2.setEnabled(true);
                cmdB2.setText("কর্মসূচী সংযোজন");
            }

            FPADate.addAll(C.getDataList("Select strftime('%d/%m/%Y', date(workPlanDate)) wdate from workPlanDetail where  providerId='" + g.getProvCode() + "' and item>=50 group by workPlanDate"));
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

            final AlertDialog.Builder adb = new AlertDialog.Builder(FpiWorkPlaning.this);



            cmdB2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    g.setAreaUnitValue(Global.Left(txtFpiWarea.getText().toString(), 2));
                    g.setAreaDate(o.get("Year") + "-" + o.get("Month") + '-' + o.get("workPlanDate"));
                    g.setAreaDateM(o.get("Year") + "-" + o.get("Month"));


                    String WorkPlainvalue =C.ReturnSingleValue("Select ifnull(status, '' ) AS status FROM workPlanMaster where providerId='"+g.getProvCode()+"' and month='"+o.get("Year") + "-" + o.get("Month")+"'");
                    if(WorkPlainvalue.equals("3")||WorkPlainvalue.equals(""))
                    {
                        DisplayFPIWorkPlaining();
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
                        DisplayFPIWorkPlaining();
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