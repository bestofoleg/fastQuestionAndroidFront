package com.robandboo.fq.service;

import com.robandboo.fq.dto.Question;

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

    @POST("/api/v1/question/saveQuestion")
    Call<Question> saveQuestion(@Body Question question);

    @Multipart
    @POST("/api/v1/question/save/question/file/{id}")
    Call<ResponseBody> saveFile(
            @Path("id") int id,
            @Part MultipartBody.Part file
    );
}
