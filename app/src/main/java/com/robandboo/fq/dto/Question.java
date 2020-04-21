package com.robandboo.fq.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.robandboo.fq.localdata.entity.ILocalData;

import java.util.List;

public class Question implements ILocalData {
    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("answers")
    @Expose
    private List<Answer> answers;

    public Question() {
    }

    public Question(long id, String text) {
        this.id = id;
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", answers=" + answers +
                '}';
    }
}
