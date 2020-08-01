package com.robandboo.fq.callback;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robandboo.fq.MainActivity;
import com.robandboo.fq.R;
import com.robandboo.fq.dto.Answer;

import java.util.List;

import lombok.Builder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Builder
public class GetAnswersForSingleMyQuestionCallback implements Callback<List<Answer>> {
    private LinearLayout answersList;

    @Override
    public void onResponse(Call<List<Answer>> call, Response<List<Answer>> response) {
        if(response.body() != null) {
            response.body().forEach(answer -> {
                View root = MainActivity.MAIN_INFLATER.inflate(
                        R.layout.answer_on_single_question_layout,
                        null, false
                ).getRootView();
                TextView answerTextView = root.findViewById(R.id.singleAnswerText);
                answerTextView.setText("- " + answer.getText());
                answersList.addView(root);
            });
        }
    }

    @Override
    public void onFailure(Call<List<Answer>> call, Throwable t) {
    }
}
