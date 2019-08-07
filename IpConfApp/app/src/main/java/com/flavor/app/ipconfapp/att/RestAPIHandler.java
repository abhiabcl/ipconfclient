package com.flavor.app.ipconfapp.att;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestAPIHandler {

    static String TAG = "IPCONF";
    static String createConfAPI = "/ipconfservice/createconfroom";
    static String DialoutAPI = "/ipconfservice/joinroom";
    static String TerminAPI = "/ipconfservice/terminateConf";

    static String createConfURL;
    static String dialoutURL;
    static String termiConfURL;
    static String confId;

    public static String getCreateConfURL() {
        return createConfURL;
    }

    public static void setCreateConfURL(String createConfURL) {
        RestAPIHandler.createConfURL = createConfURL;
    }

    public static String getDialoutURL() {
        return dialoutURL;
    }

    public static String getConfId() {
        return confId;
    }

    public static void setConfId(String confId) {
        RestAPIHandler.confId = confId;
    }

    public static void setDialoutURL(String dialoutURL) {
        RestAPIHandler.dialoutURL = dialoutURL;
    }

    public static String getTermiConfURL() {
        return termiConfURL;
    }

    public static void setTermiConfURL(String termiConfURL) {
        RestAPIHandler.termiConfURL = termiConfURL;
    }

    public static JSONObject createConfRequest(String bridgePhone, String hostCode, String passCode){
        String request = null;
        if (bridgePhone.isEmpty() && hostCode.isEmpty() && passCode.isEmpty())
            request = "{\"bridgePhone\":\"7323682907\", \"hostCode\":\"256256\", \"partCode\":\"135135135\"}";
        else
            request = "{\"bridgePhone\":\""+bridgePhone+"\", \"hostCode\":\""+hostCode+"\", \"partCode\":\""+passCode+"\"}";

        JSONObject reqObject= null;
        try {
            reqObject = new JSONObject(request);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return reqObject;
    }

    public static String dialOutRequest(String confId, String phoneno){
        String request = getDialoutURL()+"/"+confId+"/"+phoneno;
        return request;
    }


    public static String terminConfRequest(String confId){
        String request = getDialoutURL()+"/"+confId;
        return request;
    }

    public static String doHttpPost(String myUrl, JSONObject jsonObject) throws IOException, JSONException {
//        String result = "";
        BufferedReader br = null;
        URL url = new URL(myUrl);

        Log.i("IPCONF", "URL: " + myUrl);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        setPostRequestContent(conn, jsonObject);
        conn.connect();

        if (200 <= conn.getResponseCode() && conn.getResponseCode() <= 299) {
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        String line = null;
        String message = new String();
        final StringBuffer buffer = new StringBuffer(2048);
        while ((line = br.readLine()) != null) {
            // buffer.append(line);
            message += line;
        }

        Log.i(TAG, "Received msg:"+ message);
        return message;

    }

    public static String doHttpGet(String myUrl) throws IOException, JSONException {
//        String result = "";
        BufferedReader br = null;
        URL url = new URL(myUrl);

        Log.i("IPCONF", "URL: " + myUrl);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        if (200 <= conn.getResponseCode() && conn.getResponseCode() <= 299) {
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        String line = null;
        String message = new String();
        final StringBuffer buffer = new StringBuffer(2048);
        while ((line = br.readLine()) != null) {
            // buffer.append(line);
            message += line;
        }

        Log.i(TAG, "Received msg:"+ message);
        return message;

    }

    public static String getMsg (String jsonResponse){
        String result = null;
        if (jsonResponse != null) {
            try {
                JSONObject mainObject = new JSONObject(jsonResponse);
                result = mainObject.get("result").toString();
                Log.i("IPCONF", "result: " + result);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static void setPostRequestContent(HttpURLConnection conn,
                                             JSONObject jsonObject) throws IOException {
        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(jsonObject.toString());
        Log.i(TAG, jsonObject.toString());
        writer.flush();
        writer.close();
        os.close();
    }

    public static void sendEmail(Activity pActivity, String pTo, String pSubject, String pMailTxt) {
        Log.i("Send email", "");
        String TO = pTo;
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, pSubject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, pMailTxt);

        try {
            pActivity.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
//            finish();
            Log.i("IPCONF", "Finished sending email...");
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(pActivity, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public static void sendSMS(Activity pActivity, String pSMSNumber, String pSMSText ){
        Uri uri = Uri.parse("smsto:" + pSMSNumber);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", pSMSText);
        pActivity.startActivity(intent);
    }

}
