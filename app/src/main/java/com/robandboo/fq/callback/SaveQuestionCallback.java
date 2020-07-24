package com.robandboo.fq.callback;

import android.widget.LinearLayout;
import android.widget.Toast;

import com.robandboo.fq.dto.Question;
import com.robandboo.fq.localdata.repository.MyQuestionsLocalRepository;
import com.robandboo.fq.presenter.AddImagePresenter;
import com.robandboo.fq.presenter.SingleQuestionAnswersPresenter;
import com.robandboo.fq.service.QuestionService;

import java.io.File;
import java.util.List;

import lombok.Builder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Builder
public class SaveQuestionCallback implements Callback<Question> {
    private AddImagePresenter addImagePresenter;
    private MyQuestionsLocalRepository questionsLocalRepository;
    private String errorSendAskMessage;
    private LinearLayout askLayout;
    private QuestionService questionService;
    private Question askedQuestion;
    private SingleQuestionAnswersPresenter singleQuestionAnswersPresenter;

    @Override
    public void onResponse(Call<Question> call, Response<Question> response) {
        List<File> imageFiles = addImagePresenter.getImageFiles();
        Question question = response.body();

        for (int i = 0; i < imageFiles.size(); i++) {
            if (imageFiles.get(i) != null && imageFiles.get(i).exists()) {
                saveFile(response.body().getId(), imageFiles.get(i), i);
            }
        }

        if (!imageFiles.isEmpty()) {
            if (imageFiles.get(0) != null) {
                question.setFilePath1(imageFiles.get(0).getAbsolutePath());
            }
            if (imageFiles.size() > 1 && imageFiles.get(1) != null) {
                question.setFilePath2(imageFiles.get(1).getAbsolutePath());
            }
        }

        questionsLocalRepository.writeQuestion(question);
        askedQuestion.setId(response.body().getId());
        askedQuestion.setFileIds(response.body().getFileIds());
        singleQuestionAnswersPresenter.setCurrentQuestion(askedQuestion);
        singleQuestionAnswersPresenter.updateData(askedQuestion);
    }

    @Override
    public void onFailure(Call<Question> call, Throwable t) {
        Toast.makeText(
                askLayout.getContext(),
                errorSendAskMessage,
                Toast.LENGTH_SHORT
        ).show();
        t.printStackTrace();
    }

    private void saveFile(int questionId, File file, int fileIndex) {
        RequestBody imageBody =
                RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData(
                "file",
                file.getName(),
                imageBody
        );
        questionService.saveFile(questionId, imagePart).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                addImagePresenter.clearImage(fileIndex);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                addImagePresenter.clearImage(fileIndex);
                throwable.printStackTrace();
            }
        });
    }
}