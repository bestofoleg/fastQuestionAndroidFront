package com.robandboo.fq;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.robandboo.fq.chain.ChainManager;
import com.robandboo.fq.listener.NextStateClickListener;
import com.robandboo.fq.presenter.AskPresenter;
import com.robandboo.fq.presenter.QuestionPresenter;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout questionLayout = findViewById(R.id.answerLayout);
        LinearLayout askLayout = findViewById(R.id.questionLayout);
        QuestionPresenter questionPresenter = new QuestionPresenter(questionLayout);
        AskPresenter askPresenter = new AskPresenter(askLayout);
        ChainManager chainManager = new ChainManager(questionPresenter, askPresenter,3, 1);
        Button sendAnswerButton = findViewById(R.id.answerButton);
        Button sendQuestionButton = findViewById(R.id.askButton);
        NextStateClickListener nextStateClickListener =
                new NextStateClickListener(chainManager);
        sendAnswerButton.setOnClickListener(nextStateClickListener);
        sendQuestionButton.setOnClickListener(nextStateClickListener);
        chainManager.init();
    }
}
