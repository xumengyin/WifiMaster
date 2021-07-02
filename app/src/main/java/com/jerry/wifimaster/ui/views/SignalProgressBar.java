package com.jerry.wifimaster.ui.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

public class SignalProgressBar extends View {
    Paint mPaint =new Paint();

    int gap=dp2px(10);

    float progress=0;
    public SignalProgressBar(Context context) {
        super(context);
    }

    public SignalProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    ValueAnimator animator;
    public void startAnimate(long time)
    {
        if(animator!=null)
        {
            animator.cancel();
            animator=null;
        }
        animator= ValueAnimator.ofFloat(0,100f);


        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progress= (float) animation.getAnimatedValue();
                // float precent = (radius-minRadius)/(maxRadius-minRadius);
                invalidate();

            }
        });
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(time);
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStrokeWidth(getMeasuredHeight()/2f);
        mPaint.setColor(Color.parseColor("#33ffffff"));

        canvas.drawLine(gap,getMeasuredHeight()/2f,getMeasuredWidth()-gap,getMeasuredHeight()/2f,mPaint);

        mPaint.setColor(Color.parseColor("#ffffff"));

        canvas.drawLine(gap,getMeasuredHeight()/2f,(getMeasuredWidth()-gap*2)*(progress/100f)+gap,getMeasuredHeight()/2f,mPaint);
       // canvas.drawLine();

    }

    private void init()
    {
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
//        mPaint.setColor;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }
}
