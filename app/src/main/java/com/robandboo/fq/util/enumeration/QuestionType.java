package com.robandboo.fq.util.enumeration;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum QuestionType {
    TEXT("TEXT"),
    VOTE("VOTE");

    private final String value;

    public boolean isA(QuestionType questionType) {
        return this == questionType;
    }

    public boolean isA(String questionType) {
        return value.equals(questionType);
    }

    @Override
    public String toString() {
        return value;
    }
}
