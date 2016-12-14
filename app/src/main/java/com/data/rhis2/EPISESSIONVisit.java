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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
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
 * Created by Nisan on 6/6/2016.
 */
public class EPISESSIONVisit extends Activity {
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
        AlertDialog.Builder adb = new AlertDialog.Builder(EPISESSIONVisit.this);
        switch (item.getItemId()) {
            case R.id.menuClose:
                adb.setTitle("Close");
                adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান?");
                adb.setNegativeButton("না", null);
                adb.setPositiveButton("হ্যাঁ", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
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
    Calendar c = Calendar.getInstance();
    static final int DATE_DIALOG = 1;
    static final int TIME_DIALOG = 2;


    Connection C;

    Global g;
    SimpleAdapter dataAdapter;
    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    private String TableName;

    /*
      LinearLayout secIdno;
      TextView VlblIdno;
      EditText txtIdno;
      */
    LinearLayout seclblSessionvisit;
    LinearLayout seclblhowmanyEmp;
    LinearLayout secQVHA;
    TextView VlblQVHA;
    EditText txtQVHA;
    LinearLayout secQVFWA;
    TextView VlblQVFWA;
    EditText txtQVFWA;
    LinearLayout secQVN;
    TextView VlblQVN;
    EditText txtQVN;
    LinearLayout secQVOth;
    TextView VlblQVOth;
    EditText txtQVOth;
    LinearLayout seclVBCardHave;
    LinearLayout secQVChReg;
    TextView VlblQVChReg;
    RadioGroup rdogrpQVChReg;

    RadioButton rdoQVChReg1;
    RadioButton rdoQVChReg2;
    LinearLayout secQVWReg;
    TextView VlblQVWReg;
    RadioGroup rdogrpQVWReg;

    RadioButton rdoQVWReg1;
    RadioButton rdoQVWReg2;
    LinearLayout secQVChCard;
    TextView VlblQVChCard;
    RadioGroup rdogrpQVChCard;

    RadioButton rdoQVChCard1;
    RadioButton rdoQVChCard2;
    LinearLayout secQVWCard;
    TextView VlblQVWCard;
    RadioGroup rdogrpQVWCard;

    RadioButton rdoQVWCard1;
    RadioButton rdoQVWCard2;
    LinearLayout secQVTalBook;
    TextView VlblQVTalBook;
    RadioGroup rdogrpQVTalBook;

    RadioButton rdoQVTalBook1;
    RadioButton rdoQVTalBook2;
    LinearLayout secQVFIBook;
    TextView VlblQVFIBook;
    RadioGroup rdogrpQVFIBook;

    RadioButton rdoQVFIBook1;
    RadioButton rdoQVFIBook2;
    LinearLayout seclblItem;
    LinearLayout secQVVac;
    TextView VlblQVVac;
    RadioGroup rdogrpQVVac;

    RadioButton rdoQVVac1;
    RadioButton rdoQVVac2;
    LinearLayout secQVASerice;
    TextView VlblQVASerice;
    RadioGroup rdogrpQVASerice;

    RadioButton rdoQVASerice1;
    RadioButton rdoQVASerice2;
    LinearLayout secQVMSerice;
    TextView VlblQVMSerice;
    RadioGroup rdogrpQVMSerice;

    RadioButton rdoQVMSerice1;
    RadioButton rdoQVMSerice2;
    LinearLayout secQVSBox;
    TextView VlblQVSBox;
    RadioGroup rdogrpQVSBox;

    RadioButton rdoQVSBox1;
    RadioButton rdoQVSBox2;
    LinearLayout secQVVatARed;
    TextView VlblQVVatARed;
    RadioGroup rdogrpQVVatARed;

    RadioButton rdoQVVatARed1;
    RadioButton rdoQVVatARed2;
    LinearLayout secQVVatABlue;
    TextView VlblQVVatABlue;
    RadioGroup rdogrpQVVatABlue;

    RadioButton rdoQVVatABlue1;
    RadioButton rdoQVVatABlue2;
    LinearLayout secQVIICPac;
    TextView VlblQVIICPac;
    RadioGroup rdogrpQVIICPac;

    RadioButton rdoQVIICPac1;
    RadioButton rdoQVIICPac2;
    LinearLayout seclblIic;
    LinearLayout secQVBCG;
    TextView VlblQVBCG;
    RadioGroup rdogrpQVBCG;

    RadioButton rdoQVBCG1;
    RadioButton rdoQVBCG2;
    LinearLayout secQVPenta;
    TextView VlblQVPenta;
    RadioGroup rdogrpQVPenta;

    RadioButton rdoQVPenta1;
    RadioButton rdoQVPenta2;
    LinearLayout secQVPolio;
    TextView VlblQVPolio;
    RadioGroup rdogrpQVPolio;

    RadioButton rdoQVPolio1;
    RadioButton rdoQVPolio2;
    LinearLayout secQVPcv;
    TextView VlblQVPcv;
    RadioGroup rdogrpQVPcv;

    RadioButton rdoQVPcv1;
    RadioButton rdoQVPcv2;
    LinearLayout secQVIPV;
    TextView VlblQVIPV;
    RadioGroup rdogrpQVIPV;

    RadioButton rdoQVIPV1;
    RadioButton rdoQVIPV2;
    LinearLayout secQVMR;
    TextView VlblQVMR;
    RadioGroup rdogrpQVMR;

    RadioButton rdoQVMR1;
    RadioButton rdoQVMR2;
    LinearLayout secQVTT;
    TextView VlblQVTT;
    RadioGroup rdogrpQVTT;

    RadioButton rdoQVTT1;
    RadioButton rdoQVTT2;
    LinearLayout seclblVacSer;
    LinearLayout secQVSbcg;
    TextView VlblQVSbcg;
    RadioGroup rdogrpQVSbcg;

    RadioButton rdoQVSbcg1;
    RadioButton rdoQVSbcg2;
    RadioButton rdoQVSbcg3;
    LinearLayout secQVSPenta;
    TextView VlblQVSPenta;
    RadioGroup rdogrpQVSPenta;

    RadioButton rdoQVSPenta1;
    RadioButton rdoQVSPenta2;
    RadioButton rdoQVSPenta3;
    //RadioButton rdoQVSPenta4;
    LinearLayout secQVSPolio;
    TextView VlblQVSPolio;
    RadioGroup rdogrpQVSPolio;

    RadioButton rdoQVSPolio1;
    RadioButton rdoQVSPolio2;
    RadioButton rdoQVSPolio3;
    LinearLayout secQVSPcv;
    TextView VlblQVSPcv;
    RadioGroup rdogrpQVSPcv;

    RadioButton rdoQVSPcv1;
    RadioButton rdoQVSPcv2;
    RadioButton rdoQVSPcv3;
    LinearLayout secQVSIPV;
    TextView VlblQVSIPV;
    RadioGroup rdogrpQVSIPV;

    RadioButton rdoQVSIPV1;
    RadioButton rdoQVSIPV2;
    RadioButton rdoQVSIPV3;
    LinearLayout secQVSMR;
    TextView VlblQVSMR;
    RadioGroup rdogrpQVSMR;

    RadioButton rdoQVSMR1;
    RadioButton rdoQVSMR2;
    RadioButton rdoQVSMR3;
    LinearLayout secQVSTT;
    TextView VlblQVSTT;
    RadioGroup rdogrpQVSTT;

    RadioButton rdoQVSTT1;
    RadioButton rdoQVSTT2;
    RadioButton rdoQVSTT3;
    LinearLayout seclblNonTouch;
    LinearLayout secQVNToubcg;
    TextView VlblQVNToubcg;
    RadioGroup rdogrpQVNToubcg;

    RadioButton rdoQVNToubcg1;
    RadioButton rdoQVNToubcg2;
    //RadioButton rdoQVNToubcg3;
    LinearLayout secQVNToupant;
    TextView VlblQVNToupant;
    RadioGroup rdogrpQVNToupant;

    RadioButton rdoQVNToupant1;
    RadioButton rdoQVNToupant2;
    LinearLayout secQVNTouPolio;
    TextView VlblQVNTouPolio;
    RadioGroup rdogrpQVNTouPolio;

    RadioButton rdoQVNTouPolio1;
    RadioButton rdoQVNTouPolio2;
    LinearLayout secQVNToupcv;
    TextView VlblQVNToupcv;
    RadioGroup rdogrpQVNToupcv;

    RadioButton rdoQVNToupcv1;
    RadioButton rdoQVNToupcv2;
    LinearLayout secQVNTouIPV;
    TextView VlblQVNTouIPV;
    RadioGroup rdogrpQVNTouIPV;

    RadioButton rdoQVNTouIPV1;
    RadioButton rdoQVNTouIPV2;
    LinearLayout secQVNTouMR;
    TextView VlblQVNTouMR;
    RadioGroup rdogrpQVNTouMR;

    RadioButton rdoQVNTouMR1;
    RadioButton rdoQVNTouMR2;
    // RadioButton rdoQVNTouMR3;
    LinearLayout secQVNTouTT;
    TextView VlblQVNTouTT;
    RadioGroup rdogrpQVNTouTT;

    RadioButton rdoQVNTouTT1;
    RadioButton rdoQVNTouTT2;
    LinearLayout seclblroot;
    LinearLayout secQVRootbcg;
    TextView VlblQVRootbcg;
    RadioGroup rdogrpQVRootbcg;

    RadioButton rdoQVRootbcg1;
    RadioButton rdoQVRootbcg2;
    LinearLayout secQVNotwhy;
    TextView VlblQVNotwhy;
    Spinner spnQVNotwhy;
    LinearLayout secQVRootPenta;
    TextView VlblQVRootPenta;
    RadioGroup rdogrpQVRootPenta;

    RadioButton rdoQVRootPenta1;
    RadioButton rdoQVRootPenta2;
    LinearLayout secQVRootMR;
    TextView VlblQVRootMR;
    RadioGroup rdogrpQVRootMR;

    RadioButton rdoQVRootMR1;
    RadioButton rdoQVRootMR2;
    LinearLayout secQVRootTT;
    TextView VlblQVRootTT;
    RadioGroup rdogrpQVRootTT;

    RadioButton rdoQVRootTT1;
    RadioButton rdoQVRootTT2;
    LinearLayout secQVSRemoved;
    TextView VlblQVSRemoved;
    RadioGroup rdogrpQVSRemoved;

    RadioButton rdoQVSRemoved1;
    RadioButton rdoQVSRemoved2;
    LinearLayout seclblform;
    LinearLayout secQVFormbcg;
    TextView VlblQVFormbcg;
    RadioGroup rdogrpQVFormbcg;

    RadioButton rdoQVFormbcg1;
    RadioButton rdoQVFormbcg2;
    LinearLayout secQVFormpenta;
    TextView VlblQVFormpenta;
    RadioGroup rdogrpQVFormpenta;

    RadioButton rdoQVFormpenta1;
    RadioButton rdoQVFormpenta2;
    LinearLayout secQVFormPolio;
    TextView VlblQVFormPolio;
    RadioGroup rdogrpQVFormPolio;

    RadioButton rdoQVFormPolio1;
    RadioButton rdoQVFormPolio2;
    LinearLayout secQVFormpcv;
    TextView VlblQVFormpcv;
    RadioGroup rdogrpQVFormpcv;

    RadioButton rdoQVFormpcv1;
    RadioButton rdoQVFormpcv2;
    LinearLayout secQVFormipv;
    TextView VlblQVFormipv;
    RadioGroup rdogrpQVFormipv;

    RadioButton rdoQVFormipv1;
    RadioButton rdoQVFormipv2;
    LinearLayout secQVFormmr;
    TextView VlblQVFormmr;
    RadioGroup rdogrpQVFormmr;

    RadioButton rdoQVFormmr1;
    RadioButton rdoQVFormmr2;
    LinearLayout secQVFormtt;
    TextView VlblQVFormtt;
    RadioGroup rdogrpQVFormtt;

    RadioButton rdoQVFormtt1;
    RadioButton rdoQVFormtt2;
    LinearLayout seclblRegVacTCardCur;
    LinearLayout secQVCardregBook;
    TextView VlblQVCardregBook;
    RadioGroup rdogrpQVCardregBook;

    RadioButton rdoQVCardregBook1;
    RadioButton rdoQVCardregBook2;
    LinearLayout secQVCardVac;
    TextView VlblQVCardVac;
    RadioGroup rdogrpQVCardVac;

    RadioButton rdoQVCardVac1;
    RadioButton rdoQVCardVac2;
    LinearLayout secQVTTresearched;
    TextView VlblQVTTresearched;
    RadioGroup rdogrpQVTTresearched;

    RadioButton rdoQVTTresearched1;
    RadioButton rdoQVTTresearched2;
    LinearLayout secQVProtectors;
    TextView VlblQVProtectors;
    RadioGroup rdogrpQVProtectors;

    RadioButton rdoQVProtectors1;
    RadioButton rdoQVProtectors2;
    LinearLayout secQVDateOfVac;
    TextView VlblQVDateOfVac;
    RadioGroup rdogrpQVDateOfVac;

    RadioButton rdoQVDateOfVac1;
    RadioButton rdoQVDateOfVac2;
    LinearLayout secQVCard;
    TextView VlblQVCard;
    RadioGroup rdogrpQVCard;

    RadioButton rdoQVCard1;
    RadioButton rdoQVCard2;
    LinearLayout seclvlonemonth;
    LinearLayout secQVAFP;
    TextView VlblQVAFP;
    EditText txtQVAFP;
    LinearLayout secQVMeasles;
    TextView VlblQVMeasles;
    EditText txtQVMeasles;
    LinearLayout secQVNewborntetanus;
    TextView VlblQVNewborntetanus;
    EditText txtQVNewborntetanus;
    LinearLayout secQVOther;
    TextView VlblQVOther;
    EditText txtQVOther;
    LinearLayout secQVTodySession;
    TextView VlblQVTodySession;
    EditText txtQVTodySession;
    LinearLayout seclblpro;
    LinearLayout secQVProblem1;
    TextView VlblQVProblem1;
    // EditText txtQVProblem1;
    Spinner spnQVProblem2;
    Spinner spnQVSolve2;
    Spinner spnQVProblem3;
    Spinner spnQVSolve3;

    Spinner spnQVProblem4;
    Spinner spnQVSolve4;
    Spinner spnQVProblem5;
    Spinner spnQVSolve5;
    Spinner spnQVProblem6;
    Spinner spnQVSolve6;
    LinearLayout secQVProblem2;
    //TextView VlblQVProblem2;
    //EditText txtQVProblem2;

    // EditText txtQVProblem3;
    //EditText txtQVProblem4;
    //EditText txtQVProblem5;
    //LinearLayout secQVSolve1;
    TextView VlblQVSolve1;
    //EditText txtQVSolve1;
    LinearLayout secQVSolve2;
    //TextView VlblQVSolve2;
    // EditText txtQVSolve2;
    //EditText txtQVSolve3;
    //EditText txtQVSolve4;
    //EditText txtQVSolve5;

    String StartTime;
    Spinner spnProvider;
    Spinner spnEpiSubBlock;
    Spinner spnEPICenterName;
    LinearLayout secVdate;
    TextView VlblVdate;
    EditText dtpVdate;
    ImageButton btnVdate;

    //Spinner spnProvider;
    Spinner spnSessionDate;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.episessionvisit);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();

            TableName = "EPISessionVisit";

            spnProvider = (Spinner) findViewById(R.id.spnProvider);
            String Type = "";
            if (g.getProvType().equals("10")) {
                Type = "3";
            } else if (g.getProvType().equals("11")) {
                Type = "2";
            } else if (g.getProvType().equals("12")) {
                Type = "2";
            }

            spnSessionDate = (Spinner) findViewById(R.id.spnSessionDate);
            spnProvider.setAdapter(C.getArrayAdapter("Select '-' provName from ProviderDb Union Select provCode||'-'||provName from ProviderDb WHERE provType='" + Type + "'"));

            spnProvider.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    if (spnProvider.getSelectedItemPosition() > 0) {

                        // Select '9999-99-99' scheduleDate from epiScheduler Union

                        spnEpiSubBlock.setAdapter(C.getArrayAdapter("SELECT DISTINCT sb.BCode || '-' || sb.BNameBan\n" +
                                "           FROM HABlock sb\n" +
                                "               inner JOIN epiSchedulerUpdate epib\n" +
                                "                             ON sb.BCode = epib.subBlockId\n" +
                                "                             inner JOIN ProviderDB pdb\n" +
                                "                             ON epib.Dist = pdb.zillaid and  epib.Upz = pdb.upazilaid  and  epib.Un = pdb.unionid "));
                                //"where  providerId='" + Global.Left(spnProvider.getSelectedItem().toString(), 5) + "' and scheduleDate='" + Global.Right(spnSessionDate.getSelectedItem().toString(), 4) + "-" + Global.Mid(spnSessionDate.getSelectedItem().toString(), 3, 5) + "-" + Global.Left(spnSessionDate.getSelectedItem().toString(), 2) + "'"));

                      /*  spnSessionDate.setAdapter(C.getArrayAdapter("SELECT strftime( '%d/%m/%Y', date( scheduleDate )  ) AS scheduleDate\n" +
                                "  FROM epiScheduler where  providerId='" + Global.Left(spnProvider.getSelectedItem().toString(), 5) + "' and strftime('%m', date(scheduleDate))=(SELECT strftime('%m','now')) order by scheduleDate desc"));
                        ((TextView) findViewById(R.id.txtbar)).setText("");
                        ((TextView) findViewById(R.id.txtbar)).setText(g.getDay(Global.Left(spnSessionDate.getSelectedItem().toString(), 10)));*/
                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });
            spnEpiSubBlock = (Spinner) findViewById(R.id.spnEpiSubBlock);
            spnEpiSubBlock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    String val =Global.Left(spnEpiSubBlock.getSelectedItem().toString(), 2);
                    if (val.length() >0) {



                        // spnSessionDate
                       spnSessionDate.setAdapter(C.getArrayAdapter("SELECT distinct strftime( '%d/%m/%Y', date( scheduleDate )  ) AS scheduleDate\n" +
                                "  FROM epiSchedulerUpdate where subBlockId='" + val + "' and scheduleYear = ( SELECT strftime ( '%Y' , 'now' ) ) order by scheduleDate desc"));
                       //spnEPICenterName.setAdapter(C.getArrayAdapter("select substr('0' ||schedulerId, -2, 2)||'-'||centerName FROM epiSchedulerUpdate where subBlockId='" + val + "' and scheduleYear = ( SELECT strftime ( '%Y' , 'now' ) ) order by scheduleDate desc"));


                        // spnEPICenterName.setAdapter(C.getArrayAdapter("select substr('0' ||schedulerId, -2, 2)||'-'||providerId||'-'||centerName from epiScheduler where subBlockId='" + Global.Left(spnEpiSubBlock.getSelectedItem().toString(), 2) + "' and providerId='" + Global.Left(spnProvider.getSelectedItem().toString(), 5) + "' and  strftime('%d/%m/%Y', date(scheduleDate))='" + Global.Left(spnSessionDate.getSelectedItem().toString(), 10) + "'"));
                       PVisitStatus();
                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });

        /*    spnSessionDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    if (spnSessionDate.getSelectedItemPosition() >= 0) {

                        //spnEpiSubBlock.setVisibility(View.VISIBLE);
                        // spnEPICenterName.setVisibility(View.VISIBLE);


                      String val =Global.Left(spnSessionDate.getSelectedItem().toString(), 2);
                        if (val.length() >0) {

                           *//* String Y = "";
                            String M = "";
                            String D = "";

                            Y = Global.Right(spnSessionDate.getSelectedItem().toString(), 4);
                            D = Global.Left(spnSessionDate.getSelectedItem().toString(), 2);
                            M = Global.Mid(spnSessionDate.getSelectedItem().toString(), 3, 5);

                       *//*
                            ((TextView) findViewById(R.id.txtbar)).setText("");
                            ((TextView) findViewById(R.id.txtbar)).setText(g.getDay(Global.Left(spnSessionDate.getSelectedItem().toString(), 10)));
                            // spnEPICenterName.setAdapter(C.getArrayAdapter("select substr('0' ||schedulerId, -2, 2)||'-'||centerName FROM epiSchedulerUpdate where subBlockId='" + val + "' and scheduleDate='"++"' and scheduleYear = ( SELECT strftime ( '%Y' , 'now' ) ) order by scheduleDate desc"));
                            // spnSessionDate
                             // spnEPICenterName.setAdapter(C.getArrayAdapter("select substr('0' ||schedulerId, -2, 2)||'-'||providerId||'-'||centerName FROM epiSchedulerUpdate where subBlockId='" + Global.Left(spnEpiSubBlock.getSelectedItem().toString(), 2) + "' and scheduleYear = ( SELECT strftime ( '%Y' , 'now' ) ) order by scheduleDate desc"));



                        }



                    }

                   else if (spnSessionDate.getSelectedItemPosition() == 0)
                    {
                        ((TextView)findViewById(R.id.txtbar)).setText("");
                       // spnEpiSubBlock.setVisibility(View.GONE);
                        spnEPICenterName.setVisibility(View.GONE);
                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });*/

            spnSessionDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    String SubBlock =Global.Left(spnEpiSubBlock.getSelectedItem().toString(), 2);
                    String val =Global.Left(spnSessionDate.getSelectedItem().toString(), 10);


                    if (val.length() >=0) {
                        String Y = "";
                        String M = "";
                        String D = "";

                        Y = Global.Right(spnSessionDate.getSelectedItem().toString(), 4);
                        D = Global.Left(spnSessionDate.getSelectedItem().toString(), 2);
                        M = Global.Mid(spnSessionDate.getSelectedItem().toString(), 3, 5);


                        ((TextView) findViewById(R.id.txtbar)).setText("");
                        ((TextView) findViewById(R.id.txtbar)).setText(g.getDay(Global.Left(spnSessionDate.getSelectedItem().toString(), 10)));

                        //spnEPICenterName.setAdapter(C.getArrayAdapter("select substr('0' ||schedulerId, -2, 2)||'-'||centerName FROM epiSchedulerUpdate where subBlockId='" + val + "' and scheduleYear = ( SELECT strftime ( '%Y' , 'now' ) ) order by scheduleDate desc"));
                        spnEPICenterName.setAdapter(C.getArrayAdapter("select subBlockId||substr('0' ||schedulerId, -2, 2)||'-'||centerName FROM epiSchedulerUpdate where subBlockId='" + SubBlock + "' and scheduleDate='" +  Y + "-" + M + "-" + D +"'"));

                        spnEPICenterName.setEnabled(false);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });


            spnEPICenterName = (Spinner) findViewById(R.id.spnEPICenterName);
            /*
            secIdno = (LinearLayout) findViewById(R.id.secIdno);
            VlblIdno = (TextView) findViewById(R.id.VlblIdno);
            txtIdno = (EditText) findViewById(R.id.txtIdno);
            */
            secVdate = (LinearLayout) findViewById(R.id.secVdate);
            VlblVdate = (TextView) findViewById(R.id.VlblVdate);
            dtpVdate = (EditText) findViewById(R.id.dtpVdate);
            // dtpVdate.setText(Global.DateNowDMY());
            // ((TextView)findViewById(R.id.txtbar)).setText(g.getDay(dtpVdate.getText().toString()));


         /*

         dtpVdate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    spnVCenterName.setAdapter(C.getArrayAdapter("select substr('0' ||schedulerId, -2, 2)||'-'||providerId||'-'||centerName from epiScheduler where subBlockId='"+Global.Left(spnEpiSubBlock.getSelectedItem().toString(),2)+"' and providerId='"+Global.Left(spnProvider.getSelectedItem().toString(),5)+"' and  strftime('%d/%m/%Y', date(scheduleDate))='"+dtpVdate.getText()+"'"));

                }});

                */
            seclblSessionvisit = (LinearLayout) findViewById(R.id.seclblSessionvisit);
            seclblhowmanyEmp = (LinearLayout) findViewById(R.id.seclblhowmanyEmp);
            secQVHA = (LinearLayout) findViewById(R.id.secQVHA);
            VlblQVHA = (TextView) findViewById(R.id.VlblQVHA);
            txtQVHA = (EditText) findViewById(R.id.txtQVHA);
            secQVFWA = (LinearLayout) findViewById(R.id.secQVFWA);
            VlblQVFWA = (TextView) findViewById(R.id.VlblQVFWA);
            txtQVFWA = (EditText) findViewById(R.id.txtQVFWA);
            secQVN = (LinearLayout) findViewById(R.id.secQVN);
            VlblQVN = (TextView) findViewById(R.id.VlblQVN);
            txtQVN = (EditText) findViewById(R.id.txtQVN);
            secQVOth = (LinearLayout) findViewById(R.id.secQVOth);
            VlblQVOth = (TextView) findViewById(R.id.VlblQVOth);
            txtQVOth = (EditText) findViewById(R.id.txtQVOth);
            seclVBCardHave = (LinearLayout) findViewById(R.id.seclVBCardHave);
            secQVChReg = (LinearLayout) findViewById(R.id.secQVChReg);
            VlblQVChReg = (TextView) findViewById(R.id.VlblQVChReg);
            rdogrpQVChReg = (RadioGroup) findViewById(R.id.rdogrpQVChReg);

            rdoQVChReg1 = (RadioButton) findViewById(R.id.rdoQVChReg1);
            rdoQVChReg2 = (RadioButton) findViewById(R.id.rdoQVChReg2);
            secQVWReg = (LinearLayout) findViewById(R.id.secQVWReg);
            VlblQVWReg = (TextView) findViewById(R.id.VlblQVWReg);
            rdogrpQVWReg = (RadioGroup) findViewById(R.id.rdogrpQVWReg);

            rdoQVWReg1 = (RadioButton) findViewById(R.id.rdoQVWReg1);
            rdoQVWReg2 = (RadioButton) findViewById(R.id.rdoQVWReg2);
            secQVChCard = (LinearLayout) findViewById(R.id.secQVChCard);
            VlblQVChCard = (TextView) findViewById(R.id.VlblQVChCard);
            rdogrpQVChCard = (RadioGroup) findViewById(R.id.rdogrpQVChCard);

            rdoQVChCard1 = (RadioButton) findViewById(R.id.rdoQVChCard1);
            rdoQVChCard2 = (RadioButton) findViewById(R.id.rdoQVChCard2);
            secQVWCard = (LinearLayout) findViewById(R.id.secQVWCard);
            VlblQVWCard = (TextView) findViewById(R.id.VlblQVWCard);
            rdogrpQVWCard = (RadioGroup) findViewById(R.id.rdogrpQVWCard);

            rdoQVWCard1 = (RadioButton) findViewById(R.id.rdoQVWCard1);
            rdoQVWCard2 = (RadioButton) findViewById(R.id.rdoQVWCard2);
            secQVTalBook = (LinearLayout) findViewById(R.id.secQVTalBook);
            VlblQVTalBook = (TextView) findViewById(R.id.VlblQVTalBook);
            rdogrpQVTalBook = (RadioGroup) findViewById(R.id.rdogrpQVTalBook);

            rdoQVTalBook1 = (RadioButton) findViewById(R.id.rdoQVTalBook1);
            rdoQVTalBook2 = (RadioButton) findViewById(R.id.rdoQVTalBook2);
            secQVFIBook = (LinearLayout) findViewById(R.id.secQVFIBook);
            VlblQVFIBook = (TextView) findViewById(R.id.VlblQVFIBook);
            rdogrpQVFIBook = (RadioGroup) findViewById(R.id.rdogrpQVFIBook);

            rdoQVFIBook1 = (RadioButton) findViewById(R.id.rdoQVFIBook1);
            rdoQVFIBook2 = (RadioButton) findViewById(R.id.rdoQVFIBook2);
            seclblItem = (LinearLayout) findViewById(R.id.seclblItem);
            secQVVac = (LinearLayout) findViewById(R.id.secQVVac);
            VlblQVVac = (TextView) findViewById(R.id.VlblQVVac);
            rdogrpQVVac = (RadioGroup) findViewById(R.id.rdogrpQVVac);

            rdoQVVac1 = (RadioButton) findViewById(R.id.rdoQVVac1);
            rdoQVVac2 = (RadioButton) findViewById(R.id.rdoQVVac2);
            secQVASerice = (LinearLayout) findViewById(R.id.secQVASerice);
            VlblQVASerice = (TextView) findViewById(R.id.VlblQVASerice);
            rdogrpQVASerice = (RadioGroup) findViewById(R.id.rdogrpQVASerice);

            rdoQVASerice1 = (RadioButton) findViewById(R.id.rdoQVASerice1);
            rdoQVASerice2 = (RadioButton) findViewById(R.id.rdoQVASerice2);
            secQVMSerice = (LinearLayout) findViewById(R.id.secQVMSerice);
            VlblQVMSerice = (TextView) findViewById(R.id.VlblQVMSerice);
            rdogrpQVMSerice = (RadioGroup) findViewById(R.id.rdogrpQVMSerice);

            rdoQVMSerice1 = (RadioButton) findViewById(R.id.rdoQVMSerice1);
            rdoQVMSerice2 = (RadioButton) findViewById(R.id.rdoQVMSerice2);
            secQVSBox = (LinearLayout) findViewById(R.id.secQVSBox);
            VlblQVSBox = (TextView) findViewById(R.id.VlblQVSBox);
            rdogrpQVSBox = (RadioGroup) findViewById(R.id.rdogrpQVSBox);

            rdoQVSBox1 = (RadioButton) findViewById(R.id.rdoQVSBox1);
            rdoQVSBox2 = (RadioButton) findViewById(R.id.rdoQVSBox2);
            secQVVatARed = (LinearLayout) findViewById(R.id.secQVVatARed);
            VlblQVVatARed = (TextView) findViewById(R.id.VlblQVVatARed);
            rdogrpQVVatARed = (RadioGroup) findViewById(R.id.rdogrpQVVatARed);

            rdoQVVatARed1 = (RadioButton) findViewById(R.id.rdoQVVatARed1);
            rdoQVVatARed2 = (RadioButton) findViewById(R.id.rdoQVVatARed2);
            secQVVatABlue = (LinearLayout) findViewById(R.id.secQVVatABlue);
            VlblQVVatABlue = (TextView) findViewById(R.id.VlblQVVatABlue);
            rdogrpQVVatABlue = (RadioGroup) findViewById(R.id.rdogrpQVVatABlue);

            rdoQVVatABlue1 = (RadioButton) findViewById(R.id.rdoQVVatABlue1);
            rdoQVVatABlue2 = (RadioButton) findViewById(R.id.rdoQVVatABlue2);
            secQVIICPac = (LinearLayout) findViewById(R.id.secQVIICPac);
            VlblQVIICPac = (TextView) findViewById(R.id.VlblQVIICPac);
            rdogrpQVIICPac = (RadioGroup) findViewById(R.id.rdogrpQVIICPac);

            rdoQVIICPac1 = (RadioButton) findViewById(R.id.rdoQVIICPac1);
            rdoQVIICPac2 = (RadioButton) findViewById(R.id.rdoQVIICPac2);
            seclblIic = (LinearLayout) findViewById(R.id.seclblIic);
            secQVBCG = (LinearLayout) findViewById(R.id.secQVBCG);
            VlblQVBCG = (TextView) findViewById(R.id.VlblQVBCG);
            rdogrpQVBCG = (RadioGroup) findViewById(R.id.rdogrpQVBCG);

            rdoQVBCG1 = (RadioButton) findViewById(R.id.rdoQVBCG1);
            rdoQVBCG2 = (RadioButton) findViewById(R.id.rdoQVBCG2);
            secQVPenta = (LinearLayout) findViewById(R.id.secQVPenta);
            VlblQVPenta = (TextView) findViewById(R.id.VlblQVPenta);
            rdogrpQVPenta = (RadioGroup) findViewById(R.id.rdogrpQVPenta);

            rdoQVPenta1 = (RadioButton) findViewById(R.id.rdoQVPenta1);
            rdoQVPenta2 = (RadioButton) findViewById(R.id.rdoQVPenta2);
            secQVPolio = (LinearLayout) findViewById(R.id.secQVPolio);
            VlblQVPolio = (TextView) findViewById(R.id.VlblQVPolio);
            rdogrpQVPolio = (RadioGroup) findViewById(R.id.rdogrpQVPolio);

            rdoQVPolio1 = (RadioButton) findViewById(R.id.rdoQVPolio1);
            rdoQVPolio2 = (RadioButton) findViewById(R.id.rdoQVPolio2);
            secQVPcv = (LinearLayout) findViewById(R.id.secQVPcv);
            VlblQVPcv = (TextView) findViewById(R.id.VlblQVPcv);
            rdogrpQVPcv = (RadioGroup) findViewById(R.id.rdogrpQVPcv);

            rdoQVPcv1 = (RadioButton) findViewById(R.id.rdoQVPcv1);
            rdoQVPcv2 = (RadioButton) findViewById(R.id.rdoQVPcv2);
            secQVIPV = (LinearLayout) findViewById(R.id.secQVIPV);
            VlblQVIPV = (TextView) findViewById(R.id.VlblQVIPV);
            rdogrpQVIPV = (RadioGroup) findViewById(R.id.rdogrpQVIPV);

            rdoQVIPV1 = (RadioButton) findViewById(R.id.rdoQVIPV1);
            rdoQVIPV2 = (RadioButton) findViewById(R.id.rdoQVIPV2);
            secQVMR = (LinearLayout) findViewById(R.id.secQVMR);
            VlblQVMR = (TextView) findViewById(R.id.VlblQVMR);
            rdogrpQVMR = (RadioGroup) findViewById(R.id.rdogrpQVMR);

            rdoQVMR1 = (RadioButton) findViewById(R.id.rdoQVMR1);
            rdoQVMR2 = (RadioButton) findViewById(R.id.rdoQVMR2);
            secQVTT = (LinearLayout) findViewById(R.id.secQVTT);
            VlblQVTT = (TextView) findViewById(R.id.VlblQVTT);
            rdogrpQVTT = (RadioGroup) findViewById(R.id.rdogrpQVTT);

            rdoQVTT1 = (RadioButton) findViewById(R.id.rdoQVTT1);
            rdoQVTT2 = (RadioButton) findViewById(R.id.rdoQVTT2);
            seclblVacSer = (LinearLayout) findViewById(R.id.seclblVacSer);
            secQVSbcg = (LinearLayout) findViewById(R.id.secQVSbcg);
            VlblQVSbcg = (TextView) findViewById(R.id.VlblQVSbcg);
            rdogrpQVSbcg = (RadioGroup) findViewById(R.id.rdogrpQVSbcg);

            rdoQVSbcg1 = (RadioButton) findViewById(R.id.rdoQVSbcg1);
            rdoQVSbcg2 = (RadioButton) findViewById(R.id.rdoQVSbcg2);
            rdoQVSbcg3 = (RadioButton) findViewById(R.id.rdoQVSbcg3);
            secQVSPenta = (LinearLayout) findViewById(R.id.secQVSPenta);
            VlblQVSPenta = (TextView) findViewById(R.id.VlblQVSPenta);
            rdogrpQVSPenta = (RadioGroup) findViewById(R.id.rdogrpQVSPenta);

            rdoQVSPenta1 = (RadioButton) findViewById(R.id.rdoQVSPenta1);
            rdoQVSPenta2 = (RadioButton) findViewById(R.id.rdoQVSPenta2);
            rdoQVSPenta3 = (RadioButton) findViewById(R.id.rdoQVSPenta3);
            // rdoQVSPenta4 = (RadioButton) findViewById(R.id.rdoQVSPenta4);
            secQVSPolio = (LinearLayout) findViewById(R.id.secQVSPolio);
            VlblQVSPolio = (TextView) findViewById(R.id.VlblQVSPolio);
            rdogrpQVSPolio = (RadioGroup) findViewById(R.id.rdogrpQVSPolio);

            rdoQVSPolio1 = (RadioButton) findViewById(R.id.rdoQVSPolio1);
            rdoQVSPolio2 = (RadioButton) findViewById(R.id.rdoQVSPolio2);
            rdoQVSPolio3 = (RadioButton) findViewById(R.id.rdoQVSPolio3);
            secQVSPcv = (LinearLayout) findViewById(R.id.secQVSPcv);
            VlblQVSPcv = (TextView) findViewById(R.id.VlblQVSPcv);
            rdogrpQVSPcv = (RadioGroup) findViewById(R.id.rdogrpQVSPcv);

            rdoQVSPcv1 = (RadioButton) findViewById(R.id.rdoQVSPcv1);
            rdoQVSPcv2 = (RadioButton) findViewById(R.id.rdoQVSPcv2);
            rdoQVSPcv3 = (RadioButton) findViewById(R.id.rdoQVSPcv3);
            secQVSIPV = (LinearLayout) findViewById(R.id.secQVSIPV);
            VlblQVSIPV = (TextView) findViewById(R.id.VlblQVSIPV);
            rdogrpQVSIPV = (RadioGroup) findViewById(R.id.rdogrpQVSIPV);

            rdoQVSIPV1 = (RadioButton) findViewById(R.id.rdoQVSIPV1);
            rdoQVSIPV2 = (RadioButton) findViewById(R.id.rdoQVSIPV2);
            rdoQVSIPV3 = (RadioButton) findViewById(R.id.rdoQVSIPV3);
            secQVSMR = (LinearLayout) findViewById(R.id.secQVSMR);
            VlblQVSMR = (TextView) findViewById(R.id.VlblQVSMR);
            rdogrpQVSMR = (RadioGroup) findViewById(R.id.rdogrpQVSMR);

            rdoQVSMR1 = (RadioButton) findViewById(R.id.rdoQVSMR1);
            rdoQVSMR2 = (RadioButton) findViewById(R.id.rdoQVSMR2);
            rdoQVSMR3 = (RadioButton) findViewById(R.id.rdoQVSMR3);
            secQVSTT = (LinearLayout) findViewById(R.id.secQVSTT);
            VlblQVSTT = (TextView) findViewById(R.id.VlblQVSTT);
            rdogrpQVSTT = (RadioGroup) findViewById(R.id.rdogrpQVSTT);

            rdoQVSTT1 = (RadioButton) findViewById(R.id.rdoQVSTT1);
            rdoQVSTT2 = (RadioButton) findViewById(R.id.rdoQVSTT2);
            rdoQVSTT3 = (RadioButton) findViewById(R.id.rdoQVSTT3);
            seclblNonTouch = (LinearLayout) findViewById(R.id.seclblNonTouch);
            secQVNToubcg = (LinearLayout) findViewById(R.id.secQVNToubcg);
            VlblQVNToubcg = (TextView) findViewById(R.id.VlblQVNToubcg);
            rdogrpQVNToubcg = (RadioGroup) findViewById(R.id.rdogrpQVNToubcg);

            rdoQVNToubcg1 = (RadioButton) findViewById(R.id.rdoQVNToubcg1);
            rdoQVNToubcg2 = (RadioButton) findViewById(R.id.rdoQVNToubcg2);
            //rdoQVNToubcg3 = (RadioButton) findViewById(R.id.rdoQVNToubcg3);
            secQVNToupant = (LinearLayout) findViewById(R.id.secQVNToupant);
            VlblQVNToupant = (TextView) findViewById(R.id.VlblQVNToupant);
            rdogrpQVNToupant = (RadioGroup) findViewById(R.id.rdogrpQVNToupant);

            rdoQVNToupant1 = (RadioButton) findViewById(R.id.rdoQVNToupant1);
            rdoQVNToupant2 = (RadioButton) findViewById(R.id.rdoQVNToupant2);
            secQVNTouPolio = (LinearLayout) findViewById(R.id.secQVNTouPolio);
            VlblQVNTouPolio = (TextView) findViewById(R.id.VlblQVNTouPolio);
            rdogrpQVNTouPolio = (RadioGroup) findViewById(R.id.rdogrpQVNTouPolio);

            rdoQVNTouPolio1 = (RadioButton) findViewById(R.id.rdoQVNTouPolio1);
            rdoQVNTouPolio2 = (RadioButton) findViewById(R.id.rdoQVNTouPolio2);
            secQVNToupcv = (LinearLayout) findViewById(R.id.secQVNToupcv);
            VlblQVNToupcv = (TextView) findViewById(R.id.VlblQVNToupcv);
            rdogrpQVNToupcv = (RadioGroup) findViewById(R.id.rdogrpQVNToupcv);

            rdoQVNToupcv1 = (RadioButton) findViewById(R.id.rdoQVNToupcv1);
            rdoQVNToupcv2 = (RadioButton) findViewById(R.id.rdoQVNToupcv2);
            secQVNTouIPV = (LinearLayout) findViewById(R.id.secQVNTouIPV);
            VlblQVNTouIPV = (TextView) findViewById(R.id.VlblQVNTouIPV);
            rdogrpQVNTouIPV = (RadioGroup) findViewById(R.id.rdogrpQVNTouIPV);

            rdoQVNTouIPV1 = (RadioButton) findViewById(R.id.rdoQVNTouIPV1);
            rdoQVNTouIPV2 = (RadioButton) findViewById(R.id.rdoQVNTouIPV2);
            secQVNTouMR = (LinearLayout) findViewById(R.id.secQVNTouMR);
            VlblQVNTouMR = (TextView) findViewById(R.id.VlblQVNTouMR);
            rdogrpQVNTouMR = (RadioGroup) findViewById(R.id.rdogrpQVNTouMR);

            rdoQVNTouMR1 = (RadioButton) findViewById(R.id.rdoQVNTouMR1);
            rdoQVNTouMR2 = (RadioButton) findViewById(R.id.rdoQVNTouMR2);
            // rdoQVNTouMR3 = (RadioButton) findViewById(R.id.rdoQVNTouMR3);
            secQVNTouTT = (LinearLayout) findViewById(R.id.secQVNTouTT);
            VlblQVNTouTT = (TextView) findViewById(R.id.VlblQVNTouTT);
            rdogrpQVNTouTT = (RadioGroup) findViewById(R.id.rdogrpQVNTouTT);

            rdoQVNTouTT1 = (RadioButton) findViewById(R.id.rdoQVNTouTT1);
            rdoQVNTouTT2 = (RadioButton) findViewById(R.id.rdoQVNTouTT2);
            seclblroot = (LinearLayout) findViewById(R.id.seclblroot);
            secQVRootbcg = (LinearLayout) findViewById(R.id.secQVRootbcg);
            VlblQVRootbcg = (TextView) findViewById(R.id.VlblQVRootbcg);
            rdogrpQVRootbcg = (RadioGroup) findViewById(R.id.rdogrpQVRootbcg);
          //  secQVNotwhy.setVisibility(View.GONE);
            rdoQVRootbcg1 = (RadioButton) findViewById(R.id.rdoQVRootbcg1);
            rdoQVRootbcg2 = (RadioButton) findViewById(R.id.rdoQVRootbcg2);

            rdogrpQVRootbcg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup arg0, int id) {
                    if (id == R.id.rdoQVRootbcg1) {

                        secQVNotwhy.setVisibility(View.GONE);
                        spnQVNotwhy.setSelection(0);

                    } else if (id == R.id.rdoQVRootbcg2) {

                        secQVNotwhy.setVisibility(View.VISIBLE);

                    }
                }
            });

            secQVNotwhy = (LinearLayout) findViewById(R.id.secQVNotwhy);
            VlblQVNotwhy = (TextView) findViewById(R.id.VlblQVNotwhy);
            spnQVNotwhy = (Spinner) findViewById(R.id.spnQVNotwhy);
            List<String> listQVNotwhy = new ArrayList<String>();

            listQVNotwhy.add("");
            listQVNotwhy.add("01-সঠিক পদ্ধতিতে দেয়া হয়নি");
            ArrayAdapter<String> adptrQVNotwhy = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listQVNotwhy);
            spnQVNotwhy.setAdapter(adptrQVNotwhy);

            secQVRootPenta = (LinearLayout) findViewById(R.id.secQVRootPenta);
            VlblQVRootPenta = (TextView) findViewById(R.id.VlblQVRootPenta);
            rdogrpQVRootPenta = (RadioGroup) findViewById(R.id.rdogrpQVRootPenta);

            rdoQVRootPenta1 = (RadioButton) findViewById(R.id.rdoQVRootPenta1);
            rdoQVRootPenta2 = (RadioButton) findViewById(R.id.rdoQVRootPenta2);
            secQVRootMR = (LinearLayout) findViewById(R.id.secQVRootMR);
            VlblQVRootMR = (TextView) findViewById(R.id.VlblQVRootMR);
            rdogrpQVRootMR = (RadioGroup) findViewById(R.id.rdogrpQVRootMR);

            rdoQVRootMR1 = (RadioButton) findViewById(R.id.rdoQVRootMR1);
            rdoQVRootMR2 = (RadioButton) findViewById(R.id.rdoQVRootMR2);
            secQVRootTT = (LinearLayout) findViewById(R.id.secQVRootTT);
            VlblQVRootTT = (TextView) findViewById(R.id.VlblQVRootTT);
            rdogrpQVRootTT = (RadioGroup) findViewById(R.id.rdogrpQVRootTT);

            rdoQVRootTT1 = (RadioButton) findViewById(R.id.rdoQVRootTT1);
            rdoQVRootTT2 = (RadioButton) findViewById(R.id.rdoQVRootTT2);
            secQVSRemoved = (LinearLayout) findViewById(R.id.secQVSRemoved);
            VlblQVSRemoved = (TextView) findViewById(R.id.VlblQVSRemoved);
            rdogrpQVSRemoved = (RadioGroup) findViewById(R.id.rdogrpQVSRemoved);

            rdoQVSRemoved1 = (RadioButton) findViewById(R.id.rdoQVSRemoved1);
            rdoQVSRemoved2 = (RadioButton) findViewById(R.id.rdoQVSRemoved2);
            seclblform = (LinearLayout) findViewById(R.id.seclblform);
            secQVFormbcg = (LinearLayout) findViewById(R.id.secQVFormbcg);
            VlblQVFormbcg = (TextView) findViewById(R.id.VlblQVFormbcg);
            rdogrpQVFormbcg = (RadioGroup) findViewById(R.id.rdogrpQVFormbcg);

            rdoQVFormbcg1 = (RadioButton) findViewById(R.id.rdoQVFormbcg1);
            rdoQVFormbcg2 = (RadioButton) findViewById(R.id.rdoQVFormbcg2);
            secQVFormpenta = (LinearLayout) findViewById(R.id.secQVFormpenta);
            VlblQVFormpenta = (TextView) findViewById(R.id.VlblQVFormpenta);
            rdogrpQVFormpenta = (RadioGroup) findViewById(R.id.rdogrpQVFormpenta);

            rdoQVFormpenta1 = (RadioButton) findViewById(R.id.rdoQVFormpenta1);
            rdoQVFormpenta2 = (RadioButton) findViewById(R.id.rdoQVFormpenta2);
            secQVFormPolio = (LinearLayout) findViewById(R.id.secQVFormPolio);
            VlblQVFormPolio = (TextView) findViewById(R.id.VlblQVFormPolio);
            rdogrpQVFormPolio = (RadioGroup) findViewById(R.id.rdogrpQVFormPolio);

            rdoQVFormPolio1 = (RadioButton) findViewById(R.id.rdoQVFormPolio1);
            rdoQVFormPolio2 = (RadioButton) findViewById(R.id.rdoQVFormPolio2);
            secQVFormpcv = (LinearLayout) findViewById(R.id.secQVFormpcv);
            VlblQVFormpcv = (TextView) findViewById(R.id.VlblQVFormpcv);
            rdogrpQVFormpcv = (RadioGroup) findViewById(R.id.rdogrpQVFormpcv);

            rdoQVFormpcv1 = (RadioButton) findViewById(R.id.rdoQVFormpcv1);
            rdoQVFormpcv2 = (RadioButton) findViewById(R.id.rdoQVFormpcv2);
            secQVFormipv = (LinearLayout) findViewById(R.id.secQVFormipv);
            VlblQVFormipv = (TextView) findViewById(R.id.VlblQVFormipv);
            rdogrpQVFormipv = (RadioGroup) findViewById(R.id.rdogrpQVFormipv);

            rdoQVFormipv1 = (RadioButton) findViewById(R.id.rdoQVFormipv1);
            rdoQVFormipv2 = (RadioButton) findViewById(R.id.rdoQVFormipv2);
            secQVFormmr = (LinearLayout) findViewById(R.id.secQVFormmr);
            VlblQVFormmr = (TextView) findViewById(R.id.VlblQVFormmr);
            rdogrpQVFormmr = (RadioGroup) findViewById(R.id.rdogrpQVFormmr);

            rdoQVFormmr1 = (RadioButton) findViewById(R.id.rdoQVFormmr1);
            rdoQVFormmr2 = (RadioButton) findViewById(R.id.rdoQVFormmr2);
            secQVFormtt = (LinearLayout) findViewById(R.id.secQVFormtt);
            VlblQVFormtt = (TextView) findViewById(R.id.VlblQVFormtt);
            rdogrpQVFormtt = (RadioGroup) findViewById(R.id.rdogrpQVFormtt);

            rdoQVFormtt1 = (RadioButton) findViewById(R.id.rdoQVFormtt1);
            rdoQVFormtt2 = (RadioButton) findViewById(R.id.rdoQVFormtt2);
            seclblRegVacTCardCur = (LinearLayout) findViewById(R.id.seclblRegVacTCardCur);
            secQVCardregBook = (LinearLayout) findViewById(R.id.secQVCardregBook);
            VlblQVCardregBook = (TextView) findViewById(R.id.VlblQVCardregBook);
            rdogrpQVCardregBook = (RadioGroup) findViewById(R.id.rdogrpQVCardregBook);

            rdoQVCardregBook1 = (RadioButton) findViewById(R.id.rdoQVCardregBook1);
            rdoQVCardregBook2 = (RadioButton) findViewById(R.id.rdoQVCardregBook2);
            secQVCardVac = (LinearLayout) findViewById(R.id.secQVCardVac);
            VlblQVCardVac = (TextView) findViewById(R.id.VlblQVCardVac);
            rdogrpQVCardVac = (RadioGroup) findViewById(R.id.rdogrpQVCardVac);

            rdoQVCardVac1 = (RadioButton) findViewById(R.id.rdoQVCardVac1);
            rdoQVCardVac2 = (RadioButton) findViewById(R.id.rdoQVCardVac2);
            secQVTTresearched = (LinearLayout) findViewById(R.id.secQVTTresearched);
            VlblQVTTresearched = (TextView) findViewById(R.id.VlblQVTTresearched);
            rdogrpQVTTresearched = (RadioGroup) findViewById(R.id.rdogrpQVTTresearched);

            rdoQVTTresearched1 = (RadioButton) findViewById(R.id.rdoQVTTresearched1);
            rdoQVTTresearched2 = (RadioButton) findViewById(R.id.rdoQVTTresearched2);
            secQVProtectors = (LinearLayout) findViewById(R.id.secQVProtectors);
            VlblQVProtectors = (TextView) findViewById(R.id.VlblQVProtectors);
            rdogrpQVProtectors = (RadioGroup) findViewById(R.id.rdogrpQVProtectors);

            rdoQVProtectors1 = (RadioButton) findViewById(R.id.rdoQVProtectors1);
            rdoQVProtectors2 = (RadioButton) findViewById(R.id.rdoQVProtectors2);
            secQVDateOfVac = (LinearLayout) findViewById(R.id.secQVDateOfVac);
            VlblQVDateOfVac = (TextView) findViewById(R.id.VlblQVDateOfVac);
            rdogrpQVDateOfVac = (RadioGroup) findViewById(R.id.rdogrpQVDateOfVac);

            rdoQVDateOfVac1 = (RadioButton) findViewById(R.id.rdoQVDateOfVac1);
            rdoQVDateOfVac2 = (RadioButton) findViewById(R.id.rdoQVDateOfVac2);
            secQVCard = (LinearLayout) findViewById(R.id.secQVCard);
            VlblQVCard = (TextView) findViewById(R.id.VlblQVCard);
            rdogrpQVCard = (RadioGroup) findViewById(R.id.rdogrpQVCard);

            rdoQVCard1 = (RadioButton) findViewById(R.id.rdoQVCard1);
            rdoQVCard2 = (RadioButton) findViewById(R.id.rdoQVCard2);
            seclvlonemonth = (LinearLayout) findViewById(R.id.seclvlonemonth);
            secQVAFP = (LinearLayout) findViewById(R.id.secQVAFP);
            VlblQVAFP = (TextView) findViewById(R.id.VlblQVAFP);
            txtQVAFP = (EditText) findViewById(R.id.txtQVAFP);
            secQVMeasles = (LinearLayout) findViewById(R.id.secQVMeasles);
            VlblQVMeasles = (TextView) findViewById(R.id.VlblQVMeasles);
            txtQVMeasles = (EditText) findViewById(R.id.txtQVMeasles);
            secQVNewborntetanus = (LinearLayout) findViewById(R.id.secQVNewborntetanus);
            VlblQVNewborntetanus = (TextView) findViewById(R.id.VlblQVNewborntetanus);
            txtQVNewborntetanus = (EditText) findViewById(R.id.txtQVNewborntetanus);
            secQVOther = (LinearLayout) findViewById(R.id.secQVOther);
            VlblQVOther = (TextView) findViewById(R.id.VlblQVOther);
            txtQVOther = (EditText) findViewById(R.id.txtQVOther);
            secQVTodySession = (LinearLayout) findViewById(R.id.secQVTodySession);
            VlblQVTodySession = (TextView) findViewById(R.id.VlblQVTodySession);
            txtQVTodySession = (EditText) findViewById(R.id.txtQVTodySession);
            seclblpro = (LinearLayout) findViewById(R.id.seclblpro);
            secQVProblem1 = (LinearLayout) findViewById(R.id.secQVProblem1);
            VlblQVProblem1 = (TextView) findViewById(R.id.VlblQVProblem1);


            List<String> Problem2 = new ArrayList<String>();

            Problem2.add("");
            Problem2.add("01-টিকার জায়গা ফুলে যেতে পারে");
            Problem2.add("02-টিকার জায়গা পেকে যেতে পারে");
            Problem2.add("03-টিকার জায়গা শক্ত হতে পারে");
            Problem2.add("04-টিকার জায়গা চাকা হতে পারে");
            Problem2.add("05-টিকা দেওয়ার পর খিচুনী হতে পারে");
            Problem2.add("06-জ্বর আসতে পারে");


            spnQVProblem2 = (Spinner) findViewById(R.id.spnQVProblem2);
            ArrayAdapter<String> Problem2adptr = new ArrayAdapter<String>(this, R.layout.multiline_spinner_dropdown_item, Problem2);
            spnQVProblem2.setAdapter(Problem2adptr);


            List<String> Problem3 = new ArrayList<String>();

            Problem3.add("");
            Problem3.add("01-টিকার জায়গা ফুলে যেতে পারে");
            Problem3.add("02-টিকার জায়গা পেকে যেতে পারে");
            Problem3.add("03-টিকার জায়গা শক্ত হতে পারে");
            Problem3.add("04-টিকার জায়গা চাকা হতে পারে");
            Problem3.add("05-টিকা দেওয়ার পর খিচুনী হতে পারে");
            Problem3.add("06-জ্বর আসতে পারে");

            spnQVProblem3 = (Spinner) findViewById(R.id.spnQVProblem3);
            ArrayAdapter<String> Problem3adptr = new ArrayAdapter<String>(this, R.layout.multiline_spinner_dropdown_item, Problem3);
            spnQVProblem3.setAdapter(Problem3adptr);

            List<String> Problem4 = new ArrayList<String>();

            Problem4.add("");
            Problem4.add("01-টিকার জায়গা ফুলে যেতে পারে");
            Problem4.add("02-টিকার জায়গা পেকে যেতে পারে");
            Problem4.add("03-টিকার জায়গা শক্ত হতে পারে");
            Problem4.add("04-টিকার জায়গা চাকা হতে পারে");
            Problem4.add("05-টিকা দেওয়ার পর খিচুনী হতে পারে");
            Problem4.add("06-জ্বর আসতে পারে");

            spnQVProblem4 = (Spinner) findViewById(R.id.spnQVProblem4);
            ArrayAdapter<String> Problem4adptr = new ArrayAdapter<String>(this, R.layout.multiline_spinner_dropdown_item, Problem4);
            spnQVProblem4.setAdapter(Problem4adptr);

            List<String> Problem5 = new ArrayList<String>();

            Problem5.add("");
            Problem5.add("01-টিকার জায়গা ফুলে যেতে পারে");
            Problem5.add("02-টিকার জায়গা পেকে যেতে পারে");
            Problem5.add("03-টিকার জায়গা শক্ত হতে পারে");
            Problem5.add("04-টিকার জায়গা চাকা হতে পারে");
            Problem5.add("05-টিকা দেওয়ার পর খিচুনী হতে পারে");
            Problem5.add("06-জ্বর আসতে পারে");

            spnQVProblem5 = (Spinner) findViewById(R.id.spnQVProblem5);
            ArrayAdapter<String> Problem5adptr = new ArrayAdapter<String>(this, R.layout.multiline_spinner_dropdown_item, Problem5);
            spnQVProblem5.setAdapter(Problem5adptr);

            List<String> Problem6 = new ArrayList<String>();

            Problem6.add("");
            Problem6.add("01-টিকার জায়গা ফুলে যেতে পারে");
            Problem6.add("02-টিকার জায়গা পেকে যেতে পারে");
            Problem6.add("03-টিকার জায়গা শক্ত হতে পারে");
            Problem6.add("04-টিকার জায়গা চাকা হতে পারে");
            Problem6.add("05-টিকা দেওয়ার পর খিচুনী হতে পারে");
            Problem6.add("06-জ্বর আসতে পারে");

            spnQVProblem6 = (Spinner) findViewById(R.id.spnQVProblem6);
            ArrayAdapter<String> Problem6adptr = new ArrayAdapter<String>(this, R.layout.multiline_spinner_dropdown_item, Problem6);
            spnQVProblem6.setAdapter(Problem6adptr);

            List<String> Solve2 = new ArrayList<String>();

            Solve2.add("");
            Solve2.add("01-হাসপাতালে প্রেরণ করা");


            spnQVSolve2 = (Spinner) findViewById(R.id.spnQVSolve2);
            ArrayAdapter<String> Solve2adptr = new ArrayAdapter<String>(this, R.layout.multiline_spinner_dropdown_item, Solve2);
            spnQVSolve2.setAdapter(Solve2adptr);
            List<String> Solve3 = new ArrayList<String>();
            Solve3.add("");
            Solve3.add("01-হাসপাতালে প্রেরণ করা");


            spnQVSolve3 = (Spinner) findViewById(R.id.spnQVSolve3);
            ArrayAdapter<String> Solve3adptr = new ArrayAdapter<String>(this, R.layout.multiline_spinner_dropdown_item, Solve3);
            spnQVSolve3.setAdapter(Solve3adptr);

            List<String> Solve4 = new ArrayList<String>();
            Solve4.add("");
            Solve4.add("01-হাসপাতালে প্রেরণ করা");


            spnQVSolve4 = (Spinner) findViewById(R.id.spnQVSolve4);
            ArrayAdapter<String> Solve4adptr = new ArrayAdapter<String>(this, R.layout.multiline_spinner_dropdown_item, Solve4);
            spnQVSolve4.setAdapter(Solve4adptr);

            List<String> Solve5 = new ArrayList<String>();
            Solve5.add("");
            Solve5.add("01-হাসপাতালে প্রেরণ করা");


            spnQVSolve5 = (Spinner) findViewById(R.id.spnQVSolve5);
            ArrayAdapter<String> Solve5adptr = new ArrayAdapter<String>(this, R.layout.multiline_spinner_dropdown_item, Solve5);
            spnQVSolve5.setAdapter(Solve5adptr);

            List<String> Solve6 = new ArrayList<String>();
            Solve6.add("");
            Solve6.add("01-হাসপাতালে প্রেরণ করা");


            spnQVSolve6 = (Spinner) findViewById(R.id.spnQVSolve6);
            ArrayAdapter<String> Solve6adptr = new ArrayAdapter<String>(this, R.layout.multiline_spinner_dropdown_item, Solve6);
            spnQVSolve6.setAdapter(Solve5adptr);

            secQVProblem2 = (LinearLayout) findViewById(R.id.secQVProblem2);
            //  VlblQVProblem2 = (TextView) findViewById(R.id.VlblQVProblem2);
            //txtQVProblem2 = (EditText) findViewById(R.id.txtQVProblem2);
            // txtQVProblem3 = (EditText) findViewById(R.id.txtQVProblem3);
            //txtQVProblem4 = (EditText) findViewById(R.id.txtQVProblem4);
            // txtQVProblem5 = (EditText) findViewById(R.id.txtQVProblem5);
            // secQVSolve1 = (LinearLayout) findViewById(R.id.secQVSolve1);
            VlblQVSolve1 = (TextView) findViewById(R.id.VlblQVSolve1);
            //txtQVSolve1 = (EditText) findViewById(R.id.txtQVSolve1);
            secQVSolve2 = (LinearLayout) findViewById(R.id.secQVSolve2);
            // VlblQVSolve2 = (TextView) findViewById(R.id.VlblQVSolve2);
            //txtQVSolve2 = (EditText) findViewById(R.id.txtQVSolve2);
            //txtQVSolve3 = (EditText) findViewById(R.id.txtQVSolve3);
            // txtQVSolve4 = (EditText) findViewById(R.id.txtQVSolve4);
            // txtQVSolve5 = (EditText) findViewById(R.id.txtQVSolve5);
            btnVdate = (ImageButton) findViewById(R.id.btnVdate);
            // PVisitStatus();
            btnVdate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btnVdate";
                    showDialog(DATE_DIALOG);
                }
            });
            Button cmdSave = (Button) findViewById(R.id.cmdSave);
            cmdSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    DataSave();
                }
            });
        } catch (Exception e) {
            Connection.MessageBox(EPISESSIONVisit.this, e.getMessage());
            return;
        }
    }

    private void DataSave() {
        try {

            String DV = "";
            if (dtpVdate.getText().toString().length() == 0 & secVdate.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Required field:পরিদর্শনের তারিখ .");
                dtpVdate.requestFocus();
                return;
            } /*else if (txtQVHA.getText().toString().length() == 0 & secQVHA.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Required field:স্বাস্থ্য সহকারী .");
                txtQVHA.requestFocus();
                return;
            } else if (txtQVFWA.getText().toString().length() == 0 & secQVFWA.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Required field:পরিবার কল্যাণ সহকারী .");
                txtQVFWA.requestFocus();
                return;
            } else if (txtQVN.getText().toString().length() == 0 & secQVN.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Required field:ভ্যাক সিনেটর.");
                txtQVN.requestFocus();
                return;
            } else if (txtQVOth.getText().toString().length() == 0 & secQVOth.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Required field:অন্যান্য.");
                txtQVOth.requestFocus();
                return;
            } else if (!rdoQVChReg1.isChecked() & !rdoQVChReg2.isChecked() & secQVChReg.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVChReg.");
                rdoQVChReg1.requestFocus();
                return;
            } else if (!rdoQVWReg1.isChecked() & !rdoQVWReg2.isChecked() & secQVWReg.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVWReg.");
                rdoQVWReg1.requestFocus();
                return;
            } else if (!rdoQVChCard1.isChecked() & !rdoQVChCard2.isChecked() & secQVChCard.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVChCard.");
                rdoQVChCard1.requestFocus();
                return;
            } else if (!rdoQVWCard1.isChecked() & !rdoQVWCard2.isChecked() & secQVWCard.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVWCard.");
                rdoQVWCard1.requestFocus();
                return;
            } else if (!rdoQVTalBook1.isChecked() & !rdoQVTalBook2.isChecked() & secQVTalBook.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVTalBook.");
                rdoQVTalBook1.requestFocus();
                return;
            } else if (!rdoQVFIBook1.isChecked() & !rdoQVFIBook2.isChecked() & secQVFIBook.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVFIBook.");
                rdoQVFIBook1.requestFocus();
                return;
            } else if (!rdoQVVac1.isChecked() & !rdoQVVac2.isChecked() & secQVVac.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVVac.");
                rdoQVVac1.requestFocus();
                return;
            } else if (!rdoQVASerice1.isChecked() & !rdoQVASerice2.isChecked() & secQVASerice.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVASerice.");
                rdoQVASerice1.requestFocus();
                return;
            } else if (!rdoQVMSerice1.isChecked() & !rdoQVMSerice2.isChecked() & secQVMSerice.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVMSerice.");
                rdoQVMSerice1.requestFocus();
                return;
            } else if (!rdoQVSBox1.isChecked() & !rdoQVSBox2.isChecked() & secQVSBox.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVSBox.");
                rdoQVSBox1.requestFocus();
                return;
            } else if (!rdoQVVatARed1.isChecked() & !rdoQVVatARed2.isChecked() & secQVVatARed.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVVatARed.");
                rdoQVVatARed1.requestFocus();
                return;
            } else if (!rdoQVVatABlue1.isChecked() & !rdoQVVatABlue2.isChecked() & secQVVatABlue.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVVatABlue.");
                rdoQVVatABlue1.requestFocus();
                return;
            } else if (!rdoQVIICPac1.isChecked() & !rdoQVIICPac2.isChecked() & secQVIICPac.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVIICPac.");
                rdoQVIICPac1.requestFocus();
                return;
            } else if (!rdoQVBCG1.isChecked() & !rdoQVBCG2.isChecked() & secQVBCG.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVBCG.");
                rdoQVBCG1.requestFocus();
                return;
            } else if (!rdoQVPenta1.isChecked() & !rdoQVPenta2.isChecked() & secQVPenta.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVPenta.");
                rdoQVPenta1.requestFocus();
                return;
            } else if (!rdoQVPolio1.isChecked() & !rdoQVPolio2.isChecked() & secQVPolio.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVPolio.");
                rdoQVPolio1.requestFocus();
                return;
            } else if (!rdoQVPcv1.isChecked() & !rdoQVPcv2.isChecked() & secQVPcv.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVPcv.");
                rdoQVPcv1.requestFocus();
                return;
            } else if (!rdoQVIPV1.isChecked() & !rdoQVIPV2.isChecked() & secQVIPV.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVIPV.");
                rdoQVIPV1.requestFocus();
                return;
            } else if (!rdoQVMR1.isChecked() & !rdoQVMR2.isChecked() & secQVMR.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVMR.");
                rdoQVMR1.requestFocus();
                return;
            } else if (!rdoQVTT1.isChecked() & !rdoQVTT2.isChecked() & secQVTT.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVTT.");
                rdoQVTT1.requestFocus();
                return;
            } else if (!rdoQVSbcg1.isChecked() & !rdoQVSbcg2.isChecked() & !rdoQVSbcg3.isChecked() & secQVSbcg.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVSbcg.");
                rdoQVSbcg1.requestFocus();
                return;
            } else if (!rdoQVSPenta1.isChecked() & !rdoQVSPenta2.isChecked() & !rdoQVSPenta3.isChecked() & secQVSPenta.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVSPenta.");
                rdoQVSPenta1.requestFocus();
                return;
            } else if (!rdoQVSPolio1.isChecked() & !rdoQVSPolio2.isChecked() & !rdoQVSPolio3.isChecked() & secQVSPolio.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVSPolio.");
                rdoQVSPolio1.requestFocus();
                return;
            } else if (!rdoQVSPcv1.isChecked() & !rdoQVSPcv2.isChecked() & !rdoQVSPcv3.isChecked() & secQVSPcv.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVSPcv.");
                rdoQVSPcv1.requestFocus();
                return;
            } else if (!rdoQVSIPV1.isChecked() & !rdoQVSIPV2.isChecked() & !rdoQVSIPV3.isChecked() & secQVSIPV.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVSIPV.");
                rdoQVSIPV1.requestFocus();
                return;
            } else if (!rdoQVSMR1.isChecked() & !rdoQVSMR2.isChecked() & !rdoQVSMR3.isChecked() & secQVSMR.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVSMR.");
                rdoQVSMR1.requestFocus();
                return;
            } else if (!rdoQVSTT1.isChecked() & !rdoQVSTT2.isChecked() & !rdoQVSTT3.isChecked() & secQVSTT.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVSTT.");
                rdoQVSTT1.requestFocus();
                return;
            } else if (!rdoQVNToubcg1.isChecked() & !rdoQVNToubcg2.isChecked() & secQVNToubcg.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVNToubcg.");
                rdoQVNToubcg1.requestFocus();
                return;
            } else if (!rdoQVNToupant1.isChecked() & !rdoQVNToupant2.isChecked() & secQVNToupant.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVNToupant.");
                rdoQVNToupant1.requestFocus();
                return;
            } else if (!rdoQVNTouPolio1.isChecked() & !rdoQVNTouPolio2.isChecked() & secQVNTouPolio.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVNTouPolio.");
                rdoQVNTouPolio1.requestFocus();
                return;
            } else if (!rdoQVNToupcv1.isChecked() & !rdoQVNToupcv2.isChecked() & secQVNToupcv.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVNToupcv.");
                rdoQVNToupcv1.requestFocus();
                return;
            } else if (!rdoQVNTouIPV1.isChecked() & !rdoQVNTouIPV2.isChecked() & secQVNTouIPV.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVNTouIPV.");
                rdoQVNTouIPV1.requestFocus();
                return;
            } else if (!rdoQVNTouMR1.isChecked() & !rdoQVNTouMR2.isChecked() & secQVNTouMR.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVNTouMR.");
                rdoQVNTouMR1.requestFocus();
                return;
            } else if (!rdoQVNTouTT1.isChecked() & !rdoQVNTouTT2.isChecked() & secQVNTouTT.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVNTouTT.");
                rdoQVNTouTT1.requestFocus();
                return;
            } else if (!rdoQVRootbcg1.isChecked() & !rdoQVRootbcg2.isChecked() & secQVRootbcg.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVRootbcg.");
                rdoQVRootbcg1.requestFocus();
                return;
            } else if (spnQVNotwhy.getSelectedItemPosition() == 0 & secQVNotwhy.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Required field:না হলে কেন?.");
                spnQVNotwhy.requestFocus();
                return;
            } else if (!rdoQVRootPenta1.isChecked() & !rdoQVRootPenta2.isChecked() & secQVRootPenta.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVRootPenta.");
                rdoQVRootPenta1.requestFocus();
                return;
            } else if (!rdoQVRootMR1.isChecked() & !rdoQVRootMR2.isChecked() & secQVRootMR.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVRootMR.");
                rdoQVRootMR1.requestFocus();
                return;
            } else if (!rdoQVRootTT1.isChecked() & !rdoQVRootTT2.isChecked() & secQVRootTT.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVRootTT.");
                rdoQVRootTT1.requestFocus();
                return;
            } else if (!rdoQVSRemoved1.isChecked() & !rdoQVSRemoved2.isChecked() & secQVSRemoved.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVSRemoved.");
                rdoQVSRemoved1.requestFocus();
                return;
            } else if (!rdoQVFormbcg1.isChecked() & !rdoQVFormbcg2.isChecked() & secQVFormbcg.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVFormbcg.");
                rdoQVFormbcg1.requestFocus();
                return;
            } else if (!rdoQVFormpenta1.isChecked() & !rdoQVFormpenta2.isChecked() & secQVFormpenta.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVFormpenta.");
                rdoQVFormpenta1.requestFocus();
                return;
            } else if (!rdoQVFormPolio1.isChecked() & !rdoQVFormPolio2.isChecked() & secQVFormPolio.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVFormPolio.");
                rdoQVFormPolio1.requestFocus();
                return;
            } else if (!rdoQVFormpcv1.isChecked() & !rdoQVFormpcv2.isChecked() & secQVFormpcv.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVFormpcv.");
                rdoQVFormpcv1.requestFocus();
                return;
            } else if (!rdoQVFormipv1.isChecked() & !rdoQVFormipv2.isChecked() & secQVFormipv.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVFormipv.");
                rdoQVFormipv1.requestFocus();
                return;
            } else if (!rdoQVFormmr1.isChecked() & !rdoQVFormmr2.isChecked() & secQVFormmr.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVFormmr.");
                rdoQVFormmr1.requestFocus();
                return;
            } else if (!rdoQVFormtt1.isChecked() & !rdoQVFormtt2.isChecked() & secQVFormtt.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVFormtt.");
                rdoQVFormtt1.requestFocus();
                return;
            } else if (!rdoQVCardregBook1.isChecked() & !rdoQVCardregBook2.isChecked() & secQVCardregBook.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVCardregBook.");
                rdoQVCardregBook1.requestFocus();
                return;
            } else if (!rdoQVCardVac1.isChecked() & !rdoQVCardVac2.isChecked() & secQVCardVac.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVCardVac.");
                rdoQVCardVac1.requestFocus();
                return;
            } else if (!rdoQVTTresearched1.isChecked() & !rdoQVTTresearched2.isChecked() & secQVTTresearched.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVTTresearched .");
                rdoQVTTresearched1.requestFocus();
                return;
            } else if (!rdoQVProtectors1.isChecked() & !rdoQVProtectors2.isChecked() & secQVProtectors.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVProtectors.");
                rdoQVProtectors1.requestFocus();
                return;
            } else if (!rdoQVDateOfVac1.isChecked() & !rdoQVDateOfVac2.isChecked() & secQVDateOfVac.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVDateOfVac.");
                rdoQVDateOfVac1.requestFocus();
                return;
            } else if (!rdoQVCard1.isChecked() & !rdoQVCard2.isChecked() & secQVCard.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Select anyone options from QVCard.");
                rdoQVCard1.requestFocus();
                return;
            }*/ /*else if (txtQVAFP.getText().toString().length() == 0 & secQVAFP.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Required field:এএফপি.");
                txtQVAFP.requestFocus();
                return;
            } else if (txtQVMeasles.getText().toString().length() == 0 & secQVMeasles.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Required field:হাম.");
                txtQVMeasles.requestFocus();
                return;
            } else if (txtQVNewborntetanus.getText().toString().length() == 0 & secQVNewborntetanus.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Required field:নবজাতকের ধনুষ্টংকার .");
                txtQVNewborntetanus.requestFocus();
                return;
            } else if (txtQVOther.getText().toString().length() == 0 & secQVOther.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Required field:অন্যান্য .");
                txtQVOther.requestFocus();
                return;
            } else if (txtQVTodySession.getText().toString().length() == 0 & secQVTodySession.isShown()) {
                Connection.MessageBox(EPISESSIONVisit.this, "Required field:আজকের টিকাদান সেশনে কতজন শিশু হামের টিকা পেয়েছেঃ .");
                txtQVTodySession.requestFocus();
                return;
            }*/


            String SQL = "";

            if (!C.Existence("Select subBlockId,schedulerId,providerId,StartTime,EndTime,UserId,EnDt,Upload from " + TableName + "  Where subBlockId='" + Global.Left(spnEpiSubBlock.getSelectedItem().toString(), 2) + "' and schedulerId='" + Global.Left(spnEPICenterName.getSelectedItem().toString(), 2) + "' and providerId='" + Global.Left(spnProvider.getSelectedItem().toString(), 5) + "' and VDate='" + Global.DateConvertYMD(dtpVdate.getText().toString()) + "'")) {
                SQL = "Insert into " + TableName + "(subBlockId,schedulerId,providerId,VDate,StartTime,EndTime,UserId,EnDt,Upload)Values('" + Global.Left(spnEpiSubBlock.getSelectedItem().toString(), 2) + "','" + Global.Left(spnEPICenterName.getSelectedItem().toString(), 2) + "','" + Global.Left(spnProvider.getSelectedItem().toString(), 5) + "','" + Global.DateConvertYMD(dtpVdate.getText().toString()) + "','" + StartTime + "','" + g.CurrentTime24() + "','" + g.getUserID() + "','" + Global.DateTimeNowYMDHMS() + "','2')";
                C.Save(SQL);
            }

            SQL = "Update " + TableName + " Set Upload='2',";
            SQL += "VDate = '" + Global.DateConvertYMD(dtpVdate.getText().toString()) + "',";
            SQL += "QVHA = '" + txtQVHA.getText().toString() + "',";
            SQL += "QVFWA = '" + txtQVFWA.getText().toString() + "',";
            SQL += "QVN = '" + txtQVN.getText().toString() + "',";
            SQL += "QVOth = '" + txtQVOth.getText().toString() + "',";
            RadioButton rbQVChReg = (RadioButton) findViewById(rdogrpQVChReg.getCheckedRadioButtonId());
            SQL += "QVChReg = '" + (rbQVChReg == null ? "" : (Global.Left(rbQVChReg.getText().toString(), 1))) + "',";
            RadioButton rbQVWReg = (RadioButton) findViewById(rdogrpQVWReg.getCheckedRadioButtonId());
            SQL += "QVWReg = '" + (rbQVWReg == null ? "" : (Global.Left(rbQVWReg.getText().toString(), 1))) + "',";
            RadioButton rbQVChCard = (RadioButton) findViewById(rdogrpQVChCard.getCheckedRadioButtonId());
            SQL += "QVChCard = '" + (rbQVChCard == null ? "" : (Global.Left(rbQVChCard.getText().toString(), 1))) + "',";
            RadioButton rbQVWCard = (RadioButton) findViewById(rdogrpQVWCard.getCheckedRadioButtonId());
            SQL += "QVWCard = '" + (rbQVWCard == null ? "" : (Global.Left(rbQVWCard.getText().toString(), 1))) + "',";
            RadioButton rbQVTalBook = (RadioButton) findViewById(rdogrpQVTalBook.getCheckedRadioButtonId());
            SQL += "QVTalBook = '" + (rbQVTalBook == null ? "" : (Global.Left(rbQVTalBook.getText().toString(), 1))) + "',";
            RadioButton rbQVFIBook = (RadioButton) findViewById(rdogrpQVFIBook.getCheckedRadioButtonId());
            SQL += "QVFIBook = '" + (rbQVFIBook == null ? "" : (Global.Left(rbQVFIBook.getText().toString(), 1))) + "',";
            RadioButton rbQVVac = (RadioButton) findViewById(rdogrpQVVac.getCheckedRadioButtonId());
            SQL += "QVVac = '" + (rbQVVac == null ? "" : (Global.Left(rbQVVac.getText().toString(), 1))) + "',";
            RadioButton rbQVASerice = (RadioButton) findViewById(rdogrpQVASerice.getCheckedRadioButtonId());
            SQL += "QVASerice = '" + (rbQVASerice == null ? "" : (Global.Left(rbQVASerice.getText().toString(), 1))) + "',";
            RadioButton rbQVMSerice = (RadioButton) findViewById(rdogrpQVMSerice.getCheckedRadioButtonId());
            SQL += "QVMSerice = '" + (rbQVMSerice == null ? "" : (Global.Left(rbQVMSerice.getText().toString(), 1))) + "',";
            RadioButton rbQVSBox = (RadioButton) findViewById(rdogrpQVSBox.getCheckedRadioButtonId());
            SQL += "QVSBox = '" + (rbQVSBox == null ? "" : (Global.Left(rbQVSBox.getText().toString(), 1))) + "',";
            RadioButton rbQVVatARed = (RadioButton) findViewById(rdogrpQVVatARed.getCheckedRadioButtonId());
            SQL += "QVVatARed = '" + (rbQVVatARed == null ? "" : (Global.Left(rbQVVatARed.getText().toString(), 1))) + "',";
            RadioButton rbQVVatABlue = (RadioButton) findViewById(rdogrpQVVatABlue.getCheckedRadioButtonId());
            SQL += "QVVatABlue = '" + (rbQVVatABlue == null ? "" : (Global.Left(rbQVVatABlue.getText().toString(), 1))) + "',";
            RadioButton rbQVIICPac = (RadioButton) findViewById(rdogrpQVIICPac.getCheckedRadioButtonId());
            SQL += "QVIICPac = '" + (rbQVIICPac == null ? "" : (Global.Left(rbQVIICPac.getText().toString(), 1))) + "',";
            RadioButton rbQVBCG = (RadioButton) findViewById(rdogrpQVBCG.getCheckedRadioButtonId());
            SQL += "QVBCG = '" + (rbQVBCG == null ? "" : (Global.Left(rbQVBCG.getText().toString(), 1))) + "',";
            RadioButton rbQVPenta = (RadioButton) findViewById(rdogrpQVPenta.getCheckedRadioButtonId());
            SQL += "QVPenta = '" + (rbQVPenta == null ? "" : (Global.Left(rbQVPenta.getText().toString(), 1))) + "',";
            RadioButton rbQVPolio = (RadioButton) findViewById(rdogrpQVPolio.getCheckedRadioButtonId());
            SQL += "QVPolio = '" + (rbQVPolio == null ? "" : (Global.Left(rbQVPolio.getText().toString(), 1))) + "',";
            RadioButton rbQVPcv = (RadioButton) findViewById(rdogrpQVPcv.getCheckedRadioButtonId());
            SQL += "QVPcv = '" + (rbQVPcv == null ? "" : (Global.Left(rbQVPcv.getText().toString(), 1))) + "',";
            RadioButton rbQVIPV = (RadioButton) findViewById(rdogrpQVIPV.getCheckedRadioButtonId());
            SQL += "QVIPV = '" + (rbQVIPV == null ? "" : (Global.Left(rbQVIPV.getText().toString(), 1))) + "',";
            RadioButton rbQVMR = (RadioButton) findViewById(rdogrpQVMR.getCheckedRadioButtonId());
            SQL += "QVMR = '" + (rbQVMR == null ? "" : (Global.Left(rbQVMR.getText().toString(), 1))) + "',";
            RadioButton rbQVTT = (RadioButton) findViewById(rdogrpQVTT.getCheckedRadioButtonId());
            SQL += "QVTT = '" + (rbQVTT == null ? "" : (Global.Left(rbQVTT.getText().toString(), 1))) + "',";
            RadioButton rbQVSbcg = (RadioButton) findViewById(rdogrpQVSbcg.getCheckedRadioButtonId());
            SQL += "QVSbcg = '" + (rbQVSbcg == null ? "" : (Global.Left(rbQVSbcg.getText().toString(), 1))) + "',";
            RadioButton rbQVSPenta = (RadioButton) findViewById(rdogrpQVSPenta.getCheckedRadioButtonId());
            SQL += "QVSPenta = '" + (rbQVSPenta == null ? "" : (Global.Left(rbQVSPenta.getText().toString(), 1))) + "',";
            RadioButton rbQVSPolio = (RadioButton) findViewById(rdogrpQVSPolio.getCheckedRadioButtonId());
            SQL += "QVSPolio = '" + (rbQVSPolio == null ? "" : (Global.Left(rbQVSPolio.getText().toString(), 1))) + "',";
            RadioButton rbQVSPcv = (RadioButton) findViewById(rdogrpQVSPcv.getCheckedRadioButtonId());
            SQL += "QVSPcv = '" + (rbQVSPcv == null ? "" : (Global.Left(rbQVSPcv.getText().toString(), 1))) + "',";
            RadioButton rbQVSIPV = (RadioButton) findViewById(rdogrpQVSIPV.getCheckedRadioButtonId());
            SQL += "QVSIPV = '" + (rbQVSIPV == null ? "" : (Global.Left(rbQVSIPV.getText().toString(), 1))) + "',";
            RadioButton rbQVSMR = (RadioButton) findViewById(rdogrpQVSMR.getCheckedRadioButtonId());
            SQL += "QVSMR = '" + (rbQVSMR == null ? "" : (Global.Left(rbQVSMR.getText().toString(), 1))) + "',";
            RadioButton rbQVSTT = (RadioButton) findViewById(rdogrpQVSTT.getCheckedRadioButtonId());
            SQL += "QVSTT = '" + (rbQVSTT == null ? "" : (Global.Left(rbQVSTT.getText().toString(), 1))) + "',";
            RadioButton rbQVNToubcg = (RadioButton) findViewById(rdogrpQVNToubcg.getCheckedRadioButtonId());
            SQL += "QVNToubcg = '" + (rbQVNToubcg == null ? "" : (Global.Left(rbQVNToubcg.getText().toString(), 1))) + "',";
            RadioButton rbQVNToupant = (RadioButton) findViewById(rdogrpQVNToupant.getCheckedRadioButtonId());
            SQL += "QVNToupant = '" + (rbQVNToupant == null ? "" : (Global.Left(rbQVNToupant.getText().toString(), 1))) + "',";
            RadioButton rbQVNTouPolio = (RadioButton) findViewById(rdogrpQVNTouPolio.getCheckedRadioButtonId());
            SQL += "QVNTouPolio = '" + (rbQVNTouPolio == null ? "" : (Global.Left(rbQVNTouPolio.getText().toString(), 1))) + "',";
            RadioButton rbQVNToupcv = (RadioButton) findViewById(rdogrpQVNToupcv.getCheckedRadioButtonId());
            SQL += "QVNToupcv = '" + (rbQVNToupcv == null ? "" : (Global.Left(rbQVNToupcv.getText().toString(), 1))) + "',";
            RadioButton rbQVNTouIPV = (RadioButton) findViewById(rdogrpQVNTouIPV.getCheckedRadioButtonId());
            SQL += "QVNTouIPV = '" + (rbQVNTouIPV == null ? "" : (Global.Left(rbQVNTouIPV.getText().toString(), 1))) + "',";
            RadioButton rbQVNTouMR = (RadioButton) findViewById(rdogrpQVNTouMR.getCheckedRadioButtonId());
            SQL += "QVNTouMR = '" + (rbQVNTouMR == null ? "" : (Global.Left(rbQVNTouMR.getText().toString(), 1))) + "',";
            RadioButton rbQVNTouTT = (RadioButton) findViewById(rdogrpQVNTouTT.getCheckedRadioButtonId());
            SQL += "QVNTouTT = '" + (rbQVNTouTT == null ? "" : (Global.Left(rbQVNTouTT.getText().toString(), 1))) + "',";
            RadioButton rbQVRootbcg = (RadioButton) findViewById(rdogrpQVRootbcg.getCheckedRadioButtonId());
            SQL += "QVRootbcg = '" + (rbQVRootbcg == null ? "" : (Global.Left(rbQVRootbcg.getText().toString(), 1))) + "',";
            SQL += "QVNotwhy = '" + (spnQVNotwhy.getSelectedItemPosition() == 0 ? "" : Global.Left(spnQVNotwhy.getSelectedItem().toString(), 2)) + "',";
            RadioButton rbQVRootPenta = (RadioButton) findViewById(rdogrpQVRootPenta.getCheckedRadioButtonId());
            SQL += "QVRootPenta = '" + (rbQVRootPenta == null ? "" : (Global.Left(rbQVRootPenta.getText().toString(), 1))) + "',";
            RadioButton rbQVRootMR = (RadioButton) findViewById(rdogrpQVRootMR.getCheckedRadioButtonId());
            SQL += "QVRootMR = '" + (rbQVRootMR == null ? "" : (Global.Left(rbQVRootMR.getText().toString(), 1))) + "',";
            RadioButton rbQVRootTT = (RadioButton) findViewById(rdogrpQVRootTT.getCheckedRadioButtonId());
            SQL += "QVRootTT = '" + (rbQVRootTT == null ? "" : (Global.Left(rbQVRootTT.getText().toString(), 1))) + "',";
            RadioButton rbQVSRemoved = (RadioButton) findViewById(rdogrpQVSRemoved.getCheckedRadioButtonId());
            SQL += "QVSRemoved = '" + (rbQVSRemoved == null ? "" : (Global.Left(rbQVSRemoved.getText().toString(), 1))) + "',";
            RadioButton rbQVFormbcg = (RadioButton) findViewById(rdogrpQVFormbcg.getCheckedRadioButtonId());
            SQL += "QVFormbcg = '" + (rbQVFormbcg == null ? "" : (Global.Left(rbQVFormbcg.getText().toString(), 1))) + "',";
            RadioButton rbQVFormpenta = (RadioButton) findViewById(rdogrpQVFormpenta.getCheckedRadioButtonId());
            SQL += "QVFormpenta = '" + (rbQVFormpenta == null ? "" : (Global.Left(rbQVFormpenta.getText().toString(), 1))) + "',";
            RadioButton rbQVFormPolio = (RadioButton) findViewById(rdogrpQVFormPolio.getCheckedRadioButtonId());
            SQL += "QVFormPolio = '" + (rbQVFormPolio == null ? "" : (Global.Left(rbQVFormPolio.getText().toString(), 1))) + "',";
            RadioButton rbQVFormpcv = (RadioButton) findViewById(rdogrpQVFormpcv.getCheckedRadioButtonId());
            SQL += "QVFormpcv = '" + (rbQVFormpcv == null ? "" : (Global.Left(rbQVFormpcv.getText().toString(), 1))) + "',";
            RadioButton rbQVFormipv = (RadioButton) findViewById(rdogrpQVFormipv.getCheckedRadioButtonId());
            SQL += "QVFormipv = '" + (rbQVFormipv == null ? "" : (Global.Left(rbQVFormipv.getText().toString(), 1))) + "',";
            RadioButton rbQVFormmr = (RadioButton) findViewById(rdogrpQVFormmr.getCheckedRadioButtonId());
            SQL += "QVFormmr = '" + (rbQVFormmr == null ? "" : (Global.Left(rbQVFormmr.getText().toString(), 1))) + "',";
            RadioButton rbQVFormtt = (RadioButton) findViewById(rdogrpQVFormtt.getCheckedRadioButtonId());
            SQL += "QVFormtt = '" + (rbQVFormtt == null ? "" : (Global.Left(rbQVFormtt.getText().toString(), 1))) + "',";
            RadioButton rbQVCardregBook = (RadioButton) findViewById(rdogrpQVCardregBook.getCheckedRadioButtonId());
            SQL += "QVCardregBook = '" + (rbQVCardregBook == null ? "" : (Global.Left(rbQVCardregBook.getText().toString(), 1))) + "',";
            RadioButton rbQVCardVac = (RadioButton) findViewById(rdogrpQVCardVac.getCheckedRadioButtonId());
            SQL += "QVCardVac = '" + (rbQVCardVac == null ? "" : (Global.Left(rbQVCardVac.getText().toString(), 1))) + "',";
            RadioButton rbQVTTresearched = (RadioButton) findViewById(rdogrpQVTTresearched.getCheckedRadioButtonId());
            SQL += "QVTTresearched  = '" + (rbQVTTresearched == null ? "" : (Global.Left(rbQVTTresearched.getText().toString(), 1))) + "',";
            RadioButton rbQVProtectors = (RadioButton) findViewById(rdogrpQVProtectors.getCheckedRadioButtonId());
            SQL += "QVProtectors = '" + (rbQVProtectors == null ? "" : (Global.Left(rbQVProtectors.getText().toString(), 1))) + "',";
            RadioButton rbQVDateOfVac = (RadioButton) findViewById(rdogrpQVDateOfVac.getCheckedRadioButtonId());
            SQL += "QVDateOfVac = '" + (rbQVDateOfVac == null ? "" : (Global.Left(rbQVDateOfVac.getText().toString(), 1))) + "',";
            RadioButton rbQVCard = (RadioButton) findViewById(rdogrpQVCard.getCheckedRadioButtonId());
            SQL += "QVCard = '" + (rbQVCard == null ? "" : (Global.Left(rbQVCard.getText().toString(), 1))) + "',";
            SQL += "QVAFP = '" + txtQVAFP.getText().toString() + "',";
            SQL += "QVMeasles = '" + txtQVMeasles.getText().toString() + "',";
            SQL += "QVNewborntetanus = '" + txtQVNewborntetanus.getText().toString() + "',";
            SQL += "QVOther = '" + txtQVOther.getText().toString() + "',";
            SQL += "QVTodySession = '" + txtQVTodySession.getText().toString() + "',";
            SQL += "QVProblem1 = '" + (spnQVProblem2.getSelectedItemPosition() == 0 ? "" : Global.Left(spnQVProblem2.getSelectedItem().toString(), 2)) + "',";
            SQL += "QVProblem2 = '" + (spnQVProblem3.getSelectedItemPosition() == 0 ? "" : Global.Left(spnQVProblem3.getSelectedItem().toString(), 2)) + "',";
            SQL += "QVProblem3 = '" + (spnQVProblem4.getSelectedItemPosition() == 0 ? "" : Global.Left(spnQVProblem4.getSelectedItem().toString(), 2)) + "',";
            SQL += "QVProblem4 = '" + (spnQVProblem5.getSelectedItemPosition() == 0 ? "" : Global.Left(spnQVProblem5.getSelectedItem().toString(), 2)) + "',";
            SQL += "QVProblem5 = '" + (spnQVProblem6.getSelectedItemPosition() == 0 ? "" : Global.Left(spnQVProblem6.getSelectedItem().toString(), 2)) + "',";
            // SQL += "QVProblem2 = '" + txtQVProblem2.getText().toString() + "',";
           /* SQL += "QVProblem3 = '" + txtQVProblem3.getText().toString() + "',";
            SQL += "QVProblem4 = '" + txtQVProblem4.getText().toString() + "',";
            SQL += "QVProblem5 = '" + txtQVProblem5.getText().toString() + "',";
       */
            SQL += "QVSolve1 = '" + (spnQVSolve2.getSelectedItemPosition() == 0 ? "" : Global.Left(spnQVSolve2.getSelectedItem().toString(), 2)) + "',";
            SQL += "QVSolve2 = '" + (spnQVSolve3.getSelectedItemPosition() == 0 ? "" : Global.Left(spnQVSolve3.getSelectedItem().toString(), 2)) + "',";
            SQL += "QVSolve3 = '" + (spnQVSolve4.getSelectedItemPosition() == 0 ? "" : Global.Left(spnQVSolve4.getSelectedItem().toString(), 2)) + "',";
            SQL += "QVSolve4 = '" + (spnQVSolve5.getSelectedItemPosition() == 0 ? "" : Global.Left(spnQVSolve5.getSelectedItem().toString(), 2)) + "',";
            SQL += "QVSolve5 = '" + (spnQVSolve6.getSelectedItemPosition() == 0 ? "" : Global.Left(spnQVSolve6.getSelectedItem().toString(), 2)) + "'";
            //SQL += "QVSolve2 = '" + txtQVSolve2.getText().toString() + "',";
           /* SQL += "QVSolve3 = '" + txtQVSolve3.getText().toString() + "',";
            SQL += "QVSolve4 = '" + txtQVSolve4.getText().toString() + "',";
            SQL += "QVSolve5 = '" + txtQVSolve5.getText().toString() + "'";
*/
            SQL += " Where subBlockId='" + Global.Left(spnEpiSubBlock.getSelectedItem().toString(), 2) + "' and schedulerId='" + Global.Left(spnEPICenterName.getSelectedItem().toString(), 2) + "' and providerId='" + Global.Left(spnProvider.getSelectedItem().toString(), 5) + "' and VDate='" + Global.DateConvertYMD(dtpVdate.getText().toString()) + "'";
            C.Save(SQL);
            Connection.MessageBox(EPISESSIONVisit.this, "Saved Successfully");
            finish();
            Intent f = new Intent(getApplicationContext(), VillageList.class);
            startActivity(f);
        } catch (Exception e) {
            Connection.MessageBox(EPISESSIONVisit.this, e.getMessage());
            return;
        }
    }

    public void PVisitStatus() {
        GridView g1 = (GridView) findViewById(R.id.gridPVisit);
        g1.setAdapter(new PVisit(this));
        g1.setNumColumns(6);
    }

    public class PVisit extends BaseAdapter {
        private Context mContext;
        String[][] vcode;
        Integer totalRec;

        public PVisit(Context c) {
            mContext = c;
        }

        public int getCount() {
            return Integer.parseInt(C.ReturnSingleValue("Select count(*)total from EPISessionVisit Where providerId='" + Global.Left(spnProvider.getSelectedItem().toString(), 5) + "' and subBlockId='" + Global.Left(spnEpiSubBlock.getSelectedItem().toString(), 2) + "'"));
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
                //spnVCenterName.setAdapter(C.getArrayAdapter("select substr('0' ||schedulerId, -2, 2)||'-'||providerId||'-'||centerName from epiScheduler where subBlockId='"+Global.Left(spnEpiSubBlock.getSelectedItem().toString(),2)+"' and providerId='"+Global.Left(spnProvider.getSelectedItem().toString(),5)+"' and  strftime('%d/%m/%Y', date(scheduleDate))='"+dtpVdate.getText()+"'"));

                String SQL = "Select strftime('%d/%m/%Y', date(VDate)) as visitDate  from EPISessionVisit Where providerId='" + Global.Left(spnProvider.getSelectedItem().toString(), 5) + "' and subBlockId='" + Global.Left(spnEpiSubBlock.getSelectedItem().toString(), 2) + "' order by VDate  desc";//date(imudate) asc

                try {
                    Cursor cur = C.ReadData(SQL);
                    cur.moveToFirst();

                    totalRec = cur.getCount();
                    vcode = new String[2][totalRec];
                    int i = 0;
                    while (!cur.isAfterLast()) {
                        vcode[0][i] = "পরিদর্শন " + String.valueOf(i + 1);//String.valueOf(cur.getString(cur.getColumnIndex("imucode")));
                        vcode[1][i] = cur.getString(cur.getColumnIndex("visitDate"));
                        // vcode[2][i]= cur.getString(cur.getColumnIndex("imucard"));
                        // vcode[3][i]= String.valueOf(cur.getString(cur.getColumnIndex("imucode")));

                        i += 1;
                        cur.moveToNext();
                    }
                    cur.close();

                    Button tv = (Button) MyView.findViewById(R.id.image_name);
                    tv.setTextSize(14);
                    tv.setText(vcode[0][position] + "\n" + vcode[1][position]);
                    final Integer p = position;

                    tv.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog.Builder adb = new AlertDialog.Builder(EPISESSIONVisit.this);


                            adb.setTitle("Close");
                            adb.setMessage("আপনি কি পরিদর্শন বাতিল করতে  চান[হ্যাঁ/না]?");
                            adb.setNegativeButton("না", null);
                            adb.setPositiveButton("হ্যাঁ", new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String visitDate = String.valueOf(vcode[1][position]);
                                    if ((Global.DateDifferenceDays(Global.DateNowDMY(), Global.DateConvertDMY(visitDate.toString())) > 3)) {
                                        Connection.MessageBox(EPISESSIONVisit.this, "পরিদর্শনের  তথ্য ৩ দিন পর বাতিল করা প্রযোজ্য না");
                                        return;
                                    } else {
                                        String Item = String.valueOf(vcode[0][position]);
                                        String visit = String.valueOf(vcode[1][position]);
                                        String dwl = "DELETE FROM EPISessionVisit Where subBlockId='" + Global.Left(spnEpiSubBlock.getSelectedItem().toString(), 2) + "' and schedulerId='" + Global.Left(spnEPICenterName.getSelectedItem().toString(), 2) + "' and providerId='" + Global.Left(spnProvider.getSelectedItem().toString(), 5) + "' and strftime('%d/%m/%Y', date(VDate))='" + visit+ "'";
                                       // String dwl = "DELETE FROM EPISessionVisit WHERE healthId ='" + g.getGeneratedId() + "' and providerId='" + g.getProvCode() + "' and strftime('%d/%m/%Y', date(visitDate))='" + visit + "'";
                                        C.Save(dwl);


                                        PVisitStatus();


                                    }
                                    // ElcoVisitStsatus();
                                }
                            });
                            adb.show();
                            return true;


                        }
                    });
                    tv.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {

                            if (vcode[1][position] == vcode[1][position]) {

                                dtpVdate.setText(vcode[1][position]);
                                // SearchAdolescent();
                                // SearchAdolescent(g.getHealthID(),g.getProvCode(),g.getAVDate());
                                // DataRetrive(g.getGeneratedId(),vcode[1][position]);
                                // DataRetrive();
                                DataSearch(vcode[1][position]);


                            }
                            // g.setImuCode(vcode[1][position]);
                        }
                    });
                } catch (Exception ex) {
                    Connection.MessageBox(EPISESSIONVisit.this, ex.getMessage());
                }

            }
            return MyView;
        }

    }

    private void DataSearch(String VDate) {
        try {

            RadioButton rb;
            Cursor cur = C.ReadData("Select * from " + TableName + "  Where subBlockId='" + Global.Left(spnEpiSubBlock.getSelectedItem().toString(), 2) + "' and schedulerId='" + Global.Left(spnEPICenterName.getSelectedItem().toString(), 2) + "' and providerId='" + Global.Left(spnProvider.getSelectedItem().toString(), 5) + "' and VDate='" + Global.DateConvertYMD(VDate) + "'");
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                //txtIdno.setText(cur.getString(cur.getColumnIndex("Idno")));
                txtQVHA.setText(cur.getString(cur.getColumnIndex("QVHA")));
                txtQVFWA.setText(cur.getString(cur.getColumnIndex("QVFWA")));
                txtQVN.setText(cur.getString(cur.getColumnIndex("QVN")));
                txtQVOth.setText(cur.getString(cur.getColumnIndex("QVOth")));
                for (int i = 0; i < rdogrpQVChReg.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVChReg.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVChReg"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVWReg.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVWReg.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVWReg"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVChCard.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVChCard.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVChCard"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVWCard.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVWCard.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVWCard"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVTalBook.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVTalBook.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVTalBook"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVFIBook.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVFIBook.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVFIBook"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVVac.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVVac.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVVac"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVASerice.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVASerice.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVASerice"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVMSerice.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVMSerice.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVMSerice"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVSBox.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVSBox.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVSBox"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVVatARed.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVVatARed.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVVatARed"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVVatABlue.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVVatABlue.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVVatABlue"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVIICPac.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVIICPac.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVIICPac"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVBCG.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVBCG.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVBCG"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVPenta.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVPenta.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVPenta"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVPolio.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVPolio.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVPolio"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVPcv.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVPcv.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVPcv"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVIPV.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVIPV.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVIPV"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVMR.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVMR.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVMR"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVTT.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVTT.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVTT"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVSbcg.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVSbcg.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVSbcg"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVSPenta.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVSPenta.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVSPenta"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVSPolio.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVSPolio.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVSPolio"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVSPcv.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVSPcv.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVSPcv"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVSIPV.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVSIPV.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVSIPV"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVSMR.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVSMR.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVSMR"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVSTT.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVSTT.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVSTT"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVNToubcg.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVNToubcg.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVNToubcg"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVNToupant.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVNToupant.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVNToupant"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVNTouPolio.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVNTouPolio.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVNTouPolio"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVNToupcv.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVNToupcv.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVNToupcv"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVNTouIPV.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVNTouIPV.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVNTouIPV"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVNTouMR.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVNTouMR.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVNTouMR"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVNTouTT.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVNTouTT.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVNTouTT"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVRootbcg.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVRootbcg.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVRootbcg"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                spnQVNotwhy.setSelection(Global.SpinnerItemPosition(spnQVNotwhy, 2, cur.getString(cur.getColumnIndex("QVNotwhy"))));
                for (int i = 0; i < rdogrpQVRootPenta.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVRootPenta.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVRootPenta"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVRootMR.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVRootMR.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVRootMR"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVRootTT.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVRootTT.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVRootTT"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVSRemoved.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVSRemoved.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVSRemoved"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVFormbcg.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVFormbcg.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVFormbcg"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVFormpenta.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVFormpenta.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVFormpenta"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVFormPolio.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVFormPolio.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVFormPolio"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVFormpcv.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVFormpcv.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVFormpcv"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVFormipv.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVFormipv.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVFormipv"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVFormmr.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVFormmr.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVFormmr"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVFormtt.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVFormtt.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVFormtt"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVCardregBook.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVCardregBook.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVCardregBook"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVCardVac.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVCardVac.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVCardVac"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVTTresearched.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVTTresearched.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVTTresearched"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVProtectors.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVProtectors.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVProtectors"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVDateOfVac.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVDateOfVac.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVDateOfVac"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQVCard.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQVCard.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QVCard"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                txtQVAFP.setText(cur.getString(cur.getColumnIndex("QVAFP")));
                txtQVMeasles.setText(cur.getString(cur.getColumnIndex("QVMeasles")));
                txtQVNewborntetanus.setText(cur.getString(cur.getColumnIndex("QVNewborntetanus")));
                txtQVOther.setText(cur.getString(cur.getColumnIndex("QVOther")));
                txtQVTodySession.setText(cur.getString(cur.getColumnIndex("QVTodySession")));
                spnQVProblem2.setSelection(Global.SpinnerItemPosition(spnQVProblem2, 2, cur.getString(cur.getColumnIndex("QVProblem1"))));
                spnQVProblem3.setSelection(Global.SpinnerItemPosition(spnQVProblem3, 2, cur.getString(cur.getColumnIndex("QVProblem2"))));
                spnQVProblem4.setSelection(Global.SpinnerItemPosition(spnQVProblem4, 2, cur.getString(cur.getColumnIndex("QVProblem3"))));
                spnQVProblem5.setSelection(Global.SpinnerItemPosition(spnQVProblem5, 2, cur.getString(cur.getColumnIndex("QVProblem4"))));
                spnQVProblem6.setSelection(Global.SpinnerItemPosition(spnQVProblem6, 2, cur.getString(cur.getColumnIndex("QVProblem5"))));

                //txtQVProblem1.setText(cur.getString(cur.getColumnIndex("QVProblem1")));
                // txtQVProblem2.setText(cur.getString(cur.getColumnIndex("QVProblem2")));
             /*   txtQVProblem3.setText(cur.getString(cur.getColumnIndex("QVProblem3")));
                txtQVProblem4.setText(cur.getString(cur.getColumnIndex("QVProblem4")));
                txtQVProblem5.setText(cur.getString(cur.getColumnIndex("QVProblem5")));
*/
                spnQVSolve2.setSelection(Global.SpinnerItemPosition(spnQVSolve2, 2, cur.getString(cur.getColumnIndex("QVSolve1"))));
                spnQVSolve3.setSelection(Global.SpinnerItemPosition(spnQVSolve3, 2, cur.getString(cur.getColumnIndex("QVSolve2"))));
                spnQVSolve4.setSelection(Global.SpinnerItemPosition(spnQVSolve4, 2, cur.getString(cur.getColumnIndex("QVSolve3"))));
                spnQVSolve5.setSelection(Global.SpinnerItemPosition(spnQVSolve5, 2, cur.getString(cur.getColumnIndex("QVSolve4"))));
                spnQVSolve6.setSelection(Global.SpinnerItemPosition(spnQVSolve6, 2, cur.getString(cur.getColumnIndex("QVSolve5"))));
                //txtQVSolve1.setText(cur.getString(cur.getColumnIndex("QVSolve1")));
                // txtQVSolve2.setText(cur.getString(cur.getColumnIndex("QVSolve2")));
             /*   txtQVSolve3.setText(cur.getString(cur.getColumnIndex("QVSolve3")));
                txtQVSolve4.setText(cur.getString(cur.getColumnIndex("QVSolve4")));
                txtQVSolve5.setText(cur.getString(cur.getColumnIndex("QVSolve5")));
*/
                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(EPISESSIONVisit.this, e.getMessage());
            return;
        }
    }


    protected Dialog onCreateDialog(int id) {
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mDateSetListener, g.mYear, g.mMonth - 1, g.mDay);
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


            dtpDate = (EditText) findViewById(R.id.dtpVdate);
            if (VariableID.equals("btnVdate")) {
                dtpDate = (EditText) findViewById(R.id.dtpVdate);
            }

            dtpDate.setText(new StringBuilder()
                    .append(Global.Right("00" + mDay, 2)).append("/")
                    .append(Global.Right("00" + mMonth, 2)).append("/")
                    .append(mYear));
            if (VariableID.equals("btnVdate")) {

                //((TextView)findViewById(R.id.txtbar)).setText(g.getDay(dtpDate.getText().toString()));
            }
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = sdf.format(c.getTime());
                Date date1 = sdf.parse(formattedDate);

                // Date date2 = sdf.parse(dtpQBHEndDate.getText().toString());

                Date date2 = sdf.parse(dtpVdate.getText().toString());
               /* if (date2.after(date1))
                {

                   if (VariableID.equals("btnQBHEndDate"))
                    {
                        Connection.MessageBox(EPIBARIVisit.this, "তারিখ আজকের তারিখের সমান অথবা কম হতে হবে।");
                        dtpQBHEndDate.setText(null);
                        //((TextView)findViewById(R.id.txtbar)).setText("");
                        return;
                    }

                }

                Date date4 = sdf.parse(formattedDate);*/
                if (date2.after(date1)) {

                    if (VariableID.equals("btnVdate")) {
                        Connection.MessageBox(EPISESSIONVisit.this, "তারিখ আজকের তারিখের সমান অথবা কম হতে হবে।");
                        dtpVdate.setText(null);
                        ((TextView) findViewById(R.id.txtbar)).setText("");
                        return;
                    }


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


            // tpTime.setText(new StringBuilder().append(Global.Right("00"+hour,2)).append(":").append(Global.Right("00"+minute,2)));

        }
    };
}