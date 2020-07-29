package com.robandboo.fq;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.robandboo.fq.chain.ChainManager;
import com.robandboo.fq.chain.bridge.ImageFilesDataBridge;
import com.robandboo.fq.chain.bridge.QuestionDataBridge;
import com.robandboo.fq.chain.state.AnimationTransitionState;
import com.robandboo.fq.chain.state.AnswerToQuestionPageState;
import com.robandboo.fq.chain.state.AskQuestionPageState;
import com.robandboo.fq.chain.state.IState;
import com.robandboo.fq.chain.state.SingleQuestionPageState;
import com.robandboo.fq.listener.NextStateSwipeListener;
import com.robandboo.fq.listener.TouchFullScreenImageControlListener;
import com.robandboo.fq.presenter.AddImagePresenter;
import com.robandboo.fq.presenter.AnswerToQuestionsPresenter;
import com.robandboo.fq.presenter.AskQuestionPresenter;
import com.robandboo.fq.presenter.SingleQuestionAnswersPresenter;
import com.robandboo.fq.util.activity.ActivityManager;
import com.robandboo.fq.util.activity.OnFileManagerReturnResultListener;
import com.robandboo.fq.util.swipe.SwipeHandler;
import com.robandboo.fq.util.swipe.SwipeSettings;
import com.robandboo.fq.util.swipe.SwipeVector;
import com.robandboo.fq.watcher.QuestionTextEnterWatcher;

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
        Glide
                .with(activityLayout.getContext())
                .load(backgroundResourcesIds[backgroundId])
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(
                            @NonNull Drawable resource,
                            @Nullable Transition<? super Drawable> transition) {
                        activityLayout.setBackground(resource);
                    }
                });
    }

    public static final int GALLERY_REQUEST_CODE = 10;

    public static OnFileManagerReturnResultListener fileManagerReturnResultListener;

    private Point screenSize;

    public static ChainManager chainManager;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = getWindowManager().getDefaultDisplay();
        screenSize = new Point();
        display.getSize(screenSize);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        MAIN_INFLATER = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        setContentView(R.layout.activity_main);
        activityLayout = findViewById(R.id.activityMainLayout);
        changeBackground();
        LinearLayout answerLayout = findViewById(R.id.answerLayout);
        LinearLayout askLayout = findViewById(R.id.questionLayout);
        AnswerToQuestionsPresenter answerToQuestionsPresenter =
                new AnswerToQuestionsPresenter(answerLayout, this);
        ImageView addImageImageView1 = findViewById(R.id.addImageButton1);
        ImageView addImageImageView2 = findViewById(R.id.addImageButton2);
        AddImagePresenter addImagePresenter =
                new AddImagePresenter(
                        addImageImageView1,
                        addImageImageView2,
                        this
                );
        AskQuestionPresenter askQuestionPresenter =
                new AskQuestionPresenter(askLayout, addImagePresenter);
        List<IState> states = new ArrayList<>();
        int userAnswersQuantity =
                getResources().getInteger(R.integer.defaultAnswersQuantity);
        AnimationTransitionState answerToAnswerTransitionState = new AnimationTransitionState(
                R.anim.swipe_to_left_anim,
                R.anim.layout_introduce_anim,
                answerToQuestionsPresenter,
                answerToQuestionsPresenter
        );
        AnimationTransitionState answerToQuestionTransitionState = new AnimationTransitionState(
                R.anim.swipe_to_left_anim,
                R.anim.layout_introduce_anim,
                answerToQuestionsPresenter,
                askQuestionPresenter
        );
        answerToAnswerTransitionState.setChangePageTransition(true);
        answerToQuestionTransitionState.setChangePageTransition(true);
        states.add(new AnimationTransitionState(
                -1,
                R.anim.layout_introduce_anim,
                null,
                answerToQuestionsPresenter
        ));
        states.add(new AnswerToQuestionPageState(
                answerToQuestionsPresenter,
                this,
                1, userAnswersQuantity
        ));
        for (int i = 1; i < userAnswersQuantity; i++) {
            states.add(answerToAnswerTransitionState);
            states.add(new AnswerToQuestionPageState(
                    answerToQuestionsPresenter,
                    this,
                    i+1, userAnswersQuantity
            ));
        }
        states.add(answerToQuestionTransitionState);
        QuestionDataBridge questionDataBridge = new QuestionDataBridge();
        ImageFilesDataBridge imageFilesDataBridge = new ImageFilesDataBridge();
        states.add(new AskQuestionPageState(
                askQuestionPresenter, answerToQuestionsPresenter,
                this, questionDataBridge, imageFilesDataBridge
        ));
        LinearLayout singleQuestionLayout =
                findViewById(R.id.singleQuestionAnswersPopup);
        SingleQuestionAnswersPresenter singleQuestionAnswersPresenter =
                new SingleQuestionAnswersPresenter(singleQuestionLayout);
        askQuestionPresenter.setSingleQuestionAnswersPresenter(singleQuestionAnswersPresenter);
        AnimationTransitionState questionToSingleTransitionState =
                new AnimationTransitionState(
                        R.anim.swipe_to_left_anim,
                        R.anim.single_question_page_anim,
                        askQuestionPresenter,
                        singleQuestionAnswersPresenter
                );
        questionToSingleTransitionState.setChangePageTransition(true);
        states.add(questionToSingleTransitionState);
        states.add(new SingleQuestionPageState(
                singleQuestionAnswersPresenter,
                this,
                questionDataBridge, imageFilesDataBridge
        ));
        AnimationTransitionState singleToAnswerTransitionState =
                new AnimationTransitionState(
                        R.anim.swipe_to_left_anim,
                        R.anim.layout_introduce_anim,
                        singleQuestionAnswersPresenter,
                        answerToQuestionsPresenter
                );
        singleToAnswerTransitionState.setChangePageTransition(true);
        states.add(singleToAnswerTransitionState);
        SwipeSettings swipeSettings = new SwipeSettings(
                20,20, 300,
                new SwipeVector(-1, 0)
        );
        chainManager = new ChainManager(states);
        SwipeHandler swipeHandler = new SwipeHandler(swipeSettings);
        swipeHandler.setSwipeListener(
                this,
                (ViewGroup) findViewById(R.id.mainLayout),
                new NextStateSwipeListener(chainManager));
        ImageView myQuestionsButton = findViewById(R.id.myQuestionsButton);
        myQuestionsButton.setOnClickListener(view -> {
            ActivityManager activityManager =
                    new ActivityManager(MainActivity.this);
            activityManager
                    .changeActivityTo(MyQuestionsActivity.class);
        });
        EditText askQuestionEditText = findViewById(R.id.questionTextEdit);
        askQuestionEditText.addTextChangedListener(new QuestionTextEnterWatcher(
                askQuestionEditText, chainManager
        ));
        initClickableFullScreenImages(
                answerToQuestionsPresenter,
                singleQuestionAnswersPresenter
        );
    }

    private void initClickableFullScreenImages(
            AnswerToQuestionsPresenter answerToQuestionsPresenter,
            SingleQuestionAnswersPresenter singleQuestionAnswersPresenter
    ) {
        ImageView answerImageView1 = findViewById(R.id.answerImage1);
        ImageView answerImageView2 = findViewById(R.id.answerImage2);

        answerImageView1.setOnLongClickListener(view -> {
            Bitmap bitmap = answerToQuestionsPresenter.getCurrentBitmap1();
            if (bitmap != null) {
                fullScreenImage(bitmap);
            }
            return true;
        });

        answerImageView2.setOnLongClickListener(view -> {
            Bitmap bitmap = answerToQuestionsPresenter.getCurrentBitmap2();
            if (bitmap != null) {
                fullScreenImage(bitmap);
            }
            return true;
        });
        ImageView singleQuestionView1 = findViewById(R.id.questionImage1);
        ImageView singleQuestionView2 = findViewById(R.id.questionImage2);
        singleQuestionView1.setOnClickListener(view ->
                fullScreenImage(singleQuestionAnswersPresenter.getBitmaps().get(0)));
        singleQuestionView2.setOnClickListener(view ->
                fullScreenImage(singleQuestionAnswersPresenter.getBitmaps().get(1)));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (Config.RC_PICK_IMAGES == requestCode  && RESULT_OK == resultCode && data != null) {
            List<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            if (!images.isEmpty()) {
                data.putExtra("imagePath", images.get(0).getPath());
                fileManagerReturnResultListener.onReturnResult(data);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
