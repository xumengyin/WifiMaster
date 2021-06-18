package com.jerry.wifimaster.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import com.jerry.baselib.utils.SpanUtils;
import com.jerry.wifimaster.Constants;
import com.jerry.wifimaster.R;
import com.jerry.wifimaster.ui.ServiceWebActivity;


public class CustomAgreementDialog extends Dialog {

    public TextView subTvTitle;
    public TextView mTvAgree;

    public Button buttonOK;
    public Button buttonCancel;

    private NestedScrollView mScrollContainer;

    private int mContentAreaMaxHeight = -1;

    private Context mContext;

    public CustomAgreementDialog(Context context) {
        super(context, R.style.CustomDialog);
        init(context);
    }

    public CustomAgreementDialog(Context context, int theme) {
        super(context, R.style.CustomDialog);
        init(context);
    }

    private void init(Context context) {

        mContext = context;
        setCancelable(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_custom_agreement, null);

        mTvAgree = view.findViewById(R.id.tv_agree);
        SpanUtils.with(mTvAgree,getContext()).append("查看完整版")
                .append("《用户协议》")
                .setClickSpan(ContextCompat.getColor(mContext, R.color.ui_config_color_main), false, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ServiceWebActivity.start(mContext, Constants.WEB_AGREEMENT, "用户协议");
                    }
                })
                .append("和")
                .append("《隐私条款》")
                .setClickSpan(ContextCompat.getColor(mContext, R.color.ui_config_color_main), false, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ServiceWebActivity.start(mContext, Constants.WEB_PRIVACY, "隐私条款");
                    }
                })
                .create();

        mScrollContainer = view.findViewById(R.id.ui_con);
        //mScrollContainer.setMaxHeight(getContentAreaMaxHeight());
        mScrollContainer.setVerticalScrollBarEnabled(false);

        subTvTitle = (TextView) view.findViewById(R.id.tv_sub_tile);
        buttonCancel = (Button) view.findViewById(R.id.btn_cancel);
        buttonOK = (Button) view.findViewById(R.id.btn_ok);
        setContentView(view);
    }


//    protected int getContentAreaMaxHeight() {
//        if (mContentAreaMaxHeight == -1) {
//            // 屏幕高度的0.85 - 预估的 title 和 action 高度
//            return (int) (UIDisplayHelper.getScreenHeight(mContext) * 0.85) - UIDisplayHelper.dp2px(mContext, 100);
//        }
//        return mContentAreaMaxHeight;
//    }

}
