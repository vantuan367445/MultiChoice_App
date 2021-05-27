package com.example.multichoice_app.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.multichoice_app.R;

import com.example.multichoice_app.listener.ChoiceCallback;
import com.example.multichoice_app.model.JSONExamLogObject;
import com.example.multichoice_app.model.JSONQuestionObject;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.gson.Gson;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class MyFragment extends BaseFragment {
    private View view;
    private int page_now,exam_id;
        private JSONQuestionObject jsonQuestionObject = null;
    private JSONExamLogObject.QuestionLog questionLog = null;
    private ChoiceCallback choiceCallback;

    @BindView(R.id.pdf_viewer)
    PDFView pdfView;

    LinearLayout linear_choiceA, linear_choiceB, linear_choiceC, linear_choiceD;
    TextView txt_IdchoiceA, txt_IdchoiceB, txt_IdchoiceC, txt_IdchoiceD;
    //
    int id_Linear_now = -1; // đay là id của Linearlayout để lưu xem đáp án nào được chọn  ,
    // và nếu người dùng thay đổi đáp án thì id_Linear_now sẽ được cập nhật


    public static Fragment newInstance(int exam_id,int page, String jsonQue, String jsonQueLog, ChoiceCallback choiceCallback) {
        MyFragment fragmentFirst = new MyFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("page_current", page);
        bundle.putInt("exam_id", exam_id);
        bundle.putString("json_Question", jsonQue);
        bundle.putString("json_QuestionLog", jsonQueLog);
        fragmentFirst.setArguments(bundle);
        fragmentFirst.setListener(choiceCallback);
        return fragmentFirst;
    }

    private void setListener(ChoiceCallback choiceCallback) {
        this.choiceCallback = choiceCallback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            page_now = bundle.getInt("page_current", 0);// nếu setText page thì nó bắt đầu từ 0 còn nếu Toast thì nó bắt đầu từ 1
            exam_id = bundle.getInt("exam_id", 0);
            jsonQuestionObject = new Gson().fromJson(bundle.getString("json_Question"), JSONQuestionObject.class);
            questionLog = new Gson().fromJson(bundle.getString("json_QuestionLog"), JSONExamLogObject.QuestionLog.class);
            Log.d("json_QuestionLog", new Gson().toJson(questionLog));
            initComponent();
            addEvents();
        }else Log.d("CHECK_CHECK","null1");

    }

    private void addEvents() {
        if (!questionLog.getChoosenAnswer().isEmpty()) {
            // neu cau tra loi dc tra loi
            switch (questionLog.getChoosenAnswer()) {
                case "A":
                    txt_IdchoiceA.setBackgroundResource(R.drawable.custom_circle_button_chon);
                    break;
                case "B":
                    txt_IdchoiceB.setBackgroundResource(R.drawable.custom_circle_button_chon);
                    break;
                case "C":
                    txt_IdchoiceC.setBackgroundResource(R.drawable.custom_circle_button_chon);
                    break;
                default:
                    txt_IdchoiceD.setBackgroundResource(R.drawable.custom_circle_button_chon);
                    break;
            }
        }

        linear_choiceA.setOnClickListener(view -> {
            questionLog.setDateTime(getTime());
            if (questionLog.getChoosenAnswer().isEmpty() || !questionLog.getChoosenAnswer().equals("A")){
                questionLog.setChoosenAnswer("A");
                choiceCallback.execute(page_now, questionLog);
            }
            xulycautraloi(linear_choiceA.getId());
        });
        linear_choiceB.setOnClickListener(view -> {
            questionLog.setDateTime(getTime());
            if (questionLog.getChoosenAnswer().isEmpty() || !questionLog.getChoosenAnswer().equals("B")){
                questionLog.setChoosenAnswer("B");
                choiceCallback.execute(page_now, questionLog);
            }
            xulycautraloi(linear_choiceB.getId());
        });
        linear_choiceC.setOnClickListener(view -> {
            questionLog.setDateTime(getTime());
            if (questionLog.getChoosenAnswer() .isEmpty() || !questionLog.getChoosenAnswer().equals("C")){
                questionLog.setChoosenAnswer("C");
                choiceCallback.execute(page_now, questionLog);
            }
            xulycautraloi(linear_choiceC.getId());
        });
        linear_choiceD.setOnClickListener(view -> {
            questionLog.setDateTime(getTime());
            if (questionLog.getChoosenAnswer() .isEmpty() || !questionLog.getChoosenAnswer().equals("D")){
                questionLog.setChoosenAnswer("D");
                choiceCallback.execute(page_now, questionLog);
            }


            xulycautraloi(linear_choiceD.getId());
        });

    }


    private void initComponent() {
        setDataFragment();

        linear_choiceA = view.findViewById(R.id.linear_choiceA);
        linear_choiceB = view.findViewById(R.id.linear_choiceB);
        linear_choiceC = view.findViewById(R.id.linear_choiceC);
        linear_choiceD = view.findViewById(R.id.linear_choiceD);
        txt_IdchoiceA = view.findViewById(R.id.txt_IdchoiceA);
        txt_IdchoiceB = view.findViewById(R.id.txt_IdchoiceB);
        txt_IdchoiceC = view.findViewById(R.id.txt_IdchoiceC);
        txt_IdchoiceD = view.findViewById(R.id.txt_IdchoiceD);
    }


    private void xulycautraloi(int id_linearLayout) {
        if (linear_choiceA.getId() == id_linearLayout) {
            txt_IdchoiceA.setBackgroundResource(R.drawable.custom_circle_button_chon);
            if (id_Linear_now != -1 && id_Linear_now != id_linearLayout) {
                if (id_Linear_now == linear_choiceB.getId())
                    txt_IdchoiceB.setBackgroundResource(R.drawable.custom_circle_button_khong_chon);
                else if (id_Linear_now == linear_choiceC.getId())
                    txt_IdchoiceC.setBackgroundResource(R.drawable.custom_circle_button_khong_chon);
                if (id_Linear_now == linear_choiceD.getId())
                    txt_IdchoiceD.setBackgroundResource(R.drawable.custom_circle_button_khong_chon);
            }
        } else if (linear_choiceB.getId() == id_linearLayout) {
            txt_IdchoiceB.setBackgroundResource(R.drawable.custom_circle_button_chon);
            if (id_Linear_now != -1 && id_Linear_now != id_linearLayout) {
                if (id_Linear_now == linear_choiceA.getId())
                    txt_IdchoiceA.setBackgroundResource(R.drawable.custom_circle_button_khong_chon);
                else if (id_Linear_now == linear_choiceC.getId())
                    txt_IdchoiceC.setBackgroundResource(R.drawable.custom_circle_button_khong_chon);
                if (id_Linear_now == linear_choiceD.getId())
                    txt_IdchoiceD.setBackgroundResource(R.drawable.custom_circle_button_khong_chon);
            }
        } else if (linear_choiceC.getId() == id_linearLayout) {
            txt_IdchoiceC.setBackgroundResource(R.drawable.custom_circle_button_chon);
            if (id_Linear_now != -1 && id_Linear_now != id_linearLayout) {
                if (id_Linear_now == linear_choiceB.getId())
                    txt_IdchoiceB.setBackgroundResource(R.drawable.custom_circle_button_khong_chon);
                else if (id_Linear_now == linear_choiceA.getId())
                    txt_IdchoiceA.setBackgroundResource(R.drawable.custom_circle_button_khong_chon);
                if (id_Linear_now == linear_choiceD.getId())
                    txt_IdchoiceD.setBackgroundResource(R.drawable.custom_circle_button_khong_chon);
            }
        } else {
            txt_IdchoiceD.setBackgroundResource(R.drawable.custom_circle_button_chon);
            if (id_Linear_now != -1 && id_Linear_now != id_linearLayout) {
                if (id_Linear_now == linear_choiceB.getId())
                    txt_IdchoiceB.setBackgroundResource(R.drawable.custom_circle_button_khong_chon);
                else if (id_Linear_now == linear_choiceC.getId())
                    txt_IdchoiceC.setBackgroundResource(R.drawable.custom_circle_button_khong_chon);
                if (id_Linear_now == linear_choiceA.getId())
                    txt_IdchoiceA.setBackgroundResource(R.drawable.custom_circle_button_khong_chon);
            }
        }

        id_Linear_now = id_linearLayout;
    }


    @SuppressLint("SimpleDateFormat")
    private String getTime() {
        Calendar calendar = Calendar.getInstance();
        String strDateFormat24 = "HH:mm:ss";
        SimpleDateFormat sdf1 = new SimpleDateFormat(strDateFormat24);
        String time = sdf1.format(calendar.getTime());
        String strDateFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf2 = new SimpleDateFormat(strDateFormat);
        String day = sdf2.format(calendar.getTime());
        return time + " - " + day;
    }
    private void setDataFragment(){
        if(jsonQuestionObject != null && getActivity() != null) {
            String path_PDF = getActivity().getFilesDir() + "/jsonExam/pdf_" + exam_id + "/cau" + jsonQuestionObject.getQuestionId() + ".pdf";
            Log.d("CHECK_CHECK","path = " + path_PDF);
            File file = new File(path_PDF);
            if (file.exists()) {
                pdfView.fromFile(file)
                        .password(null)
                        .nightMode(preferenceHelper.getThemeValue() != 0)
                        .load();

            }
        }

    }
}
