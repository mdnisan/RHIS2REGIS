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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.List;

import Common.Connection;
import Common.Global;

/**
 * Created by TanvirHossain on 17/03/2015.
 */

public class DeathForm extends Activity {
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
        AlertDialog.Builder adb = new AlertDialog.Builder(DeathForm.this);
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

    LinearLayout secHHNo;
    TextView VlblHHNo;

    CheckBox chkHHNoDontKnow;
    EditText txtHHNo;

    LinearLayout secSNo;
    TextView VlblSNo;
    TextView txtSNo;

    LinearLayout secName;
    TextView VlblName;
    TextView txtName;

    EditText dtpDeathDT;
    ImageButton btnDeathDT;
    Spinner spnDeathCause;

    String StartTime;
    LinearLayout secAgeDeath;
    TextView lblAgeDeath;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.deathform);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();

            TableName = "Death";

            txtHealthID = (TextView) findViewById( R.id.txtHealthID );

            VlblSNo=(TextView) findViewById(R.id.VlblSNo);
            txtSNo=(TextView) findViewById(R.id.txtSNo);

            secName=(LinearLayout)findViewById(R.id.secName);
            VlblName=(TextView) findViewById(R.id.VlblName);
            txtName = (TextView) findViewById( R.id.txtName);

            dtpDeathDT=(EditText) findViewById(R.id.dtpDeathDT);
            dtpDeathDT.setText(Global.DateNowDMY());
            btnDeathDT = (ImageButton) findViewById(R.id.btnDeathDT);
            spnDeathCause= (Spinner) findViewById(R.id.spnDeathCause);
            List<String> listCauseDeath = new ArrayList<String>();


            listCauseDeath.add("");
            listCauseDeath.add("1-বার্ধক্য জনিত ");
            listCauseDeath.add("2-অসুস্থতা");
            listCauseDeath.add("3-দুর্ঘটনা জনিত");
            ArrayAdapter<String> adptrDeathCause= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listCauseDeath);
            spnDeathCause.setAdapter(adptrDeathCause);

            secAgeDeath=(LinearLayout)findViewById(R.id.secAgeDeath);
            lblAgeDeath=(TextView) findViewById(R.id.lblAgeDeath);



            btnDeathDT.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) { VariableID = "btnDeathDT"; showDialog(DATE_DIALOG); }});

            DataSearch(g.getDistrict(), g.getUpazila(), g.getUnion(), g.getMouza(), g.getVillage(), g.getHouseholdNo(), g.getSerialNo() );

            Button cmdSave = (Button) findViewById(R.id.cmdSave);
            cmdSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    DataSave();
                }});

        }
        catch(Exception  e)
        {
            Connection.MessageBox(DeathForm.this, e.getMessage());
            return;
        }
    }

    private void DataSave()
    {
        try
        {
            if(dtpDeathDT.getText().toString().length()==0)
            {
                Connection.MessageBox(DeathForm.this, "মৃত্যুর তারিখ কত লিখুন।");
                dtpDeathDT.requestFocus();
                return;
            }

            else if(Global.DateDifferenceDays(Global.DateNowDMY(), dtpDeathDT.getText().toString())<0)
            {
                Connection.MessageBox(DeathForm.this, "মৃত্যুর তারিখ সঠিক নয়।");
                dtpDeathDT.requestFocus();
                return;
            }
            else if(spnDeathCause.getSelectedItemPosition()==0)
            {
                Connection.MessageBox(DeathForm.this, "তালিকা থেকে সঠিক মৃত্যুর কারণ সিলেক্ট করুন।");
                dtpDeathDT.requestFocus();
                return;
            }

            String SQL = "";
            if(!C.Existence("Select Dist,Upz,UN,Mouza,Vill,HHNo,SNo from " + TableName + "  Where Dist='"+ g.getDistrict() +"' and Upz='"+ g.getUpazila() +"' and UN='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and Vill='"+ g.getVillage() +"' and HHNo='"+ g.getHouseholdNo() +"' and SNo='"+ txtSNo.getText().toString() +"'"))
            {
                SQL = "Insert into " + TableName + "(Dist,Upz,UN,Mouza,Vill,ProvType,ProvCode,HHNo,SNo,EnDt,Upload)Values('"+ g.getDistrict() +"','"+ g.getUpazila() +"','"+ g.getUnion() +"','"+ g.getMouza() +"','"+ g.getVillage() +"','"+ g.getProvType() +"','"+ g.getProvCode() +"','"+ g.getHouseholdNo() +"','"+ txtSNo.getText() +"','"+ Global.DateTimeNowYMDHMS() +"','2')";
                C.Save(SQL);
            }
            SQL = "Update " + TableName + " Set ";
            SQL+="DeathDT = '"+ Global.DateConvertYMD(dtpDeathDT.getText().toString()) +"'";

            SQL+="  Where Dist='"+ g.getDistrict() +"' and Upz='"+ g.getUpazila() +"' and UN='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and Vill='"+ g.getVillage() +"' and ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"' and HHNo='"+ g.getHouseholdNo() +"' and SNo='"+ txtSNo.getText().toString() +"'";
            C.Save(SQL);

            C.Save("Update Member Set ExType='55',ExDate='"+ Global.DateConvertYMD(dtpDeathDT.getText().toString()) +"' Where Dist='"+ g.getDistrict() +"' and Upz='"+ g.getUpazila() +"' and UN='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and Vill='"+ g.getVillage() +"' and ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"' and HHNo='"+ g.getHouseholdNo() +"' and SNo='"+ txtSNo.getText().toString() +"'");

            Connection.MessageBox(DeathForm.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
            finish();
            Intent f2 = new Intent(getApplicationContext(), MemberList.class);
            startActivity(f2);
        }
        catch(Exception  e)
        {
            Connection.MessageBox(DeathForm.this, e.getMessage());
            return;
        }
    }

    private void DataSearch(String Dist, String Upz, String UN, String Mouza, String Vill, String HHNo, String SNo)
    {
        try
        {
            String SQL = "";
            SQL = "Select SNo as SNo, ifnull(HealthID,'') as HealthID, ifnull(NameEng,'') as NameEng";
            SQL += " from Member Where Dist='"+ Dist +"' and Upz='"+ Upz +"' and UN='"+ UN +"' and Mouza='"+ Mouza +"' and Vill='"+ Vill +"' and ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"' and HHNo='"+ HHNo +"' and SNo='"+ SNo +"'";
            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();
            while(!cur.isAfterLast())
            {
                txtHealthID.setText(cur.getString(cur.getColumnIndex("HealthID")));
                txtSNo.setText(cur.getString(cur.getColumnIndex("SNo")));
                txtName.setText(cur.getString(cur.getColumnIndex("NameEng")));
                cur.moveToNext();
            }
            cur.close();
        }
        catch(Exception  e)
        {
            Connection.MessageBox(DeathForm.this, e.getMessage());
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

            dtpDate = (EditText)findViewById(R.id.dtpDeathDT);

            if (VariableID.equals("btnDeathDT"))
            {
                dtpDate = (EditText)findViewById(R.id.dtpDeathDT);
            }


            dtpDate.setText(new StringBuilder()
                    .append(Global.Right("00"+mDay,2)).append("/")
                    .append(Global.Right("00"+mMonth,2)).append("/")
                    .append(mYear));

            Integer age = Global.DateDifferenceDays(Global.DateNowDMY(), dtpDate.getText().toString());
            if(age>=0 & age<=7)
                lblAgeDeath.setText("০ থেকে ৭ দিন");
            else if(age>=8 & age<=28)
                lblAgeDeath.setText("৮ থেকে ২৮ দিন");
            else if(age>=29 & age<=364)
                lblAgeDeath.setText("২৯ দিন থেকে ১ বৎসরের কম");
            else if(age>=365 & age<=1825)
                lblAgeDeath.setText("১ বৎসর থেকে ৫ বৎসরের কম");
            else
                lblAgeDeath.setText("অন্য সকল মৃত্যু");

            secAgeDeath.setVisibility(View.VISIBLE);

        }
    };
    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
            hour = selectedHour; minute = selectedMinute;
            EditText tpTime;

        }
    };
}