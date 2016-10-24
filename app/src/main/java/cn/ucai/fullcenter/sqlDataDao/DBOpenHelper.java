package cn.ucai.fullcenter.sqlDataDao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cn.ucai.fullcenter.I;

/**
 * Created by Administrator on 2016/10/24.
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    private  static  final int DATABASE_VERSION = 1;
    private  static  DBOpenHelper instance;
    private  static  final String FULICENTER_USER_TABLE_NAME = "CREATE TABLE "
            + UserDao.USER_TABLE_NAME + " ("
            + UserDao.USER_COLUMN_NAME + " TEXT PRIMARY KEY, "
            + UserDao.USER_COLUMN_NICk + " TEXT, "
            + UserDao.USER_COLUMN_AVATAR_ID + " INTEGER, "
            + UserDao.USER_COLUMN_AVATAR_TYPE + " INTEGER, "
            + UserDao.USER_COLUMN_AVATA_PATH + " TEXT, "
            + UserDao.USER_COLUMN_AVATAR_SUFFIX + " TEXT, "
            + UserDao.USER_COLUMN_AVATAR_LASTAUPDATE_TIME + " TEXT);";

    public static DBOpenHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBOpenHelper(context.getApplicationContext());
        }
        return instance;
    }
    public DBOpenHelper(Context context ) {
        super(context, getUserDatabaseName(), null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       db.execSQL(FULICENTER_USER_TABLE_NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static  String getUserDatabaseName() {
        return I.User.TABLE_NAME + "_demo.db";
    }

    public  static void closeDB(){
        if(instance!=null){
            SQLiteDatabase db = instance.getWritableDatabase();
            db.close();
            instance = null;
        }
    }
}
