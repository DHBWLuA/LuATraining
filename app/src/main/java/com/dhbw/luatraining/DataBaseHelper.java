package com.dhbw.luatraining;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DataBaseHelper extends SQLiteAssetHelper
{
    private static final String DATABASE_NAME = "questionnaire.db";
    private static final int DATABASE_VERSION = 2;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setForcedUpgrade();
    }

    public Cursor queryReadBySql(String sql)
    {
        Cursor curs = this.getReadableDatabase().rawQuery(sql, null);
        LogHelper.addLogLine("SQL - " + sql + " => Number of Results: " + curs.getCount());
        return curs;
    }

    public void queryWriteBySql(String sql)
    {
        this.getReadableDatabase().rawQuery(sql, null).close();
        LogHelper.addLogLine("SQL - " + sql);
    }
}