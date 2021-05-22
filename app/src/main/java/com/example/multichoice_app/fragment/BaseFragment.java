package com.example.multichoice_app.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.multichoice_app.common.GlobalHelper;
import com.example.multichoice_app.common.PreferenceHelper;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.greenrobot.eventbus.EventBus;

public class BaseFragment extends Fragment {
    public PreferenceHelper preferenceHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EventBus.getDefault().register(this);
        if (getContext() != null)
        preferenceHelper = new PreferenceHelper(getContext(), GlobalHelper.PREFERENCE_NAME_APP);
    }

    @Override
    public void onDestroy() {
        //EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}
