package studio.knowhere.bloodbank.Activity.ui.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import studio.knowhere.bloodbank.Activity.APIs;
import studio.knowhere.bloodbank.Activity.IndividualProfileActivity;
import studio.knowhere.bloodbank.Class.HttpHandler;
import studio.knowhere.bloodbank.Class.PreferenceManager;
import studio.knowhere.bloodbank.R;

import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    ListView mListview;
    PreferenceManager preferenceManager;
    APIs apiList;
    String Jsonstatus;
    String userid;
    ProgressDialog pDialog;
    ArrayList<HashMap<String, String>> contactList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mListview = (ListView) root.findViewById(R.id.list_users);
        apiList = new APIs();
        contactList = new ArrayList<>();
        new GetUserLists().execute();

        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView <?> parent, View view, int position, long id) {

                Object SelctedItem  = parent.getItemAtPosition(position);
                String value  = String.valueOf(parent.getItemAtPosition(position));
                ArrayList<String> arrayListvalue = new ArrayList<>(Arrays.asList(value.split(",")));
                ArrayList<String> arrayListAdress = new ArrayList<>(Arrays.asList(arrayListvalue.get(1).split("=")));
              //  mAddress.setText(arrayListAdress.get(1));
                System.out.println("values of adress" + arrayListAdress.get(1));
                ArrayList<String> arrayListvalueofId = new ArrayList<>(Arrays.asList(arrayListvalue.get(2).split("=")));
             //   finalID = arrayListvalueofId.get(1);
                System.out.println("values of id" + arrayListvalueofId.get(1));
                ArrayList<String> arrayListLongitude = new ArrayList<>(Arrays.asList(arrayListvalue.get(3).split("=")));
              //  finalLong = arrayListLongitude.get(1);
                System.out.println("values of long" + arrayListLongitude.get(1));
                ArrayList<String> arrayListMobile = new ArrayList<>(Arrays.asList(arrayListvalue.get(4).split("=")));
              //  mMobileNum.setText(arrayListMobile.get(1));
                System.out.println("values of mobile" + arrayListMobile.get(1));
                ArrayList<String> arrayListEmail = new ArrayList<>(Arrays.asList(arrayListvalue.get(5).split("=")));
               // mEmail.setText(arrayListEmail.get(1));
                System.out.println("values of email" + arrayListEmail.get(1));
                ArrayList<String> arrayListName = new ArrayList<>(Arrays.asList(arrayListvalue.get(6).split("=")));
               // mName.setText(arrayListName.get(1));
                System.out.println("values of name" + arrayListName.get(1));
                ArrayList<String> arrayListLattitude = new ArrayList<>(Arrays.asList(arrayListvalue.get(8).split("=")));
               // finalLat = arrayListLattitude.get(1);
                System.out.println("values is" + value);
                System.out.println("values of lat" + arrayListLattitude.get(1));
                ArrayList<String> arrayListAailabilty = new ArrayList<>(Arrays.asList(arrayListvalue.get(7).split("=")));
                String Availability = arrayListAailabilty.get(1);
                System.out.println("values of Availability" + arrayListAailabilty.get(1));

                // Toast.makeText(getContext(), "item is"+SelctedItem+value, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), IndividualProfileActivity.class);
                intent.putExtra("NAME",arrayListName.get(1));
                intent.putExtra("LATTITUDE",arrayListLattitude.get(1));
                intent.putExtra("LONGITUDE",arrayListLongitude.get(1));
                intent.putExtra("EMAIL",arrayListEmail.get(1));
                intent.putExtra("NUMBER",arrayListMobile.get(1));
                intent.putExtra("ADRESS",arrayListAdress.get(1));
                intent.putExtra("ID",arrayListvalueofId.get(1));
                startActivity(intent);

            }
        });

        return root;
    }
    private class GetUserLists extends AsyncTask<Void, Void, Void> {

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
                    JSONArray result = jsonObj.getJSONArray("result");

                    // looping through All Contacts
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject c = result.getJSONObject(i);

                        String id = c.getString("_id");
                        String name = c.getString("name");
                        String email = c.getString("email");
                        String phone = c.getString("phone");
                        String address = c.getString("address");
                        String lattidue = c.getString("latitude");
                        String longitude = c.getString("longitude");
                        String blood_group = c.getString("blood_group");
                        String available = c.getString("available");
                        String plasma_donor = c.getString("plasma_donor");




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
                        contact.put("id", id);
                        contact.put("plasma_donor", plasma_donor);

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
                    R.layout.list_item, new String[]{"name", "available",
                    "email","mobile","bloodgroup","lattidue","longitude","plasma_donor"}, new int[]{R.id.name,
                    R.id.availabilty_id, R.id.email,R.id.mobile, R.id.bloodgroup,R.id.lattitude,R.id.longitude,R.id.plasma_donor});

            mListview.setAdapter(adapter);
        }

    }

}