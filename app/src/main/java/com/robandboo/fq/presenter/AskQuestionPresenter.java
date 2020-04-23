package com.robandboo.fq.presenter;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.robandboo.fq.R;
import com.robandboo.fq.dto.Question;
import com.robandboo.fq.localdata.repository.MyQuestionsLocalRepository;
import com.robandboo.fq.service.NetworkSingleton;
import com.robandboo.fq.service.QuestionService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AskQuestionPresenter {
    private final String FAILURE_TO_ASK_QUESTION_ERROR_MESSAGE = "Failure to ask question...";

    private QuestionService questionService;

    private LinearLayout askLayout;

    private EditText askQuestionEditText;

    private MyQuestionsLocalRepository questionsLocalRepository;

    public AskQuestionPresenter(LinearLayout askLayout) {
        this.askLayout = askLayout;
        askQuestionEditText = askLayout.findViewById(R.id.questionTextEdit);
        questionService = NetworkSingleton.getInstance().getRetrofit()
                .create(QuestionService.class);
        questionsLocalRepository = new MyQuestionsLocalRepository(askLayout.getContext());
    }

    public void sendQuestion() {
        final Question askedQuestion = new Question();
        askedQuestion.setText(askQuestionEditText.getText().toString());
        questionService.saveQuestion(askedQuestion).enqueue(new Callback<Question>() {
            @Override
            public void onResponse(Call<Question> call, Response<Question> response) {
                questionsLocalRepository.writeQuestion(response.body());
            }

            @Override
            public void onFailure(Call<Question> call, Throwable t) {
                //TODO: throw alert for ask question failure
                t.printStackTrace();
            }
        });
    }

    public void clearQuestionEditText() {
        askQuestionEditText.getText().clear();
    }

    public void setLayoutVisibility(boolean isVisible) {
        if (isVisible) {
            askLayout.setVisibility(View.VISIBLE);
        } else {
            askLayout.setVisibility(View.GONE);
        }
    }
}
