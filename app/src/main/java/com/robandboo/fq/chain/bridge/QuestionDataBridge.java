package com.robandboo.fq.chain.bridge;

import com.robandboo.fq.dto.Question;

public class QuestionDataBridge implements
        IDataBridgeStart<Question>, IDataBridgeFinish <Question> {
    private Question question;

    @Override
    public void setData(Question data) {
        question = data;
    }

    @Override
    public Question getData() {
        return question;
    }
}
