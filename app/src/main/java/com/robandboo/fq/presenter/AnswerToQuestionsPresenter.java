package com.robandboo.fq.presenter;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.robandboo.fq.R;
import com.robandboo.fq.callback.GetRandomQuestionCallback;
import com.robandboo.fq.callback.SendAnswerCallback;
import com.robandboo.fq.chain.AnswerToQuestionChainManager;
import com.robandboo.fq.chain.ChainManager;
import com.robandboo.fq.dto.Answer;
import com.robandboo.fq.dto.Question;
import com.robandboo.fq.service.AnswerService;
import com.robandboo.fq.service.NetworkSingleton;
import com.robandboo.fq.service.QuestionService;
import com.robandboo.fq.util.enumeration.QuestionType;
import com.robandboo.fq.util.validation.AnswerValidation;
import com.robandboo.fq.util.wrapper.Wrapper;
import com.robandboo.fq.watcher.AnswerTextEnterWatcher;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

public class AnswerToQuestionsPresenter implements ILayoutPresenter<LinearLayout> {
    private LinearLayout answerToQuestionLayout;

    private QuestionService questionService;

    private AnswerService answerService;

    private TextView questionTextView;

    private EditText answerEditText;

    @Getter
    private Question currentQuestion;

    private TextView questionsQuantityTextView;

    private String failureToLoadQuestionErrorMessage;

    private String failureToSendAnswerErrorMessage;

    private AnswerTextEnterWatcher answerTextEnterWatcher;

    private AnswerToQuestionChainManager answerToQuestionChainManager;

    private LinearLayout sendAnswerModeLayout;

    private LinearLayout loadAnswersModeLayout;

    private String questionIsLoadingMessage;

    @Getter
    private ImageView imageView1;

    @Getter
    private ImageView imageView2;

    @Getter
    @Setter
    private Wrapper<Bitmap> currentBitmap1;

    @Getter
    @Setter
    private Wrapper<Bitmap> currentBitmap2;

    private ChainManager chainManager;

    private String voteErrorMessage;

    @Getter
    @Setter
    private Boolean skipValidationOnce;

    private GetRandomQuestionCallback getRandomQuestionCallback;

    public AnswerToQuestionsPresenter(
            LinearLayout answerToQuestionLayout,
            AppCompatActivity appCompatActivity) {
        currentBitmap1 = new Wrapper<>();
        currentBitmap2 = new Wrapper<>();
        currentQuestion = new Question();
        skipValidationOnce = Boolean.FALSE;
        this.answerToQuestionLayout = answerToQuestionLayout;
        voteErrorMessage = answerToQuestionLayout.getContext().getResources()
                .getString(R.string.voteError);
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
        imageView1.setOnClickListener((view) -> {
            if (QuestionType.VOTE.isA(currentQuestion.getQuestionType())) {
                skipValidationOnce = Boolean.TRUE;
            }
            getRandomQuestionCallback.makeVote("image1");
        });
        imageView2.setOnClickListener((view) -> {
            if (QuestionType.VOTE.isA(currentQuestion.getQuestionType())) {
                skipValidationOnce = Boolean.TRUE;
            }
            ArrayList<Object> objects = new ArrayList<>();
            getRandomQuestionCallback.makeVote("image2");
        });
    }

    @Override
    public void focus() {
        answerEditText.requestFocus();
    }

    public Question loadRandomQuestion() {
        final Question resultQuestion = new Question();
        currentBitmap1.setData(null);
        currentBitmap2.setData(null);
        GetRandomQuestionCallback getRandomQuestionCallback = GetRandomQuestionCallback.builder()
                .resultQuestion(resultQuestion)
                .questionTextView(questionTextView)
                .answerEditText(answerEditText)
                .currentQuestion(currentQuestion)
                .answerTextEnterWatcher(answerTextEnterWatcher)
                .failureToLoadQuestionErrorMessage(failureToLoadQuestionErrorMessage)
                .questionService(questionService)
                .imageView1(imageView1)
                .imageView2(imageView2)
                .currentBitmap1(currentBitmap1)
                .currentBitmap2(currentBitmap2)
                .skipValidation(skipValidationOnce)
                .voteErrorMessage(voteErrorMessage)
                .answerToQuestionLayout(answerToQuestionLayout)
                .chainManager(chainManager)
                .answerService(answerService).build();
        this.getRandomQuestionCallback = getRandomQuestionCallback;
        questionService.getRandomQuestion().enqueue(getRandomQuestionCallback);
        return resultQuestion;
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
        answerValidation.setDataForValidation(
                answerEditText.getText().toString(),
                (currentQuestion != null) &&
                        QuestionType.VOTE.isA(currentQuestion.getQuestionType())
        );
        if (currentQuestion != null &&
                !QuestionType.VOTE.isA(currentQuestion.getQuestionType())) {
            SendAnswerCallback sendAnswerCallback = SendAnswerCallback.builder()
                    .answerToQuestionLayout(answerToQuestionLayout)
                    .failureToSendAnswerErrorMessage(failureToSendAnswerErrorMessage).build();
            answerService.saveAnswer(answer).enqueue(sendAnswerCallback);
        }
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

    public void setChainManager(ChainManager chainManager) {
        this.chainManager = chainManager;
    }
}
