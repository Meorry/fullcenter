package cn.ucai.fullcenter.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fullcenter.R;
import cn.ucai.fullcenter.bean.CategoryChildBean;
import cn.ucai.fullcenter.bean.CategoryGroupBean;
import cn.ucai.fullcenter.utils.ImageLoader;
import cn.ucai.fullcenter.utils.L;
import cn.ucai.fullcenter.utils.MFGT;

/**
 * Created by Administrator on 2016/10/20.
 */
public class CategoryAdapter extends BaseExpandableListAdapter {

    Context mContext;
    ArrayList<CategoryGroupBean> mGroupList; //大组数据保存
    ArrayList<ArrayList<CategoryChildBean>> mChildList; //大组中小组数据保存

    public CategoryAdapter(Context context, ArrayList<CategoryGroupBean> groupList,
                           ArrayList<ArrayList<CategoryChildBean>> childList) {
        mContext = context;
        mGroupList = new ArrayList<>();
        mGroupList.addAll(groupList);
        mChildList = new ArrayList<>();
        mChildList.addAll(childList);
    }

    @Override
    public int getGroupCount() {
        return mGroupList != null ? mGroupList.size() : 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildList != null && mChildList.get(groupPosition) != null ? mChildList.size() : 0;
    }

    @Override
    public CategoryGroupBean getGroup(int groupPosition) {
        return mGroupList != null ? mGroupList.get(groupPosition) : null;
    }

    @Override
    public CategoryChildBean getChild(int groupPosition, int childPosition) {
        return mChildList != null && mChildList.get(groupPosition)!=null ? mChildList.get(groupPosition).get(childPosition) : null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
        CategoryGroupViewHolder holder;
        if (view == null) {
            L.e("mContext"+mContext+","+R.layout.iterm_category_group);
            view = View.inflate(mContext, R.layout.iterm_category_group, null);
            holder = new CategoryGroupViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (CategoryGroupViewHolder) view.getTag();
        }

        CategoryGroupBean group = getGroup(groupPosition);
        ImageLoader.downloadImg(mContext, holder.ivGroupImage, group.getImageUrl());
        holder.tvGroupName.setText(group.getName());
        holder.ivGroupExpand.setImageResource(isExpanded ? R.mipmap.expand_off : R.mipmap.expand_on);

        return view;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
        CategoryChildViewHolder holder;
        if (view == null) {
            view = View.inflate(mContext, R.layout.iterm_category_child, null);
            holder = new CategoryChildViewHolder(view);
            view.setTag(holder);
        }else {
            holder = (CategoryChildViewHolder) view.getTag();
        }
        final CategoryChildBean child = getChild(groupPosition, childPosition);
        ImageLoader.downloadImg(mContext,holder.ivChildImage,child.getImageUrl());
        holder.tvChildName.setText(child.getName());
        holder.mItermCategoryChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<CategoryChildBean> list = mChildList.get(groupPosition);
                String name = mGroupList.get(groupPosition).getName();
                MFGT.gotoCategorySecondActivity(mContext,child.getId(),name,list);
            }
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void initData(ArrayList<CategoryGroupBean> groupList, ArrayList<ArrayList<CategoryChildBean>> childList) {
     if(mGroupList!=null){
         mGroupList.clear();
     }
        mGroupList.addAll(groupList);
        if(mChildList!=null){
            mChildList.clear();
        }
        mChildList.addAll(childList);
        notifyDataSetChanged();
    }

    class CategoryGroupViewHolder {
        @BindView(R.id.ivGroupImage)
        ImageView ivGroupImage;
        @BindView(R.id.tvGroupName)
        TextView tvGroupName;
        @BindView(R.id.ivGroupExpand)
        ImageView ivGroupExpand;

        CategoryGroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

     class CategoryChildViewHolder {
         @BindView(R.id.iterm_category_child)
         RelativeLayout mItermCategoryChild;
        @BindView(R.id.ivChildImage)
        ImageView ivChildImage;
        @BindView(R.id.tvChildName)
        TextView tvChildName;

        CategoryChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
