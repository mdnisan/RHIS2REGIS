package com.data.rhis2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
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
public class PNC_Mother extends Activity {
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
        AlertDialog.Builder adb = new AlertDialog.Builder(PNC_Mother.this);
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

    LinearLayout secHusName;
    TextView VlblHusName;
    TextView txtHusName;

    LinearLayout secAge;
    TextView VlblAge;
    TextView txtAge;

    LinearLayout secPNC12;
    TextView VlblPNC1;
    CheckBox ChkPNC1;
    EditText dtpDOPNC1;
    ImageButton btnDOPNC1;

    TextView VlblPNC2;
    CheckBox ChkPNC2;
    EditText dtpDOPNC2;
    ImageButton btnDOPNC2;

    LinearLayout secPNC34;
    TextView VlblPNC3;
    CheckBox ChkPNC3;
    EditText dtpDOPNC3;
    ImageButton btnDOPNC3;

    TextView VlblPNC4;
    CheckBox ChkPNC4;
    EditText dtpDOPNC4;
    ImageButton btnDOPNC4;

    LinearLayout secPNC5;
    TextView VlblPNC5;
    CheckBox ChkPNC5;
    EditText dtpDOPNC5;
    ImageButton btnDOPNC5;

    Button cmdSave;

    String StartTime;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.pnc_mother);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();

            TableName = "PNCMother";

            seclblH=(LinearLayout)findViewById(R.id.seclblH);
            lblHlblH=(TextView) findViewById(R.id.lblHlblH);
            lblHealthID=(TextView) findViewById(R.id.lblHealthID);
            txtHealthID=(TextView) findViewById(R.id.txtHealthID);

            secSl=(LinearLayout)findViewById(R.id.secSl);
            VlblSNo=(TextView) findViewById(R.id.VlblSNo);
            txtSNo=(TextView) findViewById(R.id.txtSNo);
            txtElcoNo=(TextView) findViewById(R.id.txtElcoNo);
            txtAge =(TextView) findViewById(R.id.txtAge);

            secName=(LinearLayout)findViewById(R.id.secName);
            VlblName=(TextView) findViewById(R.id.VlblName);
            txtName=(TextView) findViewById(R.id.txtName);

            secHusName=(LinearLayout)findViewById(R.id.secHusName);
            VlblHusName=(TextView) findViewById(R.id.VlblHusName);
            txtHusName=(TextView) findViewById(R.id.txtHusName);

            secPNC12=(LinearLayout)findViewById(R.id.secPNC12);
            VlblPNC1=(TextView) findViewById(R.id.VlblPNC1);
            ChkPNC1=(CheckBox) findViewById(R.id.ChkPNC1);
            dtpDOPNC1=(EditText) findViewById(R.id.dtpDOPNC1);
            btnDOPNC1 = (ImageButton) findViewById(R.id.btnDOPNC1);
            dtpDOPNC1.setText( "" );
            btnDOPNC1.setEnabled( false );
            ChkPNC1.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    if(((CheckBox) v).isChecked())
                    {
                        btnDOPNC1.setEnabled( true );
                    }
                    else
                    {
                        dtpDOPNC1.setText( "" );
                        btnDOPNC1.setEnabled( false );
                    }
                }
            });
            VlblPNC2=(TextView) findViewById(R.id.VlblPNC2);
            ChkPNC2=(CheckBox) findViewById(R.id.ChkPNC2);
            dtpDOPNC2=(EditText) findViewById(R.id.dtpDOPNC2);
            btnDOPNC2 = (ImageButton) findViewById(R.id.btnDOPNC2);
            dtpDOPNC2.setText( "" );
            btnDOPNC2.setEnabled( false );
            ChkPNC2.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    if(((CheckBox) v).isChecked())
                    {
                        btnDOPNC2.setEnabled( true );
                    }
                    else
                    {
                        dtpDOPNC2.setText( "" );
                        btnDOPNC2.setEnabled( false );
                    }
                }
            });
            VlblPNC3=(TextView) findViewById(R.id.VlblPNC3);
            ChkPNC3=(CheckBox) findViewById(R.id.ChkPNC3);
            dtpDOPNC3=(EditText) findViewById(R.id.dtpDOPNC3);
            btnDOPNC3 = (ImageButton) findViewById(R.id.btnDOPNC3);
            dtpDOPNC3.setText( "" );
            btnDOPNC3.setEnabled( false );
            ChkPNC3.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    if(((CheckBox) v).isChecked())
                    {
                        btnDOPNC3.setEnabled( true );
                    }
                    else
                    {
                        dtpDOPNC3.setText( "" );
                        btnDOPNC3.setEnabled( false );
                    }
                }
            });
            VlblPNC4=(TextView) findViewById(R.id.VlblPNC4);
            ChkPNC4=(CheckBox) findViewById(R.id.ChkPNC4);
            dtpDOPNC4=(EditText) findViewById(R.id.dtpDOPNC4);
            btnDOPNC4 = (ImageButton) findViewById(R.id.btnDOPNC4);
            dtpDOPNC4.setText( "" );
            btnDOPNC4.setEnabled( false );
            ChkPNC4.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    if(((CheckBox) v).isChecked())
                    {
                        btnDOPNC4.setEnabled( true );
                    }
                    else
                    {
                        dtpDOPNC4.setText( "" );
                        btnDOPNC4.setEnabled( false );
                    }
                }
            });

            DataSearch(g.getDistrict(), g.getUpazila(), g.getUnion(), g.getMouza(), g.getVillage(), g.getHouseholdNo(), g.getSerialNo() );
            ELCONoSearch(g.getDistrict(), g.getUpazila(), g.getUnion(), g.getMouza(), g.getVillage(), g.getHouseholdNo(), g.getSerialNo() );
            PNCVisitSearch(g.getDistrict(), g.getUpazila(), g.getUnion(), g.getMouza(), g.getVillage(), g.getHouseholdNo(), g.getSerialNo() );
            btnDOPNC1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) { VariableID = "btnDOPNC1"; showDialog(DATE_DIALOG); }});

            btnDOPNC2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) { VariableID = "btnDOPNC2"; showDialog(DATE_DIALOG); }});

            btnDOPNC3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) { VariableID = "btnDOPNC3"; showDialog(DATE_DIALOG); }});

            btnDOPNC4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) { VariableID = "btnDOPNC4"; showDialog(DATE_DIALOG); }});

            cmdSave = (Button) findViewById(R.id.cmdSave);
            cmdSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    DataSave();
                }});
        }
        catch(Exception  e)
        {
            Connection.MessageBox(PNC_Mother.this, e.getMessage());
            return;
        }
    }

    private String PGNNo()
    {
        String SQL = "";
        SQL = "Select PGN  from PregWomen";
        SQL += " where";
        SQL += " dist='" + g.getDistrict() + "' and upz='" + g.getUpazila() + "' and un='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and SNo='" + txtSNo.getText() + "'";

        String PGNNo = Global.Right(("00"+ C.ReturnSingleValue(SQL)),2);

        return PGNNo;
    }

    private void DataSave()
    {
        try
        {
            if(ChkPNC1.isChecked() & ChkPNC1.isShown())
            {
                if(dtpDOPNC1.getText().toString().length()==0 & dtpDOPNC1.isShown())
                {
                    Connection.MessageBox(PNC_Mother.this, "পি এন সি  ১ এর তারিখ কত লিখুন।");
                    dtpDOPNC1.requestFocus();
                    return;
                }
            }

            if(ChkPNC2.isChecked() & ChkPNC2.isShown())
            {
                if(dtpDOPNC2.getText().toString().length()==0  & dtpDOPNC2.isShown())
                {
                    Connection.MessageBox(PNC_Mother.this, "পি এন সি  ২ এর তারিখ কত লিখুন।");
                    dtpDOPNC2.requestFocus();
                    return;
                }
            }
            if(ChkPNC3.isChecked() & ChkPNC3.isShown())
            {
                if(dtpDOPNC3.getText().toString().length()==0  & dtpDOPNC3.isShown())
                {
                    Connection.MessageBox(PNC_Mother.this, "পি এন সি  ৩ এর তারিখ কত লিখুন।");
                    dtpDOPNC3.requestFocus();
                    return;
                }
            }

            if(ChkPNC4.isChecked() & ChkPNC4.isShown())
            {
                if(dtpDOPNC4.getText().toString().length()==0 & dtpDOPNC4.isShown())
                {
                    Connection.MessageBox(PNC_Mother.this, "পি এন সি  ৪ এর তারিখ কত লিখুন।");
                    dtpDOPNC4.requestFocus();
                    return;
                }
            }

            String SQL = "";
            if(!C.Existence("Select Dist,Upz,UN,Mouza,Vill,HHNo,SNo from " + TableName + "  Where Dist='"+ g.getDistrict() +"' and Upz='"+ g.getUpazila() +"' and UN='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and Vill='"+ g.getVillage() +"'  and ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"' and  HHNo='"+ g.getHouseholdNo() +"' and SNo='"+ txtSNo.getText().toString() +"' and PGN = '"+ PGNNo() +"'"))
            {
                SQL = "Insert into " + TableName + "(Dist,Upz,UN,Mouza,Vill,ProvType,ProvCode,HHNo,SNo,PGN,EnDt,Upload,UploadDT)Values('"+ g.getDistrict() +"','"+ g.getUpazila() +"','"+ g.getUnion() +"','"+ g.getMouza() +"','"+ g.getVillage() +"','"+ g.getProvType() +"','"+ g.getProvCode() +"','"+ g.getHouseholdNo() +"','"+ txtSNo.getText() +"','"+ PGNNo() +"','"+ Global.DateTimeNowYMDHMS() +"','2','')";
                C.Save(SQL);
            }
            SQL = "Update " + TableName + " Set ";
            SQL+="PNC1 = '"+ (ChkPNC1.isChecked()?"1":"2") +"',";
            SQL+="DOPNC1 = '"+ Global.DateConvertYMD(dtpDOPNC1.getText().toString()) +"',";
            SQL+="PNC2 = '"+ (ChkPNC2.isChecked()?"1":"2") +"',";
            SQL+="DOPNC2 = '"+ Global.DateConvertYMD(dtpDOPNC2.getText().toString()) +"',";
            SQL+="PNC3 = '"+ (ChkPNC3.isChecked()?"1":"2") +"',";
            SQL+="DOPNC3 = '"+ Global.DateConvertYMD(dtpDOPNC3.getText().toString()) +"',";
            SQL+="PNC4 = '"+ (ChkPNC4.isChecked()?"1":"2") +"',";
            SQL+="DOPNC4 = '"+ Global.DateConvertYMD(dtpDOPNC4.getText().toString()) +"',";
            SQL+="PNC5 = '',";
            SQL+="DOPNC5 = '',";
            SQL+="PNC6 = '',";
            SQL+="DOPNC6 = '',";
            SQL+="PNC7 = '',";
            SQL+="DOPNC7 = '',";
            SQL+="PNC8 = '',";
            SQL+="DOPNC8 = '',";
            SQL+="RegDT = '"+ g.DateNowYMD() +"'";
            SQL+="  Where Dist='"+ g.getDistrict() +"' and Upz='"+ g.getUpazila() +"' and UN='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and Vill='"+ g.getVillage() +"' and HHNo='"+ g.getHouseholdNo() +"' and SNo='"+ txtSNo.getText().toString() +"' and PGN = '"+ PGNNo() +"'";
            C.Save(SQL);
            Connection.MessageBox(PNC_Mother.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
            finish();
            //if(rdoOutcomeLB.isChecked())
            //{
                Intent f2 = new Intent(getApplicationContext(),PNC_Child.class);
                startActivity(f2);
           /* }
            else
            {
                Intent f2 = new Intent(getApplicationContext(), MemberList.class);
                startActivity(f2);
            }*/
        }
        catch(Exception  e)
        {
            Connection.MessageBox(PNC_Mother.this, e.getMessage());
            return;
        }
    }

    private void DataSearch(String Dist, String Upz, String UN, String Mouza, String Vill, String HHNo, String SNo)
    {
        try
        {
            String SQL = "";
            SQL = "Select Dist, Upz, UN, Mouza, Vill, HHNo, SNo as SNo, ifnull(HealthID,'') as HealthID, ifnull(NameEng,'') as NameEng,";
            SQL += " ifnull(NameBang,'') as NameBang, ifnull(Rth,'') as Rth, ifnull(HaveNID,'') as HaveNID, ifnull(NID,'') as NID, ifnull(NIDStatus,'') as NIDStatus, ifnull(HaveBR,'') as HaveBR, ifnull(BRID,'') as BRID, ifnull(BRIDStatus,'') as BRIDStatus, ifnull(MobileNo1,'') as MobileNo1,";
            SQL += " ifnull(MobileNo2,'') as MobileNo2, ifnull(DOB,'') as DOB, ifnull(Age,'') as Age, ifnull(DOBSource,'') as DOBSource, ifnull(BPlace,'') as BPlace, ifnull(FNo,'') as FNo, ifnull(Father,'') as Father, ifnull(MNo,'') as MNo, ifnull(Mother,'') as Mother,";
            SQL += " ifnull(Sex,'') as Sex, ifnull(MS,'') as MS, ifnull(SPNO1,'') as SPNO1,ifnull(SPNO2,'') as SPNO2,ifnull(SPNO3,'') as SPNO3,ifnull(SPNO4,'') as SPNO4, ifnull(ELCONo,'') as ELCONo, ifnull(ELCODontKnow,'') as ELCODontKnow, ifnull(EDU,'') as EDU, ifnull(Rel,'') as Rel, ifnull(Nationality,'') as Nationality, ifnull(OCP,'') as OCP";
            SQL += ",(select NameEng  from member where Dist='"+ Dist +"' and Upz='"+ Upz +"' and UN='"+ UN +"' and Mouza='"+ Mouza +"' and Vill='"+ Vill +"' and HHNo='"+ HHNo +"' and SNo=(select  SPNO1  from member  Where Dist='"+ Dist +"' and Upz='"+ Upz +"' and UN='"+ UN +"' and Mouza='"+ Mouza +"' and Vill='"+ Vill +"' and HHNo='"+ HHNo +"' and SNo='"+ SNo +"'))as HusName";
            SQL += " from Member Where Dist='"+ Dist +"' and Upz='"+ Upz +"' and UN='"+ UN +"' and Mouza='"+ Mouza +"' and Vill='"+ Vill +"' and HHNo='"+ HHNo +"' and SNo='"+ SNo +"'";
            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();
            while(!cur.isAfterLast())
            {
                txtHealthID.setText(cur.getString(cur.getColumnIndex("HealthID")));
                txtSNo.setText(cur.getString(cur.getColumnIndex("SNo")));
                txtName.setText(cur.getString(cur.getColumnIndex("NameEng")));
                txtHusName.setText(cur.getString(cur.getColumnIndex("HusName")));
                txtAge.setText(cur.getString(cur.getColumnIndex("Age")));

                cur.moveToNext();
            }
            cur.close();
        }
        catch(Exception  e)
        {
            Connection.MessageBox(PNC_Mother.this, e.getMessage());
            return;
        }
    }

    private void ELCONoSearch(String Dist, String Upz, String UN, String Mouza, String Vill, String HHNo, String SNo)
    {
        try
        {
            String SQL = "";
            SQL = "select E.ELCONo as ELCONo from ELCO E, member M Where E.Dist=M.Dist and E.Upz=M.Upz and E.UN=M.UN and E.Mouza=M.Mouza and E.Vill=M.Vill and ";
            SQL += "E.HHNo=M.HHNo and E.SNo=M.SNo and ";
            SQL += "E.Dist='"+ Dist +"' and E.Upz='"+ Upz +"' and E.UN='"+ UN +"' and E.Mouza='"+ Mouza +"' and E.Vill='"+ Vill +"' and E.HHNo='"+ HHNo +"' and E.SNo='"+ SNo +"'";
            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();
            while(!cur.isAfterLast())
            {
                txtElcoNo.setText(cur.getString(cur.getColumnIndex("ELCONo")));
                cur.moveToNext();
            }
            cur.close();
        }
        catch(Exception  e)
        {
            Connection.MessageBox(PNC_Mother.this, e.getMessage());
            return;
        }
    }

    private void PNCVisitSearch(String Dist, String Upz, String UN, String Mouza, String Vill, String HHNo, String SNo)
    {
        try
        {

            String SQL = "";
            SQL = "select PNC1,DOPNC1,PNC2,DOPNC2,PNC3,DOPNC3,PNC4,DOPNC4 from PNCMother";
            SQL += " Where Dist='"+ Dist +"' and Upz='"+ Upz +"' and UN='"+ UN +"' and Mouza='"+ Mouza +"' and Vill='"+ Vill +"' and HHNo='"+ HHNo +"' and SNo='"+ SNo +"' and ";
            SQL += "PGN=(select '0'||cast(max(PGN) as string) from PNCMother Where Dist='"+ Dist +"' and Upz='"+ Upz +"' and UN='"+ UN +"' and Mouza='"+ Mouza +"' and Vill='"+ Vill +"' and HHNo='"+ HHNo +"' and SNo='"+ SNo +"' and PGN=(select  max(PGN) from PNCMother Where Dist='"+ g.getDistrict() +"' and Upz='"+ g.getUpazila() +"' and UN='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and Vill='"+ g.getVillage() +"'  and ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"' and  HHNo='"+ g.getHouseholdNo() +"' and SNo='"+ txtSNo.getText().toString() +"'))";
            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();
            while(!cur.isAfterLast())
            {
                if(cur.getString(cur.getColumnIndex("PNC1")).equals("1"))
                {
                    ChkPNC1.setChecked(true);
                    dtpDOPNC1.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("DOPNC1"))));
                }
                else if(cur.getString(cur.getColumnIndex("PNC1")).equals("2"))
                {
                    ChkPNC1.setChecked(false);
                }

                if(cur.getString(cur.getColumnIndex("PNC2")).equals("1"))
                {
                    ChkPNC2.setChecked(true);
                    dtpDOPNC2.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("DOPNC2"))));
                }
                else if(cur.getString(cur.getColumnIndex("PNC2")).equals("2"))
                {
                    ChkPNC2.setChecked(false);
                }

                if(cur.getString(cur.getColumnIndex("PNC3")).equals("1"))
                {
                    ChkPNC3.setChecked(true);
                    dtpDOPNC3.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("DOPNC3"))));
                }
                else if(cur.getString(cur.getColumnIndex("PNC3")).equals("2"))
                {
                    ChkPNC3.setChecked(false);
                }

                if(cur.getString(cur.getColumnIndex("PNC4")).equals("1"))
                {
                    ChkPNC4.setChecked(true);
                    dtpDOPNC4.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("DOPNC4"))));
                }
                else if(cur.getString(cur.getColumnIndex("PNC4")).equals("2"))
                {
                    ChkPNC4.setChecked(false);
                }

                cur.moveToNext();
            }
            cur.close();
        }
        catch(Exception  e)
        {
            Connection.MessageBox(PNC_Mother.this, e.getMessage());
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

            if(VariableID.equals("btnDOPNC1"))
            {
                dtpDate = (EditText)findViewById(R.id.dtpDOPNC1);
            }
            else if(VariableID.equals("btnDOPNC2"))
            {
                dtpDate = (EditText)findViewById(R.id.dtpDOPNC2);
            }
            else if(VariableID.equals("btnDOPNC3"))
            {
                dtpDate = (EditText)findViewById(R.id.dtpDOPNC3);
            }
            else if(VariableID.equals("btnDOPNC4"))
            {
                dtpDate = (EditText)findViewById(R.id.dtpDOPNC4);
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
}