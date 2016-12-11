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
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
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

public class MemberForm extends Activity {
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
        AlertDialog.Builder adb = new AlertDialog.Builder(MemberForm.this);
        switch (item.getItemId()) {
            case R.id.menuClose:
                adb.setTitle("Close");
                adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান[হাঁ/না]?");
                adb.setNegativeButton("না", null);
                adb.setPositiveButton("হাঁ", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        if (g.getCallFrom().equals("newh") | g.getCallFrom().equalsIgnoreCase("h")) {
                            Intent f1 = new Intent(getApplicationContext(), HouseholdIndex.class);
                            startActivity(f1);
                        } else if (g.getCallFrom().equalsIgnoreCase("m") | g.getCallFrom().equalsIgnoreCase("mu")) {
                            Intent f2 = new Intent(getApplicationContext(), MemberList.class);
                            startActivity(f2);
                        }

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
    CheckBox chkIDHave;

    Connection C;
    Global g;
    SimpleAdapter dataAdapter;
    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    private String TableName;

    EditText txtHealthID;
    LinearLayout seclblH;
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
    LinearLayout secHHNo;
    TextView VlblHHNo;
    EditText txtHHNo;
    LinearLayout secSNo;
    TextView VlblSNo;
    TextView txtSNo;
    LinearLayout secNameEng;
    TextView VlblNameEng;
    EditText txtNameEng;
    LinearLayout secNameBang;
    TextView VlblNameBang;
    EditText txtNameBang;
    Spinner spnRth;

    LinearLayout secHaveNID;
    TextView VlblHaveNID;
    RadioGroup rdogrpHaveNID;
    RadioButton rdoHaveNID1;
    RadioButton rdoHaveNID2;
    CheckBox chkNIDDontKnow;

    LinearLayout secNID;
    TextView VlblNID;
    EditText txtNID;
    LinearLayout secNIDStatus;
    TextView VlblNIDStatus;
    Spinner spnNIDStatus;
    CheckBox chkBRIDDontKnow;

    LinearLayout secHaveBR;
    TextView VlblHaveBR;

    RadioGroup rdogrpHaveBR;
    RadioButton rdoHaveBR1;
    RadioButton rdoHaveBR2;


    LinearLayout secBRID;
    TextView VlblBRID;
    EditText txtBRID;
    LinearLayout secBRIDStatus;
    TextView VlblBRIDStatus;
    Spinner spnBRIDStatus;
    LinearLayout secMobileNo1;
    TextView VlblMobileNo1;
    EditText txtMobileNo1;
    LinearLayout secMobileNo2;
    TextView VlblMobileNo2;
    EditText txtMobileNo2;
    CheckBox chkMobileNo;
    LinearLayout secDOB;
    TextView VlblDOB;
    EditText dtpDOB;
    ImageButton btnDOB;
    LinearLayout secAge;
    TextView VlblAge;
    EditText txtAge;
    LinearLayout secDOBSource;
    TextView VlblDOBSource;
    RadioGroup rdogrpDOBSource;

    RadioButton rdoDOBSource1;
    RadioButton rdoDOBSource2;
    LinearLayout secBPlace;
    TextView VlblBPlace;
    Spinner spnBPlace;
    LinearLayout secFNo;
    TextView VlblFNo;
    Spinner spnFNo;
    LinearLayout secFather;
    TextView VlblFather;
    EditText txtFather;
    CheckBox chkFatherDontKnow;
    LinearLayout secMNo;
    TextView VlblMNo;
    Spinner spnMNo;
    LinearLayout secMother;
    TextView VlblMother;
    EditText txtMother;
    CheckBox chkMotherDontKnow;
    LinearLayout secSex;
    TextView VlblSex;
    RadioGroup rdogrpSex;
    RadioButton rdoSex1;
    RadioButton rdoSex2;
    RadioButton rdoSex3;
    LinearLayout secMS;
    TextView VlblMS;
    Spinner spnMS;

    LinearLayout secSPNo;
    TextView VlblSPNo;
    Spinner spnSPNo;

    LinearLayout secSPNo1;
    TextView VlblSPNo1;
    Spinner spnSPNo1;

    LinearLayout secSPNo2;
    TextView VlblSPNo2;
    Spinner spnSPNo2;

    LinearLayout secSPNo3;
    TextView VlblSPNo3;
    Spinner spnSPNo3;

    LinearLayout secELCONo;
    TextView VlblELCONo;
    EditText txtELCONo;
    TextView VlblELCODontKnow;
    CheckBox chkELCODontKnow;
    LinearLayout secEDU;
    TextView VlblEDU;
    Spinner spnEDU;
    LinearLayout secRel;
    TextView VlblRel;
    Spinner spnRel;
    LinearLayout secNationality;
    TextView VlblNationality;
    CheckBox chkNationality;
    LinearLayout secOCP;
    TextView VlblOCP;
    Spinner spnOCP;

    String StartTime;
    String PType;
    String PCode;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.memberform);
            C = new Connection(this);
            g = Global.getInstance();


            FindLocation();

            StartTime = g.CurrentTime24();

            TableName = "Member";

            txtHealthID = (EditText) findViewById(R.id.txtHealthID);
            String HID = "";
            if (g.getCallFrom().equals("m")) {
                if (C.Existence("select HealthID from HealthidRepository where ifnull(status,'1')='1' order by healthid limit 1")) {
                    HID = C.ReturnSingleValue("select HealthID from HealthidRepository where ifnull(status,'1')='1' order by healthid limit 1");
                } else {
                    Connection.MessageBox(MemberForm.this, "Health ID এর তালিকা থেকে সকল আইডি ব্যবহার করা হয়ে গেছে, নতুন আইডির জন্য আপনার সুপারভাইজারের সাথে যোগাযোগ করুন।");
                    return;
                }
            }

            txtHealthID.setText(HID);
            txtHealthID.setEnabled(false);

            chkIDHave = (CheckBox) findViewById(R.id.chkIDHave);
            chkIDHave.setChecked(false);
            chkIDHave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String HID = "";
                    if (((CheckBox) v).isChecked()) {
                        txtHealthID.setText("");
                        txtHealthID.setEnabled(true);
                        txtHealthID.requestFocus();
                    } else {
                        if (C.Existence("select HealthID from HealthidRepository where ifnull(status,'1')='1' order by healthid limit 1")) {
                            HID = C.ReturnSingleValue("select HealthID from HealthidRepository where ifnull(status,'1')='1' order by healthid limit 1");
                        } else {
                            Connection.MessageBox(MemberForm.this, "Health ID এর তালিকা থেকে সকল আইডি ব্যবহার করা হয়ে গেছে, নতুন আইডির জন্য আপনার সুপারভাইজারের সাথে যোগাযোগ করুন।");
                            return;
                        }
                        txtHealthID.setText(HID);
                        txtHealthID.setEnabled(false);
                        txtNameEng.requestFocus();

                    }

                }
            });

            seclblH = (LinearLayout) findViewById(R.id.seclblH);
            VlblSNo = (TextView) findViewById(R.id.VlblSNo);
            txtSNo = (TextView) findViewById(R.id.txtSNo);

            if (g.getSerialNo().toString().length() == 0)
                txtSNo.setText(SerialNumber());
            else
                txtSNo.setText(g.getSerialNo().toString());

            secNameEng = (LinearLayout) findViewById(R.id.secNameEng);
            VlblNameEng = (TextView) findViewById(R.id.VlblNameEng);
            txtNameEng = (EditText) findViewById(R.id.txtNameEng);
            secNameBang = (LinearLayout) findViewById(R.id.secNameBang);
            VlblNameBang = (TextView) findViewById(R.id.VlblNameBang);
            txtNameBang = (EditText) findViewById(R.id.txtNameBang);

            spnRth = (Spinner) findViewById(R.id.spnRth);
            List<String> listRth = new ArrayList<String>();

            listRth.add("");
            listRth.add("00-খানা প্রধানের সাথে সম্পর্ক নাই");
            listRth.add("01-নিজেই খানা প্রধান");
            listRth.add("02-খানা প্রধানের স্বামী/স্ত্রী");
            listRth.add("03-খানা প্রধানের ছেলে /মেয়ে");
            listRth.add("04-খানা প্রধানের বাবা /মা");
            listRth.add("05-খানা প্রধানের ভাই /বোন");
            listRth.add("06-খানা প্রধানের চাচা/ফুফু/মামা/খালা");
            listRth.add("07-খানা প্রধানের দাদা/দাদি/নানা/নানি");
            listRth.add("08-খানা প্রধানের নাতি/নাতনী");
            listRth.add("09-খানা প্রধানের শ্যালক/শ্যালিকা/দেবর/ননদ");
            listRth.add("10-খানা প্রধানের শ্বশুর/শাশুড়ি");
            listRth.add("11-খানা প্রধানের সৎ বাবা /মা");
            listRth.add("12-খানা প্রধানের সৎ ছেলে /মেয়ে");
            listRth.add("13-খানা প্রধানের পালিত ছেলে /মেয়ে");
            listRth.add("14-খানা প্রধানের সৎ ভাই/বোন");
            listRth.add("15-খানা প্রধানের ছেলের বউ/মেয়ের জামাই");
            listRth.add("16-খানা প্রধানের ভাতিজা/ভাতিজী/ভাগ্নে/ভাগ্নি");
            listRth.add("17-খানা প্রধানের বোন জামাই/ভাবি");
            listRth.add("18-চাকর/চাকরানী");
            listRth.add("77-অন্য সম্পর্ক যা উপরের তালিকায় নাই");
            listRth.add("99-জানিনা");
            ArrayAdapter<String> adptrRth = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listRth);
            spnRth.setAdapter(adptrRth);

            secHaveNID = (LinearLayout) findViewById(R.id.secHaveNID);
            VlblHaveNID = (TextView) findViewById(R.id.VlblHaveNID);
            rdogrpHaveNID = (RadioGroup) findViewById(R.id.rdogrpHaveNID);
            rdoHaveNID1 = (RadioButton) findViewById(R.id.rdoHaveNID1);
            rdoHaveNID2 = (RadioButton) findViewById(R.id.rdoHaveNID2);
        /*rdogrpHaveNID.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup arg0, int id) {
                if(id == R.id.rdoHaveNID1)
                {
                    secNID.setVisibility(View.VISIBLE);
                    txtNID.requestFocus();
                    VlblNIDStatus.setText("পরিচয় পত্র নম্বর কেন নেই");
                    ReasonNID(true);
                    secNIDStatus.setVisibility(View.VISIBLE);
                    //spnNIDStatus.setSelection(0);
                }
                else if(id == R.id.rdoHaveNID2)
                {
                    secNID.setVisibility(View.GONE);
                    txtNID.setText("");
                    VlblNIDStatus.setText("পরিচয় পত্র না থাকলে কেন নেই");
                    ReasonNID(false);
                    secNIDStatus.setVisibility(View.VISIBLE);
                    //spnNIDStatus.requestFocus();
                }
            }});
        */

            secNID = (LinearLayout) findViewById(R.id.secNID);
            VlblNID = (TextView) findViewById(R.id.VlblNID);
            txtNID = (EditText) findViewById(R.id.txtNID);
            chkNIDDontKnow = (CheckBox) findViewById(R.id.chkNIDDontKnow);
            chkNIDDontKnow.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (((CheckBox) v).isChecked()) {
                        txtNID.setText("");
                        secNIDStatus.setVisibility(View.VISIBLE);
                    } else {
                        secNIDStatus.setVisibility(View.GONE);
                        spnNIDStatus.setSelection(0);
                    }

                }
            });

            secNIDStatus = (LinearLayout) findViewById(R.id.secNIDStatus);
            VlblNIDStatus = (TextView) findViewById(R.id.VlblNIDStatus);
            spnNIDStatus = (Spinner) findViewById(R.id.spnNIDStatus);
            List<String> listNIDStatus = new ArrayList<String>();
            listNIDStatus.add("");
            listNIDStatus.add("1-কখনো ছিল না");
            listNIDStatus.add("2-হারিয়ে ফেলেছি");
            listNIDStatus.add("3-খুঁজে পাচ্ছি না");
            listNIDStatus.add("4-অন্য জায়গায় আছে");
            listNIDStatus.add("7-নাগরিক নয়");
            ArrayAdapter<String> adptrNIDStatus = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listNIDStatus);
            spnNIDStatus.setAdapter(adptrNIDStatus);
        /*spnNIDStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (spnNIDStatus.getSelectedItemPosition() > 0) {
                    txtNID.setText("");
                } else {
                    txtNID.requestFocus();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });*/

            txtNID.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (txtNID.getText().length() > 0) {
                        chkNIDDontKnow.setChecked(false);
                        spnNIDStatus.setSelection(0);
                        secNIDStatus.setVisibility(View.GONE);
                    } else {
                        chkNIDDontKnow.setChecked(true);
                        secNIDStatus.setVisibility(View.VISIBLE);
                    }
                }
            });

            secHaveBR = (LinearLayout) findViewById(R.id.secHaveBR);
            VlblHaveBR = (TextView) findViewById(R.id.VlblHaveBR);
            rdogrpHaveBR = (RadioGroup) findViewById(R.id.rdogrpHaveBR);
            rdoHaveBR1 = (RadioButton) findViewById(R.id.rdoHaveBR1);
            rdoHaveBR2 = (RadioButton) findViewById(R.id.rdoHaveBR2);

            secBRID = (LinearLayout) findViewById(R.id.secBRID);
            VlblBRID = (TextView) findViewById(R.id.VlblBRID);
            txtBRID = (EditText) findViewById(R.id.txtBRID);
            chkBRIDDontKnow = (CheckBox) findViewById(R.id.chkBRIDDontKnow);
            chkBRIDDontKnow.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (((CheckBox) v).isChecked()) {
                        txtBRID.setText("");
                        secBRIDStatus.setVisibility(View.VISIBLE);
                    } else {
                        secBRIDStatus.setVisibility(View.GONE);
                        spnBRIDStatus.setSelection(0);
                    }

                }
            });

            secBRIDStatus = (LinearLayout) findViewById(R.id.secBRIDStatus);
            VlblBRIDStatus = (TextView) findViewById(R.id.VlblBRIDStatus);
            spnBRIDStatus = (Spinner) findViewById(R.id.spnBRIDStatus);
            List<String> listBRIDStatus = new ArrayList<String>();

            listBRIDStatus.add("");
            listBRIDStatus.add("1-কখনো ছিল না");
            listBRIDStatus.add("2-হারিয়ে ফেলেছি");
            listBRIDStatus.add("3-খুঁজে পাচ্ছি না");
            listBRIDStatus.add("4-অন্য জায়গায় আছে");
            listBRIDStatus.add("7-নাগরিক নয়");
            ArrayAdapter<String> adptrBRIDStatus = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listBRIDStatus);
            spnBRIDStatus.setAdapter(adptrBRIDStatus);

        /*spnBRIDStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(spnBRIDStatus.getSelectedItemPosition()>0)
                {
                    txtBRID.setText("");
                }
                else
                {
                    txtBRID.requestFocus();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });*/

            txtBRID.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (txtBRID.getText().length() > 0) {
                        chkBRIDDontKnow.setChecked(false);
                        spnBRIDStatus.setSelection(0);
                        secBRIDStatus.setVisibility(View.GONE);
                    } else {
                        chkBRIDDontKnow.setChecked(true);
                        secBRIDStatus.setVisibility(View.VISIBLE);
                    }
                }
            });


        /*rdogrpHaveBR.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup arg0, int id) {
                if(id == R.id.rdoHaveBR1)
                {
                    secBRID.setVisibility(View.VISIBLE);
                    txtBRID.requestFocus();
                    VlblBRIDStatus.setText("জন্ম নিবন্ধন নম্বার কেন নেই");
                    ReasonBRID(true);
                    secBRIDStatus.setVisibility(View.VISIBLE);
                    //spnBRIDStatus.setSelection(0);
                }
                else if(id == R.id.rdoHaveBR2)
                {
                    secBRID.setVisibility(View.GONE);
                    txtBRID.setText("");
                    VlblBRIDStatus.setText("জন্ম নিবন্ধন নম্বার না থাকলে কেন নেই");
                    ReasonBRID(false);
                    secBRIDStatus.setVisibility(View.VISIBLE);
                    //spnBRIDStatus.requestFocus();
                }
            }});
        */


            secMobileNo1 = (LinearLayout) findViewById(R.id.secMobileNo1);
            VlblMobileNo1 = (TextView) findViewById(R.id.VlblMobileNo1);
            txtMobileNo1 = (EditText) findViewById(R.id.txtMobileNo1);
            secMobileNo2 = (LinearLayout) findViewById(R.id.secMobileNo2);
            VlblMobileNo2 = (TextView) findViewById(R.id.VlblMobileNo2);
            txtMobileNo2 = (EditText) findViewById(R.id.txtMobileNo2);
            chkMobileNo = (CheckBox) findViewById(R.id.chkMobileNo);

            txtMobileNo1.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (txtMobileNo1.getText().length() > 0) {
                        chkMobileNo.setChecked(false);
                    } else {
                        //chkMobileNo.setChecked(true);
                    }
                }
            });
            txtMobileNo2.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (txtMobileNo2.getText().length() > 0) {
                        chkMobileNo.setChecked(false);
                    } else {
                        //chkMobileNo.setChecked(true);
                    }
                }
            });

            chkMobileNo.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (((CheckBox) v).isChecked()) {
                        txtMobileNo1.setText("");
                        txtMobileNo2.setText("");
                    }

                }
            });


            secDOB = (LinearLayout) findViewById(R.id.secDOB);
            VlblDOB = (TextView) findViewById(R.id.VlblDOB);
            dtpDOB = (EditText) findViewById(R.id.dtpDOB);
            secAge = (LinearLayout) findViewById(R.id.secAge);
            VlblAge = (TextView) findViewById(R.id.VlblAge);
            txtAge = (EditText) findViewById(R.id.txtAge);
            secDOBSource = (LinearLayout) findViewById(R.id.secDOBSource);
            VlblDOBSource = (TextView) findViewById(R.id.VlblDOBSource);
            rdogrpDOBSource = (RadioGroup) findViewById(R.id.rdogrpDOBSource);
            rdoDOBSource1 = (RadioButton) findViewById(R.id.rdoDOBSource1);
            rdoDOBSource2 = (RadioButton) findViewById(R.id.rdoDOBSource2);
            rdogrpDOBSource.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup arg0, int id) {
                    if (id == R.id.rdoDOBSource1) {
                        secDOB.setVisibility(View.VISIBLE);
                        secAge.setVisibility(View.GONE);
                        txtAge.setText("");
                    } else if (id == R.id.rdoDOBSource2) {
                        dtpDOB.setText("");
                        secDOB.setVisibility(View.GONE);
                        secAge.setVisibility(View.VISIBLE);
                        txtAge.requestFocus();
                    }
                }
            });


            secBPlace = (LinearLayout) findViewById(R.id.secBPlace);
            VlblBPlace = (TextView) findViewById(R.id.VlblBPlace);
            spnBPlace = (Spinner) findViewById(R.id.spnBPlace);

            spnBPlace.setAdapter(C.getArrayAdapter("Select '  ' union select zillaid||'-'||zillaname from zilla union Select '99-Abroad'"));
            spnBPlace.setSelection(Global.SpinnerItemPosition(spnBPlace, 2, g.getDistrict()));

            secFNo = (LinearLayout) findViewById(R.id.secFNo);
            VlblFNo = (TextView) findViewById(R.id.VlblFNo);
            spnFNo = (Spinner) findViewById(R.id.spnFNo);
            List<String> listFNo = new ArrayList<String>();

            listFNo.add("");
            listFNo.add("77-এখানে থাকে না");
            ArrayAdapter<String> adptrFNo = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listFNo);
            spnFNo.setAdapter(adptrFNo);

            secFather = (LinearLayout) findViewById(R.id.secFather);
            VlblFather = (TextView) findViewById(R.id.VlblFather);
            txtFather = (EditText) findViewById(R.id.txtFather);
            chkFatherDontKnow = (CheckBox) findViewById(R.id.chkFatherDontKnow);
            chkFatherDontKnow.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (((CheckBox) v).isChecked()) {
                        txtFather.setText("");
                    }
                }
            });
            txtFather.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (txtFather.getText().length() > 0) {
                        chkFatherDontKnow.setChecked(false);
                    }
                }
            });


            spnFNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    if (spnFNo.getSelectedItem().toString().length() == 0) {
                        secFather.setVisibility(View.GONE);
                        return;
                    }
                    String spnData = Global.Left(spnFNo.getSelectedItem().toString(), 2);
                    if (spnData.equalsIgnoreCase("55") || spnData.equalsIgnoreCase("77")) {
                        secFather.setVisibility(View.VISIBLE);
                    } else {
                        secFather.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });

            secMNo = (LinearLayout) findViewById(R.id.secMNo);
            VlblMNo = (TextView) findViewById(R.id.VlblMNo);
            spnMNo = (Spinner) findViewById(R.id.spnMNo);
            List<String> listMNo = new ArrayList<String>();

            listMNo.add("");
            listMNo.add("77-এখানে থাকে না");
            ArrayAdapter<String> adptrMNo = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listMNo);
            spnMNo.setAdapter(adptrMNo);


            secMother = (LinearLayout) findViewById(R.id.secMother);
            VlblMother = (TextView) findViewById(R.id.VlblMother);
            txtMother = (EditText) findViewById(R.id.txtMother);
            chkMotherDontKnow = (CheckBox) findViewById(R.id.chkMotherDontKnow);
            chkMotherDontKnow.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (((CheckBox) v).isChecked()) {
                        txtMother.setText("");
                    }
                }
            });
            txtMother.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (txtMother.getText().length() > 0) {
                        chkMotherDontKnow.setChecked(false);
                    }
                }
            });

            spnMNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    if (spnMNo.getSelectedItem().toString().length() == 0) {
                        secMother.setVisibility(View.GONE);
                        return;
                    }
                    String spnData = Global.Left(spnMNo.getSelectedItem().toString(), 2);
                    if (spnData.equalsIgnoreCase("55") || spnData.equalsIgnoreCase("77")) {
                        secMother.setVisibility(View.VISIBLE);
                    } else {
                        secMother.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });
            secSex = (LinearLayout) findViewById(R.id.secSex);
            VlblSex = (TextView) findViewById(R.id.VlblSex);
            rdogrpSex = (RadioGroup) findViewById(R.id.rdogrpSex);

            rdoSex1 = (RadioButton) findViewById(R.id.rdoSex1);
            rdoSex2 = (RadioButton) findViewById(R.id.rdoSex2);
            rdoSex3 = (RadioButton) findViewById(R.id.rdoSex3);
            secMS = (LinearLayout) findViewById(R.id.secMS);
            VlblMS = (TextView) findViewById(R.id.VlblMS);
            spnMS = (Spinner) findViewById(R.id.spnMS);
            List<String> listMS = new ArrayList<String>();


            listMS.add("");
            listMS.add("1-অবিবাহিত");
            listMS.add("2-বিবাহিত");
            listMS.add("3-বিধবা/বিপত্নীক");
            listMS.add("4-স্বামী/স্ত্রী পৃথক");
            listMS.add("5-তালাক প্রাপ্ত/ বিবাহ বিচ্ছিন্ন");
            ArrayAdapter<String> adptrMS = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listMS);
            spnMS.setAdapter(adptrMS);

            secSPNo = (LinearLayout) findViewById(R.id.secSPNo);
            VlblSPNo = (TextView) findViewById(R.id.VlblSPNo);
            spnSPNo = (Spinner) findViewById(R.id.spnSPNo);

            secSPNo1 = (LinearLayout) findViewById(R.id.secSPNo1);
            VlblSPNo1 = (TextView) findViewById(R.id.VlblSPNo1);
            spnSPNo1 = (Spinner) findViewById(R.id.spnSPNo1);

            secSPNo2 = (LinearLayout) findViewById(R.id.secSPNo2);
            VlblSPNo2 = (TextView) findViewById(R.id.VlblSPNo2);
            spnSPNo2 = (Spinner) findViewById(R.id.spnSPNo2);

            secSPNo3 = (LinearLayout) findViewById(R.id.secSPNo3);
            VlblSPNo3 = (TextView) findViewById(R.id.VlblSPNo3);
            spnSPNo3 = (Spinner) findViewById(R.id.spnSPNo3);

            secELCONo = (LinearLayout) findViewById(R.id.secELCONo);
            VlblELCONo = (TextView) findViewById(R.id.VlblELCONo);
            txtELCONo = (EditText) findViewById(R.id.txtELCONo);
            VlblELCODontKnow = (TextView) findViewById(R.id.VlblELCODontKnow);
            chkELCODontKnow = (CheckBox) findViewById(R.id.chkELCODontKnow);
            chkELCODontKnow.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (((CheckBox) v).isChecked()) {
                        txtELCONo.setText("");
                        txtELCONo.setEnabled(false);
                    } else {
                        txtELCONo.setEnabled(true);
                    }
                }
            });

            spnMS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    if (spnMS.getSelectedItem().toString().length() == 0) {
                        secSPNo.setVisibility(View.GONE);
                        spnSPNo.setSelection(0);
                        secSPNo1.setVisibility(View.GONE);
                        spnSPNo1.setSelection(0);
                        secSPNo2.setVisibility(View.GONE);
                        spnSPNo2.setSelection(0);
                        secSPNo3.setVisibility(View.GONE);
                        spnSPNo3.setSelection(0);

                        //secELCONo.setVisibility( View.GONE );
                        //txtELCONo.setText("");
                        //chkELCODontKnow.setChecked( false );
                        return;
                    }

                    String Sex = (rdoSex1.isChecked() ? "1" : (rdoSex2.isChecked() ? "2" : "3"));
                    String spnData = Global.Left(spnMS.getSelectedItem().toString(), 1);
                    ImageButton btnSPNo1 = (ImageButton) findViewById(R.id.btnSPNo1);
                    if (spnData.equalsIgnoreCase("2") & Sex.equals("1")) {
                        secSPNo.setVisibility(View.VISIBLE);
                        btnSPNo1.setVisibility(View.VISIBLE);
                        secSPNo1.setVisibility(View.GONE);
                        secSPNo2.setVisibility(View.GONE);
                        secSPNo3.setVisibility(View.GONE);
                        //secELCONo.setVisibility( View.VISIBLE );
                    } else if (spnData.equalsIgnoreCase("2") & Sex.equals("2")) {
                        secSPNo.setVisibility(View.VISIBLE);
                        btnSPNo1.setVisibility(View.GONE);
                        secSPNo1.setVisibility(View.GONE);
                        secSPNo2.setVisibility(View.GONE);
                        secSPNo3.setVisibility(View.GONE);
                        //secELCONo.setVisibility( View.VISIBLE );
                    } else if (spnData.equalsIgnoreCase("2")) {
                        secSPNo.setVisibility(View.VISIBLE);
                        btnSPNo1.setVisibility(View.GONE);
                        secSPNo1.setVisibility(View.GONE);
                        secSPNo2.setVisibility(View.GONE);
                        secSPNo3.setVisibility(View.GONE);
                        //secELCONo.setVisibility( View.VISIBLE );
                    } else {
                        spnSPNo.setSelection(0);
                        secSPNo.setVisibility(View.GONE);
                        spnSPNo1.setSelection(0);
                        secSPNo1.setVisibility(View.GONE);
                        spnSPNo2.setSelection(0);
                        secSPNo2.setVisibility(View.GONE);
                        spnSPNo3.setSelection(0);
                        secSPNo3.setVisibility(View.GONE);
                        //txtELCONo.setText("");
                        //secELCONo.setVisibility( View.GONE );
                    }

                    String SQL = "";
                    SQL = "Select ifnull(FNo,'')||','||ifnull(MNo,'')||','||ifnull(spno1,'')||','||ifnull(spno2,'')||','||ifnull(spno3,'')||','||ifnull(spno4,'') as SNo";
                /*SQL += " from Member Where Dist='"+ g.getDistrict() +"' and Upz='"+ g.getUpazila() +"' and UN='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and Vill='"+ g.getVillage() +"' and ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"' and HHNo='"+ g.getHouseholdNo() +"' and SNo='"+ txtSNo.getText().toString() +"'";*/
                    SQL += " from Member Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and SNo='" + txtSNo.getText().toString() + "'";
                    String FM = C.ReturnSingleValue(SQL).toString();

                    if (FM.length() > 0) {
                        String[] FMNo = Connection.split(FM.toString(), ',');

                        if (FMNo[2].toString().replace("null", "").length() > 0) {
                            spnSPNo.setSelection(SpNoNoPosition(FMNo[2].replace("-", ""), 1));
                            secSPNo.setVisibility(View.VISIBLE);
                        }
                        if (FMNo[3].toString().replace("null", "").length() > 0) {
                            spnSPNo1.setSelection(SpNoNoPosition(FMNo[3].replace("-", ""), 2));
                            secSPNo1.setVisibility(View.VISIBLE);
                        }
                        if (FMNo[4].toString().replace("null", "").length() > 0) {
                            secSPNo2.setVisibility(View.VISIBLE);
                            spnSPNo2.setSelection(SpNoNoPosition(FMNo[4].replace("-", ""), 3));
                        }
                        if (FMNo[4].toString().replace("null", "").length() > 0) {
                            secSPNo3.setVisibility(View.VISIBLE);
                            spnSPNo3.setSelection(SpNoNoPosition(FMNo[5].replace("-", ""), 4));
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });


            secEDU = (LinearLayout) findViewById(R.id.secEDU);
            VlblEDU = (TextView) findViewById(R.id.VlblEDU);
            spnEDU = (Spinner) findViewById(R.id.spnEDU);
            List<String> listEDU = new ArrayList<String>();

            listEDU.add("");
            listEDU.add("00-স্কুলে যায়নি বা প্রথম শ্রেণী পাশ করেনি");
            listEDU.add("01-১ম শ্রেনী");
            listEDU.add("02-২য় শ্রেনী");
            listEDU.add("03-৩য় শ্রেনী");
            listEDU.add("04-৪র্থ শ্রেনী");
            listEDU.add("05-৫ম শ্রেনী");
            listEDU.add("06-৬ষ্ঠ শ্রেনী");
            listEDU.add("07-৭ম শ্রেনী");
            listEDU.add("08-৮ম শ্রেনী");
            listEDU.add("09-৯ম শ্রেনী");
            listEDU.add("10-মাধ্যমিক বা সমতুল্য");
            listEDU.add("11-উচ্চ মাধ্যমিক বা সমতুল্য");
            listEDU.add("12-স্নাতক বা সমতুল্য");
            listEDU.add("13-স্নাতকোত্তর বা সমতুল্য");
            listEDU.add("14-ডাক্তারি");
            listEDU.add("15-ইঞ্জিনিয়ারিং");
            listEDU.add("16-বৃত্তিমুলক শিক্ষা");
            listEDU.add("17-কারিগরি শিক্ষা");
            listEDU.add("18-ধাত্রীবিদ্যা/নার্সিং");
            listEDU.add("19-অন্যান্য");
            listEDU.add("77-প্রযোজ্য নয়");
            listEDU.add("99-শিক্ষাগত যোগ্যতা নেই");

            ArrayAdapter<String> adptrEDU = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listEDU);
            spnEDU.setAdapter(adptrEDU);

            secRel = (LinearLayout) findViewById(R.id.secRel);
            VlblRel = (TextView) findViewById(R.id.VlblRel);
            spnRel = (Spinner) findViewById(R.id.spnRel);
            List<String> listRel = new ArrayList<String>();

            listRel.add("");
            listRel.add("1-ইসলাম");
            listRel.add("2-হিন্দু");
            listRel.add("3-বৌদ্ধ");
            listRel.add("4-খ্রীস্টান");
            listRel.add("8-Refuse to disclose");
            listRel.add("9-Not a believer");
            listRel.add("0-অন্যান্য");
            ArrayAdapter<String> adptrRel = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listRel);
            spnRel.setAdapter(adptrRel);

            secNationality = (LinearLayout) findViewById(R.id.secNationality);
            VlblNationality = (TextView) findViewById(R.id.VlblNationality);
            chkNationality = (CheckBox) findViewById(R.id.chkNationality);
            chkNationality.setChecked(true);
            secOCP = (LinearLayout) findViewById(R.id.secOCP);
            VlblOCP = (TextView) findViewById(R.id.VlblOCP);
            spnOCP = (Spinner) findViewById(R.id.spnOCP);
            List<String> listOCP = new ArrayList<String>();


            listOCP.add("");
        /*
        listOCP.add("01-বেকার");
        listOCP.add("02-ছাত্র (চাকুরি করেন না)");
        listOCP.add("03-গৃহিণী (চাকুরি করেন না)");
        listOCP.add("04-ভিক্ষুক (চাকুরি করেন না)");
        listOCP.add("05-বিকলাঙ্গ (চাকুরি করেন না)");
        listOCP.add("06-অবসরপ্রাপ্ত (চাকুরি করেন না)");
        listOCP.add("07-অদক্ষ শারীরিক কাজ (হাতে কাজ করে)চাকরানি, আয়া, গার্মেন্ট শ্রমিক, উৎপাদন/ নির্মান শ্রমিক, বাস/টেম্পু হেলপার, দারোয়ান, সুইপার, পিয়ন, রিক্সা /ভ্যান/ঠেলাগাড়ী চালক, দিনমজুর, কৃষক, জেলে, অশ্রণেীভূক্ত দাপ্তরকি কাজ");
        listOCP.add("08-অন্যান্য অদক্ষ শারীরিক কাজ (হাতে কাজ করে), ধোপার কাজ");
        listOCP.add("09-দক্ষ শারীরিক কাজ (হাতে কাজ করে)গার্মেন্ট শ্রমিক, কুক, উৎপাদন/ নির্মান শ্রমিক, গাড়ি চালক, বৈদ্যুতিক কর্মী");
        listOCP.add("10-অন্যান্য দক্ষ শারীরিক কাজ(হাতে কাজ করে) দর্জি, নাপিত, কারিগর, মুচি");
        listOCP.add("11-অন্যান্য দক্ষ শারীরিক কাজ(হাতে কাজ করে), বৈদ্যুতিক ব্যাতীত অন্যান্য শ্রেনীর কর্মী");
        listOCP.add("12-কর্মকর্তা র্পযায়ের চাকরী-সরকারী, এনজিও, বেসরকারী, সাংবাদিকতা , পুলিশ, প্রতিরক্ষা বাহিনী, বিমান এবং জাহাজের কর্মকর্তা, যানবাহন ও যোগাযোগ তত্ত্বাবধায়ক, বিক্রয় তত্ত্বাবধায়ক, হিসাবরক্ষক");
        listOCP.add("13-করণিক র্পযায়ের কর্মকর্তা- সরকারী, এনজিও, বেসরকারী, সাংবাদিকতা , পুলিশ, প্রতিরক্ষা বাহিনী সহ, নিরাপত্তা কর্মী");
        listOCP.add("14-মাঠ পর্যায়ের স্বাস্থকমী: গ্রাম্য ডাক্তার, হোমিওপ্যাথি, কবিরাজ, দক্ষ/অদক্ষ দাই, ফার্মাসিস্ট, সরকারী/বেসরকারী গ্রাম্য স্বাস্থকর্মী  (মেধাসম্পন্ন-হাতের কাজ নয়)");
        listOCP.add("15-ব্যবসায়ী, নিজস্ব ব্যবসা (মেধাসম্পন্ন- হাতের কাজ নয়)");
        listOCP.add("16-পেশাদার: ডাক্তার, কৃষি-কর্মকর্তা, শিক্ষক, অর্থনীতিবিদ, ইঞ্জিনিয়ার, আইনজীবি,বিচারক,  দন্ত চিকিৎসক ও পশু চিকিৎসক, স্থপতি (মেধাসম্পন্ন- হাতের কাজ নয়)");
        listOCP.add("17-নার্স, মিডওয়াইফ, প্যারামেডিক্স এবং অন্যান্য দক্ষ স্বাস্থ্যসেবা কর্মী");
        listOCP.add("18-অন্যান্য মেধাসম্পন্ন -হাতের কাজ নয়, সাংবাদিক, শিল্পী, লেখক, ইঞ্জিনিয়ারিং ও স্থপতি সম্পর্কিত টেকনিশিয়ান, অভিনয়, কন্ঠশিল্পী, নৃত্যশিল্পী, খেলোয়াড় এবং এতদসম্পর্কিত কর্মী, ধর্মীয় কর্মী");
        listOCP.add("77-অন্যান্য");
        listOCP.add("88-প্রযোয্য নয়");
        listOCP.add("99-জানিনা");
        */
            listOCP.add("96-গৃহিণী (চাকুরি করেননা)");
            listOCP.add("95-ছাত্র (চাকুরি করেননা)");
            listOCP.add("94-বেকার");

            listOCP.add("01-ভৌত বিজ্ঞানী এবং এতদ সম্পর্কিত টেকনিশিয়ান");
            listOCP.add("02-ইঞ্জিনিয়ারিং ও স্থপতি");
            listOCP.add("03-ইঞ্জিনিয়ারিং ও স্থপতি সম্পর্কিত টেকনিশিয়ান");
            listOCP.add("04-বিমান এবং জাহাজের কর্মকর্তা ");
            listOCP.add("05-জীব বিজ্ঞানী এবং এতদসম্পর্কিত টেকনিশিয়ান");
            listOCP.add("06-চিকিৎসক, দন্ত চিকিৎসক ও পশু চিকিৎসক");
            listOCP.add("07-নার্স এবং চিকিৎসক সংক্রান্ত অন্যান্য কর্মী ");
            listOCP.add("08-পরিসংখ্যানবিদ, গনিতবিদ, সিস্টেম এনালিস্ট এবং এতদসম্পর্কিত কর্মী");
            listOCP.add("09-অর্থনীতিবিদ ");
            listOCP.add("10-হিসাবরক্ষক ");
            listOCP.add("12-বিচারক");
            listOCP.add("13-শিক্ষক");
            listOCP.add("14-ধর্মীয় কর্মী ");
            listOCP.add("15-লেখক, সাংবাদিক এবং এতদসম্পর্কিত কর্মী");
            listOCP.add("16-চিত্রশিল্পী, ফটোগ্রাফার এবং এতদসম্পর্কিত সৃজনশীল শিল্পী");
            listOCP.add("17-অভিনয়, কণ্ঠশিল্পী ও নৃত্যশিল্পী");
            listOCP.add("18-খেলোয়াড় এবং এতদসম্পর্কিত কর্মী");
            listOCP.add("19-পেশাগত, কারিগরী এবং অন্যান্য অশ্রেনিভুক্ত এতদসম্পর্কিত কর্মী");
            listOCP.add("20-আইনজীবী");
            listOCP.add("21-ম্যানেজার");
            listOCP.add("30-সরকারি নির্বাহী কর্মকর্তা");
            listOCP.add("31-করনিক (কেরানী)");
            listOCP.add("32-টাইপিস্ট/ স্টেনোগ্রাফার/ কম্পিউটার অপারেটর ");
            listOCP.add("33-রেকর্ড কিপার, ক্যাশিয়ার এবং এতদসম্পর্কিত কর্মী ");
            listOCP.add("34-কম্পিউটার সম্পর্কিত কর্মী");
            listOCP.add("35-যানবাহন ও যোগাযোগ তত্ত্বাবধায়ক");
            listOCP.add("36-গাড়ি চালক ও কন্টাক্টর (যান্ত্রিক ও কায়িক)");
            listOCP.add("37-চিঠিপত্র বিলি (ডাকপিয়ন)");
            listOCP.add("38-টেলিফোন ও টেলিগ্রাফ অপারেটর");
            listOCP.add("39-অশ্রেনিভুক্ত দাপ্তরিক কাজ");
            listOCP.add("40-ম্যানেজার (পাইকারি ও খুচরা ব্যাবসা)");
            listOCP.add("42-বিক্রয় তত্তাবধায়ক ");
            listOCP.add("43-ভ্রমন সংক্রান্ত কাজে নিয়োজিত কর্মী ");
            listOCP.add("44-বীমা, রিয়েল এস্টেট ব্যাবসা এবং এতদসম্পর্কিত সেবা বিক্রেতা");
            listOCP.add("45-ফেরিওয়ালা ");
            listOCP.add("46-অশ্রেনিভুক্ত বিক্রয়কর্মী");
            listOCP.add("50-আবাসিক হোটেল ম্যানেজার");
            listOCP.add("51-হোটেল মালিক");
            listOCP.add("52-আবাসিক হোটেল তত্ত্বাবধায়ক ");
            listOCP.add("53-বাবুর্চি, হোটেল বয় এবং এতদসম্পর্কিত কর্মী ");
            listOCP.add("54-অশ্রেনিভুক্ত গৃহপরিচারিকা ");
            listOCP.add("55-বাড়ির কেয়ারটেকার, ঝাড়ুদার এবং এতদসম্পর্কিত কর্মী");
            listOCP.add("56-ধোপার কাজ ");
            listOCP.add("58-নিরাপত্তা কর্মী");
            listOCP.add("59-অশ্রেনিভুক্ত সেবা কর্মী");
            listOCP.add("60-কৃষি খামার ব্যাবস্থাপক ও তত্ত্বাবধায়ক ");
            listOCP.add("61-কৃষিকাজ");
            listOCP.add("63-বন কর্মী");
            listOCP.add("64-জেলে, শিকারি এবং এতদ সম্পরকিত কর্মী");
            listOCP.add("70-উৎপাদন তত্ত্বাবধায়ক এবং ফোরম্যান");
            listOCP.add("71-খননকর্মী ও খননকারী");
            listOCP.add("72-ধাতু প্রক্রিয়াকারী");
            listOCP.add("74-রাসায়নিক দ্রব্য প্রক্রিয়াকারী");
            listOCP.add("75-তাঁতি কাপড় বোনা");
            listOCP.add("76-চামড়া প্রক্রিয়াকারী");
            listOCP.add("77-খাদ্য ও পানীয় প্রক্রিয়াকারী");
            listOCP.add("78-তামাক প্রক্রিয়াকারী");
            listOCP.add("79-দর্জিও অন্যান্য সেলাই কর্মী");
            listOCP.add("80-জুতা ও চামড়াজাত দ্রব্য প্রস্তুতকারী");
            listOCP.add("81-কাঠ মিস্ত্রী");
            listOCP.add("82-পাথর কাটা ও প্রক্রিয়াকারী");
            listOCP.add("83-কর্মকার, ধালাইকর্মী");
            listOCP.add("84-বৈদ্যুতিক ব্যতিত অন্যান্য মেশিন কর্মী");
            listOCP.add("85-বৈদ্যুতিক কর্মী");
            listOCP.add("86-শব্দ প্রচার কর্মী ও চলচিচত্র প্রদর্শনকারী");
            listOCP.add("87-পানি ও পয়ঃ নিষ্কাশন কাঠামো নির্মাণকারী");
            listOCP.add("88-স্বর্ণকার");
            listOCP.add("89-গ্লাস ও মাটির জিনিস প্রস্তুতকারী");
            listOCP.add("90-রাবার ও প্লাস্টিক দ্রব্য প্রস্তুতকারী");
            listOCP.add("91-রাবার ও কাগজের বোর্ড প্রস্তুতকারী মুদ্রণকাজ");
            listOCP.add("92-মুদ্রণকাজ");
            listOCP.add("97-ভিক্ষুক (চাকুরি করেননা)");
            listOCP.add("98-বিকলাঙ্গ (চাকুরি করেননা)");
            listOCP.add("99-অবসরপ্রাপ্ত (চাকুরি করেননা)");
            listOCP.add("00-প্রযোজ্য নয়");


            ArrayAdapter<String> adptrOCP = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listOCP);
            spnOCP.setAdapter(adptrOCP);



        /*if(spnRth.getSelectedItemPosition()>0)
        {
            String spnData = Global.Left(spnRth.getSelectedItem().toString(), 2);
            if(spnData.equalsIgnoreCase("02")) {
                spnFNo.setAdapter( C.getArrayAdapter( "Select '  ' union Select SNo||'-'||NameEng Name from Member Where Dist='"+ g.getDistrict() +"' and Upz='"+ g.getUpazila() +"' and UN='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and Vill='"+ g.getVillage() +"' and HHNo='"+ g.getHouseholdNo() +"' and Sex='1' and Rth not in('01') union Select '55-মারা গিয়েছে' union Select '77-এই খানার সদস্য না' union Select '88-পরে আপডেট হবে'" ) );
                spnMNo.setAdapter( C.getArrayAdapter( "Select '  ' union Select SNo||'-'||NameEng Name from Member Where Dist='"+ g.getDistrict() +"' and Upz='"+ g.getUpazila() +"' and UN='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and Vill='"+ g.getVillage() +"' and HHNo='"+ g.getHouseholdNo() +"' and Sex='2' and Rth not in('01') union Select '55-মারা গিয়েছে' union Select '77-এই খানার সদস্য না' union Select '88-পরে আপডেট হবে'" ) );
            }
            else
            {
                spnFNo.setAdapter( C.getArrayAdapter( "Select '  ' union Select SNo||'-'||NameEng Name from Member Where Dist='"+ g.getDistrict() +"' and Upz='"+ g.getUpazila() +"' and UN='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and Vill='"+ g.getVillage() +"' and HHNo='"+ g.getHouseholdNo() +"' and Sex='1' union Select '55-মারা গিয়েছে' union Select '77-এই খানার সদস্য না' union Select '88-পরে আপডেট হবে'" ) );
                spnMNo.setAdapter( C.getArrayAdapter( "Select '  ' union Select SNo||'-'||NameEng Name from Member Where Dist='"+ g.getDistrict() +"' and Upz='"+ g.getUpazila() +"' and UN='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and Vill='"+ g.getVillage() +"' and HHNo='"+ g.getHouseholdNo() +"' and Sex='2' union Select '55-মারা গিয়েছে' union Select '77-এই খানার সদস্য না' union Select '88-পরে আপডেট হবে'" ) );
            }
        }*/

            rdogrpSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup arg0, int id) {
                    ImageButton btnSPNo1 = (ImageButton) findViewById(R.id.btnSPNo1);
                    if (id == R.id.rdoSex1) {
                        VlblSPNo.setText("স্ত্রীর লাইন নম্বর(১)");
                        VlblSPNo1.setText("স্ত্রীর লাইন নম্বর(২)");
                        VlblSPNo2.setText("স্ত্রীর লাইন নম্বর(৩)");
                        VlblSPNo3.setText("স্ত্রীর লাইন নম্বর(৪)");

                        btnSPNo1.setVisibility(View.VISIBLE);
                    /*spnSPNo.setAdapter(  C.getArrayAdapter( "Select '  ' union Select cast(SNo as varchar(2))||'-'||NameEng Name from Member Where Dist='"+ g.getDistrict() +"' and Upz='"+ g.getUpazila() +"' and UN='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and Vill='"+ g.getVillage() +"' and ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"' and HHNo='"+ g.getHouseholdNo() +"' and Sex='2' and cast((julianday('now') - julianday(DOB))/365.25 as int)>=10 union Select '77-এই খানার সদস্য না' union Select '88-পরে আপডেট হবে'" ) );
                    spnSPNo1.setAdapter( C.getArrayAdapter( "Select '  ' union Select cast(SNo as varchar(2))||'-'||NameEng Name from Member Where Dist='"+ g.getDistrict() +"' and Upz='"+ g.getUpazila() +"' and UN='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and Vill='"+ g.getVillage() +"' and ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"' and HHNo='"+ g.getHouseholdNo() +"' and Sex='2' and cast((julianday('now') - julianday(DOB))/365.25 as int)>=10 union Select '77-এই খানার সদস্য না' union Select '88-পরে আপডেট হবে'" ) );
                    spnSPNo2.setAdapter( C.getArrayAdapter( "Select '  ' union Select cast(SNo as varchar(2))||'-'||NameEng Name from Member Where Dist='"+ g.getDistrict() +"' and Upz='"+ g.getUpazila() +"' and UN='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and Vill='"+ g.getVillage() +"' and ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"' and HHNo='"+ g.getHouseholdNo() +"' and Sex='2' and cast((julianday('now') - julianday(DOB))/365.25 as int)>=10 union Select '77-এই খানার সদস্য না' union Select '88-পরে আপডেট হবে'" ) );
                    spnSPNo3.setAdapter( C.getArrayAdapter( "Select '  ' union Select cast(SNo as varchar(2))||'-'||NameEng Name from Member Where Dist='"+ g.getDistrict() +"' and Upz='"+ g.getUpazila() +"' and UN='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and Vill='"+ g.getVillage() +"' and ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"' and HHNo='"+ g.getHouseholdNo() +"' and Sex='2' and cast((julianday('now') - julianday(DOB))/365.25 as int)>=10 union Select '77-এই খানার সদস্য না' union Select '88-পরে আপডেট হবে'" ) );*/

                        spnSPNo.setAdapter(C.getArrayAdapter("Select '  ' union Select cast(SNo as varchar(2))||'-'||NameEng Name from Member Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and Sex='2' and cast((julianday('now') - julianday(DOB))/365.25 as int)>=10 union Select '77-এই খানার সদস্য না' union Select '88-পরে আপডেট হবে'"));
                        spnSPNo1.setAdapter(C.getArrayAdapter("Select '  ' union Select cast(SNo as varchar(2))||'-'||NameEng Name from Member Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and Sex='2' and cast((julianday('now') - julianday(DOB))/365.25 as int)>=10 union Select '77-এই খানার সদস্য না' union Select '88-পরে আপডেট হবে'"));
                        spnSPNo2.setAdapter(C.getArrayAdapter("Select '  ' union Select cast(SNo as varchar(2))||'-'||NameEng Name from Member Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and Sex='2' and cast((julianday('now') - julianday(DOB))/365.25 as int)>=10 union Select '77-এই খানার সদস্য না' union Select '88-পরে আপডেট হবে'"));
                        spnSPNo3.setAdapter(C.getArrayAdapter("Select '  ' union Select cast(SNo as varchar(2))||'-'||NameEng Name from Member Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and Sex='2' and cast((julianday('now') - julianday(DOB))/365.25 as int)>=10 union Select '77-এই খানার সদস্য না' union Select '88-পরে আপডেট হবে'"));
                    } else if (id == R.id.rdoSex2) {
                        VlblSPNo.setText("স্বামীর লাইন নম্বর");
                        btnSPNo1.setVisibility(View.GONE);
                        spnSPNo.setAdapter(C.getArrayAdapter("Select '  ' union Select cast(SNo as varchar(2))||'-'||NameEng Name from Member Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and Sex='1' and cast((julianday('now') - julianday(DOB))/365.25 as int)>=10 union Select '77-এই খানার সদস্য না' union Select '88-পরে আপডেট হবে'"));
                        spnSPNo1.setAdapter(C.getArrayAdapter("Select '  ' union Select cast(SNo as varchar(2))||'-'||NameEng Name from Member Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and Sex='1' and cast((julianday('now') - julianday(DOB))/365.25 as int)>=10 union Select '77-এই খানার সদস্য না' union Select '88-পরে আপডেট হবে'"));
                        spnSPNo2.setAdapter(C.getArrayAdapter("Select '  ' union Select cast(SNo as varchar(2))||'-'||NameEng Name from Member Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and Sex='1' and cast((julianday('now') - julianday(DOB))/365.25 as int)>=10 union Select '77-এই খানার সদস্য না' union Select '88-পরে আপডেট হবে'"));
                        spnSPNo3.setAdapter(C.getArrayAdapter("Select '  ' union Select cast(SNo as varchar(2))||'-'||NameEng Name from Member Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and Sex='1' and cast((julianday('now') - julianday(DOB))/365.25 as int)>=10 union Select '77-এই খানার সদস্য না' union Select '88-পরে আপডেট হবে'"));
                    } else if (id == R.id.rdoSex3) {
                        btnSPNo1.setVisibility(View.GONE);
                        spnSPNo.setAdapter(C.getArrayAdapter("Select '  ' union Select cast(SNo as varchar(2))||'-'||NameEng Name from Member Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and Sex in('1','2') and cast((julianday('now') - julianday(DOB))/365.25 as int)>=10 union Select '77-এই খানার সদস্য না' union Select '88-পরে আপডেট হবে'"));
                        spnSPNo1.setAdapter(C.getArrayAdapter("Select '  ' union Select cast(SNo as varchar(2))||'-'||NameEng Name from Member Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and Sex in('1','2') and cast((julianday('now') - julianday(DOB))/365.25 as int)>=10 union Select '77-এই খানার সদস্য না' union Select '88-পরে আপডেট হবে'"));
                        spnSPNo2.setAdapter(C.getArrayAdapter("Select '  ' union Select cast(SNo as varchar(2))||'-'||NameEng Name from Member Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and Sex in('1','2') and cast((julianday('now') - julianday(DOB))/365.25 as int)>=10 union Select '77-এই খানার সদস্য না' union Select '88-পরে আপডেট হবে'"));
                        spnSPNo3.setAdapter(C.getArrayAdapter("Select '  ' union Select cast(SNo as varchar(2))||'-'||NameEng Name from Member Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and Sex in('1','2') and cast((julianday('now') - julianday(DOB))/365.25 as int)>=10 union Select '77-এই খানার সদস্য না' union Select '88-পরে আপডেট হবে'"));
                    } else {
                        spnSPNo.setAdapter(C.getArrayAdapter("Select '  ' union Select cast(SNo as varchar(2))||'-'||NameEng Name from Member Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and cast((julianday('now') - julianday(DOB))/365.25 as int)>=10 union Select '77-এই খানার সদস্য না' union Select '88-পরে আপডেট হবে'"));
                        spnSPNo1.setAdapter(C.getArrayAdapter("Select '  ' union Select cast(SNo as varchar(2))||'-'||NameEng Name from Member Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and cast((julianday('now') - julianday(DOB))/365.25 as int)>=10 union Select '77-এই খানার সদস্য না' union Select '88-পরে আপডেট হবে'"));
                        spnSPNo2.setAdapter(C.getArrayAdapter("Select '  ' union Select cast(SNo as varchar(2))||'-'||NameEng Name from Member Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and cast((julianday('now') - julianday(DOB))/365.25 as int)>=10 union Select '77-এই খানার সদস্য না' union Select '88-পরে আপডেট হবে'"));
                        spnSPNo3.setAdapter(C.getArrayAdapter("Select '  ' union Select cast(SNo as varchar(2))||'-'||NameEng Name from Member Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and cast((julianday('now') - julianday(DOB))/365.25 as int)>=10 union Select '77-এই খানার সদস্য না' union Select '88-পরে আপডেট হবে'"));
                    }

                /*
                String SQL = "";
                SQL = "Select ifnull(FNo,'')||','||ifnull(MNo,'')||','||ifnull(spno1,'')||','||ifnull(spno2,'')||','||ifnull(spno3,'')||','||ifnull(spno4,'') as SNo";
                SQL += " from Member Where Dist='"+ g.getDistrict() +"' and Upz='"+ g.getUpazila() +"' and UN='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and Vill='"+ g.getVillage() +"' and ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"' and HHNo='"+ g.getHouseholdNo() +"' and SNo='"+ txtSNo.getText().toString() +"'";
                String FM = C.ReturnSingleValue(SQL).toString();

                if(FM.length()>0) {
                    String[] FMNo = Connection.split(FM.toString(), ',');

                    if (FMNo[2].toString().length() > 0) {
                        spnSPNo.setSelection(SpNoNoPosition(FMNo[2], 1));
                        secSPNo.setVisibility(View.VISIBLE);
                    }
                    if (FMNo[3].toString().length() > 0) {
                        spnSPNo1.setSelection(SpNoNoPosition(FMNo[3], 2));
                        secSPNo1.setVisibility(View.VISIBLE);
                    }
                    if (FMNo[4].toString().length() > 0) {
                        secSPNo2.setVisibility(View.VISIBLE);
                        spnSPNo2.setSelection(SpNoNoPosition(FMNo[4], 3));
                    }
                    if (FMNo[4].toString().length() > 0) {
                        secSPNo3.setVisibility(View.VISIBLE);
                        spnSPNo3.setSelection(SpNoNoPosition(FMNo[5], 4));
                    }
                }*/
                }
            });

            spnRth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    if (spnRth.getSelectedItem().toString().length() == 0) {
                        return;
                    }

                    String spnData = Global.Left(spnRth.getSelectedItem().toString(), 2);
                    String SQL = "";
                    SQL = "Select ifnull(FNo,'')||','||ifnull(MNo,'')||','||ifnull(spno1,'')||','||ifnull(spno2,'')||','||ifnull(spno3,'')||','||ifnull(spno4,'') as SNo";
                /*SQL += " from Member Where Dist='"+ g.getDistrict() +"' and Upz='"+ g.getUpazila() +"' and UN='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and Vill='"+ g.getVillage() +"' and ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"' and HHNo='"+ g.getHouseholdNo() +"' and SNo='"+ txtSNo.getText().toString() +"'";*/
                    SQL += " from Member Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and SNo='" + txtSNo.getText().toString() + "'";
                    String FM = C.ReturnSingleValue(SQL).toString();

                    if (spnData.equalsIgnoreCase("02")) {
                        spnMS.setSelection(2);
                        spnSPNo.setSelection(Global.SpinnerItemPosition(spnSPNo, 2, HeadSerialNo()));
                        secSPNo.setVisibility(View.VISIBLE);
                        spnFNo.setAdapter(C.getArrayAdapter("Select '  ' union Select cast(SNo as varchar(2))||'-'||NameEng Name from Member Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and Sex='1' and Rth not in('01') union Select '55-মারা গিয়েছে' union Select '77-এই খানার সদস্য না' union Select '88-পরে আপডেট হবে'"));
                        spnMNo.setAdapter(C.getArrayAdapter("Select '  ' union Select cast(SNo as varchar(2))||'-'||NameEng Name from Member Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and Sex='2' and Rth not in('01') union Select '55-মারা গিয়েছে' union Select '77-এই খানার সদস্য না' union Select '88-পরে আপডেট হবে'"));
                    } else if (spnData.equalsIgnoreCase("03") & HeadSex().equals("1")) {
                        spnFNo.setAdapter(C.getArrayAdapter("Select '  ' union Select cast(SNo as varchar(2))||'-'||NameEng Name from Member Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and Sex='1' and Rth in('01') union Select '55-মারা গিয়েছে' union Select '77-এই খানার সদস্য না'"));
                        spnMNo.setAdapter(C.getArrayAdapter("Select '  ' union Select cast(SNo as varchar(2))||'-'||NameEng Name from Member Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and Sex='2' and Rth in('02') union Select '55-মারা গিয়েছে' union Select '77-এই খানার সদস্য না' union Select '88-পরে আপডেট হবে'"));
                    } else if (spnData.equalsIgnoreCase("03") & HeadSex().equals("2")) {
                        spnFNo.setAdapter(C.getArrayAdapter("Select '  ' union Select cast(SNo as varchar(2))||'-'||NameEng Name from Member Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and Sex='1' and Rth in('02') union Select '55-মারা গিয়েছে' union Select '77-এই খানার সদস্য না' union Select '88-পরে আপডেট হবে'"));
                        spnMNo.setAdapter(C.getArrayAdapter("Select '  ' union Select cast(SNo as varchar(2))||'-'||NameEng Name from Member Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and Sex='2' and Rth in('01') union Select '55-মারা গিয়েছে' union Select '77-এই খানার সদস্য না'"));
                    } else {
                        spnFNo.setAdapter(C.getArrayAdapter("Select '  ' union Select cast(SNo as varchar(2))||'-'||NameEng Name from Member Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and Sex='1' union Select '55-মারা গিয়েছে' union Select '77-এই খানার সদস্য না' union Select '88-পরে আপডেট হবে'"));
                        spnMNo.setAdapter(C.getArrayAdapter("Select '  ' union Select cast(SNo as varchar(2))||'-'||NameEng Name from Member Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and Sex='2' union Select '55-মারা গিয়েছে' union Select '77-এই খানার সদস্য না' union Select '88-পরে আপডেট হবে'"));
                    }


                    if (FM.toString().trim().length() > 0) {
                        String[] FMNo = Connection.split(FM.toString(), ',');
                        spnFNo.setSelection(FSerialNoPosition(FMNo[0].replace("-", "")));
                        spnMNo.setSelection(MSerialNoPosition(FMNo[1].replace("-", "")));

                        if (FMNo[2].toString().toString().trim().length() > 0) {
                            spnSPNo.setSelection(SpNoNoPosition(FMNo[2].replace("-", ""), 1));
                            secSPNo.setVisibility(View.VISIBLE);
                        }
                        if (FMNo[3].toString().toString().trim().length() > 0) {
                            spnSPNo1.setSelection(SpNoNoPosition(FMNo[3].replace("-", ""), 2));
                            secSPNo1.setVisibility(View.VISIBLE);
                        }
                        if (FMNo[4].toString().toString().trim().length() > 0) {
                            secSPNo2.setVisibility(View.VISIBLE);
                            spnSPNo2.setSelection(SpNoNoPosition(FMNo[4].replace("-", ""), 3));
                        }
                        if (FMNo[4].toString().toString().trim().length() > 0) {
                            secSPNo3.setVisibility(View.VISIBLE);
                            spnSPNo3.setSelection(SpNoNoPosition(FMNo[5].replace("-", ""), 4));
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });


            ImageButton btnSPNo1 = (ImageButton) findViewById(R.id.btnSPNo1);
            btnSPNo1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (!secSPNo1.isShown()) secSPNo1.setVisibility(View.VISIBLE);
                    else if (secSPNo1.isShown() & spnSPNo1.getSelectedItemPosition() > 0)
                        secSPNo2.setVisibility(View.VISIBLE);
                    else if (secSPNo2.isShown() & spnSPNo2.getSelectedItemPosition() > 0)
                        secSPNo3.setVisibility(View.VISIBLE);
                }
            });
            ImageButton btnSPNo2 = (ImageButton) findViewById(R.id.btnSPNo2);
            btnSPNo2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    spnSPNo1.setSelection(0);
                    secSPNo1.setVisibility(View.GONE);
                }
            });

            ImageButton btnSPNo3 = (ImageButton) findViewById(R.id.btnSPNo3);
            btnSPNo3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    spnSPNo2.setSelection(0);
                    secSPNo2.setVisibility(View.GONE);
                }
            });

            ImageButton btnSPNo4 = (ImageButton) findViewById(R.id.btnSPNo4);
            btnSPNo4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    spnSPNo3.setSelection(0);
                    secSPNo3.setVisibility(View.GONE);
                }
            });

            //-----------------------------------------------------------
            secNID.setVisibility(View.VISIBLE);
            txtNID.setText("");
            secNIDStatus.setVisibility(View.VISIBLE);
            spnNIDStatus.setSelection(0);

            secBRID.setVisibility(View.VISIBLE);
            txtBRID.setText("");
            secBRIDStatus.setVisibility(View.VISIBLE);
            spnBRIDStatus.setSelection(0);
            secAge.setVisibility(View.GONE);

            secFather.setVisibility(View.GONE);
            txtFather.setText("");
            secMother.setVisibility(View.GONE);
            txtMother.setText("");
            //secELCONo.setVisibility( View.GONE );
            //txtELCONo.setText("");
            //chkELCODontKnow.setChecked( false );

            secSPNo1.setVisibility(View.GONE);
            secSPNo2.setVisibility(View.GONE);
            secSPNo3.setVisibility(View.GONE);
            //-----------------------------------------------------------


            btnDOB = (ImageButton) findViewById(R.id.btnDOB);
            btnDOB.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btnDOB";
                    showDialog(DATE_DIALOG);
                }
            });

            spnRel.setSelection(Global.SpinnerItemPosition(spnRel, 1, HouseholdReligion()));

            DataSearch(g.getDistrict(), g.getUpazila(), g.getUnion(), g.getMouza(), g.getVillage(), g.getHouseholdNo(), txtSNo.getText().toString());


            Button cmdSave = (Button) findViewById(R.id.cmdSave);
            cmdSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    DataSave();
                }
            });
        } catch (Exception e) {
            Connection.MessageBox(MemberForm.this, e.getMessage());
            return;
        }
    }


    private void ReasonNID(Boolean NIDStatus) {
        List<String> listNIDStatus = new ArrayList<String>();

        listNIDStatus.add("");
        if (NIDStatus == true) {
            listNIDStatus.add("1-অন্য জায়গায় আছে");
            listNIDStatus.add("2-হারিয়ে ফেলেছি");
            listNIDStatus.add("3-খুজে পাচ্ছি না");
        } else {
            listNIDStatus.add("1-কখনো ছিল না");
            //listNIDStatus.add("2-হারিয়ে ফেলেছি");
            //listNIDStatus.add("3-খুজে পাচ্ছি না");
            listNIDStatus.add("4-নাগরিক নয়");
        }
        ArrayAdapter<String> adptrNIDStatus = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listNIDStatus);
        spnNIDStatus.setAdapter(adptrNIDStatus);
    }

    private void ReasonBRID(Boolean BRIDStatus) {
        List<String> listBRIDStatus = new ArrayList<String>();

        listBRIDStatus.add("");
        if (BRIDStatus == true) {
            listBRIDStatus.add("1-অন্য জায়গায় আছে");
            listBRIDStatus.add("2-হারিয়ে ফেলেছি");
            listBRIDStatus.add("3-খুজে পাচ্ছি না");
        } else {
            listBRIDStatus.add("1-কখনো ছিল না");
            //listBRIDStatus.add("2-হারিয়ে ফেলেছি");
            //listBRIDStatus.add("3-খুজে পাচ্ছি না");
            listBRIDStatus.add("4-নাগরিক নয়");
        }
        ArrayAdapter<String> adptrBRIDStatus = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listBRIDStatus);
        spnBRIDStatus.setAdapter(adptrBRIDStatus);
    }

    private String HeadSerialNo() {
        String HeadSl = C.ReturnSingleValue("Select SNo from Member where dist='" + g.getDistrict() + "' and upz='" + g.getUpazila() + "' and un='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and Rth='01' and (length(ExType)=0 or ExType is null)");
        return HeadSl;
    }

    private String FatherSerial(String MemberSl) {
        String FatherSl = C.ReturnSingleValue("Select FNo from Member where dist='" + g.getDistrict() + "' and upz='" + g.getUpazila() + "' and un='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and SNo='" + MemberSl + "')");
        return FatherSl;
    }

    private String MotherSerial(String MemberSl) {
        String FatherSl = C.ReturnSingleValue("Select MNo from Member where dist='" + g.getDistrict() + "' and upz='" + g.getUpazila() + "' and un='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and SNo='" + MemberSl + "')");
        return FatherSl;
    }

    private String HeadSex() {
        String HSex = C.ReturnSingleValue("Select Sex from Member where dist='" + g.getDistrict() + "' and upz='" + g.getUpazila() + "' and un='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and Rth='01' and (length(ExType)=0 or ExType is null)");
        return HSex;
    }

    private String HouseholdReligion() {
        String SQL = "";
        SQL = "Select Religion from Household";
        SQL += " where";
        SQL += " dist='" + g.getDistrict() + "' and upz='" + g.getUpazila() + "' and un='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "'";

        String Rel = C.ReturnSingleValue(SQL);

        return Rel;
    }

    private String SerialNumber() {
        String SQL = "";
        SQL = "Select (ifnull(max(cast(SNo as int)),0)+1)MaxHH from Member";
        SQL += " where";
        SQL += " dist='" + g.getDistrict() + "' and upz='" + g.getUpazila() + "' and un='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "'";

        //String HHNo = Global.Right(("00" + C.ReturnSingleValue(SQL)), 2);
        String SNo = C.ReturnSingleValue(SQL);

        return SNo;
    }

    private String HHHead(String SerialNo) {
        String MSG = "";
        String SQL = "";
        SQL = "Select Rth  from Member";
        SQL += " where";
        SQL += " dist='" + g.getDistrict() + "' and upz='" + g.getUpazila() + "' and un='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and Rth='01' and SNo != '" + SerialNo + "'";

        //String Rth = C.ReturnSingleValue(SQL);
        //if(spnRth.getSelectedItemPosition()==2 & !Rth.equals(""))
        if (C.Existence(SQL) & Global.Left(spnRth.getSelectedItem().toString(), 2).equals("01")) {
            MSG = "এক জনের বেশি খানা প্রধান হবে না।";
        }
        if (!C.Existence("Select Rth from Member where dist='" + g.getDistrict() + "' and upz='" + g.getUpazila() + "' and un='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "'") & !Global.Left(spnRth.getSelectedItem().toString(), 2).equals("01")) {
            MSG = "খানার প্রথম সদস্য অবশ্যই খানা প্রধান হতে হবে।";
        }
        return MSG;
    }

    private String HHHeadOppositeSex() {
        String SQL = "";
        String MSG = "";
        SQL = "Select sex  from Member";
        SQL += " where";
        SQL += " dist='" + g.getDistrict() + "' and upz='" + g.getUpazila() + "' and un='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and Rth='01'";

        String Sex = C.ReturnSingleValue(SQL);
        if (spnRth.getSelectedItemPosition() == 3 & Sex.equals("1") & rdoSex1.isChecked()) {
            MSG = "খানা প্রধান পুরুষ, স্ত্রীর সেক্স অবশ্যই মহিলা হতে হবে।";
        } else if (spnRth.getSelectedItemPosition() == 3 & Sex.equals("2") & rdoSex2.isChecked()) {
            MSG = "খানা প্রধান মহিলা, স্বামীর সেক্স অবশ্যই পুরুষ হতে হবে।";
        }
        return MSG;
    }

    private void ChkChildAgeWithFatherAge() {
        String SQL = "";
        SQL = "Select DOB  from Member";
        SQL += " where";
        SQL += " dist='" + g.getDistrict() + "' and upz='" + g.getUpazila() + "' and un='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and SNo='" + Global.Left(spnFNo.getSelectedItem().toString(), 2) + "' and sex='1'";

        String FatherDOB = C.ReturnSingleValue(SQL);
        int FatherAge = Global.DateDifferenceYears(Global.DateNowDMY(), Global.DateConvertDMY(FatherDOB.toString()));
        if (rdoDOBSource1.isChecked() & secDOBSource.isShown() & spnRth.getSelectedItemPosition() == 4)//Chk Age Any Father Member With Child
        {
            int MemberAge = Global.DateDifferenceYears(Global.DateNowDMY(), dtpDOB.getText().toString());
            Integer DiffFathMemAge = (FatherAge - MemberAge);
            if (DiffFathMemAge < 10) {
                Connection.MessageBox(MemberForm.this, "সদস্যের বয়স পিতার বয়সের চেয়ে ১০ বছরের কম হতে হবে.");
                return;
            }
        } else if (!rdoDOBSource1.isChecked() & secDOBSource.isShown() & spnRth.getSelectedItemPosition() == 4)//Chk Age Any Father Member With Child
        {
            int MemberAge = Integer.valueOf(txtAge.getText().toString());
            Integer DiffFathMemAge = (FatherAge - MemberAge);
            if (DiffFathMemAge < 10) {
                Connection.MessageBox(MemberForm.this, "সদস্যের বয়স পিতার  বয়সের চেয়ে ১০ বছরের কম হতে হবে.");
                return;
            }

        } else if (rdoDOBSource1.isChecked() & secDOBSource.isShown() & spnRth.getSelectedItemPosition() == 2)//Chk Age Father  With House Hold Head
        {
            int MemberAge = Global.DateDifferenceYears(Global.DateNowDMY(), dtpDOB.getText().toString());
            Integer DiffFathMemAge = (FatherAge - MemberAge);
            if (DiffFathMemAge < 10) {
                Connection.MessageBox(MemberForm.this, "খানা প্রধান বয়স পিতার বয়সের চেয়ে ১০ বছরের কম হতে হবে.");
                return;
            }
        } else if (!rdoDOBSource1.isChecked() & secDOBSource.isShown() & spnRth.getSelectedItemPosition() == 2)//Chk Age Father  With House Hold Head
        {
            int MemberAge = Integer.valueOf(txtAge.getText().toString());
            Integer DiffFathMemAge = (FatherAge - MemberAge);
            if (DiffFathMemAge < 10) {
                Connection.MessageBox(MemberForm.this, "খানা প্রধান বয়স পিতার বয়সের চেয়ে ১০ বছরের কম হতে হবে.");
                return;

            }

        }
    }

    private void ChkChildAgeWithMotherAge() {
        String SQL = "";
        SQL = "Select DOB  from Member";
        SQL += " where";
        SQL += " dist='" + g.getDistrict() + "' and upz='" + g.getUpazila() + "' and un='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and SNo='" + Global.Left(spnFNo.getSelectedItem().toString(), 2) + "' and sex='2'";//txtSNo.getText().toString()

        String MotherDOB = C.ReturnSingleValue(SQL);
        int MotherAge = Global.DateDifferenceYears(Global.DateNowDMY(), Global.DateConvertDMY(MotherDOB.toString()));
        if (rdoDOBSource1.isChecked() & secDOBSource.isShown() & spnRth.getSelectedItemPosition() == 4) {
            int MemberAge = Global.DateDifferenceYears(Global.DateNowDMY(), dtpDOB.getText().toString());
            Integer DiffMotherMemAge = (MotherAge - MemberAge);
            if (DiffMotherMemAge < 10) {
                Connection.MessageBox(MemberForm.this, "সদস্যের বয়স মাতার বয়সের চেয়ে ১০ বছরের কম হতে হবে.");
                return;
            }
        } else if (!rdoDOBSource1.isChecked() & secDOBSource.isShown() & spnRth.getSelectedItemPosition() == 4) {
            int MemberAge = Integer.valueOf(txtAge.getText().toString());
            Integer DiffMotherMemAge = (MotherAge - MemberAge);
            if (DiffMotherMemAge < 10) {
                Connection.MessageBox(MemberForm.this, "সদস্যের বয়স মাতার বয়সের চেয়ে ১০ বছরের কম হতে হবে.");
                return;
            }
        }
    }


    private void DataSave() {
        try {
            String DV = "";
            if (txtHealthID.getText().toString().length() == 0) {
                Connection.MessageBox(MemberForm.this, "Health ID এর তালিকা থেকে সকল আইডি ব্যবহার করা হয়ে গেছে, নতুন আইডির জন্য আপনার সুপারভাইজারের সাথে যোগাযোগ করুন।");
                return;
            }
            //else if(txtHealthID.getText().toString().length()!=14)
            //{
            //    Connection.MessageBox( MemberForm.this, "Health ID অবশ্যই ১৪ সংখ্যার হতে হবে।");
            //    txtHealthID.requestFocus();
            //    return;
            //}

            if (txtNameEng.getText().toString().length() == 0) {
                Connection.MessageBox(MemberForm.this, "নাম(ইংরেজি)অবশ্যই এন্ট্রি দিতে হবে.");
                txtNameEng.requestFocus();
                return;
            } else if (spnRth.getSelectedItemPosition() == 0) {
                Connection.MessageBox(MemberForm.this, "খানা প্রধানের সাথে সম্পর্ক কি।");
                spnRth.requestFocus();
                return;
            }
        /*else if(spnRth.getSelectedItemPosition()==2 & (rdoSex2.isChecked() & !rdoSex3.isChecked() & secSex.isShown()))
        {
            Connection.MessageBox(MemberForm.this, "খানা প্রধানের স্ত্রী স্ত্রী লিঙ্গ হতে হবে ।");
            txtAge.requestFocus();
            return;
        }
        else if(spnRth.getSelectedItemPosition()==3 & rdoSex1.isChecked() & secSex.isShown())
        {
            Connection.MessageBox(MemberForm.this, "খানা প্রধানের স্ত্রী স্ত্রী লিঙ্গ হতে হবে ।");
            txtAge.requestFocus();
            return;
        }*/
            if (!chkNIDDontKnow.isChecked() & txtNID.getText().toString().length() == 0) {
                Connection.MessageBox(MemberForm.this, "জাতীয় পরিচয় পত্র নম্বর লিখুন.");
                txtNID.requestFocus();
                return;
            } else if (!chkNIDDontKnow.isChecked() & (txtNID.getText().toString().length() != 13 & txtNID.getText().toString().length() != 17)) {
                Connection.MessageBox(MemberForm.this, "জাতীয় পরিচয় পত্র নম্বর ১৩/১৭ সংখ্যার হতে হবে।");
                txtNID.requestFocus();
                return;
            } else if (chkNIDDontKnow.isChecked() & spnNIDStatus.getSelectedItemPosition() == 0) {
                Connection.MessageBox(MemberForm.this, "জাতীয় পরিচয় পত্র নম্বর কেন নেই.");
                txtNID.requestFocus();
                return;
            }

        /*else if(txtNID.getText().toString().length()==0 & spnNIDStatus.getSelectedItemPosition()==0 & secNID.isShown())
          {
            Connection.MessageBox(MemberForm.this, "Required field:আইডি নাম্বার/পরিচয় পত্র নম্বর কেন নেই.");
            txtNID.requestFocus();
            return;
          }
        else if(spnNIDStatus.getSelectedItemPosition()==0  & secNIDStatus.isShown())
          {
            Connection.MessageBox(MemberForm.this, "ন্যাশনাল আইডি না থাকলে কেন লেই.");
            spnNIDStatus.requestFocus();
            return;
          }*/
            if (!chkBRIDDontKnow.isChecked() & txtBRID.getText().toString().length() == 0) {
                Connection.MessageBox(MemberForm.this, "জন্ম নিবন্ধন নম্বর লিখুন.");
                txtBRID.requestFocus();
                return;
            } else if (!chkBRIDDontKnow.isChecked() & (txtBRID.getText().toString().length() != 14 & txtBRID.getText().toString().length() != 15 & txtBRID.getText().toString().length() != 16 & txtBRID.getText().toString().length() != 17 & txtBRID.getText().toString().length() != 19)) {
                Connection.MessageBox(MemberForm.this, "জন্ম নিবন্ধন নম্বর ১৪/১৫/১৬/১৭/১৯ সংখ্যার হতে হবে।");
                txtBRID.requestFocus();
                return;
            } else if (chkBRIDDontKnow.isChecked() & spnBRIDStatus.getSelectedItemPosition() == 0) {
                Connection.MessageBox(MemberForm.this, "জন্ম নিবন্ধন নম্বর কেন নেই.");
                txtBRID.requestFocus();
                return;
            }

        /*else if(txtBRID.getText().toString().length()==0 & spnBRIDStatus.getSelectedItemPosition()==0 & secBRID.isShown())
          {
            Connection.MessageBox(MemberForm.this, "Required field:জন্ম নিবন্ধন নাম্বার/জন্ম নিবন্ধন নম্বার কেন নেই.");
            txtBRID.requestFocus();
            return;
          }
        else if(spnBRIDStatus.getSelectedItemPosition()==0  & secBRIDStatus.isShown())
          {
            Connection.MessageBox(MemberForm.this, "জন্ম নিবন্ধন নাম্বার না থাকলে কেন নেই.");
            spnBRIDStatus.requestFocus();
            return;
          }
        */

            if (txtMobileNo1.getText().toString().length() > 0 & txtMobileNo1.getText().toString().length() != 11) {
                Connection.MessageBox(MemberForm.this, "মোবাইল নম্বর ১১ সংখ্যার হতে হবে।");
                txtMobileNo1.requestFocus();
                return;
            } else if (txtMobileNo2.getText().toString().length() > 0 & txtMobileNo2.getText().toString().length() != 11) {
                Connection.MessageBox(MemberForm.this, "মোবাইল নম্বর ১১ সংখ্যার হতে হবে।");
                txtMobileNo2.requestFocus();
                return;
            } else if (!rdoDOBSource1.isChecked() & !rdoDOBSource2.isChecked() & secDOBSource.isShown()) {
                Connection.MessageBox(MemberForm.this, "সদস্যের বয়সের উৎস কি সিলেক্ট করুন।");
                rdoDOBSource1.requestFocus();
                return;
            } else if (dtpDOB.getText().toString().length() == 0 & secDOB.isShown()) {
                Connection.MessageBox(MemberForm.this, "সদস্যের জন্ম তারিখ কত লিখুন।");
                dtpDOB.requestFocus();
                return;
            }

            DV = Global.DateValidate(dtpDOB.getText().toString());
            //int PersonAge=Integer.valueOf(txtAge.getText().toString());
            if (DV.length() != 0 & secDOB.isShown()) {
                Connection.MessageBox(MemberForm.this, DV);
                dtpDOB.requestFocus();
                return;
            } else if (txtAge.getText().toString().length() == 0 & secAge.isShown()) {
                Connection.MessageBox(MemberForm.this, "সদস্যের বয়স কত লিখুন।");
                txtAge.requestFocus();
                return;
            }
            //if(secAge.isShown())
            //{
            if (Integer.valueOf(txtAge.getText().toString()) < 0 | Integer.valueOf(txtAge.getText().toString()) > 110) {
                Connection.MessageBox(MemberForm.this, "সদস্যের বয়স অবশ্যই ০ থেকে ১১০ এর মধ্যে হতে হবে।");
                txtAge.requestFocus();
                return;
            }
            //}


            if (rdoDOBSource1.isChecked()) {
                txtAge.setText(String.valueOf(Global.DateDifferenceYears(Global.DateNowDMY(), dtpDOB.getText().toString())));
            } else {
                dtpDOB.setText(Global.DateConvertDMY(Global.addDaysYMD(Global.DateNowYMD(), -Integer.valueOf(txtAge.getText().toString()) * 365)));
            }

        /*
        Integer Age = Global.DateDifferenceYears(Global.DateNowDMY(),dtpDOB.getText().toString());
        if(spnRth.getSelectedItemPosition()==2 & Age < 18)
        {
            Connection.MessageBox(MemberForm.this, "খানা প্রধানের বয়স ১৮ বছরের সমান বা বেশী হতে হবে.");
            txtAge.requestFocus();
            return;
        }
        */

            if (spnBPlace.getSelectedItemPosition() == 0 & secBPlace.isShown()) {
                Connection.MessageBox(MemberForm.this, "জন্ম স্থান কোথায় সিলেক্ট করুন।");
                spnBPlace.requestFocus();
                return;
            } else if (spnFNo.getSelectedItemPosition() == 0 & secFNo.isShown()) {
                Connection.MessageBox(MemberForm.this, "তালিকা থেকে পিতার লাইন নাম্বার সিলেক্ট করুন।");
                spnFNo.requestFocus();
                return;
            } else if (txtFather.getText().toString().length() == 0 & !chkFatherDontKnow.isChecked() & secFather.isShown()) {
                Connection.MessageBox(MemberForm.this, "পিতা এই খানায় না থাকলে পিতার নাম লিখুন।");
                txtFather.requestFocus();
                return;
            } else if (spnMNo.getSelectedItemPosition() == 0 & secMNo.isShown()) {
                Connection.MessageBox(MemberForm.this, "তালিকা থেকে মাতার লাইন নাম্বার সিলেক্ট করুন।");
                spnMNo.requestFocus();
                return;
            } else if (txtMother.getText().toString().length() == 0 & !chkMotherDontKnow.isChecked() & secMother.isShown()) {
                Connection.MessageBox(MemberForm.this, "মা এই খানায় না থাকলে মাতার নাম লিখুন।");
                txtMother.requestFocus();
                return;
            } else if (!rdoSex1.isChecked() & !rdoSex2.isChecked() & !rdoSex3.isChecked() & secSex.isShown()) {
                Connection.MessageBox(MemberForm.this, "সদস্যের সেক্স কি সিলেক্ট করুন।");
                rdoSex1.requestFocus();
                return;
            } else if (spnMS.getSelectedItemPosition() == 0 & secMS.isShown()) {
                Connection.MessageBox(MemberForm.this, "তালিকা থেকে সদস্যের বৈবাহিক অবস্থা কি সিলেক্ট করুন।");
                spnMS.requestFocus();
                return;
            }
        /*else if(spnMS.getSelectedItemPosition()==2 & secMS.isShown() & Age < 12)
        {
            Connection.MessageBox(MemberForm.this, "সদস্যের বয়স ১২ বছরের সমান বা বেশী হতে হবে.");
            txtAge.requestFocus();
            return;
        }*/

            String R = Global.Left(spnRth.getSelectedItem().toString(), 2);
            if ((R.equals("02") || R.equals("04") || R.equals("07") || R.equals("11") || R.equals("15") || R.equals("17")) & spnMS.getSelectedItemPosition() == 1) {
                Connection.MessageBox(MemberForm.this, "সদস্যের বৈবাহিক অবস্তা অবিবাহিত হতে পারে না।");
                spnMS.requestFocus();
                return;
            }

            if (spnMS.getSelectedItemPosition() == 2 & spnSPNo.getSelectedItemPosition() == 0 & secSPNo.isShown()) {
                Connection.MessageBox(MemberForm.this, "সদস্যের স্বামী/স্ত্রীর লাইন নাম্বার সিলেক্ট করুন।");
                spnSPNo.requestFocus();
                return;
            }

        /*else if(spnMS.getSelectedItemPosition()==2 & !chkELCODontKnow.isChecked() & txtELCONo.getText().toString().length()==0 & secELCONo.isShown())
          {
            Connection.MessageBox(MemberForm.this, "সদস্যের সঠিক ELCO No লিখুন।");
            txtELCONo.requestFocus();
            return;
          }
        else if(spnEDU.getSelectedItemPosition()==0  & secEDU.isShown())
          {
            Connection.MessageBox(MemberForm.this, "তালিকা থেকে সদস্যের শিক্ষাগত মান কি সিলেক্ট করুন।");
            spnEDU.requestFocus();
            return;
          }
          */
        /*else if(spnEDU.getSelectedItemPosition()!=0  & secEDU.isShown() & Age <5)
        {
            Connection.MessageBox(MemberForm.this, "তালিকা থেকে সদস্যের শিক্ষাগত মান কি সিলেক্ট করুন।");
            spnEDU.requestFocus();
            return;
        }*/
            else if (spnRel.getSelectedItemPosition() == 0 & secRel.isShown()) {
                Connection.MessageBox(MemberForm.this, "তালিকা থেকে সদস্যের ধর্ম সিলেক্ট করুন।");
                spnRel.requestFocus();
                return;
            } else if (spnOCP.getSelectedItemPosition() == 0 & secOCP.isShown()) {
                Connection.MessageBox(MemberForm.this, "তালিকা থেকে সদস্যের পেশা কি সিলেক্ট করুন।");
                spnOCP.requestFocus();
                return;
            }
        /*else if(spnOCP.getSelectedItemPosition()!=0  & secOCP.isShown() & Age < 8)
        {
            Connection.MessageBox(MemberForm.this, "সদস্যের বয়স ৮ বছরের সমান বা বেশী হতে হবে.");
            txtAge.requestFocus();
            return;
        }*/

            String Head = HHHead(txtSNo.getText().toString());
            if (Head.length() != 0) {
                Connection.MessageBox(MemberForm.this, Head);
                return;
            }

            Head = HHHeadOppositeSex();
            if (Head.length() != 0) {
                Connection.MessageBox(MemberForm.this, Head);
                return;
            }

            //ChkChildAgeWithFatherAge();
            //ChkChildAgeWithMotherAge();


            String SQL = "";
            String AG = "";

            if (!C.Existence("Select Dist,Upz,UN,Mouza,Vill,HHNo,SNo from " + TableName + "  Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and SNo='" + txtSNo.getText().toString() + "'")) {
                SQL = "Insert into " + TableName + "(Dist,Upz,UN,Mouza,Vill,ProvType,ProvCode,HHNo,SNo,HealthID,Lat,Lon,StartTime,EnType,EnDate,EndTime,ExType,ExDate,UserId,EnDt,Upload)Values('" + g.getDistrict() + "','" + g.getUpazila() + "','" + g.getUnion() + "','" + g.getMouza() + "','" + g.getVillage() + "','" + g.getProvType() + "','" + g.getProvCode() + "','" + g.getHouseholdNo() + "','" + txtSNo.getText() + "','" + txtHealthID.getText().toString() + "','" + Double.toString(currentLatitude) + "','" + Double.toString(currentLongitude) + "','" + StartTime + "','','" + g.DateNowYMD() + "','" + g.CurrentTime24() + "','','','" + g.getUserID() + "','" + Global.DateTimeNowYMDHMS() + "','2')";
                C.Save(SQL);
                C.Save("Update HealthIDRepository Set Status='2' where HealthID='" + txtHealthID.getText().toString() + "'");
            }

            SQL = "Update " + TableName + " Set Upload='2',";
            SQL += "NameEng = '" + txtNameEng.getText().toString() + "',";
            SQL += "NameBang = '" + txtNameBang.getText().toString() + "',";
            SQL += "RTH = '" + (spnRth.getSelectedItemPosition() == 0 ? "" : Global.Left(spnRth.getSelectedItem().toString(), 2)) + "',";

            //SQL+="HaveNID = '"+ (rdoHaveNID1.isChecked()?"1":"2") +"',";
            SQL += "HaveNID = '" + (chkNIDDontKnow.isChecked() ? "2" : "1") + "',";
            SQL += "NID = '" + txtNID.getText().toString() + "',";
            SQL += "NIDStatus = '" + (spnNIDStatus.getSelectedItemPosition() == 0 ? "" : Global.Left(spnNIDStatus.getSelectedItem().toString(), 1)) + "',";

            //SQL+="HaveBR = '"+ (rdoHaveBR1.isChecked()?"1":"2") +"',";
            SQL += "HaveBR = '" + (chkBRIDDontKnow.isChecked() ? "2" : "1") + "',";
            SQL += "BRID = '" + txtBRID.getText().toString() + "',";
            SQL += "BRIDStatus = '" + (spnBRIDStatus.getSelectedItemPosition() == 0 ? "" : Global.Left(spnBRIDStatus.getSelectedItem().toString(), 1)) + "',";
            SQL += "MobileNo1 = '" + txtMobileNo1.getText().toString() + "',";
            SQL += "MobileNo2 = '" + txtMobileNo2.getText().toString() + "',";
            SQL += "MobileYN = '" + (chkMobileNo.isChecked() ? "1" : "2") + "',";

            if (rdoDOBSource1.isChecked()) {
                SQL += "DOB = '" + Global.DateConvertYMD(dtpDOB.getText().toString()) + "',";
                SQL += "Age = '" + Global.DateDifferenceYears(Global.DateNowDMY(), dtpDOB.getText().toString()) + "',";
            } else {
                double db = Double.valueOf(txtAge.getText().toString()) * 365.00;
                SQL += "DOB = '" + Global.addDaysYMD(Global.DateNowDMY(), -(int) db) + "',";
                //Integer AG1 = Integer.valueOf((int) Math.round((Double.valueOf(txtAge.getText().toString())*365.25)));
                //SQL+="DOB = '"+ Global.addDaysYMD(Global.DateNowDMY(), -AG1) +"',";
                SQL += "Age = '" + txtAge.getText().toString() + "',";
            }


            SQL += "DOBSource = '" + (rdoDOBSource1.isChecked() ? "1" : "2") + "',";
            SQL += "BPlace = '" + (spnBPlace.getSelectedItemPosition() == 0 ? "" : Global.Left(spnBPlace.getSelectedItem().toString(), 2)) + "',";

            String[] f = spnFNo.getSelectedItem().toString().split("-");
            //SQL+="FNo = '"+ (spnFNo.getSelectedItemPosition()==0?"":Global.Left(spnFNo.getSelectedItem().toString(),2)) +"',";
            SQL += "FNo = '" + f[0] + "',";
            SQL += "Father = '" + txtFather.getText().toString() + "',";
            SQL += "FDontKnow = '" + (chkFatherDontKnow.isChecked() ? "1" : "2") + "',";

            String[] m = spnMNo.getSelectedItem().toString().split("-");
            //SQL+="MNo = '"+ (spnMNo.getSelectedItemPosition()==0?"":Global.Left(spnMNo.getSelectedItem().toString(),2)) +"',";
            SQL += "MNo = '" + m[0] + "',";
            SQL += "Mother = '" + txtMother.getText().toString() + "',";
            SQL += "MDontKnow = '" + (chkMotherDontKnow.isChecked() ? "1" : "2") + "',";
            SQL += "Sex = '" + (rdoSex1.isChecked() ? "1" : (rdoSex2.isChecked() ? "2" : "3")) + "',";
            SQL += "MS = '" + (spnMS.getSelectedItemPosition() == 0 ? "" : Global.Left(spnMS.getSelectedItem().toString(), 1)) + "',";

            String[] sp1 = spnSPNo.getSelectedItem().toString().split("-");
            String[] sp2 = spnSPNo1.getSelectedItem().toString().split("-");
            String[] sp3 = spnSPNo2.getSelectedItem().toString().split("-");
            String[] sp4 = spnSPNo3.getSelectedItem().toString().split("-");
            SQL += "SPNo1 = '" + sp1[0] + "',";
            SQL += "SPNo2 = '" + sp2[0] + "',";
            SQL += "SPNo3 = '" + sp3[0] + "',";
            SQL += "SPNo4 = '" + sp4[0] + "',";

        /*SQL+="SPNo1 = '"+ (spnSPNo.getSelectedItemPosition()==0?"":Global.Left(spnSPNo.getSelectedItem().toString(),2)) +"',";
        SQL+="SPNo2 = '"+ (spnSPNo1.getSelectedItemPosition()==0?"":Global.Left(spnSPNo1.getSelectedItem().toString(),2)) +"',";
        SQL+="SPNo3 = '"+ (spnSPNo2.getSelectedItemPosition()==0?"":Global.Left(spnSPNo2.getSelectedItem().toString(),2)) +"',";
        SQL+="SPNo4 = '"+ (spnSPNo3.getSelectedItemPosition()==0?"":Global.Left(spnSPNo3.getSelectedItem().toString(),2)) +"',";
        */

            SQL += "ELCONo = '" + txtELCONo.getText().toString() + "',";
            SQL += "ELCODontKnow = '" + (chkELCODontKnow.isChecked() ? "1" : "2") + "',";
            SQL += "EDU = '" + (spnEDU.getSelectedItemPosition() == 0 ? "" : Global.Left(spnEDU.getSelectedItem().toString(), 2)) + "',";
            SQL += "Rel = '" + (spnRel.getSelectedItemPosition() == 0 ? "" : Global.Left(spnRel.getSelectedItem().toString(), 1)) + "',";
            SQL += "Nationality = '" + (chkNationality.isChecked() ? "1" : "2") + "',";
            SQL += "OCP = '" + (spnOCP.getSelectedItemPosition() == 0 ? "" : Global.Left(spnOCP.getSelectedItem().toString(), 2)) + "'";
            SQL += " Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and SNo='" + txtSNo.getText().toString() + "'";
            C.Save(SQL);
            C.Save("Update HealthIDRepository Set Status='2' where HealthID='" + txtHealthID.getText().toString() + "'");

            String Rth = (spnRth.getSelectedItemPosition() == 0 ? "" : Global.Left(spnRth.getSelectedItem().toString(), 2));
            String SX = (rdoSex1.isChecked() ? "1" : (rdoSex2.isChecked() ? "2" : "3"));

            //Update Father/Mother/Spouses no
            if (Rth.equals("02")) {
                try {
                /*String SP = C.ReturnSingleValue("select (case when spno1='"+ txtSNo.getText().toString() +"' or length(spno1)=0 or spno1 is null then 'spno1' when spno2='"+ txtSNo.getText().toString() +"' or length(spno2)=0 or spno2 is null then 'spno2' when spno3='"+ txtSNo.getText().toString() +"' or length(spno3)=0 or spno3 is null then 'spno3' when spno4='"+ txtSNo.getText().toString() +"' or length(spno4)=0 or spno4 is null then 'spno4' end)SP from member where Dist='"+ g.getDistrict() +"' and Upz='"+ g.getUpazila() +"' and UN='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and Vill='"+ g.getVillage() +"' and ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"' and HHNo='"+ g.getHouseholdNo() +"' and Rth='01' and (length(ExType)=0 or ExType is null)");*/
                    String SP = C.ReturnSingleValue("select (case when spno1='" + txtSNo.getText().toString() + "' or length(spno1)=0 or spno1 is null then 'spno1' when spno2='" + txtSNo.getText().toString() + "' or length(spno2)=0 or spno2 is null then 'spno2' when spno3='" + txtSNo.getText().toString() + "' or length(spno3)=0 or spno3 is null then 'spno3' when spno4='" + txtSNo.getText().toString() + "' or length(spno4)=0 or spno4 is null then 'spno4' end)SP from member where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and Rth='01' and (length(ExType)=0 or ExType is null)");
                    if (SP != null)
                        C.Save("Update Member Set " + SP + "='" + txtSNo.getText() + "' where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and Rth='01' and (length(ExType)=0 or ExType is null)");
                } catch (Exception e) {
                    //Connection.MessageBox(MemberForm.this,e.getMessage());
                    //return;
                }
            } else if (Rth.equals("04") & SX.equals("1")) {
                //Fathers line number of sons
                C.Save("Update Member Set FNo='" + txtSNo.getText() + "' where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and Rth='01' and (length(ExType)=0 or ExType is null)");
                //Spouses line number of Father
                //String FaSP = C.ReturnSingleValue("select (case when spno1='88' or spno1='"+ txtSNo.getText().toString() +"' or length(spno1)=0 then 'spno1' when spno2='88' or spno2='"+ txtSNo.getText().toString() +"' or length(spno2)=0 then 'spno2' when spno3='88' or spno3='"+ txtSNo.getText().toString() +"' or length(spno3)=0 then 'spno3' when spno4='88' or spno1='"+ txtSNo.getText().toString() +"' or length(spno4)=0 then 'spno4' end)SP from member where Dist='"+ g.getDistrict() +"' and Upz='"+ g.getUpazila() +"' and UN='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and Vill='"+ g.getVillage() +"' and ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"' and HHNo='"+ g.getHouseholdNo() +"' and Rth='01' and (length(ExType)=0 or ExType is null)");
                //C.Save("Update Member Set FNo='"+ txtSNo.getText() +"' where Dist='"+ g.getDistrict() +"' and Upz='"+ g.getUpazila() +"' and UN='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and Vill='"+ g.getVillage() +"' and ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"' and HHNo='"+ g.getHouseholdNo() +"' and Rth='01' and (length(ExType)=0 or ExType is null)");
            } else if (Rth.equals("04") & SX.equals("2")) {
                C.Save("Update Member Set MNo='" + txtSNo.getText() + "' where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and Rth='01' and (length(ExType)=0 or ExType is null)");
            }

            Connection.MessageBox(MemberForm.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
            finish();
            Intent f2 = new Intent(getApplicationContext(), MemberList.class);
            startActivity(f2);
        } catch (Exception e) {
            Connection.MessageBox(MemberForm.this, e.getMessage());
            return;
        }
    }


    private int SpNoNoPosition(String spouseNo, int spSerial) {
        int pos = 0;
        if (spouseNo.length() != 0 & !spouseNo.equalsIgnoreCase("null")) {
            spouseNo = Integer.valueOf(spouseNo).toString();
            String[] f;

            if (spSerial == 1) {
                for (int i = 0; i < spnSPNo.getCount(); i++) {
                    f = spnSPNo.getItemAtPosition(i).toString().split("-");
                    if (spnSPNo.getItemAtPosition(i).toString().length() != 0) {
                        if (f[0].toString().equalsIgnoreCase(spouseNo)) {
                            pos = i;
                            i = spnSPNo.getCount();
                        }
                    }
                }
            } else if (spSerial == 2) {
                for (int i = 0; i < spnSPNo1.getCount(); i++) {
                    f = spnSPNo1.getItemAtPosition(i).toString().split("-");
                    if (spnSPNo1.getItemAtPosition(i).toString().length() != 0) {
                        if (f[0].toString().equalsIgnoreCase(spouseNo)) {
                            pos = i;
                            i = spnSPNo1.getCount();
                        }
                    }
                }
            }
            if (spSerial == 3) {
                for (int i = 0; i < spnSPNo2.getCount(); i++) {
                    f = spnSPNo2.getItemAtPosition(i).toString().split("-");
                    if (spnSPNo2.getItemAtPosition(i).toString().length() != 0) {
                        if (f[0].toString().equalsIgnoreCase(spouseNo)) {
                            pos = i;
                            i = spnSPNo2.getCount();
                        }
                    }
                }
            }
            if (spSerial == 4) {
                for (int i = 0; i < spnSPNo3.getCount(); i++) {
                    f = spnSPNo3.getItemAtPosition(i).toString().split("-");
                    if (spnSPNo3.getItemAtPosition(i).toString().length() != 0) {
                        if (f[0].toString().equalsIgnoreCase(spouseNo)) {
                            pos = i;
                            i = spnSPNo3.getCount();
                        }
                    }
                }
            }

        }
        return pos;
    }


    private int FSerialNoPosition(String fatherNo) {
        int pos = 0;
        if (fatherNo.length() != 0) {
            fatherNo = Integer.valueOf(fatherNo).toString();
            String[] f;
            for (int i = 0; i < spnFNo.getCount(); i++) {
                f = spnFNo.getItemAtPosition(i).toString().split("-");
                if (spnFNo.getItemAtPosition(i).toString().length() != 0) {
                    if (f[0].toString().equalsIgnoreCase(fatherNo)) {
                        pos = i;
                        i = spnFNo.getCount();
                    }
                }
            }
        }
        return pos;
    }

    private int MSerialNoPosition(String motherNo) {
        int pos = 0;
        if (motherNo.length() != 0) {
            motherNo = Integer.valueOf(motherNo).toString();
            String[] m;
            for (int i = 0; i < spnMNo.getCount(); i++) {
                m = spnMNo.getItemAtPosition(i).toString().split("-");
                if (spnMNo.getItemAtPosition(i).toString().length() != 0) {
                    if (m[0].toString().equalsIgnoreCase(motherNo)) {
                        pos = i;
                        i = spnMNo.getCount();
                    }
                }
            }
        }
        return pos;
    }


    private void DataSearch(String Dist, String Upz, String UN, String Mouza, String Vill, String HHNo, String SNo) {
        try {
            RadioButton rb;
            String SQL = "";
            SQL = "Select Dist, Upz, UN, Mouza, Vill, HHNo, SNo as SNo, ifnull(HealthID,'') as HealthID, ifnull(NameEng,'') as NameEng,";
            SQL += " ifnull(NameBang,'') as NameBang, ifnull(Rth,'') as Rth, ifnull(HaveNID,'') as HaveNID, ifnull(NID,'') as NID, ifnull(NIDStatus,'') as NIDStatus, ifnull(HaveBR,'') as HaveBR, ifnull(BRID,'') as BRID, ifnull(BRIDStatus,'') as BRIDStatus, ifnull(MobileNo1,'') as MobileNo1,";
            SQL += " ifnull(MobileNo2,'') as MobileNo2, ifnull(MobileYN,'')as MobileYN, ifnull(DOB,'') as DOB, ifnull(cast(((julianday(date('now'))-julianday(DOB))/365)as int),'') as Age, ifnull(DOBSource,'') as DOBSource, ifnull(BPlace,'') as BPlace, ifnull(FNo,'') as FNo, ifnull(Father,'') as Father, ifnull(FDontKnow,'')as FDontKnow, ifnull(MNo,'') as MNo, ifnull(Mother,'') as Mother,ifnull(MDontKnow,'')as MDontKnow,";
            SQL += " ifnull(Sex,'') as Sex, ifnull(MS,'') as MS, ifnull(SPNO1,'') as SPNO1,ifnull(SPNO2,'') as SPNO2,ifnull(SPNO3,'') as SPNO3,ifnull(SPNO4,'') as SPNO4, ifnull(ELCONo,'') as ELCONo, ifnull(ELCODontKnow,'') as ELCODontKnow, ifnull(EDU,'') as EDU, ifnull(Rel,'') as Rel, ifnull(Nationality,'') as Nationality, ifnull(OCP,'') as OCP";
            SQL += " from Member Where Dist='" + Dist + "' and Upz='" + Upz + "' and UN='" + UN + "' and Mouza='" + Mouza + "' and Vill='" + Vill + "' and HHNo='" + HHNo + "' and SNo='" + SNo + "'";
            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                chkIDHave.setEnabled(false);
                txtHealthID.setEnabled(false);
                txtHealthID.setText(cur.getString(cur.getColumnIndex("HealthID")));
                txtSNo.setText(cur.getString(cur.getColumnIndex("SNo")));
                txtNameEng.setText(cur.getString(cur.getColumnIndex("NameEng")).replace("null", ""));
                txtNameBang.setText(cur.getString(cur.getColumnIndex("NameBang")).replace("null", ""));
                spnRth.setSelection(Global.SpinnerItemPosition(spnRth, 2, cur.getString(cur.getColumnIndex("Rth"))));

                if (cur.getString(cur.getColumnIndex("HaveNID")).equals("1")) {
                    chkNIDDontKnow.setChecked(false);
                    //rdoHaveNID1.setChecked(true);
                    secNID.setVisibility(View.VISIBLE);
                    secNIDStatus.setVisibility(View.GONE);
                } else if (cur.getString(cur.getColumnIndex("HaveNID")).equals("2")) {
                    chkNIDDontKnow.setChecked(true);
                    //rdoHaveNID2.setChecked(true);
                    secNID.setVisibility(View.VISIBLE);
                    secNIDStatus.setVisibility(View.VISIBLE);
                }
                txtNID.setText(cur.getString(cur.getColumnIndex("NID")).replace("null", ""));
                spnNIDStatus.setSelection(Global.SpinnerItemPosition(spnNIDStatus, 1, cur.getString(cur.getColumnIndex("NIDStatus"))));

            /*if(cur.getString(cur.getColumnIndex("HaveNID")).equals("1"))
            {
               rdoHaveNID1.setChecked(true);
               secNID.setVisibility( View.VISIBLE );
               secNIDStatus.setVisibility( View.VISIBLE );
            }
            else if(cur.getString(cur.getColumnIndex("HaveNID")).equals("2"))
            {
               rdoHaveNID2.setChecked(true);
               secNID.setVisibility( View.GONE );
               secNIDStatus.setVisibility( View.VISIBLE );
            }
            txtNID.setText(cur.getString(cur.getColumnIndex("NID")));
            spnNIDStatus.setSelection(Global.SpinnerItemPosition(spnNIDStatus, 1 ,cur.getString(cur.getColumnIndex("NIDStatus"))));*/

                if (cur.getString(cur.getColumnIndex("HaveBR")).equals("1")) {
                    chkBRIDDontKnow.setChecked(false);
                    //rdoHaveBR1.setChecked(true);
                    secBRID.setVisibility(View.VISIBLE);
                    secBRIDStatus.setVisibility(View.GONE);
                } else if (cur.getString(cur.getColumnIndex("HaveBR")).equals("2")) {
                    chkBRIDDontKnow.setChecked(true);
                    //rdoHaveBR2.setChecked(true);
                    secBRID.setVisibility(View.VISIBLE);
                    secBRIDStatus.setVisibility(View.VISIBLE);
                }

/*
            if(cur.getString(cur.getColumnIndex("HaveBR")).equals("1"))
            {
               rdoHaveBR1.setChecked(true);
               secBRID.setVisibility( View.VISIBLE );
               secBRIDStatus.setVisibility( View.VISIBLE );
            }
            else if(cur.getString(cur.getColumnIndex("HaveBR")).equals("2"))
            {
               rdoHaveBR2.setChecked(true);
               secBRID.setVisibility( View.GONE );
               secBRIDStatus.setVisibility( View.VISIBLE );
            }
*/
                txtBRID.setText(cur.getString(cur.getColumnIndex("BRID")).replace("null", ""));
                spnBRIDStatus.setSelection(Global.SpinnerItemPosition(spnBRIDStatus, 1, cur.getString(cur.getColumnIndex("BRIDStatus"))));
                txtMobileNo1.setText(cur.getString(cur.getColumnIndex("MobileNo1")).replace("null", ""));
                txtMobileNo2.setText(cur.getString(cur.getColumnIndex("MobileNo2")).replace("null", ""));
                if (cur.getString(cur.getColumnIndex("MobileYN")).equals("1"))
                    chkMobileNo.setChecked(true);
                else
                    chkMobileNo.setChecked(false);

                if (cur.getString(cur.getColumnIndex("DOBSource")).equals("1"))
                    rdoDOBSource1.setChecked(true);
                else
                    rdoDOBSource2.setChecked(true);

                dtpDOB.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("DOB"))));
                txtAge.setText(cur.getString(cur.getColumnIndex("Age")).replace("null", ""));
                spnBPlace.setSelection(Global.SpinnerItemPosition(spnBPlace, 2, cur.getString(cur.getColumnIndex("BPlace"))));

                spnFNo.setSelection(FSerialNoPosition(cur.getString(cur.getColumnIndex("FNo"))));
                //spnFNo.setSelection(Global.SpinnerItemPosition(spnFNo, 1 ,cur.getString(cur.getColumnIndex("FNo"))));
                txtFather.setText(cur.getString(cur.getColumnIndex("Father")).replace("null", ""));
                if (cur.getString(cur.getColumnIndex("FDontKnow")).equals("1")) {
                    chkFatherDontKnow.setChecked(true);
                } else {
                    chkFatherDontKnow.setChecked(false);
                }
                if (cur.getString(cur.getColumnIndex("FNo")).endsWith("77")) {
                    secFather.setVisibility(View.VISIBLE);
                } else {
                    secFather.setVisibility(View.GONE);
                }

                spnMNo.setSelection(MSerialNoPosition(cur.getString(cur.getColumnIndex("MNo"))));
                //spnMNo.setSelection(Global.SpinnerItemPosition(spnMNo, 2 ,cur.getString(cur.getColumnIndex("MNo"))));
                txtMother.setText(cur.getString(cur.getColumnIndex("Mother")).replace("null", ""));
                if (cur.getString(cur.getColumnIndex("MDontKnow")).equals("1")) {
                    chkMotherDontKnow.setChecked(true);
                } else {
                    chkMotherDontKnow.setChecked(false);
                }

                if (cur.getString(cur.getColumnIndex("Sex")).equals("1"))
                    rdoSex1.setChecked(true);
                else if (cur.getString(cur.getColumnIndex("Sex")).equals("2"))
                    rdoSex2.setChecked(true);
                else if (cur.getString(cur.getColumnIndex("Sex")).equals("3"))
                    rdoSex3.setChecked(true);

                if (cur.getString(cur.getColumnIndex("MNo")).endsWith("77")) {
                    secMother.setVisibility(View.VISIBLE);
                } else {
                    secMother.setVisibility(View.GONE);
                }

                spnMS.setSelection(Global.SpinnerItemPosition(spnMS, 1, cur.getString(cur.getColumnIndex("MS"))));

                if (cur.getString(cur.getColumnIndex("SPNO1")).replace("null", "").toString().trim().length() > 0) {
                    secSPNo.setVisibility(View.VISIBLE);
                    spnSPNo.setSelection(SpNoNoPosition(cur.getString(cur.getColumnIndex("SPNO1")).replace("-", ""), 1));
                }
                if (cur.getString(cur.getColumnIndex("SPNO2")).replace("null", "").toString().trim().length() > 0) {
                    secSPNo1.setVisibility(View.VISIBLE);
                    spnSPNo1.setSelection(SpNoNoPosition(cur.getString(cur.getColumnIndex("SPNO2")).replace("-", ""), 2));
                } else {
                    secSPNo1.setVisibility(View.GONE);
                    VlblSPNo1.setVisibility(View.GONE);
                }
                if (cur.getString(cur.getColumnIndex("SPNO3")).replace("null", "").toString().trim().length() > 0) {
                    secSPNo2.setVisibility(View.VISIBLE);
                    spnSPNo2.setSelection(SpNoNoPosition(cur.getString(cur.getColumnIndex("SPNO3")).replace("-", ""), 3));
                } else {
                    secSPNo2.setVisibility(View.GONE);
                    VlblSPNo2.setVisibility(View.GONE);
                }
                if (cur.getString(cur.getColumnIndex("SPNO4")).replace("null", "").toString().trim().length() > 0) {
                    secSPNo3.setVisibility(View.VISIBLE);
                    spnSPNo3.setSelection(SpNoNoPosition(cur.getString(cur.getColumnIndex("SPNO4")).replace("-", ""), 4));
                } else {
                    secSPNo3.setVisibility(View.GONE);
                    VlblSPNo3.setVisibility(View.GONE);
                }

            /*spnSPNo.setSelection(Global.SpinnerItemPosition(spnSPNo, 2 ,cur.getString(cur.getColumnIndex("SPNO1"))));
            spnSPNo1.setSelection(Global.SpinnerItemPosition(spnSPNo1, 2 ,cur.getString(cur.getColumnIndex("SPNO2"))));
            spnSPNo2.setSelection(Global.SpinnerItemPosition(spnSPNo2, 2 ,cur.getString(cur.getColumnIndex("SPNO3"))));
            spnSPNo3.setSelection(Global.SpinnerItemPosition(spnSPNo3, 2 ,cur.getString(cur.getColumnIndex("SPNO4"))));


            if(spnSPNo1.getSelectedItemPosition()>0) secSPNo1.setVisibility( View.VISIBLE );
            if(spnSPNo2.getSelectedItemPosition()>0) secSPNo2.setVisibility( View.VISIBLE );
            if(spnSPNo3.getSelectedItemPosition()>0) secSPNo3.setVisibility( View.VISIBLE );
            */

                txtELCONo.setText(cur.getString(cur.getColumnIndex("ELCONo")).replace("null", ""));
                if (cur.getString(cur.getColumnIndex("ELCODontKnow")).equals("1")) {
                    chkELCODontKnow.setChecked(true);
                } else if (cur.getString(cur.getColumnIndex("ELCODontKnow")).equals("2")) {
                    chkELCODontKnow.setChecked(false);
                }
                if (cur.getString(cur.getColumnIndex("MS")).equalsIgnoreCase("2")) {
                    //secELCONo.setVisibility( View.VISIBLE );
                } else {
                    //secELCONo.setVisibility( View.GONE );
                    //txtELCONo.setText("");
                    //chkELCODontKnow.setChecked( false );
                }

                spnEDU.setSelection(Global.SpinnerItemPosition(spnEDU, 2, cur.getString(cur.getColumnIndex("EDU"))));
                spnRel.setSelection(Global.SpinnerItemPosition(spnRel, 1, cur.getString(cur.getColumnIndex("Rel"))));
                if (cur.getString(cur.getColumnIndex("Nationality")).equals("1")) {
                    chkNationality.setChecked(true);
                } else if (cur.getString(cur.getColumnIndex("Nationality")).equals("2")) {
                    chkNationality.setChecked(false);
                }
                spnOCP.setSelection(Global.SpinnerItemPosition(spnOCP, 2, cur.getString(cur.getColumnIndex("OCP"))));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            //Connection.MessageBox(MemberForm.this, e.getMessage());
            //return;
        }
    }


    protected Dialog onCreateDialog(int id) {
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        Integer Y = g.mYear;
        Integer M = g.mMonth;
        Integer D = g.mDay;
        if (dtpDOB.getText().length() > 0) {
            Y = Integer.valueOf(Global.Right(dtpDOB.getText().toString(), 4));
            M = Integer.valueOf(dtpDOB.getText().toString().substring(4, 5));
            D = Integer.valueOf(Global.Left(dtpDOB.getText().toString(), 2));
        }
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


            dtpDate = (EditText) findViewById(R.id.dtpDOB);
            if (VariableID.equals("btnDOB")) {
                dtpDate = (EditText) findViewById(R.id.dtpDOB);
            }
            dtpDate.setText(new StringBuilder()
                    .append(Global.Right("00" + mDay, 2)).append("/")
                    .append(Global.Right("00" + mMonth, 2)).append("/")
                    .append(mYear));

            txtAge.setText(String.valueOf(Global.DateDifferenceYears(Global.DateNowDMY(), dtpDate.getText().toString())));
        }
    };

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
            hour = selectedHour;
            minute = selectedMinute;
            EditText tpTime;
            //tpTime.setText(new StringBuilder().append(Global.Right("00"+hour,2)).append(":").append(Global.Right("00"+minute,2)));
        }
    };


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

}