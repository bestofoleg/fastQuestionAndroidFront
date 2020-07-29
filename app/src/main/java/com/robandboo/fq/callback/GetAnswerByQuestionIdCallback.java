package com.robandboo.fq.callback;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.robandboo.fq.MainActivity;
import com.robandboo.fq.R;
import com.robandboo.fq.dto.Answer;
import com.robandboo.fq.dto.Question;

import java.util.List;

import lombok.Builder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Builder
public class GetAnswerByQuestionIdCallback implements Callback<List<Answer>> {
    private TextView questionTextView;
    private LinearLayout answersLayout;
    private Question question;
    private String emptyAnswersDataMessage;
    private Question currentQuestion;
    private String answerPrefix;
    private LinearLayout singleQuestionLayout;
    private String failureToLoadAnswers;

    @Override
    public void onResponse(Call<List<Answer>> call, Response<List<Answer>> response) {
        questionTextView.setText(question.getText());
        answersLayout.removeAllViews();
        if (response.body() == null || response.body().isEmpty()) {
            View answer = MainActivity.MAIN_INFLATER.inflate(
                    R.layout.answer_on_single_question_layout,
                    null, false
            );
            TextView answerTextView = answer.findViewById(R.id.singleAnswerText);
            answerTextView.setText(emptyAnswersDataMessage);
            answersLayout.addView(answer);
            currentQuestion = question;
        } else {
            response.body().forEach(answer -> {
                View message = MainActivity.MAIN_INFLATER.inflate(
                        R.layout.answer_on_single_question_layout,
                        null, false
                );
                TextView messageTextView = message.findViewById(R.id.singleAnswerText);
                messageTextView.setText(answer.getText());
                answersLayout.addView(message);
                currentQuestion = question;
            });
        }
    }

    @Override
    public void onFailure(Call<List<Answer>> call, Throwable t) {
        Toast.makeText(
                singleQuestionLayout.getContext(),
                failureToLoadAnswers,
                Toast.LENGTH_SHORT).show();
    }
}
