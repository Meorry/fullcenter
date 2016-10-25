package cn.ucai.fullcenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fullcenter.FuLiCenterApplication;
import cn.ucai.fullcenter.I;
import cn.ucai.fullcenter.R;
import cn.ucai.fullcenter.fragment.BoutiqueFragment;
import cn.ucai.fullcenter.fragment.CategoryFragment;
import cn.ucai.fullcenter.fragment.NewGoodsFragment;
import cn.ucai.fullcenter.fragment.PersonalCenterFragment;
import cn.ucai.fullcenter.utils.L;
import cn.ucai.fullcenter.utils.MFGT;

public class MainActivity extends BaseActivity {

    @BindView(R.id.layout_new_good)
    RadioButton layoutNewGood;
    @BindView(R.id.layout_boutique)
    RadioButton layoutBoutique;
    @BindView(R.id.layout_category)
    RadioButton layoutCategory;
    @BindView(R.id.layout_cart)
    RadioButton layoutCart;
    @BindView(R.id.layout_personal_center)
    RadioButton layoutPersonalCenter;
    @BindView(R.id.tvCartHint)
    TextView tvCartHint;

    int index = 0;
    int currentIndex;
    RadioButton[] radioButtons;
    Fragment[] fragments;
    NewGoodsFragment mNewGoodsFragment;
    BoutiqueFragment mBoutiqueFragment;
    CategoryFragment mCategoryFragment;
    PersonalCenterFragment mPersonalCenterFragment;

    public  static final String TAG = MainActivity.class.getSimpleName();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        L.i("MainActivity onCreate");
        super.onCreate(savedInstanceState);
//        initView();
    }

    private void initFragment() {
        fragments = new Fragment[5];
        mNewGoodsFragment = new NewGoodsFragment();
        mBoutiqueFragment = new BoutiqueFragment();
        mCategoryFragment = new CategoryFragment();
        mPersonalCenterFragment = new PersonalCenterFragment();
        fragments[0] = mNewGoodsFragment;
        fragments[1] = mBoutiqueFragment;
        fragments[2] = mCategoryFragment;
        fragments[4] = mPersonalCenterFragment;
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mNewGoodsFragment)
                .add(R.id.fragment_container, mBoutiqueFragment)
                .add(R.id.fragment_container,mCategoryFragment)
                .hide(mCategoryFragment)
                .hide(mBoutiqueFragment)
                .show(mNewGoodsFragment).commit();

    }
     @Override
   protected void initView() {
        radioButtons = new RadioButton[]{layoutNewGood, layoutBoutique, layoutCategory, layoutCart, layoutPersonalCenter};
    }

    @Override
    protected void initData() {
        initFragment();
    }

    @Override
    protected void setListener() {

    }

    public void onCheckedChange(View v) {
        switch (v.getId()) {
            case R.id.layout_new_good:
                index = 0;
                break;
            case R.id.layout_boutique:
                index = 1;
                break;
            case R.id.layout_category:
                index = 2;
                break;
            case R.id.layout_cart:
                index = 3;
                break;
            case R.id.layout_personal_center:
                if(FuLiCenterApplication.getUser() == null){
                    MFGT.gotoLoginActivity(this);
                }else {
                    index = 4;

                }
                break;
        }
        setFragment();
    }

    private void setFragment() {
        if (index != currentIndex) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.hide(fragments[currentIndex]);
            if (!fragments[index].isAdded()) {
                ft.add(R.id.fragment_container, fragments[index]);
            }
            ft.show(fragments[index]).commit();
            setRadioButtonStatus();
            currentIndex = index;
        }
    }

    private void setRadioButtonStatus() {
        for (int i = 0; i < radioButtons.length; i++) {
            if (i == index) {
                radioButtons[i].setChecked(true);
            } else
                radioButtons[i].setChecked(false);

        }
    }

    public void onBackPressed(){
      finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        L.e(TAG,"onResume...");
        if(index == 4 && FuLiCenterApplication.getUser()==null){
            index = 0;
        }
        setFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.e(TAG,"onActivityResult,requestCode="+requestCode);
        if(requestCode == I.REQUEST_CODE_LOGIN && FuLiCenterApplication.getUser()!=null){
            index = 4;
        }
    }
}
