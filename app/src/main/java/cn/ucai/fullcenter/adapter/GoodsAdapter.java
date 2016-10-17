package cn.ucai.fullcenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fullcenter.I;
import cn.ucai.fullcenter.R;
import cn.ucai.fullcenter.bean.NewGoodsBean;
import cn.ucai.fullcenter.utils.ImageLoader;
import cn.ucai.fullcenter.utils.L;

/**
 * Created by Administrator on 2016/10/17.
 */
public class GoodsAdapter extends Adapter {
    Context mContext;
    ArrayList<NewGoodsBean> mList;

    public GoodsAdapter(ArrayList<NewGoodsBean> list, Context context) {
        mList=new ArrayList<>();
        mList.addAll(list);
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         ViewHolder holder = null;
        if (viewType == I.TYPE_FOOTER) {
            holder = new FooterViewHolder(View.inflate(mContext, R.layout.iterm_footer, null));
        } else {
            holder = new GoodsViewHolder(View.inflate(mContext, R.layout.iterm_goods, null));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       if(getItemViewType(position)==I.TYPE_FOOTER){

       }else {
           GoodsViewHolder mGoodsViewHolder= (GoodsViewHolder) holder;
           NewGoodsBean mNewGoodsBean=mList.get(position);
            ImageLoader.downloadImg(mContext,mGoodsViewHolder.ivGoodsPicture,mNewGoodsBean.getGoodsThumb());
           mGoodsViewHolder.tvGoodName.setText(mNewGoodsBean.getGoodsName());
           mGoodsViewHolder.tvGoodPrice.setText(mNewGoodsBean.getCurrencyPrice());
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

    public void initData(ArrayList<NewGoodsBean> list) {
        if(mList!=null){
        mList.clear();
        }
        mList.addAll(list);
        notifyDataSetChanged();
    }


    static class FooterViewHolder extends ViewHolder{
        @BindView(R.id.tvFooter)
        TextView tvFooter;

        FooterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class GoodsViewHolder extends ViewHolder{
        @BindView(R.id.ivGoodsPicture)
        ImageView ivGoodsPicture;
        @BindView(R.id.tvGoodName)
        TextView tvGoodName;
        @BindView(R.id.tvGoodPrice)
        TextView tvGoodPrice;
        @BindView(R.id.line_new_goods)
        LinearLayout lineNewGoods;

        GoodsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
