package com.example.multichoice_app.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.multichoice_app.R;
import com.example.multichoice_app.common.AnimationHelper;
import com.example.multichoice_app.listener.IntegerCallback;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("NonConstantResourceId")
public class BottomSheetReport extends BottomSheetDialogFragment {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.btn_email)
    CardView btn_email;
    @BindView(R.id.btn_facebook)
    CardView btn_facebook;

    @BindString(R.string.choose_contact)
    String choose_contact;

    @BindDrawable(R.drawable.bg_button_white_6_light)
    Drawable bg_button_white_6_light;
    @BindDrawable(R.drawable.bg_button_white_6_night)
    Drawable bg_button_white_6_night;

    private IntegerCallback itemClickCallback;

    public static BottomSheetReport newInstance(IntegerCallback itemClickCallback, int themeID) {
        Bundle args = new Bundle();
        args.putInt("THEME_ID", themeID);
        BottomSheetReport fragment = new BottomSheetReport();
        fragment.setArguments(args);
        fragment.setListener(itemClickCallback);
        return fragment;
    }

    private void setListener(IntegerCallback itemClickCallback) {
        this.itemClickCallback = itemClickCallback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_report, container, false);
        if (getDialog() != null && getDialog().getWindow() != null)
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        if (bundle != null) {
            int themeID = bundle.getInt("THEME_ID", 0);
            tv_title.setText(choose_contact);
            btn_email.setBackground(themeID == 0 ?bg_button_white_6_light : bg_button_white_6_night);
            btn_facebook.setBackground(themeID == 0 ?bg_button_white_6_light : bg_button_white_6_night);
        }
    }

    @OnClick({R.id.btn_email, R.id.btn_facebook})
    void onClick(View view) {
        AnimationHelper.ScaleAnimation(view, () -> {
            switch (view.getId()) {
                case R.id.btn_email:
                    if (itemClickCallback != null)
                        itemClickCallback.execute(0);
                    break;
                case R.id.btn_facebook:
                    if (itemClickCallback != null)
                        itemClickCallback.execute(1);
                    break;
            }
        }, 0.98f);
    }

}
