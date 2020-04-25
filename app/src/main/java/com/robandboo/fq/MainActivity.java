package com.robandboo.fq;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.robandboo.fq.chain.ChainManager;
import com.robandboo.fq.listener.NextStateSwipeListener;
import com.robandboo.fq.presenter.AnswerToQuestionsPresenter;
import com.robandboo.fq.presenter.AskQuestionPresenter;
import com.robandboo.fq.util.ActivityManager;
import com.robandboo.fq.util.swipe.SwipeHandler;
import com.robandboo.fq.util.swipe.SwipeSettings;
import com.robandboo.fq.util.swipe.SwipeVector;

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
                new AskQuestionPresenter(askLayout, this);
        ChainManager chainManager =
                new ChainManager(
                        this,
                        answerToQuestionsPresenter,
                        askQuestionPresenter,
                        3,
                        1
                );
        SwipeSettings swipeSettings = new SwipeSettings(
                50,50, 300,
                new SwipeVector(-1, 0)
        );
        SwipeHandler swipeHandler = new SwipeHandler(swipeSettings);
        swipeHandler.setSwipeListener(
                this,
                (ViewGroup) findViewById(R.id.mainLayout),
                new NextStateSwipeListener(chainManager));
        ImageView myQuestionsButton = findViewById(R.id.myQuestionsButton);
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
