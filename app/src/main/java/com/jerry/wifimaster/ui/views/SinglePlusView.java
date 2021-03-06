package com.jerry.wifimaster.ui.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.jerry.wifimaster.R;

public class SinglePlusView extends View {


    private Rect bitmapRect = new Rect();
    private Rect bitmapRect2 = new Rect();
    private RectF dstRect = new RectF();
    private RectF rect2dstRect = new RectF();
    private Paint imgPaint = new Paint();
    private Paint aniPaint = new Paint();
    private float mCenterX, mCenterY; // 圆心坐标

    float rotateDegree = 0;

    float imgRadius, mRadius, aniRingRadius;
    private float outGap = dp2px(50);
    private float innerGap = dp2px(20);

    public SinglePlusView(Context context) {
        super(context);
    }

    public SinglePlusView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    Bitmap bitmap,bitmapBg;

    private void init() {
        imgPaint.setAntiAlias(true);
        aniPaint.setAntiAlias(true);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.signal_circlr_bg);
        bitmapBg = BitmapFactory.decodeResource(getResources(), R.drawable.singnal_view_bg1);
        bitmapRect.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
        bitmapRect2.set(0, 0, bitmapBg.getWidth(), bitmapBg.getHeight());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = resolveSize(dp2px(220), widthMeasureSpec);

        mCenterX = mCenterY = getMeasuredWidth() / 2f;
       // setMeasuredDimension(width, width);
        imgRadius = mCenterX - outGap;
        dstRect.set(-imgRadius, -imgRadius, imgRadius, imgRadius);
        rect2dstRect.set(dstRect.left+innerGap,dstRect.top+innerGap,dstRect.right-innerGap,dstRect.bottom-innerGap);
        minAniRadius = imgRadius;
        maxAniRadius = mCenterX * 2;
        // startAnimate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAni();
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }

    ValueAnimator animator;
    long andmateDuration = 2000;
    float minAniRadius, maxAniRadius;

    public void stopAni() {
        if (animator != null) {
            animator.cancel();
            animator = null;
        }
    }

    public void startAnimate() {
        stopAni();
        animator = ValueAnimator.ofFloat(0, 360);


        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                rotateDegree = (float) animation.getAnimatedValue();

                for (int i = 0; i < ringRadius.length; i++) {
                    aniRingAlpha = (int) (maxAniAlpha - (maxAniAlpha - minAniAlpha) * animation.getAnimatedFraction());
                    ringRadius[i] = (maxAniRadius - minAniRadius) / (i + 1) * animation.getAnimatedFraction() + minAniRadius;
                }
                invalidate();

            }
        });
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(andmateDuration);
        animator.start();
    }

    private void drawProgress(Canvas canvas) {

        canvas.save();

        canvas.translate(mCenterX, mCenterY);
        canvas.rotate(rotateDegree);
//        aniPaint.setColor(Color.WHITE);
//        aniPaint.setAlpha(255);
//        aniPaint.setStyle(Paint.Style.FILL);
//        canvas.drawCircle(0, 0, mRadius, aniPaint);

        canvas.drawBitmap(bitmap, bitmapRect, dstRect, imgPaint);
        canvas.drawBitmap(bitmapBg, bitmapRect2, rect2dstRect, imgPaint);

        canvas.restore();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        drawProgress(canvas);
        drawRing(canvas);

    }

    float[] ringRadius = new float[3];
    int aniRingAlpha, maxAniAlpha = 80, minAniAlpha = 5;

    private void drawRing(Canvas canvas) {

        canvas.save();

        canvas.translate(mCenterX, mCenterY);
        aniPaint.setColor(Color.WHITE);
        aniPaint.setStyle(Paint.Style.STROKE);
        aniPaint.setStrokeWidth(dp2px(3));
        aniPaint.setAlpha(aniRingAlpha);
        for (float radius : ringRadius) {
            canvas.drawCircle(0, 0, radius, aniPaint);
        }

//        dstRect.set();
        //  canvas.drawBitmap(bitmap, bitmapRect, dstRect, imgPaint);

        canvas.restore();
    }
}
