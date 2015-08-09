package Common;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.widget.ArrayAdapter;

import java.io.File;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

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

    public Connection(Context context) {
        super(context, DBLocation, null, DATABASE_VERSION);
        dbContext=context;

        try
        {
            //New DatabaseStructure
            //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
            String SQL = "";

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

           /* SQL = "Create Table PregWomen(Dist Varchar(2), Upz Varchar(2), UN Varchar(2), Mouza Varchar(3), Vill Varchar(2),ProvType Varchar(2),ProvCode Varchar(5),HHNo Varchar(5), SNo Varchar(2),PGN Varchar(2),DOLMP Varchar(10),DOEDD Varchar(10),PrePregTimes Varchar(1),LCAge Varchar(2),RegDT Varchar(10), EnDt Varchar(20), Upload Varchar(1),UploadDT Varchar(20))";
            CreateTable("PregWomen",SQL);*/

            SQL =  "CREATE TABLE PregWomen (healthId BIGINT, pregNo INT, providerId INT, houseGRHoldingNo VARCHAR(30),mobileNo INT,LMP DATE,tempLMP DATE,EDD DATE,para INT,gravida INT,lastChildAge INT,height INT,bloodGroup VARCHAR( 5 ),riskHistory INT,riskHistoryNote  VARCHAR(50),StartTime VARCHAR( 20 ),EndTime VARCHAR( 20 ),lat VARCHAR( 30 ),lon VARCHAR( 30 ),systemEntryDate DATE, Upload VARCHAR( 1 ),UploadDT DATE, modifyDate DATE,PRIMARY KEY(healthId, pregNo));";
            CreateTable("PregWomen",SQL);


        }
        catch(Exception ex)
        {

        }
    }

    // Creating our initial tables
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
                android.R.layout.simple_spinner_item, dataList);

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
    public String DownloadData(String SQLStr, String TableName,String ColumnList, String UniqueField)
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
    }


    // Data Update
    //----------------------------------------------------------------------------------------------
    public String DataUpdate(String SQLStr, String TableName,String ColumnList, String UniqueField)
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
    }


    //Execute command on Database Server
    //----------------------------------------------------------------------------------------------
    public String ExecuteCommandOnServer(String SQLStr)
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
    }

    // Data Upload to Database Server
    //----------------------------------------------------------------------------------------------
    public String UploadData(String[] DataArray,String TableName,String ColumnList,String UniqueFields)
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
        cur_H = ReadData("Select "+ VariableList +" from "+ TableName);

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


    //Generate table in local database
    public void DatabaseTableSync(String TableName) {
        String SQLStr = "";
        DownloadData d = new DownloadData();
        d.Method_Name = "DownloadData";
        d.SQLStr = "Select TableName, TableScript from DatabaseTab where TableName='"+ TableName +"'";

        String DataArray[] = null;

        try {
            DataArray = d.execute("").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < DataArray.length; i++) {
            String VarData[] = split(DataArray[i], '^');
            CreateTable(VarData[0], VarData[1]);
        }
    }


    //Sync data between local and server
    public void DatabaseTableDataSync(String[] TableList) {
        String response = "";
        String TableName = "";
        String VariableList ="";
        String UniqueField = "";
        String[] V;

        for(int i=0; i< TableList.length;i++) {
            if(TableList[i].toLowerCase().equals("household"))
                UniqueField = "Div, Dist, Upz, UN, Mouza, Vill,ProvType,ProvCode, HHNo";
            else if(TableList[i].toLowerCase().equals("member"))
                UniqueField = "Dist, Upz, UN, Mouza, Vill, ProvType,ProvCode, HHNo, SNo";
            else if(TableList[i].toLowerCase().equals("visits"))
                UniqueField = "Dist, Upz, UN, Mouza, Vill,ProvType,ProvCode, HHNo, VDate";

            VariableList = GetColumnList(TableName);
            V = GenerateArrayList(VariableList, TableName);
            response = UploadData(V, TableName, VariableList, UniqueField);
        }
    }

    //Rebuild Local Database from Server
    //----------------------------------------------------------------------------------------------
    public void RebuildDatabase(String Dist, String Upz, String UN, String ProvType, String ProvCode) {
        String SQLStr= "";
        DownloadData d = new DownloadData();
        d.Method_Name = "DownloadData";
        d.SQLStr = "Select TableName, TableScript from DatabaseTab";

        String DataArray[] = null;

        //Build Database
        try {
            DataArray = d.execute("").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < DataArray.length; i++) {
            String VarData[] = split(DataArray[i],'^');
            CreateTable(VarData[0], VarData[1]);
        }

        //------------------------------------------------------------------------------------------
        //Data Sync: Download data from server
        //------------------------------------------------------------------------------------------
        String Res = "";

        try {
            //Service Provider
            SQLStr = "Select Divid, zillaid, upazilaid, unionid, ProvType, ProvCode, ProvName, EnDate, ExDate, Active, DeviceSetting from ProviderDB Where ";
            SQLStr += " ZillaId='"+ Dist +"' and";
            SQLStr += " Upazilaid='"+ Upz +"' and";
            SQLStr += " Unionid='"+ UN +"' and";
            SQLStr += " ProvType='"+ ProvType +"' and";
            SQLStr += " ProvCode='"+ ProvCode +"' and";
            SQLStr += " Active='1'";
            Res = DownloadData(SQLStr ,"ProviderDB","Divid, zillaid, upazilaid, unionid, ProvType, ProvCode, ProvName, EnDate, ExDate, Active, DeviceSetting","Divid,ZillaId,UpazilaId,UnionId,ProvType,ProvCode");

            //Service Provider Area
            SQLStr = "Select a.Divid, a.zillaid, a.upazilaid, a.unionid, a.mouzaid, a.villageid, a.FWAUnit, a.Ward, a.WardNew, a.Block";
            SQLStr += " from Village v";
            SQLStr += " inner join ProviderArea a on v.ZILLAID+v.UPAZILAID+v.UNIONID+v.MOUZAID+v.VILLAGEID=a.zillaid+a.upazilaid+a.unionid+a.mouzaid+a.villageid";
            SQLStr += " inner join ProviderAssignArea aa on a.ZILLAID+a.UPAZILAID+a.UNIONID+a.FWAUnit=aa.zillaid+aa.upazilaid+aa.unionid+aa.areacode";
            SQLStr += " Where a.ZillaId='"+ Dist +"' and";
            SQLStr += " a.Upazilaid='"+ Upz +"' and";
            SQLStr += " a.Unionid='"+ UN +"' and";
            SQLStr += " aa.ProvType='"+ ProvType +"' and";
            SQLStr += " aa.ProvCode='"+ ProvCode +"'";
            Res = DownloadData(SQLStr ,"ProviderArea","Divid, zillaid, upazilaid, unionid, mouzaid, villageid, FWAUnit, Ward, WardNew, Block","Divid, zillaid, upazilaid, unionid, mouzaid, villageid");

            //Service Provider (Assign Area)
            SQLStr = "Select Divid, zillaid, upazilaid, unionid, ProvType, ProvCode, AreaCode, EnDate, Active from ProviderAssignArea Where ";
            SQLStr += " ZillaId='"+ Dist +"' and";
            SQLStr += " Upazilaid='"+ Upz +"' and";
            SQLStr += " Unionid='"+ UN +"' and";
            SQLStr += " ProvType='"+ ProvType +"' and";
            SQLStr += " ProvCode='"+ ProvCode +"' and";
            SQLStr += " Active='1'";
            Res = DownloadData(SQLStr ,"ProviderAssignArea","Divid, zillaid, upazilaid, unionid, ProvType, ProvCode, AreaCode, EnDate, Active","Divid,ZillaId,UpazilaId,UnionId,ProvType,ProvCode,AreaCode");

            //DeviceNo
            Save("Delete from DeviceNo");
            Save("Insert into DeviceNo(DeviceNo)Values('"+ (ProvType+ProvCode) +"')");

            //Login
            SQLStr = "Select ProvCode,ProvName,''Pass from ProviderDB Where ";
            SQLStr += " ZillaId='"+ Dist +"' and";
            SQLStr += " Upazilaid='"+ Upz +"' and";
            SQLStr += " Unionid='"+ UN +"' and";
            SQLStr += " ProvType='"+ ProvType +"' and";
            SQLStr += " ProvCode='"+ ProvCode +"' and Active='1'";
            Res = DownloadData(SQLStr ,"Login","UserId,UserName,Pass","UserId");

            //District
            SQLStr = "Select DIVID, ZILLAID, ZILLANAMEENG, ZILLANAME from Zilla";
            Res = DownloadData(SQLStr ,"Zilla","DIVID, ZILLAID, ZILLANAMEENG, ZILLANAME","ZillaID");

            //Upazila
            SQLStr = "Select ZILLAID, UPAZILAID, UPAZILANAMEENG, UPAZILANAME from Upazila where ZillaID='"+ Dist +"' and Upazilaid='"+ Upz +"'";
            Res = DownloadData(SQLStr ,"Upazila","ZILLAID, UPAZILAID, UPAZILANAMEENG, UPAZILANAME","ZillaID,UPAZILAID");

            //Unions
            SQLStr = "Select ZILLAID, UPAZILAID, MUNICIPALITYID, UNIONID, UNIONNAMEENG, UNIONNAME from Unions where ZillaID='"+ Dist +"' and Upazilaid='"+ Upz +"' and Unionid='"+ UN +"'";
            Res = DownloadData(SQLStr ,"Unions","ZILLAID, UPAZILAID, MUNICIPALITYID, UNIONID, UNIONNAMEENG, UNIONNAME","ZillaID,UPAZILAID,UnionId");

            //Mouza
            SQLStr  = "Select m.ZILLAID, m.UPAZILAID, m.MUNICIPALITYID, m.UNIONID, m.MOUZAID, m.RMO, m.MOUZANAMEENG, m.MOUZANAME from Mouza m";
            SQLStr += " inner join ProviderArea a on m.ZILLAID+m.UPAZILAID+m.UNIONID+m.MOUZAID=a.zillaid+a.upazilaid+a.unionid+a.mouzaid";
            SQLStr += " inner join ProviderAssignArea aa on a.ZILLAID+a.UPAZILAID+a.UNIONID+a.areacode=aa.zillaid+aa.upazilaid+aa.unionid+aa.areacode";

            /*if(ProvType.equals("03") | ProvType.equals("01")) //03-FWA,01-Data Collector
                SQLStr += " inner join ProviderAssignArea aa on a.ZILLAID+a.UPAZILAID+a.UNIONID+a.fwaunit=aa.zillaid+aa.upazilaid+aa.unionid+aa.areacode";
            else if(ProvType.equals("02"))//HA
                SQLStr += " inner join ProviderAssignArea aa on a.ZILLAID+a.UPAZILAID+a.UNIONID+a.ward=aa.zillaid+aa.upazilaid+aa.unionid+aa.areacode";
            */

            SQLStr += " where aa.provtype='"+ ProvType +"' and aa.provcode='"+ ProvCode +"' and m.ZillaID='"+ Dist +"' and m.Upazilaid='"+ Upz +"' and m.Unionid='"+ UN +"'";
            Res = DownloadData(SQLStr ,"Mouza","ZILLAID, UPAZILAID, MUNICIPALITYID, UNIONID, MOUZAID, RMO, MOUZANAMEENG, MOUZANAME","ZillaID,UPAZILAID,UnionId,MOUZAID");

            //Village
            SQLStr  = "Select v.ZILLAID, v.UPAZILAID, v.UNIONID, v.MOUZAID, v.VILLAGEID, v.RMO, v.VILLAGENAMEENG, v.VILLAGENAME, isnull(v.CRRVILLAGENAME,'')CRRVILLAGENAME from Village v";
            SQLStr += " inner join ProviderArea a on v.ZILLAID+v.UPAZILAID+v.UNIONID+v.MOUZAID+v.VILLAGEID=a.zillaid+a.upazilaid+a.unionid+a.mouzaid+a.villageid";
            SQLStr += " inner join ProviderAssignArea aa on a.ZILLAID+a.UPAZILAID+a.UNIONID+a.areacode=aa.zillaid+aa.upazilaid+aa.unionid+aa.areacode";

            /*if(ProvType.equals("01") | ProvType.equals("03")) //03-FWA, 01-Data Collector
                SQLStr += " inner join ProviderAssignArea aa on a.ZILLAID+a.UPAZILAID+a.UNIONID+a.FWAUnit=aa.zillaid+aa.upazilaid+aa.unionid+aa.areacode";
            else if(ProvType.equals("02")) //HA
                SQLStr += " inner join ProviderAssignArea aa on a.ZILLAID+a.UPAZILAID+a.UNIONID+a.ward=aa.zillaid+aa.upazilaid+aa.unionid+aa.areacode";
            */
            SQLStr += " where aa.provtype='"+ ProvType +"' and aa.provcode='"+ ProvCode +"' and v.ZillaID='"+ Dist +"' and v.Upazilaid='"+ Upz +"' and v.Unionid='"+ UN +"'";
            Res = DownloadData(SQLStr ,"Village","ZILLAID, UPAZILAID, UNIONID, MOUZAID, VILLAGEID, RMO, VILLAGENAMEENG, VILLAGENAME, CRRVILLAGENAME","ZillaID,UPAZILAID,UnionId,MOUZAID,VillageID");

            //FWA Unit
            SQLStr = "select UCode,UName,UNameBan from FWAUnit";
            Res = DownloadData(SQLStr ,"FWAUnit","UCode,UName,UNameBan","UCode");

            //Block
            SQLStr = "select BCode,BName,BNameBan from HABlock";
            Res = DownloadData(SQLStr ,"HABlock","BCode,BName,BNameBan","BCode");

            //Update Device Setting Status in Server DB
            SQLStr  = "Update ProviderDB Set DeviceSetting='2' Where ";
            SQLStr += " ZillaId='"+ Dist +"' and";
            SQLStr += " Upazilaid='"+ Upz +"' and";
            SQLStr += " Unionid='"+ UN +"' and";
            SQLStr += " ProvType='"+ ProvType +"' and";
            SQLStr += " ProvCode='"+ ProvCode +"' and";
            SQLStr += " Active='1'";
            ExecuteCommandOnServer(SQLStr);

            //Household data
            SQLStr = "Select h.Div, h.Dist, h.Upz, h.UN, h.wardNew, h.wardOld, h.Mouza, h.FWAUnit, h.Vill, h.EPIBlock, h.PAddr, h.PermaAddress, h.ProvType, h.ProvCode, h.HHNo, h.Religion, h.VGFCard, h.StartTime, h.EndTime, h.Lat, h.Lon, h.UserId, h.EnDt, '1' Upload";
            SQLStr += " from Village v";
            SQLStr += " inner join ProviderArea a on v.ZILLAID+v.UPAZILAID+v.UNIONID+v.MOUZAID+v.VILLAGEID=a.zillaid+a.upazilaid+a.unionid+a.mouzaid+a.villageid";
            SQLStr += " inner join ProviderAssignArea aa on a.ZILLAID+a.UPAZILAID+a.UNIONID+a.areacode=aa.zillaid+aa.upazilaid+aa.unionid+aa.areacode";
            SQLStr += " inner join Household h on a.zillaid+a.upazilaid+a.unionid+a.mouzaid+a.villageid=h.dist+h.upz+h.un+h.Mouza+h.Vill";
            SQLStr += " where aa.provtype='"+ ProvType +"' and aa.provcode='"+ ProvCode +"' and v.ZillaID='"+ Dist +"' and v.Upazilaid='"+ Upz +"' and v.Unionid='"+ UN +"'";
            Res = DownloadData(SQLStr,"Household","Div, Dist, Upz, UN, wardNew, wardOld, Mouza, FWAUnit, Vill, EPIBlock, PAddr, PermaAddress, ProvType, ProvCode, HHNo, Religion, VGFCard, StartTime, EndTime, Lat, Lon, UserId, EnDt, Upload","Dist, Upz, UN, Mouza, Vill, ProvType, ProvCode, HHNo");

            //Member Data
            SQLStr = " Select h.Dist, h.Upz, h.UN, h.Mouza, h.Vill, h.ProvType, h.ProvCode, h.HHNo, h.SNo, h.HealthID, h.NameEng, h.NameBang, h.Rth, h.HaveNID, h.NID, h.NIDStatus, h.HaveBR,";
            SQLStr += " h.BRID, h.BRIDStatus, h.MobileNo1, h.MobileNo2, h.DOB, h.Age, h.DOBSource, h.BPlace, h.FNo, h.Father, h.FDontKnow, h.MNo, h.Mother, h.MDontKnow, h.Sex, h.MS, h.SPNO1,";
            SQLStr += " h.SPNO2, h.SPNO3, h.SPNO4, h.ELCONo, h.ELCODontKnow, h.EDU, h.Rel, h.Nationality, h.OCP, h.StartTime, h.EnType, h.EnDate, h.ExType, h.ExDate, h.EndTime, h.Lat, h.Lon, h.UserId, h.EnDt, '1' Upload";
            SQLStr += " from Village v";
            SQLStr += " inner join ProviderArea a on v.ZILLAID+v.UPAZILAID+v.UNIONID+v.MOUZAID+v.VILLAGEID=a.zillaid+a.upazilaid+a.unionid+a.mouzaid+a.villageid";
            SQLStr += " inner join ProviderAssignArea aa on a.ZILLAID+a.UPAZILAID+a.UNIONID+a.areacode=aa.zillaid+aa.upazilaid+aa.unionid+aa.areacode";
            SQLStr += " inner join Member h on a.zillaid+a.upazilaid+a.unionid+a.mouzaid+a.villageid=h.dist+h.upz+h.un+h.Mouza+h.Vill";
            SQLStr += " where aa.provtype='"+ ProvType +"' and aa.provcode='"+ ProvCode +"' and v.ZillaID='"+ Dist +"' and v.Upazilaid='"+ Upz +"' and v.Unionid='"+ UN +"'";
            Res = DownloadData(SQLStr,"Member","Dist, Upz, UN, Mouza, Vill, ProvType, ProvCode, HHNo, SNo, HealthID, NameEng, NameBang, Rth, HaveNID, NID, NIDStatus, HaveBR, BRID, BRIDStatus, MobileNo1, MobileNo2, DOB, Age, DOBSource, BPlace, FNo, Father, FDontKnow, MNo, Mother, MDontKnow, Sex, MS, SPNO1, SPNO2, SPNO3, SPNO4, ELCONo, ELCODontKnow, EDU, Rel, Nationality, OCP, StartTime, EnType, EnDate, ExType, ExDate, EndTime, Lat, Lon, UserId, EnDt, Upload","Dist, Upz, UN, Mouza, Vill, ProvType, ProvCode, HHNo, SNo");

            //Visits Data
            SQLStr = " Select Div, Dist, Upz, UN, Mouza, Vill, h.ProvType, h.ProvCode, HHNo, VDate, VStatus, StartTime, EndTime, Lat, Lon, UserId, EnDt,'1' Upload";
            SQLStr = " from Village v";
            SQLStr = " inner join ProviderArea a on v.ZILLAID+v.UPAZILAID+v.UNIONID+v.MOUZAID+v.VILLAGEID=a.zillaid+a.upazilaid+a.unionid+a.mouzaid+a.villageid";
            SQLStr = " inner join ProviderAssignArea aa on a.ZILLAID+a.UPAZILAID+a.UNIONID+a.areacode=aa.zillaid+aa.upazilaid+aa.unionid+aa.areacode";
            SQLStr = " inner join Visits h on a.zillaid+a.upazilaid+a.unionid+a.mouzaid+a.villageid=h.dist+h.upz+h.un+h.Mouza+h.Vill";
            SQLStr += " where aa.provtype='"+ ProvType +"' and aa.provcode='"+ ProvCode +"' and v.ZillaID='"+ Dist +"' and v.Upazilaid='"+ Upz +"' and v.Unionid='"+ UN +"'";
            Res = DownloadData(SQLStr,"Visits","Div, Dist, Upz, UN, Mouza, Vill, ProvType, ProvCode, HHNo, VDate, VStatus, StartTime, EndTime, Lat, Lon, UserId, EnDt, Upload","Dist, Upz, UN, Mouza, Vill,ProvType,ProvCode, HHNo, VDate");

            //Download Health ID
            //Service Provider
            //SQLStr = "sp_HealthIDDownload '"+ Dist +"','"+ Upz +"','"+ UN +"','"+ ProvType +"','"+ ProvCode +"'";
            //Res = DownloadData(SQLStr ,"HealthIDRepository","ZillaID, UpazilaID, UnionID, ProvType, ProvCode, AreaCode, HealthID,Status","HealthID");



        } catch (Exception e) {
            e.printStackTrace();
        }
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
    private String[] TableListServer()
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
    }

    public void TableStructureSync()
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


/*
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
*/
    }

    private String[] DownloadArrayList(String SQL)
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
    }


    //Download Health ID from the Central Database
    //----------------------------------------------------------------------------------------------
    public void DownloadHealthID(String Dist, String Upz, String UN, String ProvType, String ProvCode) {
        String SQLStr= "";
        String Res = "";

        //ID Sync
        try {
            //Service Provider
            SQLStr = "sp_HealthIDDownload '"+ Dist +"','"+ Upz +"','"+ UN +"','"+ ProvType +"','"+ ProvCode +"'";
            Res = DownloadData(SQLStr ,"HealthIDRepository","ZillaID, UpazilaID, UnionID, ProvType, ProvCode, AreaCode, HealthID,Status","HealthID");

            SQLStr = "Update ProviderDB set HealthIDRequest='2' where  Zillaid='"+ Dist +"' and UpazilaID='"+ Upz +"' and UnionID='"+ UN +"' and ProvType='"+ ProvType +"' and ProvCode='"+ ProvCode +"'";
            ExecuteCommandOnServer(SQLStr);

        } catch (Exception ex) {

        }
    }



    /*
    // Insert record into the database
    public void addTodoItem(TodoItem item) {
        // Open database connection
        SQLiteDatabase db = this.getWritableDatabase();
        // Define values for each field
        ContentValues values = new ContentValues();
        values.put(KEY_BODY, item.getBody());
        values.put(KEY_PRIORITY, item.getPriority());
        // Insert Row
        db.insertOrThrow(TABLE_TODO, null, values);
        db.close(); // Closing database connection
    }


    // Returns a single todo item by id
    public TodoItem getTodoItem(int id) {
        // Open database for reading
        SQLiteDatabase db = this.getReadableDatabase();
        // Construct and execute query
        Cursor cursor = db.query(TABLE_TODO,  // TABLE
                new String[] { KEY_ID, KEY_BODY, KEY_PRIORITY }, // SELECT
                KEY_ID + "= ?", new String[] { String.valueOf(id) },  // WHERE, ARGS
                null, null, "id ASC", "100"); // GROUP BY, HAVING, ORDER BY, LIMIT
        if (cursor != null)
            cursor.moveToFirst();
        // Load result into model object
        TodoItem item = new TodoItem(cursor.getString(1), cursor.getInt(2));
        item.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
        // return todo item
        return item;
    }

    public List<TodoItem> getAllTodoItems() {
        List<TodoItem> todoItems = new ArrayList<TodoItem>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TODO;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TodoItem item = new TodoItem(cursor.getString(1), cursor.getInt(2));
                item.setId(cursor.getInt(0));
                // Adding todo item to list
                todoItems.add(item);
            } while (cursor.moveToNext());
        }

        // return todo list
        return todoItems;
    }

    public int getTodoItemCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TODO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // return count
        return cursor.getCount();
    }

    public int updateTodoItem(TodoItem item) {
        // Open database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Setup fields to update
        ContentValues values = new ContentValues();
        values.put(KEY_BODY, item.getBody());
        values.put(KEY_PRIORITY, item.getPriority());
        // Updating row
        int result = db.update(TABLE_TODO, values, KEY_ID + " = ?",
                new String[] { String.valueOf(item.getId()) });
        // Close the database
        db.close();
        return result;
    }

    public void deleteTodoItem(TodoItem item) {
        // Open database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete the record with the specified id
        db.delete(TABLE_TODO, KEY_ID + " = ?",
                new String[] { String.valueOf(item.getId()) });
        // Close the database
        db.close();
    }
    */
}