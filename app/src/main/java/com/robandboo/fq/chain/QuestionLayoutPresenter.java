package com.robandboo.fq.chain;

import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.robandboo.fq.R;
import com.robandboo.fq.dto.Question;
import com.robandboo.fq.service.NetworkSingleton;
import com.robandboo.fq.service.QuestionService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionLayoutPresenter {
    private LinearLayout layout;

    private AppCompatActivity compatActivity;

    private TextView questionTextView;

    private QuestionService questionService;

    private static final String QUESTION_GET_FAILURE_MESSAGE = "Failure to get question";

    public QuestionLayoutPresenter(LinearLayout layout, AppCompatActivity compatActivity) {
        this.layout = layout;
        this.compatActivity = compatActivity;
        questionTextView = compatActivity.findViewById(R.id.questionTextView);
        questionService = NetworkSingleton.getInstance().getRetrofit().create(QuestionService.class);
    }

    public void loadQuestion() {
        NetworkSingleton.getInstance().getRetrofit().create(QuestionService.class).getRandomQuestion().enqueue(new Callback<Question>() {
            @Override
            public void onResponse(Call<Question> call, Response<Question> response) {
                questionTextView.setText(response.body().getText());
            }

            @Override
            public void onFailure(Call<Question> call, Throwable t) {
                questionTextView.setText(QUESTION_GET_FAILURE_MESSAGE);
                t.printStackTrace();
                Log.println(Log.ERROR, "question err caused by", t.getCause().getMessage());
            }
        });
    }
}
