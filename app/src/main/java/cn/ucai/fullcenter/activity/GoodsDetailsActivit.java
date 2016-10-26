package cn.ucai.fullcenter.activity;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fullcenter.FuLiCenterApplication;
import cn.ucai.fullcenter.I;
import cn.ucai.fullcenter.R;
import cn.ucai.fullcenter.bean.AlbumsBean;
import cn.ucai.fullcenter.bean.GoodsDetailsBean;
import cn.ucai.fullcenter.bean.MessageBean;
import cn.ucai.fullcenter.bean.User;
import cn.ucai.fullcenter.netDao.NetDao;
import cn.ucai.fullcenter.netDao.OkHttpUtils;
import cn.ucai.fullcenter.utils.CommonUtils;
import cn.ucai.fullcenter.utils.L;
import cn.ucai.fullcenter.utils.MFGT;
import cn.ucai.fullcenter.views.FlowIndicator;
import cn.ucai.fullcenter.views.SlideAutoLoopView;

public class GoodsDetailsActivit extends BaseActivity {

    @BindView(R.id.backClickArea)
    LinearLayout backClickArea;
    @BindView(R.id.tvGoodsEnglishName)
    TextView tvGoodsEnglishName;
    @BindView(R.id.tvGoodsNameName)
    TextView tvGoodsNameName;
    @BindView(R.id.tvGoodsPrice)
    TextView tvGoodsPrice;
    @BindView(R.id.tvGoodsCurrentPrice)
    TextView tvGoodsCurrentPrice;
    @BindView(R.id.sal)
    SlideAutoLoopView sal;
    @BindView(R.id.indicator)
    FlowIndicator indicator;
    @BindView(R.id.wvGoodBrief)
    WebView wvGoodBrief;
    @BindView(R.id.ivGoodCollect)
    ImageView ivGoodCollect;

    int goodId;
    GoodsDetailsActivit mContext;

    boolean isCollected = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_goods_details);
        ButterKnife.bind(this);
        goodId = getIntent().getIntExtra(I.GoodsDetails.KEY_GOODS_ID, 0);
        L.e("detail", "goodid=" + goodId);
        if (goodId == 0) {
            finish();
        }
        mContext = this;
//        initView();
//        initData();
//        setListener();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setListener() {
    }

    @Override
    protected void initData() {
        NetDao.downLoadGoodsDetails(mContext, goodId, new OkHttpUtils.OnCompleteListener<GoodsDetailsBean>() {
            @Override
            public void onSuccess(GoodsDetailsBean result) {
                L.e("details=" + result);
                if (result != null) {
                    showGoodsDetails(result);
                } else {
                    finish();
                }
            }

            @Override
            public void onError(String error) {
                finish();
                L.e("error=" + error);
                CommonUtils.showLongToast(error);
            }
        });
    }

    private void showGoodsDetails(GoodsDetailsBean result) {
        tvGoodsEnglishName.setText(result.getGoodsEnglishName());
        tvGoodsNameName.setText(result.getGoodsName());
        tvGoodsCurrentPrice.setText(result.getCurrencyPrice());
        tvGoodsPrice.setText(result.getShopPrice());
        sal.startPlayLoop(indicator, getAlumImagUrl(result), getAlumCount(result));
        wvGoodBrief.loadDataWithBaseURL(null, result.getGoodsBrief(), I.TEXT_HTML, I.UTF_8, null);
    }

    private int getAlumCount(GoodsDetailsBean details) {
        if (details.getProperties() != null && details.getProperties().length > 0) {
            return details.getProperties()[0].getAlbums().length;
        }
        return 0;
    }

    private String[] getAlumImagUrl(GoodsDetailsBean details) {
        String[] urls = new String[]{};
        if (details.getProperties() != null && details.getProperties().length > 0) {
            AlbumsBean[] mAlbumsBeen = details.getProperties()[0].getAlbums();
            urls = new String[mAlbumsBeen.length];
            for (int i = 0; i < mAlbumsBeen.length; i++) {
                urls[i] = mAlbumsBeen[i].getImgUrl();
            }
        }
        return urls;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        isCollected();
    }

    @OnClick(R.id.backClickArea)
    public void onBackClick() {
        MFGT.finish(this);
    }

    private void isCollected() {
        User user = FuLiCenterApplication.getUser();
        if (user != null) {
            NetDao.isCollectGoods(mContext, user.getMuserName(), goodId, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if (result != null && result.isSuccess()) {
                       isCollected = true;
                        updateGoodsCellectedStatus();
                    }
                }

                @Override
                public void onError(String error) {
                    isCollected = false;
                    updateGoodsCellectedStatus();
                }
            });
        }
        updateGoodsCellectedStatus();
    }

    private void updateGoodsCellectedStatus() {
        if (isCollected) {
            ivGoodCollect.setImageResource(R.mipmap.bg_collect_out);
        }else {
            ivGoodCollect.setImageResource(R.mipmap.bg_collect_in);
        }
    }
}
