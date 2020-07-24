package com.robandboo.fq.callback;

import android.widget.TextView;

import com.robandboo.fq.dto.Question;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Builder
public class GetQuestionByIdCallback implements Callback<Question> {
    private TextView firstImageVotingCountTextView;
    private TextView secondImageVotingCountTextView;

    @Override
    public void onResponse(Call<Question> call, Response<Question> response) {
        Question updatedQuestion = response.body();
        if (updatedQuestion != null &&
                updatedQuestion.getFileIds() != null &&
                updatedQuestion.getFileIds().size() > 1) {
            List<Long> votes = new ArrayList<>(updatedQuestion.getFileIds().values());
            firstImageVotingCountTextView
                    .setText(votes.get(0).toString());
            secondImageVotingCountTextView
                    .setText(votes.get(1).toString());
        } else {
            firstImageVotingCountTextView
                    .setText("0");
            secondImageVotingCountTextView
                    .setText("0");
        }
    }

    @Override
    public void onFailure(Call<Question> call, Throwable t) {
    }
}
