package com.example.multichoice_app.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multichoice_app.R;
import com.example.multichoice_app.model.ModelPhieuDapAn_Naviagation;

import java.util.ArrayList;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterNavigation extends RecyclerView.Adapter<AdapterNavigation.MyViewHolder> {
    private Activity context;
    private ArrayList<ModelPhieuDapAn_Naviagation> arrayList;

    public AdapterNavigation(Activity context, ArrayList<ModelPhieuDapAn_Naviagation> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_multiple_choice_navigation,parent,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(position < getItemCount()){
            ModelPhieuDapAn_Naviagation object = arrayList.get(position);
            holder.txt_stt_cau.setText(object.getStt() + 1 +"");
            holder.txt_A.setBackground(object.isDapanA() ? holder.custom_button_chon : holder.custom_button__khong_chon);
            holder.txt_B.setBackground(object.isDapanB() ? holder.custom_button_chon : holder.custom_button__khong_chon);
            holder.txt_C.setBackground(object.isDapanC() ? holder.custom_button_chon : holder.custom_button__khong_chon);
            holder.txt_D.setBackground(object.isDapanD() ? holder.custom_button_chon : holder.custom_button__khong_chon);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @SuppressLint("NonConstantResourceId")
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.txt_stt_cau)
        TextView txt_stt_cau;
        @BindView(R.id.txt_A)
        TextView txt_A;
        @BindView(R.id.txt_B)
        TextView txt_B;
        @BindView(R.id.txt_C)
        TextView txt_C;
        @BindView(R.id.txt_D)
        TextView txt_D;

        @BindDrawable(R.drawable.custom_button_chon)
        Drawable custom_button_chon;
        @BindDrawable(R.drawable.custom_button__khong_chon)
        Drawable custom_button__khong_chon;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setNewList(ArrayList<ModelPhieuDapAn_Naviagation> arrayList){
        this.arrayList = arrayList;
        notifyDataSetChanged();

    }
}
