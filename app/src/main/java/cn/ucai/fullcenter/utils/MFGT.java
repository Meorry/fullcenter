package cn.ucai.fullcenter.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import cn.ucai.fullcenter.I;
import cn.ucai.fullcenter.R;
import cn.ucai.fullcenter.activity.BoutiqueSecondActivity;
import cn.ucai.fullcenter.activity.CategorySecondActivity;
import cn.ucai.fullcenter.activity.GoodsDetailsActivit;
import cn.ucai.fullcenter.activity.LoginActivity;
import cn.ucai.fullcenter.activity.MainActivity;
import cn.ucai.fullcenter.bean.BoutiqueBean;
import cn.ucai.fullcenter.bean.CategoryChildBean;


public class MFGT {
    public static void finish(Activity activity){
        activity.finish();
        activity.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
    public static void gotoMainActivity(Activity context){
        startActivity(context, MainActivity.class);
    }
    public static void startActivity(Activity context,Class<?> cls){
        Intent intent = new Intent();
        intent.setClass(context,cls);
        startActivity(context,intent);
    }

    public static void gotoGoodsDetailsActivity(Context context,int goodId){
        Intent intent = new Intent();
        intent.setClass(context, GoodsDetailsActivit.class);
        intent.putExtra(I.GoodsDetails.KEY_GOODS_ID,goodId);
        startActivity(context,intent);
    }

    public static void startActivity(Context context,Intent intent){
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }

    public static void gotoBoutiqueSecondActivity(Context context, BoutiqueBean bean){
        Intent intent = new Intent();
        intent.setClass(context,BoutiqueSecondActivity.class);
        intent.putExtra(I.Boutique.CAT_ID,bean);
        startActivity(context,intent);
    }

    public static void gotoCategorySecondActivity(Context context, int catId, String name, ArrayList<CategoryChildBean> list){
        Intent intent = new Intent();
        intent.setClass(context,CategorySecondActivity.class);
        intent.putExtra(I.CategoryChild.CAT_ID,catId);
        intent.putExtra(I.CategoryGroup.NAME,name);
        intent.putExtra(I.CategoryChild.ID,list);
        startActivity(context,intent);
    }

    public static void gotoLoginActivity(Activity context){
        startActivity(context, LoginActivity.class);
    }
}
