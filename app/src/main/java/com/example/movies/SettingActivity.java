package com.example.movies;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by لا اله الا الله on 19/12/2015.
 */
public class SettingActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.sharedpref);
    }
}
