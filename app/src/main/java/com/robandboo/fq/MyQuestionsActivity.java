package com.robandboo.fq;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.robandboo.fq.listener.LoadTopicsPageClickListener;
import com.robandboo.fq.localdata.entity.MyQuestionsConfig;
import com.robandboo.fq.localdata.repository.MyQuestionsLocalRepository;
import com.robandboo.fq.presenter.MyQuestionsListPresenter;

import java.util.Random;

public class MyQuestionsActivity extends AppCompatActivity {
    private MyQuestionsListPresenter myQuestionsListPresenter;

    private MyQuestionsLocalRepository myQuestionsLocalRepository;

    private MyQuestionsConfig myQuestionsConfig;

    private static ConstraintLayout activityLayout;

    public static final int[] backgroundResourcesIds = new int[] {
            R.drawable.back1,
            R.drawable.back2,
            R.drawable.back4,
            R.drawable.back5,
            R.drawable.back6,
            R.drawable.back7,
            R.drawable.back8,
            R.drawable.back9,
            R.drawable.back10
    };

    public static void changeBackground() {
        Random random = new Random();
        int backgroundId = random.nextInt(9);
        activityLayout.setBackgroundResource(backgroundResourcesIds[backgroundId]);
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_my_questions);
        activityLayout = findViewById(R.id.myQuestionsActivity);
        //changeBackground();
        LinearLayout myQuestionsLayout = findViewById(R.id.mainMyQuestionsLayout);
        myQuestionsListPresenter =
                new MyQuestionsListPresenter(myQuestionsLayout);
        myQuestionsLocalRepository = new MyQuestionsLocalRepository(this);
        myQuestionsConfig =
                myQuestionsLocalRepository.readMyQuestionsConfig();
        loadLastPage();
        LinearLayout pagesView = findViewById(R.id.topicsPagesLayout);
        for (int i = myQuestionsConfig.getPageNumber(); i >= 0;i --) {
            //TODO:transfer to fragment file
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
