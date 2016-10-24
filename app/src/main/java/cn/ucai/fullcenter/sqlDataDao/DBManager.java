package cn.ucai.fullcenter.sqlDataDao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import cn.ucai.fullcenter.bean.User;

/**
 * Created by Administrator on 2016/10/24.
 */
public class DBManager {
    private static DBManager dbMgr = new DBManager();
    private DBOpenHelper dbHelper;
    void  onInit(Context context){
        dbHelper = new DBOpenHelper(context);
    }

    public  static synchronized DBManager getInstance(){
        return dbMgr;
    }

    public  synchronized void closeDB(){
        if(dbHelper!=null){
            dbHelper.close();
        }
    }

    public synchronized boolean saveUser(User user){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserDao.USER_COLUMN_NAME,user.getMuserName());
        values.put(UserDao.USER_COLUMN_NICk,user.getMuserNick());
        values.put(UserDao.USER_COLUMN_AVATAR_ID,user.getMavatarId());
        values.put(UserDao.USER_COLUMN_AVATAR_TYPE,user.getMavatarType());
        values.put(UserDao.USER_COLUMN_AVATA_PATH,user.getMavatarPath());
        values.put(UserDao.USER_COLUMN_AVATAR_SUFFIX,user.getMavatarSuffix());
        values.put(UserDao.USER_COLUMN_AVATAR_LASTAUPDATE_TIME,user.getMavatarLastUpdateTime());
        if(db.isOpen()){
            return  db.replace(UserDao.USER_TABLE_NAME,null,values)!=-1;
        }
        return false;
    }

    public synchronized User getUset(String username){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "select * from " + UserDao.USER_TABLE_NAME + " where "
                + UserDao.USER_COLUMN_NAME + " =?";
        User user = null;
        Cursor cursor = db.rawQuery(sql,new String[]{username});
        while (cursor.moveToNext()){
            user = new User();
            user.setMuserName(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_NAME)));
            user.setMuserNick(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_NICk)));
            user.setMavatarId(cursor.getInt(cursor.getColumnIndex(UserDao.USER_COLUMN_NICk)));
            user.setMavatarType(cursor.getInt(cursor.getColumnIndex(UserDao.USER_COLUMN_NICk)));
            user.setMavatarPath(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_NICk)));
            user.setMavatarSuffix(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_NICk)));
            user.setMavatarLastUpdateTime(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_NICk)));
            return  user;
        }
      return user;
    }

    public synchronized boolean updateUser(User user){
        int result = -1;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = UserDao.USER_TABLE_NAME + "=?";
        ContentValues values = new ContentValues();
        values.put(UserDao.USER_COLUMN_NICk,user.getMuserNick());
        if(db.isOpen()){
            result = db.update(UserDao.USER_TABLE_NAME,values,sql,new String[]{user.getMuserName()});
        }
        return result>0;
    }
}
