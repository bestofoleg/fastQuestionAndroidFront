package com.robandboo.fq.localdata.repository;

import android.content.Context;

import com.robandboo.fq.R;
import com.robandboo.fq.dto.Question;
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

    private String pageNumberParamName;

    private String questionNumberInPageParamName;

    private String idParamName;

    private String textParamName;

    private String emptyJsonObject;

    private String emptyJsonArray;

    private String myQuestionsPath;

    private String myQuestionsDataConfig;

    private String pageIsNotExistsErrorMessage;

    private int maxQuestionsQuantityInPage;

    private String myQuestionPageName;

    private String filePath1ParamName;

    private String filePath2ParamName;

    private String questionTypeParamName;

    public MyQuestionsLocalRepository(Context context) {
        this.context = context;
        pageNumberParamName =
                context.getResources().getString(R.string.pageNumberParamName);
        questionTypeParamName =
                context.getString(R.string.questionTypeParam);
        questionNumberInPageParamName =
                context.getResources().getString(R.string.questionNumberInPageParamName);
        idParamName =
                context.getResources().getString(R.string.idParamName);
        textParamName =
                context.getResources().getString(R.string.textParamName);
        emptyJsonObject =
                context.getResources().getString(R.string.emptyJsonObject);
        emptyJsonArray =
                context.getResources().getString(R.string.emptyJsonArray);
        myQuestionsPath =
                context.getResources().getString(R.string.myQuestionsPath);
        myQuestionsDataConfig =
                context.getResources().getString(R.string.myQuestionDataConfig);
        pageIsNotExistsErrorMessage =
                context.getResources().getString(R.string.pageIsNotExistsErrorMessage);
        maxQuestionsQuantityInPage =
                context.getResources().getInteger(R.integer.maxMyQuestionsQuantityInPage);
        myQuestionPageName =
                context.getResources().getString(R.string.myQuestionPageName);
        filePath1ParamName =
                context.getResources().getString(R.string.filePath1ParamName);
        filePath2ParamName =
                context.getResources().getString(R.string.filePath2ParamName);
    }

    public void clearAllData() {
        MyQuestionsConfig myQuestionsConfig = readMyQuestionsConfig();
        int savedPagesQuantity = myQuestionsConfig.getPageNumber();
        File dir = new File(context.getFilesDir(), myQuestionsPath);
        File configFile = new File(dir, myQuestionsDataConfig);
        configFile.delete();
        for (int i = 0; i < savedPagesQuantity; i++) {
            File questionPageFile = new File(
                    dir,
                    MessageFormat.format(
                            myQuestionPageName,
                            i+1
                    )
            );
            questionPageFile.delete();
        }
    }

    public MyQuestionsConfig readMyQuestionsConfig() {
        MyQuestionsConfig myQuestionsConfig = null;
        StringBuffer configTextBuffer = new StringBuffer();
        File dir = new File(context.getFilesDir(), myQuestionsPath);
        dir.mkdirs();
        File file = new File(
                dir,
                myQuestionsDataConfig
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
            String currentLine;
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
                    myQuestionsConfig.setPageNumber(jsonObject.getInt(pageNumberParamName));
                    myQuestionsConfig.setQuestionNumberInPage(
                            jsonObject.getInt(questionNumberInPageParamName)
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return myQuestionsConfig;
    }

    private void writeMyQuestionsConfig(MyQuestionsConfig myQuestionsConfig) {
        File dir = new File(context.getFilesDir(), myQuestionsPath);
        File file = new File(dir, myQuestionsDataConfig);
        JSONObject jsonConfigObject = new JSONObject();
        try {
            jsonConfigObject.put(pageNumberParamName, myQuestionsConfig.getPageNumber());
            jsonConfigObject.put(questionNumberInPageParamName, myQuestionsConfig.getQuestionNumberInPage());
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
                jsonObject = new JSONObject(emptyJsonObject);
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
        if (myQuestionsConfig.getQuestionNumberInPage() >= maxQuestionsQuantityInPage) {
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
                jsonObject.put(idParamName, question.getId());
                jsonObject.put(textParamName, question.getText());
                jsonObject.put(filePath1ParamName, question.getFilePath1());
                jsonObject.put(filePath2ParamName, question.getFilePath2());
                jsonObject.put(questionTypeParamName, question.getQuestionType());
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
            String temp;
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
                    jsonArray = new JSONArray(emptyJsonArray);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace();
            }
            try {
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = (JSONObject) jsonArray.get(i);
                        /*object.put(filePath1ParamName, "");
                        object.put(filePath2ParamName, "");*/
                        questions.add(new Question(
                                object.getInt(idParamName),
                                object.getString(textParamName),
                                (String) checkParameterAndGetByName(object, filePath1ParamName),
                                (String) checkParameterAndGetByName(object, filePath2ParamName),
                                object.getString(questionTypeParamName)
                        ));
                    }
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        return questions;
    }

    private Object checkParameterAndGetByName(JSONObject object, String name) {
        if (object.has(name)) {
            try {
                return object.get(name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private File createNewQuestionsPageFile(int newPageNumber) {
        File dir = new File(context.getFilesDir(), myQuestionsPath);
        dir.mkdirs();
        File file = new File(
                dir,
                MessageFormat.format(
                        myQuestionPageName,
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
                myQuestionsPath
        );
        dir.mkdirs();
        File file = new File(dir, MessageFormat.format(
                myQuestionPageName,
                pageNumber
        ));
        if (!file.exists()) {
            if (pageNumber == 1) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                throw new IllegalArgumentException(MessageFormat.format(
                        pageIsNotExistsErrorMessage +
                                file.getAbsolutePath(),
                        pageNumber
                ));
            }
        }
        return file;
    }
}
