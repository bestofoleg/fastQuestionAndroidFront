package com.robandboo.fq.presenter;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robandboo.fq.R;
import com.robandboo.fq.dto.Answer;
import com.robandboo.fq.dto.Question;
import com.robandboo.fq.listener.NextStateClickListener;
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

    private Button nextButton;

    private Button updateButton;

    public SingleQuestionAnswersPresenter(LinearLayout singleQuestionLayout) {
        this.singleQuestionLayout = singleQuestionLayout;
        answerService = NetworkSingleton.getInstance().getRetrofit()
                .create(AnswerService.class);
        questionTextView = singleQuestionLayout.findViewById(R.id.questionTitleSingle);
        answersLayout = singleQuestionLayout.findViewById(R.id.singleAnswersList);
        nextButton = singleQuestionLayout.findViewById(R.id.singleQuestionPageNextButton);
        updateButton = singleQuestionLayout.findViewById(R.id.updateSinglePageButton);
    }

    public Button getNextButton() {
        return nextButton;
    }

    public Button getUpdateButton() {
        return updateButton;
    }

    public void setVisibility(boolean isVisible) {
        if (isVisible) {
            singleQuestionLayout.setVisibility(View.VISIBLE);
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
                for (Answer answer : response.body()) {
                    TextView answerTextView = new TextView(answersLayout.getContext());
                    answerTextView.setText("- " + answer.getText());
                    answerTextView.setTextSize(14);
                    answersLayout.addView(answerTextView);
                }
            }

            @Override
            public void onFailure(Call<List<Answer>> call, Throwable t) {
                //TODO: implement it!
            }
        });
    }
}
