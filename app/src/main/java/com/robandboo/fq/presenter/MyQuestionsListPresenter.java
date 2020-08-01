package com.robandboo.fq.presenter;

import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.robandboo.fq.MainActivity;
import com.robandboo.fq.R;
import com.robandboo.fq.callback.GetAnswersForSingleMyQuestionCallback;
import com.robandboo.fq.dto.Question;
import com.robandboo.fq.localdata.repository.MyQuestionsLocalRepository;
import com.robandboo.fq.service.AnswerService;
import com.robandboo.fq.service.NetworkSingleton;
import com.robandboo.fq.ui.adapter.TopicExpandableListAdapter;
import com.robandboo.fq.util.enumeration.QuestionType;

import java.io.File;
import java.util.List;

public class MyQuestionsListPresenter implements ILayoutPresenter <LinearLayout> {
    private AnswerService answerService;

    private LinearLayout myQuestionsListRootLayout;

    private MyQuestionsLocalRepository myQuestionsLocalRepository;

    private ExpandableListView topicsExpandableListView;

    private TopicExpandableListAdapter topicExpandableListAdapter;

    private LinearLayout single;

    private LinearLayout questionsList;

    private TextView singleQuestionTitle;

    private ImageView singleImageView1;

    private ImageView singleImageView2;

    private TextView singleVote1;

    private TextView singleVote2;

    private LinearLayout answersListLayout;

    public MyQuestionsListPresenter(
            LinearLayout myQuestionsListRootLayout,
            LinearLayout single,
            LinearLayout questionsList
    ) {
        singleQuestionTitle = single.findViewById(R.id.questionText);
        singleImageView1 = single.findViewById(R.id.imageView1);
        singleImageView2 = single.findViewById(R.id.imageView2);
        singleVote1 = single.findViewById(R.id.image1VotesText);
        singleVote2 = single.findViewById(R.id.image2VotesText);
        answersListLayout = single.findViewById(R.id.answersContent);
        this.questionsList = questionsList;
        this.single = single;
        single.setVisibility(View.GONE);
        questionsList.setVisibility(View.VISIBLE);
        this.myQuestionsListRootLayout = myQuestionsListRootLayout;
        answerService = NetworkSingleton.getInstance()
                .getRetrofit().create(AnswerService.class);
        myQuestionsLocalRepository =
                new MyQuestionsLocalRepository(myQuestionsListRootLayout.getContext());
        /*
        topicsExpandableListView =
                new ExpandableListView(myQuestionsListRootLayout.getContext());
        myQuestionsListRootLayout.addView(topicsExpandableListView);
        topicExpandableListAdapter =
                new TopicExpandableListAdapter(new ArrayList<>());
        topicsExpandableListView.setAdapter(topicExpandableListAdapter);
        topicsExpandableListView
                .setOnGroupExpandListener(groupPosition -> {
                    topicExpandableListAdapter.updateTopicFromServerByGroupId(groupPosition);
                });*/
    }

    private void addAllQuestionTopics(LinearLayout root, List<Question> questions) {
        questions.forEach(question -> {
            View topic = MainActivity.MAIN_INFLATER.inflate(
                    R.layout.topic_question_layout, null, false
            ).getRootView();
            String hiddenMulidot = topic.getContext().getResources()
                    .getString(R.string.hiddenMulidot);
            TextView questionTextView = topic.findViewById(R.id.topicQuestion);
            /**/
            String questionText = question.getText();
            String questionTitle =
                    questionText.substring(0, questionText.length() % 30) + hiddenMulidot;
            questionTextView.setText(questionTitle);
            topic.setOnClickListener(view -> {
                questionsList.setVisibility(View.GONE);
                single.setVisibility(View.VISIBLE);
                loadAllInSingleQuestionPage(question);
                updateAnswersOnSingleQuestionTopic(question);
            });
            root.addView(topic);
        });
    }

    private void loadAllInSingleQuestionPage(Question question) {
        singleQuestionTitle.setText(question.getText());
        String filePath1 = question.getFilePath1();
        String filePath2 = question.getFilePath2();
        File file1 = new File(filePath1 != null? filePath1: "");
        File file2 = new File(filePath2 != null? filePath2: "");
        if (file1.exists()) {
            Glide
                    .with(singleImageView1)
                    .load(file1)
                    .into(singleImageView1);
            singleImageView1.setVisibility(View.VISIBLE);
        } else {
            singleImageView1.setVisibility(View.GONE);
        }
        if (file2.exists()) {
            Glide
                    .with(singleImageView2)
                    .load(file2)
                    .into(singleImageView2);
            singleImageView2.setVisibility(View.VISIBLE);
        } else {
            singleImageView2.setVisibility(View.GONE);
        }
    }

    public void loadTopicsFromPage(int page) {
        myQuestionsListRootLayout.removeAllViews();
        List<Question> questions =
                myQuestionsLocalRepository.readAllQuestionsFromPage(page);
        addAllQuestionTopics(myQuestionsListRootLayout, questions);
        /*List<Topic> topics = new ArrayList<>();
        questions.forEach(question -> topics.add(new Topic(question, new ArrayList<>())));
        topicExpandableListAdapter.setNewItems(topics);*/
    }

    private void updateAnswersOnSingleQuestionTopic(Question question) {
        Long questionId = question.getId();
        if (!QuestionType.VOTE.isA(question.getQuestionType())) {
            GetAnswersForSingleMyQuestionCallback getAnswersForSingleMyQuestionCallback =
                    GetAnswersForSingleMyQuestionCallback.builder()
                            .answersList(answersListLayout).build();
            answerService.getAnswerByQuestionId(questionId)
                    .enqueue(getAnswersForSingleMyQuestionCallback);
        } else {
            //TODO: handle vote logic for view votes in my single question
        }
    }

    @Override
    public void focus() {
        myQuestionsListRootLayout.requestFocus();
    }

    @Override
    public LinearLayout getRootLayout() {
        return myQuestionsListRootLayout;
    }

    @Override
    public void setLayoutVisibility(boolean visibility) {
        if (visibility) {
            myQuestionsListRootLayout.setVisibility(View.VISIBLE);
        } else {
            myQuestionsListRootLayout.setVisibility(View.GONE);
        }
    }
}


