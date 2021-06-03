package com.example.multichoice_app.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.multichoice_app.BuildConfig;
import com.example.multichoice_app.R;

import com.example.multichoice_app.activity.HomeActivity;
import com.example.multichoice_app.activity.MainActivity;
import com.example.multichoice_app.common.AnimationHelper;
import com.example.multichoice_app.common.GlobalHelper;
import com.example.multichoice_app.listener.IntegerCallback;
import com.example.multichoice_app.listener.StringCallback;
import com.example.multichoice_app.listener.VoidCallback;
import com.example.multichoice_app.model.JSONStudentObject;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.squareup.picasso.Picasso;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

@SuppressLint("NonConstantResourceId")
public class AccountFragment extends BaseFragment {

    @BindView(R.id.profile_image)
    CircleImageView profile_image;
    @BindView(R.id.txt_personName)
    TextView txt_personName;
    @BindView(R.id.tv_language)
    TextView tv_language;
    @BindView(R.id.tv_email)
    TextView tv_email;
    @BindView(R.id.tv_version)
    TextView tv_version;
    @BindView(R.id.tv_theme)
    TextView tv_theme;

    @BindString(R.string.feature_dev)
    String feature_dev;
    private GoogleSignInClient mGoogleSignInClient;
    private Activity activity;
    private IntegerCallback accountCallback;

    public static AccountFragment newInstance(IntegerCallback accountCallback) {
        AccountFragment fragment = new AccountFragment();
        fragment.setListener(accountCallback);
        return fragment;
    }
    private void setListener(IntegerCallback accountCallback){
        this.accountCallback = accountCallback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        ButterKnife.bind(this, view);
        createRequest();
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupUI();
    }

    private void createRequest() {
        if (getActivity() == null)
            return;
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
    }

    private void setupUI() {
        JSONStudentObject jsonStudent;
        if (preferenceHelper.getProfile() != null && !preferenceHelper.getProfile().isEmpty())
            try {
                jsonStudent = new Gson().fromJson(preferenceHelper.getProfile(), JSONStudentObject.class);
            } catch (JsonSyntaxException e) {
                jsonStudent = null;
            }
        else jsonStudent = null;
        if (jsonStudent != null) {
            Picasso.with(getActivity()).load(jsonStudent.getUrl_Photo()).into(profile_image);
            txt_personName.setText(jsonStudent.getName());
        } else return;

        tv_language.setText(GlobalHelper.getLanguageApp(getContext(), preferenceHelper.getLanguageDevice()));
        tv_email.setText(jsonStudent.getEmail() != null ? jsonStudent.getEmail() : "null");
        tv_version.setText(BuildConfig.VERSION_NAME);
        tv_theme.setText(GlobalHelper.getTextTheme(getContext(), preferenceHelper.getThemeValue()));
    }

    @OnClick({R.id.btn_logout, R.id.relative_language, R.id.relative_theme, R.id.tv_remind, R.id.tv_feedback, R.id.tv_rating,
            R.id.tv_invite_friend})
    void action(View view) {
        switch (view.getId()) {
            case R.id.btn_logout:
                AnimationHelper.ScaleAnimation(view, logOutClick, 1.01f);
                break;
            case R.id.relative_language:
                AnimationHelper.ScaleAnimation(view, languageClick, 0.96f);
                break;
            case R.id.relative_theme:
                AnimationHelper.ScaleAnimation(view, themeClick, 0.96f);
                break;
            case R.id.tv_remind:
                AnimationHelper.ScaleAnimation(view, remindClick, 0.96f);
                break;
            case R.id.tv_feedback:
                AnimationHelper.ScaleAnimation(view, feedbackClick, 0.96f);
                break;
            case R.id.tv_rating:
                AnimationHelper.ScaleAnimation(view, ratingClick, 0.96f);
                break;
            case R.id.tv_invite_friend:
                AnimationHelper.ScaleAnimation(view, inviteClick, 0.96f);
                break;
        }

    }

    private final VoidCallback languageClick = new VoidCallback() {
        @Override
        public void execute() {
            GlobalHelper.showDialogLanguage(activity, preferenceHelper.getLanguageDevice(), preferenceHelper.getThemeValue(),click);
        }
    };
    private StringCallback click = new StringCallback() {
        @Override
        public void execute(String language) {
            if (!preferenceHelper.getLanguageDevice().equals(language)) {
                preferenceHelper.setLanguageDevice(language);
                tv_language.setText(GlobalHelper.getLanguageApp(getContext(), language));
                if (accountCallback != null)
                    accountCallback.execute(0);
            }
        }
    };

    private VoidCallback themeClick = new VoidCallback() {
        @Override
        public void execute() {
            GlobalHelper.showDialogTheme(activity, preferenceHelper.getThemeValue(),click2);
        }
    };
    private IntegerCallback click2 = new IntegerCallback() {
        @Override
        public void execute(int value) {
            if (value != preferenceHelper.getThemeValue()) {
                preferenceHelper.setThemeValue(value);
                tv_theme.setText(GlobalHelper.getTextTheme(getContext(), value));
                if (accountCallback != null)
                    accountCallback.execute(1);
            }
        }
    };


    private final VoidCallback remindClick = this::setStudyRemind;
    private void setStudyRemind(){
        StudyRemindFragment fragment = StudyRemindFragment.newInstance(remindStudyCallback);
        fragment.show(getChildFragmentManager(),fragment.getTag());
    }
    private final VoidCallback remindStudyCallback = () -> {
        if(accountCallback != null)
            accountCallback.execute(3);
    };


    private final VoidCallback feedbackClick = () ->{

        BottomSheetReport bottom = BottomSheetReport.newInstance(new IntegerCallback() {
            @Override
            public void execute(int type) {
                switch (type){
                    case 0:
                        GlobalHelper.sendEmail(activity, "Hỗ trợ");
                        break;
                    case 1:
                        GlobalHelper.visit(activity, "https://m.me/ThunderPTIT");
                        break;
                }
            }
        }, preferenceHelper.getThemeValue());
        if (!bottom.isAdded())
            bottom.show(getChildFragmentManager(), bottom.getTag());
    };
    private final VoidCallback ratingClick = () -> Toast.makeText(activity, feature_dev, Toast.LENGTH_SHORT).show();
    private final VoidCallback inviteClick = () -> Toast.makeText(activity, feature_dev, Toast.LENGTH_SHORT).show();
    private final VoidCallback logOutClick = () -> {
        if (accountCallback != null) {
            mGoogleSignInClient.signOut();
            preferenceHelper.setProfile("");
            accountCallback.execute(2);
        }
    };


}
