package studio.knowhere.bloodbank.Activity.ui.Notification;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import studio.knowhere.bloodbank.Activity.APIs;
import studio.knowhere.bloodbank.Class.HttpHandler;
import studio.knowhere.bloodbank.Class.PlayerIdModel;
import studio.knowhere.bloodbank.R;

import static android.content.ContentValues.TAG;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    EditText mNotificationContent;
    Button mSend;
    ProgressDialog pDialog;
    APIs apiList;
    JSONArray result;
    PlayerIdModel playerIdModel;
    String player_id,strJsonBody;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        mNotificationContent = (EditText) root.findViewById(R.id.notification_content);
        mSend = (Button) root.findViewById(R.id.send_notify);

        apiList = new APIs();
        new GetUserListsForPlayerID().execute();

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new SendNotifiaction().execute();
            }
        });
        return root;
    }

    private class GetUserListsForPlayerID extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(apiList.getUSERLIST());

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    result = jsonObj.getJSONArray("result");

                  //  List<PlayerIdModel> aList = new ArrayList<PlayerIdModel>();
                  //  dataArray=result3.getJSONArray("students");
                   // Log.v("arrayformat:", "array elements are"+dataArray);

                    // looping through All Contacts
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject c = result.getJSONObject(i);

                       /* String id = c.getString("_id");
                        String name = c.getString("name");
                        String email = c.getString("email");
                        String phone = c.getString("phone");
                        String address = c.getString("address");
                        String lattidue = c.getString("latitude");
                        String longitude = c.getString("longitude");
                        String blood_group = c.getString("blood_group");
                        String available = c.getString("available");



                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value

                        contact.put("name", name);
                        contact.put("email", email);
                        contact.put("mobile", phone);
                        contact.put("bloodgroup", blood_group);
                        contact.put("address", address);
                        contact.put("lattidue", lattidue);
                        contact.put("longitude", longitude);
                        contact.put("available", available);
                        contact.put("id", id);*/

                        // adding contact to contact list
                      //  contactList.add(contact);
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
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */

        }

    }

    private class SendNotifiaction extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            for (int i = 0; i < result.length(); i++) {
                try {
                    String jsonResponse;

                    URL url = new URL("https://onesignal.com/api/v1/notifications");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setUseCaches(false);
                    con.setDoOutput(true);
                    con.setDoInput(true);

                    con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    con.setRequestProperty("Authorization", "Basic NzY3OWM3MzctNTY3ZS00ZTI4LWE0MzctMzM5ODNhYjdkNmJk");
                    con.setRequestMethod("POST");


                    String[] IDS = new String[result.length()];


                    playerIdModel = new PlayerIdModel();
                   JSONObject jsonobject = result.getJSONObject(i);

                    playerIdModel.setPlayrid(jsonobject.getString("player_id"));
                    Log.v("tag", "player modal of , " + i + " is " + playerIdModel);
                    player_id = playerIdModel.getPlayrid();
                    IDS[i] = playerIdModel.getPlayrid();
                    Log.v("tag", "playerid  array is " + IDS[i]);


                    Log.v("tag", "playerid array content is " + Arrays.toString(IDS));
                    strJsonBody = "{"
                            + "\"app_id\": \"01f36775-1684-4feb-ab9d-25e52ec6e81e\","
                            + "\"include_player_ids\": [\"" + player_id + "\"],"
                            + "\"data\": {\"foo\": \"bar\"},"
                            + "\"contents\": {\"en\": \""+mNotificationContent.getEditableText().toString()+"\"}"
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
            }
            return null;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getContext(), "Notification sent to all users", Toast.LENGTH_LONG).show();
        }
    }

}