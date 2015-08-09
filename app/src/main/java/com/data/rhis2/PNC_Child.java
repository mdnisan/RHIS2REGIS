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
public class PNC_Child extends Activity {
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
        AlertDialog.Builder adb = new AlertDialog.Builder(PNC_Child.this);
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

    LinearLayout secPNCCh12;
    TextView VlblPNCCh1;
    CheckBox ChkPNCCh1;
    EditText dtpDOPNCCh1;
    ImageButton btnDOPNCCh1;

    TextView VlblPNCCh2;
    CheckBox ChkPNCCh2;
    EditText dtpDOPNCCh2;
    ImageButton btnDOPNCCh2;

    LinearLayout secPNCCh34;
    TextView VlblPNCCh3;
    CheckBox ChkPNCCh3;
    EditText dtpDOPNCCh3;
    ImageButton btnDOPNCCh3;

    TextView VlblPNCCh4;
    CheckBox ChkPNCCh4;
    EditText dtpDOPNCCh4;
    ImageButton btnDOPNCCh4;

    LinearLayout secPNCCh5;
    TextView VlblPNCCh5;
    CheckBox ChkPNCCh5;
    EditText dtpDOPNCCh5;
    ImageButton btnDOPNCCh5;

    Button cmdSave;

    String StartTime;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.pnc_child);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();

            TableName = "PNCChild";

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

            secPNCCh12=(LinearLayout)findViewById(R.id.secPNCCh12);
            VlblPNCCh1=(TextView) findViewById(R.id.VlblPNCCh1);
            ChkPNCCh1=(CheckBox) findViewById(R.id.ChkPNCCh1);
            dtpDOPNCCh1=(EditText) findViewById(R.id.dtpDOPNCCh1);
            btnDOPNCCh1 = (ImageButton) findViewById(R.id.btnDOPNCCh1);
            dtpDOPNCCh1.setText( "" );
            btnDOPNCCh1.setEnabled( false );
            ChkPNCCh1.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    if(((CheckBox) v).isChecked())
                    {
                        btnDOPNCCh1.setEnabled( true );
                    }
                    else
                    {
                        dtpDOPNCCh1.setText( "" );
                        btnDOPNCCh1.setEnabled( false );
                    }
                }
            });
           /* VlblPNCCh2=(TextView) findViewById(R.id.VlblPNCCh2);
            ChkPNCCh2=(CheckBox) findViewById(R.id.ChkPNCCh2);
            dtpDOPNCCh2=(EditText) findViewById(R.id.dtpDOPNCCh2);
            btnDOPNCCh2 = (ImageButton) findViewById(R.id.btnDOPNCCh2);*/
            dtpDOPNCCh2.setText( "" );
            btnDOPNCCh2.setEnabled( false );
            ChkPNCCh2.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    if(((CheckBox) v).isChecked())
                    {
                        btnDOPNCCh2.setEnabled( true );
                    }
                    else
                    {
                        dtpDOPNCCh2.setText( "" );
                        btnDOPNCCh2.setEnabled( false );
                    }
                }
            });
           /* VlblPNCCh3=(TextView) findViewById(R.id.VlblPNCCh3);
            ChkPNCCh3=(CheckBox) findViewById(R.id.ChkPNCCh3);
            dtpDOPNCCh3=(EditText) findViewById(R.id.dtpDOPNCCh3);
            btnDOPNCCh3 = (ImageButton) findViewById(R.id.btnDOPNCCh3);*/
            dtpDOPNCCh3.setText( "" );
            btnDOPNCCh3.setEnabled( false );
            ChkPNCCh3.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    if(((CheckBox) v).isChecked())
                    {
                        btnDOPNCCh3.setEnabled( true );
                    }
                    else
                    {
                        dtpDOPNCCh3.setText( "" );
                        btnDOPNCCh3.setEnabled( false );
                    }
                }
            });
           /* VlblPNCCh4=(TextView) findViewById(R.id.VlblPNCCh4);
            ChkPNCCh4=(CheckBox) findViewById(R.id.ChkPNCCh4);
            dtpDOPNCCh4=(EditText) findViewById(R.id.dtpDOPNCCh4);
            btnDOPNCCh4 = (ImageButton) findViewById(R.id.btnDOPNCCh4);*/
            dtpDOPNCCh4.setText( "" );
            btnDOPNCCh4.setEnabled( false );
            ChkPNCCh4.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    if(((CheckBox) v).isChecked())
                    {
                        btnDOPNCCh4.setEnabled( true );
                    }
                    else
                    {
                        dtpDOPNCCh4.setText( "" );
                        btnDOPNCCh4.setEnabled( false );
                    }
                }
            });

            DataSearch(g.getDistrict(), g.getUpazila(), g.getUnion(), g.getMouza(), g.getVillage(), g.getHouseholdNo(), g.getSerialNo() );
            ELCONoSearch(g.getDistrict(), g.getUpazila(), g.getUnion(), g.getMouza(), g.getVillage(), g.getHouseholdNo(), g.getSerialNo() );
            String ServiceId = "";
            PNCVisitSearch(ServiceId );
            btnDOPNCCh1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) { VariableID = "btnDOPNCCh1"; showDialog(DATE_DIALOG); }});

            /*btnDOPNCCh2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) { VariableID = "btnDOPNCCh2"; showDialog(DATE_DIALOG); }});

            btnDOPNCCh3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) { VariableID = "btnDOPNCCh3"; showDialog(DATE_DIALOG); }});

            btnDOPNCCh4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) { VariableID = "btnDOPNCCh4"; showDialog(DATE_DIALOG); }});
*/
            cmdSave = (Button) findViewById(R.id.cmdSave);
            cmdSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    DataSave();
                }});
        }
        catch(Exception  e)
        {
            Connection.MessageBox(PNC_Child.this, e.getMessage());
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
            if(ChkPNCCh1.isChecked() & ChkPNCCh1.isShown())
            {
                if(dtpDOPNCCh1.getText().toString().length()==0 & dtpDOPNCCh1.isShown())
                {
                    Connection.MessageBox(PNC_Child.this, "পি এন সি  ১ এর তারিখ কত লিখুন।");
                    dtpDOPNCCh1.requestFocus();
                    return;
                }
            }

            /*if(ChkPNCCh2.isChecked() & ChkPNCCh2.isShown())
            {
                if(dtpDOPNCCh2.getText().toString().length()==0  & dtpDOPNCCh2.isShown())
                {
                    Connection.MessageBox(PNC_Child.this, "পি এন সি  ২ এর তারিখ কত লিখুন।");
                    dtpDOPNCCh2.requestFocus();
                    return;
                }
            }
            if(ChkPNCCh3.isChecked() & ChkPNCCh3.isShown())
            {
                if(dtpDOPNCCh3.getText().toString().length()==0  & dtpDOPNCCh3.isShown())
                {
                    Connection.MessageBox(PNC_Child.this, "পি এন সি  ৩ এর তারিখ কত লিখুন।");
                    dtpDOPNCCh3.requestFocus();
                    return;
                }
            }

            if(ChkPNCCh4.isChecked() & ChkPNCCh4.isShown())
            {
                if(dtpDOPNCCh4.getText().toString().length()==0 & dtpDOPNCCh4.isShown())
                {
                    Connection.MessageBox(PNC_Child.this, "পি এন সি  ৪ এর তারিখ কত লিখুন।");
                    dtpDOPNCCh4.requestFocus();
                    return;
                }
            }*/

            String SQL = "";
            SQL = "select healthId, pregNo, childNo,serviceId, providerId, visitDate, serviceSource, systemEntryDate, Upload, UploadDT, modifyDate FROM pncServiceChild ORDER BY visitDate";
            SQL += " WHERE healthId = '"+ g.getHealthID() +"' AND pregNo ='"+ g.getPregNo() +"' AND childNo='"+ g.getChildNo() +"' and serviceId='"+ GetServiceId() +"'";

            if(!C.Existence(SQL))
            {
                SQL = "Insert into " + TableName + "(healthId, pregNo, childNo, serviceId, providerId, visitDate, serviceSource, systemEntryDate, Upload, UploadDT, modifyDate)Values('"+
                        g.getHealthID() +"','"+ g.getPregNo() +"','"+ g.getChildNo() +"','"+ GetServiceId() +"','"+ g.getProvCode() +"','"+ dtpDOPNCCh1.getText().toString() +"','"+  "G','"+  Global.DateTimeNowYMDHMS() +"','"+ "2','"+ "','"+"')";
                C.Save(SQL);
            }
            C.Save(SQL);
            Connection.MessageBox(PNC_Child.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
            finish();
            //if(rdoOutcomeLB.isChecked())
            //{
            Intent f2 = new Intent(getApplicationContext(),MemberList.class);
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
            Connection.MessageBox(PNC_Child.this, e.getMessage());
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
            Connection.MessageBox(PNC_Child.this, e.getMessage());
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
            Connection.MessageBox(PNC_Child.this, e.getMessage());
            return;
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
                    ChkPNCCh1.setChecked(true);
                    dtpDOPNCCh1.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("visitDate"))));
                }
                /*else if(cur.getString(cur.getColumnIndex("PNC1")).equals("2"))
                {
                    ChkPNCCh1.setChecked(false);
                }

                if(cur.getString(cur.getColumnIndex("PNC2")).equals("1"))
                {
                    ChkPNCCh2.setChecked(true);
                    dtpDOPNCCh2.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("DOPNC2"))));
                }
                else if(cur.getString(cur.getColumnIndex("PNC2")).equals("2"))
                {
                    ChkPNCCh2.setChecked(false);
                }

                if(cur.getString(cur.getColumnIndex("PNC3")).equals("1"))
                {
                    ChkPNCCh3.setChecked(true);
                    dtpDOPNCCh3.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("DOPNC3"))));
                }
                else if(cur.getString(cur.getColumnIndex("PNC3")).equals("2"))
                {
                    ChkPNCCh3.setChecked(false);
                }

                if(cur.getString(cur.getColumnIndex("PNC4")).equals("1"))
                {
                    ChkPNCCh4.setChecked(true);
                    dtpDOPNCCh4.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("DOPNC4"))));
                }
                else if(cur.getString(cur.getColumnIndex("PNC4")).equals("2"))
                {
                    ChkPNCCh4.setChecked(false);
                }
*/
                cur.moveToNext();
            }
            cur.close();
        }
        catch(Exception  e)
        {
            Connection.MessageBox(PNC_Child.this, e.getMessage());
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
            else if(VariableID.equals("btnDOPNCCh2"))
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