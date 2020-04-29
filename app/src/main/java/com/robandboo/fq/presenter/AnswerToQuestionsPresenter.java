package com.robandboo.fq.presenter;

import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.robandboo.fq.R;
import com.robandboo.fq.chain.AnswerToQuestionChainManager;
import com.robandboo.fq.dto.Answer;
import com.robandboo.fq.dto.Question;
import com.robandboo.fq.service.AnswerService;
import com.robandboo.fq.service.NetworkSingleton;
import com.robandboo.fq.service.QuestionService;
import com.robandboo.fq.util.validation.AnswerValidation;
import com.robandboo.fq.watcher.AnswerTextEnterWatcher;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnswerToQuestionsPresenter implements ILayoutPresenter <LinearLayout>{
    private LinearLayout answerToQuestionLayout;

    private QuestionService questionService;

    private AnswerService answerService;

    private TextView questionTextView;

    private EditText answerEditText;

    private Question currentQuestion;

    private TextView questionsQuantityTextView;

    private String failureToLoadQuestionErrorMessage;

    private String failureToSendAnswerErrorMessage;

    private AnswerTextEnterWatcher answerTextEnterWatcher;

    private AnswerToQuestionChainManager answerToQuestionChainManager;

    private LinearLayout sendAnswerModeLayout;

    private LinearLayout loadAnswersModeLayout;

    public AnswerToQuestionsPresenter(LinearLayout answerToQuestionLayout, AppCompatActivity appCompatActivity) {
        this.answerToQuestionLayout = answerToQuestionLayout;
        questionService = NetworkSingleton.getInstance().getRetrofit()
                .create(QuestionService.class);
        answerService = NetworkSingleton.getInstance().getRetrofit()
                .create(AnswerService.class);
        questionTextView = answerToQuestionLayout.findViewById(R.id.questionTextView);
        answerEditText = answerToQuestionLayout.findViewById(R.id.answerTextEdit);
        questionsQuantityTextView = answerToQuestionLayout.findViewById(R.id.counterTextView);
        failureToLoadQuestionErrorMessage =
                answerToQuestionLayout.getContext().getResources()
                        .getString(R.string.failureToLoadQuestionErrorMessage);
        failureToSendAnswerErrorMessage =
                answerToQuestionLayout.getContext().getResources()
                        .getString(R.string.failureToSendAnswerErrorMessage);
        currentQuestion = null;
        answerToQuestionChainManager =
                new AnswerToQuestionChainManager(this);
        answerTextEnterWatcher =
                new AnswerTextEnterWatcher(answerEditText, answerToQuestionChainManager);
        answerEditText.addTextChangedListener(answerTextEnterWatcher);
        sendAnswerModeLayout = answerToQuestionLayout.findViewById(R.id.answerInputLayout);
        loadAnswersModeLayout = answerToQuestionLayout.findViewById(R.id.usersAnswersLayout);
        answerTextEnterWatcher.setAppCompatActivity(appCompatActivity);
    }

    @Override
    public void focus() {
        answerEditText.requestFocus();
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
                answerTextEnterWatcher.setQuestion(resultQuestion);
            }

            @Override
            public void onFailure(Call<Question> call, Throwable t) {
                resultQuestion.setText(failureToLoadQuestionErrorMessage);
                questionTextView.setText(failureToLoadQuestionErrorMessage);
                questionTextView.setTextColor(Color.RED);
                t.printStackTrace();
            }
        });
        return resultQuestion;
    }

    public void sendAnswer(AnswerValidation answerValidation) {
        Answer answer = new Answer();
        answer.setQuestion(currentQuestion);
        answer.setText(answerEditText.getText().toString());
        answerValidation.setDataForValidation(answerEditText.getText().toString());
        answerService.saveAnswer(answer).enqueue(new Callback<Answer>() {
            @Override
            public void onResponse(Call<Answer> call, Response<Answer> response) {
            }

            @Override
            public void onFailure(Call<Answer> call, Throwable t) {
                Toast.makeText(
                        answerToQuestionLayout.getContext(),
                        failureToSendAnswerErrorMessage,
                        Toast.LENGTH_SHORT
                ).show();
                t.printStackTrace();
            }
        });
    }

    @Override
    public void setLayoutVisibility(boolean isVisible) {
        if (isVisible) {
            answerToQuestionLayout.setVisibility(View.VISIBLE);
        } else {
            answerToQuestionLayout.setVisibility(View.GONE);
        }
    }

    public void setInputAnswerLayoutVisibility(boolean visibility) {
        if (visibility) {
            sendAnswerModeLayout.setVisibility(View.VISIBLE);
        } else {
            sendAnswerModeLayout.setVisibility(View.GONE);
        }
    }

    public void setLoadAnswersLayoutVisibility(boolean visibility) {
        if (visibility) {
            loadAnswersModeLayout.setVisibility(View.VISIBLE);
        } else {
            loadAnswersModeLayout.setVisibility(View.GONE);
        }
    }

    public void clearAnswerTextEdit() {
        answerEditText.getText().clear();
    }

    public void setQuestionNumber(int number) {
        String labelTemplate = answerToQuestionLayout
                .getResources()
                .getString(
                        R.string.counterTextView,
                        number
                );
        questionsQuantityTextView.setText(labelTemplate);
    }

    @Override
    public LinearLayout getRootLayout() {
        return answerToQuestionLayout;
    }
}
