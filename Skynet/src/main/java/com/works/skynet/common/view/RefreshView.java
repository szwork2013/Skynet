package com.works.skynet.common.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.works.skynet.activity.R;
import com.works.skynet.common.utils.DensityUtils;

/**
 * Created by wangfengchen on 14-7-23.
 */
public class RefreshView extends ViewGroup{

    private View headView;

    private Context context;

    private int screenWidth;

    private int headViewHeight;

    private int headViewPos;

    public RefreshView(Context context) {
        super(context);

    }

    public RefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int measuredHeight = measureHeight(heightMeasureSpec);

        int measuredWidth = measureWidth(widthMeasureSpec);

        setMeasuredDimension(measuredHeight, measuredWidth);

    }

    private int measureHeight(int measureSpec) {

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

// Default size if no limits are specified.

        int result = DensityUtils.getDM(context).heightPixels;
        if (specMode == MeasureSpec.AT_MOST){

// Calculate the ideal size of your
// control within this maximum size.
// If your control fills the available
// space return the outer bound.

            result = specSize;
        }
        else if (specMode == MeasureSpec.EXACTLY){

// If your control can fit within these bounds return that value.
            result = specSize;
        }

        return result;
    }

    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

// Default size if no limits are specified.
        int result = DensityUtils.getDM(context).widthPixels;
        if (specMode == MeasureSpec.AT_MOST){
// Calculate the ideal size of your control
// within this maximum size.
// If your control fills the available space
// return the outer bound.
            result = specSize;
        }

        else if (specMode == MeasureSpec.EXACTLY){
// If your control can fit within these bounds return that value.

            result = specSize;
        }

        return result;
    }

    @Override
    protected void onLayout(boolean b, int i, int i2, int i3, int i4) {
        if(b){
            for(int k = 0;k<getChildCount();k++){
                View child = getChildAt(k);
                int childWidth = child.getWidth();
                int childHeight = child.getHeight();
                child.layout(i,i2 - childHeight + headViewPos,childWidth,headViewPos);
            }
        }
    }

    private void init(){
        screenWidth = DensityUtils.getDM(context).widthPixels;
        headViewHeight = DensityUtils.dip2px(context, 60f);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        headView = layoutInflater.inflate(R.layout.head_layout,null);
        headView.setLayoutParams(new LayoutParams(screenWidth, headViewHeight));
        addView(headView);
        headView.layout(0,-headViewHeight,screenWidth,400);
        setBackgroundColor(Color.WHITE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = event.getY();
                headViewPos = (int) moveY;
                Log.d("","Y_"+headViewPos);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }
}
