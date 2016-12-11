package com.data.rhis2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import Common.Connection;
import Common.Global;

/**
 * Created by Nisan on 11/18/2015.
 */
public class epischdulwoman extends Activity {

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
        AlertDialog.Builder adb = new AlertDialog.Builder(epischdulwoman.this);
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
    private String TableName;

    TextView lblHlblepireg;
    String StartTime;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.schedulwoman);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();
            lblHlblepireg = (TextView) findViewById(R.id.lblHlblepireg);

            if (g.getepiSubBlockId().equals("1")) {
                lblHlblepireg.setText(lblHlblepireg.getText() + ":-" + "ক ১");

            } else if (g.getepiSubBlockId().equals("2")) {
                lblHlblepireg.setText(lblHlblepireg.getText() + ":-" + "ক ২");

            } else if (g.getepiSubBlockId().equals("3")) {
                lblHlblepireg.setText(lblHlblepireg.getText() + ":-" + "খ ১");

            } else if (g.getepiSubBlockId().equals("4")) {
                lblHlblepireg.setText(lblHlblepireg.getText() + ":-" + "খ ২");

            } else if (g.getepiSubBlockId().equals("5")) {

                lblHlblepireg.setText(lblHlblepireg.getText() + ":-" + "গ ১");
            } else if (g.getepiSubBlockId().equals("6")) {
                lblHlblepireg.setText(lblHlblepireg.getText() + ":-" + "গ ২");

            } else if (g.getepiSubBlockId().equals("7")) {
                lblHlblepireg.setText(lblHlblepireg.getText() + ":-" + "ঘ ১");

            } else if (g.getepiSubBlockId().equals("8")) {
                lblHlblepireg.setText(lblHlblepireg.getText() + ":-" + "ঘ ২");

            } else {
                lblHlblepireg.setText(lblHlblepireg.getText());
            }


            final ListView list = (ListView) findViewById(R.id.lstData);
            View header = getLayoutInflater().inflate(R.layout.schedulheading, null);
            list.addHeaderView(header);

            TableName = "epiSchedulerWoman";
            DataSearch();
        } catch (Exception e) {
            Connection.MessageBox(epischdulwoman.this, e.getMessage());
            return;
        }
    }


    private void DataSearch() {
        try {


            Cursor cur = C.ReadData("Select * from " + TableName + "  Where subBlockId='" + g.getepiSubBlockId() + "'");
            cur.moveToFirst();
            dataList.clear();
            int slno = 0;
            while (!cur.isAfterLast()) {
                slno += 1;
                HashMap<String, String> map = new HashMap<String, String>();
                ListView list = (ListView) findViewById(R.id.lstData);

                map.put("Slno", Integer.toString(slno));
                map.put("SchedulerId", cur.getString(cur.getColumnIndex("schedulerId")));
                map.put("ScheduleDate", cur.getString(cur.getColumnIndex("scheduleDate")));
                map.put("SubBlockId", cur.getString(cur.getColumnIndex("subBlockId")));
                map.put("CenterName", cur.getString(cur.getColumnIndex("centerName")));


                dataList.add(map);

                dataAdapter = new SimpleAdapter(epischdulwoman.this, dataList, R.layout.schedulrow, new String[]{"click"},
                        new int[]{R.id.cmdB1});

                list.setAdapter(new DataListAdapter(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(epischdulwoman.this, e.getMessage());
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
                convertView = inflater.inflate(R.layout.schedulrow, null);
            }
            Button cmdB1 = (Button) convertView.findViewById(R.id.cmdB1);


            final TextView epidate = (TextView) convertView.findViewById(R.id.epidate);
            final TextView epicenter = (TextView) convertView.findViewById(R.id.epicenter);


            final HashMap<String, String> o = (HashMap<String, String>) dataAdapter.getItem(position);

            o.get("SchedulerId");

            epidate.setText(Global.DateConvertDMY(o.get("ScheduleDate")));
            epicenter.setText(o.get("CenterName"));

            final AlertDialog.Builder adb = new AlertDialog.Builder(epischdulwoman.this);
            cmdB1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    adb.setTitle("Message:");
                    adb.setMessage("তারিখ:" + Global.DateConvertDMY(o.get("ScheduleDate")) + Html.fromHtml("<br/>") + "কেন্দ্রের নাম:" + o.get("CenterName"));
                    adb.setNegativeButton("না", null);
                    adb.setPositiveButton("হাঁ", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            g.setepiCallForm("");
                            g.setepiCallForm("1");
                            g.setepiSchedulerId(o.get("SchedulerId"));

                            g.setepiScheduleDate(Global.DateConvertDMY(o.get("ScheduleDate")));
                            g.setepiCenterName(o.get("CenterName"));
                            finish();
                            Intent f1 = new Intent(getApplicationContext(), episessionwoman.class);
                            startActivity(f1);

                        }
                    });
                    adb.show();
                }
            });

            return convertView;
        }
    }

}
