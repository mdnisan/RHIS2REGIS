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
 * Created by Nisan on 12/11/2015.
 */
public class Under5ChildView extends Activity {
    SimpleAdapter mSchedule;
    //ArrayList<HashMap<String, String>> mylist   = new ArrayList<HashMap<String, String>>();
    boolean netwoekAvailable = false;
    Location currentLocation;
    double currentLatitude, currentLongitude;
    Location currentLocationNet;
    double currentLatitudeNet, currentLongitudeNet;
    Context con;

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
    ListView list;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.under5childview);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();
            con = this;
            dtpFromDT = (EditText) findViewById(R.id.dtpFromDT);
            dtpFromDT.setText(Global.DateNowDMY());
            btnFromDT = (ImageButton) findViewById(R.id.btnFromDT);
            list = (ListView) findViewById(R.id.lstData);
            View header = getLayoutInflater().inflate(R.layout.under5childheading, null);
            list.addHeaderView(header);
            dtpToDT = (EditText) findViewById(R.id.dtpToDT);
            dtpToDT.setText(Global.DateNowDMY());
            btnToDT = (ImageButton) findViewById(R.id.btnToDT);
          /*  final ListView list = (ListView) findViewById(R.id.lstData);
           */
            //list.addHeaderView(header);
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

            // DataSearch(dtpFromDT.getText().toString(),dtpToDT.getText().toString());

            DataSearch();
            lblTCount.setText(":" + String.valueOf(totalCount));

            DisplaySearch(true);
            // lblTCount.setText( GetTotalDeath(dtpFromDT.getText().toString(),dtpToDT.getText().toString()));
            Button cmdRefresh = (Button) findViewById(R.id.cmdRefresh);
            cmdRefresh.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    totalCount = 0;
                    dataList.clear();
                    DataSearch(dtpFromDT.getText().toString(), dtpToDT.getText().toString());
                    lblTCount.setText(":" + String.valueOf(totalCount));
                    // lblTCount.setText( GetTotalDeath(dtpFromDT.getText().toString(),dtpToDT.getText().toString()));
                    //  TDeath=C.ReturnSingleValue("Select Count(*) as Tdeath from death d where d.providerId='"+ g.getProvCode() +"' and strftime('%d/%m/%Y', date(d.deathDT)) between'"+dtpFromDT.getText().toString()+"' and '"+dtpToDT.getText().toString()+"'");
                    //lblTCount.setText(TDeath);

                }
            });


        } catch (Exception e) {
            Connection.MessageBox(Under5ChildView.this, e.getMessage());
            return;
        }
    }

    private void DisplayAdvanceSearchPopup() {
        final Dialog popupView = new Dialog(Under5ChildView.this);
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
                    Connection.MessageBox(Under5ChildView.this, ex.getMessage());
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

    public void Search(Boolean displayheader) {
       /* ListView list= (ListView) findViewById(R.id.lstData);
        //View header = getLayoutInflater().inflate(R.layout.memberlistheading, null);
        //list.addHeaderView(header);
        //  list.addView(header);*/


        try {
            String SQL = "";
            SQL = "SELECT S.DOB AS DOB,S.Agey AS Agey,S.Agem AS Agem,S.Aged AS Aged,\n" +
                    "       S.NID AS NID,\n" +
                    "       S.BRID AS BRID,\n" +
                    "       S.generatedId AS generatedId,\n" +
                    "       S.SNo AS SNo,\n" +
                    "       S.HHNo AS HHNO,\n" +
                    "       ifnull( S.visitDate, '' ) AS VDate,\n" +
                    "       S.HealthId AS HealthId,\n" +
                    "       S.VName AS VName,\n" +
                    "       S.WNameEng AS WNameEng,\n" +
                    "       S.HusName AS HusName,\n" +
                    "       S.MotherName AS Mother,\n" +
                    "       S.FatherName AS Father,\n" +
                    "       S.WAge AS WAge,\n" +
                    "       S.Mobno AS Mobno,\n" +
                    "       S.Sex AS Sex\n" +
                    "  FROM ( \n" +
                    "    SELECT m.NID AS NID,\n" +
                    "           m.BRID AS BRID,\n" +
                    "           cM.generatedId AS generatedId,\n" +
                    "           m.SNO AS SNO,\n" +
                    "           m.HHNO AS HHNO,\n" +
                    "           e.visitDate AS visitDate,\n" +
                    "           m.HealthId AS HealthId,\n" +
                    "           v.VILLAGENAMEENG AS VName,\n" +
                    "           m.nameeng AS WNameEng,\n" +
                    "           s.nameeng AS HusName,\n" +
                    "           ( CAST ( ( ( julianday( date( 'now' )  ) - julianday( m.DOB )  ) / 30.4 )  AS int ) / 12 ) AS WAge,\n" +
                    "           ifnull( m.DOB, '' ) AS DOB,\n" +
                    "           CAST ( ( ( julianday( date( 'now' )  ) - julianday( m.DOB )  ) / 365 )  AS int ) AS Agey,\n" +
                    "           CAST ( ( ( julianday( date( 'now' )  ) - julianday( m.DOB )  ) / 30.4 )  AS int ) AS Agem,\n" +
                    "           CAST ( ( ( julianday( date( 'now' )  ) - julianday( m.DOB )  )  )  AS int ) AS Aged,\n" +
                    "           ifnull( m.MobileNo1, '' ) AS Mobno,\n" +
                    "           m.Sex AS Sex,\n" +
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
                    "           LEFT JOIN clientMap cM\n" +
                    "                  ON cM.healthId = m.healthId\n" +
                    "           LEFT JOIN under5Child e\n" +
                    "                  ON e.HealthId = cM.generatedId\n" +
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
                    "     WHERE CAST ( ( ( julianday( date( 'now' )  ) - julianday( m.DOB )  ) / 30.4 )  AS int ) / 12 >= 0 \n" +
                    "           AND\n" +
                    "           CAST ( ( ( julianday( date( 'now' )  ) - julianday( m.DOB )  ) / 30.4 )  AS int ) / 12 <= 5\n" +
                    "    UNION\n" +
                    "    SELECT '' AS NID,\n" +
                    "           '' AS BRID,\n" +
                    "           cM.generatedId,\n" +
                    "           '' AS SNo,\n" +
                    "           ifnull( cM.houseGrHoldingNo, '' ) AS HHNO,\n" +
                    "           '' AS VisitDate,\n" +
                    "           ifnull( under5Child.healthId, '' ) AS HealthId,\n" +
                    "           v.VILLAGENAMEENG AS VName,\n" +
                    "           ifnull( cM.name, '' ) AS WNameEng,\n" +
                    "           ifnull( cM.husbandName, '' ) AS HusName,\n" +
                    "           ifnull( cM.Age, '' ) AS WAge,\n" +
                    "           ifnull( cM.dob, '' ) AS DOB,\n" +
                    "           CAST ( ( ( julianday( date( 'now' )  ) - julianday( cM.dob )  ) / 365 )  AS int ) AS Agey,\n" +
                    "           CAST ( ( ( julianday( date( 'now' )  ) - julianday( cM.dob )  ) / 30.4 )  AS int ) AS Agem,\n" +
                    "           CAST ( ( ( julianday( date( 'now' )  ) - julianday( cM.dob )  )  )  AS int ) AS Aged,\n" +
                    "           ifnull( cM.mobileNo, '' ) AS Mobno,\n" +
                    "           cM.Gender AS Sex,\n" +
                    "           cM.motherName AS MotherName,\n" +
                    "           cM.fatherName AS FatherName\n" +
                    "      FROM under5Child\n" +
                    "           INNER JOIN clientMap cM\n" +
                    "                   ON cM.generatedId = under5Child.healthId\n" +
                    "           INNER JOIN Village v\n" +
                    "                   ON v.ZILLAID = cM.ZILLAID \n" +
                    "    AND\n" +
                    "    v.UPAZILAID = cM.UPAZILAID \n" +
                    "    AND\n" +
                    "    v.UNIONID = cM.UNIONID \n" +
                    "    AND\n" +
                    "    v.MOUZAID = cM.MOUZAID \n" +
                    "    AND\n" +
                    "    v.VILLAGEID = cM.VILLAGEID\n" +
                    "     WHERE CAST ( ( ( julianday( date( 'now' )  ) - julianday( cM.DOB )  ) / 30.4 )  AS int ) / 12 >= 0 \n" +
                    "           AND\n" +
                    "           CAST ( ( ( julianday( date( 'now' )  ) - julianday( cM.DOB )  ) / 30.4 )  AS int ) / 12 <= 5 \n" +
                    ") \n" +
                    "AS S";
            //where a.providerId='"+ g.getProvCode()+"'";
   /*         SQLStr = "Select Dist, Upz, UN, Mouza, Vill, ProvType as provtype,ProvCode as provcode, HHNo, SNo as SNo, ifnull(HealthID,'') as HealthID, ifnull(NameEng,'') as NameEng,";
            SQLStr += " ifnull(NameBang,'') as NameBang, ifnull(Rth,'') as Rth, ifnull(HaveNID,'') as HaveNID, ifnull(NID,'') as NID, ifnull(NIDStatus,'') as NIDStatus, ifnull(HaveBR,'') as HaveBR, ifnull(BRID,'') as BRID, ifnull(BRIDStatus,'') as BRIDStatus, ifnull(MobileNo1,'') as MobileNo1,";
            SQLStr += " ifnull(MobileNo2,'') as MobileNo2,ifnull(MobileYN,'')as MobileYN, ifnull(DOB,'') as DOB, Cast(((julianday(date('now'))-julianday(DOB))/365.25) as int) as Age,Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) as AgeM, ifnull(DOBSource,'') as DOBSource, ifnull(BPlace,'') as BPlace, ifnull(FNo,'') as FNo, ifnull(Father,'') as Father, ifnull(MNo,'') as MNo, ifnull(Mother,'') as Mother,";
            SQLStr += " ifnull(Sex,'') as Sex, ifnull(MS,'') as MS, ifnull(SPNO1,'') as SPNO1,ifnull(SPNO2,'') as SPNO2,ifnull(SPNO3,'') as SPNO3,ifnull(SPNO4,'') as SPNO4, ifnull(ELCONo,'') as ELCONo, ifnull(ELCODontKnow,'') as ELCODontKnow, ifnull(EDU,'') as EDU, ifnull(Rel,'') as Rel, ifnull(Nationality,'') as Nationality, ifnull(OCP,'') as OCP,ifnull(ExType,'')as ExType,ProvType as provtype, ProvCode as provcode";
            SQLStr += " from Member ";*/
            // dist='"+ g.getDistrict() +"' and upz='"+ g.getUpazila() +"' and un='"+ g.getUnion() +"' and Mouza='"+ g.getMouza() +"' and vill='"+ g.getVillage() +"' and ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"' and HHNo='"+ g.getHouseholdNo() +"'";

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
                wheresql += " Mobno like '%" + ((EditText) findViewById(R.id.txtMobileSearch)).getText().toString() + "%'";

                wheresql += " OR Mobno like '%" + ((EditText) findViewById(R.id.txtMobileSearch)).getText().toString() + "%'";
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
                wheresql += " Age  like '%" + ((EditText) findViewById(R.id.txtDOBSearch)).getText().toString() + "%'";

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


            */

            if (wheresql.length() > 0) {
                SQL = SQL + " WHERE " + wheresql;
            }
            Cursor cur1 = C.ReadData(SQL);

            cur1.moveToFirst();
            dataList.clear();

            getLayoutInflater().inflate(R.layout.under5childheading, null);
            int totalCount = 0;
            int slno = 0;
            while (!cur1.isAfterLast()) {
                HashMap<String, String> map = new HashMap<String, String>();
                displayheader = false;
                map.put("slno", Integer.toString(slno));
                map.put("VDate", Global.DateConvertDMY(cur1.getString(cur1.getColumnIndex("VDate"))));
                map.put("GeneratedId", cur1.getString(cur1.getColumnIndex("generatedId")));
                map.put("HealthId", cur1.getString(cur1.getColumnIndex("HealthId")));
                // map.put("elcono", cur1.getString(cur1.getColumnIndex("Elcono")));
                //map.put("entrydate", Global.DateConvertDMY(cur1.getString(cur1.getColumnIndex("VDate"))));
                map.put("hhho", cur1.getString(cur1.getColumnIndex("HHNO")));
                map.put("vname", cur1.getString(cur1.getColumnIndex("VName")));
                map.put("name", cur1.getString(cur1.getColumnIndex("WNameEng")));
                map.put("fname", cur1.getString(cur1.getColumnIndex("Father")));
                map.put("mname", cur1.getString(cur1.getColumnIndex("Mother")));
                //map.put("hname", cur1.getString(cur1.getColumnIndex("HusName")));
                map.put("sex", cur1.getString(cur1.getColumnIndex("Sex")));
                map.put("age", cur1.getString(cur1.getColumnIndex("WAge")));

                if (cur1.getString(cur1.getColumnIndex("Sex")).equalsIgnoreCase("1")) {
                    map.put("sex", "পুরুষ");
                } else if (cur1.getString(cur1.getColumnIndex("Sex")).equalsIgnoreCase("2")) {
                    map.put("sex", "মহিলা");
                }


                dataList.add(map);

                dataAdapter = new SimpleAdapter(Under5ChildView.this, dataList, R.layout.under5childrow, new String[]{"SNo", "EntryDate"},
                        new int[]{R.id.Slno, R.id.EntryDate});

                list.setAdapter(new DataListAdapter(this));
                cur1.moveToNext();
                totalCount += 1;
                slno = slno + 1;

            }
            cur1.close();
            ((TextView) findViewById(R.id.lblTCount)).setText(String.valueOf(totalCount) + " টি ডাটা পাওয়া গিয়েছে");
            totalCount = 0;

        } catch (Exception e) {
            Connection.MessageBox(Under5ChildView.this, e.getMessage());
        }

    }


    int totalCount = 0;

    private void DataSearch(String stdt, String enddt) {
        try {
            String SQL = "";
           /* SQL  = "Select distinct u5.healthId as healthId,u5.visitDate as vDate,SNo as SNo,m.HHNo AS HHNo,ifnull(e.elcoNo,'') as Elcono,substr(u5.systemEntryDate,9,2)||'/'||substr(u5.systemEntryDate,6,2)||'/'||substr(u5.systemEntryDate,1,4) AS EntryDate,ifnull(NameEng,'') as NameEng,ifnull(NameBang,'') as NameBang,ifnull(FNo,'') as FNo,";
            SQL += " CASE WHEN cast(FNo as int) = 55 THEN ifnull(Father,'') WHEN cast(FNo as int) = 77 THEN ifnull(Father,'') WHEN cast(FNo as int) = 88 THEN ifnull(Father,'') ELSE (select  NameEng  from member where  ProvCode=(select  ProvCode  from member  Where  healthid ='" + g.getHealthID() + "')";
            SQL += " and HHNo=(select  HHNo  from member  Where  healthid =u5.healthId)";
            SQL += " and SNo=(select  FNo  from member  Where  healthid=u5.healthId)) END AS  Father,";
            SQL += " ifnull(MNo,'') as MNo,ifnull(DOB,'') as DOB, Cast(((julianday(date('now'))-julianday(DOB))/365) as int) as Agey,Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) as Agem,Cast(((julianday(date('now'))-julianday(DOB))) as int) as Aged,";
            SQL += " CASE WHEN cast(MNo as int) = 55 THEN ifnull(Mother,'') WHEN cast(MNo as int) = 77 THEN ifnull(Mother,'') WHEN cast(MNo as int) = 88 THEN ifnull(Mother,'') ELSE ";
            SQL += " (select  NameEng  from member where  ProvCode=(select  ProvCode  from member  Where  healthid =u5.healthId)";
            SQL += " and HHNo=(select  HHNo  from member  Where  healthid=u5.healthId)";
            SQL += " and SNo=(select  MNo  from member  Where  healthid=u5.healthId)) END AS  Mother,";
            SQL += " ifnull(Sex,'') as Sex, ifnull(MS,'') as MS,ifnull(SPNO1,'') as SPNO1,ifnull(SPNO2,'') as SPNO2,v.VILLAGENAMEENG AS VName,";
            SQL += " CASE WHEN cast(SPNO1 as int) = 55 THEN (select  NameEng  from member where  ProvCode=(select  ProvCode  from member Where healthid=u5.healthId)";
            SQL += " and HHNo=(select  HHNo  from member  Where  healthid=u5.healthId)";
            SQL += " and SNo=(select  SPNO1  from member  Where healthid=u5.healthId))";
            SQL += " WHEN cast(SPNO1 as int) = 77 THEN (select  NameEng  from member where  ProvCode=(select  ProvCode  from member Where  healthid=u5.healthId)";
            SQL += " and HHNo=(select  HHNo  from member  Where  healthid=u5.healthId)";
            SQL += " and SNo=(select  SPNO1  from member Where healthid=u5.healthId))";
            SQL += " WHEN cast(SPNO1 as int) = 88 THEN (select  NameEng  from member where  ProvCode=(select  ProvCode  from member  Where  healthid=u5.healthId)";
            SQL += " and HHNo=(select  HHNo  from member  Where  healthid=u5.healthId)";
            SQL += " and SNo=(select  SPNO1  from member Where  healthid=u5.healthId))";
            SQL += " ELSE (select  NameEng  from member where  ProvCode=(select  ProvCode  from member  Where  healthid=u5.healthId)";
            SQL += " and HHNo=(select  HHNo  from member  Where  healthid=u5.healthId)";
            SQL += " and SNo=(select  SPNO1  from member  Where  healthid=u5.healthId)) END AS  HusName,";
            SQL += " (select  Age  from member where ProvCode=(select  ProvCode  from member  Where  healthid=u5.healthId)and HHNo=(select  HHNo  from member  Where  healthid=u5.healthId and ProvCode=u5.providerId)and SNo=(select  SPNO1  from member  Where healthid=u5.healthId))as HusAge from Member m inner join under5Child u5 on u5.healthId=m.healthId left outer join elco e on e.healthId=m.healthId inner join Village v on v.ZILLAID=m.Dist and v.UPAZILAID=m.Upz and v.UNIONID=m.UN and v.MOUZAID=m.Mouza and v.VILLAGEID=m.Vill where u5.providerId='"+ g.getProvCode() +"' and u5.visitDate between'"+dtfrom.toString()+"' and '"+dtto.toString()+"' GROUP BY u5.healthId HAVING  count(u5.healthId)";

*/

            SQL = "Select ifnull(u5.visitDate,'') as vDate,ifnull(cM.healthId,'') as healthId,ifnull(cM.generatedId,'') as generatedId, ";
            SQL += " '' as Elcono,ifnull(u5.systemEntryDate,'') as EntryDate, ifnull(dob,'') as DOB, Cast(((julianday(date('now'))-julianday(dob))/365) as int) as Agey,Cast(((julianday(date('now'))-julianday(dob))/30.4) as int) as Agem,Cast(((julianday(date('now'))-julianday(dob))) as int) as Aged, ";
            SQL += " ifnull(cM.houseGrHoldingNo,'') as HHNo,ifnull(v.VILLAGENAMEENG,'') as VName, ";
            SQL += " ifnull(cM.name,'') as NameEng,ifnull(cM.fatherName,'') as Father,";
            SQL += " ifnull(cM.motherName,'') as Mother,ifnull(cM.husbandName,'') as HusName,";
            SQL += " ifnull(cM.gender,'') as Sex,ifnull(cM.Age,'') as Age from  clientMap cM inner join under5Child u5 on cM.generatedId=u5.healthId  left join Village v on v.ZILLAID=cM.ZILLAID and v.UPAZILAID=cM.UPAZILAID and v.UNIONID=cM.UNIONID ";
            SQL += " and v.MOUZAID=cM.MOUZAID and v.VILLAGEID=cM.VILLAGEID where u5.providerId='" + g.getProvCode() + "' and strftime('%d/%m/%Y', date(u5.visitDate)) between'" + stdt.toString() + "' and '" + enddt.toString() + "' GROUP BY cM.healthId HAVING  count(cM.healthId)"; //+"' and cm.name like '"+"%SHMA%"

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
                map.put("VDate", Global.DateConvertDMY(cur.getString(cur.getColumnIndex("vDate"))));
                map.put("HealthId", cur.getString(cur.getColumnIndex("healthId")));
                map.put("elcono", cur.getString(cur.getColumnIndex("Elcono")));
                map.put("entrydate", Global.DateConvertDMY(cur.getString(cur.getColumnIndex("EntryDate"))));
                map.put("hhho", cur.getString(cur.getColumnIndex("HHNo")));
                map.put("vname", cur.getString(cur.getColumnIndex("VName")));
                map.put("name", cur.getString(cur.getColumnIndex("NameEng")));
                map.put("fname", cur.getString(cur.getColumnIndex("Father")));
                map.put("mname", cur.getString(cur.getColumnIndex("Mother")));
                //  map.put("hname", cur.getString(cur.getColumnIndex("HusName")));
                map.put("sex", cur.getString(cur.getColumnIndex("Sex")));
                map.put("GeneratedId", cur.getString(cur.getColumnIndex("generatedId")));
                if (cur.getString(cur.getColumnIndex("Sex")).equalsIgnoreCase("1")) {
                    map.put("sex", "ছেলে");
                } else if (cur.getString(cur.getColumnIndex("Sex")).equalsIgnoreCase("2")) {
                    map.put("sex", "মেয়ে");
                }

                map.put("dob", cur.getString(cur.getColumnIndex("DOB")));
                map.put("agey", cur.getString(cur.getColumnIndex("Agey")));
                map.put("agem", cur.getString(cur.getColumnIndex("Agem")));
                map.put("aged", cur.getString(cur.getColumnIndex("Aged")));


                dataList.add(map);

                dataAdapter = new SimpleAdapter(Under5ChildView.this, dataList, R.layout.under5childrow, new String[]{"SNo", "EntryDate"},
                        new int[]{R.id.Slno, R.id.EntryDate});

                list.setAdapter(new DataListAdapter(this));

                cur.moveToNext();
                totalCount += 1;
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(Under5ChildView.this, e.getMessage());
            return;
        }
    }

    private void DataSearch() {
        try {
            String SQL = "";
           /* SQL  = "Select distinct u5.healthId as healthId,u5.visitDate as vDate,SNo as SNo,m.HHNo AS HHNo,ifnull(e.elcoNo,'') as Elcono,substr(u5.systemEntryDate,9,2)||'/'||substr(u5.systemEntryDate,6,2)||'/'||substr(u5.systemEntryDate,1,4) AS EntryDate,ifnull(NameEng,'') as NameEng,ifnull(NameBang,'') as NameBang,ifnull(FNo,'') as FNo,";
            SQL += " CASE WHEN cast(FNo as int) = 55 THEN ifnull(Father,'') WHEN cast(FNo as int) = 77 THEN ifnull(Father,'') WHEN cast(FNo as int) = 88 THEN ifnull(Father,'') ELSE (select  NameEng  from member where  ProvCode=(select  ProvCode  from member  Where  healthid ='" + g.getHealthID() + "')";
            SQL += " and HHNo=(select  HHNo  from member  Where  healthid =u5.healthId)";
            SQL += " and SNo=(select  FNo  from member  Where  healthid=u5.healthId)) END AS  Father,";
            SQL += " ifnull(MNo,'') as MNo,ifnull(DOB,'') as DOB, Cast(((julianday(date('now'))-julianday(DOB))/365) as int) as Agey,Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) as Agem,Cast(((julianday(date('now'))-julianday(DOB))) as int) as Aged,";
            SQL += " CASE WHEN cast(MNo as int) = 55 THEN ifnull(Mother,'') WHEN cast(MNo as int) = 77 THEN ifnull(Mother,'') WHEN cast(MNo as int) = 88 THEN ifnull(Mother,'') ELSE ";
            SQL += " (select  NameEng  from member where  ProvCode=(select  ProvCode  from member  Where  healthid =u5.healthId)";
            SQL += " and HHNo=(select  HHNo  from member  Where  healthid=u5.healthId)";
            SQL += " and SNo=(select  MNo  from member  Where  healthid=u5.healthId)) END AS  Mother,";
            SQL += " ifnull(Sex,'') as Sex, ifnull(MS,'') as MS,ifnull(SPNO1,'') as SPNO1,ifnull(SPNO2,'') as SPNO2,v.VILLAGENAMEENG AS VName,";
            SQL += " CASE WHEN cast(SPNO1 as int) = 55 THEN (select  NameEng  from member where  ProvCode=(select  ProvCode  from member Where healthid=u5.healthId)";
            SQL += " and HHNo=(select  HHNo  from member  Where  healthid=u5.healthId)";
            SQL += " and SNo=(select  SPNO1  from member  Where healthid=u5.healthId))";
            SQL += " WHEN cast(SPNO1 as int) = 77 THEN (select  NameEng  from member where  ProvCode=(select  ProvCode  from member Where  healthid=u5.healthId)";
            SQL += " and HHNo=(select  HHNo  from member  Where  healthid=u5.healthId)";
            SQL += " and SNo=(select  SPNO1  from member Where healthid=u5.healthId))";
            SQL += " WHEN cast(SPNO1 as int) = 88 THEN (select  NameEng  from member where  ProvCode=(select  ProvCode  from member  Where  healthid=u5.healthId)";
            SQL += " and HHNo=(select  HHNo  from member  Where  healthid=u5.healthId)";
            SQL += " and SNo=(select  SPNO1  from member Where  healthid=u5.healthId))";
            SQL += " ELSE (select  NameEng  from member where  ProvCode=(select  ProvCode  from member  Where  healthid=u5.healthId)";
            SQL += " and HHNo=(select  HHNo  from member  Where  healthid=u5.healthId)";
            SQL += " and SNo=(select  SPNO1  from member  Where  healthid=u5.healthId)) END AS  HusName,";
            SQL += " (select  Age  from member where ProvCode=(select  ProvCode  from member  Where  healthid=u5.healthId)and HHNo=(select  HHNo  from member  Where  healthid=u5.healthId and ProvCode=u5.providerId)and SNo=(select  SPNO1  from member  Where healthid=u5.healthId))as HusAge from Member m inner join under5Child u5 on u5.healthId=m.healthId left outer join elco e on e.healthId=m.healthId inner join Village v on v.ZILLAID=m.Dist and v.UPAZILAID=m.Upz and v.UNIONID=m.UN and v.MOUZAID=m.Mouza and v.VILLAGEID=m.Vill where u5.providerId='"+ g.getProvCode()+"' GROUP BY u5.healthId HAVING  count(u5.healthId)";
*/

            SQL = "Select ifnull(u5.visitDate,'') as vDate,ifnull(cM.healthId,'') as healthId,ifnull(cM.generatedId,'') as generatedId, ";
            SQL += " '' as Elcono,ifnull(u5.systemEntryDate,'') as EntryDate, ifnull(dob,'') as DOB, Cast(((julianday(date('now'))-julianday(dob))/365) as int) as Agey,Cast(((julianday(date('now'))-julianday(dob))/30.4) as int) as Agem,Cast(((julianday(date('now'))-julianday(dob))) as int) as Aged, ";
            SQL += " ifnull(cM.houseGrHoldingNo,'') as HHNo,ifnull(v.VILLAGENAMEENG,'') as VName, ";
            SQL += " ifnull(cM.name,'') as NameEng,ifnull(cM.fatherName,'') as Father,";
            SQL += " ifnull(cM.motherName,'') as Mother,ifnull(cM.husbandName,'') as HusName,";
            SQL += " ifnull(cM.gender,'') as Sex,ifnull(cM.Age,'') as Age from  clientMap cM inner join under5Child u5 on cM.generatedId=u5.healthId  left join Village v on v.ZILLAID=cM.ZILLAID and v.UPAZILAID=cM.UPAZILAID and v.UNIONID=cM.UNIONID ";
            SQL += " and v.MOUZAID=cM.MOUZAID and v.VILLAGEID=cM.VILLAGEID where u5.providerId='" + g.getProvCode() + "' GROUP BY cM.healthId HAVING  count(cM.healthId)";
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
                map.put("VDate", Global.DateConvertDMY(cur.getString(cur.getColumnIndex("vDate"))));
                map.put("GeneratedId", cur.getString(cur.getColumnIndex("generatedId")));
                map.put("HealthId", cur.getString(cur.getColumnIndex("healthId")));
                map.put("elcono", cur.getString(cur.getColumnIndex("Elcono")));
                map.put("entrydate", Global.DateConvertDMY(cur.getString(cur.getColumnIndex("EntryDate"))));
                map.put("hhho", cur.getString(cur.getColumnIndex("HHNo")));
                map.put("vname", cur.getString(cur.getColumnIndex("VName")));
                map.put("name", cur.getString(cur.getColumnIndex("NameEng")));
                map.put("fname", cur.getString(cur.getColumnIndex("Father")));
                map.put("mname", cur.getString(cur.getColumnIndex("Mother")));
                //  map.put("hname", cur.getString(cur.getColumnIndex("HusName")));
                map.put("sex", cur.getString(cur.getColumnIndex("Sex")));

                if (cur.getString(cur.getColumnIndex("Sex")).equalsIgnoreCase("1")) {
                    map.put("sex", "ছেলে");
                } else if (cur.getString(cur.getColumnIndex("Sex")).equalsIgnoreCase("2")) {
                    map.put("sex", "মেয়ে");
                }

                map.put("dob", cur.getString(cur.getColumnIndex("DOB")));
                map.put("agey", cur.getString(cur.getColumnIndex("Agey")));
                map.put("agem", cur.getString(cur.getColumnIndex("Agem")));
                map.put("aged", cur.getString(cur.getColumnIndex("Aged")));


                dataList.add(map);

                dataAdapter = new SimpleAdapter(Under5ChildView.this, dataList, R.layout.under5childrow, new String[]{"SNo", "EntryDate"},
                        new int[]{R.id.Slno, R.id.EntryDate});

                list.setAdapter(new DataListAdapter(this));

                cur.moveToNext();
                totalCount += 1;
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(Under5ChildView.this, e.getMessage());
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
                convertView = inflater.inflate(R.layout.under5childrow, null);
            }
            final TextView Slno = (TextView) convertView.findViewById(R.id.Slno);

            final TableLayout memtab = (TableLayout) convertView.findViewById(R.id.memtab);
            // final TextView Elcono = (TextView)convertView.findViewById(R.id.Elcono);
            final TextView HealthId = (TextView) convertView.findViewById(R.id.HealthId);
            final TextView EntryDate = (TextView) convertView.findViewById(R.id.EntryDate);
            // final TextView HHNo = (TextView)convertView.findViewById(R.id.HHNo);
            //  final TextView VName = (TextView)convertView.findViewById(R.id.VName);
            final TextView Name = (TextView) convertView.findViewById(R.id.Name);
            final TextView FName = (TextView) convertView.findViewById(R.id.FName);
            final TextView MName = (TextView) convertView.findViewById(R.id.MName);
            // final TextView HName = (TextView)convertView.findViewById(R.id.HName);
            final TextView Sex = (TextView) convertView.findViewById(R.id.Sex);
            final TextView Age = (TextView) convertView.findViewById(R.id.Age);

            final HashMap<String, String> o = (HashMap<String, String>) dataAdapter.getItem(position);
            Slno.setText(o.get("slno"));

            HealthId.setText(o.get("HealthId"));
            EntryDate.setText(o.get("VDate"));
            // HHNo.setText(o.get("hhho"));
            // Elcono.setText(o.get("elcono"));
            // VName.setText(o.get("vname"));
            Name.setText(o.get("name"));
            FName.setText(o.get("fname"));
            MName.setText(o.get("mname"));
            // HName.setText(o.get("hname"));
            Sex.setText(o.get("sex"));
            // Age.setText(o.get("agey"));
            Age.setText(o.get("agey") + " (বছর)");

          /*  int D=0;
            int Ag=0;
            String Dob="";
            Dob= Global.DateConvertDMY(o.get("dob"));
            D=Global.DateDifferenceDays(Global.DateNowDMY(),Dob);
            Ag=Integer.valueOf(o.get("agey"));
            if(D<60 & Ag<1)
            {
                Age.setText(String.valueOf(D)+" (দিন)");


            }
           else  if(D>60 & Ag<1)
            {
                Age.setText(String.valueOf(D/30)+" (মাস)");

            }
            else if(Ag>=1)
            {
                Age.setText(o.get("agey")+" (বছর)");

            }*/


            memtab.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    g.setCallFrom("1");
                    g.setSLno(o.get("slno"));
                    g.setAVDate(o.get("VDate"));
                    g.setAHHNO(o.get("hhho"));
                    g.setHealthID(o.get("HealthId"));
                    g.setGeneratedId(o.get("GeneratedId"));
                    g.setAName(o.get("name"));
                    g.setASex(o.get("sex"));
                    g.setAAge(o.get("agem"));
                    g.setAgeY(o.get("agey"));
                    g.setAgeM(o.get("agem"));
                    g.setAgeD(o.get("aged"));
                    finish();
                    Intent f1 = new Intent(getApplicationContext(), Under5child.class);
                    startActivity(f1);


                }
            });
            final AlertDialog.Builder adb = new AlertDialog.Builder(Under5ChildView.this);
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
