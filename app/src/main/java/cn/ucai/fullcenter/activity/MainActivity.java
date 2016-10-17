package cn.ucai.fullcenter.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fullcenter.R;
import cn.ucai.fullcenter.fragment.NewGoodsFragment;
import cn.ucai.fullcenter.utils.L;

public class MainActivity extends AppCompatActivity {

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

    int index=0;

    RadioButton[] radioButtons;
    Fragment[] fragments;
    NewGoodsFragment mNewGoodsFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        L.i("MainActivity onCreate");
        initView();
        initFragment();
    }

    private void initFragment() {
        fragments=new Fragment[5];
        mNewGoodsFragment=new NewGoodsFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,mNewGoodsFragment)
                .show(mNewGoodsFragment).commit();

    }

    private void initView() {
        radioButtons=new RadioButton[]{layoutNewGood,layoutBoutique,layoutCategory,layoutCart,layoutPersonalCenter};
    }

    public void onCheckedChange(View v) {
       switch (v.getId()){
           case  R.id.layout_new_good:
               index=0;
           break;
           case  R.id.layout_boutique:
               index=1;
               break;
           case  R.id.layout_category:
               index=2;
               break;
           case  R.id.layout_cart:
               index=3;
               break;
           case  R.id.layout_personal_center:
               index=4;
               break;
       }
        setRadioButtonStatus();
    }

    private void setRadioButtonStatus() {
        for(int i=0;i<radioButtons.length;i++){
            if(i==index){
                radioButtons[i].setChecked(true);
            }else
                radioButtons[i].setChecked(false);

        }
    }

}
