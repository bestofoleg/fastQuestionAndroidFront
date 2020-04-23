package com.robandboo.fq.presenter;

import android.widget.LinearLayout;

import com.robandboo.fq.service.AnswerService;
import com.robandboo.fq.service.NetworkSingleton;

public class MyQuestionsListPresenter {
    private AnswerService answerService;

    private LinearLayout myQuestionsListRootLayout;

    public MyQuestionsListPresenter(LinearLayout myQuestionsListRootLayout) {
        this.myQuestionsListRootLayout = myQuestionsListRootLayout;
        answerService = NetworkSingleton.getInstance()
                .getRetrofit().create(AnswerService.class);
    }

    public void loadTopicsFromPage(int page) {
    }
}


