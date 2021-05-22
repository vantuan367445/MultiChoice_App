package com.example.multichoice_app.model;

import java.util.ArrayList;

public class ListQuestionObjectStatus {
    private int status = 0 ; // 0 là mới, 1 là lưu, 2 nộp bài
    private long timePractice = 0; // số giây
    private long timeTotal = 0;
    private int  numberCorrect = 0;
    private ArrayList<JSONExamLogObject.QuestionLog> arrayQueslog;

    public ListQuestionObjectStatus(int status,long timeTotal, long timePractice, int numberCorrect,
                                    ArrayList<JSONExamLogObject.QuestionLog> arrayQueslog) {
        this.status = status;
        this.timePractice = timePractice;
        this.timeTotal = timeTotal;
        this.numberCorrect = numberCorrect;
        this.arrayQueslog = arrayQueslog;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTimePractice() {
        return timePractice;
    }

    public void setTimePractice(long timePractice) {
        this.timePractice = timePractice;
    }

    public long getTimeTotal() {
        return timeTotal;
    }

    public void setTimeTotal(long timeTotal) {
        this.timeTotal = timeTotal;
    }

    public int getNumberCorrect() {
        return numberCorrect;
    }

    public void setNumberCorrect(int numberCorrect) {
        this.numberCorrect = numberCorrect;
    }

    public ArrayList<JSONExamLogObject.QuestionLog> getArrayQueslog() {
        return arrayQueslog;
    }

    public void setArrayQueslog(ArrayList<JSONExamLogObject.QuestionLog> arrayQueslog) {
        this.arrayQueslog = arrayQueslog;
    }
}
