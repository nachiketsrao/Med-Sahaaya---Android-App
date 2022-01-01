package studio.knowhere.bloodbank.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.onesignal.OSSubscriptionObserver;
import com.onesignal.OSSubscriptionStateChanges;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import androidx.core.app.ActivityCompat;
import studio.knowhere.bloodbank.Class.ExampleNotificationReceivedHandler;
import studio.knowhere.bloodbank.Class.SignupClass;
import studio.knowhere.bloodbank.R;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class SignupActivity extends Activity implements OSSubscriptionObserver {

    Button mSignUpR, mCancel;
    String strJsonBody,userid2,address,NEWaddress;
    EditText mName, mEmail, mPassword,mPhone,mAge,mAdress;
    Spinner mBloodgroup,mCity,mArea;
    RadioGroup mRadiogrop;
    RadioButton mMale;
    RadioButton mFemale;
    APIs apis;
    String jsonResponse,message,Blooditem,Cityitem,Areaitem;
    ProgressDialog progressDialog;
    String gender = "male";
    SignupClass signupClass;
    private FusedLocationProviderClient client;
    double latitude,longitude;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .setNotificationReceivedHandler(new ExampleNotificationReceivedHandler())
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        OneSignal.addSubscriptionObserver(this);
        setContentView(R.layout.activity_signup);

        mSignUpR = (Button) findViewById(R.id.signedup);
        mCancel = (Button) findViewById(R.id.cancel);

        mName = (EditText) findViewById(R.id.signup_enterusername);
        mAge = (EditText) findViewById(R.id.age_id);
        mArea = (Spinner) findViewById(R.id.area_id);
        mEmail = (EditText) findViewById(R.id.signup_enteremailId);
        mPassword = (EditText) findViewById(R.id.signup_enterpassword);
        mPhone = (EditText) findViewById(R.id.signup_entermobilenumber);
        mCity = (Spinner) findViewById(R.id.city_id);
        mAdress = (EditText) findViewById(R.id.signup_address);
        mBloodgroup = (Spinner) findViewById(R.id.spinner_bood_id);
        mRadiogrop = (RadioGroup) findViewById(R.id.radio_grp);

        apis = new APIs();
        signupClass = new SignupClass();
        client = LocationServices.getFusedLocationProviderClient(this);
        requestPermission();
        if (ActivityCompat.checkSelfPermission(SignupActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        client.getLastLocation().addOnSuccessListener(SignupActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                  //  Toast.makeText(SignupActivity.this,"ff:0",Toast.LENGTH_LONG).show();
                    // locationAdd.setText(address);
                    latitude = location.getLatitude();
                    longitude=location.getLongitude();
                    Log.v("tag","lat is"+latitude);
                    Log.v("tag","long is"+longitude);
                    getAddress(latitude,longitude);
                }
            }
        });

        mBloodgroup.setOnItemSelectedListener(new BloodSpinner());
        mCity.setOnItemSelectedListener(new CitySpinner());
        mArea.setOnItemSelectedListener(new AreaSpinner());

        List<String> bloodgroup = new ArrayList<>();
        bloodgroup.add("A positive");
        bloodgroup.add("A negative");
        bloodgroup.add("B positive");
        bloodgroup.add("B negative");
        bloodgroup.add("AB positive");
        bloodgroup.add("AB negative");
        bloodgroup.add("0 positive");
        bloodgroup.add("0 negative");

        List<String> City = new ArrayList<>();
        City.add("Banglore");
        City.add("Mysore");
        City.add("Mandya");


        List<String> Area = new ArrayList<>();
        Area.add("Vijaynagar");
        Area.add("Hebbal");
        Area.add("Koramangal");
        Area.add("Jpnagar");


        ArrayAdapter<String> bloodadapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,bloodgroup);
        bloodadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBloodgroup.setAdapter(bloodadapter);

        ArrayAdapter<String> cityadapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,City);
        cityadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCity.setAdapter(cityadapter);

        ArrayAdapter<String> areaadapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,Area);
        areaadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mArea.setAdapter(areaadapter);



        mRadiogrop.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int ischecked) {

                mMale =(RadioButton) mRadiogrop.findViewById(R.id.male_id);
                mFemale =(RadioButton) mRadiogrop.findViewById(R.id.female_id);

                if(ischecked == R.id.male_id){

                    Toast.makeText(SignupActivity.this,"malechecked",Toast.LENGTH_SHORT).show();
                    gender = "male";

                }else{
                    gender = "female";
                }

            }
        });



        mSignUpR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validate()){
                    new REGISTRATIONAsyncTask().execute();
                }
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean validate(){
        if(mPhone.getText().toString().trim().equals(""))
            return false;
        else if(mPassword.getText().toString().trim().equals(""))
            return false;
            // else if(signup_confirm_password.getText().toString().trim().equals(""))
            //  return false;
        else
            return true;
    }


    /// function for spinners
    class BloodSpinner implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

            Blooditem = parent.getItemAtPosition(position).toString();
           // Toast.makeText(v.getContext(), "Your choose :" + Blooditem, Toast.LENGTH_SHORT).show();
            Log.v("Tag", "Time slot is" + Blooditem + position);

            // TimeSlot = +position;

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    class CitySpinner implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

            Cityitem = parent.getItemAtPosition(position).toString();
          //  Toast.makeText(v.getContext(), "Your choose :" + Cityitem, Toast.LENGTH_SHORT).show();
            Log.v("Tag", "Time slot is" + Cityitem + position);

            // TimeSlot = +position;

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    class AreaSpinner implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

            Areaitem = parent.getItemAtPosition(position).toString();
           // Toast.makeText(v.getContext(), "Your choose :" + Cityitem, Toast.LENGTH_SHORT).show();
            Log.v("Tag", "Time slot is" + Cityitem + position);

            // TimeSlot = +position;

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }



    private class REGISTRATIONAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(SignupActivity.this);
            progressDialog.setMessage("please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            //  for (int i = 0; i < dataArray.length(); i++) {
            try {


                URL url = new URL(apis.getREGISTERAPI());
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
                        + "\"name\": \"" + mName.getEditableText().toString()+ "\","
                        + "\"email\": \""+mEmail.getEditableText().toString()+"\","
                        + "\"password\": \""+mPassword.getEditableText().toString()+"\","
                        + "\"phone\": \""+mPhone.getEditableText().toString()+"\","
                        + "\"age\": \""+mAge.getEditableText().toString()+"\","
                        + "\"area\": \""+Areaitem+"\","
                        + "\"city\": \""+Cityitem+"\","
                        + "\"blood_group\": \""+Blooditem+"\","
                        + "\"latitude\": \""+latitude+"\","
                        + "\"longitude\": \""+longitude+"\","
                        + "\"address\": \""+NEWaddress+"\","
                        + "\"diabities\": \"false\","
                        + "\"gender\": \""+gender+"\","
                        + "\"player_id\": \""+userid2+"\""
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

                JSONObject jsonObject = new JSONObject(jsonResponse);
                Log.v("TAG","JSON OBJECT IS"+jsonObject);
                JSONObject jsonObjectstatus = jsonObject.getJSONObject("status");
                Log.v("TAG","jsonObjectstatus"+jsonObjectstatus);
                message = jsonObjectstatus.getString("message");

                JSONObject jsonObjectresult = jsonObject.getJSONObject("result");
                JSONObject jsonObjectdetails = jsonObjectresult.getJSONObject("details");
               /* libuser_id =  jsonObjectdetails.getString("_id");
                Log.v("TAG","libuser_id"+libuser_id);
                preferenceManager.setKeyUserid(libuser_id);
*/

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
            progressDialog.dismiss();
            // String success = "success";
            if(jsonResponse==null){
                Toast.makeText(getBaseContext(), "Connection time out", Toast.LENGTH_LONG).show();
            }else if(message.equalsIgnoreCase("Success")){
                Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(getBaseContext(), "Registration Fails...!", Toast.LENGTH_LONG).show();
            }

        }
    }

    //check permissions
    private void requestPermission(){
        ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION},1);
    }


    @Override
    public void onOSSubscriptionChanged(OSSubscriptionStateChanges stateChanges) {
        if (!stateChanges.getFrom().getSubscribed() &&
                stateChanges.getTo().getSubscribed()) {


            new AlertDialog.Builder(this)
                    .setMessage("You've successfully subscribed to push notifications!")
                    .show();
            // get player ID
            stateChanges.getTo().getUserId();

            Log.v("Tag","USER ID IS"+stateChanges.getTo().getUserId());
            userid2 = stateChanges.getTo().getUserId();
            SignupClass signupClass = new SignupClass();
            signupClass.setDeviceid(userid2);


        }

        Log.i("Debug", "onOSPermissionChanged: " + stateChanges);

    }

    private void getAddress(double latitude, double longitude) {

        Geocoder geocoder;
        List<Address> addresses;
        geocoder=new Geocoder(SignupActivity.this, Locale.getDefault());


        try{
            addresses=geocoder.getFromLocation(latitude,longitude,1);
            address=addresses.get(0).getAddressLine(0);
            NEWaddress =  address.replace(",",".");
            mAdress.setText(NEWaddress);
            Log.v("ddff","adres:"+NEWaddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
