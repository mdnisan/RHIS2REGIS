package com.data.rhis2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
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
 * Created by Nisan on 10/7/2015.
 */
public class Elcoview extends Activity {
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
    ListView list;
    Spinner spnProvider;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.elcoview);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();
            spnProvider = (Spinner) findViewById(R.id.spnProvider);
            String Type = "";
            if (g.getProvType().equals("10")) {
                Type = "3";
            } else if (g.getProvType().equals("11")) {
                Type = "2";
            } else if (g.getProvType().equals("12")) {
                Type = "2";
            }

            ((Spinner) findViewById(R.id.spnProvider)).setAdapter(C.getArrayAdapter("Select '-' provName from ProviderDb Union Select provCode||'-'||provName from ProviderDb WHERE provType='" + Type + "' AND provCode ='" + g.getFWAProvCode() + "'"));


            dtpFromDT = (EditText) findViewById(R.id.dtpFromDT);
            // dtpFromDT.setText(Global.DateNowDMY());
            btnFromDT = (ImageButton) findViewById(R.id.btnFromDT);

            dtpToDT = (EditText) findViewById(R.id.dtpToDT);
            //dtpToDT.setText(Global.DateNowDMY());
            btnToDT = (ImageButton) findViewById(R.id.btnToDT);
            list = (ListView) findViewById(R.id.lstData);
            //View header = getLayoutInflater().inflate(R.layout.elcoviewheading, null);
            //  list.addHeaderView(header);
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
            spnProvider.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    if (spnProvider.getSelectedItemPosition() == 0) {
                        ((Spinner) findViewById(R.id.txtVillSearch1)).setAdapter(C.getArrayAdapter("Select '  ' VillageName from Village union Select ' All ' VillageName from Village"));

                    } else if (spnProvider.getSelectedItemPosition() > 0) {
                        String Type = "";
                        if (g.getProvType().equals("10")) {
                            Type = "3";
                        } else if (g.getProvType().equals("11")) {
                            Type = "2";
                        } else if (g.getProvType().equals("12")) {
                            Type = "2";
                        }

                        ((Spinner) findViewById(R.id.txtVillSearch1)).setAdapter(C.getArrayAdapter("Select '  ' VillageName from Village union Select ' All ' VillageName from Village  union select  substr('0' || v.MOUZAID , -3, 3)||substr('0' || v.VILLAGEID, -3, 3)||'-'||v.VILLAGENAMEENG as VILLAGENAME from Village  v INNER JOIN ProviderArea PA ON PA.zillaid = v.zillaid " +
                                "AND PA.upazilaid = v.UPAZILAID " +
                                "AND PA.unionid = v.UNIONID " +
                                "AND PA.mouzaid = v.MOUZAID " +
                                "AND PA.villageid = v.VILLAGEID " +
                                "AND PA.ProvCode = '" + Global.Left(spnProvider.getSelectedItem().toString(), 5) +//"'"));
                                "' AND PA.ProvType ='" + Type + "'"));
                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });

            //DataSearch();
            // ((Spinner) findViewById(R.id.txtVillSearch1)).setAdapter(C.getArrayAdapter("Select '  ' VillageName from Village union Select ' All ' VillageName from Village union select  substr('0' || MOUZAID , -3, 3)||substr('0' || VILLAGEID, -3, 3)||'-'||VILLAGENAMEENG VillageName from Village"));
            // where ZIlLAID='"+ desval+"' and UPAZILAID='"+ upval+"' and UNIONID='"+ uval +"'
/*            ((Spinner) findViewById(R.id.txtVillSearch1)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    list.setAdapter(null);
                    String val = (((Spinner) findViewById(R.id.txtVillSearch1)).getSelectedItemPosition() == 0 ? "" : Global.Left(((Spinner) findViewById(R.id.txtVillSearch1)).getSelectedItem().toString(), 3));
                    String villageCode="";
                    if(!val.equalsIgnoreCase("All")&!val.equalsIgnoreCase(""))
                    {
                        String vill = (((Spinner) findViewById(R.id.txtVillSearch1)).getSelectedItemPosition() == 0 ? "" : Global.Left(((Spinner) findViewById(R.id.txtVillSearch1)).getSelectedItem().toString(), 5));
                        villageCode=vill.toString().substring(3,5);
                    }
                    if (val.length()> 0) {
                        totalCount = 0;
                        dataList.clear();
                        list.setAdapter(null);
                        SearchVillageWise(val,villageCode);
                        lblTCount.setText(":"+String.valueOf(totalCount)+"");

                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });*/
            lblTCount.setText(":" + String.valueOf(totalCount));
            Button cmdRefresh = (Button) findViewById(R.id.cmdRefresh);
            cmdRefresh.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    totalCount = 0;
                    list.setAdapter(null);
                    String val = (((Spinner) findViewById(R.id.txtVillSearch1)).getSelectedItemPosition() == 0 ? "" : Global.Left(((Spinner) findViewById(R.id.txtVillSearch1)).getSelectedItem().toString(), 3));
                    String villageCode = "";
                    if (!val.equalsIgnoreCase("All") & !val.equalsIgnoreCase("")) {
                        String vill = (((Spinner) findViewById(R.id.txtVillSearch1)).getSelectedItemPosition() == 0 ? "" : Global.Left(((Spinner) findViewById(R.id.txtVillSearch1)).getSelectedItem().toString(), 5));
                        villageCode = vill.toString().substring(3, 5);
                    }
                    if (val.length() > 0) {
                        totalCount = 0;
                        dataList.clear();
                        list.setAdapter(null);
                        SearchVillageWise(val, villageCode);
                        lblTCount.setText(":" + String.valueOf(totalCount) + "");

                    }
                    if (val.length() == 0) {
                        totalCount = 0;
                        dataList.clear();
                        list.setAdapter(null);
                        dtpFromDT.setText("");
                        dtpToDT.setText("");
                        lblTCount.setText(":" + String.valueOf(totalCount) + "");

                    }
                    lblTCount.setText(":" + String.valueOf(totalCount));
                }
            });

            DisplaySearch(true);


        } catch (Exception e) {
            Connection.MessageBox(Elcoview.this, e.getMessage());
            return;
        }
    }

    private void DefaultDistUpzUN() {
        ((Spinner) findViewById(R.id.txtDistrictSearch)).setAdapter(C.getArrayAdapter("Select ZILLAID||'-'||ZILLANAME from zilla where ZIlLAID='" + g.getDistrict() + "'"));
        ((Spinner) findViewById(R.id.txtupazillaSearch)).setAdapter(C.getArrayAdapter("Select  substr('0' ||UPAZILAID, -2, 2)||'-'||UPAZILANAME from upazila where zillaid = '" + g.getDistrict() + "'"));
        ((Spinner) findViewById(R.id.txtunionSearch)).setAdapter(C.getArrayAdapter("Select substr('0' ||UNIONID, -2, 2)||'-'||UNIONNAME FROM UNIONS where zillaid = '" + g.getDistrict() + "' and UPAZILAID ='" + g.getUpazila() + "'"));
        ((Spinner) findViewById(R.id.txtVillSearch)).setAdapter(C.getArrayAdapter("Select '  ' VillageName from Village union Select substr('0' || MOUZAID , -3, 3)||'-'||VILLAGEID||'-'||VILLAGENAMEENG VillageName from Village where zillaid = '" + g.getDistrict() + "' and UPAZILAID ='" + g.getUpazila() + "' and  UNIONID='" + g.getUnion() + "'"));

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
                    //  ((Spinner) findViewById(R.id.txtVillSearch)).setAdapter(C.getArrayAdapter("Select '  ' VillageName from Village union select  substr('0' || MOUZAID , -3, 3)||'-'||VILLAGEID||'-'||VILLAGENAMEENG VillageName from Village where ZIlLAID='"+ desval+"' and UPAZILAID='"+ upval+"' and UNIONID='"+ uval +"'"));

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

     /*   ((Spinner) findViewById(R.id.txtVillSearch)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/
      /*  String Zil=C.ReturnSingleValue("Select zillaid  from ProviderDB");
        String UpZil=C.ReturnSingleValue("Select upazilaid  from ProviderDB");
        String Un=C.ReturnSingleValue("Select unionid  from ProviderDB");
        ((Spinner) findViewById(R.id.txtVillSearch)).setAdapter(C.getArrayAdapter("Select '  ' VillageName from Village union select substr('0' || VILLAGEID, -2, 2)||'-'||VILLAGENAMEENG VillageName from Village where ZIlLAID='"+ Zil+"' and UPAZILAID='"+ UpZil+"' and UNIONID='"+ Un +"'"));
*/

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
                    Connection.MessageBox(Elcoview.this, ex.getMessage());
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
        final Dialog popupView = new Dialog(Elcoview.this);
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
            ((CheckBox) popupView.findViewById(R.id.Vlbldistrict)).setChecked(true);

        }


        if (((LinearLayout) findViewById(R.id.secUnion)).getVisibility() == View.VISIBLE) {
            ((CheckBox) popupView.findViewById(R.id.Vlblunion)).setChecked(true);
            ((CheckBox) popupView.findViewById(R.id.Vlblupazilla)).setChecked(true);
            ((CheckBox) popupView.findViewById(R.id.Vlbldistrict)).setChecked(true);
        }


        if (((LinearLayout) findViewById(R.id.secVillSearch)).getVisibility() == View.VISIBLE) {
            ((CheckBox) popupView.findViewById(R.id.VlblVillSearch)).setChecked(true);
            ((CheckBox) popupView.findViewById(R.id.Vlblunion)).setChecked(true);
            ((CheckBox) popupView.findViewById(R.id.Vlblupazilla)).setChecked(true);
            ((CheckBox) popupView.findViewById(R.id.Vlbldistrict)).setChecked(true);
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
                    DefaultDistUpzUN();
                } else {
                    ((LinearLayout) findViewById(R.id.secDistrict)).setVisibility(View.GONE);
                }

                if (((CheckBox) popupView.findViewById(R.id.Vlblupazilla)).isChecked()) {

                    ((LinearLayout) findViewById(R.id.secDistrict)).setVisibility(View.VISIBLE);
                    ((LinearLayout) findViewById(R.id.secUpazilla)).setVisibility(View.VISIBLE);
                    DefaultDistUpzUN();

                } else {
                    ((LinearLayout) findViewById(R.id.secUpazilla)).setVisibility(View.GONE);
                }


                if (((CheckBox) popupView.findViewById(R.id.Vlblunion)).isChecked()) {
                    ((LinearLayout) findViewById(R.id.secUnion)).setVisibility(View.VISIBLE);
                    ((LinearLayout) findViewById(R.id.secDistrict)).setVisibility(View.VISIBLE);
                    ((LinearLayout) findViewById(R.id.secUpazilla)).setVisibility(View.VISIBLE);
                    DefaultDistUpzUN();
                } else {
                    ((LinearLayout) findViewById(R.id.secUnion)).setVisibility(View.GONE);
                }

                if (((CheckBox) popupView.findViewById(R.id.VlblVillSearch)).isChecked()) {
                    ((LinearLayout) findViewById(R.id.secVillSearch)).setVisibility(View.VISIBLE);
                    ((LinearLayout) findViewById(R.id.secUnion)).setVisibility(View.VISIBLE);
                    ((LinearLayout) findViewById(R.id.secDistrict)).setVisibility(View.VISIBLE);
                    ((LinearLayout) findViewById(R.id.secUpazilla)).setVisibility(View.VISIBLE);
                    DefaultDistUpzUN();
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
            String SQL = "SELECT S.Dist AS Dist,\n" +
                    "       S.Upz AS Upz,\n" +
                    "       S.UN AS UN,\n" +
                    "       S.Mouza AS Mouza,\n" +
                    "       S.Vill AS Vill,\n" +
                    "       S.NID AS NID,\n" +
                    "       S.BRID AS BRID,\n" +
                    "       S.generatedId AS generatedId,\n" +
                    "       S.SNo AS SNo,\n" +
                    "       S.HHNo AS HHNO,\n" +
                    "       S.Elcono AS Elcono,\n" +
                    "       S.HealthId AS HealthId,\n" +
                    "       S.VName AS VName,\n" +
                    "       S.WNameEng AS WNameEng,\n" +
                    "       S.HusName AS HusName,\n" +
                    "       S.providerId AS providerId,\n" +
                    "       S.MotherName AS Mother,\n" +
                    "       S.FatherName AS Father,\n" +
                    "       S.WAge AS WAge,\n" +
                    "       S.MDate AS MDate,\n" +
                    "       S.Mobno AS Mobno,\n" +
                    "       S.Son AS Son,\n" +
                    "       S.Dau AS Dau,\n" +
                    "       S.Edu AS Edu,\n" +
                    "       S.eFPIhid as eFPIhid,\n" +
                    "       S.DOB AS DoB\n" +
                    "  FROM ( \n" +
                    "    SELECT m.dist,\n" +
                    "           m.Upz,\n" +
                    "           m.UN,\n" +
                    "           m.Mouza AS Mouza,\n" +
                    "           m.Vill AS Vill,\n" +
                    "           m.NID AS NID,\n" +
                    "           m.BRID AS BRID,\n" +
                    "           cM.generatedId AS generatedId,\n" +
                    "           m.SNO AS SNO,\n" +
                    "           m.HHNO AS HHNO,\n" +
                    "           e.Elcono AS Elcono,\n" +
                    "           m.HealthId AS HealthId,\n" +
                    "           v.VILLAGENAMEENG AS VName,\n" +
                    "           m.nameeng AS WNameEng,\n" +
                    "           s.nameeng AS HusName,\n" +
                    "           e.providerId AS providerId,\n" +
                    "           m.DOB AS dob,\n" +
                    "           ( CAST ( ( ( julianday( date( 'now' )  ) - julianday( m.DOB )  ) / 30.4 )  AS int ) / 12 ) AS WAge,\n" +
                    "           ifnull( e.marrDate, '' ) AS MDate,\n" +
                    "           ifnull( m.MobileNo1, '' ) AS Mobno,\n" +
                    "           ifnull( e.son, '' ) AS Son,\n" +
                    "           ifnull( e.dau, '' ) AS Dau,\n" +
                    "           ifnull( m.EDU, '' ) AS Edu,\n" +
                    "           ( CASE\n" +
                    "           WHEN efpi.HealthId IS NOT NULL THEN 'Y'\n" +
                    "           ELSE '' END ) AS eFPIhid,\n" +
                    "           ( CASE\n" +
                    "                    WHEN CAST ( mo.MNo AS int ) = 55 \n" +
                    "               OR\n" +
                    "               CAST ( mo.MNo AS int ) = 77 \n" +
                    "               OR\n" +
                    "               CAST ( mo.MNo AS int ) = 88 THEN m.Mother \n" +
                    "                    ELSE ifnull( mo.nameeng, '' ) \n" +
                    "           END ) AS MotherName,\n" +
                    "           ( CASE\n" +
                    "                    WHEN CAST ( m.FNo AS int ) = 55 \n" +
                    "               OR\n" +
                    "               CAST ( m.FNo AS int ) = 77 \n" +
                    "               OR\n" +
                    "               CAST ( m.FNo AS int ) = 88 THEN m.Father \n" +
                    "                    ELSE ifnull( f.nameeng, '' ) \n" +
                    "           END ) AS FatherName\n" +
                    "      FROM Member m\n" +
                    "           LEFT OUTER JOIN Member f\n" +
                    "                        ON m.dist = f.dist \n" +
                    "    AND\n" +
                    "    m.upz = f.upz \n" +
                    "    AND\n" +
                    "    m.un = f.un \n" +
                    "    AND\n" +
                    "    m.mouza = f.mouza \n" +
                    "    AND\n" +
                    "    m.vill = f.vill \n" +
                    "    AND\n" +
                    "    m.hhno = f.hhno \n" +
                    "    AND\n" +
                    "    m.fno = f.sno\n" +
                    "           LEFT OUTER JOIN Member mo\n" +
                    "                        ON m.dist = mo.dist \n" +
                    "    AND\n" +
                    "    m.upz = mo.upz \n" +
                    "    AND\n" +
                    "    m.un = mo.un \n" +
                    "    AND\n" +
                    "    m.mouza = mo.mouza \n" +
                    "    AND\n" +
                    "    m.vill = mo.vill \n" +
                    "    AND\n" +
                    "    m.hhno = mo.hhno \n" +
                    "    AND\n" +
                    "    m.mno = mo.sno\n" +
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
                    "           LEFT JOIN ELCO e\n" +
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
                    "     WHERE CAST ( ( ( julianday( date( 'now' )  ) - julianday( m.DOB )  ) / 30.4 )  AS int ) / 12 >= 15 \n" +
                    "           AND\n" +
                    "           CAST ( ( ( julianday( date( 'now' )  ) - julianday( m.DOB )  ) / 30.4 )  AS int ) / 12 <= 49 \n" +
                    "           AND\n" +
                    "           m.Sex = 2 \n" +
                    "           AND\n" +
                    "           m.MS = 2 AND\n" +
                    "           e.Elcono is not null order by m.Vill,case when cast(e.Elcono as int) IS NULL THEN 1 ELSE 0 END asc,cast(e.Elcono as int) asc\n" +
                    "   \n" +
                    ") \n" +
                    "AS S \n" +
                    "                    \n" +
                    "                    \n" +
                    "\n" +
                    "                    \n" +
                    "                    \n" +
                    " \n" +
                    " ";

            String wheresql = "";
            // +"' and m.upz='"+
            // wheresql +=g.getProvCode();
            if (((EditText) findViewById(R.id.txtHealthIDSearch)).getText().toString().length() > 0) {
                wheresql += " healthId like '%" + ((EditText) findViewById(R.id.txtHealthIDSearch)).getText().toString().trim() + "%'";
            }
            if (((EditText) findViewById(R.id.txtELCONoSearch)).getText().toString().length() > 0) {
                if (wheresql.length() > 0) {
                    wheresql += " AND ";
                }
                wheresql += " Elcono like '" + ((EditText) findViewById(R.id.txtELCONoSearch)).getText().toString() + "'";
            }
            if (((EditText) findViewById(R.id.txtMobileSearch)).getText().toString().length() > 0) {
                if (wheresql.length() > 0) {
                    wheresql += " AND ";
                }
                wheresql += " Mobno like '%" + ((EditText) findViewById(R.id.txtMobileSearch)).getText().toString() + "%'";

                wheresql += " OR Mobno like '%" + ((EditText) findViewById(R.id.txtMobileSearch)).getText().toString() + "%'";

               /* wheresql += " MobileNo1 like '%" + ((EditText) findViewById(R.id.txtMobileSearch)).getText().toString() + "%'";

                wheresql += " OR MobileNo2 like '%" + ((EditText) findViewById(R.id.txtMobileSearch)).getText().toString() + "%'";
         */
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

            String dist = "";
            dist = ((Spinner) findViewById(R.id.txtDistrictSearch)).getSelectedItemPosition() == 0 ? "" : Global.Left(((Spinner) findViewById(R.id.txtDistrictSearch)).getSelectedItem().toString(), 2);
            if (dist.length() > 0) {
                if (wheresql.length() > 0) {
                    wheresql += " AND ";
                }
                wheresql += " Dist like '%" + Integer.valueOf(dist) + "%'";
            }


            String upz = "";
            if (((LinearLayout) findViewById(R.id.secUpazilla)).getVisibility() == View.VISIBLE) {
                upz = ((Spinner) findViewById(R.id.txtupazillaSearch)).getSelectedItemPosition() == 0 ? "" : Global.Left(((Spinner) findViewById(R.id.txtupazillaSearch)).getSelectedItem().toString(), 2);

                if (upz.length() > 0) {
                    if (wheresql.length() > 0) {
                        wheresql += " AND ";
                    }
                    wheresql += " Upz like '%" + Integer.valueOf(upz) + "%'";
                }
            }

            String union = "";
            if (((LinearLayout) findViewById(R.id.secUnion)).getVisibility() == View.VISIBLE) {
                union = ((Spinner) findViewById(R.id.txtunionSearch)).getSelectedItemPosition() == 0 ? "" : Global.Left(((Spinner) findViewById(R.id.txtunionSearch)).getSelectedItem().toString(), 2);

                if (union.length() > 0) {
                    if (wheresql.length() > 0) {
                        wheresql += " AND ";
                    }
                    wheresql += " UN like '%" + Integer.valueOf(union) + "%'";
                }
            }
            String vil = "";
            // String moz="";
            if (((LinearLayout) findViewById(R.id.secVillSearch)).getVisibility() == View.VISIBLE) {

                vil = ((Spinner) findViewById(R.id.txtVillSearch)).getSelectedItemPosition() == 0 ? "" : Global.Left(((Spinner) findViewById(R.id.txtVillSearch)).getSelectedItem().toString(), 3);
                // vil = ((Spinner) findViewById(R.id.txtVillSearch)).getSelectedItemPosition() == 0 ? "" : Global.Mid(((Spinner) findViewById(R.id.txtVillSearch)).getSelectedItem().toString(),5,1);

                if (vil.length() > 0) {
                    if (wheresql.length() > 0) {

                        wheresql += " AND ";

                        //wheresql += " AND ";
                    }
                    wheresql += " Mouza like '%" + Integer.valueOf(vil) + "%'";
                    //wheresql += " Vill like '%" + vil + "%'";
                }


            }

          /*







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
            // ListView list = (ListView)findViewById(R.id.lstData);
            list.setAdapter(null);
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
                map.put("mother", cur.getString(cur.getColumnIndex("Mother")));
                map.put("father", cur.getString(cur.getColumnIndex("Father")));
                map.put("wage", cur.getString(cur.getColumnIndex("WAge")));
                map.put("mdate", cur.getString(cur.getColumnIndex("MDate")));
                map.put("mobno", cur.getString(cur.getColumnIndex("Mobno")));
                map.put("son", cur.getString(cur.getColumnIndex("Son")));
                map.put("dau", cur.getString(cur.getColumnIndex("Dau")));
                map.put("edu", cur.getString(cur.getColumnIndex("Edu")));
                map.put("eFPIhid", cur.getString(cur.getColumnIndex("eFPIhid")).replace("null", ""));
                map.put("GeneratedId", cur.getString(cur.getColumnIndex("generatedId")));

                map.put("dist", cur.getString(cur.getColumnIndex("Dist")));
                map.put("upz", cur.getString(cur.getColumnIndex("Upz")));
                map.put("un", cur.getString(cur.getColumnIndex("UN")));
                map.put("mouza", cur.getString(cur.getColumnIndex("Mouza")));
                map.put("vill", cur.getString(cur.getColumnIndex("Vill")));
                if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("99")) {
                    map.put("edu", "শিক্ষাগত যোগ্যতা নেই");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("01")) {
                    map.put("edu", "১ম শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("02")) {
                    map.put("edu", "২য় শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("03")) {
                    map.put("edu", "৩য় শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("04")) {
                    map.put("edu", "৪র্থ শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("05")) {
                    map.put("edu", "৫ম শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("06")) {
                    map.put("edu", "৬ষ্ঠ শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("07")) {
                    map.put("edu", "৭ম শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("08")) {
                    map.put("edu", "৮ম শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("09")) {
                    map.put("edu", "৯ম শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("10")) {
                    map.put("edu", "মাধ্যমিক বা সমতুল্য");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("11")) {
                    map.put("edu", "উচ্চ মাধ্যমিক বা সমতুল্য");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("12")) {
                    map.put("edu", "স্নাতক বা সমতুল্য");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("13")) {
                    map.put("edu", "স্নাতকোত্তর বা সমতুল্য");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("14")) {
                    map.put("edu", "ডাক্তারি");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("15")) {
                    map.put("edu", "ইঞ্জিনিয়ারিং");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("16")) {
                    map.put("edu", "বৃত্তিমুলক শিক্ষা");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("17")) {
                    map.put("edu", "কারিগরি শিক্ষা");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("18")) {
                    map.put("edu", "ধাত্রীবিদ্যা/নার্সিং");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("19")) {
                    map.put("edu", "অন্যান্যা");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("77")) {
                    map.put("edu", "প্রযোজ্য নয়");
                }


                dataList.add(map);

                dataAdapter = new SimpleAdapter(Elcoview.this, dataList, R.layout.elcoviewrow, new String[]{"SNo", "EntryDate"},
                        new int[]{R.id.Slno, R.id.EntryDate});

                list.setAdapter(new DataListAdapter(this));

                cur.moveToNext();
                totalCount += 1;
            }
            cur.close();
            /*((TextView)findViewById(R.id.lblTCount)).setText("মোট "+ String.valueOf(slno)+ " টি ডাটা পাওয়া গিয়েছে" );*/
            ((TextView) findViewById(R.id.lblTCount)).setText(String.valueOf(slno));

        } catch (Exception e) {
            Connection.MessageBox(Elcoview.this, e.getMessage());
        }

    }

    public void SearchVillageWise(String Mouza, String Village) {

        try {

            String SQL = "";

            Integer val = (((Spinner) findViewById(R.id.txtVillSearch1)).getSelectedItemPosition());// == 0 ? "" : Global.Left(((Spinner) findViewById(R.id.txtVillSearch1)).getSelectedItem().toString(), 3));
            Integer val1 = (((Spinner) findViewById(R.id.spnProvider)).getSelectedItemPosition());
            if (val == 0) {


            } else if (val == 1 && val1 == 1) {
                //dtpFromDT.setText("");
                //dtpToDT.setText("");
                SQL = "SELECT S.Dist AS Dist,\n" +
                        "       S.Upz AS Upz,\n" +
                        "       S.UN AS UN,\n" +
                        "       S.Mouza AS Mouza,\n" +
                        "       S.Vill AS Vill,\n" +
                        "       S.NID AS NID,\n" +
                        "       S.BRID AS BRID,\n" +
                        "       S.generatedId AS generatedId,\n" +
                        "       S.SNo AS SNo,\n" +
                        "       S.HHNo AS HHNO,\n" +
                        "       S.Elcono AS Elcono,\n" +
                        "       S.HealthId AS HealthId,\n" +
                        "       S.VName AS VName,\n" +
                        "       S.WNameEng AS WNameEng,\n" +
                        "       S.HusName AS HusName,\n" +
                        "       S.providerId AS providerId,\n" +
                        "       S.MotherName AS Mother,\n" +
                        "       S.FatherName AS Father,\n" +
                        "       S.WAge AS WAge,\n" +
                        "       S.MDate AS MDate,\n" +
                        "       S.Mobno AS Mobno,\n" +
                        "       S.Son AS Son,\n" +
                        "       S.Dau AS Dau,\n" +
                        "       S.Edu AS Edu,\n" +
                        "       S.eFPIhid as eFPIhid,\n" +
                        "       S.DOB AS DoB\n" +
                        "  FROM ( \n" +
                        "    SELECT m.dist,\n" +
                        "           m.Upz,\n" +
                        "           m.UN,\n" +
                        "           m.Mouza AS Mouza,\n" +
                        "           m.Vill AS Vill,\n" +
                        "           m.NID AS NID,\n" +
                        "           m.BRID AS BRID,\n" +
                        "           cM.generatedId AS generatedId,\n" +
                        "           m.SNO AS SNO,\n" +
                        "           m.HHNO AS HHNO,\n" +
                        "           e.Elcono AS Elcono,\n" +
                        "           m.HealthId AS HealthId,\n" +
                        "           v.VILLAGENAMEENG AS VName,\n" +
                        "           m.nameeng AS WNameEng,\n" +
                        "           s.nameeng AS HusName,\n" +
                        "           e.providerId AS providerId,\n" +
                        "           m.DOB AS dob,\n" +
                        "           ( CAST ( ( ( julianday( date( 'now' )  ) - julianday( m.DOB )  ) / 30.4 )  AS int ) / 12 ) AS WAge,\n" +
                        "           ifnull( e.marrDate, '' ) AS MDate,\n" +
                        "           ifnull( m.MobileNo1, '' ) AS Mobno,\n" +
                        "           ifnull( e.son, '' ) AS Son,\n" +
                        "           ifnull( e.dau, '' ) AS Dau,\n" +
                        "           ifnull( m.EDU, '' ) AS Edu,\n" +
                        "           ( CASE\n" +
                        "           WHEN efpi.HealthId IS NOT NULL THEN 'Y'\n" +
                        "           ELSE '' END ) AS eFPIhid,\n" +
                        "           ( CASE\n" +
                        "                    WHEN CAST ( m.MNo AS int ) = 55 \n" +
                        "               OR\n" +
                        "               CAST ( m.MNo AS int ) =88 \n" +
                        "               OR\n" +
                        "               CAST ( m.MNo AS int ) =77  THEN m.Mother \n" +
                        "                    ELSE ifnull( mo.nameeng, '' ) \n" +
                        "           END ) AS MotherName,\n" +
                        "           ( CASE\n" +
                        "                    WHEN CAST ( m.FNo AS int ) = 55 \n" +
                        "               OR\n" +
                        "               CAST ( m.FNo AS int ) = 88 \n" +
                        "               OR\n" +
                        "               CAST ( m.FNo AS int ) = 77  THEN m.Father \n" +
                        "                    ELSE ifnull( f.nameeng, '' ) \n" +
                        "           END ) AS FatherName,e.regDT\n" +
                        "      FROM Member m\n" +
                        "           LEFT OUTER JOIN Member f\n" +
                        "                        ON m.dist = f.dist \n" +
                        "    AND\n" +
                        "    m.upz = f.upz \n" +
                        "    AND\n" +
                        "    m.un = f.un \n" +
                        "    AND\n" +
                        "    m.mouza = f.mouza \n" +
                        "    AND\n" +
                        "    m.vill = f.vill \n" +
                        "    AND\n" +
                        "    m.hhno = f.hhno \n" +
                        "    AND\n" +
                        "    m.fno = f.sno\n" +
                        "           LEFT OUTER JOIN Member mo\n" +
                        "                        ON m.dist = mo.dist \n" +
                        "    AND\n" +
                        "    m.upz = mo.upz \n" +
                        "    AND\n" +
                        "    m.un = mo.un \n" +
                        "    AND\n" +
                        "    m.mouza = mo.mouza \n" +
                        "    AND\n" +
                        "    m.vill = mo.vill \n" +
                        "    AND\n" +
                        "    m.hhno = mo.hhno \n" +
                        "    AND\n" +
                        "    m.mno = mo.sno\n" +
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
                        "           LEFT JOIN ELCO e\n" +
                        "                  ON e.HealthId = cM.generatedId\n" +
                        "           LEFT JOIN elcoFPI efpi\n" +
                        "                   ON efpi.HealthId = cM.generatedId\n" +
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
                        "           m.MS = 2 AND\n" +
                        "           e.Elcono is not null order by m.Vill, case when cast(e.Elcono as int) IS NULL THEN 1 ELSE 0 END asc,cast(e.Elcono as int) asc\n" +
                        "   \n" +
                        ") \n" +
                        "AS S \n" +
                        "                    \n" +
                        "                    \n" +
                        "\n" +
                        "                    \n" +
                        "                    \n" +
                        " \n" +
                        " ";
                String wheresql = " Dist like '%" + g.getDistrict() + "%' AND  Upz like '%" + g.getUpazila() + "%' AND  UN like '%" + g.getUnion() + "%' AND  Mouza in(Select mouzaid from ProviderArea where ProvCode='" + Global.Left(spnProvider.getSelectedItem().toString(), 5) + "')";

                if (((LinearLayout) findViewById(R.id.secDeathDT)).getVisibility() == View.VISIBLE) {

                    if (dtpFromDT.getText().toString().length() == 0 & dtpToDT.getText().toString().length() == 0) {

                        /*if(dtpFromDT.getText().toString().length()==0)
                        {
                            Connection.MessageBox(Elcoview.this, "শুরুর তারিখ  প্রবেশ করুন ");
                            dtpFromDT.requestFocus();
                            return;
                        }
                        else  if(dtpToDT.getText().toString().length()==0)
                        {
                            Connection.MessageBox(Elcoview.this, "শেষের তারিখ প্রবেশ করুন ");
                            dtpToDT.requestFocus();
                            return;
                        }*/
                    } else {
                        if (wheresql.length() > 0) {

                            wheresql += " AND ";

                            //wheresql += " AND ";
                        }
                        String Y = "";
                        String M = "";
                        String D = "";
                        String Y1 = "";
                        String M1 = "";
                        String D1 = "";
                        Y = Global.Right(dtpFromDT.getText().toString(), 4);
                        D = Global.Left(dtpFromDT.getText().toString(), 2);
                        M = Global.Mid(dtpFromDT.getText().toString(), 3, 5);

                        Y1 = Global.Right(dtpToDT.getText().toString(), 4);
                        D1 = Global.Left(dtpToDT.getText().toString(), 2);
                        M1 = Global.Mid(dtpToDT.getText().toString(), 3, 5);
                        //DataSearch(Y+"-"+M+"-"+D,Y1+"-"+M1+"-"+D1);
                        wheresql += " regDT between'" + Y + "-" + M + "-" + D + "' and '" + Y1 + "-" + M1 + "-" + D1 + "'";
                    }


                }
                if (wheresql.length() > 0) {
                    SQL = SQL + " WHERE " + wheresql;
                }
            } else if (val == 1) {
                //dtpFromDT.setText("");
                //dtpToDT.setText("");
                SQL = "SELECT S.Dist AS Dist,\n" +
                        "       S.Upz AS Upz,\n" +
                        "       S.UN AS UN,\n" +
                        "       S.Mouza AS Mouza,\n" +
                        "       S.Vill AS Vill,\n" +
                        "       S.NID AS NID,\n" +
                        "       S.BRID AS BRID,\n" +
                        "       S.generatedId AS generatedId,\n" +
                        "       S.SNo AS SNo,\n" +
                        "       S.HHNo AS HHNO,\n" +
                        "       S.Elcono AS Elcono,\n" +
                        "       S.HealthId AS HealthId,\n" +
                        "       S.VName AS VName,\n" +
                        "       S.WNameEng AS WNameEng,\n" +
                        "       S.HusName AS HusName,\n" +
                        "       S.providerId AS providerId,\n" +
                        "       S.MotherName AS Mother,\n" +
                        "       S.FatherName AS Father,\n" +
                        "       S.WAge AS WAge,\n" +
                        "       S.MDate AS MDate,\n" +
                        "       S.Mobno AS Mobno,\n" +
                        "       S.Son AS Son,\n" +
                        "       S.Dau AS Dau,\n" +
                        "       S.Edu AS Edu,\n" +
                        "       S.eFPIhid as eFPIhid,\n" +
                        "       S.DOB AS DoB\n" +
                        "  FROM ( \n" +
                        "    SELECT m.dist,\n" +
                        "           m.Upz,\n" +
                        "           m.UN,\n" +
                        "           m.Mouza AS Mouza,\n" +
                        "           m.Vill AS Vill,\n" +
                        "           m.NID AS NID,\n" +
                        "           m.BRID AS BRID,\n" +
                        "           cM.generatedId AS generatedId,\n" +
                        "           m.SNO AS SNO,\n" +
                        "           m.HHNO AS HHNO,\n" +
                        "           e.Elcono AS Elcono,\n" +
                        "           m.HealthId AS HealthId,\n" +
                        "           v.VILLAGENAMEENG AS VName,\n" +
                        "           m.nameeng AS WNameEng,\n" +
                        "           s.nameeng AS HusName,\n" +
                        "           e.providerId AS providerId,\n" +
                        "           m.DOB AS dob,\n" +
                        "           ( CAST ( ( ( julianday( date( 'now' )  ) - julianday( m.DOB )  ) / 30.4 )  AS int ) / 12 ) AS WAge,\n" +
                        "           ifnull( e.marrDate, '' ) AS MDate,\n" +
                        "           ifnull( m.MobileNo1, '' ) AS Mobno,\n" +
                        "           ifnull( e.son, '' ) AS Son,\n" +
                        "           ifnull( e.dau, '' ) AS Dau,\n" +
                        "           ifnull( m.EDU, '' ) AS Edu,\n" +
                        "           ( CASE\n" +
                        "           WHEN efpi.HealthId IS NOT NULL THEN 'Y'\n" +
                        "           ELSE '' END ) AS eFPIhid,\n" +
                        "           ( CASE\n" +
                        "                    WHEN CAST ( m.MNo AS int ) = 55 \n" +
                        "               OR\n" +
                        "               CAST ( m.MNo AS int ) =88 \n" +
                        "               OR\n" +
                        "               CAST ( m.MNo AS int ) =77  THEN m.Mother \n" +
                        "                    ELSE ifnull( mo.nameeng, '' ) \n" +
                        "           END ) AS MotherName,\n" +
                        "           ( CASE\n" +
                        "                    WHEN CAST ( m.FNo AS int ) = 55 \n" +
                        "               OR\n" +
                        "               CAST ( m.FNo AS int ) = 88 \n" +
                        "               OR\n" +
                        "               CAST ( m.FNo AS int ) = 77  THEN m.Father \n" +
                        "                    ELSE ifnull( f.nameeng, '' ) \n" +
                        "           END ) AS FatherName,e.regDT\n" +
                        "      FROM Member m\n" +
                        "           LEFT OUTER JOIN Member f\n" +
                        "                        ON m.dist = f.dist \n" +
                        "    AND\n" +
                        "    m.upz = f.upz \n" +
                        "    AND\n" +
                        "    m.un = f.un \n" +
                        "    AND\n" +
                        "    m.mouza = f.mouza \n" +
                        "    AND\n" +
                        "    m.vill = f.vill \n" +
                        "    AND\n" +
                        "    m.hhno = f.hhno \n" +
                        "    AND\n" +
                        "    m.fno = f.sno\n" +
                        "           LEFT OUTER JOIN Member mo\n" +
                        "                        ON m.dist = mo.dist \n" +
                        "    AND\n" +
                        "    m.upz = mo.upz \n" +
                        "    AND\n" +
                        "    m.un = mo.un \n" +
                        "    AND\n" +
                        "    m.mouza = mo.mouza \n" +
                        "    AND\n" +
                        "    m.vill = mo.vill \n" +
                        "    AND\n" +
                        "    m.hhno = mo.hhno \n" +
                        "    AND\n" +
                        "    m.mno = mo.sno\n" +
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
                        "           LEFT JOIN ELCO e\n" +
                        "                  ON e.HealthId = cM.generatedId\n" +
                        "           LEFT JOIN elcoFPI efpi\n" +
                        "                   ON efpi.HealthId = cM.generatedId\n" +
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
                        "           m.MS = 2 AND\n" +
                        "           e.Elcono is not null order by m.Vill, case when cast(e.Elcono as int) IS NULL THEN 1 ELSE 0 END asc,cast(e.Elcono as int) asc\n" +
                        "   \n" +
                        ") \n" +
                        "AS S \n" +
                        "                    \n" +
                        "                    \n" +
                        "\n" +
                        "                    \n" +
                        "                    \n" +
                        " \n" +
                        " ";
                String wheresql = " Dist like '%" + g.getDistrict() + "%' AND  Upz like '%" + g.getUpazila() + "%' AND  UN like '%" + g.getUnion() + "%'";

                if (((LinearLayout) findViewById(R.id.secDeathDT)).getVisibility() == View.VISIBLE) {

                    if (dtpFromDT.getText().toString().length() == 0 & dtpToDT.getText().toString().length() == 0) {

                        /*if(dtpFromDT.getText().toString().length()==0)
                        {
                            Connection.MessageBox(Elcoview.this, "শুরুর তারিখ  প্রবেশ করুন ");
                            dtpFromDT.requestFocus();
                            return;
                        }
                        else  if(dtpToDT.getText().toString().length()==0)
                        {
                            Connection.MessageBox(Elcoview.this, "শেষের তারিখ প্রবেশ করুন ");
                            dtpToDT.requestFocus();
                            return;
                        }*/
                    } else {
                        if (wheresql.length() > 0) {

                            wheresql += " AND ";

                            //wheresql += " AND ";
                        }
                        String Y = "";
                        String M = "";
                        String D = "";
                        String Y1 = "";
                        String M1 = "";
                        String D1 = "";
                        Y = Global.Right(dtpFromDT.getText().toString(), 4);
                        D = Global.Left(dtpFromDT.getText().toString(), 2);
                        M = Global.Mid(dtpFromDT.getText().toString(), 3, 5);

                        Y1 = Global.Right(dtpToDT.getText().toString(), 4);
                        D1 = Global.Left(dtpToDT.getText().toString(), 2);
                        M1 = Global.Mid(dtpToDT.getText().toString(), 3, 5);
                        //DataSearch(Y+"-"+M+"-"+D,Y1+"-"+M1+"-"+D1);
                        wheresql += " regDT between'" + Y + "-" + M + "-" + D + "' and '" + Y1 + "-" + M1 + "-" + D1 + "'";
                    }


                }
                if (wheresql.length() > 0) {
                    SQL = SQL + " WHERE " + wheresql;
                }
            } else if (val > 1) {

                /*if(dtpFromDT.getText().toString().length()==0)
                {
                    Connection.MessageBox(Elcoview.this, "শুরুর তারিখ  প্রবেশ করুন ");
                    dtpFromDT.requestFocus();
                    return;
                }
                else  if(dtpToDT.getText().toString().length()==0)
                {
                    Connection.MessageBox(Elcoview.this, "শেষের তারিখ প্রবেশ করুন ");
                    dtpToDT.requestFocus();
                    return;
                }*/

                SQL = "SELECT S.Dist AS Dist,\n" +
                        "       S.Upz AS Upz,\n" +
                        "       S.UN AS UN,\n" +
                        "       S.Mouza AS Mouza,\n" +
                        "       S.Vill AS Vill,\n" +
                        "       S.NID AS NID,\n" +
                        "       S.BRID AS BRID,\n" +
                        "       S.generatedId AS generatedId,\n" +
                        "       S.SNo AS SNo,\n" +
                        "       S.HHNo AS HHNO,\n" +
                        "       S.Elcono AS Elcono,\n" +
                        "       S.HealthId AS HealthId,\n" +
                        "       S.VName AS VName,\n" +
                        "       S.WNameEng AS WNameEng,\n" +
                        "       S.HusName AS HusName,\n" +
                        "       S.providerId AS providerId,\n" +
                        "       S.MotherName AS Mother,\n" +
                        "       S.FatherName AS Father,\n" +
                        "       S.WAge AS WAge,\n" +
                        "       S.MDate AS MDate,\n" +
                        "       S.Mobno AS Mobno,\n" +
                        "       S.Son AS Son,\n" +
                        "       S.Dau AS Dau,\n" +
                        "       S.Edu AS Edu,\n" +
                        "       S.eFPIhid as eFPIhid,\n" +
                        "       S.DOB AS DoB\n" +
                        "  FROM ( \n" +
                        "    SELECT m.dist,\n" +
                        "           m.Upz,\n" +
                        "           m.UN,\n" +
                        "           m.Mouza AS Mouza,\n" +
                        "           m.Vill AS Vill,\n" +
                        "           m.NID AS NID,\n" +
                        "           m.BRID AS BRID,\n" +
                        "           cM.generatedId AS generatedId,\n" +
                        "           m.SNO AS SNO,\n" +
                        "           m.HHNO AS HHNO,\n" +
                        "           e.Elcono AS Elcono,\n" +
                        "           m.HealthId AS HealthId,\n" +
                        "           v.VILLAGENAMEENG AS VName,\n" +
                        "           m.nameeng AS WNameEng,\n" +
                        "           s.nameeng AS HusName,\n" +
                        "           e.providerId AS providerId,\n" +
                        "           m.DOB AS dob,\n" +
                        "           ( CAST ( ( ( julianday( date( 'now' )  ) - julianday( m.DOB )  ) / 30.4 )  AS int ) / 12 ) AS WAge,\n" +
                        "           ifnull( e.marrDate, '' ) AS MDate,\n" +
                        "           ifnull( m.MobileNo1, '' ) AS Mobno,\n" +
                        "           ifnull( e.son, '' ) AS Son,\n" +
                        "           ifnull( e.dau, '' ) AS Dau,\n" +
                        "           ifnull( m.EDU, '' ) AS Edu,\n" +
                        "           ( CASE\n" +
                        "           WHEN efpi.HealthId IS NOT NULL THEN 'Y'\n" +
                        "           ELSE '' END ) AS eFPIhid,\n" +
                        "           ( CASE\n" +
                        "                    WHEN CAST ( m.MNo AS int ) = 55 \n" +
                        "               OR\n" +
                        "               CAST ( m.MNo AS int ) =88 \n" +
                        "               OR\n" +
                        "               CAST ( m.MNo AS int ) =77  THEN m.Mother \n" +
                        "                    ELSE ifnull( mo.nameeng, '' ) \n" +
                        "           END ) AS MotherName,\n" +
                        "           ( CASE\n" +
                        "                    WHEN CAST ( m.FNo AS int ) = 55 \n" +
                        "               OR\n" +
                        "               CAST ( m.FNo AS int ) = 88 \n" +
                        "               OR\n" +
                        "               CAST ( m.FNo AS int ) = 77  THEN m.Father \n" +
                        "                    ELSE ifnull( f.nameeng, '' ) \n" +
                        "           END ) AS FatherName,e.regDT\n" +
                        "      FROM Member m\n" +
                        "           LEFT OUTER JOIN Member f\n" +
                        "                        ON m.dist = f.dist \n" +
                        "    AND\n" +
                        "    m.upz = f.upz \n" +
                        "    AND\n" +
                        "    m.un = f.un \n" +
                        "    AND\n" +
                        "    m.mouza = f.mouza \n" +
                        "    AND\n" +
                        "    m.vill = f.vill \n" +
                        "    AND\n" +
                        "    m.hhno = f.hhno \n" +
                        "    AND\n" +
                        "    m.fno = f.sno\n" +
                        "           LEFT OUTER JOIN Member mo\n" +
                        "                        ON m.dist = mo.dist \n" +
                        "    AND\n" +
                        "    m.upz = mo.upz \n" +
                        "    AND\n" +
                        "    m.un = mo.un \n" +
                        "    AND\n" +
                        "    m.mouza = mo.mouza \n" +
                        "    AND\n" +
                        "    m.vill = mo.vill \n" +
                        "    AND\n" +
                        "    m.hhno = mo.hhno \n" +
                        "    AND\n" +
                        "    m.mno = mo.sno\n" +
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
                        "           LEFT JOIN ELCO e\n" +
                        "                  ON e.HealthId = cM.generatedId\n" +
                        "           LEFT JOIN elcoFPI efpi\n" +
                        "                   ON efpi.HealthId = cM.generatedId\n" +
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
                        "           m.MS = 2 AND\n" +
                        "           e.Elcono is not null  order by m.Vill, case when cast(e.Elcono as int) IS NULL THEN 1 ELSE 0 END asc,cast(e.Elcono as int) asc \n" +
                        "   \n" +
                        ") \n" +
                        "AS S \n" +
                        "                    \n" +
                        "                    \n" +
                        "\n" +
                        "                    \n" +
                        "                    \n" +
                        " \n" +
                        " ";
                String wheresql = " Dist like '%" + g.getDistrict() + "%' AND  Upz like '%" + g.getUpazila() + "%' AND  UN like '%" + g.getUnion() + "%' AND Mouza like '%" + Integer.valueOf(Mouza) + "%' AND Vill like '%" + Integer.valueOf(Village) + "%'";
                if (((LinearLayout) findViewById(R.id.secDeathDT)).getVisibility() == View.VISIBLE) {

                    if (dtpFromDT.getText().toString().length() == 0 & dtpToDT.getText().toString().length() == 0) {

                        //wheresql += " Vill like '%" + vil + "%'";
                    } else {
                        if (wheresql.length() > 0) {

                            wheresql += " AND ";

                            //wheresql += " AND ";
                        }
                        String Y = "";
                        String M = "";
                        String D = "";
                        String Y1 = "";
                        String M1 = "";
                        String D1 = "";
                        Y = Global.Right(dtpFromDT.getText().toString(), 4);
                        D = Global.Left(dtpFromDT.getText().toString(), 2);
                        M = Global.Mid(dtpFromDT.getText().toString(), 3, 5);

                        Y1 = Global.Right(dtpToDT.getText().toString(), 4);
                        D1 = Global.Left(dtpToDT.getText().toString(), 2);
                        M1 = Global.Mid(dtpToDT.getText().toString(), 3, 5);
                        //DataSearch(Y+"-"+M+"-"+D,Y1+"-"+M1+"-"+D1);
                        wheresql += " regDT between'" + Y + "-" + M + "-" + D + "' and '" + Y1 + "-" + M1 + "-" + D1 + "'";
                    }


                }
                if (wheresql.length() > 0) {
                    SQL = SQL + " WHERE " + wheresql;
                }
            }


            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();
            dataList = new ArrayList<HashMap<String, String>>();

            int slno = 0;
            // ListView list = (ListView)findViewById(R.id.lstData);
            list.setAdapter(null);
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
                map.put("mother", cur.getString(cur.getColumnIndex("Mother")));
                map.put("father", cur.getString(cur.getColumnIndex("Father")));
                map.put("wage", cur.getString(cur.getColumnIndex("WAge")));
                map.put("mdate", cur.getString(cur.getColumnIndex("MDate")));
                map.put("mobno", cur.getString(cur.getColumnIndex("Mobno")));
                map.put("son", cur.getString(cur.getColumnIndex("Son")));
                map.put("dau", cur.getString(cur.getColumnIndex("Dau")));
                map.put("edu", cur.getString(cur.getColumnIndex("Edu")));
                map.put("eFPIhid", cur.getString(cur.getColumnIndex("eFPIhid")).replace("null", ""));
                map.put("GeneratedId", cur.getString(cur.getColumnIndex("generatedId")));

                map.put("dist", cur.getString(cur.getColumnIndex("Dist")));
                map.put("upz", cur.getString(cur.getColumnIndex("Upz")));
                map.put("un", cur.getString(cur.getColumnIndex("UN")));
                map.put("mouza", cur.getString(cur.getColumnIndex("Mouza")));
                map.put("vill", cur.getString(cur.getColumnIndex("Vill")));
                if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("99")) {
                    map.put("edu", "শিক্ষাগত যোগ্যতা নেই");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("01")) {
                    map.put("edu", "১ম শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("02")) {
                    map.put("edu", "২য় শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("03")) {
                    map.put("edu", "৩য় শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("04")) {
                    map.put("edu", "৪র্থ শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("05")) {
                    map.put("edu", "৫ম শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("06")) {
                    map.put("edu", "৬ষ্ঠ শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("07")) {
                    map.put("edu", "৭ম শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("08")) {
                    map.put("edu", "৮ম শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("09")) {
                    map.put("edu", "৯ম শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("10")) {
                    map.put("edu", "মাধ্যমিক বা সমতুল্য");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("11")) {
                    map.put("edu", "উচ্চ মাধ্যমিক বা সমতুল্য");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("12")) {
                    map.put("edu", "স্নাতক বা সমতুল্য");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("13")) {
                    map.put("edu", "স্নাতকোত্তর বা সমতুল্য");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("14")) {
                    map.put("edu", "ডাক্তারি");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("15")) {
                    map.put("edu", "ইঞ্জিনিয়ারিং");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("16")) {
                    map.put("edu", "বৃত্তিমুলক শিক্ষা");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("17")) {
                    map.put("edu", "কারিগরি শিক্ষা");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("18")) {
                    map.put("edu", "ধাত্রীবিদ্যা/নার্সিং");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("19")) {
                    map.put("edu", "অন্যান্যা");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("77")) {
                    map.put("edu", "প্রযোজ্য নয়");
                }


                dataList.add(map);

                dataAdapter = new SimpleAdapter(Elcoview.this, dataList, R.layout.elcoviewrow, new String[]{"SNo", "EntryDate"},
                        new int[]{R.id.Slno, R.id.EntryDate});

                list.setAdapter(new DataListAdapter(this));

                cur.moveToNext();
                totalCount += 1;
            }
            cur.close();
            /*((TextView)findViewById(R.id.lblTCount)).setText("মোট "+ String.valueOf(slno)+ " টি ডাটা পাওয়া গিয়েছে" );*/
            ((TextView) findViewById(R.id.lblTCount)).setText(String.valueOf(slno));

        } catch (Exception e) {
            Connection.MessageBox(Elcoview.this, e.getMessage());
        }

    }

    private String GetTotalElco(String ProvCode) {
        //C.ReturnSingleValue("Select Count(*) as Tdeath from death d where d.providerId='"+ g.getProvCode() +"' and strftime('%d/%m/%Y', date(d.deathDT)) between'"+dtpFromDT.getText().toString()+"' and '"+dtpToDT.getText().toString()+"'");
        String sq = "Select Count(*) as elco from elco  where providerId='" + g.getProvCode() + "'";
        return C.ReturnSingleValue(sq);
    }

    private String GetTotalElco(String dtfrom, String dtto) {
        //C.ReturnSingleValue("Select Count(*) as Tdeath from death d where d.providerId='"+ g.getProvCode() +"' and strftime('%d/%m/%Y', date(d.deathDT)) between'"+dtpFromDT.getText().toString()+"' and '"+dtpToDT.getText().toString()+"'");
        String sq = "Select Count(*) as Tdeath from elco e where e.providerId='" + g.getProvCode() + "' and strftime('%d/%m/%Y', date(e.regDT)) between'" + dtfrom + "' and '" + dtto + "'";
        return C.ReturnSingleValue(sq);
    }

    int totalCount = 0;

    private void DataSearch(String stdt, String enddt) {
        try {
            String SQL = "";

            SQL = "Select ifnull(SNo,'') as SNo,ifnull(m.HHNo,'') AS HHNo,ifnull(e.elcoNo,'') as Elcono,ifnull(e.healthId,'') as HealthId,v.VILLAGENAMEENG AS VName,";
            SQL += " ifnull(NameEng,'') as WNameEng,ifnull(NameBang,'') as NameBang,";

            SQL += " CASE WHEN cast(SPNO1 as int) = 55 THEN (select  NameEng  from member where  ProvCode=(select  ProvCode  from member Where healthid=e.healthId) and HHNo=(select  HHNo  from member  Where  healthid=e.healthId) and SNo=(select  SPNO1  from member  Where healthid=e.healthId)) WHEN cast(SPNO1 as int) = 77 THEN (select  NameEng  from member where  ProvCode=(select  ProvCode  from member Where  healthid=e.healthId) and HHNo=(select  HHNo  from member  Where  healthid=e.healthId) and SNo=(select  SPNO1  from member Where healthid=e.healthId)) WHEN cast(SPNO1 as int) = 88 THEN (select  NameEng  from member where  ProvCode=(select  ProvCode  from member Where  healthid=e.healthId) and HHNo=(select  HHNo  from member  Where healthid=e.healthId) and SNo=(select  SPNO1  from member Where  healthid=e.healthId)) ELSE (select  NameEng  from member where  ProvCode=(select  ProvCode  from member  Where healthid=e.healthId) and HHNo=(select  HHNo  from member  Where  healthid=e.healthId) and SNo=(select  SPNO1  from member  Where  healthid=e.healthId)) END AS  HusName,";
            SQL += " ifnull(m.Age,'') as WAge,ifnull(e.marrDate,'') as MDate,ifnull(m.MobileNo1,'') as Mobno,ifnull(e.son,'') as Son,ifnull(e.dau,'') as Dau,ifnull(EDU,'') AS Edu from  clientMap cm inner join Member m on cM.healthId=m.healthId  left outer join elco e on e.healthId=cM.generatedId  inner join Village v on v.ZILLAID=m.Dist and v.UPAZILAID=m.Upz and v.UNIONID=m.UN and v.MOUZAID=m.Mouza and v.VILLAGEID=m.Vill where e.healthId = cM.generatedId  and strftime('%d/%m/%Y', date(e.regDT)) between'" + stdt.toString() + "' and '" + enddt.toString() + "'";
            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();
            dataList.clear();
            int slno = 0;
            list.setAdapter(null);
            while (!cur.isAfterLast()) {
                slno += 1;
                HashMap<String, String> map = new HashMap<String, String>();
                //  ListView list = (ListView)findViewById(R.id.lstData);
                map.put("slno", Integer.toString(slno));
                map.put("hhho", cur.getString(cur.getColumnIndex("HHNo")));
                map.put("healthId", cur.getString(cur.getColumnIndex("HealthId")));
                map.put("elcono", cur.getString(cur.getColumnIndex("Elcono")));
                map.put("vname", cur.getString(cur.getColumnIndex("VName")));
                map.put("wname", cur.getString(cur.getColumnIndex("WNameEng")));
                map.put("hname", cur.getString(cur.getColumnIndex("HusName")));
                map.put("wage", cur.getString(cur.getColumnIndex("WAge")));
                map.put("mdate", cur.getString(cur.getColumnIndex("MDate")));
                map.put("mobno", cur.getString(cur.getColumnIndex("Mobno")));
                map.put("son", cur.getString(cur.getColumnIndex("Son")));
                map.put("dau", cur.getString(cur.getColumnIndex("Dau")));
                map.put("edu", cur.getString(cur.getColumnIndex("Edu")));
                map.put("eFPIhid", cur.getString(cur.getColumnIndex("eFPIhid")).replace("null", ""));
                map.put("GeneratedId", cur.getString(cur.getColumnIndex("generatedId")));


                if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("99")) {
                    map.put("edu", "শিক্ষাগত যোগ্যতা নেই");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("01")) {
                    map.put("edu", "১ম শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("02")) {
                    map.put("edu", "২য় শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("03")) {
                    map.put("edu", "৩য় শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("04")) {
                    map.put("edu", "৪র্থ শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("05")) {
                    map.put("edu", "৫ম শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("06")) {
                    map.put("edu", "৬ষ্ঠ শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("07")) {
                    map.put("edu", "৭ম শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("08")) {
                    map.put("edu", "৮ম শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("09")) {
                    map.put("edu", "৯ম শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("10")) {
                    map.put("edu", "মাধ্যমিক বা সমতুল্য");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("11")) {
                    map.put("edu", "উচ্চ মাধ্যমিক বা সমতুল্য");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("12")) {
                    map.put("edu", "স্নাতক বা সমতুল্য");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("13")) {
                    map.put("edu", "স্নাতকোত্তর বা সমতুল্য");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("14")) {
                    map.put("edu", "ডাক্তারি");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("15")) {
                    map.put("edu", "ইঞ্জিনিয়ারিং");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("16")) {
                    map.put("edu", "বৃত্তিমুলক শিক্ষা");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("17")) {
                    map.put("edu", "কারিগরি শিক্ষা");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("18")) {
                    map.put("edu", "ধাত্রীবিদ্যা/নার্সিং");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("19")) {
                    map.put("edu", "অন্যান্যা");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("77")) {
                    map.put("edu", "প্রযোজ্য নয়");
                }


                dataList.add(map);

                dataAdapter = new SimpleAdapter(Elcoview.this, dataList, R.layout.deathrow, new String[]{"SNo", "EntryDate"},
                        new int[]{R.id.Slno, R.id.EntryDate});

                list.setAdapter(new DataListAdapter(this));

                cur.moveToNext();
                totalCount += 1;
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(Elcoview.this, e.getMessage());
            return;
        }
    }

    private void DataSearch() {
        try {
            String SQL = "";

            /*SQL  = "Select ifnull(SNo,'') as SNo,ifnull(m.HHNo,'') AS HHNo,ifnull(e.elcoNo,'') as Elcono,ifnull(e.healthId,'') as HealthId,v.VILLAGENAMEENG AS VName,";
            SQL += " ifnull(NameEng,'') as WNameEng,ifnull(NameBang,'') as NameBang,";
            SQL += " CASE WHEN cast(SPNO1 as int) = 55 THEN (select  NameEng  from member where  ProvCode=(select  ProvCode  from member Where healthid=e.healthId) and HHNo=(select  HHNo  from member  Where  healthid=e.healthId) and SNo=(select  SPNO1  from member  Where healthid=e.healthId)) WHEN cast(SPNO1 as int) = 77 THEN (select  NameEng  from member where  ProvCode=(select  ProvCode  from member Where  healthid=e.healthId) and HHNo=(select  HHNo  from member  Where  healthid=e.healthId) and SNo=(select  SPNO1  from member Where healthid=e.healthId)) WHEN cast(SPNO1 as int) = 88 THEN (select  NameEng  from member where  ProvCode=(select  ProvCode  from member Where  healthid=e.healthId) and HHNo=(select  HHNo  from member  Where healthid=e.healthId) and SNo=(select  SPNO1  from member Where  healthid=e.healthId)) ELSE (select  NameEng  from member where  ProvCode=(select  ProvCode  from member  Where healthid=e.healthId) and HHNo=(select  HHNo  from member  Where  healthid=e.healthId) and SNo=(select  SPNO1  from member  Where  healthid=e.healthId)) END AS  HusName,";
            SQL += " ifnull(m.Age,'') as WAge,ifnull(e.marrDate,'') as MDate,ifnull(m.MobileNo1,'') as Mobno,ifnull(e.son,'') as Son,ifnull(e.dau,'') as Dau,ifnull(EDU,'') AS Edu from Member m  left outer join elco e on e.healthId=m.healthId inner join Village v on v.ZILLAID=m.Dist and v.UPAZILAID=m.Upz and v.UNIONID=m.UN and v.MOUZAID=m.Mouza and v.VILLAGEID=m.Vill where e.providerId='"+ g.getProvCode()+"'";*/
//ifnull(e.healthId,'') as HealthId,
           /* SQL  = "Select ifnull(SNo,'') as SNo,ifnull(m.HHNo,'') AS HHNo,ifnull(e.elcoNo,'') as Elcono,ifnull(cM.healthId,'') as HealthId,ifnull(cM.generatedId,'') as generatedId,v.VILLAGENAMEENG AS VName,";
            SQL += " ifnull(NameEng,'') as WNameEng,ifnull(NameBang,'') as NameBang,";
            SQL += " CASE WHEN CAST ( m.SPNO1 AS int ) = 55 THEN ifnull ( m.NameEng , '' )\n" +
                    "WHEN CAST ( m.SPNO1 AS int ) = 77 THEN ifnull ( m.NameEng , '' )\n" +
                    "WHEN CAST ( m.SPNO1 AS int ) = 88 THEN ifnull ( m.NameEng , '' )\n" +
                    "ELSE ( SELECT NameEng FROM member A\n" +
                    "WHERE A.ProvCode = ( SELECT B.ProvCode FROM member B WHERE B.healthid =m.healthId\n" +
                    "and A.Dist = B.Dist and A.Upz = B.Upz and A.UN = B.UN and A.Mouza = B.Mouza and\n" +
                    "A.Vill =B.Vill and A.HHNo = B.HHNo ) AND \n" +
                    "A.HHNo = ( SELECT C.HHNo FROM member C WHERE C.healthid =m.healthId )\n" +
                    "AND A.SNo = ( SELECT D.SPNO1 FROM member  D WHERE D.HealthID =m.healthId ) )\n" +
                    "END AS HusName,";
           // SQL += " CASE WHEN cast(SPNO1 as int) = 55 THEN (select  NameEng  from member where  ProvCode=(select  ProvCode  from member Where healthid=e.healthId) and HHNo=(select  HHNo  from member  Where  healthid=e.healthId) and SNo=(select  SPNO1  from member  Where healthid=e.healthId)) WHEN cast(SPNO1 as int) = 77 THEN (select  NameEng  from member where  ProvCode=(select  ProvCode  from member Where  healthid=e.healthId) and HHNo=(select  HHNo  from member  Where  healthid=e.healthId) and SNo=(select  SPNO1  from member Where healthid=e.healthId)) WHEN cast(SPNO1 as int) = 88 THEN (select  NameEng  from member where  ProvCode=(select  ProvCode  from member Where  healthid=e.healthId) and HHNo=(select  HHNo  from member  Where healthid=e.healthId) and SNo=(select  SPNO1  from member Where  healthid=e.healthId)) ELSE (select  NameEng  from member where  ProvCode=(select  ProvCode  from member  Where healthid=e.healthId) and HHNo=(select  HHNo  from member  Where  healthid=e.healthId) and SNo=(select  SPNO1  from member  Where  healthid=e.healthId)) END AS  HusName,";
            SQL += " ifnull(m.Age,'') as WAge,ifnull(e.marrDate,'') as MDate,ifnull(m.MobileNo1,'') as Mobno,ifnull(e.son,'') as Son,ifnull(e.dau,'') as Dau,ifnull(EDU,'') AS Edu from  clientMap cm inner join Member m on cM.healthId=m.healthId  left outer join elco e on e.healthId=cM.generatedId  inner join Village v on v.ZILLAID=m.Dist and v.UPAZILAID=m.Upz and v.UNIONID=m.UN and v.MOUZAID=m.Mouza and v.VILLAGEID=m.Vill where e.healthId = cM.generatedId"; //where e.providerId='"+ g.getProvCode()+"'";
*/            //+"' and strftime('%d/%m/%Y', date(e.regDT)) between'"+dtfrom.toString()+"' and '"+dtto.toString()+"'";

            SQL = "SELECT distinct ifnull( SNo, '' ) AS SNo,\n" +
                    "       ifnull( m.HHNo, '' ) AS HHNo,\n" +
                    "       ifnull( e.elcoNo, '' ) AS Elcono,\n" +
                    "       ifnull( cM.healthId, '' ) AS HealthId,\n" +
                    "       ifnull( cM.generatedId, '' ) AS generatedId,\n" +
                    "       v.VILLAGENAMEENG AS VName,\n" +
                    "       ifnull( NameEng, '' ) AS WNameEng,\n" +
                    "       ifnull( NameBang, '' ) AS NameBang,\n" +
                    "       CASE\n" +
                    "            WHEN CAST ( m.SPNO1 AS int ) = 55 THEN ifnull( m.NameEng, '' ) \n" +
                    "            WHEN CAST ( m.SPNO1 AS int ) = 77 THEN ifnull( m.NameEng, '' ) \n" +
                    "            WHEN CAST ( m.SPNO1 AS int ) = 88 THEN ifnull( m.NameEng, '' ) \n" +
                    "            ELSE( \n" +
                    "           SELECT NameEng\n" +
                    "             FROM member A\n" +
                    "            WHERE A.ProvCode =( \n" +
                    "                      SELECT B.ProvCode\n" +
                    "                        FROM member B\n" +
                    "                       WHERE B.healthid = m.healthId \n" +
                    "                             AND\n" +
                    "                             A.Dist = B.Dist \n" +
                    "                             AND\n" +
                    "                             A.Upz = B.Upz \n" +
                    "                             AND\n" +
                    "                             A.UN = B.UN \n" +
                    "                             AND\n" +
                    "                             A.Mouza = B.Mouza \n" +
                    "                             AND\n" +
                    "                             A.Vill = B.Vill \n" +
                    "                             AND\n" +
                    "                             A.HHNo = B.HHNo \n" +
                    "                  ) \n" +
                    "                  \n" +
                    "                  AND\n" +
                    "                  A.HHNo =( \n" +
                    "                      SELECT C.HHNo\n" +
                    "                        FROM member C\n" +
                    "                       WHERE C.healthid = m.healthId \n" +
                    "                  ) \n" +
                    "                  \n" +
                    "                  AND\n" +
                    "                  A.SNo =( \n" +
                    "                      SELECT D.SPNO1\n" +
                    "                        FROM member D\n" +
                    "                       WHERE D.HealthID = m.healthId \n" +
                    "                  ) \n" +
                    "                   \n" +
                    "       ) \n" +
                    "       \n" +
                    "       END AS HusName,\n" +
                    "       ifnull( m.Age, '' ) AS WAge,\n" +
                    "       ifnull( e.marrDate, '' ) AS MDate,\n" +
                    "       ifnull( m.MobileNo1, '' ) AS Mobno,\n" +
                    "       ifnull( e.son, '' ) AS Son,\n" +
                    "       ifnull( e.dau, '' ) AS Dau,\n" +
                    "       ifnull( EDU, '' ) AS Edu\n" +
                    "  FROM clientMap cm\n" +
                    "       INNER JOIN Member m\n" +
                    "               ON cM.healthId = m.healthId\n" +
                    "       LEFT OUTER JOIN elco e\n" +
                    "                    ON e.healthId = cM.generatedId\n" +
                    "                     LEFT OUTER JOIN elcoVisit ev\n" +
                    "                    ON ev.healthId = cM.generatedId\n" +
                    "       INNER JOIN Village v\n" +
                    "               ON v.ZILLAID = m.Dist \n" +
                    "AND\n" +
                    "v.UPAZILAID = m.Upz \n" +
                    "AND\n" +
                    "v.UNIONID = m.UN \n" +
                    "AND\n" +
                    "v.MOUZAID = m.Mouza \n" +
                    "AND\n" +
                    "v.VILLAGEID = m.Vill\n" +
                    " WHERE e.healthId = cM.generatedId and \n" +
                    " e.providerId='" + g.getFWAProvCode() + "'";
            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();
            dataList.clear();
            int slno = 0;
            list.setAdapter(null);
            while (!cur.isAfterLast()) {
                slno += 1;
                HashMap<String, String> map = new HashMap<String, String>();
                // ListView list = (ListView)findViewById(R.id.lstData);
                map.put("slno", Integer.toString(slno));
                map.put("hhho", cur.getString(cur.getColumnIndex("HHNo")));
                map.put("healthId", cur.getString(cur.getColumnIndex("HealthId")));
                map.put("elcono", cur.getString(cur.getColumnIndex("Elcono")));
                map.put("vname", cur.getString(cur.getColumnIndex("VName")));
                map.put("wname", cur.getString(cur.getColumnIndex("WNameEng")));
                map.put("hname", cur.getString(cur.getColumnIndex("HusName")));
                map.put("wage", cur.getString(cur.getColumnIndex("WAge")));
                map.put("mdate", cur.getString(cur.getColumnIndex("MDate")));
                map.put("mobno", cur.getString(cur.getColumnIndex("Mobno")));
                map.put("son", cur.getString(cur.getColumnIndex("Son")));
                map.put("dau", cur.getString(cur.getColumnIndex("Dau")));
                map.put("edu", cur.getString(cur.getColumnIndex("Edu")));
                map.put("eFPIhid", cur.getString(cur.getColumnIndex("eFPIhid")).replace("null", ""));
                map.put("GeneratedId", cur.getString(cur.getColumnIndex("generatedId")));


                if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("99")) {
                    map.put("edu", "শিক্ষাগত যোগ্যতা নেই");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("01")) {
                    map.put("edu", "১ম শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("02")) {
                    map.put("edu", "২য় শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("03")) {
                    map.put("edu", "৩য় শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("04")) {
                    map.put("edu", "৪র্থ শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("05")) {
                    map.put("edu", "৫ম শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("06")) {
                    map.put("edu", "৬ষ্ঠ শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("07")) {
                    map.put("edu", "৭ম শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("08")) {
                    map.put("edu", "৮ম শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("09")) {
                    map.put("edu", "৯ম শ্রেনী");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("10")) {
                    map.put("edu", "মাধ্যমিক বা সমতুল্য");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("11")) {
                    map.put("edu", "উচ্চ মাধ্যমিক বা সমতুল্য");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("12")) {
                    map.put("edu", "স্নাতক বা সমতুল্য");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("13")) {
                    map.put("edu", "স্নাতকোত্তর বা সমতুল্য");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("14")) {
                    map.put("edu", "ডাক্তারি");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("15")) {
                    map.put("edu", "ইঞ্জিনিয়ারিং");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("16")) {
                    map.put("edu", "বৃত্তিমুলক শিক্ষা");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("17")) {
                    map.put("edu", "কারিগরি শিক্ষা");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("18")) {
                    map.put("edu", "ধাত্রীবিদ্যা/নার্সিং");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("19")) {
                    map.put("edu", "অন্যান্যা");
                } else if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("77")) {
                    map.put("edu", "প্রযোজ্য নয়");
                }


                dataList.add(map);

                dataAdapter = new SimpleAdapter(Elcoview.this, dataList, R.layout.deathrow, new String[]{"SNo", "EntryDate"},
                        new int[]{R.id.Slno, R.id.EntryDate});

                list.setAdapter(new DataListAdapter(this));

                cur.moveToNext();
                totalCount += 1;
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(Elcoview.this, e.getMessage());
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
                convertView = inflater.inflate(R.layout.elcoviewrow, null);
            }
            final TextView Slno = (TextView) convertView.findViewById(R.id.Slno);
            final TableLayout memtab = (TableLayout) convertView.findViewById(R.id.memtab);
            final TextView Elcono = (TextView) convertView.findViewById(R.id.Elcono);
            final TextView Healthid = (TextView) convertView.findViewById(R.id.Healthid);
            final TextView HHNo = (TextView) convertView.findViewById(R.id.HHNo);
            final TextView VName = (TextView) convertView.findViewById(R.id.VName);
            final TextView Name = (TextView) convertView.findViewById(R.id.Name);
            final TextView HName = (TextView) convertView.findViewById(R.id.HName);
            final TextView Age = (TextView) convertView.findViewById(R.id.Age);
            final TextView MDate = (TextView) convertView.findViewById(R.id.MDate);
            final TextView Mobno = (TextView) convertView.findViewById(R.id.Mobno);

            final TextView Son = (TextView) convertView.findViewById(R.id.Son);
            final TextView Dau = (TextView) convertView.findViewById(R.id.Dau);

            final TextView Edu = (TextView) convertView.findViewById(R.id.Edu);
            final TextView CDeath = (TextView) convertView.findViewById(R.id.CDeath);

            final HashMap<String, String> o = (HashMap<String, String>) dataAdapter.getItem(position);
            Slno.setText(o.get("slno"));

            HHNo.setText(o.get("hhho"));
            Elcono.setText(o.get("elcono"));
            Healthid.setText(o.get("healthId"));
            VName.setText(o.get("vname"));
            Name.setText(o.get("wname"));
            HName.setText(o.get("hname"));
            Age.setText(o.get("wage"));
            MDate.setText(Global.DateConvertDMY(o.get("mdate")));
            Mobno.setText(o.get("mobno"));
            Son.setText(o.get("son"));
            Dau.setText(o.get("dau"));
            Edu.setText(o.get("edu"));

            if (o.get("eFPIhid").equals("Y")) {


                memtab.setBackgroundColor(Color.parseColor("#99cc33"));
            }
            if (o.get("eFPIhid").equals("")) {
                memtab.setBackgroundColor(Color.parseColor("#FFFFFF"));
                // memtabrow.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
            memtab.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    g.setCallFrom("1");
                    //g.setHealthID("");

                    g.setSLno(o.get("slno"));
                    g.setAVDate(o.get("VDate"));
                    g.setHouseholdNo(o.get("hhho"));
                    g.setHealthID(o.get("healthId"));
                    g.setGeneratedId(o.get("GeneratedId"));
                    g.setAName(o.get("name"));
                    g.setASex(o.get("sex"));
                    g.setAAge(o.get("age"));
                    g.setAMElco(o.get("elcono"));
                    g.setHusName(o.get("hname"));


                    finish();
                    Intent f1 = new Intent(getApplicationContext(), ELCOFormFPI.class);
                    startActivity(f1);


                }
            });

            final AlertDialog.Builder adb = new AlertDialog.Builder(Elcoview.this);
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

