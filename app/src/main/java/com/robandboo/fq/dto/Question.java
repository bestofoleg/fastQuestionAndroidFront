package com.robandboo.fq.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Question {
    private Integer id;

    private String text;

    private List<Answer> answers;

    private String filePath1;

    private String filePath2;

    private String questionType;

    private Map<Long, Long> fileIds;

    public Question(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public Question(int id, String text, String filePath1, String filePath2, String questionType) {
        this.id = id;
        this.text = text;
        this.filePath1 = filePath1;
        this.filePath2 = filePath2;
        this.questionType = questionType;
    }
}
