package com.example.multichoice_app.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.multichoice_app.R;
import com.example.multichoice_app.common.GlobalHelper;
import com.example.multichoice_app.common.LocaleHelper;
import com.example.multichoice_app.common.PreferenceHelper;

public abstract class BaseActivity extends AppCompatActivity {
    public PreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorRed2));
        }
        preferenceHelper = new PreferenceHelper(getApplicationContext(), GlobalHelper.PREFERENCE_NAME_APP);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }
}
