package com.robandboo.fq.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.robandboo.fq.MainActivity;
import com.robandboo.fq.R;
import com.robandboo.fq.dto.Answer;
import com.robandboo.fq.service.AnswerService;
import com.robandboo.fq.service.NetworkSingleton;
import com.robandboo.fq.ui.entity.Topic;

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

    public void setNewItems(List <Topic> topics) {
        this.topics = topics;
        notifyDataSetChanged();
    }

    public void updateTopicFromServerByGroupId(final int id) {
        int questionServerId = topics.get(id).getQuestion().getId();
        answerService.getAnswerByQuestionId(questionServerId)
                .enqueue(new Callback<List<Answer>>() {
            @Override
            public void onResponse(Call<List<Answer>> call, Response<List<Answer>> response) {
                if (response.body() != null) {
                    topics.get(id).setAnswers(response.body());
                }
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Answer>> call, Throwable t) {
                //TODO: implement it!
                notifyDataSetChanged();
            }
        });
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
