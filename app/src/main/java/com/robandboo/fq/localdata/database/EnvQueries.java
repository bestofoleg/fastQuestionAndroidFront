package com.robandboo.fq.localdata.database;

public interface EnvQueries {
    String CREATE_QUESTIONS_TABLE_QUERY =
            "CREATE TABLE " + Constants.MY_QUESTIONS_TABLE_NAME + "(" +
            "   LOCAL_ID INTEGER NOT NULL AUTOINCREMENT PRIMARY KEY," +
            "   SERVER_ID INTEGER NOT NULL" +
            ");";

    String DROP_MY_QUESTIONS = "DROP TABLE " + Constants.MY_QUESTIONS_TABLE_NAME + ";";
}
