package com.jerry.wifimaster.ui.views;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.jerry.wifimaster.R;

/**
 *
 */

public class DetectiveAniView extends View {

    private float mRadius; // 内圆半径
    private float mAniRadius=0; // 内圆半径
    private float mStartAngle = 0; // 起始角度
    private float mSweepAngle = 270; // 绘制角度
    private int mStartAngle2 = 157; // 起始角度
    private int mSweepAngle2 = 315; // 绘制角度
    private int mMin = 0; // 最小值
    private int mMax = 600; // 最大值
    private String mHeaderText = "BETA"; // 表头
    private int mCreditValue = 782; // 信用分
    private int mSparkleWidth; // 亮点宽度
    private float mProgressWidth; // 进度圆弧宽度
    private float mLength1; // 刻度顶部相对边缘的长度
    private float mLength2; // 信用值指示器顶部相对边缘的长度

    private int mPadding;
    private float mCenterX, mCenterY; // 圆心坐标
    private Paint mProgressPaint, mCircleAniPaint, mCirclePaint, mImgPaint;
    private RectF mRectFProgressArc;
    private RectF mRectOutArc;
    private Rect mRectText;
    private Path mPath;
    private int[] mBgColors;


    private int outGapLength = dp2px(10);   //dp
    private int inGapLength = dp2px(4);   //dp


    public DetectiveAniView(Context context) {
        this(context, null);
    }

    public DetectiveAniView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DetectiveAniView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        init();
    }

    Bitmap bitmap;
    private void init() {

        bitmap=  BitmapFactory.decodeResource(getResources(), R.drawable.detective_dun);
        mProgressWidth = dp2px(10);
        mProgressPaint = new Paint();
        mImgPaint = new Paint();
        mImgPaint.setAntiAlias(true);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setAntiAlias(true);
        ///mProgressPaint.setColor(Color.RED);
        mProgressPaint.setShader(generateSweepGradient());
        // mProgressPaint.setShader(generateSweepGradient());

        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
        // mProgressPaint.setStrokeJoin(Paint.Join.ROUND);

        mProgressPaint.setStrokeWidth(mProgressWidth);


        mCirclePaint = new Paint();
        mCirclePaint.setColor(Color.parseColor("#80ffffff"));
        mCirclePaint.setAntiAlias(true);

        mCircleAniPaint = new Paint();
        mCircleAniPaint.setAntiAlias(true);
        mCircleAniPaint.setColor(Color.WHITE);

        mRectFProgressArc = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mPadding = Math.max(
                Math.max(getPaddingLeft(), getPaddingTop()),
                Math.max(getPaddingRight(), getPaddingBottom())
        );
        setPadding(mPadding, mPadding, mPadding, mPadding);


        int width = resolveSize(dp2px(50), widthMeasureSpec);


        setMeasuredDimension(width, width);

        mCenterX = mCenterY = getMeasuredWidth() / 2f;

        mRadius = mCenterX - mPadding - outGapLength - mProgressWidth - inGapLength;
        //maxRadius = mCenterX - mPadding - outGapLength;

        maxRadius=mRadius;
        minRadius=maxRadius/3;

        mRectFProgressArc.set(-mCenterX + outGapLength + mProgressWidth / 2, -mCenterX + outGapLength + mProgressWidth / 2
                , mCenterX - outGapLength - mProgressWidth / 2,
                mCenterX - outGapLength - mProgressWidth / 2);
        //startAnimate();
    }

    private SweepGradient generateSweepGradient() {
        SweepGradient sweepGradient = new SweepGradient(0, 0,
                new int[]{Color.argb(0, 255, 255, 255), Color.argb(255, 255, 255, 255)},
                new float[]{0f, 0.95f});
//        Matrix matrix = new Matrix();
//        matrix.setRotate(mStartAngle - 1, mCenterX, mCenterY);
//        sweepGradient.setLocalMatrix(matrix);

        return sweepGradient;
    }


    long drawGap = 35;
    long andmateDuration = 2500;
    float maxRadius, minRadius;
    float curRadius;


    private void drawProgress(Canvas canvas) {
        canvas.save();
        canvas.translate(mCenterX, mCenterY);
        canvas.rotate(mStartAngle);
        canvas.drawArc(mRectFProgressArc, 10, 340, false, mProgressPaint);

        // canvas.drawLine(0,0,100,300,mProgressPaint);
        canvas.restore();
    }

    ValueAnimator animator,animator2;
    int mAniCricleAlpha=80,mImgAlpha;
    int maxAplha = 120;
    int minAlpha = 0;
    float curAniValueF;

    AnimatorSet animatorSet;
    public void startAnimate() {
        if (animatorSet != null) {
            animatorSet.cancel();
            animatorSet = null;
        }
        animator = ValueAnimator.ofFloat(0, 360);
        animator2= ValueAnimator.ofInt(maxAplha,minAlpha,maxAplha);
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mImgAlpha = (int) animation.getAnimatedValue();

                if (animation.getAnimatedFraction()-0.5f<=0) {
                    mAniRadius=0;
                }else
                {
                    mAniCricleAlpha= (int) (maxAplha-(maxAplha-minAlpha)*((animation.getAnimatedFraction()-0.5f)*2));
                    mAniRadius=minRadius+((animation.getAnimatedFraction()-0.5f)*2)*(maxRadius-minRadius);
                }

                Log.d("xuxu", "---mImgAlpha:" + mImgAlpha);
                invalidate();
            }
        });
        animatorSet=new AnimatorSet();

//        animatorSet.addUpdateListener
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mStartAngle = (float) animation.getAnimatedValue();
                //mImgAlpha=255* animation.getAnimatedFraction();

               // mAniRadius=animation.getAnimatedFraction()

                Log.d("xuxu", "---mStartAngle:" + mStartAngle);
                invalidate();

            }
        });
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator2.setRepeatCount(ValueAnimator.INFINITE);
        animatorSet.playTogether(animator,animator2);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.setDuration(andmateDuration);
        animatorSet.start();
    }


    private void drawCircle(Canvas canvas) {
        canvas.save();
        canvas.translate(mCenterX, mCenterY);

        canvas.drawCircle(0, 0, mRadius, mCirclePaint);

        canvas.restore();
    }
    private void drawAniCircle(Canvas canvas) {
        canvas.save();
        canvas.translate(mCenterX, mCenterY);
        mCircleAniPaint.setAlpha(mAniCricleAlpha);
        canvas.drawCircle(0, 0, mAniRadius, mCircleAniPaint);

        canvas.restore();
    }

    private void drawImg(Canvas canvas) {

        canvas.save();
        canvas.translate(mCenterX, mCenterY);
        mImgPaint.setAlpha(mImgAlpha);
        canvas.drawBitmap(bitmap,-bitmap.getWidth()/2f,-bitmap.getHeight()/2f,mImgPaint);
        canvas.restore();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawProgress(canvas);
        drawCircle(canvas);
        drawImg(canvas);
        drawAniCircle(canvas);
    }

    public void stop()
    {
        if (animatorSet != null) {
            animatorSet.removeAllListeners();
            animatorSet.cancel();
            animatorSet = null;
        }
    }
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
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
