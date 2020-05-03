package com.robandboo.fq.watcher;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.robandboo.fq.R;
import com.robandboo.fq.chain.ChainManager;
import com.robandboo.fq.chain.bridge.IDataBridgeStart;
import com.robandboo.fq.dto.Question;
import com.robandboo.fq.localdata.repository.MyQuestionsLocalRepository;
import com.robandboo.fq.service.NetworkSingleton;
import com.robandboo.fq.service.QuestionService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionTextEnterWatcher implements TextWatcher {
    private EditText editText;

    private ChainManager chainManager;

    public QuestionTextEnterWatcher(
            EditText editText,
            ChainManager chainManager
    ) {
        this.editText = editText;
        this.chainManager = chainManager;
    }

    @Override
    public void beforeTextChanged(CharSequence sequence, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence sequence, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable text) {
        if (text.toString().contains("\n")) {
            text.toString().replaceAll("\n", "");
            chainManager.next();
        }
    }
}
