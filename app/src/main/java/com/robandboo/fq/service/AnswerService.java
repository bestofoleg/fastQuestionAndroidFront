package com.robandboo.fq.service;

import com.robandboo.fq.dto.Answer;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AnswerService {
    @POST("/api/v1/answer/saveAnswer")
    Call<Answer> saveAnswer(@Body Answer answer);
}
