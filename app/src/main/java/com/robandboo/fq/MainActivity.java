package com.robandboo.fq;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.robandboo.fq.chain.ChainManager;
import com.robandboo.fq.chain.bridge.QuestionDataBridge;
import com.robandboo.fq.chain.state.AnswerToQuestionPageState;
import com.robandboo.fq.chain.state.AskQuestionPageState;
import com.robandboo.fq.chain.state.IState;
import com.robandboo.fq.chain.state.SingleQuestionPageState;
import com.robandboo.fq.listener.NextStateSwipeListener;
import com.robandboo.fq.presenter.AnswerToQuestionsPresenter;
import com.robandboo.fq.presenter.AskQuestionPresenter;
import com.robandboo.fq.presenter.SingleQuestionAnswersPresenter;
import com.robandboo.fq.util.ActivityManager;
import com.robandboo.fq.util.swipe.SwipeHandler;
import com.robandboo.fq.util.swipe.SwipeSettings;
import com.robandboo.fq.util.swipe.SwipeVector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public static LayoutInflater MAIN_INFLATER;

    private static CoordinatorLayout activityLayout;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MAIN_INFLATER = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        setContentView(R.layout.activity_main);
        activityLayout = findViewById(R.id.activityMainLayout);
        changeBackground();
        LinearLayout questionLayout = findViewById(R.id.answerLayout);
        LinearLayout askLayout = findViewById(R.id.questionLayout);
        AnswerToQuestionsPresenter answerToQuestionsPresenter =
                new AnswerToQuestionsPresenter(questionLayout);
        AskQuestionPresenter askQuestionPresenter =
                new AskQuestionPresenter(askLayout);
        List<IState> states = new ArrayList<>();
        int userAnswersQuantity =
                getResources().getInteger(R.integer.defaultAnswersQuantity);
        for (int i = 0; i < userAnswersQuantity; i++) {
            states.add(new AnswerToQuestionPageState(
                    answerToQuestionsPresenter,
                    this,
                    i+1, userAnswersQuantity
            ));
        }
        QuestionDataBridge questionDataBridge = new QuestionDataBridge();
        states.add(new AskQuestionPageState(
                askQuestionPresenter, answerToQuestionsPresenter,
                this, questionDataBridge
        ));
        LinearLayout singleQuestionLayout =
                findViewById(R.id.singleQuestionAnswersPopup);
        SingleQuestionAnswersPresenter singleQuestionAnswersPresenter =
                new SingleQuestionAnswersPresenter(singleQuestionLayout);
        states.add(new SingleQuestionPageState(
                singleQuestionAnswersPresenter,
                this,
                questionDataBridge
        ));
        ChainManager chainManager = new ChainManager(states);
        SwipeSettings swipeSettings = new SwipeSettings(
                20,20, 300,
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
    }
}
