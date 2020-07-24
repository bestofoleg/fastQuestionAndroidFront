package com.robandboo.fq.callback;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.robandboo.fq.dto.Question;
import com.robandboo.fq.dto.QuestionFile;
import com.robandboo.fq.service.QuestionService;
import com.robandboo.fq.util.enumeration.QuestionType;
import com.robandboo.fq.watcher.AnswerTextEnterWatcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Builder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Builder
public class GetRandomQuestionCallback implements Callback<Question> {
    private Question resultQuestion;
    private TextView questionTextView;
    private EditText answerEditText;
    private Question currentQuestion;
    private AnswerTextEnterWatcher answerTextEnterWatcher;
    private String failureToLoadQuestionErrorMessage;
    private QuestionService questionService;
    private Map<String, Long> imageCodeToFileId;
    private ImageView imageView1;
    private ImageView imageView2;
    private Bitmap currentBitmap1;
    private Bitmap currentBitmap2;

    @Override
    public void onResponse(Call<Question> call, Response<Question> response) {
        resultQuestion.setId(response.body().getId());
        resultQuestion.setText(response.body().getText());
        resultQuestion.setAnswers(response.body().getAnswers());
        resultQuestion.setQuestionType(response.body().getQuestionType());
        resultQuestion.setFileIds(response.body().getFileIds());
        questionTextView.setText(resultQuestion.getText());
        if (resultQuestion.getQuestionType() != null &&
                QuestionType.VOTE.isA(resultQuestion.getQuestionType())) {
            answerEditText.setVisibility(View.GONE);
        } else {
            answerEditText.setVisibility(View.VISIBLE);
        }
        currentQuestion = resultQuestion;
        answerTextEnterWatcher.setQuestion(resultQuestion);
        loadImages(resultQuestion.getId());
    }

    @Override
    public void onFailure(Call<Question> call, Throwable t) {
        resultQuestion.setText(failureToLoadQuestionErrorMessage);
        questionTextView.setText(failureToLoadQuestionErrorMessage);
        questionTextView.setTextColor(Color.RED);
        t.printStackTrace();
    }

    private void loadImages(int questionId) {
        questionService.loadFile(questionId).enqueue(new Callback<List<QuestionFile>>() {
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
        });
    }
}


