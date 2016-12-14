package com.data.rhis2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import Common.Connection;
import Common.Global;

/**
 * Created by ccah on 12/20/2015.
 */
public class InjectableView extends Activity {
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
        // AlertDialog.Builder adb = new AlertDialog.Builder(Death.this);
        switch (item.getItemId()) {
            case R.id.menuClose:
                //  adb.setTitle("Close");
                //  adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান[Yes/No]?");
                //  adb.setNegativeButton("না", null);
                //  adb.setPositiveButton("হাঁ", new AlertDialog.OnClickListener() {
                // public void onClick(DialogInterface dialog, int which) {
                finish();
                // Intent f2 = new Intent(getApplicationContext(),MemberList.class);
                //  startActivity(f2);
                //     }});
                // adb.show();
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
            setContentView(R.layout.womaninjectableview);
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
            View header = getLayoutInflater().inflate(R.layout.womaninjviewheading, null);
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
            lblTCount.setText(":" + String.valueOf(totalCount));
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

            DisplaySearch(true);


        } catch (Exception e) {
            Connection.MessageBox(InjectableView.this, e.getMessage());
            return;
        }
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
                            .setAdapter(C.getArrayAdapter("Select '  ' union Select  substr('0' ||UPAZILAID, -2, 2)||'-'||UPAZILANAME from upazila where zillaid = '" + val + "'"));

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
                    ((Spinner) findViewById(R.id.txtunionSearch)).setAdapter(C.getArrayAdapter("Select '  ' union Select substr('0' ||UNIONID, -2, 2)||'-'||UNIONNAME FROM UNIONS where UPAZILAID ='" + val + "'"));


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ((Spinner) findViewById(R.id.txtunionSearch)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String desval = (((Spinner) findViewById(R.id.txtDistrictSearch)).getSelectedItemPosition() == 0 ? "" : Global.Left(((Spinner) findViewById(R.id.txtDistrictSearch)).getSelectedItem().toString(), 2));
                String upval = (((Spinner) findViewById(R.id.txtupazillaSearch)).getSelectedItemPosition() == 0 ? "" : Global.Left(((Spinner) findViewById(R.id.txtupazillaSearch)).getSelectedItem().toString(), 2));
                String uval = (((Spinner) findViewById(R.id.txtunionSearch)).getSelectedItemPosition() == 0 ? "" : Global.Left(((Spinner) findViewById(R.id.txtunionSearch)).getSelectedItem().toString(), 2));
                if (uval.length() > 0) {

                    ((Spinner) findViewById(R.id.txtVillSearch)).setAdapter(C.getArrayAdapter("Select '  ' VillageName from Village union select substr('0' || VILLAGEID, -2, 2)||'-'||VILLAGENAMEENG VillageName from Village where ZIlLAID='" + desval + "' and UPAZILAID='" + upval + "' and UNIONID='" + uval + "'"));


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
                    Search(displayheader);
                    //  list.addHeaderView(header);

                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                } catch (Exception ex) {
                    Connection.MessageBox(InjectableView.this, ex.getMessage());
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

    private void DisplayAdvanceSearchPopup() {
        final Dialog popupView = new Dialog(InjectableView.this);
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

    public void Search(Boolean displayheader) {
        // ListView list = (ListView)findViewById(R.id.lstData);
       /* ListView list= (ListView) findViewById(R.id.lstData);
        //View header = getLayoutInflater().inflate(R.layout.memberlistheading, null);
        //list.addHeaderView(header);
        //  list.addView(header);*/


        try {
            String SQL = "SELECT S.ELCONO AS Elcono,\n" +
                    "       S.NID AS NID,\n" +
                    "       S.BRID AS BRID,\n" +
                    "       S.generatedId AS generatedId,\n" +
                    "       S.SNo AS SNo,\n" +
                    "       S.HHNo AS HHNO,\n" +
                    "       S.HealthId AS HealthId,\n" +
                    "       S.VName AS VName,\n" +
                    "       S.WNameEng AS WNameEng,\n" +
                    "       S.HusName AS HusName,\n" +
                    "       S.MotherName AS Mother,\n" +
                    "       S.FatherName AS Father,\n" +
                    "       S.WAge AS WAge,\n" +
                    "       S.Mobno AS Mobno,\n" +
                    "       S.Edu AS Edu,\n" +
                    "       S.doseId AS doseId,\n" +
                    "       S.doseDate AS doseDate,\n" +
                    "       date( S.doseDate, '+90 day' )  AS DueDosedate,\n" +
                    "       S.sideEffect AS sideEffect\n" +
                    "  FROM ( \n" +
                    "    SELECT EL.ELCONO,\n" +
                    "           m.NID AS NID,\n" +
                    "           m.BRID AS BRID,\n" +
                    "           cM.generatedId AS generatedId,\n" +
                    "           m.SNO AS SNO,\n" +
                    "           m.HHNO AS HHNO,\n" +
                    "           m.HealthId AS HealthId,\n" +
                    "           v.VILLAGENAMEENG AS VName,\n" +
                    "           m.nameeng AS WNameEng,\n" +
                    "           s.nameeng AS HusName,\n" +
                    "           ( CAST ( ( ( julianday( date( 'now' )  ) - julianday( m.DOB )  ) / 30.4 )  AS int ) / 12 ) AS WAge,\n" +
                    "           ifnull( m.MobileNo1, '' ) AS Mobno,\n" +
                    "           ifnull( m.EDU, '' ) AS Edu,\n" +
                    "           ifnull( e.doseId, '' ) AS doseId,\n" +
                    "           ifnull( e.doseDate, '' ) AS doseDate,\n" +
                    "           date( e.doseDate, '+90 day' )  AS DueDosedate,\n" +
                    "           e.sideEffect AS sideEffect,\n" +
                    "           ( \n" +
                    "               SELECT CASE\n" +
                    "                           WHEN CAST ( E.MNo AS int ) = 55 THEN ifnull( E.Mother, '' ) \n" +
                    "                           WHEN CAST ( E.MNo AS int ) = 77 THEN ifnull( E.Mother, '' ) \n" +
                    "                           WHEN CAST ( E.MNo AS int ) = 88 THEN ifnull( E.Mother, '' ) \n" +
                    "                           ELSE( \n" +
                    "                          SELECT NameEng\n" +
                    "                            FROM member A\n" +
                    "                           WHERE A.ProvCode =( \n" +
                    "                                     SELECT B.ProvCode\n" +
                    "                                       FROM member B\n" +
                    "                                      WHERE B.healthid = m.HealthID \n" +
                    "                                 ) \n" +
                    "                                 \n" +
                    "                                 AND\n" +
                    "                                 A.HHNo =( \n" +
                    "                                     SELECT C.HHNo\n" +
                    "                                       FROM member C\n" +
                    "                                      WHERE C.healthid = m.HealthID \n" +
                    "                                 ) \n" +
                    "                                 \n" +
                    "                                 AND\n" +
                    "                                 A.SNo =( \n" +
                    "                                     SELECT D.MNo\n" +
                    "                                       FROM member D\n" +
                    "                                      WHERE D.HealthID = m.HealthID \n" +
                    "                                 ) \n" +
                    "                                  \n" +
                    "                      ) \n" +
                    "                      \n" +
                    "                      END AS Mother\n" +
                    "                 FROM member E\n" +
                    "                WHERE E.healthId = m.HealthID \n" +
                    "           ) \n" +
                    "           AS MotherName,\n" +
                    "           ( \n" +
                    "               SELECT CASE\n" +
                    "                           WHEN CAST ( E.FNo AS int ) = 55 THEN ifnull( E.Father, '' ) \n" +
                    "                           WHEN CAST ( E.FNo AS int ) = 77 THEN ifnull( E.Father, '' ) \n" +
                    "                           WHEN CAST ( E.FNo AS int ) = 88 THEN ifnull( E.Father, '' ) \n" +
                    "                           ELSE( \n" +
                    "                          SELECT A.NameEng\n" +
                    "                            FROM member A\n" +
                    "                           WHERE A.ProvCode =( \n" +
                    "                                     SELECT B.ProvCode\n" +
                    "                                       FROM member B\n" +
                    "                                      WHERE B.healthid = m.HealthID \n" +
                    "                                 ) \n" +
                    "                                 \n" +
                    "                                 AND\n" +
                    "                                 HHNo =( \n" +
                    "                                     SELECT HHNo\n" +
                    "                                       FROM member C\n" +
                    "                                      WHERE C.healthid = m.HealthID \n" +
                    "                                 ) \n" +
                    "                                 \n" +
                    "                                 AND\n" +
                    "                                 SNo =( \n" +
                    "                                     SELECT FNo\n" +
                    "                                       FROM member D\n" +
                    "                                      WHERE D.healthid = m.HealthID \n" +
                    "                                 ) \n" +
                    "                                  \n" +
                    "                      ) \n" +
                    "                      \n" +
                    "                      END AS Father\n" +
                    "                 FROM member E\n" +
                    "                WHERE E.healthId = m.HealthID \n" +
                    "           ) \n" +
                    "           AS FatherName\n" +
                    "      FROM Member m\n" +
                    "           LEFT OUTER JOIN Member s\n" +
                    "                        ON m.dist = s.dist \n" +
                    "    AND\n" +
                    "    m.upz = s.upz \n" +
                    "    AND\n" +
                    "    m.un = s.un \n" +
                    "    AND\n" +
                    "    m.mouza = s.mouza \n" +
                    "    AND\n" +
                    "    m.vill = s.vill \n" +
                    "    AND\n" +
                    "    m.hhno = s.hhno \n" +
                    "    AND\n" +
                    "    m.spno1 = s.sno\n" +
                    "           LEFT OUTER JOIN clientMap cM\n" +
                    "                        ON cM.healthId = m.healthId\n" +
                    "           INNER JOIN womanInjectable e\n" +
                    "                   ON e.HealthId = cM.generatedId\n" +
                    "           INNER JOIN elco EL\n" +
                    "                   ON EL.HealthId = cM.generatedId\n" +
                    "           INNER JOIN Village v\n" +
                    "                   ON v.ZILLAID = m.Dist \n" +
                    "    AND\n" +
                    "    v.UPAZILAID = m.UPZ \n" +
                    "    AND\n" +
                    "    v.UNIONID = m.UN \n" +
                    "    AND\n" +
                    "    v.MOUZAID = m.MOUZA \n" +
                    "    AND\n" +
                    "    v.VILLAGEID = m.VILL\n" +
                    "     WHERE CAST ( ( ( julianday( date( 'now' )  ) - julianday( m.DOB )  ) / 30.4 )  AS int ) / 12 >= 15 \n" +
                    "           AND\n" +
                    "           CAST ( ( ( julianday( date( 'now' )  ) - julianday( m.DOB )  ) / 30.4 )  AS int ) / 12 <= 49 \n" +
                    "           AND\n" +
                    "           m.Sex = 2 \n" +
                    "           AND\n" +
                    "           m.MS = 2\n" +
                    "     GROUP BY e.healthId \n" +
                    ") \n" +
                    "AS S\n ";

            String wheresql = "";
            // +"' and m.upz='"+
            // wheresql +=g.getProvCode();
            if (((EditText) findViewById(R.id.txtHealthIDSearch)).getText().toString().length() > 0) {
                wheresql += " healthId like '%" + ((EditText) findViewById(R.id.txtHealthIDSearch)).getText().toString().trim() + "%'";
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
                wheresql += " generatedId '%" + ((EditText) findViewById(R.id.txtNRCIdSearch)).getText().toString() + "%'";
            }

            if (((EditText) findViewById(R.id.txtNameSearch)).getText().toString().length() > 0) {
                if (wheresql.length() > 0) {
                    wheresql += " AND ";
                }
                wheresql += " WNameEng  like '%" + ((EditText) findViewById(R.id.txtNameSearch)).getText().toString() + "%'";
            }

            if (((EditText) findViewById(R.id.txtMotherSearch)).getText().toString().length() > 0) {
                if (wheresql.length() > 0) {
                    wheresql += " AND ";
                }
                wheresql += " Mother  like '%" + ((EditText) findViewById(R.id.txtMotherSearch)).getText().toString() + "%'";
                wheresql += " OR Mother like '%" + ((EditText) findViewById(R.id.txtMotherSearch)).getText().toString() + "%'";
            }

            if (((EditText) findViewById(R.id.txtFatherSearch)).getText().toString().length() > 0) {
                if (wheresql.length() > 0) {
                    wheresql += " AND ";
                }
                wheresql += " Father  like '%" + ((EditText) findViewById(R.id.txtFatherSearch)).getText().toString() + "%'";
                wheresql += " OR Father like '%" + ((EditText) findViewById(R.id.txtFatherSearch)).getText().toString() + "%'";
            }
            if (((EditText) findViewById(R.id.txtHusbandSearch)).getText().toString().length() > 0) {
                if (wheresql.length() > 0) {
                    wheresql += " AND ";
                }
                wheresql += " HusName  like '%" + ((EditText) findViewById(R.id.txtHusbandSearch)).getText().toString() + "%'";
                wheresql += " OR HusName like '%" + ((EditText) findViewById(R.id.txtHusbandSearch)).getText().toString() + "%'";
            }

            if (((EditText) findViewById(R.id.txtDOBSearch)).getText().toString().length() > 0) {
                if (wheresql.length() > 0) {
                    wheresql += " AND ";
                }
                wheresql += " WAge  like '%" + ((EditText) findViewById(R.id.txtDOBSearch)).getText().toString() + "%'";

            }

          /*





            String dist = "";
            dist = ((Spinner) findViewById(R.id.txtDistrictSearch)).getSelectedItemPosition()==0?"":Global.Left(((Spinner) findViewById(R.id.txtDistrictSearch)).getSelectedItem().toString(),1);
            if (dist.length() > 0) {
                if (wheresql.length() > 0) {
                    wheresql += " AND ";
                }
                wheresql += " Dist like '%" + dist + "%'";
            }


            String upz="";
            if(((LinearLayout) findViewById(R.id.secUpazilla)).getVisibility()==View.VISIBLE) {
                upz = ((Spinner) findViewById(R.id.txtupazillaSearch)).getSelectedItemPosition() == 0 ? "" : Global.Left(((Spinner) findViewById(R.id.txtupazillaSearch)).getSelectedItem().toString(), 1);

                if (upz.length() > 0) {
                    if (wheresql.length() > 0) {
                        wheresql += " AND ";
                    }
                    wheresql += " Upz like '%" + upz + "%'";
                }
            }

            String union="";
            if(((LinearLayout) findViewById(R.id.secUnion)).getVisibility()==View.VISIBLE) {
                union = ((Spinner) findViewById(R.id.txtunionSearch)).getSelectedItemPosition() == 0 ? "" : Global.Left(((Spinner) findViewById(R.id.txtunionSearch)).getSelectedItem().toString(), 1);

                if (union.length() > 0) {
                    if (wheresql.length() > 0) {
                        wheresql += " AND ";
                    }
                    wheresql += " UN like '%" + union + "%'";
                }
            }
            String vil="";
            if(((LinearLayout) findViewById(R.id.secVillSearch)).getVisibility()==View.VISIBLE) {
                vil = ((Spinner) findViewById(R.id.txtVillSearch)).getSelectedItemPosition() == 0 ? "" : Global.Left(((Spinner) findViewById(R.id.txtVillSearch)).getSelectedItem().toString(), 1);

                if (vil.length() > 0) {
                    if (wheresql.length() > 0) {
                        wheresql += " AND ";
                    }
                    wheresql += " Vill like '%" + vil + "%'";
                }
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
            }*/

            if (wheresql.length() > 0) {
                SQL = SQL + " WHERE " + wheresql;
            }
            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();
            dataList = new ArrayList<HashMap<String, String>>();

            int slno = 0;
            ListView list = (ListView) findViewById(R.id.lstData);
            while (!cur.isAfterLast()) {
                slno += 1;
                HashMap<String, String> map = new HashMap<String, String>();

                map.put("slno", Integer.toString(slno));
                map.put("hhho", cur.getString(cur.getColumnIndex("HHNO")));
                map.put("healthId", cur.getString(cur.getColumnIndex("HealthId")));
                map.put("elcono", cur.getString(cur.getColumnIndex("Elcono")));
                map.put("vname", cur.getString(cur.getColumnIndex("VName")));
                map.put("wname", cur.getString(cur.getColumnIndex("WNameEng")));
                map.put("hname", cur.getString(cur.getColumnIndex("HusName")));
                map.put("wage", cur.getString(cur.getColumnIndex("WAge")));
                //map.put("mdate", cur.getString(cur.getColumnIndex("MDate")));
                map.put("mobno", cur.getString(cur.getColumnIndex("Mobno")));
                /*map.put("son", cur.getString(cur.getColumnIndex("Son")));
                map.put("dau", cur.getString(cur.getColumnIndex("Dau")));
                map.put("edu", cur.getString(cur.getColumnIndex("Edu")));*/

                map.put("doseid", cur.getString(cur.getColumnIndex("doseId")));
                map.put("dosedate", cur.getString(cur.getColumnIndex("doseDate")));
                map.put("duedosedate", cur.getString(cur.getColumnIndex("DueDosedate")));

                map.put("sideeffect", cur.getString(cur.getColumnIndex("sideEffect")));


                map.put("GeneratedId", cur.getString(cur.getColumnIndex("generatedId")));
                if (cur.getString(cur.getColumnIndex("sideEffect")).equalsIgnoreCase("1")) {
                    map.put("sideeffect", "হ্যাঁ ");
                } else if (cur.getString(cur.getColumnIndex("sideEffect")).equalsIgnoreCase("2")) {
                    map.put("sideeffect", "না");
                }

                if (cur.getString(cur.getColumnIndex("doseId")).equalsIgnoreCase("1")) {
                    map.put("doseid", "১ম");
                } else if (cur.getString(cur.getColumnIndex("doseId")).equalsIgnoreCase("2")) {
                    map.put("doseid", "২য়");
                } else if (cur.getString(cur.getColumnIndex("doseId")).equalsIgnoreCase("3")) {
                    map.put("doseid", "৩য়");
                } else if (cur.getString(cur.getColumnIndex("doseId")).equalsIgnoreCase("4")) {
                    map.put("doseid", "৪র্থ");
                } else if (cur.getString(cur.getColumnIndex("doseId")).equalsIgnoreCase("5")) {
                    map.put("doseid", "৫ম ");
                } else if (cur.getString(cur.getColumnIndex("doseId")).equalsIgnoreCase("6")) {
                    map.put("doseid", "৬ষ্ঠ");
                } else if (cur.getString(cur.getColumnIndex("doseId")).equalsIgnoreCase("7")) {
                    map.put("doseid", "৭ম");
                } else if (cur.getString(cur.getColumnIndex("doseId")).equalsIgnoreCase("8")) {
                    map.put("doseid", "৮ম");
                } else if (cur.getString(cur.getColumnIndex("doseId")).equalsIgnoreCase("9")) {
                    map.put("doseid", "৯ম ");
                } else if (cur.getString(cur.getColumnIndex("doseId")).equalsIgnoreCase("10")) {
                    map.put("doseid", "১০ম");
                }

                /*if(cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("99"))
                {
                    map.put("edu","শিক্ষাগত যোগ্যতা নেই");
                }
                else if(cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("01"))
                {
                    map.put("edu","১ম শ্রেনী");
                }
                else if(cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("02"))
                {
                    map.put("edu","২য় শ্রেনী");
                }

                else if(cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("03"))
                {
                    map.put("edu","৩য় শ্রেনী");
                }

                else if(cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("04"))
                {
                    map.put("edu","৪র্থ শ্রেনী");
                }
                else if(cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("05"))
                {
                    map.put("edu","৫ম শ্রেনী");
                }
                else if(cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("06"))
                {
                    map.put("edu","৬ষ্ঠ শ্রেনী");
                }

                else if(cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("07"))
                {
                    map.put("edu","৭ম শ্রেনী");
                }

                else if(cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("08"))
                {
                    map.put("edu","৮ম শ্রেনী");
                }
                else if(cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("09"))
                {
                    map.put("edu","৯ম শ্রেনী");
                }

                else if(cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("10"))
                {
                    map.put("edu","মাধ্যমিক বা সমতুল্য");
                }
                else if(cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("11"))
                {
                    map.put("edu","উচ্চ মাধ্যমিক বা সমতুল্য");
                }
                else if(cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("12"))
                {
                    map.put("edu","স্নাতক বা সমতুল্য");
                }

                else if(cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("13"))
                {
                    map.put("edu","স্নাতকোত্তর বা সমতুল্য");
                }

                else if(cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("14"))
                {
                    map.put("edu","ডাক্তারি");
                }

                else if(cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("15"))
                {
                    map.put("edu","ইঞ্জিনিয়ারিং");
                }

                else if(cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("16"))
                {
                    map.put("edu","বৃত্তিমুলক শিক্ষা");
                }

                else if(cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("17"))
                {
                    map.put("edu","কারিগরি শিক্ষা");
                }
                else if(cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("18"))
                {
                    map.put("edu","ধাত্রীবিদ্যা/নার্সিং");
                }
                else if(cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("19"))
                {
                    map.put("edu","অন্যান্যা");
                }

                else if(cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("77"))
                {
                    map.put("edu","প্রযোজ্য নয়");
                }*/


                dataList.add(map);

                dataAdapter = new SimpleAdapter(InjectableView.this, dataList, R.layout.womaninjviewrow, new String[]{"SNo", "EntryDate"},
                        new int[]{R.id.Slno, R.id.EntryDate});

                list.setAdapter(new DataListAdapter(this));

                cur.moveToNext();
                totalCount += 1;
            }
            cur.close();
            /*((TextView)findViewById(R.id.lblTCount)).setText("মোট "+ String.valueOf(slno)+ " টি ডাটা পাওয়া গিয়েছে" );*/
            ((TextView) findViewById(R.id.lblTCount)).setText(String.valueOf(slno));

        } catch (Exception e) {
            Connection.MessageBox(InjectableView.this, e.getMessage());
        }

    }

    /*private String GetTotalElco(String ProvCode) {
        //C.ReturnSingleValue("Select Count(*) as Tdeath from death d where d.providerId='"+ g.getProvCode() +"' and strftime('%d/%m/%Y', date(d.deathDT)) between'"+dtpFromDT.getText().toString()+"' and '"+dtpToDT.getText().toString()+"'");
        String sq ="Select Count(*) as elco from womanInjectable  where providerId='"+ g.getProvCode() +"'";
        return C.ReturnSingleValue(sq);
    }*/

    private String GetTotalElco(String dtfrom, String dtto) {
        //C.ReturnSingleValue("Select Count(*) as Tdeath from death d where d.providerId='"+ g.getProvCode() +"' and strftime('%d/%m/%Y', date(d.deathDT)) between'"+dtpFromDT.getText().toString()+"' and '"+dtpToDT.getText().toString()+"'");
        String sq = "Select Count(*) as Tdeath from womanInjectable e where e.providerId='" + g.getProvCode() + "' and strftime('%d/%m/%Y', date(e.systemEntryDate)) between'" + dtfrom + "' and '" + dtto + "'";
        return C.ReturnSingleValue(sq);
    }

    int totalCount = 0;

    private void DataSearch(String stdt, String enddt) {
        try {
            String SQL = "";
            SQL = "Select ifnull(m.HHNo,'')  as HHNo,ifnull(cM.healthId,'') as healthid,ifnull(cM.generatedId,'') as generatedId,v.VILLAGENAMEENG AS VName,ifnull(m.NameEng,'') as NameEng,";
            SQL += " ifnull(m.MobileNo1,'') as mobile, Cast(((julianday(date('now'))-julianday(m.DOB))/365.25) as int) as Age,";
            SQL += " ifnull(e.elcoNo,'')  as elcoNo,(select  n.NameEng  from member n";
            SQL += " where  n.ProvCode=(select  o.ProvCode  from member o  Where  o.healthid =inj.healthId)and";
            SQL += " n.HHNo=(select  p.HHNo  from member p Where  p.healthid=inj.healthId) and";
            SQL += " n.SNo=(select  q.SPNO1  from member q Where  q.healthid=inj.healthId))as HusName,inj.doseId as doseId,inj.doseDate as doseDate,inj.sideEffect as sideEffect";
            SQL += " from  clientMap cm inner join Member m on cM.healthId=m.healthId  left outer join elco e on e.healthId=cM.generatedId  LEFT JOIN womanInjectable inj ON e.healthId = inj.healthId inner join Village v on v.ZILLAID=m.Dist and v.UPAZILAID=m.Upz and v.UNIONID=m.UN and v.MOUZAID=m.Mouza and v.VILLAGEID=m.Vill";// where from Member m
            /*SQL += " from Member m  LEFT JOIN elco e ON e.healthId = inj.healthId";
            SQL += " LEFT JOIN womanInjectable inj ON m.healthid = inj.healthId";
            SQL += " inner join Village v on v.ZILLAID=m.Dist and";
            SQL += " v.UPAZILAID=m.Upz and v.UNIONID=m.UN and v.MOUZAID=m.Mouza and  v.VILLAGEID=m.Vill";*/
            SQL += " where inj.providerId='" + g.getProvCode() + "' and strftime('%d/%m/%Y', date(inj.systemEntryDate)) between'" + stdt.toString() + "' and '" + enddt.toString() + "'";


            //SQL += " LEFT JOIN womanInjectable inj ON m.healthid = inj.healthId";
            //SQL += " inner join Village v on v.ZILLAID=m.Dist and";
            //SQL += " v.UPAZILAID=m.Upz and v.UNIONID=m.UN and v.MOUZAID=m.Mouza and  v.VILLAGEID=m.Vill";
            SQL += " where inj.providerId='" + g.getProvCode() + "'";

            //='"+ g.getProvCode()+"' and strftime('%d/%m/%Y', date(e.systemEntryDate)) between'"+dtfrom.toString()+"' and '"+dtto.toString()+"'";

            /*SQL  = "Select ifnull(SNo,'') as SNo,ifnull(m.HHNo,'') AS HHNo,ifnull(e.healthId,'') as HealthId,v.VILLAGENAMEENG AS VName, ifnull(NameEng,'') as WNameEng,ifnull(NameBang,'') as NameBang,";
            SQL += " CASE WHEN cast(SPNO1 as int) = 55 THEN (select  NameEng  from member where  ProvCode=(select  ProvCode  from member Where healthid=e.healthId) and HHNo=(select  HHNo  from member  Where  healthid=e.healthId) and SNo=(select  SPNO1  from member  Where healthid=e.healthId)) WHEN cast(SPNO1 as int) = 77 THEN (select  NameEng  from member where  ProvCode=(select  ProvCode  from member Where  healthid=e.healthId) and HHNo=(select  HHNo  from member  Where  healthid=e.healthId) and SNo=(select  SPNO1  from member Where healthid=e.healthId)) WHEN cast(SPNO1 as int) = 88 THEN (select  NameEng  from member where  ProvCode=(select  ProvCode  from member Where  healthid=e.healthId) and HHNo=(select  HHNo  from member  Where healthid=e.healthId) and SNo=(select  SPNO1  from member Where  healthid=e.healthId)) ELSE (select  NameEng  from member where  ProvCode=(select  ProvCode  from member  Where healthid=e.healthId) and HHNo=(select  HHNo  from member  Where  healthid=e.healthId) and SNo=(select  SPNO1  from member  Where  healthid=e.healthId)) END AS  HusName,";
            SQL += "ifnull(m.Age,'') as WAge,ifnull(m.MobileNo1,'') as Mobno,ifnull(EDU,'') AS Edu from Member m left outer join PregWomen e on e.healthId=m.healthId inner join Village v on v.ZILLAID=m.Dist and v.UPAZILAID=m.Upz and v.UNIONID=m.UN and v.MOUZAID=m.Mouza and  v.VILLAGEID=m.Vill  where e.providerId='"+ g.getProvCode()+"'";
*/
            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();
            dataList.clear();
            int slno = 0;
            while (!cur.isAfterLast()) {
                slno += 1;
                HashMap<String, String> map = new HashMap<String, String>();
                ListView list = (ListView) findViewById(R.id.lstData);
                map.put("slno", Integer.toString(slno));
                map.put("hhho", cur.getString(cur.getColumnIndex("HHNo")));
                map.put("healthId", cur.getString(cur.getColumnIndex("healthid")));
                map.put("elcono", cur.getString(cur.getColumnIndex("elcoNo")));
                map.put("vname", cur.getString(cur.getColumnIndex("VName")));
                map.put("wname", cur.getString(cur.getColumnIndex("NameEng")));
                map.put("hname", cur.getString(cur.getColumnIndex("HusName")));
                map.put("wage", cur.getString(cur.getColumnIndex("Age")));
                map.put("mobno", cur.getString(cur.getColumnIndex("mobile")));
                map.put("doseid", cur.getString(cur.getColumnIndex("doseId")));
                map.put("dosedate", cur.getString(cur.getColumnIndex("doseDate")));

                map.put("sideeffect", cur.getString(cur.getColumnIndex("sideEffect")));

                if (cur.getString(cur.getColumnIndex("sideEffect")).equalsIgnoreCase("1")) {
                    map.put("sideeffect", "হ্যাঁ ");
                } else if (cur.getString(cur.getColumnIndex("sideEffect")).equalsIgnoreCase("2")) {
                    map.put("sideeffect", "না");
                }

                if (cur.getString(cur.getColumnIndex("doseId")).equalsIgnoreCase("1")) {
                    map.put("doseid", "১ম");
                } else if (cur.getString(cur.getColumnIndex("doseId")).equalsIgnoreCase("2")) {
                    map.put("doseid", "২য়");
                } else if (cur.getString(cur.getColumnIndex("doseId")).equalsIgnoreCase("3")) {
                    map.put("doseid", "৩য়");
                } else if (cur.getString(cur.getColumnIndex("doseId")).equalsIgnoreCase("4")) {
                    map.put("doseid", "৪র্থ");
                } else if (cur.getString(cur.getColumnIndex("doseId")).equalsIgnoreCase("5")) {
                    map.put("doseid", "৫ম ");
                } else if (cur.getString(cur.getColumnIndex("doseId")).equalsIgnoreCase("6")) {
                    map.put("doseid", "৬ষ্ঠ");
                } else if (cur.getString(cur.getColumnIndex("doseId")).equalsIgnoreCase("7")) {
                    map.put("doseid", "৭ম");
                } else if (cur.getString(cur.getColumnIndex("doseId")).equalsIgnoreCase("8")) {
                    map.put("doseid", "৮ম");
                } else if (cur.getString(cur.getColumnIndex("doseId")).equalsIgnoreCase("9")) {
                    map.put("doseid", "৯ম ");
                } else if (cur.getString(cur.getColumnIndex("doseId")).equalsIgnoreCase("10")) {
                    map.put("doseid", "১০ম");
                }

                dataList.add(map);

                dataAdapter = new SimpleAdapter(InjectableView.this, dataList, R.layout.womaninjviewrow, new String[]{"SNo", "EntryDate"},
                        new int[]{R.id.Slno, R.id.EntryDate});

                list.setAdapter(new DataListAdapter(this));

                cur.moveToNext();
                totalCount += 1;
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(InjectableView.this, e.getMessage());
            return;
        }
    }

    private void DataSearch() {
        try {
            String SQL = "";

            /*SQL  = "Select ifnull(SNo,'') as SNo,ifnull(m.HHNo,'')  as HHNo,ifnull(m.healthid,'') as healthid,ifnull(m.NameEng,'') as NameEng,";
            SQL += " ifnull(m.MobileNo1,'') as mobile, Cast(((julianday(date('now'))-julianday(m.DOB))/365.25) as int) as Age,";
            SQL += " ifnull(e.elcoNo,'')  as elcoNo,(select  n.NameEng  from member n";
            SQL += " where  n.ProvCode=(select  o.ProvCode  from member o  Where  o.healthid ='"+ g.getHealthID()+"')and";
            SQL += " n.HHNo=(select  p.HHNo  from member p Where  p.healthid='"+ g.getHealthID()+"') and";
            SQL += " n.SNo=(select  q.SPNO1  from member q Where  q.healthid='"+ g.getHealthID()+"'))as HusName,inj.doseId,inj.doseDate,inj.sideEffect";
            SQL += " from Member m  LEFT JOIN elco e ON e.healthId = m.healthid";
            SQL += " LEFT JOIN womanInjectable inj ON inj.healthId = m.healthid where m.healthid='"+ g.getHealthID()+"'";*/


            /*SQL  = "Select ifnull(m.HHNo,'')  as HHNo,ifnull(cM.healthId,'') as healthid,ifnull(cM.generatedId,'') as generatedId,v.VILLAGENAMEENG AS VName,ifnull(m.NameEng,'') as NameEng,";
            SQL += " ifnull(m.MobileNo1,'') as mobile, Cast(((julianday(date('now'))-julianday(m.DOB))/365.25) as int) as Age,";
            SQL += " ifnull(e.elcoNo,'')  as elcoNo,(select  n.NameEng  from member n";
            SQL += " where  n.ProvCode=(select  o.ProvCode  from member o  Where  o.healthid =inj.healthId)and";
            SQL += " n.HHNo=(select  p.HHNo  from member p Where  p.healthid=inj.healthId) and";
            SQL += " n.SNo=(select  q.SPNO1  from member q Where  q.healthid=inj.healthId))as HusName,inj.doseId as doseId,inj.doseDate as doseDate,inj.sideEffect as sideEffect";
            SQL += " from  clientMap cm inner join Member m on cM.healthId=m.healthId  left outer join elco e on e.healthId=cM.generatedId  LEFT JOIN womanInjectable inj ON e.healthId = inj.healthId inner join Village v on v.ZILLAID=m.Dist and v.UPAZILAID=m.Upz and v.UNIONID=m.UN and v.MOUZAID=m.Mouza and v.VILLAGEID=m.Vill where inj.healthId=cM.generatedId GROUP BY cM.healthId HAVING  count(cM.healthId)";// where from Member m
    */        //SQL += " LEFT JOIN womanInjectable inj ON m.healthid = inj.healthId";
            //SQL += " inner join Village v on v.ZILLAID=m.Dist and";
            //SQL += " v.UPAZILAID=m.Upz and v.UNIONID=m.UN and v.MOUZAID=m.Mouza and  v.VILLAGEID=m.Vill";
            //  SQL += " where inj.providerId='"+ g.getProvCode()+"'";
            SQL = "Select ifnull(m.HHNo,'')  as HHNo,ifnull(cM.healthId,'') as healthid,\n" +
                    "ifnull(cM.generatedId,'') as generatedId,v.VILLAGENAMEENG AS VName,\n" +
                    "ifnull(m.NameEng,'') as NameEng, ifnull(m.MobileNo1,'') as mobile, \n" +
                    "Cast(((julianday(date('now'))-julianday(m.DOB))/365.25) as int) as Age, \n" +
                    "ifnull(e.elcoNo,'')  as elcoNo,\n" +
                    "CASE WHEN CAST ( m.SPNO1 AS int ) = 55 THEN ifnull ( m.NameEng , '' )\n" +
                    "WHEN CAST ( m.SPNO1 AS int ) = 77 THEN ifnull ( m.NameEng , '' )\n" +
                    "WHEN CAST ( m.SPNO1 AS int ) = 88 THEN ifnull ( m.NameEng , '' )\n" +
                    "ELSE ( SELECT NameEng FROM member A\n" +
                    "WHERE A.ProvCode = ( SELECT B.ProvCode FROM member B WHERE B.healthid =m.healthId\n" +
                    "and A.Dist = B.Dist and A.Upz = B.Upz and A.UN = B.UN and A.Mouza = B.Mouza and\n" +
                    "A.Vill =B.Vill and A.HHNo = B.HHNo ) AND \n" +
                    "A.HHNo = ( SELECT C.HHNo FROM member C WHERE C.healthid =m.healthId )\n" +
                    "AND A.SNo = ( SELECT D.SPNO1 FROM member  D WHERE D.HealthID =m.healthId ) )\n" +
                    "END AS HusName,\n" +
                    " inj.doseId as doseId,strftime('%d/%m/%Y', date(inj.doseDate)) as doseDate, date( inj.doseDate, '+90 day' )  AS DueDosedate,inj.sideEffect as \n" +
                    " sideEffect from  clientMap cm inner join Member m on cM.healthId=m.healthId \n" +
                    "  left outer join elco e on e.healthId=cM.generatedId  \n" +
                    "  LEFT JOIN womanInjectable inj ON e.healthId = inj.healthId inner \n" +
                    "  join Village v on v.ZILLAID=m.Dist and v.UPAZILAID=m.Upz \n" +
                    "  and v.UNIONID=m.UN and v.MOUZAID=m.Mouza and v.VILLAGEID=m.Vill \n" +
                    "  where inj.healthId=cM.generatedId GROUP BY cM.healthId HAVING  count(cM.healthId) ";
            /*SQL  = "Select ifnull(SNo,'') as SNo,ifnull(m.HHNo,'') AS HHNo,ifnull(e.healthId,'') as HealthId,v.VILLAGENAMEENG AS VName, ifnull(NameEng,'') as WNameEng,ifnull(NameBang,'') as NameBang,";
            SQL += " CASE WHEN cast(SPNO1 as int) = 55 THEN (select  NameEng  from member where  ProvCode=(select  ProvCode  from member Where healthid=e.healthId) and HHNo=(select  HHNo  from member  Where  healthid=e.healthId) and SNo=(select  SPNO1  from member  Where healthid=e.healthId)) WHEN cast(SPNO1 as int) = 77 THEN (select  NameEng  from member where  ProvCode=(select  ProvCode  from member Where  healthid=e.healthId) and HHNo=(select  HHNo  from member  Where  healthid=e.healthId) and SNo=(select  SPNO1  from member Where healthid=e.healthId)) WHEN cast(SPNO1 as int) = 88 THEN (select  NameEng  from member where  ProvCode=(select  ProvCode  from member Where  healthid=e.healthId) and HHNo=(select  HHNo  from member  Where healthid=e.healthId) and SNo=(select  SPNO1  from member Where  healthid=e.healthId)) ELSE (select  NameEng  from member where  ProvCode=(select  ProvCode  from member  Where healthid=e.healthId) and HHNo=(select  HHNo  from member  Where  healthid=e.healthId) and SNo=(select  SPNO1  from member  Where  healthid=e.healthId)) END AS  HusName,";
            SQL += "ifnull(m.Age,'') as WAge,ifnull(m.MobileNo1,'') as Mobno,ifnull(EDU,'') AS Edu from Member m left outer join PregWomen e on e.healthId=m.healthId inner join Village v on v.ZILLAID=m.Dist and v.UPAZILAID=m.Upz and v.UNIONID=m.UN and v.MOUZAID=m.Mouza and  v.VILLAGEID=m.Vill  where e.providerId='"+ g.getProvCode()+"'";
*/
            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();
            dataList.clear();
            int slno = 0;
            while (!cur.isAfterLast()) {
                slno += 1;
                HashMap<String, String> map = new HashMap<String, String>();
                ListView list = (ListView) findViewById(R.id.lstData);
                map.put("slno", Integer.toString(slno));
                map.put("hhho", cur.getString(cur.getColumnIndex("HHNo")));
                map.put("healthId", cur.getString(cur.getColumnIndex("healthid")));
                map.put("elcono", cur.getString(cur.getColumnIndex("elcoNo")));
                map.put("vname", cur.getString(cur.getColumnIndex("VName")));
                map.put("wname", cur.getString(cur.getColumnIndex("NameEng")));
                map.put("hname", cur.getString(cur.getColumnIndex("HusName")));
                map.put("wage", cur.getString(cur.getColumnIndex("Age")));
                map.put("mobno", cur.getString(cur.getColumnIndex("mobile")));
                map.put("doseid", cur.getString(cur.getColumnIndex("doseId")));
                map.put("dosedate", cur.getString(cur.getColumnIndex("doseDate")));
                map.put("duedosedate", cur.getString(cur.getColumnIndex("DueDosedate")));
                map.put("sideeffect", cur.getString(cur.getColumnIndex("sideEffect")));
                map.put("GeneratedId", cur.getString(cur.getColumnIndex("generatedId")));

                //map.put("edu", cur.getString(cur.getColumnIndex("Edu")));
                if (cur.getString(cur.getColumnIndex("sideEffect")).equalsIgnoreCase("1")) {
                    map.put("sideeffect", "হ্যাঁ ");
                } else if (cur.getString(cur.getColumnIndex("sideEffect")).equalsIgnoreCase("2")) {
                    map.put("sideeffect", "না");
                }

                if (cur.getString(cur.getColumnIndex("doseId")).equalsIgnoreCase("1")) {
                    map.put("doseid", "১ম");
                } else if (cur.getString(cur.getColumnIndex("doseId")).equalsIgnoreCase("2")) {
                    map.put("doseid", "২য়");
                } else if (cur.getString(cur.getColumnIndex("doseId")).equalsIgnoreCase("3")) {
                    map.put("doseid", "৩য়");
                } else if (cur.getString(cur.getColumnIndex("doseId")).equalsIgnoreCase("4")) {
                    map.put("doseid", "৪র্থ");
                } else if (cur.getString(cur.getColumnIndex("doseId")).equalsIgnoreCase("5")) {
                    map.put("doseid", "৫ম ");
                } else if (cur.getString(cur.getColumnIndex("doseId")).equalsIgnoreCase("6")) {
                    map.put("doseid", "৬ষ্ঠ");
                } else if (cur.getString(cur.getColumnIndex("doseId")).equalsIgnoreCase("7")) {
                    map.put("doseid", "৭ম");
                } else if (cur.getString(cur.getColumnIndex("doseId")).equalsIgnoreCase("8")) {
                    map.put("doseid", "৮ম");
                } else if (cur.getString(cur.getColumnIndex("doseId")).equalsIgnoreCase("9")) {
                    map.put("doseid", "৯ম ");
                } else if (cur.getString(cur.getColumnIndex("doseId")).equalsIgnoreCase("10")) {
                    map.put("doseid", "১০ম");
                }


                dataList.add(map);

                dataAdapter = new SimpleAdapter(InjectableView.this, dataList, R.layout.womaninjviewrow, new String[]{"SNo"},
                        new int[]{R.id.Slno});

                list.setAdapter(new DataListAdapter(this));

                cur.moveToNext();
                totalCount += 1;
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(InjectableView.this, e.getMessage());
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
                convertView = inflater.inflate(R.layout.womaninjviewrow, null);
            }
            final TextView Slno = (TextView) convertView.findViewById(R.id.Slno);
            final TableLayout memtab = (TableLayout) convertView.findViewById(R.id.memtab);
            final TextView HHNo = (TextView) convertView.findViewById(R.id.HHNo);
            final TextView Healthid = (TextView) convertView.findViewById(R.id.Healthid);
            final TextView Elcono = (TextView) convertView.findViewById(R.id.Elcono);
            final TextView VName = (TextView) convertView.findViewById(R.id.VName);
            final TextView WName = (TextView) convertView.findViewById(R.id.Name);
            final TextView HName = (TextView) convertView.findViewById(R.id.HName);
            final TextView Age = (TextView) convertView.findViewById(R.id.Age);
            final TextView Mobno = (TextView) convertView.findViewById(R.id.Mobno);
            final TextView DoseId = (TextView) convertView.findViewById(R.id.DoseId);
            final TextView DoseDate = (TextView) convertView.findViewById(R.id.DoseDate);
            final TextView DueDoseDate = (TextView) convertView.findViewById(R.id.DueDoseDate);
            final TextView SideEffect = (TextView) convertView.findViewById(R.id.SideEffect);

            final HashMap<String, String> o = (HashMap<String, String>) dataAdapter.getItem(position);
            Slno.setText(o.get("slno"));
            HHNo.setText(o.get("hhho"));
            Healthid.setText(o.get("healthId"));
            Elcono.setText(o.get("elcono"));
            VName.setText(o.get("vname"));
            WName.setText(o.get("wname"));
            HName.setText(o.get("hname"));
            Age.setText(o.get("wage"));
            Mobno.setText(o.get("mobno"));
            DoseId.setText(o.get("doseid"));
            DoseDate.setText(o.get("dosedate"));
            DueDoseDate.setText(Global.DateConvertDMY(o.get("duedosedate")));
            SideEffect.setText(o.get("sideeffect"));

            memtab.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    g.setCallFrom("1");
                    g.setSLno(o.get("slno"));
                    g.setAVDate(o.get("VDate"));
                    g.setAHHNO(o.get("hhho"));
                    g.setHealthID(o.get("healthId"));
                    //g.setGeneratedId(o.get("generatedId"));
                    g.setGeneratedId(o.get("GeneratedId"));
                    g.setAName(o.get("name"));
                    g.setASex(o.get("sex"));
                    g.setAAge(o.get("age"));
                    g.setAMElco(g.GetElcoNo(C, o.get("GeneratedId")));
                    finish();
                    Intent f1 = new Intent(getApplicationContext(), WomanInjectable.class);
                    startActivity(f1);


                }
            });

            final AlertDialog.Builder adb = new AlertDialog.Builder(InjectableView.this);
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
