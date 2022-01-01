package studio.knowhere.bloodbank.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.onesignal.OSSubscriptionObserver;
import com.onesignal.OSSubscriptionStateChanges;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import studio.knowhere.bloodbank.Class.PreferenceManager;
import studio.knowhere.bloodbank.Class.SignupClass;
import studio.knowhere.bloodbank.R;

public class LoginActivity extends AppCompatActivity {


    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    Button mLogin, mSignUp;
    EditText mPHONE, mPassword;
    String strJsonBody;
    APIs apis;
    String jsonResponse,id;
    ProgressDialog progressDialog;
    String message;
    String userid2;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        checkAndRequestPermissions();
        preferenceManager = new PreferenceManager(getApplicationContext());
        mLogin = (Button) findViewById(R.id.login_btn);
        mSignUp = (Button) findViewById(R.id.signup_btn);
        mPHONE = (EditText) findViewById(R.id.login_mobile);
        mPassword = (EditText) findViewById(R.id.login_enterpassword);
        apis = new APIs();

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if none of the credentials are empty, we carry on with the login
                if(validate()){
                    new LoginAsyncTask().execute();
                }

            }
        });

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean validate(){

        // this function checks if the login credentials are empty or not
        // if any of the credentials are empty, it returns false

        if(mPHONE.getText().toString().trim().equals(""))
            return false;
        else if(mPassword.getText().toString().trim().equals(""))
            return false;
            // else if(signup_confirm_password.getText().toString().trim().equals(""))
            //  return false;
        else
            return true;
    }

    private class LoginAsyncTask extends AsyncTask<String, Void, String> {
        /*
        An asynchronous task is defined by a computation that runs on a background thread and whose result is published on the UI thread.

        Has 4 steps, called onPreExecute, doInBackground, onProgressUpdate and onPostExecute.
         */

        @Override
        protected void onPreExecute() {

            // ProgressDialog is a modal dialog, which prevents the user from interacting with the app.

            // TRY: Instead of using this class, you should use a progress indicator like ProgressBar,
            // which can be embedded in your app's UI.

            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            // the 3 dots ... allow zero to multiple arguments to be passed when the function is called.

            try {

                URL url = new URL(apis.getLOGINAPI());
                // URL url = new URL("http://192.168.1.6:8080/backup_api/calls/multicreate.php");
                // http://localhost/backup_api/calls/multicreate.php
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setUseCaches(false);
                con.setDoOutput(true);
                con.setDoInput(true);

                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                con.setRequestProperty("Authorization", "Basic NzY3OWM3MzctNTY3ZS00ZTI4LWE0MzctMzM5ODNhYjdkNmJk");
                con.setRequestMethod("POST");


                // Log.v("tag", "playerid array content is " + Arrays.toString(IDS));
                strJsonBody = "{"
                        + "\"phone\": \"" + mPHONE.getEditableText().toString()+ "\","
                        + "\"password\": \""+mPassword.getEditableText().toString()+"\""
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
                JSONObject jsonObjectresult = jsonObject.getJSONObject("result");
                Log.v("TAG","jsonObjectstatus"+jsonObjectstatus);

                message = jsonObjectstatus.getString("message");
                id = jsonObjectresult.getString("_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            preferenceManager.setKeyUserid(id);
            progressDialog.dismiss();
            // String success = "success";
            if(jsonResponse==null){
                Toast.makeText(getBaseContext(), "Connection time out", Toast.LENGTH_LONG).show();
            }else if(message.equalsIgnoreCase("Success")){
                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(getBaseContext(), "Login Fails...!", Toast.LENGTH_LONG).show();
            }

        }
    }


    private  boolean checkAndRequestPermissions() {

        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int phonecallPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        int smsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        int smsreadPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (phonecallPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }
        if (smsPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (smsreadPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


}
