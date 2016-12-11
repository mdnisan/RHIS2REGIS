package com.data.rhis2;


import android.content.Context;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import Common.Connection;

/**
 * Created by angsuman on 9/27/2015.
 */
public class ClientMap {

    private String generatedId;
    private String healthId;
    private String providerId;
    private String name;
    private String DoB;
    private String age;
    private String gender;
    private String husbandName;
    private String fatherName;
    private String motherName;
    private String divisionId;
    private String zillaId;
    private String upazilaId;
    private String unionId;
    private String mouzaId;
    private String villageId;
    private String subBlock;
    private String houseGRHoldingNo;
    private String mobileNo;
    private String systemEntryDate;
    private String modifyDate;
    private String Upload;

    public String getGeneratedId() {
        return generatedId;
    }

    public void setGeneratedId(String generatedId) {
        this.generatedId = generatedId;
    }

    public String getHealthId() {
        return healthId;
    }

    public void setHealthId(String healthId) {
        this.healthId = healthId;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDoB() {
        return DoB;
    }

    public void setDoB(String DoB) {
        this.DoB = DoB;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHusbandName() {
        return husbandName;
    }

    public void setHusbandName(String husbandName) {
        this.husbandName = husbandName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(String divisionId) {
        this.divisionId = divisionId;
    }

    public String getZillaId() {
        return zillaId;
    }

    public void setZillaId(String zillaId) {
        this.zillaId = zillaId;
    }

    public String getUpazilaId() {
        return upazilaId;
    }

    public void setUpazilaId(String upazilaId) {
        this.upazilaId = upazilaId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getMouzaId() {
        return mouzaId;
    }

    public void setMouzaId(String mouzaId) {
        this.mouzaId = mouzaId;
    }

    public String getVillageId() {
        return villageId;
    }

    public void setVillageId(String villageId) {
        this.villageId = villageId;
    }

    public String getSubBlock() {
        return subBlock;
    }

    public void setSubBlock(String SubBlock) {
        this.subBlock = SubBlock;
    }

    public String getHouseGRHoldingNo() {
        return houseGRHoldingNo;
    }

    public void setHouseGRHoldingNo(String houseGRHoldingNo) {
        this.houseGRHoldingNo = houseGRHoldingNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getSystemEntryDate() {
        return systemEntryDate;
    }

    public void setSystemEntryDate(String systemEntryDate) {
        this.systemEntryDate = systemEntryDate;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }


    public String getUpload() {
        return Upload;
    }

    public void setUpload(String Upload) {
        this.Upload = Upload;
    }

    //“client’s name(eng) + client’s father name(eng)+ district code + upazila code + union code + mouza code + village code”
    public static String generateHash(String txt) {
        String generatedId = "";
        long generatedId1 = 0;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(txt.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            if (sb.toString().length() > 14) {
                generatedId1 = Long.parseLong(sb.toString().substring(0, 14), 16);
            } else {
                generatedId1 = Long.parseLong(sb.toString(), 16);
            }

            if (String.valueOf(generatedId1).length() > 14) {
                generatedId = String.valueOf(generatedId1).substring(0, 14);
            } else {
                generatedId = String.valueOf(generatedId1);
            }

            //generatedId1 = Long.valueOf(generatedId);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedId;
    }

    /*public static String generateHash(String txt){
        String generatedId = "";
        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(txt.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedId = String.valueOf(Long.parseLong(sb.toString().substring(0, 14),16)).substring(0, 14);
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedId;
    }*/
    public void Save(ClientMap cMap, Context con) {
        //client’s name(eng) + client’s father name(eng)+ district code + upazila code + union code + mouza code + village code”

        // String id = cMap.getName() + cMap.getFatherName() +cMap.getZillaId()+cMap.getUpazilaId()+cMap.getUnionId()+cMap.getMouzaId()+cMap.getVillageId();

        String SQL = "Insert Into clientMap (healthid,generatedId, providerId, name, dob, age,gender, husbandName, fatherName, motherName,divisionId, zillaId, upazilaId, unionId, mouzaId, villageId, epiBlock, houseGRHoldingNo, mobileNo, systemEntryDate,upload)" +
                "VALUES ('" + cMap.getGeneratedId() + "','" + cMap.getGeneratedId() + "','" + cMap.getProviderId() + "', '" + cMap.getName() + "','" + cMap.getDoB() + "','" + cMap.getAge() + "', '" + cMap.getGender() + "'"
                + ", '" + cMap.getHusbandName() + "'"
                + ", '" + cMap.getFatherName() + "'"
                + ", '" + cMap.getMotherName() + "'"
                + ", '" + cMap.getDivisionId() + "'"
                + ", '" + cMap.getZillaId() + "'"
                + ", '" + cMap.getUpazilaId() + "'"
                + ", '" + cMap.getUnionId() + "'"
                + ", '" + cMap.getMouzaId() + "'"
                + ", '" + cMap.getVillageId() + "'"
                + ", '" + cMap.getSubBlock() + "'"
                + ", '" + cMap.getHouseGRHoldingNo() + "'"
                + ", '" + cMap.getMobileNo() + "'"
                + ", '" + cMap.getSystemEntryDate() + "'"
                + ", '" + cMap.getUpload() + "')";


        Connection C = new Connection(con);
        C.Save(SQL);
        //generateHash(id);

    }

    public void SaveFromMember(ClientMap cMap, Context con) {
        //client’s name(eng) + client’s father name(eng)+ district code + upazila code + union code + mouza code + village code”

        // String id = cMap.getName() + cMap.getFatherName() +cMap.getZillaId()+cMap.getUpazilaId()+cMap.getUnionId()+cMap.getMouzaId()+cMap.getVillageId();

        String SQL = "Insert Into clientMap (healthid,generatedId, providerId, name, dob, age,gender, husbandName, fatherName, motherName,divisionId, zillaId, upazilaId, unionId, mouzaId, villageId, houseGRHoldingNo, mobileNo, systemEntryDate,upload)" +
                "VALUES ('" + cMap.getHealthId() + "','" + cMap.getGeneratedId() + "','" + cMap.getProviderId() + "', '" + cMap.getName() + "','" + cMap.getDoB() + "','" + cMap.getAge() + "', '" + cMap.getGender() + "'"
                + ", '" + cMap.getHusbandName() + "'"
                + ", '" + cMap.getFatherName() + "'"
                + ", '" + cMap.getMotherName() + "'"
                + ", '" + cMap.getDivisionId() + "'"
                + ", '" + cMap.getZillaId() + "'"
                + ", '" + cMap.getUpazilaId() + "'"
                + ", '" + cMap.getUnionId() + "'"
                + ", '" + cMap.getMouzaId() + "'"
                + ", '" + cMap.getVillageId() + "'"
                + ", '" + cMap.getHouseGRHoldingNo() + "'"
                + ", '" + cMap.getMobileNo() + "'"
                + ", '" + cMap.getSystemEntryDate() + "'"
                + ", '" + cMap.getUpload() + "')";


        Connection C = new Connection(con);
        C.Save(SQL);

        //generateHash(id);

    }
}
