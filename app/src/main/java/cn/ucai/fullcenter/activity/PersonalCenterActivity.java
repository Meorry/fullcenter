package cn.ucai.fullcenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fullcenter.FuLiCenterApplication;
import cn.ucai.fullcenter.I;
import cn.ucai.fullcenter.R;
import cn.ucai.fullcenter.bean.Result;
import cn.ucai.fullcenter.bean.User;
import cn.ucai.fullcenter.netDao.NetDao;
import cn.ucai.fullcenter.netDao.OkHttpUtils;
import cn.ucai.fullcenter.sqlDataDao.SharedPreferencesUtils;
import cn.ucai.fullcenter.utils.CommonUtils;
import cn.ucai.fullcenter.utils.ImageLoader;
import cn.ucai.fullcenter.utils.L;
import cn.ucai.fullcenter.utils.MFGT;
import cn.ucai.fullcenter.utils.OnSetAvatarListener;
import cn.ucai.fullcenter.utils.ResultUtils;

public class PersonalCenterActivity extends BaseActivity {
    private static final String TAG = PersonalCenterActivity.class.getSimpleName();
    @BindView(R.id.tv_user_avatar_image)
    ImageView mtvUserAvatarImage;
    @BindView(R.id.tv_user_username)
    TextView mtvUserUsername;
    @BindView(R.id.tv_user_usernick)
    TextView mtvUserUsernick;
    PersonalCenterActivity mContext;
    User user;
    OnSetAvatarListener mOnSetAvatarListener;

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
      if(user==null){
          finish();
          return;
      }
        updatePersonalCenter();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePersonalCenter();
    }

    @OnClick({R.id.backClickArea, R.id.rl_personal_center_avatar, R.id.rl_personal_center_username, R.id.rl_personal_center_usernick, R.id.bt_back_personal_center})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backClickArea:
                MFGT.finish(this);
                break;
            case R.id.rl_personal_center_avatar:
                mOnSetAvatarListener = new OnSetAvatarListener(mContext,R.id.layout_personalCenter,user.getMuserName(), I.AVATAR_TYPE_USER_PATH);
                break;
            case R.id.rl_personal_center_username:
                CommonUtils.showLongToast(R.string.user_name_connot_be_write);
                break;
            case R.id.rl_personal_center_usernick:
                MFGT.gotoUpdateUserNickActivity(mContext);
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
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.e(TAG,"requestCode="+requestCode);
        if(resultCode!=RESULT_OK){
            return;
        }
        mOnSetAvatarListener.setAvatar(requestCode,data,mtvUserAvatarImage);
        if(requestCode == OnSetAvatarListener.REQUEST_CROP_PHOTO){
            L.e("ssssssssssssssss");
            updateAvatarIamge();
        }
    }

    private void updateAvatarIamge() {
        //storage/emulated/0/Android/data/cn.ucai.fullcenter/files/Pictures/hahaha/ljy1234.jpg
        File file = new File(OnSetAvatarListener.getAvatarPath(mContext,user.getMavatarPath()+ "/"
                + user.getMuserName() + I.AVATAR_SUFFIX_JPG));
        L.e(TAG,"file="+file.exists());
        L.e(TAG,"file="+file.getAbsolutePath());
        NetDao.updateUserAvatar(mContext, user.getMuserName(), file, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                L.e(TAG,"s="+s);
                Result result = ResultUtils.getResultFromJson(s,User.class);
                L.e(TAG,"result="+result);
                if(result==null){
                   CommonUtils.showLongToast(R.string.update_user_avater_iamge_fail);
                }else {
                    User u = (User) result.getRetData();
                    if(result.isRetMsg()){
                        FuLiCenterApplication.setUser(u);
                       ImageLoader.setAvatar(ImageLoader.getAvatarUrl(u),mContext,mtvUserAvatarImage);
                        CommonUtils.showLongToast(R.string.update_user_avater_iamge_sucess);
                    }else {
                        CommonUtils.showLongToast(R.string.update_user_avater_iamge_fail);
                    }
                }
            }

            @Override
            public void onError(String error) {
              L.e(TAG,"error="+error);
            }
        });
    }

    private void updatePersonalCenter() {
        user = FuLiCenterApplication.getUser();
        if(user!=null){
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user),mContext,mtvUserAvatarImage);
            mtvUserUsername.setText(user.getMuserName());
            mtvUserUsernick.setText(user.getMuserNick());
        }
    }

}
