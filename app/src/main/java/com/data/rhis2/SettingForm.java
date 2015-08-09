package com.data.rhis2;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import Common.*;
import DataService.SyncRebuildDatabase;
import DataService.SyncReceiver;

public class SettingForm extends Activity {
   Connection C;
    //private ProgressDialog dialog;
   
public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
  try
    {
        setContentView(R.layout.devicesetting);
        C = new Connection(this);

        final Spinner spnDist     = (Spinner)findViewById(R.id.spnDist);
        SpinnerItem(spnDist, "Select ZILLAID+'-'+ZILLANAME from Zilla");

        final Spinner spnUpz      = (Spinner)findViewById(R.id.spnUpz);
        final Spinner spnUN       = (Spinner)findViewById(R.id.spnUN);
        final Spinner spnProvider      = (Spinner)findViewById(R.id.spnProvider);
        SpinnerItem(spnProvider, "select ProvType+'-'+TypeName from ProviderType");

        /*List<String> listProv = new ArrayList<String>();
        listProv.add("01-FWA");
        listProv.add("02-HA");
        ArrayAdapter<String> adptrProv= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listProv);
        spnProvider.setAdapter(adptrProv);
        */

        final TextView txtProviderCode = (TextView)findViewById(R.id.txtProviderCode);

        spnDist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                SpinnerItem(spnUpz, "select UPAZILAID+'-'+UPAZILANAME from upazila where zillaid='" + Global.Left(spnDist.getSelectedItem().toString(), 2) + "'");
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        spnUpz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                SpinnerItem(spnUN,"select UNIONID +'-'+UNIONNAME from unions where zillaid='"+ Global.Left(spnDist.getSelectedItem().toString(),2) +"' and UPAZILAID='"+ Global.Left(spnUpz.getSelectedItem().toString(),2) +"'");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        Button cmdSave = (Button)findViewById(R.id.cmdSave);
       	cmdSave.setOnClickListener(new View.OnClickListener() {
       	    public void onClick(View arg0) {
                try {

                    String SQLStr = "";
                    SQLStr = "Select zillaid from ProviderDB where";
                    SQLStr += " zillaid='" + Global.Left(spnDist.getSelectedItem().toString(), 2) + "' and";
                    SQLStr += " upazilaid='" + Global.Left(spnUpz.getSelectedItem().toString(), 2) + "' and";
                    SQLStr += " unionid='" + Global.Left(spnUN.getSelectedItem().toString(), 2) + "' and";
                    SQLStr += " provtype='" + Global.Left(spnProvider.getSelectedItem().toString(),2) + "' and";
                    SQLStr += " provcode='" + txtProviderCode.getText().toString() + "' and";
                    SQLStr += " active='1' and DeviceSetting='1'";

                    String AreaCode = C.ReturnResult("Existence", SQLStr);
                    if (AreaCode.equals("2")) {
                        Connection.MessageBox(SettingForm.this, "This is not a valid information for device setting or information not available for this provider.");
                        return;
                    }

                    //Rebuild database
                    C.RebuildDatabase(Global.Left(spnDist.getSelectedItem().toString(), 2), Global.Left(spnUpz.getSelectedItem().toString(), 2), Global.Left(spnUN.getSelectedItem().toString(), 2), Global.Left(spnProvider.getSelectedItem().toString(),2), txtProviderCode.getText().toString());

                    Connection.MessageBox(SettingForm.this, "Completed successfully...");


                    //Call Login Form
                    finish();
                    Intent f1 = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(f1);
                }
                catch(Exception ex)
                {
                    Connection.MessageBox(SettingForm.this,ex.getMessage());
                    return;
                }
       	    }
       	});   
    }
    catch(Exception ex)
    {
    	Connection.MessageBox(SettingForm.this, ex.getMessage());
    	return;
    }


 }

    private void SpinnerItem(Spinner SpinnerName,String SQL)
    {
        List<String> listItem = new ArrayList<String>();
        listItem = AreaList(SQL);
        ArrayAdapter<String> adptrList= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listItem);
        SpinnerName.setAdapter(adptrList);
    }

    private List<String> AreaList(String SQL)
    {
        String DataArray[] = null;
        DownloaAreadData d = new DownloaAreadData();
        d.setContext(getApplicationContext());
        d.Method_Name = "DownloadData";
        d.SQLStr = SQL;

        try {
            DataArray = d.execute("").get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        List<String> stringList = new ArrayList<String>(Arrays.asList(DataArray));

        return stringList;
    }

    public class DownloaAreadData extends AsyncTask<String, Void, String[]> {
        String Response = "";
        private Context context;
        private ProgressDialog dialog;

        public void setContext(Context contextf){
            context = contextf;
        }

        //Method Name
        public String Method_Name;
        public String SQLStr;

        public String WSDL_TARGET_NAMESPACE = Global.Namespace;
        public String SOAP_ACTION = Global.Namespace+Method_Name;
        public String SOAP_ADDRESS =Global.Soap_Address;

        String[] Data=null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(SettingForm.this);
            dialog.setMessage("Synchronizing Database ...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String[] doInBackground(String... urls) {

            try {
                SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,Method_Name);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_ADDRESS);

                request.addProperty("SQL",SQLStr);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet=true;
                envelope.setOutputSoapObject(request);

                androidHttpTransport.call(Global.Namespace+Method_Name, envelope);
                SoapObject result = (SoapObject)envelope.getResponse();


                Data=new String[result.getPropertyCount()];
                for(int i=0;i<result.getPropertyCount();i++)
                {
                    Data[i]=result.getProperty(i).toString();
                }
                return Data;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return Data;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
            dialog.dismiss();
        }

    }


    private class DataSyncTask extends AsyncTask<String, String, Void> {
        String Response = "";
        private Context context;
        private ProgressDialog dialog;

        public void setContext(Context contextf){
            context = contextf;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(SettingForm.this);
            dialog.setMessage("Synchronizing Database ...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.show();
        }


        protected void onProgressUpdate(String... progress) {
            dialog.setProgress(Integer.parseInt(progress[0]));
            publishProgress(progress);

        }
        /**
         * This is where YOU do YOUR work. There's nothing for me to write here
         * you have to fill this in. Make your HTTP request(s) or whatever it is
         * you have to do to get your updates in here, because this is run in a
         * separate thread
         */
        @Override
        protected Void doInBackground(String... params) {
            String[] P = Connection.split(params[0], '^');
/*
            String SQLStr = "";
            SQLStr = "Select zillaid from ProviderDB where";
            SQLStr += " zillaid   ='" + P[0] + "' and";
            SQLStr += " upazilaid ='" + P[1] + "' and";
            SQLStr += " unionid   ='" + P[2] + "' and";
            SQLStr += " provtype  ='" + P[3] + "' and";
            SQLStr += " provcode  ='" + P[4] + "' and";
            SQLStr += " active='1' and DeviceSetting='1'";

            String AreaCode = C.ReturnResult("Existence", SQLStr);
            if (AreaCode.equals("2")) {
                Response = "This is not a valid information for device setting or information not available for this provider.";
            }*/

            //Rebuild database
            C.RebuildDatabase(P[0], P[1], P[2], P[3], P[4]);

            //Response = "done";

            return null;
        }

        protected void onPostExecute(String result) {
            dialog.dismiss();
            finish();
            Intent f1 = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(f1);
        }
    }

    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
        //dialog.dismiss();
        //mWakeLock.release();
    }
}