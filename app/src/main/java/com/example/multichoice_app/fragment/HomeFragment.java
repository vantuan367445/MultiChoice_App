package com.example.multichoice_app.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.multichoice_app.R;
import com.example.multichoice_app.activity.ExamActivity;
import com.example.multichoice_app.adapter.SubjectAdapter;
import com.example.multichoice_app.common.GlobalHelper;

import com.example.multichoice_app.dbFlow.MyDataBase;

import com.example.multichoice_app.listener.IntegerCallback;
import com.example.multichoice_app.model.JSONSubjectObject;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.wang.avi.AVLoadingIndicatorView;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class HomeFragment extends BaseFragment {
    @BindView(R.id.recyclerView_subject)
    RecyclerView recyclerView_subject;
    @BindView(R.id.txt_notData)
    TextView txt_notData;
    @BindView(R.id.avi_loading)
    AVLoadingIndicatorView avi_loading;

    private ArrayList<JSONSubjectObject> listSubject;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("CHECK_DATA","onCreateView");
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        String json = MyDataBase.loadDataType(GlobalHelper.data_subject);
        if (!json.isEmpty())
            initUI(json);
    }

    private void initUI(String json) {
        if (getActivity() == null)
            return;
        Type type = new TypeToken<ArrayList<JSONSubjectObject>>() {
        }.getType();

        try {
            listSubject = new Gson().fromJson(json, type);
        } catch (JsonSyntaxException ex) {
            listSubject = new ArrayList<>();
        }

        SubjectAdapter adapter = new SubjectAdapter(getActivity(), listSubject, itemCallback);
        recyclerView_subject.setHasFixedSize(true);
        recyclerView_subject.setAdapter(adapter);
    }

    private final IntegerCallback itemCallback = value -> {
        if (getActivity() == null)
            return;
        Intent intent = new Intent(getActivity(), ExamActivity.class);
        intent.putExtra("SUBJECT_CODE", listSubject.get(value).getSubjectId());
        getActivity().startActivity(intent);
    };
}