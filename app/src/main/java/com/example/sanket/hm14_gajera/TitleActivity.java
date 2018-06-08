package com.example.sanket.hm14_gajera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static com.example.sanket.hm14_gajera.Assets.score;


public class TitleActivity extends Activity implements View.OnClickListener{
    Button play,Highscore;
    Bitmap bmp;
    Paint paint;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        paint=new Paint();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Highscore=(Button)findViewById(R.id.highscore);
        play=(Button)findViewById(R.id.play);
        play.setOnClickListener(this);
        Highscore.setOnClickListener(this);
        // setContentView(new MyView(this));
        // bmp = null;


    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.play:
                startActivity(new Intent(TitleActivity.this, MainActivity.class));
                score=0;
                break;

            case R.id.highscore:
                startActivity(new Intent(TitleActivity.this,PrefsActivity.class));
                break;


        }

    }

}


