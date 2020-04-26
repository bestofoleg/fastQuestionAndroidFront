package com.robandboo.fq.util.validation;

public interface IValidation <T>{
    boolean validate();
    boolean validateWithoutToast();
    void setDataForValidation(T data);
}
