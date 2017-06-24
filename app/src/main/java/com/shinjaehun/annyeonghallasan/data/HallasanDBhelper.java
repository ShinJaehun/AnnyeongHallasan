package com.shinjaehun.annyeonghallasan.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.shinjaehun.annyeonghallasan.data.HallasanContract.RoadEntry;
import com.shinjaehun.annyeonghallasan.data.HallasanContract.WeatherEntry;

/**
 * Created by shinjaehun on 2017-05-20.
 */

public class HallasanDBhelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "hallasan.db";


    public HallasanDBhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_ROAD_TABLE = "CREATE TABLE " + RoadEntry.TABLE_NAME + " (" +
                RoadEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RoadEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                RoadEntry.COLUMN_TIMESTAMP + " INTEGER NOT NULL, " +
                RoadEntry.COLUMN_BASE_DATE + " STRING NOT NULL, " +
                RoadEntry.COLUMN_RESTRICTION + " INTEGER NOT NULL, " +
                RoadEntry.COLUMN_SECTION + " TEXT NOT NULL, " +
                RoadEntry.COLUMN_SNOWFALL + " REAL NOT NULL, " +
                RoadEntry.COLUMN_FREEZING + " REAL NOT NULL, " +
                RoadEntry.COLUMN_CHAIN + " INTEGER NOT NULL" +
                ");";

        final String SQL_CREATE_WEATHER_TABLE = "CREATE TABLE " + WeatherEntry.TABLE_NAME + " (" +
                WeatherEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WeatherEntry.COLUMN_LOCATION + " TEXT NOT NULL, " +
                WeatherEntry.COLUMN_TIMESTAMP + " INTEGER NOT NULL, " +
                WeatherEntry.COLUMN_BASE_DATE + " STRING NOT NULL, " +
                WeatherEntry.COLUMN_BASE_TIME + " STRING NOT NULL, " +
                WeatherEntry.COLUMN_NX + " INTEGER NOT NULL, " +
                WeatherEntry.COLUMN_NY + " INTEGER NOT NULL, " +
                WeatherEntry.COLUMN_T1H + " REAL NOT NULL, " +
                WeatherEntry.COLUMN_RN1 + " REAL NOT NULL, " +
                WeatherEntry.COLUMN_SKY + " REAL NOT NULL, " +
                WeatherEntry.COLUMN_UUU + " REAL NOT NULL, " +
                WeatherEntry.COLUMN_VVV + " REAL NOT NULL, " +
                WeatherEntry.COLUMN_REH + " REAL NOT NULL, " +
                WeatherEntry.COLUMN_PTY + " REAL NOT NULL, " +
                WeatherEntry.COLUMN_LGT + " REAL NOT NULL, " +
                WeatherEntry.COLUMN_VEC + " REAL NOT NULL, " +
                WeatherEntry.COLUMN_WSD + " REAL NOT NULL" +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_ROAD_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RoadEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WeatherEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}
