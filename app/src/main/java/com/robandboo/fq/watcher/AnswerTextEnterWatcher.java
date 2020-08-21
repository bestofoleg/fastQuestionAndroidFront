package com.robandboo.fq.watcher;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.robandboo.fq.MainActivity;
import com.robandboo.fq.R;
import com.robandboo.fq.chain.AnswerToQuestionChainManager;
import com.robandboo.fq.dto.Answer;
import com.robandboo.fq.dto.Question;
import com.robandboo.fq.service.AnswerService;
import com.robandboo.fq.service.NetworkSingleton;
import com.robandboo.fq.util.validation.AnswerValidation;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnswerTextEnterWatcher implements TextWatcher {
    private AnswerService answerService;

    private Question question;

    private EditText editText;

    private AnswerToQuestionChainManager answerToQuestionChainManager;

    private String failureToSendAnswerErrorMessage;

    private AppCompatActivity appCompatActivity;

    @Getter
    @Setter
    private boolean skipAnswerSending;

    public AnswerTextEnterWatcher(
            EditText editText,
            AnswerToQuestionChainManager answerToQuestionChainManager
    ) {
        this.answerToQuestionChainManager = answerToQuestionChainManager;
        this.answerService = NetworkSingleton.getInstance()
                .getRetrofit()
                .create(AnswerService.class);
        this.editText = editText;
        failureToSendAnswerErrorMessage =
                editText.getContext().getResources()
                        .getString(R.string.failureToSendAnswerErrorMessage);
        skipAnswerSending = false;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }

    public void setAppCompatActivity(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    @Override
    public void beforeTextChanged(CharSequence text, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence text, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable text) {
        if (text.toString().contains("\n")) {
            skipAnswerSending = true;
            editText.setText(text.toString().replaceAll("\n", ""));
            Answer answer = new Answer();
            answer.setQuestion(question);
            answer.setText(editText.getText().toString());
            AnswerValidation validation = new AnswerValidation(appCompatActivity);
            validation.setDataForValidation(answer.getText());
            if (validation.validate()) {
                answerService.saveAnswer(answer).enqueue(new Callback<Answer>() {
                    @Override
                    public void onResponse(Call<Answer> call, Response<Answer> response) {
                        answerService.getAnswerByQuestionId(question.getId())
                                .enqueue(new Callback<List<Answer>>() {
                                    @Override
                                    public void onResponse(Call<List<Answer>> call, Response<List<Answer>> response) {
                                        answerToQuestionChainManager.startLoadAnswersMode(response.body());
                                        MainActivity.hideKeyboard(MainActivity.MAIN_ACTIVITY_STATIC_LINK);
                                    }

                                    @Override
                                    public void onFailure(Call<List<Answer>> call, Throwable t) {
                                        Toast.makeText(
                                                AnswerTextEnterWatcher.this.editText.getContext(),
                                                failureToSendAnswerErrorMessage,
                                                Toast.LENGTH_SHORT
                                        ).show();
                                    }
                                });
                    }

                    @Override
                    public void onFailure(Call<Answer> call, Throwable t) {
                        Toast.makeText(
                                AnswerTextEnterWatcher.this.editText.getContext(),
                                failureToSendAnswerErrorMessage,
                                Toast.LENGTH_SHORT
                        ).show();
                        t.printStackTrace();
                    }
                });
            }
        }
    }
}
