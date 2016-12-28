package com.data.rhis2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import Common.Connection;
import Common.Global;

/**
 * Created by Nisan on 4/7/2016.
 */
public class FPIMonitoring extends Activity {
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
        AlertDialog.Builder adb = new AlertDialog.Builder(FPIMonitoring.this);
        switch (item.getItemId()) {
            case R.id.menuClose:
                adb.setTitle("Close");
                adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান?");
                // adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান[Yes/No]?");
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
    static final int DATE_DIALOG = 1;
    static final int TIME_DIALOG = 2;

    Connection C;
    Global g;
    SimpleAdapter dataAdapter;
    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    private String TableName;

    LinearLayout secvDate;
    TextView VlblvDate;
    EditText dtpvDate;
    ImageButton btnvDate;
    LinearLayout secfpaCode;
    TextView VlblfpaCode;
    Spinner spnfpaCode;
    LinearLayout secfpaUnit;
    TextView VlblfpaUnit;
    EditText txtfpaUnit;
    LinearLayout secfpaVill;
    TextView VlblfpaVill;
    Spinner spnfpaVill;
    LinearLayout secfpaAdvance;
    TextView VlblfpaAdvance;
    Spinner spnfpaAdvance;
    LinearLayout secfpivisit;
    LinearLayout secneedItems1;
    TextView VlblneedItems1;
    RadioGroup rdogrpneedItems1;

    RadioButton rdoneedItems11;
    RadioButton rdoneedItems12;
    LinearLayout secneedItems2;
    TextView VlblneedItems2;
    RadioGroup rdogrpneedItems2;

    RadioButton rdoneedItems21;
    RadioButton rdoneedItems22;
    LinearLayout secneedItems3;
    TextView VlblneedItems3;
    RadioGroup rdogrpneedItems3;

    RadioButton rdoneedItems31;
    RadioButton rdoneedItems32;
    LinearLayout secneedItems4;
    TextView VlblneedItems4;
    RadioGroup rdogrpneedItems4;

    RadioButton rdoneedItems41;
    RadioButton rdoneedItems42;
    LinearLayout secneedItems5;
    TextView VlblneedItems5;
    RadioGroup rdogrpneedItems5;

    RadioButton rdoneedItems51;
    RadioButton rdoneedItems52;
    LinearLayout secneedItems6;
    TextView VlblneedItems6;
    RadioGroup rdogrpneedItems6;

    RadioButton rdoneedItems61;
    RadioButton rdoneedItems62;
    LinearLayout secneedItems7;
    TextView VlblneedItems7;
    RadioGroup rdogrpneedItems7;

    RadioButton rdoneedItems71;
    RadioButton rdoneedItems72;
    LinearLayout secneedItems8;
    TextView VlblneedItems8;
    RadioGroup rdogrpneedItems8;

    RadioButton rdoneedItems81;
    RadioButton rdoneedItems82;


    String StartTime;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.fpimonitoring);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();

            TableName = "FPIMonitoring";


            secvDate = (LinearLayout) findViewById(R.id.secvDate);
            VlblvDate = (TextView) findViewById(R.id.VlblvDate);
            dtpvDate = (EditText) findViewById(R.id.dtpvDate);
            dtpvDate.setText(Global.DateNowDMY());
            secfpaCode = (LinearLayout) findViewById(R.id.secfpaCode);
            VlblfpaCode = (TextView) findViewById(R.id.VlblfpaCode);
            spnfpaCode = (Spinner) findViewById(R.id.spnfpaCode);


            spnfpaCode.setAdapter(C.getArrayAdapterMultiline("Select ProvCode||'-'||ProvName from ProviderDB where ProvType ='3'"));

            spnfpaCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    String val = Global.Left(spnfpaCode.getSelectedItem().toString(), 5);
                    if (val.length() >= 0) {
                        txtfpaUnit.setText(C.ReturnSingleValue("Select CASE \n" +
                                " WHEN FWAUnit='01' THEN'01-১'\n" +
                                " WHEN FWAUnit='02' THEN'02-১ক'\n" +
                                " WHEN FWAUnit='03' THEN'03-১খ'\n" +
                                " WHEN FWAUnit='04' THEN'04-১গ'\n" +
                                " WHEN FWAUnit='05' THEN'05-2'\n" +
                                " WHEN FWAUnit='06' THEN'06-২ক'\n" +
                                " WHEN FWAUnit='07' THEN'07-২খ'\n" +
                                " WHEN FWAUnit='08' THEN'08-২গ'\n" +
                                " WHEN FWAUnit='09' THEN'09-৩'\n" +
                                " WHEN FWAUnit='10' THEN'10-৩ক'\n" +
                                " WHEN FWAUnit='11' THEN'11-৩খ'\n" +
                                " WHEN FWAUnit='12' THEN'12-৩গ'\n" +
                                " ELSE\n" +
                                "        ''\n" +
                                "    END from ProviderArea where  ProvCode='" + val + "'"));

                        // DataSearch("");
                        txtfpaUnit.setEnabled(false);
                        spnfpaVill.setAdapter(C.getArrayAdapterMultiline("Select '-' as VILLAGENAME union select (CASE \n" +
                                "WHEN Length(MOUZAID)=1 THEN substr('00' ||MOUZAID, -3, 3)||VILLAGEID||'-'||VILLAGENAME \n" +
                                "WHEN Length(MOUZAID)=2 THEN substr('0' ||MOUZAID, -3, 3)||VILLAGEID||'-'||VILLAGENAME \n" +
                                "WHEN Length(MOUZAID)=3 THEN substr('0' ||MOUZAID, -3, 3)||VILLAGEID||'-'||VILLAGENAME                            \n" +
                                " ELSE\n" +
                                "''\n" +
                                "END) as VILLAGENAME\n" +
                                "from Village where mouzaid||VILLAGEID in (Select mouzaid||VILLAGEID from ProviderArea where ProvCode='" + Global.Left(spnfpaCode.getSelectedItem().toString(), 5) + "')"));
                        PVisitStatus();
                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            secfpaUnit = (LinearLayout) findViewById(R.id.secfpaUnit);
            VlblfpaUnit = (TextView) findViewById(R.id.VlblfpaUnit);
            txtfpaUnit = (EditText) findViewById(R.id.txtfpaUnit);

            // txtfpaUnit.setText(g.getFWAUnit());

            secfpaVill = (LinearLayout) findViewById(R.id.secfpaVill);
            VlblfpaVill = (TextView) findViewById(R.id.VlblfpaVill);
            spnfpaVill = (Spinner) findViewById(R.id.spnfpaVill);
            secfpaAdvance = (LinearLayout) findViewById(R.id.secfpaAdvance);
            VlblfpaAdvance = (TextView) findViewById(R.id.VlblfpaAdvance);
            spnfpaAdvance = (Spinner) findViewById(R.id.spnfpaAdvance);
            List<String> listfpaAdvance = new ArrayList<String>();

            listfpaAdvance.add("");
            listfpaAdvance.add("01-সঠিক");
            listfpaAdvance.add("02-আগে");
            listfpaAdvance.add("03-পিছনে");
            ArrayAdapter<String> adptrfpaAdvance = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listfpaAdvance);
            spnfpaAdvance.setAdapter(adptrfpaAdvance);

            secfpivisit = (LinearLayout) findViewById(R.id.secfpivisit);
            secneedItems1 = (LinearLayout) findViewById(R.id.secneedItems1);
            VlblneedItems1 = (TextView) findViewById(R.id.VlblneedItems1);
            rdogrpneedItems1 = (RadioGroup) findViewById(R.id.rdogrpneedItems1);

            rdoneedItems11 = (RadioButton) findViewById(R.id.rdoneedItems11);
            rdoneedItems12 = (RadioButton) findViewById(R.id.rdoneedItems12);
            secneedItems2 = (LinearLayout) findViewById(R.id.secneedItems2);
            VlblneedItems2 = (TextView) findViewById(R.id.VlblneedItems2);
            rdogrpneedItems2 = (RadioGroup) findViewById(R.id.rdogrpneedItems2);

            rdoneedItems21 = (RadioButton) findViewById(R.id.rdoneedItems21);
            rdoneedItems22 = (RadioButton) findViewById(R.id.rdoneedItems22);
            secneedItems3 = (LinearLayout) findViewById(R.id.secneedItems3);
            VlblneedItems3 = (TextView) findViewById(R.id.VlblneedItems3);
            rdogrpneedItems3 = (RadioGroup) findViewById(R.id.rdogrpneedItems3);

            rdoneedItems31 = (RadioButton) findViewById(R.id.rdoneedItems31);
            rdoneedItems32 = (RadioButton) findViewById(R.id.rdoneedItems32);
            secneedItems4 = (LinearLayout) findViewById(R.id.secneedItems4);
            VlblneedItems4 = (TextView) findViewById(R.id.VlblneedItems4);
            rdogrpneedItems4 = (RadioGroup) findViewById(R.id.rdogrpneedItems4);

            rdoneedItems41 = (RadioButton) findViewById(R.id.rdoneedItems41);
            rdoneedItems42 = (RadioButton) findViewById(R.id.rdoneedItems42);
            secneedItems5 = (LinearLayout) findViewById(R.id.secneedItems5);
            VlblneedItems5 = (TextView) findViewById(R.id.VlblneedItems5);
            rdogrpneedItems5 = (RadioGroup) findViewById(R.id.rdogrpneedItems5);

            rdoneedItems51 = (RadioButton) findViewById(R.id.rdoneedItems51);
            rdoneedItems52 = (RadioButton) findViewById(R.id.rdoneedItems52);
            secneedItems6 = (LinearLayout) findViewById(R.id.secneedItems6);
            VlblneedItems6 = (TextView) findViewById(R.id.VlblneedItems6);
            rdogrpneedItems6 = (RadioGroup) findViewById(R.id.rdogrpneedItems6);

            rdoneedItems61 = (RadioButton) findViewById(R.id.rdoneedItems61);
            rdoneedItems62 = (RadioButton) findViewById(R.id.rdoneedItems62);
            secneedItems7 = (LinearLayout) findViewById(R.id.secneedItems7);
            VlblneedItems7 = (TextView) findViewById(R.id.VlblneedItems7);
            rdogrpneedItems7 = (RadioGroup) findViewById(R.id.rdogrpneedItems7);

            rdoneedItems71 = (RadioButton) findViewById(R.id.rdoneedItems71);
            rdoneedItems72 = (RadioButton) findViewById(R.id.rdoneedItems72);
            secneedItems8 = (LinearLayout) findViewById(R.id.secneedItems8);
            VlblneedItems8 = (TextView) findViewById(R.id.VlblneedItems8);
            rdogrpneedItems8 = (RadioGroup) findViewById(R.id.rdogrpneedItems8);

            rdoneedItems81 = (RadioButton) findViewById(R.id.rdoneedItems81);
            rdoneedItems82 = (RadioButton) findViewById(R.id.rdoneedItems82);


            PVisitStatus();

            btnvDate = (ImageButton) findViewById(R.id.btnvDate);
            btnvDate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btnvDate";
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
            Connection.MessageBox(FPIMonitoring.this, e.getMessage());
            return;
        }
    }

    private void DataSave() {
        try {

           /* DV = Global.DateValidate(dtpvDate.getText().toString());
            if(DV.length()!=0 & secvDate.isShown())
            {
                Connection.MessageBox(FPIMonitoring.this, DV);
                dtpvDate.requestFocus();
                return;
            }
            else*/ /*if(spnfpaCode.getSelectedItemPosition()==0  & secfpaCode.isShown())
            {
                Connection.MessageBox(FPIMonitoring.this, "Required field:পরিবার কল্যাণ সহকারীর নাম .");
                spnfpaCode.requestFocus();
                return;
            }*/
        /*    if(txtfpaUnit.getText().toString().length()==0 & secfpaUnit.isShown())
            {
                Connection.MessageBox(FPIMonitoring.this, "Required field:ইউনিট.");
                txtfpaUnit.requestFocus();
                return;
            }
            else*/
           /* if (spnfpaVill.getSelectedItemPosition() == 0 & secfpaVill.isShown()) {
                Connection.MessageBox(FPIMonitoring.this, "Required field:গ্রাম.");
                spnfpaVill.requestFocus();
                return;
            } else*/
            if (spnfpaAdvance.getSelectedItemPosition() == 0 & secfpaAdvance.isShown()) {
                Connection.MessageBox(FPIMonitoring.this, "Required field:কর্মী অগ্রিম কর্মসূচীর কোন পর্যায়ে .");
                spnfpaAdvance.requestFocus();
                return;
            } /*else if (!rdoneedItems11.isChecked() & !rdoneedItems12.isChecked() & secneedItems1.isShown()) {
                Connection.MessageBox(FPIMonitoring.this, "Select anyone options from needItems1.");
                rdoneedItems11.requestFocus();
                return;
            } else if (!rdoneedItems21.isChecked() & !rdoneedItems22.isChecked() & secneedItems2.isShown()) {
                Connection.MessageBox(FPIMonitoring.this, "Select anyone options from needItems2.");
                rdoneedItems21.requestFocus();
                return;
            } else if (!rdoneedItems31.isChecked() & !rdoneedItems32.isChecked() & secneedItems3.isShown()) {
                Connection.MessageBox(FPIMonitoring.this, "Select anyone options from needItems3.");
                rdoneedItems31.requestFocus();
                return;
            } else if (!rdoneedItems41.isChecked() & !rdoneedItems42.isChecked() & secneedItems4.isShown()) {
                Connection.MessageBox(FPIMonitoring.this, "Select anyone options from needItems4.");
                rdoneedItems41.requestFocus();
                return;
            } else if (!rdoneedItems51.isChecked() & !rdoneedItems52.isChecked() & secneedItems5.isShown()) {
                Connection.MessageBox(FPIMonitoring.this, "Select anyone options from needItems5.");
                rdoneedItems51.requestFocus();
                return;
            } else if (!rdoneedItems61.isChecked() & !rdoneedItems62.isChecked() & secneedItems6.isShown()) {
                Connection.MessageBox(FPIMonitoring.this, "Select anyone options from needItems6.");
                rdoneedItems61.requestFocus();
                return;
            } else if (!rdoneedItems71.isChecked() & !rdoneedItems72.isChecked() & secneedItems7.isShown()) {
                Connection.MessageBox(FPIMonitoring.this, "Select anyone options from needItems7.");
                rdoneedItems71.requestFocus();
                return;
            } else if (!rdoneedItems81.isChecked() & !rdoneedItems82.isChecked() & secneedItems8.isShown()) {
                Connection.MessageBox(FPIMonitoring.this, "Select anyone options from needItems8.");
                rdoneedItems81.requestFocus();
            }*/
            String SQL = "";

            if (!C.Existence("Select vDate,fpaCode,fpaUnit,startTime,endTime,userId,enDt,upload from " + TableName + "  Where vDate='" + Global.DateConvertYMD(dtpvDate.getText().toString()) + "' AND  fpaCode = '" + Global.Left(spnfpaCode.getSelectedItem().toString(), 5)  + "'")) //+ "' AND  fpaUnit = '" + Global.Left(txtfpaUnit.getText().toString(), 2)
            {
                SQL = "Insert into " + TableName + "(vDate,fpaCode,fpaUnit,startTime,endTime,userId,enDt,upload)Values('" + Global.DateConvertYMD(dtpvDate.getText().toString()) + "','" + Global.Left(spnfpaCode.getSelectedItem().toString(), 5) + "','" +(txtfpaUnit.getText().length() == 0 ? "" :  Global.Left(txtfpaUnit.getText().toString(), 2)) + "','" + StartTime + "','" + g.CurrentTime24() + "','" + g.getUserID() + "','" + Global.DateTimeNowYMDHMS() + "','2')";
                C.Save(SQL);
            }

            SQL = "Update " + TableName + " Set Upload='2',";
            SQL += "vDate = '" + Global.DateConvertYMD(dtpvDate.getText().toString()) + "',";
            SQL += "fpaCode = '" + Global.Left(spnfpaCode.getSelectedItem().toString(), 5) + "',";
            SQL += "fpaUnit = '" + (txtfpaUnit.getText().length() == 0 ? "" :  Global.Left(txtfpaUnit.getText().toString(), 2)) + "',";
            SQL += "fpaVill = '" + Global.Left(spnfpaVill.getSelectedItem().toString(), 4) + "',";
            SQL += "fpaAdvance = '" + (spnfpaAdvance.getSelectedItemPosition() == 0 ? "" : Global.Left(spnfpaAdvance.getSelectedItem().toString(), 2)) + "',";

            RadioButton rbneedItems1 = (RadioButton) findViewById(rdogrpneedItems1.getCheckedRadioButtonId());
            SQL += "needItems1 = '" + (rbneedItems1 == null ? "" : (Global.Left(rbneedItems1.getText().toString(), 1))) + "',";
            RadioButton rbneedItems2 = (RadioButton) findViewById(rdogrpneedItems2.getCheckedRadioButtonId());
            SQL += "needItems2 = '" + (rbneedItems2 == null ? "" : (Global.Left(rbneedItems2.getText().toString(), 1))) + "',";
            RadioButton rbneedItems3 = (RadioButton) findViewById(rdogrpneedItems3.getCheckedRadioButtonId());
            SQL += "needItems3 = '" + (rbneedItems3 == null ? "" : (Global.Left(rbneedItems3.getText().toString(), 1))) + "',";
            RadioButton rbneedItems4 = (RadioButton) findViewById(rdogrpneedItems4.getCheckedRadioButtonId());
            SQL += "needItems4 = '" + (rbneedItems4 == null ? "" : (Global.Left(rbneedItems4.getText().toString(), 1))) + "',";
            RadioButton rbneedItems5 = (RadioButton) findViewById(rdogrpneedItems5.getCheckedRadioButtonId());
            SQL += "needItems5 = '" + (rbneedItems5 == null ? "" : (Global.Left(rbneedItems5.getText().toString(), 1))) + "',";
            RadioButton rbneedItems6 = (RadioButton) findViewById(rdogrpneedItems6.getCheckedRadioButtonId());
            SQL += "needItems6 = '" + (rbneedItems6 == null ? "" : (Global.Left(rbneedItems6.getText().toString(), 1))) + "',";
            RadioButton rbneedItems7 = (RadioButton) findViewById(rdogrpneedItems7.getCheckedRadioButtonId());
            SQL += "needItems7 = '" + (rbneedItems7 == null ? "" : (Global.Left(rbneedItems7.getText().toString(), 1))) + "',";
            RadioButton rbneedItems8 = (RadioButton) findViewById(rdogrpneedItems8.getCheckedRadioButtonId());
            SQL += "needItems8 = '" + (rbneedItems8 == null ? "" : (Global.Left(rbneedItems8.getText().toString(), 1))) + "'";
            SQL += "  Where vDate='" + Global.DateConvertYMD(dtpvDate.getText().toString()) + "' AND  fpaCode = '" + Global.Left(spnfpaCode.getSelectedItem().toString(), 5)  + "'";
            C.Save(SQL);
            Connection.MessageBox(FPIMonitoring.this, "Saved Successfully");

            finish();
        } catch (Exception e) {
            Connection.MessageBox(FPIMonitoring.this, e.getMessage());
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
            return Integer.parseInt(C.ReturnSingleValue("Select count(*)total from fpiMonitoring where fpaCode='" + Global.Left(spnfpaCode.getSelectedItem().toString(), 5) + "'"));
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

                String SQL = "Select strftime('%d/%m/%Y', date(vDate)) as visitDate  from fpiMonitoring where fpaCode='" + Global.Left(spnfpaCode.getSelectedItem().toString(), 5) + "' order by vDate  desc";//date(imudate) asc

                try {
                    Cursor cur = C.ReadData(SQL);
                    cur.moveToFirst();

                    totalRec = cur.getCount();
                    vcode = new String[2][totalRec];
                    int i = 0;
                    while (!cur.isAfterLast()) {
                        vcode[0][i] = "পরিদর্শন " + String.valueOf(i + 1);//String.valueOf(cur.getString(cur.getColumnIndex("imucode")));
                        vcode[1][i] = cur.getString(cur.getColumnIndex("visitDate"));


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
                            AlertDialog.Builder adb = new AlertDialog.Builder(FPIMonitoring.this);


                            adb.setTitle("Close");
                            adb.setMessage("আপনি কি পরিদর্শন বাতিল করতে  চান[হ্যাঁ/না]?");
                            adb.setNegativeButton("না", null);
                            adb.setPositiveButton("হ্যাঁ", new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String visitDate = String.valueOf(vcode[1][position]);
                                    if ((Global.DateDifferenceDays(Global.DateNowDMY(), Global.DateConvertDMY(visitDate.toString())) > 3)) {
                                        Connection.MessageBox(FPIMonitoring.this, "পরিদর্শনের  তথ্য ৩ দিন পর বাতিল করা প্রযোজ্য না");
                                        return;
                                    } else {
                                        String Item = String.valueOf(vcode[0][position]);
                                        String visit = String.valueOf(vcode[1][position]);
                                        String dwl = "DELETE FROM fpiMonitoring where fpaCode='" + Global.Left(spnfpaCode.getSelectedItem().toString(), 5)+ "' and strftime('%d/%m/%Y', date(VDate))='" + visit+ "'";
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

                                dtpvDate.setText(vcode[1][position]);

                                DataSearch(vcode[1][position]);


                            }
                            // g.setImuCode(vcode[1][position]);
                        }
                    });
                } catch (Exception ex) {
                    Connection.MessageBox(FPIMonitoring.this, ex.getMessage());
                }

            }
            return MyView;
        }

    }

    private void DataSearch(String vDate) {
        try {

            RadioButton rb;
            Cursor cur = C.ReadData("Select * from " + TableName + "  Where strftime('%d/%m/%Y', date(vDate))='" + vDate + "'");
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                dtpvDate.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("vDate"))));
                spnfpaCode.setSelection(Global.SpinnerItemPosition(spnfpaCode, 5, cur.getString(cur.getColumnIndex("fpaCode"))));
                txtfpaUnit.setText(cur.getString(cur.getColumnIndex("fpaUnit")));
                // spnfpaVill.setSelection(Global.SpinnerItemPosition(spnfpaVill, 4 ,cur.getString(cur.getColumnIndex("fpaVill"))));

                spnfpaVill.setAdapter(C.getArrayAdapterMultiline("select (CASE WHEN Length(MOUZAID)=1 THEN substr('00' ||MOUZAID, -3, 3)||VILLAGEID||'-'||VILLAGENAME \n" +
                        "WHEN Length(MOUZAID)=2 THEN substr('0' ||MOUZAID, -3, 3)||VILLAGEID||'-'||VILLAGENAME WHEN Length(MOUZAID)=3 THEN substr('0' ||MOUZAID, -3, 3)||VILLAGEID||'-'||VILLAGENAME                        \n" +
                        " ELSE '' END) as VILLAGENAME\n" +
                        "from Village where MOUZAID='" + Global.Left(cur.getString(cur.getColumnIndex("fpaVill")), 3) + "' and VILLAGEID='" + Global.Right(cur.getString(cur.getColumnIndex("fpaVill")), 1) + "'"));
                spnfpaAdvance.setSelection(Global.SpinnerItemPosition(spnfpaAdvance, 2, cur.getString(cur.getColumnIndex("fpaAdvance"))));
                for (int i = 0; i < rdogrpneedItems1.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpneedItems1.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("needItems1"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpneedItems2.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpneedItems2.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("needItems2"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpneedItems3.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpneedItems3.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("needItems3"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpneedItems4.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpneedItems4.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("needItems4"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpneedItems5.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpneedItems5.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("needItems5"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpneedItems6.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpneedItems6.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("needItems6"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpneedItems7.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpneedItems7.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("needItems7"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpneedItems8.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpneedItems8.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("needItems8"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(FPIMonitoring.this, e.getMessage());
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


            dtpDate = (EditText) findViewById(R.id.dtpvDate);
            if (VariableID.equals("btnvDate")) {
                dtpDate = (EditText) findViewById(R.id.dtpvDate);
            }
            dtpDate.setText(new StringBuilder()
                    .append(Global.Right("00" + mDay, 2)).append("/")
                    .append(Global.Right("00" + mMonth, 2)).append("/")
                    .append(mYear));
        }
    };
    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
            hour = selectedHour;
            minute = selectedMinute;
            EditText tpTime;


            //  tpTime.setText(new StringBuilder().append(Global.Right("00"+hour,2)).append(":").append(Global.Right("00"+minute,2)));

        }
    };
}