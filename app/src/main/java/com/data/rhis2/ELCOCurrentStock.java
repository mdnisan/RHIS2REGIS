package com.data.rhis2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import Common.Connection;
import Common.Global;

/**
 * Created by ccah on 14/05/2015.
 */
public class ELCOCurrentStock extends Activity {
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
        //AlertDialog.Builder adb = new AlertDialog.Builder(ELCOCurrentStock.this);
        switch (item.getItemId()) {
            case R.id.menuClose:
                finish();
               /* adb.setTitle("Close");
                adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান[Yes/No]?");
                adb.setNegativeButton("না", null);
                adb.setPositiveButton("হাঁ", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                       // Intent f2 = new Intent(getApplicationContext(),ELCOForm.class);
                      //  startActivity(f2);
                    }});
                adb.show();*/
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
        try {
            setContentView(R.layout.elcocurrentstock);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();
            final ListView list = (ListView) findViewById(R.id.lstData);
            View header = getLayoutInflater().inflate(R.layout.elcocurrentstockheading, null);
            list.addHeaderView(header);

            DataSearch();

        } catch (Exception e) {
            Connection.MessageBox(ELCOCurrentStock.this, e.getMessage());
            return;
        }
    }

    private void DataSearch() {
        try {

            Cursor cur = C.ReadData("select I.itemCode as ItemCode,I.itemName as ItemName,I.unit as Unit,C.stockQty as StockQty from currentStock C,item I where C.itemCode=I.itemCode and C.providerId='" + g.getProvCode() + "'");//'"+ Dist +"' and Upz='"+ Upz +"' and UN='"+ UN +"' and Mouza='"+ Mouza +"' and Vill='"+ Vill +"' and HHNo='"+ HHNo +"' and SNo='"+ SNo +"'
            cur.moveToFirst();
            dataList.clear();
            while (!cur.isAfterLast()) {
                HashMap<String, String> map = new HashMap<String, String>();
                ListView list = (ListView) findViewById(R.id.lstData);
                map.put("ItemCode", cur.getString(cur.getColumnIndex("ItemCode")));
                map.put("ItemName", cur.getString(cur.getColumnIndex("ItemName")));
                map.put("Unit", cur.getString(cur.getColumnIndex("Unit")));
                map.put("StockQty", cur.getString(cur.getColumnIndex("StockQty")));
                dataList.add(map);

                dataAdapter = new SimpleAdapter(ELCOCurrentStock.this, dataList, R.layout.elcocurrentstockrow, new String[]{"ItemCode", "ItemName"},
                        new int[]{R.id.ItemCode, R.id.ItemName});

                list.setAdapter(new DataListAdapter(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(ELCOCurrentStock.this, e.getMessage());
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
                convertView = inflater.inflate(R.layout.elcocurrentstockrow, null);
            }

            final TextView ItemCode = (TextView) convertView.findViewById(R.id.ItemCode);
            final TextView ItemName = (TextView) convertView.findViewById(R.id.ItemName);
            final TextView StockQty = (TextView) convertView.findViewById(R.id.StockQty);
            final TextView Unit = (TextView) convertView.findViewById(R.id.Unit);

            final HashMap<String, String> o = (HashMap<String, String>) dataAdapter.getItem(position);
            ItemCode.setText(o.get("ItemCode"));
            ItemName.setText(o.get("ItemName"));
            Unit.setText(o.get("Unit"));
            StockQty.setText(o.get("StockQty"));


            final AlertDialog.Builder adb = new AlertDialog.Builder(ELCOCurrentStock.this);
            return convertView;
        }
    }

}
