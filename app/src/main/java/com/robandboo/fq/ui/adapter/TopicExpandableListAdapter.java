package com.robandboo.fq.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.robandboo.fq.MainActivity;
import com.robandboo.fq.R;
import com.robandboo.fq.ui.entity.Topic;

import java.util.List;

public class TopicExpandableListAdapter extends BaseExpandableListAdapter {
    private List<Topic> topics;

    public TopicExpandableListAdapter(List<Topic> topics) {
        this.topics = topics;
    }

    public void setNewItems(List <Topic> topics) {
        this.topics = topics;
        notifyDataSetChanged();
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
        TextView questionText = root.findViewById(R.id.topicQuestion);

        String questionString = topics.get(groupPosition)
                .getQuestionText();

        String questionTitle = questionString;

        if (!isExpanded) {
            questionTitle = questionString.substring(0, questionString.length() % 15) + "...";
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
        textView.setText("- " + topics.get(groupPosition).getAnswers().get(childPosition).getText());
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
