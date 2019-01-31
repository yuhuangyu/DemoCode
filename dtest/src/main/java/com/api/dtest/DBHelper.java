package com.api.dtest;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by longyu on 2016/11/29.
 */
public class DBHelper {

    public static final String DBNAME = "autosdk_down_db.db";

    public static final int VERSION = 1;

    private static final String IF_EXIT_TABLE_TEMPLET = "SELECT count(*) as c FROM sqlite_master WHERE type='table' AND name='%s'";

    private Context context;

    private DB db;

    private static DBHelper instance;

    private DBHelper(Context context) {
        this.context = context;

    }

    public static DBHelper getInstance(Context context) {
        if (instance == null)
            instance = new DBHelper(context);

        if (instance.db==null){
            instance.db = new DB(context, DBNAME, null, VERSION);
        }
        return instance;
    }

    public void close() {
        if (db != null) {
            db.close();
            db.database = null;
        }
    }

    public boolean add(String table, Map<String, Object> values) {
        if (isTableExist(table)) {
            return insert(table, values);
        } else {
            if (createTable(table, values)) {
                return insert(table, values);
            }
        }
        return false;
    }

    private boolean createTable(String table, Map<String, Object> values) {
        StringBuilder sb = new StringBuilder(
                String.format("CREATE TABLE IF NOT EXISTS %s ( Id INTEGER PRIMARY KEY AUTOINCREMENT", table)
        );
        Set<String> set = values.keySet();
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            String key = it.next();
            sb.append(String.format(",%s", key));
        }
        sb.append(");");
        try {
            db.getDatabase().execSQL(sb.toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("sdk","create table error ", e);
        } finally {
            close();
        }
        return false;
    }

    private boolean insert(String table, Map<String, Object> values) {
        if (!isTableExist(table)) {
            return false;
        }
        StringBuilder sb = new StringBuilder(String.format("insert into %s (", table));
        Set<String> set = values.keySet();
        Iterator<String> it = set.iterator();
        String[] keys = set.toArray(new String[]{});
        while (it.hasNext()) {
            String key = it.next();
            sb.append(String.format("%s,", key));
        }
        sb = sb.replace(sb.length() - 1, sb.length(), "");
        sb.append(")");
        sb.append(" values(");
        for (int i = 0; i < keys.length; i++) {
            sb.append(String.format("'%s',", values.get(keys[i])));
        }
        sb = sb.replace(sb.length() - 1, sb.length(), "");
        sb.append(");");
        try {
            db.getDatabase().execSQL(sb.toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }

    public boolean update(String table, Map<String, Object> values, String where) {
        if (!isTableExist(table)) {
            return false;
        }
        try {
            StringBuilder sb = new StringBuilder(String.format("update %s set ", table));
            Set<String> set = values.keySet();
            Iterator<String> it = set.iterator();
            String[] keys = new String[values.size()];
            while (it.hasNext()) {
                String key = it.next();
                if (key.equalsIgnoreCase("Id"))
                    continue;
                sb.append(String.format("%s='%s' ,", key, values.get(key)));
            }
            sb = sb.replace(sb.length() - 1, sb.length(), "");
            if (!TextUtils.isEmpty(where)) {
                sb.append(where);
            }
            db.getDatabase().execSQL(sb.toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }

    public boolean addOrUpdate(String table, Map<String, Object> values
    ) {
        String key = values.keySet().iterator().next();
        if (get(table, key, String.format("where %s=?", key), new String[]{values.get(key).toString()}).size() > 0) {
            return update(table, values, String.format("where %s='%s'", key, values.get(key)));
        } else {
            return add(table, values);
        }
    }

    public boolean addOrUpdate(String table, Map<String, Object> values, String where, String... whereArgs
    ) {
        String key = values.keySet().iterator().next();
        if (get(table, key, where, whereArgs).size() > 0) {
            return update(table, values, parsParams(where, whereArgs));
        } else {
            return add(table, values);
        }
    }

    public boolean del(String table, String where, String[] values) {
        if (!isTableExist(table))
            return false;
        try {
            return db.getDatabase().delete(table, where, values) >= 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }

    public boolean dropTable(String table) {
        if (!isTableExist(table))
            return false;
        try {
            db.getDatabase().execSQL("drop table IF EXISTS " + table);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }

    public List<Map<String, String>> get(String table, String where, String... values) {
        if (!isTableExist(table))
            return new ArrayList<>();
        Cursor cursor = null;
        if (TextUtils.isEmpty(where)) {
            where = "where 1=1";
            values = new String[]{};
        }
        try {
            cursor = db.getDatabase().rawQuery(String.format("select * from %s %s", table, where), values);
            List<Map<String, String>> re = new ArrayList<>();
            while (cursor.moveToNext()) {
                String[] names = cursor.getColumnNames();
                Map<String, String> result = new HashMap<>();
                for (int i = 0; i < names.length; i++) {
                    String key = names[i];
                    if (key.equalsIgnoreCase("Id")) {
                        String value = String.valueOf(cursor.getInt(cursor.getColumnIndex(key)));
                        result.put(key, value);
                        continue;
                    }
                    String value = cursor.getString(cursor.getColumnIndex(key));
                    result.put(key, value);
                }
                re.add(result);
            }
            return re;
        } finally {
            if (cursor != null)
                cursor.close();
            close();
        }

    }

    public List<Map<String, String>> get(String table, String queryKey, String where, String... values) {

        if (!isTableExist(table))
            return new ArrayList<>();
        Cursor cursor = null;
        if (TextUtils.isEmpty(where)) {
            where = "where 1=1";
            values = new String[]{};
        }
        try {
            cursor = db.getDatabase().rawQuery(String.format("select %s from %s %s", queryKey, table, where), values);
            List<Map<String, String>> re = new ArrayList<>();
            while (cursor.moveToNext()) {
                Map<String, String> result = new HashMap<>();
                String value = cursor.getString(cursor.getColumnIndex(queryKey));
                result.put(queryKey, value);
                re.add(result);
            }
            return re;
        } finally {
            if (cursor != null)
                cursor.close();
            close();
        }

    }

    private boolean isTableExist(String table) {
        boolean isTableExist = false;
        synchronized (DBHelper.class) {
            String sql = String.format(IF_EXIT_TABLE_TEMPLET, table);
            Cursor cursor = db.getDatabase().rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    isTableExist = true;
                }
            }
            cursor.close();
            close();
        }
        return isTableExist;
    }

    private String parsParams(String param, String... values) {
        String[] params = param.split("\\?");
        for (int i = 0; i < values.length; i++) {
            String value = values[i];
            params[i] += String.format("'%s'", value);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.length; i++) {
            sb.append(params[i]);
        }

        if (values.length > 0) {
            Log.i("sdk",sb.toString());
            return sb.toString();
        }

        return null;
    }

    private static class DB extends SQLiteOpenHelper {
        /**
         * @Hide
         */
        private SQLiteDatabase database;

        public SQLiteDatabase getDatabase() {
            if (database == null)
                database = getWritableDatabase();
            return database;
        }

        public DB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }
}
