package cn.ucai.fullcenter.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.ucai.fullcenter.R;
import cn.ucai.fullcenter.utils.MFGT;


public class SpflashActivity extends AppCompatActivity {
    static final long SLEEP_TIME=2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spflash);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MFGT.gotoMainActivity(SpflashActivity.this);
                finish();
            }
        },SLEEP_TIME);
    }
}
