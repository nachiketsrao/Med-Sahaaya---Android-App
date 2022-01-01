package studio.knowhere.bloodbank.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import studio.knowhere.bloodbank.Class.HttpHandler;
import studio.knowhere.bloodbank.R;

import static android.content.ContentValues.TAG;

public class SearchActivity extends AppCompatActivity {


    Button mSearch;
    String Blooditem,Cityitem,Areaitem;
    ListView lv;
    ArrayList<HashMap<String, String>> contactList;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mSearch = (Button) findViewById(R.id.search_btn_id);
        lv = (ListView) findViewById(R.id.id_list);


        Initspin();


        contactList = new ArrayList<>();



        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetDonars().execute();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView <?> parent, View view, int position, long id) {

                Object SelctedItem  = parent.getItemAtPosition(position);
                String value  = String.valueOf(parent.getItemAtPosition(position));


                // Toast.makeText(getContext(), "item is"+SelctedItem+value, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SearchActivity.this, IndividualProfileActivity.class);
                intent.putExtra("KEY",value);
                startActivity(intent);

            }
        });

    }

    public void Initspin(){

        //initialise spinner
        Spinner mCITY = (Spinner) findViewById(R.id.spinner_city);
        Spinner mBLOODGROUP = (Spinner) findViewById(R.id.spinner_blood_grp);
        Spinner mArea = (Spinner) findViewById(R.id.spinner_area);

        mBLOODGROUP.setOnItemSelectedListener(new BloodSpinner());
        mCITY.setOnItemSelectedListener(new CitySpinner());
        mArea.setOnItemSelectedListener(new AreaSpinner());

        List<String> bloodgroup = new ArrayList<>();
        bloodgroup.add("Blood group");
        bloodgroup.add("A positive");
        bloodgroup.add("A negative");
        bloodgroup.add("B positive");
        bloodgroup.add("B negative");
        bloodgroup.add("AB positive");
        bloodgroup.add("AB negative");
        bloodgroup.add("0 positive");
        bloodgroup.add("0 negative");

        List<String> City = new ArrayList<>();
        City.add("CITY");
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
        mBLOODGROUP.setAdapter(bloodadapter);

        ArrayAdapter<String> cityadapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,City);
        cityadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCITY.setAdapter(cityadapter);

        ArrayAdapter<String> areaadapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,Area);
        areaadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mArea.setAdapter(areaadapter);

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



    private class GetDonars extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(SearchActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall("http://18.216.147.252:3002/user?blood_group="+Blooditem+"&area="+Areaitem+"&city="+Cityitem+"");

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
                        contact.put("id", id);
                        contact.put("available", available);

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
                    SearchActivity.this, contactList,
                    R.layout.list_item,  new String[]{"name", "available",
                    "email","mobile","bloodgroup","lattidue","longitude"}, new int[]{R.id.name,
                    R.id.availabilty_id, R.id.email,R.id.mobile, R.id.bloodgroup,R.id.lattitude,R.id.longitude});


            lv.setAdapter(adapter);
        }

    }
}
