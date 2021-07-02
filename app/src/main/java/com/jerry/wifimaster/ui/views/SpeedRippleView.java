package com.jerry.wifimaster.ui.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 *
 */

public class SpeedRippleView extends View {

    private int mRadius; // 画布边缘半径（去除padding后的半径）
    private int mStartAngle = 135; // 起始角度
    private float mSweepAngle = 270; // 绘制角度
    private int mStartAngle2 = 157; // 起始角度
    private int mSweepAngle2 = 315; // 绘制角度
    private int mMin = 0; // 最小值
    private int mMax = 600; // 最大值
    private int mCreditValue = 782; // 信用分
    private int mSparkleWidth; // 亮点宽度
    private int mProgressWidth; // 进度圆弧宽度
    private float mLength1; // 刻度顶部相对边缘的长度
    private float mLength2; // 信用值指示器顶部相对边缘的长度

    private int mPadding;
    private float mCenterX, mCenterY; // 圆心坐标
    private Paint mPaint, mAniPaint, mCirclePaint;
    private RectF mRectFProgressArc;
    private RectF mRectOutArc;
    private Rect mRectText;
    private Path mPath;
    private int[] mBgColors;


    private int lengthMarkHeight = dp2px(10);
    private int shortMarkHeight = dp2px(2);
    private int outGapLength = dp2px(25);   //dp
    private int innerGapLength = dp2px(10);   //dp


    public SpeedRippleView(Context context) {
        this(context, null);
    }

    public SpeedRippleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpeedRippleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        mSparkleWidth = dp2px(10);
        mProgressWidth = dp2px(3);
        mAniPaint = new Paint();
        mAniPaint.setStyle(Paint.Style.STROKE);
        mCirclePaint = new Paint();
        mCirclePaint.setColor(Color.parseColor("#8095B3F9"));
        mAniPaint.setColor(Color.WHITE);
        mAniPaint.setAlpha(60);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.SQUARE);
        mPaint.setColor(Color.WHITE);

        mRectFProgressArc = new RectF();
        mRectOutArc = new RectF();
        mRectText = new Rect();
        mPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mPadding = Math.max(
                Math.max(getPaddingLeft(), getPaddingTop()),
                Math.max(getPaddingRight(), getPaddingBottom())
        );
        setPadding(mPadding, mPadding, mPadding, mPadding);

        mLength1 = mPadding + mSparkleWidth / 2f + dp2px(8);
        mLength2 = mLength1 + mProgressWidth + dp2px(4);

        int width = resolveSize(dp2px(50), widthMeasureSpec);
        mRadius = (width - mPadding * 2) / 2;

        setMeasuredDimension(width, width);

        mCenterX = mCenterY = getMeasuredWidth() / 2f;


        //maxRadius = mCenterX - mPadding - outGapLength;
        maxRadius = mCenterX ;
        minRadius = mCenterX - mPadding - (outGapLength + mProgressWidth * 1.5f + innerGapLength * 2 + lengthMarkHeight + dp2px(10));
        curRadius=0;

        mRectOutArc.set(
                mPadding + outGapLength + mProgressWidth / 2f,
                mPadding + outGapLength + mProgressWidth / 2f,
                getMeasuredWidth() - mPadding - (outGapLength + mProgressWidth / 2f),
                getMeasuredWidth() - mPadding - (outGapLength + mProgressWidth / 2f)
        );
        startAnimate();
    }

    long andmateDuration = 3500;
    float maxRadius, minRadius;
    float curRadius;
    int maxAplha = 60;
    int minAlpha = 0;
    int curAlpha = minAlpha;

    private void drawProgress(Canvas canvas) {
        canvas.save();
        canvas.translate(mCenterX, mCenterY);
        mAniPaint.setAlpha(curAlpha);
        mAniPaint.setStrokeWidth(curRadius);
        //mAniPaint.setco
        mRectOutArc.set(-(minRadius+curRadius/2),-(minRadius+curRadius/2),(minRadius+curRadius/2),(minRadius+curRadius/2));
        canvas.drawArc(mRectOutArc,0,360,false,mAniPaint);

//        if (curRadius <= 0)
//            curRadius = minRadius;
//        else if (curRadius >= maxRadius)
//            curRadius = maxRadius;
//        mAniPaint.setAlpha(curAlpha);
//        canvas.drawCircle(0, 0, curRadius, mAniPaint);
        //canvas.draw
        canvas.restore();
    }
    ValueAnimator animator;
    public void startAnimate()
    {
        if(animator!=null)
        {
            animator.cancel();
            animator=null;
        }
        animator= ValueAnimator.ofFloat(0,maxRadius-minRadius);


        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float radius= (float) animation.getAnimatedValue();
             // float precent = (radius-minRadius)/(maxRadius-minRadius);

                curRadius=radius;
                curAlpha= (int) (maxAplha-(maxAplha-minAlpha)*animation.getAnimatedFraction());
                if(curAlpha<0)
                    curAlpha=0;
               // Log.d("xuxu","---curAlpha:"+curAlpha);
                invalidate();

            }
        });
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(andmateDuration);
        animator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(animator!=null)
        {
            animator.cancel();
            animator=null;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawProgress(canvas);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }

    private int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                Resources.getSystem().getDisplayMetrics());
    }

}
