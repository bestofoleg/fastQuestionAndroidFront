package com.robandboo.fq.chain;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.robandboo.fq.MainActivity;
import com.robandboo.fq.R;
import com.robandboo.fq.dto.Answer;
import com.robandboo.fq.presenter.AnswerToQuestionsPresenter;

import java.util.List;

public class AnswerToQuestionChainManager {
    private AnswerToQuestionsPresenter answerToQuestionsPresenter;

    private LinearLayout sendAnswerModeLayout;

    private LinearLayout loadAnswersModeLayout;

    private LinearLayout questionTitleLayout;

    private TextView questionTextView;

    private LinearLayout userAnswersLayout;

    private Animation outsideAnim;

    private Animation insideAnim;

    private ImageView allAnswersImageView1;

    private ImageView allAnswersImageView2;

    public AnswerToQuestionChainManager(AnswerToQuestionsPresenter answerToQuestionsPresenter) {
        this.answerToQuestionsPresenter = answerToQuestionsPresenter;
        LinearLayout rootLayout = answerToQuestionsPresenter.getRootLayout();
        userAnswersLayout = answerToQuestionsPresenter.getRootLayout()
                .findViewById(R.id.usersAnswersLayout);
        questionTitleLayout = rootLayout.findViewById(R.id.questionTitleLayout);
        sendAnswerModeLayout = rootLayout.findViewById(R.id.answerInputLayout);
        loadAnswersModeLayout = rootLayout.findViewById(R.id.allAnswersLayout);
        questionTextView = sendAnswerModeLayout.findViewById(R.id.questionTextView);
        outsideAnim = AnimationUtils.loadAnimation(
                userAnswersLayout.getContext(), R.anim.move_top_outside
        );
        insideAnim = AnimationUtils.loadAnimation(
                userAnswersLayout.getContext(), R.anim.move_top_inside
        );
        allAnswersImageView1 = userAnswersLayout.findViewById(R.id.image1);
        allAnswersImageView2 = userAnswersLayout.findViewById(R.id.image2);
        outsideAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                sendAnswerModeLayout.setVisibility(View.GONE);
                userAnswersLayout.setVisibility(View.VISIBLE);
                userAnswersLayout.startAnimation(insideAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }

    public void startSendAnswerMode() {
        userAnswersLayout.setVisibility(View.GONE);
        sendAnswerModeLayout.setVisibility(View.VISIBLE);
    }

    public void startLoadAnswersMode(List<Answer> answers) {
        loadAnswersModeLayout.removeAllViews();
        sendAnswerModeLayout.startAnimation(outsideAnim);
        TextView questionTitleTextViewInAnswersLayout =
                questionTitleLayout.findViewById(R.id.questionTitle);

        if (answerToQuestionsPresenter.getCurrentBitmap1().getData() != null &&
                !answerToQuestionsPresenter.getCurrentBitmap1().getData().isRecycled()) {
            Glide
                    .with(allAnswersImageView1)
                    .load(answerToQuestionsPresenter.getCurrentBitmap1().getData())
                    .into(allAnswersImageView1);
            allAnswersImageView1.setVisibility(View.VISIBLE);
        } else {
            allAnswersImageView1.setVisibility(View.GONE);
        }

        if (answerToQuestionsPresenter.getCurrentBitmap2().getData() != null &&
                !answerToQuestionsPresenter.getCurrentBitmap2().getData().isRecycled()) {
            Glide
                    .with(allAnswersImageView2)
                    .load(answerToQuestionsPresenter.getCurrentBitmap2().getData())
                    .into(allAnswersImageView2);
            allAnswersImageView2.setVisibility(View.VISIBLE);
        } else {
            allAnswersImageView2.setVisibility(View.GONE);
        }
        questionTitleTextViewInAnswersLayout.setText(questionTextView.getText());
        if (answers.isEmpty()) {
            View root = MainActivity.MAIN_INFLATER.inflate(
                    R.layout.answer_on_single_question_layout,
                    null, false
            );
            TextView answerTextView = root.findViewById(R.id.singleAnswerText);
            answerTextView.setText(R.string.emptyAnswersDataMessage);
            loadAnswersModeLayout.addView(root);
        } else {
            answers.forEach(answer -> {
                View root = MainActivity.MAIN_INFLATER.inflate(
                        R.layout.answer_on_single_question_layout,
                        null, false
                );
                TextView answerTextView = root.findViewById(R.id.singleAnswerText);
                answerTextView.setText(answer.getText());
                loadAnswersModeLayout.addView(root);
            });
        }
    }
}
