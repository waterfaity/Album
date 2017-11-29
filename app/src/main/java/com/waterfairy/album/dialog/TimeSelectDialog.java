package com.waterfairy.album.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import com.waterfairy.album.R;

import java.util.Calendar;

/**
 * user : water_fairy
 * email:995637517@qq.com
 * date :2017/11/25
 * des  :选择时间
 */

public class TimeSelectDialog extends Dialog {
    private int hour, minute;

    public TimeSelectDialog(Context context, final TimePicker.OnTimeChangedListener onTimeChangedListener) {
        super(context);
        setContentView(LayoutInflater.from(context).inflate(R.layout.dialog_time_select, null));
        final TimePicker timePicker = findViewById(R.id.timePicker);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                TimeSelectDialog.this.hour = hourOfDay;
                TimeSelectDialog.this.minute = minute;
            }
        });
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.ensure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTimeChangedListener.onTimeChanged(timePicker, hour, minute);
            }
        });
        Calendar instance = Calendar.getInstance();
        hour = instance.get(Calendar.HOUR_OF_DAY);
        minute = instance.get(Calendar.MINUTE);

    }


}
