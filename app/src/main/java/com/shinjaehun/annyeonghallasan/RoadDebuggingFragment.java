package com.shinjaehun.annyeonghallasan;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.shinjaehun.annyeonghallasan.data.HallasanContract;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by shinjaehun on 2017-06-01.
 */

public class RoadDebuggingFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = RoadDebuggingFragment.class.getSimpleName();

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

    public static final int COL_ROAD_ID = 0;
    public static final int COL_ROAD_NAME = 1;
    public static final int COL_ROAD_TIMESTAMP = 2;
    public static final int COL_ROAD_BASE_DATE = 3;
    public static final int COL_ROAD_RESTRICTION = 4;
    public static final int COL_ROAD_SECTION = 5;
    public static final int COL_ROAD_SNOWFALL = 6;
    public static final int COL_ROAD_FREEZING = 7;
    public static final int COL_ROAD_CHAIN = 8;

    private static final int ROAD_DEBUG_LOADER = 1;

    ImageView road_1100Iv;
    ImageView road_516Iv;
    ImageView road_pyeonghwaIv;
    ImageView road_beonyeongIv;
    ImageView road_hanchangIv;
    ImageView road_namjoIv;
    ImageView road_bijaIv;
    ImageView road_seoseongIv;
    ImageView road_sallok1Iv;
    ImageView road_sallok2Iv;
    ImageView road_myeongnimIv;
    ImageView road_cheomdanIv;
    ImageView road_aejoIv;
    ImageView road_iljuIv;
    static TextView normalTV;

    static ArrayList<ImageView> roadImgs;
//    private boolean isDebugging = false;

    public RoadDebuggingFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_road_debug, container, false);
//
        road_1100Iv = (ImageView)v.findViewById(R.id.road_1100);
        road_516Iv = (ImageView)v.findViewById(R.id.road_516);
        road_pyeonghwaIv = (ImageView)v.findViewById(R.id.road_pyeonghwa);
        road_beonyeongIv= (ImageView)v.findViewById(R.id.road_beonyeong);
        road_hanchangIv = (ImageView)v.findViewById(R.id.road_hanchang);
        road_namjoIv = (ImageView)v.findViewById(R.id.road_namjo);
        road_bijaIv = (ImageView)v.findViewById(R.id.road_bija);
        road_seoseongIv = (ImageView)v.findViewById(R.id.road_seoseong);
        road_sallok1Iv = (ImageView)v.findViewById(R.id.road_sallok1);
        road_sallok2Iv = (ImageView)v.findViewById(R.id.road_sallok2);
        road_myeongnimIv = (ImageView)v.findViewById(R.id.road_myeongnim);
        road_cheomdanIv = (ImageView)v.findViewById(R.id.road_cheomdan);
        road_aejoIv = (ImageView)v.findViewById(R.id.road_aejo);
        road_iljuIv = (ImageView)v.findViewById(R.id.road_ilju);
        normalTV = (TextView)v.findViewById(R.id.normal);

        return v;    }

    private void updateRoad() {
            FetchRoadDebuggingTask roadDebuggingTask = new FetchRoadDebuggingTask(getContext(), MainActivity.mCalendar);
            roadDebuggingTask.execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateRoad();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(ROAD_DEBUG_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if (id == ROAD_DEBUG_LOADER) {

            String sortOrder = HallasanContract.RoadEntry.COLUMN_TIMESTAMP + " DESC";

            String timeStamp = new SimpleDateFormat("yyyyMMddHHmm").format(MainActivity.mCalendar.getTime());
            Uri roadWithDateUri = HallasanContract.RoadEntry.buildRoadUriWithDate(timeStamp);

            return new CursorLoader(getContext(),
                    roadWithDateUri,
                    RoadDebuggingFragment.ROAD_COLUMNS,
                    null,
                    null,
                    sortOrder
            );
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        roadImgs = new ArrayList<>();

        Log.v(LOG_TAG, "onLoadFinished에서 Cursor 크기 " + cursor.getCount());

        while (cursor.moveToNext()) {

            String timeStamp = cursor.getString(RoadFragment.COL_ROAD_TIMESTAMP);

            long roadId = cursor.getLong(RoadFragment.COL_ROAD_ID);
            String location = cursor.getString(RoadFragment.COL_ROAD_NAME);

            int restrict = cursor.getInt(RoadFragment.COL_ROAD_RESTRICTION);

            Log.v(LOG_TAG, "ID : " + roadId + " 장소 : " + location + " 타임스탬프 : " + timeStamp + " 제한여부 : " + restrict);

            if (restrict == HallasanContract.RoadEntry.RESTRICTION_ENABLED) {
                switch (location) {
                    case "1100도로":
                        //여러 ImageView를 동시에 깜빡이게 하기 위해서는 같은 handler를 동시에
                        roadImgs.add(road_1100Iv);
                        break;
                    case "5.16도로":
                        roadImgs.add(road_516Iv);
                        break;
                    case "번영로":
                        roadImgs.add(road_beonyeongIv);
                        break;
                    case "평화로":
                        roadImgs.add(road_pyeonghwaIv);
                        break;
                    case "한창로":
                        roadImgs.add(road_hanchangIv);
                        break;
                    case "남조로":
                        roadImgs.add(road_namjoIv);
                        break;
                    case "비자림로":
                        roadImgs.add(road_bijaIv);
                        break;
                    case "서성로":
                        roadImgs.add(road_seoseongIv);
                        break;
                    case "제1산록도로":
                        roadImgs.add(road_sallok1Iv);
                        break;
                    case "제2산록도로":
                        roadImgs.add(road_sallok2Iv);
                        break;
                    case "명림로":
                        roadImgs.add(road_myeongnimIv);
                        break;
                    case "첨단로":
                        roadImgs.add(road_cheomdanIv);
                        break;
                    case "기타도로":
                        if (cursor.getString(RoadFragment.COL_ROAD_SECTION).contains("애조로")) {
                            roadImgs.add(road_aejoIv);
                        }
                        if (cursor.getString(RoadFragment.COL_ROAD_SECTION).contains("일주도로")) {
                            roadImgs.add(road_iljuIv);
                        }
                        break;
                    default:
                        break;
                }
            }

            if (roadImgs.size() == 0) {
                normalTV.setVisibility(View.VISIBLE);
                startBlink(normalTV);
            } else {
                for (ImageView i : roadImgs) {

                    i.setVisibility(View.VISIBLE);
                    startBlink(i);
                }
            }

//        FrameLayout roadStatusL = (FrameLayout)((MainActivity)mContext).findViewById(R.id.layout_road_status);
//        roadStatusL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialogInfo = new DialogInfo(mContext, clickListener, roads);
//                dialogInfo.setCanceledOnTouchOutside(false);
//                //dialogResult 외부 화면은 터치해도 반응하지 않음
//                dialogInfo.show();
//            }
//        });

        }

    }

    private void startBlink(View i) {
        final Animation animation = new AlphaAnimation((float) 0.5, 0);
        animation.setDuration(500);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        i.startAnimation(animation);
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
