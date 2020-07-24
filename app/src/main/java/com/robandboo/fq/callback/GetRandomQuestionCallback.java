package com.robandboo.fq.callback;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.robandboo.fq.dto.Question;
import com.robandboo.fq.service.QuestionService;
import com.robandboo.fq.util.enumeration.QuestionType;
import com.robandboo.fq.watcher.AnswerTextEnterWatcher;

import java.util.Map;

import lombok.Builder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Builder
public class GetRandomQuestionCallback implements Callback<Question> {
    private Question resultQuestion;
    private TextView questionTextView;
    private EditText answerEditText;
    private Question currentQuestion;
    private AnswerTextEnterWatcher answerTextEnterWatcher;
    private String failureToLoadQuestionErrorMessage;
    private QuestionService questionService;
    private Map<String, Long> imageCodeToFileId;
    private ImageView imageView1;
    private ImageView imageView2;
    private Bitmap currentBitmap1;
    private Bitmap currentBitmap2;

    @Override
    public void onResponse(Call<Question> call, Response<Question> response) {
        resultQuestion.setId(response.body().getId());
        resultQuestion.setText(response.body().getText());
        resultQuestion.setAnswers(response.body().getAnswers());
        resultQuestion.setQuestionType(response.body().getQuestionType());
        resultQuestion.setFileIds(response.body().getFileIds());
        questionTextView.setText(resultQuestion.getText());
        if (resultQuestion.getQuestionType() != null &&
                QuestionType.VOTE.isA(resultQuestion.getQuestionType())) {
            answerEditText.setVisibility(View.GONE);
        } else {
            answerEditText.setVisibility(View.VISIBLE);
        }
        currentQuestion = resultQuestion;
        answerTextEnterWatcher.setQuestion(resultQuestion);
        loadImages(resultQuestion.getId());
    }

    @Override
    public void onFailure(Call<Question> call, Throwable t) {
        resultQuestion.setText(failureToLoadQuestionErrorMessage);
        questionTextView.setText(failureToLoadQuestionErrorMessage);
        questionTextView.setTextColor(Color.RED);
        t.printStackTrace();
    }

    private void loadImages(int questionId) {
        LoadFileCallback loadFileCallback = LoadFileCallback.builder()
                .imageCodeToFileId(imageCodeToFileId)
                .currentBitmap1(currentBitmap1)
                .currentBitmap2(currentBitmap2)
                .imageView1(imageView1)
                .imageView2(imageView2).build();
        questionService.loadFile(questionId).enqueue(loadFileCallback);
    }
}


