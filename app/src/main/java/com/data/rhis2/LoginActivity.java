package com.data.rhis2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import Common.Connection;
import Common.Global;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import DataSync.SyncScheduler;

public class LoginActivity extends Activity{
    Connection C;
    Global g;
    boolean netwoekAvailable=false;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog dialog;
    int count = 0;

    TextView lblStaffType;
    String   SystemUpdateDT="";
    private  String Password="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.login);
            C = new Connection(this);
            g = Global.getInstance();

            //C.CreateTable("Death","Create Table Death(Dist Varchar(2), Upz Varchar(2), UN Varchar(2), Mouza Varchar(3), Vill Varchar(2), ProvType varchar(2), ProvCode varchar(5), HHNo Varchar(5), SNo Varchar(2),DeathDT varchar(10), DeathCause varchar(5),EnDt Varchar(20), Upload Varchar(1))");
            //C.CreateTable("PregWomen","Create Table PregWomen(Dist Varchar(2), Upz Varchar(2), UN Varchar(2), Mouza Varchar(3), Vill Varchar(2),HHNo Varchar(5), SNo Varchar(2),PGN varchar(2),ELCONo Varchar(4),DOLMP Varchar(10),DOEDD Varchar(10),PrePregTimes Varchar(1),LCAge Varchar(2),RegDT Varchar(10), EnDt Varchar(20), Upload Varchar(1))");
            //C.CreateTable("ANC","Create Table ANC(Dist Varchar(2), Upz Varchar(2), UN Varchar(2), Mouza Varchar(3), Vill Varchar(2), HHNo Varchar(5), SNo Varchar(2),PGN varchar(2),Visit varchar(4),VDate varchar(10),IronFolStatus varchar(1),IronFolQty Varchar(3),IronFolUnit Varchar(1),MisoStatus varchar(1),MisoQty Varchar(3),MisoUnit Varchar(1),EnDt Varchar(20), Upload Varchar(1))");

            final Spinner uid      = (Spinner)findViewById(R.id.userId);
            final EditText pass    = (EditText)findViewById(R.id.pass);
            TextView lblSystemDate = (TextView)findViewById(R.id.lblSystemDate);

            //Need to update date every time whenever shared updated system
            //Format: DDMMYYYY
            //*********************************************************************
            SystemUpdateDT = "22062015";
            lblSystemDate.setText("Version: "+ SystemUpdateDT + "(" + Global.Organization + ")");

            //Only for removing the data of training
            //if(Global.DateNowDMY().equals("04/03/2015"))
            //{
            //C.Save("Delete from Member where endt<'2015-03-04'");
            //C.Save("Delete from Household where endt<'2015-03-04'");
            //C.Save("Update HealthIDRepository set Status='1'");
            //}

            //Check for Internet connectivity
            if (Connection.haveNetworkConnection(LoginActivity.this)) {
                netwoekAvailable=true;
            } else {
                netwoekAvailable=false;
            }

            //Rebuild Database
            String TotalTab = C.ReturnSingleValue("SELECT count(*) FROM sqlite_master WHERE type = 'table' AND name != 'android_metadata' AND name != 'sqlite_sequence'");
            if(Integer.valueOf(TotalTab) == 0)
            {
                if(netwoekAvailable)
                {
                    //Call Setting Form
                    finish();
                    Intent f1 = new Intent(getApplicationContext(),SettingForm.class);
                    startActivity(f1);
                    return;
                }
                else
                {
                    Connection.MessageBox(LoginActivity.this,"Internet connection is not available for building initial database.");
                    //finish();
                    //System.exit(0);
                    return;
                }
            }

            //Download Table List form Server for Generating Tables
            //25 mar 2015
            //C.DatabaseTableSync("ElcoEvent");
            /*
            C.CreateTable("ElcoEvent","Create table ElcoEvent(EVCode varchar(2) primary key,EVName nvarchar(100))");

            C.Save("Insert into ElcoEvent values('01','খাবার বড়ি')");
            C.Save("Insert into ElcoEvent values('02','কনডম')");
            C.Save("Insert into ElcoEvent values('03','ইনজেকটেবল')");
            C.Save("Insert into ElcoEvent values('04','আই ইউ ডি')");
            C.Save("Insert into ElcoEvent values('05','ইমপ্যান্ট')");
            C.Save("Insert into ElcoEvent values('06','স্থায়ী পদ্ধতি(পুরুষ)')");
            C.Save("Insert into ElcoEvent values('07','স্থায়ী পদ্ধতি(মহিলা)')");
            C.Save("Insert into ElcoEvent values('08','ইসিপি ')");
            C.Save("Insert into ElcoEvent values('09','মিসোপ্রোস্টোল')");
            C.Save("Insert into ElcoEvent values('10','পার্শ্ব-প্রতিক্রিয়ার জন্য প্রেরন ')");
            C.Save("Insert into ElcoEvent values('11','পদ্ধতির জন্য প্রেরন')");
            C.Save("Insert into ElcoEvent values('12','গর্ভবতী ')");
            C.Save("Insert into ElcoEvent values('13','জীবিত জন্ম ')");
            C.Save("Insert into ElcoEvent values('14','গর্ভ খালাস (জীবিত জন্ম ছাড়া)')");
            C.Save("Insert into ElcoEvent values('15','জরায়ু অপারেশন করে অপসারন')");
            C.Save("Insert into ElcoEvent values('16','স্বামী বিদেশ থাকলে ')");
            C.Save("Insert into ElcoEvent values('17','বন্ধ্যাত্ব বিষয়ক তথ্য ')");
            C.Save("Insert into ElcoEvent values('18','অন্য যে কোন অবস্থা ')");
            */

            //Assign Global Variable
            String Area = C.ReturnSingleValue("Select Divid||'^'||zillaid||'^'||upazilaid||'^'||unionid||'^'||ProvType||'^'||ProvCode||'^'||ProvName from ProviderDB where Active='1'");
            String[] A = Connection.split(Area,'^');
            g.setDivision(A[0]);
            g.setDistrict(A[1]);
            g.setUpazila(A[2]);
            g.setUnion(A[3]);
            g.setLoginProvType(A[4]);
            g.setLoginProvCode(A[5]);
            g.setProvType(A[4]);
            g.setProvCode(A[5]);



            String DeviceNo = C.ReturnSingleValue("Select DeviceNo from DeviceNo");
            g.setDeviceNo(DeviceNo);

            uid.setAdapter(C.getArrayAdapter("Select UserId||'-'||UserName from Login"));
            uid.setSelection(Global.SpinnerItemPosition( uid, 3, C.ReturnSingleValue( "Select LoginID from LastLogin" ) ));

            //***********************************************
            //netwoekAvailable=false;
            //***********************************************

            Button btnClose=(Button)findViewById(R.id.btnClose);
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
                    try
                    {
                        String[] U = Connection.split(uid.getSelectedItem().toString(),'-');
                        g.setUserID(U[0]);

                        //use for administration: 12 Mar 2015
                        if(pass.getText().toString().equals("")) {
                            //Call Setting Form
                            //finish();
                            //Intent f1 = new Intent(getApplicationContext(), SettingForm.class);
                            //startActivity(f1);
                        }

                        //stop for development
                        if(!pass.getText().toString().equals(""))
                        {
                            Connection.MessageBox(LoginActivity.this,"পাসওয়ার্ড সঠিক নয়, পুনরায় চেষ্টা করুন।");
                            pass.requestFocus();
                            return;
                        }


                        //Store Last Login information
                        C.Save("Delete from LastLogin");
                        C.Save("Insert into LastLogin(LoginID)Values('"+ Global.Left(uid.getSelectedItem().toString(),3) +"')");


                        if (Connection.haveNetworkConnection(LoginActivity.this)) {
                            netwoekAvailable=true;
                        } else {
                            netwoekAvailable=false;
                        }

                        //Download Updated System
                        //...................................................................................
                        if(netwoekAvailable==true)
                        {
                            //Retrieve data from server for checking local device
                            String[] ServerVal  = Connection.split(C.ReturnResult("ReturnSingleValue","sp_ServerCheck '"+ g.getDistrict().toString() +"','"+ g.getUpazila().toString() +"','"+ g.getUnion().toString() +"','"+ g.getProvType().toString() +"','"+ g.getProvCode().toString() +"'"),',');
                            String ServerDate            = ServerVal[0].toString();
                            String UpdateDT              = ServerVal[1].toString();
                            String HIDRequest            = ServerVal[2].toString(); //Health ID Request
                            String TableStructureRequest = ServerVal[3].toString(); //Table structure update request
                            String AreaUpdateRequest     = ServerVal[4].toString(); //Area update request

                            //Table structure update request: 12 Apr 2015
                            if(TableStructureRequest.equals("1"))
                            {
                                try
                                {
                                /*
                                //**ready to use and need to check before use: 12 apr 2015
                                C.TableStructureSync();
                                String SQLStr = "Update ProviderDB set TableStructureRequest='2' where  Zillaid='"+ g.getDistrict().toString() +"' and UpazilaID='"+ g.getUpazila().toString() +"' and UnionID='"+ g.getUnion().toString() +"' and ProvType='"+ g.getProvType().toString() +"' and ProvCode='"+ g.getProvCode().toString() +"'";
                                C.ExecuteCommandOnServer(SQLStr);*/
                                }
                                catch(Exception ex)
                                {

                                }

                            /*try
                            {
                                C.Save("Alter table Visits Rename to Visits1");
                                C.TableStructureSync("Visits1");
                                C.Save("Alter table Visits1 Rename to Visits");

                                //C.Save("Alter table Household Rename to Household1");
                                C.TableStructureSync("Household");
                                //C.Save("Alter table Member Rename to Member1");
                                C.TableStructureSync("Member");

                                String SQLStr = "Update ProviderDB set TableStructureRequest='2' where  Zillaid='"+ g.getDistrict().toString() +"' and UpazilaID='"+ g.getUpazila().toString() +"' and UnionID='"+ g.getUnion().toString() +"' and ProvType='"+ g.getProvType().toString() +"' and ProvCode='"+ g.getProvCode().toString() +"'";
                                C.ExecuteCommandOnServer(SQLStr);
                            } catch (Exception e) {
                            }*/
                            }


                            //Area setting update request: 24 May 2015
                            if(AreaUpdateRequest.equals("1"))
                            {
                                String Area = C.ReturnSingleValue("select AreaCode from ProviderAssignArea where zillaid='"+ g.getDistrict() +"' and upazilaid='"+ g.getUpazila() +"' and unionid='"+ g.getUnion() +"' and ProvType='"+ g.getProvType() +"' and ProvCode='"+ g.getProvCode() +"'");
                                String SQLStr="";

                                SQLStr = "Select Divid, zillaid, upazilaid, unionid, mouzaid, villageid, FWAUnit, Ward, WardNew, Block, AreaCode from ProviderArea Where ";
                                SQLStr += " ZillaId='"+ g.getDistrict() +"' and";
                                SQLStr += " Upazilaid='"+ g.getUpazila() +"' and";
                                SQLStr += " Unionid='"+ g.getUnion() +"' and";
                                SQLStr += " AreaCode='"+ Area +"'";
                                String Res = C.DownloadData(SQLStr ,"ProviderArea","Divid, zillaid, upazilaid, unionid, mouzaid, villageid, FWAUnit, Ward, WardNew, Block, AreaCode","Divid,zillaid, upazilaid, unionid, mouzaid, villageid");

                                //Mouza
                                SQLStr  = "select ZILLAID, UPAZILAID, MUNICIPALITYID, UNIONID, MOUZAID, RMO, MOUZANAMEENG, MOUZANAME from Mouza";
                                SQLStr += " where zillaid+upazilaid+unionid+mouzaid in(Select zillaid+upazilaid+unionid+mouzaid from ProviderArea";
                                SQLStr += " ZillaId='"+ g.getDistrict() +"' and";
                                SQLStr += " Upazilaid='"+ g.getUpazila() +"' and";
                                SQLStr += " Unionid='"+ g.getUnion() +"' and";
                                SQLStr += " AreaCode='"+ Area +"')";
                                Res = C.DownloadData(SQLStr, "Mouza", "ZILLAID, UPAZILAID, MUNICIPALITYID, UNIONID, MOUZAID, RMO, MOUZANAMEENG, MOUZANAME", "ZillaID,UPAZILAID,UnionId,MOUZAID");

                                //Village
                                SQLStr  = "select ZILLAID, UPAZILAID, UNIONID, MOUZAID, VILLAGEID, RMO, VILLAGENAMEENG, VILLAGENAME, isnull(CRRVILLAGENAME,'')CRRVILLAGENAME";
                                SQLStr += " from Village where zillaid+upazilaid+unionid+mouzaid+villageid in(Select zillaid+upazilaid+unionid+mouzaid+villageid from ProviderArea where";
                                SQLStr += " ZillaId='"+ g.getDistrict() +"' and";
                                SQLStr += " Upazilaid='"+ g.getUpazila() +"' and";
                                SQLStr += " Unionid='"+ g.getUnion() +"' and";
                                SQLStr += " AreaCode='"+ Area +"')";
                                Res = C.DownloadData(SQLStr, "Village", "ZILLAID, UPAZILAID, UNIONID, MOUZAID, VILLAGEID, RMO, VILLAGENAMEENG, VILLAGENAME, CRRVILLAGENAME", "ZillaID,UPAZILAID,UnionId,MOUZAID,VillageID");

                                C.ExecuteCommandOnServer("Update ProviderDB set AreaUpdate='2' where  Zillaid='"+ g.getDistrict().toString() +"' and UpazilaID='"+ g.getUpazila().toString() +"' and UnionID='"+ g.getUnion().toString() +"' and ProvType='"+ g.getProvType().toString() +"' and ProvCode='"+ g.getProvCode().toString() +"'");
                            }

                            //Update provider type and code for blank database : 30 Mar 2015
                            //C.Save("Update Visits    Set ProvType='"+ g.getProvType() +"',ProvCode='"+ g.getProvCode() +"' where length(ProvType)=0 or ProvType is null or ProvType='null'");
                            //C.Save("Update Household Set ProvType='"+ g.getProvType() +"',ProvCode='"+ g.getProvCode() +"' where length(ProvType)=0 or ProvType is null or ProvType='null'");
                            //C.Save("Update Member    Set ProvType='"+ g.getProvType() +"',ProvCode='"+ g.getProvCode() +"' where length(ProvType)=0 or ProvType is null or ProvType='null'");



                            //Download Health ID from Server
                            if(HIDRequest.equals("1")) {
                                try {
                                    //Sync Health ID
                                    C.DownloadHealthID(g.getDistrict().toString(), g.getUpazila().toString(), g.getUnion().toString(), g.getProvType().toString(), g.getProvCode().toString());
                                } catch (Exception e) {
                                }
                            }

                            //Check for New Version
                            if (!UpdateDT.equals(SystemUpdateDT)) {
                                systemDownload d = new systemDownload();
                                d.setContext(getApplicationContext());
                                d.execute(Global.UpdatedSystem);
                            }
                            else
                            {
                                //check for system date
                                if(ServerDate.equals(Global.TodaysDateforCheck())==false)
                                {
                                    Connection.MessageBox(LoginActivity.this, "আপনার ট্যাব এর তারিখ সঠিক নয় ["+ Global.DateNowDMY() +"]।");
                                    startActivity(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS));
                                    return;
                                }


                                //Village List Form
                                //...................................................................................
                                finish();
                                Intent f1 = new Intent(getApplicationContext(),VillageList.class);
                                startActivity(f1);
                            }
                        }
                        else
                        {
                            //Village List Form
                            //...................................................................................
                            finish();
                            Intent f1 = new Intent(getApplicationContext(),VillageList.class);
                            startActivity(f1);
                        }
                    }
                    catch(Exception ex)
                    {
                        Connection.MessageBox(LoginActivity.this, ex.getMessage());
                        return;
                    }
                }
            });
        }
        catch(Exception ex)
        {
            Connection.MessageBox(LoginActivity.this, ex.getMessage());
        }
    }

    //Install application
    private void InstallApplication()
    {
        File apkfile = new File(Environment.getExternalStorageDirectory() + File.separator + Global.NewVersionName +".apk");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
        intent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");

        startActivity(intent);
    }


    //Downloading updated system from the central server
    class systemDownload extends AsyncTask<String,String,Void>{
        private Context context;

        public void setContext(Context contextf){
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
            publishProgress(progress);

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

                File file=Environment.getExternalStorageDirectory();

                file.mkdirs();
                File outputFile = new File(file.getAbsolutePath()+ File.separator + Global.NewVersionName +".apk");

                if(outputFile.exists()){
                    outputFile.delete();
                }
                else
                {
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
    class HealthIDDownload extends AsyncTask<String,String,Void>{
        private Context context;

        public void setContext(Context contextf){
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
            publishProgress(progress);

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

    private void HealthIDDB(String D,String UP,String UN,String PT,String PC)
    {
        C.DownloadHealthID(D, UP, UN, PT, PC);
    }
}

