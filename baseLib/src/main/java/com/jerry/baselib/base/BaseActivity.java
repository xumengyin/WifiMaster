package com.jerry.baselib.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.jerry.baselib.R;
import com.jerry.baselib.http.AbstractRequest;
import com.jerry.baselib.http.CallServer;
import com.jerry.baselib.http.HttpCallback;
import com.jerry.baselib.utils.Configs;
import com.jerry.baselib.utils.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;



public abstract class BaseActivity extends AppCompatActivity implements BaseViewInterface {

    public static final int REQUEST_CODE_UNKNOWN_APP = 991;

    protected final String TAG = getClass().getSimpleName();

    protected List<ImageView> imageViews = new ArrayList<>();

    protected Activity mContext;

    protected void setDefaultBack() {
        UINavigationView appBar = (UINavigationView) findViewById(R.id.uinv);
        if (appBar != null) {
            appBar.setNavigationBack(R.drawable.ui_ic_back, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    /**
     * 异步请求，是否显示dialog。
     */
    public <T> void request(@NonNull AbstractRequest<T> request, boolean dialog,
                            HttpCallback<T> httpCallback) {
        request.setCancelSign(this);
        CallServer.getInstance().request(this, request, httpCallback, dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        CustomDensityHelper.setCustomDensity(this, ((Application) getApplicationContext()));
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
            //ButterKnife.bind(this);
            setDefaultBack();
        }
        //UIStatusBarHelper.setStatusBarLightMode(this);
        mContext = this;
        initViews();
        loadData(savedInstanceState);
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        /**
//         * 必须设置setIntent
//         */
//        setIntent(intent);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseImageView();
        CallServer.getInstance().cancelBySign(this);
    }

    /**
     * 将imageView添加到imageView集合中
     *
     * @param imageView
     */
    protected void addImageView(ImageView imageView) {
        if (imageView != null) {
            imageViews.add(imageView);
        }

    }

    /**
     * 将imageView集合中imageView资源回收，手动消除内存
     */
    private void releaseImageView() {
        if (imageViews != null && imageViews.size() > 0) {
            for (ImageView imageView : imageViews) {
                Drawable d = imageView.getDrawable();
                if (d != null) {
                    d.setCallback(null);
                }
                imageView.setImageDrawable(null);
                imageView.setBackgroundDrawable(null);
            }
        }
        imageViews.clear();
        imageViews = null;
    }

    protected void showKeyboard(boolean isShow) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isShow) {
            if (getCurrentFocus() == null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            } else {
                imm.showSoftInput(getCurrentFocus(), 0);
            }
        } else {
            if (getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_UNKNOWN_APP) {
            intstall();
        }
    }

    public void intstall() {
        //安装策略
        try {
            LogUtils.d(TAG, TAG + "权限");
            if (TextUtils.isEmpty(Configs.Install_File)) {
                return;
            }
            File apk = new File(Configs.Install_File);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                boolean hasInstallPermission = getPackageManager().canRequestPackageInstalls();
                if (!hasInstallPermission) {
                    Uri selfPackageUri = Uri.parse("package:" + getPackageName());
                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, selfPackageUri);

                    startActivityForResult(intent, REQUEST_CODE_UNKNOWN_APP);
                    return;
                }
            }

            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri fileUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                fileUri = FileProvider.getUriForFile(this, Configs.FILE_PATH_PROVIDER, apk);
            } else {
                fileUri = Uri.fromFile(apk);
            }
            intent.setDataAndType(fileUri,
                    "application/vnd.android.package-archive");

            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Resources getResources() {
        try {
            Resources var1 = super.getResources();
            if (var1.getConfiguration().fontScale != 1.0F) {
                Configuration var2 = new Configuration();
                var2.setToDefaults();
                var1.updateConfiguration(var2, var1.getDisplayMetrics());
            }

            return var1;
        } catch (Exception var3) {
            var3.printStackTrace();
            return super.getResources();
        }
    }



    protected void onSessionResultSuccess(String id) {


    }
}
