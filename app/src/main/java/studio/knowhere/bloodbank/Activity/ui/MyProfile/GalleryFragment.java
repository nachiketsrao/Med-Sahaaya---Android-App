package studio.knowhere.bloodbank.Activity.ui.MyProfile;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import studio.knowhere.bloodbank.Activity.APIs;
import studio.knowhere.bloodbank.Class.HttpHandler;
import studio.knowhere.bloodbank.Class.PreferenceManager;
import studio.knowhere.bloodbank.R;

import static android.content.ContentValues.TAG;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    ListView mListview;
    PreferenceManager preferenceManager;
    APIs apiList;
    String Jsonstatus,id;
    ProgressDialog pDialog;
    TextView mName, mEmail, mMobileNum, mAddress,mbloodgrp;
    String Pname,Pemail,Pphone,Paddress,Pblood_group,Pavailability,PPlasmaavailability;
    Switch mSwitch,mPlasma;
    ProgressDialog progressDialog;
    String strJsonBody,jsonResponse,message;
    String status;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        mName = (TextView) root.findViewById(R.id.signup_enterusername);
        mEmail = (TextView) root.findViewById(R.id.signup_enteremailId);
        mMobileNum = (TextView) root.findViewById(R.id.signup_entermobilenumber);
        mAddress = (TextView) root.findViewById(R.id.signup_address);
        mbloodgrp = (TextView) root.findViewById(R.id.signup_enterbloodgrp);
        mSwitch = (Switch) root.findViewById(R.id.switch_id);
        mPlasma = (Switch) root.findViewById(R.id.plasma_id);

        preferenceManager = new PreferenceManager(getContext());

        apiList = new APIs();
        new GetProfile().execute();

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    new UpdateAvailabity().execute();
                    status = "true";
                }else {
                    new UpdateAvailabity().execute();
                    status = "false";
                }
            }
        });

        mPlasma.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    new UpdatePlasmaDonarAvailability().execute();
                    status = "true";
                }else {
                    new UpdatePlasmaDonarAvailability().execute();
                    status = "false";
                }
            }
        });
        return root;
    }

    private class GetProfile extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
           /* pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();*/

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(apiList.getPROFILE()+preferenceManager.getKeyUserid(id));

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray result = jsonObj.getJSONArray("result");

                    // looping through All Contacts
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject c = result.getJSONObject(i);

                         Pname = c.getString("name");
                         Pemail = c.getString("email");
                      Pphone = c.getString("phone");
                        Paddress = c.getString("address");
                        Pblood_group = c.getString("blood_group");
                        Pavailability = c.getString("available");
                        PPlasmaavailability = c.getString("plasma_donor");



                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());


                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");


            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
           /* if (pDialog.isShowing())
                pDialog.dismiss();*/

            mName.setText(Pname);
            mbloodgrp.setText(Pblood_group);
            mEmail.setText(Pemail);
            mAddress.setText(Paddress);
            mMobileNum.setText(Pphone);

            if(Pavailability.equalsIgnoreCase("true")){
                mSwitch.setChecked(true);
            }else{
                mSwitch.setChecked(false);
            }

            if(PPlasmaavailability.equalsIgnoreCase("true")){
                mPlasma.setChecked(true);
            }else{
                mPlasma.setChecked(false);
            }
        }
    }


    private class UpdateAvailabity extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
           /* progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();*/
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            //  for (int i = 0; i < dataArray.length(); i++) {
            try {


                URL url = new URL(apiList.getAvailabilityStatus()+preferenceManager.getKeyUserid(id));
                // URL url = new URL("http://192.168.1.6:8080/backup_api/calls/multicreate.php");
                // http://localhost/backup_api/calls/multicreate.php
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setUseCaches(false);
                con.setDoOutput(true);
                con.setDoInput(true);

                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                con.setRequestProperty("Authorization", "Basic NzY3OWM3MzctNTY3ZS00ZTI4LWE0MzctMzM5ODNhYjdkNmJk");
                con.setRequestMethod("PUT");


                // Log.v("tag", "playerid array content is " + Arrays.toString(IDS));
                strJsonBody = "{"
                        + "\"status\": \""+status+"\""
                        + "}";

                System.out.println("strJsonBody:\n" + strJsonBody);
                //d7cf76e1-cfb7-41ba-8687-53e1cdf59c50


                byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                con.setFixedLengthStreamingMode(sendBytes.length);

                OutputStream outputStream = con.getOutputStream();
                outputStream.write(sendBytes);


                int httpResponse = con.getResponseCode();
                System.out.println("httpResponse: " + httpResponse);

                if (httpResponse >= HttpURLConnection.HTTP_OK
                        && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                    Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                    jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                    scanner.close();
                } else {
                    Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                    jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                    scanner.close();
                }
                System.out.println("jsonResponse:\n" + jsonResponse);




            } catch (Throwable t) {
                t.printStackTrace();
            }
            // }
            return jsonResponse;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String jsonResponse) {
            // Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
            Log.v("TAG","response is"+jsonResponse);

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonResponse);
                Log.v("TAG","JSON OBJECT IS"+jsonObject);
                JSONObject jsonObjectstatus = jsonObject.getJSONObject("status");
              //  JSONObject jsonObjectresult = jsonObject.getJSONObject("result");
              //  Log.v("TAG","jsonObjectstatus"+jsonObjectstatus);
                message = jsonObjectstatus.getString("message");

            } catch (JSONException e) {
                e.printStackTrace();
            }


           // progressDialog.dismiss();
            // String success = "success";
            if(jsonResponse==null){
                Toast.makeText(getContext(), "Connection time out", Toast.LENGTH_LONG).show();
            }else if(message.equalsIgnoreCase("Success")){
               // Intent intent = new Intent(getContext(), HomeActivity.class);
               // startActivity(intent);
                Toast.makeText(getContext(), "Status Updated...!", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getContext(), "Status Update Fails...!", Toast.LENGTH_LONG).show();
            }

        }
    }


    private class UpdatePlasmaDonarAvailability extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
           /* progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();*/
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            //  for (int i = 0; i < dataArray.length(); i++) {
            try {


                URL url = new URL(apiList.getPlasmaAvailabilityStatus()+preferenceManager.getKeyUserid(id));
                // URL url = new URL("http://192.168.1.6:8080/backup_api/calls/multicreate.php");
                // http://localhost/backup_api/calls/multicreate.php
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setUseCaches(false);
                con.setDoOutput(true);
                con.setDoInput(true);

                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                con.setRequestProperty("Authorization", "Basic NzY3OWM3MzctNTY3ZS00ZTI4LWE0MzctMzM5ODNhYjdkNmJk");
                con.setRequestMethod("PUT");


                // Log.v("tag", "playerid array content is " + Arrays.toString(IDS));
                strJsonBody = "{"
                        + "\"status\": \""+status+"\""
                        + "}";

                System.out.println("strJsonBody:\n" + strJsonBody);
                //d7cf76e1-cfb7-41ba-8687-53e1cdf59c50


                byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                con.setFixedLengthStreamingMode(sendBytes.length);

                OutputStream outputStream = con.getOutputStream();
                outputStream.write(sendBytes);


                int httpResponse = con.getResponseCode();
                System.out.println("httpResponse: " + httpResponse);

                if (httpResponse >= HttpURLConnection.HTTP_OK
                        && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                    Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                    jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                    scanner.close();
                } else {
                    Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                    jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                    scanner.close();
                }
                System.out.println("jsonResponse:\n" + jsonResponse);




            } catch (Throwable t) {
                t.printStackTrace();
            }
            // }
            return jsonResponse;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String jsonResponse) {
            // Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
            Log.v("TAG","response is"+jsonResponse);
           // progressDialog.dismiss();
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonResponse);
                Log.v("TAG","JSON OBJECT IS"+jsonObject);
                JSONObject jsonObjectstatus = jsonObject.getJSONObject("status");
                //  JSONObject jsonObjectresult = jsonObject.getJSONObject("result");
                //  Log.v("TAG","jsonObjectstatus"+jsonObjectstatus);
                message = jsonObjectstatus.getString("message");

            } catch (JSONException e) {
                e.printStackTrace();
            }



            // String success = "success";
            if(jsonResponse==null){
                Toast.makeText(getContext(), "Connection time out", Toast.LENGTH_LONG).show();
            }else if(message.equalsIgnoreCase("Success")){
                // Intent intent = new Intent(getContext(), HomeActivity.class);
                // startActivity(intent);
                Toast.makeText(getContext(), "Plasma Status Updated...!", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getContext(), "Plasma Status Update Fails...!", Toast.LENGTH_LONG).show();
            }

        }
    }

}