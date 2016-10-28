package cn.ucai.fullcenter.activity;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
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
    @BindView(R.id.tvCommonHeadTitle)
    TextView tvCommonHeadTitle;
    @BindView(R.id.ivGoodShare)
    ImageView ivGoodShare;
    @BindView(R.id.ivGoodCart)
    ImageView ivGoodCart;
    @BindView(R.id.tvGoodsCartCount)
    TextView tvGoodsCartCount;
    @BindView(R.id.layout_image)
    RelativeLayout layoutImage;
    @BindView(R.id.layout_banner)
    RelativeLayout layoutBanner;
    @BindView(R.id.layout_goods_details)
    RelativeLayout layoutGoodsDetails;


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

    @OnClick(R.id.ivGoodCollect)
    public void onIsCollectGoodsClick() {
        User user = FuLiCenterApplication.getUser();
        if (user == null) {
            MFGT.gotoLoginActivity(mContext);
        } else {
            if (isCollected) {
                NetDao.deleteCollectGoods(mContext, user.getMuserName(), goodId, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null && result.isSuccess()) {
                            isCollected = !isCollected;
                            updateGoodsCellectedStatus();
                            CommonUtils.showLongToast(mContext.getResources().getString(R.string.uncollect_goods));
                        }
                    }

                    @Override
                    public void onError(String error) {
                        L.e("error=" + error);
                        CommonUtils.showLongToast(mContext.getResources().getString(R.string.uncollect_goods_fail));
                    }
                });
            } else {
                NetDao.addCollectGoods(mContext, user.getMuserName(), goodId, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null && result.isSuccess()) {
                            isCollected = !isCollected;
                            updateGoodsCellectedStatus();
                            CommonUtils.showLongToast(mContext.getResources().getString(R.string.collect_goods_success));
                        }

                    }

                    @Override
                    public void onError(String error) {
                        L.e("error=" + error);
                        CommonUtils.showLongToast(mContext.getResources().getString(R.string.collect_goods_fail));
                    }
                });
            }
        }
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
        } else {
            ivGoodCollect.setImageResource(R.mipmap.bg_collect_in);
        }
    }

    @OnClick(R.id.ivGoodShare)
    public void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);
    }

    @OnClick(R.id.ivGoodCart)
    public void onIsAddCartClick() {
        User user = FuLiCenterApplication.getUser();
        if (user == null) {
            MFGT.gotoLoginActivity(mContext);
        } else {
            NetDao.addCartGoods(mContext, user.getMuserName(), goodId, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if (result != null && result.isSuccess()) {
                        CommonUtils.showLongToast(mContext.getResources().getString(R.string.add_cart_goods_success));
                    }else {
                        CommonUtils.showLongToast(mContext.getResources().getString(R.string.add_cart_goods_fail));
                    }
                }

                @Override
                public void onError(String error) {
                    L.e("error= "+error);
                    CommonUtils.showLongToast(mContext.getResources().getString(R.string.add_cart_goods_fail));
                }
            });
        }
    }
}
