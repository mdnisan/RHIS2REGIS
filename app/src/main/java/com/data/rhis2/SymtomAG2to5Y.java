package com.data.rhis2;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import Common.Connection;
import Common.Global;

/**
 * Created by Nisan on 11/28/2015.
 */
public class SymtomAG2to5Y extends Activity {

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
        AlertDialog.Builder adb = new AlertDialog.Builder(SymtomAG2to5Y.this);
        switch (item.getItemId()) {
            case R.id.menuClose:
                adb.setTitle("Close");
                adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান[হ্যা/না]?");
                adb.setNegativeButton("না", null);
                adb.setPositiveButton("হ্যা", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        // Intent f2 = new Intent(getApplicationContext(),MemberList.class);
                        //  startActivity(f2);
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
    SimpleAdapter dataAdapter1;
    ArrayList<HashMap<String, String>> dataList1 = new ArrayList<HashMap<String, String>>();
    // ArrayList<HashMap<String, String>> dataList2 = new ArrayList<HashMap<String, String>>();
    private String TableName;
    private String TableName1;
    // private String TableName2;
    LinearLayout secB;
    LinearLayout secB1;
    LinearLayout secB2;
    LinearLayout secB3;
    LinearLayout secD;
    LinearLayout secD1;
    LinearLayout secD2;
    LinearLayout secD3;
    LinearLayout secF;
    LinearLayout secF1;
    LinearLayout secF2;
    LinearLayout secF3;
    LinearLayout secDy;
    LinearLayout secDy1;
    LinearLayout secFv;
    LinearLayout secFv1;
    LinearLayout secMa;
    LinearLayout secMa1;
    LinearLayout secMa2;
    LinearLayout secNu;
    LinearLayout secNu1;
    LinearLayout secNu2;
    LinearLayout secNu3;


    LinearLayout secLo;
    LinearLayout secLo1;
    LinearLayout secLo2;
    LinearLayout secLo3;
    LinearLayout secOth;
    LinearLayout secOth1;
    LinearLayout secOth2;
    LinearLayout secOth3;
    LinearLayout secOth4;
    LinearLayout secOth5;
    LinearLayout secOth6;
    LinearLayout secOth7;
    CheckBox chkB;
    CheckBox chkD;
    CheckBox chkD1;
    CheckBox chkD2;
    CheckBox chkD3;
    CheckBox chkF;
    CheckBox chkF1;
    CheckBox chkF2;
    CheckBox chkF3;
    CheckBox chkB1;
    CheckBox chkB2;
    CheckBox chkB3;
    CheckBox chkDy;
    CheckBox chkDy1;
    CheckBox chkFv;
    CheckBox chkFv1;
    CheckBox chkMa;
    CheckBox chkMa1;
    CheckBox chkMa2;
    CheckBox chkNu;
    CheckBox chkNu1;
    CheckBox chkNu2;
    CheckBox chkNu3;
    CheckBox chkLo;
    CheckBox chkLo1;
    CheckBox chkLo2;
    CheckBox chkLo3;
    CheckBox chkOth;
    TextView VOth1;
    TextView VOth2;
    TextView VOth3;
    TextView VOth4;
    TextView VOth5;
    TextView VOth6;
    TextView VOth7;
    LinearLayout secListView;
    LinearLayout secListView1;
    LinearLayout secListView2;
    LinearLayout secListView3;
    LinearLayout secListViewD1;
    LinearLayout secListViewD2;
    LinearLayout secListViewD3;
    LinearLayout secListViewD4;
    LinearLayout secListViewF1;
    LinearLayout secListViewF2;
    LinearLayout secListViewF3;
    LinearLayout secListViewF4;
    LinearLayout secListViewDy;
    LinearLayout secListViewDy1;
    LinearLayout secListViewFv;
    LinearLayout secListViewFv1;
    LinearLayout secListViewMa;
    LinearLayout secListViewMa1;
    LinearLayout secListViewMa2;
    LinearLayout secListViewNu;
    LinearLayout secListViewNu1;
    LinearLayout secListViewNu2;
    LinearLayout secListViewNu3;

    LinearLayout secListViewLo;
    LinearLayout secListViewLo1;
    LinearLayout secListViewLo2;
    LinearLayout secListViewLo3;
    LinearLayout secListViewOth;
    //TextView lblHlblepireg;
    String StartTime;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.symtomag2to5y);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();
            TableName = "symtom";
            // TableName1 = "classfication";
            TableName1 = "treatment";

            secB = (LinearLayout) findViewById(R.id.secB);
            secB1 = (LinearLayout) findViewById(R.id.secB1);
            secB2 = (LinearLayout) findViewById(R.id.secB2);
            secB3 = (LinearLayout) findViewById(R.id.secB3);
            secD = (LinearLayout) findViewById(R.id.secD);
            secD1 = (LinearLayout) findViewById(R.id.secD1);
            secD2 = (LinearLayout) findViewById(R.id.secD2);
            secD3 = (LinearLayout) findViewById(R.id.secD3);
            secF = (LinearLayout) findViewById(R.id.secF);
            secF1 = (LinearLayout) findViewById(R.id.secF1);
            secF2 = (LinearLayout) findViewById(R.id.secF2);
            secF3 = (LinearLayout) findViewById(R.id.secF3);
            secDy = (LinearLayout) findViewById(R.id.secDy);
            secDy1 = (LinearLayout) findViewById(R.id.secDy1);
            secFv = (LinearLayout) findViewById(R.id.secFv);
            secFv1 = (LinearLayout) findViewById(R.id.secFv1);
            secMa = (LinearLayout) findViewById(R.id.secMa);
            secMa1 = (LinearLayout) findViewById(R.id.secMa1);
            secMa2 = (LinearLayout) findViewById(R.id.secMa2);
            secNu = (LinearLayout) findViewById(R.id.secNu);
            secNu1 = (LinearLayout) findViewById(R.id.secNu1);
            secNu2 = (LinearLayout) findViewById(R.id.secNu2);
            secNu3 = (LinearLayout) findViewById(R.id.secNu3);

            secLo = (LinearLayout) findViewById(R.id.secLo);
            secLo1 = (LinearLayout) findViewById(R.id.secLo1);
            secLo2 = (LinearLayout) findViewById(R.id.secLo2);
            secLo3 = (LinearLayout) findViewById(R.id.secLo3);
            secOth = (LinearLayout) findViewById(R.id.secOth);
            secOth1 = (LinearLayout) findViewById(R.id.secOth1);
            secOth2 = (LinearLayout) findViewById(R.id.secOth2);
            secOth3 = (LinearLayout) findViewById(R.id.secOth3);
            secOth4 = (LinearLayout) findViewById(R.id.secOth4);
            secOth5 = (LinearLayout) findViewById(R.id.secOth5);
            secOth6 = (LinearLayout) findViewById(R.id.secOth6);
            secOth7 = (LinearLayout) findViewById(R.id.secOth7);
            chkB = (CheckBox) findViewById(R.id.chkB);
            chkB1 = (CheckBox) findViewById(R.id.chkB1);
            chkB2 = (CheckBox) findViewById(R.id.chkB2);
            chkB3 = (CheckBox) findViewById(R.id.chkB3);
            chkD = (CheckBox) findViewById(R.id.chkD);
            chkD1 = (CheckBox) findViewById(R.id.chkD1);
            chkD2 = (CheckBox) findViewById(R.id.chkD2);
            chkD3 = (CheckBox) findViewById(R.id.chkD3);
            chkF = (CheckBox) findViewById(R.id.chkF);
            chkF1 = (CheckBox) findViewById(R.id.chkF1);
            chkF2 = (CheckBox) findViewById(R.id.chkF2);
            chkF3 = (CheckBox) findViewById(R.id.chkF3);
            chkDy = (CheckBox) findViewById(R.id.chkDy);
            chkDy1 = (CheckBox) findViewById(R.id.chkDy1);
            chkFv = (CheckBox) findViewById(R.id.chkFv);
            chkFv1 = (CheckBox) findViewById(R.id.chkFv1);
            chkMa = (CheckBox) findViewById(R.id.chkMa);
            chkMa1 = (CheckBox) findViewById(R.id.chkMa1);
            chkMa2 = (CheckBox) findViewById(R.id.chkMa2);
            chkNu = (CheckBox) findViewById(R.id.chkNu);
            chkNu1 = (CheckBox) findViewById(R.id.chkNu1);
            chkNu2 = (CheckBox) findViewById(R.id.chkNu2);
            chkNu3 = (CheckBox) findViewById(R.id.chkNu3);
            chkLo = (CheckBox) findViewById(R.id.chkLo);
            chkLo1 = (CheckBox) findViewById(R.id.chkLo1);
            chkLo2 = (CheckBox) findViewById(R.id.chkLo2);
            chkLo3 = (CheckBox) findViewById(R.id.chkLo3);
            chkOth = (CheckBox) findViewById(R.id.chkOth);
            VOth1 = (TextView) findViewById(R.id.VOth1);
            VOth2 = (TextView) findViewById(R.id.VOth2);
            VOth3 = (TextView) findViewById(R.id.VOth3);
            VOth4 = (TextView) findViewById(R.id.VOth4);
            VOth5 = (TextView) findViewById(R.id.VOth5);
            VOth6 = (TextView) findViewById(R.id.VOth6);
            VOth7 = (TextView) findViewById(R.id.VOth7);
            chkB.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkB.isChecked()) {
                        DataSearch();
                        secListView.setVisibility(View.VISIBLE);
                    } else {
                        secListView.setVisibility(View.GONE);
                        secB1.setVisibility(View.GONE);
                        secB2.setVisibility(View.GONE);
                        secB3.setVisibility(View.GONE);
                        chkB1.setChecked(false);
                        chkB2.setChecked(false);
                        chkB3.setChecked(false);
                    }
                }
            });
            chkB1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkB1.isChecked()) {
                        DataSearch1();
                        secListView1.setVisibility(View.VISIBLE);
                    } else {
                        secListView1.setVisibility(View.GONE);

                    }
                }
            });
            chkB2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkB2.isChecked()) {
                        DataSearch2();
                        secListView2.setVisibility(View.VISIBLE);
                    } else {
                        secListView2.setVisibility(View.GONE);

                    }
                }
            });

            chkB3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkB3.isChecked()) {
                        DataSearch3();
                        secListView3.setVisibility(View.VISIBLE);
                    } else {
                        secListView3.setVisibility(View.GONE);

                    }
                }
            });

            chkD.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkD.isChecked()) {
                        DataSearchD();
                        secListViewD1.setVisibility(View.VISIBLE);
                    } else {
                        secListViewD1.setVisibility(View.GONE);
                        secD1.setVisibility(View.GONE);
                        secD2.setVisibility(View.GONE);
                        secD3.setVisibility(View.GONE);
                        //chkD1.setChecked(false);
                        //chkD2.setChecked(false);
                    }
                }
            });

            chkD1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkD1.isChecked()) {
                        DataSearchD1();
                        secListViewD2.setVisibility(View.VISIBLE);
                    } else {
                        secListViewD2.setVisibility(View.GONE);

                    }
                }
            });

            chkD2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkD2.isChecked()) {
                        DataSearchD2();
                        secListViewD3.setVisibility(View.VISIBLE);
                    } else {
                        secListViewD3.setVisibility(View.GONE);

                    }
                }
            });

            chkD3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkD3.isChecked()) {
                        DataSearchD3();
                        secListViewD4.setVisibility(View.VISIBLE);
                    } else {
                        secListViewD4.setVisibility(View.GONE);

                    }
                }
            });

            chkF.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkF.isChecked()) {
                        DataSearchF();
                        secListViewF1.setVisibility(View.VISIBLE);
                    } else {
                        secListViewF1.setVisibility(View.GONE);
                        secF1.setVisibility(View.GONE);
                        secF2.setVisibility(View.GONE);
                        secF3.setVisibility(View.GONE);

                    }
                }
            });
            chkF1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkF1.isChecked()) {
                        DataSearchF1();
                        secListViewF2.setVisibility(View.VISIBLE);
                    } else {
                        secListViewF2.setVisibility(View.GONE);

                    }
                }
            });

            chkF2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkF2.isChecked()) {
                        DataSearchF2();
                        secListViewF3.setVisibility(View.VISIBLE);
                    } else {
                        secListViewF3.setVisibility(View.GONE);

                    }
                }
            });

            chkF3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkF3.isChecked()) {
                        DataSearchF3();
                        secListViewF4.setVisibility(View.VISIBLE);
                    } else {
                        secListViewF4.setVisibility(View.GONE);

                    }
                }
            });

            chkDy.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkDy.isChecked()) {
                        DataSearchDy();
                        secListViewDy.setVisibility(View.VISIBLE);
                    } else {
                        secListViewDy.setVisibility(View.GONE);
                        secDy1.setVisibility(View.GONE);


                    }
                }
            });

            chkDy1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkDy1.isChecked()) {
                        DataSearchDy1();
                        secListViewDy1.setVisibility(View.VISIBLE);
                    } else {
                        secListViewDy1.setVisibility(View.GONE);


                    }
                }
            });


            chkFv.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkFv.isChecked()) {
                        DataSearchFv();
                        secListViewFv.setVisibility(View.VISIBLE);
                    } else {
                        secListViewFv.setVisibility(View.GONE);
                        secFv1.setVisibility(View.GONE);


                    }
                }
            });
            chkFv1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkFv1.isChecked()) {
                        DataSearchFv1();
                        secListViewFv1.setVisibility(View.VISIBLE);
                    } else {
                        secListViewFv1.setVisibility(View.GONE);
                        //secFv1.setVisibility(View.GONE);


                    }
                }
            });

            chkMa.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkMa.isChecked()) {
                        DataSearchMa();
                        secListViewMa.setVisibility(View.VISIBLE);
                    } else {
                        secListViewMa.setVisibility(View.GONE);
                        secMa1.setVisibility(View.GONE);
                        secMa2.setVisibility(View.GONE);


                    }
                }
            });

            chkMa1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkMa1.isChecked()) {
                        DataSearchMa1();
                        secListViewMa1.setVisibility(View.VISIBLE);
                    } else {
                        secListViewMa1.setVisibility(View.GONE);
                        // secMa1.setVisibility(View.GONE);


                    }
                }
            });

            chkMa2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkMa2.isChecked()) {
                        DataSearchMa2();
                        secListViewMa2.setVisibility(View.VISIBLE);
                    } else {
                        secListViewMa2.setVisibility(View.GONE);
                        // secMa1.setVisibility(View.GONE);


                    }
                }
            });

            chkNu.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkNu.isChecked()) {
                        DataSearchNu();
                        secListViewNu.setVisibility(View.VISIBLE);
                    } else {
                        secListViewNu.setVisibility(View.GONE);
                        secNu1.setVisibility(View.GONE);
                        secNu2.setVisibility(View.GONE);
                        secNu3.setVisibility(View.GONE);

                    }
                }
            });

            chkNu1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkNu1.isChecked()) {
                        DataSearchNu1();
                        secListViewNu1.setVisibility(View.VISIBLE);
                    } else {
                        secListViewNu1.setVisibility(View.GONE);
                        // secMa1.setVisibility(View.GONE);


                    }
                }
            });

            chkNu2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkNu2.isChecked()) {
                        DataSearchNu2();
                        secListViewNu2.setVisibility(View.VISIBLE);
                    } else {
                        secListViewNu2.setVisibility(View.GONE);
                        // secMa1.setVisibility(View.GONE);


                    }
                }
            });

            chkNu3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkNu3.isChecked()) {
                        DataSearchNu3();
                        secListViewNu3.setVisibility(View.VISIBLE);
                    } else {
                        secListViewNu3.setVisibility(View.GONE);
                        // secMa1.setVisibility(View.GONE);


                    }
                }
            });

            //Low passer
            chkLo.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkLo.isChecked()) {
                        DataSearchLo();
                        secListViewLo.setVisibility(View.VISIBLE);
                    } else {
                        secListViewLo.setVisibility(View.GONE);
                        secLo1.setVisibility(View.GONE);
                        secLo2.setVisibility(View.GONE);
                        secLo3.setVisibility(View.GONE);

                    }
                }
            });

            chkLo1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkLo1.isChecked()) {
                        DataSearchLo1();
                        secListViewLo1.setVisibility(View.VISIBLE);
                    } else {
                        secListViewLo1.setVisibility(View.GONE);
                        // secMa1.setVisibility(View.GONE);


                    }
                }
            });

            chkLo2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkLo2.isChecked()) {
                        DataSearchLo2();
                        secListViewLo2.setVisibility(View.VISIBLE);
                    } else {
                        secListViewLo2.setVisibility(View.GONE);
                        // secMa1.setVisibility(View.GONE);


                    }
                }
            });

            chkLo3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkLo3.isChecked()) {
                        DataSearchLo3();
                        secListViewLo3.setVisibility(View.VISIBLE);
                    } else {
                        secListViewLo3.setVisibility(View.GONE);
                        // secMa1.setVisibility(View.GONE);


                    }
                }
            });
//Other
            chkOth.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkOth.isChecked()) {
                        DataSearchOth();
                        secListViewOth.setVisibility(View.VISIBLE);
                    } else {
                        secListViewOth.setVisibility(View.GONE);
                        secOth1.setVisibility(View.GONE);
                        secOth2.setVisibility(View.GONE);
                        secOth3.setVisibility(View.GONE);
                        secOth4.setVisibility(View.GONE);
                        secOth5.setVisibility(View.GONE);
                        secOth6.setVisibility(View.GONE);
                        secOth7.setVisibility(View.GONE);
                        //secOth1.setVisibility(View.GONE);
                        //secLo2.setVisibility(View.GONE);
                        //secLo3.setVisibility(View.GONE);

                    }
                }
            });
            secListView = (LinearLayout) findViewById(R.id.secListView);
            secListView1 = (LinearLayout) findViewById(R.id.secListView1);
            secListView2 = (LinearLayout) findViewById(R.id.secListView2);
            secListView3 = (LinearLayout) findViewById(R.id.secListView3);
            secListViewD1 = (LinearLayout) findViewById(R.id.secListViewD1);
            secListViewD2 = (LinearLayout) findViewById(R.id.secListViewD2);
            secListViewD3 = (LinearLayout) findViewById(R.id.secListViewD3);
            secListViewD4 = (LinearLayout) findViewById(R.id.secListViewD4);
            secListViewF1 = (LinearLayout) findViewById(R.id.secListViewF1);
            secListViewF2 = (LinearLayout) findViewById(R.id.secListViewF2);
            secListViewF3 = (LinearLayout) findViewById(R.id.secListViewF3);
            secListViewF4 = (LinearLayout) findViewById(R.id.secListViewF4);
            secListViewDy = (LinearLayout) findViewById(R.id.secListViewDy);
            secListViewDy1 = (LinearLayout) findViewById(R.id.secListViewDy1);
            secListViewFv = (LinearLayout) findViewById(R.id.secListViewFv);
            secListViewFv1 = (LinearLayout) findViewById(R.id.secListViewFv1);
            secListViewMa = (LinearLayout) findViewById(R.id.secListViewMa);
            secListViewMa1 = (LinearLayout) findViewById(R.id.secListViewMa1);
            secListViewMa2 = (LinearLayout) findViewById(R.id.secListViewMa2);
            secListViewNu = (LinearLayout) findViewById(R.id.secListViewNu);
            secListViewNu1 = (LinearLayout) findViewById(R.id.secListViewNu1);
            secListViewNu2 = (LinearLayout) findViewById(R.id.secListViewNu2);
            secListViewNu3 = (LinearLayout) findViewById(R.id.secListViewNu3);

            secListViewLo = (LinearLayout) findViewById(R.id.secListViewLo);
            secListViewLo1 = (LinearLayout) findViewById(R.id.secListViewLo1);
            secListViewLo2 = (LinearLayout) findViewById(R.id.secListViewLo2);
            secListViewLo3 = (LinearLayout) findViewById(R.id.secListViewLo3);
            secListViewOth = (LinearLayout) findViewById(R.id.secListViewOth);
            final ListView list = (ListView) findViewById(R.id.lstData);
            final ListView list1 = (ListView) findViewById(R.id.lstData1);
            final ListView list2 = (ListView) findViewById(R.id.lstData2);
            //  View header = getLayoutInflater().inflate(R.layout.symtomheading, null);
            // list.addHeaderView(header);
            setDynamicHeight(list);
            setDynamicHeight(list1);
            setDynamicHeight(list2);
           /* final ListView list1 = (ListView) findViewById(R.id.lstData1);
            View header1 = getLayoutInflater().inflate(R.layout.treatmentheading, null);
            list.addHeaderView(header1);*/
            // secB.setVisibility(View.GONE);
            secB1.setVisibility(View.GONE);
            secB2.setVisibility(View.GONE);
            secB3.setVisibility(View.GONE);
            secD1.setVisibility(View.GONE);
            secD2.setVisibility(View.GONE);
            secD3.setVisibility(View.GONE);
            secF1.setVisibility(View.GONE);
            secF2.setVisibility(View.GONE);
            secF3.setVisibility(View.GONE);
            secDy1.setVisibility(View.GONE);
            secFv1.setVisibility(View.GONE);
            secMa1.setVisibility(View.GONE);
            secMa2.setVisibility(View.GONE);
            secNu1.setVisibility(View.GONE);
            secNu2.setVisibility(View.GONE);
            secNu3.setVisibility(View.GONE);
            secLo1.setVisibility(View.GONE);
            secLo2.setVisibility(View.GONE);
            secLo3.setVisibility(View.GONE);
            secOth1.setVisibility(View.GONE);
            secOth2.setVisibility(View.GONE);
            secOth3.setVisibility(View.GONE);
            secOth4.setVisibility(View.GONE);
            secOth5.setVisibility(View.GONE);
            secOth6.setVisibility(View.GONE);
            secOth7.setVisibility(View.GONE);
            secListView.setVisibility(View.GONE);
            secListView1.setVisibility(View.GONE);
            secListView2.setVisibility(View.GONE);
            secListView3.setVisibility(View.GONE);
            secListViewD1.setVisibility(View.GONE);
            secListViewD2.setVisibility(View.GONE);
            secListViewD3.setVisibility(View.GONE);
            secListViewD4.setVisibility(View.GONE);
            secListViewF1.setVisibility(View.GONE);
            secListViewF2.setVisibility(View.GONE);
            secListViewF3.setVisibility(View.GONE);
            secListViewF4.setVisibility(View.GONE);
            secListViewDy.setVisibility(View.GONE);
            secListViewDy1.setVisibility(View.GONE);
            secListViewFv.setVisibility(View.GONE);
            secListViewFv1.setVisibility(View.GONE);
            secListViewMa.setVisibility(View.GONE);
            secListViewMa1.setVisibility(View.GONE);
            secListViewMa2.setVisibility(View.GONE);
            secListViewNu.setVisibility(View.GONE);
            secListViewNu1.setVisibility(View.GONE);
            secListViewNu2.setVisibility(View.GONE);
            secListViewNu3.setVisibility(View.GONE);
            secListViewLo.setVisibility(View.GONE);
            secListViewLo1.setVisibility(View.GONE);
            secListViewLo2.setVisibility(View.GONE);
            secListViewLo3.setVisibility(View.GONE);
            secListViewOth.setVisibility(View.GONE);
            // DataSearch();

        } catch (Exception e) {
            Connection.MessageBox(SymtomAG2to5Y.this, e.getMessage());
            return;
        }
    }

    /**
     * Set listview height based on listview children
     *
     * @param listView
     */
    public static void setDynamicHeight(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        //check adapter if null
        if (adapter == null) {
            return;
        }
        int height = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            height += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
        layoutParams.height = height + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(layoutParams);
        listView.requestLayout();
    }

    private void DataSearch() {
        try {
            Cursor cur = C.ReadData("Select * from " + TableName + "  Where typeCode='1' and ageGroup='" + 1 + "'");
            cur.moveToFirst();
            dataList.clear();
            int slno = 0;
            while (!cur.isAfterLast()) {
                slno += 1;
                HashMap<String, String> map = new HashMap<String, String>();
                ListView list = (ListView) findViewById(R.id.lstData);

                map.put("Slno", Integer.toString(slno));
                map.put("AgeGroup", cur.getString(cur.getColumnIndex("ageGroup")));
                map.put("TypeCode", cur.getString(cur.getColumnIndex("typeCode")));
                map.put("SymtomCode", cur.getString(cur.getColumnIndex("symtomCode")));
                map.put("SymtomDes", cur.getString(cur.getColumnIndex("symtomDes")));
                map.put("ClassficationCode", cur.getString(cur.getColumnIndex("classficationCode")));


                dataList.add(map);

                dataAdapter = new SimpleAdapter(SymtomAG2to5Y.this, dataList, R.layout.symtomrow, new String[]{"click"},
                        new int[]{R.id.chkB1});

                list.setAdapter(new DataListAdapter(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(SymtomAG2to5Y.this, e.getMessage());
            return;
        }
    }


    private void DataSearchD() {
        try {
            Cursor cur = C.ReadData("Select * from " + TableName + "  Where typeCode='1' and ageGroup='" + 2 + "'");
            cur.moveToFirst();
            dataList.clear();
            int slno = 0;
            while (!cur.isAfterLast()) {
                slno += 1;
                HashMap<String, String> map = new HashMap<String, String>();
                ListView list = (ListView) findViewById(R.id.lstDataD1);

                map.put("Slno", Integer.toString(slno));
                map.put("AgeGroup", cur.getString(cur.getColumnIndex("ageGroup")));
                map.put("TypeCode", cur.getString(cur.getColumnIndex("typeCode")));
                map.put("SymtomCode", cur.getString(cur.getColumnIndex("symtomCode")));
                map.put("SymtomDes", cur.getString(cur.getColumnIndex("symtomDes")));
                map.put("ClassficationCode", cur.getString(cur.getColumnIndex("classficationCode")));


                dataList.add(map);

                dataAdapter = new SimpleAdapter(SymtomAG2to5Y.this, dataList, R.layout.symtomrow, new String[]{"click"},
                        new int[]{R.id.chkB1});

                list.setAdapter(new DataListAdapter(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(SymtomAG2to5Y.this, e.getMessage());
            return;
        }
    }

    private void DataSearchF() {
        try {
            Cursor cur = C.ReadData("Select * from " + TableName + "  Where typeCode='2' and ageGroup='" + 2 + "'");
            cur.moveToFirst();
            dataList.clear();
            int slno = 0;
            while (!cur.isAfterLast()) {
                slno += 1;
                HashMap<String, String> map = new HashMap<String, String>();
                ListView list = (ListView) findViewById(R.id.lstDataF1);

                map.put("Slno", Integer.toString(slno));
                map.put("AgeGroup", cur.getString(cur.getColumnIndex("ageGroup")));
                map.put("TypeCode", cur.getString(cur.getColumnIndex("typeCode")));
                map.put("SymtomCode", cur.getString(cur.getColumnIndex("symtomCode")));
                map.put("SymtomDes", cur.getString(cur.getColumnIndex("symtomDes")));
                map.put("ClassficationCode", cur.getString(cur.getColumnIndex("classficationCode")));


                dataList.add(map);

                dataAdapter = new SimpleAdapter(SymtomAG2to5Y.this, dataList, R.layout.symtomrow, new String[]{"click"},
                        new int[]{R.id.chkB1});

                list.setAdapter(new DataListAdapter(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(SymtomAG2to5Y.this, e.getMessage());
            return;
        }
    }

    private void DataSearchDy() {
        try {
            Cursor cur = C.ReadData("Select * from " + TableName + "  Where typeCode='3' and ageGroup='" + 2 + "'");
            cur.moveToFirst();
            dataList.clear();
            int slno = 0;
            while (!cur.isAfterLast()) {
                slno += 1;
                HashMap<String, String> map = new HashMap<String, String>();
                ListView list = (ListView) findViewById(R.id.lstDataDy);

                map.put("Slno", Integer.toString(slno));
                map.put("AgeGroup", cur.getString(cur.getColumnIndex("ageGroup")));
                map.put("TypeCode", cur.getString(cur.getColumnIndex("typeCode")));
                map.put("SymtomCode", cur.getString(cur.getColumnIndex("symtomCode")));
                map.put("SymtomDes", cur.getString(cur.getColumnIndex("symtomDes")));
                map.put("ClassficationCode", cur.getString(cur.getColumnIndex("classficationCode")));


                dataList.add(map);

                dataAdapter = new SimpleAdapter(SymtomAG2to5Y.this, dataList, R.layout.symtomrow, new String[]{"click"},
                        new int[]{R.id.chkB1});

                list.setAdapter(new DataListAdapter(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(SymtomAG2to5Y.this, e.getMessage());
            return;
        }
    }

    private void DataSearchFv() {
        try {
            Cursor cur = C.ReadData("Select * from " + TableName + "  Where typeCode='4' and ageGroup='" + 2 + "'");
            cur.moveToFirst();
            dataList.clear();
            int slno = 0;
            while (!cur.isAfterLast()) {
                slno += 1;
                HashMap<String, String> map = new HashMap<String, String>();
                ListView list = (ListView) findViewById(R.id.lstDataFv);

                map.put("Slno", Integer.toString(slno));
                map.put("AgeGroup", cur.getString(cur.getColumnIndex("ageGroup")));
                map.put("TypeCode", cur.getString(cur.getColumnIndex("typeCode")));
                map.put("SymtomCode", cur.getString(cur.getColumnIndex("symtomCode")));
                map.put("SymtomDes", cur.getString(cur.getColumnIndex("symtomDes")));
                map.put("ClassficationCode", cur.getString(cur.getColumnIndex("classficationCode")));


                dataList.add(map);

                dataAdapter = new SimpleAdapter(SymtomAG2to5Y.this, dataList, R.layout.symtomrow, new String[]{"click"},
                        new int[]{R.id.chkB1});

                list.setAdapter(new DataListAdapter(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(SymtomAG2to5Y.this, e.getMessage());
            return;
        }
    }

    private void DataSearchMa() {
        try {
            Cursor cur = C.ReadData("Select * from " + TableName + "  Where typeCode='5' and ageGroup='" + 2 + "'");
            cur.moveToFirst();
            dataList.clear();
            int slno = 0;
            while (!cur.isAfterLast()) {
                slno += 1;
                HashMap<String, String> map = new HashMap<String, String>();
                ListView list = (ListView) findViewById(R.id.lstDataMa);

                map.put("Slno", Integer.toString(slno));
                map.put("AgeGroup", cur.getString(cur.getColumnIndex("ageGroup")));
                map.put("TypeCode", cur.getString(cur.getColumnIndex("typeCode")));
                map.put("SymtomCode", cur.getString(cur.getColumnIndex("symtomCode")));
                map.put("SymtomDes", cur.getString(cur.getColumnIndex("symtomDes")));
                map.put("ClassficationCode", cur.getString(cur.getColumnIndex("classficationCode")));


                dataList.add(map);

                dataAdapter = new SimpleAdapter(SymtomAG2to5Y.this, dataList, R.layout.symtomrow, new String[]{"click"},
                        new int[]{R.id.chkB1});

                list.setAdapter(new DataListAdapter(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(SymtomAG2to5Y.this, e.getMessage());
            return;
        }
    }

    private void DataSearchNu() {
        try {
            Cursor cur = C.ReadData("Select * from " + TableName + "  Where typeCode='6' and ageGroup='" + 2 + "'");
            cur.moveToFirst();
            dataList.clear();
            int slno = 0;
            while (!cur.isAfterLast()) {
                slno += 1;
                HashMap<String, String> map = new HashMap<String, String>();
                ListView list = (ListView) findViewById(R.id.lstDataNu);

                map.put("Slno", Integer.toString(slno));
                map.put("AgeGroup", cur.getString(cur.getColumnIndex("ageGroup")));
                map.put("TypeCode", cur.getString(cur.getColumnIndex("typeCode")));
                map.put("SymtomCode", cur.getString(cur.getColumnIndex("symtomCode")));
                map.put("SymtomDes", cur.getString(cur.getColumnIndex("symtomDes")));
                map.put("ClassficationCode", cur.getString(cur.getColumnIndex("classficationCode")));


                dataList.add(map);

                dataAdapter = new SimpleAdapter(SymtomAG2to5Y.this, dataList, R.layout.symtomrow, new String[]{"click"},
                        new int[]{R.id.chkB1});

                list.setAdapter(new DataListAdapter(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(SymtomAG2to5Y.this, e.getMessage());
            return;
        }
    }

    private void DataSearchLo() {
        try {
            Cursor cur = C.ReadData("Select * from " + TableName + "  Where typeCode='7' and ageGroup='" + 2 + "'");
            cur.moveToFirst();
            dataList.clear();
            int slno = 0;
            while (!cur.isAfterLast()) {
                slno += 1;
                HashMap<String, String> map = new HashMap<String, String>();
                ListView list = (ListView) findViewById(R.id.lstDataLo);

                map.put("Slno", Integer.toString(slno));
                map.put("AgeGroup", cur.getString(cur.getColumnIndex("ageGroup")));
                map.put("TypeCode", cur.getString(cur.getColumnIndex("typeCode")));
                map.put("SymtomCode", cur.getString(cur.getColumnIndex("symtomCode")));
                map.put("SymtomDes", cur.getString(cur.getColumnIndex("symtomDes")));
                map.put("ClassficationCode", cur.getString(cur.getColumnIndex("classficationCode")));


                dataList.add(map);

                dataAdapter = new SimpleAdapter(SymtomAG2to5Y.this, dataList, R.layout.symtomrow, new String[]{"click"},
                        new int[]{R.id.chkB1});

                list.setAdapter(new DataListAdapter(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(SymtomAG2to5Y.this, e.getMessage());
            return;
        }
    }

    private void DataSearchOth() {
        try {
            Cursor cur = C.ReadData("Select * from " + TableName + "  Where typeCode='8' and ageGroup='" + 2 + "'");
            cur.moveToFirst();
            dataList.clear();
            int slno = 0;
            while (!cur.isAfterLast()) {
                slno += 1;
                HashMap<String, String> map = new HashMap<String, String>();
                ListView list = (ListView) findViewById(R.id.lstDataOth);

                map.put("Slno", Integer.toString(slno));
                map.put("AgeGroup", cur.getString(cur.getColumnIndex("ageGroup")));
                map.put("TypeCode", cur.getString(cur.getColumnIndex("typeCode")));
                map.put("SymtomCode", cur.getString(cur.getColumnIndex("symtomCode")));
                map.put("SymtomDes", cur.getString(cur.getColumnIndex("symtomDes")));
                map.put("ClassficationCode", cur.getString(cur.getColumnIndex("classficationCode")));


                dataList.add(map);

                dataAdapter = new SimpleAdapter(SymtomAG2to5Y.this, dataList, R.layout.symtomrow, new String[]{"click"},
                        new int[]{R.id.chkB1});

                list.setAdapter(new DataListAdapter(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(SymtomAG2to5Y.this, e.getMessage());
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
                convertView = inflater.inflate(R.layout.symtomrow, null);
            }
            final CheckBox chkB1 = (CheckBox) convertView.findViewById(R.id.chkB1);
            final TextView symtom = (TextView) convertView.findViewById(R.id.symtom);

            final HashMap<String, String> o = (HashMap<String, String>) dataAdapter.getItem(position);
            //SlNo.setText(o.get("SlNo"));
            o.get("AgeGroup");
            o.get("TypeCode");
            o.get("SymtomCode");
            symtom.setText(o.get("SymtomDes"));
            o.get("ClassficationCode");


            chkB1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {


                    if (chkB1.isChecked() == true) {
                        g.setCallFrom(o.get("SymtomCode"));
                        if ((g.getCallFrom().equals("1") | g.getCallFrom().equals("2") | g.getCallFrom().equals("3")
                                | g.getCallFrom().equals("4") | g.getCallFrom().equals("5") | g.getCallFrom().equals("6")) & o.get("ClassficationCode").equals("1")) {
                            secB1.setVisibility(View.VISIBLE);


                        } else if ((g.getCallFrom().equals("7") | g.getCallFrom().equals("8")) & o.get("ClassficationCode").equals("2")) {
                            secB2.setVisibility(View.VISIBLE);


                        } else if ((g.getCallFrom().equals("9")) & o.get("ClassficationCode").equals("3")) {
                            secB3.setVisibility(View.VISIBLE);


                        } else {
                            secB1.setVisibility(View.GONE);
                            secB2.setVisibility(View.GONE);
                            secB3.setVisibility(View.GONE);
                        }
                        if ((g.getCallFrom().equals("23")) & o.get("ClassficationCode").equals("9") |
                                (g.getCallFrom().equals("24")) & o.get("ClassficationCode").equals("9")) {
                            secD1.setVisibility(View.VISIBLE);


                        } else if ((g.getCallFrom().equals("25")) & o.get("ClassficationCode").equals("10")) {
                            secD2.setVisibility(View.VISIBLE);


                        } else if ((g.getCallFrom().equals("26")) & o.get("ClassficationCode").equals("11")) {
                            secD3.setVisibility(View.VISIBLE);


                        } else

                        {
                            secD1.setVisibility(View.GONE);
                            secD2.setVisibility(View.GONE);
                            secD3.setVisibility(View.GONE);
                        }

                        if ((g.getCallFrom().equals("28")) & o.get("ClassficationCode").equals("12") |
                                (g.getCallFrom().equals("29")) & o.get("ClassficationCode").equals("12") |
                                (g.getCallFrom().equals("30")) & o.get("ClassficationCode").equals("12") |
                                (g.getCallFrom().equals("31")) & o.get("ClassficationCode").equals("12"))

                        {
                            secF1.setVisibility(View.VISIBLE);


                        } else if ((g.getCallFrom().equals("32")) & o.get("ClassficationCode").equals("13") |
                                (g.getCallFrom().equals("33")) & o.get("ClassficationCode").equals("13") |
                                (g.getCallFrom().equals("34")) & o.get("ClassficationCode").equals("13") |
                                (g.getCallFrom().equals("35")) & o.get("ClassficationCode").equals("13"))

                        {
                            secF2.setVisibility(View.VISIBLE);


                        } else if ((g.getCallFrom().equals("36")) & o.get("ClassficationCode").equals("14")) {
                            secF3.setVisibility(View.VISIBLE);


                        } else

                        {
                            secF1.setVisibility(View.GONE);
                            secF2.setVisibility(View.GONE);
                            secF3.setVisibility(View.GONE);
                        }

                        if ((g.getCallFrom().equals("37")) & o.get("ClassficationCode").equals("15")) {
                            secDy1.setVisibility(View.VISIBLE);


                        } else

                        {
                            secDy1.setVisibility(View.GONE);

                        }

                        if ((g.getCallFrom().equals("38")) & o.get("ClassficationCode").equals("16") | (g.getCallFrom().equals("39")) & o.get("ClassficationCode").equals("16")) {
                            secFv1.setVisibility(View.VISIBLE);


                        } else

                        {
                            secFv1.setVisibility(View.GONE);

                        }

                        if ((g.getCallFrom().equals("40")) & o.get("ClassficationCode").equals("17")) {
                            secMa1.setVisibility(View.VISIBLE);


                        } else if ((g.getCallFrom().equals("41")) & o.get("ClassficationCode").equals("18")) {
                            secMa2.setVisibility(View.VISIBLE);


                        } else

                        {
                            secMa1.setVisibility(View.GONE);
                            secMa2.setVisibility(View.GONE);

                        }

                        if ((g.getCallFrom().equals("42")) & o.get("ClassficationCode").equals("19") |
                                (g.getCallFrom().equals("43")) & o.get("ClassficationCode").equals("19")) {
                            secNu1.setVisibility(View.VISIBLE);


                        } else if ((g.getCallFrom().equals("44")) & o.get("ClassficationCode").equals("20")) {
                            secNu2.setVisibility(View.VISIBLE);


                        } else if ((g.getCallFrom().equals("45")) & o.get("ClassficationCode").equals("21")) {
                            secNu3.setVisibility(View.VISIBLE);


                        } else

                        {
                            secNu1.setVisibility(View.GONE);
                            secNu2.setVisibility(View.GONE);
                            secNu3.setVisibility(View.GONE);

                        }

                        if ((g.getCallFrom().equals("46")) & o.get("ClassficationCode").equals("22")) {
                            secLo1.setVisibility(View.VISIBLE);


                        } else if ((g.getCallFrom().equals("47")) & o.get("ClassficationCode").equals("23")) {
                            secLo2.setVisibility(View.VISIBLE);


                        } else if ((g.getCallFrom().equals("48")) & o.get("ClassficationCode").equals("24")) {
                            secLo3.setVisibility(View.VISIBLE);


                        } else

                        {
                            secLo1.setVisibility(View.GONE);
                            secLo2.setVisibility(View.GONE);
                            secLo3.setVisibility(View.GONE);

                        }

                        if ((g.getCallFrom().equals("49")) & o.get("ClassficationCode").equals("25")) {
                            secOth1.setVisibility(View.VISIBLE);
                            VOth1.setText(Global.Getclassfication(C, o.get("ClassficationCode")));

                        } else if ((g.getCallFrom().equals("50")) & o.get("ClassficationCode").equals("26")) {
                            secOth2.setVisibility(View.VISIBLE);
                            VOth2.setText(Global.Getclassfication(C, o.get("ClassficationCode")));

                        } else if ((g.getCallFrom().equals("51")) & o.get("ClassficationCode").equals("27")) {
                            secOth3.setVisibility(View.VISIBLE);
                            VOth3.setText(Global.Getclassfication(C, o.get("ClassficationCode")));

                        } else if ((g.getCallFrom().equals("52")) & o.get("ClassficationCode").equals("28")) {
                            secOth4.setVisibility(View.VISIBLE);
                            VOth4.setText(Global.Getclassfication(C, o.get("ClassficationCode")));

                        } else if ((g.getCallFrom().equals("53")) & o.get("ClassficationCode").equals("29")) {
                            secOth5.setVisibility(View.VISIBLE);
                            VOth5.setText(Global.Getclassfication(C, o.get("ClassficationCode")));

                        } else if ((g.getCallFrom().equals("54")) & o.get("ClassficationCode").equals("30")) {
                            secOth6.setVisibility(View.VISIBLE);
                            VOth6.setText(Global.Getclassfication(C, o.get("ClassficationCode")));

                        } else if ((g.getCallFrom().equals("55")) & o.get("ClassficationCode").equals("31")) {
                            secOth7.setVisibility(View.VISIBLE);
                            VOth7.setText(Global.Getclassfication(C, o.get("ClassficationCode")));

                        } else

                        {
                            secOth1.setVisibility(View.GONE);
                            secOth2.setVisibility(View.GONE);
                            secOth3.setVisibility(View.GONE);
                            secOth4.setVisibility(View.GONE);
                            secOth5.setVisibility(View.GONE);
                            secOth6.setVisibility(View.GONE);
                            secOth7.setVisibility(View.GONE);


                        }
                    } else if (chkB1.isChecked() == false) {
                        if (chkB1.isChecked() == false & (g.getCallFrom().equals("49")) & o.get("ClassficationCode").equals("25")) {
                            secOth1.setVisibility(View.GONE);

                        } else if (chkB1.isChecked() == false & (g.getCallFrom().equals("50")) & o.get("ClassficationCode").equals("26")) {

                            secOth2.setVisibility(View.GONE);
                        } else if (chkB1.isChecked() == false & (g.getCallFrom().equals("51")) & o.get("ClassficationCode").equals("27")) {
                            secOth3.setVisibility(View.GONE);

                        } else if (chkB1.isChecked() == false & (g.getCallFrom().equals("52")) & o.get("ClassficationCode").equals("28")) {
                            secOth4.setVisibility(View.GONE);

                        } else if (chkB1.isChecked() == false & (g.getCallFrom().equals("53")) & o.get("ClassficationCode").equals("29")) {
                            secOth5.setVisibility(View.GONE);
                        } else if (chkB1.isChecked() == false & (g.getCallFrom().equals("54")) & o.get("ClassficationCode").equals("30")) {
                            secOth6.setVisibility(View.GONE);

                        } else if (chkB1.isChecked() == false & (g.getCallFrom().equals("55")) & o.get("ClassficationCode").equals("31")) {
                            secOth7.setVisibility(View.GONE);

                        }

                        g.setCallFrom("");
                        chkB1.setChecked(false);
                        secB1.setVisibility(View.GONE);
                        secB2.setVisibility(View.GONE);
                        secB3.setVisibility(View.GONE);
                        secD1.setVisibility(View.GONE);
                        secD2.setVisibility(View.GONE);
                        secD3.setVisibility(View.GONE);
                        secF1.setVisibility(View.GONE);
                        secF2.setVisibility(View.GONE);
                        secF3.setVisibility(View.GONE);
                        secDy1.setVisibility(View.GONE);
                        secMa1.setVisibility(View.GONE);
                        secMa2.setVisibility(View.GONE);
                        secNu1.setVisibility(View.GONE);
                        secNu2.setVisibility(View.GONE);
                        secNu3.setVisibility(View.GONE);
                        secLo1.setVisibility(View.GONE);
                        secLo2.setVisibility(View.GONE);
                        secLo3.setVisibility(View.GONE);


                       /* secOth1.setVisibility(View.GONE);
                        secOth2.setVisibility(View.GONE);
                        secOth3.setVisibility(View.GONE);
                        secOth4.setVisibility(View.GONE);
                        secOth5.setVisibility(View.GONE);
                        secOth6.setVisibility(View.GONE);
                        secOth7.setVisibility(View.GONE);
                        VOth1.setText("");
                        VOth2.setText("");
                        VOth3.setText("");
                        VOth4.setText("");
                        VOth5.setText("");
                        VOth6.setText("");
                        VOth7.setText("");*/
                    }


                }
            });
            return convertView;
        }
    }

    private void DataSearch1() {
        try {
            Cursor cur = C.ReadData("Select * from treatment where classficationCode='1'");
            cur.moveToFirst();
            dataList1.clear();
            int slno = 0;
            while (!cur.isAfterLast()) {
                slno += 1;
                HashMap<String, String> map = new HashMap<String, String>();
                ListView list = (ListView) findViewById(R.id.lstData1);

                map.put("Slno", Integer.toString(slno));
                map.put("AgeGroup", cur.getString(cur.getColumnIndex("ageGroup")));
                map.put("TypeCode", cur.getString(cur.getColumnIndex("typeCode")));
                map.put("TreatmentCode", cur.getString(cur.getColumnIndex("treatmentCode")));
                map.put("TretmentDes", cur.getString(cur.getColumnIndex("tretmentDes")));
                map.put("ClassficationCode", cur.getString(cur.getColumnIndex("classficationCode")));


                dataList1.add(map);

                dataAdapter1 = new SimpleAdapter(SymtomAG2to5Y.this, dataList1, R.layout.treatmentrow, new String[]{"click"},
                        new int[]{R.id.chkB1});

                list.setAdapter(new DataListAdapter1(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(SymtomAG2to5Y.this, e.getMessage());
            return;
        }
    }

    private void DataSearch2() {
        try {
            Cursor cur = C.ReadData("Select * from treatment where classficationCode='2'");
            cur.moveToFirst();
            dataList1.clear();
            int slno = 0;
            while (!cur.isAfterLast()) {
                slno += 1;
                HashMap<String, String> map = new HashMap<String, String>();
                ListView list = (ListView) findViewById(R.id.lstData2);

                map.put("Slno", Integer.toString(slno));
                map.put("AgeGroup", cur.getString(cur.getColumnIndex("ageGroup")));
                map.put("TypeCode", cur.getString(cur.getColumnIndex("typeCode")));
                map.put("TreatmentCode", cur.getString(cur.getColumnIndex("treatmentCode")));
                map.put("TretmentDes", cur.getString(cur.getColumnIndex("tretmentDes")));
                map.put("ClassficationCode", cur.getString(cur.getColumnIndex("classficationCode")));


                dataList1.add(map);

                dataAdapter1 = new SimpleAdapter(SymtomAG2to5Y.this, dataList1, R.layout.treatmentrow, new String[]{"click"},
                        new int[]{R.id.chkB1});

                list.setAdapter(new DataListAdapter1(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(SymtomAG2to5Y.this, e.getMessage());
            return;
        }
    }

    private void DataSearch3() {
        try {
            Cursor cur = C.ReadData("Select * from treatment where classficationCode='3'");
            cur.moveToFirst();
            dataList1.clear();
            int slno = 0;
            while (!cur.isAfterLast()) {
                slno += 1;
                HashMap<String, String> map = new HashMap<String, String>();
                ListView list = (ListView) findViewById(R.id.lstData3);

                map.put("Slno", Integer.toString(slno));
                map.put("AgeGroup", cur.getString(cur.getColumnIndex("ageGroup")));
                map.put("TypeCode", cur.getString(cur.getColumnIndex("typeCode")));
                map.put("TreatmentCode", cur.getString(cur.getColumnIndex("treatmentCode")));
                map.put("TretmentDes", cur.getString(cur.getColumnIndex("tretmentDes")));
                map.put("ClassficationCode", cur.getString(cur.getColumnIndex("classficationCode")));


                dataList1.add(map);

                dataAdapter1 = new SimpleAdapter(SymtomAG2to5Y.this, dataList1, R.layout.treatmentrow, new String[]{"click"},
                        new int[]{R.id.chkB1});

                list.setAdapter(new DataListAdapter1(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(SymtomAG2to5Y.this, e.getMessage());
            return;
        }
    }

    private void DataSearchD1() {
        try {
            Cursor cur = C.ReadData("Select * from treatment where classficationCode='9'");
            cur.moveToFirst();
            dataList1.clear();
            int slno = 0;
            while (!cur.isAfterLast()) {
                slno += 1;
                HashMap<String, String> map = new HashMap<String, String>();
                ListView list = (ListView) findViewById(R.id.lstDataD2);

                map.put("Slno", Integer.toString(slno));
                map.put("AgeGroup", cur.getString(cur.getColumnIndex("ageGroup")));
                map.put("TypeCode", cur.getString(cur.getColumnIndex("typeCode")));
                map.put("TreatmentCode", cur.getString(cur.getColumnIndex("treatmentCode")));
                map.put("TretmentDes", cur.getString(cur.getColumnIndex("tretmentDes")));
                map.put("ClassficationCode", cur.getString(cur.getColumnIndex("classficationCode")));


                dataList1.add(map);

                dataAdapter1 = new SimpleAdapter(SymtomAG2to5Y.this, dataList1, R.layout.treatmentrow, new String[]{"click"},
                        new int[]{R.id.chkB1});

                list.setAdapter(new DataListAdapter1(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(SymtomAG2to5Y.this, e.getMessage());
            return;
        }
    }

    private void DataSearchD2() {
        try {
            Cursor cur = C.ReadData("Select * from treatment where classficationCode='10'");
            cur.moveToFirst();
            dataList1.clear();
            int slno = 0;
            while (!cur.isAfterLast()) {
                slno += 1;
                HashMap<String, String> map = new HashMap<String, String>();
                ListView list = (ListView) findViewById(R.id.lstDataD3);

                map.put("Slno", Integer.toString(slno));
                map.put("AgeGroup", cur.getString(cur.getColumnIndex("ageGroup")));
                map.put("TypeCode", cur.getString(cur.getColumnIndex("typeCode")));
                map.put("TreatmentCode", cur.getString(cur.getColumnIndex("treatmentCode")));
                map.put("TretmentDes", cur.getString(cur.getColumnIndex("tretmentDes")));
                map.put("ClassficationCode", cur.getString(cur.getColumnIndex("classficationCode")));


                dataList1.add(map);

                dataAdapter1 = new SimpleAdapter(SymtomAG2to5Y.this, dataList1, R.layout.treatmentrow, new String[]{"click"},
                        new int[]{R.id.chkB1});

                list.setAdapter(new DataListAdapter1(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(SymtomAG2to5Y.this, e.getMessage());
            return;
        }
    }

    private void DataSearchD3() {
        try {
            Cursor cur = C.ReadData("Select * from treatment where classficationCode='11'");
            cur.moveToFirst();
            dataList1.clear();
            int slno = 0;
            while (!cur.isAfterLast()) {
                slno += 1;
                HashMap<String, String> map = new HashMap<String, String>();
                ListView list = (ListView) findViewById(R.id.lstDataD4);

                map.put("Slno", Integer.toString(slno));
                map.put("AgeGroup", cur.getString(cur.getColumnIndex("ageGroup")));
                map.put("TypeCode", cur.getString(cur.getColumnIndex("typeCode")));
                map.put("TreatmentCode", cur.getString(cur.getColumnIndex("treatmentCode")));
                map.put("TretmentDes", cur.getString(cur.getColumnIndex("tretmentDes")));
                map.put("ClassficationCode", cur.getString(cur.getColumnIndex("classficationCode")));


                dataList1.add(map);

                dataAdapter1 = new SimpleAdapter(SymtomAG2to5Y.this, dataList1, R.layout.treatmentrow, new String[]{"click"},
                        new int[]{R.id.chkB1});

                list.setAdapter(new DataListAdapter1(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(SymtomAG2to5Y.this, e.getMessage());
            return;
        }
    }

    private void DataSearchF1() {
        try {
            Cursor cur = C.ReadData("Select * from treatment where classficationCode='12'");
            cur.moveToFirst();
            dataList1.clear();
            int slno = 0;
            while (!cur.isAfterLast()) {
                slno += 1;
                HashMap<String, String> map = new HashMap<String, String>();
                ListView list = (ListView) findViewById(R.id.lstDataF2);

                map.put("Slno", Integer.toString(slno));
                map.put("AgeGroup", cur.getString(cur.getColumnIndex("ageGroup")));
                map.put("TypeCode", cur.getString(cur.getColumnIndex("typeCode")));
                map.put("TreatmentCode", cur.getString(cur.getColumnIndex("treatmentCode")));
                map.put("TretmentDes", cur.getString(cur.getColumnIndex("tretmentDes")));
                map.put("ClassficationCode", cur.getString(cur.getColumnIndex("classficationCode")));


                dataList1.add(map);

                dataAdapter1 = new SimpleAdapter(SymtomAG2to5Y.this, dataList1, R.layout.treatmentrow, new String[]{"click"},
                        new int[]{R.id.chkB1});

                list.setAdapter(new DataListAdapter1(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(SymtomAG2to5Y.this, e.getMessage());
            return;
        }
    }

    private void DataSearchF2() {
        try {
            Cursor cur = C.ReadData("Select * from treatment where classficationCode='13'");
            cur.moveToFirst();
            dataList1.clear();
            int slno = 0;
            while (!cur.isAfterLast()) {
                slno += 1;
                HashMap<String, String> map = new HashMap<String, String>();
                ListView list = (ListView) findViewById(R.id.lstDataF3);

                map.put("Slno", Integer.toString(slno));
                map.put("AgeGroup", cur.getString(cur.getColumnIndex("ageGroup")));
                map.put("TypeCode", cur.getString(cur.getColumnIndex("typeCode")));
                map.put("TreatmentCode", cur.getString(cur.getColumnIndex("treatmentCode")));
                map.put("TretmentDes", cur.getString(cur.getColumnIndex("tretmentDes")));
                map.put("ClassficationCode", cur.getString(cur.getColumnIndex("classficationCode")));


                dataList1.add(map);

                dataAdapter1 = new SimpleAdapter(SymtomAG2to5Y.this, dataList1, R.layout.treatmentrow, new String[]{"click"},
                        new int[]{R.id.chkB1});

                list.setAdapter(new DataListAdapter1(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(SymtomAG2to5Y.this, e.getMessage());
            return;
        }
    }

    private void DataSearchF3() {
        try {
            Cursor cur = C.ReadData("Select * from treatment where classficationCode='14'");
            cur.moveToFirst();
            dataList1.clear();
            int slno = 0;
            while (!cur.isAfterLast()) {
                slno += 1;
                HashMap<String, String> map = new HashMap<String, String>();
                ListView list = (ListView) findViewById(R.id.lstDataF4);

                map.put("Slno", Integer.toString(slno));
                map.put("AgeGroup", cur.getString(cur.getColumnIndex("ageGroup")));
                map.put("TypeCode", cur.getString(cur.getColumnIndex("typeCode")));
                map.put("TreatmentCode", cur.getString(cur.getColumnIndex("treatmentCode")));
                map.put("TretmentDes", cur.getString(cur.getColumnIndex("tretmentDes")));
                map.put("ClassficationCode", cur.getString(cur.getColumnIndex("classficationCode")));


                dataList1.add(map);

                dataAdapter1 = new SimpleAdapter(SymtomAG2to5Y.this, dataList1, R.layout.treatmentrow, new String[]{"click"},
                        new int[]{R.id.chkB1});

                list.setAdapter(new DataListAdapter1(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(SymtomAG2to5Y.this, e.getMessage());
            return;
        }
    }

    private void DataSearchDy1() {
        try {
            Cursor cur = C.ReadData("Select * from treatment where classficationCode='15'");
            cur.moveToFirst();
            dataList1.clear();
            int slno = 0;
            while (!cur.isAfterLast()) {
                slno += 1;
                HashMap<String, String> map = new HashMap<String, String>();
                ListView list = (ListView) findViewById(R.id.lstDataDy1);

                map.put("Slno", Integer.toString(slno));
                map.put("AgeGroup", cur.getString(cur.getColumnIndex("ageGroup")));
                map.put("TypeCode", cur.getString(cur.getColumnIndex("typeCode")));
                map.put("TreatmentCode", cur.getString(cur.getColumnIndex("treatmentCode")));
                map.put("TretmentDes", cur.getString(cur.getColumnIndex("tretmentDes")));
                map.put("ClassficationCode", cur.getString(cur.getColumnIndex("classficationCode")));


                dataList1.add(map);

                dataAdapter1 = new SimpleAdapter(SymtomAG2to5Y.this, dataList1, R.layout.treatmentrow, new String[]{"click"},
                        new int[]{R.id.chkB1});

                list.setAdapter(new DataListAdapter1(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(SymtomAG2to5Y.this, e.getMessage());
            return;
        }
    }

    private void DataSearchFv1() {
        try {
            Cursor cur = C.ReadData("Select * from treatment where classficationCode='16'");
            cur.moveToFirst();
            dataList1.clear();
            int slno = 0;
            while (!cur.isAfterLast()) {
                slno += 1;
                HashMap<String, String> map = new HashMap<String, String>();
                ListView list = (ListView) findViewById(R.id.lstDataFv1);

                map.put("Slno", Integer.toString(slno));
                map.put("AgeGroup", cur.getString(cur.getColumnIndex("ageGroup")));
                map.put("TypeCode", cur.getString(cur.getColumnIndex("typeCode")));
                map.put("TreatmentCode", cur.getString(cur.getColumnIndex("treatmentCode")));
                map.put("TretmentDes", cur.getString(cur.getColumnIndex("tretmentDes")));
                map.put("ClassficationCode", cur.getString(cur.getColumnIndex("classficationCode")));


                dataList1.add(map);

                dataAdapter1 = new SimpleAdapter(SymtomAG2to5Y.this, dataList1, R.layout.treatmentrow, new String[]{"click"},
                        new int[]{R.id.chkB1});

                list.setAdapter(new DataListAdapter1(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(SymtomAG2to5Y.this, e.getMessage());
            return;
        }
    }

    private void DataSearchMa1() {
        try {
            Cursor cur = C.ReadData("Select * from treatment where classficationCode='17'");
            cur.moveToFirst();
            dataList1.clear();
            int slno = 0;
            while (!cur.isAfterLast()) {
                slno += 1;
                HashMap<String, String> map = new HashMap<String, String>();
                ListView list = (ListView) findViewById(R.id.lstDataMa1);

                map.put("Slno", Integer.toString(slno));
                map.put("AgeGroup", cur.getString(cur.getColumnIndex("ageGroup")));
                map.put("TypeCode", cur.getString(cur.getColumnIndex("typeCode")));
                map.put("TreatmentCode", cur.getString(cur.getColumnIndex("treatmentCode")));
                map.put("TretmentDes", cur.getString(cur.getColumnIndex("tretmentDes")));
                map.put("ClassficationCode", cur.getString(cur.getColumnIndex("classficationCode")));


                dataList1.add(map);

                dataAdapter1 = new SimpleAdapter(SymtomAG2to5Y.this, dataList1, R.layout.treatmentrow, new String[]{"click"},
                        new int[]{R.id.chkB1});

                list.setAdapter(new DataListAdapter1(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(SymtomAG2to5Y.this, e.getMessage());
            return;
        }
    }

    private void DataSearchMa2() {
        try {
            Cursor cur = C.ReadData("Select * from treatment where classficationCode='18'");
            cur.moveToFirst();
            dataList1.clear();
            int slno = 0;
            while (!cur.isAfterLast()) {
                slno += 1;
                HashMap<String, String> map = new HashMap<String, String>();
                ListView list = (ListView) findViewById(R.id.lstDataMa2);

                map.put("Slno", Integer.toString(slno));
                map.put("AgeGroup", cur.getString(cur.getColumnIndex("ageGroup")));
                map.put("TypeCode", cur.getString(cur.getColumnIndex("typeCode")));
                map.put("TreatmentCode", cur.getString(cur.getColumnIndex("treatmentCode")));
                map.put("TretmentDes", cur.getString(cur.getColumnIndex("tretmentDes")));
                map.put("ClassficationCode", cur.getString(cur.getColumnIndex("classficationCode")));


                dataList1.add(map);

                dataAdapter1 = new SimpleAdapter(SymtomAG2to5Y.this, dataList1, R.layout.treatmentrow, new String[]{"click"},
                        new int[]{R.id.chkB1});

                list.setAdapter(new DataListAdapter1(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(SymtomAG2to5Y.this, e.getMessage());
            return;
        }
    }

    private void DataSearchNu1() {
        try {
            Cursor cur = C.ReadData("Select * from treatment where classficationCode='19'");
            cur.moveToFirst();
            dataList1.clear();
            int slno = 0;
            while (!cur.isAfterLast()) {
                slno += 1;
                HashMap<String, String> map = new HashMap<String, String>();
                ListView list = (ListView) findViewById(R.id.lstDataNu1);

                map.put("Slno", Integer.toString(slno));
                map.put("AgeGroup", cur.getString(cur.getColumnIndex("ageGroup")));
                map.put("TypeCode", cur.getString(cur.getColumnIndex("typeCode")));
                map.put("TreatmentCode", cur.getString(cur.getColumnIndex("treatmentCode")));
                map.put("TretmentDes", cur.getString(cur.getColumnIndex("tretmentDes")));
                map.put("ClassficationCode", cur.getString(cur.getColumnIndex("classficationCode")));


                dataList1.add(map);

                dataAdapter1 = new SimpleAdapter(SymtomAG2to5Y.this, dataList1, R.layout.treatmentrow, new String[]{"click"},
                        new int[]{R.id.chkB1});

                list.setAdapter(new DataListAdapter1(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(SymtomAG2to5Y.this, e.getMessage());
            return;
        }
    }

    private void DataSearchNu2() {
        try {
            Cursor cur = C.ReadData("Select * from treatment where classficationCode='20'");
            cur.moveToFirst();
            dataList1.clear();
            int slno = 0;
            while (!cur.isAfterLast()) {
                slno += 1;
                HashMap<String, String> map = new HashMap<String, String>();
                ListView list = (ListView) findViewById(R.id.lstDataNu2);

                map.put("Slno", Integer.toString(slno));
                map.put("AgeGroup", cur.getString(cur.getColumnIndex("ageGroup")));
                map.put("TypeCode", cur.getString(cur.getColumnIndex("typeCode")));
                map.put("TreatmentCode", cur.getString(cur.getColumnIndex("treatmentCode")));
                map.put("TretmentDes", cur.getString(cur.getColumnIndex("tretmentDes")));
                map.put("ClassficationCode", cur.getString(cur.getColumnIndex("classficationCode")));


                dataList1.add(map);

                dataAdapter1 = new SimpleAdapter(SymtomAG2to5Y.this, dataList1, R.layout.treatmentrow, new String[]{"click"},
                        new int[]{R.id.chkB1});

                list.setAdapter(new DataListAdapter1(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(SymtomAG2to5Y.this, e.getMessage());
            return;
        }
    }

    private void DataSearchNu3() {
        try {
            Cursor cur = C.ReadData("Select * from treatment where classficationCode='21'");
            cur.moveToFirst();
            dataList1.clear();
            int slno = 0;
            while (!cur.isAfterLast()) {
                slno += 1;
                HashMap<String, String> map = new HashMap<String, String>();
                ListView list = (ListView) findViewById(R.id.lstDataNu3);

                map.put("Slno", Integer.toString(slno));
                map.put("AgeGroup", cur.getString(cur.getColumnIndex("ageGroup")));
                map.put("TypeCode", cur.getString(cur.getColumnIndex("typeCode")));
                map.put("TreatmentCode", cur.getString(cur.getColumnIndex("treatmentCode")));
                map.put("TretmentDes", cur.getString(cur.getColumnIndex("tretmentDes")));
                map.put("ClassficationCode", cur.getString(cur.getColumnIndex("classficationCode")));


                dataList1.add(map);

                dataAdapter1 = new SimpleAdapter(SymtomAG2to5Y.this, dataList1, R.layout.treatmentrow, new String[]{"click"},
                        new int[]{R.id.chkB1});

                list.setAdapter(new DataListAdapter1(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(SymtomAG2to5Y.this, e.getMessage());
            return;
        }
    }

    private void DataSearchLo1() {
        try {
            Cursor cur = C.ReadData("Select * from treatment where classficationCode='22'");
            cur.moveToFirst();
            dataList1.clear();
            int slno = 0;
            while (!cur.isAfterLast()) {
                slno += 1;
                HashMap<String, String> map = new HashMap<String, String>();
                ListView list = (ListView) findViewById(R.id.lstDataLo1);

                map.put("Slno", Integer.toString(slno));
                map.put("AgeGroup", cur.getString(cur.getColumnIndex("ageGroup")));
                map.put("TypeCode", cur.getString(cur.getColumnIndex("typeCode")));
                map.put("TreatmentCode", cur.getString(cur.getColumnIndex("treatmentCode")));
                map.put("TretmentDes", cur.getString(cur.getColumnIndex("tretmentDes")));
                map.put("ClassficationCode", cur.getString(cur.getColumnIndex("classficationCode")));


                dataList1.add(map);

                dataAdapter1 = new SimpleAdapter(SymtomAG2to5Y.this, dataList1, R.layout.treatmentrow, new String[]{"click"},
                        new int[]{R.id.chkB1});

                list.setAdapter(new DataListAdapter1(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(SymtomAG2to5Y.this, e.getMessage());
            return;
        }
    }

    private void DataSearchLo2() {
        try {
            Cursor cur = C.ReadData("Select * from treatment where classficationCode='23'");
            cur.moveToFirst();
            dataList1.clear();
            int slno = 0;
            while (!cur.isAfterLast()) {
                slno += 1;
                HashMap<String, String> map = new HashMap<String, String>();
                ListView list = (ListView) findViewById(R.id.lstDataLo2);

                map.put("Slno", Integer.toString(slno));
                map.put("AgeGroup", cur.getString(cur.getColumnIndex("ageGroup")));
                map.put("TypeCode", cur.getString(cur.getColumnIndex("typeCode")));
                map.put("TreatmentCode", cur.getString(cur.getColumnIndex("treatmentCode")));
                map.put("TretmentDes", cur.getString(cur.getColumnIndex("tretmentDes")));
                map.put("ClassficationCode", cur.getString(cur.getColumnIndex("classficationCode")));


                dataList1.add(map);

                dataAdapter1 = new SimpleAdapter(SymtomAG2to5Y.this, dataList1, R.layout.treatmentrow, new String[]{"click"},
                        new int[]{R.id.chkB1});

                list.setAdapter(new DataListAdapter1(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(SymtomAG2to5Y.this, e.getMessage());
            return;
        }
    }

    private void DataSearchLo3() {
        try {
            Cursor cur = C.ReadData("Select * from treatment where classficationCode='24'");
            cur.moveToFirst();
            dataList1.clear();
            int slno = 0;
            while (!cur.isAfterLast()) {
                slno += 1;
                HashMap<String, String> map = new HashMap<String, String>();
                ListView list = (ListView) findViewById(R.id.lstDataLo3);

                map.put("Slno", Integer.toString(slno));
                map.put("AgeGroup", cur.getString(cur.getColumnIndex("ageGroup")));
                map.put("TypeCode", cur.getString(cur.getColumnIndex("typeCode")));
                map.put("TreatmentCode", cur.getString(cur.getColumnIndex("treatmentCode")));
                map.put("TretmentDes", cur.getString(cur.getColumnIndex("tretmentDes")));
                map.put("ClassficationCode", cur.getString(cur.getColumnIndex("classficationCode")));


                dataList1.add(map);

                dataAdapter1 = new SimpleAdapter(SymtomAG2to5Y.this, dataList1, R.layout.treatmentrow, new String[]{"click"},
                        new int[]{R.id.chkB1});

                list.setAdapter(new DataListAdapter1(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(SymtomAG2to5Y.this, e.getMessage());
            return;
        }
    }

    public class DataListAdapter1 extends BaseAdapter {
        private Context context;

        public DataListAdapter1(Context c) {
            context = c;
        }

        public int getCount() {
            return dataAdapter1.getCount();
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
                convertView = inflater.inflate(R.layout.treatmentrow, null);
            }
            final CheckBox chkT1 = (CheckBox) convertView.findViewById(R.id.chkT1);
            final TextView treatment = (TextView) convertView.findViewById(R.id.treatment);

            final HashMap<String, String> o = (HashMap<String, String>) dataAdapter1.getItem(position);
            //SlNo.setText(o.get("SlNo"));
            o.get("AgeGroup");
            o.get("TypeCode");
            o.get("TreatmentCode");
            treatment.setText(o.get("TretmentDes"));
            o.get("ClassficationCode");

            return convertView;
        }
    }

}

