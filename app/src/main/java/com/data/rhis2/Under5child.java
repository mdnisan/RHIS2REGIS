package com.data.rhis2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.KeyguardManager;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import Common.Connection;
import Common.Global;

/**
 * Created by Nisan on 10/9/2015.
 */
public class Under5child extends Activity {
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
        AlertDialog.Builder adb = new AlertDialog.Builder(Under5child.this);
        switch (item.getItemId()) {
            case R.id.menuClose:
                adb.setTitle("Close");
                adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান?");
                // adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান[Yes/No]?");
                adb.setNegativeButton("না", null);
                adb.setPositiveButton("হ্যাঁ", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (g.getCallFrom().equals("1")) {
                            finish();
                            Intent f2 = new Intent(getApplicationContext(), Under5ChildView.class);
                            startActivity(f2);
                        } else {
                            finish();
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
    private String AgeN = "";
    ArrayList<String> SymtomValue = new ArrayList<String>();
    ArrayList<String> TreatmentValue = new ArrayList<String>();

    //static final String [] SymtomValue={};
    Connection C;
    Global g;
    SimpleAdapter dataAdapter;
    Calendar c = Calendar.getInstance();
    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();

    SimpleAdapter dataAdapter1;
    ArrayList<HashMap<String, String>> dataList1 = new ArrayList<HashMap<String, String>>();


    private String TableName;
    //private String TableNameS;
    private String TableNameU5p;
    private String TableNameU5a;
    private String TableName1;
    LinearLayout secDiv;
    TextView VlblDiv;
    EditText txtDiv;

    // LinearLayout secVisitCom;
    //Spinner spnVisitCom;
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
    TextView txtHHNO;

    LinearLayout secName;
    TextView VlblName;
    TextView txtName;

    // LinearLayout secHusName;
    //TextView VlblHusName;
    //TextView txtHusName;

    LinearLayout secAge;
    TextView VlblAge;
    TextView txtAge;
    TextView VlblAge1;
    TextView txtSex;
    LinearLayout secProblem;
    TextView VlblProblem;
    Spinner spnProblem;

    //LinearLayout secAdvice;
    //TextView VlblAdvice;
    //Spinner spnAdvice;

    //LinearLayout secGA;

    LinearLayout secVDate;
    TextView VlblVDate;
    //CheckBox ChkPNCCh1;
    EditText dtpVDate;
    ImageButton btnVDate;

    LinearLayout secRemarks;
    TextView VlblRemarks;
    EditText txtRemarks;


    CheckBox chkSy1;
    LinearLayout secAGO2MGA1;

    CheckBox chkSy2;
    LinearLayout secAGO2MGA2;

    CheckBox chkSy3;
    LinearLayout secAGO2MGA3;

    CheckBox chkSy4;
    LinearLayout secAGO2MGA4;

    CheckBox chkSy5;
    LinearLayout secAGO2MGA5;

    CheckBox chkSy6;
    LinearLayout secAGO2MGA6;

    CheckBox chkSy7;
    LinearLayout secAGO2MGA7;
    CheckBox chkSy8;
    LinearLayout secAGO2MGA8;
    LinearLayout secOther;
    LinearLayout secAGO2MGATetClass1;
    LinearLayout secAGO2MGATetClass2;
    LinearLayout secAGO2MGATetClass3;
    LinearLayout secAGO2MDarTetClass1;
    LinearLayout secAGO2MDarTetClass2;
    LinearLayout secAGO2MFeedTetClass1;
    LinearLayout secAGO2MFeedTetClass2;

    LinearLayout secGeA1;
    TextView VGeACode1;
    CheckBox chkGeA1;
    TextView VGeA1;

    LinearLayout secGeA2;
    TextView VGeACode2;
    CheckBox chkGeA2;
    TextView VGeA2;

    LinearLayout secGeA3;
    TextView VGeACode3;
    CheckBox chkGeA3;
    TextView VGeA3;

    LinearLayout secGeA4;
    TextView VGeACode4;
    CheckBox chkGeA4;
    TextView VGeA4;

    LinearLayout secGeA5;
    TextView VGeACode5;
    CheckBox chkGeA5;
    TextView VGeA5;

    LinearLayout secGeA6;
    TextView VGeACode6;
    CheckBox chkGeA6;
    TextView VGeA6;

    LinearLayout secGeA7;
    TextView VGeACode7;
    CheckBox chkGeA7;
    TextView VGeA7;

    LinearLayout secGeA8;
    TextView VGeACode8;
    CheckBox chkGeA8;
    TextView VGeA8;

    LinearLayout secGeA9;
    TextView VGeACode9;
    CheckBox chkGeA9;
    TextView VGeA9;


    //LinearLayout secGAC1;
    TextView VAG02GAC1;
    // LinearLayout secGAC2;
    TextView VAG02GAC2;
    //  LinearLayout secGAC3;
    TextView VAG02GAC3;

    // LinearLayout secAGO2MDarC1;
    TextView VAGO2MDarC1;
    /* LinearLayout secAGO2MDarC11;
     TextView VAGO2MDarC11;*/
    // LinearLayout secAGO2MDarC2;
    TextView VAGO2MDarC2;

    // LinearLayout secAGO2MFedC1;
    TextView VAGO2MFedC1;

    //LinearLayout secAGO2MFedC2;
    TextView VAGO2MFedC2;

    LinearLayout secGeATet1;
    TextView VGeACodeTet1;
    CheckBox chkGeATet1;
    TextView VGeATet1;
    LinearLayout secGeATet2;
    TextView VGeACodeTet2;
    CheckBox chkGeATet2;
    TextView VGeATet2;
    LinearLayout secGeATet3;
    TextView VGeACodeTet3;
    CheckBox chkGeATet3;
    TextView VGeATet3;
    LinearLayout secGeATet4;
    TextView VGeACodeTet4;
    CheckBox chkGeATet4;
    TextView VGeATet4;

    LinearLayout secGeATet5;
    TextView VGeACodeTet5;
    CheckBox chkGeATet5;
    TextView VGeATet5;

    LinearLayout secGeATet6;
    TextView VGeACodeTet6;
    CheckBox chkGeATet6;
    TextView VGeATet6;

    LinearLayout secGeATet7;
    TextView VGeACodeTet7;
    CheckBox chkGeATet7;
    TextView VGeATet7;

    LinearLayout secGeATet7a;
    TextView VGeACodeTet7a;
    CheckBox chkGeATet7a;
    TextView VGeATet7a;

    LinearLayout secGeATet8;
    TextView VGeACodeTet8;
    CheckBox chkGeATet8;
    TextView VGeATet8;
    LinearLayout secGeATet9;
    TextView VGeACodeTet9;
    CheckBox chkGeATet9;
    TextView VGeATet9;

    LinearLayout secGeATet9a;
    TextView VGeACodeTet9a;
    CheckBox chkGeATet9a;
    TextView VGeATet9a;

    LinearLayout secAGO2MDar1;
    TextView VAGO2MDar1;
    CheckBox chkAGO2MDar1;
    TextView VVAGO2MDar1;

    LinearLayout secAGO2MDar2;
    TextView VAGO2MDar2;
    CheckBox chkAGO2MDar2;
    TextView VVAGO2MDar2;

    LinearLayout secAGO2MDar3;
    TextView VAGO2MDar3;
    CheckBox chkAGO2MDar3;
    TextView VVAGO2MDar3;

    LinearLayout secAGO2MDar4;
    TextView VAGO2MDar4;
    CheckBox chkAGO2MDar4;
    TextView VVAGO2MDar4;

    LinearLayout secAGO2MDar5;
    TextView VAGO2MDar5;
    CheckBox chkAGO2MDar5;
    TextView VVAGO2MDar5;

    LinearLayout secAGO2MDar6;
    TextView VAGO2MDar6;
    CheckBox chkAGO2MDar6;
    TextView VVAGO2MDar6;

    LinearLayout secAGO2MDar7;
    TextView VAGO2MDar7;
    CheckBox chkAGO2MDar7;
    TextView VVAGO2MDar7;

    LinearLayout secAGO2MDarTet1;
    TextView VAGO2MDarCodeTet1;
    CheckBox chkAGO2MDarTet1;
    TextView VAGO2MDarTet1;

    LinearLayout secAGO2MDarTet2;
    TextView VAGO2MDarCodeTet2;
    CheckBox chkAGO2MDarTet2;
    TextView VAGO2MDarTet2;

    LinearLayout secAGO2MDarTet3;
    TextView VAGO2MDarCodeTet3;
    CheckBox chkAGO2MDarTet3;
    TextView VAGO2MDarTet3;

    LinearLayout secAGO2MDarTet4;
    TextView VAGO2MDarCodeTet4;
    CheckBox chkAGO2MDarTet4;
    TextView VAGO2MDarTet4;

    LinearLayout secAGO2MDarTet5;
    TextView VAGO2MDarCodeTet5;
    CheckBox chkAGO2MDarTet5;
    TextView VAGO2MDarTet5;

    LinearLayout secAGO2MDarTet6;
    TextView VAGO2MDarCodeTet6;
    CheckBox chkAGO2MDarTet6;
    TextView VAGO2MDarTet6;

    LinearLayout secAGO2MDarTet6a;
    TextView VAGO2MDarCodeTet6a;
    CheckBox chkAGO2MDarTet6a;
    TextView VAGO2MDarTet6a;

    LinearLayout secAGO2MFed1;
    TextView VAGO2MFed1;
    CheckBox chkAGO2MFed1;
    TextView VVAGO2MFed1;

    LinearLayout secAGO2MFed2;
    TextView VAGO2MFed2;
    CheckBox chkAGO2MFed2;
    TextView VVAGO2MFed2;


    LinearLayout secAGO2MFed3;
    TextView VAGO2MFed3;
    CheckBox chkAGO2MFed3;
    TextView VVAGO2MFed3;


    LinearLayout secAGO2MFed4;
    TextView VAGO2MFed4;
    CheckBox chkAGO2MFed4;
    TextView VVAGO2MFed4;


    LinearLayout secAGO2MFed5;
    TextView VAGO2MFed5;
    CheckBox chkAGO2MFed5;
    TextView VVAGO2MFed5;


    LinearLayout secAGO2MFed6;
    TextView VAGO2MFed6;
    CheckBox chkAGO2MFed6;
    TextView VVAGO2MFed6;

    LinearLayout secAGO2MFedTet1;
    TextView VAGO2MFedCodeTet1;
    CheckBox chkAGO2MFedTet1;
    TextView VAGO2MFedTet1;

    LinearLayout secAGO2MFedTet2;
    TextView VAGO2MFedCodeTet2;
    CheckBox chkAGO2MFedTet2;
    TextView VAGO2MFedTet2;

    LinearLayout secAGO2MFedTet3;
    TextView VAGO2MFedCodeTet3;
    CheckBox chkAGO2MFedTet3;
    TextView VAGO2MFedTet3;

    LinearLayout secAGO2MFedTet4;
    TextView VAGO2MFedCodeTet4;
    CheckBox chkAGO2MFedTet4;
    TextView VAGO2MFedTet4;
    LinearLayout secAGO2MFedTet5;
    TextView VAGO2MFedCodeTet5;
    CheckBox chkAGO2MFedTet5;
    TextView VAGO2MFedTet5;

    LinearLayout secAGO2MFedTet6;
    TextView VAGO2MFedCodeTet6;
    CheckBox chkAGO2MFedTet6;
    TextView VAGO2MFedTet6;

    LinearLayout secAGO2MFedTet7;
    TextView VAGO2MFedCodeTet7;
    CheckBox chkAGO2MFedTet7;
    TextView VAGO2MFedTet7;

    LinearLayout secSy;

    LinearLayout secAGO2MGA;
    //LinearLayout secAGO2MD;
    //LinearLayout secAGO2MF;


    // 2to 5year
    //   LinearLayout secAG25YNu;
    //  CheckBox chkAG25YNu;

    //  LinearLayout secAG25YNu1;
    TextView VAAG25YNu1;

    // LinearLayout secAG25YNu2;
    TextView VAAG25YNu2;


    //  LinearLayout secAG25YNu3;
    TextView VAAG25YNu3;


    LinearLayout secAG25YSym1;
    TextView VAG25YSymCode1;
    CheckBox chkAG25YSym1;
    TextView VVAG25YSym1;

    LinearLayout secAG25YSym2;
    TextView VAG25YSymCode2;
    CheckBox chkAG25YSym2;
    TextView VVAG25YSym2;

    LinearLayout secAG25YSym3;
    TextView VAG25YSymCode3;
    CheckBox chkAG25YSym3;
    TextView VVAG25YSym3;

    LinearLayout secAG25YSym4;
    TextView VAG25YSymCode4;
    CheckBox chkAG25YSym4;
    TextView VVAG25YSym4;

    LinearLayout secAG25YTet1;
    TextView VAG25YTetCode1;
    CheckBox chkAG25YTet1;
    TextView VVAG25YTet1;

    LinearLayout secAG25YTet2;
    TextView VAG25YTetCode2;
    CheckBox chkAG25YTet2;
    TextView VVAG25YTet2;

    LinearLayout secAG25YTet3;
    TextView VAG25YTetCode3;
    CheckBox chkAG25YTet3;
    TextView VVAG25YTet3;

    LinearLayout secAG25YTet4;
    TextView VAG25YTetCode4;
    CheckBox chkAG25YTet4;
    TextView VVAG25YTet4;

    LinearLayout secAG25YTet5;
    TextView VAG25YTetCode5;
    CheckBox chkAG25YTet5;
    TextView VVAG25YTet5;

    LinearLayout secAG25YTet5a;
    TextView VAG25YTetCode5a;
    CheckBox chkAG25YTet5a;
    TextView VVAG25YTet5a;

    LinearLayout secAG25YTet6;
    TextView VAG25YTetCode6;
    CheckBox chkAG25YTet6;
    TextView VVAG25YTet6;

    LinearLayout secAG25YTet7;
    TextView VAG25YTetCode7;
    CheckBox chkAG25YTet7;
    TextView VVAG25YTet7;

    // LinearLayout secAG25YNuSymtom1;
    LinearLayout secAG25YNuClass1;
    LinearLayout secAG25YNuClass2;
    LinearLayout secAG25YNuClass3;
    LinearLayout secAG25YDarTetClass1;
    LinearLayout secAG25YDarTetClass2;
    LinearLayout secAG25YDarTetClass3;
    // LinearLayout secAG25YDarC1;
    TextView VAG25YDarC1;
    //  LinearLayout secAG25YDarC2;
    TextView VAG25YDarC2;

    // LinearLayout secAG25YDarC3;
    TextView VAG25YDarC3;

    /*   LinearLayout secAG25YDar;
       CheckBox chkAG25YDar;*/
    // LinearLayout secAG25YDarSymtom;
    LinearLayout secAG25YDar1;
    TextView VAG25YDarCode1;
    CheckBox chkAG25YDar1;
    TextView VVAG25YDar1;

    LinearLayout secAG25YDar2;
    TextView VAG25YDarCode2;
    CheckBox chkAG25YDar2;
    TextView VVAG25YDar2;

    LinearLayout secAG25YDar3;
    TextView VAG25YDarCode3;
    CheckBox chkAG25YDar3;
    TextView VVAG25YDar3;

    LinearLayout secAG25YDar4;
    TextView VAG25YDarCode4;
    CheckBox chkAG25YDar4;
    TextView VVAG25YDar4;

    LinearLayout secAG25YDar5;
    TextView VAG25YDarCode5;
    CheckBox chkAG25YDar5;
    TextView VVAG25YDar5;

    LinearLayout secAG25YDar6;
    TextView VAG25YDarCode6;
    CheckBox chkAG25YDar6;
    TextView VVAG25YDar6;

    LinearLayout secAG25YDar7;
    TextView VAG25YDarCode7;
    CheckBox chkAG25YDar7;
    TextView VVAG25YDar7;

    LinearLayout secAG25YDar8;
    TextView VAG25YDarCode8;
    CheckBox chkAG25YDar8;
    TextView VVAG25YDar8;

    LinearLayout secAG25YDar9;
    TextView VAG25YDarCode9;
    CheckBox chkAG25YDar9;
    TextView VVAG25YDar9;

    LinearLayout secAG25YDarTet1;
    TextView VAG25YDarCodeTet1;
    CheckBox chkAG25YDarTet1;
    TextView VAG25YDarTet1;

    LinearLayout secAG25YDarTet2;
    TextView VAG25YDarCodeTet2;
    CheckBox chkAG25YDarTet2;
    TextView VAG25YDarTet2;

    LinearLayout secAG25YDarTet3;
    TextView VAG25YDarCodeTet3;
    CheckBox chkAG25YDarTet3;
    TextView VAG25YDarTet3;

    LinearLayout secAG25YDarTet3a;
    TextView VAG25YDarCodeTet3a;
    CheckBox chkAG25YDarTet3a;
    TextView VAG25YDarTet3a;

    LinearLayout secAG25YDarTet4;
    TextView VAG25YDarCodeTet4;
    CheckBox chkAG25YDarTet4;
    TextView VAG25YDarTet4;

    LinearLayout secAG25YDarTet5;
    TextView VAG25YDarCodeTet5;
    CheckBox chkAG25YDarTet5;
    TextView VAG25YDarTet5;

    LinearLayout secAG25YDarTet6;
    TextView VAG25YDarCodeTet6;
    CheckBox chkAG25YDarTet6;
    TextView VAG25YDarTet6;

    LinearLayout secAG25YDarTet6a;
    TextView VAG25YDarCodeTet6a;
    CheckBox chkAG25YDarTet6a;
    TextView VAG25YDarTet6a;

    //  LinearLayout secAG25YAm;
    // CheckBox chkAG25YAm;

    LinearLayout secAG25YAm1;
    TextView VAG25YAmCode1;
    CheckBox chkAG25YAm1;
    TextView VVAG25YAm1;

    LinearLayout secAG25YAmSymtom;
    // LinearLayout secAG25YAmC1;
    TextView VAG25YAmC1;

    LinearLayout secAG25YAmTet1;
    TextView VAG25YAmCodeTet1;
    CheckBox chkAG25YamTet1;
    TextView VAG25YAmTet1;

    LinearLayout secAG25YAmTet2;
    TextView VAG25YAmCodeTet2;
    CheckBox chkAG25YamTet2;
    TextView VAG25YAmTet2;


    LinearLayout secAG25YAmTet2a;
    TextView VAG25YAmCodeTet2a;
    CheckBox chkAG25YamTet2a;
    TextView VAG25YAmTet2a;

    // LinearLayout secAG25YFev;
    //CheckBox chkAG25YFev;

    LinearLayout secAG25YFev1;
    TextView VAG25YFevCode1;
    CheckBox chkAG25YFev1;
    TextView VVAG25YFev1;


    LinearLayout secAG25YFev2;
    TextView VAG25YFevCode2;
    CheckBox chkAG25YFev2;
    TextView VVAG25YFev2;

    LinearLayout secAG25YFevSymtom;
    // LinearLayout secAG25YFevC1;
    TextView VAG25YFevC1;

    LinearLayout secAG25YFevTet1;
    TextView VAG25YFevCodeTet1;
    CheckBox chkAG25YFevTet1;
    TextView VAG25YFevTet1;

    LinearLayout secAG25YFevTet2;
    TextView VAG25YFevCodeTet2;
    CheckBox chkAG25YFevTet2;
    TextView VAG25YFevTet2;
    LinearLayout secAG25YFevTet3;
    TextView VAG25YFevCodeTet3;
    CheckBox chkAG25YFevTet3;
    TextView VAG25YFevTet3;

    LinearLayout secAG25YFevTet4;
    TextView VAG25YFevCodeTet4;
    CheckBox chkAG25YFevTet4;
    TextView VAG25YFevTet4;

    LinearLayout secAG25YFevTet4a;
    TextView VAG25YFevCodeTet4a;
    CheckBox chkAG25YFevTet4a;
    TextView VAG25YFevTet4a;

    // LinearLayout secAG25YMal;
    // CheckBox chkAG25YMal;

    LinearLayout secAG25YMal1;
    TextView VAG25YMalCode1;
    CheckBox chkAG25YMal1;
    TextView VVAG25YMal1;


    LinearLayout secAG25YMal2;
    TextView VAG25YMalCode2;
    CheckBox chkAG25YMal2;
    TextView VVAG25YMal2;

    LinearLayout secAG25YMalSymtom1;
    // LinearLayout secAG25YMalC1;
    TextView VAG25YMalC1;
    LinearLayout secAG25YMalSymtom2;
    //LinearLayout secAG25YMalC2;
    TextView VAG25YMalC2;

    LinearLayout secAG25YMalTet1;
    TextView VAG25YMalCodeTet1;
    CheckBox chkAG25YMalTet1;
    TextView VAG25YMalTet1;

    LinearLayout secAG25YMalTet2;
    TextView VAG25YMalCodeTet2;
    CheckBox chkAG25YMalTet2;
    TextView VAG25YMalTet2;

    LinearLayout secAG25YMalTet2a;
    TextView VAG25YMalCodeTet2a;
    CheckBox chkAG25YMalTet2a;
    TextView VAG25YMalTet2a;

    LinearLayout secAG25YMalTet3;
    TextView VAG25YMalCodeTet3;
    CheckBox chkAG25YMalTet3;
    TextView VAG25YMalTet3;

    LinearLayout secAG25YMalTet4;
    TextView VAG25YMalCodeTet4;
    CheckBox chkAG25YMalTet4;
    TextView VAG25YMalTet4;

    LinearLayout secAG25YMalTet5;
    TextView VAG25YMalCodeTet5;
    CheckBox chkAG25YMalTet5;
    TextView VAG25YMalTet5;

    LinearLayout secAG25YMalTet6;
    TextView VAG25YMalCodeTet6;
    CheckBox chkAG25YMalTet6;
    TextView VAG25YMalTet6;

    LinearLayout secAG25YMalTet7;
    TextView VAG25YMalCodeTet7;
    CheckBox chkAG25YMalTet7;
    TextView VAG25YMalTet7;

    LinearLayout secAG25YMalTet8;
    TextView VAG25YMalCodeTet8;
    CheckBox chkAG25YMalTet8;
    TextView VAG25YMalTet8;

    LinearLayout secAG25YMalTet8a;
    TextView VAG25YMalCodeTet8a;
    CheckBox chkAG25YMalTet8a;
    TextView VAG25YMalTet8a;
///

    //  LinearLayout secAG25YNut;
    //  CheckBox chkAG25YNut;

    LinearLayout secAG25YNut1;
    TextView VAG25YNutCode1;
    CheckBox chkAG25YNut1;
    TextView VVAG25YNut1;


    LinearLayout secAG25YNut2;
    TextView VAG25YNutCode2;
    CheckBox chkAG25YNut2;
    TextView VVAG25YNut2;

    LinearLayout secAG25YNut3;
    TextView VAG25YNutCode3;
    CheckBox chkAG25YNut3;
    TextView VVAG25YNut3;

    LinearLayout secAG25YNut4;
    TextView VAG25YNutCode4;
    CheckBox chkAG25YNut4;
    TextView VVAG25YNut4;


    //  LinearLayout secAG25YNutSymtom;
    LinearLayout secAG25YNutSymtom1;
    //LinearLayout secAG25YNutC1;
    TextView VAG25YNutC1;
    LinearLayout secAG25YNutSymtom2;
    //LinearLayout secAG25YNutC2;
    TextView VAG25YNutC2;

    LinearLayout secAG25YNutSymtom3;
    //LinearLayout secAG25YNutC3;
    TextView VAG25YNutC3;

    LinearLayout secAG25YNutTet1;
    TextView VAG25YNutCodeTet1;
    CheckBox chkAG25YNutTet1;
    TextView VAG25YNutTet1;

    LinearLayout secAG25YNutTet2;
    TextView VAG25YNutCodeTet2;
    CheckBox chkAG25YNutTet2;
    TextView VAG25YNutTet2;
    LinearLayout secAG25YNutTet3;
    TextView VAG25YNutCodeTet3;
    CheckBox chkAG25YNutTet3;
    TextView VAG25YNutTet3;

    LinearLayout secAG25YNutTet4;
    TextView VAG25YNutCodeTet4;
    CheckBox chkAG25YNutTet4;
    TextView VAG25YNutTet4;

    LinearLayout secAG25YNutTet5;
    TextView VAG25YNutCodeTet5;
    CheckBox chkAG25YNutTet5;
    TextView VAG25YNutTet5;


    LinearLayout secAG25YNutTet5a;
    TextView VAG25YNutCodeTet5a;
    CheckBox chkAG25YNutTet5a;
    TextView VAG25YNutTet5a;

    LinearLayout secAG25YNutTet6;
    TextView VAG25YNutCodeTet6;
    CheckBox chkAG25YNutTet6;
    TextView VAG25YNutTet6;

    LinearLayout secAG25YNutTet7;
    TextView VAG25YNutCodeTet7;
    CheckBox chkAG25YNutTet7;
    TextView VAG25YNutTet7;

    LinearLayout secAG25YNutTet7a;
    TextView VAG25YNutCodeTet7a;
    CheckBox chkAG25YNutTet7a;
    TextView VAG25YNutTet7a;


//

    // LinearLayout secAG25YLow;
    // CheckBox chkAG25YLow;

    LinearLayout secAG25YLow1;
    TextView VAG25YLowCode1;
    CheckBox chkAG25YLow1;
    TextView VVAG25YLow1;


    LinearLayout secAG25YLow2;
    TextView VAG25YLowCode2;
    CheckBox chkAG25YLow2;
    TextView VVAG25YLow2;

    LinearLayout secAG25YLow3;
    TextView VAG25YLowCode3;
    CheckBox chkAG25YLow3;
    TextView VVAG25YLow3;


    //LinearLayout secAG25YLowSymtom;
    LinearLayout secAG25YLowSymtom1;
    // LinearLayout secAG25YLowC1;
    TextView VAG25YLowC1;
    LinearLayout secAG25YLowSymtom2;
    // LinearLayout secAG25YLowC2;
    TextView VAG25YLowC2;

    LinearLayout secAG25YLowSymtom3;
    //  LinearLayout secAG25YLowC3;
    TextView VAG25YLowC3;

    LinearLayout secAG25YLowTet1;
    TextView VAG25YLowCodeTet1;
    CheckBox chkAG25YLowTet1;
    TextView VAG25YLowTet1;

    LinearLayout secAG25YLowTet2;
    TextView VAG25YLowCodeTet2;
    CheckBox chkAG25YLowTet2;
    TextView VAG25YLowTet2;
    LinearLayout secAG25YLowTet3;
    TextView VAG25YLowCodeTet3;
    CheckBox chkAG25YLowTet3;
    TextView VAG25YLowTet3;

    LinearLayout secAG25YLowTet4;
    TextView VAG25YLowCodeTet4;
    CheckBox chkAG25YLowTet4;
    TextView VAG25YLowTet4;

    LinearLayout secAG25YLowTet4a;
    TextView VAG25YLowCodeTet4a;
    CheckBox chkAG25YLowTet4a;
    TextView VAG25YLowTet4a;

    LinearLayout secAG25YLowTet5;
    TextView VAG25YLowCodeTet5;
    CheckBox chkAG25YLowTet5;
    TextView VAG25YLowTet5;


    LinearLayout secAG25YLowTet5a;
    TextView VAG25YLowCodeTet5a;
    CheckBox chkAG25YLowTet5a;
    TextView VAG25YLowTet5a;

    // LinearLayout secAG25YOth;
    // CheckBox chkAG25YOth;

    LinearLayout secAG25YOth1;
    TextView VAG25YOthCode1;
    CheckBox chkAG25YOth1;
    TextView VVAG25YOth1;


    LinearLayout secAG25YOth2;
    TextView VAG25YOthCode2;
    CheckBox chkAG25YOth2;
    TextView VVAG25YOth2;

    LinearLayout secAG25YOth3;
    TextView VAG25YOthCode3;
    CheckBox chkAG25YOth3;
    TextView VVAG25YOth3;

    LinearLayout secAG25YOth4;
    TextView VAG25YOthCode4;
    CheckBox chkAG25YOth4;
    TextView VVAG25YOth4;

    LinearLayout secAG25YOth5;
    TextView VAG25YOthCode5;
    CheckBox chkAG25YOth5;
    TextView VVAG25YOth5;

    LinearLayout secAG25YOth6;
    TextView VAG25YOthCode6;
    CheckBox chkAG25YOth6;
    TextView VVAG25YOth6;

    LinearLayout secAG25YOth7;
    TextView VAG25YOthCode7;
    CheckBox chkAG25YOth7;
    TextView VVAG25YOth7;


    // LinearLayout secAG25YOthSymtom;

    LinearLayout secAG2to5Nu;

    LinearLayout secOth1;
    LinearLayout secOth2;
    LinearLayout secOth3;
    LinearLayout secOth4;
    LinearLayout secOth5;
    LinearLayout secOth6;
    LinearLayout secOth7;
    // CheckBox chkOth;
    TextView VOth1;
    TextView VOth2;
    TextView VOth3;
    TextView VOth4;
    TextView VOth5;
    TextView VOth6;
    TextView VOth7;
    Button cmdSave;
    Button btnTreatment1;
    Button btnTreatment2;
    Button btnTreatment3;
    Button btnTreatment4;
    Button btnTreatment5;
    Button btnTreatment6;
    Button btnTreatment7;

    Button btnTreatment8;
    Button btnTreatment9;
    Button btnTreatment10;
    Button btnTreatment11;
    Button btnTreatment12;
    Button btnTreatment13;
    Button btnTreatment14;
    Button btnTreatment15;
    Button btnTreatment16;
    Button btnTreatment17;
    Button btnTreatment18;
    Button btnTreatment19;
    Button btnTreatment20;
    Button btnTreatment21;

    Button btnTreatment22;
    Button btnTreatment23;
    Button btnTreatment24;
    Button btnTreatment25;
    Button btnTreatment26;
    Button btnTreatment27;
    Button btnTreatment28;
    Button btnTreatment29;
    Button btnTreatment30;

    Button cmdTVisit;
    //Button cmdProblem;
    String StartTime;
    TableLayout tableLayout1;
    TableLayout tableLayout2;
    TableLayout tableLayout3;
    TableLayout tableLayout4a;
    TableLayout tableLayout5;
    TableLayout tableLayout6a;
    TableLayout tableLayout7;
    TableLayout tableLayout8a;
    TableLayout tableLayout9;
    TableLayout tableLayout10;
    TableLayout tableLayout11a;
    TableLayout tableLayout12;
    TableLayout tableLayout13;
    TableLayout tableLayout14;
    TableLayout tableLayout15a;
    TableLayout tableLayout16;
    TableLayout tableLayout17;
    TableLayout tableLayout19a;
    TableLayout tableLayout20;
    TableLayout tableLayout21;
    TableLayout tableLayout22;
    TableLayout tableLayout23;
    TableLayout tableLayout24;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.under5child);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();

            TableName = "under5Child";
            // TableNameS = "symtom";
            TableNameU5p = "under5ChildProblem";
            TableNameU5a = "under5ChildAdvice";

            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Activity.KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
            lock.disableKeyguard();
            // secVisitCom = (LinearLayout) findViewById(R.id.secVisitCom);
            // spnVisitCom = (Spinner) findViewById(R.id.spnVisitCom);
            // spnVisitCom.setAdapter(C.getArrayAdapter("Select '' as visitDate from under5Child union Select strftime('%d/%m/%Y', date(visitDate)) from under5Child where healthId='" +g.getGeneratedId()+ "' and providerId='" +  g.getProvCode() + "'"+"order by visitDate asc"));
            seclblH = (LinearLayout) findViewById(R.id.seclblH);
            lblHlblH = (TextView) findViewById(R.id.lblHlblH);
            lblHealthID = (TextView) findViewById(R.id.lblHealthID);
            txtHealthID = (TextView) findViewById(R.id.txtHealthID);

            secSl = (LinearLayout) findViewById(R.id.secSl);
            VlblSNo = (TextView) findViewById(R.id.VlblSNo);
            txtSNo = (TextView) findViewById(R.id.txtSNo);
       /*     if (g.getCallFrom().equals("1")) {

            } else {

            }*/
            txtSex = (TextView) findViewById(R.id.txtSex);
            txtHHNO = (TextView) findViewById(R.id.txtHHNO);
            txtAge = (TextView) findViewById(R.id.txtAge);
            VlblAge1 = (TextView) findViewById(R.id.VlblAge1);
            // VlblAge1.setText("মাস");
            secName = (LinearLayout) findViewById(R.id.secName);
            VlblName = (TextView) findViewById(R.id.VlblName);
            txtName = (TextView) findViewById(R.id.txtName);
            secSy = (LinearLayout) findViewById(R.id.secSy);


            chkSy1 = (CheckBox) findViewById(R.id.chkSy1);
            chkSy1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkSy1.isChecked()) {
                        secAGO2MGA1.setVisibility(View.VISIBLE);
                        // secSy.setVisibility(View.VISIBLE);
                    } else {

                        // secSy.setVisibility(View.GONE);
                        secAGO2MGA1.setVisibility(View.GONE);
                        chkGeA1.setChecked(false);
                        chkGeA2.setChecked(false);
                        chkGeA3.setChecked(false);
                        chkGeA4.setChecked(false);
                        chkGeA5.setChecked(false);
                        chkGeA6.setChecked(false);
                        chkGeA7.setChecked(false);
                        chkGeA8.setChecked(false);
                        chkGeA9.setChecked(false);
                        chkGeATet1.setChecked(false);
                        chkGeATet2.setChecked(false);
                        chkGeATet3.setChecked(false);
                        chkGeATet4.setChecked(false);
                        chkGeATet5.setChecked(false);
                        chkGeATet6.setChecked(false);
                        chkGeATet7.setChecked(false);
                        chkGeATet7a.setChecked(false);
                        chkGeATet8.setChecked(false);
                        chkGeATet9.setChecked(false);
                        chkGeATet9a.setChecked(false);


                    }
                }
            });
            secAGO2MGA1 = (LinearLayout) findViewById(R.id.secAGO2MGA1);

            chkSy2 = (CheckBox) findViewById(R.id.chkSy2);
            chkSy2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkSy2.isChecked()) {
                        secAGO2MGA2.setVisibility(View.VISIBLE);
                        // secSy.setVisibility(View.VISIBLE);
                    } else {

                        // secSy.setVisibility(View.GONE);
                        secAGO2MGA2.setVisibility(View.GONE);
                        chkAGO2MDar1.setChecked(false);
                        chkAGO2MDar2.setChecked(false);
                        chkAGO2MDar3.setChecked(false);
                        chkAGO2MDar4.setChecked(false);
                        chkAGO2MDar5.setChecked(false);
                        chkAGO2MDar6.setChecked(false);
                        chkAGO2MDar7.setChecked(false);
                        chkAGO2MDarTet1.setChecked(false);
                        chkAGO2MDarTet2.setChecked(false);
                        chkAGO2MDarTet3.setChecked(false);
                        chkAGO2MDarTet4.setChecked(false);
                        chkAGO2MDarTet5.setChecked(false);
                        chkAGO2MDarTet6.setChecked(false);
                        chkAGO2MDarTet6a.setChecked(false);


                    }
                }
            });
            secAGO2MGA2 = (LinearLayout) findViewById(R.id.secAGO2MGA2);

            chkSy3 = (CheckBox) findViewById(R.id.chkSy3);
            chkSy3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkSy3.isChecked()) {
                        secAGO2MGA3.setVisibility(View.VISIBLE);
                        // secSy.setVisibility(View.VISIBLE);
                    } else {

                        // secSy.setVisibility(View.GONE);
                        secAGO2MGA3.setVisibility(View.GONE);
                        chkAGO2MFed1.setChecked(false);
                        chkAGO2MFed2.setChecked(false);
                        chkAGO2MFed3.setChecked(false);
                        chkAGO2MFed4.setChecked(false);
                        chkAGO2MFed5.setChecked(false);
                        chkAGO2MFed6.setChecked(false);
                        chkAGO2MFedTet1.setChecked(false);
                        chkAGO2MFedTet2.setChecked(false);
                        chkAGO2MFedTet3.setChecked(false);
                        chkAGO2MFedTet4.setChecked(false);
                        chkAGO2MFedTet5.setChecked(false);
                        chkAGO2MFedTet6.setChecked(false);
                        chkAGO2MFedTet7.setChecked(false);


                    }
                }
            });
            secAGO2MGA3 = (LinearLayout) findViewById(R.id.secAGO2MGA3);


            chkSy4 = (CheckBox) findViewById(R.id.chkSy4);
            chkSy4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkSy4.isChecked()) {
                        secAGO2MGA4.setVisibility(View.VISIBLE);
                        // secSy.setVisibility(View.VISIBLE);
                    } else {

                        // secSy.setVisibility(View.GONE);
                        secAGO2MGA4.setVisibility(View.GONE);
                        chkAG25YSym1.setChecked(false);
                        chkAG25YSym2.setChecked(false);
                        chkAG25YSym3.setChecked(false);
                        chkAG25YSym4.setChecked(false);
                        chkAG25YTet1.setChecked(false);
                        chkAG25YTet2.setChecked(false);
                        chkAG25YTet3.setChecked(false);
                        chkAG25YTet4.setChecked(false);
                        chkAG25YTet5.setChecked(false);
                        chkAG25YTet5a.setChecked(false);
                        chkAG25YTet6.setChecked(false);
                        chkAG25YTet7.setChecked(false);


                    }
                }
            });
            secAGO2MGA4 = (LinearLayout) findViewById(R.id.secAGO2MGA4);

            chkSy5 = (CheckBox) findViewById(R.id.chkSy5);
            chkSy5.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkSy5.isChecked()) {
                        secAGO2MGA5.setVisibility(View.VISIBLE);
                        // secSy.setVisibility(View.VISIBLE);
                    } else {

                        // secSy.setVisibility(View.GONE);
                        secAGO2MGA5.setVisibility(View.GONE);

                        chkAG25YDar1.setChecked(false);
                        chkAG25YDar2.setChecked(false);
                        chkAG25YDar3.setChecked(false);
                        chkAG25YDar4.setChecked(false);
                        chkAG25YDar5.setChecked(false);
                        chkAG25YDar6.setChecked(false);
                        chkAG25YDar7.setChecked(false);
                        chkAG25YDar8.setChecked(false);
                        chkAG25YDar9.setChecked(false);
                        chkAG25YAm1.setChecked(false);
                        chkAG25YDarTet1.setChecked(false);
                        chkAG25YDarTet2.setChecked(false);
                        chkAG25YDarTet3.setChecked(false);
                        chkAG25YDarTet3a.setChecked(false);
                        chkAG25YDarTet4.setChecked(false);
                        chkAG25YDarTet5.setChecked(false);
                        chkAG25YDarTet6.setChecked(false);
                        chkAG25YDarTet6a.setChecked(false);
                        chkAG25YamTet1.setChecked(false);
                        chkAG25YamTet2.setChecked(false);
                        chkAG25YamTet2a.setChecked(false);


                    }
                }
            });
            secAGO2MGA5 = (LinearLayout) findViewById(R.id.secAGO2MGA5);
            chkSy6 = (CheckBox) findViewById(R.id.chkSy6);
            chkSy6.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkSy6.isChecked()) {
                        secAGO2MGA6.setVisibility(View.VISIBLE);
                        // secSy.setVisibility(View.VISIBLE);
                    } else {

                        // secSy.setVisibility(View.GONE);
                        secAGO2MGA6.setVisibility(View.GONE);
                        chkAG25YFev1.setChecked(false);
                        chkAG25YFev2.setChecked(false);
                        chkAG25YMal1.setChecked(false);
                        chkAG25YMal2.setChecked(false);
                        chkAG25YFevTet1.setChecked(false);
                        chkAG25YFevTet2.setChecked(false);
                        chkAG25YFevTet3.setChecked(false);
                        chkAG25YFevTet4.setChecked(false);
                        chkAG25YFevTet4a.setChecked(false);
                        chkAG25YMalTet5.setChecked(false);
                        chkAG25YMalTet6.setChecked(false);
                        chkAG25YMalTet7.setChecked(false);
                        chkAG25YMalTet8.setChecked(false);
                        chkAG25YMalTet8a.setChecked(false);


                    }
                }
            });
            secAGO2MGA6 = (LinearLayout) findViewById(R.id.secAGO2MGA6);

            chkSy7 = (CheckBox) findViewById(R.id.chkSy7);
            chkSy7.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkSy7.isChecked()) {
                        secAGO2MGA7.setVisibility(View.VISIBLE);
                        // secSy.setVisibility(View.VISIBLE);
                    } else {

                        // secSy.setVisibility(View.GONE);
                        secAGO2MGA7.setVisibility(View.GONE);
                        chkAG25YNut1.setChecked(false);
                        chkAG25YNut2.setChecked(false);
                        chkAG25YNut3.setChecked(false);
                        chkAG25YNut4.setChecked(false);
                        chkAG25YNutTet1.setChecked(false);
                        chkAG25YNutTet2.setChecked(false);
                        chkAG25YNutTet3.setChecked(false);
                        chkAG25YNutTet4.setChecked(false);

                        chkAG25YNutTet5.setChecked(false);
                        chkAG25YNutTet5a.setChecked(false);
                        chkAG25YNutTet6.setChecked(false);
                        chkAG25YNutTet7.setChecked(false);
                        chkAG25YNutTet7a.setChecked(false);


                    }
                }
            });
            secAGO2MGA7 = (LinearLayout) findViewById(R.id.secAGO2MGA7);
            chkSy8 = (CheckBox) findViewById(R.id.chkSy8);
            chkSy8.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkSy8.isChecked()) {
                        secAGO2MGA8.setVisibility(View.VISIBLE);
                        // secSy.setVisibility(View.VISIBLE);
                    } else {

                        // secSy.setVisibility(View.GONE);
                        secAGO2MGA8.setVisibility(View.GONE);
                        chkAG25YLow1.setChecked(false);
                        chkAG25YLow2.setChecked(false);
                        chkAG25YLow3.setChecked(false);
                        chkAG25YLowTet1.setChecked(false);
                        chkAG25YLowTet2.setChecked(false);
                        chkAG25YLowTet3.setChecked(false);
                        chkAG25YLowTet4.setChecked(false);
                        chkAG25YLowTet4a.setChecked(false);
                        chkAG25YLowTet5.setChecked(false);
                        chkAG25YLowTet5a.setChecked(false);


                    }
                }
            });
            secAGO2MGA8 = (LinearLayout) findViewById(R.id.secAGO2MGA8);
            secOther = (LinearLayout) findViewById(R.id.secOther);

            secAGO2MGATetClass1 = (LinearLayout) findViewById(R.id.secAGO2MGATetClass1);
            secAGO2MGATetClass2 = (LinearLayout) findViewById(R.id.secAGO2MGATetClass2);
            secAGO2MGATetClass3 = (LinearLayout) findViewById(R.id.secAGO2MGATetClass3);
            secAGO2MDarTetClass1 = (LinearLayout) findViewById(R.id.secAGO2MDarTetClass1);
            secAGO2MDarTetClass2 = (LinearLayout) findViewById(R.id.secAGO2MDarTetClass2);
            secAGO2MFeedTetClass1 = (LinearLayout) findViewById(R.id.secAGO2MFeedTetClass1);
            secAGO2MFeedTetClass2 = (LinearLayout) findViewById(R.id.secAGO2MFeedTetClass2);
            secGeA1 = (LinearLayout) findViewById(R.id.secGeA1);
            VGeACode1 = (TextView) findViewById(R.id.VGeACode1);

            chkGeA1 = (CheckBox) findViewById(R.id.chkGeA1);

            chkGeA1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Classification();

                }
            });
            VGeA1 = (TextView) findViewById(R.id.VGeA1);


            secGeA2 = (LinearLayout) findViewById(R.id.secGeA2);
            VGeACode2 = (TextView) findViewById(R.id.VGeACode2);

            chkGeA2 = (CheckBox) findViewById(R.id.chkGeA2);
            chkGeA2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Classification();

                }
            });
            VGeA2 = (TextView) findViewById(R.id.VGeA2);


            secGeA3 = (LinearLayout) findViewById(R.id.secGeA3);
            VGeACode3 = (TextView) findViewById(R.id.VGeACode3);

            chkGeA3 = (CheckBox) findViewById(R.id.chkGeA3);
            chkGeA3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Classification();

                }
            });
            VGeA3 = (TextView) findViewById(R.id.VGeA3);

            secGeA4 = (LinearLayout) findViewById(R.id.secGeA4);
            VGeACode4 = (TextView) findViewById(R.id.VGeACode4);

            chkGeA4 = (CheckBox) findViewById(R.id.chkGeA4);
            chkGeA4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Classification();

                }
            });
            VGeA4 = (TextView) findViewById(R.id.VGeA4);

            secGeA5 = (LinearLayout) findViewById(R.id.secGeA5);
            VGeACode5 = (TextView) findViewById(R.id.VGeACode5);

            chkGeA5 = (CheckBox) findViewById(R.id.chkGeA5);
            chkGeA5.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Classification();

                }
            });
            VGeA5 = (TextView) findViewById(R.id.VGeA5);

            secGeA6 = (LinearLayout) findViewById(R.id.secGeA6);
            VGeACode6 = (TextView) findViewById(R.id.VGeACode6);

            chkGeA6 = (CheckBox) findViewById(R.id.chkGeA6);
            chkGeA6.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Classification();

                }
            });
            VGeA6 = (TextView) findViewById(R.id.VGeA6);

            secGeA7 = (LinearLayout) findViewById(R.id.secGeA7);
            VGeACode7 = (TextView) findViewById(R.id.VGeACode7);

            chkGeA7 = (CheckBox) findViewById(R.id.chkGeA7);
            chkGeA7.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Classification();

                }
            });
            VGeA7 = (TextView) findViewById(R.id.VGeA7);

            secGeA8 = (LinearLayout) findViewById(R.id.secGeA8);
            VGeACode8 = (TextView) findViewById(R.id.VGeACode8);

            chkGeA8 = (CheckBox) findViewById(R.id.chkGeA8);
            chkGeA8.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Classification();

                }
            });
            VGeA8 = (TextView) findViewById(R.id.VGeA8);

            secGeA9 = (LinearLayout) findViewById(R.id.secGeA9);
            VGeACode9 = (TextView) findViewById(R.id.VGeACode9);

            chkGeA9 = (CheckBox) findViewById(R.id.chkGeA9);
            chkGeA9.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Classification();

                }
            });


            VGeA9 = (TextView) findViewById(R.id.VGeA9);


            // secGAC1 = (LinearLayout) findViewById(R.id.secGAC1);
            VAG02GAC1 = (TextView) findViewById(R.id.VAG02GAC1);


            secGeATet1 = (LinearLayout) findViewById(R.id.secGeATet1);
            VGeACodeTet1 = (TextView) findViewById(R.id.VGeACodeTet1);

            secGeATet2 = (LinearLayout) findViewById(R.id.secGeATet2);
            VGeACodeTet2 = (TextView) findViewById(R.id.VGeACodeTet2);

            chkGeATet1 = (CheckBox) findViewById(R.id.chkGeATet1);
            VGeATet1 = (TextView) findViewById(R.id.VGeATet1);

            chkGeATet2 = (CheckBox) findViewById(R.id.chkGeATet2);
            VGeATet2 = (TextView) findViewById(R.id.VGeATet2);


            secGeATet3 = (LinearLayout) findViewById(R.id.secGeATet3);
            VGeACodeTet3 = (TextView) findViewById(R.id.VGeACodeTet3);

            chkGeATet3 = (CheckBox) findViewById(R.id.chkGeATet3);
            VGeATet3 = (TextView) findViewById(R.id.VGeATet3);

            secGeATet4 = (LinearLayout) findViewById(R.id.secGeATet4);
            VGeACodeTet4 = (TextView) findViewById(R.id.VGeACodeTet4);

            chkGeATet4 = (CheckBox) findViewById(R.id.chkGeATet4);
            VGeATet4 = (TextView) findViewById(R.id.VGeATet4);


            //  secGAC2 = (LinearLayout) findViewById(R.id.secGAC2);
            VAG02GAC2 = (TextView) findViewById(R.id.VAG02GAC2);


            secGeATet5 = (LinearLayout) findViewById(R.id.secGeATet5);
            VGeACodeTet5 = (TextView) findViewById(R.id.VGeACodeTet5);

            chkGeATet5 = (CheckBox) findViewById(R.id.chkGeATet5);
            VGeATet5 = (TextView) findViewById(R.id.VGeATet5);
            chkGeATet5.setEnabled(false);

            secGeATet6 = (LinearLayout) findViewById(R.id.secGeATet6);
            VGeACodeTet6 = (TextView) findViewById(R.id.VGeACodeTet6);
            chkGeATet6 = (CheckBox) findViewById(R.id.chkGeATet6);
            VGeATet6 = (TextView) findViewById(R.id.VGeATet6);


            secGeATet7 = (LinearLayout) findViewById(R.id.secGeATet7);
            VGeACodeTet7 = (TextView) findViewById(R.id.VGeACodeTet7);

            chkGeATet7 = (CheckBox) findViewById(R.id.chkGeATet7);
            VGeATet7 = (TextView) findViewById(R.id.VGeATet7);

            secGeATet7a = (LinearLayout) findViewById(R.id.secGeATet7a);
            VGeACodeTet7a = (TextView) findViewById(R.id.VGeACodeTet7a);
            chkGeATet7a = (CheckBox) findViewById(R.id.chkGeATet7a);
            VGeATet7a = (TextView) findViewById(R.id.VGeATet7a);


            //secGAC3 = (LinearLayout) findViewById(R.id.secGAC3);
            VAG02GAC3 = (TextView) findViewById(R.id.VAG02GAC3);


            secGeATet8 = (LinearLayout) findViewById(R.id.secGeATet8);
            VGeACodeTet8 = (TextView) findViewById(R.id.VGeACodeTet8);

            chkGeATet8 = (CheckBox) findViewById(R.id.chkGeATet8);
            VGeATet8 = (TextView) findViewById(R.id.VGeATet8);

            secGeATet9 = (LinearLayout) findViewById(R.id.secGeATet9);
            VGeACodeTet9 = (TextView) findViewById(R.id.VGeACodeTet9);

            chkGeATet9 = (CheckBox) findViewById(R.id.chkGeATet9);
            VGeATet9 = (TextView) findViewById(R.id.VGeATet9);

            secGeATet9a = (LinearLayout) findViewById(R.id.secGeATet9a);
            VGeACodeTet9a = (TextView) findViewById(R.id.VGeACodeTet9a);

            chkGeATet9a = (CheckBox) findViewById(R.id.chkGeATet9a);
            VGeATet9a = (TextView) findViewById(R.id.VGeATet9a);


            secAGO2MDar1 = (LinearLayout) findViewById(R.id.secAGO2MDar1);
            VAGO2MDar1 = (TextView) findViewById(R.id.VAGO2MDar1);
            chkAGO2MDar1 = (CheckBox) findViewById(R.id.chkAGO2MDar1);
            chkAGO2MDar1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationDar();

                }
            });
            VVAGO2MDar1 = (TextView) findViewById(R.id.VVAGO2MDar1);


            secAGO2MDar2 = (LinearLayout) findViewById(R.id.secAGO2MDar2);
            VAGO2MDar2 = (TextView) findViewById(R.id.VAGO2MDar2);
            chkAGO2MDar2 = (CheckBox) findViewById(R.id.chkAGO2MDar2);
            chkAGO2MDar2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationDar();

                }
            });
            VVAGO2MDar2 = (TextView) findViewById(R.id.VVAGO2MDar2);

            secAGO2MDar3 = (LinearLayout) findViewById(R.id.secAGO2MDar3);
            VAGO2MDar3 = (TextView) findViewById(R.id.VAGO2MDar3);
            chkAGO2MDar3 = (CheckBox) findViewById(R.id.chkAGO2MDar3);
            chkAGO2MDar3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationDar();

                }
            });
            VVAGO2MDar3 = (TextView) findViewById(R.id.VVAGO2MDar3);

            secAGO2MDar4 = (LinearLayout) findViewById(R.id.secAGO2MDar4);
            VAGO2MDar4 = (TextView) findViewById(R.id.VAGO2MDar4);
            chkAGO2MDar4 = (CheckBox) findViewById(R.id.chkAGO2MDar4);
            chkAGO2MDar4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationDar();

                }
            });
            VVAGO2MDar4 = (TextView) findViewById(R.id.VVAGO2MDar4);


            secAGO2MDar5 = (LinearLayout) findViewById(R.id.secAGO2MDar5);
            VAGO2MDar5 = (TextView) findViewById(R.id.VAGO2MDar5);
            chkAGO2MDar5 = (CheckBox) findViewById(R.id.chkAGO2MDar5);
            chkAGO2MDar5.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationDar();

                }
            });
            VVAGO2MDar5 = (TextView) findViewById(R.id.VVAGO2MDar5);


            secAGO2MDar6 = (LinearLayout) findViewById(R.id.secAGO2MDar6);
            VAGO2MDar6 = (TextView) findViewById(R.id.VAGO2MDar6);
            chkAGO2MDar6 = (CheckBox) findViewById(R.id.chkAGO2MDar6);
            chkAGO2MDar6.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationDar();

                }
            });
            VVAGO2MDar6 = (TextView) findViewById(R.id.VVAGO2MDar6);


            secAGO2MDar7 = (LinearLayout) findViewById(R.id.secAGO2MDar7);
            VAGO2MDar7 = (TextView) findViewById(R.id.VAGO2MDar7);
            chkAGO2MDar7 = (CheckBox) findViewById(R.id.chkAGO2MDar7);
            chkAGO2MDar7.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationDar();

                }
            });
            VVAGO2MDar7 = (TextView) findViewById(R.id.VVAGO2MDar7);


            //secAGO2MDarC1 = (LinearLayout) findViewById(R.id.secAGO2MDarC1);
            VAGO2MDarC1 = (TextView) findViewById(R.id.VAGO2MDarC1);

            //  secAGO2MDarC11=(LinearLayout)findViewById(R.id.secAGO2MDarC11);
            //VAGO2MDarC11=(TextView) findViewById(R.id.VAGO2MDarC11);


            secAGO2MDarTet1 = (LinearLayout) findViewById(R.id.secAGO2MDarTet1);
            VAGO2MDarCodeTet1 = (TextView) findViewById(R.id.VAGO2MDarCodeTet1);

            chkAGO2MDarTet1 = (CheckBox) findViewById(R.id.chkAGO2MDarTet1);
            VAGO2MDarTet1 = (TextView) findViewById(R.id.VAGO2MDarTet1);


            secAGO2MDarTet2 = (LinearLayout) findViewById(R.id.secAGO2MDarTet2);
            VAGO2MDarCodeTet2 = (TextView) findViewById(R.id.VAGO2MDarCodeTet2);

            chkAGO2MDarTet2 = (CheckBox) findViewById(R.id.chkAGO2MDarTet2);
            VAGO2MDarTet2 = (TextView) findViewById(R.id.VAGO2MDarTet2);

            secAGO2MDarTet3 = (LinearLayout) findViewById(R.id.secAGO2MDarTet3);
            VAGO2MDarCodeTet3 = (TextView) findViewById(R.id.VAGO2MDarCodeTet3);

            chkAGO2MDarTet3 = (CheckBox) findViewById(R.id.chkAGO2MDarTet3);

            VAGO2MDarTet3 = (TextView) findViewById(R.id.VAGO2MDarTet3);

            //secAGO2MDarC2 = (LinearLayout) findViewById(R.id.secAGO2MDarC2);
            VAGO2MDarC2 = (TextView) findViewById(R.id.VAGO2MDarC2);


            secAGO2MDarTet4 = (LinearLayout) findViewById(R.id.secAGO2MDarTet4);
            VAGO2MDarCodeTet4 = (TextView) findViewById(R.id.VAGO2MDarCodeTet4);

            chkAGO2MDarTet4 = (CheckBox) findViewById(R.id.chkAGO2MDarTet4);
            VAGO2MDarTet4 = (TextView) findViewById(R.id.VAGO2MDarTet4);

            secAGO2MDarTet5 = (LinearLayout) findViewById(R.id.secAGO2MDarTet5);
            VAGO2MDarCodeTet5 = (TextView) findViewById(R.id.VAGO2MDarCodeTet5);

            chkAGO2MDarTet5 = (CheckBox) findViewById(R.id.chkAGO2MDarTet5);
            VAGO2MDarTet5 = (TextView) findViewById(R.id.VAGO2MDarTet5);

            secAGO2MDarTet6 = (LinearLayout) findViewById(R.id.secAGO2MDarTet6);
            VAGO2MDarCodeTet6 = (TextView) findViewById(R.id.VAGO2MDarCodeTet6);

            chkAGO2MDarTet6 = (CheckBox) findViewById(R.id.chkAGO2MDarTet6);
            VAGO2MDarTet6 = (TextView) findViewById(R.id.VAGO2MDarTet6);

            secAGO2MDarTet6a = (LinearLayout) findViewById(R.id.secAGO2MDarTet6a);
            VAGO2MDarCodeTet6a = (TextView) findViewById(R.id.VAGO2MDarCodeTet6a);

            chkAGO2MDarTet6a = (CheckBox) findViewById(R.id.chkAGO2MDarTet6a);
            VAGO2MDarTet6a = (TextView) findViewById(R.id.VAGO2MDarTet6a);

            secAGO2MFed1 = (LinearLayout) findViewById(R.id.secAGO2MFed1);
            VAGO2MFed1 = (TextView) findViewById(R.id.VAGO2MFed1);

            chkAGO2MFed1 = (CheckBox) findViewById(R.id.chkAGO2MFed1);
            chkAGO2MFed1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationFeedingProb();

                }
            });
            VVAGO2MFed1 = (TextView) findViewById(R.id.VVAGO2MFed1);

            secAGO2MFed2 = (LinearLayout) findViewById(R.id.secAGO2MFed2);
            VAGO2MFed2 = (TextView) findViewById(R.id.VAGO2MFed2);
            chkAGO2MFed2 = (CheckBox) findViewById(R.id.chkAGO2MFed2);
            chkAGO2MFed2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationFeedingProb();

                }
            });

            VVAGO2MFed2 = (TextView) findViewById(R.id.VVAGO2MFed2);


            secAGO2MFed3 = (LinearLayout) findViewById(R.id.secAGO2MFed3);
            VAGO2MFed3 = (TextView) findViewById(R.id.VAGO2MFed3);
            chkAGO2MFed3 = (CheckBox) findViewById(R.id.chkAGO2MFed3);
            chkAGO2MFed3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationFeedingProb();

                }
            });
            VVAGO2MFed3 = (TextView) findViewById(R.id.VVAGO2MFed3);

            secAGO2MFed4 = (LinearLayout) findViewById(R.id.secAGO2MFed4);
            VAGO2MFed4 = (TextView) findViewById(R.id.VAGO2MFed4);
            chkAGO2MFed4 = (CheckBox) findViewById(R.id.chkAGO2MFed4);
            chkAGO2MFed4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationFeedingProb();

                }
            });
            VVAGO2MFed4 = (TextView) findViewById(R.id.VVAGO2MFed4);

            secAGO2MFed5 = (LinearLayout) findViewById(R.id.secAGO2MFed5);
            VAGO2MFed5 = (TextView) findViewById(R.id.VAGO2MFed5);
            chkAGO2MFed5 = (CheckBox) findViewById(R.id.chkAGO2MFed5);
            chkAGO2MFed5.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationFeedingProb();

                }
            });
            VVAGO2MFed5 = (TextView) findViewById(R.id.VVAGO2MFed5);

            secAGO2MFed6 = (LinearLayout) findViewById(R.id.secAGO2MFed6);
            VAGO2MFed6 = (TextView) findViewById(R.id.VAGO2MFed6);
            chkAGO2MFed6 = (CheckBox) findViewById(R.id.chkAGO2MFed6);
            chkAGO2MFed6.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationFeedingProb();

                }
            });
            VVAGO2MFed6 = (TextView) findViewById(R.id.VVAGO2MFed6);


            //secAGO2MFedC1 = (LinearLayout) findViewById(R.id.secAGO2MFedC1);
            VAGO2MFedC1 = (TextView) findViewById(R.id.VAGO2MFedC1);


            secAGO2MFedTet1 = (LinearLayout) findViewById(R.id.secAGO2MFedTet1);
            VAGO2MFedCodeTet1 = (TextView) findViewById(R.id.VAGO2MFedCodeTet1);

            chkAGO2MFedTet1 = (CheckBox) findViewById(R.id.chkAGO2MFedTet1);
            VAGO2MFedTet1 = (TextView) findViewById(R.id.VAGO2MFedTet1);

            secAGO2MFedTet2 = (LinearLayout) findViewById(R.id.secAGO2MFedTet2);
            VAGO2MFedCodeTet2 = (TextView) findViewById(R.id.VAGO2MFedCodeTet2);

            chkAGO2MFedTet2 = (CheckBox) findViewById(R.id.chkAGO2MFedTet2);
            VAGO2MFedTet2 = (TextView) findViewById(R.id.VAGO2MFedTet2);

            secAGO2MFedTet3 = (LinearLayout) findViewById(R.id.secAGO2MFedTet3);
            VAGO2MFedCodeTet3 = (TextView) findViewById(R.id.VAGO2MFedCodeTet3);

            chkAGO2MFedTet3 = (CheckBox) findViewById(R.id.chkAGO2MFedTet3);
            VAGO2MFedTet3 = (TextView) findViewById(R.id.VAGO2MFedTet3);

            secAGO2MFedTet4 = (LinearLayout) findViewById(R.id.secAGO2MFedTet4);
            VAGO2MFedCodeTet4 = (TextView) findViewById(R.id.VAGO2MFedCodeTet4);

            chkAGO2MFedTet4 = (CheckBox) findViewById(R.id.chkAGO2MFedTet4);
            VAGO2MFedTet4 = (TextView) findViewById(R.id.VAGO2MFedTet4);

            secAGO2MFedTet5 = (LinearLayout) findViewById(R.id.secAGO2MFedTet5);
            VAGO2MFedCodeTet5 = (TextView) findViewById(R.id.VAGO2MFedCodeTet5);

            chkAGO2MFedTet5 = (CheckBox) findViewById(R.id.chkAGO2MFedTet5);
            VAGO2MFedTet5 = (TextView) findViewById(R.id.VAGO2MFedTet5);


            //secAGO2MFedC2 = (LinearLayout) findViewById(R.id.secAGO2MFedC2);
            VAGO2MFedC2 = (TextView) findViewById(R.id.VAGO2MFedC2);


            secAGO2MFedTet6 = (LinearLayout) findViewById(R.id.secAGO2MFedTet6);
            VAGO2MFedCodeTet6 = (TextView) findViewById(R.id.VAGO2MFedCodeTet6);

            chkAGO2MFedTet6 = (CheckBox) findViewById(R.id.chkAGO2MFedTet6);
            VAGO2MFedTet6 = (TextView) findViewById(R.id.VAGO2MFedTet6);

            secAGO2MFedTet7 = (LinearLayout) findViewById(R.id.secAGO2MFedTet7);
            VAGO2MFedCodeTet7 = (TextView) findViewById(R.id.VAGO2MFedCodeTet7);
            chkAGO2MFedTet7 = (CheckBox) findViewById(R.id.chkAGO2MFedTet7);
            VAGO2MFedTet7 = (TextView) findViewById(R.id.VAGO2MFedTet7);

            secAGO2MGA = (LinearLayout) findViewById(R.id.secAGO2MGA);


            secAG2to5Nu = (LinearLayout) findViewById(R.id.secAG2to5Nu);

            VAAG25YNu1 = (TextView) findViewById(R.id.VAAG25YNu1);


            // secAG25YNu2 = (LinearLayout) findViewById(R.id.secAG25YNu2);
            VAAG25YNu2 = (TextView) findViewById(R.id.VAAG25YNu2);

            //  secAG25YNu3 = (LinearLayout) findViewById(R.id.secAG25YNu3);
            VAAG25YNu3 = (TextView) findViewById(R.id.VAAG25YNu3);


            secAG25YSym1 = (LinearLayout) findViewById(R.id.secAG25YSym1);
            VAG25YSymCode1 = (TextView) findViewById(R.id.VAG25YSymCode1);
            chkAG25YSym1 = (CheckBox) findViewById(R.id.chkAG25YSym1);
            VVAG25YSym1 = (TextView) findViewById(R.id.VVAG25YSym1);


            chkAG25YSym1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationFeedingAG25YNum();

                }
            });

            secAG25YSym2 = (LinearLayout) findViewById(R.id.secAG25YSym2);
            VAG25YSymCode2 = (TextView) findViewById(R.id.VAG25YSymCode2);
            chkAG25YSym2 = (CheckBox) findViewById(R.id.chkAG25YSym2);
            VVAG25YSym2 = (TextView) findViewById(R.id.VVAG25YSym2);

            chkAG25YSym2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationFeedingAG25YNum();

                }
            });
            secAG25YSym3 = (LinearLayout) findViewById(R.id.secAG25YSym3);
            VAG25YSymCode3 = (TextView) findViewById(R.id.VAG25YSymCode3);
            chkAG25YSym3 = (CheckBox) findViewById(R.id.chkAG25YSym3);
            VVAG25YSym3 = (TextView) findViewById(R.id.VVAG25YSym3);


            chkAG25YSym3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationFeedingAG25YNum();

                }
            });
            secAG25YSym4 = (LinearLayout) findViewById(R.id.secAG25YSym4);
            VAG25YSymCode4 = (TextView) findViewById(R.id.VAG25YSymCode4);
            chkAG25YSym4 = (CheckBox) findViewById(R.id.chkAG25YSym4);
            VVAG25YSym4 = (TextView) findViewById(R.id.VVAG25YSym4);


            chkAG25YSym4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationFeedingAG25YNum();

                }
            });
            secAG25YTet1 = (LinearLayout) findViewById(R.id.secAG25YTet1);
            VAG25YTetCode1 = (TextView) findViewById(R.id.VAG25YTetCode1);
            chkAG25YTet1 = (CheckBox) findViewById(R.id.chkAG25YTet1);
            VVAG25YTet1 = (TextView) findViewById(R.id.VVAG25YTet1);
            chkAG25YTet1.setEnabled(false);

            secAG25YTet2 = (LinearLayout) findViewById(R.id.secAG25YTet2);
            VAG25YTetCode2 = (TextView) findViewById(R.id.VAG25YTetCode2);
            chkAG25YTet2 = (CheckBox) findViewById(R.id.chkAG25YTet2);
            VVAG25YTet2 = (TextView) findViewById(R.id.VVAG25YTet2);

            secAG25YTet3 = (LinearLayout) findViewById(R.id.secAG25YTet3);
            VAG25YTetCode3 = (TextView) findViewById(R.id.VAG25YTetCode3);
            chkAG25YTet3 = (CheckBox) findViewById(R.id.chkAG25YTet3);
            chkAG25YTet3.setEnabled(false);
            VVAG25YTet3 = (TextView) findViewById(R.id.VVAG25YTet3);

            secAG25YTet4 = (LinearLayout) findViewById(R.id.secAG25YTet4);
            VAG25YTetCode4 = (TextView) findViewById(R.id.VAG25YTetCode4);
            chkAG25YTet4 = (CheckBox) findViewById(R.id.chkAG25YTet4);
            VVAG25YTet4 = (TextView) findViewById(R.id.VVAG25YTet4);

            secAG25YTet5 = (LinearLayout) findViewById(R.id.secAG25YTet5);
            VAG25YTetCode5 = (TextView) findViewById(R.id.VAG25YTetCode5);
            chkAG25YTet5 = (CheckBox) findViewById(R.id.chkAG25YTet5);
            VVAG25YTet5 = (TextView) findViewById(R.id.VVAG25YTet5);

            secAG25YTet5a = (LinearLayout) findViewById(R.id.secAG25YTet5a);
            VAG25YTetCode5a = (TextView) findViewById(R.id.VAG25YTetCode5a);
            chkAG25YTet5a = (CheckBox) findViewById(R.id.chkAG25YTet5a);
            VVAG25YTet5a = (TextView) findViewById(R.id.VVAG25YTet5a);

            secAG25YTet6 = (LinearLayout) findViewById(R.id.secAG25YTet6);
            VAG25YTetCode6 = (TextView) findViewById(R.id.VAG25YTetCode6);
            chkAG25YTet6 = (CheckBox) findViewById(R.id.chkAG25YTet6);
            VVAG25YTet6 = (TextView) findViewById(R.id.VVAG25YTet6);

            secAG25YTet7 = (LinearLayout) findViewById(R.id.secAG25YTet7);
            VAG25YTetCode7 = (TextView) findViewById(R.id.VAG25YTetCode7);
            chkAG25YTet7 = (CheckBox) findViewById(R.id.chkAG25YTet7);
            VVAG25YTet7 = (TextView) findViewById(R.id.VVAG25YTet7);

            //  secAG25YNuSymtom1=(LinearLayout)findViewById(R.id.secAG25YNuSymtom1);
            secAG25YNuClass1 = (LinearLayout) findViewById(R.id.secAG25YNuClass1);
            secAG25YNuClass2 = (LinearLayout) findViewById(R.id.secAG25YNuClass2);
            secAG25YNuClass3 = (LinearLayout) findViewById(R.id.secAG25YNuClass3);

            secAG25YDar1 = (LinearLayout) findViewById(R.id.secAG25YDar1);
            VAG25YDarCode1 = (TextView) findViewById(R.id.VAG25YDarCode1);
            chkAG25YDar1 = (CheckBox) findViewById(R.id.chkAG25YDar1);

            VVAG25YDar1 = (TextView) findViewById(R.id.VVAG25YDar1);

            chkAG25YDar1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationFeedingAG25YDar();

                }
            });
            secAG25YDar2 = (LinearLayout) findViewById(R.id.secAG25YDar2);
            VAG25YDarCode2 = (TextView) findViewById(R.id.VAG25YDarCode2);
            chkAG25YDar2 = (CheckBox) findViewById(R.id.chkAG25YDar2);
            chkAG25YDar2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationFeedingAG25YDar();

                }
            });
            VVAG25YDar2 = (TextView) findViewById(R.id.VVAG25YDar2);

            secAG25YDar3 = (LinearLayout) findViewById(R.id.secAG25YDar3);
            VAG25YDarCode3 = (TextView) findViewById(R.id.VAG25YDarCode3);
            chkAG25YDar3 = (CheckBox) findViewById(R.id.chkAG25YDar3);
            chkAG25YDar3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationFeedingAG25YDar();

                }
            });
            VVAG25YDar3 = (TextView) findViewById(R.id.VVAG25YDar3);

            secAG25YDar4 = (LinearLayout) findViewById(R.id.secAG25YDar4);
            VAG25YDarCode4 = (TextView) findViewById(R.id.VAG25YDarCode4);
            chkAG25YDar4 = (CheckBox) findViewById(R.id.chkAG25YDar4);
            chkAG25YDar4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationFeedingAG25YDar();

                }
            });
            VVAG25YDar4 = (TextView) findViewById(R.id.VVAG25YDar4);

            secAG25YDar5 = (LinearLayout) findViewById(R.id.secAG25YDar5);
            VAG25YDarCode5 = (TextView) findViewById(R.id.VAG25YDarCode5);
            chkAG25YDar5 = (CheckBox) findViewById(R.id.chkAG25YDar5);

            VVAG25YDar5 = (TextView) findViewById(R.id.VVAG25YDar5);
            chkAG25YDar5.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationFeedingAG25YDar();

                }
            });
            secAG25YDar6 = (LinearLayout) findViewById(R.id.secAG25YDar6);
            VAG25YDarCode6 = (TextView) findViewById(R.id.VAG25YDarCode6);
            chkAG25YDar6 = (CheckBox) findViewById(R.id.chkAG25YDar6);
            VVAG25YDar6 = (TextView) findViewById(R.id.VVAG25YDar6);

            chkAG25YDar6.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationFeedingAG25YDar();

                }
            });
            secAG25YDar7 = (LinearLayout) findViewById(R.id.secAG25YDar7);
            VAG25YDarCode7 = (TextView) findViewById(R.id.VAG25YDarCode7);
            chkAG25YDar7 = (CheckBox) findViewById(R.id.chkAG25YDar7);
            VVAG25YDar7 = (TextView) findViewById(R.id.VVAG25YDar7);

            chkAG25YDar7.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationFeedingAG25YDar();

                }
            });
            secAG25YDar8 = (LinearLayout) findViewById(R.id.secAG25YDar8);
            VAG25YDarCode8 = (TextView) findViewById(R.id.VAG25YDarCode8);
            chkAG25YDar8 = (CheckBox) findViewById(R.id.chkAG25YDar8);
            VVAG25YDar8 = (TextView) findViewById(R.id.VVAG25YDar8);

            chkAG25YDar8.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationFeedingAG25YDar();

                }
            });

            secAG25YDar9 = (LinearLayout) findViewById(R.id.secAG25YDar9);
            VAG25YDarCode9 = (TextView) findViewById(R.id.VAG25YDarCode9);
            chkAG25YDar9 = (CheckBox) findViewById(R.id.chkAG25YDar9);
            VVAG25YDar9 = (TextView) findViewById(R.id.VVAG25YDar9);

            chkAG25YDar9.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationFeedingAG25YDar();

                }
            });
   /*     chkAG25YDar7.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    ClassificationDar();

                }
            });*/


            secAG25YDarTetClass1 = (LinearLayout) findViewById(R.id.secAG25YDarTetClass1);
            secAG25YDarTetClass2 = (LinearLayout) findViewById(R.id.secAG25YDarTetClass2);
            secAG25YDarTetClass3 = (LinearLayout) findViewById(R.id.secAG25YDarTetClass3);

            //secAG25YDarC1 = (LinearLayout) findViewById(R.id.secAG25YDarC1);
            VAG25YDarC1 = (TextView) findViewById(R.id.VAG25YDarC1);


            secAG25YDarTet1 = (LinearLayout) findViewById(R.id.secAG25YDarTet1);
            VAG25YDarCodeTet1 = (TextView) findViewById(R.id.VAG25YDarCodeTet1);
            chkAG25YDarTet1 = (CheckBox) findViewById(R.id.chkAG25YDarTet1);
            VAG25YDarTet1 = (TextView) findViewById(R.id.VAG25YDarTet1);

            // secAG25YDarC2 = (LinearLayout) findViewById(R.id.secAG25YDarC2);
            VAG25YDarC2 = (TextView) findViewById(R.id.VAG25YDarC2);


            secAG25YDarTet2 = (LinearLayout) findViewById(R.id.secAG25YDarTet2);
            VAG25YDarCodeTet2 = (TextView) findViewById(R.id.VAG25YDarCodeTet2);

            chkAG25YDarTet2 = (CheckBox) findViewById(R.id.chkAG25YDarTet2);
            VAG25YDarTet2 = (TextView) findViewById(R.id.VAG25YDarTet2);

            secAG25YDarTet3 = (LinearLayout) findViewById(R.id.secAG25YDarTet3);
            VAG25YDarCodeTet3 = (TextView) findViewById(R.id.VAG25YDarCodeTet3);
            chkAG25YDarTet3 = (CheckBox) findViewById(R.id.chkAG25YDarTet3);
            VAG25YDarTet3 = (TextView) findViewById(R.id.VAG25YDarTet3);


            secAG25YDarTet3a = (LinearLayout) findViewById(R.id.secAG25YDarTet3a);
            VAG25YDarCodeTet3a = (TextView) findViewById(R.id.VAG25YDarCodeTet3a);
            chkAG25YDarTet3a = (CheckBox) findViewById(R.id.chkAG25YDarTet3a);
            VAG25YDarTet3a = (TextView) findViewById(R.id.VAG25YDarTet3a);

            // secAG25YDarC3 = (LinearLayout) findViewById(R.id.secAG25YDarC3);
            VAG25YDarC3 = (TextView) findViewById(R.id.VAG25YDarC3);


            secAG25YDarTet4 = (LinearLayout) findViewById(R.id.secAG25YDarTet4);
            VAG25YDarCodeTet4 = (TextView) findViewById(R.id.VAG25YDarCodeTet4);

            chkAG25YDarTet4 = (CheckBox) findViewById(R.id.chkAG25YDarTet4);
            VAG25YDarTet4 = (TextView) findViewById(R.id.VAG25YDarTet4);

            secAG25YDarTet5 = (LinearLayout) findViewById(R.id.secAG25YDarTet5);
            VAG25YDarCodeTet5 = (TextView) findViewById(R.id.VAG25YDarCodeTet5);

            chkAG25YDarTet5 = (CheckBox) findViewById(R.id.chkAG25YDarTet5);
            VAG25YDarTet5 = (TextView) findViewById(R.id.VAG25YDarTet5);

            secAG25YDarTet6 = (LinearLayout) findViewById(R.id.secAG25YDarTet6);
            VAG25YDarCodeTet6 = (TextView) findViewById(R.id.VAG25YDarCodeTet6);
            chkAG25YDarTet6 = (CheckBox) findViewById(R.id.chkAG25YDarTet6);
            VAG25YDarTet6 = (TextView) findViewById(R.id.VAG25YDarTet6);

            secAG25YDarTet6a = (LinearLayout) findViewById(R.id.secAG25YDarTet6a);
            VAG25YDarCodeTet6a = (TextView) findViewById(R.id.VAG25YDarCodeTet6a);
            chkAG25YDarTet6a = (CheckBox) findViewById(R.id.chkAG25YDarTet6a);
            VAG25YDarTet6a = (TextView) findViewById(R.id.VAG25YDarTet6a);


            secAG25YAm1 = (LinearLayout) findViewById(R.id.secAG25YAm1);
            VAG25YAmCode1 = (TextView) findViewById(R.id.VAG25YAmCode1);
            chkAG25YAm1 = (CheckBox) findViewById(R.id.chkAG25YAm1);
            VVAG25YAm1 = (TextView) findViewById(R.id.VVAG25YAm1);
            chkAG25YAm1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationFeedingAG25YDar();

                }
            });

            secAG25YAmSymtom = (LinearLayout) findViewById(R.id.secAG25YAmSymtom);
            //  secAG25YAmC1 = (LinearLayout) findViewById(R.id.secAG25YAmC1);
            VAG25YAmC1 = (TextView) findViewById(R.id.VAG25YAmC1);

            secAG25YAmTet1 = (LinearLayout) findViewById(R.id.secAG25YAmTet1);
            VAG25YAmCodeTet1 = (TextView) findViewById(R.id.VAG25YAmCodeTet1);
            chkAG25YamTet1 = (CheckBox) findViewById(R.id.chkAG25YamTet1);
            VAG25YAmTet1 = (TextView) findViewById(R.id.VAG25YAmTet1);
            // chkAG25YamTet1.setEnabled(false);
            secAG25YAmTet2 = (LinearLayout) findViewById(R.id.secAG25YAmTet2);
            VAG25YAmCodeTet2 = (TextView) findViewById(R.id.VAG25YAmCodeTet2);
            chkAG25YamTet2 = (CheckBox) findViewById(R.id.chkAG25YamTet2);
            VAG25YAmTet2 = (TextView) findViewById(R.id.VAG25YAmTet2);

            secAG25YAmTet2a = (LinearLayout) findViewById(R.id.secAG25YAmTet2a);
            VAG25YAmCodeTet2a = (TextView) findViewById(R.id.VAG25YAmCodeTet2a);
            chkAG25YamTet2a = (CheckBox) findViewById(R.id.chkAG25YamTet2a);
            VAG25YAmTet2a = (TextView) findViewById(R.id.VAG25YAmTet2a);


            secAG25YFev1 = (LinearLayout) findViewById(R.id.secAG25YFev1);
            VAG25YFevCode1 = (TextView) findViewById(R.id.VAG25YFevCode1);
            chkAG25YFev1 = (CheckBox) findViewById(R.id.chkAG25YFev1);
            VVAG25YFev1 = (TextView) findViewById(R.id.VVAG25YFev1);

            chkAG25YFev1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationFeedingAG25YFever();

                }
            });


            secAG25YFev2 = (LinearLayout) findViewById(R.id.secAG25YFev2);
            VAG25YFevCode2 = (TextView) findViewById(R.id.VAG25YFevCode2);
            chkAG25YFev2 = (CheckBox) findViewById(R.id.chkAG25YFev2);
            VVAG25YFev2 = (TextView) findViewById(R.id.VVAG25YFev2);


            chkAG25YFev2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationFeedingAG25YFever();

                }
            });
            secAG25YFevSymtom = (LinearLayout) findViewById(R.id.secAG25YFevSymtom);
            //secAG25YFevC1 = (LinearLayout) findViewById(R.id.secAG25YFevC1);
            VAG25YFevC1 = (TextView) findViewById(R.id.VAG25YFevC1);

            secAG25YFevTet1 = (LinearLayout) findViewById(R.id.secAG25YFevTet1);
            VAG25YFevCodeTet1 = (TextView) findViewById(R.id.VAG25YFevCodeTet1);
            chkAG25YFevTet1 = (CheckBox) findViewById(R.id.chkAG25YFevTet1);
            VAG25YFevTet1 = (TextView) findViewById(R.id.VAG25YFevTet1);

            secAG25YFevTet2 = (LinearLayout) findViewById(R.id.secAG25YFevTet2);
            VAG25YFevCodeTet2 = (TextView) findViewById(R.id.VAG25YFevCodeTet2);
            chkAG25YFevTet2 = (CheckBox) findViewById(R.id.chkAG25YFevTet2);
            VAG25YFevTet2 = (TextView) findViewById(R.id.VAG25YFevTet2);


            secAG25YFevTet3 = (LinearLayout) findViewById(R.id.secAG25YFevTet3);
            VAG25YFevCodeTet3 = (TextView) findViewById(R.id.VAG25YFevCodeTet3);
            chkAG25YFevTet3 = (CheckBox) findViewById(R.id.chkAG25YFevTet3);
            VAG25YFevTet3 = (TextView) findViewById(R.id.VAG25YFevTet3);

            secAG25YFevTet4 = (LinearLayout) findViewById(R.id.secAG25YFevTet4);
            VAG25YFevCodeTet4 = (TextView) findViewById(R.id.VAG25YFevCodeTet4);
            chkAG25YFevTet4 = (CheckBox) findViewById(R.id.chkAG25YFevTet4);
            VAG25YFevTet4 = (TextView) findViewById(R.id.VAG25YFevTet4);


            secAG25YFevTet4a = (LinearLayout) findViewById(R.id.secAG25YFevTet4a);
            VAG25YFevCodeTet4a = (TextView) findViewById(R.id.VAG25YFevCodeTet4a);
            chkAG25YFevTet4a = (CheckBox) findViewById(R.id.chkAG25YFevTet4a);
            VAG25YFevTet4a = (TextView) findViewById(R.id.VAG25YFevTet4a);


            secAG25YMal1 = (LinearLayout) findViewById(R.id.secAG25YMal1);
            VAG25YMalCode1 = (TextView) findViewById(R.id.VAG25YMalCode1);
            chkAG25YMal1 = (CheckBox) findViewById(R.id.chkAG25YMal1);
            VVAG25YMal1 = (TextView) findViewById(R.id.VVAG25YMal1);

            chkAG25YMal1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationFeedingAG25YFever();

                }
            });


            secAG25YMal2 = (LinearLayout) findViewById(R.id.secAG25YMal2);
            VAG25YMalCode2 = (TextView) findViewById(R.id.VAG25YMalCode2);
            chkAG25YMal2 = (CheckBox) findViewById(R.id.chkAG25YMal2);
            VVAG25YMal2 = (TextView) findViewById(R.id.VVAG25YMal2);

            chkAG25YMal2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationFeedingAG25YFever();

                }
            });
            secAG25YMalSymtom1 = (LinearLayout) findViewById(R.id.secAG25YMalSymtom1);
            //secAG25YMalC1 = (LinearLayout) findViewById(R.id.secAG25YMalC1);
            VAG25YMalC1 = (TextView) findViewById(R.id.VAG25YMalC1);

            secAG25YMalSymtom2 = (LinearLayout) findViewById(R.id.secAG25YMalSymtom2);
            //secAG25YMalC2 = (LinearLayout) findViewById(R.id.secAG25YMalC2);
            VAG25YMalC2 = (TextView) findViewById(R.id.VAG25YMalC2);


            secAG25YMalTet1 = (LinearLayout) findViewById(R.id.secAG25YMalTet1);
            VAG25YMalCodeTet1 = (TextView) findViewById(R.id.VAG25YMalCodeTet1);
            chkAG25YMalTet1 = (CheckBox) findViewById(R.id.chkAG25YMalTet1);
            VAG25YMalTet1 = (TextView) findViewById(R.id.VAG25YMalTet1);

            secAG25YMalTet2 = (LinearLayout) findViewById(R.id.secAG25YMalTet2);
            VAG25YMalCodeTet2 = (TextView) findViewById(R.id.VAG25YMalCodeTet2);
            chkAG25YMalTet2 = (CheckBox) findViewById(R.id.chkAG25YMalTet2);
            VAG25YMalTet2 = (TextView) findViewById(R.id.VAG25YMalTet2);

            secAG25YMalTet2a = (LinearLayout) findViewById(R.id.secAG25YMalTet2a);
            VAG25YMalCodeTet2a = (TextView) findViewById(R.id.VAG25YMalCodeTet2a);
            chkAG25YMalTet2a = (CheckBox) findViewById(R.id.chkAG25YMalTet2a);
            VAG25YMalTet2a = (TextView) findViewById(R.id.VAG25YMalTet2a);

            secAG25YMalTet3 = (LinearLayout) findViewById(R.id.secAG25YMalTet3);
            VAG25YMalCodeTet3 = (TextView) findViewById(R.id.VAG25YMalCodeTet3);
            chkAG25YMalTet3 = (CheckBox) findViewById(R.id.chkAG25YMalTet3);
            VAG25YMalTet3 = (TextView) findViewById(R.id.VAG25YMalTet3);

            secAG25YMalTet4 = (LinearLayout) findViewById(R.id.secAG25YMalTet4);
            VAG25YMalCodeTet4 = (TextView) findViewById(R.id.VAG25YMalCodeTet4);
            chkAG25YMalTet4 = (CheckBox) findViewById(R.id.chkAG25YMalTet4);
            VAG25YMalTet4 = (TextView) findViewById(R.id.VAG25YMalTet4);

            secAG25YMalTet5 = (LinearLayout) findViewById(R.id.secAG25YMalTet5);
            VAG25YMalCodeTet5 = (TextView) findViewById(R.id.VAG25YMalCodeTet5);
            chkAG25YMalTet5 = (CheckBox) findViewById(R.id.chkAG25YMalTet5);
            VAG25YMalTet5 = (TextView) findViewById(R.id.VAG25YMalTet5);

            secAG25YMalTet6 = (LinearLayout) findViewById(R.id.secAG25YMalTet6);
            VAG25YMalCodeTet6 = (TextView) findViewById(R.id.VAG25YMalCodeTet6);
            chkAG25YMalTet6 = (CheckBox) findViewById(R.id.chkAG25YMalTet6);
            VAG25YMalTet6 = (TextView) findViewById(R.id.VAG25YMalTet6);

            secAG25YMalTet7 = (LinearLayout) findViewById(R.id.secAG25YMalTet7);
            VAG25YMalCodeTet7 = (TextView) findViewById(R.id.VAG25YMalCodeTet7);
            chkAG25YMalTet7 = (CheckBox) findViewById(R.id.chkAG25YMalTet7);
            VAG25YMalTet7 = (TextView) findViewById(R.id.VAG25YMalTet7);

            secAG25YMalTet8 = (LinearLayout) findViewById(R.id.secAG25YMalTet8);
            VAG25YMalCodeTet8 = (TextView) findViewById(R.id.VAG25YMalCodeTet8);
            chkAG25YMalTet8 = (CheckBox) findViewById(R.id.chkAG25YMalTet8);
            VAG25YMalTet8 = (TextView) findViewById(R.id.VAG25YMalTet8);


            secAG25YMalTet8a = (LinearLayout) findViewById(R.id.secAG25YMalTet8a);
            VAG25YMalCodeTet8a = (TextView) findViewById(R.id.VAG25YMalCodeTet8a);
            chkAG25YMalTet8a = (CheckBox) findViewById(R.id.chkAG25YMalTet8a);
            VAG25YMalTet8a = (TextView) findViewById(R.id.VAG25YMalTet8a);

            secAG25YNut1 = (LinearLayout) findViewById(R.id.secAG25YNut1);
            VAG25YNutCode1 = (TextView) findViewById(R.id.VAG25YNutCode1);
            chkAG25YNut1 = (CheckBox) findViewById(R.id.chkAG25YNut1);
            VVAG25YNut1 = (TextView) findViewById(R.id.VVAG25YNut1);

            chkAG25YNut1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationFeedingAG25YNut();

                }
            });


            secAG25YNut2 = (LinearLayout) findViewById(R.id.secAG25YNut2);
            VAG25YNutCode2 = (TextView) findViewById(R.id.VAG25YNutCode2);
            chkAG25YNut2 = (CheckBox) findViewById(R.id.chkAG25YNut2);
            VVAG25YNut2 = (TextView) findViewById(R.id.VVAG25YNut2);

            chkAG25YNut2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationFeedingAG25YNut();

                }
            });

            secAG25YNut3 = (LinearLayout) findViewById(R.id.secAG25YNut3);
            VAG25YNutCode3 = (TextView) findViewById(R.id.VAG25YNutCode3);
            chkAG25YNut3 = (CheckBox) findViewById(R.id.chkAG25YNut3);
            VVAG25YNut3 = (TextView) findViewById(R.id.VVAG25YNut3);

            chkAG25YNut3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationFeedingAG25YNut();

                }
            });

            secAG25YNut4 = (LinearLayout) findViewById(R.id.secAG25YNut4);
            VAG25YNutCode4 = (TextView) findViewById(R.id.VAG25YNutCode4);
            chkAG25YNut4 = (CheckBox) findViewById(R.id.chkAG25YNut4);
            VVAG25YNut4 = (TextView) findViewById(R.id.VVAG25YNut4);

            chkAG25YNut4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationFeedingAG25YNut();

                }
            });
            //   secAG25YNutSymtom=(LinearLayout)findViewById(R.id.secAG25YNutSymtom);
            secAG25YNutSymtom1 = (LinearLayout) findViewById(R.id.secAG25YNutSymtom1);
            //secAG25YNutC1 = (LinearLayout) findViewById(R.id.secAG25YNutC1);
            VAG25YNutC1 = (TextView) findViewById(R.id.VAG25YNutC1);

            secAG25YNutSymtom2 = (LinearLayout) findViewById(R.id.secAG25YNutSymtom2);
            //secAG25YNutC2 = (LinearLayout) findViewById(R.id.secAG25YNutC2);
            VAG25YNutC2 = (TextView) findViewById(R.id.VAG25YNutC2);


            secAG25YNutSymtom3 = (LinearLayout) findViewById(R.id.secAG25YNutSymtom3);
            //secAG25YNutC3 = (LinearLayout) findViewById(R.id.secAG25YNutC3);
            VAG25YNutC3 = (TextView) findViewById(R.id.VAG25YNutC3);


            secAG25YNutTet1 = (LinearLayout) findViewById(R.id.secAG25YNutTet1);
            VAG25YNutCodeTet1 = (TextView) findViewById(R.id.VAG25YNutCodeTet1);
            chkAG25YNutTet1 = (CheckBox) findViewById(R.id.chkAG25YNutTet1);
            VAG25YNutTet1 = (TextView) findViewById(R.id.VAG25YNutTet1);

            secAG25YNutTet2 = (LinearLayout) findViewById(R.id.secAG25YNutTet2);
            VAG25YNutCodeTet2 = (TextView) findViewById(R.id.VAG25YNutCodeTet2);
            chkAG25YNutTet2 = (CheckBox) findViewById(R.id.chkAG25YNutTet2);
            VAG25YNutTet2 = (TextView) findViewById(R.id.VAG25YNutTet2);

            secAG25YNutTet3 = (LinearLayout) findViewById(R.id.secAG25YNutTet3);
            VAG25YNutCodeTet3 = (TextView) findViewById(R.id.VAG25YNutCodeTet3);
            chkAG25YNutTet3 = (CheckBox) findViewById(R.id.chkAG25YNutTet3);
            VAG25YNutTet3 = (TextView) findViewById(R.id.VAG25YNutTet3);

            secAG25YNutTet4 = (LinearLayout) findViewById(R.id.secAG25YNutTet4);
            VAG25YNutCodeTet4 = (TextView) findViewById(R.id.VAG25YNutCodeTet4);
            chkAG25YNutTet4 = (CheckBox) findViewById(R.id.chkAG25YNutTet4);
            VAG25YNutTet4 = (TextView) findViewById(R.id.VAG25YNutTet4);

            secAG25YNutTet5 = (LinearLayout) findViewById(R.id.secAG25YNutTet5);
            VAG25YNutCodeTet5 = (TextView) findViewById(R.id.VAG25YNutCodeTet5);
            chkAG25YNutTet5 = (CheckBox) findViewById(R.id.chkAG25YNutTet5);
            VAG25YNutTet5 = (TextView) findViewById(R.id.VAG25YNutTet5);

            secAG25YNutTet5a = (LinearLayout) findViewById(R.id.secAG25YNutTet5a);
            VAG25YNutCodeTet5a = (TextView) findViewById(R.id.VAG25YNutCodeTet5a);
            chkAG25YNutTet5a = (CheckBox) findViewById(R.id.chkAG25YNutTet5a);
            VAG25YNutTet5a = (TextView) findViewById(R.id.VAG25YNutTet5a);

            secAG25YNutTet6 = (LinearLayout) findViewById(R.id.secAG25YNutTet6);
            VAG25YNutCodeTet6 = (TextView) findViewById(R.id.VAG25YNutCodeTet6);
            chkAG25YNutTet6 = (CheckBox) findViewById(R.id.chkAG25YNutTet6);
            VAG25YNutTet6 = (TextView) findViewById(R.id.VAG25YNutTet6);

            secAG25YNutTet7 = (LinearLayout) findViewById(R.id.secAG25YNutTet7);
            VAG25YNutCodeTet7 = (TextView) findViewById(R.id.VAG25YNutCodeTet7);
            chkAG25YNutTet7 = (CheckBox) findViewById(R.id.chkAG25YNutTet7);
            VAG25YNutTet7 = (TextView) findViewById(R.id.VAG25YNutTet7);

            secAG25YNutTet7a = (LinearLayout) findViewById(R.id.secAG25YNutTet7a);
            VAG25YNutCodeTet7a = (TextView) findViewById(R.id.VAG25YNutCodeTet7a);
            chkAG25YNutTet7a = (CheckBox) findViewById(R.id.chkAG25YNutTet7a);
            VAG25YNutTet7a = (TextView) findViewById(R.id.VAG25YNutTet7a);

            secAG25YLow1 = (LinearLayout) findViewById(R.id.secAG25YLow1);
            VAG25YLowCode1 = (TextView) findViewById(R.id.VAG25YLowCode1);
            chkAG25YLow1 = (CheckBox) findViewById(R.id.chkAG25YLow1);
            VVAG25YLow1 = (TextView) findViewById(R.id.VVAG25YLow1);

            chkAG25YLow1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationFeedingAG25YLow();

                }
            });


            secAG25YLow2 = (LinearLayout) findViewById(R.id.secAG25YLow2);
            VAG25YLowCode2 = (TextView) findViewById(R.id.VAG25YLowCode2);
            chkAG25YLow2 = (CheckBox) findViewById(R.id.chkAG25YLow2);
            VVAG25YLow2 = (TextView) findViewById(R.id.VVAG25YLow2);

            chkAG25YLow2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationFeedingAG25YLow();

                }
            });

            secAG25YLow3 = (LinearLayout) findViewById(R.id.secAG25YLow3);
            VAG25YLowCode3 = (TextView) findViewById(R.id.VAG25YLowCode3);
            chkAG25YLow3 = (CheckBox) findViewById(R.id.chkAG25YLow3);
            VVAG25YLow3 = (TextView) findViewById(R.id.VVAG25YLow3);

            chkAG25YLow3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationFeedingAG25YLow();

                }
            });


            secAG25YLowSymtom1 = (LinearLayout) findViewById(R.id.secAG25YLowSymtom1);
            //secAG25YLowC1 = (LinearLayout) findViewById(R.id.secAG25YLowC1);
            VAG25YLowC1 = (TextView) findViewById(R.id.VAG25YLowC1);

            secAG25YLowSymtom2 = (LinearLayout) findViewById(R.id.secAG25YLowSymtom2);
            // secAG25YLowC2 = (LinearLayout) findViewById(R.id.secAG25YLowC2);
            VAG25YLowC2 = (TextView) findViewById(R.id.VAG25YLowC2);


            secAG25YLowSymtom3 = (LinearLayout) findViewById(R.id.secAG25YLowSymtom3);
            //secAG25YLowC3 = (LinearLayout) findViewById(R.id.secAG25YLowC3);
            VAG25YLowC3 = (TextView) findViewById(R.id.VAG25YLowC3);


            secAG25YLowTet1 = (LinearLayout) findViewById(R.id.secAG25YLowTet1);
            VAG25YLowCodeTet1 = (TextView) findViewById(R.id.VAG25YLowCodeTet1);
            chkAG25YLowTet1 = (CheckBox) findViewById(R.id.chkAG25YLowTet1);
            VAG25YLowTet1 = (TextView) findViewById(R.id.VAG25YLowTet1);

            secAG25YLowTet2 = (LinearLayout) findViewById(R.id.secAG25YLowTet2);
            VAG25YLowCodeTet2 = (TextView) findViewById(R.id.VAG25YLowCodeTet2);
            chkAG25YLowTet2 = (CheckBox) findViewById(R.id.chkAG25YLowTet2);
            VAG25YLowTet2 = (TextView) findViewById(R.id.VAG25YLowTet2);


            secAG25YLowTet3 = (LinearLayout) findViewById(R.id.secAG25YLowTet3);
            VAG25YLowCodeTet3 = (TextView) findViewById(R.id.VAG25YLowCodeTet3);
            chkAG25YLowTet3 = (CheckBox) findViewById(R.id.chkAG25YLowTet3);
            VAG25YLowTet3 = (TextView) findViewById(R.id.VAG25YLowTet3);

            secAG25YLowTet4 = (LinearLayout) findViewById(R.id.secAG25YLowTet4);
            VAG25YLowCodeTet4 = (TextView) findViewById(R.id.VAG25YLowCodeTet4);
            chkAG25YLowTet4 = (CheckBox) findViewById(R.id.chkAG25YLowTet4);
            VAG25YLowTet4 = (TextView) findViewById(R.id.VAG25YLowTet4);

            secAG25YLowTet4a = (LinearLayout) findViewById(R.id.secAG25YLowTet4a);
            VAG25YLowCodeTet4a = (TextView) findViewById(R.id.VAG25YLowCodeTet4a);
            chkAG25YLowTet4a = (CheckBox) findViewById(R.id.chkAG25YLowTet4a);
            VAG25YLowTet4a = (TextView) findViewById(R.id.VAG25YLowTet4a);

            secAG25YLowTet5 = (LinearLayout) findViewById(R.id.secAG25YLowTet5);
            VAG25YLowCodeTet5 = (TextView) findViewById(R.id.VAG25YLowCodeTet5);
            chkAG25YLowTet5 = (CheckBox) findViewById(R.id.chkAG25YLowTet5);
            VAG25YLowTet5 = (TextView) findViewById(R.id.VAG25YLowTet5);

            secAG25YLowTet5a = (LinearLayout) findViewById(R.id.secAG25YLowTet5a);
            VAG25YLowCodeTet5a = (TextView) findViewById(R.id.VAG25YLowCodeTet5a);
            chkAG25YLowTet5a = (CheckBox) findViewById(R.id.chkAG25YLowTet5a);
            VAG25YLowTet5a = (TextView) findViewById(R.id.VAG25YLowTet5a);

            secAG25YOth1 = (LinearLayout) findViewById(R.id.secAG25YOth1);
            VAG25YOthCode1 = (TextView) findViewById(R.id.VAG25YOthCode1);
            chkAG25YOth1 = (CheckBox) findViewById(R.id.chkAG25YOth1);
            VVAG25YOth1 = (TextView) findViewById(R.id.VVAG25YOth1);

            chkAG25YOth1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationFeedingAG25YOther();


                }
            });


            secAG25YOth2 = (LinearLayout) findViewById(R.id.secAG25YOth2);
            VAG25YOthCode2 = (TextView) findViewById(R.id.VAG25YOthCode2);
            chkAG25YOth2 = (CheckBox) findViewById(R.id.chkAG25YOth2);
            VVAG25YOth2 = (TextView) findViewById(R.id.VVAG25YOth2);

            chkAG25YOth2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationFeedingAG25YOther();

                }
            });

            secAG25YOth3 = (LinearLayout) findViewById(R.id.secAG25YOth3);
            VAG25YOthCode3 = (TextView) findViewById(R.id.VAG25YOthCode3);
            chkAG25YOth3 = (CheckBox) findViewById(R.id.chkAG25YOth3);
            VVAG25YOth3 = (TextView) findViewById(R.id.VVAG25YOth3);

            chkAG25YOth3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationFeedingAG25YOther();
                }
            });


            secAG25YOth4 = (LinearLayout) findViewById(R.id.secAG25YOth4);
            VAG25YOthCode4 = (TextView) findViewById(R.id.VAG25YOthCode4);
            chkAG25YOth4 = (CheckBox) findViewById(R.id.chkAG25YOth4);
            VVAG25YOth4 = (TextView) findViewById(R.id.VVAG25YOth4);

            chkAG25YOth4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationFeedingAG25YOther();

                }
            });

            secAG25YOth5 = (LinearLayout) findViewById(R.id.secAG25YOth5);
            VAG25YOthCode5 = (TextView) findViewById(R.id.VAG25YOthCode5);
            chkAG25YOth5 = (CheckBox) findViewById(R.id.chkAG25YOth5);
            VVAG25YOth5 = (TextView) findViewById(R.id.VVAG25YOth5);

            chkAG25YOth5.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationFeedingAG25YOther();

                }
            });

            secAG25YOth6 = (LinearLayout) findViewById(R.id.secAG25YOth6);
            VAG25YOthCode6 = (TextView) findViewById(R.id.VAG25YOthCode6);
            chkAG25YOth6 = (CheckBox) findViewById(R.id.chkAG25YOth6);
            VVAG25YOth6 = (TextView) findViewById(R.id.VVAG25YOth6);


            chkAG25YOth6.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationFeedingAG25YOther();
                }
            });

            secAG25YOth7 = (LinearLayout) findViewById(R.id.secAG25YOth7);
            VAG25YOthCode7 = (TextView) findViewById(R.id.VAG25YOthCode7);
            chkAG25YOth7 = (CheckBox) findViewById(R.id.chkAG25YOth7);
            VVAG25YOth7 = (TextView) findViewById(R.id.VVAG25YOth7);

            chkAG25YOth7.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClassificationFeedingAG25YOther();

                }
            });

            secOth1 = (LinearLayout) findViewById(R.id.secOth1);
            secOth2 = (LinearLayout) findViewById(R.id.secOth2);
            secOth3 = (LinearLayout) findViewById(R.id.secOth3);
            secOth4 = (LinearLayout) findViewById(R.id.secOth4);
            secOth5 = (LinearLayout) findViewById(R.id.secOth5);
            secOth6 = (LinearLayout) findViewById(R.id.secOth6);
            secOth7 = (LinearLayout) findViewById(R.id.secOth7);
            //    chkOth =(CheckBox) findViewById(R.id.chkOth);
            VOth1 = (TextView) findViewById(R.id.VOth1);

            VOth2 = (TextView) findViewById(R.id.VOth2);

            VOth3 = (TextView) findViewById(R.id.VOth3);

            VOth4 = (TextView) findViewById(R.id.VOth4);

            VOth5 = (TextView) findViewById(R.id.VOth5);

            VOth6 = (TextView) findViewById(R.id.VOth6);

            VOth7 = (TextView) findViewById(R.id.VOth7);


            secVDate = (LinearLayout) findViewById(R.id.secVDate);
            VlblVDate = (TextView) findViewById(R.id.VlblVDate);

            dtpVDate = (EditText) findViewById(R.id.dtpVDate);

            tableLayout1 = (TableLayout) findViewById(R.id.tableLayout1);
            tableLayout2 = (TableLayout) findViewById(R.id.tableLayout2);
            tableLayout3 = (TableLayout) findViewById(R.id.tableLayout3);
            tableLayout4a = (TableLayout) findViewById(R.id.tableLayout4a);
            tableLayout5 = (TableLayout) findViewById(R.id.tableLayout5);

            tableLayout6a = (TableLayout) findViewById(R.id.tableLayout6a);
            tableLayout7 = (TableLayout) findViewById(R.id.tableLayout7);
            tableLayout8a = (TableLayout) findViewById(R.id.tableLayout8a);

            tableLayout9 = (TableLayout) findViewById(R.id.tableLayout9);
            tableLayout10 = (TableLayout) findViewById(R.id.tableLayout10);
            tableLayout11a = (TableLayout) findViewById(R.id.tableLayout11a);
            tableLayout12 = (TableLayout) findViewById(R.id.tableLayout12);
            tableLayout13 = (TableLayout) findViewById(R.id.tableLayout13);
            tableLayout14 = (TableLayout) findViewById(R.id.tableLayout14);

            tableLayout15a = (TableLayout) findViewById(R.id.tableLayout15a);
            tableLayout16 = (TableLayout) findViewById(R.id.tableLayout16);
            tableLayout17 = (TableLayout) findViewById(R.id.tableLayout17);
            tableLayout19a = (TableLayout) findViewById(R.id.tableLayout19a);
            tableLayout20 = (TableLayout) findViewById(R.id.tableLayout20);
            tableLayout21 = (TableLayout) findViewById(R.id.tableLayout21);
            tableLayout22 = (TableLayout) findViewById(R.id.tableLayout22);
            tableLayout23 = (TableLayout) findViewById(R.id.tableLayout23);
            tableLayout24 = (TableLayout) findViewById(R.id.tableLayout24);


            btnVDate = (ImageButton) findViewById(R.id.btnVDate);
            dtpVDate.setText(Global.DateNowDMY());
            secRemarks = (LinearLayout) findViewById(R.id.secRemarks);
            VlblRemarks = (TextView) findViewById(R.id.VlblRemarks);
            txtRemarks = (EditText) findViewById(R.id.txtRemarks);
            SearchUnder5chaild();
            GridView gcount = (GridView) findViewById(R.id.gridPVisit);
            g.setImuCode(String.valueOf(gcount.getCount() + 1));
            PVisitStatus();
            // DataSearch(g.getDistrict(), g.getUpazila(), g.getUnion(), g.getMouza(), g.getVillage(), g.getHouseholdNo(), g.getSerialNo());
            btnVDate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btnVDate";
                    showDialog(DATE_DIALOG);
                }
            });

            tableLayout1.setVisibility(View.GONE);
            tableLayout2.setVisibility(View.GONE);
            tableLayout3.setVisibility(View.GONE);
            tableLayout4a.setVisibility(View.GONE);
            tableLayout5.setVisibility(View.GONE);
            tableLayout6a.setVisibility(View.GONE);
            tableLayout7.setVisibility(View.GONE);

            tableLayout8a.setVisibility(View.GONE);
            tableLayout9.setVisibility(View.GONE);
            tableLayout10.setVisibility(View.GONE);
            tableLayout11a.setVisibility(View.GONE);
            tableLayout12.setVisibility(View.GONE);
            tableLayout13.setVisibility(View.GONE);
            tableLayout14.setVisibility(View.GONE);

            tableLayout15a.setVisibility(View.GONE);
            tableLayout16.setVisibility(View.GONE);
            tableLayout17.setVisibility(View.GONE);
            tableLayout19a.setVisibility(View.GONE);

            tableLayout20.setVisibility(View.GONE);
            tableLayout21.setVisibility(View.GONE);
            tableLayout22.setVisibility(View.GONE);
            tableLayout23.setVisibility(View.GONE);
            tableLayout24.setVisibility(View.GONE);

            secAGO2MGA.setVisibility(View.GONE);

            secAGO2MGA1.setVisibility(View.GONE);
            secAGO2MGA2.setVisibility(View.GONE);
            secAGO2MGA3.setVisibility(View.GONE);

            secAGO2MGA4.setVisibility(View.GONE);
            secAGO2MGA5.setVisibility(View.GONE);
            secAGO2MGA6.setVisibility(View.GONE);
            secAGO2MGA7.setVisibility(View.GONE);
            secAGO2MGA8.setVisibility(View.GONE);

            secAGO2MGATetClass1.setVisibility(View.GONE);
            secAGO2MGATetClass2.setVisibility(View.GONE);
            secAGO2MGATetClass3.setVisibility(View.GONE);
            // secAGO2MDarC1.setVisibility(View.GONE);

            secAGO2MDarTetClass1.setVisibility(View.GONE);
            secAGO2MDarTetClass2.setVisibility(View.GONE);
            secAGO2MFeedTetClass1.setVisibility(View.GONE);
            secAGO2MFeedTetClass2.setVisibility(View.GONE);

            //   secAG25YNuSymtom1.setVisibility(View.GONE);

            secAG25YNuClass1.setVisibility(View.GONE);
            secAG25YNuClass2.setVisibility(View.GONE);
            secAG25YNuClass3.setVisibility(View.GONE);

            // secAG25YDarSymtom.setVisibility(View.GONE);
            secAG25YDarTetClass1.setVisibility(View.GONE);
            secAG25YDarTetClass2.setVisibility(View.GONE);
            secAG25YDarTetClass3.setVisibility(View.GONE);
            //secAG25YAmC1.setVisibility(View.GONE);

            secAG25YAmSymtom.setVisibility(View.GONE);

            secAG25YFevSymtom.setVisibility(View.GONE);


            secAG25YMalSymtom1.setVisibility(View.GONE);
            secAG25YMalSymtom2.setVisibility(View.GONE);


            secAG25YNutSymtom1.setVisibility(View.GONE);
            secAG25YNutSymtom2.setVisibility(View.GONE);
            secAG25YNutSymtom3.setVisibility(View.GONE);


            secAG25YLowSymtom1.setVisibility(View.GONE);
            secAG25YLowSymtom2.setVisibility(View.GONE);
            secAG25YLowSymtom3.setVisibility(View.GONE);
            secAG2to5Nu.setVisibility(View.GONE);
            secOth1.setVisibility(View.GONE);
            secOth2.setVisibility(View.GONE);
            secOth3.setVisibility(View.GONE);
            secOth4.setVisibility(View.GONE);
            secOth5.setVisibility(View.GONE);
            secOth6.setVisibility(View.GONE);
            secOth7.setVisibility(View.GONE);

            // secVisitCom.setVisibility(View.GONE);

            //secVisitCom.setVisibility(View.GONE);
            cmdTVisit = (Button) findViewById(R.id.cmdTVisit);
         /*   cmdTVisit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                   // secVisitCom.setVisibility(View.VISIBLE);
                }
            });*/
            btnTreatment1 = (Button) findViewById(R.id.btnTreatment1);
            btnTreatment1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkGeA1.isChecked() == true || chkGeA2.isChecked() == true || chkGeA3.isChecked() == true || chkGeA4.isChecked() == true || chkGeA5.isChecked() == true || chkGeA6.isChecked() == true) {
                        secAGO2MGATetClass1.setVisibility(View.VISIBLE);
                    }

                }
            });

            btnTreatment2 = (Button) findViewById(R.id.btnTreatment2);
            btnTreatment2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    secAGO2MGATetClass2.setVisibility(View.VISIBLE);
                }
            });

            btnTreatment3 = (Button) findViewById(R.id.btnTreatment3);
            btnTreatment3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    secAGO2MGATetClass3.setVisibility(View.VISIBLE);
                }
            });

            btnTreatment4 = (Button) findViewById(R.id.btnTreatment4);
            btnTreatment4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    secAGO2MDarTetClass1.setVisibility(View.VISIBLE);
                }
            });

            btnTreatment5 = (Button) findViewById(R.id.btnTreatment5);
            btnTreatment5.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    secAGO2MDarTetClass2.setVisibility(View.VISIBLE);
                }
            });

            btnTreatment6 = (Button) findViewById(R.id.btnTreatment6);
            btnTreatment6.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    secAGO2MFeedTetClass1.setVisibility(View.VISIBLE);
                }
            });

            btnTreatment7 = (Button) findViewById(R.id.btnTreatment7);
            btnTreatment7.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    secAGO2MFeedTetClass2.setVisibility(View.VISIBLE);
                }
            });

            btnTreatment8 = (Button) findViewById(R.id.btnTreatment8);
            btnTreatment8.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    secAG25YNuClass1.setVisibility(View.VISIBLE);
                }
            });

            btnTreatment9 = (Button) findViewById(R.id.btnTreatment9);
            btnTreatment9.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    secAG25YNuClass2.setVisibility(View.VISIBLE);
                }
            });

            btnTreatment10 = (Button) findViewById(R.id.btnTreatment10);
            btnTreatment10.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    secAG25YNuClass3.setVisibility(View.VISIBLE);
                }
            });

            btnTreatment11 = (Button) findViewById(R.id.btnTreatment11);
            btnTreatment11.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    secAG25YDarTetClass1.setVisibility(View.VISIBLE);
                }
            });

            btnTreatment12 = (Button) findViewById(R.id.btnTreatment12);
            btnTreatment12.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    secAG25YDarTetClass2.setVisibility(View.VISIBLE);
                }
            });
            btnTreatment13 = (Button) findViewById(R.id.btnTreatment13);
            btnTreatment13.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    secAG25YDarTetClass3.setVisibility(View.VISIBLE);
                }
            });
            btnTreatment14 = (Button) findViewById(R.id.btnTreatment14);
            btnTreatment14.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    secAG25YAmSymtom.setVisibility(View.VISIBLE);
                }
            });

            btnTreatment15 = (Button) findViewById(R.id.btnTreatment15);
            btnTreatment15.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    secAG25YFevSymtom.setVisibility(View.VISIBLE);
                }
            });


            btnTreatment16 = (Button) findViewById(R.id.btnTreatment16);
            btnTreatment16.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    secAG25YMalSymtom1.setVisibility(View.VISIBLE);
                }
            });

            btnTreatment17 = (Button) findViewById(R.id.btnTreatment17);
            btnTreatment17.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    secAG25YMalSymtom2.setVisibility(View.VISIBLE);
                }
            });

            btnTreatment18 = (Button) findViewById(R.id.btnTreatment18);
            btnTreatment18.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    secAG25YNutSymtom1.setVisibility(View.VISIBLE);
                }
            });

            btnTreatment19 = (Button) findViewById(R.id.btnTreatment19);
            btnTreatment19.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    secAG25YNutSymtom2.setVisibility(View.VISIBLE);
                }
            });
            btnTreatment20 = (Button) findViewById(R.id.btnTreatment20);
            btnTreatment20.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    secAG25YNutSymtom3.setVisibility(View.VISIBLE);
                }
            });


            btnTreatment21 = (Button) findViewById(R.id.btnTreatment21);
            btnTreatment21.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    secAG25YLowSymtom1.setVisibility(View.VISIBLE);
                }
            });

            btnTreatment22 = (Button) findViewById(R.id.btnTreatment22);
            btnTreatment22.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    secAG25YLowSymtom2.setVisibility(View.VISIBLE);
                }
            });
            btnTreatment23 = (Button) findViewById(R.id.btnTreatment23);
            btnTreatment23.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    secAG25YLowSymtom3.setVisibility(View.VISIBLE);
                }
            });

            //View page login
            // g.getCallFrom().equals("1")
            if (g.getCallFrom().equals("1")) {
                txtHealthID.setText(g.getHealthID());
                // txtHealthID.setText(g.getGeneratedId());
                txtSNo.setText(g.getSLno());
                txtHHNO.setText(g.getHHNO());
                txtSex.setText(g.getASex());
                txtName.setText(g.getAName());
                //  dtpVDate.setText(g.getAVDate());

                if (Integer.valueOf(g.getAAge()) <= 2) {
                    txtAge.setText(g.getAgeD());
                    VlblAge1.setText("দিন");
                    //VlblAge1.setText("মাস");
                } else if (Integer.valueOf(g.getAAge()) > 2 && Integer.valueOf(g.getAAge()) <= 11) {
                    txtAge.setText(g.getAgeM());
                    VlblAge1.setText("মাস");
                } else if (Integer.valueOf(g.getAAge()) > 11) {
                    txtAge.setText(g.getAgeY());
                    VlblAge1.setText("বছর");
                }

            } else {

            }
            if (g.getCallFrom().equals("1")) {

                if (Integer.valueOf(g.getAAge()) <= 2) {
                    AgeGroupOther();
                    AgeGroup02Month();
                  /*  DataRetriveAG02(g.getGeneratedId(),dtpVDate.getText().toString());
                    DataRetriveAG02Tet(g.getGeneratedId(),dtpVDate.getText().toString());
                    DataRetriveOther(g.getGeneratedId(),dtpVDate.getText().toString());
                     DataRetrive();*/

                } else if (Integer.valueOf(g.getAAge()) > 2) {

                    AgeGroup2to5Year();
                    AgeGroupOther();
                   /* DataRetriveAG25Y(g.getGeneratedId(),dtpVDate.getText().toString());
                    DataRetriveAG25Tet(g.getGeneratedId(),dtpVDate.getText().toString());
                    DataRetriveOther(g.getGeneratedId(),dtpVDate.getText().toString());
                    DataRetrive();*/

                }


            }
            if (g.getCallFrom().equals("ml")) {
                int M = 0;
                int D = 0;
                int Ag = 0;
                txtSNo.setText(ISlNo().toString());
                txtHealthID.setText(g.getHealthID());
                // txtHealthID.setText(g.getGeneratedId());
                txtHHNO.setText(g.getHouseholdNo());
                txtName.setText(g.getAName());
                // txtAge.setText(g.getAAge());
                // VlblAge1.setText("বছর");

                if (g.getASex().equals("1")) {
                    txtSex.setText("ছেলে");
                } else if (g.getASex().equals("2")) {
                    txtSex.setText("মেয়ে");
                }

                // Dob= Global.DateConvertDMY(cur.getString(cur.getColumnIndex("DOB")));
                D = Global.DateDifferenceDays(Global.DateNowDMY(), g.getDOB());
                Ag = Integer.valueOf(g.getAAge());

                M = (D / 30);
                // txtAge.setText(String.valueOf(M));

                AgeN = String.valueOf(M);

                if (D < 60 & Ag < 1) {
                    txtAge.setText(String.valueOf(D));
                    VlblAge1.setText("দিন");
                } else if (D > 60 & Ag < 1) {

                    txtAge.setText(String.valueOf(D / 30));
                    VlblAge1.setText("মাস");
                } else {
                    txtAge.setText(g.getAAge());
                    VlblAge1.setText("বছর");


                }

                if (Integer.valueOf(AgeN) <= 2) {
                    AgeGroupOther();
                    AgeGroup02Month();
        /*            DataRetriveAG02(g.getGeneratedId(), dtpVDate.getText().toString());
                    DataRetriveAG02Tet(g.getGeneratedId(), dtpVDate.getText().toString());
                    DataRetriveOther(g.getGeneratedId(), dtpVDate.getText().toString());
                    DataRetrive();*/


                } else if (Integer.valueOf(AgeN) > 2) {

                    AgeGroup2to5Year();
                    AgeGroupOther();
            /*        DataRetriveAG25Y(g.getGeneratedId(), dtpVDate.getText().toString());
                    DataRetriveAG25Tet(g.getGeneratedId(), dtpVDate.getText().toString());
                    DataRetriveOther(g.getGeneratedId(), dtpVDate.getText().toString());
                    DataRetrive();*/


                }
            }


            Button cmdTVisit = (Button) findViewById(R.id.cmdTVisit);
            cmdTVisit.setText(cmdTVisit.getText() + g.GetVisit(C, g.getGeneratedId(), g.getProvCode()));
            cmdSave = (Button) findViewById(R.id.cmdSave);
            cmdSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {


                    DataSave();
                    DataSaveSymtom();
                    DataSaveTreatment();

                    Connection.MessageBox(Under5child.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
                    if (g.getCallFrom().equals("1")) {
                        finish();
                        Intent f2 = new Intent(getApplicationContext(), Under5ChildView.class);
                        startActivity(f2);
                    } else {
                        finish();
                        Intent f2 = new Intent(getApplicationContext(), MemberList.class);
                        startActivity(f2);
                    }
                }
            });
        } catch (Exception e) {
            Connection.MessageBox(Under5child.this, e.getMessage());
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
            return Integer.parseInt(C.ReturnSingleValue("Select count(*)total from under5Child where healthid='" + g.getGeneratedId() + "'"));
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

                String SQL = "Select strftime('%d/%m/%Y', date(visitDate)) as visitDate  from under5Child where healthid='" + g.getGeneratedId() + "' order by visitDate  asc";//date(imudate) asc

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
                    tv.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {

                            if (vcode[1][position] == vcode[1][position]) {


                                DataRetriveAG02(g.getGeneratedId(), vcode[1][position]);
                                DataRetriveAG02Tet(g.getGeneratedId(), vcode[1][position]);
                                DataRetriveAG25Y(g.getGeneratedId(), vcode[1][position]);
                                DataRetriveAG25Tet(g.getGeneratedId(), vcode[1][position]);
                                DataRetriveOther(g.getGeneratedId(), vcode[1][position]);
                                DataRetrive();


                            }


                         /*   // দিন মাস বছর
                            if(VlblAge1.getText().equals("দিন"))
                            {
                                secAGO2MGA.setVisibility(View.VISIBLE);
                                secAG2to5Nu.setVisibility(View.GONE);
                                secOther.setVisibility(View.VISIBLE);
                                AgeGroupOther();
                                AgeGroup02Month();
                            }
                            else {
                                secAGO2MGA.setVisibility(View.GONE);
                                secAG2to5Nu.setVisibility(View.VISIBLE);
                                secOther.setVisibility(View.VISIBLE);
                                AgeGroup2to5Year();
                                AgeGroupOther();

                            }*/

                            g.setImuCode(vcode[1][position]);
                        }
                    });
                } catch (Exception ex) {
                    Connection.MessageBox(Under5child.this, ex.getMessage());
                }

            }
            return MyView;
        }

    }

    private String ISlNo() {
        String SQL = "";

        SQL = "select ifnull(Count(*),'')+1 from under5Child";
        SQL += " where healthId='" + g.getGeneratedId() + "' and providerId='" + g.getProvCode() + "'";
        String SNo = C.ReturnSingleValue(SQL);
        // Serial No auto increment
        return SNo;
    }


    private void AgeGroup02Month() {
        secAGO2MGA.setVisibility(View.VISIBLE);
        VGeACode1.setText(g.GetsymtomCode(C, "1"));
        VGeA1.setText(g.GetsymtomDes(C, "1"));
        VGeACode2.setText(g.GetsymtomCode(C, "2"));
        VGeA2.setText(g.GetsymtomDes(C, "2"));
        VGeACode3.setText(g.GetsymtomCode(C, "3"));
        VGeA3.setText(g.GetsymtomDes(C, "3"));
        VGeACode4.setText(g.GetsymtomCode(C, "4"));
        VGeA4.setText(g.GetsymtomDes(C, "4"));
        VGeACode5.setText(g.GetsymtomCode(C, "5"));
        VGeA5.setText(g.GetsymtomDes(C, "5"));
        VGeACode6.setText(g.GetsymtomCode(C, "6"));
        VGeA6.setText(g.GetsymtomDes(C, "6"));
        VGeACode7.setText(g.GetsymtomCode(C, "7"));
        VGeA7.setText(g.GetsymtomDes(C, "7"));
        VGeACode8.setText(g.GetsymtomCode(C, "8"));
        VGeA8.setText(g.GetsymtomDes(C, "8"));
        VGeACode9.setText(g.GetsymtomCode(C, "9"));
        VGeA9.setText(g.GetsymtomDes(C, "9"));
        VAG02GAC1.setText(g.Getclassfication(C, "1"));
        VGeACodeTet1.setText(g.GettreatmentCode(C, "1"));
        VGeATet1.setText(g.GettreatmentDes(C, "1"));

        VGeACodeTet2.setText(g.GettreatmentCode(C, "2"));
        VGeATet2.setText(g.GettreatmentDes(C, "2"));
        VGeACodeTet3.setText(g.GettreatmentCode(C, "3"));
        VGeATet3.setText(g.GettreatmentDes(C, "3"));
        VGeACodeTet4.setText(g.GettreatmentCode(C, "4"));
        VGeATet4.setText(g.GettreatmentDes(C, "4"));

        VAG02GAC2.setText(g.Getclassfication(C, "2"));
        VGeACodeTet5.setText(g.GettreatmentCode(C, "5"));
        VGeATet5.setText(g.GettreatmentDes(C, "5"));
        VGeACodeTet6.setText(g.GettreatmentCode(C, "6"));

        VGeATet6.setText(g.GettreatmentDes(C, "6"));
        VGeACodeTet7.setText(g.GettreatmentCode(C, "7"));
        VGeATet7.setText(g.GettreatmentDes(C, "7"));

        VGeACodeTet7a.setText(g.GettreatmentCode(C, "67"));
        VGeATet7a.setText(g.GettreatmentDes(C, "67"));

        VAG02GAC3.setText(g.Getclassfication(C, "3"));
        VGeACodeTet8.setText(g.GettreatmentCode(C, "8"));
        VGeATet8.setText(g.GettreatmentDes(C, "8"));
        VGeACodeTet9.setText(g.GettreatmentCode(C, "9"));
        VGeATet9.setText(g.GettreatmentDes(C, "9"));
        VGeACodeTet9a.setText(g.GettreatmentCode(C, "67"));
        VGeATet9a.setText(g.GettreatmentDes(C, "67"));

        VAGO2MDar1.setText(g.GetsymtomCode(C, "10"));
        VVAGO2MDar1.setText(g.GetsymtomDes(C, "10"));
        VAGO2MDar2.setText(g.GetsymtomCode(C, "11"));
        VVAGO2MDar2.setText(g.GetsymtomDes(C, "11"));
        VAGO2MDar3.setText(g.GetsymtomCode(C, "12"));
        VVAGO2MDar3.setText(g.GetsymtomDes(C, "12"));
        VAGO2MDar4.setText(g.GetsymtomCode(C, "13"));
        VVAGO2MDar4.setText(g.GetsymtomDes(C, "13"));
        VAGO2MDar5.setText(g.GetsymtomCode(C, "14"));
        VVAGO2MDar5.setText(g.GetsymtomDes(C, "14"));
        VAGO2MDar6.setText(g.GetsymtomCode(C, "15"));
        VVAGO2MDar6.setText(g.GetsymtomDes(C, "15"));
        VAGO2MDar7.setText(g.GetsymtomCode(C, "16"));
        VVAGO2MDar7.setText(g.GetsymtomDes(C, "16"));
        VAGO2MDarC1.setText(g.Getclassfication(C, "4"));
        VAGO2MDarCodeTet1.setText(g.GettreatmentCode(C, "10"));
        VAGO2MDarTet1.setText(g.GettreatmentDes(C, "10"));
        VAGO2MDarCodeTet2.setText(g.GettreatmentCode(C, "11"));
        VAGO2MDarTet2.setText(g.GettreatmentDes(C, "11"));
        VAGO2MDarCodeTet3.setText(g.GettreatmentCode(C, "12"));
        VAGO2MDarTet3.setText(g.GettreatmentDes(C, "12"));
        VAGO2MDarC2.setText(g.Getclassfication(C, "6"));
        VAGO2MDarCodeTet4.setText(g.GettreatmentCode(C, "16"));
        VAGO2MDarTet4.setText(g.GettreatmentDes(C, "16"));
        VAGO2MDarCodeTet5.setText(g.GettreatmentCode(C, "17"));
        VAGO2MDarTet5.setText(g.GettreatmentDes(C, "17"));
        VAGO2MDarCodeTet6.setText(g.GettreatmentCode(C, "18"));
        VAGO2MDarTet6.setText(g.GettreatmentDes(C, "18"));
        VAGO2MDarCodeTet6a.setText(g.GettreatmentCode(C, "67"));
        VAGO2MDarTet6a.setText(g.GettreatmentDes(C, "67"));
        VAGO2MFed1.setText(g.GetsymtomCode(C, "17"));
        VVAGO2MFed1.setText(g.GetsymtomDes(C, "17"));
        VAGO2MFed2.setText(g.GetsymtomCode(C, "18"));
        VVAGO2MFed2.setText(g.GetsymtomDes(C, "18"));
        VAGO2MFed3.setText(g.GetsymtomCode(C, "19"));
        VVAGO2MFed3.setText(g.GetsymtomDes(C, "19"));
        VAGO2MFed4.setText(g.GetsymtomCode(C, "20"));
        VVAGO2MFed4.setText(g.GetsymtomDes(C, "20"));
        VAGO2MFed5.setText(g.GetsymtomCode(C, "21"));
        VVAGO2MFed5.setText(g.GetsymtomDes(C, "21"));
        VAGO2MFed6.setText(g.GetsymtomCode(C, "22"));
        VVAGO2MFed6.setText(g.GetsymtomDes(C, "22"));
        VAGO2MFedC1.setText(g.Getclassfication(C, "7"));
        VAGO2MFedCodeTet1.setText(g.GettreatmentCode(C, "19"));
        VAGO2MFedTet1.setText(g.GettreatmentDes(C, "19"));
        VAGO2MFedCodeTet2.setText(g.GettreatmentCode(C, "20"));
        VAGO2MFedTet2.setText(g.GettreatmentDes(C, "20"));
        VAGO2MFedCodeTet3.setText(g.GettreatmentCode(C, "21"));
        VAGO2MFedTet3.setText(g.GettreatmentDes(C, "21"));
        VAGO2MFedCodeTet4.setText(g.GettreatmentCode(C, "22"));
        VAGO2MFedTet4.setText(g.GettreatmentDes(C, "22"));
        VAGO2MFedCodeTet5.setText(g.GettreatmentCode(C, "23"));
        VAGO2MFedTet5.setText(g.GettreatmentDes(C, "23"));
        VAGO2MFedC2.setText(g.Getclassfication(C, "8"));
        VAGO2MFedCodeTet6.setText(g.GettreatmentCode(C, "24"));
        VAGO2MFedTet6.setText(g.GettreatmentDes(C, "24"));
        VAGO2MFedCodeTet7.setText(g.GettreatmentCode(C, "25"));
        VAGO2MFedTet7.setText(g.GettreatmentDes(C, "25"));


    }

    private void AgeGroupOther() {
        VAG25YOthCode1.setText(g.GetsymtomCode(C, "49"));
        VVAG25YOth1.setText(g.GetsymtomDes(C, "49"));
        VAG25YOthCode2.setText(g.GetsymtomCode(C, "50"));
        VVAG25YOth2.setText(g.GetsymtomDes(C, "50"));

        VAG25YOthCode3.setText(g.GetsymtomCode(C, "51"));
        VVAG25YOth3.setText(g.GetsymtomDes(C, "51"));

        VAG25YOthCode4.setText(g.GetsymtomCode(C, "52"));
        VVAG25YOth4.setText(g.GetsymtomDes(C, "52"));


        VAG25YOthCode5.setText(g.GetsymtomCode(C, "53"));
        VVAG25YOth5.setText(g.GetsymtomDes(C, "53"));


        VAG25YOthCode6.setText(g.GetsymtomCode(C, "54"));
        VVAG25YOth6.setText(g.GetsymtomDes(C, "54"));


        VAG25YOthCode7.setText(g.GetsymtomCode(C, "55"));
        VVAG25YOth7.setText(g.GetsymtomDes(C, "55"));

        VOth1.setText(g.Getclassfication(C, "25"));
        VOth2.setText(g.Getclassfication(C, "26"));
        VOth3.setText(g.Getclassfication(C, "27"));
        VOth4.setText(g.Getclassfication(C, "28"));
        VOth5.setText(g.Getclassfication(C, "29"));
        VOth6.setText(g.Getclassfication(C, "30"));
        VOth7.setText(g.Getclassfication(C, "31"));
    }

    private void DataRetriveOther(String HealthId, String VDate) {
        Cursor cur = C.ReadData("select  " +
                " ifnull(MAX(CASE WHEN problemCode=49 THEN problemCode END ),'' )As P49 ," +
                " ifnull(MAX(CASE WHEN problemCode=50 THEN problemCode END ),'' )As P50 ," +
                " ifnull(MAX(CASE WHEN problemCode=51 THEN problemCode END ),'' )As P51 ," +
                " ifnull(MAX(CASE WHEN problemCode=52 THEN problemCode END ),'' )As P52 ," +
                " ifnull(MAX(CASE WHEN problemCode=53 THEN problemCode END ),'' )As P53 ," +
                " ifnull(MAX(CASE WHEN problemCode=54 THEN problemCode END ),'' )As P54 ," +
                " ifnull(MAX(CASE WHEN problemCode=55 THEN problemCode END ),'' )As P55 " +

                // " MAX(CASE WHEN problemCode=11 THEN problemCode END) P11,MAX(CASE WHEN problemCode=12 THEN problemCode END) P12 " +
                " FROM  under5ChildProblem WHERE HealthId = '" + HealthId + "' and strftime('%d/%m/%Y', date(visitDate))= '" + VDate + "'");
        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            if (cur.getString(cur.getColumnIndex("P49")).equals("49")) {
                chkAG25YOth1.setChecked(true);
            } else {
                chkAG25YOth1.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("P50")).equals("50")) {
                chkAG25YOth2.setChecked(true);
            } else {
                chkAG25YOth2.setChecked(false);
            }


            if (cur.getString(cur.getColumnIndex("P51")).equals("51")) {
                chkAG25YOth3.setChecked(true);
            } else {
                chkAG25YOth3.setChecked(false);
            }
            if (cur.getString(cur.getColumnIndex("P52")).equals("52")) {
                chkAG25YOth4.setChecked(true);
            } else {
                chkAG25YOth4.setChecked(false);
            }
            if (cur.getString(cur.getColumnIndex("P53")).equals("53")) {
                chkAG25YOth5.setChecked(true);
            } else {
                chkAG25YOth5.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("P54")).equals("54")) {
                chkAG25YOth6.setChecked(true);
            } else {
                chkAG25YOth6.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("P55")).equals("55")) {
                chkAG25YOth7.setChecked(true);
            } else {
                chkAG25YOth7.setChecked(false);
            }
            cur.moveToNext();
        }
        cur.close();
    }

    private void DataRetriveAG02Tet(String HealthId, String VDate) {
        Cursor cur = C.ReadData("select  " +
                " ifnull(MAX(CASE WHEN adviceCode=1 THEN adviceCode END),'' )As A1,ifnull(MAX(CASE WHEN adviceCode=2 THEN adviceCode END),'' )As A2 ," +
                " ifnull(MAX(CASE WHEN adviceCode=3 THEN adviceCode END),'' )As A3,ifnull(MAX(CASE WHEN adviceCode=4 THEN adviceCode END),'' )As A4 ," +
                " ifnull(MAX(CASE WHEN adviceCode=5 THEN adviceCode END),'' )As A5,ifnull(MAX(CASE WHEN adviceCode=6 THEN adviceCode END),'' )As A6 ," +
                " ifnull(MAX(CASE WHEN adviceCode=7 THEN adviceCode END),'' )As A7,ifnull(MAX(CASE WHEN adviceCode=67 THEN adviceCode END),'' )As A7a ,ifnull(MAX(CASE WHEN adviceCode=8 THEN adviceCode END),'' )As A8 ," +
                " ifnull(MAX(CASE WHEN adviceCode=9 THEN adviceCode END),'' )As A9 ,ifnull(MAX(CASE WHEN adviceCode=67 THEN adviceCode END),'' )As A9a ," +
                " ifnull(MAX(CASE WHEN adviceCode=10 THEN adviceCode END ),'' )As A10 ," +
                " ifnull(MAX(CASE WHEN adviceCode=11 THEN adviceCode END ),'' )As A11 ," +
                " ifnull(MAX(CASE WHEN adviceCode=12 THEN adviceCode END ),'' )As A12 ," +
                " ifnull(MAX(CASE WHEN adviceCode=13 THEN adviceCode END ),'' )As A13 ," +
                " ifnull(MAX(CASE WHEN adviceCode=14 THEN adviceCode END ),'' )As A14 ," +
                " ifnull(MAX(CASE WHEN adviceCode=15 THEN adviceCode END ),'' )As A15 ," +
                " ifnull(MAX(CASE WHEN adviceCode=16 THEN adviceCode END ),'' )As A16 ," +
                " ifnull(MAX(CASE WHEN adviceCode=17 THEN adviceCode END ),'' )As A17 ," +
                " ifnull(MAX(CASE WHEN adviceCode=18 THEN adviceCode END ),'' )As A18 ,ifnull(MAX(CASE WHEN adviceCode=67 THEN adviceCode END ),'' )As A18a ," +
                " ifnull(MAX(CASE WHEN adviceCode=19 THEN adviceCode END ),'' )As A19 ," +
                " ifnull(MAX(CASE WHEN adviceCode=20 THEN adviceCode END ),'' )As A20 ," +
                " ifnull(MAX(CASE WHEN adviceCode=21 THEN adviceCode END ),'' )As A21 ," +
                " ifnull(MAX(CASE WHEN adviceCode=22 THEN adviceCode END ),'' )As A22 ," +
                " ifnull(MAX(CASE WHEN adviceCode=23 THEN adviceCode END ),'' )As A23 ," +
                " ifnull(MAX(CASE WHEN adviceCode=24 THEN adviceCode END ),'' )As A24 ," +
                " ifnull(MAX(CASE WHEN adviceCode=25 THEN adviceCode END ),'' )As A25 " +

                // " MAX(CASE WHEN problemCode=11 THEN problemCode END) P11,MAX(CASE WHEN problemCode=12 THEN problemCode END) P12 " +
                " FROM  under5ChildAdvice WHERE HealthId = '" + HealthId + "' and strftime('%d/%m/%Y', date(visitDate))= '" + VDate + "'");
        cur.moveToFirst();
        while (!cur.isAfterLast()) {

            if (cur.getString(cur.getColumnIndex("A1")).equals("1")) {
                chkGeATet1.setChecked(true);
            } else {
                chkGeATet1.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A2")).equals("2")) {
                chkGeATet2.setChecked(true);
            } else {
                chkGeATet2.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A3")).equals("3")) {
                chkGeATet3.setChecked(true);
            } else {
                chkGeATet3.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A4")).equals("4")) {
                chkGeATet4.setChecked(true);
            } else {
                chkGeATet4.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A5")).equals("5")) {
                chkGeATet5.setChecked(true);
            } else {
                chkGeATet5.setChecked(false);
            }
            if (cur.getString(cur.getColumnIndex("A6")).equals("6")) {
                chkGeATet6.setChecked(true);
            } else {
                chkGeATet6.setChecked(false);
            }
            if (cur.getString(cur.getColumnIndex("A7")).equals("7")) {
                chkGeATet7.setChecked(true);
            } else {
                chkGeATet7.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A7a")).equals("67")) {
                chkGeATet7a.setChecked(true);
            } else {
                chkGeATet7a.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A8")).equals("8")) {
                chkGeATet8.setChecked(true);
            } else {
                chkGeATet8.setChecked(false);
            }
            if (cur.getString(cur.getColumnIndex("A9")).equals("9")) {
                chkGeATet9.setChecked(true);
            } else {
                chkGeATet9.setChecked(false);
            }
            if (cur.getString(cur.getColumnIndex("A9a")).equals("67")) {
                chkGeATet9a.setChecked(true);
            } else {
                chkGeATet9a.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A10")).equals("10")) {
                chkAGO2MDarTet1.setChecked(true);
            } else {
                chkAGO2MDarTet1.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A11")).equals("11")) {
                chkAGO2MDarTet2.setChecked(true);
            } else {
                chkAGO2MDarTet2.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A12")).equals("12")) {
                chkAGO2MDarTet3.setChecked(true);
            } else {
                chkAGO2MDarTet3.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A16")).equals("16")) {
                chkAGO2MDarTet4.setChecked(true);
            } else {
                chkAGO2MDarTet4.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A17")).equals("17")) {
                chkAGO2MDarTet5.setChecked(true);
            } else {
                chkAGO2MDarTet5.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A18")).equals("18")) {
                chkAGO2MDarTet6.setChecked(true);
            } else {
                chkAGO2MDarTet6.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A18a")).equals("67")) {
                chkAGO2MDarTet6a.setChecked(true);
            } else {
                chkAGO2MDarTet6a.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A19")).equals("19")) {
                chkAGO2MFedTet1.setChecked(true);
            } else {
                chkAGO2MFedTet1.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A20")).equals("20")) {
                chkAGO2MFedTet2.setChecked(true);
            } else {
                chkAGO2MFedTet2.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A21")).equals("21")) {
                chkAGO2MFedTet3.setChecked(true);
            } else {
                chkAGO2MFedTet3.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A22")).equals("22")) {
                chkAGO2MFedTet4.setChecked(true);
            } else {
                chkAGO2MFedTet4.setChecked(false);
            }
            if (cur.getString(cur.getColumnIndex("A23")).equals("23")) {
                chkAGO2MFedTet5.setChecked(true);
            } else {
                chkAGO2MFedTet5.setChecked(false);
            }
            if (cur.getString(cur.getColumnIndex("A24")).equals("24")) {
                chkAGO2MFedTet6.setChecked(true);
            } else {
                chkAGO2MFedTet6.setChecked(false);
            }
            if (cur.getString(cur.getColumnIndex("A25")).equals("25")) {
                chkAGO2MFedTet7.setChecked(true);
            } else {
                chkAGO2MFedTet7.setChecked(false);
            }
            cur.moveToNext();
        }
        cur.close();
    }

    private void DataRetriveAG02(String HealthId, String VDate) {
        Cursor cur = C.ReadData("select  " +
                " ifnull(MAX(CASE WHEN problemCode=1 THEN problemCode END),'' )As P1,ifnull(MAX(CASE WHEN problemCode=2 THEN problemCode END),'' )As P2 ," +
                " ifnull(MAX(CASE WHEN problemCode=3 THEN problemCode END),'' )As P3,ifnull(MAX(CASE WHEN problemCode=4 THEN problemCode END),'' )As P4 ," +
                " ifnull(MAX(CASE WHEN problemCode=5 THEN problemCode END),'' )As P5,ifnull(MAX(CASE WHEN problemCode=6 THEN problemCode END),'' )As P6 ," +
                " ifnull(MAX(CASE WHEN problemCode=7 THEN problemCode END),'' )As P7,ifnull(MAX(CASE WHEN problemCode=8 THEN problemCode END),'' )As P8 ," +
                " ifnull(MAX(CASE WHEN problemCode=9 THEN problemCode END),'' )As P9 ," +
                " ifnull(MAX(CASE WHEN problemCode=10 THEN problemCode END ),'' )As P10 ," +
                " ifnull(MAX(CASE WHEN problemCode=11 THEN problemCode END ),'' )As P11 ," +
                " ifnull(MAX(CASE WHEN problemCode=12 THEN problemCode END ),'' )As P12 ," +
                " ifnull(MAX(CASE WHEN problemCode=13 THEN problemCode END ),'' )As P13 ," +
                " ifnull(MAX(CASE WHEN problemCode=14 THEN problemCode END ),'' )As P14 ," +
                " ifnull(MAX(CASE WHEN problemCode=15 THEN problemCode END ),'' )As P15 ," +
                " ifnull(MAX(CASE WHEN problemCode=16 THEN problemCode END ),'' )As P16 ," +
                " ifnull(MAX(CASE WHEN problemCode=17 THEN problemCode END ),'' )As P17 ," +
                " ifnull(MAX(CASE WHEN problemCode=18 THEN problemCode END ),'' )As P18 ," +
                " ifnull(MAX(CASE WHEN problemCode=19 THEN problemCode END ),'' )As P19 ," +
                " ifnull(MAX(CASE WHEN problemCode=20 THEN problemCode END ),'' )As P20 ," +
                " ifnull(MAX(CASE WHEN problemCode=21 THEN problemCode END ),'' )As P21 ," +
                " ifnull(MAX(CASE WHEN problemCode=22 THEN problemCode END ),'' )As P22 " +

                // " MAX(CASE WHEN problemCode=11 THEN problemCode END) P11,MAX(CASE WHEN problemCode=12 THEN problemCode END) P12 " +
                " FROM  under5ChildProblem WHERE HealthId = '" + HealthId + "' and strftime('%d/%m/%Y', date(visitDate))= '" + VDate + "'");
        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            if (cur.getString(cur.getColumnIndex("P1")).equals("1")) {
                chkGeA1.setChecked(true);
            } else {
                chkGeA1.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("P2")).equals("2")) {
                chkGeA2.setChecked(true);
            } else {
                chkGeA2.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("P3")).equals("3")) {
                chkGeA3.setChecked(true);
            } else {
                chkGeA3.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("P4")).equals("4")) {
                chkGeA4.setChecked(true);
            } else {
                chkGeA4.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("P5")).equals("5")) {
                chkGeA5.setChecked(true);
            } else {
                chkGeA5.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("P6")).equals("6")) {
                chkGeA6.setChecked(true);
            } else {
                chkGeA6.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("P7")).equals("7")) {
                chkGeA7.setChecked(true);
            } else {
                chkGeA7.setChecked(false);
            }
            if (cur.getString(cur.getColumnIndex("P8")).equals("8")) {
                chkGeA8.setChecked(true);
            } else {
                chkGeA8.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("P9")).equals("9")) {
                chkGeA9.setChecked(true);
            } else {
                chkGeA9.setChecked(false);
            }
            if (cur.getString(cur.getColumnIndex("P10")).equals("10")) {
                chkAGO2MDar1.setChecked(true);
            } else {
                chkAGO2MDar1.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("P11")).equals("11")) {
                chkAGO2MDar2.setChecked(true);
            } else {
                chkAGO2MDar2.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("P12")).equals("12")) {
                chkAGO2MDar3.setChecked(true);
            } else {
                chkAGO2MDar3.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("P13")).equals("13")) {
                chkAGO2MDar4.setChecked(true);
            } else {
                chkAGO2MDar4.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("P14")).equals("14")) {
                chkAGO2MDar5.setChecked(true);
            } else {
                chkAGO2MDar5.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("P15")).equals("15")) {
                chkAGO2MDar6.setChecked(true);
            } else {
                chkAGO2MDar6.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("P16")).equals("16")) {
                chkAGO2MDar7.setChecked(true);
            } else {
                chkAGO2MDar7.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("P17")).equals("17")) {
                chkAGO2MFed1.setChecked(true);
            } else {
                chkAGO2MFed1.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("P18")).equals("18")) {
                chkAGO2MFed2.setChecked(true);
            } else {
                chkAGO2MFed2.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("P19")).equals("19")) {
                chkAGO2MFed3.setChecked(true);
            } else {
                chkAGO2MFed3.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("P20")).equals("20")) {
                chkAGO2MFed4.setChecked(true);
            } else {
                chkAGO2MFed4.setChecked(false);
            }
            if (cur.getString(cur.getColumnIndex("P21")).equals("21")) {
                chkAGO2MFed5.setChecked(true);
            } else {
                chkAGO2MFed5.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("P22")).equals("22")) {
                chkAGO2MFed6.setChecked(true);
            } else {
                chkAGO2MFed6.setChecked(false);
            }

            cur.moveToNext();
        }
        cur.close();
    }

    private void DataRetriveAG25Y(String HealthId, String VDate) {
        Cursor cur = C.ReadData("select  " +
                " ifnull(MAX(CASE WHEN problemCode=23 THEN problemCode END),'' )As P23,ifnull(MAX(CASE WHEN problemCode=24 THEN problemCode END),'' )As P24 ," +
                " ifnull(MAX(CASE WHEN problemCode=25 THEN problemCode END),'' )As P25,ifnull(MAX(CASE WHEN problemCode=26 THEN problemCode END),'' )As P26 ," +
                " ifnull(MAX(CASE WHEN problemCode=27 THEN problemCode END),'' )As P27,ifnull(MAX(CASE WHEN problemCode=28 THEN problemCode END),'' )As P28 ," +
                " ifnull(MAX(CASE WHEN problemCode=29 THEN problemCode END),'' )As P29,ifnull(MAX(CASE WHEN problemCode=30 THEN problemCode END),'' )As P30 ," +
                " ifnull(MAX(CASE WHEN problemCode=31 THEN problemCode END),'' )As P31 ," +
                " ifnull(MAX(CASE WHEN problemCode=32 THEN problemCode END ),'' )As P32 ," +
                " ifnull(MAX(CASE WHEN problemCode=33 THEN problemCode END ),'' )As P33 ," +
                " ifnull(MAX(CASE WHEN problemCode=34 THEN problemCode END ),'' )As P34 ," +
                " ifnull(MAX(CASE WHEN problemCode=35 THEN problemCode END ),'' )As P35 ," +
                " ifnull(MAX(CASE WHEN problemCode=36 THEN problemCode END ),'' )As P36 ," +
                " ifnull(MAX(CASE WHEN problemCode=37 THEN problemCode END ),'' )As P37 ," +
                " ifnull(MAX(CASE WHEN problemCode=38 THEN problemCode END ),'' )As P38 ," +
                " ifnull(MAX(CASE WHEN problemCode=39 THEN problemCode END ),'' )As P39 ," +
                " ifnull(MAX(CASE WHEN problemCode=40 THEN problemCode END ),'' )As P40 ," +
                " ifnull(MAX(CASE WHEN problemCode=41 THEN problemCode END ),'' )As P41 ," +
                " ifnull(MAX(CASE WHEN problemCode=42 THEN problemCode END ),'' )As P42 ," +
                " ifnull(MAX(CASE WHEN problemCode=43 THEN problemCode END ),'' )As P43 ," +
                " ifnull(MAX(CASE WHEN problemCode=44 THEN problemCode END ),'' )As P44 ," +
                " ifnull(MAX(CASE WHEN problemCode=45 THEN problemCode END ),'' )As P45 ," +
                " ifnull(MAX(CASE WHEN problemCode=46 THEN problemCode END ),'' )As P46 ," +
                " ifnull(MAX(CASE WHEN problemCode=47 THEN problemCode END ),'' )As P47 ," +
                " ifnull(MAX(CASE WHEN problemCode=48 THEN problemCode END ),'' )As P48 " +

                // " MAX(CASE WHEN problemCode=11 THEN problemCode END) P11,MAX(CASE WHEN problemCode=12 THEN problemCode END) P12 " +
                " FROM  under5ChildProblem WHERE HealthId = '" + HealthId + "' and strftime('%d/%m/%Y', date(visitDate))= '" + VDate + "'");
        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            if (cur.getString(cur.getColumnIndex("P23")).equals("23")) {
                chkAG25YSym1.setChecked(true);
            } else {
                chkAG25YSym1.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("P24")).equals("24")) {
                chkAG25YSym2.setChecked(true);
            } else {
                chkAG25YSym2.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("P25")).equals("25")) {
                chkAG25YSym3.setChecked(true);
            } else {
                chkAG25YSym3.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("P26")).equals("26")) {
                chkAG25YSym4.setChecked(true);
            } else {
                chkAG25YSym4.setChecked(false);
            }


            if (cur.getString(cur.getColumnIndex("P28")).equals("28")) {
                chkAG25YDar1.setChecked(true);
            } else {
                chkAG25YDar1.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("P29")).equals("29")) {
                chkAG25YDar2.setChecked(true);
            } else {
                chkAG25YDar2.setChecked(false);
            }
            if (cur.getString(cur.getColumnIndex("P30")).equals("30")) {
                chkAG25YDar3.setChecked(true);
            } else {
                chkAG25YDar3.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("P31")).equals("31")) {
                chkAG25YDar4.setChecked(true);
            } else {
                chkAG25YDar4.setChecked(false);
            }
            if (cur.getString(cur.getColumnIndex("P32")).equals("32")) {
                chkAG25YDar5.setChecked(true);
            } else {
                chkAG25YDar5.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("P33")).equals("33")) {
                chkAG25YDar6.setChecked(true);
            } else {
                chkAG25YDar6.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("P34")).equals("34")) {
                chkAG25YDar7.setChecked(true);
            } else {
                chkAG25YDar7.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("P35")).equals("P35")) {
                chkAG25YDar8.setChecked(true);
            } else {
                chkAG25YDar8.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("P36")).equals("P36")) {
                chkAG25YDar9.setChecked(true);
            } else {
                chkAG25YDar9.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("P37")).equals("37")) {
                chkAG25YAm1.setChecked(true);
            } else {
                chkAG25YAm1.setChecked(false);
            }


            if (cur.getString(cur.getColumnIndex("P38")).equals("38")) {
                chkAG25YFev1.setChecked(true);
            } else {
                chkAG25YFev1.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("P39")).equals("39")) {
                chkAG25YFev2.setChecked(true);
            } else {
                chkAG25YFev2.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("P40")).equals("40")) {
                chkAG25YMal1.setChecked(true);
            } else {
                chkAG25YMal1.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("P41")).equals("41")) {
                chkAG25YMal2.setChecked(true);
            } else {
                chkAG25YMal2.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("P42")).equals("42")) {
                chkAG25YNut1.setChecked(true);
            } else {
                chkAG25YNut1.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("P43")).equals("43")) {
                chkAG25YNut2.setChecked(true);
            } else {
                chkAG25YNut2.setChecked(false);
            }
            if (cur.getString(cur.getColumnIndex("P44")).equals("44")) {
                chkAG25YNut3.setChecked(true);
            } else {
                chkAG25YNut3.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("P45")).equals("45")) {
                chkAG25YNut4.setChecked(true);
            } else {
                chkAG25YNut4.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("P46")).equals("46")) {
                chkAG25YLow1.setChecked(true);
            } else {
                chkAG25YLow1.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("P47")).equals("47")) {
                chkAG25YLow2.setChecked(true);
            } else {
                chkAG25YLow2.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("P48")).equals("48")) {
                chkAG25YLow3.setChecked(true);
            } else {
                chkAG25YLow3.setChecked(false);
            }

            cur.moveToNext();
        }
        cur.close();
    }

    private void DataRetriveAG25Tet(String HealthId, String VDate) {
        Cursor cur = C.ReadData("select  " +
                " ifnull(MAX(CASE WHEN adviceCode=26 THEN adviceCode END),'' )As A26,ifnull(MAX(CASE WHEN adviceCode=27 THEN adviceCode END),'' )As A27 ," +
                " ifnull(MAX(CASE WHEN adviceCode=28 THEN adviceCode END),'' )As A28,ifnull(MAX(CASE WHEN adviceCode=29 THEN adviceCode END),'' )As A28 ," +
                " ifnull(MAX(CASE WHEN adviceCode=30 THEN adviceCode END),'' )As A30,ifnull(MAX(CASE WHEN adviceCode=71 THEN adviceCode END),'' )As A30a,ifnull(MAX(CASE WHEN adviceCode=31 THEN adviceCode END),'' )As A29 ," +
                " ifnull(MAX(CASE WHEN adviceCode=31 THEN adviceCode END),'' )As A31 ," +
                " ifnull(MAX(CASE WHEN adviceCode=32 THEN adviceCode END ),'' )As A32 ," +
                " ifnull(MAX(CASE WHEN adviceCode=33 THEN adviceCode END ),'' )As A33 ," +
                " ifnull(MAX(CASE WHEN adviceCode=34 THEN adviceCode END ),'' )As A34 ," +
                " ifnull(MAX(CASE WHEN adviceCode=35 THEN adviceCode END ),'' )As A35 ,ifnull(MAX(CASE WHEN adviceCode=67 THEN adviceCode END ),'' )As A35a ," +
                " ifnull(MAX(CASE WHEN adviceCode=36 THEN adviceCode END ),'' )As A36 ," +
                " ifnull(MAX(CASE WHEN adviceCode=37 THEN adviceCode END ),'' )As A37 ," +
                " ifnull(MAX(CASE WHEN adviceCode=38 THEN adviceCode END ),'' )As A38 ,ifnull(MAX(CASE WHEN adviceCode=67 THEN adviceCode END ),'' )As A38a ," +
                " ifnull(MAX(CASE WHEN adviceCode=39 THEN adviceCode END ),'' )As A39 ," +
                " ifnull(MAX(CASE WHEN adviceCode=40 THEN adviceCode END ),'' )As A40 ," +
                " ifnull(MAX(CASE WHEN adviceCode=41 THEN adviceCode END ),'' )As A41 ," +
                " ifnull(MAX(CASE WHEN adviceCode=42 THEN adviceCode END ),'' )As A42 ,ifnull(MAX(CASE WHEN adviceCode=71 THEN adviceCode END ),'' )As A42a ," +
                " ifnull(MAX(CASE WHEN adviceCode=43 THEN adviceCode END ),'' )As A43 ," +
                " ifnull(MAX(CASE WHEN adviceCode=44 THEN adviceCode END ),'' )As A44 ," +
                " ifnull(MAX(CASE WHEN adviceCode=45 THEN adviceCode END ),'' )As A45 ," +
                " ifnull(MAX(CASE WHEN adviceCode=46 THEN adviceCode END ),'' )As A46 ,ifnull(MAX(CASE WHEN adviceCode=71 THEN adviceCode END ),'' )As A46a ," +
                " ifnull(MAX(CASE WHEN adviceCode=47 THEN adviceCode END ),'' )As A47 ," +
                " ifnull(MAX(CASE WHEN adviceCode=48 THEN adviceCode END ),'' )As A48 ," +
                " ifnull(MAX(CASE WHEN adviceCode=49 THEN adviceCode END ),'' )As A49 ," +
                " ifnull(MAX(CASE WHEN adviceCode=50 THEN adviceCode END ),'' )As A50 ," +
                " ifnull(MAX(CASE WHEN adviceCode=51 THEN adviceCode END ),'' )As A51 ," +
                " ifnull(MAX(CASE WHEN adviceCode=52 THEN adviceCode END ),'' )As A52 ," +
                " ifnull(MAX(CASE WHEN adviceCode=53 THEN adviceCode END ),'' )As A53 ," +
                " ifnull(MAX(CASE WHEN adviceCode=54 THEN adviceCode END ),'' )As A54 ," +
                " ifnull(MAX(CASE WHEN adviceCode=55 THEN adviceCode END ),'' )As A55 ,ifnull(MAX(CASE WHEN adviceCode=67 THEN adviceCode END ),'' )As A55a ," +
                " ifnull(MAX(CASE WHEN adviceCode=56 THEN adviceCode END ),'' )As A56 ," +
                " ifnull(MAX(CASE WHEN adviceCode=57 THEN adviceCode END ),'' )As A57 ," +
                " ifnull(MAX(CASE WHEN adviceCode=58 THEN adviceCode END ),'' )As A58 ," +
                " ifnull(MAX(CASE WHEN adviceCode=59 THEN adviceCode END ),'' )As A59 ," +
                " ifnull(MAX(CASE WHEN adviceCode=60 THEN adviceCode END ),'' )As A60 ,ifnull(MAX(CASE WHEN adviceCode=67 THEN adviceCode END ),'' )As A60a ," +
                " ifnull(MAX(CASE WHEN adviceCode=61 THEN adviceCode END ),'' )As A61 ," +
                " ifnull(MAX(CASE WHEN adviceCode=62 THEN adviceCode END ),'' )As A62 ,ifnull(MAX(CASE WHEN adviceCode=68 THEN adviceCode END ),'' )As A62a ," +
                " ifnull(MAX(CASE WHEN adviceCode=63 THEN adviceCode END ),'' )As A63 ," +
                " ifnull(MAX(CASE WHEN adviceCode=64 THEN adviceCode END ),'' )As A64 ," +
                " ifnull(MAX(CASE WHEN adviceCode=65 THEN adviceCode END ),'' )As A65 ,ifnull(MAX(CASE WHEN adviceCode=69 THEN adviceCode END ),'' )As A65a ," +
                " ifnull(MAX(CASE WHEN adviceCode=66 THEN adviceCode END ),'' )As A66,ifnull(MAX(CASE WHEN adviceCode=70 THEN adviceCode END ),'' )As A66a  " +
                // " MAX(CASE WHEN problemCode=11 THEN problemCode END) P11,MAX(CASE WHEN problemCode=12 THEN problemCode END) P12 " +
                " FROM  under5ChildAdvice WHERE HealthId = '" + HealthId + "' and strftime('%d/%m/%Y', date(visitDate))= '" + VDate + "'");
        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            if (cur.getString(cur.getColumnIndex("A26")).equals("26")) {
                chkAG25YTet1.setChecked(true);
            } else {
                chkAG25YTet1.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A27")).equals("27")) {
                chkAG25YTet2.setChecked(true);
            } else {
                chkAG25YTet2.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A28")).equals("28")) {
                chkAG25YTet3.setChecked(true);
            } else {
                chkAG25YTet3.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A29")).equals("29")) {
                chkAG25YTet4.setChecked(true);
            } else {
                chkAG25YTet4.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A30")).equals("30")) {
                chkAG25YTet5.setChecked(true);
            } else {
                chkAG25YTet5.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A30a")).equals("71")) {
                chkAG25YTet5a.setChecked(true);
            } else {
                chkAG25YTet5a.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A31")).equals("31")) {
                chkAG25YTet6.setChecked(true);
            } else {
                chkAG25YTet6.setChecked(false);
            }
            if (cur.getString(cur.getColumnIndex("A32")).equals("32")) {
                chkAG25YTet7.setChecked(true);
            } else {
                chkAG25YTet7.setChecked(false);
            }
            if (cur.getString(cur.getColumnIndex("A33")).equals("33")) {
                chkAG25YDarTet1.setChecked(true);
            } else {
                chkAG25YDarTet1.setChecked(false);
            }
            if (cur.getString(cur.getColumnIndex("A34")).equals("34")) {
                chkAG25YDarTet2.setChecked(true);
            } else {
                chkAG25YDarTet2.setChecked(false);
            }


            if (cur.getString(cur.getColumnIndex("A35")).equals("35")) {
                chkAG25YDarTet3.setChecked(true);
            } else {
                chkAG25YDarTet3.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A35a")).equals("67")) {
                chkAG25YDarTet3a.setChecked(true);
            } else {
                chkAG25YDarTet3a.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A36")).equals("36")) {
                chkAG25YDarTet4.setChecked(true);
            } else {
                chkAG25YDarTet4.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A37")).equals("37")) {
                chkAG25YDarTet5.setChecked(true);
            } else {
                chkAG25YDarTet5.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A38")).equals("38")) {
                chkAG25YDarTet6.setChecked(true);
            } else {
                chkAG25YDarTet6.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A38a")).equals("67")) {
                chkAG25YDarTet6a.setChecked(true);
            } else {
                chkAG25YDarTet6a.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A39")).equals("39")) {
                chkAGO2MDarTet5.setChecked(true);
            } else {
                chkAGO2MDarTet5.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A40")).equals("40")) {
                chkAGO2MDarTet6.setChecked(true);
            } else {
                chkAGO2MDarTet6.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A41")).equals("41")) {
                chkAG25YamTet1.setChecked(true);
            } else {
                chkAG25YamTet1.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A42")).equals("42")) {
                chkAG25YamTet2.setChecked(true);
            } else {
                chkAG25YamTet2.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A42a")).equals("71")) {
                chkAG25YamTet2a.setChecked(true);
            } else {
                chkAG25YamTet2a.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A43")).equals("43")) {
                chkAG25YFevTet1.setChecked(true);
            } else {
                chkAG25YFevTet1.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A44")).equals("44")) {
                chkAG25YFevTet2.setChecked(true);
            } else {
                chkAG25YFevTet2.setChecked(false);
            }
            if (cur.getString(cur.getColumnIndex("A45")).equals("45")) {
                chkAG25YFevTet3.setChecked(true);
            } else {
                chkAG25YFevTet3.setChecked(false);
            }
            if (cur.getString(cur.getColumnIndex("A46")).equals("46")) {
                chkAG25YFevTet4.setChecked(true);
            } else {
                chkAG25YFevTet4.setChecked(false);
            }


            if (cur.getString(cur.getColumnIndex("A46a")).equals("71")) {
                chkAG25YFevTet4a.setChecked(true);
            } else {
                chkAG25YFevTet4a.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A47")).equals("47")) {
                chkAG25YMalTet1.setChecked(true);
            } else {
                chkAG25YMalTet1.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A48")).equals("48")) {
                chkAG25YMalTet2.setChecked(true);
            } else {
                chkAG25YMalTet2.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A49")).equals("49")) {
                chkAG25YMalTet2a.setChecked(true);
            } else {
                chkAG25YMalTet2a.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A50")).equals("50")) {
                chkAG25YMalTet3.setChecked(true);
            } else {
                chkAG25YMalTet3.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A51")).equals("51")) {
                chkAG25YMalTet4.setChecked(true);
            } else {
                chkAG25YMalTet4.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A52")).equals("52")) {
                chkAG25YMalTet5.setChecked(true);
            } else {
                chkAG25YMalTet5.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A53")).equals("53")) {
                chkAG25YMalTet6.setChecked(true);
            } else {
                chkAG25YMalTet6.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A54")).equals("54")) {
                chkAG25YMalTet7.setChecked(true);
            } else {
                chkAG25YMalTet7.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A55")).equals("55")) {
                chkAG25YMalTet8.setChecked(true);
            } else {
                chkAG25YMalTet8.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A55a")).equals("67")) {
                chkAG25YMalTet8a.setChecked(true);
            } else {
                chkAG25YMalTet8a.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A56")).equals("56")) {
                chkAG25YNutTet1.setChecked(true);
            } else {
                chkAG25YNutTet1.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A57")).equals("57")) {
                chkAG25YNutTet2.setChecked(true);
            } else {
                chkAG25YNutTet2.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A58")).equals("58")) {
                chkAG25YNutTet3.setChecked(true);
            } else {
                chkAG25YNutTet3.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A59")).equals("59")) {
                chkAG25YNutTet4.setChecked(true);
            } else {
                chkAG25YNutTet4.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A60")).equals("60")) {
                chkAG25YNutTet5.setChecked(true);
            } else {
                chkAG25YNutTet5.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A60a")).equals("67")) {
                chkAG25YNutTet5a.setChecked(true);
            } else {
                chkAG25YNutTet5a.setChecked(false);
            }


            if (cur.getString(cur.getColumnIndex("A61")).equals("61")) {
                chkAG25YNutTet6.setChecked(true);
            } else {
                chkAG25YNutTet6.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A62")).equals("62")) {
                chkAG25YNutTet7.setChecked(true);
            } else {
                chkAG25YNutTet7.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A62a")).equals("68")) {
                chkAG25YNutTet7a.setChecked(true);
            } else {
                chkAG25YNutTet7a.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A62")).equals("62")) {
                chkAG25YLowTet1.setChecked(true);
            } else {
                chkAG25YLowTet1.setChecked(false);
            }


            if (cur.getString(cur.getColumnIndex("A63")).equals("63")) {
                chkAG25YLowTet2.setChecked(true);
            } else {
                chkAG25YLowTet2.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A64")).equals("64")) {
                chkAG25YLowTet3.setChecked(true);
            } else {
                chkAG25YLowTet3.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A65")).equals("65")) {
                chkAG25YLowTet4.setChecked(true);
            } else {
                chkAG25YLowTet4.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A65a")).equals("69")) {
                chkAG25YLowTet4a.setChecked(true);
            } else {
                chkAG25YLowTet4a.setChecked(false);
            }


            if (cur.getString(cur.getColumnIndex("A66")).equals("66")) {
                chkAG25YLowTet5.setChecked(true);
            } else {
                chkAG25YLowTet5.setChecked(false);
            }

            if (cur.getString(cur.getColumnIndex("A66a")).equals("70")) {
                chkAG25YLowTet5a.setChecked(true);
            } else {
                chkAG25YLowTet5a.setChecked(false);
            }
            cur.moveToNext();
        }
        cur.close();
    }


    private void AgeGroup2to5Year() {
        secAGO2MGA.setVisibility(View.GONE);
        secAG2to5Nu.setVisibility(View.VISIBLE);
        VAAG25YNu1.setText(g.Getclassfication(C, "9"));
        VAAG25YNu2.setText(g.Getclassfication(C, "10"));
        VAAG25YNu3.setText(g.Getclassfication(C, "11"));
        VAG25YSymCode1.setText(g.GetsymtomCode(C, "23"));
        VVAG25YSym1.setText(g.GetsymtomDes(C, "23"));
        VAG25YSymCode2.setText(g.GetsymtomCode(C, "24"));
        VVAG25YSym2.setText(g.GetsymtomDes(C, "24"));
        VAG25YSymCode3.setText(g.GetsymtomCode(C, "25"));
        VVAG25YSym3.setText(g.GetsymtomDes(C, "25"));
        VAG25YSymCode4.setText(g.GetsymtomCode(C, "26"));
        VVAG25YSym4.setText(g.GetsymtomDes(C, "26"));

        VAG25YTetCode1.setText(g.GettreatmentCode(C, "26"));
        VVAG25YTet1.setText(g.GettreatmentDes(C, "26"));

        VAG25YTetCode2.setText(g.GettreatmentCode(C, "27"));
        VVAG25YTet2.setText(g.GettreatmentDes(C, "27"));


        VAG25YTetCode3.setText(g.GettreatmentCode(C, "28"));
        VVAG25YTet3.setText(g.GettreatmentDes(C, "28"));


        VAG25YTetCode5.setText(g.GettreatmentCode(C, "30"));
        VVAG25YTet5.setText(g.GettreatmentDes(C, "30"));

        VAG25YTetCode5a.setText(g.GettreatmentCode(C, "71"));
        VVAG25YTet5a.setText(g.GettreatmentDes(C, "71"));

        VAG25YTetCode7.setText(g.GettreatmentCode(C, "32"));
        VVAG25YTet7.setText(g.GettreatmentDes(C, "32"));


        VAG25YTetCode4.setText(g.GettreatmentCode(C, "29"));
        VVAG25YTet4.setText(g.GettreatmentDes(C, "29"));
        VAG25YTetCode6.setText(g.GettreatmentCode(C, "31"));
        VVAG25YTet6.setText(g.GettreatmentDes(C, "31"));
        VAG25YDarCode1.setText(g.GetsymtomCode(C, "28"));
        VVAG25YDar1.setText(g.GetsymtomDes(C, "28"));
        VAG25YDarCode2.setText(g.GetsymtomCode(C, "29"));
        VVAG25YDar2.setText(g.GetsymtomDes(C, "29"));
        VAG25YDarCode3.setText(g.GetsymtomCode(C, "30"));
        VVAG25YDar3.setText(g.GetsymtomDes(C, "30"));
        VAG25YDarCode5.setText(g.GetsymtomCode(C, "32"));
        VVAG25YDar5.setText(g.GetsymtomDes(C, "32"));
        VAG25YDarCode4.setText(g.GetsymtomCode(C, "31"));
        VVAG25YDar4.setText(g.GetsymtomDes(C, "31"));
        VAG25YDarCode6.setText(g.GetsymtomCode(C, "33"));
        VVAG25YDar6.setText(g.GetsymtomDes(C, "33"));


        VAG25YDarCode9.setText(g.GetsymtomCode(C, "36"));
        VVAG25YDar9.setText(g.GetsymtomDes(C, "36"));
        VAG25YDarCode7.setText(g.GetsymtomCode(C, "34"));
        VVAG25YDar7.setText(g.GetsymtomDes(C, "34"));

        VAG25YDarCode8.setText(g.GetsymtomCode(C, "35"));
        VVAG25YDar8.setText(g.GetsymtomDes(C, "35"));

        VAG25YDarC1.setText(g.Getclassfication(C, "12"));
        VAG25YDarTet1.setText(g.GettreatmentDes(C, "33"));
        VAG25YDarCodeTet1.setText(g.GettreatmentCode(C, "33"));


        VAG25YDarC2.setText(g.Getclassfication(C, "13"));
        VAG25YDarCodeTet2.setText(g.GettreatmentCode(C, "34"));
        VAG25YDarTet2.setText(g.GettreatmentDes(C, "34"));


        VAG25YDarCodeTet3.setText(g.GettreatmentCode(C, "35"));
        VAG25YDarTet3.setText(g.GettreatmentDes(C, "35"));

        VAG25YDarCodeTet3a.setText(g.GettreatmentCode(C, "67"));
        VAG25YDarTet3a.setText(g.GettreatmentDes(C, "67"));


        VAG25YDarC3.setText(g.Getclassfication(C, "14"));
        VAG25YDarCodeTet4.setText(g.GettreatmentCode(C, "36"));
        VAG25YDarTet4.setText(g.GettreatmentDes(C, "36"));

        VAG25YDarCodeTet5.setText(g.GettreatmentCode(C, "37"));
        VAG25YDarTet5.setText(g.GettreatmentDes(C, "37"));

        VAG25YDarCodeTet6.setText(g.GettreatmentCode(C, "38"));
        VAG25YDarTet6.setText(g.GettreatmentDes(C, "38"));

        VAG25YDarCodeTet6a.setText(g.GettreatmentCode(C, "67"));
        VAG25YDarTet6a.setText(g.GettreatmentDes(C, "67"));

        VAG25YAmCode1.setText(g.GetsymtomCode(C, "37"));
        VVAG25YAm1.setText(g.GetsymtomDes(C, "37"));

        VAG25YAmC1.setText(g.Getclassfication(C, "15"));
        VAG25YAmCodeTet1.setText(g.GettreatmentCode(C, "39"));
        VAG25YAmTet1.setText(g.GettreatmentDes(C, "39"));

        VAG25YAmCodeTet2.setText(g.GettreatmentCode(C, "40"));
        VAG25YAmTet2.setText(g.GettreatmentDes(C, "40"));

        VAG25YAmCodeTet2a.setText(g.GettreatmentCode(C, "71"));
        VAG25YAmTet2a.setText(g.GettreatmentDes(C, "71"));

        VAG25YFevCode1.setText(g.GetsymtomCode(C, "38"));
        VVAG25YFev1.setText(g.GetsymtomDes(C, "38"));


        VAG25YFevCode2.setText(g.GetsymtomCode(C, "39"));
        VVAG25YFev2.setText(g.GetsymtomDes(C, "39"));


        VAG25YFevC1.setText(g.Getclassfication(C, "16"));
        VAG25YFevCodeTet1.setText(g.GettreatmentCode(C, "41"));
        VAG25YFevTet1.setText(g.GettreatmentDes(C, "41"));

        VAG25YFevCodeTet2.setText(g.GettreatmentCode(C, "42"));
        VAG25YFevTet2.setText(g.GettreatmentDes(C, "42"));

        VAG25YFevCodeTet3.setText(g.GettreatmentCode(C, "43"));
        VAG25YFevTet3.setText(g.GettreatmentDes(C, "43"));

        VAG25YFevCodeTet4.setText(g.GettreatmentCode(C, "44"));
        VAG25YFevTet4.setText(g.GettreatmentDes(C, "44"));

        VAG25YFevCodeTet4a.setText(g.GettreatmentCode(C, "71"));
        VAG25YFevTet4a.setText(g.GettreatmentDes(C, "71"));


        VAG25YMalCode1.setText(g.GetsymtomCode(C, "40"));
        VVAG25YMal1.setText(g.GetsymtomDes(C, "40"));


        VAG25YMalCode2.setText(g.GetsymtomCode(C, "41"));
        VVAG25YMal2.setText(g.GetsymtomDes(C, "41"));


        VAG25YMalC1.setText(g.Getclassfication(C, "17"));
        VAG25YMalC2.setText(g.Getclassfication(C, "18"));
        VAG25YMalCodeTet1.setText(g.GettreatmentCode(C, "45"));
        VAG25YMalTet1.setText(g.GettreatmentDes(C, "45"));

        VAG25YMalCodeTet2.setText(g.GettreatmentCode(C, "46"));
        VAG25YMalTet2.setText(g.GettreatmentDes(C, "46"));

        VAG25YMalCodeTet2a.setText(g.GettreatmentCode(C, "47"));
        VAG25YMalTet2a.setText(g.GettreatmentDes(C, "47"));


        VAG25YMalCodeTet3.setText(g.GettreatmentCode(C, "48"));
        VAG25YMalTet3.setText(g.GettreatmentDes(C, "48"));

        VAG25YMalCodeTet4.setText(g.GettreatmentCode(C, "49"));
        VAG25YMalTet4.setText(g.GettreatmentDes(C, "49"));

        VAG25YMalCodeTet5.setText(g.GettreatmentCode(C, "50"));
        VAG25YMalTet5.setText(g.GettreatmentDes(C, "50"));

        VAG25YMalCodeTet6.setText(g.GettreatmentCode(C, "51"));
        VAG25YMalTet6.setText(g.GettreatmentDes(C, "51"));

        VAG25YMalCodeTet7.setText(g.GettreatmentCode(C, "52"));
        VAG25YMalTet7.setText(g.GettreatmentDes(C, "52"));

        VAG25YMalCodeTet8.setText(g.GettreatmentCode(C, "53"));
        VAG25YMalTet8.setText(g.GettreatmentDes(C, "53"));

        VAG25YMalCodeTet8a.setText(g.GettreatmentCode(C, "67"));
        VAG25YMalTet8a.setText(g.GettreatmentDes(C, "67"));

        VAG25YNutCode1.setText(g.GetsymtomCode(C, "42"));
        VVAG25YNut1.setText(g.GetsymtomDes(C, "42"));


        VAG25YNutCode2.setText(g.GetsymtomCode(C, "43"));
        VVAG25YNut2.setText(g.GetsymtomDes(C, "43"));

        VAG25YNutCode3.setText(g.GetsymtomCode(C, "44"));
        VVAG25YNut3.setText(g.GetsymtomDes(C, "44"));

        VAG25YNutCode4.setText(g.GetsymtomCode(C, "45"));
        VVAG25YNut4.setText(g.GetsymtomDes(C, "45"));

        VAG25YNutC1.setText(g.Getclassfication(C, "19"));
        VAG25YNutC2.setText(g.Getclassfication(C, "20"));
        VAG25YNutC3.setText(g.Getclassfication(C, "21"));
        VAG25YNutCodeTet1.setText(g.GettreatmentCode(C, "54"));
        VAG25YNutTet1.setText(g.GettreatmentDes(C, "54"));


        VAG25YNutCodeTet2.setText(g.GettreatmentCode(C, "56"));
        VAG25YNutTet2.setText(g.GettreatmentDes(C, "56"));

        VAG25YNutCodeTet3.setText(g.GettreatmentCode(C, "57"));
        VAG25YNutTet3.setText(g.GettreatmentDes(C, "57"));

        VAG25YNutCodeTet4.setText(g.GettreatmentCode(C, "58"));
        VAG25YNutTet4.setText(g.GettreatmentDes(C, "58"));

        VAG25YNutCodeTet5.setText(g.GettreatmentCode(C, "59"));
        VAG25YNutTet5.setText(g.GettreatmentDes(C, "59"));

        VAG25YNutCodeTet5a.setText(g.GettreatmentCode(C, "67"));
        VAG25YNutTet5a.setText(g.GettreatmentDes(C, "67"));

        VAG25YNutCodeTet6.setText(g.GettreatmentCode(C, "60"));
        VAG25YNutTet6.setText(g.GettreatmentDes(C, "60"));

        VAG25YNutCodeTet7.setText(g.GettreatmentCode(C, "61"));
        VAG25YNutTet7.setText(g.GettreatmentDes(C, "61"));

        VAG25YNutCodeTet7a.setText(g.GettreatmentCode(C, "68"));
        VAG25YNutTet7a.setText(g.GettreatmentDes(C, "68"));


        VAG25YLowCode1.setText(g.GetsymtomCode(C, "46"));
        VVAG25YLow1.setText(g.GetsymtomDes(C, "46"));


        VAG25YLowCode2.setText(g.GetsymtomCode(C, "47"));
        VVAG25YLow2.setText(g.GetsymtomDes(C, "47"));

        VAG25YLowCode3.setText(g.GetsymtomCode(C, "48"));
        VVAG25YLow3.setText(g.GetsymtomDes(C, "48"));

        VAG25YLowC1.setText(g.Getclassfication(C, "22"));
        VAG25YLowC2.setText(g.Getclassfication(C, "23"));
        VAG25YLowC3.setText(g.Getclassfication(C, "24"));
        VAG25YLowCodeTet1.setText(g.GettreatmentCode(C, "62"));
        VAG25YLowTet1.setText(g.GettreatmentDes(C, "62"));

        VAG25YLowCodeTet2.setText(g.GettreatmentCode(C, "63"));
        VAG25YLowTet2.setText(g.GettreatmentDes(C, "63"));


        VAG25YLowCodeTet3.setText(g.GettreatmentCode(C, "64"));
        VAG25YLowTet3.setText(g.GettreatmentDes(C, "64"));

        VAG25YLowCodeTet4.setText(g.GettreatmentCode(C, "65"));
        VAG25YLowTet4.setText(g.GettreatmentDes(C, "65"));

        VAG25YLowCodeTet4a.setText(g.GettreatmentCode(C, "69"));
        VAG25YLowTet4a.setText(g.GettreatmentDes(C, "69"));


        VAG25YLowCodeTet5.setText(g.GettreatmentCode(C, "66"));
        VAG25YLowTet5.setText(g.GettreatmentDes(C, "66"));

        VAG25YLowCodeTet5a.setText(g.GettreatmentCode(C, "70"));
        VAG25YLowTet5a.setText(g.GettreatmentDes(C, "70"));


    }


    private void Classification() {

        tableLayout1.setVisibility(View.GONE);
        tableLayout2.setVisibility(View.GONE);
        tableLayout3.setVisibility(View.GONE);
        // secGAC1.setVisibility(View.GONE);
        // secGA1.setVisibility(View.GONE);
        //secGAC2.setVisibility(View.GONE);
        // secGA2.setVisibility(View.GONE);
        // secGAC3.setVisibility(View.GONE);
        // secGA3.setVisibility(View.GONE);
        // secAGO2MGATetClass1.setVisibility(View.GONE);
        //  secAGO2MGATetClass2.setVisibility(View.GONE);
        //  secAGO2MGATetClass3.setVisibility(View.GONE);

        //মারাত্মক রক্ত  স্বল্পতা
        if (chkGeA1.isChecked() == true || chkGeA2.isChecked() == true || chkGeA3.isChecked() == true || chkGeA4.isChecked() == true || chkGeA5.isChecked() == true || chkGeA6.isChecked() == true) {
            tableLayout1.setVisibility(View.VISIBLE);
            // secGA1.setVisibility(View.VISIBLE);
            //  secAGO2MGATetClass1.setVisibility(View.VISIBLE);
        }

        //সীমিত সংক্রমন
        // else
        else if (chkGeA7.isChecked() == true || chkGeA8.isChecked() == true) {
            tableLayout2.setVisibility(View.VISIBLE);
            //  secGAC2.setVisibility(View.VISIBLE);
            //  secGA2.setVisibility(View.VISIBLE);
            // secAGO2MGATetClass2.setVisibility(View.VISIBLE);
        }

        //খুব মারাত্মক রোগ অথবা সীমিত সংক্রমনের সম্ভাবনা কম
        // else
        else if (chkGeA9.isChecked() == true) {
            tableLayout3.setVisibility(View.VISIBLE);
            //  secGAC3.setVisibility(View.VISIBLE);
            //  secGA3.setVisibility(View.VISIBLE);
            //  secAGO2MGATetClass3.setVisibility(View.VISIBLE);
        }

        if (chkGeA1.isChecked() == false && chkGeA2.isChecked() == false && chkGeA3.isChecked() == false && chkGeA4.isChecked() == false && chkGeA5.isChecked() == false && chkGeA6.isChecked() == false) {


            secAGO2MGATetClass1.setVisibility(View.GONE);
            chkGeATet1.setChecked(false);
            chkGeATet2.setChecked(false);
            chkGeATet3.setChecked(false);
            chkGeATet4.setChecked(false);

        }

        if (chkGeA7.isChecked() == false && chkGeA8.isChecked() == false) {
            secAGO2MGATetClass2.setVisibility(View.GONE);
            chkGeATet5.setChecked(false);
            chkGeATet6.setChecked(false);
            chkGeATet7.setChecked(false);
            chkGeATet7a.setChecked(false);
        }

        if (chkGeA9.isChecked() == false) {
            secAGO2MGATetClass3.setVisibility(View.GONE);
            chkGeATet8.setChecked(false);
            chkGeATet9.setChecked(false);
            chkGeATet9a.setChecked(false);
        }
    }
//

    private void ClassificationDar() {
        tableLayout4a.setVisibility(View.GONE);
        tableLayout5.setVisibility(View.GONE);
        //secAGO2MDarC1.setVisibility(View.GONE);
        //secGA4.setVisibility(View.GONE);
        //secAGO2MDarC2.setVisibility(View.GONE);
        //secGA5.setVisibility(View.GONE);
        // secAGO2MDarTetClass1.setVisibility(View.GONE);
        // secAGO2MDarTetClass2.setVisibility(View.GONE);
        // secAGO2MDarC1.setVisibility(View.GONE);
        // secAGO2MDarC11.setVisibility(View.GONE);


        //চরম পানি  স্বল্পতা
        if ((chkAGO2MDar1.isChecked() == true && chkAGO2MDar2.isChecked() == true) || (chkAGO2MDar1.isChecked() == true && chkAGO2MDar3.isChecked() == true) || (chkAGO2MDar2.isChecked() == true && chkAGO2MDar3.isChecked() == true)) {
            tableLayout4a.setVisibility(View.VISIBLE);
            //secGA4.setVisibility(View.VISIBLE);
            VAGO2MDarC1.setText(g.Getclassfication(C, "4"));
            //secAGO2MDarC1.setVisibility(View.VISIBLE);
            // secAGO2MDarTetClass1.setVisibility(View.VISIBLE);
        } else if ((chkAGO2MDar4.isChecked() == true && chkAGO2MDar5.isChecked() == true) || (chkAGO2MDar4.isChecked() == true && chkAGO2MDar6.isChecked() == true) || (chkAGO2MDar5.isChecked() == true && chkAGO2MDar6.isChecked() == true))


        {
            tableLayout4a.setVisibility(View.VISIBLE);
            //secGA4.setVisibility(View.VISIBLE);
            VAGO2MDarC1.setText(g.Getclassfication(C, "5"));
            //secAGO2MDarC1.setVisibility(View.VISIBLE);
            //  secAGO2MDarTetClass1.setVisibility(View.VISIBLE);
        }
        //পানি স্বল্পতা নাই
        else if (chkAGO2MDar7.isChecked() == true) {
            tableLayout5.setVisibility(View.VISIBLE);
            // secGA5.setVisibility(View.VISIBLE);
            VAGO2MDarC2.setText(g.Getclassfication(C, "6"));
            // secAGO2MDarC2.setVisibility(View.VISIBLE);
            // secAGO2MDarTetClass2.setVisibility(View.VISIBLE);
        }


        if (chkAGO2MDar1.isChecked() == false && chkAGO2MDar2.isChecked() == false && chkAGO2MDar3.isChecked() == false && chkAGO2MDar4.isChecked() == false && chkAGO2MDar5.isChecked() == false && chkAGO2MDar6.isChecked() == false) {
            secAGO2MDarTetClass1.setVisibility(View.GONE);
            chkAGO2MDarTet1.setChecked(false);
            chkAGO2MDarTet2.setChecked(false);
            chkAGO2MDarTet3.setChecked(false);
        }
        if (chkAGO2MDar7.isChecked() == true) {
            secAGO2MDarTetClass2.setVisibility(View.GONE);
            chkAGO2MDarTet4.setChecked(false);
            chkAGO2MDarTet5.setChecked(false);
        }
    }


    private void ClassificationFeedingProb() {


        tableLayout6a.setVisibility(View.GONE);
        //secGA6.setVisibility(View.GONE);

        tableLayout7.setVisibility(View.GONE);
        // secGA7.setVisibility(View.GONE);

        //খাওয়ানোর সমস্যা
        if (chkAGO2MFed1.isChecked() == true || chkAGO2MFed2.isChecked() == true || chkAGO2MFed3.isChecked() == true || chkAGO2MFed4.isChecked() == true || chkAGO2MFed5.isChecked() == true) {
            tableLayout6a.setVisibility(View.VISIBLE);
            tableLayout7.setVisibility(View.GONE);
            // secGA6.setVisibility(View.VISIBLE);
            // secAGO2MFedC1.setVisibility(View.VISIBLE);
            // secAGO2MFeedTetClass1.setVisibility(View.VISIBLE);

        }
        //খাওয়ানোর সমস্যা নাই
        //else
        else if (chkAGO2MFed6.isChecked() == true) {
            tableLayout7.setVisibility(View.VISIBLE);
            //secGA7.setVisibility(View.VISIBLE);
            tableLayout6a.setVisibility(View.GONE);
            //secAGO2MFeedTetClass2.setVisibility(View.VISIBLE);
        }

        if (chkAGO2MFed1.isChecked() == false && chkAGO2MFed2.isChecked() == false && chkAGO2MFed3.isChecked() == false && chkAGO2MFed4.isChecked() == false && chkAGO2MFed5.isChecked() == false) {
            secAGO2MFeedTetClass1.setVisibility(View.GONE);
            chkAGO2MFedTet1.setChecked(false);
            chkAGO2MFedTet2.setChecked(false);
            chkAGO2MFedTet3.setChecked(false);
            chkAGO2MFedTet4.setChecked(false);
            chkAGO2MFedTet5.setChecked(false);

        }

        if (chkAGO2MFed6.isChecked() == false) {
            secAGO2MFeedTetClass2.setVisibility(View.GONE);
            chkAGO2MFedTet6.setChecked(false);
            chkAGO2MFedTet7.setChecked(false);
        }

    }

    private void ClassificationFeedingAG25YNum() {

        //secAG25YNuClass1.setVisibility(View.GONE);
        // secAG25YNuClass2.setVisibility(View.GONE);
        //  secAG25YNuClass3.setVisibility(View.GONE);
        tableLayout8a.setVisibility(View.GONE);
        tableLayout9.setVisibility(View.GONE);
        tableLayout10.setVisibility(View.GONE);
        //  secGA8.setVisibility(View.GONE);
        // secGA9.setVisibility(View.GONE);
        // secGA10.setVisibility(View.GONE);


        //মারাত্মক নিউমোনিয়া অথবা খুব মারাত্মক রোগ
        if (chkAG25YSym1.isChecked() == true || chkAG25YSym2.isChecked() == true) {
            tableLayout8a.setVisibility(View.VISIBLE);
            // secGA8.setVisibility(View.VISIBLE);
            // secAG25YNuClass1.setVisibility(View.VISIBLE);

        }
        //নিউমোনিয়া

        else if (chkAG25YSym3.isChecked() == true) {
            tableLayout9.setVisibility(View.VISIBLE);
            // secGA9.setVisibility(View.VISIBLE);
            // secAG25YNuClass2.setVisibility(View.VISIBLE);
        }


        // নিউমোনিয়া নয়:কাশি অথবা সর্দি

        else if (chkAG25YSym4.isChecked() == true) {
            tableLayout10.setVisibility(View.VISIBLE);
            // secGA10.setVisibility(View.VISIBLE);
            //secAG25YNuClass3.setVisibility(View.VISIBLE);
        }

        if (chkAG25YSym1.isChecked() == false && chkAG25YSym2.isChecked() == false) {
            secAG25YNuClass1.setVisibility(View.GONE);
            chkAG25YTet1.setChecked(false);
            chkAG25YTet2.setChecked(false);

        }
        if (chkAG25YSym3.isChecked() == false) {
            secAG25YNuClass2.setVisibility(View.GONE);
            chkAG25YTet3.setChecked(false);
            chkAG25YTet4.setChecked(false);
            chkAG25YTet5.setChecked(false);
            chkAG25YTet5a.setChecked(false);
        }

        if (chkAG25YSym4.isChecked() == false) {
            secAG25YNuClass3.setVisibility(View.GONE);
            chkAG25YTet6.setChecked(false);
            chkAG25YTet7.setChecked(false);
        }
    }


    private void ClassificationFeedingAG25YDar() {

        // secAG25YDarTetClass1.setVisibility(View.GONE);
        // secAG25YDarTetClass2.setVisibility(View.GONE);
        //  secAG25YDarTetClass3.setVisibility(View.GONE);
        // secAG25YDarC1.setVisibility(View.GONE);
        // secGA11.setVisibility(View.GONE);

        tableLayout11a.setVisibility(View.GONE);
        tableLayout12.setVisibility(View.GONE);
        tableLayout13.setVisibility(View.GONE);
        tableLayout14.setVisibility(View.GONE);
        //secGA13.setVisibility(View.GONE);
        // secAG25YNu1.setVisibility(View.GONE);
        //মারাত্মক নিউমোনিয়া অথবা খুব মারাত্মক রোগ
        if ((chkAG25YDar1.isChecked() == true && chkAG25YDar2.isChecked() == true) ||
                (chkAG25YDar1.isChecked() == true && chkAG25YDar3.isChecked() == true) ||
                (chkAG25YDar1.isChecked() == true && chkAG25YDar4.isChecked() == true) ||
                (chkAG25YDar2.isChecked() == true && chkAG25YDar3.isChecked() == true) ||
                (chkAG25YDar2.isChecked() == true && chkAG25YDar4.isChecked() == true) ||
                (chkAG25YDar2.isChecked() == true && chkAG25YDar1.isChecked() == true) ||
                (chkAG25YDar3.isChecked() == true && chkAG25YDar1.isChecked() == true) ||
                (chkAG25YDar3.isChecked() == true && chkAG25YDar2.isChecked() == true) ||
                (chkAG25YDar3.isChecked() == true && chkAG25YDar4.isChecked() == true)) {
            tableLayout11a.setVisibility(View.VISIBLE);
            tableLayout12.setVisibility(View.GONE);
            tableLayout13.setVisibility(View.GONE);
            tableLayout14.setVisibility(View.GONE);
            // secGA11.setVisibility(View.VISIBLE);
          /*  secAG25YDarC2.setVisibility(View.VISIBLE);
            secGA12.setVisibility(View.VISIBLE);
            secAG25YDarC3.setVisibility(View.VISIBLE);
            secGA13.setVisibility(View.VISIBLE);*/
            // secAG25YDarTetClass1.setVisibility(View.VISIBLE);
            // secGA8.setVisibility(View.VISIBLE);

        }
        //নিউমোনিয়া

        else if ((chkAG25YDar5.isChecked() == true && chkAG25YDar6.isChecked() == true) ||
                (chkAG25YDar5.isChecked() == true && chkAG25YDar7.isChecked() == true) ||
                (chkAG25YDar5.isChecked() == true && chkAG25YDar8.isChecked() == true) ||
                (chkAG25YDar6.isChecked() == true && chkAG25YDar7.isChecked() == true) ||
                (chkAG25YDar6.isChecked() == true && chkAG25YDar8.isChecked() == true) ||
                (chkAG25YDar6.isChecked() == true && chkAG25YDar5.isChecked() == true) ||
                (chkAG25YDar7.isChecked() == true && chkAG25YDar5.isChecked() == true) ||
                (chkAG25YDar7.isChecked() == true && chkAG25YDar6.isChecked() == true) ||
                (chkAG25YDar7.isChecked() == true && chkAG25YDar8.isChecked() == true))
        //else  if(chkAG25YDar5.isChecked()==true||chkAG25YDar6.isChecked()==true|chkAG25YDar7.isChecked()==true||chkAG25YDar8.isChecked()==true)
        {
            tableLayout12.setVisibility(View.VISIBLE);

            tableLayout11a.setVisibility(View.GONE);
            tableLayout13.setVisibility(View.GONE);
            tableLayout14.setVisibility(View.GONE);
            //secGA12.setVisibility(View.VISIBLE);
            // secAG25YDarTetClass2.setVisibility(View.VISIBLE);
            // secGA9.setVisibility(View.VISIBLE);
        }


        // নিউমোনিয়া নয়:কাশি অথবা সর্দি

        else if (chkAG25YDar9.isChecked() == true) {
            tableLayout13.setVisibility(View.VISIBLE);
            tableLayout11a.setVisibility(View.GONE);
            tableLayout12.setVisibility(View.GONE);
            tableLayout14.setVisibility(View.GONE);
            //secGA13.setVisibility(View.VISIBLE);
            //  secAG25YDarTetClass3.setVisibility(View.VISIBLE);
            //secGA10.setVisibility(View.VISIBLE);
        } else if (chkAG25YAm1.isChecked() == true) {
            //  secAG25YAmSymtom.setVisibility(View.VISIBLE);
            tableLayout14.setVisibility(View.VISIBLE);
            //  secGA14.setVisibility(View.VISIBLE);

            tableLayout11a.setVisibility(View.GONE);
            tableLayout12.setVisibility(View.GONE);
            tableLayout13.setVisibility(View.GONE);
        }

        if (chkAG25YDar1.isChecked() == false && chkAG25YDar2.isChecked() == false && chkAG25YDar3.isChecked() == false && chkAG25YDar4.isChecked() == false) {
            secAG25YDarTetClass1.setVisibility(View.GONE);
            chkAG25YDarTet1.setChecked(false);
        }

        if (chkAG25YDar5.isChecked() == false && chkAG25YDar6.isChecked() == false && chkAG25YDar7.isChecked() == false && chkAG25YDar8.isChecked() == false) {
            secAG25YDarTetClass2.setVisibility(View.GONE);
            chkAG25YDarTet2.setChecked(false);
            chkAG25YDarTet3.setChecked(false);
            chkAG25YDarTet3a.setChecked(false);
        }
        if (chkAG25YDar9.isChecked() == false) {
            secAG25YDarTetClass3.setVisibility(View.GONE);
            chkAG25YDarTet4.setChecked(false);
            chkAG25YDarTet5.setChecked(false);
            chkAG25YDarTet6.setChecked(false);
            chkAG25YDarTet6a.setChecked(false);
        }

        if (chkAG25YAm1.isChecked() == false) {
            secAG25YAmSymtom.setVisibility(View.GONE);
            chkAG25YamTet1.setChecked(false);
            chkAG25YamTet2.setChecked(false);
            chkAG25YamTet2a.setChecked(false);
            // secAG25YAmC1.setVisibility(View.GONE);
            // secGA14.setVisibility(View.GONE);
        }
    }


    private void ClassificationFeedingAG25YFever() {
        tableLayout15a.setVisibility(View.GONE);
        // secGA15.setVisibility(View.GONE);
        // secGA16.setVisibility(View.GONE);
        tableLayout16.setVisibility(View.GONE);
        //  secGA17.setVisibility(View.GONE);
        tableLayout17.setVisibility(View.GONE);
        // secAG25YFevSymtom.setVisibility(View.GONE);

        //খুব মারাত্মক জ্বর  জনিত রোগ
        if (chkAG25YFev1.isChecked() == true || chkAG25YFev2.isChecked() == true) {
            tableLayout15a.setVisibility(View.VISIBLE);
            tableLayout16.setVisibility(View.GONE);
            tableLayout17.setVisibility(View.GONE);
            // secGA15.setVisibility(View.VISIBLE);
            // secAG25YFevSymtom.setVisibility(View.VISIBLE);

        } else if (chkAG25YMal1.isChecked() == true) {
            tableLayout16.setVisibility(View.VISIBLE);
            tableLayout15a.setVisibility(View.GONE);
            tableLayout17.setVisibility(View.GONE);
            // secAG25YMalC1.setVisibility(View.VISIBLE);
            // secAG25YMalSymtom1.setVisibility(View.VISIBLE);

        }
        //জ্বর সম্ভবত ম্যালেরিয়া  নয়
        else if (chkAG25YMal2.isChecked() == true) {
            tableLayout17.setVisibility(View.VISIBLE);
            // secAG25YMalC2.setVisibility(View.VISIBLE);
            tableLayout15a.setVisibility(View.GONE);
            tableLayout16.setVisibility(View.GONE);

        }

        if (chkAG25YFev1.isChecked() == false && chkAG25YFev2.isChecked() == false) {
            secAG25YFevSymtom.setVisibility(View.GONE);
            chkAG25YFevTet1.setChecked(false);
            chkAG25YFevTet2.setChecked(false);
            chkAG25YFevTet3.setChecked(false);
            chkAG25YFevTet4.setChecked(false);
            chkAG25YFevTet4a.setChecked(false);

        }

        //ম্যালেরিয়া

        if (chkAG25YMal1.isChecked() == false) {
            secAG25YMalSymtom1.setVisibility(View.GONE);
            chkAG25YMalTet1.setChecked(false);
            chkAG25YMalTet2.setChecked(false);
            chkAG25YMalTet2a.setChecked(false);
            chkAG25YMalTet3.setChecked(false);
            chkAG25YMalTet4.setChecked(false);
            chkAG25YMalTet5.setChecked(false);

        }
        //জ্বর সম্ভবত ম্যালেরিয়া  নয়
        if (chkAG25YMal2.isChecked() == false) {
            secAG25YMalSymtom2.setVisibility(View.GONE);
            chkAG25YMalTet6.setChecked(false);
            chkAG25YMalTet7.setChecked(false);
            chkAG25YMalTet8.setChecked(false);
            chkAG25YMalTet8a.setChecked(false);

        }


    }


    private void ClassificationFeedingAG25YNut() {

        // secAG25YNutSymtom1.setVisibility(View.GONE);
        // secAG25YNutSymtom2.setVisibility(View.GONE);
        // secAG25YNutSymtom3.setVisibility(View.GONE);
        tableLayout19a.setVisibility(View.GONE);
        tableLayout20.setVisibility(View.GONE);

        tableLayout21.setVisibility(View.GONE);

        //ম্যালেরিয়া
        if (chkAG25YNut1.isChecked() == true | chkAG25YNut2.isChecked() == true) {
            tableLayout19a.setVisibility(View.VISIBLE);
            tableLayout20.setVisibility(View.GONE);

            tableLayout21.setVisibility(View.GONE);

        }
        //জ্বর সম্ভবত ম্যালেরিয়া  নয়
        else if (chkAG25YNut3.isChecked() == true) {
            tableLayout20.setVisibility(View.VISIBLE);
            tableLayout19a.setVisibility(View.GONE);

            tableLayout21.setVisibility(View.GONE);

        } else if (chkAG25YNut4.isChecked() == true) {
            tableLayout21.setVisibility(View.VISIBLE);
            tableLayout20.setVisibility(View.GONE);

            tableLayout19a.setVisibility(View.GONE);
        }


        if (chkAG25YNut1.isChecked() == false && chkAG25YNut2.isChecked() == false) {
            secAG25YNutSymtom1.setVisibility(View.GONE);
            chkAG25YNutTet1.setChecked(false);
            chkAG25YNutTet2.setChecked(false);
            chkAG25YNutTet3.setChecked(false);

        }
        //জ্বর সম্ভবত ম্যালেরিয়া  নয়
        if (chkAG25YNut3.isChecked() == false) {
            secAG25YNutSymtom2.setVisibility(View.GONE);
            chkAG25YNutTet4.setChecked(false);
            chkAG25YNutTet5.setChecked(false);
            chkAG25YNutTet5a.setChecked(false);

        }
        if (chkAG25YNut4.isChecked() == false) {
            secAG25YNutSymtom3.setVisibility(View.GONE);
            chkAG25YNutTet6.setChecked(false);
            chkAG25YNutTet7.setChecked(false);
            chkAG25YNutTet7a.setChecked(false);

        }

    }


    //রক্ত স্বল্পতা
    private void ClassificationFeedingAG25YLow() {


        tableLayout22.setVisibility(View.GONE);
        tableLayout23.setVisibility(View.GONE);
        tableLayout24.setVisibility(View.GONE);
        // secAG25YLowSymtom1.setVisibility(View.GONE);
        // secAG25YLowSymtom2.setVisibility(View.GONE);
        //secAG25YLowSymtom3.setVisibility(View.GONE);
        //Low
        if (chkAG25YLow1.isChecked() == true) {
            tableLayout22.setVisibility(View.VISIBLE);
            tableLayout23.setVisibility(View.GONE);
            tableLayout24.setVisibility(View.GONE);

        }
        //জ্বর সম্ভবত ম্যালেরিয়া  নয়
        else if (chkAG25YLow2.isChecked() == true) {
            tableLayout23.setVisibility(View.VISIBLE);
            tableLayout22.setVisibility(View.GONE);
            tableLayout24.setVisibility(View.GONE);

        } else if (chkAG25YLow3.isChecked() == true) {
            tableLayout24.setVisibility(View.VISIBLE);
            tableLayout22.setVisibility(View.GONE);
            tableLayout23.setVisibility(View.GONE);
            // secAG25YLowSymtom3.setVisibility(View.VISIBLE);


        }

        if (chkAG25YLow1.isChecked() == false) {

            secAG25YLowSymtom1.setVisibility(View.GONE);
            chkAG25YLowTet1.setChecked(false);
        }
        //জ্বর সম্ভবত ম্যালেরিয়া  নয়
        if (chkAG25YLow2.isChecked() == false) {
            secAG25YLowSymtom2.setVisibility(View.GONE);

            chkAG25YLowTet2.setChecked(false);
            chkAG25YLowTet3.setChecked(false);
            chkAG25YLowTet4.setChecked(false);
            chkAG25YLowTet4a.setChecked(false);

        }
        if (chkAG25YLow3.isChecked() == false) {
            secAG25YLowSymtom3.setVisibility(View.GONE);
            chkAG25YLowTet5.setChecked(false);
            chkAG25YLowTet5a.setChecked(false);

        }

    }


    private void ClassificationFeedingAG25YOther() {

        secOth1.setVisibility(View.GONE);
        secOth2.setVisibility(View.GONE);
        secOth3.setVisibility(View.GONE);

        secOth4.setVisibility(View.GONE);
        secOth5.setVisibility(View.GONE);
        secOth6.setVisibility(View.GONE);
        secOth7.setVisibility(View.GONE);

        //ম্যালেরিয়া
        if (chkAG25YOth1.isChecked() == true) {
            secOth1.setVisibility(View.VISIBLE);

        }

        if (chkAG25YOth2.isChecked() == true) {
            secOth2.setVisibility(View.VISIBLE);

        }
        if (chkAG25YOth3.isChecked() == true) {
            secOth3.setVisibility(View.VISIBLE);

        }

        if (chkAG25YOth4.isChecked() == true) {
            secOth4.setVisibility(View.VISIBLE);

        }

        if (chkAG25YOth5.isChecked() == true) {
            secOth5.setVisibility(View.VISIBLE);

        }

        if (chkAG25YOth6.isChecked() == true) {
            secOth6.setVisibility(View.VISIBLE);

        }

        if (chkAG25YOth7.isChecked() == true) {
            secOth7.setVisibility(View.VISIBLE);

        }


    }

    private void DataSave() {
        try {


            if (dtpVDate.getText().toString().length() == 0 & dtpVDate.isShown()) {
                Connection.MessageBox(Under5child.this, "পরিদর্শনের তারিখ এর তারিখ কত লিখুন।");
                dtpVDate.requestFocus();
                return;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = sdf.format(c.getTime());
            Date date1 = sdf.parse(formattedDate);
            Date date4 = sdf.parse(dtpVDate.getText().toString());
            if (date4.after(date1) & dtpVDate.isShown()) {
                Connection.MessageBox(Under5child.this, "ভিজিটের  তারিখ আজকের তারিখ অপেক্ষা বড় হবে না");
                dtpVDate.requestFocus();
                return;
            }

            String SQL = "";

            if (!C.Existence("Select healthId,providerId,visitDate,StartTime,EndTime,Upload from " + TableName + "  where healthId='" + g.getGeneratedId() + "' and providerId='" + g.getProvCode() + "' and strftime('%d/%m/%Y', date(visitDate))= '" + dtpVDate.getText() + "'")) {
                SQL = "Insert into " + TableName + "(healthId,providerId,visitDate,startTime,endTime,systemEntryDate,Upload)Values('" + g.getGeneratedId() + "','" + g.getProvCode() + "','" + Global.DateConvertYMD(dtpVDate.getText().toString()) + "','" + StartTime + "','" + g.CurrentTime24() + "','" + Global.DateTimeNowYMDHMS() + "','2')";
                C.Save(SQL);
            }
            SQL = "Update under5Child set upload='2',";
            //SQL+="visitDate = '"+Global.DateConvertYMD(dtpVDate.getText().toString())+"',";
            SQL += "remarks = '" + txtRemarks.getText().toString() + "'";
            SQL += " where healthId='" + g.getGeneratedId() + "' and providerId='" + g.getProvCode() + "' and strftime('%d/%m/%Y', date(visitDate))= '" + dtpVDate.getText() + "'";
            C.Save(SQL);


        } catch (Exception e) {
            Connection.MessageBox(Under5child.this, e.getMessage());
            return;
        }
    }

    private void DataRetrive() {
        try {
            chkSy1.setChecked(false);
            chkSy2.setChecked(false);
            chkSy3.setChecked(false);
            chkSy4.setChecked(false);
            chkSy5.setChecked(false);
            chkSy6.setChecked(false);
            chkSy7.setChecked(false);
            chkSy8.setChecked(false);

            //chkSy4.setChecked(false);
            //chkSy5.setChecked(false);


            if (chkGeA1.isChecked() == true | chkGeA2.isChecked() == true | chkGeA3.isChecked() == true | chkGeA4.isChecked() == true | chkGeA5.isChecked() == true | chkGeA6.isChecked() == true | chkGeA7.isChecked() == true | chkGeA8.isChecked() == true | chkGeA9.isChecked() == true) {
                chkSy1.setChecked(true);
                secAGO2MGA1.setVisibility(View.VISIBLE);
                secAGO2MGA.setVisibility(View.VISIBLE);
                //chkSy.setChecked(true);
                secGeA1.setVisibility(View.VISIBLE);
                secGeA2.setVisibility(View.VISIBLE);
                secGeA3.setVisibility(View.VISIBLE);
                secGeA4.setVisibility(View.VISIBLE);
                secGeA5.setVisibility(View.VISIBLE);
                secGeA6.setVisibility(View.VISIBLE);
                secGeA7.setVisibility(View.VISIBLE);
                secGeA8.setVisibility(View.VISIBLE);
                secGeA9.setVisibility(View.VISIBLE);

            }


            if (chkGeA1.isChecked() == true | chkGeA2.isChecked() == true | chkGeA3.isChecked() == true | chkGeA4.isChecked() == true | chkGeA5.isChecked() == true | chkGeA6.isChecked() == true) {
                secAGO2MGA1.setVisibility(View.VISIBLE);
                secAGO2MGA.setVisibility(View.VISIBLE);
                secAGO2MGATetClass1.setVisibility(View.VISIBLE);
                secAGO2MGATetClass2.setVisibility(View.GONE);
                // secGAC1.setVisibility(View.VISIBLE);
                // secGA1.setVisibility(View.VISIBLE);
                tableLayout1.setVisibility(View.VISIBLE);
            } else if (chkGeA7.isChecked() == true | chkGeA8.isChecked() == true) {
                secAGO2MGA1.setVisibility(View.VISIBLE);
                secAGO2MGA.setVisibility(View.VISIBLE);
                secAGO2MGATetClass2.setVisibility(View.VISIBLE);
                secAGO2MGATetClass1.setVisibility(View.GONE);
                // secGAC2.setVisibility(View.VISIBLE);
                // secGA2.setVisibility(View.VISIBLE);
                tableLayout2.setVisibility(View.VISIBLE);
            } else if (chkGeA9.isChecked() == true) {
                secAGO2MGA.setVisibility(View.VISIBLE);
                secAGO2MGA1.setVisibility(View.VISIBLE);
                secAGO2MGATetClass3.setVisibility(View.VISIBLE);
                secAGO2MGATetClass1.setVisibility(View.GONE);
                secAGO2MGATetClass2.setVisibility(View.GONE);
                tableLayout3.setVisibility(View.VISIBLE);
                //secGAC3.setVisibility(View.VISIBLE);
                //secGA3.setVisibility(View.VISIBLE);
            }

            if (chkSy1.isChecked() == false) {
                secAGO2MGA1.setVisibility(View.GONE);
            }

            if (chkAGO2MDar1.isChecked() == true | chkAGO2MDar2.isChecked() == true | chkAGO2MDar3.isChecked() == true | chkAGO2MDar4.isChecked() == true | chkAGO2MDar5.isChecked() == true | chkAGO2MDar6.isChecked() == true) {
                secAGO2MGA2.setVisibility(View.VISIBLE);
                chkSy2.setChecked(true);
                //secGA4.setVisibility(View.VISIBLE);
                // secAGO2MDarC1.setVisibility(View.VISIBLE);
                secAGO2MDarTetClass1.setVisibility(View.VISIBLE);
                secAGO2MDarTetClass2.setVisibility(View.GONE);
                // secAGO2MDarC2.setVisibility(View.GONE);
                // secGA5.setVisibility(View.GONE);

                tableLayout4a.setVisibility(View.VISIBLE);
                tableLayout5.setVisibility(View.GONE);


            } else if (chkAGO2MDar7.isChecked() == true) {
                chkSy2.setChecked(true);
                secAGO2MGA2.setVisibility(View.VISIBLE);
                tableLayout5.setVisibility(View.VISIBLE);
                tableLayout4a.setVisibility(View.GONE);

                secAGO2MDarTetClass2.setVisibility(View.VISIBLE);
                secAGO2MDarTetClass1.setVisibility(View.GONE);
                // secGA4.setVisibility(View.GONE);
                // secGA5.setVisibility(View.VISIBLE);

            }

            if (chkSy2.isChecked() == false) {
                secAGO2MGA2.setVisibility(View.GONE);
            }
            if (chkAGO2MFed1.isChecked() == true | chkAGO2MFed2.isChecked() == true | chkAGO2MFed3.isChecked() == true | chkAGO2MFed4.isChecked() == true | chkAGO2MFed5.isChecked() == true) {
                chkSy3.setChecked(true);
                secAGO2MGA3.setVisibility(View.VISIBLE);
                //  chkAGO2MFed.setChecked(true);

                // secAGO2MFeedSymtom.setVisibility(View.VISIBLE);
                secAGO2MFeedTetClass1.setVisibility(View.VISIBLE);
                secAGO2MFeedTetClass2.setVisibility(View.GONE);

                tableLayout6a.setVisibility(View.VISIBLE);
                //secAGO2MFedC1.setVisibility(View.VISIBLE);
            } else if (chkAGO2MFed6.isChecked() == true) {
                chkSy3.setChecked(true);
                secAGO2MGA3.setVisibility(View.VISIBLE);
                tableLayout7.setVisibility(View.VISIBLE);
                //secAGO2MFedC2.setVisibility(View.VISIBLE);
                // chkAGO2MFed.setChecked(true);

                //   secAGO2MFeedSymtom.setVisibility(View.VISIBLE);
                secAGO2MFeedTetClass1.setVisibility(View.GONE);
                secAGO2MFeedTetClass2.setVisibility(View.VISIBLE);
            }
            if (chkSy3.isChecked() == false) {
                secAGO2MGA3.setVisibility(View.GONE);
            }
            if (chkAG25YSym1.isChecked() == true | chkAG25YSym2.isChecked() == true | chkAG25YSym3.isChecked() == true | chkAG25YSym4.isChecked() == true) {
                chkSy4.setChecked(true);
                secAGO2MGA4.setVisibility(View.VISIBLE);
                //chkSy.setChecked(true);
                //chkAG25YNu.setChecked(true);

                secAG2to5Nu.setVisibility(View.VISIBLE);
                // chkAG25YNu.setVisibility(View.VISIBLE);
                // secAG25YNuSymtom1.setVisibility(View.VISIBLE);


            }
            if (chkAG25YSym1.isChecked() == true | chkAG25YSym2.isChecked() == true) {
                //   chkAG25YNu.setChecked(true);
                secAGO2MGA4.setVisibility(View.VISIBLE);
                secAG2to5Nu.setVisibility(View.VISIBLE);
                tableLayout8a.setVisibility(View.VISIBLE);
                // secGA8.setVisibility(View.VISIBLE);
                secAG25YNuClass1.setVisibility(View.VISIBLE);
                secAG25YNuClass2.setVisibility(View.GONE);
                secAG25YNuClass3.setVisibility(View.GONE);
                tableLayout9.setVisibility(View.GONE);
                // secGA10.setVisibility(View.GONE);
                tableLayout10.setVisibility(View.GONE);
                // secGA9.setVisibility(View.GONE);

            } else if (chkAG25YSym3.isChecked() == true) {
                secAGO2MGA4.setVisibility(View.VISIBLE);
                //chkAG25YNu.setChecked(true);
                tableLayout8a.setVisibility(View.GONE);
                // secGA8.setVisibility(View.GONE);
                tableLayout10.setVisibility(View.GONE);
                // secGA10.setVisibility(View.GONE);
                tableLayout9.setVisibility(View.VISIBLE);
                // secGA9.setVisibility(View.VISIBLE);
                secAG2to5Nu.setVisibility(View.VISIBLE);
                secAG25YNuClass1.setVisibility(View.GONE);
                secAG25YNuClass2.setVisibility(View.VISIBLE);
                secAG25YNuClass3.setVisibility(View.GONE);

            } else if (chkAG25YSym4.isChecked() == true) {
                secAGO2MGA4.setVisibility(View.VISIBLE);
                // chkAG25YNu.setChecked(true);
                tableLayout10.setVisibility(View.VISIBLE);
                // secGA10.setVisibility(View.VISIBLE);
                secAG2to5Nu.setVisibility(View.VISIBLE);
                secAG25YNuClass1.setVisibility(View.GONE);
                secAG25YNuClass2.setVisibility(View.GONE);
                secAG25YNuClass3.setVisibility(View.VISIBLE);
                tableLayout8a.setVisibility(View.GONE);
                // secGA8.setVisibility(View.GONE);
                tableLayout9.setVisibility(View.GONE);
                //  secGA9.setVisibility(View.GONE);

            }

            if (chkSy4.isChecked() == false) {
                secAGO2MGA4.setVisibility(View.GONE);
            }

            if (chkAG25YDar1.isChecked() == true | chkAG25YDar2.isChecked() == true | chkAG25YDar3.isChecked() == true | chkAG25YDar4.isChecked() == true | chkAG25YDar5.isChecked() == true | chkAG25YDar6.isChecked() == true | chkAG25YDar7.isChecked() == true | chkAG25YDar8.isChecked() == true | chkAG25YDar9.isChecked() == true) {
                secAGO2MGA5.setVisibility(View.VISIBLE);
                chkSy5.setChecked(true);
                //chkSy.setChecked(true);
                // chkAG25YDar.setChecked(true);

                secAG2to5Nu.setVisibility(View.VISIBLE);

                // secAG25YDarSymtom.setVisibility(View.VISIBLE);


            }
            if (chkAG25YDar1.isChecked() == true | chkAG25YDar2.isChecked() == true | chkAG25YDar3.isChecked() == true | chkAG25YDar4.isChecked() == true) {
                // chkAG25YDar.setChecked(true);
                secAGO2MGA5.setVisibility(View.VISIBLE);
                secAG2to5Nu.setVisibility(View.VISIBLE);
                secAG25YDarTetClass1.setVisibility(View.VISIBLE);
                secAG25YDarTetClass2.setVisibility(View.GONE);
                secAG25YDarTetClass3.setVisibility(View.GONE);

                tableLayout11a.setVisibility(View.VISIBLE);
                //secAG25YDarC1.setVisibility(View.VISIBLE);

            } else if (chkAG25YDar5.isChecked() == true | chkAG25YDar6.isChecked() == true | chkAG25YDar7.isChecked() == true | chkAG25YDar8.isChecked() == true) {
                //chkAG25YDar.setChecked(true);
                secAGO2MGA5.setVisibility(View.VISIBLE);
                secAG2to5Nu.setVisibility(View.VISIBLE);
                secAG25YDarTetClass1.setVisibility(View.GONE);
                secAG25YDarTetClass2.setVisibility(View.VISIBLE);
                secAG25YDarTetClass3.setVisibility(View.GONE);

                tableLayout12.setVisibility(View.VISIBLE);
                // secAG25YDarC2.setVisibility(View.VISIBLE);

            } else if (chkAG25YDar9.isChecked() == true) {
                //  chkAG25YDar.setChecked(true);
                secAGO2MGA5.setVisibility(View.VISIBLE);
                secAG2to5Nu.setVisibility(View.VISIBLE);
                secAG25YDarTetClass1.setVisibility(View.GONE);
                secAG25YDarTetClass2.setVisibility(View.GONE);
                secAG25YDarTetClass3.setVisibility(View.VISIBLE);
                tableLayout13.setVisibility(View.VISIBLE);
                //secAG25YDarC3.setVisibility(View.VISIBLE);
            } else if (chkAG25YAm1.isChecked() == true) {
                secAGO2MGA5.setVisibility(View.VISIBLE);
                chkSy5.setChecked(true);
                //  chkSy.setChecked(true);
                // chkAG25YAm.setChecked(true);
                secAG25YAm1.setVisibility(View.VISIBLE);
                secAG2to5Nu.setVisibility(View.VISIBLE);

                secAG25YAmSymtom.setVisibility(View.VISIBLE);
                tableLayout14.setVisibility(View.VISIBLE);
                //secGA14.setVisibility(View.VISIBLE);
                // secAG25YAmC1.setVisibility(View.VISIBLE);

            }

            if (chkSy5.isChecked() == false) {
                secAGO2MGA5.setVisibility(View.GONE);
            }

            if (chkAG25YMal1.isChecked() == true | chkAG25YMal2.isChecked() == true) {
                secAGO2MGA6.setVisibility(View.VISIBLE);
                chkSy6.setChecked(true);
                secAG25YMal1.setVisibility(View.VISIBLE);
                secAG25YMal2.setVisibility(View.VISIBLE);
                secAG2to5Nu.setVisibility(View.VISIBLE);


            }
            if (chkAG25YFev1.isChecked() == true | chkAG25YFev2.isChecked() == true) {
                secAGO2MGA6.setVisibility(View.VISIBLE);
                chkSy6.setChecked(true);
                secAG25YFev1.setVisibility(View.VISIBLE);
                secAG25YFev2.setVisibility(View.VISIBLE);
                secAG2to5Nu.setVisibility(View.VISIBLE);
                secAG25YFevSymtom.setVisibility(View.VISIBLE);
                tableLayout15a.setVisibility(View.VISIBLE);
                // secGA15.setVisibility(View.VISIBLE);

            } else if (chkAG25YMal1.isChecked() == true) {
                secAGO2MGA6.setVisibility(View.VISIBLE);
                chkSy6.setChecked(true);
                secAG25YMal1.setVisibility(View.VISIBLE);
                secAG25YMal2.setVisibility(View.VISIBLE);
                secAG2to5Nu.setVisibility(View.VISIBLE);
                secAG25YMalSymtom1.setVisibility(View.VISIBLE);
                secAG25YMalSymtom2.setVisibility(View.GONE);
                tableLayout16.setVisibility(View.VISIBLE);
                //secGA16.setVisibility(View.VISIBLE);

            } else if (chkAG25YMal2.isChecked() == true) {
                secAGO2MGA6.setVisibility(View.VISIBLE);
                chkSy6.setChecked(true);
                secAG25YMal1.setVisibility(View.VISIBLE);
                secAG25YMal2.setVisibility(View.VISIBLE);
                secAG2to5Nu.setVisibility(View.VISIBLE);
                secAG25YMalSymtom2.setVisibility(View.VISIBLE);
                secAG25YMalSymtom1.setVisibility(View.GONE);

                tableLayout17.setVisibility(View.VISIBLE);
                //secGA17.setVisibility(View.VISIBLE);


            }
            if (chkSy6.isChecked() == false) {
                secAGO2MGA6.setVisibility(View.GONE);
            }

            if (chkAG25YNut1.isChecked() == true | chkAG25YNut2.isChecked() == true | chkAG25YNut3.isChecked() == true | chkAG25YNut4.isChecked() == true) {
                secAGO2MGA7.setVisibility(View.VISIBLE);
                chkSy7.setChecked(true);
                //  chkSy.setChecked(true);
                // chkAG25YNut.setChecked(true);
                //      secAG25YNutSymtom.setVisibility(View.VISIBLE);

                secAG2to5Nu.setVisibility(View.VISIBLE);


            }

            if (chkAG25YNut1.isChecked() == true | chkAG25YNut2.isChecked() == true) {
                //  chkSy.setChecked(true);
                // chkAG25YNut.setChecked(true);
                secAGO2MGA7.setVisibility(View.VISIBLE);
                secAG2to5Nu.setVisibility(View.VISIBLE);
                secAG25YNutSymtom1.setVisibility(View.VISIBLE);
                secAG25YNutSymtom2.setVisibility(View.GONE);
                secAG25YNutSymtom3.setVisibility(View.GONE);

                tableLayout19a.setVisibility(View.VISIBLE);

            } else if (chkAG25YNut3.isChecked() == true) {
                //   chkSy.setChecked(true);
                //chkAG25YNut.setChecked(true);
                secAGO2MGA7.setVisibility(View.VISIBLE);
                secAG2to5Nu.setVisibility(View.VISIBLE);
                secAG25YNutSymtom2.setVisibility(View.VISIBLE);
                secAG25YNutSymtom1.setVisibility(View.GONE);
                secAG25YNutSymtom3.setVisibility(View.GONE);

                tableLayout20.setVisibility(View.VISIBLE);

            } else if (chkAG25YNut4.isChecked() == true) {

                secAGO2MGA7.setVisibility(View.VISIBLE);
                secAG2to5Nu.setVisibility(View.VISIBLE);
                secAG25YNutSymtom1.setVisibility(View.GONE);
                secAG25YNutSymtom2.setVisibility(View.GONE);
                secAG25YNutSymtom3.setVisibility(View.VISIBLE);
                tableLayout21.setVisibility(View.VISIBLE);


            }

            if (chkSy7.isChecked() == false) {
                secAGO2MGA7.setVisibility(View.GONE);
            }

            if (chkAG25YLow1.isChecked() == true | chkAG25YLow2.isChecked() == true | chkAG25YLow3.isChecked() == true) {
                secAGO2MGA8.setVisibility(View.VISIBLE);
                chkSy8.setChecked(true);
                secAG2to5Nu.setVisibility(View.VISIBLE);


            }

            if (chkAG25YLow1.isChecked() == true) {
                secAG25YLowSymtom2.setVisibility(View.GONE);
                secAG25YLowSymtom3.setVisibility(View.GONE);
                secAGO2MGA8.setVisibility(View.VISIBLE);
                secAG2to5Nu.setVisibility(View.VISIBLE);
                secAG25YLowSymtom1.setVisibility(View.VISIBLE);
                tableLayout22.setVisibility(View.VISIBLE);


            } else if (chkAG25YLow2.isChecked() == true) {
                secAG25YLowSymtom1.setVisibility(View.GONE);
                secAG25YLowSymtom3.setVisibility(View.GONE);
                secAGO2MGA8.setVisibility(View.VISIBLE);
                secAG2to5Nu.setVisibility(View.VISIBLE);
                secAG25YLowSymtom2.setVisibility(View.VISIBLE);
                tableLayout23.setVisibility(View.VISIBLE);


            } else if (chkAG25YLow3.isChecked() == true) {

                secAG25YNutSymtom1.setVisibility(View.GONE);
                secAG25YNutSymtom2.setVisibility(View.GONE);
                secAGO2MGA8.setVisibility(View.VISIBLE);
                secAG2to5Nu.setVisibility(View.VISIBLE);
                secAG25YNutSymtom3.setVisibility(View.VISIBLE);
                tableLayout24.setVisibility(View.VISIBLE);


            }

            if (chkSy8.isChecked() == false) {
                secAGO2MGA8.setVisibility(View.GONE);
            }

            if (chkAG25YOth1.isChecked() == true | chkAG25YOth2.isChecked() == true | chkAG25YOth3.isChecked() == true | chkAG25YOth4.isChecked() == true | chkAG25YOth5.isChecked() == true | chkAG25YOth6.isChecked() == true | chkAG25YOth7.isChecked() == true) {

                secOther.setVisibility(View.VISIBLE);


            }

            if (chkAG25YOth1.isChecked() == true) {

                secOth1.setVisibility(View.VISIBLE);

            }

            if (chkAG25YOth2.isChecked() == true) {

                secOth2.setVisibility(View.VISIBLE);

            }

            if (chkAG25YOth3.isChecked() == true) {

                secOth3.setVisibility(View.VISIBLE);

            }

            if (chkAG25YOth4.isChecked() == true) {

                secOth4.setVisibility(View.VISIBLE);

            }

            if (chkAG25YOth5.isChecked() == true) {

                secOth5.setVisibility(View.VISIBLE);

            }

            if (chkAG25YOth6.isChecked() == true) {

                secOth6.setVisibility(View.VISIBLE);

            }

            if (chkAG25YOth7.isChecked() == true) {

                secOth7.setVisibility(View.VISIBLE);

            }
        } catch (Exception e) {
            Connection.MessageBox(Under5child.this, e.getMessage());
            return;
        }
    }

    private void DataSaveTreatment() {
        try {


            if (chkGeATet1.isChecked() == true) {

                TreatmentValue.add(VGeACodeTet1.getText().toString());
            } else if (chkGeATet1.isChecked() == false) {
                DeleteTreatment(VGeACodeTet1.getText().toString());
            }

            if (chkGeATet2.isChecked() == true) {
                TreatmentValue.add(VGeACodeTet2.getText().toString());
            } else if (chkGeATet2.isChecked() == false) {
                DeleteTreatment(VGeACodeTet2.getText().toString());
            }

            if (chkGeATet3.isChecked() == true) {
                TreatmentValue.add(VGeACodeTet3.getText().toString());
            } else if (chkGeATet3.isChecked() == false) {
                DeleteTreatment(VGeACodeTet3.getText().toString());
            }


            if (chkGeATet4.isChecked() == true) {
                TreatmentValue.add(VGeACodeTet4.getText().toString());
            } else if (chkGeATet4.isChecked() == false) {
                DeleteTreatment(VGeACodeTet4.getText().toString());
            }


            if (chkGeATet5.isChecked() == true) {
                TreatmentValue.add(VGeACodeTet5.getText().toString());
            } else if (chkGeATet5.isChecked() == false) {
                DeleteTreatment(VGeACodeTet5.getText().toString());
            }


            if (chkGeATet6.isChecked() == true) {
                TreatmentValue.add(VGeACodeTet6.getText().toString());
            } else if (chkGeATet6.isChecked() == false) {
                DeleteTreatment(VGeACodeTet6.getText().toString());
            }

            if (chkGeATet7.isChecked() == true) {
                TreatmentValue.add(VGeACodeTet7.getText().toString());
            } else if (chkGeATet7.isChecked() == false) {
                DeleteTreatment(VGeACodeTet7.getText().toString());
            }

            if (chkGeATet7a.isChecked() == true) {
                TreatmentValue.add(VGeACodeTet7a.getText().toString());
            } else if (chkGeATet7a.isChecked() == false) {
                DeleteTreatment(VGeACodeTet7a.getText().toString());
            }

            if (chkGeATet8.isChecked() == true) {
                TreatmentValue.add(VGeACodeTet8.getText().toString());
            } else if (chkGeATet8.isChecked() == false) {
                DeleteTreatment(VGeACodeTet8.getText().toString());
            }

            if (chkGeATet9.isChecked() == true) {
                TreatmentValue.add(VGeACodeTet9.getText().toString());
            } else if (chkGeATet9.isChecked() == false) {
                DeleteTreatment(VGeACodeTet9.getText().toString());
            }

            if (chkGeATet9a.isChecked() == true) {
                TreatmentValue.add(VGeACodeTet9a.getText().toString());
            } else if (chkGeATet9a.isChecked() == false) {
                DeleteTreatment(VGeACodeTet9a.getText().toString());
            }


            if (chkAGO2MDarTet1.isChecked() == true) {
                TreatmentValue.add(VAGO2MDarCodeTet1.getText().toString());
            } else if (chkAGO2MDarTet1.isChecked() == false) {
                DeleteTreatment(VAGO2MDarCodeTet1.getText().toString());
            }


            if (chkAGO2MDarTet2.isChecked() == true) {
                TreatmentValue.add(VAGO2MDarCodeTet2.getText().toString());
            } else if (chkAGO2MDarTet2.isChecked() == false) {
                DeleteTreatment(VAGO2MDarCodeTet2.getText().toString());
            }


            if (chkAGO2MDarTet3.isChecked() == true) {
                TreatmentValue.add(VAGO2MDarCodeTet3.getText().toString());
            } else if (chkAGO2MDarTet3.isChecked() == false) {
                DeleteTreatment(VAGO2MDarCodeTet3.getText().toString());
            }


            if (chkAGO2MDarTet4.isChecked() == true) {
                TreatmentValue.add(VAGO2MDarCodeTet4.getText().toString());
            } else if (chkAGO2MDarTet4.isChecked() == false) {
                DeleteTreatment(VAGO2MDarCodeTet4.getText().toString());
            }


            if (chkAGO2MDarTet5.isChecked() == true) {
                TreatmentValue.add(VAGO2MDarCodeTet5.getText().toString());
            } else if (chkAGO2MDarTet5.isChecked() == false) {
                DeleteTreatment(VAGO2MDarCodeTet5.getText().toString());
            }


            if (chkAGO2MDarTet6.isChecked() == true) {
                TreatmentValue.add(VAGO2MDarCodeTet6.getText().toString());
            } else if (chkAGO2MDarTet6.isChecked() == false) {
                DeleteTreatment(VAGO2MDarCodeTet6.getText().toString());
            }


            if (chkAGO2MDarTet6a.isChecked() == true) {
                TreatmentValue.add(VAGO2MDarCodeTet6a.getText().toString());
            } else if (chkAGO2MDarTet6a.isChecked() == false) {
                DeleteTreatment(VAGO2MDarCodeTet6a.getText().toString());
            }


            if (chkAGO2MFedTet1.isChecked() == true) {
                TreatmentValue.add(VAGO2MFedCodeTet1.getText().toString());
            } else if (chkAGO2MFedTet1.isChecked() == false) {
                DeleteTreatment(VAGO2MFedCodeTet1.getText().toString());
            }


            if (chkAGO2MFedTet2.isChecked() == true) {
                TreatmentValue.add(VAGO2MFedCodeTet2.getText().toString());
            } else if (chkAGO2MFedTet2.isChecked() == false) {
                DeleteTreatment(VAGO2MFedCodeTet2.getText().toString());
            }


            if (chkAGO2MFedTet3.isChecked() == true) {
                TreatmentValue.add(VAGO2MFedCodeTet3.getText().toString());
            } else if (chkAGO2MFedTet3.isChecked() == false) {
                DeleteTreatment(VAGO2MFedCodeTet3.getText().toString());
            }


            if (chkAGO2MFedTet4.isChecked() == true) {
                TreatmentValue.add(VAGO2MFedCodeTet4.getText().toString());
            } else if (chkAGO2MFedTet4.isChecked() == false) {
                DeleteTreatment(VAGO2MFedCodeTet4.getText().toString());
            }

            if (chkAGO2MFedTet5.isChecked() == true) {
                TreatmentValue.add(VAGO2MFedCodeTet5.getText().toString());
            } else if (chkAGO2MFedTet5.isChecked() == false) {
                DeleteTreatment(VAGO2MFedCodeTet5.getText().toString());
            }

            if (chkAGO2MFedTet6.isChecked() == true) {
                TreatmentValue.add(VAGO2MFedCodeTet6.getText().toString());
            } else if (chkAGO2MFedTet6.isChecked() == false) {
                DeleteTreatment(VAGO2MFedCodeTet6.getText().toString());
            }


            if (chkAGO2MFedTet7.isChecked() == true) {
                TreatmentValue.add(VAGO2MFedCodeTet7.getText().toString());
            } else if (chkAGO2MFedTet7.isChecked() == false) {
                DeleteTreatment(VAGO2MFedCodeTet7.getText().toString());
            }

            ///2 to 5 year


            if (chkAG25YTet1.isChecked() == true) {
                TreatmentValue.add(VAG25YTetCode1.getText().toString());
            } else if (chkAG25YTet1.isChecked() == false) {
                DeleteTreatment(VAG25YTetCode1.getText().toString());
            }
            if (chkAG25YTet2.isChecked() == true) {
                TreatmentValue.add(VAG25YTetCode2.getText().toString());
            } else if (chkAG25YTet2.isChecked() == false) {
                DeleteTreatment(VAG25YTetCode2.getText().toString());
            }

            if (chkAG25YTet3.isChecked() == true) {
                TreatmentValue.add(VAG25YTetCode3.getText().toString());
            } else if (chkAG25YTet3.isChecked() == false) {
                DeleteTreatment(VAG25YTetCode3.getText().toString());
            }

            if (chkAG25YTet4.isChecked() == true) {
                TreatmentValue.add(VAG25YTetCode4.getText().toString());
            } else if (chkAG25YTet4.isChecked() == false) {
                DeleteTreatment(VAG25YTetCode4.getText().toString());
            }

            if (chkAG25YTet5.isChecked() == true) {
                TreatmentValue.add(VAG25YTetCode5.getText().toString());
            } else if (chkAG25YTet5.isChecked() == false) {
                DeleteTreatment(VAG25YTetCode5.getText().toString());
            }
            if (chkAG25YTet5a.isChecked() == true) {
                TreatmentValue.add(VAG25YTetCode5a.getText().toString());
            } else if (chkAG25YTet5a.isChecked() == false) {
                DeleteTreatment(VAG25YTetCode5a.getText().toString());
            }
            if (chkAG25YTet6.isChecked() == true) {
                TreatmentValue.add(VAG25YTetCode6.getText().toString());
            } else if (chkAG25YTet6.isChecked() == false) {
                DeleteTreatment(VAG25YTetCode6.getText().toString());
            }

            if (chkAG25YTet7.isChecked() == true) {
                TreatmentValue.add(VAG25YTetCode7.getText().toString());
            } else if (chkAG25YTet7.isChecked() == false) {
                DeleteTreatment(VAG25YTetCode7.getText().toString());
            }

            if (chkAG25YDarTet1.isChecked() == true) {
                TreatmentValue.add(VAG25YDarCodeTet1.getText().toString());
            } else if (chkAG25YDarTet1.isChecked() == false) {
                DeleteTreatment(VAG25YDarCodeTet1.getText().toString());
            }


            if (chkAG25YDarTet2.isChecked() == true) {
                TreatmentValue.add(VAG25YDarCodeTet2.getText().toString());
            } else if (chkAG25YDarTet2.isChecked() == false) {
                DeleteTreatment(VAG25YDarCodeTet2.getText().toString());
            }


            if (chkAG25YDarTet3.isChecked() == true) {
                TreatmentValue.add(VAG25YDarCodeTet3.getText().toString());
            } else if (chkAG25YDarTet3.isChecked() == false) {
                DeleteTreatment(VAG25YDarCodeTet3.getText().toString());
            }

            if (chkAG25YDarTet3a.isChecked() == true) {
                TreatmentValue.add(VAG25YDarCodeTet3a.getText().toString());
            } else if (chkAG25YDarTet3a.isChecked() == false) {
                DeleteTreatment(VAG25YDarCodeTet3a.getText().toString());
            }

            if (chkAG25YDarTet4.isChecked() == true) {
                TreatmentValue.add(VAG25YDarCodeTet4.getText().toString());
            } else if (chkAG25YDarTet4.isChecked() == false) {
                DeleteTreatment(VAG25YDarCodeTet4.getText().toString());
            }


            if (chkAG25YDarTet5.isChecked() == true) {
                TreatmentValue.add(VAG25YDarCodeTet5.getText().toString());
            } else if (chkAG25YDarTet5.isChecked() == false) {
                DeleteTreatment(VAG25YDarCodeTet5.getText().toString());
            }


            if (chkAG25YDarTet6.isChecked() == true) {
                TreatmentValue.add(VAG25YDarCodeTet6.getText().toString());
            } else if (chkAG25YDarTet6.isChecked() == false) {
                DeleteTreatment(VAG25YDarCodeTet6.getText().toString());
            }

            if (chkAG25YDarTet6a.isChecked() == true) {
                TreatmentValue.add(VAG25YDarCodeTet6a.getText().toString());
            } else if (chkAG25YDarTet6a.isChecked() == false) {
                DeleteTreatment(VAG25YDarCodeTet6a.getText().toString());
            }


            if (chkAG25YamTet1.isChecked() == true) {
                TreatmentValue.add(VAG25YAmCodeTet1.getText().toString());
            } else if (chkAG25YamTet1.isChecked() == false) {
                DeleteTreatment(VAG25YAmCodeTet1.getText().toString());
            }

            if (chkAG25YamTet2.isChecked() == true) {
                TreatmentValue.add(VAG25YAmCodeTet2.getText().toString());
            } else if (chkAG25YamTet2.isChecked() == false) {
                DeleteTreatment(VAG25YAmCodeTet2.getText().toString());
            }

            if (chkAG25YamTet2a.isChecked() == true) {
                TreatmentValue.add(VAG25YAmCodeTet2a.getText().toString());
            } else if (chkAG25YamTet2a.isChecked() == false) {
                DeleteTreatment(VAG25YAmCodeTet2a.getText().toString());
            }

            if (chkAG25YFevTet1.isChecked() == true) {
                TreatmentValue.add(VAG25YFevCodeTet1.getText().toString());
            } else if (chkAG25YFevTet1.isChecked() == false) {
                DeleteTreatment(VAG25YFevCodeTet1.getText().toString());
            }


            if (chkAG25YFevTet2.isChecked() == true) {
                TreatmentValue.add(VAG25YFevCodeTet2.getText().toString());
            } else if (chkAG25YFevTet2.isChecked() == false) {
                DeleteTreatment(VAG25YFevCodeTet2.getText().toString());
            }

            if (chkAG25YFevTet3.isChecked() == true) {
                TreatmentValue.add(VAG25YFevCodeTet3.getText().toString());
            } else if (chkAG25YFevTet3.isChecked() == false) {
                DeleteTreatment(VAG25YFevCodeTet3.getText().toString());
            }

            if (chkAG25YFevTet4.isChecked() == true) {
                TreatmentValue.add(VAG25YFevCodeTet4.getText().toString());
            } else if (chkAG25YFevTet4.isChecked() == false) {
                DeleteTreatment(VAG25YFevCodeTet4.getText().toString());
            }

            if (chkAG25YFevTet4a.isChecked() == true) {
                TreatmentValue.add(VAG25YFevCodeTet4a.getText().toString());
            } else if (chkAG25YFevTet4a.isChecked() == false) {
                DeleteTreatment(VAG25YFevCodeTet4a.getText().toString());
            }


            if (chkAG25YMalTet1.isChecked() == true) {
                TreatmentValue.add(VAG25YMalCodeTet1.getText().toString());
            } else if (chkAG25YMalTet1.isChecked() == false) {
                DeleteTreatment(VAG25YMalCodeTet1.getText().toString());
            }

            if (chkAG25YMalTet2.isChecked() == true) {
                TreatmentValue.add(VAG25YMalCodeTet2.getText().toString());
            } else if (chkAG25YMalTet2.isChecked() == false) {
                DeleteTreatment(VAG25YMalCodeTet2.getText().toString());
            }
            if (chkAG25YMalTet2a.isChecked() == true) {
                TreatmentValue.add(VAG25YMalCodeTet2a.getText().toString());
            } else if (chkAG25YMalTet2a.isChecked() == false) {
                DeleteTreatment(VAG25YMalCodeTet2a.getText().toString());
            }
            if (chkAG25YMalTet3.isChecked() == true) {
                TreatmentValue.add(VAG25YMalCodeTet3.getText().toString());
            } else if (chkAG25YMalTet3.isChecked() == false) {
                DeleteTreatment(VAG25YMalCodeTet3.getText().toString());
            }

            if (chkAG25YMalTet4.isChecked() == true) {
                TreatmentValue.add(VAG25YMalCodeTet4.getText().toString());
            } else if (chkAG25YMalTet4.isChecked() == false) {
                DeleteTreatment(VAG25YMalCodeTet4.getText().toString());
            }

            if (chkAG25YMalTet5.isChecked() == true) {
                TreatmentValue.add(VAG25YMalCodeTet5.getText().toString());
            } else if (chkAG25YMalTet5.isChecked() == false) {
                DeleteTreatment(VAG25YMalCodeTet5.getText().toString());
            }

            if (chkAG25YMalTet6.isChecked() == true) {
                TreatmentValue.add(VAG25YMalCodeTet6.getText().toString());
            } else if (chkAG25YMalTet6.isChecked() == false) {
                DeleteTreatment(VAG25YMalCodeTet6.getText().toString());
            }

            if (chkAG25YMalTet7.isChecked() == true) {
                TreatmentValue.add(VAG25YMalCodeTet7.getText().toString());
            } else if (chkAG25YMalTet7.isChecked() == false) {
                DeleteTreatment(VAG25YMalCodeTet7.getText().toString());
            }

            if (chkAG25YMalTet8.isChecked() == true) {
                TreatmentValue.add(VAG25YMalCodeTet8.getText().toString());
            } else if (chkAG25YMalTet8.isChecked() == false) {
                DeleteTreatment(VAG25YMalCodeTet8.getText().toString());
            }

            if (chkAG25YMalTet8a.isChecked() == true) {
                TreatmentValue.add(VAG25YMalCodeTet8a.getText().toString());
            } else if (chkAG25YMalTet8a.isChecked() == false) {
                DeleteTreatment(VAG25YMalCodeTet8a.getText().toString());
            }


            if (chkAG25YNutTet1.isChecked() == true) {
                TreatmentValue.add(VAG25YNutCodeTet1.getText().toString());
            } else if (chkAG25YNutTet1.isChecked() == false) {
                DeleteTreatment(VAG25YNutCodeTet1.getText().toString());
            }

            if (chkAG25YNutTet2.isChecked() == true) {
                TreatmentValue.add(VAG25YNutCodeTet2.getText().toString());
            } else if (chkAG25YNutTet2.isChecked() == false) {
                DeleteTreatment(VAG25YNutCodeTet2.getText().toString());
            }

            if (chkAG25YNutTet3.isChecked() == true) {
                TreatmentValue.add(VAG25YNutCodeTet3.getText().toString());
            } else if (chkAG25YNutTet3.isChecked() == false) {
                DeleteTreatment(VAG25YNutCodeTet3.getText().toString());
            }


            if (chkAG25YNutTet4.isChecked() == true) {
                TreatmentValue.add(VAG25YNutCodeTet4.getText().toString());
            } else if (chkAG25YNutTet4.isChecked() == false) {
                DeleteTreatment(VAG25YNutCodeTet4.getText().toString());
            }


            if (chkAG25YNutTet5.isChecked() == true) {
                TreatmentValue.add(VAG25YNutCodeTet5.getText().toString());
            } else if (chkAG25YNutTet5.isChecked() == false) {
                DeleteTreatment(VAG25YNutCodeTet5.getText().toString());
            }

            if (chkAG25YNutTet5a.isChecked() == true) {
                TreatmentValue.add(VAG25YNutCodeTet5a.getText().toString());
            } else if (chkAG25YNutTet5a.isChecked() == false) {
                DeleteTreatment(VAG25YNutCodeTet5a.getText().toString());
            }

            if (chkAG25YNutTet6.isChecked() == true) {
                TreatmentValue.add(VAG25YNutCodeTet6.getText().toString());
            } else if (chkAG25YNutTet6.isChecked() == false) {
                DeleteTreatment(VAG25YNutCodeTet6.getText().toString());
            }

            if (chkAG25YNutTet7.isChecked() == true) {
                TreatmentValue.add(VAG25YNutCodeTet7.getText().toString());
            } else if (chkAG25YNutTet7.isChecked() == false) {
                DeleteTreatment(VAG25YNutCodeTet7.getText().toString());
            }

            if (chkAG25YNutTet7a.isChecked() == true) {
                TreatmentValue.add(VAG25YNutCodeTet7a.getText().toString());
            } else if (chkAG25YNutTet7a.isChecked() == false) {
                DeleteTreatment(VAG25YNutCodeTet7a.getText().toString());
            }

            if (chkAG25YLowTet1.isChecked() == true) {
                TreatmentValue.add(VAG25YLowCodeTet1.getText().toString());
            } else if (chkAG25YLowTet1.isChecked() == false) {
                DeleteTreatment(VAG25YLowCodeTet1.getText().toString());
            }

            if (chkAG25YLowTet2.isChecked() == true) {
                TreatmentValue.add(VAG25YLowCodeTet2.getText().toString());
            } else if (chkAG25YLowTet2.isChecked() == false) {
                DeleteTreatment(VAG25YLowCodeTet2.getText().toString());
            }


            if (chkAG25YLowTet3.isChecked() == true) {
                TreatmentValue.add(VAG25YLowCodeTet3.getText().toString());
            } else if (chkAG25YLowTet3.isChecked() == false) {
                DeleteTreatment(VAG25YLowCodeTet3.getText().toString());
            }


            if (chkAG25YLowTet4.isChecked() == true) {
                TreatmentValue.add(VAG25YLowCodeTet4.getText().toString());
            } else if (chkAG25YLowTet4.isChecked() == false) {
                DeleteTreatment(VAG25YLowCodeTet4.getText().toString());
            }

            if (chkAG25YLowTet4a.isChecked() == true) {
                TreatmentValue.add(VAG25YLowCodeTet4a.getText().toString());
            } else if (chkAG25YLowTet4a.isChecked() == false) {
                DeleteTreatment(VAG25YLowCodeTet4a.getText().toString());
            }


            if (chkAG25YLowTet5.isChecked() == true) {
                TreatmentValue.add(VAG25YLowCodeTet5.getText().toString());
            } else if (chkAG25YLowTet5.isChecked() == false) {
                DeleteTreatment(VAG25YLowCodeTet5.getText().toString());
            }

            if (chkAG25YLowTet5a.isChecked() == true) {
                TreatmentValue.add(VAG25YLowCodeTet5a.getText().toString());
            } else if (chkAG25YLowTet5a.isChecked() == false) {
                DeleteTreatment(VAG25YLowCodeTet5a.getText().toString());
            }

            int i = 0;
            for (i = 0; i < TreatmentValue.size(); i++) {
                for (String temp : TreatmentValue) {
                    // System.out.println(temp);
                    SaveTreatment(temp);

                }

                //SaveSymtom(SymtomValue.toString());
            }


        } catch (Exception e) {
            Connection.MessageBox(Under5child.this, e.getMessage());
            return;
        }
    }


    private void DataSaveSymtom() {
        try {


            if (chkGeA1.isChecked() == true) {
                SymtomValue.add(VGeACode1.getText().toString());
            } else if (chkGeA1.isChecked() == false) {
                DeleteSymtom(VGeACode1.getText().toString());
            }

            if (chkGeA2.isChecked() == true) {
                SymtomValue.add(VGeACode2.getText().toString());
            } else if (chkGeA2.isChecked() == false) {
                DeleteSymtom(VGeACode2.getText().toString());
            }

            if (chkGeA3.isChecked() == true) {
                SymtomValue.add(VGeACode3.getText().toString());
            } else if (chkGeA3.isChecked() == false) {
                DeleteSymtom(VGeACode3.getText().toString());
            }


            if (chkGeA4.isChecked() == true) {
                SymtomValue.add(VGeACode4.getText().toString());
            } else if (chkGeA4.isChecked() == false) {
                DeleteSymtom(VGeACode4.getText().toString());
            }


            if (chkGeA5.isChecked() == true) {
                SymtomValue.add(VGeACode5.getText().toString());
            } else if (chkGeA5.isChecked() == false) {
                DeleteSymtom(VGeACode5.getText().toString());
            }
            if (chkGeA6.isChecked() == true) {
                SymtomValue.add(VGeACode6.getText().toString());
            } else if (chkGeA6.isChecked() == false) {
                DeleteSymtom(VGeACode6.getText().toString());
            }

            if (chkGeA7.isChecked() == true) {
                SymtomValue.add(VGeACode7.getText().toString());
            } else if (chkGeA7.isChecked() == false) {
                DeleteSymtom(VGeACode7.getText().toString());
            }

            if (chkGeA8.isChecked() == true) {
                SymtomValue.add(VGeACode8.getText().toString());
            } else if (chkGeA8.isChecked() == false) {
                DeleteSymtom(VGeACode8.getText().toString());
            }

            if (chkGeA9.isChecked() == true) {
                SymtomValue.add(VGeACode9.getText().toString());
            } else if (chkGeA9.isChecked() == false) {
                DeleteSymtom(VGeACode9.getText().toString());
            }
            if (chkAGO2MDar1.isChecked() == true) {
                SymtomValue.add(VAGO2MDar1.getText().toString());
            } else if (chkAGO2MDar1.isChecked() == false) {
                DeleteSymtom(VAGO2MDar1.getText().toString());
            }

            if (chkAGO2MDar2.isChecked() == true) {
                SymtomValue.add(VAGO2MDar2.getText().toString());
            } else if (chkAGO2MDar2.isChecked() == false) {
                DeleteSymtom(VAGO2MDar2.getText().toString());
            }

            if (chkAGO2MDar3.isChecked() == true) {
                SymtomValue.add(VAGO2MDar3.getText().toString());
            } else if (chkAGO2MDar3.isChecked() == false) {
                DeleteSymtom(VAGO2MDar3.getText().toString());
            }


            if (chkAGO2MDar4.isChecked() == true) {
                SymtomValue.add(VAGO2MDar4.getText().toString());
            } else if (chkAGO2MDar4.isChecked() == false) {
                DeleteSymtom(VAGO2MDar4.getText().toString());
            }


            if (chkAGO2MDar5.isChecked() == true) {
                SymtomValue.add(VAGO2MDar5.getText().toString());
            } else if (chkAGO2MDar5.isChecked() == false) {
                DeleteSymtom(VAGO2MDar5.getText().toString());
            }


            if (chkAGO2MDar6.isChecked() == true) {
                SymtomValue.add(VAGO2MDar6.getText().toString());
            } else if (chkAGO2MDar6.isChecked() == false) {
                DeleteSymtom(VAGO2MDar6.getText().toString());
            }


            if (chkAGO2MDar7.isChecked() == true) {
                SymtomValue.add(VAGO2MDar7.getText().toString());
            } else if (chkAGO2MDar7.isChecked() == false) {
                DeleteSymtom(VAGO2MDar7.getText().toString());
            }

            if (chkAGO2MFed1.isChecked() == true) {
                SymtomValue.add(VAGO2MFed1.getText().toString());
            } else if (chkAGO2MFed1.isChecked() == false) {
                DeleteSymtom(VAGO2MFed1.getText().toString());
            }


            if (chkAGO2MFed2.isChecked() == true) {
                SymtomValue.add(VAGO2MFed2.getText().toString());
            } else if (chkAGO2MFed2.isChecked() == false) {
                DeleteSymtom(VAGO2MFed2.getText().toString());
            }

            if (chkAGO2MFed3.isChecked() == true) {
                SymtomValue.add(VAGO2MFed3.getText().toString());
            } else if (chkAGO2MFed3.isChecked() == false) {
                DeleteSymtom(VAGO2MFed3.getText().toString());
            }

            if (chkAGO2MFed4.isChecked() == true) {
                SymtomValue.add(VAGO2MFed4.getText().toString());
            } else if (chkAGO2MFed4.isChecked() == false) {
                DeleteSymtom(VAGO2MFed4.getText().toString());
            }

            if (chkAGO2MFed5.isChecked() == true) {
                SymtomValue.add(VAGO2MFed5.getText().toString());
            } else if (chkAGO2MFed5.isChecked() == false) {
                DeleteSymtom(VAGO2MFed5.getText().toString());
            }

            if (chkAGO2MFed6.isChecked() == true) {
                SymtomValue.add(VAGO2MFed6.getText().toString());
            } else if (chkAGO2MFed6.isChecked() == false) {
                DeleteSymtom(VAGO2MFed6.getText().toString());
            }
            //2t05 year

            if (chkAG25YSym1.isChecked() == true) {
                SymtomValue.add(VAG25YSymCode1.getText().toString());
            } else if (chkAG25YSym1.isChecked() == false) {
                DeleteSymtom(VAG25YSymCode1.getText().toString());
            }


            if (chkAG25YSym2.isChecked() == true) {
                SymtomValue.add(VAG25YSymCode2.getText().toString());
            } else if (chkAG25YSym2.isChecked() == false) {
                DeleteSymtom(VAG25YSymCode2.getText().toString());
            }

            if (chkAG25YSym3.isChecked() == true) {
                SymtomValue.add(VAG25YSymCode3.getText().toString());
            } else if (chkAG25YSym3.isChecked() == false) {
                DeleteSymtom(VAG25YSymCode3.getText().toString());
            }
            if (chkAG25YSym4.isChecked() == true) {
                SymtomValue.add(VAG25YSymCode4.getText().toString());
            } else if (chkAG25YSym4.isChecked() == false) {
                DeleteSymtom(VAG25YSymCode4.getText().toString());
            }
            if (chkAG25YDar1.isChecked() == true) {
                SymtomValue.add(VAG25YDarCode1.getText().toString());
            } else if (chkAG25YDar1.isChecked() == false) {
                DeleteSymtom(VAG25YDarCode1.getText().toString());
            }

            if (chkAG25YDar2.isChecked() == true) {
                SymtomValue.add(VAG25YDarCode2.getText().toString());
            } else if (chkAG25YDar2.isChecked() == false) {
                DeleteSymtom(VAG25YDarCode2.getText().toString());
            }


            if (chkAG25YDar3.isChecked() == true) {
                SymtomValue.add(VAG25YDarCode3.getText().toString());
            } else if (chkAG25YDar3.isChecked() == false) {
                DeleteSymtom(VAG25YDarCode3.getText().toString());
            }


            if (chkAG25YDar4.isChecked() == true) {
                SymtomValue.add(VAG25YDarCode4.getText().toString());
            } else if (chkAG25YDar4.isChecked() == false) {
                DeleteSymtom(VAG25YDarCode4.getText().toString());
            }

            if (chkAG25YDar5.isChecked() == true) {
                SymtomValue.add(VAG25YDarCode5.getText().toString());
            } else if (chkAG25YDar5.isChecked() == false) {
                DeleteSymtom(VAG25YDarCode5.getText().toString());
            }


            if (chkAG25YDar6.isChecked() == true) {
                SymtomValue.add(VAG25YDarCode6.getText().toString());
            } else if (chkAG25YDar6.isChecked() == false) {
                DeleteSymtom(VAG25YDarCode6.getText().toString());
            }


            if (chkAG25YDar7.isChecked() == true) {
                SymtomValue.add(VAG25YDarCode7.getText().toString());
            } else if (chkAG25YDar7.isChecked() == false) {
                DeleteSymtom(VAG25YDarCode7.getText().toString());
            }


            if (chkAG25YDar8.isChecked() == true) {
                SymtomValue.add(VAG25YDarCode8.getText().toString());
            } else if (chkAG25YDar8.isChecked() == false) {
                DeleteSymtom(VAG25YDarCode8.getText().toString());
            }

            if (chkAG25YDar9.isChecked() == true) {
                SymtomValue.add(VAG25YDarCode9.getText().toString());
            } else if (chkAG25YDar9.isChecked() == false) {
                DeleteSymtom(VAG25YDarCode9.getText().toString());
            }

            if (chkAG25YAm1.isChecked() == true) {
                SymtomValue.add(VAG25YAmCode1.getText().toString());
            } else if (chkAG25YAm1.isChecked() == false) {
                DeleteSymtom(VAG25YAmCode1.getText().toString());
            }

            if (chkAG25YFev1.isChecked() == true) {
                SymtomValue.add(VAG25YFevCode1.getText().toString());
            } else if (chkAG25YFev1.isChecked() == false) {
                DeleteSymtom(VAG25YFevCode1.getText().toString());
            }

            if (chkAG25YFev2.isChecked() == true) {
                SymtomValue.add(VAG25YFevCode2.getText().toString());
            } else if (chkAG25YFev2.isChecked() == false) {
                DeleteSymtom(VAG25YFevCode2.getText().toString());
            }

            if (chkAG25YMal1.isChecked() == true) {
                SymtomValue.add(VAG25YMalCode1.getText().toString());
            } else if (chkAG25YMal1.isChecked() == false) {
                DeleteSymtom(VAG25YMalCode1.getText().toString());
            }

            if (chkAG25YMal2.isChecked() == true) {
                SymtomValue.add(VAG25YMalCode2.getText().toString());
            } else if (chkAG25YMal2.isChecked() == false) {
                DeleteSymtom(VAG25YMalCode2.getText().toString());
            }

            if (chkAG25YNut1.isChecked() == true) {
                SymtomValue.add(VAG25YNutCode1.getText().toString());
            } else if (chkAG25YNut1.isChecked() == false) {
                DeleteSymtom(VAG25YNutCode1.getText().toString());
            }

            if (chkAG25YNut2.isChecked() == true) {
                SymtomValue.add(VAG25YNutCode2.getText().toString());
            } else if (chkAG25YNut2.isChecked() == false) {
                DeleteSymtom(VAG25YNutCode2.getText().toString());
            }

            if (chkAG25YNut3.isChecked() == true) {
                SymtomValue.add(VAG25YNutCode3.getText().toString());
            } else if (chkAG25YNut3.isChecked() == false) {
                DeleteSymtom(VAG25YNutCode3.getText().toString());
            }

            if (chkAG25YNut4.isChecked() == true) {
                SymtomValue.add(VAG25YNutCode4.getText().toString());
            } else if (chkAG25YNut4.isChecked() == false) {
                DeleteSymtom(VAG25YNutCode4.getText().toString());
            }

            if (chkAG25YLow1.isChecked() == true) {
                SymtomValue.add(VAG25YLowCode1.getText().toString());
            } else if (chkAG25YLow1.isChecked() == false) {
                DeleteSymtom(VAG25YLowCode1.getText().toString());
            }

            if (chkAG25YLow2.isChecked() == true) {
                SymtomValue.add(VAG25YLowCode2.getText().toString());
            } else if (chkAG25YLow2.isChecked() == false) {
                DeleteSymtom(VAG25YLowCode2.getText().toString());
            }

            if (chkAG25YLow3.isChecked() == true) {
                SymtomValue.add(VAG25YLowCode3.getText().toString());
            } else if (chkAG25YLow3.isChecked() == false) {
                DeleteSymtom(VAG25YLowCode3.getText().toString());
            }

            if (chkAG25YOth1.isChecked() == true) {
                SymtomValue.add(VAG25YOthCode1.getText().toString());
            } else if (chkAG25YOth1.isChecked() == false) {
                DeleteSymtom(VAG25YOthCode1.getText().toString());
            }

            if (chkAG25YOth2.isChecked() == true) {
                SymtomValue.add(VAG25YOthCode2.getText().toString());
            } else if (chkAG25YOth2.isChecked() == false) {
                DeleteSymtom(VAG25YOthCode2.getText().toString());
            }

            if (chkAG25YOth3.isChecked() == true) {
                SymtomValue.add(VAG25YOthCode3.getText().toString());
            } else if (chkAG25YOth3.isChecked() == false) {
                DeleteSymtom(VAG25YOthCode3.getText().toString());
            }

            if (chkAG25YOth4.isChecked() == true) {
                SymtomValue.add(VAG25YOthCode4.getText().toString());
            } else if (chkAG25YOth4.isChecked() == false) {
                DeleteSymtom(VAG25YOthCode4.getText().toString());
            }

            if (chkAG25YOth5.isChecked() == true) {
                SymtomValue.add(VAG25YOthCode5.getText().toString());
            } else if (chkAG25YOth5.isChecked() == false) {
                DeleteSymtom(VAG25YOthCode5.getText().toString());
            }

            if (chkAG25YOth6.isChecked() == true) {
                SymtomValue.add(VAG25YOthCode6.getText().toString());
            } else if (chkAG25YOth6.isChecked() == false) {
                DeleteSymtom(VAG25YOthCode6.getText().toString());
            }

            if (chkAG25YOth7.isChecked() == true) {
                SymtomValue.add(VAG25YOthCode7.getText().toString());
            } else if (chkAG25YOth7.isChecked() == false) {
                DeleteSymtom(VAG25YOthCode7.getText().toString());
            }
            int i = 0;
            for (i = 0; i < SymtomValue.size(); i++) {
                for (String temp : SymtomValue) {
                    // System.out.println(temp);
                    SaveSymtom(temp);
                }


            }


        } catch (Exception e) {
            Connection.MessageBox(Under5child.this, e.getMessage());
            return;
        }
    }


    private void SearchUnder5chaild() {

        String SQL = "";
        SQL = "Select * from under5Child where healthId='" + g.getGeneratedId() + "' and providerId='" + g.getProvCode() + "' and strftime('%d/%m/%Y', date(visitDate))= '" + dtpVDate.getText() + "'";
        if (C.Existence(SQL)) {
            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                txtRemarks.setText(cur.getString(cur.getColumnIndex("remarks")));

                cur.moveToNext();
            }
            cur.close();
        }
    }


    private void DataSearch(String Dist, String Upz, String UN, String Mouza, String Vill, String HHNo, String SNo) {
        try {
            int M = 0;
            int D = 0;
            int Ag = 0;
            String Dob = "";
            String SQL = "";
            SQL = "Select Dist, Upz, UN, Mouza, Vill, HHNo, SNo as SNo, ifnull(HealthID,'') as HealthID, ifnull(NameEng,'') as NameEng,";
            SQL += " ifnull(NameBang,'') as NameBang, ifnull(Rth,'') as Rth, ifnull(HaveNID,'') as HaveNID, ifnull(NID,'') as NID, ifnull(NIDStatus,'') as NIDStatus, ifnull(HaveBR,'') as HaveBR, ifnull(BRID,'') as BRID, ifnull(BRIDStatus,'') as BRIDStatus, ifnull(MobileNo1,'') as MobileNo1,";
            SQL += " ifnull(MobileNo2,'') as MobileNo2, ifnull(DOB,'') as DOB, ifnull(Age,'') as Age, ifnull(DOBSource,'') as DOBSource, ifnull(BPlace,'') as BPlace, ifnull(FNo,'') as FNo, ifnull(Father,'') as Father, ifnull(MNo,'') as MNo, ifnull(Mother,'') as Mother,";
            SQL += " ifnull(Sex,'') as Sex, ifnull(MS,'') as MS, ifnull(SPNO1,'') as SPNO1,ifnull(SPNO2,'') as SPNO2,ifnull(SPNO3,'') as SPNO3,ifnull(SPNO4,'') as SPNO4, ifnull(ELCONo,'') as ELCONo, ifnull(ELCODontKnow,'') as ELCODontKnow, ifnull(EDU,'') as EDU, ifnull(Rel,'') as Rel, ifnull(Nationality,'') as Nationality, ifnull(OCP,'') as OCP";
            SQL += ",(select NameEng  from member where Dist='" + Dist + "' and Upz='" + Upz + "' and UN='" + UN + "' and Mouza='" + Mouza + "' and Vill='" + Vill + "' and HHNo='" + HHNo + "' and SNo=(select  SPNO1  from member  Where Dist='" + Dist + "' and Upz='" + Upz + "' and UN='" + UN + "' and Mouza='" + Mouza + "' and Vill='" + Vill + "' and HHNo='" + HHNo + "' and SNo='" + SNo + "'))as HusName";
            SQL += " from Member Where Dist='" + Dist + "' and Upz='" + Upz + "' and UN='" + UN + "' and Mouza='" + Mouza + "' and Vill='" + Vill + "' and HHNo='" + HHNo + "' and SNo='" + SNo + "'";
            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                txtHealthID.setText(cur.getString(cur.getColumnIndex("HealthID")));
                //  txtSNo.setText(cur.getString(cur.getColumnIndex("SNo")));
                txtHHNO.setText(cur.getString(cur.getColumnIndex("HHNo")));
                txtName.setText(cur.getString(cur.getColumnIndex("NameEng")));
                //txtHusName.setText(cur.getString(cur.getColumnIndex("HusName")));

                Dob = Global.DateConvertDMY(cur.getString(cur.getColumnIndex("DOB")));
                D = Global.DateDifferenceDays(Global.DateNowDMY(), Dob);
                Ag = Integer.valueOf(cur.getString(cur.getColumnIndex("Age")));

                M = (D / 30);
                // txtAge.setText(String.valueOf(M));

                AgeN = String.valueOf(M);

                String sex = cur.getString(cur.getColumnIndex("Sex"));
                if (sex.equals("1")) {
                    txtSex.setText("ছেলে");
                } else if (sex.equals("2")) {
                    txtSex.setText("মেয়ে");
                }

                if (D < 60 & Ag < 1) {
                    txtAge.setText(String.valueOf(D));
                    VlblAge1.setText("দিন");
                } else if (D > 60 & Ag < 1) {

                    txtAge.setText(String.valueOf(D / 30));
                    VlblAge1.setText("মাস");
                } else {
                    txtAge.setText(cur.getString(cur.getColumnIndex("Age")));
                    VlblAge1.setText("বছর");


                }
                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(Under5child.this, e.getMessage());
            return;
        }
    }


    public void SaveSymtom(String SymtomCode) {
        try {


            String SQL = "";

            if (!C.Existence("Select healthId,providerId,visitDate,problemCode,StartTime,EndTime,Upload from " + TableNameU5p + "  where healthId='" + g.getGeneratedId() + "' and providerId='" + g.getProvCode() + "' and strftime('%d/%m/%Y', date(visitDate))= '" + dtpVDate.getText() + "' and problemCode = '" + SymtomCode + "'"))

            {

                SQL = "Insert into " + TableNameU5p + "(healthId,providerId,visitDate,problemCode,startTime,endTime,systemEntryDate,Upload)Values('" + g.getGeneratedId() + "','" + g.getProvCode() + "','" + Global.DateConvertYMD(dtpVDate.getText().toString()) + "','" + SymtomCode + "','" + StartTime + "','" + g.CurrentTime24() + "','" + Global.DateTimeNowYMDHMS() + "','2')";
                C.Save(SQL);
            }
            // Connection.MessageBox(SymtomAG02M.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");


        } catch (Exception e) {
            Connection.MessageBox(Under5child.this, e.getMessage());
            return;
        }

    }

    public void SaveTreatment(String treatmentCode) {
        try {


            String SQL = "";

            if (!C.Existence("Select healthId,providerId,visitDate,adviceCode,StartTime,EndTime,Upload from " + TableNameU5a + "  where healthId='" + g.getGeneratedId() + "' and providerId='" + g.getProvCode() + "' and strftime('%d/%m/%Y', date(visitDate))= '" + dtpVDate.getText() + "' and adviceCode = '" + treatmentCode + "'"))

            {

                SQL = "Insert into " + TableNameU5a + "(healthId,providerId,visitDate,adviceCode,startTime,endTime,systemEntryDate,Upload)Values('" + g.getGeneratedId() + "','" + g.getProvCode() + "','" + Global.DateConvertYMD(dtpVDate.getText().toString()) + "','" + treatmentCode + "','" + StartTime + "','" + g.CurrentTime24() + "','" + Global.DateNowDMY() + "','2')";
                C.Save(SQL);
            }
            // Connection.MessageBox(SymtomAG02M.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");


        } catch (Exception e) {
            Connection.MessageBox(Under5child.this, e.getMessage());
            return;
        }

    }

    public void DeleteTreatment(String TreatmentCode) {
        try {


            String SQL = "";

            if (C.Existence("Select * from under5ChildAdvice where healthId='" + g.getGeneratedId() + "' and providerId='" + g.getProvCode() + "' and strftime('%d/%m/%Y', date(visitDate))='" + dtpVDate.getText() + "' and adviceCode = '" + TreatmentCode + "'"))

            {

                SQL = "Delete from under5ChildAdvice where healthId='" + g.getGeneratedId() + "' and providerId='" + g.getProvCode() + "' and strftime('%d/%m/%Y', date(visitDate))='" + dtpVDate.getText() + "' and adviceCode= '" + TreatmentCode + "'";
                C.Save(SQL);

            } else {

            }


        } catch (Exception e) {
            Connection.MessageBox(Under5child.this, e.getMessage());
            return;
        }

    }


    public void DeleteSymtom(String SymtomCode) {
        try {


            String SQL = "";

            if (C.Existence("Select * from under5ChildProblem where healthId='" + g.getGeneratedId() + "' and providerId='" + g.getProvCode() + "' and strftime('%d/%m/%Y', date(visitDate))= '" + dtpVDate.getText() + "' and problemCode = '" + SymtomCode + "'"))

            {

                SQL = "Delete from under5ChildProblem where healthId='" + g.getGeneratedId() + "' and providerId='" + g.getProvCode() + "' and strftime('%d/%m/%Y', date(visitDate))='" + dtpVDate.getText() + "' and problemCode= '" + SymtomCode + "'";
                C.Save(SQL);

            } else {

            }

        } catch (Exception e) {
            Connection.MessageBox(Under5child.this, e.getMessage());
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

            dtpDate = (EditText) findViewById(R.id.dtpVDate);

            if (VariableID.equals("btnVDate")) {
                dtpDate = (EditText) findViewById(R.id.dtpVDate);
            }


   /*         else if(VariableID.equals("btnDOPNCCh2"))
            {
                // dtpDate = (EditText)findViewById(R.id.dtpDOPNCCh2);
            }
            else if(VariableID.equals("btnDOPNCCh3"))
            {
                // dtpDate = (EditText)findViewById(R.id.dtpDOPNCCh3);
            }
            else if(VariableID.equals("btnDOPNCCh4"))
            {
                //  dtpDate = (EditText)findViewById(R.id.dtpDOPNCCh4);
            }*/


            dtpDate.setText(new StringBuilder()
                    .append(Global.Right("00" + mDay, 2)).append("/")
                    .append(Global.Right("00" + mMonth, 2)).append("/")
                    .append(mYear));


            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = sdf.format(c.getTime());
                Date date1 = sdf.parse(formattedDate);
                Date date4 = sdf.parse(dtpVDate.getText().toString());


                if (date4.after(date1)) {
                    if (VariableID.equals("btnVDate")) {
                        Connection.MessageBox(Under5child.this, "ভিজিটের  তারিখ আজকের তারিখ অপেক্ষা বড় হবে না");
                    }

                    dtpVDate.setText(null);
                    // dtpVDate.requestFocus();
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


}