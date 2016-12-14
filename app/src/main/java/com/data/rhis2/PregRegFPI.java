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
import android.text.Html;
import android.util.Log;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import Common.Connection;
import Common.Global;

/**
 * Created by Nisan on 5/1/2016.
 */
public class PregRegFPI extends Activity {
    boolean netwoekAvailable = false;
    Location currentLocation;
    double currentLatitude, currentLongitude;
    Location currentLocationNet;
    double currentLatitudeNet, currentLongitudeNet;

    private boolean IsUserHA() {
        if (g.getProvType().equalsIgnoreCase("2"))
            return true;
        else
            return false;


    }

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
        AlertDialog.Builder adb = new AlertDialog.Builder(PregRegFPI.this);
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
    private String TableNameFPI;
    private String TableName;
    private String TablePregRefer;
    private String TableANC;

    private String TableNameElcoVisit;

    TextView txtHealthID;

    LinearLayout secdelete1;
    //LinearLayout secDelete;

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

    LinearLayout seclblH1;
    TextView lblHlblH;
    TextView lblHealthID;
    EditText txtHHNo;

    LinearLayout secSNo;
    TextView VlblSLNo;
    TextView txtSLNo;
    TextView VlblSNo;
    TextView txtSNo;
    EditText txtELCONo;

    LinearLayout secName;
    TextView VlblName;
    TextView txtName;

    LinearLayout secAge;
    TextView VlblAge;
    TextView txtAge;

    LinearLayout secLMP;
    TextView VlblLMP;
    EditText dtpLMP;
    ImageButton btnLMP;

    LinearLayout secLive;
    TextView VlblLiveSonDau;
    EditText txtLiveSonDau;

    LinearLayout secPgn;
    TextView VlblPgn;
    Spinner spnPgn;

    LinearLayout secEDD;
    TextView VlblEDD;
    EditText dtpEDD;


    LinearLayout secAgeL;
    LinearLayout secAgeL1;
    TextView VlblAgeL;
    EditText txtAgeM;
    EditText txtAgeY;

    /*LinearLayout secVDate;
    TextView VlblVDate;
    EditText dtpVDate;
    ImageButton btnVDate;

    LinearLayout secIron;
    TextView VlblIron;
    RadioGroup rdogrpIron;
    RadioButton rdoIron1;
    RadioButton rdoIron2;

    LinearLayout secMiso;
    TextView VlblMiso;
    RadioGroup rdogrpMiso;
    RadioButton rdoMiso1;
    RadioButton rdoMiso2;*/
    LinearLayout secDengersine;
    TextView VlblDanger;
    CheckBox chkDengersine;

    LinearLayout secDengersine1;
    TextView VlblDanger1;
    CheckBox chkDengersine1;

    LinearLayout secDengersine2;
    TextView VlblDanger2;
    CheckBox chkDengersine2;
    LinearLayout secDengersine3;
    TextView VlblDanger3;
    CheckBox chkDengersine3;
    LinearLayout secDengersine4;
    TextView VlblDanger4;
    CheckBox chkDengersine4;
    LinearLayout secDengersine5;
    TextView VlblDanger5;
    CheckBox chkDengersine5;
    LinearLayout secDengersine6;
    TextView VlblDanger6;
    CheckBox chkDengersine6;
    LinearLayout secDengersine7;
    TextView VlblDanger7;
    CheckBox chkDengersine7;

    LinearLayout secReferWomen;
    TextView VlblReferWomen;
    RadioGroup rdogrpReferWomen;
    RadioButton rdoReferWomen1;
    RadioButton rdoReferWomen2;
    LinearLayout secReferFaci;
    TextView VlblFaci;
    Spinner spnFaci;
    TextView VlblServiceId;

    //LinearLayout secANCVisit;

    //..............FPI.............
    RadioGroup rdogrpLMP;
    RadioButton rdoLMP1;
    RadioButton rdoLMP2;

    RadioGroup rdogrpEDD;
    RadioButton rdoEDD1;
    RadioButton rdoEDD2;

    RadioGroup rdogrpPgn;
    RadioButton rdoPgn1;
    RadioButton rdoPgn2;

    RadioGroup rdogrpLiveSonDau;
    RadioButton rdoLiveSonDau1;
    RadioButton rdoLiveSonDau2;

    RadioGroup rdogrpAgeL;
    RadioButton rdoAgeL1;
    RadioButton rdoAgeL2;

    RadioGroup rdogrpDanger;
    RadioButton rdoDanger1;
    RadioButton rdoDanger2;

    RadioGroup rdogrpRefWomen;
    RadioButton rdoRefWomen1;
    RadioButton rdoRefWomen2;


    String StartTime;
    Button cmdSave;
    Button btnSaveRef;
    LinearLayout secPregVisit;
    String sqlnew = "";
    String sqlupdate = "";
    // String Sex ="";
    String Age = "";

    String pregnancyNo = "";
    String pregnancyNoDelivary = "";
    String LastDelivaryDate = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.pregnancy_registerfpi);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();
            TableNameFPI = "pregWomenFPI";
            TableName = "PregWomen";
            TablePregRefer = "PregRefer";
            TableANC = "ancService";
            TableNameElcoVisit = "ELCOVisit";


            seclblH1 = (LinearLayout) findViewById(R.id.seclblH1);
            secdelete1 = (LinearLayout) findViewById(R.id.secdelete1);
            secdelete1.setVisibility(View.GONE);
            //secDelete= (LinearLayout) findViewById(R.id.secDelete);
            //secDelete.setVisibility(View.GONE);
            //Sex Return
            //Sex=C.ReturnSingleValue("Select Sex from Member where healthId ='"+g.getHealthID()+"'");
            // Age=C.ReturnSingleValue("Select Age from Member where healthId ='"+g.getHealthID()+"'");

            //secANCVisit = (LinearLayout) findViewById(R.id.secANCVisit);
            secPregVisit = (LinearLayout) findViewById(R.id.secPregVisit);

            txtHealthID = (TextView) findViewById(R.id.txtHealthID);
            txtHHNo = (EditText) findViewById(R.id.txtHHNo);

            VlblSNo = (TextView) findViewById(R.id.VlblSNo);
            txtSNo = (TextView) findViewById(R.id.txtSNo);
            VlblSLNo = (TextView) findViewById(R.id.VlblSNo);
            txtSLNo = (TextView) findViewById(R.id.txtSLNo);
            txtSLNo.setText(GetCountSLNoNumber());
            VlblSNo.setVisibility(View.GONE);
            txtSNo.setVisibility(View.GONE);

            txtELCONo = (EditText) findViewById(R.id.txtELCONo);
            txtELCONo.setEnabled(false);
            //  txtELCONo.setText(g.getAMElco());
            secName = (LinearLayout) findViewById(R.id.secName);
            VlblName = (TextView) findViewById(R.id.VlblName);
            txtName = (TextView) findViewById(R.id.txtName);

            secAge = (LinearLayout) findViewById(R.id.secAge);
            VlblAge = (TextView) findViewById(R.id.VlblAge);
            txtAge = (TextView) findViewById(R.id.txtAge);

            secLMP = (LinearLayout) findViewById(R.id.secLMP);
            VlblLMP = (TextView) findViewById(R.id.VlblLMP);
            dtpLMP = (EditText) findViewById(R.id.dtpLMP);
            btnLMP = (ImageButton) findViewById(R.id.btnLMP);

            secLive = (LinearLayout) findViewById(R.id.secLive);
            VlblLiveSonDau = (TextView) findViewById(R.id.VlblLiveSonDau);
            txtLiveSonDau = (EditText) findViewById(R.id.txtLiveSonDau);

            secPgn = (LinearLayout) findViewById(R.id.secPgn);
            VlblPgn = (TextView) findViewById(R.id.VlblPgn);
            spnPgn = (Spinner) findViewById(R.id.spnPgn);
            spnPgn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (spnPgn.getSelectedItemPosition() == 0) {

                    } else {
                        String PrevPGN = "'" + GetMaxPGN() == "0" ? "" : GetMaxPGN() + "";
                        if (PrevPGN.equalsIgnoreCase("")) {
                        } else {
                            Integer PrevPGNPlus1 = Integer.parseInt(PrevPGN) + 1;
                            Integer ActualPrevPGN = Integer.parseInt(PrevPGN);
                            Integer pgnpositionselected = Integer.parseInt(String.valueOf(spnPgn.getSelectedItemPosition()));

                            if (!pgnpositionselected.equals(ActualPrevPGN)) {
                                if (!PrevPGNPlus1.equals(pgnpositionselected)) {
                                    Connection.MessageBox(PregRegFPI.this, "বর্তমানে কত তম গর্ভ তথ্য  সঠিক নয়।");
                                    spnPgn.requestFocus();
                                    return;
                                }
                            }
                        }
                    }
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });

            secEDD = (LinearLayout) findViewById(R.id.secEDD);
            VlblEDD = (TextView) findViewById(R.id.VlblEDD);
            dtpEDD = (EditText) findViewById(R.id.dtpEDD);

            secAgeL = (LinearLayout) findViewById(R.id.secAgeL);
            secAgeL1 = (LinearLayout) findViewById(R.id.secAgeL1);
            VlblAgeL = (TextView) findViewById(R.id.VlblAgeL);
            txtAgeM = (EditText) findViewById(R.id.txtAgeMo);
            txtAgeY = (EditText) findViewById(R.id.txtAgeY);

            secDengersine = (LinearLayout) findViewById(R.id.secDengersine);
            VlblDanger = (TextView) findViewById(R.id.VlblDanger);
            chkDengersine = (CheckBox) findViewById(R.id.chkDengersine);

            chkDengersine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // g.setRisk("r");
                        secDengersine1.setVisibility(View.VISIBLE);
                        secDengersine2.setVisibility(View.VISIBLE);
                        secDengersine3.setVisibility(View.VISIBLE);
                        secDengersine4.setVisibility(View.VISIBLE);
                        secDengersine5.setVisibility(View.VISIBLE);
                        secDengersine6.setVisibility(View.VISIBLE);
                        secDengersine7.setVisibility(View.VISIBLE);
                    } else {

                        secDengersine1.setVisibility(View.GONE);
                        secDengersine2.setVisibility(View.GONE);
                        secDengersine3.setVisibility(View.GONE);
                        secDengersine4.setVisibility(View.GONE);
                        secDengersine5.setVisibility(View.GONE);
                        secDengersine6.setVisibility(View.GONE);
                        secDengersine7.setVisibility(View.GONE);
                    }

                }
            });
            secDengersine1 = (LinearLayout) findViewById(R.id.secDengersine1);
            VlblDanger1 = (TextView) findViewById(R.id.VlblDanger1);
            chkDengersine1 = (CheckBox) findViewById(R.id.chkDengersine1);
            chkDengersine1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (chkDengersine1.isChecked() == true || chkDengersine2.isChecked() == true ||
                            chkDengersine3.isChecked() == true || chkDengersine4.isChecked() == true ||
                            chkDengersine5.isChecked() == true || chkDengersine6.isChecked() == true || chkDengersine7.isChecked() == true
                            ) {

                        secReferWomen.setVisibility(View.VISIBLE);

                    } else if (chkDengersine1.isChecked() == false && chkDengersine2.isChecked() == false &&
                            chkDengersine3.isChecked() == false && chkDengersine4.isChecked() == false &&
                            chkDengersine5.isChecked() == false && chkDengersine6.isChecked() == false && chkDengersine7.isChecked() == false
                            ) {

                        secReferWomen.setVisibility(View.GONE);

                    }

                }
            });


            secDengersine2 = (LinearLayout) findViewById(R.id.secDengersine2);
            VlblDanger2 = (TextView) findViewById(R.id.VlblDanger2);
            chkDengersine2 = (CheckBox) findViewById(R.id.chkDengersine2);
            chkDengersine2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (chkDengersine1.isChecked() == true || chkDengersine2.isChecked() == true ||
                            chkDengersine3.isChecked() == true || chkDengersine4.isChecked() == true ||
                            chkDengersine5.isChecked() == true || chkDengersine6.isChecked() == true || chkDengersine7.isChecked() == true
                            ) {

                        secReferWomen.setVisibility(View.VISIBLE);

                    } else if (chkDengersine1.isChecked() == false && chkDengersine2.isChecked() == false &&
                            chkDengersine3.isChecked() == false && chkDengersine4.isChecked() == false &&
                            chkDengersine5.isChecked() == false && chkDengersine6.isChecked() == false && chkDengersine7.isChecked() == false
                            ) {

                        secReferWomen.setVisibility(View.GONE);

                    }

                }
            });
            secDengersine3 = (LinearLayout) findViewById(R.id.secDengersine3);
            VlblDanger3 = (TextView) findViewById(R.id.VlblDanger3);
            chkDengersine3 = (CheckBox) findViewById(R.id.chkDengersine3);
            chkDengersine3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (chkDengersine1.isChecked() == true || chkDengersine2.isChecked() == true ||
                            chkDengersine3.isChecked() == true || chkDengersine4.isChecked() == true ||
                            chkDengersine5.isChecked() == true || chkDengersine6.isChecked() == true || chkDengersine7.isChecked() == true
                            ) {

                        secReferWomen.setVisibility(View.VISIBLE);

                    } else if (chkDengersine1.isChecked() == false && chkDengersine2.isChecked() == false &&
                            chkDengersine3.isChecked() == false && chkDengersine4.isChecked() == false &&
                            chkDengersine5.isChecked() == false && chkDengersine6.isChecked() == false && chkDengersine7.isChecked() == false
                            ) {

                        secReferWomen.setVisibility(View.GONE);

                    }

                }
            });
            secDengersine4 = (LinearLayout) findViewById(R.id.secDengersine4);
            VlblDanger4 = (TextView) findViewById(R.id.VlblDanger4);
            chkDengersine4 = (CheckBox) findViewById(R.id.chkDengersine4);
            chkDengersine4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (chkDengersine1.isChecked() == true || chkDengersine2.isChecked() == true ||
                            chkDengersine3.isChecked() == true || chkDengersine4.isChecked() == true ||
                            chkDengersine5.isChecked() == true || chkDengersine6.isChecked() == true || chkDengersine7.isChecked() == true
                            ) {

                        secReferWomen.setVisibility(View.VISIBLE);

                    } else if (chkDengersine1.isChecked() == false && chkDengersine2.isChecked() == false &&
                            chkDengersine3.isChecked() == false && chkDengersine4.isChecked() == false &&
                            chkDengersine5.isChecked() == false && chkDengersine6.isChecked() == false && chkDengersine7.isChecked() == false
                            ) {

                        secReferWomen.setVisibility(View.GONE);

                    }

                }
            });
            secDengersine5 = (LinearLayout) findViewById(R.id.secDengersine5);
            VlblDanger5 = (TextView) findViewById(R.id.VlblDanger5);
            chkDengersine5 = (CheckBox) findViewById(R.id.chkDengersine5);
            chkDengersine5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (chkDengersine1.isChecked() == true || chkDengersine2.isChecked() == true ||
                            chkDengersine3.isChecked() == true || chkDengersine4.isChecked() == true ||
                            chkDengersine5.isChecked() == true || chkDengersine6.isChecked() == true || chkDengersine7.isChecked() == true
                            ) {

                        secReferWomen.setVisibility(View.VISIBLE);

                    } else if (chkDengersine1.isChecked() == false && chkDengersine2.isChecked() == false &&
                            chkDengersine3.isChecked() == false && chkDengersine4.isChecked() == false &&
                            chkDengersine5.isChecked() == false && chkDengersine6.isChecked() == false && chkDengersine7.isChecked() == false
                            ) {

                        secReferWomen.setVisibility(View.GONE);

                    }

                }
            });
            secDengersine6 = (LinearLayout) findViewById(R.id.secDengersine6);
            VlblDanger6 = (TextView) findViewById(R.id.VlblDanger6);
            chkDengersine6 = (CheckBox) findViewById(R.id.chkDengersine6);
            chkDengersine6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (chkDengersine1.isChecked() == true || chkDengersine2.isChecked() == true ||
                            chkDengersine3.isChecked() == true || chkDengersine4.isChecked() == true ||
                            chkDengersine5.isChecked() == true || chkDengersine6.isChecked() == true || chkDengersine7.isChecked() == true
                            ) {

                        secReferWomen.setVisibility(View.VISIBLE);

                    } else if (chkDengersine1.isChecked() == false && chkDengersine2.isChecked() == false &&
                            chkDengersine3.isChecked() == false && chkDengersine4.isChecked() == false &&
                            chkDengersine5.isChecked() == false && chkDengersine6.isChecked() == false && chkDengersine7.isChecked() == false
                            ) {

                        secReferWomen.setVisibility(View.GONE);

                    }

                }
            });
            secDengersine7 = (LinearLayout) findViewById(R.id.secDengersine7);
            VlblDanger7 = (TextView) findViewById(R.id.VlblDanger7);
            chkDengersine7 = (CheckBox) findViewById(R.id.chkDengersine7);
            chkDengersine7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (chkDengersine1.isChecked() == true || chkDengersine2.isChecked() == true ||
                            chkDengersine3.isChecked() == true || chkDengersine4.isChecked() == true ||
                            chkDengersine5.isChecked() == true || chkDengersine6.isChecked() == true || chkDengersine7.isChecked() == true
                            ) {

                        secReferWomen.setVisibility(View.VISIBLE);

                    } else if (chkDengersine1.isChecked() == false && chkDengersine2.isChecked() == false &&
                            chkDengersine3.isChecked() == false && chkDengersine4.isChecked() == false &&
                            chkDengersine5.isChecked() == false && chkDengersine6.isChecked() == false && chkDengersine7.isChecked() == false
                            ) {

                        secReferWomen.setVisibility(View.GONE);

                    }

                }
            });


            secReferWomen = (LinearLayout) findViewById(R.id.secReferWomen);
            VlblReferWomen = (TextView) findViewById(R.id.VlblReferWomen);
            rdogrpReferWomen = (RadioGroup) findViewById(R.id.rdogrpReferWomen);
            rdoReferWomen1 = (RadioButton) findViewById(R.id.rdoReferWomen1);
            rdoReferWomen2 = (RadioButton) findViewById(R.id.rdoReferWomen2);
            rdogrpReferWomen.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int radioButtonID) {
                    RadioButton rb = (RadioButton) findViewById(rdogrpReferWomen.getCheckedRadioButtonId());
                    if (rb == null) return;

                    if (rdoReferWomen2.isChecked() == true) {

                        secReferFaci.setVisibility(View.GONE);
                        spnFaci.setSelection(0);
                    } else {
                        ReferStatus();
                        secReferFaci.setVisibility(View.VISIBLE);

                    }
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });
            secReferFaci = (LinearLayout) findViewById(R.id.secReferFaci);
            VlblFaci = (TextView) findViewById(R.id.VlblFaci);
            spnFaci = (Spinner) findViewById(R.id.spnFaci);
            btnSaveRef = (Button) findViewById(R.id.btnSaveRef);
            VlblServiceId = (TextView) findViewById(R.id.VlblServiceId);
            VlblServiceId.setVisibility(View.GONE);
            List<String> listFacility = new ArrayList<String>();
            listFacility.add("");
            listFacility.add("01-উপজেলা স্বাস্থ্য কমপ্লেক্স");
            listFacility.add("02-ইউনিয়ন স্বাস্থ্য ও পরিবার কল্যাণ কেন্দ্র");
            listFacility.add("03-মা ও শিশু কল্যাণ কেন্দ্র");
            listFacility.add("04-জেলা সদর  বা অন্যান্য সরকারী হাসপাতাল");
            listFacility.add("05-এনজিও ক্লিনিক বা হাসপাতাল");
            listFacility.add("06-প্রাইভেট ক্লিনিক বা হাসপাতাল");
            listFacility.add("77-অন্যান্য");

            ArrayAdapter<String> adptrspnFaci = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listFacility);
            spnFaci.setAdapter(adptrspnFaci);
/*
          */


            List<String> listPgn = new ArrayList<String>();
            listPgn.add("");
            listPgn.add("১ম");
            listPgn.add("২য় ");
            listPgn.add("৩য়");
            listPgn.add("৪র্থ");
            listPgn.add("৫ম");
            listPgn.add("৬ষ্ঠ");
            listPgn.add("৭ম");
            listPgn.add("৮ম");
            listPgn.add("৯ম");
            listPgn.add("১০ম ");
            ArrayAdapter<String> adptrspnPgn = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listPgn);
            spnPgn.setAdapter(adptrspnPgn);
            spnPgn.setEnabled(false);

            btnLMP.setEnabled(false);
            btnLMP.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btnLMP";
                    showDialog(DATE_DIALOG);
                }
            });

            //..............FPI.............

            rdogrpLMP = (RadioGroup) findViewById(R.id.rdogrpLMP);
            rdoLMP1 = (RadioButton) findViewById(R.id.rdoLMP1);
            rdoLMP2 = (RadioButton) findViewById(R.id.rdoLMP2);

            rdogrpEDD = (RadioGroup) findViewById(R.id.rdogrpLMP);
            rdoEDD1 = (RadioButton) findViewById(R.id.rdoEDD1);
            rdoEDD2 = (RadioButton) findViewById(R.id.rdoEDD2);

            rdogrpPgn = (RadioGroup) findViewById(R.id.rdogrpPgn);
            rdoPgn1 = (RadioButton) findViewById(R.id.rdoPgn1);
            rdoPgn2 = (RadioButton) findViewById(R.id.rdoPgn2);

            rdogrpLiveSonDau = (RadioGroup) findViewById(R.id.rdogrpLiveSonDau);
            rdoLiveSonDau1 = (RadioButton) findViewById(R.id.rdoLiveSonDau1);
            rdoLiveSonDau2 = (RadioButton) findViewById(R.id.rdoLiveSonDau2);

            rdogrpAgeL = (RadioGroup) findViewById(R.id.rdogrpAgeL);
            rdoAgeL1 = (RadioButton) findViewById(R.id.rdoAgeL1);
            rdoAgeL2 = (RadioButton) findViewById(R.id.rdoAgeL2);

            rdogrpDanger = (RadioGroup) findViewById(R.id.rdogrpDanger);
            rdoDanger1 = (RadioButton) findViewById(R.id.rdoDanger1);
            rdoDanger2 = (RadioButton) findViewById(R.id.rdoDanger2);

            rdogrpRefWomen = (RadioGroup) findViewById(R.id.rdogrpRefWomen);
            rdoRefWomen1 = (RadioButton) findViewById(R.id.rdoRefWomen1);
            rdoRefWomen2 = (RadioButton) findViewById(R.id.rdoRefWomen2);

            Bundle IDbundle = new Bundle();
            IDbundle = getIntent().getExtras();

            if (IDbundle != null) {
                // fornewpregnancy = IDbundle.getString("exists");
                sqlnew = IDbundle.getString("sqlnew");
                sqlupdate = IDbundle.getString("sqlupdate");
                // PGNNo = IDbundle.getString("PGNNo");
            }

           /* if(fornewpregnancy.equalsIgnoreCase(""))
            {
                PregWomenSearch("");
            }
            else if(fornewpregnancy.equalsIgnoreCase("exists"))
            {
                PregWomenSearch("exists");
            }*/
            // add fazlur bhi
            ELCOProfile e = new ELCOProfile();
            e.ELCOProfile(this, g.getHealthID());

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

            //Comments for HA module but open for FWA Module
            //e.PregnancyInfo(this, g.getGeneratedId(), pregnancyNo);
            e.PregnancyInfo(this, g.getGeneratedId(), pregnancyNo);


            // add fazlu bhi
            // Age = String.valueOf(e.getAge());
            // Age = String.valueOf(g.getAAge());
            // Sex = e.getSex();

            dtpLMP.setText(e.getLMP());
            dtpEDD.setText(e.getEDD());
            spnPgn.setSelection(Integer.parseInt(e.getGravida() == null ? "0" : e.getGravida()));

            String totalChild = e.getTotalLiveChild();
            txtLiveSonDau.setText(totalChild);
            if (totalChild != null) {
                if (GetTotalSonGaughter(g.getGeneratedId()).equals("0")) {
                    secAgeL.setVisibility(View.GONE);
                    secAgeL1.setVisibility(View.GONE);
                } else {
                    secAgeL.setVisibility(View.VISIBLE);
                    secAgeL1.setVisibility(View.VISIBLE);
                }
            }


/*            if (totalChild.equalsIgnoreCase("0")) {
                secAgeL.setVisibility(View.GONE);
            } else {
                secAgeL.setVisibility(View.VISIBLE);
            }*/

            //PregWomenSearch();
            //DataSearch(g.getHealthID());
            //ELCONoSearch(g.getSerialNo());

            //nisan Dengersine
            secDengersine1.setVisibility(View.GONE);
            secDengersine2.setVisibility(View.GONE);
            secDengersine3.setVisibility(View.GONE);
            secDengersine4.setVisibility(View.GONE);
            secDengersine5.setVisibility(View.GONE);
            secDengersine6.setVisibility(View.GONE);
            secDengersine7.setVisibility(View.GONE);
            secReferWomen.setVisibility(View.GONE);
            secReferFaci.setVisibility(View.GONE);

            //chkDengersine.setEnabled(false);
            chkDengersine1.setEnabled(false);
            // chkDengersine2.setEnabled(false);
            chkDengersine3.setEnabled(false);
            //chkDengersine4.setEnabled(false);
            //chkDengersine5.setEnabled(false);
            // chkDengersine6.setEnabled(false);
            // chkDengersine7.setEnabled(false);


//1

            //  if((Integer.valueOf(Age)>35||Integer.valueOf(Age)<18) )
            if ((Integer.valueOf(g.getAAge()) > 35 || Integer.valueOf(g.getAAge()) < 18)) {
                //chkDengersine.setChecked(true);
                chkDengersine1.setChecked(true);
                VlblDanger1.setText("");
                VlblDanger1.setText(Html.fromHtml(VlblDanger1.getText() + "<font color=red>গর্ভবতী মায়ের বয়স ১৮ বছরের কম বা ৩৫ বছরের বেশি হলে</font>"));
            } else {
                // chkDengersine.setChecked(false);
                chkDengersine1.setChecked(false);

            }
//2

            chkDengersine2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (chkDengersine2.isChecked() == true) {

                        chkDengersine2.setChecked(true);
                        VlblDanger2.setText("");
                        VlblDanger2.setText(Html.fromHtml(VlblDanger2.getText() + "<font color=red>প্রথম গর্ভাবস্থা বা ৩ এর অধিক সন্তান জন্ম দিয়েছেন এমন মা</font>"));


                    } else if (chkDengersine2.isChecked() == false) {

                        chkDengersine2.setChecked(false);
                        VlblDanger2.setText("");
                        VlblDanger2.setText(Html.fromHtml(VlblDanger2.getText() + "প্রথম গর্ভাবস্থা বা ৩ এর অধিক সন্তান জন্ম দিয়েছেন এমন মা"));

                    }

                }
            });

            if (Integer.valueOf(PGNNo1()).equals(0) || Integer.valueOf(PGNNo1()).equals(1) || Integer.valueOf(PGNNo1()) >= 3) {
                chkDengersine2.setChecked(true);
                chkDengersine2.setEnabled(false);
                VlblDanger2.setText("");
                VlblDanger2.setText(Html.fromHtml(VlblDanger2.getText() + "<font color=red>প্রথম গর্ভাবস্থা বা ৩ এর অধিক সন্তান জন্ম দিয়েছেন এমন মা</font>"));
            } else {

                chkDengersine2.setChecked(false);
            }

            //3
            String AValue = String.format("Select healthId, pregNo  from %s where healthId = '%s' and pregNo = '%s'", TableName, g.getGeneratedId(), pregnancyNo);

            if (!C.Existence(AValue)) {

            } else if (C.Existence(AValue)) {
                //
                String height = C.ReturnSingleValue("Select ifnull(height,'') as height from pregWomen WHERE healthId ='" + g.getGeneratedId() + "' AND pregNo ='" + pregnancyNo + "'");
                if (height.equals("") | height.equals(null) || height.equals("null")) {

                } else if (height != null || height.equals("null")) {
                    if (Integer.valueOf(height) >= 1 & Integer.valueOf(height) <= 58) {
                        chkDengersine3.setEnabled(false);
                        chkDengersine3.setChecked(true);
                        VlblDanger3.setText("");
                        VlblDanger3.setText(Html.fromHtml(VlblDanger3.getText() + "<font color=red>মায়ের উচ্চতা ১৪৫ সে মি (৪ ফুট ১০ ইঞ্চি) এর কম হলে</font>"));

                    } else {

                    }
                }
            } else {

            }


            //4
            if (dtpLMP.getText().length() != 0) {

                Integer DiffDNow_OC = Global.DateDifferenceDays(e.getLMP(), GetMaxOutComeDate());

                if (DiffDNow_OC > 0 & DiffDNow_OC <= 730 & Integer.valueOf(pregnancyNo) > 1) {
                    chkDengersine4.setEnabled(false);
                    chkDengersine4.setChecked(true);
                    VlblDanger4.setText("");
                    VlblDanger4.setText(Html.fromHtml(VlblDanger4.getText() + "<font color=red>জন্ম বিরতি- ২ বছরের কম হলে</font>"));

                } else {

                }

            }
            chkDengersine4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (chkDengersine4.isChecked() == true) {

                        chkDengersine4.setChecked(true);
                        VlblDanger4.setText("");
                        VlblDanger4.setText(Html.fromHtml(VlblDanger4.getText() + "<font color=red>জন্ম বিরতি- ২ বছরের কম হলে</font>"));


                    } else if (chkDengersine4.isChecked() == false) {

                        chkDengersine4.setChecked(false);
                        VlblDanger4.setText("");
                        VlblDanger4.setText(Html.fromHtml(VlblDanger4.getText() + "জন্ম বিরতি- ২ বছরের কম হলে"));

                    }

                }
            });

            //ifnull(riskHistoryNote,'') as riskHistoryNote

            if (!C.Existence(AValue)) {

            } else if (C.Existence(AValue)) {
                //
                String riskHistory = "Select ifnull(riskHistoryNote,'') as riskHistoryNote from pregWomen WHERE healthId ='" + g.getGeneratedId() + "' AND pregNo ='" + pregnancyNo + "'";
                String riskStatus = C.ReturnSingleValue(riskHistory);
                String[] risk = Connection.split(riskStatus, ',');
                if (riskHistory.equals("") | riskHistory.equals(null)) {

                } else if (riskHistory != null)


                    if (riskStatus.length() == 1) {
                        String risk1 = risk[0].toString();

                        if (risk1.equals("1") | risk1.equals("4")) {
                            chkDengersine5.setChecked(true);
                            VlblDanger5.setText("");
                            VlblDanger5.setText(Html.fromHtml(VlblDanger5.getText() + "<font color=red>পূর্ববর্তী প্রসবে প্রসবপূব রক্তক্ষরণ, প্রসবোত্তর রক্তক্ষরণ অথবা জরায়ুতে গর্ভফু্ল আঁটকে থাকার ইতিহাস থাকলে</font>"));
                            chkDengersine5.setEnabled(false);
                        }


                        if (risk1.equals("5") | risk1.equals("6")) {
                            chkDengersine6.setChecked(true);
                            VlblDanger6.setText("");
                            VlblDanger6.setText(Html.fromHtml(VlblDanger6.getText() + "<font color=red>মৃত জন্মের বা নবজাতকের মৃতের ইতিহাস থাকলে</font>"));
                            chkDengersine6.setEnabled(false);
                        }

                        if (risk1.equals("9")) {
                            chkDengersine7.setChecked(true);
                            VlblDanger7.setText("");
                            VlblDanger7.setText(Html.fromHtml(VlblDanger7.getText() + "<font color=red>সিজারিয়ান অপারেশন বা যন্ত্রের মাধ্যমে (ফরসেফ/ ভেকুয়াম) প্রসবের ইতিহাস থাকলে</font>"));
                            chkDengersine7.setEnabled(false);
                        }
                    } else if (riskStatus.length() == 3) {
                        String risk1 = risk[0].toString();
                        String risk2 = risk[1].toString();

                        if (risk1.equals("1") | risk2.equals("1") | risk1.equals("4") | risk2.equals("4")) {
                            chkDengersine5.setChecked(true);
                            VlblDanger5.setText("");
                            VlblDanger5.setText(Html.fromHtml(VlblDanger5.getText() + "<font color=red>পূর্ববর্তী প্রসবে প্রসবপূব রক্তক্ষরণ, প্রসবোত্তর রক্তক্ষরণ অথবা জরায়ুতে গর্ভফু্ল আঁটকে থাকার ইতিহাস থাকলে</font>"));
                            chkDengersine5.setEnabled(false);
                        }

                        if (risk1.equals("5") | risk2.equals("5") | risk1.equals("6") | risk2.equals("6")) {
                            chkDengersine6.setChecked(true);
                            VlblDanger6.setText("");
                            VlblDanger6.setText(Html.fromHtml(VlblDanger6.getText() + "<font color=red>মৃত জন্মের বা নবজাতকের মৃতের ইতিহাস থাকলে</font>"));
                            chkDengersine6.setEnabled(false);
                        }

                        if (risk1.equals("9") | risk2.equals("9")) {
                            chkDengersine7.setChecked(true);
                            VlblDanger7.setText("");
                            VlblDanger7.setText(Html.fromHtml(VlblDanger7.getText() + "<font color=red>সিজারিয়ান অপারেশন বা যন্ত্রের মাধ্যমে (ফরসেফ/ ভেকুয়াম) প্রসবের ইতিহাস থাকলে</font>"));
                            chkDengersine7.setEnabled(false);
                        }
                    } else if (riskStatus.length() == 5) {
                        String risk1 = risk[0].toString();
                        String risk2 = risk[1].toString();
                        String risk3 = risk[2].toString();

                        if (risk1.equals("1") | risk2.equals("1") | risk3.equals("1") | risk1.equals("4") | risk2.equals("4") | risk3.equals("4")) {
                            chkDengersine5.setChecked(true);
                            VlblDanger5.setText("");
                            VlblDanger5.setText(Html.fromHtml(VlblDanger5.getText() + "<font color=red>পূর্ববর্তী প্রসবে প্রসবপূব রক্তক্ষরণ, প্রসবোত্তর রক্তক্ষরণ অথবা জরায়ুতে গর্ভফু্ল আঁটকে থাকার ইতিহাস থাকলে</font>"));
                            chkDengersine5.setEnabled(false);
                        }

                        if (risk1.equals("5") | risk2.equals("5") | risk3.equals("5") | risk1.equals("6") | risk2.equals("6") | risk3.equals("6")) {
                            chkDengersine6.setChecked(true);
                            VlblDanger6.setText("");
                            VlblDanger6.setText(Html.fromHtml(VlblDanger6.getText() + "<font color=red>মৃত জন্মের বা নবজাতকের মৃতের ইতিহাস থাকলে</font>"));
                            chkDengersine6.setEnabled(false);
                        }

                        if (risk1.equals("9") | risk2.equals("9") | risk3.equals("9")) {
                            chkDengersine7.setChecked(true);
                            VlblDanger7.setText("");
                            VlblDanger7.setText(Html.fromHtml(VlblDanger7.getText() + "<font color=red>সিজারিয়ান অপারেশন বা যন্ত্রের মাধ্যমে (ফরসেফ/ ভেকুয়াম) প্রসবের ইতিহাস থাকলে</font>"));
                            chkDengersine7.setEnabled(false);
                        }
                    } else if (riskStatus.length() == 7) {
                        String risk1 = risk[0].toString();
                        String risk2 = risk[1].toString();
                        String risk3 = risk[2].toString();
                        String risk4 = risk[3].toString();

                        if (risk1.equals("1") | risk2.equals("1") | risk3.equals("1") | risk4.equals("1") | risk1.equals("4") | risk2.equals("4") | risk3.equals("4") | risk4.equals("4")) {
                            chkDengersine5.setChecked(true);
                            VlblDanger5.setText("");
                            VlblDanger5.setText(Html.fromHtml(VlblDanger5.getText() + "<font color=red>পূর্ববর্তী প্রসবে প্রসবপূব রক্তক্ষরণ, প্রসবোত্তর রক্তক্ষরণ অথবা জরায়ুতে গর্ভফু্ল আঁটকে থাকার ইতিহাস থাকলে</font>"));
                            chkDengersine5.setEnabled(false);
                        }

                        if (risk1.equals("5") | risk2.equals("5") | risk3.equals("5") | risk4.equals("5") | risk1.equals("6") | risk2.equals("6") | risk3.equals("6") | risk4.equals("6")) {
                            chkDengersine6.setChecked(true);
                            VlblDanger6.setText("");
                            VlblDanger6.setText(Html.fromHtml(VlblDanger6.getText() + "<font color=red>মৃত জন্মের বা নবজাতকের মৃতের ইতিহাস থাকলে</font>"));
                            chkDengersine6.setEnabled(false);
                        }

                        if (risk1.equals("9") | risk2.equals("9") | risk3.equals("9") | risk4.equals("9")) {
                            chkDengersine7.setChecked(true);
                            VlblDanger7.setText("");
                            VlblDanger7.setText(Html.fromHtml(VlblDanger7.getText() + "<font color=red>সিজারিয়ান অপারেশন বা যন্ত্রের মাধ্যমে (ফরসেফ/ ভেকুয়াম) প্রসবের ইতিহাস থাকলে</font>"));
                            chkDengersine7.setEnabled(false);
                        }
                    } else if (riskStatus.length() == 9) {
                        String risk1 = risk[0].toString();
                        String risk2 = risk[1].toString();
                        String risk3 = risk[2].toString();
                        String risk4 = risk[3].toString();
                        String risk5 = risk[4].toString();

                        if (risk1.equals("1") | risk2.equals("1") | risk3.equals("1") | risk4.equals("1") | risk5.equals("1") | risk1.equals("4") | risk2.equals("4") | risk3.equals("4") | risk4.equals("4") | risk5.equals("4")) {
                            chkDengersine5.setChecked(true);
                            VlblDanger5.setText("");
                            VlblDanger5.setText(Html.fromHtml(VlblDanger5.getText() + "<font color=red>পূর্ববর্তী প্রসবে প্রসবপূব রক্তক্ষরণ, প্রসবোত্তর রক্তক্ষরণ অথবা জরায়ুতে গর্ভফু্ল আঁটকে থাকার ইতিহাস থাকলে</font>"));
                            chkDengersine5.setEnabled(false);
                        }

                        if (risk1.equals("5") | risk2.equals("5") | risk3.equals("5") | risk4.equals("5") | risk5.equals("5") | risk1.equals("6") | risk2.equals("6") | risk3.equals("6") | risk4.equals("6") | risk5.equals("6")) {
                            chkDengersine6.setChecked(true);
                            VlblDanger6.setText("");
                            VlblDanger6.setText(Html.fromHtml(VlblDanger6.getText() + "<font color=red>মৃত জন্মের বা নবজাতকের মৃতের ইতিহাস থাকলে</font>"));
                            chkDengersine6.setEnabled(false);
                        }

                        if (risk1.equals("9") | risk2.equals("9") | risk3.equals("9") | risk4.equals("9") | risk5.equals("9")) {
                            chkDengersine7.setChecked(true);
                            VlblDanger7.setText("");
                            VlblDanger7.setText(Html.fromHtml(VlblDanger7.getText() + "<font color=red>সিজারিয়ান অপারেশন বা যন্ত্রের মাধ্যমে (ফরসেফ/ ভেকুয়াম) প্রসবের ইতিহাস থাকলে</font>"));
                            chkDengersine7.setEnabled(false);
                        }
                    } else if (riskStatus.length() == 11) {
                        String risk1 = risk[0].toString();
                        String risk2 = risk[1].toString();
                        String risk3 = risk[2].toString();
                        String risk4 = risk[3].toString();
                        String risk5 = risk[4].toString();
                        String risk6 = risk[5].toString();

                        if (risk1.equals("1") | risk2.equals("1") | risk3.equals("1") | risk4.equals("1") | risk5.equals("1") | risk6.equals("1") | risk1.equals("4") | risk2.equals("4") | risk3.equals("4") | risk4.equals("4") | risk5.equals("4") | risk6.equals("4")) {
                            chkDengersine5.setChecked(true);
                            VlblDanger5.setText("");
                            VlblDanger5.setText(Html.fromHtml(VlblDanger5.getText() + "<font color=red>পূর্ববর্তী প্রসবে প্রসবপূব রক্তক্ষরণ, প্রসবোত্তর রক্তক্ষরণ অথবা জরায়ুতে গর্ভফু্ল আঁটকে থাকার ইতিহাস থাকলে</font>"));
                            chkDengersine5.setEnabled(false);
                        }

                        if (risk1.equals("5") | risk2.equals("5") | risk3.equals("5") | risk4.equals("5") | risk5.equals("5") | risk6.equals("5") | risk1.equals("6") | risk2.equals("6") | risk3.equals("6") | risk4.equals("6") | risk5.equals("6") | risk6.equals("6"))

                        {
                            chkDengersine6.setChecked(true);
                            VlblDanger6.setText("");
                            VlblDanger6.setText(Html.fromHtml(VlblDanger6.getText() + "<font color=red>মৃত জন্মের বা নবজাতকের মৃতের ইতিহাস থাকলে</font>"));
                            chkDengersine6.setEnabled(false);
                        }

                        if (risk1.equals("9") | risk2.equals("9") | risk3.equals("9") | risk4.equals("9") | risk5.equals("9") | risk6.equals("9")) {
                            chkDengersine7.setChecked(true);
                            VlblDanger7.setText("");
                            VlblDanger7.setText(Html.fromHtml(VlblDanger7.getText() + "<font color=red>সিজারিয়ান অপারেশন বা যন্ত্রের মাধ্যমে (ফরসেফ/ ভেকুয়াম) প্রসবের ইতিহাস থাকলে</font>"));
                            chkDengersine7.setEnabled(false);
                        }
                    } else if (riskStatus.length() == 13) {
                        String risk1 = risk[0].toString();
                        String risk2 = risk[1].toString();
                        String risk3 = risk[2].toString();
                        String risk4 = risk[3].toString();
                        String risk5 = risk[4].toString();
                        String risk6 = risk[5].toString();
                        String risk7 = risk[6].toString();

                        if (risk1.equals("1") | risk2.equals("1") | risk3.equals("1") | risk4.equals("1") | risk5.equals("1") | risk6.equals("1") | risk7.equals("1") | risk1.equals("4") | risk2.equals("4") | risk3.equals("4") | risk4.equals("4") | risk5.equals("4") | risk6.equals("4") | risk7.equals("4")) {
                            chkDengersine5.setChecked(true);
                            VlblDanger5.setText("");
                            VlblDanger5.setText(Html.fromHtml(VlblDanger5.getText() + "<font color=red>পূর্ববর্তী প্রসবে প্রসবপূব রক্তক্ষরণ, প্রসবোত্তর রক্তক্ষরণ অথবা জরায়ুতে গর্ভফু্ল আঁটকে থাকার ইতিহাস থাকলে</font>"));
                            chkDengersine5.setEnabled(false);
                        }

                        if (risk1.equals("5") | risk2.equals("5") | risk3.equals("5") | risk4.equals("5") | risk5.equals("5") | risk6.equals("5") | risk7.equals("6") | risk1.equals("6") | risk2.equals("6") | risk3.equals("6") | risk4.equals("6") | risk5.equals("6") | risk6.equals("6") | risk7.equals("6"))

                        {
                            chkDengersine6.setChecked(true);
                            VlblDanger6.setText("");
                            VlblDanger6.setText(Html.fromHtml(VlblDanger6.getText() + "<font color=red>মৃত জন্মের বা নবজাতকের মৃতের ইতিহাস থাকলে</font>"));
                            chkDengersine6.setEnabled(false);
                        }

                        if (risk1.equals("9") | risk2.equals("9") | risk3.equals("9") | risk4.equals("9") | risk5.equals("9") | risk6.equals("9") | risk7.equals("9")) {
                            chkDengersine7.setChecked(true);
                            VlblDanger7.setText("");
                            VlblDanger7.setText(Html.fromHtml(VlblDanger7.getText() + "<font color=red>সিজারিয়ান অপারেশন বা যন্ত্রের মাধ্যমে (ফরসেফ/ ভেকুয়াম) প্রসবের ইতিহাস থাকলে</font>"));
                            chkDengersine7.setEnabled(false);
                        }
                    } else if (riskStatus.length() == 15) {
                        String risk1 = risk[0].toString();
                        String risk2 = risk[1].toString();
                        String risk3 = risk[2].toString();
                        String risk4 = risk[3].toString();
                        String risk5 = risk[4].toString();
                        String risk6 = risk[5].toString();
                        String risk7 = risk[6].toString();
                        String risk8 = risk[7].toString();

                        if (risk1.equals("1") | risk2.equals("1") | risk3.equals("1") | risk4.equals("1") | risk5.equals("1") | risk6.equals("1") | risk7.equals("1") | risk8.equals("1") | risk1.equals("4") | risk2.equals("4") | risk3.equals("4") | risk4.equals("4") | risk5.equals("4") | risk6.equals("4") | risk7.equals("4") | risk8.equals("4")) {
                            chkDengersine5.setChecked(true);
                            VlblDanger5.setText("");
                            VlblDanger5.setText(Html.fromHtml(VlblDanger5.getText() + "<font color=red>পূর্ববর্তী প্রসবে প্রসবপূব রক্তক্ষরণ, প্রসবোত্তর রক্তক্ষরণ অথবা জরায়ুতে গর্ভফু্ল আঁটকে থাকার ইতিহাস থাকলে</font>"));
                            chkDengersine5.setEnabled(false);
                        }
                        if (risk1.equals("5") | risk2.equals("5") | risk3.equals("5") | risk4.equals("5") | risk5.equals("5") | risk6.equals("5") | risk7.equals("5") | risk8.equals("5") | risk1.equals("6") | risk2.equals("6") | risk3.equals("6") | risk4.equals("6") | risk5.equals("6") | risk6.equals("6") | risk7.equals("6") | risk8.equals("6"))

                        {
                            chkDengersine6.setChecked(true);
                            VlblDanger6.setText("");
                            VlblDanger6.setText(Html.fromHtml(VlblDanger6.getText() + "<font color=red>মৃত জন্মের বা নবজাতকের মৃতের ইতিহাস থাকলে</font>"));
                            chkDengersine6.setEnabled(false);
                        }

                        if (risk1.equals("9") | risk2.equals("9") | risk3.equals("9") | risk4.equals("9") | risk5.equals("9") | risk6.equals("9") | risk7.equals("9") | risk8.equals("9")) {
                            chkDengersine7.setChecked(true);
                            VlblDanger7.setText("");
                            VlblDanger7.setText(Html.fromHtml(VlblDanger7.getText() + "<font color=red>সিজারিয়ান অপারেশন বা যন্ত্রের মাধ্যমে (ফরসেফ/ ভেকুয়াম) প্রসবের ইতিহাস থাকলে</font>"));
                            chkDengersine7.setEnabled(false);
                        }
                    } else if (riskStatus.length() == 17) {
                        String risk1 = risk[0].toString();
                        String risk2 = risk[1].toString();
                        String risk3 = risk[2].toString();
                        String risk4 = risk[3].toString();
                        String risk5 = risk[4].toString();
                        String risk6 = risk[5].toString();
                        String risk7 = risk[6].toString();
                        String risk8 = risk[7].toString();
                        String risk9 = risk[8].toString();

                        if (risk1.equals("1") | risk2.equals("1") | risk3.equals("1") | risk4.equals("1") | risk5.equals("1") | risk6.equals("1") | risk7.equals("1") | risk8.equals("1") | risk9.equals("1") | risk1.equals("4") | risk2.equals("4") | risk3.equals("4") | risk4.equals("4") | risk5.equals("4") | risk6.equals("4") | risk7.equals("4") | risk8.equals("4") | risk9.equals("4")) {
                            chkDengersine5.setChecked(true);
                            VlblDanger5.setText("");
                            VlblDanger5.setText(Html.fromHtml(VlblDanger5.getText() + "<font color=red>পূর্ববর্তী প্রসবে প্রসবপূব রক্তক্ষরণ, প্রসবোত্তর রক্তক্ষরণ অথবা জরায়ুতে গর্ভফু্ল আঁটকে থাকার ইতিহাস থাকলে</font>"));
                            chkDengersine5.setEnabled(false);
                        }

                        if (risk1.equals("5") | risk2.equals("5") | risk3.equals("5") | risk4.equals("5") | risk5.equals("5") | risk6.equals("5") | risk7.equals("5") | risk8.equals("5") | risk9.equals("5") | risk1.equals("6") | risk2.equals("6") | risk3.equals("6") | risk4.equals("6") | risk5.equals("6") | risk6.equals("6") | risk7.equals("6") | risk8.equals("6") | risk9.equals("6"))

                        {
                            chkDengersine6.setChecked(true);
                            VlblDanger6.setText("");
                            VlblDanger6.setText(Html.fromHtml(VlblDanger6.getText() + "<font color=red>মৃত জন্মের বা নবজাতকের মৃতের ইতিহাস থাকলে</font>"));
                            chkDengersine6.setEnabled(false);
                        }

                        if (risk1.equals("9") | risk2.equals("9") | risk3.equals("9") | risk4.equals("9") | risk5.equals("9") | risk6.equals("9") | risk7.equals("9") | risk8.equals("9") | risk9.equals("9")) {
                            chkDengersine7.setChecked(true);
                            VlblDanger7.setText("");
                            VlblDanger7.setText(Html.fromHtml(VlblDanger7.getText() + "<font color=red>সিজারিয়ান অপারেশন বা যন্ত্রের মাধ্যমে (ফরসেফ/ ভেকুয়াম) প্রসবের ইতিহাস থাকলে</font>"));
                            chkDengersine7.setEnabled(false);
                        }
                    } else {

                    }


            } else {

            }


            //6
            if (!GetStillbirthBirth().equalsIgnoreCase("null") && !GetStillbirthBirth().equalsIgnoreCase("")) {
                if (Integer.valueOf(GetStillbirthBirth()) < 1) {

                }
                if (Integer.valueOf(GetStillbirthBirth()) >= 1) {
                    chkDengersine6.setChecked(true);
                    VlblDanger6.setText("");
                    VlblDanger6.setText(Html.fromHtml(VlblDanger6.getText() + "<font color=red>মৃত জন্মের বা নবজাতকের মৃতের ইতিহাস থাকলে </font>"));
                }
            }

            if (chkDengersine1.isChecked() == true || chkDengersine2.isChecked() == true ||
                    chkDengersine3.isChecked() == true || chkDengersine4.isChecked() == true ||
                    chkDengersine5.isChecked() == true || chkDengersine6.isChecked() == true || chkDengersine7.isChecked() == true
                    ) {

                // seclblH1.(Color.RED);
                chkDengersine.setChecked(true);
                secReferWomen.setVisibility(View.VISIBLE);
                //  secReferFaci.setVisibility(View.VISIBLE);
            }

            if (chkDengersine1.isChecked() == false && chkDengersine2.isChecked() == false &&
                    chkDengersine3.isChecked() == false && chkDengersine4.isChecked() == false &&
                    chkDengersine5.isChecked() == false && chkDengersine6.isChecked() == false && chkDengersine7.isChecked() == false
                    ) {
                secDengersine.setVisibility(View.GONE);
                secReferWomen.setVisibility(View.GONE);
                secReferFaci.setVisibility(View.GONE);
                chkDengersine.setChecked(false);
            }

            DataSerchPregRefer();

            FPIDataSearch(g.getGeneratedId(), pregnancyNo);


          /*  btnSaveRef.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(spnFaci.getSelectedItemPosition()==0)
                    {
                        Connection.MessageBox(PregRegFPI.this, "কোথায় রেফার করা হয়েছে  সিলেক্ট করুন।");
                        spnFaci.requestFocus();
                        return;
                    }
                    else
                    {
                       // DataPregRefer();
                        Connection.MessageBox(PregRegFPI.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
                        //ReferStatus();
                    }

                }
            });*/

            Button cmdSavePreg = (Button) findViewById(R.id.cmdSavePreg);
            cmdSavePreg.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // DataSavePregnant();
                    DataSave();


                }
            });
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            PregWomenSearch();
            PregWomenLastChildSearch();
        } catch (Exception e) {
            Connection.MessageBox(PregRegFPI.this, e.getMessage());
            return;
        }
    }

    private void DataSave() {
        try {
            String SQL = "";
            String AG = "";

            if (!rdoLMP1.isChecked() & !rdoLMP2.isChecked() & secLMP.isShown()) {
                Connection.MessageBox(PregRegFPI.this, "শেষ মাসিকের তারিখ যাচাই করুন ।");
                rdoLMP1.requestFocus();
                return;
            } else if (!rdoEDD1.isChecked() & !rdoEDD2.isChecked() & secEDD.isShown()) {
                Connection.MessageBox(PregRegFPI.this, "প্রসবের সম্ভাব্য  তারিখ যাচাই করুন ।");
                rdoEDD1.requestFocus();
                return;
            } else if (!rdoPgn1.isChecked() & !rdoPgn2.isChecked() & secPgn.isShown()) {
                Connection.MessageBox(PregRegFPI.this, "বর্তমানে কততম গর্ভ যাচাই করুন ।");
                rdoPgn1.requestFocus();
                return;
            } else if (!rdoLiveSonDau1.isChecked() & !rdoLiveSonDau2.isChecked() & secPgn.isShown()) {
                Connection.MessageBox(PregRegFPI.this, "জীবিত সন্তানের সংখ্যা যাচাই করুন ।");
                rdoLiveSonDau1.requestFocus();
                return;
            } else if (!rdoAgeL1.isChecked() & !rdoAgeL2.isChecked() & secAgeL.isShown()) {
                Connection.MessageBox(PregRegFPI.this, "শেষ সন্তানের বয়স যাচাই করুন ।");
                rdoAgeL1.requestFocus();
                return;
            } else if (!rdoDanger1.isChecked() & !rdoDanger2.isChecked() & secDengersine.isShown()) {
                Connection.MessageBox(PregRegFPI.this, "ঝুঁকিপূর্ণ গর্ভ নির্ণয় যাচাই করুন ।");
                rdoDanger1.requestFocus();
                return;
            } else if (!rdoRefWomen1.isChecked() & !rdoRefWomen2.isChecked() & secDengersine.isShown()) {
                Connection.MessageBox(PregRegFPI.this, "জটিল গর্ভবতী মাকে রেফার করা হয়েছে যাচাই করুন ।");
                rdoRefWomen1.requestFocus();
                return;
            }


            if (!C.Existence("Select healthId from " + TableNameFPI + "  Where healthId='" + g.getGeneratedId() + "' AND pregNo ='" + pregnancyNo + "'")) {
                SQL = "Insert into " + TableNameFPI + "(HealthID,pregNo,providerId,systemEntryDate,Upload)Values('" + g.getGeneratedId() + "','" + pregnancyNo + "','" + g.getProvCode() + "','" + Global.DateTimeNowYMDHMS() + "','2')";
                C.Save(SQL);
            }

            SQL = "Update " + TableNameFPI + " Set Upload='2',";
            SQL += "LMP = '" + (rdoLMP1.isChecked() ? "1" : (rdoLMP2.isChecked() ? "2" : "")) + "',";
            SQL += "EDD = '" + (rdoEDD1.isChecked() ? "1" : (rdoEDD2.isChecked() ? "2" : "")) + "',";
            SQL += "para = '" + (rdoPgn1.isChecked() ? "1" : (rdoPgn2.isChecked() ? "2" : "")) + "',";
            SQL += "gravida = '" + (rdoLiveSonDau1.isChecked() ? "1" : (rdoLiveSonDau2.isChecked() ? "2" : "")) + "',";
            SQL += "lastChildAge = '" + (rdoAgeL1.isChecked() ? "1" : (rdoAgeL2.isChecked() ? "2" : "")) + "',";
            SQL += "riskHistoryNote = '" + (rdoDanger1.isChecked() ? "1" : (rdoDanger2.isChecked() ? "2" : "")) + "',";
            SQL += "pregRefer = '" + (rdoRefWomen1.isChecked() ? "1" : (rdoRefWomen2.isChecked() ? "2" : "")) + "'";

            SQL += " Where healthId='" + g.getGeneratedId() + "' AND pregNo ='" + pregnancyNo + "'";
            C.Save(SQL);
            Connection.MessageBox(PregRegFPI.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
           // finish();

        } catch (Exception e) {
            Connection.MessageBox(PregRegFPI.this, e.getMessage());
            return;
        }
    }

    private void FPIDataSearch(String generatedId, String pregnancyNo) {
        try {
            RadioButton rb;
            String SQL1 = "";
            SQL1 = "Select ifnull(healthId,'') as HealthID,ifnull(LMP,'') as LMP,ifnull(EDD,'') as EDD,ifnull(para,'') as para,ifnull(gravida,'') as gravida,ifnull(lastChildAge,'') as lastChildAge,ifnull(riskHistoryNote,'') as riskHistoryNote,ifnull(pregRefer,'') as pregRefer\n" +
                    "from pregWomenFPI FPI Where HealthID='" + generatedId + "' AND pregNo ='" + pregnancyNo + "'";
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

                if (cur1.getString(cur1.getColumnIndex("LMP")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoLMP1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("LMP")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoLMP2.setChecked(true);
                }
                if (cur1.getString(cur1.getColumnIndex("EDD")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoEDD1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("EDD")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoEDD2.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("para")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoPgn1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("para")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoPgn2.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("gravida")).replace("null", "").toString().equalsIgnoreCase("1")) {

                    rdoLiveSonDau1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("gravida")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoLiveSonDau2.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("lastChildAge")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoAgeL1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("lastChildAge")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoAgeL2.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("riskHistoryNote")).replace("null", "").toString().equalsIgnoreCase("1")) {

                    rdoDanger1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("riskHistoryNote")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoDanger2.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("pregRefer")).replace("null", "").toString().equalsIgnoreCase("1")) {

                    rdoRefWomen1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("pregRefer")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoRefWomen2.setChecked(true);
                }


                cur1.moveToNext();
            }
            cur1.close();
        } catch (Exception e) {
            Connection.MessageBox(PregRegFPI.this, e.getMessage());
            return;
        }
    }

    //Add days with system Date
    public static String getCalculatedDate(String dateFormat, int days) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(dateFormat);
        cal.add(Calendar.DAY_OF_YEAR, days);
        return s.format(new Date(cal.getTimeInMillis()));
    }

    //Add days with user given date
    public static String getCalculatedDate1(String date, String dateFormat, int days) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(dateFormat);
        cal.add(Calendar.DAY_OF_YEAR, days);
        try {
            return s.format(new Date(s.parse(date).getTime()));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            Log.e("TAG", "Error in Parsing Date : " + e.getMessage());
        }
        return null;
    }

    private String VisitNumber() {
        String SQL = "";
        SQL = "Select (ifnull(max(cast(Visit as int)),0)+1)MaxHH from ANC";
        SQL += " where";
        SQL += " dist='" + g.getDistrict() + "' and upz='" + g.getUpazila() + "' and un='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and vill='" + g.getVillage() + "' and ProvType='" + g.getProvType() + "' and ProvCode='" + g.getProvCode() + "' and HHNo='" + g.getHouseholdNo() + "' and SNo='" + txtSNo.getText() + "'";

        String VisitNo = Global.Right(("00" + C.ReturnSingleValue(SQL)), 2);

        return VisitNo;
    }

    private String GetCountSLNoNumber() {

        String SQL = "select ((cast(Count(*) as int))) as Totalno  from PregWomen";
        String Val = String.valueOf(C.ReturnSingleValue(SQL));
        if (Val.equalsIgnoreCase("0")) {
            return "1";
        } else
            return Val;
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
        SQL = "select '0'||(ifnull(max(cast(serviceId as int)),0))MaxserviceId from PregRefer WHERE healthId = " + g.getGeneratedId() + " AND pregNo = " + pregNo;

        String tempserviceID = C.ReturnSingleValue(SQL);


        return String.valueOf((Long.parseLong(tempserviceID) + 1));

        /*if (serviceID.equalsIgnoreCase("1")) {
            return String.valueOf(g.getGeneratedId() + pregNo + serviceID);
        } else {
            return String.valueOf(serviceID);
        }*/
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

    private String GetStillbirthBirth() {
        String SQL = "";
        String SBirth = "";
        SQL = String.format("Select stillbirth from delivery where healthId = '%s'", g.getGeneratedId());
        SBirth = C.ReturnSingleValue(SQL);
        return SBirth;


    }


    private String ELCOLastVisitNum() {
        String SQL = "";
        SQL = "Select max(Visit)MaxHH from ELCOVisit";
        SQL += " where";
        SQL += " dist='" + g.getDistrict() + "' and upz='" + g.getUpazila() + "' and un='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and SNo='" + txtSNo.getText() + "'";

        String VisitNo = Global.Right(("00" + C.ReturnSingleValue(SQL)), 2);

        return VisitNo;
    }

    private void ClearAll() {
        dtpLMP.setText("");
        dtpEDD.setText("");
        spnPgn.setSelection(0);
        txtAgeM.setText("");
        txtAgeY.setText("");
    }

    private void DataSavePreg() {
        AlertDialog.Builder adb = new AlertDialog.Builder(PregRegFPI.this);
        adb.setTitle("Close");

        adb.setMessage("তথ্য সফলভাবে সংরক্ষণ হয়েছে। আপনি কি গর্ভকালীন সেবার তথ্য সংগ্রহ করতে চান?");
        //adb.setNegativeButton("No", null);
        adb.setNegativeButton("না", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
                Intent f2 = new Intent(getApplicationContext(), MemberList.class);
                startActivity(f2);

            }
        });

        adb.setPositiveButton("হ্যাঁ", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //setContentView(R.layout.fwareg_main);
                //TabHost tabHost = getTabHost();
                //((TabHost)findViewById(R.id..tabhost))
                //secPregVisit.setVisibility(View.VISIBLE);

            }
        });
        adb.show();
    }

    private String GetTotalSonGaughter(String HealthId) {

        //String sq = String.format("Select SUM((ifnull((cast(son as int)),0)) + (ifnull((cast(dau as int)),0))) Total from elco WHERE healthId = '%s'", g.getGeneratedId());
        String sq = String.format("Select (ifnull(SUM((ifnull((cast(son as int)),0)) + (ifnull((cast(dau as int)),0))),0)) Total from elco WHERE healthId = '%s'", g.getGeneratedId());
        return C.ReturnSingleValue(sq);
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

    private String GetMaxANCDate() {
        String SQL = "";
        String MaxANCDate = "";
        SQL = "Select ifnull(Max(visitDate),0) from ancService WHERE healthId ='" + g.getGeneratedId() + "' and pregNo=(select Max(pregNo) from ancService WHERE healthId='" + g.getGeneratedId() + "')";
        MaxANCDate = C.ReturnSingleValue(SQL);
        return MaxANCDate;
    }

    /*private String GetMaxANCDate(String HealthId) {
        //Select Max(visitDate) from ancService WHERE healthId='261174' and pregNo=(select Max(pregNo) from ancService WHERE healthId='261174')
        String sq = String.format("Select Max(visitDate) from ancService WHERE healthId = '%s' and pregNo=(select Max(pregNo) from ancService WHERE healthId='%s')", g.getGeneratedId());
        return C.ReturnSingleValue(sq);
    }*/

    private String GetMaxPGN() {
        String SQL = "";
        String MaxPGN = "";
        SQL = "Select  '0'||ifnull(gravida,0)gravida from PregWomen WHERE healthId ='" + g.getGeneratedId() + "' and pregNo=(select Max(pregNo) from PregWomen WHERE healthId='" + g.getGeneratedId() + "')";
        MaxPGN = C.ReturnSingleValue(SQL);
        return MaxPGN;
    }

    private void DataSerchPregRefer() {
        //Ewmt

        String PGNAnc = String.format("Select healthId, pregNo from %s where healthId = '%s' and pregNo = '%s'", TablePregRefer, g.getGeneratedId(), pregnancyNo);
        if (C.Existence(PGNAnc)) {
            rdoReferWomen1.setChecked(true);
        } else {
            rdoReferWomen1.setChecked(false);
        }
    }
   /* private void DataPregRefer() {
        //Ewmt
        String SQL = "";

        String PGNAnc = "select referCenter FROM PregRefer ";
        PGNAnc += " WHERE healthId = '"+ g.getGeneratedId() +"' AND pregNo ='"+ pregnancyNo +"' and serviceId='"+ VlblServiceId.getText() +"'";//visitDate='"+ Global.DateConvertYMD(dtpVDate.getText().toString()) +"'

        //String PGNAnc = String.format("Select healthId, pregNo,serviceId  from %s where healthId = '%s' and pregNo = '%s'",TablePregRefer,g.getGeneratedId(), pregnancyNo,VlblServiceId.getText());
        try {
            if (!C.Existence(PGNAnc)) {
                if (rdoReferWomen1.isChecked() & secReferWomen.isShown()) {
                    if (spnFaci.getSelectedItemPosition() == 0) {
                        Connection.MessageBox(PregRegFPI.this, "কোথায় রেফার করা হয়েছে  সিলেক্ট করুন।");
                        spnFaci.requestFocus();
                        return;
                    }
                }
                String ServiceId = serviceID(pregnancyNo);
                SQL = "INSERT INTO " + TablePregRefer + " (healthId,pregNo,serviceId,providerId,referCenter,systemEntryDate,upload) " +
                        "VALUES (" + "'" + g.getGeneratedId() + "'," + "'" + pregnancyNo + "','" + ServiceId + "'," + "'" + g.getProvCode() + "'," + "'" + Global.Left(spnFaci.getSelectedItem().toString(), 2) + "','" + Global.DateTimeNowYMDHMS() + "','2')";
                C.Save(SQL);
                //ReferStatus();
                DisplayReferStatus();
                VlblServiceId.setText("");
                btnSaveRef.setText("Add");

            } else {
                if (rdoReferWomen1.isChecked() & secReferWomen.isShown()) {
                    if (spnFaci.getSelectedItemPosition() == 0) {
                        Connection.MessageBox(PregRegFPI.this, "কোথায় রেফার করা হয়েছে  সিলেক্ট করুন।");
                        spnFaci.requestFocus();
                        return;
                    }
                }
                SQL = "Update " + TablePregRefer + " Set ";
                SQL += "referCenter = '" + Global.Left(spnFaci.getSelectedItem().toString(), 2) + "',";
                SQL += "modifyDate = '" + Global.DateTimeNowYMDHMS() + "'";
                SQL += "Where healthId='" + g.getGeneratedId() + "' and pregNo='" + pregnancyNo + "' and serviceId='" + VlblServiceId.getText() + "'";
                C.Save(SQL);
                DisplayReferStatus();
                //ReferStatus();
                VlblServiceId.setText("");
                btnSaveRef.setText("Add");
            }
        }
        catch (Exception e) {
            Connection.MessageBox(PregRegFPI.this, e.getMessage());
            return;
        }


    }
*/


  /*  private void DataSavePregnant() {
        try {
            String dob = GetDOB(g.getHealthID());
            Integer DobAge = Global.DateDifferenceYears(dtpLMP.getText().toString(), Global.DateConvertDMY(dob.toString()));
            Integer DiffDNow_VD = Global.DateDifferenceDays(Global.DateNowDMY(), dtpLMP.getText().toString());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = sdf.format(c.getTime());
            Date date1 = sdf.parse(formattedDate);
            Date date5 = sdf.parse(dtpLMP.getText().toString());
            String PrevPGN = "'" + GetMaxPGN() == "0" ? "" : GetMaxPGN() + "";
            if (dtpLMP.getText().toString().length() == 0) {
                Connection.MessageBox(PregRegFPI.this, "সর্বশেষ মাসিকের  তারিখ কত লিখুন।");
                dtpLMP.requestFocus();
                return;
            }*//* else if (DobAge < 12) {
                Connection.MessageBox(PregReg.this, "মহিলার বয়স ১২ বছরের বেশি হতে হবে ");
                dtpLMP.requestFocus();
                return;
            } *//*else if (dtpEDD.getText().toString().length() == 0) {
                Connection.MessageBox(PregRegFPI.this, "সম্ভাব্য প্রসবের তারিখ কত লিখুন।");
                dtpEDD.requestFocus();
                return;
            } else if (spnPgn.getSelectedItemPosition() == 0 & spnPgn.isShown()) {
                Connection.MessageBox(PregRegFPI.this, "বর্তমানে কত তম গর্ভ তালিকা থেকে  সিলেক্ট করুন।।");
                spnPgn.requestFocus();
                return;
            } else if (date5.after(date1)) {
                Connection.MessageBox(PregRegFPI.this, "শেষ মাসিকের তারিখ আজকের তারিখ অপেক্ষা বড় হবে না");
                dtpLMP.requestFocus();
                return;
            } else if (date5.equals(date1)) {
                Connection.MessageBox(PregRegFPI.this, "শেষ মাসিকের তারিখ আজকের তারিখ সমান হবে না");
                dtpLMP.requestFocus();
                return;
            } else if (DiffDNow_VD <= 29) {
                Connection.MessageBox(PregRegFPI.this, "শেষ মাসিকের তারিখ ৩০ দিনের কম হবে না");
                dtpLMP.requestFocus();
                return;
            }
            *//*else if (GetCurrentPregNoFromAncService(pregnancyNo).equals(pregnancyNo)) {
                Connection.MessageBox(PregReg.this, "এ এন সি ভিজিট থাকলে গর্ভবতী মহিলার সাধারন তথ্য সম্পাদন হবে না");
                //dtpLMP.requestFocus();
                return;
            }*//*
            else if (PrevPGN.equalsIgnoreCase("")) {

            } else {

            }


            if (txtLiveSonDau.getText().toString().equalsIgnoreCase("0")) {

            } else {
                if(txtAgeY.getText().toString().length()==0)
                {
                    Connection.MessageBox(PregRegFPI.this, "শেষ সন্তানের বয়স কত বছর লিখুন");
                    txtAgeY.requestFocus();
                    return;
                }
                else if(txtAgeM.getText().toString().length()==0)
                {
                    Connection.MessageBox(PregRegFPI.this, "শেষ সন্তানের বয়স কত মাস লিখুন।");
                    txtAgeM.requestFocus();
                    return;
                }
                *//*if (txtAgeM.getText().toString().length() == 0 && txtAgeY.getText().toString().length() == 0) {
                    Connection.MessageBox(PregReg.this, "শেষ সন্তানের বয়স কত লিখুন।");
                    txtAgeM.requestFocus();
                    return;
                }*//*
            }




            //Ewmt
            String SQL = "";
            String PGNNoNull = PGNNoNull();
            String PGNNo = PGNNo();
            String PregMaxPGNNo = PregMaxPGNNo();
            //  if(DelivPGNStatus.equalsIgnoreCase(""))
            //  {


            //pgn will be increase 1
            String pgnpositionselected = String.valueOf(spnPgn.getSelectedItemPosition());
            // String pregNo = (spnPgn.getSelectedItemPosition()==0?"":Global.Left(spnPgn.getSelectedItem().toString(),2));
            String refCenter = String.valueOf(spnFaci.getSelectedItemPosition());
            String sq = String.format("Select healthId, pregNo from %s where healthId = '%s' and pregNo = '%s'", TableName, g.getGeneratedId(), pregnancyNo);
            if (!C.Existence(sq)) {
                *//*SQL = "INSERT INTO " + TableName + " (healthId , pregNo, providerId, LMP,tempLMP, EDD, gravida, lastChildAge,StartTime, EndTime, systemEntryDate,Upload) " +
                        "VALUES (" + "'" + g.getGeneratedId() + "'," + "'" + pregnancyNo + "'," + "'" + g.getProvCode() + "'," + "'" + Global.DateConvertYMD(dtpLMP.getText().toString()) + "'," +
                        "'" + Global.DateConvertYMD(dtpLMP.getText().toString()) + "'," +
                        "'" + Global.DateConvertYMD(dtpEDD.getText().toString()) + "','" +
                        pgnpositionselected + "'," + "'" + ComputeAge(txtAgeM.getText().toString(), txtAgeY.getText().toString()) + "'," + "'" + StartTime + "'," + "'" +
                        g.CurrentTime24() + "'," + "'" + g.DateNowYMD() + "','2')";

                C.Save(SQL);*//*

                SQL = "INSERT INTO " + TableName + " (healthId , pregNo, providerId, LMP,tempLMP, EDD, gravida, lastChildAge,systemEntryDate,upload) " +
                        "VALUES (" + "'" + g.getGeneratedId() + "'," + "'" + pregnancyNo + "'," + "'" + g.getProvCode() + "'," + "'" + Global.DateConvertYMD(dtpLMP.getText().toString()) + "'," +
                        "'" + Global.DateConvertYMD(dtpLMP.getText().toString()) + "'," +
                        "'" + Global.DateConvertYMD(dtpEDD.getText().toString()) + "','" +
                        pgnpositionselected + "'," + "'" + ComputeAge(txtAgeM.getText().toString(), txtAgeY.getText().toString()) + "','" + Global.DateTimeNowYMDHMS() + "','2')";

                C.Save(SQL);
                if(sqlnew!=null & !sqlnew.equalsIgnoreCase(""))
                {
                    if(sqlnew.length()>0)
                    {
                        C.Save(sqlnew);
                    }
                }
                else if(sqlnew.equalsIgnoreCase(""))
                {

                    if(!C.Existence("Select healthid from ELCO  Where healthid='"+ g.getGeneratedId() +"'"))// and  currStatus = '"+ Global.Left(spnMethod.getSelectedItem().toString(), 2) +"'
                    {
                        String ELCOsql="";
                        ELCOsql = "Insert into ELCO(healthId,providerId,regDT,systemEntryDate,upload)Values(";
                        ELCOsql += "'" + g.getGeneratedId() + "',";
                        ELCOsql += "'" + g.getProvCode() + "',";
                        ELCOsql += "'',";
                        ELCOsql += "'" + Global.DateTimeNowYMDHMS() + "',";
                        ELCOsql += "'2')";
                        C.Save(ELCOsql);
                    }
                    String ELCOupdate="";
                    ELCOupdate = "Update ELCO Set hhStatus = '1',";
                    ELCOupdate += "haHHNo = '',";//Global.DateTimeNowYMDHMS() " + Global.DateConvertYMD(dtpOutcomeDT.getText().toString()) + "
                    ELCOupdate += "elcoNo = '',";
                    ELCOupdate += "husbandName = '',";
                    ELCOupdate += "husbandAge = '',";
                    ELCOupdate += "DOMSource = '',";
                    ELCOupdate += "MarrDate = '',";
                    ELCOupdate += "MarrAge = '',";
                    ELCOupdate += "Son = '',";
                    ELCOupdate += "Dau = '',";
                    ELCOupdate += "modifyDate = '"+ Global.DateTimeNowYMDHMS() +"'";
                    ELCOupdate += " Where HealthID='" + g.getGeneratedId() + "'";
                    C.Save(ELCOupdate);
                }
            }
            SQL = "Update " + TableName + " Set ";
            SQL+="LMP = '"+ Global.DateConvertYMD(dtpLMP.getText().toString()) +"',";
            SQL+="tempLMP = '"+ Global.DateConvertYMD(dtpLMP.getText().toString()) +"',";
            SQL+="EDD = '"+ Global.DateConvertYMD(dtpEDD.getText().toString()) +"',";
            SQL+="gravida = '"+ pgnpositionselected +"',";
            SQL+="lastChildAge = '"+ ComputeAge(txtAgeM.getText().toString(), txtAgeY.getText().toString())+ "',";
            SQL+="modifyDate = '"+ Global.DateTimeNowYMDHMS() + "'";
            SQL+="Where healthId='"+ g.getGeneratedId() +"' and pregNo='"+ pregnancyNo +"'";
            C.Save(SQL);
            if(sqlupdate!=null & !sqlupdate.equalsIgnoreCase("")) {
                if (sqlupdate.length() > 0) {
                    C.Save(sqlupdate);
                }
            }
            else if(sqlupdate.equalsIgnoreCase(""))
            {
                String VisitNo=VisitNumber(g.getGeneratedId());

                if(!C.Existence("Select healthid from elcoVisit  Where healthid='"+ g.getGeneratedId() +"' and Visit='"+ VisitNo +"'"))// and  currStatus = '"+ Global.Left(spnMethod.getSelectedItem().toString(), 2) +"'
                {
                    String elcoVisitsql="";
                    elcoVisitsql = "Insert into elcoVisit(healthId,providerId,visit,systemEntryDate,modifyDate, pregNo)Values(";
                    elcoVisitsql += "'" + g.getGeneratedId() + "',";
                    elcoVisitsql += "'" + g.getProvCode() + "',";
                    elcoVisitsql += "'" + VisitNo + "',";
                    elcoVisitsql += "'" + Global.DateTimeNowYMDHMS() + "',";
                    elcoVisitsql += "'" + Global.DateTimeNowYMDHMS() + "',";
                    elcoVisitsql += "'" + pregnancyNo + "')";
                    C.Save(elcoVisitsql);
                }
                String  elcoVisitupdate="";
                elcoVisitupdate = "Update elcoVisit Set pregNo = '" + pregnancyNo + "',";
                elcoVisitupdate += "vDate = '',";//Global.DateTimeNowYMDHMS() " + Global.DateConvertYMD(dtpOutcomeDT.getText().toString()) + "
                elcoVisitupdate += "visitStatus = '1',";
                elcoVisitupdate += "currStatus = '12',";
                elcoVisitupdate += "newOld = 'M',";
                elcoVisitupdate += "mDate = '',";
                elcoVisitupdate += "sSource = '',";
                elcoVisitupdate += "qty = '',";
                elcoVisitupdate += "unit = '',";
                elcoVisitupdate += "brand = '',";
                elcoVisitupdate += "referPlace = '',";
                elcoVisitupdate += "validity = '',";
                elcoVisitupdate += "dayMonYear = '',";
                elcoVisitupdate += "mrSource = '1',";
                elcoVisitupdate += "MRDate = '',";
                elcoVisitupdate += "MRAge = '0',";
                elcoVisitupdate += "modifyDate = '"+ Global.DateTimeNowYMDHMS() +"',";
                elcoVisitupdate += "syrinsQty = '',";
                elcoVisitupdate += "upload = '2'";
                elcoVisitupdate += " Where HealthID='" + g.getGeneratedId() + "' and Visit='" + VisitNo + "'";
                C.Save(elcoVisitupdate);
            }
            String PGNAnc = String.format("Select healthId, pregNo from %s where healthId = '%s' and pregNo = '%s'", TablePregRefer, g.getGeneratedId(), pregnancyNo);





            if (IsUserHA()) {
                g.setCallFrom("HAregis");
                DataSavePreg();
            }
            else
            {
                g.setCallFrom("regis");
                DataSavePreg();
            }


        }
        catch (Exception e) {
            Connection.MessageBox(PregRegFPI.this, e.getMessage());
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

    private void DisplayReferStatus() {
        GridView gcount = (GridView) findViewById(R.id.gridRF);
        g.setImuCode(String.valueOf(gcount.getCount() + 1));
        ReferStatus();
    }

    public void ReferStatus() {
        GridView g1 = (GridView) findViewById(R.id.gridRF);
        g1.setAdapter(new Refer(this));
        g1.setNumColumns(6);
    }

    public class Refer extends BaseAdapter {
        private Context mContext;
        String[][] vcode;
        Integer totalRec;

        public Refer(Context c) {
            mContext = c;
        }

        public int getCount() {
            return Integer.parseInt(C.ReturnSingleValue("Select count(*)total from PregRefer where healthid='" + g.getGeneratedId() + "' AND pregNo = '" + pregnancyNo + "'"));
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
                MyView = li.inflate(R.layout.tt_item, null);

                //String ServiceId = String.valueOf(vcode[1][position]);
                String SQL = "Select (case when referCenter='01' then 'UHC' \n" +
                        "when referCenter='02' then 'UHFWC' \n" +
                        "when referCenter='03' then 'MCWC' \n" +
                        "when referCenter='04' then 'DH/GOB Hosp.' \n" +
                        "when referCenter='05' then 'NGO Clinic/Hosp.' \n" +
                        "when referCenter='06' then 'Private Clinic/Hosp.' \n" +
                        "when referCenter='77' then 'Others' \n" +
                        "else '' end) referCenter,serviceId from PregRefer where healthid='" + g.getGeneratedId() + "' AND pregNo = '" + pregnancyNo + "'";//order by systemEntryDate  asc date(imudate) asc

                try {
                    Cursor cur = C.ReadData(SQL);
                    cur.moveToFirst();

                    totalRec = cur.getCount();
                    vcode = new String[3][totalRec];
                    int i = 0;
                    while (!cur.isAfterLast()) {
                        vcode[0][i] = "রেফার " + String.valueOf(i + 1);//String.valueOf(cur.getString(cur.getColumnIndex("imucode")));
                        vcode[1][i] = cur.getString(cur.getColumnIndex("referCenter"));
                        vcode[2][i] = cur.getString(cur.getColumnIndex("serviceId"));
                        // vcode[3][i]= String.valueOf(cur.getString(cur.getColumnIndex("imucode")));

                        i += 1;
                        cur.moveToNext();
                    }
                    cur.close();

                    Button tv = (Button) MyView.findViewById(R.id.image_name);
                    tv.setTextSize(14);
                    if (vcode[0][position] != "") {
                        tv.setText(vcode[0][position] + "\n" + vcode[1][position]);
                    }

                    final Integer p = position;
                    tv.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            String ServiceId = String.valueOf(vcode[2][position]);
                            Cursor cur = C.ReadData("Select (case when referCenter='01' then 'UHC' \n" +
                                    "when referCenter='02' then 'UHFWC' \n" +
                                    "when referCenter='03' then 'MCWC' \n" +
                                    "when referCenter='04' then 'DH/GOB Hosp.' \n" +
                                    "when referCenter='05' then 'NGO Clinic/Hosp.' \n" +
                                    "when referCenter='06' then 'Private Clinic/Hosp.' \n" +
                                    "when referCenter='77' then 'Others' \n" +
                                    "else '' end) referCenter,serviceId from PregRefer where healthid='" + g.getGeneratedId() + "' AND pregNo = '" + pregnancyNo + "' AND serviceId = '" + ServiceId + "'");//order by systemEntryDate  asc date(imudate) asc

                            cur.moveToFirst();

                            while (!cur.isAfterLast()) {
                                VlblServiceId.setText(ServiceId);

                                if (vcode[1][position].equals("UHC")) {
                                    spnFaci.setSelection(1);
                                    rdoReferWomen1.setChecked(true);
                                    //rdogrpTTCard.setVisibility(View.VISIBLE);
                                } else if (vcode[1][position].equals("UHFWC")) {
                                    spnFaci.setSelection(2);
                                    rdoReferWomen1.setChecked(true);
                                } else if (vcode[1][position].equals("MCWC")) {
                                    spnFaci.setSelection(3);
                                    rdoReferWomen1.setChecked(true);
                                } else if (vcode[1][position].equals("DH/GOB Hosp.")) {
                                    spnFaci.setSelection(4);
                                    rdoReferWomen1.setChecked(true);
                                } else if (vcode[1][position].equals("NGO Clinic/Hosp.")) {
                                    spnFaci.setSelection(5);
                                    rdoReferWomen1.setChecked(true);
                                } else if (vcode[1][position].equals("Private Clinic/Hosp.")) {
                                    spnFaci.setSelection(6);
                                    rdoReferWomen1.setChecked(true);
                                } else if (vcode[1][position].equals("Others")) {
                                    spnFaci.setSelection(7);
                                    rdoReferWomen1.setChecked(true);
                                }
                                //  if(vcode[1][position].length()!=0)
                                // {
                                //dtpDOTT1.setText(vcode[1][position]);
                                // dtpDOTT1.setVisibility(View.VISIBLE);
                                // btnDOTT1.setVisibility(View.VISIBLE);
                                // }
                                //  else
                                // {
                                //dtpDOTT1.setText("");
                                //dtpDOTT1.setVisibility(View.GONE);
                                //btnDOTT1.setVisibility(View.GONE);
                                //  }

                                //secTT1.setVisibility(View.VISIBLE);
                                // btnTTClose.setVisibility(View.VISIBLE);
                                // btnAddTT.setVisibility(View.GONE);
                                // btnSaveTT.setVisibility(View.VISIBLE);
                                //g.setImuCode(vcode[1][position]);
                                btnSaveRef.setText("Edit");
                                cur.moveToNext();
                            }
                            cur.close();


                        }
                    });
                } catch (Exception ex) {
                    Connection.MessageBox(PregRegFPI.this, ex.getMessage());
                }

            }
            return MyView;
        }

    }

    private Integer ComputeAge(String mon, String yr) {
        Integer totalmonth = 0;
        if (yr.length() > 0) {
            if (Integer.parseInt(yr) >= 1) {
                Integer yearinmonth = Integer.parseInt(yr);
                totalmonth = 12 * yearinmonth;
            }
            if (Integer.parseInt(mon) > 0) {
                totalmonth = totalmonth + Integer.parseInt(mon);
            }
        }
        return totalmonth;

    }

    private void DisplayAge(String mon) {
        Float totalmonth = null;
        if (mon != null) {
            if (mon.length() > 0) {
                if (Integer.parseInt(mon) >= 1) {
                    Integer yearinmonth = Integer.parseInt(mon) / 12;
                    txtAgeY.setText(String.valueOf(yearinmonth));
                    txtAgeM.setText(String.valueOf(Integer.parseInt(mon) - (yearinmonth * 12)));

                }

            }
        }


    }

    private void DataSearch(String healthID) {
        try {
            String SQL = "";
            SQL = "Select SNo as SNo, ifnull(HealthID,'') as HealthID, ifnull(NameEng,'') as NameEng,";
            SQL += "ifnull(Age,'') as Age";
            SQL += " from Member Where HealthID='" + healthID + "'";
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
            Connection.MessageBox(PregRegFPI.this, e.getMessage());
            return;
        }
    }


    private void ELCONoSearch(String SNo) {
        try {
            String SQL = "";
            SQL = "select E.ELCONo as ELCONo from ELCO E Where E.healthId='" + g.getGeneratedId() + "'";
            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                txtELCONo.setText(cur.getString(cur.getColumnIndex("ELCONo")));
                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(PregRegFPI.this, e.getMessage());
            return;
        }
    }

    private String GetCurrentPregNoFromAncService(String pregNo) {
        String SQL = "select (ifnull(max(pregNo),0))pregno from ancService";
        SQL += " Where HealthId='" + g.getGeneratedId() + "' AND pregNo = '" + pregNo + "'";
        String Val = String.valueOf(C.ReturnSingleValue(SQL));
        return Val;
        /*String Val = String.valueOf(C.ReturnSingleValue(SQL));
        if (Val.equalsIgnoreCase("")) {
            return "0";
        } else
            return Val;*/

    }

    private void PregWomenSearch() {
        try {
            String SQL = "";
            txtLiveSonDau.setText(GetTotalSonGaughter(g.getGeneratedId()));


            SQL = "select referCenter from PregRefer";
            SQL += " Where HealthId='" + g.getGeneratedId() + "' and pregNo = '" + pregnancyNo + "'";

            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {

                spnFaci.setSelection(Global.SpinnerItemPosition(spnFaci, 2, cur.getString(cur.getColumnIndex("referCenter"))));
                btnSaveRef.setText("Add");
                // DisplayAge(cur.getString(cur.getColumnIndex("lastChildAge")));

                String sq = String.format("Select healthId, pregNo from %s where healthId = '%s' and pregNo = '%s'", TableANC, g.getGeneratedId(), g.getPregNo());
                if (!C.Existence(sq)) {
                    secPregVisit.setVisibility(View.VISIBLE);

                } else {
                    secPregVisit.setVisibility(View.GONE);
                }

                cur.moveToNext();
            }

            cur.close();

        } catch (Exception e) {
            Log.e("Error from pregwoman", e.getMessage());
            Connection.MessageBox(PregRegFPI.this, e.getMessage());
            return;
        }
    }

    private void PregWomenLastChildSearch() {
        try {
            String SQL = "";


            SQL = "select lastChildAge from pregWomen";
            SQL += " Where HealthId='" + g.getGeneratedId() + "' and pregNo = '" + pregnancyNo + "'";

            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {


                DisplayAge(cur.getString(cur.getColumnIndex("lastChildAge")));


                cur.moveToNext();
            }

            cur.close();

        } catch (Exception e) {
            Log.e("Error from pregwoman", e.getMessage());
            Connection.MessageBox(PregRegFPI.this, e.getMessage());
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

            if (VariableID.equals("btnLMP")) {
                dtpDate = (EditText) findViewById(R.id.dtpLMP);

            } else if (VariableID.equals("btnVDate")) {
                dtpDate = (EditText) findViewById(R.id.dtpVDate);
            }


            dtpDate.setText(new StringBuilder()
                    .append(Global.Right("00" + mDay, 2)).append("/")
                    .append(Global.Right("00" + mMonth, 2)).append("/")
                    .append(mYear));

            dtpEDD.setText(Global.addDays(dtpLMP.getText().toString(), 280));
            //comments Integer DiffLMP_VD = Global.DateDifferenceDays(dtpVDate.getText().toString(), dtpLMP.getText().toString());
            Integer DiffDNow_VD = Global.DateDifferenceDays(Global.DateNowDMY(), dtpLMP.getText().toString());
            Integer DiffDNow_OC = Global.DateDifferenceDays(dtpLMP.getText().toString(), GetMaxOutComeDate());

//comments
            /*if (DiffLMP_VD >= 224) {
                secMiso.setVisibility(View.VISIBLE);
            } else {
                secMiso.setVisibility(View.GONE);
            }*/

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = sdf.format(c.getTime());
                String PrevAncVisit = "'" + GetMaxANCDate() == "0" ? "" : GetMaxANCDate() + "";// '"+  GetMaxANCDate()==0?"":GetMaxANCDate()) +"'";
                //Date MaxAncVisitDate = sdf.parse(Global.DateConvertDMY(PrevAncVisit));
                Date date1 = sdf.parse(formattedDate);
                //comments Date date4 = sdf.parse(dtpVDate.getText().toString());
                Date date5 = sdf.parse(dtpLMP.getText().toString());
                //comments Date date2 = sdf.parse(dtpVDate.getText().toString());

                String DOB = GetDOB(g.getGeneratedId());
                Integer DobAge = Global.DateDifferenceYears(dtpLMP.getText().toString(), Global.DateConvertDMY(DOB.toString()));
                //comments
               /* if (date4.after(date1)) {
                    if (VariableID.equals("btnVDate")) {
                        Connection.MessageBox(PregReg.this, "ভিজিটের  তারিখ আজকের তারিখ অপেক্ষা বড় হবে না");
                    }
                    //dtpVDate.setText(null);
                    dtpVDate.requestFocus();
                } else if (date4.before(date5)) {
                    if (VariableID.equals("btnVDate")) {
                        Connection.MessageBox(PregReg.this, "ভিজিটের  তারিখ শেষ মাসিকের তারিখ এবং সিস্টেমের তারিখের মধ্যে হবে");
                    }
                    //dtpVDate.setText(null);
                    dtpVDate.requestFocus();
                } else if (DiffLMP_VD >= 300) {
                    if (VariableID.equals("btnVDate")) {
                        Connection.MessageBox(PregReg.this, "ভিজিটের  তারিখ শেষ মাসিকের তারিখ থেকে ৩০০ দিনের বেশি হতে পারে না।");
                    }
                    //dtpVDate.setText(null);
                    dtpVDate.requestFocus();
                }
                if (PrevAncVisit.equalsIgnoreCase("0")) {

                } else {
                    Date MaxAncVisitDate = sdf.parse(Global.DateConvertDMY(PrevAncVisit));
                    if (date4.before(MaxAncVisitDate)) {
                        if (VariableID.equals("btnVDate")) {
                            Connection.MessageBox(PregReg.this, "পূর্বের ভিজিটের তারিখ অপেক্ষা বর্তমান ভিজিট বড় হতে হবে।");
                        }
                        //dtpVDate.setText(null);
                        dtpVDate.requestFocus();
                    }
                }*/

                if (date5.after(date1)) {
                    if (VariableID.equals("btnLMP")) {
                        Connection.MessageBox(PregRegFPI.this, "শেষ মাসিকের তারিখ আজকের তারিখ অপেক্ষা বড় হবে না");
                    }
                    //dtpLMP.setText(null);
                    dtpLMP.requestFocus();

                    //return;
                } else if (date5.equals(date1)) {
                    if (VariableID.equals("btnLMP")) {
                        Connection.MessageBox(PregRegFPI.this, "শেষ মাসিকের তারিখ আজকের তারিখ সমান হবে না");
                    }
                    //dtpLMP.setText(null);
                    dtpLMP.requestFocus();
                } else if (DiffDNow_VD <= 29) {
                    if (VariableID.equals("btnLMP")) {
                        Connection.MessageBox(PregRegFPI.this, "শেষ মাসিকের তারিখ ৩০ দিনের কম হবে না");
                    }
                    //dtpLMP.setText(null);
                    dtpLMP.requestFocus();
                } else if (DobAge < 12) {
                    if (VariableID.equals("btnLMP")) {
                        Connection.MessageBox(PregRegFPI.this, "মহিলার বয়স ১২ বছরের বেশি হতে হবে ");
                    }

                    //comments dtpVDate.requestFocus();
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

                // String SQL  = "Select imucode as imucode,imudate as imudate,imuCard as imucard from Immunization where healthid='"+ g.getGeneratedId() +"' order by cast(imudate as date) desc";

                try {
                    //Cursor cur = C.ReadData(SQL);
                    // cur.moveToFirst();

                    totalRec = 4;//cur.getCount();
                    vcode = new String[4][totalRec];
                    int i = 0;
                    // while(!cur.isAfterLast())
                    //{
                    //dtpEDD.setText(Global.addDays(dtpLMP.getText().toString(), 280));

                    /*vcode[0][0] = "পরিদর্শন 1 " + Global.addDays(dtpLMP.getText().toString(), 120);
                    vcode[0][1] = "পরিদর্শন 2 " + Global.addDays(dtpLMP.getText().toString(), 180);
                    vcode[0][2] = "পরিদর্শন 3 " + Global.addDays(dtpLMP.getText().toString(), 240);
                    vcode[0][3] = "পরিদর্শন 4 " + Global.addDays(dtpLMP.getText().toString(), 270);
                    */
                    vcode[0][0] = "পরিদর্শন 1 " + Global.addDays(dtpLMP.getText().toString(), 106) + "                           হতে                         " + Global.addDays(dtpLMP.getText().toString(), 112);
                    vcode[0][1] = "পরিদর্শন 2 " + Global.addDays(dtpLMP.getText().toString(), 162) + "                                   হতে                         " + Global.addDays(dtpLMP.getText().toString(), 196);
                    vcode[0][2] = "পরিদর্শন 3 " + Global.addDays(dtpLMP.getText().toString(), 218) + "                                            হতে                         " + Global.addDays(dtpLMP.getText().toString(), 224);
                    vcode[0][3] = "পরিদর্শন 4 " + Global.addDays(dtpLMP.getText().toString(), 246) + "                                                     হতে                         " + Global.addDays(dtpLMP.getText().toString(), 252);
                    //+ String.valueOf(cur.getString(cur.getColumnIndex("imucode")));
                    // vcode[1][i]= "ANC 2 - "+ Global.DateConvertYMD(dtpLMP.getText().toString());//cur.getString(cur.getColumnIndex("imudate"));
                    //vcode[2][i]= "ANC 3 - "+ Global.DateConvertYMD(dtpLMP.getText().toString());//cur.getString(cur.getColumnIndex("imucard"));
                    // vcode[3][i]= "ANC 4 - "+ Global.DateConvertYMD(dtpLMP.getText().toString());//String.valueOf(cur.getString(cur.getColumnIndex("imucode")));

                    i += 1;
                    // cur.moveToNext();
                    //  }
                    //cur.close();

                    Button tv = (Button) MyView.findViewById(R.id.image_name);
                    tv.setTextSize(14);
                    //tv.setText(vcode[0][position] + "\n" + vcode[1][position]);
                    tv.setText(vcode[0][position] + "\n");
                    final Integer p = position;
                /*    tv.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                           // ChkTT1.setChecked(true);
                            if(vcode[2][position].equals("1"))
                            {
                               // rdoCardYes.setChecked(true);
                            }
                            else if(vcode[2][position].equals("2"))
                            {
                               // rdoCardNo.setChecked(true);
                            }

                            if(vcode[1][position].length()!=0)
                            {
                               // dtpDOTT1.setText(vcode[1][position]);
                            }
                            else
                            {
                              //  dtpDOTT1.setText("");
                            }

                          //  secTT1.setVisibility(View.VISIBLE);
                          //  btnTTClose.setVisibility(View.VISIBLE);
                           // btnAddTT.setVisibility(View.GONE);
                            g.setImuCode(vcode[3][position]);
                        }
                    });*/
                } catch (Exception ex) {
                    Connection.MessageBox(PregRegFPI.this, ex.getMessage());
                }

            }
            return MyView;
        }

    }

}

