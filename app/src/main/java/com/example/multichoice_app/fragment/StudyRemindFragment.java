package com.example.multichoice_app.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;

import com.example.multichoice_app.R;
import com.example.multichoice_app.common.GlobalHelper;
import com.example.multichoice_app.common.PreferenceHelper;
import com.example.multichoice_app.listener.VoidCallback;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class StudyRemindFragment extends AppCompatDialogFragment {

    @BindView(R.id.card_timePicker)
    CardView card_timePicker;
    @BindView(R.id.card_save)
    CardView card_save;
    @BindView(R.id.sc_time_onboard_3)
    SwitchCompat sc_time_onboard_3;

    @BindView(R.id.line_timePicker)
    LinearLayout line_timePicker;
    @BindView(R.id.hour)
    NumberPicker hour;
    @BindView(R.id.minute)
    NumberPicker minute;


    @BindView(R.id.txt_time)
    TextView txt_time;

    @BindDrawable(R.drawable.bg_button_red_1)
    Drawable bg_button_red_1;
    @BindDrawable(R.drawable.bg_button_white_5)
    Drawable bg_button_white_5;

    private PreferenceHelper preferenceHelper;
    private int hourValue;
    private int minuteValue;
    private VoidCallback saveCallback;

    public static StudyRemindFragment newInstance(VoidCallback saveCallback) {
        StudyRemindFragment fragment = new StudyRemindFragment();
        fragment.setListener(saveCallback);
        return fragment;
    }
    private void setListener(VoidCallback voidCallback){
        this.saveCallback = voidCallback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_study_remind, container, false);
        ButterKnife.bind(this, view);
        if (getDialog() != null && getDialog().getWindow() != null)
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.bottom_top_dialog);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferenceHelper = new PreferenceHelper(getContext(), GlobalHelper.PREFERENCE_NAME_APP);

        card_timePicker.setBackground(bg_button_red_1);
        card_save.setBackground(bg_button_white_5);
        txt_time.setText(preferenceHelper.getStudyRemind());

        hour.setMaxValue(23);
        hour.setMinValue(0);
        hour.setFormatter(TWO_DIGIT_FORMATTER);
        minute.setMaxValue(59);
        minute.setMinValue(0);
        minute.setFormatter(TWO_DIGIT_FORMATTER);

        String[] list1 = new String[60];
        String[] list2 = new String[24];
        for(int i = 0 ; i < 60 ; i ++){
            if(i < 10){
                list1[i] = "0" + i;
                list2[i] = "0" + i;
            }else if( i < 24){
                list1[i] = i +"";
                list2[i] = i +"";
            }
            else list1[i] = i +"";
        }

        minuteValue = Integer.parseInt(String.valueOf(minute.getValue()));
        hourValue = Integer.parseInt(String.valueOf(hour.getValue()));

        minute.setDisplayedValues(list1);
        hour.setDisplayedValues(list2);

        if(preferenceHelper.getStudyRemind().contains(":") && preferenceHelper.getStudyRemind().split(":").length == 2){
            String[] string = preferenceHelper.getStudyRemind().split(":");
            try {
                hour.setValue(Integer.parseInt(string[0]));
                minute.setValue(Integer.parseInt(string[1]));

                hourValue = Integer.parseInt(string[0]);
                minuteValue = Integer.parseInt(string[1]);
            }catch (NumberFormatException ex){
                hour.setValue(0);
                minute.setValue(0);
                hourValue = 0;
                minuteValue = 0;
            }

        }

        sc_time_onboard_3.setChecked(preferenceHelper.isNotifyReminder());
        if (sc_time_onboard_3.isChecked()){
            hour.setEnabled(true);
            minute.setEnabled(true);
        }else {
            hour.setEnabled(false);
            minute.setEnabled(false);
        }

        hour.setOnValueChangedListener((picker, oldVal, newVal) ->{
            hourValue = newVal;
            setValueText();
        });
        minute.setOnValueChangedListener((picker, oldVal, newVal) -> {
            minuteValue = newVal;
            setValueText();
        });

        card_save.setOnClickListener(v ->{
            if(saveCallback != null){
                preferenceHelper.setStudyRemind(txt_time.getText().toString());
                saveCallback.execute();
            }
            dismiss();

        });
        sc_time_onboard_3.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceHelper.setNotifyReminder(isChecked);
            sc_time_onboard_3.setChecked(preferenceHelper.isNotifyReminder());
            if (sc_time_onboard_3.isChecked()){
                hour.setEnabled(true);
                minute.setEnabled(true);
            }else {
                hour.setEnabled(false);
                minute.setEnabled(false);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setValueText(){
        String text1 = hourValue < 10 ? ("0" + hourValue ) : (hourValue +"");
        String text2 = minuteValue < 10 ? ("0" + minuteValue ) : (minuteValue +"");
        txt_time.setText(text1 +":" + text2);
    }

    @SuppressLint("DefaultLocale")
    public static final NumberPicker.Formatter TWO_DIGIT_FORMATTER =
            value -> {
                // TODO Auto-generated method stub
                return String.format("%02d", value);
            };


}
