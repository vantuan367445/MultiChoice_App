package com.example.multichoice_app.activity;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.multichoice_app.R;
import com.example.multichoice_app.adapter.ExamAdapter;
import com.example.multichoice_app.common.GlobalHelper;
import com.example.multichoice_app.common.PreferenceHelper;
import com.example.multichoice_app.utils.APIUtills;
import com.example.multichoice_app.utils.CheckConnection;
import com.example.multichoice_app.utils.DataClient;

import com.example.multichoice_app.dbFlow.MyDataBase;
import com.example.multichoice_app.dbFlow.TypeDataSave;
import com.example.multichoice_app.listener.IntegerCallback;
import com.example.multichoice_app.model.JSONExamLogObject;
import com.example.multichoice_app.model.JSONExamsObject;
import com.example.multichoice_app.model.JSONQuestionObject;
import com.example.multichoice_app.model.JSONStudentObject;
import com.example.multichoice_app.model.ListQuestionObjectStatus;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("NonConstantResourceId")
public class ExamActivity extends BaseActivity {
    @BindView(R.id.card_toolBar)
    CardView card_toolBar;
    @BindView(R.id.relative_notConnected)
    RelativeLayout relative_notConnected;
    ArrayList<JSONExamsObject> arrayExam;
    ExamAdapter examAdapter;
    @BindView(R.id.recyclerViewDeThi)
    RecyclerView recyclerViewDeThi;
    @BindView(R.id.avi_loading)
    AVLoadingIndicatorView avi_loading;
    @BindView(R.id.relative_download)
    RelativeLayout relative_download;
    @BindView(R.id.car_download)
    CardView car_download;
    @BindView(R.id.relative_content)
    RelativeLayout relative_content;
    @BindView(R.id.txt_notData)
    TextView txt_notData;
    @BindView(R.id.txt_phantram)
    TextView txt_percent;
    @BindView(R.id.btn_thulai)
    TextView btn_thulai;
    @BindView(R.id.img_back)
    ImageView img_back;

    @BindDrawable(R.drawable.bg_button_red_2)
    Drawable bg_button_red_2;
    @BindDrawable(R.drawable.bg_button_white_2)
    Drawable bg_button_white_2;
    @BindDrawable(R.drawable.custom_button_bo_goc_yellow)
    Drawable custom_button_bo_goc_yellow;

    @BindString(R.string.try_later)
    String try_later;
    @BindString(R.string.internet_error_1)
    String internet_error_1;

    private String subject_code;
    private ArrayList<JSONQuestionObject> arrayQues;
    private int ques_current;
    private DataClient dataClient;
    private JSONExamsObject jsonExamsObject;
    private String studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferenceHelper = new PreferenceHelper(this, GlobalHelper.PREFERENCE_NAME_APP);
        if (preferenceHelper.getThemeValue() == 0)
            setTheme(R.style.ThemeLight);
        else setTheme(R.style.ThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        ButterKnife.bind(this);
        btn_thulai.setBackground(custom_button_bo_goc_yellow);

        checkSignInGetData();
    }

    private void checkSignInGetData() {
        if (!preferenceHelper.getProfile().isEmpty()) {
            try {
                // conver string(json ) - > object
                // object -> string(json)
                String jsonProfile = preferenceHelper.getProfile();
                JSONStudentObject studentObject = new Gson().fromJson(jsonProfile, JSONStudentObject.class);
                studentId = studentObject.getStudentId();
                initUI();
            } catch (JsonSyntaxException ex) {
                studentId = null;
            }
        } else studentId = null;
    }

    private void initUI() {
        dataClient = APIUtills.getData();

        Intent intent = getIntent();

        subject_code = intent.getStringExtra("SUBJECT_CODE");

        arrayExam = new ArrayList<>();
        examAdapter = new ExamAdapter(arrayExam, this, itemCallback, preferenceHelper.getThemeValue());
        recyclerViewDeThi.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewDeThi.setHasFixedSize(true);
        recyclerViewDeThi.setAdapter(examAdapter);

        getExams();
    }

    private void getExams() {
        showViewLoadData(true, false);
        dataClient = APIUtills.getData();
        subject_code = subject_code == null ? "0" : subject_code;
        Call<String> callExams = dataClient.getExams(subject_code);
        callExams.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // thanh cong
                if (response.body() != null && !response.body().isEmpty()) {
                    MyDataBase.saveDataType(new TypeDataSave(GlobalHelper.data_exam + "_" + subject_code, response.body()));
                    updateAdapter(response.body());
                } else
                    showPlaceHolderData(false);
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // lỗi
                String key = GlobalHelper.data_exam + "_" + subject_code;
                String json = MyDataBase.loadDataType(key);
                if (json != null && !json.isEmpty())
                    updateAdapter(json);
                else {
                    if (!CheckConnection.checkCon(ExamActivity.this))
                        showPlaceHolderNotConnect();
                    else showPlaceHolderData(false);
                }
            }
        });
    }

    public void updateAdapter(String json) {
        Type type = new TypeToken<ArrayList<JSONExamsObject>>() {
        }.getType();
        try {
            arrayExam = new Gson().fromJson(json, type);
        } catch (JsonSyntaxException ex) {
            arrayExam = new ArrayList<>();
        }

        if (arrayExam.size() > 0) {
            examAdapter.setNewData(arrayExam);
            showPlaceHolderData(true);
        } else showPlaceHolderData(false);
    }

    private final IntegerCallback itemCallback = new IntegerCallback() {
        @Override
        public void execute(int position) {
            ExamAdapter.ViewHolder viewHolder = (ExamAdapter.ViewHolder) recyclerViewDeThi.findViewHolderForAdapterPosition(position);
            if (viewHolder == null)
                return;
            new Handler(Looper.getMainLooper()).postDelayed(() -> viewHolder.line1.setBackground(null), 200);
            jsonExamsObject = arrayExam.get(position);

            String json = MyDataBase.loadDataType(GlobalHelper.data_questions_status + "_" + jsonExamsObject.getExamId().trim());
            if (!json.isEmpty()) {
                ListQuestionObjectStatus status = new Gson().fromJson(json, ListQuestionObjectStatus.class);
                if (status.getStatus() > 0) {
                    GlobalHelper.showDialogClickExam(ExamActivity.this, historyExamCallback, jsonExamsObject.getExamName(),
                            jsonExamsObject.getTime(), jsonExamsObject.getNumberOfQuestions(), preferenceHelper.getThemeValue(), status.getStatus());
                } else
                    getQuestions(jsonExamsObject.getExamId().trim());

            } else {
                getQuestions(jsonExamsObject.getExamId().trim());
            }

        }
    };
    private final IntegerCallback historyExamCallback = new IntegerCallback() {
        @Override
        public void execute(int status) {
            if (status == 0) { // moi
                getQuestions(jsonExamsObject.getExamId().trim());
            } else if (status == 1) {// luu => tiep tuc lam
                String json1 = MyDataBase.loadDataType(GlobalHelper.data_questions + "_" + jsonExamsObject.getExamId());
                String json2 = MyDataBase.loadDataType(GlobalHelper.data_questions_status + "_" + jsonExamsObject.getExamId().trim());

                if (!json1.isEmpty() && !json2.isEmpty()) {
                    Type type = new TypeToken<ArrayList<JSONQuestionObject>>() {
                    }.getType();
                    arrayQues = new Gson().fromJson(json1, type);
                    ListQuestionObjectStatus listQuestionObjectStatus = new Gson().fromJson(json2, ListQuestionObjectStatus.class);
                    Intent intent = new Intent(ExamActivity.this, PracticeActivity.class);
                    intent.putExtra("STATUS_PRACTICE",status);
                    intent.putExtra("JSON_QUESTION", new Gson().toJson(arrayQues));
                    intent.putExtra("JSONExamsObject", new Gson().toJson(jsonExamsObject));
                    intent.putExtra("STUDENT_ID", studentId);
                    intent.putExtra("JSON_QUESTION_LOG", new Gson().toJson(listQuestionObjectStatus.getArrayQueslog()));
                    intent.putExtra("TIME_PRACTICE", listQuestionObjectStatus.getTimePractice()); // da doi sang s
                    intent.putExtra("TIME_TOTAL", Long.parseLong(jsonExamsObject.getTime())*60);// *60 dổi sang s
                    startActivity(intent);
                    finish();
                }

            } else if (status == 2) { // nop bai => xem dap an
                String json1 = MyDataBase.loadDataType(GlobalHelper.data_questions + "_" + jsonExamsObject.getExamId());
                String json2 = MyDataBase.loadDataType(GlobalHelper.data_questions_status + "_" + jsonExamsObject.getExamId().trim());

                if (!json1.isEmpty() && !json2.isEmpty()) {
                    Type type = new TypeToken<ArrayList<JSONQuestionObject>>() {
                    }.getType();
                    arrayQues = new Gson().fromJson(json1, type);
                    ListQuestionObjectStatus listQuestionObjectStatus = new Gson().fromJson(json2, ListQuestionObjectStatus.class);

                    Intent intent = new Intent(ExamActivity.this, ResultAnActivity.class);
                    intent.putExtra("exam_id", Integer.parseInt(jsonExamsObject.getExamId()));
                    intent.putExtra("arrayQuestion", new Gson().toJson(arrayQues));
                    intent.putExtra("arrayQueslog", new Gson().toJson(listQuestionObjectStatus.getArrayQueslog()));
                    intent.putExtra("socaudung", listQuestionObjectStatus.getNumberCorrect());

                    ExamActivity.this.startActivity(intent);
                    finish();
                }
            }
        }
    };


    private void deleteData_before() {
        String path = getFilesDir() + "/jsonExam/pdf_\"+ exam_id";
        File dir = new File(path);
        if (dir.exists()) {
            if (dir.isDirectory()) {
                for (File child : Objects.requireNonNull(dir.listFiles())) {
                    deleteRecursive(child);
                }
            }
            boolean delete = dir.delete();
        }
    }

    private static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : Objects.requireNonNull(fileOrDirectory.listFiles()))
                deleteRecursive(child);
        boolean delete = fileOrDirectory.delete();
    }

    private void getQuestions(String exam_id) {
        Animation anim = AnimationUtils.loadAnimation(ExamActivity.this, R.anim.translate_down_up_rela);
        car_download.setAnimation(anim);

        Call<String> call_Question = dataClient.getquestions(exam_id);
        call_Question.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.body() != null && !response.body().isEmpty()) {
                    Type type = new TypeToken<ArrayList<JSONQuestionObject>>() {
                    }.getType();
                    arrayQues = new Gson().fromJson(response.body(), type);
                    File file = new File(getFilesDir(), "jsonExam/pdf_" + exam_id);
                    if (!file.exists()) file.mkdirs();
                    if (arrayQues.size() > 0) {
                        MyDataBase.saveDataType(new TypeDataSave(GlobalHelper.data_questions + "_" +
                                exam_id, response.body()));
                        showViewLoadData(false, true);
                        deleteData_before();
                        ques_current = 0;
                        for (int i = 0; i < arrayQues.size(); i++)
                            download_Write_Pdf_Question(file.getPath(), i);
                    } else
                        Toast.makeText(ExamActivity.this, try_later, Toast.LENGTH_SHORT).show();
                } else {
                    String json = MyDataBase.loadDataType(GlobalHelper.data_questions + "_" + exam_id);
                    if (!json.isEmpty()) {
                        Type type = new TypeToken<ArrayList<JSONQuestionObject>>() {
                        }.getType();
                        arrayQues = new Gson().fromJson(json, type);
                        downLoaded();
                    } else Toast.makeText(ExamActivity.this, try_later, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                String json = MyDataBase.loadDataType(GlobalHelper.data_questions + "_" + exam_id);
                if (!json.isEmpty()) {
                    Type type = new TypeToken<ArrayList<JSONQuestionObject>>() {
                    }.getType();
                    arrayQues = new Gson().fromJson(json, type);
                    downLoaded();
                } else if (!CheckConnection.checkCon(ExamActivity.this))
                    Toast.makeText(ExamActivity.this, internet_error_1, Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(ExamActivity.this, try_later, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void download_Write_Pdf_Question(String path_PDf, int i) {
        String question_id = arrayQues.get(i).getQuestionId().trim();
        String link_ques = arrayQues.get(i).getQuestionPath().trim();
        link_ques = link_ques.substring(1);// bỏ dau / , bị thua dau /
        File file1 = new File(path_PDf, "cau" + question_id + ".pdf");
        Call<ResponseBody> call = dataClient.downloadFileFDP(link_ques);
        if (!call.isCanceled()) {
            call.enqueue(new Callback<ResponseBody>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.body() != null) {
                        FileOutputStream out;
                        try {
                            out = new FileOutputStream(file1);
                            out.write(response.body().bytes());
                            out.close();
                            ques_current++;
                            txt_percent.setText(ques_current * 100 / arrayQues.size() + "%");
                            if (ques_current == arrayQues.size()) {
                                downLoaded();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("LOI_LOI", "1 = " + t.getMessage());
                    download_Write_Pdf_Question(path_PDf, ques_current);
                }
            });
        }
    }

    public void downLoaded() {
        ArrayList<JSONExamLogObject.QuestionLog> arrayQueslog = new ArrayList<>();
        for (int i = 0; i < arrayQues.size(); i++) {
            arrayQueslog.add(new JSONExamLogObject.QuestionLog(""
                    , arrayQues.get(i).getCorrectAnswer()
                    , Integer.parseInt(studentId), Integer.parseInt(arrayQues.get(i).getQuestionId())
                    , ""));
        }
        Intent intent = new Intent(ExamActivity.this, PracticeActivity.class);
        intent.putExtra("JSON_QUESTION", new Gson().toJson(arrayQues));
        intent.putExtra("JSONExamsObject", new Gson().toJson(jsonExamsObject));
        intent.putExtra("STUDENT_ID", studentId);
        intent.putExtra("JSON_QUESTION_LOG", new Gson().toJson(arrayQueslog));
        intent.putExtra("TIME_TOTAL", Long.parseLong(jsonExamsObject.getTime())*60);
        startActivity(intent);
        finish();
    }

    @OnClick({R.id.btn_thulai, R.id.img_back})
    void onClick(View view) {
        if (view.getId() == R.id.btn_thulai) {
            if (!CheckConnection.checkCon(ExamActivity.this)) {
                showPlaceHolderNotConnect();
            } else {
                getExams();
            }
        } else if (view.getId() == R.id.img_back)
            onBackPressed();
    }

    private void showPlaceHolderNotConnect() {
        relative_notConnected.setVisibility(View.VISIBLE);
        card_toolBar.setVisibility(View.GONE);
        relative_download.setVisibility(View.GONE);
        relative_content.setVisibility(View.GONE);
        txt_notData.setVisibility(View.GONE);
    }

    private void showPlaceHolderData(boolean isExistData) {
        if (isExistData) {
            relative_notConnected.setVisibility(View.GONE);
            card_toolBar.setVisibility(View.VISIBLE);
            relative_download.setVisibility(View.GONE);
            relative_content.setVisibility(View.VISIBLE);
            avi_loading.setVisibility(View.GONE);
            recyclerViewDeThi.setVisibility(View.VISIBLE);
            txt_notData.setVisibility(View.GONE);
        } else {
            relative_notConnected.setVisibility(View.GONE);
            card_toolBar.setVisibility(View.VISIBLE);
            relative_download.setVisibility(View.GONE);
            relative_content.setVisibility(View.GONE);
            txt_notData.setVisibility(View.VISIBLE);
        }
    }

    private void showViewLoadData(boolean isDownloadExam, boolean isDownloadQuestion) {
        if (isDownloadExam) {
            relative_notConnected.setVisibility(View.GONE);
            card_toolBar.setVisibility(View.VISIBLE);
            relative_download.setVisibility(View.GONE);
            relative_content.setVisibility(View.VISIBLE);
            avi_loading.setVisibility(View.VISIBLE);
            avi_loading.show();
            recyclerViewDeThi.setVisibility(View.GONE);
            txt_notData.setVisibility(View.GONE);
        } else if (isDownloadQuestion) {
            relative_notConnected.setVisibility(View.GONE);
            card_toolBar.setVisibility(View.VISIBLE);
            relative_download.setVisibility(View.VISIBLE);
            relative_content.setVisibility(View.GONE);
            txt_notData.setVisibility(View.GONE);
        } else {
            relative_notConnected.setVisibility(View.GONE);
            card_toolBar.setVisibility(View.VISIBLE);
            relative_download.setVisibility(View.GONE);
            relative_content.setVisibility(View.GONE);
            txt_notData.setVisibility(View.VISIBLE);
        }
    }

}