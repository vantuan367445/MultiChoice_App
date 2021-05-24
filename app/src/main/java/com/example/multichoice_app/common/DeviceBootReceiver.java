package com.example.multichoice_app.common;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

public class DeviceBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        PreferenceHelper preferenceHelper = new PreferenceHelper(context, GlobalHelper.PREFERENCE_NAME_APP);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Objects.equals(intent.getAction(), "android.intent.action.BOOT_COMPLETED")) {
                // on device boot complete, reset the alarm
                Intent alarmIntent = new Intent(context, ReminderService.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, alarmIntent, 0);

                AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                String string_Time = preferenceHelper.getStudyRemind();
                if (string_Time.isEmpty())
                    string_Time = "00:00";
                String[] time = string_Time.split(":");

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                int hour, minute;
                try {
                    hour = Integer.parseInt(time[0]);
                    minute = Integer.parseInt(time[1]);
                } catch (Exception e) {
                    hour = 0;
                    minute = 0;
                }
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 1);

                Calendar newC = new GregorianCalendar();
                newC.setTimeInMillis(preferenceHelper.getNextNotifyTime());

                if (calendar.after(newC)) {
                    calendar.add(Calendar.HOUR, 1);
                }

                if (manager != null) {
                    manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY, pendingIntent);
                }
            }
        }

    }


}