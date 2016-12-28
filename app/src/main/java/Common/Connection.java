package Common;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.data.rhis2.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import DataSync.DataClass;
import DataSync.DataClassProperty;
import DataSync.DownloadJSONData;
import DataSync.DownloadRequestClass;
import DataSync.UploadJSONData;
import DataSync.downloadClass;

//--------------------------------------------------------------------------------------------------
// Created by TanvirHossain on 17/03/2015.
//--------------------------------------------------------------------------------------------------

public class Connection extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DB_NAME = Global.DatabaseName;
    private static final String DBLocation= Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Global.DatabaseFolder + File.separator +  DB_NAME;

    // Todo table name
    private static final String TABLE_TODO = "todo_items";

    private Context dbContext;

    private static Context ud_context;

    public Connection(Context context) {
        super(context, DBLocation, null, DATABASE_VERSION);
        dbContext=context;

        try
        {

            if (!Existence("Select * from deathReason where code='mdeath'")) {
                Save("Insert into deathReason (deathReasonId,code,detail)Values(16, 'mdeath', 'অতিরিক্ত খিঁচুনি- গর্ভাবস্থায় এবং প্রসব পরবর্তী সময়ে')");
                Save("Insert into deathReason (deathReasonId,code,detail)Values(16, 'mdeath', 'অতিরিক্ত খিঁচুনি- গর্ভাবস্থায় এবং প্রসব পরবর্তী সময়ে')");
                Save("Insert into deathReason (deathReasonId,code,detail)Values(17, 'mdeath', 'পুষ্টিহীনতা-  গর্ভাবস্থায় এবং প্রসব পরবর্তী সময়ে')");
                Save("Insert into deathReason (deathReasonId,code,detail)Values(18, 'mdeath', 'অতিরিক্ত রক্তস্রাব- প্রসব পরবর্তী সময়ে')");
                Save("Insert into deathReason (deathReasonId,code,detail)Values(19, 'mdeath', 'একলাম্পসিয়া')");
                Save("Insert into deathReason (deathReasonId,code,detail)Values(20, 'mdeath', 'অনিরাপদ গর্ভপাত')");
                Save("Insert into deathReason (deathReasonId,code,detail)Values(21, 'mdeath', 'প্রসব পরবর্তী সংক্রমণ')");
                Save("Insert into deathReason (deathReasonId,code,detail)Values(22, 'mdeath', 'বিলম্বিত বা বাধাগ্রস্থ প্রসব')");
                Save("Insert into deathReason (deathReasonId,code,detail)Values(23, 'mdeath', 'প্রসবের পরে গর্ভ ফুল না পড়লে')");
                Save("Insert into deathReason (deathReasonId,code,detail)Values(24, 'mdeath', 'রক্তস্বল্পতা')");
                Save("Insert into deathReason (deathReasonId,code,detail)Values(25, 'mdeath', 'ডেলিভারির সময় বাচ্চার মাথা আগে না এসে যদি অন্য কোন অঙ্গ প্রত্যঙ্গ যেমন- হাত, পা ইত্যাদি আসে')");
                Save("Insert into deathReason (deathReasonId,code,detail)Values(26, 'mdeath', 'নারী নির্যাতন ও আঘাত জনিত কারণে')");
                Save("Insert into deathReason (deathReasonId,code,detail)Values(27, 'mdeath', 'অন্যান্য')");
                Save("Update adoSymtom set problemDes='মাসিকের সমস্যা' where problemCode = '1'");
                Save("Update adoSymtom set problemDes='সাদা স্রাব' where problemCode = '2'");
                Save("Update adoSymtom set problemDes='তলপেটে ব্যাথা' where problemCode = '3'");
                Save("Update adoSymtom set problemDes='টিটি টিকা দিতে ' where problemCode = '4'");
                Save("Update adoSymtom set problemDes='অপুষ্টি' where problemCode = '6'");
                Save("Update adoSymtom set problemDes='রক্ত স্বল্পতা' where problemCode = '7'");
                Save("Update adoSymtom set problemDes='জ্বর' where problemCode = '14'");






            }
            //New DatabaseStructure

            //Household
            Cursor h= ReadData("Select * from Household limit 1");
            if(h.getColumnCount()==23)
            {
                Save("alter table Household add Elco VARCHAR( 300 ) default ''");
                GenerateElco();

                /*if (!Existence("Select * from deathReason where code='mdeath'")) {
                    Save("Insert into deathReason (deathReasonId,code,detail)Values(16, 'mdeath', 'অতিরিক্ত খিঁচুনি- গর্ভাবস্থায় এবং প্রসব পরবর্তী সময়ে')");
                    Save("Insert into deathReason (deathReasonId,code,detail)Values(16, 'mdeath', 'অতিরিক্ত খিঁচুনি- গর্ভাবস্থায় এবং প্রসব পরবর্তী সময়ে')");
                    Save("Insert into deathReason (deathReasonId,code,detail)Values(17, 'mdeath', 'পুষ্টিহীনতা-  গর্ভাবস্থায় এবং প্রসব পরবর্তী সময়ে')");
                    Save("Insert into deathReason (deathReasonId,code,detail)Values(18, 'mdeath', 'অতিরিক্ত রক্তস্রাব- প্রসব পরবর্তী সময়ে')");
                    Save("Insert into deathReason (deathReasonId,code,detail)Values(19, 'mdeath', 'একলাম্পসিয়া')");
                    Save("Insert into deathReason (deathReasonId,code,detail)Values(20, 'mdeath', 'অনিরাপদ গর্ভপাত')");
                    Save("Insert into deathReason (deathReasonId,code,detail)Values(21, 'mdeath', 'প্রসব পরবর্তী সংক্রমণ')");
                    Save("Insert into deathReason (deathReasonId,code,detail)Values(22, 'mdeath', 'বিলম্বিত বা বাধাগ্রস্থ প্রসব')");
                    Save("Insert into deathReason (deathReasonId,code,detail)Values(23, 'mdeath', 'প্রসবের পরে গর্ভ ফুল না পড়লে')");
                    Save("Insert into deathReason (deathReasonId,code,detail)Values(24, 'mdeath', 'রক্তস্বল্পতা')");
                    Save("Insert into deathReason (deathReasonId,code,detail)Values(25, 'mdeath', 'ডেলিভারির সময় বাচ্চার মাথা আগে না এসে যদি অন্য কোন অঙ্গ প্রত্যঙ্গ যেমন- হাত, পা ইত্যাদি আসে')");
                    Save("Insert into deathReason (deathReasonId,code,detail)Values(26, 'mdeath', 'নারী নির্যাতন ও আঘাত জনিত কারণে')");
                    Save("Insert into deathReason (deathReasonId,code,detail)Values(27, 'mdeath', 'অন্যান্য')");
                }
*/
            }
            h.close();

           h= ReadData("Select * from ses limit 1");
            if(h.getColumnCount()==47)
            {
                Save("alter table ses add Q9 VARCHAR( 1 ) default ''");
                Save("alter table ses add Q10 VARCHAR( 1 ) default ''");

            }
            h.close();




            //ancService
            h= ReadData("Select * from ancService limit 1");
            if(h.getColumnCount()==37)
            {
                Save("alter table ancService add visitSource INTEGER ");
                Save("alter table ancService add visitMonth INTEGER ");

            }
            h.close();

            //pncServiceMother
            h= ReadData("Select * from pncServiceMother limit 1");
            if(h.getColumnCount()==30)
            {
                Save("alter table pncServiceMother add visitSource INTEGER ");
                Save("alter table pncServiceMother add visitMonth INTEGER ");

            }
            h.close();

            //pncServiceChild
            h= ReadData("Select * from pncServiceChild limit 1");
            if(h.getColumnCount()==25)
            {
                Save("alter table pncServiceChild add visitSource INTEGER ");
                Save("alter table pncServiceChild add visitMonth INTEGER ");

            }
            h.close();
            //elco
            h= ReadData("Select * from elco limit 1");
            if(h.getColumnCount()==15)
            {

                Save("alter table elco add husbandAge INTEGER ");

            }

            h.close();

            //workPlanDetail
            h= ReadData("Select * from workPlanDetail limit 1");
            if(h.getColumnCount()==27)
            {

                Save("alter table workPlanDetail add status INTEGER ");

            }
            h.close();


           //
           //

           // Save("Drop table EPIBariVisit");
            CreateTable("epiBariVisit","Create Table epiBariVisit(dist INT,upz INT,un INT,mouza INT,vill INT, provType INT,provCode INT,hhNo INT,vDate VARCHAR( 10 ), qBHHNo Varchar(50),qBHEndDate Varchar(10),qBPVisitEPI1 Varchar(1),qBPVisitEPI2 Varchar(1),qBPVisitEPI3 Varchar(1),qBPVisitEPI4 Varchar(1),qBPVisitEPI5 Varchar(1),qBNextDoss Varchar(1),qB1stDoss1 Varchar(1),qB1stDoss2 Varchar(1),qB1stDoss3 Varchar(1),qB1stDoss4 Varchar(1),qB1stDoss5 Varchar(1),qBCNoSessiondossY Varchar(1),qBCNoSessiondoss Varchar(2), qBWNoSessiondossY Varchar(1),qBWNoSessiondoss Varchar(2),qBVitmA Varchar(1), qBChildCard Varchar(1), qBWomenCard Varchar(1),startTime varchar(5),endTime varchar(5),userId Varchar(10),enDt Varchar(20), upload Varchar(1))");           // Save("Drop table FormCall");
            CreateTable("FormCall","Create Table FormCall(fCall Varchar(1))");

           //  Save("Drop table EPISessionVisit");
            CreateTable("epiSessionVisit","Create Table epiSessionVisit(\n" +
                    " subBlockId INT,\n" +
                    " schedulerId INT,\n" +
                    " providerId INT,\n" +
                    " vDate VARCHAR( 10 ),\n" +
                    " qVHA Varchar(2),\n" +
                    " qVFWA Varchar(2),\n" +
                    " qVN Varchar(2),\n" +
                    " qVOth Varchar(2),\n" +
                    " qVChReg Varchar(1),\n" +
                    " qVWReg Varchar(1),\n" +
                    " qVChCard Varchar(1),\n" +
                    " qVWCard Varchar(1),\n" +
                    " qVTalBook Varchar(1),\n" +
                    " qVFIBook Varchar(1),\n" +
                    " qVVac Varchar(1),\n" +
                    " qVASerice Varchar(1),\n" +
                    " qVMSerice Varchar(1),\n" +
                    " qVSBox Varchar(1),\n" +
                    " qVVatARed Varchar(1),\n" +
                    " qVVatABlue Varchar(1),\n" +
                    " qVIICPac Varchar(1),\n" +
                    " qVBCG Varchar(1),\n" +
                    " qVPenta Varchar(1),\n" +
                    " qVPolio Varchar(1),\n" +
                    " qVPcv Varchar(1),\n" +
                    " qVIPV Varchar(1),\n" +
                    " qVMR Varchar(1),\n" +
                    " qVTT Varchar(1),\n" +
                    " qVSbcg Varchar(1),\n" +
                    " qVSPenta Varchar(1),\n" +
                    " qVSPolio Varchar(1),\n" +
                    " qVSPcv Varchar(1),\n" +
                    " qVSIPV Varchar(1),\n" +
                    " qVSMR Varchar(1),\n" +
                    " qVSTT Varchar(1),\n" +
                    " qVNToubcg Varchar(1),\n" +
                    " qVNToupant Varchar(1),\n" +
                    " qVNTouPolio Varchar(1),\n" +
                    " qVNToupcv Varchar(1),\n" +
                    " qVNTouIPV Varchar(1),\n" +
                    " qVNTouMR Varchar(1),\n" +
                    " qVNTouTT Varchar(1),\n" +
                    " qVRootbcg Varchar(1),\n" +
                    " qVNotwhy Varchar(2),\n" +
                    " qVRootPenta Varchar(1),\n" +
                    " qVRootMR Varchar(1),\n" +
                    " qVRootTT Varchar(1),\n" +
                    " qVSRemoved Varchar(1),\n" +
                    " qVFormbcg Varchar(1),\n" +
                    " qVFormpenta Varchar(1),\n" +
                    " qVFormPolio Varchar(1),\n" +
                    " qVFormpcv Varchar(1),\n" +
                    " qVFormipv Varchar(1),\n" +
                    " qVFormmr Varchar(1),\n" +
                    " qVFormtt Varchar(1),\n" +
                    " qVCardregBook Varchar(1),\n" +
                    " qVCardVac Varchar(1),\n" +
                    " qVTTresearched  Varchar(1),\n" +
                    " qVProtectors Varchar(1),\n" +
                    " qVDateOfVac Varchar(1),\n" +
                    " qVCard Varchar(1),\n" +
                    " qVAFP Varchar(2),\n" +
                    " qVMeasles Varchar(2),\n" +
                    " qVNewborntetanus Varchar(2),\n" +
                    " qVOther Varchar(2),\n" +
                    " qVTodySession Varchar(2),\n" +
                    " qVProblem1 Varchar(50),\n" +
                    " qVProblem2 Varchar(50),\n" +
                    " qVProblem3 Varchar(50),\n" +
                    " qVProblem4 Varchar(50),\n" +
                    " qVProblem5 Varchar(50),\n" +
                    " qVSolve1 Varchar(50),\n" +
                    " qVSolve2 Varchar(50),\n" +
                    " qVSolve3 Varchar(50),\n" +
                    " qVSolve4 Varchar(50),\n" +
                    " qVSolve5 Varchar(50),\n" +
                    " startTime varchar(5),\n" +
                    " endTime varchar(5),\n" +
                    " userId Varchar(10),\n" +
                    " enDt Varchar(20),\n" +
                    " upload Varchar(1))");

            //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
       /*     String SQL = "";

            SQL = "Create table elco (healthId	BIGINT primary key,	providerId	INT,	hhStatus	INT,	haHHNo	TEXT,	elcoNo	TEXT,	husbandName	TEXT,	domSource	INT,";
            SQL += " marrDate	DATE,	marrAge	INT,	son	INT,	dau	INT,	regDT	DATE,	systemEntryDate	DATE,	modifyDate	DATE)";
            CreateTable("elco",SQL);

            //Save("Drop table Immunization");
            SQL  = " Create table Immunization(	healthId	BIGINT,	providerId	INT,	imuCode	INT,	imuDate	DATE, imuCard int,	imuSource	TEXT, imuDose int, systemEntryDate	DATE,	modifyDate	DATE)";
            CreateTable("Immunization",SQL);

            //Save("Drop table elcoVisit");
            SQL = " Create table elcoVisit(healthId	BIGINT,	visit INT, providerId	INT,	vDate	DATE,	visitStatus	INT,	currStatus	TEXT,";
            SQL += " newOld	TEXT,	msDate	DATE,	sSource	INT,	qty	INT,	unit	INT,	brand	INT, validity int, dayMonYear int,referPlace int,systemEntryDate	DATE,	modifyDate	DATE)";
            CreateTable("elcoVisit",SQL);

           *//* SQL = "Create Table PregWomen(Dist Varchar(2), Upz Varchar(2), UN Varchar(2), Mouza Varchar(3), Vill Varchar(2),ProvType Varchar(2),ProvCode Varchar(5),HHNo Varchar(5), SNo Varchar(2),PGN Varchar(2),DOLMP Varchar(10),DOEDD Varchar(10),PrePregTimes Varchar(1),LCAge Varchar(2),RegDT Varchar(10), EnDt Varchar(20), Upload Varchar(1),UploadDT Varchar(20))";
            CreateTable("PregWomen",SQL);*//*

            SQL =  "CREATE TABLE PregWomen (healthId BIGINT, pregNo INT, providerId INT, houseGRHoldingNo VARCHAR(30),mobileNo INT,LMP DATE,tempLMP DATE,EDD DATE,para INT,gravida INT,lastChildAge INT,height INT,bloodGroup VARCHAR( 5 ),riskHistory INT,riskHistoryNote  VARCHAR(50),StartTime VARCHAR( 20 ),EndTime VARCHAR( 20 ),lat VARCHAR( 30 ),lon VARCHAR( 30 ),systemEntryDate DATE, Upload VARCHAR( 1 ),UploadDT DATE, modifyDate DATE,PRIMARY KEY(healthId, pregNo));";
            CreateTable("PregWomen",SQL);
        // Save("Drop table PregRefer");
            SQL =  "CREATE TABLE PregRefer (healthId BIGINT, providerId INT,pregNo INT,referCenter VARCHAR(50),upload VARCHAR( 1 ),PRIMARY KEY(healthId,pregNo));";
            CreateTable("PregRefer", SQL);
            //Save("Drop table Death");
            SQL =  "CREATE TABLE death (healthId BIGINT, providerId INT,pregNo INT,childNo INT,deathDT  Date,placeOfDeath  INT,causeOfDeath  INT,deathOfPregWomen INT,systemEntryDate  Date,modifyDate  Date,upload VARCHAR( 1 ),PRIMARY KEY(healthId));";
            CreateTable("Death", SQL);
            //  Save("Drop table epiScheduler");

        *//*    1	2015-11-15	68974	1	BASAIL	11-13-2015	11-13-2015	2
            2	2015-11-18	68974	1	BASAIL1	11-13-2015	11-13-2015	2
            3	2015-11-19	68974	1	BASAIL2	11-13-2015	11-13-2015	2
            4	2015-11-20	68974	2	BASAIL3	11-13-2015	11-13-2015	2*//*

         //  Save("Insert into epiScheduler values(1,'2015-11-15','68974',1,'BASAIL','11-13-2015','11-13-2015',2)");
         // Save("Insert into epiScheduler values(2,'2015-11-23','68974',1,'BASAIL1','11-23-2015','11-23-2015',2)");
            SQL =  "CREATE TABLE epiScheduler (schedulerId BIGINT, scheduleDate DATE, providerId INT, subBlockId INT,centerName VARCHAR(200),systemEntryDate DATE,modifyDate DATE, upload INT,PRIMARY KEY(schedulerId, providerId));";
            CreateTable("epiScheduler",SQL);
            //  Save("Drop table sessionMaster");
            SQL =  "CREATE TABLE sessionMaster (schedulerId BIGINT, vaccineDate DATE, bcgMixTime VARCHAR(8), mrMixTime VARCHAR(8),humMixTime VARCHAR(8),systemEntryDate DATE,modifyDate DATE, upload INT,PRIMARY KEY(schedulerId));";
            CreateTable("sessionMaster",SQL);
            //  Save("Drop table epiMaster");
           SQL =  "CREATE TABLE epiMaster (schedulerId BIGINT,healthId BIGINT, providerId INT, houseNo VARCHAR(50), regNo VARCHAR(50), regDate Date,brCertificateNo VARCHAR(50),brDate Date,remarks VARCHAR(50),systemEntryDate DATE,modifyDate DATE, upload INT,PRIMARY KEY(schedulerId,healthId, providerId));";
            CreateTable("epiMaster",SQL);
            //  Save("Drop table under5Child");
            SQL =  "CREATE TABLE under5Child (healthId BIGINT, providerId INT,visitDate DATE,remarks VARCHAR( 250 ),startTime VARCHAR( 5 ),endTime VARCHAR( 5 ),systemEntryDate DATE, upload VARCHAR( 1 ),PRIMARY KEY(healthId,providerId, visitDate));";
            CreateTable("under5Child",SQL);


           // Save("Drop table under5ChildProblem");
            SQL =  "CREATE TABLE under5ChildProblem (healthId BIGINT, providerId INT,visitDate DATE,problemCode INT,startTime VARCHAR( 5 ),endTime VARCHAR( 5 ),systemEntryDate DATE, upload VARCHAR( 1 ),PRIMARY KEY(healthId, visitDate,providerId,problemCode));";
            CreateTable("under5ChildProblem",SQL);



           //  Save("Drop table under5ChildAdvice");
            SQL =  "CREATE TABLE under5ChildAdvice (healthId BIGINT, providerId INT,visitDate DATE,adviceCode INT,startTime VARCHAR( 5 ),endTime VARCHAR( 5 ),systemEntryDate DATE,upload VARCHAR( 1 ),PRIMARY KEY(healthId, visitDate,providerId,AdviceCode));";
            CreateTable("under5ChildAdvice",SQL);

            SQL =  "CREATE TABLE immunizationHistory (healthId BIGINT,providerId  INT,imuCode INT,imuDate DATE,imuCard INT,imuSource  VARCHAR( 20 ),imuDose  INT,systemEntryDate DATE,modifyDate DATE,upload INT,PRIMARY KEY ( healthId, imuCode, imuDose ));";
            CreateTable("immunizationHistory",SQL);
          //  Save("Drop table immunization");
            SQL =  "CREATE TABLE immunization (imuCode INT ,imuName   VARCHAR( 100 ),numOfDose VARCHAR( 2 ),upload    VARCHAR( 10 ),forChild  INT,forWoman  INT,PRIMARY KEY (imuCode ));";
            CreateTable("immunization",SQL);

            SQL =  "CREATE TABLE adoSymtom (problemCode BIGINT, problemDes VARCHAR( 500 ));";
            CreateTable("adoSymtom",SQL);*/
           /* Save("Insert into adoSymtom values(1,'মাসিকের সমস্যা')");
            Save("Insert into adoSymtom values(2,'অপুষ্টি')");
            Save("Insert into adoSymtom values(3,'রক্ত স্বল্পতা')");
            Save("Insert into adoSymtom values(4,'জ্বর')");
            Save("Insert into adoSymtom values(5,'কাঁশি ')");
            Save("Insert into adoSymtom values(6,'সাদাস্রাব')");
            Save("Insert into adoSymtom values(7,'তলপেটে ব্যাথা')");
            Save("Insert into adoSymtom values(8,'নিউমোনিয়া')");
            Save("Insert into adoSymtom values(9,'ঠাণ্ডা জ্বর')");
            Save("Insert into adoSymtom values(10,'জন্ডিস')");
            Save("Insert into adoSymtom values(11,'ডায়রিযা')");
            Save("Insert into adoSymtom values(12,'দূর্বল')");
            Save("Insert into adoSymtom values(13,'মাথা ব্যাথা')");
            Save("Insert into adoSymtom values(14,'টিটি টিকা দিতে ')");
            Save("Insert into adoSymtom values(77,'অন্যান্য')");
            Save("Insert into adoSymtom values(15,'ক-বাল্যবিবাহ এবং কিশোরী মাতৃত্বের কুফল ')");
            Save("Insert into adoSymtom values(16,'খ-কিশোরীকে আয়রণ ও ফলিক এসিড বড়ি খেতে বলা ')");
            Save("Insert into adoSymtom values(17,'গ-কিশোর-কিশোরীদের পুষ্টিকর ও সুষম খাবার')");
            Save("Insert into adoSymtom values(18,'ঘ-কিশোর-কিশোরীকে বয়ঃসন্ধিকালিন পরিবর্তন বিষয়ে')");
            Save("Insert into adoSymtom values(19,'ঙ-কিশোরীদের মাসিককালিন পরিছন্নতা ও মাসিক সংক্রান্ত  জটিলতা ')");
            Save("Insert into adoSymtom values(20,'চ-কিশোর-কিশোরীর প্রজননতন্ত্রের সংক্রমণ ও যৌনবাহিত রোগ')");*/

                    //*/
            //Save("Drop table adolescent");
           /* SQL =  "CREATE TABLE adolescent (healthId BIGINT, providerId INT,visitDate DATE,problemOther VARCHAR( 250 ), referCode INT,referOther VARCHAR( 250 ),remarks VARCHAR( 250 ),startTime VARCHAR( 5 ),endTime VARCHAR( 5 ),systemEntryDate DATE, upload VARCHAR( 1 ),PRIMARY KEY(healthId,providerId, visitDate));";
            CreateTable("adolescent",SQL);*/


            // Save("Drop table adolescentProblem");
           /* SQL =  "CREATE TABLE adolescentProblem (healthId BIGINT, providerId INT,visitDate DATE,problemCode INT,startTime VARCHAR( 5 ),endTime VARCHAR( 5 ),systemEntryDate DATE, upload VARCHAR( 1 ),PRIMARY KEY(healthId, visitDate,providerId,problemCode));";
            CreateTable("adolescentProblem",SQL);*/


            /*
            Save("delete from elcoVisit");
            Save("delete from pregWomen");
            Save("delete from ancservice");
            Save("delete from delivery");
            Save("delete from  newborn");
            Save("delete from pncservicechild");
            Save("delete from pncservicemother");
            */
            //Save("delete from delivery");

        }
        catch(Exception ex)
        {

        }
    }

    public void AlterStockTransaction()
    {
        Save("DROP TABLE stockTransaction");
        Save("CREATE TABLE stockTransaction( \n" +
                "    transactionId        BIGINT,\n" +
                "    transactionDateTime DATE,\n" +
                "    providerId          INTEGER,\n" +
                "    transactionType     INTEGER,\n" +
                "    healthId            BIGINT,\n" +
                "    itemCode            INTEGER,\n" +
                "    receiveQty          INTEGER,\n" +
                "    issueQty            INTEGER,\n" +
                "    systemEntryDate     DATE,\n" +
                "    modifyDate          DATE,\n" +
                "    upload              INTEGER,\n" +
                "    PRIMARY KEY ( transactionId, transactionType, providerId ) \n" +
                ")\n");


    }

    // Creating our initial tablesSave("delete from delivery");
    // These is where we need to write create table statements.
    // This is called when database is created.
    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("Create Table abc(sid varchar(10))");
    }

    // Upgrading the database between versions
    // This method is called when database is upgraded like modifying the table structure,
    // adding constraints to database, etc
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion == 1) {
            // Wipe older tables if existed
            //db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
            // Create tables again
            onCreate(db);
        }
    }

    //Check the existence of database table
    //----------------------------------------------------------------------------------------------
    public boolean TableExists(String TableName)
    {
        Cursor c = null;
        boolean tableExists = false;
        SQLiteDatabase db = this.getReadableDatabase();
        try
        {
            c = db.rawQuery("Select * from "+TableName,null);
            tableExists = true;
            c.close();
            db.close();
        }
        catch (Exception e) {
        }
        return tableExists;
    }

    //Create database tables
    //----------------------------------------------------------------------------------------------
    public void CreateTable(String TableName,String SQL)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        if(!TableExists(TableName))
        {
            db.execSQL(SQL);
        }
    }

    //Read data from database and return to Cursor variable
    //----------------------------------------------------------------------------------------------
    public Cursor ReadData(String SQL)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur=db.rawQuery(SQL, null);
        return cur;
    }

    //Check existence of data in database
    //----------------------------------------------------------------------------------------------
    public boolean Existence(String SQL)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur=db.rawQuery(SQL, null);
        if(cur.getCount()==0)
        {
            cur.close();
            db.close();
            return false;
        }
        else
        {
            cur.close();
            db.close();
            return true;
        }
    }

    //Return single result based on the SQL query
    //----------------------------------------------------------------------------------------------
    public String ReturnSingleValue(String SQL)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur=db.rawQuery(SQL, null);
        String retValue="";
        cur.moveToFirst();
        while(!cur.isAfterLast())
        {
            retValue=cur.getString(0);
            cur.moveToNext();
        }
        cur.close();
        db.close();
        return retValue;
    }

    //Split function
    //----------------------------------------------------------------------------------------------
    public static String[] split(String s, char separator)
    {
        ArrayList<String> d = new ArrayList<String>();
        for (int ini = 0, end = 0; ini <= s.length(); ini = end + 1)
        {
            end = s.indexOf(separator, ini);
            if (end == -1) {
                end = s.length();
            }

            String st = s.substring(ini, end).trim();


            if (st.length() > 0) {
                d.add(st);
            }
            else {
                d.add("");
            }
        }

        String[] temp = new String[d.size()];
        temp=d.toArray(temp);
        return temp;
    }

    //Save/Update/Delete data in to database
    //----------------------------------------------------------------------------------------------
    public void Save(String SQL)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(SQL);
        db.close();
    }


    //Message Box
    //----------------------------------------------------------------------------------------------
    public static void MessageBox(Context ClassName,String Msg)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(ClassName);
        builder.setMessage(Msg)
                .setTitle("Message")
                .setCancelable(true)
                //.setIcon(R.drawable.logo)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Ok", null);
        builder.show();
    }

    //Generate data list
    //----------------------------------------------------------------------------------------------
    public List<String> getDataList(String SQL){
        List<String> data = new ArrayList<String>();
        Cursor cursor = ReadData(SQL);
        if (cursor.moveToFirst()) {
            do {
                data.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return data;
    }

    //Array adapter for spinner item
    //----------------------------------------------------------------------------------------------
    public ArrayAdapter<String> getArrayAdapter(String SQL){
        List<String> dataList = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery(SQL, null);
        if (cursor.moveToFirst()) {
            do {
                dataList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.dbContext,
                R.layout.multiline_spinner_dropdown_item, dataList);

        return dataAdapter;
    }

    public ArrayAdapter<String> getArrayAdapterMultiline(String SQL){
        List<String> dataList = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery(SQL, null);
        if (cursor.moveToFirst()) {
            do {
                dataList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.dbContext,
                R.layout.multiline_spinner_dropdown_item, dataList);
       //ArrayAdapter<String> dataAdapter = new ArrayAdapter.createFromResource(this.dbContext, dataList,
             //   R.layout.multiline_spinner_dropdown_item);
        return dataAdapter;
    }

    //Check whether internet connectivity available or not
    //----------------------------------------------------------------------------------------------
    public static boolean haveNetworkConnection(Context con) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        try
        {
            ConnectivityManager cm = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();
            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                    if (ni.isConnected())
                        haveConnectedWifi = true;
                if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (ni.isConnected())
                        haveConnectedMobile = true;
            }
        }
        catch(Exception e)
        {

        }
        return haveConnectedWifi || haveConnectedMobile;
    }


    // Download data from Database Server
    //----------------------------------------------------------------------------------------------
/*    public String DownloadData(String SQLStr, String TableName,String ColumnList, String UniqueField)
    {
        String rep = "";
        String SQL = "";

        int totalDownload=0;
        String DownloadStatus="";
        String WhereClause="";
        int varPos=0;

        try
        {
            DownloadData d=new DownloadData();
            d.Method_Name="DownloadData";
            d.SQLStr=SQLStr;

            String DataArray[]=null;
            DataArray=d.execute("").get();

            String UField[]  = UniqueField.split(",");
            String VarList[] = ColumnList.split(",");

            for(int i=0;i<DataArray.length;i++)
            {
                String VarData[] = split(DataArray[i],'^');

                //Generate where clause
                SQL="";
                WhereClause="";
                varPos=0;
                for(int j=0; j< UField.length; j++)
                {
                    varPos = VarPosition(UField[j].toString(),VarList);
                    if(j==0)
                    {
                        WhereClause = UField[j].toString()+"="+ "'"+ VarData[varPos].toString() +"'";
                    }
                    else
                    {
                        WhereClause += " and "+ UField[j].toString()+"="+ "'"+ VarData[varPos].toString() +"'";
                    }
                }

                //Update command
                if(Existence("Select "+ VarList[0] +" from "+ TableName +" Where "+ WhereClause))
                {
                    for(int r=0;r<VarList.length;r++)
                    {
                        if(r==0)
                        {
                            SQL = "Update "+ TableName +" Set ";
                            SQL+= VarList[r] + " = '"+ VarData[r].toString() +"'";
                        }
                        else
                        {
                            if(r == VarData.length-1)
                            {
                                SQL+= ","+ VarList[r] + " = '"+ VarData[r].toString() +"'";
                                SQL += " Where "+ WhereClause;
                            }
                            else
                            {
                                SQL+= ","+ VarList[r] + " = '"+ VarData[r].toString() +"'";
                            }
                        }
                    }

                    Save(SQL);
                    totalDownload+=1;
                }
                //Insert command
                else
                {
                    for(int r=0;r<VarList.length;r++)
                    {
                        if(r==0)
                        {
                            SQL = "Insert into "+ TableName +"("+ ColumnList +")Values(";
                            SQL+= "'"+ VarData[r].toString() +"'";
                        }
                        else
                        {
                            SQL+= ",'"+ VarData[r].toString() +"'";
                        }
                    }
                    SQL += ")";

                    Save(SQL);
                    totalDownload+=1;
                }

                //update download status on server
                //rep = ExecuteCommandOnServer("Update "+ TableName +" set Download='1', DownloadDt='"+ Global.DateTimeNowYMDHMS() +"' Where "+ WhereClause);
            }

            DownloadStatus="Total download completed: "+ totalDownload +" of "+ DataArray.length;

            return DownloadStatus;
        }
        catch(Exception e)
        {
            return "Download Error:"+ e.getMessage();
        }
    }*/


    // Data Update
    //----------------------------------------------------------------------------------------------
/*    public String DataUpdate(String SQLStr, String TableName,String ColumnList, String UniqueField)
    {
        String rep = "";
        String SQL = "";

        int totalDownload=0;
        String DownloadStatus="";
        String WhereClause="";
        int varPos=0;

        try
        {
            DownloadData d=new DownloadData();
            d.Method_Name="DownloadData";
            d.SQLStr=SQLStr;

            String DataArray[]=null;
            DataArray=d.execute("").get();

            String UField[]  = UniqueField.split(",");
            String VarList[] = ColumnList.split(",");

            for(int i=0;i<DataArray.length;i++)
            {
                String VarData[] = split(DataArray[i],'^');

                //Generate where clause
                SQL="";
                WhereClause="";
                varPos=0;
                for(int j=0; j< UField.length; j++)
                {
                    varPos = VarPosition(UField[j].toString(),VarList);
                    if(j==0)
                    {
                        WhereClause = UField[j].toString()+"="+ "'"+ VarData[varPos].toString() +"'";
                    }
                    else
                    {
                        WhereClause += " and "+ UField[j].toString()+"="+ "'"+ VarData[varPos].toString() +"'";
                    }
                }

                //Update command
                if(Existence("Select "+ VarList[0] +" from "+ TableName +" Where "+ WhereClause))
                {
                    for(int r=0;r<VarList.length;r++)
                    {
                        if(r==0)
                        {
                            SQL = "Update "+ TableName +" Set ";
                            SQL+= VarList[r] + " = '"+ VarData[r].toString() +"'";
                        }
                        else
                        {
                            if(r == VarData.length-1)
                            {
                                SQL+= ","+ VarList[r] + " = '"+ VarData[r].toString() +"'";
                                SQL += " Where "+ WhereClause;
                            }
                            else
                            {
                                SQL+= ","+ VarList[r] + " = '"+ VarData[r].toString() +"'";
                            }
                        }
                    }

                    Save(SQL);
                    totalDownload+=1;
                }
                //Insert command
                else
                {
                    for(int r=0;r<VarList.length;r++)
                    {
                        if(r==0)
                        {
                            SQL = "Insert into "+ TableName +"("+ ColumnList +")Values(";
                            SQL+= "'"+ VarData[r].toString() +"'";
                        }
                        else
                        {
                            SQL+= ",'"+ VarData[r].toString() +"'";
                        }
                    }
                    SQL += ")";

                    Save(SQL);
                    totalDownload+=1;
                }

                //update download status on server
                rep = ExecuteCommandOnServer("Update "+ TableName +" set Upload='2' Where "+ WhereClause);
            }

            DownloadStatus="Total download completed: "+ totalDownload +" of "+ DataArray.length;

            return DownloadStatus;
        }
        catch(Exception e)
        {
            return "Download Error:"+ e.getMessage();
        }
    }*/


    //Execute command on Database Server
    //----------------------------------------------------------------------------------------------
  /*  public String ExecuteCommandOnServer(String SQLStr)
    {
        String response="";
        String result="";
        ExecuteCommand e=new ExecuteCommand();

        try {
            response = e.execute(SQLStr).get();
            if(response.equals("done"))
            {
                result = "done";
            }
            else
            {
                result = "not";
            }
        }
        catch (Exception e1){
            result = "not";
        }

        return result;
    }*/

    // Data Upload to Database Server
    //----------------------------------------------------------------------------------------------
  /*  public String UploadData(String[] DataArray,String TableName,String ColumnList,String UniqueFields)
    {
        String[] D=new String[DataArray.length];
        String[] Col=ColumnList.split(",");
        String VarName[]=UniqueFields.split(",");
        int varPos=0;
        String WhereClause="";

        String response="";
        int totalRec=0;
        for(int i=0;i<DataArray.length;i++)
        {
            //Generate Where Clause
            String VarData[]=DataArray[i].toString().split("\\^");
            varPos=0;
            WhereClause="";

            for(int j=0; j< VarName.length; j++)
            {
                varPos=VarPosition(VarName[j].toString(),Col);
                if(j==0)
                {
                    WhereClause = VarName[j].toString()+"="+ "'"+ VarData[varPos].toString() +"'";
                }
                else
                {
                    WhereClause += " and "+VarName[j].toString()+"="+ "'"+ VarData[varPos].toString() +"'";
                }
            }

            //Calling web service through class: UploadData
            UploadData u = new UploadData();
            u.TableName			  = TableName;
            u.ColumnList		  = ColumnList;
            u.UniqueFieldWithData = WhereClause;
            try{
                response=u.execute(DataArray[i]).get();
                if(response.equalsIgnoreCase("done"))
                {
                    Save("Update " + TableName + " Set Upload='1' where " + WhereClause);
                    totalRec+=1;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Integer.toString(totalRec);
    }
*/
    //Find the variable positions in an array list
    //----------------------------------------------------------------------------------------------
    private int VarPosition(String VariableName, String[] ColumnList)
    {
        int pos=0;
        for(int i=0; i< ColumnList.length; i++)
        {
            if(VariableName.trim().equalsIgnoreCase(ColumnList[i].toString().trim()))
            {
                pos=i;
                i=ColumnList.length;
            }
        }
        return pos;
    }


    // Getting array list for Upload with ^ separator from Cursor
    //----------------------------------------------------------------------------------------------
    public String[] GenerateArrayList(String VariableList,String TableName)
    {
        Cursor cur_H;
       // cur_H = ReadData("Select "+ VariableList +" from "+ TableName);
        cur_H = ReadData("Select "+ VariableList +" from "+ TableName +" where upload='2'");
        cur_H.moveToFirst();
        String[] Data    = new String[cur_H.getCount()];
        String DataList = "";
        String[] Count=VariableList.toString().split(",");
        int RecordCount=0;

        while(!cur_H.isAfterLast())
        {
            for(int c=0; c<Count.length; c++)
            {
                if(c==0)
                {
                    if (cur_H.getString(c) == null)
                        DataList = "";
                    else if(cur_H.getString(c).equals("null"))
                        DataList = "";
                    else
                        DataList = cur_H.getString(c).toString();

                }
                else
                {
                    if (cur_H.getString(c) == null)
                        DataList+="^"+"";
                    else if(cur_H.getString(c).equals("null"))
                        DataList+="^"+"";
                    else
                        DataList+="^"+cur_H.getString(c).toString();
                }
            }
            Data[RecordCount]=DataList;
            RecordCount+=1;
            cur_H.moveToNext();
        }
        cur_H.close();

        return Data;
    }

    // Getting result from database server based on SQL
    //----------------------------------------------------------------------------------------------
    public String ReturnResult(String MethodName, String SQL)
    {
        ReturnResult r=new ReturnResult();
        String response="";
        r.Method_Name = MethodName;
        r.SQLStr=SQL;
        try {
            response=r.execute("").get();
        } catch (InterruptedException e) {

            e.printStackTrace();
        } catch (ExecutionException e) {

            e.printStackTrace();
        }
        return response;
    }
    public String ReturnSingleValueJSON(String SQL)
    {
        DownloadRequestClass dr = new DownloadRequestClass();
        dr.setmethodname("ReturnSingleValue");
        dr.setSQL(SQL);
        Gson gson = new Gson();
        String json = gson.toJson(dr);

        //For Web Api
        //--------------------------------------------------------------------------------------
        DownloadJSONData dload = new DownloadJSONData();
        String response = null;
        try {
            response = dload.execute(json).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return response;
    }
    public String DataStringJSON(String SQL)
    {
        DownloadRequestClass dr = new DownloadRequestClass();
        dr.setmethodname("Existence");
        dr.setSQL(SQL);
        Gson gson = new Gson();
        String json = gson.toJson(dr);

        //For Web Api
        //--------------------------------------------------------------------------------------
        DownloadJSONData dload = new DownloadJSONData();
        String response = null;
        try {
            response = dload.execute(json).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return response;
    }





    public List<String> DataListJSON(String SQL)
    {
        DownloadRequestClass dr = new DownloadRequestClass();
        dr.setmethodname("DownloadData");
        dr.setSQL(SQL);
        Gson gson = new Gson();
        String json = gson.toJson(dr);

        //For Web Api
        //--------------------------------------------------------------------------------------
        DownloadJSONData dload = new DownloadJSONData();
        String response = null;
        try {
            response = dload.execute(json).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        List<String> data = new ArrayList<String>();
        downloadClass responseData = (downloadClass) gson.fromJson(response,downloadClass.class);
        data = responseData.getdata();
        return data;
    }
    //Rebuild Local Database from Server
    //----------------------------------------------------------------------------------------------
    public void RebuildDatabase(String Dist, String Upz, String UN, String ProvType, String ProvCode, String Stype) {
        String SQL = "Select \"TableName\", \"TableScript\" from \"DatabaseTab\"";
        List<String> tableList = new ArrayList<String>();
        tableList = DataListJSON(SQL);

        for (int i = 0; i < tableList.size(); i++) {
            String VarData[] = split(tableList.get(i), '^');
            CreateTable(VarData[0], VarData[1]);
        }

        //------------------------------------------------------------------------------------------
        //Data Sync: Download data from server
        //------------------------------------------------------------------------------------------
        String Res = "";
        String SQLStr = "";

        try {


            //month
            SQLStr = "SELECT * FROM \"FWAUnit\"";
            Res = DownloadJSON(SQLStr, "FWAUnit", "UCode, UName, UNameBan", "UCode");
            //month
            SQLStr = "SELECT * FROM \"month\"";
            Res = DownloadJSON(SQLStr, "month", "id, mName", "id");

            //fpaItem
            SQLStr = "SELECT * FROM \"fpaItem\"";
            Res = DownloadJSON(SQLStr, "fpaItem", "itemCode, itemDes, type", "itemCode,type");

            //ccInfo
     /*       SQLStr = "SELECT * FROM \"ccInfo\"";
            Res = DownloadJSON(SQLStr, "ccInfo", "zilaid, upazilaid, unionid, unionname, wardid, ward, ccid, ccname, hprovdername, mobailno", "ccid");
      */

            //Service Provider

            SQLStr = "Select zillaid,upazilaid,unionid,\"ProvType\",\"ProvCode\",\"ProvName\",\"EnDate\",\"ExDate\",\"Active\",\"DeviceSetting\" from \"ProviderDB\" where ";
            SQLStr += " zillaid='" + Dist + "' and";
            SQLStr += " upazilaid='" + Upz + "' and";
           // SQLStr += " unionid='" + UN + "' and";
            SQLStr += "  \"unionid\" in(Select distinct \"unionid\" from \"ProviderDB\" where \"supervisorCode\"='" + ProvCode + "') and";
            SQLStr += " \"ProvType\"='" + ProvType + "' and";
            SQLStr += " \"supervisorCode\"='" + ProvCode + "' and";
            SQLStr += " \"Active\"='1' UNION ALL Select DISTINCT zillaid,upazilaid,unionid,\"ProvType\" AS \"ProvType\"\n" +
                    "\t,\"ProvCode\" AS \"ProvCode\"\n" +
                    "\t,\"ProvName\" AS \"ProvName\",\"EnDate\",\"ExDate\",\"Active\",\"DeviceSetting\" from \"ProviderDB\" where ";
            SQLStr += " zillaid='" + Dist + "' and";
            SQLStr += " upazilaid='" + Upz + "' and";
            SQLStr += " unionid='" + UN + "' and";
            SQLStr += " \"ProvType\"='" + Stype + "' and";
            SQLStr += " \"ProvCode\"='" + ProvCode + "' and \"Active\"='1'";

            Res = DownloadJSON(SQLStr, "ProviderDB", "zillaid,upazilaid,unionid,ProvType,ProvCode,ProvName,EnDate,ExDate,Active,DeviceSetting", "zillaid,upazilaid,unionid,ProvType,ProvCode");

            //Service Provider Area



            SQLStr = "Select a.zillaid, a.upazilaid, a.unionid, a.mouzaid, a.villageid, a.\"FWAUnit\", a.\"Ward\", a.\"WardNew\", a.\"Block\",a.\"provType\",a.\"provCode\"";
            SQLStr += " from \"ProviderArea\" a";
            SQLStr += " Where a.\"provType\"='" + ProvType + "' and";
            SQLStr += "  a.\"provCode\" in(Select \"ProvCode\" from \"ProviderDB\" where \"supervisorCode\"='" + ProvCode + "')";
            Res = DownloadJSON(SQLStr, "ProviderArea", "zillaid, upazilaid, unionid, mouzaid, villageid, FWAUnit, Ward, WardNew, Block,provType,provCode", "zillaid, upazilaid, unionid, mouzaid, villageid,provCode");

           //DeviceNo
            Save("Delete from DeviceNo");
            Save("Insert into DeviceNo(DeviceNo)Values('" + (ProvType + ProvCode) + "')");

            //Login
            SQLStr = "Select DISTINCT \"ProvCode\" AS \"ProvCode\",\"ProvName\" AS \"ProvName\",''Pass from \"ProviderDB\" Where ";
            SQLStr += " zillaid='" + Dist + "' and";
            SQLStr += " upazilaid='" + Upz + "' and";
            SQLStr += " unionid='" + UN + "' and";
            SQLStr += " \"ProvType\"='" + Stype + "' and";
            SQLStr += " \"ProvCode\"='" + ProvCode + "' and";
            SQLStr += " \"Active\"='1'";
            Res = DownloadJSON(SQLStr, "Login", "UserId,UserName,Pass", "UserId");


            //Division
            SQLStr = "SELECT * FROM \"Division\"";
            Res = DownloadJSON(SQLStr, "Division", "id, division", "id");


            //District/Zilla
            SQLStr = "Select \"DIVID\", \"ZILLAID\", \"ZILLANAMEENG\", \"ZILLANAME\" from \"Zilla\" where \"ZILLAID\"='" + Dist + "'";
            Res = DownloadJSON(SQLStr, "Zilla", "DIVID, ZILLAID, ZILLANAMEENG, ZILLANAME", "ZillaID");

            //Upazila
            SQLStr = "Select \"ZILLAID\", \"UPAZILAID\", \"UPAZILANAMEENG\", \"UPAZILANAME\" from \"Upazila\" where \"ZILLAID\"='" + Dist + "' and \"UPAZILAID\"='" + Upz + "'";
            Res = DownloadJSON(SQLStr, "Upazila", "ZILLAID, UPAZILAID, UPAZILANAMEENG, UPAZILANAME", "ZillaID,UPAZILAID");

            //Unions
             SQLStr = "Select \"ZILLAID\", \"UPAZILAID\", \"MUNICIPALITYID\", \"UNIONID\", \"UNIONNAMEENG\", \"UNIONNAME\" from \"Unions\" where \"ZILLAID\"='" + Dist + "' and \"UPAZILAID\"='" + Upz + "' and";
            SQLStr += "  \"UNIONID\" in (Select distinct \"unionid\" from \"ProviderArea\" where \"provCode\" in (Select \"ProvCode\" from \"ProviderDB\" where \"supervisorCode\"='" + ProvCode + "'))";
            Res = DownloadJSON(SQLStr, "Unions", "ZILLAID, UPAZILAID, MUNICIPALITYID, UNIONID, UNIONNAMEENG, UNIONNAME", "ZillaID,UPAZILAID,UnionId");

           // in (Select distinct "unionid" from "ProviderDB" where "supervisorCode" = '93124')
            //Mouza

            SQLStr = "Select m.\"ZILLAID\", m.\"UPAZILAID\", m.\"MUNICIPALITYID\", m.\"UNIONID\", m.\"MOUZAID\", m.\"RMO\", m.\"MOUZANAMEENG\", m.\"MOUZANAME\" from \"Mouza\" m";
            SQLStr += " inner join \"ProviderArea\" a on m.\"ZILLAID\"=a.zillaid and m.\"UPAZILAID\"=a.upazilaid and m.\"UNIONID\"=a.unionid and m.\"MOUZAID\"=a.mouzaid";
            SQLStr+=" \tINNER JOIN \"ProviderDB\" PDB ON PDB.\"ProvCode\" = a.\"provCode\" ";
            SQLStr += " where PDB.\"ProvType\" = '"+ ProvType +"'  AND PDB.\"supervisorCode\"='" + ProvCode + "' and m.\"ZILLAID\"='" + Dist + "' and m.\"UPAZILAID\"='" + Upz + "' and";
            SQLStr += "  \"UNIONID\" in (Select distinct \"unionid\" from \"ProviderArea\" where \"provCode\" in (Select \"ProvCode\" from \"ProviderDB\" where \"supervisorCode\"='" + ProvCode + "')) and";
            //SQLStr += "  \"UNIONID\" in(Select distinct \"unionid\" from \"ProviderDB\" where \"supervisorCode\"='" + ProvCode + "') and";
            //SQLStr += "  \"MOUZAID\" in(Select distinct \"mouzaid\" from \"ProviderDB\" where \"supervisorCode\"='" + ProvCode + "')";
            SQLStr += "  \"MOUZAID\" in (Select distinct \"mouzaid\" from \"ProviderArea\" where \"provCode\" in (Select \"ProvCode\" from \"ProviderDB\" where \"supervisorCode\"='" + ProvCode + "'))";
            Res = DownloadJSON(SQLStr, "Mouza", "ZILLAID, UPAZILAID, MUNICIPALITYID, UNIONID, MOUZAID, RMO, MOUZANAMEENG, MOUZANAME", "ZillaID,UPAZILAID,UnionId,MOUZAID");


            // SQLStr+=" in (select distinct unionid from \"ProviderArea\" where \"provCode\" in (\tselect \"ProvCode\" from \"ProviderDB\"\twhere \"supervisorCode\" in (\tselect \"ProvCode\" from \"ProviderDB\" where \"supervisorCode\" ='"+ ProvCode+"')))";
            //Village



            SQLStr = "Select v.\"ZILLAID\", v.\"UPAZILAID\", v.\"UNIONID\", v.\"MOUZAID\", v.\"VILLAGEID\", v.\"RMO\", v.\"VILLAGENAMEENG\", v.\"VILLAGENAME\", coalesce(v.\"CRRVILLAGENAME\",'')CRRVILLAGENAME from \"Village\" v";
            SQLStr += " inner join \"ProviderArea\" a on v.\"ZILLAID\"=a.zillaid and v.\"UPAZILAID\"=a.upazilaid and v.\"UNIONID\"=a.unionid and v.\"MOUZAID\"=a.mouzaid and v.\"VILLAGEID\"=a.villageid";
            SQLStr+=" INNER JOIN \"ProviderDB\" PDB ON PDB.\"ProvCode\" = a.\"provCode\" ";
            SQLStr += " where PDB.\"ProvType\" = '"+ ProvType +"' AND PDB.\"supervisorCode\"='" + ProvCode + "' and v.\"ZILLAID\"='" + Dist + "' and v.\"UPAZILAID\"='" + Upz + "' and";
            SQLStr += "  \"UNIONID\" in (Select distinct \"unionid\" from \"ProviderArea\" where \"provCode\" in (Select \"ProvCode\" from \"ProviderDB\" where \"supervisorCode\"='" + ProvCode + "')) and";
            //SQLStr += "  \"UNIONID\" in(Select distinct \"unionid\" from \"ProviderDB\" where \"supervisorCode\"='" + ProvCode + "') and";
            SQLStr += "  \"MOUZAID\" in (Select distinct \"mouzaid\" from \"ProviderArea\" where \"provCode\" in (Select \"ProvCode\" from \"ProviderDB\" where \"supervisorCode\"='" + ProvCode + "')) and";
           //SQLStr += "  \"MOUZAID\" in(Select distinct \"mouzaid\" from \"ProviderDB\" where \"supervisorCode\"='" + ProvCode + "') and";
            //SQLStr += "  \"VILLAGEID\" in(Select distinct \"villageid\" from \"ProviderDB\" where \"supervisorCode\"='" + ProvCode + "')";
            SQLStr += "  \"VILLAGEID\" in (Select distinct \"villageid\" from \"ProviderArea\" where \"provCode\" in (Select \"ProvCode\" from \"ProviderDB\" where \"supervisorCode\"='" + ProvCode + "'))";

            Res = DownloadJSON(SQLStr, "Village", "ZILLAID, UPAZILAID, UNIONID, MOUZAID, VILLAGEID, RMO, VILLAGENAMEENG, VILLAGENAME, CRRVILLAGENAME", "ZillaID,UPAZILAID,UnionId,MOUZAID,VillageID");


            // epiScheduler

            //Download EPI Scheduler by New Table
           // jumpTime += 1;
           // Global.getInstance().setProgressMessage("Downloading EPI Scheduler");
           // progressHandler.sendMessage(progressHandler.obtainMessage());
            SQLStr = "SELECT \"Div\"\n" +
                    ",\"Dist\"\n" +
                    ",\"Upz\"\n" +
                    ",\"UN\"\n" +
                    ",\"wordOld\"\n" +
                    ",\"subBlockId\"\n" +
                    ",\"scheduleYear\"\n" +
                    ",\"schedulerId\"\n" +
                    ",\"scheduleDate\"\n" +
                    ",\"centerName\"\n" +
                    ",\"centerType\"\n" +
                    ",\"KhanaNoFrom\"\n" +
                    ",\"KhanaNoTo\"\n" +
                    ",\"KhanaTotal\"\n" +
                    ",\"systemEntryDate\"\n" +
                    ",\"modifyDate\"\n" +
                    ",\"upload\"\n" +
                    "FROM \"public\".\"epiSchedulerUpdate\"\n" +
                    "where \"Dist\"='" + Dist + "' and \"Upz\"='" + Upz + "'" +
                   // " and \"UN\"='" + UN + "'" +
                     "and  \"UN\" in(Select distinct \"unionid\" from \"ProviderDB\" where \"supervisorCode\"='" + ProvCode + "') ";
                  //  "and \"wordOld\" in(select distinct cast(\"Ward\" as integer) from \"ProviderArea\"\n" +
                    //"where \"provCode\"='" + ProvCode + "')";
                  // "where  \"provCode\" in(Select distinct \"ProvCode\" from \"ProviderDB\" where \"supervisorCode\"='" + ProvCode + "')";
                    //"where \"provCode\"='" + ProvCode + "')";
                  Res = DownloadJSON(SQLStr, "epiSchedulerUpdate", "Div,Dist,Upz,UN,wordOld,subBlockId,scheduleYear,schedulerId,scheduleDate,centerName,centerType,KhanaNoFrom,KhanaNoTo,KhanaTotal,systemEntryDate,modifyDate,upload", "Dist,Upz,UN,wordOld,subBlockId,scheduleYear,schedulerId");



            //ElcoEvent
            SQLStr = "select \"EVCode\", \"EVName\" from \"ElcoEvent\"";
            Res = DownloadJSON(SQLStr, "ElcoEvent", "EVCode, EVName", "EVCode");

          //BrandMethod
            SQLStr = "SELECT * FROM \"BrandMethod\"";
            Res = DownloadJSON(SQLStr, "BrandMethod", "brandCode, brandName", "brandCode");

            //FWA Unit
            SQLStr = "select \"UCode\",\"UName\",\"UNameBan\" from \"FWAUnit\"";
            Res = DownloadJSON(SQLStr, "FWAUnit", "UCode,UName,UNameBan", "UCode");

            //Block
            SQLStr = "select \"BCode\",\"BName\",\"BNameBan\" from \"HABlock\"";
            Res = DownloadJSON(SQLStr, "HABlock", "BCode,BName,BNameBan", "BCode");


            //item
            SQLStr = "select \"itemCode\",\"itemName\", \"brand\", \"unit\", \"status\" from item";
            Res = DownloadJSON(SQLStr, "item", "itemCode, itemName, brand, unit, status", "itemCode");



            //Download OCP Code List
            SQLStr = "select \"ocpCode\", \"ocpName\", \"dCode\", \"upz\", \"ocpSequence\" from \"ocpList\" order by \"ocpSequence\"";
            Res = DownloadJSON(SQLStr, "ocplist", "ocpCode, ocpName, DCode, Upz, ocpSequence", "ocpCode");





            SQLStr = "SELECT * FROM \"AttendantDesignation\"";
            Res = DownloadJSON(SQLStr, "AttendantDesignation", "attendantCode, attendantDesig", "attendantCode");



            //currentStock
            SQLStr = "select \"providerId\",\"itemCode\",\"stockQty\", \"systemEntryDate\", \"modifyDate\", \"upload\" from \"currentStock\" where \"providerId\"='" + ProvCode + "'";
            Res = DownloadJSON(SQLStr, "currentStock", "providerId, itemCode, stockQty, systemEntryDate, modifyDate, upload", "providerId,itemCode");

            //download ServiceTables
            DownloadServiceTables(ProvCode);




        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void RebuildDatabaseHI(String Dist, String Upz, String UN, String ProvType, String ProvCode, String Stype) {
        String SQL = "Select \"TableName\", \"TableScript\" from \"DatabaseTab\"";
        List<String> tableList = new ArrayList<String>();
        tableList = DataListJSON(SQL);

        for (int i = 0; i < tableList.size(); i++) {
            String VarData[] = split(tableList.get(i), '^');
            CreateTable(VarData[0], VarData[1]);
        }

        //------------------------------------------------------------------------------------------
        //Data Sync: Download data from server
        //------------------------------------------------------------------------------------------
        String Res = "";
        String SQLStr = "";

        try {


          //Service Provider

            SQLStr = "Select zillaid,upazilaid,unionid,\"ProvType\",\"ProvCode\",\"ProvName\",\"EnDate\",\"ExDate\",\"Active\",\"DeviceSetting\" from \"ProviderDB\" where ";
            SQLStr += " zillaid='" + Dist + "' and";
            SQLStr += " upazilaid='" + Upz + "' and";
            // SQLStr += " unionid='" + UN + "' and";
            SQLStr += " \"ProvType\"='" + ProvType + "' and";
            SQLStr += " \"supervisorCode\"='" + ProvCode + "' and";
            SQLStr += " \"Active\"='1' UNION ALL Select DISTINCT zillaid,upazilaid,unionid,\"ProvType\" AS \"ProvType\"\n" +
                    "\t,\"ProvCode\" AS \"ProvCode\"\n" +
                    "\t,\"ProvName\" AS \"ProvName\",\"EnDate\",\"ExDate\",\"Active\",\"DeviceSetting\" from \"ProviderDB\" where ";
            SQLStr += " zillaid='" + Dist + "' and";
            SQLStr += " upazilaid='" + Upz + "' and";
            // SQLStr += " unionid='" + UN + "' and";
            SQLStr += " \"ProvType\"='" + Stype + "' and";
            SQLStr += " \"ProvCode\"='" + ProvCode + "' and \"Active\"='1'";
            SQLStr += " UNION ALL Select DISTINCT zillaid,upazilaid,unionid,\"ProvType\" AS \"ProvType\"\n" +
                    "\t,\"ProvCode\" AS \"ProvCode\"\n" +
                    "\t,\"ProvName\" AS \"ProvName\",\"EnDate\",\"ExDate\",\"Active\",\"DeviceSetting\" from \"ProviderDB\" where \"supervisorCode\" in (select \"ProvCode\" from \"ProviderDB\" where \"supervisorCode\" ='"+ProvCode+"')";


            Res = DownloadJSON(SQLStr, "ProviderDB", "zillaid,upazilaid,unionid,ProvType,ProvCode,ProvName,EnDate,ExDate,Active,DeviceSetting", "zillaid,upazilaid,unionid,ProvType,ProvCode");



            SQLStr = "Select a.zillaid, a.upazilaid, a.unionid, a.mouzaid, a.villageid, a.\"FWAUnit\", a.\"Ward\", a.\"WardNew\", a.\"Block\",a.\"provType\",a.\"provCode\"";
            SQLStr += " from \"ProviderArea\" a";
            SQLStr += " Where a.\"provCode\"";
            SQLStr += " in (Select \"ProvCode\" from \"ProviderDB\" where \"supervisorCode\" in ( Select \"ProvCode\" from \"ProviderDB\"where \"supervisorCode\"='" + ProvCode + "'))";

            Res = DownloadJSON(SQLStr, "ProviderArea", "zillaid, upazilaid, unionid, mouzaid, villageid, FWAUnit, Ward, WardNew, Block,provType,provCode", "zillaid, upazilaid, unionid, mouzaid, villageid,provCode");



            //DeviceNo
            Save("Delete from DeviceNo");
            Save("Insert into DeviceNo(DeviceNo)Values('" + (ProvType + ProvCode) + "')");

            //Login
            SQLStr = "Select DISTINCT \"ProvCode\" AS \"ProvCode\",\"ProvName\" AS \"ProvName\",''Pass from \"ProviderDB\" Where ";
            SQLStr += " zillaid='" + Dist + "' and";
            SQLStr += " upazilaid='" + Upz + "' and";
            SQLStr += " unionid='" + UN + "' and";
            SQLStr += " \"ProvType\"='" + Stype + "' and";
            SQLStr += " \"ProvCode\"='" + ProvCode + "' and";
            SQLStr += " \"Active\"='1'";
            Res = DownloadJSON(SQLStr, "Login", "UserId,UserName,Pass", "UserId");


          /*  //Division
            SQLStr = "SELECT * FROM \"ccInfo\"";
            Res = DownloadJSON(SQLStr, "ccInfo", "zilaid, upazilaid,unionid,unionname,wardid,ward,ccid,ccname,hprovdername,mobailno", "ccid");
*/
            //Division
            SQLStr = "SELECT * FROM \"Division\"";
            Res = DownloadJSON(SQLStr, "Division", "id, division", "id");


            //District
            SQLStr = "Select \"DIVID\", \"ZILLAID\", \"ZILLANAMEENG\", \"ZILLANAME\" from \"Zilla\" where \"ZILLAID\"='" + Dist + "'";
            Res = DownloadJSON(SQLStr, "Zilla", "DIVID, ZILLAID, ZILLANAMEENG, ZILLANAME", "ZillaID");

            //Upazila
            SQLStr = "Select \"ZILLAID\", \"UPAZILAID\", \"UPAZILANAMEENG\", \"UPAZILANAME\" from \"Upazila\" where \"ZILLAID\"='" + Dist + "' and \"UPAZILAID\"='" + Upz + "'";
            Res = DownloadJSON(SQLStr, "Upazila", "ZILLAID, UPAZILAID, UPAZILANAMEENG, UPAZILANAME", "ZillaID,UPAZILAID");

            //Unions
            SQLStr = "Select \"ZILLAID\", \"UPAZILAID\", \"MUNICIPALITYID\", \"UNIONID\", \"UNIONNAMEENG\", \"UNIONNAME\" from \"Unions\" where \"ZILLAID\"='" + Dist + "' and \"UPAZILAID\"='" + Upz + "' and \"UNIONID\"='" + UN + "'";
            SQLStr+= " UNION ALL Select \"ZILLAID\", \"UPAZILAID\", \"MUNICIPALITYID\", \"UNIONID\", \"UNIONNAMEENG\", \"UNIONNAME\" from \"Unions\" where \"ZILLAID\"='" + Dist + "' and \"UPAZILAID\"='" + Upz + "' and \"UNIONID\""; //
            SQLStr+=" in (select distinct unionid from \"ProviderArea\" where \"provCode\" in (\tselect \"ProvCode\" from \"ProviderDB\"\twhere \"supervisorCode\" in (\tselect \"ProvCode\" from \"ProviderDB\" where \"supervisorCode\" ='"+ ProvCode+"')))";
            Res = DownloadJSON(SQLStr, "Unions", "ZILLAID, UPAZILAID, MUNICIPALITYID, UNIONID, UNIONNAMEENG, UNIONNAME", "ZillaID,UPAZILAID,UnionId");


            //Mouza
            SQLStr = "Select m.\"ZILLAID\", m.\"UPAZILAID\", m.\"MUNICIPALITYID\", m.\"UNIONID\", m.\"MOUZAID\", m.\"RMO\", m.\"MOUZANAMEENG\", m.\"MOUZANAME\" from \"Mouza\" m";
            SQLStr += " inner join \"ProviderArea\" a on m.\"ZILLAID\"=a.zillaid and m.\"UPAZILAID\"=a.upazilaid and m.\"UNIONID\"=a.unionid and m.\"MOUZAID\"=a.mouzaid";
            SQLStr+=" \tINNER JOIN \"ProviderDB\" PDB ON PDB.\"ProvCode\" = a.\"provCode\" ";
            //SQLStr += " where PDB.\"ProvType\" = '"+ ProvType +"'  AND PDB.\"supervisorCode\"='" + ProvCode + "' and m.\"ZILLAID\"='" + Dist + "' and m.\"UPAZILAID\"='" + Upz + "' and m.\"UNIONID\"='" + UN + "'";
            SQLStr += " where m.\"ZILLAID\"='" + Dist + "' and m.\"UPAZILAID\"='" + Upz + "' and m.\"UNIONID\"";
            SQLStr+=" in (select distinct unionid from \"ProviderArea\" where \"provCode\" in (\tselect \"ProvCode\" from \"ProviderDB\"\twhere \"supervisorCode\" in (\tselect \"ProvCode\" from \"ProviderDB\" where \"supervisorCode\" ='"+ ProvCode+"')))";
            Res = DownloadJSON(SQLStr, "Mouza", "ZILLAID, UPAZILAID, MUNICIPALITYID, UNIONID, MOUZAID, RMO, MOUZANAMEENG, MOUZANAME", "ZillaID,UPAZILAID,UnionId,MOUZAID");

            //Village
            SQLStr = "Select v.\"ZILLAID\", v.\"UPAZILAID\", v.\"UNIONID\", v.\"MOUZAID\", v.\"VILLAGEID\", v.\"RMO\", v.\"VILLAGENAMEENG\", v.\"VILLAGENAME\", coalesce(v.\"CRRVILLAGENAME\",'')CRRVILLAGENAME from \"Village\" v";
            SQLStr += " inner join \"ProviderArea\" a on v.\"ZILLAID\"=a.zillaid and v.\"UPAZILAID\"=a.upazilaid and v.\"UNIONID\"=a.unionid and v.\"MOUZAID\"=a.mouzaid and v.\"VILLAGEID\"=a.villageid";
            SQLStr+=" INNER JOIN \"ProviderDB\" PDB ON PDB.\"ProvCode\" = a.\"provCode\" ";
           // SQLStr += " where PDB.\"ProvType\" = '"+ ProvType +"' AND PDB.\"supervisorCode\"='" + ProvCode + "' and v.\"ZILLAID\"='" + Dist + "' and v.\"UPAZILAID\"='" + Upz + "' and v.\"UNIONID\"='" + UN + "'";
            SQLStr += " where v.\"ZILLAID\"='" + Dist + "' and v.\"UPAZILAID\"='" + Upz + "' and v.\"UNIONID\"";
            SQLStr+=" in (select distinct unionid from \"ProviderArea\" where \"provCode\" in (\tselect \"ProvCode\" from \"ProviderDB\"\twhere \"supervisorCode\" in (\tselect \"ProvCode\" from \"ProviderDB\" where \"supervisorCode\" ='"+ ProvCode+"')))";
            Res = DownloadJSON(SQLStr, "Village", "ZILLAID, UPAZILAID, UNIONID, MOUZAID, VILLAGEID, RMO, VILLAGENAMEENG, VILLAGENAME, CRRVILLAGENAME", "ZillaID,UPAZILAID,UnionId,MOUZAID,VillageID");


            //month
            SQLStr = "SELECT * FROM \"FWAUnit\"";
            Res = DownloadJSON(SQLStr, "FWAUnit", "UCode, UName, UNameBan", "UCode");
            //month
            SQLStr = "SELECT * FROM \"month\"";
            Res = DownloadJSON(SQLStr, "month", "id, mName", "id");

            //fpaItem
            SQLStr = "SELECT * FROM \"fpaItem\"";
            Res = DownloadJSON(SQLStr, "fpaItem", "itemCode, itemDes, type", "itemCode,type");
           //ElcoEvent
            SQLStr = "select \"EVCode\", \"EVName\" from \"ElcoEvent\"";
            Res = DownloadJSON(SQLStr, "ElcoEvent", "EVCode, EVName", "EVCode");

            //BrandMethod
            SQLStr = "SELECT * FROM \"BrandMethod\"";
            Res = DownloadJSON(SQLStr, "BrandMethod", "brandCode, brandName", "brandCode");

            //FWA Unit
            SQLStr = "select \"UCode\",\"UName\",\"UNameBan\" from \"FWAUnit\"";
            Res = DownloadJSON(SQLStr, "FWAUnit", "UCode,UName,UNameBan", "UCode");

            //Block
            SQLStr = "select \"BCode\",\"BName\",\"BNameBan\" from \"HABlock\"";
            Res = DownloadJSON(SQLStr, "HABlock", "BCode,BName,BNameBan", "BCode");


            //item
            SQLStr = "select \"itemCode\",\"itemName\", \"brand\", \"unit\", \"status\" from item";
            Res = DownloadJSON(SQLStr, "item", "itemCode, itemName, brand, unit, status", "itemCode");



            //currentStock
            SQLStr = "select \"providerId\",\"itemCode\",\"stockQty\", \"systemEntryDate\", \"modifyDate\", \"upload\" from \"currentStock\" where \"providerId\"='" + ProvCode + "'";
            Res = DownloadJSON(SQLStr, "currentStock", "providerId, itemCode, stockQty, systemEntryDate, modifyDate, upload", "providerId,itemCode");


            // epiScheduler


            SQLStr = "SELECT \"Div\"\n" +
                    ",\"Dist\"\n" +
                    ",\"Upz\"\n" +
                    ",\"UN\"\n" +
                    ",\"wordOld\"\n" +
                    ",\"subBlockId\"\n" +
                    ",\"scheduleYear\"\n" +
                    ",\"schedulerId\"\n" +
                    ",\"scheduleDate\"\n" +
                    ",\"centerName\"\n" +
                    ",\"centerType\"\n" +
                    ",\"KhanaNoFrom\"\n" +
                    ",\"KhanaNoTo\"\n" +
                    ",\"KhanaTotal\"\n" +
                    ",\"systemEntryDate\"\n" +
                    ",\"modifyDate\"\n" +
                    ",\"upload\"\n" +
                    "FROM \"public\".\"epiSchedulerUpdate\"\n" +
                    "where \"Dist\"='" + Dist + "' and \"Upz\"='" + Upz + "'" +
                    // " and \"UN\"='" + UN + "'" +
                    "and  \"UN\" in(Select distinct \"unionid\" from \"ProviderDB\" where \"supervisorCode\"='" + ProvCode + "') ";
                    //"and \"wordOld\" in(select distinct cast(\"Ward\" as integer) from \"ProviderArea\"\n" +
                    //"where \"provCode\"='" + ProvCode + "')";
                   // "where  \"provCode\" in(Select distinct \"ProvCode\" from \"ProviderDB\" where \"supervisorCode\"='" + ProvCode + "')";
            //"where \"provCode\"='" + ProvCode + "')";
            Res = DownloadJSON(SQLStr, "epiSchedulerUpdate", "Div,Dist,Upz,UN,wordOld,subBlockId,scheduleYear,schedulerId,scheduleDate,centerName,centerType,KhanaNoFrom,KhanaNoTo,KhanaTotal,systemEntryDate,modifyDate,upload", "Dist,Upz,UN,wordOld,subBlockId,scheduleYear,schedulerId");

            DownloadServiceTables(ProvCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void Sync_HealthID(String PType, String PCode)
    {
        Connection C = new Connection(ud_context);
        String SQL = "";

        //Trigger for updating client map
        SQL = "create trigger if not exists tri_hid_update after";
        SQL += " update on Member";
        SQL += " for each row";
        SQL += " when (New.HealthId<>Old.HealthId)";
        SQL += " begin";
        SQL += "    Update ClientMap set HealthId=New.HealthId where HealthId=Old.HealthId;";
        SQL += " end";
        C.Save(SQL);

        //Total number of records need to dowload
        SQL = "Select count(*)totalRecord from \"Member\" as t";
        SQL += " inner join \"ProviderArea\" a on t.\"Dist\"=a.\"zillaid\" and t.\"Upz\"=a.\"upazilaid\" and t.\"UN\"=a.\"unionid\" and t.\"Mouza\"=a.\"mouzaid\" and t.\"Vill\"=a.\"villageid\" and a.\"provType\"='" + PType + "' and a.\"provCode\"='" + PCode + "'";
        SQL += " where not exists(select * from data_sync_management where";
        SQL += " lower(\"tableName\")  = 'member' and";
        SQL += " to_date(\"modifyDate\",'yyyy-mm-dd') = to_date(t.\"EnDt\",'yyyy-mm-dd') and";
        SQL += " \"recordId\" = cast(\"Dist\" as text)||cast(\"Upz\" as text)||cast(\"UN\" as text)||cast(\"Mouza\" as text)||cast(\"Vill\" as text)||cast(\"HHNo\" as text)||cast(\"SNo\" as text)";
        SQL += " and cast(\"provType\" as text)='" + PType + "' and cast(\"provCode\" as text)='" + PCode + "')";
        int totalRecord = Integer.valueOf(C.ReturnSingleValueJSON(SQL));

        int batchSize = 5000;
        int totalBatch = (totalRecord/batchSize)+1;
        for(int i = 0; i < totalBatch; i++) {
            SQL = "Select \"Dist\", \"Upz\", \"UN\", \"Mouza\", \"Vill\", \"HHNo\", \"SNo\",\"HealthID\",to_date(\"EnDt\",'yyyy-mm-dd')modifyDate from \"Member\" as t";
            SQL += " inner join \"ProviderArea\" a on t.\"Dist\"=a.\"zillaid\" and t.\"Upz\"=a.\"upazilaid\" and t.\"UN\"=a.\"unionid\" and t.\"Mouza\"=a.\"mouzaid\" and t.\"Vill\"=a.\"villageid\" and a.\"provType\"='" + PType + "' and a.\"provCode\"='" + PCode + "'";
            SQL += " where not exists(select * from data_sync_management where";
            SQL += " lower(\"tableName\")  = 'member' and";
            SQL += " to_date(\"modifyDate\",'yyyy-mm-dd') = to_date(t.\"EnDt\",'yyyy-mm-dd') and";
            SQL += " \"recordId\" = cast(\"Dist\" as text)||cast(\"Upz\" as text)||cast(\"UN\" as text)||cast(\"Mouza\" as text)||cast(\"Vill\" as text)||cast(\"HHNo\" as text)||cast(\"SNo\" as text)";
            SQL += " and cast(\"provType\" as text)='" + PType + "' and cast(\"provCode\" as text)='" + PCode + "') limit " + batchSize;

            C.DownloadJSON_HealthID(SQL, "Member", "Dist, Upz, UN, Mouza, Vill, HHNo, SNo, HealthID, modifyDate", "Dist, Upz, UN, Mouza, Vill, HHNo, SNo");
        }


    }

    public void GenerateElco() {
        //Updating ElcoInformation group_concat( ifnull( A.NameENG, '' ) || '(' || ifnull( B.NameEng, '' ) || ')' ) AS ElcoName
        String SQL = "Create table elcodetails as\n" +
                "SELECT A.DisT,\n" +
                "       A.Upz,\n" +
                "       A.UN,\n" +
                "       A.MOUZA,\n" +
                "       A.Vill,\n" +
                "       A.HHno AS hhno,\n" +
                "       A.ElcoName AS Elco\n" +
                "  FROM ( \n" +
                "  \n" +
                "    SELECT A.DisT,\n" +
                "           A.Upz,\n" +
                "           A.UN,\n" +
                "           A.MOUZA,\n" +
                "           A.Vill,\n" +
                "           A.HHno,\n" +
                "           group_concat( ifnull( ' ' || A.NameENG, '' ) || ' (' || ifnull( B.NameEng, '' ) || ') ' ) AS ElcoName\n" +
                "      FROM Member A\n" +
                "           LEFT OUTER JOIN Member B\n" +
                "                        ON a.dist = b.dist \n" +
                "    AND\n" +
                "    a.upz = b.upz \n" +
                "    AND\n" +
                "    a.un = b.un \n" +
                "    AND\n" +
                "    a.mouza = b.mouza \n" +
                "    AND\n" +
                "    a.vill = b.vill \n" +
                "    AND\n" +
                "    a.hhno = b.hhno \n" +
                "    AND\n" +
                "    CAST ( a.spno1 AS int ) = b.sno\n" +
                "     WHERE A.MS = '2' \n" +
                "           AND\n" +
                "           A.Sex = 2\n" +
                "     GROUP BY A.DisT,\n" +
                "              A.Upz,\n" +
                "              A.UN,\n" +
                "              A.MOUZA,\n" +
                "              A.Vill,\n" +
                "              A.HHno \n" +
                ") \n" +
                "AS A\n" +
                "       INNER JOIN Household household\n" +
                "               ON A.HHNo = household.HHNo\n" +
                " WHERE A.dist = household.dist \n" +
                "       AND\n" +
                "       A.upz = household.upz \n" +
                "       AND\n" +
                "       A.un = household.un \n" +
                "       AND\n" +
                "       A.mouza = household.mouza \n" +
                "       AND\n" +
                "       A.vill = household.vill \n" +
                "       AND\n" +
                "       A.hhno = household.hhno\n" +
                " ORDER BY A.DisT,\n" +
                "           A.Upz,\n" +
                "           A.UN,\n" +
                "           A.MOUZA,\n" +
                "           A.Vill,\n" +
                "           A.HHno;\n";
        Save(SQL);
        SQL = " update Household set Elco = \n" +
                "(select Elco from elcodetails h \n" +
                "where h.DisT = Household.DisT and\n" +
                "       \n" +
                "       h.Upz = Household.Upz and\n" +
                "       h.UN = Household.UN and \n" +
                "       h.MOUZA = Household.MOUZA and\n" +
                "       h.Vill = Household.Vill and h.hhno= household.hhno);";
        Save(SQL);
        SQL = "DROP TABLE elcodetails;";
        Save(SQL);
    }
    public void GenerateHHHTotalMem()
    {
        String SQL = "";
        SQL = "Create table totalmem as";
        SQL += " select dist,upz,un,mouza,vill,hhno,count(*)totalmem from Member";
        SQL += " group by dist,upz,un,mouza,vill,hhno";

        Save(SQL);

        SQL = "Create table headName as";
        SQL += " select dist,upz,un,mouza,vill,provtype,provcode,hhno,nameeng headname from Member where rth='01' and length(extype)=0";
        Save(SQL);

        SQL = "update household set HHHead=(select headname from headname h where h.dist=household.dist and h.upz=household.upz and h.un=household.un and h.mouza=household.mouza and h.vill=household.vill and  h.hhno=household.hhno)";
        Save(SQL);

        SQL = "update household set TotalMem=(select totalmem from totalmem h where h.dist=household.dist and h.upz=household.upz and h.un=household.un and h.mouza=household.mouza and h.vill=household.vill  and h.hhno=household.hhno)";
        Save(SQL);

        Save("drop table totalmem");
        Save("drop table headName");

    }

    public void DownloadServiceTables(String provCode){



        //fpaWorkPlanMaster
        String VariableList = "";
        String  sql = "select  \"workPlanId\", \"workAreaId\", \"providerId\", \"month\", \"status\", \"systemEntryDate\", \"modifyDate\", \"upload\" \n" +
                "from \"workPlanMaster\" \n" +
                "where \"providerId\" =" + provCode;

        VariableList = "workPlanId,workAreaId,providerId,month,status,systemEntryDate,modifyDate,upload";
        DownloadJSON(sql, "workPlanMaster", VariableList, "workPlanId, providerId, month");

        //fpaWorkPlanDetail

        sql = "select  \"workPlanId\", \"item\", \"workPlanDate\", \"unitNo\", \"village\", \"elcoFrom\", \"elcoTo\", \"leaveType\", \"providerId\", \"systemEntryDate\", \"modifyDate\", \"otherDec\", \"remarks\", \"upload\" \n" +
                "from \"workPlanDetail\" \n" +
                "where \"providerId\" =" + provCode;

        VariableList = "workPlanId,fpaItem,workPlanDate,unitNo,village,elcoFrom,elcoTo,leaveType,providerId,systemEntryDate,modifyDate,otherDec,remarks,upload";
        DownloadJSON(sql, "workPlanDetail", VariableList, "workPlanId, fpaItem, workPlanDate, providerId");

        // fpiMonitoring
        sql = "select  \"vDate\", \"fpaCode\", \"fpaUnit\", \"fpaVill\", \"fpaAdvance\", \"needItems1\", \"needItems2\", \"needItems3\", \"needItems4\", \"needItems5\", \"needItems6\", \"needItems7\", \"needItems8\", \"startTime\", \"endTime\", \"userId\" , \"enDt\" , \"upload\"  \n" +
                "from \"fpiMonitoring\" \n" +
                "where cast(\"userId\" as Integer) =" + provCode;
        VariableList = "vDate,fpaCode,fpaUnit,fpaVill,fpaAdvance,needItems1,needItems2,needItems3,needItems4,needItems5,needItems6,needItems7,needItems8,startTime,endTime,userId,enDt,upload";
        DownloadJSON(sql, "fpiMonitoring", VariableList, "vDate, fpaCode");


        // HouseholdFPI
        sql = "select  \"Dist\", \"Upz\", \"UN\", \"Mouza\", \"Vill\", \"ProvCode\", \"HHNo\", \"houseHoldStatus\", \"causeOfHouseHoldStatus\", \"subBlockStatus\", \"pAddrStatus\", \"permaAddressStatus\", \"religionStatus\", \"StartTime\", \"EndTime\", \"Lat\" , \"Lon\" , \"UserId\", \"EnDt\", \"Upload\"  \n" +
                "from \"HouseholdFPI\" \n" +
                "where cast(\"UserId\" as Integer) =" + provCode;
        VariableList = "Dist,Upz,UN,Mouza,Vill,ProvType,ProvCode,HHNo,houseHoldStatus,causeOfHouseHoldStatus,subBlockStatus,pAddrStatus,permaAddressStatus,religionStatus,StartTime,EndTime,Lat,Lon,UserId,EnDt,Upload";
        DownloadJSON(sql, "HouseholdFPI", VariableList, "Dist,  Upz, UN,Mouza,Vill,HHNo");

        // memberfpi
        sql = "select  \"dist\", \"upz\", \"un\", \"mouza\", \"vill\", \"provtype\", \"provcode\", \"hhno\", \"sno\", \"healthid\", \"nameengstatus\", \"rthstatus\", \"havenidstatus\", \"nidstatus\", \"havebrstatus\", \"bridstatus\" , \"mobileno1status\" , \"mobileno2status\", \"dobstatus\", \"agestatus\", \"dobsourcestatus\", \"bplacestatus\", \"fnostatus\", \"fatherstatus\", \"mnostatus\", \"motherstatus\", \"sexstatus\", \"msstatus\", \"spno1status\", \"spno2status\", \"spno3status\", \"spno4status\", \"edustatus\", \"relstatus\", \"nationalitystatus\", \"ocpstatus\", \"starttime\", \"entype\", \"endate\", \"extype\" , \"exdate\" , \"endtime\" , \"lat\" , \"lon\" , \"userid\", \"endt\", \"upload\"  \n" +
                "from \"memberfpi\" \n" +
                "where cast(\"userid\" as Integer) =" + provCode;
        VariableList = "dist,upz,un,mouza,vill,provtype,provcode,hhno,sno,healthid,nameengstatus,rthstatus,havenidstatus,nidstatus,havebrstatus,bridstatus,mobileno1status,mobileno2status,dobstatus,agestatus,dobsourcestatus,bplacestatus,fnostatus,fatherstatus,mnostatus,motherstatus,sexstatus,msstatus,spno1status,spno2status,spno3status,spno4status,edustatus,relstatus,nationalitystatus,ocpstatus,starttime,entype,endate,extype,exdate,endtime,lat,lon,userid,endt,upload";
        DownloadJSON(sql, "memberfpi", VariableList, "dist,upz,un,mouza,vill, hhno, sno");



        // sesfpi
        sql = "select  \"dist\", \"upz\", \"un\", \"mouza\", \"vill\", \"provtype\", \"provcode\", \"hhno\", \"sesstatus\", \"q1status\", \"q11status\", \"q2status\", \"q21status\", \"q3astatus\", \"q3bstatus\", \"q3cstatus\" , \"q3dstatus\" , \"q3estatus\", \"q3fstatus\", \"q3gstatus\", \"q3hstatus\", \"q3istatus\", \"q3jstatus\", \"q3kstatus\", \"q3lstatus\", \"q3mstatus\", \"q3nstatus\", \"q3ostatus\", \"q3pstatus\", \"q4status\", \"q41status\", \"q5status\", \"q51status\", \"q6status\", \"q61status\", \"q7status\", \"q71status\", \"q8astatus\", \"q8bstatus\", \"q8cstatus\", \"q8dstatus\", \"q8estatus\", \"q9status\", \"q10status\", \"q11astatus\", \"starttime\", \"endtime\", \"userid\", \"endt\", \"upload\"  \n" +
                "from \"sesfpi\" \n" +
                "where cast(\"userid\" as Integer) =" + provCode;
        VariableList = "dist,upz,un,mouza,vill,provtype,provcode,hhno,sesstatus,q1status,q11status,q2status,q21status,q3astatus,q3bstatus,q3cstatus,q3dstatus,q3estatus,q3fstatus,q3gstatus,q3hstatus,q3istatus,q3jstatus,q3kstatus,q3lstatus,q3mstatus,q3nstatus,q3ostatus,q3pstatus,q4status,q41status,q5status,q51status,q6status,q61status,q7status,q71status,q8astatus,q8bstatus,q8cstatus,q8dstatus,q8estatus,q9status,q10status,q11astatus,starttime,endtime,userid,endt,upload";
        DownloadJSON(sql, "sesfpi", VariableList, "dist,upz,un,mouza,vill, hhno");

// elcoFPI
        sql = "select  \"healthId\", \"providerId\", \"hhStatus\", \"haHHNo\", \"elcoNo\", \"husbandName\", \"husbandAge\", \"domSource\", \"marrDate\", \"marrAge\", \"son\", \"dau\", \"regDT\", \"systemEntryDate\", \"modifyDate\", \"tt1\" , \"tt2\" , \"tt3\", \"tt4\", \"tt5\", \"upload\"   \n" +
                "from \"elcoFPI\" \n" +
                "where \"providerId\" =" + provCode;
        VariableList = "healthId, providerId, hhStatus, haHHNo, elcoNo, husbandName,husbandAge,domSource, marrDate, marrAge, son, dau, regDT, systemEntryDate,modifyDate,tt1,tt2,tt3,tt4,tt5, upload";
        DownloadJSON(sql, "elcoFPI", VariableList, "healthId");


        // elcoVisitFPI
        sql = "select  \"healthId\", \"pregNo\", \"providerId\", \"transactionId\", \"visit\", \"vDate\", \"visitStatus\", \"currStatus\", \"newOld\", \"mDate\", \"sSource\", \"qty\", \"unit\", \"brand\", \"validity\", \"dayMonYear\" , \"referPlace\" , \"syrinsQty\", \"mrSource\", \"MRDate\", \"MRAge\", \"systemEntryDate\" , \"modifyDate\" , \"upload\"    \n" +
                "from \"elcoVisitFPI\" \n" +
                "where \"providerId\" =" + provCode;
        VariableList = "healthId,pregNo,providerId,transactionId,visit,vDate,visitStatus,currStatus,newOld,mDate,sSource,qty,unit,brand,validity,dayMonYear,referPlace,syrinsQty,mrSource,MRDate,MRAge,systemEntryDate,modifyDate,upload";
        DownloadJSON(sql, "elcoVisitFPI", VariableList, "healthId,visit");


        // pregWomenFPI
        sql = "select  \"healthId\", \"pregNo\", \"providerId\", \"LMP\", \"EDD\", \"para\", \"gravida\", \"lastChildAge\", \"riskHistoryNote\", \"pregRefer\", \"systemEntryDate\", \"upload\" \n" +
                "from \"pregWomenFPI\" \n" +
                "where \"providerId\" =" + provCode;
        VariableList = "healthId, pregNo, providerId,LMP, EDD, para, gravida, lastChildAge, riskHistoryNote,pregRefer, systemEntryDate, upload";
        DownloadJSON(sql, "pregWomenFPI", VariableList, "healthId, pregNo");


        // ancServiceFPI
        sql = "select  \"healthId\", \"pregNo\", \"serviceId\", \"providerId\", \"visitSource\", \"visitDate\", \"visitMonth\", \"ironFolStatus\", \"misoStatus\", \"systemEntryDate\", \"upload\" \n" +
                "from \"ancServiceFPI\" \n" +
                "where \"providerId\" =" + provCode;
        VariableList = "healthId,pregNo,serviceId,providerId,visitSource,visitDate,visitMonth,ironFolStatus,misoStatus,systemEntryDate,upload";
        DownloadJSON(sql, "ancServiceFPI", VariableList, "healthId, pregNo, serviceId");


        // deliveryFPI
        sql = "select  \"healthId\", \"pregNo\", \"providerId\", \"outcomePlace\", \"outcomeDate\", \"outcomeType\", \"liveBirth\", \"stillBirth\", \"abortion\", \"misoprostol\", \"attendantDesignation\", \"systemEntryDate\", \"upload\" \n" +
                "from \"deliveryFPI\" \n" +
                "where \"providerId\" =" + provCode;
        VariableList = "healthId, pregNo, providerId, outcomePlace, outcomeDate,  outcomeType,liveBirth, stillBirth, abortion, misoprostol, attendantDesignation,systemEntryDate, upload";
        DownloadJSON(sql, "deliveryFPI", VariableList, "healthId, pregNo");


        // newBornFPI
        sql = "select  \"healthId\", \"pregNo\", \"childNo\", \"providerId\", \"birthWeight\", \"immatureBirth\", \"dryingAfterBirth\", \"resassitation\", \"stimulation\", \"bagNMask\", \"chlorehexidin\", \"skinTouch\", \"breastFeed\", \"bathThreeDays\" , \"systemEntryDate\" , \"modifyDate\" , \"upload\"  \n" +
                "from \"newBornFPI\" \n" +
                "where \"providerId\" =" + provCode;
        VariableList = "healthId, pregNo, childNo, providerId, birthWeight, immatureBirth,dryingAfterBirth, resassitation, stimulation, bagNMask, chlorehexidin,skinTouch, breastFeed,bathThreeDays, systemEntryDate, modifyDate, upload";
        DownloadJSON(sql, "newBornFPI", VariableList, "healthId, pregNo, childNo");

        // pncServiceChildFPI
        sql = "select  \"healthId\", \"pregNo\", \"childNo\", \"serviceId\", \"providerId\", \"visitSource\", \"visitDate\", \"visitMonth\", \"systemEntryDate\", \"modifyDate\", \"upload\" \n" +
                "from \"pncServiceChildFPI\" \n" +
                "where \"providerId\" =" + provCode;
        VariableList = "healthId, pregNo, childNo, serviceId,providerId,visitSource,visitDate,visitMonth, systemEntryDate, modifyDate, upload";
        DownloadJSON(sql, "pncServiceChildFPI", VariableList, "healthId, pregNo, childNo, serviceId");


        // pncServiceMother
        sql = "select  \"healthId\", \"pregNo\", \"serviceId\", \"providerId\", \"visitSource\", \"visitDate\", \"visitMonth\", \"systemEntryDate\", \"modifyDate\", \"upload\" \n" +
                "from \"pncServiceMother\" \n" +
                "where \"providerId\" =" + provCode;
        VariableList = "healthId, pregNo, serviceId, providerId,visitSource,visitDate,visitMonth,systemEntryDate, modifyDate, upload";
        DownloadJSON(sql, "pncServiceMother", VariableList, "healthId, pregNo, serviceId");

        // epiBariVisit
        sql = "select  \"dist\", \"upz\", \"un\", \"un\", \"vill\", \"provType\", \"provCode\", \"hHNo\", \"qBHHNo\", \"qBHEndDate\", \"qBPVisitEPI1\", \"qBPVisitEPI2\", \"qBPVisitEPI3\", \"qBPVisitEPI4\", \"qBPVisitEPI5\", \"qBNextDoss\", \"qB1stDoss1\", \"qB1stDoss2\", \"qB1stDoss3\", \"qB1stDoss4\", \"qB1stDoss5\", \"qBCNoSessiondossY\", \"qBCNoSessiondoss\", \"qBWNoSessiondossY\", \"qBWNoSessiondoss\" , \"qBVitmA\", \"qBChildCard\", \"qBWomenCard\", \"startTime\", \"endTime\", \"userId\", \"enDt\", \"upload\"\n" +
                "from \"epiBariVisit\" \n" +
                "where \"userId\" =" + provCode;
        VariableList = "dist,upz,un,mouza,vill,provType,provCode,hHNo,vDate,qBHHNo,qBHEndDate,qBPVisitEPI1,qBPVisitEPI2,qBPVisitEPI3,qBPVisitEPI4,qBPVisitEPI5,qBNextDoss,qB1stDoss1,qB1stDoss2,qB1stDoss3,qB1stDoss4,qB1stDoss5,qBCNoSessiondossY,qBCNoSessiondoss,qBWNoSessiondossY,qBWNoSessiondoss,qBVitmA,qBChildCard,qBWomenCard,startTime,endTime,userId,enDt,upload";
        DownloadJSON(sql, "epiBariVisit", VariableList, "dist,upz,un,mouza,vill,provCode,hHNo,vDate");

// epiSessionVisit
        sql = "select  \"subBlockId\", \"schedulerId\", \"providerId\", \"vDate\", \"qVHA\", \"qVFWA\", \"qVN\", \"qVOth\", \"qVChReg\", \"qVWReg\", \"qVChCard\", \"qVWCard\", \"qVTalBook\", \"qVFIBook\", \"qVVac\", \"qVASerice\", \"qVMSerice\", \"qVSBox\", \"qVVatARed\", \"qVVatABlue\", \"qVIICPac\", \"qVBCG\", \"qVPenta\", \"qVPolio\", \"qVPcv\" , \"qVIPV\", \"qVMR\", \"qVTT\", \"qVSbcg\", \"qVSPenta\", \"qVSPolio\", \"qVSPcv\", \"qVSIPV\", \"qVSMR\", \"qVSTT\", \"qVNToubcg\", \"qVNToupant\", \"qVNTouPolio\", \"qVNToupcv\", \"qVNTouIPV\", \"qVNTouMR\", \"qVNTouTT\", \"qVRootbcg\", \"qVNotwhy\", \"qVRootPenta\", \"qVRootMR\", \"qVRootTT\", \"qVSRemoved\", \"qVFormbcg\", \"qVFormpenta\", \"qVFormPolio\", \"qVFormpcv\", \"qVFormipv\", \"qVFormmr\", \"qVFormtt\", \"qVCardregBook\", \"qVCardVac\", \"qVTTresearched\", \"qVProtectors\", \"qVDateOfVac\", \"qVCard\", \"qVAFP\", \"qVMeasles\", \"qVNewborntetanus\", \"qVOther\", \"qVTodySession\", \"qVProblem1\", \"qVProblem2\", \"qVProblem3\", \"qVProblem4\", \"qVProblem5\", \"qVSolve1\", \"qVSolve2\", \"qVSolve3\", \"qVSolve3\", \"qVSolve4\", \"qVSolve5\", \"startTime\", \"endTime\", \"userId\", \"enDt\", \"upload\" \n" +
                "from \"epiSessionVisit\" \n" +
                "where \"userId\" =" + provCode;
        VariableList = "subBlockId,schedulerId,providerId,vDate,qVHA,qVFWA,qVN,qVOth,qVChReg,qVWReg,qVChCard,qVWCard,qVTalBook,qVFIBook,qVVac,qVASerice,qVMSerice,qVSBox,qVVatARed,qVVatABlue,qVIICPac,qVBCG,qVPenta,qVPolio,qVPcv,qVIPV,qVMR,qVTT,qVSbcg,qVSPenta,qVSPolio,qVSPcv,qVSIPV,qVSMR,qVSTT,qVNToubcg,qVNToupant,qVNTouPolio,qVNToupcv,qVNTouIPV,qVNTouMR,qVNTouTT,qVRootbcg,qVNotwhy,qVRootPenta,qVRootMR,qVRootTT,qVSRemoved,qVFormbcg,qVFormpenta,qVFormPolio,qVFormpcv,qVFormipv,qVFormmr,qVFormtt,qVCardregBook,qVCardVac,qVTTresearched,qVProtectors,qVDateOfVac,qVCard,qVAFP,qVMeasles,qVNewborntetanus,qVOther,qVTodySession,qVProblem1,qVProblem2,qVProblem3,qVProblem4,qVProblem5,qVSolve1,qVSolve2,qVSolve3,qVSolve4,qVSolve5,startTime,endTime,userId,enDt,upload";
        DownloadJSON(sql, "epiSessionVisit", VariableList, "subBlockId,schedulerId,providerId,vDate");


        //ClientMap

      sql = "select  B.\"generatedId\", B.\"name\",  B.\"age\", B.\"divisionId\", B.\"zillaId\", B.\"upazilaId\",  B.\"unionId\", B.\"mouzaId\",B.\"villageId\", B.\"houseGRHoldingNo\", B.\"mobileNo\",  B.\"systemEntryDate\", B.\"modifyDate\", B.\"providerId\",  B.\"healthId\",  B.\"gender\", B.\"fatherName\", B.\"motherName\", B.\"husbandName\", B.\"dob\", B.\"ownMobile\", B.\"epiBlock\",   '1' Upload \n" +
                "from \"ProviderArea\" pa \n" +
                "INNER JOIN \"Member\" A ON A.\"Dist\" = pa.zillaid and A.\"Upz\" = pa.upazilaid and A.\"UN\" = pa.unionid\n" +
                "and A.\"Mouza\" = pa.mouzaid and A.\"Vill\" = pa.villageid \n" +
                "INNER JOIN \"clientMap\" B ON A.\"HealthID\" = B.\"healthId\"\n" +
                "where pa.\"provCode\" =" + provCode ;



        DownloadJSON(sql, "clientMap", "generatedId, name,  age, divisionId, zillaId, upazilaId,  unionId, mouzaId,villageId, houseGRHoldingNo, mobileNo,  systemEntryDate, modifyDate, providerId,  healthId,  gender, fatherName, motherName, husbandName, dob, ownMobile, epiBlock,  upload", "generatedId");


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
            /*    "where a.\"mouzaid\" ='" + mouzaid  + "'\n" +
                "\tand a.\"villageid\" = '" + villageid + "'\n" +
                "\tand a.\"zillaid\" = '" + zillaid + "'\n" +
                "\tand a.\"upazilaid\" = '" + upazilaid + "'\n" +
                "\tand a.\"unionid\" = '" + unionid+ "'"+*/
                "\twhere h.\"HealthID\" in(Select \"HealthID\" from \"clientMap\" where \"HealthID\" = B.\"healthId\")";

        DownloadJSON(sql, "Member", "Dist, Upz, UN, Mouza, Vill, ProvType, ProvCode, HHNo, SNo, HealthID, NameEng, NameBang, Rth, HaveNID, NID, NIDStatus, HaveBR, BRID, BRIDStatus, MobileNo1, MobileNo2,MobileYN, DOB, Age, DOBSource, BPlace, FNo, Father, FDontKnow, MNo, Mother, MDontKnow, Sex, MS, SPNO1, SPNO2, SPNO3, SPNO4, ELCONo, ELCODontKnow, EDU, Rel, Nationality, OCP, StartTime, EnType, EnDate, ExType, ExDate, EndTime, Lat, Lon, UserId, EnDt, hidDistributed, hidDistributionDate, Upload", "Dist, Upz, UN, Mouza, Vill, HHNo, SNo");




    }
    public void DownloadServiceTables(String provCode, ProgressDialog progress, String message, Handler progressHandler,int jumpTime, boolean DowloadOnlyAppropriateRecords){

        jumpTime += 1;
        message = "Downloading Client Map";
        progressHandler.sendMessage(progressHandler.obtainMessage());

        //fpaWorkPlanMaster
        String VariableList = "";
        String  sql = "select  \"workPlanId\", \"workAreaId\", \"providerId\", \"month\", \"status\", \"systemEntryDate\", \"modifyDate\", \"upload\" \n" +
                "from \"workPlanMaster\" \n" +
                "where \"providerId\" =" + provCode;

        VariableList = "workPlanId,workAreaId,providerId,month,status,systemEntryDate,modifyDate,upload";
        DownloadJSON(sql, "workPlanMaster", VariableList, "workPlanId, providerId, month");

        //fpaWorkPlanDetail

        sql = "select  \"workPlanId\", \"item\", \"workPlanDate\", \"unitNo\", \"village\", \"elcoFrom\", \"elcoTo\", \"leaveType\", \"providerId\", \"systemEntryDate\", \"modifyDate\", \"otherDec\", \"remarks\", \"upload\" \n" +
                "from \"workPlanDetail\" \n" +
                "where \"providerId\" =" + provCode;

        VariableList = "workPlanId,fpaItem,workPlanDate,unitNo,village,elcoFrom,elcoTo,leaveType,providerId,systemEntryDate,modifyDate,otherDec,remarks,upload";
        DownloadJSON(sql, "workPlanDetail", VariableList, "workPlanId, fpaItem, workPlanDate, providerId");

        // fpiMonitoring
        sql = "select  \"vDate\", \"fpaCode\", \"fpaUnit\", \"fpaVill\", \"fpaAdvance\", \"needItems1\", \"needItems2\", \"needItems3\", \"needItems4\", \"needItems5\", \"needItems6\", \"needItems7\", \"needItems8\", \"startTime\", \"endTime\", \"userId\" , \"enDt\" , \"upload\"  \n" +
                "from \"fpiMonitoring\" \n" +
                "where cast(\"userId\" as Integer) =" + provCode;
        VariableList = "vDate,fpaCode,fpaUnit,fpaVill,fpaAdvance,needItems1,needItems2,needItems3,needItems4,needItems5,needItems6,needItems7,needItems8,startTime,endTime,userId,enDt,upload";
        DownloadJSON(sql, "fpiMonitoring", VariableList, "vDate, fpaCode");


        // HouseholdFPI
        sql = "select  \"Dist\", \"Upz\", \"UN\", \"Mouza\", \"Vill\", \"ProvCode\", \"HHNo\", \"houseHoldStatus\", \"causeOfHouseHoldStatus\", \"subBlockStatus\", \"pAddrStatus\", \"permaAddressStatus\", \"religionStatus\", \"StartTime\", \"EndTime\", \"Lat\" , \"Lon\" , \"UserId\", \"EnDt\", \"Upload\"  \n" +
                "from \"HouseholdFPI\" \n" +
                "where cast(\"UserId\" as Integer) =" + provCode;
        VariableList = "Dist,Upz,UN,Mouza,Vill,ProvType,ProvCode,HHNo,houseHoldStatus,causeOfHouseHoldStatus,subBlockStatus,pAddrStatus,permaAddressStatus,religionStatus,StartTime,EndTime,Lat,Lon,UserId,EnDt,Upload";
        DownloadJSON(sql, "HouseholdFPI", VariableList, "Dist,  Upz, UN,Mouza,Vill,HHNo");

        // memberfpi
        sql = "select  \"dist\", \"upz\", \"un\", \"mouza\", \"vill\", \"provtype\", \"provcode\", \"hhno\", \"sno\", \"healthid\", \"nameengstatus\", \"rthstatus\", \"havenidstatus\", \"nidstatus\", \"havebrstatus\", \"bridstatus\" , \"mobileno1status\" , \"mobileno2status\", \"dobstatus\", \"agestatus\", \"dobsourcestatus\", \"bplacestatus\", \"fnostatus\", \"fatherstatus\", \"mnostatus\", \"motherstatus\", \"sexstatus\", \"msstatus\", \"spno1status\", \"spno2status\", \"spno3status\", \"spno4status\", \"edustatus\", \"relstatus\", \"nationalitystatus\", \"ocpstatus\", \"starttime\", \"entype\", \"endate\", \"extype\" , \"exdate\" , \"endtime\" , \"lat\" , \"lon\" , \"userid\", \"endt\", \"upload\"  \n" +
                "from \"memberfpi\" \n" +
                "where cast(\"userid\" as Integer) =" + provCode;
        VariableList = "dist,upz,un,mouza,vill,provtype,provcode,hhno,sno,healthid,nameengstatus,rthstatus,havenidstatus,nidstatus,havebrstatus,bridstatus,mobileno1status,mobileno2status,dobstatus,agestatus,dobsourcestatus,bplacestatus,fnostatus,fatherstatus,mnostatus,motherstatus,sexstatus,msstatus,spno1status,spno2status,spno3status,spno4status,edustatus,relstatus,nationalitystatus,ocpstatus,starttime,entype,endate,extype,exdate,endtime,lat,lon,userid,endt,upload";
        DownloadJSON(sql, "memberfpi", VariableList, "dist,upz,un,mouza,vill, hhno, sno");



        // sesfpi
        sql = "select  \"dist\", \"upz\", \"un\", \"mouza\", \"vill\", \"provtype\", \"provcode\", \"hhno\", \"sesstatus\", \"q1status\", \"q11status\", \"q2status\", \"q21status\", \"q3astatus\", \"q3bstatus\", \"q3cstatus\" , \"q3dstatus\" , \"q3estatus\", \"q3fstatus\", \"q3gstatus\", \"q3hstatus\", \"q3istatus\", \"q3jstatus\", \"q3kstatus\", \"q3lstatus\", \"q3mstatus\", \"q3nstatus\", \"q3ostatus\", \"q3pstatus\", \"q4status\", \"q41status\", \"q5status\", \"q51status\", \"q6status\", \"q61status\", \"q7status\", \"q71status\", \"q8astatus\", \"q8bstatus\", \"q8cstatus\", \"q8dstatus\", \"q8estatus\", \"q9status\", \"q10status\", \"q11astatus\", \"starttime\", \"endtime\", \"userid\", \"endt\", \"upload\"  \n" +
                "from \"sesfpi\" \n" +
                "where cast(\"userid\" as Integer) =" + provCode;
        VariableList = "dist,upz,un,mouza,vill,provtype,provcode,hhno,sesstatus,q1status,q11status,q2status,q21status,q3astatus,q3bstatus,q3cstatus,q3dstatus,q3estatus,q3fstatus,q3gstatus,q3hstatus,q3istatus,q3jstatus,q3kstatus,q3lstatus,q3mstatus,q3nstatus,q3ostatus,q3pstatus,q4status,q41status,q5status,q51status,q6status,q61status,q7status,q71status,q8astatus,q8bstatus,q8cstatus,q8dstatus,q8estatus,q9status,q10status,q11astatus,starttime,endtime,userid,endt,upload";
        DownloadJSON(sql, "sesfpi", VariableList, "dist,upz,un,mouza,vill, hhno");

// elcoFPI
        sql = "select  \"healthId\", \"providerId\", \"hhStatus\", \"haHHNo\", \"elcoNo\", \"husbandName\", \"husbandAge\", \"domSource\", \"marrDate\", \"marrAge\", \"son\", \"dau\", \"regDT\", \"systemEntryDate\", \"modifyDate\", \"tt1\" , \"tt2\" , \"tt3\", \"tt4\", \"tt5\", \"upload\"   \n" +
                "from \"elcoFPI\" \n" +
                "where \"providerId\" =" + provCode;
        VariableList = "healthId, providerId, hhStatus, haHHNo, elcoNo, husbandName,husbandAge,domSource, marrDate, marrAge, son, dau, regDT, systemEntryDate,modifyDate,tt1,tt2,tt3,tt4,tt5, upload";
        DownloadJSON(sql, "elcoFPI", VariableList, "healthId");


        // elcoVisitFPI
        sql = "select  \"healthId\", \"pregNo\", \"providerId\", \"transactionId\", \"visit\", \"vDate\", \"visitStatus\", \"currStatus\", \"newOld\", \"mDate\", \"sSource\", \"qty\", \"unit\", \"brand\", \"validity\", \"dayMonYear\" , \"referPlace\" , \"syrinsQty\", \"mrSource\", \"MRDate\", \"MRAge\", \"systemEntryDate\" , \"modifyDate\" , \"upload\"    \n" +
                "from \"elcoVisitFPI\" \n" +
                "where \"providerId\" =" + provCode;
        VariableList = "healthId,pregNo,providerId,transactionId,visit,vDate,visitStatus,currStatus,newOld,mDate,sSource,qty,unit,brand,validity,dayMonYear,referPlace,syrinsQty,mrSource,MRDate,MRAge,systemEntryDate,modifyDate,upload";
        DownloadJSON(sql, "elcoVisitFPI", VariableList, "healthId,visit");


        // pregWomenFPI
        sql = "select  \"healthId\", \"pregNo\", \"providerId\", \"LMP\", \"EDD\", \"para\", \"gravida\", \"lastChildAge\", \"riskHistoryNote\", \"pregRefer\", \"systemEntryDate\", \"upload\" \n" +
                "from \"pregWomenFPI\" \n" +
                "where \"providerId\" =" + provCode;
        VariableList = "healthId, pregNo, providerId,LMP, EDD, para, gravida, lastChildAge, riskHistoryNote,pregRefer, systemEntryDate, upload";
        DownloadJSON(sql, "pregWomenFPI", VariableList, "healthId, pregNo");


        // ancServiceFPI
        sql = "select  \"healthId\", \"pregNo\", \"serviceId\", \"providerId\", \"visitSource\", \"visitDate\", \"visitMonth\", \"ironFolStatus\", \"misoStatus\", \"systemEntryDate\", \"upload\" \n" +
                "from \"ancServiceFPI\" \n" +
                "where \"providerId\" =" + provCode;
        VariableList = "healthId,pregNo,serviceId,providerId,visitSource,visitDate,visitMonth,ironFolStatus,misoStatus,systemEntryDate,upload";
        DownloadJSON(sql, "ancServiceFPI", VariableList, "healthId, pregNo, serviceId");


        // deliveryFPI
        sql = "select  \"healthId\", \"pregNo\", \"providerId\", \"outcomePlace\", \"outcomeDate\", \"outcomeType\", \"liveBirth\", \"stillBirth\", \"abortion\", \"misoprostol\", \"attendantDesignation\", \"systemEntryDate\", \"upload\" \n" +
                "from \"deliveryFPI\" \n" +
                "where \"providerId\" =" + provCode;
        VariableList = "healthId, pregNo, providerId, outcomePlace, outcomeDate,  outcomeType,liveBirth, stillBirth, abortion, misoprostol, attendantDesignation,systemEntryDate, upload";
        DownloadJSON(sql, "deliveryFPI", VariableList, "healthId, pregNo");


        // newBornFPI
        sql = "select  \"healthId\", \"pregNo\", \"childNo\", \"providerId\", \"birthWeight\", \"immatureBirth\", \"dryingAfterBirth\", \"resassitation\", \"stimulation\", \"bagNMask\", \"chlorehexidin\", \"skinTouch\", \"breastFeed\", \"bathThreeDays\" , \"systemEntryDate\" , \"modifyDate\" , \"upload\"  \n" +
                "from \"newBornFPI\" \n" +
                "where \"providerId\" =" + provCode;
        VariableList = "healthId, pregNo, childNo, providerId, birthWeight, immatureBirth,dryingAfterBirth, resassitation, stimulation, bagNMask, chlorehexidin,skinTouch, breastFeed,bathThreeDays, systemEntryDate, modifyDate, upload";
        DownloadJSON(sql, "newBornFPI", VariableList, "healthId, pregNo, childNo");

        // pncServiceChildFPI
        sql = "select  \"healthId\", \"pregNo\", \"childNo\", \"serviceId\", \"providerId\", \"visitSource\", \"visitDate\", \"visitMonth\", \"systemEntryDate\", \"modifyDate\", \"upload\" \n" +
                "from \"pncServiceChildFPI\" \n" +
                "where \"providerId\" =" + provCode;
        VariableList = "healthId, pregNo, childNo, serviceId,providerId,visitSource,visitDate,visitMonth, systemEntryDate, modifyDate, upload";
        DownloadJSON(sql, "pncServiceChildFPI", VariableList, "healthId, pregNo, childNo, serviceId");


        // pncServiceMother
        sql = "select  \"healthId\", \"pregNo\", \"serviceId\", \"providerId\", \"visitSource\", \"visitDate\", \"visitMonth\", \"systemEntryDate\", \"modifyDate\", \"upload\" \n" +
                "from \"pncServiceMother\" \n" +
                "where \"providerId\" =" + provCode;
        VariableList = "healthId, pregNo, serviceId, providerId,visitSource,visitDate,visitMonth,systemEntryDate, modifyDate, upload";
        DownloadJSON(sql, "pncServiceMother", VariableList, "healthId, pregNo, serviceId");

        // epiBariVisit
        sql = "select  \"dist\", \"upz\", \"un\", \"un\", \"vill\", \"provType\", \"provCode\", \"hHNo\", \"qBHHNo\", \"qBHEndDate\", \"qBPVisitEPI1\", \"qBPVisitEPI2\", \"qBPVisitEPI3\", \"qBPVisitEPI4\", \"qBPVisitEPI5\", \"qBNextDoss\", \"qB1stDoss1\", \"qB1stDoss2\", \"qB1stDoss3\", \"qB1stDoss4\", \"qB1stDoss5\", \"qBCNoSessiondossY\", \"qBCNoSessiondoss\", \"qBWNoSessiondossY\", \"qBWNoSessiondoss\" , \"qBVitmA\", \"qBChildCard\", \"qBWomenCard\", \"startTime\", \"endTime\", \"userId\", \"enDt\", \"upload\"\n" +
                "from \"epiBariVisit\" \n" +
                "where \"userId\" =" + provCode;
        VariableList = "dist,upz,un,mouza,vill,provType,provCode,hHNo,vDate,qBHHNo,qBHEndDate,qBPVisitEPI1,qBPVisitEPI2,qBPVisitEPI3,qBPVisitEPI4,qBPVisitEPI5,qBNextDoss,qB1stDoss1,qB1stDoss2,qB1stDoss3,qB1stDoss4,qB1stDoss5,qBCNoSessiondossY,qBCNoSessiondoss,qBWNoSessiondossY,qBWNoSessiondoss,qBVitmA,qBChildCard,qBWomenCard,startTime,endTime,userId,enDt,upload";
        DownloadJSON(sql, "epiBariVisit", VariableList, "dist,upz,un,mouza,vill,provCode,hHNo,vDate");

// epiSessionVisit
        sql = "select  \"subBlockId\", \"schedulerId\", \"providerId\", \"vDate\", \"qVHA\", \"qVFWA\", \"qVN\", \"qVOth\", \"qVChReg\", \"qVWReg\", \"qVChCard\", \"qVWCard\", \"qVTalBook\", \"qVFIBook\", \"qVVac\", \"qVASerice\", \"qVMSerice\", \"qVSBox\", \"qVVatARed\", \"qVVatABlue\", \"qVIICPac\", \"qVBCG\", \"qVPenta\", \"qVPolio\", \"qVPcv\" , \"qVIPV\", \"qVMR\", \"qVTT\", \"qVSbcg\", \"qVSPenta\", \"qVSPolio\", \"qVSPcv\", \"qVSIPV\", \"qVSMR\", \"qVSTT\", \"qVNToubcg\", \"qVNToupant\", \"qVNTouPolio\", \"qVNToupcv\", \"qVNTouIPV\", \"qVNTouMR\", \"qVNTouTT\", \"qVRootbcg\", \"qVNotwhy\", \"qVRootPenta\", \"qVRootMR\", \"qVRootTT\", \"qVSRemoved\", \"qVFormbcg\", \"qVFormpenta\", \"qVFormPolio\", \"qVFormpcv\", \"qVFormipv\", \"qVFormmr\", \"qVFormtt\", \"qVCardregBook\", \"qVCardVac\", \"qVTTresearched\", \"qVProtectors\", \"qVDateOfVac\", \"qVCard\", \"qVAFP\", \"qVMeasles\", \"qVNewborntetanus\", \"qVOther\", \"qVTodySession\", \"qVProblem1\", \"qVProblem2\", \"qVProblem3\", \"qVProblem4\", \"qVProblem5\", \"qVSolve1\", \"qVSolve2\", \"qVSolve3\", \"qVSolve3\", \"qVSolve4\", \"qVSolve5\", \"startTime\", \"endTime\", \"userId\", \"enDt\", \"upload\" \n" +
                "from \"epiSessionVisit\" \n" +
                "where \"userId\" =" + provCode;
        VariableList = "subBlockId,schedulerId,providerId,vDate,qVHA,qVFWA,qVN,qVOth,qVChReg,qVWReg,qVChCard,qVWCard,qVTalBook,qVFIBook,qVVac,qVASerice,qVMSerice,qVSBox,qVVatARed,qVVatABlue,qVIICPac,qVBCG,qVPenta,qVPolio,qVPcv,qVIPV,qVMR,qVTT,qVSbcg,qVSPenta,qVSPolio,qVSPcv,qVSIPV,qVSMR,qVSTT,qVNToubcg,qVNToupant,qVNTouPolio,qVNToupcv,qVNTouIPV,qVNTouMR,qVNTouTT,qVRootbcg,qVNotwhy,qVRootPenta,qVRootMR,qVRootTT,qVSRemoved,qVFormbcg,qVFormpenta,qVFormPolio,qVFormpcv,qVFormipv,qVFormmr,qVFormtt,qVCardregBook,qVCardVac,qVTTresearched,qVProtectors,qVDateOfVac,qVCard,qVAFP,qVMeasles,qVNewborntetanus,qVOther,qVTodySession,qVProblem1,qVProblem2,qVProblem3,qVProblem4,qVProblem5,qVSolve1,qVSolve2,qVSolve3,qVSolve4,qVSolve5,startTime,endTime,userId,enDt,upload";
        DownloadJSON(sql, "epiSessionVisit", VariableList, "subBlockId,schedulerId,providerId,vDate");



    }

   //Download Data from Server
    public String DownloadJSON_HealthID(String SQL, String TableName, String ColumnList, String UniqueField) {
        DownloadRequestClass dr = new DownloadRequestClass();
        dr.setmethodname("DownloadData");
        dr.setSQL(SQL);
        Gson gson = new Gson();
        String json = gson.toJson(dr);

        String WhereClause = "";
        int varPos = 0;

        String response = "";
        String resp = "";
        String IDNO = "";
        try {
            //For Web Api
            //--------------------------------------------------------------------------------------
            DownloadJSONData dload = new DownloadJSONData();
            response = dload.execute(json).get();
            downloadClass responseData = (downloadClass) gson.fromJson(response, downloadClass.class);
            //--------------------------------------------------------------------------------------


            String UField[] = UniqueField.toString().replace(" ", "").split(",");
            String VarList[] = ColumnList.toString().replace(" ", "").split(",");
            String InsertSQL = "";

            List<String> dataStatus = new ArrayList<String>();
            List<DataClassProperty> data = new ArrayList<DataClassProperty>();
            DataClassProperty d;
            String DataList = "";

            String modify_Date = "";
            if (responseData != null) {
                for (int i = 0; i < responseData.getdata().size(); i++) {
                    String VarData[] = split(responseData.getdata().get(i).toString(), '^');

                    //Generate where clause
                    SQL = "";
                    WhereClause = "";
                    varPos = 0;
                    for (int j = 0; j < UField.length; j++) {
                        varPos = VarPosition(UField[j].toString(), VarList);
                        if (j == 0) {
                            WhereClause = UField[j].toString() + "=" + "'" + VarData[varPos].toString() + "'";
                            IDNO += VarData[varPos].toString();
                        } else {
                            WhereClause += " and " + UField[j].toString() + "=" + "'" + VarData[varPos].toString() + "'";
                            IDNO += VarData[varPos].toString();
                        }
                    }

                    //Update command
                    int hid_var_pos = VarPosition("HealthId", VarList);
                    int modifydt_var_pos = VarPosition("modifyDate", VarList);
                    modify_Date = VarData[modifydt_var_pos].toString().replace("null", "");

                    Save("Update Member set HealthId='"+ VarData[hid_var_pos].toString() +"' Where " + WhereClause);
                    //Save("Update ClientMap set HealthId='"+ VarData[varPos].toString() +"' Where " + WhereClause);
                /*
                if (Existence("Select " + VarList[0] + " from " + TableName + " Where " + WhereClause)) {
                    for (int r = 0; r < VarList.length; r++) {
                        if (r == 0) {
                            SQL = "Update " + TableName + " Set ";
                            SQL += VarList[r] + " = '" + VarData[r].toString() + "'";
                        } else {
                            if (r == VarData.length - 1) {
                                SQL += "," + VarList[r] + " = '" + VarData[r].toString() + "'";
                                SQL += " Where " + WhereClause;
                            } else {
                                SQL += "," + VarList[r] + " = '" + VarData[r].toString() + "'";
                            }
                        }
                        if ("modifyDate".equalsIgnoreCase(VarList[r])) {
                            modify_Date = VarData[r].toString().replace("null", "");
                        }
                    }

                    Save(SQL);
                }*/

                    //Insert command
                /*else {
                    for (int r = 0; r < VarList.length; r++) {
                        if (r == 0) {
                            SQL = "Insert into " + TableName + "(" + ColumnList + ")Values(";
                            SQL += "'" + VarData[r].toString() + "'";
                        } else {
                            SQL += ",'" + VarData[r].toString() + "'";
                        }
                        if (VarList[r].toString().equalsIgnoreCase("modifyDate")) {
                            modify_Date = VarData[r].toString().replace("null", "");
                        }
                    }
                    SQL += ")";

                    Save(SQL);
                }*/
                    DataList = TableName + "^" + IDNO + "^" + modify_Date + "^" + Global.getInstance().getProvType() + "^" + Global.getInstance().getProvCode();
                    d = new DataClassProperty();
                    d.setdatalist(DataList);
                    d.setuniquefieldwithdata("\"recordId\"='" + IDNO + "' and \"modifyDate\"='" + modify_Date + "' and \"provCode\"='" + Global.getInstance().getProvCode() + "'");
                    data.add(d);

                    IDNO = "";
                }

                //Upload status to data_sync_management
                DataClass dt = new DataClass();
                dt.setmethodname("UploadData_For_Sync");
                dt.settablename("data_sync_management");
                dt.setcolumnlist("tableName,recordId,modifyDate,provType,provCode");
                dt.setdata(data);

                Gson gson1   = new Gson();
                String json1 = gson1.toJson(dt);
                String response1 = "";

                UploadJSONData u = new UploadJSONData();

                try{
                    response1=u.execute(json1).get();
                } catch (Exception e) {
                    //  Toast.makeText(dbContext, "All data did not upload. Try again.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            resp = e.getMessage();
            e.printStackTrace();
        }

        return resp;
    }

        //To get the list of columns(string) in table
    //----------------------------------------------------------------------------------------------
    public String GetColumnList(String TableName)
    {
        String CList = "";
        Cursor cur_H;
        cur_H = ReadData("pragma table_info('"+ TableName +"')");

        cur_H.moveToFirst();
        int RecordCount=0;

        while(!cur_H.isAfterLast())
        {
            if(RecordCount==0)
                CList +=  cur_H.getString(cur_H.getColumnIndex("name"));
            else
                CList +=  ","+ cur_H.getString(cur_H.getColumnIndex("name"));

            RecordCount += 1;
            cur_H.moveToNext();
        }
        cur_H.close();

        return CList;
    }

    //To get the list of columns(string array) in table
    //----------------------------------------------------------------------------------------------
    public String[] GetColumnListArray(String TableName)
    {
        Cursor cur = ReadData("SELECT * FROM "+ TableName +" WHERE 0");
        String[] columnNames;
        try {
            columnNames = cur.getColumnNames();
        } finally {
            cur.close();
        }
        return columnNames;
    }

    //Download Table List from server
/*    private String[] TableListServer()
    {
        String SQLStr= "";
        DownloadData d = new DownloadData();
        d.Method_Name = "DownloadData";
        d.SQLStr = "Select TableName from DatabaseTab";

        String DataArray[] = null;

        try {
            DataArray = d.execute("").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return DataArray;
    }*/

/*    public void TableStructureSync()
    {
        String TableList[] = TableListServer();
        String SQL = "";
        String TableName = "";
        for (int t = 0; t < TableList.length; t++) {
            TableName = TableList[t];
            SQL = "select (c.name+'^'+cast(c.length as varchar(10)))ColwithLength from sysobjects t,syscolumns c";
            SQL += " where t.id=c.id and t.name='"+ TableName +"' and lower(t.xtype)='u' order by colid";

            //Local database
            String[] local = GetColumnListArray(TableName);

            //Server database
            String[] server = DownloadArrayList(SQL);

            String[] C;
            Boolean matched = false;

            //matched database columns(local and server)
            for (int i=0;i<server.length;i++)
            {
                matched = false;
                C = Connection.split(server[i], '^');
                for(int j=0;j<local.length;j++)
                {
                    if(C[0].toString().toLowerCase().equals(local[j].toString().toLowerCase()))
                    {
                        matched = true;
                        j = local.length;
                    }
                }
                if(matched==false)
                {
                    Save("Alter table "+ TableName +" add column "+ C[0].toString() +" varchar("+ C[1].toString() +") default ''");
                }
            }
        }


*//*
        String SQL = "";
        SQL = "select (c.name+'^'+cast(c.length as varchar(10)))ColwithLength from sysobjects t,syscolumns c";
        SQL += " where t.id=c.id and t.name='"+ TableName +"' and lower(t.xtype)='u' order by colid";

        //Local database
        String[] local = GetColumnListArray(TableName);

        //Server database
        String[] server = DownloadArrayList(SQL);

        String[] C;
        Boolean matched = false;

        //matched database columns(local and server)
        for (int i=0;i<server.length;i++)
        {
            matched = false;
            C = Connection.split(server[i], '^');
            for(int j=0;j<local.length;j++)
            {
                if(C[0].toString().toLowerCase().equals(local[j].toString().toLowerCase()))
                {
                    matched = true;
                    j = local.length;
                }
            }
            if(matched==false)
            {
                Save("Alter table "+ TableName +" add column "+ C[0].toString() +" varchar("+ C[1].toString() +") default ''");
            }
        }
*//*
    }*/

/*    private String[] DownloadArrayList(String SQL)
    {
        DownloadData d = new DownloadData();
        d.Method_Name="DownloadData";
        d.SQLStr=SQL;

        String DataArray[] = new String[0];
        try {
            DataArray = d.execute("").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return DataArray;
    }*/


    //Download Health ID from the Central Database
    //----------------------------------------------------------------------------------------------
    public void DownloadHealthID(String Dist, String Upz, String UN, String ProvType, String ProvCode, int totalId) {
        String SQLStr= "";
        String Res = "";

        //ID Sync
        try {
            /*SQLStr = "Select sp_GenerateHealthID ('"+ Dist +"','"+ Upz +"','"+ UN +"','"+ ProvType +"','"+ ProvCode +"',"+ totalId +")";
            ExecuteCommandOnServer(SQLStr);*/
            SQLStr = "Select sp_GenerateHealthID ('"+ Dist +"','"+ Upz +"','"+ UN +"','"+ ProvType +"','"+ ProvCode +"',"+ totalId +")";
            List<String> str = new ArrayList<String>();
            str.add(SQLStr);
            ExecuteCommandOnServerJSON(str);

            //Service Provider
            SQLStr = "Select * from sp_HealthIDDownload ('"+ Dist +"','"+ Upz +"','"+ UN +"','"+ ProvType +"','"+ ProvCode +"')";
            Res = DownloadJSON(SQLStr ,"HealthIDRepository","ZillaID, UpazilaID, UnionID, ProvType, ProvCode, AreaCode, HealthID,Status","HealthID");

            //SQLStr = "Update ProviderDB set HealthIDRequest='2' where  Zillaid='"+ Dist +"' and UpazilaID='"+ Upz +"' and UnionID='"+ UN +"' and ProvType='"+ ProvType +"' and ProvCode='"+ ProvCode +"'";
            SQLStr="Update \"ProviderDB\" set \"HealthIDRequest\"='2' where  zillaid='"+ Dist +"' and upazilaid='"+ Upz +"' and unionid='"+ UN +"' and \"ProvType\"='"+ ProvType +"' and \"ProvCode\"='"+ ProvCode +"'";
            str = new ArrayList<String>();
            str.add(SQLStr);
            ExecuteCommandOnServerJSON(str);
            //Update "ProviderDB" set "HealthIDRequest"='2' where  zillaid='93' and upazilaid='9' and unionid='11' and "ProvType"='3' and "ProvCode"='93004'
            //ExecuteCommandOnServer(SQLStr);

        } catch (Exception ex) {

        }
    }

    private int TotalRecordCount(String TableName )
    {
        return Integer.parseInt(this.ReturnSingleValue("Select COUNT(*) AS TotalRecords from " + TableName + " WHERE Upload='2'"));
    }
    public List<DataClassProperty> GetDataListJSON(String VariableList,String TableName,String UniqueField, int limit_of_records, int offset_settings)
    {


        Cursor cur_H = ReadData("Select " + VariableList + " from " + TableName + " WHERE Upload='2' LIMIT "+ limit_of_records +" OFFSET "+ offset_settings);
        cur_H.moveToFirst();
        List<DataClassProperty> data = new ArrayList<DataClassProperty>();
        DataClassProperty d;

        String DataList = "";
        //String[] Count  = VariableList.toString().split(",");
        String[] Count  = VariableList.toString().replace(" ","").split(",");
        //String[] UField = UniqueField.toString().split(",");
        String[] UField = UniqueField.toString().replace(" ","").split(",");
        int RecordCount=0;

        String WhereClause="";
        String VarData[];
        int varPos=0;
        while(!cur_H.isAfterLast())
        {
            //Prepare Data List
            for(int c=0; c < Count.length; c++)
            {
                if(c==0)
                {
                    if (cur_H.getString(c) == null)
                        DataList = "";
                    else if(cur_H.getString(c).equals("null"))
                        DataList = "";
                    else
                        DataList = cur_H.getString(c).toString();

                }
                else
                {
                    if (cur_H.getString(c) == null)
                        DataList+="^"+"";
                    else if(cur_H.getString(c).equals("null"))
                        DataList+="^"+"";
                    else
                        DataList+="^"+cur_H.getString(c).toString();
                }
            }

            //Prepare Where Clause
            VarData = DataList.split("\\^");
            varPos=0;


            for(int j=0; j< UField.length; j++)
            {
                varPos=VarPosition(UField[j].toString(),Count);
                if(j==0)
                {
                    WhereClause = "\""+ UField[j].toString()+"\"="+ "'"+ VarData[varPos].toString() +"'";
                }
                else
                {
                    WhereClause += " and \"" +UField[j].toString()+"\"="+ "'"+ VarData[varPos].toString() +"'";
                }
            }

            d = new DataClassProperty();
            d.setdatalist(DataList);
            d.setuniquefieldwithdata(WhereClause);
            data.add(d);

            RecordCount+=1;
            cur_H.moveToNext();
        }
        cur_H.close();

        return data;
    }
    //New add code by nisan

    public List<DataClassProperty> GetDataListJSONAproved(String VariableList,String TableName,String UniqueField, int limit_of_records, int offset_settings)
    {


        Cursor cur_H = ReadData("Select " + VariableList + " from " + TableName + " WHERE Upload='2' And status='2' OR status='3' LIMIT "+ limit_of_records +" OFFSET "+ offset_settings);
        cur_H.moveToFirst();
        List<DataClassProperty> data = new ArrayList<DataClassProperty>();
        DataClassProperty d;

        String DataList = "";
        //String[] Count  = VariableList.toString().split(",");
        String[] Count  = VariableList.toString().replace(" ","").split(",");
        //String[] UField = UniqueField.toString().split(",");
        String[] UField = UniqueField.toString().replace(" ","").split(",");
        int RecordCount=0;

        String WhereClause="";
        String VarData[];
        int varPos=0;
        while(!cur_H.isAfterLast())
        {
            //Prepare Data List
            for(int c=0; c < Count.length; c++)
            {
                if(c==0)
                {
                    if (cur_H.getString(c) == null)
                        DataList = "";
                    else if(cur_H.getString(c).equals("null"))
                        DataList = "";
                    else
                        DataList = cur_H.getString(c).toString();

                }
                else
                {
                    if (cur_H.getString(c) == null)
                        DataList+="^"+"";
                    else if(cur_H.getString(c).equals("null"))
                        DataList+="^"+"";
                    else
                        DataList+="^"+cur_H.getString(c).toString();
                }
            }

            //Prepare Where Clause
            VarData = DataList.split("\\^");
            varPos=0;


            for(int j=0; j< UField.length; j++)
            {
                varPos=VarPosition(UField[j].toString(),Count);
                if(j==0)
                {
                    WhereClause = "\""+ UField[j].toString()+"\"="+ "'"+ VarData[varPos].toString() +"'";
                }
                else
                {
                    WhereClause += " and \"" +UField[j].toString()+"\"="+ "'"+ VarData[varPos].toString() +"'";
                }
            }

            d = new DataClassProperty();
            d.setdatalist(DataList);
            d.setuniquefieldwithdata(WhereClause);
            data.add(d);

            RecordCount+=1;
            cur_H.moveToNext();
        }
        cur_H.close();

        return data;
    }
    public List<DataClassProperty> GetDataListJSONworkPlanMaster(String VariableList,String TableName,String UniqueField,String Month,String providerId, int limit_of_records, int offset_settings)
    {

      /*//  Save("PRAGMA journal_mode = TRUNCATE");
        Save("PRAGMA auto_vacuum = 1");
        Save("PRAGMA locking_mod = EXCLUSIVE");*/

     /*   select * from workPlanMaster A
        INNER JOIN workPlanDetail B ON A.workPlanId = B.workPlanId
        WHERE  A.month='2016-01' and A.providerId='93002'*/

        // cur_H = ReadData("Select "+ VariableList +" from "+ TableName +" s1 inner join Section2 s2 inner join Section3 s3 inner join Section4 s4 inner join Section5 s5 inner join Section6 s6 inner join Section7 s7 inner join Section8 s8 inner join Section9 s9 inner join Section10 s10  inner join Section77 s77 where s1.Upload='2' and s2.idno=s1.idno and s3.idno=s1.idno and s4.idno=s1.idno and s5.idno=s1.idno and s6.idno=s1.idno and s7.idno=s1.idno and s8.idno=s1.idno and s9.idno=s1.idno and (s10.idno=s1.idno and s10.Slno='01') and (s77.idno=s1.idno and s77.Slno='01')");
        // Cursor cur_H = ReadData("Select " + VariableList + " from " + TableName + " WHERE Upload='2' LIMIT "+ limit_of_records);
        Cursor cur_H = ReadData("Select " + VariableList + " from " + TableName + "  WHERE Upload='2' AND month='"+Month+"' AND providerId='"+providerId+"' LIMIT "+ limit_of_records); // +" OFFSET "+ offset_settings
        cur_H.moveToFirst();
        List<DataClassProperty> data = new ArrayList<DataClassProperty>();
        DataClassProperty d;

        String DataList = "";
        //String[] Count  = VariableList.toString().split(",");
        String[] Count  = VariableList.toString().replace(" ","").split(",");
        //String[] UField = UniqueField.toString().split(",");
        String[] UField = UniqueField.toString().replace(" ","").split(",");
        int RecordCount=0;

        String WhereClause="";
        String VarData[];
        int varPos=0;
        while(!cur_H.isAfterLast())
        {
            //Prepare Data List
            for(int c=0; c < Count.length; c++)
            {
                if(c==0)
                {
                    if (cur_H.getString(c) == null)
                        DataList = "";
                    else if(cur_H.getString(c).equals("null"))
                        DataList = "";
                    else
                        DataList = cur_H.getString(c).toString();

                }
                else
                {
                    if (cur_H.getString(c) == null)
                        DataList+="^"+"";
                    else if(cur_H.getString(c).equals("null"))
                        DataList+="^"+"";
                    else
                        DataList+="^"+cur_H.getString(c).toString();
                }
            }

            //Prepare Where Clause
            VarData = DataList.split("\\^");
            varPos=0;


            for(int j=0; j< UField.length; j++)
            {
                varPos=VarPosition(UField[j].toString(),Count);
                if(j==0)
                {
                    WhereClause = "\""+ UField[j].toString()+"\"="+ "'"+ VarData[varPos].toString() +"'";
                }
                else
                {
                    WhereClause += " and \"" +UField[j].toString()+"\"="+ "'"+ VarData[varPos].toString() +"'";
                }
            }

            d = new DataClassProperty();
            d.setdatalist(DataList);
            d.setuniquefieldwithdata(WhereClause);
            data.add(d);

            RecordCount+=1;
            cur_H.moveToNext();
        }
        cur_H.close();

        return data;
    }

    //New add code by nisan
//workPlanDetail
    public List<DataClassProperty> GetDataListJSONworkPlanDetail(String VariableList,String TableName,String UniqueField,String Month,String providerId, int limit_of_records, int offset_settings)
    {

      /*//  Save("PRAGMA journal_mode = TRUNCATE");
        Save("PRAGMA auto_vacuum = 1");
        Save("PRAGMA locking_mod = EXCLUSIVE");*/

     /*   select * from workPlanMaster A
        INNER JOIN workPlanDetail B ON A.workPlanId = B.workPlanId
        WHERE  A.month='2016-01' and A.providerId='93002'*/

        // cur_H = ReadData("Select "+ VariableList +" from "+ TableName +" s1 inner join Section2 s2 inner join Section3 s3 inner join Section4 s4 inner join Section5 s5 inner join Section6 s6 inner join Section7 s7 inner join Section8 s8 inner join Section9 s9 inner join Section10 s10  inner join Section77 s77 where s1.Upload='2' and s2.idno=s1.idno and s3.idno=s1.idno and s4.idno=s1.idno and s5.idno=s1.idno and s6.idno=s1.idno and s7.idno=s1.idno and s8.idno=s1.idno and s9.idno=s1.idno and (s10.idno=s1.idno and s10.Slno='01') and (s77.idno=s1.idno and s77.Slno='01')");
        // Cursor cur_H = ReadData("Select " + VariableList + " from " + TableName + " WHERE Upload='2' LIMIT "+ limit_of_records);
        Cursor cur_H = ReadData("Select " + VariableList + " from " + TableName + "  WHERE Upload='2' AND substr( workPlanDate, 1, 7 )='"+Month+"' AND providerId='"+providerId+"' LIMIT "+ limit_of_records); // +" OFFSET "+ offset_settings
        cur_H.moveToFirst();
        List<DataClassProperty> data = new ArrayList<DataClassProperty>();
        DataClassProperty d;

        String DataList = "";
        //String[] Count  = VariableList.toString().split(",");
        String[] Count  = VariableList.toString().replace(" ","").split(",");
        //String[] UField = UniqueField.toString().split(",");
        String[] UField = UniqueField.toString().replace(" ","").split(",");
        int RecordCount=0;

        String WhereClause="";
        String VarData[];
        int varPos=0;
        while(!cur_H.isAfterLast())
        {
            //Prepare Data List
            for(int c=0; c < Count.length; c++)
            {
                if(c==0)
                {
                    if (cur_H.getString(c) == null)
                        DataList = "";
                    else if(cur_H.getString(c).equals("null"))
                        DataList = "";
                    else
                        DataList = cur_H.getString(c).toString();

                }
                else
                {
                    if (cur_H.getString(c) == null)
                        DataList+="^"+"";
                    else if(cur_H.getString(c).equals("null"))
                        DataList+="^"+"";
                    else
                        DataList+="^"+cur_H.getString(c).toString();
                }
            }

            //Prepare Where Clause
            VarData = DataList.split("\\^");
            varPos=0;


            for(int j=0; j< UField.length; j++)
            {
                varPos=VarPosition(UField[j].toString(),Count);
                if(j==0)
                {
                    WhereClause = "\""+ UField[j].toString()+"\"="+ "'"+ VarData[varPos].toString() +"'";
                }
                else
                {
                    WhereClause += " and \"" +UField[j].toString()+"\"="+ "'"+ VarData[varPos].toString() +"'";
                }
            }

            d = new DataClassProperty();
            d.setdatalist(DataList);
            d.setuniquefieldwithdata(WhereClause);
            data.add(d);

            RecordCount+=1;
            cur_H.moveToNext();
        }
        cur_H.close();

        return data;
    }
    public List<DataClassProperty> GetDataListJSONworkPlanDetailNotApproved(String VariableList, String TableName, String UniqueField, String Month, String providerId,String status, int limit_of_records, int offset_settings) {

      /*//  Save("PRAGMA journal_mode = TRUNCATE");
        Save("PRAGMA auto_vacuum = 1");
        Save("PRAGMA locking_mod = EXCLUSIVE");*/

     /*   select * from workPlanMaster A
        INNER JOIN workPlanDetail B ON A.workPlanId = B.workPlanId
        WHERE  A.month='2016-01' and A.providerId='93002'*/

        // cur_H = ReadData("Select "+ VariableList +" from "+ TableName +" s1 inner join Section2 s2 inner join Section3 s3 inner join Section4 s4 inner join Section5 s5 inner join Section6 s6 inner join Section7 s7 inner join Section8 s8 inner join Section9 s9 inner join Section10 s10  inner join Section77 s77 where s1.Upload='2' and s2.idno=s1.idno and s3.idno=s1.idno and s4.idno=s1.idno and s5.idno=s1.idno and s6.idno=s1.idno and s7.idno=s1.idno and s8.idno=s1.idno and s9.idno=s1.idno and (s10.idno=s1.idno and s10.Slno='01') and (s77.idno=s1.idno and s77.Slno='01')");
        // Cursor cur_H = ReadData("Select " + VariableList + " from " + TableName + " WHERE Upload='2' LIMIT "+ limit_of_records);
        Cursor cur_H = ReadData("Select " + VariableList + " from " + TableName + "  WHERE Upload='1' and status='"+status+"' AND substr( workPlanDate, 1, 7 )='" + Month + "' AND providerId='" + providerId + "' LIMIT " + limit_of_records); // +" OFFSET "+ offset_settings
        cur_H.moveToFirst();
        List<DataClassProperty> data = new ArrayList<DataClassProperty>();
        DataClassProperty d;

        String DataList = "";
        //String[] Count  = VariableList.toString().split(",");
        String[] Count = VariableList.toString().replace(" ", "").split(",");
        //String[] UField = UniqueField.toString().split(",");
        String[] UField = UniqueField.toString().replace(" ", "").split(",");
        int RecordCount = 0;

        String WhereClause = "";
        String VarData[];
        int varPos = 0;
        while (!cur_H.isAfterLast()) {
            //Prepare Data List
            for (int c = 0; c < Count.length; c++) {
                if (c == 0) {
                    if (cur_H.getString(c) == null)
                        DataList = "";
                    else if (cur_H.getString(c).equals("null"))
                        DataList = "";
                    else
                        DataList = cur_H.getString(c).toString();

                } else {
                    if (cur_H.getString(c) == null)
                        DataList += "^" + "";
                    else if (cur_H.getString(c).equals("null"))
                        DataList += "^" + "";
                    else
                        DataList += "^" + cur_H.getString(c).toString();
                }
            }

            //Prepare Where Clause
            VarData = DataList.split("\\^");
            varPos = 0;


            for (int j = 0; j < UField.length; j++) {
                varPos = VarPosition(UField[j].toString(), Count);
                if (j == 0) {
                    WhereClause = "\"" + UField[j].toString() + "\"=" + "'" + VarData[varPos].toString() + "'";
                } else {
                    WhereClause += " and \"" + UField[j].toString() + "\"=" + "'" + VarData[varPos].toString() + "'";
                }
            }

            d = new DataClassProperty();
            d.setdatalist(DataList);
            d.setuniquefieldwithdata(WhereClause);
            data.add(d);

            RecordCount += 1;
            cur_H.moveToNext();
        }
        cur_H.close();

        return data;
    }
///
    /*
    Previous version of GetDataListJSON Function
    public List<DataClassProperty> GetDataListJSON(String VariableList,String TableName,String UniqueField)
    {


        Cursor cur_H = ReadData("Select " + VariableList + " from " + TableName + " WHERE Upload='2'");
        cur_H.moveToFirst();
        List<DataClassProperty> data = new ArrayList<DataClassProperty>();
        DataClassProperty d;

        String DataList = "";
        //String[] Count  = VariableList.toString().split(",");
        String[] Count  = VariableList.toString().replace(" ","").split(",");
        //String[] UField = UniqueField.toString().split(",");
        String[] UField = UniqueField.toString().replace(" ","").split(",");
        int RecordCount=0;

        String WhereClause="";
        String VarData[];
        int varPos=0;
        while(!cur_H.isAfterLast())
        {
            //Prepare Data List
            for(int c=0; c < Count.length; c++)
            {
                if(c==0)
                {
                    if (cur_H.getString(c) == null)
                        DataList = "";
                    else if(cur_H.getString(c).equals("null"))
                        DataList = "";
                    else
                        DataList = cur_H.getString(c).toString();

                }
                else
                {
                    if (cur_H.getString(c) == null)
                        DataList+="^"+"";
                    else if(cur_H.getString(c).equals("null"))
                        DataList+="^"+"";
                    else
                        DataList+="^"+cur_H.getString(c).toString();
                }
            }

            //Prepare Where Clause
            VarData = DataList.split("\\^");
            varPos=0;


            for(int j=0; j< UField.length; j++)
            {
                varPos=VarPosition(UField[j].toString(),Count);
                if(j==0)
                {
                    WhereClause = "\""+ UField[j].toString()+"\"="+ "'"+ VarData[varPos].toString() +"'";
                }
                else
                {
                    WhereClause += " and \"" +UField[j].toString()+"\"="+ "'"+ VarData[varPos].toString() +"'";
                }
            }

            d = new DataClassProperty();
            d.setdatalist(DataList);
            d.setuniquefieldwithdata(WhereClause);
            data.add(d);

            RecordCount+=1;
            cur_H.moveToNext();
        }
        cur_H.close();

        return data;
    }


    */

    //Download Data from Server (Soap Service/Web API)
    public  String DownloadJSON(String SQL,String TableName,String ColumnList, String UniqueField)
    {
        DownloadRequestClass dr = new DownloadRequestClass();
        dr.setmethodname("DownloadData");
        dr.setSQL(SQL);
        Gson gson = new Gson();
        String json = gson.toJson(dr);

        String WhereClause="";
        int varPos=0;

        String response = "";
        String resp = "";
        String IDNO ="";
        try{


            //For Web Service
            //--------------------------------------------------------------------------------------
            //DownloadDataJSON dload = new DownloadDataJSON();
            //response=dload.execute(SQL).get();
            //Process Response
            //downloadClass d = new downloadClass();
            //Gson gson = new Gson();
            //Type collType = new TypeToken<downloadClass>(){}.getType();
            //downloadClass responseData = (downloadClass) gson.fromJson(response,collType);


            //For Web Api
            //--------------------------------------------------------------------------------------
            DownloadJSONData dload = new DownloadJSONData();
            response = dload.execute(json).get();
            downloadClass responseData = (downloadClass) gson.fromJson(response,downloadClass.class);
            //--------------------------------------------------------------------------------------


            String UField[]  = UniqueField.toString().replace(" ","").split(",");
            String VarList[] = ColumnList.toString().replace(" ","").split(",");

            List<String> dataStatus = new ArrayList<String>();
            String modify_Date = "";
            for(int i=0; i<responseData.getdata().size(); i++)
            {
                String VarData[] = split(responseData.getdata().get(i).toString(),'^');

                //Generate where clause
                SQL="";
                WhereClause="";
                varPos=0;
                for(int j=0; j< UField.length; j++)
                {
                    varPos = VarPosition(UField[j].toString(),VarList);
                    if(j==0)
                    {
                        WhereClause = UField[j].toString()+"="+ "'"+ VarData[varPos].toString() +"'";
                        //IDNO += VarData[varPos].toString();
                    }
                    else
                    {
                        WhereClause += " and "+ UField[j].toString()+"="+ "'"+ VarData[varPos].toString() +"'";
                       // IDNO += VarData[varPos].toString();
                    }
                }

                //Update command
                if(Existence("Select "+ VarList[0] +" from "+ TableName +" Where "+ WhereClause))
                {
                    for(int r=0;r<VarList.length;r++)
                    {
                        if(r==0)
                        {
                            SQL = "Update "+ TableName +" Set ";
                            SQL+= VarList[r] + " = '"+ VarData[r].toString() +"'";
                        }
                        else
                        {
                            if(r == VarData.length-1)
                            {
                                SQL+= ","+ VarList[r] + " = '"+ VarData[r].toString() +"'";
                                SQL += " Where "+ WhereClause;
                            }
                            else
                            {
                                SQL+= ","+ VarList[r] + " = '"+ VarData[r].toString() +"'";
                            }
                        }
                        //if("modifyDate".equalsIgnoreCase(VarList[r]))
                        //{
                            //if(!VarData[r].toString().equals(null) | !VarData[r].toString().equals("null")) {
                           // modify_Date = VarData[r].toString().replace("null", "");
                            //}

                       // }
                    }

                    Save(SQL);
                }
                //Insert command
                else
                {
                    for(int r=0;r<VarList.length;r++)
                    {
                        if(r==0)
                        {
                            SQL = "Insert into "+ TableName +"("+ ColumnList +")Values(";
                            SQL+= "'"+ VarData[r].toString() +"'";
                        }
                        else
                        {
                            SQL+= ",'"+ VarData[r].toString() +"'";
                        }
                        /*if("modifyDate".equalsIgnoreCase(VarList[r]))
                        {
                            // if(!VarData[r].toString().equals(null) | !VarData[r].toString().equals("null")) {
                            modify_Date = VarData[r].toString().replace("null", "");
                            //   }
                        }*/
                    }
                    SQL += ")";

                    Save(SQL);
                    //if("modifyDate".equalsIgnoreCase(VarList[r]))
                }

               /* dataStatus.add("Insert into \"data_sync_management\"(\"tableName\"\n" +
                        "\t\t,\"recordId\"\n" +
                        "\t\t,\"modifyDate\"\n" +
                        "\t\t,\"provType\"\n" +
                        "\t\t,\"provCode\")\n" +
                        "\tVALUES\n" +
                        "\t\t('"+ TableName +"', '"+ IDNO +"','"+ modify_Date +"', '"+
                        Global.getInstance().getProvType() +"', '" +
                        Global.getInstance().getProvCode() +"') ");
                IDNO = "";*/

            }


            //Status back to server
           // if(dataStatus.size()>0)
           // {
                //Generate SQL String
               /* List<String> sqlString = new ArrayList<String>();
                for(String data : dataStatus){
                    sqlString.add(data);

                }*/
              //  ExecuteCommandOnServerJSON(dataStatus);
            //}


        } catch (Exception e) {
            resp = e.getMessage();
            e.printStackTrace();
        }

        return resp;
    }
/*    public  String DownloadJSON(String SQL,String TableName,String ColumnList, String UniqueField)
    {
        DownloadRequestClass dr = new DownloadRequestClass();
        dr.setmethodname("DownloadData");
        dr.setSQL(SQL);
        Gson gson = new Gson();
        String json = gson.toJson(dr);

        String WhereClause="";
        int varPos=0;

        String response = "";
        String resp = "";

        try{


            //For Web Service
            //--------------------------------------------------------------------------------------
            //DownloadDataJSON dload = new DownloadDataJSON();
            //response=dload.execute(SQL).get();
            //Process Response
            //downloadClass d = new downloadClass();
            //Gson gson = new Gson();
            //Type collType = new TypeToken<downloadClass>(){}.getType();
            //downloadClass responseData = (downloadClass) gson.fromJson(response,collType);


            //For Web Api
            //--------------------------------------------------------------------------------------
            DownloadJSONData dload = new DownloadJSONData();
            response = dload.execute(json).get();
            downloadClass responseData = (downloadClass) gson.fromJson(response,downloadClass.class);
            //--------------------------------------------------------------------------------------


            String UField[]  = UniqueField.toString().replace(" ","").split(",");
            String VarList[] = ColumnList.toString().replace(" ","").split(",");

            List<String> dataStatus = new ArrayList<String>();

            for(int i=0; i<responseData.getdata().size(); i++)
            {
                String VarData[] = split(responseData.getdata().get(i).toString(),'^');

                //Generate where clause
                SQL="";
                WhereClause="";
                varPos=0;
                for(int j=0; j< UField.length; j++)
                {
                    varPos = VarPosition(UField[j].toString(),VarList);
                    if(j==0)
                    {
                        WhereClause = UField[j].toString()+"="+ "'"+ VarData[varPos].toString() +"'";
                    }
                    else
                    {
                        WhereClause += " and "+ UField[j].toString()+"="+ "'"+ VarData[varPos].toString() +"'";
                    }
                }

                //Update command
                if(Existence("Select "+ VarList[0] +" from "+ TableName +" Where "+ WhereClause))
                {
                    for(int r=0;r<VarList.length;r++)
                    {
                        if(r==0)
                        {
                            SQL = "Update "+ TableName +" Set ";
                            SQL+= VarList[r] + " = '"+ VarData[r].toString() +"'";
                        }
                        else
                        {
                            if(r == VarData.length-1)
                            {
                                SQL+= ","+ VarList[r] + " = '"+ VarData[r].toString() +"'";
                                SQL += " Where "+ WhereClause;
                            }
                            else
                            {
                                SQL+= ","+ VarList[r] + " = '"+ VarData[r].toString() +"'";
                            }
                        }
                    }

                    Save(SQL.replace("Upload = '2'", "Upload = '1'"));
                }
                //Insert command
                else
                {
                    for(int r=0;r<VarList.length;r++)
                    {
                        if(r==0)
                        {
                            SQL = "Insert into "+ TableName +"("+ ColumnList +")Values(";
                            SQL+= "'"+ VarData[r].toString() +"'";
                        }
                        else
                        {
                            SQL+= ",'"+ VarData[r].toString() +"'";
                        }
                    }
                    SQL += ")";

                    Save(SQL.replace("Upload='2'", "Upload='1'"));
                }

                dataStatus.add(WhereClause);
            }


            //Status back to server
            if(dataStatus.size()>0)
            {

            }


        } catch (Exception e) {
            resp = e.getMessage();
            e.printStackTrace();
        }

        return resp;
    }*/

    public String ExecuteCommandOnServerJSON(List<String> SQLList)
    {
        ExecuteCommandClass dr = new ExecuteCommandClass();
        dr.setmethodname("ExecuteSQLList");
        dr.setSQL(SQLList);
        dr.setSecutiryCodeL("org.postgresql");
        Gson gson = new Gson();
        String json = gson.toJson(dr);

        DownloadJSONData dload = new DownloadJSONData();
        String response = null;
        try {
            response = dload.execute(json).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return response;
    }

    //Upload Data to Server (Soap Service/Web API)
    public void UploadJSON(String TableName,String ColumnList,String UniqueFields, int total_records_in_table)
    {
       // int total_records_in_table = this.TotalRecordCount(TableName);
        int limit_of_records = total_records_in_table;
        int offset_settings = 0;


            DataClass dt = new DataClass();
            dt.setmethodname("UploadData");
            dt.settablename(TableName);
            dt.setcolumnlist(ColumnList);
            List<DataClassProperty> data = GetDataListJSON(ColumnList, TableName, UniqueFields, limit_of_records, offset_settings);
            dt.setdata(data);

            Gson gson = new Gson();
            String json = gson.toJson(dt);
            String response = "";

            //Web Service(asmx)
            //------------------------------------------------------------------------------------------
            //UploadDataJSON u = new UploadDataJSON();

            //Web APIs(java)
            //------------------------------------------------------------------------------------------
            UploadJSONData u = new UploadJSONData();

            try{
                response=u.execute(json).get();

                //Process Response
                downloadClass d = new downloadClass();
                Type collType   = new TypeToken<downloadClass>(){}.getType();
                downloadClass responseData = (downloadClass) gson.fromJson(response,collType);


                for(int i=0; i<responseData.getdata().size(); i++)
                {
                    // String s ="Update " + TableName + " Set Upload='2' where " + responseData.getdata().get(i).toString();
                    Save("Update " + TableName + " Set Upload='1' where " + responseData.getdata().get(i).toString());


                }

            } catch (Exception e) {
                //  Toast.makeText(dbContext, "All data did not upload. Try again.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

    }
    public void UploadJSON(String TableName,String ColumnList,String UniqueFields)
    {
        int total_records_in_table = this.TotalRecordCount(TableName);
        int limit_of_records = 20;
        int offset_settings = 0;
        int total_sent = 0;

        if(total_records_in_table==0)
            return;

        if(total_records_in_table<=20)
        {
            UploadJSON(TableName, ColumnList, UniqueFields, total_records_in_table);
            return;
        }


       int total_count = total_records_in_table/20;

        for(int start=0; start<= total_count; start=start+1)
        {
            if((total_records_in_table-total_sent)<=20)
            {
                UploadJSON(TableName, ColumnList, UniqueFields, total_records_in_table);
                return;
            }

            DataClass dt = new DataClass();
            dt.setmethodname("UploadData");
            dt.settablename(TableName);
            dt.setcolumnlist(ColumnList);
            List<DataClassProperty> data = GetDataListJSON(ColumnList, TableName, UniqueFields, limit_of_records, offset_settings);
            dt.setdata(data);

            Gson gson = new Gson();
            String json = gson.toJson(dt);
            String response = "";

            //Web Service(asmx)
            //------------------------------------------------------------------------------------------
            //UploadDataJSON u = new UploadDataJSON();

            //Web APIs(java)
            //------------------------------------------------------------------------------------------
            UploadJSONData u = new UploadJSONData();

            try{
                response=u.execute(json).get();

                //Process Response
                downloadClass d = new downloadClass();
                Type collType   = new TypeToken<downloadClass>(){}.getType();
                downloadClass responseData = (downloadClass) gson.fromJson(response,collType);


                for(int i=0; i<responseData.getdata().size(); i++)
                {
                    // String s ="Update " + TableName + " Set Upload='2' where " + responseData.getdata().get(i).toString();
                    Save("Update " + TableName + " Set Upload='1' where " + responseData.getdata().get(i).toString());
                    total_sent=total_sent+1;

                }

            } catch (Exception e) {
              //  Toast.makeText(dbContext, "All data did not upload. Try again.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            offset_settings = limit_of_records + offset_settings;
           // return response;
        }


    }

//New add code by nisan
    //workPlanMaster
    // Month,providerId


    public void UploadJSONAproved(String TableName,String ColumnList,String UniqueFields, int total_records_in_table)
    {
        // int total_records_in_table = this.TotalRecordCount(TableName);
        int limit_of_records = total_records_in_table;
        int offset_settings = 0;


        DataClass dt = new DataClass();
        dt.setmethodname("UploadData");
        dt.settablename(TableName);
        dt.setcolumnlist(ColumnList);
        List<DataClassProperty> data = GetDataListJSONAproved(ColumnList, TableName, UniqueFields, limit_of_records, offset_settings);
        dt.setdata(data);

        Gson gson = new Gson();
        String json = gson.toJson(dt);
        String response = "";

        //Web Service(asmx)
        //------------------------------------------------------------------------------------------
        //UploadDataJSON u = new UploadDataJSON();

        //Web APIs(java)
        //------------------------------------------------------------------------------------------
        UploadJSONData u = new UploadJSONData();

        try{
            response=u.execute(json).get();

            //Process Response
            downloadClass d = new downloadClass();
            Type collType   = new TypeToken<downloadClass>(){}.getType();
            downloadClass responseData = (downloadClass) gson.fromJson(response,collType);


            for(int i=0; i<responseData.getdata().size(); i++)
            {
                // String s ="Update " + TableName + " Set Upload='2' where " + responseData.getdata().get(i).toString();
                Save("Update " + TableName + " Set Upload='1' where " + responseData.getdata().get(i).toString());


            }

        } catch (Exception e) {
            //  Toast.makeText(dbContext, "All data did not upload. Try again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }
    public void UploadJSONAproved(String TableName,String ColumnList,String UniqueFields)
    {
        int total_records_in_table = this.TotalRecordCount(TableName);
        int limit_of_records = 20;
        int offset_settings = 0;
        int total_sent = 0;

        if(total_records_in_table==0)
            return;

        if(total_records_in_table<=20)
        {
            UploadJSONAproved(TableName, ColumnList, UniqueFields, total_records_in_table);
            return;
        }


        int total_count = total_records_in_table/20;

        for(int start=0; start<= total_count; start=start+1)
        {
            if((total_records_in_table-total_sent)<=20)
            {
                UploadJSONAproved(TableName, ColumnList, UniqueFields, total_records_in_table);
                return;
            }

            DataClass dt = new DataClass();
            dt.setmethodname("UploadData");
            dt.settablename(TableName);
            dt.setcolumnlist(ColumnList);
            List<DataClassProperty> data = GetDataListJSONAproved(ColumnList, TableName, UniqueFields, limit_of_records, offset_settings);
            dt.setdata(data);

            Gson gson = new Gson();
            String json = gson.toJson(dt);
            String response = "";

            //Web Service(asmx)
            //------------------------------------------------------------------------------------------
            //UploadDataJSON u = new UploadDataJSON();

            //Web APIs(java)
            //------------------------------------------------------------------------------------------
            UploadJSONData u = new UploadJSONData();

            try{
                response=u.execute(json).get();

                //Process Response
                downloadClass d = new downloadClass();
                Type collType   = new TypeToken<downloadClass>(){}.getType();
                downloadClass responseData = (downloadClass) gson.fromJson(response,collType);


                for(int i=0; i<responseData.getdata().size(); i++)
                {
                    // String s ="Update " + TableName + " Set Upload='2' where " + responseData.getdata().get(i).toString();
                    Save("Update " + TableName + " Set Upload='1' where " + responseData.getdata().get(i).toString());
                    total_sent=total_sent+1;

                }

            } catch (Exception e) {
                //  Toast.makeText(dbContext, "All data did not upload. Try again.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            offset_settings = limit_of_records + offset_settings;
            // return response;
        }


    }

    public void UploadJSONworkPlanMaster(String TableName,String ColumnList,String UniqueFields,String Month,String providerId, int total_records_in_table)
    {
        // int total_records_in_table = this.TotalRecordCount(TableName);
        int limit_of_records = total_records_in_table;
        int offset_settings = 0;


        DataClass dt = new DataClass();
        dt.setmethodname("UploadData");
        dt.settablename(TableName);
        dt.setcolumnlist(ColumnList);
        List<DataClassProperty> data = GetDataListJSONworkPlanMaster(ColumnList, TableName, UniqueFields,Month,providerId, limit_of_records, offset_settings);
        dt.setdata(data);

        Gson gson = new Gson();
        String json = gson.toJson(dt);
        String response = "";

        //Web Service(asmx)
        //------------------------------------------------------------------------------------------
        //UploadDataJSON u = new UploadDataJSON();

        //Web APIs(java)
        //------------------------------------------------------------------------------------------
        UploadJSONData u = new UploadJSONData();

        try{
            response=u.execute(json).get();

            //Process Response
            downloadClass d = new downloadClass();
            Type collType   = new TypeToken<downloadClass>(){}.getType();
            downloadClass responseData = (downloadClass) gson.fromJson(response,collType);


            for(int i=0; i<responseData.getdata().size(); i++)
            {
                // String s ="Update " + TableName + " Set Upload='2' where " + responseData.getdata().get(i).toString();
                Save("Update " + TableName + " Set Upload='1' where " + responseData.getdata().get(i).toString());


            }

        } catch (Exception e) {
            //  Toast.makeText(dbContext, "All data did not upload. Try again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    public void UploadJSONworkPlanMaster(String TableName,String ColumnList,String UniqueFields,String Month,String providerId)
    {
        int total_records_in_table = this.TotalRecordCount(TableName);
        int records_to_sync_at_a_time = Integer.parseInt(this.ReturnSingleValue("SELECT  dataLength AS Total FROM DataLengthTable  WHERE tableName = '" + TableName +"'"));
        if(records_to_sync_at_a_time<=0)
        {
            records_to_sync_at_a_time = 20;
        }
        int limit_of_records = records_to_sync_at_a_time;
        int offset_settings = 0;
        int total_sent = 0;

        if(total_records_in_table==0)
            return;

        if(total_records_in_table<=records_to_sync_at_a_time)
        {
            UploadJSONworkPlanMaster(TableName, ColumnList, UniqueFields,Month,providerId, total_records_in_table);
            return;
        }


        int total_count = total_records_in_table/records_to_sync_at_a_time;

        for(int start=0; start<= total_count; start=start+1)
        {
            if((total_records_in_table-total_sent)<=records_to_sync_at_a_time)
            {
                UploadJSONworkPlanMaster(TableName, ColumnList, UniqueFields,Month,providerId, total_records_in_table);
                return;
            }

            DataClass dt = new DataClass();
            dt.setmethodname("UploadData");
            dt.settablename(TableName);
            dt.setcolumnlist(ColumnList);
            List<DataClassProperty> data = GetDataListJSONworkPlanMaster(ColumnList, TableName, UniqueFields,Month,providerId, limit_of_records, offset_settings);
            dt.setdata(data);

            Gson gson = new Gson();
            String json = gson.toJson(dt);
            String response = "";

            //Web Service(asmx)
            //------------------------------------------------------------------------------------------
            //UploadDataJSON u = new UploadDataJSON();

            //Web APIs(java)
            //------------------------------------------------------------------------------------------
            UploadJSONData u = new UploadJSONData();

            try{
                response=u.execute(json).get();

                //Process Response
                downloadClass d = new downloadClass();
                Type collType   = new TypeToken<downloadClass>(){}.getType();
                downloadClass responseData = (downloadClass) gson.fromJson(response,collType);


                for(int i=0; i<responseData.getdata().size(); i++)
                {
                    // String s ="Update " + TableName + " Set Upload='2' where " + responseData.getdata().get(i).toString();
                    Save("Update " + TableName + " Set Upload='1' where " + responseData.getdata().get(i).toString());
                    total_sent=total_sent+1;

                }

            } catch (Exception e) {
                //  Toast.makeText(dbContext, "All data did not upload. Try again.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            offset_settings = limit_of_records + offset_settings;
            // return response;
        }


    }


    //workPlanDetail
    // Month,providerId

    public void UploadJSONworkPlanDetail(String TableName,String ColumnList,String UniqueFields,String Month,String providerId, int total_records_in_table)
    {
        // int total_records_in_table = this.TotalRecordCount(TableName);
        int limit_of_records = total_records_in_table;
        int offset_settings = 0;


        DataClass dt = new DataClass();
        dt.setmethodname("UploadData");
        dt.settablename(TableName);
        dt.setcolumnlist(ColumnList);
        List<DataClassProperty> data = GetDataListJSONworkPlanDetail(ColumnList, TableName, UniqueFields,Month,providerId, limit_of_records, offset_settings);
        dt.setdata(data);

        Gson gson = new Gson();
        String json = gson.toJson(dt);
        String response = "";

        //Web Service(asmx)
        //------------------------------------------------------------------------------------------
        //UploadDataJSON u = new UploadDataJSON();

        //Web APIs(java)
        //------------------------------------------------------------------------------------------
        UploadJSONData u = new UploadJSONData();

        try{
            response=u.execute(json).get();

            //Process Response
            downloadClass d = new downloadClass();
            Type collType   = new TypeToken<downloadClass>(){}.getType();
            downloadClass responseData = (downloadClass) gson.fromJson(response,collType);


            for(int i=0; i<responseData.getdata().size(); i++)
            {
                // String s ="Update " + TableName + " Set Upload='2' where " + responseData.getdata().get(i).toString();
                Save("Update " + TableName + " Set Upload='1' where " + responseData.getdata().get(i).toString());


            }

        } catch (Exception e) {
            //  Toast.makeText(dbContext, "All data did not upload. Try again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    public void UploadJSONworkPlanDetail(String TableName,String ColumnList,String UniqueFields,String Month,String providerId)
    {
        int total_records_in_table = this.TotalRecordCount(TableName);
        int records_to_sync_at_a_time = Integer.parseInt(this.ReturnSingleValue("SELECT  dataLength AS Total FROM DataLengthTable  WHERE tableName = '" + TableName +"'"));
        if(records_to_sync_at_a_time<=0)
        {
            records_to_sync_at_a_time = 20;
        }
        int limit_of_records = records_to_sync_at_a_time;
        int offset_settings = 0;
        int total_sent = 0;

        if(total_records_in_table==0)
            return;

        if(total_records_in_table<=records_to_sync_at_a_time)
        {
            UploadJSONworkPlanDetail(TableName, ColumnList, UniqueFields,Month,providerId, total_records_in_table);
            return;
        }


        int total_count = total_records_in_table/records_to_sync_at_a_time;

        for(int start=0; start<= total_count; start=start+1)
        {
            if((total_records_in_table-total_sent)<=records_to_sync_at_a_time)
            {
                UploadJSONworkPlanDetail(TableName, ColumnList, UniqueFields,Month,providerId, total_records_in_table);
                return;
            }

            DataClass dt = new DataClass();
            dt.setmethodname("UploadData");
            dt.settablename(TableName);
            dt.setcolumnlist(ColumnList);
            List<DataClassProperty> data = GetDataListJSONworkPlanDetail(ColumnList, TableName, UniqueFields,Month,providerId, limit_of_records, offset_settings);
            dt.setdata(data);

            Gson gson = new Gson();
            String json = gson.toJson(dt);
            String response = "";

            //Web Service(asmx)
            //------------------------------------------------------------------------------------------
            //UploadDataJSON u = new UploadDataJSON();

            //Web APIs(java)
            //------------------------------------------------------------------------------------------
            UploadJSONData u = new UploadJSONData();

            try{
                response=u.execute(json).get();

                //Process Response
                downloadClass d = new downloadClass();
                Type collType   = new TypeToken<downloadClass>(){}.getType();
                downloadClass responseData = (downloadClass) gson.fromJson(response,collType);


                for(int i=0; i<responseData.getdata().size(); i++)
                {
                    // String s ="Update " + TableName + " Set Upload='2' where " + responseData.getdata().get(i).toString();
                    Save("Update " + TableName + " Set Upload='1' where " + responseData.getdata().get(i).toString());
                    total_sent=total_sent+1;

                }

            } catch (Exception e) {
                //  Toast.makeText(dbContext, "All data did not upload. Try again.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            offset_settings = limit_of_records + offset_settings;
            // return response;
        }


    }


    //NotApproved

    public void UploadJSONworkPlanDetailNotApproved(String TableName, String ColumnList, String UniqueFields, String Month, String providerId, String status, int total_records_in_table) {
        // int total_records_in_table = this.TotalRecordCount(TableName);
        int limit_of_records = total_records_in_table;
        int offset_settings = 0;


        DataClass dt = new DataClass();
        dt.setmethodname("UploadData");
        dt.settablename(TableName);
        dt.setcolumnlist(ColumnList);
        List<DataClassProperty> data = GetDataListJSONworkPlanDetailNotApproved(ColumnList, TableName, UniqueFields, Month, providerId,status, limit_of_records, offset_settings);
        dt.setdata(data);

        Gson gson = new Gson();
        String json = gson.toJson(dt);
        String response = "";

        //Web Service(asmx)
        //------------------------------------------------------------------------------------------
        //UploadDataJSON u = new UploadDataJSON();

        //Web APIs(java)
        //------------------------------------------------------------------------------------------
        UploadJSONData u = new UploadJSONData();

        try {
            response = u.execute(json).get();

            //Process Response
            downloadClass d = new downloadClass();
            Type collType = new TypeToken<downloadClass>() {
            }.getType();
            downloadClass responseData = (downloadClass) gson.fromJson(response, collType);


            for (int i = 0; i < responseData.getdata().size(); i++) {
                // String s ="Update " + TableName + " Set Upload='2' where " + responseData.getdata().get(i).toString();
                Save("Update " + TableName + " Set Upload='1' where " + responseData.getdata().get(i).toString());


            }

        } catch (Exception e) {
            //  Toast.makeText(dbContext, "All data did not upload. Try again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    public void UploadJSONworkPlanDetailNotApproved(String TableName, String ColumnList, String UniqueFields, String Month, String providerId,String status) {
        int total_records_in_table = this.TotalRecordCount(TableName);
        int records_to_sync_at_a_time = Integer.parseInt(this.ReturnSingleValue("SELECT  dataLength AS Total FROM DataLengthTable  WHERE tableName = '" + TableName + "'"));
        if (records_to_sync_at_a_time <= 0) {
            records_to_sync_at_a_time = 20;
        }
        int limit_of_records = records_to_sync_at_a_time;
        int offset_settings = 0;
        int total_sent = 0;

        if (total_records_in_table == 0)
            return;

        if (total_records_in_table <= records_to_sync_at_a_time) {
            UploadJSONworkPlanDetailNotApproved(TableName, ColumnList, UniqueFields, Month, providerId,status, total_records_in_table);
            return;
        }


        int total_count = total_records_in_table / records_to_sync_at_a_time;

        for (int start = 0; start <= total_count; start = start + 1) {
            if ((total_records_in_table - total_sent) <= records_to_sync_at_a_time) {
                UploadJSONworkPlanDetailNotApproved(TableName, ColumnList, UniqueFields, Month, providerId,status, total_records_in_table);
                return;
            }

            DataClass dt = new DataClass();
            dt.setmethodname("UploadData");
            dt.settablename(TableName);
            dt.setcolumnlist(ColumnList);
            List<DataClassProperty> data = GetDataListJSONworkPlanDetailNotApproved(ColumnList, TableName, UniqueFields, Month, providerId,status, limit_of_records, offset_settings);
            dt.setdata(data);

            Gson gson = new Gson();
            String json = gson.toJson(dt);
            String response = "";

            //Web Service(asmx)
            //------------------------------------------------------------------------------------------
            //UploadDataJSON u = new UploadDataJSON();

            //Web APIs(java)
            //------------------------------------------------------------------------------------------
            UploadJSONData u = new UploadJSONData();

            try {
                response = u.execute(json).get();

                //Process Response
                downloadClass d = new downloadClass();
                Type collType = new TypeToken<downloadClass>() {
                }.getType();
                downloadClass responseData = (downloadClass) gson.fromJson(response, collType);


                for (int i = 0; i < responseData.getdata().size(); i++) {
                    // String s ="Update " + TableName + " Set Upload='2' where " + responseData.getdata().get(i).toString();
                    Save("Update " + TableName + " Set Upload='1' where " + responseData.getdata().get(i).toString());
                    total_sent = total_sent + 1;

                }

            } catch (Exception e) {
                //  Toast.makeText(dbContext, "All data did not upload. Try again.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            offset_settings = limit_of_records + offset_settings;
            // return response;
        }


    }


    public String JoinQueryForAppropriateRecords(String sql, String tableName, String provCode)
    {
        if(tableName.equals("elco")) {
            sql += " " + " and not exists(select \"recordId\" from  data_sync_management where "
                    + "\"tableName\"='" + tableName + "' and \n"
                    + "\"modifyDate\" = coalesce(cast(C.\"modifyDate\" as text),'') and \n"
                    + "\"provCode\"=" + provCode + " and \n"
                    + "\"recordId\"=cast(C.\"healthId\" as text))";
        }
        if(tableName.equals("clientMap")) {
            sql += " " + " and not exists(select \"recordId\" from  data_sync_management where "
                    + "\"tableName\"='" + tableName + "' and \n"
                    + "\"modifyDate\" = coalesce(cast(C.\"modifyDate\" as text),'') and \n"
                    + "\"provCode\"=" + provCode + " and \n"
                    + "\"recordId\"=cast(C.\"generatedId\" as text))";
        }
        if(tableName.equals("elcoVisit")) {
            sql += " " + " and not exists(select \"recordId\" from  data_sync_management where "
                    + "\"tableName\"='" + tableName + "' and \n"
                    + "\"modifyDate\" = coalesce(cast(C.\"modifyDate\" as text),'') and \n"
                    + "\"provCode\"=" + provCode + " and \n"
                    + "\"recordId\"=cast(C.\"healthId\" as text)|| cast(C.\"visit\" as text))";
        }

        if(tableName.equals("PregRefer")) {
            sql += " " + " and not exists(select \"recordId\" from  data_sync_management where "
                    + "\"tableName\"='" + tableName + "' and \n"
                    + "\"modifyDate\" = coalesce(cast(C.\"modifyDate\" as text),'') and \n"
                    + "\"provCode\"=" + provCode + " and \n"
                    + "\"recordId\"=cast(C.\"healthId\" as text)|| cast(C.\"pregNo\" as text)|| cast(C.\"serviceId\" as text))";
        }
        if(tableName.equals("pregWomen")) {
            sql += " " + " and not exists(select \"recordId\" from  data_sync_management where "
                    + "\"tableName\"='" + tableName + "' and \n"
                    + "\"modifyDate\" = coalesce(cast(C.\"modifyDate\" as text),'') and \n"
                    + "\"provCode\"=" + provCode + " and \n"
                    + "\"recordId\"=cast(C.\"healthId\" as text)|| cast(C.\"pregNo\" as text))";
        }
        if(tableName.equals("ancService")) {
            sql += " " + " and not exists(select \"recordId\" from  data_sync_management where "
                    + "\"tableName\"='" + tableName + "' and \n"
                    + "\"modifyDate\" = coalesce(cast(C.\"modifyDate\" as text),'') and \n"
                    + "\"provCode\"=" + provCode + " and \n"
                    + "\"recordId\"=cast(C.\"healthId\" as text)|| cast(C.\"pregNo\" as text)|| cast(C.\"serviceId\" as text))";
        }
        if(tableName.equals("delivery")) {
            sql += " " + " and not exists(select \"recordId\" from  data_sync_management where "
                    + "\"tableName\"='" + tableName + "' and \n"
                    + "\"modifyDate\" = coalesce(cast(C.\"modifyDate\" as text),'') and \n"
                    + "\"provCode\"=" + provCode + " and \n"
                    + "\"recordId\"=cast(C.\"healthId\" as text)|| cast(C.\"pregNo\" as text))";
        }

        if(tableName.equals("newBorn")) {
            sql += " " + " and not exists(select \"recordId\" from  data_sync_management where "
                    + "\"tableName\"='" + tableName + "' and \n"
                    + "\"modifyDate\" = coalesce(cast(C.\"modifyDate\" as text),'') and \n"
                    + "\"provCode\"=" + provCode + " and \n"
                    + "\"recordId\"=cast(C.\"healthId\" as text)|| cast(C.\"pregNo\" as text)|| cast(C.\"childNo\" as text))";
        }
        if(tableName.equals("pncServiceChild")) {
            sql += " " + " and not exists(select \"recordId\" from  data_sync_management where "
                    + "\"tableName\"='" + tableName + "' and \n"
                    + "\"modifyDate\" = coalesce(cast(C.\"modifyDate\" as text),'') and \n"
                    + "\"provCode\"=" + provCode + " and \n"
                    + "\"recordId\"=cast(C.\"healthId\" as text)|| cast(C.\"pregNo\" as text)|| cast(C.\"childNo\" as text)|| cast(C.\"serviceId\" as text))";
        }

        if(tableName.equals("pncServiceMother")) {
            sql += " " + " and not exists(select \"recordId\" from  data_sync_management where "
                    + "\"tableName\"='" + tableName + "' and \n"
                    + "\"modifyDate\" = coalesce(cast(C.\"modifyDate\" as text),'') and \n"
                    + "\"provCode\"=" + provCode + " and \n"
                    + "\"recordId\"=cast(C.\"healthId\" as text)|| cast(C.\"pregNo\" as text)|| cast(C.\"serviceId\" as text))";
        }

        if(tableName.equals("womanImmunization")) {
            sql += " " + " and not exists(select \"recordId\" from  data_sync_management where "
                    + "\"tableName\"='" + tableName + "' and \n"
                    + "\"modifyDate\" = coalesce(cast(C.\"modifyDate\" as text),'') and \n"
                    + "\"provCode\"=" + provCode + " and \n"
                    + "\"recordId\"=cast(C.\"healthId\" as text)|| cast(C.\"imuCode\" as text))";
        }
        if(tableName.equals("stockTransaction")) {
            sql += " " + " and not exists(select \"recordId\" from  data_sync_management where "
                    + "\"tableName\"='" + tableName + "' and \n"
                    + "\"modifyDate\" = coalesce(cast(C.\"modifyDate\" as text),'') and \n"
                    + "\"provCode\"=" + provCode + " and \n"
                    + "\"recordId\"=cast(C.\"transactionId\" as text)|| cast(C.\"providerId\" as text)|| cast(C.\"transactionType\" as text)|| cast(C.\"itemCode\" as text))";
        }
        if(tableName.equals("immunizationHistory")) {
            sql += " " + " and not exists(select \"recordId\" from  data_sync_management where "
                    + "\"tableName\"='" + tableName + "' and \n"
                    + "\"modifyDate\" = coalesce(cast(C.\"modifyDate\" as text),'') and \n"
                    + "\"provCode\"=" + provCode + " and \n"
                    + "\"recordId\"=cast(C.\"healthId\" as text)|| cast(C.\"imuCode\" as text)|| cast(C.\"imuDose\" as text))";
        }
        /*if(tableName.equals("death")) {
            sql += " " + " and not exists(select \"recordId\" from  data_sync_management where "
                    + "\"tableName\"='" + tableName + "' and \n"
                    + "\"modifyDate\" = coalesce(cast(C.\"modifyDate\" as text),'') and \n"
                    + "\"provCode\"=" + provCode + " and \n"
                    + "\"recordId\"=cast(C.\"healthId\" as text))";
        }*/
        if(tableName.equals("death")) {
            sql += " " + " and not exists(select \"recordId\" from  data_sync_management where "
                    + "\"tableName\"='" + tableName + "' and \n"
                    + "\"modifyDate\" = coalesce(cast(C.\"modifyDate\" as text),'') and \n"
                    + "\"provCode\"=" + provCode + " and \n"
                    + "\"recordId\"=cast(C.\"healthId\" as text))";
        }
        if(tableName.equals("adolescent")) {
            sql += " " + " and not exists(select \"recordId\" from  data_sync_management where "
                    + "\"tableName\"='" + tableName + "' and \n"
                    + "\"modifyDate\" = coalesce(cast(C.\"modifyDate\" as text),'') and \n"
                    + "\"provCode\"=" + provCode + " and \n"
                    + "\"recordId\"=cast(C.\"healthId\" as text)|| cast(C.\"providerId\" as text)|| cast(C.\"visitDate\" as text))";
        }

        if(tableName.equals("adolescentProblem")) {
            sql += " " + " and not exists(select \"recordId\" from  data_sync_management where "
                    + "\"tableName\"='" + tableName + "' and \n"
                    + "\"modifyDate\" = coalesce(cast(C.\"modifyDate\" as text),'') and \n"
                    + "\"provCode\"=" + provCode + " and \n"
                    + "\"recordId\"=cast(C.\"healthId\" as text)|| cast(C.\"providerId\" as text)|| cast(C.\"visitDate\" as text)|| cast(C.\"problemCode\" as text))";
        }
        if(tableName.equals("under5Child")) {
            sql += " " + " and not exists(select \"recordId\" from  data_sync_management where "
                    + "\"tableName\"='" + tableName + "' and \n"
                    + "\"modifyDate\" = coalesce(cast(C.\"modifyDate\" as text),'') and \n"
                    + "\"provCode\"=" + provCode + " and \n"
                    + "\"recordId\"=cast(C.\"healthId\" as text)|| cast(C.\"providerId\" as text)|| cast(C.\"visitDate\" as text))";
        }
        if(tableName.equals("under5ChildProblem")) {
            sql += " " + " and not exists(select \"recordId\" from  data_sync_management where "
                    + "\"tableName\"='" + tableName + "' and \n"
                    + "\"modifyDate\" = coalesce(cast(C.\"modifyDate\" as text),'') and \n"
                    + "\"provCode\"=" + provCode + " and \n"
                    + "\"recordId\"=cast(C.\"healthId\" as text)|| cast(C.\"providerId\" as text)|| cast(C.\"visitDate\" as text)|| cast(C.\"problemCode\" as text))";
        }
        if(tableName.equals("ses")) {
            sql += " " + " and not exists(select \"recordId\" from  data_sync_management where "
                    + "\"tableName\"='" + tableName + "' and \n"
                    + "\"modifyDate\" = coalesce(cast(C.\"modifyDate\" as text),'') and \n"
                    + "\"provCode\"=" + provCode + " and \n"
                    + "\"recordId\"=cast(C.\"dist\" as text)|| cast(C.\"upz\" as text)|| cast(C.\"un\" as text)|| cast(C.\"mouza\" as text)|| cast(C.\"vill\" as text)|| cast(C.\"hhNo\" as text))";
        }
        if(tableName.equals("Household")) {
            sql += " " + " and not exists(select \"recordId\" from  data_sync_management where "
                    + "\"tableName\"='" + tableName + "' and \n"
                    + "\"modifyDate\" = coalesce(cast(C.\"modifyDate\" as text),'') and \n"
                    + "\"provCode\"=" + provCode + " and \n"
                    + "\"recordId\"=cast(C.\"Dist\" as text)|| cast(C.\"Upz\" as text)|| cast(C.\"UN\" as text)|| cast(C.\"Mouza\" as text)|| cast(C.\"Vill\" as text)|| cast(C.\"HHNo\" as text))";
        }
        if(tableName.equals("Member")) {
            sql += " " + " and not exists(select \"recordId\" from  data_sync_management where "
                    + "\"tableName\"='" + tableName + "' and \n"
                    + "\"modifyDate\" = coalesce(cast(C.\"modifyDate\" as text),'') and \n"
                    + "\"provCode\"=" + provCode + " and \n"
                    + "\"recordId\"=cast(C.\"Dist\" as text)|| cast(C.\"Upz\" as text)|| cast(C.\"UN\" as text)|| cast(C.\"Mouza\" as text)|| cast(C.\"Vill\" as text)|| cast(C.\"HHNo\" as text)|| cast(C.\"SNo\" as text))";
        }
        if(tableName.equals("Visits")) {
            sql += " " + " and not exists(select \"recordId\" from  data_sync_management where "
                    + "\"tableName\"='" + tableName + "' and \n"
                    + "\"modifyDate\" = coalesce(cast(C.\"modifyDate\" as text),'') and \n"
                    + "\"provCode\"=" + provCode + " and \n"
                    + "\"recordId\"=cast(C.\"Dist\" as text)|| cast(C.\"Upz\" as text)|| cast(C.\"UN\" as text)|| cast(C.\"Mouza\" as text)|| cast(C.\"Vill\" as text)|| cast(C.\"HHNo\" as text)|| cast(C.\"VDate\" as text))";
        }

        return sql;
    }



}