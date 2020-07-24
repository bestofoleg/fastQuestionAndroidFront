package com.robandboo.fq.callback;

import com.robandboo.fq.presenter.AddImagePresenter;

import lombok.Builder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Builder
public class SaveFileAndClearViewsCallback implements Callback<ResponseBody> {
    private AddImagePresenter addImagePresenter;
    private int fileIndex;

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        addImagePresenter.clearImage(fileIndex);
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable throwable) {
        addImagePresenter.clearImage(fileIndex);
        throwable.printStackTrace();
    }
}
