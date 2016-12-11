package com.data.rhis2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.KeyguardManager;
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
import android.text.Editable;
import android.text.TextWatcher;
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

public class episession extends Activity {

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
    private String TableName;
    SimpleAdapter mSchedule;
    SimpleAdapter eList;

    ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> evmylist = new ArrayList<HashMap<String, String>>();
    Connection C;
    Global g;
    private static final int UPDATEDONE = 900;
    //Spinner spnEPIBlock;
    LinearLayout secdate;
    EditText txtdate;
    EditText txtBcgtime;
    EditText txtMrtime;
    EditText txtHumtime;
    LinearLayout secBcgtime;
    LinearLayout secdate1;
    EditText txtdate1;
    TextView txtCenterName;

    EditText txtSearch;
    /* private ProgressDialog pDialog;
     private static String vill;
     private static String bari;
     private static String hhno;
     private static String hhhead;
     private static String totalmember;
     private String ErrMsg;
     private static String vdate;

     TextView txtHHNo;
     RadioGroup rdogrpPAddr;
     RadioButton rdoPAddr1;
     RadioButton rdoPAddr2;
     EditText txtPermaAddress;


     RadioGroup rdogrpVGFCard;
     RadioButton rdoVGFCard1;
     RadioButton rdoVGFCard2;

     LinearLayout secPAddr;
     LinearLayout secPermaAddress;
     LinearLayout secReligion;*/
    String StartTime;

    //TextView FPMethod;
    // TextView LMP;
    // TextView EDD;

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mnuclose, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder adb = new AlertDialog.Builder(episession.this);
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
            setContentView(R.layout.episession);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();
            con = this;
            TableName = "sessionMaster";
            txtSearch = (EditText) findViewById(R.id.txtSearch);
            secdate1 = (LinearLayout) findViewById(R.id.secdate1);
            txtdate1 = (EditText) findViewById(R.id.txtdate1);
            txtdate1.setText(g.getepiScheduleDate());
            txtdate1.setEnabled(false);
            txtCenterName = (TextView) findViewById(R.id.txtCenterName);
            txtCenterName.setText(g.getepiCenterName());
            secdate = (LinearLayout) findViewById(R.id.secdate);
            txtdate = (EditText) findViewById(R.id.txtdate);
            txtdate.setEnabled(false);
            txtBcgtime = (EditText) findViewById(R.id.txtBcgtime);
            txtMrtime = (EditText) findViewById(R.id.txtMrtime);
            txtHumtime = (EditText) findViewById(R.id.txtHumtime);
            secBcgtime = (LinearLayout) findViewById(R.id.secBcgtime);
            final ImageButton cmdSearch = (ImageButton) findViewById(R.id.cmdSearch);
            cmdSearch.setBackgroundResource(R.drawable.search);

            final Button cmdNrcEpiReg = (Button) findViewById(R.id.cmdNrcEpiReg);
            cmdNrcEpiReg.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    // DisplayEpiReg();
                    Intent f1 = new Intent(getApplicationContext(), EpiNonRegistration.class);
                    startActivity(f1);
                }
            });
            ((ImageButton) findViewById(R.id.btndate)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btndate";
                    showDialog(DATE_DIALOG);
                }
            });
            ((ImageButton) findViewById(R.id.btndate1)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btndate1";
                    showDialog(DATE_DIALOG);
                }
            });

            txtdate.addTextChangedListener(new TextWatcher() {

                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                    if (!s.equals("")) {
                        final ListView list = (ListView) findViewById(R.id.listepiIndex);
                        DataRetrieve("", list, "active", "");
                    }

                }

                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {

                }

                public void afterTextChanged(Editable s) {

                }
            });

            ((ImageButton) findViewById(R.id.btnBcgtime)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btnBcgtime";
                    showDialog(TIME_DIALOG);
                }
            });

            ((ImageButton) findViewById(R.id.btnMrtime)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btnMrtime";
                    showDialog(TIME_DIALOG);
                }
            });

            ((ImageButton) findViewById(R.id.btnHumtime)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btnHumtime";
                    showDialog(TIME_DIALOG);
                }
            });

            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Activity.KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
            lock.disableKeyguard();

            LoadDataMemberList();
            DataSearch(g.getepiSchedulerId());
            Button cmdSave = (Button) findViewById(R.id.cmdSave);
            cmdSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    DataSave();

                    // finish();

                }
            });


        } catch (Exception ex) {
            Connection.MessageBox(episession.this, ex.getMessage());
            return;
        }
    }


    private void DataSave() {
        try {

          /*  String DV="";
            String DV1="";

            DV = Global.DateValidate(dtpregDate.getText().toString());
            DV1 = Global.DateValidate(dtpdob.getText().toString());


            if(DV.length()!=0 & secregDate.isShown())
            {
                Connection.MessageBox(EpiRegistration.this, DV);
                dtpregDate.requestFocus();
                return;
            }


            if(DV.length()!=0 & secdob.isShown())
            {
                Connection.MessageBox(EpiRegistration.this, DV);
                dtpdob.requestFocus();
                return;
            }
*/
            if (txtdate.getText().toString().length() == 0 & secdate.isShown()) {
                Connection.MessageBox(episession.this, "Required field:Registration no..");
                txtdate.requestFocus();
                return;
            } else if (txtBcgtime.getText().toString().length() == 0 & secBcgtime.isShown()) {
                Connection.MessageBox(episession.this, "Required field:Registration no..");
                txtBcgtime.requestFocus();
                return;
            } else if (txtMrtime.getText().toString().length() == 0 & secBcgtime.isShown()) {
                Connection.MessageBox(episession.this, "Required field:Registration no..");
                txtMrtime.requestFocus();
                return;
            } else if (txtHumtime.getText().toString().length() == 0 & secBcgtime.isShown()) {
                Connection.MessageBox(episession.this, "Required field:Registration no..");
                txtHumtime.requestFocus();
                return;
            }

            String SQL = "";

            if (!C.Existence("Select schedulerId,upload from " + TableName + "  Where schedulerId='" + g.getepiSchedulerId() + "'")) {
                SQL = "Insert into " + TableName + "(schedulerId,upload)Values('" + g.getepiSchedulerId() + "','2')";
                C.Save(SQL);
            }

            SQL = "Update " + TableName + " Set ";
            SQL += "vaccineDate = '" + Global.DateConvertYMD(txtdate.getText().toString()) + "',";
            SQL += "bcgMixTime = '" + txtBcgtime.getText().toString() + "',";
            SQL += "mrMixTime = '" + txtMrtime.getText().toString() + "',";
            SQL += "humMixTime = '" + txtHumtime.getText().toString() + "',";
            SQL += "systemEntryDate = '" + Global.DateTimeNowYMDHMS() + "',";
            SQL += "upload ='2'";
            SQL += "  Where schedulerId='" + g.getepiSchedulerId() + "'";
            // SQL+=" where Cluster='"+ cluster +"' and Block='"+ block +"' and HH='"+ hh +"' and StudyID='"+ txtStudyId.getText() +"'and Visit='"+ txtVisitNo.getText()+"'";
            C.Save(SQL);
            Connection.MessageBox(episession.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
            // finish();
        } catch (Exception e) {
            Connection.MessageBox(episession.this, e.getMessage());
            return;
        }
    }

    private void DataSearch(String schedulerId) {
        try {

            RadioButton rb;
            Cursor cur = C.ReadData("Select * from " + TableName + "  Where schedulerId='" + schedulerId + "'");
            cur.moveToFirst();
            while (!cur.isAfterLast()) {

                txtdate.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("vaccineDate"))));
                txtBcgtime.setText(cur.getString(cur.getColumnIndex("bcgMixTime")));
                txtMrtime.setText(cur.getString(cur.getColumnIndex("mrMixTime")));
                txtHumtime.setText(cur.getString(cur.getColumnIndex("humMixTime")));


                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(episession.this, e.getMessage());
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

            //  if(heading ==true)
            // {
            View header = getLayoutInflater().inflate(R.layout.episessionheading, null);
            list.addHeaderView(header);
            // }

            DataRetrieve("", list, "active", "");
           /* spnEPIBlock = (Spinner) findViewById(R.id.spnEPIBlock);

            spnEPIBlock.setAdapter(C.getArrayAdapter("Select '  'as BCode union Select BCode||'-'||BNameBan as BCode from HABlock order by BCode asc"));

            spnEPIBlock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    String val = spnEPIBlock.getSelectedItemPosition() == 0 ? "" : Global.Left(spnEPIBlock.getSelectedItem().toString(), 2);
                    if (val.length() > 0) {

                        DataRetrieve("", list, "active", val);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });*/


            //HHDataSearch(g.getDistrict(), g.getUpazila(), g.getUnion(), g.getMouza(), g.getVillage(), txtHHNo.getText().toString());


        } catch (Exception ex) {
            Connection.MessageBox(episession.this, ex.getMessage());
            return;
        }
    }


    //Retrieve member list
    //***************************************************************************************************************************
    public void DataRetrieve(String HH, ListView list, String ActiveOrAll, String EPIBlock) {
        try {
            String SQLStr = "";
            /*SQLStr = "Select Dist, Upz, UN, Mouza, Vill, ProvType as provtype,ProvCode as provcode, HHNo, SNo as SNo, ifnull(HealthID,'') as HealthID, ifnull(NameEng,'') as NameEng,";
            SQLStr += " ifnull(NameBang,'') as NameBang, ifnull(Rth,'') as Rth, ifnull(HaveNID,'') as HaveNID, ifnull(NID,'') as NID, ifnull(NIDStatus,'') as NIDStatus, ifnull(HaveBR,'') as HaveBR, ifnull(BRID,'') as BRID, ifnull(BRIDStatus,'') as BRIDStatus, ifnull(MobileNo1,'') as MobileNo1,";
            SQLStr += " ifnull(MobileNo2,'') as MobileNo2,ifnull(MobileYN,'')as MobileYN, ifnull(DOB,'') as DOB, Cast(((julianday(date('now'))-julianday(DOB))/365.25) as int) as Age,Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) as AgeM, ifnull(DOBSource,'') as DOBSource, ifnull(BPlace,'') as BPlace, ifnull(FNo,'') as FNo, ifnull(Father,'') as Father, ifnull(MNo,'') as MNo, ifnull(Mother,'') as Mother,";
            SQLStr += " ifnull(Sex,'') as Sex, ifnull(MS,'') as MS, ifnull(SPNO1,'') as SPNO1,ifnull(SPNO2,'') as SPNO2,ifnull(SPNO3,'') as SPNO3,ifnull(SPNO4,'') as SPNO4, ifnull(ELCONo,'') as ELCONo, ifnull(ELCODontKnow,'') as ELCODontKnow, ifnull(EDU,'') as EDU, ifnull(Rel,'') as Rel, ifnull(Nationality,'') as Nationality, ifnull(OCP,'') as OCP,ifnull(ExType,'')as ExType,ProvType as provtype, ProvCode as provcode";
            SQLStr += " from Member Where dist='"+ g.getDistrict() +"' and upz='"+ g.getUpazila() +"' and un='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and vill='"+ g.getVillage() +"' and ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"' and Cast(((julianday(date('now'))-julianday(DOB))/365.25) as int) <=5 ";

*/
//Without mouza
           /* SQLStr = "Select epimaster.healthId As epiHealthId,epimaster.regNo AS regNo, epimaster.regDate AS regDate,epimaster.houseNo AS houseNo, epiMaster.brCertificateNo AS brCertificateNo," +
                    " ExDate, " +
                    " Dist, Upz, UN, Mouza, Vill, (select VILLAGENAME FROM Village WHERE VILLAGEID IN(SELECT Vill FROM Member)) VillageName, ProvType as provtype,ProvCode as provcode, HHNo, SNo as SNo, " +
                    " ifnull(member.HealthID,'') as HealthID, ifnull(NameEng,'') as NameEng," +
                    " ifnull(NameBang,'') as NameBang, ifnull(Rth,'') as Rth, ifnull(HaveNID,'') as HaveNID, ifnull(NID,'') as NID, " +
                    " ifnull(NIDStatus,'') as NIDStatus, ifnull(HaveBR,'') as HaveBR, ifnull(BRID,'') as BRID," +
                    "  ifnull(BRIDStatus,'') as BRIDStatus, ifnull(MobileNo1,'') as MobileNo1," +
                    " ifnull(MobileNo2,'') as MobileNo2,ifnull(MobileYN,'')as MobileYN, ifnull(DOB,'') as DOB, " +
                    " Cast(((julianday(date('now'))-julianday(DOB))/365.25) as int) as Age," +
                    " Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) as AgeM, ifnull(DOBSource,'') as DOBSource, " +
                    " ifnull(BPlace,'') as BPlace, ifnull(FNo,'') as FNo, ifnull(MNo,'') as MNo, " +
                    " ifnull(Sex,'') as Sex, ifnull(MS,'') as MS, ifnull(SPNO1,'') as SPNO1,ifnull(SPNO2,'') as SPNO2," +
                    " ifnull(SPNO3,'') as SPNO3,ifnull(SPNO4,'') as SPNO4, ifnull(ELCONo,'') as ELCONo, " +
                    " ifnull(ELCODontKnow,'') as ELCODontKnow, ifnull(EDU,'') as EDU, ifnull(Rel,'') as Rel, " +
                    " ifnull(Nationality,'') as Nationality, ifnull(OCP,'') as OCP," +
                    " ifnull(ExType,'')as ExType,ProvType as provtype, ProvCode as provcode" +
                    " from Member " +
                    " LEFT JOIN epimaster ON epimaster.healthId = Member.healthid" +

                    " WHERE Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) <=18";*/

            SQLStr = "Select distinct e.healthId As epiHealthId,e.regNo AS regNo, " +
                    " (case when e.regNo is null then '1' else '2' end) as regC, " +
                    " e.regDate AS regDate, " +
                    " (select VILLAGENAME FROM Village WHERE VILLAGEID IN(SELECT Vill FROM Member)) VillageName, " +
                    " ifnull(m.HealthID,'') as HealthID, " +
                    " ifnull(NameEng,'') as NameEng, ifnull(NameBang,'') as NameBang, ifnull(Rth,'') as Rth, " +
                    " ifnull(HaveNID,'') as HaveNID, ifnull(NID,'') as NID, ifnull(NIDStatus,'') as NIDStatus, " +
                    " ifnull(HaveBR,'') as HaveBR, ifnull(BRID,'') as BRID, ifnull(BRIDStatus,'') as BRIDStatus, " +
                    " ifnull(MobileNo1,'') as MobileNo1, ifnull(MobileNo2,'') as MobileNo2,ifnull(MobileYN,'')as MobileYN, " +
                    " ifnull(DOB,'') as DOB, Cast(((julianday(date('now'))-julianday(DOB))/365.25) as int) as Age, " +
                    " Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) as AgeM, ifnull(DOBSource,'') as DOBSource, " +
                    " ifnull(BPlace,'') as BPlace, ifnull(FNo,'') as FNo, ifnull(MNo,'') as MNo,  ifnull(Sex,'') as Sex " +
                    " from Member m LEFT JOIN Household h ON h.Dist= m.Dist and h.Upz= m.Upz  and h.UN= m.UN and h.Mouza= m.Mouza  and h.Vill= m.Vill " +
                    " inner join HABlock b on b.BCode=h.subBlock LEFT JOIN epiMaster e ON e.healthId = m.healthid WHERE Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) <=18  and m.healthId=e.healthId";
            //" AND Member.dist='"+ g.getDistrict() +"' and Member.upz='"+ g.getUpazila() +"' and Member.un='"+ g.getUnion() +"' and Member.Mouza='"+ g.getMouza() +"' and Member.vill='"+ g.getVillage() +"'";


            Cursor cur1 = C.ReadData(SQLStr);

            cur1.moveToFirst();
            mylist.clear();


            int i = 0;
            while (!cur1.isAfterLast()) {
                HashMap<String, String> map = new HashMap<String, String>();

                if (i == 0) {
                    //View header = getLayoutInflater().inflate(R.layout.memberlistheading, null);
                    //list.addHeaderView(header);
                }

                String HealthId = cur1.getString(cur1.getColumnIndex("HealthID"));

                map.put("HealthId", cur1.getString(cur1.getColumnIndex("HealthID")));
                map.put("regNo", cur1.getString(cur1.getColumnIndex("regNo")));
                map.put("regDate", Global.DateConvertDMY(cur1.getString(cur1.getColumnIndex("regDate"))));
                map.put("nameeng", cur1.getString(cur1.getColumnIndex("NameEng")));
                map.put("sex", cur1.getString(cur1.getColumnIndex("Sex")));
                map.put("dob", Global.DateConvertDMY(cur1.getString(cur1.getColumnIndex("DOB"))));
                map.put("mother", g.GetMotherName(C, HealthId));
                map.put("father", g.GetFatherName(C, HealthId));
                map.put("guardian", "");
                map.put("vill", cur1.getString(cur1.getColumnIndex("VillageName")));
                map.put("mobileno1", cur1.getString(cur1.getColumnIndex("MobileNo1")));
                map.put("mobileno2", cur1.getString(cur1.getColumnIndex("MobileNo2")));
                //map.put("houseNo", cur1.getString(cur1.getColumnIndex("houseNo")));

                String date = Global.DateConvertDMY(cur1.getString(cur1.getColumnIndex("DOB")));
                //   map.put("mothervitA", Global.addDays(date, 42));
                //  map.put("pentaPCV", Global.addDays(date, 42));
                //map.put("mrfirst", Global.addDays(date, 70));
                //map.put("mrsecond", Global.addDays(date, 70));

                //Mapping Immunization

                // map.put("bcg", g.GetImmunizationDateForChild(C, HealthId, "1"));

                //  map.put("penta1", g.GetImmunizationDateForChild(C, HealthId, "2"));
                //  map.put("penta2", g.GetImmunizationDateForChild(C, HealthId, "3"));
                //  map.put("penta3", g.GetImmunizationDateForChild(C, HealthId, "4"));

                //  map.put("pcv1", g.GetImmunizationDateForChild(C,HealthId, "5"));
                //  map.put("pcv2", g.GetImmunizationDateForChild(C,HealthId, "6"));
                // map.put("pcv3", g.GetImmunizationDateForChild(C,HealthId, "7"));

                // map.put("opv0", g.GetImmunizationDateForChild(C,HealthId, "8"));
                // map.put("opv1", g.GetImmunizationDateForChild(C,HealthId, "9"));
                //  map.put("opv2", g.GetImmunizationDateForChild(C,HealthId, "10"));
                // map.put("opv3", g.GetImmunizationDateForChild(C,HealthId, "11"));

                //map.put("ipv", g.GetImmunizationDateForChild(C,HealthId, "12"));

                //map.put("mr1", g.GetImmunizationDateForChild(C,HealthId, "13"));
                //map.put("mr2", g.GetImmunizationDateForChild(C, HealthId, "14"));


                // map.put("agem", cur1.getString(cur1.getColumnIndex("AgeM")));
                // map.put("totalimmucount", "");
                // map.put("nid", cur1.getString(cur1.getColumnIndex("NIDStatus")));
                // map.put("brcertificateno", cur1.getString(cur1.getColumnIndex("brCertificateNo")));
                // map.put("exdate", cur1.getString(cur1.getColumnIndex("ExDate")));
                // map.put("SNo", cur1.getString(cur1.getColumnIndex("SNo")));


                mylist.add(map);
                mSchedule = new SimpleAdapter(episession.this, mylist, R.layout.episessionrow, new String[]{"HealthID"},
                        new int[]{R.id.HealthID});
                list.setAdapter(new MemberListAdapter(this));

                i += 1;
                cur1.moveToNext();
            }
            cur1.close();


        } catch (Exception e) {
            Connection.MessageBox(episession.this, e.getMessage());
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
                convertView = inflater.inflate(R.layout.episessionrow, null);
            }

            try {

                TextView HealthID = (TextView) convertView.findViewById(R.id.HealthID);
                TextView regno = (TextView) convertView.findViewById(R.id.regno);
                // TextView regdate  =  (TextView) convertView.findViewById(R.id.regdate);
                TextView name = (TextView) convertView.findViewById(R.id.name);
                TextView sex = (TextView) convertView.findViewById(R.id.sex);
                TextView dob = (TextView) convertView.findViewById(R.id.dob);
                TextView mname = (TextView) convertView.findViewById(R.id.mname);
                TextView fname = (TextView) convertView.findViewById(R.id.fname);
                TextView oname = (TextView) convertView.findViewById(R.id.oname);
                TextView vill = (TextView) convertView.findViewById(R.id.vill);
                TextView mobile = (TextView) convertView.findViewById(R.id.mobile);
                Button epicard = (Button) convertView.findViewById(R.id.epicard);


                final HashMap<String, String> o = (HashMap<String, String>) mSchedule.getItem(position);


                HealthID.setText("হেল্‌থ আইডি : " + o.get("HealthId"));

                name.setText("শিশুর নাম : " + o.get("nameeng"));
                if (o.get("sex").equals("1"))
                    sex.setText("ছেলে/মেয়ে : " + "ছেলে");
                else if (o.get("sex").equals("2"))
                    sex.setText("ছেলে/মেয়ে : " + "মেয়ে");

                dob.setText("জন্ম তারিখ : " + o.get("dob"));
                mname.setText("মাতার নাম : " + o.get("mother"));
                fname.setText("পিতার নাম : " + o.get("father"));
                // oname.setText("");
                //vill.setText(o.get("vill"));

                regno.setText("রেজিঃ নং : " + o.get("regNo"));
                //  regdate.setText("রেজিঃ তারিখ : "+o.get("regDate"));
                mobile.setText("মোবাইল নম্বর: " + o.get("mobileno1"));


                final LinearLayout memtab = (LinearLayout) convertView.findViewById(R.id.memtab);

                if (g.GetIfImmunizationisGivenForAParticularDate(C, o.get("HealthId"), txtdate.getText().toString()).length() > 0) {
                    memtab.setBackgroundColor(Color.parseColor("#66CCFF"));
                } else {
                    memtab.setBackgroundColor(Color.parseColor("#FBF6D9"));
                }

                //
                //final TextView btnEpi = epicard;
                //epicard.setBackgroundResource(R.drawable.epicard);
                epicard.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        g.setHealthID(o.get("HealthId"));
                        g.setDOB(o.get("dob"));

                        runOnUiThread(new Runnable() {
                            public void run() {
                                DiaplayPopup(o.get("HealthId"), txtdate.getText().toString());
                            }
                        });

                    }
                });

                memtab.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        g.setHealthID(o.get("HealthId"));
                        g.setDOB(o.get("dob"));
                        DiaplayPopup(o.get("HealthId"), txtdate.getText().toString());

                        //    DiaplayPopup(o.get("HealthId"));

                    }
                });

            } catch (Exception ex) {

            }
            return convertView;
        }
    }


    Dialog popupView = null;
    Dialog popupbutton = null;


    private void DiaplayPopup(String healthId, String epidate) {

        popupView = new Dialog(episession.this);
        popupView.setTitle("");
        popupView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popupView.setContentView(R.layout.childimmunizationnew);
        popupView.setCancelable(true);
        popupView.setCanceledOnTouchOutside(false);
        ((TextView) popupView.findViewById(R.id.txtHealthID)).setText(healthId);

        //Bcg
        //Displaying DOB
        String dob = g.getDOB();
        if (Global.GetImmunizationDateForChild(C, g.getHealthID(), "1").length() > 0) {
            ((TextView) popupView.findViewById(R.id.col1date)).setText(Global.GetImmunizationDateForChild(C, g.getHealthID(), "1"));
        } else {
            ((TextView) popupView.findViewById(R.id.col1date)).setText(epidate);
        }

        ((TextView) popupView.findViewById(R.id.col2date)).setText(Global.addDays(((TextView) popupView.findViewById(R.id.col1date)).getText().toString(), 28));
        ((TextView) popupView.findViewById(R.id.col3date)).setText(Global.addDays(((TextView) popupView.findViewById(R.id.col2date)).getText().toString(), 28));
        ((TextView) popupView.findViewById(R.id.col4date)).setText(Global.addDays(((TextView) popupView.findViewById(R.id.col3date)).getText().toString(), 28));
        ((TextView) popupView.findViewById(R.id.col5date)).setText(Global.addDays(dob, 270));
        ((TextView) popupView.findViewById(R.id.col6date)).setText(Global.addDays(dob, 450));


        DisplayImuDateinButton(((Button) popupView.findViewById(R.id.row1firsttime)), "1");//Bcg
        BindButtonForEpiDatePopUp(((Button) popupView.findViewById(R.id.row1firsttime)), "1", "বিসিজি", Global.addDays(g.getDOB(), 42), ((Button) popupView.findViewById(R.id.row1firsttime)).getText().toString());

        //Penta Dose 1
        DisplayImuDateinButton(((Button) popupView.findViewById(R.id.row2firsttime)), "2");//Penta 1
        BindButtonForEpiDatePopUp(((Button) popupView.findViewById(R.id.row2firsttime)), "2", "পেন্টাভ্যালেন্ট", Global.addDays(g.getDOB(), 42), ((Button) popupView.findViewById(R.id.row2firsttime)).getText().toString());

        //Penta Dose 2
        DisplayImuDateinButton(((Button) popupView.findViewById(R.id.row2secondtime)), "3");//Penta 2

        if (Global.GetImmunizationDateForChild(C, g.getHealthID(), "2").length() > 0) {

            BindButtonForEpiDatePopUp(((Button) popupView.findViewById(R.id.row2secondtime)), "3", "পেন্টাভ্যালেন্ট", Global.addDays(Global.GetImmunizationDateForChild(C, g.getHealthID(), "2"), 28), ((Button) popupView.findViewById(R.id.row2secondtime)).getText().toString());
        }


        //Penta Dose 3
        DisplayImuDateinButton(((Button) popupView.findViewById(R.id.row2thirdtime)), "4");//Bcg
        if (Global.GetImmunizationDateForChild(C, g.getHealthID(), "3").length() > 0) {

            BindButtonForEpiDatePopUp(((Button) popupView.findViewById(R.id.row2thirdtime)), "4", "পেন্টাভ্যালেন্ট", Global.addDays(Global.GetImmunizationDateForChild(C, g.getHealthID(), "3"), 28), ((Button) popupView.findViewById(R.id.row2thirdtime)).getText().toString());
        }

        //OPV Dose 1
        DisplayImuDateinButton(((Button) popupView.findViewById(R.id.row3firsttime)), "8");//Opv
        BindButtonForEpiDatePopUp(((Button) popupView.findViewById(R.id.row3firsttime)), "8", "ওপিভি", Global.addDays(g.getDOB(), 42), ((Button) popupView.findViewById(R.id.row3firsttime)).getText().toString());

        //OPV Dose 2
        DisplayImuDateinButton(((Button) popupView.findViewById(R.id.row3secondtime)), "9");//Opv
        if (Global.GetImmunizationDateForChild(C, g.getHealthID(), "8").length() > 0) {
            BindButtonForEpiDatePopUp(((Button) popupView.findViewById(R.id.row3secondtime)), "9", "ওপিভি", Global.addDays(Global.GetImmunizationDateForChild(C, g.getHealthID(), "8"), 28), ((Button) popupView.findViewById(R.id.row3secondtime)).getText().toString());
        }
        //OPV Dose 3
        DisplayImuDateinButton(((Button) popupView.findViewById(R.id.row3thirdtime)), "10");//Opv
        if (Global.GetImmunizationDateForChild(C, g.getHealthID(), "9").length() > 0) {
            BindButtonForEpiDatePopUp(((Button) popupView.findViewById(R.id.row3thirdtime)), "10", "ওপিভি", Global.addDays(Global.GetImmunizationDateForChild(C, g.getHealthID(), "9"), 28), ((Button) popupView.findViewById(R.id.row3thirdtime)).getText().toString());
        }

        //PCV Dose 1
        DisplayImuDateinButton(((Button) popupView.findViewById(R.id.row4firsttime)), "5");//Pcv

        BindButtonForEpiDatePopUp(((Button) popupView.findViewById(R.id.row4firsttime)), "5", "পিসিভি", Global.addDays(g.getDOB(), 42), ((Button) popupView.findViewById(R.id.row4firsttime)).getText().toString());


        //PCV Dose 2
        DisplayImuDateinButton(((Button) popupView.findViewById(R.id.row4secondtime)), "6");//Pcv
        if (Global.GetImmunizationDateForChild(C, g.getHealthID(), "5").length() > 0) {
            BindButtonForEpiDatePopUp(((Button) popupView.findViewById(R.id.row4secondtime)), "6", "পিসিভি", Global.addDays(Global.GetImmunizationDateForChild(C, g.getHealthID(), "5"), 28), ((Button) popupView.findViewById(R.id.row4secondtime)).getText().toString());
        }

        //PCV Dose 3
        DisplayImuDateinButton(((Button) popupView.findViewById(R.id.row4fourthtime)), "7");//Pcv

        if (Global.GetImmunizationDateForChild(C, g.getHealthID(), "6").length() > 0) {

            BindButtonForEpiDatePopUp(((Button) popupView.findViewById(R.id.row4fourthtime)), "7", "পিসিভি", Global.addDays(Global.GetImmunizationDateForChild(C, g.getHealthID(), "6"), 28), ((Button) popupView.findViewById(R.id.row4fourthtime)).getText().toString());
        }
        //Ipv 1
        DisplayImuDateinButton(((Button) popupView.findViewById(R.id.row5thirdtime)), "12");//Ipv
        BindButtonForEpiDatePopUp(((Button) popupView.findViewById(R.id.row5thirdtime)), "12", "আইপিভি", Global.addDays(g.getDOB(), 98), ((Button) popupView.findViewById(R.id.row5thirdtime)).getText().toString());


        //MR 1
        DisplayImuDateinButton(((Button) popupView.findViewById(R.id.row6fifthtime)), "13");//Mr
        BindButtonForEpiDatePopUp(((Button) popupView.findViewById(R.id.row6fifthtime)), "13", "এমআর (হাম ও রুবেলা)", Global.addDays(g.getDOB(), 270), ((Button) popupView.findViewById(R.id.row6fifthtime)).getText().toString());


        //MR 2
        DisplayImuDateinButton(((Button) popupView.findViewById(R.id.row7sixthtime)), "14");//Mr
        BindButtonForEpiDatePopUp(((Button) popupView.findViewById(R.id.row7sixthtime)), "14", "হাম/এমআর (২য় ডোজ)", Global.addDays(g.getDOB(), 450), ((Button) popupView.findViewById(R.id.row7sixthtime)).getText().toString());


        Button btnCancel = (Button) popupView.findViewById(R.id.Cancel);
        btnCancel.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                popupView.dismiss();
            }
        });


        popupView.show();
    }


    private void DisplayImuDateinButton(Button btn, String imuCode) {
        String date = Global.GetImmunizationDateForChild(C, g.getHealthID(), imuCode);
        if (date.length() > 0)
            btn.setText(date);
        else
            // btn.setText(Global.DateNowDMY());
            btn.setText(".....");

    }

    private void BindButtonForEpiDatePopUp(final Button btn, final String imuCode, final String imuName, final String dateTobeGiven, final String dateGiven) {
        btn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                popupbutton = new Dialog(episession.this);
                popupbutton.setTitle("");
                popupbutton.requestWindowFeature(Window.FEATURE_NO_TITLE);
                popupbutton.setContentView(R.layout.epidate);
                popupbutton.setCancelable(true);
                popupbutton.setCanceledOnTouchOutside(false);

                ((TextView) popupbutton.findViewById(R.id.imuName)).setText(imuName);
                ((TextView) popupbutton.findViewById(R.id.datetobegiven)).setText(dateTobeGiven);

                if (!dateGiven.equalsIgnoreCase("....."))
                    ((TextView) popupbutton.findViewById(R.id.popupDate)).setText(dateGiven);

                if (dateGiven.equalsIgnoreCase("....."))
                    ((TextView) popupbutton.findViewById(R.id.popupDate)).setText(Global.DateNowDMY());

                Button btnCancel = (Button) popupbutton.findViewById(R.id.Cancel);
                btnCancel.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        popupbutton.dismiss();
                    }
                });

                ((ImageButton) popupbutton.findViewById(R.id.btndate)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        VariableID = "popupbtndate";
                        showDialog(DATE_DIALOG);

                    }
                });


                Button btnSave = (Button) popupbutton.findViewById(R.id.Save);
                btnSave.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub


                        String sql = "";
                        try {

                            if (String.valueOf(((TextView) popupbutton.findViewById(R.id.popupDate)).getText().toString()).length() <= 0) {
                                Connection.MessageBox(episession.this, "তারিখ লিখুন");
                                return;
                            }

                            if (imuCode.equalsIgnoreCase("1")) {

                            } else {
                                if (Global.DateDifferenceDays(String.valueOf(((TextView) popupbutton.findViewById(R.id.popupDate)).getText().toString()), String.valueOf(((TextView) popupbutton.findViewById(R.id.datetobegiven)).getText().toString())) < 0) {
                                    Connection.MessageBox(episession.this, "টিকা প্রদানের তারিখ, যে তারিখে দিতে হবে তা থেকে আগে হবে না");
                                    return;
                                }
                                if (Global.DateDifferenceDays(String.valueOf(((TextView) popupbutton.findViewById(R.id.popupDate)).getText().toString()), String.valueOf(Global.DateNowDMY())) > 0) {
                                    Connection.MessageBox(episession.this, "টিকা প্রদানের তারিখ আজকের তারিখের পরে হবে না");
                                    return;
                                }
                            }

                            if (imuCode != null) {

                                sql = "select * from immunizationHistory WHERE  HealthId = '" + g.getHealthID() + "' AND imuCode = '" + String.valueOf(imuCode) + "'";
                                if (!C.Existence(sql)) {
/*

                                sql = "Insert Into epiMaster(healthId , providerId , regNo , regDate , houseNo , brCertificateNo , remarks , systemEntryDate ,  upload) ";
                                sql = sql + "VALUES ('" + txtHealthId.getText().toString() + "', '" + g.getProvCode() + "','" + ((TextView) findViewById(R.id.txtRegNo)).getText().toString() + "','" +
                                        ((TextView) findViewById(R.id.txtRegDate)).getText().toString() + "','" + ((TextView) findViewById(R.id.txtHouse)).getText().toString() + "','" +
                                        ((TextView) findViewById(R.id.txtcert)).getText().toString() + "','" + ((TextView) findViewById(R.id.txtremarks)).getText().toString() + "','" + Global.DateTimeNowYMDHMS() + "','2'" +")";
*/

                                    sql = "INSERT INTO immunizationHistory (healthId, providerId, imuCode, imuDose, imuDate, systemEntryDate, upload)";
                                    sql = sql + "VALUES ('" + g.getHealthID() + "', '" + g.getProvCode() + "','" +
                                            String.valueOf(imuCode) + "','" + Global.GetImmunizationDose(C, String.valueOf(imuCode)) + "','" + String.valueOf(((TextView) popupbutton.findViewById(R.id.popupDate)).getText().toString()) + "','" + Global.DateTimeNowYMDHMS() + "','2')";

                                    C.Save(sql);
                                    DisplayImuDateinButton(btn, imuCode);//Bcg
                                } else {
                                    sql = "UPDATE immunizationHistory SET imuDate = '" + String.valueOf(((TextView) popupbutton.findViewById(R.id.popupDate)).getText().toString()) +
                                            "' , upload = '2' , modifyDate = '" + Global.DateTimeNowYMDHMS() + "' WHERE healthId = '" + g.getHealthID() + "' AND imuCode = '" + imuCode + "'";


                                    C.Save(sql);
                                    DisplayImuDateinButton(btn, imuCode);//Bcg
                                }

                                popupbutton.dismiss();
                                //  RefreshGrid(false);
                            }

                        } catch (Exception ex) {
                            Connection.MessageBox(episession.this, ex.getMessage());
                            return;
                        }

                    }
                });


                popupbutton.show();


            }
        });

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

            dtpDate = (TextView) findViewById(R.id.txtdate);

            if (VariableID.equals("btndate")) {
                dtpDate = (TextView) findViewById(R.id.txtdate);
            }

            if (VariableID.equals("btndate1")) {
                dtpDate = (TextView) findViewById(R.id.txtdate1);
            }

            if (VariableID.equals("popupbtndate")) {
                dtpDate = (TextView) popupbutton.findViewById(R.id.popupDate);
            }


            dtpDate.setText(new StringBuilder()
                    .append(Global.Right("00" + mDay, 2)).append("/")
                    .append(Global.Right("00" + mMonth, 2)).append("/")
                    .append(mYear));

            if (VariableID.equals("btndate")) {

                ((TextView) findViewById(R.id.txtbar)).setText(g.getDay(dtpDate.getText().toString()));
            }
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = sdf.format(c.getTime());
                Date date1 = sdf.parse(formattedDate);

                Date date2 = sdf.parse(dtpDate.getText().toString());

                if (date2.after(date1)) {

                    if (VariableID.equals("txtdate")) {
                        Connection.MessageBox(episession.this, "তারিখ আজকের তারিখের সমান অথবা কম হতে হবে।");
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

            tpTime = (EditText) findViewById(R.id.txtBcgtime);

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
                    .append(am_pm));

        }
    };
}
