package com.robandboo.fq.callback;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.robandboo.fq.chain.ChainManager;
import com.robandboo.fq.dto.Question;
import com.robandboo.fq.dto.QuestionFile;
import com.robandboo.fq.service.AnswerService;
import com.robandboo.fq.util.enumeration.QuestionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Builder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Builder
public class LoadFileInAnswerToQuestionImageViewsCallback implements Callback<List<QuestionFile>> {
    private Map<String, Long> imageCodeToFileId;
    private ImageView imageView1;
    private ImageView imageView2;
    private Bitmap currentBitmap1;
    private Bitmap currentBitmap2;
    private Question currentQuestion;
    private Boolean skipValidation;
    private String voteErrorMessage;
    private LinearLayout answerToQuestionLayout;
    private ChainManager chainManager;
    private AnswerService answerService;

    public void makeVote(String imageName) {
        Long fileId = imageCodeToFileId.get(imageName);
        if (QuestionType.VOTE.isA(currentQuestion.getQuestionType())) {
            skipValidation = Boolean.TRUE;
            List<Long> fileIds = new ArrayList<>(currentQuestion.getFileIds().keySet());
            RollChangeManagerWhenVotingCallback rollChangeManagerWhenVotingCallback = RollChangeManagerWhenVotingCallback.builder()
                    .voteErrorMessage(voteErrorMessage)
                    .answerToQuestionLayout(answerToQuestionLayout)
                    .chainManager(chainManager).build();
            answerService.saveVote(fileId).enqueue(rollChangeManagerWhenVotingCallback);
        } else {
            skipValidation = Boolean.FALSE;
        }
    }

    @Override
    public void onResponse(Call<List<QuestionFile>> call,
                           Response<List<QuestionFile>> response) {
        imageCodeToFileId = new HashMap<>();
        List<QuestionFile> questionFiles = response.body();
        if (questionFiles != null && !questionFiles.isEmpty()) {
            imageView1.setVisibility(View.VISIBLE);

            byte[] bytes1 = Base64.decode(
                    questionFiles.get(0).getData().getBytes(),
                    Base64.DEFAULT
            );

            Bitmap bitmap1 = BitmapFactory.decodeByteArray(
                    bytes1, 0, bytes1.length
            );
            currentBitmap1 = bitmap1;
            Glide
                    .with(imageView1)
                    .load(bitmap1)
                    .into(imageView1);
            imageCodeToFileId.put("image1", questionFiles.get(0).getId());
            if (questionFiles.size() > 1) {
                imageView2.setVisibility(View.VISIBLE);
                byte[] bytes2 = Base64.decode(
                        questionFiles.get(1).getData().getBytes(),
                        Base64.DEFAULT
                );

                Bitmap bitmap2 = BitmapFactory.decodeByteArray(
                        bytes2, 0, bytes2.length
                );
                currentBitmap2 = bitmap2;
                Glide
                        .with(imageView2)
                        .load(bitmap2)
                        .into(imageView2);
                imageCodeToFileId.put("image2", questionFiles.get(1).getId());
            } else {
                imageView2.setVisibility(View.GONE);
                currentBitmap2 = null;
            }
        } else {
            imageView1.setVisibility(View.GONE);
            imageView2.setVisibility(View.GONE);
            currentBitmap1 = null;
            currentBitmap2 = null;
        }
    }

    @Override
    public void onFailure(Call<List<QuestionFile>> call, Throwable t) {
        t.printStackTrace();
    }
}
