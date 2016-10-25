package cn.ucai.fullcenter.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fullcenter.FuLiCenterApplication;
import cn.ucai.fullcenter.R;
import cn.ucai.fullcenter.bean.User;
import cn.ucai.fullcenter.sqlDataDao.SharedPreferencesUtils;
import cn.ucai.fullcenter.utils.ImageLoader;
import cn.ucai.fullcenter.utils.MFGT;

public class PersonalCenterActivity extends BaseActivity {

    @BindView(R.id.tv_user_avatar_image)
    ImageView mtvUserAvatarImage;
    @BindView(R.id.tv_user_username)
    TextView mtvUserUsername;
    @BindView(R.id.tv_user_usernick)
    TextView mtvUserUsernick;
    PersonalCenterActivity mContext;
    User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_personal_center);
        ButterKnife.bind(this);
        mContext = this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
       user = FuLiCenterApplication.getUser();
        if(user!=null){
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user),mContext,mtvUserAvatarImage);
            mtvUserUsername.setText(user.getMuserName());
            mtvUserUsernick.setText(user.getMuserNick());
        }else{
            finish();
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    @OnClick({R.id.backClickArea, R.id.rl_personal_center_avatar, R.id.rl_personal_center_username, R.id.rl_personal_center_usernick, R.id.bt_back_personal_center})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backClickArea:
                MFGT.finish(this);
                break;
            case R.id.rl_personal_center_avatar:
                break;
            case R.id.rl_personal_center_username:
                break;
            case R.id.rl_personal_center_usernick:
                break;
            case R.id.bt_back_personal_center:
                logouet();
                break;
        }
    }

    private void logouet() {
        if(user!=null){
            SharedPreferencesUtils.getInstance(mContext).removeUser();
            FuLiCenterApplication.setUser(null);
            MFGT.gotoLoginActivity(mContext);
        }
    }
}
