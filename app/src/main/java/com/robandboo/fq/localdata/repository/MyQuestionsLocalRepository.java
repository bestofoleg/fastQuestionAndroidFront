package com.robandboo.fq.localdata.repository;

import com.robandboo.fq.dto.Question;
import com.robandboo.fq.localdata.constant.LocalRepositoryConstants;
import com.robandboo.fq.localdata.entity.MyQuestionsConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MyQuestionsLocalRepository {
    private MyQuestionsConfig readMyQuestionsConfig() {
        MyQuestionsConfig myQuestionsConfig = null;
        StringBuffer configTextBuffer = new StringBuffer();
        File file = new File(
                LocalRepositoryConstants.MY_QUESTIONS_PATH +
                        LocalRepositoryConstants.MY_QUESTIONS_DATA_CONFIG
        );
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (
                InputStream inputStream = new FileInputStream(file);
                BufferedReader bufferedReader =
                        new BufferedReader(new InputStreamReader(inputStream))
        ) {
            String currentLine = "";
            while ((currentLine = bufferedReader.readLine()) != null) {
                configTextBuffer.append(currentLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            myQuestionsConfig = new MyQuestionsConfig();
            if (configTextBuffer.length() > 0) {
                JSONObject jsonObject = getJSONObjectByString(configTextBuffer.toString());
                try {
                    myQuestionsConfig.setPageNumber(jsonObject.getInt("pageNumber"));
                    myQuestionsConfig.setQuestionNumberInPage(
                            jsonObject.getInt("questionNumberInPage")
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return myQuestionsConfig;
    }

    public void writeMyQuestionsConfig(MyQuestionsConfig myQuestionsConfig) {
        File file = new File(LocalRepositoryConstants.MY_QUESTIONS_PATH +
                LocalRepositoryConstants.MY_QUESTIONS_DATA_CONFIG);
        JSONObject jsonConfigObject = new JSONObject();
        try {
            jsonConfigObject.put("pageNumber", myQuestionsConfig.getPageNumber());
            jsonConfigObject.put("questionNumberInPage", myQuestionsConfig.getQuestionNumberInPage());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (
                OutputStream outputStream = new FileOutputStream(file);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)
        ) {
            bufferedWriter.write(jsonConfigObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JSONObject getJSONObjectByString(String str) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(str);
        } catch (JSONException e) {
            e.printStackTrace();
            try {
                jsonObject = new JSONObject("{}");
            } catch (JSONException eInside) {
                eInside.printStackTrace();
            }
        } finally {
        }
        return jsonObject;
    }

    public void writeQuestion(Question question) {
        MyQuestionsConfig myQuestionsConfig = readMyQuestionsConfig();
        String fileName = String.format(LocalRepositoryConstants.MY_QUESTIONS_PATH +
                LocalRepositoryConstants.MY_QUESTION_PAGE_NAME, myQuestionsConfig.getPageNumber());
        File file = null;
        if (myQuestionsConfig.getQuestionNumberInPage() >= LocalRepositoryConstants.MAX_MY_QUESTIONS_QUANTITY_IN_PAGE) {
            file = createNewQuestionsPageFile(myQuestionsConfig.getPageNumber() + 1);
        } else {
            file = getMyQuestionsPageFileByNumber(myQuestionsConfig.getPageNumber());
        }
        JSONObject questionJsonObject = new JSONObject();
        try {
            questionJsonObject.put("id", question.getId());
            questionJsonObject.put("text", question.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try (
                OutputStream outputStream = new FileOutputStream(file);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)
        ) {
            bufferedWriter.write(questionJsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Question> readAllQuestionsFromPage(int page) {
        List <Question> questions = new ArrayList<>();
        StringBuffer jsonBuffer = new StringBuffer();
        File file = getMyQuestionsPageFileByNumber(page);
        try (
                InputStream inputStream = new FileInputStream(file);
                BufferedReader bufferedReader =
                        new BufferedReader(new InputStreamReader(inputStream))
        ){
            String temp = "";
            while ((temp = bufferedReader.readLine()) != null) {
                jsonBuffer.append(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(jsonBuffer);
            } catch (JSONException e) {
                try {
                    jsonArray = new JSONArray("[]");
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace();
                try {
                    for (int i = 0;i < jsonArray.length();i ++) {
                        questions.add((Question) jsonArray.get(i));
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return questions;
    }

    private File createNewQuestionsPageFile(int newPageNumber) {
        File file = new File(String.format(LocalRepositoryConstants.MY_QUESTIONS_PATH +
                        LocalRepositoryConstants.MY_QUESTION_PAGE_NAME, newPageNumber));
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private File getMyQuestionsPageFileByNumber(int pageNumber) {
        File file = new File(String.format(LocalRepositoryConstants.MY_QUESTIONS_PATH +
                LocalRepositoryConstants.MY_QUESTION_PAGE_NAME, pageNumber));
        if (!file.exists()) {
            throw new IllegalArgumentException(String.format(
                    LocalRepositoryConstants.PAGE_IS_NOT_EXISTS_ERROR_MESSAGE, pageNumber
            ));
        }
        return file;
    }
}
