package com.shinjaehun.annyeonghallasan;

import android.database.Cursor;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.shinjaehun.annyeonghallasan.data.HallasanContract;

import java.util.ArrayList;

/**
 * Created by shinjaehun on 2017-05-22.
 */

public class RoadFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
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
    private static final String LOG_TAG = RoadFragment.class.getSimpleName();

    private static final int ROAD_LOADER = 1;

    static TextView normalTV;
    static ArrayList<ImageView> roadImgs;
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

    TextView unavailableTV;
    private Animation animation;

    static boolean isDebugging;

    static int month;

    public static final RoadFragment newInstance(boolean d, int m) {
        isDebugging = d;
        month = m;
        return new RoadFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        setHasOptionsMenu(true);

//멍청하게도 debugging 스위치를 HallasanSyncAdapter와 RoadFragment 양쪽에 뒀다.
        //반드시 양쪽 함께 활성화시킬 것

        View v = inflater.inflate(R.layout.fragment_road, container, false);

        road_1100Iv = (ImageView) v.findViewById(R.id.road_1100);
        road_516Iv = (ImageView) v.findViewById(R.id.road_516);
        road_pyeonghwaIv = (ImageView) v.findViewById(R.id.road_pyeonghwa);
        road_beonyeongIv = (ImageView) v.findViewById(R.id.road_beonyeong);
        road_hanchangIv = (ImageView) v.findViewById(R.id.road_hanchang);
        road_namjoIv = (ImageView) v.findViewById(R.id.road_namjo);
        road_bijaIv = (ImageView) v.findViewById(R.id.road_bija);
        road_seoseongIv = (ImageView) v.findViewById(R.id.road_seoseong);
        road_sallok1Iv = (ImageView) v.findViewById(R.id.road_sallok1);
        road_sallok2Iv = (ImageView) v.findViewById(R.id.road_sallok2);
        road_myeongnimIv = (ImageView) v.findViewById(R.id.road_myeongnim);
        road_cheomdanIv = (ImageView) v.findViewById(R.id.road_cheomdan);
        road_aejoIv = (ImageView) v.findViewById(R.id.road_aejo);
        road_iljuIv = (ImageView) v.findViewById(R.id.road_ilju);
        normalTV = (TextView) v.findViewById(R.id.text_normal);
        unavailableTV = (TextView) v.findViewById(R.id.text_unavailable);

        if (isDebugging) {
            FrameLayout roadMapL = (FrameLayout) v.findViewById(R.id.layout_road_status);
            roadMapL.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    GetRoadFromDBTask getRoadFromDBTask = new GetRoadFromDBTask(getContext());
                    getRoadFromDBTask.execute();
                }
            });
        } else {

            if (month < 04 || month > 10) {
                FrameLayout roadMapL = (FrameLayout) v.findViewById(R.id.layout_road_status);
                roadMapL.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        GetRoadFromDBTask getRoadFromDBTask = new GetRoadFromDBTask(getContext());
                        getRoadFromDBTask.execute();
                    }
                });
            }
        }

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        animation = new AlphaAnimation((float) 0.5, 0);


//        SharedPreferences pref = getContext().getSharedPreferences("sync_road_pref", Activity.MODE_PRIVATE);
//        long lastSyncTime = pref.getLong("last_road_sync_time", 0);
//
//        Calendar calendar = Calendar.getInstance();
//
//        if (isDebugging) {
////            long now = calendar.getTime().getTime();
////            long min = (now - lastSyncTime) / 60000;
////            if (lastSyncTime == 0 || min >= 30) {
////                SharedPreferences.Editor editor = pref.edit();
////                editor.putLong("last_road_sync_time", now);
////                editor.commit();
//                animation = new AlphaAnimation((float) 0.5, 0);
//                HallasanSyncAdapter.syncImmediately(getActivity());
////            }
//        } else {
//            month = Integer.parseInt(new SimpleDateFormat("MM").format(calendar.getTime()));
//
//            if (month < 04 || month > 10) {
//
//                long now = calendar.getTime().getTime();
//
//                long min = (now - lastSyncTime) / 60000;
//
//                if (lastSyncTime == 0 || min >= 30) {
//                    SharedPreferences.Editor editor = pref.edit();
//
//                    editor.putLong("last_road_sync_time", now);
//                    editor.commit();
//                    animation = new AlphaAnimation((float) 0.5, 0);
////                    getLoaderManager().restartLoader(ROAD_LOADER, null, this);
//// 백그라운드에서 다시 활성화시켰을 때 Loader 재시작 이거 안 하면 Loader는 재시작 하더라도 그대로...
//                    //syncImmediately()를 WeatherFragment와 RoadFragment 양쪽에서 실행하기 때문에 DB Insert()가 필요 이상으로 반복되는 문제가 있다.
//                    //그러나 삼성 Device 문제 때문에 어쩔 수 없는 면이 있다.
//
//                    //몇몇 삼성 Device에서 발생하는 SyncAdapter 관련 오류로 Loader를 재시작하지 않고 OnResume에서 DB Push를 실시한다.
//                    //릴리즈 전에 restartLoader 주석처리하고 syncImmediately를 해제할 것
//                    HallasanSyncAdapter.syncImmediately(getActivity());
//                }
//            }
//        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(ROAD_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

//    void timeStampChanged() {
//        // timestamp가 변경되면 이걸 실행시켜서 Loader의 Uri를 변경해야 함!
//        getLoaderManager().restartLoader(ROAD_LOADER, null, this);
//    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "RoadFragment의 onCreateLoader입니다.");

            if (id == ROAD_LOADER) {

                String sortOrder = HallasanContract.RoadEntry._ID + " DESC limit 13";

//            String timeStamp = new SimpleDateFormat("yyyyMMddHHmm").format(MainActivity.mCalendar.getTime());

//            timePrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
//            mTimeStamp = timePrefs.getString(MainActivity.TIME_STAMP, null);

//            SharedPreferences timePrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
//            String mainTimeStamp = timePrefs.getString(MainActivity.TIME_STAMP, null);

                // 결국은 timestamp에 따라 loader가 달라지니 timestamp가 변경되었을 때는
                // loader가 그 timestamp에 해당하는 uri를 갖도록 해 줘야 한다
                // 그니까 timeStampChanged에서 restartLoader를 해서 onCreateLoader가 재실행되도록 해야 하는거야...
                // 이걸 무슨 Activity나 Fragment의 Lifecycle 관련지어서 졸라 해맸어ㅠㅠ
//            Calendar calendar = Calendar.getInstance();
//            String timeStamp = new SimpleDateFormat("yyyyMMddHHmm").format(calendar.getTime());
//
//            Uri roadWithDateUri = HallasanContract.RoadEntry.buildRoadUriWithDate(timeStamp);
//
//            return new CursorLoader(getContext(),
//                    roadWithDateUri,
//                    RoadFragment.ROAD_COLUMNS,
//                    null,
//                    null,
//                    sortOrder
//            );

                return new CursorLoader(getContext(),
                        HallasanContract.RoadEntry.CONTENT_URI,
                        RoadFragment.ROAD_COLUMNS,
                        null,
                        null,
                        sortOrder
                );
            }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (isDebugging) {
           drawAnimation(cursor);
        } else {
            if (month < 04 || month > 10) {
                drawAnimation(cursor);
            } else {
                clearAnimation();
                unavailableTV.setVisibility(View.VISIBLE);

            }
        }

    }

    private void drawAnimation(Cursor cursor) {
        clearAnimation();

        roadImgs = new ArrayList<>();
        Log.v(LOG_TAG, "onLoadFinished에서 Road Cursor 크기 " + cursor.getCount());

//        String mainTimeStamp = new SimpleDateFormat("yyyyMMddHHmm").format(MainActivity.mCalendar.getTime());
//        Log.v(LOG_TAG, "메인 타임스탬프 : " + mainTimeStamp);

        while (cursor.moveToNext()) {
            long timeStamp = cursor.getLong(RoadFragment.COL_ROAD_TIMESTAMP);
//            Log.v(LOG_TAG, "커서 타임스탬프 : " + timeStamp);

            long roadId = cursor.getLong(RoadFragment.COL_ROAD_ID);
            String roadName = cursor.getString(RoadFragment.COL_ROAD_NAME);

            int restrict = cursor.getInt(RoadFragment.COL_ROAD_RESTRICTION);

            Log.v(LOG_TAG, "ID : " + roadId + " 장소 : " + roadName + " 타임스탬프 : " + timeStamp + " 제한여부 : " + restrict);

            if (restrict == HallasanContract.RoadEntry.RESTRICTION_ENABLED) {
                switch (roadName) {
                    case "1100도로":
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

        //처음 Fragment를 만들었을 때는 받아온 게 없으니 roadImgs는 0이다. 그니까 normalTV가 번쩍이기 시작한다.
        //근데 Sync를 해서 roadImgs가 생기면 roadImgs는 0이 아니게 된다. 그럼 roadImgs도 번쩍인다.
        //그니까 normalTV와 roadImgs가 모두 번쩍이는 결과가 생긴다.
        //이걸 해결해야 해... 여기 사용된 MainActivity.sync를 쓰는 건 효과가 없는 것 같다.

//        if (roadImgs.size() == 0 && MainActivity.sync != 0) {
        if (cursor.getCount() != 0) {
            if (roadImgs.size() == 0) {
                normalTV.setVisibility(View.VISIBLE);
                startBlink(normalTV);
            } else {
                for (ImageView i : roadImgs) {
                    i.setVisibility(View.VISIBLE);
                    startBlink(i);
                }
            }
        }
    }

    private void clearAnimation() {
        // 이걸 안 해 놓으면 계속 그려버린다.
        normalTV.setVisibility(View.INVISIBLE);
        normalTV.clearAnimation();

        road_1100Iv.setVisibility(View.INVISIBLE);
        road_1100Iv.clearAnimation();

        road_516Iv.setVisibility(View.INVISIBLE);
        road_516Iv.clearAnimation();

        road_pyeonghwaIv.setVisibility(View.INVISIBLE);
        road_pyeonghwaIv.clearAnimation();

        road_beonyeongIv.setVisibility(View.INVISIBLE);
        road_beonyeongIv.clearAnimation();

        road_hanchangIv.setVisibility(View.INVISIBLE);
        road_hanchangIv.clearAnimation();

        road_namjoIv.setVisibility(View.INVISIBLE);
        road_namjoIv.clearAnimation();

        road_bijaIv.setVisibility(View.INVISIBLE);
        road_bijaIv.clearAnimation();

        road_seoseongIv.setVisibility(View.INVISIBLE);
        road_seoseongIv.clearAnimation();

        road_sallok1Iv.setVisibility(View.INVISIBLE);
        road_sallok1Iv.clearAnimation();

        road_sallok2Iv.setVisibility(View.INVISIBLE);
        road_sallok2Iv.clearAnimation();

        road_myeongnimIv.setVisibility(View.INVISIBLE);
        road_myeongnimIv.clearAnimation();

        road_cheomdanIv.setVisibility(View.INVISIBLE);
        road_cheomdanIv.clearAnimation();

        road_aejoIv.setVisibility(View.INVISIBLE);
        road_aejoIv.clearAnimation();

        road_iljuIv.setVisibility(View.INVISIBLE);
        road_iljuIv.clearAnimation();

        unavailableTV.setVisibility(View.GONE);

    }

    private void startBlink(View i) {
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
