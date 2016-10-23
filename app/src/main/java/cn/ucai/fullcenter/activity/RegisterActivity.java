package cn.ucai.fullcenter.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fullcenter.I;
import cn.ucai.fullcenter.R;
import cn.ucai.fullcenter.bean.Result;
import cn.ucai.fullcenter.netDao.NetDao;
import cn.ucai.fullcenter.netDao.OkHttpUtils;
import cn.ucai.fullcenter.utils.CommonUtils;
import cn.ucai.fullcenter.utils.MFGT;
import cn.ucai.fullcenter.views.DisplayUtils;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.username)
    EditText mUsername;
    @BindView(R.id.nick)
    EditText mNick;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.confirm_password)
    EditText mConfirmPassword;
    @BindView(R.id.activity_register)
    LinearLayout mActivityRegister;

    RegisterActivity mcontext;
    String username;
    String nick;
    String password;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mcontext=this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        DisplayUtils.initBackWithTitle(this,"用户注册");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    @OnClick(R.id.btn_register)
    public void onClick() {
        username = mUsername.getText().toString().trim();
        nick = mNick.getText().toString().trim();
        password = mPassword.getText().toString().trim();
        String confirmPassword = mConfirmPassword.getText().toString().trim();
        if(TextUtils.isEmpty(username)){
            CommonUtils.showShortToast(R.string.user_name_connot_be_empty);
            mUsername.requestFocus();
            return;
        }else if(!username.matches("[a-zA-Z]\\w{5,15}")){
            CommonUtils.showShortToast(R.string.illegal_user_name);
            mUsername.requestFocus();
            return;
        }else if(TextUtils.isEmpty(nick)){
            CommonUtils.showShortToast(R.string.nick_name_connot_be_empty);
            mNick.requestFocus();
            return;
        }else if(TextUtils.isEmpty(password)){
            CommonUtils.showShortToast(R.string.confirm_password_connot_be_empty);
            mPassword.requestFocus();
            return;
        }else  if(TextUtils.isEmpty(confirmPassword)){
            CommonUtils.showShortToast(R.string.confirm_password_connot_be_empty);
            mConfirmPassword.requestFocus();
            return;
        }else if(!password.equals(confirmPassword)){
            CommonUtils.showShortToast(R.string.two_input_password);
            mConfirmPassword.requestFocus();
            return;
        }
        register();
    }

    private void register() {
       final  ProgressDialog pd = new ProgressDialog(mcontext);
        pd.setMessage(getResources().getString(R.string.registering));
        pd.show();
        NetDao.register(mcontext, username, nick, password, new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                pd.dismiss();
                if(result == null){
                    CommonUtils.showShortToast(R.string.register_fail);
                }else if(result.isRetMsg()){
                    CommonUtils.showLongToast(R.string.register_success);
                    setResult(RESULT_OK,new Intent().putExtra(I.User.USER_NAME,username));
                    MFGT.finish(mcontext);
                }else {
                    CommonUtils.showLongToast(R.string.register_fail_exists);
                    mUsername.requestFocus();
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }
}
