package com.robandboo.fq;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.robandboo.fq.listener.LoadTopicsPageClickListener;
import com.robandboo.fq.localdata.entity.MyQuestionsConfig;
import com.robandboo.fq.localdata.repository.MyQuestionsLocalRepository;
import com.robandboo.fq.presenter.MyQuestionsListPresenter;
import com.robandboo.fq.util.activity.ActivityManager;

public class MyQuestionsActivity extends AppCompatActivity {
    private MyQuestionsListPresenter myQuestionsListPresenter;

    private MyQuestionsLocalRepository myQuestionsLocalRepository;

    private MyQuestionsConfig myQuestionsConfig;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_my_questions);
        LinearLayout myQuestionsLayout = findViewById(R.id.myQuestionsAllScrolledContent);
        LinearLayout myQuestionList = findViewById(R.id.questionLists);
        LinearLayout mySingleQuestion = findViewById(R.id.singleQuestion);
        ImageView backBtn = findViewById(R.id.backToQListImgBtn);
        backBtn.setOnClickListener(view -> {
            mySingleQuestion.setVisibility(View.GONE);
            myQuestionList.setVisibility(View.VISIBLE);
        });
        myQuestionsListPresenter =
                new MyQuestionsListPresenter(
                        myQuestionsLayout,
                        mySingleQuestion,
                        myQuestionList
                );
        myQuestionsLocalRepository = new MyQuestionsLocalRepository(this);
        myQuestionsConfig =
                myQuestionsLocalRepository.readMyQuestionsConfig();
        loadLastPage();
        LinearLayout pagesView = findViewById(R.id.topicsPagesLayout);
        for (int i = myQuestionsConfig.getPageNumber(); i >= 1; i --) {
            //TODO:transfer to fragment file
            TextView href = new TextView(pagesView.getContext());
            href.setText(String.valueOf(myQuestionsConfig.getPageNumber() - i + 1));
            href.setTextSize(24f);
            href.setPadding(50, 0, 0, 0);
            href.setOnClickListener(new LoadTopicsPageClickListener(
                    i,
                    myQuestionsListPresenter
            ));
            pagesView.addView(href);
        }
        Button clearMyQuestionsCacheBtn = findViewById(R.id.clearMyQuestionsCacheBtn);
        clearMyQuestionsCacheBtn.setOnClickListener(view -> {
            myQuestionsLocalRepository.clearAllData();
            restartActivity();
        });
    }

    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public void loadLastPage() {
        myQuestionsListPresenter
                .loadTopicsFromPage(myQuestionsConfig.getPageNumber());
    }
}
