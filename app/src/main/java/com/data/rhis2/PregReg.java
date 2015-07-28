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
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
 * Created by user on 10/03/2015.
 */
public class PregReg extends Activity {
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
        AlertDialog.Builder adb = new AlertDialog.Builder(PregReg.this);
        switch (item.getItemId()) {
            case R.id.menuClose:
                adb.setTitle("Close");
                adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান[Yes/No]?");
                adb.setNegativeButton("No", null);
                adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        Intent f2 = new Intent(getApplicationContext(), ELCOForm.class);
                        startActivity(f2);
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
    private String TableANC;

    private String TableNameElcoVisit;

    TextView txtHealthID;

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

    LinearLayout seclblH1;
    TextView lblHlblH;
    TextView lblHealthID;
    EditText txtHHNo;

    LinearLayout secSNo;
    TextView VlblSNo;
    TextView txtSNo;
    EditText txtELCONo;

    LinearLayout secName;
    TextView VlblName;
    TextView txtName;

    LinearLayout secAge;
    TextView VlblAge;
    TextView txtAge;

    LinearLayout secLMP;
    TextView VlblLMP;
    EditText dtpLMP;
    EditText txtLiveSonDau;
    ImageButton btnLMP;

    LinearLayout secPgn;
    TextView VlblPgn;
    Spinner spnPgn;

    LinearLayout secEDD;
    TextView VlblEDD;
    EditText dtpEDD;


    LinearLayout secAgeL;
    TextView VlblAgeL;
    EditText txtAgeM;
    EditText txtAgeY;

    LinearLayout secVDate;
    TextView VlblVDate;
    EditText dtpVDate;
    ImageButton btnVDate;

    LinearLayout secIron;
    TextView VlblIron;
    RadioGroup rdogrpIron;
    RadioButton rdoIron1;
    RadioButton rdoIron2;

    LinearLayout secIronUnit;
    TextView VlblIronQty;
    EditText txtIronQty;
    TextView VlblIronUnit;
    Spinner spnIronUnit;
    Spinner spnANCSource;

    LinearLayout secMiso;
    TextView VlblMiso;
    RadioGroup rdogrpMiso;
    RadioButton rdoMiso1;
    RadioButton rdoMiso2;

    LinearLayout secMisoUnit;
    TextView VlblMisoQty;
    EditText txtMisoQty;
    TextView VlblMisoUnit;
    Spinner spnMisoUnit;

    LinearLayout secANCVisit;


    String StartTime;
    Button cmdSave;
    LinearLayout secPregVisit;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.pregnancy_register);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();

            TableName = "PregWomen";
            TableANC = "ancService";
            TableNameElcoVisit = "ELCOVisit";

            secANCVisit = (LinearLayout) findViewById(R.id.secANCVisit);
            secPregVisit = (LinearLayout) findViewById(R.id.secPregVisit);

            txtHealthID = (TextView) findViewById(R.id.txtHealthID);
            txtHHNo = (EditText) findViewById(R.id.txtHHNo);

            VlblSNo = (TextView) findViewById(R.id.VlblSNo);
            txtSNo = (TextView) findViewById(R.id.txtSNo);
            txtELCONo = (EditText) findViewById(R.id.txtELCONo);
            txtELCONo.setEnabled(false);

            secName = (LinearLayout) findViewById(R.id.secName);
            VlblName = (TextView) findViewById(R.id.VlblName);
            txtName = (TextView) findViewById(R.id.txtName);

            secAge = (LinearLayout) findViewById(R.id.secAge);
            VlblAge = (TextView) findViewById(R.id.VlblAge);
            txtAge = (TextView) findViewById(R.id.txtAge);

            secLMP = (LinearLayout) findViewById(R.id.secLMP);
            VlblLMP = (TextView) findViewById(R.id.VlblLMP);
            dtpLMP = (EditText) findViewById(R.id.dtpLMP);
            txtLiveSonDau = (EditText) findViewById(R.id.txtLiveSonDau);
            btnLMP = (ImageButton) findViewById(R.id.btnLMP);

            secPgn = (LinearLayout) findViewById(R.id.secPgn);
            VlblPgn = (TextView) findViewById(R.id.VlblPgn);
            spnPgn = (Spinner) findViewById(R.id.spnPgn);

            secEDD = (LinearLayout) findViewById(R.id.secEDD);
            VlblEDD = (TextView) findViewById(R.id.VlblEDD);
            dtpEDD = (EditText) findViewById(R.id.dtpEDD);

            secAgeL = (LinearLayout) findViewById(R.id.secAgeL);
            VlblAgeL = (TextView) findViewById(R.id.VlblAgeL);
            txtAgeM = (EditText) findViewById(R.id.txtAgeMo);
            txtAgeY = (EditText) findViewById(R.id.txtAgeY);

            ////////Visit
            secIron = (LinearLayout) findViewById(R.id.secIron);
            VlblIron = (TextView) findViewById(R.id.VlblIron);
            rdogrpIron = (RadioGroup) findViewById(R.id.rdogrpIron);
            rdoIron1 = (RadioButton) findViewById(R.id.rdoIron1);
            rdoIron2 = (RadioButton) findViewById(R.id.rdoIron2);


            secIronUnit = (LinearLayout) findViewById(R.id.secIronUnit);
            secIronUnit.setVisibility(View.GONE);
            VlblIronQty = (TextView) findViewById(R.id.VlblIronQty);
            txtIronQty = (EditText) findViewById(R.id.txtIronQty);
            VlblIronUnit = (TextView) findViewById(R.id.VlblIronUnit);
            spnIronUnit = (Spinner) findViewById(R.id.spnIronUnit);
            spnANCSource= (Spinner) findViewById(R.id.spnANCSource);

            rdogrpIron.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup arg0, int id) {
                    if (id == R.id.rdoIron1) {
                        secIronUnit.setVisibility(View.VISIBLE);
                    } else {
                        secIronUnit.setVisibility(View.GONE);
                        spnIronUnit.setSelection(0);
                        txtIronQty.setText("");
                    }
                }
            });

            secMiso = (LinearLayout) findViewById(R.id.secMiso);
            VlblMiso = (TextView) findViewById(R.id.VlblMiso);
            rdogrpMiso = (RadioGroup) findViewById(R.id.rdogrpMiso);
            rdoMiso1 = (RadioButton) findViewById(R.id.rdoMiso1);
            rdoMiso2 = (RadioButton) findViewById(R.id.rdoMiso2);

            secMisoUnit = (LinearLayout) findViewById(R.id.secMisoUnit);
            secMisoUnit.setVisibility(View.GONE);
            VlblMisoQty = (TextView) findViewById(R.id.VlblMisoQty);
            txtMisoQty = (EditText) findViewById(R.id.txtMisoQty);
            VlblMisoUnit = (TextView) findViewById(R.id.VlblMisoUnit);
            spnMisoUnit = (Spinner) findViewById(R.id.spnMisoUnit);
            rdogrpMiso.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup arg0, int id) {
                    if (id == R.id.rdoMiso1) {
                        secMisoUnit.setVisibility(View.VISIBLE);
                    } else {
                        secMisoUnit.setVisibility(View.GONE);
                        spnMisoUnit.setSelection(0);
                        txtMisoQty.setText("");
                    }
                }
            });

            List<String> listPgn = new ArrayList<String>();
            listPgn.add("");
            listPgn.add("১ম");
            listPgn.add("২য় ");
            listPgn.add("৩য়");
            listPgn.add("৪র্থ");
            listPgn.add("৫ম");
            listPgn.add("৬স্ট");
            listPgn.add("৭ম");
            listPgn.add("৮ম");
            listPgn.add("৯ম");
            listPgn.add("১০ম ");
            ArrayAdapter<String> adptrspnPgn = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listPgn);
            spnPgn.setAdapter(adptrspnPgn);

            List<String> listPillUnit = new ArrayList<String>();
            listPillUnit.add("");
            listPillUnit.add("চক্র");
            listPillUnit.add("পিছ");
            listPillUnit.add("ডোজ");
            ArrayAdapter<String> adptrMethodUnit = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listPillUnit);
            spnIronUnit.setAdapter(adptrMethodUnit);
            spnMisoUnit.setAdapter(adptrMethodUnit);

            List<String> listAncSource = new ArrayList<String>();
            listAncSource.add("");
            listAncSource.add("সরকারি");
            listAncSource.add("বেসরকারি");
            ArrayAdapter<String> adptrAncService = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listAncSource);
            spnANCSource.setAdapter(adptrAncService);




            secVDate = (LinearLayout) findViewById(R.id.secVDate);
            VlblVDate = (TextView) findViewById(R.id.VlblVDate);
            dtpVDate = (EditText) findViewById(R.id.dtpVDate);
            dtpVDate.setText(Global.DateNowDMY());
            btnVDate = (ImageButton) findViewById(R.id.btnVDate);
            btnLMP.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btnLMP";
                    showDialog(DATE_DIALOG);
                }
            });

            btnVDate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btnVDate";
                    showDialog(DATE_DIALOG);
                }
            });


            secANCVisit.setVisibility(View.GONE);
            String PGN = "";
            /*String SQL = "";
            SQL = "select  DOLMP,DOEDD,PrePregTimes,LCAge from PregWomen";
            SQL += " Where Dist='"+ g.getDistrict() +"' and Upz='"+ g.getUpazila() +"' and UN='"+  g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and Vill='"+ g.getVillage() +"' and HHNo='"+ g.getHouseholdNo() +"' and SNo='"+ g.getSerialNo() +"' and ";
            SQL += "PGN=(select '0'||cast(max(PGN) as string) from PregWomen Where Dist='"+ g.getDistrict() +"' and Upz='"+ g.getUpazila() +"' and UN='"+  g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and Vill='"+ g.getVillage() +"' and HHNo='"+ g.getHouseholdNo() +"' and SNo='"+ g.getSerialNo() +"' and PGN=(select  max(PGN) from PregWomen Where Dist='"+ g.getDistrict() +"' and Upz='"+ g.getUpazila() +"' and UN='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and Vill='"+ g.getVillage() +"'  and ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"' and  HHNo='"+ g.getHouseholdNo() +"' and SNo='"+ SNo +"'))";
            */
            PregWomenSearch();
            //DataSearch(g.getDistrict(), g.getUpazila(), g.getUnion(), g.getMouza(), g.getVillage(), g.getHouseholdNo(), g.getSerialNo());
            DataSearch(g.getHealthID());
            ELCONoSearch(g.getSerialNo());

            cmdSave = (Button) findViewById(R.id.cmdSaveANC);
            cmdSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    DataSaveANC();
                    DisplayANCVisit();
                }
            });


            Button cmdSavePreg = (Button) findViewById(R.id.cmdSavePreg);
            cmdSavePreg.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    DataSavePreg();
                }
            });

            DisplayTempANCVisit(Global.DateConvertDMY(dtpLMP.getText().toString()));
            DisplayANCVisit();
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } catch (Exception e) {
            Connection.MessageBox(PregReg.this, e.getMessage());
            return;
        }
    }


    private void DataSaveANC() {

        if (dtpVDate.getText().toString().length() == 0) {
            Connection.MessageBox(PregReg.this, "পরিদর্শনের তারিখ কত লিখুন।");
            dtpVDate.requestFocus();
            return;
        }
        if (!rdoIron1.isChecked() & !rdoIron2.isChecked() & secIron.isShown()) {
            Connection.MessageBox(PregReg.this, "আয়রন ও ফলিক এসিড পেয়েছেন কিনা  সিলেক্ট করুন।");
            rdoIron1.requestFocus();
            return;
        }

        if (rdoIron1.isChecked() & secIron.isShown()) {
            if (txtIronQty.getText().toString().length() == 0) {
                Connection.MessageBox(PregReg.this, "আয়রন ও ফলিক এসিডের পরিমাণ কত লিখুন।");
                txtIronQty.requestFocus();
                return;
            } else if (spnIronUnit.getSelectedItemPosition() == 0) {
                Connection.MessageBox(PregReg.this, "আয়রন ও ফলিক এসিডের ইউনিট  তালিকা থেকে  সিলেক্ট করুন।।");
                spnIronUnit.requestFocus();
                return;
            }
        }

        if (!rdoMiso1.isChecked() & !rdoMiso2.isChecked() & secMiso.isShown()) {
            Connection.MessageBox(PregReg.this, "মিসোপ্রস্টল বড়ি পেয়েছেন কিনা  সিলেক্ট করুন।");
            rdoMiso1.requestFocus();
            return;
        }

        if (rdoMiso1.isChecked() & secMiso.isShown()) {
            if (txtMisoQty.getText().toString().length() == 0) {
                Connection.MessageBox(PregReg.this, "মিসোপ্রস্টল বড়ির পরিমাণ কত লিখুন।");
                txtMisoQty.requestFocus();
                return;
            } else if (spnMisoUnit.getSelectedItemPosition() == 0) {
                Connection.MessageBox(PregReg.this, "মিসোপ্রস্টল বড়ির ইউনিট  তালিকা থেকে  সিলেক্ট করুন।।");
                spnMisoUnit.requestFocus();
                return;
            }
        }

        if (spnANCSource.getSelectedItemPosition() == 0) {
            Connection.MessageBox(PregReg.this, "গর্ভকালীন সেবার স্থান করুন।।");
            spnANCSource.requestFocus();
            return;
        }

            /*PGNNo();
            String SQ1 = "";
            SQ1 = "select pregNo from PregWomen where";
            SQ1 += " healthId='"+ g.getHouseholdNo() +"' and SNo='"+ g.getSerialNo() +"'";
            SQ1 += " order by RegDT desc limit 1";
            String RecentPregPGN = C.ReturnSingleValue(SQ1);
            String PregPGNStatus = RecentPregPGN;

            String SQ2 = "";
            SQ2 = "select PGN||'^'||Outcome from Deliv where";
            SQ2 += " Dist='"+ g.getDistrict() +"' and";
            SQ2 += " Upz='"+ g.getUpazila() +"' and";
            SQ2 += " UN='"+ g.getUnion() +"' and";
            SQ2 += " Vill='"+ g.getVillage() +"' and";
            SQ2 += " ProvType='"+ g.getProvType() +"' and";
            SQ2 += " ProvCode='"+ g.getProvCode() +"' and";
            SQ2 += " HHNo='"+ g.getHouseholdNo() +"' and SNo='"+ g.getSerialNo() +"'";
            SQ2 += " order by EnDt desc limit 1";*/
        // String RecentDelivPGN = C.ReturnSingleValue(SQ2);
        //  String[] CurrStatusDelivPGN = Connection.split(RecentDelivPGN, '^');
        //  String DelivPGNStatus = CurrStatusDelivPGN[0].toString();
        //String DelivOutcomesatus = CurrStatusDelivPGN[1].toString();

        //Ewmt
        String SQL = "";
        String PGNNoNull = PGNNoNull();
        String PGNNo = PGNNo();
        String PregMaxPGNNo = PregMaxPGNNo();
        //  if(DelivPGNStatus.equalsIgnoreCase(""))
        //  {

        // String sq = String.format("Select healthId, pregNo from %s where healthId = '%s' and pregNo = '%s'", TableName, g.getHealthID(), PGNNo);

        //pgn will be increase 1
       /* String pregNo = (spnPgn.getSelectedItemPosition()==0?"":Global.Left(spnPgn.getSelectedItem().toString(),2));

        if (!C.Existence(sq)) {
            SQL = "INSERT INTO " + TableName +" (healthId , pregNo, providerId, LMP,tempLMP, EDD, gravida, lastChildAge, StartTime, EndTime, systemEntryDate) " +
                    "VALUES (" + "'"+ g.getHealthID()+"'," + "'"+ PGNNo+"'," + "'"+ g.getProvCode()+"'," + "'"+ Global.DateConvertYMD(dtpLMP.getText().toString())+"'," +
                    "'"+ Global.DateConvertYMD(dtpEDD.getText().toString())+"',"+
                    "'"+ Global.DateConvertYMD(dtpEDD.getText().toString())+"','"+
                    PregMaxPGNNo+"'," + "'"+ txtAgeL.getText().toString()+ "'," + "'"+StartTime+"'," + "'"+
                    g.CurrentTime24()+"'," + "'"+ g.DateNowYMD() +"')";

            C.Save(SQL);
        }*/
                /*SQL = "Update " + TableName + " Set ";
                SQL+="DOLMP = '"+ Global.DateConvertYMD(dtpLMP.getText().toString()) +"',";
                SQL+="DOEDD = '"+ Global.DateConvertYMD(dtpEDD.getText().toString()) +"',";
                SQL+="PrePregTimes = '"+ (spnPgn.getSelectedItemPosition()==0?"":Global.Left(spnPgn.getSelectedItem().toString(),2)) +"',";
                SQL+="LCAge = '"+ txtAgeL.getText().toString() +"'";
                SQL+="Where Dist='"+ g.getDistrict() +"' and Upz='"+ g.getUpazila() +"' and UN='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and Vill='"+ g.getVillage() +"' and HHNo='"+ g.getHouseholdNo() +"' and SNo='"+ txtSNo.getText().toString() +"' and PGN ='"+ PGNNoNull +"'";
                C.Save(SQL);*/
        // }
           /* else if(PregPGNStatus.equals(DelivPGNStatus))
            {
                //pgn will be increase 1 with max
                if (!C.Existence("Select Dist,Upz,UN,Mouza,Vill,HHNo,SNo from " + TableName + "  Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "'  and ProvType='" + g.getProvType() + "' and ProvCode='" + g.getProvCode() + "' and  HHNo='" + g.getHouseholdNo() + "' and SNo='" + txtSNo.getText().toString() + "' and PGN = '" + PGNNo + "'")) {
                    SQL = "Insert into " + TableName + "(Dist,Upz,UN,Mouza,Vill,ProvType,ProvCode,HHNo,SNo,PGN,RegDT,EnDt,Upload,UploadDT)Values('" + g.getDistrict() + "','" + g.getUpazila() + "','" + g.getUnion() + "','" + g.getMouza() + "','" + g.getVillage() + "','" + g.getProvType() + "','" + g.getProvCode() + "','" + g.getHouseholdNo() + "','" + txtSNo.getText() + "','" + PGNNo + "','"+ g.DateNowYMD() +"','" + Global.DateTimeNowYMDHMS() + "','2','')";
                    C.Save(SQL);
                }
                SQL = "Update " + TableName + " Set ";
                SQL+="DOLMP = '"+ Global.DateConvertYMD(dtpLMP.getText().toString()) +"',";
                SQL+="DOEDD = '"+ Global.DateConvertYMD(dtpEDD.getText().toString()) +"',";
                SQL+="PrePregTimes = '"+ (spnPgn.getSelectedItemPosition()==0?"":Global.Left(spnPgn.getSelectedItem().toString(),2)) +"',";
                SQL+="LCAge = '"+ txtAgeL.getText().toString() +"'";
                SQL+="Where Dist='"+ g.getDistrict() +"' and Upz='"+ g.getUpazila() +"' and UN='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and Vill='"+ g.getVillage() +"' and HHNo='"+ g.getHouseholdNo() +"' and SNo='"+ txtSNo.getText().toString() +"' and PGN ='"+ PGNNo +"'";
                C.Save(SQL);
            }
            else if(!PregPGNStatus.equals(DelivPGNStatus))
            {
                //MAX pgn will be retrieved from db
                if (!C.Existence("Select Dist,Upz,UN,Mouza,Vill,HHNo,SNo from " + TableName + "  Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "'  and ProvType='" + g.getProvType() + "' and ProvCode='" + g.getProvCode() + "' and  HHNo='" + g.getHouseholdNo() + "' and SNo='" + txtSNo.getText().toString() + "' and PGN = '" + PregMaxPGNNo + "'")) {
                    SQL = "Insert into " + TableName + "(Dist,Upz,UN,Mouza,Vill,ProvType,ProvCode,HHNo,SNo,PGN,RegDT,EnDt,Upload,UploadDT)Values('" + g.getDistrict() + "','" + g.getUpazila() + "','" + g.getUnion() + "','" + g.getMouza() + "','" + g.getVillage() + "','" + g.getProvType() + "','" + g.getProvCode() + "','" + g.getHouseholdNo() + "','" + txtSNo.getText() + "','" + PregMaxPGNNo + "','"+ g.DateNowYMD() +"','" + Global.DateTimeNowYMDHMS() + "','2','')";
                    C.Save(SQL);
                }
                SQL = "Update " + TableName + " Set ";
                SQL+="DOLMP = '"+ Global.DateConvertYMD(dtpLMP.getText().toString()) +"',";
                SQL+="DOEDD = '"+ Global.DateConvertYMD(dtpEDD.getText().toString()) +"',";
                SQL+="PrePregTimes = '"+ spnPgn.getSelectedItemPosition() +"',";
                SQL+="LCAge = '"+ txtAgeL.getText().toString() +"'";
                SQL+="Where Dist='"+ g.getDistrict() +"' and Upz='"+ g.getUpazila() +"' and UN='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and Vill='"+ g.getVillage() +"' and HHNo='"+ g.getHouseholdNo() +"' and SNo='"+ txtSNo.getText().toString() +"' and PGN ='"+ PGNNoNull +"'";
                C.Save(SQL);
            }
            String MaxPGNNo=PregMaxPGNNo();
            SQL = "Insert into " + TableANC + "(Dist,Upz,UN,Mouza,Vill,ProvType,ProvCode,HHNo,SNo,PGN,Visit,VDate,IronFolStatus,IronFolQty,IronFolUnit,MisoStatus,MisoQty,MisoUnit,EnDt,Upload,UploadDT)Values('"+ g.getDistrict() +"','"+ g.getUpazila() +"','"+ g.getUnion() +"','"+ g.getMouza() +"','"+ g.getVillage() +"','"+ g.getProvType() +"','"+ g.getProvCode() +"','"+ g.getHouseholdNo() +"','"+ txtSNo.getText() +"','" + MaxPGNNo + "','"+ VisitNumber() +"','"+ Global.DateConvertYMD(dtpVDate.getText().toString()) +"','"+ ((rdoIron1.isChecked()?"1":"2")) +"','"+ txtIronQty.getText().toString() +"','"+ spnIronUnit.getSelectedItemPosition() +"','"+ ((rdoMiso1.isChecked()?"1":"2")) +"','"+ txtMisoQty.getText().toString() +"','"+ spnMisoUnit.getSelectedItemPosition() +"','"+ Global.DateTimeNowYMDHMS() +"','2','')";
            C.Save(SQL);

            SQL = "Update " + TableNameElcoVisit + " Set ";
            SQL+="CurrStatus = '12'";
            SQL+="Where Dist='"+ g.getDistrict() +"' and Upz='"+ g.getUpazila() +"' and UN='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and Vill='"+ g.getVillage() +"' and HHNo='"+ g.getHouseholdNo() +"' and SNo='"+ txtSNo.getText().toString() +"' and Visit='" + ELCOLastVisitNum() +"'";
            C.Save(SQL);*/
        //ClearAll();

        String pgnpositionselected = String.valueOf(spnPgn.getSelectedItemPosition());

        String MaxPGNNo = PregMaxPGNNo();
        String ServiceId = serviceID(MaxPGNNo);

        SQL = "Insert into " + TableANC + "(healthId,pregNo,serviceId,providerId,visitDate,serviceSource,ironFolStatus," +
                "ironFolQty,ironFolUnit,misoStatus,misoQty,MisoUnit, sateliteCenterName,systemEntryDate,Upload) VALUES ('" + g.getHealthID() + "','" + MaxPGNNo + "','" + ServiceId + "','" + g.getProvCode() + "'" +
                ",'" + Global.DateConvertYMD(dtpVDate.getText().toString()) + "','" + spnANCSource.getSelectedItemPosition() + "','" +
                ((rdoIron1.isChecked() ? "1" : "2")) + "','" + txtIronQty.getText().toString() + "','" + spnIronUnit.getSelectedItemPosition() + "','"
                + ((rdoMiso1.isChecked() ? "1" : "2")) + "','" + txtMisoQty.getText().toString() + "','" + spnMisoUnit.getSelectedItemPosition() + "','" + "', " + "'" +
                Global.DateTimeNowYMDHMS() + "'," + "'" + "2" + "')";
        C.Save(SQL);

       /* SQL = "Update " + TableNameElcoVisit + " Set ";
        SQL+="CurrStatus = '12'";
        SQL+="Where Dist='"+ g.getDistrict() +"' and Upz='"+ g.getUpazila() +"' and UN='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and Vill='"+ g.getVillage() +"' and HHNo='"+ g.getHouseholdNo() +"' and SNo='"+ txtSNo.getText().toString() +"' and Visit='" + ELCOLastVisitNum() +"'";
        C.Save(SQL);*/

        rdogrpIron.clearCheck();
        txtIronQty.setText("");
        spnIronUnit.getSelectedItemPosition();
        rdogrpMiso.clearCheck();
        txtMisoQty.setText("");
        spnMisoUnit.setSelection(0);
        spnANCSource.setSelection(0);

        Connection.MessageBox(PregReg.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");

    }

    public class ANC extends BaseAdapter {
        private Context mContext;
        String[][] vcode;
        Integer totalRec;

        public ANC(Context c) {
            mContext = c;
        }

        public int getCount() {

            //String pgnpositionselected = String.valueOf(spnPgn.getSelectedItemPosition());
            // String ServiceId = serviceID(pgnpositionselected);

            return Integer.parseInt(C.ReturnSingleValue("Select count(*)total from ancService where healthid='" + g.getHealthID() + "' AND pregNo = '" + PregMaxPGNNo() + "'"));
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
                String pgnpositionselected = String.valueOf(spnPgn.getSelectedItemPosition());
                String ServiceId = g.getHealthID() + pgnpositionselected;

                String SQL = "Select serviceId, visitDate,ironFolStatus,ironFolQty,ironFolUnit,misoStatus,misoQty,misoUnit,serviceSource from ancService where healthid='" + g.getHealthID() + "' AND pregNo = '" + PregMaxPGNNo() + "'" + " order by cast(visitDate as DATE) asc";

                try {
                    Cursor cur = C.ReadData(SQL);
                    cur.moveToFirst();

                    totalRec = cur.getCount();
                    vcode = new String[4][totalRec];
                    int i = 0;
                    while (!cur.isAfterLast()) {
                        vcode[0][i] = "পরিদর্শন " + String.valueOf(i + 1) + " " + Global.DateConvertDMY(String.valueOf(cur.getString(cur.getColumnIndex("visitDate"))));
                        vcode[1][i] = String.valueOf(cur.getString(cur.getColumnIndex("serviceId")));
                        /*vcode[1][i]= String.valueOf(cur.getString(cur.getColumnIndex("serviceId")));
                        vcode[2][i]= cur.getString(cur.getColumnIndex("imucard"));
                        vcode[3][i]= String.valueOf(cur.getString(cur.getColumnIndex("imucode")));*/

                        i += 1;
                        cur.moveToNext();
                    }
                    cur.close();

                    Button tv = (Button) MyView.findViewById(R.id.image_name);
                    tv.setTextSize(14);
                    tv.setText(vcode[0][position]);// + "\n" + vcode[1][position]);
                    final Integer p = position;
                    tv.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            // ChkTT1.setChecked(true);
                            // if(vcode[2][position].equals("1"))
                            // {
                            //rdoCardYes.setChecked(true);
                            // }
                            // else if(vcode[2][position].equals("2"))
                            // {
                            // rdoCardNo.setChecked(true);
                            // }
                            //String pgnpositionselected = String.valueOf(spnPgn.getSelectedItemPosition());
                            String ServiceId = String.valueOf(vcode[1][position]);


                             /*   if(vcode[1][position].length()!=0)
                            {
                                dtpDOTT1.setText(vcode[1][position]);
                            }
                            else
                            {
                                dtpDOTT1.setText("");
                            }*/

                            // secTT1.setVisibility(View.VISIBLE);
                            //   btnTTClose.setVisibility(View.VISIBLE);
                            //  btnAddTT.setVisibility(View.GONE);
                            // g.setImuCode(vcode[3][position]);
                            DisplaySelectedANCInfo(ServiceId);
                        }
                    });
                } catch (Exception ex) {
                    Connection.MessageBox(PregReg.this, ex.getMessage());
                }

            }
            return MyView;
        }

    }

    private void DisplaySelectedANCInfo(String ServiceId) {

        String SQL = "Select visitDate,ironFolStatus,ironFolQty,ironFolUnit,misoStatus,misoQty,misoUnit,serviceSource from ancService where serviceId = '" + ServiceId + "'";

        final Dialog popupView = new Dialog(PregReg.this);
        popupView.setTitle("Events");
        popupView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popupView.setContentView(R.layout.ancinfo);
        popupView.setCancelable(true);
        popupView.setCanceledOnTouchOutside(true);

        try {
            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();

            while (!cur.isAfterLast()) {
                // dtpVDate.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("visitDate"))));
                if (cur.getString(cur.getColumnIndex("ironFolStatus")).equalsIgnoreCase("1")) {
                    ((TextView) popupView.findViewById(R.id.VlblIronQty)).setVisibility(View.VISIBLE);
                    ((TextView) popupView.findViewById(R.id.txtIronQty)).setVisibility(View.VISIBLE);
                    ((TextView) popupView.findViewById(R.id.VlblIronYN)).setText("হ্যাঁ");
                    ((TextView) popupView.findViewById(R.id.txtIronQty)).setText(cur.getString(cur.getColumnIndex("ironFolQty")));

                    if (!cur.getString(cur.getColumnIndex("ironFolUnit")).toString().equalsIgnoreCase("")) {
                        if (!cur.getString(cur.getColumnIndex("ironFolUnit")).toString().equalsIgnoreCase("null")) {
                            if (cur.getString(cur.getColumnIndex("ironFolUnit")).toString().equalsIgnoreCase("1")) {
                                if (!cur.getString(cur.getColumnIndex("ironFolQty")).equalsIgnoreCase(""))
                                    ((TextView) popupView.findViewById(R.id.spnIronUnit)).setText("চক্র");
                            }

                            if (cur.getString(cur.getColumnIndex("ironFolUnit")).toString().equalsIgnoreCase("2")) {
                                ((TextView) popupView.findViewById(R.id.spnIronUnit)).setText("পিছ");
                            }
                            if (cur.getString(cur.getColumnIndex("ironFolUnit")).toString().equalsIgnoreCase("3")) {
                                ((TextView) popupView.findViewById(R.id.spnIronUnit)).setText("ডোজ");
                            }
                            //((TextView) popupView.findViewById(R.id.spnIronUnit)).setText(cur.getString(cur.getColumnIndex("ironFolUnit")));
                        }
                    }
                } else if (cur.getString(cur.getColumnIndex("ironFolStatus")).equalsIgnoreCase("2")) {
                    ((TextView) popupView.findViewById(R.id.VlblIronYN)).setText("না");
                    ((TextView) popupView.findViewById(R.id.VlblIronQty)).setVisibility(View.GONE);
                    ((TextView) popupView.findViewById(R.id.txtIronQty)).setVisibility(View.GONE);
                }


                if (cur.getString(cur.getColumnIndex("misoStatus")).equalsIgnoreCase("1")) {
                    ((TextView) popupView.findViewById(R.id.VlblMisoQty)).setVisibility(View.VISIBLE);
                    ((TextView) popupView.findViewById(R.id.txtMisoQty)).setVisibility(View.VISIBLE);
                    ((TextView) popupView.findViewById(R.id.VlblMisoYN)).setText("হ্যাঁ");
                    ((TextView) popupView.findViewById(R.id.txtMisoQty)).setText(cur.getString(cur.getColumnIndex("misoQty")));

                    if (!cur.getString(cur.getColumnIndex("misoUnit")).toString().equalsIgnoreCase("")) {
                        if (!cur.getString(cur.getColumnIndex("misoUnit")).toString().equalsIgnoreCase("null")) {
                            if (cur.getString(cur.getColumnIndex("misoUnit")).toString().equalsIgnoreCase("1")) {
                                if (!cur.getString(cur.getColumnIndex("misoQty")).equalsIgnoreCase(""))
                                    ((TextView) popupView.findViewById(R.id.spnMisoUnit)).setText("চক্র");
                            }
                            if (cur.getString(cur.getColumnIndex("misoUnit")).toString().equalsIgnoreCase("2")) {
                                ((TextView) popupView.findViewById(R.id.spnMisoUnit)).setText("পিছ");
                            }
                            if (cur.getString(cur.getColumnIndex("misoUnit")).toString().equalsIgnoreCase("3")) {
                                ((TextView) popupView.findViewById(R.id.spnMisoUnit)).setText("ডোজ");
                            }

                            //((TextView) popupView.findViewById(R.id.spnMisoUnit)).setText(cur.getString(cur.getColumnIndex("misoUnit")));
                        }
                    }
                } else if (cur.getString(cur.getColumnIndex("misoStatus")).equalsIgnoreCase("2")) {
                    ((TextView) popupView.findViewById(R.id.VlblMisoYN)).setText("না");
                    ((TextView) popupView.findViewById(R.id.VlblMisoQty)).setVisibility(View.GONE);
                    ((TextView) popupView.findViewById(R.id.txtMisoQty)).setVisibility(View.GONE);
                }

                if (!cur.getString(cur.getColumnIndex("serviceSource")).toString().equalsIgnoreCase("null"))
                {
                    if(cur.getString(cur.getColumnIndex("serviceSource")).equalsIgnoreCase("1"))
                    {
                        ((TextView) popupView.findViewById(R.id.txtserviceSource)).setText("সরকারি");
                    }
                    else if(cur.getString(cur.getColumnIndex("serviceSource")).equalsIgnoreCase("2"))
                    {
                        ((TextView) popupView.findViewById(R.id.txtserviceSource)).setText("বেসরকারি");
                    }

                }


                cur.moveToNext();
            }
            cur.close();


            //  popupView.showAtLocation(popupView, Gravity.CENTER, 0, 0);
            Button btnDismiss = (Button) popupView.findViewById(R.id.dismiss);
            btnDismiss.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    popupView.dismiss();
                }
            });

            popupView.show();
        } catch (Exception ex) {
            Connection.MessageBox(PregReg.this, ex.getMessage());
        }

    }

    //Add days with system Date
    public static String getCalculatedDate(String dateFormat, int days) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(dateFormat);
        cal.add(Calendar.DAY_OF_YEAR, days);
        return s.format(new Date(cal.getTimeInMillis()));
    }

    //Add days with user given date
    public static String getCalculatedDate1(String date, String dateFormat, int days) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(dateFormat);
        cal.add(Calendar.DAY_OF_YEAR, days);
        try {
            return s.format(new Date(s.parse(date).getTime()));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            Log.e("TAG", "Error in Parsing Date : " + e.getMessage());
        }
        return null;
    }

    private String VisitNumber() {
        String SQL = "";
        SQL = "Select (ifnull(max(cast(Visit as int)),0)+1)MaxHH from ANC";
        SQL += " where";
        SQL += " dist='" + g.getDistrict() + "' and upz='" + g.getUpazila() + "' and un='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and vill='" + g.getVillage() + "' and ProvType='" + g.getProvType() + "' and ProvCode='" + g.getProvCode() + "' and HHNo='" + g.getHouseholdNo() + "' and SNo='" + txtSNo.getText() + "'";

        String VisitNo = Global.Right(("00" + C.ReturnSingleValue(SQL)), 2);

        return VisitNo;
    }

    private String PGNNoNull() {
        String SQL = "";
        String PGNNo = "";

        SQL = "select '0'||(ifnull(max(cast(PregNo as int)),1))MaxPGNNo from PregWomen WHERE healthId = " + g.getHealthID();
            /*SQL += " where";
            SQL += " dist='" + g.getDistrict() + "' and upz='" + g.getUpazila() + "' and un='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and vill='" + g.getVillage() + "'  and ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"' and  HHNo='" + g.getHouseholdNo() + "' and SNo='" + txtSNo.getText() + "'";*/
        PGNNo = Global.Right(("00" + C.ReturnSingleValue(SQL)), 2);

        return PGNNo;
    }

    private String serviceID(String pregNo) {
        String SQL = "";
        String PGNNo = "";

        //String MaxPGNNo=PregMaxPGNNo();
        SQL = "select '0'||(ifnull(max(cast(serviceId as int)),0))MaxserviceId from ancService WHERE healthId = " + g.getHealthID() + " AND pregNo = " + pregNo;

        String tempserviceID = C.ReturnSingleValue(SQL);


        String serviceID = String.valueOf((Integer.parseInt(tempserviceID) + 1));

        if (serviceID.equalsIgnoreCase("1")) {
            return String.valueOf(g.getHealthID() + pregNo + serviceID);
        } else {
            return String.valueOf(serviceID);
        }
    }

    private String PregMaxPGNNo() {
        String SQL = "";
        String PGNNo = "";

        SQL = "select (ifnull(max(cast(PregNo as int)),0)) AS PregNo from PregWomen WHERE healthId=" + g.getHealthID();
        /*SQL += " where";
        SQL += " dist='" + g.getDistrict() + "' and upz='" + g.getUpazila() + "' and un='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and vill='" + g.getVillage() + "'  and ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"' and  HHNo='" + g.getHouseholdNo() + "' and SNo='" + txtSNo.getText() + "'";*/
        // PGNNo = Global.Right(("00"+ C.ReturnSingleValue(SQL)),2);
        PGNNo = C.ReturnSingleValue(SQL);
        return PGNNo;
    }

    private String PGNNo() {
        String SQL = "";
        String PGNNo = "";

        SQL = "Select (ifnull(max(cast(PregNo as int)),0)+1)MaxPGNNo from PregWomen WHERE healthId = " + g.getHealthID();
        /*SQL += " where";
        SQL += " dist='" + g.getDistrict() + "' and upz='" + g.getUpazila() + "' and un='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and vill='" + g.getVillage() + "'  and ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"' and  HHNo='" + g.getHouseholdNo() + "' and SNo='" + txtSNo.getText() + "'";*/
        PGNNo = Global.Right(("00" + C.ReturnSingleValue(SQL)), 2);
        return PGNNo;
    }

    private String ELCOLastVisitNum() {
        String SQL = "";
        SQL = "Select max(Visit)MaxHH from ELCOVisit";
        SQL += " where";
        SQL += " dist='" + g.getDistrict() + "' and upz='" + g.getUpazila() + "' and un='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and SNo='" + txtSNo.getText() + "'";

        String VisitNo = Global.Right(("00" + C.ReturnSingleValue(SQL)), 2);

        return VisitNo;
    }

    private void ClearAll() {
        dtpLMP.setText("");
        dtpEDD.setText("");
        spnPgn.setSelection(0);
        txtAgeM.setText("");
        txtAgeY.setText("");
        dtpVDate.setText("");
        rdogrpIron.clearCheck();
        txtIronQty.setText("");
        spnIronUnit.getSelectedItemPosition();
        rdogrpMiso.clearCheck();
        txtMisoQty.setText("");
        spnMisoUnit.setSelection(0);
    }

    private void DataSavePreg() {
        AlertDialog.Builder adb = new AlertDialog.Builder(PregReg.this);
        adb.setTitle("Close");
        adb.setMessage("তথ্য সফলভাবে সংরক্ষণ হয়েছে। আপনি কি ANC ভিজিটের তথ্য সংগ্রহ করতে চান[Yes/No]?");
        //adb.setNegativeButton("No", null);
        adb.setNegativeButton("No", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                DataSavePregnant();
                finish();
                Intent f2 = new Intent(getApplicationContext(), MemberList.class);
                startActivity(f2);
            }
        });

        adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                DataSavePregnant();
                DisplayTempANCVisit(Global.DateConvertYMD(dtpLMP.getText().toString()));

                secANCVisit.setVisibility(View.VISIBLE);
                secPregVisit.setVisibility(View.GONE);
            }
        });
        adb.show();
    }

    private String GetTotalSonGaughter(String HealthId) {

        String sq = String.format("Select SUM((ifnull((cast(son as int)),0)) + (ifnull((cast(dau as int)),0))) Total from elco WHERE healthId = '%s'", g.getHealthID());
        return C.ReturnSingleValue(sq);
    }

    private void DataSavePregnant() {
        try {
            if (dtpLMP.getText().toString().length() == 0) {
                Connection.MessageBox(PregReg.this, "সর্বশেষ মাসিকের  তারিখ কত লিখুন।");
                dtpLMP.requestFocus();
                return;
            } else if (dtpEDD.getText().toString().length() == 0) {
                Connection.MessageBox(PregReg.this, "সম্ভাব্য প্রসবের তারিখ কত লিখুন।");
                dtpEDD.requestFocus();
                return;
            } else if (spnPgn.getSelectedItemPosition() == 0 & spnPgn.isShown()) {
                Connection.MessageBox(PregReg.this, "বর্তমানে কত তম গর্ভ তালিকা থেকে  সিলেক্ট করুন।।");
                spnPgn.requestFocus();
                return;
            }
            if (txtLiveSonDau.getText().toString().equalsIgnoreCase("0")) {

            } else {
                if (txtAgeM.getText().toString().length() == 0 && txtAgeY.getText().toString().length() == 0) {
                    Connection.MessageBox(PregReg.this, "শেষ সন্তানের বয়স কত লিখুন।");
                    txtAgeM.requestFocus();
                    return;
                }
            }


            /*else if(dtpVDate.getText().toString().length()==0)
            {
                Connection.MessageBox(PregReg.this,"পরিদর্শনের তারিখ কত লিখুন।");
                dtpVDate.requestFocus();
                return;
            }
            if(!rdoIron1.isChecked() & !rdoIron2.isChecked() & secIron.isShown())
            {
                Connection.MessageBox(PregReg.this, "আয়রন ও ফলিক এসিড পেয়েছেন কিনা  সিলেক্ট করুন।");
                rdoIron1.requestFocus();
                return;
            }

            if(rdoIron1.isChecked() & secIron.isShown())
            {
                if(txtIronQty.getText().toString().length()==0)
                {
                    Connection.MessageBox(PregReg.this, "আয়রন ও ফলিক এসিডের পরিমাণ কত লিখুন।");
                    txtIronQty.requestFocus();
                    return;
                }
                else if(spnIronUnit.getSelectedItemPosition()==0)
                {
                    Connection.MessageBox(PregReg.this, "আয়রন ও ফলিক এসিডের ইউনিট  তালিকা থেকে  সিলেক্ট করুন।।");
                    spnIronUnit.requestFocus();
                    return;
                }
            }

            if(!rdoMiso1.isChecked() & !rdoMiso2.isChecked() & secMiso.isShown())
            {
                Connection.MessageBox(PregReg.this, "মিসোপ্রস্টল বড়ি পেয়েছেন কিনা  সিলেক্ট করুন।");
                rdoMiso1.requestFocus();
                return;
            }

            if(rdoMiso1.isChecked() & secMiso.isShown())
            {
                if(txtMisoQty.getText().toString().length()==0)
                {
                    Connection.MessageBox(PregReg.this, "মিসোপ্রস্টল বড়ির পরিমাণ কত লিখুন।");
                    txtMisoQty.requestFocus();
                    return;
                }
                else if(spnMisoUnit.getSelectedItemPosition()==0)
                {
                    Connection.MessageBox(PregReg.this, "মিসোপ্রস্টল বড়ির ইউনিট  তালিকা থেকে  সিলেক্ট করুন।।");
                    spnMisoUnit.requestFocus();
                    return;
                }
            }
*/
           /* String SQ1 = "";
            SQ1 = "select PGN from PregWomen where";
            SQ1 += " Dist='"+ g.getDistrict() +"' and";
            SQ1 += " Upz='"+ g.getUpazila() +"' and";
            SQ1 += " UN='"+ g.getUnion() +"' and";
            SQ1 += " Vill='"+ g.getVillage() +"' and";
            SQ1 += " ProvType='"+ g.getProvType() +"' and";
            SQ1 += " ProvCode='"+ g.getProvCode() +"' and";
            SQ1 += " HHNo='"+ g.getHouseholdNo() +"' and SNo='"+ g.getSerialNo() +"'";
            SQ1 += " order by RegDT desc limit 1";
            String RecentPregPGN = C.ReturnSingleValue(SQ1);
            String PregPGNStatus = RecentPregPGN;

            String SQ2 = "";
            SQ2 = "select PGN||'^'||Outcome from Deliv where";
            SQ2 += " Dist='"+ g.getDistrict() +"' and";
            SQ2 += " Upz='"+ g.getUpazila() +"' and";
            SQ2 += " UN='"+ g.getUnion() +"' and";
            SQ2 += " Vill='"+ g.getVillage() +"' and";
            SQ2 += " ProvType='"+ g.getProvType() +"' and";
            SQ2 += " ProvCode='"+ g.getProvCode() +"' and";
            SQ2 += " HHNo='"+ g.getHouseholdNo() +"' and SNo='"+ g.getSerialNo() +"'";
            SQ2 += " order by EnDt desc limit 1";*/
            // String RecentDelivPGN = C.ReturnSingleValue(SQ2);
            //  String[] CurrStatusDelivPGN = Connection.split(RecentDelivPGN, '^');
            //  String DelivPGNStatus = CurrStatusDelivPGN[0].toString();
            //String DelivOutcomesatus = CurrStatusDelivPGN[1].toString();

            //Ewmt
            String SQL = "";
            String PGNNoNull = PGNNoNull();
            String PGNNo = PGNNo();
            String PregMaxPGNNo = PregMaxPGNNo();
            //  if(DelivPGNStatus.equalsIgnoreCase(""))
            //  {


            //pgn will be increase 1
            String pgnpositionselected = String.valueOf(spnPgn.getSelectedItemPosition());
            // String pregNo = (spnPgn.getSelectedItemPosition()==0?"":Global.Left(spnPgn.getSelectedItem().toString(),2));

            String sq = String.format("Select healthId, pregNo from %s where healthId = '%s' and pregNo = '%s'", TableName, g.getHealthID(), PGNNo);

            if (!C.Existence(sq)) {
                SQL = "INSERT INTO " + TableName + " (healthId , pregNo, providerId, LMP,tempLMP, EDD, gravida, lastChildAge, StartTime, EndTime, systemEntryDate) " +
                        "VALUES (" + "'" + g.getHealthID() + "'," + "'" + PGNNo + "'," + "'" + g.getProvCode() + "'," + "'" + Global.DateConvertYMD(dtpLMP.getText().toString()) + "'," +
                        "'" + Global.DateConvertYMD(dtpEDD.getText().toString()) + "'," +
                        "'" + Global.DateConvertYMD(dtpEDD.getText().toString()) + "','" +
                        pgnpositionselected + "'," + "'" + ComputeAge(txtAgeM.getText().toString(), txtAgeY.getText().toString()) + "'," + "'" + StartTime + "'," + "'" +
                        g.CurrentTime24() + "'," + "'" + g.DateNowYMD() + "')";

                C.Save(SQL);
            }
                /*SQL = "Update " + TableName + " Set ";
                SQL+="DOLMP = '"+ Global.DateConvertYMD(dtpLMP.getText().toString()) +"',";
                SQL+="DOEDD = '"+ Global.DateConvertYMD(dtpEDD.getText().toString()) +"',";
                SQL+="PrePregTimes = '"+ (spnPgn.getSelectedItemPosition()==0?"":Global.Left(spnPgn.getSelectedItem().toString(),2)) +"',";
                SQL+="LCAge = '"+ txtAgeL.getText().toString() +"'";
                SQL+="Where Dist='"+ g.getDistrict() +"' and Upz='"+ g.getUpazila() +"' and UN='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and Vill='"+ g.getVillage() +"' and HHNo='"+ g.getHouseholdNo() +"' and SNo='"+ txtSNo.getText().toString() +"' and PGN ='"+ PGNNoNull +"'";
                C.Save(SQL);*/
            // }
           /* else if(PregPGNStatus.equals(DelivPGNStatus))
            {
                //pgn will be increase 1 with max
                if (!C.Existence("Select Dist,Upz,UN,Mouza,Vill,HHNo,SNo from " + TableName + "  Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "'  and ProvType='" + g.getProvType() + "' and ProvCode='" + g.getProvCode() + "' and  HHNo='" + g.getHouseholdNo() + "' and SNo='" + txtSNo.getText().toString() + "' and PGN = '" + PGNNo + "'")) {
                    SQL = "Insert into " + TableName + "(Dist,Upz,UN,Mouza,Vill,ProvType,ProvCode,HHNo,SNo,PGN,RegDT,EnDt,Upload,UploadDT)Values('" + g.getDistrict() + "','" + g.getUpazila() + "','" + g.getUnion() + "','" + g.getMouza() + "','" + g.getVillage() + "','" + g.getProvType() + "','" + g.getProvCode() + "','" + g.getHouseholdNo() + "','" + txtSNo.getText() + "','" + PGNNo + "','"+ g.DateNowYMD() +"','" + Global.DateTimeNowYMDHMS() + "','2','')";
                    C.Save(SQL);
                }
                SQL = "Update " + TableName + " Set ";
                SQL+="DOLMP = '"+ Global.DateConvertYMD(dtpLMP.getText().toString()) +"',";
                SQL+="DOEDD = '"+ Global.DateConvertYMD(dtpEDD.getText().toString()) +"',";
                SQL+="PrePregTimes = '"+ (spnPgn.getSelectedItemPosition()==0?"":Global.Left(spnPgn.getSelectedItem().toString(),2)) +"',";
                SQL+="LCAge = '"+ txtAgeL.getText().toString() +"'";
                SQL+="Where Dist='"+ g.getDistrict() +"' and Upz='"+ g.getUpazila() +"' and UN='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and Vill='"+ g.getVillage() +"' and HHNo='"+ g.getHouseholdNo() +"' and SNo='"+ txtSNo.getText().toString() +"' and PGN ='"+ PGNNo +"'";
                C.Save(SQL);
            }
            else if(!PregPGNStatus.equals(DelivPGNStatus))
            {
                //MAX pgn will be retrieved from db
                if (!C.Existence("Select Dist,Upz,UN,Mouza,Vill,HHNo,SNo from " + TableName + "  Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "'  and ProvType='" + g.getProvType() + "' and ProvCode='" + g.getProvCode() + "' and  HHNo='" + g.getHouseholdNo() + "' and SNo='" + txtSNo.getText().toString() + "' and PGN = '" + PregMaxPGNNo + "'")) {
                    SQL = "Insert into " + TableName + "(Dist,Upz,UN,Mouza,Vill,ProvType,ProvCode,HHNo,SNo,PGN,RegDT,EnDt,Upload,UploadDT)Values('" + g.getDistrict() + "','" + g.getUpazila() + "','" + g.getUnion() + "','" + g.getMouza() + "','" + g.getVillage() + "','" + g.getProvType() + "','" + g.getProvCode() + "','" + g.getHouseholdNo() + "','" + txtSNo.getText() + "','" + PregMaxPGNNo + "','"+ g.DateNowYMD() +"','" + Global.DateTimeNowYMDHMS() + "','2','')";
                    C.Save(SQL);
                }
                SQL = "Update " + TableName + " Set ";
                SQL+="DOLMP = '"+ Global.DateConvertYMD(dtpLMP.getText().toString()) +"',";
                SQL+="DOEDD = '"+ Global.DateConvertYMD(dtpEDD.getText().toString()) +"',";
                SQL+="PrePregTimes = '"+ spnPgn.getSelectedItemPosition() +"',";
                SQL+="LCAge = '"+ txtAgeL.getText().toString() +"'";
                SQL+="Where Dist='"+ g.getDistrict() +"' and Upz='"+ g.getUpazila() +"' and UN='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and Vill='"+ g.getVillage() +"' and HHNo='"+ g.getHouseholdNo() +"' and SNo='"+ txtSNo.getText().toString() +"' and PGN ='"+ PGNNoNull +"'";
                C.Save(SQL);
            }
            String MaxPGNNo=PregMaxPGNNo();
            SQL = "Insert into " + TableANC + "(Dist,Upz,UN,Mouza,Vill,ProvType,ProvCode,HHNo,SNo,PGN,Visit,VDate,IronFolStatus,IronFolQty,IronFolUnit,MisoStatus,MisoQty,MisoUnit,EnDt,Upload,UploadDT)Values('"+ g.getDistrict() +"','"+ g.getUpazila() +"','"+ g.getUnion() +"','"+ g.getMouza() +"','"+ g.getVillage() +"','"+ g.getProvType() +"','"+ g.getProvCode() +"','"+ g.getHouseholdNo() +"','"+ txtSNo.getText() +"','" + MaxPGNNo + "','"+ VisitNumber() +"','"+ Global.DateConvertYMD(dtpVDate.getText().toString()) +"','"+ ((rdoIron1.isChecked()?"1":"2")) +"','"+ txtIronQty.getText().toString() +"','"+ spnIronUnit.getSelectedItemPosition() +"','"+ ((rdoMiso1.isChecked()?"1":"2")) +"','"+ txtMisoQty.getText().toString() +"','"+ spnMisoUnit.getSelectedItemPosition() +"','"+ Global.DateTimeNowYMDHMS() +"','2','')";
            C.Save(SQL);

            SQL = "Update " + TableNameElcoVisit + " Set ";
            SQL+="CurrStatus = '12'";
            SQL+="Where Dist='"+ g.getDistrict() +"' and Upz='"+ g.getUpazila() +"' and UN='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and Vill='"+ g.getVillage() +"' and HHNo='"+ g.getHouseholdNo() +"' and SNo='"+ txtSNo.getText().toString() +"' and Visit='" + ELCOLastVisitNum() +"'";
            C.Save(SQL);*/
            //ClearAll();

            rdogrpIron.clearCheck();
            txtIronQty.setText("");
            spnIronUnit.getSelectedItemPosition();
            rdogrpMiso.clearCheck();
            txtMisoQty.setText("");
            spnMisoUnit.setSelection(0);

            Connection.MessageBox(PregReg.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");


        } catch (Exception e) {
            Connection.MessageBox(PregReg.this, e.getMessage());
            return;
        }
    }

    private Integer ComputeAge(String mon, String yr) {
        Integer totalmonth = 0;
        if (yr.length() > 0) {
            if (Integer.parseInt(yr) >= 1) {
                Integer yearinmonth = Integer.parseInt(yr);
                totalmonth = 12 * yearinmonth;
            }
            if (Integer.parseInt(mon) > 0) {
                totalmonth = totalmonth + Integer.parseInt(mon);
            }
        }
        return totalmonth;

    }

    private void DisplayAge(String mon) {
        Float totalmonth = null;
        if (mon != null) {
            if (mon.length() > 0) {
                if (Integer.parseInt(mon) >= 1) {
                    Integer yearinmonth = Integer.parseInt(mon) / 12;
                    txtAgeY.setText(String.valueOf(yearinmonth));
                    txtAgeM.setText(String.valueOf(Integer.parseInt(mon) - (yearinmonth * 12)));

                }
                /*if (Float.parseFloat(mon) > 0) {
                    totalmonth = totalmonth + Integer.parseInt(mon) / 12;
                }*/
            }
        }


    }

    private void DisplayANCVisit() {
        GridView gcount = (GridView) findViewById(R.id.gridANC);
        g.setImuCode(String.valueOf(gcount.getCount() + 1));
        ANCVisits();
    }

    public void ANCVisits() {
        GridView g1 = (GridView) findViewById(R.id.gridANC);
        g1.setAdapter(new ANC(this));
        g1.setNumColumns(6);
    }


    private void DisplayTempANCVisit(String ExpLMPDate) {
        GridView gcount = (GridView) findViewById(R.id.gridANCExp);
        g.setImuCode(String.valueOf(gcount.getCount() + 1));
        ExpectedANC(ExpLMPDate);
    }

    public void ExpectedANC(String ExpANCDate) {
        GridView g1 = (GridView) findViewById(R.id.gridANCExp);
        g1.setAdapter(new ExpectedANC(this, ExpANCDate));
        g1.setNumColumns(6);
    }

    private void DataSearch(String healthID) {
        try {
            String SQL = "";
            SQL = "Select SNo as SNo, ifnull(HealthID,'') as HealthID, ifnull(NameEng,'') as NameEng,";
            SQL += "ifnull(Age,'') as Age";
            SQL += " from Member Where HealthID='" + healthID + "'";
            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                txtHealthID.setText(cur.getString(cur.getColumnIndex("HealthID")));
                txtSNo.setText(cur.getString(cur.getColumnIndex("SNo")));
                txtName.setText(cur.getString(cur.getColumnIndex("NameEng")));
                txtAge.setText(cur.getString(cur.getColumnIndex("Age")));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(PregReg.this, e.getMessage());
            return;
        }
    }

    private void ELCONoSearch(String SNo) {
        try {
            String SQL = "";
            SQL = "select E.ELCONo as ELCONo from ELCO E Where E.healthId='" + g.getHealthID() + "'";
            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                txtELCONo.setText(cur.getString(cur.getColumnIndex("ELCONo")));
                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(PregReg.this, e.getMessage());
            return;
        }
    }

    private void PregWomenSearch() {
        try {
            String SQL = "";
            /*SQL = "select LMP,EDD,gravida,lastChildAge from PregWomen";
            SQL += " Where HealthId='"+ g.getHealthID() +"' and pregNo= '"+ SNo +"' and pregNo=(select '0'||cast(max(pregNo) as string) from PregWomen";
            SQL += " Where HealthId='"+ g.getHealthID() +"' and pregNo= '"+ SNo +"'))";*/

            SQL = "select LMP,EDD,gravida,lastChildAge, pregNo AS pregNo from PregWomen";
            SQL += " Where HealthId='" + g.getHealthID() + "' and pregNo = (select '0'||cast(max(pregNo) as string) FROM PregWomen  WHERE HealthId= + '" + g.getHealthID() + "')";


            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();
            secANCVisit.setVisibility(View.GONE);
            while (!cur.isAfterLast()) {

                dtpLMP.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("LMP"))));
                dtpEDD.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("EDD"))));
                spnPgn.setSelection(Integer.parseInt(cur.getString(cur.getColumnIndex("gravida"))));

                DisplayAge(cur.getString(cur.getColumnIndex("lastChildAge")));


                secANCVisit.setVisibility(View.VISIBLE);
                secPregVisit.setVisibility(View.GONE);
                cur.moveToNext();
            }
            txtLiveSonDau.setText(GetTotalSonGaughter(g.getHealthID()));
            cur.close();
        } catch (Exception e) {
            Log.e("Error from pregwoman", e.getMessage());
            Connection.MessageBox(PregReg.this, e.getMessage());
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

            dtpDate = (EditText) findViewById(R.id.dtpLMP);

            if (VariableID.equals("btnLMP")) {
                dtpDate = (EditText) findViewById(R.id.dtpLMP);
                //dtpEDD.setText(g.addDaysDMY(Global.DateConvertDMY(dtpLMP.getText().toString()),280));
                //dtpEDD.setText(g.addDaysDMY(Global.DateConvertDMY(dtpLMP.getText().toString()),280));
                //dtpEDD.setText(g.addDaysYMD(dtpLMP.getText().toString(),280));
                //dtpEDD.setText(getCalculatedDate(dtpLMP.getText().toString(),280));
                //addDaysDMY(dtpLMP.setText(),280);

                //dtpEDD.setText(getCalculatedDate1(Global.DateConvertDMY(dtpLMP.getText().toString()),"dd-MM-yyyy",280));
               /* Calendar cal = Calendar.getInstance();
                int mYear = cal.get(Calendar.YEAR);
                int mMonth = cal.get(Calendar.MONTH);
                int mDay = cal.get(Calendar.DAY_OF_MONTH);

                // display the current date
                String CurrentDate = dtpDate.toString();//mYear + "/" + mMonth + "/" + mDay;
                String dateInString = CurrentDate; // Start date
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                cal.add(Calendar.DATE,280);

                /*String dt = sdf.format(cal.getTime());  // dt is now the new date
                //Toast.makeText(this, "" + dt, 5000).show();
                dtpEDD.setText(dt);*/

                /*sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date resultdate = new Date(cal.getTimeInMillis());
                dateInString = sdf.format(resultdate);
                dtpEDD.setText(dateInString);*/
            } else if (VariableID.equals("btnVDate")) {
                dtpDate = (EditText) findViewById(R.id.dtpVDate);
            }


            dtpDate.setText(new StringBuilder()
                    .append(Global.Right("00" + mDay, 2)).append("/")
                    .append(Global.Right("00" + mMonth, 2)).append("/")
                    .append(mYear));

            dtpEDD.setText(Global.addDays(dtpLMP.getText().toString(), 280));
        }
    };
    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
            hour = selectedHour;
            minute = selectedMinute;
            EditText tpTime;

        }
    };


    public class ExpectedANC extends BaseAdapter {
        private Context mContext;
        String[][] vcode;
        Integer totalRec;
        String ExpDate;

        public ExpectedANC(Context c, String ExpAncDate) {
            mContext = c;
            ExpDate = ExpAncDate;
        }

        public int getCount() {


            return Integer.parseInt("4");//C.ReturnSingleValue("Select count(*)total from Immunization where healthid='"+ g.getHealthID() +"'"));
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View MyView = convertView;
            if (convertView == null) {
                LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                MyView = li.inflate(R.layout.anc_item, null);

                // String SQL  = "Select imucode as imucode,imudate as imudate,imuCard as imucard from Immunization where healthid='"+ g.getHealthID() +"' order by cast(imudate as date) desc";

                try {
                    //Cursor cur = C.ReadData(SQL);
                    // cur.moveToFirst();

                    totalRec = 4;//cur.getCount();
                    vcode = new String[4][totalRec];
                    int i = 0;
                    // while(!cur.isAfterLast())
                    //{
                    vcode[0][0] = "পরিদর্শন 1 " + Global.addDaysDMY(ExpDate, 120);
                    vcode[0][1] = "পরিদর্শন 2 " + Global.addDaysDMY(ExpDate, 180);
                    vcode[0][2] = "পরিদর্শন 3 " + Global.addDaysDMY(ExpDate, 240);
                    vcode[0][3] = "পরিদর্শন 4 " + Global.addDaysDMY(ExpDate, 270);
                    //+ String.valueOf(cur.getString(cur.getColumnIndex("imucode")));
                    // vcode[1][i]= "ANC 2 - "+ Global.DateConvertYMD(dtpLMP.getText().toString());//cur.getString(cur.getColumnIndex("imudate"));
                    //vcode[2][i]= "ANC 3 - "+ Global.DateConvertYMD(dtpLMP.getText().toString());//cur.getString(cur.getColumnIndex("imucard"));
                    // vcode[3][i]= "ANC 4 - "+ Global.DateConvertYMD(dtpLMP.getText().toString());//String.valueOf(cur.getString(cur.getColumnIndex("imucode")));

                    i += 1;
                    // cur.moveToNext();
                    //  }
                    //cur.close();

                    Button tv = (Button) MyView.findViewById(R.id.image_name);
                    tv.setTextSize(14);
                    //tv.setText(vcode[0][position] + "\n" + vcode[1][position]);
                    tv.setText(vcode[0][position] + "\n");
                    final Integer p = position;
                /*    tv.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                           // ChkTT1.setChecked(true);
                            if(vcode[2][position].equals("1"))
                            {
                               // rdoCardYes.setChecked(true);
                            }
                            else if(vcode[2][position].equals("2"))
                            {
                               // rdoCardNo.setChecked(true);
                            }

                            if(vcode[1][position].length()!=0)
                            {
                               // dtpDOTT1.setText(vcode[1][position]);
                            }
                            else
                            {
                              //  dtpDOTT1.setText("");
                            }

                          //  secTT1.setVisibility(View.VISIBLE);
                          //  btnTTClose.setVisibility(View.VISIBLE);
                           // btnAddTT.setVisibility(View.GONE);
                            g.setImuCode(vcode[3][position]);
                        }
                    });*/
                } catch (Exception ex) {
                    Connection.MessageBox(PregReg.this, ex.getMessage());
                }

            }
            return MyView;
        }

    }

}

