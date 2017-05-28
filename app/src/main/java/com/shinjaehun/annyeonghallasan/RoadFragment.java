package com.shinjaehun.annyeonghallasan;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;

import com.shinjaehun.annyeonghallasan.data.HallasanContract;
import com.shinjaehun.annyeonghallasan.model.Road;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by shinjaehun on 2017-05-22.
 */

public class RoadFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = RoadFragment.class.getSimpleName();

    public static final String[] ROAD_COLUMNS = {
            HallasanContract.RoadEntry.TABLE_NAME + "." + HallasanContract.RoadEntry._ID,
            HallasanContract.RoadEntry.COLUMN_NAME,
            HallasanContract.RoadEntry.COLUMN_TIMESTAMP,
            HallasanContract.RoadEntry.COLUMN_BASE_DATE,
            HallasanContract.RoadEntry.COLUMN_RESTRICTION,
            HallasanContract.RoadEntry.COLUMN_SECTION,
            HallasanContract.RoadEntry.COLUMN_SNOWFALL,
            HallasanContract.RoadEntry.COLUMN_FREEZING,
            HallasanContract.RoadEntry.COLUMN_CHAIN
    };

    static final int COL_ROAD_ID = 0;
    static final int COL_ROAD_NAME = 1;
    static final int COL_ROAD_TIMESTAMP = 2;
    static final int COL_ROAD_BASE_DATE = 3;
    static final int COL_ROAD_RESTRICTION = 4;
    static final int COL_ROAD_SECTION = 5;
    static final int COL_ROAD_SNOWFALL = 6;
    static final int COL_ROAD_FREEZING = 7;
    static final int COL_ROAD_CHAIN = 8;

//    private static Calendar mCalendar;
    private static final int ROAD_LOADER = 1;

    public RoadFragment() {
//        mCalendar = Calendar.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getLoaderManager().restartLoader(ROAD_LOADER, null, this);

        View v = inflater.inflate(R.layout.fragment_road, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String sortOrder = HallasanContract.RoadEntry.COLUMN_TIMESTAMP + " ASC";
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmm").format(MainActivity.mCalendar.getTime());
        Uri roadWithDateUri = HallasanContract.RoadEntry.buildRoadUriWithDate(timeStamp);

        return new CursorLoader(getContext(),
                roadWithDateUri,
                RoadFragment.ROAD_COLUMNS,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        while (cursor.moveToNext()) {

            long roadId = cursor.getLong(RoadFragment.COL_ROAD_ID);
            String location = cursor.getString(RoadFragment.COL_ROAD_NAME);
            String timeStamp = cursor.getString(RoadFragment.COL_ROAD_TIMESTAMP);
            int restrict = cursor.getInt(RoadFragment.COL_ROAD_RESTRICTION);

            Log.v(LOG_TAG,  "ID : " + roadId + " 장소 : " + location + " 타임스탬프 : " + timeStamp + " 제한여부 : " + restrict);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
