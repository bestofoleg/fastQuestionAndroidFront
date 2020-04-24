package com.robandboo.fq.localdata.repository;

import android.content.Context;

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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MyQuestionsLocalRepository {
    private Context context;

    public MyQuestionsLocalRepository(Context context) {
        this.context = context;
    }

    public MyQuestionsConfig readMyQuestionsConfig() {
        MyQuestionsConfig myQuestionsConfig = null;
        StringBuffer configTextBuffer = new StringBuffer();
        File dir = new File(context.getFilesDir(), LocalRepositoryConstants.MY_QUESTIONS_PATH);
        dir.mkdirs();
        File file = new File(
                dir,
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

    private void writeMyQuestionsConfig(MyQuestionsConfig myQuestionsConfig) {
        File dir = new File(context.getFilesDir(), LocalRepositoryConstants.MY_QUESTIONS_PATH);
        File file = new File(dir, LocalRepositoryConstants.MY_QUESTIONS_DATA_CONFIG);
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
        File file = null;
        List<Question> questions = new ArrayList<>();
        if (myQuestionsConfig.getQuestionNumberInPage() >= LocalRepositoryConstants.MAX_MY_QUESTIONS_QUANTITY_IN_PAGE) {
            file = createNewQuestionsPageFile(myQuestionsConfig.getPageNumber() + 1);
            myQuestionsConfig.setQuestionNumberInPage(0);
            myQuestionsConfig.setPageNumber(myQuestionsConfig.getPageNumber() + 1);
        } else {
            file = getMyQuestionsPageFileByNumber(myQuestionsConfig.getPageNumber());
            myQuestionsConfig.setQuestionNumberInPage(
                    myQuestionsConfig.getQuestionNumberInPage() + 1
            );
            questions.addAll(readAllQuestionsFromPage(myQuestionsConfig.getPageNumber()));
        }
        questions.add(question);
        JSONArray questionJsonObjects = new JSONArray(convertListToJSONObjectsList(questions));
        try (
                OutputStream outputStream = new FileOutputStream(file);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)
        ) {
            bufferedWriter.write(questionJsonObjects.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        writeMyQuestionsConfig(myQuestionsConfig);
    }

    private List<JSONObject> convertListToJSONObjectsList(List<Question> questions) {
        List<JSONObject> objects = new ArrayList<>();
        try {
            for (Question question : questions) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", question.getId());
                jsonObject.put("text", question.getText());
                objects.add(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objects;
    }

    public List<Question> readAllQuestionsFromPage(int page) {
        List<Question> questions = new ArrayList<>();
        StringBuffer jsonBuffer = new StringBuffer();
        File file = getMyQuestionsPageFileByNumber(page);
        try (
                InputStream inputStream = new FileInputStream(file);
                BufferedReader bufferedReader =
                        new BufferedReader(new InputStreamReader(inputStream))
        ) {
            String temp = "";
            while ((temp = bufferedReader.readLine()) != null) {
                jsonBuffer.append(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            JSONArray jsonArray = null;
            try {
                if (jsonBuffer.length() > 0) {
                    jsonArray = new JSONArray(jsonBuffer.toString());
                }
            } catch (JSONException e) {
                try {
                    jsonArray = new JSONArray("[]");
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace();
            }
            try {
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = (JSONObject) jsonArray.get(i);
                        questions.add(new Question(
                                object.getInt("id"),
                                object.getString("text")
                        ));
                    }
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        return questions;
    }

    private File createNewQuestionsPageFile(int newPageNumber) {
        File dir = new File(context.getFilesDir(), LocalRepositoryConstants.MY_QUESTIONS_PATH);
        dir.mkdirs();
        File file = new File(
                dir,
                MessageFormat.format(
                        LocalRepositoryConstants.MY_QUESTION_PAGE_NAME,
                        newPageNumber
                )
        );
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private File getMyQuestionsPageFileByNumber(int pageNumber) {
        File dir = new File(
                context.getFilesDir(),
                LocalRepositoryConstants.MY_QUESTIONS_PATH
        );
        dir.mkdirs();
        File file = new File(dir, MessageFormat.format(
                LocalRepositoryConstants.MY_QUESTION_PAGE_NAME,
                pageNumber
        ));
        if (!file.exists()) {
            if (pageNumber == 0) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                throw new IllegalArgumentException(MessageFormat.format(
                        LocalRepositoryConstants.PAGE_IS_NOT_EXISTS_ERROR_MESSAGE +
                                " " + file.getAbsolutePath(),
                        pageNumber
                ));
            }
        }
        return file;
    }
}
