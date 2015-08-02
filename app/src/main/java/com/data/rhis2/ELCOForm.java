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
import android.provider.Settings;
import android.renderscript.Int4;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    boolean netwoekAvailable=false;
    Location currentLocation;
    double currentLatitude,currentLongitude;
    Location currentLocationNet;
    double currentLatitudeNet,currentLongitudeNet;
    Calendar c = Calendar.getInstance();
    //Disabled Back/Home key
    //--------------------------------------------------------------------------------------------------
    @Override
    public boolean onKeyDown(int iKeyCode, KeyEvent event)
    {
        if(iKeyCode == KeyEvent.KEYCODE_BACK || iKeyCode == KeyEvent.KEYCODE_HOME)
        { return false; }
        else { return true;  }
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
                adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান[Yes/No]?");
                adb.setNegativeButton("No", null);
                adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        Intent f2 = new Intent(getApplicationContext(),MemberList.class);
                        startActivity(f2);
                    }});
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
    TextView txtHusName;
    TextView txtHusAge;

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

    LinearLayout secBrand;
    Spinner spnBrand;

    LinearLayout secWherePlace;
    TextView VlblWherePlace;
    Spinner spnWherePlace;

    LinearLayout secDetail;

    String StartTime;

    LinearLayout secValidity;
    EditText txtValidity;
    Spinner spnValidityUnit;
    GridView g1;

    ImageButton btnAddTT;
    ImageButton btnTTClose;
    TextView txtVisitNo;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.elco);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();

            TableName = "ELCO";
            TableNameELCOVisit = "ELCOVisit";

            secDetail=(LinearLayout)findViewById(R.id.secDetail);
            seclblH=(LinearLayout)findViewById(R.id.seclblH);
            seclblH.setVisibility(View.GONE);
            txtHealthID = (TextView) findViewById( R.id.txtHealthID );
            txtHealthID.setText(g.getHealthID());

            txtVisitNo = (TextView)findViewById(R.id.txtVisitNo);
            txtVisitNo.setText(VisitNumber(txtHealthID.getText().toString()));

            txtPHHNo = (TextView) findViewById( R.id.txtPHHNo );
            txtPHHNo.setText(g.getHouseholdNo());

            chkHHNoDontKnow=(CheckBox) findViewById(R.id.chkHHNoDontKnow);
            txtHHNo=(EditText) findViewById(R.id.txtHHNo);
            //txtHHNo.setEnabled(false);
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
            VlblSNo=(TextView) findViewById(R.id.VlblSNo);
            txtSNo=(TextView) findViewById(R.id.txtSNo);

            VlblDOB=(TextView) findViewById(R.id.VlblDOB);
            DOB=(TextView) findViewById(R.id.DOB);

            VlblHus=(TextView) findViewById(R.id.VlblHus);

            secName=(LinearLayout)findViewById(R.id.secName);
            VlblName=(TextView) findViewById(R.id.VlblName);
            txtName = (TextView) findViewById( R.id.txtName);

            secAge=(LinearLayout)findViewById(R.id.secAge);
            VlblAge=(TextView) findViewById(R.id.VlblAge);
            txtAge = (TextView) findViewById( R.id.txtAge);

            secHusName=(LinearLayout)findViewById(R.id.secHusName);
            txtHusName = (TextView) findViewById( R.id.txtHusName);
            txtHusAge = (TextView) findViewById( R.id.txtHusAge);

            secMEdu=(LinearLayout)findViewById(R.id.secMEdu);
            VlblMEdu=(TextView) findViewById(R.id.VlblMEdu);
            txtMEdu = (TextView) findViewById( R.id.txtMEdu);

            secMNID=(LinearLayout)findViewById(R.id.secMNID);
            VlblMNID=(TextView) findViewById(R.id.VlblMNID);
            txtMNID = (TextView) findViewById( R.id.txtMNID);

            secMBRID=(LinearLayout)findViewById(R.id.secMBRID);
            VlblMBRID=(TextView) findViewById(R.id.VlblMBRID);
            txtMBRID = (TextView) findViewById( R.id.txtMBRID);

            secMMNo=(LinearLayout)findViewById(R.id.secMMNo);
            VlblMMNo=(TextView) findViewById(R.id.VlblMMNo);
            txtMMNo = (TextView) findViewById( R.id.txtMMNo);

            txtELCONo = (EditText)findViewById(R.id.txtELCONo);

            btnPlus=(ImageButton)findViewById(R.id.btnPlus);
            btnMinus=(ImageButton)findViewById(R.id.btnMinus);
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

            secTTCardStatus=(LinearLayout)findViewById(R.id.secTTCardStatus);
            VlblTTCardStatus = (TextView) findViewById(R.id.VlblTTCardStatus);

            secTT1=(LinearLayout)findViewById(R.id.secTT1);

            VlblTT1=(TextView) findViewById(R.id.VlblTT1);
            ChkTT1=(CheckBox) findViewById(R.id.ChkTT1);
            rdogrpTTCard = (RadioGroup)findViewById(R.id.rdogrpTTCard);
            rdoCardYes = (RadioButton)findViewById(R.id.rdoCardYes);
            rdoCardNo = (RadioButton)findViewById(R.id.rdoCardNo);
            rdogrpTTCard.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(RadioGroup radioGroup,int radioButtonID) {
                    if(rdoCardYes.isChecked())
                    {
                        btnDOTT1.setEnabled(true);
                    }
                    else if(rdoCardNo.isChecked())
                    {
                        dtpDOTT1.setText("");
                        btnDOTT1.setEnabled(false);
                    }
                }
                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });
            //ChkCard1=(CheckBox) findViewById(R.id.ChkCard1);
            dtpDOTT1=(EditText) findViewById(R.id.dtpDOTT1);
            btnDOTT1 = (ImageButton) findViewById(R.id.btnDOTT1);
            dtpDOTT1.setText("");
            btnDOTT1.setEnabled(false);
            ChkTT1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (((CheckBox) v).isChecked()) {
                        btnDOTT1.setEnabled(true);
                    } else {
                        rdogrpTTCard.clearCheck();
                        dtpDOTT1.setText("");
                        btnDOTT1.setEnabled(false);
                    }
                }
            });

            dtpDOM=(EditText) findViewById(R.id.dtpDOM);
            btnDOM = (ImageButton) findViewById(R.id.btnDOM);
            secDOMSource=(LinearLayout)findViewById(R.id.secDOMSource);
            VlblDOMSource = (TextView) findViewById(R.id.VlblDOMSource);
            rdogrpDOMSource = (RadioGroup) findViewById(R.id.rdogrpDOMSource);
            rdoDOMSource1 = (RadioButton) findViewById(R.id.rdoDOMSource1);
            rdoDOMSource1.setChecked(true);
            rdoDOMSource2 = (RadioButton) findViewById(R.id.rdoDOMSource2);
            //secMAge.setVisibility( View.GONE );
            txtMAge=(EditText) findViewById(R.id.txtMAge);

            dtpDOM.setVisibility( View.VISIBLE );
            btnDOM.setVisibility(View.VISIBLE);
            txtMAge.setVisibility(View.GONE);
            VlblMAge=(TextView)findViewById(R.id.VlblMAge);
            VlblMAge.setVisibility( View.GONE);
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

            secLiveSon=(LinearLayout)findViewById(R.id.secLiveSon);
            VlblLiveSon=(TextView) findViewById(R.id.VlblLiveSon);
            txtLiveSon=(EditText) findViewById(R.id.txtLiveSon);

            VlblLiveDau=(TextView) findViewById(R.id.VlblLiveDau);
            txtLiveDau=(EditText) findViewById(R.id.txtLiveDau);

            dtpDOV=(EditText) findViewById(R.id.dtpDOV);
            dtpDOV.setText(Global.DateNowDMY());
            btnDOV = (ImageButton) findViewById(R.id.btnDOV);

            rdogrpVS = (RadioGroup) findViewById(R.id.rdogrpVS);
            rdoVSPresent = (RadioButton) findViewById(R.id.rdoVSPresent);
            rdoVSAbsent = (RadioButton) findViewById(R.id.rdoVSAbsent);

            secMethod=(LinearLayout)findViewById(R.id.secMethod);

            secSS=(LinearLayout)findViewById(R.id.secSS);
            rdogrpSS = (RadioGroup) findViewById(R.id.rdogrpSS);
            rdoSSOtherGovt= (RadioButton) findViewById(R.id.rdoSSOtherGovt);
            rdoSSFWA = (RadioButton) findViewById(R.id.rdoSSFWA);
            rdoSSLMarket = (RadioButton) findViewById(R.id.rdoSSLMarket);
            rdoSSOther = (RadioButton) findViewById(R.id.rdoSSOther);

            rdogrpSS.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup arg0, int id) {
                    if(id == R.id.rdoSSFWA)
                    {
                        txtMethodQty.setEnabled(true);
                    }
                    else if(id == R.id.rdoSSLMarket |id == R.id.rdoSSOther)
                    {
                        txtMethodQty.setText("");
                        txtMethodQty.setEnabled(false);
                    }
                }});



            VlblMethod=(TextView) findViewById(R.id.VlblMethod);
            spnMethod=(Spinner) findViewById(R.id.spnMethod);

            secMethodUnit=(LinearLayout)findViewById(R.id.secMethodUnit);
            spnMethodUnit = (Spinner) findViewById(R.id.spnMethodUnit);
            txtMethodQty=(EditText) findViewById(R.id.txtMethodQty);

            secWherePlace=(LinearLayout)findViewById(R.id.secWherePlace);
            VlblWherePlace=(TextView) findViewById(R.id.VlblWherePlace);
            spnWherePlace= (Spinner) findViewById(R.id.spnWherePlace);
            List<String> listFacility = new ArrayList<String>();
            listFacility.add("");
            listFacility.add("01-Facility1");
            ArrayAdapter<String> adptrspnWherePlace= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listFacility);
            spnWherePlace.setAdapter(adptrspnWherePlace);


            secBrand=(LinearLayout)findViewById(R.id.secBrand);
            secBrand.setVisibility( View.GONE );
            spnBrand= (Spinner) findViewById(R.id.spnBrand);

            rdogrpVS.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup arg0, int id) {
                    if(id == R.id.rdoVSPresent)
                    {
                        MethodList("p");
                        secMethod.setVisibility(View.VISIBLE);
                        spnMethod.setSelection(0);
                        txtMethodQty.setText("");
                        spnMethodUnit.setSelection(0);
                    }
                    else if(id == R.id.rdoVSAbsent)
                    {
                        MethodList("a");
                        secMethod.setVisibility( View.VISIBLE );
                        spnMethod.setSelection(0);
                        txtMethodQty.setText("");
                        spnMethodUnit.setSelection(0);
                    }
                    else
                    {
                        //secMethod.setVisibility( View.GONE );
                    }
                }});
            /*
            List<String> listMethod = new ArrayList<String>();

            listMethod.add("");
            listMethod.add("01-খাবার বড়ি");
            listMethod.add("02-কনডম");
            listMethod.add("03-ইনজেকটেবল");
            listMethod.add("04-আই ইউ ডি");
            listMethod.add("05-ইমপ্যান্ট");
            listMethod.add("06-স্থায়ী পদ্ধতি(পুরুষ)");
            listMethod.add("07-স্থায়ী পদ্ধতি(মহিলা)");
            listMethod.add("08-ইসিপি ");
            listMethod.add("09-মিসোপ্রোস্টোল");
            listMethod.add("10-পার্শ্ব-প্রতিক্রিয়ার জন্য প্রেরন ");
            listMethod.add("11-পদ্ধতির জন্য প্রেরন");
            listMethod.add("12-গর্ভবতী ");
            listMethod.add("13-জীবিত জন্ম ");
            listMethod.add("14-গর্ভ খালাস (জীবিত জন্ম ছাড়া)");
            listMethod.add("15-জরায়ু অপারেশন করে অপসারন");
            listMethod.add("16-স্বামী বিদেশ থাকলে ");
            listMethod.add("17-বন্ধ্যাত্ব বিষয়ক তথ্য ");
            listMethod.add("18-অন্য যে কোন অবস্থা ");

            ArrayAdapter<String> adptrMethod= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listMethod);
            spnMethod.setAdapter(adptrMethod);
            */
            if(CurrentStatus(txtHealthID.getText().toString()).equals("12"))
                spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select EvCode||'-'||EVName as EvCode from ElcoEvent where evcode in('12','13','14') order by EvCode asc"));
            else
                spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select EvCode||'-'||EVName as EvCode from ElcoEvent where evcode not in('13','14') order by EvCode asc"));

            List<String> listBrand = new ArrayList<String>();
            listBrand.add("");
            listBrand.add("সুখী");
            listBrand.add("আপন");
            listBrand.add("বড়ি");
            ArrayAdapter<String> adptrspnBrand= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listBrand);
            spnBrand.setAdapter(adptrspnBrand);


            List<String> listMethodUnit = new ArrayList<String>();
            listMethodUnit.add("");
            listMethodUnit.add("চক্র");
            listMethodUnit.add("পিস");
            listMethodUnit.add("ডোজ");
            ArrayAdapter<String> adptrMethodUnit= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listMethodUnit);
            spnMethodUnit.setAdapter(adptrMethodUnit);

            spnMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    if (spnMethod.getSelectedItemPosition() == 0) {
                        //secMethod.setVisibility(View.GONE);
                        secSS.setVisibility(View.GONE);
                        //rdogrpSS.clearCheck();
                        secMethodUnit.setVisibility(View.GONE);
                        spnMethodUnit.setSelection(0);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        txtMethodQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        secValidity.setVisibility(View.GONE);
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);
                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("01")) {
                        secSS.setVisibility(View.VISIBLE);
                        //rdogrpSS.clearCheck();
                        //rdoSSOtherGovt.setVisibility(View.GONE);
                        //rdoSSFWA.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.VISIBLE);
                        secBrand.setVisibility(View.VISIBLE);
                        spnBrand.setSelection(3);
                        spnMethodUnit.setSelection(1);
                        txtMethodQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        secValidity.setVisibility(View.GONE);
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("02")) {
                        secSS.setVisibility(View.VISIBLE);
                        //rdogrpSS.clearCheck();
                        //rdoSSFWA.setVisibility(View.VISIBLE);
                        //rdoSSOtherGovt.setVisibility(View.GONE);
                        secMethodUnit.setVisibility(View.VISIBLE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(2);
                        txtMethodQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        secValidity.setVisibility(View.GONE);
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("03")) {
                        secSS.setVisibility(View.VISIBLE);
                        //rdogrpSS.clearCheck();
                        //rdoSSOtherGovt.setVisibility(View.GONE);
                        //rdoSSFWA.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.VISIBLE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(3);
                        txtMethodQty.setText("1");
                        txtMethodQty.setEnabled(false);
                        secWherePlace.setVisibility(View.GONE);
                        secValidity.setVisibility(View.GONE);
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("04")) {
                        secSS.setVisibility(View.VISIBLE);
                        //rdogrpSS.clearCheck();
                        //rdoSSOtherGovt.setVisibility(View.VISIBLE);
                        //rdoSSFWA.setVisibility(View.GONE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        secValidity.setVisibility(View.VISIBLE);
                        spnValidityUnit.setSelection(3);
                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("05")) {
                        secSS.setVisibility(View.VISIBLE);
                        //rdogrpSS.clearCheck();
                        //rdoSSOtherGovt.setVisibility(View.VISIBLE);
                        //rdoSSFWA.setVisibility(View.GONE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        secValidity.setVisibility(View.GONE);
                        spnValidityUnit.setSelection(2);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("06")) {
                        secSS.setVisibility(View.VISIBLE);
                        //rdogrpSS.clearCheck();
                        //rdoSSOtherGovt.setVisibility(View.VISIBLE);
                        //rdoSSFWA.setVisibility(View.GONE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnBrand.setSelection(0);
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        secValidity.setVisibility(View.GONE);
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("07")) {
                        secSS.setVisibility(View.VISIBLE);
                        //rdogrpSS.clearCheck();
                        //rdoSSOtherGovt.setVisibility(View.GONE);
                        //rdoSSFWA.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.VISIBLE);
                        secBrand.setVisibility(View.VISIBLE);
                        spnMethodUnit.setSelection(3);
                        txtMethodQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        secValidity.setVisibility(View.GONE);
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("08")) {
                        secSS.setVisibility(View.VISIBLE);
                        //rdogrpSS.clearCheck();
                        //rdoSSOtherGovt.setVisibility(View.GONE);
                        //rdoSSFWA.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.VISIBLE);
                        secBrand.setVisibility(View.VISIBLE);
                        spnMethodUnit.setSelection(3);
                        txtMethodQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        secValidity.setVisibility(View.GONE);
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("09")) {
                        secSS.setVisibility(View.VISIBLE);
                        //rdogrpSS.clearCheck();
                        //rdoSSOtherGovt.setVisibility(View.GONE);
                        //rdoSSFWA.setVisibility(View.VISIBLE);
                        secMethodUnit.setVisibility(View.VISIBLE);
                        secBrand.setVisibility(View.VISIBLE);
                        spnMethodUnit.setSelection(1);
                        txtMethodQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        secValidity.setVisibility(View.GONE);
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("10")) {
                        secSS.setVisibility(View.GONE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                        secWherePlace.setVisibility(View.VISIBLE);
                        secValidity.setVisibility(View.GONE);
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("11")) {
                        secSS.setVisibility(View.GONE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                        secWherePlace.setVisibility(View.VISIBLE);
                        secValidity.setVisibility(View.GONE);
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);

                    } else if (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("15")) {
                        secSS.setVisibility(View.GONE);
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                        secWherePlace.setVisibility(View.GONE);
                        secValidity.setVisibility(View.GONE);
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);

                    } else {
                        spnMethodUnit.setSelection(0);
                        txtMethodQty.setText("");
                        secSS.setVisibility(View.GONE);
                        //rdogrpSS.clearCheck();
                        secMethodUnit.setVisibility(View.GONE);
                        secBrand.setVisibility(View.GONE);
                        secWherePlace.setVisibility(View.GONE);
                        secValidity.setVisibility(View.GONE);
                        txtValidity.setText("");
                        spnValidityUnit.setSelection(0);

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

            btnDOTT1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) { VariableID = "btnDOTT1"; showDialog(DATE_DIALOG); }});


            secValidity = (LinearLayout)findViewById(R.id.secValidity);
            secValidity.setVisibility(View.GONE);
            txtValidity = (EditText)findViewById(R.id.txtValidity);
            spnValidityUnit = (Spinner)findViewById(R.id.spnValidityUnit);
            List<String> listYMD = new ArrayList<String>();
            listYMD.add("");
            listYMD.add("দিন");
            listYMD.add("মাস");
            listYMD.add("বছর");
            ArrayAdapter<String> adptrspnYMD= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listYMD);
            spnValidityUnit.setAdapter(adptrspnYMD);

            DataSearch(txtHealthID.getText().toString());
            ELCOMemSearch(txtHealthID.getText().toString());
            //ImmunizationData(txtHealthID.getText().toString());

            Button cmdSave = (Button) findViewById(R.id.cmdSave);
            cmdSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    DataSave();
                }
            });


            btnAddTT = (ImageButton)findViewById(R.id.btnAddTT);
            btnTTClose = (ImageButton)findViewById(R.id.btnTTClose);
            btnAddTT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    secTT1.setVisibility(View.VISIBLE);
                    btnTTClose.setVisibility(View.VISIBLE);
                    btnAddTT.setVisibility(View.GONE);
                    GridView gcount=(GridView) findViewById(R.id.gridTT);
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


            ImageButton btnSaveTT = (ImageButton)findViewById(R.id.btnSaveTT);
            btnSaveTT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //GridView gcount=(GridView) findViewById(R.id.gridTT);
                    if(!ChkTT1.isChecked())
                    {
                        Connection.MessageBox(ELCOForm.this,"টিটি পেয়েছে কিনা এ তথ্য না হতে পারে না।");
                        ChkTT1.requestFocus();
                        return;
                    }
                    else if(ChkTT1.isChecked())
                    {
                        if(!rdoCardYes.isChecked() & !rdoCardNo.isChecked())
                        {
                            Connection.MessageBox(ELCOForm.this,"টিটি কার্ড আছে কিনা এ তথ্য হ্যাঁ/না হতে হবে।");
                            rdoCardYes.requestFocus();
                            return;
                        }
                        else if(rdoCardYes.isChecked() & dtpDOTT1.getText().length()==0)
                        {
                            Connection.MessageBox(ELCOForm.this,"টিটি কার্ড থাকলে অবশ্যই তারিখ থাকতে হবে।");
                            dtpDOTT1.requestFocus();
                            return;
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
                                Connection.MessageBox(ELCOForm.this,"টিটি এর তারিখ আজকের তারিখ["+ formattedDate +"] এর সমান অথবা কম হতে হবে।");
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
        }
        catch(Exception  e)
        {
            Connection.MessageBox(ELCOForm.this, e.getMessage());
            return;
        }
    }


    private void MethodList(String Status)
    {
        spnMethod.setAdapter(null);
        if(Status.equals("p")) {
            if(CurrentStatus(txtHealthID.getText().toString()).equals("12"))
                spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select EvCode||'-'||EVName as EvCode from ElcoEvent where evcode in('12','13','14') order by EvCode asc"));
            else
                spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select EvCode||'-'||EVName as EvCode from ElcoEvent where evcode not in('13','14') order by EvCode asc"));
        }
        else if(Status.equals("a"))
        {
            if(CurrentStatus(txtHealthID.getText().toString()).equals("12"))
                spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode"));
            else
                spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select EvCode||'-'||EVName as EvCode from ElcoEvent where evcode in('01','02') order by EvCode asc"));
        }
    }


    public void ElcoVisitStsatus()
    {
        g1=(GridView) findViewById(R.id.gridElcoVisit);
        g1.setAdapter(new ElcoVisit(this));
        g1.setNumColumns(5);
    }


    public void ImmunizationStatus()
    {
        GridView g1=(GridView) findViewById(R.id.gridTT);
        g1.setAdapter(new TTCard(this));
        g1.setNumColumns(6);
    }

    //Pregnancy
    private String CurrentStatus(String HealthID)
    {
       return C.ReturnSingleValue("select ifnull(currstatus,'')currstatus from elcovisit where healthid='"+ HealthID +"' order by systementrydate desc limit 1");
    }

    private String VisitNumber(String HealthID)
    {
        String SQL = "";
        SQL = "Select (ifnull(max(cast(Visit as int)),0)+1)MaxVisit from ELCOVisit";
        SQL += " where healthid='"+ HealthID +"'";

        String VisitNo = Global.Right(("00"+ C.ReturnSingleValue(SQL)),2);

        return VisitNo;
    }

    private void DataSave()
    {
        try
        {

            //ELCO Profile
            if(!chkHHNoDontKnow.isChecked())
            {
                if(txtHHNo.getText().toString().length()==0)
                {
                    Connection.MessageBox(ELCOForm.this, "খানা নম্বর কত লিখুন।");
                    txtHHNo.requestFocus();
                    return;
                }
            }

            if(txtELCONo.getText().toString().length()==0)
            {
                Connection.MessageBox(ELCOForm.this, "দম্পতি নং কত লিখুন।");
                txtELCONo.requestFocus();
                return;
            }
            /*if(ChkTT1.isChecked() & ChkTT1.isShown())
            {
                if(dtpDOTT1.getText().toString().length()==0 & dtpDOTT1.isShown())
                {
                    Connection.MessageBox(ELCOForm.this, "টিটি ১ এর তারিখ কত লিখুন।");
                    dtpDOTT1.requestFocus();
                    return;
                }
            }

            if(ChkTT2.isChecked() & ChkTT2.isShown())
            {
                if(dtpDOTT2.getText().toString().length()==0  & dtpDOTT2.isShown())
                {
                    Connection.MessageBox(ELCOForm.this, "টিটি ২ এর তারিখ কত লিখুন।");
                    dtpDOTT2.requestFocus();
                    return;
                }
            }
            if(ChkTT3.isChecked() & ChkTT3.isShown())
            {
                if(dtpDOTT3.getText().toString().length()==0  & dtpDOTT3.isShown())
                {
                    Connection.MessageBox(ELCOForm.this, "টিটি ৩ এর তারিখ কত লিখুন।");
                    dtpDOTT3.requestFocus();
                    return;
                }
            }

            if(ChkTT4.isChecked() & ChkTT4.isShown())
            {
                if(dtpDOTT4.getText().toString().length()==0 & dtpDOTT4.isShown())
                {
                    Connection.MessageBox(ELCOForm.this, "টিটি ৪ এর তারিখ কত লিখুন।");
                    dtpDOTT4.requestFocus();
                    return;
                }
            }

            if(ChkTT5.isChecked() & ChkTT5.isShown())
            {
                if(dtpDOTT5.getText().toString().length()==0 & dtpDOTT5.isShown())
                {
                    Connection.MessageBox(ELCOForm.this, "টিটি ৫ এর তারিখ কত লিখুন।");
                    dtpDOTT5.requestFocus();
                    return;
                }
            }
            */

            if(dtpDOM.getText().toString().length()==0 & dtpDOM.isShown()& rdoDOMSource1.isChecked() )
            {
                Connection.MessageBox(ELCOForm.this, "প্রকৃত বিবাহের তারিখ কত লিখুন।");
                dtpDOM.requestFocus();
                return;
            }
            else if(txtMAge.getText().toString().length()==0 & rdoDOMSource2.isChecked() )
            {
                Connection.MessageBox(ELCOForm.this, "বিবাহের বয়স  কত লিখুন।");
                txtMAge.requestFocus();
                return;
            }
            else if(txtLiveSon.getText().toString().length()==0)
            {
                Connection.MessageBox(ELCOForm.this, "জীবিত ছেলের সংখ্যা লিখুন।");
                txtLiveSon.requestFocus();
                return;
            }
            else if(txtLiveDau.getText().toString().length()==0)
            {
                Connection.MessageBox(ELCOForm.this, "জীবিত মেয়ের সংখ্যা লিখুন।");
                txtLiveDau.requestFocus();
                return;
            }


            //Follow-up Visit
            /*if(dtpDOV.getText().toString().length()==0 & dtpDOV.isShown())
            {
                Connection.MessageBox(ELCOForm.this, "পরিদর্শনের তারিখ কত লিখুন।");
                dtpDOV.requestFocus();
                return;
            }
            else if(!rdoVSAbsent.isChecked() & !rdoVSPresent.isChecked())
            {
                Connection.MessageBox(ELCOForm.this, "পরিদর্শনের অবস্থা সিলেক্ট করুন।");
                rdoVSAbsent.requestFocus();
                return;
            }
            else if(rdoVSPresent.isChecked())
            {
                 if (spnMethod.getSelectedItemPosition() == 0 & spnMethod.isShown()) {
                    Connection.MessageBox(ELCOForm.this, "জন্মনিয়ন্ত্রণ ব্যাবস্থা/গর্ভাবস্থা তালিকা থেকে  সিলেক্ট করুন।");
                    spnMethod.requestFocus();
                    return;
                }

                if((!rdoSSFWA.isChecked() & !rdoSSOther.isChecked() & !rdoSSLMarket.isChecked() & secSS.isShown()) & (Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("01") | Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("02") | Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("03")) )
                {
                    Connection.MessageBox(ELCOForm.this, "প্রদানের উৎস  সিলেক্ট করুন।");
                    rdoSSFWA.requestFocus();
                    return;
                }

                if(rdoSSFWA.isChecked() & Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("01"))
                {
                    if(txtMethodQty.getText().toString().length()==0)
                    {
                        Connection.MessageBox(ELCOForm.this, "পরিমাণ কত লিখুন।");
                        txtMethodQty.requestFocus();
                        return;
                    }

                }

                if(Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("01") | Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("02") | Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("03"))
                {
                    if(spnMethodUnit.getSelectedItemPosition()==0)
                    {
                        Connection.MessageBox(ELCOForm.this, "ইউনিট কি সিলেক্ট করুন।");
                        spnMethodUnit.requestFocus();
                        return;
                    }
                }
                if(Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("01"))
                {
                    if (spnBrand.getSelectedItemPosition() == 0 & secBrand.isShown()) {
                        Connection.MessageBox(ELCOForm.this, "ব্রান্ডের নাম তালিকা থেকে  সিলেক্ট করুন।");
                        spnBrand.requestFocus();
                        return;
                    }
                }
            }

            String VStatus=null;
            if (rdoVSPresent.isChecked() == true)
            {
                VStatus = "1";
            }
            else if (rdoVSAbsent.isChecked() == true)
            {
                VStatus = "2";
            }
            else if (rdoVSAbsent.isChecked() == false)
            {
                VStatus = "";
            }
            else if (rdoVSPresent.isChecked() == false)
            {
                VStatus = "";
            }
            String SSource=null;
            if (rdoSSFWA.isChecked() == true)
            {
                SSource = "1";
            }
            else if (rdoSSLMarket.isChecked() == true)
            {
                SSource = "2";
            }
            else if (rdoSSOther.isChecked() == true)
            {
                SSource = "3";
            }
            else if (rdoSSLMarket.isChecked() == false)
            {
                SSource = "";
            }
            else if (rdoSSFWA.isChecked() == false)
            {
                SSource = "";
            }
            else if (rdoSSOther.isChecked() == false)
            {
                SSource = "";
            }
            String SQ1 = "";
            SQ1 = "select CurrStatus||'^'||NewOld from ELCOVisit where";
            SQ1 += " Dist='"+ g.getDistrict() +"' and";
            SQ1 += " Upz='"+ g.getUpazila() +"' and";
            SQ1 += " UN='"+ g.getUnion() +"' and";
            SQ1 += " Vill='"+ g.getVillage() +"' and";
            SQ1 += " ProvType='"+ g.getProvType() +"' and";
            SQ1 += " ProvCode='"+ g.getProvCode() +"' and";
            SQ1 += " HHNo='"+ g.getHouseholdNo() +"' and SNo='"+ g.getSerialNo() +"'";
            SQ1 += " order by EnDt desc limit 1";
            String PerviousStatus = C.ReturnSingleValue(SQ1);
            String[] PerviousMethods = Connection.split(PerviousStatus, '^');
            String PreMethodStatus = PerviousMethods[0].toString();
           // String PerviousMethodSign = PerviousMethods[1].toString();
            */



            String SQL = "";

            //ELCO Profile
            //------------------------------------------------------------------------------------------------------------------------
            if(!C.Existence("Select healthid from "+ TableName +"  Where healthid='"+ txtHealthID.getText().toString() +"'"))
            {
                SQL = "Insert into "+ TableName +"(healthId,providerId,regDT,systemEntryDate,modifyDate)Values(";
                SQL += "'"+ txtHealthID.getText().toString() +"',";
                SQL += "'"+ g.getUserID() +"',";
                SQL += "'"+ Global.DateNowDMY() +"',";
                SQL += "'"+ Global.DateTimeNowDMYHMS() +"',";
                SQL += "'"+ Global.DateTimeNowDMYHMS() +"')";

                C.Save(SQL);
            }

            SQL = "Update "+ TableName +" Set ";
            SQL+="hhStatus = '"+ (chkHHNoDontKnow.isChecked()?"1":"2") +"',";
            SQL+="haHHNo = '"+ txtHHNo.getText().toString() +"',";
            SQL+="elcoNo = '"+ txtELCONo.getText().toString() +"',";
            SQL+="husbandName = '"+ txtHusName.getText().toString() +"',";
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

            /*if(rdoDOMSource1.isChecked())
            {
                SQL+="MarrDate = '"+ dtpDOM.getText().toString() +"',";
                SQL+="MarrAge = '"+ Global.DateDifferenceYears(Global.DateNowDMY(), dtpDOM.getText().toString()) +"',";
            }
            else
            {
                SQL+="MarrDate = '"+ Global.addDaysDMY(Global.DateNowDMY(), -Integer.valueOf(txtMAge.getText().toString())*365) +"',";
                SQL+="MarrAge = '"+ txtMAge.getText().toString() +"',";
            }*/

            SQL+="Son = '"+ txtLiveSon.getText().toString() +"',";
            SQL+="Dau = '"+ txtLiveDau.getText().toString() +"'";

            SQL+=" Where HealthID='"+ txtHealthID.getText().toString() +"'";
            C.Save(SQL);


            //ELCO Immunization
            //------------------------------------------------------------------------------------------------------------------------
            /*    if(ChkTT1.isChecked()) {
                    if(!C.Existence("Select healthid from Immunization  Where healthid='"+ txtHealthID.getText().toString() +"' and imuCode='1'")) {
                        SQL = "Insert into Immunization(healthId,providerId,imuCode,imuDate,imuCard,imuSource,systemEntryDate,modifyDate)Values(";
                        SQL += "'" + txtHealthID.getText().toString() + "',";
                        SQL += "'" + g.getUserID() + "',";
                        SQL += "'1',";
                        SQL += "'" + (dtpDOTT1.getText().toString().length()==0?"":dtpDOTT1.getText().toString()) +"',";
                        if(ChkTT1.isChecked() & ChkCard1.isChecked())
                            SQL += "'1',";
                        else if(ChkTT1.isChecked() & !ChkCard1.isChecked())
                            SQL += "'2',";
                        else
                            SQL += "'',";

                        SQL += "'',";
                        SQL += "'" + Global.DateTimeNowDMYHMS() + "',";
                        SQL += "'" + Global.DateTimeNowDMYHMS() + "')";
                    }
                    else
                    {
                        SQL = "Update Immunization set ";
                        SQL += "imuDate='" + (dtpDOTT1.getText().toString().length()==0?"":dtpDOTT1.getText().toString()) +"',";
                        if(ChkTT1.isChecked() & ChkCard1.isChecked())
                            SQL += "imuCard='1'";
                        else if(ChkTT1.isChecked() & !ChkCard1.isChecked())
                            SQL += "imuCard='2'";
                        else
                            SQL += "imuCard=''";
                        SQL += " where healthid='"+ txtHealthID.getText().toString() +"' and imuCode='1'";
                    }
                    C.Save(SQL);
                }
                if(ChkTT2.isChecked()) {
                    if(!C.Existence("Select healthid from Immunization  Where healthid='"+ txtHealthID.getText().toString() +"' and imuCode='2'")) {
                        SQL = "Insert into Immunization(healthId,providerId,imuCode,imuDate,imuCard,imuSource,systemEntryDate,modifyDate)Values(";
                        SQL += "'" + txtHealthID.getText().toString() + "',";
                        SQL += "'" + g.getUserID() + "',";
                        SQL += "'2',";
                        SQL += "'" + (dtpDOTT2.getText().toString().length()==0?"":dtpDOTT2.getText().toString()) +"',";
                        if(ChkTT2.isChecked() & ChkCard2.isChecked())
                            SQL += "'1',";
                        else if(ChkTT2.isChecked() & !ChkCard2.isChecked())
                            SQL += "'2',";
                        else
                            SQL += "'',";

                        SQL += "'',";
                        SQL += "'" + Global.DateTimeNowDMYHMS() + "',";
                        SQL += "'" + Global.DateTimeNowDMYHMS() + "')";
                    }
                    else
                    {
                        SQL = "Update Immunization set ";
                        SQL += "imuDate='" + (dtpDOTT2.getText().toString().length()==0?"":dtpDOTT2.getText().toString()) +"',";
                        if(ChkTT2.isChecked() & ChkCard2.isChecked())
                            SQL += "imuCard='1'";
                        else if(ChkTT2.isChecked() & !ChkCard2.isChecked())
                            SQL += "imuCard='2'";
                        else
                            SQL += "imuCard=''";
                        SQL += " where healthid='"+ txtHealthID.getText().toString() +"' and imuCode='2'";
                    }
                    C.Save(SQL);
                }
            if(ChkTT3.isChecked()) {
                if(!C.Existence("Select healthid from Immunization  Where healthid='"+ txtHealthID.getText().toString() +"' and imuCode='3'")) {
                    SQL = "Insert into Immunization(healthId,providerId,imuCode,imuDate,imuCard,imuSource,systemEntryDate,modifyDate)Values(";
                    SQL += "'" + txtHealthID.getText().toString() + "',";
                    SQL += "'" + g.getUserID() + "',";
                    SQL += "'3',";
                    SQL += "'" + (dtpDOTT3.getText().toString().length()==0?"":dtpDOTT3.getText().toString()) +"',";
                    if(ChkTT3.isChecked() & ChkCard3.isChecked())
                        SQL += "'1',";
                    else if(ChkTT3.isChecked() & !ChkCard3.isChecked())
                        SQL += "'2',";
                    else
                        SQL += "'',";

                    SQL += "'',";
                    SQL += "'" + Global.DateTimeNowDMYHMS() + "',";
                    SQL += "'" + Global.DateTimeNowDMYHMS() + "')";
                }
                else
                {
                    SQL = "Update Immunization set ";
                    SQL += "imuDate='" + (dtpDOTT3.getText().toString().length()==0?"":dtpDOTT3.getText().toString()) +"',";
                    if(ChkTT3.isChecked() & ChkCard3.isChecked())
                        SQL += "imuCard='1'";
                    else if(ChkTT3.isChecked() & !ChkCard3.isChecked())
                        SQL += "imuCard='2'";
                    else
                        SQL += "imuCard=''";
                    SQL += " where healthid='"+ txtHealthID.getText().toString() +"' and imuCode='3'";
                }
                C.Save(SQL);
            }
            if(ChkTT4.isChecked()) {
                if(!C.Existence("Select healthid from Immunization  Where healthid='"+ txtHealthID.getText().toString() +"' and imuCode='4'")) {
                    SQL = "Insert into Immunization(healthId,providerId,imuCode,imuDate,imuCard,imuSource,systemEntryDate,modifyDate)Values(";
                    SQL += "'" + txtHealthID.getText().toString() + "',";
                    SQL += "'" + g.getUserID() + "',";
                    SQL += "'4',";
                    SQL += "'" + (dtpDOTT4.getText().toString().length()==0?"":dtpDOTT4.getText().toString()) +"',";
                    if(ChkTT4.isChecked() & ChkCard4.isChecked())
                        SQL += "'1',";
                    else if(ChkTT4.isChecked() & !ChkCard4.isChecked())
                        SQL += "'2',";
                    else
                        SQL += "'',";

                    SQL += "'',";
                    SQL += "'" + Global.DateTimeNowDMYHMS() + "',";
                    SQL += "'" + Global.DateTimeNowDMYHMS() + "')";
                }
                else
                {
                    SQL = "Update Immunization set ";
                    SQL += "imuDate='" + (dtpDOTT4.getText().toString().length()==0?"":dtpDOTT4.getText().toString()) +"',";
                    if(ChkTT4.isChecked() & ChkCard4.isChecked())
                        SQL += "imuCard='1'";
                    else if(ChkTT4.isChecked() & !ChkCard4.isChecked())
                        SQL += "imuCard='2'";
                    else
                        SQL += "imuCard=''";
                    SQL += " where healthid='"+ txtHealthID.getText().toString() +"' and imuCode='4'";
                }
                C.Save(SQL);
            }
            if(ChkTT5.isChecked()) {
                if(!C.Existence("Select healthid from Immunization  Where healthid='"+ txtHealthID.getText().toString() +"' and imuCode='5'")) {
                    SQL = "Insert into Immunization(healthId,providerId,imuCode,imuDate,imuCard,imuSource,systemEntryDate,modifyDate)Values(";
                    SQL += "'" + txtHealthID.getText().toString() + "',";
                    SQL += "'" + g.getUserID() + "',";
                    SQL += "'5',";
                    SQL += "'" + (dtpDOTT5.getText().toString().length()==0?"":dtpDOTT5.getText().toString()) +"',";
                    if(ChkTT5.isChecked() & ChkCard5.isChecked())
                        SQL += "'1',";
                    else if(ChkTT5.isChecked() & !ChkCard5.isChecked())
                        SQL += "'2',";
                    else
                        SQL += "'',";

                    SQL += "'',";
                    SQL += "'" + Global.DateTimeNowDMYHMS() + "',";
                    SQL += "'" + Global.DateTimeNowDMYHMS() + "')";
                }
                else
                {
                    SQL = "Update Immunization set ";
                    SQL += "imuDate='" + (dtpDOTT5.getText().toString().length()==0?"":dtpDOTT5.getText().toString()) +"',";
                    if(ChkTT5.isChecked() & ChkCard5.isChecked())
                        SQL += "imuCard='1'";
                    else if(ChkTT5.isChecked() & !ChkCard5.isChecked())
                        SQL += "imuCard='2'";
                    else
                        SQL += "imuCard=''";
                    SQL += " where healthid='"+ txtHealthID.getText().toString() +"' and imuCode='5'";
                }
                C.Save(SQL);
            }

            */


            /*
            String CurrentMethod="";
            if(PreMethodStatus.equalsIgnoreCase(""))
            {
                CurrentMethod="N";
            }
            else  if(PreMethodStatus.equals(Global.Left(spnMethod.getSelectedItem().toString(), 2)))
            {
                CurrentMethod="O";
            }
            else if(!PreMethodStatus.equals(Global.Left(spnMethod.getSelectedItem().toString(), 2)))
            {
                CurrentMethod="N";
            }

            if(Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("12")) {
                SQL = "Insert into " + TableNameELCOVisit + "(Dist,Upz,UN,Mouza,Vill,ProvType,ProvCode,HHNo,SNo,Visit,VDate,VisitStatus,CurrStatus,NewOld,MSDate,SSource,Qty,Unit,Brand,EnDt,Upload,UploadDT)Values('" + g.getDistrict() + "','" + g.getUpazila() + "','" + g.getUnion() + "','" + g.getMouza() + "','" + g.getVillage() + "','"+ g.getProvType() +"','"+ g.getProvCode() +"','" + g.getHouseholdNo() + "','" + txtSNo.getText() + "','" + VisitNumber() + "','" + Global.DateConvertYMD(dtpDOV.getText().toString()) + "','" + VStatus + "','','" + CurrentMethod +"','" + Global.DateConvertYMD(dtpDOMS.getText().toString()) + "','" + SSource + "','" + txtMethodQty.getText().toString() + "','" + spnMethodUnit.getSelectedItemPosition() + "','" + spnBrand.getSelectedItemPosition() + "','" + Global.DateTimeNowYMDHMS() + "','2','')";
                C.Save(SQL);
            }
            else if(Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("13")) {
                SQL = "Insert into " + TableNameELCOVisit + "(Dist,Upz,UN,Mouza,Vill,ProvType,ProvCode,HHNo,SNo,Visit,VDate,VisitStatus,CurrStatus,NewOld,MSDate,SSource,Qty,Unit,Brand,EnDt,Upload,UploadDT)Values('" + g.getDistrict() + "','" + g.getUpazila() + "','" + g.getUnion() + "','" + g.getMouza() + "','" + g.getVillage() + "','"+ g.getProvType() +"','"+ g.getProvCode() +"','" + g.getHouseholdNo() + "','" + txtSNo.getText() + "','" + VisitNumber() + "','" + Global.DateConvertYMD(dtpDOV.getText().toString()) + "','" + VStatus + "','','" + CurrentMethod +"','" + Global.DateConvertYMD(dtpDOMS.getText().toString()) + "','" + SSource + "','" + txtMethodQty.getText().toString() + "','" + spnMethodUnit.getSelectedItemPosition() + "','" + spnBrand.getSelectedItemPosition() + "','" + Global.DateTimeNowYMDHMS() + "','2','')";
                C.Save(SQL);
            }
            else
            {
                SQL = "Insert into " + TableNameELCOVisit + "(Dist,Upz,UN,Mouza,Vill,ProvType,ProvCode,HHNo,SNo,Visit,VDate,VisitStatus,CurrStatus,NewOld,MSDate,SSource,Qty,Unit,Brand,EnDt,Upload,UploadDT)Values('" + g.getDistrict() + "','" + g.getUpazila() + "','" + g.getUnion() + "','" + g.getMouza() + "','" + g.getVillage() + "','"+ g.getProvType() +"','"+ g.getProvCode() +"','" + g.getHouseholdNo() + "','" + txtSNo.getText() + "','" + VisitNumber() + "','" + Global.DateConvertYMD(dtpDOV.getText().toString()) + "','" + VStatus + "','" + (spnMethod.getSelectedItemPosition() == 0 ? "" : Global.Left(spnMethod.getSelectedItem().toString(), 2)) + "','" + CurrentMethod +"','" + Global.DateConvertYMD(dtpDOMS.getText().toString()) + "','" + SSource + "','" + txtMethodQty.getText().toString() + "','" + spnMethodUnit.getSelectedItemPosition() + "','" + spnBrand.getSelectedItemPosition() + "','" + Global.DateTimeNowYMDHMS() + "','2','')";
                C.Save(SQL);
            }

            Connection.MessageBox(ELCOForm.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
            finish();
            if(Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("12"))
            {
                Intent f2 = new Intent(getApplicationContext(),PregReg.class);
                startActivity(f2);
            }
            else if(Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("13"))
            {
                Intent f2 = new Intent(getApplicationContext(),Deliv.class);
                startActivity(f2);
            }
            else if(Global.Left(spnMethod.getSelectedItem().toString(), 2).equalsIgnoreCase("14"))
            {
                Intent f2 = new Intent(getApplicationContext(),Deliv.class);
                startActivity(f2);
            }

            else
            {
                Intent f2 = new Intent(getApplicationContext(), MemberList.class);
                startActivity(f2);
            }
            */

            //ELCO Visit
            //String VisitNo = VisitNumber(txtHealthID.getText().toString());
            String VisitNo = txtVisitNo.getText().toString();
            if(!C.Existence("Select healthid from elcoVisit  Where healthid='"+ txtHealthID.getText().toString() +"' and Visit='"+ VisitNo +"'"))
            {
                SQL = "Insert into elcoVisit(healthId,providerId,visit,systemEntryDate,modifyDate)Values(";
                SQL += "'"+ txtHealthID.getText().toString() +"',";
                SQL += "'"+ g.getUserID() +"',";
                SQL += "'"+ VisitNo +"',";
                SQL += "'"+ Global.DateTimeNowDMYHMS() +"',";
                SQL += "'"+ Global.DateTimeNowDMYHMS() +"')";

                C.Save(SQL);
            }

            SQL = "Update elcoVisit Set ";
            SQL+="vDate = '"+ Global.DateConvertYMD(dtpDOV.getText().toString()) +"',";
            SQL+="visitStatus = '"+ (rdoVSPresent.isChecked()?"1":"2") +"',";
            if(spnMethod.getSelectedItemPosition()==0)
                SQL+="currStatus = '',";
            else
                SQL+="currStatus = '"+ Global.Left(spnMethod.getSelectedItem().toString(), 2) +"',";
            SQL+="newOld = 'n',";
            SQL+="msDate = '"+ Global.DateConvertYMD(dtpDOV.getText().toString()) +"',";
            String SS = "";
            if(rdoSSFWA.isChecked())        SS="1";
            if(rdoSSLMarket.isChecked())    SS="2";
            if(rdoSSOther.isChecked())      SS="3";
            if(rdoSSOtherGovt.isChecked())  SS="4";
            SQL+="sSource = '"+ SS +"',";
            SQL+="qty = '"+ txtMethodQty.getText().toString() +"',";
            SQL+="unit = '"+ (spnMethodUnit.getSelectedItemPosition()==0?"":String.valueOf(spnMethodUnit.getSelectedItemPosition())) +"',";
            SQL+="brand = '"+ (spnBrand.getSelectedItemPosition()==0?"":String.valueOf(spnBrand.getSelectedItemPosition())) +"',";
            SQL+="referPlace = '"+ (spnWherePlace.getSelectedItemPosition()==0?"":String.valueOf(spnWherePlace.getSelectedItemPosition())) +"',";
            SQL+="validity = '"+ txtValidity.getText().toString() +"',";
            SQL+="dayMonYear = '"+ (spnValidityUnit.getSelectedItemPosition()==0?"":String.valueOf(spnValidityUnit.getSelectedItemPosition())) +"'";

            SQL+=" Where HealthID='"+ txtHealthID.getText().toString() +"' and Visit='"+ VisitNo +"'";
            C.Save(SQL);

            String CurStatus = Global.Left(spnMethod.getSelectedItem().toString(), 2);

            ElcoVisitStsatus();

            spnMethod.setAdapter(null);
            if(CurrentStatus(txtHealthID.getText().toString()).equals("12"))
                spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select EvCode||'-'||EVName as EvCode from ElcoEvent where evcode in('12','13','14') order by EvCode asc"));
            else
                spnMethod.setAdapter(C.getArrayAdapter("Select '  'as EvCode union Select EvCode||'-'||EVName as EvCode from ElcoEvent where evcode not in('13','14') order by EvCode asc"));


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

            Connection.MessageBox(ELCOForm.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");

           // finish();

            //pregnant
            if(CurStatus.equalsIgnoreCase("12"))
            {
                Intent f2 = new Intent(getApplicationContext(),PregReg.class);
                startActivity(f2);
                finish();
            }
            //live birth
            else if(CurStatus.equalsIgnoreCase("13"))
            {
                Intent f2 = new Intent(getApplicationContext(),Deliv.class);
                Bundle IDbundle = new Bundle();
                IDbundle.putString("currentstatus","13");
                f2.putExtras(IDbundle);
                startActivity(f2);
                finish();
            }
            //Still birth/abortion
            else if(CurStatus.equalsIgnoreCase("14"))
            {
                Intent f2 = new Intent(getApplicationContext(),Deliv.class);
                Bundle IDbundle = new Bundle();
                IDbundle.putString("currentstatus","14");
                f2.putExtras(IDbundle);
                startActivity(f2);
                finish();
            }
            //Injectable
            else if(CurStatus.equalsIgnoreCase("03"))
            {
                //Intent f2 = new Intent(getApplicationContext(),Deliv.class);
                //startActivity(f2);
            }
            else
            {
                Intent f2 = new Intent(getApplicationContext(), MemberList.class);
                startActivity(f2);
                finish();
            }

        }
        catch(Exception  e)
        {
            Connection.MessageBox(ELCOForm.this, e.getMessage());
            return;
        }
    }

    //ELCO Immunization
    private void ImmunizationSave(String ImuIDNo)
    {
        String SQL = "";
            if(!C.Existence("Select healthid from Immunization  Where healthid='"+ txtHealthID.getText().toString() +"' and imuCode='"+ ImuIDNo +"'")) {
                SQL = "Insert into Immunization(healthId,providerId,imuCode,imuDate,imuCard,imuSource,systemEntryDate,modifyDate)Values(";
                SQL += "'" + txtHealthID.getText().toString() + "',";
                SQL += "'" + g.getUserID() + "',";
                SQL += "'"+ ImuIDNo +"',";
                SQL += "'" + (dtpDOTT1.getText().toString().length()==0?"":Global.DateConvertYMD(dtpDOTT1.getText().toString())) +"',";
                if(ChkTT1.isChecked() & rdoCardYes.isChecked())
                    SQL += "'1',";
                else if(ChkTT1.isChecked() & rdoCardNo.isChecked())
                    SQL += "'2',";
                else
                    SQL += "'',";

                SQL += "'',";
                SQL += "'" + Global.DateTimeNowDMYHMS() + "',";
                SQL += "'" + Global.DateTimeNowDMYHMS() + "')";
            }
            else
            {
                SQL = "Update Immunization set ";
                SQL += "imuDate='" + (dtpDOTT1.getText().toString().length()==0?"":Global.DateConvertYMD(dtpDOTT1.getText().toString())) +"',";
                if(ChkTT1.isChecked() & rdoCardYes.isChecked())
                    SQL += "imuCard='1'";
                else if(ChkTT1.isChecked() & rdoCardNo.isChecked())
                    SQL += "imuCard='2'";
                else
                    SQL += "imuCard=''";
                SQL += " where healthid='"+ txtHealthID.getText().toString() +"' and imuCode='"+ ImuIDNo +"'";
            }
            C.Save(SQL);
    }



    private void ImmunizationData(String healthId)
    {
        Cursor cur = C.ReadData("Select imuCode,imuDate,imuCard,imuSource,systemEntryDate,modifyDate from Immunization where healthid='"+ healthId +"' order by cast(imuDate as date) asc");//imuDate
        cur.moveToFirst();
        while(!cur.isAfterLast())
        {
            if(cur.getString(cur.getColumnIndex("imuCode")).equals("1"))
            {
                ChkTT1.setChecked(true);
                if(cur.getString(cur.getColumnIndex("imuCard")).equals("1"))  rdoCardYes.setChecked(true);
                if(cur.getString(cur.getColumnIndex("imuDate")).length()>0) dtpDOTT1.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("imuDate"))));
            }

            cur.moveToNext();
        }
        cur.close();
    }

    private void DataSearch(String healthId)
    {
        try
        {
            String SQL = "";
            SQL  = " Select Dist, Upz, UN, Mouza, Vill, HHNo, SNo as SNo, ifnull(HealthID,'') as HealthID, ifnull(NameEng,'') as NameEng,";
            SQL += " ifnull(NameBang,'') as NameBang, ifnull(Rth,'') as Rth, ifnull(HaveNID,'') as HaveNID, ifnull(NID,'') as NID, ifnull(NIDStatus,'') as NIDStatus, ifnull(HaveBR,'') as HaveBR, ifnull(BRID,'') as BRID, ifnull(BRIDStatus,'') as BRIDStatus, ifnull(MobileNo1,'') as MobileNo1,";
            SQL += " ifnull(MobileNo2,'') as MobileNo2, ifnull(DOB,'') as DOB, Cast(((julianday(date('now'))-julianday(DOB))/365.25) as int) as Age, ifnull(DOBSource,'') as DOBSource, ifnull(BPlace,'') as BPlace, ifnull(FNo,'') as FNo, ifnull(Father,'') as Father, ifnull(MNo,'') as MNo, ifnull(Mother,'') as Mother,";
            SQL += " ifnull(Sex,'') as Sex, ifnull(MS,'') as MS, ifnull(SPNO1,'') as SPNO1,ifnull(SPNO2,'') as SPNO2,ifnull(SPNO3,'') as SPNO3,ifnull(SPNO4,'') as SPNO4, ifnull(ELCONo,'') as ELCONo, ifnull(ELCODontKnow,'') as ELCODontKnow, ifnull(EDU,'') as EDU, ifnull(Rel,'') as Rel, ifnull(Nationality,'') as Nationality, ifnull(OCP,'') as OCP";
            SQL += ",(select  NameEng  from member where Dist=(select  Dist  from member  Where  healthid='"+ healthId +"') and Upz=(select  Upz  from member  Where  healthid='"+ healthId +"') and UN=(select  UN  from member  Where  healthid='"+ healthId +"') and Mouza=(select  Mouza  from member  Where  healthid='"+ healthId +"') and Vill=(select  Vill  from member  Where  healthid='"+ healthId +"') and ProvCode=(select  ProvCode  from member  Where  healthid='"+ healthId +"') and HHNo=(select  HHNo  from member  Where  healthid='"+ healthId +"') and SNo=(select  SPNO1  from member  Where  healthid='"+ healthId +"'))as HusName";
            SQL += ",(select  Age  from member where Dist=(select  Dist  from member  Where  healthid='"+ healthId +"') and Upz=(select  Upz  from member  Where  healthid='"+ healthId +"') and UN=(select  UN  from member  Where  healthid='"+ healthId +"') and Mouza=(select  Mouza  from member  Where  healthid='"+ healthId +"') and Vill=(select  Vill  from member  Where  healthid='"+ healthId +"') and ProvCode=(select  ProvCode  from member  Where  healthid='"+ healthId +"') and HHNo=(select  HHNo  from member  Where  healthid='"+ healthId +"') and SNo=(select  SPNO1  from member  Where  healthid='"+ healthId +"'))as HusAge";
            SQL += " from Member where healthid='"+ healthId +"'";

            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();
            while(!cur.isAfterLast())
            {
                txtSNo.setText(cur.getString(cur.getColumnIndex("SNo")));
                DOB.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("DOB"))));
                txtName.setText(cur.getString(cur.getColumnIndex("NameEng")));
                txtAge.setText(cur.getString(cur.getColumnIndex("Age")));
                txtHusName.setText(cur.getString(cur.getColumnIndex("HusName")));
                txtHusAge.setText(cur.getString(cur.getColumnIndex("HusAge")));

                if(cur.getString(cur.getColumnIndex("EDU")).equals("00"))
                {
                    txtMEdu.setText("নার্সারি/কিন্ডার গার্ডেন");
                }
                else if(cur.getString(cur.getColumnIndex("EDU")).equals("01"))
                {
                    txtMEdu.setText("১ম শ্রেনী");
                }
                else if(cur.getString(cur.getColumnIndex("EDU")).equals("02"))
                {
                    txtMEdu.setText("২য় শ্রেনী");
                }
                else if(cur.getString(cur.getColumnIndex("EDU")).equals("03"))
                {
                    txtMEdu.setText("৩য় শ্রেনী");
                }
                else if(cur.getString(cur.getColumnIndex("EDU")).equals("04"))
                {
                    txtMEdu.setText("৪র্থ শ্রেনী");
                }
                else if(cur.getString(cur.getColumnIndex("EDU")).equals("05"))
                {
                    txtMEdu.setText("৫ম শ্রেনী");
                }
                else if(cur.getString(cur.getColumnIndex("EDU")).equals("06"))
                {
                    txtMEdu.setText("৬ষ্ঠ শ্রেনী");
                }
                else if(cur.getString(cur.getColumnIndex("EDU")).equals("07"))
                {
                    txtMEdu.setText("৭ম শ্রেনী");
                }
                else if(cur.getString(cur.getColumnIndex("EDU")).equals("08"))
                {
                    txtMEdu.setText("৮ম শ্রেনী");
                }
                else if(cur.getString(cur.getColumnIndex("EDU")).equals("09"))
                {
                    txtMEdu.setText("৯ম শ্রেনী");
                }
                else if(cur.getString(cur.getColumnIndex("EDU")).equals("10"))
                {
                    txtMEdu.setText("১০ম শ্রেনী বা সমতুল্য");
                }
                else if(cur.getString(cur.getColumnIndex("EDU")).equals("11"))
                {
                    txtMEdu.setText("উচ্চ মাধ্যমিক বা সমতুল্য");
                }
                else if(cur.getString(cur.getColumnIndex("EDU")).equals("12"))
                {
                    txtMEdu.setText("স্মাতক বা সমতুল্য");
                }
                else if(cur.getString(cur.getColumnIndex("EDU")).equals("13"))
                {
                    txtMEdu.setText("স্মাতকোত্তর বা সমতুল্য");
                }
                else if(cur.getString(cur.getColumnIndex("EDU")).equals("14"))
                {
                    txtMEdu.setText("ডাক্তারি");
                }
                else if(cur.getString(cur.getColumnIndex("EDU")).equals("15"))
                {
                    txtMEdu.setText("ইঞ্জিনিয়ারিং");
                }
                else if(cur.getString(cur.getColumnIndex("EDU")).equals("16"))
                {
                    txtMEdu.setText("বৃত্তিমুলক শিক্ষা");
                }
                else if(cur.getString(cur.getColumnIndex("EDU")).equals("17"))
                {
                    txtMEdu.setText("কারিগরি শিক্ষা");
                }
                else if(cur.getString(cur.getColumnIndex("EDU")).equals("18"))
                {
                    txtMEdu.setText("ধাত্রীবিদ্যা/নার্সিং");
                }
                else if(cur.getString(cur.getColumnIndex("EDU")).equals("19"))
                {
                    txtMEdu.setText("অন্যান্য");
                }
                else if(cur.getString(cur.getColumnIndex("EDU")).equals("77"))
                {
                    txtMEdu.setText("প্রযোজ্য নয়");
                }
                else if(cur.getString(cur.getColumnIndex("EDU")).equals("99"))
                {
                    txtMEdu.setText("শিক্ষাগত যোগ্যতা নেই");
                }

                if (cur.getString(cur.getColumnIndex("HaveNID")).equals("1"))
                {
                    txtMNID.setText(cur.getString(cur.getColumnIndex("NID")));
                }
                else
                {
                    if(cur.getString(cur.getColumnIndex("NIDStatus")).equals("1"))
                    {
                        txtMNID.setText("কখনো ছিল না");
                    }
                    else if(cur.getString(cur.getColumnIndex("NIDStatus")).equals("2"))
                    {
                        txtMNID.setText("হারিয়ে ফেলেছি");
                    }
                    else if(cur.getString(cur.getColumnIndex("NIDStatus")).equals("3"))
                    {
                        txtMNID.setText("খুজে পাচ্ছি না");
                    }
                    else if(cur.getString(cur.getColumnIndex("NIDStatus")).equals("4"))
                    {
                        txtMNID.setText("অন্য জায়গায় আছে");
                    }
                    else if(cur.getString(cur.getColumnIndex("NIDStatus")).equals("7"))
                    {
                        txtMNID.setText("নাগরিক নয়");
                    }
                }

                if (cur.getString(cur.getColumnIndex("HaveBR")).equals("1"))
                {
                    txtMBRID.setText(cur.getString(cur.getColumnIndex("BRID")));
                }
                else
                {
                    if(cur.getString(cur.getColumnIndex("BRIDStatus")).equals("1"))
                    {
                        txtMBRID.setText("কখনো ছিল না");
                    }
                    else if(cur.getString(cur.getColumnIndex("BRIDStatus")).equals("2"))
                    {
                        txtMBRID.setText("হারিয়ে ফেলেছি");
                    }
                    else if(cur.getString(cur.getColumnIndex("BRIDStatus")).equals("3"))
                    {
                        txtMBRID.setText("খুজে পাচ্ছি না");
                    }
                    else if(cur.getString(cur.getColumnIndex("BRIDStatus")).equals("4"))
                    {
                        txtMBRID.setText("অন্য জায়গায় আছে");
                    }
                    else if(cur.getString(cur.getColumnIndex("BRIDStatus")).equals("7"))
                    {
                        txtMBRID.setText("নাগরিক নয়");
                    }
                }

                if (cur.getString(cur.getColumnIndex("MobileNo1")).equals(""))
                {
                    txtMMNo.setText(cur.getString(cur.getColumnIndex("MobileNo2")));
                }
                else if (cur.getString(cur.getColumnIndex("MobileNo2")).equals(""))
                {
                    txtMMNo.setText(cur.getString(cur.getColumnIndex("MobileNo1")));
                }
                else if (!cur.getString(cur.getColumnIndex("MobileNo1")).equals(""))
                {
                    txtMMNo.setText(cur.getString(cur.getColumnIndex("MobileNo1")));
                }
                else if (!cur.getString(cur.getColumnIndex("MobileNo2")).equals(""))
                {
                    txtMMNo.setText(cur.getString(cur.getColumnIndex("MobileNo1")));
                }
                cur.moveToNext();
            }
            cur.close();

            //Husband Info
            /*
            Cursor h = C.ReadData("Select ");
            h.moveToFirst();
            while(!h.isAfterLast())
            {
                txtHusName.setText(h.getString(h.getColumnIndex("HusName")));
                txtHusAge.setText(h.getString(h.getColumnIndex("HusAge")));
                h.moveToNext();
            }
            h.close();
            */
        }
        catch(Exception  e)
        {
            Connection.MessageBox(ELCOForm.this, e.getMessage());
            return;
        }
    }

    private void ELCOMemSearch(String HealthID)
    {
        try
        {
            String SQL = "";
            SQL = "Select healthId,providerId,hhStatus as HHStatus,haHHNo as HAHHNo,elcoNo as ELCONo,husbandName as husbandName,domSource as DOMSource,marrDate as MarrDate,marrAge as MarrAge,son as Son,dau as Dau,regDT";
            SQL += " from ELCO Where healthId='"+ HealthID +"'";
            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();
            while(!cur.isAfterLast())
            {
                if(cur.getString(cur.getColumnIndex("HHStatus")).equals("1"))
                {
                    chkHHNoDontKnow.setChecked(true);
                }
                else if(cur.getString(cur.getColumnIndex("HHStatus")).equals("2"))
                {
                    chkHHNoDontKnow.setChecked(false);
                    txtHHNo.setText(cur.getString(cur.getColumnIndex("HAHHNo")));
                }

                txtELCONo.setText(cur.getString(cur.getColumnIndex("ELCONo")));
                txtLiveSon.setText(cur.getString(cur.getColumnIndex("Son")));
                txtLiveDau.setText(cur.getString(cur.getColumnIndex("Dau")));
                dtpDOM.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("MarrDate"))));
                txtMAge.setText(cur.getString(cur.getColumnIndex("MarrAge")));
                if(cur.getString(cur.getColumnIndex("DOMSource")).equals( "1" ))
                    rdoDOMSource1.setChecked( true );
                else
                    rdoDOMSource2.setChecked( true );

                cur.moveToNext();
            }
            cur.close();
        }
        catch(Exception  e)
        {
            Connection.MessageBox(ELCOForm.this, e.getMessage());
            return;
        }
    }
    private void ELCOMemUpdateVisit(String Dist, String Upz, String UN, String Mouza, String Vill, String HHNo, String SNo)
    {
        try
        {
            String SQL = "";
            SQL = "Select VDate,VisitStatus,CurrStatus,MSDate,SSource,MethodQty,MethodUnit,Brand,MNutrStatus,MNutrQty,MNutrUnit";
            SQL += " from ELCOVisit Where Dist='"+ Dist +"' and Upz='"+ Upz +"' and UN='"+ UN +"' and Mouza='"+ Mouza +"' and Vill='"+ Vill +"' and HHNo='"+ HHNo +"' and SNo='"+ SNo +"' and visit=(select max(visit) from ELCOVisit ";
            SQL += " Where Dist='"+ Dist +"' and Upz='"+ Upz +"' and UN='"+ UN +"' and Mouza='"+ Mouza +"' and Vill='"+ Vill +"' and HHNo='"+ HHNo +"' and SNo='"+ SNo +"')";
            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();
            while(!cur.isAfterLast())
            {
                if(cur.getString(cur.getColumnIndex("VisitStatus")).equals( "1" )) {
                    rdoVSPresent.setChecked(true);
                    secVS.setVisibility(View.VISIBLE);
                }
                else {
                    rdoVSAbsent.setChecked(true);
                    secVS.setVisibility(View.GONE);
                }
                spnMethod.setSelection(Global.SpinnerItemPosition(spnMethod, 2, cur.getString(cur.getColumnIndex("CurrStatus"))));
                if(cur.getString(cur.getColumnIndex("CurrStatus")).equals( "01" ))
                {

                    secSS.setVisibility(View.VISIBLE);
                    secMethodUnit.setVisibility(View.VISIBLE);
                    secBrand.setVisibility(View.VISIBLE);
                }
                else if(cur.getString(cur.getColumnIndex("CurrStatus")).equals("02"))
                {

                    secSS.setVisibility(View.VISIBLE);
                    secMethodUnit.setVisibility(View.VISIBLE);
                }
                else if(cur.getString(cur.getColumnIndex("CurrStatus")).equals("03"))
                {

                    secSS.setVisibility(View.VISIBLE);
                    secMethodUnit.setVisibility(View.VISIBLE);
                }

                if(cur.getString(cur.getColumnIndex("SSource")).equals( "1" ))
                {
                    rdoSSFWA.setChecked(true);
                }
                else if(cur.getString(cur.getColumnIndex("SSource")).equals("2"))
                {
                    rdoSSOther.setChecked(true);
                }
                else if(cur.getString(cur.getColumnIndex("SSource")).equals("3"))
                {
                    rdoSSLMarket.setChecked(true);
                }
                txtMethodQty.setText(cur.getString(cur.getColumnIndex("MethodQty")));
                if(cur.getString(cur.getColumnIndex("MethodUnit")).equals( "1" ))
                {
                    spnMethodUnit.setSelection(1);
                }
                else if(cur.getString(cur.getColumnIndex("MethodUnit")).equals( "2" ))
                {
                    spnMethodUnit.setSelection(2);
                }
                else if(cur.getString(cur.getColumnIndex("MethodUnit")).equals( "3" ))
                {
                    spnMethodUnit.setSelection(3);
                }
                if(cur.getString(cur.getColumnIndex("Brand")).equals( "1" ))
                {
                    spnBrand.setSelection(1);
                }
                else if(cur.getString(cur.getColumnIndex("Brand")).equals( "2" ))
                {
                    spnBrand.setSelection(2);
                }

                cur.moveToNext();
            }
            cur.close();
        }
        catch(Exception  e)
        {
            Connection.MessageBox(ELCOForm.this, e.getMessage());
            return;
        }
    }


    public class ElcoVisit extends BaseAdapter {
        private Context mContext;
        String[][] vcode;
        String MouzaCode;
        Integer totalRec;

        public ElcoVisit(Context c) {
            mContext = c;
        }
        public int getCount() {
            return Integer.parseInt(C.ReturnSingleValue("Select count(*)Total from elcoVisit where healthid='"+ g.getHealthID() +"' order by vdate desc limit 10"));
        }

        public Object getItem(int position){
            return null;
        }

        public long getItemId(int position){
            return 0;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            View MyView = convertView;
            if (convertView == null) {
                LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                MyView = li.inflate(R.layout.elcovisit_item, null);

                String SQL  = "Select currStatus as currstatus,vdate as vdate, visit as visit from elcoVisit where healthid='"+ g.getHealthID() +"' order by cast(vdate as date) asc";

                try {
                        Cursor cur = C.ReadData(SQL);
                        cur.moveToFirst();

                        totalRec = cur.getCount();
                        vcode=new String[4][totalRec];
                        int i=0;
                        String M = "";
                        while(!cur.isAfterLast())
                        {
                            vcode[0][i]= cur.getString(cur.getColumnIndex("currstatus"));
                            vcode[1][i]= cur.getString(cur.getColumnIndex("vdate"));

                            M = cur.getString(cur.getColumnIndex("currstatus"));

                            if(M.equals("01"))      vcode[2][i]= "খাবার বড়ি";
                            else if(M.equals("02")) vcode[2][i]= "কনডম";
                            else if(M.equals("03")) vcode[2][i]= "ইনজেকটেবল";
                            else if(M.equals("04")) vcode[2][i]= "আই ইউ ডি";
                            else if(M.equals("05")) vcode[2][i]= "ইমপ্যান্ট";
                            else if(M.equals("06")) vcode[2][i]= "স্থায়ী পদ্ধতি(পুরুষ)";
                            else if(M.equals("07")) vcode[2][i]= "স্থায়ী পদ্ধতি(মহিলা)";
                            else if(M.equals("08")) vcode[2][i]= "ইসিপি ";
                            else if(M.equals("09")) vcode[2][i]= "মিসোপ্রোস্টোল";
                            else if(M.equals("10")) vcode[2][i]= "পার্শ্ব-প্রতিক্রিয়ার জন্য প্রেরন ";
                            else if(M.equals("11")) vcode[2][i]= "পদ্ধতির জন্য প্রেরন";
                            else if(M.equals("12")) vcode[2][i]= "গর্ভবতী ";
                            else if(M.equals("13")) vcode[2][i]= "জীবিত জন্ম ";
                            else if(M.equals("14")) vcode[2][i]= "গর্ভ খালাস (জীবিত জন্ম ছাড়া)";
                            else if(M.equals("15")) vcode[2][i]= "জরায়ু অপারেশন করে অপসারন";
                            else if(M.equals("16")) vcode[2][i]= "স্বামী বিদেশ থাকলে ";
                            else if(M.equals("17")) vcode[2][i]= "বন্ধ্যাত্ব বিষয়ক তথ্য ";
                            else if(M.equals("18")) vcode[2][i]= "অন্য যে কোন অবস্থা ";
                            else vcode[2][i]= "অনুপস্থিত";

                            vcode[3][i]= cur.getString(cur.getColumnIndex("visit"));

                            i +=1;
                            cur.moveToNext();
                        }
                        cur.close();

                    Button tv = (Button)MyView.findViewById(R.id.image_name);
                    tv.setTextSize(14);
                    tv.setText(vcode[2][position] + "\n" + Global.DateConvertDMY(vcode[1][position]));
                    final Integer p = position;
                    tv.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Cursor cur = C.ReadData("Select visit as visit,vdate as vdate,visitstatus as visitstatus,currstatus as currstatus,ssource as ssource,qty as qty,unit as unit,brand as brand,validity as validity,daymonyear as dmy, ifnull(referPlace,'') as referplace from ElcoVisit where healthid='"+ txtHealthID.getText().toString() +"' and visit='"+ vcode[3][position] +"'");
                            cur.moveToFirst();

                            while(!cur.isAfterLast())
                            {
                                txtVisitNo.setText(cur.getString(cur.getColumnIndex("visit")));
                                if(cur.getString(cur.getColumnIndex("visitstatus")).equals("1"))
                                {
                                    rdoVSPresent.setChecked(true);
                                }
                                else if(cur.getString(cur.getColumnIndex("visitstatus")).equals("2"))
                                {
                                    rdoVSAbsent.setChecked(true);
                                }

                                spnMethod.setSelection(Global.SpinnerItemPosition(spnMethod, 2, cur.getString(cur.getColumnIndex("currstatus"))));

                                if(cur.getString(cur.getColumnIndex("ssource")).equals("1"))      rdoSSFWA.setChecked(true);
                                else if(cur.getString(cur.getColumnIndex("ssource")).equals("2")) rdoSSLMarket.setChecked(true);
                                else if(cur.getString(cur.getColumnIndex("ssource")).equals("3")) rdoSSOther.setChecked(true);
                                else if(cur.getString(cur.getColumnIndex("ssource")).equals("4")) rdoSSOtherGovt.setChecked(true);

                                if(cur.getString(cur.getColumnIndex("brand")).equals("1"))      spnBrand.setSelection(1);
                                else if(cur.getString(cur.getColumnIndex("brand")).equals("2")) spnBrand.setSelection(2);
                                else if(cur.getString(cur.getColumnIndex("brand")).equals("3")) spnBrand.setSelection(3);

                                txtMethodQty.setText(cur.getString(cur.getColumnIndex("qty")));

                                if(cur.getString(cur.getColumnIndex("unit")).equals("1"))      spnMethodUnit.setSelection(1);
                                else if(cur.getString(cur.getColumnIndex("unit")).equals("2")) spnMethodUnit.setSelection(2);
                                else if(cur.getString(cur.getColumnIndex("unit")).equals("3")) spnMethodUnit.setSelection(3);

                                spnWherePlace.setSelection(Global.SpinnerItemPosition(spnWherePlace, 2, cur.getString(cur.getColumnIndex("referplace"))));

                                txtValidity.setText(cur.getString(cur.getColumnIndex("validity")));

                                if(cur.getString(cur.getColumnIndex("dmy")).equals("1"))      spnValidityUnit.setSelection(1);
                                else if(cur.getString(cur.getColumnIndex("dmy")).equals("2")) spnValidityUnit.setSelection(2);
                                else if(cur.getString(cur.getColumnIndex("dmy")).equals("3")) spnValidityUnit.setSelection(3);

                                cur.moveToNext();
                            }
                            cur.close();


                            if(String.valueOf(vcode[0][position]).equalsIgnoreCase("12")) {

                                Intent f2 = new Intent(getApplicationContext(), PregReg.class);
                                startActivity(f2);
                                finish();
                            }
                            else if(String.valueOf(vcode[0][position]).equalsIgnoreCase("13")) {
                                Intent f2 = new Intent(getApplicationContext(), Deliv.class);
                                Bundle IDbundle = new Bundle();
                                IDbundle.putString("currentstatus","13");


                                f2.putExtras(IDbundle);
                                startActivity(f2);
                                finish();
                            }
                            else if(String.valueOf(vcode[0][position]).equalsIgnoreCase("14")) {
                                Intent f2 = new Intent(getApplicationContext(), Deliv.class);
                                Bundle IDbundle = new Bundle();
                                IDbundle.putString("currentstatus","14");
                                f2.putExtras(IDbundle);
                                startActivity(f2);
                                finish();
                            }
                        }
                    });
                }
                catch(Exception ex)
                {
                    Connection.MessageBox(ELCOForm.this,ex.getMessage());
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
            return Integer.parseInt(C.ReturnSingleValue("Select count(*)total from Immunization where healthid='"+ g.getHealthID() +"'"));
        }

        public Object getItem(int position){
            return null;
        }

        public long getItemId(int position){
            return 0;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            View MyView = convertView;
            if (convertView == null) {
                LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                MyView = li.inflate(R.layout.tt_item, null);

                String SQL  = "Select imucode as imucode,imudate as imudate,imuCard as imucard from Immunization where healthid='"+ g.getHealthID() +"' order by date(imudate) asc";

                try {
                    Cursor cur = C.ReadData(SQL);
                    cur.moveToFirst();

                    totalRec = cur.getCount();
                    vcode=new String[4][totalRec];
                    int i=0;
                    while(!cur.isAfterLast())
                    {
                        vcode[0][i]= "টিটি "+ String.valueOf(i+1);//String.valueOf(cur.getString(cur.getColumnIndex("imucode")));
                        vcode[1][i]= Global.DateConvertDMY(cur.getString(cur.getColumnIndex("imudate")));
                        vcode[2][i]= cur.getString(cur.getColumnIndex("imucard"));
                        vcode[3][i]= String.valueOf(cur.getString(cur.getColumnIndex("imucode")));

                        i +=1;
                        cur.moveToNext();
                    }
                    cur.close();

                    Button tv = (Button)MyView.findViewById(R.id.image_name);
                    tv.setTextSize(14);
                    tv.setText(vcode[0][position] + "\n" + vcode[1][position]);
                    final Integer p = position;
                    tv.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            ChkTT1.setChecked(true);
                            if(vcode[2][position].equals("1"))
                            {
                                rdoCardYes.setChecked(true);
                            }
                            else if(vcode[2][position].equals("2"))
                            {
                                rdoCardNo.setChecked(true);
                            }

                            if(vcode[1][position].length()!=0)
                            {
                               dtpDOTT1.setText(vcode[1][position]);
                            }
                            else
                            {
                                dtpDOTT1.setText("");
                            }

                            secTT1.setVisibility(View.VISIBLE);
                            btnTTClose.setVisibility(View.VISIBLE);
                            btnAddTT.setVisibility(View.GONE);
                            g.setImuCode(vcode[3][position]);
                        }
                    });
                }
                catch(Exception ex)
                {
                    Connection.MessageBox(ELCOForm.this,ex.getMessage());
                }

            }
            return MyView;
        }

    }



    protected Dialog onCreateDialog(int id) {
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        Integer Y=g.mYear;
        Integer M=g.mMonth;
        Integer D=g.mDay;
       /* if(dtpDOM.getText().length()>0)
        {
            Y = Integer.valueOf(Global.Right(dtpDOM.getText().toString(), 4));
            M = Integer.valueOf(dtpDOM.getText().toString().substring(4,5));
            D = Integer.valueOf(Global.Left(dtpDOM.getText().toString(), 2));
        }*/
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mDateSetListener, Y, M-1,D);
            case TIME_DIALOG:
                return new TimePickerDialog(this, timePickerListener, hour, minute,false);
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
            } /*else if (VariableID.equals("btnDOTT2")) {
                dtpDate = (EditText) findViewById(R.id.dtpDOTT2);
            } else if (VariableID.equals("btnDOTT3")) {
                dtpDate = (EditText) findViewById(R.id.dtpDOTT3);
            } else if (VariableID.equals("btnDOTT4")) {
                dtpDate = (EditText) findViewById(R.id.dtpDOTT4);
            } else if (VariableID.equals("btnDOTT5")) {
                dtpDate = (EditText) findViewById(R.id.dtpDOTT5);
            }*/


            dtpDate.setText(new StringBuilder()
                    .append(Global.Right("00" + mDay, 2)).append("/")
                    .append(Global.Right("00" + mMonth, 2)).append("/")
                    .append(mYear));
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = sdf.format(c.getTime());
                Date date1 = sdf.parse(formattedDate);

                Date date2 = sdf.parse(dtpDate.getText().toString());

                if (date2.after(date1))
                {

                    if (VariableID.equals("btnDOV"))
                    {
                        Connection.MessageBox(ELCOForm.this, "তারিখ আজকের তারিখের সমান অথবা কম হতে হবে।");
                        return;
                    }
                    //dtpDate.setText(null);
                }
                String DOB1 = "15/" + DOB.getText() + "/" + DOB.getText();
                Date DOB11 = sdf.parse(DOB1);
                if(date2.before(DOB11))
                {

                    if (VariableID.equals("btnB11"))
                    {
                        Connection.MessageBox(ELCOForm.this,"আপনার সর্বশেষ গর্ভ ফলাফলের তারিখ  জন্ম তারিখ অপেক্ষা বড় হবে না");
                        return;
                    }


                    //dtpDate.setText(null);


                }
            }
            catch(ParseException ex)
            {
                ex.printStackTrace();
            }

        }
    };
    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
            hour = selectedHour; minute = selectedMinute;
            EditText tpTime;

        }
    };
}