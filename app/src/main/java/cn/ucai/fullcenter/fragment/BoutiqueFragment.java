package cn.ucai.fullcenter.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fullcenter.R;
import cn.ucai.fullcenter.activity.MainActivity;
import cn.ucai.fullcenter.adapter.BoutiqueAdapter;
import cn.ucai.fullcenter.bean.BoutiqueBean;
import cn.ucai.fullcenter.netDao.NetDao;
import cn.ucai.fullcenter.netDao.OkHttpUtils;
import cn.ucai.fullcenter.utils.CommonUtils;
import cn.ucai.fullcenter.utils.ConvertUtils;
import cn.ucai.fullcenter.utils.L;
import cn.ucai.fullcenter.views.SpaceItemDecoration;

/**
 * Created by Administrator on 2016/10/19.
 */
public class BoutiqueFragment extends BaseFragment {

    @BindView(R.id.tv_refresh)
    TextView tvRefresh;
    @BindView(R.id.rlv)
    RecyclerView rlv;
    @BindView(R.id.sfl)
    SwipeRefreshLayout sfl;
    LinearLayoutManager mLinearLayoutManager;
    MainActivity mContext;
    BoutiqueAdapter mAdapter;
    ArrayList<BoutiqueBean> mList;
    public BoutiqueFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_new_goods, container, false);
        ButterKnife.bind(this, layout);
        mContext = (MainActivity) getContext();
        mList=new ArrayList<>();
        mAdapter = new BoutiqueAdapter(mContext,mList);
        super.onCreateView(inflater,container,savedInstanceState);
//        initView();
//        initData();
//        setListener();
        return layout;
    }
     @Override
    protected void setListener() {
        setPullDownListener();
    }
    private void setPullDownListener() {
        sfl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sfl.setRefreshing(true);
                tvRefresh.setVisibility(View.VISIBLE);
                downLoadData();
            }
        });
    }

    @Override
    protected void initData() {
        downLoadData();
    }

    private void downLoadData() {
        NetDao.downLoadBoutique(mContext, new OkHttpUtils.OnCompleteListener<BoutiqueBean[]>() {
            @Override
            public void onSuccess(BoutiqueBean[] result) {
                sfl.setRefreshing(false);//设置是否刷新
                tvRefresh.setVisibility(View.GONE);//隐藏刷新提示
                if(result != null && result.length>0){
                    ArrayList<BoutiqueBean> list = ConvertUtils.array2List(result);
                    mAdapter.initData(list);
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

    @Override
    protected void initView() {
        sfl.setColorSchemeColors(getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_yellow),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_green));
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        rlv.setLayoutManager(mLinearLayoutManager);
        rlv.setHasFixedSize(true);
        rlv.setAdapter(mAdapter);
        rlv.addItemDecoration(new SpaceItemDecoration(10));

    }
}
