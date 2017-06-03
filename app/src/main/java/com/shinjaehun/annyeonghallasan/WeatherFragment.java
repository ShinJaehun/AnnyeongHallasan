package com.shinjaehun.annyeonghallasan;

import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.shinjaehun.annyeonghallasan.data.HallasanContract;
import com.shinjaehun.annyeonghallasan.sync.HallasanSyncAdapter;

/**
 * Created by shinjaehun on 2017-05-22.
 */

public class WeatherFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private final String LOG_TAG = WeatherFragment.class.getSimpleName();

    public static final String[] WEATHER_COLUMNS = {
            //getColumnIndex 대신 cursor의 값을 쉽게 사용하기 위한 Projection
            HallasanContract.WeatherEntry.TABLE_NAME + "." + HallasanContract.WeatherEntry._ID,
            HallasanContract.WeatherEntry.COLUMN_LOCATION,
            HallasanContract.WeatherEntry.COLUMN_TIMESTAMP,
            HallasanContract.WeatherEntry.COLUMN_BASE_DATE,
            HallasanContract.WeatherEntry.COLUMN_BASE_TIME,
            HallasanContract.WeatherEntry.COLUMN_NX,
            HallasanContract.WeatherEntry.COLUMN_NY,
            HallasanContract.WeatherEntry.COLUMN_T1H,
            HallasanContract.WeatherEntry.COLUMN_RN1,
            HallasanContract.WeatherEntry.COLUMN_SKY,
            HallasanContract.WeatherEntry.COLUMN_UUU,
            HallasanContract.WeatherEntry.COLUMN_VVV,
            HallasanContract.WeatherEntry.COLUMN_REH,
            HallasanContract.WeatherEntry.COLUMN_PTY,
            HallasanContract.WeatherEntry.COLUMN_LGT,
            HallasanContract.WeatherEntry.COLUMN_VEC,
            HallasanContract.WeatherEntry.COLUMN_WSD
    };

    //Projection에서 몇몇 값만 받아오기로 했다면 아래 COL도 변경되어야 함
    static final int COL_WEATHER_ID = 0;
    static final int COL_WEATHER_LOCATION = 1;
    static final int COL_WEATHER_TIMESTAMP = 2;
    static final int COL_WEATHER_BASE_DATE = 3;
    static final int COL_WEATHER_BASE_TIME = 4;
    static final int COL_WEATHER_NX = 5;
    static final int COL_WEATHER_NY = 6;
    static final int COL_WEATHER_T1H = 7;
    static final int COL_WEATHER_RN1 = 8;
    static final int COL_WEATHER_SKY = 9;
    static final int COL_WEATHER_UUU = 10;
    static final int COL_WEATHER_VVV = 11;
    static final int COL_WEATHER_REH = 12;
    static final int COL_WEATHER_PTY = 13;
    static final int COL_WEATHER_LGT = 14;
    static final int COL_WEATHER_VEC = 15;
    static final int COL_WEATHER_WSD = 16;

    private WeatherAdapter mWeatherAdapter;
    private static final int WEATHER_LOADER = 0;

    public WeatherFragment() {
    }

//    private SharedPreferences timePrefs = null;
//    private String mTimeStamp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateWeather();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        mWeatherAdapter = new WeatherAdapter(getContext(), null, 0);
        //이거 씨발 getActivity() 누구야!  cursor is deactivated prior to calling this method 이런 전혀 관련이 없는 것 같은 오류 메시지나 내 보이고1!!

        View v = inflater.inflate(R.layout.fragment_weather, container, false);

        GridView gridView = (GridView)v.findViewById(R.id.gridview_weather);
        gridView.setAdapter(mWeatherAdapter);

        return v;

    }

    private void updateWeather() {
        FetchWeatherTask weatherTask = new FetchWeatherTask(getContext(), MainActivity.mCalendar, MainActivity.mTimeStamp);
        weatherTask.execute();

//        HallasanSyncAdapter.syncImmediately(getContext(), MainActivity.mCalendar, MainActivity.mTimeStamp);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(WEATHER_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if (id == WEATHER_LOADER) {

            String sortOrder = HallasanContract.WeatherEntry.COLUMN_TIMESTAMP + " ASC";

//            timePrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
//            mTimeStamp = timePrefs.getString(MainActivity.TIME_STAMP, null);

//            String timeStamp = new SimpleDateFormat("yyyyMMddHHmm").format(MainActivity.mCalendar.getTime());

            Uri weatherWithDateUri = HallasanContract.WeatherEntry.buildWeatherUriWithDate(MainActivity.mTimeStamp);

            return new CursorLoader(getContext(),
                    weatherWithDateUri,
                    WEATHER_COLUMNS,
                    null,
                    null,
                    sortOrder
            );
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.v(LOG_TAG, "onLoadFinished에서 Weather Cursor 크기 " + cursor.getCount());

        while (cursor.moveToNext()) {

            String timeStamp = cursor.getString(WeatherFragment.COL_WEATHER_TIMESTAMP);

            long id = cursor.getLong(WeatherFragment.COL_WEATHER_ID);
            String location = cursor.getString(WeatherFragment.COL_WEATHER_LOCATION);

            Log.v(LOG_TAG, "ID : " + id + " 장소 : " + location + " 타임스탬프 : " + timeStamp);
        }

        mWeatherAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mWeatherAdapter.swapCursor(null);
    }
}
