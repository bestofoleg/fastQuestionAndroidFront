package com.robandboo.fq.presenter;

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
        topicExpandableListAdapter =
                new TopicExpandableListAdapter(new ArrayList<Topic>());
        topicsExpandableListView.setAdapter(topicExpandableListAdapter);
        topicsExpandableListView
                .setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                topicExpandableListAdapter.updateTopicFromServerByGroupId(groupPosition);
            }
        });
    }

    public void loadTopicsFromPage(int page) {
        List<Question> questions =
                myQuestionsLocalRepository.readAllQuestionsFromPage(page);
        List<Topic> topics = new ArrayList<>();
        for (Question question : questions) {
            topics.add(new Topic(question, new ArrayList<Answer>()));
        }
        topicExpandableListAdapter.setNewItems(topics);
    }
}


