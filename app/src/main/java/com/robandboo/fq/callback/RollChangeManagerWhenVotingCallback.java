package com.robandboo.fq.callback;

import android.widget.LinearLayout;
import android.widget.Toast;

import com.robandboo.fq.MainActivity;
import com.robandboo.fq.chain.ChainManager;

import lombok.Builder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Builder
public class RollChangeManagerWhenVotingCallback implements Callback<Void> {
    private LinearLayout answerToQuestionLayout;
    private ChainManager chainManager;
    private String voteErrorMessage;

    @Override
    public void onResponse(Call<Void> call, Response<Void> response) {
        MainActivity.chainManager.next();
    }

    @Override
    public void onFailure(Call<Void> call, Throwable t) {
        Toast.makeText(
                answerToQuestionLayout.getContext(),
                voteErrorMessage,
                Toast.LENGTH_SHORT
        );
    }
}
