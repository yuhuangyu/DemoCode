package com.test.huisuo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ASUS on 2018/4/11.
 */

public class AppsFlyerDBHelp extends SQLiteOpenHelper  {






//    private static class DB extends SQLiteOpenHelper {
        private static final String DB_NAME = "appflyer_plugin.db";
        public static String TABLE_NAME = "file";//表名
        private static final int VERSION = 1;
        private static final String SQL_CREATE = "create table if not exists " +TABLE_NAME + " (packageName varchar,install_referrer varchar,referrer_click_seconds Long,install_begin_seconds Long)";
        private static final String SQL_DROP = "drop table if exists " + TABLE_NAME;
        /**
         * @Hide
         */
        private SQLiteDatabase database;

        public SQLiteDatabase getDatabase() {
            if (database == null)
                database = getWritableDatabase();
            return database;
        }
        public AppsFlyerDBHelp(Context context) {
            super(context, DB_NAME, null, VERSION);
        }
        public AppsFlyerDBHelp(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(SQL_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(SQL_DROP);
            sqLiteDatabase.execSQL(SQL_CREATE);
        }


        public void insertData(AppsFlyerBean info) {
            SQLiteDatabase database = getDatabase();

            ContentValues values = new ContentValues();
            values.put("packageName", info.packageName);
            values.put("install_referrer", info.install_referrer);
            values.put("install_begin_seconds", info.install_begin_seconds);
            values.put("referrer_click_seconds", info.referrer_click_seconds);
            database.insert(TABLE_NAME, null, values);
        }

        public void updateData(AppsFlyerBean info) {
            SQLiteDatabase database = getDatabase();

            if (queryDataByName(info.packageName) == null) {
                insertData(info);
            }else {
                ContentValues values = new ContentValues();
                values.put("packageName", info.packageName);
                if (info.install_referrer != null) {
                    values.put("install_referrer", info.install_referrer);
                }
                if (info.install_begin_seconds != 0) {
                    values.put("install_begin_seconds", info.install_begin_seconds);
                }
                if (info.referrer_click_seconds != 0) {
                    values.put("referrer_click_seconds", info.referrer_click_seconds);
                }
                //修改条件
                String whereClause = "packageName = ?";
                //修改添加参数
                String[] whereArgs={info.packageName};
                database.update(TABLE_NAME, values, whereClause, whereArgs);
            }
        }

        public AppsFlyerBean queryDataByName(String packageName) {
            SQLiteDatabase database = getDatabase();

            Cursor cursor = database.query(TABLE_NAME, null, "packageName = ?", new String[]{packageName}, null, null, null, null);
            AppsFlyerBean info = null;
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    long referrer_click_seconds = cursor.getLong(cursor.getColumnIndex("referrer_click_seconds"));
                    long install_begin_seconds = cursor.getLong(cursor.getColumnIndex("install_begin_seconds"));
                    String install_referrer = cursor.getString(cursor.getColumnIndex("install_referrer"));
                    info = new AppsFlyerBean();
                    info.referrer_click_seconds = referrer_click_seconds;
                    info.install_begin_seconds = install_begin_seconds;
                    info.install_referrer = install_referrer;
                }
                cursor.close();
            }
            return info;
        }

        /**
         * 是否已经插入这条数据
         *//*
        public boolean isExist(SQLiteDatabase db, FileInfo info) {
            Cursor cursor = db.query(TABLE_NAME, null, "url = ?", new String[]{info.getUrl()}, null, null, null, null);
            boolean exist = cursor.moveToNext();
            cursor.close();
            return exist;
        }

        *//**
         * 查询已经存在的一条信息
         *//*
        public FileInfo queryData(SQLiteDatabase db, String url) {
            Cursor cursor = db.query(TABLE_NAME, null, "url = ?", new String[]{url}, null, null, null, null);
            FileInfo info = null;
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String fileName = cursor.getString(cursor.getColumnIndex("fileName"));
                    long length = cursor.getLong(cursor.getColumnIndex("length"));
                    long finished = cursor.getLong(cursor.getColumnIndex("finished"));
                    int version = cursor.getInt(cursor.getColumnIndex("version"));
                    info = new FileInfo();
                    info.setStop(false);
                    info.setFileName(fileName);
                    info.setUrl(url);
                    info.setLength(length);
                    info.setFinished(finished);
                    info.setVersion(version);
                }
                cursor.close();
            }
            return info;
        }

        *//**
         * 查询已经存在的一条信息
         *//*
        public FileInfo queryDataByName(SQLiteDatabase db, String name) {
            Cursor cursor = db.query(TABLE_NAME, null, "fileName = ?", new String[]{name}, null, null, null, null);
            FileInfo info = null;
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    long length = cursor.getLong(cursor.getColumnIndex("length"));
                    long finished = cursor.getLong(cursor.getColumnIndex("finished"));
                    String url = cursor.getString(cursor.getColumnIndex("url"));
                    int version = cursor.getInt(cursor.getColumnIndex("version"));
                    info = new FileInfo();
                    info.setStop(false);
                    info.setFileName(name);
                    info.setUrl(url);
                    info.setLength(length);
                    info.setFinished(finished);
                    info.setVersion(version);
                }
                cursor.close();
            }
            return info;
        }

        public void updateFinishSize(SQLiteDatabase db, FileInfo info) {
            ContentValues values = new ContentValues();
            values.put("finished", info.getFinished());
            values.put("length", info.getLength());
            //修改条件
            String whereClause = "url = ?";
            //修改添加参数
            String[] whereArgs={info.getUrl()};
            db.update(TABLE_NAME, values, whereClause, whereArgs);
        }

        public void updateData(SQLiteDatabase db, FileInfo info) {
            ContentValues values = new ContentValues();
            values.put("fileName", info.getFileName());
            values.put("url", info.getUrl());
            values.put("length", info.getLength());
            values.put("finished", info.getFinished());
            values.put("version", info.getVersion());
            //修改条件
            String whereClause = "url = ?";
            //修改添加参数
            String[] whereArgs={info.getUrl()};
            db.update(TABLE_NAME, values, whereClause, whereArgs);
        }

        public void deleteData(SQLiteDatabase db, FileInfo info) {
            String whereClause = "url = ?";
            String[] whereArgs = {info.getUrl()};
            db.delete(TABLE_NAME, whereClause, whereArgs);
//        String SQLString = "delete from " + TABLE_NAME + " where url = " + info.getUrl();
//        db.execSQL(SQLString);
        }

        *//**
         * 恢复一条下载信息
         *//*
        public void resetData(SQLiteDatabase db, String url) {
            ContentValues values = new ContentValues();
            values.put("finished", 0);
            values.put("length", 0);
            db.update(TABLE_NAME, values, "url = ?", new String[]{url});
        }*/
//    }
}
