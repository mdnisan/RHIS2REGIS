package com.data.rhis2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Common.Connection;
import Common.Global;

/**
 * Created by user on 18/03/2015.
 */
public class StockAdjustment extends Activity {
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
        AlertDialog.Builder adb = new AlertDialog.Builder(StockAdjustment.this);
        switch (item.getItemId()) {
            case R.id.menuClose:
                adb.setTitle("Close");
                adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান[হ্যাঁ/না]?");
                adb.setNegativeButton("না", null);
                adb.setPositiveButton("হ্যাঁ", new AlertDialog.OnClickListener() {
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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.stockadjustment);
            C = new Connection(this);
            g = Global.getInstance();


            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            fillUnitAndQty();
            ((Button) findViewById(R.id.cmdSave)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    save();
                    fillUnitAndQty();
                    Toast.makeText(v.getContext(), "মজুদ সমন্বয় হয়েছে ", Toast.LENGTH_LONG).show();

                    //  AlertDialog.Builder adb = new AlertDialog.Builder(StockAdjustment.this);
                    //{
                        /*String reason1 = ((Spinner)findViewById(R.id.row1reason)).getSelectedItemPosition()==0?"":Global.Left(((Spinner)findViewById(R.id.row1reason)).getSelectedItem().toString(),1);
                        String reason2 = ((Spinner)findViewById(R.id.row2reason)).getSelectedItemPosition()==0?"":Global.Left(((Spinner)findViewById(R.id.row2reason)).getSelectedItem().toString(),1);
                        String reason3 = ((Spinner)findViewById(R.id.row3reason)).getSelectedItemPosition()==0?"":Global.Left(((Spinner)findViewById(R.id.row3reason)).getSelectedItem().toString(),1);
                        String reason4 = ((Spinner)findViewById(R.id.row4reason)).getSelectedItemPosition()==0?"":Global.Left(((Spinner)findViewById(R.id.row4reason)).getSelectedItem().toString(),1);
                        String reason5 = ((Spinner)findViewById(R.id.row5reason)).getSelectedItemPosition()==0?"":Global.Left(((Spinner)findViewById(R.id.row5reason)).getSelectedItem().toString(),1);
                        String reason6 = ((Spinner)findViewById(R.id.row6reason)).getSelectedItemPosition()==0?"":Global.Left(((Spinner)findViewById(R.id.row6reason)).getSelectedItem().toString(),1);
                        String msg="";

                        if(reason1.length()>0) {
                            String  AdjustQty = ((EditText) findViewById(R.id.row1col4)).getText().toString();
                            if (AdjustQty.length() > 0) {

                               msg  = "item name :" + "1. Condom" + "Karon :" + reason1 +" Qty = "+ AdjustQty;
                            }
                        }

                        if(reason2.length()>0) {
                            String  AdjustQty = ((EditText) findViewById(R.id.row2col4)).getText().toString();
                            if (AdjustQty.length() > 0) {

                                msg  = "item name :" + "1. Condom" + "Karon :" + reason2 +" Qty = "+ AdjustQty;
                            }
                        }*/

                       /* adb.setMessage("আপনি কি  মজুদ সমন্বয় করতে  চান?");

                        adb.setNegativeButton("না", new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        adb.setPositiveButton("হাঁ", new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // adb.show();

                                                 }
                        });*/

                    //  }
                }
            });

        } catch (Exception e) {
            Connection.MessageBox(StockAdjustment.this, e.getMessage());
            return;
        }
    }

    private void fillReasons() {
        List<String> listReasons = new ArrayList<String>();
        listReasons.add("");
        listReasons.add("1-নষ্ট হয়ে গেছে");
        listReasons.add("2-হারিয়ে গেছে");
        listReasons.add("3-গুদামে ফেরত দেয়া হয়েছে");
        listReasons.add("4-মেয়াদোত্তীর্ণিন হয়ে গেছে");


        ArrayAdapter<String> ReasonsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listReasons);
        ((Spinner) findViewById(R.id.row1reason)).setAdapter(ReasonsAdapter);
        ((Spinner) findViewById(R.id.row2reason)).setAdapter(ReasonsAdapter);
        ((Spinner) findViewById(R.id.row3reason)).setAdapter(ReasonsAdapter);
        ((Spinner) findViewById(R.id.row4reason)).setAdapter(ReasonsAdapter);
        ((Spinner) findViewById(R.id.row5reason)).setAdapter(ReasonsAdapter);
        ((Spinner) findViewById(R.id.row6reason)).setAdapter(ReasonsAdapter);
        //row1reason
    }

    private void fillUnitAndQty() {
        getCurrentStock((TextView) findViewById(R.id.row1unit), (TextView) findViewById(R.id.row1qty), "1");
        getCurrentStock((TextView) findViewById(R.id.row2unit), (TextView) findViewById(R.id.row2qty), "2");
        getCurrentStock((TextView) findViewById(R.id.row3unit), (TextView) findViewById(R.id.row3qty), "3");
        getCurrentStock((TextView) findViewById(R.id.row4unit), (TextView) findViewById(R.id.row4qty), "9");
        getCurrentStock((TextView) findViewById(R.id.row5unit), (TextView) findViewById(R.id.row5qty), "5");
        getCurrentStock((TextView) findViewById(R.id.row6unit), (TextView) findViewById(R.id.row6qty), "8");
        fillReasons();
    }

    private void getCurrentStock(TextView tvunit, TextView tvqty, String itemCode) {
        try {

            Cursor cur = C.ReadData("select I.itemCode as ItemCode,I.itemName as ItemName,C.stockQty as StockQty,I.unit as Unit from currentStock C,item I where C.itemCode=I.itemCode and C.providerId='" + g.getProvCode() + "' AND I.itemCode = '" + itemCode + "'");
            cur.moveToFirst();

            while (!cur.isAfterLast()) {
                tvunit.setText(cur.getString(cur.getColumnIndex("Unit")));
                tvqty.setText(cur.getString(cur.getColumnIndex("StockQty")));


                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(StockAdjustment.this, e.getMessage());
            return;
        }
    }

    private void save() {
        String reason1 = ((Spinner) findViewById(R.id.row1reason)).getSelectedItemPosition() == 0 ? "" : Global.Left(((Spinner) findViewById(R.id.row1reason)).getSelectedItem().toString(), 1);
        String reason2 = ((Spinner) findViewById(R.id.row2reason)).getSelectedItemPosition() == 0 ? "" : Global.Left(((Spinner) findViewById(R.id.row2reason)).getSelectedItem().toString(), 1);
        String reason3 = ((Spinner) findViewById(R.id.row3reason)).getSelectedItemPosition() == 0 ? "" : Global.Left(((Spinner) findViewById(R.id.row3reason)).getSelectedItem().toString(), 1);
        String reason4 = ((Spinner) findViewById(R.id.row4reason)).getSelectedItemPosition() == 0 ? "" : Global.Left(((Spinner) findViewById(R.id.row4reason)).getSelectedItem().toString(), 1);
        String reason5 = ((Spinner) findViewById(R.id.row5reason)).getSelectedItemPosition() == 0 ? "" : Global.Left(((Spinner) findViewById(R.id.row5reason)).getSelectedItem().toString(), 1);
        String reason6 = ((Spinner) findViewById(R.id.row6reason)).getSelectedItemPosition() == 0 ? "" : Global.Left(((Spinner) findViewById(R.id.row6reason)).getSelectedItem().toString(), 1);

        InsertItemToitemAdjustmentMinus("1", reason1, ((TextView) findViewById(R.id.row1qty)).getText().toString(), ((EditText) findViewById(R.id.row1col4)).getText().toString());
        InsertItemToitemAdjustmentMinus("2", reason2, ((TextView) findViewById(R.id.row2qty)).getText().toString(), ((EditText) findViewById(R.id.row2col4)).getText().toString());
        InsertItemToitemAdjustmentMinus("3", reason3, ((TextView) findViewById(R.id.row3qty)).getText().toString(), ((EditText) findViewById(R.id.row3col4)).getText().toString());
        InsertItemToitemAdjustmentMinus("9", reason4, ((TextView) findViewById(R.id.row4qty)).getText().toString(), ((EditText) findViewById(R.id.row4col4)).getText().toString());
        InsertItemToitemAdjustmentMinus("5", reason5, ((TextView) findViewById(R.id.row5qty)).getText().toString(), ((EditText) findViewById(R.id.row5col4)).getText().toString());
        InsertItemToitemAdjustmentMinus("8", reason6, ((TextView) findViewById(R.id.row6qty)).getText().toString(), ((EditText) findViewById(R.id.row6col4)).getText().toString());

    }

    private void InsertItemToitemAdjustmentMinus(String ItemCode, String Reason, String CurrentQty, String AdjustQty) {

        if (Reason.length() > 0) {

            if (AdjustQty.length() > 0) {

                if (Integer.parseInt(AdjustQty) < Integer.parseInt(CurrentQty)) {
                    String sql = "INSERT INTO itemAdjustmentMinus (adjustmentId, providerId, itemCode, adjustmentType, adjustmentQty, adjustmentDate, adjustmentRemarks, createdBy, createdDate, upload) VALUES('" +
                            "" + serviceID() + "', '" + g.getProvCode() + "','" + ItemCode + "', '" + Reason + "','" + AdjustQty + "', '" + Global.DateNowYMD() + "', '','" + g.getProvCode() + "','" + Global.DateNowYMD() + "','2')";

                    C.Save(sql);
                    UpdateCurrentStock(g.getProvCode(), ItemCode, AdjustQty);
                    if (ItemCode.equalsIgnoreCase("1")) {
                        ClearEntry((EditText) findViewById(R.id.row1col4));
                    } else if (ItemCode.equalsIgnoreCase("2")) {
                        ClearEntry((EditText) findViewById(R.id.row2col4));
                    } else if (ItemCode.equalsIgnoreCase("3")) {
                        ClearEntry((EditText) findViewById(R.id.row3col4));
                    } else if (ItemCode.equalsIgnoreCase("9")) {
                        ClearEntry((EditText) findViewById(R.id.row4col4));
                    } else if (ItemCode.equalsIgnoreCase("5")) {
                        ClearEntry((EditText) findViewById(R.id.row5col4));
                    } else if (ItemCode.equalsIgnoreCase("8")) {
                        ClearEntry((EditText) findViewById(R.id.row6col4));
                    }
                } else {
                    Connection.MessageBox(StockAdjustment.this, "মজুদের পরিমান কম আছে");
                    return;
                }

            } else {
                Connection.MessageBox(StockAdjustment.this, "সংখ্যা লিখুন");
                return;
            }
        }
    /*else
    {
        Connection.MessageBox(StockAdjustment.this, "কারন লিখুন");
        return;
    }*/
    }

    private void ClearEntry(EditText txt) {
        txt.setText("");
    }

    private void UpdateCurrentStock(String providerId, String ItemCode, String Qty) {
        Integer q = Global.GetCurrentStockOfItem(C, g.getProvCode(), ItemCode);
        Integer currentStock = q - Integer.parseInt(Qty);
        String Sql = "Update currentStock Set stockQty= '" + String.valueOf(currentStock) + "', modifyDate = '" + Global.DateTimeNowYMDHMS() + "' WHERE providerId = '" + g.getProvCode() + "' AND itemCode = '" + ItemCode + "'";
        C.Save(Sql);
    }

    private String serviceID() {
        String SQL = "";
        String PGNNo = "";

        SQL = "select '0'||(ifnull(max(cast(adjustmentId as int)),0))MaxId from itemAdjustmentMinus";

        String tempserviceID = C.ReturnSingleValue(SQL);


        String serviceID = String.valueOf((Integer.parseInt(tempserviceID) + 1));

        return String.valueOf(serviceID);

    }
}





