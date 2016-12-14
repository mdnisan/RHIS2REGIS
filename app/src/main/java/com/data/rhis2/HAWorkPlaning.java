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
 * Created by Nisan on 6/25/2016.
 */
public class HAWorkPlaning extends Activity {
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
        AlertDialog.Builder adb = new AlertDialog.Builder(HAWorkPlaning.this);
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
    ArrayList<String> ItemValue = new ArrayList<String>();
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
            setContentView(R.layout.haworkplaining);
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
            lblHS10.setText("স্বাস্থ্য সহকারীর  মাসিক অগ্রিম কর্মসূচী");


            secFPIPMonth = (LinearLayout) findViewById(R.id.secFPIPMonth);
            VlblFPIPMonth = (TextView) findViewById(R.id.VlblFPIPMonth);
            spnFPIPMonth = (Spinner) findViewById(R.id.spnFPIPMonth);
            // spnFPIPMonth.setAdapter(C.getArrayAdapterMultiline("SELECT strftime('%Y', date('now')) ||','||mName||':-'||substr('0' ||id, -2, 2) as ym from month  where id <=(SELECT strftime('%m','now')+1) order by id Desc"));
           // spnFPIPMonth.setAdapter(C.getArrayAdapterMultiline("SELECT substr('0' ||id, -2, 2)||'-'||mName||','||strftime('%Y', date('now'))  as ym from month  where id <=(SELECT strftime('%m','now')+1) order by id Desc"));
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
            Connection.MessageBox(HAWorkPlaning.this, e.getMessage());
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
                // map.put("WorkDate", String.valueOf(C.ReturnSingleValue("SELECT strftime('%d/%m/%Y', date(workPlanDate)) from fpaWorkPlanDetail where workPlanDate='"+String.valueOf(Global.Left(spnFPIPMonth.getSelectedItem().toString(),4))+"-"+String.valueOf(Global.Right(spnFPIPMonth.getSelectedItem().toString(),2))+"-"+String.valueOf(i)+"'")));
                map.put("itemdes", "");


                dataList.add(map);

                dataAdapter = new SimpleAdapter(HAWorkPlaning.this, dataList, R.layout.fpaworkplanrow, new String[]{"edit", "delete"},
                        new int[]{R.id.cmdB1, R.id.cmdB2});

                list.setAdapter(new DataListAdapter(this));
            }
        } catch (Exception e) {
            Connection.MessageBox(HAWorkPlaning.this, e.getMessage());
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


    public void DeleteItem(String Item) {
        try {


            String SQL = "";

            if (C.Existence("Select workPlanId, providerId,workPlanDate,item from fpaWorkPlanDetail where workPlanId='" + GetParentWorkPlanId1() + "' and providerId='" + g.getProvCode() + "' and workPlanDate='" + g.getAreaDate() + "' and item= '" + Item + "'"))

            {

                SQL = "Delete from workPlanDetail where workPlanId='" + GetParentWorkPlanId1() + "' and providerId='" + g.getProvCode() + "' and item= '" + Item + "' and workPlanDate='" + g.getAreaDate() + "'";
                C.Save(SQL);

            } else {

            }


        } catch (Exception e) {
            Connection.MessageBox(HAWorkPlaning.this, e.getMessage());
            return;
        }

    }

    OptionsFPI op = new OptionsFPI();

    private void DisplayAHIWorkPlain() {
        final Dialog dialog = new Dialog(HAWorkPlaning.this);
        dialog.setTitle("HI ওয়ার্কপ্ল্যান");
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
                "                FROM supervisoryItem where \n" +
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

    private String ProvArea(String ProvType, String ProvCode) {
        String SQL = "";
        if (ProvType.equals("2"))
            SQL = "select distinct ward from ProviderArea WHERE provCode = '" + ProvCode + "'";
        else if (ProvType.equals("3"))
            SQL = "select distinct fwaunit from ProviderArea WHERE provCode = '" + ProvCode + "'";

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

    private void DisplayAHIWorkPlaining() {
        final Dialog popupView = new Dialog(HAWorkPlaning.this);
        popupView.setTitle("AHI ওয়ার্কপ্ল্যান");

        popupView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popupView.setContentView(R.layout.haworkplainingpopup);
        popupView.setCancelable(true);
        popupView.setCanceledOnTouchOutside(true);

        final TextView lblHlblepireg = (TextView) popupView.findViewById(R.id.lblHlblepireg);

        lblHlblepireg.setText(Global.DateConvertDMY(g.getAreaDate()) + ":-" + "কর্মসূচী সমূহ");

        //final Spinner SpnUnion = (Spinner) popupView.findViewById(R.id.SpnUnion);

         /*List<String> fpiothermeeting = new ArrayList<String>();

        fpiothermeeting.add("");
        fpiothermeeting.add("1-তথ্য");
        fpiothermeeting.add("2-শিক্ষা ও যোগাযোগ বিষয়ক সভা");
        fpiothermeeting.add("3-স্বাস্থ্য শিক্ষা ও পুষ্টি বিষয়ক কার্যক্রম");
        fpiothermeeting.add("4-বৃক্ষরোপণ কার্যক্রম");
        fpiothermeeting.add("5-উদ্ভুদ্ধকরন কার্যক্রম");
        fpiothermeeting.add("6-সচেতনতা মূলক কার্যক্রম");
        fpiothermeeting.add("7-বিভিন্ন পর্যায় ও সংস্থার প্রতিনিধিদের সাথে সমন্বয় বিষয়ক কার্যক্রম");


       final Spinner spnfpiothermeeting = (Spinner) popupView.findViewById(R.id.spnfpiothermeeting);
        ArrayAdapter<String> fpiothermeetingadptr= new ArrayAdapter<String>(this, R.layout.multiline_spinner_dropdown_item, fpiothermeeting);
        spnfpiothermeeting.setAdapter(fpiothermeetingadptr);*/

        List<String> leaves = new ArrayList<String>();

        leaves.add("");
        leaves.add("1-বাৎসরিক");
        leaves.add("2-অসুস্থতা জনিত");
        leaves.add("3-মাতৃত্বকালীন");
        leaves.add("4-পিতৃত্ব কালীন ছুটি");
        leaves.add("5-নৈমিত্তিক ছুটি");
        leaves.add("6-শ্রান্তি ও বিনোদন");
        leaves.add("7-ঐচ্ছিক ছুটি ");


        final Spinner spnLeave = (Spinner) popupView.findViewById(R.id.spnLeave);
        ArrayAdapter<String> leaveadptr = new ArrayAdapter<String>(this, R.layout.multiline_spinner_dropdown_item, leaves);
        spnLeave.setAdapter(leaveadptr);
        final Spinner spnProvider = (Spinner) popupView.findViewById(R.id.spnProvider);
        String Type = "";
        if (g.getProvType().equals("10")) {
            Type = "3";
        } else if (g.getProvType().equals("11")) {
            Type = "2";
        }
        spnProvider.setAdapter(C.getArrayAdapter("Select '-' provName from ProviderDb Union Select provCode||'-'||provName from ProviderDb WHERE provType='" + Type + "'"));

        final Spinner spnSessionDate = (Spinner) popupView.findViewById(R.id.spnSessionDate);
        spnProvider.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (spnProvider.getSelectedItemPosition() > 0) {


                    spnSessionDate.setAdapter(C.getArrayAdapter("select strftime('%d/%m/%Y', date(scheduleDate)) as scheduleDate from epiScheduler where  providerId='" + Global.Left(spnProvider.getSelectedItem().toString(), 5) + "' and strftime('%m', date(scheduleDate))=(SELECT strftime('%m','now'))  order by scheduleDate desc"));


                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        final Spinner spnEpiSubBlock = (Spinner) popupView.findViewById(R.id.spnEpiSubBlock);

        final Spinner spnEPICenterName = (Spinner) popupView.findViewById(R.id.spnEPICenterName);

        spnSessionDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (spnSessionDate.getSelectedItemPosition() > 0) {

                    // spnEpiSubBlock.setVisibility(View.VISIBLE);
                    // spnEPICenterName.setVisibility(View.VISIBLE);
                    spnEpiSubBlock.setAdapter(C.getArrayAdapter("select distinct sb.BCode||'-'||sb.BNameBan from HABlock sb left outer join epiScheduler epib on sb.BCode=epib.subBlockId where  providerId='" + Global.Left(spnProvider.getSelectedItem().toString(), 5) + "' and scheduleDate='" + Global.Right(spnSessionDate.getSelectedItem().toString(), 4) + "-" + Global.Mid(spnSessionDate.getSelectedItem().toString(), 3, 5) + "-" + Global.Left(spnSessionDate.getSelectedItem().toString(), 2) + "'"));
                    //Select '-' BNameBan from HABlock Union

                }

            /*    else if (spnSessionDate.getSelectedItemPosition() == 0)
                {

                    spnEpiSubBlock.setVisibility(View.GONE);
                    spnEPICenterName.setVisibility(View.GONE);
                }*/


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
                    spnEPICenterName.setAdapter(C.getArrayAdapter("select substr('0' ||schedulerId, -2, 2)||'-'||providerId||'-'||centerName from epiScheduler where subBlockId='" + Global.Left(spnEpiSubBlock.getSelectedItem().toString(), 2) + "' and providerId='" + Global.Left(spnProvider.getSelectedItem().toString(), 5) + "' and  strftime('%d/%m/%Y', date(scheduleDate))='" + Global.Left(spnSessionDate.getSelectedItem().toString(), 10) + "'"));

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        // final Spinner spnccName = (Spinner) popupView.findViewById(R.id.spnccName);
      /*  spnHSubBlock.setAdapter(C.getArrayAdapter("select distinct sb.BCode||'-'||sb.BNameBan from HABlock sb\n" +
                "left outer join ProviderArea p on sb.BCode=p.FWAUnit"));
  */      //spnHSubBlock.setAdapter(C.getArrayAdapter("Select ' সকল সাব ব্লক'as BCode union Select BCode||'-'||BNameBan as BCode from HABlock order by BCode asc"));
        // spnEPICenterName.setAdapter(C.getArrayAdapter("Select subBlockId||'-'||centerName as subBlockId from epiScheduler order by subBlockId asc"));

    /*    spnHSubBlock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (spnHSubBlock.getSelectedItemPosition() >= 0) {

                    spnEPICenterName.setAdapter(C.getArrayAdapter("select distinct substr('0' ||schedulerId, -2, 2)||'-'||providerId||'-'||centerName from epiScheduler where subBlockId='"+Global.Left(spnHSubBlock.getSelectedItem().toString(),2)+"'"));

                }





            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });*/

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
        final LinearLayout secMouza = (LinearLayout) popupView.findViewById(R.id.secMouza);
        //  final LinearLayout secVill=(LinearLayout) popupView.findViewById(R.id.secVill);
        final LinearLayout secPara = (LinearLayout) popupView.findViewById(R.id.secPara);
        final LinearLayout secBarivisit = (LinearLayout) popupView.findViewById(R.id.secBarivisit);

        final LinearLayout secWord = (LinearLayout) popupView.findViewById(R.id.secWord);
        final LinearLayout secname = (LinearLayout) popupView.findViewById(R.id.secname);
        final LinearLayout secSessionDate = (LinearLayout) popupView.findViewById(R.id.secSessionDate);
        final LinearLayout secHSubBlock = (LinearLayout) popupView.findViewById(R.id.secHSubBlock);
        final LinearLayout secEPICenterName = (LinearLayout) popupView.findViewById(R.id.secEPICenterName);
        final LinearLayout secccWord = (LinearLayout) popupView.findViewById(R.id.secccWord);
        final LinearLayout secccName = (LinearLayout) popupView.findViewById(R.id.secccName);
        final LinearLayout secNProgram = (LinearLayout) popupView.findViewById(R.id.secNProgram);
        final Spinner SpnUnion = (Spinner) popupView.findViewById(R.id.SpnUnion);
        final Spinner SpnWord = (Spinner) popupView.findViewById(R.id.SpnWord);
        final Spinner SpnMouza = (Spinner) popupView.findViewById(R.id.SpnMouza);

        final EditText txtPara = (EditText) popupView.findViewById(R.id.txtPara);
        final EditText txtBarivisit1 = (EditText) popupView.findViewById(R.id.txtBarivisit1);
        final EditText txtBarivisit2 = (EditText) popupView.findViewById(R.id.txtBarivisit2);
        //SpnUnion.setAdapter(C.getArrayAdapter("select ward from ProviderArea where provCode = '"+ g.getProvCode()+"'"));
        // SpnUnion.setAdapter(C.getArrayAdapter("select cast(unionid as varchar(2))||'-'||unionname from Unions where zillaid='"+ g.getDistrict() +"' and upazilaid='"+ g.getUpazila() +"' and unionid='"+ g.getUnion() +"'"));
        SpnUnion.setAdapter(C.getArrayAdapter("select '-' unionname from Unions Union select distinct substr('0' ||p.unionid, -2, 2)||'-'||u.unionname from Unions u left outer join ProviderArea p on u.zillaid=p.zillaid and  u.upazilaid=p.upazilaid"));
        SpnWord.setAdapter(C.getArrayAdapter("select '-' ward from ProviderArea Union select distinct ward from ProviderArea where zillaid='" + g.getDistrict() + "' and upazilaid='" + g.getUpazila() + "' and unionid='" + g.getUnion() + "'"));
        SpnWord.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (SpnWord.getSelectedItemPosition() >= 0) {

                    SpnMouza.setAdapter(C.getArrayAdapterMultiline("Select '  ' VILLAGENAME from Village union select substr('0' ||v.MOUZAID, -3, 3)||v.VILLAGEID||'-'||v.VILLAGENAME as VILLAGENAME from Village v\n" +
                            "left outer join ProviderArea p on v.ZILLAID=p.zillaid and  v.UPAZILAID=p.upazilaid and\n" +
                            " v.MOUZAID||v.VILLAGEID=p.mouzaid||p.villageid where p.Ward='" + Global.Left(SpnWord.getSelectedItem().toString(), 1) + "'"));

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        //  SpnUnion.setText(C.ReturnSingleValue("select cast(unionid as varchar(2))||','||unionname from Unions where zillaid='"+ g.getDistrict() +"' and upazilaid='"+ g.getUpazila() +"' and unionid='"+ g.getUnion() +"'"));
        // txtWord.setText(g.getFWAUnit());
        // SpnWord.setText(ProvArea("2",g.getProvCode()));
        // final Spinner spnVill = (Spinner) popupView.findViewById(R.id.spnVill);


        //  spnVill.setAdapter(C.getArrayAdapterMultiline("Select '-' as VILLAGENAME union select MOUZAID||VILLAGEID||'-'||VILLAGENAME as VILLAGENAME\n" +
        //        "from Village"));

      /*  List<String> ccName = new ArrayList<String>();

        ccName.add("");
        ccName.add("1-Center A");
        ccName.add("2-Center B");*/


        final Spinner SpnCCWord = (Spinner) popupView.findViewById(R.id.SpnCCWord);
        SpnCCWord.setAdapter(C.getArrayAdapter("select '-' wardId from ccInfo Union select distinct wardId from ccInfo where zillaid='" + g.getDistrict() + "' and upazilaid='" + g.getUpazila() + "' and unionid='" + g.getUnion() + "'"));
        final Spinner spnccName = (Spinner) popupView.findViewById(R.id.spnccName);
       /* ArrayAdapter<String> ccNameadptr= new ArrayAdapter<String>(this, R.layout.multiline_spinner_dropdown_item, ccName);
        spnccName.setAdapter(ccNameadptr);*/

        SpnCCWord.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (SpnCCWord.getSelectedItemPosition() >= 0) {

                    spnccName.setAdapter(C.getArrayAdapterMultiline("select distinct wardId||'-'||ccName from ccInfo where zillaid='" + g.getDistrict() + "' and upazilaid='" + g.getUpazila() + "' and unionid='" + g.getUnion() + "' and wardId='" + Global.Left(SpnCCWord.getSelectedItem().toString(), 1) + "'"));

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        final CheckBox chkIPCVisit = (CheckBox) popupView.findViewById(R.id.chkIPCVisit);
        final CheckBox chkEPIVisit = (CheckBox) popupView.findViewById(R.id.chkEPIVisit);
        final CheckBox chkCCVisit = (CheckBox) popupView.findViewById(R.id.chkCCVisit);
        final CheckBox chkOfficeWork = (CheckBox) popupView.findViewById(R.id.chkOfficeWork);
        final CheckBox chkUHMMeeting = (CheckBox) popupView.findViewById(R.id.chkUHMMeeting);
        final CheckBox chkMonthlyReportColl = (CheckBox) popupView.findViewById(R.id.chkMonthlyReportColl);
        final CheckBox chkMonthlyWorkPlan = (CheckBox) popupView.findViewById(R.id.chkMonthlyWorkPlan);
        final CheckBox chkTrainingAttended = (CheckBox) popupView.findViewById(R.id.chkTrainingAttended);
        final CheckBox chkNationalProgramAttended = (CheckBox) popupView.findViewById(R.id.chkNationalProgramAttended);
        final CheckBox chkOtherProgram = (CheckBox) popupView.findViewById(R.id.chkOtherProgram);
        //  final CheckBox chkfpiuzmeeting = (CheckBox) popupView.findViewById(R.id.chkfpiuzmeeting);
        //  final CheckBox chkfpiothermeeting = (CheckBox) popupView.findViewById(R.id.chkfpiothermeeting);
        //final CheckBox chkfpiunmeeting1 = (CheckBox) popupView.findViewById(R.id.chkfpiunmeeting1);
        final CheckBox chkHDay = (CheckBox) popupView.findViewById(R.id.chkHDay);
        //  final CheckBox chkMreport = (CheckBox) popupView.findViewById(R.id.chkMreport);
        //final CheckBox chkTraining = (CheckBox) popupView.findViewById(R.id.chkTraining);
        // final CheckBox chkROther = (CheckBox) popupView.findViewById(R.id.chkROther);
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

        chkCCVisit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (chkCCVisit.isChecked()) {
                    secccWord.setVisibility(View.VISIBLE);
                    secccName.setVisibility(View.VISIBLE);


                } else {
                    secccWord.setVisibility(View.GONE);
                    secccName.setVisibility(View.GONE);
                    spnccName.setSelection(0);


                }
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


        popupView.show();
        secBarivisit.setVisibility(View.GONE);
        secPara.setVisibility(View.GONE);
        //secVill.setVisibility(View.GONE);
        secUnion.setVisibility(View.GONE);
        secWord.setVisibility(View.GONE);
        secMouza.setVisibility(View.GONE);
        secLeave.setVisibility(View.GONE);
        secname.setVisibility(View.GONE);
        secSessionDate.setVisibility(View.GONE);
        secHSubBlock.setVisibility(View.GONE);
        secEPICenterName.setVisibility(View.GONE);
        secccWord.setVisibility(View.GONE);
        secccName.setVisibility(View.GONE);
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
                } else {
                    chkIPCVisit.setChecked(false);


                }

                if (cur.getString(cur.getColumnIndex("P2")).equals("2")) {
                    chkEPIVisit.setChecked(true);
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
                } else {
                    chkNationalProgramAttended.setChecked(false);


                }

                if (cur.getString(cur.getColumnIndex("P11")).equals("11")) {
                    chkOtherProgram.setChecked(true);
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
                    if (chkIPCVisit.isChecked() == true) {

                        ItemValue.add("1");
                    }


                    if (chkEPIVisit.isChecked() == true) {

                        ItemValue.add("2");
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
                        ItemValue.add("6");
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

                        ItemValue.add("10");
                    }


                    if (chkOtherProgram.isChecked() == true) {

                        ItemValue.add("11");
                    }


                }


                int i = 0;
                for (i = 0; i < ItemValue.size(); i++) {
                    for (String temp : ItemValue) {
                        // System.out.println(temp);
                        // SaveSymtom(temp);
                       // workAreaId='" + g.getAreaUnitValue() + "' AND
                        String workplanId1 = C.ReturnSingleValue("Select workPlanId from workPlanMaster Where  providerId = '" + g.getProvCode() + "' AND month = '" + Global.Left(g.getAreaDate(), 7) + "'");

                        if (!C.Existence("Select workPlanId, providerId, item, workPlanDate from " + TableNameDetail + "  where workPlanId='" + workplanId1 + "' and providerId='" + g.getProvCode() + "' and item = '" + temp + "' and workPlanDate='" + g.getAreaDate() + "'"))

                        {

                            //   SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epiproviderId,epischedulerId,ccWard,ccID,leaveType,natProgramType,providerId,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('"+workplanId1+"','"+temp+"','"+g.getAreaDate()+"','"+(SpnUnion.getSelectedItemPosition()==0?"":Global.Left(SpnUnion.getSelectedItem().toString(), 2))+"','"+(SpnWord.getSelectedItemPosition()==0?"":Global.Left(SpnWord.getSelectedItem().toString(), 1))+"','"+ (SpnMouza.getSelectedItemPosition()==0?"":Global.Left(SpnMouza.getSelectedItem().toString(), 3)) +"','"+ (SpnMouza.getSelectedItemPosition()==0?"":Global.Mid(SpnMouza.getSelectedItem().toString(), 3,4))+"','"+txtPara.getText()+"','"+txtBarivisit1.getText()+"','"+txtBarivisit2.getText()+"','"+Global.Left(spnEPICenterName.getSelectedItem().toString(), 2)+"','"+(spnProvider.getSelectedItemPosition()==0?"":Global.Left(spnProvider.getSelectedItem().toString(), 5))+"','"+(SpnCCWord.getSelectedItemPosition()==0?"":Global.Left(SpnCCWord.getSelectedItem().toString(), 1))+"','"+(spnccName.getSelectedItemPosition()==0?"":Global.Left(spnccName.getSelectedItem().toString(), 1))+"','"+ (spnLeave.getSelectedItemPosition()==0?"":Global.Left(spnLeave.getSelectedItem().toString(), 1))+"','"+(spnNProgram.getSelectedItemPosition()==0?"":Global.Left(spnNProgram.getSelectedItem().toString(), 1))+"','"+g.getProvCode()+"','"+ Global.DateTimeNowYMDHMS() +"','"+""+"','"+ ""+"','"+"','2')";
                            if (temp.equals("1")) {
                                SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epiproviderId,epischedulerId,ccWard,ccID,leaveType,natProgramType,providerId,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + temp + "','" + g.getAreaDate() + "','" + (SpnUnion.getSelectedItemPosition() == 0 ? "" : Global.Left(SpnUnion.getSelectedItem().toString(), 2)) + "','" + (SpnWord.getSelectedItemPosition() == 0 ? "" : Global.Left(SpnWord.getSelectedItem().toString(), 1)) + "','" + (SpnMouza.getSelectedItemPosition() == 0 ? "" : Global.Left(SpnMouza.getSelectedItem().toString(), 3)) + "','" + (SpnMouza.getSelectedItemPosition() == 0 ? "" : Global.Mid(SpnMouza.getSelectedItem().toString(), 3, 4)) + "','" + txtPara.getText() + "','" + txtBarivisit1.getText() + "','" + txtBarivisit2.getText() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "','2')";
                                C.Save(SQL);
                            } else if (temp.equals("2")) {
                                SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epiproviderId,epischedulerId,ccWard,ccID,leaveType,natProgramType,providerId,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + temp + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + Global.Left(spnEPICenterName.getSelectedItem().toString(), 2) + "','" + (spnProvider.getSelectedItemPosition() == 0 ? "" : Global.Left(spnProvider.getSelectedItem().toString(), 5)) + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "','2')";
                                C.Save(SQL);
                            } else if (temp.equals("3")) {
                                SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epiproviderId,epischedulerId,ccWard,ccID,leaveType,natProgramType,providerId,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + temp + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + (SpnCCWord.getSelectedItemPosition() == 0 ? "" : Global.Left(SpnCCWord.getSelectedItem().toString(), 1)) + "','" + (spnccName.getSelectedItemPosition() == 0 ? "" : Global.Left(spnccName.getSelectedItem().toString(), 1)) + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "','2')";
                                C.Save(SQL);
                            } else if (temp.equals("6")) {
                                SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epiproviderId,epischedulerId,ccWard,ccID,leaveType,natProgramType,providerId,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + temp + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + (spnLeave.getSelectedItemPosition() == 0 ? "" : Global.Left(spnLeave.getSelectedItem().toString(), 1)) + "','" + "" + "','" + g.getProvCode() + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "','2')";
                                C.Save(SQL);
                            } else if (temp.equals("10")) {
                                SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epiproviderId,epischedulerId,ccWard,ccID,leaveType,natProgramType,providerId,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + temp + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + (spnNProgram.getSelectedItemPosition() == 0 ? "" : Global.Left(spnNProgram.getSelectedItem().toString(), 1)) + "','" + g.getProvCode() + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "','2')";
                                C.Save(SQL);
                            } else {
                                SQL = "Insert into workPlanDetail (workPlanId,item,workPlanDate,ipcUN,ipcWord,ipcMouza,ipcVill, ipcPara,ipcBariFrom,ipcBariTo,epiproviderId,epischedulerId,ccWard,ccID,leaveType,natProgramType,providerId,systemEntryDate,modifyDate,otherDec,remarks,upload)Values('" + workplanId1 + "','" + temp + "','" + g.getAreaDate() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + g.getProvCode() + "','" + Global.DateTimeNowYMDHMS() + "','" + "" + "','" + "" + "','" + "','2')";
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
                // cmdB2.setBackgroundColor(Color.parseColor("#ff00ff"));
                cmdB2.setBackgroundColor(Color.parseColor("#D7D7D7"));
                // workPlanDate.setBackgroundColor(Color.parseColor("#99cc33"));
                // cmdB2.setBackgroundColor(Color.BLUE);
            }

         /*   else  if(g.getDay(o.get("workPlanDate")+'/'+o.get("Month")+'/'+o.get("Year")).equalsIgnoreCase("শনিবার"))
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
            final AlertDialog.Builder adb = new AlertDialog.Builder(HAWorkPlaning.this);
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
                        DisplayAHIWorkPlaining();
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
