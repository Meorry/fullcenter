package cn.ucai.fullcenter.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import cn.ucai.fullcenter.R;
import cn.ucai.fullcenter.utils.L;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        L.i("MainActivity onCreate");
    }

    public void onCheckedChange(View v){}
}
