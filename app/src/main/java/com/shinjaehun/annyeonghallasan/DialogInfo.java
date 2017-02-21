package com.shinjaehun.annyeonghallasan;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.shinjaehun.annyeonghallasan.data.RoadReport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by shinjaehun on 2017-02-02.
 */

public class DialogInfo extends Dialog {
    private static final String TAG = DialogInfo.class.getSimpleName();

    private TextView updateDateTV;

    private TextView normalTrafficTV;
    private Button confirmBTN;

    private View.OnClickListener listener;
    private RoadReport roadReport;

    public DialogInfo(Context context, View.OnClickListener listener, RoadReport roadReport) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.listener = listener;
        this.roadReport = roadReport;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // dialog 외부 흐림 효과
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dialog_info);

        updateDateTV = (TextView)findViewById(R.id.text_update_date);
        normalTrafficTV = (TextView)findViewById(R.id.text_normal_traffic);

        String rawDate = roadReport.getUpdateDate();

        try {
            Date date = new SimpleDateFormat("yyyyMMddHHmm").parse(rawDate);
            updateDateTV.setText(new SimpleDateFormat("yyyy년 MM월 dd일 aa hh시 mm분").format(date));

        } catch (ParseException e) {
            e.printStackTrace();
        }

//        StringBuffer sb = new StringBuffer();
//        for (RoadCondition rc : roadReport.getRoadConditions()) {
//            sb.append(rc.getName() + " " + rc.getDate() + " " + rc.isRestriction() + " " + rc.getSection() + " " + rc.getSnowfall() + " " + rc.getFreezing() + " " + rc.isSnowChainBig() + " " + rc.isSnowChainSmall() + "\n");
//        }




        //해당 유형별 해결한 문제 수 표시
//        int currentOperationNumber = 0;
//        for (Record r : records) {
//            if (r.getOperation().equals(currentRecord.getOperation())) {
//                currentOperationNumber++;
//            }
//        }

//        operationNumberTV.setText(String.valueOf(recordMapOfToday.getRecordsMap().get(currentRecord.getOperation())));

        //오늘 해결한 문제 수 표시
//        totalNumberTV.setText(String.valueOf(recordMapOfToday.getTotal()));

//        //실수가 없었다면! '한번도 실수하지 않았습니다' 레이아웃 표시
//        if (currentRecord.hasMistake() == 0) {
//            hasMistakeL.setVisibility(View.VISIBLE);
//        }

//        titleTV = (TextView)findViewById(R.id.text_title);
//        contentTV = (TextView)findViewById(R.id.text_content);
//        ListView achievementLV = (ListView)findViewById(R.id.list_achievement);


//        if (resultMessages != null) {
//            ListResultMessagesAdapter adapter = new ListResultMessagesAdapter(getContext(), resultMessages);
//            achievementLV.setAdapter(adapter);
//        }


//        resultLV.setOnItemClickListener(this);

        confirmBTN = (Button)findViewById(R.id.button_confirm);

//        contentTV.setText(content);

//        confirmBTN.setOnClickListener(new Button.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), ProblemActivity.class);
//                getContext().startActivity(intent);
//            }
//        });

//        for (Achievement a : userAchievements) {
//            Log.v(LOG_TAG, "Today Achievement : " + a.getLocation() + " " + a.getType() + " " + a.getAka() + " " + a.getNumber() + " " + a.getDay());
//        }

        confirmBTN.setOnClickListener(listener);
    }


}
