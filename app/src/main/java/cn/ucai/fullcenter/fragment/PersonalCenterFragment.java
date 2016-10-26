package cn.ucai.fullcenter.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fullcenter.FuLiCenterApplication;
import cn.ucai.fullcenter.R;
import cn.ucai.fullcenter.activity.MainActivity;
import cn.ucai.fullcenter.bean.MessageBean;
import cn.ucai.fullcenter.bean.Result;
import cn.ucai.fullcenter.bean.User;
import cn.ucai.fullcenter.netDao.NetDao;
import cn.ucai.fullcenter.netDao.OkHttpUtils;
import cn.ucai.fullcenter.sqlDataDao.UserDao;
import cn.ucai.fullcenter.utils.ImageLoader;
import cn.ucai.fullcenter.utils.L;
import cn.ucai.fullcenter.utils.MFGT;
import cn.ucai.fullcenter.utils.ResultUtils;

public class PersonalCenterFragment extends BaseFragment {
    private static final String TAG = PersonalCenterFragment.class.getSimpleName();
    @BindView(R.id.iv_user_avatar)
    ImageView mivUserAvatar;
    @BindView(R.id.tv_user_name)
    TextView mtvUserName;
    User user;
    MainActivity mContext;
    @BindView(R.id.tv_collect_goods)
    TextView mtvCollectGoods;
    @BindView(R.id.tv_collect_shop)
    TextView mtvCollectShop;
    @BindView(R.id.tv_my_footer)
    TextView mtvMyFooter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_personal_center, container, false);
        ButterKnife.bind(this, layout);
        mContext = (MainActivity) getContext();
        super.onCreateView(inflater, container, savedInstanceState);
        return layout;
    }

    @Override
    protected void initView() {
        User user = FuLiCenterApplication.getUser();
        L.e(TAG, "user=" + user);
        if (user == null) {
            MFGT.gotoLoginActivity(mContext);
        } else {
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), mContext, mivUserAvatar);
            mtvUserName.setText(user.getMuserName());
        }
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onResume() {
        super.onResume();
        user = FuLiCenterApplication.getUser();
        if (user != null) {
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), mContext, mivUserAvatar);
            mtvUserName.setText(user.getMuserName());
            synInfoUser();
            getCollectGoodsCount();
        }
    }

    @OnClick({R.id.bt_user_setting, R.id.rl_user_personal})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_user_setting:
                MFGT.gotoPersonalCenterActivity(mContext);
                break;
            case R.id.rl_user_personal:
                MFGT.gotoPersonalCenterActivity(mContext);
                break;
        }
    }

    private void synInfoUser() {
        NetDao.updateInfoByUsername(mContext, user.getMuserName(), new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                Result result = ResultUtils.getResultFromJson(s, User.class);
                if (result != null) {
                    User u = (User) result.getRetData();
                    if (u.equals(user)) {
                        UserDao dao = new UserDao(mContext);
                        boolean b = dao.saveUser(u);
                        if (b) {
                            FuLiCenterApplication.setUser(u);
                            user = u;
                            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), mContext, mivUserAvatar);
                            mtvUserName.setText(user.getMuserName());
                        }
                    }
                }
            }

            @Override
            public void onError(String error) {
                L.e(TAG, "error=" + error);
            }
        });
    }

    private void getCollectGoodsCount() {
        NetDao.getCollectGoodsCount(mContext, user.getMuserName(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean result) {
                if (result != null && result.isSuccess()) {
                    mtvCollectGoods.setText(result.getMsg());
                } else {
                    mtvCollectGoods.setText(String.valueOf(0));
                }
            }

            @Override
            public void onError(String error) {
                mtvCollectGoods.setText(String.valueOf(0));
                L.e(TAG, "error=" + error);
            }
        });
    }

    @OnClick({R.id.line_collect_goods, R.id.line_collect_shop, R.id.line_my_footer})
    public void onCellectClick(View view) {
        switch (view.getId()) {
            case R.id.line_collect_goods:
                MFGT.gotoCollectGoodsActivity(mContext);
                break;
            case R.id.line_collect_shop:
                break;
            case R.id.line_my_footer:
                break;
        }
    }
}
