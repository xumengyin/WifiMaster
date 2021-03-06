/**
 * @Title: OFAlertDialog.java
 * @Package com.cpsdna.libs.ui.widget
 */
package com.jerry.wifimaster.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jerry.wifimaster.R;


/**
 *
 */
public class BaseAlertDialog extends Dialog {

    private Context mContext;
    /**
     * 标题
     */
    private TextView mTitle;
    private View mCustomPanel;


    /**
     * 消息
     */
    private TextView mMessage;
    /** 标题区域 */
//	private View mTopPanel;
    /**
     * 内容区域
     */
    //private View mContentMsg;

    /**
     * 右边按钮
     */
    private Button mPositiveBtn;
    private View.OnClickListener mPositionBtnListener;
    /**
     * 左边按钮
     */
    private Button mNegativeBtn;
    private View.OnClickListener mNegativeBtnListener;

    private boolean controlDismiss=true;

    private View.OnClickListener mButtonListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v.equals(mPositiveBtn)) {
                if (mPositionBtnListener != null)
                    mPositionBtnListener.onClick(v);

            } else if (v.equals(mNegativeBtn)) {
                if (mNegativeBtnListener != null)
                    mNegativeBtnListener.onClick(v);

            }
            if(controlDismiss)
                 dismiss();
        }
    };

    public BaseAlertDialog setControlDismiss(boolean controlDismiss) {
        this.controlDismiss = controlDismiss;
        return this;
    }

    public BaseAlertDialog(Context context) {
        this(context, R.layout.base_dialog);
    }

    public BaseAlertDialog(Context context, int contentLayoutId) {
        super(context, R.style.CustomDialog);
        mContext = context;
        setContentView(contentLayoutId);
        setCanceledOnTouchOutside(true);

        mTitle = (TextView) findViewById(R.id.tv_sub_tile);
        mMessage = (TextView) findViewById(R.id.msg);
       // mContentMsg = findViewById(R.id.msg);
        mCustomPanel = findViewById(R.id.content);
        mPositiveBtn = (Button) findViewById(R.id.btn_ok);
        mNegativeBtn = (Button) findViewById(R.id.btn_cancel);
        mPositiveBtn.setOnClickListener(mButtonListener);
        mNegativeBtn.setOnClickListener(mButtonListener);
    }


    public BaseAlertDialog setTitles(CharSequence title) {
        mTitle.setText(title);
        return this;
    }

    public BaseAlertDialog setTitles(int titleId) {
        mTitle.setText(titleId);
        return this;
    }

    public BaseAlertDialog setMessage(CharSequence message) {
        mMessage.setVisibility(View.VISIBLE);
        mCustomPanel.setVisibility(View.GONE);
        mMessage.setText(message);
        return this;
    }

    public BaseAlertDialog setMessage(int messageId) {
        mMessage.setVisibility(View.VISIBLE);
        mCustomPanel.setVisibility(View.GONE);
        mMessage.setText(messageId);
        return this;
    }

    public BaseAlertDialog setCustomView(int resId) {
        mMessage.setVisibility(View.GONE);
        mCustomPanel.setVisibility(View.VISIBLE);
        View customView = View.inflate(mContext, resId, null);
        ((FrameLayout) mCustomPanel).addView(customView);
        return this;
    }

    public View getCustomView() {
        return mCustomPanel;
    }

    public BaseAlertDialog setPositiveButton(int textId, View.OnClickListener listener) {
        mPositiveBtn.setVisibility(View.VISIBLE);
        mPositiveBtn.setText(textId);
        mPositionBtnListener = listener;
        return this;
    }

    public BaseAlertDialog setPositiveButton(String text) {
        if (TextUtils.isEmpty(text)) {
            mPositiveBtn.setVisibility(View.GONE);
        } else {
            mPositiveBtn.setVisibility(View.VISIBLE);
            mPositiveBtn.setText(text);
        }
        return this;
    }

    public BaseAlertDialog setPositiveButtonListener(View.OnClickListener listener) {
        mPositionBtnListener = listener;
        return this;
    }

    public Button getOkButton() {
        return mPositiveBtn;
    }

    public BaseAlertDialog setNegativeButton(int textId, View.OnClickListener listener) {
        mNegativeBtn.setVisibility(View.VISIBLE);
        mNegativeBtn.setText(textId);
        mNegativeBtnListener = listener;
        return this;
    }

    public BaseAlertDialog setNegativeButton(String text) {
        if (TextUtils.isEmpty(text)) {
            mNegativeBtn.setVisibility(View.GONE);
        } else {
            mNegativeBtn.setVisibility(View.VISIBLE);
            mNegativeBtn.setText(text);
        }
        return this;
    }

    public BaseAlertDialog setNegativeButtonListener(View.OnClickListener listener) {
        mNegativeBtnListener = listener;
        return this;
    }


    public static BaseAlertDialog createPasswordDialog(Context context,String wifi) {
        BaseAlertDialog dialog = new BaseAlertDialog(context);
        dialog.setTitles(wifi).setCustomView(R.layout.dialog_custom1).setControlDismiss(false);
        return dialog;
    }

    public static BaseAlertDialog createCommonDialog(Context context,String title,String msg)
    {
        BaseAlertDialog dialog = new BaseAlertDialog(context);
        dialog.setTitles(title).setMessage(msg);
        return dialog;
    }
}
