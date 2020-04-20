package com.robandboo.fq.service;

import com.robandboo.fq.dto.Question;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface QuestionService {
    @GET("/api/v1/question/getRandomQuestion")
    Call<Question> getRandomQuestion();

    @POST("/api/v1/question/saveQuestion")
    Call<Question> saveQuestion(@Body Question question);
}
