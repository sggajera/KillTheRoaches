package com.example.sanket.hm14_gajera;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;


public class PrefsFragmentSettings extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    public PrefsFragmentSettings () {
    }
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preference from an XML resource
        addPreferencesFromResource(R.xml.prefs_fragment_settings);
    }
    @Override
    public void onResume () {
        super.onResume();
        // Set up a listener when a key changes
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);



        // Set up a click listener for company info
        Preference pref;
        pref = getPreferenceScreen().findPreference("key_company_info");
        pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick (Preference preference) {
                // Handle action on click here
                try {
                    Uri site = Uri.parse("https://www.stlzoo.org/animals/abouttheanimals/listallanimals/");
                    Intent intent = new Intent(Intent.ACTION_VIEW, site);
                    startActivity(intent);
                }
                catch (Exception e) {
                    Log.e("PrefsFragmentSettings", "Browser failed", e);
                }
                return true;
            }
        });
        if(Assets.highscore>Assets.score){

            pref = getPreferenceScreen().findPreference("key_highscore");


            String s=""+Assets.highscore;
            pref.setSummary(s);}
        else{
            pref = getPreferenceScreen().findPreference("key_highscore");

            String s=""+Assets.score;
            pref.setSummary(s);}




    }
    public void onSharedPreferenceChanged (SharedPreferences sharedPreferences, String key) {
        if (key.equals("key_music_enabled")) {
            boolean b = sharedPreferences.getBoolean("key_music_enabled", true);

        }
    }



}
