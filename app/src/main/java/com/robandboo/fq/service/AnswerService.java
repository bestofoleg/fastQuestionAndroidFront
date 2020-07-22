package com.robandboo.fq.service;

import com.robandboo.fq.dto.Answer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AnswerService {
    @POST("/api/v1/answer/saveAnswer")
    Call<Answer> saveAnswer(@Body Answer answer);

    @GET("/api/v1/answer/getAnswerByQuestionId/{id}")
    Call<List<Answer>> getAnswerByQuestionId(@Path("id") int id);

    @POST("/api/v1/answer/save/file/vote/{id}")
    Call<Void> saveVote(@Path("id") Long id);
}
