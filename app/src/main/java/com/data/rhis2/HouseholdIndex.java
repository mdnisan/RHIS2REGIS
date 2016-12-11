package com.data.rhis2;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import Common.Connection;
import Common.Global;

public class HouseholdIndex extends Activity {
    Connection C;
    Global g;

    ArrayList<HashMap<String, String>> mylist;
    SimpleAdapter mSchedule;
    Bundle IDbundle;

    private static String CurrentVillage;
    private static String CurrentVCode;

    Location currentLocation;
    double currentLatitude, currentLongitude;

    Location currentLocationNet;
    double currentLatitudeNet, currentLongitudeNet;

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mnuclose, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuClose:
                finish();
                Intent f = new Intent(getApplicationContext(), VillageList.class);
                startActivity(f);
                return true;
/*                case R.id.menuNewHH:
                    VisitForm(g.getDistrict(),g.getUpazila(),g.getUnion(),g.getMouza(),g.getVillage(),HouseholdNumber());
                    return true;*/
        }
        return false;
    }


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


    ListView list;
    String StartTime;
    private int mDay;
    private int mMonth;
    private int mYear;
    static final int DATE_DIALOG = 1;
    EditText dtpVDate;
    ImageButton btnVDate;
    String VariableID;
    TextView txtNameSearch;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.householdindex);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();

            //GPS Reading
            FindLocation();

            TextView lblBlock = (TextView) findViewById(R.id.lblBlock);
            TextView lblVillage = (TextView) findViewById(R.id.lblVillageName);
            lblVillage.setText("গ্রামঃ  " + g.getMouza() + "-" + g.getVillage() + ", " + g.getVillageName());

            list = (ListView) findViewById(R.id.listHHIndex);


            list.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                    view.setSelected(true);

                }
            });
            txtNameSearch = ((EditText) findViewById(R.id.txtNameSearch));
            txtNameSearch.setHint("অনুসন্ধান করুণ:  খানা নং/খানা প্রধান/স্ত্রীর/স্বামীর নাম দিয়ে");
            //Added by Angsuman
            ((EditText) findViewById(R.id.txtNameSearch)).addTextChangedListener(new TextWatcher() {

                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                    if (!s.equals("")) {
                        list = (ListView) findViewById(R.id.listHHIndex);

                        /*final ProgressDialog progDailog = ProgressDialog.show(
                                HouseholdIndex.this, "", "অপেক্ষা করুন ...", true);
                        new Thread() {
                            public void run() {
                                // do the long operation on this thread*/
                        mylist = new ArrayList<HashMap<String, String>>();
                        mylist = PrepareData();

                        try {
                            //   runOnUiThread(new Runnable() {
                            //     @Override
                            //   public void run() {

                            SearchHouseholdList(false, ((EditText) findViewById(R.id.txtNameSearch)).getText().toString());
                            //     progDailog.dismiss();
                            //}
                            //});
                        } catch (Exception e) {

                        }
                        //}
                        //}.start();
                    }

                }

                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {

                }

                public void afterTextChanged(Editable s) {

                }
            });


            /*((Button)findViewById(R.id.cmdSearch)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {


                }});*/


            final ProgressDialog progDailog = ProgressDialog.show(
                    HouseholdIndex.this, "", "অপেক্ষা করুন ...", true);
            new Thread() {
                public void run() {

                    mylist = new ArrayList<HashMap<String, String>>();
                    mylist = PrepareDataList();

                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                DisplayHouseholdList(true);
                                progDailog.dismiss();
                            }
                        });
                    } catch (Exception e) {

                    }
                }
            }.start();


        } catch (Exception ex) {
            Connection.MessageBox(HouseholdIndex.this, ex.getMessage());
            return;
        }

    }


    private void searchbyHHHeadName() {


    }

    private void VisitForm(final String Dist, final String Upz, final String Un, final String Mouza, final String Vill, final String HH) {
        final Dialog dialog = new Dialog(HouseholdIndex.this);
        dialog.setTitle("Visit Form");
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.visitform);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        final EditText txtHHNo = (EditText) dialog.findViewById(R.id.txtHHNo);
        txtHHNo.setText(HH);
        dtpVDate = (EditText) dialog.findViewById(R.id.dtpVDate);
        dtpVDate.setText(Global.DateNowDMY());
        btnVDate = (ImageButton) dialog.findViewById(R.id.btnVDate);
        btnVDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariableID = "btnVDate";
                showDialog(DATE_DIALOG);
            }
        });

        RadioGroup rdogrpVisitStatus = (RadioGroup) dialog.findViewById(R.id.rdogrpVisitStatus);
        final RadioButton rdoVisitStatus1 = (RadioButton) dialog.findViewById(R.id.rdoVisitStatus1);
        final RadioButton rdoVisitStatus2 = (RadioButton) dialog.findViewById(R.id.rdoVisitStatus2);

        Button cmdVisitSave = (Button) dialog.findViewById(R.id.cmdVisitSave);

        cmdVisitSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String VD = "";
                VD = Global.DateValidate(dtpVDate.getText().toString());
                if (VD.length() != 0) {
                    Connection.MessageBox(HouseholdIndex.this, VD);
                    return;
                } else if (!rdoVisitStatus1.isChecked() & !rdoVisitStatus2.isChecked()) {
                    Connection.MessageBox(HouseholdIndex.this, "ভিজিটের ফ্লাফ্ল সিলেক্ট করুন।");
                    return;
                }

                String SQL = "";
               /* if(!C.Existence("Select Dist from Visits where Dist='" + Dist + "' and Upz='" + Upz + "' and UN='" + Un + "' and Mouza='" + Mouza + "' and Vill='" + Vill + "' and ProvType='"+ g.getLoginProvType() +"' and ProvCode='"+ g.getLoginProvCode() +"' and HHNo='" + HH + "' and VDate='"+ Global.DateConvertYMD(dtpVDate.getText().toString()) +"'"))*/
                if (!C.Existence("Select Dist from Visits where Dist='" + Dist + "' and Upz='" + Upz + "' and UN='" + Un + "' and Mouza='" + Mouza + "' and Vill='" + Vill + "' and HHNo='" + HH + "' and VDate='" + Global.DateConvertYMD(dtpVDate.getText().toString()) + "'")) {
                    SQL = "Insert into Visits(Dist, Upz, UN, Mouza, Vill, ProvType, ProvCode, HHNo, VDate, VStatus, StartTime, EndTime, Lat, Lon, UserId, EnDt, Upload)Values(";
                    SQL += "'" + Dist + "',";
                    SQL += "'" + Upz + "',";
                    SQL += "'" + Un + "',";
                    SQL += "'" + Mouza + "',";
                    SQL += "'" + Vill + "',";
                    SQL += "'" + g.getLoginProvType() + "',";
                    SQL += "'" + g.getLoginProvCode() + "',";
                    SQL += "'" + HH + "',";
                    SQL += "'" + Global.DateConvertYMD(dtpVDate.getText().toString()) + "',";
                    SQL += "'" + (rdoVisitStatus1.isChecked() ? "1" : (rdoVisitStatus2.isChecked() ? "2" : "")) + "',";
                    SQL += "'" + StartTime + "',";
                    SQL += "'" + g.CurrentTime24() + "',";
                    SQL += "'" + Double.toString(currentLatitude) + "',";
                    SQL += "'" + Double.toString(currentLongitude) + "',";
                    SQL += "'" + g.getUserID() + "',";
                    SQL += "'" + Global.DateTimeNowYMDHMS() + "',";
                    SQL += "'2')";

                    C.Save(SQL);
                } else {
                    SQL = "Update Visits Set Upload='2',";
                    SQL += "VStatus='" + (rdoVisitStatus1.isChecked() ? "1" : (rdoVisitStatus2.isChecked() ? "2" : "")) + "'";
                    /*SQL += " where Dist='" + Dist + "' and Upz='" + Upz + "' and UN='" + Un + "' and Mouza='" + Mouza + "' and Vill='" + Vill + "' and ProvType='"+ g.getLoginProvType() +"' and ProvCode='"+ g.getLoginProvCode() +"' and HHNo='" + HH + "' and VDate='"+ Global.DateConvertYMD(dtpVDate.getText().toString()) +"'";*/
                    SQL += " where Dist='" + Dist + "' and Upz='" + Upz + "' and UN='" + Un + "' and Mouza='" + Mouza + "' and Vill='" + Vill + "' and HHNo='" + HH + "' and VDate='" + Global.DateConvertYMD(dtpVDate.getText().toString()) + "'";

                    C.Save(SQL);
                }

                /*SQL  = "Insert into Visits(Div, Dist, Upz, UN, Mouza, Vill, ProvType, ProvCode, HHNo, VDate, VStatus, StartTime, EndTime, Lat, Lon, UserId, EnDt, Upload)Values(";
                SQL += "'"+ Div +"',";
                SQL += "'"+ Dist +"',";
                SQL += "'"+ Upz +"',";
                SQL += "'"+ Un +"',";
                SQL += "'"+ Mouza +"',";
                SQL += "'"+ Vill +"',";
                SQL += "'"+ g.getProvType() +"',";
                SQL += "'"+ g.getProvCode() +"',";
                SQL += "'"+ HH +"',";
                SQL += "'"+ Global.DateConvertYMD(dtpVDate.getText().toString()) +"'," ;
                SQL += "'"+ (rdoVisitStatus1.isChecked()?"1":(rdoVisitStatus2.isChecked()?"2":"")) +"',";
                SQL += "'"+ StartTime +"',";
                SQL += "'"+ g.CurrentTime24() +"',";
                SQL += "'"+ Double.toString(currentLatitude) +"',";
                SQL += "'"+ Double.toString(currentLongitude) +"',";
                SQL += "'"+ g.getUserID() +"',";
                SQL += "'"+ Global.DateTimeNowYMDHMS() +"',";
                SQL += "'2')";*/

                //C.Save(SQL);


                if (rdoVisitStatus1.isChecked()) {
                    finish();
                    g.setCallFrom("newh");
                    g.setProvType(g.getLoginProvType());
                    g.setProvCode(g.getLoginProvCode());
                    g.setHouseholdNo("");
                    Intent f1 = new Intent(getApplicationContext(), MemberListFPI.class);
                    startActivity(f1);
                } else if (rdoVisitStatus2.isChecked()) {
                    /*if (!C.Existence("Select Dist,Upz,UN,Mouza,Vill,HHNo from Household  Where Dist='" + Dist + "' and Upz='" + Upz + "' and UN='" + Un + "' and Mouza='" + Mouza + "' and Vill='" + Vill + "' and ProvType='"+ g.getLoginProvType() +"' and ProvCode='"+ g.getLoginProvCode() +"' and HHNo='" + HH + "'")) {*/
                    if (!C.Existence("Select Dist,Upz,UN,Mouza,Vill,HHNo from Household  Where Dist='" + Dist + "' and Upz='" + Upz + "' and UN='" + Un + "' and Mouza='" + Mouza + "' and Vill='" + Vill + "' and HHNo='" + HH + "'")) {
                        SQL = "Insert into Household(Dist,Upz,UN,Mouza,Vill,ProvType,ProvCode,HHNo,Lat,Lon,StartTime,EndTime,UserId,EnDt,Upload)Values('" + Dist + "','" + Upz + "','" + Un + "','" + Mouza + "','" + Vill + "','" + g.getLoginProvType() + "','" + g.getLoginProvCode() + "','" + HH + "','" + Double.toString(currentLatitude) + "','" + Double.toString(currentLongitude) + "','" + g.CurrentTime24() + "','" + g.CurrentTime24() + "','" + g.getUserID() + "','" + Global.DateTimeNowYMDHMS() + "','2')";
                        C.Save(SQL);
                    }

                    SQL = "Update Household Set Upload='2',";
                    SQL += "PAddr = '',";
                    SQL += "PermaAddress = '',";
                    SQL += "Religion = '',";
                    SQL += "VGFCard = ''";

                   /* SQL += "  Where Dist='" + Dist + "' and Upz='" + Upz + "' and UN='" + Un + "' and Mouza='" + Mouza + "' and Vill='" + Vill + "' and ProvType='"+ g.getLoginProvType() +"' and ProvCode='"+ g.getLoginProvCode() +"' and HHNo='" + HH + "'";*/
                    SQL += "  Where Dist='" + Dist + "' and Upz='" + Upz + "' and UN='" + Un + "' and Mouza='" + Mouza + "' and Vill='" + Vill + "' and HHNo='" + HH + "'";
                    C.Save(SQL);

                    finish();
                    g.setHouseholdNo("");
                    Intent f1 = new Intent(getApplicationContext(), HouseholdIndex.class);
                    startActivity(f1);
                }
            }
        });
        Button cmdVisitClose = (Button) dialog.findViewById(R.id.cmdVisitClose);
        cmdVisitClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private String HouseholdNumber() {
        String SQL = "";

        SQL = "Select (ifnull(max(cast(HHNo as int)),10000)+1)MaxHH from Household where";
        /*SQL += " dist='"+ g.getDistrict() +"' and upz='"+ g.getUpazila() +"' and un='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and vill='"+ g.getVillage() +"' and ProvType='"+ g.getLoginProvType() +"' and ProvCode='"+ g.getLoginProvCode() +"'";*/
        SQL += " dist='" + g.getDistrict() + "' and upz='" + g.getUpazila() + "' and un='" + g.getUnion() + "' and Mouza='" + g.getMouza() + "' and vill='" + g.getVillage() + "'";

        String HHNo = C.ReturnSingleValue(SQL);

        return HHNo;
    }

    private ArrayList<HashMap<String, String>> PrepareDataList() {
        final ListView list = (ListView) findViewById(R.id.listHHIndex);
        mylist = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;

        String SQL = "Select h.HHNo as hhno, ifnull(h.HHHead,'') as hhhead,ifnull(h.TotalMem,'') as totalmem,h.provtype as provtype, h.provcode as provcode, ifnull(h.Elco,'') as elco,( CASE WHEN hp.HHNo  IS NOT NULL THEN 'Y' ELSE '' END ) AS HouseholdFPIid,( CASE WHEN bv.HHNo  IS NOT NULL THEN 'Y' ELSE '' END ) AS EPIBARIVisitId from Household h LEFT Outer JOIN HouseholdFPI hp on  h.dist =hp.dist AND h.upz =hp.upz AND h.un = hp.un AND h.mouza = hp.mouza AND h.vill =  hp.vill AND h.HHNo =  hp.HHNo";
        SQL += " LEFT Outer JOIN EPIBARIVisit bv on  h.dist =bv.dist AND h.upz =bv.upz AND h.un = bv.un AND h.mouza = bv.mouza AND h.vill =  bv.vill AND h.HHNo =  bv.HHNo";
        SQL += " Where h.dist='" + g.getDistrict() + "' and h.upz='" + g.getUpazila() + "' and h.un='" + g.getUnion() + "' and h.mouza='" + g.getMouza() + "' and h.vill='" + g.getVillage() + "'";
        SQL += " GROUP BY h.HHNo HAVING  count(h.HHNo) order by cast(h.HHNo as int) asc";

        Cursor cur = C.ReadData(SQL);

        cur.moveToFirst();


        while (!cur.isAfterLast()) {
            map = new HashMap<String, String>();

            map.put("hhno", "খানা নম্বর   : " + cur.getString(cur.getColumnIndex("hhno")));
            map.put("hhhead", "খানা প্রধান : " + cur.getString(cur.getColumnIndex("hhhead")));
            map.put("totalmem", "মোট সদস্য : " + cur.getString(cur.getColumnIndex("totalmem")));
            map.put("elco", "দম্পতি         : " + cur.getString(cur.getColumnIndex("elco")));
            map.put("HouseholdFPIid",cur.getString(cur.getColumnIndex("HouseholdFPIid")));
            map.put("EPIBARIVisitId",cur.getString(cur.getColumnIndex("EPIBARIVisitId")));

               /*     map.put("hhno",cur.getString(cur.getColumnIndex("hhno")));
                    map.put("hhhead", cur.getString(cur.getColumnIndex("hhhead")));
                    map.put("totalmem", cur.getString(cur.getColumnIndex("totalmem")));
                    map.put("elco", cur.getString(cur.getColumnIndex("Elco")));*/
                   /* map.put("provtype", cur.getString(3));
                    map.put("provcode", cur.getString(4));*/

            map.put("provtype", g.getProvType());
            map.put("provcode", g.getProvCode());
            mylist.add(map);
            cur.moveToNext();
        }
        cur.close();
        return mylist;
    }

    public void DisplayHouseholdList(boolean heading) {
        if (heading == true) {
            View header = getLayoutInflater().inflate(R.layout.householdindexheading, null);
            list.addHeaderView(header);
        }
        mSchedule = new SimpleAdapter(this, mylist, R.layout.householdindexrow,
                new String[]{"hhno", "hhhead", "totalmem", "elco"},
                new int[]{R.id.HHNo, R.id.HHHead, R.id.txtTotalMem, R.id.elconame});

        list.setAdapter(new HHListAdapter(this));
    }

    private ArrayList<HashMap<String, String>> PrepareData() {
        String SQL = "Select h.HHNo as hhno, ifnull(h.HHHead,'') as hhhead,ifnull(h.TotalMem,'') as totalmem,h.provtype as provtype, h.provcode as provcode, h.Elco AS elco,( CASE WHEN hp.HHNo  IS NOT NULL THEN 'Y' ELSE '' END ) AS HouseholdFPIid,( CASE WHEN bv.HHNo  IS NOT NULL THEN 'Y' ELSE '' END ) AS EPIBARIVisitId from Household h LEFT Outer JOIN HouseholdFPI hp on  h.dist =hp.dist AND h.upz =hp.upz AND h.un = hp.un AND h.mouza = hp.mouza AND h.vill =  hp.vill AND h.HHNo =  hp.HHNo";
        SQL += " LEFT Outer JOIN EPIBARIVisit bv on  h.dist =bv.dist AND h.upz =bv.upz AND h.un = bv.un AND h.mouza = bv.mouza AND h.vill =  bv.vill AND h.HHNo =  bv.HHNo";
        SQL += " Where (h.HHHead like '%" + ((EditText) findViewById(R.id.txtNameSearch)).getText().toString().trim() + "%' OR " +
                "h.Elco like '%" + ((EditText) findViewById(R.id.txtNameSearch)).getText().toString().trim() + "%'  OR " +
                "h.Elco like '%(" + ((EditText) findViewById(R.id.txtNameSearch)).getText().toString().trim() + "%')" +
                " and  h.dist='" + g.getDistrict() + "' and h.upz='" + g.getUpazila() + "' and h.un='" + g.getUnion() + "' and h.mouza='" +
                g.getMouza() + "' and h.vill='" + g.getVillage() + "'";
        SQL += " GROUP BY h.HHNo HAVING  count(h.HHNo) order by cast(h.HHNo as int) asc";

        Cursor cur = C.ReadData(SQL);
        cur.moveToFirst();
        HashMap<String, String> map;
        while (!cur.isAfterLast()) {
            map = new HashMap<String, String>();
            map.put("hhno", "খানা নম্বর   : " + cur.getString(cur.getColumnIndex("hhno")));
            map.put("hhhead", "খানা প্রধান : " + cur.getString(cur.getColumnIndex("hhhead")));
            map.put("totalmem", "মোট সদস্য : " + cur.getString(cur.getColumnIndex("totalmem")));
            map.put("elco", "দম্পতি         : " + cur.getString(cur.getColumnIndex("elco")));
            map.put("HouseholdFPIid",cur.getString(cur.getColumnIndex("HouseholdFPIid")));
            map.put("EPIBARIVisitId",cur.getString(cur.getColumnIndex("EPIBARIVisitId")));
            map.put("provtype", g.getProvType());
            map.put("provcode", g.getProvCode());
            mylist.add(map);
            cur.moveToNext();
        }
        cur.close();

        return mylist;
    }

    public void SearchHouseholdList(boolean heading, String headName) {



        /*String SQL = "Select h.HHNo as hhno, ifnull(h.HHHead,'') as hhhead,ifnull(h.TotalMem,'') as totalmem,h.provtype as provtype, h.provcode as provcode, h.Elco AS elco from Household h";
        SQL += " Where h.HHHead like '" + ((EditText)findViewById(R.id.txtNameSearch)).getText().toString().trim() +"%' OR "+
               "h.Elco like 'দম্পতি: " + ((EditText)findViewById(R.id.txtNameSearch)).getText().toString().trim() +"%'  OR "+
                "h.Elco like '(" + ((EditText)findViewById(R.id.txtNameSearch)).getText().toString().trim() +"%'"+
        " and  h.dist='"+ g.getDistrict() +"' and h.upz='"+ g.getUpazila() +"' and h.un='"+ g.getUnion() +"' and h.mouza='"+ g.getMouza() +"' and h.vill='"+ g.getVillage() +"'";
        SQL += " GROUP BY h.HHNo order by cast(h.HHNo as int) asc";*/

        // Cursor cur=C.ReadData(SQL);
        //cur=C.ReadData(SQL);

        if (heading == true) {
            View header = getLayoutInflater().inflate(R.layout.householdindexheading, null);
            list.addHeaderView(header);
        }

       /* while(!cur.isAfterLast())
        {
            map = new HashMap<String, String>();
            map.put("hhno", "খানা নম্বর: " + cur.getString(cur.getColumnIndex("hhno")));
            map.put("hhhead", "খানা প্রধানের নাম : "+cur.getString(cur.getColumnIndex("hhhead")));
            map.put("totalmem", "মোট সদস্য : "+ cur.getString(cur.getColumnIndex("totalmem")));
            map.put("elco", cur.getString(cur.getColumnIndex("elco")));
            map.put("provtype", g.getProvType());
            map.put("provcode", g.getProvCode());
            mylist.add(map);
            cur.moveToNext();
        }
        cur.close();*/
        mSchedule = new SimpleAdapter(this, mylist, R.layout.householdindexrow,
                new String[]{"hhno", "hhhead", "totalmem", "elco"},
                new int[]{R.id.HHNo, R.id.HHHead, R.id.txtTotalMem, R.id.elconame});

        list.setAdapter(new HHListAdapter(this));
    }

    public class HHListAdapter extends BaseAdapter {
        private Context context;

        public HHListAdapter(Context c) {
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
                convertView = inflater.inflate(R.layout.householdindexrow, null);
            }

            TextView HHNo = (TextView) convertView.findViewById(R.id.HHNo);
            TextView HHHead = (TextView) convertView.findViewById(R.id.HHHead);
            TextView TotalMem = (TextView) convertView.findViewById(R.id.TotalMem);
            TextView elconame = (TextView) convertView.findViewById(R.id.elconame);

            final HashMap<String, String> o = (HashMap<String, String>) mSchedule.getItem(position);


            HHNo.setText(o.get("hhno"));
            HHHead.setText(o.get("hhhead"));
            TotalMem.setText(o.get("totalmem"));
            elconame.setText(o.get("elco").replace("null", "").replace("()", ""));
            //Absent Household
            if (o.get("totalmem").equals("0")) {
                HHNo.setTextColor(Color.parseColor("#FF0000"));
            } else {
                HHNo.setTextColor(Color.parseColor("#000000"));
            }


            LinearLayout hhrow;
            //Alternate row color
            if (position % 2 == 0) {
                hhrow = (LinearLayout) convertView.findViewById(R.id.secHHIndex);
                hhrow.setBackgroundColor(Color.parseColor("#F3F3F3"));
            } else {
                hhrow = (LinearLayout) convertView.findViewById(R.id.secHHIndex);
                hhrow.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }

            if (o.get("HouseholdFPIid").equals("Y")) {


                hhrow.setBackgroundColor(Color.parseColor("#99cc33"));
            }
            if (o.get("HouseholdFPIid").equals("")) {
                hhrow.setBackgroundColor(Color.parseColor("#FFFFFF"));
                // memtabrow.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }


            if (o.get("EPIBARIVisitId").equals("Y")) {


                hhrow.setBackgroundColor(Color.parseColor("#99cc33"));
            }
            if (o.get("EPIBARIVisitId").equals("")) {
                hhrow.setBackgroundColor(Color.parseColor("#FFFFFF"));
                // memtabrow.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }

            RelativeLayout rowlay = (RelativeLayout) convertView.findViewById(R.id.rowlay);
            rowlay.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {


                    final ProgressDialog progDailog = ProgressDialog.show(
                            HouseholdIndex.this, "", "অপেক্ষা করুন ...", true);
                    new Thread() {
                        public void run() {

                            try {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        g.setProvType(o.get("provtype"));
                                        g.setProvCode(o.get("provcode"));
                                        g.setHouseholdNo(o.get("hhno").replace("খানা নম্বর   : ", ""));
                                        g.setHouseholdHName(o.get("hhhead").replace("খানা প্রধান : ", ""));
                                        g.setCallFrom("oldh");
                                        if (C.ReturnSingleValue("Select fCall from FormCall").equals("1")) {
                                            finish();
                                            Intent f2 = new Intent(getApplicationContext(), EPIBARIVisit.class);
                                            startActivity(f2);


                                        } else if (C.ReturnSingleValue("Select fCall from FormCall").equals("2")) {
                                            finish();
                                            Intent f1 = new Intent(getApplicationContext(), MemberListFPI.class);
                                            startActivity(f1);
                                        } else {
                                            finish();
                                            Intent f1 = new Intent(getApplicationContext(), MemberListFPI.class);
                                            startActivity(f1);
                                        }

                                        progDailog.dismiss();
                                    }
                                });
                            } catch (Exception e) {

                            }
                        }
                    }.start();


                }
            });


            return convertView;
        }
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

    //Location from network provider	            
    public void FindLocationNet() {
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                updateLocationNet(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    void updateLocationNet(Location location1) {
        currentLocationNet = location1;
        currentLatitudeNet = currentLocationNet.getLatitude();
        currentLongitudeNet = currentLocationNet.getLongitude();
    }


    protected Dialog onCreateDialog(int id) {
        final Calendar c = Calendar.getInstance();
        Integer Y = g.mYear;
        Integer M = g.mMonth;
        Integer D = g.mDay;
        if (dtpVDate.getText().length() > 0) {
            Y = Integer.valueOf(Global.Right(dtpVDate.getText().toString(), 4));
            M = Integer.valueOf(dtpVDate.getText().toString().substring(4, 5));
            D = Integer.valueOf(Global.Left(dtpVDate.getText().toString(), 2));
        }
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mDateSetListener, Y, M - 1, D);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear + 1;
            mDay = dayOfMonth;
            dtpVDate.setText(new StringBuilder()
                    .append(Global.Right("00" + mDay, 2)).append("/")
                    .append(Global.Right("00" + mMonth, 2)).append("/")
                    .append(mYear));

            //txtAge.setText(Global.DateDifferenceYears( Global.DateNowDMY(), dtpDate.getText().toString() ));
        }
    };
}
