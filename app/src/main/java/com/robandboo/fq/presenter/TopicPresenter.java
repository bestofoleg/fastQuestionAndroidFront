package com.robandboo.fq.presenter;

import android.widget.LinearLayout;
import android.widget.TextView;

public class TopicPresenter {
    private LinearLayout topicLayout;

    private TextView questionTextView;

    public TopicPresenter(LinearLayout topicLayout) {
        this.topicLayout = topicLayout;
    }

    public void setQuestionText(String text){
        questionTextView.setText(text);
    }

    public void loadAllAnswers() {

    }

    public void toggle(boolean isOpen) {

    }

    public void clearAllAnswers() {

    }
}
