package com.okar.icz.common;

import android.app.DialogFragment;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.okar.icz.android.R;

/**
 * Created by wangfengchen on 15/11/30.
 */
public class ProgressView {

    ImageView progressIV;
    TextView progressTV;

    PopupWindow popupWindow;
    ViewGroup parent;

    public boolean isShow() {
        return show;
    }

    private boolean show;

    public ProgressView(Context context, ViewGroup parent) {
        init(context, parent);
    }

    private void init(Context context, ViewGroup parent) {
        this.parent = parent;
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(context).inflate(
                R.layout.layout_progress, null);
        progressIV = (ImageView) contentView.findViewById(R.id.progress_image);
        progressTV = (TextView) contentView.findViewById(R.id.progress_text);
        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(
                R.drawable.add_photo_bg));
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("onTouch","Touch");
                return true;
            }
        });
    }

    public void show() {
        show = true;
        // 设置好参数之后再show
        progressTV.setText("");
        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }

    public void dismiss() {
        show = false;
        popupWindow.dismiss();
    }

    public void progress(int p, int t) {
        if (show) {
            progressTV.setText(String.format("(%d,%d)", p, t));
        }
    }

}
