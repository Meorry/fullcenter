package cn.ucai.fullcenter.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.EditText;

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
import cn.ucai.fullcenter.sqlDataDao.UserDao;
import cn.ucai.fullcenter.utils.CommonUtils;
import cn.ucai.fullcenter.utils.L;
import cn.ucai.fullcenter.utils.MFGT;
import cn.ucai.fullcenter.utils.ResultUtils;

public class UpdateUserNickActivity extends BaseActivity {
    public static final String TAG = UpdateUserNickActivity.class.getSimpleName();
    UpdateUserNickActivity mContext;

    User user;
    @BindView(R.id.tv_update_usernick)
    EditText mtvUpdateUsernick;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_updater_user_nick);
        ButterKnife.bind(this);
        mContext = this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        user = FuLiCenterApplication.getUser();
        if (user != null) {
            mtvUpdateUsernick.setText(user.getMuserNick());
        } else {
            finish();
        }
    }

    @Override
    protected void setListener() {

    }

    @OnClick(R.id.bt_update_user_nick)
    public void updateNick() {
        final ProgressDialog pd = new ProgressDialog(mContext);
        pd.setMessage(getResources().getString(R.string.updatenick));
        pd.show();
        String nick = mtvUpdateUsernick.getText().toString();
        NetDao.updateUserNick(mContext, user.getMuserName(), nick, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                Result result = ResultUtils.getResultFromJson(s, User.class);
                L.e(TAG, "result=" + result);
                if (result == null) {
                    CommonUtils.showShortToast(R.string.update_faile);
                } else {
                    if (result.isRetMsg()) {
                        User u = (User) result.getRetData();
                        L.d(TAG, "user=" + user);
                        UserDao dao = new UserDao(mContext);
                        boolean isSuccess = dao.saveUser(u);
                        if (isSuccess) {
                            FuLiCenterApplication.setUser(u);
                            setResult(RESULT_OK);
                            MFGT.finish(mContext);
                        } else {
                            CommonUtils.showLongToast(R.string.user_datavase_error);
                        }
                    } else {
                        if (result.getRetCode() == I.MSG_USER_SAME_NICK) {
                            CommonUtils.showLongToast(R.string.update_nick_fail_unmotify);
                        } else if (result.getRetCode() == I.MSG_USER_UPDATE_NICK_FAIL) {
                            CommonUtils.showLongToast(R.string.update_faile);
                        } else {
                            CommonUtils.showLongToast(R.string.update_faile);
                        }
                    }
                }
                pd.dismiss();
            }

            @Override
            public void onError(String error) {
                pd.dismiss();
                CommonUtils.showShortToast(error);
                L.e(TAG,"error="+error);
            }
        });
        finish();
    }

}
