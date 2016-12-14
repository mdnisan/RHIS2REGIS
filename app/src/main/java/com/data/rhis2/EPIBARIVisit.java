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
 * Created by Nisan on 6/6/2016.
 */
public class EPIBARIVisit extends Activity {
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
        inflater.inflate(R.menu.menubarivisit, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuClose:
                finish();
                Intent f = new Intent(getApplicationContext(), HouseholdIndex.class);
                startActivity(f);
                return true;
/*                case R.id.menuNewBariVisit:
                Intent f1 = new Intent(getApplicationContext(), EPIBARIVisit.class);
                startActivity(f1);
                return true;*/
        }
        return false;
    }

    /* public boolean onOptionsItemSelected(MenuItem item) {
         AlertDialog.Builder adb = new AlertDialog.Builder(EPIBARIVisit.this);
         switch (item.getItemId()) {
             case R.id.menuClose:
                 adb.setTitle("Close");
                 adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান[Yes/No]?");
                 adb.setNegativeButton("No", null);
                 adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                     public void onClick(DialogInterface dialog, int which) {
                         finish();
                     }});
                 adb.show();
                 return true;
         }
         return false;
     }*/
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

    /* LinearLayout secIdno;
     TextView VlblIdno;
     EditText txtIdno;*/
    LinearLayout seclblvisitBari;
    LinearLayout secQBHHNo;
    TextView VlblQBHHNo;
    EditText txtQBHHNo;

    LinearLayout secQBHHHNo;
    TextView VlblQBHHHNo;
    TextView txtQBHHHN0;


    LinearLayout secQBHHHName;
    TextView VlblQBHHHName;
    TextView txtQBHHHName;
    LinearLayout secQBHEndDate;
    TextView VlblQBHEndDate;

    EditText dtpQBHEndDate;
    ImageButton btnQBHEndDate;
    /*  LinearLayout secQBPVisitEPI;
      TextView VlblQBPVisitEPI;
      Spinner spnQBPVisitEPI;*/
    LinearLayout secQBNextDoss;
    TextView VlblQBNextDoss;
    RadioGroup rdogrpQBNextDoss;

    RadioButton rdoQBNextDoss1;
    RadioButton rdoQBNextDoss2;
    LinearLayout secQB1stDoss;
    LinearLayout secQB1stDoss1;
    // LinearLayout secQB1stDoss2;
    // TextView VlblQB1stDoss2;
    CheckBox chkQBPVisitEPI1;
    CheckBox chkQBPVisitEPI2;
    CheckBox chkQBPVisitEPI3;
    CheckBox chkQBPVisitEPI4;
    CheckBox chkQBPVisitEPI5;
    CheckBox chkQB1stDoss1;
    CheckBox chkQB1stDoss2;
    //  LinearLayout secQB1stDoss3;
    //  TextView VlblQB1stDoss3;
    CheckBox chkQB1stDoss3;
    //   LinearLayout secQB1stDoss4;
    // TextView VlblQB1stDoss4;
    CheckBox chkQB1stDoss4;
    // LinearLayout secQB1stDoss5;
    // TextView VlblQB1stDoss5;
    CheckBox chkQB1stDoss5;
    LinearLayout secQBCNoSessiondoss;
    RadioGroup rdogrpQBCNoSessiondoss;

    RadioButton rdoQBCNoSessiondoss1;
    RadioButton rdoQBCNoSessiondoss2;
    TextView VlblQBCNoSessiondoss;
    Spinner spnQBCNoSessiondoss;

    LinearLayout secQBCNoSessiondoss1;
    TextView VlblQBCNoSessiondoss1;
    LinearLayout secQBWNoSessiondoss;
    RadioGroup rdogrpQBWNoSessiondoss;

    RadioButton rdoQBWNoSessiondoss1;
    RadioButton rdoQBWNoSessiondoss2;
    LinearLayout secQBWNoSessiondoss1;
    TextView VlblQBWNoSessiondoss;
    Spinner spnQBWNoSessiondoss;
    LinearLayout secQBVitmA;
    TextView VlblQBVitmA;
    //CheckBox chkQBVitmA;
    RadioGroup rdogrpQBVitmA;

    RadioButton rdoQBVitmA1;
    RadioButton rdoQBVitmA2;
    LinearLayout secQBChildCard;
    TextView VlblQBChildCard;
    RadioGroup rdogrpQBChildCard;

    RadioButton rdoQBChildCard1;
    RadioButton rdoQBChildCard2;
    LinearLayout secQBWomenCard;
    TextView VlblQBWomenCard;
    RadioGroup rdogrpQBWomenCard;

    RadioButton rdoQBWomenCard1;
    RadioButton rdoQBWomenCard2;

    String StartTime;

    TextView txtQBUpz;
    TextView txtQBDist;
    TextView txtQBUN;
    Spinner spnWord;

    TextView txtQBVill;
    //Spinner spnVill;
   /* Spinner spnEpiSubBlock;
    Spinner spnVCenterName;*/

    LinearLayout secVdate;
    TextView VlblVdate;
    EditText dtpVdate;
    ImageButton btnVdate;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.epibarivisit);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();

            TableName = "EPIBARIVisit";


      /*      secIdno=(LinearLayout)findViewById(R.id.secIdno);
            VlblIdno=(TextView) findViewById(R.id.VlblIdno);
            txtIdno=(EditText) findViewById(R.id.txtIdno);*/
            txtQBDist = (TextView) findViewById(R.id.txtQBDist);
            txtQBDist.setText(C.ReturnSingleValue("select cast(ZILLAID as varchar(2))||'-'||ZILLANAME from Zilla where ZILLAID='" + g.getDistrict() + "'"));
            txtQBUpz = (TextView) findViewById(R.id.txtQBUpz);
            txtQBUpz.setText(C.ReturnSingleValue("select substr('0' ||cast(upazilaid as varchar(2)), -2, 2)||'-'||upazilaname from upazila where zillaid='" + g.getDistrict() + "' and upazilaid='" + g.getUpazila() + "'"));
            txtQBUN = (TextView) findViewById(R.id.txtQBUN);
            txtQBUN.setText(C.ReturnSingleValue("select cast(unionid as varchar(2))||'-'||unionname from Unions where zillaid='" + g.getDistrict() + "' and upazilaid='" + g.getUpazila() + "' and unionid='" + g.getUnion() + "'"));
            spnWord = (Spinner) findViewById(R.id.spnWord);
            //"select distinct ward from ProviderArea WHERE provCode = '"+ ProvCode +"'"
            // spnEPIBlock.setAdapter(C.getArrayAdapter("select BCode||'-'||BNameBan from HABlock"));

            // spnWord.setAdapter(C.getArrayAdapter("select ward from ProviderArea where provCode = '"+ g.getFWAProvCode()+"'"));
            txtQBVill = (TextView) findViewById(R.id.txtQBVill);
            //Select '-' as VILLAGENAME union
            if (g.getMouza().length() == 2) {
                txtQBVill.setText("0" + g.getMouza() + "-" + g.getVillage() + ", " + g.getVillageName());
            } else
                txtQBVill.setText(g.getMouza() + "-" + g.getVillage() + ", " + g.getVillageName());

            spnWord.setAdapter(C.getArrayAdapter("select ward from ProviderArea where zillaid='" + g.getDistrict() + "' and upazilaid='" + g.getUpazila() + "' and unionid='" + g.getUnion() + "' and mouzaid='" + g.getMouza() + "' and villageid='" + g.getVillage() + "'"));
            // lblVillage.setText("গ্রামঃ  "+ g.getMouza() +"-"+ g.getVillage() + ", " + g.getVillageName());


         /*   spnVill.setAdapter(C.getArrayAdapterMultiline("select substr('0' ||v.MOUZAID, -3, 3)||v.VILLAGEID||'-'||v.VILLAGENAME as VILLAGENAME\n" +
                    "from Village v\n" +
                    "left outer join ProviderArea p on v.ZILLAID=p.zillaid and  v.UPAZILAID=p.upazilaid and\n" +
                    "v.MOUZAID||v.VILLAGEID=p.mouzaid||p.villageid where p.provCode='"+ g.getFWAProvCode()+"'"));
  */         /* spnEpiSubBlock=(Spinner) findViewById(R.id.spnEpiSubBlock);
            spnEpiSubBlock.setAdapter(C.getArrayAdapter("select sb.BCode||'-'||sb.BNameBan from HABlock sb\n" +
                    "left outer join ProviderArea p on sb.BCode=p.FWAUnit where p.provCode='"+ g.getFWAProvCode()+"'"));
            spnVCenterName=(Spinner) findViewById(R.id.spnVCenterName);
            spnVCenterName.setAdapter(C.getArrayAdapter("select schedulerId||providerId||'-'||centerName from epiScheduler where subBlockId='"+Global.Left(spnEpiSubBlock.getSelectedItem().toString(),2)+"' and providerId='"+ g.getFWAProvCode()+"'"));
*/
            seclblvisitBari = (LinearLayout) findViewById(R.id.seclblvisitBari);
            secQBHHNo = (LinearLayout) findViewById(R.id.secQBHHNo);
            VlblQBHHNo = (TextView) findViewById(R.id.VlblQBHHNo);
            txtQBHHNo = (EditText) findViewById(R.id.txtQBHHNo);
         /*  if(g.getCallFrom().equals("2"))
           {
               secQBHHHNo.setVisibility(View.GONE);
           }

            else
           {
               secQBHHHNo.setVisibility(View.VISIBLE);
           }*/
            secQBHHHNo = (LinearLayout) findViewById(R.id.secQBHHHNo);
            VlblQBHHHNo = (TextView) findViewById(R.id.VlblQBHHHNo);
            txtQBHHHN0 = (TextView) findViewById(R.id.txtQBHHHN0);
            txtQBHHHN0.setText(g.getHouseholdNo());
            secQBHHHName = (LinearLayout) findViewById(R.id.secQBHHHName);
            VlblQBHHHName = (TextView) findViewById(R.id.VlblQBHHHName);
            txtQBHHHName = (TextView) findViewById(R.id.txtQBHHHName);
            txtQBHHHName.setText(g.getHouseholdHName());
            secQBHEndDate = (LinearLayout) findViewById(R.id.secQBHEndDate);
            VlblQBHEndDate = (TextView) findViewById(R.id.VlblQBHEndDate);
            dtpQBHEndDate = (EditText) findViewById(R.id.dtpQBHEndDate);

            secVdate = (LinearLayout) findViewById(R.id.secVdate);
            VlblVdate = (TextView) findViewById(R.id.VlblVdate);
            dtpVdate = (EditText) findViewById(R.id.dtpVdate);

       /*     secQBPVisitEPI=(LinearLayout)findViewById(R.id.secQBPVisitEPI);
            VlblQBPVisitEPI=(TextView) findViewById(R.id.VlblQBPVisitEPI);
            spnQBPVisitEPI=(Spinner) findViewById(R.id.spnQBPVisitEPI);
            List<String> listQBPVisitEPI = new ArrayList<String>();

            listQBPVisitEPI.add("");
            listQBPVisitEPI.add("01-বিসিজি: যক্ষার কাজ করে");
            listQBPVisitEPI.add("02-পেন্টা: ৫টি রোগের প্রতিষধেক");
            listQBPVisitEPI.add("03-পোলিও: পোলিও মাইলাইটিস রোগের প্রতিষধেক");
            listQBPVisitEPI.add("04-হাম: হাম রোগের প্রতিষধেক");
            listQBPVisitEPI.add("05-টিটি: ধনুষ্ঠংকার রোগের প্রতিষধেক");
            ArrayAdapter<String> adptrQBPVisitEPI= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listQBPVisitEPI);
            spnQBPVisitEPI.setAdapter(adptrQBPVisitEPI);*/

            secQBNextDoss = (LinearLayout) findViewById(R.id.secQBNextDoss);
            VlblQBNextDoss = (TextView) findViewById(R.id.VlblQBNextDoss);
            rdogrpQBNextDoss = (RadioGroup) findViewById(R.id.rdogrpQBNextDoss);

            rdoQBNextDoss1 = (RadioButton) findViewById(R.id.rdoQBNextDoss1);
            rdoQBNextDoss2 = (RadioButton) findViewById(R.id.rdoQBNextDoss2);
            secQB1stDoss = (LinearLayout) findViewById(R.id.secQB1stDoss);
            secQB1stDoss1 = (LinearLayout) findViewById(R.id.secQB1stDoss1);
            // secQB1stDoss2=(LinearLayout)findViewById(R.id.secQB1stDoss2);
            // VlblQB1stDoss2=(TextView) findViewById(R.id.VlblQB1stDoss2);
            chkQBPVisitEPI1 = (CheckBox) findViewById(R.id.chkQBPVisitEPI1);
            chkQBPVisitEPI2 = (CheckBox) findViewById(R.id.chkQBPVisitEPI2);
            chkQBPVisitEPI3 = (CheckBox) findViewById(R.id.chkQBPVisitEPI3);
            chkQBPVisitEPI4 = (CheckBox) findViewById(R.id.chkQBPVisitEPI4);
            chkQBPVisitEPI5 = (CheckBox) findViewById(R.id.chkQBPVisitEPI5);
            chkQB1stDoss1 = (CheckBox) findViewById(R.id.chkQB1stDoss1);
            chkQB1stDoss2 = (CheckBox) findViewById(R.id.chkQB1stDoss2);
            //   secQB1stDoss3=(LinearLayout)findViewById(R.id.secQB1stDoss3);
            //  VlblQB1stDoss3=(TextView) findViewById(R.id.VlblQB1stDoss3);
            chkQB1stDoss3 = (CheckBox) findViewById(R.id.chkQB1stDoss3);
            //   secQB1stDoss4=(LinearLayout)findViewById(R.id.secQB1stDoss4);
            //VlblQB1stDoss4=(TextView) findViewById(R.id.VlblQB1stDoss4);
            chkQB1stDoss4 = (CheckBox) findViewById(R.id.chkQB1stDoss4);
            // secQB1stDoss5=(LinearLayout)findViewById(R.id.secQB1stDoss5);
            //  VlblQB1stDoss5=(TextView) findViewById(R.id.VlblQB1stDoss5);
            chkQB1stDoss5 = (CheckBox) findViewById(R.id.chkQB1stDoss5);
            secQBCNoSessiondoss = (LinearLayout) findViewById(R.id.secQBCNoSessiondoss);
            rdogrpQBCNoSessiondoss = (RadioGroup) findViewById(R.id.rdogrpQBCNoSessiondoss);

            rdoQBCNoSessiondoss1 = (RadioButton) findViewById(R.id.rdoQBCNoSessiondoss1);
            rdoQBCNoSessiondoss2 = (RadioButton) findViewById(R.id.rdoQBCNoSessiondoss2);

            rdogrpQBCNoSessiondoss.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int radioButtonID) {
                    RadioButton rb = (RadioButton) findViewById(rdogrpQBCNoSessiondoss.getCheckedRadioButtonId());
                    if (rb == null) return;
                    String rbData = Global.Left(rb.getText().toString(), 1);
                    if (rbData.equalsIgnoreCase("1")) {

                        secQBCNoSessiondoss1.setVisibility(View.GONE);

                        spnQBCNoSessiondoss.setSelection(0);


                    } else {

                        secQBCNoSessiondoss1.setVisibility(View.VISIBLE);
                        //secS635.setVisibility(View.VISIBLE);
                    }
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });
            secQBCNoSessiondoss1 = (LinearLayout) findViewById(R.id.secQBCNoSessiondoss1);
            VlblQBCNoSessiondoss = (TextView) findViewById(R.id.VlblQBCNoSessiondoss);
            spnQBCNoSessiondoss = (Spinner) findViewById(R.id.spnQBCNoSessiondoss);
            List<String> listQBCNoSessiondoss = new ArrayList<String>();

            listQBCNoSessiondoss.add("");
            listQBCNoSessiondoss.add("01-বাড়ীতে থাকে না");
            listQBCNoSessiondoss.add("02-শিশু অসুস্থ থাকলে");
            listQBCNoSessiondoss.add("03-দীর্ঘ সময় আত্মীয়র বাড়ী থাকলে");
            ArrayAdapter<String> adptrQBCNoSessiondoss = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listQBCNoSessiondoss);
            spnQBCNoSessiondoss.setAdapter(adptrQBCNoSessiondoss);

            secQBWNoSessiondoss = (LinearLayout) findViewById(R.id.secQBWNoSessiondoss);
            rdogrpQBWNoSessiondoss = (RadioGroup) findViewById(R.id.rdogrpQBWNoSessiondoss);

            rdoQBWNoSessiondoss1 = (RadioButton) findViewById(R.id.rdoQBWNoSessiondoss1);
            rdoQBWNoSessiondoss2 = (RadioButton) findViewById(R.id.rdoQBWNoSessiondoss2);

            rdogrpQBWNoSessiondoss.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int radioButtonID) {
                    RadioButton rb = (RadioButton) findViewById(rdogrpQBWNoSessiondoss.getCheckedRadioButtonId());
                    if (rb == null) return;
                    String rbData = Global.Left(rb.getText().toString(), 1);
                    if (rbData.equalsIgnoreCase("1")) {

                        secQBWNoSessiondoss1.setVisibility(View.GONE);

                        spnQBWNoSessiondoss.setSelection(0);


                    } else {

                        secQBWNoSessiondoss1.setVisibility(View.VISIBLE);
                        //secS635.setVisibility(View.VISIBLE);
                    }
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });
            secQBWNoSessiondoss1 = (LinearLayout) findViewById(R.id.secQBWNoSessiondoss1);
            VlblQBWNoSessiondoss = (TextView) findViewById(R.id.VlblQBWNoSessiondoss);
            spnQBWNoSessiondoss = (Spinner) findViewById(R.id.spnQBWNoSessiondoss);
            List<String> listQBWNoSessiondoss = new ArrayList<String>();

            listQBWNoSessiondoss.add("");
            listQBWNoSessiondoss.add("01-বাড়ীতে থাকে না");
            listQBWNoSessiondoss.add("02-কিশোরী মেয়েদের বিবাহের পর অন্যত্র চলে যায়");
            listQBWNoSessiondoss.add("03-ধর্মীয় গোড়ামী");
            listQBWNoSessiondoss.add("04-অসুস্থ থাকার কারণে");
            ArrayAdapter<String> adptrQBWNoSessiondoss = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listQBWNoSessiondoss);
            spnQBWNoSessiondoss.setAdapter(adptrQBWNoSessiondoss);

            secQBVitmA = (LinearLayout) findViewById(R.id.secQBVitmA);
            VlblQBVitmA = (TextView) findViewById(R.id.VlblQBVitmA);
            rdogrpQBVitmA = (RadioGroup) findViewById(R.id.rdogrpQBVitmA);

            rdoQBVitmA1 = (RadioButton) findViewById(R.id.rdoQBVitmA1);
            rdoQBVitmA2 = (RadioButton) findViewById(R.id.rdoQBVitmA2);
            //chkQBVitmA=(CheckBox) findViewById(R.id.chkQBVitmA);
            secQBChildCard = (LinearLayout) findViewById(R.id.secQBChildCard);
            VlblQBChildCard = (TextView) findViewById(R.id.VlblQBChildCard);
            rdogrpQBChildCard = (RadioGroup) findViewById(R.id.rdogrpQBChildCard);

            rdoQBChildCard1 = (RadioButton) findViewById(R.id.rdoQBChildCard1);
            rdoQBChildCard2 = (RadioButton) findViewById(R.id.rdoQBChildCard2);
            secQBWomenCard = (LinearLayout) findViewById(R.id.secQBWomenCard);
            VlblQBWomenCard = (TextView) findViewById(R.id.VlblQBWomenCard);
            rdogrpQBWomenCard = (RadioGroup) findViewById(R.id.rdogrpQBWomenCard);

            rdoQBWomenCard1 = (RadioButton) findViewById(R.id.rdoQBWomenCard1);
            rdoQBWomenCard2 = (RadioButton) findViewById(R.id.rdoQBWomenCard2);
            secQBCNoSessiondoss1.setVisibility(View.GONE);
            secQBWNoSessiondoss1.setVisibility(View.GONE);
            PVisitStatus();

            btnQBHEndDate = (ImageButton) findViewById(R.id.btnQBHEndDate);
            btnQBHEndDate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btnQBHEndDate";
                    showDialog(DATE_DIALOG);
                }
            });


            btnVdate = (ImageButton) findViewById(R.id.btnVdate);
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
                 /*   finish();
                    Intent f = new Intent(getApplicationContext(),HouseholdIndex.class);
                    startActivity(f);*/
                }
            });
        } catch (Exception e) {
            Connection.MessageBox(EPIBARIVisit.this, e.getMessage());
            return;
        }
    }

    private void DataSave() {
        try {
            String DV = "";
            DV = Global.DateValidate(dtpQBHEndDate.getText().toString());

            if (dtpVdate.getText().toString().length() == 0 & secVdate.isShown()) {
                Connection.MessageBox(EPIBARIVisit.this, "Required field:পরিদর্শনের তারিখ .");
                dtpVdate.requestFocus();
                return;
            }

         /*   else if(txtQBHHNo.getText().toString().length()==0 & secQBHHNo.isShown())
            {
                Connection.MessageBox(EPIBARIVisit.this, "Required field:জি আর / হোল্ডিং নং .");
                txtQBHHNo.requestFocus();
                return;
            }*/
            /*else if (txtQBHHHName.getText().toString().length() == 0 & secQBHHHName.isShown()) {
                Connection.MessageBox(EPIBARIVisit.this, "Required field:খানা প্রধানের নাম .");
                txtQBHHHName.requestFocus();
                return;
            }

            if (DV.length() != 0 & secQBHEndDate.isShown()) {
                Connection.MessageBox(EPIBARIVisit.this, DV);
                dtpQBHEndDate.requestFocus();
                return;
            }
     *//*      else if(spnQBPVisitEPI.getSelectedItemPosition()==0  & secQBPVisitEPI.isShown())
            {
                Connection.MessageBox(EPIBARIVisit.this, "Required field:স্বাস্থ্য সহকারী/ ভ্যাকসিনেটর গত পরিদর্শনে ই পি আই – এর কি বিষয় আলোচনা করেছেন (মা/ অভিভাবককে জিজ্ঞাসা) .");
                spnQBPVisitEPI.requestFocus();
                return;
            }*//*


            else if (!rdoQBNextDoss1.isChecked() & !rdoQBNextDoss2.isChecked() & secQBNextDoss.isShown()) {
                Connection.MessageBox(EPIBARIVisit.this, "Required field:শিশু/ মহিলা পরবর্তী ডোজ কবে ও কোথায় পাবেন তা অভিভাবক জানেন.");
                rdoQBNextDoss1.requestFocus();
                return;
            } else if (spnQBCNoSessiondoss.getSelectedItemPosition() == 0 & secQBCNoSessiondoss1.isShown()) {
                Connection.MessageBox(EPIBARIVisit.this, "Required field:শিশু গত সেশনে টিকা না পেয়ে থাকলে; কেন? .");
                spnQBCNoSessiondoss.requestFocus();
                return;
            } else if (spnQBWNoSessiondoss.getSelectedItemPosition() == 0 & secQBWNoSessiondoss1.isShown()) {
                Connection.MessageBox(EPIBARIVisit.this, "Required field:মহিলা গত সেশনে টিকা না পেয়ে থাকলে কেন?");
                spnQBWNoSessiondoss.requestFocus();
                return;
            } else if (!rdoQBChildCard1.isChecked() & !rdoQBChildCard2.isChecked() & secQBChildCard.isShown()) {
                Connection.MessageBox(EPIBARIVisit.this, "Required field:শিশু কার্ড আছে?");
                rdoQBChildCard1.requestFocus();
                return;
            } else if (!rdoQBWomenCard1.isChecked() & !rdoQBWomenCard2.isChecked() & secQBWomenCard.isShown()) {
                Connection.MessageBox(EPIBARIVisit.this, "Required field:মহিলা কার্ড আছে ?");
                rdoQBWomenCard1.requestFocus();
                return;
            }*/

            String SQL = "";

            if (!C.Existence("Select Dist,Upz,UN,Mouza,Vill,ProvCode,HHNo,VDate,StartTime,EndTime,UserId,EnDt,Upload from " + TableName + "  Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and ProvCode='" + g.getProvCode() + "' and HHNo='" + g.getHouseholdNo() + "' and VDate='" + Global.DateConvertYMD(dtpVdate.getText().toString()) + "'")) {
                SQL = "Insert into " + TableName + "(Dist,Upz,UN,Mouza,Vill,ProvCode,HHNo,VDate,StartTime,EndTime,UserId,EnDt,Upload)Values('" + g.getDistrict() + "','" + g.getUpazila() + "','" + g.getUnion() + "','" + g.getMouza() + "','" + g.getVillage() + "','" + g.getProvCode() + "','" + g.getHouseholdNo() + "','" + Global.DateConvertYMD(dtpVdate.getText().toString()) + "','" + StartTime + "','" + g.CurrentTime24() + "','" + g.getUserID() + "','" + Global.DateTimeNowYMDHMS() + "','2')";
                C.Save(SQL);
            }


            SQL = "Update " + TableName + " Set Upload='2',";
            //SQL+="Idno = '"+ txtIdno.getText().toString() +"',";
            SQL += "QBHHNo = '" + txtQBHHNo.getText().toString() + "',";
            // SQL+="QBHHHName = '"+ txtQBHHHName.getText().toString() +"',";
            SQL += "QBHEndDate = '" + Global.DateConvertYMD(dtpQBHEndDate.getText().toString()) + "',";
            SQL += "QBPVisitEPI1 = '" + (chkQBPVisitEPI1.isChecked() ? "1" : "2") + "',";
            SQL += "QBPVisitEPI2 = '" + (chkQBPVisitEPI2.isChecked() ? "1" : "2") + "',";
            SQL += "QBPVisitEPI3 = '" + (chkQBPVisitEPI3.isChecked() ? "1" : "2") + "',";
            SQL += "QBPVisitEPI4 = '" + (chkQBPVisitEPI4.isChecked() ? "1" : "2") + "',";
            SQL += "QBPVisitEPI5 = '" + (chkQBPVisitEPI5.isChecked() ? "1" : "2") + "',";

            // SQL+="QBPVisitEPI = '"+ (spnQBPVisitEPI.getSelectedItemPosition()==0?"":Global.Left(spnQBPVisitEPI.getSelectedItem().toString(),2)) +"',";
            RadioButton rbQBNextDoss = (RadioButton) findViewById(rdogrpQBNextDoss.getCheckedRadioButtonId());
            SQL += "QBNextDoss = '" + (rbQBNextDoss == null ? "" : (Global.Left(rbQBNextDoss.getText().toString(), 1))) + "',";
            SQL += "QB1stDoss1 = '" + (chkQB1stDoss1.isChecked() ? "1" : "2") + "',";
            SQL += "QB1stDoss2 = '" + (chkQB1stDoss2.isChecked() ? "1" : "2") + "',";
            SQL += "QB1stDoss3 = '" + (chkQB1stDoss3.isChecked() ? "1" : "2") + "',";
            SQL += "QB1stDoss4 = '" + (chkQB1stDoss4.isChecked() ? "1" : "2") + "',";
            SQL += "QB1stDoss5 = '" + (chkQB1stDoss5.isChecked() ? "1" : "2") + "',";
            RadioButton rbQBCNoSessiondossY = (RadioButton) findViewById(rdogrpQBCNoSessiondoss.getCheckedRadioButtonId());
            SQL += "QBCNoSessiondossY = '" + (rbQBCNoSessiondossY == null ? "" : (Global.Left(rbQBCNoSessiondossY.getText().toString(), 1))) + "',";
            SQL += "QBCNoSessiondoss = '" + (spnQBCNoSessiondoss.getSelectedItemPosition() == 0 ? "" : Global.Left(spnQBCNoSessiondoss.getSelectedItem().toString(), 2)) + "',";
            RadioButton rbQBWNoSessiondossY = (RadioButton) findViewById(rdogrpQBWNoSessiondoss.getCheckedRadioButtonId());
            SQL += "QBWNoSessiondossY = '" + (rbQBWNoSessiondossY == null ? "" : (Global.Left(rbQBWNoSessiondossY.getText().toString(), 1))) + "',";
            SQL += "QBWNoSessiondoss = '" + (spnQBWNoSessiondoss.getSelectedItemPosition() == 0 ? "" : Global.Left(spnQBWNoSessiondoss.getSelectedItem().toString(), 2)) + "',";
            // SQL+="QBVitmA = '"+ (chkQBVitmA.isChecked()?"1":"2") +"',";
            RadioButton rbQBVitmA = (RadioButton) findViewById(rdogrpQBVitmA.getCheckedRadioButtonId());
            SQL += "QBVitmA = '" + (rbQBVitmA == null ? "" : (Global.Left(rbQBVitmA.getText().toString(), 1))) + "',";
            RadioButton rbQBChildCard = (RadioButton) findViewById(rdogrpQBChildCard.getCheckedRadioButtonId());
            SQL += "QBChildCard = '" + (rbQBChildCard == null ? "" : (Global.Left(rbQBChildCard.getText().toString(), 1))) + "',";
            RadioButton rbQBWomenCard = (RadioButton) findViewById(rdogrpQBWomenCard.getCheckedRadioButtonId());
            SQL += "QBWomenCard = '" + (rbQBWomenCard == null ? "" : (Global.Left(rbQBWomenCard.getText().toString(), 1))) + "'";
            SQL += " Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and ProvCode='" + g.getProvCode() + "' and HHNo='" + g.getHouseholdNo() + "' and VDate='" + Global.DateConvertYMD(dtpVdate.getText().toString()) + "'";
            C.Save(SQL);
            Connection.MessageBox(EPIBARIVisit.this, "Saved Successfully");
            finish();
            Intent f = new Intent(getApplicationContext(), HouseholdIndex.class);
            startActivity(f);
        } catch (Exception e) {
            Connection.MessageBox(EPIBARIVisit.this, e.getMessage());
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
            return Integer.parseInt(C.ReturnSingleValue("Select count(*)total from EPIBariVisit Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and ProvCode='" + g.getProvCode() + "' and HHNo='" + g.getHouseholdNo()/*+"' and VDate='"+ Global.DateConvertYMD(dtpVdate.getText().toString())*/ + "'"));
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

                String SQL = "Select strftime('%d/%m/%Y', date(VDate)) as visitDate  from EPIBariVisit Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and ProvCode='" + g.getProvCode() + "' and HHNo='" + g.getHouseholdNo()/*+"' and VDate='"+ Global.DateConvertYMD(dtpVdate.getText().toString())*/ + "' order by VDate  desc";//date(imudate) asc

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
                            AlertDialog.Builder adb = new AlertDialog.Builder(EPIBARIVisit.this);


                            adb.setTitle("Close");
                            adb.setMessage("আপনি কি পরিদর্শন বাতিল করতে  চান[হ্যাঁ/না]?");
                            adb.setNegativeButton("না", null);
                            adb.setPositiveButton("হ্যাঁ", new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String visitDate = String.valueOf(vcode[1][position]);
                                    if ((Global.DateDifferenceDays(Global.DateNowDMY(), Global.DateConvertDMY(visitDate.toString())) > 3)) {
                                        Connection.MessageBox(EPIBARIVisit.this, "পরিদর্শনের  তথ্য ৩ দিন পর বাতিল করা প্রযোজ্য না");
                                        return;
                                    } else {
                                        String Item = String.valueOf(vcode[0][position]);
                                        String visit = String.valueOf(vcode[1][position]);
                                        String dwl = "DELETE FROM EPIBariVisit Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and ProvCode='" + g.getProvCode() + "' and HHNo='" + g.getHouseholdNo() + "' and strftime('%d/%m/%Y', date(VDate))='" + visit+ "'";
                                        //String dwl = "DELETE FROM EPIBariVisit Where subBlockId='" + Global.Left(spnEpiSubBlock.getSelectedItem().toString(), 2) + "' and schedulerId='" + Global.Left(spnEPICenterName.getSelectedItem().toString(), 2) + "' and providerId='" + Global.Left(spnProvider.getSelectedItem().toString(), 5) + "' and strftime('%d/%m/%Y', date(VDate))='" + visit+ "'";
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
                    Connection.MessageBox(EPIBARIVisit.this, ex.getMessage());
                }

            }
            return MyView;
        }

    }

    private void DataSearch(String VDate) {
        try {

            RadioButton rb;
            Cursor cur = C.ReadData("Select * from " + TableName + "  Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and ProvCode='" + g.getProvCode() + "' and HHNo='" + g.getHouseholdNo() + "' and VDate='" + Global.DateConvertYMD(VDate) + "'");
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                //txtIdno.setText(cur.getString(cur.getColumnIndex("Idno")));
                txtQBHHNo.setText(cur.getString(cur.getColumnIndex("QBHHNo")));
                // txtQBHHHName.setText(cur.getString(cur.getColumnIndex("QBHHHName")));
                dtpQBHEndDate.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("QBHEndDate"))));
                //spnQBPVisitEPI.setSelection(Global.SpinnerItemPosition(spnQBPVisitEPI, 2 ,cur.getString(cur.getColumnIndex("QBPVisitEPI"))));
                if (cur.getString(cur.getColumnIndex("QBPVisitEPI1")).equals("1")) {
                    chkQBPVisitEPI1.setChecked(true);
                } else if (cur.getString(cur.getColumnIndex("QBPVisitEPI1")).equals("2")) {
                    chkQBPVisitEPI1.setChecked(false);
                }
                if (cur.getString(cur.getColumnIndex("QBPVisitEPI2")).equals("1")) {
                    chkQBPVisitEPI2.setChecked(true);
                } else if (cur.getString(cur.getColumnIndex("QBPVisitEPI2")).equals("2")) {
                    chkQBPVisitEPI2.setChecked(false);
                }
                if (cur.getString(cur.getColumnIndex("QBPVisitEPI3")).equals("1")) {
                    chkQBPVisitEPI3.setChecked(true);
                } else if (cur.getString(cur.getColumnIndex("QBPVisitEPI3")).equals("2")) {
                    chkQBPVisitEPI3.setChecked(false);
                }
                if (cur.getString(cur.getColumnIndex("QBPVisitEPI4")).equals("1")) {
                    chkQBPVisitEPI4.setChecked(true);
                } else if (cur.getString(cur.getColumnIndex("QBPVisitEPI4")).equals("2")) {
                    chkQBPVisitEPI4.setChecked(false);
                }
                if (cur.getString(cur.getColumnIndex("QBPVisitEPI5")).equals("1")) {
                    chkQBPVisitEPI5.setChecked(true);
                } else if (cur.getString(cur.getColumnIndex("QBPVisitEPI5")).equals("2")) {
                    chkQBPVisitEPI5.setChecked(false);
                }
                for (int i = 0; i < rdogrpQBNextDoss.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQBNextDoss.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QBNextDoss"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }

                if (cur.getString(cur.getColumnIndex("QB1stDoss1")).equals("1")) {
                    chkQB1stDoss1.setChecked(true);
                } else if (cur.getString(cur.getColumnIndex("QB1stDoss1")).equals("2")) {
                    chkQB1stDoss1.setChecked(false);
                }
                if (cur.getString(cur.getColumnIndex("QB1stDoss2")).equals("1")) {
                    chkQB1stDoss2.setChecked(true);
                } else if (cur.getString(cur.getColumnIndex("QB1stDoss2")).equals("2")) {
                    chkQB1stDoss2.setChecked(false);
                }
                if (cur.getString(cur.getColumnIndex("QB1stDoss3")).equals("1")) {
                    chkQB1stDoss3.setChecked(true);
                } else if (cur.getString(cur.getColumnIndex("QB1stDoss3")).equals("2")) {
                    chkQB1stDoss3.setChecked(false);
                }
                if (cur.getString(cur.getColumnIndex("QB1stDoss4")).equals("1")) {
                    chkQB1stDoss4.setChecked(true);
                } else if (cur.getString(cur.getColumnIndex("QB1stDoss4")).equals("2")) {
                    chkQB1stDoss4.setChecked(false);
                }
                if (cur.getString(cur.getColumnIndex("QB1stDoss5")).equals("1")) {
                    chkQB1stDoss5.setChecked(true);
                } else if (cur.getString(cur.getColumnIndex("QB1stDoss5")).equals("2")) {
                    chkQB1stDoss5.setChecked(false);
                }
                spnQBCNoSessiondoss.setSelection(Global.SpinnerItemPosition(spnQBCNoSessiondoss, 2, cur.getString(cur.getColumnIndex("QBCNoSessiondoss"))));

                for (int i = 0; i < rdogrpQBCNoSessiondoss.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQBCNoSessiondoss.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QBCNoSessiondossY"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                spnQBWNoSessiondoss.setSelection(Global.SpinnerItemPosition(spnQBWNoSessiondoss, 2, cur.getString(cur.getColumnIndex("QBWNoSessiondoss"))));

                for (int i = 0; i < rdogrpQBWNoSessiondoss.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQBWNoSessiondoss.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QBWNoSessiondossY"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
           /*     if(cur.getString(cur.getColumnIndex("QBVitmA")).equals("1"))
                {
                    chkQBVitmA.setChecked(true);
                }
                else if(cur.getString(cur.getColumnIndex("QBVitmA")).equals("2"))
                {
                    chkQBVitmA.setChecked(false);
                }*/

                for (int i = 0; i < rdogrpQBVitmA.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQBVitmA.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QBVitmA"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQBChildCard.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQBChildCard.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QBChildCard"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQBWomenCard.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQBWomenCard.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("QBWomenCard"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(EPIBARIVisit.this, e.getMessage());
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


            dtpDate = (EditText) findViewById(R.id.dtpQBHEndDate);
            if (VariableID.equals("btnQBHEndDate")) {
                dtpDate = (EditText) findViewById(R.id.dtpQBHEndDate);
            } else if (VariableID.equals("btnVdate")) {
                dtpDate = (EditText) findViewById(R.id.dtpVdate);
            }

            dtpDate.setText(new StringBuilder()
                    .append(Global.Right("00" + mDay, 2)).append("/")
                    .append(Global.Right("00" + mMonth, 2)).append("/")
                    .append(mYear));
         /*   if (VariableID.equals("btnVdate")) {

                ((TextView)findViewById(R.id.txtbar)).setText(g.getDay(dtpDate.getText().toString()));
            }*/
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
                        Connection.MessageBox(EPIBARIVisit.this, "তারিখ আজকের তারিখের সমান অথবা কম হতে হবে।");
                        dtpVdate.setText(null);
                        //((TextView)findViewById(R.id.txtbar)).setText("");
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


            //tpTime.setText(new StringBuilder().append(Global.Right("00"+hour,2)).append(":").append(Global.Right("00"+minute,2)));

        }
    };
}
