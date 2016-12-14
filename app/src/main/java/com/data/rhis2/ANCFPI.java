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
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
 * Created by Nisan on 5/1/2016.
 */
public class ANCFPI extends Activity {
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
        AlertDialog.Builder adb = new AlertDialog.Builder(ANCFPI.this);
        switch (item.getItemId()) {
            case R.id.menuClose:
                adb.setTitle("Close");
                adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান?");
                adb.setNegativeButton("না", null);
                adb.setPositiveButton("হ্যাঁ", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        Intent f2 = new Intent(getApplicationContext(), ELCOForm.class);
                        startActivity(f2);
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

    Connection C;
    Global g;
    SimpleAdapter dataAdapter;
    Calendar c = Calendar.getInstance();
    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    private String TableName;
    private String TableNameFPI;
    private String TableANC;
    private String TableNameElcoVisit;

    TextView txtHealthID;

    LinearLayout secdelete1;

    LinearLayout secVDate;
    TextView VlblVDate;
    RadioGroup rdogrpVDate;
    RadioButton rdoVDate1;
    RadioButton rdoVDate2;
    TextView VlblVMonth;
    EditText txtVMonth;
    EditText dtpVDate;
    ImageButton btnVDate;

    LinearLayout secIron;
    TextView VlblIron;
    RadioGroup rdogrpIron;
    RadioButton rdoIron1;
    RadioButton rdoIron2;

   /* LinearLayout secIronUnit;
    TextView VlblIronQty;
    EditText txtIronQty;
    TextView VlblIronUnit;
    Spinner spnIronUnit;
    Spinner spnANCSource;*/

    LinearLayout secMiso;
    TextView VlblMiso;
    RadioGroup rdogrpMiso;
    RadioButton rdoMiso1;
    RadioButton rdoMiso2;

    LinearLayout secANCVisit;


    //..............FPI.............
    RadioGroup rdogrpVDateyn;
    RadioButton rdoVDateyn1;
    RadioButton rdoVDateyn2;

    RadioGroup rdogrpIron1;
    RadioButton rdoIron11;
    RadioButton rdoIron12;

    RadioGroup rdogrpMiso1;
    RadioButton rdoMiso11;
    RadioButton rdoMiso12;


    String StartTime;
    Button cmdSave;
    TextView VlblServiceId;
    LinearLayout secPregVisit;
    String sqlnew = "";
    String sqlupdate = "";
    String sqlupdate1 = "";
    String Sex = "";
    String Age = "";

    String pregnancyNo;
    String pregnancyNoDelivary = "";
    String LastDelivaryDate = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.ancfpi);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();
            TableNameFPI = "ancServiceFPI";
            TableName = "PregWomen";
            TableANC = "ancService";
            TableNameElcoVisit = "ELCOVisit";

            ELCOProfile e = new ELCOProfile();
            e.ELCOProfile(this, g.getHealthID());
            secMiso = (LinearLayout) findViewById(R.id.secMiso);
            secIron = (LinearLayout) findViewById(R.id.secIron);
            //call from elco
            if (g.getCallFrom().equals("elco")) {
                pregnancyNo = e.CurrentPregNumber(this, g.getGeneratedId());
            }
            //call from register
            else if (g.getCallFrom().equals("regis")) {
                pregnancyNo = e.LastPregNumber(this, g.getGeneratedId());
            } else if (g.getCallFrom().equals("1")) {
                pregnancyNo = e.LastPregNumber(this, g.getGeneratedId());
            } else if (g.getCallFrom().equals("HAregis")) {
                secMiso.setVisibility(View.GONE);
                // secIron.setVisibility(View.GONE);
                pregnancyNo = e.LastPregNumber(this, g.getGeneratedId());
                pregnancyNoDelivary = e.LastPregNumberFromDelivary(this, g.getGeneratedId());

                //pregnancyNo = e.LastPregNumber(this, g.getGeneratedId());
                //pregnancyNoDelivary = e.LastPregNumberFromDelivary(this, g.getGeneratedId());

                if (pregnancyNo.equalsIgnoreCase("0")) {
                    pregnancyNo = "1";
                } else if (pregnancyNo.equalsIgnoreCase(pregnancyNoDelivary)) {
                    LastDelivaryDate = e.LastDelivaryDateFromDelivary(this, g.getGeneratedId());
                    Integer DaysFromDelivaryDays = Global.DateDifferenceDays(Global.DateNowDMY(), Global.DateConvertDMY(LastDelivaryDate));
                    if (DaysFromDelivaryDays > 60) {
                        pregnancyNo = e.CurrentPregNumber(this, g.getGeneratedId());
                    } else {
                        pregnancyNo = e.LastPregNumber(this, g.getGeneratedId());
                    }
                } else if (!pregnancyNo.equalsIgnoreCase(pregnancyNoDelivary)) {
                    pregnancyNo = e.LastPregNumber(this, g.getGeneratedId());
                }
            }
            ////////Visit

            VlblIron = (TextView) findViewById(R.id.VlblIron);
            rdogrpIron = (RadioGroup) findViewById(R.id.rdogrpIron);
            rdoIron1 = (RadioButton) findViewById(R.id.rdoIron1);
            rdoIron2 = (RadioButton) findViewById(R.id.rdoIron2);


            VlblMiso = (TextView) findViewById(R.id.VlblMiso);
            rdogrpMiso = (RadioGroup) findViewById(R.id.rdogrpMiso);
            rdoMiso1 = (RadioButton) findViewById(R.id.rdoMiso1);
            rdoMiso2 = (RadioButton) findViewById(R.id.rdoMiso2);
            VlblServiceId = (TextView) findViewById(R.id.VlblServiceId);
            VlblServiceId.setVisibility(View.GONE);
            secVDate = (LinearLayout) findViewById(R.id.secVDate);
            VlblVDate = (TextView) findViewById(R.id.VlblVDate);
            rdogrpVDate = (RadioGroup) findViewById(R.id.rdogrpVDate);
            rdoVDate1 = (RadioButton) findViewById(R.id.rdoVDate1);
            rdoVDate2 = (RadioButton) findViewById(R.id.rdoVDate2);
            VlblVMonth = (TextView) findViewById(R.id.VlblVMonth);
            txtVMonth = (EditText) findViewById(R.id.txtVMonth);
            dtpVDate = (EditText) findViewById(R.id.dtpVDate);
            btnVDate = (ImageButton) findViewById(R.id.btnVDate);
            btnVDate.setEnabled(false);
            //dtpVDate.setText(Global.DateNowDMY());
            rdogrpVDate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup arg0, int id) {
                    if (id == R.id.rdoVDate1) {
                        dtpVDate.setVisibility(View.VISIBLE);
                        btnVDate.setVisibility(View.VISIBLE);
                        txtVMonth.setVisibility(View.GONE);
                        txtVMonth.setText("");
                        VlblVMonth.setVisibility(View.GONE);
                        btnVDate.requestFocus();
                    } else if (id == R.id.rdoVDate2) {
                        dtpVDate.setText("");
                        dtpVDate.setVisibility(View.GONE);
                        btnVDate.setVisibility(View.GONE);
                        txtVMonth.setVisibility(View.VISIBLE);
                        VlblVMonth.setVisibility(View.VISIBLE);
                        txtVMonth.requestFocus();
                    }
                }
            });
            e.PregnancyInfo(this, g.getGeneratedId(), pregnancyNo);
            String dtpLMP = e.getLMP();
            if (dtpLMP != null) {
                Integer DiffLMP_VD = Global.DateDifferenceDays(Global.DateNowDMY(), dtpLMP);
                if (DiffLMP_VD >= 224) {
                    if (g.getProvType().equalsIgnoreCase("2")) {
                        secMiso.setVisibility(View.GONE);
                    } else {
                        secMiso.setVisibility(View.VISIBLE);
                    }
                } else {
                    secMiso.setVisibility(View.GONE);
                }
            }

            btnVDate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btnVDate";
                    showDialog(DATE_DIALOG);
                }
            });


            //..............FPI.............

            rdogrpVDateyn = (RadioGroup) findViewById(R.id.rdogrpVDateyn);
            rdoVDateyn1 = (RadioButton) findViewById(R.id.rdoVDateyn1);
            rdoVDateyn2 = (RadioButton) findViewById(R.id.rdoVDateyn2);

            rdogrpIron1 = (RadioGroup) findViewById(R.id.rdogrpIron1);
            rdoIron11 = (RadioButton) findViewById(R.id.rdoIron11);
            rdoIron12 = (RadioButton) findViewById(R.id.rdoIron12);

            rdogrpMiso1 = (RadioGroup) findViewById(R.id.rdogrpMiso1);
            rdoMiso11 = (RadioButton) findViewById(R.id.rdoMiso11);
            rdoMiso12 = (RadioButton) findViewById(R.id.rdoMiso12);

            RadioButton rdoMiso12;
            cmdSave = (Button) findViewById(R.id.cmdSaveANC);
            cmdSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    DataSave();
                    //DataSaveANC();
                    //DisplayANCVisit();
                }
            });


            /*if(!C.Existence("Select healthid from ancService where HealthId='"+ g.getGeneratedId() +"'"))// and PregNo='"+ pregnancyNo +"'
            {
                Connection.MessageBox(ANC.this,"গর্ভবতী মহিলার সাধারন তথ্য না থাকাতে এ এন সি তথ্য নাই");
                return;
            }*/
            //String dtpLMP = GetMaxLMPDate();
            if (dtpLMP != null) {
                DisplayTempANCVisit(Global.DateConvertDMY(dtpLMP));
            } else {
                Connection.MessageBox(ANCFPI.this, "গর্ভবতী মহিলার সাধারন তথ্য না থাকাতে এ এন সি তথ্য নাই");
                return;
            }
            //secMiso.setVisibility(View.GONE);
            DisplayANCVisit();
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        } catch (Exception e) {
            Connection.MessageBox(ANCFPI.this, e.getMessage());
            return;
        }
    }


    private void DataSave() {
        try {
            String SQL = "";
            String AG = "";
            String ServiceId = serviceID(pregnancyNo);
            if (!rdoVDateyn1.isChecked() & !rdoVDateyn2.isChecked() & secVDate.isShown()) {
                Connection.MessageBox(ANCFPI.this, "পরিদর্শনের তারিখ যাচাই করুন ।");
                rdoVDateyn1.requestFocus();
                return;
            } else if (!rdoIron11.isChecked() & !rdoIron12.isChecked() & secIron.isShown()) {
                Connection.MessageBox(ANCFPI.this, "আয়রন ও ফলিক এসিড বড়ি পেয়েছেন কিনা  যাচাই করুন ।");
                rdoIron11.requestFocus();
                return;
            } else if (!rdoMiso11.isChecked() & !rdoMiso12.isChecked() & secMiso.isShown()) {
                Connection.MessageBox(ANCFPI.this, "মিসোপ্রোস্টোল বড়ি পেয়েছেন কিনা  যাচাই করুন ।");
                rdoMiso11.requestFocus();
                return;
            }


            if (!C.Existence("Select healthId from " + TableNameFPI + "  Where healthId='" + g.getGeneratedId() + "' AND pregNo ='" + pregnancyNo + "' AND serviceId ='" + ServiceId + "'")) {
                SQL = "Insert into " + TableNameFPI + "(HealthID,pregNo,serviceId,providerId,systemEntryDate,Upload)Values('" + g.getGeneratedId() + "','" + pregnancyNo + "','" + ServiceId + "','" + g.getProvCode() + "','" + Global.DateTimeNowYMDHMS() + "','2')";
                C.Save(SQL);
            }

            SQL = "Update " + TableNameFPI + " Set Upload='2',";
            SQL += "visitSource = '" + (rdoVDate1.isChecked() ? "1" : (rdoVDate2.isChecked() ? "2" : "")) + "',";
            if (rdoVDate1.isChecked() == true) {
                SQL += "visitDate = '" + (rdoVDateyn1.isChecked() ? "1" : (rdoVDateyn2.isChecked() ? "2" : "")) + "',";
            }
            if (rdoVDate2.isChecked() == true) {
                SQL += "visitMonth = '" + (rdoVDateyn1.isChecked() ? "1" : (rdoVDateyn2.isChecked() ? "2" : "")) + "',";
            }
            SQL += "ironFolStatus = '" + (rdoIron11.isChecked() ? "1" : (rdoIron12.isChecked() ? "2" : "")) + "',";
            SQL += "misoStatus = '" + (rdoMiso11.isChecked() ? "1" : (rdoMiso12.isChecked() ? "2" : "")) + "'";
            SQL += " Where healthId='" + g.getGeneratedId() + "' AND pregNo ='" + pregnancyNo + "' AND serviceId ='" + ServiceId + "'";
            C.Save(SQL);
            Connection.MessageBox(ANCFPI.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
            //finish();

        } catch (Exception e) {
            Connection.MessageBox(ANCFPI.this, e.getMessage());
            return;
        }
    }

    private void FPIDataSearch(String generatedId, String pregnancyNo, String ServiceId) {
        try {
            RadioButton rb;
            String SQL1 = "";
            SQL1 = "Select ifnull(healthId,'') as HealthID,ifnull(visitDate,'') as visitDate,ifnull(visitMonth,'') as visitMonth,ifnull(ironFolStatus,'') as ironFolStatus,ifnull(misoStatus,'') as misoStatus\n" +
                    "from ancServiceFPI FPI Where HealthID='" + generatedId + "' AND pregNo ='" + pregnancyNo + "' AND serviceId ='" + ServiceId + "'";
            /*SQL += " ifnull(NameBang,'') as NameBang, ifnull(Rth,'') as Rth, ifnull(HaveNID,'') as HaveNID, ifnull(NID,'') as NID, ifnull(NIDStatus,'') as NIDStatus, ifnull(HaveBR,'') as HaveBR, ifnull(BRID,'') as BRID, ifnull(BRIDStatus,'') as BRIDStatus, ifnull(MobileNo1,'') as MobileNo1,";
            SQL += " ifnull(MobileNo2,'') as MobileNo2, ifnull(MobileYN,'')as MobileYN, ifnull(DOB,'') as DOB, ifnull(cast(((julianday(date('now'))-julianday(DOB))/365)as int),'') as Age, ifnull(DOBSource,'') as DOBSource, ifnull(BPlace,'') as BPlace, ifnull(FNo,'') as FNo, ifnull(Father,'') as Father, ifnull(FDontKnow,'')as FDontKnow, ifnull(MNo,'') as MNo, ifnull(Mother,'') as Mother,ifnull(MDontKnow,'')as MDontKnow,";
            SQL += " ifnull(Sex,'') as Sex, ifnull(MS,'') as MS, ifnull(SPNO1,'') as SPNO1,ifnull(SPNO2,'') as SPNO2,ifnull(SPNO3,'') as SPNO3,ifnull(SPNO4,'') as SPNO4, ifnull(ELCONo,'') as ELCONo, ifnull(ELCODontKnow,'') as ELCODontKnow, ifnull(EDU,'') as EDU, ifnull(Rel,'') as Rel, ifnull(Nationality,'') as Nationality, ifnull(OCP,'') as OCP";*/
            //SQL1 += " from MemberFPI Where Dist='"+ Dist +"' and Upz='"+ Upz +"' and UN='"+ UN +"' and Mouza='"+ Mouza +"' and Vill='"+ Vill +"' and HHNo='"+ HHNo +"' and SNo='"+ SNo +"'";
            Cursor cur1 = C.ReadData(SQL1);
            cur1.moveToFirst();
            while (!cur1.isAfterLast()) {

                /*for (int i = 0; i < rdogrpNameEng.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpNameEng.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur1.getString(cur1.getColumnIndex("NameEngStatus"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }*/
                //-----------FPI Retrived-------------------

                if (cur1.getString(cur1.getColumnIndex("visitDate")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoVDateyn1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("visitDate")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoVDateyn2.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("visitMonth")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoVDateyn1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("visitMonth")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoVDateyn2.setChecked(true);
                }


                if (cur1.getString(cur1.getColumnIndex("ironFolStatus")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoIron11.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("ironFolStatus")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoIron12.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("misoStatus")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoMiso11.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("misoStatus")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoMiso12.setChecked(true);
                }


                cur1.moveToNext();
            }
            cur1.close();
        } catch (Exception e) {
            Connection.MessageBox(ANCFPI.this, e.getMessage());
            return;
        }
    }

    /*  private void DataSaveANC() {
          try {

              if(!rdoVDate1.isChecked() & !rdoVDate2.isChecked() & secVDate.isShown())
              {
                  Connection.MessageBox(ANCFPI.this, "পরিদর্শনের উৎস কি সিলেক্ট করুন ।");
                  rdoVDate1.requestFocus();
                  return;
              }

              if(dtpVDate.getText().toString().length()==0 & dtpVDate.isShown() )
              {
                  Connection.MessageBox(ANCFPI.this, "প্রকৃত পরিদর্শনের তারিখ কত লিখুন ।");
                  dtpVDate.requestFocus();
                  return;
              }
              if(txtVMonth.getText().toString().length()==0 & txtVMonth.isShown())
              {
                  Connection.MessageBox(ANCFPI.this, "আনুমানিক পরিদর্শনের মাস কত লিখুন ।");
                  txtVMonth.requestFocus();
                  return;
              }

              String dtpLMP = GetMaxLMPDate();
              Integer DiffLMP_VD = Global.DateDifferenceDays(dtpVDate.getText().toString(), Global.DateConvertDMY(dtpLMP));
              SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
              String formattedDate = sdf.format(c.getTime());
              String PrevAncVisit = "'" + GetMaxANCDate() == "0" ? "" : GetMaxANCDate() + "";// '"+  GetMaxANCDate()==0?"":GetMaxANCDate()) +"'";
              Date date1 = sdf.parse(formattedDate);
              if(dtpVDate.getText().toString().length()!=0)
              {
                  Date date4 = sdf.parse(dtpVDate.getText().toString());
                  Date date5 = sdf.parse(Global.DateConvertDMY(dtpLMP));
                  if (date4.after(date1)) {
                      Connection.MessageBox(ANCFPI.this, "ভিজিটের  তারিখ আজকের তারিখ অপেক্ষা বেশি হবে না");
                      dtpVDate.requestFocus();
                      return;
                  } else if (date4.before(date5)) {
                      Connection.MessageBox(ANCFPI.this, "ভিজিটের  তারিখ শেষ মাসিকের তারিখ এবং সিস্টেমের তারিখের মধ্যে হবে");
                      dtpVDate.requestFocus();
                      return;
                  }
              }

              String DOB = GetDOB(g.getGeneratedId());
              //String dtpLMP = GetMaxLMPDate();
              //String PrevAncVisit = "'" + GetMaxANCDate() == "0" ? "" : GetMaxANCDate() + "";
              Integer DobAge = Global.DateDifferenceYears(Global.DateConvertDMY(dtpLMP), Global.DateConvertDMY(DOB.toString()));
              *//*if (dtpVDate.getText().toString().length() == 0) {
                Connection.MessageBox(ANC.this, "পরিদর্শনের তারিখ কত লিখুন।");
                dtpVDate.requestFocus();
                return;
            }*//*


            if (!rdoIron1.isChecked() & !rdoIron2.isChecked() & secIron.isShown()) {
                Connection.MessageBox(ANCFPI.this, "আয়রন ও ফলিক এসিড পেয়েছেন কিনা  সিলেক্ট করুন।");
                rdoIron1.requestFocus();
                return;
            } else if (DiffLMP_VD >= 224) {
                if (!rdoMiso1.isChecked() & !rdoMiso2.isChecked() & secMiso.isShown()) {
                    Connection.MessageBox(ANCFPI.this, "মিসোপ্রস্টল বড়ি পেয়েছেন কিনা  সিলেক্ট করুন।");
                    rdoMiso1.requestFocus();
                    return;
                }
            }  else if (DiffLMP_VD >= 300) {
                Connection.MessageBox(ANCFPI.this, "ভিজিটের  তারিখ শেষ মাসিকের তারিখ থেকে ৩০০ দিনের বেশি হতে পারে না।");
                dtpVDate.requestFocus();
                return;
            }


            //Ewmt
            String SQL = "";
            String PGNNoNull = PGNNoNull();

            String ServiceId = serviceID(pregnancyNo);
            //VlblServiceId.setText(ServiceId);
            String SQ = "select visitDate FROM ancService ";
            SQ += " WHERE healthId = '"+ g.getGeneratedId() +"' AND pregNo ='"+ pregnancyNo +"' and serviceId='"+ VlblServiceId.getText() +"'";//visitDate='"+ Global.DateConvertYMD(dtpVDate.getText().toString()) +"'

            if(!C.Existence(SQ))
            {
                SQL = "Insert into " + TableANC + "(healthId,pregNo,serviceId,providerId,visitDate,ironFolStatus," +
                        "misoStatus,systemEntryDate,upload) VALUES ('" + g.getGeneratedId() + "','" + pregnancyNo + "','" + ServiceId + "','" + g.getProvCode() + "'" +
                        ",'" + Global.DateConvertYMD(dtpVDate.getText().toString()) + "','" + ((rdoIron1.isChecked() ? "1" : "2")) + "','" + ((rdoMiso1.isChecked() ? "1" : "2")) + "','" + Global.DateTimeNowYMDHMS() + "','2')";
                C.Save(SQL);
                sqlupdate1 = "Update ancService Set ";
                sqlupdate1+="visitSource = '"+ (rdoVDate1.isChecked()?"1":"2") +"',";
                if(rdoVDate1.isChecked())
                {
                    sqlupdate1+="visitDate = '"+ Global.DateConvertYMD(dtpVDate.getText().toString()) +"',";
                    sqlupdate1+="visitMonth = '"+ Global.DateDifferenceMonth(Global.DateNowDMY(), dtpVDate.getText().toString()) +"'";//DateDifferenceYears DateDifferenceMonth
                }
                else
                {
                    sqlupdate1+="visitDate = '"+ Global.addDaysYMD(Global.DateNowDMY(), -Integer.valueOf(txtVMonth.getText().toString())*30) +"',";
                    //sqlupdate+="MRDate = '"+ Global.addDaysYMD(Global.DateConvertDMY(dtpDOV.getText().toString()), -Integer.valueOf(txtMDAge.getText().toString())*30) +"',";
                    sqlupdate1+="visitMonth = '"+ txtVMonth.getText().toString() +"'";
                }
                sqlupdate1+=" Where HealthID='"+ g.getGeneratedId() +"' and pregNo='"+ pregnancyNo +"' and serviceId='"+ ServiceId +"'";
                C.Save(sqlupdate1);
                DisplayANCVisit();
                txtVMonth.setText("");
                rdogrpVDate.clearCheck();
                VlblServiceId.setText("");
                dtpVDate.setText("");
                rdogrpIron.clearCheck();
                rdogrpMiso.clearCheck();
                cmdSave.setText("Save");
                Connection.MessageBox(ANCFPI.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
            }
            else
            {
                //Connection.MessageBox(ANC.this, "এ এন সি  এর ভিসিট দেয়া আছে");
                //return;
                SQL = "Update "+ TableANC +" Set ";
                SQL+="providerId = '"+ g.getProvCode() +"',";
                SQL+="visitSource = '"+ (rdoVDate1.isChecked()?"1":"2") +"',";
                if(rdoVDate1.isChecked())
                {
                    SQL+="visitDate = '"+ Global.DateConvertYMD(dtpVDate.getText().toString()) +"',";
                    SQL+="visitMonth = '"+ Global.DateDifferenceMonth(Global.DateNowDMY(), dtpVDate.getText().toString()) +"',";//DateDifferenceYears DateDifferenceMonth
                }
                else
                {
                    SQL+="visitDate = '"+ Global.addDaysYMD(Global.DateNowDMY(), -Integer.valueOf(txtVMonth.getText().toString())*30) +"',";
                    //sqlupdate+="MRDate = '"+ Global.addDaysYMD(Global.DateConvertDMY(dtpDOV.getText().toString()), -Integer.valueOf(txtMDAge.getText().toString())*30) +"',";
                    SQL+="visitMonth = '"+ txtVMonth.getText().toString() +"',";
                }
                //SQL+="visitDate = '"+ Global.DateConvertYMD(dtpVDate.getText().toString()) +"',";
                SQL+="ironFolStatus = '"+ ((rdoIron1.isChecked() ? "1" : "2")) +"',";
                SQL+="misoStatus = '"+ ((rdoMiso1.isChecked() ? "1" : "2")) +"',";
                SQL+="modifyDate = '"+ Global.DateTimeNowYMDHMS() +"',";
                SQL+="upload = '2'";
                SQL+=" Where HealthID='"+ g.getGeneratedId() +"' and pregNo='"+ pregnancyNo +"' and serviceId='"+ VlblServiceId.getText() +"'";
                C.Save(SQL);
                DisplayANCVisit();
                txtVMonth.setText("");
                rdogrpVDate.clearCheck();
                VlblServiceId.setText("");
                dtpVDate.setText("");
                rdogrpIron.clearCheck();
                rdogrpMiso.clearCheck();
                cmdSave.setText("Save");
                Connection.MessageBox(ANCFPI.this, "তথ্য সফলভাবে সংশোধন হয়েছে ।");
            }
        } catch (Exception e) {
            Connection.MessageBox(ANCFPI.this, e.getMessage());
            return;
        }
    }*/
    private String GetMaxANCDate() {
        String SQL = "";
        String MaxANCDate = "";
        SQL = "Select ifnull(Max(visitDate),0) from ancService WHERE healthId ='" + g.getGeneratedId() + "' and pregNo=(select Max(pregNo) from ancService WHERE healthId='" + g.getGeneratedId() + "')";
        MaxANCDate = C.ReturnSingleValue(SQL);
        return MaxANCDate;
    }

    private String GetMaxLMPDate() {
        String SQL = "";
        String MaxANCDate = "";
        SQL = "Select ifnull(Max(LMP),0) from PregWomen WHERE healthId ='" + g.getGeneratedId() + "' and pregNo=(select Max(pregNo) from PregWomen WHERE healthId='" + g.getGeneratedId() + "')";
        MaxANCDate = C.ReturnSingleValue(SQL);
        return MaxANCDate;
    }

    private String GetDOB(String HealthId) {
        String sq = String.format("Select DOB from member WHERE healthId = '%s'", g.getHealthID());
        return C.ReturnSingleValue(sq);
    }

    private String GetMaxOutComeDate() {
        String SQL = "";
        String MaxOutcomeDate = "";
        SQL = "Select ifnull(Max(outcomedate),0) from delivery WHERE healthId ='" + g.getGeneratedId() + "' and pregNo=(select Max(pregNo) from ancService WHERE healthId='" + g.getGeneratedId() + "')";
        MaxOutcomeDate = C.ReturnSingleValue(SQL);
        return MaxOutcomeDate;
    }

    private String PGNNoNull() {
        String SQL = "";
        String PGNNo = "";

        SQL = "select '0'||(ifnull(max(cast(PregNo as int)),1))MaxPGNNo from PregWomen WHERE healthId = " + g.getGeneratedId();
            /*SQL += " where";
            SQL += " dist='" + g.getDistrict() + "' and upz='" + g.getUpazila() + "' and un='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and vill='" + g.getVillage() + "'  and ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"' and  HHNo='" + g.getHouseholdNo() + "' and SNo='" + txtSNo.getText() + "'";*/
        PGNNo = Global.Right(("00" + C.ReturnSingleValue(SQL)), 2);

        return PGNNo;
    }

    private String serviceID(String pregNo) {
        String SQL = "";
        String PGNNo = "";

        //String MaxPGNNo=PregMaxPGNNo();
        SQL = "select '0'||(ifnull(max(cast(serviceId as int)),0))MaxserviceId from ancService WHERE healthId = " + g.getGeneratedId() + " AND pregNo = " + pregNo;

        String tempserviceID = C.ReturnSingleValue(SQL);


        String serviceID = String.valueOf((Long.parseLong(tempserviceID)));
        return String.valueOf(serviceID);

        //if (serviceID.equalsIgnoreCase("1")) {
            /*return String.valueOf(g.getGeneratedId() + pregNo + serviceID);*/
        // return String.valueOf(serviceID);
        // }
        // else {

        // }
    }

    private String GetCurrentPregnancyNumber() {

        String SQL = "select ifnull((select '0'||cast(max(pregNo) as string)),0) AS PregNo from PregWomen";
        SQL += " Where HealthId='" + g.getGeneratedId() + "'";
        String Val = String.valueOf(C.ReturnSingleValue(SQL));
        if (Val.equalsIgnoreCase("")) {
            return "0";
        } else
            return Val;
    }

    private String PregMaxPGNNo() {


        String SQL = "";
        String PGNNo = "";

        SQL = "select ifnull((select '0'||cast(max(pregNo) as string)),0) AS PregNo from PregWomen WHERE healthId=" + g.getGeneratedId();
        /*SQL += " where";
        SQL += " dist='" + g.getDistrict() + "' and upz='" + g.getUpazila() + "' and un='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and vill='" + g.getVillage() + "'  and ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"' and  HHNo='" + g.getHouseholdNo() + "' and SNo='" + txtSNo.getText() + "'";*/
        // PGNNo = Global.Right(("00"+ C.ReturnSingleValue(SQL)),2);
        PGNNo = C.ReturnSingleValue(SQL);
        return PGNNo;
    }

    private String PGNNo() {
        String SQL = "";
        String PGNNo = "";

        SQL = "Select (ifnull(max(cast(PregNo as int)),0)+1)MaxPGNNo from PregWomen WHERE healthId = " + g.getGeneratedId();
        /*SQL += " where";
        SQL += " dist='" + g.getDistrict() + "' and upz='" + g.getUpazila() + "' and un='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and vill='" + g.getVillage() + "'  and ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"' and  HHNo='" + g.getHouseholdNo() + "' and SNo='" + txtSNo.getText() + "'";*/
        PGNNo = Global.Right(("00" + C.ReturnSingleValue(SQL)), 2);
        return PGNNo;
    }


    private String PGNNo1() {
        String SQL = "";
        String PGNNo = "";

        SQL = "Select (ifnull(max(cast(pregNo as int)),0))pregNo from delivery WHERE healthId = " + g.getGeneratedId();
        /*SQL += " where";
        SQL += " dist='" + g.getDistrict() + "' and upz='" + g.getUpazila() + "' and un='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and vill='" + g.getVillage() + "'  and ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"' and  HHNo='" + g.getHouseholdNo() + "' and SNo='" + txtSNo.getText() + "'";*/
        PGNNo = Global.Right(("00" + C.ReturnSingleValue(SQL)), 2);
        return PGNNo;
    }

    public class ActualANC extends BaseAdapter {
        private Context mContext;
        String[][] vcode;
        Integer totalRec;

        public ActualANC(Context c) {
            mContext = c;
        }

        public int getCount() {

            //String pgnpositionselected = String.valueOf(spnPgn.getSelectedItemPosition());
            // String ServiceId = serviceID(pgnpositionselected);

            //return Integer.parseInt(C.ReturnSingleValue("Select count(*)total from ancService where healthid='" + g.getGeneratedId() + "' AND pregNo = '" + PregMaxPGNNo() + "'"));
            return Integer.parseInt(C.ReturnSingleValue("Select count(*)total from ancService where healthid='" + g.getGeneratedId() + "' AND pregNo = '" + pregnancyNo + "'"));
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            View MyView = convertView;
            if (convertView == null) {
                LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                MyView = li.inflate(R.layout.anc_item_actual, null);
                //String pgnpositionselected = String.valueOf(spnPgn.getSelectedItemPosition());
                //String ServiceId = g.getGeneratedId() + pgnpositionselected;

                //String SQL = "Select serviceId, visitDate,ironFolStatus,ironFolQty,ironFolUnit,misoStatus,misoQty,misoUnit,serviceSource from ancService where healthid='" + g.getGeneratedId() + "' AND pregNo = '" + PregMaxPGNNo() + "'" + " order by cast(visitDate as DATE) asc";
                String SQL = "Select serviceId, visitDate,ironFolStatus,ironFolQty,ironFolUnit,misoStatus,misoQty,misoUnit,serviceSource from ancService where healthid='" + g.getGeneratedId() + "' AND pregNo = '" + pregnancyNo + "'" + " order by cast(visitDate as DATE) asc";

                try {
                    Cursor cur = C.ReadData(SQL);
                    cur.moveToFirst();

                    totalRec = cur.getCount();
                    vcode = new String[4][totalRec];
                    int i = 0;
                    while (!cur.isAfterLast()) {
                        vcode[0][i] = "পরিদর্শন " + String.valueOf(i + 1) + " " + Global.DateConvertDMY(String.valueOf(cur.getString(cur.getColumnIndex("visitDate"))));
                        vcode[1][i] = String.valueOf(cur.getString(cur.getColumnIndex("serviceId")));
                        /*vcode[1][i]= String.valueOf(cur.getString(cur.getColumnIndex("serviceId")));
                        vcode[2][i]= cur.getString(cur.getColumnIndex("imucard"));
                        vcode[3][i]= String.valueOf(cur.getString(cur.getColumnIndex("imucode")));*/

                        i += 1;
                        cur.moveToNext();
                    }
                    cur.close();

                    Button tv = (Button) MyView.findViewById(R.id.image_name);
                    tv.setTextSize(14);
                    tv.setText(vcode[0][position]);// + "\n" + vcode[1][position]);
                    final Integer p = position;
                    tv.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            String ServiceId = String.valueOf(vcode[1][position]);
                            FPIDataSearch(g.getGeneratedId(), pregnancyNo, ServiceId);
                            //Cursor cur = C.ReadData("Select  visit as visit,vdate as vdate,visitstatus as visitstatus,currstatus as currstatus,ssource as ssource,qty as qty,unit as unit,brand as brand,validity as validity,daymonyear as dmy, ifnull(referPlace,'') as referplace, MRDate,syrinsQty, ifnull(pregNo,'') AS PGNNo from ElcoVisit where healthid='"+ g.getGeneratedId() +"' and visit='"+ vcode[3][position] +"'");
                            Cursor cur = C.ReadData("Select serviceId,visitSource,visitDate,visitMonth,ironFolStatus,ironFolQty,ironFolUnit,misoStatus,misoQty,misoUnit,serviceSource from ancService where healthid='" + g.getGeneratedId() + "' AND pregNo = '" + pregnancyNo + "'  and serviceId='" + ServiceId + "'");//vcode[1][position]
                            cur.moveToFirst();

                            while (!cur.isAfterLast()) {
                                //rdogrpVS.clearCheck();
                                //spnMethod.setSelection(0);
                                //rdogrpSS.clearCheck();
                                VlblServiceId.setText(ServiceId);
                                if (cur.getString(cur.getColumnIndex("visitSource")).equals("1") | cur.getString(cur.getColumnIndex("visitSource")).equals("null") | cur.getString(cur.getColumnIndex("visitSource")).equals(null)) {
                                    rdoVDate1.setChecked(true);
                                } else if (cur.getString(cur.getColumnIndex("visitSource")).equals("2")) {
                                    rdoVDate2.setChecked(true);
                                }
                                String visitDate = cur.getString(cur.getColumnIndex("visitDate"));
                                if (visitDate != null) {
                                    dtpVDate.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("visitDate"))));
                                }
                                txtVMonth.setText(cur.getString(cur.getColumnIndex("visitMonth")));
                                //dtpVDate.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("visitDate"))));//cur.getString(cur.getColumnIndex("visitDate")));
                                if (cur.getString(cur.getColumnIndex("ironFolStatus")).equals("1")) {
                                    rdoIron1.setChecked(true);
                                } else if (cur.getString(cur.getColumnIndex("ironFolStatus")).equals("2")) {
                                    rdoIron2.setChecked(true);
                                }
                                if (!cur.getString(cur.getColumnIndex("misoStatus")).equals("")) {
                                    if (cur.getString(cur.getColumnIndex("misoStatus")).equals("1")) {
                                        rdoMiso1.setChecked(true);
                                    } else if (cur.getString(cur.getColumnIndex("misoStatus")).equals("2")) {
                                        rdoMiso2.setChecked(true);
                                    }
                                }
                                //cmdSave.setText("Edit");
                                //spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select EvCode||'-'||EVName as EvCode from ElcoEvent where evcode  order by EvCode asc"));
                                //spnMethod.setSelection(Global.SpinnerItemPosition(spnMethod, 2, cur.getString(cur.getColumnIndex("currstatus"))));

                                cur.moveToNext();
                            }
                            cur.close();
                            //DisplaySelectedANCInfo(ServiceId);


                        }
                    });
                } catch (Exception ex) {
                    Connection.MessageBox(ANCFPI.this, ex.getMessage());
                }

            }
            return MyView;
        }

    }

    private void DisplaySelectedANCInfo(String ServiceId) {

        String SQL = "Select visitDate,ironFolStatus,ironFolQty,ironFolUnit,misoStatus,misoQty,misoUnit,serviceSource from ancService where serviceId = '" + ServiceId + "'";

        final Dialog popupView = new Dialog(ANCFPI.this);
        popupView.setTitle("Events");
        popupView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popupView.setContentView(R.layout.ancinfo);
        popupView.setCancelable(true);
        popupView.setCanceledOnTouchOutside(true);

        try {
            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();

            while (!cur.isAfterLast()) {
                ((TextView) popupView.findViewById(R.id.dtpVDate)).setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("visitDate"))));
                ImageButton btnVDate = (ImageButton) popupView.findViewById(R.id.btnVDate);
                btnVDate.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        VariableID = "btnEditVDate";
                        showDialog(DATE_DIALOG);

                        //dtpEditVDate = (EditText) findViewById(R.id.dtpEditVDate);
                    }
                    /*public void onClick(View v) {
                        VariableID = "btnVDate";
                        showDialog(DATE_DIALOG);
                    }*/
                });
                //((TextView) popupView.findViewById(R.id.dtpEditVDate)).setText(dtpEditVDate.getText());
                if (cur.getString(cur.getColumnIndex("ironFolStatus")).equalsIgnoreCase("1")) {
                    //((TextView) popupView.findViewById(R.id.VlblIronYN)).setText("হ্যাঁ");
                    ((RadioButton) popupView.findViewById(R.id.rdoIron1)).setChecked(true);
                } else if (cur.getString(cur.getColumnIndex("ironFolStatus")).equalsIgnoreCase("2")) {
                    //((TextView) popupView.findViewById(R.id.VlblIronYN)).setText("না");
                    ((RadioButton) popupView.findViewById(R.id.rdoIron2)).setChecked(true);
                }


                if (cur.getString(cur.getColumnIndex("misoStatus")).equalsIgnoreCase("1")) {
                    //((TextView) popupView.findViewById(R.id.VlblMisoYN)).setText("হ্যাঁ");
                    ((RadioButton) popupView.findViewById(R.id.rdoMiso1)).setChecked(true);
                } else if (cur.getString(cur.getColumnIndex("misoStatus")).equalsIgnoreCase("2")) {
                    //((TextView) popupView.findViewById(R.id.VlblMisoYN)).setText("না");
                    ((RadioButton) popupView.findViewById(R.id.rdoMiso2)).setChecked(true);
                }


                cur.moveToNext();
            }
            cur.close();


            //  popupView.showAtLocation(popupView, Gravity.CENTER, 0, 0);
            Button btnDismiss = (Button) popupView.findViewById(R.id.dismiss);
            btnDismiss.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    popupView.dismiss();
                }
            });

            popupView.show();
        } catch (Exception ex) {
            Connection.MessageBox(ANCFPI.this, ex.getMessage());
        }

    }

    private void DisplayANCVisit() {
        GridView gcount = (GridView) findViewById(R.id.gridANC);
        g.setImuCode(String.valueOf(gcount.getCount() + 1));
        ANCVisits();
    }

    public void ANCVisits() {
        GridView g1 = (GridView) findViewById(R.id.gridANC);
        g1.setAdapter(new ActualANC(this));
        g1.setNumColumns(6);
    }


    private void DisplayTempANCVisit(String ExpLMPDate) {
        GridView gcount = (GridView) findViewById(R.id.gridANCExp);
        g.setImuCode(String.valueOf(gcount.getCount() + 1));
        ExpectedANC(ExpLMPDate);
    }

    public void ExpectedANC(String ExpANCDate) {
        GridView g1 = (GridView) findViewById(R.id.gridANCExp);
        g1.setAdapter(new ExpectedANC(this, ExpANCDate));
        g1.setNumColumns(6);
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
            EditText dtpDate;

            dtpDate = (EditText) findViewById(R.id.dtpLMP);

            if (VariableID.equals("btnVDate")) {
                dtpDate = (EditText) findViewById(R.id.dtpVDate);
            }

            if (VariableID.equals("btnEditVDate")) {
                dtpDate = (EditText) findViewById(R.id.dtpEditVDate);
            }


            dtpDate.setText(new StringBuilder()
                    .append(Global.Right("00" + mDay, 2)).append("/")
                    .append(Global.Right("00" + mMonth, 2)).append("/")
                    .append(mYear));

            //dtpEDD.setText(Global.addDays(dtpLMP.getText().toString(), 280));
            String dtpLMP = GetMaxLMPDate();
            DisplayTempANCVisit(Global.DateConvertDMY(dtpLMP));
            Integer DiffLMP_VD = Global.DateDifferenceDays(dtpVDate.getText().toString(), Global.DateConvertDMY(dtpLMP));
            Integer DiffDNow_VD = Global.DateDifferenceDays(Global.DateNowDMY(), Global.DateConvertDMY(dtpLMP));
            Integer DiffDNow_OC = Global.DateDifferenceDays(Global.DateConvertDMY(dtpLMP), GetMaxOutComeDate());


            if (DiffLMP_VD >= 224) {
                if (g.getProvType().equalsIgnoreCase("2")) {
                    secMiso.setVisibility(View.GONE);
                } else {
                    secMiso.setVisibility(View.VISIBLE);
                }
            } else {
                secMiso.setVisibility(View.GONE);
            }

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = sdf.format(c.getTime());
                String PrevAncVisit = "'" + GetMaxANCDate() == "0" ? "" : GetMaxANCDate() + "";// '"+  GetMaxANCDate()==0?"":GetMaxANCDate()) +"'";
                //Date MaxAncVisitDate = sdf.parse(Global.DateConvertDMY(PrevAncVisit));
                Date date1 = sdf.parse(formattedDate);
                Date date4 = sdf.parse(dtpVDate.getText().toString());
                Date date5 = sdf.parse(Global.DateConvertDMY(dtpLMP));
                Date date2 = sdf.parse(dtpVDate.getText().toString());

                String DOB = GetDOB(g.getHealthID());
                Integer DobAge = Global.DateDifferenceYears(Global.DateConvertDMY(dtpLMP), Global.DateConvertDMY(DOB.toString()));
                if (date4.after(date1)) {
                    if (VariableID.equals("btnVDate")) {
                        Connection.MessageBox(ANCFPI.this, "ভিজিটের  তারিখ আজকের তারিখ অপেক্ষা বেশি হবে না");
                    }
                    //dtpVDate.setText(null);
                    dtpVDate.requestFocus();
                } else if (date4.before(date5)) {
                    if (VariableID.equals("btnVDate")) {
                        Connection.MessageBox(ANCFPI.this, "ভিজিটের  তারিখ শেষ মাসিকের তারিখ এবং সিস্টেমের তারিখের মধ্যে হবে");
                    }
                    //dtpVDate.setText(null);
                    dtpVDate.requestFocus();
                } else if (DiffLMP_VD >= 300) {
                    if (VariableID.equals("btnVDate")) {
                        Connection.MessageBox(ANCFPI.this, "ভিজিটের  তারিখ শেষ মাসিকের তারিখ থেকে ৩০০ দিনের বেশি হতে পারে না।");
                    }
                    //dtpVDate.setText(null);
                    dtpVDate.requestFocus();
                }
                /*if (PrevAncVisit.equalsIgnoreCase("0")) {

                } else {
                    Date MaxAncVisitDate = sdf.parse(Global.DateConvertDMY(PrevAncVisit));
                    if (date4.before(MaxAncVisitDate)) {
                        if (VariableID.equals("btnVDate")) {
                            Connection.MessageBox(ANC.this, "পূর্বের ভিজিটের তারিখ অপেক্ষা বর্তমান ভিজিটের তারিখ বেশি হতে হবে।");
                        }
                        //dtpVDate.setText(null);
                        dtpVDate.requestFocus();
                    }
                }*/

                if (date5.after(date1)) {
                    if (VariableID.equals("btnLMP")) {
                        Connection.MessageBox(ANCFPI.this, "শেষ মাসিকের তারিখ আজকের তারিখ অপেক্ষা বেশি হবে না");
                    }
                    //dtpLMP.requestFocus(); comments by faz

                    //return;
                } else if (date5.equals(date1)) {
                    if (VariableID.equals("btnLMP")) {
                        Connection.MessageBox(ANCFPI.this, "শেষ মাসিকের তারিখ আজকের তারিখ সমান হবে না");
                    }
                    //dtpLMP.requestFocus(); comments by faz
                } else if (DiffDNow_VD <= 29) {
                    if (VariableID.equals("btnLMP")) {
                        Connection.MessageBox(ANCFPI.this, "শেষ মাসিকের তারিখ ৩০ দিনের কম হবে না");
                    }
                    //dtpLMP.setText(null);
                    //dtpLMP.requestFocus(); comments by faz
                } else if (DobAge < 12) {
                    if (VariableID.equals("btnLMP")) {
                        Connection.MessageBox(ANCFPI.this, "মহিলার বয়স ১২ বছরের বেশি হতে হবে ");
                    }

                    dtpVDate.requestFocus();
                }


              /* if (DiffDNow_OC<730)
               {
                   chkDengersine4.setChecked(true);
               }
                else
               {

               }*/

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

    public class ExpectedANC extends BaseAdapter {
        private Context mContext;
        String[][] vcode;
        Integer totalRec;
        String ExpDate;

        public ExpectedANC(Context c, String ExpAncDate) {
            mContext = c;
            ExpDate = ExpAncDate;
        }

        public int getCount() {


            return Integer.parseInt("4");//C.ReturnSingleValue("Select count(*)total from Immunization where healthid='"+ g.getHealthID() +"'"));
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View MyView = convertView;
            if (convertView == null) {
                LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                MyView = li.inflate(R.layout.anc_item, null);

                try {

                    totalRec = 4;//cur.getCount();
                    vcode = new String[4][totalRec];
                    int i = 0;
                    String dtpLMP = GetMaxLMPDate();
                    //vcode[0][0] = "পরিদর্শন ১ " + Global.addDays(Global.DateConvertDMY(dtpLMP), 106) +"\n হতে \n"+ Global.addDays(Global.DateConvertDMY(dtpLMP), 112);
                    vcode[0][0] = "পরিদর্শন ১ " + Global.addDays(Global.DateConvertDMY(dtpLMP), 112) + "\n তারিখের মধ্যে       ";
                    vcode[0][1] = "পরিদর্শন ২ " + Global.addDays(Global.DateConvertDMY(dtpLMP), 162) + "\n হতে \n" + Global.addDays(Global.DateConvertDMY(dtpLMP), 196);
                    vcode[0][2] = "পরিদর্শন ৩ " + Global.addDays(Global.DateConvertDMY(dtpLMP), 218) + "\n হতে \n" + Global.addDays(Global.DateConvertDMY(dtpLMP), 224);
                    vcode[0][3] = "পরিদর্শন ৪ " + Global.addDays(Global.DateConvertDMY(dtpLMP), 246) + "\n হতে \n" + Global.addDays(Global.DateConvertDMY(dtpLMP), 252);

                    i += 1;

                    Button tv = (Button) MyView.findViewById(R.id.image_name);
                    tv.setTextSize(14);
                    tv.setText(vcode[0][position] + "\n");
                    final Integer p = position;

                } catch (Exception ex) {
                    Connection.MessageBox(ANCFPI.this, ex.getMessage());
                }

            }
            return MyView;
        }
    }

}

