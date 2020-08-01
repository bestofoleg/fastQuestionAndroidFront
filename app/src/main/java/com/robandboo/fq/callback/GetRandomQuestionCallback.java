package com.robandboo.fq.callback;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robandboo.fq.chain.ChainManager;
import com.robandboo.fq.dto.Question;
import com.robandboo.fq.service.AnswerService;
import com.robandboo.fq.service.QuestionService;
import com.robandboo.fq.util.enumeration.QuestionType;
import com.robandboo.fq.watcher.AnswerTextEnterWatcher;

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
    private ImageView imageView1;
    private ImageView imageView2;
    private Bitmap currentBitmap1;
    private Bitmap currentBitmap2;
    private LoadFileInAnswerToQuestionImageViewsCallback loadFileCallback;
    private Boolean skipValidation;
    private String voteErrorMessage;
    private LinearLayout answerToQuestionLayout;
    private ChainManager chainManager;
    private AnswerService answerService;

    @Override
    public void onResponse(Call<Question> call, Response<Question> response) {
        resultQuestion.setId(response.body().getId());
        resultQuestion.setText(response.body().getText());
        resultQuestion.setAnswers(response.body().getAnswers());
        resultQuestion.setQuestionType(response.body().getQuestionType());
        resultQuestion.setFileIds(response.body().getFileIds());
        questionTextView.setText(resultQuestion.getText());
        if (resultQuestion != null &&
                QuestionType.VOTE.isA(resultQuestion.getQuestionType())) {
            answerEditText.setVisibility(View.GONE);
            skipValidation = Boolean.TRUE;
        } else {
            answerEditText.setVisibility(View.VISIBLE);
            skipValidation = Boolean.FALSE;
        }
        currentQuestion.setId(resultQuestion.getId());
        currentQuestion.setFileIds(resultQuestion.getFileIds());
        currentQuestion.setFilePath1(resultQuestion.getFilePath1());
        currentQuestion.setFilePath2(resultQuestion.getFilePath2());
        currentQuestion.setText(resultQuestion.getText());
        currentQuestion.setAnswers(resultQuestion.getAnswers());
        currentQuestion.setQuestionType(resultQuestion.getQuestionType());
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

    public void makeVote(String imageName) {
        loadFileCallback.makeVote(imageName);
    }

    private void loadImages(Long questionId) {
        LoadFileInAnswerToQuestionImageViewsCallback loadFileInAnswerToQuestionImageViewsCallback = LoadFileInAnswerToQuestionImageViewsCallback.builder()
                .currentBitmap1(currentBitmap1)
                .currentBitmap2(currentBitmap2)
                .imageView1(imageView1)
                .imageView2(imageView2)
                .currentQuestion(currentQuestion)
                .skipValidation(skipValidation)
                .voteErrorMessage(voteErrorMessage)
                .answerToQuestionLayout(answerToQuestionLayout)
                .chainManager(chainManager)
                .answerService(answerService).build();
        loadFileCallback = loadFileInAnswerToQuestionImageViewsCallback;
        questionService.loadFile(questionId).enqueue(loadFileInAnswerToQuestionImageViewsCallback);
    }
}


