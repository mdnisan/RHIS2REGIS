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
import android.view.Window;
import android.widget.AdapterView;
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

import Common.Connection;
import Common.Global;

/**
 * Created by Nisan on 12/29/2015.
 */
public class nrc extends Activity {
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
        AlertDialog.Builder adb = new AlertDialog.Builder(nrc.this);
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
    EditText txthusName;
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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.nrc);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();

            TableName = "ClientMap";

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
                    spnDistrict.setAdapter(C.getArrayAdapter("select ZIlLAID||'-'||ZILLANAMEENG DistName from Zilla where DIVID='" + Global.Left(spnDiv.getSelectedItem().toString(), 2) + "'"));// Global.Left(spnDiv.getSelectedItem().toString(),2)
                    spnDistrict.setSelection(DivzillaSelect("zilla"));


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
                    //spnUpazilla.setSelection(DivUpazilaUnionSelect("union"));


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
                    //


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
                    //  spnMouza.setSelection(DivUpazilaUnionSelect("mouza"));


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
                    spnVillage.setAdapter(C.getArrayAdapter("Select '  ' VillageName from Village union select substr('0' || VILLAGEID, -2, 2)||'-'||VILLAGENAMEENG VillageName from Village where ZIlLAID='" + Global.Left(spnDistrict.getSelectedItem().toString(), 2) + "' and UPAZILAID='" + Global.Left(spnUpazilla.getSelectedItem().toString(), 2) + "' and UNIONID='" + Global.Left(spnUnion.getSelectedItem().toString(), 2) + "'"));

                    // spnVillage.setSelection(DivUpazilaUnionSelect("villageId"));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });
            secVillage = (LinearLayout) findViewById(R.id.secVillage);
            VlblVillage = (TextView) findViewById(R.id.VlblVillage);
            // txtVillage=(EditText) findViewById(R.id.txtVillage);
            spnVillage = (Spinner) findViewById(R.id.spnVillage);
            secBari = (LinearLayout) findViewById(R.id.secBari);
            VlblBari = (TextView) findViewById(R.id.VlblBari);
            txtBari = (EditText) findViewById(R.id.txtBari);
            txtBari.setText(g.getHouseholdNo());
            //txtBari.setEnabled(false);
           /* secregisNo=(LinearLayout)findViewById(R.id.secregisNo);
            VlblregisNo=(TextView) findViewById(R.id.VlblregisNo);
            txtregisNo=(EditText) findViewById(R.id.txtregisNo);
            secregDate=(LinearLayout)findViewById(R.id.secregDate);
            VlblregDate=(TextView) findViewById(R.id.VlblregDate);
            dtpregDate=(EditText) findViewById(R.id.dtpregDate);*/
            seccName = (LinearLayout) findViewById(R.id.seccName);
            VlblcName = (TextView) findViewById(R.id.VlblcName);
            txtcName = (EditText) findViewById(R.id.txtcName);
            secfName = (LinearLayout) findViewById(R.id.secfName);
            VlblfName = (TextView) findViewById(R.id.VlblfName);
            txtfName = (EditText) findViewById(R.id.txtfName);
            secmName = (LinearLayout) findViewById(R.id.secmName);
            VlblmName = (TextView) findViewById(R.id.VlblmName);
            txtmName = (EditText) findViewById(R.id.txtmName);
            txthusName = (EditText) findViewById(R.id.txthusName);
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



        /*    btnregDate = (ImageButton) findViewById(R.id.btnregDate);
            btnregDate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) { VariableID = "btnregDate"; showDialog(DATE_DIALOG); }});*/

            btndob = (ImageButton) findViewById(R.id.btndob);
            btndob.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btndob";
                    showDialog(DATE_DIALOG);
                }
            });


            //  DataSearch(g.getHealthID());
            Button cmdSave = (Button) findViewById(R.id.cmdSave);
            cmdSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    if (C.ReturnSingleValue("Select generatedId from clientMap where generatedId='" + g.getHealthID() + "'").equalsIgnoreCase(g.getHealthID())) {
                        DataUpdate();
                    } else {
                        DataSave();
                    }


                }
            });
        } catch (Exception e) {
            Connection.MessageBox(nrc.this, e.getMessage());
            return;
        }
    }

    private void DataUpdate() {
        try {

            if (txtcName.getText().toString().length() == 0 & seccName.isShown()) {
                Connection.MessageBox(nrc.this, "Required field:Name.");
                txtcName.requestFocus();
                return;
            } else if (txtage.getText().toString().length() == 0 & secage.isShown()) {
                Connection.MessageBox(nrc.this, "Required field:Age.");
                txtage.requestFocus();
                return;
            } else if (!rdosex1.isChecked() & !rdosex2.isChecked() & secsex.isShown()) {
                Connection.MessageBox(nrc.this, "Select anyone options from sex.");
                rdosex1.requestFocus();
                return;
            } else if (txtfName.getText().toString().length() == 0 & secfName.isShown()) {
                Connection.MessageBox(nrc.this, "Required field:Father Name.");
                txtfName.requestFocus();
                return;
            } else if (txtmName.getText().toString().length() == 0 & secmName.isShown()) {
                Connection.MessageBox(nrc.this, "Required field:Mother Name.");
                txtmName.requestFocus();
                return;
            } else if (spnDiv.getSelectedItemPosition() == 0 & secDiv.isShown()) {
                Connection.MessageBox(nrc.this, "Required field:Division.");
                // txtDistrict.requestFocus();
                return;
            }
           /* else if(spnDistrict.getSelectedItemPosition()==0 & secDistrict.isShown())
            {
                Connection.MessageBox(nrc.this, "Required field:District.");
               // txtDistrict.requestFocus();
                return;
            }*/
            else if (spnUpazilla.getSelectedItemPosition() == 0 & secUpazilla.isShown()) {
                Connection.MessageBox(nrc.this, "Required field:District.");
                // txtDistrict.requestFocus();
                return;
            } else if (spnUnion.getSelectedItemPosition() == 0 & secUnion.isShown()) {
                Connection.MessageBox(nrc.this, "Required field:Union.");
                //txtUnion.requestFocus();
                return;
            } else if (spnVillage.getSelectedItemPosition() == 0 & secVillage.isShown()) {
                Connection.MessageBox(nrc.this, "Required field:Village.");
                //txtVillage.requestFocus();
                return;
            } else if (spnMouza.getSelectedItemPosition() == 0 & secMouza.isShown()) {
                Connection.MessageBox(nrc.this, "Required field:Mouza.");
                // txtMouza.requestFocus();
                return;
            } else if (txtBari.getText().toString().length() == 0 & secBari.isShown()) {
                Connection.MessageBox(nrc.this, "Required field:Bari.");
                txtBari.requestFocus();
                return;
            } else if (txtmobileNo.getText().toString().length() == 0 & secmobileNo.isShown()) {
                Connection.MessageBox(nrc.this, "Required field:Mobile No.");
                txtmobileNo.requestFocus();
                return;
            } else if (dtpdob.getText().toString().length() == 0 & secdob.isShown()) {
                Connection.MessageBox(nrc.this, "Required field:DOB.");
                dtpdob.requestFocus();
                return;
            }
            String SQL = "";
            //  divisionId,zillaId,upazilaId,unionId,mouzaId,villageId
            SQL = "Update " + TableName + " Set ";
            SQL += "divisionId = '" + (Global.Left(spnDiv.getSelectedItem().toString(), 2)) + "',";
            SQL += "zillaId = '" + (Global.Left(spnDistrict.getSelectedItem().toString(), 2)) + "',";
            SQL += "upazilaId = '" + (spnUpazilla.getSelectedItemPosition() == 0 ? "" : Global.Left(spnUpazilla.getSelectedItem().toString(), 2)) + "',";
            SQL += "unionId = '" + (Global.Left(spnUnion.getSelectedItem().toString(), 2)) + "',";
            SQL += "mouzaId = '" + (Global.Left(spnMouza.getSelectedItem().toString(), 3)) + "',";
            SQL += "villageId = '" + (Global.Left(spnVillage.getSelectedItem().toString(), 2)) + "',";
            SQL += "name = '" + txtcName.getText().toString() + "',";
            RadioButton rbsex = (RadioButton) findViewById(rdogrpsex.getCheckedRadioButtonId());
            SQL += " gender = '" + (rbsex == null ? "" : (Global.Left(rbsex.getText().toString(), 1))) + "',";
            SQL += "fatherName = '" + txtfName.getText().toString() + "',";
            SQL += "houseGrHoldingNo = '" + txtBari.getText().toString() + "',";
            SQL += "motherName = '" + txtmName.getText().toString() + "',";
            SQL += "husbandName = '" + txthusName.getText().toString() + "',";
            SQL += " mobileNo = '" + txtmobileNo.getText().toString() + "',";
            SQL += "dob = '" + Global.DateConvertYMD(dtpdob.getText().toString()) + "',";
            SQL += "age = '" + txtage.getText().toString() + "',";
            SQL += "modifyDate = '" + Global.DateTimeNowYMDHMS() + "',";
            SQL += "systemEntryDate = '" + Global.DateTimeNowYMDHMS() + "',";
            SQL += "upload ='2'";
            SQL += "  Where generatedId='" + g.getHealthID() + "'";
            // SQL+=" where Cluster='"+ cluster +"' and Block='"+ block +"' and HH='"+ hh +"' and StudyID='"+ txtStudyId.getText() +"'and Visit='"+ txtVisitNo.getText()+"'";
            C.Save(SQL);
            Connection.MessageBox(nrc.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
            // finish();
        } catch (Exception e) {
            Connection.MessageBox(nrc.this, e.getMessage());
            return;
        }
    }

    private void DataSave() {
        try {

            if (txtcName.getText().toString().length() == 0 & seccName.isShown()) {
                Connection.MessageBox(nrc.this, "Required field:Name.");
                txtcName.requestFocus();
                return;
            } else if (txtage.getText().toString().length() == 0 & secage.isShown()) {
                Connection.MessageBox(nrc.this, "Required field:Age.");
                txtage.requestFocus();
                return;
            } else if (!rdosex1.isChecked() & !rdosex2.isChecked() & secsex.isShown()) {
                Connection.MessageBox(nrc.this, "Select anyone options from sex.");
                rdosex1.requestFocus();
                return;
            } else if (txtfName.getText().toString().length() == 0 & secfName.isShown()) {
                Connection.MessageBox(nrc.this, "Required field:Father Name.");
                txtfName.requestFocus();
                return;
            } else if (txtmName.getText().toString().length() == 0 & secmName.isShown()) {
                Connection.MessageBox(nrc.this, "Required field:Mother Name.");
                txtmName.requestFocus();
                return;
            } else if (spnDiv.getSelectedItemPosition() == 0 & secDiv.isShown()) {
                Connection.MessageBox(nrc.this, "Required field:Division.");
                // txtDistrict.requestFocus();
                return;
            }
           /* else if(spnDistrict.getSelectedItemPosition()==0 & secDistrict.isShown())
            {
                Connection.MessageBox(nrc.this, "Required field:District.");
               // txtDistrict.requestFocus();
                return;
            }*/
            else if (spnUpazilla.getSelectedItemPosition() == 0 & secUpazilla.isShown()) {
                Connection.MessageBox(nrc.this, "Required field:District.");
                // txtDistrict.requestFocus();
                return;
            } else if (spnUnion.getSelectedItemPosition() == 0 & secUnion.isShown()) {
                Connection.MessageBox(nrc.this, "Required field:Union.");
                //txtUnion.requestFocus();
                return;
            } else if (spnVillage.getSelectedItemPosition() == 0 & secVillage.isShown()) {
                Connection.MessageBox(nrc.this, "Required field:Village.");
                //txtVillage.requestFocus();
                return;
            } else if (spnMouza.getSelectedItemPosition() == 0 & secMouza.isShown()) {
                Connection.MessageBox(nrc.this, "Required field:Mouza.");
                // txtMouza.requestFocus();
                return;
            } else if (txtBari.getText().toString().length() == 0 & secBari.isShown()) {
                Connection.MessageBox(nrc.this, "Required field:Bari.");
                txtBari.requestFocus();
                return;
            } else if (txtmobileNo.getText().toString().length() == 0 & secmobileNo.isShown()) {
                Connection.MessageBox(nrc.this, "Required field:Mobile No.");
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
            cMap.setHusbandName(((EditText) findViewById(R.id.txthusName)).getText().toString());
            cMap.setFatherName(((EditText) findViewById(R.id.txtfName)).getText().toString());
            cMap.setMotherName(((EditText) findViewById(R.id.txtmName)).getText().toString());
            cMap.setDivisionId(Global.Left(spnDiv.getSelectedItem().toString(), 2));
            cMap.setZillaId(Global.Left(spnDistrict.getSelectedItem().toString(), 2));
            cMap.setUpazilaId(Global.Left(spnUpazilla.getSelectedItem().toString(), 2));
            cMap.setUnionId(Global.Left(spnUnion.getSelectedItem().toString(), 2));
            cMap.setMouzaId(Global.Left(spnMouza.getSelectedItem().toString(), 3));
            cMap.setVillageId(Global.Left(spnVillage.getSelectedItem().toString(), 2));
            cMap.setHouseGRHoldingNo(((TextView) findViewById(R.id.txtBari)).getText().toString());
            cMap.setMobileNo(((EditText) findViewById(R.id.txtmobileNo)).getText().toString());
            cMap.setSystemEntryDate(Global.DateTimeNowYMDHMS());
            cMap.setUpload("2");
            String id = cMap.getName() + cMap.getFatherName() + cMap.getZillaId() + cMap.getUpazilaId() + cMap.getUnionId() + cMap.getMouzaId() + cMap.getVillageId();
            cMap.setGeneratedId(cMap.generateHash(id));
            cMap.Save(cMap, getApplicationContext());
            g.setGeneratedId(cMap.getGeneratedId());

            Connection.MessageBox(nrc.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
            finish();
            // EventForm(g.getProvType(), g.getProvCode(), g.getHouseholdNo(), g.getSLno(), cMap.getName(), cMap.getAge(), "", cMap.getGender(), cMap.getGeneratedId());


        } catch (Exception e) {
            Connection.MessageBox(nrc.this, e.getMessage());
            return;
        }
    }

    private Integer DivzillaSelect(String U) {
        Integer i = 0;
        Cursor cur = C.ReadData("Select divisionId,zillaId,upazilaId,unionId,mouzaId,villageId from clientMap  Where generatedId='" + g.getHealthID() + "'");
        cur.moveToFirst();

        while (!cur.isAfterLast()) {
//divisionId,zillaId,upazilaId,unionId,mouzaId,villageId

            if (U.equals("div")) {
                i = Global.SpinnerItemPosition(spnDiv, 2, cur.getString(cur.getColumnIndex("divisionId")));
            }

            if (U.equals("zilla")) {
                i = Global.SpinnerItemPosition(spnDistrict, 2, cur.getString(cur.getColumnIndex("zillaId")));
            }


          /* else if(U.equals("upazila"))
            {
                i=Global.SpinnerItemPosition(spnUpazilla, 2, cur.getString(cur.getColumnIndex("upazilaId")));
            }

            else if(U.equals("union"))
            {
                i=Global.SpinnerItemPosition(spnUnion, 2, cur.getString(cur.getColumnIndex("unionId")));
            }
            else  if(U.equals("mouza"))
            {
                i=Global.SpinnerItemPosition(spnMouza, 3, cur.getString(cur.getColumnIndex("mouzaId")));
            }
            if(U.equals("village"))
            {
                i=Global.SpinnerItemPosition(spnVillage, 2, cur.getString(cur.getColumnIndex("villageId")));
            }*/
            cur.moveToNext();
        }
        cur.close();

        return i;
    }


    private Integer DivUpazilaUnionSelect(String U) {
        Integer i = 0;
        Cursor cur = C.ReadData("Select divisionId,zillaId,upazilaId,unionId,mouzaId,villageId from clientMap  Where generatedId='" + g.getHealthID() + "'");
        cur.moveToFirst();

        while (!cur.isAfterLast()) {
//divisionId,zillaId,upazilaId,unionId,mouzaId,villageId

      /*      if(U.equals("div"))
            {
                i=Global.SpinnerItemPosition(spnDiv, 2, cur.getString(cur.getColumnIndex("divisionId")));
            }

            else if(U.equals("zilla"))
            {
                i=Global.SpinnerItemPosition(spnDistrict, 2, cur.getString(cur.getColumnIndex("zillaId")));
            }
*/

            if (U.equals("upazila")) {
                i = Global.SpinnerItemPosition(spnUpazilla, 2, cur.getString(cur.getColumnIndex("upazilaId")));
            } else if (U.equals("union")) {
                i = Global.SpinnerItemPosition(spnUnion, 2, cur.getString(cur.getColumnIndex("unionId")));
            } else if (U.equals("mouza")) {
                i = Global.SpinnerItemPosition(spnMouza, 3, cur.getString(cur.getColumnIndex("mouzaId")));
            }
            if (U.equals("village")) {
                i = Global.SpinnerItemPosition(spnVillage, 2, cur.getString(cur.getColumnIndex("villageId")));
            }
            cur.moveToNext();
        }
        cur.close();

        return i;
    }

    private void DataSearch(String generatedId) {
        try {

            RadioButton rb;
            Cursor cur = C.ReadData("Select * from clientMap  Where generatedId='" + generatedId + "'");
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                //divisionId,zillaId,upazilaId,unionId,mouzaId,villageId
                spnDiv.setSelection(Global.SpinnerItemPosition(spnDiv, 2, cur.getString(cur.getColumnIndex("divisionId"))));
                spnDistrict.setSelection(Global.SpinnerItemPosition(spnDistrict, 2, cur.getString(cur.getColumnIndex("zillaId"))));
                spnUpazilla.setSelection(Global.SpinnerItemPosition(spnUpazilla, 2, cur.getString(cur.getColumnIndex("upazilaId"))));
                spnUnion.setSelection(Global.SpinnerItemPosition(spnUnion, 2, cur.getString(cur.getColumnIndex("unionId"))));
                spnMouza.setSelection(Global.SpinnerItemPosition(spnMouza, 3, cur.getString(cur.getColumnIndex("mouzaId"))));
                spnVillage.setSelection(Global.SpinnerItemPosition(spnVillage, 2, cur.getString(cur.getColumnIndex("villageId"))));
                txtBari.setText(cur.getString(cur.getColumnIndex("houseGrHoldingNo")));
                txtcName.setText(cur.getString(cur.getColumnIndex("name")));

                txtfName.setText(cur.getString(cur.getColumnIndex("fatherName")));
                txtmName.setText(cur.getString(cur.getColumnIndex("motherName")));
                txthusName.setText(cur.getString(cur.getColumnIndex("husbandName")));
                for (int i = 0; i < rdogrpsex.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpsex.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("gender"))))
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
            Connection.MessageBox(nrc.this, e.getMessage());
            return;
        }
    }

    private void EventForm(final String ProvType, final String ProvCode, String HH, final String SNo, final String MemName, final String Age, final String MS, final String Sex, final String HealthId) {
        try {
            final Dialog dialog = new Dialog(nrc.this);
            dialog.setTitle("Events");
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.eventsform);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);

            final ImageButton cmdEdit = (ImageButton) dialog.findViewById(R.id.cmdEdit);
            final ImageButton cmdDelete = (ImageButton) dialog.findViewById(R.id.cmdDelete);
            final LinearLayout secEdit = (LinearLayout) dialog.findViewById(R.id.secEdit);
            final LinearLayout secDelete = (LinearLayout) dialog.findViewById(R.id.secDelete);
            final TextView memName = (TextView) dialog.findViewById(R.id.memName);
            memName.setText(SNo + "-" + MemName);
            //final Button PregWomentReg = (Button)dialog.findViewById(R.id.cmd5);

            /*final Button cmdEligibleCouple = (Button) dialog.findViewById(R.id.cmdEligibleCouple);
            final Button cmdAge0to1 = (Button)dialog.findViewById(R.id.cmd1);
            final Button cmdAge0to5 = (Button)dialog.findViewById(R.id.cmd2);
            final Button cmdAdolescent = (Button)dialog.findViewById(R.id.cmd3);
            final Button cmdInjectable = (Button)dialog.findViewById(R.id.cmd4);
            final Button cmdPreg = (Button)dialog.findViewById(R.id.cmd5);*/
            final Button DeathForm = (Button) dialog.findViewById(R.id.cmd6);
            /*final Button cmdChildImmunization = (Button)dialog.findViewById(R.id.cmd8);
            final Button cmdWomanImmunization = (Button)dialog.findViewById(R.id.cmd9);*/
            /*if(MS.equals("2") & Sex.equals("1"))//couple
            {
                if(IsUserHA(ProvType))
                {
                    cmdEligibleCouple.setEnabled(false);
                }
                else
                {
                    cmdEligibleCouple.setEnabled(true);
                }
                cmdAge0to1.setEnabled(false);
                cmdAge0to5.setEnabled(false);
                cmdAdolescent.setEnabled(false);
                cmdInjectable.setEnabled(false);
                cmdPreg.setEnabled(false);
                cmdChildImmunization.setEnabled(false);
                cmdWomanImmunization.setEnabled(false);
            }
            else if(MS.equals("2") & Sex.equals("2") & Integer.valueOf(Age) <= 49)//couple
            {

                cmdAge0to1.setEnabled(false);
                cmdAge0to5.setEnabled(false);
                cmdAdolescent.setEnabled(false);
                cmdChildImmunization.setEnabled(false);

                if(IsUserHA(ProvType)) {
                    cmdInjectable.setEnabled(false);
                    cmdPreg.setEnabled(false);
                    cmdEligibleCouple.setEnabled(false);
                }
                else {
                    cmdInjectable.setEnabled(true);
                    cmdPreg.setEnabled(true);
                    cmdEligibleCouple.setEnabled(true);
                }

            }
            else if(Integer.valueOf(Age) <= 1)
            {
                cmdEligibleCouple.setEnabled(false);
                if(IsUserHA(ProvType)) {
                    cmdAge0to1.setEnabled(false);
                    cmdAge0to5.setEnabled(false);
                }
                else {
                    cmdAge0to1.setEnabled(true);
                    cmdAge0to5.setEnabled(true);
                }
                cmdAdolescent.setEnabled(false);
                cmdInjectable.setEnabled(false);
                cmdPreg.setEnabled(false);
            }
            else if(Integer.valueOf(Age) <= 5)
            {
                cmdEligibleCouple.setEnabled(false);
                if(IsUserHA(ProvType)) {
                    cmdAge0to1.setEnabled(false);
                    cmdAge0to5.setEnabled(false);
                    cmdChildImmunization.setEnabled(false);
                }
                else {

                    cmdAge0to1.setEnabled(true);
                    cmdAge0to5.setEnabled(true);
                    cmdChildImmunization.setEnabled(true);
                }
                cmdAdolescent.setEnabled(false);
                cmdInjectable.setEnabled(false);
                cmdPreg.setEnabled(false);

                cmdWomanImmunization.setEnabled(false);
            }

            else if(Integer.valueOf(Age) >= 5 & MS.equals("1"))
            {
                cmdEligibleCouple.setEnabled(false);
                cmdAge0to1.setEnabled(false);
                cmdAge0to5.setEnabled(false);
                if(IsUserHA(ProvType)) {
                    cmdAdolescent.setEnabled(false);
                }
                else{
                    cmdAdolescent.setEnabled(true);}
                cmdInjectable.setEnabled(false);
                cmdPreg.setEnabled(false);
                cmdChildImmunization.setEnabled(false);
                cmdWomanImmunization.setEnabled(false);
            }
            else if(Integer.valueOf(Age) >= 5 & MS.equals("2"))
            {
                cmdEligibleCouple.setEnabled(false);
                cmdAge0to1.setEnabled(false);
                cmdAge0to5.setEnabled(false);
                cmdAdolescent.setEnabled(false);
                cmdInjectable.setEnabled(false);
                cmdPreg.setEnabled(false);
                cmdChildImmunization.setEnabled(false);
                cmdWomanImmunization.setEnabled(false);
            }
            else if((Integer.valueOf(Age) >= 15 && Integer.valueOf(Age) <= 49) & Sex.equals("2"))
            {
                cmdEligibleCouple.setEnabled(false);
                cmdAge0to1.setEnabled(false);
                cmdAge0to5.setEnabled(false);

                if(IsUserHA(ProvType))
                {
                    cmdAdolescent.setEnabled(false);
                    cmdWomanImmunization.setEnabled(true);
                } else
                {
                    cmdAdolescent.setEnabled(true);
                    cmdWomanImmunization.setEnabled(false);
                }

                cmdInjectable.setEnabled(false);
                cmdPreg.setEnabled(false);
                cmdChildImmunization.setEnabled(false);

            }
            else if((Integer.valueOf(Age) >= 10 && Integer.valueOf(Age) <= 19))
            {
                cmdEligibleCouple.setEnabled(false);
                cmdAge0to1.setEnabled(false);
                cmdAge0to5.setEnabled(false);
                if(IsUserHA(ProvType))
                {
                    cmdAdolescent.setEnabled(false);
                }
                else
                {
                    cmdAdolescent.setEnabled(true);
                }

                cmdInjectable.setEnabled(false);
                cmdPreg.setEnabled(false);
                cmdChildImmunization.setEnabled(false);
                cmdWomanImmunization.setEnabled(false);
                //cmdAdolescent.setEnabled(true);
            }
            secEdit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(nrc.this);
                    adb.setTitle("সদস্যের তথ্য সংশোধন");
                    adb.setMessage("আপনি কি [ " + MemName + " ] এর তথ্য পরিবর্তন করতে চান [হাঁ/না]?");

                    adb.setNegativeButton("না", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog1, int which) {
                            dialog.dismiss();
                        }
                    });


                    adb.setPositiveButton("হাঁ", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog1, int which) {
                            g.setProvType(ProvType);
                            g.setProvCode(ProvCode);
                            dialog.dismiss();
                            finish();
                            g.setCallFrom("mu");
                            g.setSerialNo(SNo);
                            Intent f1 = new Intent(getApplicationContext(), MemberForm.class);
                            startActivity(f1);
                        }
                    });

                    adb.show();
                }
            });

            secDelete.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(nrc.this);
                    adb.setTitle("সদস্যের তথ্য মোছা");
                    adb.setMessage("আপনি কি [ " + MemName + " ] এর তথ্য মোছে ফেলতে চান [হাঁ/না]?");

                    adb.setNegativeButton("না", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog1, int which) {
                            dialog.dismiss();
                        }
                    });

                    adb.setPositiveButton("হাঁ", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog1, int which) {
                            dialog.dismiss();
                            C.Save("delete from Member where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and ProvType='" + ProvType + "' and ProvCode='" + ProvCode + "' and HHNo='" + g.getHouseholdNo() + "' and SNo='" + SNo + "'");
                            finish();
                            Intent f1 = new Intent(getApplicationContext(), MemberList.class);
                            startActivity(f1);
                        }
                    });

                    adb.show();
                }
            });

            cmdEligibleCouple.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    g.setSerialNo(SNo);
                    g.setHealthID(HealthId);
                    dialog.dismiss();
                    finish();
                    g.setCallFrom("elco");
                    Intent f1 = new Intent(getApplicationContext(), ELCOForm.class);
                    startActivity(f1);


                }
            });
            cmdInjectable.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    g.setSerialNo(SNo);
                    g.setHealthID(HealthId);
                    dialog.dismiss();
                    finish();
                    //g.setCallFrom("elco");
                    Intent f1 = new Intent(getApplicationContext(), WomanInjectable.class);
                    startActivity(f1);
                }
            });

            cmdPreg.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    g.setSerialNo(SNo);
                    g.setHealthID(HealthId);
                    dialog.dismiss();
                    g.setCallFrom("regis");
                    finish();
                    Intent f1 = new Intent(getApplicationContext(), FWAReg.class);
                    startActivity(f1);
                }
            });

            cmdAge0to5.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    g.setSerialNo(SNo);
                    g.setHealthID(HealthId);
                    dialog.dismiss();
                    finish();
                    Intent f1 = new Intent(getApplicationContext(), Under5child.class);
                    startActivity(f1);
                }
            });


            DeathForm.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    g.setSerialNo(SNo);
                    g.setHealthID(HealthId);
                    dialog.dismiss();
                    finish();
                    Intent f1 = new Intent(getApplicationContext(), DeathForm.class);
                    startActivity(f1);
                }
            });

            cmdChildImmunization.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    g.setSerialNo(SNo);
                    g.setHealthID(HealthId);
                    dialog.dismiss();
                    finish();
                    Intent f1 = new Intent(getApplicationContext(), ChildImmunization.class);
                    startActivity(f1);
                }
            });

            cmdWomanImmunization.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    g.setSerialNo(SNo);
                    g.setHealthID(HealthId);
                    dialog.dismiss();
                    finish();
                    Intent f1 = new Intent(getApplicationContext(), WomanImmunization.class);
                    startActivity(f1);
                }
            });
            cmdAdolescent.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    g.setSerialNo(SNo);
                    g.setHealthID(HealthId);
                    dialog.dismiss();
                    finish();
                    Intent f1 = new Intent(getApplicationContext(), Adolescent.class);
                    startActivity(f1);
                }
            });*/
            dialog.show();
        } catch (Exception ex) {
            Connection.MessageBox(nrc.this, ex.getMessage());
            return;
        }
    }


    private boolean IsUserHA(String provtype) {
        if (provtype.equalsIgnoreCase("02"))
            return true;
        else
            return false;


    }
   /* private void DataSearch(String regisNo)
    {
        try
        {

            RadioButton rb;
            Cursor cur = C.ReadData("Select * from "+ TableName +"  Where regisNo='"+ regisNo +"'");
            cur.moveToFirst();
            while(!cur.isAfterLast())
            {
                spnDistrict.setSelection(Global.SpinnerItemPosition(spnDistrict, 2 ,cur.getString(cur.getColumnIndex("District"))));
               // txtDistrict.setText(cur.getString(cur.getColumnIndex("District")));
                //txtUpazilla.setText(cur.getString(cur.getColumnIndex("Upazilla")));
                spnUpazilla.setSelection(Global.SpinnerItemPosition(spnUpazilla, 2 ,cur.getString(cur.getColumnIndex("Upazilla"))));
                spnUnion.setSelection(Global.SpinnerItemPosition(spnUnion, 2 ,cur.getString(cur.getColumnIndex("Union"))));
                spnMouza.setSelection(Global.SpinnerItemPosition(spnMouza, 3 ,cur.getString(cur.getColumnIndex("Mouza"))));
                spnVillage.setSelection(Global.SpinnerItemPosition(spnVillage, 2 ,cur.getString(cur.getColumnIndex("Village"))));

               // txtUnion.setText(cur.getString(cur.getColumnIndex("Union")));
                //txtVillage.setText(cur.getString(cur.getColumnIndex("Village")));
                //txtMouza.setText(cur.getString(cur.getColumnIndex("Mouza")));
                txtBari.setText(cur.getString(cur.getColumnIndex("Bari")));
                txtregisNo.setText(cur.getString(cur.getColumnIndex("regisNo")));
                dtpregDate.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("regDate"))));
                txtcName.setText(cur.getString(cur.getColumnIndex("cName")));
                txtfName.setText(cur.getString(cur.getColumnIndex("fName")));
                txtmName.setText(cur.getString(cur.getColumnIndex("mName")));
                ((EditText)findViewById(R.id.txthusName)).setText(cur.getColumnIndex("husbandName"));

                for (int i = 0; i < rdogrpsex.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpsex.getChildAt(i);
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
        }
        catch(Exception  e)
        {
            Connection.MessageBox(nrc.this, e.getMessage());
            return;
        }
    }

*/


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


            String Age = String.valueOf(Global.DateDifferenceMonth(Global.DateNowDMY(), dtpDate.getText().toString()));
            String AgeY = String.valueOf(Global.DateDifferenceYears(Global.DateNowDMY(), dtpDate.getText().toString()));
            String AgeD = String.valueOf(Global.DateDifferenceDays(Global.DateNowDMY(), dtpDate.getText().toString()));
            txtage.setText(AgeY);
            Vlblage1.setText("বছর");
           /*if(Integer.valueOf(Age)<12)
            {
                Vlblage1.setText("");
                txtage.setText(Age);
                Vlblage1.setText("মাস");
            }

            else if(Integer.valueOf(Age)>11)

            {
                Vlblage1.setText("");
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