package com.robandboo.fq.service;

import com.robandboo.fq.dto.Question;
import com.robandboo.fq.dto.QuestionFile;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface QuestionService {
    @GET("/api/v1/question/getRandomQuestion")
    Call<Question> getRandomQuestion();

    @GET("/api/v1/question/get/question/{id}")
    Call<Question> getQuestionById(@Path("id") int id);

    @POST("/api/v1/question/saveQuestion")
    Call<Question> saveQuestion(@Body Question question);

    @Multipart
    @POST("/api/v1/question/save/question/file/{id}")
    Call<ResponseBody> saveFile(
            @Path("id") int id,
            @Part MultipartBody.Part file
    );

    @GET("/api/v1/question/get/file/{id}")
    Call<List<QuestionFile>> loadFile(@Path("id") int id);

    @GET("/api/v1/question/get/file/{id}")
    Call<List<QuestionFile>> getFileByQuestionId(@Path("id") int id);
}
