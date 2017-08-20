package com.shinjaehun.annyeonghallasan;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.StringBuilderPrinter;
import android.view.View;

import com.shinjaehun.annyeonghallasan.data.HallasanContract;

/**
 * Created by shinjaehun on 2017-06-24.
 */

public class GetRoadFromDBTask extends AsyncTask<Void, Void, Void> {
    Context mContext;
    String title;
    String baseDate;
    long timeStamp;
    StringBuilder messageSb;
    RoadDialog roadDialog;

    public GetRoadFromDBTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        String sortOrder =  HallasanContract.RoadEntry._ID + " DESC limit 13";

        Cursor cursor = mContext.getContentResolver().query(HallasanContract.RoadEntry.CONTENT_URI,
                RoadFragment.ROAD_COLUMNS,
                null,
                null,
                sortOrder
        );

        boolean isRestricted = false;
        messageSb = new StringBuilder();

        while (cursor.moveToNext()) {
            String d = cursor.getString(RoadFragment.COL_ROAD_BASE_DATE);
            timeStamp = cursor.getLong(RoadFragment.COL_ROAD_TIMESTAMP);
            baseDate = d.substring(0, 4) + "년"
                    + d.substring(4, 6) + "월"
                    + d.substring(6, 8) + "일 "
                    + d.substring(8, 10) + "시"
                    + d.substring(10) + "분\n" + "타임스탬프" + String.valueOf(timeStamp);
//           이것도 문제를 일으킬 수 있다.
//            baseDate = d;
            if (cursor.getInt(RoadFragment.COL_ROAD_RESTRICTION) == HallasanContract.RoadEntry.RESTRICTION_ENABLED) {
                if (isRestricted == false) {
                    title = "교통통제";
                    isRestricted = true;
                }

                messageSb.append(cursor.getString(RoadFragment.COL_ROAD_NAME) + " : ");

                switch (cursor.getInt(RoadFragment.COL_ROAD_CHAIN)) {
                    case HallasanContract.RoadEntry.CHAIN_BIG:
                        messageSb.append("대형체인\n");
                        break;
                    case HallasanContract.RoadEntry.CHAIN_SMALL:
                        messageSb.append("소형체인\n");
                        break;
                    default:
                        messageSb.append("\n");
                        break;
                }
            }
        }

        if (isRestricted == false) {
            title = "정상운행";
        }

        cursor.close();
        //이건 Loader나 adapter에서 사용하는 게 아니라서 cursor를 꼭 닫아야 하는 듯 하다.
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        roadDialog = new RoadDialog(mContext, clickListener, title, baseDate, messageSb.toString());
        roadDialog.setCanceledOnTouchOutside(false);
        roadDialog.show();

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
//            Intent intent = ((Activity)mContext).getIntent();
            roadDialog.dismiss();
            roadDialog = null;
//            ((Activity)mContext).finish();
//            mContext.startActivity(intent);
        }
    };

}
