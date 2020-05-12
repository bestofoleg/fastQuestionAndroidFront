package com.robandboo.fq.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.robandboo.fq.R;
import com.robandboo.fq.chain.AnswerToQuestionChainManager;
import com.robandboo.fq.dto.Answer;
import com.robandboo.fq.dto.Question;
import com.robandboo.fq.dto.QuestionFile;
import com.robandboo.fq.service.AnswerService;
import com.robandboo.fq.service.NetworkSingleton;
import com.robandboo.fq.service.QuestionService;
import com.robandboo.fq.util.validation.AnswerValidation;
import com.robandboo.fq.watcher.AnswerTextEnterWatcher;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnswerToQuestionsPresenter implements ILayoutPresenter <LinearLayout>{
    private LinearLayout answerToQuestionLayout;

    private QuestionService questionService;

    private AnswerService answerService;

    private TextView questionTextView;

    private EditText answerEditText;

    private Question currentQuestion;

    private TextView questionsQuantityTextView;

    private String failureToLoadQuestionErrorMessage;

    private String failureToSendAnswerErrorMessage;

    private AnswerTextEnterWatcher answerTextEnterWatcher;

    private AnswerToQuestionChainManager answerToQuestionChainManager;

    private LinearLayout sendAnswerModeLayout;

    private LinearLayout loadAnswersModeLayout;

    private String questionIsLoadingMessage;

    private ImageView imageView1;

    private ImageView imageView2;

    private Bitmap currentBitmap1;

    private Bitmap currentBitmap2;

    public AnswerToQuestionsPresenter(LinearLayout answerToQuestionLayout, AppCompatActivity appCompatActivity) {
        this.answerToQuestionLayout = answerToQuestionLayout;
        questionService = NetworkSingleton.getInstance().getRetrofit()
                .create(QuestionService.class);
        answerService = NetworkSingleton.getInstance().getRetrofit()
                .create(AnswerService.class);
        questionTextView = answerToQuestionLayout.findViewById(R.id.questionTextView);
        answerEditText = answerToQuestionLayout.findViewById(R.id.answerTextEdit);
        questionsQuantityTextView = answerToQuestionLayout.findViewById(R.id.counterTextView);
        failureToLoadQuestionErrorMessage =
                answerToQuestionLayout.getContext().getResources()
                        .getString(R.string.failureToLoadQuestionErrorMessage);
        failureToSendAnswerErrorMessage =
                answerToQuestionLayout.getContext().getResources()
                        .getString(R.string.failureToSendAnswerErrorMessage);
        currentQuestion = null;
        answerToQuestionChainManager =
                new AnswerToQuestionChainManager(this);
        answerTextEnterWatcher =
                new AnswerTextEnterWatcher(answerEditText, answerToQuestionChainManager);
        answerEditText.addTextChangedListener(answerTextEnterWatcher);
        sendAnswerModeLayout = answerToQuestionLayout.findViewById(R.id.answerInputLayout);
        loadAnswersModeLayout = answerToQuestionLayout.findViewById(R.id.usersAnswersLayout);
        answerTextEnterWatcher.setAppCompatActivity(appCompatActivity);
        questionIsLoadingMessage = answerToQuestionLayout.getContext().getResources()
                .getString(R.string.questionLoading);
        imageView1 = answerToQuestionLayout.findViewById(R.id.answerImage1);
        imageView2 = answerToQuestionLayout.findViewById(R.id.answerImage2);
    }

    @Override
    public void focus() {
        answerEditText.requestFocus();
    }

    public Question loadRandomQuestion() {
        final Question resultQuestion = new Question();
        questionService.getRandomQuestion().enqueue(new Callback<Question>() {
            @Override
            public void onResponse(Call<Question> call, Response<Question> response) {
                resultQuestion.setId(response.body().getId());
                resultQuestion.setText(response.body().getText());
                resultQuestion.setAnswers(response.body().getAnswers());
                questionTextView.setText(resultQuestion.getText());
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
        });
        return resultQuestion;
    }

    private void loadImages(int questionId) {
        questionService.loadFile(questionId).enqueue(new Callback<List<QuestionFile>>() {
            @Override
            public void onResponse(Call<List<QuestionFile>> call,
                                   Response<List<QuestionFile>> response) {
                List <QuestionFile> questionFiles = response.body();
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

    public Bitmap getCurrentBitmap1() {
        return currentBitmap1;
    }

    public void setCurrentBitmap1(Bitmap currentBitmap1) {
        this.currentBitmap1 = currentBitmap1;
    }

    public Bitmap getCurrentBitmap2() {
        return currentBitmap2;
    }

    public void setCurrentBitmap2(Bitmap currentBitmap2) {
        this.currentBitmap2 = currentBitmap2;
    }

    public void transferImagesToLoadState() {
        Glide
                .with(imageView1)
                .load(R.drawable.loading)
                .into(imageView1);
        Glide
                .with(imageView2)
                .load(R.drawable.loading)
                .into(imageView2);
    }

    public void sendAnswer(AnswerValidation answerValidation) {
        Answer answer = new Answer();
        answer.setQuestion(currentQuestion);
        answer.setText(answerEditText.getText().toString());
        answerValidation.setDataForValidation(answerEditText.getText().toString());
        answerService.saveAnswer(answer).enqueue(new Callback<Answer>() {
            @Override
            public void onResponse(Call<Answer> call, Response<Answer> response) {
            }

            @Override
            public void onFailure(Call<Answer> call, Throwable t) {
                Toast.makeText(
                        answerToQuestionLayout.getContext(),
                        failureToSendAnswerErrorMessage,
                        Toast.LENGTH_SHORT
                ).show();
                t.printStackTrace();
            }
        });
    }

    @Override
    public void setLayoutVisibility(boolean isVisible) {
        if (isVisible) {
            answerToQuestionLayout.setVisibility(View.VISIBLE);
        } else {
            answerToQuestionLayout.setVisibility(View.GONE);
        }
    }

    public void setInputAnswerLayoutVisibility(boolean visibility) {
        if (visibility) {
            sendAnswerModeLayout.setVisibility(View.VISIBLE);
        } else {
            sendAnswerModeLayout.setVisibility(View.GONE);
        }
    }

    public void setLoadAnswersLayoutVisibility(boolean visibility) {
        if (visibility) {
            loadAnswersModeLayout.setVisibility(View.VISIBLE);
        } else {
            loadAnswersModeLayout.setVisibility(View.GONE);
        }
    }

    public void clearAnswerTextEdit() {
        answerEditText.getText().clear();
    }

    public void transferQuestionTextViewInLoadState() {
        questionTextView.setText(questionIsLoadingMessage);
    }

    public void setQuestionNumber(int number) {
        String labelTemplate = answerToQuestionLayout
                .getResources()
                .getString(
                        R.string.counterTextView,
                        number
                );
        questionsQuantityTextView.setText(labelTemplate);
    }

    @Override
    public LinearLayout getRootLayout() {
        return answerToQuestionLayout;
    }
}
