package com.data.rhis2;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import java.util.Calendar;

import Common.Connection;
import Common.Global;

/**
 * Created by Nisan on 9/16/2015.
 */
public class FWAReg extends TabActivity {
    boolean netwoekAvailable = false;
    Location currentLocation;
    double currentLatitude, currentLongitude;
    Location currentLocationNet;
    double currentLatitudeNet, currentLongitudeNet;
    Calendar c = Calendar.getInstance();

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mnuclose, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder adb = new AlertDialog.Builder(FWAReg.this);
        switch (item.getItemId()) {
            case R.id.menuClose:
                adb.setTitle("Close");
                adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান?");
                adb.setNegativeButton("না", null);
                adb.setPositiveButton("হ্যাঁ", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (g.getCallFrom().equals("1")) {
                            finish();
                            Intent f2 = new Intent(getApplicationContext(), PregRegView.class);
                            startActivity(f2);
                        } else {
                            finish();
                            Intent f2 = new Intent(getApplicationContext(), MemberList.class);
                            startActivity(f2);
                            g.setGeneratedId("");
                        }
                    }
                });
                adb.show();

                return true;

        }
        return false;
    }

    Connection C;
    Global g;
    LinearLayout seclblH;
    TextView lblHlblH;
    TextView lblHealthID;
    TextView txtHealthID;

    LinearLayout secSl;
    TextView txtSLNo;
    TextView VlblSLNo;

    TextView VlblSNo;
    TextView txtSNo;
    TextView txtELCONumber;

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

    String StartTime;
    Button cmdSave;
    LinearLayout secPregVisit;
    String sqlnew = "";
    String sqlupdate = "";

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


    String dist;
    String upazila;
    String unions;
    String mouza;
    String village;
    String provType;
    String provCode;
    String hhNo;
    String sno;
    String name;
    int age;

    String husbandName;
    int husbandage;

    String elcono;
    String pregnancyNo = "";
    String pregnancyNoDelivary = "";
    String LastDelivaryDate = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fwareg_main);

        C = new Connection(this);
        g = Global.getInstance();

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

        txtELCONumber = (TextView) findViewById(R.id.txtELCONumber);
        txtAge = (TextView) findViewById(R.id.txtAge);

        secName = (LinearLayout) findViewById(R.id.secName);
        VlblName = (TextView) findViewById(R.id.VlblName);
        txtName = (TextView) findViewById(R.id.txtName);

        secHusName = (LinearLayout) findViewById(R.id.secHusName);
        VlblHusName = (TextView) findViewById(R.id.VlblHusName);
        txtHusName = (TextView) findViewById(R.id.txtHusName);
        txtAgeYrHus = (TextView) findViewById(R.id.txtAgeYrHus);

        Bundle IDbundle = new Bundle();
        IDbundle = getIntent().getExtras();
        // String fornewpregnancy="";

        if (IDbundle != null) {
            // fornewpregnancy = IDbundle.getString("exists");
            sqlnew = IDbundle.getString("sqlnew");
            sqlupdate = IDbundle.getString("sqlupdate");
            // PGNNo = IDbundle.getString("PGNNo");
        }

        Resources ressources = getResources();
        final TabHost tabHost = getTabHost();

        // Basicinfo tab
        Intent intentBasicinfo = new Intent().setClass(this, PregRegFPI.class);
        TabSpec tabSpecBasicinfo = tabHost
                .newTabSpec("Basicinfo")
                .setIndicator("গর্ভবতী মহিলার সাধারন তথ্য", ressources.getDrawable(R.drawable.logo))
                .setContent(intentBasicinfo);


        /*
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            public void onTabChanged(String arg0) {
                for (int i = 0; i < tab.getTabWidget().getChildCount(); i++) {
                    tab.getTabWidget().getChildAt(i)
                            .setBackgroundResource(Color.LTGRAY); // unselected
                }
                tab.getTabWidget().getChildAt(tab.getCurrentTab())
                        .setBackgroundResource(Color.BLUE); // selected

            }
        });
        */


        if (IDbundle != null) {
            if (sqlnew != null) {
                if (sqlnew.length() > 0)
                    IDbundle.putString("sqlnew", sqlnew);
            }
            if (sqlupdate != null) {
                if (sqlupdate.length() > 0)
                    IDbundle.putString("sqlupdate", sqlupdate);
            }
            intentBasicinfo.putExtras(IDbundle);
        }


        ELCOProfile e = new ELCOProfile();
        // e.ELCOProfile(this, g.getHealthID());

        //call from elco
        if (g.getCallFrom().equals("elco")) {
            pregnancyNo = e.CurrentPregNumber(this, g.getHealthID());
            //txtHealthID.setText(g.getHealthID());
            DataSearch(g.getHealthID());

            txtELCONumber.setText(g.getAMElco());
            g.setAAge(txtAge.getText().toString());
        }
        //call from register
        else if (g.getCallFrom().equals("regis")) {
            pregnancyNo = e.LastPregNumber(this, g.getHealthID());
            DataSearch(g.getHealthID());
            txtELCONumber.setText(g.getAMElco());
            g.setAAge(txtAge.getText().toString());
        }
        //call from register
        else if (g.getCallFrom().equals("HAregis")) {
            /*pregnancyNo = e.CurrentPregNumber(this, g.getHealthID());
            DataSearch(g.getHealthID());
            g.setAAge(txtAge.getText().toString());*/

            DataSearch(g.getHealthID());

            pregnancyNo = e.LastPregNumber(this, g.getGeneratedId());
            pregnancyNoDelivary = e.LastPregNumberFromDelivary(this, g.getGeneratedId());


            //  e.PregnancyInfo(this, g.getGeneratedId(), pregnancyNo);
            txtELCONumber.setText(C.ReturnSingleValue("SELECT elcoNo FROM elco WHERE HealthId = " + g.getGeneratedId()));


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

                // pregnancyNo = e.CurrentPregNumber(this, g.getGeneratedId());
                //pregnancyNo="1";
            } else if (!pregnancyNo.equalsIgnoreCase(pregnancyNoDelivary)) {
                pregnancyNo = e.LastPregNumber(this, g.getGeneratedId());
            }
        } else if (g.getCallFrom().equals("1")) {
            pregnancyNo = e.LastPregNumber(this, g.getGeneratedId());
            DataSearch(g.getHealthID());
            txtELCONumber.setText(g.getAMElco());
            g.setAAge(txtAge.getText().toString());

        }


        String name = e.getName();
        dist = e.getDistrict();
        upazila = e.getUpazila();
        unions = e.getUnions();
        mouza = e.getMouza();
        village = e.getVillage();
        provType = e.getProvType();
        provCode = e.getProvCode();
        hhNo = e.getHhNo();
        sno = e.getSNo();
        name = e.getName();
        age = e.getAge();

        husbandName = e.getHusbandName();
        husbandage = e.getHusbandAge();

        elcono = e.getELCONo();



        /*txtHealthID.setText(g.getHealthID());
        txtSNo.setText(String.valueOf(sno));
        txtName.setText(name);
        txtAge.setText(String.valueOf(age));
        txtHusName.setText(husbandName);
        txtAgeYrHus.setText(String.valueOf(husbandage));
        txtELCONumber.setText(elcono);*/


        //startActivity(intentBasicinfo);

        // Anc tab
        Intent intentAnc = new Intent().setClass(this, ANCFPI.class);
        TabSpec tabSpecAnc = tabHost
                .newTabSpec("ANC")
                .setIndicator("গর্ভকালীন সেবার তথ্য", ressources.getDrawable(R.drawable.logo))
                .setContent(intentAnc);

        // Delivery tab
        Intent intentDelivery = new Intent().setClass(this, Delivfpi.class);
        TabSpec tabSpecDelivery = tabHost
                .newTabSpec("Delivery")
                .setIndicator("প্রসব সংক্রান্ত তথ্য", ressources.getDrawable(R.drawable.logo))
                .setContent(intentDelivery);

        // Child tab
        Intent intentChild = new Intent().setClass(this, ChildFPI.class);
        TabSpec tabSpecChild = tabHost
                .newTabSpec("Newborn")
                .setIndicator("নবজাতক সংক্রান্ত তথ্য", ressources.getDrawable(R.drawable.logo))
                .setContent(intentChild);

        // PNC tab
        Intent intentMotherNewbornPNC = new Intent().setClass(this, PNCMotherChildFPI.class);
        TabSpec tabSpecMotherNewbornPNC = tabHost
                .newTabSpec("PNC")
                .setIndicator("প্রসবোত্তর (মা ও নবজাতক)সেবার তথ্য", ressources.getDrawable(R.drawable.logo))
                .setContent(intentMotherNewbornPNC);

        //Color Name,HealthID
//1
        if ((Integer.valueOf(g.getAAge()) > 35 || Integer.valueOf(g.getAAge()) < 18)) {

            txtHealthID.setTextColor(Color.RED);
            txtName.setTextColor(Color.RED);
            // VlblName.setTextColor(Color.RED);
            txtAge.setTextColor(Color.RED);
        }
//2
        if (Integer.valueOf(PGNNo1()).equals(0) || Integer.valueOf(PGNNo1()).equals(1) || Integer.valueOf(PGNNo1()) >= 3) {
            txtHealthID.setTextColor(Color.RED);
            txtName.setTextColor(Color.RED);
            // VlblName.setTextColor(Color.RED);
            //  txtAge.setTextColor(Color.RED);
        }

        //3
        String TableName = "PregWomen";
        String AValue = String.format("Select healthId, pregNo  from %s where healthId = '%s' and pregNo = '%s'", TableName, g.getGeneratedId(), pregnancyNo);

        if (!C.Existence(AValue)) {

        } else if (C.Existence(AValue)) {
            //
            String height = C.ReturnSingleValue("Select ifnull(height,'') as height from pregWomen WHERE healthId ='" + g.getGeneratedId() + "' AND pregNo ='" + pregnancyNo + "'");
            if (height.equals("") | height.equals(null) | height.equals("null")) {

            } else if (height != null)

                if (Integer.valueOf(height) >= 1 & Integer.valueOf(height) <= 58) {
                    txtHealthID.setTextColor(Color.RED);
                    txtName.setTextColor(Color.RED);
                    // VlblName.setTextColor(Color.RED);
                    // txtAge.setTextColor(Color.RED);
                } else {

                }

        } else {

        }
        //5,6,7
        if (!C.Existence(AValue)) {

        } else if (C.Existence(AValue)) {
            //
            String riskHistory = "Select ifnull(riskHistoryNote,'') as riskHistoryNote from pregWomen WHERE healthId ='" + g.getGeneratedId() + "' AND pregNo ='" + pregnancyNo + "'";
            String riskStatus = C.ReturnSingleValue(riskHistory);
            String[] risk = Connection.split(riskStatus, ',');
            if (riskHistory.equals("") | riskHistory.equals("null") | riskHistory.equals(null)) {

            } else if (riskHistory != null)


                if (riskStatus.length() == 1) {
                    String risk1 = risk[0].toString();

                    if (risk1.equals("1") | risk1.equals("4")) {
                        txtHealthID.setTextColor(Color.RED);
                        txtName.setTextColor(Color.RED);
                        // VlblName.setTextColor(Color.RED);
                        // txtAge.setTextColor(Color.RED);
                    }


                    if (risk1.equals("5") | risk1.equals("6")) {
                        txtHealthID.setTextColor(Color.RED);
                        txtName.setTextColor(Color.RED);
                        // VlblName.setTextColor(Color.RED);
                        //  txtAge.setTextColor(Color.RED);
                    }

                    if (risk1.equals("9")) {
                        txtHealthID.setTextColor(Color.RED);
                        txtName.setTextColor(Color.RED);
                        // VlblName.setTextColor(Color.RED);
                        //  txtAge.setTextColor(Color.RED);
                    }
                } else if (riskStatus.length() == 3) {
                    String risk1 = risk[0].toString();
                    String risk2 = risk[1].toString();

                    if (risk1.equals("1") | risk2.equals("1") | risk1.equals("4") | risk2.equals("4")) {
                        txtHealthID.setTextColor(Color.RED);
                        txtName.setTextColor(Color.RED);
                        // VlblName.setTextColor(Color.RED);
                        // txtAge.setTextColor(Color.RED);
                    }

                    if (risk1.equals("5") | risk2.equals("5") | risk1.equals("6") | risk2.equals("6")) {
                        txtHealthID.setTextColor(Color.RED);
                        txtName.setTextColor(Color.RED);
                        // VlblName.setTextColor(Color.RED);
                        // txtAge.setTextColor(Color.RED);
                    }

                    if (risk1.equals("9") | risk2.equals("9")) {
                        txtHealthID.setTextColor(Color.RED);
                        txtName.setTextColor(Color.RED);
                        // VlblName.setTextColor(Color.RED);
                        //txtAge.setTextColor(Color.RED);
                    }
                } else if (riskStatus.length() == 5) {
                    String risk1 = risk[0].toString();
                    String risk2 = risk[1].toString();
                    String risk3 = risk[2].toString();

                    if (risk1.equals("1") | risk2.equals("1") | risk3.equals("1") | risk1.equals("4") | risk2.equals("4") | risk3.equals("4")) {
                        txtHealthID.setTextColor(Color.RED);
                        txtName.setTextColor(Color.RED);
                        // VlblName.setTextColor(Color.RED);
                        //txtAge.setTextColor(Color.RED);
                    }

                    if (risk1.equals("5") | risk2.equals("5") | risk3.equals("5") | risk1.equals("6") | risk2.equals("6") | risk3.equals("6")) {
                        txtHealthID.setTextColor(Color.RED);
                        txtName.setTextColor(Color.RED);
                        // VlblName.setTextColor(Color.RED);
                        //txtAge.setTextColor(Color.RED);
                    }

                    if (risk1.equals("9") | risk2.equals("9") | risk3.equals("9")) {
                        txtHealthID.setTextColor(Color.RED);
                        txtName.setTextColor(Color.RED);
                        // VlblName.setTextColor(Color.RED);
                        //txtAge.setTextColor(Color.RED);
                    }
                } else if (riskStatus.length() == 7) {
                    String risk1 = risk[0].toString();
                    String risk2 = risk[1].toString();
                    String risk3 = risk[2].toString();
                    String risk4 = risk[3].toString();

                    if (risk1.equals("1") | risk2.equals("1") | risk3.equals("1") | risk4.equals("1") | risk1.equals("4") | risk2.equals("4") | risk3.equals("4") | risk4.equals("4")) {
                        txtHealthID.setTextColor(Color.RED);
                        txtName.setTextColor(Color.RED);
                        // VlblName.setTextColor(Color.RED);
                        //txtAge.setTextColor(Color.RED);
                    }

                    if (risk1.equals("5") | risk2.equals("5") | risk3.equals("5") | risk4.equals("5") | risk1.equals("6") | risk2.equals("6") | risk3.equals("6") | risk4.equals("6")) {
                        txtHealthID.setTextColor(Color.RED);
                        txtName.setTextColor(Color.RED);
                        // VlblName.setTextColor(Color.RED);
                        // txtAge.setTextColor(Color.RED);
                    }

                    if (risk1.equals("9") | risk2.equals("9") | risk3.equals("9") | risk4.equals("9")) {
                        txtHealthID.setTextColor(Color.RED);
                        txtName.setTextColor(Color.RED);
                        // VlblName.setTextColor(Color.RED);
                        //txtAge.setTextColor(Color.RED);
                    }
                } else if (riskStatus.length() == 9) {
                    String risk1 = risk[0].toString();
                    String risk2 = risk[1].toString();
                    String risk3 = risk[2].toString();
                    String risk4 = risk[3].toString();
                    String risk5 = risk[4].toString();

                    if (risk1.equals("1") | risk2.equals("1") | risk3.equals("1") | risk4.equals("1") | risk5.equals("1") | risk1.equals("4") | risk2.equals("4") | risk3.equals("4") | risk4.equals("4") | risk5.equals("4")) {
                        txtHealthID.setTextColor(Color.RED);
                        txtName.setTextColor(Color.RED);
                        // VlblName.setTextColor(Color.RED);
                        //txtAge.setTextColor(Color.RED);
                    }

                    if (risk1.equals("5") | risk2.equals("5") | risk3.equals("5") | risk4.equals("5") | risk5.equals("5") | risk1.equals("6") | risk2.equals("6") | risk3.equals("6") | risk4.equals("6") | risk5.equals("6")) {
                        txtHealthID.setTextColor(Color.RED);
                        txtName.setTextColor(Color.RED);
                        // VlblName.setTextColor(Color.RED);
                        // txtAge.setTextColor(Color.RED);
                    }

                    if (risk1.equals("9") | risk2.equals("9") | risk3.equals("9") | risk4.equals("9") | risk5.equals("9")) {
                        txtHealthID.setTextColor(Color.RED);
                        txtName.setTextColor(Color.RED);
                        // VlblName.setTextColor(Color.RED);
                        // txtAge.setTextColor(Color.RED);
                    }
                } else if (riskStatus.length() == 11) {
                    String risk1 = risk[0].toString();
                    String risk2 = risk[1].toString();
                    String risk3 = risk[2].toString();
                    String risk4 = risk[3].toString();
                    String risk5 = risk[4].toString();
                    String risk6 = risk[5].toString();

                    if (risk1.equals("1") | risk2.equals("1") | risk3.equals("1") | risk4.equals("1") | risk5.equals("1") | risk6.equals("1") | risk1.equals("4") | risk2.equals("4") | risk3.equals("4") | risk4.equals("4") | risk5.equals("4") | risk6.equals("4")) {
                        txtHealthID.setTextColor(Color.RED);
                        txtName.setTextColor(Color.RED);
                        // VlblName.setTextColor(Color.RED);
                        // txtAge.setTextColor(Color.RED);
                    }

                    if (risk1.equals("5") | risk2.equals("5") | risk3.equals("5") | risk4.equals("5") | risk5.equals("5") | risk6.equals("5") | risk1.equals("6") | risk2.equals("6") | risk3.equals("6") | risk4.equals("6") | risk5.equals("6") | risk6.equals("6"))

                    {
                        txtHealthID.setTextColor(Color.RED);
                        txtName.setTextColor(Color.RED);
                        // VlblName.setTextColor(Color.RED);
                        //  txtAge.setTextColor(Color.RED);
                    }

                    if (risk1.equals("9") | risk2.equals("9") | risk3.equals("9") | risk4.equals("9") | risk5.equals("9") | risk6.equals("9")) {
                        txtHealthID.setTextColor(Color.RED);
                        txtName.setTextColor(Color.RED);
                        // VlblName.setTextColor(Color.RED);
                        //txtAge.setTextColor(Color.RED);
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
                        txtHealthID.setTextColor(Color.RED);
                        txtName.setTextColor(Color.RED);
                        // VlblName.setTextColor(Color.RED);
                        //txtAge.setTextColor(Color.RED);
                    }

                    if (risk1.equals("5") | risk2.equals("5") | risk3.equals("5") | risk4.equals("5") | risk5.equals("5") | risk6.equals("5") | risk7.equals("6") | risk1.equals("6") | risk2.equals("6") | risk3.equals("6") | risk4.equals("6") | risk5.equals("6") | risk6.equals("6") | risk7.equals("6"))

                    {
                        txtHealthID.setTextColor(Color.RED);
                        txtName.setTextColor(Color.RED);
                        // VlblName.setTextColor(Color.RED);
                        //txtAge.setTextColor(Color.RED);
                    }

                    if (risk1.equals("9") | risk2.equals("9") | risk3.equals("9") | risk4.equals("9") | risk5.equals("9") | risk6.equals("9") | risk7.equals("9")) {
                        txtHealthID.setTextColor(Color.RED);
                        txtName.setTextColor(Color.RED);
                        // VlblName.setTextColor(Color.RED);
                        //txtAge.setTextColor(Color.RED);
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
                        txtHealthID.setTextColor(Color.RED);
                        txtName.setTextColor(Color.RED);
                        // VlblName.setTextColor(Color.RED);
                        // txtAge.setTextColor(Color.RED);
                    }
                    if (risk1.equals("5") | risk2.equals("5") | risk3.equals("5") | risk4.equals("5") | risk5.equals("5") | risk6.equals("5") | risk7.equals("5") | risk8.equals("5") | risk1.equals("6") | risk2.equals("6") | risk3.equals("6") | risk4.equals("6") | risk5.equals("6") | risk6.equals("6") | risk7.equals("6") | risk8.equals("6"))

                    {
                        txtHealthID.setTextColor(Color.RED);
                        txtName.setTextColor(Color.RED);
                        // VlblName.setTextColor(Color.RED);
                        // txtAge.setTextColor(Color.RED);
                    }

                    if (risk1.equals("9") | risk2.equals("9") | risk3.equals("9") | risk4.equals("9") | risk5.equals("9") | risk6.equals("9") | risk7.equals("9") | risk8.equals("9")) {
                        txtHealthID.setTextColor(Color.RED);
                        txtName.setTextColor(Color.RED);
                        // VlblName.setTextColor(Color.RED);
                        //  txtAge.setTextColor(Color.RED);
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
                        txtHealthID.setTextColor(Color.RED);
                        txtName.setTextColor(Color.RED);
                        // VlblName.setTextColor(Color.RED);
                        //txtAge.setTextColor(Color.RED);
                    }

                    if (risk1.equals("5") | risk2.equals("5") | risk3.equals("5") | risk4.equals("5") | risk5.equals("5") | risk6.equals("5") | risk7.equals("5") | risk8.equals("5") | risk9.equals("5") | risk1.equals("6") | risk2.equals("6") | risk3.equals("6") | risk4.equals("6") | risk5.equals("6") | risk6.equals("6") | risk7.equals("6") | risk8.equals("6") | risk9.equals("6"))

                    {
                        txtHealthID.setTextColor(Color.RED);
                        txtName.setTextColor(Color.RED);
                        // VlblName.setTextColor(Color.RED);
                        //txtAge.setTextColor(Color.RED);
                    }

                    if (risk1.equals("9") | risk2.equals("9") | risk3.equals("9") | risk4.equals("9") | risk5.equals("9") | risk6.equals("9") | risk7.equals("9") | risk8.equals("9") | risk9.equals("9")) {
                        txtHealthID.setTextColor(Color.RED);
                        txtName.setTextColor(Color.RED);
                        // VlblName.setTextColor(Color.RED);
                        //txtAge.setTextColor(Color.RED);
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
                txtHealthID.setTextColor(Color.RED);
                txtName.setTextColor(Color.RED);
                // VlblName.setTextColor(Color.RED);
                //  txtAge.setTextColor(Color.RED);
            }
        }
        // Pnc tab
           /* Intent intentPnc = new Intent().setClass(this, Pnc.class);
            TabSpec tabSpecPnc = tabHost
                    .newTabSpec("Pnc")
                    .setIndicator("প্রসবোত্তর  সেবা (মা)", ressources.getDrawable(R.drawable.pnc))
                    .setContent(intentPnc);

            // Newborn tab
            Intent intentNewborn = new Intent().setClass(this, Newborn.class);
            TabSpec tabSpecNewborn = tabHost
                    .newTabSpec("Newborn")
                    .setIndicator("প্রসবোত্তর সেবা (নবজাতক)", ressources.getDrawable(R.drawable.newborn))
                    .setContent(intentNewborn);

            // Newborn tab
            Intent intentOthr = new Intent().setClass(this, Newborn.class);
            TabSpec tabSpecOthr = tabHost
                    .newTabSpec("Other")
                    .setIndicator("অন্যান্য", ressources.getDrawable(R.drawable.newborn))
                    .setContent(intentNewborn);
*/

        // add all tabs

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            public void onTabChanged(String arg0) {
                setTabColor(tabHost);

            }
        });

        tabHost.addTab(tabSpecBasicinfo);
        tabHost.addTab(tabSpecAnc);
        //tabHost.getTabWidget().getChildTabViewAt(1).setEnabled(false);
        tabHost.addTab(tabSpecDelivery);
        tabHost.addTab(tabSpecChild);
        tabHost.addTab(tabSpecMotherNewbornPNC);
        //set Windows tab as default (zero based)
        //tabHost.setActivated(false);

        tabHost.setCurrentTab(0);


        // ELCONoSearch(g.getHealthID());


        //DataSearch(g.getHealthID());

    }

    public static void setTabColor(TabHost tabhost) {
        for (int i = 0; i < tabhost.getTabWidget().getChildCount(); i++) {
            tabhost.getTabWidget().getChildAt(i).setBackgroundColor(Color.GRAY);
            ; //unselected
        }
        tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundColor(Color.GREEN); // selected
    }

    private String GetCountSLNoNumber() {

        String SQL = "select ((cast(Count(*) as int))) as Totalno  from PregWomen";
        String Val = String.valueOf(C.ReturnSingleValue(SQL));
        if (Val.equalsIgnoreCase("0")) {
            return "1";
        } else
            return Val;
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
        SQL = String.format("Select CAST(stillbirth AS INT) AS stillbirth from delivery where healthId = '%s'", g.getGeneratedId());
        SBirth = C.ReturnSingleValue(SQL);

        return SBirth;


    }

    private void DataSearch(String healthId) {
        try {
            String SQL = "";

            /*SQL = "Select SNo as SNo, ifnull(HealthID,'') as HealthID, ifnull(NameEng,'') as NameEng,ifnull(Age,'') as Age," +
                    "(select  NameEng  from member where  ProvCode=(select  ProvCode  from member  Where  healthid = '"+healthId +"')" +
                    "and HHNo=(select  HHNo  from member  Where  healthid = '"+healthId +"')" +
                    "and SNo=(select  SPNO1  from member  Where  healthid= '"+healthId +"'))as HusName," +
                    "(select  Age  from member where  " +
                    " ProvCode=(select  ProvCode  from member  Where  healthid='"+healthId +"') " +
                    "and HHNo=(select  HHNo  from member  Where  healthid='"+healthId +"') " +
                    "and SNo=(select  SPNO1  from member  Where  healthid='"+healthId +"'))as HusAge from " +
                    "Member where healthid='" + healthId +"'";*/

            SQL = "SELECT  E.SNo as SNo, ifnull(E.HealthID,'') as HealthID, E.NameEng AS NameEng,(Cast(((julianday(date('now'))-julianday(E.DOB))/30.4) as int)/12) AS Age,\n" +
                    "CASE WHEN CAST ( E.SPNO1 AS int ) = 55 THEN ifnull ( E.NameEng , '' )\n" +
                    "WHEN CAST ( E.SPNO1 AS int ) = 77 THEN ifnull ( E.NameEng , '' )\n" +
                    "WHEN CAST ( E.SPNO1 AS int ) = 88 THEN ifnull ( E.NameEng , '' )\n" +
                    "ELSE ( SELECT NameEng FROM member A\n" +
                    "WHERE A.ProvCode = ( SELECT B.ProvCode FROM member B WHERE B.healthid = '" + healthId + "'\n" +
                    "and A.Dist = B.Dist and A.Upz = B.Upz and A.UN = B.UN and A.Mouza = B.Mouza and\n" +
                    "A.Vill =B.Vill and A.HHNo = B.HHNo ) AND \n" +
                    "A.HHNo = ( SELECT C.HHNo FROM member C WHERE C.healthid = '" + healthId + "' )\n" +
                    "AND A.SNo = ( SELECT D.SPNO1 FROM member  D WHERE D.HealthID = '" + healthId + "' ) )\n" +
                    "END AS HusName,\n" +
                    "(SELECT (Cast(((julianday(date('now'))-julianday(A.DOB))/30.4) as int)/12) AS Age FROM member A\n" +
                    "WHERE A.ProvCode = ( SELECT B.ProvCode FROM member B WHERE B.healthid = '" + healthId + "'\n" +
                    "and A.Dist = B.Dist and A.Upz = B.Upz and A.UN = B.UN         and    A.Mouza    = B.Mouza    and\n" +
                    "A.Vill =B.Vill and A.HHNo = B.HHNo)\n" +
                    "AND A.HHNo = ( SELECT C.HHNo FROM member C WHERE C.healthid = '" + healthId + "' )\n" +
                    "AND A.SNo = ( SELECT D.SPNO1 FROM member  D WHERE D.HealthID = '" + healthId + "' )) AS HusAge\n" +
                    "FROM member E WHERE E.healthId ='" + healthId + "'";

            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                txtHealthID.setText(cur.getString(cur.getColumnIndex("HealthID")));
                txtSNo.setText(cur.getString(cur.getColumnIndex("SNo")));
                txtName.setText(cur.getString(cur.getColumnIndex("NameEng")));
                txtAge.setText(cur.getString(cur.getColumnIndex("Age")));
                txtHusName.setText(cur.getString(cur.getColumnIndex("HusName")));
                txtAgeYrHus.setText(cur.getString(cur.getColumnIndex("HusAge")));
                if (txtName.getText().toString().equalsIgnoreCase(txtHusName.getText().toString())) {
                    txtHusName.setText("");
                    txtAgeYrHus.setText("");
                    String HusName = C.ReturnSingleValue("Select husbandName from elco where cast(healthid as bigint)=" + g.getGeneratedId() + "");
                    txtHusName.setText(HusName);
                    String HusAge = C.ReturnSingleValue("Select husbandAge from elco where cast(healthid as bigint)=" + g.getGeneratedId() + "");
                    txtAgeYrHus.setText(HusAge);

                    txtHusName.setBackgroundColor(Color.parseColor("#ffffff"));
                    txtHusName.setEnabled(true);
                    txtAgeYrHus.setBackgroundColor(Color.parseColor("#ffffff"));
                    txtAgeYrHus.setEnabled(true);
                } else {
                    txtHusName.setBackgroundColor(Color.parseColor("#D7D7D7"));
                    //txtHusName.setEnabled(false);
                    txtAgeYrHus.setBackgroundColor(Color.parseColor("#D7D7D7"));
                    //txtHusAge.setEnabled(false);
                }


                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            // Connection.MessageBox(Deliv.this, e.getMessage());
            return;
        }
    }


    private void ELCONoSearch() {
        try {
            String SQL = "";
            SQL = "select E.ELCONo as ELCONo from ELCO E Where E.healthId='" + g.getHealthID() + "'";
            C.ReturnSingleValue(SQL);

        } catch (Exception e) {
            // Connection.MessageBox(PregReg.this, e.getMessage());
            return;
        }
    }



    /*private void setBackgroundColor() {
        int inactiveColor = getResources().getColor(R.color.inactive_tab);
        int activeColor = getResources().getColor(R.color.active_tab);

        // In this loop you will set the inactive tabs backgroung color
        for (int i = 0; i < tabWidget.getChildCount(); i++) {
            tabWidget.getChildAt(i).setBackgroundColor(inactiveColor);
        }

        // Here you will set the active tab background color
        tabWidget.getChildAt(tabHost.getCurrentTab()).setBackgroundColor(
                activeColor);
    }
    */
}

