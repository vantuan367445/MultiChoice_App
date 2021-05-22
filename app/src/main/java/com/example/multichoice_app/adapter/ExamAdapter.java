package com.example.multichoice_app.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.multichoice_app.R;
import com.example.multichoice_app.listener.IntegerCallback;
import com.example.multichoice_app.model.JSONExamsObject;


import java.util.ArrayList;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ViewHolder> {

    private ArrayList<JSONExamsObject> arrayDeThi;
    private final Activity content;
    private final IntegerCallback itemCallback;
    private final int themeID;


    public ExamAdapter(ArrayList<JSONExamsObject> jsonExamsObjects, Activity content, IntegerCallback itemCallback,int themeID) {
        this.arrayDeThi = jsonExamsObjects;
        this.content = content;
        this.itemCallback = itemCallback;
        this.themeID = themeID;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dethi, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        JSONExamsObject modelDeThi = arrayDeThi.get(i);
        viewHolder.txt_nameDeThi.setText(modelDeThi.getExamName() + "");
        viewHolder.txt_so_cau_hoiDeThi.setText(modelDeThi.getNumberOfQuestions() + " " + viewHolder.sentence);
        viewHolder.txt_time_lam_baiDeThi.setText(modelDeThi.getTime() + " " + viewHolder.minute);
        viewHolder.line_start.setOnClickListener(v ->{
            viewHolder.line1.setBackground(themeID == 0 ? viewHolder.bg_button_red_2 : viewHolder.bg_button_white_2);
            itemCallback.execute(i);
        });
    }

    @Override
    public int getItemCount() {
        return arrayDeThi == null ? 0 : arrayDeThi.size();
    }

    @SuppressLint("NonConstantResourceId")
    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_nameDeThi)
        TextView txt_nameDeThi;
        @BindView(R.id.txt_timelambaiDeThi)
        TextView txt_time_lam_baiDeThi;
        @BindView(R.id.btn_lambaiThi)
        TextView btn_lam_baiThi;
        @BindView(R.id.txt_socauhoiDeThi)
        TextView txt_so_cau_hoiDeThi;
        @BindView(R.id.line_start)
        LinearLayout line_start;

        @BindView(R.id.line1)
        public LinearLayout line1;

        @BindString(R.string.minute)
        String minute;
        @BindString(R.string.sentence)
        String sentence;

        @BindDrawable(R.drawable.bg_button_white_2)
        Drawable bg_button_white_2;
        @BindDrawable(R.drawable.bg_button_red_2)
        Drawable bg_button_red_2;

        ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            Animation animation = AnimationUtils.loadAnimation(content, R.anim.translate_up_down);
            itemView.startAnimation(animation);
        }
    }


    public void setNewData(ArrayList<JSONExamsObject> list) {
        this.arrayDeThi = list;
        notifyDataSetChanged();
    }


}
