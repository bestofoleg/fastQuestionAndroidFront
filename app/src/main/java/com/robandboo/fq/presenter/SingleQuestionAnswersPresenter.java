package com.robandboo.fq.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.robandboo.fq.R;
import com.robandboo.fq.callback.GetAnswerByQuestionIdCallback;
import com.robandboo.fq.callback.GetQuestionByIdCallback;
import com.robandboo.fq.dto.Question;
import com.robandboo.fq.service.AnswerService;
import com.robandboo.fq.service.NetworkSingleton;
import com.robandboo.fq.service.QuestionService;
import com.robandboo.fq.util.enumeration.QuestionType;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import lombok.Setter;

public class SingleQuestionAnswersPresenter implements ILayoutPresenter<LinearLayout> {
    private LinearLayout singleQuestionLayout;

    private AnswerService answerService;

    private TextView questionTextView;

    private LinearLayout answersLayout;

    private ImageButton imageButton;

    @Setter
    private Question currentQuestion;

    private String answerPrefix;

    private String emptyAnswersDataMessage;

    private String failureToLoadAnswers;

    private ImageView imageView1;

    private Bitmap bitmap1;

    private ImageView imageView2;

    private Bitmap bitmap2;

    private TextView firstImageVotingCountTextView;

    private TextView secondImageVotingCountTextView;

    private QuestionService questionService;

    public SingleQuestionAnswersPresenter(LinearLayout singleQuestionLayout) {
        this.singleQuestionLayout = singleQuestionLayout;
        answerService = NetworkSingleton.getInstance().getRetrofit()
                .create(AnswerService.class);
        questionService = NetworkSingleton.getInstance().getRetrofit()
                .create(QuestionService.class);
        questionTextView = singleQuestionLayout.findViewById(R.id.questionTitleSingle);
        answersLayout = singleQuestionLayout.findViewById(R.id.singleAnswersList);
        imageButton = singleQuestionLayout.findViewById(R.id.updateSinglePageButton);
        imageButton.setOnClickListener((view)->
                SingleQuestionAnswersPresenter.this.updateData(currentQuestion));
        answerPrefix = singleQuestionLayout.getContext()
                .getResources().getString(R.string.answerPrefix);
        emptyAnswersDataMessage = singleQuestionLayout.getContext()
                .getResources().getString(R.string.emptyAnswersDataMessage);
        failureToLoadAnswers = singleQuestionLayout.getContext()
                .getResources().getString(R.string.failureToLoadAnswers);
        imageView1 = singleQuestionLayout.findViewById(R.id.questionImage1);
        imageView2 = singleQuestionLayout.findViewById(R.id.questionImage2);
        firstImageVotingCountTextView = singleQuestionLayout.findViewById(R.id.rate1);
        secondImageVotingCountTextView = singleQuestionLayout.findViewById(R.id.rate2);
    }

    @Override
    public void focus() {
        singleQuestionLayout.requestFocus();
    }

    public void viewAllImagesForQuestion(List<File> imageFiles) {
        bitmap1 = null;
        bitmap2 = null;
        imageView1.setVisibility(View.GONE);
        imageView2.setVisibility(View.GONE);
        if (imageFiles != null && !imageFiles.isEmpty()) {
            if (imageFiles.get(0) != null) {
                Glide
                        .with(imageView1)
                        .load(imageFiles.get(0))
                        .into(imageView1);
                bitmap1 = BitmapFactory.decodeFile(imageFiles.get(0).getAbsolutePath());
                imageView1.setVisibility(View.VISIBLE);
            }
            if (imageFiles.size() > 1 && imageFiles.get(1) != null) {
                Glide
                        .with(imageView2)
                        .load(imageFiles.get(1))
                        .into(imageView2);
                bitmap2 = BitmapFactory.decodeFile(imageFiles.get(1).getAbsolutePath());
                imageView2.setVisibility(View.VISIBLE);
            }
        }
    }

    public List<Bitmap> getBitmaps() {
        return Arrays.asList(bitmap1, bitmap2);
    }

    private void setVisibilityForVotingRateTextViews(boolean isVisible) {
        if (isVisible) {
            firstImageVotingCountTextView.setVisibility(View.VISIBLE);
            secondImageVotingCountTextView.setVisibility(View.VISIBLE);
        } else {
            firstImageVotingCountTextView.setVisibility(View.GONE);
            secondImageVotingCountTextView.setVisibility(View.GONE);
        }
    }

    public void updateData(final Question question) {
        if (!QuestionType.VOTE.isA(question.getQuestionType())) {
            setVisibilityForVotingRateTextViews(false);
            GetAnswerByQuestionIdCallback getAnswerByQuestionIdCallback =
                    GetAnswerByQuestionIdCallback.builder()
                    .answerPrefix(answerPrefix)
                    .currentQuestion(currentQuestion)
                    .question(question)
                    .questionTextView(questionTextView)
                    .singleQuestionLayout(singleQuestionLayout)
                    .answersLayout(answersLayout)
                    .emptyAnswersDataMessage(emptyAnswersDataMessage)
                    .failureToLoadAnswers(failureToLoadAnswers).build();
            answerService.getAnswerByQuestionId(question.getId())
                    .enqueue(getAnswerByQuestionIdCallback);
        } else {
            if (question.getId() != null) {
                setVisibilityForVotingRateTextViews(true);
                GetQuestionByIdCallback getQuestionByIdCallback = GetQuestionByIdCallback.builder()
                        .firstImageVotingCountTextView(firstImageVotingCountTextView)
                        .secondImageVotingCountTextView(secondImageVotingCountTextView).build();
                questionService.getQuestionById(question.getId()).enqueue(getQuestionByIdCallback);
            }
        }
    }

    @Override
    public LinearLayout getRootLayout() {
        return singleQuestionLayout;
    }

    @Override
    public void setLayoutVisibility(boolean visibility) {
        if (visibility) {
            singleQuestionLayout.setVisibility(View.VISIBLE);
        } else {
            singleQuestionLayout.setVisibility(View.GONE);
        }
    }
}
