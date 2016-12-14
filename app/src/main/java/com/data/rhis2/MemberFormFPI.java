package com.data.rhis2;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import Common.Connection;
import Common.Global;

/**
 * Created by ccah on 3/15/2016.
 */
public class MemberFormFPI extends Activity {
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
        AlertDialog.Builder adb = new AlertDialog.Builder(MemberFormFPI.this);
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
                            Intent f2 = new Intent(getApplicationContext(), MemberListFPI.class);
                            startActivity(f2);
                        } else if (g.getCallFrom().equalsIgnoreCase("V")) {
                            Intent f2 = new Intent(getApplicationContext(), VillageList.class);
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
    TextView txtNameEng;
    LinearLayout secNameBang;
    TextView VlblNameBang;
    TextView txtNameBang;
    TextView txtRth;

    LinearLayout secHaveNID;
    TextView VlblHaveNID;
    RadioGroup rdogrpHaveNID;
    RadioButton rdoHaveNID1;
    RadioButton rdoHaveNID2;
    CheckBox chkNIDDontKnow;

    LinearLayout secNID;
    TextView VlblNID;
    TextView txtNID;
    LinearLayout secNIDStatus;
    TextView VlblNIDStatus;
    TextView txtNIDStatus;
    CheckBox chkBRIDDontKnow;

    LinearLayout secNIDStatus1;

    LinearLayout secHaveBR;
    TextView VlblHaveBR;

    RadioGroup rdogrpHaveBR;
    RadioButton rdoHaveBR1;
    RadioButton rdoHaveBR2;


    LinearLayout secBRID;
    TextView VlblBRID;
    TextView txtBRID;
    LinearLayout secBRIDStatus;
    TextView VlblBRIDStatus;
    TextView txtBRIDStatus;
    LinearLayout secBRIDStatus1;
    LinearLayout secMobileNo1;
    TextView VlblMobileNo1;
    TextView txtMobileNo1;
    LinearLayout secMobileNo2;
    TextView VlblMobileNo2;
    TextView txtMobileNo2;
    CheckBox chkMobileNo;
    LinearLayout secDOB;
    TextView VlblDOB;
    TextView dtpDOB;
    ImageButton btnDOB;
    LinearLayout secAge;
    TextView VlblAge;
    TextView txtAge;
    LinearLayout secDOBSource;
    TextView VlblDOBSource;
    RadioGroup rdogrpDOBSource;

    RadioButton rdoDOBSource1;
    RadioButton rdoDOBSource2;
    LinearLayout secBPlace;
    TextView VlblBPlace;
    TextView txtBPlace;
    LinearLayout secFNo;
    TextView VlblFNo;
    TextView txtFNo;
    LinearLayout secFather;
    LinearLayout secFather1;

    TextView VlblFather;
    TextView txtFather;
    CheckBox chkFatherDontKnow;
    LinearLayout secMNo;
    TextView VlblMNo;
    TextView txtMNo;
    LinearLayout secMother;
    LinearLayout secMother1;
    TextView VlblMother;
    TextView txtMother;
    CheckBox chkMotherDontKnow;
    LinearLayout secSex;
    TextView VlblSex;
    RadioGroup rdogrpSex;
    RadioButton rdoSex1;
    RadioButton rdoSex2;
    RadioButton rdoSex3;
    LinearLayout secMS;
    TextView VlblMS;
    TextView txtMS;

    LinearLayout secSPNo;
    TextView VlblSPNo;
    TextView txtSPNo4;

    LinearLayout secSPNo1;
    TextView VlblSPNo1;
    TextView txtSPNo1;

    LinearLayout secSPNo2;
    TextView VlblSPNo2;
    TextView txtSPNo2;

    LinearLayout secSPNo3;
    TextView VlblSPNo3;
    TextView txtSPNo3;

    LinearLayout secSP10;
    LinearLayout secSPNo11;
    LinearLayout secSPNo21;
    LinearLayout secSPNo31;

    LinearLayout secELCONo;
    TextView VlblELCONo;
    EditText txtELCONo;
    TextView VlblELCODontKnow;
    CheckBox chkELCODontKnow;
    LinearLayout secEDU;
    TextView VlblEDU;
    TextView txtEDU;
    LinearLayout secRel;
    TextView VlblRel;
    TextView txtRel;
    LinearLayout secNationality;
    TextView VlblNationality;
    CheckBox chkNationality;
    LinearLayout secOCP;
    TextView VlblOCP;
    TextView txtOCP;

    RadioGroup rdogrpNameEng;
    RadioButton rdoNameEng1;
    RadioButton rdoNameEng2;

    RadioGroup rdogrpNameBang;
    RadioButton rdoNameBang1;
    RadioButton rdoNameBang2;

    RadioGroup rdogrpRth;
    RadioButton rdoRth1;
    RadioButton rdoRth2;

    RadioGroup rdogrpNID;
    RadioButton rdoNID1;
    RadioButton rdoNID2;

    RadioGroup rdogrpNIDStatus;
    RadioButton rdoNIDStatus1;
    RadioButton rdoNIDStatus2;

    RadioGroup rdogrpBRID;
    RadioButton rdoBRID1;
    RadioButton rdoBRID2;

    RadioGroup rdogrpBRIDStatus;
    RadioButton rdoBRIDStatus1;
    RadioButton rdoBRIDStatus2;

    RadioGroup rdogrpMobileNo;
    RadioButton rdoMobileNo1;
    RadioButton rdoMobileNo2;

    RadioGroup rdogrpMobileNo2;
    RadioButton rdoMobileNo21;
    RadioButton rdoMobileNo22;

    RadioGroup rdogrpDOBStatus;
    RadioButton rdoDOBStatus1;
    RadioButton rdoDOBStatus2;

    RadioGroup rdogrpDOB;
    RadioButton rdoDOB1;
    RadioButton rdoDOB2;

    RadioGroup rdogrpAge;
    RadioButton rdoAge1;
    RadioButton rdoAge2;

    RadioGroup rdogrpBPlace;
    RadioButton rdoBPlace1;
    RadioButton rdoBPlace2;

    RadioGroup rdogrpFNo;
    RadioButton rdoFNo1;
    RadioButton rdoFNo2;

    RadioGroup rdogrpFather;
    RadioButton rdoFather1;
    RadioButton rdoFather2;

    RadioGroup rdogrpMNo;
    RadioButton rdoMNo1;
    RadioButton rdoMNo2;

    RadioGroup rdogrpMother;
    RadioButton rdoMother1;
    RadioButton rdoMother2;

    RadioGroup rdogrpGender;
    RadioButton rdoGender1;
    RadioButton rdoGender2;

    RadioGroup rdogrpMS;
    RadioButton rdoMS1;
    RadioButton rdoMS2;

    RadioGroup rdogrpSPNo1;
    RadioButton rdoSPNo11;
    RadioButton rdoSPNo12;

    RadioGroup rdogrpSPNo2;
    RadioButton rdoSPNo13;
    RadioButton rdoSPNo14;

    RadioGroup rdogrpSPNo3;
    RadioButton rdoSPNo15;
    RadioButton rdoSPNo16;

    RadioGroup rdogrpSPNo4;
    RadioButton rdoSPNo17;
    RadioButton rdoSPNo18;

    RadioGroup rdogrpEDU;
    RadioButton rdoEDU1;
    RadioButton rdoEDU2;

    RadioGroup rdogrpRel;
    RadioButton rdoRel1;
    RadioButton rdoRel2;

    RadioGroup rdogrpNationality;
    RadioButton rdoNationality1;
    RadioButton rdoNationality2;

    RadioGroup rdogrpOCP;
    RadioButton rdoOCP1;
    RadioButton rdoOCP2;

    LinearLayout secNameEng1;
    LinearLayout secRth1;
    LinearLayout secNID1;
    LinearLayout secBRID1;

    String StartTime;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.memberformfpi);
            C = new Connection(this);
            g = Global.getInstance();


            FindLocation();

            StartTime = g.CurrentTime24();

            TableName = "MemberFPI";
            txtHealthID = (EditText) findViewById(R.id.txtHealthID);
            txtHealthID.setEnabled(false);

            chkIDHave = (CheckBox) findViewById(R.id.chkIDHave);
            chkIDHave.setChecked(false);

            seclblH = (LinearLayout) findViewById(R.id.seclblH);
            VlblSNo = (TextView) findViewById(R.id.VlblSNo);
            txtSNo = (TextView) findViewById(R.id.txtSNo);

            /*if(g.getSerialNo().toString().length()==0)
                txtSNo.setText( SerialNumber() );
            else
                txtSNo.setText(g.getSerialNo().toString());*/

            txtSNo.setText(g.getSerialNo().toString());

            secNameEng = (LinearLayout) findViewById(R.id.secNameEng);
            VlblNameEng = (TextView) findViewById(R.id.VlblNameEng);
            txtNameEng = (TextView) findViewById(R.id.txtNameEng);
            secNameBang = (LinearLayout) findViewById(R.id.secNameBang);
            VlblNameBang = (TextView) findViewById(R.id.VlblNameBang);
            txtNameBang = (TextView) findViewById(R.id.txtNameBang);

            txtRth = (TextView) findViewById(R.id.txtRth);

            secHaveNID = (LinearLayout) findViewById(R.id.secHaveNID);
            VlblHaveNID = (TextView) findViewById(R.id.VlblHaveNID);
            rdogrpHaveNID = (RadioGroup) findViewById(R.id.rdogrpHaveNID);
            rdoHaveNID1 = (RadioButton) findViewById(R.id.rdoHaveNID1);
            rdoHaveNID2 = (RadioButton) findViewById(R.id.rdoHaveNID2);

            secNID = (LinearLayout) findViewById(R.id.secNID);
            VlblNID = (TextView) findViewById(R.id.VlblNID);
            txtNID = (TextView) findViewById(R.id.txtNID);
            chkNIDDontKnow = (CheckBox) findViewById(R.id.chkNIDDontKnow);
            chkNIDDontKnow.setEnabled(false);
            secNIDStatus = (LinearLayout) findViewById(R.id.secNIDStatus);
            secNIDStatus1 = (LinearLayout) findViewById(R.id.secNIDStatus1);
            VlblNIDStatus = (TextView) findViewById(R.id.VlblNIDStatus);
            txtNIDStatus = (TextView) findViewById(R.id.txtNIDStatus);
            secHaveBR = (LinearLayout) findViewById(R.id.secHaveBR);
            VlblHaveBR = (TextView) findViewById(R.id.VlblHaveBR);
            rdogrpHaveBR = (RadioGroup) findViewById(R.id.rdogrpHaveBR);
            rdoHaveBR1 = (RadioButton) findViewById(R.id.rdoHaveBR1);
            rdoHaveBR2 = (RadioButton) findViewById(R.id.rdoHaveBR2);

            secBRID = (LinearLayout) findViewById(R.id.secBRID);
            VlblBRID = (TextView) findViewById(R.id.VlblBRID);
            txtBRID = (TextView) findViewById(R.id.txtBRID);
            chkBRIDDontKnow = (CheckBox) findViewById(R.id.chkBRIDDontKnow);
            chkBRIDDontKnow.setEnabled(false);
            secBRIDStatus = (LinearLayout) findViewById(R.id.secBRIDStatus);
            secBRIDStatus1 = (LinearLayout) findViewById(R.id.secBRIDStatus1);
            VlblBRIDStatus = (TextView) findViewById(R.id.VlblBRIDStatus);
            txtBRIDStatus = (TextView) findViewById(R.id.txtBRIDStatus);


            secMobileNo1 = (LinearLayout) findViewById(R.id.secMobileNo1);
            VlblMobileNo1 = (TextView) findViewById(R.id.VlblMobileNo1);
            txtMobileNo1 = (TextView) findViewById(R.id.txtMobileNo1);
            secMobileNo2 = (LinearLayout) findViewById(R.id.secMobileNo2);
            VlblMobileNo2 = (TextView) findViewById(R.id.VlblMobileNo2);
            txtMobileNo2 = (TextView) findViewById(R.id.txtMobileNo2);
            chkMobileNo = (CheckBox) findViewById(R.id.chkMobileNo);
            chkMobileNo.setEnabled(false);
            /*txtMobileNo1.addTextChangedListener(new TextWatcher() {
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
            });*/


            secDOB = (LinearLayout) findViewById(R.id.secDOB);
            VlblDOB = (TextView) findViewById(R.id.VlblDOB);
            dtpDOB = (TextView) findViewById(R.id.dtpDOB);
            secAge = (LinearLayout) findViewById(R.id.secAge);
            VlblAge = (TextView) findViewById(R.id.VlblAge);
            txtAge = (TextView) findViewById(R.id.txtAge);
            secDOBSource = (LinearLayout) findViewById(R.id.secDOBSource);
            VlblDOBSource = (TextView) findViewById(R.id.VlblDOBSource);
            rdogrpDOBSource = (RadioGroup) findViewById(R.id.rdogrpDOBSource);
            rdogrpDOBSource.setEnabled(false);
            rdoDOBSource1 = (RadioButton) findViewById(R.id.rdoDOBSource1);
            rdoDOBSource1.setEnabled(false);
            rdoDOBSource2 = (RadioButton) findViewById(R.id.rdoDOBSource2);
            rdoDOBSource2.setEnabled(false);
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
            txtBPlace = (TextView) findViewById(R.id.txtBPlace);

            secFNo = (LinearLayout) findViewById(R.id.secFNo);
            VlblFNo = (TextView) findViewById(R.id.VlblFNo);
            txtFNo = (TextView) findViewById(R.id.txtFNo);

            secFather = (LinearLayout) findViewById(R.id.secFather);
            secFather1 = (LinearLayout) findViewById(R.id.secFather1);
            VlblFather = (TextView) findViewById(R.id.VlblFather);
            txtFather = (TextView) findViewById(R.id.txtFather);
            chkFatherDontKnow = (CheckBox) findViewById(R.id.chkFatherDontKnow);
            chkFatherDontKnow.setEnabled(false);
            secMNo = (LinearLayout) findViewById(R.id.secMNo);
            VlblMNo = (TextView) findViewById(R.id.VlblMNo);
            txtMNo = (TextView) findViewById(R.id.txtMNo);

            secMother = (LinearLayout) findViewById(R.id.secMother);
            secMother1 = (LinearLayout) findViewById(R.id.secMother1);
            VlblMother = (TextView) findViewById(R.id.VlblMother);
            txtMother = (TextView) findViewById(R.id.txtMother);
            chkMotherDontKnow = (CheckBox) findViewById(R.id.chkMotherDontKnow);
            chkMotherDontKnow.setEnabled(false);
            secSex = (LinearLayout) findViewById(R.id.secSex);
            VlblSex = (TextView) findViewById(R.id.VlblSex);
            rdogrpSex = (RadioGroup) findViewById(R.id.rdogrpSex);
            rdoSex1 = (RadioButton) findViewById(R.id.rdoSex1);
            rdoSex1.setEnabled(false);
            rdoSex2 = (RadioButton) findViewById(R.id.rdoSex2);
            rdoSex2.setEnabled(false);
            rdoSex3 = (RadioButton) findViewById(R.id.rdoSex3);
            rdoSex3.setEnabled(false);
            secMS = (LinearLayout) findViewById(R.id.secMS);
            VlblMS = (TextView) findViewById(R.id.VlblMS);
            txtMS = (TextView) findViewById(R.id.txtMS);

            secSPNo = (LinearLayout) findViewById(R.id.secSPNo);
            VlblSPNo = (TextView) findViewById(R.id.VlblSPNo);
            txtSPNo1 = (TextView) findViewById(R.id.txtSPNo1);

            secSPNo1 = (LinearLayout) findViewById(R.id.secSPNo1);
            VlblSPNo1 = (TextView) findViewById(R.id.VlblSPNo1);
            txtSPNo1 = (TextView) findViewById(R.id.txtSPNo1);

            secSPNo2 = (LinearLayout) findViewById(R.id.secSPNo2);
            VlblSPNo2 = (TextView) findViewById(R.id.VlblSPNo2);
            txtSPNo2 = (TextView) findViewById(R.id.txtSPNo2);

            secSPNo3 = (LinearLayout) findViewById(R.id.secSPNo3);
            VlblSPNo3 = (TextView) findViewById(R.id.VlblSPNo3);
            txtSPNo3 = (TextView) findViewById(R.id.txtSPNo3);
            txtSPNo4 = (TextView) findViewById(R.id.txtSPNo4);

            secSP10 = (LinearLayout) findViewById(R.id.secSP10);
            secSPNo11 = (LinearLayout) findViewById(R.id.secSPNo11);
            secSPNo21 = (LinearLayout) findViewById(R.id.secSPNo21);
            secSPNo31 = (LinearLayout) findViewById(R.id.secSPNo31);

            secELCONo = (LinearLayout) findViewById(R.id.secELCONo);
            VlblELCONo = (TextView) findViewById(R.id.VlblELCONo);
            txtELCONo = (EditText) findViewById(R.id.txtELCONo);
            VlblELCODontKnow = (TextView) findViewById(R.id.VlblELCODontKnow);
            chkELCODontKnow = (CheckBox) findViewById(R.id.chkELCODontKnow);

            secEDU = (LinearLayout) findViewById(R.id.secEDU);
            VlblEDU = (TextView) findViewById(R.id.VlblEDU);
            txtEDU = (TextView) findViewById(R.id.txtEDU);


            secRel = (LinearLayout) findViewById(R.id.secRel);
            VlblRel = (TextView) findViewById(R.id.VlblRel);
            txtRel = (TextView) findViewById(R.id.txtRel);

            secNationality = (LinearLayout) findViewById(R.id.secNationality);
            VlblNationality = (TextView) findViewById(R.id.VlblNationality);
            chkNationality = (CheckBox) findViewById(R.id.chkNationality);
            chkNationality.setChecked(true);
            chkNationality.setEnabled(false);
            secOCP = (LinearLayout) findViewById(R.id.secOCP);
            VlblOCP = (TextView) findViewById(R.id.VlblOCP);
            txtOCP = (TextView) findViewById(R.id.txtOCP);


            //-----------------------------------------------------------
            secNID.setVisibility(View.VISIBLE);
            txtNID.setText("");
            secNIDStatus.setVisibility(View.VISIBLE);
            txtNIDStatus.setText("");

            secBRID.setVisibility(View.VISIBLE);
            txtBRID.setText("");
            secBRIDStatus.setVisibility(View.VISIBLE);
            txtBRIDStatus.setText("");
            secAge.setVisibility(View.GONE);

            secFather.setVisibility(View.GONE);
            secFather1.setVisibility(View.GONE);
            txtFather.setText("");
            secMother.setVisibility(View.GONE);
            secMother1.setVisibility(View.GONE);
            txtMother.setText("");
            //secELCONo.setVisibility( View.GONE );
            //txtELCONo.setText("");
            //chkELCODontKnow.setChecked( false );
            secSPNo.setVisibility(View.GONE);
            secSPNo1.setVisibility(View.GONE);
            secSPNo2.setVisibility(View.GONE);
            secSPNo3.setVisibility(View.GONE);
            //secSP10.setVisibility( View.GONE );
            secSPNo11.setVisibility(View.GONE);
            secSPNo21.setVisibility(View.GONE);
            secSPNo31.setVisibility(View.GONE);
            //---------------------FPI--------------------------------------
            rdogrpNameEng = (RadioGroup) findViewById(R.id.rdogrpNameEng);
            rdoNameEng1 = (RadioButton) findViewById(R.id.rdoNameEng1);
            rdoNameEng2 = (RadioButton) findViewById(R.id.rdoNameEng2);

            secNameEng1 = (LinearLayout) findViewById(R.id.secNameEng1);
            secRth1 = (LinearLayout) findViewById(R.id.secRth1);
            secNID1 = (LinearLayout) findViewById(R.id.secNID1);
            secBRID1 = (LinearLayout) findViewById(R.id.secBRID1);


            rdogrpRth = (RadioGroup) findViewById(R.id.rdogrpRth);
            rdoRth1 = (RadioButton) findViewById(R.id.rdoRth1);
            rdoRth2 = (RadioButton) findViewById(R.id.rdoRth2);

            rdogrpNID = (RadioGroup) findViewById(R.id.rdogrpNID);
            rdoNID1 = (RadioButton) findViewById(R.id.rdoNID1);
            rdoNID2 = (RadioButton) findViewById(R.id.rdoNID2);

            rdogrpNIDStatus = (RadioGroup) findViewById(R.id.rdogrpNIDStatus);
            rdoNIDStatus1 = (RadioButton) findViewById(R.id.rdoNIDStatus1);
            rdoNIDStatus2 = (RadioButton) findViewById(R.id.rdoNIDStatus2);

            rdogrpBRID = (RadioGroup) findViewById(R.id.rdogrpBRID);
            rdoBRID1 = (RadioButton) findViewById(R.id.rdoBRID1);
            rdoBRID2 = (RadioButton) findViewById(R.id.rdoBRID2);

            rdogrpBRIDStatus = (RadioGroup) findViewById(R.id.rdogrpBRIDStatus);
            rdoBRIDStatus1 = (RadioButton) findViewById(R.id.rdoBRIDStatus1);
            rdoBRIDStatus2 = (RadioButton) findViewById(R.id.rdoBRIDStatus2);

            rdogrpMobileNo = (RadioGroup) findViewById(R.id.rdogrpMobileNo);
            rdoMobileNo1 = (RadioButton) findViewById(R.id.rdoMobileNo1);
            rdoMobileNo2 = (RadioButton) findViewById(R.id.rdoMobileNo2);

            rdogrpMobileNo2 = (RadioGroup) findViewById(R.id.rdogrpMobileNo2);
            rdoMobileNo21 = (RadioButton) findViewById(R.id.rdoMobileNo21);
            rdoMobileNo22 = (RadioButton) findViewById(R.id.rdoMobileNo22);

            rdogrpDOBStatus = (RadioGroup) findViewById(R.id.rdogrpDOBStatus);
            rdoDOBStatus1 = (RadioButton) findViewById(R.id.rdoDOBStatus1);
            rdoDOBStatus2 = (RadioButton) findViewById(R.id.rdoDOBStatus2);

            rdogrpDOB = (RadioGroup) findViewById(R.id.rdogrpDOB);
            rdoDOB1 = (RadioButton) findViewById(R.id.rdoDOB1);
            rdoDOB2 = (RadioButton) findViewById(R.id.rdoDOB2);

            rdogrpAge = (RadioGroup) findViewById(R.id.rdogrpAge);
            rdoAge1 = (RadioButton) findViewById(R.id.rdoAge1);
            rdoAge2 = (RadioButton) findViewById(R.id.rdoAge2);

            rdogrpBPlace = (RadioGroup) findViewById(R.id.rdogrpBPlace);
            rdoBPlace1 = (RadioButton) findViewById(R.id.rdoBPlace1);
            rdoBPlace2 = (RadioButton) findViewById(R.id.rdoBPlace2);

            rdogrpFNo = (RadioGroup) findViewById(R.id.rdogrpFNo);
            rdoFNo1 = (RadioButton) findViewById(R.id.rdoFNo1);
            rdoFNo2 = (RadioButton) findViewById(R.id.rdoFNo2);

            rdogrpFather = (RadioGroup) findViewById(R.id.rdogrpFather);
            rdoFather1 = (RadioButton) findViewById(R.id.rdoFather1);
            rdoFather2 = (RadioButton) findViewById(R.id.rdoFather2);

            rdogrpMNo = (RadioGroup) findViewById(R.id.rdogrpMNo);
            rdoMNo1 = (RadioButton) findViewById(R.id.rdoMNo1);
            rdoMNo2 = (RadioButton) findViewById(R.id.rdoMNo2);

            rdogrpMother = (RadioGroup) findViewById(R.id.rdogrpMother);
            rdoMother1 = (RadioButton) findViewById(R.id.rdoMother1);
            rdoMother2 = (RadioButton) findViewById(R.id.rdoMother2);

            rdogrpGender = (RadioGroup) findViewById(R.id.rdogrpGender);
            rdoGender1 = (RadioButton) findViewById(R.id.rdoGender1);
            rdoGender2 = (RadioButton) findViewById(R.id.rdoGender2);

            rdogrpMS = (RadioGroup) findViewById(R.id.rdogrpMS);
            rdoMS1 = (RadioButton) findViewById(R.id.rdoMS1);
            rdoMS2 = (RadioButton) findViewById(R.id.rdoMS2);

            rdogrpSPNo1 = (RadioGroup) findViewById(R.id.rdogrpSPNo1);
            rdoSPNo11 = (RadioButton) findViewById(R.id.rdoSPNo11);
            rdoSPNo12 = (RadioButton) findViewById(R.id.rdoSPNo12);

            rdogrpSPNo2 = (RadioGroup) findViewById(R.id.rdogrpSPNo2);
            rdoSPNo13 = (RadioButton) findViewById(R.id.rdoSPNo13);
            rdoSPNo14 = (RadioButton) findViewById(R.id.rdoSPNo14);

            rdogrpSPNo3 = (RadioGroup) findViewById(R.id.rdogrpSPNo3);
            rdoSPNo15 = (RadioButton) findViewById(R.id.rdoSPNo15);
            rdoSPNo16 = (RadioButton) findViewById(R.id.rdoSPNo16);

            rdogrpSPNo4 = (RadioGroup) findViewById(R.id.rdogrpSPNo4);
            rdoSPNo17 = (RadioButton) findViewById(R.id.rdoSPNo17);
            rdoSPNo18 = (RadioButton) findViewById(R.id.rdoSPNo18);

            rdogrpEDU = (RadioGroup) findViewById(R.id.rdogrpEDU);
            rdoEDU1 = (RadioButton) findViewById(R.id.rdoEDU1);
            rdoEDU2 = (RadioButton) findViewById(R.id.rdoEDU2);

            rdogrpRel = (RadioGroup) findViewById(R.id.rdogrpRel);
            rdoRel1 = (RadioButton) findViewById(R.id.rdoRel1);
            rdoRel2 = (RadioButton) findViewById(R.id.rdoRel2);

            rdogrpNationality = (RadioGroup) findViewById(R.id.rdogrpNationality);
            rdoNationality1 = (RadioButton) findViewById(R.id.rdoNationality1);
            rdoNationality2 = (RadioButton) findViewById(R.id.rdoNationality2);

            rdogrpOCP = (RadioGroup) findViewById(R.id.rdogrpOCP);
            rdoOCP1 = (RadioButton) findViewById(R.id.rdoOCP1);
            rdoOCP2 = (RadioButton) findViewById(R.id.rdoOCP2);


            //DataSearch("93","9","71","78","1","10002","1");
            Button cmdSave = (Button) findViewById(R.id.cmdSave);
            cmdSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    DataSave();
                }
            });
            DataSearch(g.getDistrict(), g.getUpazila(), g.getUnion(), g.getMouza(), g.getVillage(), g.getHouseholdNo(), txtSNo.getText().toString());
            FPIDataSearch(g.getDistrict(), g.getUpazila(), g.getUnion(), g.getMouza(), g.getVillage(), g.getHouseholdNo(), txtSNo.getText().toString());
        } catch (Exception e) {
            Connection.MessageBox(MemberFormFPI.this, e.getMessage());
            return;
        }

    }

    private void DataSave() {
        try {
            String SQL = "";
            String AG = "";

            if (!rdoNameEng1.isChecked() & !rdoNameEng2.isChecked() & secNameEng1.isShown()) {
                Connection.MessageBox(MemberFormFPI.this, "সদস্যের নাম যাচাই করুন ।");
                rdoNameEng1.requestFocus();
                return;
            } else if (!rdoRth1.isChecked() & !rdoRth2.isChecked() & secRth1.isShown()) {
                Connection.MessageBox(MemberFormFPI.this, "সদস্যের খানা প্রধানের সাথে সম্পর্ক যাচাই করুন ।");
                rdoRth1.requestFocus();
                return;
            } else if (!rdoNID1.isChecked() & !rdoNID2.isChecked() & secNID1.isShown()) {
                Connection.MessageBox(MemberFormFPI.this, "সদস্যের জাতীয় পরিচয় পত্র নম্বর যাচাই করুন ।");
                rdoNID1.requestFocus();
                return;
            } else if (!rdoNIDStatus1.isChecked() & !rdoNIDStatus2.isChecked() & secNIDStatus1.isShown()) {
                Connection.MessageBox(MemberFormFPI.this, "সদস্যের জাতীয় পরিচয় পত্র নম্বর না থাকলে কেন নাই যাচাই করুন ।");
                rdoNIDStatus1.requestFocus();
                return;
            } else if (!rdoBRID1.isChecked() & !rdoBRID2.isChecked() & secBRID1.isShown()) {
                Connection.MessageBox(MemberFormFPI.this, "সদস্যের জন্ম নিবন্ধন নম্বর যাচাই করুন ।");
                rdoBRID1.requestFocus();
                return;
            } else if (!rdoBRIDStatus1.isChecked() & !rdoBRIDStatus2.isChecked() & secBRIDStatus1.isShown()) {
                Connection.MessageBox(MemberFormFPI.this, "সদস্যের জন্ম নিবন্ধন নম্বর না থাকলে কেন নাই যাচাই করুন ।");
                rdoBRIDStatus1.requestFocus();
                return;
            }
            if (!C.Existence("Select Dist,Upz,UN,Mouza,Vill,HHNo,SNo from " + TableName + "  Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and SNo='" + txtSNo.getText().toString() + "'")) {
                SQL = "Insert into " + TableName + "(Dist,Upz,UN,Mouza,Vill,ProvType,ProvCode,HHNo,SNo,HealthID,Lat,Lon,StartTime,EnType,EnDate,EndTime,ExType,ExDate,UserId,EnDt,Upload)Values('" + g.getDistrict() + "','" + g.getUpazila() + "','" + g.getUnion() + "','" + g.getMouza() + "','" + g.getVillage() + "','" + g.getProvType() + "','" + g.getProvCode() + "','" + g.getHouseholdNo() + "','" + txtSNo.getText() + "','" + txtHealthID.getText().toString() + "','" + Double.toString(currentLatitude) + "','" + Double.toString(currentLongitude) + "','" + StartTime + "','','" + g.DateNowYMD() + "','" + g.CurrentTime24() + "','','','" + g.getUserID() + "','" + Global.DateTimeNowYMDHMS() + "','2')";
                C.Save(SQL);
            }

            SQL = "Update " + TableName + " Set Upload='2',";
            SQL += "NameEngStatus = '" + (rdoNameEng1.isChecked() ? "1" : (rdoNameEng2.isChecked() ? "2" : "")) + "',";
            SQL += "RTHStatus ='" + (rdoRth1.isChecked() ? "1" : (rdoRth2.isChecked() ? "2" : "")) + "',";

            SQL += "HaveNIDStatus = '" + (rdoNID1.isChecked() ? "1" : (rdoNID2.isChecked() ? "2" : "")) + "',";
            SQL += "NIDStatus = '" + (rdoNIDStatus1.isChecked() ? "1" : (rdoNIDStatus2.isChecked() ? "2" : "")) + "',";
            SQL += "HaveBRStatus = '" + (rdoBRID1.isChecked() ? "1" : (rdoBRID2.isChecked() ? "2" : "")) + "',";
            SQL += "BRIDStatus = '" + (rdoBRIDStatus1.isChecked() ? "1" : (rdoBRIDStatus2.isChecked() ? "2" : "")) + "',";
            SQL += "MobileNo1Status = '" + (rdoMobileNo1.isChecked() ? "1" : (rdoMobileNo2.isChecked() ? "2" : "")) + "',";
            SQL += "MobileNo2Status = '" + (rdoMobileNo21.isChecked() ? "1" : (rdoMobileNo22.isChecked() ? "2" : "")) + "',";
            if (rdoDOBSource1.isChecked()) {
                SQL += "DOBStatus = '" + (rdoDOB1.isChecked() ? "1" : (rdoDOB2.isChecked() ? "2" : "")) + "',";
                SQL += "AgeStatus = '" + (rdoAge1.isChecked() ? "1" : (rdoAge2.isChecked() ? "2" : "")) + "',";
            } else {
                SQL += "DOBStatus = '" + (rdoDOB1.isChecked() ? "1" : (rdoDOB2.isChecked() ? "2" : "")) + "',";
                SQL += "AgeStatus = '" + (rdoAge1.isChecked() ? "1" : (rdoAge2.isChecked() ? "2" : "")) + "',";
            }
            SQL += "DOBSourceStatus = '" + (rdoDOBStatus1.isChecked() ? "1" : (rdoDOBStatus2.isChecked() ? "2" : "")) + "',";
            SQL += "BPlaceStatus = '" + (rdoBPlace1.isChecked() ? "1" : (rdoBPlace2.isChecked() ? "2" : "")) + "',";
            SQL += "FNoStatus = '" + (rdoFNo1.isChecked() ? "1" : (rdoFNo2.isChecked() ? "2" : "")) + "',";
            SQL += "FatherStatus = '" + (rdoFather1.isChecked() ? "1" : (rdoFather2.isChecked() ? "2" : "")) + "',";
            SQL += "MNoStatus = '" + (rdoMNo1.isChecked() ? "1" : (rdoMNo2.isChecked() ? "2" : "")) + "',";
            SQL += "MotherStatus = '" + (rdoMother1.isChecked() ? "1" : (rdoMother2.isChecked() ? "2" : "")) + "',";
            SQL += "SexStatus = '" + (rdoGender1.isChecked() ? "1" : (rdoGender2.isChecked() ? "2" : "")) + "',";
            SQL += "MSStatus = '" + (rdoMS1.isChecked() ? "1" : (rdoMS2.isChecked() ? "2" : "")) + "',";
            SQL += "SPNo1Status = '" + (rdoSPNo11.isChecked() ? "1" : (rdoSPNo12.isChecked() ? "2" : "")) + "',";
            SQL += "SPNo2Status = '" + (rdoSPNo13.isChecked() ? "1" : (rdoSPNo14.isChecked() ? "2" : "")) + "',";
            SQL += "SPNo3Status = '" + (rdoSPNo15.isChecked() ? "1" : (rdoSPNo16.isChecked() ? "2" : "")) + "',";
            SQL += "SPNo4Status = '" + (rdoSPNo17.isChecked() ? "1" : (rdoSPNo18.isChecked() ? "2" : "")) + "',";
            SQL += "EDUStatus = '" + (rdoEDU1.isChecked() ? "1" : (rdoEDU2.isChecked() ? "2" : "")) + "',";
            SQL += "RelStatus = '" + (rdoRel1.isChecked() ? "1" : (rdoRel2.isChecked() ? "2" : "")) + "',";
            SQL += "NationalityStatus = '" + (rdoNationality1.isChecked() ? "1" : (rdoNationality2.isChecked() ? "2" : "")) + "',";
            SQL += "OCPStatus = '" + (rdoOCP1.isChecked() ? "1" : (rdoOCP2.isChecked() ? "2" : "")) + "'";
            SQL += " Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and SNo='" + txtSNo.getText().toString() + "'";
            C.Save(SQL);
            Connection.MessageBox(MemberFormFPI.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
            finish();
            Intent f2 = new Intent(getApplicationContext(), MemberListFPI.class);
            startActivity(f2);
        } catch (Exception e) {
            Connection.MessageBox(MemberFormFPI.this, e.getMessage());
            return;
        }
    }

/*    private int SpNoNoPosition(String spouseNo, int spSerial)
    {
        int pos = 0;
        if(spouseNo.length()!=0 & !spouseNo.equalsIgnoreCase("null"))
        {
            spouseNo = Integer.valueOf(spouseNo).toString();
            String[] f;

            if(spSerial==1) {
                for (int i = 0; i < spnSPNo.getCount(); i++) {
                    f = spnSPNo.getItemAtPosition(i).toString().split("-");
                    if (spnSPNo.getItemAtPosition(i).toString().length() != 0) {
                        if (f[0].toString().equalsIgnoreCase(spouseNo)) {
                            pos = i;
                            i = spnSPNo.getCount();
                        }
                    }
                }
            }
            else if(spSerial==2) {
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
            if(spSerial==3) {
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
            if(spSerial==4) {
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
        return  pos;
    }


    private int FSerialNoPosition(String fatherNo)
    {
        int pos = 0;
        if(fatherNo.length()!=0)
        {
            fatherNo = Integer.valueOf(fatherNo).toString();
            String[] f;
            for(int i=0;i<spnFNo.getCount();i++)
            {
                f = spnFNo.getItemAtPosition(i).toString().split("-");
                if(spnFNo.getItemAtPosition(i).toString().length()!=0)
                {
                    if(f[0].toString().equalsIgnoreCase(fatherNo))
                    {
                        pos = i;
                        i   = spnFNo.getCount();
                    }
                }
            }
        }
        return  pos;
    }

    private int MSerialNoPosition(String motherNo)
    {
        int pos = 0;
        if(motherNo.length()!=0)
        {
            motherNo = Integer.valueOf(motherNo).toString();
            String[] m;
            for(int i=0;i<spnMNo.getCount();i++)
            {
                m = spnMNo.getItemAtPosition(i).toString().split("-");
                if(spnMNo.getItemAtPosition(i).toString().length()!=0)
                {
                    if(m[0].toString().equalsIgnoreCase(motherNo))
                    {
                        pos = i;
                        i   = spnMNo.getCount();
                    }
                }
            }
        }
        return  pos;
    }*/


    private void DataSearch(String Dist, String Upz, String UN, String Mouza, String Vill, String HHNo, String SNo) {
        try {
            RadioButton rb;
            String SQL = "";
            /*SQL = "Select FPI.Dist, FPI.Upz, FPI.UN, FPI.Mouza, FPI.Vill, FPI.HHNo, FPI.SNo as SNo, ifnull(FPI.HealthID,'') as HealthID,\n" +
                    "ifnull(mem.NameEng,'') as NameEng,ifnull(FPI.NameEngStatus,'') as NameEngStatus,ifnull(mem.NameBang,'') as NameBang, \n" +
                    "ifnull(mem.Rth,'') as Rth,ifnull(FPI.RthStatus,'') as RthStatus, ifnull(mem.HaveNID,'') as HaveNID,ifnull(FPI.HaveNIDStatus,'') as HaveNIDStatus,ifnull(mem.NID,'') as NID,ifnull(mem.NIDStatus,'') as NIDStatus,ifnull(FPI.NIDStatus,'') as FPINIDStatus,\n" +
                    "ifnull(mem.HaveBR,'') as HaveBR,ifnull(FPI.HaveBRStatus,'') as HaveBRStatus,ifnull(mem.BRID,'') as BRID, ifnull(mem.BRIDStatus,'') as BRIDStatus,ifnull(FPI.BRIDStatus,'') as FPIBRIDStatus,ifnull(mem.MobileNo1,'') as MobileNo1,ifnull(FPI.MobileNo1Status,'') as MobileNo1Status,\n" +
                    "ifnull(mem.MobileNo2,'') as MobileNo2,ifnull(FPI.MobileNo2Status,'') as MobileNo2Status, ifnull(mem.MobileYN,'')as MobileYN, ifnull(mem.DOB,'') as DOB,ifnull(FPI.DOBStatus,'') as DOBStatus,\n" +
                    "ifnull(cast(((julianday(date('now'))-julianday(mem.DOB))/365)as int),'') as Age, ifnull(FPI.AgeStatus,'') as FPIAge,ifnull(mem.DOBSource,'') as DOBSource,ifnull(FPI.DOBSourceStatus,'') as DOBSourceStatus, \n" +
                    "ifnull(mem.BPlace,'') as BPlace,ifnull(FPI.BPlaceStatus,'') as BPlaceStatus,ifnull(mem.FNo,'') as FNo,ifnull(FPI.FNoStatus,'') as FNoStatus,ifnull(mem.Father,'') as Father,ifnull(FPI.FatherStatus,'') as FatherStatus,ifnull(mem.FDontKnow,'')as FDontKnow, \n" +
                    "ifnull(mem.MNo,'') as MNo,ifnull(FPI.MNoStatus,'') as MNoStatus,ifnull(mem.Mother,'') as Mother,ifnull(FPI.MotherStatus,'') as MotherStatus,ifnull(mem.MDontKnow,'')as MDontKnow,ifnull(mem.Sex,'') as Sex,ifnull(FPI.SexStatus,'') as SexStatus, \n" +
                    "ifnull(mem.MS,'') as MS,ifnull(FPI.MSStatus,'') as MSStatus,ifnull(mem.SPNO1,'') as SPNO1,ifnull(FPI.SPNO1Status,'') as SPNO1Status,ifnull(mem.SPNO2,'') as SPNO2,ifnull(FPI.SPNO2Status,'') as SPNO2Status,ifnull(mem.SPNO3,'') as SPNO3,ifnull(FPI.SPNO3Status,'') as SPNO3Status,\n" +
                    "ifnull(mem.SPNO4,'') as SPNO4,ifnull(FPI.SPNO4Status,'') as SPNO4Status,ifnull(mem.ELCONo,'') as ELCONo, ifnull(mem.ELCODontKnow,'') as ELCODontKnow, ifnull(mem.EDU,'') as EDU,ifnull(FPI.EDUStatus,'') as EDUStatus,ifnull(mem.Rel,'') as Rel,ifnull(FPI.RelStatus,'') as RelStatus,ifnull(mem.Nationality,'') as Nationality,ifnull(FPI.NationalityStatus,'') as NationalityStatus,ifnull(mem.OCP,'') as OCP,ifnull(FPI.OCPStatus,'') as OCPStatus\n" +
                    "from MemberFPI FPI LEFT JOIN Member mem\n" +
                    "ON mem.healthId = FPI.healthid Where FPI.Dist='"+ Dist +"' and FPI.Upz='"+ Upz +"' and FPI.UN='"+ UN +"' and FPI.Mouza='"+ Mouza +"' and FPI.Vill='"+ Vill +"' and FPI.HHNo='"+ HHNo +"' and FPI.SNo='"+ SNo +"'";*/
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
                //txtNameBang.setText(cur.getString(cur.getColumnIndex("NameBang")).replace("null", ""));
                String RelWithHead = (cur.getString(cur.getColumnIndex("Rth")).toString());
                if (RelWithHead.equalsIgnoreCase("00")) {
                    txtRth.setText("00-খানা প্রধানের সাথে সম্পর্ক নাই");
                } else if (RelWithHead.equalsIgnoreCase("01")) {
                    txtRth.setText("01-নিজেই খানা প্রধান");
                } else if (RelWithHead.equalsIgnoreCase("02")) {
                    txtRth.setText("02-খানা প্রধানের স্বামী/স্ত্রী");
                } else if (RelWithHead.equalsIgnoreCase("03")) {
                    txtRth.setText("03-খানা প্রধানের ছেলে /মেয়ে");
                } else if (RelWithHead.equalsIgnoreCase("04")) {
                    txtRth.setText("04-খানা প্রধানের বাবা /মা");
                } else if (RelWithHead.equalsIgnoreCase("05")) {
                    txtRth.setText("05-খানা প্রধানের ভাই /বোন");
                } else if (RelWithHead.equalsIgnoreCase("06")) {
                    txtRth.setText("06-খানা প্রধানের চাচা/ফুফু/মামা/খালা");
                } else if (RelWithHead.equalsIgnoreCase("07")) {
                    txtRth.setText("07-খানা প্রধানের দাদা/দাদি/নানা/নানি");
                } else if (RelWithHead.equalsIgnoreCase("08")) {
                    txtRth.setText("08-খানা প্রধানের নাতি/নাতনী");
                } else if (RelWithHead.equalsIgnoreCase("09")) {
                    txtRth.setText("09-খানা প্রধানের শ্যালক/শ্যালিকা/দেবর/ননদ");
                } else if (RelWithHead.equalsIgnoreCase("10")) {
                    txtRth.setText("10-খানা প্রধানের শ্বশুর/শাশুড়ি");
                } else if (RelWithHead.equalsIgnoreCase("11")) {
                    txtRth.setText("11-খানা প্রধানের সৎ বাবা /মা");
                } else if (RelWithHead.equalsIgnoreCase("12")) {
                    txtRth.setText("12-খানা প্রধানের সৎ ছেলে /মেয়ে");
                } else if (RelWithHead.equalsIgnoreCase("13")) {
                    txtRth.setText("13-খানা প্রধানের পালিত ছেলে /মেয়ে");
                } else if (RelWithHead.equalsIgnoreCase("14")) {
                    txtRth.setText("14-খানা প্রধানের সৎ ভাই/বোন");
                } else if (RelWithHead.equalsIgnoreCase("15")) {
                    txtRth.setText("15-খানা প্রধানের ছেলের বউ/মেয়ের জামাই");
                } else if (RelWithHead.equalsIgnoreCase("16")) {
                    txtRth.setText("16-খানা প্রধানের ভাতিজা/ভাতিজী/ভাগ্নে/ভাগ্নি");
                } else if (RelWithHead.equalsIgnoreCase("17")) {
                    txtRth.setText("17-খানা প্রধানের বোন জামাই/ভাবি");
                } else if (RelWithHead.equalsIgnoreCase("18")) {
                    txtRth.setText("18-চাকর/চাকরানী");
                } else if (RelWithHead.equalsIgnoreCase("77")) {
                    txtRth.setText("77-অন্য সম্পর্ক যা উপরের তালিকায় নাই");
                } else if (RelWithHead.equalsIgnoreCase("99")) {
                    txtRth.setText("99-জানিনা");
                }

                if (cur.getString(cur.getColumnIndex("HaveNID")).equals("1")) {
                    chkNIDDontKnow.setChecked(false);
                    //rdoHaveNID1.setChecked(true);
                    secNID.setVisibility(View.VISIBLE);
                    secNIDStatus.setVisibility(View.GONE);
                    secNIDStatus1.setVisibility(View.GONE);
                } else if (cur.getString(cur.getColumnIndex("HaveNID")).equals("2")) {
                    chkNIDDontKnow.setChecked(true);
                    //rdoHaveNID2.setChecked(true);
                    secNID.setVisibility(View.VISIBLE);
                    secNIDStatus.setVisibility(View.VISIBLE);
                    secNIDStatus1.setVisibility(View.VISIBLE);
                    rdogrpNIDStatus.clearCheck();
                }
                txtNID.setText(cur.getString(cur.getColumnIndex("NID")).replace("null", ""));
                //txtNIDStatus.setText(cur.getString(cur.getColumnIndex("NIDStatus")).replace("null",""));
                String NIDStatus = (cur.getString(cur.getColumnIndex("NIDStatus")).replace("null", "").toString());
                if (NIDStatus.equalsIgnoreCase("1")) {
                    txtNIDStatus.setText("1-কখনো ছিল না");
                } else if (NIDStatus.equalsIgnoreCase("2")) {
                    txtNIDStatus.setText("2-হারিয়ে ফেলেছি");
                } else if (NIDStatus.equalsIgnoreCase("3")) {
                    txtNIDStatus.setText("3-খুঁজে পাচ্ছি না");
                } else if (NIDStatus.equalsIgnoreCase("4")) {
                    txtNIDStatus.setText("4-অন্য জায়গায় আছে");
                } else if (NIDStatus.equalsIgnoreCase("7")) {
                    txtNIDStatus.setText("7-নাগরিক নয়");
                }
                //spnNIDStatus.setSelection(Global.SpinnerItemPosition(spnNIDStatus, 1 ,cur.getString(cur.getColumnIndex("NIDStatus"))));

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
                    secBRIDStatus1.setVisibility(View.GONE);
                } else if (cur.getString(cur.getColumnIndex("HaveBR")).equals("2")) {
                    chkBRIDDontKnow.setChecked(true);
                    //rdoHaveBR2.setChecked(true);
                    secBRID.setVisibility(View.VISIBLE);
                    secBRIDStatus.setVisibility(View.VISIBLE);
                    secBRIDStatus1.setVisibility(View.VISIBLE);
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
                //txtBRIDStatus.setText(cur.getString(cur.getColumnIndex("BRIDStatus")).replace("null", ""));
                String BRIDStatus = (cur.getString(cur.getColumnIndex("BRIDStatus")).replace("null", "").toString());
                if (BRIDStatus.equalsIgnoreCase("1")) {
                    txtBRIDStatus.setText("1-কখনো ছিল না");
                } else if (BRIDStatus.equalsIgnoreCase("2")) {
                    txtBRIDStatus.setText("2-হারিয়ে ফেলেছি");
                } else if (BRIDStatus.equalsIgnoreCase("3")) {
                    txtBRIDStatus.setText("3-খুঁজে পাচ্ছি না");
                } else if (BRIDStatus.equalsIgnoreCase("4")) {
                    txtBRIDStatus.setText("4-অন্য জায়গায় আছে");
                } else if (BRIDStatus.equalsIgnoreCase("7")) {
                    txtBRIDStatus.setText("7-নাগরিক নয়");
                }
                //spnBRIDStatus.setSelection(Global.SpinnerItemPosition(spnBRIDStatus, 1 ,cur.getString(cur.getColumnIndex("BRIDStatus"))));
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
                //txtBPlace.setText(cur.getString(cur.getColumnIndex("BPlace")).replace("null", ""));
                String BPlace = (cur.getString(cur.getColumnIndex("BPlace")).replace("null", "").toString());
                if (BPlace.equalsIgnoreCase("93")) {
                    txtBPlace.setText("93-টাঙ্গাইল");
                }
                String FNo = (cur.getString(cur.getColumnIndex("FNo")).replace("null", "").toString());
                if (FNo.equalsIgnoreCase("55")) {
                    txtFNo.setText("55-মারা গিয়েছে");
                    secFather.setVisibility(View.VISIBLE);
                    secFather1.setVisibility(View.VISIBLE);
                    txtFather.setText(cur.getString(cur.getColumnIndex("Father")).replace("null", ""));
                } else if (FNo.equalsIgnoreCase("77")) {
                    txtFNo.setText("77-এই খানার সদস্য না");
                    secFather.setVisibility(View.VISIBLE);
                    secFather1.setVisibility(View.VISIBLE);
                    txtFather.setText(cur.getString(cur.getColumnIndex("Father")).replace("null", ""));
                } else if (FNo.equalsIgnoreCase("88")) {
                    txtFNo.setText("88-পরে আপডেট হবে");
                } else {
                    secFather.setVisibility(View.GONE);
                    secFather1.setVisibility(View.GONE);
                    txtFNo.setText(C.ReturnSingleValue("select   ifnull(NameEng,'') as NameEng from Member where Dist='" + Dist + "' and Upz='" + Upz + "' and UN='" + UN + "' and Mouza='" + Mouza + "' and Vill='" + Vill + "' and HHNo='" + HHNo + "' and SNo='" + FNo + "'"));
                }

                if (cur.getString(cur.getColumnIndex("FDontKnow")).equals("1")) {
                    chkFatherDontKnow.setChecked(true);
                } else {
                    chkFatherDontKnow.setChecked(false);
                }
                /*if(cur.getString(cur.getColumnIndex("FNo")).endsWith( "77" ))
                {
                    secFather.setVisibility( View.VISIBLE );
                }
                else {
                    secFather.setVisibility( View.GONE );
                }*/
                //txtMNo.setText(cur.getString(cur.getColumnIndex("MNo")).replace("null", ""));
                String MNo = (cur.getString(cur.getColumnIndex("MNo")).replace("null", "").toString());
                if (MNo.equalsIgnoreCase("55")) {
                    txtMNo.setText("55-মারা গিয়েছে");
                    secMother.setVisibility(View.VISIBLE);
                    secMother1.setVisibility(View.VISIBLE);
                    txtMother.setText(cur.getString(cur.getColumnIndex("Mother")).replace("null", ""));
                } else if (MNo.equalsIgnoreCase("77")) {
                    txtMNo.setText("77-এই খানার সদস্য না");
                    secMother.setVisibility(View.VISIBLE);
                    secMother1.setVisibility(View.VISIBLE);
                    txtMother.setText(cur.getString(cur.getColumnIndex("Mother")).replace("null", ""));
                } else if (MNo.equalsIgnoreCase("88")) {
                    txtMNo.setText("88-পরে আপডেট হবে");
                } else {
                    secMother.setVisibility(View.GONE);
                    secMother1.setVisibility(View.GONE);
                    txtMNo.setText(C.ReturnSingleValue("select   ifnull(NameEng,'') as NameEng from Member where Dist='" + Dist + "' and Upz='" + Upz + "' and UN='" + UN + "' and Mouza='" + Mouza + "' and Vill='" + Vill + "' and HHNo='" + HHNo + "' and SNo='" + MNo + "'"));
                }
                //txtMother.setText(cur.getString(cur.getColumnIndex("Mother")).replace("null", ""));
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

                /*if(cur.getString(cur.getColumnIndex("MNo")).endsWith( "77" ))
                {
                    secMother.setVisibility( View.VISIBLE );
                }
                else
                {
                    secMother.setVisibility(View.GONE);
                }*/
                //txtMS.setText(cur.getString(cur.getColumnIndex("MS")).replace("null", ""));
                String MS = (cur.getString(cur.getColumnIndex("MS")).replace("null", "").toString());
                if (MS.equalsIgnoreCase("1")) {
                    txtMS.setText("1-অবিবাহিত");
                    secSP10.setVisibility(View.GONE);
                } else if (MS.equalsIgnoreCase("2")) {
                    txtMS.setText("2-বিবাহিত");
                } else if (MS.equalsIgnoreCase("3")) {
                    txtMS.setText("3-বিধবা/বিপত্নীক");
                } else if (MS.equalsIgnoreCase("4")) {
                    txtMS.setText("4-স্বামী/স্ত্রী পৃথক");
                } else if (MS.equalsIgnoreCase("5")) {
                    txtMS.setText("5-তালাক প্রাপ্ত/ বিবাহ বিচ্ছিন্ন");
                }
                //spnMS.setSelection(Global.SpinnerItemPosition(spnMS, 1, cur.getString(cur.getColumnIndex("MS"))));

               /* if(cur.getString(cur.getColumnIndex("SPNO1")).replace("null","").toString().trim().length()>0)
                {
                    secSPNo.setVisibility(View.VISIBLE);
                    spnSPNo.setSelection(SpNoNoPosition(cur.getString(cur.getColumnIndex("SPNO1")).replace("-", ""), 1));
                }
                if(cur.getString(cur.getColumnIndex("SPNO2")).replace("null", "").toString().trim().length()>0)
                {
                    secSPNo1.setVisibility(View.VISIBLE);
                    spnSPNo1.setSelection(SpNoNoPosition(cur.getString(cur.getColumnIndex("SPNO2")).replace("-", ""), 2));
                }
                else
                {
                    secSPNo1.setVisibility(View.GONE);
                    VlblSPNo1.setVisibility(View.GONE);
                }
                if(cur.getString(cur.getColumnIndex("SPNO3")).replace("null", "").toString().trim().length()>0)
                {
                    secSPNo2.setVisibility(View.VISIBLE);
                    spnSPNo2.setSelection(SpNoNoPosition(cur.getString(cur.getColumnIndex("SPNO3")).replace("-", ""), 3));
                }
                else
                {
                    secSPNo2.setVisibility(View.GONE);
                    VlblSPNo2.setVisibility(View.GONE);
                }
                if(cur.getString(cur.getColumnIndex("SPNO4")).replace("null","").toString().trim().length()>0)
                {
                    secSPNo3.setVisibility(View.VISIBLE);
                    spnSPNo3.setSelection(SpNoNoPosition(cur.getString(cur.getColumnIndex("SPNO4")).replace("-",""), 4));
                }
                else
                {
                    secSPNo3.setVisibility(View.GONE);
                    VlblSPNo3.setVisibility(View.GONE);
                }*/

                //txtSPNo1.setText(cur.getString(cur.getColumnIndex("SPNO1")));
                String SPNo1 = (cur.getString(cur.getColumnIndex("SPNO1")).replace("null", "").toString());
                txtSPNo1.setText(C.ReturnSingleValue("select   ifnull(NameEng,'') as NameEng from Member where Dist='" + Dist + "' and Upz='" + Upz + "' and UN='" + UN + "' and Mouza='" + Mouza + "' and Vill='" + Vill + "' and HHNo='" + HHNo + "' and SNo='" + SPNo1 + "'"));
                //txtSPNo2.setText(cur.getString(cur.getColumnIndex("SPNO2")));
                String SPNo2 = (cur.getString(cur.getColumnIndex("SPNO2")).replace("null", "").toString());
                txtSPNo2.setText(C.ReturnSingleValue("select   ifnull(NameEng,'') as NameEng from Member where Dist='" + Dist + "' and Upz='" + Upz + "' and UN='" + UN + "' and Mouza='" + Mouza + "' and Vill='" + Vill + "' and HHNo='" + HHNo + "' and SNo='" + SPNo2 + "'"));
                //txtSPNo3.setText(cur.getString(cur.getColumnIndex("SPNO3")));
                String SPNo3 = (cur.getString(cur.getColumnIndex("SPNO3")).replace("null", "").toString());
                txtSPNo3.setText(C.ReturnSingleValue("select   ifnull(NameEng,'') as NameEng from Member where Dist='" + Dist + "' and Upz='" + Upz + "' and UN='" + UN + "' and Mouza='" + Mouza + "' and Vill='" + Vill + "' and HHNo='" + HHNo + "' and SNo='" + SPNo3 + "'"));
                //txtSPNo4.setText(cur.getString(cur.getColumnIndex("SPNO4")));
                String SPNo4 = (cur.getString(cur.getColumnIndex("SPNO4")).replace("null", "").toString());
                txtSPNo4.setText(C.ReturnSingleValue("select   ifnull(NameEng,'') as NameEng from Member where Dist='" + Dist + "' and Upz='" + Upz + "' and UN='" + UN + "' and Mouza='" + Mouza + "' and Vill='" + Vill + "' and HHNo='" + HHNo + "' and SNo='" + SPNo4 + "'"));

                if (!txtSPNo1.getText().toString().equalsIgnoreCase("")) {
                    secSPNo.setVisibility(View.VISIBLE);
                }
                if (!txtSPNo2.getText().toString().equalsIgnoreCase("")) {
                    secSPNo1.setVisibility(View.VISIBLE);
                    secSPNo11.setVisibility(View.VISIBLE);
                }
                if (!txtSPNo3.getText().toString().equalsIgnoreCase("")) {
                    secSPNo2.setVisibility(View.VISIBLE);
                    secSPNo21.setVisibility(View.VISIBLE);
                }
                if (!txtSPNo4.getText().toString().equalsIgnoreCase("")) {
                    secSPNo3.setVisibility(View.VISIBLE);
                    secSPNo31.setVisibility(View.VISIBLE);
                }


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

                //txtEDU.setText(cur.getString(cur.getColumnIndex("EDU")).replace("null",""));
                String EDU = (cur.getString(cur.getColumnIndex("EDU")).replace("null", "").toString());
                if (EDU.equalsIgnoreCase("00")) {
                    txtEDU.setText("স্কুলে যায়নি বা প্রথম শ্রেণী পাশ করেনি");
                } else if (EDU.equalsIgnoreCase("01")) {
                    txtEDU.setText("১ম শ্রেনী");
                } else if (EDU.equalsIgnoreCase("02")) {
                    txtEDU.setText("২য় শ্রেনী");
                } else if (EDU.equalsIgnoreCase("03")) {
                    txtEDU.setText("৩য় শ্রেনী");
                } else if (EDU.equalsIgnoreCase("04")) {
                    txtEDU.setText("৪র্থ শ্রেনী");
                } else if (EDU.equalsIgnoreCase("05")) {
                    txtEDU.setText("৫ম শ্রেনী");
                } else if (EDU.equalsIgnoreCase("06")) {
                    txtEDU.setText("৬ষ্ঠ শ্রেনী");
                } else if (EDU.equalsIgnoreCase("07")) {
                    txtEDU.setText("৭ম শ্রেনী");
                } else if (EDU.equalsIgnoreCase("08")) {
                    txtEDU.setText("৮ম শ্রেনী");
                } else if (EDU.equalsIgnoreCase("09")) {
                    txtEDU.setText("৯ম শ্রেনী");
                } else if (EDU.equalsIgnoreCase("10")) {
                    txtEDU.setText("মাধ্যমিক বা সমতুল্য");
                } else if (EDU.equalsIgnoreCase("11")) {
                    txtEDU.setText("উচ্চ মাধ্যমিক বা সমতুল্য");
                } else if (EDU.equalsIgnoreCase("12")) {
                    txtEDU.setText("স্নাতক বা সমতুল্য");
                } else if (EDU.equalsIgnoreCase("13")) {
                    txtEDU.setText("স্নাতকোত্তর বা সমতুল্য");
                } else if (EDU.equalsIgnoreCase("14")) {
                    txtEDU.setText("ডাক্তারি");
                } else if (EDU.equalsIgnoreCase("15")) {
                    txtEDU.setText("ইঞ্জিনিয়ারিং");
                } else if (EDU.equalsIgnoreCase("16")) {
                    txtEDU.setText("বৃত্তিমুলক শিক্ষা");
                } else if (EDU.equalsIgnoreCase("17")) {
                    txtEDU.setText("কারিগরি শিক্ষা");
                } else if (EDU.equalsIgnoreCase("18")) {
                    txtEDU.setText("ধাত্রীবিদ্যা/নার্সিং");
                } else if (EDU.equalsIgnoreCase("19")) {
                    txtEDU.setText("অন্যান্য");
                } else if (EDU.equalsIgnoreCase("77")) {
                    txtEDU.setText("প্রযোজ্য নয়");
                } else if (EDU.equalsIgnoreCase("99")) {
                    txtEDU.setText("শিক্ষাগত যোগ্যতা নেই");
                }

/*
        listEDU.add("14-ডাক্তারি");
        listEDU.add("15-ইঞ্জিনিয়ারিং");
        listEDU.add("16-বৃত্তিমুলক শিক্ষা");
        listEDU.add("17-কারিগরি শিক্ষা");
        listEDU.add("18-ধাত্রীবিদ্যা/নার্সিং");
        listEDU.add("19-অন্যান্য");
        listEDU.add("77-প্রযোজ্য নয়");
        listEDU.add("99-শিক্ষাগত যোগ্যতা নেই");
*/
                String Rel = (cur.getString(cur.getColumnIndex("Rel")).replace("null", "").toString());
                if (Rel.equalsIgnoreCase("1")) {
                    txtRel.setText("ইসলাম");
                } else if (Rel.equalsIgnoreCase("2")) {
                    txtRel.setText("হিন্দু");
                } else if (Rel.equalsIgnoreCase("3")) {
                    txtRel.setText("বৌদ্ধ");
                } else if (Rel.equalsIgnoreCase("4")) {
                    txtRel.setText("খ্রীস্টান");
                } else if (Rel.equalsIgnoreCase("8")) {
                    txtRel.setText("Refuse to disclose");
                } else if (Rel.equalsIgnoreCase("0")) {
                    txtRel.setText("Not a believer");
                } else if (Rel.equalsIgnoreCase("0")) {
                    txtRel.setText("অন্যান্য");
                }
                //spnEDU.setSelection(Global.SpinnerItemPosition(spnEDU, 2 ,cur.getString(cur.getColumnIndex("EDU"))));
                //spnRel.setSelection(Global.SpinnerItemPosition(spnRel, 1 ,cur.getString(cur.getColumnIndex("Rel"))));
                if (cur.getString(cur.getColumnIndex("Nationality")).equals("1")) {
                    chkNationality.setChecked(true);
                } else if (cur.getString(cur.getColumnIndex("Nationality")).equals("2")) {
                    chkNationality.setChecked(false);
                }
                //txtOCP.setText(cur.getString(cur.getColumnIndex("OCP")).replace("null",""));
                String OCP = (cur.getString(cur.getColumnIndex("OCP")).replace("null", "").toString());
                txtOCP.setText(C.ReturnSingleValue("select substr('0' ||ocpCode, -2, 2)||'-'||ifnull(ocpName,'') as ocpName from ocpList where ocpCode='" + OCP + "'"));
                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(MemberFormFPI.this, e.getMessage());
            return;
        }
    }


    //Search Status

    private void FPIDataSearch(String Dist, String Upz, String UN, String Mouza, String Vill, String HHNo, String SNo) {
        try {
            RadioButton rb;
            String SQL1 = "";
            SQL1 = "Select FPI.Dist, FPI.Upz, FPI.UN, FPI.Mouza, FPI.Vill, FPI.HHNo, FPI.SNo as SNo, ifnull(FPI.HealthID,'') as HealthID,\n" +
                    "ifnull(FPI.NameEngStatus,'') as NameEngStatus,ifnull(FPI.RthStatus,'') as RthStatus,ifnull(FPI.HaveNIDStatus,'') as HaveNIDStatus,ifnull(FPI.NIDStatus,'') as FPINIDStatus,\n" +
                    "ifnull(FPI.HaveBRStatus,'') as HaveBRStatus,ifnull(FPI.BRIDStatus,'') as FPIBRIDStatus,ifnull(FPI.MobileNo1Status,'') as MobileNo1Status,\n" +
                    "ifnull(FPI.MobileNo2Status,'') as MobileNo2Status,ifnull(FPI.DOBStatus,'') as DOBStatus,ifnull(FPI.AgeStatus,'') as FPIAge,ifnull(FPI.DOBSourceStatus,'') as DOBSourceStatus, \n" +
                    "ifnull(FPI.BPlaceStatus,'') as BPlaceStatus,ifnull(FPI.FNoStatus,'') as FNoStatus,ifnull(FPI.FatherStatus,'') as FatherStatus, \n" +
                    "ifnull(FPI.MNoStatus,'') as MNoStatus,ifnull(FPI.MotherStatus,'') as MotherStatus,ifnull(FPI.SexStatus,'') as SexStatus, \n" +
                    "ifnull(FPI.MSStatus,'') as MSStatus,ifnull(FPI.SPNO1Status,'') as SPNO1Status,ifnull(FPI.SPNO2Status,'') as SPNO2Status,ifnull(FPI.SPNO3Status,'') as SPNO3Status,\n" +
                    "ifnull(FPI.SPNO4Status,'') as SPNO4Status,ifnull(FPI.EDUStatus,'') as EDUStatus,ifnull(FPI.RelStatus,'') as RelStatus,ifnull(FPI.NationalityStatus,'') as NationalityStatus,ifnull(FPI.OCPStatus,'') as OCPStatus\n" +
                    "from MemberFPI FPI Where Dist='" + Dist + "' and Upz='" + Upz + "' and UN='" + UN + "' and Mouza='" + Mouza + "' and Vill='" + Vill + "' and HHNo='" + HHNo + "' and SNo='" + SNo + "'";
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

                if (cur1.getString(cur1.getColumnIndex("NameEngStatus")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoNameEng1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("NameEngStatus")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoNameEng2.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("RthStatus")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoRth1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("RthStatus")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoRth2.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("HaveNIDStatus")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoNID1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("HaveNIDStatus")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoNID2.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("FPINIDStatus")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoNIDStatus1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("FPINIDStatus")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoNIDStatus2.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("HaveBRStatus")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoBRID1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("HaveBRStatus")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoBRID2.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("FPIBRIDStatus")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoBRIDStatus1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("FPIBRIDStatus")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoBRIDStatus2.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("MobileNo1Status")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoMobileNo1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("MobileNo1Status")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoMobileNo2.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("MobileNo2Status")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoMobileNo21.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("MobileNo2Status")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoMobileNo22.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("DOBStatus")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoDOB1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("DOBStatus")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoDOB2.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("FPIAge")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoAge1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("FPIAge")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoAge2.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("DOBSourceStatus")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoDOBStatus1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("DOBSourceStatus")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoDOBStatus2.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("BPlaceStatus")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoBPlace1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("BPlaceStatus")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoBPlace2.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("FNoStatus")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoFNo1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("FNoStatus")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoFNo2.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("FatherStatus")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoFather1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("FatherStatus")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoFather2.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("MNoStatus")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoMNo1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("MNoStatus")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoMNo2.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("MotherStatus")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoMother1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("MotherStatus")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoMother2.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("SexStatus")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoGender1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("SexStatus")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoGender2.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("MSStatus")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoMS1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("MSStatus")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoMS2.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("SPNO1Status")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoSPNo11.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("SPNO1Status")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoSPNo12.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("SPNO2Status")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoSPNo13.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("SPNO2Status")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoSPNo14.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("SPNO3Status")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoSPNo15.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("SPNO3Status")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoSPNo16.setChecked(true);
                }


                if (cur1.getString(cur1.getColumnIndex("SPNO4Status")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoSPNo17.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("SPNO4Status")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoSPNo18.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("EDUStatus")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoEDU1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("EDUStatus")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoEDU2.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("RelStatus")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoRel1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("RelStatus")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoRel2.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("NationalityStatus")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoNationality1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("NationalityStatus")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoNationality2.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("OCPStatus")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoOCP1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("OCPStatus")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoOCP2.setChecked(true);
                }
                cur1.moveToNext();
            }
            cur1.close();
        } catch (Exception e) {
            Connection.MessageBox(MemberFormFPI.this, e.getMessage());
            return;
        }
    }

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
