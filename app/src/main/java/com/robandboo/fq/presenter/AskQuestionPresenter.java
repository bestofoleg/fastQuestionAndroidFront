package com.robandboo.fq.presenter;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.robandboo.fq.R;
import com.robandboo.fq.dto.Question;
import com.robandboo.fq.listener.SaveQuestionCallbackListener;
import com.robandboo.fq.localdata.repository.MyQuestionsLocalRepository;
import com.robandboo.fq.service.NetworkSingleton;
import com.robandboo.fq.service.QuestionService;
import com.robandboo.fq.util.validation.QuestionValidation;

import java.io.File;
import java.util.List;

import lombok.Setter;

public class AskQuestionPresenter implements ILayoutPresenter <LinearLayout>{
    private final String FAILURE_TO_ASK_QUESTION_ERROR_MESSAGE = "Failure to ask question...";

    private QuestionService questionService;

    private LinearLayout askLayout;

    private EditText askQuestionEditText;

    private MyQuestionsLocalRepository questionsLocalRepository;

    private String errorSendAskMessage;

    private AddImagePresenter addImagePresenter;

    private CheckBox isVoteCheckBox;

    @Setter
    private SingleQuestionAnswersPresenter singleQuestionAnswersPresenter;

    public AskQuestionPresenter(LinearLayout askLayout, AddImagePresenter addImagePresenter) {
        this.askLayout = askLayout;
        this.addImagePresenter = addImagePresenter;
        isVoteCheckBox = askLayout.findViewById(R.id.isVoiter);
        askQuestionEditText = askLayout.findViewById(R.id.questionTextEdit);
        questionService = NetworkSingleton.getInstance().getRetrofit()
                .create(QuestionService.class);
        questionsLocalRepository = new MyQuestionsLocalRepository(askLayout.getContext());
        errorSendAskMessage = askLayout.getResources().getString(R.string.errorSendAskMessage);
    }

    public List<File> getImageFilesFromAddImagePresenter() {
        return addImagePresenter.getImageFiles();
    }

    public Question sendQuestion(QuestionValidation questionValidation) {
        final Question askedQuestion = new Question();
        askedQuestion.setText(askQuestionEditText.getText().toString());
        askedQuestion.setQuestionType((isVoteCheckBox.isChecked())?"VOTE":"TEXT");
        questionValidation.setDataForValidation(askQuestionEditText.getText().toString());
        if (questionValidation.validateWithoutToast()) {
            SaveQuestionCallbackListener callback = SaveQuestionCallbackListener.builder()
                    .addImagePresenter(addImagePresenter)
                    .askedQuestion(askedQuestion)
                    .askLayout(askLayout)
                    .errorSendAskMessage(errorSendAskMessage)
                    .questionsLocalRepository(questionsLocalRepository)
                    .questionService(questionService)
                    .singleQuestionAnswersPresenter(singleQuestionAnswersPresenter)
                    .build();
            questionService.saveQuestion(askedQuestion).enqueue(callback);
        }
        return askedQuestion;
    }

    public void clearQuestionEditText() {
        askQuestionEditText.getText().clear();
    }

    public void clearCheckBox() {
        isVoteCheckBox.setChecked(false);
        isVoteCheckBox.setVisibility(View.GONE);
    }

    public void clearRemoveImageButtons() {
        addImagePresenter.setRemoveImageButtonActive(false);
    }

    @Override
    public void setLayoutVisibility(boolean isVisible) {
        if (isVisible) {
            askLayout.setVisibility(View.VISIBLE);
        } else {
            askLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void focus() {
        askQuestionEditText.requestFocus();
    }

    @Override
    public LinearLayout getRootLayout() {
        return askLayout;
    }
}
