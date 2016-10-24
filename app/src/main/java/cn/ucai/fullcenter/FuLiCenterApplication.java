package cn.ucai.fullcenter;

import android.app.Application;

import cn.ucai.fullcenter.bean.User;

/**
 * Created by Administrator on 2016/10/17.
 */
public class FuLiCenterApplication extends Application {

    private  static FuLiCenterApplication instance;
    private  static String username;
    private  static User user;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        FuLiCenterApplication.user = user;
    }

    public static void setInstance(FuLiCenterApplication instance) {
        FuLiCenterApplication.instance = instance;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        FuLiCenterApplication.username = username;
    }

    public FuLiCenterApplication() {
        instance=this;
    }

    public static FuLiCenterApplication getInstance(){
        if(instance==null){
            instance=new FuLiCenterApplication();
        }
        return instance;
    }
}
