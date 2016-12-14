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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
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

/**
 * Created by ccah on 12/8/2015.
 */
public class WomanInjectable extends Activity {

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
    String StartTime;
    TextView dtDozeDate;
    TextView dt1stDueDoze;
    TextView EDD;
    ImageButton btnDozeDate;

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menumember, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder adb = new AlertDialog.Builder(WomanInjectable.this);
        switch (item.getItemId()) {
            case R.id.menuClose:
                adb.setTitle("Close");
                adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান?");
                adb.setNegativeButton("না", null);
                adb.setPositiveButton("হ্যাঁ", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (g.getCallFrom().equals("1")) {
                            finish();
                            Intent f2 = new Intent(getApplicationContext(), InjectableView.class);
                            startActivity(f2);
                        } else {
                            finish();
                            Intent f2 = new Intent(getApplicationContext(), MemberList.class);
                            startActivity(f2);
                            g.setGeneratedId("");
                        }
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
            setContentView(R.layout.injectable);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();
            con = this;
            dtDozeDate = (TextView) findViewById(R.id.dtDozeDate);
            dt1stDueDoze = (TextView) findViewById(R.id.dt1stDueDoze);
            IDbundle = getIntent().getExtras();
            btnDozeDate = (ImageButton) findViewById(R.id.btnDozeDate);
            if (IDbundle != null) {
                if (IDbundle.getString("search").equalsIgnoreCase("search")) {
                    // DisplaySearch(true);

                } else {
                    // DisplaySearch(false);
                    LoadData();
                }
            } else {
                // DisplaySearch(false);
                LoadData();
            }
        } catch (Exception ex) {
            Connection.MessageBox(WomanInjectable.this, ex.getMessage());
            return;
        }
    }

    private void LoadData() {
        try {

            // FindLocation();


            ((ImageButton) findViewById(R.id.btnDozeDate)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btnDozeDate";
                    showDialog(DATE_DIALOG);
                }
            });


            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Activity.KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
            lock.disableKeyguard();


            txtHealthId = (TextView) findViewById(R.id.txtHealthID);

            txtHealthId.setText(g.getHealthID());

            DataRetrieve();

            ((ImageButton) findViewById(R.id.cmdSave)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    DataSave();
                }
            });

        } catch (Exception ex) {
            Connection.MessageBox(WomanInjectable.this, ex.getMessage());
            return;
        }
    }

    private void DataSave() {
        ListView list = (ListView) findViewById(R.id.lstimu);
        Integer mCount = list.getCount();
        String sql = "";
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = sdf.format(c.getTime());
            Date date1 = sdf.parse(formattedDate);


            if (dt1stDueDoze.getText().toString().length() == 0) {
                Connection.MessageBox(WomanInjectable.this, "প্রথম ডোজের তারিখ এন্ট্রি আবশ্যক.");
                return;
            }

            if (dtDozeDate.getText().toString().length() == 0) {
                Connection.MessageBox(WomanInjectable.this, "প্রয়োগের তারিখ  এন্ট্রি করতে হবে .");
                return;
            }

            Date date2 = sdf.parse(dtDozeDate.getText().toString());
            if (date2.after(date1)) {
                if (VariableID.equals("btnDozeDate")) {
                    Connection.MessageBox(WomanInjectable.this, "তারিখ আজকের তারিখের সমান অথবা কম হতে হবে।");
                    return;
                }

            }


            String dozeId = ISlNo();

            sql = "INSERT INTO womanInjectable (healthId,providerId,doseId,doseDate,sideEffect,systemEntryDate,modifyDate,upload)";
            sql = sql + "VALUES ('" + g.getGeneratedId() + "', '" + g.getProvCode() + "','" + dozeId + "','" +
                    Global.DateConvertYMD(dtDozeDate.getText().toString()) + "','2','" + Global.DateTimeNowYMDHMS() + "','" + Global.DateTimeNowYMDHMS() + "','2')";
            C.Save(sql);
            RefreshGrid(true);
            String lastDozeDate = C.ReturnSingleValue("select  dozeDate from womanInjectable where healthId='" + g.getGeneratedId() + "' and dozeId=(select  max(dozeId) from womanInjectable)");
            if (lastDozeDate != "") {
                ((TextView) findViewById(R.id.dtDueDoze)).setText(Global.addDays(Global.DateConvertDMY(lastDozeDate), 90));
            }

           /* for (int i = 0; i < list.getCount(); i++) {
                TextView txtImuCode = (TextView) list.getChildAt(i).findViewById(R.id.imuCode);
                TextView txtImudose = (TextView) list.getChildAt(i).findViewById(R.id.numOfDose);
                TextView txtdateGiven = (TextView)list.getChildAt(i).findViewById(R.id.dategiven);


                if (txtImuCode!=null) {

                    sql = "INSERT INTO childImmunization (healthId, providerId, slNo, regDate, houseNo, imuCode, imuDate, systemEntryDate)";
                    sql = sql + "VALUES ('" + txtHealthId.getText().toString() + "', '" + g.getProvCode() + "','" + ((TextView) findViewById(R.id.txtSNo)).getText().toString() + "','" +
                            ((TextView) findViewById(R.id.txtRegDate)).getText().toString() + "','" + ((TextView) findViewById(R.id.txtHouse)).getText().toString() + "','" +
                            String.valueOf(txtImuCode.getText()) + "','" + String.valueOf(txtdateGiven.getText()) + "','" + Global.DateTimeNowYMDHMS() + "')";
                    C.Save(sql);
                }
            }*/


            /*for (int i = 0; i < mCount; i++) {
                View curr = mAdapter.getView(i, null, null);

                TextView txtImuCode = (TextView) curr.findViewById(R.id.imuCode);
                TextView txtImudose = (TextView) curr.findViewById(R.id.numOfDose);
                TextView txtdateGiven = (TextView) curr.findViewById(R.id.dategiven);
                if(txtImuCode!=null)
                {
                    sql = "INSERT INTO womanImmunization (healthId, providerId, slNo, regDate, houseNo, imuCode, imuDate, systemEntryDate)";
                    sql = sql + "VALUES ('" + txtHealthId.getText().toString() + "', '" + g.getProvCode() + "','" + ((TextView) findViewById(R.id.txtSNo)).getText().toString() + "','" +
                            ((TextView) findViewById(R.id.txtRegDate)).getText().toString() + "','" + ((TextView) findViewById(R.id.txtHouse)).getText().toString() + "','" +
                            String.valueOf(txtImuCode.getText()) + "','" + String.valueOf(txtdateGiven.getText()) + "','" + Global.DateTimeNowYMDHMS() + "')";
                    C.Save(sql);
                }
            }*/
        } catch (Exception ex) {
            Connection.MessageBox(WomanInjectable.this, ex.getMessage());
            return;
        }


    }


    //
    //Retrieve member list
    String d = "";

    //***************************************************************************************************************************
    public void DataRetrieve() {
        try {
            String SQL = "";
            /*SQL  = " Select ifnull(m.NameEng,'') as NameEng,";
            SQL += " Cast(((julianday(date('now'))-julianday(m.DOB))/365.25) as int) as Age,ifnull(e.elcoNo,'')  as elcoNo,";
            SQL += " (select  n.NameEng  from member n where  n.ProvCode=(select  o.ProvCode  from member o ";
            SQL += " Where  o.healthid ='" + g.getHealthID() + "')and n.HHNo=(select  p.HHNo  from member p Where  p.healthid='" + g.getHealthID() + "') and ";
            SQL += " n.SNo=(select  q.SPNO1  from member q Where  q.healthid='" + g.getHealthID() + "'))as HusName ";
            SQL += " from Member m ";
            SQL += " LEFT JOIN elco e ON e.healthId = m.healthid ";
            SQL += " where m.healthid='" + g.getHealthID() + "'";*/

            SQL = "SELECT ifnull(E.DOB,'') AS DOB , ifnull(E.Sex,'') AS Sex,  ifnull(E.SNO,'') AS SNo, ifnull(E.HHno,'') as HHNo, ifnull(E.HealthID,'') as HealthID, ifnull(E.NameEng,'') as NameEng ,(Cast(((julianday(date('now'))-julianday(E.DOB))/30.4) as int)/12) AS Age,\n" +
                    "CASE WHEN CAST ( E.SPNO1 AS int ) = 55 THEN ifnull ( E.NameEng , '' )\n" +
                    "WHEN CAST ( E.SPNO1 AS int ) = 77 THEN ifnull ( E.NameEng , '' )\n" +
                    "WHEN CAST ( E.SPNO1 AS int ) = 88 THEN ifnull ( E.NameEng , '' )\n" +
                    "ELSE ( SELECT NameEng FROM member A\n" +
                    "WHERE A.ProvCode = ( SELECT B.ProvCode FROM member B WHERE B.healthid ='" + g.getHealthID() + "'\n" +
                    "and A.Dist = B.Dist and A.Upz = B.Upz and A.UN = B.UN and A.Mouza = B.Mouza and\n" +
                    "A.Vill =B.Vill and A.HHNo = B.HHNo ) AND \n" +
                    "A.HHNo = ( SELECT C.HHNo FROM member C WHERE C.healthid ='" + g.getHealthID() + "' )\n" +
                    "AND A.SNo = ( SELECT D.SPNO1 FROM member  D WHERE D.HealthID ='" + g.getHealthID() + "' ) )\n" +
                    "END AS HusName,\n" +
                    "(SELECT (Cast(((julianday(date('now'))-julianday(A.DOB))/30.4) as int)/12) AS Age FROM member A\n" +
                    "WHERE A.ProvCode = ( SELECT B.ProvCode FROM member B WHERE B.healthid ='" + g.getHealthID() + "'\n" +
                    "and A.Dist = B.Dist and A.Upz = B.Upz and A.UN = B.UN         and    A.Mouza    = B.Mouza    and\n" +
                    "A.Vill =B.Vill and A.HHNo = B.HHNo)\n" +
                    "AND A.HHNo = ( SELECT C.HHNo FROM member C WHERE C.healthid ='" + g.getHealthID() + "' )\n" +
                    "AND A.SNo = ( SELECT D.SPNO1 FROM member  D WHERE D.HealthID ='" + g.getHealthID() + "' )) AS HusAge,\n" +
                    "ifnull(E.EDU,'') AS EDU, ifnull(E.HaveNID,'') AS HaveNID, ifnull(E.NID,'') as NID, ifnull(E.NIDStatus,'') AS NIDStatus, ifnull(E.HaveBR,'') AS HaveBR, ifnull(E.BRIDStatus,'') AS BRIDStatus, ifnull(E.MobileNo1,'') AS MobileNo1, ifnull(E.MobileNo2,'') as MobileNo2 \n" +
                    "FROM member E WHERE E.healthId ='" + g.getHealthID() + "'";


            Cursor cur1 = C.ReadData(SQL);

            cur1.moveToFirst();
            while (!cur1.isAfterLast()) {
                ((TextView) findViewById(R.id.txtSNo)).setText(ISlNo());
                //((TextView)findViewById(R.id.ELCONo)).setText(cur1.getString(cur1.getColumnIndex("elcoNo")));
                ((TextView) findViewById(R.id.ELCONo)).setText(g.getAMElco());
                //txtELCONumber.setText(g.getAMElco());
                ((TextView) findViewById(R.id.txtName)).setText(cur1.getString(cur1.getColumnIndex("NameEng")));
                ((TextView) findViewById(R.id.txtAge)).setText(cur1.getString(cur1.getColumnIndex("Age")));
                ((TextView) findViewById(R.id.txtHusName)).setText(cur1.getString(cur1.getColumnIndex("HusName")));
                ((TextView) findViewById(R.id.txtHusAge)).setText(cur1.getString(cur1.getColumnIndex("HusAge")));//
                if (((TextView) findViewById(R.id.txtName)).getText().toString().equalsIgnoreCase(((TextView) findViewById(R.id.txtHusName)).getText().toString())) {
                    ((TextView) findViewById(R.id.txtHusName)).setText("");
                    ((TextView) findViewById(R.id.txtHusAge)).setText("");
                    String HusName = C.ReturnSingleValue("Select husbandName from elco where cast(healthid as bigint)=" + g.getGeneratedId() + "");
                    ((TextView) findViewById(R.id.txtHusName)).setText(HusName);
                    String HusAge = C.ReturnSingleValue("Select husbandAge from elco where cast(healthid as bigint)=" + g.getGeneratedId() + "");
                    ((TextView) findViewById(R.id.txtHusAge)).setText(HusAge);

                } else {

                }
                ((TextView) findViewById(R.id.txtFName)).setText(g.GetMotherName(C, g.getHealthID()));
                ((TextView) findViewById(R.id.txtMName)).setText(g.GetFatherName(C, g.getHealthID()));
                String totalDoze = C.ReturnSingleValue("select  count(*) from womanInjectable where healthId='" + g.getGeneratedId() + "'");
                Integer numOfDoze = Integer.parseInt(totalDoze);
                String firstDozeDate = C.ReturnSingleValue("select  doseDate from womanInjectable where healthId='" + g.getGeneratedId() + "' and doseId='1'");
                String lastDozeDate = C.ReturnSingleValue("select  doseDate from womanInjectable where healthId='" + g.getGeneratedId() + "' and doseId=(select  max(doseId) from womanInjectable)");
                if (numOfDoze > 1) {
                    if (lastDozeDate != "") {
                        ((TextView) findViewById(R.id.dt1stDueDoze)).setText(Global.DateConvertDMY(firstDozeDate));
                        ((TextView) findViewById(R.id.dtDueDoze)).setText(Global.addDays(Global.DateConvertDMY(lastDozeDate), 90));
                    }
                } else {

                    if (firstDozeDate != "") {
                        ((TextView) findViewById(R.id.dt1stDueDoze)).setText(Global.DateConvertDMY(firstDozeDate));
                        ((TextView) findViewById(R.id.dtDueDoze)).setText(Global.addDays(Global.DateConvertDMY(firstDozeDate), 90));
                    }
                }


                //((TextView)findViewById(R.id.txtFName)).setText(cur1.getString(cur1.getColumnIndex("Father")));
                //((TextView)findViewById(R.id.txtMName)).setText(cur1.getString(cur1.getColumnIndex("Mother")));
                //((TextView)findViewById(R.id.txtVil)).setText(g.getVillageName());
                cur1.moveToNext();

            }
            cur1.close();
            RefreshGrid(true);


        } catch (Exception e) {
            Connection.MessageBox(WomanInjectable.this, e.getMessage());
        }
    }

    private String ISlNo() {
        String SQL = "";

        SQL = "select ifnull(Count(*),'')+1 from womanInjectable";
        SQL += " where healthId='" + g.getGeneratedId() + "' and providerId='" + g.getProvCode() + "'";
        String SNo = C.ReturnSingleValue(SQL);
        // Serial No auto increment
        return SNo;
    }

    private void RefreshGrid(Boolean heading) {
        mylist.clear();

        ListView list = (ListView) findViewById(R.id.lstimu);


        if (heading == true) {
            View header = getLayoutInflater().inflate(R.layout.womaninjectableheading, null);
            list.addHeaderView(header);
        }

//womanInjectable (healthId,providerId,dozeId,dozeDate,sideEffect,systemEntryDate,modifyDate,upload)
        String sqlString = "select doseId AS imuCode, sideEffect AS imuName, doseId AS numOfDose, doseDate AS imuDate" +
                " FROM womanInjectable where healthId='" + g.getGeneratedId() + "'";
        Cursor cur = C.ReadData(sqlString);

        cur.moveToFirst();

        // int i=0;
        while (!cur.isAfterLast()) {
            HashMap<String, String> map = new HashMap<String, String>();

            //if(i==0)
            //  {
            //View header = getLayoutInflater().inflate(R.layout.memberlistheading, null);
            //list.addHeaderView(header);
            // }
            map.put("imuCode", cur.getString(cur.getColumnIndex("imuCode")));
            Integer imuCode = Integer.parseInt(cur.getString(cur.getColumnIndex("imuCode")));


            switch (imuCode) {

                /*case 17datetobegiventetobegiven",  Global.addDays(((TextView) findViewById(R.id.txtDOB)).getText().toString(), 5475));
                    d = Global.addDays(((TextView) findViewById(R.id.txtDOB)).getText().toString(), 5475);
                    break;
                case 18:
                    d = Global.addDays(d, 28);
                    map.put("datetobegiven",  d);
                    break;
                case 19:  d = Global.addDays(d, 42);
                    map.put("datetobegiven", d);
                    break;
                case 20:
                    d = Global.addDays(d, 365);
                    map.put("datetobegiven",  d);
                    break;
                case 21:
                    d = Global.addDays(d, 365);
                    map.put("datetobegiven",  d);
                    break;*/


            }

            map.put("imuName", cur.getString(cur.getColumnIndex("imuName")));
            map.put("numOfDose", cur.getString(cur.getColumnIndex("numOfDose")));
            map.put("dategiven", cur.getString(cur.getColumnIndex("imuDate")));

            mylist.add(map);

            mAdapter = new SimpleAdapter(WomanInjectable.this, mylist, R.layout.womaninjectablerow, new String[]{"imuCode"},
                    new int[]{R.id.imuCode});
            list.setAdapter(new WomanImuListAdapter(this));
            //  i+=1;
            cur.moveToNext();
        }
        cur.close();

    }

    public class WomanImuListAdapter extends BaseAdapter {
        private Context context;


        public void setDateSet(String dSet) {
            dateSet = dSet;
        }

        public WomanImuListAdapter(Context c) {
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
                convertView = inflater.inflate(R.layout.womanimulistrow, parent, false);
            }

            try {

                TextView imuCode = (TextView) convertView.findViewById(R.id.imuCode);
                TextView imuName = (TextView) convertView.findViewById(R.id.imuName);
                TextView numOfDose = (TextView) convertView.findViewById(R.id.numOfDose);
                TextView sideeffect = (TextView) convertView.findViewById(R.id.sideeffect);
                TextView dategiven = (TextView) convertView.findViewById(R.id.dategiven);

                ImageButton btndategiven = (ImageButton) convertView.findViewById(R.id.btndate);


                final HashMap<String, String> o = (HashMap<String, String>) mAdapter.getItem(position);
                imuCode.setText(o.get("imuCode"));
                imuName.setText("ইনজেকটেবল");//imuName.setText(o.get("imuName"));
                numOfDose.setText(o.get("numOfDose"));
                // sideeffect.setText(o.get("imuName"));
                if (o.get("imuName").equals("2"))
                    sideeffect.setText("না");
                else if (o.get("imuName").equals("1"))
                    sideeffect.setText("হ্যাঁ");
                dategiven.setText(Global.DateConvertDMY(o.get("dategiven")));

                ((ImageButton) convertView.findViewById(R.id.btndate)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        DiaplayPopup(g.getGeneratedId(), o.get("numOfDose"), o.get("imuName"), o.get("dategiven"));
                    }
                });


            } catch (Exception ex) {

            }
            return convertView;
        }
    }

    Dialog popupView = null;

    //dozeId AS imuCode, sideEffect AS imuName, dozeId AS numOfDose, dozeDate
    private void DiaplayPopup(String healthId, final String dozeId, String sideEffect, String dozeDate) {
        popupView = new Dialog(WomanInjectable.this);
        popupView.setTitle(dozeId);
        popupView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popupView.setContentView(R.layout.injectablepopup);
        popupView.setCancelable(true);
        popupView.setCanceledOnTouchOutside(false);
        ((TextView) popupView.findViewById(R.id.imuName)).setText("ইনজেকটেবল");
        ((TextView) popupView.findViewById(R.id.datetobegiven)).setText(dozeId);
        if (sideEffect.equalsIgnoreCase("1")) {
            ((RadioButton) popupView.findViewById(R.id.rdoSideEffect1)).setChecked(true);
        } else if (sideEffect.equalsIgnoreCase("2")) {
            ((RadioButton) popupView.findViewById(R.id.rdoSideEffect2)).setChecked(true);
        }
        ((TextView) popupView.findViewById(R.id.popupDate)).setText(Global.DateConvertDMY(dozeDate));
       /* ((TextView)popupView.findViewById(R.id.imuDose)).setText(dose);*/
        //((TextView)popupView.findViewById(R.id.imuName)).setText(imuName);

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
                        Connection.MessageBox(WomanInjectable.this, "তারিখ লিখুন");
                        return;
                    }

                    if (dozeId != null) {
                        sql = "SELECT doseId FROM womanInjectable WHERE doseId='" + dozeId + "' AND HealthId = '" + g.getGeneratedId() + "'";
                        if (!C.Existence(sql)) {
                            sql = "INSERT INTO womanInjectable (healthId,providerId,doseId,doseDate,sideEffect,systemEntryDate,modifyDate,upload)";
                            sql = sql + "VALUES ('" + g.getGeneratedId() + "', '" + g.getProvCode() + "','" + ((TextView) findViewById(R.id.txtSNo)).getText().toString() + "','" +
                                    Global.DateConvertYMD(dtDozeDate.getText().toString()) + "','2','" + Global.DateTimeNowYMDHMS() + "','" + Global.DateTimeNowYMDHMS() + "','2')";
                            C.Save(sql);
                        } else {
                            /*sql = "UPDATE womanInjectable SET sideEffect = '" + String.valueOf(((TextView) popupView.findViewById(R.id.popupDate)).getText().toString()) +
                                    "' , upload = '2' , modifyDate = '"+ Global.DateTimeNowYMDHMS() + "' WHERE healthId = '" + g.getHealthID() +"' AND dozeId = '"+ dozeId +"'" ;
*/
                            sql = "Update womanInjectable Set Upload='2',";
                            if (((RadioButton) popupView.findViewById(R.id.rdoSideEffect1)).isChecked()) {
                                sql += "sideEffect = '1',";
                            } else if (((RadioButton) popupView.findViewById(R.id.rdoSideEffect2)).isChecked()) {
                                sql += "sideEffect = '2',";
                            }
                            sql += "modifyDate = '" + Global.DateTimeNowYMDHMS() + "'";
                            sql += " Where healthId='" + g.getGeneratedId() + "' AND doseId = '" + dozeId + "'";
                            C.Save(sql);
                        }

                        popupView.dismiss();
                        RefreshGrid(false);
                    }

                } catch (Exception ex) {
                    Connection.MessageBox(WomanInjectable.this, ex.getMessage());
                    return;
                }

            }
        });

        /*((ImageButton)popupView.findViewById(R.id.btndate)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VariableID = "btndate";
                showDialog(DATE_DIALOG);

            }
        });*/

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

            if (VariableID.equals("btnDozeDate")) {
                dtpDate = (TextView) findViewById(R.id.dtDozeDate);
            }

            if (VariableID.equals("btndate")) {
                dtpDate = (TextView) findViewById(R.id.popupDate);
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

                    if (VariableID.equals("btnDozeDate")) {
                        Connection.MessageBox(WomanInjectable.this, "তারিখ আজকের তারিখের সমান অথবা কম হতে হবে।");
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
