package com.robandboo.fq.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Answer {
    private Long id;

    private String text;

    private Question question;

    public Answer(String text) {
        this.text = text;
    }
}
