package com.data.rhis2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
 * Created by ccah on 11/25/2015.
 */
public class PregRegView extends Activity {
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
    LinearLayout secDownload;
    EditText dtpFromDT;
    ImageButton btnFromDT;
    EditText dtpToDT;
    ImageButton btnToDT;

    SimpleAdapter dataAdapter;
    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    private String TableNameELCOVisit;
    TextView VlblProviderName;

    RadioButton rdoS77d1;
    RadioButton rdoS77d2;

    String StartTime;
    String TDeath;
    ListView list;
    Spinner spnProvider;
    Spinner txtVillSearch1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.pregview);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();
            secDeathDT = (LinearLayout) findViewById(R.id.secDeathDT);
            secDownload= (LinearLayout) findViewById(R.id.secDownload);
            spnProvider = (Spinner) findViewById(R.id.spnProvider);

            txtVillSearch1 = (Spinner) findViewById(R.id.txtVillSearch1);
            VlblProviderName= (TextView) findViewById(R.id.VlblProviderName);
            String Type = "";
            if (g.getProvType().equals("10")) {
                Type = "3";
                VlblProviderName.setText("পঃ কঃ সহকারীর নাম");
            } else if (g.getProvType().equals("11")) {
                Type = "2";
                VlblProviderName.setText("স্বাস্থ্য সহকারীর  নাম");
            } else if (g.getProvType().equals("12")) {
                Type = "2";
                VlblProviderName.setText("স্বাস্থ্য সহকারীর  নাম");
            }

            //spnProvider.setEnabled(false);

            ((Spinner) findViewById(R.id.spnProvider)).setAdapter(C.getArrayAdapter("Select substr('0' ||ProvCode, -6, 6)||'-'||provName from ProviderDb WHERE provType='" + Type + "' AND provCode ='" + g.getFWAProvCode() + "'"));
            ((Spinner) findViewById(R.id.spnProvider)).setEnabled(false);

            txtVillSearch1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                   // String val = txtVillSearch1.getSelectedItemPosition() == 0 ? "" : Global.Left(txtVillSearch1.getSelectedItem().toString(), 5);
                    //String val1 = txtVillSearch1.getSelectedItemPosition() == 0 ? "" : Global.Left(txtVillSearch1.getSelectedItem().toString(), 3);

                    String val1 = (((Spinner) findViewById(R.id.txtVillSearch1)).getSelectedItemPosition() == 0 ? "" : Global.Left(((Spinner) findViewById(R.id.txtVillSearch1)).getSelectedItem().toString(), 5));

                   // if (!val.equalsIgnoreCase("All") & !val.equalsIgnoreCase("")) {

                       // if(val.equals("")||val1.equalsIgnoreCase("All")) {

                    if( val1.equalsIgnoreCase(""))
                    {
                        secDownload.setVisibility(View.GONE);
                        secDeathDT.setVisibility(View.GONE);
                    }

                  else  if(val1.equalsIgnoreCase(" All "))
                    {
                        secDownload.setVisibility(View.GONE);
                        secDeathDT.setVisibility(View.VISIBLE);
                    }

                    else
                    {

                        secDownload.setVisibility(View.VISIBLE);
                        secDeathDT.setVisibility(View.VISIBLE);
                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            //Select '-' provName from ProviderDb Union
            dtpFromDT = (EditText) findViewById(R.id.dtpFromDT);
            //dtpFromDT.setText(Global.DateNowDMY());
            btnFromDT = (ImageButton) findViewById(R.id.btnFromDT);

            dtpToDT = (EditText) findViewById(R.id.dtpToDT);
            //dtpToDT.setText(Global.DateNowDMY());
            btnToDT = (ImageButton) findViewById(R.id.btnToDT);
            list = (ListView) findViewById(R.id.lstData);
            secDownload.setVisibility(View.GONE);
            secDeathDT.setVisibility(View.GONE);
            /*View header = getLayoutInflater().inflate(R.layout.pregviewheading, null);
            list.addHeaderView(header);*/
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
                   /* if (spnProvider.getSelectedItemPosition() == 0) {
                        ((Spinner) findViewById(R.id.txtVillSearch1)).setAdapter(C.getArrayAdapter("Select '  ' VillageName from Village union Select ' All ' VillageName from Village"));

                    } else */if (spnProvider.getSelectedItemPosition() >= 0) {
                        String Type = "";
                        if (g.getProvType().equals("10")) {
                            Type = "3";
                        } else if (g.getProvType().equals("11")) {
                            Type = "2";
                        } else if (g.getProvType().equals("12")) {
                            Type = "2";
                        }

                        // union Select ' All ' VillageName from Village

                        ((Spinner) findViewById(R.id.txtVillSearch1)).setAdapter(C.getArrayAdapter("Select '  ' VillageName from Village union select  substr('0' || v.MOUZAID , -3, 3)||substr('0' || v.VILLAGEID, -3, 3)||'-'||v.VILLAGENAMEENG as VILLAGENAME from Village  v INNER JOIN ProviderArea PA ON PA.zillaid = v.zillaid " +
                                "AND PA.upazilaid = v.UPAZILAID " +
                                "AND PA.unionid = v.UNIONID " +
                                "AND PA.mouzaid = v.MOUZAID " +
                                "AND PA.villageid = v.VILLAGEID " +
                                "AND Cast(PA.ProvCode AS INT) = '" + Global.Left(spnProvider.getSelectedItem().toString(),6) +//"'"));
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
            DisplaySearch(true);
            // lblTCount.setText( GetTotalDeath(dtpFromDT.getText().toString(),dtpToDT.getText().toString()));
            Button cmdRefresh = (Button) findViewById(R.id.cmdRefresh);

            Button cmdDownload = (Button) findViewById(R.id.cmdDownload);

            cmdDownload.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    /*if (((Spinner) findViewById(R.id.spnProvider)).getSelectedItemPosition() == 0) {
                        Connection.MessageBox(PregRegView.this, "Provider সিলেক্ট করুন।");
                        // dtpDeathDT.requestFocus();
                        return;
                    }*/
                    if (((Spinner) findViewById(R.id.txtVillSearch1)).getSelectedItemPosition() == 0) {
                        Connection.MessageBox(PregRegView.this, "গ্রাম সিলেক্ট করুন।");
                        // dtpDeathDT.requestFocus();
                        return;
                    }
                    boolean netwoekAvailable = false;
                    if (Connection.haveNetworkConnection(PregRegView.this)) {
                        netwoekAvailable = true;
                        //  UploadFWA();
                        final ProgressDialog progDailog = ProgressDialog.show(
                                PregRegView.this, "", "অপেক্ষা করুন ...", true);
                        new Thread() {
                            public void run() {
                                String vill = (((Spinner) findViewById(R.id.txtVillSearch1)).getSelectedItemPosition() == 0 ? "" : Global.Left(((Spinner) findViewById(R.id.txtVillSearch1)).getSelectedItem().toString(), 5));
                                //String mouzaid = vill.toString().substring(0, 3);
                                if(vill.equals(""))
                                {

                                }
                                else {
                                    String mouzaid = Global.Left(vill, 3);
                                    String villageCode = vill.toString().substring(3, 5);
                                    Integer zillaid = Integer.valueOf(C.ReturnSingleValue("Select zillaid from ProviderDB where ProvCode='" + Global.Left(spnProvider.getSelectedItem().toString(), 6) + "'"));
                                    Integer upazilaid = Integer.valueOf(C.ReturnSingleValue("Select upazilaid from ProviderDB where ProvCode='" + Global.Left(spnProvider.getSelectedItem().toString(), 6) + "'"));
                                    Integer unionid = Integer.valueOf(C.ReturnSingleValue("Select unionid from ProviderDB where ProvCode='" + Global.Left(spnProvider.getSelectedItem().toString(), 6) + "'"));
                                    //Integer UN = Integer.valueOf(C.ReturnSingleValue("Select UNIONID from Unions where" + " ZILLAID like '%" + g.getDistrict() + "%' AND  UPAZILAID like '%" + g.getUpazila() + "%'"));
                                    DownloadMCHTablesAll(mouzaid, villageCode, String.valueOf(zillaid), String.valueOf(upazilaid), String.valueOf(unionid));
                                    // DownloadMCHTables(Global.Left(spnProvider.getSelectedItem().toString(), 5));
                                }
                                progDailog.dismiss();


                                        //stuff that updates ui


                            }
                        }.start();




                    }

                    else {
                        netwoekAvailable = false;
                         Connection.MessageBox(PregRegView.this, "Internet connection is not available.");
                          //return true;

                    }

                }
            });

          //  Button cmdRefresh = (Button) findViewById(R.id.cmdRefresh);
            cmdRefresh.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                 /*   if (((Spinner) findViewById(R.id.spnProvider)).getSelectedItemPosition() == 0) {
                        Connection.MessageBox(PregRegView.this, "Provider সিলেক্ট করুন।");
                        // dtpDeathDT.requestFocus();
                        return;
                    }*/
                    if (((Spinner) findViewById(R.id.txtVillSearch1)).getSelectedItemPosition() == 0) {
                        Connection.MessageBox(PregRegView.this, "গ্রাম সিলেক্ট করুন।");
                        // dtpDeathDT.requestFocus();
                        return;
                    }
                    totalCount = 0;
                    list.setAdapter(null);
                    String val = (((Spinner) findViewById(R.id.txtVillSearch1)).getSelectedItemPosition() == 0 ? "" : Global.Left(((Spinner) findViewById(R.id.txtVillSearch1)).getSelectedItem().toString(), 3));
                    String villageCode = "";
                   // if (!val.equalsIgnoreCase("All") & !val.equalsIgnoreCase(""))
                   // !val.equalsIgnoreCase(" Al") &
                    if (!val.equalsIgnoreCase("")) {
                       String vill = (((Spinner) findViewById(R.id.txtVillSearch1)).getSelectedItemPosition() == 0 ? "" : Global.Left(((Spinner) findViewById(R.id.txtVillSearch1)).getSelectedItem().toString(), 6));
                        villageCode = vill.toString().substring(3, 5);
                        if (val.length() > 0) {
                            totalCount = 0;
                            dataList.clear();
                            list.setAdapter(null);
                            SearchVillageWise(val, villageCode);
                            lblTCount.setText(":" + String.valueOf(totalCount) + "");

                        }
                    }
                    else if (val.equalsIgnoreCase(" Al")||val.equalsIgnoreCase(""))
                    {
                        // SearchVillageWise("", "");
                    }

                  /*  if (val.length() == 0) {
                        totalCount = 0;
                        dataList.clear();
                        list.setAdapter(null);
                        dtpFromDT.setText("");
                        dtpToDT.setText("");
                        lblTCount.setText(":" + String.valueOf(totalCount) + "");

                    }*/
                    //lblTCount.setText(":" + String.valueOf(totalCount));
                }
            });
           /* cmdRefresh.setOnClickListener(new View.OnClickListener() {
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
            });*/


        } catch (Exception e) {
            Connection.MessageBox(PregRegView.this, e.getMessage());
            return;
        }
    }


    public void DownloadMCHTablesAll(String mouzaid,String villageid,String zillaid,String upazilaid,String unionid) {

        String VariableList = "";
        // /pregWomen
        //  \"providerId\" =" + provCode + "";


        String sql = "select distinct B.\"generatedId\", B.\"name\",  B.\"age\", B.\"divisionId\", B.\"zillaId\", B.\"upazilaId\",  B.\"unionId\", B.\"mouzaId\",B.\"villageId\", B.\"houseGRHoldingNo\", B.\"mobileNo\",  B.\"systemEntryDate\", B.\"modifyDate\", B.\"providerId\",  B.\"healthId\",  B.\"gender\", B.\"fatherName\", B.\"motherName\", B.\"husbandName\", B.\"dob\", B.\"ownMobile\", B.\"epiBlock\",   '1' Upload \n" +
                "from \"ProviderArea\" pa \n" +
                "INNER JOIN \"Member\" A ON A.\"Dist\" = pa.zillaid and A.\"Upz\" = pa.upazilaid and A.\"UN\" = pa.unionid\n" +
                "and A.\"Mouza\" = pa.mouzaid and A.\"Vill\" = pa.villageid \n" +
                "INNER JOIN \"clientMap\" B ON A.\"HealthID\" = B.\"healthId\"\n" +
                "INNER JOIN \"pregWomen\" C ON C.\"healthId\" = B.\"generatedId\"\n" +
                "where pa.\"mouzaid\" ='" + mouzaid  + "'\n" +
                "\tand pa.\"villageid\" = '" + villageid + "'\n" +
                "\tand pa.\"zillaid\" = '" + zillaid + "'\n" +
                "\tand pa.\"upazilaid\" = '" + upazilaid + "'\n" +
                "\tand pa.\"unionid\" = '" + unionid+ "'";
               /* "where pa.\"provCode\" =" + provCode +
                "\tand pa.\"mouzaid\" = '" + mouzaid + "'\n" +
                "\tand pa.\"villageid\" = '" + villageid + "'\n" +
                "\tand pa.\"zillaid\" = '" + zillaid + "'\n" +
                "\tand pa.\"upazilaid\" = '" + upazilaid + "'\n" +
                "\tand pa.\"unionid\" = '" + unionid + "' and A.\"HHNo\" IN(" + HHinClause + ")";*/


        C.DownloadJSON(sql, "clientMap", "generatedId, name,  age, divisionId, zillaId, upazilaId,  unionId, mouzaId,villageId, houseGRHoldingNo, mobileNo,  systemEntryDate, modifyDate, providerId,  healthId,  gender, fatherName, motherName, husbandName, dob, ownMobile, epiBlock,  upload", "generatedId");


        //Household data
/*        String SQLStr = "Select h.\"Dist\", h.\"Upz\", h.\"UN\", h.\"Mouza\", h.\"Vill\", h.\"PAddr\", h.\"PermaAddress\", h.\"ProvType\", h.\"ProvCode\", h.\"HHNo\", h.\"Religion\", h.\"VGFCard\",";
        SQLStr += " h.\"subBlock\",h.\"unit\", h.\"StartTime\", h.\"EndTime\", h.\"Lat\", h.\"Lon\", h.\"UserId\", h.\"EnDt\", '1' Upload";
        SQLStr += " from \"Village\" v";
        SQLStr += " inner join \"ProviderArea\" a on v.\"ZILLAID\"=a.zillaid and v.\"UPAZILAID\"=a.upazilaid and v.\"UNIONID\"=a.unionid and v.\"MOUZAID\"=a.mouzaid and v.\"VILLAGEID\"=a.villageid";
        SQLStr += " inner join \"Household\" h on a.zillaid=h.\"Dist\" and a.upazilaid=h.\"Upz\" and a.unionid=h.\"UN\" and a.mouzaid=h.\"Mouza\" and a.villageid=h.\"Vill\""+
       // "INNER JOIN \"clientMap\" B ON h.\"HealthID\" = B.\"healthId\"\n" +
                "where a.\"mouzaid\" ='" + mouzaid  + "'\n" +
                "\tand a.\"villageid\" = '" + villageid + "'\n" +
                "\tand a.\"zillaid\" = '" + zillaid + "'\n" +
                "\tand a.\"upazilaid\" = '" + upazilaid + "'\n" +
                "\tand a.\"unionid\" = '" + unionid+ "'"+*/

               // "\tand h.\"HHNo\"  = B.\"houseGRHoldingNo\"\n";

        /* SQLStr += " and v.\"MOUZAID\" = '" + Mouza + "'\n" +
                "\tand v.\"VILLAGEID\" = '" + village + "'\n" +
                "\tand a.\"provType\" = '" + ProvType + "'\n" +
                "\tand a.\"provCode\" = '" + ProvCode + "'\n" +
                "\tand v.\"ZILLAID\" = '" + Dist + "'\n" +
                "\tand v.\"UPAZILAID\" = '" + Upz + "'\n" +
                "\tand v.\"UNIONID\" = '" + Union + "'";
        SQLStr += " where v.\"MOUZAID\" = '" + Mouza + "'\n" +
                "\tand v.\"VILLAGEID\" = '" + village + "' and a.\"provType\"='" + ProvType + "' and a.\"provCode\"='" +
                ProvCode + "' and v.\"ZILLAID\"='" +
                Dist + "' and v.\"UPAZILAID\"='" + Upz + "' and v.\"UNIONID\"='" + Union + "' and h.\"HHNo\" IN(" + incaluse.substring(0, incaluse.length() - 1) + ")";

      */
       // C.DownloadJSON(SQLStr, "Household", "Dist, Upz, UN, Mouza, Vill, PAddr, PermaAddress, ProvType, ProvCode, HHNo, Religion, VGFCard,subBlock,unit, StartTime, EndTime, Lat, Lon, UserId, EnDt, Upload", "Dist, Upz, UN, Mouza, Vill, HHNo");


        //Member Data
        sql = " Select distinct h.\"Dist\", h.\"Upz\", h.\"UN\", h.\"Mouza\", h.\"Vill\", h.\"ProvType\", h.\"ProvCode\", h.\"HHNo\", h.\"SNo\", h.\"HealthID\", h.\"NameEng\", h.\"NameBang\", h.\"Rth\", h.\"HaveNID\", h.\"NID\", h.\"NIDStatus\", h.\"HaveBR\",";
        sql += " h.\"BRID\", h.\"BRIDStatus\", h.\"MobileNo1\", h.\"MobileNo2\",h.mobileyn, h.\"DOB\", h.\"Age\", h.\"DOBSource\", h.\"BPlace\", h.\"FNo\", h.\"Father\", h.\"FDontKnow\", h.\"MNo\", h.\"Mother\", h.\"MDontKnow\", h.\"Sex\", h.\"MS\", h.\"SPNO1\",";
        sql += " h.\"SPNO2\", h.\"SPNO3\", h.\"SPNO4\", h.\"ELCONo\", h.\"ELCODontKnow\", h.\"EDU\", h.\"Rel\", h.\"Nationality\", h.\"OCP\", h.\"StartTime\", h.\"EnType\", h.\"EnDate\", coalesce(h.\"ExType\", '')  AS \"ExType\", h.\"ExDate\", h.\"EndTime\", h.\"Lat\", h.\"Lon\", h.\"UserId\", h.\"EnDt\" , h.\"hidDistributed\"\n" +
                "\t,h.\"hidDistributionDate\", '1' Upload";
        sql += " from \"Village\" v";
        sql += " inner join \"ProviderArea\" a on v.\"ZILLAID\"=a.zillaid and v.\"UPAZILAID\"=a.upazilaid and v.\"UNIONID\"=a.unionid and v.\"MOUZAID\"=a.mouzaid and v.\"VILLAGEID\"=a.villageid";
        sql += " inner join \"Member\" h on a.zillaid=h.\"Dist\" and a.upazilaid=h.\"Upz\" and a.unionid=h.\"UN\" and a.mouzaid=h.\"Mouza\" and a.villageid=h.\"Vill\""+
                "INNER JOIN \"clientMap\" B ON h.\"HealthID\" = B.\"healthId\""+
                "INNER JOIN \"pregWomen\" C ON C.\"healthId\" = B.\"generatedId\""+
        "where a.\"mouzaid\" ='" + mouzaid  + "'\n" +
                "\tand a.\"villageid\" = '" + villageid + "'\n" +
                "\tand a.\"zillaid\" = '" + zillaid + "'\n" +
                "\tand a.\"upazilaid\" = '" + upazilaid + "'\n" +
                "\tand a.\"unionid\" = '" + unionid+ "'"+
                 "\tand h.\"HealthID\" in(Select \"HealthID\" from \"clientMap\" where \"HealthID\" = B.\"healthId\")";
        //SQLStr +=" and h.HHNo IN('\" + incaluse.substring(0,incaluse.length()-1)+ \"')";
        // SQLStr += " where a.\"provType\"='" + ProvType + "' and a.\"provCode\"='" + ProvCode + "' and v.\"ZILLAID\"='" + Dist + "' and v.\"UPAZILAID\"='" + Upz + "' and v.\"UNIONID\"='" + Union + "'";
     /*   sql += " where v.\"MOUZAID\" = '" + Mouza + "'\n" +
                "\tand v.\"VILLAGEID\" = '" + village + "'\n" +
                "\tand a.\"provType\"= '" + ProvType + "'\n" +
                "\tand a.\"provCode\" = '" + ProvCode + "'\n" +
                "\tand v.\"ZILLAID\" = '" + Dist + "'\n" +
                "\tand v.\"UPAZILAID\" = '" + Upz + "'\n" +

                "\tand v.\"UNIONID\" = '" + Union + "' and h.\"HHNo\" IN(" + incaluse.substring(0, incaluse.length() - 1) + ")";

*/
        C.DownloadJSON(sql, "Member", "Dist, Upz, UN, Mouza, Vill, ProvType, ProvCode, HHNo, SNo, HealthID, NameEng, NameBang, Rth, HaveNID, NID, NIDStatus, HaveBR, BRID, BRIDStatus, MobileNo1, MobileNo2,MobileYN, DOB, Age, DOBSource, BPlace, FNo, Father, FDontKnow, MNo, Mother, MDontKnow, Sex, MS, SPNO1, SPNO2, SPNO3, SPNO4, ELCONo, ELCODontKnow, EDU, Rel, Nationality, OCP, StartTime, EnType, EnDate, ExType, ExDate, EndTime, Lat, Lon, UserId, EnDt, hidDistributed, hidDistributionDate, Upload", "Dist, Upz, UN, Mouza, Vill, HHNo, SNo");

        sql = "select distinct C.\"healthId\", C.\"providerId\", C.\"hhStatus\", C.\"haHHNo\", C.\"elcoNo\", C.\"husbandName\", C.\"husbandAge\", C.\"domSource\", C.\"marrDate\", C.\"marrAge\", C.\"son\", C.\"dau\", C.\"regDT\", C.\"systemEntryDate\", C.\"modifyDate\", '1' upload \n" +
                "from \"ProviderArea\" pa \n" +
                "INNER JOIN \"Member\" A ON A.\"Dist\" = pa.zillaid and A.\"Upz\" = pa.upazilaid and A.\"UN\" = pa.unionid\n" +
                "and A.\"Mouza\" = pa.mouzaid and A.\"Vill\" = pa.villageid \n" +
                "INNER JOIN \"clientMap\" B ON A.\"HealthID\" = B.\"healthId\"\n" +
                "INNER JOIN \"elco\" C ON C.\"healthId\" = B.\"generatedId\"\n" +
                "INNER JOIN \"pregWomen\" D ON D.\"healthId\" = B.\"generatedId\"\n" +
                "where pa.\"mouzaid\" ='" + mouzaid  + "'\n" +
                "\tand pa.\"villageid\" = '" + villageid + "'\n" +
                "\tand pa.\"zillaid\" = '" + zillaid + "'\n" +
                "\tand pa.\"upazilaid\" = '" + upazilaid + "'\n" +
                "\tand pa.\"unionid\" = '" + unionid+ "'";
               /* "where pa.\"provCode\" =" + provCode +
                "\tand pa.\"mouzaid\" = '" + mouzaid + "'\n" +
                "\tand pa.\"villageid\" = '" + villageid + "'\n" +
                "\tand pa.\"zillaid\" = '" + zillaid + "'\n" +
                "\tand pa.\"upazilaid\" = '" + upazilaid + "'\n" +
                "\tand pa.\"unionid\" = '" + unionid + "' and A.\"HHNo\" IN(" + HHinClause + ")";*/

        VariableList = "healthId, providerId, hhStatus, haHHNo, elcoNo, husbandName, husbandAge, domSource, marrDate, marrAge, son, dau, regDT, systemEntryDate, modifyDate, upload";
        C.DownloadJSON(sql, "elco", VariableList, "healthId");


        //elco visit
        sql = "select distinct C.\"healthId\",C.\"pregNo\",C.\"providerId\",C.\"transactionId\",C.\"visit\",C.\"vDate\",C.\"visitStatus\",C.\"currStatus\",C.\"newOld\",C.\"mDate\",C.\"sSource\",C.\"qty\",C.\"unit\",C.\"brand\",C.\"validity\",C.\"dayMonYear\",C.\"referPlace\",C.\"syrinsQty\",C.\"mrSource\",C.\"MRDate\",C.\"MRAge\",C.\"systemEntryDate\",C.\"modifyDate\", '1' upload \n" +
                "from \"ProviderArea\" pa \n" +
                "INNER JOIN \"Member\" A ON A.\"Dist\" = pa.zillaid and A.\"Upz\" = pa.upazilaid and A.\"UN\" = pa.unionid\n" +
                "and A.\"Mouza\" = pa.mouzaid and A.\"Vill\" = pa.villageid \n" +
                "INNER JOIN \"clientMap\" B ON A.\"HealthID\" = B.\"healthId\"\n" +
                "INNER JOIN \"elcoVisit\" C ON C.\"healthId\" = B.\"generatedId\"\n" +
                "INNER JOIN \"pregWomen\" D ON D.\"healthId\" = B.\"generatedId\"\n" +
                "where pa.\"mouzaid\" ='" + mouzaid  + "'\n" +
                "\tand pa.\"villageid\" = '" + villageid + "'\n" +
                "\tand pa.\"zillaid\" = '" + zillaid + "'\n" +
                "\tand pa.\"upazilaid\" = '" + upazilaid + "'\n" +
                "\tand pa.\"unionid\" = '" + unionid+ "'";
          /*      "where pa.\"provCode\" =" + provCode +
                "\tand pa.\"mouzaid\" = '" + mouzaid + "'\n" +
                "\tand pa.\"villageid\" = '" + villageid + "'\n" +
                "\tand pa.\"zillaid\" = '" + zillaid + "'\n" +
                "\tand pa.\"upazilaid\" = '" + upazilaid + "'\n" +
                "\tand pa.\"unionid\" = '" + unionid + "' and A.\"HHNo\" IN(" + HHinClause + ")";*/

        // HHinClause.substring(0, HHinClause.length() - 1)
        VariableList = "healthId,pregNo,providerId,transactionId,visit,vDate,visitStatus,currStatus,newOld,mDate,sSource,qty,unit,brand,validity,dayMonYear,referPlace,syrinsQty,mrSource,MRDate,MRAge,systemEntryDate,modifyDate,upload";
        C.DownloadJSON(sql, "elcoVisit", VariableList, "healthId,visit");


        sql = "select distinct C.\"healthId\", C.\"pregNo\", C.\"providerId\", C.\"houseGRHoldingNo\", C.\"mobileNo\",C.\"LMP\", C.\"tempLMP\", C.\"statusLMP\", C.\"EDD\", C.\"para\", C.\"gravida\", C.\"lastChildAge\", C.\"height\",C.\"bloodGroup\", C.\"riskHistory\", C.\"riskHistoryNote\", C.\"systemEntryDate\",C.\"modifyDate\",C.\"sateliteCenterName\",C.\"client\", '1' upload \n" +
                "from \"ProviderArea\" pa \n" +
                "INNER JOIN \"Member\" A ON A.\"Dist\" = pa.zillaid and A.\"Upz\" = pa.upazilaid and A.\"UN\" = pa.unionid\n" +
                "and A.\"Mouza\" = pa.mouzaid and A.\"Vill\" = pa.villageid \n" +
                "INNER JOIN \"clientMap\" B ON A.\"HealthID\" = B.\"healthId\"\n" +
                "INNER JOIN \"pregWomen\" C ON C.\"healthId\" = B.\"generatedId\"\n" +
                "where pa.\"mouzaid\" ='" + mouzaid  + "'\n" +
                "\tand pa.\"villageid\" = '" + villageid + "'\n" +
                "\tand pa.\"zillaid\" = '" + zillaid + "'\n" +
                "\tand pa.\"upazilaid\" = '" + upazilaid + "'\n" +
                "\tand pa.\"unionid\" = '" + unionid+ "'";
        //"where pa.\"provCode\" =" + provCode  + "";
                /*"\tand pa.\"mouzaid\" = '" + mouzaid + "'\n" +
                "\tand pa.\"villageid\" = '" + villageid + "'\n" +
                "\tand pa.\"zillaid\" = '" + zillaid + "'\n" +
                "\tand pa.\"upazilaid\" = '" + upazilaid + "'\n" +
                "\tand pa.\"unionid\" = '" + unionid + "' and A.\"HHNo\" IN(" + HHinClause + ")";*/

       /* VariableList = "healthId, pregNo, providerId, houseGRHoldingNo, mobileNo,LMP, tempLMP, EDD, para, gravida, lastChildAge, height,bloodGroup, riskHistory, riskHistoryNote, systemEntryDate,modifyDate, upload";*/
        VariableList = "healthId, pregNo, providerId, houseGRHoldingNo, mobileNo,LMP, tempLMP, statusLMP, EDD, para, gravida, lastChildAge, height,bloodGroup, riskHistory, riskHistoryNote, systemEntryDate,modifyDate,sateliteCenterName,client, upload";

        C.DownloadJSON(sql, "pregWomen", VariableList, "healthId, pregNo");


        //ancService
        sql = "select distinct C.\"healthId\",C.\"pregNo\",C.\"serviceId\",C.\"providerId\",C.\"visitSource\",C.\"visitDate\",C.\"visitMonth\",C.\"serviceSource\",C.\"urineAlbumin\",C.\"ironFolStatus\",C.\"ironFolQty\",C.\"ironFolUnit\",C.\"misoStatus\",C.\"misoQty\",C.\"misoUnit\",C.\"sateliteCenterName\",C.\"systemEntryDate\",C.\"modifyDate\", '1' upload\n" +
                "from \"ProviderArea\" pa \n" +
                "INNER JOIN \"Member\" A ON A.\"Dist\" = pa.zillaid and A.\"Upz\" = pa.upazilaid and A.\"UN\" = pa.unionid\n" +
                "and A.\"Mouza\" = pa.mouzaid and A.\"Vill\" = pa.villageid \n" +
                "INNER JOIN \"clientMap\" B ON A.\"HealthID\" = B.\"healthId\"\n" +
                "INNER JOIN \"ancService\" C ON C.\"healthId\" = B.\"generatedId\"\n" +
                "where pa.\"mouzaid\" ='" + mouzaid  + "'\n" +
                "\tand pa.\"villageid\" = '" + villageid + "'\n" +
                "\tand pa.\"zillaid\" = '" + zillaid + "'\n" +
                "\tand pa.\"upazilaid\" = '" + upazilaid + "'\n" +
                "\tand pa.\"unionid\" = '" + unionid+ "'";
                //"where pa.\"provCode\" =" + provCode  + "";
               /* "\tand pa.\"mouzaid\" = '" + mouzaid + "'\n" +
                "\tand pa.\"villageid\" = '" + villageid + "'\n" +
                "\tand pa.\"zillaid\" = '" + zillaid + "'\n" +
                "\tand pa.\"upazilaid\" = '" + upazilaid + "'\n" +
                "\tand pa.\"unionid\" = '" + unionid + "' and A.\"HHNo\" IN(" + HHinClause + ")";*/

        VariableList = "healthId,pregNo,serviceId,providerId,visitSource,visitDate,visitMonth,serviceSource,urineAlbumin,ironFolStatus,ironFolQty,ironFolUnit,misoStatus,misoQty,misoUnit,sateliteCenterName,systemEntryDate,modifyDate,upload";
        C.DownloadJSON(sql, "ancService", VariableList, "healthId, pregNo, serviceId");


        //delivery
        sql = "select distinct C.\"healthId\", C.\"pregNo\", C.\"providerId\", C.\"outcomePlace\", C.\"deliveryCenterName\",C.\"admissionDate\", C.\"ward\", C.\"bed\", C.\"outcomeDate\", C.\"outcomeTime\", C.\"outcomeType\",C.\"liveBirth\", C.\"stillBirth\", C.\"stillBirthFresh\", C.\"stillBirthMacerated\",C.\"abortion\", C.\"nBoy\", C.\"nGirl\", C.\"applyOxytocin\", C.\"applyTraction\",C.\"uterusMassage\", C.\"episiotomy\", C.\"misoprostol\", C.\"attendantName\", C.\"attendantDesignation\",C.\"excessBloodLoss\", C.\"lateDelivery\", C.\"blockedDelivery\", C.\"placenta\",C.\"headache\", C.\"blurryVision\", C.\"otherBodyPart\", C.\"convulsion\", C.\"others\",C.\"othersNote\", C.\"treatment\", C.\"advice\", C.\"refer\", C.\"referReason\", C.\"referCenterName\",C.\"systemEntryDate\", C.\"modifyDate\", '1' upload \n" +
                "from \"ProviderArea\" pa \n" +
                "INNER JOIN \"Member\" A ON A.\"Dist\" = pa.zillaid and A.\"Upz\" = pa.upazilaid and A.\"UN\" = pa.unionid\n" +
                "and A.\"Mouza\" = pa.mouzaid and A.\"Vill\" = pa.villageid \n" +
                "INNER JOIN \"clientMap\" B ON A.\"HealthID\" = B.\"healthId\"\n" +
                "INNER JOIN \"delivery\" C ON C.\"healthId\" = B.\"generatedId\"\n" +
                "where pa.\"mouzaid\" ='" + mouzaid  + "'\n" +
                "\tand pa.\"villageid\" = '" + villageid + "'\n" +
                "\tand pa.\"zillaid\" = '" + zillaid + "'\n" +
                "\tand pa.\"upazilaid\" = '" + upazilaid + "'\n" +
                "\tand pa.\"unionid\" = '" + unionid+ "'";
                //"where pa.\"provCode\" =" + provCode  + "";
                /*"\tand pa.\"mouzaid\" = '" + mouzaid + "'\n" +
                "\tand pa.\"villageid\" = '" + villageid + "'\n" +
                "\tand pa.\"zillaid\" = '" + zillaid + "'\n" +
                "\tand pa.\"upazilaid\" = '" + upazilaid + "'\n" +
                "\tand pa.\"unionid\" = '" + unionid + "' and A.\"HHNo\" IN(" + HHinClause + ")";*/

        VariableList = "healthId, pregNo, providerId, outcomePlace, deliveryCenterName,admissionDate, ward, bed, outcomeDate, outcomeTime, outcomeType,liveBirth, stillBirth, stillBirthFresh, stillBirthMacerated,abortion, nBoy, nGirl, applyOxytocin, applyTraction,uterusMassage, episiotomy, misoprostol, attendantName, attendantDesignation,excessBloodLoss, lateDelivery, blockedDelivery, placenta,headache, blurryVision, otherBodyPart, convulsion, others,othersNote, treatment, advice, refer, referReason, referCenterName,systemEntryDate, modifyDate, upload";
        C.DownloadJSON(sql, "delivery", VariableList, "healthId, pregNo");


        //newBorn
        sql = "select distinct C.\"healthId\", C.\"pregNo\", C.\"childNo\", C.\"providerId\", C.\"childHealthId\",C.\"birthStatus\", C.\"gender\", C.\"outcomePlace\", C.\"outcomeDate\", C.\"outcomeTime\",C.\"attendantDesignation\", C.\"outcomeType\", C.\"birthWeightStatus\", C.\"birthWeight\", C.\"immatureBirth\",C.\"dryingAfterBirth\", C.\"resassitation\", C.\"stimulation\", C.\"bagNMask\", C.\"chlorehexidin\",C.\"skinTouch\", C.\"breastFeed\", C.\"bathThreeDays\", C.\"refer\", C.\"referReason\",C.\"referCenterName\", C.\"systemEntryDate\", C.\"modifyDate\", '1' upload \n" +
                "from \"ProviderArea\" pa \n" +
                "INNER JOIN \"Member\" A ON A.\"Dist\" = pa.zillaid and A.\"Upz\" = pa.upazilaid and A.\"UN\" = pa.unionid\n" +
                "and A.\"Mouza\" = pa.mouzaid and A.\"Vill\" = pa.villageid \n" +
                "INNER JOIN \"clientMap\" B ON A.\"HealthID\" = B.\"healthId\"\n" +
                "INNER JOIN \"newBorn\" C ON C.\"healthId\" = B.\"generatedId\"\n" +
                "where pa.\"mouzaid\" ='" + mouzaid  + "'\n" +
                "\tand pa.\"villageid\" = '" + villageid + "'\n" +
                "\tand pa.\"zillaid\" = '" + zillaid + "'\n" +
                "\tand pa.\"upazilaid\" = '" + upazilaid + "'\n" +
                "\tand pa.\"unionid\" = '" + unionid+ "'";
               // "where pa.\"provCode\" =" + provCode  + "";
               /* "\tand pa.\"mouzaid\" = '" + mouzaid + "'\n" +
                "\tand pa.\"villageid\" = '" + villageid + "'\n" +
                "\tand pa.\"zillaid\" = '" + zillaid + "'\n" +
                "\tand pa.\"upazilaid\" = '" + upazilaid + "'\n" +
                "\tand pa.\"unionid\" = '" + unionid + "' and A.\"HHNo\" IN(" + HHinClause + ")";*/

        VariableList = "healthId, pregNo, childNo, providerId, childHealthId,birthStatus, gender, outcomePlace, outcomeDate, outcomeTime,attendantDesignation, outcomeType,birthWeightStatus, birthWeight, immatureBirth,dryingAfterBirth, resassitation, stimulation, bagNMask, chlorehexidin,skinTouch, breastFeed, bathThreeDays, refer, referReason,referCenterName, systemEntryDate, modifyDate, upload";
        //VariableList = "healthId, pregNo, childNo, providerId, childHealthId,birthStatus, gender, outcomePlace, outcomeDate, outcomeTime,attendantDesignation, outcomeType,birthWeightStatus, birthWeight, immatureBirth,dryingAfterBirth, resassitation, stimulation, bagNMask, chlorehexidin,skinTouch, breastFeed, bathThreeDays, refer, referReason,referCenterName, systemEntryDate, modifyDate, upload";
        C.DownloadJSON(sql, "newBorn", VariableList, "healthId, pregNo, childNo");


        //pncServiceChild
        sql = "select distinct C.\"healthId\", C.\"pregNo\", C.\"childNo\", C.\"childHealthId\", C.\"serviceId\",C.\"providerId\", C.\"visitDate\", C.\"serviceSource\", C.\"temperature\", C.\"weight\",C.\"breathingPerMinute\", C.\"dangerSign\", C.\"breastFeedingOnly\", C.\"symptom\",C.\"disease\", C.\"treatment\", C.\"advice\", C.\"refer\", C.\"referReason\", C.\"referCenterName\",C.\"systemEntryDate\", C.\"modifyDate\",  C.\"modifyDate\", C.\"visitSource\", C.\"visitMonth\", '1' upload\n" +
                "from \"ProviderArea\" pa \n" +
                "INNER JOIN \"Member\" A ON A.\"Dist\" = pa.zillaid and A.\"Upz\" = pa.upazilaid and A.\"UN\" = pa.unionid\n" +
                "and A.\"Mouza\" = pa.mouzaid and A.\"Vill\" = pa.villageid \n" +
                "INNER JOIN \"clientMap\" B ON A.\"HealthID\" = B.\"healthId\"\n" +
                "INNER JOIN \"pncServiceChild\" C ON C.\"healthId\" = B.\"generatedId\"\n" +
                "where pa.\"mouzaid\" ='" + mouzaid  + "'\n" +
                "\tand pa.\"villageid\" = '" + villageid + "'\n" +
                "\tand pa.\"zillaid\" = '" + zillaid + "'\n" +
                "\tand pa.\"upazilaid\" = '" + upazilaid + "'\n" +
                "\tand pa.\"unionid\" = '" + unionid+ "'";
                //"where pa.\"provCode\" =" + provCode  + "";
               /* "\tand pa.\"mouzaid\" = '" + mouzaid + "'\n" +
                "\tand pa.\"villageid\" = '" + villageid + "'\n" +
                "\tand pa.\"zillaid\" = '" + zillaid + "'\n" +
                "\tand pa.\"upazilaid\" = '" + upazilaid + "'\n" +
                "\tand pa.\"unionid\" = '" + unionid + "' and A.\"HHNo\" IN(" + HHinClause + ")";*/

        VariableList = "healthId, pregNo, childNo, childHealthId, serviceId,providerId, visitDate, serviceSource, temperature, weight,breathingPerMinute, dangerSign, breastFeedingOnly, symptom,disease, treatment, advice, refer, referReason, referCenterName,systemEntryDate, modifyDate, visitSource, visitMonth, upload";
        C.DownloadJSON(sql, "pncServiceChild", VariableList, "healthId, pregNo, childNo, serviceId");


        //pncServiceMother
        sql = "select distinct C.\"healthId\", C.\"pregNo\", C.\"serviceId\", C.\"providerId\", C.\"visitDate\",C.\"serviceSource\", C.\"temperature\", C.\"bpSystolic\", C.\"bpDiastolic\", C.\"hemoglobin\",C.\"breastCondition\", C.\"uterusInvolution\", C.\"hematuria\", C.\"perineum\", C.\"FPMethod\",C.\"symptom\", C.\"disease\", C.\"treatment\", C.\"advice\", C.\"refer\", C.\"referReason\", C.\"referCenterName\",C.\"systemEntryDate\", C.\"modifyDate\", C.\"visitSource\", C.\"visitMonth\", '1' upload\n" +
                "from \"ProviderArea\" pa \n" +
                "INNER JOIN \"Member\" A ON A.\"Dist\" = pa.zillaid and A.\"Upz\" = pa.upazilaid and A.\"UN\" = pa.unionid\n" +
                "and A.\"Mouza\" = pa.mouzaid and A.\"Vill\" = pa.villageid \n" +
                "INNER JOIN \"clientMap\" B ON A.\"HealthID\" = B.\"healthId\"\n" +
                "INNER JOIN \"pncServiceMother\" C ON C.\"healthId\" = B.\"generatedId\"\n" +
                "where pa.\"mouzaid\" ='" + mouzaid  + "'\n" +
                "\tand pa.\"villageid\" = '" + villageid + "'\n" +
                "\tand pa.\"zillaid\" = '" + zillaid + "'\n" +
                "\tand pa.\"upazilaid\" = '" + upazilaid + "'\n" +
                "\tand pa.\"unionid\" = '" + unionid+ "'";
                //"where pa.\"provCode\" =" + provCode  + "";
                /*"\tand pa.\"mouzaid\" = '" + mouzaid + "'\n" +
                "\tand pa.\"villageid\" = '" + villageid + "'\n" +
                "\tand pa.\"zillaid\" = '" + zillaid + "'\n" +
                "\tand pa.\"upazilaid\" = '" + upazilaid + "'\n" +
                "\tand pa.\"unionid\" = '" + unionid + "' and A.\"HHNo\" IN(" + HHinClause + ")";*/

        VariableList = "healthId, pregNo, serviceId, providerId, visitDate,serviceSource, temperature, bpSystolic, bpDiastolic, hemoglobin,breastCondition, uterusInvolution, hematuria, perineum, FPMethod,symptom, disease, treatment, advice, refer, referReason, referCenterName,systemEntryDate, modifyDate, visitSource, visitMonth, upload";
        C.DownloadJSON(sql, "pncServiceMother", VariableList, "healthId, pregNo, serviceId");

        //workPlanMaster
       /* String VariableList = "";
        String sql = "select  \"workPlanId\", \"workAreaId\", \"providerId\", \"month\", \"status\", \"systemEntryDate\", \"modifyDate\", 1 as upload\n" +
                "from \"workPlanMaster\" \n" +
                "where \"providerId\" =" + provCode + "";


        VariableList = "workPlanId,workAreaId,providerId,month,status,systemEntryDate,modifyDate,upload";
        C.DownloadJSON(sql, "workPlanMaster", VariableList, "workPlanId, workAreaId, providerId");

        //workPlanDetail
        sql = "select  \"workPlanId\", \"item\", \"workPlanDate\", \"unitNo\", \"village\", \"elcoFrom\", \"elcoTo\", \"ipcUN\",\"ipcWord\",\"ipcMouza\",\"ipcVill\",\"ipcPara\", \"ipcBariFrom\",\"ipcBariTo\",\"epiproviderId\",\"epischedulerId\",\"ccWard\",\"ccID\",\"natProgramType\",\"fpiOtherMeeting\",\"leaveType\", \"providerId\", \"systemEntryDate\", \"modifyDate\", \"otherDec\", \"remarks\", 1 as upload,status\n" +
                "from \"workPlanDetail\" \n" +
                "where status<>'2' and status<>'3' and \"providerId\" =" + provCode + "";


        VariableList = "workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,ipcUN,ipcWord,ipcMouza,ipcVill,ipcPara,ipcBariFrom,ipcBariTo,epiproviderId,epischedulerId,ccWard,ccID,natProgramType,fpiOtherMeeting,leaveType,providerId,systemEntryDate,modifyDate,otherDec,remarks,upload,status";
        C.DownloadJSON(sql, "workPlanDetail", VariableList, "workPlanId, item, workPlanDate, providerId");
*/    }


    public void DownloadMCHTables(String provCode) {

        String VariableList = "";
       // /pregWomen
      //  \"providerId\" =" + provCode + "";
        String   sql = "select  C.\"healthId\", C.\"pregNo\", C.\"providerId\", C.\"houseGRHoldingNo\", C.\"mobileNo\",C.\"LMP\", C.\"tempLMP\", C.\"statusLMP\", C.\"EDD\", C.\"para\", C.\"gravida\", C.\"lastChildAge\", C.\"height\",C.\"bloodGroup\", C.\"riskHistory\", C.\"riskHistoryNote\", C.\"systemEntryDate\",C.\"modifyDate\",C.\"sateliteCenterName\",C.\"client\", '1' upload \n" +
                "from \"ProviderArea\" pa \n" +
                "INNER JOIN \"Member\" A ON A.\"Dist\" = pa.zillaid and A.\"Upz\" = pa.upazilaid and A.\"UN\" = pa.unionid\n" +
                "and A.\"Mouza\" = pa.mouzaid and A.\"Vill\" = pa.villageid \n" +
                "INNER JOIN \"clientMap\" B ON A.\"HealthID\" = B.\"healthId\"\n" +
                "INNER JOIN \"pregWomen\" C ON C.\"healthId\" = B.\"generatedId\"\n" +
                "where pa.\"provCode\" =" + provCode  + "";
                /*"\tand pa.\"mouzaid\" = '" + mouzaid + "'\n" +
                "\tand pa.\"villageid\" = '" + villageid + "'\n" +
                "\tand pa.\"zillaid\" = '" + zillaid + "'\n" +
                "\tand pa.\"upazilaid\" = '" + upazilaid + "'\n" +
                "\tand pa.\"unionid\" = '" + unionid + "' and A.\"HHNo\" IN(" + HHinClause + ")";*/

       /* VariableList = "healthId, pregNo, providerId, houseGRHoldingNo, mobileNo,LMP, tempLMP, EDD, para, gravida, lastChildAge, height,bloodGroup, riskHistory, riskHistoryNote, systemEntryDate,modifyDate, upload";*/
        VariableList = "healthId, pregNo, providerId, houseGRHoldingNo, mobileNo,LMP, tempLMP, statusLMP, EDD, para, gravida, lastChildAge, height,bloodGroup, riskHistory, riskHistoryNote, systemEntryDate,modifyDate,sateliteCenterName,client, upload";

        C.DownloadJSON(sql, "pregWomen", VariableList, "healthId, pregNo");


        //ancService
        sql = "select  C.\"healthId\",C.\"pregNo\",C.\"serviceId\",C.\"providerId\",C.\"visitSource\",C.\"visitDate\",C.\"visitMonth\",C.\"serviceSource\",C.\"urineAlbumin\",C.\"ironFolStatus\",C.\"ironFolQty\",C.\"ironFolUnit\",C.\"misoStatus\",C.\"misoQty\",C.\"misoUnit\",C.\"sateliteCenterName\",C.\"systemEntryDate\",C.\"modifyDate\", '1' upload\n" +
                "from \"ProviderArea\" pa \n" +
                "INNER JOIN \"Member\" A ON A.\"Dist\" = pa.zillaid and A.\"Upz\" = pa.upazilaid and A.\"UN\" = pa.unionid\n" +
                "and A.\"Mouza\" = pa.mouzaid and A.\"Vill\" = pa.villageid \n" +
                "INNER JOIN \"clientMap\" B ON A.\"HealthID\" = B.\"healthId\"\n" +
                "INNER JOIN \"ancService\" C ON C.\"healthId\" = B.\"generatedId\"\n" +
                "where pa.\"provCode\" =" + provCode  + "";
               /* "\tand pa.\"mouzaid\" = '" + mouzaid + "'\n" +
                "\tand pa.\"villageid\" = '" + villageid + "'\n" +
                "\tand pa.\"zillaid\" = '" + zillaid + "'\n" +
                "\tand pa.\"upazilaid\" = '" + upazilaid + "'\n" +
                "\tand pa.\"unionid\" = '" + unionid + "' and A.\"HHNo\" IN(" + HHinClause + ")";*/

        VariableList = "healthId,pregNo,serviceId,providerId,visitSource,visitDate,visitMonth,serviceSource,urineAlbumin,ironFolStatus,ironFolQty,ironFolUnit,misoStatus,misoQty,misoUnit,sateliteCenterName,systemEntryDate,modifyDate,upload";
        C.DownloadJSON(sql, "ancService", VariableList, "healthId, pregNo, serviceId");


        //delivery
        sql = "select  C.\"healthId\", C.\"pregNo\", C.\"providerId\", C.\"outcomePlace\", C.\"deliveryCenterName\",C.\"admissionDate\", C.\"ward\", C.\"bed\", C.\"outcomeDate\", C.\"outcomeTime\", C.\"outcomeType\",C.\"liveBirth\", C.\"stillBirth\", C.\"stillBirthFresh\", C.\"stillBirthMacerated\",C.\"abortion\", C.\"nBoy\", C.\"nGirl\", C.\"applyOxytocin\", C.\"applyTraction\",C.\"uterusMassage\", C.\"episiotomy\", C.\"misoprostol\", C.\"attendantName\", C.\"attendantDesignation\",C.\"excessBloodLoss\", C.\"lateDelivery\", C.\"blockedDelivery\", C.\"placenta\",C.\"headache\", C.\"blurryVision\", C.\"otherBodyPart\", C.\"convulsion\", C.\"others\",C.\"othersNote\", C.\"treatment\", C.\"advice\", C.\"refer\", C.\"referReason\", C.\"referCenterName\",C.\"systemEntryDate\", C.\"modifyDate\", '1' upload \n" +
                "from \"ProviderArea\" pa \n" +
                "INNER JOIN \"Member\" A ON A.\"Dist\" = pa.zillaid and A.\"Upz\" = pa.upazilaid and A.\"UN\" = pa.unionid\n" +
                "and A.\"Mouza\" = pa.mouzaid and A.\"Vill\" = pa.villageid \n" +
                "INNER JOIN \"clientMap\" B ON A.\"HealthID\" = B.\"healthId\"\n" +
                "INNER JOIN \"delivery\" C ON C.\"healthId\" = B.\"generatedId\"\n" +
                "where pa.\"provCode\" =" + provCode  + "";
                /*"\tand pa.\"mouzaid\" = '" + mouzaid + "'\n" +
                "\tand pa.\"villageid\" = '" + villageid + "'\n" +
                "\tand pa.\"zillaid\" = '" + zillaid + "'\n" +
                "\tand pa.\"upazilaid\" = '" + upazilaid + "'\n" +
                "\tand pa.\"unionid\" = '" + unionid + "' and A.\"HHNo\" IN(" + HHinClause + ")";*/

        VariableList = "healthId, pregNo, providerId, outcomePlace, deliveryCenterName,admissionDate, ward, bed, outcomeDate, outcomeTime, outcomeType,liveBirth, stillBirth, stillBirthFresh, stillBirthMacerated,abortion, nBoy, nGirl, applyOxytocin, applyTraction,uterusMassage, episiotomy, misoprostol, attendantName, attendantDesignation,excessBloodLoss, lateDelivery, blockedDelivery, placenta,headache, blurryVision, otherBodyPart, convulsion, others,othersNote, treatment, advice, refer, referReason, referCenterName,systemEntryDate, modifyDate, upload";
        C.DownloadJSON(sql, "delivery", VariableList, "healthId, pregNo");


        //newBorn
        sql = "select  C.\"healthId\", C.\"pregNo\", C.\"childNo\", C.\"providerId\", C.\"childHealthId\",C.\"birthStatus\", C.\"gender\", C.\"outcomePlace\", C.\"outcomeDate\", C.\"outcomeTime\",C.\"attendantDesignation\", C.\"outcomeType\", C.\"birthWeightStatus\", C.\"birthWeight\", C.\"immatureBirth\",C.\"dryingAfterBirth\", C.\"resassitation\", C.\"stimulation\", C.\"bagNMask\", C.\"chlorehexidin\",C.\"skinTouch\", C.\"breastFeed\", C.\"bathThreeDays\", C.\"refer\", C.\"referReason\",C.\"referCenterName\", C.\"systemEntryDate\", C.\"modifyDate\", '1' upload \n" +
                "from \"ProviderArea\" pa \n" +
                "INNER JOIN \"Member\" A ON A.\"Dist\" = pa.zillaid and A.\"Upz\" = pa.upazilaid and A.\"UN\" = pa.unionid\n" +
                "and A.\"Mouza\" = pa.mouzaid and A.\"Vill\" = pa.villageid \n" +
                "INNER JOIN \"clientMap\" B ON A.\"HealthID\" = B.\"healthId\"\n" +
                "INNER JOIN \"newBorn\" C ON C.\"healthId\" = B.\"generatedId\"\n" +
                "where pa.\"provCode\" =" + provCode  + "";
               /* "\tand pa.\"mouzaid\" = '" + mouzaid + "'\n" +
                "\tand pa.\"villageid\" = '" + villageid + "'\n" +
                "\tand pa.\"zillaid\" = '" + zillaid + "'\n" +
                "\tand pa.\"upazilaid\" = '" + upazilaid + "'\n" +
                "\tand pa.\"unionid\" = '" + unionid + "' and A.\"HHNo\" IN(" + HHinClause + ")";*/

        VariableList = "healthId, pregNo, childNo, providerId, childHealthId,birthStatus, gender, outcomePlace, outcomeDate, outcomeTime,attendantDesignation, outcomeType,birthWeightStatus, birthWeight, immatureBirth,dryingAfterBirth, resassitation, stimulation, bagNMask, chlorehexidin,skinTouch, breastFeed, bathThreeDays, refer, referReason,referCenterName, systemEntryDate, modifyDate, upload";
        //VariableList = "healthId, pregNo, childNo, providerId, childHealthId,birthStatus, gender, outcomePlace, outcomeDate, outcomeTime,attendantDesignation, outcomeType,birthWeightStatus, birthWeight, immatureBirth,dryingAfterBirth, resassitation, stimulation, bagNMask, chlorehexidin,skinTouch, breastFeed, bathThreeDays, refer, referReason,referCenterName, systemEntryDate, modifyDate, upload";
        C.DownloadJSON(sql, "newBorn", VariableList, "healthId, pregNo, childNo");


        //pncServiceChild
        sql = "select  C.\"healthId\", C.\"pregNo\", C.\"childNo\", C.\"childHealthId\", C.\"serviceId\",C.\"providerId\", C.\"visitDate\", C.\"serviceSource\", C.\"temperature\", C.\"weight\",C.\"breathingPerMinute\", C.\"dangerSign\", C.\"breastFeedingOnly\", C.\"symptom\",C.\"disease\", C.\"treatment\", C.\"advice\", C.\"refer\", C.\"referReason\", C.\"referCenterName\",C.\"systemEntryDate\", C.\"modifyDate\",  C.\"modifyDate\", C.\"visitSource\", C.\"visitMonth\", '1' upload\n" +
                "from \"ProviderArea\" pa \n" +
                "INNER JOIN \"Member\" A ON A.\"Dist\" = pa.zillaid and A.\"Upz\" = pa.upazilaid and A.\"UN\" = pa.unionid\n" +
                "and A.\"Mouza\" = pa.mouzaid and A.\"Vill\" = pa.villageid \n" +
                "INNER JOIN \"clientMap\" B ON A.\"HealthID\" = B.\"healthId\"\n" +
                "INNER JOIN \"pncServiceChild\" C ON C.\"healthId\" = B.\"generatedId\"\n" +
                "where pa.\"provCode\" =" + provCode  + "";
               /* "\tand pa.\"mouzaid\" = '" + mouzaid + "'\n" +
                "\tand pa.\"villageid\" = '" + villageid + "'\n" +
                "\tand pa.\"zillaid\" = '" + zillaid + "'\n" +
                "\tand pa.\"upazilaid\" = '" + upazilaid + "'\n" +
                "\tand pa.\"unionid\" = '" + unionid + "' and A.\"HHNo\" IN(" + HHinClause + ")";*/

        VariableList = "healthId, pregNo, childNo, childHealthId, serviceId,providerId, visitDate, serviceSource, temperature, weight,breathingPerMinute, dangerSign, breastFeedingOnly, symptom,disease, treatment, advice, refer, referReason, referCenterName,systemEntryDate, modifyDate, visitSource, visitMonth, upload";
        C.DownloadJSON(sql, "pncServiceChild", VariableList, "healthId, pregNo, childNo, serviceId");


        //pncServiceMother
        sql = "select  C.\"healthId\", C.\"pregNo\", C.\"serviceId\", C.\"providerId\", C.\"visitDate\",C.\"serviceSource\", C.\"temperature\", C.\"bpSystolic\", C.\"bpDiastolic\", C.\"hemoglobin\",C.\"breastCondition\", C.\"uterusInvolution\", C.\"hematuria\", C.\"perineum\", C.\"FPMethod\",C.\"symptom\", C.\"disease\", C.\"treatment\", C.\"advice\", C.\"refer\", C.\"referReason\", C.\"referCenterName\",C.\"systemEntryDate\", C.\"modifyDate\", C.\"visitSource\", C.\"visitMonth\", '1' upload\n" +
                "from \"ProviderArea\" pa \n" +
                "INNER JOIN \"Member\" A ON A.\"Dist\" = pa.zillaid and A.\"Upz\" = pa.upazilaid and A.\"UN\" = pa.unionid\n" +
                "and A.\"Mouza\" = pa.mouzaid and A.\"Vill\" = pa.villageid \n" +
                "INNER JOIN \"clientMap\" B ON A.\"HealthID\" = B.\"healthId\"\n" +
                "INNER JOIN \"pncServiceMother\" C ON C.\"healthId\" = B.\"generatedId\"\n" +
                "where pa.\"provCode\" =" + provCode  + "";
                /*"\tand pa.\"mouzaid\" = '" + mouzaid + "'\n" +
                "\tand pa.\"villageid\" = '" + villageid + "'\n" +
                "\tand pa.\"zillaid\" = '" + zillaid + "'\n" +
                "\tand pa.\"upazilaid\" = '" + upazilaid + "'\n" +
                "\tand pa.\"unionid\" = '" + unionid + "' and A.\"HHNo\" IN(" + HHinClause + ")";*/

        VariableList = "healthId, pregNo, serviceId, providerId, visitDate,serviceSource, temperature, bpSystolic, bpDiastolic, hemoglobin,breastCondition, uterusInvolution, hematuria, perineum, FPMethod,symptom, disease, treatment, advice, refer, referReason, referCenterName,systemEntryDate, modifyDate, visitSource, visitMonth, upload";
        C.DownloadJSON(sql, "pncServiceMother", VariableList, "healthId, pregNo, serviceId");

        //workPlanMaster
       /* String VariableList = "";
        String sql = "select  \"workPlanId\", \"workAreaId\", \"providerId\", \"month\", \"status\", \"systemEntryDate\", \"modifyDate\", 1 as upload\n" +
                "from \"workPlanMaster\" \n" +
                "where \"providerId\" =" + provCode + "";


        VariableList = "workPlanId,workAreaId,providerId,month,status,systemEntryDate,modifyDate,upload";
        C.DownloadJSON(sql, "workPlanMaster", VariableList, "workPlanId, workAreaId, providerId");

        //workPlanDetail
        sql = "select  \"workPlanId\", \"item\", \"workPlanDate\", \"unitNo\", \"village\", \"elcoFrom\", \"elcoTo\", \"ipcUN\",\"ipcWord\",\"ipcMouza\",\"ipcVill\",\"ipcPara\", \"ipcBariFrom\",\"ipcBariTo\",\"epiproviderId\",\"epischedulerId\",\"ccWard\",\"ccID\",\"natProgramType\",\"fpiOtherMeeting\",\"leaveType\", \"providerId\", \"systemEntryDate\", \"modifyDate\", \"otherDec\", \"remarks\", 1 as upload,status\n" +
                "from \"workPlanDetail\" \n" +
                "where status<>'2' and status<>'3' and \"providerId\" =" + provCode + "";


        VariableList = "workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,ipcUN,ipcWord,ipcMouza,ipcVill,ipcPara,ipcBariFrom,ipcBariTo,epiproviderId,epischedulerId,ccWard,ccID,natProgramType,fpiOtherMeeting,leaveType,providerId,systemEntryDate,modifyDate,otherDec,remarks,upload,status";
        C.DownloadJSON(sql, "workPlanDetail", VariableList, "workPlanId, item, workPlanDate, providerId");
*/    }

    private void DisplayAdvanceSearchPopup() {
        final Dialog popupView = new Dialog(PregRegView.this);
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
                } else {
                    ((LinearLayout) findViewById(R.id.secDistrict)).setVisibility(View.GONE);
                }

                if (((CheckBox) popupView.findViewById(R.id.Vlblupazilla)).isChecked()) {

                    ((LinearLayout) findViewById(R.id.secDistrict)).setVisibility(View.VISIBLE);
                    ((LinearLayout) findViewById(R.id.secUpazilla)).setVisibility(View.VISIBLE);


                } else {
                    ((LinearLayout) findViewById(R.id.secUpazilla)).setVisibility(View.GONE);
                }


                if (((CheckBox) popupView.findViewById(R.id.Vlblunion)).isChecked()) {
                    ((LinearLayout) findViewById(R.id.secUnion)).setVisibility(View.VISIBLE);
                    ((LinearLayout) findViewById(R.id.secDistrict)).setVisibility(View.VISIBLE);
                    ((LinearLayout) findViewById(R.id.secUpazilla)).setVisibility(View.VISIBLE);
                } else {
                    ((LinearLayout) findViewById(R.id.secUnion)).setVisibility(View.GONE);
                }

                if (((CheckBox) popupView.findViewById(R.id.VlblVillSearch)).isChecked()) {
                    ((LinearLayout) findViewById(R.id.secVillSearch)).setVisibility(View.VISIBLE);
                    ((LinearLayout) findViewById(R.id.secUnion)).setVisibility(View.VISIBLE);
                    ((LinearLayout) findViewById(R.id.secDistrict)).setVisibility(View.VISIBLE);
                    ((LinearLayout) findViewById(R.id.secUpazilla)).setVisibility(View.VISIBLE);
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

                    ((Spinner) findViewById(R.id.txtVillSearch)).setAdapter(C.getArrayAdapter("Select '  ' VillageName from Village union select  substr('0' || MOUZAID , -3, 3)||'-'||VILLAGEID||'-'||VILLAGENAMEENG VillageName from Village where ZIlLAID='" + desval + "' and UPAZILAID='" + upval + "' and UNIONID='" + uval + "'"));


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
                    Connection.MessageBox(PregRegView.this, ex.getMessage());
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
        // ListView list = (ListView)findViewById(R.id.lstData);
       /* ListView list= (ListView) findViewById(R.id.lstData);
        //View header = getLayoutInflater().inflate(R.layout.memberlistheading, null);
        //list.addHeaderView(header);
        //  list.addView(header);*/


        try {
            String SQL = "SELECT S.Dist AS Dist,S.Upz AS Upz,S.UN AS UN,S.Mouza AS Mouza, S.Vill AS Vill,S.ElcoNo AS Elcono, S.NID AS NID,\n" +
                    "       S.BRID AS BRID,\n" +
                    "       S.generatedId AS generatedId,\n" +
                    "       S.SNo AS SNo,\n" +
                    "       S.HHNo AS HHNO,\n" +
                    "       S.HealthId AS HealthId,\n" +
                    "       S.VName AS VName,\n" +
                    "       S.WNameEng AS WNameEng,\n" +
                    "       S.HusName AS HusName,\n" +
                    "       S.MotherName AS Mother,\n" +
                    "       S.FatherName AS Father,\n" +
                    "       S.WAge AS WAge,\n" +
                    "       S.Mobno AS Mobno,\n" +
                    "       S.Sex AS Sex,\n" +
                    "       S.eFPIhid as eFPIhid,\n" +
                    "       S.marrDate AS MDate,\n" +
                    "       S.Son AS Son, S.Dau AS Dau, S.Edu AS Edu\n" +
                    "  FROM ( \n" +
                    "    SELECT m.dist,m.Upz,m.UN,m.Mouza AS Mouza,m.Vill AS Vill,eco.marrDate, eco.ElcoNo, m.NID AS NID,\n" +
                    "           m.BRID AS BRID,\n" +
                    "           cM.generatedId AS generatedId,\n" +
                    "           m.SNO AS SNO,\n" +
                    "           m.HHNO AS HHNO,\n" +
                    "           m.HealthId AS HealthId,\n" +
                    "           v.VILLAGENAMEENG AS VName,\n" +
                    "           m.nameeng AS WNameEng,\n" +
                    "           s.nameeng AS HusName,\n" +
                    "           ( CAST ( ( ( julianday( date( 'now' )  ) - julianday( m.DOB )  ) / 30.4 )  AS int ) / 12 ) AS WAge,\n" +
                    "           ifnull( m.MobileNo1, '' ) AS Mobno,\n" +
                    "           ifnull(eco.son,'') as Son, ifnull(eco.dau,'') as Dau,ifnull(m.EDU,'') AS Edu,\n" +
                    "           m.Sex AS Sex,\n" +
                    "           ( CASE\n" +
                    "           WHEN efpi.HealthId IS NOT NULL THEN 'Y'\n" +
                    "           ELSE '' END ) AS eFPIhid,\n" +
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
                    "           LEFT OUTER JOIN PregWomen e\n" +
                    "                        ON e.healthId = cM.generatedId\n" +
                    "           INNER JOIN ElcoVisit ev1\n" +
                    "                   ON ev1.healthId = cM.generatedId \n" +
                    "    AND\n" +
                    "    ev1.currStatus = '12'\n" +
                    "           INNER JOIN ElcoVisit ev2\n" +
                    "                   ON ev2.healthId = ev1.healthId \n" +
                    "    AND\n" +
                    "    ev2.currStatus = '12' \n" +
                    "    AND\n" +
                    "    ev2.pregNo = ev1.pregNo\n" +
                    "           LEFT JOIN ELCO eco\n" +
                    "                  ON eco.healthId = ev2.healthId\n" +
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

                    ") \n" +
                    "AS S";

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

            if (wheresql.length() > 0) {
                SQL = SQL + " WHERE " + wheresql;
            }
            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();
            dataList = new ArrayList<HashMap<String, String>>();

            int slno = 0;
            //ListView list = (ListView)findViewById(R.id.lstData);
            list.setAdapter(null);

            while (!cur.isAfterLast()) {
                slno += 1;
                HashMap<String, String> map = new HashMap<String, String>();


                map.put("slno", Integer.toString(slno));
                map.put("hhho", cur.getString(cur.getColumnIndex("HHNO")));
                map.put("healthId", cur.getString(cur.getColumnIndex("HealthId")));
                map.put("elcono", cur.getString(cur.getColumnIndex("Elcono")).replace("null", ""));
                map.put("vname", cur.getString(cur.getColumnIndex("VName")));
                map.put("wname", cur.getString(cur.getColumnIndex("WNameEng")));
                map.put("hname", cur.getString(cur.getColumnIndex("HusName")));
                map.put("wage", cur.getString(cur.getColumnIndex("WAge")));
                map.put("mdate", cur.getString(cur.getColumnIndex("MDate")));
                map.put("mobno", cur.getString(cur.getColumnIndex("Mobno")).replace("null", ""));
                map.put("son", cur.getString(cur.getColumnIndex("Son")).replace("null", ""));
                map.put("dau", cur.getString(cur.getColumnIndex("Dau")).replace("null", ""));
                map.put("edu", cur.getString(cur.getColumnIndex("Edu")).replace("null", ""));
                map.put("GeneratedId", cur.getString(cur.getColumnIndex("generatedId")));


               /* if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("99")) {
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
                }*/


                dataList.add(map);

                dataAdapter = new SimpleAdapter(PregRegView.this, dataList, R.layout.pregviewrow, new String[]{"SNo"},
                        new int[]{R.id.Slno});

                list.setAdapter(new DataListAdapter(this));

                cur.moveToNext();
                totalCount += 1;
            }
            cur.close();
            /*((TextView)findViewById(R.id.lblTCount)).setText("মোট "+ String.valueOf(slno)+ " টি ডাটা পাওয়া গিয়েছে" );*/
            ((TextView) findViewById(R.id.lblTCount)).setText(String.valueOf(slno));

        } catch (Exception e) {
            Connection.MessageBox(PregRegView.this, e.getMessage());
        }

    }
    public void SearchVillageWise(String Mouza, String Village) {

        try {

            String SQL = "";
            //Integer UN = Integer.valueOf(C.ReturnSingleValue("Select UNIONID from Unions where" + " ZILLAID like '%" + g.getDistrict() + "%' AND  UPAZILAID like '%" + g.getUpazila() + "%'"));
            Integer val = (((Spinner) findViewById(R.id.txtVillSearch1)).getSelectedItemPosition());// == 0 ? "" : Global.Left(((Spinner) findViewById(R.id.txtVillSearch1)).getSelectedItem().toString(), 3));
            if (val == 0) {

                //((Spinner) findViewById(R.id.txtVillSearch)).setAdapter(C.getArrayAdapter("Select '  ' VillageName from Village union select  substr('0' || MOUZAID , -3, 3)||'-'||VILLAGEID||'-'||VILLAGENAMEENG VillageName from Village where ZIlLAID='"+ desval+"' and UPAZILAID='"+ upval+"' and UNIONID='"+ uval +"'"));

            } else if (val == 1) {
                SQL = "SELECT distinct S.Dist AS Dist,S.Upz AS Upz,S.UN AS UN,S.Mouza AS Mouza, S.Vill AS Vill,S.ElcoNo AS Elcono, S.NID AS NID,\n" +
                        "       S.BRID AS BRID,\n" +
                        "       S.generatedId AS generatedId,\n" +
                        "       S.SNo AS SNo,\n" +
                        "       S.HHNo AS HHNO,\n" +
                        "       S.HealthId AS HealthId,\n" +
                        "       S.VName AS VName,\n" +
                        "       S.WNameEng AS WNameEng,\n" +
                        "       S.HusName AS HusName,\n" +
                        "       S.MotherName AS Mother,\n" +
                        "       S.FatherName AS Father,\n" +
                        "       S.WAge AS WAge,\n" +
                        "       S.Mobno AS Mobno,\n" +
                        "       S.Sex AS Sex,\n" +
                        "       ifnull(S.marrDate,'') AS MDate,\n" +
                        "       S.Son AS Son,S.lmp as lmp,\n" +
                        "       S.edd as edd, S.Dau AS Dau, S.Edu AS Edu,S.regDT AS egDT\n" +
                        "  FROM ( \n" +
                        "    SELECT eco.regDT,m.dist,m.Upz,m.UN,m.Mouza AS Mouza,m.Vill AS Vill,eco.marrDate, ifnull(eco.ElcoNo,'') as ElcoNo , m.NID AS NID,\n" +
                        "           m.BRID AS BRID,\n" +
                        "           cM.generatedId AS generatedId,e.systemEntryDate as systemEntryDate,\n" +
                        "           m.SNO AS SNO,\n" +
                        "           m.HHNO AS HHNO,\n" +
                        "           m.HealthId AS HealthId,\n" +
                        "           v.VILLAGENAMEENG AS VName,\n" +
                        "           m.nameeng AS WNameEng,\n" +
                        "           s.nameeng AS HusName,\n" +
                        "           ( CAST ( ( ( julianday( date( 'now' )  ) - julianday( m.DOB )  ) / 30.4 )  AS int ) / 12 ) AS WAge,\n" +
                        "           ifnull( m.MobileNo1, '' ) AS Mobno,strftime( '%d/%m/%Y', date( e.LMP )  ) AS lmp,\n" +
                        "           strftime( '%d/%m/%Y', date( e.EDD )  ) AS edd,\n" +
                        "           ifnull(eco.son,'') as Son, ifnull(eco.dau,'') as Dau,ifnull(m.EDU,'') AS Edu,\n" +
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
                        "           LEFT OUTER JOIN PregWomen e\n" +
                        "                        ON e.healthId = cM.generatedId\n" +
                        "           INNER JOIN ElcoVisit ev1\n" +
                        "                   ON ev1.healthId = cM.generatedId \n" +
                        "    AND\n" +
                        "    ev1.currStatus = '12'\n" +
                        "           INNER JOIN ElcoVisit ev2\n" +
                        "                   ON ev2.healthId = ev1.healthId \n" +
                        "    AND\n" +
                        "    ev2.currStatus = '12' \n" +
                        "    AND\n" +
                        "    ev2.pregNo = ev1.pregNo\n" +
                        "           LEFT JOIN ELCO eco\n" +
                        "                  ON eco.healthId = ev2.healthId\n" +
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

                        ") \n" +
                        "AS S" +
                        "                    \n" +
                        "                    \n" +
                        "\n" +
                        "                    \n" +
                        "                    \n" +
                        " \n" +
                        " ";
                String wheresql = "";
                //String wheresql = " Dist like '%" + g.getDistrict() + "%' AND  Upz like '%" + g.getUpazila() + "%' AND  UN like '%" + g.getUnion() + "%'";
                wheresql = " Dist like '%" + g.getDistrict() + "%' AND  Upz like '%" + g.getUpazila() + "%' AND  UN like '%" + g.getUnion() + "%'";
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
                        wheresql += " systemEntryDate between'" + Y + "-" + M + "-" + D + "' and '" + Y1 + "-" + M1 + "-" + D1 + "'";
                    }


                }
                if (wheresql.length() > 0) {
                    SQL = SQL + " WHERE " + wheresql;
                }
            } else if (val > 1) {


                SQL = "SELECT distinct S.Dist AS Dist,S.Upz AS Upz,S.UN AS UN,S.Mouza AS Mouza, S.Vill AS Vill,S.ElcoNo AS Elcono, S.NID AS NID,\n" +
                        "       S.BRID AS BRID,\n" +
                        "       S.generatedId AS generatedId,\n" +
                        "       S.SNo AS SNo,\n" +
                        "       S.HHNo AS HHNO,\n" +
                        "       S.HealthId AS HealthId,\n" +
                        "       S.VName AS VName,\n" +
                        "       S.WNameEng AS WNameEng,\n" +
                        "       S.HusName AS HusName,\n" +
                        "       S.MotherName AS Mother,\n" +
                        "       S.FatherName AS Father,\n" +
                        "       S.WAge AS WAge,\n" +
                        "       S.Mobno AS Mobno,\n" +
                        "       S.Sex AS Sex,\n" +
                        "       ifnull(S.marrDate,'') AS MDate,\n" +
                        "       S.Son AS Son,S.lmp as lmp,\n" +
                        "       S.edd as edd, S.Dau AS Dau, S.Edu AS Edu,S.regDT AS egDT\n" +
                        "  FROM ( \n" +
                        "    SELECT eco.regDT,m.dist,m.Upz,m.UN,m.Mouza AS Mouza,m.Vill AS Vill,eco.marrDate, ifnull(eco.ElcoNo,'') as ElcoNo , m.NID AS NID,\n" +
                        "           m.BRID AS BRID,\n" +
                        "           cM.generatedId AS generatedId,e.systemEntryDate as systemEntryDate,\n" +
                        "           m.SNO AS SNO,\n" +
                        "           m.HHNO AS HHNO,\n" +
                        "           m.HealthId AS HealthId,\n" +
                        "           v.VILLAGENAMEENG AS VName,\n" +
                        "           m.nameeng AS WNameEng,\n" +
                        "           s.nameeng AS HusName,\n" +
                        "           ( CAST ( ( ( julianday( date( 'now' )  ) - julianday( m.DOB )  ) / 30.4 )  AS int ) / 12 ) AS WAge,\n" +
                        "           ifnull( m.MobileNo1, '' ) AS Mobno,strftime( '%d/%m/%Y', date( e.LMP )  ) AS lmp,\n" +
                        "           strftime( '%d/%m/%Y', date( e.EDD )  ) AS edd,\n" +
                        "           ifnull(eco.son,'') as Son, ifnull(eco.dau,'') as Dau,ifnull(m.EDU,'') AS Edu,\n" +
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
                        "           LEFT OUTER JOIN PregWomen e\n" +
                        "                        ON e.healthId = cM.generatedId\n" +
                        "           INNER JOIN ElcoVisit ev1\n" +
                        "                   ON ev1.healthId = cM.generatedId \n" +
                        "    AND\n" +
                        "    ev1.currStatus = '12'\n" +
                        "           INNER JOIN ElcoVisit ev2\n" +
                        "                   ON ev2.healthId = ev1.healthId \n" +
                        "    AND\n" +
                        "    ev2.currStatus = '12' \n" +
                        "    AND\n" +
                        "    ev2.pregNo = ev1.pregNo\n" +
                        "           LEFT JOIN ELCO eco\n" +
                        "                  ON eco.healthId = ev2.healthId\n" +
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

                        ") \n" +
                        "AS S" +
                        "                    \n" +
                        "                    \n" +
                        "\n" +
                        "                    \n" +
                        "                    \n" +
                        " \n" +
                        " ";
                // String wheresql = " Dist like '%" + g.getDistrict() + "%' AND  Upz like '%" + g.getUpazila() + "%' AND  UN like '%" + g.getUnion() + "%' AND Mouza like '%" + Integer.valueOf(Mouza) + "%' AND Vill like '%" + Integer.valueOf(Village) + "%'";
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
                        wheresql += " systemEntryDate between'" + Y + "-" + M + "-" + D + "' and '" + Y1 + "-" + M1 + "-" + D1 + "'";
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
                map.put("wage", cur.getString(cur.getColumnIndex("WAge")));
                map.put("mdate", cur.getString(cur.getColumnIndex("MDate")));//.replace("null", "")
                map.put("mobno", cur.getString(cur.getColumnIndex("Mobno")));
                map.put("son", cur.getString(cur.getColumnIndex("Son")));
                map.put("dau", cur.getString(cur.getColumnIndex("Dau")));
                map.put("edu", cur.getString(cur.getColumnIndex("Edu")));
                map.put("GeneratedId", cur.getString(cur.getColumnIndex("generatedId")));
                map.put("LMP", cur.getString(cur.getColumnIndex("lmp")));
                map.put("EDD", cur.getString(cur.getColumnIndex("edd")));
                map.put("UN", cur.getString(cur.getColumnIndex("UN")));
                map.put("Mouza", cur.getString(cur.getColumnIndex("Mouza")));
                map.put("Vill", cur.getString(cur.getColumnIndex("Vill")));


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

                dataAdapter = new SimpleAdapter(PregRegView.this, dataList, R.layout.pregviewrow, new String[]{"SNo"},
                        new int[]{R.id.Slno});

                list.setAdapter(new DataListAdapter(this));

                cur.moveToNext();
                totalCount += 1;
            }
            cur.close();
            /*((TextView)findViewById(R.id.lblTCount)).setText("মোট "+ String.valueOf(slno)+ " টি ডাটা পাওয়া গিয়েছে" );*/
            ((TextView) findViewById(R.id.lblTCount)).setText(String.valueOf(slno));

        } catch (Exception e) {
            Connection.MessageBox(PregRegView.this, e.getMessage());
        }

    }
   /* public void SearchVillageWise(String Mouza, String Village) {

        try {

            String SQL = "";

            Integer val = (((Spinner) findViewById(R.id.txtVillSearch1)).getSelectedItemPosition());// == 0 ? "" : Global.Left(((Spinner) findViewById(R.id.txtVillSearch1)).getSelectedItem().toString(), 3));
            Integer val1 = (((Spinner) findViewById(R.id.spnProvider)).getSelectedItemPosition());
            if (val == 0) {

                //((Spinner) findViewById(R.id.txtVillSearch)).setAdapter(C.getArrayAdapter("Select '  ' VillageName from Village union select  substr('0' || MOUZAID , -3, 3)||'-'||VILLAGEID||'-'||VILLAGENAMEENG VillageName from Village where ZIlLAID='"+ desval+"' and UPAZILAID='"+ upval+"' and UNIONID='"+ uval +"'"));

            } else if (val == 1 && val1 == 1) {
                SQL = "SELECT S.Dist AS Dist,S.Upz AS Upz,S.UN AS UN,S.Mouza AS Mouza, S.Vill AS Vill,S.ElcoNo AS Elcono, S.NID AS NID,\n" +
                        "       S.BRID AS BRID,\n" +
                        "       S.generatedId AS generatedId,\n" +
                        "       S.SNo AS SNo,\n" +
                        "       S.HHNo AS HHNO,\n" +
                        "       S.HealthId AS HealthId,\n" +
                        "       S.VName AS VName,\n" +
                        "       S.WNameEng AS WNameEng,\n" +
                        "       S.HusName AS HusName,\n" +
                        "       S.MotherName AS Mother,\n" +
                        "       S.FatherName AS Father,\n" +
                        "       S.WAge AS WAge,\n" +
                        "       S.Mobno AS Mobno,\n" +
                        "       S.Sex AS Sex,\n" +
                        "       S.eFPIhid as eFPIhid,\n" +
                        "       ifnull(S.marrDate,'') AS MDate,\n" +
                        "       S.Son AS Son,S.lmp as lmp,\n" +
                        "       S.edd as edd, S.Dau AS Dau, S.Edu AS Edu,S.regDT AS egDT\n" +
                        "  FROM ( \n" +
                        "    SELECT eco.regDT,m.dist,m.Upz,m.UN,m.Mouza AS Mouza,m.Vill AS Vill,eco.marrDate, ifnull(eco.ElcoNo,'') as ElcoNo , m.NID AS NID,\n" +
                        "           m.BRID AS BRID,\n" +
                        "           cM.generatedId AS generatedId,e.systemEntryDate as systemEntryDate,\n" +
                        "           m.SNO AS SNO,\n" +
                        "           m.HHNO AS HHNO,\n" +
                        "           m.HealthId AS HealthId,\n" +
                        "           v.VILLAGENAMEENG AS VName,\n" +
                        "           m.nameeng AS WNameEng,\n" +
                        "           s.nameeng AS HusName,\n" +
                        "           ( CAST ( ( ( julianday( date( 'now' )  ) - julianday( m.DOB )  ) / 30.4 )  AS int ) / 12 ) AS WAge,\n" +
                        "           ifnull( m.MobileNo1, '' ) AS Mobno,strftime( '%d/%m/%Y', date( e.LMP )  ) AS lmp,\n" +
                        "           strftime( '%d/%m/%Y', date( e.EDD )  ) AS edd,\n" +
                        "           ifnull(eco.son,'') as Son, ifnull(eco.dau,'') as Dau,ifnull(m.EDU,'') AS Edu,\n" +
                        "           m.Sex AS Sex,\n" +
                        "           ( CASE\n" +
                        "           WHEN efpi.HealthId IS NOT NULL THEN 'Y'\n" +
                        "           ELSE '' END ) AS eFPIhid,\n" +
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
                        "           LEFT OUTER JOIN PregWomen e\n" +
                        "                        ON e.healthId = cM.generatedId\n" +
                        "           LEFT JOIN PregWomenFPI efpi\n" +
                        "                   ON efpi.HealthId = cM.generatedId\n" +
                        "           INNER JOIN ElcoVisit ev1\n" +
                        "                   ON ev1.healthId = cM.generatedId \n" +
                        "    AND\n" +
                        "    ev1.currStatus = '12'\n" +
                        "           INNER JOIN ElcoVisit ev2\n" +
                        "                   ON ev2.healthId = ev1.healthId \n" +
                        "    AND\n" +
                        "    ev2.currStatus = '12' \n" +
                        "    AND\n" +
                        "    ev2.pregNo = ev1.pregNo\n" +
                        "           LEFT JOIN ELCO eco\n" +
                        "                  ON eco.healthId = ev2.healthId\n" +
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

                        ") \n" +
                        "AS S" +
                        "                    \n" +
                        "                    \n" +
                        "\n" +
                        "                    \n" +
                        "                    \n" +
                        " \n" +
                        " ";
                //   String wheresql = " Dist like '%" + g.getDistrict() + "%' AND  Upz like '%" + g.getUpazila() + "%' AND  UN like '%" + g.getUnion() + "%'";
                String wheresql = " Dist like '%" + g.getDistrict() + "%' AND  Upz like '%" + g.getUpazila() + "%' AND  UN like '%" + g.getUnion() + "%' AND  Mouza in(Select mouzaid from ProviderArea where cast(ProvCode As Int)='" + Global.Left(spnProvider.getSelectedItem().toString(), 6) + "')";
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
                        wheresql += " systemEntryDate between'" + Y + "-" + M + "-" + D + "' and '" + Y1 + "-" + M1 + "-" + D1 + "'";
                    }


                }
                if (wheresql.length() > 0) {
                    SQL = SQL + " WHERE " + wheresql;
                }
            } else if (val == 1) {
                SQL = "SELECT S.Dist AS Dist,S.Upz AS Upz,S.UN AS UN,S.Mouza AS Mouza, S.Vill AS Vill,S.ElcoNo AS Elcono, S.NID AS NID,\n" +
                        "       S.BRID AS BRID,\n" +
                        "       S.generatedId AS generatedId,\n" +
                        "       S.SNo AS SNo,\n" +
                        "       S.HHNo AS HHNO,\n" +
                        "       S.HealthId AS HealthId,\n" +
                        "       S.VName AS VName,\n" +
                        "       S.WNameEng AS WNameEng,\n" +
                        "       S.HusName AS HusName,\n" +
                        "       S.MotherName AS Mother,\n" +
                        "       S.FatherName AS Father,\n" +
                        "       S.WAge AS WAge,\n" +
                        "       S.Mobno AS Mobno,\n" +
                        "       S.Sex AS Sex,\n" +
                        "       S.eFPIhid as eFPIhid,\n" +
                        "       ifnull(S.marrDate,'') AS MDate,\n" +
                        "       S.Son AS Son,S.lmp as lmp,\n" +
                        "       S.edd as edd, S.Dau AS Dau, S.Edu AS Edu,S.regDT AS egDT\n" +
                        "  FROM ( \n" +
                        "    SELECT eco.regDT,m.dist,m.Upz,m.UN,m.Mouza AS Mouza,m.Vill AS Vill,eco.marrDate, ifnull(eco.ElcoNo,'') as ElcoNo , m.NID AS NID,\n" +
                        "           m.BRID AS BRID,\n" +
                        "           cM.generatedId AS generatedId,e.systemEntryDate as systemEntryDate,\n" +
                        "           m.SNO AS SNO,\n" +
                        "           m.HHNO AS HHNO,\n" +
                        "           m.HealthId AS HealthId,\n" +
                        "           v.VILLAGENAMEENG AS VName,\n" +
                        "           m.nameeng AS WNameEng,\n" +
                        "           s.nameeng AS HusName,\n" +
                        "           ( CAST ( ( ( julianday( date( 'now' )  ) - julianday( m.DOB )  ) / 30.4 )  AS int ) / 12 ) AS WAge,\n" +
                        "           ifnull( m.MobileNo1, '' ) AS Mobno,strftime( '%d/%m/%Y', date( e.LMP )  ) AS lmp,\n" +
                        "           strftime( '%d/%m/%Y', date( e.EDD )  ) AS edd,\n" +
                        "           ifnull(eco.son,'') as Son, ifnull(eco.dau,'') as Dau,ifnull(m.EDU,'') AS Edu,\n" +
                        "           m.Sex AS Sex,\n" +
                        "           ( CASE\n" +
                        "           WHEN efpi.HealthId IS NOT NULL THEN 'Y'\n" +
                        "           ELSE '' END ) AS eFPIhid,\n" +
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
                        "           LEFT OUTER JOIN PregWomen e\n" +
                        "                        ON e.healthId = cM.generatedId\n" +
                        "           LEFT JOIN PregWomenFPI efpi\n" +
                        "                   ON efpi.HealthId = cM.generatedId\n" +
                        "           INNER JOIN ElcoVisit ev1\n" +
                        "                   ON ev1.healthId = cM.generatedId \n" +
                        "    AND\n" +
                        "    ev1.currStatus = '12'\n" +
                        "           INNER JOIN ElcoVisit ev2\n" +
                        "                   ON ev2.healthId = ev1.healthId \n" +
                        "    AND\n" +
                        "    ev2.currStatus = '12' \n" +
                        "    AND\n" +
                        "    ev2.pregNo = ev1.pregNo\n" +
                        "           LEFT JOIN ELCO eco\n" +
                        "                  ON eco.healthId = ev2.healthId\n" +
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

                        ") \n" +
                        "AS S" +
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
                        wheresql += " systemEntryDate between'" + Y + "-" + M + "-" + D + "' and '" + Y1 + "-" + M1 + "-" + D1 + "'";
                    }


                }
                if (wheresql.length() > 0) {
                    SQL = SQL + " WHERE " + wheresql;
                }
            } else if (val > 1) {


                SQL = "SELECT S.Dist AS Dist,S.Upz AS Upz,S.UN AS UN,S.Mouza AS Mouza, S.Vill AS Vill,S.ElcoNo AS Elcono, S.NID AS NID,\n" +
                        "       S.BRID AS BRID,\n" +
                        "       S.generatedId AS generatedId,\n" +
                        "       S.SNo AS SNo,\n" +
                        "       S.HHNo AS HHNO,\n" +
                        "       S.HealthId AS HealthId,\n" +
                        "       S.VName AS VName,\n" +
                        "       S.WNameEng AS WNameEng,\n" +
                        "       S.HusName AS HusName,\n" +
                        "       S.MotherName AS Mother,\n" +
                        "       S.FatherName AS Father,\n" +
                        "       S.WAge AS WAge,\n" +
                        "       S.Mobno AS Mobno,\n" +
                        "       S.Sex AS Sex,\n" +
                        "       S.eFPIhid as eFPIhid,\n" +
                        "       ifnull(S.marrDate,'') AS MDate,\n" +
                        "       S.Son AS Son,S.lmp as lmp,\n" +
                        "       S.edd as edd, S.Dau AS Dau, S.Edu AS Edu,S.regDT AS egDT\n" +
                        "  FROM ( \n" +
                        "    SELECT eco.regDT,m.dist,m.Upz,m.UN,m.Mouza AS Mouza,m.Vill AS Vill,eco.marrDate, ifnull(eco.ElcoNo,'') as ElcoNo , m.NID AS NID,\n" +
                        "           m.BRID AS BRID,\n" +
                        "           cM.generatedId AS generatedId,e.systemEntryDate as systemEntryDate,\n" +
                        "           m.SNO AS SNO,\n" +
                        "           m.HHNO AS HHNO,\n" +
                        "           m.HealthId AS HealthId,\n" +
                        "           v.VILLAGENAMEENG AS VName,\n" +
                        "           m.nameeng AS WNameEng,\n" +
                        "           s.nameeng AS HusName,\n" +
                        "           ( CAST ( ( ( julianday( date( 'now' )  ) - julianday( m.DOB )  ) / 30.4 )  AS int ) / 12 ) AS WAge,\n" +
                        "           ifnull( m.MobileNo1, '' ) AS Mobno,strftime( '%d/%m/%Y', date( e.LMP )  ) AS lmp,\n" +
                        "           strftime( '%d/%m/%Y', date( e.EDD )  ) AS edd,\n" +
                        "           ifnull(eco.son,'') as Son, ifnull(eco.dau,'') as Dau,ifnull(m.EDU,'') AS Edu,\n" +
                        "           m.Sex AS Sex,\n" +
                        "           ( CASE\n" +
                        "           WHEN efpi.HealthId IS NOT NULL THEN 'Y'\n" +
                        "           ELSE '' END ) AS eFPIhid,\n" +
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
                        "           LEFT OUTER JOIN PregWomen e\n" +
                        "                        ON e.healthId = cM.generatedId\n" +
                        "           LEFT JOIN PregWomenFPI efpi\n" +
                        "                   ON efpi.HealthId = cM.generatedId\n" +
                        "           INNER JOIN ElcoVisit ev1\n" +
                        "                   ON ev1.healthId = cM.generatedId \n" +
                        "    AND\n" +
                        "    ev1.currStatus = '12'\n" +
                        "           INNER JOIN ElcoVisit ev2\n" +
                        "                   ON ev2.healthId = ev1.healthId \n" +
                        "    AND\n" +
                        "    ev2.currStatus = '12' \n" +
                        "    AND\n" +
                        "    ev2.pregNo = ev1.pregNo\n" +
                        "           LEFT JOIN ELCO eco\n" +
                        "                  ON eco.healthId = ev2.healthId\n" +
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

                        ") \n" +
                        "AS S" +
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
                        wheresql += " systemEntryDate between'" + Y + "-" + M + "-" + D + "' and '" + Y1 + "-" + M1 + "-" + D1 + "'";
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
                map.put("wage", cur.getString(cur.getColumnIndex("WAge")));
                map.put("mdate", cur.getString(cur.getColumnIndex("MDate")));//.replace("null", "")
                map.put("mobno", cur.getString(cur.getColumnIndex("Mobno")));
                map.put("son", cur.getString(cur.getColumnIndex("Son")));
                map.put("dau", cur.getString(cur.getColumnIndex("Dau")));
                map.put("edu", cur.getString(cur.getColumnIndex("Edu")));
                map.put("eFPIhid", cur.getString(cur.getColumnIndex("eFPIhid")).replace("null", ""));
                map.put("GeneratedId", cur.getString(cur.getColumnIndex("generatedId")));
                map.put("LMP", cur.getString(cur.getColumnIndex("lmp")));
                map.put("EDD", cur.getString(cur.getColumnIndex("edd")));
                map.put("UN", cur.getString(cur.getColumnIndex("UN")));
                map.put("Mouza", cur.getString(cur.getColumnIndex("Mouza")));
                map.put("Vill", cur.getString(cur.getColumnIndex("Vill")));


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

                dataAdapter = new SimpleAdapter(PregRegView.this, dataList, R.layout.pregviewrow, new String[]{"SNo", "EntryDate"},
                        new int[]{R.id.Slno, R.id.EntryDate});

                list.setAdapter(new DataListAdapter(this));

                cur.moveToNext();
                totalCount += 1;
            }
            cur.close();
            *//*((TextView)findViewById(R.id.lblTCount)).setText("মোট "+ String.valueOf(slno)+ " টি ডাটা পাওয়া গিয়েছে" );*//*
            ((TextView) findViewById(R.id.lblTCount)).setText(String.valueOf(slno));

        } catch (Exception e) {
            Connection.MessageBox(PregRegView.this, e.getMessage());
        }

    }
*/
    int totalCount = 0;

   /* private void DataSearch(String stdt, String enddt) {
        try {
            String SQL = "";
            // list = (ListView)findViewById(R.id.lstData);
            list.setAdapter(null);
           *//* SQL  = "SELECT ifnull( m.SNo, '' ) AS SNo,\n" +
                    "       ifnull( m.HHNo, '' ) AS HHNo,\n" +
                    "       ifnull( el.elcoNo, '' ) AS Elcono,\n" +
                    "       ifnull( cM.healthId, '' ) AS HealthId,\n" +
                    "       ifnull( cM.generatedId, '' ) AS generatedId,\n" +
                    "       ifnull( e.pregNo, '' ) AS pregNo,\n" +
                    "       ifnull( v.VILLAGENAMEENG, '' ) AS VName,\n" +
                    "       ifnull( m.NameEng, '' ) AS WNameEng,\n" +
                    "       ifnull( m.NameBang, '' ) AS NameBang,\n" +
                    "       *//**//*CASE\n" +
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
                    "       END AS HusName,*//**//*(case when CAST ( m.SPNO1 AS int ) = 55 OR CAST ( m.SPNO1 AS int ) = 77 OR CAST ( m.SPNO1 AS int ) = 88 then m.nameeng else ifnull(hu.nameeng, '' )\n" +
                    "end) AS HusName,\n" +
                    "        (case when CAST ( m.MNo AS int ) = 55 OR CAST ( m.MNo AS int ) = 77 OR CAST ( m.MNo AS int ) = 88 then m.Mother else ifnull(mo.nameeng, '' )\n" +
                    "end) AS MotherName,\n" +
                    "(case when CAST ( m.FNo AS int ) = 55 OR CAST ( m.FNo AS int ) = 77 OR CAST ( m.FNo AS int ) = 88 then m.Father else ifnull(f.nameeng, '' ) \n" +
                    "end) AS FatherName,\n" +
                    "       ifnull( m.Age, '' ) AS WAge,\n" +
                    "       ifnull( m.MobileNo1, '' ) AS Mobno,\n" +
                    "       ifnull( m.EDU, '' ) AS Edu,\n" +
                    "       ifnull( el.Son, '' ) AS Son,\n" +
                    "       ifnull( el.Dau, '' ) AS Dau,\n" +
                    "       strftime( '%d/%m/%Y', date( el.marrDate )  ) AS MDate\n" +
                    "  FROM clientMap cm\n" +
                    "       INNER JOIN Member m\n" +
                    "               ON cM.healthId = m.healthId\n" +
                    "               left outer join Member f  on m.dist=f.dist and m.upz=f.upz and m.un=f.un and m.mouza=f.mouza and m.vill=f.vill and m.hhno=f.hhno and m.fno=f.sno\n" +
                    "left outer join Member mo on m.dist=mo.dist and m.upz=mo.upz and m.un=mo.un and m.mouza=mo.mouza and m.vill=mo.vill and m.hhno=mo.hhno and m.mno=mo.sno\n" +
                    "left outer join Member hu on m.dist=hu.dist and m.upz=hu.upz and m.un=hu.un and m.mouza=hu.mouza and m.vill=hu.vill and m.hhno=hu.hhno and m.SPNO1=hu.sno\n" +
                    "\n" +
                    "       LEFT OUTER JOIN PregWomen e\n" +
                    "                    ON e.healthId = cM.generatedId\n" +
                    "       LEFT OUTER JOIN elco el\n" +
                    "                    ON el.healthId = cM.generatedId\n" +
                    "       LEFT OUTER JOIN elcoVisit ev\n" +
                    "                    ON ev.healthId = cM.generatedId \n" +
                    "AND\n" +
                    "ev.pregNo = e.pregNo\n" +
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
                    " WHERE e.healthId = cM.generatedId \n" +
                    "       AND\n" +
                    "       ev.currStatus = '12' and strftime('%d/%m/%Y', date(e.systemEntryDate)) between'"+stdt.toString()+"' and '"+enddt.toString()+"'";

*//*
            SQL = "SELECT distinct m.Dist AS Dist,m.Upz AS Upz,m.UN AS UN,m.Mouza AS Mouza, m.Vill AS Vill,ev.currStatus,ifnull(m.SNo, '' ) AS SNo,\n" +
                    "       ifnull( m.HHNo, '' ) AS HHNo,\n" +
                    "       ifnull( el.elcoNo, '' ) AS Elcono,\n" +
                    "       ifnull( cM.healthId, '' ) AS HealthId,\n" +
                    "       ifnull( cM.generatedId, '' ) AS generatedId,\n" +
                    "       ifnull( e.pregNo, '' ) AS pregNo,\n" +
                    "       strftime( '%d/%m/%Y', date( e.LMP )  ) AS lmp,\n" +
                    "       strftime( '%d/%m/%Y', date( e.EDD )  ) AS edd,\n" +
                    "       ifnull( v.VILLAGENAMEENG, '' ) AS VName,\n" +
                    "       ifnull( m.NameEng, '' ) AS WNameEng,\n" +
                    "       ifnull( m.NameBang, '' ) AS NameBang,\n" +
                    "     (case when CAST ( m.SPNO1 AS int ) = 55 OR CAST ( m.SPNO1 AS int ) = 77 OR CAST ( m.SPNO1 AS int ) = 88 then '' else ifnull(hu.nameeng, '' )\n" +
                    "end) AS HusName,\n" +
                    "        (case when CAST ( m.MNo AS int ) = 55 OR CAST ( m.MNo AS int ) = 77 OR CAST ( m.MNo AS int ) = 88 then m.Mother else ifnull(mo.nameeng, '' )\n" +
                    "end) AS MotherName,\n" +
                    "(case when CAST ( m.FNo AS int ) = 55 OR CAST ( m.FNo AS int ) = 77 OR CAST ( m.FNo AS int ) = 88 then m.Father else ifnull(f.nameeng, '' ) \n" +
                    "end) AS FatherName,\n" +
                    "       ifnull( m.Age, '' ) AS WAge,\n" +
                    "       ifnull( m.MobileNo1, '' ) AS Mobno,\n" +
                    "       ifnull( m.EDU, '' ) AS Edu,\n" +
                    "       ifnull( el.Son, '' ) AS Son,\n" +
                    "       ifnull( el.Dau, '' ) AS Dau,\n" +
                    "       strftime( '%d/%m/%Y', date( el.marrDate )  ) AS MDate\n" +
                    "  FROM clientMap cm\n" +
                    "       INNER JOIN Member m\n" +
                    "               ON cM.healthId = m.healthId\n" +
                    "               left outer join Member f  on m.dist=f.dist and m.upz=f.upz and m.un=f.un and m.mouza=f.mouza and m.vill=f.vill and m.hhno=f.hhno and m.fno=f.sno\n" +
                    "left outer join Member mo on m.dist=mo.dist and m.upz=mo.upz and m.un=mo.un and m.mouza=mo.mouza and m.vill=mo.vill and m.hhno=mo.hhno and m.mno=mo.sno\n" +
                    "left outer join Member hu on m.dist=hu.dist and m.upz=hu.upz and m.un=hu.un and m.mouza=hu.mouza and m.vill=hu.vill and m.hhno=hu.hhno and m.SPNO1=hu.sno\n" +
                    "       LEFT OUTER JOIN PregWomen e\n" +
                    "                    ON e.healthId = cM.generatedId\n" +
                    "       LEFT OUTER JOIN elco el\n" +
                    "                    ON el.healthId = cM.generatedId\n" +
                    "       LEFT OUTER JOIN elcoVisit ev\n" +
                    "                    ON ev.healthId = cM.generatedId \n" +
                    "AND\n" +
                    "ev.pregNo = e.pregNo\n" +
                    "       LEFT OUTER JOIN delivery d\n" +
                    "                    ON d.healthId = cM.generatedId \n" +
                    "AND\n" +
                    "d.pregNo = e.pregNo\n" +
                    "       LEFT OUTER JOIN pregWomen p\n" +
                    "                    ON p.healthId = cM.generatedId \n" +
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
                    " WHERE --e.healthId = cM.generatedId \n" +
                    "       --AND\n" +
                    "       ev.currStatus = '12' and d.healthId is null and p.healthId=e.healthId\n" +
                    "         AND\n" +
                    " e.systemEntryDate between'" + stdt + "' and '" + enddt + "'";//strftime( '%d/%m/%Y',  e.systemEntryDate )
            SQL += "       Union All\n" +
                    "       SELECT distinct m.Dist AS Dist,m.Upz AS Upz,m.UN AS UN,m.Mouza AS Mouza, m.Vill AS Vill,ev.currStatus,ifnull(m.SNo, '' ) AS SNo,\n" +
                    "       ifnull( m.HHNo, '' ) AS HHNo,\n" +
                    "       ifnull( el.elcoNo, '' ) AS Elcono,\n" +
                    "       ifnull( cM.healthId, '' ) AS HealthId,\n" +
                    "       ifnull( cM.generatedId, '' ) AS generatedId,\n" +
                    "       ifnull( e.pregNo, '' ) AS pregNo,\n" +
                    "       strftime( '%d/%m/%Y', date( e.LMP )  ) AS lmp,\n" +
                    "       strftime( '%d/%m/%Y', date( e.EDD )  ) AS edd,\n" +
                    "       ifnull( v.VILLAGENAMEENG, '' ) AS VName,\n" +
                    "       ifnull( m.NameEng, '' ) AS WNameEng,\n" +
                    "       ifnull( m.NameBang, '' ) AS NameBang,\n" +
                    "     (case when CAST ( m.SPNO1 AS int ) = 55 OR CAST ( m.SPNO1 AS int ) = 77 OR CAST ( m.SPNO1 AS int ) = 88 then '' else ifnull(hu.nameeng, '' )\n" +
                    "end) AS HusName,\n" +
                    "        (case when CAST ( m.MNo AS int ) = 55 OR CAST ( m.MNo AS int ) = 77 OR CAST ( m.MNo AS int ) = 88 then m.Mother else ifnull(mo.nameeng, '' )\n" +
                    "end) AS MotherName,\n" +
                    "(case when CAST ( m.FNo AS int ) = 55 OR CAST ( m.FNo AS int ) = 77 OR CAST ( m.FNo AS int ) = 88 then m.Father else ifnull(f.nameeng, '' ) \n" +
                    "end) AS FatherName,\n" +
                    "       ifnull( m.Age, '' ) AS WAge,\n" +
                    "       ifnull( m.MobileNo1, '' ) AS Mobno,\n" +
                    "       ifnull( m.EDU, '' ) AS Edu,\n" +
                    "       ifnull( el.Son, '' ) AS Son,\n" +
                    "       ifnull( el.Dau, '' ) AS Dau,\n" +
                    "       strftime( '%d/%m/%Y', date( el.marrDate )  ) AS MDate\n" +
                    "  FROM clientMap cm\n" +
                    "       INNER JOIN Member m\n" +
                    "               ON cM.healthId = m.healthId\n" +
                    "               left outer join Member f  on m.dist=f.dist and m.upz=f.upz and m.un=f.un and m.mouza=f.mouza and m.vill=f.vill and m.hhno=f.hhno and m.fno=f.sno\n" +
                    "left outer join Member mo on m.dist=mo.dist and m.upz=mo.upz and m.un=mo.un and m.mouza=mo.mouza and m.vill=mo.vill and m.hhno=mo.hhno and m.mno=mo.sno\n" +
                    "left outer join Member hu on m.dist=hu.dist and m.upz=hu.upz and m.un=hu.un and m.mouza=hu.mouza and m.vill=hu.vill and m.hhno=hu.hhno and m.SPNO1=hu.sno\n" +
                    "       LEFT OUTER JOIN PregWomen e\n" +
                    "                    ON e.healthId = cM.generatedId\n" +
                    "       LEFT OUTER JOIN elco el\n" +
                    "                    ON el.healthId = cM.generatedId\n" +
                    "       LEFT OUTER JOIN elcoVisit ev\n" +
                    "                    ON ev.healthId = cM.generatedId \n" +
                    "AND\n" +
                    "ev.pregNo = e.pregNo\n" +
                    "       LEFT OUTER JOIN delivery d\n" +
                    "                    ON d.healthId = cM.generatedId \n" +
                    "AND\n" +
                    "d.pregNo = e.pregNo\n" +
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
                    " WHERE e.healthId = cM.generatedId \n" +
                    "       AND\n" +
                    "       ev.currStatus = '12' \n" +
                    "       AND\n" +
                    "       CAST ( ( ( julianday( date( 'now' )  ) - julianday( d.outcomeDate )  )  )  AS int ) < 42\n" +
                    "         AND\n" +
                    " e.systemEntryDate between'" + stdt + "' and '" + enddt + "'";
*//*strftime( '%d/%m/%Y',  e.systemEntryDate )*//*
           *//* SQL  = "Select ifnull(SNo,'') as SNo,ifnull(m.HHNo,'') AS HHNo,ifnull(cM.healthId,'') as HealthId,ifnull(cM.generatedId,'') as generatedId,v.VILLAGENAMEENG AS VName, ifnull(NameEng,'') as WNameEng,ifnull(NameBang,'') as NameBang,";
            SQL += " CASE WHEN cast(SPNO1 as int) = 55 THEN (select  NameEng  from member where  ProvCode=(select  ProvCode  from member Where healthid=e.healthId) and HHNo=(select  HHNo  from member  Where  healthid=e.healthId) and SNo=(select  SPNO1  from member  Where healthid=e.healthId)) WHEN cast(SPNO1 as int) = 77 THEN (select  NameEng  from member where  ProvCode=(select  ProvCode  from member Where  healthid=e.healthId) and HHNo=(select  HHNo  from member  Where  healthid=e.healthId) and SNo=(select  SPNO1  from member Where healthid=e.healthId)) WHEN cast(SPNO1 as int) = 88 THEN (select  NameEng  from member where  ProvCode=(select  ProvCode  from member Where  healthid=e.healthId) and HHNo=(select  HHNo  from member  Where healthid=e.healthId) and SNo=(select  SPNO1  from member Where  healthid=e.healthId)) ELSE (select  NameEng  from member where  ProvCode=(select  ProvCode  from member  Where healthid=e.healthId) and HHNo=(select  HHNo  from member  Where  healthid=e.healthId) and SNo=(select  SPNO1  from member  Where  healthid=e.healthId)) END AS  HusName,";
            SQL += "ifnull(m.Age,'') as WAge,ifnull(m.MobileNo1,'') as Mobno,ifnull(EDU,'') AS Edu from  clientMap cm inner join Member m on cM.healthId=m.healthId  left outer join PregWomen e on e.healthId=cM.generatedId  inner join Village v on v.ZILLAID=m.Dist and v.UPAZILAID=m.Upz and v.UNIONID=m.UN and v.MOUZAID=m.Mouza and v.VILLAGEID=m.Vill where e.providerId='"+ g.getProvCode()+"' and strftime('%d/%m/%Y', date(e.systemEntryDate)) between'"+stdt.toString()+"' and '"+enddt.toString()+"'";
*//*
           *//* SQL  = "Select ifnull(SNo,'') as SNo,ifnull(m.HHNo,'') AS HHNo,ifnull(e.healthId,'') as HealthId,v.VILLAGENAMEENG AS VName, ifnull(NameEng,'') as WNameEng,ifnull(NameBang,'') as NameBang,";
            SQL += " CASE WHEN cast(SPNO1 as int) = 55 THEN (select  NameEng  from member where  ProvCode=(select  ProvCode  from member Where healthid=e.healthId) and HHNo=(select  HHNo  from member  Where  healthid=e.healthId) and SNo=(select  SPNO1  from member  Where healthid=e.healthId)) WHEN cast(SPNO1 as int) = 77 THEN (select  NameEng  from member where  ProvCode=(select  ProvCode  from member Where  healthid=e.healthId) and HHNo=(select  HHNo  from member  Where  healthid=e.healthId) and SNo=(select  SPNO1  from member Where healthid=e.healthId)) WHEN cast(SPNO1 as int) = 88 THEN (select  NameEng  from member where  ProvCode=(select  ProvCode  from member Where  healthid=e.healthId) and HHNo=(select  HHNo  from member  Where healthid=e.healthId) and SNo=(select  SPNO1  from member Where  healthid=e.healthId)) ELSE (select  NameEng  from member where  ProvCode=(select  ProvCode  from member  Where healthid=e.healthId) and HHNo=(select  HHNo  from member  Where  healthid=e.healthId) and SNo=(select  SPNO1  from member  Where  healthid=e.healthId)) END AS  HusName,";
            SQL += "ifnull(m.Age,'') as WAge,ifnull(m.MobileNo1,'') as Mobno,ifnull(EDU,'') AS Edu from Member m left outer join PregWomen e on e.healthId=m.healthId inner join Village v on v.ZILLAID=m.Dist and v.UPAZILAID=m.Upz and v.UNIONID=m.UN and v.MOUZAID=m.Mouza and  v.VILLAGEID=m.Vill  where e.providerId='"+ g.getProvCode()+"' and strftime('%d/%m/%Y', date(e.systemEntryDate)) between'"+dtfrom.toString()+"' and '"+dtto.toString()+"'";
*//*
            *//*SQL  = "Select ifnull(SNo,'') as SNo,ifnull(m.HHNo,'') AS HHNo,ifnull(e.elcoNo,'') as Elcono,ifnull(e.healthId,'') as HealthId,v.VILLAGENAMEENG AS VName,";
            SQL += " ifnull(NameEng,'') as WNameEng,ifnull(NameBang,'') as NameBang,";
            SQL += " CASE WHEN cast(SPNO1 as int) = 55 THEN (select  NameEng  from member where  ProvCode=(select  ProvCode  from member Where healthid=e.healthId) and HHNo=(select  HHNo  from member  Where  healthid=e.healthId) and SNo=(select  SPNO1  from member  Where healthid=e.healthId)) WHEN cast(SPNO1 as int) = 77 THEN (select  NameEng  from member where  ProvCode=(select  ProvCode  from member Where  healthid=e.healthId) and HHNo=(select  HHNo  from member  Where  healthid=e.healthId) and SNo=(select  SPNO1  from member Where healthid=e.healthId)) WHEN cast(SPNO1 as int) = 88 THEN (select  NameEng  from member where  ProvCode=(select  ProvCode  from member Where  healthid=e.healthId) and HHNo=(select  HHNo  from member  Where healthid=e.healthId) and SNo=(select  SPNO1  from member Where  healthid=e.healthId)) ELSE (select  NameEng  from member where  ProvCode=(select  ProvCode  from member  Where healthid=e.healthId) and HHNo=(select  HHNo  from member  Where  healthid=e.healthId) and SNo=(select  SPNO1  from member  Where  healthid=e.healthId)) END AS  HusName,";
            SQL += " ifnull(m.Age,'') as WAge,ifnull(e.marrDate,'') as MDate,ifnull(m.MobileNo1,'') as Mobno,ifnull(e.son,'') as Son,ifnull(e.dau,'') as Dau,ifnull(EDU,'') AS Edu from Member m  left outer join elco e on e.healthId=m.healthId inner join Village v on v.ZILLAID=m.Dist and v.UPAZILAID=m.Upz and v.UNIONID=m.UN and v.MOUZAID=m.Mouza and v.VILLAGEID=m.Vill where e.providerId='"+ g.getProvCode() +"' and strftime('%d/%m/%Y', date(e.regDT)) between'"+dtfrom.toString()+"' and '"+dtto.toString()+"'";*//*
            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();
            dataList.clear();
            int slno = 0;
            while (!cur.isAfterLast()) {
                slno += 1;
                HashMap<String, String> map = new HashMap<String, String>();
                // ListView list = (ListView)findViewById(R.id.lstData);
           *//*     map.put("slno", Integer.toString(slno));
                map.put("hhho", cur.getString(cur.getColumnIndex("HHNo")));
                map.put("healthId", cur.getString(cur.getColumnIndex("HealthId")));
                map.put("elcono", cur.getString(cur.getColumnIndex("Elcono")).replace("null", ""));
                map.put("vname", cur.getString(cur.getColumnIndex("VName")));
                map.put("wname", cur.getString(cur.getColumnIndex("WNameEng")).replace("null", ""));
                map.put("hname", cur.getString(cur.getColumnIndex("HusName")).replace("null", ""));
                map.put("wage", cur.getString(cur.getColumnIndex("WAge")).replace("null", ""));
                map.put("mdate", cur.getString(cur.getColumnIndex("MDate")).replace("null", ""));
                map.put("mobno", cur.getString(cur.getColumnIndex("Mobno")).replace("null", ""));
                map.put("son", cur.getString(cur.getColumnIndex("Son")).replace("null", ""));
                map.put("dau", cur.getString(cur.getColumnIndex("Dau")).replace("null", ""));
                map.put("edu", cur.getString(cur.getColumnIndex("Edu")).replace("null", ""));*//*

                map.put("slno", Integer.toString(slno));
                map.put("hhho", cur.getString(cur.getColumnIndex("HHNo")));
                map.put("healthId", cur.getString(cur.getColumnIndex("HealthId")));
                map.put("elcono", cur.getString(cur.getColumnIndex("Elcono")).replace("null", ""));
                map.put("vname", cur.getString(cur.getColumnIndex("VName")));
                map.put("wname", cur.getString(cur.getColumnIndex("WNameEng")));
                map.put("hname", cur.getString(cur.getColumnIndex("HusName")));
                map.put("wage", cur.getString(cur.getColumnIndex("WAge")));
                map.put("mdate", cur.getString(cur.getColumnIndex("MDate")));
                map.put("mobno", cur.getString(cur.getColumnIndex("Mobno")).replace("null", ""));
                map.put("son", cur.getString(cur.getColumnIndex("Son")).replace("null", ""));
                map.put("dau", cur.getString(cur.getColumnIndex("Dau")).replace("null", ""));
                map.put("edu", cur.getString(cur.getColumnIndex("Edu")).replace("null", ""));
                map.put("eFPIhid", cur.getString(cur.getColumnIndex("eFPIhid")).replace("null", ""));
                map.put("GeneratedId", cur.getString(cur.getColumnIndex("generatedId")));
                map.put("pregNo", cur.getString(cur.getColumnIndex("pregNo")));
                map.put("LMP", cur.getString(cur.getColumnIndex("lmp")));
                map.put("EDD", cur.getString(cur.getColumnIndex("edd")));

                map.put("UN", cur.getString(cur.getColumnIndex("UN")));
                map.put("Mouza", cur.getString(cur.getColumnIndex("Mouza")));
                map.put("Vill", cur.getString(cur.getColumnIndex("Vill")));

              *//*  if (cur.getString(cur.getColumnIndex("Edu")).equalsIgnoreCase("99")) {
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
                }*//*


                dataList.add(map);

                dataAdapter = new SimpleAdapter(PregRegView.this, dataList, R.layout.pregviewrow, new String[]{"SNo"},
                        new int[]{R.id.Slno});

                list.setAdapter(new DataListAdapter(this));

                cur.moveToNext();
                totalCount += 1;
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(PregRegView.this, e.getMessage());
            return;
        }
    }
*/


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
                convertView = inflater.inflate(R.layout.pregviewrow, null);
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
            MDate.setText(o.get("mdate"));
            Mobno.setText(o.get("mobno"));
            Son.setText(o.get("son"));
            Dau.setText(o.get("dau"));
            Edu.setText(o.get("edu"));

          /*  if (o.get("eFPIhid").equals("Y")) {


                memtab.setBackgroundColor(Color.parseColor("#99cc33"));
            }
            if (o.get("eFPIhid").equals("")) {
                memtab.setBackgroundColor(Color.parseColor("#FFFFFF"));
                // memtabrow.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }*/

            memtab.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    g.setCallFrom("1");
                    g.setSLno(o.get("slno"));
                    g.setAVDate(o.get("VDate"));
                    g.setAHHNO(o.get("hhho"));
                    g.setHealthID(o.get("healthId"));
                    g.setGeneratedId(o.get("GeneratedId"));
                    g.setAName(o.get("name"));
                    g.setASex(o.get("sex"));
                    g.setAAge(o.get("age"));
                    g.setPregNo(o.get("pregNo"));
                    //g.setAMElco(g.GetElcoNo(C, o.get("GeneratedId")));
                    //g.setAMElco(C.ReturnSingleValue("select elcoNo from elco where healthId ='"+o.get("GeneratedId")+"'"));
                    finish();
                    Intent f1 = new Intent(getApplicationContext(), FWAReg.class);
                    startActivity(f1);


                }
            });

            final AlertDialog.Builder adb = new AlertDialog.Builder(PregRegView.this);
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

