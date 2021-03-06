package cn.ucai.fullcenter.adapter;

import android.app.backup.FullBackupDataOutput;
import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fullcenter.FuLiCenterApplication;
import cn.ucai.fullcenter.I;
import cn.ucai.fullcenter.R;
import cn.ucai.fullcenter.bean.CollectBean;
import cn.ucai.fullcenter.bean.MessageBean;
import cn.ucai.fullcenter.netDao.NetDao;
import cn.ucai.fullcenter.netDao.OkHttpUtils;
import cn.ucai.fullcenter.utils.CommonUtils;
import cn.ucai.fullcenter.utils.ImageLoader;
import cn.ucai.fullcenter.utils.L;
import cn.ucai.fullcenter.utils.MFGT;
import cn.ucai.fullcenter.views.FooterViewHolder;

/**
 * Created by Administrator on 2016/10/17.
 */
public class CollectGoodsAdapter extends Adapter {
    Context mContext;
    ArrayList<CollectBean> mList;

    boolean isMore;//加载信息提示

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
        notifyDataSetChanged();
    }

    public CollectGoodsAdapter(ArrayList<CollectBean> list, Context context) {
        mList = new ArrayList<>();
        mList.addAll(list);
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = null;
        if (viewType == I.TYPE_FOOTER) {
            holder = new FooterViewHolder(View.inflate(mContext, R.layout.iterm_footer, null));
        } else {
            holder = new CollectGoodsViewHolder(View.inflate(mContext, R.layout.iterm_collect_goods, null));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemViewType(position) == I.TYPE_FOOTER) {
            FooterViewHolder mFooterViewHolder = (FooterViewHolder) holder;
            mFooterViewHolder.tvFooter.setText(getFootString());

        } else {
            CollectGoodsViewHolder mCollectGoodsViewHolder = (CollectGoodsViewHolder) holder;
            CollectBean mCollectBean = mList.get(position);
            ImageLoader.downloadImg(mContext, mCollectGoodsViewHolder.ivGoodsPicture, mCollectBean.getGoodsThumb());
            mCollectGoodsViewHolder.tvGoodName.setText(mCollectBean.getGoodsName());
            mCollectGoodsViewHolder.lineNewGoods.setTag(mCollectBean);
        }
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() + 1 : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        }
        return I.TYPE_ITEM;
    }

    public void initData(ArrayList<CollectBean> list) {
        if (mList != null) {
            mList.clear();
        }
        mList.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 设置页脚页眉提示信息
     */
    public int getFootString() {
        return isMore ? R.string.load_more : R.string.no_more;
    }

    public void AddData(ArrayList<CollectBean> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    class CollectGoodsViewHolder extends ViewHolder {
        @BindView(R.id.ivGoodsPicture)
        ImageView ivGoodsPicture;
        @BindView(R.id.tvGoodName)
        TextView tvGoodName;
        @BindView(R.id.ivDeleteCollectGoods)
        ImageView ivDeleteCollectGoods;
        @BindView(R.id.line_new_goods)
        RelativeLayout lineNewGoods;

        CollectGoodsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.line_new_goods)
        public void OnGoodsItermDetailClick() {
            CollectBean goods = (CollectBean) lineNewGoods.getTag();
            MFGT.gotoGoodsDetailsActivity(mContext, goods.getGoodsId());
        }

        @OnClick(R.id.ivDeleteCollectGoods)
        public void deleteCollectGoods() {
            String username = FuLiCenterApplication.getUser().getMuserName();
            final CollectBean goods = (CollectBean) lineNewGoods.getTag();
            NetDao.deleteCollectGoods(mContext, username, goods.getGoodsId(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if(result!=null && result.isSuccess()){
                        mList.remove(goods);
                        notifyDataSetChanged();
                    }else {
                        CommonUtils.showShortToast(result!=null?result.getMsg()
                                :mContext.getResources().getString(R.string.delete_collect_goods_fail));
                    }
                }

                @Override
                public void onError(String error) {
                    L.e("error="+error);
                    CommonUtils.showLongToast(mContext.getResources().getString(R.string.delete_collect_goods_fail));
                }
            });
        }
    }

}
