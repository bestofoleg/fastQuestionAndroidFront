package com.robandboo.fq.presenter;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.robandboo.fq.R;
import com.robandboo.fq.dto.Answer;
import com.robandboo.fq.dto.Question;
import com.robandboo.fq.service.AnswerService;
import com.robandboo.fq.service.NetworkSingleton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingleQuestionAnswersPresenter {
    private LinearLayout singleQuestionLayout;

    private AnswerService answerService;

    private TextView questionTextView;

    private LinearLayout answersLayout;

    private ImageButton imageButton;

    private Question currentQuestion;

    private String answerPrefix;

    private String emptyAnswersDataMessage;

    private String failureToLoadAnswers;

    private Animation animation;

    public SingleQuestionAnswersPresenter(LinearLayout singleQuestionLayout) {
        this.singleQuestionLayout = singleQuestionLayout;
        answerService = NetworkSingleton.getInstance().getRetrofit()
                .create(AnswerService.class);
        questionTextView = singleQuestionLayout.findViewById(R.id.questionTitleSingle);
        answersLayout = singleQuestionLayout.findViewById(R.id.singleAnswersList);
        imageButton = singleQuestionLayout.findViewById(R.id.updateSinglePageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleQuestionAnswersPresenter.this.updateData(currentQuestion);
            }
        });
        answerPrefix = singleQuestionLayout.getContext()
                .getResources().getString(R.string.answerPrefix);
        emptyAnswersDataMessage = singleQuestionLayout.getContext()
                .getResources().getString(R.string.emptyAnswersDataMessage);
        failureToLoadAnswers = singleQuestionLayout.getContext()
                .getResources().getString(R.string.failureToLoadAnswers);
        animation = AnimationUtils.loadAnimation(
                singleQuestionLayout.getContext(),
                R.anim.single_question_page_anim
        );
    }

    public void setVisibility(boolean isVisible) {
        if (isVisible) {
            singleQuestionLayout.setVisibility(View.VISIBLE);
            singleQuestionLayout.startAnimation(animation);
        } else {
            singleQuestionLayout.setVisibility(View.GONE);
        }
    }

    public void updateData(final Question question) {
        answerService.getAnswerByQuestionId(question.getId())
                .enqueue(new Callback<List<Answer>>() {
            @Override
            public void onResponse(Call<List<Answer>> call, Response<List<Answer>> response) {
                questionTextView.setText(question.getText());
                answersLayout.removeAllViews();
                if (response.body().isEmpty()) {
                    TextView answerTextView = new TextView(answersLayout.getContext());
                    answerTextView.setText(emptyAnswersDataMessage);
                    answerTextView.setTextSize(14);
                    answersLayout.addView(answerTextView);
                    currentQuestion = question;
                } else {
                    for (Answer answer : response.body()) {
                        TextView answerTextView = new TextView(answersLayout.getContext());
                        answerTextView.setText(answerPrefix + answer.getText());
                        answerTextView.setTextSize(14);
                        answersLayout.addView(answerTextView);
                        currentQuestion = question;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Answer>> call, Throwable t) {
                Toast.makeText(
                        singleQuestionLayout.getContext(),
                        failureToLoadAnswers,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
