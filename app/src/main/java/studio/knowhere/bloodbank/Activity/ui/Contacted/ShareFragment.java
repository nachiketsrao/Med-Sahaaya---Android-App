package studio.knowhere.bloodbank.Activity.ui.Contacted;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import studio.knowhere.bloodbank.Activity.APIs;
import studio.knowhere.bloodbank.Class.HttpHandler;
import studio.knowhere.bloodbank.Class.PreferenceManager;
import studio.knowhere.bloodbank.R;

import static android.content.ContentValues.TAG;

public class ShareFragment extends Fragment {

    private ShareViewModel shareViewModel;
    APIs apiList;
    String Jsonstatus,id;
    String userid;
    ProgressDialog pDialog;
    ListView mListview;
    ArrayList<HashMap<String, String>> contactList;
    PreferenceManager preferenceManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_share, container, false);

        preferenceManager = new PreferenceManager(getActivity());
        mListview = (ListView) root.findViewById(R.id.list_users_contacted);
        apiList = new APIs();
        contactList = new ArrayList<>();
        new GetUserContactedLists().execute();



        return root;
    }

    private class GetUserContactedLists extends AsyncTask<Void, Void, Void> {

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
            String jsonStr = sh.makeServiceCall(apiList.getDonarComm()+preferenceManager.getKeyUserid(id));

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray result = jsonObj.getJSONArray("result");

                    // looping through All Contacts
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject c = result.getJSONObject(i);

                        String id = c.getString("_id");
                        String typeofcomm = c.getString("type_of_communication");
                        JSONObject donar = c.getJSONObject("donot_id");
                        String name = donar.getString("name");
                        String email = donar.getString("email");
                        String phone = donar.getString("phone");
                        String blood_group = donar.getString("blood_group");

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value

                        contact.put("name", name);
                        contact.put("email", email);
                        contact.put("mobile", phone);
                        contact.put("bloodgroup", blood_group);
                        contact.put("typeofcomm", typeofcomm);


                        // adding contact to contact list
                        contactList.add(contact);
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
            ListAdapter adapter = new SimpleAdapter(
                    getContext(), contactList,
                    R.layout.list_item_contacted, new String[]{"name", "email",
                    "mobile","bloodgroup","typeofcomm"}, new int[]{R.id.name,
                    R.id.email, R.id.mobile, R.id.bloodgroup,R.id.typeofcomm});

            mListview.setAdapter(adapter);
        }

    }

}