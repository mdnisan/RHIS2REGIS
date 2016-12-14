package com.data.rhis2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import Common.Connection;
import Common.Global;

/**
 * Created by Nisan on 12/26/2015.
 */
public class Requisition extends Activity {
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
        AlertDialog.Builder adb = new AlertDialog.Builder(Requisition.this);
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
    LinearLayout secS10;
    LinearLayout secSlNo;
    TextView VlblSlNo;
    EditText txtSlNo;
    LinearLayout secReq;
    TextView VlblReqName;
    TextView txtReqName;
    LinearLayout secReqToCode;
    TextView VlblReqToCode;
    EditText txtReqToCode;
    LinearLayout secItem;
    TextView VlblItem;
    Spinner spnItem;

    LinearLayout secItemUnit;
    TextView VlblItemUnit;
    //Spinner spnItemUnit;
    TextView txtItemUnit;

    LinearLayout secRequestQty;
    TextView VlblRequestQty;
    EditText txtRequestQty;

    LinearLayout secRemarks;
    TextView VlblRemarks;
    EditText txtRemarks;

    String StartTime;
    String DeviceNo;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.requisition);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();
            final ListView list = (ListView) findViewById(R.id.lstData);
            View header = getLayoutInflater().inflate(R.layout.requisitionheading, null);
            list.addHeaderView(header);

            TableName = "itemRequest";

            secS10 = (LinearLayout) findViewById(R.id.secS10);
            secSlNo = (LinearLayout) findViewById(R.id.secSlNo);
            VlblSlNo = (TextView) findViewById(R.id.VlblSlNo);
            txtSlNo = (EditText) findViewById(R.id.txtSlNo);
            txtSlNo.setText(ISlNo());
            txtSlNo.setEnabled(false);

            final Button cmdSave = (Button) findViewById(R.id.cmdSave);

            secReq = (LinearLayout) findViewById(R.id.secReq);
            VlblReqName = (TextView) findViewById(R.id.VlblReqName);

            txtReqName = (TextView) findViewById(R.id.txtReqName);
            txtReqName.setText(C.ReturnSingleValue("Select ProvName from ProviderDB where ProvCode='" + g.getProvCode() + "'"));

            secReqToCode = (LinearLayout) findViewById(R.id.secReqToCode);
            VlblReqToCode = (TextView) findViewById(R.id.VlblReqToCode);
            txtReqToCode = (EditText) findViewById(R.id.txtReqToCode);
            secItem = (LinearLayout) findViewById(R.id.secItem);
            VlblItem = (TextView) findViewById(R.id.VlblItem);
            spnItem = (Spinner) findViewById(R.id.spnItem);
            spnItem.setAdapter(C.getArrayAdapter("select ''||'-'||'' itemName from item union select itemCode||'-'||itemName itemName from item "));

            spnItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // spnItemUnit.setAdapter(C.getArrayAdapter("select unit itemName from item  where itemCode='"+ Global.Left(spnItem.getSelectedItem().toString(),1) +"'"));
                    //spnItemUnit.setAdapter(C.getArrayAdapter("select itemCode||'-'||unit itemName from item  where itemCode='"+ Global.Left(spnItem.getSelectedItem().toString(),1) +"'"));
                    // spnDCode.setSelection(DivUpazilaUnionSelect("DCode"));
                    txtItemUnit.setText(C.ReturnSingleValue("select unit itemName from item  where itemCode='" + Global.Left(spnItem.getSelectedItem().toString(), 1) + "'"));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });

            secItemUnit = (LinearLayout) findViewById(R.id.secItemUnit);
            VlblItemUnit = (TextView) findViewById(R.id.VlblItemUnit);
            txtItemUnit = (TextView) findViewById(R.id.txtItemUnit);

            secRequestQty = (LinearLayout) findViewById(R.id.secRequestQty);
            VlblRequestQty = (TextView) findViewById(R.id.VlblRequestQty);
            txtRequestQty = (EditText) findViewById(R.id.txtRequestQty);

            secRemarks = (LinearLayout) findViewById(R.id.secRemarks);
            VlblRemarks = (TextView) findViewById(R.id.VlblRemarks);
            txtRemarks = (EditText) findViewById(R.id.txtRemarks);

            DataSearch();
            cmdSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    DataSave();
                }
            });
        } catch (Exception e) {
            Connection.MessageBox(Requisition.this, e.getMessage());
            return;
        }
    }


    private void DataSave() {
        try {


            if (txtSlNo.getText().length() == 0 & secSlNo.isShown()) {
                Connection.MessageBox(Requisition.this, "Required field:Request Id.");
                txtSlNo.requestFocus();
                return;
            } else if (txtReqName.getText().length() == 0 & secReq.isShown()) {
                Connection.MessageBox(Requisition.this, "Required field:Request by Name.");
                txtReqName.requestFocus();
                return;
            }


            String SQL = "";

            if (!C.Existence("Select requestId,requestBy,requestTo,itemCode from " + TableName + "  Where requestId='" + txtSlNo.getText().toString() + "'")) {
                SQL = "Insert into " + TableName + "(requestId,requestBy,requestTo,itemCode,systemEntryDate,Upload)Values('" + txtSlNo.getText() + "','" + g.getProvCode() + "','" + txtReqToCode.getText() + "','" + Global.Left(spnItem.getSelectedItem().toString(), 1) + "','" + Global.DateTimeNowYMDHMS() + "','2')";
                C.Save(SQL);
            }

            SQL = "Update " + TableName + " Set ";
            SQL += "requestQty = '" + txtRequestQty.getText() + "',";
            //SQL+="transactionType ='3',";
            SQL += "requestStatus ='0',";
            SQL += "requestRemarks = '" + txtRemarks.getText() + "',";
            SQL += "Upload ='2'";
            SQL += "  Where requestId='" + txtSlNo.getText().toString() + "' and requestBy='" + g.getProvCode() + "' and requestTo='" + txtReqToCode.getText() + "' and itemCode='" + Global.Left(spnItem.getSelectedItem().toString(), 1) + "'";
            C.Save(SQL);
            Connection.MessageBox(Requisition.this, "Saved Successfully");
            DataSearch();
            txtSlNo.setText(ISlNo());
            ClearAll();
        } catch (Exception e) {
            Connection.MessageBox(Requisition.this, e.getMessage());
            return;
        }
    }

    private String ISlNo() {
        String SQL = "";

        SQL = "select ifnull(Count(*),'')+1 from itemRequest";
        // SQL += " where healthId='"+ g.getHealthID()+"' and providerId='"+ g.getProvCode() +"'";
        String SNo = C.ReturnSingleValue(SQL);
        // Serial No auto increment
        return SNo;
    }

    private void ClearAll() {
        txtReqToCode.setText("");
        spnItem.setSelection(0);
        txtItemUnit.setText("");
        txtRequestQty.setText("");
        txtRemarks.setText("");


    }


    private void DataSearch() {
        try {

            Cursor cur = C.ReadData("Select * from '" + TableName + "'");
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
                map.put("Upload", cur.getString(cur.getColumnIndex("upload")));


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

                // String a=cur.getString(cur.getColumnIndex("transactionType"));


                dataList.add(map);

                dataAdapter = new SimpleAdapter(Requisition.this, dataList, R.layout.requisitionrow, new String[]{"edit", "del"},
                        new int[]{R.id.cmdB1, R.id.cmdB2});

                list.setAdapter(new DataListAdapter(this));

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(Requisition.this, e.getMessage());
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
                convertView = inflater.inflate(R.layout.requisitionrow, null);
            }
            Button cmdB1 = (Button) convertView.findViewById(R.id.cmdB1);
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
            //RequestBy.setText();
            RequestTo.setText(o.get("RequestTo"));
            ItemCode.setText(o.get("ItemCode"));
            RequestQty.setText(o.get("RequestQty"));
            RequestStatus.setText(o.get("RequestStatus"));
            RequestRemarks.setText(o.get("RequestRemarks"));

     /*      if(String.valueOf(C.ReturnSingleValue("Select TransactionType from "+ TableName + " where TransactionType='"+o.get("TransactionType")+"'"))=="3")
           {
               cmdB2.setEnabled(false);
           }*/

            if (String.valueOf(o.get("Upload")).equals("1") && o.get("RequestStatus").equals("0")) {
                cmdB2.setEnabled(false);
                cmdB1.setEnabled(false);
                cmdB2.setText("Done");
                cmdB2.setBackgroundColor(Color.BLUE);

            }

            if (String.valueOf(o.get("Upload")).equals("1") && o.get("RequestStatus").equals("1")) {
                cmdB2.setEnabled(false);
                cmdB1.setEnabled(false);
                cmdB2.setText("Approved");
                cmdB2.setBackgroundColor(Color.BLUE);

            }

            final AlertDialog.Builder adb = new AlertDialog.Builder(Requisition.this);
            cmdB1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    adb.setTitle("Message:");
                    adb.setMessage("Do you want to update this information?[Yes/No]?");
                    adb.setNegativeButton("No", null);
                    adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Cursor cur = C.ReadData("Select * from " + TableName + "  Where requestId='" + o.get("RequestId") + "'");
                            cur.moveToFirst();
                            while (!cur.isAfterLast()) {
                                txtSlNo.setText(cur.getString(cur.getColumnIndex("requestId")));
                                txtReqName.setText(C.ReturnSingleValue("Select ProvName from ProviderDB where ProvCode='" + cur.getString(cur.getColumnIndex("requestBy")) + "'"));
                                txtReqToCode.setText(cur.getString(cur.getColumnIndex("requestTo")));
                                if (cur.getString(cur.getColumnIndex("itemCode")).equals("1")) {
                                    spnItem.setSelection(1);
                                } else if (cur.getString(cur.getColumnIndex("itemCode")).equals("2")) {
                                    spnItem.setSelection(2);
                                } else if (cur.getString(cur.getColumnIndex("itemCode")).equals("3")) {
                                    spnItem.setSelection(3);
                                } else if (cur.getString(cur.getColumnIndex("itemCode")).equals("9")) {
                                    spnItem.setSelection(4);
                                } else if (cur.getString(cur.getColumnIndex("itemCode")).equals("5")) {
                                    spnItem.setSelection(5);
                                } else if (cur.getString(cur.getColumnIndex("itemCode")).equals("8")) {
                                    spnItem.setSelection(6);
                                }

                                txtRequestQty.setText(cur.getString(cur.getColumnIndex("requestQty")));
                                txtRemarks.setText(cur.getString(cur.getColumnIndex("requestRemarks")));
                                cur.moveToNext();


                            }
                            cur.close();
                            DataSearch();
                        }
                    });
                    adb.show();
                }
            });
            cmdB2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    adb.setTitle("Message:");
                    adb.setMessage("Do you want to send this information?[Yes/No]?");
                    adb.setNegativeButton("No", null);

                    adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {

                            netwoekAvailable();
                            DataSearch();
                            //finish();


                        }
                    });

                    adb.show();
                }
            });
            return convertView;
        }
    }

    private void netwoekAvailable() {
        boolean netwoekAvailable = false;

        if (C.haveNetworkConnection(this)) {
            netwoekAvailable = true;
            final ProgressDialog progDailog = ProgressDialog.show(
                    Requisition.this, "", "অপেক্ষা করুন ...", true);
            new Thread() {
                public void run() {

                    ReqUpload();
                    //DataSearch();
                    progDailog.dismiss();

                }
            }.start();


        } else {
            netwoekAvailable = false;
            Connection.MessageBox(Requisition.this, "Internet connection is not available.");
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