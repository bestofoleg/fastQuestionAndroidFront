package com.robandboo.fq.callback;

import android.graphics.Typeface;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.robandboo.fq.MainActivity;
import com.robandboo.fq.chain.ChainManager;
import com.robandboo.fq.dto.Question;
import com.robandboo.fq.dto.QuestionFile;
import com.robandboo.fq.service.AnswerService;
import com.robandboo.fq.service.QuestionService;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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
                    vote1.setText(String.valueOf(fileIds.get(image1Id)));
                    Long image2Id = imageCodeToFileId.get("image2");
                    vote2.setText(String.valueOf(fileIds.get(image2Id)));
                    if (Objects.nonNull(image1Id) && Objects.nonNull(image2Id)) {
                        if (image1Id.compareTo(image2Id) > 0) {
                            vote1.setTypeface(Typeface.DEFAULT_BOLD);
                        }
                        if (image1Id.compareTo(image2Id) < 0) {
                            vote2.setTypeface(Typeface.DEFAULT_BOLD);
                        }
                        if (image1Id.equals(image2Id)) {
                            vote1.setTypeface(Typeface.DEFAULT);
                            vote2.setTypeface(Typeface.DEFAULT);
                        }
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
