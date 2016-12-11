package com.data.rhis2;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.IBinder;

import Common.Connection;
import Common.Global;

/*
 * Created by TanvirHossain on 08/03/2015.
 */
public class DataSyncService extends Service {
    Connection C;
    Global g;
    private NotificationManager mManager;

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    /*@Override
    public void onCreate()
    {
        // TODO Auto-generated method stub
        super.onCreate();
        C=new Connection(this);
    }*/

    private void handleIntent(Intent intent) {
        // obtain the wake lock
        /*
                PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
                mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, Const.TAG);
                mWakeLock.acquire();
        */

        // check the global background data setting
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (!cm.getBackgroundDataSetting()) {
            stopSelf();
            return;
        }

        C = new Connection(this);
        g = Global.getInstance();
        // do the actual work, in a separate thread
        new DataSyncTask().execute();
    }

    private class DataSyncTask extends AsyncTask<Void, Void, Void> {
        /**
         * This is where YOU do YOUR work. There's nothing for me to write here
         * you have to fill this in. Make your HTTP request(s) or whatever it is
         * you have to do to get your updates in here, because this is run in a
         * separate thread
         */
        @Override
        protected Void doInBackground(Void... params) {
            String TableName = "";
            String VariableList = "";
            String ColumnList = "";
            String UniqueField = "";

            String ResponseString = "Status:";
            String response;

            try {
                new Thread() {
                    public void run() {
                        String TableName = "";
                        String VariableList = "";
                        String ColumnList = "";
                        String UniqueField = "";

                        String ResponseString = "Status:";
                        String response;

                        try {
                            //Data upload to central server
                            /*TableName = "household";
                            VariableList = "dist,upz,un,mouza,vill,paddr,permaaddress,provtype,provcode,hhno,religion,vgfcard,subblock,unit,hhhead,totalmem,starttime,endtime,lat,lon,userid,endt,upload";
                            response = C.UploadJSON(TableName , VariableList , "dist, upz, un, mouza, vill,provtype,provcode, hhno");

                            TableName = "member";
                            VariableList = "dist,upz,un,mouza,vill,provtype,provcode,hhno,sno,healthid,nameeng,namebang,rth,havenid,nid,nidstatus,havebr,brid,bridstatus,mobileno1,mobileno2,mobileyn,dob,age,dobsource,bplace,fno,father,fdontknow,mno,mother,mdontknow,sex,ms,spno1,spno2,spno3,spno4,elcono,elcodontknow,edu,rel,nationality,ocp,starttime,entype,endate,extype,exdate,endtime,lat,lon,userid,endt,upload";
                            response = C.UploadJSON(TableName , VariableList , "dist, upz, un, mouza, vill,provtype,provcode, hhno, sno");

                            TableName = "visits";
                            VariableList = "dist,upz,un,mouza,vill,provtype,provcode,hhno,vdate,vstatus,starttime,endtime,lat,lon,userid,endt,upload";
                            response = C.UploadJSON(TableName , VariableList , "dist, upz, un, mouza, vill,provtype,provcode, hhno, vdate");

                            TableName = "ses";
                            VariableList = "dist,upz,un,mouza,vill,provtype,provcode,hhno,status,q1,q11,q2,q21,q3a,q3b,q3c,q3d,q3e,q3f,q3g,q3h,q3i,q3j,q3k,q3l,q3m,q3n,q3o,q3p,q4,q41,q5,q51,q6,q61,q7,q71,q8a,q8b,q8c,q8d,q8e,starttime,endtime,userid,endt,upload";
                            response = C.UploadJSON(TableName , VariableList , "dist, upz, un, mouza, vill,provtype,provcode, hhno");
*/
                            UploadData();
                            // Download();

                        } catch (Exception e) {

                        }
                    }
                }.start();
                //Sync Database

            } catch (Exception e) {

            }

            // do stuff!
            return null;
        }

        /**
         * In here you should interpret whatever you fetched in doInBackground
         * and push any notifications you need to the status bar, using the
         * NotificationManager. I will not cover this here, go check the docs on
         * NotificationManager.
         * <p>
         * What you HAVE to do is call stopSelf() after you've pushed your
         * notification(s). This will:
         * 1) Kill the service so it doesn't waste precious resources
         * 2) Call onDestroy() which will release the wake lock, so the device
         * can go to sleep again and save precious battery.
         */
        @Override
        protected void onPostExecute(Void result) {
            // handle your data
            stopSelf();
        }
    }

    /* private void Download()
     {
         final String ProvCode = g.getProvCode();

         final String Dist = g.getDistrict();
         final String Upz = g.getUpazila();
         final String UN = g.getUnion();
         final String ProvType = g.getProvType();

         C.DownloadServiceTables(ProvCode);

         String SQLStr = "Select zillaid,upazilaid,unionid,\"ProvType\",\"ProvCode\",\"ProvName\",\"EnDate\",\"ExDate\",\"Active\",\"DeviceSetting\" from \"ProviderDB\" where ";
         SQLStr += " zillaid='" + Dist + "' and";
         SQLStr += " upazilaid='" + Upz + "' and";
         SQLStr += " unionid='" + UN + "' and";
         SQLStr += " \"ProvType\"='" + ProvType + "' and";
         SQLStr += " \"ProvCode\"='" + ProvCode + "' and";
         SQLStr += " \"Active\"='1'";
         String Res = C.DownloadJSON(SQLStr, "ProviderDB", "zillaid,upazilaid,unionid,ProvType,ProvCode,ProvName,EnDate,ExDate,Active,DeviceSetting", "zillaid,upazilaid,unionid,ProvType,ProvCode");

         //Service Provider Area
         SQLStr = "Select a.zillaid, a.upazilaid, a.unionid, a.mouzaid, a.villageid, a.\"FWAUnit\", a.\"Ward\", a.\"WardNew\", a.\"Block\",a.\"provType\",a.\"provCode\"";
         SQLStr += " from \"Village\" v";
         SQLStr += " inner join \"ProviderArea\" a on v.\"ZILLAID\"=a.zillaid and v.\"UPAZILAID\"=a.upazilaid and v.\"UNIONID\"=a.unionid and v.\"MOUZAID\"=a.mouzaid and v.\"VILLAGEID\"=a.villageid";
         SQLStr += " Where a.zillaid='" + Dist + "' and";
         SQLStr += " a.upazilaid='" + Upz + "' and";
         SQLStr += " a.unionid='" + UN + "' and";
         SQLStr += " a.\"provType\"='" + ProvType + "' and";
         SQLStr += " a.\"provCode\"='" + ProvCode + "'";

         Res = C.DownloadJSON(SQLStr, "ProviderArea", "zillaid, upazilaid, unionid, mouzaid, villageid, FWAUnit, Ward, WardNew, Block,provType,provCode", "zillaid, upazilaid, unionid, mouzaid, villageid");

         //DeviceNo
         //Save("Delete from DeviceNo");
         //Save("Insert into DeviceNo(DeviceNo)Values('" + (ProvType + ProvCode) + "')");

         //Login
         SQLStr = "Select \"ProvCode\",\"ProvName\",''Pass from \"ProviderDB\" Where ";
         SQLStr += " zillaid='" + Dist + "' and";
         SQLStr += " upazilaid='" + Upz + "' and";
         SQLStr += " unionid='" + UN + "' and";
         SQLStr += " \"ProvType\"='" + ProvType + "' and";
         SQLStr += " \"ProvCode\"='" + ProvCode + "' and";
         SQLStr += " \"Active\"='1'";
         Res = C.DownloadJSON(SQLStr, "Login", "UserId,UserName,Pass", "UserId");


         //Division
         SQLStr = "SELECT * FROM \"Division\"";
         Res = C.DownloadJSON(SQLStr, "Division", "id, division", "id");


         //District
         SQLStr = "Select \"DIVID\", \"ZILLAID\", \"ZILLANAMEENG\", \"ZILLANAME\" from \"Zilla\"";
         Res = C.DownloadJSON(SQLStr, "Zilla", "DIVID, ZILLAID, ZILLANAMEENG, ZILLANAME", "ZillaID");

         //Upazila
         SQLStr = "Select \"ZILLAID\", \"UPAZILAID\", \"UPAZILANAMEENG\", \"UPAZILANAME\" from \"Upazila\" where \"ZILLAID\"='" + Dist + "' and \"UPAZILAID\"='" + Upz + "'";
         Res = C.DownloadJSON(SQLStr, "Upazila", "ZILLAID, UPAZILAID, UPAZILANAMEENG, UPAZILANAME", "ZillaID,UPAZILAID");

         //Unions
         SQLStr = "Select \"ZILLAID\", \"UPAZILAID\", \"MUNICIPALITYID\", \"UNIONID\", \"UNIONNAMEENG\", \"UNIONNAME\" from \"Unions\" where \"ZILLAID\"='" + Dist + "' and \"UPAZILAID\"='" + Upz + "' and \"UNIONID\"='" + UN + "'";
         Res = C.DownloadJSON(SQLStr, "Unions", "ZILLAID, UPAZILAID, MUNICIPALITYID, UNIONID, UNIONNAMEENG, UNIONNAME", "ZillaID,UPAZILAID,UnionId");

         //Mouza
         SQLStr = "Select m.\"ZILLAID\", m.\"UPAZILAID\", m.\"MUNICIPALITYID\", m.\"UNIONID\", m.\"MOUZAID\", m.\"RMO\", m.\"MOUZANAMEENG\", m.\"MOUZANAME\" from \"Mouza\" m";
         SQLStr += " inner join \"ProviderArea\" a on m.\"ZILLAID\"=a.zillaid and m.\"UPAZILAID\"=a.upazilaid and m.\"UNIONID\"=a.unionid and m.\"MOUZAID\"=a.mouzaid";
         SQLStr += " where a.\"provType\"='" + ProvType + "' and a.\"provCode\"='" + ProvCode + "' and m.\"ZILLAID\"='" + Dist + "' and m.\"UPAZILAID\"='" + Upz + "' and m.\"UNIONID\"='" + UN + "'";
         Res = C.DownloadJSON(SQLStr, "Mouza", "ZILLAID, UPAZILAID, MUNICIPALITYID, UNIONID, MOUZAID, RMO, MOUZANAMEENG, MOUZANAME", "ZillaID,UPAZILAID,UnionId,MOUZAID");

         //Village
         SQLStr = "Select v.\"ZILLAID\", v.\"UPAZILAID\", v.\"UNIONID\", v.\"MOUZAID\", v.\"VILLAGEID\", v.\"RMO\", v.\"VILLAGENAMEENG\", v.\"VILLAGENAME\", coalesce(v.\"CRRVILLAGENAME\",'')CRRVILLAGENAME from \"Village\" v";
         SQLStr += " inner join \"ProviderArea\" a on v.\"ZILLAID\"=a.zillaid and v.\"UPAZILAID\"=a.upazilaid and v.\"UNIONID\"=a.unionid and v.\"MOUZAID\"=a.mouzaid and v.\"VILLAGEID\"=a.villageid";
         SQLStr += " where a.\"provType\"='" + ProvType + "' and a.\"provCode\"='" + ProvCode + "' and v.\"ZILLAID\"='" + Dist + "' and v.\"UPAZILAID\"='" + Upz + "' and v.\"UNIONID\"='" + UN + "'";
         Res = C.DownloadJSON(SQLStr, "Village", "ZILLAID, UPAZILAID, UNIONID, MOUZAID, VILLAGEID, RMO, VILLAGENAMEENG, VILLAGENAME, CRRVILLAGENAME", "ZillaID,UPAZILAID,UnionId,MOUZAID,VillageID");


         //Update Device Setting Status in Server DB
             *//*
            SQLStr  = "Update \"ProviderDB\" Set \"DeviceSetting\"='2' Where ";
            SQLStr += " zillaid='"+ Dist +"' and";
            SQLStr += " upazilaid='"+ Upz +"' and";
            SQLStr += " unionid='"+ UN +"' and";
            SQLStr += " \"ProvType\"='"+ ProvType +"' and";
            SQLStr += " \"ProvCode\"='"+ ProvCode +"' and";
            SQLStr += " \"Active\"='1'";
            ExecuteCommandOnServer(SQLStr);
            *//*
//ses
        SQLStr = "Select C.*\n" +
                "from \"Village\" v\n" +
                "INNER JOIN \"ProviderArea\" a on v.\"ZILLAID\" = a.zillaid\n" +
                "\tand v.\"UPAZILAID\" = a.upazilaid\n" +
                "\tand v.\"UNIONID\" = a.unionid\n" +
                "\tand v.\"MOUZAID\" = a.mouzaid\n" +
                "\tand v.\"VILLAGEID\" = a.villageid\n" +
                "INNER JOIN \"ses\" C on a.zillaid = CAST(C.\"dist\" as Integer)\n" +
                "\tand a.upazilaid = CAST(C.\"upz\" as Integer)\n" +
                "\tand a.unionid = CAST(C.\"un\" as Integer)\n" +
                "\tand a.mouzaid = CAST(C.\"mouza\" as Integer)\n" +
                "\tand a.villageid = CAST(C.\"vill\" as Integer)\n" +
                "where a.\"provType\" = '" + ProvType + "'\n" +
                "\tand a.\"provCode\" = '" + ProvCode + "'\n" +
                "\tand v.\"ZILLAID\" = '" + Dist + "'\n" +
                "\tand v.\"UPAZILAID\" = '" + Upz + "'\n" +
                "\tand v.\"UNIONID\" = '" + UN + "'\n";


        Res = C.DownloadJSON(SQLStr, "ses", "dist, upz, un, mouza, vill, provType,provCode,hhNo,status,q1,q11,q2 ,q21, q3a,q3b,q3c,q3d, q3e, q3f,q3g,q3h, q3i, q3j, q3k, q3l, q3m, q3n, q3o, q3p, q4, q41, q5, q51, q6, q61, q7, q71, q8a, q8b, q8c, q8d, q8e, Q9, Q10, startTime, endTime, userId, enDt, upload", "dist, upz, un, mouza, vill, hhNo");


        //Household data
        SQLStr = "Select h.\"Dist\", h.\"Upz\", h.\"UN\", h.\"Mouza\", h.\"Vill\", h.\"PAddr\", h.\"PermaAddress\", h.\"ProvType\", h.\"ProvCode\", h.\"HHNo\", h.\"Religion\", h.\"VGFCard\",";
        SQLStr += " h.\"subBlock\",h.\"unit\", h.\"StartTime\", h.\"EndTime\", h.\"Lat\", h.\"Lon\", h.\"UserId\", h.\"EnDt\", '1' Upload";
        SQLStr += " from \"Village\" v";
        SQLStr += " inner join \"ProviderArea\" a on v.\"ZILLAID\"=a.zillaid and v.\"UPAZILAID\"=a.upazilaid and v.\"UNIONID\"=a.unionid and v.\"MOUZAID\"=a.mouzaid and v.\"VILLAGEID\"=a.villageid";
        SQLStr += " inner join \"Household\" h on a.zillaid=h.\"Dist\" and a.upazilaid=h.\"Upz\" and a.unionid=h.\"UN\" and a.mouzaid=h.\"Mouza\" and a.villageid=h.\"Vill\"";
        SQLStr += " where a.\"provType\"='" + ProvType + "' and a.\"provCode\"='" + ProvCode + "' and v.\"ZILLAID\"='" + Dist + "' and v.\"UPAZILAID\"='" + Upz + "' and v.\"UNIONID\"='" + UN + "'";
            *//*Res = DownloadJSON(SQLStr, "Household", "Dist, Upz, UN, Mouza, Vill, PAddr, PermaAddress, ProvType, ProvCode, HHNo, Religion, VGFCard,subBlock,unit, StartTime, EndTime, Lat, Lon, UserId, EnDt, Upload", "Dist, Upz, UN, Mouza, Vill, ProvType, ProvCode, HHNo");*//*
        Res = C.DownloadJSON(SQLStr, "Household", "Dist, Upz, UN, Mouza, Vill, PAddr, PermaAddress, ProvType, ProvCode, HHNo, Religion, VGFCard,subBlock,unit, StartTime, EndTime, Lat, Lon, UserId, EnDt, Upload", "Dist, Upz, UN, Mouza, Vill, HHNo");

        //Member Data
        SQLStr = " Select h.\"Dist\", h.\"Upz\", h.\"UN\", h.\"Mouza\", h.\"Vill\", h.\"ProvType\", h.\"ProvCode\", h.\"HHNo\", h.\"SNo\", h.\"HealthID\", h.\"NameEng\", h.\"NameBang\", h.\"Rth\", h.\"HaveNID\", h.\"NID\", h.\"NIDStatus\", h.\"HaveBR\",";
        SQLStr += " h.\"BRID\", h.\"BRIDStatus\", h.\"MobileNo1\", h.\"MobileNo2\",h.mobileyn, h.\"DOB\", h.\"Age\", h.\"DOBSource\", h.\"BPlace\", h.\"FNo\", h.\"Father\", h.\"FDontKnow\", h.\"MNo\", h.\"Mother\", h.\"MDontKnow\", h.\"Sex\", h.\"MS\", h.\"SPNO1\",";
        SQLStr += " h.\"SPNO2\", h.\"SPNO3\", h.\"SPNO4\", h.\"ELCONo\", h.\"ELCODontKnow\", h.\"EDU\", h.\"Rel\", h.\"Nationality\", h.\"OCP\", h.\"StartTime\", h.\"EnType\", h.\"EnDate\", coalesce(h.\"ExType\", '')  AS \"ExType\", h.\"ExDate\", h.\"EndTime\", h.\"Lat\", h.\"Lon\", h.\"UserId\", h.\"EnDt\", '1' Upload";
        SQLStr += " from \"Village\" v";
        SQLStr += " inner join \"ProviderArea\" a on v.\"ZILLAID\"=a.zillaid and v.\"UPAZILAID\"=a.upazilaid and v.\"UNIONID\"=a.unionid and v.\"MOUZAID\"=a.mouzaid and v.\"VILLAGEID\"=a.villageid";
        SQLStr += " inner join \"Member\" h on a.zillaid=h.\"Dist\" and a.upazilaid=h.\"Upz\" and a.unionid=h.\"UN\" and a.mouzaid=h.\"Mouza\" and a.villageid=h.\"Vill\"";
        SQLStr += " where a.\"provType\"='" + ProvType + "' and a.\"provCode\"='" + ProvCode + "' and v.\"ZILLAID\"='" + Dist + "' and v.\"UPAZILAID\"='" + Upz + "' and v.\"UNIONID\"='" + UN + "'";
            *//*Res = DownloadJSON(SQLStr, "Member", "Dist, Upz, UN, Mouza, Vill, ProvType, ProvCode, HHNo, SNo, HealthID, NameEng, NameBang, Rth, HaveNID, NID, NIDStatus, HaveBR, BRID, BRIDStatus, MobileNo1, MobileNo2,MobileYN, DOB, Age, DOBSource, BPlace, FNo, Father, FDontKnow, MNo, Mother, MDontKnow, Sex, MS, SPNO1, SPNO2, SPNO3, SPNO4, ELCONo, ELCODontKnow, EDU, Rel, Nationality, OCP, StartTime, EnType, EnDate, ExType, ExDate, EndTime, Lat, Lon, UserId, EnDt, Upload", "Dist, Upz, UN, Mouza, Vill, ProvType, ProvCode, HHNo, SNo");*//*
        Res = C.DownloadJSON(SQLStr, "Member", "Dist, Upz, UN, Mouza, Vill, ProvType, ProvCode, HHNo, SNo, HealthID, NameEng, NameBang, Rth, HaveNID, NID, NIDStatus, HaveBR, BRID, BRIDStatus, MobileNo1, MobileNo2,MobileYN, DOB, Age, DOBSource, BPlace, FNo, Father, FDontKnow, MNo, Mother, MDontKnow, Sex, MS, SPNO1, SPNO2, SPNO3, SPNO4, ELCONo, ELCODontKnow, EDU, Rel, Nationality, OCP, StartTime, EnType, EnDate, ExType, ExDate, EndTime, Lat, Lon, UserId, EnDt, Upload", "Dist, Upz, UN, Mouza, Vill, HHNo, SNo");

        //Visits Data
        SQLStr = " Select dist, upz, un, mouza, vill, h.\"provType\", h.\"provCode\", \"hhNo\", \"vDate\", \"vStatus\", \"startTime\", \"endTime\", \"lat\", \"lon\", \"userId\", \"enDt\",'1' Upload";
        SQLStr += " from \"Village\" v";
        SQLStr += " inner join \"ProviderArea\" a on v.\"ZILLAID\"=a.zillaid and v.\"UPAZILAID\"=a.upazilaid and v.\"UNIONID\"=a.unionid and v.\"MOUZAID\"=a.mouzaid and v.\"VILLAGEID\"=a.villageid";
        SQLStr += " inner join \"visits\" h on a.zillaid=h.dist and a.upazilaid=h.upz and a.unionid=h.un and a.mouzaid=h.mouza and a.villageid=h.vill";
        SQLStr += " where a.\"provType\"='" + ProvType + "' and a.\"provCode\"='" + ProvCode + "' and v.ZillaID='" + Dist + "' and v.Upazilaid='" + Upz + "' and v.Unionid='" + UN + "'";
            *//*Res = DownloadJSON(SQLStr, "Visits", "Dist, Upz, UN, Mouza, Vill, ProvType, ProvCode, HHNo, VDate, VStatus, StartTime, EndTime, Lat, Lon, UserId, EnDt, Upload", "Dist, Upz, UN, Mouza, Vill,ProvType,ProvCode, HHNo, VDate");*//*
        Res = C.DownloadJSON(SQLStr, "Visits", "Dist, Upz, UN, Mouza, Vill, ProvType, ProvCode, HHNo, VDate, VStatus, StartTime, EndTime, Lat, Lon, UserId, EnDt, Upload", "Dist, Upz, UN, Mouza, Vill, HHNo, VDate");


        //Download OCP Code List
        SQLStr = "select \"ocpCode\", \"ocpName\", \"dCode\", \"upz\", \"ocpSequence\" from \"ocpList\" order by \"ocpSequence\"";
        Res = C.DownloadJSON(SQLStr, "ocplist", "ocpCode, ocpName, DCode, Upz, ocpSequence", "ocpCode");


        SQLStr = "SELECT * FROM \"BrandMethod\"";
        Res = C.DownloadJSON(SQLStr, "BrandMethod", "brandCode, brandName", "brandCode");

        SQLStr = "SELECT * FROM \"AttendantDesignation\"";
        Res = C.DownloadJSON(SQLStr, "AttendantDesignation", "attendantCode, attendantDesig", "attendantCode");
        //CodeList
        SQLStr = "SELECT * FROM \"CodeList\"";
        Res = C.DownloadJSON(SQLStr, "CodeList", "TypeName, Code, CName", "TypeName, Code");

        //ProviderType
        SQLStr = "SELECT * FROM \"ProviderType\"";
        Res = C.DownloadJSON(SQLStr, "ProviderType", "ProvType, TypeName", "TypeName");

//FWA Unit
        SQLStr = "select \"UCode\",\"UName\",\"UNameBan\" from \"FWAUnit\"";
        Res = C.DownloadJSON(SQLStr, "FWAUnit", "UCode,UName,UNameBan", "UCode");

        //Block
        SQLStr = "select \"BCode\",\"BName\",\"BNameBan\" from \"HABlock\"";
        Res = C.DownloadJSON(SQLStr, "HABlock", "BCode,BName,BNameBan", "BCode");


        //item
        SQLStr = "select \"itemCode\",\"itemName\", \"brand\", \"unit\", \"status\" from item";
        Res = C.DownloadJSON(SQLStr, "item", "itemCode, itemName, brand, unit, status", "itemCode");

        //currentStock
        SQLStr = "select \"providerId\",\"itemCode\",\"stockQty\", \"systemEntryDate\", \"modifyDate\", \"upload\" from \"currentStock\" where \"providerId\"='" + ProvCode + "'";
        Res = C.DownloadJSON(SQLStr, "currentStock", "providerId, itemCode, stockQty, systemEntryDate, modifyDate, upload", "providerId,itemCode");

        //classfication
        SQLStr = "select \"classficationCode\", \"classficationName\" from classfication";
        Res = C.DownloadJSON(SQLStr, "classfication", "classficationCode, classficationName", "classficationCode");

        //symtom
        SQLStr = "select \"symtomCode\", \"symtomDes\" from symtom";
        Res = C.DownloadJSON(SQLStr, "symtom", "symtomCode, symtomDes", "symtomCode");


        //treatment
        SQLStr = "select \"treatmentCode\", \"tretmentDes\" from treatment";
        Res = C.DownloadJSON(SQLStr, "treatment", "treatmentCode, tretmentDes", "treatmentCode");


        //adoSymtom
        SQLStr = "select \"problemCode\", \"problemDes\" from \"adoSymtom\"";
        Res = C.DownloadJSON(SQLStr, "adoSymtom", "problemCode, problemDes", "problemCode");


        SQLStr = "select * from \"immunization\"";
        Res = C.DownloadJSON(SQLStr, "immunization", "imuCode, imuName, numOfDose,forChild,forWoman", "imuCode");

        SQLStr = "select  *  FROM \"epiSchedulerWoman\" where \"providerId\" =" + ProvCode;
        Res = C.DownloadJSON(SQLStr, "epiSchedulerWoman", "schedulerId, scheduleDate, providerId, subBlockId, centerName, systemEntryDate, modifyDate, upload", "schedulerId, providerId");

        SQLStr = "select  *  FROM \"epiScheduler\" where \"providerId\" =" + ProvCode;
        Res = C.DownloadJSON(SQLStr, "epiScheduler", "schedulerId, scheduleDate, providerId, subBlockId, centerName, systemEntryDate, modifyDate, upload", "schedulerId, providerId");


        //ElcoEvent
        SQLStr = "select \"EVCode\", \"EVName\" from \"ElcoEvent\"";
        Res = C.DownloadJSON(SQLStr, "ElcoEvent", "EVCode, EVName", "EVCode");

        //deathReason
        SQLStr = "select \"deathReasonId\", \"code\", \"detail\" from \"deathReason\"";
        Res = C.DownloadJSON(SQLStr, "deathReason", "deathReasonId, code, detail", "deathReasonId");


        //Download Health ID
        //Service Provider
        SQLStr = "sp_HealthIDDownload '" + Dist + "','" + Upz + "','" + UN + "','" + ProvType + "','" + ProvCode + "'";
        Res = C.DownloadJSON(SQLStr, "HealthIDRepository", "ZillaID, UpazilaID, UnionID, ProvType, ProvCode, AreaCode, HealthID,Status", "HealthID");


        //Update household table based on the data of member table
        //String SQL = "";
        String SQL = "Create table totalmem as";
        SQL += " select dist,upz,un,mouza,vill,provtype,provcode,hhno,count(*)totalmem from Member";
        SQL += " group by dist,upz,un,mouza,vill,provtype,provcode,hhno";
        C.Save(SQL);

        SQL = "Create table headName as";
        SQL += " select dist,upz,un,mouza,vill,provtype,provcode,hhno,nameeng headname from Member where rth='01' and length(extype)=0";
        C.Save(SQL);

        SQL = "update household set HHHead=(select headname from headname h where h.dist=household.dist and h.upz=household.upz and h.un=household.un and h.mouza=household.mouza and h.vill=household.vill and h.provtype=household.provtype and h.provcode=household.provcode and h.hhno=household.hhno)";
        C.Save(SQL);

        SQL = "update household set TotalMem=(select totalmem from totalmem h where h.dist=household.dist and h.upz=household.upz and h.un=household.un and h.mouza=household.mouza and h.vill=household.vill and h.provtype=household.provtype and h.provcode=household.provcode and h.hhno=household.hhno)";
        C.Save(SQL);

        C.Save("drop table totalmem");
        C. Save("drop table headName");

        SQL = "Create table totalmem as";
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

        C.Save("drop table totalmem");
        C.Save("drop table headName");
        C.GenerateElco();


    }*/


    private void UploadDataworkPlanDetail()
    {

        if (C.Existence("Select * FROM workPlanDetail WHERE status='2' and upload='2'")) {
            String TableName = "workPlanDetail";
            String VariableList = "workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,ipcUN,ipcWord,ipcMouza,ipcVill,ipcPara,ipcBariFrom,ipcBariTo,epiproviderId,epischedulerId,ccWard,ccID,leaveType,natProgramType,providerId,systemEntryDate,modifyDate,otherDec,remarks,upload";
            C.UploadJSON(TableName, VariableList, "workPlanId, item, workPlanDate, providerId");
        }

        else
        {

        }

    }

    private void UploadData() {
        // String TableName = "death";

        // ----------start----
        String TableName = "workPlanMaster";

        String VariableList = "workPlanId,workAreaId,providerId,month,status,systemEntryDate,modifyDate,upload";
        C.UploadJSONAproved(TableName, VariableList, "workPlanId, workAreaId, providerId");

        //UploadDataworkPlanDetail();
        // message = "Uploading workPlanDetail";
        // jumpTime += 1;
        // progressHandler.sendMessage(progressHandler.obtainMessage());
     /*
        TableName = "workPlanDetail";
        VariableList = "workPlanId,item,workPlanDate,unitNo,village,elcoFrom,elcoTo,ipcUN,ipcWord,ipcMouza,ipcVill,ipcPara,ipcBariFrom,ipcBariTo,epiproviderId,epischedulerId,ccWard,ccID,leaveType,natProgramType,providerId,systemEntryDate,modifyDate,otherDec,remarks,upload";
        C.UploadJSON(TableName, VariableList, "workPlanId, item, workPlanDate, providerId");

        */

        TableName = "fpiMonitoring";
        VariableList = "vDate,fpaCode,fpaUnit,fpaVill,fpaAdvance,needItems1,needItems2,needItems3,needItems4,needItems5,needItems6,needItems7,needItems8,startTime,endTime,userId,enDt,upload";
        C.UploadJSON(TableName, VariableList, "vDate, fpaCode, fpaUnit");

        TableName = "HouseholdFPI";
        VariableList = "Dist,Upz,UN,Mouza,Vill,ProvType,ProvCode,HHNo,houseHoldStatus,causeOfHouseHoldStatus,subBlockStatus,pAddrStatus,permaAddressStatus,religionStatus,StartTime,EndTime,Lat,Lon,UserId,EnDt,Upload";
        // String[] H = C.GenerateArrayList(VariableList, TableName);
        // response = C.UploadData(H, TableName , VariableList , "Dist, Upz, UN, Mouza, Vill,ProvType,ProvCode, HHNo");
        C.UploadJSON(TableName, VariableList, "Dist,  Upz, UN,Mouza,Vill,HHNo");


        // jumpTime += 1;
        // Global.getInstance().setProgressMessage(message);
        //  progressHandler.sendMessage(progressHandler.obtainMessage());
        TableName = "memberfpi";
        VariableList = "dist,upz,un,mouza,vill,provtype,provcode,hhno,sno,healthid,nameengstatus,rthstatus,havenidstatus,nidstatus,havebrstatus,bridstatus,mobileno1status,mobileno2status,dobstatus,agestatus,dobsourcestatus,bplacestatus,fnostatus,fatherstatus,mnostatus,motherstatus,sexstatus,msstatus,spno1status,spno2status,spno3status,spno4status,edustatus,relstatus,nationalitystatus,ocpstatus,starttime,entype,endate,extype,exdate,endtime,lat,lon,userid,endt,upload";
        C.UploadJSON(TableName, VariableList, "dist,upz,un,mouza,vill, hhno, sno");


        TableName = "sesfpi";
        VariableList = "dist,upz,un,mouza,vill,provtype,provcode,hhno,sesstatus,q1status,q11status,q2status,q21status,q3astatus,q3bstatus,q3cstatus,q3dstatus,q3estatus,q3fstatus,q3gstatus,q3hstatus,q3istatus,q3jstatus,q3kstatus,q3lstatus,q3mstatus,q3nstatus,q3ostatus,q3pstatus,q4status,q41status,q5status,q51status,q6status,q61status,q7status,q71status,q8astatus,q8bstatus,q8cstatus,q8dstatus,q8estatus,q9status,q10status,q11astatus,starttime,endtime,userid,endt,upload";
        C.UploadJSON(TableName, VariableList, "dist,upz,un,mouza,vill, hhno");

        // done


        TableName = "elcoFPI";
        VariableList = "healthId, providerId, hhStatus, haHHNo, elcoNo, husbandName,husbandAge,domSource, marrDate, marrAge, son, dau, regDT, systemEntryDate,modifyDate,tt1,tt2,tt3,tt4,tt5, upload";
        C.UploadJSON(TableName, VariableList, "healthId");


        TableName = "elcoVisitFPI";
        VariableList = "healthId,pregNo,providerId,transactionId,visit,vDate,visitStatus,currStatus,newOld,mDate,sSource,qty,unit,brand,validity,dayMonYear,referPlace,syrinsQty,mrSource,MRDate,MRAge,systemEntryDate,modifyDate,upload";
        C.UploadJSON(TableName, VariableList, "healthId,visit");

        // done


        TableName = "PregRefer";
        VariableList = "healthId, providerId, pregNo,serviceId,referCenter,systemEntryDate,modifyDate, upload";
        C.UploadJSON(TableName, VariableList, "healthId, pregNo,serviceId");


        TableName = "pregWomenFPI";
        VariableList = "healthId, pregNo, providerId,LMP, EDD, para, gravida, lastChildAge, riskHistoryNote,pregRefer, systemEntryDate, upload";
        C.UploadJSON(TableName, VariableList, "healthId, pregNo");


        TableName = "ancServiceFPI";
        VariableList = "healthId,pregNo,serviceId,providerId,visitSource,visitDate,visitMonth,ironFolStatus,misoStatus,systemEntryDate,upload";
        C.UploadJSON(TableName, VariableList, "healthId, pregNo, serviceId");


        TableName = "deliveryFPI";
        VariableList = "healthId, pregNo, providerId, outcomePlace, outcomeDate,  outcomeType,liveBirth, stillBirth, abortion, misoprostol, attendantDesignation,systemEntryDate, upload";
        C.UploadJSON(TableName, VariableList, "healthId, pregNo");


        TableName = "newBornFPI";
        VariableList = "healthId, pregNo, childNo, providerId, birthWeight, immatureBirth,dryingAfterBirth, resassitation, stimulation, bagNMask, chlorehexidin,skinTouch, breastFeed,bathThreeDays, systemEntryDate, modifyDate, upload";
        C.UploadJSON(TableName, VariableList, "healthId, pregNo, childNo");


        TableName = "pncServiceChildFPI";
        VariableList = "healthId, pregNo, childNo, serviceId,providerId,visitSource,visitDate,visitMonth, systemEntryDate, modifyDate, upload";
        C.UploadJSON(TableName, VariableList, "healthId, pregNo, childNo, serviceId");


        TableName = "pncServiceMother";
        VariableList = "healthId, pregNo, serviceId, providerId,visitSource,visitDate,visitMonth,systemEntryDate, modifyDate, upload";
        C.UploadJSON(TableName, VariableList, "healthId, pregNo, serviceId");


        TableName = "epiBariVisit";
        VariableList = "dist,upz,un,mouza,vill,provType,provCode,hHNo,vDate,qBHHNo,qBHEndDate,qBPVisitEPI1,qBPVisitEPI2,qBPVisitEPI3,qBPVisitEPI4,qBPVisitEPI5,qBNextDoss,qB1stDoss1,qB1stDoss2,qB1stDoss3,qB1stDoss4,qB1stDoss5,qBCNoSessiondossY,qBCNoSessiondoss,qBWNoSessiondossY,qBWNoSessiondoss,qBVitmA,qBChildCard,qBWomenCard,startTime,endTime,userId,enDt,upload";
        C.UploadJSON(TableName, VariableList, "dist,upz,un,mouza,vill,provCode,hHNo,vDate");


        TableName = "epiSessionVisit";
        VariableList = "subBlockId,schedulerId,providerId,vDate,qVHA,qVFWA,qVN,qVOth,qVChReg,qVWReg,qVChCard,qVWCard,qVTalBook,qVFIBook,qVVac,qVASerice,qVMSerice,qVSBox,qVVatARed,qVVatABlue,qVIICPac,qVBCG,qVPenta,qVPolio,qVPcv,qVIPV,qVMR,qVTT,qVSbcg,qVSPenta,qVSPolio,qVSPcv,qVSIPV,qVSMR,qVSTT,qVNToubcg,qVNToupant,qVNTouPolio,qVNToupcv,qVNTouIPV,qVNTouMR,qVNTouTT,qVRootbcg\t,qVNotwhy,qVRootPenta,qVRootMR,qVRootTT,qVSRemoved,qVFormbcg,qVFormpenta,qVFormPolio,qVFormpcv,qVFormipv,qVFormmr,qVFormtt,qVCardregBook,qVCardVac,qVTTresearched,qVProtectors,qVDateOfVac,qVCard,qVAFP,qVMeasles,qVNewborntetanus,qVOther,qVTodySession,qVProblem1,qVProblem2,qVProblem3,qVProblem4,qVProblem5,qVSolve1,qVSolve2,qVSolve3,qVSolve4,qVSolve5,startTime,endTime,userId,enDt,upload";
        C.UploadJSON(TableName, VariableList, "subBlockId,schedulerId,providerId,vDate");

        //Data upload to central server

       /* String TableName = "death";
        String VariableList = "healthId,providerId,deathDT,causeOfDeath,placeOfDeath,deathOfPregWomen,pregNo,childNo,systemEntryDate,modifyDate,upload";
        C.UploadJSON(TableName, VariableList, "healthId");

        TableName = "clientMap";
        VariableList = "generatedId, name,  age, divisionId, zillaId, upazilaId,  unionId, mouzaId,villageId, houseGRHoldingNo, mobileNo,  systemEntryDate, modifyDate, providerId,  healthId,  gender, fatherName, motherName, husbandName, dob, ownMobile, epiBlock,  upload";
        C.UploadJSON(TableName, VariableList, "generatedId");

        TableName = "elco";
        VariableList = "healthId, providerId, hhStatus, haHHNo, elcoNo, husbandName,husbandAge,domSource, marrDate, marrAge, son, dau, regDT, systemEntryDate,modifyDate, upload";
        C.UploadJSON(TableName, VariableList, "healthId");

        TableName = "pncServiceMother";
        VariableList = "healthId, pregNo, serviceId, providerId,visitSource,visitDate,visitMonth,serviceSource, temperature, bpSystolic, bpDiastolic, hemoglobin,breastCondition, uterusInvolution, hematuria, perineum, FPMethod,symptom, disease, treatment, advice, refer, referReason, referCenterName,systemEntryDate, modifyDate, upload";
        C.UploadJSON(TableName, VariableList, "healthId, pregNo, serviceId");

        TableName = "pregWomen";
        VariableList = "healthId, pregNo, providerId, houseGRHoldingNo, mobileNo,LMP, tempLMP, statusLMP, EDD, para, gravida, lastChildAge, height,bloodGroup, riskHistory, riskHistoryNote, systemEntryDate,modifyDate,sateliteCenterName,client, upload";
        C.UploadJSON(TableName, VariableList, "healthId, pregNo");

        TableName = "newBorn";
        VariableList = "healthId, pregNo, childNo, providerId, childHealthId,birthStatus, gender, outcomePlace, outcomeDate, outcomeTime,attendantDesignation, outcomeType,birthWeightStatus, birthWeight, immatureBirth,dryingAfterBirth, resassitation, stimulation, bagNMask, chlorehexidin,skinTouch, breastFeed, bathThreeDays, refer, referReason,referCenterName, systemEntryDate, modifyDate, upload";
        C.UploadJSON(TableName, VariableList, "healthId, pregNo, childNo");


        TableName = "stockTransaction";
        VariableList = "transactionId,transactionDateTime,providerId,transactionType,healthId,itemCode,receiveQty,issueQty,systemEntryDate,modifyDate,upload";
        C.UploadJSON(TableName, VariableList, "transactionId, providerId, transactionType, itemCode");


        TableName = "immunizationHistory";
        VariableList = "healthId,providerId,imuCode,imuDate,imuCard,imuSource,imuDose,systemEntryDate,modifyDate,upload";
        C.UploadJSON(TableName, VariableList, "healthId,imuCode,imuDose");



        TableName = "elcoVisit";
        VariableList = "healthId,pregNo,providerId,transactionId,visit,vDate,visitStatus,currStatus,newOld,mDate,sSource,qty,unit,brand,validity,dayMonYear,referPlace,syrinsQty,mrSource,MRDate,MRAge,systemEntryDate,modifyDate,upload";
        //VariableList = "healthId,visit,upload";
        //VariableList = "healthId, providerId, transactionId, visit, vDate, visitStatus,currStatus, newOld, mDate, sSource, qty, unit, brand,validity, dayMonYear, referPlace, pregNo, syrinsQty, MRDate,systemEntryDate, modifyDate, upload";
        C.UploadJSON(TableName, VariableList, "healthId,visit");


        TableName = "PregRefer";
        VariableList = "healthId, providerId, pregNo,serviceId,referCenter,systemEntryDate,modifyDate, upload";
        C.UploadJSON(TableName, VariableList, "healthId, pregNo,serviceId");



        TableName = "ancService";
        VariableList = "healthId,pregNo,serviceId,providerId,visitSource,visitDate,visitMonth,serviceSource,urineAlbumin,ironFolStatus,ironFolQty,ironFolUnit,misoStatus,misoQty,misoUnit,sateliteCenterName,systemEntryDate,modifyDate,upload";
        C.UploadJSON(TableName, VariableList, "healthId, pregNo, serviceId");

        TableName = "delivery";
        VariableList = "healthId, pregNo, providerId, outcomePlace, deliveryCenterName,admissionDate, ward, bed, outcomeDate, outcomeTime, outcomeType,liveBirth, stillBirth, stillBirthFresh, stillBirthMacerated,abortion, nBoy, nGirl, applyOxytocin, applyTraction,uterusMassage, episiotomy, misoprostol, attendantName, attendantDesignation,excessBloodLoss, lateDelivery, blockedDelivery, placenta,headache, blurryVision, otherBodyPart, convulsion, others,othersNote, treatment, advice, refer, referReason, referCenterName,systemEntryDate, modifyDate, upload";
        C.UploadJSON(TableName, VariableList, "healthId, pregNo");


        TableName = "pncServiceChild";
        VariableList = "healthId, pregNo, childNo, childHealthId, serviceId,providerId,visitSource,visitDate,visitMonth, serviceSource, temperature, weight,breathingPerMinute, dangerSign, breastFeedingOnly, symptom,disease, treatment, advice, refer, referReason, referCenterName,systemEntryDate, modifyDate, upload";
        C.UploadJSON(TableName, VariableList, "healthId, pregNo, childNo, serviceId");




        TableName = "itemAdjustmentMinus";
        VariableList = "adjustmentId,providerId,itemCode,adjustmentType,adjustmentQty,adjustmentDate,adjustmentRemarks,createdBy,createdDate,modifyDate,systemIp,upload";
        C.UploadJSON(TableName, VariableList, "adjustmentId,providerId,itemCode");


        TableName = "womanImmunization";
        VariableList = "healthId,providerId,slNo,regDate,houseNo,imuCode,imuDate,systemEntryDate,modifyDate,upload";
        C.UploadJSON(TableName, VariableList, "healthId,imuCode");









        TableName = "adolescent";
        VariableList = "healthId,providerId,visitDate,problemOther,referCode,referOther,remarks,startTime,endTime,systemEntryDate,upload";
        C.UploadJSON(TableName, VariableList, "healthId,providerId,visitDate");

        TableName = "adolescentProblem";
        VariableList = "healthId,providerId,visitDate,problemCode,startTime,endTime,systemEntryDate,upload";
        C.UploadJSON(TableName, VariableList, "healthId,visitDate,providerId,problemCode");

        TableName = "under5Child";
        VariableList = "healthId,providerId,visitDate,remarks,startTime,endTime,systemEntryDate,upload";
        C.UploadJSON(TableName, VariableList, "healthId,providerId,visitDate");

        TableName = "under5ChildProblem";
        VariableList = "healthId,providerId,visitDate,problemCode,startTime,endTime,systemEntryDate,upload";
        C.UploadJSON(TableName, VariableList, "healthId,visitDate,providerId,problemCode");

        TableName = "under5ChildAdvice";
        VariableList = "healthId,providerId,visitDate,adviceCode,startTime,endTime,systemEntryDate,upload";
        C.UploadJSON(TableName, VariableList, "healthId,visitDate,providerId,adviceCode");

        TableName = "epiMaster";
        VariableList = "schedulerId,healthId,providerId,houseNo,regNo,regDate,brCertificateNo,brDate,remarks,systemEntryDate,modifyDate,upload";
        C.UploadJSON(TableName, VariableList, "schedulerId,healthId,providerId");

        TableName = "epiMasterWoman";
        VariableList = "schedulerId,healthId,providerId,houseNo,regNo,regDate,remarks,systemEntryDate,modifyDate,upload";
        C.UploadJSON(TableName, VariableList, "regNo");

        TableName = "sessionMaster";
        VariableList = "schedulerId,vaccineDate,bcgMixTime,mrMixTime,humMixTime,systemEntryDate,modifyDate,upload";
        C.UploadJSON(TableName, VariableList, "schedulerId");


        TableName = "sessionMasterWoman";
        VariableList = "schedulerId,vaccineDate,systemEntryDate,modifyDate,upload";
        C.UploadJSON(TableName, VariableList, "schedulerId");
*/
        /*TableName = "epiScheduler";
        VariableList = "schedulerId,scheduleDate,providerId,subBlockId,centerName,systemEntryDate,modifyDate,upload";
        C.UploadJSON(TableName, VariableList, "schedulerId,providerId");


        TableName = "epiSchedulerWoman";
        VariableList = "schedulerId,scheduleDate,providerId,subBlockId,centerName,systemEntryDate,modifyDate,upload";
        C.UploadJSON(TableName, VariableList, "schedulerId,providerId");*/


/*

        TableName = "childImmunization";
        VariableList = "healthId,providerId,slNo,regDate,houseNo,imuCode,imuDate,systemEntryDate,modifyDate,upload";
        C.UploadJSON(TableName, VariableList, "healthId,imuCode");

        TableName = "itemRequest";
        VariableList = "requestId,requestBy,requestTo,itemCode,requestQty,expectedDate,requestRemarks,requestStatus,approveQty,createdBy,systemEntryDate,modifyDate,systemIp,upload";
        C.UploadJSON(TableName, VariableList, "requestId, requestBy, requestTo, itemCode");



        TableName = "womanInjectable";
        VariableList = "healthId,providerId,doseId,doseDate,weight,pulse,bpSystolic,bpDiastolic,breastMass,PVC,LMP,sideEffect,systemEntryDate,modifyDate,upload";
        C.UploadJSON(TableName, VariableList, "healthId,providerId,doseId");

*/


    }

    //@SuppressWarnings("static-access")
    @Override
    public void onStart(Intent intent, int startId) {
        handleIntent(intent);
        /*
        super.onStart(intent, startId);
        String TableName = "";
        String VariableList = "";
        String ColumnList = "";
        String UniqueField = "";

        String ResponseString="Status:";
        String response;

        try {
            TableName = "Household";
            VariableList = "Div, Dist, Upz, UN, wardNew, wardOld, Mouza, FWAUnit, Vill, EPIBlock, PAddr, PermaAddress, HHNo, Religion, VGFCard, StartTime, EndTime, Lat, Lon, UserId, EnDt, Upload";
            String[] H = C.GenerateArrayList(VariableList, TableName);
            response = C.UploadData(H, TableName , VariableList , "Div, Dist, Upz, UN, Mouza, Vill, HHNo");

            TableName = "Member";
            VariableList = "Dist, Upz, UN, Mouza, Vill, HHNo, SNo, HealthID, NameEng, NameBang, Rth, HaveNID, NID, NIDStatus, HaveBR, BRID, BRIDStatus, MobileNo1, MobileNo2, DOB, Age, DOBSource, BPlace, FNo, Father, MNo, Mother, Sex, MS, SPNO1, SPNO2, SPNO3, SPNO4, ELCONo, ELCODontKnow, EDU, Rel, Nationality, OCP, StartTime, EnType, EnDate, ExType, ExDate, EndTime, Lat, Lon, UserId, EnDt, Upload";
            String[] M = C.GenerateArrayList(VariableList, TableName);
            response = C.UploadData(M, TableName , VariableList , "Dist, Upz, UN, Mouza, Vill, HHNo, SNo");

            //Sync Database

        } catch (Exception e) {

        }
        */
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleIntent(intent);
        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        //mWakeLock.release();
    }

}

