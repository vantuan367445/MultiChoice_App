package com.example.multichoice_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JSONExamLogObject {

@SerializedName("exam_log")
@Expose
private ExamLog examLog;
@SerializedName("question_log")
@Expose
private List<QuestionLog> questionLog = null;

public ExamLog getExamLog() {
return examLog;
}

public void setExamLog(ExamLog examLog) {
this.examLog = examLog;
}

public List<QuestionLog> getQuestionLog() {
return questionLog;
}

public void setQuestionLog(List<QuestionLog> questionLog) {
this.questionLog = questionLog;
}

    public static class ExamLog {

        @SerializedName("date_time")
        @Expose
        private String dateTime;
        @SerializedName("total_score")
        @Expose
        private String totalScore;
        @SerializedName("exam_id")
        @Expose
        private Integer examId;
        @SerializedName("student_id")
        @Expose
        private Integer studentId;

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getTotalScore() {
            return totalScore;
        }

        public void setTotalScore(String totalScore) {
            this.totalScore = totalScore;
        }

        public Integer getExamId() {
            return examId;
        }

        public void setExamId(Integer examId) {
            this.examId = examId;
        }

        public Integer getStudentId() {
            return studentId;
        }

        public void setStudentId(Integer studentId) {
            this.studentId = studentId;
        }

    }
    public static class QuestionLog {

        @SerializedName("choosen_answer")
        @Expose
        private String choosenAnswer;
        @SerializedName("correct_answer")
        @Expose
        private String correctAnswer;
        @SerializedName("student_id")
        @Expose
        private Integer studentId;
        @SerializedName("question_id")
        @Expose
        private Integer questionId;
        @SerializedName("date_time")
        @Expose
        private String dateTime;

        public QuestionLog(String choosenAnswer, String correctAnswer, int student_id, int questionId, String dateTime) {
            this.choosenAnswer = choosenAnswer;
            this.correctAnswer = correctAnswer;
            this.studentId = student_id;
            this.questionId = questionId;
            this.dateTime = dateTime;
        }

        public String getChoosenAnswer() {
            return choosenAnswer;
        }

        public void setChoosenAnswer(String choosenAnswer) {
            this.choosenAnswer = choosenAnswer;
        }

        public String getCorrectAnswer() {
            return correctAnswer;
        }

        public void setCorrectAnswer(String correctAnswer) {
            this.correctAnswer = correctAnswer;
        }

        public Integer getStudentId() {
            return studentId;
        }

        public void setStudentId(Integer studentId) {
            this.studentId = studentId;
        }

        public Integer getQuestionId() {
            return questionId;
        }

        public void setQuestionId(Integer questionId) {
            this.questionId = questionId;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

    }
}
