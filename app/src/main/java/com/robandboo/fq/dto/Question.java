package com.robandboo.fq.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Question {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("answers")
    @Expose
    private List<Answer> answers;

    @SerializedName("filePath1")
    @Expose
    private String filePath1;

    @SerializedName("filePath2")
    @Expose
    private String filePath2;

    public Question() {
    }

    public Question(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public Question(int id, String text, String filePath1, String filePath2) {
        this.id = id;
        this.text = text;
        this.filePath1 = filePath1;
        this.filePath2 = filePath2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getFilePath1() {
        return filePath1;
    }

    public void setFilePath1(String filePath1) {
        this.filePath1 = filePath1;
    }

    public String getFilePath2() {
        return filePath2;
    }

    public void setFilePath2(String filePath2) {
        this.filePath2 = filePath2;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", answers=" + answers +
                ", filePath1='" + filePath1 + '\'' +
                ", filePath2='" + filePath2 + '\'' +
                '}';
    }
}
