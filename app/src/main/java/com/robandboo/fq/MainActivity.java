package com.robandboo.fq;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.robandboo.fq.chain.ChainManager;
import com.robandboo.fq.listener.SendAnswerButtonClickListener;
import com.robandboo.fq.presenter.QuestionPresenter;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout questionLayout = findViewById(R.id.answerLayout);
        QuestionPresenter questionPresenter = new QuestionPresenter(questionLayout);
        ChainManager chainManager = new ChainManager(questionPresenter, 3, 1);
        Button sendAnswerButton = findViewById(R.id.answerButton);
        sendAnswerButton.setOnClickListener(new SendAnswerButtonClickListener(chainManager));
        chainManager.next(false);
    }
}
