package cn.ucai.fullcenter.netDao;

import android.content.Context;

import cn.ucai.fullcenter.I;
import cn.ucai.fullcenter.bean.BoutiqueBean;
import cn.ucai.fullcenter.bean.CategoryChildBean;
import cn.ucai.fullcenter.bean.CategoryGroupBean;
import cn.ucai.fullcenter.bean.GoodsDetailsBean;
import cn.ucai.fullcenter.bean.NewGoodsBean;

/**
 * Created by Administrator on 2016/10/17.
 */
public class NetDao {

    public static void downLoadNewGoods(Context mcontext,int catId, int pageId, OkHttpUtils.OnCompleteListener<NewGoodsBean[]> listener){
        OkHttpUtils utils=new OkHttpUtils(mcontext);
        utils.setRequestUrl(I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                .addParam(I.NewAndBoutiqueGoods.CAT_ID,String.valueOf(catId))
                .addParam(I.PAGE_ID,String.valueOf(pageId))
                .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(NewGoodsBean[].class)
                .execute(listener);
    }
    public static void downLoadGoodsDetails(Context mcontext, int goodsId, OkHttpUtils.OnCompleteListener<GoodsDetailsBean> listener){
        OkHttpUtils utils=new OkHttpUtils(mcontext);
        utils.setRequestUrl(I.REQUEST_FIND_GOOD_DETAILS)
                .addParam(I.GoodsDetails.KEY_GOODS_ID,String.valueOf(goodsId))
                .targetClass(GoodsDetailsBean.class)
                .execute(listener);
    }

    public static void downLoadBoutique(Context mcontext, OkHttpUtils.OnCompleteListener<BoutiqueBean[]> listener){
        OkHttpUtils utils = new OkHttpUtils(mcontext);
        utils.setRequestUrl(I.REQUEST_FIND_BOUTIQUES)
                .targetClass(BoutiqueBean[].class)
                .execute(listener);
    }

    public static void downLoadCategoryGroup(Context context, OkHttpUtils.OnCompleteListener<CategoryGroupBean[]> listener){
        OkHttpUtils utils = new OkHttpUtils(context);
        utils.setRequestUrl(I.REQUEST_FIND_CATEGORY_GROUP)
                .targetClass(CategoryGroupBean[].class)
                .execute(listener);
    }

    public static void downLoadCategoryChild(Context context,int parentId, OkHttpUtils.OnCompleteListener<CategoryChildBean[]> listener){
        OkHttpUtils utils = new OkHttpUtils(context);
        utils.setRequestUrl(I.REQUEST_FIND_CATEGORY_CHILDREN)
                .addParam(I.CategoryChild.PARENT_ID,String.valueOf(parentId))
                .targetClass(CategoryChildBean[].class)
                .execute(listener);
    }

}
