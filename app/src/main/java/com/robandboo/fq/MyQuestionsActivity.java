package com.robandboo.fq;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.robandboo.fq.listener.LoadTopicsPageClickListener;
import com.robandboo.fq.listener.TouchFullScreenImageControlListener;
import com.robandboo.fq.localdata.entity.MyQuestionsConfig;
import com.robandboo.fq.localdata.repository.MyQuestionsLocalRepository;
import com.robandboo.fq.presenter.MyQuestionsListPresenter;

import java.io.File;

public class MyQuestionsActivity extends AppCompatActivity {
    private MyQuestionsListPresenter myQuestionsListPresenter;

    private MyQuestionsLocalRepository myQuestionsLocalRepository;

    private MyQuestionsConfig myQuestionsConfig;

    private ImageView updateSingleQuestionBtn;

    private Point screenSize;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_questions);
        Display display = getWindowManager().getDefaultDisplay();
        screenSize = new Point();
        display.getSize(screenSize);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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
        for (int i = myQuestionsConfig.getPageNumber(); i >= 1; i--) {
            //TODO:transfer to fragment file
            TextView href = new TextView(pagesView.getContext());
            href.setText(String.valueOf(myQuestionsConfig.getPageNumber() - i + 1));
            href.setTextSize(24f);
            href.setTextColor(Color.BLACK);
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
        updateSingleQuestionBtn = mySingleQuestion.findViewById(R.id.updateSingleQuestion);
        updateSingleQuestionBtn.setOnClickListener(view -> {
            myQuestionsListPresenter.updateCurrentSingleQuestionPage();
        });
        ImageView imageView1 = mySingleQuestion.findViewById(R.id.imageView1);
        imageView1.setOnClickListener(view -> {
            File file = new File(myQuestionsListPresenter.getFilePath1());
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory
                        .decodeFile(file.getAbsolutePath());
                fullScreenImage(bitmap);
            }
        });
        ImageView imageView2 = mySingleQuestion.findViewById(R.id.imageView2);
        imageView2.setOnClickListener(view -> {
            File file = new File(myQuestionsListPresenter.getFilePath2());
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory
                        .decodeFile(file.getAbsolutePath());
                fullScreenImage(bitmap);
            }
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

    private void fullScreenImage(Bitmap bitmap) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View content = getLayoutInflater().inflate(R.layout.image_full_screen_view, null);
        dialog.setContentView(content);
        ImageView popupImageView = dialog.findViewById(R.id.fullScreenImageView);
        content.setOnTouchListener(
                new TouchFullScreenImageControlListener(dialog, popupImageView, screenSize)
        );
        popupImageView.setImageBitmap(bitmap);
        dialog.getWindow().setBackgroundDrawable(null);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();
    }
}
