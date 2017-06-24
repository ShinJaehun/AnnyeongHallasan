package com.shinjaehun.annyeonghallasan;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by shinjaehun on 2017-06-24.
 */

public class RoadDialog extends Dialog {

    private Context mContext;
    private String mTitle;
    private String mBaseDate;
    private String mMessage;

    private TextView titleTV;
    private TextView baseDateTV;
    private TextView messageTV;
    private Button confirmBTN;

    private View.OnClickListener listener;

    public RoadDialog(Context context, View.OnClickListener clickListener, String title, String baseDate, String message) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        mContext = context;
        listener = clickListener;
        mTitle = title;
        mBaseDate = baseDate;
        mMessage = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // dialog 외부 흐림 효과
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dialog_road_info);

        titleTV = (TextView)findViewById(R.id.text_title);
        baseDateTV = (TextView)findViewById(R.id.text_base_date);
        messageTV = (TextView)findViewById(R.id.text_message);
        confirmBTN = (Button)findViewById(R.id.button_confirm);

        titleTV.setText(mTitle);
        baseDateTV.setText(mBaseDate);
        messageTV.setText(mMessage);

        confirmBTN.setOnClickListener(listener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        dismiss();
    }
}
