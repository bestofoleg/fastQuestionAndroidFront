package com.robandboo.fq;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.robandboo.fq.chain.ChainManager;
import com.robandboo.fq.chain.QuestionLayoutPresenter;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout questionLayout = findViewById(R.id.questionLayout);
        QuestionLayoutPresenter questionLayoutPresenter =
                new QuestionLayoutPresenter(questionLayout, this);
        ChainManager chainManager = new ChainManager(questionLayoutPresenter);
        chainManager.setMaxQuestionsQuantity(3);
        chainManager.next();
    }
}
