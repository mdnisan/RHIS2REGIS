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
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
 * Created by user on 18/03/2015.
 */
public class Child extends Activity {
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
        AlertDialog.Builder adb = new AlertDialog.Builder(Child.this);
        switch (item.getItemId()) {
            case R.id.menuClose:
                adb.setTitle("Close");
                adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান[হ্যাঁ/না]?");
                adb.setNegativeButton("না", null);
                adb.setPositiveButton("হ্যাঁ", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        Intent f2 = new Intent(getApplicationContext(), MemberList.class);
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
    private String TableNameDel;

    LinearLayout seclblH1;
    LinearLayout secdelete11;
    LinearLayout secDelete;
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
    TextView txtElcoNo;

    LinearLayout secName;
    TextView VlblName;
    TextView txtName;

    TextView txtAge;
    LinearLayout secOutcomeDT;
    TextView VlblOutcomeDT;
    EditText dtpOutcomeDT;
    ImageButton btnOutcomeDT;


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
    LinearLayout secBWeight;
    TextView VlblBWeight;
    EditText txtBWeight;
    CheckBox chkBWeightDontKnow;
    LinearLayout secImbirth;
    TextView VlblImbirth;
    RadioGroup rdogrpImbirth;
    RadioButton rdoImbirthYes;
    RadioButton rdoImbirthNo;

    LinearLayout secDryWrap;
    TextView VlblVSDryWrap;
    RadioGroup rdogrpVSDryWrap;
    RadioButton rdoDryWrapYes;
    RadioButton rdoDryWrapNo;

    LinearLayout secCHX;
    TextView VlblCHX;
    RadioGroup rdogrpCHX;
    RadioButton rdoCHXYes;
    RadioButton rdoCHXNo;

    LinearLayout secBfeed1;
    TextView VlblBfeed1;
    RadioGroup rdogrpBfeed1;
    RadioButton rdoBfeed1Yes;
    RadioButton rdoBfeed1No;

    LinearLayout secBath3;
    TextView VlblBath3;
    RadioGroup rdogrpBath3;
    RadioButton rdoBath3Yes;
    RadioButton rdoBath3No;

    Button cmdSave;
    ImageButton cmdSavePNC;

    String StartTime;
    private String TableNamePNC;
    LinearLayout secPNCCh12;
    TextView VlblPNCCh1;
    CheckBox ChkPNCCh1;
    EditText dtpDOPNCCh1;
    ImageButton btnDOPNCCh1;

    LinearLayout secMultipleChild;
    String pregnancyNo = "";
    String pregnancyNoDelivary = "";
    String childNo;
    LinearLayout secChildListHH;
    LinearLayout secChildList;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.child);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();

            TableName = "newBorn";
            TableNamePNC = "pncServiceChild";
            TableNameDel = "delivery";

            secMultipleChild = (LinearLayout) findViewById(R.id.secMultipleChild);
            secMultipleChild.setVisibility(View.GONE);
            secChildListHH = (LinearLayout) findViewById(R.id.secChildListHH);
            secChildList = (LinearLayout) findViewById(R.id.secChildList);

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
            } else if (g.getCallFrom().equals("HAregis")) {
                pregnancyNo = e.LastPregNumber(this, g.getGeneratedId());

            /*pregnancyNo = e.LastPregNumber(this, g.getGeneratedId());
            pregnancyNoDelivary = e.LastPregNumberFromDelivary(this, g.getGeneratedId());
            if(pregnancyNo.equalsIgnoreCase("0"))
            {
                pregnancyNo="1";
            }
            else if(pregnancyNo.equalsIgnoreCase(pregnancyNoDelivary))
            {
                pregnancyNo = e.CurrentPregNumber(this, g.getGeneratedId());
                //pregnancyNo="1";
            }
            else if(!pregnancyNo.equalsIgnoreCase(pregnancyNoDelivary))
            {
                pregnancyNo = e.LastPregNumber(this, g.getGeneratedId());
            }*/
            }
            //g.setCallFrom("regis");

            //secOutcomeDT = (LinearLayout) findViewById(R.id.secOutcomeDT);
            //VlblOutcomeDT = (TextView) findViewById(R.id.VlblOutcomeDT);
            dtpOutcomeDT = (EditText) findViewById(R.id.dtpOutcomeDT);
            //btnOutcomeDT = (ImageButton) findViewById(R.id.btnOutcomeDT);
            btnOutcomeDT = (ImageButton) findViewById(R.id.btnOutcomeDT);
            btnOutcomeDT.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btnOutcomeDT";
                    showDialog(DATE_DIALOG);
                }
            });
            secBPlace = (LinearLayout) findViewById(R.id.secBPlace);
            VlblBPlace = (TextView) findViewById(R.id.VlblBPlace);
            spnBPlace = (Spinner) findViewById(R.id.spnBPlace);
            secBType = (LinearLayout) findViewById(R.id.secBType);
            VlblBType = (TextView) findViewById(R.id.VlblBType);
            rdogrpBType = (RadioGroup) findViewById(R.id.rdogrpBType);
            rdoBTypeNor = (RadioButton) findViewById(R.id.rdoBTypeNor);
            rdoBTypeSeg = (RadioButton) findViewById(R.id.rdoBTypeSeg);

            List<String> listBPlace = new ArrayList<String>();

            listBPlace.add("");
            listBPlace.add("01-বাড়িতে");
            listBPlace.add("02-উপজেলা স্বাস্থ্য কমপ্লেক্স");
            listBPlace.add("03-ইউনিয়ন স্বাস্থ্য ও পরিবার কল্যাণ কেন্দ্র");
            listBPlace.add("04-মা ও শিশু কল্যাণ কেন্দ্র");
            listBPlace.add("05-জেলা সদর ও অন্যান্য সরকারী হাসপাতাল");
            listBPlace.add("06-এনজিও ক্লিনিক বা হাসপাতাল");
            listBPlace.add("07-প্রাইভেট ক্লিনিক বা হাসপাতাল");
            listBPlace.add("77-অন্যান্য");

            ArrayAdapter<String> BPlace = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listBPlace);
            spnBPlace.setAdapter(BPlace);
            secBAtten = (LinearLayout) findViewById(R.id.secBAtten);
            VlblBAtten = (TextView) findViewById(R.id.VlblBAtten);
            spnBAtten = (Spinner) findViewById(R.id.spnBAtten);

            spnBPlace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    String val = (spnBPlace.getSelectedItemPosition() == 0 ? "" : Global.Left(spnBPlace.getSelectedItem().toString(), 2));
                    if (val.length() > 0) {
                        spnBAtten.setAdapter(null);
                        if (val.equalsIgnoreCase("01")) {
                            rdoBTypeNor.setChecked(true);
                            rdoBTypeSeg.setEnabled(false);
                            spnBAtten.setAdapter(C.getArrayAdapter("Select '  'as attendantCode union Select substr('0' ||attendantCode, -2, 2)||'-'||attendantDesig as attendantCode from AttendantDesignation  order by attendantCode asc"));
                            spnBAtten.setSelection(Global.SpinnerItemPosition(spnBAtten, 2, C.ReturnSingleValue("Select  ifnull(attendantDesignation,'') as attendantDesignation from  newBorn where  cast(HealthId as longint)='" + g.getGeneratedId() + "' AND Pregno='" + pregnancyNo + "' AND ChildNo='" + childNo + "'")));
                        } else {
                            rdogrpBType.clearCheck();
                            rdoBTypeNor.setEnabled(true);
                            rdoBTypeSeg.setEnabled(true);
                            spnBAtten.setAdapter(C.getArrayAdapter("Select '  'as attendantCode union Select substr('0' ||attendantCode, -2, 2)||'-'||attendantDesig as attendantCode from AttendantDesignation where substr('0' ||attendantCode, -2, 2)  in('01','02','03','04','05','06','77') order by attendantCode asc"));
                            spnBAtten.setSelection(Global.SpinnerItemPosition(spnBAtten, 2, C.ReturnSingleValue("Select  ifnull(attendantDesignation,'') as attendantDesignation from  newBorn where  cast(HealthId as longint)='" + g.getGeneratedId() + "' AND Pregno='" + pregnancyNo + "' AND ChildNo='" + childNo + "'")));
                        }


                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

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


            secBWeight = (LinearLayout) findViewById(R.id.secBWeight);
            VlblBWeight = (TextView) findViewById(R.id.VlblBWeight);
            txtBWeight = (EditText) findViewById(R.id.txtBWeight);
            chkBWeightDontKnow = (CheckBox) findViewById(R.id.chkBWeightDontKnow);

            chkBWeightDontKnow.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (((CheckBox) v).isChecked()) {
                        txtBWeight.setText("");
                        txtBWeight.setEnabled(false);
                    } else {
                        txtBWeight.setEnabled(true);
                    }
                }
            });

            txtBWeight.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (txtBWeight.getText().length() > 0) {
                        String a = txtBWeight.getText().toString();
                        Float Val = Float.parseFloat(a.length() == 0 ? "0" : a);
                        LoadWeight();
                   /* if(Val<=1.6 & Val!=0)
                    {
                        VlblBWeight.setText("");
                        VlblBWeight.setText(Html.fromHtml(VlblBWeight.getText() + "<font color=red>জন্মের সময় ওজন (কেজি)</font>"));
                    }
                    else if((Val>=1.7 & Val<=2.4) & Val!=0)
                    {
                        VlblBWeight.setText("");
                        VlblBWeight.setText(Html.fromHtml(VlblBWeight.getText() + "<font color=yellow>জন্মের সময় ওজন (কেজি)</font>"));
                    }
                    else if(Val>=2.5)
                    {
                        VlblBWeight.setText("");
                        VlblBWeight.setText(Html.fromHtml(VlblBWeight.getText() + "<font color=black>জন্মের সময় ওজন (কেজি)</font>"));
                    }
                    else
                    {
                        VlblBWeight.setText("");
                        VlblBWeight.setText(Html.fromHtml(VlblBWeight.getText() + "<font color=black>জন্মের সময় ওজন (কেজি)</font>"));
                    }*/
                    }
                }
            });


            secImbirth = (LinearLayout) findViewById(R.id.secImbirth);
            VlblImbirth = (TextView) findViewById(R.id.VlblImbirth);
            rdogrpImbirth = (RadioGroup) findViewById(R.id.rdogrpImbirth);
            rdoImbirthYes = (RadioButton) findViewById(R.id.rdoImbirthYes);
            rdoImbirthNo = (RadioButton) findViewById(R.id.rdoImbirthNo);

            secDryWrap = (LinearLayout) findViewById(R.id.secDryWrap);
            VlblVSDryWrap = (TextView) findViewById(R.id.VlblVSDryWrap);
            rdogrpVSDryWrap = (RadioGroup) findViewById(R.id.rdogrpVSDryWrap);
            rdoDryWrapYes = (RadioButton) findViewById(R.id.rdoDryWrapYes);
            rdoDryWrapNo = (RadioButton) findViewById(R.id.rdoDryWrapNo);


            secCHX = (LinearLayout) findViewById(R.id.secCHX);
            VlblCHX = (TextView) findViewById(R.id.VlblCHX);
            rdogrpCHX = (RadioGroup) findViewById(R.id.rdogrpCHX);
            rdoCHXYes = (RadioButton) findViewById(R.id.rdoCHXYes);
            rdoCHXNo = (RadioButton) findViewById(R.id.rdoCHXNo);

            secBfeed1 = (LinearLayout) findViewById(R.id.secBfeed1);
            VlblBfeed1 = (TextView) findViewById(R.id.VlblBfeed1);
            rdogrpBfeed1 = (RadioGroup) findViewById(R.id.rdogrpBfeed1);
            rdoBfeed1Yes = (RadioButton) findViewById(R.id.rdoBfeed1Yes);
            rdoBfeed1No = (RadioButton) findViewById(R.id.rdoBfeed1No);

            secBath3 = (LinearLayout) findViewById(R.id.secBath3);
            VlblBath3 = (TextView) findViewById(R.id.VlblBath3);
            rdogrpBath3 = (RadioGroup) findViewById(R.id.rdogrpBath3);
            rdoBath3Yes = (RadioButton) findViewById(R.id.rdoBath3Yes);
            rdoBath3No = (RadioButton) findViewById(R.id.rdoBath3No);


        /*if(GetLiveBirth().equals("2"))
        {
            secMultipleChild.setVisibility(View.VISIBLE);
        }

        else
        {
            secMultipleChild.setVisibility(View.GONE);
            dtpOutcomeDT.setText("");
            spnBPlace.setSelection(0);
            spnBAtten.setSelection(0);
            rdogrpBType.clearCheck();
        }*/


            //DataSearch(g.getGeneratedId());
            //String pregNo=PregMaxPGNNo();
            if ((C.ReturnSingleValue("Select count(*)total from newBorn where cast(healthid as bigint)=" + g.getGeneratedId() + " AND pregNo = '" + pregnancyNo + "'")).equals("1")) {
                childNo = "1";
                g.setChildNo("1");
                //DisplayChildlist();
                secChildList.setVisibility(View.GONE);
                secMultipleChild.setVisibility(View.GONE);
                secBWeight.setVisibility(View.VISIBLE);
                txtBWeight.setVisibility(View.VISIBLE);
                secImbirth.setVisibility(View.VISIBLE);
                secDryWrap.setVisibility(View.VISIBLE);
                secCHX.setVisibility(View.VISIBLE);
                secBfeed1.setVisibility(View.VISIBLE);
                secBath3.setVisibility(View.VISIBLE);
                DataSearch(g.getGeneratedId(), childNo, pregnancyNo);
            } else {
                //childNo = "1";
                DisplayChildlist();
                secChildList.setVisibility(View.VISIBLE);
                secMultipleChild.setVisibility(View.GONE);
                secBWeight.setVisibility(View.GONE);
                txtBWeight.setVisibility(View.GONE);
                secImbirth.setVisibility(View.GONE);
                secDryWrap.setVisibility(View.GONE);
                secCHX.setVisibility(View.GONE);
                secBfeed1.setVisibility(View.GONE);
                secBath3.setVisibility(View.GONE);
            }

            //DataSearch(g.getGeneratedId(), childNo, pregnancyNo);
            LoadWeight();
            cmdSave = (Button) findViewById(R.id.cmdSave);

            cmdSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    DataSave();
                }
            });


        } catch (Exception e) {
            Connection.MessageBox(Child.this, e.getMessage());
            return;
        }
    }

    private String PGNNo() {
        String SQL = "";
        SQL = "Select pregNo  from PregWomen";
        SQL += " where HealthId='" + g.getGeneratedId() + "'";

        String PGNNo = Global.Right(("00" + C.ReturnSingleValue(SQL)), 2);

        return PGNNo;
    }

    private void LoadWeight() {
        String a = txtBWeight.getText().toString();
        Float Val = Float.parseFloat(a.length() == 0 ? "0" : a);
        if (Val != 0 & Val <= 1.7) {
            VlblBWeight.setText("");
            VlblBWeight.setText(Html.fromHtml(VlblBWeight.getText() + "<font color=red>জন্মের সময় ওজন (কেজি)</font>"));
        } else if (Val != 0 & (Val <= 2.4 & Val >= 1.8)) {
            VlblBWeight.setText("");
            VlblBWeight.setText(Html.fromHtml(VlblBWeight.getText() + "<font color=yellow>জন্মের সময় ওজন (কেজি)</font>"));
        } else if (Val >= 2.5) {
            VlblBWeight.setText("");
            VlblBWeight.setText(Html.fromHtml(VlblBWeight.getText() + "<font color=black>জন্মের সময় ওজন (কেজি)</font>"));
        }
    }

    private int Get37Weeks() {
        String sq = String.format("Select outcomeDate from delivery where healthId = '%s' and pregNo = '%s'", g.getGeneratedId(), PGNNo());
        if (C.Existence(sq)) {
            String LMPdate = C.ReturnSingleValue("Select LMP from pregWomen where healthId = '" + g.getGeneratedId() + "' AND pregNo = '" + PGNNo() + "'");
            String deldate = C.ReturnSingleValue("Select outcomeDate from delivery where healthId = '" + g.getGeneratedId() + "' AND pregNo = '" + PGNNo() + "'");

            //return Global.DateDifferenceDays(Global.DateConvertDMY(LMPdate), Global.DateConvertDMY(deldate));
            return Global.DateDifferenceDays(Global.DateConvertDMY(deldate), Global.DateConvertDMY(LMPdate));
            //return Global.DateDifferenceDays(Global.DateNowDMY(), Global.DateConvertDMY(deldate));
        }
        return 0;
    }

    private String GetLiveBirth() {
        String sq = String.format("Select liveBirth from delivery where healthId = '%s' and pregNo = '%s'", g.getGeneratedId(), PGNNo());
        if (C.Existence(sq)) {
            String LBirth = C.ReturnSingleValue("Select liveBirth from delivery where healthId = '" + g.getGeneratedId() + "' AND pregNo = '" + PGNNo() + "'");

            return LBirth;
        }
        return sq;
    }

    private String GetStillBirth() {
        String sq = String.format("Select stillBirth from delivery where healthId = '%s' and pregNo = '%s'", g.getGeneratedId(), PGNNo());
        if (C.Existence(sq)) {
            String LBirth = C.ReturnSingleValue("Select liveBirth from delivery where healthId = '" + g.getGeneratedId() + "' AND pregNo = '" + PGNNo() + "'");

            return LBirth;
        }
        return sq;
    }

/*private String ChildSlNo()
{
    String SQL = "";
    SQL = "Select (ifnull(max(cast(ChildSl as int)),0)+1)MaxChildSl from Child";
    SQL += " where";
    SQL += " dist='" + g.getDistrict() + "' and upz='" + g.getUpazila() + "' and un='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and SNo='" + txtSNo.getText() + "'";
    String ChildSlNo = Global.Right(("00"+ C.ReturnSingleValue(SQL)),2);
    return ChildSlNo;
}

private String PGNNo()
{
    String SQL = "";
    SQL = "Select PGN  from PregWomen";
    SQL += " where";
    SQL += " dist='" + g.getDistrict() + "' and upz='" + g.getUpazila() + "' and un='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and SNo='" + txtSNo.getText() + "'";

    String PGNNo = Global.Right(("00"+ C.ReturnSingleValue(SQL)),2);

    return PGNNo;
}*/

    private void DataSave() {
        try {

       /* if(dtpOutcomeDT.getText().length()==0 & secOutcomeDT.isShown())
        {
            Connection.MessageBox(Child.this, "প্রসব/গর্ভপাতের তারিখ  সিলেক্ট করুন।");
            dtpOutcomeDT.requestFocus();
            return;
        }*/

            if (spnBPlace.getSelectedItemPosition() == 0 & secBPlace.isShown()) {
                Connection.MessageBox(Child.this, "কোথায় প্রসব হয়েছে  সিলেক্ট করুন।");
                spnBPlace.requestFocus();
                return;
            } else if (spnBAtten.getSelectedItemPosition() == 0 & secBAtten.isShown()) {
                Connection.MessageBox(Child.this, "কে প্রসব করিয়েছেন  সিলেক্ট করুন।");
                spnBAtten.requestFocus();
                return;
            } else if (!rdoBTypeNor.isChecked() & !rdoBTypeSeg.isChecked() & secBAtten.isShown()) {
                Connection.MessageBox(Child.this, "প্রসবের ধরণ  সিলেক্ট করুন।");
                rdoBTypeNor.requestFocus();
                return;
            } else if (!chkBWeightDontKnow.isChecked()) {
                if (txtBWeight.getText().toString().length() == 0) {
                    Connection.MessageBox(Child.this, "জন্মের সময় ওজন (কেজি) কত ছিল ।");
                    txtBWeight.requestFocus();
                    return;
                }
            } else if (!rdoImbirthYes.isChecked() & !rdoImbirthNo.isChecked()) {
                Connection.MessageBox(Child.this, "অপরিনত জন্ম (৩৭ সপ্তাহের পূর্বে )  সিলেক্ট করুন।");
                rdoImbirthYes.requestFocus();
                return;
            } else if (!rdoDryWrapYes.isChecked() & !rdoDryWrapNo.isChecked()) {
                Connection.MessageBox(Child.this, "জন্মের পর পরই শুষ্ক ও পরিস্কার কাপড় দিয়ে মুছানো ও মোড়ানো হয়েছে কি না  সিলেক্ট করুন।");
                rdoDryWrapYes.requestFocus();
                return;
            } else if (!rdoCHXYes.isChecked() & !rdoCHXNo.isChecked()) {
                Connection.MessageBox(Child.this, "নাড়ি কাটার পর ৭.১% ক্লোরহেক্সিডিন ব্যবহার করা হয়েছে কি না  সিলেক্ট করুন।");
                rdoCHXYes.requestFocus();
                return;
            } else if (!rdoBfeed1Yes.isChecked() & !rdoBfeed1No.isChecked()) {
                Connection.MessageBox(Child.this, "জন্মের ১ ঘন্টার মধ্যে বুকের দুধ খাওয়ানো হয়েছে কি না সিলেক্ট করুন।");
                rdoBfeed1Yes.requestFocus();
                return;
            } else if (!rdoBath3Yes.isChecked() & !rdoBath3No.isChecked()) {
                Connection.MessageBox(Child.this, "জন্মের প্রথম ৩ দিন গোসল থেকে বিরত রাখা হয়েছে কি না  সিলেক্ট করুন।");
                rdoBath3Yes.requestFocus();
                return;
            }

            String SQL = "";


            SQL = "Update " + TableName + " Set upload='2',";
            SQL += "providerId = '" + g.getUserID() + "',";

            SQL += "outcomeDate = '" + Global.DateConvertYMD(dtpOutcomeDT.getText().toString()) + "',";
            //SQL+="outcomeTime =  '"+ Global.DateTimeNowYMDHMS()+"',";
            SQL += "outcomePlace = '" + (spnBPlace.getSelectedItemPosition() == 0 ? "" : Integer.parseInt(Global.Left(spnBPlace.getSelectedItem().toString(), 2))) + "',";
            SQL += "attendantDesignation = '" + (spnBAtten.getSelectedItemPosition() == 0 ? "" : Global.Left(spnBAtten.getSelectedItem().toString(), 2)) + "',";
            SQL += "outcomeType = '" + (rdoBTypeNor.isChecked() ? "1" : "2") + "',";
            SQL += "birthWeightStatus = '" + (chkBWeightDontKnow.isChecked() ? "1" : "2") + "',";
            SQL += "birthWeight = '" + txtBWeight.getText().toString() + "',";
            SQL += "immatureBirth = '" + ((rdoImbirthYes.isChecked() ? "1" : "2")) + "',";
            SQL += "dryingAfterBirth = '" + ((rdoDryWrapYes.isChecked() ? "1" : "2")) + "',";
            SQL += "chlorehexidin = '" + ((rdoCHXYes.isChecked() ? "1" : "2")) + "',";
            //SQL+="skinTouch = '',";
            SQL += "breastFeed = '" + ((rdoBfeed1Yes.isChecked() ? "1" : "2")) + "',";
            SQL += "bathThreeDays = '" + ((rdoBath3Yes.isChecked() ? "1" : "2")) + "',";
            SQL += "modifyDate = '" + Global.DateTimeNowYMDHMS() + "'";
            SQL += " Where HealthID='" + g.getGeneratedId() + "' and pregNo='" + pregnancyNo + "' and childNo='" + g.getChildNo() + "'";//childNo
            C.Save(SQL);

            Connection.MessageBox(Child.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
        } catch (Exception e) {
            Connection.MessageBox(Child.this, e.getMessage());
            return;
        }
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

    public void DataSearch(String HealthId) {
        try {
            String SQL = "";
            SQL = "Select Dist, Upz, UN, Mouza, Vill, HHNo, SNo as SNo, ifnull(HealthID,'') as HealthID, ifnull(NameEng,'') as NameEng," +
                    "ifnull(NameBang,'') as NameBang, ifnull(Rth,'') as Rth, ifnull(HaveNID,'') as HaveNID, ifnull(NID,'') as NID, ifnull(NIDStatus,'') as NIDStatus, ifnull(HaveBR,'') as HaveBR, ifnull(BRID,'') as BRID, ifnull(BRIDStatus,'') as BRIDStatus, ifnull(MobileNo1,'') as MobileNo1," +
                    "ifnull(MobileNo2,'') as MobileNo2, ifnull(DOB,'') as DOB, ifnull(Age,'') as Age, ifnull(DOBSource,'') as DOBSource, ifnull(BPlace,'') as BPlace, ifnull(FNo,'') as FNo, ifnull(Father,'') as Father, ifnull(MNo,'') as MNo, ifnull(Mother,'') as Mother," +
                    "ifnull(Sex,'') as Sex, ifnull(MS,'') as MS, ifnull(SPNO1,'') as SPNO1,ifnull(SPNO2,'') as SPNO2,ifnull(SPNO3,'') " +
                    "as SPNO3,ifnull(SPNO4,'') as SPNO4, ifnull(ELCONo,'') as ELCONo, ifnull(ELCODontKnow,'') as ELCODontKnow, " +
                    "ifnull(EDU,'') as EDU, ifnull(Rel,'') as Rel, ifnull(Nationality,'') as Nationality, ifnull(OCP,'') as OCP," +
                    "(select NameEng  from member where  HealthId='" + g.getHealthID() + "' and SNo=(select  SPNO1  from member  Where   HealthId='" + g.getHealthID() + "'" + " ))as HusName " +
                    " from Member Where  HealthId='" + g.getHealthID() + "'";
        /*SQL = "Select Dist, Upz, UN, Mouza, Vill, HHNo, SNo as SNo, ifnull(HealthID,'') as HealthID, ifnull(NameEng,'') as NameEng,";
        SQL += " ifnull(NameBang,'') as NameBang, ifnull(Rth,'') as Rth, ifnull(HaveNID,'') as HaveNID, ifnull(NID,'') as NID, ifnull(NIDStatus,'') as NIDStatus, ifnull(HaveBR,'') as HaveBR, ifnull(BRID,'') as BRID, ifnull(BRIDStatus,'') as BRIDStatus, ifnull(MobileNo1,'') as MobileNo1,";
        SQL += " ifnull(MobileNo2,'') as MobileNo2, ifnull(DOB,'') as DOB, ifnull(Age,'') as Age, ifnull(DOBSource,'') as DOBSource, ifnull(BPlace,'') as BPlace, ifnull(FNo,'') as FNo, ifnull(Father,'') as Father, ifnull(MNo,'') as MNo, ifnull(Mother,'') as Mother,";
        SQL += " ifnull(Sex,'') as Sex, ifnull(MS,'') as MS, ifnull(SPNO1,'') as SPNO1,ifnull(SPNO2,'') as SPNO2,ifnull(SPNO3,'') as SPNO3,ifnull(SPNO4,'') as SPNO4, ifnull(ELCONo,'') as ELCONo, ifnull(ELCODontKnow,'') as ELCODontKnow, ifnull(EDU,'') as EDU, ifnull(Rel,'') as Rel, ifnull(Nationality,'') as Nationality, ifnull(OCP,'') as OCP";
        SQL += " from Member Where Dist='"+ Dist +"' and Upz='"+ Upz +"' and UN='"+ UN +"' and Mouza='"+ Mouza +"' and Vill='"+ Vill +"' and HHNo='"+ HHNo +"' and SNo='"+ SNo +"'";*/
            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                txtHealthID.setText(cur.getString(cur.getColumnIndex("HealthID")));
                txtSNo.setText(cur.getString(cur.getColumnIndex("SNo")));
                txtName.setText(cur.getString(cur.getColumnIndex("NameEng")));
                txtAge.setText(cur.getString(cur.getColumnIndex("Age")));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(Child.this, e.getMessage());
            return;
        }
    }

    public void DataSearch(String HealthId, String ChildId, String PregNo) {
        try {
            String SQL = "";
            SQL = "Select healthId, pregNo, childNo, providerId, childHealthId, birthStatus, gender,  outcomePlace,attendantDesignation, outcomeDate,  outcomeTime,  outcomeType," +
                    "ifnull(birthWeightStatus,'') as birthWeightStatus, birthWeight,immatureBirth, dryingAfterBirth, resassitation, stimulation,  bagNMask, chlorehexidin, skinTouch, breastFeed, bathThreeDays," +
                    "refer, referReason,  referCenterName,  systemEntryDate,upload,modifyDate from newBorn where  cast(HealthId as longint)='" + HealthId + "' AND Pregno='"
                    + PregNo + "' AND ChildNo='" + ChildId + "'";


            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                //dtpOutcomeDT.setText("");
                spnBPlace.setSelection(0);
                spnBAtten.setSelection(0);
                rdogrpBType.clearCheck();
                txtBWeight.setText("");
                rdogrpImbirth.clearCheck();
                rdogrpVSDryWrap.clearCheck();
                rdogrpCHX.clearCheck();
                rdogrpBfeed1.clearCheck();
                rdogrpBath3.clearCheck();

                dtpOutcomeDT.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("outcomeDate"))));
                //spnBPlace.setSelection(Global.SpinnerItemPosition(spnBPlace, 1 ,cur.getString(cur.getColumnIndex("outcomePlace"))));
                if (cur.getString(cur.getColumnIndex("outcomePlace")).length() >= 2) {
                    Global.SetSpinnerItem(spnBPlace, cur.getString(cur.getColumnIndex("outcomePlace")));
                } else {
                    Global.SetSpinnerItem(spnBPlace, "0" + cur.getString(cur.getColumnIndex("outcomePlace")));
                }
                if (cur.getString(cur.getColumnIndex("attendantDesignation")) != null & cur.getString(cur.getColumnIndex("attendantDesignation")) != "null") {
                    if (cur.getString(cur.getColumnIndex("attendantDesignation")).length() >= 2) {
                        Global.SetSpinnerItem(spnBAtten, cur.getString(cur.getColumnIndex("attendantDesignation")));
                        //spnBAtten.setSelection(Global.SpinnerItemPosition(spnBAtten, 1, C.ReturnSingleValue("Select  ifnull(attendantDesignation,'') as attendantDesignation from  newBorn where  cast(HealthId as longint)='" + g.getGeneratedId() +"' AND Pregno='"+ pregnancyNo +"' AND ChildNo='" + childNo +"'")));
                    } else {
                        Global.SetSpinnerItem(spnBAtten, "0" + cur.getString(cur.getColumnIndex("attendantDesignation")));
                        //spnBAtten.setSelection(Global.SpinnerItemPosition(spnBAtten, 2, C.ReturnSingleValue("Select  ifnull(attendantDesignation,'') as attendantDesignation from  newBorn where  cast(HealthId as longint)='" + g.getGeneratedId() +"' AND Pregno='"+ pregnancyNo +"' AND ChildNo='" + childNo +"'")));
                    }
                }
                //Global.SetSpinnerItem(spnBAtten, cur.getString(cur.getColumnIndex("attendantDesignation")));
            /*if(cur.getString(cur.getColumnIndex("attendantDesignation"))!=null & cur.getString(cur.getColumnIndex("attendantDesignation"))!="null") {
                if(cur.getString(cur.getColumnIndex("attendantDesignation")).equals("01"))
                {
                    spnBAtten.setSelection(1);
                }
                else if(cur.getString(cur.getColumnIndex("attendantDesignation")).equals("02"))
                {
                    spnBAtten.setSelection(2);
                }
                else if(cur.getString(cur.getColumnIndex("attendantDesignation")).equals("03"))
                {
                    spnBAtten.setSelection(3);
                }
                else if(cur.getString(cur.getColumnIndex("attendantDesignation")).equals("04"))
                {
                    spnBAtten.setSelection(4);
                }
                else if(cur.getString(cur.getColumnIndex("attendantDesignation")).equals("05"))
                {
                    spnBAtten.setSelection(5);
                }
                else if(cur.getString(cur.getColumnIndex("attendantDesignation")).equals("06"))
                {
                    spnBAtten.setSelection(6);
                }
                else if(cur.getString(cur.getColumnIndex("attendantDesignation")).equals("07"))
                {
                    spnBAtten.setSelection(7);
                }
                else if(cur.getString(cur.getColumnIndex("attendantDesignation")).equals("08"))
                {
                    spnBAtten.setSelection(8);
                }
                else if(cur.getString(cur.getColumnIndex("attendantDesignation")).equals("09"))
                {
                    spnBAtten.setSelection(9);
                }
                else if(cur.getString(cur.getColumnIndex("attendantDesignation")).equals("10"))
                {
                    spnBAtten.setSelection(10);
                }
                else if(cur.getString(cur.getColumnIndex("attendantDesignation")).equals("11"))
                {
                    spnBAtten.setSelection(11);
                }
                else if(cur.getString(cur.getColumnIndex("attendantDesignation")).equals("12"))
                {
                    spnBAtten.setSelection(12);
                }
                else if(cur.getString(cur.getColumnIndex("attendantDesignation")).equals("13"))
                {
                    spnBAtten.setSelection(13);
                }
                else if(cur.getString(cur.getColumnIndex("attendantDesignation")).equals("77"))
                {
                    spnBAtten.setSelection(14);
                }
                //spnBAtten.setSelection(Global.SpinnerItemPosition(spnBAtten, 2, cur.getString(cur.getColumnIndex("attendantDesignation"))));
            }*/
                if (cur.getString(cur.getColumnIndex("outcomeType")) != null) {
                    if (cur.getString(cur.getColumnIndex("outcomeType")).equalsIgnoreCase("1")) {
                        rdoBTypeNor.setChecked(true);
                    } else if (cur.getString(cur.getColumnIndex("outcomeType")).equalsIgnoreCase("2")) {
                        rdoBTypeSeg.setChecked(true);
                    }
                }


                if (cur.getString(cur.getColumnIndex("birthWeightStatus")).equals("1")) {
                    chkBWeightDontKnow.setChecked(true);
                } else if (cur.getString(cur.getColumnIndex("birthWeightStatus")).equals("2") | cur.getString(cur.getColumnIndex("birthWeightStatus")).equals("null") | cur.getString(cur.getColumnIndex("birthWeightStatus")).equals(null)) {
                    chkBWeightDontKnow.setChecked(false);
                    txtBWeight.setText(cur.getString(cur.getColumnIndex("birthWeight")));
                }
                //txtBWeight.setText(cur.getString(cur.getColumnIndex("birthWeight")));

                if (Get37Weeks() < 260) {
                    rdoImbirthYes.setChecked(true);
                    VlblImbirth.setText("");
                    VlblImbirth.setText(Html.fromHtml(VlblImbirth.getText() + "<font color=red>অপরিনত জন্ম (৩৭ সপ্তাহের পূর্বে ) </font>"));
                    rdoImbirthYes.setEnabled(false);
                    rdoImbirthNo.setEnabled(false);
                } else {
                    rdoImbirthNo.setChecked(true);
                    rdoImbirthYes.setEnabled(false);
                    rdoImbirthNo.setEnabled(false);
                    VlblImbirth.setText("");
                    VlblImbirth.setText(Html.fromHtml(VlblImbirth.getText() + "<font color=black>অপরিনত জন্ম (৩৭ সপ্তাহের পূর্বে ) </font>"));
                }
                if (cur.getString(cur.getColumnIndex("immatureBirth")) != null & cur.getString(cur.getColumnIndex("immatureBirth")) != "null") {
                    if (cur.getString(cur.getColumnIndex("immatureBirth")).equalsIgnoreCase("1")) {
                        rdoImbirthYes.setChecked(true);
                        rdoImbirthYes.setEnabled(false);
                        rdoImbirthNo.setEnabled(false);
                    } else if (cur.getString(cur.getColumnIndex("immatureBirth")).equalsIgnoreCase("2")) {
                        rdoImbirthNo.setChecked(true);
                        rdoImbirthYes.setEnabled(false);
                        rdoImbirthNo.setEnabled(false);
                    }
                }

                if (cur.getString(cur.getColumnIndex("dryingAfterBirth")) != null & cur.getString(cur.getColumnIndex("dryingAfterBirth")) != "null") {
                    if (cur.getString(cur.getColumnIndex("dryingAfterBirth")).equalsIgnoreCase("1")) {
                        rdoDryWrapYes.setChecked(true);
                    } else if (cur.getString(cur.getColumnIndex("dryingAfterBirth")).equalsIgnoreCase("2")) {
                        rdoDryWrapNo.setChecked(true);
                    }
                }

                if (cur.getString(cur.getColumnIndex("chlorehexidin")) != null & cur.getString(cur.getColumnIndex("dryingAfterBirth")) != "null") {
                    if (cur.getString(cur.getColumnIndex("chlorehexidin")).equalsIgnoreCase("1")) {
                        rdoCHXYes.setChecked(true);
                    } else if (cur.getString(cur.getColumnIndex("chlorehexidin")).equalsIgnoreCase("2")) {
                        rdoCHXNo.setChecked(true);
                    }
                }


                if (cur.getString(cur.getColumnIndex("breastFeed")) != null & cur.getString(cur.getColumnIndex("breastFeed")) != "null") {
                    if (cur.getString(cur.getColumnIndex("breastFeed")).equalsIgnoreCase("1")) {
                        rdoBfeed1Yes.setChecked(true);
                    } else if (cur.getString(cur.getColumnIndex("breastFeed")).equalsIgnoreCase("2")) {
                        rdoBfeed1No.setChecked(true);
                    }
                }


                if (cur.getString(cur.getColumnIndex("bathThreeDays")) != null & cur.getString(cur.getColumnIndex("bathThreeDays")) != "null") {
                    if (cur.getString(cur.getColumnIndex("bathThreeDays")).equalsIgnoreCase("1")) {
                        rdoBath3Yes.setChecked(true);
                    } else if (cur.getString(cur.getColumnIndex("bathThreeDays")).equalsIgnoreCase("2")) {
                        rdoBath3No.setChecked(true);
                    }
                }


                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(Child.this, e.getMessage());
            return;
        }
    }

    private void DeliverySearch(String HealthId) {
        try {
            String SQL = "";


            SQL = "Select healthId, pregNo, providerId, outcomePlace, outcomeDate, outcomeType, liveBirth, stillBirth," +
                    "stillBirthFresh, stillBirthMacerated, misoprostol, abortion , systemEntryDate, modifyDate, attendantDesignation " +
                    "FROM delivery where HealthId='" + g.getGeneratedId() + "' AND pregNo = '" + PregMaxPGNNo() + "'";//g.getPregNo()
            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();

            while (!cur.isAfterLast()) {

                Global.SetSpinnerItem(spnBAtten, cur.getString(cur.getColumnIndex("attendantDesignation")));

                cur.moveToNext();

            }
            cur.close();

        } catch (Exception e) {
            Connection.MessageBox(Child.this, e.getMessage());
            return;
        }
    }

    private boolean validateChildPNCDate() {
    /*String sq = String.format("Select outcomeDate from newBorn where healthId = '%s' and pregNo = '%s' and childNo = '%s'", g.getGeneratedId(), PGNNo(), g.getChildNo());*/
        String sq = String.format("Select outcomeDate from newBorn where healthId = '%s' and pregNo = '%s' and childNo = '%s'", g.getGeneratedId(), g.getPregNo(), g.getChildNo());
        if (C.Existence(sq)) {
        /*String outcomeDate = C.ReturnSingleValue("Select LMP from PregWomen where healthId = '" + g.getGeneratedId() + "' AND pregNo = '" + PGNNo() + "'");*/
            String outcomeDate = C.ReturnSingleValue("Select LMP from PregWomen where healthId = '" + g.getGeneratedId() + "' AND pregNo = '" + g.getPregNo() + "'");
            if (/*Global.DateDifferenceDays(Global.DateConvertDMY(outcomeDate),dtpDOPNCCh1.getText().toString())>=1
                ||*/ (Global.DateDifferenceDays(Global.DateNowDMY(), dtpDOPNCCh1.getText().toString()) <= 1)) {
                return true;
            }


        }
        return false;
    }

    private int GetDifferencedateofBirth() {

   /* String sq = String.format("Select outcomeDate from newBorn where healthId = '%s' and pregNo = '%s' and childNo = '%s'", g.getGeneratedId(), PGNNo(), g.getChildNo());*/
        String sq = String.format("Select outcomeDate from newBorn where healthId = '%s' and pregNo = '%s' and childNo = '%s'", g.getGeneratedId(), g.getPregNo(), g.getChildNo());
        if (C.Existence(sq)) {
            //String outcomeDate = C.ReturnSingleValue(String.format("Select outcomeDate from newBorn where healthId = '%s' and pregNo = '%s' and childNo = '%s'", g.getGeneratedId(), PGNNo(), g.getChildNo()));
            String outcomeDate = C.ReturnSingleValue(String.format("Select outcomeDate from newBorn where healthId = '%s' and pregNo = '%s' and childNo = '%s'", g.getGeneratedId(), g.getPregNo(), g.getChildNo()));

            return Global.DateDifferenceDays(dtpDOPNCCh1.getText().toString(), Global.DateConvertDMY(outcomeDate));
        }
        return 0;

    }


    private boolean GetDifferencedateWithSystemDate() {

        if (Global.DateDifferenceDays(dtpDOPNCCh1.getText().toString(), Global.DateNowDMY()) <= 1) {
            return true;
        }

        return false;
    }

    /*private void DataSavePNC()
    {
        try
        {
                if(dtpDOPNCCh1.getText().toString().length()==0 & dtpDOPNCCh1.isShown())
                {
                    Connection.MessageBox(Child.this, "পি এন সি  এর তারিখ কত লিখুন।");
                    dtpDOPNCCh1.requestFocus();
                    return;
                }

            if(GetDifferencedateofBirth()<0)
            {
                Connection.MessageBox(Child.this, "পি এন সি  এর ভিসিট ডেট জন্ম তারিখ থেকে বেশি হবে");
                return;
            }
           if(GetDifferencedateWithSystemDate())
            {
                Connection.MessageBox(Child.this, "পি এন সি  এর ভিসিট ডেট আজকের তারিখ থেকে বেশি হবে না");
                return;
            }

            String SQL = "";
            SQL = "select healthId, pregNo, childNo,serviceId, providerId, visitDate, serviceSource, systemEntryDate, Upload, UploadDT, modifyDate FROM pncServiceChild ";
            SQL += " WHERE healthId = '"+ g.getGeneratedId() +"' AND pregNo ='"+ g.getPregNo() +"' AND childNo='"+ g.getChildNo() +"' and serviceId='"+ GetServiceId() +"'  ORDER BY visitDate";


            String SQ = "select visitDate FROM pncServiceMother ";
            SQ += " WHERE healthId = '"+ g.getGeneratedId() +"' AND pregNo ='"+ g.getPregNo() +"' and visitDate='"+ Global.DateConvertYMD(dtpDOPNCCh1.getText().toString()) +"'";


            if(!C.Existence(SQL))// && !C.Existence(SQ))
            {
                SQL = "Insert into " + TableNamePNC + "(healthId, pregNo, childNo, serviceId, providerId, visitDate, serviceSource, systemEntryDate, Upload, UploadDT, modifyDate)Values('"+
                        g.getGeneratedId() +"','"+ g.getPregNo() +"','"+ g.getChildNo() +"','"+ GetServiceId() +"','"+ g.getProvCode() +"','"+ Global.DateConvertYMD(dtpDOPNCCh1.getText().toString()) +"','"+  "G','"+  Global.DateTimeNowYMDHMS() +"','"+ "2','"+ "','"+"')";
                C.Save(SQL);
                Connection.MessageBox(Child.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
            }

            C.Save(SQL);



        }
        catch(Exception  e)
        {
            Connection.MessageBox(Child.this, e.getMessage());
            return;
        }
    }*/
    private String GetServiceId() {
        String SQL = "";

        SQL = "select '0'||(ifnull(max(cast(serviceId as int)),0))MaxserviceId from pncServiceChild";
        SQL += " WHERE healthId = '" + g.getGeneratedId() + "' AND pregNo ='" + PregMaxPGNNo() + "' AND childNo='" + PregMaxPGNNo() + "'";//g.getPregNo()  g.getChildNo()

        String tempserviceID = C.ReturnSingleValue(SQL);


        String serviceID = String.valueOf((Integer.parseInt(tempserviceID) + 1));

        if (serviceID.equalsIgnoreCase("1")) {
            return String.valueOf(g.getGeneratedId() + g.getPregNo() + serviceID);
        } else {
            return String.valueOf(serviceID);
        }
    }

    private String GetCountSLNoNumber() {

        String SQL = "select ((cast(Count(*) as int))) as Totalno  from newBorn";
        String Val = String.valueOf(C.ReturnSingleValue(SQL));
        if (Val.equalsIgnoreCase("0")) {
            return "1";
        } else
            return Val;
    }

    /*private void PNCVisitSearch(String serviceId)
    {
        try
        {
            String SQL = "";
            SQL = "select healthId, pregNo, childNo,serviceId, providerId, visitDate, serviceSource, systemEntryDate, Upload, UploadDT, modifyDate FROM pncServiceChild ORDER BY visitDate";
            SQL += " WHERE healthId = '"+ g.getGeneratedId() +"' AND pregNo ='"+ g.getPregNo() +"' AND childNo='"+ g.getChildNo() +"' and serviceId='"+ serviceId +"'";
            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();
            while(!cur.isAfterLast())
            {
                if(!cur.getString(cur.getColumnIndex("visitDate")).equals("null"))
                {
                   dtpDOPNCCh1.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("visitDate"))));
                }

                cur.moveToNext();
            }
            cur.close();
        }
        catch(Exception  e)
        {
            Connection.MessageBox(Child.this, e.getMessage());
            return;
        }
    }*/
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

            dtpDate = (EditText) findViewById(R.id.dtpOutcomeDT);

            if (VariableID.equals("btnOutcomeDT")) {
                dtpDate = (EditText) findViewById(R.id.dtpOutcomeDT);
            }
       /* if(VariableID.equals("btnDOPNCCh1"))
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

/*private void DisplayPNCVisits() {
    GridView gcount = (GridView) findViewById(R.id.gridPNC);
    g.setImuCode(String.valueOf(gcount.getCount() + 1));
    PNCVisits();
}*/
/*public void PNCVisits() {
    GridView g1 = (GridView) findViewById(R.id.gridPNC);
    g1.setAdapter(new PNC(this));
    g1.setNumColumns(6);
}*/

/*public class PNC extends BaseAdapter {
    private Context mContext;
    String[][] vcode;
    Integer totalRec;

    public PNC(Context c) {
        mContext = c;
    }

    public int getCount() {

        //String pgnpositionselected = String.valueOf(spnPgn.getSelectedItemPosition());
        // String ServiceId = serviceID(pgnpositionselected);

        return Integer.parseInt(C.ReturnSingleValue("Select count(*)total from pncServiceChild where healthid='" + g.getGeneratedId() + "' AND pregNo = '" + PregMaxPGNNo() + "'"+ " AND ChildNo = '" + PregMaxPGNNo() + "'"));//g.getPregNo() g.getChildNo()
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
            String SQL = "Select healthId, pregNo,childNo, serviceId, visitDate from pncServiceChild where healthid='" + g.getGeneratedId() + "' AND pregNo = '" + PregMaxPGNNo() + "'" + " AND ChildNo = '" + PregMaxPGNNo() + "'"+ " order by cast(visitDate as DATE) asc";//g.getPregNo() g.getChildNo()

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
                Connection.MessageBox(Child.this, ex.getMessage());
            }

        }
        return MyView;
    }

}
*/
/*private void DisplaySelectedPNCInfo(String ServiceId) {

    String SQL = "Select visitDate from pncServiceChild where serviceId = '" + ServiceId + "'";



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
    }
    catch (Exception ex) {
        Connection.MessageBox(Child.this, ex.getMessage());
    }
 }*/


    private void DisplayChildlist() {
        GridView gcount = (GridView) findViewById(R.id.gridChildlist);
        g.setImuCode(String.valueOf(gcount.getCount() + 1));
        Childlist();
    }

    public void Childlist() {
        GridView g1 = (GridView) findViewById(R.id.gridChildlist);
        g1.setAdapter(new Childlists(this));
        g1.setNumColumns(6);
    }

    public class Childlists extends BaseAdapter {
        private Context mContext;
        String[][] vcode;
        Integer totalRec;

        public Childlists(Context c) {
            mContext = c;
        }

        public int getCount() {
            return Integer.parseInt(C.ReturnSingleValue("Select count(*)total from newBorn where cast(healthid as bigint)=" + g.getGeneratedId() + " AND pregNo = '" + pregnancyNo + "'"));
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
                MyView = li.inflate(R.layout.anc_item, null);

                String SQL = "Select healthId,pregNo,childNo,ifnull(birthWeightStatus,'') as birthWeightStatus,ifnull(birthWeight,'') as birthWeight,ifnull(gender,'') as gender, ifnull(outcomeType,'') as outcomeType  from newBorn where cast(healthid as bigint)=" + g.getGeneratedId() + " AND pregNo = '" + pregnancyNo + "' order by childNo asc";

                try {
                    Cursor cur = C.ReadData(SQL);
                    cur.moveToFirst();

                    totalRec = cur.getCount();
                    vcode = new String[4][totalRec];
                    int i = 0;
                    while (!cur.isAfterLast()) {
                        /*vcode[0][i] = "শিশু " + " " + String.valueOf(cur.getString(cur.getColumnIndex("childNo")))
                                + " ওজন " + String.valueOf(cur.getString(cur.getColumnIndex("birthWeight")))
                                + " \n প্রসব ফলাফল " + String.valueOf(cur.getString(cur.getColumnIndex("outcomeType")));*/
                        vcode[0][i] = "শিশু " + " " + String.valueOf(cur.getString(cur.getColumnIndex("childNo")));
                        vcode[1][i] = String.valueOf(cur.getString(cur.getColumnIndex("healthId")));
                        vcode[2][i] = String.valueOf(cur.getString(cur.getColumnIndex("pregNo")));
                        vcode[3][i] = String.valueOf(cur.getString(cur.getColumnIndex("childNo")));

                        i += 1;
                        cur.moveToNext();
                    }
                    cur.close();

                    Button tv = (Button) MyView.findViewById(R.id.image_name);
                    tv.setTextSize(14);
                    tv.setText(vcode[0][position]);// + "\n" + vcode[1][position]);
                    final Integer p = position;

                    tv.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog.Builder adb = new AlertDialog.Builder(Child.this);


                            adb.setTitle("Close");
                            adb.setMessage("আপনি কি তথ্য বাতিল করতে  চান[হ্যাঁ/না]?");
                            adb.setNegativeButton("না", null);
                            adb.setPositiveButton("হ্যাঁ", new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String pregNo = String.valueOf(vcode[2][position]);
                                    String childNo = String.valueOf(vcode[3][position]);
                                    String dwl = "DELETE FROM newBorn WHERE healthId ='" + g.getGeneratedId() + "' and childNo='" + childNo + "' and pregNo='" + pregNo + "'";
                                    C.Save(dwl);
                                    //finish();
                                }
                            });
                            adb.show();
                            return true;


                        }
                    });
                    tv.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {

                            String pregNo = String.valueOf(vcode[2][position]);
                            String childNo = String.valueOf(vcode[3][position]);
                            g.setPregNo(pregNo);
                            g.setChildNo(childNo);
                            //comments by faz(11092015)

                           /* String var=GetLiveBirth();
                            Integer liveBirth=Integer.parseInt(var);

                            String var1=GetStillBirth();
                            Integer stillBirth=Integer.parseInt(var1);

                            if(liveBirth>=2)
                            {
                                secChildListHH.setVisibility(View.VISIBLE);
                                secMultipleChild.setVisibility(View.VISIBLE);
                                secBWeight.setVisibility(View.VISIBLE);
                                txtBWeight.setVisibility(View.VISIBLE);
                                secImbirth.setVisibility(View.VISIBLE);
                                secDryWrap.setVisibility(View.VISIBLE);
                                secCHX.setVisibility(View.VISIBLE);
                                secBfeed1.setVisibility(View.VISIBLE);
                                secBath3.setVisibility(View.VISIBLE);
                            }
                            else if(stillBirth>=2)
                            {
                                secChildListHH.setVisibility(View.VISIBLE);
                                secMultipleChild.setVisibility(View.VISIBLE);
                                secBWeight.setVisibility(View.VISIBLE);
                                txtBWeight.setVisibility(View.VISIBLE);
                                secImbirth.setVisibility(View.VISIBLE);
                                secDryWrap.setVisibility(View.VISIBLE);
                                secCHX.setVisibility(View.VISIBLE);
                                secBfeed1.setVisibility(View.VISIBLE);
                                secBath3.setVisibility(View.VISIBLE);
                            }
                            else if(liveBirth==1 & stillBirth==1)
                            {
                                secChildListHH.setVisibility(View.VISIBLE);
                                secMultipleChild.setVisibility(View.VISIBLE);
                                secBWeight.setVisibility(View.VISIBLE);
                                txtBWeight.setVisibility(View.VISIBLE);
                                secImbirth.setVisibility(View.VISIBLE);
                                secDryWrap.setVisibility(View.VISIBLE);
                                secCHX.setVisibility(View.VISIBLE);
                                secBfeed1.setVisibility(View.VISIBLE);
                                secBath3.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                secChildListHH.setVisibility(View.VISIBLE);
                                secMultipleChild.setVisibility(View.GONE);
                                dtpOutcomeDT.setText("");
                                spnBPlace.setSelection(0);
                                spnBAtten.setSelection(0);
                                rdogrpBType.clearCheck();
                                secBWeight.setVisibility(View.VISIBLE);
                                txtBWeight.setVisibility(View.VISIBLE);
                                secImbirth.setVisibility(View.VISIBLE);
                                secDryWrap.setVisibility(View.VISIBLE);
                                secCHX.setVisibility(View.VISIBLE);
                                secBfeed1.setVisibility(View.VISIBLE);
                                secBath3.setVisibility(View.VISIBLE);
                            }*/
                            /*if((C.ReturnSingleValue("Select count(*)total from newBorn where cast(healthid as bigint)=" + g.getGeneratedId() + " AND pregNo = '" + pregnancyNo + "'")).equals("1"))
                            {

                            }
                            else {*/
                            secMultipleChild.setVisibility(View.VISIBLE);
                            secBWeight.setVisibility(View.VISIBLE);
                            txtBWeight.setVisibility(View.VISIBLE);
                            secImbirth.setVisibility(View.VISIBLE);
                            secDryWrap.setVisibility(View.VISIBLE);
                            secCHX.setVisibility(View.VISIBLE);
                            secBfeed1.setVisibility(View.VISIBLE);
                            secBath3.setVisibility(View.VISIBLE);
                            DataSearch(g.getGeneratedId(), childNo, pregnancyNo);
                        }

                        /*}*/
                    });
                } catch (Exception ex) {
                    Connection.MessageBox(Child.this, ex.getMessage());
                }

            }
            return MyView;
        }

    }
}