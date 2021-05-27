package com.example.multichoice_app.activity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.widget.TextView;

import com.example.multichoice_app.R;
import com.example.multichoice_app.adapter.AdapterMyFragment;
import com.example.multichoice_app.adapter.AdapterNavigation;
import com.example.multichoice_app.common.GlobalHelper;
import com.example.multichoice_app.common.PreferenceHelper;
import com.example.multichoice_app.utils.APIUtills;
import com.example.multichoice_app.utils.DataClient;
import com.example.multichoice_app.dbFlow.MyDataBase;
import com.example.multichoice_app.dbFlow.TypeDataSave;
import com.example.multichoice_app.fragment.MyFragment;
import com.example.multichoice_app.listener.ChoiceCallback;
import com.example.multichoice_app.listener.IntegerCallback;
import com.example.multichoice_app.model.JSONExamLogObject;
import com.example.multichoice_app.model.JSONExamsObject;
import com.example.multichoice_app.model.JSONQuestionObject;
import com.example.multichoice_app.model.ListQuestionObjectStatus;
import com.example.multichoice_app.model.ModelPhieuDapAn_Naviagation;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint({"NonConstantResourceId", "StaticFieldLeak"})
public class PracticeActivity extends BaseActivity {
    private JSONExamsObject jsonExamsObject = null;
    private ArrayList<JSONQuestionObject> arrayQuestion;

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.sliding_tabs)
    TabLayout tabLayout;
    ArrayList<MyFragment> fragmentList;
    AdapterMyFragment adapterMyFragment;

    @BindView(R.id.toolBarMain)
    Toolbar toolbar;
    @BindView(R.id.drawerlayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.txt_lambai_made)
    TextView txt_lambai_made;
    @BindView(R.id.txt_cauhoihientai)
    TextView txt_cauhoihientai;

    @BindView(R.id.recyclerView_Navigation)
    RecyclerView recyclerView_Navigation;

    @BindString(R.string.code)
    String code;
    @BindString(R.string.score)
    String score;
    @BindString(R.string.times_up)
    String times_up;
    @BindDrawable(R.drawable.custom_button_bo_goc_yellow)
    Drawable custom_button_bo_goc_yellow;

    private int socauhoi;
    private int exam_id;
    public static int student_id;
    public static JSONExamLogObject examLogObject;
    // tính thời gian
    @BindView(R.id.count_timeText)
    TextView count_timeText;
    private CountDownTimer countDownTimer;
    long timelambai = 0;
    int phut = 0;
    int giay = 0;
    int posFragment = 0;

    //Drawable Navigation
    private ArrayList<ModelPhieuDapAn_Naviagation> listNavigation;
    private AdapterNavigation adapterNavigation;
    public static ArrayList<JSONExamLogObject.QuestionLog> arrayQueslog;
    private int soCauDung = 0;

    private boolean checkConnect;
    private boolean isRunning = false;
    private boolean setPageLimit;
    private int statusPractice = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferenceHelper = new PreferenceHelper(this, GlobalHelper.PREFERENCE_NAME_APP);
        if (preferenceHelper.getThemeValue() == 0)
            setTheme(R.style.ThemeLight);
        else setTheme(R.style.ThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lam_bai);
        ButterKnife.bind(this);
        getDataFromIntent();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        try {
            jsonExamsObject = new Gson().fromJson(intent.getStringExtra("JSONExamsObject"), JSONExamsObject.class);
            Type type = new TypeToken<ArrayList<JSONQuestionObject>>() {
            }.getType();
            arrayQuestion = new Gson().fromJson(intent.getStringExtra("JSON_QUESTION"), type);
            student_id = Integer.parseInt(intent.getStringExtra("STUDENT_ID"));
            Type type2 = new TypeToken<ArrayList<JSONExamLogObject.QuestionLog>>() {
            }.getType();
            arrayQueslog = new Gson().fromJson(intent.getStringExtra("JSON_QUESTION_LOG"), type2);
            socauhoi = arrayQuestion.size();
            exam_id = Integer.parseInt(jsonExamsObject.getExamId());
            int status = intent.getIntExtra("STATUS_PRACTICE", 0);
            int timeTotal = (int) (intent.getLongExtra("TIME_TOTAL", 0) * 1000);
            if (status > 0) timelambai = (int) (intent.getLongExtra("TIME_PRACTICE", 0) * 1000);
            else timelambai = timeTotal;


            //timelambai = Integer.parseInt(jsonExamsObject.getNumberOfQuestions()) * 60000;

            initUI();
        } catch (Exception e) {
            socauhoi = 0;
            exam_id = 0;
        }
    }

    @SuppressLint("SetTextI18n")
    private void initUI() {
        initArrayFragment();
        initCountDownTimer();
        initDrawerLayout();
        ActionBar();
    }

    @SuppressLint("SetTextI18n")
    public void initArrayFragment() {
        fragmentList = new ArrayList<>();
        genFragmentList();
        adapterMyFragment = new AdapterMyFragment(getSupportFragmentManager(), this, fragmentList);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapterMyFragment);
        tabLayout.setupWithViewPager(viewPager);
        txt_cauhoihientai.setText(posFragment + 1 + "\\" + socauhoi);
        runOnUiThread(() -> {
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onPageScrolled(int i, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    txt_cauhoihientai.setText(position + 1 + "\\" + socauhoi);
                    posFragment = position;
                    if (!setPageLimit) {
                        setPageLimit = true;
                        viewPager.setOffscreenPageLimit(fragmentList.size() - 1);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
//            if(!setPageLimit){
//                setPageLimit = true;
//                viewPager.setOffscreenPageLimit(fragmentList.size() - 1);
//            }
        });
    }


    private void genFragmentList() {
        if (arrayQuestion.size() != arrayQueslog.size())
            return;
        for (int i = 0; i < socauhoi; i++) {
            JSONQuestionObject jsonQuestionObject = arrayQuestion.get(i);
            JSONExamLogObject.QuestionLog questionLog = arrayQueslog.get(i);
            fragmentList.add((MyFragment) MyFragment.newInstance(exam_id, i
                    , new Gson().toJson(jsonQuestionObject)
                    , new Gson().toJson(questionLog)
                    , choiceCallback
            ));
        }
    }

    private final ChoiceCallback choiceCallback = new ChoiceCallback() {
        @Override
        public void execute(int pos, JSONExamLogObject.QuestionLog object) {
            arrayQueslog.set(pos, object);
            updateAnswerNavigation(pos, arrayQueslog.get(pos).getChoosenAnswer());
            if (pos < adapterMyFragment.getCount() - 1)
                new Handler(Looper.getMainLooper()).postDelayed(() -> viewPager.setCurrentItem(pos + 1), 650);
        }
    };

    private void initCountDownTimer() {
        countDownTimer = new CountDownTimer(timelambai, 1000) {
            public void onTick(long millisUntilFinished) {
                isRunning = true;
                String text;
                phut = (int) (millisUntilFinished / 60000);
                giay = (int) (millisUntilFinished % 60000 / 1000);
                text = phut + ":";
                if (giay < 10) {
                    text = text + "0" + giay;
                } else {
                    text = text + giay + "";
                }
                count_timeText.setText(text);
            }

            public void onFinish() {
                if(statusPractice == -1)
                    statusPractice = 2;// 0 là mới, 1 là lưu, 2 nộp bài
                isRunning = false;
                xuLyDialogTinDiem();
            }
        }.start();

    }

    private void initDrawerLayout() {
        listNavigation = new ArrayList<>();
        for (int i = 0; i < socauhoi; i++) {
            ModelPhieuDapAn_Naviagation dapAn = new ModelPhieuDapAn_Naviagation(i, false, false, false, false);
            if(i < arrayQueslog.size() && !arrayQueslog.get(i).getChoosenAnswer().isEmpty()){
                switch (arrayQueslog.get(i).getChoosenAnswer().trim()){
                    case "A":
                        dapAn.setDapanA(true);
                        break;
                    case "B":
                        dapAn.setDapanB(true);
                        break;
                    case "C":
                        dapAn.setDapanC(true);
                        break;
                    case "D":
                        dapAn.setDapanD(true);
                        break;
                }
            }
            listNavigation.add(dapAn);
        }
        adapterNavigation = new AdapterNavigation(this, listNavigation);
        recyclerView_Navigation.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView_Navigation.setAdapter(adapterNavigation);
    }

    @SuppressLint("RestrictedApi")
    private void ActionBar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));
    }

    @SuppressLint("SetTextI18n")
    public void xuLyDialogTinDiem() {
        if(statusPractice == 1){
            long timeRemain = phut * 60 + giay;
            ListQuestionObjectStatus status = new ListQuestionObjectStatus
                    (statusPractice, Long.parseLong(jsonExamsObject.getTime()) * 60, timeRemain, soCauDung, arrayQueslog);
            MyDataBase.saveDataType(new TypeDataSave(
                    GlobalHelper.data_questions_status + "_" + exam_id, new Gson().toJson(status)));
            finish();
        }else if(statusPractice == 2){
            if (arrayQueslog.size() == socauhoi) {
                for (int i = 0; i < socauhoi; i++) {
                    JSONExamLogObject.QuestionLog questionLog = arrayQueslog.get(i);
                    if (questionLog.getChoosenAnswer().equals(questionLog.getCorrectAnswer()))
                        soCauDung++;

                }
            }
            count_timeText.setText(times_up);
            countDownTimer.cancel();
            float d = (float) Math.round(((float) (soCauDung * 10) / (float) socauhoi) * 100) / 100;
            long timeRemain = phut * 60 + giay;
            ListQuestionObjectStatus status = new ListQuestionObjectStatus
                    (statusPractice, Long.parseLong(jsonExamsObject.getTime()) * 60, timeRemain, soCauDung, arrayQueslog);
            MyDataBase.saveDataType(new TypeDataSave(
                    GlobalHelper.data_questions_status + "_" + exam_id, new Gson().toJson(status)));
            examLogObject = new JSONExamLogObject();
            JSONExamLogObject.ExamLog examLog = new JSONExamLogObject.ExamLog();
            examLog.setExamId(exam_id);
            examLog.setTotalScore(d + "");
            examLog.setStudentId(student_id);
            examLog.setDateTime(getTime());
            examLogObject.setExamLog(examLog);
            examLogObject.setQuestionLog(arrayQueslog);
            //sendExamLog(new Gson().toJson(examLog), new Gson().toJson(arrayQueslog));
            if (checkConnect)
                sendExamLog(examLog, examLogObject); // u+?i len server
            else {
                TypeDataSave typeDataSave = new TypeDataSave(GlobalHelper.JSONExamLog_Object, new Gson().toJson(examLogObject));
                MyDataBase.saveDataType(typeDataSave);
            }
            if (!this.isFinishing()){
                Log.d("CHECK_BACK","dialogScoreIsShow1 = " + dialogScoreIsShow);
                dialogScoreIsShow = true;
                GlobalHelper.showDialogScore(this, scoreCallback, soCauDung, socauhoi);
            }

        }

    }
    private boolean dialogScoreIsShow = false;

    private final IntegerCallback scoreCallback = new IntegerCallback() {
        @Override
        public void execute(int value) {
            if (value == 1) {
                Intent intent = new Intent(PracticeActivity.this, ResultAnActivity.class);
                intent.putExtra("exam_id", exam_id);
                intent.putExtra("arrayQuestion", new Gson().toJson(arrayQuestion));
                intent.putExtra("arrayQueslog", new Gson().toJson(arrayQueslog));
                intent.putExtra("socaudung", soCauDung);
                PracticeActivity.this.startActivity(intent);
            }
            PracticeActivity.this.finish();
        }
    };

    private String getTime() {
        Calendar calendar = Calendar.getInstance();
        String strDateFormat24 = "HH:mm:ss";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf1 = new SimpleDateFormat(strDateFormat24);
        String time = sdf1.format(calendar.getTime());
        String strDateFormat = "dd/MM/yyyy";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf2 = new SimpleDateFormat(strDateFormat);
        String day = sdf2.format(calendar.getTime());
        return time + " - " + day;
    }

    private void sendExamLog(JSONExamLogObject.ExamLog jsonExamLog, JSONExamLogObject examLogObject) {
        DataClient dataClient = APIUtills.getData();
        Call<String> call3 = dataClient.sendExamQues_log(examLogObject);
        call3.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("RESULT_examLogObject", response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("LOI", t.getMessage());
            }
        });
    }

    private void updateAnswerNavigation(int cau, String choices) {
        ModelPhieuDapAn_Naviagation object = listNavigation.get(cau);
        switch (choices) {
            case "A": {
                object.setDapanA(true);
                object.setDapanB(false);
                object.setDapanC(false);
                object.setDapanD(false);
                break;
            }

            case "B": {
                object.setDapanB(true);
                object.setDapanA(false);
                object.setDapanC(false);
                object.setDapanD(false);
                break;
            }

            case "C": {
                object.setDapanC(true);
                object.setDapanA(false);
                object.setDapanB(false);
                object.setDapanD(false);
                break;
            }
            case "D": {
                object.setDapanD(true);
                object.setDapanA(false);
                object.setDapanB(false);
                object.setDapanC(false);
                break;
            }

        }

        listNavigation.set(cau, object);
        adapterNavigation.setNewList(listNavigation);
    }

    @Override
    public void onBackPressed() {
        if (isRunning)
            GlobalHelper.showDialogNopBai(PracticeActivity.this, nopBaiCallback);
        else  {
            // đóng listView
            if ((drawerLayout.isDrawerOpen(GravityCompat.START))) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            super.onBackPressed();
        }
    }

    @Override // tạo menu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mnu_done, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        GlobalHelper.showDialogNopBai(PracticeActivity.this, nopBaiCallback);
        return super.onOptionsItemSelected(item);
    }

    IntegerCallback nopBaiCallback = new IntegerCallback() {
        @Override
        public void execute(int value) {
            if (value == 0) {// nộp bài
                statusPractice = 2; // 0 là mới, 1 là lưu, 2 nộp bài
                countDownTimer.onFinish();
            } else if (value == 1) { // lưu
                statusPractice = 1;
                countDownTimer.onFinish();
            }

        }
    };
}