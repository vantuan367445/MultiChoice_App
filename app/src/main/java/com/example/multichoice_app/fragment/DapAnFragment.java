package com.example.multichoice_app.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.multichoice_app.R;

import com.example.multichoice_app.model.JSONExamLogObject;
import com.example.multichoice_app.model.JSONQuestionObject;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.gson.Gson;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class DapAnFragment extends BaseFragment {
    View view;
    @BindView(R.id.pdf_viewer)
    PDFView pdfView;
    @BindView(R.id.linear_choiceADapAn)
    LinearLayout linear_choiceADapAn;
    @BindView(R.id.txt_IdchoiceADapAn)
    TextView txt_IdchoiceADapAn;
    @BindView(R.id.linear_choiceBDapAn)
    LinearLayout linear_choiceBDapAn;
    @BindView(R.id.txt_IdchoiceBDapAn)
    TextView txt_IdchoiceBDapAn;
    @BindView(R.id.linear_choiceCDapAn)
    LinearLayout linear_choiceCDapAn;
    @BindView(R.id.txt_IdchoiceCDapAn)
    TextView txt_IdchoiceCDapAn;
    @BindView(R.id.linear_choiceDDapAn)
    LinearLayout linear_choiceDDapAn;
    @BindView(R.id.txt_IdchoiceDDapAn)
    TextView txt_IdchoiceDDapAn;
    @BindView(R.id.img_truefalseDapAn)
    ImageView img_truefalseDapAn;

    private JSONQuestionObject jsonQuestionObject;
    private JSONExamLogObject.QuestionLog questionLog;
    private int exam_id;


    public static Fragment newInstance(int exam_id,int page_current, String jsonQuesLog, String jsonQues) {
        DapAnFragment fragmentFirst = new DapAnFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("page_current", page_current);
        bundle.putInt("exam_id", exam_id);
        bundle.putString("jsonQuestionLog", jsonQuesLog);
        bundle.putString("jsonQuestionObject", jsonQues);
        fragmentFirst.setArguments(bundle);
        return fragmentFirst;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dap_an, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){
            try {
                //int page_current = bundle.getInt("page_current", 0);
                questionLog = new Gson().fromJson(bundle.getString("jsonQuestionLog"), JSONExamLogObject.QuestionLog.class);
                jsonQuestionObject = new Gson().fromJson(bundle.getString("jsonQuestionObject"),JSONQuestionObject.class);
                exam_id = bundle.getInt("exam_id",0);
                Init();
            }catch (Exception e){
                Log.d("LOI", e.toString());
            }
        }

    }

    private void Init() {
        if(getActivity() == null)
            return;
        String path_PDF = getActivity().getFilesDir() + "/jsonExam/pdf_"+ exam_id + "/cau" + jsonQuestionObject.getQuestionId() + ".pdf";
        File file = new File(path_PDF);
        if(file.exists()){
            pdfView.fromFile(file)
                    .password(null)
                    .nightMode(preferenceHelper.getThemeValue() != 0)
                    .load();
        }
        initDapAn();
    }

    private void initDapAn() {

            if(questionLog != null){
                //int yourChoice = modelSaveAnswer.getNumber_answer();
                String yourChoice = questionLog.getChoosenAnswer();
                String severChoice = questionLog.getCorrectAnswer();
                //Toast.makeText(getContext(), yourChoice+"  = " + severChoice, Toast.LENGTH_SHORT).show();
                if(yourChoice.equals(severChoice)){
                    // neu cẩu trả lời trùng với đáp án của server
                    switch (yourChoice) {
                        case "A":
                            txt_IdchoiceADapAn.setBackgroundResource(R.drawable.custom_button_true);
                            break;
                        case "B":
                            txt_IdchoiceBDapAn.setBackgroundResource(R.drawable.custom_button_true);
                            break;
                        case "C":
                            txt_IdchoiceCDapAn.setBackgroundResource(R.drawable.custom_button_true);
                            break;
                        default:
                            txt_IdchoiceDDapAn.setBackgroundResource(R.drawable.custom_button_true);
                            break;
                    }

                    img_truefalseDapAn.setImageResource(R.drawable.ic_true_24dp);
                }
                else {
                    // nếu trả lời sai
                    if(yourChoice.isEmpty() || yourChoice  == null){
                        // nếu người thi cố tình không chọn thì ta hiện đáp án đúng nên
                        switch (severChoice) {
                            case "A":
                                txt_IdchoiceADapAn.setBackgroundResource(R.drawable.custom_button_true);
                                break;
                            case "B":
                                txt_IdchoiceBDapAn.setBackgroundResource(R.drawable.custom_button_true);
                                break;
                            case "C":
                                txt_IdchoiceCDapAn.setBackgroundResource(R.drawable.custom_button_true);
                                break;
                            default:
                                txt_IdchoiceDDapAn.setBackgroundResource(R.drawable.custom_button_true);
                                break;
                        }

                    }
                    else {
                        switch (severChoice) {
                            case "A":
                                txt_IdchoiceADapAn.setBackgroundResource(R.drawable.custom_button_true);
                                break;
                            case "B":
                                txt_IdchoiceBDapAn.setBackgroundResource(R.drawable.custom_button_true);
                                break;
                            case "C":
                                txt_IdchoiceCDapAn.setBackgroundResource(R.drawable.custom_button_true);
                                break;
                            default:
                                txt_IdchoiceDDapAn.setBackgroundResource(R.drawable.custom_button_true);
                                break;
                        }


                        switch (yourChoice) {
                            case "A":
                                txt_IdchoiceADapAn.setBackgroundResource(R.drawable.custom_button_false);
                                break;
                            case "B":
                                txt_IdchoiceBDapAn.setBackgroundResource(R.drawable.custom_button_false);
                                break;
                            case "C":
                                txt_IdchoiceCDapAn.setBackgroundResource(R.drawable.custom_button_false);
                                break;
                            default:
                                txt_IdchoiceDDapAn.setBackgroundResource(R.drawable.custom_button_false);
                                break;
                        }
                    }
                    img_truefalseDapAn.setImageResource(R.drawable.ic_false_24dp);
                }
        }
    }



}
