package com.example.multichoice_app.common;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;

public class PreferenceHelper {
    private SharedPreferences sharedPreferences;
    private Context context;
    public PreferenceHelper(Context context, String name) {
        if (context == null) {
            return;
        }
        sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        this.context = context;
    }

    public String getProfile() {
        if (sharedPreferences == null) return "";
        return sharedPreferences.getString(GlobalHelper.profile, "");
    }

    public void setProfile(String newValue) {
        if (sharedPreferences == null) return;
        sharedPreferences.edit().putString(GlobalHelper.profile, newValue).apply();
    }
    public void setStatusBarHeight(int newValue) {
        if (sharedPreferences == null) return;
        sharedPreferences.edit().putInt(GlobalHelper.statusBarHeight, newValue).apply();
    }
    public int getStatusBarHeight(){
        if(sharedPreferences == null) return  0;
        return sharedPreferences.getInt(GlobalHelper.statusBarHeight, 0);
    }

    public void setActionBarHeight(int newValue) {
        if (sharedPreferences == null) return;
        sharedPreferences.edit().putInt(GlobalHelper.actionBarHeight, newValue).apply();
    }
    public int getActionBarHeight(){
        if(sharedPreferences == null) return  0;
        return sharedPreferences.getInt(GlobalHelper.actionBarHeight, 0);
    }

    public void setLanguageDevice(String lang){
        if (sharedPreferences == null) return;
        sharedPreferences.edit().putString(GlobalHelper.languageDevice, lang).apply();
    }
    public String getLanguageDevice(){
        if (sharedPreferences == null) return "en";
        return sharedPreferences.getString(GlobalHelper.languageDevice, "en");
    }

    public int getThemeValue(){
        if (sharedPreferences == null) return 0;
        return sharedPreferences.getInt(GlobalHelper.themeValue, 0); // 0  = light, 1 = night // default 1
    }

    public void setThemeValue(int value){
        if (sharedPreferences == null) return;
        sharedPreferences.edit().putInt(GlobalHelper.themeValue, value).apply();
    }


    public void setStudyRemind(String lang){
        if (sharedPreferences == null) return;
        sharedPreferences.edit().putString(GlobalHelper.studyRemind, lang).apply();
    }
    public String getStudyRemind(){
        if (sharedPreferences == null) return "00:00";
        return sharedPreferences.getString(GlobalHelper.studyRemind, "00:00");
    }
    public Boolean isNotifyReminder() {
        if (sharedPreferences == null) return false;
        return sharedPreferences.getBoolean(GlobalHelper.isNotifyReminder, false);
    }

    public void setNotifyReminder(Boolean b) {
        if (sharedPreferences == null) return;
        sharedPreferences.edit().putBoolean(GlobalHelper.isNotifyReminder, b).apply();
    }
    long getNextNotifyTime() {
        if (sharedPreferences == null) return Calendar.getInstance().getTimeInMillis();

        return sharedPreferences.getLong(GlobalHelper.next_notify, Calendar.getInstance().getTimeInMillis());
    }

    void setNextNotifyTime(long newValue) {
        if (sharedPreferences == null) return;

        sharedPreferences.edit().putLong(GlobalHelper.next_notify, newValue).apply();
    }


}
