package cn.ucai.fullcenter.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fullcenter.I;
import cn.ucai.fullcenter.R;
import cn.ucai.fullcenter.bean.Result;
import cn.ucai.fullcenter.bean.User;
import cn.ucai.fullcenter.netDao.NetDao;
import cn.ucai.fullcenter.netDao.OkHttpUtils;
import cn.ucai.fullcenter.utils.CommonUtils;
import cn.ucai.fullcenter.utils.L;
import cn.ucai.fullcenter.utils.MFGT;

public class LoginActivity extends BaseActivity {
    private static final String TAG=LoginActivity.class.getSimpleName();
    @BindView(R.id.etUserName)
    EditText metUserName;
    @BindView(R.id.etPassWord)
    EditText metPassWord;
    String username;
    String password;
    LoginActivity mcontext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mcontext = this;
        super.onCreate(savedInstanceState);


    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    @OnClick({R.id.btnLogin, R.id.btnRegister})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                checkedInput();
                break;
            case R.id.btnRegister:
                MFGT.gotoRegisterActivity(this);
                break;
        }
    }

    private void checkedInput() {
        username = metUserName.getText().toString().trim();
        password = metPassWord.getText().toString().trim();
        if (TextUtils.isEmpty(username)){
            CommonUtils.showShortToast(R.string.user_name_connot_be_empty);
            metUserName.requestFocus();
            return;
        }else if (TextUtils.isEmpty(password)){
            CommonUtils.showShortToast(R.string.password_connot_be_empty);
            metPassWord.requestFocus();
            return;
        }
        login();
    }

    private void login() {
        final ProgressDialog pd=new ProgressDialog(mcontext);
        pd.setMessage(getResources().getString(R.string.logining));
        pd.show();
        L.e(TAG,"username="+username+"password="+password);
        NetDao.login(mcontext, username, password, new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                pd.dismiss();
                L.e(TAG,"result="+result);
                if (result==null){
                    CommonUtils.showShortToast(R.string.login_fail);
                }else {
                    if (result.isRetMsg()){
                       User user = (User) result.getRetData();
                        L.d(TAG,"user="+user);
                        MFGT.finish(mcontext);
                    }else {
                        if (result.getRetCode()==I.MSG_LOGIN_UNKNOW_USER){
                            CommonUtils.showLongToast(R.string.login_fail_unknow_user);
                        }else if (result.getRetCode()==I.MSG_LOGIN_ERROR_PASSWORD){
                            CommonUtils.showLongToast(R.string.login_fail_error_password);
                        }else {
                            CommonUtils.showLongToast(R.string.login_fail);
                        }
                    }
                }

            }

            @Override
            public void onError(String error) {
                CommonUtils.showShortToast(error);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == I.REQUEST_CODE_REGISTER){
            String name=data.getStringExtra(I.User.USER_NAME);
            metUserName.setText(name);
        }
    }
}
