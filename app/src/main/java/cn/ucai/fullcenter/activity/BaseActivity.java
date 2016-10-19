package cn.ucai.fullcenter.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.ucai.fullcenter.utils.MFGT;

/**
 * Created by Administrator on 2016/10/19.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
        setListener();
    }

    protected abstract void initView();
    protected abstract void initData();
    protected abstract void setListener();

    public void onBackPressed(){
        MFGT.finish(this);
    }
}
