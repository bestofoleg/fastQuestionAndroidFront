package com.robandboo.fq.localdata.entity;

public class MyQuestionsConfig {
    private int pageNumber;

    private int questionNumberInPage;

    public MyQuestionsConfig() {
        pageNumber = 1;
        questionNumberInPage = 0;
    }

    public MyQuestionsConfig(int pageNumber, int questionNumberInPage) {
        this.pageNumber = pageNumber;
        this.questionNumberInPage = questionNumberInPage;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getQuestionNumberInPage() {
        return questionNumberInPage;
    }

    public void setQuestionNumberInPage(int questionNumberInPage) {
        this.questionNumberInPage = questionNumberInPage;
    }
}
