package com.robandboo.fq.callback;

import android.graphics.Typeface;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.robandboo.fq.chain.ChainManager;
import com.robandboo.fq.dto.Question;
import com.robandboo.fq.service.QuestionService;

import java.util.Map;

import lombok.Builder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Builder
public class RollChangeManagerWhenVotingCallback implements Callback<Void> {
    private LinearLayout answerToQuestionLayout;
    private ChainManager chainManager;
    private String voteErrorMessage;
    private Question currentQuestion;
    private Long bestPictureId;
    private Map<String, Long> imageCodeToFileId;
    private QuestionService questionService;
    private TextView vote1;
    private TextView vote2;

    @Override
    public void onResponse(Call<Void> call, Response<Void> response) {
        questionService.getQuestionById(currentQuestion.getId()).enqueue(new Callback<Question>() {
            @Override
            public void onResponse(Call<Question> call, Response<Question> response) {
                Question question = response.body();
                if (question != null) {
                    vote1.setVisibility(View.VISIBLE);
                    vote2.setVisibility(View.VISIBLE);
                    Map<Long, Long> fileIds = question.getFileIds();
                    Long image1Id = imageCodeToFileId.get("image1");
                    Long imageVotesCount1 = fileIds.get(image1Id);
                    vote1.setText(String.valueOf(imageVotesCount1));
                    Long image2Id = imageCodeToFileId.get("image2");
                    Long imageVotesCount2 = fileIds.get(image2Id);
                    vote2.setText(String.valueOf(imageVotesCount2));
                    if (imageVotesCount1.compareTo(imageVotesCount2) > 0) {
                        vote1.setTypeface(null, Typeface.BOLD);
                    }
                    if (imageVotesCount1.compareTo(imageVotesCount2) < 0) {
                        vote2.setTypeface(null, Typeface.BOLD);
                    }
                    if (imageVotesCount1.equals(imageVotesCount2)) {
                        vote1.setTypeface(Typeface.DEFAULT);
                        vote2.setTypeface(Typeface.DEFAULT);
                    }
                }
            }

            @Override
            public void onFailure(Call<Question> call, Throwable t) {
            }
        });
    }

    @Override
    public void onFailure(Call<Void> call, Throwable t) {
        Toast.makeText(
                answerToQuestionLayout.getContext(),
                voteErrorMessage,
                Toast.LENGTH_SHORT
        );
    }
}
