package com.shinjaehun.annyeonghallasan.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by shinjaehun on 2017-05-20.
 */

public class HallasanProvider extends ContentProvider {
    
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private HallasanDBhelper mDBHelper;

    private static final int WEATHER = 100;
    private static final int WEATHER_WITH_DATE = 101;
    private static final int ROAD = 200;
    private static final int ROAD_WITH_DATE = 201;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = HallasanContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, HallasanContract.PATH_WEATHER, WEATHER);
        matcher.addURI(authority, HallasanContract.PATH_WEATHER + "/*", WEATHER_WITH_DATE);
        matcher.addURI(authority, HallasanContract.PATH_ROAD, ROAD);
        matcher.addURI(authority, HallasanContract.PATH_ROAD + "/*", ROAD_WITH_DATE);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDBHelper = new HallasanDBhelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor returnCursor;
        SQLiteDatabase db = mDBHelper.getReadableDatabase();

        switch (sUriMatcher.match(uri)) {
            case WEATHER:
                returnCursor = db.query(HallasanContract.WeatherEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case ROAD:
                returnCursor = db.query(HallasanContract.RoadEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case WEATHER_WITH_DATE:
                String weatherTimeStamp = HallasanContract.WeatherEntry.getTimeStampFromUri(uri);
                returnCursor = db.query(
                        HallasanContract.WeatherEntry.TABLE_NAME,
                        projection,
                        HallasanContract.WeatherEntry.COLUMN_TIMESTAMP + " = ? ",
                        new String[] { weatherTimeStamp },
                        null,
                        null,
                        sortOrder);
                break;
            case ROAD_WITH_DATE:
                String roadTimeStamp = HallasanContract.RoadEntry.getTimeStampFromUri(uri);
                returnCursor = db.query(
                        HallasanContract.RoadEntry.TABLE_NAME,
                        projection,
                        HallasanContract.RoadEntry.COLUMN_TIMESTAMP + " = ? ",
                        new String[] { roadTimeStamp },
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);
        }
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case WEATHER:
                return HallasanContract.WeatherEntry.CONTENT_TYPE;
            case ROAD:
                return HallasanContract.RoadEntry.CONTENT_TYPE;
            case WEATHER_WITH_DATE:
                return HallasanContract.WeatherEntry.CONTENT_ITEM_TYPE;
            case ROAD_WITH_DATE:
                return HallasanContract.RoadEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        Uri returnUri;

        switch (sUriMatcher.match(uri)) {
            case WEATHER:
                long weatheId = db.insert(HallasanContract.WeatherEntry.TABLE_NAME, null, contentValues);
                if (weatheId > 0)
                    returnUri = HallasanContract.WeatherEntry.buildWeatherUri(weatheId);
                else
                    throw new android.database.SQLException("Failed to insert Weather row into " + uri);
                break;
            case ROAD:
                long roadId = db.insert(HallasanContract.RoadEntry.TABLE_NAME, null, contentValues);
                if (roadId > 0)
                    returnUri = HallasanContract.RoadEntry.buildRoadUri(roadId);
                else
                    throw new android.database.SQLException("Failed to insert Road row into " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int rowsDeleted;
        if (selection == null) selection = "1";
        switch (sUriMatcher.match(uri)) {
            case WEATHER:
                rowsDeleted = db.delete(HallasanContract.WeatherEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ROAD:
                rowsDeleted = db.delete(HallasanContract.RoadEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);

        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int rowsUpdated;
        switch (sUriMatcher.match(uri)) {
            case WEATHER:
                rowsUpdated = db.update(HallasanContract.WeatherEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case ROAD:
                rowsUpdated = db.update(HallasanContract.RoadEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);

        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
            case WEATHER:
                db.beginTransaction();
                int returnWeatherCount = 0;
                try {
                    for (ContentValues value : values) {
                        long weatherId = db.insert(HallasanContract.WeatherEntry.TABLE_NAME, null, value);
                        if (weatherId != -1) {
                            returnWeatherCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnWeatherCount;
            case ROAD:
                db.beginTransaction();
                int returnRoadCount = 0;
                try {
                    for (ContentValues value : values) {
                        long roadId = db.insert(HallasanContract.RoadEntry.TABLE_NAME, null, value);
                        if (roadId != -1) {
                            returnRoadCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnRoadCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mDBHelper.close();
        super.shutdown();
    }
}
