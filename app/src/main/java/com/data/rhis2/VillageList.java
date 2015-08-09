package com.data.rhis2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import Common.Connection;
import Common.Global;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class VillageList extends Activity{
    Connection C;
    Global g;
    private static final int UPDATEDONE = 900;
    private ProgressDialog pDialog;

    private int mDay;
    private int mMonth;
    private int mYear;
    static final int DATE_DIALOG = 1;
    static final int TIME_DIALOG = 2;
    int DateID = 0;

    //Top menu
    //--------------------------------------------------------------------------------------------------
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mnuexit, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder adb = new AlertDialog.Builder(VillageList.this);
        switch (item.getItemId()) {

            case R.id.menuSearch:
               pDialog = ProgressDialog.show(con, "Wait","অপেক্ষা করুন ...");
                new Thread() {

                   public void run() {
                        try {
                           // finish();
                            Looper.prepare();


                            Bundle IDbundle = new Bundle();
                            IDbundle.putString("search", "search");

                            Intent searchform = new Intent(getApplicationContext(),MemberList.class);
                            searchform.putExtras(IDbundle);
                            startActivity(searchform);

                            Message msg = new Message();
                            msg.what = UPDATEDONE;
                            pDialogHandler.sendMessage(msg);


                        } catch (Exception e) {

                  }

                        Looper.loop();
                    }
                }.start();

                 return true;

            case R.id.menuExit:
                adb.setTitle("Exit");
                  adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান[হাঁ/না]?");
                  adb.setNegativeButton("না", null);
                  adb.setPositiveButton("হাঁ", new AlertDialog.OnClickListener() {
                      public void onClick(DialogInterface dialog, int which) {
                          finish();
                          System.exit(0);
                      }});
                  adb.show();
               return true;
            /*case R.id.menuReport:
                ReportForm();
                return true;*/

            case R.id.menuSync:
                try
                {
                    //Check for Internet connectivity
                    //*******************************************************************************
                    boolean netwoekAvailable=false;
                    if (Connection.haveNetworkConnection(this)) {
                        netwoekAvailable=true;

                    } else {
                        netwoekAvailable=false;
                        Connection.MessageBox(VillageList.this, "Internet connection is not available.");
                        return true;
                    }


                    String ResponseString="Status:";

                    final ProgressDialog progDailog = ProgressDialog.show(
                            VillageList.this, "", "অপেক্ষা করুন ...", true);

                    new Thread() {
                        public void run() {
                            String TableName = "";
                            String VariableList = "";
                            String ColumnList = "";
                            String UniqueField = "";

                            String ResponseString="Status:";
                            String response;

                            try {

                                //**ready to use and need to check before use: 12 apr 2015
                                /*
                                String[] TableList={"household","member","visits"};
                                //Upload data to server
                                C.DatabaseTableDataSync(TableList);

                                //Update data on local database

                                //data sync between local and server
                                */
                                TableName = "elco";
                                VariableList = "healthId,providerId,hhStatus,haHHNo,elcoNo,husbandName,domSource,marrDate,marrAge,son,dau,regDT,systemEntryDate,modifyDate";
                                String[] E = C.GenerateArrayList(VariableList, TableName);
                                response = C.UploadData(E, TableName , VariableList , "healthid");

                                /*
                                //Data upload to central server
                                TableName = "Household";
                                VariableList = "Div, Dist, Upz, UN, wardNew, wardOld, Mouza, FWAUnit, Vill, EPIBlock, PAddr, PermaAddress,ProvType,ProvCode, HHNo, Religion, VGFCard, StartTime, EndTime, Lat, Lon, UserId, EnDt, Upload";
                                String[] H = C.GenerateArrayList(VariableList, TableName);
                                response = C.UploadData(H, TableName , VariableList , "Div, Dist, Upz, UN, Mouza, Vill,ProvType,ProvCode, HHNo");

                                TableName = "Member";
                                VariableList = "Dist, Upz, UN, Mouza, Vill, ProvType,ProvCode, HHNo, SNo, HealthID, NameEng, NameBang, Rth, HaveNID, NID, NIDStatus, HaveBR, BRID, BRIDStatus, MobileNo1, MobileNo2, MobileYN, DOB, Age, DOBSource, BPlace, FNo, Father, FDontKnow, MNo, Mother, MDontKnow, Sex, MS, SPNO1, SPNO2, SPNO3, SPNO4, ELCONo, ELCODontKnow, EDU, Rel, Nationality, OCP, StartTime, EnType, EnDate, ExType, ExDate, EndTime, Lat, Lon, UserId, EnDt, Upload";
                                String[] M = C.GenerateArrayList(VariableList, TableName);
                                response = C.UploadData(M, TableName , VariableList , "Dist, Upz, UN, Mouza, Vill, ProvType,ProvCode, HHNo, SNo");

                                TableName = "Visits";
                                VariableList = "Div, Dist, Upz, UN, Mouza, Vill,ProvType,ProvCode, HHNo, VDate, VStatus, StartTime, EndTime, Lat, Lon, UserId, EnDt, Upload";
                                String[] V = C.GenerateArrayList(VariableList, TableName);
                                response = C.UploadData(V, TableName , VariableList , "Dist, Upz, UN, Mouza, Vill,ProvType,ProvCode, HHNo, VDate");


                                //Data Update on local database from Server
                                String SQLStr = "";

                                //Member Table
                                SQLStr  = " Select h.Dist, h.Upz, h.UN, h.Mouza, h.Vill, h.ProvType, h.ProvCode, h.HHNo, h.SNo,h.HealthID, h.NameEng, h.NameBang, h.Rth, h.HaveNID, h.NID, h.NIDStatus, h.HaveBR,";
                                SQLStr += " h.BRID, h.BRIDStatus, h.MobileNo1, h.MobileNo2, h.DOB, h.Age, h.DOBSource, h.BPlace, h.FNo, h.Father, h.FDontKnow, h.MNo, h.Mother, h.MDontKnow, h.Sex, h.MS, h.SPNO1,";
                                SQLStr += " h.SPNO2, h.SPNO3, h.SPNO4, h.ELCONo, h.ELCODontKnow, h.EDU, h.Rel, h.Nationality, h.OCP, h.StartTime, h.EnType, h.EnDate, h.ExType, h.ExDate";
                                SQLStr += " from Village v";
                                SQLStr += " inner join ProviderArea a on v.ZILLAID+v.UPAZILAID+v.UNIONID+v.MOUZAID+v.VILLAGEID=a.zillaid+a.upazilaid+a.unionid+a.mouzaid+a.villageid";
                                SQLStr += " inner join ProviderAssignArea aa on a.ZILLAID+a.UPAZILAID+a.UNIONID+a.areacode=aa.zillaid+aa.upazilaid+aa.unionid+aa.areacode";
                                SQLStr += " inner join Member h on a.zillaid+a.upazilaid+a.unionid+a.mouzaid+a.villageid=h.dist+h.upz+h.un+h.Mouza+h.Vill";
                                SQLStr += " where h.Upload='3' and aa.provtype='"+ g.getProvType() +"' and aa.provcode='"+ g.getProvCode() +"' and v.ZillaID='"+ g.getDistrict() +"' and v.Upazilaid='"+ g.getUpazila() +"' and v.Unionid='"+ g.getUnion() +"'";
                                response = C.DataUpdate(SQLStr,"Member","Dist, Upz, UN, Mouza, Vill, ProvType, ProvCode, HHNo, SNo,HealthID, NameEng, NameBang, Rth, HaveNID, NID, NIDStatus, HaveBR, BRID, BRIDStatus, MobileNo1, MobileNo2, DOB, Age, DOBSource, BPlace, FNo, Father, FDontKnow, MNo, Mother, MDontKnow, Sex, MS, SPNO1, SPNO2, SPNO3, SPNO4, ELCONo, ELCODontKnow, EDU, Rel, Nationality, OCP, StartTime, EnType, EnDate, ExType, ExDate","Dist, Upz, UN, Mouza, Vill, ProvType,ProvCode, HHNo, SNo");
                                */

                            } catch (Exception e) {

                            }
                            progDailog.dismiss();

                        }
                    }.start();
                    //Connection.MessageBox(VillageList.this,"তথ্য সফলভাবে ডাটাবেজ সার্ভারে আপলোড হয়েছে।");

                }
                catch(Exception ex)
                {
                    Connection.MessageBox(VillageList.this, ex.getMessage());
                }

               return true;


        }
        return false;
    }

    private Handler pDialogHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATEDONE:
                    pDialog.dismiss();
                    break;
            }

        }
    };
    boolean netwoekAvailable=false;
    Location currentLocation;
    double currentLatitude,currentLongitude;

    ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
    SimpleAdapter mSchedule;


    TextView txtUpazila;
    TextView txtUnion;
    Spinner spnMouza;
    Spinner spnWardNew;
    Spinner spnWardOld;
    Spinner spnFWAUnit;
    Spinner spnEPIBlock;
        
    Integer TI=0;
    Integer TD=0;
    Context con;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
                setContentView(R.layout.villagelist);
                C = new Connection(this);
                g = Global.getInstance();
                con = this;
                turnGPSOn();

                //GPS Location
                FindLocation();


            //Start Data Sync Scheduler
            try {
                //SyncScheduler.startOnlyIfConnectedToNetwork(getApplicationContext());
            }
            catch(Exception ex)
            {
                Connection.MessageBox(VillageList.this,ex.getMessage());
                return;
            }


                txtUpazila = (TextView)findViewById(R.id.txtUpazila);
                txtUpazila.setText(C.ReturnSingleValue("select upazilaid||','||upazilaname from upazila where zillaid='"+ g.getDistrict() +"' and upazilaid='"+ g.getUpazila() +"'"));
                txtUnion   = (TextView)findViewById(R.id.txtUnion);
                txtUnion.setText(C.ReturnSingleValue("select unionid||','||unionname from Unions where zillaid='"+ g.getDistrict() +"' and upazilaid='"+ g.getUpazila() +"' and unionid='"+ g.getUnion() +"'"));

                spnMouza = (Spinner)findViewById( R.id.spnMouza );                
                spnMouza.setAdapter( C.getArrayAdapter( "Select '.All' union select mouzaid||'-'||mouzaname from Mouza where zillaid='"+ g.getDistrict() +"' and upazilaid='"+ g.getUpazila() +"' and unionid='"+ g.getUnion() +"'" ) );
                

                spnWardNew  = (Spinner)findViewById( R.id.spnWardNew );
                List<String> wardList = new ArrayList<String>();
                
                wardList.add("");
                wardList.add("01");
                wardList.add("02");
                wardList.add("03");
                wardList.add("04");
                wardList.add("05");
                wardList.add("06");
                wardList.add("07");
                wardList.add("08");
                wardList.add("09");

                ArrayAdapter<String> adptrWard= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, wardList);
                spnWardNew.setAdapter(adptrWard);                

                spnWardOld  = (Spinner)findViewById(R.id.spnWardOld);
                List<String> wardListOld = new ArrayList<String>();
                
                wardListOld.add("");
                wardListOld.add("01");
                wardListOld.add("02");
                wardListOld.add("03");

                ArrayAdapter<String> adptrWardOld= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, wardListOld);
                spnWardOld.setAdapter(adptrWardOld);
                
                spnFWAUnit=(Spinner) findViewById(R.id.spnFWAUnit);
                //spnFWAUnit.setAdapter(C.getArrayAdapter("select distinct ucode||UNameBan from ProviderArea a,FWAUnit u where a.fwaunit=u.ucode"));
                spnFWAUnit.setAdapter(C.getArrayAdapter("select distinct ucode||'-'||UNameBan from FWAUnit"));


                List<String> listFWAUnit = new ArrayList<String>();
                
                listFWAUnit.add("");
                listFWAUnit.add("11-১/ক");
                listFWAUnit.add("12-১/খ");
                listFWAUnit.add("13-১/গ");
                listFWAUnit.add("14-১/ঘ");
                listFWAUnit.add("21-২/ক");
                listFWAUnit.add("22-২/খ");
                listFWAUnit.add("23-২/গ");
                listFWAUnit.add("24-২/ঘ");
                listFWAUnit.add("31-৩/ক");
                listFWAUnit.add("32-৩/খ");
                listFWAUnit.add("33-৩/গ");
                listFWAUnit.add("34-৩/ঘ");

                ArrayAdapter<String> adptrFWAUnit= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listFWAUnit);
                spnFWAUnit.setAdapter(adptrFWAUnit);



                spnEPIBlock=(Spinner) findViewById(R.id.spnEPIBlock);
                spnEPIBlock.setAdapter(C.getArrayAdapter("select BCode||'-'||BNameBan from HABlock"));



                List<String> listEPIBlock = new ArrayList<String>();
                
                listEPIBlock.add("");
                listEPIBlock.add("01-ক-১");
                listEPIBlock.add("02-ক-২");
                listEPIBlock.add("03-খ-১");
                listEPIBlock.add("04-খ-২");
                listEPIBlock.add("05-গ-১");
                listEPIBlock.add("06-গ-২");
                listEPIBlock.add("07-ঘ-১");
                listEPIBlock.add("08-ঘ-২");
                ArrayAdapter<String> adptrEPIBlock= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listEPIBlock);
                spnEPIBlock.setAdapter(adptrEPIBlock);



                AreaList();
                
                spnMouza.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        //AreaList();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                    }
                });

            TextView txtHIDRemaining = (TextView)findViewById(R.id.txtHIDRemaining);
            txtHIDRemaining.setText(": "+ C.ReturnSingleValue("select Count(*)Total from HealthIDRepository where ifnull(Status,'1')='1'"));



        }
        catch(Exception ex)
        {
                Connection.MessageBox(VillageList.this, ex.getMessage());
        }
        
        }

    public void onResume() {
        super.onResume();

        /*
        //Start Data Sync Scheduler
        try {
            SyncScheduler.startOnlyIfConnectedToNetwork(getApplicationContext());
        }
        catch(Exception ex)
        {
            Connection.MessageBox(VillageList.this,ex.getMessage());
            return;
        }*/

    }


    TextView txtVDate;
    ImageButton btnVDate;
    String VariableID;
    TextView txtHHNo;
    TextView txtPresent;
    TextView txtAbsent;
    TextView txtTotalMem;

    private void ReportForm()
    {
        final Dialog dialog = new Dialog(VillageList.this);
        dialog.setTitle("Report Form");
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.visitedstatus);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        txtHHNo     = (TextView)dialog.findViewById(R.id.txtTotalHH);
        txtPresent  = (TextView)dialog.findViewById(R.id.txtPresent);
        txtAbsent   = (TextView)dialog.findViewById(R.id.txtAbsent);
        txtTotalMem = (TextView)dialog.findViewById(R.id.txtTotalMem);

        txtVDate = (TextView)dialog.findViewById(R.id.txtVDate);
        txtVDate.setText(Global.DateNowDMY());
        btnVDate = (ImageButton) dialog.findViewById(R.id.btnVDate);
        btnVDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { VariableID = "btnVDate"; showDialog(DATE_DIALOG); }});

        Button cmdPrev = (Button) dialog.findViewById(R.id.cmdPrev);
        cmdPrev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }});

        Button cmdNext = (Button) dialog.findViewById(R.id.cmdNext);
        cmdNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }});

        VisitedStatus(Global.DateConvertYMD(txtVDate.getText().toString()));


        dialog.show();
    }

    private void VisitedStatus(String VisitDate)
    {
        txtHHNo.setText(C.ReturnSingleValue("Select Count(*)Total from Household  where strftime('%Y-%m-%d',EnDt)='"+ VisitDate +"'"));
        txtTotalMem.setText(C.ReturnSingleValue("Select Count(*)Total from Member  where strftime('%Y-%m-%d',EnDt)='"+ VisitDate +"'"));
        //txtPresent.setText(C.ReturnSingleValue("Select count(distinct dist||upz||un||mouza||vill||hhno) from Member  where strftime('%Y-%m-%d',EnDt)='"+ VisitDate +"'"));
        //txtAbsent.setText(C.ReturnSingleValue("select count(*)Total from Household h where not exists(select dist from Member where dist||upz||un||mouza||vill||hhno=h.dist||h.upz||h.un||h.mouza||h.vill||h.hhno) and strftime('%Y-%m-%d',h.EnDt)='"+ VisitDate +"'"));


        /*Cursor cur = C.ReadData("");
        cur.moveToFirst();

        while(!cur.isAfterLast())
        {
            txtHHNo.setText(C.ReturnSingleValue("Select Count(*)Total from Household  where strftime('%Y-%m-%d',EnDt)='"+ VisitDate +"'"));
            txtTotalMem.setText(C.ReturnSingleValue("Select Count(*)Total from Member  where strftime('%Y-%m-%d',EnDt)='"+ VisitDate +"'"));

            //vcode[0][i]= cur.getString(cur.getColumnIndex("dist"));
            //i +=1;
            cur.moveToNext();
        }
        cur.close();*/
    }


        public void AreaList()
        {
            GridView g1=(GridView) findViewById(R.id.gridMenu);
            g1.setAdapter(new GridAdapter(this));
        }
        
        
        public class GridAdapter extends BaseAdapter { 
                private Context mContext;
                String[][] vcode;
                String MouzaCode;
                Integer totalRec;
                
                public GridAdapter(Context c) {
                        mContext = c;
                        //MouzaCode = Mouza;
                }
        
                public int getCount() {
                        String M = Global.Left(spnMouza.getSelectedItem().toString(), 3).equalsIgnoreCase(".al")?"%":Global.Left(spnMouza.getSelectedItem().toString(), 3);
                        String S = "select count(villageid)TotalVill from Village where zillaid='"+ g.getDistrict() +"' and upazilaid='"+ g.getUpazila() +"' and unionid='"+ g.getUnion() +"' and Mouzaid like('"+ M +"')";                        
                        return Integer.parseInt(C.ReturnSingleValue(S));
                }
        
                public Object getItem(int position){
                        return null;
                }
        
                public long getItemId(int position){
                        return 0;
                }
        
                //Create a new ImageView for each item referenced by the Adapter
                public View getView(int position, View convertView, ViewGroup parent) {
                        View MyView = convertView;
                if (convertView == null) { 
                    LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
                    MyView = li.inflate(R.layout.single_grid_item, null);
        
                    String SQL  = "";
                    String SQL1 = "";
                    String M1 = Global.Left(spnMouza.getSelectedItem().toString(), 3).equalsIgnoreCase(".al")?"%":Global.Left(spnMouza.getSelectedItem().toString(), 3);
                    
                        SQL  = "select zillaid as dist,upazilaid as upazila,unionid as unions,mouzaid as mouza,villageid as village,villagename as villname,";
                        SQL  += " count(h.dist)as totalhh from Village v";
                        SQL  += " left outer join Household h on v.zillaid||v.upazilaid||v.unionid||v.mouzaid||v.villageid=h.dist||h.upz||h.un||h.mouza||h.vill";
                        SQL  += " where zillaid='"+ g.getDistrict() +"' and upazilaid='"+ g.getUpazila() +"' and unionid='"+ g.getUnion() +"' and Mouzaid like('"+ M1 +"')";
                        SQL  += " group by zillaid,upazilaid,unionid,mouzaid,villageid,villagename";
                        
                        Cursor cur = C.ReadData(SQL);
                        cur.moveToFirst();
                        
                        totalRec = getCount();
                        vcode=new String[7][totalRec];
                        int i=0;
                        while(!cur.isAfterLast())
                        {
                                vcode[0][i]= cur.getString(cur.getColumnIndex("dist"));
                                vcode[1][i]= cur.getString(cur.getColumnIndex("upazila"));
                                vcode[2][i]= cur.getString(cur.getColumnIndex("unions"));
                                vcode[3][i]= cur.getString(cur.getColumnIndex("mouza"));
                                vcode[4][i]= cur.getString(cur.getColumnIndex("village"));
                                vcode[5][i]= cur.getString(cur.getColumnIndex("villname"));
                                vcode[6][i]= cur.getString(cur.getColumnIndex("totalhh"));
                                
                                i +=1;                          
                                cur.moveToNext();
                        }
                        cur.close();
                        
                    Button tv = (Button)MyView.findViewById(R.id.image_name);
                    tv.setTextSize(14);   
                    tv.setText(vcode[3][position]+"-"+vcode[4][position]+"\n"+vcode[5][position]+"\n("+vcode[6][position]+")");
                    final Integer p = position;
                                tv.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                finish();
                                g.setMouza(vcode[3][p].toString());
                                g.setVillage(vcode[4][p].toString());
                                g.setVillageName(vcode[5][p].toString());
                                Intent f1 = new Intent(getApplicationContext(),HouseholdIndex.class);
                                startActivity(f1);
                            }
                        });     
                }
                return MyView;
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
        currentLocation  = location; 
        currentLatitude  = currentLocation.getLatitude(); 
        currentLongitude = currentLocation.getLongitude(); 
    } 

    //Method to turn on GPS
    public void turnGPSOn(){
        try
        {
                String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        
                if(!provider.contains("gps")){ //if gps is disabled
                    final Intent poke = new Intent();
                    poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                    poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                    poke.setData(Uri.parse("3"));
                    sendBroadcast(poke);
                }
        }
        catch (Exception e) {

        }
    }
    
    //Method to turn off the GPS
    public void turnGPSOff(){
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(provider.contains("gps")){ //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }

    //turning off the GPS if its in on state. to avoid the battery drain.
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        turnGPSOff();
    }


    protected Dialog onCreateDialog(int id) {
        final Calendar c = Calendar.getInstance();
        Integer Y=g.mYear;
        Integer M=g.mMonth;
        Integer D=g.mDay;

        if(txtVDate.getText().length()>0)
        {
            Y = Integer.valueOf(Global.Right(txtVDate.getText().toString(), 4));
            M = Integer.valueOf(txtVDate.getText().toString().substring(4,5));
            D = Integer.valueOf(Global.Left(txtVDate.getText().toString(), 2));
        }
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mDateSetListener, Y, M-1,D);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year; mMonth = monthOfYear+1; mDay = dayOfMonth;
            txtVDate.setText(new StringBuilder()
                    .append(Global.Right("00"+mDay,2)).append("/")
                    .append(Global.Right("00"+mMonth,2)).append("/")
                    .append(mYear));

            VisitedStatus(Global.DateConvertYMD(txtVDate.getText().toString()));
        }
    };
}