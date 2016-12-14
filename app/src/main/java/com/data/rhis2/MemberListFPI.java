package com.data.rhis2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import Common.Connection;
import Common.Global;

/**
 * Created by ccah on 4/9/2016.
 */
public class MemberListFPI extends Activity {
    SimpleAdapter mSchedule;
    SimpleAdapter eList;
    private static final int UPDATEDONE = 900;
    private ProgressDialog pDialog;
    ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> evmylist = new ArrayList<HashMap<String, String>>();

    Connection C;
    Global g;
    Bundle IDbundle;

    private static String vill;
    private static String bari;
    private static String hhno;
    private static String hhhead;
    private static String totalmember;
    private String ErrMsg;
    private static String vdate;

    TextView txtHHNo;
    RadioGroup rdogrpHouseHold;
    RadioButton rdoHouseHold1;
    RadioButton rdoHouseHold2;
    LinearLayout secHouseHoldStatus;
    RadioGroup rdogrpHouseHoldStatus;
    RadioButton rdoHouseHoldStatus1;
    RadioButton rdoHouseHoldStatus2;
    RadioButton rdoHouseHoldStatus3;

    RadioGroup rdogrpPAddr;
    RadioButton rdoPAddr1;
    RadioButton rdoPAddr2;
    TextView txtPermaAddress;
    RadioGroup rdogrpAddressStatus;
    RadioButton rdoAddressStatus1;
    RadioButton rdoAddressStatus2;
    TextView txtReligion;

    RadioGroup rdogrpVGFCard;
    RadioButton rdoVGFCard1;
    RadioButton rdoVGFCard2;
    LinearLayout secOther;
    EditText txtOther;
    LinearLayout secPAddr;
    LinearLayout secPAddr1;
    LinearLayout secSES;
    LinearLayout secPermaAddress;
    LinearLayout secPermaAddress1;
    LinearLayout secReligion;
    String StartTime;

    TextView FPMethod;
    TextView LMP;
    TextView EDD;

    LinearLayout secSubBlock;
    RadioGroup rdogrpSubBlockStatus;
    RadioButton rdoSubBlock1;
    RadioButton rdoSubBlock2;

    RadioGroup rdogrpPAddrYesNo;
    RadioButton rdoPAddrYes;
    RadioButton rdoPAddrNo;

    RadioGroup rdogrpReligion;
    RadioButton rdoReligion1;
    RadioButton rdoReligion2;
    LinearLayout secSave;
    //Spinner spnSubBlock;
    TextView txtSubBlock;

    ListView list;

    //Top menu
    //--------------------------------------------------------------------------------------------------
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mnuclose, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder adb = new AlertDialog.Builder(MemberListFPI.this);
        switch (item.getItemId()) {
            case R.id.menuClose:
                adb.setTitle("Close");
                adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান?");
                adb.setNegativeButton("না", null);
                adb.setPositiveButton("হ্যাঁ", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        //05 Jun 2015
                        //Update total member and household head name
                        if (IDbundle != null) {
                            if (!IDbundle.getString("search").equalsIgnoreCase("search")) {


                                String SQL = "";
                                SQL = "Update Household set HHHead=(select nameeng from Member where rth='01' and length(extype)=0 and";
                                /*SQL += " Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and ProvType='" + g.getProvType() + "' and ProvCode='" + g.getProvCode() + "' and HHNo='" +g.getHouseholdNo()+ "')";*/
                                SQL += " Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "')";
                                SQL += " where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "'  and HHNo='" + g.getHouseholdNo() + "'";
                                C.Save(SQL);

                                SQL = " Update Household set totalmem=(select count(*)totalmem from Member where length(extype)=0 and";
                                /*SQL += " Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and ProvType='" + g.getProvType() + "' and ProvCode='" + g.getProvCode() + "' and HHNo='" +g.getHouseholdNo()+ "')";*/
                                SQL += " Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "')";
                                SQL += " where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "'";
                                C.Save(SQL);

                                UpdateElcoByHousehold();
                            } else {
                                String SQL = "";

                                SQL = "Update Household set HHHead=(select nameeng from Member where rth='01' and length(extype)=0 and";
                                SQL += " Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "'  and HHNo='" + g.getHouseholdNo() + "')";
                                SQL += " where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "'";
                                C.Save(SQL);

                                SQL = " Update Household set totalmem=(select count(*)totalmem from Member where length(extype)=0 and";
                                SQL += " Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "'  and HHNo='" + g.getHouseholdNo() + "')";
                                SQL += " where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "'";
                                C.Save(SQL);
                                UpdateElcoByHousehold();
                            }

                        } else {
                            String SQL = "";

                            SQL = "Update Household set HHHead=(select nameeng from Member where rth='01' and length(extype)=0 and";
                            SQL += " Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "'  and HHNo='" + g.getHouseholdNo() + "')";
                            SQL += " where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "'";
                            C.Save(SQL);

                            SQL = " Update Household set totalmem=(select count(*)totalmem from Member where length(extype)=0 and";
                            SQL += " Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "'  and HHNo='" + g.getHouseholdNo() + "')";
                            SQL += " where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "'";
                            C.Save(SQL);
                            UpdateElcoByHousehold();
                        }

                        if (g.getCallFrom().equals("sa")) {
                            finish();
                            Intent f1 = new Intent(getApplicationContext(), VillageList.class);
                            startActivity(f1);
                        } else {
                            finish();
                            Intent f1 = new Intent(getApplicationContext(), HouseholdIndex.class);
                            startActivity(f1);
                        }
                    }
                });
                adb.show();
                return true;

        }
        return false;
    }

    private void UpdateElcoByHousehold() {
        String SQL = "UPDATE HOUSEHOLD\n" +
                "   SET Elco =( \n" +
                "           SELECT A.ElcoName AS Elco\n" +
                "             FROM ( \n" +
                "                   SELECT A.DisT,\n" +
                "                          A.Upz,\n" +
                "                          A.UN,\n" +
                "                          A.MOUZA,\n" +
                "                          A.Vill,\n" +
                "                          A.HHno,\n" +
                "                          group_concat( ifnull( A.NameENG, '' ) || '(' || ifnull( B.NameEng, '' ) || ')' ) AS ElcoName\n" +
                "                           FROM Member A\n" +
                "                           LEFT OUTER JOIN Member B\n" +
                "                           ON a.dist = b.dist \n" +
                "                           AND\n" +
                "                        a.upz = b.upz \n" +
                "    AND\n" +
                "    a.un = b.un \n" +
                "    AND\n" +
                "    a.mouza = b.mouza \n" +
                "    AND\n" +
                "    a.vill = b.vill \n" +
                "    AND\n" +
                "    a.hhno = b.hhno \n" +
                "    AND\n" +
                "    CAST ( a.spno1 AS int ) = b.sno\n" +
                "     WHERE A.MS = '2' \n" +
                "           AND\n" +
                "           A.Sex = 2\n" +
                "              AND A.Dist = " + g.getDistrict() + " AND " +
                "                  A.Upz = " + g.getUpazila() + " AND " +
                "                  A.UN = " + g.getUnion() + " AND " +
                "                  A.MOUZA = " + g.getMouza() + " AND " +
                "                  A.Vill = " + g.getVillage() + " AND " +
                "                  A.HHNo = " + g.getHouseholdNo() +
                "     GROUP BY A.DisT,\n" +
                "              A.Upz,\n" +
                "              A.UN,\n" +
                "              A.MOUZA,\n" +
                "              A.Vill,\n" +
                "              A.HHno  \n" +
                "               ) \n" +
                "               AS A\n" +
                "                  INNER JOIN Household household\n" +
                "                          ON A.HHNo = household.HHNo\n" +
                "            WHERE A.dist = household.dist \n" +
                "                  AND\n" +
                "                  A.upz = household.upz \n" +
                "                  AND\n" +
                "                  A.un = household.un \n" +
                "                  AND\n" +
                "                  A.mouza = household.mouza \n" +
                "                  AND\n" +
                "                  A.vill = household.vill \n" +
                "                  AND\n" +
                "                  A.hhno = household.hhno \n" +
                "                  AND\n" +
                "                  A.Dist = " + g.getDistrict() + " AND " +
                "                  A.Upz = " + g.getUpazila() + " AND " +
                "                  A.UN = " + g.getUnion() + " AND " +
                "                  A.MOUZA = " + g.getMouza() + " AND " +
                "                  A.Vill = " + g.getVillage() + " AND " +
                "                  A.HHNo = " + g.getHouseholdNo() +
                "       )\n" +
                " WHERE DisT = " + g.getDistrict() + " AND " +
                "       Upz = " + g.getUpazila() + " AND " +
                "       UN = " + g.getUnion() + " AND " +
                "       MOUZA = " + g.getMouza() + " AND " +
                "       Vill = " + g.getVillage() + " AND" +
                "       HHNo = " + g.getHouseholdNo() + "";


        C.Save(SQL);
    }


    Context con;
    Location currentLocation;
    double currentLatitude, currentLongitude;


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


    Button cmdSES;

    //***************************************************************************************************************************
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.memberlistfpi);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();
            con = this;

            IDbundle = getIntent().getExtras();

            if (IDbundle != null) {
                if (IDbundle.getString("search").equalsIgnoreCase("search")) {
                    //DisplaySearch(true);
                    //ListView list = (ListView) findViewById(R.id.lstMember);
                   // View header = getLayoutInflater().inflate(R.layout.memberlistheading, null);
                  //  list.addHeaderView(header);

                } else {
                  //  DisplaySearch(false);
                    LoadDataMemberList();
                }
            } else {
                DisplaySearch(false);
                LoadDataMemberList();
            }
            View header = getLayoutInflater().inflate(R.layout.memberlistheading, null);
            list.addHeaderView(header);

        } catch (Exception ex) {
            Connection.MessageBox(MemberListFPI.this, ex.getMessage());
            return;
        }
    }

    private void LoadDataMemberList() {
        try {

            FindLocation();


            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Activity.KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
            lock.disableKeyguard();

            TextView lblVillName = (TextView) findViewById(R.id.lblVillageName);
            lblVillName.setText("গ্রামঃ " + g.getMouza() + "-" + g.getVillage() + ", " + g.getVillageName());
            list= (ListView) findViewById(R.id.lstMember);
            txtHHNo = (TextView) findViewById(R.id.txtHHNo);
            if (g.getHouseholdNo().length() == 0)
                txtHHNo.setText(HouseholdNumber());
            else
                txtHHNo.setText(g.getHouseholdNo());

            rdogrpHouseHold = (RadioGroup) findViewById(R.id.rdogrpHouseHold);
            rdoHouseHold1 = (RadioButton) findViewById(R.id.rdoHouseHold1);
            rdoHouseHold2 = (RadioButton) findViewById(R.id.rdoHouseHold2);

            rdogrpHouseHoldStatus = (RadioGroup) findViewById(R.id.rdogrpHouseHoldStatus);
            rdoHouseHoldStatus1 = (RadioButton) findViewById(R.id.rdoHouseHoldStatus1);
            rdoHouseHoldStatus2 = (RadioButton) findViewById(R.id.rdoHouseHoldStatus2);
            rdoHouseHoldStatus3 = (RadioButton) findViewById(R.id.rdoHouseHoldStatus3);
            secHouseHoldStatus = (LinearLayout) findViewById(R.id.secHouseHoldStatus);
            secHouseHoldStatus.setVisibility(View.GONE);


            secSubBlock = (LinearLayout) findViewById(R.id.secSubBlock);
            txtSubBlock = (TextView) findViewById(R.id.txtSubBlock);
            rdogrpSubBlockStatus = (RadioGroup) findViewById(R.id.rdogrpSubBlockStatus);
            rdoSubBlock1 = (RadioButton) findViewById(R.id.rdoSubBlock1);
            rdoSubBlock2 = (RadioButton) findViewById(R.id.rdoSubBlock2);
            txtOther= (EditText) findViewById(R.id.txtOther);
            secOther= (LinearLayout) findViewById(R.id.secOther);
            secSES = (LinearLayout) findViewById(R.id.secSES);
            secPAddr = (LinearLayout) findViewById(R.id.secPAddr);
            secPAddr1 = (LinearLayout) findViewById(R.id.secPAddr1);
            rdogrpPAddr = (RadioGroup) findViewById(R.id.rdogrpPAddr);
            rdoPAddr1 = (RadioButton) findViewById(R.id.rdoPAddr1);
            rdoPAddr1.setEnabled(false);
            rdoPAddr2 = (RadioButton) findViewById(R.id.rdoPAddr2);
            rdoPAddr2.setEnabled(false);

            rdogrpPAddrYesNo = (RadioGroup) findViewById(R.id.rdogrpPAddrYesNo);
            rdoPAddrYes = (RadioButton) findViewById(R.id.rdoPAddrYes);
            rdoPAddrNo = (RadioButton) findViewById(R.id.rdoPAddrNo);


            secPermaAddress = (LinearLayout) findViewById(R.id.secPermaAddress);
            secPermaAddress1 = (LinearLayout) findViewById(R.id.secPermaAddress1);
            txtPermaAddress = (TextView) findViewById(R.id.txtPermaAddress);
            rdogrpAddressStatus = (RadioGroup) findViewById(R.id.rdogrpAddressStatus);
            rdoAddressStatus1 = (RadioButton) findViewById(R.id.rdoAddressStatus1);
            rdoAddressStatus2 = (RadioButton) findViewById(R.id.rdoAddressStatus2);


            txtReligion = (TextView) findViewById(R.id.txtReligion);
            rdogrpReligion = (RadioGroup) findViewById(R.id.rdogrpReligion);
            rdoReligion1 = (RadioButton) findViewById(R.id.rdoReligion1);
            rdoReligion2 = (RadioButton) findViewById(R.id.rdoReligion2);

            rdogrpVGFCard = (RadioGroup) findViewById(R.id.rdogrpVGFCard);
            rdoVGFCard1 = (RadioButton) findViewById(R.id.rdoVGFCard1);
            rdoVGFCard2 = (RadioButton) findViewById(R.id.rdoVGFCard2);


            secReligion = (LinearLayout) findViewById(R.id.secReligion);

            rdogrpHouseHoldStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int radioButtonID) {
                    if (rdoHouseHoldStatus1.isChecked()) {
                        secOther.setVisibility(View.GONE);
                        txtOther.setText("");
                        rdogrpSubBlockStatus.clearCheck();
                        rdoSubBlock1.setVisibility(View.GONE);
                        rdoSubBlock2.setVisibility(View.GONE);
                        rdogrpPAddrYesNo.clearCheck();
                        rdoPAddrYes.setVisibility(View.GONE);
                        rdoPAddrNo.setVisibility(View.GONE);
                        rdogrpAddressStatus.clearCheck();
                        rdoAddressStatus1.setVisibility(View.GONE);
                        rdoAddressStatus2.setVisibility(View.GONE);
                        rdogrpReligion.clearCheck();
                        rdoReligion1.setVisibility(View.GONE);
                        rdoReligion2.setVisibility(View.GONE);
                       // ListView list = (ListView) findViewById(R.id.lstMember);
                        list.setVisibility(View.GONE);
                    } else if (rdoHouseHoldStatus2.isChecked()) {
                        secOther.setVisibility(View.GONE);
                        txtOther.setText("");
                        rdogrpSubBlockStatus.clearCheck();
                        rdoSubBlock1.setVisibility(View.GONE);
                        rdoSubBlock2.setVisibility(View.GONE);
                        rdogrpPAddrYesNo.clearCheck();
                        rdoPAddrYes.setVisibility(View.GONE);
                        rdoPAddrNo.setVisibility(View.GONE);
                        rdogrpAddressStatus.clearCheck();
                        rdoAddressStatus1.setVisibility(View.GONE);
                        rdoAddressStatus2.setVisibility(View.GONE);
                        rdogrpReligion.clearCheck();
                        rdoReligion1.setVisibility(View.GONE);
                        rdoReligion2.setVisibility(View.GONE);
                        //ListView list = (ListView) findViewById(R.id.lstMember);
                        list.setVisibility(View.GONE);
                    }

                    else if (rdoHouseHoldStatus3.isChecked()) {
                        secOther.setVisibility(View.VISIBLE);
                        rdogrpSubBlockStatus.clearCheck();
                        rdoSubBlock1.setVisibility(View.GONE);
                        rdoSubBlock2.setVisibility(View.GONE);
                        rdogrpPAddrYesNo.clearCheck();
                        rdoPAddrYes.setVisibility(View.GONE);
                        rdoPAddrNo.setVisibility(View.GONE);
                        rdogrpAddressStatus.clearCheck();
                        rdoAddressStatus1.setVisibility(View.GONE);
                        rdoAddressStatus2.setVisibility(View.GONE);
                        rdogrpReligion.clearCheck();
                        rdoReligion1.setVisibility(View.GONE);
                        rdoReligion2.setVisibility(View.GONE);
                        //ListView list = (ListView) findViewById(R.id.lstMember);
                        list.setVisibility(View.GONE);
                    }
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });
            secSubBlock.setVisibility(View.GONE);
            secPAddr.setVisibility(View.GONE);
            secPAddr1.setVisibility(View.GONE);
            secReligion.setVisibility(View.GONE);
            secSES.setVisibility(View.GONE);
            rdogrpHouseHold.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int radioButtonID) {
                    if (rdoHouseHold2.isChecked()) {
                        secHouseHoldStatus.setVisibility(View.VISIBLE);
                        secSubBlock.setVisibility(View.GONE);
                        secPAddr.setVisibility(View.GONE);
                        secPAddr1.setVisibility(View.GONE);
                        secReligion.setVisibility(View.GONE);
                        secSES.setVisibility(View.GONE);
                        rdogrpSubBlockStatus.clearCheck();
                        rdogrpPAddrYesNo.clearCheck();
                        rdogrpReligion.clearCheck();
                        //  ListView list = (ListView) findViewById(R.id.lstMember);
                        list.setVisibility(View.GONE);

                        secOther.setVisibility(View.GONE);

                        //View header = getLayoutInflater().inflate(R.layout.memberlistheading, null);
                        //list.removeHeaderView(header);
                        //header.setVisibility(View.GONE);
                        //secSubBlock.setVisibility(View.GONE);


                    } else {
                        secHouseHoldStatus.setVisibility(View.GONE);
                        DataRetrieve("", true, "active");
                        secSubBlock.setVisibility(View.VISIBLE);
                        secPAddr.setVisibility(View.VISIBLE);
                        secPAddr1.setVisibility(View.VISIBLE);
                        secReligion.setVisibility(View.VISIBLE);
                        secSES.setVisibility(View.VISIBLE);
                        rdogrpHouseHoldStatus.clearCheck();
                        //rdogrpSubBlockStatus.clearCheck();
                        rdoSubBlock1.setVisibility(View.VISIBLE);
                        rdoSubBlock2.setVisibility(View.VISIBLE);
                        //rdogrpPAddrYesNo.clearCheck();
                        rdoPAddrYes.setVisibility(View.VISIBLE);
                        rdoPAddrNo.setVisibility(View.VISIBLE);
                        //rdogrpAddressStatus.clearCheck();
                        rdoAddressStatus1.setVisibility(View.VISIBLE);
                        rdoAddressStatus2.setVisibility(View.VISIBLE);
                        //rdogrpReligion.clearCheck();
                        rdoReligion1.setVisibility(View.VISIBLE);
                        rdoReligion2.setVisibility(View.VISIBLE);
                        // ListView list = (ListView) findViewById(R.id.lstMember);
                        list.setVisibility(View.VISIBLE);
                        secOther.setVisibility(View.GONE);
                        //View header = getLayoutInflater().inflate(R.layout.memberlistheading, null);

                        //list.addHeaderView(header);
                        // View header = getLayoutInflater().inflate(R.layout.memberlistheading, null);
                        // list.addHeaderView(header);
                    }
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });

            secOther.setVisibility(View.GONE);
            secSave = (LinearLayout) findViewById(R.id.secSave);
            Button cmdSave = (Button) findViewById(R.id.cmdSave);
            cmdSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    DataSave();
                }
            });

            cmdSES = (Button) findViewById(R.id.cmdSES);
            cmdSES.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    try {

                        String DV = "";
                        if (!rdoHouseHold1.isChecked() & !rdoHouseHold2.isChecked() & secSubBlock.isShown()) {
                            Connection.MessageBox(MemberListFPI.this, "খানার  অবস্থা যাচাই করুন ।");
                            rdoHouseHold1.requestFocus();
                            return;
                        }
                        if (rdoHouseHold2.isChecked()) {
                            if (!rdoHouseHoldStatus1.isChecked() & !rdoHouseHoldStatus2.isChecked()& !rdoHouseHoldStatus3.isChecked() & secHouseHoldStatus.isShown()) {
                                Connection.MessageBox(MemberListFPI.this, "খানার  অবস্থা সঠিক না থাকলে কেন নাই যাচাই করুন ।");
                                rdoHouseHoldStatus1.requestFocus();
                                return;
                            }

                        }

                        String SQL = "";


                        /*if (!C.Existence("Select Dist,Upz,UN,Mouza,Vill,HHNo from Household  Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"' and HHNo='" + txtHHNo.getText().toString() + "'")) {*/
                        if (!C.Existence("Select Dist,Upz,UN,Mouza,Vill,HHNo from HouseholdFPI  Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + txtHHNo.getText().toString() + "'")) {
                            SQL = "Insert into HouseholdFPI(Dist,Upz,UN,Mouza,Vill,ProvType,ProvCode,HHNo,Lat,Lon,StartTime,EndTime,UserId,EnDt,Upload)Values('" + g.getDistrict() + "','" + g.getUpazila() + "','" + g.getUnion() + "','" + g.getMouza() + "','" + g.getVillage() + "','" + g.getProvType() + "','" + g.getProvCode() + "','" + txtHHNo.getText() + "','" + Double.toString(currentLatitude) + "','" + Double.toString(currentLongitude) + "','" + StartTime + "','" + g.CurrentTime24() + "','" + g.getUserID() + "','" + Global.DateTimeNowYMDHMS() + "','2')";
                            C.Save(SQL);
                        }

                        SQL = "Update HouseholdFPI Set Upload='2',";

                        /*if(spnSubBlock.getSelectedItem().toString().length()>0)
                            SQL += "subBlock = '" + Global.Left(spnSubBlock.getSelectedItem().toString(),2) + "',";*/
                        SQL += "houseHoldStatus = '" + (rdoHouseHold1.isChecked() ? "1" : (rdoHouseHold2.isChecked() ? "2" : "")) + "',";
                        SQL += "causeOfHouseHoldStatus = '" + (rdoHouseHoldStatus1.isChecked() ? "1" :(rdoHouseHoldStatus2.isChecked() ? "2": (rdoHouseHoldStatus3.isChecked() ? "3" : "")) )+ "',";
                        SQL += "causeOfHouseHoldStatusOther = '" +  (rdoHouseHoldStatus3.isChecked() ? txtOther.getText() : "")+ "',";
                        SQL += "subBlockStatus = '" + (rdoSubBlock1.isChecked() ? "1" : (rdoSubBlock2.isChecked() ? "2" : "")) + "',";
                        SQL += "pAddrStatus = '" + (rdoPAddrYes.isChecked() ? "1" : (rdoPAddrNo.isChecked() ? "2" : "")) + "',";
                        SQL += "permaAddressStatus = '" + (rdoAddressStatus1.isChecked() ? "1" : (rdoAddressStatus2.isChecked() ? "2" : "")) + "',";
                        SQL += "religionStatus = '" + (rdoReligion1.isChecked() ? "1" : (rdoReligion2.isChecked() ? "2" : "")) + "'";
                        //SQL += "VGFCard = '" + (rdoVGFCard1.isChecked() ? "1" : "2") + "'";


                        /*SQL += "  Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and  ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"' and HHNo='" + txtHHNo.getText().toString() + "'";*/
                        SQL += "  Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + txtHHNo.getText().toString() + "'";
                        C.Save(SQL);

                        g.setHouseholdNo(txtHHNo.getText().toString());
                        Intent f1 = new Intent(getApplicationContext(), sesformFPI.class);
                        startActivity(f1);
                    } catch (Exception ex) {
                        Connection.MessageBox(MemberListFPI.this, ex.getMessage());
                        return;
                    }
                }
            });


            rdogrpPAddr.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int radioButtonID) {
                    if (rdoPAddr2.isChecked()) {
                        secPermaAddress.setVisibility(View.VISIBLE);
                        secPermaAddress1.setVisibility(View.VISIBLE);
                    } else {
                        secPermaAddress.setVisibility(View.GONE);
                        secPermaAddress1.setVisibility(View.GONE);
                        txtPermaAddress.setText("");
                    }
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });


            final RadioGroup roMemberOption = (RadioGroup) findViewById(R.id.roMemberOption);
            roMemberOption.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup arg0, int id) {
                    if (id == R.id.roActiveMember) {
                        DataRetrieve(vill + bari + hhno, false, "active");
                    } else if (id == R.id.roAllMember) {
                        DataRetrieve(vill + bari + hhno, false, "all");
                    }
                }
            });

            secPermaAddress.setVisibility(View.GONE);

            HHDataSearch(g.getDistrict(), g.getUpazila(), g.getUnion(), g.getMouza(), g.getVillage(), txtHHNo.getText().toString());
            FPIDataSearch(g.getDistrict(), g.getUpazila(), g.getUnion(), g.getMouza(), g.getVillage(), txtHHNo.getText().toString());
         //   DataRetrieve("", true, "active");
            //Gone for all


            Button cmdVGDCard = (Button) findViewById(R.id.cmdVGDCard);
            cmdVGDCard.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    try {
                        VGDCardForm();
                    } catch (Exception ex) {
                        Connection.MessageBox(MemberListFPI.this, ex.getMessage());
                        return;
                    }
                }
            });


        } catch (Exception ex) {
            Connection.MessageBox(MemberListFPI.this, ex.getMessage());
            return;
        }
    }

    private void DataSave() {
        try {

            if (!rdoHouseHold1.isChecked() & !rdoHouseHold2.isChecked() & secSubBlock.isShown()) {
                Connection.MessageBox(MemberListFPI.this, "খানার  অবস্থা যাচাই করুন ।");
                rdoHouseHold1.requestFocus();
                return;
            }
            if (rdoHouseHold2.isChecked()) {
                if (!rdoHouseHoldStatus1.isChecked() & !rdoHouseHoldStatus2.isChecked()& !rdoHouseHoldStatus3.isChecked() & secHouseHoldStatus.isShown()) {
                    Connection.MessageBox(MemberListFPI.this, "খানার  অবস্থা সঠিক না থাকলে কেন নাই যাচাই করুন ।");
                    rdoHouseHoldStatus1.requestFocus();
                    return;
                }

            }
            String SQL = "";
            if (!C.Existence("Select Dist,Upz,UN,Mouza,Vill,HHNo from HouseholdFPI  Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + txtHHNo.getText().toString() + "'")) {
                SQL = "Insert into HouseholdFPI(Dist,Upz,UN,Mouza,Vill,ProvType,ProvCode,HHNo,Lat,Lon,StartTime,EndTime,UserId,EnDt,Upload)Values('" + g.getDistrict() + "','" + g.getUpazila() + "','" + g.getUnion() + "','" + g.getMouza() + "','" + g.getVillage() + "','" + g.getProvType() + "','" + g.getProvCode() + "','" + txtHHNo.getText() + "','" + Double.toString(currentLatitude) + "','" + Double.toString(currentLongitude) + "','" + StartTime + "','" + g.CurrentTime24() + "','" + g.getUserID() + "','" + Global.DateTimeNowYMDHMS() + "','2')";
                C.Save(SQL);
            }

            SQL = "Update HouseholdFPI Set Upload='2',";
            SQL += "houseHoldStatus = '" + (rdoHouseHold1.isChecked() ? "1" : (rdoHouseHold2.isChecked() ? "2" : "")) + "',";
           // SQL += "causeOfHouseHoldStatus = '" + (rdoHouseHoldStatus1.isChecked() ? "1" : (rdoHouseHoldStatus2.isChecked() ? "2" : "")) + "',";
            SQL += "causeOfHouseHoldStatus = '" + (rdoHouseHoldStatus1.isChecked() ? "1" :(rdoHouseHoldStatus2.isChecked() ? "2": (rdoHouseHoldStatus3.isChecked() ? "3" : "")) )+ "',";
            SQL += "causeOfHouseHoldStatusOther = '" +  (rdoHouseHoldStatus3.isChecked() ? txtOther.getText() : "")+ "',";
            SQL += "subBlockStatus = '" + (rdoSubBlock1.isChecked() ? "1" : (rdoSubBlock2.isChecked() ? "2" : "")) + "',";
            SQL += "pAddrStatus = '" + (rdoPAddrYes.isChecked() ? "1" : (rdoPAddrNo.isChecked() ? "2" : "")) + "',";
            SQL += "permaAddressStatus = '" + (rdoAddressStatus1.isChecked() ? "1" : (rdoAddressStatus2.isChecked() ? "2" : "")) + "',";
            SQL += "religionStatus = '" + (rdoReligion1.isChecked() ? "1" : (rdoReligion2.isChecked() ? "2" : "")) + "'";
            SQL += "  Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + txtHHNo.getText().toString() + "'";
            C.Save(SQL);
            Connection.MessageBox(MemberListFPI.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
        } catch (Exception e) {
            Connection.MessageBox(MemberListFPI.this, e.getMessage());
            return;
        }
    }

    private void VGDCardForm() {
        final Dialog dialog = new Dialog(MemberListFPI.this);
        dialog.setTitle("ভি জি ডি কার্ড(ছবি)");
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.vgdcard);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

    }

    private void FPIDataSearch(String Dist, String Upz, String UN, String Mouza, String Vill, String HHNo) {
        try {
            RadioButton rb;
            String SQL = "";
            SQL = "select ifnull(houseHoldStatus,'') as houseHoldStatus,ifnull(causeOfHouseHoldStatus,'') as causeOfHouseHoldStatus,ifnull(causeOfHouseHoldStatusOther,'') as causeOfHouseHoldStatusOther,ifnull(subBlockStatus,'') as subBlockStatus,ifnull(pAddrStatus,'') as pAddrStatus,ifnull(permaAddressStatus,'') as permaAddressStatus,ifnull(religionStatus,'') as religionStatus";
            SQL += " from HouseholdFPI Where Dist='" + Dist + "' and Upz='" + Upz + "' and UN='" + UN + "' and Mouza='" + Mouza + "' and Vill='" + Vill + "' and HHNo='" + HHNo + "'";

            Cursor cur1 = C.ReadData(SQL);
            cur1.moveToFirst();
            while (!cur1.isAfterLast()) {
                if (cur1.getString(cur1.getColumnIndex("houseHoldStatus")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoHouseHold1.setChecked(true);
                  //  secSave.setVisibility(View.GONE);
                } else if (cur1.getString(cur1.getColumnIndex("houseHoldStatus")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoHouseHold2.setChecked(true);
                  //  secSave.setVisibility(View.GONE);
                } else {
                   // secSave.setVisibility(View.VISIBLE);
                }

                if (cur1.getString(cur1.getColumnIndex("causeOfHouseHoldStatus")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoHouseHoldStatus1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("causeOfHouseHoldStatus")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoHouseHoldStatus2.setChecked(true);
                }

                else if (cur1.getString(cur1.getColumnIndex("causeOfHouseHoldStatus")).replace("null", "").toString().equalsIgnoreCase("3")) {
                    rdoHouseHoldStatus3.setChecked(true);
                    txtOther.setText(cur1.getString(cur1.getColumnIndex("causeOfHouseHoldStatusOther")));
                }
                if (cur1.getString(cur1.getColumnIndex("subBlockStatus")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoSubBlock1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("subBlockStatus")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoSubBlock2.setChecked(true);
                }

                if (cur1.getString(cur1.getColumnIndex("pAddrStatus")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoPAddrYes.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("pAddrStatus")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoPAddrNo.setChecked(true);
                }
                if (cur1.getString(cur1.getColumnIndex("permaAddressStatus")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoAddressStatus1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("permaAddressStatus")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoAddressStatus2.setChecked(true);
                }
                if (cur1.getString(cur1.getColumnIndex("religionStatus")).replace("null", "").toString().equalsIgnoreCase("1")) {
                    rdoReligion1.setChecked(true);
                } else if (cur1.getString(cur1.getColumnIndex("religionStatus")).replace("null", "").toString().equalsIgnoreCase("2")) {
                    rdoReligion2.setChecked(true);
                }
                cur1.moveToNext();
            }
            cur1.close();
        } catch (Exception e) {
            Connection.MessageBox(MemberListFPI.this, e.getMessage());
            return;
        }
    }

    private void DisplayAdvanceSearchPopup() {
        final Dialog popupView = new Dialog(MemberListFPI.this);
        popupView.setTitle("Advace Search");
        popupView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popupView.setContentView(R.layout.searchpopup);
        popupView.setCancelable(true);
        popupView.setCanceledOnTouchOutside(true);

        /*if(((LinearLayout)findViewById(R.id.secNameSearch)).getVisibility()== View.VISIBLE)
        {
            ((CheckBox)popupView.findViewById(R.id.VlblNameSearch)).setChecked(true);
        }
        if(((LinearLayout)findViewById(R.id.sechealthid)).getVisibility()== View.VISIBLE){
            ((CheckBox)popupView.findViewById(R.id.Vlblhealthid)).setChecked(true);
        }
        if(((LinearLayout)findViewById(R.id.secMobileNo)).getVisibility()== View.VISIBLE){

            //((CheckBox)popupView.findViewById(R.id.VlblMobileNo)).setChecked(true);
        }
        if(((LinearLayout)findViewById(R.id.secFatherSearch)).getVisibility()== View.VISIBLE){
            ((CheckBox)popupView.findViewById(R.id.VlblFatherSearch)).setChecked(true);

        }
        if(((LinearLayout)findViewById(R.id.secMotherSearch)).getVisibility()== View.VISIBLE){
            ((CheckBox)popupView.findViewById(R.id.VlblMotherSearch)).setChecked(true);
        }
        if(((LinearLayout)findViewById(R.id.secDOBSearch)).getVisibility()== View.VISIBLE){
            ((CheckBox)popupView.findViewById(R.id.VlblDOBSearch)).setChecked(true);
        }
        if(((LinearLayout)findViewById(R.id.secSex)).getVisibility()== View.VISIBLE){
            ((CheckBox)popupView.findViewById(R.id.VlblSex)).setChecked(true);
        }*/

        if (((LinearLayout) findViewById(R.id.secDistrict)).getVisibility() == View.VISIBLE) {
            ((CheckBox) popupView.findViewById(R.id.Vlbldistrict)).setChecked(true);
        }


        if (((LinearLayout) findViewById(R.id.secUpazilla)).getVisibility() == View.VISIBLE) {
            ((CheckBox) popupView.findViewById(R.id.Vlblupazilla)).setChecked(true);
        }


        if (((LinearLayout) findViewById(R.id.secUnion)).getVisibility() == View.VISIBLE) {
            ((CheckBox) popupView.findViewById(R.id.Vlblunion)).setChecked(true);
        }


        if (((LinearLayout) findViewById(R.id.secVillSearch)).getVisibility() == View.VISIBLE) {
            ((CheckBox) popupView.findViewById(R.id.VlblVillSearch)).setChecked(true);
        }


        if (((LinearLayout) findViewById(R.id.secNameSearch)).getVisibility() == View.VISIBLE) {
            ((CheckBox) popupView.findViewById(R.id.VlblNameSearch)).setChecked(true);
        }

        if (((LinearLayout) findViewById(R.id.secFatherSearch)).getVisibility() == View.VISIBLE) {
            ((CheckBox) popupView.findViewById(R.id.VlblFatherSearch)).setChecked(true);
        }

        if (((LinearLayout) findViewById(R.id.secMotherSearch)).getVisibility() == View.VISIBLE) {
            ((CheckBox) popupView.findViewById(R.id.VlblMotherSearch)).setChecked(true);
        }


        if (((LinearLayout) findViewById(R.id.secHusbandSearch)).getVisibility() == View.VISIBLE) {
            ((CheckBox) popupView.findViewById(R.id.VlblHusid)).setChecked(true);
        }


        if (((LinearLayout) findViewById(R.id.secDOBSearch)).getVisibility() == View.VISIBLE) {
            ((CheckBox) popupView.findViewById(R.id.VlblDOBSearch)).setChecked(true);
        }


        if (((LinearLayout) findViewById(R.id.secSex)).getVisibility() == View.VISIBLE) {
            ((CheckBox) popupView.findViewById(R.id.VlblSex)).setChecked(true);
        }


        Button btnDismiss = (Button) popupView.findViewById(R.id.dismiss);
        btnDismiss.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                if (((CheckBox) popupView.findViewById(R.id.Vlbldistrict)).isChecked()) {
                    ((LinearLayout) findViewById(R.id.secDistrict)).setVisibility(View.VISIBLE);
                } else {
                    ((LinearLayout) findViewById(R.id.secDistrict)).setVisibility(View.GONE);
                }

                if (((CheckBox) popupView.findViewById(R.id.Vlblupazilla)).isChecked()) {
                    ((LinearLayout) findViewById(R.id.secUpazilla)).setVisibility(View.VISIBLE);
                } else {
                    ((LinearLayout) findViewById(R.id.secUpazilla)).setVisibility(View.GONE);
                }

                if (((CheckBox) popupView.findViewById(R.id.Vlblunion)).isChecked()) {
                    ((LinearLayout) findViewById(R.id.secUnion)).setVisibility(View.VISIBLE);
                } else {
                    ((LinearLayout) findViewById(R.id.secUnion)).setVisibility(View.GONE);
                }

                if (((CheckBox) popupView.findViewById(R.id.VlblVillSearch)).isChecked()) {
                    ((LinearLayout) findViewById(R.id.secVillSearch)).setVisibility(View.VISIBLE);
                } else {
                    ((LinearLayout) findViewById(R.id.secVillSearch)).setVisibility(View.GONE);
                }

                if (((CheckBox) popupView.findViewById(R.id.VlblNameSearch)).isChecked()) {
                    ((LinearLayout) findViewById(R.id.secNameSearch)).setVisibility(View.VISIBLE);
                } else {
                    ((LinearLayout) findViewById(R.id.secNameSearch)).setVisibility(View.GONE);
                }
                if (((CheckBox) popupView.findViewById(R.id.VlblFatherSearch)).isChecked()) {
                    ((LinearLayout) findViewById(R.id.secFatherSearch)).setVisibility(View.VISIBLE);
                } else {
                    ((LinearLayout) findViewById(R.id.secFatherSearch)).setVisibility(View.GONE);
                }
                if (((CheckBox) popupView.findViewById(R.id.VlblMotherSearch)).isChecked()) {
                    ((LinearLayout) findViewById(R.id.secMotherSearch)).setVisibility(View.VISIBLE);
                } else {
                    ((LinearLayout) findViewById(R.id.secMotherSearch)).setVisibility(View.GONE);
                }


                if (((CheckBox) popupView.findViewById(R.id.VlblHusid)).isChecked()) {
                    ((LinearLayout) findViewById(R.id.secHusbandSearch)).setVisibility(View.VISIBLE);
                } else {
                    ((LinearLayout) findViewById(R.id.secHusbandSearch)).setVisibility(View.GONE);
                }

                if (((CheckBox) popupView.findViewById(R.id.VlblDOBSearch)).isChecked()) {
                    ((LinearLayout) findViewById(R.id.secDOBSearch)).setVisibility(View.VISIBLE);
                } else {
                    ((LinearLayout) findViewById(R.id.secDOBSearch)).setVisibility(View.GONE);
                }

                if (((CheckBox) popupView.findViewById(R.id.VlblSex)).isChecked()) {
                    ((LinearLayout) findViewById(R.id.secSex)).setVisibility(View.VISIBLE);
                } else {
                    ((LinearLayout) findViewById(R.id.secSex)).setVisibility(View.GONE);
                }

                popupView.dismiss();
            }
        });

        popupView.show();

    }

    private void DisplaySearch(boolean willdisplaysearch) {

        ((LinearLayout) findViewById(R.id.secDistrict)).setVisibility(View.GONE);
        ((LinearLayout) findViewById(R.id.secUpazilla)).setVisibility(View.GONE);
        ((LinearLayout) findViewById(R.id.secUnion)).setVisibility(View.GONE);
        //  ((LinearLayout) findViewById(R.id.secWard)).setVisibility(View.GONE);
        ((LinearLayout) findViewById(R.id.secVillSearch)).setVisibility(View.GONE);

        ((LinearLayout) findViewById(R.id.secNameSearch)).setVisibility(View.GONE);
        ((LinearLayout) findViewById(R.id.secFatherSearch)).setVisibility(View.GONE);
        ((LinearLayout) findViewById(R.id.secMotherSearch)).setVisibility(View.GONE);
        ((LinearLayout) findViewById(R.id.secHusbandSearch)).setVisibility(View.GONE);

        ((LinearLayout) findViewById(R.id.secDOBSearch)).setVisibility(View.GONE);
        ((LinearLayout) findViewById(R.id.secSex)).setVisibility(View.GONE);

        Button cmdAdvanceSearch = (Button) findViewById(R.id.cmdAdvanceSearch);
        cmdAdvanceSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                DisplayAdvanceSearchPopup();
            }
        });

        ((Spinner) findViewById(R.id.txtDistrictSearch)).setAdapter(C.getArrayAdapter("Select '  ' AS ZILLANAME union Select ZILLAID||'-'||ZILLANAME from zilla ORDER BY ZILLANAME"));
        ((Spinner) findViewById(R.id.txtDistrictSearch)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String val = (((Spinner) findViewById(R.id.txtDistrictSearch)).getSelectedItemPosition() == 0 ? "" : Global.Left(((Spinner) findViewById(R.id.txtDistrictSearch)).getSelectedItem().toString(), 2));
                if (val.length() > 0) {

                    ((Spinner) findViewById(R.id.txtupazillaSearch))
                            .setAdapter(C.getArrayAdapter("Select '  ' union Select UPAZILAID||'-'||UPAZILANAME from upazila where zillaid = '" + val + "'"));

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ((Spinner) findViewById(R.id.txtupazillaSearch)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String val = (((Spinner) findViewById(R.id.txtupazillaSearch)).getSelectedItemPosition() == 0 ? "" : Global.Left(((Spinner) findViewById(R.id.txtupazillaSearch)).getSelectedItem().toString(), 2));
                if (val.length() > 0) {
                    ((Spinner) findViewById(R.id.txtunionSearch)).setAdapter(C.getArrayAdapter("Select '  ' union Select UNIONID||'-'||UNIONNAME FROM UNIONS where UPAZILAID ='" + val + "'"));


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ((Spinner) findViewById(R.id.txtunionSearch)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String val = (((Spinner) findViewById(R.id.txtunionSearch)).getSelectedItemPosition() == 0 ? "" : Global.Left(((Spinner) findViewById(R.id.txtunionSearch)).getSelectedItem().toString(), 2));
                if (val.length() > 0) {

                    //((Spinner) findViewById(R.id.txtWardSearch)).setAdapter(C.getArrayAdapter("Select '  ' union Select MOUZAID||'-'||MOUZANAME FROM Mouza where UNIONID ='" + val + "'"));

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

      /*  ((Spinner) findViewById(R.id.txtWardSearch)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String val = (((Spinner) findViewById(R.id.txtWardSearch)).getSelectedItemPosition() == 0 ? "" : Global.Left(((Spinner) findViewById(R.id.txtWardSearch)).getSelectedItem().toString(), 2));
                if (val.length() > 0) {

                    ((Spinner) findViewById(R.id.txtVillSearch)).setAdapter(C.getArrayAdapter("Select '  ' union Select VILLAGEID||'-'||VILLAGENAME FROM village where MouzaID = '" + val + "'"));

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ((ImageButton) findViewById(R.id.btnMinus)).setVisibility(View.VISIBLE);
        ((ImageButton) findViewById(R.id.btnPlus)).setVisibility(View.GONE);
        ((LinearLayout) findViewById(R.id.seclblSearchGroup)).setVisibility(View.GONE);

        ((ImageButton) findViewById(R.id.btnPlus)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ((LinearLayout) findViewById(R.id.seclblSearchGroup)).setVisibility(View.VISIBLE);
                ((ImageButton) findViewById(R.id.btnPlus)).setVisibility(View.GONE);
                ((ImageButton) findViewById(R.id.btnMinus)).setVisibility(View.VISIBLE);
            }
        });
        ((ImageButton) findViewById(R.id.btnMinus)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ((LinearLayout) findViewById(R.id.seclblSearchGroup)).setVisibility(View.GONE);
                ((ImageButton) findViewById(R.id.btnMinus)).setVisibility(View.GONE);
                ((ImageButton) findViewById(R.id.btnPlus)).setVisibility(View.VISIBLE);
            }
        });

        final Boolean displayheader = true;
        ((Button) findViewById(R.id.cmdSearch)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {


                try {
                    //  int a=0;
                    // Button cmdSearch = (Button) findViewById(R.id.cmdSearch);
                    //  ListView list = (ListView) findViewById(R.id.lstMember);
                    // View header = getLayoutInflater().inflate(R.layout.memberlistheading, null);
                    //  list.removeHeaderView(header);
                    //  list.addHeaderView(header)null;
                    // list.addHeaderView(header);
                    Search(displayheader);
                    //  list.addHeaderView(header);

                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                } catch (Exception ex) {
                    Connection.MessageBox(MemberListFPI.this, ex.getMessage());
                    return;
                }

            /*try {
                 pDialog = ProgressDialog.show(con, "Wait", "অপেক্ষা করুন ...");

                try {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Search();
                            pDialog.dismiss();
                        }
                    });


                } catch (Exception e) {

                }


            } catch (Exception ex) {
                Connection.MessageBox(MemberListFPI.this, ex.getMessage());
                return;
            }*/
            }
        });
        if (willdisplaysearch) {
            ((LinearLayout) findViewById(R.id.seclblSearch)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.seclblSearchGroup)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.seclblHH)).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.secSubBlock)).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.secPAddr)).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.secPermaAddress)).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.secReligion)).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.secSES)).setVisibility(View.GONE);

            // ((LinearLayout) findViewById(R.id.menuNrcNewMember)).setVisibility(View.GONE);
            //((LinearLayout) findViewById(R.id.menuNewMember)).setVisibility(View.GONE);
            //menuNewMember.


        } else {
            ((LinearLayout) findViewById(R.id.seclblSearch)).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.seclblSearchGroup)).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.seclblHH)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.secSubBlock)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.secPAddr)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.secReligion)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.secSES)).setVisibility(View.VISIBLE);
            // ((LinearLayout) findViewById(R.id.menuNrcNewMember)).setVisibility(View.VISIBLE);
            //  ((LinearLayout) findViewById(R.id.menuNewMember)).setVisibility(View.VISIBLE);


        }
    }

    public void Search(Boolean displayheader) {
    //    ListView list = (ListView) findViewById(R.id.lstMember);
        //View header = getLayoutInflater().inflate(R.layout.memberlistheading, null);
        //list.addHeaderView(header);
        //  list.addView(header);


        try {
            String SQLStr = "";


            SQLStr = "Select Dist, Upz, UN, Mouza, Vill, ProvType as provtype,ProvCode as provcode, HHNo, SNo as SNo, ifnull(HealthID,'') as HealthID, ifnull(NameEng,'') as NameEng,";
            SQLStr += " ifnull(NameBang,'') as NameBang, ifnull(Rth,'') as Rth, ifnull(HaveNID,'') as HaveNID, ifnull(NID,'') as NID, ifnull(NIDStatus,'') as NIDStatus, ifnull(HaveBR,'') as HaveBR, ifnull(BRID,'') as BRID, ifnull(BRIDStatus,'') as BRIDStatus, ifnull(MobileNo1,'') as MobileNo1,";
            SQLStr += " ifnull(MobileNo2,'') as MobileNo2,ifnull(MobileYN,'')as MobileYN, ifnull(DOB,'') as DOB, Cast(((julianday(date('now'))-julianday(DOB))/365.25) as int) as Age,Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) as AgeM, ifnull(DOBSource,'') as DOBSource, ifnull(BPlace,'') as BPlace, ifnull(FNo,'') as FNo, ifnull(Father,'') as Father, ifnull(MNo,'') as MNo, ifnull(Mother,'') as Mother,";
            SQLStr += " ifnull(Sex,'') as Sex, ifnull(MS,'') as MS, ifnull(SPNO1,'') as SPNO1,ifnull(SPNO2,'') as SPNO2,ifnull(SPNO3,'') as SPNO3,ifnull(SPNO4,'') as SPNO4, ifnull(ELCONo,'') as ELCONo, ifnull(ELCODontKnow,'') as ELCODontKnow, ifnull(EDU,'') as EDU, ifnull(Rel,'') as Rel, ifnull(Nationality,'') as Nationality, ifnull(OCP,'') as OCP,ifnull(ExType,'')as ExType,ProvType as provtype, ProvCode as provcode";
            SQLStr += " from Member ";// dist='"+ g.getDistrict() +"' and upz='"+ g.getUpazila() +"' and un='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and vill='"+ g.getVillage() +"' and ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"' and HHNo='"+ g.getHouseholdNo() +"'";

            String wheresql = "";

            if (((EditText) findViewById(R.id.txtHealthIDSearch)).getText().toString().length() > 0) {
                wheresql += " HealthID like '%" + ((EditText) findViewById(R.id.txtHealthIDSearch)).getText().toString() + "%'";
            }
            if (((EditText) findViewById(R.id.txtMobileSearch)).getText().toString().length() > 0) {
                if (wheresql.length() > 0) {
                    wheresql += " AND ";
                }
                wheresql += " MobileNo1 like '%" + ((EditText) findViewById(R.id.txtMobileSearch)).getText().toString() + "%'";

                wheresql += " OR MobileNo2 like '%" + ((EditText) findViewById(R.id.txtMobileSearch)).getText().toString() + "%'";
            }

            if (((EditText) findViewById(R.id.txtNationalIdSearch)).getText().toString().length() > 0) {
                if (wheresql.length() > 0) {
                    wheresql += " AND ";
                }
                wheresql += " NID like'%" + ((EditText) findViewById(R.id.txtNationalIdSearch)).getText().toString() + "%'";
            }
            if (((EditText) findViewById(R.id.txtBRNIdSearch)).getText().toString().length() > 0) {
                if (wheresql.length() > 0) {
                    wheresql += " AND ";
                }
                wheresql += " BRID '%" + ((EditText) findViewById(R.id.txtBRNIdSearch)).getText().toString() + "%'";
            }
            if (((EditText) findViewById(R.id.txtNRCIdSearch)).getText().toString().length() > 0) {
                if (wheresql.length() > 0) {
                    wheresql += " AND ";
                }
                wheresql += " ELCONo '%" + ((EditText) findViewById(R.id.txtNRCIdSearch)).getText().toString() + "%'";
            }

            String dist = "";
            dist = ((Spinner) findViewById(R.id.txtDistrictSearch)).getSelectedItemPosition() == 0 ? "" : Global.Left(((Spinner) findViewById(R.id.txtDistrictSearch)).getSelectedItem().toString(), 1);
            if (dist.length() > 0) {
                if (wheresql.length() > 0) {
                    wheresql += " AND ";
                }
                wheresql += " Dist like '%" + dist + "%'";
            }


            String upz = "";
            if (((LinearLayout) findViewById(R.id.secUpazilla)).getVisibility() == View.VISIBLE) {
                upz = ((Spinner) findViewById(R.id.txtupazillaSearch)).getSelectedItemPosition() == 0 ? "" : Global.Left(((Spinner) findViewById(R.id.txtupazillaSearch)).getSelectedItem().toString(), 1);

                if (upz.length() > 0) {
                    if (wheresql.length() > 0) {
                        wheresql += " AND ";
                    }
                    wheresql += " Upz like '%" + upz + "%'";
                }
            }

            String union = "";
            if (((LinearLayout) findViewById(R.id.secUnion)).getVisibility() == View.VISIBLE) {
                union = ((Spinner) findViewById(R.id.txtunionSearch)).getSelectedItemPosition() == 0 ? "" : Global.Left(((Spinner) findViewById(R.id.txtunionSearch)).getSelectedItem().toString(), 1);

                if (union.length() > 0) {
                    if (wheresql.length() > 0) {
                        wheresql += " AND ";
                    }
                    wheresql += " UN like '%" + union + "%'";
                }
            }
            String vil = "";
            if (((LinearLayout) findViewById(R.id.secVillSearch)).getVisibility() == View.VISIBLE) {
                vil = ((Spinner) findViewById(R.id.txtVillSearch)).getSelectedItemPosition() == 0 ? "" : Global.Left(((Spinner) findViewById(R.id.txtVillSearch)).getSelectedItem().toString(), 1);

                if (vil.length() > 0) {
                    if (wheresql.length() > 0) {
                        wheresql += " AND ";
                    }
                    wheresql += " Vill like '%" + vil + "%'";
                }
            }


            if (((EditText) findViewById(R.id.txtNameSearch)).getText().toString().length() > 0) {
                if (wheresql.length() > 0) {
                    wheresql += " AND ";
                }
                wheresql += " NameEng  like '%" + ((EditText) findViewById(R.id.txtNameSearch)).getText().toString() + "%'";
            }
            if (((EditText) findViewById(R.id.txtFatherSearch)).getText().toString().length() > 0) {
                if (wheresql.length() > 0) {
                    wheresql += " AND ";
                }
                wheresql += " Father  like '%" + ((EditText) findViewById(R.id.txtFatherSearch)).getText().toString() + "%'";
                wheresql += " OR Father like '%" + ((EditText) findViewById(R.id.txtFatherSearch)).getText().toString() + "%'";
            }

            if (((EditText) findViewById(R.id.txtMotherSearch)).getText().toString().length() > 0) {
                if (wheresql.length() > 0) {
                    wheresql += " AND ";
                }
                wheresql += " Mother  like '%" + ((EditText) findViewById(R.id.txtMotherSearch)).getText().toString() + "%'";
                wheresql += " OR Mother like '%" + ((EditText) findViewById(R.id.txtMotherSearch)).getText().toString() + "%'";
            }

            if (((EditText) findViewById(R.id.txtHusbandSearch)).getText().toString().length() > 0) {
                if (wheresql.length() > 0) {
                    wheresql += " AND ";
                }
                wheresql += " Husband  like '%" + ((EditText) findViewById(R.id.txtHusbandSearch)).getText().toString() + "%'";
                wheresql += " OR Husband like '%" + ((EditText) findViewById(R.id.txtHusbandSearch)).getText().toString() + "%'";
            }

            if (((EditText) findViewById(R.id.txtDOBSearch)).getText().toString().length() > 0) {
                if (wheresql.length() > 0) {
                    wheresql += " AND ";
                }
                wheresql += " DOB  like '%" + ((EditText) findViewById(R.id.txtDOBSearch)).getText().toString() + "%'";

            }

            if (((RadioButton) findViewById(R.id.rdoSex1)).isChecked()) {
                if (wheresql.length() > 0) {
                    wheresql += " AND ";
                }
                wheresql += " Sex  = '1'";
            }
            if (((RadioButton) findViewById(R.id.rdoSex2)).isChecked()) {
                if (wheresql.length() > 0) {
                    wheresql += " AND ";
                }
                wheresql += " Sex  = '2'";
            }
            if (((RadioButton) findViewById(R.id.rdoSex3)).isChecked()) {
                if (wheresql.length() > 0) {
                    wheresql += " AND ";
                }
                wheresql += " Sex  = '3'";
            }

            if (wheresql.length() > 0) {
                SQLStr = SQLStr + " WHERE " + wheresql;
            }
            Cursor cur1 = C.ReadData(SQLStr);

            cur1.moveToFirst();
            mylist.clear();
            int i = 0;
            list.setAdapter(null);
            //ListView list = (ListView) findViewById(R.id.lstMember);
            // if(heading ==true)
            // {
            // View header = getLayoutInflater().inflate(R.layout.memberlistheading, null);
            //list.addHeaderView(header);
            // }

            //getLayoutInflater().inflate(R.layout.memberlistheading, null);
          //  int i = 0;

            while (!cur1.isAfterLast()) {
                HashMap<String, String> map = new HashMap<String, String>();
                displayheader = false;
             /*  if (i == 0) {
                    View header = getLayoutInflater().inflate(R.layout.memberlistheading, null);
                    list.addHeaderView(header);
                }*/
                map.put("dist", cur1.getString(cur1.getColumnIndex("Dist")));
                map.put("upz", cur1.getString(cur1.getColumnIndex("Upz")));
                map.put("un", cur1.getString(cur1.getColumnIndex("UN")));
                map.put("mouza", cur1.getString(cur1.getColumnIndex("Mouza")));
                map.put("vill", cur1.getString(cur1.getColumnIndex("Vill")));
                map.put("provtype", cur1.getString(cur1.getColumnIndex("provtype")));
                map.put("provcode", cur1.getString(cur1.getColumnIndex("provcode")));
                map.put("hhno", cur1.getString(cur1.getColumnIndex("HHNo")));
                map.put("sno", cur1.getString(cur1.getColumnIndex("SNo")));
                map.put("nameeng", cur1.getString(cur1.getColumnIndex("NameEng")));
                map.put("namebang", cur1.getString(cur1.getColumnIndex("NameBang")));
                map.put("healthid", cur1.getString(cur1.getColumnIndex("HealthID")));
                map.put("rth", cur1.getString(cur1.getColumnIndex("Rth")));
                map.put("havenid", cur1.getString(cur1.getColumnIndex("HaveNID")));
                map.put("nid", cur1.getString(cur1.getColumnIndex("NID")));
                map.put("nidstatus", cur1.getString(cur1.getColumnIndex("NIDStatus")));
                map.put("havebr", cur1.getString(cur1.getColumnIndex("HaveBR")));
                map.put("brid", cur1.getString(cur1.getColumnIndex("BRID")));
                map.put("bridstatus", cur1.getString(cur1.getColumnIndex("BRIDStatus")));
                map.put("mobileno1", cur1.getString(cur1.getColumnIndex("MobileNo1")));
                map.put("mobileno2", cur1.getString(cur1.getColumnIndex("MobileNo2")));
                map.put("mobileyn", cur1.getString(cur1.getColumnIndex("MobileYN")));
                map.put("dob", cur1.getString(cur1.getColumnIndex("DOB")));
                map.put("age", cur1.getString(cur1.getColumnIndex("Age")));
                map.put("agem", cur1.getString(cur1.getColumnIndex("AgeM")));
                map.put("dobsource", cur1.getString(cur1.getColumnIndex("DOBSource")));
                map.put("bplace", cur1.getString(cur1.getColumnIndex("BPlace")));
                map.put("fno", cur1.getString(cur1.getColumnIndex("FNo")));
                map.put("father", cur1.getString(cur1.getColumnIndex("Father")));
                map.put("mno", cur1.getString(cur1.getColumnIndex("MNo")));
                map.put("mother", cur1.getString(cur1.getColumnIndex("Mother")));
                map.put("sex", cur1.getString(cur1.getColumnIndex("Sex")));
                map.put("ms", cur1.getString(cur1.getColumnIndex("MS")));
                map.put("elcono", cur1.getString(cur1.getColumnIndex("ELCONo")));
                map.put("elcodontknow", cur1.getString(cur1.getColumnIndex("ELCODontKnow")));
                map.put("edu", cur1.getString(cur1.getColumnIndex("EDU")));
                map.put("rel", cur1.getString(cur1.getColumnIndex("Rel")));
                map.put("nationality", cur1.getString(cur1.getColumnIndex("Nationality")));
                map.put("ocp", cur1.getString(cur1.getColumnIndex("OCP")));
                map.put("spno1", cur1.getString(cur1.getColumnIndex("SPNO1")));
                map.put("spno2", cur1.getString(cur1.getColumnIndex("SPNO2")));
                map.put("spno3", cur1.getString(cur1.getColumnIndex("SPNO3")));
                map.put("spno4", cur1.getString(cur1.getColumnIndex("SPNO4")));
                map.put("extype", cur1.getString(cur1.getColumnIndex("ExType")));

                map.put("provtype", cur1.getString(cur1.getColumnIndex("provtype")));
                map.put("provcode", cur1.getString(cur1.getColumnIndex("provcode")));


                mylist.add(map);
                mSchedule = new SimpleAdapter(MemberListFPI.this, mylist, R.layout.memberlistrow, new String[]{"sno"},
                        new int[]{R.id.SNo});

                //  pDialog = ProgressDialog.show(con, "Wait","অপেক্ষা করুন ...");
                // new Thread() {

                //   public void run() {
                //     try {
                // finish();
                //       Looper.prepare();
                mSchedule.notifyDataSetChanged();
                mSchedule = new SimpleAdapter(MemberListFPI.this, mylist, R.layout.memberlistrow, new String[]{"sno"},
                        new int[]{R.id.SNo});

                mSchedule.notifyDataSetChanged();
                //     Message msg = new Message();
                //   msg.what = UPDATEDONE;
                // pDialogHandler.sendMessage(msg);


                //} catch (Exception e) {

                //}

                //Looper.loop();
                //}
                //}.start();

                list.setAdapter(new MemberListAdapter(con));

                i += 1;
                cur1.moveToNext();


            }
            cur1.close();
            ((TextView) findViewById(R.id.Vlblcount)).setText("মোট " + String.valueOf(i) + " টি ডাটা পাওয়া গিয়েছে");

        } catch (Exception e) {
            Connection.MessageBox(MemberListFPI.this, e.getMessage());
        }

    }

    private Handler pDialogHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATEDONE:
                    pDialog.dismiss();
                    break;
            }

        }

        @Override
        public void close() {

        }

        @Override
        public void flush() {

        }

        @Override
        public void publish(LogRecord logRecord) {

        }
    };

    private String HouseholdNumber() {
        String SQL = "";

        SQL = "Select (ifnull(max(cast(HHNo as int)),10000)+1)MaxHH from Household where";
       /* SQL += " dist='"+ g.getDistrict() +"' and upz='"+ g.getUpazila() +"' and un='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and vill='"+ g.getVillage() +"' and ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"'";*/
        SQL += " dist='" + g.getDistrict() + "' and upz='" + g.getUpazila() + "' and un='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and vill='" + g.getVillage() + "'";

        String HHNo = C.ReturnSingleValue(SQL);

        return HHNo;
    }


    private void HHDataSearch(String Dist, String Upz, String UN, String Mouza, String Vill, String HHNo) {
        try {
            RadioButton rb;
            //Cursor cur = C.ReadData("Select PAddr, PermaAddress, Religion, VGFCard, ifnull(subBlock,'')subBlock, ifnull(unit,'')unit, HHHead, totalMem from Household  Where Dist='"+ Dist +"' and Upz='"+ Upz +"' and UN='"+ UN +"' and Mouza='"+ Mouza +"' and Vill='"+ Vill +"' and ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"' and HHNo='"+ HHNo +"'");
            Cursor cur = C.ReadData("Select PAddr, PermaAddress, Religion, VGFCard, ifnull(subBlock,'')subBlock, ifnull(unit,'')unit, HHHead, totalMem from Household  Where Dist='" + Dist + "' and Upz='" + Upz + "' and UN='" + UN + "' and Mouza='" + Mouza + "' and Vill='" + Vill + "' and HHNo='" + HHNo + "'");
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                String SubBlock = cur.getString(cur.getColumnIndex("subBlock")).replace("null", "");
                if (SubBlock.equalsIgnoreCase("01")) {
                    txtSubBlock.setText("ক-১");
                } else if (SubBlock.equalsIgnoreCase("02")) {
                    txtSubBlock.setText("ক-২");
                } else if (SubBlock.equalsIgnoreCase("03")) {
                    txtSubBlock.setText("খ-১");
                } else if (SubBlock.equalsIgnoreCase("04")) {
                    txtSubBlock.setText("খ-২");
                } else if (SubBlock.equalsIgnoreCase("05")) {
                    txtSubBlock.setText("গ-১");
                } else if (SubBlock.equalsIgnoreCase("06")) {
                    txtSubBlock.setText("গ-২");
                } else if (SubBlock.equalsIgnoreCase("07")) {
                    txtSubBlock.setText("ঘ-১");
                } else if (SubBlock.equalsIgnoreCase("08")) {
                    txtSubBlock.setText("ঘ-২");
                }
                if (cur.getString(cur.getColumnIndex("PAddr")).equals("1"))
                    rdoPAddr1.setChecked(true);
                else if (cur.getString(cur.getColumnIndex("PAddr")).equals("2"))
                    rdoPAddr2.setChecked(true);

                txtPermaAddress.setText(cur.getString(cur.getColumnIndex("PermaAddress")).replace("null", ""));
                String Rel = cur.getString(cur.getColumnIndex("Religion")).replace("null", "");
                if (Rel.equalsIgnoreCase("1")) {
                    txtReligion.setText("ইসলাম");
                } else if (Rel.equalsIgnoreCase("2")) {
                    txtReligion.setText("হিন্দু");
                } else if (Rel.equalsIgnoreCase("3")) {
                    txtReligion.setText("বৌদ্ধ");
                } else if (Rel.equalsIgnoreCase("4")) {
                    txtReligion.setText("খ্রীস্টান");
                } else if (Rel.equalsIgnoreCase("8")) {
                    txtReligion.setText("Refuse to disclose");
                } else if (Rel.equalsIgnoreCase("9")) {
                    txtReligion.setText("Not a believer");
                } else if (Rel.equalsIgnoreCase("0")) {
                    txtReligion.setText("অন্যান্য");
                }


                if (cur.getString(cur.getColumnIndex("VGFCard")).equals("1"))
                    rdoVGFCard1.setChecked(true);
                else if (cur.getString(cur.getColumnIndex("VGFCard")).equals("2"))
                    rdoVGFCard2.setChecked(true);


                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(MemberListFPI.this, e.getMessage());
            return;
        }
    }

    private void EventForm(final String ProvType, final String ProvCode, final String HH, final String SNo, final String MemName, final String Age, final String MS, final String Sex, final String HealthId, final String DoB) {
        try {
            final Dialog dialog = new Dialog(MemberListFPI.this);
            dialog.setTitle("Events");
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.eventsform);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);

            final ImageButton cmdEdit = (ImageButton) dialog.findViewById(R.id.cmdEdit);
            final ImageButton cmdDelete = (ImageButton) dialog.findViewById(R.id.cmdDelete);

            final LinearLayout secEditDelete = (LinearLayout) dialog.findViewById(R.id.secEditDelete);
            secEditDelete.setVisibility(View.GONE);
            final LinearLayout secEdit = (LinearLayout) dialog.findViewById(R.id.secEdit);
            final LinearLayout secDelete = (LinearLayout) dialog.findViewById(R.id.secDelete);
            final TextView memName = (TextView) dialog.findViewById(R.id.memName);
            memName.setText(SNo + "-" + MemName);
            final Button cmdMemberFormFPI = (Button) dialog.findViewById(R.id.cmdmemFPI);
            final Button DeathForm = (Button) dialog.findViewById(R.id.cmd6);


            if (ProvType.equals("4"))//couple
            {
                secEdit.setEnabled(false);
                secDelete.setEnabled(false);
                DeathForm.setEnabled(false);
            }

            secEdit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(MemberListFPI.this);
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
                            //  g.setHealthID(HealthId);
                            g.setHealthID(HealthId);
                            // check membernrc table for hid

                            // if true then show

                            if (C.ReturnSingleValue("Select generatedId from clientMap where generatedId='" + g.getHealthID() + "'").equalsIgnoreCase(g.getHealthID())) {
                                Intent f1 = new Intent(getApplicationContext(), nrc.class);
                                startActivity(f1);
                            } else {
                                Intent f1 = new Intent(getApplicationContext(), MemberForm.class);
                                startActivity(f1);
                            }
                        }
                    });

                    adb.show();
                }
            });


            secDelete.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(MemberListFPI.this);
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
                            //
                            C.Save("delete from death where  healthId='" + g.getGeneratedId() + "'");
                            /*C.Save("Update Member Set ExType='',ExDate='"+ ""+"' Where Dist='"+ g.getDistrict() +"' and Upz='"+ g.getUpazila() +"' and UN='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and Vill='"+ g.getVillage() +"' and ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"' and HHNo='"+ g.getHouseholdNo() +"' and SNo='"+g.getSerialNo()+"'");*/
                            C.Save("Update Member Set ExType='',ExDate='" + "" + "' Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and HHNo='" + g.getHouseholdNo() + "' and SNo='" + g.getSerialNo() + "'");

                            finish();
                            Intent f1 = new Intent(getApplicationContext(), MemberListFPI.class);
                            startActivity(f1);
                        }
                    });

                    adb.show();
                }
            });
            cmdMemberFormFPI.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    g.setProvType(ProvType);
                    g.setProvCode(ProvCode);
                    dialog.dismiss();
                    finish();
                    g.setCallFrom("mu");
                    g.setSerialNo(SNo);
                    g.setHealthID(HealthId);

                    Intent f1 = new Intent(getApplicationContext(), MemberFormFPI.class);
                    startActivity(f1);
                }
            });

            dialog.show();
        } catch (Exception ex) {
            Connection.MessageBox(MemberListFPI.this, ex.getMessage());
            return;
        }
    }

    private String CurrentStatus(String HealthID) {
        return C.ReturnSingleValue("select ifnull(currstatus,'')currstatus from elcovisit where healthid='" + HealthID + "' order by date(vDate) desc limit 1");//order by date(systementrydate) desc limit 1// select ifnull(currstatus,'')currstatus from elcovisit where healthid='"+ HealthID +"' order by date(systementrydate) desc limit 1
    }


    //Retrieve member list
    //***************************************************************************************************************************

    public void DataRetrieve(String HH, Boolean heading, String ActiveOrAll) {
        try {
            String SQLStr = "";


           /* Previous code from PRS
           SQLStr = "Select Dist, Upz, UN, Mouza, Vill, ProvType as provtype,ProvCode as provcode, HHNo, SNo as SNo, ifnull(HealthID,'') as HealthID, ifnull(NameEng,'') as NameEng,";
            SQLStr += " ifnull(NameBang,'') as NameBang, ifnull(Rth,'') as Rth, ifnull(HaveNID,'') as HaveNID, ifnull(NID,'') as NID, ifnull(NIDStatus,'') as NIDStatus, ifnull(HaveBR,'') as HaveBR, ifnull(BRID,'') as BRID, ifnull(BRIDStatus,'') as BRIDStatus, ifnull(MobileNo1,'') as MobileNo1,";
            SQLStr += " ifnull(MobileNo2,'') as MobileNo2,ifnull(MobileYN,'')as MobileYN, ifnull(DOB,'') as DOB, Cast(((julianday(date('now'))-julianday(DOB))/365) as int) as Age,Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) as AgeM, ifnull(DOBSource,'') as DOBSource, ifnull(BPlace,'') as BPlace, ifnull(FNo,'') as FNo, ifnull(Father,'') as Father, ifnull(MNo,'') as MNo, ifnull(Mother,'') as Mother,";
            SQLStr += " ifnull(Sex,'') as Sex, ifnull(MS,'') as MS, ifnull(SPNO1,'') as SPNO1,ifnull(SPNO2,'') as SPNO2,ifnull(SPNO3,'') as SPNO3,ifnull(SPNO4,'') as SPNO4, ifnull(ELCONo,'') as ELCONo, ifnull(ELCODontKnow,'') as ELCODontKnow, ifnull(EDU,'') as EDU, ifnull(Rel,'') as Rel, ifnull(Nationality,'') as Nationality, ifnull(OCP,'') as OCP,ifnull(ExType,'')as ExType";//ifnull(ExType,'')as ExType//(case when exType='55' then '55' else '' end) as ExType
            SQLStr += " from Member Where dist='"+ g.getDistrict() +"' and upz='"+ g.getUpazila() +"' and un='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and vill='"+ g.getVillage() +"' and ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"' and HHNo='"+ g.getHouseholdNo() +"'";

            SQLStr += " UNION ALL Select cM.zillaId as Dist,cM.upazilaId as Upz,  cM.unionId as UN, cM.mouzaId as Mouza, cM.villageId as Vill, '' provtype,cM.providerId as provcode,cM.houseGrHoldingNo as HHNo, '00' as SNo, ifnull(cM.generatedId,'') as HealthID, ifnull(cM.name,'') as NameEng,";
            SQLStr += " '' as NameBang,'' as Rth, '' as HaveNID,'' as NID, '' as NIDStatus, '' as HaveBR, '' as BRID, '' BRIDStatus, ifnull(cM.mobileNo,'') as MobileNo1,";
            SQLStr += " '' as MobileNo2,'' as MobileYN, ifnull(cM.DOB,'') as DOB, Cast(((julianday(date('now'))-julianday(cM.DOB))/365) as int) as Age,Cast(((julianday(date('now'))-julianday(cM.DOB))/30.4) as int) as AgeM, '' as DOBSource, '' as BPlace, '' as FNo, ifnull(cM.fatherName,'') as Father, '' as MNo, ifnull(cM.motherName,'') as Mother,";
            SQLStr += " ifnull(cM.gender,'') as Sex, '' as MS, ifnull(cM.husbandName,'') as SPNO1,'' as SPNO2,'' as SPNO3,'' as SPNO4, '' as ELCONo, '' as ELCODontKnow, '' as EDU, '' as Rel, '1' as Nationality, '' as OCP,(case when d.healthId is not null then '55' else '' end) as ExType ";
            SQLStr += " from clientMap cM left join ProviderDB pd on cM.providerId =pd.ProvCode left join death d on cM.generatedId=d.healthId Where cM.healthId NOT IN(SELECT healthId from Member where HHNo='"+g.getHouseholdNo() +"') and cM.zillaId='"+ g.getDistrict() +"' and cM.upazilaId='"+ g.getUpazila() +"' and cM.unionId='"+ g.getUnion() +"' and cM.mouzaId='"+ g.getMouza() +"' and cM.villageId='"+ g.getVillage() +"' and pd.ProvType='"+ g.getProvType() +"' and cM.providerId='"+ g.getProvCode() +"' and cM.houseGrHoldingNo='"+ g.getHouseholdNo() +"'";
*/

            //Current Code Edited by Angsuman
            /*SQLStr = "Select Dist, Upz, UN, Mouza, Vill, ProvType as provtype,ProvCode as provcode, HHNo, SNo as SNo, ifnull(HealthID,'') as HealthID, ifnull(NameEng,'') as NameEng,";
            SQLStr += " ifnull(NameBang,'') as NameBang, ifnull(Rth,'') as Rth, ifnull(HaveNID,'') as HaveNID, ifnull(NID,'') as NID, ifnull(NIDStatus,'') as NIDStatus, ifnull(HaveBR,'') as HaveBR, ifnull(BRID,'') as BRID, ifnull(BRIDStatus,'') as BRIDStatus, ifnull(MobileNo1,'') as MobileNo1,";
            SQLStr += " ifnull(MobileNo2,'') as MobileNo2,ifnull(MobileYN,'')as MobileYN, ifnull(DOB,'') as DOB, Cast(((julianday(date('now'))-julianday(DOB))/365) as int) as Age,Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) as AgeM, ifnull(DOBSource,'') as DOBSource, ifnull(BPlace,'') as BPlace, ifnull(FNo,'') as FNo, ifnull(Father,'') as Father, ifnull(MNo,'') as MNo, ifnull(Mother,'') as Mother,";
            SQLStr += " ifnull(Sex,'') as Sex, ifnull(MS,'') as MS, ifnull(SPNO1,'') as SPNO1,ifnull(SPNO2,'') as SPNO2,ifnull(SPNO3,'') as SPNO3,ifnull(SPNO4,'') as SPNO4, ifnull(ELCONo,'') as ELCONo, ifnull(ELCODontKnow,'') as ELCODontKnow, ifnull(EDU,'') as EDU, ifnull(Rel,'') as Rel, ifnull(Nationality,'') as Nationality, ifnull(OCP,'') as OCP,ifnull(ExType,'')as ExType";//ifnull(ExType,'')as ExType//(case when exType='55' then '55' else '' end) as ExType
            SQLStr += " from Member Where dist='"+ g.getDistrict() +"' and upz='"+ g.getUpazila() +"' and un='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and vill='"+ g.getVillage() +"' and HHNo='"+ g.getHouseholdNo() +"'";

            SQLStr += " UNION ALL Select cM.zillaId as Dist,cM.upazilaId as Upz,  cM.unionId as UN, cM.mouzaId as Mouza, cM.villageId as Vill, '' provtype,cM.providerId as provcode,cM.houseGrHoldingNo as HHNo, '00' as SNo, ifnull(cM.generatedId,'') as HealthID, ifnull(cM.name,'') as NameEng,";
            SQLStr += " '' as NameBang,'' as Rth, '' as HaveNID,'' as NID, '' as NIDStatus, '' as HaveBR, '' as BRID, '' BRIDStatus, ifnull(cM.mobileNo,'') as MobileNo1,";
            SQLStr += " '' as MobileNo2,'' as MobileYN, ifnull(cM.DOB,'') as DOB, Cast(((julianday(date('now'))-julianday(cM.DOB))/365) as int) as Age,Cast(((julianday(date('now'))-julianday(cM.DOB))/30.4) as int) as AgeM, '' as DOBSource, '' as BPlace, '' as FNo, ifnull(cM.fatherName,'') as Father, '' as MNo, ifnull(cM.motherName,'') as Mother,";
            SQLStr += " ifnull(cM.gender,'') as Sex, '' as MS, ifnull(cM.husbandName,'') as SPNO1,'' as SPNO2,'' as SPNO3,'' as SPNO4, '' as ELCONo, '' as ELCODontKnow, '' as EDU, '' as Rel, '1' as Nationality, '' as OCP,(case when d.healthId is not null then '55' else '' end) as ExType ";
            SQLStr += " from clientMap cM left join ProviderDB pd on cM.providerId =pd.ProvCode left join death d on cM.generatedId=d.healthId Where cM.healthId NOT IN(SELECT healthId from Member where HHNo='"+g.getHouseholdNo() +"') and cM.zillaId='"+ g.getDistrict() +"' and cM.upazilaId='"+ g.getUpazila() +"' and cM.unionId='"+ g.getUnion() +"' and cM.mouzaId='"+ g.getMouza() +"' and cM.villageId='"+ g.getVillage() +"' and cM.houseGrHoldingNo='"+ g.getHouseholdNo() +"'";
*/

            /*
            /*
            SQLStr = "Select m.Dist as Dist, m.Upz as Upz, m.UN as UN, m.Mouza as Mouza, m.Vill as Vill, m.HHNo as HHNo, m.SNo as SNo, ifnull(HealthID,'') as HealthID, ifnull(NameEng,'') as NameEng,";
            SQLStr += " ifnull(NameBang,'') as NameBang, ifnull(Rth,'') as Rth, ifnull(HaveNID,'') as HaveNID, ifnull(NID,'') as NID, ifnull(NIDStatus,'') as NIDStatus, ifnull(HaveBR,'') as HaveBR, ifnull(BRID,'') as BRID, ifnull(BRIDStatus,'') as BRIDStatus, ifnull(MobileNo1,'') as MobileNo1,";
            SQLStr += " ifnull(MobileNo2,'') as MobileNo2, ifnull(DOB,'') as DOB, Cast(((julianday(date('now'))-julianday(DOB))/365.25) as int) as Age, ifnull(DOBSource,'') as DOBSource, ifnull(BPlace,'') as BPlace, ifnull(FNo,'') as FNo, ifnull(Father,'') as Father, ifnull(MNo,'') as MNo, ifnull(Mother,'') as Mother,";
            SQLStr += " ifnull(Sex,'') as Sex, ifnull(MS,'') as MS, ifnull(SPNO1,'') as SPNO1,ifnull(SPNO2,'') as SPNO2,ifnull(SPNO3,'') as SPNO3,ifnull(SPNO4,'') as SPNO4, ifnull(m.ELCONo,'') as ELCONo, ifnull(ELCODontKnow,'') as ELCODontKnow, ifnull(EDU,'') as EDU, ifnull(Rel,'') as Rel, ifnull(Nationality,'') as Nationality, ifnull(OCP,'') as OCP,ifnull(ExType,'')as ExType,";
            SQLStr += " ifnull(p.dolmp,'')as lmp,ifnull(p.doedd,'')as edd";
            SQLStr += "     (case when cast(Visit as int)=1 then 1 else 0 end)anc1,";
            SQLStr += "     (case when cast(Visit as int)=2 then 1 else 0 end)anc2,";
            SQLStr += "     (case when cast(Visit as int)=3 then 1 else 0 end)anc3,";
            SQLStr += "     (case when cast(Visit as int)=4 then 1 else 0 end)anc4";
            SQLStr += " from Member m";
            SQLStr += " left outer join PregWomen p";
            SQLStr += " on m.dist||m.upz||m.un||m.mouza||m.vill||m.hhno||m.sno=p.dist||p.upz||p.un||p.mouza||p.vill||p.hhno||p.sno";
            SQLStr += " left outer join anc a";
            SQLStr += " on p.dist||p.upz||p.un||p.mouza||p.vill||p.hhno||p.sno=a.dist||a.upz||a.un||a.mouza||a.vill||a.hhno||a.sno";
            SQLStr += " Where m.dist='"+ g.getDistrict() +"' and m.upz='"+ g.getUpazila() +"' and m.un='"+ g.getUnion() +"' and m.Mouza='"+ g.getMouza() +"' and m.vill='"+ g.getVillage() +"' and m.HHNo='"+ g.getHouseholdNo() +"'";
            */

            //Current Code Edited by Nisan
            /*SQLStr = "Select Dist, Upz, UN, Mouza, Vill, ProvType as provtype,ProvCode as provcode, HHNo, SNo as SNo, ifnull(HealthID,'') as HealthID, ifnull(NameEng,'') as NameEng,";
            SQLStr += " ifnull(NameBang,'') as NameBang, ifnull(Rth,'') as Rth, ifnull(HaveNID,'') as HaveNID, ifnull(NID,'') as NID, ifnull(NIDStatus,'') as NIDStatus, ifnull(HaveBR,'') as HaveBR, ifnull(BRID,'') as BRID, ifnull(BRIDStatus,'') as BRIDStatus, ifnull(MobileNo1,'') as MobileNo1,";
            SQLStr += " ifnull(MobileNo2,'') as MobileNo2,ifnull(MobileYN,'')as MobileYN, ifnull(DOB,'') as DOB, Cast(((julianday(date('now'))-julianday(DOB))/365) as int) as Age,Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) as AgeM, ifnull(DOBSource,'') as DOBSource, ifnull(BPlace,'') as BPlace, ifnull(FNo,'') as FNo,  ( SELECT CASE WHEN CAST ( M.Fno AS int ) = 55 THEN ifnull( M.Father, '' ) WHEN CAST ( M.Fno AS int ) = 77 THEN ifnull( M.Father, '' ) \n" +
                    "                       WHEN CAST ( M.Fno AS int ) = 88 THEN ifnull( M.Father, '' ) ELSE(SELECT A.NameEng\n" +
                    "             FROM member A\n" +
                    "            WHERE A.SNO = M.Fno \n" +
                    "                  AND\n" +
                    "                  M.dist ='"+ g.getDistrict() +"' AND \n" +
                    "                  M.upz ='"+ g.getUpazila() +"' AND \n" +
                    "                  M.un  ='"+ g.getUnion()+"' AND \n" +
                    "                  M.Mouza  ='"+ g.getMouza()+"' AND \n" +
                    "                  M.vill  ='"+ g.getVillage()+"' \n" +
                    "                 AND\n" +
                    "                  A.HHNo = M.HHno\n" +
                    "       ) \n" +
                    "                  \n" +
                    "                  END AS Father) \n" +
                    "       AS Father, ifnull(MNo,'') as MNo, (SELECT CASE WHEN CAST ( M.MNo AS int ) = 55 THEN ifnull( M.Mother, '' ) WHEN CAST ( M.MNo AS int ) = 77 THEN ifnull( M.Mother, '' )\n" +
                    "                       WHEN CAST ( M.MNo AS int ) = 88 THEN ifnull( M.Mother, '' ) ELSE(SELECT D.NameEng FROM member D WHERE D.SNO = M.Mno \n" +
                    "                  AND\n" +
                    "                  D.dist ='"+ g.getDistrict() +"' AND \n" +
                    "                  D.upz ='"+ g.getUpazila() +"' AND \n" +
                    "                  D.un  ='"+ g.getUnion()+"' AND \n" +
                    "                  D.Mouza  ='"+ g.getMouza()+"' AND \n" +
                    "                  D.vill  ='"+ g.getVillage()+"' \n" +
                    "                  and   D.HHNo = M.HHno\n" +
                    "         ) \n" +
                    "                  \n" +
                    "                  END AS Mother) \n" +
                    "       AS Mother,";
            SQLStr += " ifnull(Sex,'') as Sex, ifnull(MS,'') as MS, ifnull(SPNO1,'') as SPNO1,ifnull(SPNO2,'') as SPNO2,ifnull(SPNO3,'') as SPNO3,ifnull(SPNO4,'') as SPNO4, ifnull(ELCONo,'') as ELCONo, ifnull(ELCODontKnow,'') as ELCODontKnow, ifnull(EDU,'') as EDU, ifnull(Rel,'') as Rel, ifnull(Nationality,'') as Nationality, ifnull(OCP,'') as OCP,ifnull(ExType,'')as ExType";//ifnull(ExType,'')as ExType//(case when exType='55' then '55' else '' end) as ExType
            SQLStr += " from Member M Where M.dist='"+ g.getDistrict() +"' and M.upz='"+ g.getUpazila() +"' and M.un='"+ g.getUnion() +"' and M.Mouza='"+ g.getMouza() +"' and M.vill='"+ g.getVillage() +"' and M.HHNo='"+ g.getHouseholdNo() +"'";

            SQLStr += " UNION ALL Select cM.zillaId as Dist,cM.upazilaId as Upz,  cM.unionId as UN, cM.mouzaId as Mouza, cM.villageId as Vill, '' provtype,cM.providerId as provcode,cM.houseGrHoldingNo as HHNo, '00' as SNo, ifnull(cM.generatedId,'') as HealthID, ifnull(cM.name,'') as NameEng,";
            SQLStr += " '' as NameBang,'' as Rth, '' as HaveNID,'' as NID, '' as NIDStatus, '' as HaveBR, '' as BRID, '' BRIDStatus, ifnull(cM.mobileNo,'') as MobileNo1,";
            SQLStr += " '' as MobileNo2,'' as MobileYN, ifnull(cM.DOB,'') as DOB, Cast(((julianday(date('now'))-julianday(cM.DOB))/365) as int) as Age,Cast(((julianday(date('now'))-julianday(cM.DOB))/30.4) as int) as AgeM, '' as DOBSource, '' as BPlace, '' as FNo, ifnull(cM.fatherName,'') as Father, '' as MNo, ifnull(cM.motherName,'') as Mother,";
            SQLStr += " ifnull(cM.gender,'') as Sex, '' as MS, ifnull(cM.husbandName,'') as SPNO1,'' as SPNO2,'' as SPNO3,'' as SPNO4, '' as ELCONo, '' as ELCODontKnow, '' as EDU, '' as Rel, '1' as Nationality, '' as OCP,(case when d.healthId is not null then '55' else '' end) as ExType ";
            SQLStr += " from clientMap cM left join ProviderDB pd on cM.providerId =pd.ProvCode left join death d on cM.generatedId=d.healthId Where cM.healthId NOT IN(SELECT healthId from Member where HHNo='"+g.getHouseholdNo() +"') and cM.zillaId='"+ g.getDistrict() +"' and cM.upazilaId='"+ g.getUpazila() +"' and cM.unionId='"+ g.getUnion() +"' and cM.mouzaId='"+ g.getMouza() +"' and cM.villageId='"+ g.getVillage() +"' and cM.houseGrHoldingNo='"+ g.getHouseholdNo() +"'";
*/
            SQLStr = "Select M.Dist AS Dist,\n" +
                    "       M.Upz AS Upz,\n" +
                    "       M.UN As UN,\n" +
                    "       M.Mouza As Mouza,\n" +
                    "       M.Vill As Vill,M.ProvType as provtype,M.ProvCode as provcode, M.HHNo, M.SNo as SNo, ifnull(M.HealthID,'') as HealthID, ifnull(M.NameEng,'') as NameEng,";
            SQLStr += " ifnull(M.NameBang,'') as NameBang, ifnull(M.Rth,'') as Rth, ifnull(M.HaveNID,'') as HaveNID, ifnull(M.NID,'') as NID, ifnull(M.NIDStatus,'') as NIDStatus, ifnull(M.HaveBR,'') as HaveBR, ifnull(M.BRID,'') as BRID, ifnull(M.BRIDStatus,'') as BRIDStatus, ifnull(M.MobileNo1,'') as MobileNo1,";
            SQLStr += " ifnull(M.MobileNo2,'') as MobileNo2,ifnull(M.MobileYN,'')as MobileYN, ifnull(M.DOB,'') as DOB, Cast(((julianday(date('now'))-julianday(M.DOB))/365) as int) as Age,Cast(((julianday(date('now'))-julianday(M.DOB))/30.4) as int) as AgeM, ifnull(M.DOBSource,'') as DOBSource, ifnull(M.BPlace,'') as BPlace, ifnull(M.FNo,'') as FNo,  ( SELECT CASE WHEN CAST ( M.Fno AS int ) = 55 THEN ifnull( M.Father, '' ) WHEN CAST ( M.Fno AS int ) = 77 THEN ifnull( M.Father, '' ) \n" +
                    "                       WHEN CAST ( M.Fno AS int ) = 88 THEN ifnull( M.Father, '' ) ELSE(SELECT A.NameEng\n" +
                    "             FROM member A\n" +
                    "            WHERE A.SNO = M.Fno \n" +
                    "                  AND\n" +
                    "                  M.dist ='" + g.getDistrict() + "' AND \n" +
                    "                  M.upz ='" + g.getUpazila() + "' AND \n" +
                    "                  M.un  ='" + g.getUnion() + "' AND \n" +
                    "                  M.Mouza  ='" + g.getMouza() + "' AND \n" +
                    "                  M.vill  ='" + g.getVillage() + "' \n" +
                    "                 AND\n" +
                    "                  A.HHNo = M.HHno\n" +
                    "       ) \n" +
                    "                  \n" +
                    "                  END AS Father) \n" +
                    "       AS Father, ifnull(MNo,'') as MNo, (SELECT CASE WHEN CAST ( M.MNo AS int ) = 55 THEN ifnull( M.Mother, '' ) WHEN CAST ( M.MNo AS int ) = 77 THEN ifnull( M.Mother, '' )\n" +
                    "                       WHEN CAST ( M.MNo AS int ) = 88 THEN ifnull( M.Mother, '' ) ELSE(SELECT D.NameEng FROM member D WHERE D.SNO = M.Mno \n" +
                    "                  AND\n" +
                    "                  D.dist ='" + g.getDistrict() + "' AND \n" +
                    "                  D.upz ='" + g.getUpazila() + "' AND \n" +
                    "                  D.un  ='" + g.getUnion() + "' AND \n" +
                    "                  D.Mouza  ='" + g.getMouza() + "' AND \n" +
                    "                  D.vill  ='" + g.getVillage() + "' \n" +
                    "                  and   D.HHNo = M.HHno\n" +
                    "         ) \n" +
                    "                  \n" +
                    "                  END AS Mother) \n" +
                    "       AS Mother,";
            SQLStr += " ifnull(M.Sex,'') as Sex, ifnull(M.MS,'') as MS, ifnull(M.SPNO1,'') as SPNO1,ifnull(M.SPNO2,'') as SPNO2,ifnull(M.SPNO3,'') as SPNO3,ifnull(M.SPNO4,'') as SPNO4, ifnull(M.ELCONo,'') as ELCONo, ifnull(M.ELCODontKnow,'') as ELCODontKnow, ifnull(M.EDU,'') as EDU, ifnull(M.Rel,'') as Rel, ifnull(M.Nationality,'') as Nationality, ifnull(M.OCP,'') as OCP,ifnull(M.ExType,'')as ExType,( CASE\n" +
                    "                WHEN mp.healthId IS NOT NULL THEN 'Y' \n" +
                    "                ELSE '' \n" +
                    "       END ) AS MemberFPIhid";//ifnull(ExType,'')as ExType//(case when exType='55' then '55' else '' end) as ExType
            SQLStr += " from Member M LEFT Outer JOIN MemberFPI mp\n" +
                    "              ON mp.healthId = M.healthId Where M.dist='" + g.getDistrict() + "' and M.upz='" + g.getUpazila() + "' and M.un='" + g.getUnion() + "' and M.Mouza='" + g.getMouza() + "' and M.vill='" + g.getVillage() + "' and M.HHNo='" + g.getHouseholdNo() + "'";

            SQLStr += " UNION ALL Select cM.zillaId as Dist,cM.upazilaId as Upz,  cM.unionId as UN, cM.mouzaId as Mouza, cM.villageId as Vill, '' provtype,cM.providerId as provcode,cM.houseGrHoldingNo as HHNo, '00' as SNo, ifnull(cM.generatedId,'') as HealthID, ifnull(cM.name,'') as NameEng,";
            SQLStr += " '' as NameBang,'' as Rth, '' as HaveNID,'' as NID, '' as NIDStatus, '' as HaveBR, '' as BRID, '' BRIDStatus, ifnull(cM.mobileNo,'') as MobileNo1,";
            SQLStr += " '' as MobileNo2,'' as MobileYN, ifnull(cM.DOB,'') as DOB, Cast(((julianday(date('now'))-julianday(cM.DOB))/365) as int) as Age,Cast(((julianday(date('now'))-julianday(cM.DOB))/30.4) as int) as AgeM, '' as DOBSource, '' as BPlace, '' as FNo, ifnull(cM.fatherName,'') as Father, '' as MNo, ifnull(cM.motherName,'') as Mother,";
            SQLStr += " ifnull(cM.gender,'') as Sex, '' as MS, ifnull(cM.husbandName,'') as SPNO1,'' as SPNO2,'' as SPNO3,'' as SPNO4, '' as ELCONo, '' as ELCODontKnow, '' as EDU, '' as Rel, '1' as Nationality, '' as OCP,(case when d.healthId is not null then '55' else '' end) as ExType,'' AS MemberFPIhid ";
            SQLStr += " from clientMap cM left join ProviderDB pd on cM.providerId =pd.ProvCode left join death d on cM.generatedId=d.healthId Where cM.healthId NOT IN(SELECT healthId from Member where HHNo='" + g.getHouseholdNo() + "') and cM.zillaId='" + g.getDistrict() + "' and cM.upazilaId='" + g.getUpazila() + "' and cM.unionId='" + g.getUnion() + "' and cM.mouzaId='" + g.getMouza() + "' and cM.villageId='" + g.getVillage() + "' and cM.houseGrHoldingNo='" + g.getHouseholdNo() + "'";

            Cursor cur1 = C.ReadData(SQLStr);

            cur1.moveToFirst();
            mylist.clear();
            int i = 0;
            list.setAdapter(null);
           // ListView list = (ListView) findViewById(R.id.lstMember);
            if (heading == true) {
       /*         View header = getLayoutInflater().inflate(R.layout.memberlistheading, null);
                list.addHeaderView(header);*/
            }

           // int i = 0;
            while (!cur1.isAfterLast()) {
                HashMap<String, String> map = new HashMap<String, String>();

                if (i == 0) {
                    //View header = getLayoutInflater().inflate(R.layout.memberlistheading, null);
                    //list.addHeaderView(header);
                }
                map.put("dist", cur1.getString(cur1.getColumnIndex("Dist")));
                map.put("upz", cur1.getString(cur1.getColumnIndex("Upz")));
                map.put("un", cur1.getString(cur1.getColumnIndex("UN")));
                map.put("mouza", cur1.getString(cur1.getColumnIndex("Mouza")));
                map.put("vill", cur1.getString(cur1.getColumnIndex("Vill")));
                map.put("provtype", cur1.getString(cur1.getColumnIndex("provtype")));
                map.put("provcode", cur1.getString(cur1.getColumnIndex("provcode")));
                map.put("hhno", cur1.getString(cur1.getColumnIndex("HHNo")));
                map.put("sno", cur1.getString(cur1.getColumnIndex("SNo")));
                map.put("nameeng", cur1.getString(cur1.getColumnIndex("NameEng")).replace("null", ""));
                map.put("namebang", cur1.getString(cur1.getColumnIndex("NameBang")).replace("null", ""));
                map.put("healthid", cur1.getString(cur1.getColumnIndex("HealthID")).replace("null", ""));
                map.put("rth", cur1.getString(cur1.getColumnIndex("Rth")).replace("null", ""));
                map.put("havenid", cur1.getString(cur1.getColumnIndex("HaveNID")).replace("null", ""));
                map.put("nid", cur1.getString(cur1.getColumnIndex("NID")).replace("null", ""));
                map.put("nidstatus", cur1.getString(cur1.getColumnIndex("NIDStatus")).replace("null", ""));
                map.put("havebr", cur1.getString(cur1.getColumnIndex("HaveBR")).replace("null", ""));
                map.put("brid", cur1.getString(cur1.getColumnIndex("BRID")).replace("null", ""));
                map.put("bridstatus", cur1.getString(cur1.getColumnIndex("BRIDStatus")).replace("null", ""));
                map.put("mobileno1", cur1.getString(cur1.getColumnIndex("MobileNo1")).replace("null", ""));
                map.put("mobileno2", cur1.getString(cur1.getColumnIndex("MobileNo2")).replace("null", ""));
                map.put("mobileyn", cur1.getString(cur1.getColumnIndex("MobileYN")).replace("null", ""));
                map.put("dob", cur1.getString(cur1.getColumnIndex("DOB")).replace("null", ""));
                map.put("age", cur1.getString(cur1.getColumnIndex("Age")).replace("null", ""));
                map.put("agem", cur1.getString(cur1.getColumnIndex("AgeM")).replace("null", ""));
                map.put("dobsource", cur1.getString(cur1.getColumnIndex("DOBSource")).replace("null", ""));
                map.put("bplace", cur1.getString(cur1.getColumnIndex("BPlace")).replace("null", ""));
                map.put("fno", cur1.getString(cur1.getColumnIndex("FNo")).replace("null", ""));
                map.put("father", cur1.getString(cur1.getColumnIndex("Father")).replace("null", ""));
                map.put("mno", cur1.getString(cur1.getColumnIndex("MNo")).replace("null", ""));
                map.put("mother", cur1.getString(cur1.getColumnIndex("Mother")).replace("null", ""));
                map.put("sex", cur1.getString(cur1.getColumnIndex("Sex")).replace("null", ""));
                map.put("ms", cur1.getString(cur1.getColumnIndex("MS")).replace("null", ""));
                map.put("elcono", cur1.getString(cur1.getColumnIndex("ELCONo")).replace("null", ""));
                map.put("elcodontknow", cur1.getString(cur1.getColumnIndex("ELCODontKnow")).replace("null", ""));
                map.put("edu", cur1.getString(cur1.getColumnIndex("EDU")).replace("null", ""));
                map.put("rel", cur1.getString(cur1.getColumnIndex("Rel")).replace("null", ""));
                map.put("nationality", cur1.getString(cur1.getColumnIndex("Nationality")).replace("null", ""));
                map.put("ocp", cur1.getString(cur1.getColumnIndex("OCP")).replace("null", ""));
                map.put("spno1", cur1.getString(cur1.getColumnIndex("SPNO1")).replace("null", ""));
                map.put("spno2", cur1.getString(cur1.getColumnIndex("SPNO2")).replace("null", ""));
                map.put("spno3", cur1.getString(cur1.getColumnIndex("SPNO3")).replace("null", ""));
                map.put("spno4", cur1.getString(cur1.getColumnIndex("SPNO4")).replace("null", ""));
                map.put("extype", cur1.getString(cur1.getColumnIndex("ExType")).replace("null", ""));
                map.put("mfpihid", cur1.getString(cur1.getColumnIndex("MemberFPIhid")).replace("null", ""));
                /*map.put("provtype", cur1.getString(cur1.getColumnIndex("provtype")));
                map.put("provcode", cur1.getString(cur1.getColumnIndex("provcode")));*/

                map.put("provtype", g.getProvType());
                map.put("provcode", g.getProvCode());

                /*map.put("lmp", cur1.getString(cur1.getColumnIndex("lmp")));
                map.put("edd", cur1.getString(cur1.getColumnIndex("edd")));
                map.put("anc1", cur1.getString(cur1.getColumnIndex("anc1")));
                map.put("anc2", cur1.getString(cur1.getColumnIndex("anc2")));
                map.put("anc3", cur1.getString(cur1.getColumnIndex("anc3")));
                map.put("anc4", cur1.getString(cur1.getColumnIndex("anc4")));*/

                mylist.add(map);
                mSchedule = new SimpleAdapter(MemberListFPI.this, mylist, R.layout.memberlistrow, new String[]{"sno"},
                        new int[]{R.id.SNo});
                list.setAdapter(new MemberListAdapter(this));

                i += 1;
                cur1.moveToNext();
            }
            cur1.close();


        } catch (Exception e) {
            Connection.MessageBox(MemberListFPI.this, e.getMessage());
        }
    }


    public class MemberListAdapter extends BaseAdapter {
        private Context context;

        public MemberListAdapter(Context c) {
            context = c;
        }

        public int getCount() {
            return mSchedule.getCount();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }


        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.memberlistrow, null);
            }

            try {

                TextView sno = (TextView) convertView.findViewById(R.id.SNo);
                TextView nameeng = (TextView) convertView.findViewById(R.id.Name);
                TextView healthid = (TextView) convertView.findViewById(R.id.HealthID);
                TextView rth = (TextView) convertView.findViewById(R.id.Rth);
                TextView havenid = (TextView) convertView.findViewById(R.id.HaveNID);
                TextView nid = (TextView) convertView.findViewById(R.id.NID);
                TextView nidstatus = (TextView) convertView.findViewById(R.id.NIDStatus);
                TextView havebr = (TextView) convertView.findViewById(R.id.HaveBR);
                TextView brid = (TextView) convertView.findViewById(R.id.BRID);
                TextView bridstatus = (TextView) convertView.findViewById(R.id.BRIDStatus);
                TextView mobileno1 = (TextView) convertView.findViewById(R.id.MobileNo1);
                TextView mobileno2 = (TextView) convertView.findViewById(R.id.MobileNo2);
                TextView mobileyn = (TextView) convertView.findViewById(R.id.MobileYN);
                TextView dob = (TextView) convertView.findViewById(R.id.DOB);
                TextView age = (TextView) convertView.findViewById(R.id.Age);
                TextView dobsource = (TextView) convertView.findViewById(R.id.DOBSource);
                TextView bplace = (TextView) convertView.findViewById(R.id.BPlace);
                TextView fno = (TextView) convertView.findViewById(R.id.FNo);
                TextView father = (TextView) convertView.findViewById(R.id.Father);
                TextView mno = (TextView) convertView.findViewById(R.id.MNo);
                TextView mother = (TextView) convertView.findViewById(R.id.Mother);
                TextView sex = (TextView) convertView.findViewById(R.id.Sex);
                TextView ms = (TextView) convertView.findViewById(R.id.MS);
                TextView elcono = (TextView) convertView.findViewById(R.id.ELCONo);
                TextView elcodontknow = (TextView) convertView.findViewById(R.id.ELCODontKnow);
                TextView edu = (TextView) convertView.findViewById(R.id.EDU);
                TextView rel = (TextView) convertView.findViewById(R.id.Rel);
                TextView nationality = (TextView) convertView.findViewById(R.id.Nationality);
                TextView ocp = (TextView) convertView.findViewById(R.id.OCP);

                TextView sp1 = (TextView) convertView.findViewById(R.id.Sp1);
                TextView sp2 = (TextView) convertView.findViewById(R.id.Sp2);
                TextView sp3 = (TextView) convertView.findViewById(R.id.Sp3);
                TextView sp4 = (TextView) convertView.findViewById(R.id.Sp4);

                TextView FPMethod = (TextView) convertView.findViewById(R.id.FPMethod);
                TextView LMP = (TextView) convertView.findViewById(R.id.LMP);
                TextView EDD = (TextView) convertView.findViewById(R.id.EDD);

                Button cmdANC1 = (Button) convertView.findViewById(R.id.cmdANC1);
                Button cmdANC2 = (Button) convertView.findViewById(R.id.cmdANC2);
                Button cmdANC3 = (Button) convertView.findViewById(R.id.cmdANC3);
                Button cmdANC4 = (Button) convertView.findViewById(R.id.cmdANC4);


                final HashMap<String, String> o = (HashMap<String, String>) mSchedule.getItem(position);

                /*if(o.get("anc1").equals("1")) cmdANC1.setBackgroundColor(Color.parseColor("#FF0000"));
                if(o.get("anc2").equals("1")) cmdANC1.setBackgroundColor(Color.parseColor("#FF0000"));
                if(o.get("anc3").equals("1")) cmdANC1.setBackgroundColor(Color.parseColor("#FF0000"));
                if(o.get("anc4").equals("1")) cmdANC1.setBackgroundColor(Color.parseColor("#FF0000"));
                */

                sno.setText(o.get("sno"));


                //----------------------------------------------------------------------------------
                /*if (PregWomenCurrentStatus(o.get("sno")).equals("01"))
                    FPMethod.setText("খাবার বড়ি");
                else if (PregWomenCurrentStatus(o.get("sno")).equals("02"))
                    FPMethod.setText("কনডম");
                else if (PregWomenCurrentStatus(o.get("sno")).equals("03"))
                    FPMethod.setText("ইনজেকটেবল");
                else if (PregWomenCurrentStatus(o.get("sno")).equals("04"))
                    FPMethod.setText("আই ইউ ডি");
                else if (PregWomenCurrentStatus(o.get("sno")).equals("05"))
                    FPMethod.setText("ইমপ্যান্ট");
                else if (PregWomenCurrentStatus(o.get("sno")).equals("06"))
                    FPMethod.setText("স্থায়ী পদ্ধতি(মহিলা)");
                else if (PregWomenCurrentStatus(o.get("sno")).equals("07"))
                    FPMethod.setText("ইসিপি");
                else if (PregWomenCurrentStatus(o.get("sno")).equals("08"))
                    FPMethod.setText("মিসোপ্রোস্টোল");
                else if (PregWomenCurrentStatus(o.get("sno")).equals("09"))
                    FPMethod.setText("পার্শ্ব-প্রতিক্রিয়ার জন্য প্রেরন ");
                else if (PregWomenCurrentStatus(o.get("sno")).equals("10"))
                    FPMethod.setText("পদ্ধতির জন্য প্রেরন");

                LMP.setText(Global.DateConvertDMY(LMPDate(o.get("sno"))));
                EDD.setText(Global.DateConvertDMY(EDDDate(o.get("sno"))));
                */
                //LMP.setText(o.get("lmp"));
                //EDD.setText(o.get("edd"));

                //----------------------------------------------------------------------------------

                nameeng.setText(o.get("nameeng"));
                healthid.setText(o.get("healthid"));

                // healthid.setText();
                rth.setText(o.get("rth"));


                nid.setText(o.get("nid"));
                if (o.get("havenid").equals("1")) {
                    havenid.setText("হাঁ");

                    if (o.get("nidstatus").equals("1"))
                        nidstatus.setText("অন্য জায়গায় আছে");
                    else if (o.get("nidstatus").equals("2"))
                        nidstatus.setText("হারিয়ে ফেলেছি");
                    else if (o.get("nidstatus").equals("3"))
                        nidstatus.setText("খুজে পাচ্ছি না");
                } else {
                    havenid.setText("না");

                    if (o.get("nidstatus").equals("1"))
                        nidstatus.setText("কখনো ছিল না");
                    else if (o.get("nidstatus").equals("2"))
                        nidstatus.setText("হারিয়ে ফেলেছি");
                    else if (o.get("nidstatus").equals("3"))
                        nidstatus.setText("খুজে পাচ্ছি না");
                    else if (o.get("nidstatus").equals("4"))
                        nidstatus.setText("নাগরিক নয়");
                }


                brid.setText(o.get("brid"));
                if (o.get("havebr").equals("1")) {
                    havebr.setText("হাঁ");

                    if (o.get("bridstatus").equals("1"))
                        bridstatus.setText("অন্য জায়গায় আছে");
                    else if (o.get("bridstatus").equals("2"))
                        bridstatus.setText("হারিয়ে ফেলেছি");
                    else if (o.get("bridstatus").equals("3"))
                        bridstatus.setText("খুজে পাচ্ছি না");
                } else {
                    havebr.setText("না");

                    if (o.get("bridstatus").equals("1"))
                        bridstatus.setText("কখনো ছিল না");
                    else if (o.get("bridstatus").equals("2"))
                        bridstatus.setText("হারিয়ে ফেলেছি");
                    else if (o.get("bridstatus").equals("3"))
                        bridstatus.setText("খুজে পাচ্ছি না");
                    else if (o.get("bridstatus").equals("4"))
                        bridstatus.setText("নাগরিক নয়");
                }

                mobileno1.setText(o.get("mobileno1"));
                mobileno2.setText(o.get("mobileno2"));
                if (o.get("mobileyn").equals("1"))
                    mobileyn.setText("নাই");
                else
                    mobileyn.setText("");

                dob.setText(Global.DateConvertDMY(o.get("dob")));
                if (o.get("agem").replace("null", "").length() > 0) {
                    if (Integer.parseInt(o.get("agem")) < 12) {
                        age.setText(o.get("agem") + "(মাস)");
                    } else {
                        age.setText(o.get("age") + "(বছর)");
                    }
                } else {
                    age.setText(o.get("age") + "(বছর)");
                }

                dobsource.setText(o.get("dobsource").equals("1") ? "প্রকৃত" : "আনুমানিক");
                bplace.setText(o.get("bplace"));
                fno.setText(o.get("fno"));
                father.setText(o.get("father"));
                mno.setText(o.get("mno"));
                mother.setText(o.get("mother"));

                if (o.get("sex").equals("1"))
                    sex.setText("পুরুষ");
                else if (o.get("sex").equals("2"))
                    sex.setText("মহিলা");
                else if (o.get("sex").equals("3"))
                    sex.setText("হিজরা");
                else if (o.get("sex").equals(""))
                    sex.setText("");

                // sex.setText(o.get("sex").equals("1") ? "পুরুষ" : (o.get("sex").equals("2") ? "মহিলা" : "হিজরা"));

                if (o.get("ms").equals("1"))
                    ms.setText("অবিবাহিত");
                else if (o.get("ms").equals("2"))
                    ms.setText("বিবাহিত");
                else if (o.get("ms").equals("3"))
                    ms.setText("বিধবা/বিপত্নীক");
                else if (o.get("ms").equals("4"))
                    ms.setText("স্বামী/স্ত্রী পৃথক");
                else if (o.get("ms").equals("3"))
                    ms.setText("তালাক প্রাপ্ত/ বিবাহ বিচ্ছিন্ন");

                sp1.setText(o.get("spno1"));
                sp2.setText(o.get("spno2"));
                sp3.setText(o.get("spno3"));
                sp4.setText(o.get("spno4"));

                elcono.setText(o.get("elcono"));
                if (o.get("elcodontknow").equals("1"))
                    elcodontknow.setText("জানা নেই");
                else
                    elcodontknow.setText(o.get("elcodontknow"));

                edu.setText(o.get("edu"));
                if (o.get("rel").equals("1"))
                    rel.setText("ইসলাম");
                else if (o.get("rel").equals("2"))
                    rel.setText("হিন্দু");
                else if (o.get("rel").equals("3"))
                    rel.setText("বৌদ্ধ");
                else if (o.get("rel").equals("4"))
                    rel.setText("খ্রীস্টান");
                else if (o.get("rel").equals("8"))
                    rel.setText("Refuse to disclose");
                else if (o.get("rel").equals("9"))
                    rel.setText("Not a believer");
                else if (o.get("rel").equals("0"))
                    rel.setText("অন্যান্য");

                //nationality.setText(o.get("nationality"));
                if (o.get("nationality").equals("1"))
                    nationality.setText("বাংলাদেশি");
                else
                    nationality.setText("অন্যদেশি");

                ocp.setText(o.get("ocp"));

                final TableLayout memtab = (TableLayout) convertView.findViewById(R.id.memtab);

                //alternative row color
                // if (o.get("extype").toString().length() != 0 ||o.get("extype").equals("55"))
                if (o.get("extype").equals("55")) {
                    nameeng.setTextColor(Color.parseColor("#FF0000"));
                    sno.setTextColor(Color.parseColor("#FF0000"));
                    nameeng.setTextColor(Color.parseColor("#FF0000"));
                    healthid.setTextColor(Color.parseColor("#FF0000"));
                    rth.setTextColor(Color.parseColor("#FF0000"));
                    havenid.setTextColor(Color.parseColor("#FF0000"));
                    nid.setTextColor(Color.parseColor("#FF0000"));
                    nidstatus.setTextColor(Color.parseColor("#FF0000"));
                    havebr.setTextColor(Color.parseColor("#FF0000"));
                    brid.setTextColor(Color.parseColor("#FF0000"));
                    bridstatus.setTextColor(Color.parseColor("#FF0000"));
                    mobileno1.setTextColor(Color.parseColor("#FF0000"));
                    mobileno2.setTextColor(Color.parseColor("#FF0000"));
                    mobileyn.setTextColor(Color.parseColor("#FF0000"));
                    dob.setTextColor(Color.parseColor("#FF0000"));
                    age.setTextColor(Color.parseColor("#FF0000"));
                    dobsource.setTextColor(Color.parseColor("#FF0000"));
                    bplace.setTextColor(Color.parseColor("#FF0000"));
                    fno.setTextColor(Color.parseColor("#FF0000"));
                    father.setTextColor(Color.parseColor("#FF0000"));
                    mno.setTextColor(Color.parseColor("#FF0000"));
                    mother.setTextColor(Color.parseColor("#FF0000"));
                    sex.setTextColor(Color.parseColor("#FF0000"));
                    ms.setTextColor(Color.parseColor("#FF0000"));
                    elcono.setTextColor(Color.parseColor("#FF0000"));
                    elcodontknow.setTextColor(Color.parseColor("#FF0000"));
                    edu.setTextColor(Color.parseColor("#FF0000"));
                    rel.setTextColor(Color.parseColor("#FF0000"));
                    nationality.setTextColor(Color.parseColor("#FF0000"));
                    ocp.setTextColor(Color.parseColor("#FF0000"));
                    sp1.setTextColor(Color.parseColor("#FF0000"));
                    sp2.setTextColor(Color.parseColor("#FF0000"));
                    sp3.setTextColor(Color.parseColor("#FF0000"));
                    sp4.setTextColor(Color.parseColor("#FF0000"));
                    FPMethod.setTextColor(Color.parseColor("#FF0000"));
                    LMP.setTextColor(Color.parseColor("#FF0000"));
                    EDD.setTextColor(Color.parseColor("#FF0000"));

                    //memtab.setBackgroundColor(Color.parseColor("#FF0000"));
                } else {
                    if (position % 2 == 0) {
                        memtab.setBackgroundColor(Color.parseColor("#F3F3F3"));
                    } else {
                        memtab.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    }
                }

                if (o.get("mfpihid").equals("Y")) {


                    memtab.setBackgroundColor(Color.parseColor("#99cc33"));
                }
                if (o.get("mfpihid").equals("")) {
                    memtab.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    // memtabrow.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
            /*    else
                {
                    if (position % 2 == 0) {
                        memtab.setBackgroundColor(Color.parseColor("#F3F3F3"));
                    } else {
                        memtab.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    }
                }*/


                memtab.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        //if(o.get("extype").toString().length()!=0)
                        // {
                        if (o.get("extype").toString().equals("55"))

                        {
                            //Connection.MessageBox(MemberListFPI.this,"সদস্য মারা গিয়েছে।");
                            g.setCallFrom("ml");
                            g.setSerialNo(o.get("sno"));
                            g.setHealthID(o.get("healthid"));
                            g.setHouseholdNo(o.get("hhno"));
                            g.setAName(o.get("nameeng"));
                            g.setAAge(o.get("age"));
                            g.setASex(o.get("sex"));
                            memtab.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    final Dialog dialog = new Dialog(MemberListFPI.this);
                                    AlertDialog.Builder adb = new AlertDialog.Builder(MemberListFPI.this);
                                    adb.setTitle("মৃত্যু তথ্য ");
                                    adb.setMessage("আপনি কি মৃত্যু তথ্য সংশোধন /বাতিল করতে  চান  [সংশোধন/বাতিল]?");

                                    adb.setNegativeButton("সংশোধন ", new AlertDialog.OnClickListener() {
                                        public void onClick(DialogInterface dialog1, int which) {
                                            dialog.dismiss();


                                            String TotalU = C.ReturnSingleValue("Select healthId from clientMap where  healthId='" + o.get("healthid") + "'");
                                            String GeneratedIdU = C.ReturnSingleValue("Select GeneratedId from clientMap where  healthId='" + o.get("healthid") + "'");

                                            if (TotalU.equals(GeneratedIdU) || TotalU.length() >= 1) {
                                                g.setGeneratedId(GeneratedIdU);

                                            }
                                            g.setCallFrom("ml");

                                           finish();
                                           /* Intent f1 = new Intent(getApplicationContext(), DeathForm.class);
                                            startActivity(f1);*/
                                        }
                                    });

                                    adb.setPositiveButton("বাতিল", new AlertDialog.OnClickListener() {
                                        public void onClick(DialogInterface dialog1, int which) {
                                            dialog.dismiss();
                                            String TotalD = C.ReturnSingleValue("Select healthId from clientMap where  healthId='" + o.get("healthid") + "'");
                                            String GeneratedIdD = C.ReturnSingleValue("Select GeneratedId from clientMap where  healthId='" + o.get("healthid") + "'");

                                            if (TotalD.equals(GeneratedIdD) || TotalD.length() >= 1) {
                                                g.setGeneratedId(GeneratedIdD);
                                            }
                                            C.Save("delete from death where healthId='" + g.getGeneratedId() + "'");
                                            C.Save("update Member set Extype=''  where healthId='" + o.get("healthid") + "'");
                                            finish();
                                            Intent f1 = new Intent(getApplicationContext(), MemberListFPI.class);
                                            startActivity(f1);
                                        }
                                    });

                                    adb.show();
                                }
                            });
                            // else
                            // Connection.MessageBox(MemberListFPI.this,"সদস্য এই খানায় সক্রিয় নয়।");

                            // return;
                        } else {

                            g.setVillage(o.get("vill"));
                            g.setDistrict(o.get("dist"));
                            g.setUpazila(o.get("upz"));
                            g.setUnion(o.get("un"));
                            g.setMouza(o.get("mouza"));
                            g.setHouseholdNo(o.get("hhno").toString());
                            g.setFatherName(o.get("father").toString());
                            g.setMotherName(o.get("mother").toString());
                            g.setHusName(o.get("spno1").toString());
                            // g.setFatherName(o.get("father").toString());
                            // g.setMotherName(o.get("mother").toString());
                            String Total = C.ReturnSingleValue("Select healthId from clientMap where  healthId='" + o.get("healthid") + "'");
                            String GeneratedId = C.ReturnSingleValue("Select GeneratedId from clientMap where  healthId='" + o.get("healthid") + "'");
                            if (Total.length() <= 0) {
                                ClientMap cMap = new ClientMap();
                                cMap.setHealthId(o.get(("healthid")));
                                cMap.setProviderId(o.get(("provcode")));
                                cMap.setName(o.get(("nameeng")));
                                cMap.setAge(o.get(("age")));
                                cMap.setDoB(o.get(("dob")));
                                cMap.setGender(o.get(("sex")));
                                //cMap.setHusbandName(g.GetHusNameNrc(C, o.get("healthid")));
                                cMap.setFatherName(o.get("father").toString());
                                cMap.setMotherName(o.get("mother").toString());
                                // cMap.setFatherName(g.GetFatherNameNrc(C, o.get("healthid")));
                                //  cMap.setMotherName(g.GetMotherNameNrc(C, o.get("healthid")));
                                cMap.setDivisionId("30");
                                cMap.setZillaId(o.get("dist"));
                                cMap.setUpazilaId(o.get("upz"));
                                cMap.setUnionId(o.get("un"));
                                cMap.setMouzaId(o.get("mouza"));
                                cMap.setVillageId(o.get("vill"));
                                cMap.setHouseGRHoldingNo(o.get("hhno"));//
                                // cMap.setHouseGRHoldingNo(txtHHNo.getText().toString());
                                cMap.setMobileNo(o.get("mobileno1"));
                                String id = cMap.getName() + cMap.getFatherName() + cMap.getMotherName() + cMap.getDoB() + cMap.getZillaId() + cMap.getUpazilaId() + cMap.getUnionId() + cMap.getMouzaId() + cMap.getVillageId();
                                cMap.setGeneratedId(cMap.generateHash(id));
                                cMap.setUpload("2");
                                cMap.setSystemEntryDate(Global.DateTimeNowYMDHMS());
                                cMap.SaveFromMember(cMap, getApplicationContext());
                                // String CGeneratedId=C.ReturnSingleValue("Select GeneratedId from clientMap where  healthId='" + o.get("healthid")+ "'");
                                g.setGeneratedId(cMap.getGeneratedId());


                            } else if (Total.equals(GeneratedId) || Total.length() >= 1) {
                                g.setGeneratedId(GeneratedId);
                            } else {

                            }
                            EventForm(o.get("provtype").toString(), o.get("provcode").toString(), o.get("hhno").toString(), o.get("sno").toString(), o.get("nameeng").toString(), o.get("age").toString(), o.get("ms").toString(), o.get("sex"), o.get("healthid"), Global.DateConvertDMY(o.get("dob")));
                        }
                    }
                });


                ImageButton memberImage = (ImageButton) convertView.findViewById(R.id.memberImage);

                Integer agemonth = Integer.valueOf(o.get("agem"));
                if (o.get("ms").equals("2")) {
                    memberImage.setBackgroundResource(R.drawable.couple);
                }
                //Pregnant women


                //Male member
                else if (o.get("sex").equals("1") & agemonth < 12) {
                    memberImage.setBackgroundResource(R.drawable.male0to1);
                } else if (o.get("sex").equals("1") & agemonth <= 59) {
                    memberImage.setBackgroundResource(R.drawable.male0to5);
                } else if (o.get("sex").equals("1")) {
                    memberImage.setBackgroundResource(R.drawable.male);
                }
                //Female member
                else if (o.get("sex").equals("2") & agemonth < 12) {
                    memberImage.setBackgroundResource(R.drawable.female0to1);
                } else if (o.get("sex").equals("2") & agemonth <= 59) {
                    memberImage.setBackgroundResource(R.drawable.female0to5);
                } else if (o.get("sex").equals("2")) {
                    memberImage.setBackgroundResource(R.drawable.female);
                }


                final LinearLayout SecFPMethod = (LinearLayout) convertView.findViewById(R.id.SecFPMethod);
                final LinearLayout SecPreg = (LinearLayout) convertView.findViewById(R.id.SecPreg);
                final LinearLayout SecDeliv = (LinearLayout) convertView.findViewById(R.id.SecDeliv);
                final LinearLayout SecPNC = (LinearLayout) convertView.findViewById(R.id.SecPNC);

                //Default : hide all section
                SecFPMethod.setVisibility(View.GONE);
                SecPreg.setVisibility(View.GONE);
                SecDeliv.setVisibility(View.GONE);
                SecPNC.setVisibility(View.GONE);

                //Status of ELCO Visit
                String ElcoVisitStatus = ElcoVisitStatus(o.get("dist"), o.get("upz"), o.get("un"), o.get("mouza"), o.get("vill"), o.get("provtype"), o.get("provcode"), o.get("hhno"), o.get("sno"));

                //No Status for Male Member
                if (o.get("sex").equals("1") | !o.get("ms").equals("2")) {
                    SecFPMethod.setVisibility(View.GONE);
                    SecPreg.setVisibility(View.GONE);
                    SecDeliv.setVisibility(View.GONE);
                    SecPNC.setVisibility(View.GONE);
                }

                //Method Used
                else if (!ElcoVisitStatus.equals("12") & !ElcoVisitStatus.equals("13") & !ElcoVisitStatus.equals("14")) {
                    SecFPMethod.setVisibility(View.VISIBLE);
                    SecPreg.setVisibility(View.GONE);
                    SecDeliv.setVisibility(View.GONE);
                    SecPNC.setVisibility(View.GONE);

                    if (ElcoVisitStatus.equals("01"))
                        FPMethod.setText("খাবার বড়ি");
                    else if (ElcoVisitStatus.equals("02"))
                        FPMethod.setText("কনডম");
                    else if (ElcoVisitStatus.equals("03"))
                        FPMethod.setText("ইনজেকটেবল");
                    else if (ElcoVisitStatus.equals("04"))
                        FPMethod.setText("আই ইউ ডি");
                    else if (ElcoVisitStatus.equals("05"))
                        FPMethod.setText("ইমপ্যান্ট");
                    else if (ElcoVisitStatus.equals("06"))
                        FPMethod.setText("স্থায়ী পদ্ধতি(মহিলা)");
                    else if (ElcoVisitStatus.equals("07"))
                        FPMethod.setText("ইসিপি");
                    else if (ElcoVisitStatus.equals("08"))
                        FPMethod.setText("মিসোপ্রোস্টোল");
                    else if (ElcoVisitStatus.equals("09"))
                        FPMethod.setText("পার্শ্ব-প্রতিক্রিয়ার জন্য প্রেরন ");
                    else if (ElcoVisitStatus.equals("10"))
                        FPMethod.setText("পদ্ধতির জন্য প্রেরন");

                }

                //Pregnancy Status & ANC visit
                else if (ElcoVisitStatus.equals("12")) {
                    //need to highlight if pregnancy is high risk
                    memberImage.setBackgroundResource(R.drawable.preg);
                    SecFPMethod.setVisibility(View.GONE);
                    SecPreg.setVisibility(View.VISIBLE);
                    SecDeliv.setVisibility(View.GONE);
                    SecPNC.setVisibility(View.GONE);
                    String SQ = "";
                    SQ = "select PGN||'^'||DOLMP||'^'||date(DOLMP,'+280 day') from PregWomen where";
                    SQ += " Dist='" + o.get("dist") + "' and";
                    SQ += " Upz='" + o.get("upz") + "' and";
                    SQ += " UN='" + o.get("un") + "' and";
                    SQ += " Vill='" + o.get("vill") + "' and";
                   /* SQ += " ProvType='"+ o.get("provtype") +"' and";
                    SQ += " ProvCode='"+ o.get("provcode") +"' and";*/
                    SQ += " HHNo='" + o.get("hhno") + "' and SNo='" + o.get("sno") + "'";
                    SQ += " order by cast(DOLMP as date) desc limit 1";
                    String PGNLMP = C.ReturnSingleValue(SQ);
                    String[] PGNLMPValue = Connection.split(PGNLMP, '^');
                    String PGN = PGNLMPValue[0].toString();
                    String LMPDT = PGNLMPValue[1].toString();
                    String EDDDT = PGNLMPValue[2].toString();
                    LMP.setText(Global.DateConvertDMY(LMPDT));
                    EDD.setText(Global.DateConvertDMY(EDDDT));

                    String[] ANCVisit = GetANCVisit(o.get("dist"), o.get("upz"), o.get("un"), o.get("mouza"), o.get("vill"), o.get("provtype"), o.get("provcode"), o.get("hhno"), o.get("sno"), PGN);
                    cmdANC1.setBackgroundColor(Color.parseColor("#F3F3F3"));
                    cmdANC2.setBackgroundColor(Color.parseColor("#F3F3F3"));
                    cmdANC3.setBackgroundColor(Color.parseColor("#F3F3F3"));
                    cmdANC4.setBackgroundColor(Color.parseColor("#F3F3F3"));
                    //ANC Schedule

                    //ANC Given
                    for (int a = 0; a < ANCVisit.length; a++) {
                        if (a == 0) {
                            cmdANC1.setBackgroundColor(Color.parseColor("#00CC33"));
                            cmdANC1.setTextColor(Color.parseColor("#FFFFFF"));
                        } else if (a == 1) {
                            cmdANC2.setBackgroundColor(Color.parseColor("#00CC33"));
                            cmdANC2.setTextColor(Color.parseColor("#FFFFFF"));
                        } else if (a == 2) {
                            cmdANC3.setBackgroundColor(Color.parseColor("#00CC33"));
                            cmdANC3.setTextColor(Color.parseColor("#FFFFFF"));
                        } else if (a == 3) {
                            cmdANC4.setBackgroundColor(Color.parseColor("#00CC33"));
                            cmdANC4.setTextColor(Color.parseColor("#FFFFFF"));
                        }
                    }


                }

                //Delivery Status
                else if (ElcoVisitStatus.equals("13")) {
                    SecFPMethod.setVisibility(View.GONE);
                    SecPreg.setVisibility(View.GONE);
                    SecDeliv.setVisibility(View.VISIBLE);
                    SecPNC.setVisibility(View.GONE);
                } else if (ElcoVisitStatus.equals("14")) {
                    SecFPMethod.setVisibility(View.GONE);
                    SecPreg.setVisibility(View.GONE);
                    SecDeliv.setVisibility(View.GONE);
                    SecPNC.setVisibility(View.GONE);
                }


                //PNC Visit


            } catch (Exception ex) {

            }
            return convertView;
        }
    }


    private boolean IsUserHA(String provtype) {
        if (provtype.equalsIgnoreCase("5"))
            return true;
        else
            return false;


    }

    public String[] GetANCVisit(String Dist, String Upz, String UN, String Mouza, String Vill, String PType, String PCode, String HH, String SN, String PGN) {
        String SQL = "Select Visit as visit from PregWomenVisit where";
        SQL += " Dist='" + Dist + "' and";
        SQL += " Upz='" + Upz + "' and";
        SQL += " UN='" + UN + "' and";
        SQL += " Mouza='" + Mouza + "' and";
        SQL += " Vill='" + Vill + "' and";
       /* SQL += " ProvType='"+ PType +"' and";
        SQL += " ProvCode='"+ PCode +"' and";*/
        SQL += " HHNo='" + HH + "' and SNo='" + SN + "' ";
        //SQL += " and PGN='"+ PGN +"'";
        SQL += " order by cast(Visit as int) asc";

        Cursor cur = C.ReadData(SQL);
        cur.moveToFirst();
        int RecordCount = 0;
        String[] visitList = new String[cur.getCount()];

        while (!cur.isAfterLast()) {
            visitList[RecordCount] = cur.getString(cur.getColumnIndex("visit"));
            RecordCount += 1;
            cur.moveToNext();
        }
        cur.close();
        return visitList;
    }

    private String ElcoVisitStatus(String Dist, String Upz, String UN, String Mouza, String Vill, String PType, String PCode, String HH, String SN) {
        String VStatus = "";

        String SQL = "Select CurrStatus from ELCOVisit where";
        SQL += " Dist='" + Dist + "' and";
        SQL += " Upz='" + Upz + "' and";
        SQL += " UN='" + UN + "' and";
        SQL += " Mouza='" + Mouza + "' and";
        SQL += " Vill='" + Vill + "' and";
       /* SQL += " ProvType='"+ PType +"' and";
        SQL += " ProvCode='"+ PCode +"' and";*/
        SQL += " HHNo='" + HH + "' and SNo='" + SN + "'";
        SQL += " order by cast(VDate as date) desc, cast(visit as int)desc limit 1";
        VStatus = C.ReturnSingleValue(SQL);
        if (VStatus == null)
            VStatus = "";

        return VStatus;
    }

    //GPS Reading
    //.....................................................................................................
    public void FindLocation() {
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                updateLocation(location);
            }

            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    void updateLocation(Location location) {
        currentLocation = location;
        currentLatitude = currentLocation.getLatitude();
        currentLongitude = currentLocation.getLongitude();
    }

    // Method to turn on GPS
    public void turnGPSOn() {
        try {
            String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

            if (!provider.contains("gps")) { //if gps is disabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                sendBroadcast(poke);
            }
        } catch (Exception e) {

        }
    }

    // Method to turn off the GPS
    public void turnGPSOff() {
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (provider.contains("gps")) { //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }

    // turning off the GPS if its in on state. to avoid the battery drain.
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        turnGPSOff();
    }

}
