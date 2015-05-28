package com.material.db.log;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 用户操作日志数据库DBHelper
 *
 * Created by mojingtian on 15/5/19.
 */
public class UserLogDBHelper extends SQLiteOpenHelper {
    Context context;

    private static final int DB_VERSION = 1;

    private static final String DB_NAME = "user_log.db";

    public final String TABLE_LOG = "user_log";

    public UserLogDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }


    /**
     * 表的字段名
     */
    public enum UserLogFieldName {
        id("id"), user_id("user_id"), package_name("package_name"), version("version"), action("action"), amount(
                "amount"), is_suc("is_suc"), date("date");

        private String fieldName;

        private UserLogFieldName(String name) {
            fieldName = name;
        }

        public String getValue() {
            return fieldName;
        }
    }

    /**
     * 表示消息是否成功
     */
    public enum ActionIsSuc {
        suc(1), fail(0);
        private int value;

        private ActionIsSuc(int val) {
            value = val;
        }

        public int getValue() {
            return value;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        creatLogTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drops the old tables
        db.execSQL("drop table if exists " + TABLE_LOG);
        onCreate(db);
    }

    /**
     * 删除Log表内数据
     *
     * @param db SQLiteDatabase
     */
    public void deleteLog(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOG);
        creatLogTable(db);
    }

    /**
     * 创建Log表
     *
     * @param db SQLiteDatabase
     */
    private void creatLogTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS ["
                + TABLE_LOG
                + "] ("
                + " [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                + " [user_id] CHAR, "
                + " [package_name], "
                + " [version] CHAR, "
                + " [action] CHAR, "
                + " [amount] NUMBER, "
                + " [is_suc] INTEGER DEFAULT (1),"
                + " [date] CHAR )");
    }
}
