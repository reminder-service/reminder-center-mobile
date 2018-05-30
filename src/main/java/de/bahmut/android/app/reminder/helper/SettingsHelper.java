package de.bahmut.android.app.reminder.helper;

import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import de.bahmut.android.app.reminder.activities.SettingsActivity;

public class SettingsHelper {

    private static SharedPreferences sharedPreferences;

    private static SharedPreferences getSharedPreferences(final AppCompatActivity activity) {
        if (sharedPreferences == null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        }
        return sharedPreferences;
    }

    @SuppressWarnings("ConstantConditions")
    public static Uri getApiLocation(final AppCompatActivity activity) {
        return Uri.parse(getSharedPreferences(activity).getString(SettingsActivity.KEY_API_URI, "http://localhost/"));
    }

    public static String getApiUsername(final AppCompatActivity activity) {
        return getSharedPreferences(activity).getString(SettingsActivity.KEY_API_USERNAME, "");
    }

    public static String getApiPassword(final AppCompatActivity activity) {
        return getSharedPreferences(activity).getString(SettingsActivity.KEY_API_PASSWORD, "");
    }

}
