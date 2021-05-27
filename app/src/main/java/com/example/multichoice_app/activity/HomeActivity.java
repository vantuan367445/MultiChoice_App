package com.example.multichoice_app.activity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.example.multichoice_app.R;

import com.example.multichoice_app.adapter.ViewPagerAdapter;
import com.example.multichoice_app.common.DeviceBootReceiver;
import com.example.multichoice_app.common.GlobalHelper;
import com.example.multichoice_app.common.PreferenceHelper;
import com.example.multichoice_app.common.ReminderService;
import com.example.multichoice_app.utils.APIUtills;
import com.example.multichoice_app.utils.CheckConnectiom;
import com.example.multichoice_app.utils.DataClient;
import com.example.multichoice_app.utils.view.NonSwipeAbleViewPager;
import com.example.multichoice_app.dbFlow.MyDataBase;
import com.example.multichoice_app.dbFlow.TypeDataSave;
import com.example.multichoice_app.fragment.AccountFragment;
import com.example.multichoice_app.fragment.HomeFragment;
import com.example.multichoice_app.listener.IntegerCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("NonConstantResourceId")
public class HomeActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.rela_notconnected)
    RelativeLayout real_not_connected;
    @BindView(R.id.botton_nav)
    BottomNavigationView botton_nav;
    @BindView(R.id.card_nav)
    CardView card_nav;
    @BindView(R.id.mViewpager)
    NonSwipeAbleViewPager mViewpager;
    @BindView(R.id.btn_thulai)
    CardView btn_thulai;

    @BindDrawable(R.drawable.custom_button_bo_goc_yellow)
    Drawable custom_button_bo_goc_yellow;

    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferenceHelper = new PreferenceHelper(this, GlobalHelper.PREFERENCE_NAME_APP);
        if (preferenceHelper.getThemeValue() == 0)
            setTheme(R.style.ThemeLight);
        else setTheme(R.style.ThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu);
        ButterKnife.bind(this);

        getDataPhone();
        getSubject();
        addEvents();
    }

    private void getSubject() {
        DataClient dataClient = APIUtills.getData();
        Call<String> callQuestions = dataClient.getSubject();
        callQuestions.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null && !response.body().isEmpty()) {
                    // thanh cong
                    String json = response.body();
                    MyDataBase.saveDataType(new TypeDataSave(GlobalHelper.data_subject,json));
                    initUI();
                } else {
                    //lỗi
                    String json = MyDataBase.loadDataType(GlobalHelper.data_subject);
                    if (!json.isEmpty())
                        initUI();
                    else showPlaceHolder(false);
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //lỗi
                String json = MyDataBase.loadDataType(GlobalHelper.data_subject);
                if (!json.isEmpty())
                    initUI();
                else showPlaceHolder(false);
            }
        });
    }

    private void initUI() {
        btn_thulai.setBackground(custom_button_bo_goc_yellow);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        HomeFragment homeFragment = new HomeFragment();
        AccountFragment accountFragment = AccountFragment.newInstance(accountCallback);
        adapter.addFragment(homeFragment);
        adapter.addFragment(accountFragment);

        mViewpager.setAdapter(adapter);
        mViewpager.setOffscreenPageLimit(2);
        mViewpager.setCurrentItem(0, false);

        showPlaceHolder(true);
    }
    private final IntegerCallback accountCallback = value -> {
        if(value == 0){ // language
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else if(value == 1){ // change theme
            //HomeActivity.this.recreate();
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else if(value == 2){ // logOut
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
            finish();
        }else if(value == 3){//set study remind
            Log.d("CHECK_CHECK","s = " + preferenceHelper.getStudyRemind());
            setStudyRemind();
        }
    };

    private void getDataPhone() {
        if (preferenceHelper.getStatusBarHeight() == 0)
            preferenceHelper.setStatusBarHeight(GlobalHelper.getStatusBarHeight(HomeActivity.this));
        if (preferenceHelper.getActionBarHeight() == 0)
            preferenceHelper.setActionBarHeight(GlobalHelper.getActionBarHeight(HomeActivity.this));
    }

    @SuppressLint("NonConstantResourceId")
    private void addEvents() {
        botton_nav.setSelectedItemId(R.id.nav_ic_home);
        botton_nav.setItemIconTintList(null);
        botton_nav.setOnNavigationItemSelectedListener(this);
    }


    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_thulai)
    void onClick(View view) {
        if (view.getId() == R.id.btn_thulai) {
            boolean checkConnect = CheckConnectiom.checkCon(HomeActivity.this);
            if (checkConnect)
                getSubject();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_ic_home:
                mViewpager.setCurrentItem(0, true);
                return true;
            case R.id.nav_ic_account:
                mViewpager.setCurrentItem(1, true);
                return true;
        }
        return false;
    }

    private void showPlaceHolder(boolean isExistData) {
        if (!isExistData) {
            real_not_connected.setVisibility(View.VISIBLE);
            mViewpager.setVisibility(View.GONE);
            card_nav.setVisibility(View.GONE);
        } else {
            real_not_connected.setVisibility(View.GONE);
            mViewpager.setVisibility(View.VISIBLE);
            card_nav.setVisibility(View.VISIBLE);
        }
    }
    private void setStudyRemind(){
        PackageManager pm = HomeActivity.this.getPackageManager();
        ComponentName receiver = new ComponentName(HomeActivity.this, DeviceBootReceiver.class);
        Intent alarmIntent = new Intent(HomeActivity.this, ReminderService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(HomeActivity.this, 100, alarmIntent, 0);
        AlarmManager manager = (AlarmManager) HomeActivity.this.getSystemService(Context.ALARM_SERVICE);
        if (preferenceHelper.isNotifyReminder()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());

            String string_Time = preferenceHelper.getStudyRemind();
            if (string_Time.isEmpty())
                string_Time = "00:00";

            int hour, minute;
            try {
                String[] time = string_Time.split(":");
                hour = Integer.parseInt(time[0]);
                minute = Integer.parseInt(time[1]);
            } catch (Exception e) {
                hour = 0;
                minute = 0;
            }
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 1);
            // if notification time is before selected time, send notification the next day
            if (Calendar.getInstance().after(calendar))
                calendar.add(Calendar.DATE, 1);

            if (manager != null) {
                manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pendingIntent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }
            }
            //To enable Boot Receiver class
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
        } else { //Disable Daily Notifications
            if (PendingIntent.getBroadcast(HomeActivity.this, 100, alarmIntent, 0) != null && manager != null)
                manager.cancel(pendingIntent);
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
        }
    }
}
