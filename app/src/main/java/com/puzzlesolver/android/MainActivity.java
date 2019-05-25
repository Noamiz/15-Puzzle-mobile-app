package com.puzzlesolver.android;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import es.dmoral.toasty.Toasty;


public class MainActivity extends AppCompatActivity {

    boolean mPressedOnce = false;
    LinearLayout sect_1,sect_2,sect_3,sect_4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RunnableAnimation();
        initAll();
    }

    private void initAll(){
        sect_1 = (LinearLayout) findViewById(R.id.sect_1);
        sect_1.setVisibility(View.INVISIBLE);
        sect_2 = (LinearLayout) findViewById(R.id.sect_2);
        sect_2.setVisibility(View.INVISIBLE);
        sect_3 = (LinearLayout) findViewById(R.id.sect_3);
        sect_3.setVisibility(View.INVISIBLE);
        sect_4 = (LinearLayout) findViewById(R.id.sect_4);
        sect_4.setVisibility(View.INVISIBLE);


        // Animation Sets
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sect_1.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.BounceInDown).duration(1000).repeat(0).playOn(findViewById(R.id.sect_1));
            }
        }, 500);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sect_2.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.BounceInDown).duration(1000).repeat(0).playOn(findViewById(R.id.sect_2));
            }
        }, 1500);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sect_3.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.BounceInDown).duration(1000).repeat(0).playOn(findViewById(R.id.sect_3));
            }
        }, 2500);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sect_4.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.BounceInUp).duration(1000).repeat(0).playOn(findViewById(R.id.sect_4));
            }
        }, 3500);
    }

    void RunnableAnimation(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                YoYo.with(Techniques.RubberBand).duration(500).repeat(0).playOn(findViewById(R.id.btnRun));
                RunnableAnimation();
            }
        }, 3000);
    }

    @Override
    public void onBackPressed() {
        // Add twice back press to exit here...
        if (mPressedOnce) {
            super.onBackPressed();
            finish();
            overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finishAffinity();
                    System.exit(0);
                }
            }, 500);
        }
        this.mPressedOnce = true;
        Toasty.warning(this, "Press BACK again to exit", Toast.LENGTH_SHORT, true).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                mPressedOnce = false;
            }
        }, 2000);
    }

    public void runBtnClick(View runBtn) {
        try {
            solve();
        }
        catch (InputException ie){
            showProblem(ie.prob);
        }

    }

    private void solve() throws InputException{
        UI_IO ips = new UI_IO(this);
        Solver s = new Solver(ips);
        s.solve();
    }

    private void showProblem(ProblemIdentifier prob){
        switch (prob) {
            case initBoard:
                Toasty.error(this, "A problem in the input of the initial board", Toast.LENGTH_LONG, true).show();
                break;
            case costHalf:
                Toasty.error(this, "A problem in the input of blocks of cost 0.5", Toast.LENGTH_LONG, true).show();
                break;
            case cost2:
                Toasty.error(this, "A problem in the input of blocks of cost 2", Toast.LENGTH_LONG, true).show();
                break;
            case someCost2cost05Same:
                Toasty.error(this, "Some of the blocks in cost 2 blocks' " +
                        "list are in cost 0.5 blocks' list", Toast.LENGTH_LONG, true).show();
                break;
        }
    }
}
