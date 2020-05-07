package com.robandboo.fq.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuestionFile {
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("fileName")
    @Expose
    private String fileName;

    @SerializedName("fileType")
    @Expose
    private String fileType;

    @SerializedName("data")
    @Expose
    private String data;

    public QuestionFile() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
