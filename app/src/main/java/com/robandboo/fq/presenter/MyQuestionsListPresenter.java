package com.robandboo.fq.presenter;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.robandboo.fq.dto.Answer;
import com.robandboo.fq.dto.Question;
import com.robandboo.fq.localdata.repository.MyQuestionsLocalRepository;
import com.robandboo.fq.service.AnswerService;
import com.robandboo.fq.service.NetworkSingleton;
import com.robandboo.fq.ui.adapter.TopicExpandableListAdapter;
import com.robandboo.fq.ui.entity.Topic;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyQuestionsListPresenter {
    private AnswerService answerService;

    private LinearLayout myQuestionsListRootLayout;

    private MyQuestionsLocalRepository myQuestionsLocalRepository;

    private ExpandableListView topicsExpandableListView;

    private TopicExpandableListAdapter topicExpandableListAdapter;

    public MyQuestionsListPresenter(LinearLayout myQuestionsListRootLayout) {
        this.myQuestionsListRootLayout = myQuestionsListRootLayout;
        answerService = NetworkSingleton.getInstance()
                .getRetrofit().create(AnswerService.class);
        myQuestionsLocalRepository =
                new MyQuestionsLocalRepository(myQuestionsListRootLayout.getContext());
        topicsExpandableListView =
                new ExpandableListView(myQuestionsListRootLayout.getContext());
        myQuestionsListRootLayout.addView(topicsExpandableListView);
    }

    public void loadTopicsFromPage(int page) {
        List<Question> questions =
                myQuestionsLocalRepository.readAllQuestionsFromPage(page);
        final List<Topic> topics = new ArrayList<>();
        for (final Question question : questions) {
            answerService.getAnswerByQuestionId(question.getId()).enqueue(new Callback<List<Answer>>() {
                @Override
                public void onResponse(Call<List<Answer>> call, Response<List<Answer>> response) {
                    if (response.body() != null) {
                        topics.add(new Topic(question.getText(), response.body()));
                    } else {
                        topics.add(new Topic(question.getText(), new ArrayList<Answer>()));
                    }
                }

                @Override
                public void onFailure(Call<List<Answer>> call, Throwable t) {
                    //TODO: Implement it!
                }
            });
        }
        if (topicExpandableListAdapter == null) {
            topicExpandableListAdapter = new TopicExpandableListAdapter(topics);
        } else {
            topicExpandableListAdapter.setNewItems(topics);
        }
        topicsExpandableListView.setAdapter(topicExpandableListAdapter);

    }
}


