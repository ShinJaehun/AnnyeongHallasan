package com.shinjaehun.annyeonghallasan.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import org.jsoup.Connection;

/**
 * Created by shinjaehun on 2017-05-20.
 */

public class HallasanContract {

    public static final String CONTENT_AUTHORITY = "com.shinjaehun.annyeonghallasan";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_WEATHER = "weather";
    public static final String PATH_ROAD = "road";

    private HallasanContract() { }

    public static final class RoadEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ROAD).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ROAD;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ROAD;


        public final static String TABLE_NAME = "roads";
        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_NAME = "name";
        public final static String COLUMN_TIMESTAMP = "time_stamp";
        public final static String COLUMN_BASE_DATE = "base_date";

        public final static String COLUMN_RESTRICTION = "restriction";

        public final static int RESTRICTION_ENABLED = 1;
        public final static int RESTRICTION_DISABLED = 0;

        public final static String COLUMN_SECTION = "section";
        public final static String COLUMN_SNOWFALL = "snowfall";
        public final static String COLUMN_FREEZING = "freezing";

        public final static String COLUMN_CHAIN = "chain";

        public final static int CHAIN_NONE = 0;
        public final static int CHAIN_SMALL = 1;
        public final static int CHAIN_BIG = 2;

        public static Uri buildRoadUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildRoadUriWithDate(String timeStamp) {
            return CONTENT_URI.buildUpon().
                    appendPath("search").
                    appendQueryParameter(COLUMN_TIMESTAMP, timeStamp).build();
        }

        public static String getTimeStampFromUri(Uri uri) {
            String timeStamp = uri.getQueryParameter(COLUMN_TIMESTAMP);
            if (timeStamp != null && timeStamp.length() > 0) {
                return timeStamp;
            } else {
                return null;
            }
        }

    }

    public static final class WeatherEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_WEATHER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WEATHER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WEATHER;

        public enum Category {
            T1H, RN1, SKY, UUU, VVV,
            REH, PTY, LGT, VEC, WSD
        }

        public final static String TABLE_NAME = "weathers";
        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_LOCATION = "location";
        public final static String COLUMN_TIMESTAMP = "time_stamp";

        public final static String COLUMN_BASE_DATE = "base_date";
        public final static String COLUMN_NX = "nx";
        public final static String COLUMN_NY = "ny";
        public final static String COLUMN_T1H = "t1h";
        public final static String COLUMN_RN1 = "rn1";
        public final static String COLUMN_SKY = "sky";
        public final static String COLUMN_UUU = "uuu";
        public final static String COLUMN_VVV = "vvv";
        public final static String COLUMN_REH = "reh";
        public final static String COLUMN_PTY = "pty";
        public final static String COLUMN_LGT = "lgt";
        public final static String COLUMN_VEC = "vec";
        public final static String COLUMN_WSD = "wsd";

        public static Uri buildWeatherUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildWeatherUriWithDate(String timeStamp) {
            return CONTENT_URI.buildUpon().
                    appendPath("search").
                    appendQueryParameter(COLUMN_TIMESTAMP, timeStamp).build();
        }

        public static String getTimeStampFromUri(Uri uri) {
            String timeStamp = uri.getQueryParameter(COLUMN_TIMESTAMP);
            if (timeStamp != null && timeStamp.length() > 0) {
                return timeStamp;
            } else {
                return null;
            }
        }


    }

}