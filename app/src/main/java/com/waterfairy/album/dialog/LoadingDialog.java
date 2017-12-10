package com.waterfairy.album.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.waterfairy.album.R;

/**
 * user : water_fairy
 * email:995637517@qq.com
 * date :2017/12/9
 * des  :
 */

public class LoadingDialog extends Dialog {
    private TextView textView;

    public LoadingDialog(@NonNull Context context, View.OnClickListener onClickListener) {
        super(context);
        View inflate = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null, false);
        setContentView(inflate);
        textView = inflate.findViewById(R.id.content);
        inflate.findViewById(R.id.cancel).setOnClickListener(onClickListener);
    }

    public void setText(String text) {
        textView.setText(text);
    }
}
