package com.example.multichoice_app.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.TypedValue;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import com.example.multichoice_app.R;
import com.example.multichoice_app.listener.IntegerCallback;
import com.example.multichoice_app.listener.StringCallback;

public class GlobalHelper {
    public static String PREFERENCE_NAME_APP = "com.example.multichoice_app";


    public static String profile = "profile";
    public static String data_subject = "data_subject";
    public static String data_exam = "data_exam";
    public static String data_questions_status = "data_questions_status";
    public static String data_questions = "data_questions";
    public static String JSONExamLog_Object = "JSONExamLog_Object";
    public static String statusBarHeight = "statusBarHeight";
    public static String actionBarHeight = "actionBarHeight";
    public static String languageDevice = "languageDevice";
    public static String themeValue = "themeValue";
    public static String studyRemind = "studyRemind";
    public static String isNotifyReminder = "isNotifyReminder";
    public static String next_notify = "next_notify";

    public static int getStatusBarHeight(Activity activity) {
        int result = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getActionBarHeight(Activity activity) {
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, activity.getResources().getDisplayMetrics());
        return actionBarHeight;
    }
    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    public static void showDialogClickExam(Activity activity, IntegerCallback clickCallback
    ,String name,String time,String numberQuestion,int themeID,int status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.quiz_dialog);
        View mView = activity.getLayoutInflater().inflate(R.layout.dialog_click_exam, null);
        TextView txt_nameDeThi = mView.findViewById(R.id.txt_nameDeThi);
        TextView txt_socauhoiDeThi = mView.findViewById(R.id.txt_socauhoiDeThi);
        TextView txt_timelambaiDeThi = mView.findViewById(R.id.txt_timelambaiDeThi);

        LinearLayout line_continue = mView.findViewById(R.id.line_continue);
        LinearLayout line_txt_remake = mView.findViewById(R.id.line_txt_remake);
        TextView txt_seeResult = mView.findViewById(R.id.txt_seeResult);

        txt_nameDeThi.setText(name);
        txt_socauhoiDeThi.setText(numberQuestion +" " + activity.getResources().getString(R.string.sentence));
        txt_timelambaiDeThi.setText(time +" " + activity.getResources().getString(R.string.minute));
        txt_seeResult.setText(activity.getResources().getString(
                status == 2 ? R.string.see_result : R.string.text_continue));

        builder.setView(mView);
        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        line_continue.setBackground(activity.getResources().getDrawable(R.drawable.bg_button_red_3));

        line_continue.setOnClickListener(v -> {// 0 là mới, 1 là lưu, 2 nộp bài
            clickCallback.execute(status);
            dialog.dismiss();
        });
        line_txt_remake.setOnClickListener(v -> {
            clickCallback.execute(0);
            dialog.dismiss();
        });
        dialog.show();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public static void showDialogNopBai(Activity activity, IntegerCallback clickCallback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.quiz_dialog);
        View mView = activity.getLayoutInflater().inflate(R.layout.dialog_nop_bai, null);
        TextView txt_yes = mView.findViewById(R.id.txt_yes);
        TextView txt_no = mView.findViewById(R.id.txt_no);
        TextView txt_save = mView.findViewById(R.id.txt_save);

        builder.setView(mView);
        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



        txt_yes.setOnClickListener(v -> {
            clickCallback.execute(0);
            dialog.dismiss();
        });
        txt_no.setOnClickListener(v -> {
            clickCallback.execute(-1);
            dialog.dismiss();
        });
        txt_save.setOnClickListener(v -> {
            clickCallback.execute(1);
            dialog.dismiss();
        });
        dialog.show();
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    public static void showDialogScore(Activity activity, IntegerCallback clickCallback, int soCauDung, int sizeQuestion) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.quiz_dialog);
        View mView = activity.getLayoutInflater().inflate(R.layout.dialog_tinh_diem, null);
        TextView txt_number_correct = mView.findViewById(R.id.txt_number_correct);
        TextView txt_score = mView.findViewById(R.id.txt_score);
        CardView card_new_exam = mView.findViewById(R.id.card_new_exam);
        CardView card_see_result = mView.findViewById(R.id.card_see_result);

        txt_number_correct.setText(soCauDung + "/" + sizeQuestion);
        float score = (float) Math.round(((float) (soCauDung * 10) / (float) sizeQuestion) * 100) / 100;
        txt_score.setText(score + " " + activity.getResources().getString(R.string.score));
        builder.setView(mView);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        card_new_exam.setBackground(activity.getResources().getDrawable(R.drawable.custom_button_bo_goc_yellow));
        card_see_result.setBackground(activity.getResources().getDrawable(R.drawable.custom_button_bo_goc_blu));

        card_new_exam.setOnClickListener(v -> {
            clickCallback.execute(0);
            dialog.dismiss();
        });
        card_see_result.setOnClickListener(v -> {
            clickCallback.execute(1);
            dialog.dismiss();
        });
        dialog.show();
    }
    public static void showDialogTheme(Activity activity,int themeID,IntegerCallback themeCallback){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity,R.style.bottom_top_dialog);
        View view = activity.getLayoutInflater().inflate(R.layout.dialog_theme, null);

        RadioButton rd_light = view.findViewById(R.id.rd_light);
        RadioButton rd_night = view.findViewById(R.id.rd_night);

        builder.setView(view);
        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if(themeID == 0)
            rd_light.setChecked(true);
        else
            rd_night.setChecked(true);

        rd_light.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked && themeCallback != null) {
                themeCallback.execute(0);
                dialog.dismiss();
            }
        });
        rd_night.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked && themeCallback != null) {
                themeCallback.execute(1);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void showDialogLanguage(Activity activity, String languageDevice,int themeID, StringCallback languageListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.bottom_top_dialog);
        View view = activity.getLayoutInflater().inflate(R.layout.dialog_language, null);

        RadioButton rd_english = view.findViewById(R.id.rd_english);
        RadioButton rd_vietnam = view.findViewById(R.id.rd_vietnam);

        builder.setView(view);
        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if ("vi".equals(languageDevice)) {
            rd_vietnam.setChecked(true);
        } else {
            rd_english.setChecked(true);
        }

        final String[] language = new String[1];
        language[0] = languageDevice;
        rd_english.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                language[0] = "en";
                dialog.dismiss();
            }
        });

        rd_vietnam.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                language[0] = "vi";
                dialog.dismiss();
            }
        });


        dialog.setOnDismissListener(dialog1 -> {
            if (languageListener != null)
                languageListener.execute(language[0]);
        });

        dialog.show();
    }

    public static String getLanguageApp(Context context, String languageCode) {
        switch (languageCode) {
            case "en":
                return context.getResources().getString(R.string.english);
            case "vi":
                return context.getResources().getString(R.string.vietnamese);
        }

        return context.getResources().getString(R.string.english);
    }
    public static String getTextTheme(Context context, int themeID) {
        switch (themeID) {
            case 0:
                return context.getResources().getString(R.string.theme_light);
            case 1:
                return context.getResources().getString(R.string.theme_night);
        }
        return context.getResources().getString(R.string.theme_light);
    }

    public static void visit(Activity activity, String url) {

        Uri webpage = Uri.parse(url);

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            webpage = Uri.parse("http://" + url);
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(activity.getPackageManager()) != null)
            activity.startActivity(intent);
    }
}
