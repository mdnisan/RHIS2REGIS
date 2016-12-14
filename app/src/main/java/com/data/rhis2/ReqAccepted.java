package com.data.rhis2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import Common.Connection;
import Common.Global;

/**
 * Created by Nisan on 12/27/2015.
 */
public class ReqAccepted extends Activity {
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
        AlertDialog.Builder adb = new AlertDialog.Builder(ReqAccepted.this);
        switch (item.getItemId()) {
            case R.id.menuClose:
                adb.setTitle("Close");
                adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান[Yes/No]?");
                adb.setNegativeButton("No", null);
                adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
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

    String StartTime;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.reqaccepted);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();
            final ListView list = (ListView) findViewById(R.id.lstData);
            View header = getLayoutInflater().inflate(R.layout.reqacceptedheading, null);
            list.addHeaderView(header);

            TableName = "itemRequest";

            String rqid = C.ReturnSingleValue("Select requestTo from itemRequest where requestTo='" + g.getProvCode() + "'");
            String etDate = C.ReturnSingleValue("Select systemEntryDate from itemRequest where requestTo='" + g.getProvCode() + "'");
            String EDate = String.valueOf(Global.DateDifferenceDays(Global.DateNowDMY(), Global.DateConvertDMY(etDate)));


            if (g.getProvCode().equals(rqid) && Integer.valueOf(EDate) <= 3) {
                DataSearch();
            } else {

            }

        } catch (Exception e) {
            Connection.MessageBox(ReqAccepted.this, e.getMessage());
            return;
        }
    }


    private void DataSearch() {
        try {

            Cursor cur = C.ReadData("Select * from '" + TableName + "'  Where requestStatus='" + '0' + "'");
            cur.moveToFirst();
            dataList.clear();
            while (!cur.isAfterLast()) {
                HashMap<String, String> map = new HashMap<String, String>();
                ListView list = (ListView) findViewById(R.id.lstData);

                map.put("RequestId", cur.getString(cur.getColumnIndex("requestId")));
                map.put("RequestBy", cur.getString(cur.getColumnIndex("requestBy")));
                map.put("RequestTo", cur.getString(cur.getColumnIndex("requestTo")));
                map.put("ItemCode", cur.getString(cur.getColumnIndex("itemCode")));
                map.put("RequestQty", cur.getString(cur.getColumnIndex("requestQty")));
                map.put("RequestStatus", cur.getString(cur.getColumnIndex("requestStatus")));
                map.put("RequestRemarks", cur.getString(cur.getColumnIndex("requestRemarks")));
                if (cur.getString(cur.getColumnIndex("itemCode")).equals("1")) {
                    map.put("ItemCode", "1-খাবার বড়ি");
                } else if (cur.getString(cur.getColumnIndex("itemCode")).equals("2")) {
                    map.put("ItemCode", "2-কনডম");
                } else if (cur.getString(cur.getColumnIndex("itemCode")).equals("3")) {
                    map.put("ItemCode", "3-ইনজেকটেবল");
                } else if (cur.getString(cur.getColumnIndex("itemCode")).equals("9")) {
                    map.put("ItemCode", "9-মিসোপ্রোস্টোল");
                } else if (cur.getString(cur.getColumnIndex("itemCode")).equals("5")) {
                    map.put("ItemCode", "5-সিরিঙ্গ");
                } else if (cur.getString(cur.getColumnIndex("itemCode")).equals("8")) {
                    map.put("ItemCode", "8-ইসিপি ");
                }
                dataList.add(map);

                dataAdapter = new SimpleAdapter(ReqAccepted.this, dataList, R.layout.reqacceptedrow, new String[]{"edit", "del"},
                        new int[]{R.id.cmdB2});

                list.setAdapter(new DataListAdapter(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(ReqAccepted.this, e.getMessage());
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
                convertView = inflater.inflate(R.layout.reqacceptedrow, null);
            }
            //  Button   cmdB1 = (Button)convertView.findViewById(R.id.cmdB1);
            Button cmdB2 = (Button) convertView.findViewById(R.id.cmdB2);

            //
            final TextView RequestId = (TextView) convertView.findViewById(R.id.RequestId);
            final TextView RequestBy = (TextView) convertView.findViewById(R.id.RequestBy);

            final TextView RequestTo = (TextView) convertView.findViewById(R.id.RequestTo);

            final TextView ItemCode = (TextView) convertView.findViewById(R.id.ItemCode);
            final TextView RequestQty = (TextView) convertView.findViewById(R.id.RequestQty);
            final TextView RequestStatus = (TextView) convertView.findViewById(R.id.RequestStatus);
            final TextView RequestRemarks = (TextView) convertView.findViewById(R.id.RequestRemarks);
            //final TextView TransactionId = (TextView)convertView.findViewById(R.id.TransactionId);


            final HashMap<String, String> o = (HashMap<String, String>) dataAdapter.getItem(position);
            // IdNo.setText(o.get("IdNo"));
            RequestId.setText(o.get("RequestId"));
            RequestBy.setText(C.ReturnSingleValue("Select ProvName from ProviderDB where ProvCode='" + o.get("RequestBy") + "'"));

            //RequestBy.setText(o.get("RequestBy"));
            RequestTo.setText(o.get("RequestTo"));
            ItemCode.setText(o.get("ItemCode"));
            RequestQty.setText(o.get("RequestQty"));
            RequestStatus.setText(o.get("RequestStatus"));
            RequestRemarks.setText(o.get("RequestRemarks"));
            //  TransactionId.setText(o.get("TransactionId"));

            final AlertDialog.Builder adb = new AlertDialog.Builder(ReqAccepted.this);

            cmdB2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    adb.setTitle("Message:");
                    adb.setMessage("Do you want to send this information?[Yes/No]?");
                    adb.setNegativeButton("No", null);
                    adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            netwoekAvailableFast();
                            String SQL = "";
                            SQL = "Update " + TableName + " Set ";
                            SQL += "requestStatus ='1',";
                            SQL += "modifyDate = '" + Global.DateTimeNowYMDHMS() + "',";
                            // SQL+="approvalDate = '"+ Global.DateTimeNowYMDHMS()+"',";
                            //SQL+="approveQty = '"+o.get("RequestQty")+"',";
                            SQL += "upload ='2'";
                            SQL += "  Where requestId='" + o.get("RequestId") + "'";
                            C.Save(SQL);
                            //UpdateCurrentStockAdd(o.get("RequestBy"),Global.Left(o.get("ItemCode"),1),o.get("RequestQty"));
                            netwoekAvailable();
                            UpdateCurrentStockSubstract(o.get("RequestTo"), Global.Left(o.get("ItemCode"), 1), o.get("RequestQty"));
                            //  Connection.MessageBox(ReqAccepted.this, "Update Successfully");

                            DataSearch();

                        }
                    });
                    adb.show();
                }
            });
            return convertView;
        }


        private void UpdateCurrentStockAdd(String providertoId, String ItemCode, String Qty) {
            Integer q = Global.GetCurrentStockOfItem(C, g.getProvCode(), ItemCode);
            Integer currentStock = q + Integer.parseInt(Qty);
            String Sql = "Update currentStock Set stockQty= '" + String.valueOf(currentStock) + "', modifyDate = '" + Global.DateTimeNowYMDHMS() + "' WHERE providerId = '" + g.getProvCode() + "' AND itemCode = '" + ItemCode + "'";
            C.Save(Sql);
        }

        private void UpdateCurrentStockSubstract(String providerfromId, String ItemCode, String Qty) {
            Integer q = Global.GetCurrentStockOfItem(C, g.getProvCode(), ItemCode);
            Integer currentStock = q - Integer.parseInt(Qty);
            String Sql = "Update currentStock Set stockQty= '" + String.valueOf(currentStock) + "', modifyDate = '" + Global.DateTimeNowYMDHMS() + "' WHERE providerId = '" + g.getProvCode() + "' AND itemCode = '" + ItemCode + "'";
            C.Save(Sql);
        }


    }

    private boolean netwoekAvailableFast() {
        boolean netwoekAvailable = false;

        if (C.haveNetworkConnection(this)) {
            netwoekAvailable = true;


        } else {
            netwoekAvailable = false;
            Connection.MessageBox(ReqAccepted.this, "Internet connection is not available.");

        }
        return true;
    }

    private void netwoekAvailable() {
        boolean netwoekAvailable = false;

        if (C.haveNetworkConnection(this)) {
            netwoekAvailable = true;
            final ProgressDialog progDailog = ProgressDialog.show(
                    ReqAccepted.this, "", "অপেক্ষা করুন ...", true);
            new Thread() {
                public void run() {

                    ReqUpload();
                    //DataSearch();
                    progDailog.dismiss();

                }
            }.start();


        } else {
            netwoekAvailable = false;
            Connection.MessageBox(ReqAccepted.this, "Internet connection is not available.");
            // return true;
        }
    }

    private void ReqUpload() {
        //netwoekAvailable();

        String TableName = "";
        String VariableList = "";
        TableName = "itemRequest";
        VariableList = "requestId,requestBy,requestTo,itemCode,requestQty,expectedDate,requestRemarks,requestStatus,approveQty,createdBy,systemEntryDate,modifyDate,systemIp,upload";
        C.UploadJSON(TableName, VariableList, "requestId,requestBy,requestTo,itemCode");

    }

}