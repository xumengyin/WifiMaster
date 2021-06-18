package com.jerry.baselib.base;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jerry.baselib.R;


/**
 * @author guqian
 */
public class UINavigationView extends RelativeLayout {

    private ImageView mImgNavigationBack;

    private TextView mTvNavigationTitle;

    private ImageView mImgNavigationRight;
    private ImageView mImgNavigationSearch;
    private TextView mTvNavigationRight;

    private Context mContext;

    private int NavigationLeftDrawableId = 0;

    public UINavigationView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public UINavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public UINavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.ui_top_bar, this, true);
        mImgNavigationBack = (ImageView) findViewById(R.id.img_top_bar_back);
        mTvNavigationTitle = (TextView) findViewById(R.id.tv_top_bar_title);
        mImgNavigationRight = (ImageView) findViewById(R.id.img_app_right);
        mTvNavigationRight = (TextView) findViewById(R.id.tv_app_right);
        mImgNavigationSearch = (ImageView) findViewById(R.id.img_app_search);
        setNavigationLeftDefault();
    }

    public TextView getNavigationTitle() {
        return mTvNavigationTitle;
    }

    public ImageView getNavigationBack() {
        return mImgNavigationBack;
    }

    public ImageView getNavigationImageRight() {
        return mImgNavigationRight;
    }

    public ImageView getNavigationImageSearch() {
        return mImgNavigationSearch;
    }

    public TextView getNavigationTvRight() {
        return mTvNavigationRight;
    }

    public void setNavigationLeftDefault() {
        if (NavigationLeftDrawableId == 0) {
            NavigationLeftDrawableId = R.drawable.ui_ic_back;
        }
        mImgNavigationBack.setImageResource(NavigationLeftDrawableId);
    }

    /**
     * 设置返回图标和事件
     *
     * @param leftDrawableId
     */
    public void setNavigationBack(int leftDrawableId, OnClickListener listener) {
        this.NavigationLeftDrawableId = leftDrawableId;
        setNavigationLeftDefault();
        mImgNavigationBack.setOnClickListener(listener);
    }

    public void setNavigationBack(OnClickListener listener) {
        setNavigationLeftDefault();
        mImgNavigationBack.setOnClickListener(listener);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setNavigationTitle(String title) {
        mTvNavigationTitle.setText(title);
    }

    public void setNavigationTitle(int titleId) {
        mTvNavigationTitle.setText(getResources().getString(titleId));
    }

    /**
     * 设置右边的文字
     *
     * @param text
     * @param listener
     */

    public void setNavigationRightText(String text, OnClickListener listener) {
        mTvNavigationRight.setVisibility(VISIBLE);
        mTvNavigationRight.setText(text);
        mTvNavigationRight.setOnClickListener(listener);
    }

    public void setNavigationRightText(String text) {
        mTvNavigationRight.setVisibility(VISIBLE);
        mTvNavigationRight.setText(text);
    }

    public void setNavigationRightText(OnClickListener listener) {
        mTvNavigationRight.setVisibility(VISIBLE);
        mTvNavigationRight.setOnClickListener(listener);
    }


    public void setNavigationRightText(int text, OnClickListener listener) {
        mTvNavigationRight.setVisibility(VISIBLE);
        mTvNavigationRight.setText(getResources().getString(text));
        mTvNavigationRight.setOnClickListener(listener);
    }


    /**
     * 设置右边的图标和事件
     *
     * @param rightDrawableId
     * @param listener
     */

    public void setNavigationRight(int rightDrawableId, OnClickListener listener) {
        mImgNavigationRight.setVisibility(VISIBLE);
        mImgNavigationRight.setImageResource(rightDrawableId);
        mImgNavigationRight.setOnClickListener(listener);
    }

    public void setNavigationSearch(int rightDrawableId, OnClickListener listener) {
        mImgNavigationSearch.setVisibility(VISIBLE);
        mImgNavigationSearch.setImageResource(rightDrawableId);
        mImgNavigationSearch.setOnClickListener(listener);
    }


//    public void setMessageCount(int count) {
//        TextView tvMessageCount = findViewById(R.id.tv_message_count);
//        if (count == 0) {
//            tvMessageCount.setVisibility(GONE);
//        } else {
//            tvMessageCount.setVisibility(View.VISIBLE);
//            if (count >= 10) {
//                if (count > 99) {
//                    tvMessageCount.setText("99+");
//                } else {
//                    tvMessageCount.setText(count + "");
//                }
//                tvMessageCount.setBackgroundResource(R.drawable.bg_message_retangle_type);
//            } else {
//                tvMessageCount.setBackgroundResource(R.drawable.bg_message_circle_type);
//                tvMessageCount.setText(count + "");
//            }
//        }
//    }
}

