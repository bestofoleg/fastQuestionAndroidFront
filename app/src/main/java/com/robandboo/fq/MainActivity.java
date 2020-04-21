package com.robandboo.fq;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.robandboo.fq.chain.ChainManager;
import com.robandboo.fq.listener.NextStateClickListener;
import com.robandboo.fq.presenter.AskQuestionPresenter;
import com.robandboo.fq.presenter.AnswerToQuestionsPresenter;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout questionLayout = findViewById(R.id.answerLayout);
        LinearLayout askLayout = findViewById(R.id.questionLayout);
        AnswerToQuestionsPresenter answerToQuestionsPresenter =
                new AnswerToQuestionsPresenter(questionLayout);
        AskQuestionPresenter askQuestionPresenter =
                new AskQuestionPresenter(askLayout);
        ChainManager chainManager =
                new ChainManager(
                        answerToQuestionsPresenter,
                        askQuestionPresenter,
                        3,
                        1
                );
        Button sendAnswerButton = findViewById(R.id.answerButton);
        Button sendQuestionButton = findViewById(R.id.askButton);
        NextStateClickListener nextStateClickListener =
                new NextStateClickListener(chainManager);
        sendAnswerButton.setOnClickListener(nextStateClickListener);
        sendQuestionButton.setOnClickListener(nextStateClickListener);
        chainManager.init();
    }
}
