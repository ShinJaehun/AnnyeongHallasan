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
            //getColumnIndex 대신 cursor의 값을 쉽게 사용하기 위한 Projection
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
    //Projection에서 몇몇 값만 받아오기로 했다면 아래 COL도 변경되어야 함
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
            //Debug 모드에서는 그냥 정상적으로 Map에 대해 onClick()을 받음
            //Debug 모드에서는 월에 관계 없이 테스트 가능해야 하므로...
            FrameLayout roadMapL = (FrameLayout) v.findViewById(R.id.layout_road_status);
            roadMapL.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    GetRoadFromDBTask getRoadFromDBTask = new GetRoadFromDBTask(getContext());
                    getRoadFromDBTask.execute();
                    //RoadAdapter가 존재하지 않기 때문에 Road 상태를 DB에서 불러오려면 cursor를 사용하는 대신
                    //asynctask() 등으로 직접 읽어와야 한다
                }
            });
        } else {
            //11월부터 3월까지는 roadPrecess()가 동작하고 Map에서 onClick()도 받음
            if (month < 04 || month > 10) {
                FrameLayout roadMapL = (FrameLayout) v.findViewById(R.id.layout_road_status);
                roadMapL.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        GetRoadFromDBTask getRoadFromDBTask = new GetRoadFromDBTask(getContext());
                        getRoadFromDBTask.execute();
                        //RoadAdapter가 존재하지 않기 때문에 Road 상태를 DB에서 불러오려면 cursor를 사용하는 대신
                        //asynctask() 등으로 직접 읽어와야 한다
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
        //Fragment를 화면에 보여주면서 애니메이션 깜빡이게...
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(ROAD_LOADER, null, this);
        //Loader init
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//        Log.v(LOG_TAG, "RoadFragment의 onCreateLoader입니다.");
            if (id == ROAD_LOADER) {
            //DB에 입력된 순서로 뒤에서 13번째까지(모든 도로에 대한 정보) cursor로 받아옴
                String sortOrder = HallasanContract.RoadEntry._ID + " DESC limit 13";
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
            //디버깅 모드에서는 걍 애니메이션 출력함
           drawAnimation(cursor);
        } else {
            //11월부터 3월까지는 애니메이션 출력
            if (month < 04 || month > 10) {
                drawAnimation(cursor);
            } else {
                //4월부터 10월까지는 애니메이션 없애고 메시지 출력
                clearAnimation();
                unavailableTV.setVisibility(View.VISIBLE);

            }
        }

    }

    private void drawAnimation(Cursor cursor) {
        clearAnimation();
        //일단 기존 애니메이션은 지우고

        roadImgs = new ArrayList<>();
        Log.v(LOG_TAG, "onLoadFinished에서 Road Cursor 크기 " + cursor.getCount());

        while (cursor.moveToNext()) {
            //cursor로 받아온 자료를 가지고
            long timeStamp = cursor.getLong(RoadFragment.COL_ROAD_TIMESTAMP);
            long roadId = cursor.getLong(RoadFragment.COL_ROAD_ID);
            String roadName = cursor.getString(RoadFragment.COL_ROAD_NAME);
            int restrict = cursor.getInt(RoadFragment.COL_ROAD_RESTRICTION);

//            Log.v(LOG_TAG, "ID : " + roadId + " 장소 : " + roadName + " 타임스탬프 : " + timeStamp + " 제한여부 : " + restrict);

            if (restrict == HallasanContract.RoadEntry.RESTRICTION_ENABLED) {
                //제한된 도로라면 각 도로에 해당하는 이미지를 roadImgs에 저장
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
        }

        if (cursor.getCount() != 0) {
            //DB가 갱신되어 새로운 cursor 값이 발생하면
            if (roadImgs.size() == 0) {
                //제한하는 도로가 없다면 정상운행 메시지 애니메이션 처리
                normalTV.setVisibility(View.VISIBLE);
                startBlink(normalTV);
            } else {
                //제한하는 도로가 있다면
                for (ImageView i : roadImgs) {
                    //해당 도로 이미지 모두 애니메이션 처리
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
        //반짝이는 애니메이션
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
