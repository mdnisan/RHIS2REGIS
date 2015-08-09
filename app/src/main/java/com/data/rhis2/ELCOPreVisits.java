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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Common.Connection;
import Common.Global;

/**
 * Created by ccah on 14/05/2015.
 */
public class ELCOPreVisits extends Activity {
    boolean netwoekAvailable=false;
    Location currentLocation;
    double currentLatitude,currentLongitude;
    Location currentLocationNet;
    double currentLatitudeNet,currentLongitudeNet;
    //Disabled Back/Home key
    //--------------------------------------------------------------------------------------------------
    @Override
    public boolean onKeyDown(int iKeyCode, KeyEvent event)
    {
        if(iKeyCode == KeyEvent.KEYCODE_BACK || iKeyCode == KeyEvent.KEYCODE_HOME)
        { return false; }
        else { return true;  }
    }
    //Top menu
    //--------------------------------------------------------------------------------------------------
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mnuclose, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder adb = new AlertDialog.Builder(ELCOPreVisits.this);
        switch (item.getItemId()) {
            case R.id.menuClose:
                adb.setTitle("Close");
                adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান[Yes/No]?");
                adb.setNegativeButton("No", null);
                adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        Intent f2 = new Intent(getApplicationContext(),ELCOForm.class);
                        startActivity(f2);
                    }});
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
    private String TableNameELCOVisit;


    RadioButton rdoS77d1;
    RadioButton rdoS77d2;

    String StartTime;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.elcoprevisit);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();
            final ListView list = (ListView) findViewById(R.id.lstData);
            View header = getLayoutInflater().inflate(R.layout.elcoprevisitheading, null);
            list.addHeaderView(header);

            TableNameELCOVisit = "ELCOVisit";
            DataSearch(g.getDistrict(), g.getUpazila(), g.getUnion(), g.getMouza(), g.getVillage(), g.getHouseholdNo(), g.getSerialNo() );

        }
        catch(Exception  e)
        {
            Connection.MessageBox(ELCOPreVisits.this, e.getMessage());
            return;
        }
    }

    private void DataSearch(String Dist, String Upz, String UN, String Mouza, String Vill, String HHNo, String SNo)
    {
        try
        {

            Cursor cur = C.ReadData("Select * from "+ TableNameELCOVisit +"  Where Dist='"+ Dist +"' and Upz='"+ Upz +"' and UN='"+ UN +"' and Mouza='"+ Mouza +"' and Vill='"+ Vill +"' and HHNo='"+ HHNo +"' and SNo='"+ SNo +"'");
            cur.moveToFirst();
            dataList.clear();
            while(!cur.isAfterLast())
            {
                HashMap<String, String> map = new HashMap<String, String>();
                ListView list = (ListView)findViewById(R.id.lstData);


                map.put("Visit", cur.getString(cur.getColumnIndex("Visit")));
                map.put("VDate", cur.getString(cur.getColumnIndex("VDate")));
                //map.put("VisitStatus", cur.getString(cur.getColumnIndex("VisitStatus")));
                if(cur.getString(cur.getColumnIndex("VisitStatus")).equals("1"))
                {
                    map.put("VisitStatus","উপস্থিত");
                }
                else if(cur.getString(cur.getColumnIndex("VisitStatus")).equals("2"))
                {
                    map.put("VisitStatus","অনুপস্থিত");
                }
                //map.put("CurrStatus", cur.getString(cur.getColumnIndex("CurrStatus")));
                if(cur.getString(cur.getColumnIndex("CurrStatus")).equals("01"))
                {
                    map.put("CurrStatus","খাবার বড়ি");
                }
                else if(cur.getString(cur.getColumnIndex("CurrStatus")).equals("02"))
                {
                    map.put("CurrStatus","কনডম");
                }
                else if(cur.getString(cur.getColumnIndex("CurrStatus")).equals("03"))
                {
                    map.put("CurrStatus","ইনজেকটেবল");
                }
                else if(cur.getString(cur.getColumnIndex("CurrStatus")).equals("04"))
                {
                    map.put("CurrStatus","আই ইউ ডি");
                }
                else if(cur.getString(cur.getColumnIndex("CurrStatus")).equals("05"))
                {
                    map.put("CurrStatus","ইমপ্যান্ট");
                }
                else if(cur.getString(cur.getColumnIndex("CurrStatus")).equals("06"))
                {
                    map.put("CurrStatus","স্থায়ী পদ্ধতি(পুরুষ)");
                }
                else if(cur.getString(cur.getColumnIndex("CurrStatus")).equals("07"))
                {
                    map.put("CurrStatus","স্থায়ী পদ্ধতি(মহিলা)");
                }
                else if(cur.getString(cur.getColumnIndex("CurrStatus")).equals("08"))
                {
                    map.put("CurrStatus","ইসিপি");
                }
                else if(cur.getString(cur.getColumnIndex("CurrStatus")).equals("09"))
                {
                    map.put("CurrStatus","মিসোপ্রোস্টোল");
                }
                else if(cur.getString(cur.getColumnIndex("CurrStatus")).equals("10"))
                {
                    map.put("CurrStatus","পার্শ্ব-প্রতিক্রিয়ার জন্য প্রেরন ");
                }
                else if(cur.getString(cur.getColumnIndex("CurrStatus")).equals("11"))
                {
                    map.put("CurrStatus","পদ্ধতির জন্য প্রেরন");
                }
                else if(cur.getString(cur.getColumnIndex("CurrStatus")).equals("12"))
                {
                    map.put("CurrStatus","গর্ভবতী");
                }
                else if(cur.getString(cur.getColumnIndex("CurrStatus")).equals("13"))
                {
                    map.put("CurrStatus","জীবিত জন্ম");
                }
                else if(cur.getString(cur.getColumnIndex("CurrStatus")).equals("14"))
                {
                    map.put("CurrStatus","গর্ভ খালাস (জীবিত জন্ম ছাড়া)");
                }
                else if(cur.getString(cur.getColumnIndex("CurrStatus")).equals("15"))
                {
                    map.put("CurrStatus","জরায়ু অপারেশন করে অপসারন");
                }
                else if(cur.getString(cur.getColumnIndex("CurrStatus")).equals("16"))
                {
                    map.put("CurrStatus","স্বামী বিদেশ থাকলে ");
                }
                else if(cur.getString(cur.getColumnIndex("CurrStatus")).equals("17"))
                {
                    map.put("CurrStatus","বন্ধ্যাত্ব বিষয়ক তথ্য ");
                }
                else if(cur.getString(cur.getColumnIndex("CurrStatus")).equals("18"))
                {
                    map.put("CurrStatus","অন্য যে কোন অবস্থা ");
                }

                map.put("MSDate", cur.getString(cur.getColumnIndex("MSDate")));

                dataList.add(map);

                dataAdapter = new SimpleAdapter(ELCOPreVisits.this, dataList, R.layout.elcoprevisitrow,new String[] {"edit","del"},
                        new int[] {R.id.cmdB1,R.id.cmdB2});

                list.setAdapter(new DataListAdapter(this));

                cur.moveToNext();
            }
            cur.close();
        }
        catch(Exception  e)
        {
            Connection.MessageBox(ELCOPreVisits.this, e.getMessage());
            return;
        }
    }


    public class DataListAdapter extends BaseAdapter
    {
        private Context context;
        public DataListAdapter(Context c){ context = c;  }
        public int getCount() {  return dataAdapter.getCount();  }
        public Object getItem(int position) {  return position;  }
        public long getItemId(int position) {  return position;  }
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.elcoprevisitrow, null);
            }
            Button   cmdB1 = (Button)convertView.findViewById(R.id.cmdB1);
            Button   cmdB2 = (Button)convertView.findViewById(R.id.cmdB2);

            final TextView Visit = (TextView)convertView.findViewById(R.id.Visit);
            final TextView VDate = (TextView)convertView.findViewById(R.id.Vdate);
            final TextView VisitStatus = (TextView)convertView.findViewById(R.id.VisitStatus);
            final TextView CurrStatus = (TextView)convertView.findViewById(R.id.CurrStatus);
            final TextView MSDate = (TextView)convertView.findViewById(R.id.MSDate);

            final HashMap<String, String> o = (HashMap<String, String>) dataAdapter.getItem(position);
            Visit.setText(o.get("Visit"));
            VDate.setText(Global.DateConvertDMY(o.get("VDate")));
            //VDate.setText(o.get("VDate"));
            VisitStatus.setText(o.get("VisitStatus"));
            CurrStatus.setText(o.get("CurrStatus"));
            //MSDate.setText(o.get("MSDate"));
            MSDate.setText(Global.DateConvertDMY(o.get("MSDate")));
            final AlertDialog.Builder adb = new AlertDialog.Builder(ELCOPreVisits.this);
            cmdB1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    adb.setTitle("Message:");
                    adb.setMessage("Do you want to update this information?[Yes/No]?");
                    adb.setNegativeButton("No", null);
                    adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            /*Cursor cur=C.ReadData("Select * from "+ TableNameELCOVisit +"  Where IdNo='"+ txtIdNo.getText() +"' and SlNo='"+ o.get("SlNo") +"'");
                            cur.moveToFirst();
                            while(!cur.isAfterLast())
                            {
                                txtIdNo.setText(cur.getString(cur.getColumnIndex("IdNo")));
                                txtSlNo.setText(cur.getString(cur.getColumnIndex("SlNo")));
                                txtS77a.setText(cur.getString(cur.getColumnIndex("S77a")));
                                if(cur.getString(cur.getColumnIndex("S77b")).equals("00"))
                                {
                                    spnS77b.setSelection(0);
                                }
                                else if(cur.getString(cur.getColumnIndex("S77b")).equals("01"))
                                {
                                    spnS77b.setSelection(1);
                                }
                                else if(cur.getString(cur.getColumnIndex("S77b")).equals("02"))
                                {
                                    spnS77b.setSelection(2);
                                }
                                txtS77c.setText(cur.getString(cur.getColumnIndex("S77c")));
                                if(cur.getString(cur.getColumnIndex("S77d")).equalsIgnoreCase("1"))
                                {
                                    rdoS77d1.setChecked(true);
                                }
                                else if(cur.getString(cur.getColumnIndex("S77d")).equalsIgnoreCase("2"))
                                {
                                    rdoS77d2.setChecked(true);
                                }
                                cur.moveToNext();
                            }
                            cur.close();
                            DataSearch(g.getDistrict(), g.getUpazila(), g.getUnion(), g.getMouza(), g.getVillage(), g.getHouseholdNo(), g.getSerialNo() );
                        */}});
                    adb.show();
                }
            });
            cmdB2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    adb.setTitle("Message:");
                    adb.setMessage("Do you want to remove this information?[Yes/No]?");
                    adb.setNegativeButton("No", null);
                    adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {/*
                            Cursor cur=C.ReadData("Delete from "+ TableNameELCOVisit +"  Where IdNo='"+ txtIdNo.getText() +"' and SlNo='"+ o.get("SlNo") +"'");
                            cur.moveToFirst();
                            while(!cur.isAfterLast())
                            {

                                cur.moveToNext();
                            }

                            cur.close();
                            DataSearch(g.getDistrict(), g.getUpazila(), g.getUnion(), g.getMouza(), g.getVillage(), g.getHouseholdNo(), g.getSerialNo() );
                        */}});
                    adb.show();
                }
            });
            return convertView;
        }
    }

}
