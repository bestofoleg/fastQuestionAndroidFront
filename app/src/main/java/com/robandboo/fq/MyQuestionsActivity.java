package com.robandboo.fq;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.fonts.FontStyle;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robandboo.fq.listener.LoadTopicsPageClickListener;
import com.robandboo.fq.localdata.entity.MyQuestionsConfig;
import com.robandboo.fq.localdata.repository.MyQuestionsLocalRepository;
import com.robandboo.fq.presenter.MyQuestionsListPresenter;

public class MyQuestionsActivity extends AppCompatActivity {
    private MyQuestionsListPresenter myQuestionsListPresenter;

    private MyQuestionsLocalRepository myQuestionsLocalRepository;

    private MyQuestionsConfig myQuestionsConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_questions);
        LinearLayout myQuestionsLayout = findViewById(R.id.mainMyQuestionsLayout);
        myQuestionsListPresenter =
                new MyQuestionsListPresenter(myQuestionsLayout);
        myQuestionsLocalRepository = new MyQuestionsLocalRepository(this);
        myQuestionsConfig =
                myQuestionsLocalRepository.readMyQuestionsConfig();
        loadLastPage();
        LinearLayout pagesView = findViewById(R.id.topicsPagesLayout);
        for (int i = myQuestionsConfig.getPageNumber(); i >= 0;i --) {
            TextView href = new TextView(pagesView.getContext());
            href.setText(String.valueOf(i));
            href.setTextSize(24f);
            href.setPadding(50, 0, 0, 0);
            href.setOnClickListener(new LoadTopicsPageClickListener(i, myQuestionsListPresenter));
            pagesView.addView(href);
        }
    }

    public void loadLastPage() {
        myQuestionsListPresenter
                .loadTopicsFromPage(myQuestionsConfig.getPageNumber());
    }
}
