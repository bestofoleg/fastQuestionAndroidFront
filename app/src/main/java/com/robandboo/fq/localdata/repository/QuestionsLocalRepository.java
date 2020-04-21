package com.robandboo.fq.localdata.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.robandboo.fq.dto.Question;
import com.robandboo.fq.localdata.database.Constants;
import com.robandboo.fq.localdata.database.DBHelper;

public class QuestionsLocalRepository implements ILocalDataRepository <Question> {
    private DBHelper dbHelper;

    private SQLiteDatabase database;

    public QuestionsLocalRepository(Context context) {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    @Override
    public void save(Question data) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.MY_QUESTIONS_TABLE_SERVER_ID_PARAM, data.getId());
        database.insert(
                Constants.MY_QUESTIONS_TABLE_NAME,
                null,
                contentValues
        );
    }

    @Override
    public void deleteByLocalId(int local_id) {
        database.delete(
                Constants.MY_QUESTIONS_TABLE_NAME,
                Constants.MY_QUESTIONS_TABLE_LOCAL_ID_PARAM + " = " + local_id,
                null
        );
    }

    @Override
    public void deleteByServerId(int server_id) {
        database.delete(
                Constants.MY_QUESTIONS_TABLE_NAME,
                Constants.MY_QUESTIONS_TABLE_SERVER_ID_PARAM + " = " + server_id,
                null
        );
    }

    @Override
    public int loadLocalIdByServerId(int serverId) {
        Cursor cursor = database.query(
                Constants.MY_QUESTIONS_TABLE_NAME,
                new String[]{Constants.MY_QUESTIONS_TABLE_SERVER_ID_PARAM},
                Constants.MY_QUESTIONS_TABLE_SERVER_ID_PARAM + " " + serverId,
                null,
                null,
                null,
                null
        );
        return cursor.getColumnIndex(Constants.MY_QUESTIONS_TABLE_SERVER_ID_PARAM);
    }

    @Override
    public int loadServerIdByLocalId(int localId) {
        Cursor cursor = database.query(
                Constants.MY_QUESTIONS_TABLE_NAME,
                new String[]{Constants.MY_QUESTIONS_TABLE_LOCAL_ID_PARAM},
                Constants.MY_QUESTIONS_TABLE_LOCAL_ID_PARAM + " = " + localId,
                null,
                null,
                null,
                null
        );
        return cursor.getColumnIndex(Constants.MY_QUESTIONS_TABLE_LOCAL_ID_PARAM);
    }
}
