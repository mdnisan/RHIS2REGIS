package DataSync;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import Common.Global;

/**
 * Created by TanvirHossain on 02/12/2015.
 */
public class UploadJSONData extends AsyncTask<String, Void, String> {
    private Context context;
    final String USER_AGENT = Global.USER_AGENT_WebAPI;
    HttpURLConnection conn1 = null;
    final String url        = Global.URL_WebAPI;
    StringBuffer response;

    public void setContext(Context contextf){
        context = contextf;
    }

    private Exception exception;
    @Override
    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            URL obj = null;
            try {
                obj = new URL(url);
                conn1 = (HttpURLConnection) obj.openConnection();
                //add reuqest header
                conn1.setRequestMethod("POST");
                conn1.setRequestProperty("User-Agent", USER_AGENT);
                conn1.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                conn1.setRequestProperty("Content-Type", "application/json");

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                conn1.disconnect();
                e.printStackTrace();
            }

            // Send post request
            conn1.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(conn1.getOutputStream());
            wr.writeBytes(urls[0]);
            wr.flush();
            wr.close();

            // getting post response
            int responseCode = conn1.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn1.getInputStream()));
            String inputLine;
            response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

        } catch (Exception e) {
            conn1.disconnect();
            this.exception = e;
            return null;
        }
        return response.toString();
    }

    protected void onPostExecute(String feed) {
    }
}