package com.data.rhis2;

import java.lang.reflect.Member;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import Common.Connection;
import Common.Global;
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
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
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

public class MemberList extends Activity{

    SimpleAdapter mSchedule;
    SimpleAdapter eList;

    ArrayList<HashMap<String, String>> mylist   = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> evmylist = new ArrayList<HashMap<String, String>>();
    Connection C;
    Global g;
    private static final int UPDATEDONE = 900;
    private ProgressDialog pDialog;
    private static String vill;
    private static String bari;
    private static String hhno;
    private static String hhhead;
    private static String totalmember;
    private String ErrMsg;
    private static String vdate;

    TextView txtHHNo;
    RadioGroup rdogrpPAddr;
    RadioButton rdoPAddr1;
    RadioButton rdoPAddr2;
    EditText txtPermaAddress;
    Spinner spnReligion;

    RadioGroup rdogrpVGFCard;
    RadioButton rdoVGFCard1;
    RadioButton rdoVGFCard2;

    LinearLayout secPAddr;
    LinearLayout secPermaAddress;
    LinearLayout secReligion;
    String StartTime;

    TextView FPMethod;
    TextView LMP;
    TextView EDD;

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menumember, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder adb = new AlertDialog.Builder(MemberList.this);
        switch (item.getItemId()) {
            case R.id.menuClose:
                adb.setTitle("Close");
                adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান[হাঁ/না]?");
                adb.setNegativeButton("না", null);
                adb.setPositiveButton("হাঁ", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //05 Jun 2015
                        //Update total member and household head name
                        if (IDbundle != null) {
                            if (!IDbundle.getString("search").equalsIgnoreCase("search")) {


                                String SQL = "";
                                SQL = "Update Household set HHHead=(select nameeng from Member where rth='01' and length(extype)=0 and";
                                SQL += " Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and ProvType='" + g.getProvType() + "' and ProvCode='" + g.getProvCode() + "' and HHNo='" + txtHHNo.getText().toString() + "')";
                                SQL += " where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and ProvType='" + g.getProvType() + "' and ProvCode='" + g.getProvCode() + "' and HHNo='" + txtHHNo.getText().toString() + "'";
                                C.Save(SQL);

                                SQL = " Update Household set totalmem=(select count(*)totalmem from Member where length(extype)=0 and";
                                SQL += " Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and ProvType='" + g.getProvType() + "' and ProvCode='" + g.getProvCode() + "' and HHNo='" + txtHHNo.getText().toString() + "')";
                                SQL += " where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and ProvType='" + g.getProvType() + "' and ProvCode='" + g.getProvCode() + "' and HHNo='" + txtHHNo.getText().toString() + "'";
                                C.Save(SQL);
                            }
                        }
                        finish();
                        /*Intent f1 = new Intent(getApplicationContext(),HouseholdIndex.class);
                        startActivity(f1);*/
                    }});
                adb.show();
                return true;
            case R.id.menuNewMember:
                String DV="";
                try {
                    if (!rdoPAddr1.isChecked() & !rdoPAddr2.isChecked() & secPAddr.isShown()) {
                        Connection.MessageBox(MemberList.this, "এটা কি আপনার স্থায়ী ঠিকানা কিনা এ তথ্য সিলেক্ট করতে হবে");
                        rdoPAddr1.requestFocus();
                        return false;
                    } else if (txtPermaAddress.getText().toString().length() == 0 & secPermaAddress.isShown()) {
                        Connection.MessageBox(MemberList.this, "সঠিক স্থায়ী ঠিকানা লিপিবদ্ধ করুন।");
                        txtPermaAddress.requestFocus();
                        return false;
                    } else if (spnReligion.getSelectedItemPosition() == 0 & secReligion.isShown()) {
                        Connection.MessageBox(MemberList.this, "তালিকা থেকে সঠিক ধর্ম  সিলেক্ট করুন.");
                        spnReligion.requestFocus();
                        return false;
                    } else if (!rdoVGFCard1.isChecked() & !rdoVGFCard2.isChecked()) {
                        Connection.MessageBox(MemberList.this, "ভি জি এফ কার্ড আছে কি, এ তথ্য সিলেক্ট করতে হবে");
                        rdoVGFCard1.requestFocus();
                        return false;
                    }

                    String SQL = "";

                    if (!C.Existence("Select Dist,Upz,UN,Mouza,Vill,HHNo from Household  Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"' and HHNo='" + txtHHNo.getText().toString() + "'")) {
                        SQL = "Insert into Household(Dist,Upz,UN,Mouza,Vill,ProvType,ProvCode,HHNo,Lat,Lon,StartTime,EndTime,UserId,EnDt,Upload)Values('" + g.getDistrict() + "','" + g.getUpazila() + "','" + g.getUnion() + "','" + g.getMouza() + "','" + g.getVillage() + "','"+ g.getProvType() +"','"+ g.getProvCode() +"','" + txtHHNo.getText() + "','" + Double.toString(currentLatitude) + "','" + Double.toString(currentLongitude) + "','" + StartTime + "','" + g.CurrentTime24() + "','" + g.getUserID() + "','" + Global.DateTimeNowYMDHMS() + "','2')";
                        C.Save(SQL);
                    }

                    SQL = "Update Household Set Upload='2',";
                    SQL += "wardNew = '" + g.getWardNew() + "',";
                    SQL += "wardOld = '" + g.getWardOld() + "',";
                    SQL += "FWAUnit = '" + g.getFWAUnit() + "',";
                    SQL += "EPIBlock = '" + g.getEPIBlock() + "',";
                    SQL += "PAddr = '" + (rdoPAddr1.isChecked() ? "1" : "2") + "',";
                    SQL += "PermaAddress = '" + txtPermaAddress.getText().toString() + "',";
                    SQL += "Religion = '" + (spnReligion.getSelectedItemPosition() == 0 ? "" : Global.Left(spnReligion.getSelectedItem().toString(), 1)) + "',";
                    SQL += "VGFCard = '" + (rdoVGFCard1.isChecked() ? "1" : "2") + "'";


                    SQL += "  Where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and  ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"' and HHNo='" + txtHHNo.getText().toString() + "'";
                    C.Save(SQL);

                    g.setHouseholdNo(txtHHNo.getText().toString());

                    finish();
                    g.setSerialNo("");
                    g.setCallFrom("m");
                    Intent f1 = new Intent(getApplicationContext(), MemberForm.class);
                    startActivity(f1);
                }
                catch (Exception ex)
                {
                    Connection.MessageBox(MemberList.this,ex.getMessage());
                    return false;
                }
                return true;
        }
        return false;
    }

    private String PregnancyStatus(String dist,String upz, String un, String mouza,String vill,String hhno,String sno)
    {
        String SQ = "";
        SQ = "select dolmp,doedd,";
        SQ += " (case when cast(Visit as int)=1 then 1 else 0 end)anc1,";
        SQ += " (case when cast(Visit as int)=2 then 1 else 0 end)anc2,";
        SQ += " (case when cast(Visit as int)=3 then 1 else 0 end)anc3,";
        SQ += " (case when cast(Visit as int)=4 then 1 else 0 end)anc4";
        SQ += " from";
        SQ += " (select dist,upz,un,mouza,vill,hhno,sno, CurrStatus from ElcoVisit where";
        SQ += " dist||upz||un||mouza||vill||hhno||sno = '"+ (dist+upz+un+mouza+vill+hhno+sno) +"'";
        SQ += " order by date(vdate) desc limit 1";
        SQ += " )e left outer join PregWomen p";
        SQ += " on e.dist||e.upz||e.un||e.mouza||e.vill||e.hhno||e.sno=p.dist||p.upz||p.un||p.mouza||p.vill||p.hhno||p.sno";
        SQ += " left outer join anc a";
        SQ += " on p.dist||p.upz||p.un||p.mouza||p.vill||p.hhno||p.sno=a.dist||a.upz||a.un||a.mouza||a.vill||a.hhno||a.sno";
        String CurrentStatus = (C.ReturnSingleValue(SQ));

        return CurrentStatus;
    }
    private String PregWomenCurrentStatus(String SNo)
    {
        String SQL = "";
        SQL = "Select CurrStatus";
        SQL += " from ELCOVisit Where Dist='"+ g.getDistrict() +"' and upz='"+ g.getUpazila() +"' and un='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and vill='"+ g.getVillage() +"' and HHNo='"+ g.getHouseholdNo() +"' and SNo='"+ SNo +"' and visit=(select max(visit) from ELCOVisit ";
        SQL += " Where Dist='"+ g.getDistrict() +"' and upz='"+ g.getUpazila() +"' and un='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and vill='"+ g.getVillage() +"' and HHNo='"+ g.getHouseholdNo() +"' and SNo='"+ SNo +"')";

        String CurrentStatus = (C.ReturnSingleValue(SQL));

        return CurrentStatus;
    }

    private String LMPDate(String SNo)
    {
        String SQL = "";
        SQL = "Select DOLMP  from PregWomen";
        SQL += " where";
        SQL += " dist='"+ g.getDistrict() +"' and upz='"+ g.getUpazila() +"' and un='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and vill='"+ g.getVillage() +"' and HHNo='"+ g.getHouseholdNo() +"' and SNo='"+ SNo +"'";

        String DateOfLMP = (C.ReturnSingleValue(SQL));

        return DateOfLMP;
    }

    private String EDDDate(String SNo)
    {
        String SQL = "";
        SQL = "Select DOEDD  from PregWomen";
        SQL += " where";
        SQL += " dist='"+ g.getDistrict() +"' and upz='"+ g.getUpazila() +"' and un='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and vill='"+ g.getVillage() +"' and HHNo='"+ g.getHouseholdNo() +"' and SNo='"+ SNo +"'";

        String DateOfEDD = (C.ReturnSingleValue(SQL));

        return DateOfEDD;
    }

    Location currentLocation;
    double currentLatitude,currentLongitude;
    Bundle IDbundle = new Bundle();
    Context con;


    //Disabled Back/Home key
    //--------------------------------------------------------------------------------------------------
    @Override
    public boolean onKeyDown(int iKeyCode, KeyEvent event)
    {
        if(iKeyCode == KeyEvent.KEYCODE_BACK || iKeyCode == KeyEvent.KEYCODE_HOME)
        { return false; }
        else { return true;  }
    }

    //***************************************************************************************************************************
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.memberlist);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();
            con = this;

            IDbundle = getIntent().getExtras();

            if (IDbundle != null) {
                if (IDbundle.getString("search").equalsIgnoreCase("search")) {
                    DisplaySearch(true);

                } else {
                    DisplaySearch(false);
                    LoadDataMemberList();
                }
            }
            else {
                DisplaySearch(false);
                LoadDataMemberList();
            }
        } catch (Exception ex) {
            Connection.MessageBox(MemberList.this, ex.getMessage());
            return;
        }
    }

    private void LoadDataMemberList()
    {
        try {

            FindLocation();

            //StartTime = g.CurrentTime24();


            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Activity.KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
            lock.disableKeyguard();

            TextView lblVillName = (TextView) findViewById(R.id.lblVillageName);
            lblVillName.setText("গ্রামঃ " + g.getMouza() + "-" + g.getVillage() + ", " + g.getVillageName());

            txtHHNo = (TextView) findViewById(R.id.txtHHNo);
            if (g.getHouseholdNo().length() == 0)
                txtHHNo.setText(HouseholdNumber());
            else
                txtHHNo.setText(g.getHouseholdNo());

            rdogrpPAddr = (RadioGroup) findViewById(R.id.rdogrpPAddr);
            rdoPAddr1 = (RadioButton) findViewById(R.id.rdoPAddr1);
            rdoPAddr2 = (RadioButton) findViewById(R.id.rdoPAddr2);

            txtPermaAddress = (EditText) findViewById(R.id.txtPermaAddress);
            spnReligion = (Spinner) findViewById(R.id.spnReligion);
            List<String> listRel = new ArrayList<String>();

            listRel.add("");
            listRel.add("1-ইসলাম");
            listRel.add("2-হিন্দু");
            listRel.add("3-বৌদ্ধ");
            listRel.add("4-খ্রীস্টান");
            listRel.add("8-Refuse to disclose");
            listRel.add("9-Not a believer");
            listRel.add("0-অন্যান্য");
            ArrayAdapter<String> adptrRel = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listRel);
            spnReligion.setAdapter(adptrRel);

            rdogrpVGFCard = (RadioGroup) findViewById(R.id.rdogrpVGFCard);
            rdoVGFCard1 = (RadioButton) findViewById(R.id.rdoVGFCard1);
            rdoVGFCard2 = (RadioButton) findViewById(R.id.rdoVGFCard2);

            secPAddr = (LinearLayout) findViewById(R.id.secPAddr);
            secPermaAddress = (LinearLayout) findViewById(R.id.secPermaAddress);
            secReligion = (LinearLayout) findViewById(R.id.secReligion);
            rdogrpPAddr.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int radioButtonID) {
                    if (rdoPAddr2.isChecked()) {
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
            DataRetrieve("", true, "active");


            Button cmdVGDCard = (Button)findViewById(R.id.cmdVGDCard);
            cmdVGDCard.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    try {
                        VGDCardForm();
                    }
                    catch(Exception ex)
                    {
                        Connection.MessageBox(MemberList.this,ex.getMessage());
                        return;
                    }
                }
            });



        }
        catch(Exception ex)
        {
            Connection.MessageBox(MemberList.this,ex.getMessage());
            return;
        }
    }
    private void DisplaySearch(boolean willdisplaysearch) {
        ((Spinner)findViewById(R.id.txtVillSearch))
                .setAdapter( C.getArrayAdapter( "Select '  ' union Select VillageId||'-'||VillageName from Village" ) );
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

        ((Button) findViewById(R.id.cmdSearch)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {


                try {

                    Search();

                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                } catch (Exception ex) {
                    Connection.MessageBox(MemberList.this, ex.getMessage());
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
                Connection.MessageBox(MemberList.this, ex.getMessage());
                return;
            }*/
            }
        });
        if (willdisplaysearch) {
            ((LinearLayout) findViewById(R.id.seclblSearch)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.seclblSearchGroup)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.seclblHH)).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.sec)).setVisibility(View.GONE);

        } else {
            ((LinearLayout) findViewById(R.id.seclblSearch)).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.seclblSearchGroup)).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.seclblHH)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.sec)).setVisibility(View.VISIBLE);
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
    };

    private void VGDCardForm()
    {
        final Dialog dialog = new Dialog(MemberList.this);
        dialog.setTitle("ভি জি ডি কার্ড(ছবি)");
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.vgdcard);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

    }


    private String HouseholdNumber()
    {
        String SQL = "";
        /*SQL = "Select (ifnull(max(cast(HHNo as int)),10000)+1)MaxHH from Household";
        SQL += " where";
        SQL += " dist='"+ g.getDistrict() +"' and upz='"+ g.getUpazila() +"' and un='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and vill='"+ g.getVillage() +"'";
        */
        SQL = "Select (ifnull(max(cast(HHNo as int)),10000)+1)MaxHH from Household where";
        SQL += " dist='"+ g.getDistrict() +"' and upz='"+ g.getUpazila() +"' and un='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and vill='"+ g.getVillage() +"' and ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"'";

        String HHNo = C.ReturnSingleValue(SQL);

        return HHNo;
    }

    /*
    private void HHDataSave()
    {
        try
        {
            String DV="";

            if(!rdoPAddr1.isChecked() & !rdoPAddr2.isChecked() & secPAddr.isShown())
            {
                Connection.MessageBox(MemberList.this, "এটা কি আপনার স্থায়ী ঠিকানা কিনা এ তথ্য সিলেক্ট করতে হবে");
                rdoPAddr1.requestFocus();
                return;
            }
            else if(txtPermaAddress.getText().toString().length()==0 & secPermaAddress.isShown())
            {
                Connection.MessageBox(MemberList.this, "সঠিক স্থায়ী ঠিকানা লিপিবদ্ধ করুন।");
                txtPermaAddress.requestFocus();
                return;
            }
            else if(spnReligion.getSelectedItemPosition()==0  & secReligion.isShown())
            {
                Connection.MessageBox(MemberList.this, "তালিকা থেকে সঠিক ধর্ম  সিলেক্ট করুন.");
                spnReligion.requestFocus();
                return;
            }
            else if(!rdoVGFCard1.isChecked() & !rdoVGFCard2.isChecked())
            {
                Connection.MessageBox(MemberList.this, "ভি জি এফ কার্ড আছে কি, এ তথ্য সিলেক্ট করতে হবে");
                rdoVGFCard1.requestFocus();
                return;
            }

            String SQL = "";

            if(!C.Existence("Select Dist,Upz,UN,Mouza,Vill,HHNo from Household  Where Dist='"+ g.getDistrict() +"' and Upz='"+ g.getUpazila() +"' and UN='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and Vill='"+ g.getVillage() +"' and HHNo='"+ txtHHNo.getText().toString() +"'"))
            {
                SQL = "Insert into Household(Dist,Upz,UN,Mouza,Vill,HHNo,StartTime,EndTime,UserId,EnDt,Upload)Values('"+ g.getDistrict() +"','"+ g.getUpazila() +"','"+ g.getUnion() +"','"+ g.getMouza() +"','"+ g.getVillage() +"','"+ txtHHNo.getText() +"','"+ StartTime +"','"+ g.CurrentTime24() +"','"+ g.getUserID() +"','"+ Global.DateTimeNowYMDHMS() +"','2')";
                C.Save(SQL);
            }

            SQL = "Update Household Set ";
            SQL+="wardNew = '"+ g.getWardNew() +"',";
            SQL+="wardOld = '"+ g.getWardOld() +"',";
            SQL+="FWAUnit = '"+ g.getFWAUnit() +"',";
            SQL+="EPIBlock = '"+ g.getEPIBlock() +"',";
            SQL+="PAddr = '"+ (rdoPAddr1.isChecked()?"1":"2") +"',";
            SQL+="PermaAddress = '"+ txtPermaAddress.getText().toString() +"',";
            SQL+="Religion = '"+ (spnReligion.getSelectedItemPosition()==0?"":Global.Left(spnReligion.getSelectedItem().toString(),1)) +"',";
            SQL+="VGFCard = '"+ (rdoVGFCard1.isChecked()?"1":"2") +"'";


            SQL+="  Where Dist='"+ g.getDistrict() +"' and Upz='"+ g.getUpazila() +"' and UN='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and Vill='"+ g.getVillage() +"' and HHNo='"+ txtHHNo.getText().toString() +"'";
            C.Save(SQL);

            g.setHouseholdNo(txtHHNo.getText().toString());
        }
        catch(Exception  e)
        {
            Connection.MessageBox(MemberList.this, e.getMessage());
            return;
        }
    }

    */
    private void HHDataSearch(String Dist, String Upz, String UN, String Mouza, String Vill, String HHNo)
    {
        try
        {
            RadioButton rb;
            Cursor cur = C.ReadData("Select * from Household  Where Dist='"+ Dist +"' and Upz='"+ Upz +"' and UN='"+ UN +"' and Mouza='"+ Mouza +"' and Vill='"+ Vill +"' and ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"' and HHNo='"+ HHNo +"'");
            cur.moveToFirst();
            while(!cur.isAfterLast())
            {
                if(cur.getString(cur.getColumnIndex("PAddr")).equals("1"))
                    rdoPAddr1.setChecked( true );
                else if(cur.getString(cur.getColumnIndex("PAddr")).equals("2"))
                    rdoPAddr2.setChecked( true );

                txtPermaAddress.setText(cur.getString(cur.getColumnIndex("PermaAddress")));
                spnReligion.setSelection(Global.SpinnerItemPosition(spnReligion, 1 ,cur.getString(cur.getColumnIndex("Religion"))));
                if(cur.getString(cur.getColumnIndex("VGFCard")).equals( "1" ))
                    rdoVGFCard1.setChecked( true );
                else if(cur.getString(cur.getColumnIndex("VGFCard")).equals( "2" ))
                    rdoVGFCard2.setChecked( true );



                cur.moveToNext();
            }
            cur.close();
        }
        catch(Exception  e)
        {
            Connection.MessageBox(MemberList.this, e.getMessage());
            return;
        }
    }



    private void EventForm(final String ProvType, final String ProvCode, String HH, final String SNo, final String MemName, final String Age, final String MS, final String Sex, final String HealthId)
    {
        try {
            final Dialog dialog = new Dialog(MemberList.this);
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
            final Button PregWomentReg = (Button)dialog.findViewById(R.id.cmd5);

            final Button cmdEligibleCouple = (Button) dialog.findViewById(R.id.cmdEligibleCouple);
            final Button cmdAge0to1 = (Button)dialog.findViewById(R.id.cmd1);
            final Button cmdAge0to5 = (Button)dialog.findViewById(R.id.cmd2);
            final Button cmdAdolescent = (Button)dialog.findViewById(R.id.cmd3);
            final Button cmdInjectable = (Button)dialog.findViewById(R.id.cmd4);
            final Button cmdPreg = (Button)dialog.findViewById(R.id.cmd5);
            final Button DeathForm = (Button)dialog.findViewById(R.id.cmd6);

            if(MS.equals("2") & Sex.equals("1"))//couple
            {
                cmdEligibleCouple.setEnabled(false);
                cmdAge0to1.setEnabled(false);
                cmdAge0to5.setEnabled(false);
                cmdAdolescent.setEnabled(false);
                cmdInjectable.setEnabled(false);
                cmdPreg.setEnabled(false);
            }
            else if(MS.equals("2") & Sex.equals("2"))//couple
            {
                cmdEligibleCouple.setEnabled(true);
                cmdAge0to1.setEnabled(false);
                cmdAge0to5.setEnabled(false);
                cmdAdolescent.setEnabled(false);
                cmdInjectable.setEnabled(false);
                cmdPreg.setEnabled(false);
            }
            else if(Integer.valueOf(Age) <= 1)
            {
                cmdEligibleCouple.setEnabled(false);
                cmdAge0to1.setEnabled(true);
                cmdAge0to5.setEnabled(false);
                cmdAdolescent.setEnabled(false);
                cmdInjectable.setEnabled(false);
                cmdPreg.setEnabled(false);
            }
            else if(Integer.valueOf(Age) < 5)
            {
                cmdEligibleCouple.setEnabled(false);
                cmdAge0to1.setEnabled(true);
                cmdAge0to5.setEnabled(true);
                cmdAdolescent.setEnabled(false);
                cmdInjectable.setEnabled(false);
                cmdPreg.setEnabled(false);
            }
            else if(Integer.valueOf(Age) >= 5)
            {
                cmdEligibleCouple.setEnabled(false);
                cmdAge0to1.setEnabled(false);
                cmdAge0to5.setEnabled(false);
                cmdAdolescent.setEnabled(true);
                cmdInjectable.setEnabled(false);
                cmdPreg.setEnabled(false);
            }


            secEdit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(MemberList.this);
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
                    AlertDialog.Builder adb = new AlertDialog.Builder(MemberList.this);
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
                            C.Save("delete from Member where Dist='" + g.getDistrict() + "' and Upz='" + g.getUpazila() + "' and UN='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and Vill='" + g.getVillage() + "' and ProvType='"+ ProvType +"' and ProvCode='"+ ProvCode +"' and HHNo='" + g.getHouseholdNo() + "' and SNo='" + SNo + "'");
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

                                Intent f1 = new Intent(getApplicationContext(), ELCOForm.class);
                                startActivity(f1);




                }
            });

            PregWomentReg.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    g.setSerialNo(SNo);
                    g.setHealthID(HealthId);
                    dialog.dismiss();
                    finish();
                    //Intent f1 = new Intent(getApplicationContext(),PregReg.class);
                    //startActivity(f1);
                }});


            DeathForm.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    g.setSerialNo(SNo);
                    g.setHealthID(HealthId);
                    dialog.dismiss();
                    finish();
                    Intent f1 = new Intent(getApplicationContext(),DeathForm.class);
                    startActivity(f1);
                }});

            dialog.show();
        }
        catch (Exception ex)
        {
            Connection.MessageBox(MemberList.this,ex.getMessage());
            return;
        }
    }


    //Search
    public void Search() {




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
            if (((EditText) findViewById(R.id.txtMobileNoSearch)).getText().toString().length() > 0) {
                if (wheresql.length() > 0) {
                    wheresql += " AND ";
                }
                wheresql += " MobileNo1 like '%" + ((EditText) findViewById(R.id.txtMobileNoSearch)).getText().toString() + "%'";

                wheresql += " OR MobileNo2 like '%" + ((EditText) findViewById(R.id.txtMobileNoSearch)).getText().toString() + "%'";
            }

            if (((EditText) findViewById(R.id.txtNIDSearch)).getText().toString().length() > 0) {
                if (wheresql.length() > 0) {
                    wheresql += " AND ";
                }
                wheresql += " NID like'%" + ((EditText) findViewById(R.id.txtNIDSearch)).getText().toString() + "%'";
            }
            if (((EditText) findViewById(R.id.txtBRIDSearch)).getText().toString().length() > 0) {
                if (wheresql.length() > 0) {
                    wheresql += " AND ";
                }
                wheresql += " BRID '%" + ((EditText) findViewById(R.id.txtBRIDSearch)).getText().toString() + "%'";
            }
            if (((EditText) findViewById(R.id.txtElcoNoSearch)).getText().toString().length() > 0) {
                if (wheresql.length() > 0) {
                    wheresql += " AND ";
                }
                wheresql += " ELCONo '%" + ((EditText) findViewById(R.id.txtElcoNoSearch)).getText().toString() + "%'";
            }

            String vil = "";
            vil = ((Spinner) findViewById(R.id.txtVillSearch)).getSelectedItemPosition()==0?"":Global.Left(((Spinner) findViewById(R.id.txtVillSearch)).getSelectedItem().toString(),1);
            if (vil.length() > 0) {
                if (wheresql.length() > 0) {
                    wheresql += " AND ";
                }
                wheresql += " Vill like '%" + vil + "%'";
            }
            if (((EditText) findViewById(R.id.txtNameSearch)).getText().toString().length() > 0) {
                if (wheresql.length() > 0) {
                    wheresql += " AND ";
                }
                wheresql += " NameEng  like '%" + ((EditText) findViewById(R.id.txtNameSearch)).getText().toString() + "%'";
            }
            if (((EditText) findViewById(R.id.txtFatherMotherSearch)).getText().toString().length() > 0) {
                if (wheresql.length() > 0) {
                    wheresql += " AND ";
                }
                wheresql += " Father  like '%" + ((EditText) findViewById(R.id.txtFatherMotherSearch)).getText().toString() + "%'";
                wheresql += " OR Father like '%" + ((EditText) findViewById(R.id.txtFatherMotherSearch)).getText().toString() + "%'";
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

            ListView list = (ListView) findViewById(R.id.lstMember);

            //CustomCursorAdapter customAdapter = new CustomCursorAdapter(MemberList.this, cur1);

            //   if(heading ==true)
            // {
            // View header = getLayoutInflater().inflate(R.layout.memberlistheading, null);
            //list.addHeaderView(header);
            // }

            int i = 0;

            while (!cur1.isAfterLast()) {
                HashMap<String, String> map = new HashMap<String, String>();

                if (i == 0) {
                    View header = getLayoutInflater().inflate(R.layout.memberlistheading, null);
                    list.addHeaderView(header);
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
                mSchedule = new SimpleAdapter(MemberList.this, mylist, R.layout.memberlistrow, new String[]{"sno"},
                        new int[]{R.id.SNo});

               /* pDialog = ProgressDialog.show(con, "Wait","অপেক্ষা করুন ...");
                new Thread() {

                    public void run() {
                        try {
                            // finish();
                            Looper.prepare();
                            mSchedule.notifyDataSetChanged();
                            mSchedule = new SimpleAdapter(MemberList.this, mylist, R.layout.memberlistrow, new String[]{"sno"},
                                    new int[]{R.id.SNo});

                            mSchedule.notifyDataSetChanged();
                            Message msg = new Message();
                            msg.what = UPDATEDONE;
                            pDialogHandler.sendMessage(msg);


                        } catch (Exception e) {

                        }

                        Looper.loop();
                    }
                }.start();*/

                list.setAdapter(new MemberListAdapter(con));

                i += 1;
                cur1.moveToNext();
            }
            cur1.close();
            ((TextView)findViewById(R.id.Vlblcount)).setText("মোট "+ String.valueOf(i)+ " টি ডাটা পাওয়া গিয়েছে" );

        } catch (Exception e) {
            Connection.MessageBox(MemberList.this, e.getMessage());
        }

    }

    //
    //Retrieve member list
    //***************************************************************************************************************************
    public void DataRetrieve(String HH, Boolean heading, String ActiveOrAll)
    {
        try
        {
            String SQLStr = "";
            SQLStr = "Select Dist, Upz, UN, Mouza, Vill, ProvType as provtype,ProvCode as provcode, HHNo, SNo as SNo, ifnull(HealthID,'') as HealthID, ifnull(NameEng,'') as NameEng,";
            SQLStr += " ifnull(NameBang,'') as NameBang, ifnull(Rth,'') as Rth, ifnull(HaveNID,'') as HaveNID, ifnull(NID,'') as NID, ifnull(NIDStatus,'') as NIDStatus, ifnull(HaveBR,'') as HaveBR, ifnull(BRID,'') as BRID, ifnull(BRIDStatus,'') as BRIDStatus, ifnull(MobileNo1,'') as MobileNo1,";
            SQLStr += " ifnull(MobileNo2,'') as MobileNo2,ifnull(MobileYN,'')as MobileYN, ifnull(DOB,'') as DOB, Cast(((julianday(date('now'))-julianday(DOB))/365.25) as int) as Age,Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) as AgeM, ifnull(DOBSource,'') as DOBSource, ifnull(BPlace,'') as BPlace, ifnull(FNo,'') as FNo, ifnull(Father,'') as Father, ifnull(MNo,'') as MNo, ifnull(Mother,'') as Mother,";
            SQLStr += " ifnull(Sex,'') as Sex, ifnull(MS,'') as MS, ifnull(SPNO1,'') as SPNO1,ifnull(SPNO2,'') as SPNO2,ifnull(SPNO3,'') as SPNO3,ifnull(SPNO4,'') as SPNO4, ifnull(ELCONo,'') as ELCONo, ifnull(ELCODontKnow,'') as ELCODontKnow, ifnull(EDU,'') as EDU, ifnull(Rel,'') as Rel, ifnull(Nationality,'') as Nationality, ifnull(OCP,'') as OCP,ifnull(ExType,'')as ExType,ProvType as provtype, ProvCode as provcode";
            SQLStr += " from Member Where dist='"+ g.getDistrict() +"' and upz='"+ g.getUpazila() +"' and un='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and vill='"+ g.getVillage() +"' and ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"' and HHNo='"+ g.getHouseholdNo() +"'";

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


            Cursor cur1 = C.ReadData(SQLStr);

            cur1.moveToFirst();
            mylist.clear();

            ListView list = (ListView) findViewById(R.id.lstMember);
            if(heading ==true)
            {
                View header = getLayoutInflater().inflate(R.layout.memberlistheading, null);
                list.addHeaderView(header);
            }

            int i=0;
            while(!cur1.isAfterLast())
            {
                HashMap<String, String> map = new HashMap<String, String>();

                if(i==0)
                {
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

                /*map.put("lmp", cur1.getString(cur1.getColumnIndex("lmp")));
                map.put("edd", cur1.getString(cur1.getColumnIndex("edd")));
                map.put("anc1", cur1.getString(cur1.getColumnIndex("anc1")));
                map.put("anc2", cur1.getString(cur1.getColumnIndex("anc2")));
                map.put("anc3", cur1.getString(cur1.getColumnIndex("anc3")));
                map.put("anc4", cur1.getString(cur1.getColumnIndex("anc4")));*/

                mylist.add(map);
                mSchedule = new SimpleAdapter(MemberList.this, mylist, R.layout.memberlistrow,new String[] {"sno"},
                        new int[] {R.id.SNo});
                list.setAdapter(new MemberListAdapter(this));

                i+=1;
                cur1.moveToNext();
            }
            cur1.close();


        }
        catch(Exception  e)
        {
            Connection.MessageBox(MemberList.this, e.getMessage());
        }
    }


    public class MemberListAdapter extends BaseAdapter
    {
        private Context context;

        public MemberListAdapter(Context c){
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
                if(o.get("mobileyn").equals("1"))
                    mobileyn.setText("নাই");
                else
                    mobileyn.setText("");

                dob.setText(Global.DateConvertDMY(o.get("dob")));
                age.setText(o.get("age"));
                dobsource.setText(o.get("dobsource").equals("1") ? "প্রকৃত" : "আনুমানিক");
                bplace.setText(o.get("bplace"));
                fno.setText(o.get("fno"));
                father.setText(o.get("father"));
                mno.setText(o.get("mno"));
                mother.setText(o.get("mother"));

                sex.setText(o.get("sex").equals("1") ? "পুরুষ" : (o.get("sex").equals("2") ? "মহিলা" : "হিজরা"));

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
                if (o.get("extype").toString().length() != 0) {
                    nameeng.setTextColor(Color.parseColor("#FF0000"));
                    //memtab.setBackgroundColor(Color.parseColor("#FF0000"));
                }
                else
                {
                    if (position % 2 == 0) {
                        memtab.setBackgroundColor(Color.parseColor("#F3F3F3"));
                    } else {
                        memtab.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    }
                }



                memtab.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if(o.get("extype").toString().length()!=0)
                        {
                            if(o.get("extype").toString().equals("55"))
                                Connection.MessageBox(MemberList.this,"সদস্য মারা গিয়েছে।");
                            else
                                Connection.MessageBox(MemberList.this,"সদস্য এই খানায় সক্রিয় নয়।");

                            return;
                        }
                        else {
                            EventForm(o.get("provtype").toString(), o.get("provcode").toString(), o.get("hhno").toString(), o.get("sno").toString(), o.get("nameeng").toString(),o.get("age").toString(),o.get("ms").toString(),o.get("sex"),o.get("healthid"));
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
                }
                else if (o.get("sex").equals("1") & agemonth <=59) {
                    memberImage.setBackgroundResource(R.drawable.male0to5);
                }
                else if(o.get("sex").equals("1"))
                {
                    memberImage.setBackgroundResource(R.drawable.male);
                }
                //Female member
                else if (o.get("sex").equals("2") & agemonth < 12) {
                    memberImage.setBackgroundResource(R.drawable.female0to1);
                }
                else if (o.get("sex").equals("2") & agemonth <= 59) {
                    memberImage.setBackgroundResource(R.drawable.female0to5);
                }
                else if (o.get("sex").equals("2")) {
                    memberImage.setBackgroundResource(R.drawable.female);
                }


                final LinearLayout SecFPMethod=(LinearLayout)convertView.findViewById(R.id.SecFPMethod);
                final LinearLayout SecPreg=(LinearLayout)convertView.findViewById(R.id.SecPreg);
                final LinearLayout SecDeliv=(LinearLayout)convertView.findViewById(R.id.SecDeliv);
                final LinearLayout SecPNC=(LinearLayout)convertView.findViewById(R.id.SecPNC);

                //Default : hide all section
                SecFPMethod.setVisibility(View.GONE);
                SecPreg.setVisibility(View.GONE);
                SecDeliv.setVisibility(View.GONE);
                SecPNC.setVisibility(View.GONE);

                //Status of ELCO Visit
                String ElcoVisitStatus = ElcoVisitStatus(o.get("dist"), o.get("upz"),o.get("un"), o.get("mouza"), o.get("vill"),o.get("provtype"),o.get("provcode"), o.get("hhno"),o.get("sno"));

                //No Status for Male Member
                if (o.get("sex").equals("1") | !o.get("ms").equals("2"))
                {
                    SecFPMethod.setVisibility(View.GONE);
                    SecPreg.setVisibility(View.GONE);
                    SecDeliv.setVisibility(View.GONE);
                    SecPNC.setVisibility(View.GONE);
                }

                //Method Used
                else if(!ElcoVisitStatus.equals("12") & !ElcoVisitStatus.equals("13") & !ElcoVisitStatus.equals("14"))
                {
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
                else if(ElcoVisitStatus.equals("12"))
                {
                    //need to highlight if pregnancy is high risk
                    memberImage.setBackgroundResource(R.drawable.preg);
                    SecFPMethod.setVisibility(View.GONE);
                    SecPreg.setVisibility(View.VISIBLE);
                    SecDeliv.setVisibility(View.GONE);
                    SecPNC.setVisibility(View.GONE);
                    String SQ = "";
                    SQ = "select PGN||'^'||DOLMP||'^'||date(DOLMP,'+280 day') from PregWomen where";
                    SQ += " Dist='"+ o.get("dist") +"' and";
                    SQ += " Upz='"+ o.get("upz") +"' and";
                    SQ += " UN='"+ o.get("un") +"' and";
                    SQ += " Vill='"+ o.get("vill") +"' and";
                    SQ += " ProvType='"+ o.get("provtype") +"' and";
                    SQ += " ProvCode='"+ o.get("provcode") +"' and";
                    SQ += " HHNo='"+ o.get("hhno") +"' and SNo='"+ o.get("sno") +"'";
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
                    for(int a=0; a<ANCVisit.length; a++)
                    {
                        if(a==0) {
                            cmdANC1.setBackgroundColor(Color.parseColor("#00CC33"));
                            cmdANC1.setTextColor(Color.parseColor("#FFFFFF"));
                        }
                        else if(a==1) {
                            cmdANC2.setBackgroundColor(Color.parseColor("#00CC33"));
                            cmdANC2.setTextColor(Color.parseColor("#FFFFFF"));
                        }
                        else if(a==2) {
                            cmdANC3.setBackgroundColor(Color.parseColor("#00CC33"));
                            cmdANC3.setTextColor(Color.parseColor("#FFFFFF"));
                        }
                        else if(a==3) {
                            cmdANC4.setBackgroundColor(Color.parseColor("#00CC33"));
                            cmdANC4.setTextColor(Color.parseColor("#FFFFFF"));
                        }
                    }


                }

                //Delivery Status
                else if(ElcoVisitStatus.equals("13"))
                {
                    SecFPMethod.setVisibility(View.GONE);
                    SecPreg.setVisibility(View.GONE);
                    SecDeliv.setVisibility(View.VISIBLE);
                    SecPNC.setVisibility(View.GONE);
                }
                else if(ElcoVisitStatus.equals("14"))
                {
                    SecFPMethod.setVisibility(View.GONE);
                    SecPreg.setVisibility(View.GONE);
                    SecDeliv.setVisibility(View.GONE);
                    SecPNC.setVisibility(View.GONE);
                }


                //PNC Visit


                /*
                String PregCurrentStatus=PregWomenCurrentStatus(o.get("sno"));
                if(PregCurrentStatus.equals("11"))
                {
                    if (o.get("sex").equals("1"))
                    {
                        SecFPMethod.setVisibility(View.GONE);

                        SecPreg.setVisibility(View.GONE);
                        SecDeliv.setVisibility(View.GONE);
                        SecPNC.setVisibility(View.GONE);
                    }
                    //Female member
                    else if (o.get("sex").equals("2"))
                    {
                        memberImage.setBackgroundResource(R.drawable.preg);
                        SecFPMethod.setVisibility(View.GONE);
                        SecPreg.setVisibility(View.VISIBLE);
                        SecDeliv.setVisibility(View.GONE); //if still pregnant
                        SecPNC.setVisibility(View.GONE);
                    }

                }
                else if(PregCurrentStatus.equals("01")|PregCurrentStatus.equals("02")|PregCurrentStatus.equals("03")|PregCurrentStatus.equals("04")|PregCurrentStatus.equals("05")|PregCurrentStatus.equals("06")|PregCurrentStatus.equals("07")|PregCurrentStatus.equals("08")|PregCurrentStatus.equals("09")|PregCurrentStatus.equals("10"))
                {
                    if (o.get("sex").equals("1")) {
                        SecFPMethod.setVisibility(View.GONE);

                        SecPreg.setVisibility(View.GONE);
                        SecDeliv.setVisibility(View.GONE);
                        SecPNC.setVisibility(View.GONE);
                    }
                    //Female member
                    else if (o.get("sex").equals("2"))
                    {
                        SecFPMethod.setVisibility(View.VISIBLE);

                        SecPreg.setVisibility(View.GONE);
                        SecDeliv.setVisibility(View.GONE);
                        SecPNC.setVisibility(View.GONE);
                    }
                }
                else
                {
                    SecFPMethod.setVisibility(View.GONE);

                    SecPreg.setVisibility(View.GONE);
                    SecDeliv.setVisibility(View.GONE);
                    SecPNC.setVisibility(View.GONE);
                }
                */
            }
            catch (Exception ex)
            {

            }
            return convertView;
        }
    }




    public String[] GetANCVisit(String Dist, String Upz, String UN, String Mouza, String Vill, String PType, String PCode, String HH, String SN, String PGN)
    {
        String SQL = "Select Visit as visit from PregWomenVisit where";
        SQL += " Dist='"+ Dist +"' and";
        SQL += " Upz='"+ Upz +"' and";
        SQL += " UN='"+ UN +"' and";
        SQL += " Mouza='"+ Mouza +"' and";
        SQL += " Vill='"+ Vill +"' and";
        SQL += " ProvType='"+ PType +"' and";
        SQL += " ProvCode='"+ PCode +"' and";
        SQL += " HHNo='"+ HH +"' and SNo='"+ SN +"' ";
        //SQL += " and PGN='"+ PGN +"'";
        SQL += " order by cast(Visit as int) asc";

        Cursor cur = C.ReadData(SQL);
        cur.moveToFirst();
        int RecordCount=0;
        String[] visitList = new String[cur.getCount()];

        while(!cur.isAfterLast())
        {
            visitList[RecordCount] = cur.getString(cur.getColumnIndex("visit"));
            RecordCount+=1;
            cur.moveToNext();
        }
        cur.close();
        return visitList;
    }

    private String ElcoVisitStatus(String Dist, String Upz, String UN, String Mouza, String Vill, String PType, String PCode, String HH, String SN)
    {
        String VStatus = "";

        String SQL = "Select CurrStatus from ELCOVisit where";
        SQL += " Dist='"+ Dist +"' and";
        SQL += " Upz='"+ Upz +"' and";
        SQL += " UN='"+ UN +"' and";
        SQL += " Mouza='"+ Mouza +"' and";
        SQL += " Vill='"+ Vill +"' and";
        SQL += " ProvType='"+ PType +"' and";
        SQL += " ProvCode='"+ PCode +"' and";
        SQL += " HHNo='"+ HH +"' and SNo='"+ SN +"'";
        SQL += " order by cast(VDate as date) desc, cast(visit as int)desc limit 1";
        VStatus = C.ReturnSingleValue(SQL);
        if(VStatus==null)
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
        currentLocation  = location;
        currentLatitude  = currentLocation.getLatitude();
        currentLongitude = currentLocation.getLongitude();
    }

    // Method to turn on GPS
    public void turnGPSOn(){
        try
        {
            String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

            if(!provider.contains("gps")){ //if gps is disabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                sendBroadcast(poke);
            }
        }
        catch (Exception e) {

        }
    }

    // Method to turn off the GPS
    public void turnGPSOff(){
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(provider.contains("gps")){ //if gps is enabled
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
