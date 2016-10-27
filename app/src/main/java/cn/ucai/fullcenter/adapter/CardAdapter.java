package cn.ucai.fullcenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fullcenter.R;
import cn.ucai.fullcenter.bean.CartBean;

public class CardAdapter extends RecyclerView.Adapter {
    Context mContext;
    ArrayList<CartBean> mList;

    public CardAdapter(Context context, ArrayList<CartBean> list) {
        mContext = context;
        mList = new ArrayList<>();
        mList.addAll(list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        holder = new CardViewHolder(View.inflate(mContext, R.layout.iterm_card, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CardViewHolder bh = (CardViewHolder) holder;
        CartBean mCartBean = mList.get(position);
//            ImageLoader.downloadImg(mContext,bh.ivBoutiqueImage,mCartBean.getImageurl());
//            bh.tvBoutiqueTitle.setText(mCartBean.getTitle());
//            bh.tvBoutiqueName.setText(mCartBean.getName());
//            bh.tvBoutiqueDescription.setText(mCartBean.getDescription());
//            bh.mLinearLayout.setTag(mCartBean);
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public void initData(ArrayList<CartBean> list) {
        if (mList != null) {
            mList.clear();
        }
        mList.addAll(list);
        notifyDataSetChanged();
    }


    static class CardViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cb_card_checked)
        CheckBox cbCardChecked;
        @BindView(R.id.iv_card_goods_image)
        ImageView ivCardGoodsImage;
        @BindView(R.id.iv_card_goodsName)
        TextView ivCardGoodsName;
        @BindView(R.id.iv_card_add)
        ImageView ivCardAdd;
        @BindView(R.id.tv_card_goods_count)
        TextView tvCardGoodsCount;
        @BindView(R.id.iv_card_del)
        ImageView ivCardDel;
        @BindView(R.id.tv_card_goods_price)
        TextView tvCardGoodsPrice;
        @BindView(R.id.rl_layout_card)
        RelativeLayout rlLayoutCard;

        CardViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

