package com.robandboo.fq.callback;

import com.robandboo.fq.dto.Answer;
import com.robandboo.fq.ui.adapter.TopicExpandableListAdapter;
import com.robandboo.fq.ui.entity.Topic;

import java.util.List;

import lombok.Builder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Builder
public class GetAnswerByQuestionIdForUpdateTopicCallback implements Callback<List<Answer>> {
    private List<Topic> topics;
    private Integer id;
    private TopicExpandableListAdapter topicExpandableListAdapter;

    @Override
    public void onResponse(Call<List<Answer>> call, Response<List<Answer>> response) {
        if (response.body() != null) {
            topics.get(id).setAnswers(response.body());
        }
        topicExpandableListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailure(Call<List<Answer>> call, Throwable t) {}
}
