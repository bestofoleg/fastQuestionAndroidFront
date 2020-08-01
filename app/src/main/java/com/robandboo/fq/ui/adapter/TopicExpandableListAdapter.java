package com.robandboo.fq.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.robandboo.fq.MainActivity;
import com.robandboo.fq.R;
import com.robandboo.fq.callback.GetAnswerByQuestionIdForUpdateTopicCallback;
import com.robandboo.fq.dto.Answer;
import com.robandboo.fq.service.AnswerService;
import com.robandboo.fq.service.NetworkSingleton;
import com.robandboo.fq.ui.entity.Topic;

import java.io.File;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopicExpandableListAdapter extends BaseExpandableListAdapter {
    private List<Topic> topics;

    private AnswerService answerService;

    public TopicExpandableListAdapter(List<Topic> topics) {
        this.topics = topics;
        answerService = NetworkSingleton.getInstance()
                .getRetrofit().create(AnswerService.class);
    }

    public void setNewItems(List<Topic> topics) {
        this.topics = topics;
        notifyDataSetChanged();
    }

    public void updateTopicFromServerByGroupId(final Long id) {
        /*int questionServerId = topics.get(id).getQuestion().getId();
        GetAnswerByQuestionIdForUpdateTopicCallback getAnswerByQuestionIdForUpdateTopicCallback =
                GetAnswerByQuestionIdForUpdateTopicCallback.builder()
                    .id(id)
                    .topicExpandableListAdapter(this)
                    .topics(topics).build();
        answerService.getAnswerByQuestionId(questionServerId)
                .enqueue(getAnswerByQuestionIdForUpdateTopicCallback);*/
    }

    @Override
    public int getGroupCount() {
        return topics.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return topics.get(groupPosition).getAnswers().size();
    }

    @Override
    public Topic getGroup(int groupPosition) {
        return topics.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return topics.get(groupPosition).getAnswers().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View root = MainActivity.MAIN_INFLATER.inflate(
                R.layout.topic_question_layout, null, false
        ).getRootView();
        String hiddenMulidot = root.getContext().getResources().getString(R.string.hiddenMulidot);
        TextView questionText = root.findViewById(R.id.topicQuestion);
        String questionString = topics.get(groupPosition).getQuestion().getText();
        String questionTitle = questionString;
        if (!isExpanded) {
            ImageView image1 = root.findViewById(R.id.topicImage1);
            ImageView image2 = root.findViewById(R.id.topicImage2);
            String filePath1 = topics.get(groupPosition).getQuestion().getFilePath1();
            String filePath2 = topics.get(groupPosition).getQuestion().getFilePath2();
            File file1 = new File(filePath1 != null? filePath1: "");
            File file2 = new File(filePath2 != null? filePath2: "");
            if (file1.exists()) {
                Glide
                        .with(image1)
                        .load(file1)
                        .into(image1);
                image1.setVisibility(View.VISIBLE);
            } else {
                image1.setVisibility(View.GONE);
            }
            if (file2.exists()) {
                Glide
                        .with(image2)
                        .load(file2)
                        .into(image2);
                image1.setVisibility(View.VISIBLE);
            } else {
                image1.setVisibility(View.GONE);
            }
            questionTitle =
                    questionString.substring(0, questionString.length() % 15) + hiddenMulidot;
        }
        questionText.setText(questionTitle);
        return root;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = MainActivity.MAIN_INFLATER.inflate(
                R.layout.topic_answer_layout, null, false
        ).getRootView();
        TextView textView = view.findViewById(R.id.answerText);
        String answerPrefix =
                view.getContext().getResources().getString(R.string.answerPrefix);
        textView.setText(answerPrefix +
                topics.get(groupPosition).getAnswers().get(childPosition).getText());
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
