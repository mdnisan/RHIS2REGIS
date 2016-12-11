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
import android.widget.DatePicker;
import android.widget.EditText;
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

public class Household extends Activity {
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
        AlertDialog.Builder adb = new AlertDialog.Builder(Household.this);
        switch (item.getItemId()) {
            case R.id.menuClose:
                adb.setTitle("Close");
                adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান[Yes/No]?");
                adb.setNegativeButton("No", null);
                adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        Intent f1 = new Intent(getApplicationContext(), HouseholdIndex.class);
                        startActivity(f1);
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

    LinearLayout seclblHH;
    LinearLayout secwardNew;
    TextView VlblwardNew;
    Spinner spnwardNew;
    LinearLayout secwardOld;
    TextView VlblwardOld;
    Spinner spnwardOld;
    LinearLayout secMouza;
    TextView VlblMouza;
    Spinner spnMouza;
    LinearLayout secFWAUnit;
    TextView VlblFWAUnit;
    Spinner spnFWAUnit;
    LinearLayout secVill;
    TextView VlblVill;
    Spinner spnVill;
    LinearLayout secEPIBlock;
    TextView VlblEPIBlock;
    Spinner spnEPIBlock;
    LinearLayout secPAddr;
    TextView VlblPAddr;
    RadioGroup rdogrpPAddr;

    RadioButton rdoPAddr1;
    RadioButton rdoPAddr2;
    LinearLayout secPermaAddress;
    TextView VlblPermaAddress;
    EditText txtPermaAddress;
    LinearLayout secHHNo;
    TextView VlblHHNo;
    TextView txtHHNo;
    LinearLayout secReligion;
    TextView VlblReligion;
    Spinner spnReligion;
    LinearLayout secVGFCard;
    TextView VlblVGFCard;
    //CheckBox chkVGFCard;
    RadioGroup rdogrpVGFCard;

    RadioButton rdoVGFCard1;
    RadioButton rdoVGFCard2;

    String StartTime;

    String Div;
    String Dist;
    String Upz;
    String UN;
    String Vill;
    String HHNo;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.household);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();

            TableName = "Household";


            TextView lblVillage = (TextView) findViewById(R.id.lblVillageName);
            lblVillage.setText("গ্রামঃ  " + g.getMouza() + "-" + g.getVillage() + ", " + g.getVillageName());

            txtHHNo = (TextView) findViewById(R.id.txtHHNo);
            if (g.getHouseholdNo().length() == 0)
                txtHHNo.setText(HouseholdNumber());
            else
                txtHHNo.setText(g.getHouseholdNo());

            secwardNew = (LinearLayout) findViewById(R.id.secwardNew);
            VlblwardNew = (TextView) findViewById(R.id.VlblwardNew);
            spnwardNew = (Spinner) findViewById(R.id.spnwardNew);
            List<String> listwardNew = new ArrayList<String>();

            listwardNew.add("");
            listwardNew.add("01-Ward-1");
            listwardNew.add("02-Ward-2");
            listwardNew.add("03-Ward-3");
            listwardNew.add("04-Ward-4");
            listwardNew.add("05-Ward-5");
            listwardNew.add("06-Ward-6");
            listwardNew.add("07-Ward-7");
            listwardNew.add("08-Ward-8");
            listwardNew.add("09-Ward-9");
            ArrayAdapter<String> adptrwardNew = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listwardNew);
            spnwardNew.setAdapter(adptrwardNew);

            spnwardOld = (Spinner) findViewById(R.id.spnwardOld);
            List<String> listwardOld = new ArrayList<String>();

            listwardOld.add("");
            listwardOld.add("01-Ward-1");
            listwardOld.add("02-Ward-2");
            listwardOld.add("03-Ward-3");
            ArrayAdapter<String> adptrwardOld = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listwardOld);
            spnwardOld.setAdapter(adptrwardOld);

            secFWAUnit = (LinearLayout) findViewById(R.id.secFWAUnit);
            VlblFWAUnit = (TextView) findViewById(R.id.VlblFWAUnit);
            spnFWAUnit = (Spinner) findViewById(R.id.spnFWAUnit);
            List<String> listFWAUnit = new ArrayList<String>();

            listFWAUnit.add("");
            listFWAUnit.add("11-1KA");
            listFWAUnit.add("12-1KHA");
            listFWAUnit.add("13-1GA");
            listFWAUnit.add("14-1GHA");
            listFWAUnit.add("21-2KA");
            listFWAUnit.add("22-2KHA");
            listFWAUnit.add("23-2GA");
            listFWAUnit.add("24-2GHA");
            listFWAUnit.add("31-3KA");
            listFWAUnit.add("32-3KHA");
            listFWAUnit.add("33-3GA");
            listFWAUnit.add("34-3GHA");

            ArrayAdapter<String> adptrFWAUnit = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listFWAUnit);
            spnFWAUnit.setAdapter(adptrFWAUnit);

            secEPIBlock = (LinearLayout) findViewById(R.id.secEPIBlock);
            VlblEPIBlock = (TextView) findViewById(R.id.VlblEPIBlock);
            spnEPIBlock = (Spinner) findViewById(R.id.spnEPIBlock);
            List<String> listEPIBlock = new ArrayList<String>();

            listEPIBlock.add("");
            listEPIBlock.add("01-ক-১");
            listEPIBlock.add("02-ক-২");
            listEPIBlock.add("03-খ-১");
            listEPIBlock.add("04-খ-২");
            listEPIBlock.add("05-গ-১");
            listEPIBlock.add("06-গ-২");
            listEPIBlock.add("07-ঘ-১");
            listEPIBlock.add("08-ঘ-২");

            ArrayAdapter<String> adptrEPIBlock = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listEPIBlock);
            spnEPIBlock.setAdapter(adptrEPIBlock);

            secPAddr = (LinearLayout) findViewById(R.id.secPAddr);
            VlblPAddr = (TextView) findViewById(R.id.VlblPAddr);
            rdogrpPAddr = (RadioGroup) findViewById(R.id.rdogrpPAddr);

            rdoPAddr1 = (RadioButton) findViewById(R.id.rdoPAddr1);
            rdoPAddr2 = (RadioButton) findViewById(R.id.rdoPAddr2);
            secPermaAddress = (LinearLayout) findViewById(R.id.secPermaAddress);
            VlblPermaAddress = (TextView) findViewById(R.id.VlblPermaAddress);
            txtPermaAddress = (EditText) findViewById(R.id.txtPermaAddress);
            rdogrpPAddr.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int radioButtonID) {
                    RadioButton rb = (RadioButton) findViewById(rdogrpPAddr.getCheckedRadioButtonId());
                    if (rb == null) return;
                    String rbData = Global.Left(rb.getText().toString(), 1);
                    if (rbData.equalsIgnoreCase("2")) {
                        secPermaAddress.setVisibility(View.VISIBLE);
                    } else {
                        secPermaAddress.setVisibility(View.GONE);
                        txtPermaAddress.setText("");
                    }

                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });

            VlblHHNo = (TextView) findViewById(R.id.VlblHHNo);

            secReligion = (LinearLayout) findViewById(R.id.secReligion);
            VlblReligion = (TextView) findViewById(R.id.VlblReligion);
            spnReligion = (Spinner) findViewById(R.id.spnReligion);
            List<String> listReligion = new ArrayList<String>();

            listReligion.add("");
            listReligion.add("1-মুসলমান");
            listReligion.add("2-হিন্দু");
            listReligion.add("3-বৌদ্ধ");
            listReligion.add("4-খ্রীস্টান");
            listReligion.add("8-Refuse to disclose");
            listReligion.add("9-Not a believer");
            listReligion.add("0-অন্যান্য");
            ArrayAdapter<String> adptrReligion = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listReligion);
            spnReligion.setAdapter(adptrReligion);

            secVGFCard = (LinearLayout) findViewById(R.id.secVGFCard);
            VlblVGFCard = (TextView) findViewById(R.id.VlblVGFCard);
            //chkVGFCard=(CheckBox) findViewById(R.id.chkVGFCard);
            rdogrpVGFCard = (RadioGroup) findViewById(R.id.rdogrpVGFCard);

            rdoVGFCard1 = (RadioButton) findViewById(R.id.rdoVGFCard1);
            rdoVGFCard2 = (RadioButton) findViewById(R.id.rdoVGFCard2);


            secPermaAddress.setVisibility(View.GONE);

            DataSearch(g.getDistrict(), g.getUpazila(), g.getUnion(), g.getMouza(), g.getVillage(), txtHHNo.getText().toString());

            Button cmdSave = (Button) findViewById(R.id.cmdSave);
            if (g.getCallFrom().equals("oldh")) {
                //cmdSave.setText("তথ্য সংরক্ষণ   -> সদস্যের তালিকা");
            }

            cmdSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    DataSave();
                }
            });
        } catch (Exception e) {
            Connection.MessageBox(Household.this, e.getMessage());
            return;
        }
    }

    private String HouseholdNumber() {
        String SQL = "";
        SQL = "Select (ifnull(max(cast(HHNo as int)),10000)+1)MaxHH from Household";
        SQL += " where";
        SQL += " dist='" + g.getDistrict() + "' and upz='" + g.getUpazila() + "' and un='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and vill='" + g.getVillage() + "'";

        String HHNo = C.ReturnSingleValue(SQL);

        return HHNo;
    }

    private void DataSave() {
        try {

            String DV = "";
        /*
        if(spnwardNew.getSelectedItemPosition()==0  & secwardNew.isShown())
          {
            Connection.MessageBox(Household.this, "Required field:ওয়ার্ড(নতুন).");
            spnwardNew.requestFocus(); 
            return;    
          }
        else if(spnwardOld.getSelectedItemPosition()==0  & secwardOld.isShown())
          {
            Connection.MessageBox(Household.this, "Required field:ওয়ার্ড(পুরাতন).");
            spnwardOld.requestFocus(); 
            return;    
          }
        else if(spnMouza.getSelectedItemPosition()==0  & secMouza.isShown())
          {
            Connection.MessageBox(Household.this, "Required field:মওজা/মহল্লা.");
            spnMouza.requestFocus(); 
            return;    
          }*/
            if (spnFWAUnit.getSelectedItemPosition() == 0 & secFWAUnit.isShown()) {
                Connection.MessageBox(Household.this, "তালিকা থেকে সঠিক FWA ইউনিট সিলেক্ট করুন.");
                spnFWAUnit.requestFocus();
                return;
            } else if (!rdoPAddr1.isChecked() & !rdoPAddr2.isChecked() & secPAddr.isShown()) {
                Connection.MessageBox(Household.this, "এটা কি আপনার স্থায়ী ঠিকানা কিনা এ তথ্য সিলেক্ট করতে হবে");
                rdoPAddr1.requestFocus();
                return;
            } else if (txtPermaAddress.getText().toString().length() == 0 & secPermaAddress.isShown()) {
                Connection.MessageBox(Household.this, "সঠিক স্থায়ী ঠিকানা লিপিবদ্ধ করুন।");
                txtPermaAddress.requestFocus();
                return;
            } else if (spnReligion.getSelectedItemPosition() == 0 & secReligion.isShown()) {
                Connection.MessageBox(Household.this, "তালিকা থেকে সঠিক ধর্ম  সিলেক্ট করুন.");
                spnReligion.requestFocus();
                return;
            } else if (!rdoVGFCard1.isChecked() & !rdoVGFCard2.isChecked()) {
                Connection.MessageBox(Household.this, "ভি জি এফ কার্ড আছে কি, এ তথ্য সিলেক্ট করতে হবে");
                rdoVGFCard1.requestFocus();
                return;
            }

            String SQL = "";

            if (!C.Existence("Select Div,Dist,Upz,UN,Mouza,Vill,HHNo from " + TableName + "  Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + txtHHNo.getText().toString() + "'")) {
                SQL = "Insert into " + TableName + "(Div,Dist,Upz,UN,Mouza,Vill,HHNo,StartTime,EndTime,UserId,EnDt,Upload)Values('" + g.getDivision() + "','" + g.getDistrict() + "','" + g.getUpazila() + "','" + g.getUnion() + "','" + g.getMouza() + "','" + g.getVillage() + "','" + txtHHNo.getText() + "','" + StartTime + "','" + g.CurrentTime24() + "','" + g.getUserID() + "','" + Global.DateTimeNowYMDHMS() + "','2')";
                C.Save(SQL);
            }

            SQL = "Update " + TableName + " Set ";
            SQL += "wardNew = '" + (spnwardNew.getSelectedItemPosition() == 0 ? "" : Global.Left(spnwardNew.getSelectedItem().toString(), 2)) + "',";
            SQL += "wardOld = '" + (spnwardOld.getSelectedItemPosition() == 0 ? "" : Global.Left(spnwardOld.getSelectedItem().toString(), 2)) + "',";
            SQL += "FWAUnit = '" + (spnFWAUnit.getSelectedItemPosition() == 0 ? "" : Global.Left(spnFWAUnit.getSelectedItem().toString(), 2)) + "',";
            SQL += "EPIBlock = '" + (spnEPIBlock.getSelectedItemPosition() == 0 ? "" : Global.Left(spnEPIBlock.getSelectedItem().toString(), 2)) + "',";
            RadioButton rbPAddr = (RadioButton) findViewById(rdogrpPAddr.getCheckedRadioButtonId());
            SQL += "PAddr = '" + (rbPAddr == null ? "" : (Global.Left(rbPAddr.getText().toString(), 1))) + "',";
            SQL += "PermaAddress = '" + txtPermaAddress.getText().toString() + "',";
            SQL += "Religion = '" + (spnReligion.getSelectedItemPosition() == 0 ? "" : Global.Left(spnReligion.getSelectedItem().toString(), 1)) + "',";
            RadioButton rbVGFCard = (RadioButton) findViewById(rdogrpVGFCard.getCheckedRadioButtonId());
            SQL += "VGFCard = '" + (rbVGFCard == null ? "" : (Global.Left(rbVGFCard.getText().toString(), 1))) + "'";


            SQL += "  Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + txtHHNo.getText().toString() + "'";
            C.Save(SQL);
            Connection.MessageBox(Household.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");

            g.setHouseholdNo(txtHHNo.getText().toString());
            finish();
            g.setSerialNo("");
            if (g.getCallFrom().equals("newh")) {
                Intent f1 = new Intent(getApplicationContext(), MemberForm.class);
                startActivity(f1);
            } else if (g.getCallFrom().equals("oldh")) {
                Intent f2 = new Intent(getApplicationContext(), MemberList.class);
                startActivity(f2);
            }

        } catch (Exception e) {
            Connection.MessageBox(Household.this, e.getMessage());
            return;
        }
    }

    private void DataSearch(String Dist, String Upz, String UN, String Mouza, String Vill, String HHNo) {
        try {
            RadioButton rb;
            Cursor cur = C.ReadData("Select * from " + TableName + "  Where Dist='" + Dist + "' and Upz='" + Upz + "' and UN='" + UN + "' and Mouza='" + Mouza + "' and Vill='" + Vill + "' and HHNo='" + HHNo + "'");
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                spnwardNew.setSelection(Global.SpinnerItemPosition(spnwardNew, 2, cur.getString(cur.getColumnIndex("wardNew"))));
                spnwardOld.setSelection(Global.SpinnerItemPosition(spnwardOld, 2, cur.getString(cur.getColumnIndex("wardOld"))));
                spnFWAUnit.setSelection(Global.SpinnerItemPosition(spnFWAUnit, 2, cur.getString(cur.getColumnIndex("FWAUnit"))));
                spnEPIBlock.setSelection(Global.SpinnerItemPosition(spnEPIBlock, 2, cur.getString(cur.getColumnIndex("EPIBlock"))));
                for (int i = 0; i < rdogrpPAddr.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpPAddr.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("PAddr"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                txtPermaAddress.setText(cur.getString(cur.getColumnIndex("PermaAddress")));
                spnReligion.setSelection(Global.SpinnerItemPosition(spnReligion, 1, cur.getString(cur.getColumnIndex("Religion"))));

                for (int i = 0; i < rdogrpVGFCard.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpVGFCard.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("VGFCard"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }


                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(Household.this, e.getMessage());
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


     /*dtpDate.setText(new StringBuilder()
     .append(Global.Right("00"+mDay,2)).append("/")
     .append(Global.Right("00"+mMonth,2)).append("/")
     .append(mYear));*/
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