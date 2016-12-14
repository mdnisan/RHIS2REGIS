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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import Common.Connection;
import Common.Global;

/**
 * Created by Nisan on 11/17/2015.
 */
public class EpiWomenlist extends Activity {
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mnuclose, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder adb = new AlertDialog.Builder(EpiWomenlist.this);
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
    EditText txtdate;
    TextView txtCenterName;
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
            setContentView(R.layout.epilistwomen);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();
            con = this;

            TableName = "epiMaster";
            TableName1 = "epiScheduler";

            // schedulerId="01";
            //   EB=C.ReturnSingleValue("Select count(*)from Member m LEFT JOIN Household h ON h.Dist= m.Dist and h.Upz= m.Upz  and h.UN= m.UN and h.Mouza= m.Mouza  and h.Vill= m.Vill inner join HABlock b on b.BCode=h.EPIBlock LEFT JOIN epiMaster e ON e.healthId = m.healthid WHERE Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) <=18");
            lblTCount = (TextView) findViewById(R.id.lblTCount);
            spnEPIBlock = (Spinner) findViewById(R.id.spnEPIBlock);
            txtdate = (EditText) findViewById(R.id.txtdate);
            txtSearch = (EditText) findViewById(R.id.txtSearch);
            list = (ListView) findViewById(R.id.listepiIndex);
            txtCenterName = (TextView) findViewById(R.id.txtCenterName);


            final Button cmdEpiReg = (Button) findViewById(R.id.cmdEpiReg);
            final Button cmdEpisec = (Button) findViewById(R.id.cmdEpisec);
            final ImageButton cmdSearch = (ImageButton) findViewById(R.id.cmdSearch);
            cmdSearch.setBackgroundResource(R.drawable.search);
            cmdEpiReg.setEnabled(false);
            cmdEpisec.setEnabled(false);
            //txtSearch.setEnabled(false);
            // cmdSearch.setEnabled(false);
            txtdate.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                    if (s.toString().length() > 0) {
                        String val = spnEPIBlock.getSelectedItemPosition() == 0 ? "" : Global.Left(spnEPIBlock.getSelectedItem().toString(), 2);
                        if (val.length() > 0) {
                            txtCenterName.setText("");

                            DataSearch(txtdate.getText().toString(), spnEPIBlock.getSelectedItemPosition());
                            if (txtCenterName.length() == 0) {
                                cmdEpiReg.setEnabled(false);
                                cmdEpisec.setEnabled(false);
                                // txtSearch.setEnabled(false);
                                // cmdSearch.setEnabled(false);
                            } else if (txtCenterName.length() > 0) {
                                cmdEpiReg.setEnabled(true);
                                cmdEpisec.setEnabled(true);
                                // txtSearch.setEnabled(true);
                                // cmdSearch.setEnabled(true);
                            }
                        }
                    }
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }
            });

            spnEPIBlock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    String val = spnEPIBlock.getSelectedItemPosition() == 0 ? "" : Global.Left(spnEPIBlock.getSelectedItem().toString(), 2);
                    if (val.length() > 0) {
                        if (txtdate.length() > 0) {
                            txtCenterName.setText("");
                            DataSearch(txtdate.getText().toString(), spnEPIBlock.getSelectedItemPosition());
                            if (txtCenterName.length() == 0) {
                                cmdEpiReg.setEnabled(false);
                                cmdEpisec.setEnabled(false);
                                // txtSearch.setEnabled(false);
                                //cmdSearch.setEnabled(false);
                            } else if (txtCenterName.length() > 0) {
                                cmdEpiReg.setEnabled(true);
                                cmdEpisec.setEnabled(true);
                                // txtSearch.setEnabled(true);
                                // cmdSearch.setEnabled(true);
                            }
                        }


                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            //DataSearch();
            ((ImageButton) findViewById(R.id.btndate)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btndate";
                    showDialog(DATE_DIALOG1);
                }
            });


            cmdEpiReg.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    // DisplayEpiReg();
                    Intent f1 = new Intent(getApplicationContext(), EpiNonRegistration.class);
                    startActivity(f1);
                }
            });


            cmdEpisec.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    // DisplayEpiReg();
                    Intent f1 = new Intent(getApplicationContext(), episession.class);
                    startActivity(f1);
                }
            });


            //HHDataSearch(g.getDistrict(), g.getUpazila(), g.getUnion(), g.getMouza(), g.getVillage(), txtHHNo.getText().toString());
           /* Button cmdSearch  = (Button) findViewById(R.id.cmdSearch);
            cmdSearch.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    DataRetrieve("", list, "active","");
                    //Intent f1 = new Intent(getApplicationContext(), EpiRegistration.class);
                    //startActivity(f1);
                }
            });*/

            LoadDataMemberList();

        } catch (Exception ex) {
            Connection.MessageBox(EpiWomenlist.this, ex.getMessage());
            return;
        }
    }


    private void DataSearch(String SDate, int EBlock) {
        try {


            Cursor cur = C.ReadData("Select * from epiScheduler Where strftime('%d/%m/%Y', date(scheduleDate))='" + SDate + "'and subBlockId='" + EBlock + "'");
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                txtCenterName.setText(cur.getString(cur.getColumnIndex("centerName")));
                g.setepiSchedulerId(cur.getString(cur.getColumnIndex("schedulerId")));


                // txtregisNo.setText(cur.getString(cur.getColumnIndex("regisNo")));


                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(EpiWomenlist.this, e.getMessage());
            return;
        }
    }


    private void DisplayEpiReg() {
        final Dialog popupView = new Dialog(EpiWomenlist.this);
        popupView.setTitle("EPI Registration");

        popupView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popupView.setContentView(R.layout.epireg);
        popupView.setCancelable(true);
        popupView.setCanceledOnTouchOutside(true);

        final EditText txtregisNo = (EditText) popupView.findViewById(R.id.txtregisNo);
        //final EditText dtpregDate = (EditText) popupView.findViewById(R.id.dtpregDate);

        //  dtpregDate.setEnabled(true);
        // final ImageButton btnregDate = (ImageButton) popupView.findViewById(R.id.btnregDate);
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


        String SQL = "Select * from epiMaster Where healthId='" + g.getHealthID() + "'";
        if (C.Existence(SQL)) {
            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();

            while (!cur.isAfterLast()) {
                txtregisNo.setText(cur.getString(cur.getColumnIndex("regNo")));
                //txtregisNo.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("regNo"))));

                VisitDate.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("regDate"))));

                cur.moveToNext();
            }
            cur.close();
        } else {
            Button cmdSave = (Button) popupView.findViewById(R.id.cmdSave);
            cmdSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    TableName = "epiMaster";
                    String SQL = "";
                    //String DV = "";
                    //DV = Global.DateValidate(VisitDate.getText().toString());
                    // DV1 = Global.DateValidate(dtpdob.getText().toString());
                    if (txtregisNo.getText().toString().length() == 0 & secregisNo.isShown()) {
                        Connection.MessageBox(EpiWomenlist.this, "Required field:Registration no..");
                        txtregisNo.requestFocus();
                        return;
                    }

                    if (VisitDate.getText().toString().length() == 0 & secregDate.isShown()) {
                        Connection.MessageBox(EpiWomenlist.this, "Required field:Registration Date..");
                        VisitDate.requestFocus();
                        return;
                    }

                    if (!C.Existence("Select schedulerId,healthId,providerId,upload from " + TableName + "  Where schedulerId='" + g.getepiSchedulerId() + "' and healthId='" + g.getHealthID() + "'and providerId='" + g.getProvCode() + "'")) {
                        SQL = "Insert into " + TableName + "(schedulerId,healthId,providerId,upload)Values('" + g.getepiSchedulerId() + "', '" + g.getHealthID() + "', '" + g.getProvCode() + "','2')";
                        C.Save(SQL);
                    }


                    SQL = "Update " + TableName + " Set ";
                    // SQL+="schedulerId= '"+schedulerId+"',";
                    //SQL += "healthId = '" + g.getHealthID() + "',";
                    // SQL += "providerId = '" + g.getProvCode() + "',";
                    SQL += "houseNo='',";
                    SQL += "regNo = '" + txtregisNo.getText().toString() + "',";
                    SQL += "regDate = '" + Global.DateConvertYMD(VisitDate.getText().toString()) + "',";
                    SQL += "brCertificateNo ='',";
                    SQL += "brDate ='',";
                    SQL += "remarks ='',";
                    SQL += "systemEntryDate = '" + Global.DateTimeNowYMDHMS() + "',";
                    SQL += "upload ='2'";
                    SQL += "  Where schedulerId='" + g.getepiSchedulerId() + "' and healthId='" + g.getHealthID() + "'and providerId='" + g.getProvCode() + "'";
                    // SQL+=" where Cluster='"+ cluster +"' and Block='"+ block +"' and HH='"+ hh +"' and StudyID='"+ txtStudyId.getText() +"'and Visit='"+ txtVisitNo.getText()+"'";
                    C.Save(SQL);
                    Connection.MessageBox(EpiWomenlist.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
                    finish();
                    Intent f1 = new Intent(getApplicationContext(), EpiWomenlist.class);
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
        /*Button cmdEPICard = (Button) popupView.findViewById(R.id.cmdEPICard);
        cmdEPICard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                //popupView.cancel();
                Intent f1 = new Intent(getApplicationContext(),EpiList.class);
                startActivity(f1);
            }
        });*/
    }


    private void DataSave() {
        try {


          /*  String DV="";
            String DV1="";

            DV = Global.DateValidate(dtpregDate.getText().toString());
            DV1 = Global.DateValidate(dtpdob.getText().toString());
            if(txtregisNo.getText().toString().length()==0 & secregisNo.isShown())
            {
                Connection.MessageBox(EpiRegistration.this, "Required field:Registration no..");
                txtregisNo.requestFocus();
                return;
            }

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
            // SQL+="regDate = '"+ Global.DateConvertYMD(dtpregDate.getText().toString()) +"',";
            SQL += "brCertificateNo ='',";
            SQL += "brDate ='',";
            SQL += "remarks ='',";
            SQL += "systemEntryDate = '" + Global.DateTimeNowYMDHMS() + "',";
            SQL += "upload ='2'";
            SQL += "  Where schedulerId='" + g.getepiSchedulerId() + "' and healthId='" + g.getHealthID() + "'and providerId='" + g.getProvCode() + "'";
            // SQL+=" where Cluster='"+ cluster +"' and Block='"+ block +"' and HH='"+ hh +"' and StudyID='"+ txtStudyId.getText() +"'and Visit='"+ txtVisitNo.getText()+"'";
            C.Save(SQL);
            Connection.MessageBox(EpiWomenlist.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
            finish();
        } catch (Exception e) {
            Connection.MessageBox(EpiWomenlist.this, e.getMessage());
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
            View header = getLayoutInflater().inflate(R.layout.epilistwomenheading, null);
            list.addHeaderView(header);
            // }


            spnEPIBlock.setAdapter(C.getArrayAdapter("Select ' All 'as BCode union Select BCode||'-'||BNameBan as BCode from HABlock order by BCode asc"));

            spnEPIBlock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    String val = spnEPIBlock.getSelectedItemPosition() == 0 ? "" : Global.Left(spnEPIBlock.getSelectedItem().toString(), 2);
                    if (val.length() >= 0) {
                        totalCount = 0;
                        //
                        //DataRetrieve("", list, "active", val);
                        DataRetrieve("", list, "active", Global.Left(spnEPIBlock.getSelectedItem().toString(), 2), txtSearch.getText().toString());
                        lblTCount.setText(String.valueOf(totalCount));
                        //lblTCount.setText(EB);
                        //  lblTCount.setText(GetTotalTotalBlock(Global.Left(spnEPIBlock.getSelectedItem().toString(), 2)));

                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            // ImageButton cmdSearch  = (ImageButton) findViewById(R.id.cmdSearch);
            final ImageButton cmdSearch = (ImageButton) findViewById(R.id.cmdSearch);
            cmdSearch.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    totalCount = 0;
                    if (txtSearch.length() == 0) {
                        Connection.MessageBox(EpiWomenlist.this, "Required field:Search..");
                        txtSearch.requestFocus();
                        return;
                    }
                    DataRetrieve("", list, "active", Global.Left(spnEPIBlock.getSelectedItem().toString(), 2), txtSearch.getText().toString());
                    lblTCount.setText(String.valueOf(totalCount));

                    //Intent f1 = new Intent(getApplicationContext(), EpiRegistration.class);
                    //startActivity(f1);
                }
            });


            DataRetrieve("", list, "active", Global.Left(spnEPIBlock.getSelectedItem().toString(), 2), "");
        } catch (Exception ex) {
            Connection.MessageBox(EpiWomenlist.this, ex.getMessage());
            return;
        }
    }


    //Retrieve member list
    //***************************************************************************************************************************
    //public void DataRetrieve(String HH, ListView list, String ActiveOrAll, String EPIBlock)
    int totalCount = 0;


    public void DataRetrieve(String HH, ListView list, String ActiveOrAll, String EPIBlock, String healthId) {
        try {
            String SQLStr = "";

// :EPIBlock Search

            if (spnEPIBlock.getSelectedItemPosition() == 0 & txtSearch.length() == 0) {
                SQLStr = "Select epimaster.healthId As epiHealthId,epimaster.regNo AS regNo, (case when epimaster.regNo is not null then '2' else '1' end) as regC, strftime('%d/%m/%Y', date(epimaster.regDate)) AS regDate,epimaster.houseNo AS houseNo, epiMaster.brCertificateNo AS brCertificateNo," +
                        " strftime('%d/%m/%Y', date(ExDate)) AS ExDate, " +
                        " Dist, Upz, UN, Mouza, Vill, (select VILLAGENAME FROM Village WHERE VILLAGEID IN(SELECT Vill FROM Member)) VillageName, ProvType as provtype,ProvCode as provcode, HHNo, SNo as SNo, " +
                        " ifnull(member.HealthID,'') as HealthID, ifnull(NameEng,'') as NameEng," +
                        " ifnull(NameBang,'') as NameBang, ifnull(Rth,'') as Rth, ifnull(HaveNID,'') as HaveNID, ifnull(NID,'') as NID, " +
                        " ifnull(NIDStatus,'') as NIDStatus, ifnull(HaveBR,'') as HaveBR, ifnull(BRID,'') as BRID," +
                        "  ifnull(BRIDStatus,'') as BRIDStatus, ifnull(MobileNo1,'') as MobileNo1," +
                        " ifnull(MobileNo2,'') as MobileNo2,ifnull(MobileYN,'')as MobileYN,strftime('%d/%m/%Y', date(ifnull(DOB,''))) AS DOB, " +
                        " Cast(((julianday(date('now'))-julianday(DOB))/365.25) as int) as Age," +
                        " Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) as AgeM, ifnull(DOBSource,'') as DOBSource, " +
                        " ifnull(BPlace,'') as BPlace, ifnull(FNo,'') as FNo, ifnull(MNo,'') as MNo, " +
                        " ifnull(Sex,'') as Sex, ifnull(MS,'') as MS, ifnull(SPNO1,'') as SPNO1,ifnull(SPNO2,'') as SPNO2," +
                        " ifnull(SPNO3,'') as SPNO3,ifnull(SPNO4,'') as SPNO4, ifnull(ELCONo,'') as ELCONo, " +
                        " ifnull(ELCODontKnow,'') as ELCODontKnow, ifnull(EDU,'') as EDU, ifnull(Rel,'') as Rel, " +
                        " ifnull(Nationality,'') as Nationality, ifnull(OCP,'') as OCP," +
                        " ifnull(ExType,'')as ExType " +
                        " from Member " +
                        " LEFT JOIN epiMaster epimaster ON epimaster.healthId = Member.healthid" +
                        " WHERE cast(Age as int)>=15 and cast(Age as int)<=49  and sex='2'";
                // " WHERE Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) <=18";

            } else if (spnEPIBlock.getSelectedItemPosition() > 0 & txtSearch.length() == 0) {


                SQLStr = "Select epimaster.healthId As epiHealthId,epimaster.regNo AS regNo, (case when epimaster.regNo is not null then '2' else '1' end) as regC,strftime('%d/%m/%Y', date(epimaster.regDate)) AS regDate,epimaster.houseNo AS houseNo, epiMaster.brCertificateNo AS brCertificateNo," +
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
                        " ifnull(SPNO3,'') as SPNO3,ifnull(SPNO4,'') as SPNO4, ifnull(ELCONo,'') as ELCONo, " +
                        " ifnull(ELCODontKnow,'') as ELCODontKnow, ifnull(EDU,'') as EDU, ifnull(Rel,'') as Rel, " +
                        " ifnull(Nationality,'') as Nationality, ifnull(OCP,'') as OCP," +
                        " ifnull(ExType,'')as ExType " +
                        " from Member " +
                        " LEFT JOIN Household h ON h.Dist= Member.Dist and h.Upz= Member.Upz  and h.UN= Member.UN and h.Mouza= Member.Mouza  and h.Vill= Member.Vill" +
                        " inner join HABlock b on b.BCode=h.EPIBlock" +
                        " LEFT JOIN epiMaster epimaster ON epimaster.healthId = Member.healthid" +
                        " WHERE cast(Age as int)>=15 and cast(Age as int)<=49 and sex='2' and h.EPIBlock='" + EPIBlock + "'";
                //" WHERE Cast(((julianday(date('now'))-julianday(Member.DOB))/30.4) as int) <=18 and h.EPIBlock='" +EPIBlock+ "'";


            } else if (spnEPIBlock.getSelectedItemPosition() > 0 & txtSearch.length() > 0) {
                SQLStr = "Select epimaster.healthId As epiHealthId,epimaster.regNo AS regNo, (case when epimaster.regNo is not null then '2' else '1' end) as regC,strftime('%d/%m/%Y', date(epimaster.regDate)) AS regDate,epimaster.houseNo AS houseNo, epiMaster.brCertificateNo AS brCertificateNo," +
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
                        " ifnull(SPNO3,'') as SPNO3,ifnull(SPNO4,'') as SPNO4, ifnull(ELCONo,'') as ELCONo, " +
                        " ifnull(ELCODontKnow,'') as ELCODontKnow, ifnull(EDU,'') as EDU, ifnull(Rel,'') as Rel, " +
                        " ifnull(Nationality,'') as Nationality, ifnull(OCP,'') as OCP," +
                        " ifnull(ExType,'')as ExType " +
                        " from Member " +
                        " LEFT JOIN Household h ON h.Dist= Member.Dist and h.Upz= Member.Upz  and h.UN= Member.UN and h.Mouza= Member.Mouza  and h.Vill= Member.Vill" +
                        " inner join HABlock b on b.BCode=h.EPIBlock" +
                        " LEFT JOIN epiMaster epimaster ON epimaster.healthId = Member.healthid" +
                        " WHERE cast(Age as int)>=15 and cast(Age as int)<=49 and sex='2' and h.EPIBlock='" + EPIBlock + "' and member.HealthID='" + healthId + "'";
                // " WHERE Cast(((julianday(date('now'))-julianday(Member.DOB))/30.4) as int) <=18 and h.EPIBlock='"+ EPIBlock+"' and epimaster.healthId='" +healthId+ "'";

            } else if (spnEPIBlock.getSelectedItemPosition() == 0 & txtSearch.length() > 0) {
                SQLStr = "Select distinct epimaster.healthId As epiHealthId,epimaster.regNo AS regNo, (case when epimaster.regNo is not null then '2' else '1' end) as regC,strftime('%d/%m/%Y', date(epimaster.regDate)) AS regDate,epimaster.houseNo AS houseNo, epiMaster.brCertificateNo AS brCertificateNo," +
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
                        " ifnull(SPNO3,'') as SPNO3,ifnull(SPNO4,'') as SPNO4, ifnull(ELCONo,'') as ELCONo, " +
                        " ifnull(ELCODontKnow,'') as ELCODontKnow, ifnull(EDU,'') as EDU, ifnull(Rel,'') as Rel, " +
                        " ifnull(Nationality,'') as Nationality, ifnull(OCP,'') as OCP," +
                        " ifnull(ExType,'')as ExType " +
                        " from Member " +
                        " LEFT JOIN Household h ON h.Dist= Member.Dist and h.Upz= Member.Upz  and h.UN= Member.UN and h.Mouza= Member.Mouza  and h.Vill= Member.Vill" +
                        " inner join HABlock b on b.BCode=h.EPIBlock" +
                        " LEFT JOIN epiMaster epimaster ON epimaster.healthId = Member.healthid" +
                        " WHERE cast(Age as int)>=15 and cast(Age as int)<=49 and sex='2' and h.EPIBlock<>'0' and member.HealthID='" + healthId + "'";
                // " WHERE Cast(((julianday(date('now'))-julianday(Member.DOB))/30.4) as int) <=18 and h.EPIBlock='"+ EPIBlock+"' and epimaster.healthId='" +healthId+ "'";

            }

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
                map.put("regc", cur1.getString(cur1.getColumnIndex("regC")));
                map.put("regDate", cur1.getString(cur1.getColumnIndex("regDate")));
                map.put("nameeng", cur1.getString(cur1.getColumnIndex("NameEng")));
                map.put("sex", cur1.getString(cur1.getColumnIndex("Sex")));
                map.put("dob", cur1.getString(cur1.getColumnIndex("DOB")));
                map.put("mother", g.GetMotherName(C, HealthId));
                map.put("father", g.GetFatherName(C, HealthId));
                map.put("guardian", "");
                map.put("vill", cur1.getString(cur1.getColumnIndex("VillageName")));
                map.put("mobileno1", cur1.getString(cur1.getColumnIndex("MobileNo1")));
                map.put("mobileno2", cur1.getString(cur1.getColumnIndex("MobileNo2")));
                map.put("houseNo", cur1.getString(cur1.getColumnIndex("houseNo")));

                String date = Global.DateConvertDMY(cur1.getString(cur1.getColumnIndex("DOB")));
                map.put("mothervitA", Global.addDays(date, 42));
                map.put("pentaPCV", Global.addDays(date, 42));
                map.put("mrfirst", Global.addDays(date, 70));
                map.put("mrsecond", Global.addDays(date, 70));

                //Mapping Immunization

                map.put("bcg", g.GetImmunizationDateForChild(C, HealthId, "1"));

                map.put("penta1", g.GetImmunizationDateForChild(C, HealthId, "2"));
                map.put("penta2", g.GetImmunizationDateForChild(C, HealthId, "3"));
                map.put("penta3", g.GetImmunizationDateForChild(C, HealthId, "4"));

                map.put("pcv1", g.GetImmunizationDateForChild(C, HealthId, "5"));
                map.put("pcv2", g.GetImmunizationDateForChild(C, HealthId, "6"));
               /* map.put("pcv3", g.GetImmunizationDateForChild(C,HealthId, "7"));

                map.put("opv0", g.GetImmunizationDateForChild(C,HealthId, "8"));
                map.put("opv1", g.GetImmunizationDateForChild(C,HealthId, "9"));
                map.put("opv2", g.GetImmunizationDateForChild(C,HealthId, "10"));
                map.put("opv3", g.GetImmunizationDateForChild(C,HealthId, "11"));

                map.put("ipv", g.GetImmunizationDateForChild(C,HealthId, "12"));

                map.put("mr1", g.GetImmunizationDateForChild(C,HealthId, "13"));
                map.put("mr2", g.GetImmunizationDateForChild(C, HealthId, "14"));


                map.put("agem", cur1.getString(cur1.getColumnIndex("AgeM")));
                map.put("totalimmucount", "");
                map.put("nid", cur1.getString(cur1.getColumnIndex("NIDStatus")));*/
                map.put("brcertificateno", cur1.getString(cur1.getColumnIndex("brCertificateNo")));
                map.put("exdate", cur1.getString(cur1.getColumnIndex("ExDate")));
                map.put("SNo", cur1.getString(cur1.getColumnIndex("SNo")));


                mylist.add(map);
                mSchedule = new SimpleAdapter(EpiWomenlist.this, mylist, R.layout.epilistwomenrow, new String[]{"HealthID"},
                        new int[]{R.id.HealthID});
                list.setAdapter(new MemberListAdapter(this));

                i += 1;
                cur1.moveToNext();
                totalCount += 1;

            }
            cur1.close();


        } catch (Exception e) {
            Connection.MessageBox(EpiWomenlist.this, e.getMessage());
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
                convertView = inflater.inflate(R.layout.epilistwomenrow, null);
            }

            try {

                final TableLayout memtab = (TableLayout) convertView.findViewById(R.id.memtab);
                TableRow memtabrow = (TableRow) convertView.findViewById(R.id.memtabrow);
                TextView HealthID = (TextView) convertView.findViewById(R.id.HealthID);
                TextView regno = (TextView) convertView.findViewById(R.id.regno);
                TextView regdate = (TextView) convertView.findViewById(R.id.regdate);
                TextView name = (TextView) convertView.findViewById(R.id.name);
                TextView sex = (TextView) convertView.findViewById(R.id.sex);
                TextView dob = (TextView) convertView.findViewById(R.id.dob);
                TextView mname = (TextView) convertView.findViewById(R.id.mname);
                TextView fname = (TextView) convertView.findViewById(R.id.fname);
                TextView oname = (TextView) convertView.findViewById(R.id.oname);
                TextView vill = (TextView) convertView.findViewById(R.id.vill);
                TextView mobile = (TextView) convertView.findViewById(R.id.mobile);
                TextView bari = (TextView) convertView.findViewById(R.id.bari);
                TextView vita = (TextView) convertView.findViewById(R.id.vita);
                TextView penta = (TextView) convertView.findViewById(R.id.penta);
                TextView mr1 = (TextView) convertView.findViewById(R.id.mr1);
                TextView mr2 = (TextView) convertView.findViewById(R.id.mr2);
                TextView bcg = (TextView) convertView.findViewById(R.id.bcg);
                TextView penta1 = (TextView) convertView.findViewById(R.id.penta1);
                TextView penta2 = (TextView) convertView.findViewById(R.id.penta2);
                TextView penta3 = (TextView) convertView.findViewById(R.id.penta3);
                TextView pcv1 = (TextView) convertView.findViewById(R.id.pcv1);
                TextView pcv2 = (TextView) convertView.findViewById(R.id.pcv2);
               /* TextView pcv3  =  (TextView) convertView.findViewById(R.id.pcv3);
                TextView opv0  =  (TextView) convertView.findViewById(R.id.opv0);
                TextView opv1  =  (TextView) convertView.findViewById(R.id.opv1);
                TextView opv2  =  (TextView) convertView.findViewById(R.id.opv2);
                TextView opv3  =  (TextView) convertView.findViewById(R.id.opv3);
                TextView ipv  =  (TextView) convertView.findViewById(R.id.ipv);
                TextView mrdate1  =  (TextView) convertView.findViewById(R.id.mrdate1);
                TextView mrdate2  =  (TextView) convertView.findViewById(R.id.mrdate2);
                ImageButton age12to23  =  (ImageButton) convertView.findViewById(R.id.age12to23);
                TextView allimmugiven  =  (TextView) convertView.findViewById(R.id.allimmugiven);
                TextView br  =  (TextView) convertView.findViewById(R.id.br);*/
                TextView brcertificate = (TextView) convertView.findViewById(R.id.brcertificate);
                TextView deathdate = (TextView) convertView.findViewById(R.id.deathdate);
                TextView remarks = (TextView) convertView.findViewById(R.id.remarks);


                final HashMap<String, String> o = (HashMap<String, String>) mSchedule.getItem(position);


                HealthID.setText(o.get("HealthId"));
                regno.setText(o.get("regNo"));
                regdate.setText(o.get("regDate"));
                name.setText(o.get("nameeng"));
                if (o.get("sex").equals("1"))
                    sex.setText("ছেলে");
                else if (o.get("sex").equals("2"))
                    sex.setText("মেয়ে");

                dob.setText(o.get("dob"));
                mname.setText(o.get("mother"));
                fname.setText(o.get("father"));
                oname.setText("");
                vill.setText(o.get("vill"));
                mobile.setText(o.get("mobileno1"));
                bari.setText(o.get("houseNo"));
                vita.setText(o.get("mothervitA"));
                penta.setText(o.get("pentaPCV"));
                mr1.setText(o.get("mrfirst"));
                mr2.setText(o.get("mrsecond"));

                bcg.setText(o.get("bcg"));
                penta1.setText(o.get("penta1"));
                penta2.setText(o.get("penta2"));
                penta3.setText(o.get("penta3"));
                pcv1.setText(o.get("pcv1"));
                pcv2.setText(o.get("pcv2"));
                /*pcv3.setText(o.get("pcv3"));
                opv0.setText(o.get("opv0"));
                opv1.setText(o.get("opv1"));
                opv2.setText(o.get("opv2"));
                opv3.setText(o.get("opv3"));
                ipv.setText(o.get("ipv"));
                mrdate1.setText(o.get("mr1"));
                mrdate2.setText(o.get("mr2"));
                if (o.get("regc").equals("2"))
                {
                    memtabrow.setBackgroundColor(Color.parseColor("#99cc33"));

                }
                //  Log.e("Healthid : " , )
                if (o.get("regc").equals("1"))
                {
                    memtabrow.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }

                // ImageButton memberImage = (ImageButton) convertView.findViewById(R.id.memberImage);

                Integer agemonth = Integer.valueOf(o.get("agem"));
                if (agemonth>=12 && agemonth<=23) {
                    age12to23.setBackgroundResource(R.drawable.correct_sign);
                }
                else
                {

                }


                allimmugiven.setText(o.get("totalimmucount"));
                br.setText(o.get("nid"));*/
                brcertificate.setText(o.get("brcertificateno"));
                deathdate.setText(o.get("exdate"));
                remarks.setText("");


                memtab.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        g.setHealthID(o.get("HealthId"));
                        if (txtCenterName.length() == 0) {
                            Connection.MessageBox(EpiWomenlist.this, "ই পি আই কেন্দ্র নাম খালি হতে পারে না");


                        } else {
                            DisplayEpiReg();
                        }

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
            case DATE_DIALOG1:
                return new DatePickerDialog(this, mDateSetListener1, g.mYear, g.mMonth - 1, g.mDay);


        }
        return null;
    }


    private DatePickerDialog.OnDateSetListener mDateSetListener1 = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear + 1;
            mDay = dayOfMonth;

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

                ((TextView) findViewById(R.id.txtbar)).setText(g.getDay(dtpDate.getText().toString()));
            }
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = sdf.format(c.getTime());
                Date date1 = sdf.parse(formattedDate);

                Date date2 = sdf.parse(dtpDate.getText().toString());

                if (date2.after(date1)) {

                    if (VariableID.equals("txtdate")) {
                        Connection.MessageBox(EpiWomenlist.this, "তারিখ আজকের তারিখের সমান অথবা কম হতে হবে।");
                        return;
                    }

                }


            } catch (ParseException ex) {
                ex.printStackTrace();
            }

        }
    };

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