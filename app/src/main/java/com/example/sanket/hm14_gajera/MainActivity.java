//Sanket Gajera
//L20374308

package com.example.sanket.hm14_gajera;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {
    MainView v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Disable the title
        //requestWindowFeature (Window.FEATURE_NO_TITLE);  // use the styles.xml file to set no title bar
        // Make full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Start the view
        v = new MainView(this);
        setContentView(v);
    }

    @Override
    protected void onPause () {
        super.onPause();
        v.pause();
    }

    @Override
    protected void onResume () {
        super.onResume();
        v.resume();
        final SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor=prefs.edit();
        editor.putInt("key_highscores",Assets.highscore);
        editor.commit();
        Assets.highscore=prefs.getInt("key_highscores",0);
    }
}
