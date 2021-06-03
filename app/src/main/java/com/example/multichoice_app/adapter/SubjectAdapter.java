package com.example.multichoice_app.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.multichoice_app.R;
import com.example.multichoice_app.listener.IntegerCallback;
import com.example.multichoice_app.model.JSONSubjectObject;

import java.util.ArrayList;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.MyViewHolder> {
        private final Activity context;
        private ArrayList<JSONSubjectObject> listSubject;
        private final IntegerCallback itemCallback;

    public SubjectAdapter(Activity context, ArrayList<JSONSubjectObject> listSubbject, IntegerCallback a) {
        this.context = context;
        this.listSubject = listSubbject;
        this.itemCallback = a;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_subject,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //final JSONSubjectObject subject = listSubbject.get(position);
        //holder.txt_namesubject.setText(subject.getSubjectName());
        //Picasso.with(context).load(APIUtills.baseURL + subject.getIconUrl().trim()).error(R.drawable.ic_error24dp).into(holder.img_subject);
        switch (position){
            case 0:
                holder.img_subject.setImageDrawable(holder.ic_math);
                holder.txt_namesubject.setText(holder.math);
                break;
            case 1:
                holder.img_subject.setImageDrawable(holder.ic_physic);
                holder.txt_namesubject.setText(holder.physic);
                break;
            case 2:
                holder.img_subject.setImageDrawable(holder.ic_chemistry);
                holder.txt_namesubject.setText(holder.chemistry);
                break;
            case 3:
                holder.img_subject.setImageDrawable(holder.ic_english);
                holder.txt_namesubject.setText(holder.english_sub);
                break;
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemCallback.execute(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listSubject != null ? listSubject.size() : 0;
    }

    @SuppressLint("NonConstantResourceId")
    public class MyViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.img_subject)
        ImageView img_subject;
        @BindView(R.id.txt_namesubject)
        TextView txt_namesubject;

        @BindDrawable(R.drawable.ic_math)
        Drawable ic_math;
        @BindDrawable(R.drawable.ic_physic)
        Drawable ic_physic;
        @BindDrawable(R.drawable.ic_chemistry)
        Drawable ic_chemistry;
        @BindDrawable(R.drawable.ic_english)
        Drawable ic_english;

        @BindString(R.string.math)
        String math;
        @BindString(R.string.physic)
        String physic;
        @BindString(R.string.chemistry)
        String chemistry;
        @BindString(R.string.english_sub)
        String english_sub;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            Animation animation = AnimationUtils.loadAnimation(context,R.anim.translate_up_down);
            itemView.startAnimation(animation);
        }
    }
    public void setNewList(ArrayList<JSONSubjectObject> listSubject){
        this.listSubject = listSubject;
        notifyDataSetChanged();
    }
}
