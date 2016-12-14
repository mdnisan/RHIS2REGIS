package com.data.rhis2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.BaseAdapter;
import android.widget.Button;
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
public class SymtomAG02M extends Activity {

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
        AlertDialog.Builder adb = new AlertDialog.Builder(SymtomAG02M.this);
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
    LinearLayout secF;
    LinearLayout secF1;
    LinearLayout secF2;
    CheckBox chkB;
    CheckBox chkD;
    CheckBox chkD1;
    CheckBox chkD2;
    CheckBox chkF;
    CheckBox chkF1;
    CheckBox chkF2;
    CheckBox chkB1;
    CheckBox chkB2;
    CheckBox chkB3;

    LinearLayout secListView;
    LinearLayout secListViewD1;
    LinearLayout secListViewD2;
    LinearLayout secListViewD3;
    LinearLayout secListViewF1;
    LinearLayout secListViewF2;
    LinearLayout secListViewF3;
    LinearLayout secListView1;
    LinearLayout secListView2;
    LinearLayout secListView3;
    //TextView lblHlblepireg;
    String StartTime;
    Button cmdSave;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.symtomag02m);
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
            secF = (LinearLayout) findViewById(R.id.secF);
            secF1 = (LinearLayout) findViewById(R.id.secF1);
            secF2 = (LinearLayout) findViewById(R.id.secF2);
            chkB = (CheckBox) findViewById(R.id.chkB);
            chkD = (CheckBox) findViewById(R.id.chkD);
            chkD1 = (CheckBox) findViewById(R.id.chkD1);
            chkD2 = (CheckBox) findViewById(R.id.chkD2);
            chkF = (CheckBox) findViewById(R.id.chkF);
            chkF1 = (CheckBox) findViewById(R.id.chkF1);
            chkF2 = (CheckBox) findViewById(R.id.chkF2);
            chkB1 = (CheckBox) findViewById(R.id.chkB1);
            chkB2 = (CheckBox) findViewById(R.id.chkB2);
            chkB3 = (CheckBox) findViewById(R.id.chkB3);
            cmdSave = (Button) findViewById(R.id.cmdSave);
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

            chkD.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkD.isChecked()) {
                        DataSearchD();
                        secListViewD1.setVisibility(View.VISIBLE);
                    } else {
                        secListViewD1.setVisibility(View.GONE);
                        secD1.setVisibility(View.GONE);
                        secD2.setVisibility(View.GONE);
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

            chkF.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkF.isChecked()) {
                        DataSearchF();
                        secListViewF1.setVisibility(View.VISIBLE);
                    } else {
                        secListViewF1.setVisibility(View.GONE);
                        secF1.setVisibility(View.GONE);
                        secF2.setVisibility(View.GONE);

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
            //  secT1= (LinearLayout) findViewById(R.id.secT1);
            secListView = (LinearLayout) findViewById(R.id.secListView);
            secListViewD1 = (LinearLayout) findViewById(R.id.secListViewD1);
            secListViewD2 = (LinearLayout) findViewById(R.id.secListViewD2);
            secListViewD3 = (LinearLayout) findViewById(R.id.secListViewD3);
            secListViewF1 = (LinearLayout) findViewById(R.id.secListViewF1);
            secListViewF2 = (LinearLayout) findViewById(R.id.secListViewF2);
            secListViewF3 = (LinearLayout) findViewById(R.id.secListViewF3);
            secListView1 = (LinearLayout) findViewById(R.id.secListView1);
            secListView2 = (LinearLayout) findViewById(R.id.secListView2);
            secListView3 = (LinearLayout) findViewById(R.id.secListView3);
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
            secF1.setVisibility(View.GONE);
            secF2.setVisibility(View.GONE);
            secListView.setVisibility(View.GONE);
            secListViewD1.setVisibility(View.GONE);
            secListViewD2.setVisibility(View.GONE);
            secListViewD3.setVisibility(View.GONE);
            secListViewF1.setVisibility(View.GONE);
            secListViewF2.setVisibility(View.GONE);
            secListViewF3.setVisibility(View.GONE);
            secListView1.setVisibility(View.GONE);
            secListView2.setVisibility(View.GONE);
            secListView3.setVisibility(View.GONE);
            // DataSearch();

            cmdSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    //DataSave();
                }
            });

        } catch (Exception e) {
            Connection.MessageBox(SymtomAG02M.this, e.getMessage());
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

    public void Save() {
        try {


            String SQL = "";

            if (!C.Existence("Select healthId,providerId,visitDate,StartTime,EndTime,Upload from " + TableName + "  where healthId='" + g.getHealthID() + "' and providerId='" + g.getProvCode() + "' and visitDate = '" + Global.DateNowDMY() + "'")) {
                // SQL = "Insert into " + TableName + "(healthId,providerId,visitDate,startTime,endTime,systemEntryDate,Upload)Values('"+ g.getHealthID() +"','"+g.getProvCode()+"','"+dtpVDate.getText()+"','"+ StartTime +"','"+ g.CurrentTime24()  +"','"+ Global.DateTimeNowYMDHMS() +"','2')";
                C.Save(SQL);
            }
            SQL = "Update under5Child set upload='2',";
            // SQL+="visitDate = '"+dtpVDate.getText() +"',";
            //  SQL+="remarks = '"+ txtRemarks.getText().toString() +"'";
            SQL += " where healthId='" + g.getHealthID() + "' and providerId='" + g.getProvCode() + "'";
            C.Save(SQL);
            Connection.MessageBox(SymtomAG02M.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
            finish();

            Intent f2 = new Intent(getApplicationContext(), Under5child.class);
            startActivity(f2);

        } catch (Exception e) {
            Connection.MessageBox(SymtomAG02M.this, e.getMessage());
            return;
        }

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

                dataAdapter = new SimpleAdapter(SymtomAG02M.this, dataList, R.layout.symtomrow, new String[]{"click"},
                        new int[]{R.id.chkB1});

                list.setAdapter(new DataListAdapter(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(SymtomAG02M.this, e.getMessage());
            return;
        }
    }


    private void DataSearchD() {
        try {
            Cursor cur = C.ReadData("Select * from " + TableName + "  Where typeCode='2' and ageGroup='" + 1 + "'");
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

                dataAdapter = new SimpleAdapter(SymtomAG02M.this, dataList, R.layout.symtomrow, new String[]{"click"},
                        new int[]{R.id.chkB1});

                list.setAdapter(new DataListAdapter(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(SymtomAG02M.this, e.getMessage());
            return;
        }
    }

    private void DataSearchF() {
        try {
            Cursor cur = C.ReadData("Select * from " + TableName + "  Where typeCode='3' and ageGroup='" + 1 + "'");
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

                dataAdapter = new SimpleAdapter(SymtomAG02M.this, dataList, R.layout.symtomrow, new String[]{"click"},
                        new int[]{R.id.chkB1});

                list.setAdapter(new DataListAdapter(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(SymtomAG02M.this, e.getMessage());
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
                        if ((g.getCallFrom().equals("10")) & o.get("ClassficationCode").equals("4") |
                                (g.getCallFrom().equals("11")) & o.get("ClassficationCode").equals("4") |
                                (g.getCallFrom().equals("12")) & o.get("ClassficationCode").equals("4") |
                                (g.getCallFrom().equals("13")) & o.get("ClassficationCode").equals("5") |
                                (g.getCallFrom().equals("14")) & o.get("ClassficationCode").equals("5") |
                                (g.getCallFrom().equals("15")) & o.get("ClassficationCode").equals("5")) {
                            secD1.setVisibility(View.VISIBLE);


                        } else if ((g.getCallFrom().equals("16")) & o.get("ClassficationCode").equals("6")) {
                            secD2.setVisibility(View.VISIBLE);


                        } else

                        {
                            secD1.setVisibility(View.GONE);
                            secD2.setVisibility(View.GONE);
                        }

                        if ((g.getCallFrom().equals("17")) & o.get("ClassficationCode").equals("7") |
                                (g.getCallFrom().equals("18")) & o.get("ClassficationCode").equals("7") |
                                (g.getCallFrom().equals("19")) & o.get("ClassficationCode").equals("7") |
                                (g.getCallFrom().equals("20")) & o.get("ClassficationCode").equals("7") |
                                (g.getCallFrom().equals("21")) & o.get("ClassficationCode").equals("7"))

                        {
                            secF1.setVisibility(View.VISIBLE);


                        } else if ((g.getCallFrom().equals("22")) & o.get("ClassficationCode").equals("8")) {
                            secF2.setVisibility(View.VISIBLE);


                        } else

                        {
                            secF1.setVisibility(View.GONE);
                            secF2.setVisibility(View.GONE);
                        }
                    } else if (chkB1.isChecked() == false) {
                        g.setCallFrom("");
                        chkB1.setChecked(false);
                        secB1.setVisibility(View.GONE);
                        secB2.setVisibility(View.GONE);
                        secB3.setVisibility(View.GONE);
                        secD1.setVisibility(View.GONE);
                        secD2.setVisibility(View.GONE);
                        secF1.setVisibility(View.GONE);
                        secF2.setVisibility(View.GONE);
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

                dataAdapter1 = new SimpleAdapter(SymtomAG02M.this, dataList1, R.layout.treatmentrow, new String[]{"click"},
                        new int[]{R.id.chkB1});

                list.setAdapter(new DataListAdapter1(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(SymtomAG02M.this, e.getMessage());
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

                dataAdapter1 = new SimpleAdapter(SymtomAG02M.this, dataList1, R.layout.treatmentrow, new String[]{"click"},
                        new int[]{R.id.chkB1});

                list.setAdapter(new DataListAdapter1(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(SymtomAG02M.this, e.getMessage());
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

                dataAdapter1 = new SimpleAdapter(SymtomAG02M.this, dataList1, R.layout.treatmentrow, new String[]{"click"},
                        new int[]{R.id.chkB1});

                list.setAdapter(new DataListAdapter1(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(SymtomAG02M.this, e.getMessage());
            return;
        }
    }

    private void DataSearchD1() {
        try {
            Cursor cur = C.ReadData("Select * from treatment where classficationCode='4'");
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

                dataAdapter1 = new SimpleAdapter(SymtomAG02M.this, dataList1, R.layout.treatmentrow, new String[]{"click"},
                        new int[]{R.id.chkB1});

                list.setAdapter(new DataListAdapter1(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(SymtomAG02M.this, e.getMessage());
            return;
        }
    }

    private void DataSearchD2() {
        try {
            Cursor cur = C.ReadData("Select * from treatment where classficationCode='6'");
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

                dataAdapter1 = new SimpleAdapter(SymtomAG02M.this, dataList1, R.layout.treatmentrow, new String[]{"click"},
                        new int[]{R.id.chkB1});

                list.setAdapter(new DataListAdapter1(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(SymtomAG02M.this, e.getMessage());
            return;
        }
    }

    private void DataSearchF1() {
        try {
            Cursor cur = C.ReadData("Select * from treatment where classficationCode='7'");
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

                dataAdapter1 = new SimpleAdapter(SymtomAG02M.this, dataList1, R.layout.treatmentrow, new String[]{"click"},
                        new int[]{R.id.chkB1});

                list.setAdapter(new DataListAdapter1(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(SymtomAG02M.this, e.getMessage());
            return;
        }
    }

    private void DataSearchF2() {
        try {
            Cursor cur = C.ReadData("Select * from treatment where classficationCode='8'");
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

                dataAdapter1 = new SimpleAdapter(SymtomAG02M.this, dataList1, R.layout.treatmentrow, new String[]{"click"},
                        new int[]{R.id.chkB1});

                list.setAdapter(new DataListAdapter1(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(SymtomAG02M.this, e.getMessage());
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

