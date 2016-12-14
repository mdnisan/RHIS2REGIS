package com.data.rhis2;

import android.content.Context;
import android.database.Cursor;

import Common.Connection;
import Common.Global;

/**
 * Created by TanvirHossain on 29/10/2015.
 */
public class ELCOProfile {
    private long _HealthId;


    public void setHealthId(long HealthId) {
        this._HealthId = HealthId;
    }

    private String _dist;

    public String getDistrict() {
        return _dist;
    }

    private String _upazila;

    public String getUpazila() {
        return _upazila;
    }

    private String _unions;

    public String getUnions() {
        return _unions;
    }

    private String _mouza;

    public String getMouza() {
        return _mouza;
    }

    private String _village;

    public String getVillage() {
        return _village;
    }

    private String _provType;

    public String getProvType() {
        return _provType;
    }

    private String _provCode;

    public String getProvCode() {
        return _provCode;
    }

    private String _hhNo;

    public String getHhNo() {
        return _hhNo;
    }

    private String _sno;

    public String getSNo() {
        return _sno;
    }

    private String _name;

    public String getName() {
        return _name;
    }

    private int _age;

    public int getAge() {
        return _age;
    }

    private String _sex;

    public String getSex() {
        return _sex;
    }

    private String _husbandName;

    public String getHusbandName() {
        return _husbandName;
    }

    private int _husbandage;

    public int getHusbandAge() {
        return _husbandage;
    }

    private String _elcono;

    public String getELCONo() {
        return _elcono;
    }


    public void ELCOProfile(Context con, String HealthId) {
        try {
            //ELCOProfile e=new ELCOProfile();
            Connection C = new Connection(con);
            String SQL = "";

            SQL = "Select Dist, Upz, UN, Mouza, Vill, ProvType, ProvCode,HHNo, SNo, NameEng,Cast(((julianday(date('now'))-julianday(DOB))/365) as int) as Age,Cast(((julianday(date('now'))-julianday(DOB))/30.4) as int) as AgeM,SPNO1 from Member";
            SQL += " Where cast(HealthId as longint)=" + HealthId + "";

            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                _dist = (cur.getString(cur.getColumnIndex("Dist")));
                _upazila = (cur.getString(cur.getColumnIndex("Upz")));
                _unions = (cur.getString(cur.getColumnIndex("UN")));
                _mouza = (cur.getString(cur.getColumnIndex("Mouza")));
                _village = (cur.getString(cur.getColumnIndex("Vill")));
                _provType = (cur.getString(cur.getColumnIndex("ProvType")));
                _provCode = (cur.getString(cur.getColumnIndex("ProvCode")));
                _hhNo = (cur.getString(cur.getColumnIndex("HHNo")));
                _sno = (cur.getString(cur.getColumnIndex("SNo")));
                _name = cur.getString(cur.getColumnIndex("NameEng")).toString();
                _age = Integer.parseInt(cur.getString(cur.getColumnIndex("Age")));
                String _SPNO1 = cur.getString(cur.getColumnIndex("SPNO1")).toString();

                //Husband
                SQL = "Select NameEng||','||Cast(((julianday(date('now'))-julianday(DOB))/365) as int) as Age from Member";
                SQL += " Where dist='" + _dist + "' and upz='" + _upazila + "' and un='" + _unions + "' and mouza='" + _mouza + "' and vill='" + _village + "' and provtype='" + _provType + "' and provcode='" + _provCode + "' and hhno='" + _hhNo + "' and sno='" + _SPNO1 + "'";
                String H = C.ReturnSingleValue(SQL).toString();
                if (H.length() > 0) {
                    String[] H1 = H.split(",");
                    _husbandName = H1[0];
                    _husbandage = Integer.parseInt(H1[1]);
                } else {
                    _husbandName = "";
                    _husbandage = 0;
                }

                SQL = "select E.ELCONo from ELCO E Where E.healthId='" + HealthId + "'";
                _elcono = C.ReturnSingleValue(SQL);

                cur.moveToNext();
            }

            cur.close();
        } catch (Exception e) {
        }
    }

    //Pregnancy Information
    //----------------------------------------------------------------------------------------------
    private String _lmp;

    public String getLMP() {
        return _lmp;
    }

    private String _edd;

    public String getEDD() {
        return _edd;
    }

    private String _gravida = "0";

    public String getGravida() {
        return _gravida;
    }

    private String _lastchildage;

    public String getLastChildAge() {
        return _lastchildage;
    }

    private String _son = "0";

    public String getSon() {
        return _elcono;
    }

    private String _daughter = "0";

    public String getDaughter() {
        return _daughter;
    }

    public String getTotalLiveChild() {
        return String.valueOf(Integer.valueOf(_son) + Integer.valueOf(_daughter));
    }

    public String CurrentPregNumber(Context con, String HealthId) {
        Connection C = new Connection(con);
        String SQL = "select (ifnull(max(pregNo),0)+1)pregno from pregWomen where cast(healthid as longint)=" + HealthId + "";
        return C.ReturnSingleValue(SQL);
    }

    public String CurrentPregNumberFromAncService(Context con, String HealthId) {
        Connection C = new Connection(con);
        String SQL = "select (ifnull(max(pregNo),0)+1)pregno from ancService where cast(healthid as longint)=" + HealthId + "";
        return C.ReturnSingleValue(SQL);
    }

    public String LastPregNumberFromDelivary(Context con, String HealthId) {
        Connection C = new Connection(con);
        String SQL = "select ifnull(max(pregNo),0)pregno from delivery where cast(healthid as longint)=" + HealthId + "";
        return C.ReturnSingleValue(SQL);
    }

    public String LastDelivaryDateFromDelivary(Context con, String HealthId) {
        Connection C = new Connection(con);
        String SQL = "select date(outcomeDate) as LastoutcomeDate  from delivery where cast(healthid as longint)=" + HealthId + " and pregNo=(select ifnull(max(pregNo),0)pregno from delivery)";
        return C.ReturnSingleValue(SQL);
    }

    public String LastPregNumber(Context con, String HealthId) {
        Connection C = new Connection(con);
        String SQL = "select ifnull(max(pregNo),0)pregno from pregWomen where cast(healthid as longint)=" + HealthId + "";
        return C.ReturnSingleValue(SQL);
    }

    Connection C;
    Global g;

    private boolean IsUserHA(Context con, String HealthId) {
        Connection C = new Connection(con);
        String ProvType = "Select ProvType from Member Where cast(HealthId as longint)=" + HealthId + "";
        String PType = C.ReturnSingleValue(ProvType);
        if (PType.equalsIgnoreCase("2"))
            return true;
        else
            return false;
    }

    public void PregnancyInfo(Context con, String HealthId, String PregNo) {
        Connection C = new Connection(con);
        String SQL = "";
        SQL = "select LMP,EDD,gravida,lastChildAge, pregNo AS pregNo, riskHistory, riskHistoryNote from PregWomen";
        SQL += " Where cast(healthid as longint)=" + HealthId + " and pregNo = " + PregNo + "";

        Cursor cur = C.ReadData(SQL);
        cur.moveToFirst();
        while (!cur.isAfterLast()) {

            _lmp = Global.DateConvertDMY(cur.getString(cur.getColumnIndex("LMP")));
            _edd = Global.DateConvertDMY(cur.getString(cur.getColumnIndex("EDD")));
            _gravida = cur.getString(cur.getColumnIndex("gravida"));
            _lastchildage = cur.getString(cur.getColumnIndex("lastChildAge"));

            //String ProvType = "Select ProvType from Member Where cast(HealthId as longint)=" + HealthId + "";
            //String PType =C.ReturnSingleValue(ProvType);

            String ELCONo = "Select ifnull( ELCONo, '' ) AS ELCONo from ELCO Where cast(HealthId as longint)=" + HealthId + "";
            String ELCONoStatus = C.ReturnSingleValue(ELCONo);
            String ProvCode = "Select ifnull( providerId, '' ) AS providerId from ELCO Where cast(HealthId as longint)=" + HealthId + "";

            String ELCOProvCode = C.ReturnSingleValue(ProvCode);
            String ProvType = "Select ifnull( ProvType, '' ) AS ProvType from ProviderDB Where ProvCode=" + ELCOProvCode + "";
            // g.setProvType(C.ReturnSingleValue(ProvType));
            if (!ELCONoStatus.equalsIgnoreCase("null") && !ELCONoStatus.equalsIgnoreCase("")) {

                if (ProvType.equalsIgnoreCase("2")) {

                } else {
                    SQL = "select (ELCONo||','||son||','||dau)txt from ELCO E Where cast(e.healthid as longint)=" + HealthId + "";
                    String[] EL = C.ReturnSingleValue(SQL).split(",");
                    _elcono = EL[0];
                    _son = EL[1];
                    _daughter = EL[2];
                }
            }

            cur.moveToNext();
        }

        cur.close();
    }

    public void PregnancyInfo(Context con, String HealthId, String PregNo, String ProvType) {
        Connection C = new Connection(con);
        String SQL = "";
        SQL = "select LMP,EDD,gravida,lastChildAge, pregNo AS pregNo, riskHistory, riskHistoryNote from PregWomen";
        SQL += " Where cast(healthid as longint)=" + HealthId + " and pregNo = " + PregNo + "";

        Cursor cur = C.ReadData(SQL);
        cur.moveToFirst();
        while (!cur.isAfterLast()) {

            _lmp = Global.DateConvertDMY(cur.getString(cur.getColumnIndex("LMP")));
            _edd = Global.DateConvertDMY(cur.getString(cur.getColumnIndex("EDD")));
            _gravida = cur.getString(cur.getColumnIndex("gravida"));
            _lastchildage = cur.getString(cur.getColumnIndex("lastChildAge"));

            //String ProvType = "Select ProvType from Member Where cast(HealthId as longint)=" + HealthId + "";
            //String PType =C.ReturnSingleValue(ProvType);

            if (ProvType.equalsIgnoreCase("2")) {

            } else {
                SQL = "select (ELCONo||','||son||','||dau)txt from ELCO E Where cast(e.healthid as longint)=" + HealthId + "";
                String[] EL = C.ReturnSingleValue(SQL).split(",");
                _elcono = EL[0];
                _son = EL[1];
                _daughter = EL[2];
            }


            cur.moveToNext();
        }

        cur.close();
    }
}