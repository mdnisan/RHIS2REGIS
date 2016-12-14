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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import Common.Connection;
import Common.Global;

/**
 * Created by Nisan on 5/1/2016.
 */
public class Delivfpi extends Activity {
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
        AlertDialog.Builder adb = new AlertDialog.Builder(Delivfpi.this);
        switch (item.getItemId()) {
            case R.id.menuClose:
                adb.setTitle("Close");
                adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান?");
                adb.setNegativeButton("না", null);
                adb.setPositiveButton("হ্যাঁ", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        Intent f2 = new Intent(getApplicationContext(), MemberListFPI.class);
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
    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();

    private String TableName;
    private String TableNameFPI;
    private String TableNamePNC;
    private String ChildTableName;
    private String MemberTable;

    LinearLayout seclbldelete;
    LinearLayout secDiv;
    TextView VlblDiv;
    EditText txtDiv;

    LinearLayout secDist;
    TextView VlblDist;
    EditText txtDist;

    LinearLayout secUpz;
    TextView VlblUpz;
    EditText txtUpz;

    LinearLayout secUN;
    TextView VlblUN;
    EditText txtUN;

    LinearLayout secMouza;
    TextView VlblMouza;
    EditText txtMouza;

    LinearLayout secVill;
    TextView VlblVill;
    EditText txtVill;

    LinearLayout seclblH;
    TextView lblHlblH;
    TextView lblHealthID;
    TextView txtHealthID;

    LinearLayout secSl;
    TextView txtSLNo;
    TextView VlblSLNo;

    TextView VlblSNo;
    TextView txtSNo;
    TextView txtElCONo;

    LinearLayout secName;
    TextView VlblName;
    TextView txtName;

    LinearLayout secHusName;
    TextView VlblHusName;
    TextView txtHusName;
    TextView txtAgeYrHus;

    LinearLayout secAge;
    TextView VlblAge;
    TextView txtAge;

    LinearLayout secOutcome;
    TextView VlblOutcome;
    RadioGroup rdogrpOutcome;
    RadioButton rdoOutcomeLB;
    RadioButton rdoOutcomeSB;
    RadioButton rdoOutcomeAbo;

    LinearLayout secLivebirth;
    TextView VlblLivebirth;
    EditText txtLivebirthNum1;
    EditText txtLivebirthNum2;


    LinearLayout secOutcomeDT;
    TextView VlblOutcomeDT;
    EditText dtpOutcomeDT;
    ImageButton btnOutcomeDT;
    ImageButton btnDOPNCCh1;

    LinearLayout secBPlace;
    TextView VlblBPlace;
    Spinner spnBPlace;

    LinearLayout secBAtten;
    TextView VlblBAtten;
    Spinner spnBAtten;

    LinearLayout secBType;
    TextView VlblBType;
    RadioGroup rdogrpBType;
    RadioButton rdoBTypeNor;
    RadioButton rdoBTypeSeg;

    LinearLayout secMiso;
    TextView VlblMiso;
    RadioGroup rdogrpMiso;
    RadioButton rdoMisoYes;
    RadioButton rdoMisoNo;
    LinearLayout seclivebirth;

    //..............FPI.............
    RadioGroup rdogrpbirthdeathabortion1;
    RadioButton rdobirthdeathabortion1;
    RadioButton rdobirthdeathabortion2;

    RadioGroup rdogrpOutcomeDT;
    RadioButton rdoOutcomeDT1;
    RadioButton rdoOutcomeDT2;

    RadioGroup rdogrpBPlace;
    RadioButton rdoBPlace1;
    RadioButton rdoBPlace2;

    RadioGroup rdogrpBAtten;
    RadioButton rdoBAtten1;
    RadioButton rdoBAtten2;

    RadioGroup rdogrpBType1;
    RadioButton rdoBType11;
    RadioButton rdoBType12;

    RadioGroup rdogrpMiso1;
    RadioButton rdoMiso11;
    RadioButton rdoMiso12;

    Button cmdSave;
    ImageButton cmdSavePNC;

    String StartTime;
    TextView VlblChildList;
    CheckBox chklivebirth;
    CheckBox chkdeathbirth;
    CheckBox chkabortion;
    EditText dtpDOPNCCh1;
    String sqlnew = "";
    String sqlupdate = "";

    String pregnancyNo;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.delivfpi);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();

            TableName = "delivery";
            TableNameFPI = "deliveryFPI";
            TableNamePNC = "pncServiceMother";
            ChildTableName = "newBorn";
            MemberTable = "Member";

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

            /*
            seclblH = (LinearLayout) findViewById(R.id.seclblH);
            lblHlblH = (TextView) findViewById(R.id.lblHlblH);
            lblHealthID = (TextView) findViewById(R.id.lblHealthID);
            txtHealthID = (TextView) findViewById(R.id.txtHealthID);

            secSl = (LinearLayout) findViewById(R.id.secSl);
            VlblSNo = (TextView) findViewById(R.id.VlblSNo);
            txtSNo = (TextView) findViewById(R.id.txtSNo);
            VlblSLNo = (TextView) findViewById(R.id.VlblSNo);
            txtSLNo = (TextView) findViewById(R.id.txtSLNo);
            txtSLNo.setText(GetCountSLNoNumber());
            VlblSNo.setVisibility(View.GONE);
            txtSNo.setVisibility(View.GONE);

            txtAge = (TextView) findViewById(R.id.txtAge);

            secName = (LinearLayout) findViewById(R.id.secName);
            VlblName = (TextView) findViewById(R.id.VlblName);
            txtName = (TextView) findViewById(R.id.txtName);

            secHusName = (LinearLayout) findViewById(R.id.secHusName);
            VlblHusName = (TextView) findViewById(R.id.VlblHusName);
            txtHusName = (TextView) findViewById(R.id.txtHusName);
            txtAgeYrHus = (TextView) findViewById(R.id.txtAgeYrHus);

            */
            seclivebirth = (LinearLayout) findViewById(R.id.seclivebirth);
            secOutcome = (LinearLayout) findViewById(R.id.secOutcome);

            txtLivebirthNum1 = (EditText) findViewById(R.id.txtLivebirthNum1);

            txtLivebirthNum1.setEnabled(false);

            txtLivebirthNum2 = (EditText) findViewById(R.id.txtLivebirthNum2);
            txtLivebirthNum2.setEnabled(false);
            VlblChildList = (TextView) findViewById(R.id.VlblChildList);

            chklivebirth = (CheckBox) findViewById(R.id.chklivebirth);
            chkdeathbirth = (CheckBox) findViewById(R.id.chkdeathbirth);
            chkabortion = (CheckBox) findViewById(R.id.chkabortion);
            //dtpDOPNCCh1 = (EditText) findViewById(R.id.dtpDOPNCCh1);
            //((LinearLayout)findViewById(R.id.secPNCCh12)).setVisibility(View.GONE);
            chklivebirth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        txtLivebirthNum1.setEnabled(true);

                        //txtLivebirthNum1.setText("1");
                        VlblOutcomeDT.setText("প্রসবের তারিখ");
                        chkabortion.setChecked(!b);
                    } else {
                        txtLivebirthNum1.setText("");
                        txtLivebirthNum1.setEnabled(false);
                    }

                }
            });
            chkdeathbirth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        txtLivebirthNum2.setEnabled(true);
                        //txtLivebirthNum2.setText("1");
                        VlblOutcomeDT.setText("প্রসবের তারিখ");
                        chkabortion.setChecked(!b);
                    } else {
                        txtLivebirthNum2.setEnabled(false);
                        txtLivebirthNum2.setText("");
                    }

                }
            });

            chkabortion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {

                        txtLivebirthNum1.setEnabled(false);
                        txtLivebirthNum2.setEnabled(false);
                        VlblOutcomeDT.setText("গর্ভপাতের তারিখ");
                        chklivebirth.setChecked(!b);
                        chkdeathbirth.setChecked(!b);
                        VlblBAtten.setText("কে গর্ভপাত করিয়েছেন");
                        VlblBPlace.setText("কোথায় গর্ভপাত হয়েছে");
                        secBType.setVisibility(View.GONE);
                        rdogrpBType.clearCheck();
                        secMiso.setVisibility(View.GONE);
                        rdogrpMiso.clearCheck();

                    } else {
                        secBType.setVisibility(View.VISIBLE);
                        secMiso.setVisibility(View.VISIBLE);
                        VlblBAtten.setText("কে প্রসব করিয়েছেন");
                        VlblBPlace.setText("কোথায় প্রসব হয়েছে");
                    }
                }
            });

            secOutcomeDT = (LinearLayout) findViewById(R.id.secOutcomeDT);
            VlblOutcomeDT = (TextView) findViewById(R.id.VlblOutcomeDT);
            dtpOutcomeDT = (EditText) findViewById(R.id.dtpOutcomeDT);
            btnOutcomeDT = (ImageButton) findViewById(R.id.btnOutcomeDT);
            btnDOPNCCh1 = (ImageButton) findViewById(R.id.btnDOPNCCh1);
            secBPlace = (LinearLayout) findViewById(R.id.secBPlace);
            VlblBPlace = (TextView) findViewById(R.id.VlblBPlace);
            spnBPlace = (Spinner) findViewById(R.id.spnBPlace);
            secBType = (LinearLayout) findViewById(R.id.secBType);
            VlblBType = (TextView) findViewById(R.id.VlblBType);
            rdogrpBType = (RadioGroup) findViewById(R.id.rdogrpBType);
            rdoBTypeNor = (RadioButton) findViewById(R.id.rdoBTypeNor);
            rdoBTypeSeg = (RadioButton) findViewById(R.id.rdoBTypeSeg);

            secMiso = (LinearLayout) findViewById(R.id.secMiso);
            VlblMiso = (TextView) findViewById(R.id.VlblMiso);
            rdogrpMiso = (RadioGroup) findViewById(R.id.rdogrpMiso);
            rdoMisoYes = (RadioButton) findViewById(R.id.rdoMisoYes);
            rdoMisoNo = (RadioButton) findViewById(R.id.rdoMisoNo);
            List<String> listBPlace = new ArrayList<String>();

            listBPlace.add("");
            listBPlace.add("01-বাড়িতে");
            listBPlace.add("02-উপজেলা স্বাস্থ্য কমপ্লেক্স");
            listBPlace.add("03-ইউনিয়ন স্বাস্থ্য  ও পরিবার কল্যাণ কেন্দ্র");
            listBPlace.add("04-মা ও শিশু কল্যাণ কেন্দ্র");
            listBPlace.add("05-জেলা সদর ও অন্যান্য \n সরকারী হাসপাতাল");
            listBPlace.add("06-এনজিও ক্লিনিক বা হাসপাতাল");
            listBPlace.add("07-প্রাইভেট ক্লিনিক বা হাসপাতাল");
            listBPlace.add("77-অন্যান্য");

            ArrayAdapter<String> BPlace = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listBPlace);
            spnBPlace.setAdapter(BPlace);

            spnBPlace.setEnabled(false);
            spnBPlace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    String val = (spnBPlace.getSelectedItemPosition() == 0 ? "" : Global.Left(spnBPlace.getSelectedItem().toString(), 2));
                    if (val.length() > 0) {
                        if (val.equalsIgnoreCase("01")) {
                            rdoBTypeNor.setChecked(true);
                            rdoBTypeSeg.setEnabled(false);
                            spnBAtten.setAdapter(null);
                            FillSpinner(true);

                        } else {
                            rdogrpBType.clearCheck();
                            rdoBTypeNor.setEnabled(true);
                            rdoBTypeSeg.setEnabled(true);
                            spnBAtten.setAdapter(null);
                            FillSpinner(false);

                            //Select '  'as attendantCode union
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            secBAtten = (LinearLayout) findViewById(R.id.secBAtten);
            VlblBAtten = (TextView) findViewById(R.id.VlblBAtten);
            spnBAtten = (Spinner) findViewById(R.id.spnBAtten);

            //..............FPI.............
            rdogrpbirthdeathabortion1 = (RadioGroup) findViewById(R.id.rdogrpbirthdeathabortion1);
            rdobirthdeathabortion1 = (RadioButton) findViewById(R.id.rdobirthdeathabortion1);
            rdobirthdeathabortion2 = (RadioButton) findViewById(R.id.rdobirthdeathabortion2);

            rdogrpOutcomeDT = (RadioGroup) findViewById(R.id.rdogrpOutcomeDT);
            rdoOutcomeDT1 = (RadioButton) findViewById(R.id.rdoOutcomeDT1);
            rdoOutcomeDT2 = (RadioButton) findViewById(R.id.rdoOutcomeDT2);

            rdogrpBPlace = (RadioGroup) findViewById(R.id.rdogrpBPlace);
            rdoBPlace1 = (RadioButton) findViewById(R.id.rdoBPlace1);
            rdoBPlace2 = (RadioButton) findViewById(R.id.rdoBPlace2);

            rdogrpBAtten = (RadioGroup) findViewById(R.id.rdogrpBAtten);
            rdoBAtten1 = (RadioButton) findViewById(R.id.rdoBAtten1);
            rdoBAtten2 = (RadioButton) findViewById(R.id.rdoBAtten2);

            rdogrpBType1 = (RadioGroup) findViewById(R.id.rdogrpBType1);
            rdoBType11 = (RadioButton) findViewById(R.id.rdoBType11);
            rdoBType12 = (RadioButton) findViewById(R.id.rdoBType12);

            rdogrpMiso1 = (RadioGroup) findViewById(R.id.rdogrpMiso1);
            rdoMiso11 = (RadioButton) findViewById(R.id.rdoMiso11);
            rdoMiso12 = (RadioButton) findViewById(R.id.rdoMiso12);


            // spnBAtten.setEnabled(false);
            /*List<String> listBAtten = new ArrayList<String>();

            listBAtten.add("");
            listBAtten.add("01-ডাক্তার");
            listBAtten.add("02-নার্স");
            listBAtten.add("03-স্যাকমো");
            listBAtten.add("04-এফ ডব্লিউ ভি");
            listBAtten.add("05-প্যারামেডিক্স");
            listBAtten.add("06-সি এস বি এ");
            listBAtten.add("77-অন্যান্য");

            ArrayAdapter<String> BAtten = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listBAtten);
            spnBAtten.setAdapter(BAtten);*/


            cmdSave = (Button) findViewById(R.id.cmdSave);
            //cmdSavePNC = (ImageButton) findViewById(R.id.cmdSavePNC);
            //((LinearLayout)findViewById(R.id.List)).setVisibility(View.GONE);
            /*btnDOPNCCh1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btnDOPNCCh1";
                    showDialog(DATE_DIALOG);
                }
            });
            */

            DeliverySearch(g.getGeneratedId(), pregnancyNo);
            FPIDataSearch(g.getGeneratedId(), pregnancyNo);
            //DisplayChildlist();

            btnOutcomeDT.setEnabled(false);
            btnOutcomeDT.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btnOutcomeDT";
                    showDialog(DATE_DIALOG);
                }
            });
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            cmdSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    DataSave();
                    /*if (txtLivebirthNum1.getText().length() == 0 & txtLivebirthNum2.getText().length() == 0 & chkabortion.isChecked() == false) {
                        Connection.MessageBox(Deliv.this, "প্রসবের ফলাফল  সিলেক্ট করুন।");
                        return;
                    }*/

                  /*  if (txtLivebirthNum1.getText().length() == 0 & txtLivebirthNum2.getText().length() == 0 & chkabortion.isChecked() == false) {
                        Connection.MessageBox(Delivfpi.this, "প্রসবের ফলাফল  কি ।");
                        return;
                    }

                    if(chklivebirth.isChecked() || chkdeathbirth.isChecked() || chkabortion.isChecked())
                    {
                        String NumberofBirth1 = "";
                        String NumberofBirth2 = "";

                        NumberofBirth1 = txtLivebirthNum1.getText().toString();
                        NumberofBirth2 = txtLivebirthNum2.getText().toString();

                        if(NumberofBirth1.length()==0)
                        {
                            NumberofBirth1 = "0";
                        }

                        if(NumberofBirth2.length()==0)
                        {
                            NumberofBirth2 = "0";
                        }

                        if(Integer.parseInt(NumberofBirth1)>2 || Integer.parseInt(NumberofBirth2)>2)
                        {
                            AlertDialog.Builder adb = new AlertDialog.Builder(Delivfpi.this);

                            adb.setTitle("নিশ্চিত করুন");

                            adb.setMessage("আপনি কি শিশুর সংখ্যা নিশ্চিত[হাঁ/না]?");
                            adb.setNegativeButton("No", new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    DataSave();
                                }
                            });
                            adb.show();
                        }
                        else
                        {
                            DataSave();
                        }
                    }*/
                }
            });
            /*
            DisplayPNCVisits();
            cmdSavePNC.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v)
                {
                    DataSavePNC();
                    DisplayPNCVisits();
                }
//test
            });
            */

        } catch (Exception e) {
            Connection.MessageBox(Delivfpi.this, e.getMessage());
            return;
        }
    }

    private void DataSave() {
        try {
            String SQL = "";
            String AG = "";
            // String ServiceId = serviceID(pregnancyNo);
            if (!rdobirthdeathabortion1.isChecked() & !rdobirthdeathabortion2.isChecked() & seclivebirth.isShown()) {
                Connection.MessageBox(Delivfpi.this, "প্রসব ফলাফল যাচাই করুন ।");
                rdobirthdeathabortion1.requestFocus();
                return;
            } else if (!rdoOutcomeDT1.isChecked() & !rdoOutcomeDT2.isChecked() & secOutcomeDT.isShown()) {
                Connection.MessageBox(Delivfpi.this, "প্রসব/গর্ভপাতের তারিখ যাচাই করুন ।");
                rdoOutcomeDT1.requestFocus();
                return;
            } else if (!rdoBPlace1.isChecked() & !rdoBPlace2.isChecked() & secBPlace.isShown()) {
                Connection.MessageBox(Delivfpi.this, "কোথায় প্রসব হয়েছে যাচাই করুন ।");
                rdoBPlace1.requestFocus();
                return;
            } else if (!rdoBAtten1.isChecked() & !rdoBAtten2.isChecked() & secBAtten.isShown()) {
                Connection.MessageBox(Delivfpi.this, "কে প্রসব করিয়েছেন যাচাই করুন ।");
                rdoBAtten1.requestFocus();
                return;
            } else if (!rdoBType11.isChecked() & !rdoBType12.isChecked() & secBAtten.isShown()) {
                Connection.MessageBox(Delivfpi.this, "প্রসবের ধরণ যাচাই করুন ।");
                rdoBType11.requestFocus();
                return;
            } else if (!rdoMiso11.isChecked() & !rdoMiso12.isChecked() & secMiso.isShown()) {
                Connection.MessageBox(Delivfpi.this, "মিসোপ্রোস্টোল বড়ি পেয়েছেন কিনা  যাচাই করুন ।");
                rdoMiso11.requestFocus();
                return;
            }


            if (!C.Existence("Select healthId from " + TableNameFPI + "  Where healthId='" + g.getGeneratedId() + "' AND pregNo ='" + pregnancyNo + "'")) {
                SQL = "Insert into " + TableNameFPI + "(HealthID,pregNo,providerId,systemEntryDate,Upload)Values('" + g.getGeneratedId() + "','" + pregnancyNo + "','" + g.getProvCode() + "','" + Global.DateTimeNowYMDHMS() + "','2')";
                C.Save(SQL);
            }

            SQL = "Update " + TableNameFPI + " Set Upload='2',";

            if (chklivebirth.isChecked() == true) {
                SQL += "liveBirth = '" + (rdobirthdeathabortion1.isChecked() ? "1" : (rdobirthdeathabortion2.isChecked() ? "2" : "")) + "',";
            } else if (chkdeathbirth.isChecked() == true) {
                SQL += "stillBirth = '" + (rdobirthdeathabortion1.isChecked() ? "1" : (rdobirthdeathabortion2.isChecked() ? "2" : "")) + "',";
            } else if (chkabortion.isChecked() == true) {
                SQL += "abortion = '" + (rdobirthdeathabortion1.isChecked() ? "1" : (rdobirthdeathabortion2.isChecked() ? "2" : "")) + "',";
            }
            SQL += "outcomeDate = '" + (rdoOutcomeDT1.isChecked() ? "1" : (rdoOutcomeDT1.isChecked() ? "2" : "")) + "',";
            SQL += "outcomePlace = '" + (rdoBPlace1.isChecked() ? "1" : (rdoBPlace2.isChecked() ? "2" : "")) + "',";
            SQL += "attendantDesignation = '" + (rdoBAtten1.isChecked() ? "1" : (rdoBAtten2.isChecked() ? "2" : "")) + "',";
            SQL += "outcomeType = '" + (rdoBType11.isChecked() ? "1" : (rdoBType11.isChecked() ? "2" : "")) + "',";
            SQL += "misoprostol = '" + (rdoMiso11.isChecked() ? "1" : (rdoMiso12.isChecked() ? "2" : "")) + "'";
            SQL += " Where healthId='" + g.getGeneratedId() + "' AND pregNo ='" + pregnancyNo + "'";
            C.Save(SQL);
            Connection.MessageBox(Delivfpi.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
            //finish();

        } catch (Exception e) {
            Connection.MessageBox(Delivfpi.this, e.getMessage());
            return;
        }
    }

    private void FPIDataSearch(String generatedId, String pregnancyNo) {
        try {
            RadioButton rb;
            String SQL1 = "";
            SQL1 = "Select ifnull(healthId,'') as HealthID,ifnull(liveBirth,'') as liveBirth,ifnull(stillBirth,'') as stillBirth,ifnull(abortion,'') as abortion,ifnull(outcomeDate,'') as outcomeDate,ifnull(outcomePlace,'') as outcomePlace,ifnull(attendantDesignation,'') as attendantDesignation,ifnull(outcomeType,'') as outcomeType,ifnull(misoprostol,'') as misoprostol\n" +
                    "from deliveryFPI FPI Where HealthID='" + generatedId + "' AND pregNo ='" + pregnancyNo + "'";
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

                if (cur1.getString(cur1.getColumnIndex("liveBirth")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdobirthdeathabortion1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("liveBirth")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdobirthdeathabortion2.setChecked(true);
                }
                if (cur1.getString(cur1.getColumnIndex("stillBirth")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdobirthdeathabortion1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("stillBirth")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdobirthdeathabortion2.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("abortion")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdobirthdeathabortion1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("abortion")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdobirthdeathabortion2.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("outcomeDate")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoOutcomeDT1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("outcomeDate")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoOutcomeDT2.setChecked(true);
                }


                if (cur1.getString(cur1.getColumnIndex("outcomePlace")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoBPlace1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("outcomePlace")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoBPlace2.setChecked(true);
                }
                if (cur1.getString(cur1.getColumnIndex("attendantDesignation")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoBAtten1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("attendantDesignation")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoBAtten1.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("outcomeType")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoBType11.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("outcomeType")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoBType11.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("misoprostol")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoMiso11.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("misoprostol")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoMiso12.setChecked(true);
                }


                cur1.moveToNext();
            }
            cur1.close();
        } catch (Exception e) {
            Connection.MessageBox(Delivfpi.this, e.getMessage());
            return;
        }
    }

    private void FillSpinner(boolean isathome) {
        spnBAtten.setAdapter(null);
        if (isathome) {
            spnBAtten.setAdapter(C.getArrayAdapter("Select '  'as attendantCode union Select substr('0' ||attendantCode, -2, 2)||'-'||attendantDesig as attendantCode from AttendantDesignation  order by attendantCode asc"));

        } else {
            spnBAtten.setAdapter(C.getArrayAdapter("Select '  'as attendantCode union Select substr('0' ||attendantCode, -2, 2)||'-'||attendantDesig as attendantCode from AttendantDesignation where substr('0' ||attendantCode, -2, 2)  in('01','02','03','04','05','06','77') order by attendantCode asc"));
        }
    }

    private void setDelivery(String DeliveryType) {

    }

    private void DeliverySearch(String HealthId, String PregNo) {
        try {
            String SQL = "";

            SQL = "Select healthId, pregNo, providerId, outcomePlace, outcomeDate, outcomeType, liveBirth, stillBirth," +
                    " stillBirthFresh, stillBirthMacerated, misoprostol, abortion , systemEntryDate, modifyDate, attendantDesignation " +
                    " FROM delivery where HealthId='" + g.getGeneratedId() + "' AND pregNo = '" + PregNo + "'";

            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {

                txtLivebirthNum1.setText(cur.getString(cur.getColumnIndex("liveBirth")));
                txtLivebirthNum2.setText(cur.getString(cur.getColumnIndex("stillBirth")));


                if (!txtLivebirthNum1.getText().toString().equalsIgnoreCase("")) {
                    chklivebirth.setChecked(true);
                }

                if (!txtLivebirthNum2.getText().toString().equalsIgnoreCase("")) {
                    chkdeathbirth.setChecked(true);
                }


                dtpOutcomeDT.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("outcomeDate"))));
                if (cur.getString(cur.getColumnIndex("outcomePlace")).length() >= 1) {
                    if (cur.getString(cur.getColumnIndex("outcomePlace")).equalsIgnoreCase("01")) {

                        FillSpinner(true);
                        if (cur.getString(cur.getColumnIndex("attendantDesignation")).length() >= 2) {

                            Global.SetSpinnerItem(spnBAtten, cur.getString(cur.getColumnIndex("attendantDesignation")));
                        } else {
                            Global.SetSpinnerItem(spnBAtten, cur.getString(cur.getColumnIndex("attendantDesignation")));
                        }
                    } else {

                        FillSpinner(false);
                    }

                    Global.SetSpinnerItem(spnBPlace, "0" + cur.getString(cur.getColumnIndex("outcomePlace")));
                    if (cur.getString(cur.getColumnIndex("attendantDesignation")).length() >= 2) {

                        Global.SetSpinnerItem(spnBAtten, cur.getString(cur.getColumnIndex("attendantDesignation")));
                    } else {
                        Global.SetSpinnerItem(spnBAtten, cur.getString(cur.getColumnIndex("attendantDesignation")));
                    }
                } else {
                    Global.SetSpinnerItem(spnBPlace, "0" + cur.getString(cur.getColumnIndex("outcomePlace")));
                }
                /*if(cur.getString(cur.getColumnIndex("attendantDesignation")).length()>=2)
                {

                    Global.SetSpinnerItem(spnBAtten, cur.getString(cur.getColumnIndex("attendantDesignation")));
                }
                else
                {
                    Global.SetSpinnerItem(spnBAtten, cur.getString(cur.getColumnIndex("attendantDesignation")));
                }*/
                //Global.SetSpinnerItem(spnBAtten, cur.getString(cur.getColumnIndex("attendantDesignation")));

                if (cur.getString(cur.getColumnIndex("outcomeType")).equals("1")) {
                    rdoBTypeNor.setChecked(true);
                    secBType.setVisibility(View.VISIBLE);
                } else if (cur.getString(cur.getColumnIndex("outcomeType")).equals("2")) {
                    rdoBTypeSeg.setChecked(true);
                    secBType.setVisibility(View.VISIBLE);
                }


                if (cur.getString(cur.getColumnIndex("misoprostol")).equals("1")) {
                    rdoMisoYes.setChecked(true);
                    secMiso.setVisibility(View.VISIBLE);
                } else if (cur.getString(cur.getColumnIndex("misoprostol")).equals("2")) {
                    rdoMisoNo.setChecked(true);
                    secMiso.setVisibility(View.VISIBLE);
                }

                if (cur.getString(cur.getColumnIndex("abortion")).equals("1")) {
                    chkabortion.setChecked(true);
                    secBType.setVisibility(View.GONE);
                    rdogrpBType.clearCheck();
                    secMiso.setVisibility(View.GONE);
                    rdogrpMiso.clearCheck();
                }
                cur.moveToNext();
            }
            cur.close();

        } catch (Exception e) {
            Connection.MessageBox(Delivfpi.this, e.getMessage());
            return;
        }
    }


    private boolean validateDeliveryDate() {
        String sq = String.format("Select LMP from PregWomen where healthId = '%s' and pregNo = '%s'", g.getGeneratedId(), pregnancyNo);
        if (C.Existence(sq)) {
            String LmpDate = C.ReturnSingleValue("Select LMP from PregWomen where healthId = '" + g.getGeneratedId() + "' AND pregNo = '" + pregnancyNo + "'");

            if (Global.DateDifferenceDays(dtpOutcomeDT.getText().toString(), Global.DateConvertDMY(LmpDate)) < 1) {
                return true;
            }
        }
        return false;
    }

    private boolean validateDeliveryDateAgainstSystemDate() {


        if ((Global.DateDifferenceDays(dtpOutcomeDT.getText().toString(), Global.DateNowDMY()) >= 1)) {
            return true;
        }


        return false;

    }

    private boolean validateLMPDateIsMoreThan24Weeks() {
        String sq = String.format("Select LMP from PregWomen where healthId = '%s' and pregNo = '%s'", g.getGeneratedId(), pregnancyNo);
        if (C.Existence(sq)) {
            String LmpDate = C.ReturnSingleValue("Select LMP from PregWomen where healthId = '" + g.getGeneratedId() + "' AND pregNo = '" + pregnancyNo + "'");

            if (Global.DateDifferenceDays(dtpOutcomeDT.getText().toString(), Global.DateConvertDMY(LmpDate)) < 168) {
                return true;
            }
        }
        return false;
    }


    /*private void DataSave() {
        try {
            if (txtLivebirthNum1.getText().length() == 0 & txtLivebirthNum2.getText().length() == 0 & chkabortion.isChecked() == false) {
                Connection.MessageBox(Delivfpi.this, "প্রসবের ফলাফল কি ।");
                return;
            }
            //txtLivebirthNum1

            if (dtpOutcomeDT.getText().length() == 0) {
                Connection.MessageBox(Delivfpi.this, "ফলাফলের তারিখ কত লিখুন।");
                dtpOutcomeDT.requestFocus();
                return;
            } else if (spnBPlace.getSelectedItemPosition() == 0) {
                Connection.MessageBox(Delivfpi.this, "প্রসবের স্থান কোথায় ছিল লিখুন");
                spnBPlace.requestFocus();
                return;
            } else if (spnBAtten.getSelectedItemPosition() == 0) {
                Connection.MessageBox(Delivfpi.this, "কে প্রসব করিয়েছে লিখুন।");
                secBAtten.requestFocus();
                return;
            } else if (!rdoBTypeSeg.isChecked() & !rdoBTypeNor.isChecked() & secBType.isShown()) {
                Connection.MessageBox(Delivfpi.this, "প্রসবের ধরণ কি ছিল লিখুন।");
                rdoBTypeSeg.requestFocus();
                return;
            } else if (!rdoMisoYes.isChecked() & !rdoMisoNo.isChecked() & secMiso.isShown()) {
                Connection.MessageBox(Delivfpi.this, "মিসোপ্রোস্টল বড়ি খেয়েছে কি না  লিখুন");
                rdoMisoYes.requestFocus();
                return;
            }

            if(chklivebirth.isChecked()) {
                if (validateLMPDateIsMoreThan24Weeks()) {
                    Connection.MessageBox(Delivfpi.this, "প্রসব/গর্ভপাত, শেষ মাসিকের তারিখ হতে ২৪ সপ্তাহ এর বেশি  হবে");
                    return;
                }
            }

            if(validateDeliveryDate())
            {
                Connection.MessageBox(Delivfpi.this, "প্রসব/গর্ভপাত, শেষ মাসিকের তারিখ হতে বেশি  হবে");
                return;
            }

            if(validateDeliveryDateAgainstSystemDate())
            {
                Connection.MessageBox(Delivfpi.this, "প্রসব/গর্ভপাত, আজকের তারিখ হতে বেশি  হবে");
                return;
            }
            String SQL = "";
            //String sq = String.format("Select healthId, pregNo from PregWomen where healthId = '%s' and pregNo = '%s'", g.getGeneratedId(),pregnancyNo);
            String sqdel = String.format("Select healthId, pregNo from delivery where healthId = '%s' and pregNo = '%s'", g.getGeneratedId(), pregnancyNo);
            String VisitNo=VisitNumber(g.getGeneratedId());
            String NumberofBirth1 = "";
            String NumberofBirth2 = "";
            String NumberofBirth3 = "";
            NumberofBirth1 = txtLivebirthNum1.getText().toString();
            NumberofBirth2 = txtLivebirthNum2.getText().toString();
            if (chkabortion.isChecked()) {
                NumberofBirth3 = "1";
                if(!C.Existence("Select healthid from elcoVisit  Where healthid='"+ g.getGeneratedId() +"' and Visit='"+ VisitNo +"'"))// and  currStatus = '"+ Global.Left(spnMethod.getSelectedItem().toString(), 2) +"'
                {
                    String sqlnew1="";
                    sqlnew1 = "Insert into elcoVisit(healthId,providerId,visit,systemEntryDate,modifyDate,pregNo)Values(";
                    sqlnew1 += "'" + g.getGeneratedId() + "',";
                    sqlnew1 += "'" + g.getProvCode() + "',";
                    sqlnew1 += "'" + VisitNo + "',";
                    sqlnew1 += "'" + Global.DateTimeNowYMDHMS() + "',";
                    sqlnew1 += "'" + Global.DateTimeNowYMDHMS() + "',";
                    sqlnew1 += "'" + pregnancyNo + "')";
                    C.Save(sqlnew1);
                }
                String sqlupdate1="";
                sqlupdate1 = "Update elcoVisit Set pregNo = '" + pregnancyNo + "' , ";
                sqlupdate1 += "vDate = '" + Global.DateConvertYMD(dtpOutcomeDT.getText().toString()) + "',";//Global.DateTimeNowYMDHMS()
                sqlupdate1 += "visitStatus = '1',";
                sqlupdate1 += "currStatus = '14',";
                sqlupdate1 += "newOld = '',";
                sqlupdate1 += "mDate = '',";
                sqlupdate1 += "sSource = '',";
                sqlupdate1 += "qty = '',";
                sqlupdate1 += "unit = '',";
                sqlupdate1 += "brand = '',";
                sqlupdate1 += "referPlace = '',";
                sqlupdate1 += "validity = '',";
                sqlupdate1 += "dayMonYear = '',";
                sqlupdate1 += "MRDate = '',";
                sqlupdate1 += "syrinsQty = ''";
                sqlupdate1 += " Where HealthID='" + g.getGeneratedId() + "' and Visit='" + VisitNo + "'";
                C.Save(sqlupdate1);
            } else {
                NumberofBirth3 = "0";
            }

            if(!C.Existence(sqdel))
            {
                //Delivery Information
                SQL = "Insert into " + TableName + "(healthId,pregNo,providerId,outcomePlace,outcomeDate,outcomeType,attendantDesignation,liveBirth,stillBirth," +
                        "abortion, systemEntryDate, upload, misoprostol )Values" +
                        "('" + g.getGeneratedId() + "','" + pregnancyNo + "','" + g.getProvCode() + "'" +
                        ",'" + (spnBPlace.getSelectedItemPosition() == 0 ? "" : Integer.parseInt(Global.Left(spnBPlace.getSelectedItem().toString(), 2))) + "'" +
                        ",'" + Global.DateConvertYMD(dtpOutcomeDT.getText().toString()) + "'" +
                        ",'" + ((rdoBTypeNor.isChecked() ? "1" : "2")) + "'" +
                        ",'" + (spnBAtten.getSelectedItemPosition() == 0 ? "" : Global.Left(spnBAtten.getSelectedItem().toString(), 2)) + "'" +
                        ",'" + NumberofBirth1 + "'," +
                        "'" + NumberofBirth2 + "'," +
                        "'" + NumberofBirth3 + "'," +
                        "'" + Global.DateTimeNowYMDHMS() + "','2'," +
                        "'" + ((rdoMisoYes.isChecked() ? "1" : "2")) + "')";
                C.Save(SQL);

                //Child Information
                //((LinearLayout)findViewById(R.id.secChildList)).setVisibility(View.VISIBLE);

                int ChildNo = 0;
                //String ChildHealthId=HealthIdNO();
                //childHealthId
                //,'" + ChildHealthId + "'
                //Live Birth
                if (!NumberofBirth1.equalsIgnoreCase("")) {
                    if (Integer.parseInt(NumberofBirth1) > 0) {
                        for (int i = 0; i < Integer.parseInt(NumberofBirth1); i++) {
                            AddChildToPRS();
                            ChildNo+=1;
                            SQL = "Insert into " + ChildTableName + "(healthId,pregNo,childNo,providerId,systemEntryDate,upload, outcomePlace," +
                                    "outcomeDate," +
                                    "outcomeTime," +
                                    "outcomeType)Values" +
                                    "('" + g.getGeneratedId() + "','" + pregnancyNo + "','" + String.valueOf(ChildNo) + "','" + g.getProvCode() + "'" +
                                    ",'" + Global.DateTimeNowYMDHMS() + "','2','"
                                    + (spnBPlace.getSelectedItemPosition() == 0 ? "" : Integer.parseInt(Global.Left(spnBPlace.getSelectedItem().toString(), 2))) + "','"
                                    + Global.DateConvertYMD(dtpOutcomeDT.getText().toString()) + "','" + Global.DateTimeNowYMDHMS() + "','"  + ((rdoBTypeNor.isChecked() ? "1" : "2")) + "'" +")";
                            C.Save(SQL);
                        }
                        if(!C.Existence("Select healthid from elcoVisit  Where healthid='"+ g.getGeneratedId() +"' and Visit='"+ VisitNo +"'"))// and  currStatus = '"+ Global.Left(spnMethod.getSelectedItem().toString(), 2) +"'
                        {
                            String sqlnew1="";
                            sqlnew1 = "Insert into elcoVisit(healthId,providerId,visit,systemEntryDate,modifyDate, pregNo)Values(";
                            sqlnew1 += "'" + g.getGeneratedId() + "',";
                            sqlnew1 += "'" + g.getProvCode() + "',";
                            sqlnew1 += "'" + VisitNo + "',";
                            sqlnew1 += "'" + Global.DateTimeNowYMDHMS() + "',";
                            sqlnew1 += "'" + Global.DateTimeNowYMDHMS() + "',";
                            sqlnew1 += "'" + pregnancyNo + "')";
                            C.Save(sqlnew1);
                        }
                        String sqlupdate1="";
                        sqlupdate1 = "Update elcoVisit Set pregNo = '" + pregnancyNo + "',";
                        sqlupdate1 += "vDate = '" + Global.DateConvertYMD(dtpOutcomeDT.getText().toString()) + "',";//Global.DateTimeNowYMDHMS()
                        sqlupdate1 += "visitStatus = '1',";
                        sqlupdate1 += "currStatus = '13',";
                        sqlupdate1 += "newOld = '',";
                        sqlupdate1 += "mDate = '',";
                        sqlupdate1 += "sSource = '',";
                        sqlupdate1 += "qty = '',";
                        sqlupdate1 += "unit = '',";
                        sqlupdate1 += "brand = '',";
                        sqlupdate1 += "referPlace = '',";
                        sqlupdate1 += "validity = '',";
                        sqlupdate1 += "dayMonYear = '',";
                        sqlupdate1 += "MRDate = '',";
                        sqlupdate1 += "syrinsQty = ''";
                        sqlupdate1 += " Where HealthID='" + g.getGeneratedId() + "' and Visit='" + VisitNo + "'";
                        C.Save(sqlupdate1);

                    }
                }


                //Still Birth

                if (!NumberofBirth2.equalsIgnoreCase("")) {
                    if (Integer.parseInt(NumberofBirth2) > 0) {
                        for (int i = 0; i < Integer.parseInt(NumberofBirth2); i++) {
                            //AddChildToPRS();

                            //String ChildNo=NewbornChildNo(txtHealthID.getText().toString());
                            SQL = "Insert into " + ChildTableName + "(healthId,pregNo,childNo,providerId,systemEntryDate,upload, outcomePlace," +
                                    "outcomeDate," +
                                    "outcomeTime," +
                                    "outcomeType)Values" +
                                    "('" + g.getGeneratedId() + "','" + pregnancyNo + "','" + String.valueOf(ChildNo + 1) + "','" + g.getProvCode() + "'" + //String.valueOf(i + 1)
                                    ",'" + Global.DateTimeNowYMDHMS() + "','2','"
                                    + (spnBPlace.getSelectedItemPosition() == 0 ? "" : Integer.parseInt(Global.Left(spnBPlace.getSelectedItem().toString(), 2))) + "','"
                                    + Global.DateConvertYMD(dtpOutcomeDT.getText().toString()) + "','" + Global.DateTimeNowYMDHMS() + "','"  + ((rdoBTypeNor.isChecked() ? "1" : "2")) + "'" +")";
                            C.Save(SQL);


                        }
                    }

                }
            }
            else
            {

                SQL = "Update " + TableName + " set upload='2'," +
                        "outcomePlace = '" + (spnBPlace.getSelectedItemPosition() == 0 ? "" : Integer.parseInt(Global.Left(spnBPlace.getSelectedItem().toString(), 2))) + "'," +
                        "outcomeDate = '" + Global.DateConvertYMD(dtpOutcomeDT.getText().toString()) + "'," +
                        "outcomeType = '" + ((rdoBTypeNor.isChecked() ? "1" : "2")) + "'," +
                        "attendantDesignation = '" + (spnBAtten.getSelectedItemPosition() == 0 ? "" : Global.Left(spnBAtten.getSelectedItem().toString(), 2)) + "'," +
                        "liveBirth = '" + NumberofBirth1 + "'," +
                        "stillBirth = '" + NumberofBirth2 + "'," +
                        "abortion = '" + NumberofBirth3 + "'," +
                        "misoprostol = '" + ((rdoMisoYes.isChecked() ? "1" : "2")) + "'," +
                        "modifyDate = '" + Global.DateTimeNowYMDHMS() + "'" +
                        " where healthId='"+ g.getGeneratedId() +"' and PregNo='"+ pregnancyNo +"'";
                C.Save(SQL);

                //need to update child information

            }



            Connection.MessageBox(Delivfpi.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");


        } catch (Exception e) {
            Connection.MessageBox(Delivfpi.this, e.getMessage());
            return;
        }
    }*/

    private String VisitNumber(String HealthID) {
        String SQL = "";
        SQL = "Select (ifnull(max(cast(Visit as int)),0)+1)MaxVisit from ELCOVisit";
        SQL += " where healthid='" + HealthID + "'";

        String VisitNo = Global.Right(("00" + C.ReturnSingleValue(SQL)), 2);

        return VisitNo;
    }


 /*   private void DataSearch(String healthId) {
        try {
            String SQL = "";

            SQL = "Select SNo as SNo, ifnull(HealthID,'') as HealthID, ifnull(NameEng,'') as NameEng,ifnull(Age,'') as Age," +
                    "(select  NameEng  from member where  ProvCode=(select  ProvCode  from member  Where  healthid = '"+healthId +"')" +
                    "and HHNo=(select  HHNo  from member  Where  healthid = '"+healthId +"')" +
                    "and SNo=(select  SPNO1  from member  Where  healthid= '"+healthId +"'))as HusName," +
                    "(select  Age  from member where  " +
                    " ProvCode=(select  ProvCode  from member  Where  healthid='"+healthId +"') " +
                    "and HHNo=(select  HHNo  from member  Where  healthid='"+healthId +"') " +
                    "and SNo=(select  SPNO1  from member  Where  healthid='"+healthId +"'))as HusAge from " +
                    "Member where healthid='" + healthId +"'";

           Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                txtHealthID.setText(cur.getString(cur.getColumnIndex("HealthID")));
                txtSNo.setText(cur.getString(cur.getColumnIndex("SNo")));
                txtName.setText(cur.getString(cur.getColumnIndex("NameEng")));
                txtAge.setText(cur.getString(cur.getColumnIndex("Age")));
                txtHusName.setText(cur.getString(cur.getColumnIndex("HusName")));
                txtAgeYrHus.setText(cur.getString(cur.getColumnIndex("HusAge")));


                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            // Connection.MessageBox(Deliv.this, e.getMessage());
            return;
        }
    }*/

  /*  private void ELCONoSearch(String Dist, String Upz, String UN, String Mouza, String Vill, String HHNo, String SNo) {
        try {
            String SQL = "";
            SQL = "select E.ELCONo as ELCONo from ELCO E Where E.healthId='" + g.getGeneratedId() + "'";
            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                txtElCONo.setText(cur.getString(cur.getColumnIndex("ELCONo")));
                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
             Connection.MessageBox(Deliv.this, e.getMessage());
            return;
        }
    }

    private String PregMaxPGNNo() {


        String SQL = "";
        String pregNo = "";

        SQL = "select ifnull((select '0'||cast(max(pregNo) as string)),0) AS PregNo from PregWomen WHERE healthId=" + g.getGeneratedId();
        pregNo = C.ReturnSingleValue(SQL);
        return pregNo;
    }*/

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

            dtpDate = (EditText) findViewById(R.id.dtpOutcomeDT);

            if (VariableID.equals("btnOutcomeDT")) {
                dtpDate = (EditText) findViewById(R.id.dtpOutcomeDT);
            }
            /*if(VariableID.equals("btnDOPNCCh1"))
            {
                dtpDate = (EditText)findViewById(R.id.dtpDOPNCCh1);
            }*/
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


    private String NewbornPGNNo(String HealthId) {
        String SQL = "";
        SQL = "select (ifnull(cast(PregNo as int),0)) as pregNo from newborn ";
        SQL += " where HealthId='" + g.getGeneratedId() + "'order by systementrydate asc limit 1";

        String PGNNo = Global.Right(("0" + C.ReturnSingleValue(SQL)), 1);

        return PGNNo;
    }

    private String NewbornChildNo(String HealthId) {
        String SQL = "";
        SQL = "select  (ifnull(cast(childNo as int),0))+1 as childNo from newborn ";
        SQL += " where HealthId='" + g.getGeneratedId() + "' and PregNo=(Select (ifnull(max(cast(PregNo as int)),0)) as pregNo  from newborn where HealthId='" + g.getGeneratedId() + "')order by systementrydate asc limit 1";

        String ChildNo = Global.Right(("0" + C.ReturnSingleValue(SQL)), 1);

        return ChildNo;
    }

    private String MaxSNo() {
        String SQL = "";
        String MaxSNo = "";
        SQL += "Select (ifnull(max(cast(SNo as int)),0)+1)MaxSNo from Member ";
        SQL += "where dist='" + g.getDistrict() + "' and upz='" + g.getUpazila() + "' and un='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and vill='" + g.getVillage() + "'  and  HHNo='" + g.getHouseholdNo() + "'";
        MaxSNo = Global.Right(("00" + C.ReturnSingleValue(SQL)), 2);
        return MaxSNo;
    }

    private String GetCountSLNoNumber() {

        String SQL = "select ((cast(Count(*) as int))) as Totalno  from delivery";
        String Val = String.valueOf(C.ReturnSingleValue(SQL));
        if (Val.equalsIgnoreCase("0")) {
            return "1";
        } else
            return Val;
    }

    private String HealthIdNO() {
        String HID = "";
        String HealthID = "";
        if (C.Existence("select HealthID from HealthidRepository where ifnull(status,'1')='1' order by healthid limit 1")) {
            HID = C.ReturnSingleValue("select HealthID from HealthidRepository where ifnull(status,'1')='1' order by healthid limit 1");
        } else {
            Connection.MessageBox(Delivfpi.this, "Health ID এর তালিকা থেকে সকল আইডি ব্যবহার করা হয়ে গেছে, নতুন আইডির জন্য আপনার সুপারভাইজারের সাথে যোগাযোগ করুন।");
            return HID;
        }

        return HID;
    }

    private String GetOutComeDate(String HealthId) {
        String SQL = "";
        String DOB = "";
        SQL += "Select outcomeDate from delivery WHERE healthId = '" + g.getGeneratedId() + "'";// and PregNo=";
        DOB = Global.Right(("00" + C.ReturnSingleValue(SQL)), 10);
        return DOB;
    }

    public void AddChildToPRS() {
        try {
            String SQL = "";
            String AG = "";
            String MaxSNo = MaxSNo();
            String DOB = GetOutComeDate(g.getGeneratedId());
            String HealthId = HealthIdNO();
            //Issue SNo and HealthID
            if (!C.Existence("Select Dist,Upz,UN,Mouza,Vill,HHNo,SNo from " + MemberTable + "  Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and SNo='" + MaxSNo + "'")) {
                SQL = "Insert into " + MemberTable + "(Dist,Upz,UN,Mouza,Vill,ProvType,ProvCode,HHNo,SNo,HealthID,Lat,Lon,StartTime,EnType,EnDate,EndTime,ExType,ExDate,UserId,EnDt,upload)Values('" + g.getDistrict() + "','" + g.getUpazila() + "','" + g.getUnion() + "','" + g.getMouza() + "','" + g.getVillage() + "','" + g.getProvType() + "','" + g.getProvCode() + "','" + g.getHouseholdNo() + "','" + MaxSNo + "','" + HealthId + "','" + Double.toString(currentLatitude) + "','" + Double.toString(currentLongitude) + "','" + StartTime + "','','" + g.DateNowYMD() + "','" + g.CurrentTime24() + "','','','" + g.getUserID() + "','" + Global.DateTimeNowYMDHMS() + "','2')";
                C.Save(SQL);
                C.Save("Update HealthIDRepository Set Status='2' where HealthID='" + HealthId + "'");
            }

            String SQL1 = "";
            SQL1 = "Select Dist, Upz, UN, Mouza, Vill, HHNo, SNo as MotherNo, ifnull(HealthID,'') as HealthID, ifnull(NameEng,'') as NameEng,";
            SQL1 += " ifnull(NameBang,'') as NameBang, ifnull(Rth,'') as Rth, ifnull(HaveNID,'') as HaveNID, ifnull(NID,'') as NID, ifnull(NIDStatus,'') as NIDStatus, ifnull(HaveBR,'') as HaveBR, ifnull(BRID,'') as BRID, ifnull(BRIDStatus,'') as BRIDStatus, ifnull(MobileNo1,'') as MobileNo1,";
            SQL1 += " ifnull(MobileNo2,'') as MobileNo2, ifnull(MobileYN,'')as MobileYN, ifnull(DOB,'') as DOB, ifnull(cast(((julianday(date('now'))-julianday(DOB))/365.25)as int),'') as Age, ifnull(DOBSource,'') as DOBSource, ifnull(BPlace,'') as BPlace, ifnull(FNo,'') as FNo, ifnull(Father,'') as Father, ifnull(FDontKnow,'')as FDontKnow, ifnull(MNo,'') as MNo, ifnull(Mother,'') as Mother,ifnull(MDontKnow,'')as MDontKnow,";
            SQL1 += " ifnull(Sex,'') as Sex, ifnull(MS,'') as MS, ifnull(SPNO1,'') as SPNO1,ifnull(SPNO2,'') as SPNO2,ifnull(SPNO3,'') as SPNO3,ifnull(SPNO4,'') as SPNO4, ifnull(ELCONo,'') as ELCONo, ifnull(ELCODontKnow,'') as ELCODontKnow, ifnull(EDU,'') as EDU, ifnull(Rel,'') as Rel, ifnull(Nationality,'') as Nationality, ifnull(OCP,'') as OCP,";
            SQL1 += " (select  NameEng  from member where  ProvCode=(select  ProvCode  from member  Where  healthid ='" + g.getHealthID() + "')";
            SQL1 += "and HHNo=(select  HHNo  from member  Where  healthid='" + g.getHealthID() + "')";
            SQL1 += "and SNo=(select  SPNO1  from member  Where  healthid='" + g.getHealthID() + "'))as HusName,";
            SQL1 += "(select  SNo  from member where ProvCode=(select  ProvCode  from member  Where  healthid='" + g.getHealthID() + "')";
            SQL1 += "and HHNo=(select  HHNo  from member  Where  healthid='" + g.getHealthID() + "')";
            SQL1 += "and SNo=(select  SPNO1  from member  Where  healthid='" + g.getHealthID() + "'))as FatherNo from Member where healthid='" + g.getHealthID() + "'";

            Cursor cur = C.ReadData(SQL1);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                SQL = "Update " + MemberTable + " Set ";
                String MotherName = cur.getString(cur.getColumnIndex("NameEng"));
                String ChildOf = "Child of ";
                SQL += "NameEng = '" + ChildOf + " " + MotherName + "',";
                SQL += "NameBang = '',"; //convert english name to bangla
                String RelationWithHead = cur.getString(cur.getColumnIndex("Rth"));
                if (RelationWithHead.equalsIgnoreCase("02"))
                    SQL += "RTH = '03',";
                else if (RelationWithHead.equalsIgnoreCase("15") | RelationWithHead.equalsIgnoreCase("12") | RelationWithHead.equalsIgnoreCase("03") | RelationWithHead.equalsIgnoreCase("13") | RelationWithHead.equalsIgnoreCase("16"))
                    SQL += "RTH = '08',";
                else if (RelationWithHead.equalsIgnoreCase("05") | RelationWithHead.equalsIgnoreCase("14"))
                    SQL += "RTH = '16',";
                else if (RelationWithHead.equalsIgnoreCase("04") | RelationWithHead.equalsIgnoreCase("11"))
                    SQL += "RTH = '05',";
                else if (RelationWithHead.equalsIgnoreCase("06"))
                    SQL += "RTH = '05',";
                else if (RelationWithHead.equalsIgnoreCase("10"))
                    SQL += "RTH = '09',";
                else if (RelationWithHead.equalsIgnoreCase("17"))
                    SQL += "RTH = '16',";
                else if (RelationWithHead.equalsIgnoreCase("18"))
                    SQL += "RTH = '77',";
                else
                    SQL += "RTH = '77',";
                SQL += "HaveNID = '2',";
                SQL += "NID = '',";
                SQL += "NIDStatus = '7',";
                SQL += "HaveBR = '2',";
                SQL += "BRID = '',";
                SQL += "BRIDStatus = '7',";
                SQL += "MobileNo1 = '',";
                SQL += "MobileNo2 = '',";
                SQL += "MobileYN = '',";
                SQL += "DOB = '" + DOB + "',"; //Global.DateConvertYMD(dtpDOB.getText().toString())
                SQL += "Age = '" + Global.DateDifferenceYears(Global.DateNowDMY(), Global.DateConvertDMY(DOB.toString())) + "',";
                SQL += "DOBSource = '1',";
                SQL += "BPlace = '" + g.getDistrict() + "',";
                String FatherSNo = cur.getString(cur.getColumnIndex("FatherNo"));
                SQL += "FNo = '" + FatherSNo + "',";
                SQL += "Father = '',";
                SQL += "FDontKnow = '2',";
                String MatherSNo = cur.getString(cur.getColumnIndex("MotherNo"));
                SQL += "MNo = '" + MatherSNo + "',";
                SQL += "Mother = '',";
                SQL += "MDontKnow = '2',";
                SQL += "Sex = '',";
                SQL += "MS = '1',";
                SQL += "SPNo1 = '',";
                SQL += "SPNo2 = '',";
                SQL += "SPNo3 = '',";
                SQL += "SPNo4 = '',";
                SQL += "ELCONo = '',";
                SQL += "ELCODontKnow = '1',";
                SQL += "EDU = '99',";
                String Religious = cur.getString(cur.getColumnIndex("Rel"));
                SQL += "Rel = '" + Religious + "',";
                String Nationality = cur.getString(cur.getColumnIndex("Nationality"));
                SQL += "Nationality = '" + Nationality + "',";
                SQL += "OCP = '88'";
                SQL += " Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and ProvType='" + g.getProvType() + "' and ProvCode='" + g.getProvCode() + "' and HHNo='" + g.getHouseholdNo() + "' and SNo='" + MaxSNo + "'";
                C.Save(SQL);
                cur.moveToNext();
                cur.close();
            }

        } catch (Exception e) {
            Connection.MessageBox(Delivfpi.this, e.getMessage());
            return;
        }
    }


    private void DisplayPNCVisits() {
        GridView gcount = (GridView) findViewById(R.id.gridPNC);
        g.setImuCode(String.valueOf(gcount.getCount() + 1));
        PNCVisits();
    }

    public void PNCVisits() {
        GridView g1 = (GridView) findViewById(R.id.gridPNC);
        g1.setAdapter(new PNC(this));
        g1.setNumColumns(6);
    }

    public class PNC extends BaseAdapter {
        private Context mContext;
        String[][] vcode;
        Integer totalRec;

        public PNC(Context c) {
            mContext = c;
        }

        public int getCount() {
            return Integer.parseInt(C.ReturnSingleValue("Select count(*)total from pncServiceMother where healthid='" + g.getGeneratedId() + "' AND pregNo = '" + g.getPregNo() + "'"));
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
                String SQL = "Select healthId, pregNo,serviceId, visitDate from pncServiceMother where healthid='" + g.getGeneratedId() + "' AND pregNo = '" + g.getPregNo() + "'" + " order by cast(visitDate as DATE) asc";

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

                            DisplaySelectedPNCInfo(ServiceId);
                        }
                    });
                } catch (Exception ex) {
                    Connection.MessageBox(Delivfpi.this, ex.getMessage());
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
            Connection.MessageBox(Delivfpi.this, ex.getMessage());
        }
    }

    private void DataSavePNC() {

        try {
            if (dtpDOPNCCh1.getText().toString().length() == 0 & dtpDOPNCCh1.isShown()) {
                Connection.MessageBox(Delivfpi.this, "পি এন সি  এর তারিখ কত লিখুন।");
                dtpDOPNCCh1.requestFocus();
                return;
            }

            if (validateDeliveryDate()) {
                Connection.MessageBox(Delivfpi.this, "পি এন সি  এর সঠিক তারিখ কত লিখুন।");
                return;
            }

            String SQL = "";

            SQL = "select healthId, pregNo, serviceId, providerId, visitDate, serviceSource, systemEntryDate, upload,modifyDate FROM pncServiceMother ";
            SQL += " WHERE healthId = '" + g.getGeneratedId() + "' AND pregNo ='" + g.getPregNo() + "' and serviceId='" + GetServiceId() + "'  ORDER BY visitDate";

            String SQ = "select visitDate FROM pncServiceMother ";
            SQ += " WHERE healthId = '" + g.getGeneratedId() + "' AND pregNo ='" + g.getPregNo() + "' and visitDate='" + Global.DateConvertYMD(dtpDOPNCCh1.getText().toString()) + "'";


            if (!C.Existence(SQL) && !C.Existence(SQ)) {

                SQL = "Insert into " + TableNamePNC + "(healthId, pregNo, serviceId, providerId, visitDate, serviceSource, systemEntryDate, upload)Values('" +
                        g.getGeneratedId() + "','" + g.getPregNo() + "','" + GetServiceId() + "','" + g.getProvCode() + "','" + Global.DateConvertYMD(dtpDOPNCCh1.getText().toString()) + "','" + "G','" + Global.DateTimeNowYMDHMS() + "','" + "2')";
                C.Save(SQL);
                ((LinearLayout) findViewById(R.id.secChildList)).setVisibility(View.VISIBLE);
                Connection.MessageBox(Delivfpi.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
            } else {
                Connection.MessageBox(Delivfpi.this, "পি এন সি  এর ভিসিট দেয়া আছে");
                return;
            }

        } catch (Exception e) {
            Connection.MessageBox(Delivfpi.this, e.getMessage());
            return;
        }
    }


    private String GetServiceId() {
        String SQL = "";

        SQL = "select '0'||(ifnull(max(cast(serviceId as int)),0))MaxserviceId from pncServiceMother";
        SQL += " WHERE healthId = '" + g.getGeneratedId() + "' AND pregNo ='" + g.getPregNo() + "'";

        String tempserviceID = C.ReturnSingleValue(SQL);


        String serviceID = String.valueOf((Integer.parseInt(tempserviceID) + 1));

        if (serviceID.equalsIgnoreCase("1")) {
            return String.valueOf(g.getGeneratedId() + g.getPregNo() + serviceID);
        } else {
            return String.valueOf(serviceID);
        }
    }

    private void PNCVisitSearch(String serviceId) {
        try {
            String SQL = "";
            SQL = "select healthId, pregNo, serviceId, providerId, visitDate, serviceSource, systemEntryDate, upload,modifyDate FROM pncServiceMother ORDER BY visitDate";
            SQL += " WHERE healthId = '" + g.getGeneratedId() + "' AND pregNo ='" + g.getPregNo() + "'  and serviceId='" + serviceId + "'";
            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                if (!cur.getString(cur.getColumnIndex("visitDate")).equals("null")) {
                    dtpDOPNCCh1.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("visitDate"))));
                }

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(Delivfpi.this, e.getMessage());
            return;
        }
    }


}