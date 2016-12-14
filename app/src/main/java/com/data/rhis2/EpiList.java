package com.data.rhis2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import Common.Connection;
import Common.Global;

public class EpiList extends Activity {
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mnuclose, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder adb = new AlertDialog.Builder(EpiList.this);
        switch (item.getItemId()) {
            case R.id.menuClose:
                adb.setTitle("Close");
                adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান?");
                adb.setNegativeButton("না", null);
                adb.setPositiveButton("হ্যাঁ", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        finish();
                        /*Intent f1 = new Intent(getApplicationContext(),EpiList.class);
                        startActivity(f1);*/
                    }
                });
                adb.show();
                return true;

        }
        return false;
    }


    ImageButton btnVDate;
    EditText VisitDate;
    String VariableID;
    public String dateSet = "";
    private int hour;
    private int minute;
    private int mDay;
    private int mMonth;
    private int mYear;
    static final int DATE_DIALOG = 1;
    static final int DATE_DIALOG1 = 3;
    static final int TIME_DIALOG = 2;
    Calendar c = Calendar.getInstance();

    SimpleAdapter mSchedule;
    SimpleAdapter eList;

    ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> evmylist = new ArrayList<HashMap<String, String>>();
    Connection C;
    Global g;
    private static final int UPDATEDONE = 900;
    private ProgressDialog pDialog;
    private static String vill;
    private static String bari;
    private static String hhno;
    private static String hhhead;
    private static String totalmember;
    private String ErrMsg;

    private String EB;
    private static String vdate;
    private String TableName;
    private String TableName1;
    // private String schedulerId;
    Spinner spnEPIBlock;
    TextView lblTCount;
    EditText txtSearch;
    ListView list;
    //TextView txtdate;
    //TextView txtCenterName;
    TextView VlblSH3;
    TextView VlblSH4;
    //TextView txtEPIBlock0;
    //  TextView lblTCount;
/*    TextView txtHHNo;
    RadioGroup rdogrpPAddr;
    RadioButton rdoPAddr1;
    RadioButton rdoPAddr2;
    EditText txtPermaAddress;


    RadioGroup rdogrpVGFCard;
    RadioButton rdoVGFCard1;
    RadioButton rdoVGFCard2;

    LinearLayout secPAddr;
    LinearLayout secPermaAddress;
    LinearLayout secReligion;


    TextView FPMethod;
    TextView LMP;
    TextView EDD;*/
    String StartTime;


    Location currentLocation;
    double currentLatitude, currentLongitude;
    Bundle IDbundle = new Bundle();
    Context con;


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

    //***************************************************************************************************************************
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.epilist);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();
            con = this;

            TableName = "epiMaster";
            TableName1 = "epiScheduler";

            // schedulerId="01";
            //   EB=C.ReturnSingleValue("Select count(*)from Member m LEFT JOIN Household h ON h.Dist= m.Dist and h.Upz= m.Upz  and h.UN= m.UN and h.Mouza= m.Mouza  and h.Vill= m.Vill inner join HABlock b on b.BCode=h.EPIBlock LEFT JOIN epiMaster e ON e.healthId = m.healthid WHERE Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) <=18");
            lblTCount = (TextView) findViewById(R.id.lblTCount);
            VlblSH3 = (TextView) findViewById(R.id.VlblSH3);
            VlblSH4 = (TextView) findViewById(R.id.VlblSH4);
            VlblSH3.setBackgroundColor(Color.parseColor("#99cc33"));
            VlblSH4.setBackgroundColor(Color.parseColor("#F0F0F0"));
            //  lblNoRegColor.setText( lblNoRegColor.getText()+"কালার নাই");
            spnEPIBlock = (Spinner) findViewById(R.id.spnEPIBlock);
            // txtEPIBlock0=(TextView) findViewById(R.id.txtEPIBlock0);

         /*  if(g.getepiCallForm().equals("1"))
            {
                //txtEPIBlock0.setText(g.getepiSubBlockId());
               if (g.getEPIBlock().equals("1")) {
                    spnEPIBlock.setSelection(1);
                } else if (g.getEPIBlock().equals("2")) {
                    spnEPIBlock.setSelection(2);
                } else if (g.getEPIBlock().equals("3")) {
                    spnEPIBlock.setSelection(3);
                } else if (g.getEPIBlock().equals("4")) {
                    spnEPIBlock.setSelection(4);
                } else if (g.getEPIBlock().equals("5")) {
                    spnEPIBlock.setSelection(5);
                } else if (g.getEPIBlock().equals("6")) {
                    spnEPIBlock.setSelection(6);
                } else if (g.getEPIBlock().equals("7")) {
                    spnEPIBlock.setSelection(7);
                } else if (g.getEPIBlock().equals("8")) {
                    spnEPIBlock.setSelection(8);
                } else {
                    spnEPIBlock.setSelection(0);
                }
            }
            else  if(g.getepiCallForm().equals("2"))
            {

            }*/
            //txtdate=(TextView) findViewById(R.id.txtdate);
            txtSearch = (EditText) findViewById(R.id.txtSearch);
            list = (ListView) findViewById(R.id.listepiIndex);
            //txtCenterName=(TextView) findViewById(R.id.txtCenterName);
            final Button cmdSchedul = (Button) findViewById(R.id.cmdSchedul);
            // final   Button cmdEpiReg  = (Button) findViewById(R.id.cmdEpiReg);
            //final  Button cmdEpisec  = (Button) findViewById(R.id.cmdEpisec);
            final ImageButton cmdSearch = (ImageButton) findViewById(R.id.cmdSearch);
            cmdSearch.setBackgroundResource(R.drawable.search);
            //cmdEpiReg.setEnabled(false);
            //cmdEpisec.setEnabled(false);
            // txtSearch.setEnabled(false);
            //cmdSearch.setEnabled(false);
   /*         txtdate.addTextChangedListener(new TextWatcher(){
                public void afterTextChanged(Editable s) {
                    if(s.toString().length()>0)
                    {
                        String val = spnEPIBlock.getSelectedItemPosition() == 0 ? "" : Global.Left(spnEPIBlock.getSelectedItem().toString(), 2);
                        if(val.length()>0)
                        {
                            txtCenterName.setText("");
                            DataSearch(g.getepiScheduleDate(),g.getEPIBlock());
                           //DataSearch(txtdate.getText().toString(), spnEPIBlock.getSelectedItemPosition());
                            if(txtCenterName.length()==0)
                            {
                                cmdEpiReg.setEnabled(false);
                                cmdEpisec.setEnabled(false);
                               // txtSearch.setEnabled(false);
                               // cmdSearch.setEnabled(false);
                            }

                            else if(txtCenterName.length()>0)
                            {
                                cmdEpiReg.setEnabled(true);
                                cmdEpisec.setEnabled(true);
                               // txtSearch.setEnabled(true);
                               // cmdSearch.setEnabled(true);
                            }
                        }
                    }
                }
                public void beforeTextChanged(CharSequence s, int start, int count, int after){}
                public void onTextChanged(CharSequence s, int start, int before, int count){

                }
            });
*/

            /*if(txtCenterName.length()==0)
            {
                cmdEpiReg.setEnabled(false);
                cmdEpisec.setEnabled(false);
                // txtSearch.setEnabled(false);
                // cmdSearch.setEnabled(false);
            }

            else if(txtCenterName.length()>0)
            {
                cmdEpiReg.setEnabled(true);
                cmdEpisec.setEnabled(true);
                // txtSearch.setEnabled(true);
                // cmdSearch.setEnabled(true);
            }*/
            spnEPIBlock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    String val = spnEPIBlock.getSelectedItemPosition() == 0 ? "" : Global.Left(spnEPIBlock.getSelectedItem().toString(), 2);
                    if (val.length() > 0) {
                      /*  if(txtdate.length()>0)
                        {
                            txtCenterName.setText("");
                            //DataSearch(g.getepiScheduleDate(),g.getepiSubBlockId());
                            if(txtCenterName.length()==0)
                            {
                                cmdEpiReg.setEnabled(false);
                                cmdEpisec.setEnabled(false);
                               // txtSearch.setEnabled(false);
                               // cmdSearch.setEnabled(false);
                            }

                            else if(txtCenterName.length()>0)
                            {
                                cmdEpiReg.setEnabled(true);
                                cmdEpisec.setEnabled(true);
                               // txtSearch.setEnabled(true);
                               // cmdSearch.setEnabled(true);
                            }
                        }*/


                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            //DataSearch();
       /*     ((ImageButton)findViewById(R.id.btndate)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btndate";
                    showDialog(DATE_DIALOG1);
                }
            });*/


           /* cmdEpiReg.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    // DisplayEpiReg();
                    Intent f1 = new Intent(getApplicationContext(), EpiRegistration.class);
                    startActivity(f1);
                }
            });*/


        /*    cmdEpisec.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    // DisplayEpiReg();
                    Intent f1 = new Intent(getApplicationContext(), episession.class);
                    startActivity(f1);
                }
            })*/
            ;

            cmdSchedul.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Intger.valueOf("");

                    //  g.setepiSubBlockId(String.valueOf(spnEPIBlock.getSelectedItemPosition()));
                    g.setepiSubBlockId(Global.Mid(spnEPIBlock.getSelectedItem().toString(), 1, 2));
                    Intent f1 = new Intent(getApplicationContext(), epischdul.class);
                    startActivity(f1);
                    // DisplayEpiSchedul();

                }
            });
            //HHDataSearch(g.getDistrict(), g.getUpazila(), g.getUnion(), g.getMouza(), g.getVillage(), txtHHNo.getText().toString());
           /* Button cmdSearch  = (Button) findViewById(R.id.cmdSearch);
            cmdSearch.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    DataRetrieve("", list, "active","");
                    //Intent f1 = new Intent(getApplicationContext(), EpiRegistration.class);
                    //startActivity(f1);
                }
            });*/

            LoadDataMemberList();

        } catch (Exception ex) {
            Connection.MessageBox(EpiList.this, ex.getMessage());
            return;
        }
    }


    /*private void DataSearch(String SDate,String EBlock)
    {
        try
        {


            Cursor cur = C.ReadData("Select * from epiScheduler Where strftime('%d/%m/%Y', date(scheduleDate))='"+SDate+ "'and subBlockId='" + EBlock+ "'");
            cur.moveToFirst();
            while(!cur.isAfterLast())
            {
                spnEPIBlock.setSelection(Global.SpinnerItemPosition(spnEPIBlock,2 ,"0"+cur.getString(cur.getColumnIndex("subBlockId"))));
                txtdate.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("scheduleDate"))));
                txtCenterName.setText(cur.getString(cur.getColumnIndex("centerName")));
               // g.setepiSchedulerId(cur.getString(cur.getColumnIndex("schedulerId")));


                // txtregisNo.setText(cur.getString(cur.getColumnIndex("regisNo")));


                cur.moveToNext();
            }
            cur.close();
        }
        catch(Exception  e)
        {
            Connection.MessageBox(EpiList.this, e.getMessage());
            return;
        }
    }*/


    private void DisplayEpiReg() {
        final Dialog popupView = new Dialog(EpiList.this);
        popupView.setTitle("EPI Registration");

        popupView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popupView.setContentView(R.layout.epireg);
        popupView.setCancelable(true);
        popupView.setCanceledOnTouchOutside(true);


        final EditText txtregisNo = (EditText) popupView.findViewById(R.id.txtregisNo);
        //final EditText dtpregDate = (EditText) popupView.findViewById(R.id.dtpregDate);

        //  dtpregDate.setEnabled(true);
        // final ImageButton btnregDate = (ImageButton) popupView.findViewById(R.id.btnregDate);
        final TextView txtName = (TextView) popupView.findViewById(R.id.txtName);
        final TextView txtsex = (TextView) popupView.findViewById(R.id.txtsex);
        final TextView txtmName = (TextView) popupView.findViewById(R.id.txtmName);
        final TextView txtfName = (TextView) popupView.findViewById(R.id.txtfName);
        final TextView txtDoB = (TextView) popupView.findViewById(R.id.txtDoB);

        final LinearLayout secregisNo = (LinearLayout) popupView.findViewById(R.id.secregisNo);
        final LinearLayout secregDate = (LinearLayout) popupView.findViewById(R.id.secregDate);
        // int id;
        popupView.show();


        VisitDate = (EditText) popupView.findViewById(R.id.dtpregDate);
        // VisitDate.setText(Global.DateNowDMY());
        btnVDate = (ImageButton) popupView.findViewById(R.id.btnregDate);
        btnVDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG);
            }
        });
        txtName.setText(g.getepiName());

        if (g.getepiSex().equals("1")) {
            txtsex.setText("ছেলে");
        } else if (g.getepiSex().equals("2")) {
            txtsex.setText("মেয়ে");
        }

        txtmName.setText(g.getepimName());
        txtfName.setText(g.getepifName());
        txtDoB.setText(g.getDOB());

        String SQL = "Select * from epiMaster Where healthId='" + g.getHealthID() + "'";
        if (C.Existence(SQL)) {
            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();

            while (!cur.isAfterLast()) {
                txtregisNo.setText(cur.getString(cur.getColumnIndex("regNo")));
                //txtregisNo.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("regNo"))));

                VisitDate.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("regDate"))));

                cur.moveToNext();
            }
            cur.close();
        } else {
            Button cmdSave = (Button) popupView.findViewById(R.id.cmdSave);
            cmdSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    TableName = "epiMaster";
                    String SQL = "";
                    //String DV = "";
                    //DV = Global.DateValidate(VisitDate.getText().toString());
                    // DV1 = Global.DateValidate(dtpdob.getText().toString());
                    if (txtregisNo.getText().toString().length() == 0 & secregisNo.isShown()) {
                        Connection.MessageBox(EpiList.this, "Required field:Registration no..");
                        txtregisNo.requestFocus();
                        return;
                    }

                    if (VisitDate.getText().toString().length() == 0 & secregDate.isShown()) {
                        Connection.MessageBox(EpiList.this, "Required field:Registration Date..");
                        VisitDate.requestFocus();
                        return;
                    }

                    if (!C.Existence("Select schedulerId,healthId,providerId,upload from " + TableName + "  Where schedulerId='" + g.getepiSchedulerId() + "' and healthId='" + g.getHealthID() + "'and providerId='" + g.getProvCode() + "'")) {
                        SQL = "Insert into " + TableName + "(schedulerId,healthId,providerId,upload)Values('" + g.getepiSchedulerId() + "', '" + g.getHealthID() + "', '" + g.getProvCode() + "','2')";
                        C.Save(SQL);
                    }


                    SQL = "Update " + TableName + " Set ";
                    // SQL+="schedulerId= '"+schedulerId+"',";
                    //SQL += "healthId = '" + g.getHealthID() + "',";
                    // SQL += "providerId = '" + g.getProvCode() + "',";
                    SQL += "houseNo='',";
                    SQL += "regNo = '" + txtregisNo.getText().toString() + "',";
                    SQL += "regDate = '" + Global.DateConvertYMD(VisitDate.getText().toString()) + "',";
                    SQL += "brCertificateNo ='',";
                    SQL += "brDate ='',";
                    SQL += "remarks ='',";
                    SQL += "systemEntryDate = '" + Global.DateTimeNowYMDHMS() + "',";
                    SQL += "upload ='2'";
                    SQL += "  Where schedulerId='" + g.getepiSchedulerId() + "' and healthId='" + g.getHealthID() + "'and providerId='" + g.getProvCode() + "'";
                    // SQL+=" where Cluster='"+ cluster +"' and Block='"+ block +"' and HH='"+ hh +"' and StudyID='"+ txtStudyId.getText() +"'and Visit='"+ txtVisitNo.getText()+"'";
                    C.Save(SQL);
                    Connection.MessageBox(EpiList.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
                    finish();
                    Intent f1 = new Intent(getApplicationContext(), EpiList.class);
                    startActivity(f1);

                }
            });


        }
        Button cmdClose = (Button) popupView.findViewById(R.id.cmdClose);
        cmdClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                popupView.cancel();
            }
        });
        /*Button cmdEPICard = (Button) popupView.findViewById(R.id.cmdEPICard);
        cmdEPICard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                //popupView.cancel();
                Intent f1 = new Intent(getApplicationContext(),EpiList.class);
                startActivity(f1);
            }
        });*/
    }


    private void DataSave() {
        try {


          /*  String DV="";
            String DV1="";

            DV = Global.DateValidate(dtpregDate.getText().toString());
            DV1 = Global.DateValidate(dtpdob.getText().toString());
            if(txtregisNo.getText().toString().length()==0 & secregisNo.isShown())
            {
                Connection.MessageBox(EpiRegistration.this, "Required field:Registration no..");
                txtregisNo.requestFocus();
                return;
            }

            if(DV.length()!=0 & secregDate.isShown())
            {
                Connection.MessageBox(EpiRegistration.this, DV);
                dtpregDate.requestFocus();
                return;
            }


            if(DV.length()!=0 & secdob.isShown())
            {
                Connection.MessageBox(EpiRegistration.this, DV);
                dtpdob.requestFocus();
                return;
            }
*/

            String SQL = "";

            if (!C.Existence("Select schedulerId,healthId,providerId,upload from " + TableName + "  Where schedulerId='" + g.getepiSchedulerId() + "' and healthId='" + g.getHealthID() + "'and providerId='" + g.getProvCode() + "'")) {
                SQL = "Insert into " + TableName + "(schedulerId,healthId,providerId,upload)Values('" + g.getepiSchedulerId() + "', '" + g.getHealthID() + "', '" + g.getProvCode() + "','2')";
                C.Save(SQL);
            }

            SQL = "Update " + TableName + " Set ";
            SQL += "schedulerId= '" + g.getepiSchedulerId() + "',";
            SQL += "healthId = '" + g.getHealthID() + "',";
            SQL += "providerId = '" + g.getProvCode() + "',";
            SQL += "houseNo='',";
            // SQL+="regDate = '"+ Global.DateConvertYMD(dtpregDate.getText().toString()) +"',";
            SQL += "brCertificateNo ='',";
            SQL += "brDate ='',";
            SQL += "remarks ='',";
            SQL += "systemEntryDate = '" + Global.DateTimeNowYMDHMS() + "',";
            SQL += "upload ='2'";
            SQL += "  Where schedulerId='" + g.getepiSchedulerId() + "' and healthId='" + g.getHealthID() + "'and providerId='" + g.getProvCode() + "'";
            // SQL+=" where Cluster='"+ cluster +"' and Block='"+ block +"' and HH='"+ hh +"' and StudyID='"+ txtStudyId.getText() +"'and Visit='"+ txtVisitNo.getText()+"'";
            C.Save(SQL);
            Connection.MessageBox(EpiList.this, "তথ্য সফলভাবে সংরক্ষণ হয়েছে।");
            finish();
        } catch (Exception e) {
            Connection.MessageBox(EpiList.this, e.getMessage());
            return;
        }
    }

    private void LoadDataMemberList() {
        try {

            FindLocation();

            StartTime = g.CurrentTime24();


            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Activity.KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
            lock.disableKeyguard();

            final ListView list = (ListView) findViewById(R.id.listepiIndex);
            //final   Button cmdEpiReg  = (Button) findViewById(R.id.cmdEpiReg);
            // final  Button cmdEpisec  = (Button) findViewById(R.id.cmdEpisec);
            //  if(heading ==true)
            // {
            View header = getLayoutInflater().inflate(R.layout.epilistheading, null);
            list.addHeaderView(header);
            // }


            //   if(g.getepiCallForm().equals("1")) {
            //txtdate.setText(g.getepiScheduleDate());
            //txtCenterName.setText(g.getepiCenterName());
            //  cmdEpiReg.setEnabled(true);
            // cmdEpisec.setEnabled(true);
            //DataSearch(g.getepiScheduleDate(), g.getepiSubBlockId());
            //    spnEPIBlock.setAdapter(C.getArrayAdapter("Select BCode||'-'||BNameBan as BCode from HABlock where BCode='"+"0"+g.getepiSubBlockId() +"'order by BCode asc"));
               /* if (g.getepiSubBlockId().equals("1")) {
                    spnEPIBlock.setSelection(1);
                } else if (g.getepiSubBlockId().equals("2")) {
                    spnEPIBlock.setSelection(2);
                } else if (g.getepiSubBlockId().equals("3")) {
                    spnEPIBlock.setSelection(3);
                } else if (g.getepiSubBlockId().equals("4")) {
                    spnEPIBlock.setSelection(4);
                } else if (g.getepiSubBlockId().equals("5")) {
                    spnEPIBlock.setSelection(5);
                } else if (g.getepiSubBlockId().equals("6")) {
                    spnEPIBlock.setSelection(6);
                } else if (g.getepiSubBlockId().equals("7")) {
                    spnEPIBlock.setSelection(7);
                } else if (g.getepiSubBlockId().equals("8")) {
                    spnEPIBlock.setSelection(8);
                } else {
                    spnEPIBlock.setSelection(0);
                }*/
            //    }

            //  else  if(g.getepiCallForm().equals("2"))
            // {

            //txtCenterName.setText("");
            //txtdate.setText("");
            // cmdEpiReg.setEnabled(false);
            // cmdEpisec.setEnabled(false);
            //  }

            spnEPIBlock.setAdapter(C.getArrayAdapter("Select ' সকল সাব ব্লক'as BCode union Select BCode||'-'||BNameBan as BCode from HABlock order by BCode asc"));


            spnEPIBlock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    String val = spnEPIBlock.getSelectedItemPosition() == 0 ? "" : Global.Left(spnEPIBlock.getSelectedItem().toString(), 2);
                    if (val.length() >= 0) {
                        totalCount = 0;
                        //
                        //DataRetrieve("", list, "active", val);
                        mylist.clear();
                        DataRetrieve("", list, "active", Global.Left(spnEPIBlock.getSelectedItem().toString(), 2), txtSearch.getText().toString());
                        lblTCount.setText("(সর্ব মোট:" + String.valueOf(totalCount) + ")");
                        //lblTCount.setText(EB);
                        //  lblTCount.setText(GetTotalTotalBlock(Global.Left(spnEPIBlock.getSelectedItem().toString(), 2)));

                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            // ImageButton cmdSearch  = (ImageButton) findViewById(R.id.cmdSearch);
            final ImageButton cmdSearch = (ImageButton) findViewById(R.id.cmdSearch);
            cmdSearch.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    totalCount = 0;
                    if (txtSearch.length() == 0) {
                        Connection.MessageBox(EpiList.this, "Required field:Search..");
                        txtSearch.requestFocus();
                        return;
                    }
                    DataRetrieve("", list, "active", Global.Left(spnEPIBlock.getSelectedItem().toString(), 2), txtSearch.getText().toString());
                    lblTCount.setText("(সর্ব মোট:" + String.valueOf(totalCount) + ")");

                    //Intent f1 = new Intent(getApplicationContext(), EpiRegistration.class);
                    //startActivity(f1);
                }
            });


            DataRetrieve("", list, "active", Global.Left(spnEPIBlock.getSelectedItem().toString(), 2), "");

        } catch (Exception ex) {
            Connection.MessageBox(EpiList.this, ex.getMessage());
            return;
        }
    }


    //Retrieve member list
    //***************************************************************************************************************************
    //public void DataRetrieve(String HH, ListView list, String ActiveOrAll, String EPIBlock)
    int totalCount = 0;


    public void DataRetrieve(String HH, ListView list, String ActiveOrAll, String EPIBlock, String healthId) {
        try {
            String SQLStr = "";


// :EPIBlock Search

            if (spnEPIBlock.getSelectedItemPosition() == 0 & txtSearch.length() == 0) {
                   /*
                        SQLStr = "Select epimaster.healthId As epiHealthId,epimaster.regNo AS regNo, (case when epimaster.regNo is not null then '2' else '1' end) as regC, strftime('%d/%m/%Y', date(epimaster.regDate)) AS regDate,epimaster.houseNo AS houseNo, epiMaster.brCertificateNo AS brCertificateNo," +
                        " strftime('%d/%m/%Y', date(ExDate)) AS ExDate, " +
                        " Dist, Upz, UN, Mouza, Vill, (select VILLAGENAME FROM Village WHERE VILLAGEID IN(SELECT Vill FROM Member)) VillageName, ProvType as provtype,ProvCode as provcode, HHNo, SNo as SNo, " +
                        " ifnull(member.HealthID,'') as HealthID, ifnull(NameEng,'') as NameEng," +
                        " ifnull(NameBang,'') as NameBang, ifnull(Rth,'') as Rth, ifnull(HaveNID,'') as HaveNID, ifnull(NID,'') as NID, " +
                        " ifnull(NIDStatus,'') as NIDStatus, ifnull(HaveBR,'') as HaveBR, ifnull(BRID,'') as BRID," +
                        "  ifnull(BRIDStatus,'') as BRIDStatus, ifnull(MobileNo1,'') as MobileNo1," +
                        " ifnull(MobileNo2,'') as MobileNo2,ifnull(MobileYN,'')as MobileYN,strftime('%d/%m/%Y', date(ifnull(DOB,''))) AS DOB, " +
                        " Cast(((julianday(date('now'))-julianday(DOB))/365.25) as int) as Age," +
                        " Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) as AgeM, ifnull(DOBSource,'') as DOBSource, " +
                        " ifnull(BPlace,'') as BPlace, ifnull(FNo,'') as FNo, ifnull(MNo,'') as MNo, " +
                        " ifnull(Sex,'') as Sex, ifnull(MS,'') as MS, ifnull(SPNO1,'') as SPNO1,ifnull(SPNO2,'') as SPNO2," +
                        " ifnull(SPNO3,'') as SPNO3,ifnull(SPNO4,'') as SPNO4, ifnull(ELCONo,'') as ELCONo, " +
                        " ifnull(ELCODontKnow,'') as ELCODontKnow, ifnull(EDU,'') as EDU, ifnull(Rel,'') as Rel, " +
                        " ifnull(Nationality,'') as Nationality, ifnull(OCP,'') as OCP," +
                        " ifnull(ExType,'')as ExType " +
                        " from Member " +
                        " LEFT JOIN epiMaster epimaster ON epimaster.healthId = Member.healthid" +
                        " WHERE Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) <=18";
                        */

               /* SQLStr = "Select ifnull(member.HealthID,'') as HealthID, ifnull(NameEng,'') as NameEng,ifnull(NameBang,'') as NameBang,ifnull(Sex,'') as Sex,strftime('%d/%m/%Y', date(ifnull(DOB,''))) AS DOB,epimaster.regNo AS regNo, (case when epimaster.regNo is not null then '2' else '1' end) as regC, strftime('%d/%m/%Y', date(epimaster.regDate)) AS regDate, " +
                        " Dist, Upz, UN, Mouza, Vill, (select VILLAGENAME FROM Village WHERE VILLAGEID IN(SELECT Vill FROM Member)) VillageName,ProvCode as provcode, HHNo, SNo as SNo,strftime('%d/%m/%Y', date(ExDate)) AS ExDate,ifnull(MobileNo1,'') as MobileNo1,epimaster.healthId As epiHealthId,epimaster.houseNo AS houseNo, epiMaster.brCertificateNo AS brCertificateNo,ifnull(NIDStatus,'') as NIDStatus, " +
                        " Cast(((julianday(date('now'))-julianday(DOB))/365.25) as int) as Age,Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) as AgeM,ifnull(ExType,'')as ExType from Member LEFT JOIN epiMaster epimaster ON epimaster.healthId = Member.healthid WHERE Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) <=18 " +
                        " union All  Select ifnull(clientMap.generatedId,'') as HealthID, ifnull(clientMap.name,'') as NameEng,'' as NameBang,ifnull(gender,'') as Sex,strftime('%d/%m/%Y', date(ifnull(DOB,''))) AS DOB,e.regNo AS regNo,(case when e.regNo is not null then '2' else '1' end) as regC, strftime('%d/%m/%Y', date(e.regDate)) AS regDate, " +
                        " zillaId as Dist , upazilaId as Upz, unionId as UN, mouzaId as Mouza, villageId as Vill, (select VILLAGENAME FROM Village WHERE VILLAGEID IN(SELECT Vill FROM Member)) VillageName,e.providerId as provcode, '' HHNo, '' as SNo,'' AS ExDate,ifnull(mobileno,'') as MobileNo1,e.healthId As epiHealthId,e.houseNo AS houseNo, e.brCertificateNo AS brCertificateNo,'' as NIDStatus, " +
                        " Cast(((julianday(date('now'))-julianday(DOB))/365.25) as int) as Age,Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) as AgeM,'' as ExType from clientMap LEFT JOIN epiMaster e ON e.healthId = clientMap.generatedId  WHERE Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) <=18 " ;
*/
                SQLStr = "SELECT ifnull ( member . HealthID , '' ) AS HealthID , ifnull ( NameEng , '' ) AS NameEng , ifnull ( NameBang , '' ) AS NameBang , \n" +
                        "ifnull ( Sex , '' ) AS Sex , strftime ( '%d/%m/%Y' , date ( ifnull ( DOB , '' ) ) ) AS DOB , \n" +
                        "epimaster . regNo AS regNo , ( CASE WHEN epimaster . regNo IS NOT NULL THEN '2' ELSE '1' END ) AS regC , \n" +
                        "strftime ( '%d/%m/%Y' , date ( epimaster . regDate ) ) AS regDate , Dist , Upz , UN , Mouza , Vill , \n" +
                        "( SELECT VILLAGENAME FROM Village WHERE VILLAGEID IN ( SELECT Vill FROM Member ) ) VillageName , \n" +
                        "ProvCode AS provcode , HHNo , SNo AS SNo , strftime ( '%d/%m/%Y' , date ( ExDate ) ) AS ExDate , ifnull ( MobileNo1 , '' ) AS MobileNo1 , \n" +
                        "epimaster . healthId AS epiHealthId , epimaster . houseNo AS houseNo , \n" +
                        "epiMaster . brCertificateNo AS brCertificateNo , ifnull ( NIDStatus , '' ) AS NIDStatus , \n" +
                        "CAST ( ( ( julianday ( date ( 'now' ) ) - julianday ( DOB ) ) / 365.25 ) AS int ) AS Age , \n" +
                        "CAST ( ( ( julianday ( date ( 'now' ) ) - julianday ( DOB ) ) / 30.4 ) AS int ) AS AgeM , ifnull ( ExType , '' ) AS ExType , \n" +
                        "( SELECT CASE \n" +
                        "WHEN CAST ( E.MNo AS int ) = 55 THEN ifnull ( E.Mother , '' ) \n" +
                        "WHEN CAST ( E.MNo AS int ) = 77 THEN ifnull ( E.Mother , '' ) \n" +
                        "WHEN CAST ( E.MNo AS int ) = 88 THEN ifnull ( E.Mother , '' ) \n" +
                        "ELSE ( SELECT NameEng FROM member A\n" +
                        "WHERE A.ProvCode = ( SELECT B.ProvCode FROM member B WHERE B.healthid = member . HealthID ) \n" +
                        "AND A.HHNo = ( SELECT C.HHNo FROM member C WHERE C.healthid = member . HealthID ) \n" +
                        "AND A.SNo = ( SELECT D.MNo FROM member  D WHERE D.HealthID = member . HealthID ) ) \n" +
                        "END AS Mother FROM member E WHERE E.healthId = member . HealthID ) AS MotherName,\n" +
                        "(SELECT \n" +
                        "CASE WHEN cast(E.FNo as int) = 55 THEN ifnull(E.Father,'') \n" +
                        "WHEN cast(E.FNo as int) = 77 THEN ifnull(E.Father,'')\n" +
                        "WHEN cast(E.FNo as int) = 88 THEN ifnull(E.Father,'') \n" +
                        "ELSE (select  A.NameEng  from member A\n" +
                        "where  A.ProvCode=(select  B.ProvCode  from member B Where  B.healthid =member.HealthID) \n" +
                        "and HHNo=(select  HHNo  from member  C Where  C.healthid  =member.HealthID) and \n" +
                        "SNo=(select  FNo  from member D Where  D.healthid = member.HealthID)) END AS  Father \n" +
                        "FROM member E WHERE E.healthId =member.HealthID) AS FatherName,  \n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 1) AS bcg,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 2) AS penta1,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 3) AS penta2,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 4) AS penta3,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 5) AS pcv1,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 6) AS pcv2,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 7) AS pcv3,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 8) AS opv0,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 9) AS opv1,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 10) AS opv2,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 11) AS opv3,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 12) AS ipv,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 13) AS mrdate1,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 14) AS mrdate2\n" +
                        ", ifnull ( member . HealthID , '' ) AS _id \n" +
                        "FROM Member LEFT JOIN epiMaster epimaster ON epimaster . healthId = Member . healthid \n" +
                        "WHERE CAST ( ( ( julianday ( date ( 'now' ) ) - julianday ( DOB ) ) / 30.4 ) AS int ) <= 18 \n" +
                        "UNION ALL SELECT ifnull ( clientMap . generatedId , '' ) AS HealthID , ifnull ( clientMap . name , '' ) AS NameEng , \n" +
                        "'' AS NameBang , ifnull ( gender , '' ) AS Sex , strftime ( '%d/%m/%Y' , date ( ifnull ( DOB , '' ) ) ) AS DOB , \n" +
                        "e . regNo AS regNo , ( CASE WHEN e . regNo IS NOT NULL THEN '2' ELSE '1' END ) AS regC , \n" +
                        "strftime ( '%d/%m/%Y' , date ( e . regDate ) ) AS regDate , zillaId AS Dist , upazilaId AS Upz , \n" +
                        "unionId AS UN , mouzaId AS Mouza , villageId AS Vill , ( SELECT VILLAGENAME FROM Village \n" +
                        "WHERE VILLAGEID IN ( SELECT Vill FROM Member ) ) VillageName , e . providerId AS provcode , '' HHNo , \n" +
                        "'' AS SNo , '' AS ExDate , ifnull ( mobileno , '' ) AS MobileNo1 , e . healthId AS epiHealthId , e . houseNo AS houseNo , \n" +
                        "e . brCertificateNo AS brCertificateNo , '' AS NIDStatus , CAST ( ( ( julianday ( date ( 'now' ) ) - julianday ( DOB ) ) / 365.25 ) AS int ) \n" +
                        "AS Age , CAST ( ( ( julianday ( date ( 'now' ) ) - julianday ( DOB ) ) / 30.4 ) AS int ) AS AgeM , '' AS ExType ,\n" +
                        "clientMap.motherName, clientMap.fatherName, \n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 1) AS bcg,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.HealthID AND ih.imuCode = 2) AS penta1,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 3) AS penta2,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 4) AS penta3,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 5) AS pcv1,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 6) AS pcv2,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 7) AS pcv3,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 8) AS opv0,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 9) AS opv1,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 10) AS opv2,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 11) AS opv3,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 12) AS ipv,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 13) AS mrdate1,\n" +
                        "(SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 14) AS mrdate2,\n" +
                        "ifnull ( clientMap . generatedId , '' ) AS _id FROM clientMap \n" +
                        "LEFT JOIN epiMaster e ON e . healthId = clientMap . generatedId \n" +
                        "WHERE CAST ( ( ( julianday ( date ( 'now' ) ) - julianday ( DOB ) ) / 30.4 ) AS int ) <= 18 ";


            } else if (spnEPIBlock.getSelectedItemPosition() > 0 & txtSearch.length() == 0) {

/*                SQLStr = "Select ifnull(member.HealthID,'') as HealthID, ifnull(NameEng,'') as NameEng,ifnull(NameBang,'') as NameBang,ifnull(Sex,'') as Sex,strftime('%d/%m/%Y', date(ifnull(DOB,''))) AS DOB,epimaster.regNo AS regNo, (case when epimaster.regNo is not null then '2' else '1' end) as regC, strftime('%d/%m/%Y', date(epimaster.regDate)) AS regDate, " +
                        " Dist, Upz, UN, Mouza, Vill, (select VILLAGENAME FROM Village WHERE VILLAGEID IN(SELECT Vill FROM Member)) VillageName,ProvCode as provcode, HHNo, SNo as SNo,strftime('%d/%m/%Y', date(ExDate)) AS ExDate,ifnull(MobileNo1,'') as MobileNo1,epimaster.healthId As epiHealthId,epimaster.houseNo AS houseNo, epiMaster.brCertificateNo AS brCertificateNo,ifnull(NIDStatus,'') as NIDStatus, " +
                        " Cast(((julianday(date('now'))-julianday(DOB))/365.25) as int) as Age,Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) as AgeM,ifnull(ExType,'')as ExType from Member LEFT JOIN epiMaster epimaster ON epimaster.healthId = Member.healthid WHERE Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) <=18 " +
                        " union All  Select ifnull(clientMap.generatedId,'') as HealthID, ifnull(clientMap.name,'') as NameEng,'' as NameBang,ifnull(gender,'') as Sex,strftime('%d/%m/%Y', date(ifnull(DOB,''))) AS DOB,e.regNo AS regNo,(case when e.regNo is not null then '2' else '1' end) as regC, strftime('%d/%m/%Y', date(e.regDate)) AS regDate, " +
                        " zillaId as Dist , upazilaId as Upz, unionId as UN, mouzaId as Mouza, villageId as Vill, (select VILLAGENAME FROM Village WHERE VILLAGEID IN(SELECT Vill FROM Member)) VillageName,e.providerId as provcode, '' HHNo, '' as SNo,'' AS ExDate,ifnull(mobileno,'') as MobileNo1,e.healthId As epiHealthId,e.houseNo AS houseNo, e.brCertificateNo AS brCertificateNo,'' as NIDStatus, " +
                        " Cast(((julianday(date('now'))-julianday(DOB))/365.25) as int) as Age,Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) as AgeM,'' as ExType from clientMap LEFT JOIN epiMaster e ON e.healthId = clientMap.generatedId " ;*/


               /* SQLStr = "Select ifnull(member.HealthID,'') as HealthID, ifnull(NameEng,'') as NameEng,ifnull(NameBang,'') as NameBang,ifnull(Sex,'') as Sex,strftime('%d/%m/%Y', date(ifnull(DOB,''))) AS DOB,epimaster.regNo AS regNo, (case when epimaster.regNo is not null then '2' else '1' end) as regC, strftime('%d/%m/%Y', date(epimaster.regDate)) AS regDate, " +
                        " Dist, Upz, UN, Mouza, Vill, (select VILLAGENAME FROM Village WHERE VILLAGEID IN(SELECT Vill FROM Member)) VillageName,ProvCode as provcode, HHNo, SNo as SNo,strftime('%d/%m/%Y', date(ExDate)) AS ExDate,ifnull(MobileNo1,'') as MobileNo1,epimaster.healthId As epiHealthId,epimaster.houseNo AS houseNo, epiMaster.brCertificateNo AS brCertificateNo,ifnull(NIDStatus,'') as NIDStatus, " +
                        " Cast(((julianday(date('now'))-julianday(DOB))/365.25) as int) as Age,Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) as AgeM,ifnull(ExType,'')as ExType from Member " +
                        " LEFT JOIN Household h ON h.Dist= Member.Dist and h.Upz= Member.Upz  and h.UN= Member.UN and h.Mouza= Member.Mouza  and h.Vill= Member.Vill" +
                        "LEFT JOIN epiMaster epimaster ON epimaster.healthId = Member.healthid WHERE Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) <=18 " +
                        " union All  Select ifnull(clientMap.generatedId,'') as HealthID, ifnull(clientMap.name,'') as NameEng,'' as NameBang,ifnull(gender,'') as Sex,strftime('%d/%m/%Y', date(ifnull(DOB,''))) AS DOB,e.regNo AS regNo,(case when e.regNo is not null then '2' else '1' end) as regC, strftime('%d/%m/%Y', date(e.regDate)) AS regDate, " +
                        " zillaId as Dist , upazilaId as Upz, unionId as UN, mouzaId as Mouza, villageId as Vill, (select VILLAGENAME FROM Village WHERE VILLAGEID IN(SELECT Vill FROM Member)) VillageName,e.providerId as provcode, '' HHNo, '' as SNo,'' AS ExDate,ifnull(mobileno,'') as MobileNo1,e.healthId As epiHealthId,e.houseNo AS houseNo, e.brCertificateNo AS brCertificateNo,'' as NIDStatus, " +
                        " Cast(((julianday(date('now'))-julianday(DOB))/365.25) as int) as Age,Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) as AgeM,'' as ExType from clientMap " +
                        " LEFT JOIN clientMapSubBlock csb ON csb.generatedId = clientMap.generatedId " +
                        " LEFT JOIN epiMaster e ON e.healthId = clientMap.generatedId  WHERE Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) <=18 " ;
*/

               /* SQLStr = "Select epimaster.healthId As epiHealthId,epimaster.regNo AS regNo, (case when epimaster.regNo is not null then '2' else '1' end) as regC,strftime('%d/%m/%Y', date(epimaster.regDate)) AS regDate,epimaster.houseNo AS houseNo, epiMaster.brCertificateNo AS brCertificateNo," +
                        " strftime('%d/%m/%Y', date(ExDate)) AS ExDate, " +
                        " Member.Dist As Dist, Member.Upz as Upz, Member.UN as UN, Member.Mouza as Mouza, Member.Vill as Vill, (select VILLAGENAME FROM Village WHERE VILLAGEID IN(SELECT Vill FROM Member)) VillageName, Member.ProvType as provtype,Member.ProvCode as provcode, Member.HHNo As HHNo, SNo as SNo, " +
                        " ifnull(member.HealthID,'') as HealthID, ifnull(NameEng,'') as NameEng," +
                        " ifnull(NameBang,'') as NameBang, ifnull(Rth,'') as Rth, ifnull(HaveNID,'') as HaveNID, ifnull(NID,'') as NID, " +
                        " ifnull(NIDStatus,'') as NIDStatus, ifnull(HaveBR,'') as HaveBR, ifnull(BRID,'') as BRID," +
                        "  ifnull(BRIDStatus,'') as BRIDStatus, ifnull(MobileNo1,'') as MobileNo1," +
                        " ifnull(MobileNo2,'') as MobileNo2,ifnull(MobileYN,'')as MobileYN, strftime('%d/%m/%Y', date(ifnull(DOB,''))) AS DOB, " +
                        " Cast(((julianday(date('now'))-julianday(DOB))/365.25) as int) as Age," +
                        " Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) as AgeM, ifnull(DOBSource,'') as DOBSource, " +
                        " ifnull(BPlace,'') as BPlace, ifnull(FNo,'') as FNo, ifnull(MNo,'') as MNo, " +
                        " ifnull(Sex,'') as Sex, ifnull(MS,'') as MS, ifnull(SPNO1,'') as SPNO1,ifnull(SPNO2,'') as SPNO2," +
                        " ifnull(SPNO3,'') as SPNO3,ifnull(SPNO4,'') as SPNO4, ifnull(ELCONo,'') as ELCONo, " +
                        " ifnull(ELCODontKnow,'') as ELCODontKnow, ifnull(EDU,'') as EDU, ifnull(Rel,'') as Rel, " +
                        " ifnull(Nationality,'') as Nationality, ifnull(OCP,'') as OCP," +
                        " ifnull(ExType,'')as ExType " +
                        " from Member " +
                        " LEFT JOIN Household h ON h.Dist= Member.Dist and h.Upz= Member.Upz  and h.UN= Member.UN and h.Mouza= Member.Mouza  and h.Vill= Member.Vill" +
                        " inner join HABlock b on b.BCode=h.subBlock" +
                        " LEFT JOIN epiMaster epimaster ON epimaster.healthId = Member.healthid" +
                        " WHERE Cast(((julianday(date('now'))-julianday(Member.DOB))/30.4) as int) <=18 and h.subBlock='" +EPIBlock+ "'";*/

                SQLStr = "SELECT ifnull ( member . HealthID , '' ) AS HealthID , ifnull ( NameEng , '' ) AS NameEng , ifnull ( NameBang , '' ) AS NameBang , \n" +
                        " ifnull ( Sex , '' ) AS Sex , strftime ( '%d/%m/%Y' , date ( ifnull ( DOB , '' ) ) ) AS DOB , \n" +
                        " epimaster . regNo AS regNo , ( CASE WHEN epimaster . regNo IS NOT NULL THEN '2' ELSE '1' END ) AS regC , \n" +
                        " strftime ( '%d/%m/%Y' , date ( epimaster . regDate ) ) AS regDate , Member.Dist , Member.Upz , Member.UN , Member.Mouza , Member.Vill , \n" +
                        " ( SELECT VILLAGENAME FROM Village WHERE VILLAGEID IN ( SELECT Vill FROM Member ) ) VillageName , \n" +
                        " Member.ProvCode AS provcode , Member.HHNo , SNo AS SNo , strftime ( '%d/%m/%Y' , date ( ExDate ) ) AS ExDate , ifnull ( MobileNo1 , '' ) AS MobileNo1 , \n" +
                        " epimaster . healthId AS epiHealthId , epimaster . houseNo AS houseNo , \n" +
                        " epiMaster . brCertificateNo AS brCertificateNo , ifnull ( NIDStatus , '' ) AS NIDStatus , \n" +
                        " CAST ( ( ( julianday ( date ( 'now' ) ) - julianday ( DOB ) ) / 365.25 ) AS int ) AS Age , \n" +
                        " CAST ( ( ( julianday ( date ( 'now' ) ) - julianday ( DOB ) ) / 30.4 ) AS int ) AS AgeM , ifnull ( ExType , '' ) AS ExType , \n" +
                        " ( SELECT CASE \n" +
                        " WHEN CAST ( E.MNo AS int ) = 55 THEN ifnull ( E.Mother , '' ) \n" +
                        " WHEN CAST ( E.MNo AS int ) = 77 THEN ifnull ( E.Mother , '' ) \n" +
                        " WHEN CAST ( E.MNo AS int ) = 88 THEN ifnull ( E.Mother , '' ) \n" +
                        " ELSE ( SELECT NameEng FROM member A\n" +
                        " WHERE A.ProvCode = ( SELECT B.ProvCode FROM member B WHERE B.healthid = member . HealthID ) \n" +
                        " AND A.HHNo = ( SELECT C.HHNo FROM member C WHERE C.healthid = member . HealthID ) \n" +
                        " AND A.SNo = ( SELECT D.MNo FROM member  D WHERE D.HealthID = member . HealthID ) ) \n" +
                        " END AS Mother FROM member E WHERE E.healthId = member . HealthID ) AS MotherName,\n" +
                        " (SELECT \n" +
                        " CASE WHEN cast(E.FNo as int) = 55 THEN ifnull(E.Father,'') \n" +
                        " WHEN cast(E.FNo as int) = 77 THEN ifnull(E.Father,'')\n" +
                        " WHEN cast(E.FNo as int) = 88 THEN ifnull(E.Father,'') \n" +
                        " ELSE (select  A.NameEng  from member A\n" +
                        " where  A.ProvCode=(select  B.ProvCode  from member B Where  B.healthid =member.HealthID) \n" +
                        " and HHNo=(select  HHNo  from member  C Where  C.healthid  =member.HealthID) and \n" +
                        " SNo=(select  FNo  from member D Where  D.healthid = member.HealthID)) END AS  Father \n" +
                        " FROM member E WHERE E.healthId =member.HealthID) AS FatherName,  \n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 1) AS bcg,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 2) AS penta1,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 3) AS penta2,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 4) AS penta3,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 5) AS pcv1,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 6) AS pcv2,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 7) AS pcv3,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 8) AS opv0,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 9) AS opv1,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 10) AS opv2,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 11) AS opv3,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 12) AS ipv,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 13) AS mrdate1,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 14) AS mrdate2\n" +
                        ", ifnull ( member . HealthID , '' ) AS _id \n" +
                        " FROM Member \n" +
                        " LEFT JOIN Household h ON h.Dist= Member.Dist and h.Upz= Member.Upz  and h.UN= Member.UN \n" +
                        " and h.Mouza= Member.Mouza  and h.Vill= Member.Vill and h.HHNo = Member.HHNo\n" +
                        " inner join HABlock b on b.BCode=h.subBlock \n" +
                        " LEFT JOIN epiMaster epimaster ON epimaster.healthId = Member.healthid \n" +
                        " WHERE Cast(((julianday(date('now'))-julianday(Member.DOB))/30.4) as int) <=18" +
                        " and h.subBlock='" + EPIBlock + "'" +
                        " UNION ALL SELECT ifnull ( clientMap . generatedId , '' ) AS HealthID , ifnull ( clientMap . name , '' ) AS NameEng , \n" +
                        " '' AS NameBang , ifnull ( gender , '' ) AS Sex , strftime ( '%d/%m/%Y' , date ( ifnull ( DOB , '' ) ) ) AS DOB , \n" +
                        " e. regNo AS regNo , ( CASE WHEN e . regNo IS NOT NULL THEN '2' ELSE '1' END ) AS regC , \n" +
                        " strftime ( '%d/%m/%Y' , date ( e . regDate ) ) AS regDate , zillaId AS Dist , upazilaId AS Upz , \n" +
                        " unionId AS UN , mouzaId AS Mouza , villageId AS Vill , ( SELECT VILLAGENAME FROM Village \n" +
                        " WHERE VILLAGEID IN ( SELECT Vill FROM Member ) ) VillageName , e . providerId AS provcode , '' HHNo , \n" +
                        " '' AS SNo , '' AS ExDate , ifnull ( mobileno , '' ) AS MobileNo1 , e . healthId AS epiHealthId , e . houseNo AS houseNo , \n" +
                        " e . brCertificateNo AS brCertificateNo , '' AS NIDStatus , CAST ( ( ( julianday ( date ( 'now' ) ) - julianday ( DOB ) ) / 365.25 ) AS int ) \n" +
                        " AS Age , CAST ( ( ( julianday ( date ( 'now' ) ) - julianday ( DOB ) ) / 30.4 ) AS int ) AS AgeM , '' AS ExType ,\n" +
                        " clientMap.motherName, clientMap.fatherName, \n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 1) AS bcg,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.HealthID AND ih.imuCode = 2) AS penta1,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 3) AS penta2,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 4) AS penta3,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 5) AS pcv1,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 6) AS pcv2,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 7) AS pcv3,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 8) AS opv0,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 9) AS opv1,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 10) AS opv2,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 11) AS opv3,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 12) AS ipv,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 13) AS mrdate1,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 14) AS mrdate2,\n" +
                        " ifnull ( clientMap . generatedId , '' ) AS _id FROM clientMap \n" +
                        " LEFT JOIN Household h ON h.Dist= clientMap.zillaId and h.Upz= clientMap.upazilaId  and h.UN= clientMap.unionId \n" +
                        " and h.Mouza= clientMap.mouzaId  and h.Vill= clientMap.villageId \n" +
                        " inner join HABlock b on b.BCode=h.subBlock \n" +
                        " LEFT JOIN epiMaster e ON e.healthId = clientMap.healthid \n" +
                        "  WHERE Cast(((julianday(date('now'))-julianday(clientMap.DOB))/30.4) as int) <=18" +
                        " and h.subBlock='" + EPIBlock + "'";


            } else if (spnEPIBlock.getSelectedItemPosition() > 0 & txtSearch.length() > 0) {
              /*  SQLStr = "Select epimaster.healthId As epiHealthId,epimaster.regNo AS regNo, (case when epimaster.regNo is not null then '2' else '1' end) as regC,strftime('%d/%m/%Y', date(epimaster.regDate)) AS regDate,epimaster.houseNo AS houseNo, epiMaster.brCertificateNo AS brCertificateNo," +
                        " strftime('%d/%m/%Y', date(ExDate)) AS ExDate, " +
                        " Member.Dist As Dist, Member.Upz as Upz, Member.UN as UN, Member.Mouza as Mouza, Member.Vill as Vill, (select VILLAGENAME FROM Village WHERE VILLAGEID IN(SELECT Vill FROM Member)) VillageName, Member.ProvType as provtype,Member.ProvCode as provcode, Member.HHNo As HHNo, SNo as SNo, " +
                        " ifnull(member.HealthID,'') as HealthID, ifnull(NameEng,'') as NameEng," +
                        " ifnull(NameBang,'') as NameBang, ifnull(Rth,'') as Rth, ifnull(HaveNID,'') as HaveNID, ifnull(NID,'') as NID, " +
                        " ifnull(NIDStatus,'') as NIDStatus, ifnull(HaveBR,'') as HaveBR, ifnull(BRID,'') as BRID," +
                        "  ifnull(BRIDStatus,'') as BRIDStatus, ifnull(MobileNo1,'') as MobileNo1," +
                        " ifnull(MobileNo2,'') as MobileNo2,ifnull(MobileYN,'')as MobileYN,strftime('%d/%m/%Y', date(ifnull(DOB,''))) AS DOB, " +
                        " Cast(((julianday(date('now'))-julianday(DOB))/365.25) as int) as Age," +
                        " Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) as AgeM, ifnull(DOBSource,'') as DOBSource, " +
                        " ifnull(BPlace,'') as BPlace, ifnull(FNo,'') as FNo, ifnull(MNo,'') as MNo, " +
                        " ifnull(Sex,'') as Sex, ifnull(MS,'') as MS, ifnull(SPNO1,'') as SPNO1,ifnull(SPNO2,'') as SPNO2," +
                        " ifnull(SPNO3,'') as SPNO3,ifnull(SPNO4,'') as SPNO4, ifnull(ELCONo,'') as ELCONo, " +
                        " ifnull(ELCODontKnow,'') as ELCODontKnow, ifnull(EDU,'') as EDU, ifnull(Rel,'') as Rel, " +
                        " ifnull(Nationality,'') as Nationality, ifnull(OCP,'') as OCP," +
                        " ifnull(ExType,'')as ExType " +
                        " from Member " +
                        " LEFT JOIN Household h ON h.Dist= Member.Dist and h.Upz= Member.Upz  and h.UN= Member.UN and h.Mouza= Member.Mouza  and h.Vill= Member.Vill" +
                        " inner join HABlock b on b.BCode=h.subBlock" +
                        " LEFT JOIN epiMaster epimaster ON epimaster.healthId = Member.healthid" +
                        " WHERE Cast(((julianday(date('now'))-julianday(Member.DOB))/30.4) as int) <=18 and h.subBlock='"+ EPIBlock+"' and member.HealthID='" +healthId+ "'";*/

                SQLStr = "SELECT ifnull ( member . HealthID , '' ) AS HealthID , ifnull ( NameEng , '' ) AS NameEng , ifnull ( NameBang , '' ) AS NameBang , \n" +
                        " ifnull ( Sex , '' ) AS Sex , strftime ( '%d/%m/%Y' , date ( ifnull ( DOB , '' ) ) ) AS DOB , \n" +
                        " epimaster . regNo AS regNo , ( CASE WHEN epimaster . regNo IS NOT NULL THEN '2' ELSE '1' END ) AS regC , \n" +
                        " strftime ( '%d/%m/%Y' , date ( epimaster . regDate ) ) AS regDate , Member.Dist , Member.Upz , Member.UN , Member.Mouza , Member.Vill , \n" +
                        " ( SELECT VILLAGENAME FROM Village WHERE VILLAGEID IN ( SELECT Vill FROM Member ) ) VillageName , \n" +
                        " Member.ProvCode AS provcode , Member.HHNo , SNo AS SNo , strftime ( '%d/%m/%Y' , date ( ExDate ) ) AS ExDate , ifnull ( MobileNo1 , '' ) AS MobileNo1 , \n" +
                        " epimaster . healthId AS epiHealthId , epimaster . houseNo AS houseNo , \n" +
                        " epiMaster . brCertificateNo AS brCertificateNo , ifnull ( NIDStatus , '' ) AS NIDStatus , \n" +
                        " CAST ( ( ( julianday ( date ( 'now' ) ) - julianday ( DOB ) ) / 365.25 ) AS int ) AS Age , \n" +
                        " CAST ( ( ( julianday ( date ( 'now' ) ) - julianday ( DOB ) ) / 30.4 ) AS int ) AS AgeM , ifnull ( ExType , '' ) AS ExType , \n" +
                        " ( SELECT CASE \n" +
                        " WHEN CAST ( E.MNo AS int ) = 55 THEN ifnull ( E.Mother , '' ) \n" +
                        " WHEN CAST ( E.MNo AS int ) = 77 THEN ifnull ( E.Mother , '' ) \n" +
                        " WHEN CAST ( E.MNo AS int ) = 88 THEN ifnull ( E.Mother , '' ) \n" +
                        " ELSE ( SELECT NameEng FROM member A\n" +
                        " WHERE A.ProvCode = ( SELECT B.ProvCode FROM member B WHERE B.healthid = member . HealthID ) \n" +
                        " AND A.HHNo = ( SELECT C.HHNo FROM member C WHERE C.healthid = member . HealthID ) \n" +
                        " AND A.SNo = ( SELECT D.MNo FROM member  D WHERE D.HealthID = member . HealthID ) ) \n" +
                        " END AS Mother FROM member E WHERE E.healthId = member . HealthID ) AS MotherName,\n" +
                        " (SELECT \n" +
                        " CASE WHEN cast(E.FNo as int) = 55 THEN ifnull(E.Father,'') \n" +
                        " WHEN cast(E.FNo as int) = 77 THEN ifnull(E.Father,'')\n" +
                        " WHEN cast(E.FNo as int) = 88 THEN ifnull(E.Father,'') \n" +
                        " ELSE (select  A.NameEng  from member A\n" +
                        " where  A.ProvCode=(select  B.ProvCode  from member B Where  B.healthid =member.HealthID) \n" +
                        " and HHNo=(select  HHNo  from member  C Where  C.healthid  =member.HealthID) and \n" +
                        " SNo=(select  FNo  from member D Where  D.healthid = member.HealthID)) END AS  Father \n" +
                        " FROM member E WHERE E.healthId =member.HealthID) AS FatherName,  \n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 1) AS bcg,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 2) AS penta1,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 3) AS penta2,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 4) AS penta3,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 5) AS pcv1,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 6) AS pcv2,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 7) AS pcv3,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 8) AS opv0,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 9) AS opv1,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 10) AS opv2,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 11) AS opv3,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 12) AS ipv,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 13) AS mrdate1,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 14) AS mrdate2\n" +
                        ", ifnull ( member . HealthID , '' ) AS _id \n" +
                        " FROM Member \n" +
                        " LEFT JOIN Household h ON h.Dist= Member.Dist and h.Upz= Member.Upz  and h.UN= Member.UN \n" +
                        " and h.Mouza= Member.Mouza  and h.Vill= Member.Vill and h.HHNo = Member.HHNo\n" +
                        " inner join HABlock b on b.BCode=h.subBlock \n" +
                        " LEFT JOIN epiMaster epimaster ON epimaster.healthId = Member.healthid \n" +
                        " WHERE Cast(((julianday(date('now'))-julianday(Member.DOB))/30.4) as int) <=18" +
                        " and h.subBlock='" + EPIBlock + "'  and Member.HealthID= '" + healthId + "'" +
                        " UNION ALL SELECT ifnull ( clientMap . generatedId , '' ) AS HealthID , ifnull ( clientMap . name , '' ) AS NameEng , \n" +
                        " '' AS NameBang , ifnull ( gender , '' ) AS Sex , strftime ( '%d/%m/%Y' , date ( ifnull ( DOB , '' ) ) ) AS DOB , \n" +
                        " e. regNo AS regNo , ( CASE WHEN e . regNo IS NOT NULL THEN '2' ELSE '1' END ) AS regC , \n" +
                        " strftime ( '%d/%m/%Y' , date ( e . regDate ) ) AS regDate , zillaId AS Dist , upazilaId AS Upz , \n" +
                        " unionId AS UN , mouzaId AS Mouza , villageId AS Vill , ( SELECT VILLAGENAME FROM Village \n" +
                        " WHERE VILLAGEID IN ( SELECT Vill FROM Member ) ) VillageName , e . providerId AS provcode , '' HHNo , \n" +
                        " '' AS SNo , '' AS ExDate , ifnull ( mobileno , '' ) AS MobileNo1 , e . healthId AS epiHealthId , e . houseNo AS houseNo , \n" +
                        " e . brCertificateNo AS brCertificateNo , '' AS NIDStatus , CAST ( ( ( julianday ( date ( 'now' ) ) - julianday ( DOB ) ) / 365.25 ) AS int ) \n" +
                        " AS Age , CAST ( ( ( julianday ( date ( 'now' ) ) - julianday ( DOB ) ) / 30.4 ) AS int ) AS AgeM , '' AS ExType ,\n" +
                        " clientMap.motherName, clientMap.fatherName, \n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 1) AS bcg,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.HealthID AND ih.imuCode = 2) AS penta1,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 3) AS penta2,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 4) AS penta3,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 5) AS pcv1,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 6) AS pcv2,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 7) AS pcv3,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 8) AS opv0,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 9) AS opv1,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 10) AS opv2,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 11) AS opv3,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 12) AS ipv,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 13) AS mrdate1,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 14) AS mrdate2,\n" +
                        " ifnull ( clientMap . generatedId , '' ) AS _id FROM clientMap \n" +
                        " LEFT JOIN Household h ON h.Dist= clientMap.zillaId and h.Upz= clientMap.upazilaId  and h.UN= clientMap.unionId \n" +
                        " and h.Mouza= clientMap.mouzaId  and h.Vill= clientMap.villageId \n" +
                        " inner join HABlock b on b.BCode=h.subBlock \n" +
                        " LEFT JOIN epiMaster e ON e.healthId = clientMap.healthid \n" +
                        "  WHERE Cast(((julianday(date('now'))-julianday(clientMap.DOB))/30.4) as int) <=18" +
                        " and h.subBlock='" + EPIBlock + "'  and clientMap.HealthID= '" + healthId + "'";
            } else if (spnEPIBlock.getSelectedItemPosition() == 0 & txtSearch.length() > 0) {
                /*SQLStr = "Select distinct epimaster.healthId As epiHealthId,epimaster.regNo AS regNo, (case when epimaster.regNo is not null then '2' else '1' end) as regC,strftime('%d/%m/%Y', date(epimaster.regDate)) AS regDate,epimaster.houseNo AS houseNo, epiMaster.brCertificateNo AS brCertificateNo," +
                        " strftime('%d/%m/%Y', date(ExDate)) AS ExDate, " +
                        " Member.Dist As Dist, Member.Upz as Upz, Member.UN as UN, Member.Mouza as Mouza, Member.Vill as Vill, (select VILLAGENAME FROM Village WHERE VILLAGEID IN(SELECT Vill FROM Member)) VillageName, Member.ProvType as provtype,Member.ProvCode as provcode, Member.HHNo As HHNo, SNo as SNo, " +
                        " ifnull(member.HealthID,'') as HealthID, ifnull(NameEng,'') as NameEng," +
                        " ifnull(NameBang,'') as NameBang, ifnull(Rth,'') as Rth, ifnull(HaveNID,'') as HaveNID, ifnull(NID,'') as NID, " +
                        " ifnull(NIDStatus,'') as NIDStatus, ifnull(HaveBR,'') as HaveBR, ifnull(BRID,'') as BRID," +
                        "  ifnull(BRIDStatus,'') as BRIDStatus, ifnull(MobileNo1,'') as MobileNo1," +
                        " ifnull(MobileNo2,'') as MobileNo2,ifnull(MobileYN,'')as MobileYN,strftime('%d/%m/%Y', date(ifnull(DOB,''))) AS DOB, " +
                        " Cast(((julianday(date('now'))-julianday(DOB))/365.25) as int) as Age," +
                        " Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) as AgeM, ifnull(DOBSource,'') as DOBSource, " +
                        " ifnull(BPlace,'') as BPlace, ifnull(FNo,'') as FNo, ifnull(MNo,'') as MNo, " +
                        " ifnull(Sex,'') as Sex, ifnull(MS,'') as MS, ifnull(SPNO1,'') as SPNO1,ifnull(SPNO2,'') as SPNO2," +
                        " ifnull(SPNO3,'') as SPNO3,ifnull(SPNO4,'') as SPNO4, ifnull(ELCONo,'') as ELCONo, " +
                        " ifnull(ELCODontKnow,'') as ELCODontKnow, ifnull(EDU,'') as EDU, ifnull(Rel,'') as Rel, " +
                        " ifnull(Nationality,'') as Nationality, ifnull(OCP,'') as OCP," +
                        " ifnull(ExType,'')as ExType " +
                        " from Member " +
                        " LEFT JOIN Household h ON h.Dist= Member.Dist and h.Upz= Member.Upz  and h.UN= Member.UN and h.Mouza= Member.Mouza  and h.Vill= Member.Vill" +
                        " inner join HABlock b on b.BCode=h.subBlock" +
                        " LEFT JOIN epiMaster epimaster ON epimaster.healthId = Member.healthid" +
                        " WHERE Cast(((julianday(date('now'))-julianday(Member.DOB))/30.4) as int) <=18 and h.subBlock<>'0' and member.HealthID='" +healthId+ "'";*/

                SQLStr = "SELECT ifnull ( member . HealthID , '' ) AS HealthID , ifnull ( NameEng , '' ) AS NameEng , ifnull ( NameBang , '' ) AS NameBang , \n" +
                        " ifnull ( Sex , '' ) AS Sex , strftime ( '%d/%m/%Y' , date ( ifnull ( DOB , '' ) ) ) AS DOB , \n" +
                        " epimaster . regNo AS regNo , ( CASE WHEN epimaster . regNo IS NOT NULL THEN '2' ELSE '1' END ) AS regC , \n" +
                        " strftime ( '%d/%m/%Y' , date ( epimaster . regDate ) ) AS regDate , Member.Dist , Member.Upz , Member.UN , Member.Mouza , Member.Vill , \n" +
                        " ( SELECT VILLAGENAME FROM Village WHERE VILLAGEID IN ( SELECT Vill FROM Member ) ) VillageName , \n" +
                        " Member.ProvCode AS provcode , Member.HHNo , SNo AS SNo , strftime ( '%d/%m/%Y' , date ( ExDate ) ) AS ExDate , ifnull ( MobileNo1 , '' ) AS MobileNo1 , \n" +
                        " epimaster . healthId AS epiHealthId , epimaster . houseNo AS houseNo , \n" +
                        " epiMaster . brCertificateNo AS brCertificateNo , ifnull ( NIDStatus , '' ) AS NIDStatus , \n" +
                        " CAST ( ( ( julianday ( date ( 'now' ) ) - julianday ( DOB ) ) / 365.25 ) AS int ) AS Age , \n" +
                        " CAST ( ( ( julianday ( date ( 'now' ) ) - julianday ( DOB ) ) / 30.4 ) AS int ) AS AgeM , ifnull ( ExType , '' ) AS ExType , \n" +
                        " ( SELECT CASE \n" +
                        " WHEN CAST ( E.MNo AS int ) = 55 THEN ifnull ( E.Mother , '' ) \n" +
                        " WHEN CAST ( E.MNo AS int ) = 77 THEN ifnull ( E.Mother , '' ) \n" +
                        " WHEN CAST ( E.MNo AS int ) = 88 THEN ifnull ( E.Mother , '' ) \n" +
                        " ELSE ( SELECT NameEng FROM member A\n" +
                        " WHERE A.ProvCode = ( SELECT B.ProvCode FROM member B WHERE B.healthid = member . HealthID ) \n" +
                        " AND A.HHNo = ( SELECT C.HHNo FROM member C WHERE C.healthid = member . HealthID ) \n" +
                        " AND A.SNo = ( SELECT D.MNo FROM member  D WHERE D.HealthID = member . HealthID ) ) \n" +
                        " END AS Mother FROM member E WHERE E.healthId = member . HealthID ) AS MotherName,\n" +
                        " (SELECT \n" +
                        " CASE WHEN cast(E.FNo as int) = 55 THEN ifnull(E.Father,'') \n" +
                        " WHEN cast(E.FNo as int) = 77 THEN ifnull(E.Father,'')\n" +
                        " WHEN cast(E.FNo as int) = 88 THEN ifnull(E.Father,'') \n" +
                        " ELSE (select  A.NameEng  from member A\n" +
                        " where  A.ProvCode=(select  B.ProvCode  from member B Where  B.healthid =member.HealthID) \n" +
                        " and HHNo=(select  HHNo  from member  C Where  C.healthid  =member.HealthID) and \n" +
                        " SNo=(select  FNo  from member D Where  D.healthid = member.HealthID)) END AS  Father \n" +
                        " FROM member E WHERE E.healthId =member.HealthID) AS FatherName,  \n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 1) AS bcg,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 2) AS penta1,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 3) AS penta2,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 4) AS penta3,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 5) AS pcv1,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 6) AS pcv2,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 7) AS pcv3,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 8) AS opv0,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 9) AS opv1,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 10) AS opv2,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 11) AS opv3,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 12) AS ipv,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 13) AS mrdate1,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = member.HealthID AND ih.imuCode = 14) AS mrdate2\n" +
                        ", ifnull ( member . HealthID , '' ) AS _id \n" +
                        " FROM Member \n" +
                        " LEFT JOIN Household h ON h.Dist= Member.Dist and h.Upz= Member.Upz  and h.UN= Member.UN \n" +
                        " and h.Mouza= Member.Mouza  and h.Vill= Member.Vill and h.HHNo = Member.HHNo\n" +
                        " inner join HABlock b on b.BCode=h.subBlock \n" +
                        " LEFT JOIN epiMaster epimaster ON epimaster.healthId = Member.healthid \n" +
                        " WHERE Cast(((julianday(date('now'))-julianday(Member.DOB))/30.4) as int) <=18" +
                        " and h.subBlock<>'0'  and Member.HealthID= '" + healthId + "'" +
                        " UNION ALL SELECT ifnull ( clientMap . generatedId , '' ) AS HealthID , ifnull ( clientMap . name , '' ) AS NameEng , \n" +
                        " '' AS NameBang , ifnull ( gender , '' ) AS Sex , strftime ( '%d/%m/%Y' , date ( ifnull ( DOB , '' ) ) ) AS DOB , \n" +
                        " e. regNo AS regNo , ( CASE WHEN e . regNo IS NOT NULL THEN '2' ELSE '1' END ) AS regC , \n" +
                        " strftime ( '%d/%m/%Y' , date ( e . regDate ) ) AS regDate , zillaId AS Dist , upazilaId AS Upz , \n" +
                        " unionId AS UN , mouzaId AS Mouza , villageId AS Vill , ( SELECT VILLAGENAME FROM Village \n" +
                        " WHERE VILLAGEID IN ( SELECT Vill FROM Member ) ) VillageName , e . providerId AS provcode , '' HHNo , \n" +
                        " '' AS SNo , '' AS ExDate , ifnull ( mobileno , '' ) AS MobileNo1 , e . healthId AS epiHealthId , e . houseNo AS houseNo , \n" +
                        " e . brCertificateNo AS brCertificateNo , '' AS NIDStatus , CAST ( ( ( julianday ( date ( 'now' ) ) - julianday ( DOB ) ) / 365.25 ) AS int ) \n" +
                        " AS Age , CAST ( ( ( julianday ( date ( 'now' ) ) - julianday ( DOB ) ) / 30.4 ) AS int ) AS AgeM , '' AS ExType ,\n" +
                        " clientMap.motherName, clientMap.fatherName, \n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 1) AS bcg,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.HealthID AND ih.imuCode = 2) AS penta1,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 3) AS penta2,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 4) AS penta3,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 5) AS pcv1,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 6) AS pcv2,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 7) AS pcv3,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 8) AS opv0,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 9) AS opv1,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 10) AS opv2,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 11) AS opv3,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 12) AS ipv,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 13) AS mrdate1,\n" +
                        " (SELECT ih.imuDate FROM immunizationHistory ih where ih.Healthid = clientMap.generatedId AND ih.imuCode = 14) AS mrdate2,\n" +
                        " ifnull ( clientMap . generatedId , '' ) AS _id FROM clientMap \n" +
                        " LEFT JOIN Household h ON h.Dist= clientMap.zillaId and h.Upz= clientMap.upazilaId  and h.UN= clientMap.unionId \n" +
                        " and h.Mouza= clientMap.mouzaId  and h.Vill= clientMap.villageId \n" +
                        " inner join HABlock b on b.BCode=h.subBlock \n" +
                        " LEFT JOIN epiMaster e ON e.healthId = clientMap.healthid \n" +
                        "  WHERE Cast(((julianday(date('now'))-julianday(clientMap.DOB))/30.4) as int) <=18" +
                        " and h.subBlock<>'0' and clientMap.HealthID= '" + healthId + "'";
            }


            Cursor cur1 = C.ReadData(SQLStr);

            cur1.moveToFirst();
            mylist.clear();


            int i = 0;
            while (!cur1.isAfterLast()) {
                HashMap<String, String> map = new HashMap<String, String>();

                if (i == 0) {
                    //View header = getLayoutInflater().inflate(R.layout.memberlistheading, null);
                    //list.addHeaderView(header);
                }

                String HealthId = cur1.getString(cur1.getColumnIndex("HealthID"));

                map.put("HealthId", cur1.getString(cur1.getColumnIndex("HealthID")));
                map.put("regNo", cur1.getString(cur1.getColumnIndex("regNo")));
                map.put("regc", cur1.getString(cur1.getColumnIndex("regC")));
                map.put("regDate", cur1.getString(cur1.getColumnIndex("regDate")));
                map.put("nameeng", cur1.getString(cur1.getColumnIndex("NameEng")));
                map.put("sex", cur1.getString(cur1.getColumnIndex("Sex")));
                map.put("dob", cur1.getString(cur1.getColumnIndex("DOB")));

               /* map.put("mother", g.GetMotherName(C, HealthId));
                map.put("father", g.GetFatherName(C, HealthId));*/

                map.put("mother", cur1.getString(cur1.getColumnIndex("MotherName")));
                map.put("father", cur1.getString(cur1.getColumnIndex("FatherName")));

                map.put("guardian", "");
                map.put("vill", cur1.getString(cur1.getColumnIndex("VillageName")));
                map.put("mobileno1", cur1.getString(cur1.getColumnIndex("MobileNo1")));
                //map.put("mobileno2", cur1.getString(cur1.getColumnIndex("MobileNo2")));
                map.put("houseNo", cur1.getString(cur1.getColumnIndex("houseNo")));

                String date = Global.DateConvertDMY(cur1.getString(cur1.getColumnIndex("DOB")));
                map.put("mothervitA", Global.addDays(date, 42));
                map.put("pentaPCV", Global.addDays(date, 42));
                map.put("mrfirst", Global.addDays(date, 70));
                map.put("mrsecond", Global.addDays(date, 70));

                //Mapping Immunization

                /*map.put("bcg", g.GetImmunizationDateForChild(C, HealthId, "1"));

                map.put("penta1", g.GetImmunizationDateForChild(C, HealthId, "2"));
                map.put("penta2", g.GetImmunizationDateForChild(C, HealthId, "3"));
                map.put("penta3", g.GetImmunizationDateForChild(C, HealthId, "4"));

                map.put("pcv1", g.GetImmunizationDateForChild(C,HealthId, "5"));
                map.put("pcv2", g.GetImmunizationDateForChild(C,HealthId, "6"));
                map.put("pcv3", g.GetImmunizationDateForChild(C,HealthId, "7"));

                map.put("opv0", g.GetImmunizationDateForChild(C,HealthId, "8"));
                map.put("opv1", g.GetImmunizationDateForChild(C,HealthId, "9"));
                map.put("opv2", g.GetImmunizationDateForChild(C,HealthId, "10"));
                map.put("opv3", g.GetImmunizationDateForChild(C,HealthId, "11"));

                map.put("ipv", g.GetImmunizationDateForChild(C,HealthId, "12"));

                map.put("mr1", g.GetImmunizationDateForChild(C,HealthId, "13"));
                map.put("mr2", g.GetImmunizationDateForChild(C, HealthId, "14"));*/

                map.put("bcg", cur1.getString(cur1.getColumnIndex("bcg")));

                map.put("penta1", cur1.getString(cur1.getColumnIndex("penta1")));
                map.put("penta2", cur1.getString(cur1.getColumnIndex("penta2")));
                map.put("penta3", cur1.getString(cur1.getColumnIndex("penta3")));

                map.put("pcv1", cur1.getString(cur1.getColumnIndex("pcv1")));
                map.put("pcv2", cur1.getString(cur1.getColumnIndex("pcv2")));
                map.put("pcv3", cur1.getString(cur1.getColumnIndex("pcv3")));

                map.put("opv0", cur1.getString(cur1.getColumnIndex("opv0")));
                map.put("opv1", cur1.getString(cur1.getColumnIndex("opv1")));
                map.put("opv2", cur1.getString(cur1.getColumnIndex("opv2")));
                map.put("opv3", cur1.getString(cur1.getColumnIndex("opv3")));

                map.put("ipv", cur1.getString(cur1.getColumnIndex("ipv")));

                map.put("mr1", cur1.getString(cur1.getColumnIndex("mrdate1")));
                map.put("mr2", cur1.getString(cur1.getColumnIndex("mrdate2")));


                map.put("agem", cur1.getString(cur1.getColumnIndex("AgeM")));
                map.put("totalimmucount", "");
                map.put("nid", cur1.getString(cur1.getColumnIndex("NIDStatus")));
                map.put("brcertificateno", cur1.getString(cur1.getColumnIndex("brCertificateNo")));
                map.put("exdate", cur1.getString(cur1.getColumnIndex("ExDate")));
                map.put("SNo", cur1.getString(cur1.getColumnIndex("SNo")));


                mylist.add(map);
                mSchedule = new SimpleAdapter(EpiList.this, mylist, R.layout.epilistrow, new String[]{"HealthID"},
                        new int[]{R.id.HealthID});
                list.setAdapter(new MemberListAdapter(this));

                i += 1;
                cur1.moveToNext();
                totalCount += 1;

            }
            cur1.close();


        } catch (Exception e) {
            Connection.MessageBox(EpiList.this, e.getMessage());
        }
    }


    public class MemberListAdapter extends BaseAdapter {
        private Context context;

        public MemberListAdapter(Context c) {
            context = c;
        }

        public int getCount() {
            return mSchedule.getCount();
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
                convertView = inflater.inflate(R.layout.epilistrow, null);
            }

            try {

                final TableLayout memtab = (TableLayout) convertView.findViewById(R.id.memtab);
                TableRow memtabrow = (TableRow) convertView.findViewById(R.id.memtabrow);
                TextView HealthID = (TextView) convertView.findViewById(R.id.HealthID);
                TextView regno = (TextView) convertView.findViewById(R.id.regno);
                TextView regdate = (TextView) convertView.findViewById(R.id.regdate);
                TextView name = (TextView) convertView.findViewById(R.id.name);
                TextView sex = (TextView) convertView.findViewById(R.id.sex);
                TextView dob = (TextView) convertView.findViewById(R.id.dob);
                TextView mname = (TextView) convertView.findViewById(R.id.mname);
                TextView fname = (TextView) convertView.findViewById(R.id.fname);
                TextView oname = (TextView) convertView.findViewById(R.id.oname);
                TextView vill = (TextView) convertView.findViewById(R.id.vill);
                TextView mobile = (TextView) convertView.findViewById(R.id.mobile);
                TextView bari = (TextView) convertView.findViewById(R.id.bari);
                TextView vita = (TextView) convertView.findViewById(R.id.vita);
                TextView penta = (TextView) convertView.findViewById(R.id.penta);
                TextView mr1 = (TextView) convertView.findViewById(R.id.mr1);
                TextView mr2 = (TextView) convertView.findViewById(R.id.mr2);
                TextView bcg = (TextView) convertView.findViewById(R.id.bcg);
                TextView penta1 = (TextView) convertView.findViewById(R.id.penta1);
                TextView penta2 = (TextView) convertView.findViewById(R.id.penta2);
                TextView penta3 = (TextView) convertView.findViewById(R.id.penta3);
                TextView pcv1 = (TextView) convertView.findViewById(R.id.pcv1);
                TextView pcv2 = (TextView) convertView.findViewById(R.id.pcv2);
                TextView pcv3 = (TextView) convertView.findViewById(R.id.pcv3);
                TextView opv0 = (TextView) convertView.findViewById(R.id.opv0);
                TextView opv1 = (TextView) convertView.findViewById(R.id.opv1);
                TextView opv2 = (TextView) convertView.findViewById(R.id.opv2);
                TextView opv3 = (TextView) convertView.findViewById(R.id.opv3);
                TextView ipv = (TextView) convertView.findViewById(R.id.ipv);
                TextView mrdate1 = (TextView) convertView.findViewById(R.id.mrdate1);
                TextView mrdate2 = (TextView) convertView.findViewById(R.id.mrdate2);
                ImageButton age12to23 = (ImageButton) convertView.findViewById(R.id.age12to23);
                TextView allimmugiven = (TextView) convertView.findViewById(R.id.allimmugiven);
                TextView br = (TextView) convertView.findViewById(R.id.br);
                TextView brcertificate = (TextView) convertView.findViewById(R.id.brcertificate);
                TextView deathdate = (TextView) convertView.findViewById(R.id.deathdate);
                TextView remarks = (TextView) convertView.findViewById(R.id.remarks);


                final HashMap<String, String> o = (HashMap<String, String>) mSchedule.getItem(position);


                HealthID.setText(o.get("HealthId"));
                regno.setText(o.get("regNo"));
                regdate.setText(o.get("regDate"));
                name.setText(o.get("nameeng"));
                if (o.get("sex").equals("1"))
                    sex.setText("ছেলে");
                else if (o.get("sex").equals("2"))
                    sex.setText("মেয়ে");

                dob.setText(o.get("dob"));
                mname.setText(o.get("mother"));
                fname.setText(o.get("father"));
                oname.setText("");
                vill.setText(o.get("vill"));
                mobile.setText(o.get("mobileno1"));
                bari.setText(o.get("houseNo"));
                vita.setText(o.get("mothervitA"));
                penta.setText(o.get("pentaPCV"));
                mr1.setText(o.get("mrfirst"));
                mr2.setText(o.get("mrsecond"));

                bcg.setText(o.get("bcg"));
                penta1.setText(o.get("penta1"));
                penta2.setText(o.get("penta2"));
                penta3.setText(o.get("penta3"));
                pcv1.setText(o.get("pcv1"));
                pcv2.setText(o.get("pcv2"));
                pcv3.setText(o.get("pcv3"));
                opv0.setText(o.get("opv0"));
                opv1.setText(o.get("opv1"));
                opv2.setText(o.get("opv2"));
                opv3.setText(o.get("opv3"));
                ipv.setText(o.get("ipv"));
                mrdate1.setText(o.get("mr1"));
                mrdate2.setText(o.get("mr2"));
                if (o.get("regc").equals("2")) {
                    memtabrow.setBackgroundColor(Color.parseColor("#99cc33"));

                }
                //  Log.e("Healthid : " , )
                if (o.get("regc").equals("1")) {
                    memtabrow.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }

                // ImageButton memberImage = (ImageButton) convertView.findViewById(R.id.memberImage);

                Integer agemonth = Integer.valueOf(o.get("agem"));
                if (agemonth >= 12 && agemonth <= 23) {
                    age12to23.setBackgroundResource(R.drawable.correct_sign);
                } else {

                }


                allimmugiven.setText(o.get("totalimmucount"));
                br.setText(o.get("nid"));
                brcertificate.setText(o.get("brcertificateno"));
                deathdate.setText(o.get("exdate"));
                remarks.setText("");


                memtab.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        g.setHealthID(o.get("HealthId"));
                        g.setepiName(o.get("nameeng"));
                        g.setepiSex(o.get("sex"));
                        g.setepimName(o.get("mother"));
                        g.setepifName(o.get("father"));
                        g.setDOB(o.get("dob"));
                        DisplayEpiReg();
                        /*if(txtCenterName.length()==0)
                        {
                            Connection.MessageBox(EpiList.this, "ই পি আই কেন্দ্র নাম খালি হতে পারে না");

                        }
                        else {




                        }*/

                    }
                });

            } catch (Exception ex) {

            }
            return convertView;
        }
    }


    //GPS Reading
    //.....................................................................................................
    public void FindLocation() {
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                updateLocation(location);
            }

            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    void updateLocation(Location location) {
        currentLocation = location;
        currentLatitude = currentLocation.getLatitude();
        currentLongitude = currentLocation.getLongitude();
    }

    // Method to turn on GPS
    public void turnGPSOn() {
        try {
            String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

            if (!provider.contains("gps")) { //if gps is disabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                sendBroadcast(poke);
            }
        } catch (Exception e) {

        }
    }

    // Method to turn off the GPS
    public void turnGPSOff() {
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (provider.contains("gps")) { //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }

    // turning off the GPS if its in on state. to avoid the battery drain.
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        turnGPSOff();
    }

    protected Dialog onCreateDialog(int id) {
        final Calendar c = Calendar.getInstance();
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mDateSetListener, g.mYear, g.mMonth - 1, g.mDay);
            // case DATE_DIALOG1:
            //   return new DatePickerDialog(this, mDateSetListener1,g.mYear,g.mMonth-1,g.mDay);


        }
        return null;
    }


   /* private DatePickerDialog.OnDateSetListener mDateSetListener1 = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year; mMonth = monthOfYear+1; mDay = dayOfMonth;

            TextView dtpDate;


            dtpDate = (TextView) findViewById(R.id.txtdate);


            if (VariableID.equals("btndate")) {
                dtpDate = (TextView) findViewById(R.id.txtdate);

                dtpDate.setText(new StringBuilder()
                        .append(Global.Right("00" + mDay, 2)).append("/")
                        .append(Global.Right("00" + mMonth, 2)).append("/")
                        .append(mYear));
            }

            if (VariableID.equals("btndate")) {

                ((TextView)findViewById(R.id.txtbar)).setText(g.getDay(dtpDate.getText().toString()));
            }
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = sdf.format(c.getTime());
                Date date1 = sdf.parse(formattedDate);

                Date date2 = sdf.parse(dtpDate.getText().toString());

                if (date2.after(date1))
                {

                    if (VariableID.equals("txtdate"))
                    {
                        Connection.MessageBox(EpiList.this, "তারিখ আজকের তারিখের সমান অথবা কম হতে হবে।");
                        return;
                    }

                }


            }
            catch(ParseException ex)
            {
                ex.printStackTrace();
            }

        }
    };*/

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear + 1;
            mDay = dayOfMonth;


            VisitDate.setText(new StringBuilder()
                    .append(Global.Right("00" + mDay, 2)).append("/")
                    .append(Global.Right("00" + mMonth, 2)).append("/")
                    .append(mYear).append(" "));

        }
    };
    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
            hour = selectedHour;
            minute = selectedMinute;
            EditText tpTime;

            /*tpTime = (EditText) findViewById(R.id.txtBcgtime);

            if (VariableID.equals("btnBcgtime") || VariableID.equals("btnMrtime") || VariableID.equals("btnHumtime")) {
                tpTime = (EditText) findViewById(R.id.txtBcgtime);
            }
            if (VariableID.equals("btnMrtime")) {
                tpTime = (EditText) findViewById(R.id.txtMrtime);
            }
            if (VariableID.equals("btnHumtime")) {
                tpTime = (EditText) findViewById(R.id.txtHumtime);
            }

            String am_pm = (hour < 12) ? "AM" : "PM";
            tpTime.setText(new StringBuilder()
                    .append(Global.Right("00" + hour, 2)).append(":")
                    .append(Global.Right("00" + minute, 2)).append(" ")
                    .append(am_pm));*/

        }
    };
}