package com.data.rhis2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
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
 * Created by Nisan on 12/29/2015.
 */
public class EpiNonRegistration extends Activity {
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
        AlertDialog.Builder adb = new AlertDialog.Builder(EpiNonRegistration.this);
        switch (item.getItemId()) {
            case R.id.menuClose:
                adb.setTitle("Close");
                adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান[হাঁ/না]?");
                adb.setNegativeButton("না", null);
                adb.setPositiveButton("হাঁ", new AlertDialog.OnClickListener() {
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

    LinearLayout seclblepireg;

    LinearLayout secDiv;
    TextView VlblDiv;
    Spinner spnDiv;

    LinearLayout secDistrict;
    TextView VlblDistrict;
    Spinner spnDistrict;
    LinearLayout secUpazilla;
    TextView VlblUpazilla;
    Spinner spnUpazilla;
    // EditText txtUpazilla;
    LinearLayout secUnion;
    TextView VlblUnion;
    Spinner spnUnion;
    //EditText txtUnion;
    LinearLayout secVillage;
    TextView VlblVillage;
    Spinner spnVillage;
    //EditText txtVillage;
    LinearLayout secMouza;
    TextView VlblMouza;
    // EditText txtMouza;
    Spinner spnMouza;
    LinearLayout secBari;
    TextView VlblBari;
    EditText txtBari;
    LinearLayout secregisNo;
    TextView VlblregisNo;
    EditText txtregisNo;
    LinearLayout secregDate;
    TextView VlblregDate;
    EditText dtpregDate;
    ImageButton btnregDate;
    LinearLayout seccName;
    TextView VlblcName;
    EditText txtcName;
    LinearLayout secfName;
    TextView VlblfName;
    EditText txtfName;
    LinearLayout secmName;
    TextView VlblmName;
    EditText txtmName;
    LinearLayout secsex;
    TextView Vlblsex;
    RadioGroup rdogrpsex;

    RadioButton rdosex1;
    RadioButton rdosex2;
    LinearLayout secmobileNo;
    TextView VlblmobileNo;
    EditText txtmobileNo;
    LinearLayout secdob;
    TextView Vlbldob;
    EditText dtpdob;
    ImageButton btndob;
    LinearLayout secage;
    TextView Vlblage;
    EditText txtage;
    TextView Vlblage1;

    String StartTime;
    /* String District="";
     String Upazilla="";
     String Unions="";
     String Mouza="";
     String Village="";

     String ZIlLAID="";
     String UpazilaId="";
     String UNIONID="";
     String MOUZAID="";
     String VILLAGEID="";*/
    Spinner spnSubBlock;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.epinonregistration);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();

            TableName = "epiMaster";

            //ClientMap cMap = new ClientMap();
            // String id = cMap.getName() + cMap.getFatherName() +cMap.getZillaId()+cMap.getUpazilaId()+cMap.getUnionId()+cMap.getMouzaId()+cMap.getVillageId();

            seclblepireg = (LinearLayout) findViewById(R.id.seclblepireg);

            secDiv = (LinearLayout) findViewById(R.id.secDiv);
            VlblDiv = (TextView) findViewById(R.id.VlblDiv);

            spnDiv = (Spinner) findViewById(R.id.spnDiv);
            spnDiv.setAdapter(C.getArrayAdapter("select id||'-'||division DistName from Division order by id"));

            spnDiv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // spnUpazilla.setAdapter(C.getArrayAdapter("Select '  ' UPAZILAName from UPAZILA union select UpazilaId||'-'||UPAZILANAMEENG UPAZILAName from UPAZILA where ZIlLAID='"+ Global.Left(spnDistrict.getSelectedItem().toString(),2) +"'"));
                    spnDistrict.setAdapter(C.getArrayAdapter("select ZIlLAID||'-'||ZILLANAMEENG DistName from Zilla where DIVID='" + Global.Left(spnDiv.getSelectedItem().toString(), 2) + "'"));
                    //spnDCode.setSelection(DivUpazilaUnionSelect("DCode"));

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });

            secDistrict = (LinearLayout) findViewById(R.id.secDistrict);
            VlblDistrict = (TextView) findViewById(R.id.VlblDistrict);
            spnDistrict = (Spinner) findViewById(R.id.spnDistrict);


            spnDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    spnUpazilla.setAdapter(C.getArrayAdapter("Select '  ' UPAZILAName from UPAZILA union select substr('0' || UpazilaId, -2, 2)||'-'||UPAZILANAMEENG UPAZILAName from UPAZILA where ZIlLAID='" + Global.Left(spnDistrict.getSelectedItem().toString(), 2) + "'"));
                    //spnDCode.setSelection(DivUpazilaUnionSelect("DCode"));

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });
            secUpazilla = (LinearLayout) findViewById(R.id.secUpazilla);
            VlblUpazilla = (TextView) findViewById(R.id.VlblUpazilla);

            spnUpazilla = (Spinner) findViewById(R.id.spnUpazilla);

            spnUpazilla.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    spnUnion.setAdapter(C.getArrayAdapter("Select '  ' UnionsName from Unions union select substr('0' || UNIONID, -2, 2)||'-'||UNIONNAMEENG UnionsName from Unions where ZIlLAID='" + Global.Left(spnDistrict.getSelectedItem().toString(), 2) + "' and UpazilaId='" + Global.Left(spnUpazilla.getSelectedItem().toString(), 2) + "'"));


                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });

            secUnion = (LinearLayout) findViewById(R.id.secUnion);
            VlblUnion = (TextView) findViewById(R.id.VlblUnion);
            spnUnion = (Spinner) findViewById(R.id.spnUnion);

            spnUnion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    spnMouza.setAdapter(C.getArrayAdapter("Select '  ' MOUZAIDName from Mouza union select MOUZAID||'-'||MOUZANAMEENG MOUZAIDName from Mouza where ZIlLAID='" + Global.Left(spnDistrict.getSelectedItem().toString(), 2) + "' and UpazilaId='" + Global.Left(spnUpazilla.getSelectedItem().toString(), 2) + "' and UNIONID='" + Global.Left(spnUnion.getSelectedItem().toString(), 2) + "'"));


                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });

            secMouza = (LinearLayout) findViewById(R.id.secMouza);
            VlblMouza = (TextView) findViewById(R.id.VlblMouza);
            spnMouza = (Spinner) findViewById(R.id.spnMouza);

            spnMouza.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    spnVillage.setAdapter(C.getArrayAdapter("Select '  ' VillageName from Village union select substr('0' || VILLAGEID, -2, 2)||'-'||VILLAGENAMEENG VillageName from Village where ZIlLAID='" + Global.Left(spnDistrict.getSelectedItem().toString(), 2) + "' and UPAZILAID='" + Global.Left(spnUpazilla.getSelectedItem().toString(), 2) + "' and UNIONID='" + Global.Left(spnUnion.getSelectedItem().toString(), 2) + "' and UNIONID='" + Global.Left(spnUnion.getSelectedItem().toString(), 2) + "'"));


                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });

            spnSubBlock = (Spinner) findViewById(R.id.spnSubBlock);
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
            spnSubBlock.setAdapter(adptrEPIBlock);
            secVillage = (LinearLayout) findViewById(R.id.secVillage);
            VlblVillage = (TextView) findViewById(R.id.VlblVillage);
            // txtVillage=(EditText) findViewById(R.id.txtVillage);
            spnVillage = (Spinner) findViewById(R.id.spnVillage);
            secBari = (LinearLayout) findViewById(R.id.secBari);
            VlblBari = (TextView) findViewById(R.id.VlblBari);
            txtBari = (EditText) findViewById(R.id.txtBari);
            secregisNo = (LinearLayout) findViewById(R.id.secregisNo);
            VlblregisNo = (TextView) findViewById(R.id.VlblregisNo);
            txtregisNo = (EditText) findViewById(R.id.txtregisNo);
            secregDate = (LinearLayout) findViewById(R.id.secregDate);
            VlblregDate = (TextView) findViewById(R.id.VlblregDate);
            dtpregDate = (EditText) findViewById(R.id.dtpregDate);
            seccName = (LinearLayout) findViewById(R.id.seccName);
            VlblcName = (TextView) findViewById(R.id.VlblcName);
            txtcName = (EditText) findViewById(R.id.txtcName);
            secfName = (LinearLayout) findViewById(R.id.secfName);
            VlblfName = (TextView) findViewById(R.id.VlblfName);
            txtfName = (EditText) findViewById(R.id.txtfName);
            secmName = (LinearLayout) findViewById(R.id.secmName);
            VlblmName = (TextView) findViewById(R.id.VlblmName);
            txtmName = (EditText) findViewById(R.id.txtmName);
            secsex = (LinearLayout) findViewById(R.id.secsex);
            Vlblsex = (TextView) findViewById(R.id.Vlblsex);
            rdogrpsex = (RadioGroup) findViewById(R.id.rdogrpsex);
            rdosex1 = (RadioButton) findViewById(R.id.rdosex1);
            rdosex2 = (RadioButton) findViewById(R.id.rdosex2);
            secmobileNo = (LinearLayout) findViewById(R.id.secmobileNo);
            VlblmobileNo = (TextView) findViewById(R.id.VlblmobileNo);
            txtmobileNo = (EditText) findViewById(R.id.txtmobileNo);
            secdob = (LinearLayout) findViewById(R.id.secdob);
            Vlbldob = (TextView) findViewById(R.id.Vlbldob);
            dtpdob = (EditText) findViewById(R.id.dtpdob);
            secage = (LinearLayout) findViewById(R.id.secage);
            Vlblage = (TextView) findViewById(R.id.Vlblage);
            Vlblage1 = (TextView) findViewById(R.id.Vlblage1);
            txtage = (EditText) findViewById(R.id.txtage);


            btnregDate = (ImageButton) findViewById(R.id.btnregDate);
            btnregDate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btnregDate";
                    showDialog(DATE_DIALOG);
                }
            });

            btndob = (ImageButton) findViewById(R.id.btndob);
            btndob.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btndob";
                    showDialog(DATE_DIALOG);
                }
            });


            Button cmdSave = (Button) findViewById(R.id.cmdSave);
            cmdSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    //  DataSave();
                    DataSave();
                    // finish();

                }
            });
        } catch (Exception e) {
            Connection.MessageBox(EpiNonRegistration.this, e.getMessage());
            return;
        }
    }

  /*  private void DataSave()
    {
        try
        {


            String SQL = "";
           // ClientMap cMap = new ClientMap();
           // String id = cMap.getName() + cMap.getFatherName() +cMap.getZillaId()+cMap.getUpazilaId()+cMap.getUnionId()+cMap.getMouzaId()+cMap.getVillageId();
            if (!C.Existence("Select schedulerId,healthId,providerId,upload from " + TableName + "  Where schedulerId='" +g.getepiSchedulerId() + "' and healthId='" +cMap.generateHash(id)+ "'and providerId='" + g.getProvCode() + "'")) {
                SQL = "Insert into " + TableName + "(schedulerId,healthId,providerId,upload)Values('" + g.getepiSchedulerId() + "', '" + cMap.generateHash(id)+ "', '" + g.getProvCode() + "','2')";
                C.Save(SQL);
            }

            SQL = "Update " + TableName + " Set ";
            // SQL+="schedulerId= '"+ g.getepiSchedulerId()+"',";
            //SQL+="healthId = '"+ g.getHealthID()+"',";
            // SQL+="providerId = '"+ g.getProvCode()+"',";
            SQL+="houseNo='',";
            SQL += "regNo = '" + txtregisNo.getText().toString() + "',";
            SQL+="regDate = '"+ Global.DateConvertYMD(dtpregDate.getText().toString()) +"',";
            SQL+="brCertificateNo ='',";
            SQL+="brDate ='',";
            SQL+="remarks ='',";
            SQL+="systemEntryDate = '"+ Global.DateTimeNowYMDHMS()+"',";
            SQL+="upload ='2'";
            SQL += "  Where schedulerId='" + g.getepiSchedulerId() + "' and healthId='" + cMap.generateHash(id)+ "'and providerId='" + g.getProvCode() + "'";
            // SQL+=" where Cluster='"+ cluster +"' and Block='"+ block +"' and HH='"+ hh +"' and StudyID='"+ txtStudyId.getText() +"'and Visit='"+ txtVisitNo.getText()+"'";
            C.Save(SQL);
            Connection.MessageBox(EpiNonRegistration.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
            finish();
        }
        catch(Exception  e)
        {
            Connection.MessageBox(EpiNonRegistration.this, e.getMessage());
            return;
        }
    }*/

    private void DataSave() {
        try {

            if (txtcName.getText().toString().length() == 0 & seccName.isShown()) {
                Connection.MessageBox(EpiNonRegistration.this, "Required field:Name.");
                txtcName.requestFocus();
                return;
            } else if (txtage.getText().toString().length() == 0 & secage.isShown()) {
                Connection.MessageBox(EpiNonRegistration.this, "Required field:Age.");
                txtage.requestFocus();
                return;
            } else if (!rdosex1.isChecked() & !rdosex2.isChecked() & secsex.isShown()) {
                Connection.MessageBox(EpiNonRegistration.this, "Select anyone options from sex.");
                rdosex1.requestFocus();
                return;
            } else if (txtfName.getText().toString().length() == 0 & secfName.isShown()) {
                Connection.MessageBox(EpiNonRegistration.this, "Required field:Father Name.");
                txtfName.requestFocus();
                return;
            } else if (txtmName.getText().toString().length() == 0 & secmName.isShown()) {
                Connection.MessageBox(EpiNonRegistration.this, "Required field:Mother Name.");
                txtmName.requestFocus();
                return;
            } else if (spnDiv.getSelectedItemPosition() == 0 & secDiv.isShown()) {
                Connection.MessageBox(EpiNonRegistration.this, "Required field:Division.");
                // txtDistrict.requestFocus();
                return;
            } else if (spnDistrict.getSelectedItemPosition() == 0 & secDistrict.isShown()) {
                Connection.MessageBox(EpiNonRegistration.this, "Required field:District.");
                // txtDistrict.requestFocus();
                return;
            } else if (spnUpazilla.getSelectedItemPosition() == 0 & secUpazilla.isShown()) {
                Connection.MessageBox(EpiNonRegistration.this, "Required field:District.");
                // txtDistrict.requestFocus();
                return;
            } else if (spnUnion.getSelectedItemPosition() == 0 & secUnion.isShown()) {
                Connection.MessageBox(EpiNonRegistration.this, "Required field:Union.");
                //txtUnion.requestFocus();
                return;
            } else if (spnVillage.getSelectedItemPosition() == 0 & secVillage.isShown()) {
                Connection.MessageBox(EpiNonRegistration.this, "Required field:Village.");
                //txtVillage.requestFocus();
                return;
            } else if (spnMouza.getSelectedItemPosition() == 0 & secMouza.isShown()) {
                Connection.MessageBox(EpiNonRegistration.this, "Required field:Mouza.");
                // txtMouza.requestFocus();
                return;
            } else if (txtBari.getText().toString().length() == 0 & secBari.isShown()) {
                Connection.MessageBox(EpiNonRegistration.this, "Required field:Bari.");
                txtBari.requestFocus();
                return;
            } else if (txtmobileNo.getText().toString().length() == 0 & secmobileNo.isShown()) {
                Connection.MessageBox(EpiNonRegistration.this, "Required field:Mobile No.");
                txtmobileNo.requestFocus();
                return;
            }

            String SQL = "";
            ClientMap cMap = new ClientMap();

            cMap.setProviderId(g.getProvCode());
            cMap.setName(((EditText) findViewById(R.id.txtcName)).getText().toString());
            cMap.setAge(((EditText) findViewById(R.id.txtage)).getText().toString());
            cMap.setDoB(Global.DateConvertYMD(dtpdob.getText().toString()));
            RadioButton rbsex = (RadioButton) findViewById(rdogrpsex.getCheckedRadioButtonId());
            cMap.setGender((rbsex == null ? "" : (Global.Left(rbsex.getText().toString(), 1))));
            cMap.setHusbandName("");
            cMap.setFatherName(((EditText) findViewById(R.id.txtfName)).getText().toString());
            cMap.setMotherName(((EditText) findViewById(R.id.txtmName)).getText().toString());
            cMap.setDivisionId(Global.Left(spnDiv.getSelectedItem().toString(), 2));
            cMap.setZillaId(Global.Left(spnDistrict.getSelectedItem().toString(), 2));
            cMap.setUpazilaId(Global.Left(spnUpazilla.getSelectedItem().toString(), 2));
            cMap.setUnionId(Global.Left(spnUnion.getSelectedItem().toString(), 2));
            cMap.setMouzaId(Global.Left(spnMouza.getSelectedItem().toString(), 3));
            cMap.setVillageId(Global.Left(spnVillage.getSelectedItem().toString(), 2));
            cMap.setSubBlock(Global.Left(spnSubBlock.getSelectedItem().toString(), 2));
            cMap.setHouseGRHoldingNo(((EditText) findViewById(R.id.txtBari)).getText().toString());
            cMap.setMobileNo(((EditText) findViewById(R.id.txtmobileNo)).getText().toString());
            cMap.setSystemEntryDate(Global.DateTimeNowYMDHMS());
            cMap.setUpload("2");
            String id = cMap.getName() + cMap.getFatherName() + cMap.getMotherName() + cMap.getDoB() + cMap.getZillaId() + cMap.getUpazilaId() + cMap.getUnionId() + cMap.getMouzaId() + cMap.getVillageId();
            //String id = cMap.getName() + cMap.getFatherName() +cMap.getZillaId()+cMap.getUpazilaId()+cMap.getUnionId()+cMap.getMouzaId()+cMap.getVillageId();
            cMap.setGeneratedId(cMap.generateHash(id));
            cMap.Save(cMap, getApplicationContext());

            if (!C.Existence("Select schedulerId,healthId,providerId,upload from " + TableName + "  Where schedulerId='" + g.getepiSchedulerId() + "' and healthId='" + cMap.getGeneratedId() + "'and providerId='" + g.getProvCode() + "'")) {
                SQL = "Insert into " + TableName + "(schedulerId,healthId,providerId,upload)Values('" + g.getepiSchedulerId() + "', '" + cMap.getGeneratedId() + "', '" + g.getProvCode() + "','2')";
                C.Save(SQL);
            }

            SQL = "Update " + TableName + " Set ";
            // SQL+="schedulerId= '"+ g.getepiSchedulerId()+"',";
            //SQL+="healthId = '"+ g.getHealthID()+"',";
            // SQL+="providerId = '"+ g.getProvCode()+"',";
            SQL += "houseNo='',";
            SQL += "regNo = '" + txtregisNo.getText().toString() + "',";
            SQL += "regDate = '" + Global.DateConvertYMD(dtpregDate.getText().toString()) + "',";
            SQL += "brCertificateNo ='',";
            SQL += "brDate ='',";
            SQL += "remarks ='',";
            SQL += "systemEntryDate = '" + Global.DateTimeNowYMDHMS() + "',";
            SQL += "upload ='2'";
            SQL += "  Where schedulerId='" + g.getepiSchedulerId() + "' and healthId='" + cMap.getGeneratedId() + "'and providerId='" + g.getProvCode() + "'";
            // SQL+=" where Cluster='"+ cluster +"' and Block='"+ block +"' and HH='"+ hh +"' and StudyID='"+ txtStudyId.getText() +"'and Visit='"+ txtVisitNo.getText()+"'";
            C.Save(SQL);
            Connection.MessageBox(EpiNonRegistration.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
            finish();
            //Connection.MessageBox(EpiNonRegistration.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
            //finish();

        } catch (Exception e) {
            Connection.MessageBox(EpiNonRegistration.this, e.getMessage());
            return;
        }
    }

    private void DataSearch(String regisNo) {
        try {

            RadioButton rb;
            Cursor cur = C.ReadData("Select * from " + TableName + "  Where regisNo='" + regisNo + "'");
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                spnDistrict.setSelection(Global.SpinnerItemPosition(spnDistrict, 2, cur.getString(cur.getColumnIndex("District"))));
                // txtDistrict.setText(cur.getString(cur.getColumnIndex("District")));
                //txtUpazilla.setText(cur.getString(cur.getColumnIndex("Upazilla")));
                spnUpazilla.setSelection(Global.SpinnerItemPosition(spnUpazilla, 2, cur.getString(cur.getColumnIndex("Upazilla"))));
                spnUnion.setSelection(Global.SpinnerItemPosition(spnUnion, 2, cur.getString(cur.getColumnIndex("Union"))));
                spnMouza.setSelection(Global.SpinnerItemPosition(spnMouza, 3, cur.getString(cur.getColumnIndex("Mouza"))));
                spnVillage.setSelection(Global.SpinnerItemPosition(spnVillage, 2, cur.getString(cur.getColumnIndex("Village"))));

                // txtUnion.setText(cur.getString(cur.getColumnIndex("Union")));
                //txtVillage.setText(cur.getString(cur.getColumnIndex("Village")));
                //txtMouza.setText(cur.getString(cur.getColumnIndex("Mouza")));
                txtBari.setText(cur.getString(cur.getColumnIndex("Bari")));
                txtregisNo.setText(cur.getString(cur.getColumnIndex("regisNo")));
                dtpregDate.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("regDate"))));
                txtcName.setText(cur.getString(cur.getColumnIndex("cName")));
                txtfName.setText(cur.getString(cur.getColumnIndex("fName")));
                txtmName.setText(cur.getString(cur.getColumnIndex("mName")));
                for (int i = 0; i < rdogrpsex.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpsex.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("sex"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                txtmobileNo.setText(cur.getString(cur.getColumnIndex("mobileNo")));
                dtpdob.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("dob"))));
                txtage.setText(cur.getString(cur.getColumnIndex("age")));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(EpiNonRegistration.this, e.getMessage());
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


            dtpDate = (EditText) findViewById(R.id.dtpregDate);
            if (VariableID.equals("btnregDate")) {
                dtpDate = (EditText) findViewById(R.id.dtpregDate);
            } else if (VariableID.equals("btndob")) {
                dtpDate = (EditText) findViewById(R.id.dtpdob);
            }
            dtpDate.setText(new StringBuilder()
                    .append(Global.Right("00" + mDay, 2)).append("/")
                    .append(Global.Right("00" + mMonth, 2)).append("/")
                    .append(mYear));

            //  String Age = String.valueOf(Global.DateDifferenceMonth( Global.DateNowDMY(), dtpDate.getText().toString()));
            //txtage.setText(Age);
            //Vlblage1.setText("মাস");

            if (dtpdob.getText().length() > 0) {
                //  String Age = String.valueOf(Global.DateDifferenceMonth( Global.DateNowDMY(), dtpdob.getText().toString()));
                String AgeY = String.valueOf(Global.DateDifferenceYears(Global.DateNowDMY(), dtpdob.getText().toString()));
                // String AgeD = String.valueOf(Global.DateDifferenceDays( Global.DateNowDMY(), dtpdob.getText().toString()));

                txtage.setText(AgeY);
                Vlblage1.setText("বছর");
            }

          /*  String AgeY = String.valueOf(Global.DateDifferenceYears( Global.DateNowDMY(), dtpDate.getText().toString()));
            String AgeD = String.valueOf(Global.DateDifferenceDays( Global.DateNowDMY(), dtpDate.getText().toString()));
          */


         /*   if(Integer.valueOf(Age)<11)
            {
                txtage.setText(Age);
                Vlblage1.setText("মাস");
            }

            else if(Integer.valueOf(Age)>11)

            {

                txtage.setText(AgeY);
                Vlblage1.setText("বছর");
            }*/


        }
    };
    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
            hour = selectedHour;
            minute = selectedMinute;
            EditText tpTime;


            //   tpTime.setText(new StringBuilder().append(Global.Right("00"+hour,2)).append(":").append(Global.Right("00"+minute,2)));

        }
    };
}