package studio.knowhere.bloodbank.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import studio.knowhere.bloodbank.Class.PreferenceManager;
import studio.knowhere.bloodbank.R;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class IndividualProfileActivity extends AppCompatActivity {

    // we extend AppCompatActivity when we need backward compatibility of the action bar

    TextView mName, mEmail, mMobileNum, mAddress;
    String finalID, finalLat, finalLong,strJsonBody,jsonResponse,message,id;
    Button mCallbtn, mEmailbtn, mMsgbtn, mMapbtn;
    private FusedLocationProviderClient client;
    double latitude,longitude;
    ProgressDialog progressDialog;
    APIs apIs;
    PreferenceManager preferenceManager;
    String TYPEofCOMM = "null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_profile);

        mName = (TextView) findViewById(R.id.signup_enterusername);
        mEmail = (TextView) findViewById(R.id.signup_enteremailId);
        mMobileNum = (TextView) findViewById(R.id.signup_entermobilenumber);
        mAddress = (TextView) findViewById(R.id.signup_address);
        mCallbtn = (Button) findViewById(R.id.call_id_btn);
        mEmailbtn = (Button) findViewById(R.id.email_id_btn);
        mMsgbtn = (Button) findViewById(R.id.msg_id_btn);
        mMapbtn = (Button) findViewById(R.id.map_id_btn);


        Intent intent = getIntent();
        String NAME = intent.getStringExtra("NAME");
         finalLat = intent.getStringExtra("LATTITUDE");
         finalLong = intent.getStringExtra("LONGITUDE");
        String EMAIL = intent.getStringExtra("EMAIL");
        String NUMBER = intent.getStringExtra("NUMBER");
        String ADRESS = intent.getStringExtra("ADRESS");
         finalID = intent.getStringExtra("ID");

        mAddress.setText(ADRESS);
        mMobileNum.setText(NUMBER);
        mEmail.setText(EMAIL);
        mName.setText(NAME);
        apIs = new APIs();
        preferenceManager = new PreferenceManager(getApplicationContext());

       /* ArrayList<String> arrayListvalue = new ArrayList<>(Arrays.asList(SelectedItemsAre.split(",")));
        ArrayList<String> arrayListAdress = new ArrayList<>(Arrays.asList(arrayListvalue.get(0).split("=")));
        mAddress.setText(arrayListAdress.get(1));
        ArrayList<String> arrayListvalueofId = new ArrayList<>(Arrays.asList(arrayListvalue.get(1).split("=")));
        finalID = arrayListvalueofId.get(1);
        System.out.println("values of id" + arrayListvalueofId.get(1));
        ArrayList<String> arrayListLongitude = new ArrayList<>(Arrays.asList(arrayListvalue.get(2).split("=")));
        finalLong = arrayListLongitude.get(1);
        System.out.println("values of long" + arrayListLongitude.get(1));
        ArrayList<String> arrayListMobile = new ArrayList<>(Arrays.asList(arrayListvalue.get(3).split("=")));
        mMobileNum.setText(arrayListMobile.get(1));
        System.out.println("values of mobile" + arrayListMobile.get(1));
        ArrayList<String> arrayListEmail = new ArrayList<>(Arrays.asList(arrayListvalue.get(4).split("=")));
        mEmail.setText(arrayListEmail.get(1));
        System.out.println("values of email" + arrayListEmail.get(1));
        ArrayList<String> arrayListName = new ArrayList<>(Arrays.asList(arrayListvalue.get(5).split("=")));
        mName.setText(arrayListName.get(1));
        System.out.println("values of name" + arrayListName.get(1));
        ArrayList<String> arrayListLattitude = new ArrayList<>(Arrays.asList(arrayListvalue.get(6).split("=")));
        finalLat = arrayListLattitude.get(1);
        System.out.println("values is" + SelectedItemsAre);
        System.out.println("values of lat" + arrayListLattitude.get(1));
        ArrayList<String> arrayListAailabilty = new ArrayList<>(Arrays.asList(arrayListvalue.get(7).split("=")));
       String Availability = arrayListAailabilty.get(1);
        System.out.println("values of Availability" + arrayListAailabilty.get(1));*/

        client = LocationServices.getFusedLocationProviderClient(this);
        requestPermission();
        if (ActivityCompat.checkSelfPermission(IndividualProfileActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        client.getLastLocation().addOnSuccessListener(IndividualProfileActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    //  Toast.makeText(SignupActivity.this,"ff:0",Toast.LENGTH_LONG).show();
                    // locationAdd.setText(address);
                    latitude = location.getLatitude();
                    longitude=location.getLongitude();
                    Log.v("tag","lat is"+latitude);
                    Log.v("tag","long is"+longitude);
                  //  getAddress(latitude,longitude);
                }
            }
        });



        mCallbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TYPEofCOMM = "CALL";
                new TypeofCommAsyncTask().execute();

                Intent intentcall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mMobileNum.getText().toString()));
                if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                startActivity(intentcall);

            }
        });
        mMsgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TYPEofCOMM = "MESSAGE";
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setData(Uri.parse("smsto:"));
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address"  , new String (mMobileNum.getText().toString()));
                smsIntent.putExtra("sms_body"  , "Test ");
                startActivity(smsIntent);
                new TypeofCommAsyncTask().execute();
               /* String number = mMobileNum.getText().toString();  // The number on which you want to send SMS
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));

*/
            }
        });
        mEmailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TYPEofCOMM = "EMAIL";
                new TypeofCommAsyncTask().execute();
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",mEmail.getText().toString(), null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "EMERGENCY BOOD NEEDED");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });
        mMapbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr="+finalLat+","+finalLong+""));
                startActivity(intent);
                Log.v("TAG","URL MAP"+"http://maps.google.com/maps?daddr="+finalLat+","+finalLong+"");
            }
        });

    }

    //check permissions
    private void requestPermission(){
        ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION},1);
    }


    private class TypeofCommAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(IndividualProfileActivity.this);
            progressDialog.setMessage("please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            //  for (int i = 0; i < dataArray.length(); i++) {
            try {


                URL url = new URL(apIs.getADDDONAR());
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
                        + "\"user_id\": \"" +preferenceManager.getKeyUserid(id)+ "\","
                        + "\"donot_id\": \""+finalID+"\","
                        + "\"type_of_communication\": \""+TYPEofCOMM+"\","
                        + "\"date\": \"01/03/2020\""
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

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonResponse);
                Log.v("TAG","JSON OBJECT IS"+jsonObject);
                JSONObject jsonObjectstatus = jsonObject.getJSONObject("status");
               // JSONObject jsonObjectresult = jsonObject.getJSONObject("result");
                Log.v("TAG","jsonObjectstatus"+jsonObjectstatus);

                message = jsonObjectstatus.getString("message");
                Log.v("TAG","response is"+jsonResponse);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            progressDialog.dismiss();
            // String success = "success";
            if(jsonResponse==null){
                Toast.makeText(getBaseContext(), "Connection time out", Toast.LENGTH_LONG).show();
            }else if(message.equalsIgnoreCase("Success")){
              /*  Intent intent = new Intent(IndividualProfileActivity.this,HomeActivity.class);
                startActivity(intent);*/
            }else{
                Toast.makeText(getBaseContext(), " Failed to update...!", Toast.LENGTH_LONG).show();
            }

        }
    }

}
