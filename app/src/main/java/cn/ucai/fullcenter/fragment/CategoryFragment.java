package cn.ucai.fullcenter.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fullcenter.I;
import cn.ucai.fullcenter.R;
import cn.ucai.fullcenter.activity.MainActivity;
import cn.ucai.fullcenter.adapter.CategoryAdapter;
import cn.ucai.fullcenter.bean.CategoryChildBean;
import cn.ucai.fullcenter.bean.CategoryGroupBean;
import cn.ucai.fullcenter.netDao.NetDao;
import cn.ucai.fullcenter.netDao.OkHttpUtils;
import cn.ucai.fullcenter.utils.ConvertUtils;
import cn.ucai.fullcenter.utils.L;

/**
 * Created by Administrator on 2016/10/20.
 */
public class CategoryFragment extends BaseFragment {
    @BindView(R.id.layout_listView)
    ExpandableListView layoutListView;

    CategoryAdapter mAdapter;
    MainActivity mContext;
    ArrayList<CategoryGroupBean> mGroupList;
    ArrayList<ArrayList<CategoryChildBean>> mChildList;
    int groupCount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_category, null);
        ButterKnife.bind(this, layout);
        mContext = (MainActivity) getContext();
        mGroupList = new ArrayList<>();
        mChildList = new ArrayList<>();
        mAdapter = new CategoryAdapter(mContext,mGroupList,mChildList);
        super.onCreateView(inflater, container, savedInstanceState);
        return layout;
    }

    @Override
    protected void initView() {
        layoutListView.setGroupIndicator(null);
        layoutListView.setAdapter(mAdapter);

    }

    @Override
    protected void initData() {
        downLoadCategory();

    }

    private void downLoadCategory() {
        NetDao.downLoadCategoryGroup(mContext, new OkHttpUtils.OnCompleteListener<CategoryGroupBean[]>() {
            @Override
            public void onSuccess(CategoryGroupBean[] result) {
                if(result!=null && result.length>0){
                    L.e("result= "+result.length);
                    ArrayList<CategoryGroupBean> groupBeen = ConvertUtils.array2List(result);
                    mGroupList.addAll(groupBeen);
                    for(int i=0;i<groupBeen.size();i++){
                        mChildList.add(new ArrayList<CategoryChildBean>());
                        CategoryGroupBean group = groupBeen.get(i);
                        downLoadChild(group.getId(),i);
                    }
                }
            }

            @Override
            public void onError(String error) {
              L.e("error"+error);
            }
        });
    }

    private void downLoadChild(int id, final int index) {
        NetDao.downLoadCategoryChild(mContext, id, new OkHttpUtils.OnCompleteListener<CategoryChildBean[]>() {
            @Override
            public void onSuccess(CategoryChildBean[] result) {
                groupCount++;
                if(result!=null && result.length>0){
                    L.e("result= "+result.length);
                    ArrayList<CategoryChildBean> childBeen = ConvertUtils.array2List(result);
                    mChildList.set(index,childBeen);
                }
                if(groupCount == mGroupList.size()){
                    mAdapter.initData(mGroupList,mChildList);
                }
            }

            @Override
            public void onError(String error) {
                L.e("error"+error);
            }
        });
    }
    /**
     * 也可以在这里进行小分类监听事件
     */
    @Override
    protected void setListener() {
//       layoutListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//           @Override
//           public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//               return false;
//           }
//       });
    }
}
