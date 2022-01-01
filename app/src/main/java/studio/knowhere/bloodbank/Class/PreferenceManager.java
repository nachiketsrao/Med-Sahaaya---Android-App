package studio.knowhere.bloodbank.Class;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class PreferenceManager {

    /* ABOUT PREFERENCES:

    Preferences is an Android lightweight mechanism to store and retrieve pairs of primitive data
    types (also called Maps, and Associative Arrays).
    In each entry of the form the key is a string and the value must be a primitive data type.
    PREFERENCES are typically used to keep state information and shared data among several activities of an application.

    Shared Preferences is the storage, in android, that you can use to store some basic things related
    to functionality, users' customization or its profile.
    Suppose you want to save user's name in your app for future purposes. You cant save such a little
    thing in database, So you better keep it saved in your Preferences. Preferences is just like a file
    , from which you can retrieve value anytime in application's lifetime in a KEY-VALUE pair manner.

    Whenever you clear data for any app, preferences are deleted.
     */

    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "DOAapp";

    // All Shared Preferences Keys

    private static final String KEY_MOBILE_NUMBER = "mobile_number";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_MOBILE = "mobile";

    private static final String KEY_PASSWORD = "password";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_IS_REGISTERED = "isregistered";
    private static final String KEY_NOMINEE_ID= "idnominee";
    private static final String KEY_USERID = "userid";
    private static final String KEY_ASSETID = "subid";
    private static final String KEY_NOM_ASSETID = "nomidasset";
    private static final String KEY_STATUS = "status";
    private static final String KEY_DATE = "date";

    private static final String KEY_USER_MOBILE = "user_mobile";
    private static final String KEY_VEHICLE_NUMBER = "vehicle_number";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAILID = "user_emailid";








    public PreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setKeyUserMobile(String user_mobile) {
        editor.putString(KEY_USER_MOBILE, user_mobile);
        editor.commit();
    }

    public void setKeyUserName(String user_name) {
        editor.putString(KEY_USER_NAME, user_name);
        editor.commit();
    }


    public void setKeyUserEmailid(String user_emailid) {
        editor.putString(KEY_USER_EMAILID, user_emailid);
        editor.commit();
    }
    public void setKeyVehicleNumber(String vehicle_number) {
        editor.putString(KEY_VEHICLE_NUMBER, vehicle_number);
        editor.commit();
    }
    public  String getKeyUserMobile() {
        return pref.getString(KEY_USER_MOBILE, null);
        //return KEY_USER_MOBILE;
    }

    public  String getKeyVehicleNumber() {
        return pref.getString(KEY_VEHICLE_NUMBER, null);
        // return KEY_VEHICLE_NUMBER;
    }

    public  String getKeyUserName() {
        return pref.getString(KEY_USER_NAME, null);
        // return KEY_VEHICLE_NUMBER;
    }

    public  String getKeyUserEmailid() {
        return pref.getString(KEY_USER_EMAILID, null);
        // return KEY_VEHICLE_NUMBER;
    }

    public  void setKeyDate(String date) {
        editor.putString(KEY_DATE, date);
        editor.commit();
    }

    public String getKeyDate(String date) {
        return pref.getString(KEY_DATE, null);
    }

    public  void setKeyUserid(String userid) {
        editor.putString(KEY_USERID, userid);
        editor.commit();
    }

    public String getKeyUserid(String id) {
        return pref.getString(KEY_USERID,null);
    }
    public  void setKeyStatus(String status) {
        editor.putString(KEY_STATUS, status);
        editor.commit();
    }
    public String getKeyStatus(String STATUS) {
        return pref.getString(KEY_STATUS,null);
    }

    public void setMobileNumber(String mobileNumber) {
        editor.putString(KEY_MOBILE_NUMBER, mobileNumber);
        editor.commit();
    }


    public  void setKeyAssetid(String idSub) {
        editor.putString(KEY_ASSETID, idSub);
        editor.commit();
    }

    public String getKeyAssetid(String idSub) {
        return pref.getString(KEY_ASSETID,null);
    }


    public  void setKeyNomAssetid(String nomidasset) {
        editor.putString(KEY_NOM_ASSETID, nomidasset);
        editor.commit();
    }
    public String getKeyNomAssetid(String asssetid) {
        return pref.getString(KEY_NOM_ASSETID,null);
    }



    ///////////setters id nominee
    public String getKeyNomineeId(String idnomine) {
        return pref.getString(KEY_NOMINEE_ID,null);
    }

    public void  setKeyNomineeId(String idnominee) {
        editor.putString(KEY_NOMINEE_ID, idnominee);
        editor.commit();
    }

    public void createLogin(String name, String email, String mobile, String location, String password) {
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_PASSWORD,password);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void clearSession() {
        editor.clear();
        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> profile = new HashMap<>();
        profile.put("name", pref.getString(KEY_NAME, null));
        profile.put("email", pref.getString(KEY_EMAIL, null));
        profile.put("mobile", pref.getString(KEY_MOBILE, null));
        profile.put("password",pref.getString(KEY_PASSWORD,null));
        return profile;
    }
    public void setKeyIsLoggedIn(String mobileNumber) {
        editor.putString(KEY_IS_LOGGED_IN, mobileNumber);
        editor.commit();
    }
    //retreiving the userid using key
    public String getKeyIsLoggedIn() {
        return pref.getString(KEY_IS_LOGGED_IN, null);
    }

    public void setKeyIsRegistered(String mobileNumber) {
        editor.putString(KEY_IS_REGISTERED, mobileNumber);
        editor.commit();
    }
    //retreiving the userid using key
    public String getKeyIsRegistered() {
        return pref.getString(KEY_IS_REGISTERED, null);
    }




}
