package com.robandboo.fq;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.robandboo.fq.chain.ChainManager;
import com.robandboo.fq.listener.NextStateClickListener;
import com.robandboo.fq.presenter.AnswerToQuestionsPresenter;
import com.robandboo.fq.presenter.AskQuestionPresenter;
import com.robandboo.fq.util.ActivityManager;

public class MainActivity extends AppCompatActivity {
    public static LayoutInflater MAIN_INFLATER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MAIN_INFLATER = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        setContentView(R.layout.activity_main);
        LinearLayout questionLayout = findViewById(R.id.answerLayout);
        LinearLayout askLayout = findViewById(R.id.questionLayout);
        AnswerToQuestionsPresenter answerToQuestionsPresenter =
                new AnswerToQuestionsPresenter(questionLayout);
        AskQuestionPresenter askQuestionPresenter =
                new AskQuestionPresenter(askLayout);
        ChainManager chainManager =
                new ChainManager(
                        this,
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
        Button myQuestionsButton = findViewById(R.id.myQuestionsButton);
        myQuestionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager activityManager =
                        new ActivityManager(MainActivity.this);
                activityManager
                        .changeActivityTo(MyQuestionsActivity.class);
            }
        });
        chainManager.init();
    }
}
