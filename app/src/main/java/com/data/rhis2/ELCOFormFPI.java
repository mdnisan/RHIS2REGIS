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
 * Created by Nisan on 4/24/2016.
 */
public class ELCOFormFPI extends Activity {
    boolean netwoekAvailable = false;
    Location currentLocation;
    double currentLatitude, currentLongitude;
    Location currentLocationNet;
    double currentLatitudeNet, currentLongitudeNet;
    Calendar c = Calendar.getInstance();

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
        AlertDialog.Builder adb = new AlertDialog.Builder(ELCOFormFPI.this);
        switch (item.getItemId()) {
            case R.id.menuClose:
                adb.setTitle("Close");
                adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান?");
                adb.setNegativeButton("না", null);
                adb.setPositiveButton("হ্যাঁ", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (g.getCallFrom().equals("1")) {
                            finish();
                            Intent f2 = new Intent(getApplicationContext(), Elcoview.class);
                            startActivity(f2);
                        } else {
                            finish();
                            Intent f2 = new Intent(getApplicationContext(), MemberListFPI.class);
                            startActivity(f2);
                            g.setGeneratedId("");
                        }
                     /*   finish();
                        Intent f2 = new Intent(getApplicationContext(),MemberListFPI.class);
                        startActivity(f2);*/
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
    private String TableNameELCOVisit;


    TextView txtHealthID;
    TextView txtPHHNo;

    LinearLayout seclblH;
    LinearLayout secDiv;
    TextView VlblDiv;
    EditText txtDiv;

    ImageButton btnPlus;
    ImageButton btnMinus;

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
    LinearLayout secHHNo1;
    LinearLayout secHHNo;
    TextView VlblHHNo;

    CheckBox chkHHNoDontKnow;
    EditText txtHHNo;

    LinearLayout secSNo;
    TextView VlblSNo;
    TextView txtSNo;
    TextView txtSS;
    TextView txtSS1;
    TextView VlblDOB;
    TextView DOB;

    LinearLayout secName;
    TextView VlblName;
    TextView txtName;

    LinearLayout secAge;
    TextView VlblAge;
    TextView txtAge;

    LinearLayout secHusName;
    LinearLayout secHusName1;
    LinearLayout secDOMSource1;
    LinearLayout secELCONo;
    TextView VlblHus;
    EditText txtHusName;
    EditText txtHusAge;

    LinearLayout secMEdu;
    TextView VlblMEdu;
    TextView txtMEdu;

    LinearLayout secMNID;
    TextView VlblMNID;
    TextView txtMNID;


    LinearLayout secMBRID;
    TextView VlblMBRID;
    TextView txtMBRID;

    LinearLayout secMMNo;
    TextView VlblMMNo;
    TextView txtMMNo;

    EditText txtELCONo;
    LinearLayout secHTT;
    TextView VlblHTT;

    LinearLayout secTTCardStatus;
    TextView VlblTTCardStatus;

    LinearLayout secTT1;
    TextView VlblTT1;
    CheckBox ChkTT1;
    TextView VlblTTCard;
    RadioGroup rdogrpTTCard;
    RadioButton rdoCardYes;
    RadioButton rdoCardNo;
    EditText dtpDOTT1;
    ImageButton btnDOTT1;

    LinearLayout secDOM;
    TextView VlblDOM;
    EditText dtpDOM;
    ImageButton btnDOM;
    LinearLayout secMAge;
    TextView VlblMAge;
    EditText txtMAge;

    LinearLayout secDOMSource;
    TextView VlblDOMSource;
    RadioGroup rdogrpDOMSource;
    RadioButton rdoDOMSource1;
    RadioButton rdoDOMSource2;
    LinearLayout secMethod1;
    LinearLayout secVSPA;
    LinearLayout secLiveSon;
    LinearLayout secLiveDau;
    LinearLayout secElcoVisit;
    TextView VlblLiveSon;
    EditText txtLiveSon;

    TextView VlblLiveDau;
    EditText txtLiveDau;

    LinearLayout secDOV;
    TextView VlblDOV;
    EditText dtpDOV;
    ImageButton btnDOV;

    Button cmdPreVisits;

    LinearLayout secVS;
    RadioGroup rdogrpVS;
    RadioButton rdoVSPresent;
    RadioButton rdoVSAbsent;

    LinearLayout secSS;
    RadioGroup rdogrpSS;
    RadioButton rdoSSOtherGovt;
    RadioButton rdoSSFWA;
    RadioButton rdoSSLMarket;
    RadioButton rdoSSOther;

    LinearLayout secMethod;
    TextView VlblMethod;
    Spinner spnMethod;

    LinearLayout secMethodUnit;
    Spinner spnMethodUnit;
    EditText txtMethodQty;
    EditText txtSyringeQty;
    TextView dtDueDoze;
    LinearLayout secSyringe;
    LinearLayout secSyringe1;
    LinearLayout secBrand;
    Spinner spnBrand;

    LinearLayout secMethodReceveDT;
    LinearLayout secMethodReceveDT1;
    TextView VlblMethodReceveDT;
    RadioGroup rdogrpMethodReceveDT;
    RadioButton rdoMethodReceveDT1;
    RadioButton rdoMethodReceveDT2;
    EditText txtMDAge;
    TextView VlblMDAge;
    EditText dtpMD;
    ImageButton btnMD;

    LinearLayout secWherePlace;
    TextView VlblWherePlace;
    Spinner spnWherePlace;

    LinearLayout secDetail;

    String StartTime;
    LinearLayout secTT1a;
    LinearLayout secTT2b;
    LinearLayout secTT3c;
    LinearLayout secTT4d;
    LinearLayout secTT5e;
    LinearLayout secValidity;
    EditText txtValidity;
    Spinner spnValidityUnit;
    GridView g1;

    ImageButton btnAddTT;
    ImageButton btnSaveTT;
    ImageButton btnTTClose;
    TextView txtVisitNo;
    TextView tvMAge;
    TextView tvMAge1;
    TextView Sex;
    String LMP = "";
    String pregnancyNo = "";
    Context conxt;
    Button cmdSave;
    LinearLayout secMessage;
    TextView VlblMessage;
    TextView VlblMessageMeso;

    //..............FPI.............
    RadioGroup rdogrpHusName;
    RadioButton rdoHusName1;
    RadioButton rdoHusName2;

    RadioGroup rdogrpHusAge;
    RadioButton rdoHusAge1;
    RadioButton rdoHusAge2;

    RadioGroup rdogrpELCONo;
    RadioButton rdoELCONo1;
    RadioButton rdoELCONo2;

    RadioGroup rdogrpHHNo;
    RadioButton rdoHHNo1;
    RadioButton rdoHHNo2;

    RadioGroup rdogrpDOMSou;
    RadioButton rdoDOMSou1;
    RadioButton rdoDOMSou2;

    RadioGroup rdogrpHHNoNoDontKnow;
    RadioButton rdoHHNoNoDontKnow1;
    RadioButton rdoHHNoNoDontKnow2;

    RadioGroup rdogrpMAge;
    RadioButton rdoMAge1;
    RadioButton rdoMAge2;

    RadioGroup rdogrpLiveSon;
    RadioButton rdoLiveSon1;
    RadioButton rdoLiveSon2;


    RadioGroup rdogrpLiveDau;
    RadioButton rdoLiveDau1;
    RadioButton rdoLiveDau2;

    RadioGroup rdogrpTT1;
    RadioButton rdoTT11;
    RadioButton rdoTT12;

    RadioGroup rdogrpTT2;
    RadioButton rdoTT21;
    RadioButton rdoTT22;

    RadioGroup rdogrpTT3;
    RadioButton rdoTT31;
    RadioButton rdoTT32;

    RadioGroup rdogrpTT4;
    RadioButton rdoTT41;
    RadioButton rdoTT42;
    RadioGroup rdogrpTT5;
    RadioButton rdoTT51;
    RadioButton rdoTT52;

    RadioGroup rdogrpElcoVisit;
    RadioButton rdoElcoVisit1;
    RadioButton rdoElcoVisit2;

    RadioGroup rdogrpVSPA;
    RadioButton rdoVSPA1;
    RadioButton rdoVSPA2;
    RadioGroup rdogrpMethod;
    RadioButton rdoMethod1;
    RadioButton rdoMethod2;

    RadioGroup rdogrpSorce;
    RadioButton rdoSorce1;
    RadioButton rdoSorce2;

    RadioGroup rdogrpBrand;
    RadioButton rdoBrand1;
    RadioButton rdoBrand2;

    RadioGroup rdogrpMethodQty;
    RadioButton rdoMethodQty1;
    RadioButton rdoMethodQty2;

    RadioGroup rdogrpSyringeQty;
    RadioButton rdoSyringeQty1;
    RadioButton rdoSyringeQty2;

    RadioGroup rdogrpDueDoze;
    RadioButton rdoDueDoze1;
    RadioButton rdoDueDoze2;

    RadioGroup rdogrpMethodReceve;
    RadioButton rdoMethodReceve1;
    RadioButton rdoMethodReceve2;

    RadioGroup rdogrpMDAge;
    RadioButton rdoMDAge1;
    RadioButton rdoMDAge2;

    RadioGroup rdogrpWherePlace;
    RadioButton rdoWherePlace1;
    RadioButton rdoWherePlace2;

    RadioGroup rdogrpValidity;
    RadioButton rdoValidity1;
    RadioButton rdoValidity2;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.elcofpi);

            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();
            conxt = this;
            TableName = "elcoFPI";
            TableNameELCOVisit = "elcoVisitFPI";


            secMessage = (LinearLayout) findViewById(R.id.secMessage);
            VlblMessage = (TextView) findViewById(R.id.VlblMessage);
            VlblMessageMeso = (TextView) findViewById(R.id.VlblMessageMeso);
            VlblMessage.setVisibility(View.GONE);
            VlblMessageMeso.setVisibility(View.GONE);
            secDetail = (LinearLayout) findViewById(R.id.secDetail);
            seclblH = (LinearLayout) findViewById(R.id.seclblH);
            seclblH.setVisibility(View.GONE);
            txtHealthID = (TextView) findViewById(R.id.txtHealthID);
            txtHealthID.setText(g.getHealthID());
            txtVisitNo = (TextView) findViewById(R.id.txtVisitNo);
            txtVisitNo.setText(VisitNumber(g.getGeneratedId()));
            txtSS = (TextView) findViewById(R.id.txtSS);
            txtSS1 = (TextView) findViewById(R.id.txtSS1);
            txtPHHNo = (TextView) findViewById(R.id.txtPHHNo);
            txtPHHNo.setText(g.getHouseholdNo());
            secHHNo = (LinearLayout) findViewById(R.id.seclblH);
            secHHNo1 = (LinearLayout) findViewById(R.id.seclblH1);
            chkHHNoDontKnow = (CheckBox) findViewById(R.id.chkHHNoDontKnow);
            tvMAge = (TextView) findViewById(R.id.tvMAge);
            tvMAge.setVisibility(View.GONE);
            tvMAge1 = (TextView) findViewById(R.id.tvMAge1);
            tvMAge1.setVisibility(View.GONE);
            txtHHNo = (EditText) findViewById(R.id.txtHHNo);
            chkHHNoDontKnow.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (((CheckBox) v).isChecked()) {
                        txtHHNo.setText("");
                        txtHHNo.setEnabled(false);
                    } else {
                        txtHHNo.setEnabled(true);
                    }
                }
            });
            VlblSNo = (TextView) findViewById(R.id.VlblSNo);
            txtSNo = (TextView) findViewById(R.id.txtSNo);

            VlblDOB = (TextView) findViewById(R.id.VlblDOB);
            DOB = (TextView) findViewById(R.id.DOB);

            VlblHus = (TextView) findViewById(R.id.VlblHus);

            secName = (LinearLayout) findViewById(R.id.secName);
            VlblName = (TextView) findViewById(R.id.VlblName);
            txtName = (TextView) findViewById(R.id.txtName);

            secAge = (LinearLayout) findViewById(R.id.secAge);
            VlblAge = (TextView) findViewById(R.id.VlblAge);
            txtAge = (TextView) findViewById(R.id.txtAge);

            secHusName = (LinearLayout) findViewById(R.id.secHusName);
            secHusName1 = (LinearLayout) findViewById(R.id.secHusName1);
            secDOMSource1 = (LinearLayout) findViewById(R.id.secDOMSource1);

            secELCONo = (LinearLayout) findViewById(R.id.secELCONo);
            txtHusName = (EditText) findViewById(R.id.txtHusName);
            txtHusAge = (EditText) findViewById(R.id.txtHusAge);

            secMEdu = (LinearLayout) findViewById(R.id.secMEdu);
            VlblMEdu = (TextView) findViewById(R.id.VlblMEdu);
            txtMEdu = (TextView) findViewById(R.id.txtMEdu);

            secMNID = (LinearLayout) findViewById(R.id.secMNID);
            VlblMNID = (TextView) findViewById(R.id.VlblMNID);
            txtMNID = (TextView) findViewById(R.id.txtMNID);

            secMBRID = (LinearLayout) findViewById(R.id.secMBRID);
            VlblMBRID = (TextView) findViewById(R.id.VlblMBRID);
            txtMBRID = (TextView) findViewById(R.id.txtMBRID);

            secMMNo = (LinearLayout) findViewById(R.id.secMMNo);
            VlblMMNo = (TextView) findViewById(R.id.VlblMMNo);
            txtMMNo = (TextView) findViewById(R.id.txtMMNo);

            txtELCONo = (EditText) findViewById(R.id.txtELCONo);
            txtELCONo.requestFocus();
            btnPlus = (ImageButton) findViewById(R.id.btnPlus);
            btnMinus = (ImageButton) findViewById(R.id.btnMinus);
            btnMinus.setVisibility(View.INVISIBLE);
            secTT1a = (LinearLayout) findViewById(R.id.secTT1a);
            secTT2b = (LinearLayout) findViewById(R.id.secTT2b);
            secTT3c = (LinearLayout) findViewById(R.id.secTT3c);
            secTT4d = (LinearLayout) findViewById(R.id.secTT4d);
            secTT5e = (LinearLayout) findViewById(R.id.secTT5e);
            secTT1a.setVisibility(View.GONE);
            secTT2b.setVisibility(View.GONE);
            secTT3c.setVisibility(View.GONE);
            secTT4d.setVisibility(View.GONE);
            secTT5e.setVisibility(View.GONE);
            secDetail.setVisibility(View.GONE);
            secMEdu.setVisibility(View.GONE);
            secMNID.setVisibility(View.GONE);
            secMBRID.setVisibility(View.GONE);
            secMMNo.setVisibility(View.GONE);


            //---------------------FPI--------------------------------------


            rdogrpHusName = (RadioGroup) findViewById(R.id.rdogrpHusName);
            rdoHusName1 = (RadioButton) findViewById(R.id.rdoHusName1);
            rdoHusName2 = (RadioButton) findViewById(R.id.rdoHusName2);

            rdogrpHusAge = (RadioGroup) findViewById(R.id.rdogrpHusAge);
            rdoHusAge1 = (RadioButton) findViewById(R.id.rdoHusAge1);
            rdoHusAge2 = (RadioButton) findViewById(R.id.rdoHusAge2);

            rdogrpELCONo = (RadioGroup) findViewById(R.id.rdogrpELCONo);
            rdoELCONo1 = (RadioButton) findViewById(R.id.rdoELCONo1);
            rdoELCONo2 = (RadioButton) findViewById(R.id.rdoELCONo2);

            rdogrpHHNo = (RadioGroup) findViewById(R.id.rdogrpHHNo);
            rdoHHNo1 = (RadioButton) findViewById(R.id.rdoHHNo1);
            rdoHHNo2 = (RadioButton) findViewById(R.id.rdoHHNo2);

            rdogrpDOMSou = (RadioGroup) findViewById(R.id.rdogrpDOMSou);
            rdoDOMSou1 = (RadioButton) findViewById(R.id.rdoDOMSou1);
            rdoDOMSou2 = (RadioButton) findViewById(R.id.rdoDOMSou2);

            rdogrpHHNoNoDontKnow = (RadioGroup) findViewById(R.id.rdogrpHHNoNoDontKnow);
            rdoHHNoNoDontKnow1 = (RadioButton) findViewById(R.id.rdoHHNoNoDontKnow1);
            rdoHHNoNoDontKnow2 = (RadioButton) findViewById(R.id.rdoHHNoNoDontKnow2);

            rdogrpMAge = (RadioGroup) findViewById(R.id.rdogrpMAge);
            rdoMAge1 = (RadioButton) findViewById(R.id.rdoMAge1);
            rdoMAge2 = (RadioButton) findViewById(R.id.rdoMAge2);

            rdogrpLiveSon = (RadioGroup) findViewById(R.id.rdogrpLiveSon);
            rdoLiveSon1 = (RadioButton) findViewById(R.id.rdoLiveSon1);
            rdoLiveSon2 = (RadioButton) findViewById(R.id.rdoLiveSon2);


            rdogrpLiveDau = (RadioGroup) findViewById(R.id.rdogrpLiveDau);
            rdoLiveDau1 = (RadioButton) findViewById(R.id.rdoLiveDau1);
            rdoLiveDau2 = (RadioButton) findViewById(R.id.rdoLiveDau2);

            rdogrpTT1 = (RadioGroup) findViewById(R.id.rdogrpTT1);
            rdoTT11 = (RadioButton) findViewById(R.id.rdoTT11);
            rdoTT12 = (RadioButton) findViewById(R.id.rdoTT12);

            rdogrpTT2 = (RadioGroup) findViewById(R.id.rdogrpTT2);
            rdoTT21 = (RadioButton) findViewById(R.id.rdoTT21);
            rdoTT22 = (RadioButton) findViewById(R.id.rdoTT22);

            rdogrpTT3 = (RadioGroup) findViewById(R.id.rdogrpTT3);
            rdoTT31 = (RadioButton) findViewById(R.id.rdoTT31);
            rdoTT32 = (RadioButton) findViewById(R.id.rdoTT32);

            rdogrpTT4 = (RadioGroup) findViewById(R.id.rdogrpTT4);
            rdoTT41 = (RadioButton) findViewById(R.id.rdoTT41);
            rdoTT42 = (RadioButton) findViewById(R.id.rdoTT42);

            rdogrpTT5 = (RadioGroup) findViewById(R.id.rdogrpTT5);
            rdoTT51 = (RadioButton) findViewById(R.id.rdoTT51);
            rdoTT52 = (RadioButton) findViewById(R.id.rdoTT52);

            rdogrpElcoVisit = (RadioGroup) findViewById(R.id.rdogrpElcoVisit);
            rdoElcoVisit1 = (RadioButton) findViewById(R.id.rdoElcoVisit1);
            rdoElcoVisit2 = (RadioButton) findViewById(R.id.rdoElcoVisit2);

            rdogrpVSPA = (RadioGroup) findViewById(R.id.rdogrpVSPA);
            rdoVSPA1 = (RadioButton) findViewById(R.id.rdoVSPA1);
            rdoVSPA2 = (RadioButton) findViewById(R.id.rdoVSPA2);

            rdogrpMethod = (RadioGroup) findViewById(R.id.rdogrpMethod);
            rdoMethod1 = (RadioButton) findViewById(R.id.rdoMethod1);
            rdoMethod2 = (RadioButton) findViewById(R.id.rdoMethod2);

            rdogrpSorce = (RadioGroup) findViewById(R.id.rdogrpSorce);
            rdoSorce1 = (RadioButton) findViewById(R.id.rdoSorce1);
            rdoSorce2 = (RadioButton) findViewById(R.id.rdoSorce2);

            rdogrpBrand = (RadioGroup) findViewById(R.id.rdogrpBrand);
            rdoBrand1 = (RadioButton) findViewById(R.id.rdoBrand1);
            rdoBrand2 = (RadioButton) findViewById(R.id.rdoBrand2);

            rdogrpMethodQty = (RadioGroup) findViewById(R.id.rdogrpMethodQty);
            rdoMethodQty1 = (RadioButton) findViewById(R.id.rdoMethodQty1);
            rdoMethodQty2 = (RadioButton) findViewById(R.id.rdoMethodQty2);

            rdogrpSyringeQty = (RadioGroup) findViewById(R.id.rdogrpSyringeQty);
            rdoSyringeQty1 = (RadioButton) findViewById(R.id.rdoSyringeQty1);
            rdoSyringeQty2 = (RadioButton) findViewById(R.id.rdoSyringeQty2);

            rdogrpDueDoze = (RadioGroup) findViewById(R.id.rdogrpDueDoze);
            rdoDueDoze1 = (RadioButton) findViewById(R.id.rdoDueDoze1);
            rdoDueDoze2 = (RadioButton) findViewById(R.id.rdoDueDoze2);

            rdogrpMethodReceve = (RadioGroup) findViewById(R.id.rdogrpMethodReceve);
            rdoMethodReceve1 = (RadioButton) findViewById(R.id.rdoMethodReceve1);
            rdoMethodReceve2 = (RadioButton) findViewById(R.id.rdoMethodReceve2);

            rdogrpMDAge = (RadioGroup) findViewById(R.id.rdogrpMDAge);
            rdoMDAge1 = (RadioButton) findViewById(R.id.rdoMDAge1);
            rdoMDAge2 = (RadioButton) findViewById(R.id.rdoMDAge2);

            rdogrpWherePlace = (RadioGroup) findViewById(R.id.rdogrpWherePlace);
            rdoWherePlace1 = (RadioButton) findViewById(R.id.rdoWherePlace1);
            rdoWherePlace2 = (RadioButton) findViewById(R.id.rdoWherePlace2);

            rdogrpValidity = (RadioGroup) findViewById(R.id.rdogrpValidity);
            rdoValidity1 = (RadioButton) findViewById(R.id.rdoValidity1);
            rdoValidity2 = (RadioButton) findViewById(R.id.rdoValidity2);

            ///
            // txtMDAge.setVisibility(View.GONE);
            // VlblMDAge.setVisibility(View.GONE);
            // dtpMD.setVisibility(View.GONE);
            // btnMD.setVisibility(View.GONE);
            btnPlus.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    secDetail.setVisibility(View.VISIBLE);
                    btnPlus.setVisibility(View.GONE);
                    btnMinus.setVisibility(View.VISIBLE);

                    secMEdu.setVisibility(View.VISIBLE);
                    secMNID.setVisibility(View.VISIBLE);
                    secMBRID.setVisibility(View.VISIBLE);
                    secMMNo.setVisibility(View.VISIBLE);
                }
            });

            btnMinus.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    secDetail.setVisibility(View.GONE);
                    btnPlus.setVisibility(View.VISIBLE);
                    btnMinus.setVisibility(View.GONE);

                    secMEdu.setVisibility(View.GONE);
                    secMNID.setVisibility(View.GONE);
                    secMBRID.setVisibility(View.GONE);
                    secMMNo.setVisibility(View.GONE);
                }
            });

            secTTCardStatus = (LinearLayout) findViewById(R.id.secTTCardStatus);
            VlblTTCardStatus = (TextView) findViewById(R.id.VlblTTCardStatus);

            secTT1 = (LinearLayout) findViewById(R.id.secTT1);

            VlblTT1 = (TextView) findViewById(R.id.VlblTT1);
            ChkTT1 = (CheckBox) findViewById(R.id.ChkTT1);
            VlblTTCard = (TextView) findViewById(R.id.VlblTTCard);
            rdogrpTTCard = (RadioGroup) findViewById(R.id.rdogrpTTCard);
            rdoCardYes = (RadioButton) findViewById(R.id.rdoCardYes);
            rdoCardNo = (RadioButton) findViewById(R.id.rdoCardNo);
            rdogrpTTCard.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int radioButtonID) {
                    if (rdoCardYes.isChecked()) {
                        btnDOTT1.setEnabled(true);
                        dtpDOTT1.setVisibility(View.VISIBLE);
                        btnDOTT1.setVisibility(View.VISIBLE);
                    } else if (rdoCardNo.isChecked()) {
                        dtpDOTT1.setText("");
                        btnDOTT1.setEnabled(false);
                        dtpDOTT1.setVisibility(View.GONE);
                        btnDOTT1.setVisibility(View.GONE);
                    }
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });
            dtpDOTT1 = (EditText) findViewById(R.id.dtpDOTT1);
            btnDOTT1 = (ImageButton) findViewById(R.id.btnDOTT1);
            dtpDOTT1.setText("");
            btnDOTT1.setEnabled(false);
            ChkTT1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (((CheckBox) v).isChecked()) {
                        btnDOTT1.setEnabled(true);
                        VlblTTCard.setVisibility(View.VISIBLE);
                        rdogrpTTCard.setVisibility(View.VISIBLE);
                        btnSaveTT.setVisibility(View.VISIBLE);
                    } else {
                        rdogrpTTCard.clearCheck();
                        dtpDOTT1.setText("");
                        btnDOTT1.setEnabled(false);
                        VlblTTCard.setVisibility(View.GONE);
                        rdogrpTTCard.setVisibility(View.GONE);
                        dtpDOTT1.setVisibility(View.GONE);
                        btnDOTT1.setVisibility(View.GONE);
                        btnSaveTT.setVisibility(View.GONE);
                    }
                }
            });

            dtpDOM = (EditText) findViewById(R.id.dtpDOM);
            btnDOM = (ImageButton) findViewById(R.id.btnDOM);
            secDOMSource = (LinearLayout) findViewById(R.id.secDOMSource);
            VlblDOMSource = (TextView) findViewById(R.id.VlblDOMSource);
            rdogrpDOMSource = (RadioGroup) findViewById(R.id.rdogrpDOMSource);
            rdoDOMSource1 = (RadioButton) findViewById(R.id.rdoDOMSource1);
            rdoDOMSource1.setChecked(true);
            rdoDOMSource2 = (RadioButton) findViewById(R.id.rdoDOMSource2);
            //secMAge.setVisibility( View.GONE );
            txtMAge = (EditText) findViewById(R.id.txtMAge);

            dtpDOM.setVisibility(View.VISIBLE);
            btnDOM.setVisibility(View.VISIBLE);
            txtMAge.setVisibility(View.GONE);
            VlblMAge = (TextView) findViewById(R.id.VlblMAge);
            VlblMAge.setVisibility(View.GONE);
            rdogrpDOMSource.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup arg0, int id) {
                    if (id == R.id.rdoDOMSource1) {
                        tvMAge.setVisibility(View.VISIBLE);
                        tvMAge1.setVisibility(View.GONE);
                        dtpDOM.setVisibility(View.VISIBLE);
                        btnDOM.setVisibility(View.VISIBLE);
                        txtMAge.setVisibility(View.GONE);
                        txtMAge.setText("");
                        VlblMAge.setVisibility(View.GONE);
                        btnDOM.requestFocus();
                    } else if (id == R.id.rdoDOMSource2) {
                        dtpDOM.setText("");
                        dtpDOM.setVisibility(View.GONE);
                        btnDOM.setVisibility(View.GONE);
                        tvMAge.setVisibility(View.GONE);
                        tvMAge1.setVisibility(View.VISIBLE);
                        txtMAge.setVisibility(View.VISIBLE);
                        VlblMAge.setVisibility(View.VISIBLE);
                        txtMAge.requestFocus();
                    }
                }
            });
            secMethod1 = (LinearLayout) findViewById(R.id.secMethod1);
            secVSPA = (LinearLayout) findViewById(R.id.secVSPA);
            secLiveSon = (LinearLayout) findViewById(R.id.secLiveSon);
            secLiveDau = (LinearLayout) findViewById(R.id.secLiveDau);
            secElcoVisit = (LinearLayout) findViewById(R.id.secElcoVisit);

            VlblLiveSon = (TextView) findViewById(R.id.VlblLiveSon);
            txtLiveSon = (EditText) findViewById(R.id.txtLiveSon);

            VlblLiveDau = (TextView) findViewById(R.id.VlblLiveDau);
            txtLiveDau = (EditText) findViewById(R.id.txtLiveDau);

            dtpDOV = (EditText) findViewById(R.id.dtpDOV);
            dtpDOV.setText(Global.DateNowDMY());
            btnDOV = (ImageButton) findViewById(R.id.btnDOV);
            btnDOV.setEnabled(false);


            secMethodReceveDT = (LinearLayout) findViewById(R.id.secMethodReceveDT);
            secMethodReceveDT1 = (LinearLayout) findViewById(R.id.secMethodReceveDT1);
            VlblMethodReceveDT = (TextView) findViewById(R.id.VlblMethodReceveDT);
            rdogrpMethodReceveDT = (RadioGroup) findViewById(R.id.rdogrpMethodReceveDT);
            rdoMethodReceveDT1 = (RadioButton) findViewById(R.id.rdoMethodReceveDT1);
            rdoMethodReceveDT1.setChecked(true);
            rdoMethodReceveDT2 = (RadioButton) findViewById(R.id.rdoMethodReceveDT2);
            txtMDAge = (EditText) findViewById(R.id.txtMDAge);
            VlblMDAge = (TextView) findViewById(R.id.VlblMDAge);

            dtpMD = (EditText) findViewById(R.id.dtpMD);
            dtpMD.setText(Global.DateNowDMY());
            btnMD = (ImageButton) findViewById(R.id.btnMD);
            //txtMDAge.setVisibility(View.GONE);
            // VlblMDAge.setVisibility(View.GONE);
            // dtpMD.setVisibility(View.GONE);
            //  btnMD.setVisibility(View.GONE);
            if (rdoMethodReceveDT1.isChecked() == true) {
                txtMDAge.setVisibility(View.GONE);
                VlblMDAge.setVisibility(View.GONE);
            }
            if (rdoMethodReceveDT2.isChecked() == true) {
                dtpMD.setVisibility(View.GONE);
                btnMD.setVisibility(View.GONE);
            }

            rdogrpMethodReceveDT.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup arg0, int id) {
                    if (id == R.id.rdoMethodReceveDT1) {
                        dtpMD.setVisibility(View.VISIBLE);
                        btnMD.setVisibility(View.VISIBLE);
                        txtMDAge.setVisibility(View.GONE);
                        txtMDAge.setText("");
                        VlblMDAge.setVisibility(View.GONE);
                        btnMD.requestFocus();
                    } else if (id == R.id.rdoMethodReceveDT2) {
                        dtpMD.setText("");
                        dtpMD.setVisibility(View.GONE);
                        btnMD.setVisibility(View.GONE);
                        txtMDAge.setVisibility(View.VISIBLE);
                        VlblMDAge.setVisibility(View.VISIBLE);
                        txtMDAge.requestFocus();
                    }
                }
            });

            rdogrpVS = (RadioGroup) findViewById(R.id.rdogrpVS);
            rdoVSPresent = (RadioButton) findViewById(R.id.rdoVSPresent);
            rdoVSAbsent = (RadioButton) findViewById(R.id.rdoVSAbsent);

            secMethod = (LinearLayout) findViewById(R.id.secMethod);
            VlblMethod = (TextView) findViewById(R.id.VlblMethod);
            spnMethod = (Spinner) findViewById(R.id.spnMethod);
            spnMethod.setEnabled(false);

            secSS = (LinearLayout) findViewById(R.id.secSS);
            rdogrpSS = (RadioGroup) findViewById(R.id.rdogrpSS);
            rdoSSOtherGovt = (RadioButton) findViewById(R.id.rdoSSOtherGovt);
            rdoSSFWA = (RadioButton) findViewById(R.id.rdoSSFWA);
            rdoSSLMarket = (RadioButton) findViewById(R.id.rdoSSLMarket);
            rdoSSOther = (RadioButton) findViewById(R.id.rdoSSOther);
            cmdSave = (Button) findViewById(R.id.cmdSave);
            Sex = (TextView) findViewById(R.id.Sex);
            Sex.setVisibility(View.GONE);
            ELCOProfile e = new ELCOProfile();
            e.PregnancyInfo(conxt, g.getGeneratedId(), LastPGNNo());
            LMP = String.valueOf(e.getLMP());
            Integer DiffLMP_VD = Global.DateDifferenceDays(dtpDOV.getText().toString(), LMP);

            secBrand = (LinearLayout) findViewById(R.id.secBrand);
            secBrand.setVisibility(View.GONE);
            spnBrand = (Spinner) findViewById(R.id.spnBrand);

            spnBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    //Implanon
                    if (Global.Left(spnBrand.getSelectedItem().toString(), 2).equalsIgnoreCase("04")) {
                        txtValidity.setText("3");
                        txtValidity.setEnabled(false);
                        spnValidityUnit.setEnabled(false);
                    }
                    //Zdel
                    else if (Global.Left(spnBrand.getSelectedItem().toString(), 2).equalsIgnoreCase("05")) {
                        txtValidity.setText("5");
                        txtValidity.setEnabled(false);
                        spnValidityUnit.setEnabled(false);
                    } else {
                        txtValidity.setText("");
                        txtValidity.setEnabled(true);
                        spnValidityUnit.setEnabled(true);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });

            rdogrpSS.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup arg0, int id) {

                    Integer DiffLMP_VD = Global.DateDifferenceDays(dtpDOV.getText().toString(), LMP);

                    if (id == R.id.rdoSSFWA & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("01")) {
                        txtSS.setVisibility(View.VISIBLE);
                        txtSS1.setVisibility(View.GONE);
                        secMethodUnit.setVisibility(View.VISIBLE);
                        txtMethodQty.setEnabled(false);
                        spnMethodUnit.setSelection(1);
                        spnMethodUnit.setEnabled(false);
                        secBrand.setVisibility(View.VISIBLE);
                        spnBrand.setAdapter(null);
                        spnBrand.setAdapter(C.getArrayAdapter("Select '  'as brandCode union Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('01','02') order by brandCode asc"));
                        rdoSSFWA.setVisibility(View.VISIBLE);
                        spnBrand.setSelection(Global.SpinnerItemPosition(spnBrand, 2, C.ReturnSingleValue("Select  ifnull(brand,'') as brand from ElcoVisit where healthid='" + g.getGeneratedId() + "' and visit='" + txtVisitNo.getText() + "'")));

                    } else if (id == R.id.rdoSSLMarket & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("01")) {
                        secMethodUnit.setVisibility(View.GONE);
                        spnMethodUnit.setSelection(0);
                        secBrand.setVisibility(View.VISIBLE);
                        spnBrand.setAdapter(null);
                        spnBrand.setAdapter(C.getArrayAdapter("Select '  'as brandCode union Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('77') order by brandCode asc"));
                        //spnBrand.setEnabled(false);
                    } else if (id == R.id.rdoSSOther & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("01")) {
                        secMethodUnit.setVisibility(View.GONE);
                        spnMethodUnit.setSelection(0);
                        secBrand.setVisibility(View.VISIBLE);
                        spnBrand.setAdapter(null);
                        spnBrand.setAdapter(C.getArrayAdapter("Select '  'as brandCode union Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('01','02','77') order by brandCode asc"));
                        spnBrand.setEnabled(true);
                    } else if (id == R.id.rdoSSOtherGovt & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("01")) {
                        secMethodUnit.setVisibility(View.GONE);
                        spnMethodUnit.setSelection(0);
                        secBrand.setVisibility(View.VISIBLE);
                        spnBrand.setAdapter(null);
                        spnBrand.setAdapter(C.getArrayAdapter("Select '  'as brandCode union Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('01','02') order by brandCode asc"));
                        spnBrand.setEnabled(true);
                    } else if (id == R.id.rdoSSFWA & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("02")) {
                        txtSS.setVisibility(View.VISIBLE);
                        txtSS1.setVisibility(View.GONE);
                        secMethodUnit.setVisibility(View.VISIBLE);
                        txtMethodQty.setEnabled(false);
                        spnMethodUnit.setSelection(2);
                        rdoSSFWA.setVisibility(View.VISIBLE);
                        secBrand.setVisibility(View.VISIBLE);
                        spnBrand.setAdapter(null);
                        spnBrand.setAdapter(C.getArrayAdapter("Select '  'as brandCode union Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('03') order by brandCode asc"));
                    } else if (id == R.id.rdoSSLMarket & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("02")) {
                        secMethodUnit.setVisibility(View.GONE);
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                        secBrand.setVisibility(View.VISIBLE);
                        spnBrand.setAdapter(null);
                        spnBrand.setAdapter(C.getArrayAdapter("Select '  'as brandCode union Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('77') order by brandCode asc"));
                    } else if (id == R.id.rdoSSOther & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("02")) {
                        secMethodUnit.setVisibility(View.GONE);
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                        secBrand.setVisibility(View.VISIBLE);
                        spnBrand.setAdapter(null);
                        spnBrand.setAdapter(C.getArrayAdapter("Select '  'as brandCode union Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('03','77') order by brandCode asc"));
                    } else if (id == R.id.rdoSSOtherGovt & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("02")) {
                        secMethodUnit.setVisibility(View.GONE);
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                        secBrand.setVisibility(View.VISIBLE);
                        spnBrand.setAdapter(null);
                        spnBrand.setAdapter(C.getArrayAdapter("Select '  'as brandCode union Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('03') order by brandCode asc"));
                    } else if (id == R.id.rdoSSFWA & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("03")) {
                        txtSS.setVisibility(View.VISIBLE);
                        txtSS1.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        secMethodReceveDT1.setVisibility(View.VISIBLE);
                        secSyringe.setVisibility(View.VISIBLE);
                        secSyringe1.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.VISIBLE);
                        txtMethodQty.setEnabled(false);
                        spnMethodUnit.setSelection(3);
                        txtMethodQty.setText("1");
                        txtSyringeQty.setText("1");
                        rdoSSFWA.setVisibility(View.VISIBLE);
                    } else if (id == R.id.rdoSSLMarket & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("03")) {
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        secMethodReceveDT1.setVisibility(View.VISIBLE);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        secMethodUnit.setVisibility(View.GONE);
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                    } else if (id == R.id.rdoSSOther & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("03")) {
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        secMethodReceveDT1.setVisibility(View.VISIBLE);
                        secSyringe.setVisibility(View.GONE);
                        secMethodUnit.setVisibility(View.GONE);
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                    } else if (id == R.id.rdoSSOtherGovt & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("03")) {
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        secMethodReceveDT1.setVisibility(View.VISIBLE);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        secMethodUnit.setVisibility(View.GONE);
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                    } else if (id == R.id.rdoSSFWA & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("04")) {
                        txtSS.setVisibility(View.VISIBLE);
                        txtSS1.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        secMethodReceveDT1.setVisibility(View.VISIBLE);
                        secValidity.setVisibility(View.VISIBLE);
                        spnValidityUnit.setSelection(3);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);
                    } else if (id == R.id.rdoSSLMarket & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("04")) {
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        secMethodReceveDT1.setVisibility(View.VISIBLE);
                        secValidity.setVisibility(View.VISIBLE);
                        spnValidityUnit.setSelection(3);
                    } else if (id == R.id.rdoSSOther & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("04")) {
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        secMethodReceveDT1.setVisibility(View.VISIBLE);
                        secValidity.setVisibility(View.VISIBLE);
                        spnValidityUnit.setSelection(3);
                    } else if (id == R.id.rdoSSOtherGovt & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("04")) {
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        secMethodReceveDT1.setVisibility(View.VISIBLE);
                        secValidity.setVisibility(View.VISIBLE);
                        spnValidityUnit.setSelection(3);
                    } else if (id == R.id.rdoSSFWA & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("05")) {
                        txtSS.setVisibility(View.VISIBLE);
                        txtSS1.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        secMethodReceveDT1.setVisibility(View.VISIBLE);
                        secValidity.setVisibility(View.VISIBLE);
                        spnValidityUnit.setSelection(3);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);
                        //secBrand.setVisibility(View.VISIBLE);
                        //spnBrand.setAdapter(C.getArrayAdapter("Select '  'as brandCode union Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('04','05') order by brandCode asc"));
                    } else if (id == R.id.rdoSSLMarket & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("05")) {
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        secMethodReceveDT1.setVisibility(View.VISIBLE);
                        secValidity.setVisibility(View.VISIBLE);
                        spnValidityUnit.setSelection(3);
                        secBrand.setVisibility(View.VISIBLE);
                        spnBrand.setAdapter(null);
                        spnBrand.setAdapter(C.getArrayAdapter("Select '  'as brandCode union Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('77') order by brandCode asc"));
                    } else if (id == R.id.rdoSSOther & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("05")) {
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        secMethodReceveDT1.setVisibility(View.VISIBLE);
                        secValidity.setVisibility(View.VISIBLE);
                        spnValidityUnit.setSelection(3);
                        secBrand.setVisibility(View.VISIBLE);
                        spnBrand.setAdapter(null);
                        spnBrand.setAdapter(C.getArrayAdapter("Select '  'as brandCode union Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('04','05','77') order by brandCode asc"));
                    } else if (id == R.id.rdoSSOtherGovt & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("05")) {
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        secMethodReceveDT1.setVisibility(View.VISIBLE);
                        secValidity.setVisibility(View.VISIBLE);
                        spnValidityUnit.setSelection(3);
                        secBrand.setVisibility(View.VISIBLE);
                        spnBrand.setAdapter(null);
                        spnBrand.setAdapter(C.getArrayAdapter("Select '  'as brandCode union Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('04','05') order by brandCode asc"));

                    } else if (id == R.id.rdoSSFWA & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("06")) {
                        txtSS.setVisibility(View.GONE);
                        txtSS1.setVisibility(View.VISIBLE);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);

                    } else if (id == R.id.rdoSSFWA & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("07")) {
                        txtSS.setVisibility(View.GONE);
                        txtSS1.setVisibility(View.VISIBLE);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);

                    } else if (id == R.id.rdoSSFWA & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("08")) {
                        txtSS.setVisibility(View.VISIBLE);
                        txtSS1.setVisibility(View.GONE);
                        txtSS.setVisibility(View.VISIBLE);
                        txtSS1.setVisibility(View.GONE);
                        secMethodUnit.setVisibility(View.VISIBLE);
                        txtMethodQty.setEnabled(false);
                        spnMethodUnit.setSelection(3);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);
                    } else if (id == R.id.rdoSSLMarket & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("08")) {
                        txtSS.setVisibility(View.VISIBLE);
                        txtSS1.setVisibility(View.GONE);
                        secMethodUnit.setVisibility(View.GONE);
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);
                    } else if (id == R.id.rdoSSOther & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("08")) {
                        secMethodUnit.setVisibility(View.GONE);
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);
                        txtSS.setVisibility(View.VISIBLE);
                        txtSS1.setVisibility(View.GONE);
                    } else if (id == R.id.rdoSSOtherGovt & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("08")) {
                        secMethodUnit.setVisibility(View.GONE);
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                    } else if (id == R.id.rdoSSFWA & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("09")) {

                        if (DiffLMP_VD >= 224) {
                            cmdSave.setEnabled(true);
                            secMethodUnit.setVisibility(View.VISIBLE);
                            txtMethodQty.setEnabled(false);
                            spnMethodUnit.setSelection(3);
                            secBrand.setVisibility(View.GONE);
                            spnBrand.setSelection(0);
                            VlblMessage.setVisibility(View.GONE);
                            VlblMessageMeso.setVisibility(View.VISIBLE);
                        } else {
                            VlblMessage.setVisibility(View.GONE);
                            VlblMessageMeso.setVisibility(View.GONE);

                            cmdSave.setEnabled(true);
                            //Connection.MessageBox(ELCOFormFPI.this, "গর্ভকালীন সেবার সময়ে ২২৩ দিনের পর থেকে মিসোপ্রোস্টোল বড়ি দিতে পারবে । ");
                            return;
                        }


                    } else if (id == R.id.rdoSSLMarket & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("09")) {
                        if (DiffLMP_VD >= 224) {
                            cmdSave.setEnabled(true);
                            secMethodUnit.setVisibility(View.GONE);
                            txtMethodQty.setText("");
                            spnMethodUnit.setSelection(0);
                            secBrand.setVisibility(View.GONE);
                            spnBrand.setSelection(0);
                            VlblMessage.setVisibility(View.GONE);
                            VlblMessageMeso.setVisibility(View.VISIBLE);
                        } else {
                            VlblMessage.setVisibility(View.GONE);
                            VlblMessageMeso.setVisibility(View.GONE);
                            cmdSave.setEnabled(true);
                            // Connection.MessageBox(ELCOFormFPI.this, "গর্ভকালীন সেবার সময়ে ২২৩ দিনের পর থেকে মিসোপ্রোস্টোল বড়ি দিতে পারবে । ");
                            return;
                        }
                    } else if (id == R.id.rdoSSOther & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("09")) {
                        if (DiffLMP_VD >= 224) {
                            cmdSave.setEnabled(true);
                            secMethodUnit.setVisibility(View.GONE);
                            txtMethodQty.setText("");
                            spnMethodUnit.setSelection(0);
                            secBrand.setVisibility(View.GONE);
                            spnBrand.setSelection(0);
                            VlblMessage.setVisibility(View.GONE);
                            VlblMessageMeso.setVisibility(View.VISIBLE);
                        } else {
                            VlblMessage.setVisibility(View.GONE);
                            VlblMessageMeso.setVisibility(View.GONE);
                            cmdSave.setEnabled(true);
                            // Connection.MessageBox(ELCOFormFPI.this, "গর্ভকালীন সেবার সময়ে ২২৩ দিনের পর থেকে মিসোপ্রোস্টোল বড়ি দিতে পারবে । ");
                            return;
                        }
                    } else if (id == R.id.rdoSSOtherGovt & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("09")) {
                        if (DiffLMP_VD >= 224) {
                            cmdSave.setEnabled(true);
                            secMethodUnit.setVisibility(View.GONE);
                            txtMethodQty.setText("");
                            spnMethodUnit.setSelection(0);
                            secBrand.setVisibility(View.GONE);
                            spnBrand.setSelection(0);
                            VlblMessage.setVisibility(View.GONE);
                            VlblMessageMeso.setVisibility(View.VISIBLE);
                        } else {
                            VlblMessage.setVisibility(View.GONE);
                            VlblMessageMeso.setVisibility(View.GONE);
                            cmdSave.setEnabled(true);
                            // Connection.MessageBox(ELCOFormFPI.this, "গর্ভকালীন সেবার সময়ে ২২৩ দিনের পর থেকে মিসোপ্রোস্টোল বড়ি দিতে পারবে । ");
                            return;
                        }
                    } else if (id == R.id.rdoSSLMarket | id == R.id.rdoSSOther | id == R.id.rdoSSOtherGovt) {
                        txtMethodQty.setText("");
                        txtMethodQty.setEnabled(false);
                        secMethodUnit.setVisibility(View.GONE);
                    }
                }
            });


            secMethodUnit = (LinearLayout) findViewById(R.id.secMethodUnit);
            spnMethodUnit = (Spinner) findViewById(R.id.spnMethodUnit);
            txtMethodQty = (EditText) findViewById(R.id.txtMethodQty);
            secSyringe = (LinearLayout) findViewById(R.id.secSyringe);
            secSyringe1 = (LinearLayout) findViewById(R.id.secSyringe1);

            txtSyringeQty = (EditText) findViewById(R.id.txtSyringeQty);
            dtDueDoze = (TextView) findViewById(R.id.dtDueDoze);
            String lastDozeDate = C.ReturnSingleValue("select  doseDate from womanInjectable where healthId='" + g.getGeneratedId() + "' and doseId=(select  max(doseId) from womanInjectable)");
            if (lastDozeDate != "") {
                ((TextView) findViewById(R.id.dtDueDoze)).setText(Global.addDays(Global.DateConvertDMY(lastDozeDate), 90));
            }
            secWherePlace = (LinearLayout) findViewById(R.id.secWherePlace);
            VlblWherePlace = (TextView) findViewById(R.id.VlblWherePlace);
            spnWherePlace = (Spinner) findViewById(R.id.spnWherePlace);
            List<String> listFacility = new ArrayList<String>();
            listFacility.add("");
            listFacility.add("01-উপজেলা স্বাস্থ্য কমপেস্নক্স");
            listFacility.add("02-ইউনিয়ন স্বাস্থ্য ও পরিবার কল্যাণ কেন্দ্র");
            listFacility.add("03-মা ও শিশু কল্যাণ কেন্দ্র");
            listFacility.add("04-জেলা সদর  বা অন্যান্য সরকারী হাসপাতাল");
            listFacility.add("05-এনজিও ক্লিনিক বা হাসপাতাল");
            listFacility.add("06-প্রাইভেট ক্লিনিক বা হাসপাতাল");

            ArrayAdapter<String> adptrspnWherePlace = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listFacility);
            spnWherePlace.setAdapter(adptrspnWherePlace);


            secBrand = (LinearLayout) findViewById(R.id.secBrand);
            secBrand.setVisibility(View.GONE);
            spnBrand = (Spinner) findViewById(R.id.spnBrand);


            rdogrpVS.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup arg0, int id) {
                    if (id == R.id.rdoVSPresent) {
                        MethodList("p", Sex.getText().toString());
                        rdogrpSS.clearCheck();
                        secMethod.setVisibility(View.VISIBLE);
                        spnMethod.setSelection(0);
                        txtMethodQty.setText("");
                        spnMethodUnit.setSelection(0);
                        spnWherePlace.setSelection(0);
                        dtpMD.setText("");
                        spnMethod.setEnabled(false);
                    } else if (id == R.id.rdoVSAbsent) {
                        MethodList("a", Sex.getText().toString());
                        rdogrpSS.clearCheck();
                        secMethod.setVisibility(View.VISIBLE);
                        spnMethod.setSelection(0);
                        spnWherePlace.setSelection(0);
                        txtMethodQty.setText("");
                        dtpMD.setText("");
                        spnMethodUnit.setSelection(0);
                        spnMethod.setEnabled(false);
                    } else {
                        //secMethod.setVisibility( View.GONE );
                    }
                }
            });

            if (CurrentStatus(g.getGeneratedId()).equals("12")) {
                spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2) in('09') order by EvCode asc"));
                secMessage.setVisibility(View.VISIBLE);
                VlblMessage.setVisibility(View.GONE);
                //  cmdSave.setEnabled(false);
                cmdSave.setEnabled(true);
                /*if (DiffLMP_VD >= 224) {
                    VlblMessage.setVisibility(View.VISIBLE);
                    VlblMessageMeso.setVisibility(View.VISIBLE);
                    cmdSave.setEnabled(true);
                }
                else {
                    VlblMessageMeso.setVisibility(View.GONE);
                    VlblMessage.setVisibility(View.VISIBLE);
                    cmdSave.setEnabled(false);
                }*/
            } else if (CurrentStatus(g.getGeneratedId()).equals("09")) {
                spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode"));
                secMessage.setVisibility(View.VISIBLE);
                VlblMessage.setVisibility(View.GONE);
                //cmdSave.setEnabled(false);
                cmdSave.setEnabled(true);

            } else {
                spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2) not in('09','13','14') order by EvCode asc"));
                cmdSave.setEnabled(true);
                secMessage.setVisibility(View.GONE);
                VlblMessage.setVisibility(View.GONE);
                VlblMessageMeso.setVisibility(View.GONE);
            }

            /*List<String> listBrand = new ArrayList<String>();
            listBrand.add("");
            listBrand.add("সুখী");
            listBrand.add("আপন");
            listBrand.add("ইমপ্লানন");
            listBrand.add("জেডেল  ");
            listBrand.add("অন্যান্য");
            ArrayAdapter<String> adptrspnBrand= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listBrand);
            spnBrand.setAdapter(adptrspnBrand);*/


            List<String> listMethodUnit = new ArrayList<String>();
            listMethodUnit.add("");
            listMethodUnit.add("চক্র");
            listMethodUnit.add("পিস");
            listMethodUnit.add("ডোজ");
            ArrayAdapter<String> adptrMethodUnit = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listMethodUnit);
            spnMethodUnit.setAdapter(adptrMethodUnit);

            spnMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    if (spnMethod.getSelectedItemPosition() == 0) {
                        secSS.setVisibility(View.GONE);
                        secMethodUnit.setVisibility(View.GONE);
                        spnMethodUnit.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        txtMethodQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        secMethodReceveDT1.setVisibility(View.GONE);
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);
                        txtSS.setVisibility(View.VISIBLE);
                        txtSS1.setVisibility(View.GONE);
                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("01") && rdoSSFWA.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.VISIBLE);
                        spnMethodUnit.setSelection(1);
                        spnMethodUnit.setEnabled(false);
                        secBrand.setVisibility(View.VISIBLE);
                        spnBrand.setAdapter(null);
                        spnBrand.setAdapter(C.getArrayAdapter("Select '  'as brandCode union Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2) in('01','02') order by brandCode asc"));
                        spnBrand.setSelection(Global.SpinnerItemPosition(spnBrand, 2, C.ReturnSingleValue("Select  ifnull(brand,'') as brand from ElcoVisit where healthid='" + g.getGeneratedId() + "' and visit='" + txtVisitNo.getText() + "'")));
                        spnBrand.setEnabled(true);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        secMethodReceveDT1.setVisibility(View.GONE);
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);
                        txtSS.setVisibility(View.VISIBLE);
                        txtSS1.setVisibility(View.GONE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("01") && rdoSSLMarket.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.VISIBLE);
                        spnBrand.setAdapter(C.getArrayAdapter("Select '  'as brandCode union Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('77') order by brandCode asc"));
                        spnBrand.setEnabled(true);
                        spnMethodUnit.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        secMethodReceveDT1.setVisibility(View.GONE);
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);
                        txtSS.setVisibility(View.VISIBLE);
                        txtSS1.setVisibility(View.GONE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("01") && rdoSSOther.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.VISIBLE);
                        spnBrand.setAdapter(C.getArrayAdapter("Select '  'as brandCode union Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('01','02','77') order by brandCode asc"));
                        spnBrand.setEnabled(true);
                        spnMethodUnit.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        secMethodReceveDT1.setVisibility(View.GONE);
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);
                        txtSS.setVisibility(View.VISIBLE);
                        txtSS1.setVisibility(View.GONE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("01") && rdoSSOtherGovt.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.VISIBLE);
                        spnBrand.setAdapter(C.getArrayAdapter("Select '  'as brandCode union Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('01','02') order by brandCode asc"));
                        spnBrand.setEnabled(true);
                        spnMethodUnit.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        secMethodReceveDT1.setVisibility(View.GONE);
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("01")) {
                        secSS.setVisibility(View.VISIBLE);
                        rdogrpSS.clearCheck();
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        txtMethodQty.setText("");
                        spnMethodUnit.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        secMethodReceveDT1.setVisibility(View.GONE);
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);
                        txtSS.setVisibility(View.VISIBLE);
                        txtSS1.setVisibility(View.GONE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("02") && rdoSSFWA.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.VISIBLE);
                        secBrand.setVisibility(View.VISIBLE);
                        spnBrand.setAdapter(C.getArrayAdapter("Select '  'as brandCode union Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('03') order by brandCode asc"));
                        spnMethodUnit.setSelection(2);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        secMethodReceveDT1.setVisibility(View.GONE);
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);
                        txtSS.setVisibility(View.VISIBLE);
                        txtSS1.setVisibility(View.GONE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("02") && rdoSSLMarket.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.VISIBLE);
                        spnBrand.setAdapter(C.getArrayAdapter("Select '  'as brandCode union Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('77') order by brandCode asc"));
                        spnMethodUnit.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        secMethodReceveDT1.setVisibility(View.GONE);
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);
                        txtSS.setVisibility(View.VISIBLE);
                        txtSS1.setVisibility(View.GONE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("02") && rdoSSOther.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.VISIBLE);
                        spnBrand.setAdapter(C.getArrayAdapter("Select '  'as brandCode union Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('03','77') order by brandCode asc"));
                        spnMethodUnit.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        secMethodReceveDT1.setVisibility(View.GONE);
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("02") && rdoSSOtherGovt.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.VISIBLE);
                        spnBrand.setAdapter(C.getArrayAdapter("Select '  'as brandCode union Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('03') order by brandCode asc"));
                        spnMethodUnit.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        secMethodReceveDT1.setVisibility(View.GONE);
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);
                        txtSS.setVisibility(View.VISIBLE);
                        txtSS1.setVisibility(View.GONE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("02")) {
                        secSS.setVisibility(View.VISIBLE);
                        rdogrpSS.clearCheck();
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        txtMethodQty.setText("");
                        spnMethodUnit.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        secMethodReceveDT1.setVisibility(View.GONE);
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);
                        txtSS.setVisibility(View.VISIBLE);
                        txtSS1.setVisibility(View.GONE);
                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("03") && rdoSSFWA.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.VISIBLE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(3);
                        txtMethodQty.setText("1");
                        txtMethodQty.setEnabled(false);
                        secSyringe.setVisibility(View.VISIBLE);
                        secSyringe1.setVisibility(View.VISIBLE);
                        txtSyringeQty.setText("1");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        secMethodReceveDT1.setVisibility(View.VISIBLE);
                        //dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);
                        txtSS.setVisibility(View.VISIBLE);
                        txtSS1.setVisibility(View.GONE);
                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("03") && rdoSSLMarket.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                        txtMethodQty.setEnabled(false);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        secMethodReceveDT1.setVisibility(View.VISIBLE);
                        //dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);
                        txtSS.setVisibility(View.VISIBLE);
                        txtSS1.setVisibility(View.GONE);
                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("03") && rdoSSOther.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                        txtMethodQty.setEnabled(false);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        secMethodReceveDT1.setVisibility(View.VISIBLE);
                        //dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);
                        txtSS.setVisibility(View.VISIBLE);
                        txtSS1.setVisibility(View.GONE);
                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("03") && rdoSSOtherGovt.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                        txtMethodQty.setEnabled(false);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        secMethodReceveDT1.setVisibility(View.VISIBLE);
                        //dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);
                        txtSS.setVisibility(View.VISIBLE);
                        txtSS1.setVisibility(View.GONE);
                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("03")) {
                        secSS.setVisibility(View.VISIBLE);
                        rdogrpSS.clearCheck();
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                        txtMethodQty.setEnabled(false);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        secMethodReceveDT1.setVisibility(View.GONE);
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);
                        txtSS.setVisibility(View.VISIBLE);
                        txtSS1.setVisibility(View.GONE);
                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("04") && rdoSSFWA.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        secMethodReceveDT1.setVisibility(View.VISIBLE);
                        secValidity.setVisibility(View.VISIBLE);
                        spnValidityUnit.setSelection(3);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);
                        txtSS.setVisibility(View.GONE);
                        txtSS1.setVisibility(View.VISIBLE);
                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("04") && rdoSSLMarket.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        secMethodReceveDT1.setVisibility(View.VISIBLE);
                        secValidity.setVisibility(View.VISIBLE);
                        spnValidityUnit.setSelection(3);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);
                        txtSS.setVisibility(View.GONE);
                        txtSS1.setVisibility(View.VISIBLE);
                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("04") && rdoSSOther.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        secMethodReceveDT1.setVisibility(View.VISIBLE);
                        secValidity.setVisibility(View.VISIBLE);
                        spnValidityUnit.setSelection(3);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);
                        txtSS.setVisibility(View.GONE);
                        txtSS1.setVisibility(View.VISIBLE);
                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("04") && rdoSSOtherGovt.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        secMethodReceveDT1.setVisibility(View.VISIBLE);
                        secValidity.setVisibility(View.VISIBLE);
                        spnValidityUnit.setSelection(3);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);
                        txtSS.setVisibility(View.GONE);
                        txtSS1.setVisibility(View.VISIBLE);
                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("04")) {
                        secSS.setVisibility(View.VISIBLE);
                        rdogrpSS.clearCheck();
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secMethodReceveDT.setVisibility(View.GONE);
                        secMethodReceveDT1.setVisibility(View.GONE);
                        dtpMD.setText("");
                        secValidity.setVisibility(View.GONE);
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);
                        txtSS.setVisibility(View.GONE);
                        txtSS1.setVisibility(View.VISIBLE);
                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("05") && rdoSSFWA.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        secMethodReceveDT1.setVisibility(View.VISIBLE);
                        secValidity.setVisibility(View.VISIBLE);
                        spnValidityUnit.setSelection(3);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);
                        txtSS.setVisibility(View.GONE);
                        txtSS1.setVisibility(View.VISIBLE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("05") && rdoSSLMarket.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.VISIBLE);
                        spnBrand.setAdapter(C.getArrayAdapter("Select '  'as brandCode union Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('77') order by brandCode asc"));
                        spnMethodUnit.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        secMethodReceveDT1.setVisibility(View.VISIBLE);
                        secValidity.setVisibility(View.VISIBLE);
                        spnValidityUnit.setSelection(3);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);
                        txtSS.setVisibility(View.GONE);
                        txtSS1.setVisibility(View.VISIBLE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("05") && rdoSSOther.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.VISIBLE);
                        spnBrand.setAdapter(C.getArrayAdapter("Select '  'as brandCode union Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('04','05','77') order by brandCode asc"));
                        spnMethodUnit.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        secMethodReceveDT1.setVisibility(View.VISIBLE);
                        secValidity.setVisibility(View.VISIBLE);
                        spnValidityUnit.setSelection(3);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);
                        txtSS.setVisibility(View.GONE);
                        txtSS1.setVisibility(View.VISIBLE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("05") && rdoSSOtherGovt.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.VISIBLE);
                        spnBrand.setAdapter(C.getArrayAdapter("Select '  'as brandCode union Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('04','05') order by brandCode asc"));
                        spnMethodUnit.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        secMethodReceveDT1.setVisibility(View.VISIBLE);
                        secValidity.setVisibility(View.VISIBLE);
                        spnValidityUnit.setSelection(3);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);
                        txtSS.setVisibility(View.GONE);
                        txtSS1.setVisibility(View.VISIBLE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("05")) {
                        secSS.setVisibility(View.VISIBLE);
                        rdogrpSS.clearCheck();
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        txtMethodQty.setText("");
                        spnMethodUnit.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secMethodReceveDT.setVisibility(View.GONE);
                        secMethodReceveDT1.setVisibility(View.GONE);
                        secValidity.setVisibility(View.GONE);
                        spnValidityUnit.setSelection(0);
                        dtpMD.setText("");
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);
                        txtSS.setVisibility(View.GONE);
                        txtSS1.setVisibility(View.VISIBLE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("06") && rdoSSFWA.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);

                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        secMethodReceveDT1.setVisibility(View.GONE);
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);
                        txtSS.setVisibility(View.GONE);
                        txtSS1.setVisibility(View.VISIBLE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("06") && rdoSSLMarket.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        secMethodReceveDT1.setVisibility(View.GONE);
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);
                        txtSS.setVisibility(View.GONE);
                        txtSS1.setVisibility(View.VISIBLE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("06") && rdoSSOther.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        secMethodReceveDT1.setVisibility(View.GONE);
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);
                        txtSS.setVisibility(View.GONE);
                        txtSS1.setVisibility(View.VISIBLE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("06") && rdoSSOtherGovt.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        secMethodReceveDT1.setVisibility(View.GONE);
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);
                        txtSS.setVisibility(View.GONE);
                        txtSS1.setVisibility(View.VISIBLE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("06")) {
                        secSS.setVisibility(View.VISIBLE);
                        rdogrpSS.clearCheck();
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        secMethodReceveDT1.setVisibility(View.GONE);
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);
                        txtSS.setVisibility(View.GONE);
                        txtSS1.setVisibility(View.VISIBLE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("07") && rdoSSFWA.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnMethodUnit.setSelection(3);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        secMethodReceveDT1.setVisibility(View.GONE);
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);
                        txtSS.setVisibility(View.GONE);
                        txtSS1.setVisibility(View.VISIBLE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("07") && rdoSSLMarket.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnMethodUnit.setSelection(3);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        secMethodReceveDT1.setVisibility(View.GONE);
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);
                        txtSS.setVisibility(View.GONE);
                        txtSS1.setVisibility(View.VISIBLE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("07") && rdoSSOther.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnMethodUnit.setSelection(3);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        secMethodReceveDT1.setVisibility(View.GONE);
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);
                        txtSS.setVisibility(View.GONE);
                        txtSS1.setVisibility(View.VISIBLE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("07") && rdoSSOtherGovt.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnMethodUnit.setSelection(3);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        secMethodReceveDT1.setVisibility(View.GONE);
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);
                        txtSS.setVisibility(View.GONE);
                        txtSS1.setVisibility(View.VISIBLE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("07")) {
                        secSS.setVisibility(View.VISIBLE);
                        rdogrpSS.clearCheck();
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        secMethodReceveDT1.setVisibility(View.GONE);
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);
                        txtSS.setVisibility(View.GONE);
                        txtSS1.setVisibility(View.VISIBLE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("08") && rdoSSFWA.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.VISIBLE);
                        spnMethodUnit.setSelection(3);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        secMethodReceveDT1.setVisibility(View.GONE);
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);
                        txtSS.setVisibility(View.VISIBLE);
                        txtSS1.setVisibility(View.GONE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("08") && rdoSSLMarket.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        txtMethodQty.setText("");
                        spnMethodUnit.setSelection(0);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        secMethodReceveDT1.setVisibility(View.GONE);
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);
                        txtSS.setVisibility(View.VISIBLE);
                        txtSS1.setVisibility(View.GONE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("08") && rdoSSOther.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        txtMethodQty.setText("");
                        spnMethodUnit.setSelection(0);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        secMethodReceveDT1.setVisibility(View.GONE);
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);
                        txtSS.setVisibility(View.VISIBLE);
                        txtSS1.setVisibility(View.GONE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("08") && rdoSSOtherGovt.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        txtMethodQty.setText("");
                        spnMethodUnit.setSelection(0);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        secMethodReceveDT1.setVisibility(View.GONE);
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);
                        txtSS.setVisibility(View.VISIBLE);
                        txtSS1.setVisibility(View.GONE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("08")) {
                        secSS.setVisibility(View.VISIBLE);
                        rdogrpSS.clearCheck();
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        secMethodReceveDT1.setVisibility(View.GONE);
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);
                        txtSS.setVisibility(View.VISIBLE);
                        txtSS1.setVisibility(View.GONE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("09") && rdoSSFWA.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.VISIBLE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(3);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        secMethodReceveDT1.setVisibility(View.GONE);
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);
                        txtSS.setVisibility(View.VISIBLE);
                        txtSS1.setVisibility(View.GONE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("09") && rdoSSLMarket.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        txtMethodQty.setText("");
                        spnMethodUnit.setSelection(0);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        secMethodReceveDT1.setVisibility(View.GONE);
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);
                        txtSS.setVisibility(View.VISIBLE);
                        txtSS1.setVisibility(View.GONE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("09") && rdoSSOther.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        txtMethodQty.setText("");
                        spnMethodUnit.setSelection(0);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        secMethodReceveDT1.setVisibility(View.GONE);
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);
                        txtSS.setVisibility(View.VISIBLE);
                        txtSS1.setVisibility(View.GONE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("09") && rdoSSOtherGovt.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        txtMethodQty.setText("");
                        spnMethodUnit.setSelection(0);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        secMethodReceveDT1.setVisibility(View.GONE);
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);
                        txtSS.setVisibility(View.VISIBLE);
                        txtSS1.setVisibility(View.GONE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("09")) {
                        secSS.setVisibility(View.VISIBLE);
                        rdogrpSS.clearCheck();
                        secMethodUnit.setVisibility(View.GONE);
                        spnMethodUnit.setSelection(0);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        txtMethodQty.setText("");
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        secMethodReceveDT1.setVisibility(View.GONE);
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);
                        txtSS.setVisibility(View.VISIBLE);
                        txtSS1.setVisibility(View.GONE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("10")) {
                        secSS.setVisibility(View.GONE);
                        rdogrpSS.clearCheck();
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.VISIBLE);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        secMethodReceveDT1.setVisibility(View.GONE);
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("11")) {
                        secSS.setVisibility(View.GONE);
                        rdogrpSS.clearCheck();
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.VISIBLE);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        secMethodReceveDT1.setVisibility(View.GONE);
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("15")) {
                        secSS.setVisibility(View.GONE);
                        rdogrpSS.clearCheck();
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        secMethodReceveDT1.setVisibility(View.GONE);
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("12")) {
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                        secSS.setVisibility(View.GONE);
                        rdogrpSS.clearCheck();
                        secMethodUnit.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                        secBrand.setVisibility(View.GONE);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        secMethodReceveDT1.setVisibility(View.GONE);
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        // cmdSave.setText("Save and Continue...");
                        cmdSave.setText("Save");
                    } else {
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                        secSS.setVisibility(View.GONE);
                        rdogrpSS.clearCheck();
                        secMethodUnit.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                        secBrand.setVisibility(View.GONE);
                        secSyringe.setVisibility(View.GONE);
                        secSyringe1.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        secMethodReceveDT1.setVisibility(View.GONE);
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        cmdSave.setText("Save");

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });

            btnDOM.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btnDOM";
                    showDialog(DATE_DIALOG);
                }
            });

            btnDOV.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btnDOV";
                    showDialog(DATE_DIALOG);
                }
            });


            btnMD.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btnMD";
                    showDialog(DATE_DIALOG);
                }
            });

            btnDOTT1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btnDOTT1";
                    showDialog(DATE_DIALOG);
                }
            });


            secValidity = (LinearLayout) findViewById(R.id.secValidity);
            secValidity.setVisibility(View.GONE);
            txtValidity = (EditText) findViewById(R.id.txtValidity);


            spnValidityUnit = (Spinner) findViewById(R.id.spnValidityUnit);
            List<String> listYMD = new ArrayList<String>();
            listYMD.add("");
            listYMD.add("দিন");
            listYMD.add("মাস");
            listYMD.add("বছর");
            ArrayAdapter<String> adptrspnYMD = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listYMD);
            spnValidityUnit.setAdapter(adptrspnYMD);

            DataSearch(g.getHealthID());
            ELCOMemSearch(g.getGeneratedId());
            FPIDataSearch(g.getGeneratedId());
            //ImmunizationData(txtHealthID.getText().toString());
            cmdSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                  /*  if(!rdoVSPresent.isChecked() & !rdoVSAbsent.isChecked())
                    {
                        Connection.MessageBox(ELCOFormFPI.this, "পরিদর্শনের অবস্থা সিলেক্ট করুন।");
                        rdoVSPresent.requestFocus();
                        return;
                    }
                    if(rdoVSPresent.isChecked() & spnMethod.getSelectedItemPosition()==0  & secMethod.isShown())
                    {
                        Connection.MessageBox(ELCOFormFPI.this, "জন্মনিয়ন্ত্রণ ব্যবস্থা/গর্ভাবস্থা সিলেক্ট করুন।।");
                        rdoVSPresent.requestFocus();
                        return;
                    }*/

                    DataSave();
                    DataSaveElCOVilsit();
                }
            });

            btnAddTT = (ImageButton) findViewById(R.id.btnAddTT);
            btnSaveTT = (ImageButton) findViewById(R.id.btnSaveTT);
            btnTTClose = (ImageButton) findViewById(R.id.btnTTClose);
            btnAddTT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    secTT1.setVisibility(View.VISIBLE);
                    btnTTClose.setVisibility(View.VISIBLE);
                    btnAddTT.setVisibility(View.GONE);

                    VlblTTCard.setVisibility(View.GONE);
                    rdogrpTTCard.setVisibility(View.GONE);
                    dtpDOTT1.setVisibility(View.GONE);
                    btnDOTT1.setVisibility(View.GONE);
                    btnSaveTT.setVisibility(View.GONE);
                    GridView gcount = (GridView) findViewById(R.id.gridTT);
                    g.setImuCode(String.valueOf(gcount.getCount() + 1));
                }
            });


            btnTTClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ChkTT1.setChecked(false);
                    rdogrpTTCard.clearCheck();
                    dtpDOTT1.setText("");
                    secTT1.setVisibility(View.GONE);
                    btnTTClose.setVisibility(View.GONE);
                    btnAddTT.setVisibility(View.VISIBLE);
                    g.setImuCode("");
                }
            });


            //ImageButton btnSaveTT = (ImageButton)findViewById(R.id.btnSaveTT);
       /*     btnSaveTT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //GridView gcount=(GridView) findViewById(R.id.gridTT);
                    if(!ChkTT1.isChecked())
                    {
                        Connection.MessageBox(ELCOFormFPI.this,"টিটি পেয়েছে কিনা এ তথ্য না হতে পারে না।");
                        ChkTT1.requestFocus();
                        return;
                    }
                    else if(ChkTT1.isChecked())
                    {
                        if(!rdoCardYes.isChecked() & !rdoCardNo.isChecked())
                        {
                            Connection.MessageBox(ELCOFormFPI.this,"টিটি কার্ড আছে কিনা এ তথ্য হ্যাঁ/না হতে হবে।");
                            rdoCardYes.requestFocus();
                            return;
                        }
                        else if(rdoCardYes.isChecked() & dtpDOTT1.getText().length()==0)
                        {
                            Connection.MessageBox(ELCOFormFPI.this,"টিটি কার্ড থাকলে অবশ্যই তারিখ থাকতে হবে।");
                            dtpDOTT1.requestFocus();
                            return;
                        }
                        else if(rdoCardNo.isChecked() & dtpDOTT1.getText().length()==0)
                        {

                        }

                        else
                        {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            String formattedDate = sdf.format(c.getTime());
                            Date date1 = null;
                            try {
                                date1 = sdf.parse(formattedDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            Date date2 = null;
                            try {
                                date2 = sdf.parse(dtpDOTT1.getText().toString());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            if (date2.after(date1))
                            {
                                Connection.MessageBox(ELCOFormFPI.this,"টিটি এর তারিখ আজকের তারিখ["+ formattedDate +"] এর সমান অথবা কম হতে হবে।");
                                dtpDOTT1.requestFocus();
                                return;
                            }
                            if(validateTTDateAgainstDOB())
                            {
                                Connection.MessageBox(ELCOFormFPI.this,"টিটি এর তারিখ জন্ম তারিখ এর থেকে  কম হবে না ।");
                                dtpDOTT1.requestFocus();
                                return;
                            }
                        }
                    }

                    ImmunizationSave(g.getImuCode());

                    ChkTT1.setChecked(false);
                    rdogrpTTCard.clearCheck();
                    dtpDOTT1.setText("");
                    secTT1.setVisibility(View.GONE);
                    btnTTClose.setVisibility(View.GONE);
                    btnAddTT.setVisibility(View.VISIBLE);
                    g.setImuCode("");
                    ImmunizationStatus();
                }
            });
*/


            ImmunizationStatus();
            ElcoVisitStsatus();

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } catch (Exception e) {
            Connection.MessageBox(ELCOFormFPI.this, e.getMessage());
            return;
        }
    }

    private void DataSave() {
        try {
            String SQL = "";
            String AG = "";

            if (!rdoHusName1.isChecked() & !rdoHusName2.isChecked() & secHusName.isShown()) {
                Connection.MessageBox(ELCOFormFPI.this, "স্বামীর নাম যাচাই করুন ।");
                rdoHusName1.requestFocus();
                return;
            } else if (!rdoHusAge1.isChecked() & !rdoHusAge2.isChecked() & secHusName1.isShown()) {
                Connection.MessageBox(ELCOFormFPI.this, "স্বামীর বয়স্ক যাচাই করুন ।");
                rdoHusAge1.requestFocus();
                return;
            }

           /* else if(!rdoELCONo1.isChecked() & !rdoELCONo2.isChecked() & secELCONo.isShown())
            {
                Connection.MessageBox(ELCOFormFPI.this, "দম্পতি নং  যাচাই করুন ।");
                rdoHusAge1.requestFocus();
                return;
            }

            else if(!rdoHHNo1.isChecked() & !rdoHHNo2.isChecked() & secHHNo.isShown())
            {
                Connection.MessageBox(ELCOFormFPI.this, "খানা নং  যাচাই করুন ।");
                rdoHHNo1.requestFocus();
                return;
            }

              else if(!rdoHHNoNoDontKnow1.isChecked() & !rdoHHNoNoDontKnow2.isChecked() & secHHNo1.isShown())
            {
                Connection.MessageBox(ELCOFormFPI.this, "খানা নং জানি না যাচাই করুন ।");
                rdoHHNoNoDontKnow1.requestFocus();
                return;
            }

          else if(!rdoDOMSou1.isChecked() & !rdoDOMSou2.isChecked() & secDOMSource.isShown())
            {
                Connection.MessageBox(ELCOFormFPI.this, "বিবাহের তারিখ যাচাই করুন ।");
                rdoHHNoNoDontKnow1.requestFocus();
                return;
            }

            else if(!rdoMAge1.isChecked() & !rdoMAge2.isChecked() & secDOMSource1.isShown())
            {
                Connection.MessageBox(ELCOFormFPI.this, "বিবাহের তারিখ  যাচাই করুন ।");
                rdoMAge1.requestFocus();
                return;
            }

            else if(!rdoLiveSon1.isChecked() & !rdoLiveSon2.isChecked() & secLiveSon.isShown())
            {
                Connection.MessageBox(ELCOFormFPI.this, "জীবিত     ছেলে  যাচাই করুন ।");
                rdoLiveSon1.requestFocus();
                return;
            }

            else if(!rdoLiveDau1.isChecked() & !rdoLiveDau2.isChecked() & secLiveDau.isShown())
            {
                Connection.MessageBox(ELCOFormFPI.this, "জীবিত     মেয়ে  যাচাই করুন ।");
                rdoLiveSon1.requestFocus();
                return;
            }
            else if(!rdoTT11.isChecked() & !rdoTT12.isChecked() & secTT1a.isShown())
            {
                Connection.MessageBox(ELCOFormFPI.this, "টিটি-1  যাচাই করুন ।");
                rdoTT11.requestFocus();
                return;
            }

            else if(!rdoTT21.isChecked() & !rdoTT22.isChecked() & secTT2b.isShown())
            {
                Connection.MessageBox(ELCOFormFPI.this, "টিটি-2  যাচাই করুন ।");
                rdoTT21.requestFocus();
                return;
            }

            else if(!rdoTT31.isChecked() & !rdoTT32.isChecked() & secTT3c.isShown())
            {
                Connection.MessageBox(ELCOFormFPI.this, "টিটি-3  যাচাই করুন ।");
                rdoTT31.requestFocus();
                return;
            }

            else if(!rdoTT41.isChecked() & !rdoTT42.isChecked() & secTT4d.isShown())
            {
                Connection.MessageBox(ELCOFormFPI.this, "টিটি-4  যাচাই করুন ।");
                rdoTT41.requestFocus();
                return;
            }

            else if(!rdoTT51.isChecked() & !rdoTT52.isChecked() & secTT5e.isShown())
            {
                Connection.MessageBox(ELCOFormFPI.this, "টিটি-5  যাচাই করুন ।");
                rdoTT51.requestFocus();
                return;
            }

            else if(!rdoElcoVisit1.isChecked() & !rdoElcoVisit2.isChecked() & secElcoVisit.isShown())
            {
                Connection.MessageBox(ELCOFormFPI.this, "তারিখ  যাচাই করুন ।");
                rdoElcoVisit1.requestFocus();
                return;
            }

            else if(!rdoVSPA1.isChecked() & !rdoVSPA2.isChecked() & secVSPA.isShown())
            {
                Connection.MessageBox(ELCOFormFPI.this, "তারিখ  যাচাই করুন ।");
                rdoVSPA1.requestFocus();
                return;
            }
            else if(!rdoMethod1.isChecked() & !rdoMethod2.isChecked() & secMethod1.isShown())
            {
                Connection.MessageBox(ELCOFormFPI.this, "জন্মনিয়ন্ত্রণ ব্যবস্থা/গর্ভাবস্থা  যাচাই করুন ।");
                rdoMethod1.requestFocus();
                return;
            }

            else if(!rdoSorce1.isChecked() & !rdoSorce2.isChecked() & secSS.isShown())
            {
                Connection.MessageBox(ELCOFormFPI.this, "উৎস যাচাই করুন ।");
                rdoSorce1.requestFocus();
                return;
            }

            else if(!rdoBrand1.isChecked() & !rdoBrand2.isChecked() & secBrand.isShown())
            {
                Connection.MessageBox(ELCOFormFPI.this, "উৎস যাচাই করুন ।");
                rdoBrand1.requestFocus();
                return;
            }

            else if(!rdoBrand1.isChecked() & !rdoBrand2.isChecked() & secBrand.isShown())
            {
                Connection.MessageBox(ELCOFormFPI.this, "উৎস যাচাই করুন ।");
                rdoBrand1.requestFocus();
                return;
            }

            else if(!rdoMethodUnit1.isChecked() & !rdoMethodUnit2.isChecked() & secMethodUnit.isShown())
            {
                Connection.MessageBox(ELCOFormFPI.this, "পরিমাণ যাচাই করুন ।");
                rdoMethodUnit1.requestFocus();
                return;
            }

            else if(!rdoSyringeQty1.isChecked() & !rdoSyringeQty2.isChecked() & secSyringe.isShown())
            {
                Connection.MessageBox(ELCOFormFPI.this, "সিরিঞ্জ যাচাই করুন ।");
                rdoSyringeQty1.requestFocus();
                return;
            }

            else if(!rdoDueDoze1.isChecked() & !rdoDueDoze2.isChecked() & secSyringe1.isShown())
            {
                Connection.MessageBox(ELCOFormFPI.this, "ডিউ ডোজ যাচাই করুন ।");
                rdoDueDoze1.requestFocus();
                return;
            }

            else if(!rdoMethodReceve1.isChecked() & !rdoMethodReceve2.isChecked() & secMethodReceveDT.isShown())
            {
                Connection.MessageBox(ELCOFormFPI.this, "পদ্ধতি গ্রহণের তারিখ যাচাই করুন ।");
                rdoMethodReceve1.requestFocus();
                return;
            }

            else if(!rdoMDAge1.isChecked() & !rdoMDAge2.isChecked() & secMethodReceveDT1.isShown())
            {
                Connection.MessageBox(ELCOFormFPI.this, "পদ্ধতি গ্রহণের তারিখ (মাস পূর্বে) যাচাই করুন ।");
                rdoMDAge1.requestFocus();
                return;
            }

            else if(!rdoWherePlace1.isChecked() & !rdoWherePlace2.isChecked() & secWherePlace.isShown())
            {
                Connection.MessageBox(ELCOFormFPI.this, "কোথায় প্রেরণ যাচাই করুন ।");
                rdoWherePlace1.requestFocus();
                return;
            }

            else if(!rdoValidity1.isChecked() & !rdoValidity2.isChecked() & secWherePlace.isShown())
            {
                Connection.MessageBox(ELCOFormFPI.this, "কোথায় প্রেরণ যাচাই করুন ।");
                rdoWherePlace1.requestFocus();
                return;
            }

            else if(!rdoValidity1.isChecked() & !rdoValidity2.isChecked() & secValidity.isShown())
            {
                Connection.MessageBox(ELCOFormFPI.this, "কার্যকারিতা যাচাই করুন ।");
                rdoValidity1.requestFocus();
                return;
            }

            else if(!rdoValidity1.isChecked() & !rdoValidity2.isChecked() & secValidity.isShown())
            {
                Connection.MessageBox(ELCOFormFPI.this, "কার্যকারিতা যাচাই করুন ।");
                rdoValidity1.requestFocus();
                return;
            }*/
           /* else if(!rdoNID1.isChecked() & !rdoNID2.isChecked() & secNID1.isShown())
            {
                Connection.MessageBox(ELCOFormFPI.this, "সদস্যের জাতীয় পরিচয় পত্র নম্বর যাচাই করুন ।");
                rdoNID1.requestFocus();
                return;
            }
            else if(!rdoNIDStatus1.isChecked() & !rdoNIDStatus2.isChecked() & secNIDStatus1.isShown())
            {
                Connection.MessageBox(ELCOFormFPI.this, "সদস্যের জাতীয় পরিচয় পত্র নম্বর না থাকলে কেন নাই যাচাই করুন ।");
                rdoNIDStatus1.requestFocus();
                return;
            }
            else if(!rdoBRID1.isChecked() & !rdoBRID2.isChecked() & secBRID1.isShown())
            {
                Connection.MessageBox(ELCOFormFPI.this, "সদস্যের জন্ম নিবন্ধন নম্বর যাচাই করুন ।");
                rdoBRID1.requestFocus();
                return;
            }
            else if(!rdoBRIDStatus1.isChecked() & !rdoBRIDStatus2.isChecked() & secBRIDStatus1.isShown())
            {
                Connection.MessageBox(ELCOFormFPI.this, "সদস্যের জন্ম নিবন্ধন নম্বর না থাকলে কেন নাই যাচাই করুন ।");
                rdoBRIDStatus1.requestFocus();
                return;
            }*/
            if (!C.Existence("Select healthId from " + TableName + "  Where healthId='" + g.getGeneratedId() + "'")) {
                SQL = "Insert into " + TableName + "(HealthID,providerId,systemEntryDate,Upload)Values('" + g.getGeneratedId() + "','" + g.getProvCode() + "','" + Global.DateTimeNowYMDHMS() + "','2')";
                C.Save(SQL);
            }

            SQL = "Update " + TableName + " Set Upload='2',";
            SQL += "husbandName = '" + (rdoHusName1.isChecked() ? "1" : (rdoHusName2.isChecked() ? "2" : "")) + "',";

            SQL += "husbandAge = '" + (rdoHusAge1.isChecked() ? "1" : (rdoHusAge2.isChecked() ? "2" : "")) + "',";
            SQL += "elcoNo = '" + (rdoELCONo1.isChecked() ? "1" : (rdoELCONo2.isChecked() ? "2" : "")) + "',";
            SQL += "hhStatus = '" + (rdoHHNoNoDontKnow1.isChecked() ? "1" : (rdoHHNoNoDontKnow1.isChecked() ? "2" : "")) + "',";
            SQL += "haHHNo = '" + (rdoHHNo1.isChecked() ? "1" : (rdoHHNo2.isChecked() ? "2" : "")) + "',";
            SQL += "domSource = '" + (rdoDOMSou1.isChecked() ? "1" : (rdoDOMSou2.isChecked() ? "2" : "")) + "',";
            if (rdoDOMSource1.isChecked() == true) {
                SQL += "marrDate = '" + (rdoMAge1.isChecked() ? "1" : (rdoMAge2.isChecked() ? "2" : "")) + "',";
            } else {
                SQL += "marrAge = '" + (rdoMAge1.isChecked() ? "1" : (rdoMAge2.isChecked() ? "2" : "")) + "',";
            }
            SQL += "son = '" + (rdoLiveSon1.isChecked() ? "1" : (rdoLiveSon2.isChecked() ? "2" : "")) + "',";
            SQL += "dau = '" + (rdoLiveDau1.isChecked() ? "1" : (rdoLiveDau2.isChecked() ? "2" : "")) + "',";
            SQL += "tt1 = '" + (rdoTT11.isChecked() ? "1" : (rdoTT12.isChecked() ? "2" : "")) + "',";
            SQL += "tt2 = '" + (rdoTT21.isChecked() ? "1" : (rdoTT22.isChecked() ? "2" : "")) + "',";
            SQL += "tt3 = '" + (rdoTT31.isChecked() ? "1" : (rdoTT32.isChecked() ? "2" : "")) + "',";
            SQL += "tt4 = '" + (rdoTT41.isChecked() ? "1" : (rdoTT42.isChecked() ? "2" : "")) + "',";
            SQL += "tt5 = '" + (rdoTT51.isChecked() ? "1" : (rdoTT52.isChecked() ? "2" : "")) + "'";
            SQL += " Where healthId='" + g.getGeneratedId() + "'";
            C.Save(SQL);
            Connection.MessageBox(ELCOFormFPI.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
            finish();

        } catch (Exception e) {
            Connection.MessageBox(ELCOFormFPI.this, e.getMessage());
            return;
        }
    }

    private void DataSaveElCOVilsit() {
        try {
            String SQL = "";
            String AG = "";

            String visit = GetVisitId();

         /*   if(!rdoHusName1.isChecked() & !rdoHusName2.isChecked() & secHusName.isShown())
            {
                Connection.MessageBox(ELCOFormFPI.this, "স্বামীর নাম যাচাই করুন ।");
                rdoHusName1.requestFocus();
                return;
            }*/
            /*else if(!rdoHusAge1.isChecked() & !rdoHusAge2.isChecked() & secHusName1.isShown())
            {
                Connection.MessageBox(ELCOFormFPI.this, "স্বামীর বয়স্ক যাচাই করুন ।");
                rdoHusAge1.requestFocus();
                return;
            }*/


            if (!C.Existence("Select healthId,visit from " + TableNameELCOVisit + "  Where healthId='" + g.getGeneratedId() + "' and visit='" + visit + "'")) {
                SQL = "Insert into " + TableNameELCOVisit + "(HealthID,visit,providerId,systemEntryDate,Upload)Values('" + g.getGeneratedId() + "','" + visit + "','" + g.getProvCode() + "','" + Global.DateTimeNowYMDHMS() + "','2')";
                C.Save(SQL);
            }

            SQL = "Update " + TableNameELCOVisit + " Set Upload='2',";
            SQL += "vDate = '" + (rdoElcoVisit1.isChecked() ? "1" : (rdoElcoVisit2.isChecked() ? "2" : "")) + "',";
            SQL += "visitStatus = '" + (rdoVSPA1.isChecked() ? "1" : (rdoVSPA2.isChecked() ? "2" : "")) + "',";
            SQL += "currStatus = '" + (rdoMethod1.isChecked() ? "1" : (rdoMethod2.isChecked() ? "2" : "")) + "',";
            SQL += "sSource = '" + (rdoSorce1.isChecked() ? "1" : (rdoSorce2.isChecked() ? "2" : "")) + "',";
            SQL += "brand = '" + (rdoBrand1.isChecked() ? "1" : (rdoBrand2.isChecked() ? "2" : "")) + "',";
            SQL += "qty = '" + (rdoMethodQty1.isChecked() ? "1" : (rdoMethodQty2.isChecked() ? "2" : "")) + "',";
            SQL += "syrinsQty = '" + (rdoSyringeQty1.isChecked() ? "1" : (rdoSyringeQty2.isChecked() ? "2" : "")) + "',";
            //Not add Columes DueDoze,MethodReceve
            // SQL += "syrinsQty = '"+ (rdoDueDoze1.isChecked()?"1":(rdoDueDoze2.isChecked()?"2":"")) +"',";

            SQL += "dayMonYear = '" + (rdoMDAge1.isChecked() ? "1" : (rdoMDAge2.isChecked() ? "2" : "")) + "',";
            SQL += "referPlace = '" + (rdoWherePlace1.isChecked() ? "1" : (rdoWherePlace2.isChecked() ? "2" : "")) + "',";
            SQL += "validity = '" + (rdoValidity1.isChecked() ? "1" : (rdoValidity2.isChecked() ? "2" : "")) + "'";

            /*SQL += "husbandAge = '"+ (rdoHusAge1.isChecked()?"1":(rdoHusAge2.isChecked()?"2":"")) +"',";
            SQL += "elcoNo = '"+ (rdoELCONo1.isChecked()?"1":(rdoELCONo2.isChecked()?"2":"")) +"',";
            SQL += "hhStatus = '"+ (rdoHHNoNoDontKnow1.isChecked()?"1":(rdoHHNoNoDontKnow1.isChecked()?"2":"")) +"',";
            SQL += "haHHNo = '"+ (rdoHHNo1.isChecked()?"1":(rdoHHNo2.isChecked()?"2":"")) +"',";
            SQL += "domSource = '"+ (rdoDOMSou1.isChecked()?"1":(rdoDOMSou2.isChecked()?"2":"")) +"',";
            if(rdoDOMSource1.isChecked()==true) {
                SQL += "marrDate = '" + (rdoMAge1.isChecked() ? "1" : (rdoMAge2.isChecked() ? "2" : "")) + "',";
            }
            else
            {
                SQL += "marrAge = '" + (rdoMAge1.isChecked() ? "1" : (rdoMAge2.isChecked() ? "2" : "")) + "',";
            }
            SQL += "son = '"+ (rdoLiveSon1.isChecked()?"1":(rdoLiveSon2.isChecked()?"2":"")) +"',";
            SQL += "dau = '"+ (rdoLiveDau1.isChecked()?"1":(rdoLiveDau2.isChecked()?"2":"")) +"',";
            SQL += "tt1 = '"+ (rdoTT11.isChecked()?"1":(rdoTT12.isChecked()?"2":"")) +"',";
            SQL += "tt2 = '"+ (rdoTT21.isChecked()?"1":(rdoTT22.isChecked()?"2":"")) +"',";
            SQL += "tt3 = '"+ (rdoTT31.isChecked()?"1":(rdoTT32.isChecked()?"2":"")) +"',";
            SQL += "tt4 = '"+ (rdoTT41.isChecked()?"1":(rdoTT42.isChecked()?"2":"")) +"',";
            SQL += "tt5 = '"+ (rdoTT51.isChecked()?"1":(rdoTT52.isChecked()?"2":"")) +"'";*/
            SQL += " Where healthId='" + g.getGeneratedId() + "' and visit='" + visit + "'";
            C.Save(SQL);
            Connection.MessageBox(ELCOFormFPI.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
            finish();

        } catch (Exception e) {
            Connection.MessageBox(ELCOFormFPI.this, e.getMessage());
            return;
        }
    }

    private String GetVisitId() {
        String SQL = "";
        String ProvCode = "";

        ProvCode = "select providerId from elco";
        ProvCode += " where healthId='" + g.getGeneratedId() + "'";
        String ProvID = C.ReturnSingleValue(ProvCode);

        SQL = "select visit from elcoVisit";
        SQL += " where healthId='" + g.getGeneratedId() + "' and providerId='" + ProvID + "' and strftime('%d/%m/%Y', date(vDate))='" + dtpDOV.getText() + "'";
        String SNo = C.ReturnSingleValue(SQL);
        // Serial No auto increment
        return SNo;
    }

    private void FPIDataSearch(String generatedId) {
        try {
            RadioButton rb;
            String SQL1 = "";
            SQL1 = "Select ifnull(healthId,'') as HealthID,ifnull(husbandName,'') as husbandName,ifnull(husbandAge,'') as husbandAge,ifnull(elcoNo,'') as elcoNo,ifnull(hhStatus,'') as hhStatus,ifnull(haHHNo,'') as haHHNo,ifnull(domSource,'') as domSource,ifnull(marrDate,'') as marrDate,ifnull(marrAge,'') as marrAge,ifnull(son,'') as son,ifnull(dau,'') as dau,ifnull(tt1,'') as tt1,ifnull(tt2,'') as tt2,ifnull(tt3,'') as tt3,ifnull(tt4,'') as tt4,ifnull(tt5,'') as tt5\n" +
                    "from elcoFpi FPI Where HealthID='" + generatedId + "'";
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

                if (cur1.getString(cur1.getColumnIndex("husbandName")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoHusName1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("husbandName")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoHusName2.setChecked(true);
                }


                if (cur1.getString(cur1.getColumnIndex("husbandAge")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoHusAge1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("husbandAge")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoHusAge2.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("elcoNo")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoELCONo1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("elcoNo")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoELCONo2.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("haHHNo")).replace("null", "").toString().equalsIgnoreCase("1")) {

                    rdoHHNo1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("haHHNo")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoHHNo2.setChecked(true);
                }
                if (cur1.getString(cur1.getColumnIndex("hhStatus")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoHHNoNoDontKnow1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("hhStatus")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoHHNoNoDontKnow2.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("domSource")).replace("null", "").toString().equalsIgnoreCase("1")) {

                    rdoDOMSou1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("domSource")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoDOMSou1.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("marrDate")).replace("null", "").toString().equalsIgnoreCase("1")) {

                    rdoMAge1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("marrDate")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoMAge1.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("marrAge")).replace("null", "").toString().equalsIgnoreCase("1")) {

                    rdoMAge1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("marrAge")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoMAge1.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("son")).replace("null", "").toString().equalsIgnoreCase("1")) {

                    rdoLiveSon1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("son")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoLiveSon1.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("dau")).replace("null", "").toString().equalsIgnoreCase("1")) {

                    rdoLiveDau1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("dau")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoLiveDau2.setChecked(true);
                }
                if (cur1.getString(cur1.getColumnIndex("tt1")).replace("null", "").toString().equalsIgnoreCase("1")) {

                    rdoTT11.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("tt1")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoTT12.setChecked(true);
                }
                if (cur1.getString(cur1.getColumnIndex("tt2")).replace("null", "").toString().equalsIgnoreCase("1")) {

                    rdoTT21.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("tt2")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoTT22.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("tt3")).replace("null", "").toString().equalsIgnoreCase("1")) {

                    rdoTT31.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("tt3")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoTT32.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("tt4")).replace("null", "").toString().equalsIgnoreCase("1")) {

                    rdoTT41.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("tt4")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoTT42.setChecked(true);
                }
                if (cur1.getString(cur1.getColumnIndex("tt5")).replace("null", "").toString().equalsIgnoreCase("1")) {

                    rdoTT51.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("tt5")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoTT52.setChecked(true);
                }

                cur1.moveToNext();
            }
            cur1.close();
        } catch (Exception e) {
            Connection.MessageBox(ELCOFormFPI.this, e.getMessage());
            return;
        }
    }


    private void FPIDataSearchElcoVisit(String generatedId, String Visit) {
        try {
            RadioButton rb;
            String SQL1 = "";
            SQL1 = "Select ifnull(healthId,'') as HealthID,ifnull(vDate,'') as vDate,ifnull(visitStatus,'') as visitStatus,ifnull(currStatus,'') as currStatus,ifnull(sSource,'') as sSource,ifnull(brand,'') as brand,ifnull(qty,'') as qty,ifnull(syrinsQty,'') as syrinsQty\n" +
                    "from elcoVisitFPI Where HealthID='" + generatedId + "' and visit='" + Visit + "'";
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

                if (cur1.getString(cur1.getColumnIndex("vDate")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoElcoVisit1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("vDate")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoElcoVisit1.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("visitStatus")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoVSPA1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("visitStatus")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoVSPA2.setChecked(true);
                }


                if (cur1.getString(cur1.getColumnIndex("currStatus")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoMethod1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("currStatus")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoMethod2.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("sSource")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoSorce1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("sSource")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoSorce2.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("brand")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoBrand1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("brand")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoBrand2.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("qty")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoMethodQty1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("qty")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoMethodQty2.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("syrinsQty")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoSyringeQty1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("syrinsQty")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoSyringeQty2.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("syrinsQty")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoDueDoze1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("syrinsQty")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoDueDoze2.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("syrinsQty")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoMethodReceve1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("syrinsQty")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoMethodReceve2.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("syrinsQty")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoMDAge1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("syrinsQty")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoMDAge2.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("syrinsQty")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoWherePlace1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("syrinsQty")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoWherePlace2.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("syrinsQty")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoValidity1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("syrinsQty")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoValidity2.setChecked(true);
                }

                cur1.moveToNext();
            }
            cur1.close();
        } catch (Exception e) {
            Connection.MessageBox(ELCOFormFPI.this, e.getMessage());
            return;
        }
    }

    private boolean validateVisitDateAgainstSystemDate() {

        if ((Global.DateDifferenceDays(dtpDOV.getText().toString(), Global.DateNowDMY()) >= 1)) {
            return true;
        }

        return false;
    }

    private boolean validateMarriageDateAgainstSystemDate() {

        if ((Global.DateDifferenceDays(dtpDOM.getText().toString(), Global.DateNowDMY()) >= 1)) {
            return true;
        }

        return false;
    }

    private boolean validateMethodDateAgainstMarriageDate() {

        if ((Global.DateDifferenceDays(dtpDOM.getText().toString(), dtpDOV.getText().toString()) >= 1)) {
            return true;
        }

        return false;
    }

    private boolean validateTTDateAgainstDOB() {
        String sq = String.format("Select DOB from Member where healthId = '%s' ", g.getHealthID());
        if (C.Existence(sq)) {
            String DOB = C.ReturnSingleValue(String.format("Select DOB from Member where healthId = '%s' ", g.getHealthID()));

            if (Global.DateDifferenceDays(dtpDOTT1.getText().toString(), Global.DateConvertDMY(DOB)) < 1) {
                return true;
            }
        }
        return false;
    }

    private void MethodList(String Status, String sex) {
        spnMethod.setAdapter(null);
        if (Status.equals("p") & sex.equals("1")) {
            spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2)  in('06') order by EvCode asc"));
            cmdSave.setEnabled(true);
        } else if (Status.equals("p") & sex.equals("2")) {
            if (CurrentStatus(g.getGeneratedId()).equals("12")) {
                spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2) in('09') order by EvCode asc"));//in('09','12','13','14') order by EvCode asc"));
                cmdSave.setEnabled(true);
            } else if (CurrentStatus(g.getGeneratedId()).equals("09")) {
                spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode"));
                cmdSave.setEnabled(true);
            } else {
                spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2) not in('13','14','09') order by EvCode asc"));
                cmdSave.setEnabled(true);
            }
        } else if (Status.equals("a") & sex.equals("1")) {
            spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode"));
            cmdSave.setEnabled(true);
        } else if (Status.equals("a") & sex.equals("2")) {
            if (CurrentStatus(g.getGeneratedId()).equals("12")) {
                spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2) in('09') order by EvCode asc"));
                cmdSave.setEnabled(true);
            } else if (CurrentStatus(g.getGeneratedId()).equals("09")) {
                spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode"));
                cmdSave.setEnabled(true);
            } else
                spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2) in('01','02') order by EvCode asc"));
        }
    }

    public void ElcoVisitStsatus() {
        g1 = (GridView) findViewById(R.id.gridElcoVisit);
        g1.setAdapter(new ElcoVisit(this));
        g1.setNumColumns(8);
    }


    public void ImmunizationStatus() {
        GridView g1 = (GridView) findViewById(R.id.gridTT);
        g1.setAdapter(new TTCard(this));
        g1.setNumColumns(6);
    }

    //Pregnancy
    private String CurrentStatus(String HealthID) {
        return C.ReturnSingleValue("select ifnull(currstatus,'')currstatus from elcovisit where healthid='" + g.getGeneratedId() + "' order by date(vDate) desc limit 1");//order by date(systementrydate) desc limit 1// select ifnull(currstatus,'')currstatus from elcovisit where healthid='"+ HealthID +"' order by date(systementrydate) desc limit 1
    }

    //Pregnancy
    private String CurrentStatusValidate(String HealthID) {
        return C.ReturnSingleValue("select ifnull(currstatus,'')currstatus from elcovisit where healthid='" + g.getGeneratedId() + "' and vDate = '" + Global.DateConvertYMD(dtpDOV.getText().toString()) + "'");//order by date(vDate) desc limit 1// select ifnull(currstatus,'')currstatus from elcovisit where healthid='"+ HealthID +"' order by date(systementrydate) desc limit 1
    }

    private String VisitNumber(String HealthID) {
        String SQL = "";
        SQL = "Select (ifnull(max(cast(Visit as int)),0)+1)MaxVisit from ELCOVisit";
        SQL += " where healthid='" + HealthID + "'";

        String VisitNo = Global.Right(("00" + C.ReturnSingleValue(SQL)), 2);

        return VisitNo;
    }


   /* private void DataSave()
    {
        try
        {

            //ELCO Profile
            if(!chkHHNoDontKnow.isChecked())
            {
                if(txtHHNo.getText().toString().length()==0)
                {
                    Connection.MessageBox(ELCOFormFPI.this, "খানা নম্বর কত লিখুন।");
                    txtHHNo.requestFocus();
                    return;
                }
            }

            if(txtELCONo.getText().toString().length()==0)
            {
                Connection.MessageBox(ELCOFormFPI.this, "দম্পতি নং কত লিখুন।");
                txtELCONo.requestFocus();
                return;
            }

            if(dtpDOM.getText().toString().length()==0 & dtpDOM.isShown()& rdoDOMSource1.isChecked() )
            {
                Connection.MessageBox(ELCOFormFPI.this, "প্রকৃত বিবাহের তারিখ কত লিখুন।");
                dtpDOM.requestFocus();
                return;
            }
            else if(txtMAge.getText().toString().length()==0 & rdoDOMSource2.isChecked() )
            {
                Connection.MessageBox(ELCOFormFPI.this, "বিবাহের বয়স  কত লিখুন।");
                txtMAge.requestFocus();
                return;
            }
            else if(txtLiveSon.getText().toString().length()==0)
            {
                Connection.MessageBox(ELCOFormFPI.this, "জীবিত ছেলের সংখ্যা লিখুন।");
                txtLiveSon.requestFocus();
                return;
            }
            else if(txtLiveDau.getText().toString().length()==0)
            {
                Connection.MessageBox(ELCOFormFPI.this, "জীবিত মেয়ের সংখ্যা লিখুন।");
                txtLiveDau.requestFocus();
                return;
            }

            if(validateVisitDateAgainstSystemDate())
            {
                Connection.MessageBox(ELCOFormFPI.this," ভিজিট এর তারিখ আজকের তারিখ এর সমান অথবা কম হতে হবে।");
                return;
            }
            if(GetDOV(g.getGeneratedId()).equals(""))
            {

            }
            else
            {//if(CurrentStatus(txtHealthID.getText().toString()).equals("12"))
                String status = CurrentStatusValidate(g.getGeneratedId());
                String spinnervalue = Global.Left(spnMethod.getSelectedItem().toString(), 2);

                if ((status.equalsIgnoreCase(spinnervalue) && spinnervalue.equalsIgnoreCase("01") |
                        (status.equalsIgnoreCase(spinnervalue) && spinnervalue.equalsIgnoreCase("02")) |
                        (status.equalsIgnoreCase(spinnervalue) && spinnervalue.equalsIgnoreCase("03"))  |
                        (status.equalsIgnoreCase(spinnervalue) && spinnervalue.equalsIgnoreCase("08")) |
                        (status.equalsIgnoreCase(spinnervalue) && spinnervalue.equalsIgnoreCase("09"))) && Global.DateDifferenceDays(dtpDOV.getText().toString(), Global.DateConvertDMY(GetDOV(g.getGeneratedId()))) <= 0) {

                   *//* if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("01") |
                            Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("02") |
                            Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("03")  |
                            Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("08") |
                            Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("09") )
                    {*//*
                    if (spinnervalue.equalsIgnoreCase("01")) {

                        if(secBrand.getVisibility()==View.VISIBLE)
                        {
                            if(spnBrand.getSelectedItemPosition()==0) {
                                Connection.MessageBox(ELCOFormFPI.this, " ব্র্যান্ড লিখুন");
                                return;
                            }
                        }

                        if(secMethodUnit.getVisibility()==View.VISIBLE) {
                            if (txtMethodQty.getVisibility() == View.VISIBLE) {
                                if (txtMethodQty.getText().toString().length() == 0) {
                                    Connection.MessageBox(ELCOFormFPI.this, " পরিমান লিখুন");
                                    return;
                                }

                            }
                        }
                        //Suki
                        if(Global.Left(spnBrand.getSelectedItem().toString(), 2).equalsIgnoreCase("01"))
                        {
                            AdjustStockQty(txtMethodQty.getText().toString(), Global.Left(spnMethod.getSelectedItem().toString(), 2).toString(), g.getProvCode());
                        }
                        //Apon
                        else if(Global.Left(spnBrand.getSelectedItem().toString(), 2).equalsIgnoreCase("02"))
                        {
                            AdjustStockQty(txtMethodQty.getText().toString(), "10", g.getProvCode());
                        }

                        *//*Connection.MessageBox(ELCOForm.this, "তথ্য সফলভাবে সংরক্ষণ করা হয়েছে।");
                        if(g.getCallFrom().equals("1"))
                        {
                            finish();
                            Intent f2 = new Intent(getApplicationContext(),Elcoview.class);
                            startActivity(f2);
                        }
                        else {
                            finish();
                            Intent f2 = new Intent(getApplicationContext(), MemberListFPI.class);
                            startActivity(f2);
                            g.setGeneratedId("");
                        }*//*
                    }
                    else {
                        AdjustStockQty(txtMethodQty.getText().toString(), Global.Left(spnMethod.getSelectedItem().toString(), 2).toString(), g.getProvCode());
                    }
                    if(Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("03"))
                    {
                        this.InsertInjectableToWomanInjectableTable(Global.DateConvertYMD(dtpMD.getText().toString()));
                    }
                    Connection.MessageBox(ELCOFormFPI.this, "তথ্য সফলভাবে সংরক্ষণ করা হয়েছে।");
                    if(g.getCallFrom().equals("1"))
                    {
                        finish();
                        Intent f2 = new Intent(getApplicationContext(),Elcoview.class);
                        startActivity(f2);
                    }
                    else {
                        finish();
                        Intent f2 = new Intent(getApplicationContext(), MemberListFPI.class);
                        startActivity(f2);
                        return;
                        //g.setGeneratedId("");
                    }

                }
                    *//*  else if (Global.DateDifferenceDays(dtpDOV.getText().toString(), Global.DateConvertDMY(GetDOV(g.getGeneratedId()))) <= 0) {

                    Connection.MessageBox(ELCOForm.this, " ভিজিট এর তারিখ পূর্বের ভিজিট তারিখ এর  বেশি  হতে হবে");
                    return;
                }*//*


            }



            if(validateMarriageDateAgainstSystemDate())
            {
                Connection.MessageBox(ELCOFormFPI.this," বিবাহের এর তারিখ আজকের তারিখ এর সমান অথবা কম হতে হবে।");
                return;
            }
            if(validateMethodDateAgainstMarriageDate())
            {
                Connection.MessageBox(ELCOFormFPI.this," ভিজিট এর তারিখ বিবাহের তারিখ এর সমান অথবা বেশি  হতে হবে।");
                return;
            }

            if(secBrand.getVisibility()==View.VISIBLE)
            {
                if(spnBrand.getSelectedItemPosition()==0) {
                    Connection.MessageBox(ELCOFormFPI.this, " ব্র্যান্ড লিখুন");
                    return;
                }
            }

            if(secMethodUnit.getVisibility()==View.VISIBLE) {
                if (txtMethodQty.getVisibility() == View.VISIBLE) {
                    if (txtMethodQty.getText().toString().length() == 0) {
                        Connection.MessageBox(ELCOFormFPI.this, " পরিমান লিখুন");
                        return;
                    }

                }
            }

            if(secWherePlace.getVisibility()==View.VISIBLE)
            {
                if(spnWherePlace.getSelectedItemPosition()==0) {
                    Connection.MessageBox(ELCOFormFPI.this, " কোথায় প্রেরন করা হয়েছে  লিখুন");
                    return;
                }
            }
            if(secSS.getVisibility() == View.VISIBLE) {
                if (rdogrpSS.getVisibility() == View.VISIBLE) {

                    Integer source = rdogrpSS.getCheckedRadioButtonId();
                    if (source == null) {
                        Connection.MessageBox(ELCOFormFPI.this, " উৎস সিলেক্ট করুন।");
                        return;
                    }

                    if (source == -1) {
                        Connection.MessageBox(ELCOFormFPI.this, " উৎস সিলেক্ট করুন।");
                        return;
                    }
                }
            }

            if(secSyringe.getVisibility() == View.VISIBLE) {
                if (txtSyringeQty.getText().toString().length() == 0) {
                    Connection.MessageBox(ELCOFormFPI.this, "সিরিঙ্গের পরিমান লিখুন");
                    txtSyringeQty.requestFocus();
                    return;
                }

            }

            if(dtpMD.getText().toString().length()==0 & dtpMD.isShown()& rdoMethodReceveDT1.isChecked() )
            {
                Connection.MessageBox(ELCOFormFPI.this, "পদ্ধতি গ্রহনের তারিখ লিখুন ।");
                dtpMD.requestFocus();
                return;
            }
            else if(txtMDAge.getText().toString().length()==0 & rdoMethodReceveDT2.isChecked() )
            {
                Connection.MessageBox(ELCOFormFPI.this, "পদ্ধতি গ্রহনের বয়স  কত লিখুন।");
                txtMDAge.requestFocus();
                return;
            }


            if(secValidity.getVisibility() == View.VISIBLE) {
                if (txtValidity.getText().toString().length() == 0) {
                    Connection.MessageBox(ELCOFormFPI.this, " কার্যকারিতা লিখুন");
                    txtValidity.requestFocus();
                    return;
                }
                else if (spnValidityUnit.getSelectedItemPosition()==0) {
                    Connection.MessageBox(ELCOFormFPI.this, " ইউনিট লিখুন");
                    return;
                }
            }


            String CurStatus = Global.Left(spnMethod.getSelectedItem().toString(), 2);

            //Stock

            if(secMethodUnit.getVisibility()==View.VISIBLE) {

                if (txtMethodQty.getVisibility() == View.VISIBLE) {
                    if (txtMethodQty.getText().toString().length() == 0) {
                        Connection.MessageBox(ELCOFormFPI.this, " পরিমান লিখুন");
                        return;
                    }
                    else if (spnMethodUnit.getSelectedItemPosition()==0) {
                        Connection.MessageBox(ELCOFormFPI.this, " ইউনিট লিখুন");
                        return;
                    }
                    else
                    {
                        Integer currentqty = Global.GetCurrentStockOfItem(C, g.getProvCode(), CurStatus);
                        if(CurStatus.equalsIgnoreCase("01"))
                        {
                            if(Integer.parseInt(txtMethodQty.getText().toString())>3)
                            {
                                Connection.MessageBox(ELCOFormFPI.this, "ইস্যুর পরিমান 3টির  থেকে কম হবে। ");
                                return;
                            }
                        }

                    }

                }
            }




            String SQL = "";

            //ELCO Profile
            //------------------------------------------------------------------------------------------------------------------------
            if(!C.Existence("Select healthid from "+ TableName +"  Where healthid='"+ g.getGeneratedId() +"'"))
            {
                SQL = "Insert into "+ TableName +"(healthId,providerId,regDT,systemEntryDate,upload)Values(";
                SQL += "'"+ g.getGeneratedId() +"',";
                SQL += "'"+ g.getProvCode() +"',";
                SQL += "'"+ Global.DateNowYMD() +"',";
                SQL += "'"+ Global.DateTimeNowDMYHMS() +"',";
                SQL += "'2')";

                C.Save(SQL);
            }

            SQL = "Update "+ TableName +" Set ";
            SQL+="hhStatus = '"+ (chkHHNoDontKnow.isChecked()?"1":"2") +"',";
            SQL+="haHHNo = '"+ txtHHNo.getText().toString() +"',";
            SQL+="elcoNo = '"+ txtELCONo.getText().toString() +"',";
            SQL+="husbandName = '"+ txtHusName.getText().toString() +"',";
            SQL+="husbandAge = '"+ txtHusAge.getText().toString() +"',";
            SQL+="DOMSource = '"+ (rdoDOMSource1.isChecked()?"1":"2") +"',";

            if(rdoDOMSource1.isChecked())
            {
                SQL+="MarrDate = '"+ Global.DateConvertYMD(dtpDOM.getText().toString()) +"',";
                SQL+="MarrAge = '"+ Global.DateDifferenceYears(Global.DateNowDMY(), dtpDOM.getText().toString()) +"',";
            }
            else
            {
                SQL+="MarrDate = '"+ Global.addDaysYMD(Global.DateNowDMY(), -Integer.valueOf(txtMAge.getText().toString())*365) +"',";
                SQL+="MarrAge = '"+ txtMAge.getText().toString() +"',";
            }

            SQL+="Son = '"+ txtLiveSon.getText().toString() +"',";
            SQL+="Dau = '"+ txtLiveDau.getText().toString() +"',";
            SQL+="modifyDate = '"+ Global.DateTimeNowDMYHMS() +"'";
            SQL+=" Where HealthID='"+ g.getGeneratedId() +"'";
            C.Save(SQL);

            if(Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("03"))
            {
                this.InsertInjectableToWomanInjectableTable(Global.DateConvertYMD(dtpMD.getText().toString()));
            }

            //ELCO Visit
            //when 12 14 come then handale another way.(Pandding)
            String SQ1 = "";
            SQ1 = "select CurrStatus||'^'||NewOld from ELCOVisit Where healthid='" + g.getGeneratedId() + "'";//and Visit='"+ txtVisitNo.getText().toString() +"'
            SQ1 += " order by systemEntryDate desc limit 1";
            String PerviousStatus = C.ReturnSingleValue(SQ1);
            String[] PerviousMethods = Connection.split(PerviousStatus, '^');
            String PreMethodStatus = PerviousMethods[0].toString();
            // String PerviousMethodSign = PerviousMethods[1].toString();
            *//*
            1.No Method=if Client Gorvoboti Hoy tahola M
            Total Client=10
            Method Neasa 5
            2.No Method=(Total Client - Method Nease) tahola M
            3.New=Goto masa Kono method chilo na but a masa kono poddotir nilo tahola N
            4.New=Goto masa ja poddoti ta chilo a masa jodi client poddoti change kora thahola N
            5.Swith over=Goto masa ja poddoti ta chilo a masa jodi client poddoti change kora thahola C
            6.Old=Goto masa ja poddoti ta chilo a masa o client same poddotita nila  thahola O

            4+5=C Hoba karon aki session ar modda porasa.sa jonno Solution of New formula follow korta hoba.

            Solution of New
            ----------------
            Total New=Numbers of N + Numbers of C

             Solution of switch over
            ------------------------
            Total Swith over=Numbers of C

            *//*
            String CurrentMethod = "";
            if(GetDOV(g.getGeneratedId()).equals("")){

                if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("12")) {
                    CurrentMethod = "M";
                } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("13")) {
                    CurrentMethod = "";
                } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("14")) {
                    CurrentMethod = "";
                } else {
                    if (PreMethodStatus.equalsIgnoreCase("")) {
                        CurrentMethod = "N";
                    } else if (PreMethodStatus.equals(Global.Left(spnMethod.getSelectedItem().toString(), 2))) {
                        CurrentMethod = "O";
                    } else if (!PreMethodStatus.equals(Global.Left(spnMethod.getSelectedItem().toString(), 2))) {
                        CurrentMethod = "C";
                    }
                }
            }
            else  if (Global.DateDifferenceDays(dtpDOV.getText().toString(), Global.DateConvertDMY(GetDOV(g.getGeneratedId()))) <= 0) {
                String VisitNo = txtVisitNo.getText().toString();
                String ExistMethodStatus=C.ReturnSingleValue("Select newOld  from elcovisit Where HealthID='"+ g.getGeneratedId() +"' and Visit='"+ VisitNo +"'");
                CurrentMethod=ExistMethodStatus;
            }
            else if(!GetDOV(g.getGeneratedId()).equals("")){
                if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("12")) {
                    CurrentMethod = "M";
                } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("13")) {
                    CurrentMethod = "";
                } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("14")) {
                    CurrentMethod = "";
                } else {
                    if (PreMethodStatus.equalsIgnoreCase("")) {
                        CurrentMethod = "N";
                    } else if (PreMethodStatus.equals(Global.Left(spnMethod.getSelectedItem().toString(), 2))) {
                        CurrentMethod = "O";
                    } else if (!PreMethodStatus.equals(Global.Left(spnMethod.getSelectedItem().toString(), 2))) {
                        CurrentMethod = "C";
                    }
                }
            }
            String VisitNo = txtVisitNo.getText().toString();
            String sqlnew = "";
            String sqlupdate = "";
            String trnid=Global.GenerateProviderId(g.getProvCode());
            if(!C.Existence("Select healthid from elcoVisit  Where healthid='"+ g.getGeneratedId() +"' and Visit='"+ VisitNo +"' and  currStatus = '"+ Global.Left(spnMethod.getSelectedItem().toString(), 2) +"'"))
            {
                sqlnew = "Insert into elcoVisit(healthId,providerId,visit,systemEntryDate,upload,transactionId)Values(";
                sqlnew += "'"+ g.getGeneratedId() +"',";
                sqlnew += "'"+ g.getProvCode() +"',";
                sqlnew += "'"+ VisitNo +"',";
                sqlnew += "'"+ Global.DateTimeNowYMDHMS() +"',";
                sqlnew += "'2',";
                sqlnew += "'" + trnid + "')";

                //if(!Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("12")  && !Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("13") && !Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("14"))
                if(!Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("12"))
                    C.Save(sqlnew);
                else
                {

                    sqlnew = "Insert into elcoVisit(healthId,providerId,visit,systemEntryDate,upload,pregNo)Values(";
                    sqlnew += "'" + g.getGeneratedId() + "',";
                    sqlnew += "'" + g.getProvCode() + "',";
                    sqlnew += "'" + VisitNo + "',";
                    sqlnew += "'" + Global.DateTimeNowYMDHMS() + "',";
                    sqlnew += "'2',";
                    sqlnew += "'" + PGNNo() + "')";
                    g.setPregNo(PGNNo());
                    //}
                }
            }

            sqlupdate = "Update elcoVisit Set ";
            sqlupdate+="vDate = '"+ Global.DateConvertYMD(dtpDOV.getText().toString()) +"',";
            sqlupdate+="visitStatus = '"+ (rdoVSPresent.isChecked()?"1":"2") +"',";
            if(spnMethod.getSelectedItemPosition()==0)
                sqlupdate+="currStatus = '',";
            else
                sqlupdate+="currStatus = '"+ Global.Left(spnMethod.getSelectedItem().toString(), 2) +"',";
            sqlupdate+="newOld = '"+ CurrentMethod +"',";
            sqlupdate+="mDate = '"+ Global.DateConvertYMD(dtpDOV.getText().toString()) +"',";
            String SS = "";
            if(rdoSSFWA.isChecked())        SS="1";
            if(rdoSSLMarket.isChecked())    SS="2";
            if(rdoSSOther.isChecked())      SS="3";
            if(rdoSSOtherGovt.isChecked())  SS="4";
            sqlupdate+="sSource = '"+ SS +"',";
            sqlupdate+="qty = '"+ txtMethodQty.getText().toString() +"',";
            sqlupdate+="unit = '"+ (spnMethodUnit.getSelectedItemPosition()==0?"":String.valueOf(spnMethodUnit.getSelectedItemPosition())) +"',";
            if(spnBrand.getSelectedItemPosition()==0)
                sqlupdate+="brand = '',";
            else
                sqlupdate+="brand = '"+ Global.Left(spnBrand.getSelectedItem().toString(), 2) +"',";
            //sqlupdate+="brand = '"+ Global.Left(spnBrand.getSelectedItem().toString(), 2) +"',";
            sqlupdate+="referPlace = '"+ (spnWherePlace.getSelectedItemPosition()==0?"":String.valueOf(spnWherePlace.getSelectedItemPosition())) +"',";
            sqlupdate+="validity = '"+ txtValidity.getText().toString() +"',";
            sqlupdate+="dayMonYear = '"+ (spnValidityUnit.getSelectedItemPosition()==0?"":String.valueOf(spnValidityUnit.getSelectedItemPosition())) +"',";
            sqlupdate+="mrSource = '"+ (rdoMethodReceveDT1.isChecked()?"1":"2") +"',";
            if(rdoMethodReceveDT1.isChecked())
            {
                sqlupdate+="MRDate = '"+ Global.DateConvertYMD(dtpMD.getText().toString()) +"',";
                sqlupdate+="MRAge = '"+ Global.DateDifferenceMonth(Global.DateNowDMY(), dtpMD.getText().toString()) +"',";//DateDifferenceYears DateDifferenceMonth
            }
            else
            {
                sqlupdate+="MRDate = '"+ Global.addDaysYMD(Global.DateNowDMY(), -Integer.valueOf(txtMDAge.getText().toString())*30) +"',";
                //sqlupdate+="MRDate = '"+ Global.addDaysYMD(Global.DateConvertDMY(dtpDOV.getText().toString()), -Integer.valueOf(txtMDAge.getText().toString())*30) +"',";
                sqlupdate+="MRAge = '"+ txtMDAge.getText().toString() +"',";
            }
            //sqlupdate+="MRDate = '"+ Global.DateConvertYMD(dtpMD.getText().toString()) +"',";
            sqlupdate+="syrinsQty = '"+ txtSyringeQty.getText().toString() +"'";
            sqlupdate+=" Where HealthID='"+ g.getGeneratedId() +"' and Visit='"+ VisitNo +"'";

            if(!Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("12") && !Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("13"))
                C.Save(sqlupdate);
            else {

                sqlupdate = "Update elcoVisit Set pregNo = '" + PGNNo() + "' , ";
                sqlupdate += "vDate = '" + Global.DateConvertYMD(dtpDOV.getText().toString()) + "',";//dtpDOV
                sqlupdate += "visitStatus = '" + (rdoVSPresent.isChecked() ? "1" : "2") + "',";
                if (spnMethod.getSelectedItemPosition() == 0)
                    sqlupdate += "currStatus = '',";
                else
                    sqlupdate += "currStatus = '" + Global.Left(spnMethod.getSelectedItem().toString(), 2) + "',";
                sqlupdate += "newOld = '" + CurrentMethod + "',";
                sqlupdate += "mDate = '" + Global.DateConvertYMD(dtpDOV.getText().toString()) + "',";
                SS = "";
                if (rdoSSFWA.isChecked()) SS = "1";
                if (rdoSSLMarket.isChecked()) SS = "2";
                if (rdoSSOther.isChecked()) SS = "3";
                if (rdoSSOtherGovt.isChecked()) SS = "4";
                sqlupdate += "sSource = '" + SS + "',";
                sqlupdate += "qty = '" + txtMethodQty.getText().toString() + "',";
                sqlupdate += "unit = '" + (spnMethodUnit.getSelectedItemPosition() == 0 ? "" : String.valueOf(spnMethodUnit.getSelectedItemPosition())) + "',";
                if(spnBrand.getSelectedItemPosition()==0)
                    sqlupdate+="brand = '',";
                else
                    sqlupdate+="brand = '"+ Global.Left(spnBrand.getSelectedItem().toString(), 2) +"',";
                //sqlupdate += "brand = '" + (spnBrand.getSelectedItemPosition() == 0 ? "" : Global.Left(spnBrand.getSelectedItem().toString(), 2)) + "',";
                sqlupdate += "referPlace = '" + (spnWherePlace.getSelectedItemPosition() == 0 ? "" : String.valueOf(spnWherePlace.getSelectedItemPosition())) + "',";
                sqlupdate += "validity = '" + txtValidity.getText().toString() + "',";
                sqlupdate += "dayMonYear = '" + (spnValidityUnit.getSelectedItemPosition() == 0 ? "" : String.valueOf(spnValidityUnit.getSelectedItemPosition())) + "',";
                sqlupdate+="mrSource = '"+ (rdoMethodReceveDT1.isChecked()?"1":"2") +"',";
                if(rdoMethodReceveDT1.isChecked())
                {
                    sqlupdate+="MRDate = '"+ Global.DateConvertYMD(dtpMD.getText().toString()) +"',";
                    sqlupdate+="MRAge = '"+ Global.DateDifferenceYears(Global.DateNowDMY(), dtpMD.getText().toString()) +"',";
                }
                else
                {
                    sqlupdate+="MRDate = '"+ Global.addDaysYMD(Global.DateNowDMY(), -Integer.valueOf(txtMDAge.getText().toString())*365) +"',";
                    sqlupdate+="MRAge = '"+ txtMDAge.getText().toString() +"',";
                }
                sqlupdate+="syrinsQty = '"+ txtSyringeQty.getText().toString() +"',";
                sqlupdate+="modifyDate = '"+  Global.DateTimeNowDMYHMS() +"'";
                sqlupdate += " Where HealthID='" + g.getGeneratedId() + "' and Visit='" + VisitNo + "'";
                g.setPregNo(PGNNo());
                // }
            }

            CurStatus = Global.Left(spnMethod.getSelectedItem().toString(), 2);

            if(secMethodUnit.getVisibility()==View.VISIBLE) {

                if(txtMethodQty.getVisibility()==View.VISIBLE)
                {
                    //Validation is not required here. Validation of current stock is handled above.
                                *//*if (!C.Existence("SELECT * FROM stockTransaction WHERE transactionType ='4' AND providerId ='" + g.getProvCode() + "' AND healthId = '" +
                                        g.getGeneratedId() + "' AND itemCode = '" + CurStatus + "' AND systemEntryDate ='" + Global.DateNowYMD() + "'"))*//*
                    if (!C.Existence("SELECT * FROM stockTransaction WHERE transactionType ='4' AND providerId ='" + g.getProvCode() + "' AND healthId = '" +
                            g.getGeneratedId() + "' AND itemCode = '" + CurStatus + "' AND systemEntryDate ='" + Global.DateConvertYMD(dtpDOV.getText().toString()) + "'")){
                        if (CurStatus.equalsIgnoreCase("01")) {

                            //Suki
                            if(Global.Left(spnBrand.getSelectedItem().toString(), 2).equalsIgnoreCase("01"))
                            {
                                InsertTransaction(CurStatus, g.getProvCode(), txtMethodQty.getText().toString(), trnid);
                                UpdateCurrentStock(g.getProvCode(), Global.Left(spnMethod.getSelectedItem().toString(), 2).toString(), txtMethodQty.getText().toString());
                            }
                            //Apon
                            else if(Global.Left(spnBrand.getSelectedItem().toString(), 2).equalsIgnoreCase("02"))
                            {
                                InsertTransaction("10", g.getProvCode(), txtMethodQty.getText().toString(), trnid);
                                UpdateCurrentStock(g.getProvCode(), "10", txtMethodQty.getText().toString());
                            }
                        }
                        else  if (CurStatus.equalsIgnoreCase("02") | CurStatus.equalsIgnoreCase("08") || CurStatus.equalsIgnoreCase("09")) {
                            InsertTransaction(CurStatus, g.getProvCode(), txtMethodQty.getText().toString(), trnid);
                            UpdateCurrentStock(g.getProvCode(), Global.Left(spnMethod.getSelectedItem().toString(), 2).toString(), txtMethodQty.getText().toString());
                        }
                        else if (CurStatus.equalsIgnoreCase("03")) { //Injectable
                            InsertTransaction(CurStatus, g.getProvCode(), txtMethodQty.getText().toString(), trnid);
                            UpdateCurrentStock(g.getProvCode(), Global.Left(spnMethod.getSelectedItem().toString(), 2).toString(), txtMethodQty.getText().toString());


                            //trnid = Global.GenerateProviderId(g.getProvCode()) ;

                            trnid = String.valueOf(Long.parseLong(Global.GenerateProviderId(g.getProvCode()))+1) ;
                            //String str1 = Global.GenerateProviderIdForInjectable1(g.getProvCode());
                            //String str2 = Global.GenerateProviderIdForInjectable2(g.getProvCode());
                            //trnid = Global.GenerateProviderIdForInjectable1(g.getProvCode())  + String.valueOf(Long.parseLong(Global.GenerateProviderIdForInjectable2(g.getProvCode()))+1);
                            InsertTransaction("5", g.getProvCode(), txtMethodQty.getText().toString(), trnid);
                            UpdateCurrentStock(g.getProvCode(), "5", txtMethodQty.getText().toString());

                            //this.InsertInjectableToWomanInjectableTable(dtpDOV.getText().toString());
                        }

                    }
                }
            }
            ElcoVisitStsatus();
            rdogrpVS.clearCheck();
            spnMethod.setSelection(0);
            rdogrpSS.clearCheck();
            spnBrand.setSelection(0);
            txtMethodQty.setText("");
            spnMethodUnit.setSelection(0);
            spnWherePlace.setSelection(0);
            txtValidity.setText("");
            spnValidityUnit.setSelection(0);
            secMethod.setVisibility(View.VISIBLE);
            spnMethod.setSelection(0);
            txtMethodQty.setText("");
            spnMethodUnit.setSelection(0);
            Connection.MessageBox(ELCOFormFPI.this, "তথ্য সফলভাবে সংরক্ষণ করা হয়েছে।");
            //pregnant
            if(CurStatus.equalsIgnoreCase("12"))
            {
                Intent f2 = new Intent(getApplicationContext(),FWAReg.class);
                //add nisan
                g.setAMElco(txtELCONo.getText().toString());
                g.setAAge(txtAge.getText().toString());
                Bundle IDbundle = new Bundle();

                if(sqlnew.length()>0)
                    IDbundle.putString("sqlnew",sqlnew);
                if (sqlupdate.length()>0)
                    IDbundle.putString("sqlupdate",sqlupdate);

                f2.putExtras(IDbundle);

                startActivity(f2);
                finish();
            }

            else
            {
                if(g.getCallFrom().equals("1"))
                {
                    finish();
                    Intent f2 = new Intent(getApplicationContext(),Elcoview.class);
                    startActivity(f2);
                }
                else {
                    finish();
                    Intent f2 = new Intent(getApplicationContext(), MemberListFPI.class);
                    startActivity(f2);
                    //g.setGeneratedId("");
                }
            }

        }
        catch(Exception  e)
        {
            Connection.MessageBox(ELCOFormFPI.this, e.getMessage());
            return;
        }
    }*/


    private Integer BrandSelect(String U) {
        Integer i = 0;
        //Cursor cur = C.ReadData("Select DivCode,DCode,Upz,UN from Section1 where idno='" + txtIdNo.getText().toString().trim() + "'");
        Cursor cur = C.ReadData("Select brandCode as brandCode from BrandMethod where idno ='" + Global.Left(spnBrand.getSelectedItem().toString(), 2) + "'");
        cur.moveToFirst();

        while (!cur.isAfterLast()) {


            if (U.equals("brandCode")) {
                i = Global.SpinnerItemPosition(spnBrand, 2, cur.getString(cur.getColumnIndex("brandCode")));
            }

            cur.moveToNext();
        }
        cur.close();
        return i;
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

    private String LastPGNNo() {
        String SQL = "";
        String PGNNo = "";

        SQL = "Select (ifnull(max(cast(PregNo as int)),0))MaxPGNNo from PregWomen WHERE healthId = " + g.getGeneratedId();
        /*SQL += " where";
        SQL += " dist='" + g.getDistrict() + "' and upz='" + g.getUpazila() + "' and un='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and vill='" + g.getVillage() + "'  and ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"' and  HHNo='" + g.getHouseholdNo() + "' and SNo='" + txtSNo.getText() + "'";*/
        PGNNo = Global.Right(("00" + C.ReturnSingleValue(SQL)), 2);
        return PGNNo;
    }

    private void InsertTransaction(String ItemCode, String ProviderCode, String Qty, String trnid) {
        /*if(!C.Existence("SELECT * FROM stockTransaction WHERE transactionType ='4' AND providerId ='" + g.getProvCode() + "' AND healthId = '" +
                g.getGeneratedId() +"' AND itemCode = '" + ItemCode +"' AND systemEntryDate ='" + Global.DateNowYMD() +"'"))*/
        if (!C.Existence("SELECT * FROM stockTransaction WHERE transactionType ='4' AND providerId ='" + g.getProvCode() + "' AND healthId = '" +
                g.getGeneratedId() + "' AND itemCode = '" + ItemCode + "' AND systemEntryDate ='" + Global.DateConvertYMD(dtpDOV.getText().toString()) + "'"))//transactionId ='" + trnid +"'"))
        {
            String Sql = "Insert Into stockTransaction(transactionId,transactionDateTime,providerId,transactionType,healthId,itemCode,receiveQty,issueQty,systemEntryDate)" +
                    " VALUES('" + trnid + "','" + Global.DateTimeNowYMDHMS() + "','" + g.getProvCode() + "','" + "4'" + ",'" + g.getGeneratedId() + "', '" + ItemCode + "','" + "0" + "', '"
                    + Qty + "','" + Global.DateConvertYMD(dtpDOV.getText().toString()) + "')";
            /*String Sql = "Insert Into stockTransaction(transactionId,transactionDateTime,providerId,transactionType,healthId,itemCode,receiveQty,issueQty,systemEntryDate)" +
                    " VALUES('"+trnid + "','" +Global.DateTimeNowYMDHMS()+"','"+ g.getProvCode() +"','"+ "4'" + ",'" + g.getGeneratedId() +"', '"+ ItemCode +"','"+ "0" + "', '"
                    + Qty +"','" + Global.DateNowYMD() + "')";*/
            C.Save(Sql);
        } else {
            String Sql = "Update stockTransaction Set issueQty= '" + Qty + "', modifyDate = '" + Global.DateTimeNowYMDHMS() + "' WHERE providerId = '" + g.getProvCode() + "' AND itemCode = '" + ItemCode +
                    "' AND healthId = '" + g.getGeneratedId() + "' AND transactionType = '" + "4' AND systemEntryDate =' " + Global.DateConvertYMD(dtpDOV.getText().toString()) + "'";
            /*String Sql = "Update stockTransaction Set issueQty= '" + Qty +"', modifyDate = '" + Global.DateTimeNowYMDHMS()+ "' WHERE providerId = '" + g.getProvCode() + "' AND itemCode = '" + ItemCode +
                    "' AND healthId = '" + g.getGeneratedId() +"' AND transactionType = '" + "4' AND systemEntryDate =' " + Global.DateNowYMD() +"'";*/
            C.Save(Sql);
        }

    }

    private void UpdateCurrentStock(String providerId, String ItemCode, String Qty) {
        Integer q = Global.GetCurrentStockOfItem(C, g.getProvCode(), ItemCode);
        Integer currentStock = q - Integer.parseInt(Qty);
        String Sql = "Update currentStock Set stockQty= '" + String.valueOf(currentStock) + "', modifyDate = '" + Global.DateTimeNowYMDHMS() + "' WHERE providerId = '" + g.getProvCode() + "' AND itemCode = '" + ItemCode + "'";
        C.Save(Sql);
    }

    private void AdjustStockQty(String CurrentIssueQty, String ItemCode, String providerId) {
        String VisitNo = txtVisitNo.getText().toString();
        String Trid = C.ReturnSingleValue("Select transactionId  from elcovisit Where HealthID='" + g.getGeneratedId() + "' and Visit='" + VisitNo + "'");
        String IssueQty = C.ReturnSingleValue("Select qty from elcovisit Where HealthID='" + g.getGeneratedId() + "' and Visit='" + VisitNo + "'");
        if (!IssueQty.equalsIgnoreCase("")) {
            String CurrentQty = C.ReturnSingleValue("SELECT stockQty FROM currentStock WHERE  providerId = '" + g.getProvCode() + "' AND itemCode = '" + ItemCode + "'");
            Integer CurrentQty1 = Integer.parseInt(CurrentQty);
            Integer UpdatedStock = CurrentQty1 + Integer.parseInt(IssueQty);
            String Sql = "Update currentStock Set stockQty= '" + UpdatedStock + "', modifyDate = '" + Global.DateTimeNowYMDHMS() + "' WHERE providerId = '" + g.getProvCode() + "' AND itemCode = '" + ItemCode + "'";
            C.Save(Sql);

            Sql = "Update stockTransaction Set issueQty= '" + CurrentIssueQty + "', modifyDate = '" + Global.DateTimeNowYMDHMS() + "' WHERE  transactionId='" + Trid + "'";
            C.Save(Sql);

            Sql = "Update elcovisit Set qty= '" + CurrentIssueQty + "', modifyDate = '" + Global.DateTimeNowYMDHMS() + "' WHERE  transactionId='" + Trid + "'";
            C.Save(Sql);

            // Needs this method
            UpdateCurrentStock(providerId, ItemCode, CurrentIssueQty);

        }
    }

    private void InsertInjectableToWomanInjectableTable(String doseDate) {
        //Global.DateConvertYMD(doseDate)
        if (!C.Existence("SELECT * FROM womanInjectable WHERE doseDate ='" + doseDate + "' AND providerId ='" + g.getProvCode() + "' AND healthId = '" + g.getGeneratedId() + "'")) {
            String sql = "INSERT INTO womanInjectable (healthId,providerId,doseId,doseDate,sideEffect,systemEntryDate,upload)";
            sql = sql + "VALUES ('" + g.getGeneratedId() + "', '" + g.getProvCode() + "','" + GetInjectableDoseNumber() + "','" +
                    doseDate + "','2','" + Global.DateConvertYMD(dtpDOV.getText().toString()) + "','2')";

            C.Save(sql);
        }
        /*else
        {
            String Sql = "Update womanInjectable Set doseDate= '" + doseDate +"', sideEffect = '2',modifyDate='" + Global.DateTimeNowYMDHMS() + "', upload='2' WHERE healthId = '" + g.getHealthID() + "' AND providerId = '" + g.getProvCode() +
                    "' AND doseId = '" + GetCurrentInjectableDoseNumber() +"'";
            C.Save(Sql);
        }*/


    }

    private String GetInjectableDoseNumber() {
        String SQL = "";

        SQL = "select ifnull(Count(*),'')+1 from womanInjectable";
        SQL += " where healthId='" + g.getGeneratedId() + "' and providerId='" + g.getProvCode() + "'";
        String SNo = C.ReturnSingleValue(SQL);
        // Serial No auto increment
        return SNo;
    }

    private String GetCurrentInjectableDoseNumber() {
        String SQL = "";

        SQL = "select ifnull(Count(*),'') from womanInjectable";
        SQL += " where healthId='" + g.getGeneratedId() + "' and providerId='" + g.getProvCode() + "'";
        String SNo = C.ReturnSingleValue(SQL);
        // Serial No auto increment
        return SNo;
    }


    //ELCO Immunization
    private void ImmunizationSave(String ImuIDNo) {
        //Immunization
        String SQL = "";
        if (!C.Existence("Select healthid from immunizationHistory  Where healthid='" + g.getGeneratedId() + "' and imuCode='" + ImuIDNo + "'")) {
            SQL = "Insert into immunizationHistory(healthId,providerId,imuCode,imuDate,imuCard,imuSource,systemEntryDate,modifyDate)Values(";
            SQL += "'" + g.getGeneratedId() + "',";
            SQL += "'" + g.getProvCode() + "',";
            SQL += "'" + ImuIDNo + "',";
            SQL += "'" + (dtpDOTT1.getText().toString().length() == 0 ? "" : Global.DateConvertYMD(dtpDOTT1.getText().toString())) + "',";
            if (ChkTT1.isChecked() & rdoCardYes.isChecked())
                SQL += "'1',";
            else if (ChkTT1.isChecked() & rdoCardNo.isChecked())
                SQL += "'2',";
            else
                SQL += "'',";

            SQL += "'',";
            SQL += "'" + Global.DateTimeNowDMYHMS() + "',";
            SQL += "'" + Global.DateTimeNowDMYHMS() + "')";
        } else {
            SQL = "Update immunizationHistory set ";
            SQL += "imuDate='" + (dtpDOTT1.getText().toString().length() == 0 ? "" : Global.DateConvertYMD(dtpDOTT1.getText().toString())) + "',";
            if (ChkTT1.isChecked() & rdoCardYes.isChecked())
                SQL += "imuCard='1'";
            else if (ChkTT1.isChecked() & rdoCardNo.isChecked())
                SQL += "imuCard='2'";
            else
                SQL += "imuCard=''";
            SQL += " where healthid='" + g.getGeneratedId() + "' and imuCode='" + ImuIDNo + "'";
        }
        C.Save(SQL);
    }

    private void DataSearch(String healthId) {
        try {
            String SQL = "";
            //01 jun 2016
            SQL = "SELECT ifnull(E.DOB,'') AS DOB , ifnull(E.Sex,'') AS Sex,  ifnull(E.SNO,'') AS SNo, ifnull(E.HHno,'') as HHNo, ifnull(E.HealthID,'') as HealthID, ifnull(E.NameEng,'') as NameEng ,(Cast(((julianday(date('now'))-julianday(E.DOB))/30.4) as int)/12) AS Age,";
            SQL += " ifnull((case when h.NameEng is null then ifnull(el.husbandName,'') else h.NameEng end),'')HusName,";
            SQL += " ifnull((case when (Cast(((julianday(date('now'))-julianday(h.DOB))/30.4) as int)/12) is null then ifnull(el.husbandAge,'') else (Cast(((julianday(date('now'))-julianday(h.DOB))/30.4) as int)/12) end),'')HusAge,";
            SQL += " ifnull(E.EDU,'') AS EDU, ifnull(E.HaveNID,'') AS HaveNID, ifnull(E.NID,'') as NID, ifnull(E.NIDStatus,'') AS NIDStatus, ifnull(E.HaveBR,'') AS HaveBR,ifnull(E.BRID,'') AS BRID, ifnull(E.BRIDStatus,'') AS BRIDStatus, ifnull(E.MobileNo1,'') AS MobileNo1, ifnull(E.MobileNo2,'') as MobileNo2";
            SQL += " FROM member E";
            SQL += " left outer join Member h on e.dist=h.dist and e.upz=h.upz and e.un=h.un and";
            SQL += " e.mouza=h.mouza and e.vill=h.vill and e.hhno=h.hhno and e.spno1=h.sno";
            SQL += " left outer join clientMap cm on e.healthid=cm.healthid";
            SQL += " left outer join ELCO el on el.healthid=cm.generatedid";
            SQL += " WHERE E.healthId ='" + healthId + "'";

            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                txtSNo.setText(cur.getString(cur.getColumnIndex("SNo")));
                DOB.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("DOB"))));
                txtName.setText(cur.getString(cur.getColumnIndex("NameEng")));
                txtAge.setText(cur.getString(cur.getColumnIndex("Age")));
                txtHusName.setText(cur.getString(cur.getColumnIndex("HusName")));
                txtHusAge.setText(cur.getString(cur.getColumnIndex("HusAge")));
                Sex.setText(cur.getString(cur.getColumnIndex("Sex")));
                txtPHHNo.setText(cur.getString(cur.getColumnIndex("HHNo")));
                if (cur.getString(cur.getColumnIndex("Sex")).equalsIgnoreCase("1")) {
                    VlblName.setText("স্বামীর নাম");
                    VlblHus.setText("স্ত্রীর নাম");
                } else {
                    VlblHus.setText("স্বামীর নাম");
                    VlblName.setText("স্ত্রীর নাম");
                }
                if (cur.getString(cur.getColumnIndex("EDU")).equals("00")) {
                    txtMEdu.setText("নার্সারি/কিন্ডার গার্ডেন");
                } else if (cur.getString(cur.getColumnIndex("EDU")).equals("01")) {
                    txtMEdu.setText("১ম শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("EDU")).equals("02")) {
                    txtMEdu.setText("২য় শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("EDU")).equals("03")) {
                    txtMEdu.setText("৩য় শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("EDU")).equals("04")) {
                    txtMEdu.setText("৪র্থ শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("EDU")).equals("05")) {
                    txtMEdu.setText("৫ম শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("EDU")).equals("06")) {
                    txtMEdu.setText("৬ষ্ঠ শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("EDU")).equals("07")) {
                    txtMEdu.setText("৭ম শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("EDU")).equals("08")) {
                    txtMEdu.setText("৮ম শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("EDU")).equals("09")) {
                    txtMEdu.setText("৯ম শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("EDU")).equals("10")) {
                    txtMEdu.setText("১০ম শ্রেনী বা সমতুল্য");
                } else if (cur.getString(cur.getColumnIndex("EDU")).equals("11")) {
                    txtMEdu.setText("উচ্চ মাধ্যমিক বা সমতুল্য");
                } else if (cur.getString(cur.getColumnIndex("EDU")).equals("12")) {
                    txtMEdu.setText("স্মাতক বা সমতুল্য");
                } else if (cur.getString(cur.getColumnIndex("EDU")).equals("13")) {
                    txtMEdu.setText("স্মাতকোত্তর বা সমতুল্য");
                } else if (cur.getString(cur.getColumnIndex("EDU")).equals("14")) {
                    txtMEdu.setText("ডাক্তারি");
                } else if (cur.getString(cur.getColumnIndex("EDU")).equals("15")) {
                    txtMEdu.setText("ইঞ্জিনিয়ারিং");
                } else if (cur.getString(cur.getColumnIndex("EDU")).equals("16")) {
                    txtMEdu.setText("বৃত্তিমুলক শিক্ষা");
                } else if (cur.getString(cur.getColumnIndex("EDU")).equals("17")) {
                    txtMEdu.setText("কারিগরি শিক্ষা");
                } else if (cur.getString(cur.getColumnIndex("EDU")).equals("18")) {
                    txtMEdu.setText("ধাত্রীবিদ্যা/নার্সিং");
                } else if (cur.getString(cur.getColumnIndex("EDU")).equals("12")) {
                    txtMEdu.setText("অন্যান্য");
                } else if (cur.getString(cur.getColumnIndex("EDU")).equals("77")) {
                    txtMEdu.setText("প্রযোজ্য নয়");
                } else if (cur.getString(cur.getColumnIndex("EDU")).equals("99")) {
                    txtMEdu.setText("শিক্ষাগত যোগ্যতা নেই");
                }

                if (cur.getString(cur.getColumnIndex("HaveNID")).equals("1")) {
                    txtMNID.setText(cur.getString(cur.getColumnIndex("NID")));
                } else {
                    if (cur.getString(cur.getColumnIndex("NIDStatus")).equals("1")) {
                        txtMNID.setText("কখনো ছিল না");
                    } else if (cur.getString(cur.getColumnIndex("NIDStatus")).equals("2")) {
                        txtMNID.setText("হারিয়ে ফেলেছি");
                    } else if (cur.getString(cur.getColumnIndex("NIDStatus")).equals("3")) {
                        txtMNID.setText("খুঁজে পাচ্ছি না");
                    } else if (cur.getString(cur.getColumnIndex("NIDStatus")).equals("4")) {
                        txtMNID.setText("অন্য জায়গায় আছে");
                    } else if (cur.getString(cur.getColumnIndex("NIDStatus")).equals("7")) {
                        txtMNID.setText("নাগরিক নয়");
                    }
                }

                if (cur.getString(cur.getColumnIndex("HaveBR")).equals("1")) {
                    txtMBRID.setText(cur.getString(cur.getColumnIndex("BRID")));
                } else {
                    if (cur.getString(cur.getColumnIndex("BRIDStatus")).equals("1")) {
                        txtMBRID.setText("কখনো ছিল না");
                    } else if (cur.getString(cur.getColumnIndex("BRIDStatus")).equals("2")) {
                        txtMBRID.setText("হারিয়ে ফেলেছি");
                    } else if (cur.getString(cur.getColumnIndex("BRIDStatus")).equals("3")) {
                        txtMBRID.setText("খুজে পাচ্ছি না");
                    } else if (cur.getString(cur.getColumnIndex("BRIDStatus")).equals("4")) {
                        txtMBRID.setText("অন্য জায়গায় আছে");
                    } else if (cur.getString(cur.getColumnIndex("BRIDStatus")).equals("7")) {
                        txtMBRID.setText("নাগরিক নয়");
                    }
                }

                if (cur.getString(cur.getColumnIndex("MobileNo1")).equals("")) {
                    txtMMNo.setText(cur.getString(cur.getColumnIndex("MobileNo2")).replace("null", ""));
                } else if (cur.getString(cur.getColumnIndex("MobileNo2")).equals("")) {
                    txtMMNo.setText(cur.getString(cur.getColumnIndex("MobileNo1")).replace("null", ""));
                } else if (!cur.getString(cur.getColumnIndex("MobileNo1")).equals("")) {
                    txtMMNo.setText(cur.getString(cur.getColumnIndex("MobileNo1")).replace("null", ""));
                } else if (!cur.getString(cur.getColumnIndex("MobileNo2")).equals("")) {
                    txtMMNo.setText(cur.getString(cur.getColumnIndex("MobileNo1")).replace("null", ""));
                }
                cur.moveToNext();
            }
            cur.close();

        } catch (Exception e) {
            Connection.MessageBox(ELCOFormFPI.this, e.getMessage());
            return;
        }
    }

    private void ELCOMemSearch(String HealthID) {
        try {
            String SQL = "";
            SQL = "Select healthId,providerId,hhStatus as HHStatus,haHHNo as HAHHNo,elcoNo as ELCONo,husbandName as husbandName,domSource as DOMSource,marrDate as MarrDate,marrAge as MarrAge,son as Son,dau as Dau,regDT";
            SQL += " from ELCO Where healthId='" + HealthID + "'";
            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                if (cur.getString(cur.getColumnIndex("HHStatus")).equals("1")) {
                    chkHHNoDontKnow.setChecked(true);
                } else if (cur.getString(cur.getColumnIndex("HHStatus")).equals("2")) {
                    chkHHNoDontKnow.setChecked(false);
                    txtHHNo.setText(cur.getString(cur.getColumnIndex("HAHHNo")));
                }

                txtELCONo.setText(cur.getString(cur.getColumnIndex("ELCONo")));
                txtLiveSon.setText(cur.getString(cur.getColumnIndex("Son")));
                txtLiveDau.setText(cur.getString(cur.getColumnIndex("Dau")));
                dtpDOM.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("MarrDate"))));
                txtMAge.setText(cur.getString(cur.getColumnIndex("MarrAge")));
                if (cur.getString(cur.getColumnIndex("DOMSource")).equals("1")) {
                    rdoDOMSource1.setChecked(true);
                    tvMAge1.setVisibility(View.GONE);
                    tvMAge.setVisibility(View.VISIBLE);

                } else {
                    tvMAge.setVisibility(View.GONE);
                    rdoDOMSource2.setChecked(true);
                    tvMAge1.setVisibility(View.VISIBLE);

                }
                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(ELCOFormFPI.this, e.getMessage());
            return;
        }
    }

    public class ElcoVisit extends BaseAdapter {
        private Context mContext;
        String[][] vcode;
        String MouzaCode;
        Integer totalRec;
        String PGNNo = "";

        public ElcoVisit(Context c) {
            mContext = c;
        }

        public int getCount() {
            return Integer.parseInt(C.ReturnSingleValue("Select count(*)Total from elcoVisit where healthid='" + g.getGeneratedId() + "' order by vdate desc limit 10"));
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
                MyView = li.inflate(R.layout.elcovisit_item, null);

                //Previous Code
                String SQL = "Select currStatus as currstatus,vdate as vdate, visit as visit from elcoVisit where healthid='" + g.getGeneratedId() + "' order by date(vdate) asc";//limit 10

                try {
                    Cursor cur = C.ReadData(SQL);
                    cur.moveToFirst();

                    totalRec = cur.getCount();
                    vcode = new String[4][totalRec];
                    int i = 0;
                    String M = "";
                    while (!cur.isAfterLast()) {

                        vcode[0][i] = cur.getString(cur.getColumnIndex("currstatus"));
                        vcode[1][i] = cur.getString(cur.getColumnIndex("vdate"));

                        M = cur.getString(cur.getColumnIndex("currstatus"));

                        if (M.equals("01")) vcode[2][i] = "খাবার বড়ি";
                        else if (M.equals("02")) vcode[2][i] = "কনডম";
                        else if (M.equals("03")) vcode[2][i] = "ইনজেকটেবল";
                        else if (M.equals("04")) vcode[2][i] = "আই ইউ ডি";
                        else if (M.equals("05")) vcode[2][i] = "ইমপ্যান্ট";
                        else if (M.equals("06")) vcode[2][i] = "স্থায়ী পদ্ধতি(পুরুষ)";
                        else if (M.equals("07")) vcode[2][i] = "স্থায়ী পদ্ধতি(মহিলা)";
                        else if (M.equals("08")) vcode[2][i] = "ইসিপি ";
                        else if (M.equals("09")) vcode[2][i] = "মিসোপ্রোস্টোল";
                        else if (M.equals("10")) vcode[2][i] = "পার্শ্ব-প্রতিক্রিয়ার জন্য প্রেরন ";
                        else if (M.equals("11")) vcode[2][i] = "পদ্ধতির জন্য প্রেরন";
                        else if (M.equals("12")) vcode[2][i] = "গর্ভবতী ";
                        else if (M.equals("13")) vcode[2][i] = "জীবিত জন্ম ";
                        else if (M.equals("14")) vcode[2][i] = "গর্ভ খালাস (জীবিত জন্ম ছাড়া)";
                        else if (M.equals("15")) vcode[2][i] = "জরায়ু অপারেশন করে অপসারন";
                        else if (M.equals("16")) vcode[2][i] = "স্বামী বিদেশ থাকলে ";
                        else if (M.equals("17")) vcode[2][i] = "বন্ধ্যাত্ব বিষয়ক তথ্য ";
                        else if (M.equals("18")) vcode[2][i] = "অন্য যে কোন অবস্থা ";
                           /* else if(M.equals("19")) vcode[2][i]= "এ এন সি সার্ভিস ";*/
                        else vcode[2][i] = "অনুপস্থিত";

                        vcode[3][i] = cur.getString(cur.getColumnIndex("visit"));


                        i += 1;
                        cur.moveToNext();
                    }
                    cur.close();

                    Button tv = (Button) MyView.findViewById(R.id.image_name);
                    tv.setTextSize(14);
                    tv.setText(vcode[2][position] + "\n" + Global.DateConvertDMY(vcode[1][position]));
                    final Integer p = position;

                    tv.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            spnMethod.setEnabled(false);
                            FPIDataSearchElcoVisit(g.getGeneratedId(), vcode[3][position]);
                            Cursor cur = C.ReadData("Select  visit as visit,vdate as vdate,visitstatus as visitstatus,currstatus as currstatus,ssource as ssource,qty as qty,unit as unit,brand as brand,validity as validity,daymonyear as dmy, ifnull(referPlace,'') as referplace,mrSource, MRDate,MRAge,syrinsQty, ifnull(pregNo,'') AS PGNNo from ElcoVisit where healthid='" + g.getGeneratedId() + "' and visit='" + vcode[3][position] + "'");
                            cur.moveToFirst();

                            while (!cur.isAfterLast()) {

                                rdogrpVS.clearCheck();
                                //spnMethod.setSelection(0);
                                rdogrpSS.clearCheck();
                                txtVisitNo.setText(cur.getString(cur.getColumnIndex("visit")));
                                if (cur.getString(cur.getColumnIndex("visitstatus")).equals("1")) {
                                    rdoVSPresent.setChecked(true);
                                } else if (cur.getString(cur.getColumnIndex("visitstatus")).equals("2")) {
                                    rdoVSAbsent.setChecked(true);
                                }
                                //spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select EvCode||'-'||EVName as EvCode from ElcoEvent where evcode  order by EvCode asc"));
                                //spnMethod.setSelection(Global.SpinnerItemPosition(spnMethod, 2, cur.getString(cur.getColumnIndex("currstatus"))));
                                Integer spnMethods = Integer.parseInt(cur.getString(cur.getColumnIndex("currstatus")));
                                if (spnMethods == 1) {
                                    rdoSSFWA.setVisibility(View.VISIBLE);
                                    spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2) in('01')  order by EvCode asc"));
                                    spnMethod.setSelection(Global.SpinnerItemPosition(spnMethod, 2, cur.getString(cur.getColumnIndex("currstatus"))));
                                    if (cur.getString(cur.getColumnIndex("ssource")).equals("1")) {
                                        if (cur.getString(cur.getColumnIndex("brand")).equals("1")) {
                                            //BrandSelect("brandCode");
                                            spnBrand.setAdapter(C.getArrayAdapter("Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('01') order by brandCode asc"));
                                            spnBrand.setSelection(Global.SpinnerItemPosition(spnBrand, 2, cur.getString(cur.getColumnIndex("brand"))));
                                        } else if (cur.getString(cur.getColumnIndex("brand")).equals("2")) {
                                            spnBrand.setAdapter(C.getArrayAdapter("Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('02') order by brandCode asc"));
                                            spnBrand.setSelection(Global.SpinnerItemPosition(spnBrand, 1, cur.getString(cur.getColumnIndex("brand"))));
                                            //spnBrand.setSelection(Global.SpinnerItemPosition(spnBrand, 2, C.ReturnSingleValue("Select  ifnull(brand,'') as brand from ElcoVisit where healthid='"+ g.getGeneratedId() +"' and visit='"+ txtVisitNo.getText() +"'")));
                                        }
                                    } else if (cur.getString(cur.getColumnIndex("ssource")).equals("2")) {
                                        spnBrand.setAdapter(C.getArrayAdapter("Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('77') order by brandCode asc"));
                                        spnBrand.setSelection(Global.SpinnerItemPosition(spnBrand, 1, cur.getString(cur.getColumnIndex("brand"))));
                                    } else if (cur.getString(cur.getColumnIndex("ssource")).equals("3")) {
                                        if (cur.getString(cur.getColumnIndex("brand")).equals("1")) {
                                            spnBrand.setAdapter(C.getArrayAdapter("Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('01') order by brandCode asc"));
                                            spnBrand.setSelection(Global.SpinnerItemPosition(spnBrand, 1, cur.getString(cur.getColumnIndex("brand"))));
                                        } else if (cur.getString(cur.getColumnIndex("brand")).equals("2")) {
                                            spnBrand.setAdapter(C.getArrayAdapter("Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('02') order by brandCode asc"));
                                            spnBrand.setSelection(Global.SpinnerItemPosition(spnBrand, 1, cur.getString(cur.getColumnIndex("brand"))));
                                        } else if (cur.getString(cur.getColumnIndex("brand")).equals("77")) {
                                            spnBrand.setAdapter(C.getArrayAdapter("Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('77') order by brandCode asc"));
                                            spnBrand.setSelection(Global.SpinnerItemPosition(spnBrand, 1, cur.getString(cur.getColumnIndex("brand"))));
                                        }
                                    } else if (cur.getString(cur.getColumnIndex("ssource")).equals("4")) {
                                        if (cur.getString(cur.getColumnIndex("brand")).equals("1")) {
                                            spnBrand.setAdapter(C.getArrayAdapter("Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('01') order by brandCode asc"));
                                            spnBrand.setSelection(Global.SpinnerItemPosition(spnBrand, 1, cur.getString(cur.getColumnIndex("brand"))));
                                        } else if (cur.getString(cur.getColumnIndex("brand")).equals("2")) {
                                            spnBrand.setAdapter(C.getArrayAdapter("Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('02') order by brandCode asc"));
                                            spnBrand.setSelection(Global.SpinnerItemPosition(spnBrand, 1, cur.getString(cur.getColumnIndex("brand"))));
                                        }
                                    }

                                } else if (spnMethods == 2) {
                                    rdoSSFWA.setVisibility(View.VISIBLE);
                                    spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2) in('02')  order by EvCode asc"));
                                    spnMethod.setSelection(Global.SpinnerItemPosition(spnMethod, 2, cur.getString(cur.getColumnIndex("currstatus"))));

                                    if (cur.getString(cur.getColumnIndex("ssource")).equals("1")) {
                                        if (cur.getString(cur.getColumnIndex("brand")).equals("3")) {
                                            spnBrand.setAdapter(C.getArrayAdapter("Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('03') order by brandCode asc"));
                                            spnBrand.setSelection(Global.SpinnerItemPosition(spnBrand, 1, cur.getString(cur.getColumnIndex("brand"))));
                                        }

                                    } else if (cur.getString(cur.getColumnIndex("ssource")).equals("2")) {
                                        spnBrand.setAdapter(C.getArrayAdapter("Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('77') order by brandCode asc"));
                                        spnBrand.setSelection(Global.SpinnerItemPosition(spnBrand, 1, cur.getString(cur.getColumnIndex("brand"))));
                                    } else if (cur.getString(cur.getColumnIndex("ssource")).equals("3")) {
                                        if (cur.getString(cur.getColumnIndex("brand")).equals("3")) {
                                            spnBrand.setAdapter(C.getArrayAdapter("Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('03') order by brandCode asc"));
                                            spnBrand.setSelection(Global.SpinnerItemPosition(spnBrand, 1, cur.getString(cur.getColumnIndex("brand"))));
                                        } else if (cur.getString(cur.getColumnIndex("brand")).equals("77")) {
                                            spnBrand.setAdapter(C.getArrayAdapter("Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('77') order by brandCode asc"));
                                            spnBrand.setSelection(Global.SpinnerItemPosition(spnBrand, 1, cur.getString(cur.getColumnIndex("brand"))));
                                        }

                                    } else if (cur.getString(cur.getColumnIndex("ssource")).equals("4")) {
                                        if (cur.getString(cur.getColumnIndex("brand")).equals("3")) {
                                            spnBrand.setAdapter(C.getArrayAdapter("Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('03') order by brandCode asc"));
                                            spnBrand.setSelection(Global.SpinnerItemPosition(spnBrand, 1, cur.getString(cur.getColumnIndex("brand"))));
                                        }

                                    }
                                } else if (spnMethods == 3) {
                                    rdoSSFWA.setVisibility(View.VISIBLE);
                                    spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2) in('03')  order by EvCode asc"));
                                    spnMethod.setSelection(Global.SpinnerItemPosition(spnMethod, 2, cur.getString(cur.getColumnIndex("currstatus"))));
                                } else if (spnMethods == 4) {
                                    rdoSSFWA.setVisibility(View.GONE);
                                    rdoSSFWA.setChecked(false);
                                    spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2) in('04')  order by EvCode asc"));
                                    spnMethod.setSelection(Global.SpinnerItemPosition(spnMethod, 2, cur.getString(cur.getColumnIndex("currstatus"))));
                                } else if (spnMethods == 5) {
                                    rdoSSFWA.setVisibility(View.GONE);
                                    rdoSSFWA.setChecked(false);
                                    spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2) in('05')  order by EvCode asc"));
                                    spnMethod.setSelection(Global.SpinnerItemPosition(spnMethod, 2, cur.getString(cur.getColumnIndex("currstatus"))));
                                    if (cur.getString(cur.getColumnIndex("ssource")).equals("2")) {
                                        spnBrand.setAdapter(C.getArrayAdapter("Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('77') order by brandCode asc"));
                                        spnBrand.setSelection(Global.SpinnerItemPosition(spnMethod, 1, cur.getString(cur.getColumnIndex("brand"))));
                                    } else if (cur.getString(cur.getColumnIndex("ssource")).equals("3")) {
                                        if (cur.getString(cur.getColumnIndex("brand")).equals("4")) {
                                            spnBrand.setAdapter(C.getArrayAdapter("Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('04') order by brandCode asc"));
                                            spnBrand.setSelection(Global.SpinnerItemPosition(spnMethod, 1, cur.getString(cur.getColumnIndex("brand"))));
                                        } else if (cur.getString(cur.getColumnIndex("brand")).equals("5")) {
                                            spnBrand.setAdapter(C.getArrayAdapter("Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('05') order by brandCode asc"));
                                            spnBrand.setSelection(Global.SpinnerItemPosition(spnMethod, 1, cur.getString(cur.getColumnIndex("brand"))));
                                        } else if (cur.getString(cur.getColumnIndex("brand")).equals("77")) {
                                            spnBrand.setAdapter(C.getArrayAdapter("Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('77') order by brandCode asc"));
                                            spnBrand.setSelection(Global.SpinnerItemPosition(spnMethod, 1, cur.getString(cur.getColumnIndex("brand"))));
                                        }

                                    } else if (cur.getString(cur.getColumnIndex("ssource")).equals("4")) {
                                        if (cur.getString(cur.getColumnIndex("brand")).equals("4")) {
                                            spnBrand.setAdapter(C.getArrayAdapter("Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('04') order by brandCode asc"));
                                            spnBrand.setSelection(Global.SpinnerItemPosition(spnMethod, 1, cur.getString(cur.getColumnIndex("brand"))));
                                        } else if (cur.getString(cur.getColumnIndex("brand")).equals("5")) {
                                            spnBrand.setAdapter(C.getArrayAdapter("Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('05') order by brandCode asc"));
                                            spnBrand.setSelection(Global.SpinnerItemPosition(spnMethod, 1, cur.getString(cur.getColumnIndex("brand"))));
                                        }

                                    }
                                } else if (spnMethods == 6) {
                                    rdoSSFWA.setVisibility(View.GONE);
                                    rdoSSFWA.setChecked(false);
                                    spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2) in('06')  order by EvCode asc"));
                                    spnMethod.setSelection(Global.SpinnerItemPosition(spnMethod, 2, cur.getString(cur.getColumnIndex("currstatus"))));
                                } else if (spnMethods == 7) {
                                    rdoSSFWA.setVisibility(View.GONE);
                                    rdoSSFWA.setChecked(false);
                                    spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2) in('07')  order by EvCode asc"));
                                    spnMethod.setSelection(Global.SpinnerItemPosition(spnMethod, 2, cur.getString(cur.getColumnIndex("currstatus"))));
                                } else if (spnMethods == 8) {
                                    rdoSSFWA.setVisibility(View.VISIBLE);
                                    spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2) in('08')  order by EvCode asc"));
                                    spnMethod.setSelection(Global.SpinnerItemPosition(spnMethod, 2, cur.getString(cur.getColumnIndex("currstatus"))));
                                } else if (spnMethods == 9) {
                                    rdoSSFWA.setVisibility(View.VISIBLE);
                                    spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2) in('09')  order by EvCode asc"));
                                    spnMethod.setSelection(Global.SpinnerItemPosition(spnMethod, 2, cur.getString(cur.getColumnIndex("currstatus"))));
                                } else if (spnMethods == 10) {
                                    spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2) in('10')  order by EvCode asc"));
                                    spnMethod.setSelection(Global.SpinnerItemPosition(spnMethod, 2, cur.getString(cur.getColumnIndex("currstatus"))));
                                } else if (spnMethods == 11) {

                                    spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2) in('11')  order by EvCode asc"));
                                    spnMethod.setSelection(Global.SpinnerItemPosition(spnMethod, 2, cur.getString(cur.getColumnIndex("currstatus"))));
                                } else if (spnMethods == 12) {
                                    spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2) in('12')  order by EvCode asc"));
                                    spnMethod.setSelection(Global.SpinnerItemPosition(spnMethod, 2, cur.getString(cur.getColumnIndex("currstatus"))));
                                } else if (spnMethods == 13) {
                                    spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2) in('13')  order by EvCode asc"));
                                    spnMethod.setSelection(Global.SpinnerItemPosition(spnMethod, 2, cur.getString(cur.getColumnIndex("currstatus"))));
                                } else if (spnMethods == 14) {
                                    spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2) in('14')  order by EvCode asc"));
                                    spnMethod.setSelection(Global.SpinnerItemPosition(spnMethod, 2, cur.getString(cur.getColumnIndex("currstatus"))));
                                } else if (spnMethods == 15) {
                                    spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2) in('15')  order by EvCode asc"));
                                    spnMethod.setSelection(Global.SpinnerItemPosition(spnMethod, 2, cur.getString(cur.getColumnIndex("currstatus"))));
                                } else if (spnMethods == 16) {
                                    spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2) in('16')  order by EvCode asc"));
                                    spnMethod.setSelection(Global.SpinnerItemPosition(spnMethod, 2, cur.getString(cur.getColumnIndex("currstatus"))));
                                } else if (spnMethods == 17) {
                                    spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2) in('17')  order by EvCode asc"));
                                    spnMethod.setSelection(Global.SpinnerItemPosition(spnMethod, 2, cur.getString(cur.getColumnIndex("currstatus"))));
                                } else if (spnMethods == 18) {
                                    spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2) in('18')  order by EvCode asc"));
                                    spnMethod.setSelection(Global.SpinnerItemPosition(spnMethod, 2, cur.getString(cur.getColumnIndex("currstatus"))));
                                }

                                //spnMethod.setSelection(Integer.parseInt(Global.Left(spnMethod.getSelectedItem().toString(), 2)));
                                //spnMethod.setSelection(Integer.parseInt(cur.getString(cur.getColumnIndex("currstatus"))));
                                //spnMethod.setSelection(Integer.parseInt(cur.getString(cur.getColumnIndex("currstatus"))));
                                if (cur.getString(cur.getColumnIndex("ssource")).equals("1")) {
                                    rdoSSFWA.setChecked(true);
                                    txtMethodQty.setText(cur.getString(cur.getColumnIndex("qty")));
                                    if (cur.getString(cur.getColumnIndex("unit")).equals("1"))
                                        spnMethodUnit.setSelection(1);
                                    else if (cur.getString(cur.getColumnIndex("unit")).equals("2"))
                                        spnMethodUnit.setSelection(2);
                                    else if (cur.getString(cur.getColumnIndex("unit")).equals("3"))
                                        spnMethodUnit.setSelection(3);
                                } else if (cur.getString(cur.getColumnIndex("ssource")).equals("2"))
                                    rdoSSLMarket.setChecked(true);
                                else if (cur.getString(cur.getColumnIndex("ssource")).equals("3"))
                                    rdoSSOther.setChecked(true);
                                else if (cur.getString(cur.getColumnIndex("ssource")).equals("4"))
                                    rdoSSOtherGovt.setChecked(true);


                                // else if(cur.getString(cur.getColumnIndex("brand")).equals("3")) spnBrand.setSelection(3);
                                //
                                //spnBrand.setSelection(Global.SpinnerItemPosition(spnBrand, 2, cur.getString(cur.getColumnIndex("brand"))));
                                //Integer Brand=Integer.parseInt(cur.getString(cur.getColumnIndex("brand")));
                                //if(Brand==77) {
                                ///spnBrand.setAdapter(C.getArrayAdapter("Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)   order by brandCode asc"));
                                // spnBrand.setSelection(Global.SpinnerItemPosition(spnBrand, 2, cur.getString(cur.getColumnIndex("brand"))));
                                // }
                                /*else if(Brand==1)
                                {
                                    spnBrand.setSelection(1);
                                }
                                else if(Brand==2)
                                {
                                    spnBrand.setSelection(2);
                                }*/
                                //spnFaci.setSelection(Global.SpinnerItemPosition(spnFaci, 2 ,cur.getString(cur.getColumnIndex("referCenter"))));

                                //secMethodUnit.setVisibility(View.GONE);

                                if (cur.getString(cur.getColumnIndex("referplace")).equals("1"))
                                    spnWherePlace.setSelection(1);
                                else if (cur.getString(cur.getColumnIndex("referplace")).equals("2"))
                                    spnWherePlace.setSelection(2);
                                else if (cur.getString(cur.getColumnIndex("referplace")).equals("3"))
                                    spnWherePlace.setSelection(3);
                                else if (cur.getString(cur.getColumnIndex("referplace")).equals("4"))
                                    spnWherePlace.setSelection(4);
                                else if (cur.getString(cur.getColumnIndex("referplace")).equals("5"))
                                    spnWherePlace.setSelection(5);
                                else if (cur.getString(cur.getColumnIndex("referplace")).equals("6"))
                                    spnWherePlace.setSelection(6);

                                txtValidity.setText(cur.getString(cur.getColumnIndex("validity")));

                                if (cur.getString(cur.getColumnIndex("dmy")).equals("1"))
                                    spnValidityUnit.setSelection(1);
                                else if (cur.getString(cur.getColumnIndex("dmy")).equals("2"))
                                    spnValidityUnit.setSelection(2);
                                else if (cur.getString(cur.getColumnIndex("dmy")).equals("3"))
                                    spnValidityUnit.setSelection(3);

                                String MRdate = cur.getString(cur.getColumnIndex("MRDate"));
                                PGNNo = cur.getString(cur.getColumnIndex("PGNNo"));
                                if (MRdate != null) {
                                    dtpMD.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("MRDate"))));
                                }
                                //dtpMD.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("MRDate"))));
                                txtMDAge.setText(cur.getString(cur.getColumnIndex("MRAge")));
                                String mrSource = cur.getString(cur.getColumnIndex("mrSource"));
                                if (mrSource != null) {

                                    if (cur.getString(cur.getColumnIndex("mrSource")).equals("1")) {
                                        rdoMethodReceveDT1.setChecked(true);
                                        /*txtMDAge.setVisibility(View.GONE);
                                        VlblMDAge.setVisibility(View.GONE);
                                        dtpMD.setVisibility(View.VISIBLE);
                                        btnMD.setVisibility(View.VISIBLE);*/
                                    } else {
                                        rdoMethodReceveDT2.setChecked(true);
                                      /*  txtMDAge.setVisibility(View.VISIBLE);
                                        VlblMDAge.setVisibility(View.VISIBLE);
                                        dtpMD.setVisibility(View.GONE);
                                        btnMD.setVisibility(View.GONE);*/
                                    }
                                }


                                txtSyringeQty.setText(cur.getString(cur.getColumnIndex("syrinsQty")));
                                dtpDOV.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("vdate"))));
                                //dtpMD.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("MRDate"))));
                                cur.moveToNext();
                            }
                            cur.close();


                            if (String.valueOf(vcode[0][position]).equalsIgnoreCase("12")) {
                                //  Connection.MessageBox(ELCOFormFPI.this, "এই মহিলাটি বর্তমানে গর্ভবতী। এই গর্ভবতী মহিলার গর্ভ সংক্রান্ত সকল তথ্য 'গর্ভবতী মা ও নবজাতকের তথ্য/সেবা ছক' এ সংগ্রহ করুন ।");
                                cmdSave.setEnabled(true);
                                //cmdSave.setEnabled(false);
                                /*Intent f2 = new Intent(getApplicationContext(), FWAReg.class);
                                Bundle IDbundle = new Bundle();
                                g.setPregNo(PGNNo);
                                f2.putExtras(IDbundle);
                                startActivity(f2);
                                finish();*/
                            } else if (String.valueOf(vcode[0][position]).equalsIgnoreCase("13")) {
                                cmdSave.setEnabled(true);
                                // cmdSave.setEnabled(false);
                            } else if (String.valueOf(vcode[0][position]).equalsIgnoreCase("14")) {
                                cmdSave.setEnabled(true);
                                // cmdSave.setEnabled(false);
                            }
                            /*else if(String.valueOf(vcode[0][position]).equalsIgnoreCase("13")) {
                                Intent f2 = new Intent(getApplicationContext(), FWAReg.class);
                                Bundle IDbundle = new Bundle();
                                g.setPregNo(PGNNo);
                                IDbundle.putString("currentstatus","13");


                                f2.putExtras(IDbundle);
                                startActivity(f2);
                                finish();
                            }
                            else if(String.valueOf(vcode[0][position]).equalsIgnoreCase("14")) {
                                Intent f2 = new Intent(getApplicationContext(), FWAReg.class);
                                Bundle IDbundle = new Bundle();
                                g.setPregNo(PGNNo);
                                IDbundle.putString("currentstatus","14");
                                f2.putExtras(IDbundle);
                                startActivity(f2);
                                finish();
                            }*/
                        }
                    });
                } catch (Exception ex) {
                    Connection.MessageBox(ELCOFormFPI.this, ex.getMessage());
                }

            }
            return MyView;
        }

    }

    public class TTCard extends BaseAdapter {
        private Context mContext;
        String[][] vcode;
        Integer totalRec;

        public TTCard(Context c) {
            mContext = c;
        }

        public int getCount() {
            return Integer.parseInt(C.ReturnSingleValue("Select count(*)total from immunizationHistory where healthid='" + g.getGeneratedId() + "'"));
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

                String SQL = "Select imucode as imucode,imudate as imudate,imuCard as imucard from immunizationHistory where healthid='" + g.getGeneratedId() + "' order by imuCode  asc";//date(imudate) asc

                try {
                    Cursor cur = C.ReadData(SQL);
                    cur.moveToFirst();

                    totalRec = cur.getCount();
                    vcode = new String[4][totalRec];
                    int i = 0;
                    while (!cur.isAfterLast()) {
                        vcode[0][i] = "টিটি " + String.valueOf(i + 1);//String.valueOf(cur.getString(cur.getColumnIndex("imucode")));
                        vcode[1][i] = Global.DateConvertDMY(cur.getString(cur.getColumnIndex("imudate")));
                        vcode[2][i] = cur.getString(cur.getColumnIndex("imucard"));
                        vcode[3][i] = String.valueOf(cur.getString(cur.getColumnIndex("imucode")));

                        i += 1;
                        cur.moveToNext();
                    }
                    if (i == 1) {
                        secTT1a.setVisibility(View.VISIBLE);


                    } else if (i == 2) {
                        secTT1a.setVisibility(View.VISIBLE);
                        secTT2b.setVisibility(View.VISIBLE);

                    } else if (i == 3) {
                        secTT1a.setVisibility(View.VISIBLE);
                        secTT2b.setVisibility(View.VISIBLE);
                        secTT3c.setVisibility(View.VISIBLE);
                    } else if (i == 4) {
                        secTT1a.setVisibility(View.VISIBLE);
                        secTT2b.setVisibility(View.VISIBLE);
                        secTT3c.setVisibility(View.VISIBLE);
                        secTT4d.setVisibility(View.VISIBLE);
                    } else if (i == 5) {
                        secTT1a.setVisibility(View.VISIBLE);
                        secTT2b.setVisibility(View.VISIBLE);
                        secTT3c.setVisibility(View.VISIBLE);
                        secTT4d.setVisibility(View.VISIBLE);
                        secTT5e.setVisibility(View.VISIBLE);
                    }
                    cur.close();

                    Button tv = (Button) MyView.findViewById(R.id.image_name);
                    tv.setTextSize(14);
                    tv.setText(vcode[0][position] + "\n" + vcode[1][position]);
                    final Integer p = position;
                    tv.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            ChkTT1.setChecked(true);
                            if (vcode[2][position].equals("1")) {
                                rdoCardYes.setChecked(true);
                                rdogrpTTCard.setVisibility(View.VISIBLE);
                                // secTT1a.setVisibility(View.VISIBLE);

                            } else if (vcode[2][position].equals("2")) {
                                rdoCardNo.setChecked(true);
                                rdogrpTTCard.setVisibility(View.VISIBLE);
                                // secTT1a.setVisibility(View.VISIBLE);
                                //secTT2b.setVisibility(View.VISIBLE);

                            }
                           /* else if(vcode[2][position].equals("3"))
                            {

                            }

                            else if(vcode[2][position].equals("4"))
                            {
                                secTT1a.setVisibility(View.VISIBLE);
                                secTT2b.setVisibility(View.VISIBLE);
                                secTT3c.setVisibility(View.VISIBLE);
                                secTT4d.setVisibility(View.VISIBLE);
                            }
                            else if(vcode[2][position].equals("5"))
                            {
                                secTT1a.setVisibility(View.VISIBLE);
                                secTT2b.setVisibility(View.VISIBLE);
                                secTT3c.setVisibility(View.VISIBLE);
                                secTT4d.setVisibility(View.VISIBLE);
                               secTT5e.setVisibility(View.VISIBLE);
                            }*/

                            if (vcode[1][position].length() != 0) {
                                dtpDOTT1.setText(vcode[1][position]);
                                dtpDOTT1.setVisibility(View.VISIBLE);
                                btnDOTT1.setVisibility(View.VISIBLE);
                            } else {
                                dtpDOTT1.setText("");
                                dtpDOTT1.setVisibility(View.GONE);
                                btnDOTT1.setVisibility(View.GONE);
                            }

                            secTT1.setVisibility(View.VISIBLE);
                            btnTTClose.setVisibility(View.VISIBLE);
                            btnAddTT.setVisibility(View.GONE);
                            btnSaveTT.setVisibility(View.VISIBLE);
                            g.setImuCode(vcode[3][position]);
                        }
                    });
                } catch (Exception ex) {
                    Connection.MessageBox(ELCOFormFPI.this, ex.getMessage());
                }

            }
            return MyView;
        }

    }

    private String GetDOV(String HealthId) {
        String sq = String.format("Select ifnull(max(vDate),'') as Vdate from elcoVisit WHERE healthId = '%s'", g.getGeneratedId());
        return C.ReturnSingleValue(sq);
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

            dtpDate = (EditText) findViewById(R.id.dtpDOM);

            if (VariableID.equals("btnDOM")) {
                dtpDate = (EditText) findViewById(R.id.dtpDOM);
            } else if (VariableID.equals("btnDOV")) {
                dtpDate = (EditText) findViewById(R.id.dtpDOV);
            } else if (VariableID.equals("btnDOTT1")) {
                dtpDate = (EditText) findViewById(R.id.dtpDOTT1);
            } else if (VariableID.equals("btnMD")) {
                dtpDate = (EditText) findViewById(R.id.dtpMD);
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

                    if (VariableID.equals("btnDOV")) {
                        Connection.MessageBox(ELCOFormFPI.this, "তারিখ আজকের তারিখের সমান অথবা কম হতে হবে।");
                        return;
                    }
                }
                String DOB1 = "15/" + DOB.getText() + "/" + DOB.getText();
                Date DOB11 = sdf.parse(DOB1);
                if (date2.before(DOB11)) {

                    if (VariableID.equals("btnB11")) {
                        Connection.MessageBox(ELCOFormFPI.this, "আপনার সর্বশেষ গর্ভ ফলাফলের তারিখ  জন্ম তারিখ অপেক্ষা বড় হবে না");
                        return;
                    }
                }
                String DOB2 = Global.DateConvertDMY(GetDOV(g.getGeneratedId()));
                Date DOB21 = sdf.parse(DOB2);
                if (date2.before(DOB21)) {

                    /*if (VariableID.equals("btnDOV"))
                    {
                        Connection.MessageBox(ELCOForm.this," ভিজিট এর তারিখ পূর্বের ভিজিট তারিখ এর সমান অথবা বেশি  হতে হবে");
                        return;
                    }*/
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