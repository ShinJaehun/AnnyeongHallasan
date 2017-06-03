package com.shinjaehun.annyeonghallasan;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shinjaehun.annyeonghallasan.data.HallasanContract;
import com.shinjaehun.annyeonghallasan.sync.HallasanSyncAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
    private boolean isDebugging = false;

    private Animation animation;

    public RoadFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        setHasOptionsMenu(true);

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
        normalTV = (TextView) v.findViewById(R.id.normal);

        return v;
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_main, menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        Calendar calendar = Calendar.getInstance();
//        String rightNow = new SimpleDateFormat("yyyyMMddHHmm").format(calendar.getTime());
//        Log.v(LOG_TAG, "지금 시간은 : " + rightNow);
//
//
//        SharedPreferences timePrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
//        String oldTimeStamp = timePrefs.getString(MainActivity.TIME_STAMP, null);
//
//        switch (item.getItemId()) {
//            case R.id.action_debug:
//
//                if (!oldTimeStamp.equals(rightNow) || oldTimeStamp == null) {
//                    Log.v(LOG_TAG, "Menu에서 Sync 합니다!!!!!! : 현재 타임스탬프는 " + rightNow + " 예전 타임스탬프는 " + oldTimeStamp);
//
//                    Boolean isDebugging = true;
//                    HallasanSyncAdapter.syncImmediately(getContext(), Calendar.getInstance(), isDebugging);
//                    SharedPreferences.Editor editor = timePrefs.edit();
//                    editor.putString(MainActivity.TIME_STAMP, rightNow);
//                    editor.commit();
//                } else {
//                    Log.v(LOG_TAG, "Menu에서 Sync는 이루어지지 않았습니다 : 현재 타임스탬프는 " + rightNow + " 예전 타임스탬프는 " + oldTimeStamp);
//
//                }
//        }
//        return super.onOptionsItemSelected(item);
//
//    }
// 이렇게 메뉴를 처리하면 디버깅에서 쓸 DB가 갱신되긴 하는데 바로 RoadFragment에 도로가 변경되지는 않는다.
// 물론 앱을 종료했다가 다시 시작하면 내용이 반영된다.
// 아마 Adapter를 사용하지 않기 때문에 바로 반영되지는 않는 것으로 보인다.
// 그런 이유로 이 부분은 사용하지 않기로 했다.

    private void updateRoad() {
        FetchRoadTask roadTask = new FetchRoadTask(getContext(), MainActivity.mTimeStamp, isDebugging);
        roadTask.execute();

//        RoadSyncAdapter.syncImmediately(getContext(), MainActivity.mTimeStamp, isDebugging);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateRoad();
    }


    @Override
    public void onResume() {
        super.onResume();
        animation = new AlphaAnimation((float) 0.5, 0);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        if(getLoaderManager().getLoader(ROAD_LOADER) == null) {
//            getLoaderManager().initLoader(ROAD_LOADER, null, this);
//        } else {
//            getLoaderManager().restartLoader(ROAD_LOADER, null, this);
//        }

        getLoaderManager().initLoader(ROAD_LOADER, null, this);

        super.onActivityCreated(savedInstanceState);
    }

    //    public class RoadAlarmReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            FetchRoadTask roadTask = new FetchRoadTask(getContext(), MainActivity.mCalendar, MainActivity.isDebugging);
//            roadTask.execute();
//        }
//    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if (id == ROAD_LOADER) {

            String sortOrder = HallasanContract.RoadEntry.COLUMN_TIMESTAMP + " DESC";

//            String timeStamp = new SimpleDateFormat("yyyyMMddHHmm").format(MainActivity.mCalendar.getTime());

//            timePrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
//            mTimeStamp = timePrefs.getString(MainActivity.TIME_STAMP, null);
            Uri roadWithDateUri = HallasanContract.RoadEntry.buildRoadUriWithDate(MainActivity.mTimeStamp);

            return new CursorLoader(getContext(),
                    roadWithDateUri,
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
        roadImgs = new ArrayList<>();

        Log.v(LOG_TAG, "onLoadFinished에서 Road Cursor 크기 " + cursor.getCount());

//        String mainTimeStamp = new SimpleDateFormat("yyyyMMddHHmm").format(MainActivity.mCalendar.getTime());
//        Log.v(LOG_TAG, "메인 타임스탬프 : " + mainTimeStamp);

        while (cursor.moveToNext()) {

            String timeStamp = cursor.getString(RoadFragment.COL_ROAD_TIMESTAMP);
//            Log.v(LOG_TAG, "커서 타임스탬프 : " + timeStamp);

//            if (timeStamp.equals(mTimeStamp)) {
            long roadId = cursor.getLong(RoadFragment.COL_ROAD_ID);
            String location = cursor.getString(RoadFragment.COL_ROAD_NAME);

            int restrict = cursor.getInt(RoadFragment.COL_ROAD_RESTRICTION);

            Log.v(LOG_TAG, "ID : " + roadId + " 장소 : " + location + " 타임스탬프 : " + timeStamp + " 제한여부 : " + restrict);

            if (restrict == HallasanContract.RoadEntry.RESTRICTION_ENABLED) {
                switch (location) {
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
            if (roadImgs.size() == 0){
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

    private void startBlink(View i) {
        animation.setDuration(500);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        i.startAnimation(animation);
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        normalTV.setVisibility(View.INVISIBLE);
//        normalTV.clearAnimation();
//        for (ImageView i : roadImgs) {
//            i.setVisibility(View.INVISIBLE);
//            i.clearAnimation();
//        }
//    }

    //    private static Runnable r = new Runnable() {
//
//        @Override
//        public void run() {
//            if (img_blink) {
//                if (roadImgs.size() == 0) {
//                    normalTV.setAlpha(0);
//                } else {
//                    for (ImageView i : roadImgs) {
//                        i.setAlpha(0);
//                    }
//                    //원래는 "img.setAlpha(0)" 이런 식으로 되어 있었는데 imgs의 모든 ImageView에 값을 할당하도록 변경함
//                }
//                img_blink = false;
//            } else {
//                if (roadImgs.size() == 0) {
//                    normalTV.setAlpha(1.0f);
//                } else {
//                    for (ImageView i : roadImgs) {
//                        i.setAlpha(1000);
//                    }
//                }
//                img_blink = true;
//            }
//            handler.postDelayed(r, 700);
//        }
//    };

//    static public void cleanMap() {
//        //MainActivity에서 호출 가능하도록 static으로
//        if (handler != null) {
//            handler.removeCallbacks(r);
//        }
//
//        if (roadImgs != null) {
//            if (roadImgs.size() == 0) {
//                normalTV.setVisibility(View.GONE);
//            } else {
//                for (ImageView i : roadImgs) {
//                    i.setVisibility(View.GONE);
//                }
//            }
//        }
//        //임시로 이렇게 해 두자
//    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

}
