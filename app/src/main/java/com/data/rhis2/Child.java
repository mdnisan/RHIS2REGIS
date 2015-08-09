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
import android.view.Window;
import android.view.WindowManager;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import Common.Connection;
import Common.Global;

/**
 * Created by user on 18/03/2015.
 */
public class Child extends Activity {
    boolean netwoekAvailable=false;
    Location currentLocation;
    double currentLatitude,currentLongitude;
    Location currentLocationNet;
    double currentLatitudeNet,currentLongitudeNet;


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
        AlertDialog.Builder adb = new AlertDialog.Builder(Child.this);
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
    TextView txtElcoNo;

    LinearLayout secName;
    TextView VlblName;
    TextView txtName;

    TextView txtAge;

    LinearLayout secBWeight;
    TextView VlblBWeight;
    EditText txtBWeight;

    LinearLayout secImbirth;
    TextView VlblImbirth;
    RadioGroup rdogrpImbirth;
    RadioButton rdoImbirthYes;
    RadioButton rdoImbirthNo;

    LinearLayout secDryWrap;
    TextView VlblVSDryWrap;
    RadioGroup rdogrpVSDryWrap;
    RadioButton rdoDryWrapYes;
    RadioButton rdoDryWrapNo;

    LinearLayout secCHX;
    TextView VlblCHX;
    RadioGroup rdogrpCHX;
    RadioButton rdoCHXYes;
    RadioButton rdoCHXNo;

    LinearLayout secBfeed1;
    TextView VlblBfeed1;
    RadioGroup rdogrpBfeed1;
    RadioButton rdoBfeed1Yes;
    RadioButton rdoBfeed1No;

    LinearLayout secBath3;
    TextView VlblBath3;
    RadioGroup rdogrpBath3;
    RadioButton rdoBath3Yes;
    RadioButton rdoBath3No;

    Button cmdSave;
    ImageButton cmdSavePNC;

    String StartTime;
    private String TableNamePNC;
    LinearLayout secPNCCh12;
    TextView VlblPNCCh1;
    CheckBox ChkPNCCh1;
    EditText dtpDOPNCCh1;
    ImageButton btnDOPNCCh1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.child);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();

            TableName = "newBorn";
            TableNamePNC = "pncServiceChild";

            seclblH=(LinearLayout)findViewById(R.id.seclblH);
            lblHlblH=(TextView) findViewById(R.id.lblHlblH);
            lblHealthID=(TextView) findViewById(R.id.lblHealthID);
            txtHealthID=(TextView) findViewById(R.id.txtHealthID);
            txtHealthID.setText(g.getHealthID());
            secSl=(LinearLayout)findViewById(R.id.secSl);
            VlblSNo=(TextView) findViewById(R.id.VlblSNo);
            txtSNo=(TextView) findViewById(R.id.txtSNo);
            txtElcoNo=(TextView) findViewById(R.id.txtElcoNo);
            txtAge =(TextView) findViewById(R.id.txtAge);

            secName=(LinearLayout)findViewById(R.id.secName);
            VlblName=(TextView) findViewById(R.id.VlblName);
            txtName=(TextView) findViewById(R.id.txtName);

            secBWeight=(LinearLayout)findViewById(R.id.secBWeight);
            VlblBWeight=(TextView) findViewById(R.id.VlblBWeight);
            txtBWeight=(EditText) findViewById(R.id.txtBWeight);

            secImbirth=(LinearLayout)findViewById(R.id.secImbirth);
            VlblImbirth=(TextView) findViewById(R.id.VlblImbirth);
            rdogrpImbirth = (RadioGroup) findViewById(R.id.rdogrpImbirth);
            rdoImbirthYes = (RadioButton) findViewById(R.id.rdoImbirthYes);
            rdoImbirthNo = (RadioButton) findViewById(R.id.rdoImbirthNo);

            secDryWrap=(LinearLayout)findViewById(R.id.secDryWrap);
            VlblVSDryWrap=(TextView) findViewById(R.id.VlblVSDryWrap);
            rdogrpVSDryWrap = (RadioGroup) findViewById(R.id.rdogrpVSDryWrap);
            rdoDryWrapYes = (RadioButton) findViewById(R.id.rdoDryWrapYes);
            rdoDryWrapNo = (RadioButton) findViewById(R.id.rdoDryWrapNo);


            secCHX=(LinearLayout)findViewById(R.id.secCHX);
            VlblCHX=(TextView) findViewById(R.id.VlblCHX);
            rdogrpCHX = (RadioGroup) findViewById(R.id.rdogrpCHX);
            rdoCHXYes = (RadioButton) findViewById(R.id.rdoCHXYes);
            rdoCHXNo = (RadioButton) findViewById(R.id.rdoCHXNo);

            secBfeed1=(LinearLayout)findViewById(R.id.secBfeed1);
            VlblBfeed1=(TextView) findViewById(R.id.VlblBfeed1);
            rdogrpBfeed1 = (RadioGroup) findViewById(R.id.rdogrpBfeed1);
            rdoBfeed1Yes = (RadioButton) findViewById(R.id.rdoBfeed1Yes);
            rdoBfeed1No = (RadioButton) findViewById(R.id.rdoBfeed1No);

            secBath3=(LinearLayout)findViewById(R.id.secBath3);
            VlblBath3=(TextView) findViewById(R.id.VlblBath3);
            rdogrpBath3 = (RadioGroup) findViewById(R.id.rdogrpBath3);
            rdoBath3Yes = (RadioButton) findViewById(R.id.rdoBath3Yes);
            rdoBath3No = (RadioButton) findViewById(R.id.rdoBath3No);
            DataSearch(g.getHealthID());
            DataSearch(g.getHealthID(), g.getChildNo(), g.getPregNo());


            cmdSave = (Button) findViewById(R.id.cmdSave);
            cmdSavePNC = (ImageButton) findViewById(R.id.cmdSavePNC);

            cmdSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    DataSave();
                }});


            //PNC Section
            secPNCCh12=(LinearLayout)findViewById(R.id.secPNCCh12);
            VlblPNCCh1=(TextView) findViewById(R.id.VlblPNCCh1);

            dtpDOPNCCh1=(EditText) findViewById(R.id.dtpDOPNCCh1);
            btnDOPNCCh1 = (ImageButton) findViewById(R.id.btnDOPNCCh1);
            dtpDOPNCCh1.setText( "" );
           // btnDOPNCCh1.setEnabled( false );

            btnDOPNCCh1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btnDOPNCCh1";
                    showDialog(DATE_DIALOG);
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
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            //PNC Section End
        }
        catch(Exception  e)
        {
            Connection.MessageBox(Child.this, e.getMessage());
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

    private int Get37Weeks()
    {
        String sq = String.format("Select outcomeDate from delivery where healthId = '%s' and pregNo = '%s'", g.getHealthID(), PGNNo());
        if (C.Existence(sq))
        {
            String deldate = C.ReturnSingleValue("Select outcomeDate from delivery where healthId = '" + g.getHealthID() + "' AND pregNo = '" + PGNNo() + "'");

            return Global.DateDifferenceDays(Global.DateNowDMY(), Global.DateConvertDMY(deldate));
         }
        return 0;
    }
    /*private String ChildSlNo()
    {
        String SQL = "";
        SQL = "Select (ifnull(max(cast(ChildSl as int)),0)+1)MaxChildSl from Child";
        SQL += " where";
        SQL += " dist='" + g.getDistrict() + "' and upz='" + g.getUpazila() + "' and un='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and SNo='" + txtSNo.getText() + "'";
        String ChildSlNo = Global.Right(("00"+ C.ReturnSingleValue(SQL)),2);
        return ChildSlNo;
    }

    private String PGNNo()
    {
        String SQL = "";
        SQL = "Select PGN  from PregWomen";
        SQL += " where";
        SQL += " dist='" + g.getDistrict() + "' and upz='" + g.getUpazila() + "' and un='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and SNo='" + txtSNo.getText() + "'";

        String PGNNo = Global.Right(("00"+ C.ReturnSingleValue(SQL)),2);

        return PGNNo;
    }*/

    private void DataSave()
    {
        try
        {
            if(txtBWeight.getText().length()==0)
            {
                Connection.MessageBox(Child.this, "জন্মের সময় ওজন (কেজি) কত ছিল।");
                txtBWeight.requestFocus();
                return;
            }
            else if(!rdoImbirthYes.isChecked() & !rdoImbirthNo.isChecked())
            {
                Connection.MessageBox(Child.this, "অপরিনত জন্ম (৩৭ সপ্তাহের পূর্বে )  সিলেক্ট করুন।");
                rdoImbirthYes.requestFocus();
                return;
            }
            else if (!rdoDryWrapYes.isChecked() & !rdoDryWrapNo.isChecked())
            {
                Connection.MessageBox(Child.this, "জন্মের পর পরই শুষ্ক ও পরিস্কার কাপড় দিয়ে মুছানো ও মোড়ানো হয়েছে কি না  সিলেক্ট করুন।");
                rdoImbirthYes.requestFocus();
                return;
            }
            else if (!rdoCHXYes.isChecked() & !rdoCHXNo.isChecked())
            {
                Connection.MessageBox(Child.this, "নাড়ি কাটার পর ৭.১% ক্লোরহেক্সিডিন ব্যবহার করা হয়েছে কি না  সিলেক্ট করুন।");
                rdoImbirthYes.requestFocus();
                return;
            }
            else if (!rdoBfeed1Yes.isChecked() & !rdoBfeed1No.isChecked())
            {
                Connection.MessageBox(Child.this, "জন্মের ১ ঘন্টার মধ্যে বুকের দুধ খাওয়ানো হয়েছে কি না সিলেক্ট করুন।");
                rdoImbirthYes.requestFocus();
                return;
            }
            else if (!rdoBath3Yes.isChecked() & !rdoBath3No.isChecked())
            {
                Connection.MessageBox(Child.this, "জন্মের প্রথম ৩ দিন গোসল থেকে বিরত রাখা হয়েছে কি না  সিলেক্ট করুন।");
                rdoImbirthYes.requestFocus();
                return;
            }

            String SQL = "";
            String pregNo=g.getPregNo();
            String childNo=g.getChildNo();
            /*if(!C.Existence("Select Dist,Upz,UN,Mouza,Vill,HHNo,SNo from " + TableName + "  Where healthid='"+ txtHealthID.getText().toString() +"'  and pregNo = '"+ pregNo +"' and childNo = '" + childNo +"'"))
            {
                SQL = "Insert into " + TableName + "(Dist,Upz,UN,Mouza,Vill,ProvType,ProvCode,HHNo,SNo,PGN,ChildSl,EnDt,Upload,UploadDT)Values('"+ g.getDistrict() +"','"+ g.getUpazila() +"','"+ g.getUnion() +"','"+ g.getMouza() +"','"+ g.getVillage() +"','"+ g.getProvType() +"','"+ g.getProvCode() +"','"+ g.getHouseholdNo() +"','"+ txtSNo.getText() +"','"+ PGNNo() +"','"+ ChildSlNo +"','"+ Global.DateTimeNowYMDHMS() +"','2','')";
                C.Save(SQL);
            }*/

            SQL = "Update " + TableName + " Set ";
            SQL+="providerId = '"+ g.getUserID() +"',";
           /* SQL+="childHealthId = '',";
            SQL+="birthStatus = '',";
            SQL+="outcomePlace = '',";
            SQL+="outcomeDate = '',";
            SQL+="outcomeTime = '',";
            SQL+="outcomeType = '',";*/
            SQL+="birthWeight = '"+ txtBWeight.getText().toString() +"',";
            SQL+="immatureBirth = '"+ ((rdoImbirthYes.isChecked()?"1":"2")) +"',";
            SQL+="dryingAfterBirth = '"+ ((rdoDryWrapYes.isChecked()?"1":"2")) +"',";
            SQL+="chlorehexidin = '"+ ((rdoCHXYes.isChecked()?"1":"2")) +"',";
            //SQL+="skinTouch = '',";
            SQL+="breastFeed = '"+ ((rdoBfeed1Yes.isChecked()?"1":"2")) +"',";
            SQL+="bathThreeDays = '"+ ((rdoBath3Yes.isChecked()?"1":"2")) +"',";
            SQL+="systemEntryDate = '"+ Global.DateTimeNowYMDHMS() +"',";
            SQL+="modifyDate = '"+ Global.DateTimeNowYMDHMS() +"'";
            SQL+=" Where HealthID='"+ txtHealthID.getText().toString() +"' and pregNo='"+ pregNo +"' and childNo='"+ childNo +"'";
            C.Save(SQL);
//Update newBorn Set providerId = '68974',childHealthId = '',birthStatus = '',outcomePlace = '',outcomeDate = '',outcomeTime = '',outcomeType = '',dryingAfterBirth = '2',chlorehexidin = '2',skinTouch = '',breastFeed = '2',bathThreeDays = '2',systemEntryDate = '2015-07-13 07:15:27'modifyDate = '2015-07-13 07:15:32' Where HealthID='0000266096' and pregNo='1' and childNo='1'
/*healthId
pregNo
childNo
providerId
childHealthId
birthStatus
outcomePlace
outcomeDate
outcomeTime
outcomeType
dryingAfterBirth
chlorehexidin
skinTouch
breastFeed
bathThreeDays
systemEntryDate
modifyDate
*/

            /*String SQL = "";
            String ChildSlNo=ChildSlNo();
            if(!C.Existence("Select Dist,Upz,UN,Mouza,Vill,HHNo,SNo from " + TableName + "  Where Dist='"+ g.getDistrict() +"' and Upz='"+ g.getUpazila() +"' and UN='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and Vill='"+ g.getVillage() +"'  and ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"' and  HHNo='"+ g.getHouseholdNo() +"' and SNo='"+ txtSNo.getText().toString() +"' and PGN = '"+ PGNNo() +"' and ChildSl = '" + ChildSlNo +"'"))
            {
                SQL = "Insert into " + TableName + "(Dist,Upz,UN,Mouza,Vill,ProvType,ProvCode,HHNo,SNo,PGN,ChildSl,EnDt,Upload,UploadDT)Values('"+ g.getDistrict() +"','"+ g.getUpazila() +"','"+ g.getUnion() +"','"+ g.getMouza() +"','"+ g.getVillage() +"','"+ g.getProvType() +"','"+ g.getProvCode() +"','"+ g.getHouseholdNo() +"','"+ txtSNo.getText() +"','"+ PGNNo() +"','"+ ChildSlNo +"','"+ Global.DateTimeNowYMDHMS() +"','2','')";
                C.Save(SQL);
            }

            SQL = "Update " + TableName + " Set ";
            SQL+="BWeight = '"+ txtBWeight.getText().toString() +"',";
            SQL+="Imbirth = '"+ ((rdoImbirthYes.isChecked()?"1":"2")) +"',";
            SQL+="DryWrap = '"+ ((rdoDryWrapYes.isChecked()?"1":"2")) +"',";
            SQL+="CHX = '"+ ((rdoCHXYes.isChecked()?"1":"2")) +"',";
            SQL+="Bfeed1 = '"+ ((rdoBfeed1Yes.isChecked()?"1":"2")) +"',";
            SQL+="Bath3 = '"+ ((rdoBath3Yes.isChecked()?"1":"2")) +"',";
            SQL+="RegDT = '"+ g.DateNowYMD() +"'";
            SQL+="  Where Dist='"+ g.getDistrict() +"' and Upz='"+ g.getUpazila() +"' and UN='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and Vill='"+ g.getVillage() +"' and HHNo='"+ g.getHouseholdNo() +"' and SNo='"+ txtSNo.getText().toString() +"' and PGN = '"+ PGNNo() +"' and ChildSl = '" + ChildSlNo +"'";
            C.Save(SQL);*/

            Connection.MessageBox(Child.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
           // finish();

               /* Intent f2 = new Intent(getApplicationContext(),Deliv.class);
                startActivity(f2);*/

        }
        catch(Exception  e)
        {
            Connection.MessageBox(Child.this, e.getMessage());
            return;
        }
    }
    public void DataSearch(String HealthId)
    {
        try
        {
            String SQL = "";
            SQL = "Select Dist, Upz, UN, Mouza, Vill, HHNo, SNo as SNo, ifnull(HealthID,'') as HealthID, ifnull(NameEng,'') as NameEng," +
                    "ifnull(NameBang,'') as NameBang, ifnull(Rth,'') as Rth, ifnull(HaveNID,'') as HaveNID, ifnull(NID,'') as NID, ifnull(NIDStatus,'') as NIDStatus, ifnull(HaveBR,'') as HaveBR, ifnull(BRID,'') as BRID, ifnull(BRIDStatus,'') as BRIDStatus, ifnull(MobileNo1,'') as MobileNo1," +
                    "ifnull(MobileNo2,'') as MobileNo2, ifnull(DOB,'') as DOB, ifnull(Age,'') as Age, ifnull(DOBSource,'') as DOBSource, ifnull(BPlace,'') as BPlace, ifnull(FNo,'') as FNo, ifnull(Father,'') as Father, ifnull(MNo,'') as MNo, ifnull(Mother,'') as Mother," +
                    "ifnull(Sex,'') as Sex, ifnull(MS,'') as MS, ifnull(SPNO1,'') as SPNO1,ifnull(SPNO2,'') as SPNO2,ifnull(SPNO3,'') " +
                    "as SPNO3,ifnull(SPNO4,'') as SPNO4, ifnull(ELCONo,'') as ELCONo, ifnull(ELCODontKnow,'') as ELCODontKnow, " +
                    "ifnull(EDU,'') as EDU, ifnull(Rel,'') as Rel, ifnull(Nationality,'') as Nationality, ifnull(OCP,'') as OCP," +
                    "(select NameEng  from member where  HealthId='" + g.getHealthID() + "' and SNo=(select  SPNO1  from member  Where   HealthId='" + g.getHealthID() + "'" + " ))as HusName " +
                    " from Member Where  HealthId='" + g.getHealthID() + "'";
            /*SQL = "Select Dist, Upz, UN, Mouza, Vill, HHNo, SNo as SNo, ifnull(HealthID,'') as HealthID, ifnull(NameEng,'') as NameEng,";
            SQL += " ifnull(NameBang,'') as NameBang, ifnull(Rth,'') as Rth, ifnull(HaveNID,'') as HaveNID, ifnull(NID,'') as NID, ifnull(NIDStatus,'') as NIDStatus, ifnull(HaveBR,'') as HaveBR, ifnull(BRID,'') as BRID, ifnull(BRIDStatus,'') as BRIDStatus, ifnull(MobileNo1,'') as MobileNo1,";
            SQL += " ifnull(MobileNo2,'') as MobileNo2, ifnull(DOB,'') as DOB, ifnull(Age,'') as Age, ifnull(DOBSource,'') as DOBSource, ifnull(BPlace,'') as BPlace, ifnull(FNo,'') as FNo, ifnull(Father,'') as Father, ifnull(MNo,'') as MNo, ifnull(Mother,'') as Mother,";
            SQL += " ifnull(Sex,'') as Sex, ifnull(MS,'') as MS, ifnull(SPNO1,'') as SPNO1,ifnull(SPNO2,'') as SPNO2,ifnull(SPNO3,'') as SPNO3,ifnull(SPNO4,'') as SPNO4, ifnull(ELCONo,'') as ELCONo, ifnull(ELCODontKnow,'') as ELCODontKnow, ifnull(EDU,'') as EDU, ifnull(Rel,'') as Rel, ifnull(Nationality,'') as Nationality, ifnull(OCP,'') as OCP";
            SQL += " from Member Where Dist='"+ Dist +"' and Upz='"+ Upz +"' and UN='"+ UN +"' and Mouza='"+ Mouza +"' and Vill='"+ Vill +"' and HHNo='"+ HHNo +"' and SNo='"+ SNo +"'";*/
            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();
            while(!cur.isAfterLast())
            {
                txtHealthID.setText(cur.getString(cur.getColumnIndex("HealthID")));
                txtSNo.setText(cur.getString(cur.getColumnIndex("SNo")));
                txtName.setText(cur.getString(cur.getColumnIndex("NameEng")));
                txtAge.setText(cur.getString(cur.getColumnIndex("Age")));

                cur.moveToNext();
            }
            cur.close();
        }
        catch(Exception  e)
        {
            Connection.MessageBox(Child.this, e.getMessage());
            return;
        }
    }

    public void DataSearch(String HealthId, String ChildId, String PregNo)
    {
        try
        {
            String SQL = "";
            SQL = "Select healthId, pregNo, childNo, providerId, childHealthId, birthStatus, gender,  outcomePlace, outcomeDate,  outcomeTime,  outcomeType," +
                    "birthWeight,  immatureBirth, dryingAfterBirth, resassitation, stimulation,  bagNMask, chlorehexidin, skinTouch, breastFeed, bathThreeDays," +
                    "refer, referReason,  referCenterName,  systemEntryDate,  Upload, UploadDT, modifyDate from newBorn where  HealthId='" + HealthId +"' AND Pregno='"
                    + PregNo +"' AND ChildNo='" + ChildId +"'";


            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();
            while(!cur.isAfterLast())
            {
                txtBWeight.setText(cur.getString(cur.getColumnIndex("birthWeight")));

                if(Get37Weeks()<1110)
                {
                    rdoImbirthYes.setChecked(true);
                }
                else
                {
                    rdoImbirthNo.setChecked(true);
                }
                if(cur.getString(cur.getColumnIndex("immatureBirth"))!=null)
                {
                    if(cur.getString(cur.getColumnIndex("immatureBirth")).equalsIgnoreCase("1"))
                    {
                        rdoImbirthYes.setChecked(true);
                    }
                    else if(cur.getString(cur.getColumnIndex("immatureBirth")).equalsIgnoreCase("2"))
                    {
                        rdoImbirthNo.setChecked(true);
                    }
                }

                if(cur.getString(cur.getColumnIndex("dryingAfterBirth"))!=null)
                {
                    if(cur.getString(cur.getColumnIndex("dryingAfterBirth")).equalsIgnoreCase("1"))
                    {
                        rdoDryWrapYes.setChecked(true);
                    }
                    else if(cur.getString(cur.getColumnIndex("dryingAfterBirth")).equalsIgnoreCase("2"))
                    {
                        rdoDryWrapNo.setChecked(true);
                    }
                }

                if(cur.getString(cur.getColumnIndex("chlorehexidin"))!=null)
                {
                    if(cur.getString(cur.getColumnIndex("chlorehexidin")).equalsIgnoreCase("1"))
                    {
                        rdoCHXYes.setChecked(true);
                    }
                    else if(cur.getString(cur.getColumnIndex("chlorehexidin")).equalsIgnoreCase("2"))
                    {
                        rdoCHXNo.setChecked(true);
                    }
                }





                if(cur.getString(cur.getColumnIndex("breastFeed"))!=null)
                {
                    if(cur.getString(cur.getColumnIndex("breastFeed")).equalsIgnoreCase("1"))
                    {
                        rdoBfeed1Yes.setChecked(true);
                    }
                    else if(cur.getString(cur.getColumnIndex("breastFeed")).equalsIgnoreCase("2"))
                    {
                        rdoBfeed1No.setChecked(true);
                    }
                }


                if(cur.getString(cur.getColumnIndex("bathThreeDays"))!=null)
                {
                    if(cur.getString(cur.getColumnIndex("bathThreeDays")).equalsIgnoreCase("1"))
                    {
                        rdoBath3Yes.setChecked(true);
                    }
                    else if(cur.getString(cur.getColumnIndex("bathThreeDays")).equalsIgnoreCase("2"))
                    {
                        rdoBath3No.setChecked(true);
                    }
                }


                cur.moveToNext();
            }
            cur.close();
        }
        catch(Exception  e)
        {
            Connection.MessageBox(Child.this, e.getMessage());
            return;
        }
    }


    private boolean validateChildPNCDate()
    {
        String sq = String.format("Select outcomeDate from newBorn where healthId = '%s' and pregNo = '%s' and childNo = '%s'", g.getHealthID(), PGNNo(), g.getChildNo());
        if (C.Existence(sq))
        {
            String outcomeDate = C.ReturnSingleValue("Select LMP from PregWomen where healthId = '" + g.getHealthID() + "' AND pregNo = '" + PGNNo() + "'");

            if(/*Global.DateDifferenceDays(Global.DateConvertDMY(outcomeDate),dtpDOPNCCh1.getText().toString())>=1
                    ||*/ (Global.DateDifferenceDays(Global.DateNowDMY(),dtpDOPNCCh1.getText().toString())<=1))
            {
                return true;
            }


        }
        return false;
    }

    private int GetDifferencedateofBirth()
    {

        String sq = String.format("Select outcomeDate from newBorn where healthId = '%s' and pregNo = '%s' and childNo = '%s'", g.getHealthID(), PGNNo(), g.getChildNo());
        if (C.Existence(sq))
        {
            String outcomeDate = C.ReturnSingleValue(String.format("Select outcomeDate from newBorn where healthId = '%s' and pregNo = '%s' and childNo = '%s'", g.getHealthID(), PGNNo(), g.getChildNo()));


            return Global.DateDifferenceDays(dtpDOPNCCh1.getText().toString(),Global.DateConvertDMY(outcomeDate));
        }
        return 0;

    }


    private boolean GetDifferencedateWithSystemDate()
    {

      if(Global.DateDifferenceDays(dtpDOPNCCh1.getText().toString(), Global.DateNowDMY())<=1)
        {
            return true;
        }

        return false;
    }

    private void DataSavePNC()
    {
        try
        {
                if(dtpDOPNCCh1.getText().toString().length()==0 & dtpDOPNCCh1.isShown())
                {
                    Connection.MessageBox(Child.this, "পি এন সি  এর তারিখ কত লিখুন।");
                    dtpDOPNCCh1.requestFocus();
                    return;
                }

            if(GetDifferencedateofBirth()<0)
            {
                Connection.MessageBox(Child.this, "পি এন সি  এর ভিসিট ডেট জন্ম তারিখ থেকে বেশি হবে");
                return;
            }
           if(GetDifferencedateWithSystemDate())
            {
                Connection.MessageBox(Child.this, "পি এন সি  এর ভিসিট ডেট আজকের তারিখ থেকে বেশি হবে না");
                return;
            }

            String SQL = "";
            SQL = "select healthId, pregNo, childNo,serviceId, providerId, visitDate, serviceSource, systemEntryDate, Upload, UploadDT, modifyDate FROM pncServiceChild ";
            SQL += " WHERE healthId = '"+ g.getHealthID() +"' AND pregNo ='"+ g.getPregNo() +"' AND childNo='"+ g.getChildNo() +"' and serviceId='"+ GetServiceId() +"'  ORDER BY visitDate";


            String SQ = "select visitDate FROM pncServiceMother ";
            SQ += " WHERE healthId = '"+ g.getHealthID() +"' AND pregNo ='"+ g.getPregNo() +"' and visitDate='"+ Global.DateConvertYMD(dtpDOPNCCh1.getText().toString()) +"'";


            if(!C.Existence(SQL))// && !C.Existence(SQ))
            {
                SQL = "Insert into " + TableNamePNC + "(healthId, pregNo, childNo, serviceId, providerId, visitDate, serviceSource, systemEntryDate, Upload, UploadDT, modifyDate)Values('"+
                        g.getHealthID() +"','"+ g.getPregNo() +"','"+ g.getChildNo() +"','"+ GetServiceId() +"','"+ g.getProvCode() +"','"+ Global.DateConvertYMD(dtpDOPNCCh1.getText().toString()) +"','"+  "G','"+  Global.DateTimeNowYMDHMS() +"','"+ "2','"+ "','"+"')";
                C.Save(SQL);
                Connection.MessageBox(Child.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
            }
           /* else
            {
                Connection.MessageBox(Child.this, "পি এন সি  এর ভিসিট দেয়া আছে");
                return;
            }*/
           // C.Save(SQL);



        }
        catch(Exception  e)
        {
            Connection.MessageBox(Child.this, e.getMessage());
            return;
        }
    }
    private String GetServiceId()
    {
        String SQL = "";

        SQL = "select '0'||(ifnull(max(cast(serviceId as int)),0))MaxserviceId from pncServiceChild";
        SQL += " WHERE healthId = '"+ g.getHealthID() +"' AND pregNo ='"+ g.getPregNo() +"' AND childNo='"+ g.getChildNo() +"'";

        String tempserviceID = C.ReturnSingleValue(SQL);


        String serviceID = String.valueOf((Integer.parseInt(tempserviceID) + 1));

        if (serviceID.equalsIgnoreCase("1")) {
            return String.valueOf(g.getHealthID() + g.getPregNo() + serviceID);
        } else {
            return String.valueOf(serviceID);
        }
    }
    private void PNCVisitSearch(String serviceId)
    {
        try
        {
            String SQL = "";
            SQL = "select healthId, pregNo, childNo,serviceId, providerId, visitDate, serviceSource, systemEntryDate, Upload, UploadDT, modifyDate FROM pncServiceChild ORDER BY visitDate";
            SQL += " WHERE healthId = '"+ g.getHealthID() +"' AND pregNo ='"+ g.getPregNo() +"' AND childNo='"+ g.getChildNo() +"' and serviceId='"+ serviceId +"'";
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
            Connection.MessageBox(Child.this, e.getMessage());
            return;
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
            mYear = year; mMonth = monthOfYear+1; mDay = dayOfMonth;
            EditText dtpDate;

            dtpDate = (EditText)findViewById(R.id.dtpDOM);

            if(VariableID.equals("btnDOPNCCh1"))
            {
                dtpDate = (EditText)findViewById(R.id.dtpDOPNCCh1);
            }


            dtpDate.setText(new StringBuilder()
                    .append(Global.Right("00"+mDay,2)).append("/")
                    .append(Global.Right("00"+mMonth,2)).append("/")
                    .append(mYear));
        }
    };
    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
            hour = selectedHour; minute = selectedMinute;
            EditText tpTime;

        }
    };

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

            return Integer.parseInt(C.ReturnSingleValue("Select count(*)total from pncServiceChild where healthid='" + g.getHealthID() + "' AND pregNo = '" + g.getPregNo() + "'"));
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
                String SQL = "Select healthId, pregNo,childNo, serviceId, visitDate from pncServiceChild where healthid='" + g.getHealthID() + "' AND pregNo = '" + g.getPregNo() + "'" + " AND ChildNo = '" + g.getChildNo() + "'"+ " order by cast(visitDate as DATE) asc";

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
                    Connection.MessageBox(Child.this, ex.getMessage());
                }

            }
            return MyView;
        }

    }

    private void DisplaySelectedPNCInfo(String ServiceId) {

        String SQL = "Select visitDate from pncServiceChild where serviceId = '" + ServiceId + "'";



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
            Connection.MessageBox(Child.this, ex.getMessage());
        }
            }





}