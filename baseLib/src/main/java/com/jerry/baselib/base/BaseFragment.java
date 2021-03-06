package com.jerry.baselib.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jerry.baselib.http.AbstractRequest;
import com.jerry.baselib.http.CallServer;
import com.jerry.baselib.http.HttpCallback;

import org.greenrobot.eventbus.EventBus;

public abstract class BaseFragment extends Fragment implements BaseViewInterface{


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(),null,false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initViews();
    }
    /**
     * 异步请求，是否显示dialog。
     */
    public <T> void request(@NonNull AbstractRequest<T> request, boolean dialog,
                            HttpCallback<T> httpCallback) {
        request.setCancelSign(this);
        CallServer.getInstance().request(getContext(), request, httpCallback, dialog);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CallServer.getInstance().cancelBySign(this);
        if(isRegisterEventBus())
        {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData(savedInstanceState);
        if(isRegisterEventBus())
        {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void initViews() {

    }

    @Override
    public void loadData(Bundle savedInstanceState) {

    }

    @Override
    public boolean isRegisterEventBus() {
        return false;
    }
}
