package com.robandboo.fq.dto;

public class Answer {
    private String message;

    public Answer() {
    }

    public Answer(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "message='" + message + '\'' +
                '}';
    }
}
