package com.shinjaehun.annyeonghallasan;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.shinjaehun.annyeonghallasan.data.HallasanContract;
import com.shinjaehun.annyeonghallasan.sync.HallasanSyncAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    static final int COL_WEATHER_NX = 4;
    static final int COL_WEATHER_NY = 5;
    static final int COL_WEATHER_T1H = 6;
    static final int COL_WEATHER_RN1 = 7;
    static final int COL_WEATHER_SKY = 8;
    static final int COL_WEATHER_UUU = 9;
    static final int COL_WEATHER_VVV = 10;
    static final int COL_WEATHER_REH = 11;
    static final int COL_WEATHER_PTY = 12;
    static final int COL_WEATHER_LGT = 13;
    static final int COL_WEATHER_VEC = 14;
    static final int COL_WEATHER_WSD = 15;

    private static final int WEATHER_LOADER = 0;
    private WeatherAdapter mWeatherAdapter;

    WeatherDialog weatherDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        mWeatherAdapter = new WeatherAdapter(getContext(), null, 0);
        //이거 씨발 getActivity() 누구야!  cursor is deactivated prior to calling this method 이런 전혀 관련이 없는 것 같은 오류 메시지나 내 보이고1!!

        View v = inflater.inflate(R.layout.fragment_weather, container, false);

        GridView gridView = (GridView) v.findViewById(R.id.gridview_weather);

        gridView.setAdapter(mWeatherAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //gridView 각 아이템에 대한 click event 처리
                Cursor cursor = (Cursor)adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    String location = cursor.getString(WeatherFragment.COL_WEATHER_LOCATION);
                    String baseDate = cursor.getString(WeatherFragment.COL_WEATHER_BASE_DATE);
                    long timeStamp = cursor.getLong(WeatherFragment.COL_WEATHER_TIMESTAMP);
//                    float sky = cursor.getFloat(WeatherFragment.COL_WEATHER_SKY);
//                    float pty = cursor.getFloat(WeatherFragment.COL_WEATHER_PTY);
                    int sky = cursor.getInt(WeatherFragment.COL_WEATHER_SKY);
                    int pty = cursor.getInt(WeatherFragment.COL_WEATHER_PTY);
                    float t1h = cursor.getFloat(WeatherFragment.COL_WEATHER_T1H);
                    float reh = cursor.getFloat(WeatherFragment.COL_WEATHER_REH);
                    float rn1 = cursor.getFloat(WeatherFragment.COL_WEATHER_RN1);
                    float vec = (cursor.getFloat(WeatherFragment.COL_WEATHER_VEC));
                    float wsd = (cursor.getFloat(WeatherFragment.COL_WEATHER_WSD));

                    weatherDialog = new WeatherDialog(getContext(), clickListener,
                            location, baseDate, timeStamp, sky, pty, t1h, reh, rn1, vec, wsd);
                    weatherDialog.setCanceledOnTouchOutside(false);
                    weatherDialog.show();
                    //Dialog 보여주기
                }

                //여기서 cursor.close해야 하는 것 아닌가?
                //아냐 여기서 닫으면 이미 닫아버린 DB를 다시 열려고 한다고 오류가 발생한다.
//                cursor.close();
            }
        });

        return v;

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            //언제부터 clickListener를 이렇게 표현하기 시작했을까?
            weatherDialog.dismiss();
            weatherDialog = null;
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        //Leak을 막기 위해 onStop()에서 Dialog dismiss()
        if (weatherDialog != null) {
            weatherDialog.dismiss();
            weatherDialog = null;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(WEATHER_LOADER, null, this);
        //Loader init
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if (id == WEATHER_LOADER) {

            String sortOrder = HallasanContract.WeatherEntry._ID + " DESC limit 6";
            //DB에 입력된 순서로 뒤에서 6번째까지(각 위치에 해당하는 정보) cursor로 받아옴

            return new CursorLoader(getContext(),
                    HallasanContract.WeatherEntry.CONTENT_URI,
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
//        Log.v(LOG_TAG, "onLoadFinished에서 Weather Cursor 크기 " + cursor.getCount());
//        while (cursor.moveToNext()) {
//
//            long timeStamp = cursor.getLong(WeatherFragment.COL_WEATHER_TIMESTAMP);
//            long id = cursor.getLong(WeatherFragment.COL_WEATHER_ID);
//            String location = cursor.getString(WeatherFragment.COL_WEATHER_LOCATION);
//
//            Log.v(LOG_TAG, "ID : " + id + " 장소 : " + location + " 타임스탬프 : " + timeStamp);
//        }
        mWeatherAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mWeatherAdapter.swapCursor(null);
    }
}
