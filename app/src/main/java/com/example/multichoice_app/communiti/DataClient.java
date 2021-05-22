package com.example.multichoice_app.communiti;


import com.example.multichoice_app.model.JSONExamLogObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DataClient {
    // muốn gửi tham số thì gọi @Field()
    // muốn POST dữ liệu dạng chuỗi thì dùng FormUrlEncoded

    @GET("getSubjects.php")
    Call<String> getSubject();

    @FormUrlEncoded
    @POST("getExams.php")
    Call<String> getExams(@Field("subject_id") String subject_id);

    @FormUrlEncoded
    @POST("getQuestions.php")
    Call<String> getquestions(@Field("exam_id") String exam_Id);

    @GET("{param}")
    Call<ResponseBody> downloadFileFDP(@Path(value = "param", encoded = true) String param);

    @FormUrlEncoded
    @POST("postStudentInfo.php")
    Call<String> requesrStudent(@Field("name") String student_name
            , @Field("email") String student_email);


    //    @POST("postExamlog_TUAN.php")
    //    Call<String> sendExamlog(@Body JSONExamLogObject.ExamLog examLog);
    //    @POST("postQuestionlog_TUAN.php")
    //    Call<String> sendQueslog(@Body ArrayList<JSONExamLogObject.QuestionLog> arrayQueslog );

        @POST("postStudentResult.php")
        Call<String> sendExamQues_log(@Body JSONExamLogObject examLogObject);
}
