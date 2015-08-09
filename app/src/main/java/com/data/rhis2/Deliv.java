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
 * Created by user on 18/03/2015.
 */
public class Deliv extends Activity {
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
        AlertDialog.Builder adb = new AlertDialog.Builder(Deliv.this);
        switch (item.getItemId()) {
            case R.id.menuClose:
                adb.setTitle("Close");
                adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান[Yes/No]?");
                adb.setNegativeButton("No", null);
                adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
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
    private String TableNamePNC;
    private String ChildTableName;

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

    Button cmdSave;
    ImageButton cmdSavePNC;

    String StartTime;
    TextView VlblChildList;
    CheckBox chklivebirth;
    CheckBox chkdeathbirth;
    CheckBox chkabortion;
EditText dtpDOPNCCh1;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.deliv);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();

            TableName = "delivery";
            TableNamePNC = "pncServiceMother";
            ChildTableName = "newBorn";

            seclblH = (LinearLayout) findViewById(R.id.seclblH);
            lblHlblH = (TextView) findViewById(R.id.lblHlblH);
            lblHealthID = (TextView) findViewById(R.id.lblHealthID);
            txtHealthID = (TextView) findViewById(R.id.txtHealthID);

            secSl = (LinearLayout) findViewById(R.id.secSl);
            VlblSNo = (TextView) findViewById(R.id.VlblSNo);
            txtSNo = (TextView) findViewById(R.id.txtSNo);
            txtElCONo = (TextView) findViewById(R.id.txtElCONo);
            txtAge = (TextView) findViewById(R.id.txtAge);

            secName = (LinearLayout) findViewById(R.id.secName);
            VlblName = (TextView) findViewById(R.id.VlblName);
            txtName = (TextView) findViewById(R.id.txtName);

            secHusName = (LinearLayout) findViewById(R.id.secHusName);
            VlblHusName = (TextView) findViewById(R.id.VlblHusName);
            txtHusName = (TextView) findViewById(R.id.txtHusName);
            txtAgeYrHus = (TextView) findViewById(R.id.txtAgeYrHus);
            secOutcome = (LinearLayout) findViewById(R.id.secOutcome);
            VlblOutcome = (TextView) findViewById(R.id.VlblOutcome);
            // rdogrpOutcome = (RadioGroup) findViewById(R.id.rdogrpOutcome);
            //  rdoOutcomeLB = (RadioButton) findViewById(R.id.rdoOutcomeLB);
            //  rdoOutcomeSB = (RadioButton) findViewById(R.id.rdoOutcomeSB);
            //  rdoOutcomeAbo = (RadioButton) findViewById(R.id.rdoOutcomeAbo);

            // secLivebirth=(LinearLayout)findViewById(R.id.secLivebirth);
            // secLivebirth.setVisibility( View.GONE );
            // VlblLivebirth=(TextView) findViewById(R.id.VlblLivebirth);
            txtLivebirthNum1 = (EditText) findViewById(R.id.txtLivebirthNum1);

            txtLivebirthNum1.setEnabled(false);

            txtLivebirthNum2 = (EditText) findViewById(R.id.txtLivebirthNum2);
            txtLivebirthNum2.setEnabled(false);
            VlblChildList = (TextView) findViewById(R.id.VlblChildList);

            chklivebirth = (CheckBox) findViewById(R.id.chklivebirth);
            chkdeathbirth = (CheckBox) findViewById(R.id.chkdeathbirth);
            chkabortion = (CheckBox) findViewById(R.id.chkabortion);
            dtpDOPNCCh1 = (EditText) findViewById(R.id.dtpDOPNCCh1);
            ((LinearLayout)findViewById(R.id.secPNCCh12)).setVisibility(View.GONE);
            chklivebirth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        txtLivebirthNum1.setEnabled(true);

                        txtLivebirthNum1.setText("1");
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
                        txtLivebirthNum2.setText("1");
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
                        chklivebirth.setChecked(!b);
                        chkdeathbirth.setChecked(!b);
                    } else {

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
            listBPlace.add("1-বাড়িতে");
            listBPlace.add("2-উপজেলা স্বাস্থ্য কমপ্লেক্স");
            listBPlace.add("3-ইউনিয়ন স্বাস্থ্য ও পরিবার কল্যাণ কেন্দ্র");
            listBPlace.add("4-মা ও শিশু কল্যাণ কেন্দ্র");
            listBPlace.add("5-জেলা সদর ও অন্যান্য সরকারী হাসপাতাল");
            listBPlace.add("6-এনজিও ক্লিনিক বা হাসপাতাল");
            listBPlace.add("7-প্রাইভেট ক্লিনিক বা হাসপাতাল");
            listBPlace.add("77-অন্যান্য");

            ArrayAdapter<String> BPlace = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listBPlace);
            spnBPlace.setAdapter(BPlace);

            spnBPlace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    String val =  (spnBPlace.getSelectedItemPosition() == 0 ? "" : Global.Left(spnBPlace.getSelectedItem().toString(), 1));
                    if(val.length()>0)
                    {
                        if(val.equalsIgnoreCase("1"))
                        {
                            rdoBTypeNor.setChecked(true);
                        }
                        else
                        {
                            rdogrpBType.clearCheck();
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
            List<String> listBAtten = new ArrayList<String>();

            listBAtten.add("");
            listBAtten.add("1-ডাক্তার");
            listBAtten.add("2-নার্স");
            listBAtten.add("3-স্যাকমো");
            listBAtten.add("4-এফ ডব্লিউ ভি");
            listBAtten.add("5-প্যারামেডিক্স");
            listBAtten.add("6-সি এস বি এ");
            listBAtten.add("77-অন্যান্য");

            ArrayAdapter<String> BAtten = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listBAtten);
            spnBAtten.setAdapter(BAtten);


           // VlblChildList.setVisibility(View.GONE);
            cmdSave = (Button) findViewById(R.id.cmdSave);
            cmdSavePNC = (ImageButton) findViewById(R.id.cmdSavePNC);
            ((LinearLayout)findViewById(R.id.List)).setVisibility(View.GONE);
            btnDOPNCCh1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btnDOPNCCh1";
                    showDialog(DATE_DIALOG);
                }
            });

            Bundle IDbundle = new Bundle();
            IDbundle = getIntent().getExtras();
            String currentstatus = IDbundle.getString("currentstatus");



            DataSearch(g.getHealthID());
            DeliverySearch(g.getHealthID());

            if (currentstatus.equalsIgnoreCase("13")) {
                chkabortion.setEnabled(false);
                chkdeathbirth.setEnabled(true);
                chklivebirth.setEnabled(true);
            } else if (currentstatus.equalsIgnoreCase("14")) {
                chkabortion.setEnabled(true);
                chkdeathbirth.setEnabled(false);
                chklivebirth.setEnabled(false);
            }
            ELCONoSearch(g.getDistrict(), g.getUpazila(), g.getUnion(), g.getMouza(), g.getVillage(), g.getHouseholdNo(), g.getSerialNo());
            DisplayChildlist();


           btnOutcomeDT.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btnOutcomeDT";
                    showDialog(DATE_DIALOG);
                }
            });
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            cmdSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    if (txtLivebirthNum1.getText().length() == 0 & txtLivebirthNum2.getText().length() == 0 & chkabortion.isChecked() == false) {
                        Connection.MessageBox(Deliv.this, "প্রসবের ফলাফল  সিলেক্ট করুন।");
                        //rdoOutcomeLB.requestFocus();
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
                            AlertDialog.Builder adb = new AlertDialog.Builder(Deliv.this);

                            adb.setTitle("নিশ্চিত করুন");
                            adb.setMessage("আপনি কি শিশুর সংখ্যা নিশ্চিত[Yes/No]?");
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
                    }
                }
            });

            DisplayPNCVisits();
            cmdSavePNC.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v)
                {
                    DataSavePNC();
                    DisplayPNCVisits();
                }
//test
            });

        } catch (Exception e) {
            Connection.MessageBox(Deliv.this, e.getMessage());
            return;
        }
    }



    private void setDelivery(String DeliveryType) {

    }

    private void DeliverySearch(String HealthId) {
        try {
            String SQL = "";

            SQL = "Select healthId, pregNo, providerId, outcomePlace, outcomeDate, outcomeType, liveBirth, stillBirth," +
                    "stillBirthFresh, stillBirthMacerated, misoprostol, abortion , systemEntryDate, modifyDate, attendantDesignation " +
                    "FROM delivery where HealthId='" + g.getHealthID() + "' AND pregNo = '" + PGNNo() + "'";


            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();

            while (!cur.isAfterLast()) {
                ((LinearLayout)findViewById(R.id.secPNCCh12)).setVisibility(View.VISIBLE);
                ((LinearLayout)findViewById(R.id.List)).setVisibility(View.VISIBLE);
                String outcomeType = cur.getString(cur.getColumnIndex("outcomeType"));
                if (outcomeType.equalsIgnoreCase("1")) {
                    rdoBTypeNor.setChecked(true);

                } else if (outcomeType.equalsIgnoreCase("2")) {
                    rdoBTypeSeg.setChecked(true);
                }

                txtLivebirthNum1.setText(cur.getString(cur.getColumnIndex("liveBirth")));
                txtLivebirthNum2.setText(cur.getString(cur.getColumnIndex("stillBirth")));

                if (!txtLivebirthNum1.getText().toString().equalsIgnoreCase("")) {
                    chklivebirth.setChecked(true);
                }

                if (!txtLivebirthNum2.getText().toString().equalsIgnoreCase("")) {
                    chkdeathbirth.setChecked(true);
                }
                /*if(!txtLivebirthNum3.getText().toString().equalsIgnoreCase(""))
                {
                    chkdeathbirth.setChecked(true);
                }*/

                dtpOutcomeDT.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("outcomeDate"))));
                Global.SetSpinnerItem(spnBPlace, cur.getString(cur.getColumnIndex("outcomePlace")));
                Global.SetSpinnerItem(spnBAtten, cur.getString(cur.getColumnIndex("attendantDesignation")));
                //spnBPlace.setSelection(Integer.parseInt(cur.getString(cur.getColumnIndex("outcomePlace"))));
               // spnBAtten.setSelection(Integer.parseInt(cur.getString(cur.getColumnIndex("attendantDesignation"))));
                String misoprostol = cur.getString(cur.getColumnIndex("misoprostol"));

                if (misoprostol.equalsIgnoreCase("1")) {
                    rdoMisoYes.setChecked(true);

                } else if (outcomeType.equalsIgnoreCase("2")) {
                    rdoMisoYes.setChecked(true);
                }

                cur.moveToNext();
                cmdSave.setVisibility(View.GONE);
            }
            cur.close();

        } catch (Exception e) {
            Connection.MessageBox(Deliv.this, e.getMessage());
            return;
        }
    }

    private String PGNNo() {
        String SQL = "";
        SQL = "Select pregNo  from PregWomen";
        SQL += " where HealthId='" + g.getHealthID() + "'";

        String PGNNo = Global.Right(("00" + C.ReturnSingleValue(SQL)), 2);

        return PGNNo;
    }





    private boolean validateDeliveryDate() {
        String sq = String.format("Select LMP from PregWomen where healthId = '%s' and pregNo = '%s'", g.getHealthID(), PGNNo());
        if (C.Existence(sq)) {
            String LmpDate = C.ReturnSingleValue("Select LMP from PregWomen where healthId = '" + g.getHealthID() + "' AND pregNo = '" + PGNNo() + "'");

            if (Global.DateDifferenceDays(dtpOutcomeDT.getText().toString(),Global.DateConvertDMY(LmpDate)) < 1 ) {
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
        String sq = String.format("Select LMP from PregWomen where healthId = '%s' and pregNo = '%s'", g.getHealthID(), PGNNo());
        if (C.Existence(sq)) {
            String LmpDate = C.ReturnSingleValue("Select LMP from PregWomen where healthId = '" + g.getHealthID() + "' AND pregNo = '" + PGNNo() + "'");

            if (Global.DateDifferenceDays(dtpOutcomeDT.getText().toString(), Global.DateConvertDMY(LmpDate)) < 168) {
                return true;
            }
        }
        return false;
    }


    private void DataSave() {
        try {
            if (txtLivebirthNum1.getText().length() == 0 & txtLivebirthNum2.getText().length() == 0 & chkabortion.isChecked() == false) {
                Connection.MessageBox(Deliv.this, "প্রসবের ফলাফল  সিলেক্ট করুন।");
               // rdoOutcomeLB.requestFocus();
                return;
            }
           // if(!ConfirmIfChildCountIsMoreThanTwo())
           // {
           //     Connection.MessageBox(Deliv.this, "শিশুর সঠিক সংখ্যার চয়ন করুন");
           //     return;
           // }


            /*if(rdoOutcomeLB.isChecked())
            {
                if(txtLivebirthNum.getText().length()==0) {
                    Connection.MessageBox(Deliv.this, "এই গর্ভে কয়টি বাচ্চা ছিল লিখুন।");
                    txtLivebirthNum.requestFocus();
                    return;
                }
            }*/
            if (dtpOutcomeDT.getText().length() == 0) {
                Connection.MessageBox(Deliv.this, "ফলাফলের তারিখ কত লিখুন।");
                dtpOutcomeDT.requestFocus();
                return;
            } else if (spnBPlace.getSelectedItemPosition() == 0) {
                Connection.MessageBox(Deliv.this, "প্রসবের স্থান কোথায় ছিল লিখুন");
                spnBPlace.requestFocus();
                return;
            } else if (spnBAtten.getSelectedItemPosition() == 0) {
                Connection.MessageBox(Deliv.this, "কে প্রসব করিয়েছে লিখুন।");
                secBAtten.requestFocus();
                return;
            } else if (!rdoBTypeSeg.isChecked() & !rdoBTypeNor.isChecked()) {
                Connection.MessageBox(Deliv.this, "প্রসবের ধরণ কি ছিল লিখুন।");
                rdoBTypeSeg.requestFocus();
                return;
            } else if (!rdoMisoYes.isChecked() & !rdoMisoNo.isChecked()) {
                Connection.MessageBox(Deliv.this, "মিসোপ্রোস্টল বড়ি খেয়েছে কি না  লিখুন");
                rdoMisoYes.requestFocus();
                return;
            }

            if(chklivebirth.isChecked()) {
                if (validateLMPDateIsMoreThan24Weeks()) {
                    Connection.MessageBox(Deliv.this, "প্রসব/গর্ভপাত, শেষ মাসিকের তারিখ হতে ২৪ সপ্তাহ এর বেশি  হবে");
                    return;
                }
            }

            if(validateDeliveryDate())
            {
                Connection.MessageBox(Deliv.this, "প্রসব/গর্ভপাত, শেষ মাসিকের তারিখ হতে বেশি  হবে");
                return;
            }

            if(validateDeliveryDateAgainstSystemDate())
            {
                Connection.MessageBox(Deliv.this, "প্রসব/গর্ভপাত, আজকের তারিখ হতে বেশি  হবে");
                return;
            }
            String SQL = "";
            String sq = String.format("Select healthId, pregNo from PregWomen where healthId = '%s' and pregNo = '%s'", g.getHealthID(), PGNNo());
            String sqdel = String.format("Select healthId, pregNo from delivery where healthId = '%s' and pregNo = '%s'", g.getHealthID(), PGNNo());
            if (C.Existence(sq) && !C.Existence(sqdel))//"Select Dist,Upz,UN,Mouza,Vill,HHNo,SNo from " + TableName + "  Where Dist='"+ g.getDistrict() +"' and Upz='"+ g.getUpazila() +"' and UN='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and Vill='"+ g.getVillage() +"'  and ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"' and  HHNo='"+ g.getHouseholdNo() +"' and SNo='"+ txtSNo.getText().toString() +"' and PGN = '"+ PGNNo() +"'"))
            {
                String NumberofBirth1 = "";
                String NumberofBirth2 = "";
                String NumberofBirth3 = "";
                NumberofBirth1 = txtLivebirthNum1.getText().toString();
                NumberofBirth2 = txtLivebirthNum2.getText().toString();
                if (chkabortion.isChecked()) {
                    NumberofBirth3 = "1";
                } else {
                    NumberofBirth3 = "0";
                }

                SQL = "Insert into " + TableName + "(healthId,pregNo,providerId,outcomePlace,outcomeDate,outcomeType,attendantDesignation,liveBirth,stillBirth," +
                        "abortion, systemEntryDate, Upload, misoprostol )Values" +
                        "('" + g.getHealthID() + "','" + PGNNo() + "','" + g.getProvCode() + "'" +
                        ",'" + (spnBPlace.getSelectedItemPosition() == 0 ? "" : Global.Left(spnBPlace.getSelectedItem().toString(), 1)) + "'" +
                        ",'" + Global.DateConvertYMD(dtpOutcomeDT.getText().toString()) + "'" +
                        ",'" + ((rdoBTypeNor.isChecked() ? "1" : "2")) + "'" +
                        ",'" + (spnBAtten.getSelectedItemPosition() == 0 ? "" : Global.Left(spnBAtten.getSelectedItem().toString(), 1)) + "'" +
                        ",'" + NumberofBirth1 + "'," +
                        "'" + NumberofBirth2 + "'," +
                        "'" + NumberofBirth3 + "'," +
                        "'" + Global.DateTimeNowYMDHMS() + "','2'," +
                        "'" + ((rdoMisoYes.isChecked() ? "1" : "2")) + "')";
                C.Save(SQL);
                ((LinearLayout)findViewById(R.id.List)).setVisibility(View.VISIBLE);
                ((LinearLayout)findViewById(R.id.secPNCCh12)).setVisibility(View.VISIBLE);

                cmdSave.setVisibility(View.GONE);

                ((LinearLayout)findViewById(R.id.secChildList)).setVisibility(View.VISIBLE);
                if (!NumberofBirth1.equalsIgnoreCase("")) {
                    if (Integer.parseInt(NumberofBirth1) > 0) {
                        for (int i = 0; i < Integer.parseInt(NumberofBirth1); i++) {

                            SQL = "Insert into " + ChildTableName + "(healthId,pregNo,childNo,systemEntryDate, Upload, outcomePlace," +
                                    "outcomeDate," +
                                    "outcomeTime," +
                                    "outcomeType)Values" +
                                    "('" + g.getHealthID() + "','" + PGNNo() + "','" + String.valueOf(i + 1) + "'" +
                                    ",'" + Global.DateTimeNowYMDHMS() + "','2','"
                                    + (spnBPlace.getSelectedItemPosition() == 0 ? "" : Global.Left(spnBPlace.getSelectedItem().toString(), 1)) + "','"
                                    + Global.DateConvertYMD(dtpOutcomeDT.getText().toString()) + "','" + Global.DateTimeNowYMDHMS() + "','"  + ((rdoBTypeNor.isChecked() ? "1" : "2")) + "'" +")";
                            C.Save(SQL);
                        }
                    }
                }
                if (!NumberofBirth2.equalsIgnoreCase("")) {
                    if (Integer.parseInt(NumberofBirth2) > 0) {
                        for (int i = 0; i < Integer.parseInt(NumberofBirth2); i++) {

                          /*  SQL = "Insert into " + ChildTableName + "(healthId,pregNo,childNo,systemEntryDate, Upload)Values" +
                                    "('" + g.getHealthID() + "','" + PGNNo() + "','" + String.valueOf(i + 1) + "'" +
                                    ",'" + Global.DateTimeNowYMDHMS() + "','2')";*/
                            SQL = "Insert into " + ChildTableName + "(healthId,pregNo,childNo,systemEntryDate, Upload, outcomePlace," +
                                    "outcomeDate," +
                                    "outcomeTime," +
                                    "outcomeType,)Values" +
                                    "('" + g.getHealthID() + "','" + PGNNo() + "','" + String.valueOf(i + 1) + "'" +
                                    ",'" + Global.DateTimeNowYMDHMS() + "','2','"
                                    + (spnBPlace.getSelectedItemPosition() == 0 ? "" : Global.Left(spnBPlace.getSelectedItem().toString(), 1)) + "','"
                                    + Global.DateConvertYMD(dtpOutcomeDT.getText().toString()) + "','" + Global.DateTimeNowYMDHMS() + "','"  + ((rdoBTypeNor.isChecked() ? "1" : "2")) + "'" +")";
                            C.Save(SQL);
                        }
                    }
                }
                Connection.MessageBox(Deliv.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
                DisplayChildlist();
            } else {
                Connection.MessageBox(Deliv.this, "মহিলা টি গর্ভবতী নন অথবা তথ্য সঠিক নয়");
            }




           /* SQL = "Update " + TableName + " Set ";
            SQL+="Outcome = '"+ (rdoOutcomeLB.isChecked()?"1":(rdoOutcomeSB.isChecked()?"2":"3")) +"',";
            SQL+="LBNum = '"+ txtLivebirthNum.getText().toString() +"',";
            SQL+="OutcomeDT = '"+ Global.DateConvertYMD(dtpOutcomeDT.getText().toString()) +"',";
            SQL+="BPlace = '"+ (spnBPlace.getSelectedItemPosition()==0?"":Global.Left(spnBPlace.getSelectedItem().toString(),2)) +"',";
            SQL+="BAtten = '"+ (spnBAtten.getSelectedItemPosition()==0?"":Global.Left(spnBAtten.getSelectedItem().toString(),2)) +"',";
            SQL+="BType = '"+ ((rdoBTypeNor.isChecked()?"1":"2")) +"',";
            SQL+="Miso = '"+ ((rdoMisoYes.isChecked()?"1":"2")) +"',";
            SQL+="RegDT = '"+ g.DateNowYMD() +"'";
            SQL+="  Where Dist='"+ g.getDistrict() +"' and Upz='"+ g.getUpazila() +"' and UN='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and Vill='"+ g.getVillage() +"' and HHNo='"+ g.getHouseholdNo() +"' and SNo='"+ txtSNo.getText().toString() +"' and PGN = '"+ PGNNo() +"'";*/
            //  C.Save(SQL);


            /*finish();
            if(rdoOutcomeLB.isChecked())
            {
                Intent f2 = new Intent(getApplicationContext(),Child.class);
                startActivity(f2);
            }
            else
            {
                Intent f2 = new Intent(getApplicationContext(), MemberList.class);
                startActivity(f2);
            }*/
        } catch (Exception e) {
            Connection.MessageBox(Deliv.this, e.getMessage());
            return;
        }
    }

    private void DataSearch(String healthId) {
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


         /*   SQL = "Select Dist, Upz, UN, Mouza, Vill, HHNo, SNo as SNo, ifnull(HealthID,'') as HealthID, ifnull(NameEng,'') as NameEng," +
                    "ifnull(NameBang,'') as NameBang, ifnull(Rth,'') as Rth, ifnull(HaveNID,'') as HaveNID, ifnull(NID,'') as NID, ifnull(NIDStatus,'') as NIDStatus, ifnull(HaveBR,'') as HaveBR, ifnull(BRID,'') as BRID, ifnull(BRIDStatus,'') as BRIDStatus, ifnull(MobileNo1,'') as MobileNo1," +
                    "ifnull(MobileNo2,'') as MobileNo2, ifnull(DOB,'') as DOB, ifnull(Age,'') as Age, ifnull(DOBSource,'') as DOBSource, ifnull(BPlace,'') as BPlace, ifnull(FNo,'') as FNo, ifnull(Father,'') as Father, ifnull(MNo,'') as MNo, ifnull(Mother,'') as Mother," +
                    "ifnull(Sex,'') as Sex, ifnull(MS,'') as MS, ifnull(SPNO1,'') as SPNO1,ifnull(SPNO2,'') as SPNO2,ifnull(SPNO3,'') " +
                    "as SPNO3,ifnull(SPNO4,'') as SPNO4, ifnull(ELCONo,'') as ELCONo, ifnull(ELCODontKnow,'') as ELCODontKnow, " +
                    "ifnull(EDU,'') as EDU, ifnull(Rel,'') as Rel, ifnull(Nationality,'') as Nationality, ifnull(OCP,'') as OCP," +
                    "(select  NameEng  from member where Dist=(select  Dist  from member Where  healthid='"+ healthId +"') " +
                    "and Upz=(select  Upz  from member  Where  healthid='"+ healthId +"') and UN=(select  UN  from member  " +
                    "Where  healthid='"+ healthId +"') and Mouza=(select  Mouza  from member  Where  healthid='"+ healthId +"') " +
                    "and Vill=(select  Vill  from member  Where  healthid='"+ healthId +"') " +
                    "and ProvCode=(select  ProvCode  from member  Where  healthid='"+ healthId +"') " +
                    "and HHNo=(select  HHNo  from member  Where  healthid='"+ healthId +"') " +
                    "and SNo=(select  SPNO1  from member  Where  healthid='"+ healthId +"'))as HusName";
            SQL += ",(select  Age  from member where Dist=(select  Dist  from member  Where  healthid='"+ healthId +"') " +
                    "and Upz=(select  Upz  from member  Where  healthid='"+ healthId +"') " +
                    "and UN=(select  UN  from member  Where  healthid='"+ healthId +"') " +
                    "and Mouza=(select  Mouza  from member  Where  healthid='"+ healthId +"') " +
                    "and Vill=(select  Vill  from member  Where  healthid='"+ healthId +"') " +
                    "and ProvCode=(select  ProvCode  from member  Where  healthid='"+ healthId +"') " +
                    "and HHNo=(select  HHNo  from member  Where  healthid='"+ healthId +"') " +
                    "and SNo=(select  SPNO1  from member  Where  healthid='"+ healthId +"'))as HusAge";
            SQL += " from Member where healthid='"+ healthId +"'";*/



           /* SQL = "Select Dist, Upz, UN, Mouza, Vill, HHNo, SNo as SNo, ifnull(HealthID,'') as HealthID, ifnull(NameEng,'') as NameEng," +
                    "ifnull(NameBang,'') as NameBang, ifnull(Rth,'') as Rth, ifnull(HaveNID,'') as HaveNID, ifnull(NID,'') as NID, ifnull(NIDStatus,'') as NIDStatus, ifnull(HaveBR,'') as HaveBR, ifnull(BRID,'') as BRID, ifnull(BRIDStatus,'') as BRIDStatus, ifnull(MobileNo1,'') as MobileNo1," +
                    "ifnull(MobileNo2,'') as MobileNo2, ifnull(DOB,'') as DOB, ifnull(Age,'') as Age, ifnull(DOBSource,'') as DOBSource, ifnull(BPlace,'') as BPlace, ifnull(FNo,'') as FNo, ifnull(Father,'') as Father, ifnull(MNo,'') as MNo, ifnull(Mother,'') as Mother," +
                    "ifnull(Sex,'') as Sex, ifnull(MS,'') as MS, ifnull(SPNO1,'') as SPNO1,ifnull(SPNO2,'') as SPNO2,ifnull(SPNO3,'') " +
                    "as SPNO3,ifnull(SPNO4,'') as SPNO4, ifnull(ELCONo,'') as ELCONo, ifnull(ELCODontKnow,'') as ELCODontKnow, " +
                    "ifnull(EDU,'') as EDU, ifnull(Rel,'') as Rel, ifnull(Nationality,'') as Nationality, ifnull(OCP,'') as OCP," +
                    "(select NameEng  from member where  HealthId='" + g.getHealthID() + "' and SNo=(select  SPNO1  from member  Where   HealthId='" + g.getHealthID() + "' and SNo='" + SNo + "'" + " ))as HusName " +
                    " from Member Where  HealthId='" + g.getHealthID() + "' and SNo='" + SNo + "'";*/


            /*SQL = "Select Dist, Upz, UN, Mouza, Vill, HHNo, SNo as SNo, ifnull(HealthID,'') as HealthID, ifnull(NameEng,'') as NameEng,";
            SQL += " ifnull(NameBang,'') as NameBang, ifnull(Rth,'') as Rth, ifnull(HaveNID,'') as HaveNID, ifnull(NID,'') as NID, ifnull(NIDStatus,'') as NIDStatus, ifnull(HaveBR,'') as HaveBR, ifnull(BRID,'') as BRID, ifnull(BRIDStatus,'') as BRIDStatus, ifnull(MobileNo1,'') as MobileNo1,";
            SQL += " ifnull(MobileNo2,'') as MobileNo2, ifnull(DOB,'') as DOB, ifnull(Age,'') as Age, ifnull(DOBSource,'') as DOBSource, ifnull(BPlace,'') as BPlace, ifnull(FNo,'') as FNo, ifnull(Father,'') as Father, ifnull(MNo,'') as MNo, ifnull(Mother,'') as Mother,";
            SQL += " ifnull(Sex,'') as Sex, ifnull(MS,'') as MS, ifnull(SPNO1,'') as SPNO1,ifnull(SPNO2,'') as SPNO2,ifnull(SPNO3,'') as SPNO3,ifnull(SPNO4,'') as SPNO4, ifnull(ELCONo,'') as ELCONo, ifnull(ELCODontKnow,'') as ELCODontKnow, ifnull(EDU,'') as EDU, ifnull(Rel,'') as Rel, ifnull(Nationality,'') as Nationality, ifnull(OCP,'') as OCP";
            SQL += ",(select NameEng  from member where Dist='"+ Dist +"' and Upz='"+ Upz +"' and UN='"+ UN +"' and Mouza='"+ Mouza +"' and Vill='"+ Vill +"' and HHNo='"+ HHNo +"' and SNo=(select  SPNO1  from member  Where Dist='"+ Dist +"' and Upz='"+ Upz +"' and UN='"+ UN +"' and Mouza='"+ Mouza +"' and Vill='"+ Vill +"' and HHNo='"+ HHNo +"' and SNo='"+ SNo +"'))as HusName";
            SQL += " from Member Where Dist='"+ Dist +"' and Upz='"+ Upz +"' and UN='"+ UN +"' and Mouza='"+ Mouza +"' and Vill='"+ Vill +"' and HHNo='"+ HHNo +"' and SNo='"+ SNo +"'";*/
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
    }

    private void ELCONoSearch(String Dist, String Upz, String UN, String Mouza, String Vill, String HHNo, String SNo) {
        try {
            String SQL = "";
            SQL = "select E.ELCONo as ELCONo from ELCO E Where E.healthId='" + g.getHealthID() + "'";
            /*SQL = "select E.ELCONo as ELCONo from ELCO E, member M Where E.Dist=M.Dist and E.Upz=M.Upz and E.UN=M.UN and E.Mouza=M.Mouza and E.Vill=M.Vill and ";
            SQL += "E.HHNo=M.HHNo and E.SNo=M.SNo and ";
            SQL += "E.Dist='"+ Dist +"' and E.Upz='"+ Upz +"' and E.UN='"+ UN +"' and E.Mouza='"+ Mouza +"' and E.Vill='"+ Vill +"' and E.HHNo='"+ HHNo +"' and E.SNo='"+ SNo +"'";*/
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
            if(VariableID.equals("btnDOPNCCh1"))
            {
                dtpDate = (EditText)findViewById(R.id.dtpDOPNCCh1);
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

            //String pgnpositionselected = String.valueOf(spnPgn.getSelectedItemPosition());
            // String ServiceId = serviceID(pgnpositionselected);

            return Integer.parseInt(C.ReturnSingleValue("Select count(*)total from newBorn where healthid='" + g.getHealthID() + "' AND pregNo = '" + PGNNo() + "'"));
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


                String SQL = "Select healthId,pregNo,childNo,ifnull(birthWeight,'') as birthWeight,ifnull(gender,'') as gender, ifnull(outcomeType,'') as outcomeType  from newBorn where healthid='" + g.getHealthID() + "' AND pregNo = '" + PGNNo() + "'" + " order by childNo asc";

                try {
                    Cursor cur = C.ReadData(SQL);
                    cur.moveToFirst();

                    totalRec = cur.getCount();
                    vcode = new String[4][totalRec];
                    int i = 0;
                    while (!cur.isAfterLast()) {
                        ((LinearLayout)findViewById(R.id.secChildList)).setVisibility(View.VISIBLE);
                        vcode[0][i] = "শিশু " + " " + String.valueOf(cur.getString(cur.getColumnIndex("childNo")))
                                + " ওজন " + String.valueOf(cur.getString(cur.getColumnIndex("birthWeight")))
                                + " \n প্রসব ফলাফল " + String.valueOf(cur.getString(cur.getColumnIndex("outcomeType")));
                        vcode[1][i] = String.valueOf(cur.getString(cur.getColumnIndex("healthId")));
                        vcode[2][i] = String.valueOf(cur.getString(cur.getColumnIndex("pregNo")));
                        vcode[3][i] = String.valueOf(cur.getString(cur.getColumnIndex("childNo")));
                        /*vcode[1][i]= String.valueOf(cur.getString(cur.getColumnIndex("serviceId")));
                        vcode[2][i]= cur.getString(cur.getColumnIndex("imucard"));
                        vcode[3][i]= String.valueOf(cur.getString(cur.getColumnIndex("imucode")));*/

                        i += 1;
                        cur.moveToNext();
                      //  VlblChildList.setVisibility(View.VISIBLE);
                    }
                    cur.close();

                    Button tv = (Button) MyView.findViewById(R.id.image_name);
                    tv.setTextSize(14);
                    tv.setText(vcode[0][position]);// + "\n" + vcode[1][position]);
                    final Integer p = position;
                    tv.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            // ChkTT1.setChecked(true);
                            // if(vcode[2][position].equals("1"))
                            // {
                            //rdoCardYes.setChecked(true);
                            // }
                            // else if(vcode[2][position].equals("2"))
                            // {
                            // rdoCardNo.setChecked(true);
                            // }
                            //String pgnpositionselected = String.valueOf(spnPgn.getSelectedItemPosition());
                            //    String ServiceId = String.valueOf(vcode[1][position]);


                             /*   if(vcode[1][position].length()!=0)
                            {
                                dtpDOTT1.setText(vcode[1][position]);
                            }
                            else
                            {
                                dtpDOTT1.setText("");
                            }*/

                            // secTT1.setVisibility(View.VISIBLE);
                            //   btnTTClose.setVisibility(View.VISIBLE);
                            //  btnAddTT.setVisibility(View.GONE);
                            // g.setImuCode(vcode[3][position]);
                            //String ServiceId = String.valueOf(vcode[1][position]);
                            // DisplaySelectedANCInfo(ServiceId);
                            String pregNo = String.valueOf(vcode[2][position]);
                            String childNo = String.valueOf(vcode[3][position]);
                            g.setPregNo(pregNo);
                            g.setChildNo(childNo);
                             finish();

                            Intent f2 = new Intent(getApplicationContext(), Child.class);
                            startActivity(f2);
                        }
                    });
                } catch (Exception ex) {
                    // Connection.MessageBox(Deliv.this, ex.getMessage());
                }

            }
            return MyView;
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

            //String pgnpositionselected = String.valueOf(spnPgn.getSelectedItemPosition());
            // String ServiceId = serviceID(pgnpositionselected);

            return Integer.parseInt(C.ReturnSingleValue("Select count(*)total from pncServiceMother where healthid='" + g.getHealthID() + "' AND pregNo = '" + PGNNo() + "'"));
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
                String SQL = "Select healthId, pregNo,serviceId, visitDate from pncServiceMother where healthid='" + g.getHealthID() + "' AND pregNo = '" + PGNNo() + "'" + " order by cast(visitDate as DATE) asc";

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
                    Connection.MessageBox(Deliv.this, ex.getMessage());
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
        }
        catch (Exception ex) {
            Connection.MessageBox(Deliv.this, ex.getMessage());
        }
    }

    private void DataSavePNC()
    {

        try
        {
            if(dtpDOPNCCh1.getText().toString().length()==0 & dtpDOPNCCh1.isShown())
            {
                Connection.MessageBox(Deliv.this, "পি এন সি  এর তারিখ কত লিখুন।");
                dtpDOPNCCh1.requestFocus();
                return;
            }

            if(validateDeliveryDate())
            {
                Connection.MessageBox(Deliv.this, "পি এন সি  এর সঠিক তারিখ কত লিখুন।");
                return;
            }

            String SQL = "";
            SQL = "select healthId, pregNo, serviceId, providerId, visitDate, serviceSource, systemEntryDate, Upload, UploadDT, modifyDate FROM pncServiceMother ";
            SQL += " WHERE healthId = '"+ g.getHealthID() +"' AND pregNo ='"+ PGNNo() +"' and serviceId='"+ GetServiceId() +"'  ORDER BY visitDate";

            String SQ = "select visitDate FROM pncServiceMother ";
            SQ += " WHERE healthId = '"+ g.getHealthID() +"' AND pregNo ='"+ PGNNo() +"' and visitDate='"+ Global.DateConvertYMD(dtpDOPNCCh1.getText().toString()) +"'";



            if(!C.Existence(SQL) && !C.Existence(SQ))
            {

                SQL = "Insert into " + TableNamePNC + "(healthId, pregNo, serviceId, providerId, visitDate, serviceSource, systemEntryDate, Upload)Values('"+
                        g.getHealthID() +"','"+ PGNNo() +"','"+  GetServiceId() +"','"+ g.getProvCode() +"','"+ Global.DateConvertYMD(dtpDOPNCCh1.getText().toString()) +"','"+  "G','"+  Global.DateTimeNowYMDHMS() +"','"+ "2')";
                C.Save(SQL);
                ((LinearLayout)findViewById(R.id.secChildList)).setVisibility(View.VISIBLE);
                Connection.MessageBox(Deliv.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
            }
            else
            {
                Connection.MessageBox(Deliv.this, "পি এন সি  এর ভিসিট দেয়া আছে");
                return;
            }

        }
        catch(Exception  e)
        {
            Connection.MessageBox(Deliv.this, e.getMessage());
            return;
        }
    }
    private String GetServiceId()
    {
        String SQL = "";

        SQL = "select '0'||(ifnull(max(cast(serviceId as int)),0))MaxserviceId from pncServiceMother";
        SQL += " WHERE healthId = '"+ g.getHealthID() +"' AND pregNo ='"+ PGNNo() +"'";

        String tempserviceID = C.ReturnSingleValue(SQL);


        String serviceID = String.valueOf((Integer.parseInt(tempserviceID) + 1));

        if (serviceID.equalsIgnoreCase("1")) {
            return String.valueOf(g.getHealthID() + PGNNo() + serviceID);
        } else {
            return String.valueOf(serviceID);
        }
    }
    private void PNCVisitSearch(String serviceId)
    {
        try
        {
            String SQL = "";
            SQL = "select healthId, pregNo, serviceId, providerId, visitDate, serviceSource, systemEntryDate, Upload, UploadDT, modifyDate FROM pncServiceMother ORDER BY visitDate";
            SQL += " WHERE healthId = '"+ g.getHealthID() +"' AND pregNo ='"+ g.getPregNo() +"'  and serviceId='"+ serviceId +"'";
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
            Connection.MessageBox(Deliv.this, e.getMessage());
            return;
        }
    }


}