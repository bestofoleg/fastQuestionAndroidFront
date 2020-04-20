package com.robandboo.fq.service;

import com.robandboo.fq.dto.Question;

import retrofit2.Call;
import retrofit2.http.GET;

public interface QuestionService {
    @GET("/api/v1/question/getRandomQuestion")
    Call<Question> getRandomQuestion();
}
