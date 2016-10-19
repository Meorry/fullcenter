package cn.ucai.fullcenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fullcenter.R;
import cn.ucai.fullcenter.bean.BoutiqueBean;
import cn.ucai.fullcenter.utils.ImageLoader;
import cn.ucai.fullcenter.utils.MFGT;

/**
 * Created by Administrator on 2016/10/19.
 */
public class BoutiqueAdapter extends RecyclerView.Adapter {
    Context mContext;
    ArrayList<BoutiqueBean> mList;
    public BoutiqueAdapter(Context context, ArrayList<BoutiqueBean> list) {
        mContext = context;
        mList = new ArrayList<>();
        mList.addAll(list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        holder = new BoutiqueViewHolder(View.inflate(mContext, R.layout.iterm_boutique, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            BoutiqueViewHolder bh = (BoutiqueViewHolder) holder;
            BoutiqueBean mBoutiqueBean = mList.get(position);
            ImageLoader.downloadImg(mContext,bh.ivBoutiqueImage,mBoutiqueBean.getImageurl());
            bh.tvBoutiqueTitle.setText(mBoutiqueBean.getTitle());
            bh.tvBoutiqueName.setText(mBoutiqueBean.getName());
            bh.tvBoutiqueDescription.setText(mBoutiqueBean.getDescription());
            bh.mLinearLayout.setTag(mBoutiqueBean);
    }
    @Override
    public int getItemCount() {
        return mList != null?mList.size() :0;
    }
    public void initData(ArrayList<BoutiqueBean> list) {
        if(mList!=null){
            mList.clear();
        }
        mList.addAll(list);
        notifyDataSetChanged();
    }
    class BoutiqueViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.ivBoutiqueImage)
        ImageView ivBoutiqueImage;
        @BindView(R.id.tvBoutiqueTitle)
        TextView tvBoutiqueTitle;
        @BindView(R.id.tvBoutiqueName)
        TextView tvBoutiqueName;
        @BindView(R.id.tvBoutiqueDescription)
        TextView tvBoutiqueDescription;
        @BindView(R.id.layoutBoutique)
        RelativeLayout mLinearLayout;

        BoutiqueViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
        @OnClick(R.id.layoutBoutique)
        public void onBoutiqueClick(){
            BoutiqueBean mBoutiqueBean= (BoutiqueBean) mLinearLayout.getTag();
            MFGT.gotoBoutiqueSecondActivity(mContext,mBoutiqueBean);
        }

    }
}
