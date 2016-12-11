package com.data.rhis2;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import Common.Connection;
import Common.Global;

/**
 * Created by angsuman on 4/12/2016.
 */
public class Villagepopup extends Activity {

    Connection C;
    Global g;
    boolean networkAvailable = false;
    ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
    SimpleAdapter mSchedule;
    private static Context localContext;
    private Context conxt;
    //DownloadProvider objDownloadFile;
    private ProgressDialog progress;
    /*LinearLayout secUnit;
    LinearLayout secWord;
    Spinner  spnProvider;*/
    public static Context getLocalContext() {
        return localContext;
    }

    public static void setLocalContext(Context localContext) {
        Villagepopup.localContext = localContext;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            C = new Connection(this);
            g = Global.getInstance();
            conxt = this;
            setContentView(R.layout.villagepopup);
            setFinishOnTouchOutside(false);
           /* secUnit = (LinearLayout) findViewById(R.id.secUnit);
            secWord = (LinearLayout) findViewById(R.id.secWord);
            spnProvider= (Spinner) findViewById(R.id.spnProvider);*/
            Villagepopup.setLocalContext(getApplicationContext());
            FillProvider();

        } catch (Exception exp) {

        }
    }

    Options op = new Options();
    String message = "";
    int jumpTime = 0;


    // ((EditText) findViewById(R.id.txtHealthIDSearch)).
    public void LookForCheckBox(ViewGroup parent, boolean setChecked, String lookup) {
        for (int i = parent.getChildCount() - 1; i >= 0; i--) {
            final View child = parent.getChildAt(i);
            if (child instanceof ViewGroup) {
                LookForCheckBox((ViewGroup) child, setChecked, lookup);
                // DO SOMETHING WITH VIEWGROUP, AFTER CHILDREN HAS BEEN LOOPED
            } else {
                if (child != null) {
                    for (int x = 0; x < parent.getChildCount(); x++) {
                        View c = parent.getChildAt(i);
                        if (child instanceof CheckBox) {
                            if (((CheckBox) child).getText().toString().toLowerCase().contains(lookup.toLowerCase()))
                                //((CheckBox) c).setChecked(true);
                                //  if(((CheckBox) child).getText().toString().toLowerCase().contains(""))
                                ((CheckBox) child).setBackgroundColor(Color.BLUE);
                            else
                                ((CheckBox) child).setBackgroundColor(Color.WHITE);

                        }
                    }
                }
            }
        }
    }

    private void DisplayHouseholdDetails(List<String> listItem, List<String> listItem1, final String Mouza, final String village, final String ProvCode, final String Dist, final String Upz, final String Union, final String ProvType) {
      /*
      final ProgressDialog progDailog = ProgressDialog.show(
                Villagepopup.this, "", "অপেক্ষা করুন ...", true);
        new Thread() {
            public void run() {

                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progDailog.dismiss();


                        }
                    });
                } catch (Exception e) {

                }
            }
        }.start();
        */

        final Dialog dialog = new Dialog(Villagepopup.this);
        dialog.setTitle("Household");
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.frmmultiplechoice);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);

        final LinearLayout checkBoxHolder = (LinearLayout) dialog.findViewById(R.id.checkBoxHolder);
        checkBoxHolder.removeAllViews();
        LinearLayout.LayoutParams lnlParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams layoutParamForcheck = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        DisplayMetrics dm;
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        final ArrayList<Integer> checkedOptions = new ArrayList<Integer>();

       /* String sql = "SELECT\n" +
                "                ifnull( HealthID, '' ) AS HealthID,\n" +
                "                ifnull( NameEng, '' ) AS NameEng, hidDistributed \n" +
                "\n" +
                "                FROM Member M where \n" +
                "                M.dist='" + g.getDistrict() + "' and M.upz='" + g.getUpazila() +
                "' and M.un='" + g.getUnion() + "' and M.Mouza='" + g.getMouza() + "' and M.vill='" +
                g.getVillage() + "' and M.HHNo='" + g.getHouseholdNo() + "'";


               Cursor cur = C.ReadData(sql);


        cur.moveToFirst();
        int index_of_hid = 0;
        op = new Options();
        while (!cur.isAfterLast()) {

            op.codeList.add(index_of_hid);
            op.HealthidList.add(cur.getString(cur.getColumnIndex("HealthID")));
            op.capEngList.add(cur.getString(cur.getColumnIndex("NameEng")));
            cur.moveToNext();
            index_of_hid = index_of_hid + 1;
        }

        cur.close();
        */


        int index_of_hid = 0;
        op = new Options();
        for (String temp : listItem) {

            op.codeList.add(index_of_hid);
            op.HouseholdidList.add(temp);


            index_of_hid = index_of_hid + 1;
        }
        for (String temp1 : listItem1) {

            //  op.codeList.add(index_of_hid);
            op.capEngList.add(temp1);


            //  index_of_hid = index_of_hid + 1;
        }
        //cur.close();

        Collections.reverse(op.codeList);
        Collections.reverse(op.capEngList);
        Collections.reverse(op.HouseholdidList);

        for (int i = 0; i < op.codeList.size(); i++) {
            checkedOptions.add(-1);
        }

        for (int i = 0; i < op.codeList.size(); i++) {
            final LinearLayout ln = new LinearLayout(this);
            final CheckBox checkButton = new CheckBox(this);
            ln.setOrientation(LinearLayout.HORIZONTAL);
            // ln.setShowDividers(LinearLayout.HORIZONTAL);
            ln.setPadding(0, 12, 0, 0);
            checkButton.setText(op.HouseholdidList.get(i) + "   " + op.capEngList.get(i));
            //checkButton.setText(op.HouseholdidList.get(i));
            checkButton.setId(op.codeList.get(i));
            ln.setId(i);
            ln.addView(checkButton, 0, layoutParamForcheck);
            checkBoxHolder.addView(ln, 0, lnlParams);

            checkButton
                    .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        public void onCheckedChanged(CompoundButton buttonView,
                                                     boolean isChecked) {
                            // TODO Auto-generated method stub
                            if (isChecked) {
                                checkedOptions.set(ln.getId(), 1);
                            } else {
                                checkedOptions.set(ln.getId(), -1);

                            }
                        }
                    });


            String result = C.ReturnSingleValue("SELECT HHNo FROM Household  WHERE  HHNo = '" + op.HouseholdidList.get(i) +
                    "' AND MOUZA = '" + Mouza + "' and VILL = '" + village + "' and Dist = '" + Dist +
                    "'  and UPz = '" + Upz + "' and UN = '" + Union + "'");
            //String rs1=C.ReturnSingleValue(sql);
            if (result.length() > 0) {
                checkButton.setChecked(true);
            } else {
                checkButton.setChecked(false);
            }

        }


        ((EditText) dialog.findViewById(R.id.txtHealthIDSearch)).addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!s.equals("")) {


                    try {

                        // SearchHouseholdList(false, ((EditText) findViewById(R.id.txtNameSearch)).getText().toString());
                        LookForCheckBox((LinearLayout) dialog.findViewById(R.id.checkBoxHolder), true, s.toString());
                    } catch (Exception e) {

                    }

                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });

        ((CheckBox) dialog.findViewById(R.id.chkall)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    LookForCheckBox1(checkBoxHolder, true);
                    //((CheckBox)dialog.findViewById(R.id.chkall)).setText("Un select all");
                } else {
                    LookForCheckBox1(checkBoxHolder, false);
                    //((CheckBox)dialog.findViewById(R.id.chkall)).setText("Select all");
                }
            }
        });

        Button saveNxtButton = (Button) dialog.findViewById(R.id.buttonSave);


        saveNxtButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {


                ///////////////////////////////////////////////////////////////
                progress = new ProgressDialog(conxt);
                progress.setMessage("Downloading");
                progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progress.setIcon(R.drawable.rhislogo2);
                progress.setIndeterminate(false);
                progress.setCancelable(false);
                progress.setProgress(0);

                progress.show();

                final int totalProgressTime = 100;
                final Thread t = new Thread() {
                    @Override
                    public void run() {


                        try {
                            jumpTime = 0;
                            message = "Downloading Households";
                            progressHandler.sendMessage(progressHandler.obtainMessage());

                            String incaluse = "";
                            for (int i = 0; i < op.codeList.size(); i++) {


                                // String sql ="";
                                if (checkedOptions.get(i) == 1) {
                                    incaluse += String.valueOf(op.HouseholdidList.get(i)) + ",";


                                }


                            }

                            //Household data
                            String SQLStr = "Select h.\"Dist\", h.\"Upz\", h.\"UN\", h.\"Mouza\", h.\"Vill\", h.\"PAddr\", h.\"PermaAddress\", h.\"ProvType\", h.\"ProvCode\", h.\"HHNo\", h.\"Religion\", h.\"VGFCard\",";
                            SQLStr += " h.\"subBlock\",h.\"unit\", h.\"StartTime\", h.\"EndTime\", h.\"Lat\", h.\"Lon\", h.\"UserId\", h.\"EnDt\", '1' Upload";
                            SQLStr += " from \"Village\" v";
                            SQLStr += " inner join \"ProviderArea\" a on v.\"ZILLAID\"=a.zillaid and v.\"UPAZILAID\"=a.upazilaid and v.\"UNIONID\"=a.unionid and v.\"MOUZAID\"=a.mouzaid and v.\"VILLAGEID\"=a.villageid";
                            SQLStr += " inner join \"Household\" h on a.zillaid=h.\"Dist\" and a.upazilaid=h.\"Upz\" and a.unionid=h.\"UN\" and a.mouzaid=h.\"Mouza\" and a.villageid=h.\"Vill\"";
                            SQLStr += " and v.\"MOUZAID\" = '" + Mouza + "'\n" +
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


                            C.DownloadJSON(SQLStr, "Household", "Dist, Upz, UN, Mouza, Vill, PAddr, PermaAddress, ProvType, ProvCode, HHNo, Religion, VGFCard,subBlock,unit, StartTime, EndTime, Lat, Lon, UserId, EnDt, Upload", "Dist, Upz, UN, Mouza, Vill, HHNo");

                            sleep(2);
                            jumpTime += 20;
                            message = "Downloading Socio Economic Data";
                            progressHandler.sendMessage(progressHandler.obtainMessage());
                            //ses
                            SQLStr = "Select C.*\n" +
                                    "from \"Village\" v\n" +
                                    "INNER JOIN \"ProviderArea\" a on v.\"ZILLAID\" = a.zillaid\n" +
                                    "\tand v.\"UPAZILAID\" = a.upazilaid\n" +
                                    "\tand v.\"UNIONID\" = a.unionid\n" +
                                    "\tand v.\"MOUZAID\" = a.mouzaid\n" +
                                    "\tand v.\"VILLAGEID\" = a.villageid\n" +
                                    "INNER JOIN \"Household\" h on a.zillaid = h.\"Dist\"\n" +
                                    "\tand a.upazilaid = h.\"Upz\"\n" +
                                    "\tand a.unionid = h.\"UN\"\n" +
                                    "\tand a.mouzaid = h.\"Mouza\"\n" +
                                    "\tand a.villageid = h.\"Vill\"\n" +
                                    "\tand v.\"MOUZAID\"= '" + Mouza + "'\n" +
                                    "\tand v.\"VILLAGEID\" = '" + village + "'\n" +
                                    "\tand a.\"provType\" = '" + ProvType + "'\n" +
                                    "\tand a.\"provCode\" = '" + ProvCode + "'\n" +
                                    "\tand v.\"ZILLAID\"= '" + Dist + "'\n" +
                                    "\tand v.\"UPAZILAID\" = '" + Upz + "'\n" +
                                    "\tand v.\"UNIONID\"= '" + Union + "'\n" +
                                    // "\tand C.\"hhNo\" IN('" + incaluse.substring(0,incaluse.length()-1)+ "')'\n"+
                                    "INNER JOIN \"ses\" C on a.zillaid = CAST(C.\"dist\" as Integer)\n" +
                                    "\tand a.upazilaid = CAST(C.\"upz\" as Integer)\n" +
                                    "\tand a.unionid = CAST(C.\"un\" as Integer)\n" +
                                    "\tand a.mouzaid = CAST(C.\"mouza\" as Integer)\n" +
                                    "\tand a.villageid = CAST(C.\"vill\" as Integer)\n" +
                                    "\t and v.\"MOUZAID\" = '" + Mouza + "'\n" +
                                    "\tand v.\"VILLAGEID\" = '" + village + "'\n" +
                                    "\tand a.\"provType\"= '" + ProvType + "'\n" +
                                    "\tand a.\"provCode\" = '" + ProvCode + "'\n" +
                                    "\tand v.\"ZILLAID\" = '" + Dist + "'\n" +
                                    "\tand v.\"UPAZILAID\" = '" + Upz + "'\n" +
                                    "\tand v.\"UNIONID\" = '" + Union + "'";
                            SQLStr += " where a.\"provType\" = '" + ProvType + "'\n" +
                                    "\tand a.\"provCode\" = '" + ProvCode + "'\n" +
                                    "\tand v.\"ZILLAID\" = '" + Dist + "'\n" +
                                    "\tand v.\"UPAZILAID\" = '" + Upz + "'\n" +
                                    "\tand v.\"UNIONID\" = '" + Union + "' and C.\"hhNo\" IN(" + incaluse.substring(0, incaluse.length() - 1) + ")";


                            C.DownloadJSON(SQLStr, "ses", "dist, upz, un, mouza, vill, provType,provCode,hhNo,status,q1,q11,q2 ,q21, q3a,q3b,q3c,q3d, q3e, q3f,q3g,q3h, q3i, q3j, q3k, q3l, q3m, q3n, q3o, q3p, q4, q41, q5, q51, q6, q61, q7, q71, q8a, q8b, q8c, q8d, q8e, Q9, Q10, startTime, endTime, userId, enDt, upload", "dist, upz, un, mouza, vill, hhNo");
                            sleep(2);
                            jumpTime += 30;
                            message = "Downloading Household Members";
                            progressHandler.sendMessage(progressHandler.obtainMessage());

                            //Member Data
                            SQLStr = " Select h.\"Dist\", h.\"Upz\", h.\"UN\", h.\"Mouza\", h.\"Vill\", h.\"ProvType\", h.\"ProvCode\", h.\"HHNo\", h.\"SNo\", h.\"HealthID\", h.\"NameEng\", h.\"NameBang\", h.\"Rth\", h.\"HaveNID\", h.\"NID\", h.\"NIDStatus\", h.\"HaveBR\",";
                            SQLStr += " h.\"BRID\", h.\"BRIDStatus\", h.\"MobileNo1\", h.\"MobileNo2\",h.mobileyn, h.\"DOB\", h.\"Age\", h.\"DOBSource\", h.\"BPlace\", h.\"FNo\", h.\"Father\", h.\"FDontKnow\", h.\"MNo\", h.\"Mother\", h.\"MDontKnow\", h.\"Sex\", h.\"MS\", h.\"SPNO1\",";
                            SQLStr += " h.\"SPNO2\", h.\"SPNO3\", h.\"SPNO4\", h.\"ELCONo\", h.\"ELCODontKnow\", h.\"EDU\", h.\"Rel\", h.\"Nationality\", h.\"OCP\", h.\"StartTime\", h.\"EnType\", h.\"EnDate\", coalesce(h.\"ExType\", '')  AS \"ExType\", h.\"ExDate\", h.\"EndTime\", h.\"Lat\", h.\"Lon\", h.\"UserId\", h.\"EnDt\" , h.\"hidDistributed\"\n" +
                                    "\t,h.\"hidDistributionDate\", '1' Upload";
                            SQLStr += " from \"Village\" v";
                            SQLStr += " inner join \"ProviderArea\" a on v.\"ZILLAID\"=a.zillaid and v.\"UPAZILAID\"=a.upazilaid and v.\"UNIONID\"=a.unionid and v.\"MOUZAID\"=a.mouzaid and v.\"VILLAGEID\"=a.villageid";
                            SQLStr += " inner join \"Member\" h on a.zillaid=h.\"Dist\" and a.upazilaid=h.\"Upz\" and a.unionid=h.\"UN\" and a.mouzaid=h.\"Mouza\" and a.villageid=h.\"Vill\"";
                            //SQLStr +=" and h.HHNo IN('\" + incaluse.substring(0,incaluse.length()-1)+ \"')";
                            // SQLStr += " where a.\"provType\"='" + ProvType + "' and a.\"provCode\"='" + ProvCode + "' and v.\"ZILLAID\"='" + Dist + "' and v.\"UPAZILAID\"='" + Upz + "' and v.\"UNIONID\"='" + Union + "'";
                            SQLStr += " where v.\"MOUZAID\" = '" + Mouza + "'\n" +
                                    "\tand v.\"VILLAGEID\" = '" + village + "'\n" +
                                    "\tand a.\"provType\"= '" + ProvType + "'\n" +
                                    "\tand a.\"provCode\" = '" + ProvCode + "'\n" +
                                    "\tand v.\"ZILLAID\" = '" + Dist + "'\n" +
                                    "\tand v.\"UPAZILAID\" = '" + Upz + "'\n" +

                                    "\tand v.\"UNIONID\" = '" + Union + "' and h.\"HHNo\" IN(" + incaluse.substring(0, incaluse.length() - 1) + ")";


                            C.DownloadJSON(SQLStr, "Member", "Dist, Upz, UN, Mouza, Vill, ProvType, ProvCode, HHNo, SNo, HealthID, NameEng, NameBang, Rth, HaveNID, NID, NIDStatus, HaveBR, BRID, BRIDStatus, MobileNo1, MobileNo2,MobileYN, DOB, Age, DOBSource, BPlace, FNo, Father, FDontKnow, MNo, Mother, MDontKnow, Sex, MS, SPNO1, SPNO2, SPNO3, SPNO4, ELCONo, ELCODontKnow, EDU, Rel, Nationality, OCP, StartTime, EnType, EnDate, ExType, ExDate, EndTime, Lat, Lon, UserId, EnDt, hidDistributed, hidDistributionDate, Upload", "Dist, Upz, UN, Mouza, Vill, HHNo, SNo");
                            sleep(2);
                            jumpTime += 25;
                            message = "Downloading Visits of Provider";
                            progressHandler.sendMessage(progressHandler.obtainMessage());

                            //Visits Data
                            SQLStr = " Senllect dist, upz, un, mouza, vill, h.\"provType\", h.\"provCode\", \"hhNo\", \"vDate\", \"vStatus\", \"startTime\", \"endTime\", \"lat\", \"lon\", \"userId\", \"enDt\",'1' Upload";
                            SQLStr += " from \"Village\" v";
                            SQLStr += " inner join \"ProviderArea\" a on v.\"ZILLAID\"=a.zillaid and v.\"UPAZILAID\"=a.upazilaid and v.\"UNIONID\"=a.unionid and v.\"MOUZAID\"=a.mouzaid and v.\"VILLAGEID\"=a.villageid";
                            SQLStr += " inner join \"visits\" h on a.zillaid=h.dist and a.upazilaid=h.upz and a.unionid=h.un and a.mouzaid=h.mouza and a.villageid=h.vill";
                            SQLStr += " where v.\"MOUZAID\" = '" + Mouza + "'\n" +
                                    "\tand v.\"VILLAGEID\" = '" + village + "'\n" +
                                    "\tand a.\"provType\" = '" + ProvType + "'\n" +
                                    "\tand a.\"provCode\" = '" + ProvCode + "'\n" +
                                    "\tand v.\"ZILLAID\" = '" + Dist + "'\n" +
                                    "\tand v.\"UPAZILAID\" = '" + Upz + "'\n" +
                                    "\tand v.\"UNIONID\" = '" + Union + "' and h.\"hhNo\" IN(" + incaluse.substring(0, incaluse.length() - 1) + ")";


                            C.DownloadJSON(SQLStr, "Visits", "Dist, Upz, UN, Mouza, Vill, ProvType, ProvCode, HHNo, VDate, VStatus, StartTime, EndTime, Lat, Lon, UserId, EnDt, Upload", "Dist, Upz, UN, Mouza, Vill, HHNo, VDate");


                            jumpTime += 25;
                            message = "Download complete. Please wait 5 seconds";
                            progressHandler.sendMessage(progressHandler.obtainMessage());


                            //Visits Data
                            DownloadServiceTables(ProvCode, incaluse.substring(0, incaluse.length() - 1), Mouza, village,
                                    Dist, Upz, Union);
                            jumpTime += 25;
                            message = "Download complete. Please wait 5 seconds";
                            progressHandler.sendMessage(progressHandler.obtainMessage());

                            //FWAWWORKPlAINT
                            DownloadFWAWWORKPlAINTables(ProvCode);
                            jumpTime += 25;
                            message = "Download complete. Please wait 5 seconds";
                            progressHandler.sendMessage(progressHandler.obtainMessage());
                            /// Add to SQL
                          /*  String SQL = "";

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
                            message = "Creating HouseHold Head";

                            */
                            progressHandler.sendMessage(progressHandler.obtainMessage());
                            sleep(100);
                            progress.dismiss();

                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        //}
                    }
                };
                t.start();
            }

            Handler progressHandler = new Handler() {
                public void handleMessage(Message msg) {
                    progress.incrementProgressBy(jumpTime);
                    progress.setMessage(message);
                }
            };

        });

        Button closeButton = (Button) dialog.findViewById(R.id.btnClose);

        closeButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                C.GenerateHHHTotalMem();
                C.GenerateElco();

                dialog.dismiss();
            }

        });

        dialog.show();
    }

    public void LookForCheckBox1(ViewGroup parent, boolean setChecked) {
        for (int i = parent.getChildCount() - 1; i >= 0; i--) {
            final View child = parent.getChildAt(i);
            if (child instanceof ViewGroup) {
                LookForCheckBox1((ViewGroup) child, setChecked);
                // DO SOMETHING WITH VIEWGROUP, AFTER CHILDREN HAS BEEN LOOPED
            } else {
                if (child != null) {
                    for (int x = 0; x < parent.getChildCount(); x++) {
                        View c = parent.getChildAt(i);
                        if (child instanceof CheckBox) {
                            if (setChecked)
                                ((CheckBox) c).setChecked(true);
                            else
                                ((CheckBox) c).setChecked(false);
                        }
                    }
                }
            }
        }
    }

    /* SQLStr = "SELECT * FROM \"fpaWorkPlanMaster\" where ";
     SQLStr += " \"providerId\"='" + Global.Left(spnProvider.getSelectedItem().toString(),5) + "'";
     Res = C.DownloadJSON(SQLStr, "fpaWorkPlanMaster", "workPlanId,workAreaId,providerId,month,status,systemEntryDate,modifyDate,upload", "workPlanId, workAreaId, providerId");

     SQLStr = "SELECT * FROM \"fpaWorkPlanDetail\" where ";
     SQLStr += " \"providerId\"='" + Global.Left(spnProvider.getSelectedItem().toString(),5) + "'";
     Res = C.DownloadJSON(SQLStr, "fpaWorkPlanDetail", "workPlanId,fpaItem,workPlanDate,unitNo,village,elcoFrom,elcoTo,leaveType,providerId,systemEntryDate,modifyDate,otherDec,remarks,upload", "workPlanId, fpaItem, workPlanDate, providerId");

 */
    public void DownloadFWAWWORKPlAINTables(String provCode) {

        //workPlanMaster
        String VariableList = "";
        String sql = "select  \"workPlanId\", \"workAreaId\", \"providerId\", \"month\", \"status\", \"systemEntryDate\", \"modifyDate\", \"upload\"\n" +
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

        sql = "select  \"workPlanId\", \"item\", \"workPlanDate\", \"unitNo\", \"village\", \"elcoFrom\", \"elcoTo\", \"ipcUN\",\"ipcWord\",\"ipcMouza\",\"ipcVill\",\"ipcPara\", \"ipcBariFrom\",\"ipcBariTo\",\"epiproviderId\",\"epischedulerId\",\"ccWard\",\"ccID\",\"natProgramType\",\"fpiOtherMeeting\",\"leaveType\", \"providerId\", \"systemEntryDate\", \"modifyDate\", \"otherDec\", \"remarks\", 1 as upload,status\n" +
                "from \"workPlanDetail\" \n" +
                "where status<>'2' and status<>'3' and \"providerId\" =" + provCode + "";

        //fpiMonitoring


        sql = "select  \"vDate\", \"fpaCode\", \"fpaUnit\", \"fpaVill\", \"fpaAdvance\", \"needItems1\", \"needItems2\",\"needItems3\",\"needItems4\",\"needItems5\",\"needItems6\",\"needItems7\",\"needItems8\",\"startTime\",\"endTime\",\"userId\",\"enDt\", \"upload\"\n" +
                "from \"fpiMonitoring\" \n" +
                "where \"fpaCode\" =" + provCode + "";

        VariableList = "vDate,fpaCode,fpaUnit,fpaVill,fpaAdvance,needItems1,needItems2,needItems3,needItems4,needItems5,needItems6,needItems7,needItems8,startTime,endTime,userId,enDt,upload";
        C.DownloadJSON(sql, "fpiMonitoring", VariableList, "vDate,fpaCode");

        //HouseholdFPI


        sql="\"Div\",\"Dist\",\"Upz\",\"UN\",\"Mouza\",\"Vill\",\"ProvType\",\"ProvCode\",\"HHNo\",\"houseHoldStatus\",\"causeOfHouseHoldStatus\",\"subBlockStatus\",\"pAddrStatus\",\"permaAddressStatus\",\"religionStatus\",\"StartTime\",\"EndTime\",\"Lat\",\"Lon\",\"UserId\",\"EnDt\",\"Upload\",\"hidDistributed\",\"hidDistributionDate\"\n"+
          "from \"HouseholdFPI\" \n" +
           "where \"UserId\" =" + provCode + "";

        VariableList = "Div,Dist,Upz,UN,Mouza,Vill,ProvType,ProvCode,HHNo,houseHoldStatus,causeOfHouseHoldStatus,subBlockStatus,pAddrStatus,permaAddressStatus,religionStatus,StartTime,EndTime,Lat,Lon,UserId,EnDt,Upload,hidDistributed,hidDistributionDate";
        C.DownloadJSON(sql, "HouseholdFPI", VariableList, "Div,Dist,Upz,UN,Mouza,Vill,HHNo");
        //memberfpi
        sql="\"dist\",\"upz\",\"un\",\"mouza\",\"vill\",\"provtype\",\"provcode\",\"hhno\",\"sno\",\"healthid\",\"nameengstatus\",\"rthstatus\",\"havenidstatus\",\"nidstatus\",\"havebrstatus\",\"bridstatus\",\"mobileno1status\",\"mobileno2status\",\"dobstatus\",\"agestatus\",\"dobsourcestatus\",\"bplacestatus\"\t,\"fnostatus\",\"fatherstatus\",\"mnostatus\",\"motherstatus\",\"sexstatus\",\"msstatus\",\"spno1status\",\"spno2status\",\"spno3status\",\"spno4status\",\"edustatus\",\"relstatus\",\"nationalitystatus\",\"ocpstatus\",\"starttime\",\"entype\",\"endate\",\"extype\",\"exdate\",\"endtime\",\"lat\",\"lon\",\"userid\",\"endt\",\"upload\"\n"+
                "from \"memberfpi\" \n" +
                "where \"userid\" =" + provCode + "";

        VariableList = "dist,upz,un,mouza,vill,provtype,provcode,hhno,sno,healthid,nameengstatus,rthstatus,havenidstatus,nidstatus,havebrstatus,bridstatus,mobileno1status,mobileno2status,dobstatus,agestatus,dobsourcestatus,bplacestatus,fnostatus,fatherstatus,mnostatus,motherstatus,sexstatus,msstatus,spno1status,spno2status,spno3status,spno4status,edustatus,relstatus,nationalitystatus,ocpstatus,starttime,entype,endate,extype,exdate,endtime,lat,lon,userid,endt,upload";
        C.DownloadJSON(sql, "memberfpi", VariableList, "dist,upz,un,mouza,vill,hhno,sno");
//sesfpi

        sql="\"dist\",\"upz\",\"un\",\"mouza\",\"vill\",\"provtype\",\"provcode\",\"hhno\",\"sesstatus\",\"q1status\",\"q11status\",\"q2status\",\"q21status\",\"q3astatus\",\"q3bstatus\",\"q3cstatus\",\"q3dstatus\",\"q3estatus\",\"q3fstatus\",\"q3gstatus\",\"q3hstatus\",\"q3istatus\",\"q3jstatus\",\"q3kstatus\",\"q3lstatus\",\"q3mstatus\",\"q3nstatus\",\"q3ostatus\",\"q3pstatus\",\"q4status\",\"q41status\",\"q5status\",\"q51status\",\"q6status\",\"q61status\",\"q7status\",\"q71status\",\"q8astatus\",\"q8bstatus\",\"q8cstatus\",\"q8dstatus\",\"q8estatus\",\"q9status\",\"q10status\",\"q11astatus\",\"starttime\",\"endtime\",\"userid\",\"endt\",\"upload\"\n"+
                "from \"sesfpi\" \n" +
                "where \"userid\" =" + provCode + "";

        VariableList = "dist,upz,un,mouza,vill,provtype,provcode,hhno,sesstatus,q1status,q11status,q2status,q21status,q3astatus,q3bstatus,q3cstatus,q3dstatus,q3estatus,q3fstatus,q3gstatus,q3hstatus,q3istatus,q3jstatus,q3kstatus,q3lstatus,q3mstatus,q3nstatus,q3ostatus,q3pstatus,q4status,q41status,q5status,q51status,q6status,q61status,q7status,q71status,q8astatus,q8bstatus,q8cstatus,q8dstatus,q8estatus,q9status,q10status,q11astatus,starttime,endtime,userid,endt,upload";
        C.DownloadJSON(sql, "sesfpi", VariableList, "dist,upz,un,mouza,vill,hhno");
        //elcoFPI
        sql="\"healthId\",\"providerId\",\"hhStatus\",\"haHHNo\",\"elcoNo\",\"husbandName\",\"domSource\",\"marrDate\",\"marrAge\",\"son\",\"dau\",\"regDT\",\"systemEntryDate\",\"modifyDate\",\"upload\",\"husbandAge\",\"tt1\",\"tt2\",\"tt3\",\"tt4\",\"tt5\"\n"+
                "from \"elcoFPI\" \n" +
                "where \"providerId\" =" + provCode + "";

        VariableList = "healthId,providerId,hhStatus,haHHNo,elcoNo,husbandName,domSource,marrDate,marrAge,son,dau,regDT,systemEntryDate,modifyDate,upload,husbandAge,tt1,tt2,tt3,tt4,tt5";
        C.DownloadJSON(sql, "elcoFPI", VariableList, "healthId");
//elcoVisitFPI
        sql="\"healthId\",\"pregNo\",\"providerId\",\"transactionId\",\"visit\",\"vDate\",\"visitStatus\",\"currStatus\",\"newOld\",\"mDate\",\"sSource\",\"qty\",\"unit\",\"brand\",\"validity\",\"dayMonYear\",\"referPlace\",\"syrinsQty\",\"mrSource\",\"MRDate\",\"MRAge\",\"systemEntryDate\",\"modifyDate\",\"upload\"\n"+
                "from \"elcoVisitFPI\" \n" +
                "where \"providerId\" =" + provCode + "";

        VariableList = "healthId,pregNo,providerId,transactionId,visit,vDate,visitStatus,currStatus,newOld,mDate,sSource,qty,unit,brand,validity,dayMonYear,referPlace,syrinsQty,mrSource,MRDate,MRAge,systemEntryDate,modifyDate,upload";
        C.DownloadJSON(sql, "elcoVisitFPI", VariableList, "healthId");
//pregWomenFPI
        sql="\"healthId\",\"pregNo\",\"providerId\",\"LMP\",\"EDD\",\"para\",\"gravida\",\"lastChildAge\",\"riskHistoryNote\",\"pregRefer\",\"systemEntryDate\",\"upload\"\n"+
                "from \"pregWomenFPI\" \n" +
                "where \"providerId\" =" + provCode + "";
        VariableList = "healthId,pregNo,providerId,LMP,EDD,para,gravida,lastChildAge,riskHistoryNote,pregRefer,systemEntryDate,upload";
        C.DownloadJSON(sql, "pregWomenFPI", VariableList, "healthId,pregNo");
//ancServiceFPI
        sql="\"healthId\",\"pregNo\",\"serviceId\",\"providerId\",\"visitSource\",\"visitDate\",\"visitMonth\",\"ironFolStatus\",\"misoStatus\",\"systemEntryDate\",\"upload\"\n"+
                "from \"ancServiceFPI\" \n" +
                "where \"providerId\" =" + provCode + "";

        VariableList = "healthId,pregNo,serviceId,providerId,visitSource,visitDate,visitMonth,ironFolStatus,misoStatus,systemEntryDate,upload";
        C.DownloadJSON(sql, "ancServiceFPI", VariableList, "healthId,pregNo,serviceId");
//deliveryFPI
        sql="\"healthId\",\"pregNo\",\"providerId\",\"outcomePlace\",\"outcomeDate\",\"outcomeType\",\"liveBirth\",\"stillBirth\",\"abortion\",\"misoprostol\",\"attendantDesignation\",\"systemEntryDate\",\"upload\"\n"+
                "from \"deliveryFPI\" \n" +
                "where \"providerId\" =" + provCode + "";

        VariableList = "healthId,pregNo,providerId,outcomePlace,outcomeDate,outcomeType,liveBirth,stillBirth,abortion,misoprostol,attendantDesignation,systemEntryDate,upload";
        C.DownloadJSON(sql, "deliveryFPI", VariableList, "healthId,pregNo");


        //newBornFPI
        sql="\"healthId\",\"pregNo\",\"childNo\",\"providerId\",\"birthWeight\",\"immatureBirth\",\"dryingAfterBirth\",\"resassitation\",\"stimulation\",\"bagNMask\",\"chlorehexidin\",\"skinTouch\",\"breastFeed\",\"bathThreeDays\",\"systemEntryDate\",\"modifyDate\",\"upload\"\n"+
                "from \"newBornFPI\" \n" +
                "where \"providerId\" =" + provCode + "";

        VariableList = "healthId,pregNo,childNo,providerId,birthWeight,immatureBirth,dryingAfterBirth,resassitation,stimulation,bagNMask,chlorehexidin,skinTouch,breastFeed,bathThreeDays,systemEntryDate,modifyDate,upload";
        C.DownloadJSON(sql, "newBornFPI", VariableList, "healthId,pregNo,childNo");


        //pncServiceMotherFPI
        sql="\"\"healthId\",\"pregNo\",\"serviceId\",\"providerId\",\"visitSource\",\"visitDate\",\"visitMonth\",\"systemEntryDate\",\"modifyDate\",\"upload\"\n"+
                "from \"pncServiceMotherFPI\" \n" +
                "where \"providerId\" =" + provCode + "";

        VariableList = "healthId,pregNo,serviceId,providerId,visitSource,visitDate,visitMonth,systemEntryDate,modifyDate,upload";
        C.DownloadJSON(sql, "pncServiceMotherFPI", VariableList, "healthId,pregNo,serviceId");


        //pncServiceChildFPI
        sql="\"healthId\",\"pregNo\",\"childNo\",\"childHealthId\",\"serviceId\",\"providerId\",\"visitSource\",\"visitDate\",\"visitMonth\",\"systemEntryDate\",\"modifyDate\",\"upload\"\n"+
                "from \"pncServiceChildFPI\" \n" +
                "where \"providerId\" =" + provCode + "";

        VariableList = "healthId,pregNo,childNo,childHealthId,serviceId,providerId,visitSource,visitDate,visitMonth,systemEntryDate,modifyDate,upload";
        C.DownloadJSON(sql, "pncServiceChildFPI", VariableList, "healthId,pregNo,childNo,serviceId");
       // epiBariVisit
       sql="\"dist\",\"upz\",\"un\",\"mouza\",\"vill\",\"provType\",\"provCode\",\"hHNo\",\"vDate\",\"qBHHNo\",\"qBHEndDate\",\"qBPVisitEPI1\",\"qBPVisitEPI2\",\"qBPVisitEPI3\",\"qBPVisitEPI4\",\"qBPVisitEPI5\",\"qBNextDoss\",\"qB1stDoss1\",\"qB1stDoss2\",\"qB1stDoss3\",\"qB1stDoss4\",\"qB1stDoss5\",\"qBCNoSessiondossY\",\"qBCNoSessiondoss\",\"qBWNoSessiondossY\",\"qBWNoSessiondoss\",\"qBVitmA\",\"qBChildCard\",\"qBWomenCard\",\"startTime\",\"endTime\",\"userId\",\"enDt\",\"upload\"\n"+
        "from \"epiBariVisit\" \n" +
        "where \"userId\" =" + provCode + "";

        VariableList = "dist,upz,un,mouza,vill,provType,provCode,hHNo,vDate,qBHHNo,qBHEndDate,qBPVisitEPI1,qBPVisitEPI2,qBPVisitEPI3,qBPVisitEPI4,qBPVisitEPI5,qBNextDoss,qB1stDoss1,qB1stDoss2,qB1stDoss3,qB1stDoss4,qB1stDoss5,qBCNoSessiondossY,qBCNoSessiondoss,qBWNoSessiondossY,qBWNoSessiondoss,qBVitmA,qBChildCard,qBWomenCard,startTime,endTime,userId,enDt,upload";
        C.DownloadJSON(sql, "epiBariVisit", VariableList, "dist,upz,un,mouza,vill,provCode,hHNo,vDate");
        //
        sql="\"subBlockId\",\"schedulerId\",\"providerId\",\"vDate\",\"qVHA\",\"qVFWA\",\"qVN\",\"qVOth\",\"qVChReg\",\"qVWReg\",\"qVChCard\",\"qVWCard\",\"qVTalBook\",\"qVFIBook\",\"qVVac\",\"qVASerice\",\"qVMSerice\",\"qVSBox\",\"qVVatARed\",\"qVVatABlue\",\"qVIICPac\",\"qVBCG\",\"qVPenta\",\"qVPolio\",\"qVPcv\",\"qVIPV\",\"qVMR\",\"qVTT\",\"qVSbcg\",\"qVSPenta\",\"qVSPolio\",\"qVSPcv\",\"qVSIPV\",\"qVSMR\",\"qVSTT\",\"qVNToubcg\",\"qVNToupant\",\"qVNTouPolio\",\"qVNToupcv\",\"qVNTouIPV\",\"qVNTouMR\",\"qVNTouTT\",\"qVRootbcg\",\"qVNotwhy\",\"qVRootPenta\",\"qVRootMR\",\"qVRootTT\",\"qVSRemoved\",\"qVFormbcg\",\"qVFormpenta\",\"qVFormPolio\",\"qVFormpcv\",\"qVFormipv\",\"qVFormmr\",\"qVFormtt\",\"qVCardregBook\",\"qVCardVac\",\"qVTTresearched\",\"qVProtectors\",\"qVDateOfVac\",\"qVCard\",\"qVAFP\",\"qVMeasles\",\"qVNewborntetanus\",\"qVOther\",\"qVTodySession\",\"qVProblem1\",\"qVProblem2\",\"qVProblem3\",\"qVProblem4\",\"qVProblem5\",\"qVSolve1\",\"qVSolve2\",\"qVSolve3\",\"qVSolve4\",\"qVSolve5\",\"startTime\",\"endTime\",\"userId\",\"enDt\",\"upload\"\n"+
                "from \"epiSessionVisit\" \n" +
                "where \"userId\" =" + provCode + "";


        VariableList="subBlockId,schedulerId,providerId,vDate,qVHA,qVFWA,qVN,qVOth,qVChReg,qVWReg,qVChCard,qVWCard,qVTalBook,qVFIBook,qVVac,qVASerice,qVMSerice,qVSBox,qVVatARed,qVVatABlue,qVIICPac,qVBCG,qVPenta,qVPolio,qVPcv,qVIPV,qVMR,qVTT,qVSbcg,qVSPenta,qVSPolio,qVSPcv,qVSIPV,qVSMR,qVSTT,qVNToubcg,qVNToupant,qVNTouPolio,qVNToupcv,qVNTouIPV,qVNTouMR,qVNTouTT,qVRootbcg,qVNotwhy,qVRootPenta,qVRootMR,qVRootTT,qVSRemoved,qVFormbcg,qVFormpenta,qVFormPolio,qVFormpcv,qVFormipv,qVFormmr,qVFormtt,qVCardregBook,qVCardVac,qVTTresearched,qVProtectors,qVDateOfVac,qVCard,qVAFP,qVMeasles,qVNewborntetanus,qVOther,qVTodySession,qVProblem1,qVProblem2,qVProblem3,qVProblem4,qVProblem5,qVSolve1,qVSolve2,qVSolve3,qVSolve4,qVSolve5,startTime,endTime,userId,enDt,upload";
        C.DownloadJSON(sql, "epiSessionVisit", VariableList, "subBlockId,schedulerId,providerId,vDate");


    }

    public void DownloadServiceTables(String provCode, String HHinClause,
                                      String mouzaid, String villageid,
                                      String zillaid, String upazilaid, String unionid) {

        //ClientMap
        String VariableList = "";
        String sql = "select  B.\"generatedId\", B.\"name\",  B.\"age\", B.\"divisionId\", B.\"zillaId\", B.\"upazilaId\",  B.\"unionId\", B.\"mouzaId\",B.\"villageId\", B.\"houseGRHoldingNo\", B.\"mobileNo\",  B.\"systemEntryDate\", B.\"modifyDate\", B.\"providerId\",  B.\"healthId\",  B.\"gender\", B.\"fatherName\", B.\"motherName\", B.\"husbandName\", B.\"dob\", B.\"ownMobile\", B.\"epiBlock\",   '1' Upload \n" +
                "from \"ProviderArea\" pa \n" +
                "INNER JOIN \"Member\" A ON A.\"Dist\" = pa.zillaid and A.\"Upz\" = pa.upazilaid and A.\"UN\" = pa.unionid\n" +
                "and A.\"Mouza\" = pa.mouzaid and A.\"Vill\" = pa.villageid \n" +
                "INNER JOIN \"clientMap\" B ON A.\"HealthID\" = B.\"healthId\"\n" +
                "where pa.\"provCode\" =" + provCode +
                "\tand pa.\"mouzaid\" = '" + mouzaid + "'\n" +
                "\tand pa.\"villageid\" = '" + villageid + "'\n" +
                "\tand pa.\"zillaid\" = '" + zillaid + "'\n" +
                "\tand pa.\"upazilaid\" = '" + upazilaid + "'\n" +
                "\tand pa.\"unionid\" = '" + unionid + "' and A.\"HHNo\" IN(" + HHinClause + ")";


        C.DownloadJSON(sql, "clientMap", "generatedId, name,  age, divisionId, zillaId, upazilaId,  unionId, mouzaId,villageId, houseGRHoldingNo, mobileNo,  systemEntryDate, modifyDate, providerId,  healthId,  gender, fatherName, motherName, husbandName, dob, ownMobile, epiBlock,  upload", "generatedId");





        //elco

        sql = "select  C.\"healthId\", C.\"providerId\", C.\"hhStatus\", C.\"haHHNo\", C.\"elcoNo\", C.\"husbandName\", C.\"husbandAge\", C.\"domSource\", C.\"marrDate\", C.\"marrAge\", C.\"son\", C.\"dau\", C.\"regDT\", C.\"systemEntryDate\", C.\"modifyDate\", '1' upload \n" +
                "from \"ProviderArea\" pa \n" +
                "INNER JOIN \"Member\" A ON A.\"Dist\" = pa.zillaid and A.\"Upz\" = pa.upazilaid and A.\"UN\" = pa.unionid\n" +
                "and A.\"Mouza\" = pa.mouzaid and A.\"Vill\" = pa.villageid \n" +
                "INNER JOIN \"clientMap\" B ON A.\"HealthID\" = B.\"healthId\"\n" +
                "INNER JOIN \"elco\" C ON C.\"healthId\" = B.\"generatedId\"\n" +
                "where pa.\"provCode\" =" + provCode +
                "\tand pa.\"mouzaid\" = '" + mouzaid + "'\n" +
                "\tand pa.\"villageid\" = '" + villageid + "'\n" +
                "\tand pa.\"zillaid\" = '" + zillaid + "'\n" +
                "\tand pa.\"upazilaid\" = '" + upazilaid + "'\n" +
                "\tand pa.\"unionid\" = '" + unionid + "' and A.\"HHNo\" IN(" + HHinClause + ")";

        VariableList = "healthId, providerId, hhStatus, haHHNo, elcoNo, husbandName, husbandAge, domSource, marrDate, marrAge, son, dau, regDT, systemEntryDate, modifyDate, upload";
        C.DownloadJSON(sql, "elco", VariableList, "healthId");


        //elco visit
        sql = "select  C.\"healthId\",C.\"pregNo\",C.\"providerId\",C.\"transactionId\",C.\"visit\",C.\"vDate\",C.\"visitStatus\",C.\"currStatus\",C.\"newOld\",C.\"mDate\",C.\"sSource\",C.\"qty\",C.\"unit\",C.\"brand\",C.\"validity\",C.\"dayMonYear\",C.\"referPlace\",C.\"syrinsQty\",C.\"mrSource\",C.\"MRDate\",C.\"MRAge\",C.\"systemEntryDate\",C.\"modifyDate\", '1' upload \n" +
                "from \"ProviderArea\" pa \n" +
                "INNER JOIN \"Member\" A ON A.\"Dist\" = pa.zillaid and A.\"Upz\" = pa.upazilaid and A.\"UN\" = pa.unionid\n" +
                "and A.\"Mouza\" = pa.mouzaid and A.\"Vill\" = pa.villageid \n" +
                "INNER JOIN \"clientMap\" B ON A.\"HealthID\" = B.\"healthId\"\n" +
                "INNER JOIN \"elcoVisit\" C ON C.\"healthId\" = B.\"generatedId\"\n" +
                "where pa.\"provCode\" =" + provCode +
                "\tand pa.\"mouzaid\" = '" + mouzaid + "'\n" +
                "\tand pa.\"villageid\" = '" + villageid + "'\n" +
                "\tand pa.\"zillaid\" = '" + zillaid + "'\n" +
                "\tand pa.\"upazilaid\" = '" + upazilaid + "'\n" +
                "\tand pa.\"unionid\" = '" + unionid + "' and A.\"HHNo\" IN(" + HHinClause + ")";

        // HHinClause.substring(0, HHinClause.length() - 1)
        VariableList = "healthId,pregNo,providerId,transactionId,visit,vDate,visitStatus,currStatus,newOld,mDate,sSource,qty,unit,brand,validity,dayMonYear,referPlace,syrinsQty,mrSource,MRDate,MRAge,systemEntryDate,modifyDate,upload";
        C.DownloadJSON(sql, "elcoVisit", VariableList, "healthId,visit");

        //pregWomen
        sql = "select  C.\"healthId\", C.\"pregNo\", C.\"providerId\", C.\"houseGRHoldingNo\", C.\"mobileNo\",C.\"LMP\", C.\"tempLMP\", C.\"statusLMP\", C.\"EDD\", C.\"para\", C.\"gravida\", C.\"lastChildAge\", C.\"height\",C.\"bloodGroup\", C.\"riskHistory\", C.\"riskHistoryNote\", C.\"systemEntryDate\",C.\"modifyDate\",C.\"sateliteCenterName\",C.\"client\", '1' upload \n" +
                "from \"ProviderArea\" pa \n" +
                "INNER JOIN \"Member\" A ON A.\"Dist\" = pa.zillaid and A.\"Upz\" = pa.upazilaid and A.\"UN\" = pa.unionid\n" +
                "and A.\"Mouza\" = pa.mouzaid and A.\"Vill\" = pa.villageid \n" +
                "INNER JOIN \"clientMap\" B ON A.\"HealthID\" = B.\"healthId\"\n" +
                "INNER JOIN \"pregWomen\" C ON C.\"healthId\" = B.\"generatedId\"\n" +
                "where pa.\"provCode\" =" + provCode +
                "\tand pa.\"mouzaid\" = '" + mouzaid + "'\n" +
                "\tand pa.\"villageid\" = '" + villageid + "'\n" +
                "\tand pa.\"zillaid\" = '" + zillaid + "'\n" +
                "\tand pa.\"upazilaid\" = '" + upazilaid + "'\n" +
                "\tand pa.\"unionid\" = '" + unionid + "' and A.\"HHNo\" IN(" + HHinClause + ")";

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
                "where pa.\"provCode\" =" + provCode +
                "\tand pa.\"mouzaid\" = '" + mouzaid + "'\n" +
                "\tand pa.\"villageid\" = '" + villageid + "'\n" +
                "\tand pa.\"zillaid\" = '" + zillaid + "'\n" +
                "\tand pa.\"upazilaid\" = '" + upazilaid + "'\n" +
                "\tand pa.\"unionid\" = '" + unionid + "' and A.\"HHNo\" IN(" + HHinClause + ")";

        VariableList = "healthId,pregNo,serviceId,providerId,visitSource,visitDate,visitMonth,serviceSource,urineAlbumin,ironFolStatus,ironFolQty,ironFolUnit,misoStatus,misoQty,misoUnit,sateliteCenterName,systemEntryDate,modifyDate,upload";
        C.DownloadJSON(sql, "ancService", VariableList, "healthId, pregNo, serviceId");


        //delivery
        sql = "select  C.\"healthId\", C.\"pregNo\", C.\"providerId\", C.\"outcomePlace\", C.\"deliveryCenterName\",C.\"admissionDate\", C.\"ward\", C.\"bed\", C.\"outcomeDate\", C.\"outcomeTime\", C.\"outcomeType\",C.\"liveBirth\", C.\"stillBirth\", C.\"stillBirthFresh\", C.\"stillBirthMacerated\",C.\"abortion\", C.\"nBoy\", C.\"nGirl\", C.\"applyOxytocin\", C.\"applyTraction\",C.\"uterusMassage\", C.\"episiotomy\", C.\"misoprostol\", C.\"attendantName\", C.\"attendantDesignation\",C.\"excessBloodLoss\", C.\"lateDelivery\", C.\"blockedDelivery\", C.\"placenta\",C.\"headache\", C.\"blurryVision\", C.\"otherBodyPart\", C.\"convulsion\", C.\"others\",C.\"othersNote\", C.\"treatment\", C.\"advice\", C.\"refer\", C.\"referReason\", C.\"referCenterName\",C.\"systemEntryDate\", C.\"modifyDate\", '1' upload \n" +
                "from \"ProviderArea\" pa \n" +
                "INNER JOIN \"Member\" A ON A.\"Dist\" = pa.zillaid and A.\"Upz\" = pa.upazilaid and A.\"UN\" = pa.unionid\n" +
                "and A.\"Mouza\" = pa.mouzaid and A.\"Vill\" = pa.villageid \n" +
                "INNER JOIN \"clientMap\" B ON A.\"HealthID\" = B.\"healthId\"\n" +
                "INNER JOIN \"delivery\" C ON C.\"healthId\" = B.\"generatedId\"\n" +
                "where pa.\"provCode\" =" + provCode +
                "\tand pa.\"mouzaid\" = '" + mouzaid + "'\n" +
                "\tand pa.\"villageid\" = '" + villageid + "'\n" +
                "\tand pa.\"zillaid\" = '" + zillaid + "'\n" +
                "\tand pa.\"upazilaid\" = '" + upazilaid + "'\n" +
                "\tand pa.\"unionid\" = '" + unionid + "' and A.\"HHNo\" IN(" + HHinClause + ")";

        VariableList = "healthId, pregNo, providerId, outcomePlace, deliveryCenterName,admissionDate, ward, bed, outcomeDate, outcomeTime, outcomeType,liveBirth, stillBirth, stillBirthFresh, stillBirthMacerated,abortion, nBoy, nGirl, applyOxytocin, applyTraction,uterusMassage, episiotomy, misoprostol, attendantName, attendantDesignation,excessBloodLoss, lateDelivery, blockedDelivery, placenta,headache, blurryVision, otherBodyPart, convulsion, others,othersNote, treatment, advice, refer, referReason, referCenterName,systemEntryDate, modifyDate, upload";
        C.DownloadJSON(sql, "delivery", VariableList, "healthId, pregNo");


        //newBorn
        sql = "select  C.\"healthId\", C.\"pregNo\", C.\"childNo\", C.\"providerId\", C.\"childHealthId\",C.\"birthStatus\", C.\"gender\", C.\"outcomePlace\", C.\"outcomeDate\", C.\"outcomeTime\",C.\"attendantDesignation\", C.\"outcomeType\", C.\"birthWeightStatus\", C.\"birthWeight\", C.\"immatureBirth\",C.\"dryingAfterBirth\", C.\"resassitation\", C.\"stimulation\", C.\"bagNMask\", C.\"chlorehexidin\",C.\"skinTouch\", C.\"breastFeed\", C.\"bathThreeDays\", C.\"refer\", C.\"referReason\",C.\"referCenterName\", C.\"systemEntryDate\", C.\"modifyDate\", '1' upload \n" +
                "from \"ProviderArea\" pa \n" +
                "INNER JOIN \"Member\" A ON A.\"Dist\" = pa.zillaid and A.\"Upz\" = pa.upazilaid and A.\"UN\" = pa.unionid\n" +
                "and A.\"Mouza\" = pa.mouzaid and A.\"Vill\" = pa.villageid \n" +
                "INNER JOIN \"clientMap\" B ON A.\"HealthID\" = B.\"healthId\"\n" +
                "INNER JOIN \"newBorn\" C ON C.\"healthId\" = B.\"generatedId\"\n" +
                "where pa.\"provCode\" =" + provCode +
                "\tand pa.\"mouzaid\" = '" + mouzaid + "'\n" +
                "\tand pa.\"villageid\" = '" + villageid + "'\n" +
                "\tand pa.\"zillaid\" = '" + zillaid + "'\n" +
                "\tand pa.\"upazilaid\" = '" + upazilaid + "'\n" +
                "\tand pa.\"unionid\" = '" + unionid + "' and A.\"HHNo\" IN(" + HHinClause + ")";

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
                "where pa.\"provCode\" =" + provCode +
                "\tand pa.\"mouzaid\" = '" + mouzaid + "'\n" +
                "\tand pa.\"villageid\" = '" + villageid + "'\n" +
                "\tand pa.\"zillaid\" = '" + zillaid + "'\n" +
                "\tand pa.\"upazilaid\" = '" + upazilaid + "'\n" +
                "\tand pa.\"unionid\" = '" + unionid + "' and A.\"HHNo\" IN(" + HHinClause + ")";

        VariableList = "healthId, pregNo, childNo, childHealthId, serviceId,providerId, visitDate, serviceSource, temperature, weight,breathingPerMinute, dangerSign, breastFeedingOnly, symptom,disease, treatment, advice, refer, referReason, referCenterName,systemEntryDate, modifyDate, visitSource, visitMonth, upload";
        C.DownloadJSON(sql, "pncServiceChild", VariableList, "healthId, pregNo, childNo, serviceId");


        //pncServiceMother
        sql = "select  C.\"healthId\", C.\"pregNo\", C.\"serviceId\", C.\"providerId\", C.\"visitDate\",C.\"serviceSource\", C.\"temperature\", C.\"bpSystolic\", C.\"bpDiastolic\", C.\"hemoglobin\",C.\"breastCondition\", C.\"uterusInvolution\", C.\"hematuria\", C.\"perineum\", C.\"FPMethod\",C.\"symptom\", C.\"disease\", C.\"treatment\", C.\"advice\", C.\"refer\", C.\"referReason\", C.\"referCenterName\",C.\"systemEntryDate\", C.\"modifyDate\", C.\"visitSource\", C.\"visitMonth\", '1' upload\n" +
                "from \"ProviderArea\" pa \n" +
                "INNER JOIN \"Member\" A ON A.\"Dist\" = pa.zillaid and A.\"Upz\" = pa.upazilaid and A.\"UN\" = pa.unionid\n" +
                "and A.\"Mouza\" = pa.mouzaid and A.\"Vill\" = pa.villageid \n" +
                "INNER JOIN \"clientMap\" B ON A.\"HealthID\" = B.\"healthId\"\n" +
                "INNER JOIN \"pncServiceMother\" C ON C.\"healthId\" = B.\"generatedId\"\n" +
                "where pa.\"provCode\" =" + provCode +
                "\tand pa.\"mouzaid\" = '" + mouzaid + "'\n" +
                "\tand pa.\"villageid\" = '" + villageid + "'\n" +
                "\tand pa.\"zillaid\" = '" + zillaid + "'\n" +
                "\tand pa.\"upazilaid\" = '" + upazilaid + "'\n" +
                "\tand pa.\"unionid\" = '" + unionid + "' and A.\"HHNo\" IN(" + HHinClause + ")";

        VariableList = "healthId, pregNo, serviceId, providerId, visitDate,serviceSource, temperature, bpSystolic, bpDiastolic, hemoglobin,breastCondition, uterusInvolution, hematuria, perineum, FPMethod,symptom, disease, treatment, advice, refer, referReason, referCenterName,systemEntryDate, modifyDate, visitSource, visitMonth, upload";
        C.DownloadJSON(sql, "pncServiceMother", VariableList, "healthId, pregNo, serviceId");


    }

    private void FillProvider() {

        String Type = "";
        if (g.getProvType().equals("10")) {

            Type = "3";
            //secUnit.setVisibility(View.VISIBLE);
        } else if (g.getProvType().equals("11")) {
            Type = "2";
            //secWord.setVisibility(View.VISIBLE);
        } else if (g.getProvType().equals("12")) {
            Type = "2";
            //secWord.setVisibility(View.VISIBLE);
        }
        ((Spinner) findViewById(R.id.spnProvider)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                DisplayProviderArea();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        String sql = "Select provCode||'-'||provName from ProviderDb WHERE provType='" + Type + "'";
        ((Spinner) findViewById(R.id.spnProvider)).setAdapter(C.getArrayAdapter(sql));



    }


    private void FillFWAUnit() {



        ((Spinner) findViewById(R.id.spnProvider)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (g.getProvType().equals("10")) {


                } else if (g.getProvType().equals("11")||g.getProvType().equals("12")) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

       // String sql = "Select provCode||'-'||provName from ProviderDb WHERE provType='" + Type + "'";
       // ((Spinner) findViewById(R.id.spnProvider)).setAdapter(C.getArrayAdapter(sql));



    }

    private void DisplayProviderArea() {
        GridView g1 = (GridView) findViewById(R.id.gridMenu);
        g1.setAdapter(new GridAdapter(this));


    }

    public class GridAdapter extends BaseAdapter {
        private Context mContext;
        String[][] vcode;
        String MouzaCode;
        Integer totalRec;
        String message = "";
        int jumpTime = 0;


        public GridAdapter(Context c) {
            mContext = c;

        }

        public int getCount() {

            return Integer.parseInt(C.ReturnSingleValue("select count(*)TotalVill from ProviderArea where ProvCode = '" +
                    Connection.split(((Spinner) findViewById(R.id.spnProvider)).getSelectedItem().toString(), '-')[0] + "'"));
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        //Create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            View MyView = convertView;
            if (convertView == null) {
                LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                MyView = li.inflate(R.layout.single_grid_item, null);

                String sql = "SELECT v.zillaid AS dist,\n" +
                        "       v.upazilaid AS upazila,\n" +
                        "       v.unionid AS unions,\n" +
                        "       v.mouzaid AS mouza,\n" +
                        "       v.villageid AS village,\n" +
                        "       v.villagename AS villname FROM Village v\n" +
                        "       \n" +
                        "       Inner Join ProviderArea PA \n" +
                        "       ON v.zillaid = PA.zillaid \n" +
                        "AND\n" +
                        "v.upazilaid = PA.upazilaid\n" +
                        "AND\n" +
                        "v.unionid = PA.unionid \n" +
                        "AND\n" +
                        "v.mouzaid = PA.mouzaid \n  and\n" +
                        "v.VILLAGEID = PA.villageid " +
                        "\n" +
                        "       WHERE PA.ProvCode = '" + Connection.split(((Spinner) findViewById(R.id.spnProvider)).getSelectedItem().toString(), '-')[0] + "'";

                Cursor cur = C.ReadData(sql);
                cur.moveToFirst();

                totalRec = getCount();
                vcode = new String[6][totalRec];
                int i = 0;
                while (!cur.isAfterLast()) {
                    vcode[0][i] = cur.getString(cur.getColumnIndex("dist"));
                    vcode[1][i] = cur.getString(cur.getColumnIndex("upazila"));
                    vcode[2][i] = cur.getString(cur.getColumnIndex("unions"));
                    vcode[3][i] = cur.getString(cur.getColumnIndex("mouza"));
                    vcode[4][i] = cur.getString(cur.getColumnIndex("village"));
                    vcode[5][i] = cur.getString(cur.getColumnIndex("villname"));
                   /* vcode[6][i] = cur.getString(cur.getColumnIndex("totalhh"));
                    vcode[7][i] = cur.getString(cur.getColumnIndex("mouzaname"));
*/
                    i += 1;
                    cur.moveToNext();
                }

                cur.close();

                final Button tv = (Button) MyView.findViewById(R.id.image_name);
                tv.setTextSize(14);

                /*tv.setText("মৌজা: " + vcode[3][position] + "-" + vcode[7][position] + "\n" +
                        " গ্রাম: " + vcode[4][position] + "-" + vcode[5][position] + "\n(" + vcode[6][position] + ")");*/

                tv.setText(" গ্রাম: " + vcode[4][position] + "-" + vcode[5][position]);
                final Integer p = position;


                tv.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        // final ProgressDialog pdlg = ProgressDialog.show(
                        // Villagepopup.this, "", "অপেক্ষা করুন ...", true);

                        progress = new ProgressDialog(conxt);
                        progress.setMessage("Getting Household List from Server");
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.setIcon(R.drawable.rhislogo2);
                        progress.setIndeterminate(true);
                        progress.setCancelable(false);
                        //  progress.setProgress(0);

                        progress.show();


                        final Thread t = new Thread() {
                            @Override
                            public void run() {
                                Looper.prepare();

                                try {

                                    String Dist = vcode[0][p];
                                    String Upz = vcode[1][p];
                                    String Union = vcode[2][p];
                                    String Mouza = vcode[3][p];
                                    String village = vcode[4][p];
                                    String ProvCode = Connection.split(((Spinner) findViewById(R.id.spnProvider)).getSelectedItem().toString(), '-')[0];//vcode[5][p];
                                    //String ProvType = "3";//vcode[6][p];
                                    String ProvType = "";
                                    if (g.getProvType().equals("10")) {
                                        ProvType = "3";
                                    } else if (g.getProvType().equals("11")) {
                                        ProvType = "2";
                                    } else if (g.getProvType().equals("12")) {
                                        ProvType = "2";
                                    }




                                /*    message = "Completing...";
                                    progressHandler.sendMessage(progressHandler.obtainMessage());

                                    sleep(100)*/
                                    ;
                             /*       String SQLStr = "Select h.\"HHNo\" from \"Village\" v";
                                    SQLStr += " inner join \"ProviderArea\" a on v.\"ZILLAID\"=a.zillaid and v.\"UPAZILAID\"=a.upazilaid and v.\"UNIONID\"=a.unionid and v.\"MOUZAID\"=a.mouzaid and v.\"VILLAGEID\"=a.villageid";
                                    SQLStr += " inner join \"Household\" h on a.zillaid=h.\"Dist\" and a.upazilaid=h.\"Upz\" and a.unionid=h.\"UN\" and a.mouzaid=h.\"Mouza\" and a.villageid=h.\"Vill\"";
                                   *//* SQLStr+="LEFT OUTER JOIN \"Member\" m on a.zillaid = m.\"Dist\"\n" +
                                            "\tand a.upazilaid = m.\"Upz\"\n" +
                                            "\tand a.unionid = m.\"UN\"\n" +
                                            "\tand a.mouzaid = m.\"Mouza\"\n" +
                                            "\tand a.villageid = m.\"Vill\"\t\n" +
                                            "\tand m.\"SNo\"='1'";*//*
                                    SQLStr += " where v.\"MOUZAID\" = '" + Mouza + "'\n" +
                                            "\tand v.\"VILLAGEID\" = '" + village + "'\n" +
                                            "\tand a.\"provType\" =  '" + ProvType + "'\n" +
                                            "\tand a.\"provCode\" = '" + ProvCode + "'\n" +
                                            "\tand v.\"ZILLAID\" = '" + Dist + "'\n" +
                                            "\tand v.\"UPAZILAID\" = '" + Upz + "'\n" +
                                            "\tand v.\"UNIONID\" = '" + Union + "'";
                                 *//*   SQLStr += " and v.\"MOUZAID\" = '" + Mouza + "'\n" +
                                            "\tand v.\"VILLAGEID\" = '" + village + "' and a.\"provType\"='" + ProvType + "' and a.\"provCode\"='" +
                                            ProvCode + "' and v.\"ZILLAID\"='" +
                                            Dist + "' and v.\"UPAZILAID\"='" + Upz + "' and v.\"UNIONID\"='" + Union + "'";*//*



                                    List<String> listItem = new ArrayList<String>();
                                    listItem = C.DataListJSON(SQLStr);

                                    message = "Completing...";
                                    progressHandler.sendMessage(progressHandler.obtainMessage());

                                    sleep(100);*/


                                    //Household Head data
                                    // String SQLStr1 = "Select   m.\"HHNo\"||':' ||m.\"NameEng\" as HHNoName from \"Village\" v";
                                    //Household data

                                    String SQLStr = "Select m.\"HHNo\" from \"Village\" v";
                                    SQLStr += " inner join \"ProviderArea\" a on v.\"ZILLAID\"=a.zillaid and v.\"UPAZILAID\"=a.upazilaid and v.\"UNIONID\"=a.unionid and v.\"MOUZAID\"=a.mouzaid and v.\"VILLAGEID\"=a.villageid";
                                    SQLStr += " inner join \"Household\" h on a.zillaid=h.\"Dist\" and a.upazilaid=h.\"Upz\" and a.unionid=h.\"UN\" and a.mouzaid=h.\"Mouza\" and a.villageid=h.\"Vill\"";
                                    SQLStr += " INNER JOIN \"Member\" m on a.zillaid = m.\"Dist\"\n" +
                                            "\tand a.upazilaid = m.\"Upz\"\n" +
                                            "\tand a.unionid = m.\"UN\"\n" +
                                            "\tand a.mouzaid = m.\"Mouza\"\n" +
                                            "\tand a.villageid = m.\"Vill\"\t\n" +
                                            "\tand h.\"HHNo\"= m.\"HHNo\"\t\n" +
                                            "\tand m.\"SNo\"='1'";
                                    SQLStr += " where v.\"MOUZAID\" = '" + Mouza + "'\n" +
                                            "\tand v.\"VILLAGEID\" = '" + village + "'\n" +
                                            "\tand a.\"provType\" = '" + ProvType + "'\n" +
                                            "\tand a.\"provCode\" = '" + ProvCode + "'\n" +
                                            "\tand v.\"ZILLAID\" = '" + Dist + "'\n" +
                                            "\tand v.\"UPAZILAID\" = '" + Upz + "'\n" +
                                            "\tand v.\"UNIONID\" = '" + Union + "'";
                                    String SQLStr1 = "Select m.\"NameEng\" as HHNoName from \"Village\" v";
                                    SQLStr1 += " inner join \"ProviderArea\" a on v.\"ZILLAID\"=a.zillaid and v.\"UPAZILAID\"=a.upazilaid and v.\"UNIONID\"=a.unionid and v.\"MOUZAID\"=a.mouzaid and v.\"VILLAGEID\"=a.villageid";
                                    SQLStr1 += " inner join \"Household\" h on a.zillaid=h.\"Dist\" and a.upazilaid=h.\"Upz\" and a.unionid=h.\"UN\" and a.mouzaid=h.\"Mouza\" and a.villageid=h.\"Vill\"";
                                    SQLStr1 += " INNER JOIN \"Member\" m on a.zillaid = m.\"Dist\"\n" +
                                            "\tand a.upazilaid = m.\"Upz\"\n" +
                                            "\tand a.unionid = m.\"UN\"\n" +
                                            "\tand a.mouzaid = m.\"Mouza\"\n" +
                                            "\tand a.villageid = m.\"Vill\"\t\n" +
                                            "\tand h.\"HHNo\"= m.\"HHNo\"\t\n" +
                                            "\tand m.\"SNo\"='1'";
                                    SQLStr1 += " where v.\"MOUZAID\" = '" + Mouza + "'\n" +
                                            "\tand v.\"VILLAGEID\" = '" + village + "'\n" +
                                            "\tand a.\"provType\" = '" + ProvType + "'\n" +
                                            "\tand a.\"provCode\" = '" + ProvCode + "'\n" +
                                            "\tand v.\"ZILLAID\" = '" + Dist + "'\n" +
                                            "\tand v.\"UPAZILAID\" = '" + Upz + "'\n" +
                                            "\tand v.\"UNIONID\" = '" + Union + "'";
                                  /*  SQLStr1 += " and v.\"MOUZAID\" = '" + Mouza + "'\n" +
                                            "\tand v.\"VILLAGEID\" = '" + village + "' and a.\"provType\"='" + ProvType + "' and a.\"provCode\"='" +
                                            ProvCode + "' and v.\"ZILLAID\"='" +
                                            Dist + "' and v.\"UPAZILAID\"='" + Upz + "' and v.\"UNIONID\"='" + Union + "'";*//*

*/

                                    List<String> listItem = new ArrayList<String>();
                                    listItem = C.DataListJSON(SQLStr);
                                    List<String> listItem1 = new ArrayList<String>();
                                    listItem1 = C.DataListJSON(SQLStr1);

                                    message = "Completing...";
                                    progressHandler.sendMessage(progressHandler.obtainMessage());

                                    sleep(100);
                                    DisplayHouseholdDetails(listItem, listItem1, Mouza, village, ProvCode, Dist, Upz, Union, ProvType);

                                    //DisplayHouseholdDetails(listItem, Mouza, village, ProvCode, Dist, Upz, Union, ProvType);
                                    progress.dismiss();
                                } catch (Exception exp) {
                                    Log.e("Error", exp.getMessage());

                                }


                                Looper.loop();
                            }

                        };
                        t.start();


                    }

                    Handler progressHandler = new Handler() {
                        public void handleMessage(Message msg) {

                            progress.setMessage(message);
                        }
                    };
                });

            }
            return MyView;
        }

    }


}

