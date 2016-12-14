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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import Common.Connection;
import Common.Global;

/**
 * Created by Nisan on 5/2/2016.
 */
public class PNCMotherChildFPI extends Activity {
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
        AlertDialog.Builder adb = new AlertDialog.Builder(PNCMotherChildFPI.this);
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
    Calendar c = Calendar.getInstance();
    SimpleAdapter dataAdapter;
    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();

    private String TableName;
    private String TableNameMFPI;
    private String TableNameCFPI;
    //private String TableNamePNC;
    private String ChildTableName;
    private String MemberTable;

    String StartTime;
    String pregnancyNo;
    String TableNamePNC_Mother;
    String TableNamePNC_Child;


    //EditText dtpDOPNCMo1;
    //ImageButton btnDOPNCMo1;
    Button cmdSavePNCMo;
    LinearLayout secPNCMother1;
    LinearLayout secPNCMother;
    TextView VlblPNCMother;
    RadioGroup rdogrpPNCMother;
    RadioButton rdoPNCMother1;
    RadioButton rdoPNCMother2;
    EditText dtpDOPNCMo1;
    ImageButton btnDOPNCMo1;
    EditText txtMotherPNCMonth;
    TextView VlblMotherPNCMonth;

    LinearLayout secPNCChild1;
    LinearLayout secPNCChild;
    TextView VlblPNCChild;
    RadioGroup rdogrpPNCChild;
    RadioButton rdoPNCChild1;
    RadioButton rdoPNCChild2;
    EditText dtpDOPNCh1;
    ImageButton btnDOPNCCh1;
    EditText txtChildPNCMonth;
    TextView VlblChildPNCMonth;


    //..............FPI.............
    RadioGroup rdogrpMotherPNC;
    RadioButton rdoMotherPNC1;
    RadioButton rdoMotherPNC2;

    RadioGroup rdogrpPNCChild1;
    RadioButton rdoPNCChild11;
    RadioButton rdoPNCChild12;


    Button cmdSaveCh1;
    Button cmdChild1;
    Button cmdChild2;
    Button cmdChild3;
    Button cmdChild4;
    TextView VlblServiceIdMother;
    TextView VlblServiceIdChild;
    TextView ChildNo;
    String sqlupdate1 = "";
    String sqlupdate2 = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pncmotherchildfpi);
        C = new Connection(this);
        g = Global.getInstance();
        StartTime = g.CurrentTime24();
        TableNameMFPI = "pncServiceMotherFPI";
        TableNameCFPI = "pncServiceChildFPI";
        TableNamePNC_Mother = "pncServiceMother";
        TableNamePNC_Child = "pncServiceChild";

        ELCOProfile e = new ELCOProfile();
        e.ELCOProfile(this, g.getGeneratedId());

        //call from elco
        if (g.getCallFrom().equals("elco")) {
            pregnancyNo = e.CurrentPregNumber(this, g.getGeneratedId());
        }
        //call from register
        else if (g.getCallFrom().equals("regis")) {
            pregnancyNo = e.LastPregNumber(this, g.getGeneratedId());
        } else if (g.getCallFrom().equals("1")) {
            pregnancyNo = e.LastPregNumber(this, g.getGeneratedId());
        }

        VlblServiceIdMother = (TextView) findViewById(R.id.VlblServiceIdMother);
        VlblServiceIdMother.setVisibility(View.GONE);

        VlblServiceIdChild = (TextView) findViewById(R.id.VlblServiceIdChild);
        VlblServiceIdChild.setVisibility(View.GONE);
        ChildNo = (TextView) findViewById(R.id.ChildNo);
        ChildNo.setVisibility(View.GONE);
        ChildNo.setText("1");
        cmdChild1 = (Button) findViewById(R.id.cmdChild1);
        cmdChild1.setBackgroundColor(Color.GREEN);
        secPNCMother1 = (LinearLayout) findViewById(R.id.secPNCMother1);
        secPNCMother = (LinearLayout) findViewById(R.id.secPNCMother);
        VlblPNCMother = (TextView) findViewById(R.id.VlblPNCMother);
        rdogrpPNCMother = (RadioGroup) findViewById(R.id.rdogrpPNCMother);
        rdoPNCMother1 = (RadioButton) findViewById(R.id.rdoPNCMother1);
        rdoPNCMother2 = (RadioButton) findViewById(R.id.rdoPNCMother2);
        dtpDOPNCMo1 = (EditText) findViewById(R.id.dtpDOPNCMo1);
        //dtpDOPNCMo1.setText(Global.DateNowDMY());
        btnDOPNCMo1 = (ImageButton) findViewById(R.id.btnDOPNCMo1);
        txtMotherPNCMonth = (EditText) findViewById(R.id.txtMotherPNCMonth);
        VlblMotherPNCMonth = (TextView) findViewById(R.id.VlblMotherPNCMonth);
        rdogrpPNCMother.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup arg0, int id) {
                if (id == R.id.rdoPNCMother1) {
                    dtpDOPNCMo1.setVisibility(View.VISIBLE);
                    btnDOPNCMo1.setVisibility(View.VISIBLE);
                    txtMotherPNCMonth.setVisibility(View.GONE);
                    txtMotherPNCMonth.setText("");
                    VlblMotherPNCMonth.setVisibility(View.GONE);
                    btnDOPNCMo1.requestFocus();
                } else if (id == R.id.rdoPNCMother2) {
                    dtpDOPNCMo1.setText("");
                    dtpDOPNCMo1.setVisibility(View.GONE);
                    btnDOPNCMo1.setVisibility(View.GONE);
                    txtMotherPNCMonth.setVisibility(View.VISIBLE);
                    VlblMotherPNCMonth.setVisibility(View.VISIBLE);
                    txtMotherPNCMonth.requestFocus();
                }
            }
        });

        btnDOPNCMo1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariableID = "btnDOPNCMo1";
                showDialog(DATE_DIALOG);
            }
        });
        cmdSavePNCMo = (Button) findViewById(R.id.cmdSavePNCMo);
        secPNCChild1 = (LinearLayout) findViewById(R.id.secPNCChild1);
        secPNCChild = (LinearLayout) findViewById(R.id.secPNCChild);
        VlblPNCChild = (TextView) findViewById(R.id.VlblPNCChild);
        rdogrpPNCChild = (RadioGroup) findViewById(R.id.rdogrpPNCChild);
        rdoPNCChild1 = (RadioButton) findViewById(R.id.rdoPNCChild1);
        rdoPNCChild2 = (RadioButton) findViewById(R.id.rdoPNCChild2);
        dtpDOPNCh1 = (EditText) findViewById(R.id.dtpDOPNCh1);
        //dtpDOPNCh1.setText(Global.DateNowDMY());
        btnDOPNCCh1 = (ImageButton) findViewById(R.id.btnDOPNCCh1);
        txtChildPNCMonth = (EditText) findViewById(R.id.txtChildPNCMonth);
        VlblChildPNCMonth = (TextView) findViewById(R.id.VlblChildPNCMonth);
        rdogrpPNCChild.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup arg0, int id) {
                if (id == R.id.rdoPNCChild1) {
                    dtpDOPNCh1.setVisibility(View.VISIBLE);
                    btnDOPNCCh1.setVisibility(View.VISIBLE);
                    txtChildPNCMonth.setVisibility(View.GONE);
                    txtChildPNCMonth.setText("");
                    VlblChildPNCMonth.setVisibility(View.GONE);
                    btnDOPNCCh1.requestFocus();
                } else if (id == R.id.rdoPNCChild2) {
                    dtpDOPNCh1.setText("");
                    dtpDOPNCh1.setVisibility(View.GONE);
                    btnDOPNCCh1.setVisibility(View.GONE);
                    txtChildPNCMonth.setVisibility(View.VISIBLE);
                    VlblChildPNCMonth.setVisibility(View.VISIBLE);
                    txtChildPNCMonth.requestFocus();
                }
            }
        });

        btnDOPNCCh1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariableID = "btnDOPNCCh1";
                showDialog(DATE_DIALOG);
            }
        });


        //..............FPI.............

        rdogrpMotherPNC = (RadioGroup) findViewById(R.id.rdogrpMotherPNC);
        rdoMotherPNC1 = (RadioButton) findViewById(R.id.rdoMotherPNC1);
        rdoMotherPNC2 = (RadioButton) findViewById(R.id.rdoMotherPNC2);

        rdogrpPNCChild1 = (RadioGroup) findViewById(R.id.rdogrpPNCChild1);
        rdoPNCChild11 = (RadioButton) findViewById(R.id.rdoPNCChild11);
        rdoPNCChild12 = (RadioButton) findViewById(R.id.rdoPNCChild12);

        cmdSaveCh1 = (Button) findViewById(R.id.cmdSaveCh1);
        cmdChild1 = (Button) findViewById(R.id.cmdChild1);
        cmdChild1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cmdChild1.setBackgroundColor(Color.GREEN);
                ChildNo.setText("1");
                cmdChild2.setBackgroundColor(Color.parseColor("#D7D7D7"));
                cmdChild3.setBackgroundColor(Color.parseColor("#D7D7D7"));
                cmdChild4.setBackgroundColor(Color.parseColor("#D7D7D7"));
                DisplayPNCVisitsChild();
            }

        });
        cmdChild2 = (Button) findViewById(R.id.cmdChild2);
        cmdChild2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cmdChild1.setBackgroundColor(Color.parseColor("#D7D7D7"));
                cmdChild2.setBackgroundColor(Color.GREEN);
                ChildNo.setText("2");
                cmdChild3.setBackgroundColor(Color.parseColor("#D7D7D7"));
                cmdChild4.setBackgroundColor(Color.parseColor("#D7D7D7"));
                DisplayPNCVisitsChild();
            }

        });
        cmdChild3 = (Button) findViewById(R.id.cmdChild3);
        cmdChild3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cmdChild1.setBackgroundColor(Color.parseColor("#D7D7D7"));
                cmdChild2.setBackgroundColor(Color.parseColor("#D7D7D7"));
                cmdChild3.setBackgroundColor(Color.GREEN);
                ChildNo.setText("3");
                cmdChild4.setBackgroundColor(Color.parseColor("#D7D7D7"));
                DisplayPNCVisitsChild();
            }

        });
        cmdChild4 = (Button) findViewById(R.id.cmdChild4);
        cmdChild4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cmdChild1.setBackgroundColor(Color.parseColor("#D7D7D7"));
                cmdChild2.setBackgroundColor(Color.parseColor("#D7D7D7"));
                cmdChild3.setBackgroundColor(Color.parseColor("#D7D7D7"));
                cmdChild4.setBackgroundColor(Color.GREEN);
                ChildNo.setText("4");
                DisplayPNCVisitsChild();
            }

        });

        if ((C.ReturnSingleValue("Select count(*)total from newBorn where cast(healthid as bigint)=" + g.getGeneratedId() + " AND pregNo = '" + pregnancyNo + "'")).equals("1")) {
            cmdChild1.setVisibility(View.VISIBLE);
            cmdChild2.setVisibility(View.GONE);
            cmdChild3.setVisibility(View.GONE);
            cmdChild4.setVisibility(View.GONE);
        } else if ((C.ReturnSingleValue("Select count(*)total from newBorn where cast(healthid as bigint)=" + g.getGeneratedId() + " AND pregNo = '" + pregnancyNo + "'")).equals("2")) {
            cmdChild1.setVisibility(View.VISIBLE);
            cmdChild2.setVisibility(View.VISIBLE);
            cmdChild3.setVisibility(View.GONE);
            cmdChild4.setVisibility(View.GONE);
        } else if ((C.ReturnSingleValue("Select count(*)total from newBorn where cast(healthid as bigint)=" + g.getGeneratedId() + " AND pregNo = '" + pregnancyNo + "'")).equals("3")) {
            cmdChild1.setVisibility(View.VISIBLE);
            cmdChild2.setVisibility(View.VISIBLE);
            cmdChild3.setVisibility(View.VISIBLE);
            cmdChild4.setVisibility(View.GONE);
        } else if ((C.ReturnSingleValue("Select count(*)total from newBorn where cast(healthid as bigint)=" + g.getGeneratedId() + " AND pregNo = '" + pregnancyNo + "'")).equals("4")) {
            cmdChild1.setVisibility(View.VISIBLE);
            cmdChild2.setVisibility(View.VISIBLE);
            cmdChild3.setVisibility(View.VISIBLE);
            cmdChild4.setVisibility(View.VISIBLE);
        }
        cmdSavePNCMo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // DataSavePNCMother();
                DataSavePNCMother();
            }
        });

        cmdSaveCh1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //   DataSavePNC();
                DataSavePNCChild();
            }
        });

        if (!C.Existence("Select healthid from Delivery where HealthId='" + g.getGeneratedId() + "' and PregNo='" + pregnancyNo + "'")) {
            Connection.MessageBox(PNCMotherChildFPI.this, "মহিলার ডেলিভারি তথ্য না থাকাতে পি এন সি তথ্য নাই");
            return;
        }


        DisplayPNCVisitsMOther();
        DisplayPNCVisitsChild();
        String delivDate = C.ReturnSingleValue("Select outcomeDate from Delivery where HealthId='" + g.getGeneratedId() + "' and PregNo='" + pregnancyNo + "'");
        if (delivDate.length() > 0) {
            if (!delivDate.equalsIgnoreCase("null")) {
                DisplayTempPNCVisit(Global.DateConvertDMY(delivDate));
            }
        }

    }

    private void DataSavePNCMother() {
        try {
            String SQL = "";
            String AG = "";
            //String ServiceId = serviceID(pregnancyNo);
            if (!rdoMotherPNC1.isChecked() & !rdoMotherPNC2.isChecked() & secPNCMother.isShown()) {
                Connection.MessageBox(PNCMotherChildFPI.this, "পরিদর্শনের তারিখ যাচাই করুন ।");
                rdoMotherPNC1.requestFocus();
                return;
            }


            String MotherServiceId = MotherserviceID(pregnancyNo);


            if (!C.Existence("Select healthId from " + TableNameMFPI + "  Where healthId='" + g.getGeneratedId() + "' AND pregNo ='" + pregnancyNo + "' AND serviceId ='" + MotherServiceId + "'")) {
                SQL = "Insert into " + TableNameMFPI + "(HealthID,pregNo,serviceId,providerId,systemEntryDate,Upload)Values('" + g.getGeneratedId() + "','" + pregnancyNo + "','" + MotherServiceId + "','" + g.getProvCode() + "','" + Global.DateTimeNowYMDHMS() + "','2')";
                C.Save(SQL);
            }

            SQL = "Update " + TableNameMFPI + " Set Upload='2',";
            SQL += "visitSource = '" + (rdoPNCMother1.isChecked() ? "1" : (rdoPNCMother2.isChecked() ? "2" : "")) + "',";
            if (rdoPNCMother1.isChecked() == true) {
                SQL += "visitDate = '" + (rdoMotherPNC1.isChecked() ? "1" : (rdoMotherPNC2.isChecked() ? "2" : "")) + "'";
            } else if (rdoPNCMother2.isChecked() == true) {
                SQL += "visitMonth = '" + (rdoMotherPNC1.isChecked() ? "1" : (rdoMotherPNC2.isChecked() ? "2" : "")) + "'";
            }
            SQL += " Where healthId='" + g.getGeneratedId() + "' AND pregNo ='" + pregnancyNo + "' AND serviceId ='" + MotherServiceId + "'";
            C.Save(SQL);
            Connection.MessageBox(PNCMotherChildFPI.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
            finish();

        } catch (Exception e) {
            Connection.MessageBox(PNCMotherChildFPI.this, e.getMessage());
            return;
        }
    }


    private void DataSavePNCChild() {
        try {
            String SQL = "";
            String AG = "";
            //String ServiceId = serviceID(pregnancyNo);

            if (!rdoPNCChild11.isChecked() & !rdoPNCChild12.isChecked() & secPNCChild.isShown()) {
                Connection.MessageBox(PNCMotherChildFPI.this, "পরিদর্শনের তারিখ যাচাই করুন ।");
                rdoPNCChild11.requestFocus();
                return;
            }


            String ChildServiceId = ChildserviceID(pregnancyNo);


            if (!C.Existence("Select healthId from " + TableNameCFPI + "  Where healthId='" + g.getGeneratedId() + "' AND pregNo ='" + pregnancyNo + "' AND serviceId ='" + ChildServiceId + "'")) {
                SQL = "Insert into " + TableNameCFPI + "(HealthID,pregNo,serviceId,providerId,systemEntryDate,Upload)Values('" + g.getGeneratedId() + "','" + pregnancyNo + "','" + ChildServiceId + "','" + g.getProvCode() + "','" + Global.DateTimeNowYMDHMS() + "','2')";
                C.Save(SQL);
            }

            SQL = "Update " + TableNameCFPI + " Set Upload='2',";
            SQL += "visitSource = '" + (rdoPNCChild1.isChecked() ? "1" : (rdoPNCChild2.isChecked() ? "2" : "")) + "',";
            if (rdoPNCChild1.isChecked() == true) {
                SQL += "visitDate = '" + (rdoPNCChild11.isChecked() ? "1" : (rdoPNCChild12.isChecked() ? "2" : "")) + "'";
            } else if (rdoPNCChild2.isChecked() == true) {
                SQL += "visitMonth = '" + (rdoPNCChild11.isChecked() ? "1" : (rdoPNCChild12.isChecked() ? "2" : "")) + "'";
            }
            SQL += " Where healthId='" + g.getGeneratedId() + "' AND pregNo ='" + pregnancyNo + "' AND serviceId ='" + ChildServiceId + "'";
            C.Save(SQL);
            Connection.MessageBox(PNCMotherChildFPI.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
            //finish();

        } catch (Exception e) {
            Connection.MessageBox(PNCMotherChildFPI.this, e.getMessage());
            return;
        }
    }

    //PNC Child Start
    //----------------------------------------------------------------------------------------------
   /* private void DataSavePNC()
    {

        try
        {
            if(!rdoPNCChild1.isChecked() & !rdoPNCChild2.isChecked() & secPNCChild.isShown())
            {
                Connection.MessageBox(PNCMotherChildFPI.this, "পরিদর্শনের উৎস কি সিলেক্ট করুন ।");
                rdoPNCChild1.requestFocus();
                return;
            }

            if(dtpDOPNCh1.getText().toString().length()==0 & dtpDOPNCh1.isShown() )
            {
                Connection.MessageBox(PNCMotherChildFPI.this, "প্রকৃত পরিদর্শনের তারিখ কত লিখুন ।");
                dtpDOPNCh1.requestFocus();
                return;
            }
            if(txtChildPNCMonth.getText().toString().length()==0 & txtChildPNCMonth.isShown())
            {
                Connection.MessageBox(PNCMotherChildFPI.this, "আনুমানিক পরিদর্শনের মাস কত লিখুন ।");
                txtChildPNCMonth.requestFocus();
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = sdf.format(c.getTime());
            String PrevChildPNCVisit = "'" + GetMaxChildPNCDate() == "0" ? "" : GetMaxChildPNCDate() + "";// '"+  GetMaxANCDate()==0?"":GetMaxANCDate()) +"'";
            Date date1 = sdf.parse(formattedDate);
            if(dtpDOPNCh1.getText().toString().length()!=0)
            {
                Date date4 = sdf.parse(dtpDOPNCh1.getText().toString());

                *//*if (PrevChildPNCVisit.equalsIgnoreCase("0")) {

            } else {
                Date MaxChildPNCVisitDate = sdf.parse(Global.DateConvertDMY(PrevChildPNCVisit));
                if (date4.before(MaxChildPNCVisitDate)) {
                    Connection.MessageBox(PNCMotherChildFPI.this, "পূর্বের ভিজিটের তারিখ অপেক্ষা বর্তমান ভিজিট বেশি  হতে হবে।");
                    dtpDOPNCh1.requestFocus();
                    return;
                }
            }*//*

            }


            if(dtpDOPNCh1.getText().toString().length()==0 & dtpDOPNCh1.isShown())
            {
                Connection.MessageBox(PNCMotherChildFPI.this, "পি এন সি  এর তারিখ কত লিখুন।");
                dtpDOPNCh1.requestFocus();
                return;
            }


            String SQL = "";

            String SQ = "select visitDate FROM pncServiceChild ";
            SQ += " WHERE healthId = '"+ g.getGeneratedId() +"' AND pregNo ='"+ pregnancyNo +"' and serviceId='"+ VlblServiceIdChild.getText() +"'";//visitDate='"+ Global.DateConvertYMD(dtpDOPNCh1.getText().toString()) +"'

           *//* if(!C.Existence(SQL) && !C.Existence(SQ))
            {*//*
            if(!C.Existence(SQ))
            {
                String ChildServiceId = ChildserviceID(pregnancyNo);
                SQL = "Insert into "+ TableNamePNC_Child +"(healthId,pregNo,ChildNo,serviceId,providerId,visitDate,systemEntryDate,upload)Values(";
                SQL += "'"+ g.getGeneratedId() +"',";
                SQL += "'"+ pregnancyNo +"',";
                SQL += "'"+ ChildNo.getText() +"',";
                SQL += "'"+ ChildServiceId +"',";//GetServiceId()
                SQL += "'"+ g.getProvCode() +"',";
                SQL += "'"+ Global.DateConvertYMD(dtpDOPNCh1.getText().toString()) +"',";
                SQL += "'"+ Global.DateTimeNowYMDHMS() +"',";
                SQL += "'2')";
                C.Save(SQL);
                sqlupdate1 = "Update pncServiceChild Set ";
                sqlupdate1+="visitSource = '"+ (rdoPNCChild1.isChecked()?"1":"2") +"',";
                if(rdoPNCChild1.isChecked())
                {
                    sqlupdate1+="visitDate = '"+ Global.DateConvertYMD(dtpDOPNCh1.getText().toString()) +"',";
                    sqlupdate1+="visitMonth = '"+ Global.DateDifferenceMonth(Global.DateNowDMY(), dtpDOPNCh1.getText().toString()) +"'";//DateDifferenceYears DateDifferenceMonth
                }
                else
                {
                    sqlupdate1+="visitDate = '"+ Global.addDaysYMD(Global.DateNowDMY(), -Integer.valueOf(txtChildPNCMonth.getText().toString())*30) +"',";
                    //sqlupdate+="MRDate = '"+ Global.addDaysYMD(Global.DateConvertDMY(dtpDOV.getText().toString()), -Integer.valueOf(txtMDAge.getText().toString())*30) +"',";
                    sqlupdate1+="visitMonth = '"+ txtChildPNCMonth.getText().toString() +"'";
                }
                sqlupdate1+=" Where HealthID='"+ g.getGeneratedId() +"' and pregNo='"+ pregnancyNo +"' and serviceId='"+ ChildServiceId +"'";
                C.Save(sqlupdate1);
                txtChildPNCMonth.setText("");
                rdogrpPNCChild.clearCheck();
                dtpDOPNCh1.setText("");
                DisplayPNCVisitsChild();
                VlblServiceIdChild.setText("");
                cmdSaveCh1.setText("Save");
                ((LinearLayout)findViewById(R.id.secChildList)).setVisibility(View.VISIBLE);
                Connection.MessageBox(PNCMotherChildFPI.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
            }
            else
            {
                //Connection.MessageBox(PNCMotherChildFPI.this, "পি এন সি  এর ভিসিট দেয়া আছে");
                //return;
                SQL = "Update "+ TableNamePNC_Child +" Set ";
                SQL+="providerId = '"+ g.getProvCode() +"',";
                //SQL+="visitDate = '"+ Global.DateConvertYMD(dtpDOPNCh1.getText().toString()) +"',";
                SQL+="visitSource = '"+ (rdoPNCChild1.isChecked()?"1":"2") +"',";
                if(rdoPNCChild1.isChecked())
                {
                    SQL+="visitDate = '"+ Global.DateConvertYMD(dtpDOPNCh1.getText().toString()) +"',";
                    SQL+="visitMonth = '"+ Global.DateDifferenceMonth(Global.DateNowDMY(), dtpDOPNCh1.getText().toString()) +"',";//DateDifferenceYears DateDifferenceMonth
                }
                else
                {
                    SQL+="visitDate = '"+ Global.addDaysYMD(Global.DateNowDMY(), -Integer.valueOf(txtChildPNCMonth.getText().toString())*30) +"',";
                    SQL+="visitMonth = '"+ txtChildPNCMonth.getText().toString() +"',";
                }
                SQL+="modifyDate = '"+ Global.DateTimeNowYMDHMS() +"',";
                SQL+="upload = '2'";
                SQL+=" Where HealthID='"+ g.getGeneratedId() +"' and pregNo='"+ pregnancyNo +"' and childNo='"+ ChildNo.getText() +"' and serviceId='"+ VlblServiceIdChild.getText() +"'";
                C.Save(SQL);
                dtpDOPNCh1.setText("");
                txtChildPNCMonth.setText("");
                rdogrpPNCChild.clearCheck();
                DisplayPNCVisitsChild();
                VlblServiceIdChild.setText("");
                cmdSaveCh1.setText("Save");
                Connection.MessageBox(PNCMotherChildFPI.this, "তথ্য সফলভাবে সংশোধন হয়েছে।");
            }

        }
        catch(Exception  e)
        {
            Connection.MessageBox(PNCMotherChildFPI.this, e.getMessage());
            return;
        }
    }
*/
    private String GetMaxChildPNCDate() {
        String SQL = "";
        String MaxANCDate = "";
        SQL = "Select ifnull(Max(visitDate),0) from pncServiceChild WHERE healthId ='" + g.getGeneratedId() + "' and pregNo=(select Max(pregNo) from pncServiceChild WHERE healthId='" + g.getGeneratedId() + "')";
        MaxANCDate = C.ReturnSingleValue(SQL);
        return MaxANCDate;
    }


    private void DisplayPNCVisitsChild() {
        GridView gcount = (GridView) findViewById(R.id.gridPNC1);
        g.setImuCode(String.valueOf(gcount.getCount() + 1));
        PNCVisitsChild();
    }

    public void PNCVisitsChild() {
        GridView g1 = (GridView) findViewById(R.id.gridPNC1);
        g1.setAdapter(new PNCChild(this));
        g1.setNumColumns(6);
    }

    public class PNCChild extends BaseAdapter {
        private Context mContext;
        String[][] vcode;
        Integer totalRec;

        public PNCChild(Context c) {
            mContext = c;
        }

        public int getCount() {
            return Integer.parseInt(C.ReturnSingleValue("Select count(*)total from pncServiceChild where healthid='" + g.getGeneratedId() + "' AND pregNo = '" + pregnancyNo + "' and ChildNo='" + ChildNo.getText() + "'"));
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
                /*String SQL = "Select healthId, pregNo,serviceId, visitDate from pncServiceMother where healthid='" + g.getGeneratedId() + "' AND pregNo = '" + PGNNo() + "'" + " order by cast(visitDate as DATE) asc";*/
                String SQL = "Select healthId, pregNo,serviceId, visitDate,visitMonth, childNo from pncServiceChild where healthid='" + g.getGeneratedId() + "' AND pregNo = '" + pregnancyNo + "' and ChildNo='" + ChildNo.getText() + "' order by cast(visitDate as DATE) asc";

                try {
                    Cursor cur = C.ReadData(SQL);
                    cur.moveToFirst();

                    totalRec = cur.getCount();
                    vcode = new String[4][totalRec];
                    int i = 0;
                    while (!cur.isAfterLast()) {
                        vcode[0][i] = "পরিদর্শন " + String.valueOf(i + 1) + "\n শিশু  " + String.valueOf(cur.getString(cur.getColumnIndex("childNo"))) + "\n " + Global.DateConvertDMY(String.valueOf(cur.getString(cur.getColumnIndex("visitDate"))));
                        vcode[1][i] = String.valueOf(cur.getString(cur.getColumnIndex("serviceId")));
                        vcode[2][i] = String.valueOf(cur.getString(cur.getColumnIndex("visitDate")));
                        //vcode[3][i] = String.valueOf(cur.getString(cur.getColumnIndex("childNo")));
                        i += 1;
                        cur.moveToNext();
                    }

                    cur.close();

                    Button tv = (Button) MyView.findViewById(R.id.image_name);
                    tv.setTextSize(14);
                    tv.setText(vcode[0][position]);//+ "\n" + vcode[2][position]);
                    final Integer p = position;
                    tv.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {

                            String ServiceId = String.valueOf(vcode[1][position]);

                            //Cursor cur = C.ReadData("Select  visit as visit,vdate as vdate,visitstatus as visitstatus,currstatus as currstatus,ssource as ssource,qty as qty,unit as unit,brand as brand,validity as validity,daymonyear as dmy, ifnull(referPlace,'') as referplace, MRDate,syrinsQty, ifnull(pregNo,'') AS PGNNo from ElcoVisit where healthid='"+ g.getGeneratedId() +"' and visit='"+ vcode[3][position] +"'");
                            Cursor cur = C.ReadData("Select healthId, pregNo,serviceId,visitSource,visitDate,childNo,visitMonth from pncServiceChild where healthid='" + g.getGeneratedId() + "' AND pregNo = '" + pregnancyNo + "' and ChildNo='" + ChildNo.getText() + "' and serviceId='" + ServiceId + "'");//vcode[1][position]
                            cur.moveToFirst();

                            while (!cur.isAfterLast()) {

                                VlblServiceIdChild.setText(ServiceId);
                                //cur.getString(cur.getColumnIndex("visitDate")));
                                // cmdSaveCh1.setText("Edit");

                                if (cur.getString(cur.getColumnIndex("visitSource")).equals("1") | cur.getString(cur.getColumnIndex("visitSource")).equals("null") | cur.getString(cur.getColumnIndex("visitSource")).equals(null)) {
                                    rdoPNCChild1.setChecked(true);
                                } else if (cur.getString(cur.getColumnIndex("visitSource")).equals("2")) {
                                    rdoPNCChild2.setChecked(true);
                                }
                                String visitDate = cur.getString(cur.getColumnIndex("visitDate"));
                                if (visitDate != null) {
                                    dtpDOPNCh1.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("visitDate"))));
                                }
                                txtChildPNCMonth.setText(cur.getString(cur.getColumnIndex("visitMonth")));

                                cur.moveToNext();
                            }
                            cur.close();
                            FPIDataSearchChild(g.getGeneratedId(), pregnancyNo, ServiceId);
                        }
                    });
                } catch (Exception ex) {
                    Connection.MessageBox(PNCMotherChildFPI.this, ex.getMessage());
                }

            }
            return MyView;
        }

    }

    private void FPIDataSearchMother(String generatedId, String pregnancyNo, String ServiceId) {
        try {
            RadioButton rb;
            String SQL1 = "";
            SQL1 = "Select ifnull(healthId,'') as HealthID,ifnull(visitDate,'') as visitDate,ifnull(visitMonth,'') as visitMonth\n" +
                    "from pncServiceMotherFPI FPI Where HealthID='" + generatedId + "' AND pregNo ='" + pregnancyNo + "' AND serviceId ='" + ServiceId + "'";
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
                    rdoMotherPNC1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("visitDate")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoMotherPNC2.setChecked(true);
                }


                if (cur1.getString(cur1.getColumnIndex("visitMonth")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoMotherPNC1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("visitMonth")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoMotherPNC2.setChecked(true);
                }


                cur1.moveToNext();
            }
            cur1.close();
        } catch (Exception e) {
            Connection.MessageBox(PNCMotherChildFPI.this, e.getMessage());
            return;
        }
    }

    private void FPIDataSearchChild(String generatedId, String pregnancyNo, String ServiceId) {
        try {
            RadioButton rb;
            String SQL1 = "";
            SQL1 = "Select ifnull(healthId,'') as HealthID,ifnull(visitDate,'') as visitDate,ifnull(visitMonth,'') as visitMonth\n" +
                    "from pncServiceChildFPI FPI Where HealthID='" + generatedId + "' AND pregNo ='" + pregnancyNo + "' AND serviceId ='" + ServiceId + "'";
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
                    rdoPNCChild11.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("visitDate")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoPNCChild12.setChecked(true);
                }


                if (cur1.getString(cur1.getColumnIndex("visitMonth")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoPNCChild11.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("visitMonth")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoPNCChild12.setChecked(true);
                }


                cur1.moveToNext();
            }
            cur1.close();
        } catch (Exception e) {
            Connection.MessageBox(PNCMotherChildFPI.this, e.getMessage());
            return;
        }
    }

    //PNC Child End
    //----------------------------------------------------------------------------------------------
   /* private void DataSavePNCMother()
    {

        try
        {
            if(!rdoPNCMother1.isChecked() & !rdoPNCMother2.isChecked() & secPNCMother.isShown())
            {
                Connection.MessageBox(PNCMotherChildFPI.this, "পরিদর্শনের উৎস কি সিলেক্ট করুন ।");
                rdoPNCMother1.requestFocus();
                return;
            }

            if(dtpDOPNCMo1.getText().toString().length()==0 & dtpDOPNCMo1.isShown() )
            {
                Connection.MessageBox(PNCMotherChildFPI.this, "প্রকৃত পরিদর্শনের তারিখ কত লিখুন ।");
                dtpDOPNCMo1.requestFocus();
                return;
            }
            if(txtMotherPNCMonth.getText().toString().length()==0 & txtMotherPNCMonth.isShown())
            {
                Connection.MessageBox(PNCMotherChildFPI.this, "আনুমানিক পরিদর্শনের মাস কত লিখুন ।");
                txtMotherPNCMonth.requestFocus();
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = sdf.format(c.getTime());
            String PrevPNCVisit = "'" + GetMaxPNCDate() == "0" ? "" : GetMaxPNCDate() + "";// '"+  GetMaxANCDate()==0?"":GetMaxANCDate()) +"'";
            Date date1 = sdf.parse(formattedDate);

            if(dtpDOPNCMo1.getText().toString().length()!=0)
            {
                Date date4 = sdf.parse(dtpDOPNCMo1.getText().toString());
                 *//* if (PrevPNCVisit.equalsIgnoreCase("0")) {

            } else {
                Date MaxPNCVisitDate = sdf.parse(Global.DateConvertDMY(PrevPNCVisit));
                if (date4.before(MaxPNCVisitDate)) {
                    Connection.MessageBox(PNCMotherChildFPI.this, "পূর্বের ভিজিটের তারিখ অপেক্ষা বর্তমান ভিজিট বেশি হতে হবে।");
                    dtpDOPNCMo1.requestFocus();
                    return;
                }
            }*//*
            }


            String SQL = "";
            String SQ = "select visitDate FROM pncServiceMother ";
            SQ += " WHERE healthId = '"+ g.getGeneratedId() +"' AND pregNo ='"+ pregnancyNo +"' and serviceId='"+ VlblServiceIdMother.getText() +"'";//visitDate='"+ Global.DateConvertYMD(dtpDOPNCMo1.getText().toString()) +"'";
           *//* if(!C.Existence(SQL) && !C.Existence(SQ))
            {*//*
            if(!C.Existence(SQ))
            {
                String MotherServiceId = MotherserviceID(pregnancyNo);
                SQL = "Insert into " + TableNamePNC_Mother + "(healthId, pregNo, serviceId, providerId, visitDate,systemEntryDate,upload)Values('"+
                        g.getGeneratedId() +"','"+ pregnancyNo +"','"+  MotherServiceId +"','"+ g.getProvCode() +"','"+ Global.DateConvertYMD(dtpDOPNCMo1.getText().toString()) +"','"+  Global.DateTimeNowYMDHMS() +"','2')";
                C.Save(SQL);

                sqlupdate2 = "Update pncServiceMother Set ";
                sqlupdate2+="visitSource = '"+ (rdoPNCMother1.isChecked()?"1":"2") +"',";
                if(rdoPNCMother1.isChecked())
                {
                    sqlupdate2+="visitDate = '"+ Global.DateConvertYMD(dtpDOPNCMo1.getText().toString()) +"',";
                    sqlupdate2+="visitMonth = '"+ Global.DateDifferenceMonth(Global.DateNowDMY(), dtpDOPNCMo1.getText().toString()) +"'";//DateDifferenceYears DateDifferenceMonth
                }
                else
                {
                    sqlupdate2+="visitDate = '"+ Global.addDaysYMD(Global.DateNowDMY(), -Integer.valueOf(txtMotherPNCMonth.getText().toString())*30) +"',";
                    sqlupdate2+="visitMonth = '"+ txtMotherPNCMonth.getText().toString() +"'";
                }
                sqlupdate2+=" Where HealthID='"+ g.getGeneratedId() +"' and pregNo='"+ pregnancyNo +"' and serviceId='"+ MotherServiceId +"'";
                C.Save(sqlupdate2);
                txtMotherPNCMonth.setText("");
                rdogrpPNCMother.clearCheck();
                dtpDOPNCMo1.setText("");
                VlblServiceIdMother.setText("");
                cmdSavePNCMo.setText("Save");
                DisplayPNCVisitsMOther();
                ((LinearLayout)findViewById(R.id.List)).setVisibility(View.VISIBLE);
                Connection.MessageBox(PNCMotherChildFPI.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
            }
            else
            {
                SQL = "Update "+ TableNamePNC_Mother +" Set ";
                SQL+="providerId = '"+ g.getProvCode() +"',";
                SQL+="visitSource = '"+ (rdoPNCMother1.isChecked()?"1":"2") +"',";
                if(rdoPNCMother1.isChecked())
                {
                    SQL+="visitDate = '"+ Global.DateConvertYMD(dtpDOPNCMo1.getText().toString()) +"',";
                    SQL+="visitMonth = '"+ Global.DateDifferenceMonth(Global.DateNowDMY(), dtpDOPNCMo1.getText().toString()) +"',";//DateDifferenceYears DateDifferenceMonth
                }
                else
                {
                    SQL+="visitDate = '"+ Global.addDaysYMD(Global.DateNowDMY(), -Integer.valueOf(txtMotherPNCMonth.getText().toString())*30) +"',";
                    SQL+="visitMonth = '"+ txtMotherPNCMonth.getText().toString() +"',";
                }
                //SQL+="visitDate = '"+ Global.DateConvertYMD(dtpDOPNCMo1.getText().toString()) +"',";
                SQL+="modifyDate = '"+ Global.DateTimeNowYMDHMS() +"',";
                SQL+="upload = '2'";
                SQL+=" Where HealthID='"+ g.getGeneratedId() +"' and pregNo='"+ pregnancyNo +"' and serviceId='"+ VlblServiceIdMother.getText() +"'";
                C.Save(SQL);
                txtMotherPNCMonth.setText("");
                rdogrpPNCMother.clearCheck();
                dtpDOPNCMo1.setText("");
                DisplayPNCVisitsMOther();
                VlblServiceIdMother.setText("");
                cmdSavePNCMo.setText("Save");
                Connection.MessageBox(PNCMotherChildFPI.this, "তথ্য সফলভাবে সংশোধন হয়েছে।");
            }

        }
        catch(Exception  e)
        {
            Connection.MessageBox(PNCMotherChildFPI.this, e.getMessage());
            return;
        }
    }
*/
    private String GetMaxPNCDate() {
        String SQL = "";
        String MaxANCDate = "";
        SQL = "Select ifnull(Max(visitDate),0) from pncServiceMother WHERE healthId ='" + g.getGeneratedId() + "' and pregNo=(select Max(pregNo) from pncServiceMother WHERE healthId='" + g.getGeneratedId() + "')";
        MaxANCDate = C.ReturnSingleValue(SQL);
        return MaxANCDate;
    }

    private void DisplayPNCVisitsMOther() {
        GridView gcount = (GridView) findViewById(R.id.gridPNC);
        g.setImuCode(String.valueOf(gcount.getCount() + 1));
        PNCVisits();
    }

    public void PNCVisits() {
        GridView g1 = (GridView) findViewById(R.id.gridPNC);
        g1.setAdapter(new PNCMother(this));
        g1.setNumColumns(6);
    }

    public class PNCMother extends BaseAdapter {
        private Context mContext;
        String[][] vcode;
        Integer totalRec;

        public PNCMother(Context c) {
            mContext = c;
        }

        public int getCount() {
            return Integer.parseInt(C.ReturnSingleValue("Select count(*)total from pncServiceMother where healthid='" + g.getGeneratedId() + "' AND pregNo = '" + pregnancyNo + "'"));
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
                String SQL = "Select healthId, pregNo,serviceId, visitDate from pncServiceMother where healthid='" + g.getGeneratedId() + "' AND pregNo = '" + pregnancyNo + "'" + " order by cast(visitDate as DATE) asc";

                try {
                    Cursor cur = C.ReadData(SQL);
                    cur.moveToFirst();

                    totalRec = cur.getCount();
                    vcode = new String[4][totalRec];
                    int i = 0;
                    while (!cur.isAfterLast()) {
                        vcode[0][i] = "পরিদর্শন " + String.valueOf(i + 1) + " " + Global.DateConvertDMY(String.valueOf(cur.getString(cur.getColumnIndex("visitDate"))));
                        vcode[1][i] = String.valueOf(cur.getString(cur.getColumnIndex("serviceId")));
                        vcode[2][i] = String.valueOf(cur.getString(cur.getColumnIndex("visitDate")));
                        i += 1;
                        cur.moveToNext();
                    }

                    cur.close();

                    Button tv = (Button) MyView.findViewById(R.id.image_name);
                    tv.setTextSize(14);
                    tv.setText(vcode[0][position]);//+ "\n" + vcode[2][position]);
                    final Integer p = position;
                    tv.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {

                            String ServiceId = String.valueOf(vcode[1][position]);

                            Cursor cur = C.ReadData("Select healthId, pregNo,serviceId,visitSource, visitDate,visitMonth from pncServiceMother where healthid='" + g.getGeneratedId() + "' AND pregNo = '" + pregnancyNo + "' and serviceId='" + ServiceId + "'");//vcode[1][position]
                            cur.moveToFirst();

                            while (!cur.isAfterLast()) {

                                VlblServiceIdMother.setText(ServiceId);

                                if (cur.getString(cur.getColumnIndex("visitSource")).equals("1") | cur.getString(cur.getColumnIndex("visitSource")).equals("null") | cur.getString(cur.getColumnIndex("visitSource")).equals(null)) {
                                    rdoPNCMother1.setChecked(true);
                                } else if (cur.getString(cur.getColumnIndex("visitSource")).equals("2")) {
                                    rdoPNCMother2.setChecked(true);
                                }
                                String visitDate = cur.getString(cur.getColumnIndex("visitDate"));
                                if (visitDate != null) {
                                    dtpDOPNCMo1.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("visitDate"))));
                                }
                                txtMotherPNCMonth.setText(cur.getString(cur.getColumnIndex("visitMonth")));
                                // dtpDOPNCMo1.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("visitDate"))));//cur.getString(cur.getColumnIndex("visitDate")));
                                // cmdSavePNCMo.setText("Edit");
                                cur.moveToNext();
                            }
                            cur.close();
                            FPIDataSearchMother(g.getGeneratedId(), pregnancyNo, ServiceId);
                            //DisplaySelectedPNCInfo(ServiceId);
                        }
                    });
                } catch (Exception ex) {
                    Connection.MessageBox(PNCMotherChildFPI.this, ex.getMessage());
                }

            }
            return MyView;
        }

    }

    private void DisplaySelectedPNCInfo(String ServiceId) {

        String SQL = "Select visitDate from pncServiceMother where serviceId = '" + ServiceId + "'";


        try {
            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();

            while (!cur.isAfterLast()) {
                // dtpVDate.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("visitDate"))));
                if (!cur.getString(cur.getColumnIndex("visitDate")).equalsIgnoreCase("")) {
                    ((EditText) findViewById(R.id.dtpDOPNCCh1)).setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("visitDate"))));
                }
                cur.moveToNext();
            }

            cur.close();
        } catch (Exception ex) {
            Connection.MessageBox(PNCMotherChildFPI.this, ex.getMessage());
        }
    }

    private String ChildserviceID(String pregNo) {
        String SQL = "";
        String PGNNo = "";

        //String MaxPGNNo=PregMaxPGNNo();
        SQL = "select '0'||(ifnull(max(cast(serviceId as int)),0))MaxserviceId from pncServiceChild WHERE healthId = " + g.getGeneratedId() + " AND pregNo = " + pregNo;

        String tempserviceID = C.ReturnSingleValue(SQL);


        return String.valueOf((Long.parseLong(tempserviceID)));

      /*  if (serviceID.equalsIgnoreCase("1")) {
            return String.valueOf(g.getGeneratedId() + pregNo + serviceID);
        } else {
            return String.valueOf(serviceID);
        }*/
    }

    private String MotherserviceID(String pregNo) {
        String SQL = "";
        String PGNNo = "";

        //String MaxPGNNo=PregMaxPGNNo();
        SQL = "select '0'||(ifnull(max(cast(serviceId as int)),0))MaxserviceId from pncServiceMother WHERE healthId = " + g.getGeneratedId() + " AND pregNo = " + pregNo;

        String tempserviceID = C.ReturnSingleValue(SQL);


        return String.valueOf((Long.parseLong(tempserviceID)));

        /*if (serviceID.equalsIgnoreCase("1")) {
            return String.valueOf(g.getGeneratedId() + pregNo + serviceID);
        } else {
            return String.valueOf(serviceID);
        }*/
    }

    private String GetServiceId() {
        String SQL = "";

        SQL = "select '0'||(ifnull(max(cast(serviceId as int)),0))MaxserviceId from pncServiceMother";
        SQL += " WHERE healthId = '" + g.getGeneratedId() + "' AND pregNo ='" + pregnancyNo + "'";

        String tempserviceID = C.ReturnSingleValue(SQL);


        //String serviceID = String.valueOf((Integer.parseInt(tempserviceID) + 1));
        //Long.parseLong
        String serviceID = String.valueOf((Long.parseLong(tempserviceID) + 1));

        if (serviceID.equalsIgnoreCase("1")) {
            return String.valueOf(g.getGeneratedId() + pregnancyNo + serviceID);
        } else {
            return String.valueOf(serviceID);
        }
    }

    private void PNCVisitSearch(String serviceId) {
        try {
            String SQL = "";
            SQL = "select healthId, pregNo, serviceId, providerId, visitDate, serviceSource, systemEntryDate, upload,modifyDate FROM pncServiceMother ORDER BY visitDate";
            SQL += " WHERE healthId = '" + g.getGeneratedId() + "' AND pregNo ='" + pregnancyNo + "'  and serviceId='" + serviceId + "'";
            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                if (!cur.getString(cur.getColumnIndex("visitDate")).equals("null")) {
                    dtpDOPNCh1.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("visitDate"))));
                }

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(PNCMotherChildFPI.this, e.getMessage());
            return;
        }
    }


    protected Dialog onCreateDialog(int id) {
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        Integer Y = g.mYear;
        Integer M = g.mMonth;
        Integer D = g.mDay;
       /* if(dtpDOM.getText().length()>0)
        {
            Y = Integer.valueOf(Global.Right(dtpDOM.getText().toString(), 4));
            M = Integer.valueOf(dtpDOM.getText().toString().substring(4,5));
            D = Integer.valueOf(Global.Left(dtpDOM.getText().toString(), 2));
        }*/
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

            dtpDate = (EditText) findViewById(R.id.dtpDOM);


            if (VariableID.equals("btnDOPNCMo1")) {
                dtpDate = (EditText) findViewById(R.id.dtpDOPNCMo1);
            } else if (VariableID.equals("btnDOPNCCh1")) {
                dtpDate = (EditText) findViewById(R.id.dtpDOPNCh1);
            }


            dtpDate.setText(new StringBuilder()
                    .append(Global.Right("00" + mDay, 2)).append("/")
                    .append(Global.Right("00" + mMonth, 2)).append("/")
                    .append(mYear));
        }
    };
    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
            hour = selectedHour;
            minute = selectedMinute;
            EditText tpTime;

        }
    };


    private void DisplayTempPNCVisit(String ExpDelivDate) {
        GridView gcount = (GridView) findViewById(R.id.gridPNCExp);
        g.setImuCode(String.valueOf(gcount.getCount() + 1));
        ExpectedPNCMother(ExpDelivDate);
    }

    public void ExpectedPNCMother(String ExpPNCDate) {
        GridView g1 = (GridView) findViewById(R.id.gridPNCExp);
        g1.setAdapter(new ExpectedPNCMother(this, ExpPNCDate));
        g1.setNumColumns(6);
    }

    public class ExpectedPNCMother extends BaseAdapter {
        private Context mContext;
        String[][] vcode;
        Integer totalRec;
        String ExpDate;

        public ExpectedPNCMother(Context c, String ExpPncDate) {
            mContext = c;
            ExpDate = ExpPncDate;
        }

        public int getCount() {


            return Integer.parseInt("4");//C.ReturnSingleValue("Select count(*)total from Immunization where healthid='"+ g.getGeneratedId() +"'"));
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
                    String expDate = ExpDate;//GetMaxLMPDate();
                   /* vcode[0][0] = "পরিদর্শন 1 " + Global.addDays(Global.DateConvertDMY(expDate), 106) +"\n হতে \n"+ Global.addDays(Global.DateConvertDMY(expDate), 112);
                    vcode[0][1] = "পরিদর্শন 2 " + Global.addDays(Global.DateConvertDMY(expDate), 162) +"\n হতে \n"+ Global.addDays(Global.DateConvertDMY(expDate), 196);
                    vcode[0][2] = "পরিদর্শন 3 " + Global.addDays(Global.DateConvertDMY(expDate), 218) +"\n হতে \n"+ Global.addDays(Global.DateConvertDMY(expDate), 224);
                    vcode[0][3] = "পরিদর্শন 4 " + Global.addDays(Global.DateConvertDMY(expDate), 246) +"\n হতে \n"+ Global.addDays(Global.DateConvertDMY(expDate), 252);
*/
                    vcode[0][0] = "পরিদর্শন ১ " + Global.addDays(expDate, 0) + "\n হতে \n" + Global.addDays(expDate, 0);
                    vcode[0][1] = "পরিদর্শন ২ " + Global.addDays(expDate, 1) + "\n হতে \n" + Global.addDays(expDate, 2);
                    vcode[0][2] = "পরিদর্শন ৩ " + Global.addDays(expDate, 6) + "\n হতে \n" + Global.addDays(expDate, 13);
                    vcode[0][3] = "পরিদর্শন ৪ " + Global.addDays(expDate, 36) + "\n হতে \n" + Global.addDays(expDate, 42);

                    /* vcode[0][0] = "পরিদর্শন ১ " + Global.addDays(expDate, 106) +"\n হতে \n"+ Global.addDays(expDate, 112);
                    vcode[0][1] = "পরিদর্শন ২ " + Global.addDays(expDate, 162) +"\n হতে \n"+ Global.addDays(expDate, 196);
                    vcode[0][2] = "পরিদর্শন ৩ " + Global.addDays(expDate, 218) +"\n হতে \n"+ Global.addDays(expDate, 224);
                    vcode[0][3] = "পরিদর্শন ৪ " + Global.addDays(expDate, 246) +"\n হতে \n"+ Global.addDays(expDate, 252);*/

                    i += 1;

                    Button tv = (Button) MyView.findViewById(R.id.image_name);
                    tv.setTextSize(14);
                    tv.setText(vcode[0][position] + "\n");
                    final Integer p = position;

                } catch (Exception ex) {
                    Connection.MessageBox(PNCMotherChildFPI.this, ex.getMessage());
                }

            }
            return MyView;
        }
    }

}
