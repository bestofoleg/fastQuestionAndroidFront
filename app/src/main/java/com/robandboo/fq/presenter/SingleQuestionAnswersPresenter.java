package com.robandboo.fq.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.robandboo.fq.MainActivity;
import com.robandboo.fq.R;
import com.robandboo.fq.dto.Answer;
import com.robandboo.fq.dto.Question;
import com.robandboo.fq.service.AnswerService;
import com.robandboo.fq.service.NetworkSingleton;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingleQuestionAnswersPresenter implements ILayoutPresenter <LinearLayout> {
    private LinearLayout singleQuestionLayout;

    private AnswerService answerService;

    private TextView questionTextView;

    private LinearLayout answersLayout;

    private ImageButton imageButton;

    private Question currentQuestion;

    private String answerPrefix;

    private String emptyAnswersDataMessage;

    private String failureToLoadAnswers;

    private ImageView imageView1;

    private Bitmap bitmap1;

    private ImageView imageView2;

    private Bitmap bitmap2;

    public SingleQuestionAnswersPresenter(LinearLayout singleQuestionLayout) {
        this.singleQuestionLayout = singleQuestionLayout;
        answerService = NetworkSingleton.getInstance().getRetrofit()
                .create(AnswerService.class);
        questionTextView = singleQuestionLayout.findViewById(R.id.questionTitleSingle);
        answersLayout = singleQuestionLayout.findViewById(R.id.singleAnswersList);
        imageButton = singleQuestionLayout.findViewById(R.id.updateSinglePageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleQuestionAnswersPresenter.this.updateData(currentQuestion);
            }
        });
        answerPrefix = singleQuestionLayout.getContext()
                .getResources().getString(R.string.answerPrefix);
        emptyAnswersDataMessage = singleQuestionLayout.getContext()
                .getResources().getString(R.string.emptyAnswersDataMessage);
        failureToLoadAnswers = singleQuestionLayout.getContext()
                .getResources().getString(R.string.failureToLoadAnswers);
        imageView1 = singleQuestionLayout.findViewById(R.id.questionImage1);
        imageView2 = singleQuestionLayout.findViewById(R.id.questionImage2);
    }

    @Override
    public void focus() {
        singleQuestionLayout.requestFocus();
    }

    public void viewAllImagesForQuestion(List<File> imageFiles) {
        bitmap1 = null;
        bitmap2 = null;
        imageView1.setVisibility(View.GONE);
        imageView2.setVisibility(View.GONE);
        if (imageFiles != null && !imageFiles.isEmpty()) {
            if (imageFiles.get(0) != null) {
                Glide
                        .with(imageView1)
                        .load(imageFiles.get(0))
                        .into(imageView1);
                bitmap1 = BitmapFactory.decodeFile(imageFiles.get(0).getAbsolutePath());
                imageView1.setVisibility(View.VISIBLE);
            }
            if (imageFiles.size() > 1 && imageFiles.get(1) != null) {
                Glide
                        .with(imageView2)
                        .load(imageFiles.get(1))
                        .into(imageView2);
                bitmap2 = BitmapFactory.decodeFile(imageFiles.get(1).getAbsolutePath());
                imageView2.setVisibility(View.VISIBLE);
            }
        }
    }

    public List<Bitmap> getBitmaps() {
        return Arrays.asList(bitmap1, bitmap2);
    }

    public void updateData(final Question question) {
        answerService.getAnswerByQuestionId(question.getId())
                .enqueue(new Callback<List<Answer>>() {
            @Override
            public void onResponse(Call<List<Answer>> call, Response<List<Answer>> response) {
                questionTextView.setText(question.getText());
                answersLayout.removeAllViews();
                if (response.body().isEmpty()) {
                    View answer = MainActivity.MAIN_INFLATER.inflate(
                            R.layout.answer_on_single_question_layout,
                            null, false
                    );
                    TextView answerTextView = answer.findViewById(R.id.singleAnswerText);
                    answerTextView.setText(emptyAnswersDataMessage);
                    answersLayout.addView(answer);
                    currentQuestion = question;
                } else {
                    for (Answer answer : response.body()) {
                        TextView answerTextView = new TextView(answersLayout.getContext());
                        answerTextView.setText(answerPrefix + answer.getText());
                        answerTextView.setTextSize(14);
                        answersLayout.addView(answerTextView);
                        currentQuestion = question;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Answer>> call, Throwable t) {
                Toast.makeText(
                        singleQuestionLayout.getContext(),
                        failureToLoadAnswers,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public LinearLayout getRootLayout() {
        return singleQuestionLayout;
    }

    @Override
    public void setLayoutVisibility(boolean visibility) {
        if (visibility) {
            singleQuestionLayout.setVisibility(View.VISIBLE);
        } else {
            singleQuestionLayout.setVisibility(View.GONE);
        }
    }
}
