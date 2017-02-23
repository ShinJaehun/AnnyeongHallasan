package com.shinjaehun.annyeonghallasan.data;

import android.provider.BaseColumns;

import org.jsoup.Connection;

/**
 * Created by shinjaehun on 2017-02-24.
 */

public class HallasanContract {

    private HallasanContract() { }

    public static final class RoadEntry implements BaseColumns {
        public final static String TABLE_NAME = "roads";
        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_NAME = "name";
        public final static String COLUMN_DATE = "date";

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

    }

    public static final class WeatherEntry implements BaseColumns {

        public enum Category {
            T1H, RN1, SKY, UUU, VVV,
            REH, PTY, LGT, VEC, WSD
        }

        public final static String TABLE_NAME = "weather";
        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_LOCATION = "location";

        public final static String COLUMN_BASE_DATE = "base_date";
        public final static String COLUMN_BASE_TIME = "base_time";
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

    }
}
