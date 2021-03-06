package com.data.rhis2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import Common.Connection;
import Common.Global;

public class LoginActivity extends Activity {
    Connection C;
    Global g;
    boolean networkAvailable = false;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog dialog;
    int count = 0;

    TextView lblStaffType;
    String SystemUpdateDT = "";
    private String Password = "";
    TextView AppName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.login);
            C = new Connection(this);
            g = Global.getInstance();

            final Spinner uid = (Spinner) findViewById(R.id.userId);
            final EditText pass = (EditText) findViewById(R.id.pass);
            TextView lblSystemDate = (TextView) findViewById(R.id.lblSystemDate);
            TextView lblSystemDate1 = (TextView) findViewById(R.id.lblSystemDate1);


            //Need to update date every time whenever shared updated system
            //Format: DDMMYYYYom
            //*********************************************************************
            // SystemUpdateDT
            //*********************************************************************
            //icddr,b FPI
            //Bhuapur FPI System Update Date is 19/09/2016
            //Mirzapur FPI System Update Date is 04/09/2016
            //Mirzapur 2nd FPI System Update Date is 03/10/2016

            //Basail last FPI System Update Date is 29/09/2016
            //Delduar FPI System Update date is 03/10/2016
            //Nagorpur FPI System Update date is 03/10/2016
            //Gopalpur FPI System Update date is 03/10/2016
            //Donbari FPI System Update date is 03/10/2016
            //Tangail sader FPI System Update date is 03/10/2016
            //Gatail FPI System Update date is 03/10/2016
            //SCI
            //FPI System Update Date is 18/10/2016
            //*********************************************************************
            //icddr,b AHI
          //Basail last AHI System Update Date is 29/05/2016
            //*********************************************************************
            //icddr,b HI
            //Basail last HI System Update Date is 29/05/2016

            //Need to update date every time whenever shared updated system
            //Format: DDMMYYYYom
            //*********************************************************************



          //  SystemUpdateDT="03/10/2016";
            //AHI,HI
            SystemUpdateDT="21/11/2016";
            //FPI
           // SystemUpdateDT="29/09/2016";
            lblSystemDate.setText("Version: 1.0");
            lblSystemDate1.setText("Release Date: " + SystemUpdateDT);


            //Check for Internet connectivity
            if (Connection.haveNetworkConnection(LoginActivity.this)) {
                networkAvailable = true;

                //Health id replace by SHR
                Intent syncService = new Intent(this, SyncHealthID_Service.class);
                startService(syncService);
                //stopService(syncService);
            } else {
                networkAvailable = false;
            }



            //Rebuild Database
            String TotalTab = C.ReturnSingleValue("SELECT count(*) FROM sqlite_master WHERE type = 'table' AND name != 'android_metadata' AND name != 'sqlite_sequence'");

            if (Integer.valueOf(TotalTab) == 0) {
                if (networkAvailable) {
                    //Call Setting Form
                    finish();
                    Intent f1 = new Intent(getApplicationContext(), SettingForm.class);
                    startActivity(f1);
                    return;
                } else {
                    Connection.MessageBox(LoginActivity.this, "Internet connection is not available for building initial database.");
                    //finish();
                    //System.exit(0);
                    return;
                }
            }

            //FPI New ProvType Change

     /*       if (C.Existence("Select * from ProviderDB where ProvType='4'")) {
                UpdateProvType();
            }
       */

            //Assign Global Variable
            String PType = C.ReturnSingleValue("Select ProvType from ProviderDB where ProvCode=(Select UserID from Login)");
            String Area = C.ReturnSingleValue("Select zillaid||'^'||upazilaid||'^'||unionid||'^'||ProvType||'^'||ProvCode||'^'||ProvName from ProviderDB where ProvType='" + PType + "' and Active='1'");
            String[] A = Connection.split(Area, '^');
            //g.setDivision(A[0]);
            g.setDistrict(A[0]);
            g.setUpazila(A[1]);
            g.setUnion(A[2]);
            g.setLoginProvType(A[3]);
            g.setLoginProvCode(A[4]);
            g.setProvType(A[3]);
            g.setProvCode(A[4]);
            //Icddrb
            if (g.getDistrict().equals("93")) {


                C.Save("DROP TABLE IF EXISTS DataLengthTable");
                C.Save("CREATE TABLE DataLengthTable ( \n" +
                        "  tableName VARCHAR( 20 )     PRIMARY KEY,\n" +
                        "dataLength INT);");


                C.Save("INSERT INTO DataLengthTable (tableName, dataLength) VALUES('workPlanMaster', 50)");
                C.Save("INSERT INTO DataLengthTable (tableName, dataLength) VALUES('workPlanDetail', 50)");

                C.Save("update workPlanDetail set epischedulerId=substr( epischedulerId, 1, 1 ) ||( \n" +
                        "           SELECT DISTINCT cast(ward as int)\n" +
                        "                      FROM ProviderArea\n" +
                        "                     WHERE CAST ( provCode AS INT ) = epiproviderId \n" +
                        "       )||2016||substr( epischedulerId, 2, 3 ) WHERE Length( epischedulerId ) = 3;");
            }


            if (g.getProvType().equalsIgnoreCase("10")) {
                AppName = (TextView) findViewById(R.id.AppName);
              AppName.setText("পরিবার পরিকল্পনা পরিদর্শক তদারকি কার্যক্রম");

               // AppName.setText("পরিবার পরিকল্পনা পরিদর্শক সুপরভিষন মডুল");

            } else if (g.getProvType().equalsIgnoreCase("11")) {
                AppName = (TextView) findViewById(R.id.AppName);
                AppName.setText("সহকারী স্বাস্থ্য পরিদর্শক তদারকি কার্যক্রম");
               // AppName.setText("সহকারী স্বাস্থ্য পরিদর্শক সুপরভিষন মডুল");

            } else if (g.getProvType().equalsIgnoreCase("12")) {
                AppName = (TextView) findViewById(R.id.AppName);
              AppName.setText("স্বাস্থ্য পরিদর্শক তদারকি কার্যক্রম");
               //AppName.setText("স্বাস্থ্য পরিদর্শক সুপরভিষন মডুল");

            }
            String DeviceNo = C.ReturnSingleValue("Select DeviceNo from DeviceNo");
            g.setDeviceNo(DeviceNo);

            uid.setAdapter(C.getArrayAdapter("Select UserId||'-'||UserName from Login"));
            uid.setSelection(Global.SpinnerItemPosition(uid, 3, C.ReturnSingleValue("Select LoginID from LastLogin")));

            //Only for removing the data of training: 17 Nov 2015
        /*    if (Global.DateNowDMY().equals("07/01/2016") & g.getDistrict().equals("93") & g.getUpazila().equals("38") & (g.getProvType().equals("2") | g.getProvType().equals("3"))) {
                *//*C.Save("Delete from Member where date(endt)    <= '2016-01-07'");
                C.Save("Delete from Household where date(endt) <= '2016-01-07'");
                C.Save("Delete from Visits where date(endt)    <= '2016-01-07'");
                C.Save("Delete from SES where date(endt)       <= '2016-01-07'");
                C.Save("Update HealthIDRepository set Status='1'");*//*

            }*/

            //***********************************************
            //netwoekAvailable=false;
            //***********************************************

            Button btnClose = (Button) findViewById(R.id.btnClose);
            btnClose.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    finish();
                    System.exit(0);
                }
            });


            //Login -----------------------------------------------------------------------
            Button loginButton = (Button) findViewById(R.id.btnLogin);
            loginButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    try {
                        String[] U = Connection.split(uid.getSelectedItem().toString(), '-');
                        g.setUserID(U[0]);

                        //use for administration: 12 Mar 2015
                    /*    if (pass.getText().toString().equals("rhis2015admin")) {
                            //Call Setting Form
                            //finish();
                            //Intent f1 = new Intent(getApplicationContext(), SettingForm.class);
                            //startActivity(f1);

                        }*/

                    /*    //stop for development
                        if (pass.getText().toString().equals("123")) {
                            C.AlterStockTransaction();
                            //Connection.MessageBox(LoginActivity.this,"পাসওয়ার্ড সঠিক নয়, পুনরায় চেষ্টা করুন।");
                            //pass.requestFocus();
                            //return;
                        }*/


                        //Store Last Login information
                        C.Save("Delete from LastLogin");
                        C.Save("Insert into LastLogin(LoginID)Values('" + Global.Left(uid.getSelectedItem().toString(), 3) + "')");
                        // C.Save("update healthidrepository set healthid='2'||substr(healthid,2,10) where substr(healthid,1,1)='0'");

                        if (Connection.haveNetworkConnection(LoginActivity.this)) {
                            networkAvailable = true;

                        } else {
                            networkAvailable = false;
                        }

                     /*   */

                        //netwoekAvailable=false;

                        //Download Updated System
                        //...................................................................................
                        if (networkAvailable == true) {
                            //Retrieve data from server for checking local device
                            //String resp ="select * from sp_ServerCheck ('"+ g.getDistrict().toString() +"','"+ g.getUpazila().toString() +"','"+ g.getUnion().toString() +"','"+ g.getProvType().toString() +"','"+ g.getProvCode().toString() +"')";
                            //String[] ServerVal  = Connection.split(resp,','); // Connection.split(C.ReturnResult("ReturnSingleValue","select * from sp_ServerCheck ("+ g.getDistrict().toString() +",'"+ g.getUpazila().toString() +"','"+ g.getUnion().toString() +"','"+ g.getProvType().toString() +"','"+ g.getProvCode().toString() +"')"),',');
                            String[] ServerVal = Connection.split(C.ReturnSingleValueJSON("select * from sp_ServerCheck (" + g.getDistrict().toString() + ",'" + g.getUpazila().toString() + "','" + g.getUnion().toString() + "','" + g.getProvType().toString() + "','" + g.getProvCode().toString() + "')"), ',');

                            String ServerDate = ServerVal[0].toString();
                            String UpdateDT = ServerVal[1].toString();
                           // String HIDRequest = ServerVal[2].toString(); //Health ID Request
                            String TableStructureRequest = ServerVal[3].toString(); //Table structure update request
                            String AreaUpdateRequest = ServerVal[4].toString(); //Area update request
/*
                            C.Save("DROP TABLE IF EXISTS month");
                            C.Save("CREATE TABLE month ( \n" +
                                    "id INT PRIMARY KEY,\n" +
                                    "mName VARCHAR( 50 ));");


                            if (!C.Existence("Select *  from month")) {
                                C.Save("Insert into month(id,mName)Values(1,'January')");
                                C.Save("Insert into month(id,mName)Values(2,'Febuary')");
                                C.Save("Insert into month(id,mName)Values(3,'March')");
                                C.Save("Insert into month(id,mName)Values(4,'April')");
                                C.Save("Insert into month(id,mName)Values(5,'May')");
                                C.Save("Insert into month(id,mName)Values(6,'June')");
                                C.Save("Insert into month(id,mName)Values(7,'July')");
                                C.Save("Insert into month(id,mName)Values(8,'August')");
                                C.Save("Insert into month(id,mName)Values(9,'September')");
                                C.Save("Insert into month(id,mName)Values(10,'October')");
                                C.Save("Insert into month(id,mName)Values(11,'November')");
                                C.Save("Insert into month(id,mName)Values(12,'December')");
                            }*/

                            Cursor hhold = C.ReadData("Select * from HouseholdFPI limit 1");
                            if (hhold.getColumnCount() == 21) {

                                C.Save("alter table HouseholdFPI add causeOfHouseHoldStatusOther varchar(100)");



                            }






                            //Table structure update request: 12 Apr 2015
                        /*    if (TableStructureRequest.equals("1")) {
                                try {
                                    *//*
                                    /*//**ready to use and need to check before use: 12 apr 2015
                                    C.TableStructureSync();
                                    String SQLStr = "Update ProviderDB set TableStructureRequest='2' where  Zillaid='"+ g.getDistrict().toString() +"' and UpazilaID='"+ g.getUpazila().toString() +"' and UnionID='"+ g.getUnion().toString() +"' and ProvType='"+ g.getProvType().toString() +"' and ProvCode='"+ g.getProvCode().toString() +"'";
                                    C.ExecuteCommandOnServer(SQLStr);*//*
                                } catch (Exception ex) {

                                }
                           } */


                            //Area setting update request: 24 May 2015
                            if (AreaUpdateRequest.equals("1")) {
                                /* need revision
                                String Area = C.ReturnSingleValue("select AreaCode from ProviderAssignArea where zillaid='"+ g.getDistrict() +"' and upazilaid='"+ g.getUpazila() +"' and unionid='"+ g.getUnion() +"' and ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"'");
                                String SQLStr="";

                                SQLStr = "Select zillaid, upazilaid, unionid, mouzaid, villageid, FWAUnit, Ward, WardNew, Block, AreaCode from ProviderArea Where ";
                                SQLStr += " ZillaId='"+ g.getDistrict() +"' and";
                                SQLStr += " Upazilaid='"+ g.getUpazila() +"' and";
                                SQLStr += " Unionid='"+ g.getUnion() +"' and";
                                SQLStr += " ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"'";
                                String Res = C.DownloadData(SQLStr ,"ProviderArea","zillaid, upazilaid, unionid, mouzaid, villageid, FWAUnit, Ward, WardNew, Block, AreaCode","Divid,zillaid, upazilaid, unionid, mouzaid, villageid");

                                //Mouza
                                SQLStr  = "select ZILLAID, UPAZILAID, MUNICIPALITYID, UNIONID, MOUZAID, RMO, MOUZANAMEENG, MOUZANAME from Mouza";
                                SQLStr += " where zillaid+upazilaid+unionid+mouzaid in(Select zillaid+upazilaid+unionid+mouzaid from ProviderArea";
                                SQLStr += " ZillaId='"+ g.getDistrict() +"' and";
                                SQLStr += " Upazilaid='"+ g.getUpazila() +"' and";
                                SQLStr += " Unionid='"+ g.getUnion() +"' and";
                                SQLStr += " ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"'";
                                Res = C.DownloadData(SQLStr, "Mouza", "ZILLAID, UPAZILAID, MUNICIPALITYID, UNIONID, MOUZAID, RMO, MOUZANAMEENG, MOUZANAME", "ZillaID,UPAZILAID,UnionId,MOUZAID");

                                //Village
                                SQLStr  = "select ZILLAID, UPAZILAID, UNIONID, MOUZAID, VILLAGEID, RMO, VILLAGENAMEENG, VILLAGENAME, isnull(CRRVILLAGENAME,'')CRRVILLAGENAME";
                                SQLStr += " from Village where zillaid+upazilaid+unionid+mouzaid+villageid in(Select zillaid+upazilaid+unionid+mouzaid+villageid from ProviderArea where";
                                SQLStr += " ZillaId='"+ g.getDistrict() +"' and";
                                SQLStr += " Upazilaid='"+ g.getUpazila() +"' and";
                                SQLStr += " Unionid='"+ g.getUnion() +"' and";
                                SQLStr += " ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"'";
                                Res = C.DownloadData(SQLStr, "Village", "ZILLAID, UPAZILAID, UNIONID, MOUZAID, VILLAGEID, RMO, VILLAGENAMEENG, VILLAGENAME, CRRVILLAGENAME", "ZillaID,UPAZILAID,UnionId,MOUZAID,VillageID");

                                C.ExecuteCommandOnServer("Update ProviderDB set AreaUpdate='2' where  Zillaid='"+ g.getDistrict().toString() +"' and UpazilaID='"+ g.getUpazila().toString() +"' and UnionID='"+ g.getUnion().toString() +"' and ProvType='"+ g.getProvType().toString() +"' and ProvCode='"+ g.getProvCode().toString() +"'");
                                */
                            }


                            //Check for New Version
                            if (!UpdateDT.equals(SystemUpdateDT.replace("/", ""))) {
                                systemDownload d = new systemDownload();
                                d.setContext(getApplicationContext());
                                if (g.getProvType().equals("10"))
                                    d.execute(Global.UpdatedSystemFPI);
                                else if (g.getProvType().equals("11"))
                                    d.execute(Global.UpdatedSystemAHI);
                                else if (g.getProvType().equals("12"))
                                    d.execute(Global.UpdatedSystemHI);
                            } else {
                                //check for system date
                                if (ServerDate.equals(Global.TodaysDateforCheck()) == false) {
                                    Connection.MessageBox(LoginActivity.this, "আপনার ট্যাব এর তারিখ সঠিক নয় [" + Global.DateNowDMY() + "]।");
                                    startActivity(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS));
                                    return;
                                }

                                //Village List Form
                                //...................................................................................

                                final ProgressDialog progDailog = ProgressDialog.show(
                                        LoginActivity.this, "", "অপেক্ষা করুন ...", true);
                                new Thread() {
                                    public void run() {

                                        try {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    finish();
                                                    // C.GenerateElco();
                                                    Intent f1 = new Intent(getApplicationContext(), VillageList.class);
                                                    startActivity(f1);
                                                    progDailog.dismiss();
                                                }
                                            });
                                        } catch (Exception e) {

                                        }
                                    }
                                }.start();
                            }
                        } else {
                            //C.Save("drop table totalmem");
                            //C.Save("drop table headName");
                            // setupInitialDB();
                            //Village List Form
                            //...................................................................................


                            final ProgressDialog progDailog = ProgressDialog.show(
                                    LoginActivity.this, "", "অপেক্ষা করুন ...", true);
                            new Thread() {
                                public void run() {

                                    try {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                finish();
                                                //C.GenerateElco();
                                                Intent f1 = new Intent(getApplicationContext(), VillageList.class);
                                                startActivity(f1);
                                                progDailog.dismiss();
                                            }
                                        });
                                    } catch (Exception e) {

                                    }
                                }
                            }.start();


                        }
                    } catch (Exception ex) {
                        Connection.MessageBox(LoginActivity.this, ex.getMessage());
                        return;
                    }
                }
            });
        } catch (Exception ex) {
            Connection.MessageBox(LoginActivity.this, ex.getMessage());
        }
    }

 /*   private void UpdateProvType() {
        C.Save("Update ProviderDB set ProvType='10' where ProvType='4'");
    }*/

   /* private void setupInitialDB() {
   *//* C.Save("drop table totalmem");
    C.Save("drop table headName");
*//*

        C.Save("delete from PregRefer");
        C.Save("delete from adolescent");
        C.Save("delete from adolescentProblem");
        C.Save("delete from ancService");
        C.Save("delete from clientMap");
        C.Save("delete from death");
        C.Save("delete from delivery");
        C.Save("delete from elco");
        C.Save("delete from elcoVisit");
        C.Save("delete from newBorn");
        C.Save("delete from pncServiceChild");
        C.Save("delete from pncServiceMother");
        C.Save("delete from pregWomen");
        C.Save("delete from stockTransaction");
        C.Save("delete from under5Child");
        C.Save("delete from under5ChildProblem");
        C.Save("delete from under5ChildAdvice");
        C.Save("delete from womanImmunization");
        C.Save("delete from womanInjectable");
        C.Save("delete from immunizationHistory");

        String SQL = "Create table totalmem as";
        SQL += " select dist,upz,un,mouza,vill,hhno,count(*)totalmem from Member";
        SQL += " group by dist,upz,un,mouza,vill,hhno";

        C.Save(SQL);

        SQL = "Create table headName as";
        SQL += " select dist,upz,un,mouza,vill,provtype,provcode,hhno,nameeng headname from Member where rth='01' and length(extype)=0";
        C.Save(SQL);

        SQL = "update household set HHHead=(select headname from headname h where h.dist=household.dist and h.upz=household.upz and h.un=household.un and h.mouza=household.mouza and h.vill=household.vill and  h.hhno=household.hhno)";
        C.Save(SQL);

        SQL = "update household set TotalMem=(select totalmem from totalmem h where h.dist=household.dist and h.upz=household.upz and h.un=household.un and h.mouza=household.mouza and h.vill=household.vill  and h.hhno=household.hhno)";
        C.Save(SQL);


   *//* SQL = "Create table elcodetails as";
    SQL += " select dist,upz,un,mouza,vill, hhno, nameeng headname from Member where rth='01' and length(extype)=0";
    C.Save(SQL);*//*

        SQL = "update household set HHHead=(select headname from headname h where h.dist=household.dist and h.upz=household.upz and h.un=household.un and h.mouza=household.mouza and h.vill=household.vill and  h.hhno=household.hhno)";
        C.Save(SQL);

        C.Save("drop table totalmem");
        C.Save("drop table headName");
        C.Save("INSERT INTO currentStock(providerId,itemCode,stockQty,systemEntryDate,modifyDate,upload)\n" +
                "SELECT (select ProvCode from ProviderDB) AS providerId, A.ItemCode AS ItemCode,  2000 AS stockQty, \n" +
                "'2016-02-11' AS systemEntryDate, NULL AS modifyDate, 1 AS upload\n" +
                "    from Item A\n" +
                "LEFt JOIN currentStock B ON A.ItemCode = B.ItemCode");


    }*/

    //Install application
    private void InstallApplication() {
        File apkfile = new File(Environment.getExternalStorageDirectory() + File.separator + Global.NewVersionName + ".apk");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
        intent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");

        startActivity(intent);
    }


    //Downloading updated system from the central server
    class systemDownload extends AsyncTask<String, String, Void> {
        private Context context;

        public void setContext(Context contextf) {
            context = contextf;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("Downloading Updated System...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.show();
        }


        protected void onProgressUpdate(String... progress) {
            dialog.setProgress(Integer.parseInt(progress[0]));
            //publishProgress(progress);

        }

        //@Override
        protected void onPostExecute(String unused) {
            dialog.dismiss();
        }


        @Override
        protected Void doInBackground(String... arg0) {
            try {
                URL url = new URL(arg0[0]);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.connect();
                int lenghtOfFile = c.getContentLength();

                File file = Environment.getExternalStorageDirectory();

                file.mkdirs();
                File outputFile = new File(file.getAbsolutePath() + File.separator + Global.NewVersionName + ".apk");

                if (outputFile.exists()) {
                    outputFile.delete();
                } else {
                    outputFile.createNewFile();
                }

                FileOutputStream fos = new FileOutputStream(outputFile);

                InputStream is = c.getInputStream();


                byte[] buffer = new byte[1024];
                int len1 = 0;
                long total = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                    count++;
                }
                fos.close();
                is.close();


                InstallApplication();

                dialog.dismiss();

            } catch (IOException e) {
                //Log.e("UpdateAPP", "Update error! " + e.getMessage());
            }
            return null;
        }
    }


    //Download Health ID
    class HealthIDDownload extends AsyncTask<String, String, Void> {
        private Context context;

        public void setContext(Context contextf) {
            context = contextf;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("Downloading Health ID...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.show();
        }


        protected void onProgressUpdate(String... progress) {
            dialog.setProgress(Integer.parseInt(progress[0]));
            //publishProgress(progress);

        }

        //@Override
        protected void onPostExecute(String unused) {
            dialog.dismiss();
        }


        @Override
        protected Void doInBackground(String... arg0) {
            try {
                String[] P = Connection.split(arg0[0], '^');

                //Rebuild database
                //C.RebuildDatabase(P[0], P[1], P[2], P[3], P[4]);
                //HealthIDDB(P[0], P[1], P[2], P[3], P[4]);
                dialog.dismiss();

            } catch (Exception e) {
                //Log.e("UpdateAPP", "Update error! " + e.getMessage());
            }
            return null;
        }
    }

    private void HealthIDDB(String D, String UP, String UN, String PT, String PC) {
        C.DownloadHealthID(D, UP, UN, PT, PC, 500);
    }

    private boolean IsUserHA() {
        String providerType = C.ReturnSingleValue("select ProvType from ProviderDB");
        if (providerType.equalsIgnoreCase("2"))
            return true;
        else
            return false;


    }
}

