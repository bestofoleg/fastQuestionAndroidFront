package com.robandboo.fq.util.validation;

public interface IValidation <T>{
    boolean validate();
    boolean validateWithoutToast();
    void setDataForValidation(T data,  boolean isVote);
    void setDataForValidation(String data);
}
