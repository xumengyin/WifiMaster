package com.jerry.wifimaster.ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jerry.wifimaster.R;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheetRootLayout;

public class BottomPanel extends QMUIBottomSheet.BottomGridSheetBuilder {


    static {

    }

    public int iconId;
    public CharSequence wifiName;

    public BottomPanel(Context context) {
        super(context);
    }


    public QMUIBottomSheet.BottomGridSheetBuilder setWifiAndIcon(CharSequence title, int icon) {
        this.iconId = icon;
        this.wifiName = title;
        return this;
    }

    @Nullable
    @Override
    protected View onCreateTitleView(@NonNull QMUIBottomSheet bottomSheet, @NonNull QMUIBottomSheetRootLayout rootLayout, @NonNull Context context) {

        View v = LayoutInflater.from(context).inflate(R.layout.panel_title_view, null, false);
        ((TextView) v.findViewById(R.id.wifiName)).setText(this.wifiName);
        ((ImageView) v.findViewById(R.id.wifiLock)).setImageResource(this.iconId);
        return v;
    }


    public static QMUIBottomSheet createCurNetDialog(Context context,String wifiName,int icon)
    {
        BottomPanel bottomPanel=new BottomPanel(context);
//        bottomPanel.setWifiAndIcon(wifiName,icon)
//                    .addItem()
        return null;
    }
}
