package com.robandboo.fq.callback;

import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.robandboo.fq.dto.Question;
import com.robandboo.fq.localdata.repository.MyQuestionsLocalRepository;
import com.robandboo.fq.presenter.AddImagePresenter;
import com.robandboo.fq.presenter.SingleQuestionAnswersPresenter;
import com.robandboo.fq.service.QuestionService;

import java.io.File;
import java.util.List;

import id.zelory.compressor.Compressor;
import lombok.Builder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    private Compressor fileCompressor;

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

    private void saveFile(Long questionId, File file, int fileIndex) {
        Log.i("MEMORY", "file before compress: size = " + file.length());
        file = fileCompressor.compressToFile(file);
        Log.i("MEMORY", "file after compress: size = " + file.length());
        RequestBody imageBody =
                RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData(
                "file",
                file.getName(),
                imageBody
        );
        SaveFileAndClearViewsCallback saveFileAndClearViewsCallback = SaveFileAndClearViewsCallback.builder()
                .fileIndex(fileIndex)
                .addImagePresenter(addImagePresenter).build();
        questionService.saveFile(questionId, imagePart).enqueue(saveFileAndClearViewsCallback);
    }
}