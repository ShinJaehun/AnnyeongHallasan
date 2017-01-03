package com.shinjaehun.annyeonghallasan;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by shinjaehun on 2016-12-30.
 */


public class FetchData extends AsyncTask<Void, Void, Void> {
    private static final String TAG = FetchData.class.getSimpleName();

    private final Context context;

    ArrayList<RoadStatus> roadStatuses;
    String url = "http://www.jjpolice.go.kr/jjpolice/police25/traffic.htm?act=rss";

    static ArrayList<ImageView> imgs;
    static TextView normalTV;

    static boolean img_blink = true;

    static Handler handler;

    public FetchData(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Document doc = null;
        //실제 URL로 테스트하기
            try {
                doc = Jsoup.connect(url).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

        //XML 파일로 테스트하기
//        InputStream inputStream = context.getResources().openRawResource(R.raw.aaa);
//        try {
//            doc = Jsoup.parse(inputStream, "UTF-8", url);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        Elements dateData = doc.select("date");
        Elements roadsData = doc.select("title");
        Elements statusData = doc.select("description");

        roadStatuses = new ArrayList<>();

        for (int i = 1; i < 14; i++) {
            //제주지방경찰청에서 제공하는 13개의 도로 DATA

            String n = roadsData.get(i).text().replace("<![CDATA[", "").replace("]]>", "").toString().trim();
            //정말 귀찮은데 도로명에 <![CDATA[~~~]> 이거 붙는 거 없애고

            String name;
            if (n.matches(".*[0-9]\\)$")) {
                //"숫자)"로 끝나는 문자열이라면
                name = n.substring(0, n.indexOf("("));
                // 도로명 "1100도로(1139)"에서 도로 번호를 생략

            } else {
                name = n;
                // 도로번호가 없는 도로명은 그대로 쓰기
            }


            String description = statusData.get(i).text().replaceAll("&nbsp;", "").replaceAll("&amp;nbsp;", "").toString().trim();
            //description에 nbsp랑 amp 붙는거 너무 짜증나!

            String date = dateData.get(0).text().toString().trim();
            String[] v = description.split(":", -6);

//                    Log.v(TAG, name + " 크기는 " + v.length);
//                    Log.v(TAG, name + "," + v[0].trim() );
//                    Log.v(TAG, name + "," + v[1].trim() );
//                    Log.v(TAG, name + "," + v[2].trim() );
//                    Log.v(TAG, name + "," + v[3].trim() );
//                    Log.v(TAG, name + "," + v[4].trim() );
//                    Log.v(TAG, name + "," + v[5].trim() );
/*
                    통제하는 경우 description 내용
                    구간 : 제주대 입구 ~ 성판악 적설 : 1 결빙 : 대형 통재상항 :    소형 통재상항 : 체인

                    통제하지 않는 경우 descrition 내용
                    구간 : 정상 적설 : 결빙 : 대형 통재상항 :    소형 통재상항 :
*/
            boolean restriction = false;
            String section = "정상";
            if (!v[1].contains("정상")) {
                //통제하는 경우
                restriction = true;
                section = v[1].substring(0, v[1].indexOf("적설")).trim();
                //통제구간 : "제주대 입구 ~ 성판악"
            }

            Integer snowfall = 0;
//                if (v[2].matches(".*[0-9]*.")) {
            if (v[2].matches(".*\\d+.*")) {
                snowfall = Integer.parseInt(v[2].substring(0, v[2].indexOf("결빙")).trim());
                //적설
            }

            Integer freezing = 0;
            if (v[3].matches(".*\\d+.*")) {
                freezing = Integer.parseInt(v[3].substring(0, v[3].indexOf("대형")).trim());
                //결빙
            }

            boolean snowChainBig = false;
            if(v[4].contains("체인")) {
                snowChainBig = true;
            }

            boolean snowChainSmall = false;
            if (v[5].contains("체인")) {
                snowChainSmall = true;
            }

            roadStatuses.add(new RoadStatus(name, description, date, restriction, section, snowfall, freezing, snowChainBig, snowChainSmall));
            //도로명, description, 발표시간을 생성자로 하여 RoadStatus 생성



        }

//                Elements roadsData = doc.select("title");
//                for (int i = 0; i < roadsData.size(); i++) {
//                roads.add(roadsData.get(i).text().toString().trim());
//                Log.v(TAG, "Road : " + i + " " + roadsData.get(i).text().toString().trim());
//                }
//
//                Elements statusData = doc.select("description");
//                for (int i = 0; i < statusData.size(); i++) {
//                status.add(statusData.get(i).text().replaceAll("&nbsp;","").toString().trim());
//                Log.v(TAG, "Status : " + i + " " + statusData.get(i).text().replaceAll("&nbsp;","").toString().trim());
//                }
//


//        for (RoadStatus rs : roadStatuses) {
//            Log.v(TAG, rs.getName() + " : " + rs.getDescription() );
//        }

        for (RoadStatus rs : roadStatuses) {
            Log.v(TAG, rs.getName() + " " + rs.getDate() + " " + rs.isRestriction() + " " + rs.getSection() + " " + rs.getSnowfall() + " " + rs.getFreezing() + " " + rs.isSnowChainBig() + " " + rs.isSnowChainSmall());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        imgs = new ArrayList<>();

        for (RoadStatus rs : roadStatuses) {
            if (rs.isRestriction()) {
                switch (rs.name) {
                    case "1100도로" :
//                        img = (ImageView)((MainActivity)context).findViewById(R.id.road_1100);
//                        img.setVisibility(View.VISIBLE);
//                        handler = new Handler();
//                        handler.post(r);
                        //여러 ImageView를 동시에 깜빡이게 하기 위해서는 같은 handler를 동시에
                        imgs.add((ImageView)((MainActivity)context).findViewById(R.id.road_1100));

                        break;
                    case "5.16도로" :
                        imgs.add((ImageView)((MainActivity)context).findViewById(R.id.road_516));
                        break;
                    case "번영로" :
                        break;
                    case "평화로" :
                        imgs.add((ImageView)((MainActivity)context).findViewById(R.id.road_pyeonghwa));
                        break;
                    case "한창로" :
                        imgs.add((ImageView)((MainActivity)context).findViewById(R.id.road_hanchang));
                        break;
                    case "남조로" :
                        imgs.add((ImageView)((MainActivity)context).findViewById(R.id.road_namjo));
                        break;
                    case "비자림로" :
                        imgs.add((ImageView)((MainActivity)context).findViewById(R.id.road_bija));
                        break;
                    case "서성로" :
                        imgs.add((ImageView)((MainActivity)context).findViewById(R.id.road_seoseong));
                        break;
                    case "제1산록도로" :
                        imgs.add((ImageView)((MainActivity)context).findViewById(R.id.road_sallok1));
                        break;
                    case "제2산록도로" :
                        imgs.add((ImageView)((MainActivity)context).findViewById(R.id.road_sallok2));
                        break;
                    case "명림로" :
                        imgs.add((ImageView)((MainActivity)context).findViewById(R.id.road_myeongnim));
                        break;
                    case "첨단로" :
                        imgs.add((ImageView)((MainActivity)context).findViewById(R.id.road_cheomdan));
                        break;
                    case "기타도로" :
                        if (rs.section.contains("애조로")) {
                            imgs.add((ImageView) ((MainActivity) context).findViewById(R.id.road_seoseong));
                        }
                        if (rs.section.contains("일주도로")) {
                            imgs.add((ImageView) ((MainActivity) context).findViewById(R.id.road_ilju));
                        }
                        break;
                    default:
                        break;
                }
            }
        }

        if (imgs.size() == 0) {
            normalTV = (TextView)((MainActivity)context).findViewById(R.id.normal);
            normalTV.setVisibility(View.VISIBLE);
//            Log.v(TAG, "imgs 비었다");
        } else {
            for (ImageView i : imgs) {
                i.setVisibility(View.VISIBLE);
            }
//            Log.v(TAG, "imgs 안 비었다");
        }

//            Animation startAnimation = AnimationUtils.loadAnimation(context, R.anim.blink_animation);
        handler = new Handler();
        handler.post(r);

//        super.onPostExecute(aVoid);
        if (roadStatuses != null) {
            StringBuilder sb = new StringBuilder();
            for (RoadStatus rs : roadStatuses) {
                sb.append(rs.getName() + " : " + rs.getDescription() + "\n");
            }

            TextView statusTV = (TextView)((MainActivity)context).findViewById(R.id.status);
            statusTV.setText(sb.toString());
        }
    }

    private static Runnable r = new Runnable() {

        @Override
        public void run() {
            if (img_blink) {
                if (imgs.size() == 0) {
                    normalTV.setAlpha(0);
                } else {
                    for (ImageView i : imgs) {
                        i.setAlpha(0);
                    }
                    //원래는 "img.setAlpha(0)" 이런 식으로 되어 있었는데 imgs의 모든 ImageView에 값을 할당하도록 변경함
                }
                img_blink = false;
            } else {
                if (imgs.size() == 0) {
                    normalTV.setAlpha(1.0f);
                } else {
                    for (ImageView i : imgs) {
                        i.setAlpha(1000);
                    }
                }
                img_blink = true;
            }
            handler.postDelayed(r, 700);
        }
    };

    static public void stopHandler() {
        //MainActivity에서 호출 가능하도록 static으로
        handler.removeCallbacks(r);
    }

}