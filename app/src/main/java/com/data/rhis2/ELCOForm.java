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
import android.text.TextWatcher;
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
 * Created by user on 20/02/2015.
 */
public class ELCOForm extends Activity {
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
        AlertDialog.Builder adb = new AlertDialog.Builder(ELCOForm.this);
        switch (item.getItemId()) {
            case R.id.menuClose:
                adb.setTitle("Close");
                adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান?");
                adb.setNegativeButton("না", null);
                adb.setPositiveButton("হ্যাঁ", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (g.getCallFrom().equals("HAregis")) {
                            finish();
                            Intent f2 = new Intent(getApplicationContext(), Elcoview.class);
                            startActivity(f2);
                        } else {
                            finish();
                            Intent f2 = new Intent(getApplicationContext(), MemberList.class);
                            startActivity(f2);
                            g.setGeneratedId("");
                        }
                     /*   finish();
                        Intent f2 = new Intent(getApplicationContext(),MemberList.class);
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

    LinearLayout secHHNo;
    TextView VlblHHNo;

    CheckBox chkHHNoDontKnow;
    EditText txtHHNo;

    LinearLayout secSNo;
    TextView VlblSNo;
    TextView txtSNo;

    TextView VlblDOB;
    TextView DOB;

    LinearLayout secName;
    TextView VlblName;
    TextView txtName;

    LinearLayout secAge;
    TextView VlblAge;
    TextView txtAge;

    LinearLayout secHusName;
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

    TextView VlblELCOStatus;
    RadioGroup rdogrpELCOStatus;
    RadioButton rdoELCOStatus1;
    RadioButton rdoELCOStatus2;

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


    LinearLayout secLiveSon;
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
    LinearLayout secBrand;
    Spinner spnBrand;

    LinearLayout secMethodReceveDT;
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

    LinearLayout secSendForMethod;
    TextView VlblSendForMethod;
    Spinner spnSendForMethod;

    LinearLayout secImpotent;
    Spinner spnImpotent;
    TextView VlblImpotent;

    LinearLayout secDetail;

    String StartTime;

    LinearLayout secValidity;
    EditText txtValidity;
    Spinner spnValidityUnit;
    GridView g1;

    ImageButton btnAddTT;
    ImageButton btnSaveTT;
    ImageButton btnTTClose;
    TextView txtVisitNo;

    TextView Sex;
    String LMP = "";
    String pregnancyNo = "";
    Context conxt;
    Button cmdSave;
    LinearLayout secMessage;
    TextView VlblMessage;
    TextView VlblMessageMeso;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.elco);

            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();
            conxt = this;
            TableName = "ELCO";
            TableNameELCOVisit = "ELCOVisit";


            // Adapter
            /*SpinnerAdapter adapter =
                    ArrayAdapter.createFromResource(this, R.array.actions,
                            android.R.layout.simple_spinner_dropdown_item);

            // Callback
            ActionBar.OnNavigationListener callback = new ActionBar.OnNavigationListener() {

                String[] items = getResources().getStringArray(R.array.actions); // List items from res

                @Override
                public boolean onNavigationItemSelected(int position, long id) {

                    if (position == 1) {
                        Intent f2 = new Intent(getApplicationContext(), ELCOCurrentStock.class);
                        startActivity(f2);
                    }
                    if (position == 2) {
                        Intent f2 = new Intent(getApplicationContext(), Death.class);
                        startActivity(f2);
                    }
                    if (position == 3) {
                        Intent f2 = new Intent(getApplicationContext(), Elcoview.class);
                        startActivity(f2);
                    }
                    return true;

                }

            };

            // Action Bar
            ActionBar actions = getActionBar();
            actions.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
            actions.setDisplayShowTitleEnabled(true);
            actions.setListNavigationCallbacks(adapter, callback);
            String[] items = getResources().getStringArray(R.array.actions);*/
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

            /*if(g.getCallFrom().equals("1"))
            {
                txtHealthID.setText(g.getHealthID());
                txtHHNo.setText(g.getHHNO());
                txtSNo.setText(g.getSLno());
                txtAge.setText(g.getAAge());
                txtName.setText(g.getAName());
            }

            if(g.getCallFrom().equals("ml")) {
                txtHealthID.setText(g.getHealthID());
                txtHHNo.setText(g.getHouseholdNo());
                txtName.setText(g.getAName());
                txtAge.setText(g.getAAge());
            }*/

            txtVisitNo = (TextView) findViewById(R.id.txtVisitNo);
            txtVisitNo.setText(VisitNumber(g.getGeneratedId()));

            txtPHHNo = (TextView) findViewById(R.id.txtPHHNo);
            txtPHHNo.setText(g.getHouseholdNo());

            chkHHNoDontKnow = (CheckBox) findViewById(R.id.chkHHNoDontKnow);
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
            txtHHNo.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (txtHHNo.getText().length() > 0) {
                        chkHHNoDontKnow.setChecked(false);
                    } else {
                        chkHHNoDontKnow.setChecked(true);
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

            rdogrpELCOStatus = (RadioGroup) findViewById(R.id.rdogrpELCOStatus);
            rdoELCOStatus1 = (RadioButton) findViewById(R.id.rdoELCOStatus1);
            rdoELCOStatus2 = (RadioButton) findViewById(R.id.rdoELCOStatus2);

            btnPlus = (ImageButton) findViewById(R.id.btnPlus);
            btnMinus = (ImageButton) findViewById(R.id.btnMinus);
            btnMinus.setVisibility(View.INVISIBLE);

            secDetail.setVisibility(View.GONE);
            secMEdu.setVisibility(View.GONE);
            secMNID.setVisibility(View.GONE);
            secMBRID.setVisibility(View.GONE);
            secMMNo.setVisibility(View.GONE);
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
                        txtMAge.setVisibility(View.VISIBLE);
                        VlblMAge.setVisibility(View.VISIBLE);
                        txtMAge.requestFocus();
                    }
                }
            });

            secLiveSon = (LinearLayout) findViewById(R.id.secLiveSon);
            VlblLiveSon = (TextView) findViewById(R.id.VlblLiveSon);
            txtLiveSon = (EditText) findViewById(R.id.txtLiveSon);

            VlblLiveDau = (TextView) findViewById(R.id.VlblLiveDau);
            txtLiveDau = (EditText) findViewById(R.id.txtLiveDau);

            dtpDOV = (EditText) findViewById(R.id.dtpDOV);
            dtpDOV.setText(Global.DateNowDMY());
            btnDOV = (ImageButton) findViewById(R.id.btnDOV);


            secMethodReceveDT = (LinearLayout) findViewById(R.id.secMethodReceveDT);
            VlblMethodReceveDT = (TextView) findViewById(R.id.VlblMethodReceveDT);
            rdogrpMethodReceveDT = (RadioGroup) findViewById(R.id.rdogrpMethodReceveDT);
            rdoMethodReceveDT1 = (RadioButton) findViewById(R.id.rdoMethodReceveDT1);
            //rdoMethodReceveDT1.setChecked(true);
            rdoMethodReceveDT2 = (RadioButton) findViewById(R.id.rdoMethodReceveDT2);
            txtMDAge = (EditText) findViewById(R.id.txtMDAge);
            VlblMDAge = (TextView) findViewById(R.id.VlblMDAge);

            dtpMD = (EditText) findViewById(R.id.dtpMD);
            dtpMD.setText(Global.DateNowDMY());
            btnMD = (ImageButton) findViewById(R.id.btnMD);

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

            secSendForMethod = (LinearLayout) findViewById(R.id.secSendForMethod);
            VlblSendForMethod = (TextView) findViewById(R.id.VlblSendForMethod);
            spnSendForMethod = (Spinner) findViewById(R.id.spnSendForMethod);
            List<String> listSendForMethod = new ArrayList<String>();
            listSendForMethod.add("");
            listSendForMethod.add("01-খাবার বড়ি");
            listSendForMethod.add("02-কনডম");
            listSendForMethod.add("03-ইনজেকটেবল");
            listSendForMethod.add("04-আই ইউ ডি");
            listSendForMethod.add("05-ইমপ্ল্যান্ট");
            listSendForMethod.add("06-স্থায়ী পদ্ধতি(পুরুষ) এনএসভি");
            listSendForMethod.add("07-স্থায়ী পদ্ধতি(মহিলা) টিউবেকটমী");
            ArrayAdapter<String> adptrspnSendForMethod = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listSendForMethod);
            spnSendForMethod.setAdapter(adptrspnSendForMethod);

            secImpotent = (LinearLayout) findViewById(R.id.secImpotent);
            VlblImpotent = (TextView) findViewById(R.id.VlblImpotent);
            spnImpotent = (Spinner) findViewById(R.id.spnImpotent);
            List<String> listImpotent = new ArrayList<String>();
            listImpotent.add("");
            listImpotent.add("01-১ম");
            listImpotent.add("02-২য়");
            ArrayAdapter<String> adptrspnImpotent = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listImpotent);
            spnImpotent.setAdapter(adptrspnImpotent);

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

            e.PregnancyInfo(conxt, g.getGeneratedId(), LastPGNNo(), g.getProvType());
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
                        secMethodUnit.setVisibility(View.VISIBLE);
                        txtMethodQty.setEnabled(true);
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
                        secMethodUnit.setVisibility(View.VISIBLE);
                        txtMethodQty.setEnabled(true);
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
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        rdogrpMethodReceveDT.clearCheck();
                        dtpMD.setText("");
                        secSyringe.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.VISIBLE);
                        txtMethodQty.setEnabled(false);
                        spnMethodUnit.setSelection(3);
                        txtMethodQty.setText("1");
                        txtSyringeQty.setText("1");
                        rdoSSFWA.setVisibility(View.VISIBLE);
                    } else if (id == R.id.rdoSSLMarket & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("03")) {
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        rdogrpMethodReceveDT.clearCheck();
                        dtpMD.setText("");
                        secSyringe.setVisibility(View.GONE);
                        secMethodUnit.setVisibility(View.GONE);
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                    } else if (id == R.id.rdoSSOther & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("03")) {
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        rdogrpMethodReceveDT.clearCheck();
                        dtpMD.setText("");
                        secSyringe.setVisibility(View.GONE);
                        secMethodUnit.setVisibility(View.GONE);
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                    } else if (id == R.id.rdoSSOtherGovt & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("03")) {
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        rdogrpMethodReceveDT.clearCheck();
                        dtpMD.setText("");
                        secSyringe.setVisibility(View.GONE);
                        secMethodUnit.setVisibility(View.GONE);
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                    } else if (id == R.id.rdoSSFWA & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("04")) {
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        secValidity.setVisibility(View.VISIBLE);
                        spnValidityUnit.setSelection(3);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);
                    } else if (id == R.id.rdoSSLMarket & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("04")) {
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        secValidity.setVisibility(View.VISIBLE);
                        spnValidityUnit.setSelection(3);
                    } else if (id == R.id.rdoSSOther & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("04")) {
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        secValidity.setVisibility(View.VISIBLE);
                        spnValidityUnit.setSelection(3);
                    } else if (id == R.id.rdoSSOtherGovt & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("04")) {
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        secValidity.setVisibility(View.VISIBLE);
                        spnValidityUnit.setSelection(3);
                    } else if (id == R.id.rdoSSFWA & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("05")) {
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        secValidity.setVisibility(View.VISIBLE);
                        spnValidityUnit.setSelection(3);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);
                        //secBrand.setVisibility(View.VISIBLE);
                        //spnBrand.setAdapter(C.getArrayAdapter("Select '  'as brandCode union Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('04','05') order by brandCode asc"));
                    } else if (id == R.id.rdoSSLMarket & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("05")) {
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        secValidity.setVisibility(View.VISIBLE);
                        spnValidityUnit.setSelection(3);
                        secBrand.setVisibility(View.VISIBLE);
                        spnBrand.setAdapter(null);
                        spnBrand.setAdapter(C.getArrayAdapter("Select '  'as brandCode union Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('77') order by brandCode asc"));
                    } else if (id == R.id.rdoSSOther & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("05")) {
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        secValidity.setVisibility(View.VISIBLE);
                        spnValidityUnit.setSelection(3);
                        secBrand.setVisibility(View.VISIBLE);
                        spnBrand.setAdapter(null);
                        spnBrand.setAdapter(C.getArrayAdapter("Select '  'as brandCode union Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('04','05','77') order by brandCode asc"));
                    } else if (id == R.id.rdoSSOtherGovt & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("05")) {
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        secValidity.setVisibility(View.VISIBLE);
                        spnValidityUnit.setSelection(3);
                        secBrand.setVisibility(View.VISIBLE);
                        spnBrand.setAdapter(null);
                        spnBrand.setAdapter(C.getArrayAdapter("Select '  'as brandCode union Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('04','05') order by brandCode asc"));

                    } else if (id == R.id.rdoSSFWA & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("06")) {
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);

                    } else if (id == R.id.rdoSSFWA & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("07")) {
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);

                    } else if (id == R.id.rdoSSFWA & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("08")) {
                        secMethodUnit.setVisibility(View.VISIBLE);
                        txtMethodQty.setEnabled(true);
                        spnMethodUnit.setSelection(3);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);
                    } else if (id == R.id.rdoSSLMarket & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("08")) {
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
                            txtMethodQty.setEnabled(true);
                            spnMethodUnit.setSelection(3);
                            secBrand.setVisibility(View.GONE);
                            spnBrand.setSelection(0);
                            VlblMessage.setVisibility(View.GONE);
                            VlblMessageMeso.setVisibility(View.VISIBLE);
                        } else {
                            VlblMessage.setVisibility(View.GONE);
                            VlblMessageMeso.setVisibility(View.GONE);
                            cmdSave.setEnabled(false);
                            Connection.MessageBox(ELCOForm.this, "গর্ভকালীন সেবার সময়ে ২২৩ দিনের পর থেকে মিসোপ্রোস্টোল বড়ি দিতে পারবে । ");
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
                            cmdSave.setEnabled(false);
                            Connection.MessageBox(ELCOForm.this, "গর্ভকালীন সেবার সময়ে ২২৩ দিনের পর থেকে মিসোপ্রোস্টোল বড়ি দিতে পারবে । ");
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
                            cmdSave.setEnabled(false);
                            Connection.MessageBox(ELCOForm.this, "গর্ভকালীন সেবার সময়ে ২২৩ দিনের পর থেকে মিসোপ্রোস্টোল বড়ি দিতে পারবে । ");
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
                            cmdSave.setEnabled(false);
                            Connection.MessageBox(ELCOForm.this, "গর্ভকালীন সেবার সময়ে ২২৩ দিনের পর থেকে মিসোপ্রোস্টোল বড়ি দিতে পারবে । ");
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
            txtSyringeQty = (EditText) findViewById(R.id.txtSyringeQty);
            dtDueDoze = (TextView) findViewById(R.id.dtDueDoze);
            String lastDozeDate = C.ReturnSingleValue("select  strftime('%d/%m/%Y', date( doseDate)) from womanInjectable where healthId='" + g.getGeneratedId() + "' and doseId=(select  max(doseId) from womanInjectable)");
            if (lastDozeDate != "") {
                String DozeDate = Global.addDays(lastDozeDate, 90);
                ((TextView) findViewById(R.id.dtDueDoze)).setText(DozeDate);
            }

            secWherePlace = (LinearLayout) findViewById(R.id.secWherePlace);
            VlblWherePlace = (TextView) findViewById(R.id.VlblWherePlace);
            spnWherePlace = (Spinner) findViewById(R.id.spnWherePlace);
            List<String> listFacility = new ArrayList<String>();
            listFacility.add("");
            listFacility.add("01-উপজেলা স্বাস্থ্য কমপ্লেক্স");
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
                        spnMethod.setEnabled(true);
                    } else if (id == R.id.rdoVSAbsent) {
                        MethodList("a", Sex.getText().toString());
                        rdogrpSS.clearCheck();
                        secMethod.setVisibility(View.VISIBLE);
                        spnMethod.setSelection(0);
                        spnWherePlace.setSelection(0);
                        txtMethodQty.setText("");
                        dtpMD.setText("");
                        spnMethodUnit.setSelection(0);
                        spnMethod.setEnabled(true);
                    } else {
                        //secMethod.setVisibility( View.GONE );
                    }
                }
            });

            if (CurrentStatus(g.getGeneratedId()).equals("12")) {
                spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2) in('09') order by EvCode asc"));
                secMessage.setVisibility(View.VISIBLE);
                VlblMessage.setVisibility(View.VISIBLE);
                cmdSave.setEnabled(false);

            } else if (CurrentStatus(g.getGeneratedId()).equals("09")) {
                spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode"));
                secMessage.setVisibility(View.VISIBLE);
                VlblMessage.setVisibility(View.VISIBLE);
                cmdSave.setEnabled(false);

            } else {
                spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2) not in('09','13','14') order by EvCode asc"));
                cmdSave.setEnabled(true);
                secMessage.setVisibility(View.GONE);
                VlblMessage.setVisibility(View.GONE);
                VlblMessageMeso.setVisibility(View.GONE);
            }
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
                        txtSyringeQty.setText("");
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        txtMethodQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        rdogrpMethodReceveDT.clearCheck();
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);
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
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        rdogrpMethodReceveDT.clearCheck();
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("01") && rdoSSLMarket.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.VISIBLE);
                        spnBrand.setAdapter(C.getArrayAdapter("Select '  'as brandCode union Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('77') order by brandCode asc"));
                        spnBrand.setEnabled(true);
                        spnMethodUnit.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        rdogrpMethodReceveDT.clearCheck();
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("01") && rdoSSOther.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.VISIBLE);
                        spnBrand.setAdapter(C.getArrayAdapter("Select '  'as brandCode union Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('01','02','77') order by brandCode asc"));
                        spnBrand.setEnabled(true);
                        spnMethodUnit.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        rdogrpMethodReceveDT.clearCheck();
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("01") && rdoSSOtherGovt.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.VISIBLE);
                        spnBrand.setAdapter(C.getArrayAdapter("Select '  'as brandCode union Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('01','02') order by brandCode asc"));
                        spnBrand.setEnabled(true);
                        spnMethodUnit.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        rdogrpMethodReceveDT.clearCheck();
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
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        rdogrpMethodReceveDT.clearCheck();
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("02") && rdoSSFWA.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.VISIBLE);
                        secBrand.setVisibility(View.VISIBLE);
                        spnBrand.setAdapter(C.getArrayAdapter("Select '  'as brandCode union Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('03') order by brandCode asc"));
                        spnMethodUnit.setSelection(2);
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        rdogrpMethodReceveDT.clearCheck();
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("02") && rdoSSLMarket.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.VISIBLE);
                        spnBrand.setAdapter(C.getArrayAdapter("Select '  'as brandCode union Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('77') order by brandCode asc"));
                        spnMethodUnit.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        rdogrpMethodReceveDT.clearCheck();
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("02") && rdoSSOther.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.VISIBLE);
                        spnBrand.setAdapter(C.getArrayAdapter("Select '  'as brandCode union Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('03','77') order by brandCode asc"));
                        spnMethodUnit.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        rdogrpMethodReceveDT.clearCheck();
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
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        rdogrpMethodReceveDT.clearCheck();
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("02")) {
                        secSS.setVisibility(View.VISIBLE);
                        rdogrpSS.clearCheck();
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        txtMethodQty.setText("");
                        spnMethodUnit.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        rdogrpMethodReceveDT.clearCheck();
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);
                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("03") && rdoSSFWA.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.VISIBLE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(3);
                        txtMethodQty.setText("1");
                        txtMethodQty.setEnabled(false);
                        secSyringe.setVisibility(View.VISIBLE);
                        txtSyringeQty.setText("1");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        //dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);
                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("03") && rdoSSLMarket.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                        txtMethodQty.setEnabled(false);
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        //dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);
                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("03") && rdoSSOther.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                        txtMethodQty.setEnabled(false);
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        //dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);
                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("03") && rdoSSOtherGovt.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                        txtMethodQty.setEnabled(false);
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        //dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);
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
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        rdogrpMethodReceveDT.clearCheck();
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);
                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("04") && rdoSSFWA.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        secValidity.setVisibility(View.VISIBLE);
                        spnValidityUnit.setSelection(3);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);
                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("04") && rdoSSLMarket.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        secValidity.setVisibility(View.VISIBLE);
                        spnValidityUnit.setSelection(3);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);
                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("04") && rdoSSOther.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        secValidity.setVisibility(View.VISIBLE);
                        spnValidityUnit.setSelection(3);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);
                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("04") && rdoSSOtherGovt.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        secValidity.setVisibility(View.VISIBLE);
                        spnValidityUnit.setSelection(3);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);
                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("04")) {
                        secSS.setVisibility(View.VISIBLE);
                        rdogrpSS.clearCheck();
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secMethodReceveDT.setVisibility(View.GONE);
                        rdogrpMethodReceveDT.clearCheck();
                        dtpMD.setText("");
                        secValidity.setVisibility(View.GONE);
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);
                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("05") && rdoSSFWA.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        secValidity.setVisibility(View.VISIBLE);
                        spnValidityUnit.setSelection(3);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("05") && rdoSSLMarket.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.VISIBLE);
                        spnBrand.setAdapter(C.getArrayAdapter("Select '  'as brandCode union Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('77') order by brandCode asc"));
                        spnMethodUnit.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        secValidity.setVisibility(View.VISIBLE);
                        spnValidityUnit.setSelection(3);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("05") && rdoSSOther.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.VISIBLE);
                        spnBrand.setAdapter(C.getArrayAdapter("Select '  'as brandCode union Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('04','05','77') order by brandCode asc"));
                        spnMethodUnit.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        secValidity.setVisibility(View.VISIBLE);
                        spnValidityUnit.setSelection(3);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("05") && rdoSSOtherGovt.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.VISIBLE);
                        spnBrand.setAdapter(C.getArrayAdapter("Select '  'as brandCode union Select substr('0' ||brandCode, -2, 2)||'-'||brandName as brandCode from BrandMethod where substr('0' ||brandCode, -2, 2)  in('04','05') order by brandCode asc"));
                        spnMethodUnit.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secMethodReceveDT.setVisibility(View.VISIBLE);
                        secValidity.setVisibility(View.VISIBLE);
                        spnValidityUnit.setSelection(3);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("05")) {
                        secSS.setVisibility(View.VISIBLE);
                        rdogrpSS.clearCheck();
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        txtMethodQty.setText("");
                        spnMethodUnit.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secMethodReceveDT.setVisibility(View.GONE);
                        rdogrpMethodReceveDT.clearCheck();
                        secValidity.setVisibility(View.GONE);
                        spnValidityUnit.setSelection(0);
                        dtpMD.setText("");
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("06") && rdoSSFWA.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        rdogrpMethodReceveDT.clearCheck();
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("06") && rdoSSLMarket.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        rdogrpMethodReceveDT.clearCheck();
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("06") && rdoSSOther.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        rdogrpMethodReceveDT.clearCheck();
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("06") && rdoSSOtherGovt.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        rdogrpMethodReceveDT.clearCheck();
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("06")) {
                        secSS.setVisibility(View.VISIBLE);
                        rdogrpSS.clearCheck();
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        rdogrpMethodReceveDT.clearCheck();
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("07") && rdoSSFWA.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnMethodUnit.setSelection(3);
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        rdogrpMethodReceveDT.clearCheck();
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("07") && rdoSSLMarket.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnMethodUnit.setSelection(3);
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        rdogrpMethodReceveDT.clearCheck();
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("07") && rdoSSOther.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnMethodUnit.setSelection(3);
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        rdogrpMethodReceveDT.clearCheck();
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("07") && rdoSSOtherGovt.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnMethodUnit.setSelection(3);
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        rdogrpMethodReceveDT.clearCheck();
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("07")) {
                        secSS.setVisibility(View.VISIBLE);
                        rdogrpSS.clearCheck();
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        rdogrpMethodReceveDT.clearCheck();
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.GONE);
                        rdoSSFWA.setChecked(false);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("08") && rdoSSFWA.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.VISIBLE);
                        spnMethodUnit.setSelection(3);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        rdogrpMethodReceveDT.clearCheck();
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("08") && rdoSSLMarket.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        txtMethodQty.setText("");
                        spnMethodUnit.setSelection(0);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        rdogrpMethodReceveDT.clearCheck();
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("08") && rdoSSOther.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        txtMethodQty.setText("");
                        spnMethodUnit.setSelection(0);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        rdogrpMethodReceveDT.clearCheck();
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("08") && rdoSSOtherGovt.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        txtMethodQty.setText("");
                        spnMethodUnit.setSelection(0);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        rdogrpMethodReceveDT.clearCheck();
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("08")) {
                        secSS.setVisibility(View.VISIBLE);
                        rdogrpSS.clearCheck();
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        rdogrpMethodReceveDT.clearCheck();
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("09") && rdoSSFWA.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.VISIBLE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(3);
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        rdogrpMethodReceveDT.clearCheck();
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("09") && rdoSSLMarket.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        txtMethodQty.setText("");
                        spnMethodUnit.setSelection(0);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        rdogrpMethodReceveDT.clearCheck();
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("09") && rdoSSOther.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        txtMethodQty.setText("");
                        spnMethodUnit.setSelection(0);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        rdogrpMethodReceveDT.clearCheck();
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("09") && rdoSSOtherGovt.isChecked()) {
                        secSS.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.GONE);
                        txtMethodQty.setText("");
                        spnMethodUnit.setSelection(0);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        rdogrpMethodReceveDT.clearCheck();
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("09")) {
                        secSS.setVisibility(View.VISIBLE);
                        rdogrpSS.clearCheck();
                        secMethodUnit.setVisibility(View.GONE);
                        spnMethodUnit.setSelection(0);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        txtMethodQty.setText("");
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        rdogrpMethodReceveDT.clearCheck();
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        rdoSSFWA.setVisibility(View.VISIBLE);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("10")) {
                        secSS.setVisibility(View.GONE);
                        rdogrpSS.clearCheck();
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.VISIBLE);
                        //spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.VISIBLE);
                        //spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        rdogrpMethodReceveDT.clearCheck();
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
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.VISIBLE);
                        //spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.VISIBLE);
                        //spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        rdogrpMethodReceveDT.clearCheck();
                        dtpMD.setText("");
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);

                    }
                    //
                    else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("17")) {
                        secSS.setVisibility(View.GONE);
                        rdogrpSS.clearCheck();
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                        secSyringe.setVisibility(View.GONE);
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.VISIBLE);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        rdogrpMethodReceveDT.clearCheck();
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
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        rdogrpMethodReceveDT.clearCheck();
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
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        rdogrpMethodReceveDT.clearCheck();
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                        cmdSave.setText("Save and Continue...");

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
                        txtSyringeQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        spnWherePlace.setSelection(0);
                        secSendForMethod.setVisibility(View.GONE);
                        spnSendForMethod.setSelection(0);
                        secImpotent.setVisibility(View.GONE);
                        spnImpotent.setSelection(0);
                        secValidity.setVisibility(View.GONE);
                        secMethodReceveDT.setVisibility(View.GONE);
                        rdogrpMethodReceveDT.clearCheck();
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
            //ImmunizationData(txtHealthID.getText().toString());
            cmdSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {


                    DataSave();
                    //g.setCallFrom("elco");
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
            btnSaveTT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //GridView gcount=(GridView) findViewById(R.id.gridTT);
                    if (!ChkTT1.isChecked()) {
                        Connection.MessageBox(ELCOForm.this, "টিটি পেয়েছে কিনা এ তথ্য না হতে পারে না।");
                        ChkTT1.requestFocus();
                        return;
                    } else if (ChkTT1.isChecked()) {
                        if (!rdoCardYes.isChecked() & !rdoCardNo.isChecked()) {
                            Connection.MessageBox(ELCOForm.this, "টিটি কার্ড আছে কিনা এ তথ্য হ্যাঁ/না হতে হবে।");
                            rdoCardYes.requestFocus();
                            return;
                        } else if (rdoCardYes.isChecked() & dtpDOTT1.getText().length() == 0) {
                            Connection.MessageBox(ELCOForm.this, "টিটি কার্ড থাকলে অবশ্যই তারিখ থাকতে হবে।");
                            dtpDOTT1.requestFocus();
                            return;
                        } else if (rdoCardNo.isChecked() & dtpDOTT1.getText().length() == 0) {

                        } else {
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

                            if (date2.after(date1)) {
                                Connection.MessageBox(ELCOForm.this, "টিটি এর তারিখ আজকের তারিখ[" + formattedDate + "] এর সমান অথবা কম হতে হবে।");
                                dtpDOTT1.requestFocus();
                                return;
                            }
                            if (validateTTDateAgainstDOB()) {
                                Connection.MessageBox(ELCOForm.this, "টিটি এর তারিখ জন্ম তারিখ এর থেকে  কম হবে না ।");
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


            ImmunizationStatus();
            ElcoVisitStsatus();

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } catch (Exception e) {
            Connection.MessageBox(ELCOForm.this, e.getMessage());
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
                cmdSave.setEnabled(false);
            } else if (CurrentStatus(g.getGeneratedId()).equals("09")) {
                spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode"));
                cmdSave.setEnabled(false);
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
                cmdSave.setEnabled(false);
            } else if (CurrentStatus(g.getGeneratedId()).equals("09")) {
                spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode"));
                cmdSave.setEnabled(false);
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

    private void SameDateDifferentItem() {
        try {
            if (validateMarriageDateAgainstSystemDate()) {
                Connection.MessageBox(ELCOForm.this, " বিবাহের এর তারিখ আজকের তারিখ এর সমান অথবা কম হতে হবে।");
                return;
            }
            if (validateMethodDateAgainstMarriageDate()) {
                Connection.MessageBox(ELCOForm.this, " ভিজিট এর তারিখ বিবাহের তারিখ এর সমান অথবা বেশি  হতে হবে।");
                return;
            }

            if (secBrand.getVisibility() == View.VISIBLE) {
                if (spnBrand.getSelectedItemPosition() == 0) {
                    Connection.MessageBox(ELCOForm.this, " ব্র্যান্ড লিখুন");
                    return;
                }
            }

            if (secMethodUnit.getVisibility() == View.VISIBLE) {
                if (txtMethodQty.getVisibility() == View.VISIBLE) {
                    if (txtMethodQty.getText().toString().length() == 0) {
                        Connection.MessageBox(ELCOForm.this, " পরিমান লিখুন");
                        return;
                    }

                }
            }

            if (secWherePlace.getVisibility() == View.VISIBLE) {
                if (spnWherePlace.getSelectedItemPosition() == 0) {
                    Connection.MessageBox(ELCOForm.this, " কোথায় প্রেরন করা হয়েছে  লিখুন");
                    return;
                }
            }

            if (secSendForMethod.getVisibility() == View.VISIBLE) {
                if (spnSendForMethod.getSelectedItemPosition() == 0) {
                    Connection.MessageBox(ELCOForm.this, "কোন পদ্ধতি জন্য প্রেরন  লিখুন");
                    return;
                }
            }

            if (secImpotent.getVisibility() == View.VISIBLE) {
                if (spnImpotent.getSelectedItemPosition() == 0) {
                    Connection.MessageBox(ELCOForm.this, "কত তম বন্ধ্যা সিলেক্ট করুন");
                    return;
                }
            }

            if (secSS.getVisibility() == View.VISIBLE) {
                if (rdogrpSS.getVisibility() == View.VISIBLE) {

                    Integer source = rdogrpSS.getCheckedRadioButtonId();
                    if (source == null) {
                        Connection.MessageBox(ELCOForm.this, " উৎস সিলেক্ট করুন।");
                        return;
                    }

                    if (source == -1) {
                        Connection.MessageBox(ELCOForm.this, " উৎস সিলেক্ট করুন।");
                        return;
                    }
                }
            }


            if (secSyringe.getVisibility() == View.VISIBLE) {
                if (txtSyringeQty.getText().toString().length() == 0) {
                    Connection.MessageBox(ELCOForm.this, "সিরিঙ্গের পরিমান লিখুন");
                    txtSyringeQty.requestFocus();
                    return;
                }

            }
            if (!rdoMethodReceveDT1.isChecked() & !rdoMethodReceveDT2.isChecked() & secMethodReceveDT.isShown()) {
                Connection.MessageBox(ELCOForm.this, "পদ্ধতি গ্রহনের উৎস কি সিলেক্ট করুন।");
                rdoMethodReceveDT1.requestFocus();
                return;
            } else if (dtpMD.getText().toString().length() == 0 & secMethodReceveDT.isShown() & rdoMethodReceveDT1.isChecked()) {
                Connection.MessageBox(ELCOForm.this, "পদ্ধতি গ্রহনের তারিখ লিখুন ।");
                dtpMD.requestFocus();
                return;
            } else if (txtMDAge.getText().toString().length() == 0 & rdoMethodReceveDT2.isChecked() & secMethodReceveDT.isShown()) {
                Connection.MessageBox(ELCOForm.this, "পদ্ধতি গ্রহনের বয়স  কত লিখুন।");
                txtMDAge.requestFocus();
                return;
            }

            if (secValidity.getVisibility() == View.VISIBLE) {
                if (txtValidity.getText().toString().length() == 0) {
                    Connection.MessageBox(ELCOForm.this, " কার্যকারিতা লিখুন");
                    txtValidity.requestFocus();
                    return;
                } else if (spnValidityUnit.getSelectedItemPosition() == 0) {
                    Connection.MessageBox(ELCOForm.this, " ইউনিট লিখুন");
                    return;
                }
            }


            String CurStatus = Global.Left(spnMethod.getSelectedItem().toString(), 2);

            //Stock

            if (secMethodUnit.getVisibility() == View.VISIBLE) {

                if (txtMethodQty.getVisibility() == View.VISIBLE) {
                    if (txtMethodQty.getText().toString().length() == 0) {
                        Connection.MessageBox(ELCOForm.this, " পরিমান লিখুন");
                        return;
                    } else if (spnMethodUnit.getSelectedItemPosition() == 0) {
                        Connection.MessageBox(ELCOForm.this, " ইউনিট লিখুন");
                        return;
                    } else {
                        // Integer currentqty = Global.GetCurrentStockOfItem(C, g.getProvCode(), CurStatus);
                        if (CurStatus.equalsIgnoreCase("01")) {
                            if (Integer.parseInt(txtMethodQty.getText().toString()) > 3) {
                                Connection.MessageBox(ELCOForm.this, "ইস্যুর পরিমান 3টির  থেকে কম হবে। ");
                                return;
                            }
                        }

                    }

                }
            }
            //ELCO Visit
            String VisitNo = txtVisitNo.getText().toString();
            String sqlnew = "";
            String sqlupdate = "";
            String trnid = C.ReturnSingleValue("Select transactionId  from elcovisit Where HealthID='" + g.getGeneratedId() + "' and Visit='" + VisitNo + "'");
            String PrevItem = C.ReturnSingleValue("Select currStatus from elcovisit Where HealthID='" + g.getGeneratedId() + "' and Visit='" + VisitNo + "'");
            sqlupdate = "Update elcoVisit Set ";
            //sqlupdate+="vDate = '"+ Global.DateConvertYMD(dtpDOV.getText().toString()) +"',";
            sqlupdate += "visitStatus = '" + (rdoVSPresent.isChecked() ? "1" : "2") + "',";
            if (spnMethod.getSelectedItemPosition() == 0)
                sqlupdate += "currStatus = '',";
            else
                sqlupdate += "currStatus = '" + Global.Left(spnMethod.getSelectedItem().toString(), 2) + "',";
            //sqlupdate+="newOld = '"+ CurrentMethod +"',";
            //sqlupdate+="mDate = '"+ Global.DateConvertYMD(dtpDOV.getText().toString()) +"',";
            String SS = "";
            if (rdoSSFWA.isChecked()) SS = "1";
            if (rdoSSLMarket.isChecked()) SS = "2";
            if (rdoSSOther.isChecked()) SS = "3";
            if (rdoSSOtherGovt.isChecked()) SS = "4";
            sqlupdate += "sSource = '" + SS + "',";
            sqlupdate += "qty = '" + txtMethodQty.getText().toString() + "',";
            sqlupdate += "unit = '" + (spnMethodUnit.getSelectedItemPosition() == 0 ? "" : String.valueOf(spnMethodUnit.getSelectedItemPosition())) + "',";
            if (spnBrand.getSelectedItemPosition() == 0)
                sqlupdate += "brand = '',";
            else
                sqlupdate += "brand = '" + Global.Left(spnBrand.getSelectedItem().toString(), 2) + "',";
            //sqlupdate+="brand = '"+ Global.Left(spnBrand.getSelectedItem().toString(), 2) +"',";
            sqlupdate += "referPlace = '" + (spnWherePlace.getSelectedItemPosition() == 0 ? "" : String.valueOf(spnWherePlace.getSelectedItemPosition())) + "',";
            sqlupdate += "validity = '" + txtValidity.getText().toString() + "',";
            sqlupdate += "dayMonYear = '" + (spnValidityUnit.getSelectedItemPosition() == 0 ? "" : String.valueOf(spnValidityUnit.getSelectedItemPosition())) + "',";
            sqlupdate += "mrSource = '" + (rdoMethodReceveDT1.isChecked() ? "1" : (rdoMethodReceveDT2.isChecked() ? "2" : "")) + "',";//(rdoMethodReceveDT1.isChecked()?"1":"2")
            if (rdoMethodReceveDT1.isChecked()) {
                sqlupdate += "MRDate = '" + Global.DateConvertYMD(dtpMD.getText().toString()) + "',";
                sqlupdate += "MRAge = '" + Global.DateDifferenceMonth(Global.DateNowDMY(), dtpMD.getText().toString()) + "',";//DateDifferenceYears DateDifferenceMonth
            } else if (rdoMethodReceveDT2.isChecked()) {
                sqlupdate += "MRDate = '" + Global.addDaysYMD(Global.DateNowDMY(), -Integer.valueOf(txtMDAge.getText().toString()) * 30) + "',";
                sqlupdate += "MRAge = '" + txtMDAge.getText().toString() + "', ";
            } else {
                sqlupdate += "MRDate = '',";
                sqlupdate += "MRAge = '', ";
            }
            sqlupdate += "syrinsQty = '" + txtSyringeQty.getText().toString() + "',";
            if (spnSendForMethod.getSelectedItemPosition() == 0)
                sqlupdate += "sendForMethod = '',";
            else
                sqlupdate += "sendForMethod = '" + Global.Left(spnSendForMethod.getSelectedItem().toString(), 2) + "',";
            if (spnImpotent.getSelectedItemPosition() == 0)
                sqlupdate += "impotent = ''";
            else
                sqlupdate += "impotent = '" + Global.Left(spnImpotent.getSelectedItem().toString(), 2) + "'";
            sqlupdate += " Where HealthID='" + g.getGeneratedId() + "' and Visit='" + VisitNo + "'";

            if (!Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("12") && !Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("13"))
                C.Save(sqlupdate);
            else {
                sqlupdate = "Update elcoVisit Set pregNo = '" + PGNNo() + "' , ";
                //sqlupdate += "vDate = '" + Global.DateConvertYMD(dtpDOV.getText().toString()) + "',";//dtpDOV
                sqlupdate += "visitStatus = '" + (rdoVSPresent.isChecked() ? "1" : "2") + "',";
                if (spnMethod.getSelectedItemPosition() == 0)
                    sqlupdate += "currStatus = '',";
                else
                    sqlupdate += "currStatus = '" + Global.Left(spnMethod.getSelectedItem().toString(), 2) + "',";
                //sqlupdate += "newOld = '" + CurrentMethod + "',";
                //sqlupdate += "mDate = '" + Global.DateConvertYMD(dtpDOV.getText().toString()) + "',";
                SS = "";
                if (rdoSSFWA.isChecked()) SS = "1";
                if (rdoSSLMarket.isChecked()) SS = "2";
                if (rdoSSOther.isChecked()) SS = "3";
                if (rdoSSOtherGovt.isChecked()) SS = "4";
                sqlupdate += "sSource = '" + SS + "',";
                sqlupdate += "qty = '" + txtMethodQty.getText().toString() + "',";
                sqlupdate += "unit = '" + (spnMethodUnit.getSelectedItemPosition() == 0 ? "" : String.valueOf(spnMethodUnit.getSelectedItemPosition())) + "',";
                if (spnBrand.getSelectedItemPosition() == 0)
                    sqlupdate += "brand = '',";
                else
                    sqlupdate += "brand = '" + Global.Left(spnBrand.getSelectedItem().toString(), 2) + "',";
                //sqlupdate += "brand = '" + (spnBrand.getSelectedItemPosition() == 0 ? "" : Global.Left(spnBrand.getSelectedItem().toString(), 2)) + "',";
                sqlupdate += "referPlace = '" + (spnWherePlace.getSelectedItemPosition() == 0 ? "" : String.valueOf(spnWherePlace.getSelectedItemPosition())) + "',";
                sqlupdate += "validity = '" + txtValidity.getText().toString() + "',";
                sqlupdate += "dayMonYear = '" + (spnValidityUnit.getSelectedItemPosition() == 0 ? "" : String.valueOf(spnValidityUnit.getSelectedItemPosition())) + "',";
                sqlupdate += "mrSource = '" + (rdoMethodReceveDT1.isChecked() ? "1" : (rdoMethodReceveDT2.isChecked() ? "2" : "")) + "',";//(rdoMethodReceveDT1.isChecked()?"1":"2")
                if (rdoMethodReceveDT1.isChecked()) {
                    sqlupdate += "MRDate = '" + Global.DateConvertYMD(dtpMD.getText().toString()) + "',";
                    sqlupdate += "MRAge = '" + Global.DateDifferenceMonth(Global.DateNowDMY(), dtpMD.getText().toString()) + "',";//DateDifferenceYears DateDifferenceMonth
                } else if (rdoMethodReceveDT2.isChecked()) {
                    sqlupdate += "MRDate = '" + Global.addDaysYMD(Global.DateNowDMY(), -Integer.valueOf(txtMDAge.getText().toString()) * 30) + "',";
                    sqlupdate += "MRAge = '" + txtMDAge.getText().toString() + "', ";
                } else {
                    sqlupdate += "MRDate = '',";
                    sqlupdate += "MRAge = '', ";
                }
                sqlupdate += "syrinsQty = '" + txtSyringeQty.getText().toString() + "',";
                sqlupdate += "sendForMethod = '',";
                sqlupdate += "impotent = '',";
                sqlupdate += "modifyDate = '" + Global.DateTimeNowYMDHMS() + "'";
                sqlupdate += " Where HealthID='" + g.getGeneratedId() + "' and Visit='" + VisitNo + "'";
                g.setPregNo(PGNNo());
            }
            //String CurStatus = Global.Left(spnMethod.getSelectedItem().toString(), 2);

            if (secMethodUnit.getVisibility() == View.VISIBLE) {

                if (txtMethodQty.getVisibility() == View.VISIBLE) {
                    //Validation is not required here. Validation of current stock is handled above.
                                    /*if (!C.Existence("SELECT * FROM stockTransaction WHERE transactionType ='4' AND providerId ='" + g.getProvCode() + "' AND healthId = '" +
                                            g.getGeneratedId() + "' AND itemCode = '" + CurStatus + "' AND systemEntryDate ='" + Global.DateNowYMD() + "'"))*/
                    String TransactionSQL = "SELECT * FROM stockTransaction WHERE transactionType ='4' AND providerId ='" + g.getProvCode() + "' AND healthId = '" + g.getGeneratedId() + "'  AND systemEntryDate ='" + Global.DateConvertYMD(dtpDOV.getText().toString()) + "'and transactionId== '" + trnid + "'";
                    if (C.Existence(TransactionSQL)) {
                        if (CurStatus.equalsIgnoreCase("01")) {

                            //Suki
                            if (Global.Left(spnBrand.getSelectedItem().toString(), 2).equalsIgnoreCase("01")) {
                                UpdateStockAndItem(trnid, g.getProvCode(), PrevItem, Global.Left(spnMethod.getSelectedItem().toString(), 2).toString(), txtMethodQty.getText().toString());
                            }
                            //Apon
                            else if (Global.Left(spnBrand.getSelectedItem().toString(), 2).equalsIgnoreCase("02")) {
                                UpdateStockAndItem(trnid, g.getProvCode(), PrevItem, Global.Left(spnMethod.getSelectedItem().toString(), 2).toString(), txtMethodQty.getText().toString());
                            }
                        } else if (CurStatus.equalsIgnoreCase("02") | CurStatus.equalsIgnoreCase("08") || CurStatus.equalsIgnoreCase("09")) {
                            UpdateStockAndItem(trnid, g.getProvCode(), PrevItem, Global.Left(spnMethod.getSelectedItem().toString(), 2).toString(), txtMethodQty.getText().toString());
                        } else if (CurStatus.equalsIgnoreCase("03")) { //Injectable
                            UpdateStockAndItem(trnid, g.getProvCode(), PrevItem, Global.Left(spnMethod.getSelectedItem().toString(), 2).toString(), txtMethodQty.getText().toString());
                        }

                    } else {
                        if (CurStatus.equalsIgnoreCase("01")) {

                            //Suki
                            if (Global.Left(spnBrand.getSelectedItem().toString(), 2).equalsIgnoreCase("01")) {
                                String trnsactionid = Global.GenerateProviderId(g.getProvCode());
                                InsertTransactionUnstockToStock(CurStatus, g.getProvCode(), txtMethodQty.getText().toString(), trnsactionid);
                            }
                            //Apon
                            else if (Global.Left(spnBrand.getSelectedItem().toString(), 2).equalsIgnoreCase("02")) {
                                String trnsactionid = Global.GenerateProviderId(g.getProvCode());
                                InsertTransactionUnstockToStock("10", g.getProvCode(), txtMethodQty.getText().toString(), trnsactionid);
                            }
                        } else if (CurStatus.equalsIgnoreCase("02") | CurStatus.equalsIgnoreCase("08") || CurStatus.equalsIgnoreCase("09")) {
                            String trnsactionid = Global.GenerateProviderId(g.getProvCode());
                            InsertTransactionUnstockToStock(CurStatus, g.getProvCode(), txtMethodQty.getText().toString(), trnsactionid);
                        } else if (CurStatus.equalsIgnoreCase("03")) { //Injectable
                            String trnsactionid = Global.GenerateProviderId(g.getProvCode());
                            InsertTransactionUnstockToStock(CurStatus, g.getProvCode(), txtMethodQty.getText().toString(), trnsactionid);
                            trnid = String.valueOf(Long.parseLong(Global.GenerateProviderId(g.getProvCode())) + 1);
                            InsertTransactionUnstockToStock("5", g.getProvCode(), txtMethodQty.getText().toString(), trnid);
                        }
                    }
                }
            } else {
                if (PrevItem.equalsIgnoreCase("01") | PrevItem.equalsIgnoreCase("02") | PrevItem.equalsIgnoreCase("03") | PrevItem.equalsIgnoreCase("08") | PrevItem.equalsIgnoreCase("09")) {
                    UpdateStockZeroAndItem(trnid, PrevItem, txtMethodQty.getText().toString());
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
        } catch (Exception e) {
            Log.e("Error from ELCO", e.getMessage());
            Connection.MessageBox(ELCOForm.this, e.getMessage());
            return;
        }

    }

    private void DataSave() {
        try {

            //ELCO Profile


            if (txtELCONo.getText().toString().length() == 0) {
                Connection.MessageBox(ELCOForm.this, "দম্পতি নং কত লিখুন।");
                txtELCONo.requestFocus();
                return;
            }

            if (!rdoELCOStatus1.isChecked() & !rdoELCOStatus2.isChecked()) {
                Connection.MessageBox(ELCOForm.this, "দম্পতির অবস্থা কি");
                rdoELCOStatus1.requestFocus();
                return;
            }

            if (!chkHHNoDontKnow.isChecked()) {
                if (txtHHNo.getText().toString().length() == 0) {
                    Connection.MessageBox(ELCOForm.this, "খানা নম্বর কত লিখুন।");
                    txtHHNo.requestFocus();
                    return;
                }
            }

            if (dtpDOM.getText().toString().length() == 0 & dtpDOM.isShown() & rdoDOMSource1.isChecked()) {
                Connection.MessageBox(ELCOForm.this, "প্রকৃত বিবাহের তারিখ কত লিখুন।");
                dtpDOM.requestFocus();
                return;
            } else if (txtMAge.getText().toString().length() == 0 & rdoDOMSource2.isChecked()) {
                Connection.MessageBox(ELCOForm.this, "বিবাহের বয়স  কত লিখুন।");
                txtMAge.requestFocus();
                return;
            } else if (txtLiveSon.getText().toString().length() == 0) {
                Connection.MessageBox(ELCOForm.this, "জীবিত ছেলের সংখ্যা লিখুন।");
                txtLiveSon.requestFocus();
                return;
            } else if (txtLiveDau.getText().toString().length() == 0) {
                Connection.MessageBox(ELCOForm.this, "জীবিত মেয়ের সংখ্যা লিখুন।");
                txtLiveDau.requestFocus();
                return;
            }

            if (!rdoVSPresent.isChecked() & !rdoVSAbsent.isChecked()) {
                Connection.MessageBox(ELCOForm.this, "পরিদর্শনের অবস্থা সিলেক্ট করুন।");
                rdoVSPresent.requestFocus();
                return;
            }
            if (rdoVSPresent.isChecked() & spnMethod.getSelectedItemPosition() == 0 & secMethod.isShown()) {
                Connection.MessageBox(ELCOForm.this, "জন্মনিয়ন্ত্রণ ব্যবস্থা/গর্ভাবস্থা সিলেক্ট করুন।।");
                rdoVSPresent.requestFocus();
                return;
            }

            if (validateVisitDateAgainstSystemDate()) {
                Connection.MessageBox(ELCOForm.this, " ভিজিট এর তারিখ আজকের তারিখ এর সমান অথবা কম হতে হবে।");
                return;
            }
            String PrevItem = C.ReturnSingleValue("Select currStatus from elcovisit Where HealthID='" + g.getGeneratedId() + "' and Visit='" + txtVisitNo.getText().toString() + "'");
            if (!PrevItem.equalsIgnoreCase("")) {
                if (Global.DateDifferenceDays(dtpDOV.getText().toString(), Global.DateConvertDMY(GetDOV(g.getGeneratedId()))) == 0 & IsItemSame(Global.Left(spnMethod.getSelectedItem().toString(), 2).toString(), txtVisitNo.getText().toString())) {
                    SameDateDifferentItem();
                    Connection.MessageBox(ELCOForm.this, "তথ্য সফলভাবে সম্পাদন করা হয়েছে।");
                    if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("12")) {
                        Intent f2 = new Intent(getApplicationContext(), FWAReg.class);
                        g.setAMElco(txtELCONo.getText().toString());
                        g.setAAge(txtAge.getText().toString());
                        Bundle IDbundle = new Bundle();
                        String sqlnew = "";
                        String sqlupdate = "";
                        if (sqlnew.length() > 0)
                            IDbundle.putString("sqlnew", sqlnew);
                        if (sqlupdate.length() > 0)
                            IDbundle.putString("sqlupdate", sqlupdate);

                        f2.putExtras(IDbundle);

                        startActivity(f2);
                        finish();
                        return;
                    } else {
                        if (g.getCallFrom().equals("HAregis")) {
                            finish();
                            Intent f2 = new Intent(getApplicationContext(), Elcoview.class);
                            startActivity(f2);
                            return;
                        } else {
                            finish();
                            Intent f2 = new Intent(getApplicationContext(), MemberList.class);
                            startActivity(f2);
                            return;
                        }
                    }
                }
            }

            if (GetDOV(g.getGeneratedId()).equals("")) {

            } else {//if(CurrentStatus(txtHealthID.getText().toString()).equals("12"))
                String status = CurrentStatusValidate(g.getGeneratedId());
                String spinnervalue = Global.Left(spnMethod.getSelectedItem().toString(), 2);

                if ((status.equalsIgnoreCase(spinnervalue) && spinnervalue.equalsIgnoreCase("01") |
                        (status.equalsIgnoreCase(spinnervalue) && spinnervalue.equalsIgnoreCase("02")) |
                        (status.equalsIgnoreCase(spinnervalue) && spinnervalue.equalsIgnoreCase("03")) |
                        (status.equalsIgnoreCase(spinnervalue) && spinnervalue.equalsIgnoreCase("08")) |
                        (status.equalsIgnoreCase(spinnervalue) && spinnervalue.equalsIgnoreCase("09"))) && Global.DateDifferenceDays(dtpDOV.getText().toString(), Global.DateConvertDMY(GetDOV(g.getGeneratedId()))) <= 0) {

                    if (spinnervalue.equalsIgnoreCase("01")) {

                        if (secBrand.getVisibility() == View.VISIBLE) {
                            if (spnBrand.getSelectedItemPosition() == 0) {
                                Connection.MessageBox(ELCOForm.this, " ব্র্যান্ড লিখুন");
                                return;
                            }
                        }

                        if (secMethodUnit.getVisibility() == View.VISIBLE) {
                            if (txtMethodQty.getVisibility() == View.VISIBLE) {
                                if (txtMethodQty.getText().toString().length() == 0) {
                                    Connection.MessageBox(ELCOForm.this, " পরিমান লিখুন");
                                    return;
                                }

                            }
                        }
                        //Suki
                        if (Global.Left(spnBrand.getSelectedItem().toString(), 2).equalsIgnoreCase("01")) {
                            AdjustStockQty(txtMethodQty.getText().toString(), Global.Left(spnMethod.getSelectedItem().toString(), 2).toString(), g.getProvCode());
                        }
                        //Apon
                        else if (Global.Left(spnBrand.getSelectedItem().toString(), 2).equalsIgnoreCase("02")) {
                            AdjustStockQty(txtMethodQty.getText().toString(), "10", g.getProvCode());
                        }

                    } else {
                       /*if(IsItemSame(Global.Left(spnMethod.getSelectedItem().toString(), 2).toString(),txtVisitNo.getText().toString()))
                       {
                           SameDateDifferentItem();
                       }
                        else
                       {
                           AdjustStockQty(txtMethodQty.getText().toString(), Global.Left(spnMethod.getSelectedItem().toString(), 2).toString(), g.getProvCode());
                       }*/
                        AdjustStockQty(txtMethodQty.getText().toString(), Global.Left(spnMethod.getSelectedItem().toString(), 2).toString(), g.getProvCode());

                    }
                    if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("03")) {
                        this.InsertInjectableToWomanInjectableTable(Global.DateConvertYMD(dtpMD.getText().toString()));
                    }
                    Connection.MessageBox(ELCOForm.this, "তথ্য সফলভাবে সংরক্ষণ করা হয়েছে।");
                    if (g.getCallFrom().equals("HAregis")) {
                        finish();
                        Intent f2 = new Intent(getApplicationContext(), Elcoview.class);
                        startActivity(f2);
                        return;
                    } else {
                        finish();
                        Intent f2 = new Intent(getApplicationContext(), MemberList.class);
                        startActivity(f2);
                        return;
                    }

                }
                    /*  else if (Global.DateDifferenceDays(dtpDOV.getText().toString(), Global.DateConvertDMY(GetDOV(g.getGeneratedId()))) <= 0) {

                    Connection.MessageBox(ELCOForm.this, " ভিজিট এর তারিখ পূর্বের ভিজিট তারিখ এর  বেশি  হতে হবে");
                    return;
                }*/


            }
            if (validateMarriageDateAgainstSystemDate()) {
                Connection.MessageBox(ELCOForm.this, " বিবাহের এর তারিখ আজকের তারিখ এর সমান অথবা কম হতে হবে।");
                return;
            }
            if (validateMethodDateAgainstMarriageDate()) {
                Connection.MessageBox(ELCOForm.this, " ভিজিট এর তারিখ বিবাহের তারিখ এর সমান অথবা বেশি  হতে হবে।");
                return;
            }

            if (secBrand.getVisibility() == View.VISIBLE) {
                if (spnBrand.getSelectedItemPosition() == 0) {
                    Connection.MessageBox(ELCOForm.this, " ব্র্যান্ড লিখুন");
                    return;
                }
            }

            if (secMethodUnit.getVisibility() == View.VISIBLE) {
                if (txtMethodQty.getVisibility() == View.VISIBLE) {
                    if (txtMethodQty.getText().toString().length() == 0) {
                        Connection.MessageBox(ELCOForm.this, " পরিমান লিখুন");
                        return;
                    }

                }
            }

            if (secWherePlace.getVisibility() == View.VISIBLE) {
                if (spnWherePlace.getSelectedItemPosition() == 0) {
                    Connection.MessageBox(ELCOForm.this, " কোথায় প্রেরন করা হয়েছে  লিখুন");
                    return;
                }
            }

            if (secSendForMethod.getVisibility() == View.VISIBLE) {
                if (spnSendForMethod.getSelectedItemPosition() == 0) {
                    Connection.MessageBox(ELCOForm.this, "কোন পদ্ধতি জন্য প্রেরন  লিখুন");
                    return;
                }
            }

            if (secImpotent.getVisibility() == View.VISIBLE) {
                if (spnImpotent.getSelectedItemPosition() == 0) {
                    Connection.MessageBox(ELCOForm.this, "কত তম বন্ধ্যা সিলেক্ট করুন");
                    return;
                }
            }

            if (secSS.getVisibility() == View.VISIBLE) {
                if (rdogrpSS.getVisibility() == View.VISIBLE) {

                    Integer source = rdogrpSS.getCheckedRadioButtonId();
                    if (source == null) {
                        Connection.MessageBox(ELCOForm.this, " উৎস সিলেক্ট করুন।");
                        return;
                    }

                    if (source == -1) {
                        Connection.MessageBox(ELCOForm.this, " উৎস সিলেক্ট করুন।");
                        return;
                    }
                }
            }
            /*if(secMethodReceveDT.getVisibility() == View.VISIBLE & secValidity.getVisibility()==View.VISIBLE ) {
                if (dtpMD.getText().toString().length() == 0 | txtValidity.getText().toString().length() == 0|spnValidityUnit.getSelectedItemPosition() == 0) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(ELCOForm.this);
                    if(dtpMD.getText().toString().length() == 0) {
                        adb.setTitle("Close");
                        adb.setMessage("আপনি কি পদ্ধতি গ্রহনের তারিখ ছাড়াই ভিজিট সম্পূর্ণ  করতে চান[হাঁ/না]");
                        adb.setNegativeButton("না", new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (dtpMD.getText().toString().length() == 0) {
                                    Connection.MessageBox(ELCOForm.this, " পদ্ধতি গ্রহনের তারিখ লিখুন");
                                    return;
                                }
                            }
                        });
                    }
                    else  if(txtValidity.getText().toString().length() == 0) {
                        adb.setTitle("Close");
                        adb.setMessage("আপনি কি কার্যকারিতা ছাড়াই ভিজিট সম্পূর্ণ  করতে চান[হাঁ/না]");
                        adb.setNegativeButton("না", new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (txtValidity.getText().toString().length() == 0) {
                                    Connection.MessageBox(ELCOForm.this, " কার্যকারিতা লিখুন");
                                    return;
                                }
                            }
                        });
                    }
                    else  if(spnValidityUnit.getSelectedItemPosition() == 0) {
                        adb.setTitle("Close");
                        adb.setMessage("আপনি কি ইউনিট ছাড়াই ভিজিট সম্পূর্ণ  করতে চান[হাঁ/না]");
                        adb.setNegativeButton("না", new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (spnValidityUnit.getSelectedItemPosition() == 0) {
                                    Connection.MessageBox(ELCOForm.this, " ইউনিট লিখুন");
                                    return;
                                }
                            }
                        });
                    }
                    adb.setPositiveButton("হাঁ", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            spnValidityUnit.setSelection(0);
                            ELCOSave();
                        }
                    });
                    adb.show();
                    return;
                }
            }*/

            if (secSyringe.getVisibility() == View.VISIBLE) {
                if (txtSyringeQty.getText().toString().length() == 0) {
                    Connection.MessageBox(ELCOForm.this, "সিরিঙ্গের পরিমান লিখুন");
                    txtSyringeQty.requestFocus();
                    return;
                }

            }
            if (!rdoMethodReceveDT1.isChecked() & !rdoMethodReceveDT2.isChecked() & secMethodReceveDT.isShown()) {
                Connection.MessageBox(ELCOForm.this, "পদ্ধতি গ্রহনের উৎস কি সিলেক্ট করুন।");
                rdoMethodReceveDT1.requestFocus();
                return;
            } else if (dtpMD.getText().toString().length() == 0 & secMethodReceveDT.isShown() & rdoMethodReceveDT1.isChecked()) {
                Connection.MessageBox(ELCOForm.this, "পদ্ধতি গ্রহনের তারিখ লিখুন ।");
                dtpMD.requestFocus();
                return;
            } else if (txtMDAge.getText().toString().length() == 0 & rdoMethodReceveDT2.isChecked() & secMethodReceveDT.isShown()) {
                Connection.MessageBox(ELCOForm.this, "পদ্ধতি গ্রহনের বয়স  কত লিখুন।");
                txtMDAge.requestFocus();
                return;
            }

            if (secValidity.getVisibility() == View.VISIBLE) {
                if (txtValidity.getText().toString().length() == 0) {
                    Connection.MessageBox(ELCOForm.this, " কার্যকারিতা লিখুন");
                    txtValidity.requestFocus();
                    return;
                } else if (spnValidityUnit.getSelectedItemPosition() == 0) {
                    Connection.MessageBox(ELCOForm.this, " ইউনিট লিখুন");
                    return;
                }
            }


            String CurStatus = Global.Left(spnMethod.getSelectedItem().toString(), 2);

            //Stock

            if (secMethodUnit.getVisibility() == View.VISIBLE) {

                if (txtMethodQty.getVisibility() == View.VISIBLE) {
                    if (txtMethodQty.getText().toString().length() == 0) {
                        Connection.MessageBox(ELCOForm.this, " পরিমান লিখুন");
                        return;
                    } else if (spnMethodUnit.getSelectedItemPosition() == 0) {
                        Connection.MessageBox(ELCOForm.this, " ইউনিট লিখুন");
                        return;
                    } else {
                        // Integer currentqty = Global.GetCurrentStockOfItem(C, g.getProvCode(), CurStatus);
                        if (CurStatus.equalsIgnoreCase("01")) {
                            if (Integer.parseInt(txtMethodQty.getText().toString()) > 3) {
                                Connection.MessageBox(ELCOForm.this, "ইস্যুর পরিমান 3টির  থেকে কম হবে। ");
                                return;
                            }
                        }

                    }

                }
            }


            String SQL = "";

            //ELCO Registation
            //------------------------------------------------------------------------------------------------------------------------
            if (!C.Existence("Select healthid from " + TableName + "  Where healthid='" + g.getGeneratedId() + "'")) {
                SQL = "Insert into " + TableName + "(healthId,providerId,regDT,systemEntryDate,upload)Values(";
                SQL += "'" + g.getGeneratedId() + "',";
                SQL += "'" + g.getProvCode() + "',";
                SQL += "'" + Global.DateNowYMD() + "',";
                SQL += "'" + Global.DateTimeNowYMDHMS() + "',";
                SQL += "'2')";

                C.Save(SQL);
            }

            SQL = "Update " + TableName + " Set ";
            SQL += "hhStatus = '" + (chkHHNoDontKnow.isChecked() ? "1" : "2") + "',";
            SQL += "haHHNo = '" + txtHHNo.getText().toString() + "',";
            SQL += "elcoNo = '" + txtELCONo.getText().toString() + "',";
            SQL += "acceptorStatus  = '" + (rdoELCOStatus1.isChecked() ? "1" : "2") + "',";
            SQL += "husbandName = '" + txtHusName.getText().toString() + "',";
            SQL += "husbandAge = '" + txtHusAge.getText().toString() + "',";
            SQL += "DOMSource = '" + (rdoDOMSource1.isChecked() ? "1" : "2") + "',";

            if (rdoDOMSource1.isChecked()) {
                SQL += "MarrDate = '" + Global.DateConvertYMD(dtpDOM.getText().toString()) + "',";
                SQL += "MarrAge = '" + Global.DateDifferenceYears(Global.DateNowDMY(), dtpDOM.getText().toString()) + "',";
            } else {
                SQL += "MarrDate = '" + Global.addDaysYMD(Global.DateNowDMY(), -Integer.valueOf(txtMAge.getText().toString()) * 365) + "',";
                SQL += "MarrAge = '" + txtMAge.getText().toString() + "',";
            }

            SQL += "Son = '" + txtLiveSon.getText().toString() + "',";
            SQL += "Dau = '" + txtLiveDau.getText().toString() + "',";
            SQL += "modifyDate = '" + Global.DateTimeNowYMDHMS() + "'";
            SQL += " Where HealthID='" + g.getGeneratedId() + "'";
            C.Save(SQL);

            if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("03")) {
                this.InsertInjectableToWomanInjectableTable(Global.DateConvertYMD(dtpMD.getText().toString()));
            }

            //ELCO Visit
            //When 12 14 come then handale another way.(Pandding)
            String SQ1 = "";
            SQ1 = "select CurrStatus||'^'||NewOld from ELCOVisit Where healthid='" + g.getGeneratedId() + "'";//and Visit='"+ txtVisitNo.getText().toString() +"'
            SQ1 += " order by systemEntryDate desc limit 1";
            String PerviousStatus = C.ReturnSingleValue(SQ1);
            String[] PerviousMethods = Connection.split(PerviousStatus, '^');
            String PreMethodStatus = PerviousMethods[0].toString();
            // String PerviousMethodSign = PerviousMethods[1].toString();
            /*
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

            */
            String CurrentMethod = "";
            if (GetDOV(g.getGeneratedId()).equals("")) {

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
            } else if (Global.DateDifferenceDays(dtpDOV.getText().toString(), Global.DateConvertDMY(GetDOV(g.getGeneratedId()))) <= 0) {
                String VisitNo = txtVisitNo.getText().toString();
                String ExistMethodStatus = C.ReturnSingleValue("Select newOld  from elcovisit Where HealthID='" + g.getGeneratedId() + "' and Visit='" + VisitNo + "'");
                CurrentMethod = ExistMethodStatus;
            } else if (!GetDOV(g.getGeneratedId()).equals("")) {
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
            String trnid = Global.GenerateProviderId(g.getProvCode());
            //if(CurStatus.equalsIgnoreCase("01") |CurStatus.equalsIgnoreCase("02") | CurStatus.equalsIgnoreCase("03") |CurStatus.equalsIgnoreCase("08") || CurStatus.equalsIgnoreCase("09")) {
            //if(!C.Existence("Select healthid from elcoVisit  Where healthid='"+ g.getGeneratedId() +"' and Visit='"+ VisitNo +"' and  currStatus = '"+ Global.Left(spnMethod.getSelectedItem().toString(), 2) +"'"))
            if (!C.Existence("Select healthid from elcoVisit  Where healthid='" + g.getGeneratedId() + "' and Visit='" + VisitNo + "'")) {
                sqlnew = "Insert into elcoVisit(healthId,providerId,visit,systemEntryDate,upload,transactionId)Values(";
                sqlnew += "'" + g.getGeneratedId() + "',";
                sqlnew += "'" + g.getProvCode() + "',";
                sqlnew += "'" + VisitNo + "',";
                sqlnew += "'" + Global.DateTimeNowYMDHMS() + "',";
                sqlnew += "'2',";
                sqlnew += "'" + trnid + "')";

                if (!Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("12") & !Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("04") & !Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("05") & !Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("18") & !Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("15") & !Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("16") & !Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("06") & !Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("07") & !Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("17") & !Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("11") & !Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("10")) {
                    if (txtMethodQty.getVisibility() == View.VISIBLE & secMethodUnit.isShown()) {
                        C.Save(sqlnew);
                    } else {
                        sqlnew = "Insert into elcoVisit(healthId,providerId,visit,systemEntryDate,upload)Values(";
                        sqlnew += "'" + g.getGeneratedId() + "',";
                        sqlnew += "'" + g.getProvCode() + "',";
                        sqlnew += "'" + VisitNo + "',";
                        sqlnew += "'" + Global.DateTimeNowYMDHMS() + "',";
                        sqlnew += "'2')";
                        C.Save(sqlnew);
                    }
                } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("04") | Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("05") | Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("18") | Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("15") | Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("16") | Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("06") | Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("07") | Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("17") | Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("11") | Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("10")) {
                    sqlnew = "Insert into elcoVisit(healthId,providerId,visit,systemEntryDate,upload)Values(";
                    sqlnew += "'" + g.getGeneratedId() + "',";
                    sqlnew += "'" + g.getProvCode() + "',";
                    sqlnew += "'" + VisitNo + "',";
                    sqlnew += "'" + Global.DateTimeNowYMDHMS() + "',";
                    sqlnew += "'2')";
                    //sqlnew += "'" + trnid + "')";
                    C.Save(sqlnew);
                } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("12")) {
                    sqlnew = "Insert into elcoVisit(healthId,providerId,visit,systemEntryDate,upload,pregNo)Values(";
                    sqlnew += "'" + g.getGeneratedId() + "',";
                    sqlnew += "'" + g.getProvCode() + "',";
                    sqlnew += "'" + VisitNo + "',";
                    sqlnew += "'" + Global.DateTimeNowYMDHMS() + "',";
                    sqlnew += "'2',";
                    sqlnew += "'" + PGNNo() + "')";
                    g.setPregNo(PGNNo());
                }
            }
            //}

            sqlupdate = "Update elcoVisit Set ";
            sqlupdate += "vDate = '" + Global.DateConvertYMD(dtpDOV.getText().toString()) + "',";
            sqlupdate += "visitStatus = '" + (rdoVSPresent.isChecked() ? "1" : "2") + "',";
            if (spnMethod.getSelectedItemPosition() == 0)
                sqlupdate += "currStatus = '',";
            else
                sqlupdate += "currStatus = '" + Global.Left(spnMethod.getSelectedItem().toString(), 2) + "',";
            sqlupdate += "newOld = '" + CurrentMethod + "',";
            sqlupdate += "mDate = '" + Global.DateConvertYMD(dtpDOV.getText().toString()) + "',";
            String SS = "";
            if (rdoSSFWA.isChecked()) SS = "1";
            if (rdoSSLMarket.isChecked()) SS = "2";
            if (rdoSSOther.isChecked()) SS = "3";
            if (rdoSSOtherGovt.isChecked()) SS = "4";
            sqlupdate += "sSource = '" + SS + "',";
            sqlupdate += "qty = '" + txtMethodQty.getText().toString() + "',";
            sqlupdate += "unit = '" + (spnMethodUnit.getSelectedItemPosition() == 0 ? "" : String.valueOf(spnMethodUnit.getSelectedItemPosition())) + "',";
            if (spnBrand.getSelectedItemPosition() == 0)
                sqlupdate += "brand = '',";
            else
                sqlupdate += "brand = '" + Global.Left(spnBrand.getSelectedItem().toString(), 2) + "',";
            //sqlupdate+="brand = '"+ Global.Left(spnBrand.getSelectedItem().toString(), 2) +"',";
            sqlupdate += "referPlace = '" + (spnWherePlace.getSelectedItemPosition() == 0 ? "" : String.valueOf(spnWherePlace.getSelectedItemPosition())) + "',";
            sqlupdate += "validity = '" + txtValidity.getText().toString() + "',";
            sqlupdate += "dayMonYear = '" + (spnValidityUnit.getSelectedItemPosition() == 0 ? "" : String.valueOf(spnValidityUnit.getSelectedItemPosition())) + "',";
            sqlupdate += "mrSource = '" + (rdoMethodReceveDT1.isChecked() ? "1" : (rdoMethodReceveDT2.isChecked() ? "2" : "")) + "',";//(rdoMethodReceveDT1.isChecked()?"1":"2")
            if (rdoMethodReceveDT1.isChecked()) {
                sqlupdate += "MRDate = '" + Global.DateConvertYMD(dtpMD.getText().toString()) + "',";
                sqlupdate += "MRAge = '" + Global.DateDifferenceMonth(Global.DateNowDMY(), dtpMD.getText().toString()) + "',";//DateDifferenceYears DateDifferenceMonth
            } else if (rdoMethodReceveDT2.isChecked()) {
                sqlupdate += "MRDate = '" + Global.addDaysYMD(Global.DateNowDMY(), -Integer.valueOf(txtMDAge.getText().toString()) * 30) + "',";
                sqlupdate += "MRAge = '" + txtMDAge.getText().toString() + "', ";
            } else {
                sqlupdate += "MRDate = '',";
                sqlupdate += "MRAge = '', ";
            }
            sqlupdate += "syrinsQty = '" + txtSyringeQty.getText().toString() + "',";
            if (spnSendForMethod.getSelectedItemPosition() == 0)
                sqlupdate += "sendForMethod = '',";
            else
                sqlupdate += "sendForMethod = '" + Global.Left(spnSendForMethod.getSelectedItem().toString(), 2) + "',";
            if (spnImpotent.getSelectedItemPosition() == 0)
                sqlupdate += "impotent = ''";
            else
                sqlupdate += "impotent = '" + Global.Left(spnImpotent.getSelectedItem().toString(), 2) + "'";
            sqlupdate += " Where HealthID='" + g.getGeneratedId() + "' and Visit='" + VisitNo + "'";

            if (!Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("12") && !Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("13"))
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
                if (spnBrand.getSelectedItemPosition() == 0)
                    sqlupdate += "brand = '',";
                else
                    sqlupdate += "brand = '" + Global.Left(spnBrand.getSelectedItem().toString(), 2) + "',";
                //sqlupdate += "brand = '" + (spnBrand.getSelectedItemPosition() == 0 ? "" : Global.Left(spnBrand.getSelectedItem().toString(), 2)) + "',";
                sqlupdate += "referPlace = '" + (spnWherePlace.getSelectedItemPosition() == 0 ? "" : String.valueOf(spnWherePlace.getSelectedItemPosition())) + "',";
                sqlupdate += "validity = '" + txtValidity.getText().toString() + "',";
                sqlupdate += "dayMonYear = '" + (spnValidityUnit.getSelectedItemPosition() == 0 ? "" : String.valueOf(spnValidityUnit.getSelectedItemPosition())) + "',";
                sqlupdate += "mrSource = '" + (rdoMethodReceveDT1.isChecked() ? "1" : (rdoMethodReceveDT2.isChecked() ? "2" : "")) + "',";//(rdoMethodReceveDT1.isChecked()?"1":"2")
                if (rdoMethodReceveDT1.isChecked()) {
                    sqlupdate += "MRDate = '" + Global.DateConvertYMD(dtpMD.getText().toString()) + "',";
                    sqlupdate += "MRAge = '" + Global.DateDifferenceMonth(Global.DateNowDMY(), dtpMD.getText().toString()) + "',";//DateDifferenceYears DateDifferenceMonth
                } else if (rdoMethodReceveDT2.isChecked()) {
                    sqlupdate += "MRDate = '" + Global.addDaysYMD(Global.DateNowDMY(), -Integer.valueOf(txtMDAge.getText().toString()) * 30) + "',";
                    sqlupdate += "MRAge = '" + txtMDAge.getText().toString() + "', ";
                } else {
                    sqlupdate += "MRDate = '',";
                    sqlupdate += "MRAge = '', ";
                }
                sqlupdate += "syrinsQty = '" + txtSyringeQty.getText().toString() + "',";
                sqlupdate += "sendForMethod = '',";
                sqlupdate += "impotent = '',";
                sqlupdate += "modifyDate = '" + Global.DateTimeNowYMDHMS() + "'";
                sqlupdate += " Where HealthID='" + g.getGeneratedId() + "' and Visit='" + VisitNo + "'";
                g.setPregNo(PGNNo());
            }

            CurStatus = Global.Left(spnMethod.getSelectedItem().toString(), 2);

            if (secMethodUnit.getVisibility() == View.VISIBLE) {

                if (txtMethodQty.getVisibility() == View.VISIBLE) {
                    //Validation is not required here. Validation of current stock is handled above.
                                /*if (!C.Existence("SELECT * FROM stockTransaction WHERE transactionType ='4' AND providerId ='" + g.getProvCode() + "' AND healthId = '" +
                                        g.getGeneratedId() + "' AND itemCode = '" + CurStatus + "' AND systemEntryDate ='" + Global.DateNowYMD() + "'"))*/
                    if (!C.Existence("SELECT * FROM stockTransaction WHERE transactionType ='4' AND providerId ='" + g.getProvCode() + "' AND healthId = '" +
                            g.getGeneratedId() + "' AND itemCode = '" + CurStatus + "' AND systemEntryDate ='" + Global.DateConvertYMD(dtpDOV.getText().toString()) + "'")) {
                        if (CurStatus.equalsIgnoreCase("01")) {

                            //Suki
                            if (Global.Left(spnBrand.getSelectedItem().toString(), 2).equalsIgnoreCase("01")) {
                                InsertTransaction(CurStatus, g.getProvCode(), txtMethodQty.getText().toString(), trnid);
                                UpdateCurrentStock(g.getProvCode(), Global.Left(spnMethod.getSelectedItem().toString(), 2).toString(), txtMethodQty.getText().toString());
                            }
                            //Apon
                            else if (Global.Left(spnBrand.getSelectedItem().toString(), 2).equalsIgnoreCase("02")) {
                                InsertTransaction("10", g.getProvCode(), txtMethodQty.getText().toString(), trnid);
                                UpdateCurrentStock(g.getProvCode(), "10", txtMethodQty.getText().toString());
                            }
                        } else if (CurStatus.equalsIgnoreCase("02") | CurStatus.equalsIgnoreCase("08") || CurStatus.equalsIgnoreCase("09")) {
                            InsertTransaction(CurStatus, g.getProvCode(), txtMethodQty.getText().toString(), trnid);
                            UpdateCurrentStock(g.getProvCode(), Global.Left(spnMethod.getSelectedItem().toString(), 2).toString(), txtMethodQty.getText().toString());
                        } else if (CurStatus.equalsIgnoreCase("03")) { //Injectable
                            InsertTransaction(CurStatus, g.getProvCode(), txtMethodQty.getText().toString(), trnid);
                            UpdateCurrentStock(g.getProvCode(), Global.Left(spnMethod.getSelectedItem().toString(), 2).toString(), txtMethodQty.getText().toString());
                            trnid = String.valueOf(Long.parseLong(Global.GenerateProviderId(g.getProvCode())) + 1);
                            InsertTransaction("5", g.getProvCode(), txtMethodQty.getText().toString(), trnid);
                            UpdateCurrentStock(g.getProvCode(), "5", txtMethodQty.getText().toString());
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
            Connection.MessageBox(ELCOForm.this, "তথ্য সফলভাবে সংরক্ষণ করা হয়েছে।");
            //pregnant
            if (CurStatus.equalsIgnoreCase("12")) {
                Intent f2 = new Intent(getApplicationContext(), FWAReg.class);
                //add nisan
                g.setAMElco(txtELCONo.getText().toString());
                g.setAAge(txtAge.getText().toString());
                Bundle IDbundle = new Bundle();

                if (sqlnew.length() > 0)
                    IDbundle.putString("sqlnew", sqlnew);
                if (sqlupdate.length() > 0)
                    IDbundle.putString("sqlupdate", sqlupdate);

                f2.putExtras(IDbundle);

                startActivity(f2);
                finish();
            } else {
                if (g.getCallFrom().equals("HAregis")) {
                    finish();
                    Intent f2 = new Intent(getApplicationContext(), Elcoview.class);
                    startActivity(f2);
                } else {
                    finish();
                    Intent f2 = new Intent(getApplicationContext(), MemberList.class);
                    startActivity(f2);
                }
            }

        } catch (Exception e) {
            Connection.MessageBox(ELCOForm.this, e.getMessage());
            return;
        }
    }


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
        PGNNo = Global.Right(("00" + C.ReturnSingleValue(SQL)), 2);
        return PGNNo;
    }

    private String LastPGNNo() {
        String SQL = "";
        String PGNNo = "";

        SQL = "Select (ifnull(max(cast(PregNo as int)),0))MaxPGNNo from PregWomen WHERE healthId = " + g.getGeneratedId();
        PGNNo = Global.Right(("00" + C.ReturnSingleValue(SQL)), 2);
        return PGNNo;
    }

    private void InsertTransaction(String ItemCode, String ProviderCode, String Qty, String trnid) {
        /*if(!C.Existence("SELECT * FROM stockTransaction WHERE transactionType ='4' AND providerId ='" + g.getProvCode() + "' AND healthId = '" +
                g.getGeneratedId() +"' AND itemCode = '" + ItemCode +"' AND systemEntryDate ='" + Global.DateNowYMD() +"'"))*/
        if (!C.Existence("SELECT * FROM stockTransaction WHERE transactionType ='4' AND providerId ='" + g.getProvCode() + "' AND healthId = '" +
                g.getGeneratedId() + "' AND itemCode = '" + ItemCode + "' AND systemEntryDate ='" + Global.DateConvertYMD(dtpDOV.getText().toString()) + "'"))//transactionId ='" + trnid +"'"))
        {
            String Sql = "Insert Into stockTransaction(transactionId,transactionDateTime,providerId,transactionType,healthId,itemCode,receiveQty,issueQty,systemEntryDate, Upload)" +
                    " VALUES('" + trnid + "','" + Global.DateTimeNowYMDHMS() + "','" + g.getProvCode() + "','" + "4'" + ",'" + g.getGeneratedId() + "', '" + ItemCode + "','" + "0" + "', '"
                    + Qty + "','" + Global.DateConvertYMD(dtpDOV.getText().toString()) + "', '2')";
            /*String Sql = "Insert Into stockTransaction(transactionId,transactionDateTime,providerId,transactionType,healthId,itemCode,receiveQty,issueQty,systemEntryDate)" +
                    " VALUES('"+trnid + "','" +Global.DateTimeNowYMDHMS()+"','"+ g.getProvCode() +"','"+ "4'" + ",'" + g.getGeneratedId() +"', '"+ ItemCode +"','"+ "0" + "', '"
                    + Qty +"','" + Global.DateNowYMD() + "')";*/
            C.Save(Sql);
        } else {
            String Sql = "Update stockTransaction Set Upload = '2', issueQty= '" + Qty + "', modifyDate = '" + Global.DateTimeNowYMDHMS() + "' WHERE providerId = '" + g.getProvCode() + "' AND itemCode = '" + ItemCode +
                    "' AND healthId = '" + g.getGeneratedId() + "' AND transactionType = '" + "4' AND systemEntryDate =' " + Global.DateConvertYMD(dtpDOV.getText().toString()) + "'";
            /*String Sql = "Update stockTransaction Set issueQty= '" + Qty +"', modifyDate = '" + Global.DateTimeNowYMDHMS()+ "' WHERE providerId = '" + g.getProvCode() + "' AND itemCode = '" + ItemCode +
                    "' AND healthId = '" + g.getGeneratedId() +"' AND transactionType = '" + "4' AND systemEntryDate =' " + Global.DateNowYMD() +"'";*/
            C.Save(Sql);
        }

    }

    private void InsertTransactionUnstockToStock(String ItemCode, String ProviderCode, String Qty, String trnid) {
        /*if(!C.Existence("SELECT * FROM stockTransaction WHERE transactionType ='4' AND providerId ='" + g.getProvCode() + "' AND healthId = '" +
                g.getGeneratedId() +"' AND itemCode = '" + ItemCode +"' AND systemEntryDate ='" + Global.DateNowYMD() +"'"))*/
        if (!C.Existence("SELECT * FROM stockTransaction WHERE transactionType ='4' AND providerId ='" + g.getProvCode() + "' AND healthId = '" +
                g.getGeneratedId() + "' AND itemCode = '" + ItemCode + "' AND systemEntryDate ='" + Global.DateConvertYMD(dtpDOV.getText().toString()) + "'"))//transactionId ='" + trnid +"'"))
        {
            String Sql = "Insert Into stockTransaction(transactionId,transactionDateTime,providerId,transactionType,healthId,itemCode,receiveQty,issueQty,systemEntryDate, Upload)" +
                    " VALUES('" + trnid + "','" + Global.DateTimeNowYMDHMS() + "','" + g.getProvCode() + "','" + "4'" + ",'" + g.getGeneratedId() + "', '" + ItemCode + "','" + "0" + "', '"
                    + Qty + "','" + Global.DateConvertYMD(dtpDOV.getText().toString()) + "', '2')";
            /*String Sql = "Insert Into stockTransaction(transactionId,transactionDateTime,providerId,transactionType,healthId,itemCode,receiveQty,issueQty,systemEntryDate)" +
                    " VALUES('"+trnid + "','" +Global.DateTimeNowYMDHMS()+"','"+ g.getProvCode() +"','"+ "4'" + ",'" + g.getGeneratedId() +"', '"+ ItemCode +"','"+ "0" + "', '"
                    + Qty +"','" + Global.DateNowYMD() + "')";*/
            C.Save(Sql);
            String sqlupdate = "";
            String VisitNo = txtVisitNo.getText().toString();
            sqlupdate = "Update elcoVisit Set ";
            sqlupdate += "transactionId = '" + trnid + "'";
            sqlupdate += " Where HealthID='" + g.getGeneratedId() + "' and Visit='" + VisitNo + "'";
            C.Save(sqlupdate);

        }
    }

    private void UpdateStockAndItem(String trnid, String providerId, String PrevItemCode, String CurrentItemCode, String Qty) {
        String Sql = "Update stockTransaction Set issueQty= '" + String.valueOf(Qty) + "',itemCode = '" + CurrentItemCode + "', modifyDate = '" + Global.DateTimeNowYMDHMS() + "', Upload='2' WHERE providerId = '" + g.getProvCode() + "' AND itemCode = '" + PrevItemCode + "' and transactionId== '" + trnid + "'";
        C.Save(Sql);
    }

    private void UpdateStockZeroAndItem(String trnid, String PrevItemCode, String Qty) {
        String Sql = "Update stockTransaction Set issueQty= '0', modifyDate = '" + Global.DateTimeNowYMDHMS() + "', Upload='2' WHERE providerId = '" + g.getProvCode() + "' AND itemCode = '" + PrevItemCode + "' and transactionId== '" + trnid + "'";
        C.Save(Sql);
    }

    private void UpdateCurrentStock(String providerId, String ItemCode, String Qty) {
        Integer q = Global.GetCurrentStockOfItem(C, g.getProvCode(), ItemCode);
        Integer currentStock = q - Integer.parseInt(Qty);
        String Sql = "Update currentStock Set stockQty= '" + String.valueOf(currentStock) + "', modifyDate = '" + Global.DateTimeNowYMDHMS() + "' WHERE providerId = '" + g.getProvCode() + "' AND itemCode = '" + ItemCode + "'";
        C.Save(Sql);
    }

    private boolean IsItemSame(String CurrentItem, String VisitNo) {
        String PrevItem = C.ReturnSingleValue("Select currStatus from elcovisit Where HealthID='" + g.getGeneratedId() + "' and Visit='" + VisitNo + "'");
        if (!CurrentItem.equalsIgnoreCase(PrevItem)) {
            return true;
        } else {
            return false;
        }

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

            Sql = "Update stockTransaction Set Upload = '2', issueQty= '" + CurrentIssueQty + "', modifyDate = '" + Global.DateTimeNowYMDHMS() + "' WHERE  transactionId='" + Trid + "'";
            C.Save(Sql);

            Sql = "Update elcovisit Set qty= '" + CurrentIssueQty + "', modifyDate = '" + Global.DateTimeNowYMDHMS() + "' WHERE  transactionId='" + Trid + "'";
            C.Save(Sql);

            // Needs this method
            UpdateCurrentStock(providerId, ItemCode, CurrentIssueQty);

        }
    }

    private void InsertInjectableToWomanInjectableTable(String doseDate) {
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

    //ELCO Immunization
    private void ImmunizationSave(String ImuIDNo) {
        //Immunization
        String SQL = "";
        if (!C.Existence("Select healthid from immunizationHistory  Where healthid='" + g.getGeneratedId() + "' and imuCode='" + ImuIDNo + "'")) {
            SQL = "Insert into immunizationHistory(healthId,providerId,imuCode,imuDate,imuCard,imuSource,systemEntryDate,imuDose, Upload)Values(";
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
            SQL += "'" + Global.DateTimeNowYMDHMS() + "',";
            //SQL += "'" + Global.DateTimeNowYMDHMS() + "',";
            SQL += "'1','2')";
        } else {
            SQL = "Update immunizationHistory set Upload = '2',";
            SQL += "imuDate='" + (dtpDOTT1.getText().toString().length() == 0 ? "" : Global.DateConvertYMD(dtpDOTT1.getText().toString())) + "',";
            SQL += "modifyDate='" + Global.DateTimeNowYMDHMS() + "',";
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
                txtHusName.setText(cur.getString(cur.getColumnIndex("HusName")).replace("null", ""));
                txtHusAge.setText(cur.getString(cur.getColumnIndex("HusAge")).replace("null", ""));
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
            Connection.MessageBox(ELCOForm.this, e.getMessage());
            return;
        }
    }

    private void ELCOMemSearch(String HealthID) {
        try {
            String SQL = "";
            SQL = "Select healthId,providerId,hhStatus as HHStatus,haHHNo as HAHHNo,elcoNo as ELCONo,acceptorStatus,husbandName as husbandName,domSource as DOMSource,marrDate as MarrDate,marrAge as MarrAge,son as Son,dau as Dau,regDT";
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

                txtELCONo.setText(cur.getString(cur.getColumnIndex("ELCONo")).replace("null", ""));
                if (cur.getString(cur.getColumnIndex("acceptorStatus")) != null) {
                    if (cur.getString(cur.getColumnIndex("acceptorStatus")).equals("1"))
                        rdoELCOStatus1.setChecked(true);
                    else if (cur.getString(cur.getColumnIndex("acceptorStatus")).equals("2"))
                        rdoELCOStatus2.setChecked(true);
                }
                txtLiveSon.setText(cur.getString(cur.getColumnIndex("Son")));
                txtLiveDau.setText(cur.getString(cur.getColumnIndex("Dau")));
                dtpDOM.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("MarrDate")).replace("null", "")));
                txtMAge.setText(cur.getString(cur.getColumnIndex("MarrAge")).replace("null", ""));
                if (cur.getString(cur.getColumnIndex("DOMSource")).equals("1"))
                    rdoDOMSource1.setChecked(true);
                else
                    rdoDOMSource2.setChecked(true);

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(ELCOForm.this, e.getMessage());
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
                        else if (M.equals("06")) vcode[2][i] = "স্থায়ী পদ্ধতি(পুরুষ) এনএসভি";
                        else if (M.equals("07")) vcode[2][i] = "স্থায়ী পদ্ধতি(মহিলা) টিউবেকটমী";
                        else if (M.equals("08")) vcode[2][i] = "ইসিপি ";
                        else if (M.equals("09")) vcode[2][i] = "মিসোপ্রোস্টোল";
                        else if (M.equals("10")) vcode[2][i] = "পার্শ্ব-প্রতিক্রিয়ার জন্য প্রেরন ";
                        else if (M.equals("11")) vcode[2][i] = "পদ্ধতির জন্য প্রেরন";
                        else if (M.equals("12")) vcode[2][i] = "গর্ভবতী ";
                        else if (M.equals("13")) vcode[2][i] = "জীবিত জন্ম ";
                        else if (M.equals("14")) vcode[2][i] = "গর্ভ খালাস (জীবিত জন্ম ছাড়া)";
                        else if (M.equals("15")) vcode[2][i] = "অপারেশন করে জরায়ু অপসারণ";
                        else if (M.equals("16")) vcode[2][i] = "স্বামী বিদেশ";
                        else if (M.equals("17")) vcode[2][i] = "বন্ধ্যা";
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
                            Cursor cur = C.ReadData("Select  visit as visit,vdate as vdate,visitstatus as visitstatus,currstatus as currstatus,ssource as ssource,qty as qty,unit as unit,brand as brand,validity as validity,daymonyear as dmy, ifnull(referPlace,'') as referplace,mrSource, MRDate,MRAge,syrinsQty, ifnull(pregNo,'') AS PGNNo,sendForMethod,impotent from ElcoVisit where healthid='" + g.getGeneratedId() + "' and visit='" + vcode[3][position] + "'");
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
                                String spnMethodForNullCheck = cur.getString(cur.getColumnIndex("currstatus")).replace("null", "");
                                if (!spnMethodForNullCheck.equals("") & !spnMethodForNullCheck.equals("null")) {
                                    Integer spnMethods = Integer.parseInt(cur.getString(cur.getColumnIndex("currstatus")));
                                    if (spnMethods == 1) {
                                        rdoSSFWA.setVisibility(View.VISIBLE);
                                        spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2)   order by EvCode asc"));
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
                                        spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2)   order by EvCode asc"));
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
                                        spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2)   order by EvCode asc"));
                                        spnMethod.setSelection(Global.SpinnerItemPosition(spnMethod, 2, cur.getString(cur.getColumnIndex("currstatus"))));
                                    } else if (spnMethods == 4) {
                                        rdoSSFWA.setVisibility(View.GONE);
                                        rdoSSFWA.setChecked(false);
                                        spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2)   order by EvCode asc"));
                                        spnMethod.setSelection(Global.SpinnerItemPosition(spnMethod, 2, cur.getString(cur.getColumnIndex("currstatus"))));
                                    } else if (spnMethods == 5) {
                                        rdoSSFWA.setVisibility(View.GONE);
                                        rdoSSFWA.setChecked(false);
                                        spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2)   order by EvCode asc"));
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
                                        spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2)   order by EvCode asc"));
                                        spnMethod.setSelection(Global.SpinnerItemPosition(spnMethod, 2, cur.getString(cur.getColumnIndex("currstatus"))));
                                    } else if (spnMethods == 7) {
                                        rdoSSFWA.setVisibility(View.GONE);
                                        rdoSSFWA.setChecked(false);
                                        spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2)   order by EvCode asc"));
                                        spnMethod.setSelection(Global.SpinnerItemPosition(spnMethod, 2, cur.getString(cur.getColumnIndex("currstatus"))));
                                    } else if (spnMethods == 8) {
                                        rdoSSFWA.setVisibility(View.VISIBLE);
                                        spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2)   order by EvCode asc"));//in('08')
                                        spnMethod.setSelection(Global.SpinnerItemPosition(spnMethod, 2, cur.getString(cur.getColumnIndex("currstatus"))));
                                    } else if (spnMethods == 9) {
                                        rdoSSFWA.setVisibility(View.VISIBLE);
                                        spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2)  order by EvCode asc"));
                                        spnMethod.setSelection(Global.SpinnerItemPosition(spnMethod, 2, cur.getString(cur.getColumnIndex("currstatus"))));
                                    } else if (spnMethods == 10) {
                                        spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2)   order by EvCode asc"));
                                        spnMethod.setSelection(Global.SpinnerItemPosition(spnMethod, 2, cur.getString(cur.getColumnIndex("currstatus"))));
                                    } else if (spnMethods == 11) {

                                        spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2)   order by EvCode asc"));
                                        spnMethod.setSelection(Global.SpinnerItemPosition(spnMethod, 2, cur.getString(cur.getColumnIndex("currstatus"))));
                                    } else if (spnMethods == 12) {
                                        spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2)   order by EvCode asc"));
                                        spnMethod.setSelection(Global.SpinnerItemPosition(spnMethod, 2, cur.getString(cur.getColumnIndex("currstatus"))));
                                    } else if (spnMethods == 13) {
                                        spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2)   order by EvCode asc"));
                                        spnMethod.setSelection(Global.SpinnerItemPosition(spnMethod, 2, cur.getString(cur.getColumnIndex("currstatus"))));
                                    } else if (spnMethods == 14) {
                                        spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2)   order by EvCode asc"));
                                        spnMethod.setSelection(Global.SpinnerItemPosition(spnMethod, 2, cur.getString(cur.getColumnIndex("currstatus"))));
                                    } else if (spnMethods == 15) {
                                        spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2)   order by EvCode asc"));
                                        spnMethod.setSelection(Global.SpinnerItemPosition(spnMethod, 2, cur.getString(cur.getColumnIndex("currstatus"))));
                                    } else if (spnMethods == 16) {
                                        spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2)   order by EvCode asc"));
                                        spnMethod.setSelection(Global.SpinnerItemPosition(spnMethod, 2, cur.getString(cur.getColumnIndex("currstatus"))));
                                    } else if (spnMethods == 17) {
                                        spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2)   order by EvCode asc"));
                                        spnMethod.setSelection(Global.SpinnerItemPosition(spnMethod, 2, cur.getString(cur.getColumnIndex("currstatus"))));
                                    } else if (spnMethods == 18) {
                                        spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select substr('0' ||EvCode, -2, 2)||'-'||EVName as EvCode from ElcoEvent where substr('0' ||EvCode, -2, 2)   order by EvCode asc"));
                                        spnMethod.setSelection(Global.SpinnerItemPosition(spnMethod, 2, cur.getString(cur.getColumnIndex("currstatus"))));
                                    }
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
                                    if (cur.getString(cur.getColumnIndex("mrSource")).equals("1"))
                                        rdoMethodReceveDT1.setChecked(true);
                                    else if (cur.getString(cur.getColumnIndex("mrSource")).equals("2"))
                                        rdoMethodReceveDT2.setChecked(true);
                                    else {

                                    }
                                }


                                txtSyringeQty.setText(cur.getString(cur.getColumnIndex("syrinsQty")));
                                dtpDOV.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("vdate"))));
                                String sendForMethod = cur.getString(cur.getColumnIndex("sendForMethod"));
                                if (sendForMethod != null) {
                                    if (cur.getString(cur.getColumnIndex("sendForMethod")).equals("01")) {
                                        spnSendForMethod.setSelection(1);
                                    } else if (cur.getString(cur.getColumnIndex("sendForMethod")).equals("02")) {
                                        spnSendForMethod.setSelection(2);
                                    } else if (cur.getString(cur.getColumnIndex("sendForMethod")).equals("03")) {
                                        spnSendForMethod.setSelection(3);
                                    } else if (cur.getString(cur.getColumnIndex("sendForMethod")).equals("04")) {
                                        spnSendForMethod.setSelection(4);
                                    } else if (cur.getString(cur.getColumnIndex("sendForMethod")).equals("05")) {
                                        spnSendForMethod.setSelection(5);
                                    } else if (cur.getString(cur.getColumnIndex("sendForMethod")).equals("06")) {
                                        spnSendForMethod.setSelection(6);
                                    } else if (cur.getString(cur.getColumnIndex("sendForMethod")).equals("07")) {
                                        spnSendForMethod.setSelection(7);
                                    }
                                }

                                String impotent = cur.getString(cur.getColumnIndex("impotent"));
                                if (impotent != null) {
                                    if (cur.getString(cur.getColumnIndex("impotent")).equals("01")) {
                                        spnImpotent.setSelection(1);
                                    } else if (cur.getString(cur.getColumnIndex("impotent")).equals("02")) {
                                        spnImpotent.setSelection(2);
                                    }
                                }


                                cur.moveToNext();
                            }
                            cur.close();


                            if (String.valueOf(vcode[0][position]).equalsIgnoreCase("12")) {
                                Connection.MessageBox(ELCOForm.this, "এই মহিলাটি বর্তমানে গর্ভবতী। এই গর্ভবতী মহিলার গর্ভ সংক্রান্ত সকল তথ্য 'গর্ভবতী মা ও নবজাতকের তথ্য/সেবা ছক' এ সংগ্রহ করুন ।");
                                cmdSave.setEnabled(false);
                                /*Intent f2 = new Intent(getApplicationContext(), FWAReg.class);
                                Bundle IDbundle = new Bundle();
                                g.setPregNo(PGNNo);
                                f2.putExtras(IDbundle);
                                startActivity(f2);
                                finish();*/
                            } else if (String.valueOf(vcode[0][position]).equalsIgnoreCase("13")) {
                                cmdSave.setEnabled(false);
                            } else if (String.valueOf(vcode[0][position]).equalsIgnoreCase("14")) {
                                cmdSave.setEnabled(false);
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
                    Connection.MessageBox(ELCOForm.this, ex.getMessage());
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
                            } else if (vcode[2][position].equals("2")) {
                                rdoCardNo.setChecked(true);
                                rdogrpTTCard.setVisibility(View.VISIBLE);
                            }

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
                    Connection.MessageBox(ELCOForm.this, ex.getMessage());
                }

            }
            return MyView;
        }

    }

    private String GetDOV(String HealthId) {
        String sq = String.format("Select ifnull(max(vDate),'') as Vdate from elcoVisit WHERE healthId = '%s'  and vDate<>'null'", g.getGeneratedId());
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
                        Connection.MessageBox(ELCOForm.this, "তারিখ আজকের তারিখের সমান অথবা কম হতে হবে।");
                        return;
                    }
                }
                String DOB1 = "15/" + DOB.getText() + "/" + DOB.getText();
                Date DOB11 = sdf.parse(DOB1);
                if (date2.before(DOB11)) {

                    if (VariableID.equals("btnB11")) {
                        Connection.MessageBox(ELCOForm.this, "আপনার সর্বশেষ গর্ভ ফলাফলের তারিখ  জন্ম তারিখ অপেক্ষা বড় হবে না");
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