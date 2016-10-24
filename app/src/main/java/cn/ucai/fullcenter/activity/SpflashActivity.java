package cn.ucai.fullcenter.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.ucai.fullcenter.FuLiCenterApplication;
import cn.ucai.fullcenter.R;
import cn.ucai.fullcenter.bean.User;
import cn.ucai.fullcenter.sqlDataDao.SharedPreferencesUtils;
import cn.ucai.fullcenter.sqlDataDao.UserDao;
import cn.ucai.fullcenter.utils.L;
import cn.ucai.fullcenter.utils.MFGT;


public class SpflashActivity extends AppCompatActivity {
    static final long SLEEP_TIME=2000;
    private static final  String TAG = SpflashActivity.class.getSimpleName();
    SpflashActivity mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spflash);
        mContext = this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                User user = FuLiCenterApplication.getUser();
                L.e(TAG,"FuLiCenterApplication,user= "+user);
                String username = SharedPreferencesUtils.getInstance(mContext).getUser();
                L.e(TAG,"SharedPreferencesUtils,username= "+username);
                if(user==null && username!=null) {
                    UserDao dao = new UserDao(mContext);
                    user = dao.getUser(username);
                    L.e(TAG,"database,user= "+user);
                    if(username!=null){
                        FuLiCenterApplication.setUser(user);
                    }
                }
                MFGT.gotoMainActivity(SpflashActivity.this);
                finish();
            }
        },SLEEP_TIME);
    }
}
