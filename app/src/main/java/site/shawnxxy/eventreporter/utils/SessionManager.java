package site.shawnxxy.eventreporter.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

import site.shawnxxy.eventreporter.activity.MainActivity;

/**
 * Created by ShawnX on 10/4/17.
 */

public class SessionManager {
    /**
     *  SET UP
     */
    // Shared Preferences
    SharedPreferences pref;
    // Editor for shared preferences
    Editor editor;
    // Context
    Context context;
    // shared pref mode
    int PRIVATE_MODE = 0;
    // SharedPref file name
    private static final String PREF_NAME = "AndroidHivePref";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "username";
    public static final String KEY_PASSWORD = "password";
    // Constructor
    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     *  Create Login session
     */
    public void createLoginSession(String username, String password) {
        // Store login value
        editor.putBoolean(IS_LOGIN, true);
        // Store name if pref
        editor.putString(KEY_NAME, username);
        editor.putString(KEY_PASSWORD, password);
        // Commit changes
        editor.commit();
    }

    /**
     *  Check login method
     *  check user login status
     *  if false, will redirect user to login page
     */
    public void checkLogin() {
        if (!this.isLoggedIn()) {
            Intent i = new Intent(context, MainActivity.class);
            // Closing all activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Add new flag to start new activity
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            // Start login
            context.startActivity(i);
        }
    }
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    /**
     *  Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
//        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
        return user;
    }

    /**
     *  Clear session
     */
    public void logoutUser() {
        // Clearing all data from Shared preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(context, MainActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Staring Login Activity
        context.startActivity(i);
    }
}
