package com.material.db.log;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 用于操作用户日志数据库
 * <p/>
 * Created by mojingtian on 15/5/19.
 */
public class UserLogDao {
    private final ReadWriteLock mLock = new ReentrantReadWriteLock();

    private final Lock mReadLock = mLock.readLock(); // 读锁

    private final Lock mWriteLock = mLock.writeLock(); // 写锁

    private final UserLogDBHelper mDBHelper;

    public UserLogDao(Context context) {
        mDBHelper = new UserLogDBHelper(context);
    }

    /**
     * 新增用户操作日志
     *
     * @param userLog 用户操作日志
     * @return 是否成功
     */
    public boolean insertUserLog(final UserLog userLog) throws Exception {
        boolean flag = false;
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        if (userLog != null) {
            mWriteLock.lock();
            ContentValues values = putUserLogValues(userLog);
            try {
                db.insert(mDBHelper.TABLE_LOG, null, values);
                flag = true;
            } catch (Exception e) {
                flag = false;
            } finally {
                try {
                    db.close();
                    mWriteLock.unlock();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }

    /**
     * 读取所有用户日志消息
     *
     * @return 用户日志消息
     */
    public ArrayList<UserLog> queryUserLogAll() {
        ArrayList<UserLog> userLogs = new ArrayList<UserLog>();
        Cursor cursor = null;
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        try {
            mReadLock.lock();
            cursor = db.rawQuery(
                    "SELECT * FROM " + mDBHelper.TABLE_LOG + " ORDER BY date", null);

            if (cursor != null && cursor.getCount() > 0) {
                UserLog userLog;
                while (cursor.moveToNext()) {
                    userLog = getUserLog(cursor);
                    if (userLog != null) {
                        userLogs.add(userLog);
                    }
                }
            }
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                    db.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mReadLock.unlock();
            }
        }

        return userLogs;
    }

    /**
     * 读取所有用户日志消息条数
     *
     * @return 用户日志消息条数
     */
    public int getUserLogCount() {
        int count = 0;
        Cursor cursor = null;
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        try {
            mReadLock.lock();
            cursor = db.rawQuery(
                    "select count(" + UserLogDBHelper.UserLogFieldName.id + ") from " + mDBHelper.TABLE_LOG, null);

            if (cursor != null && cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                    db.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mReadLock.unlock();
            }
        }

        return count;
    }

    /**
     * 分页读取所有用户日志消息
     *
     * @param pageNo   页数，从0开始
     * @param pageSize 每一页的条数
     * @return 用户日志消息
     */
    public ArrayList<UserLog> queryUserLogBySize(int pageNo, int pageSize) {
        ArrayList<UserLog> userLogs = new ArrayList<UserLog>();
        Cursor cursor = null;
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        try {
            mReadLock.lock();
            String sql = "select * from " + mDBHelper.TABLE_LOG + " ORDER BY date"
                    + " Limit " + String.valueOf(pageSize) + " Offset " + String.valueOf(pageNo * pageSize);
            cursor = db.rawQuery(sql, null);

            if (cursor != null && cursor.getCount() > 0) {
                UserLog userLog = null;
                while (cursor.moveToNext()) {
                    userLog = getUserLog(cursor);
                    if (userLog != null) {
                        userLogs.add(userLog);
                    }
                }
            }
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                    db.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mReadLock.unlock();
            }
        }

        return userLogs;
    }

    /**
     * 删除用户操作日志
     *
     * @return 是否成功
     */
    public boolean delUserLogAll() {
        boolean flag = false;
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        try {
            mWriteLock.lock();
            mDBHelper.deleteLog(db);
            flag = true;
        } catch (Exception e) {
            flag = false;
        } finally {
            try {
                db.close();
                mWriteLock.unlock();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * 封装数据至 ContentValues
     *
     * @param userLog 用户操作日志
     * @return ContentValues
     */
    private ContentValues putUserLogValues(UserLog userLog) throws Exception {
        ContentValues values = new ContentValues();
        if (!TextUtils.isEmpty(userLog.getUserId())) {
            values.put(UserLogDBHelper.UserLogFieldName.user_id.getValue(), userLog.getUserId());
        } else {
            throw new Exception("userId is null");
        }

        if (!TextUtils.isEmpty(userLog.getPackageName())) {
            values.put(UserLogDBHelper.UserLogFieldName.package_name.getValue(), userLog.getPackageName());
        } else {
            Log.i("JT", "jion this");
            throw new Exception("packageName is null");
        }

        if (!TextUtils.isEmpty(userLog.getVersion())) {
            values.put(UserLogDBHelper.UserLogFieldName.version.getValue(), userLog.getVersion());
        } else {
            throw new Exception("version is null");
        }

        if (!TextUtils.isEmpty(userLog.getAction())) {
            values.put(UserLogDBHelper.UserLogFieldName.action.getValue(), userLog.getAction());
        } else {
            throw new Exception("action is null");
        }

        if (!TextUtils.isEmpty(userLog.getAmount())) {
            values.put(UserLogDBHelper.UserLogFieldName.amount.getValue(), userLog.getAmount());
        }

        if (userLog.getIsSuc() != null) {
            if (userLog.getIsSuc() == UserLogDBHelper.ActionIsSuc.suc.getValue()
                    || userLog.getIsSuc() == UserLogDBHelper.ActionIsSuc.fail.getValue()) {
                values.put(UserLogDBHelper.UserLogFieldName.is_suc.getValue(), userLog.getIsSuc());
            } else {
                throw new Exception("isSuc is illegal ");
            }
        }

        values.put(UserLogDBHelper.UserLogFieldName.date.getValue(), System.currentTimeMillis());
        return values;
    }

    /**
     * 数据库中读取一条用户操作日志信息，并封装到userLog
     *
     * @param cursor 游标
     * @return UserLog
     */
    public UserLog getUserLog(Cursor cursor) {
        UserLog userLog = null;
        if (cursor != null) {

            int id = cursor.getInt(cursor.getColumnIndex(UserLogDBHelper.UserLogFieldName.id.getValue()));

            String userId = cursor.getString(cursor.getColumnIndex(UserLogDBHelper.UserLogFieldName.user_id.getValue()));

            String packageName = cursor.getString(cursor.getColumnIndex(UserLogDBHelper.UserLogFieldName.package_name.getValue()));

            String version = cursor.getString(cursor.getColumnIndex(UserLogDBHelper.UserLogFieldName.version.getValue()));

            String action = cursor.getString(cursor.getColumnIndex(UserLogDBHelper.UserLogFieldName.action.getValue()));

            String amount = cursor.getString(cursor.getColumnIndex(UserLogDBHelper.UserLogFieldName.amount.getValue()));

            Integer isSuc = cursor.getInt(cursor.getColumnIndex(UserLogDBHelper.UserLogFieldName.is_suc.getValue()));

            String date = cursor.getString(cursor.getColumnIndex(UserLogDBHelper.UserLogFieldName.date.getValue()));

            userLog = new UserLog();
            userLog.setId(id);
            userLog.setUserId(userId);
            userLog.setPackageName(packageName);
            userLog.setVersion(version);
            userLog.setAction(action);
            userLog.setAmount(amount);
            userLog.setIsSuc(isSuc);
            userLog.setDate(date);
        }
        return userLog;
    }
}
