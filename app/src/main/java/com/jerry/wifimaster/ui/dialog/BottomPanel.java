package com.jerry.wifimaster.ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jerry.wifimaster.R;
import com.jerry.wifimaster.bean.WifiPanelMenu;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheetRootLayout;

import java.util.ArrayList;
import java.util.List;

public class BottomPanel extends QMUIBottomSheet.BottomGridSheetBuilder {

    static List<WifiPanelMenu> connectedMenus = new ArrayList<>();
    static List<WifiPanelMenu> unConnectMenus = new ArrayList<>();

    static {
        connectedMenus.add(new WifiPanelMenu("网络测速", R.drawable.panel_menu5, WifiPanelMenu.TYPE_TEST_SPEED));
        connectedMenus.add(new WifiPanelMenu("安全检测", R.drawable.panel_menu4, WifiPanelMenu.TYPE_CHECK));
        connectedMenus.add(new WifiPanelMenu("举报钓鱼", R.drawable.panel_menu3, WifiPanelMenu.TYPE_REPORT));
        connectedMenus.add(new WifiPanelMenu("忘记网络", R.drawable.panel_menu2, WifiPanelMenu.TYPE_FORGET_NET));
        unConnectMenus.add(new WifiPanelMenu("密码连接", R.drawable.panel_menu1, WifiPanelMenu.TYPE_PASS_CONNECT));
        unConnectMenus.add(new WifiPanelMenu("举报钓鱼", R.drawable.panel_menu3, WifiPanelMenu.TYPE_REPORT));
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

        View v = LayoutInflater.from(context).inflate(R.layout.panel_title_view, rootLayout, false);
        ((TextView) v.findViewById(R.id.wifiName)).setText(this.wifiName);
        ((ImageView) v.findViewById(R.id.wifiLock)).setImageResource(this.iconId);
        return v;
    }


    public static BottomPanel createCurNetDialog(Context context, String wifiName) {
        BottomPanel bottomPanel = new BottomPanel(context);
        bottomPanel.setWifiAndIcon(wifiName, R.drawable.top_wifi_connect);
//                    .addItem()

        for (WifiPanelMenu connectedMenu : connectedMenus) {
            bottomPanel.addItem(connectedMenu.icon, connectedMenu.title,connectedMenu, FIRST_LINE);
        }

        return bottomPanel;
    }

    public static BottomPanel createPassNetDialog(Context context, String wifiName) {
        BottomPanel bottomPanel = new BottomPanel(context);
        bottomPanel.setWifiAndIcon(wifiName, R.drawable.top_wifi_password);
        for (WifiPanelMenu connectedMenu : unConnectMenus) {
            bottomPanel.addItem(connectedMenu.icon, connectedMenu.title,connectedMenu, FIRST_LINE);
        }

        return bottomPanel;
    }
}
