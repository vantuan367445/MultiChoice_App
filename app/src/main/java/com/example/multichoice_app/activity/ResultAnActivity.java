package com.example.multichoice_app.activity;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.multichoice_app.R;
import com.example.multichoice_app.adapter.AdapterDapAnFragment;
import com.example.multichoice_app.common.GlobalHelper;
import com.example.multichoice_app.common.PreferenceHelper;
import com.example.multichoice_app.fragment.DapAnFragment;
import com.example.multichoice_app.model.JSONExamLogObject;
import com.example.multichoice_app.model.JSONExamsObject;
import com.example.multichoice_app.model.JSONQuestionObject;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class ResultAnActivity extends BaseActivity {

    @BindView(R.id.toolBarChamDiem)
    Toolbar toolBar;
    @BindView(R.id.viewPagerChamDiem)
    ViewPager viewPager;
    @BindView(R.id.sliding_tabsChamDiem)
    TabLayout tabLayout;
    @BindView(R.id.txt_lambai_socauDungChamDiem)
    TextView txt_number_correct;
    @BindView(R.id.txt_cauhoihientaiChamDiem)
    TextView txt_number_current;

    @BindString(R.string.correct)
    String correct;

    ArrayList<DapAnFragment> fragmentListChamDiem;
    AdapterDapAnFragment adapterMyFragmentChamDiem;


    private ArrayList<JSONQuestionObject> arrayQuestion;
    private ArrayList<JSONExamLogObject.QuestionLog> arrayQuesLog;
    private int socaudung;
    private int socauhoi;
    private int exam_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferenceHelper = new PreferenceHelper(this, GlobalHelper.PREFERENCE_NAME_APP);
        if (preferenceHelper.getThemeValue() == 0)
            setTheme(R.style.ThemeLight);
        else setTheme(R.style.ThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_dap_an);
        ButterKnife.bind(this);

        getDataFromIntent();

        init();
        addEvents();
        ActionBar();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        try {
            Type type1 = new TypeToken<ArrayList<JSONQuestionObject>>() {
            }.getType();
            arrayQuestion = new Gson().fromJson(intent.getStringExtra("arrayQuestion"), type1);
            Type type2 = new TypeToken<ArrayList<JSONExamLogObject.QuestionLog>>() {
            }.getType();
            arrayQuesLog = new Gson().fromJson(intent.getStringExtra("arrayQueslog"), type2);
            socaudung = intent.getIntExtra("socaudung", 0);
            exam_id = intent.getIntExtra("exam_id", 0);
            socauhoi = arrayQuestion.size();

        } catch (Exception e) {
            Log.d("LOI", e.toString());
        }
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        fragmentListChamDiem = new ArrayList<>();
        genFragmentList();
        adapterMyFragmentChamDiem = new AdapterDapAnFragment(getSupportFragmentManager(), this, fragmentListChamDiem);
        viewPager.setAdapter(adapterMyFragmentChamDiem);
        tabLayout.setupWithViewPager(viewPager);
        txt_number_correct.setText(correct + " " + socaudung + "/" + socauhoi);
    }
    private void genFragmentList() {
        for(int  i = 0 ; i < socauhoi ; i ++){
            JSONQuestionObject jsonQuestionObject = arrayQuestion.get(i);
            JSONExamLogObject.QuestionLog questionLog = arrayQuesLog.get(i);
            fragmentListChamDiem.add((DapAnFragment) DapAnFragment.newInstance(exam_id,i
                    ,new Gson().toJson(questionLog)
                    ,new Gson().toJson(jsonQuestionObject)));
        }
    }
    private void addEvents() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                txt_number_current.setText(i+1+"\\"+socauhoi);
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }
    @Override // tạo menu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.mnu_new,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
    @SuppressLint("RestrictedApi")
    private void ActionBar() {
        if(getSupportActionBar() != null){
            setSupportActionBar(toolBar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

    }
}