package com.robandboo.fq.listener;

import android.view.View;

import com.robandboo.fq.presenter.MyQuestionsListPresenter;

public class LoadTopicsPageClickListener implements View.OnClickListener {
    private int page;

    private MyQuestionsListPresenter myQuestionsListPresenter;

    public LoadTopicsPageClickListener(int page, MyQuestionsListPresenter myQuestionsListPresenter) {
        this.page = page;
        this.myQuestionsListPresenter = myQuestionsListPresenter;
    }

    @Override
    public void onClick(View view) {
        myQuestionsListPresenter.loadTopicsFromPage(page);
    }
}
