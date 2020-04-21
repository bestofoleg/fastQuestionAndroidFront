package com.robandboo.fq.presenter;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robandboo.fq.R;
import com.robandboo.fq.dto.Answer;
import com.robandboo.fq.dto.Question;
import com.robandboo.fq.service.AnswerService;
import com.robandboo.fq.service.NetworkSingleton;
import com.robandboo.fq.service.QuestionService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnswerToQuestionsPresenter {
    private static final String FAILURE_TO_LOAD_QUESTION_ERROR_MESSAGE = "Failure to load question...";

    private static final String FAILURE_TO_SEND_ANSWER_ERROR_MESSAGE = "Failure to send answer...";

    private LinearLayout askToQuestionLayout;

    private QuestionService questionService;

    private AnswerService answerService;

    private TextView questionTextView;

    private EditText answerEditText;

    private Question currentQuestion;

    public AnswerToQuestionsPresenter(LinearLayout askToQuestionLayout) {
        this.askToQuestionLayout = askToQuestionLayout;
        questionService = NetworkSingleton.getInstance().getRetrofit()
                .create(QuestionService.class);
        answerService = NetworkSingleton.getInstance().getRetrofit()
                .create(AnswerService.class);
        questionTextView = askToQuestionLayout.findViewById(R.id.questionTextView);
        answerEditText = askToQuestionLayout.findViewById(R.id.answerTextEdit);
        currentQuestion = null;
    }

    public Question loadRandomQuestion() {
        final Question resultQuestion = new Question();
        questionService.getRandomQuestion().enqueue(new Callback<Question>() {
            @Override
            public void onResponse(Call<Question> call, Response<Question> response) {
                resultQuestion.setId(response.body().getId());
                resultQuestion.setText(response.body().getText());
                resultQuestion.setAnswers(response.body().getAnswers());
                questionTextView.setText(resultQuestion.getText());
                currentQuestion = resultQuestion;
            }

            @Override
            public void onFailure(Call<Question> call, Throwable t) {
                resultQuestion.setText(FAILURE_TO_LOAD_QUESTION_ERROR_MESSAGE);
                questionTextView.setText(FAILURE_TO_LOAD_QUESTION_ERROR_MESSAGE);
                t.printStackTrace();
            }
        });
        return resultQuestion;
    }

    public void sendAnswer() {
        Answer answer = new Answer();
        answer.setQuestion(currentQuestion);
        answer.setText(answerEditText.getText().toString());
        answerService.saveAnswer(answer).enqueue(new Callback<Answer>() {
            @Override
            public void onResponse(Call<Answer> call, Response<Answer> response) {
            }

            @Override
            public void onFailure(Call<Answer> call, Throwable t) {
                answerEditText.setText(FAILURE_TO_SEND_ANSWER_ERROR_MESSAGE);
                t.printStackTrace();
            }
        });
    }

    public void setLayoutVisibility(boolean isVisible) {
        if (isVisible) {
            askToQuestionLayout.setVisibility(View.VISIBLE);
        } else {
            askToQuestionLayout.setVisibility(View.GONE);
        }
    }

    public void clearAnswerTextEdit() {
        answerEditText.getText().clear();
    }
}
