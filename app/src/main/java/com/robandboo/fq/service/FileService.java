package com.robandboo.fq.service;

import com.robandboo.fq.dto.QuestionFile;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FileService {
    @GET("/api/v1/question/get/file/{id}")
    Call<QuestionFile> getFileById(@Path("id") Long id);
}
