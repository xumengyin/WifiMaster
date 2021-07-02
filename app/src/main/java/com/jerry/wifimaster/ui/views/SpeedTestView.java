package com.jerry.wifimaster.ui.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 */

public class SpeedTestView extends View {

    private int mRadius; // 画布边缘半径（去除padding后的半径）
    private int mStartAngle = 135; // 起始角度
    private float mSweepAngle = 270; // 绘制角度
    private int mStartAngle2 = 157; // 起始角度
    private int mSweepAngle2 = 315; // 绘制角度
    private int mMin = 0; // 最小值
    private int mMax = 600; // 最大值
    private String mHeaderText = "BETA"; // 表头
    private long mCreditValue = 0; //
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


    public SpeedTestView(Context context) {
        this(context, null);
    }

    public SpeedTestView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpeedTestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        mSparkleWidth = dp2px(10);
        mProgressWidth = dp2px(3);
        mAniPaint = new Paint();
        mCirclePaint = new Paint();
        mCirclePaint.setColor(Color.parseColor("#8095B3F9"));
        mAniPaint.setColor(Color.WHITE);
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

        int width = resolveSize(dp2px(220), widthMeasureSpec);
        mRadius = (width - mPadding * 2) / 2;

        setMeasuredDimension(width, width);

        mCenterX = mCenterY = getMeasuredWidth() / 2f;
        mRectFProgressArc.set(
                mPadding + (outGapLength + mProgressWidth * 1.5f + innerGapLength * 2 + lengthMarkHeight),
                mPadding + (outGapLength + mProgressWidth * 1.5f + innerGapLength * 2 + lengthMarkHeight),
                getMeasuredWidth() - mPadding - (outGapLength + mProgressWidth * 1.5f + innerGapLength * 2 + lengthMarkHeight),
                getMeasuredWidth() - mPadding - (outGapLength + mProgressWidth * 1.5f + innerGapLength * 2 + lengthMarkHeight)
        );
        mRectOutArc.set(
                mPadding + outGapLength + mProgressWidth / 2f,
                mPadding + outGapLength + mProgressWidth / 2f,
                getMeasuredWidth() - mPadding - (outGapLength + mProgressWidth / 2f),
                getMeasuredWidth() - mPadding - (outGapLength + mProgressWidth / 2f)
        );
        innderCircleRadius = mCenterX - mPadding - (outGapLength + mProgressWidth * 1.5f + innerGapLength * 2 + lengthMarkHeight + dp2px(10));
        mPaint.setTextSize(sp2px(10));
        mPaint.getTextBounds("0", 0, "0".length(), mRectText);
    }

    int cnt = 60; //分成刻度 60个
    float cntDegree = mSweepAngle / cnt;
    float curValueDegree = 0;
    int curDegreeCntNum = 0;
    float innderCircleRadius = 0;

    /**
     * 刻度
     *
     * @param canvas
     * @param isAllCnt
     */
    private void drawCnt(Canvas canvas, boolean isAllCnt) {
        mPaint.setShader(null);
        if (isAllCnt) {
            mPaint.setAlpha(100);
            curDegreeCntNum = cnt;
        } else {
            mPaint.setAlpha(255);
            curValueDegree = calculateRelativeAngleWithValue(mCreditValue);
            curDegreeCntNum = (int) (curValueDegree / cntDegree) + 1;
            if (curDegreeCntNum > cnt)
                curDegreeCntNum = cnt;
        }
        canvas.save();
        canvas.translate(mCenterX, mCenterY);
        canvas.rotate(-270 + mStartAngle);
        //   canvas.drawLine(0, -mCenterX+(mPadding + outGapLength + mProgressWidth + innerGapLength + lengthMarkHeight), 0, -mCenterX+(mPadding + outGapLength + mProgressWidth + innerGapLength), mPaint);
        for (int i = 0; i < curDegreeCntNum; i++) {

            if (i % 10 == 0) {
                //画长的  (outGapLength+mProgressWidth+innerGapLength+lengthMarkHeight/2)
                canvas.drawLine(0, -mCenterX + (mPadding + outGapLength + mProgressWidth + innerGapLength + lengthMarkHeight), 0, -mCenterX + (mPadding + outGapLength + mProgressWidth + innerGapLength), mPaint);

            } else {
                //短的
                canvas.drawLine(0, -mCenterX + (mPadding + (outGapLength + mProgressWidth + innerGapLength + lengthMarkHeight - (lengthMarkHeight - shortMarkHeight) / 2f)), 0, -mCenterX + (mPadding + (outGapLength + mProgressWidth + innerGapLength + (lengthMarkHeight - shortMarkHeight) / 2f)), mPaint);
            }
            canvas.rotate(cntDegree);
        }
        canvas.restore();
    }

    private void drawInnerCircle(Canvas canvas) {
//        mPaint.setAlpha(128);
//        mPaint.setColor(Color.parseColor("#95B3F9"));

        canvas.save();
        canvas.translate(mCenterX, mCenterY);

        canvas.drawCircle(0, 0, innderCircleRadius, mCirclePaint);
        canvas.restore();
    }

    DecimalFormat df = new java.text.DecimalFormat("#.##");
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

       // canvas.drawColor(mBgColors[4]);

        /**
         * 画进度圆弧背景
         */
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mProgressWidth);
        mPaint.setAlpha(80);
        canvas.drawArc(mRectFProgressArc, mStartAngle, mSweepAngle, false, mPaint);
        canvas.drawArc(mRectOutArc, mStartAngle, mSweepAngle, false, mPaint);

        mPaint.setAlpha(255);
        /**
         * 画进度圆弧(起始到信用值)
         */
        mPaint.setShader(generateSweepGradient());
        canvas.drawArc(mRectFProgressArc, mStartAngle,
                calculateRelativeAngleWithValue(mCreditValue), false, mPaint);
        /**
         * 画信用值指示亮点
         */
        float[] point = getCoordinatePoint(
                mCenterX - mRectFProgressArc.left,
                mStartAngle + calculateRelativeAngleWithValue(mCreditValue)
        );
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setShader(generateRadialGradient(point[0], point[1]));
        canvas.drawCircle(point[0], point[1], mSparkleWidth / 2f, mPaint);


        drawCnt(canvas, true);
        drawCnt(canvas, false);
        drawInnerCircle(canvas);
        /**
         * 画实时度数值
         */
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(sp2px(35));
        mPaint.setTextAlign(Paint.Align.CENTER);
        //KB
        if(mCreditValue>1024)
        {
            mHeaderText="MB/s";
            canvas.drawText(df.format(mCreditValue/1024f), mCenterX, mCenterY, mPaint);
        }else
        {
            mHeaderText="KB/s";
            canvas.drawText(String.valueOf(mCreditValue), mCenterX, mCenterY, mPaint);
        }
        mPaint.setTextSize(sp2px(12));
        canvas.drawText(mHeaderText, mCenterX, mCenterY + dp2px(20), mPaint);
        /**
         * 画信用描述
         */
//        mPaint.setTextSize(sp2px(14));
//        canvas.drawText(calculateCreditDescription(), mCenterX, mCenterY - dp2px(40), mPaint);

        /**
         * 画表头
         */
//        mPaint.setAlpha(160);
//        mPaint.setTextSize(sp2px(10));
//        canvas.drawText(mHeaderText, mCenterX, mCenterY - dp2px(65), mPaint);

        /**
         * 画评估时间
         */
//        mPaint.setAlpha(160);
//        mPaint.setTextSize(sp2px(9));
//        canvas.drawText(getFormatTimeStr(), mCenterX, mCenterY + dp2px(25), mPaint);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }

    private int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                Resources.getSystem().getDisplayMetrics());
    }

    private SweepGradient generateSweepGradient() {
        SweepGradient sweepGradient = new SweepGradient(mCenterX, mCenterY,
                new int[]{Color.argb(0, 255, 255, 255), Color.argb(200, 255, 255, 255)},
                new float[]{0, calculateRelativeAngleWithValue(mCreditValue) / 360}
        );
        Matrix matrix = new Matrix();
        matrix.setRotate(mStartAngle - 1, mCenterX, mCenterY);
        sweepGradient.setLocalMatrix(matrix);

        return sweepGradient;
    }

    private RadialGradient generateRadialGradient(float x, float y) {
        return new RadialGradient(x, y, mSparkleWidth / 2f,
                new int[]{Color.argb(255, 255, 255, 255), Color.argb(80, 255, 255, 255)},
                new float[]{0.4f, 1},
                Shader.TileMode.CLAMP
        );
    }

    private float[] getCoordinatePoint(float radius, float angle) {
        float[] point = new float[2];

        double arcAngle = Math.toRadians(angle); //将角度转换为弧度
        if (angle < 90) {
            point[0] = (float) (mCenterX + Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY + Math.sin(arcAngle) * radius);
        } else if (angle == 90) {
            point[0] = mCenterX;
            point[1] = mCenterY + radius;
        } else if (angle > 90 && angle < 180) {
            arcAngle = Math.PI * (180 - angle) / 180.0;
            point[0] = (float) (mCenterX - Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY + Math.sin(arcAngle) * radius);
        } else if (angle == 180) {
            point[0] = mCenterX - radius;
            point[1] = mCenterY;
        } else if (angle > 180 && angle < 270) {
            arcAngle = Math.PI * (angle - 180) / 180.0;
            point[0] = (float) (mCenterX - Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY - Math.sin(arcAngle) * radius);
        } else if (angle == 270) {
            point[0] = mCenterX;
            point[1] = mCenterY - radius;
        } else {
            arcAngle = Math.PI * (360 - angle) / 180.0;
            point[0] = (float) (mCenterX + Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY - Math.sin(arcAngle) * radius);
        }

        return point;
    }

    /**
     * 相对起始角度计算信用分所对应的角度大小
     */
    private float calculateRelativeAngleWithValue(float value) {
        if (value > mMax) {
            value = mMax;
        } else if (value < mMin) {
            value = mMin;
        }

        return mSweepAngle * value * 1f / mMax;
    }

    /**
     * 信用分对应信用描述
     */
    private String calculateCreditDescription() {
        if (mCreditValue > 700) {
            return "信用极好";
        } else if (mCreditValue > 650) {
            return "信用优秀";
        } else if (mCreditValue > 600) {
            return "信用良好";
        } else if (mCreditValue > 550) {
            return "信用中等";
        }
        return "信用较差";
    }

    /**
     * 信用分对应信用描述
     */
    private int calculateBGColorWithValue(int value) {
        if (value > 700) {
            return mBgColors[4];
        } else if (value > 650) {
            return mBgColors[3];
        } else if (value > 600) {
            return mBgColors[2];
        } else if (value > 550) {
            return mBgColors[1];
        }
        return mBgColors[0];
    }

    private SimpleDateFormat mDateFormat;

    private String getFormatTimeStr() {
        if (mDateFormat == null) {
            mDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        }
        return String.format("评估时间:%s", mDateFormat.format(new Date()));
    }

    public float getCreditValue() {
        return mCreditValue;
    }

    /**
     * 设置信用值
     *
     * @param creditValue 信用值
     */
    public void setCreditValue(int creditValue) {
        if (mCreditValue == creditValue) {
            return;
        }
       // realCreValue=creditValue;
//        if (creditValue > mMax)
//            creditValue = mMax;
        if (creditValue <= mMin)
            creditValue = mMin;

        // postInvalidate();
        animateValue(creditValue);
    }

    ValueAnimator animator;
    long andmateDuration = 400;

    private void animateValue(final int creditValue) {
        if (animator != null) {
            animator.cancel();
            animator = null;
        }
        animator = ValueAnimator.ofFloat(mCreditValue, creditValue);


        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value=(float) animation.getAnimatedValue();
                mCreditValue = (long) value;

//                float radius= (float) animation.getAnimatedValue();
//                float precent = (radius-minRadius)/(maxRadius-minRadius);
//                Log.d("xuxu","---anni:"+animation.getAnimatedFraction());
//                curRadius=radius;
//                curAlpha= (int) (maxAplha-(maxAplha-minAlpha)*precent);
                postInvalidate();

            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCreditValue = creditValue;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCreditValue = creditValue;
            }
        });
        animator.setRepeatCount(0);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(andmateDuration);
        animator.start();
    }

}
