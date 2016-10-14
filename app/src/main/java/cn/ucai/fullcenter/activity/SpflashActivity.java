package cn.ucai.fullcenter.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.ucai.fullcenter.R;


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
        new Thread(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                long cost_time=System.currentTimeMillis()-start;
                if(SLEEP_TIME-cost_time>0){
                    try {
                        Thread.sleep(SLEEP_TIME-cost_time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                startActivity(new Intent(SpflashActivity.this,MainActivity.class));
                finish();
            }
        }).start();
    }
}
