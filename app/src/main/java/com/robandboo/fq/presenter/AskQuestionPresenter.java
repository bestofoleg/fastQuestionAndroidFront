package com.robandboo.fq.presenter;

import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.robandboo.fq.R;
import com.robandboo.fq.dto.Question;
import com.robandboo.fq.localdata.repository.MyQuestionsLocalRepository;
import com.robandboo.fq.service.NetworkSingleton;
import com.robandboo.fq.service.QuestionService;
import com.robandboo.fq.util.validation.QuestionValidation;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AskQuestionPresenter implements ILayoutPresenter <LinearLayout>{
    private final String FAILURE_TO_ASK_QUESTION_ERROR_MESSAGE = "Failure to ask question...";

    private QuestionService questionService;

    private LinearLayout askLayout;

    private EditText askQuestionEditText;

    private MyQuestionsLocalRepository questionsLocalRepository;

    private String errorSendAskMessage;

    private AddImagePresenter addImagePresenter;

    public AskQuestionPresenter(LinearLayout askLayout, AddImagePresenter addImagePresenter) {
        this.askLayout = askLayout;
        this.addImagePresenter = addImagePresenter;
        askQuestionEditText = askLayout.findViewById(R.id.questionTextEdit);
        questionService = NetworkSingleton.getInstance().getRetrofit()
                .create(QuestionService.class);
        questionsLocalRepository = new MyQuestionsLocalRepository(askLayout.getContext());
        errorSendAskMessage = askLayout.getResources().getString(R.string.errorSendAskMessage);
    }

    public Question sendQuestion(QuestionValidation questionValidation) {
        final Question askedQuestion = new Question();
        askedQuestion.setText(askQuestionEditText.getText().toString());
        questionValidation.setDataForValidation(askQuestionEditText.getText().toString());
        if (questionValidation.validateWithoutToast()) {
            questionService.saveQuestion(askedQuestion).enqueue(new Callback<Question>() {
                @Override
                public void onResponse(Call<Question> call, Response<Question> response) {
                    for (File imageFile : addImagePresenter.getImageFiles()) {
                        if (imageFile.exists()) {
                            saveFile(response.body().getId(), imageFile);
                        }
                    }
                    questionsLocalRepository.writeQuestion(response.body());
                    askedQuestion.setId(response.body().getId());
                }

                @Override
                public void onFailure(Call<Question> call, Throwable t) {
                    Toast.makeText(
                            askLayout.getContext(),
                            errorSendAskMessage,
                            Toast.LENGTH_SHORT
                    ).show();
                    t.printStackTrace();
                }
            });
        }
        return askedQuestion;
    }

    private void saveFile(int questionId, File file) {
        RequestBody imageBody =
                RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData(
                "file",
                file.getName(),
                imageBody
        );
        questionService.saveFile(questionId, imagePart).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    public void clearQuestionEditText() {
        askQuestionEditText.getText().clear();
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
