package com.robandboo.fq.watcher;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.robandboo.fq.chain.ChainManager;

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
            editText.setText(text.toString().replaceAll("\n", ""));
            chainManager.next();
        }
    }
}
