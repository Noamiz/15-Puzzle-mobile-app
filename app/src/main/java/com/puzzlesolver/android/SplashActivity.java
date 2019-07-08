package com.puzzlesolver.android;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class SplashActivity extends AppCompatActivity {

    private long SPLASH_DISPLAY_LENGTH = 3000;

    TextView tv_appname;
    ImageView iv_applogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tv_appname = (TextView)findViewById(R.id.tv_appname);
        iv_applogo = (ImageView) findViewById(R.id.iv_applogo);

        //Animations...
        YoYo.with(Techniques.BounceInUp).duration(2000).repeat(0).playOn(findViewById(R.id.iv_applogo));
        YoYo.with(Techniques.BounceInDown).duration(2000).repeat(0).playOn(findViewById(R.id.tv_appname));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
