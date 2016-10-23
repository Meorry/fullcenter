package cn.ucai.fullcenter.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fullcenter.R;
import cn.ucai.fullcenter.utils.MFGT;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.etUserName)
    EditText metUserName;
    @BindView(R.id.etPassWord)
    EditText metPassWord;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
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
                break;
            case R.id.btnRegister:
                MFGT.gotoRegisterActivity(this);
                break;
        }
    }
}
