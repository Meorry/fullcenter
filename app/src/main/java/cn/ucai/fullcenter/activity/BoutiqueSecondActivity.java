package cn.ucai.fullcenter.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fullcenter.I;
import cn.ucai.fullcenter.R;
import cn.ucai.fullcenter.adapter.GoodsAdapter;
import cn.ucai.fullcenter.bean.BoutiqueBean;
import cn.ucai.fullcenter.bean.NewGoodsBean;
import cn.ucai.fullcenter.netDao.NetDao;
import cn.ucai.fullcenter.netDao.OkHttpUtils;
import cn.ucai.fullcenter.utils.CommonUtils;
import cn.ucai.fullcenter.utils.ConvertUtils;
import cn.ucai.fullcenter.utils.L;
import cn.ucai.fullcenter.utils.MFGT;
import cn.ucai.fullcenter.views.SpaceItemDecoration;

/**
 * Created by Administrator on 2016/10/19.
 */
public class BoutiqueSecondActivity extends BaseActivity {
    @BindView(R.id.tv_refresh)
    TextView tvRefresh;
    @BindView(R.id.rlv)
    RecyclerView rlv;
    @BindView(R.id.sfl)
    SwipeRefreshLayout sfl;
    @BindView(R.id.tvCommonHeadTitle)
    TextView tvCommonHeadTitle;

    GridLayoutManager glm;
    BoutiqueSecondActivity mcontext;
    ArrayList<NewGoodsBean> mList;
    GoodsAdapter mAdapter;

    int pageId = 1;
    BoutiqueBean mBean;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_boutique_second);
        ButterKnife.bind(this);
        mBean = (BoutiqueBean) getIntent().getSerializableExtra(I.Boutique.CAT_ID);
        if(mBean == null){
            finish();
        }
        mList = new ArrayList<>();
        mcontext = this;
        mAdapter = new GoodsAdapter(mList, mcontext);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        sfl.setColorSchemeColors(getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_yellow),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_green));
        glm = new GridLayoutManager(mcontext, I.COLUM_NUM);
        rlv.setLayoutManager(glm);
        rlv.setEnabled(true);
        rlv.setAdapter(mAdapter);
        rlv.addItemDecoration(new SpaceItemDecoration(16));
        tvCommonHeadTitle.setText(mBean.getTitle());
    }

    @Override
    protected void initData() {
        downLoadData(I.ACTION_DOWNLOAD);
    }

    @Override
    protected void setListener() {
        setPullUpListener();
        setPullDownListener();
    }

    private void downLoadData(final int action) {
        NetDao.downLoadNewGoods(mcontext, mBean.getId(), pageId, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                sfl.setRefreshing(false);
                tvRefresh.setVisibility(View.GONE);
                mAdapter.setMore(true);
                if (result != null && result.length > 0) {
                    ArrayList<NewGoodsBean> list = ConvertUtils.array2List(result);
                    if (action == I.ACTION_DOWNLOAD || action == I.ACTION_PULL_DOWN) {
                        mAdapter.initData(list);
                    } else {
                        mAdapter.AddData(list);
                    }

                    if (list.size() < I.PAGE_SIZE_DEFAULT) {
                        mAdapter.setMore(false);
                    }
                } else {
                    mAdapter.setMore(false);
                }
            }

            @Override
            public void onError(String error) {
                sfl.setRefreshing(false);
                tvRefresh.setVisibility(View.GONE);
                CommonUtils.showLongToast(error);
                L.e("error: " + error);

            }
        });
    }

    /**
     * 下拉刷新
     */
    private void setPullDownListener() {
        sfl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sfl.setRefreshing(true);
                tvRefresh.setVisibility(View.VISIBLE);
                pageId = 1;
                downLoadData(I.ACTION_PULL_DOWN);
            }
        });
    }

    /**
     * 上拉刷新
     */
    private void setPullUpListener() {
        rlv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = glm.findLastVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastPosition == mAdapter.getItemCount() - 1
                        && mAdapter.isMore()) {
                    pageId++;
                    downLoadData(I.ACTION_PULL_UP);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstPosition = glm.findFirstVisibleItemPosition();
                sfl.setEnabled(firstPosition == 0);
            }
        });
    }

    @OnClick(R.id.backClickArea)
    public void onClick() {
        MFGT.finish(this);
    }
}
