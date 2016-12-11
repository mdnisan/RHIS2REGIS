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
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
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
 * Created by Nisan on 9/1/2015.
 */
public class Death extends Activity {
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
        AlertDialog.Builder adb = new AlertDialog.Builder(Death.this);
        switch (item.getItemId()) {
            case R.id.menuClose:
                adb.setTitle("Close");
                adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান[Yes/No]?");
                adb.setNegativeButton("না", null);
                adb.setPositiveButton("হাঁ", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        //Intent f2 = new Intent(getApplicationContext(),MemberList.class);
                        // startActivity(f2);
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

    LinearLayout secDeathDT;
    EditText dtpFromDT;
    ImageButton btnFromDT;
    EditText dtpToDT;
    ImageButton btnToDT;

    SimpleAdapter dataAdapter;
    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    private String TableNameELCOVisit;


    RadioButton rdoS77d1;
    RadioButton rdoS77d2;

    String StartTime;
    String TDeath;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.death);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();

            dtpFromDT = (EditText) findViewById(R.id.dtpFromDT);
            dtpFromDT.setText(Global.DateNowDMY());
            btnFromDT = (ImageButton) findViewById(R.id.btnFromDT);

            dtpToDT = (EditText) findViewById(R.id.dtpToDT);
            dtpToDT.setText(Global.DateNowDMY());
            btnToDT = (ImageButton) findViewById(R.id.btnToDT);
            final ListView list = (ListView) findViewById(R.id.lstData);
            View header = getLayoutInflater().inflate(R.layout.deathheading, null);
            list.addHeaderView(header);
            btnFromDT.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btnFromDT";
                    showDialog(DATE_DIALOG);
                }
            });
            btnToDT.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btnToDT";
                    showDialog(DATE_DIALOG);
                }
            });
            // TDeath=C.ReturnSingleValue("Select Count(*) as Tdeath from death d where d.providerId='"+ g.getProvCode() +"' and strftime('%d/%m/%Y', date(d.deathDT)) between'"+dtpFromDT.getText().toString()+"' and '"+dtpToDT.getText().toString()+"'");
            final TextView lblTCount = (TextView) findViewById(R.id.lblTCount);
            DataSearch();
            //DataSearch(dtpFromDT.getText().toString(),dtpToDT.getText().toString());
            lblTCount.setText(":" + String.valueOf(totalCount));
            DisplaySearch(true);
            // lblTCount.setText( GetTotalDeath(dtpFromDT.getText().toString(),dtpToDT.getText().toString()));
            Button cmdRefresh = (Button) findViewById(R.id.cmdRefresh);
            cmdRefresh.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    totalCount = 0;
                    DataSearch(dtpFromDT.getText().toString(), dtpToDT.getText().toString());
                    lblTCount.setText(":" + String.valueOf(totalCount));
                    // lblTCount.setText( GetTotalDeath(dtpFromDT.getText().toString(),dtpToDT.getText().toString()));
                    //  TDeath=C.ReturnSingleValue("Select Count(*) as Tdeath from death d where d.providerId='"+ g.getProvCode() +"' and strftime('%d/%m/%Y', date(d.deathDT)) between'"+dtpFromDT.getText().toString()+"' and '"+dtpToDT.getText().toString()+"'");
                    //lblTCount.setText(TDeath);

                }
            });


        } catch (Exception e) {
            Connection.MessageBox(Death.this, e.getMessage());
            return;
        }
    }

    private void DisplayAdvanceSearchPopup() {
        final Dialog popupView = new Dialog(Death.this);
        popupView.setTitle("Advace Search");
        popupView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popupView.setContentView(R.layout.searchpopup);
        popupView.setCancelable(true);
        popupView.setCanceledOnTouchOutside(true);


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


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ((ImageButton) findViewById(R.id.btnMinus)).setVisibility(View.GONE);
        ((ImageButton) findViewById(R.id.btnPlus)).setVisibility(View.VISIBLE);
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
                    //  Search(displayheader);
                    //  list.addHeaderView(header);

                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                } catch (Exception ex) {
                    Connection.MessageBox(Death.this, ex.getMessage());
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
    }

    int totalCount = 0;

    private void DataSearch() {
        try {
            String SQL = "";


            SQL = "Select ifnull(cM.healthId,'') as healthId,ifnull(cM.generatedId,'') as generatedId,Cast(((julianday(date(d.deathDT))-julianday(cM.dob))/365.25) as int) AS Agey,Cast(strftime('%m-%d',d.deathDT) - strftime('%m-%d',cM.dob) As int) AS Agem,Cast(strftime('%d',d.deathDT) - strftime('%d',cM.dob)As int) AS Aged, ";
            SQL += " '' as Elcono,ifnull(d.DeathDT,'') as DeathDT,ifnull(d.systemEntryDate,'') as EntryDate, ifnull(d.deathOfPregWomen,'') as deathOfPregWomen, ifnull(d.causeOfDeath,'') as causeOfDeath,";
            SQL += " ifnull(cM.houseGrHoldingNo,'') as HHNo,ifnull(v.VILLAGENAMEENG,'') as VName, ";
            SQL += " ifnull(cM.name,'') as NameEng,ifnull(cM.fatherName,'') as Father,";
            SQL += " ifnull(cM.motherName,'') as Mother,ifnull(cM.husbandName,'') as HusName,";
            SQL += " ifnull(cM.gender,'') as Sex,ifnull(cM.Age,'') as Age from  clientMap cM inner join death d on cM.generatedId=d.healthId  left join Village v on v.ZILLAID=cM.ZILLAID and v.UPAZILAID=cM.UPAZILAID and v.UNIONID=cM.UNIONID ";
            SQL += " and v.MOUZAID=cM.MOUZAID and v.VILLAGEID=cM.VILLAGEID where d.providerId='" + g.getProvCode() + "'";
            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();
            dataList.clear();
            int slno = 0;
            while (!cur.isAfterLast()) {
                slno += 1;
                HashMap<String, String> map = new HashMap<String, String>();
                ListView list = (ListView) findViewById(R.id.lstData);
                map.put("slno", Integer.toString(slno));
                // map.put("tcount", cur.getString(cur.getColumnIndex("TCount")));
                // map.put("slno", cur.getString(cur.getColumnIndex("SLNO")));
                map.put("elcono", cur.getString(cur.getColumnIndex("Elcono")));
                map.put("entrydate", Global.DateConvertDMY(cur.getString(cur.getColumnIndex("EntryDate"))));
                map.put("hhho", cur.getString(cur.getColumnIndex("HHNo")));
                map.put("vname", cur.getString(cur.getColumnIndex("VName")));
                map.put("name", cur.getString(cur.getColumnIndex("NameEng")));
                map.put("fname", cur.getString(cur.getColumnIndex("Father")));
                map.put("mname", cur.getString(cur.getColumnIndex("Mother")));
                map.put("hname", cur.getString(cur.getColumnIndex("HusName")));
                map.put("sex", cur.getString(cur.getColumnIndex("Sex")));
                map.put("deathdt", Global.DateConvertDMY(cur.getString(cur.getColumnIndex("DeathDT"))));

                map.put("agey", cur.getString(cur.getColumnIndex("Agey")));
                map.put("agem", cur.getString(cur.getColumnIndex("Agem")));
                map.put("aged", cur.getString(cur.getColumnIndex("Aged")));
                map.put("wdeath", cur.getString(cur.getColumnIndex("deathOfPregWomen")));
                map.put("cdeath", cur.getString(cur.getColumnIndex("causeOfDeath")));

                if (cur.getString(cur.getColumnIndex("deathOfPregWomen")).equalsIgnoreCase("1")) {
                    map.put("wdeath", "হ্যাঁ");
                } else if (cur.getString(cur.getColumnIndex("deathOfPregWomen")).equalsIgnoreCase("2")) {
                    map.put("wdeath", "");
                }
                if (cur.getString(cur.getColumnIndex("causeOfDeath")).equalsIgnoreCase("1")) {
                    map.put("cdeath", "ষ্ট্রোক");
                } else if (cur.getString(cur.getColumnIndex("causeOfDeath")).equalsIgnoreCase("2")) {
                    map.put("cdeath", "হার্ট এ্যাটাক");
                } else if (cur.getString(cur.getColumnIndex("causeOfDeath")).equalsIgnoreCase("3")) {
                    map.put("cdeath", "ক্যান্স");
                } else if (cur.getString(cur.getColumnIndex("causeOfDeath")).equalsIgnoreCase("4")) {
                    map.put("cdeath", "পানিতে ডুবে মৃত্যু   ");
                } else if (cur.getString(cur.getColumnIndex("causeOfDeath")).equalsIgnoreCase("5")) {
                    map.put("cdeath", "বিষ খেয়ে মৃত্যু  ");
                } else if (cur.getString(cur.getColumnIndex("causeOfDeath")).equalsIgnoreCase("6")) {
                    map.put("cdeath", "বজ্রপাতে মৃত্যু  ");
                } else if (cur.getString(cur.getColumnIndex("causeOfDeath")).equalsIgnoreCase("7")) {
                    map.put("cdeath", "সড়ক দুর্ঘটনায় মৃত্যু ");
                } else if (cur.getString(cur.getColumnIndex("causeOfDeath")).equalsIgnoreCase("8")) {
                    map.put("cdeath", "বিদু্্যতস্পৃষ্ট মৃত্যু");
                } else if (cur.getString(cur.getColumnIndex("causeOfDeath")).equalsIgnoreCase("9")) {
                    map.put("cdeath", "আত্ম্যহত্যা");
                } else if (cur.getString(cur.getColumnIndex("causeOfDeath")).equalsIgnoreCase("10")) {
                    map.put("cdeath", "ডায়রিয়া");
                } else if (cur.getString(cur.getColumnIndex("causeOfDeath")).equalsIgnoreCase("11")) {
                    map.put("cdeath", "নিউমোনিয়া");
                } else if (cur.getString(cur.getColumnIndex("causeOfDeath")).equalsIgnoreCase("12")) {
                    map.put("cdeath", "আগুনে পুড়ে মারা যাওয়া");
                } else if (cur.getString(cur.getColumnIndex("causeOfDeath")).equalsIgnoreCase("13")) {
                    map.put("cdeath", "বার্ধক্যজনিত মৃত্যু");
                } else if (cur.getString(cur.getColumnIndex("causeOfDeath")).equalsIgnoreCase("14")) {
                    map.put("cdeath", "অন্যান্য");
                }

                if (cur.getString(cur.getColumnIndex("Sex")).equalsIgnoreCase("1")) {
                    map.put("sex", "পুরুষ");
                } else if (cur.getString(cur.getColumnIndex("Sex")).equalsIgnoreCase("2")) {
                    map.put("sex", "মহিলা");
                }

                dataList.add(map);

                dataAdapter = new SimpleAdapter(Death.this, dataList, R.layout.deathrow, new String[]{"SNo", "EntryDate"},
                        new int[]{R.id.Slno, R.id.EntryDate});

                list.setAdapter(new DataListAdapter(this));

                cur.moveToNext();
                totalCount += 1;
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(Death.this, e.getMessage());
            return;
        }
    }

    private void DataSearch(String stdt, String enddt) {
        try {
            String SQL = "";

          /*  SQL  = "Select SNo as SNo,m.HHNo AS HHNo,ifnull(e.elcoNo,'') as Elcono,substr(d.systemEntryDate,9,2)||'/'||substr(d.systemEntryDate,6,2)||'/'||substr(d.systemEntryDate,1,4) AS EntryDate,ifnull(NameEng,'') as NameEng,ifnull(NameBang,'') as NameBang,substr(d.deathDT,9,2)||'/'||substr(d.deathDT,6,2)||'/'||substr(d.deathDT,1,4) AS DeathDT,Cast(((julianday(date(d.deathDT))-julianday(m.DOB))/365.25) as int) AS Agey,Cast(strftime('%m-%d',d.deathDT) - strftime('%m-%d',m.DOB) As int) AS Agem,Cast(strftime('%d',d.deathDT) - strftime('%d',m.DOB)As int) AS Aged,ifnull(FNo,'') as FNo,";
            SQL += " CASE WHEN cast(FNo as int) = 55 THEN ifnull(Father,'') WHEN cast(FNo as int) = 77 THEN ifnull(Father,'') WHEN cast(FNo as int) = 88 THEN ifnull(Father,'') ELSE (select  NameEng  from member where  ProvCode=(select  ProvCode  from member  Where  healthid ='" + g.getHealthID() + "')";
            SQL += " and HHNo=(select  HHNo  from member  Where  healthid =d.healthId)";
            SQL += " and SNo=(select  FNo  from member  Where  healthid=d.healthId)) END AS  Father,";
            SQL += " ifnull(MNo,'') as MNo,";
            SQL += " CASE WHEN cast(MNo as int) = 55 THEN ifnull(Mother,'') WHEN cast(MNo as int) = 77 THEN ifnull(Mother,'') WHEN cast(MNo as int) = 88 THEN ifnull(Mother,'') ELSE ";
            SQL += " (select  NameEng  from member where  ProvCode=(select  ProvCode  from member  Where  healthid =d.healthId)";
            SQL += " and HHNo=(select  HHNo  from member  Where  healthid=d.healthId)";
            SQL += " and SNo=(select  MNo  from member  Where  healthid=d.healthId)) END AS  Mother,";
            SQL += " ifnull(Sex,'') as Sex, ifnull(MS,'') as MS,ifnull(SPNO1,'') as SPNO1,ifnull(SPNO2,'') as SPNO2,v.VILLAGENAMEENG AS VName,ifnull(d.causeOfDeath,'') AS Cdeath,ifnull(d.deathOfPregWomen,'') AS WDeath,";
            SQL += " CASE WHEN cast(SPNO1 as int) = 55 THEN (select  NameEng  from member where  ProvCode=(select  ProvCode  from member Where healthid=d.healthId)";
            SQL += " and HHNo=(select  HHNo  from member  Where  healthid=d.healthId)";
            SQL += " and SNo=(select  SPNO1  from member  Where healthid=d.healthId))";
            SQL += " WHEN cast(SPNO1 as int) = 77 THEN (select  NameEng  from member where  ProvCode=(select  ProvCode  from member Where  healthid=d.healthId)";
            SQL += " and HHNo=(select  HHNo  from member  Where  healthid=d.healthId)";
            SQL += " and SNo=(select  SPNO1  from member Where healthid=d.healthId))";
            SQL += " WHEN cast(SPNO1 as int) = 88 THEN (select  NameEng  from member where  ProvCode=(select  ProvCode  from member  Where  healthid=d.healthId)";
            SQL += " and HHNo=(select  HHNo  from member  Where  healthid=d.healthId)";
            SQL += " and SNo=(select  SPNO1  from member Where  healthid=d.healthId))";
            SQL += " ELSE (select  NameEng  from member where  ProvCode=(select  ProvCode  from member  Where  healthid=d.healthId)";
            SQL += " and HHNo=(select  HHNo  from member  Where  healthid=d.healthId)";
            SQL += " and SNo=(select  SPNO1  from member  Where  healthid=d.healthId)) END AS  HusName,";
            SQL += " (select  Age  from member where ProvCode=(select  ProvCode  from member  Where  healthid=d.healthId)and HHNo=(select  HHNo  from member  Where  healthid=d.healthId and ProvCode=d.providerId)and SNo=(select  SPNO1  from member  Where healthid=d.healthId))as HusAge from Member m inner join death d on d.healthId=m.healthId left outer join elco e on e.healthId=m.healthId inner join Village v on v.ZILLAID=m.Dist and v.UPAZILAID=m.Upz and v.UNIONID=m.UN and v.MOUZAID=m.Mouza and v.VILLAGEID=m.Vill where d.providerId='"+ g.getProvCode() +"' and strftime('%d/%m/%Y', date(d.deathDT)) between'"+dtfrom.toString()+"' and '"+dtto.toString()+"'";

*/


            SQL = "Select ifnull(cM.healthId,'') as healthId,ifnull(cM.generatedId,'') as generatedId,Cast(((julianday(date(d.deathDT))-julianday(cM.dob))/365.25) as int) AS Agey,Cast(strftime('%m-%d',d.deathDT) - strftime('%m-%d',cM.dob) As int) AS Agem,Cast(strftime('%d',d.deathDT) - strftime('%d',cM.dob)As int) AS Aged, ";
            SQL += " '' as Elcono,ifnull(d.placeOfDeath ,'') as placeOfDeath,ifnull(d.DeathDT,'') as DeathDT,ifnull(d.systemEntryDate,'') as EntryDate, ifnull(d.deathOfPregWomen,'') as deathOfPregWomen, ifnull(d.causeOfDeath,'') as causeOfDeath,";
            SQL += " ifnull(cM.houseGrHoldingNo,'') as HHNo,ifnull(v.VILLAGENAMEENG,'') as VName, ";
            SQL += " ifnull(cM.name,'') as NameEng,ifnull(cM.fatherName,'') as Father,";
            SQL += " ifnull(cM.motherName,'') as Mother,ifnull(cM.husbandName,'') as HusName,";
            SQL += " ifnull(cM.gender,'') as Sex,ifnull(cM.Age,'') as Age from  clientMap cM inner join death d on cM.generatedId=d.healthId  left join Village v on v.ZILLAID=cM.ZILLAID and v.UPAZILAID=cM.UPAZILAID and v.UNIONID=cM.UNIONID ";
            SQL += " and v.MOUZAID=cM.MOUZAID and v.VILLAGEID=cM.VILLAGEID where d.providerId='" + g.getProvCode() + "' and strftime('%d/%m/%Y', date(d.DeathDT)) between'" + stdt.toString() + "' and '" + enddt.toString() + "'";

            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();
            dataList.clear();
            int slno = 0;
            while (!cur.isAfterLast()) {
                slno += 1;
                HashMap<String, String> map = new HashMap<String, String>();
                ListView list = (ListView) findViewById(R.id.lstData);
                map.put("slno", Integer.toString(slno));
                // map.put("tcount", cur.getString(cur.getColumnIndex("TCount")));
                map.put("placeOfDeath ", cur.getString(cur.getColumnIndex("placeOfDeath ")));
                map.put("elcono", cur.getString(cur.getColumnIndex("Elcono")));
                map.put("entrydate", Global.DateConvertDMY(cur.getString(cur.getColumnIndex("EntryDate"))));
                map.put("hhho", cur.getString(cur.getColumnIndex("HHNo")));
                map.put("vname", cur.getString(cur.getColumnIndex("VName")));
                map.put("name", cur.getString(cur.getColumnIndex("NameEng")));
                map.put("fname", cur.getString(cur.getColumnIndex("Father")));
                map.put("mname", cur.getString(cur.getColumnIndex("Mother")));
                map.put("hname", cur.getString(cur.getColumnIndex("HusName")));
                map.put("sex", cur.getString(cur.getColumnIndex("Sex")));
                map.put("deathdt", Global.DateConvertDMY(cur.getString(cur.getColumnIndex("DeathDT"))));

                map.put("agey", cur.getString(cur.getColumnIndex("Agey")));
                map.put("agem", cur.getString(cur.getColumnIndex("Agem")));
                map.put("aged", cur.getString(cur.getColumnIndex("Aged")));
                map.put("wdeath", cur.getString(cur.getColumnIndex("deathOfPregWomen")));
                map.put("cdeath", cur.getString(cur.getColumnIndex("causeOfDeath")));

                if (cur.getString(cur.getColumnIndex("deathOfPregWomen")).equalsIgnoreCase("1")) {
                    map.put("wdeath", "হ্যাঁ");
                } else if (cur.getString(cur.getColumnIndex("deathOfPregWomen")).equalsIgnoreCase("2")) {
                    map.put("wdeath", "");
                }
                if (cur.getString(cur.getColumnIndex("causeOfDeath")).equalsIgnoreCase("1")) {
                    map.put("cdeath", "ষ্ট্রোক");
                } else if (cur.getString(cur.getColumnIndex("causeOfDeath")).equalsIgnoreCase("2")) {
                    map.put("cdeath", "হার্ট এ্যাটাক");
                } else if (cur.getString(cur.getColumnIndex("causeOfDeath")).equalsIgnoreCase("3")) {
                    map.put("cdeath", "ক্যান্স");
                } else if (cur.getString(cur.getColumnIndex("causeOfDeath")).equalsIgnoreCase("4")) {
                    map.put("cdeath", "পানিতে ডুবে মৃত্যু   ");
                } else if (cur.getString(cur.getColumnIndex("causeOfDeath")).equalsIgnoreCase("5")) {
                    map.put("cdeath", "বিষ খেয়ে মৃত্যু  ");
                } else if (cur.getString(cur.getColumnIndex("causeOfDeath")).equalsIgnoreCase("6")) {
                    map.put("cdeath", "বজ্রপাতে মৃত্যু  ");
                } else if (cur.getString(cur.getColumnIndex("causeOfDeath")).equalsIgnoreCase("7")) {
                    map.put("cdeath", "সড়ক দুর্ঘটনায় মৃত্যু ");
                } else if (cur.getString(cur.getColumnIndex("causeOfDeath")).equalsIgnoreCase("8")) {
                    map.put("cdeath", "বিদু্্যতস্পৃষ্ট মৃত্যু");
                } else if (cur.getString(cur.getColumnIndex("causeOfDeath")).equalsIgnoreCase("9")) {
                    map.put("cdeath", "আত্ম্যহত্যা");
                } else if (cur.getString(cur.getColumnIndex("causeOfDeath")).equalsIgnoreCase("10")) {
                    map.put("cdeath", "ডায়রিয়া");
                } else if (cur.getString(cur.getColumnIndex("causeOfDeath")).equalsIgnoreCase("11")) {
                    map.put("cdeath", "নিউমোনিয়া");
                } else if (cur.getString(cur.getColumnIndex("causeOfDeath")).equalsIgnoreCase("12")) {
                    map.put("cdeath", "আগুনে পুড়ে মারা যাওয়া");
                } else if (cur.getString(cur.getColumnIndex("causeOfDeath")).equalsIgnoreCase("13")) {
                    map.put("cdeath", "বার্ধক্যজনিত মৃত্যু");
                } else if (cur.getString(cur.getColumnIndex("causeOfDeath")).equalsIgnoreCase("14")) {
                    map.put("cdeath", "অন্যান্য");
                }


                if (cur.getString(cur.getColumnIndex("Sex")).equalsIgnoreCase("1")) {
                    map.put("sex", "পুরুষ");
                } else if (cur.getString(cur.getColumnIndex("Sex")).equalsIgnoreCase("2")) {
                    map.put("sex", "মহিলা");
                }

                dataList.add(map);

                dataAdapter = new SimpleAdapter(Death.this, dataList, R.layout.deathrow, new String[]{"SNo", "EntryDate"},
                        new int[]{R.id.Slno, R.id.EntryDate});

                list.setAdapter(new DataListAdapter(this));

                cur.moveToNext();
                totalCount += 1;
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(Death.this, e.getMessage());
            return;
        }
    }


    public class DataListAdapter extends BaseAdapter {
        private Context context;

        public DataListAdapter(Context c) {
            context = c;
        }

        public int getCount() {
            return dataAdapter.getCount();
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
                convertView = inflater.inflate(R.layout.deathrow, null);
            }
            final TextView Slno = (TextView) convertView.findViewById(R.id.Slno);

            final TextView Elcono = (TextView) convertView.findViewById(R.id.Elcono);
            final TextView EntryDate = (TextView) convertView.findViewById(R.id.EntryDate);
            final TextView HHNo = (TextView) convertView.findViewById(R.id.HHNo);
            final TextView VName = (TextView) convertView.findViewById(R.id.VName);
            final TextView Name = (TextView) convertView.findViewById(R.id.Name);
            final TextView FName = (TextView) convertView.findViewById(R.id.FName);
            final TextView MName = (TextView) convertView.findViewById(R.id.MName);
            final TextView HName = (TextView) convertView.findViewById(R.id.HName);
            final TextView Sex = (TextView) convertView.findViewById(R.id.Sex);
            final TextView DeathDT = (TextView) convertView.findViewById(R.id.DeathDT);
            final TextView Age = (TextView) convertView.findViewById(R.id.Age);
            final TextView WDeath = (TextView) convertView.findViewById(R.id.WDeath);
            final TextView CDeath = (TextView) convertView.findViewById(R.id.CDeath);

            final HashMap<String, String> o = (HashMap<String, String>) dataAdapter.getItem(position);
            Slno.setText(o.get("slno"));
            // Sno.setText(o.get("sno"));
            // g.setTotalDeath(o.get("tcount"));
            EntryDate.setText(o.get("entrydate"));
            HHNo.setText(o.get("hhho"));
            Elcono.setText(o.get("elcono"));
            VName.setText(o.get("vname"));
            Name.setText(o.get("name"));
            FName.setText(o.get("fname"));
            MName.setText(o.get("mname"));
            HName.setText(o.get("hname"));
            Sex.setText(o.get("sex"));
            DeathDT.setText(o.get("deathdt"));
            if (o.get("agey").equalsIgnoreCase("0") & o.get("agem").equalsIgnoreCase("0") & Integer.valueOf(o.get("aged")) < 28) {
                Age.setText(o.get("aged") + "(দিন)");
            } else if (o.get("agey").equalsIgnoreCase("0") & Integer.valueOf(o.get("agem")) != 0 & (Integer.valueOf(o.get("aged")) > 28 || Integer.valueOf(o.get("aged")) < 60)) {
                Age.setText(o.get("agem") + "(মাস)");
            } else {
                Age.setText(o.get("agey") + "(বৎসর)");
            }
            WDeath.setText(o.get("wdeath"));

            CDeath.setText(o.get("cdeath"));
            final AlertDialog.Builder adb = new AlertDialog.Builder(Death.this);
            return convertView;
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

            dtpDate = (EditText) findViewById(R.id.dtpFromDT);

            if (VariableID.equals("btnFromDT")) {
                dtpDate = (EditText) findViewById(R.id.dtpFromDT);
            } else if (VariableID.equals("btnToDT")) {
                dtpDate = (EditText) findViewById(R.id.dtpToDT);
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

        }
    };
}
