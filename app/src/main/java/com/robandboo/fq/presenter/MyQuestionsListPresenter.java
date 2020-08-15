package com.robandboo.fq.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.robandboo.fq.MainActivity;
import com.robandboo.fq.R;
import com.robandboo.fq.callback.GetAnswersForSingleMyQuestionCallback;
import com.robandboo.fq.dto.Question;
import com.robandboo.fq.dto.QuestionFile;
import com.robandboo.fq.localdata.repository.MyQuestionsLocalRepository;
import com.robandboo.fq.service.AnswerService;
import com.robandboo.fq.service.FileService;
import com.robandboo.fq.service.NetworkSingleton;
import com.robandboo.fq.service.QuestionService;
import com.robandboo.fq.ui.adapter.TopicExpandableListAdapter;
import com.robandboo.fq.util.enumeration.QuestionType;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyQuestionsListPresenter implements ILayoutPresenter<LinearLayout> {
    private AnswerService answerService;

    private LinearLayout myQuestionsListRootLayout;

    private MyQuestionsLocalRepository myQuestionsLocalRepository;

    private ExpandableListView topicsExpandableListView;

    private TopicExpandableListAdapter topicExpandableListAdapter;

    private LinearLayout single;

    private LinearLayout questionsList;

    private TextView singleQuestionTitle;

    private ImageView singleImageView1;

    private ImageView singleImageView2;

    private TextView singleVote1;

    private TextView singleVote2;

    private LinearLayout answersListLayout;

    private Question currentSinglePageQuestion;

    private QuestionService questionService;

    @Getter
    private String filePath1;

    @Getter
    private String filePath2;

    private FileService fileService;

    public MyQuestionsListPresenter(
            LinearLayout myQuestionsListRootLayout,
            LinearLayout single,
            LinearLayout questionsList
    ) {
        fileService = NetworkSingleton.getInstance()
                .getRetrofit()
                .create(FileService.class);
        questionService = NetworkSingleton.getInstance()
                .getRetrofit()
                .create(QuestionService.class);
        singleQuestionTitle = single.findViewById(R.id.questionText);
        singleImageView1 = single.findViewById(R.id.imageView1);
        singleImageView2 = single.findViewById(R.id.imageView2);
        singleVote1 = single.findViewById(R.id.image1VotesText);
        singleVote2 = single.findViewById(R.id.image2VotesText);
        answersListLayout = single.findViewById(R.id.answersContent);
        this.questionsList = questionsList;
        this.single = single;
        single.setVisibility(View.GONE);
        questionsList.setVisibility(View.VISIBLE);
        this.myQuestionsListRootLayout = myQuestionsListRootLayout;
        answerService = NetworkSingleton.getInstance()
                .getRetrofit().create(AnswerService.class);
        myQuestionsLocalRepository =
                new MyQuestionsLocalRepository(myQuestionsListRootLayout.getContext());
        /*
        topicsExpandableListView =
                new ExpandableListView(myQuestionsListRootLayout.getContext());
        myQuestionsListRootLayout.addView(topicsExpandableListView);
        topicExpandableListAdapter =
                new TopicExpandableListAdapter(new ArrayList<>());
        topicsExpandableListView.setAdapter(topicExpandableListAdapter);
        topicsExpandableListView
                .setOnGroupExpandListener(groupPosition -> {
                    topicExpandableListAdapter.updateTopicFromServerByGroupId(groupPosition);
                });*/
    }

    private void addAllQuestionTopics(LinearLayout root, List<Question> questions) {
        questions.forEach(question -> {
            View topic = MainActivity.MAIN_INFLATER.inflate(
                    R.layout.topic_question_layout, null, false
            ).getRootView();
            String hiddenMulidot = topic.getContext().getResources()
                    .getString(R.string.hiddenMulidot);
            TextView questionTextView = topic.findViewById(R.id.topicQuestion);
            LinearLayout.LayoutParams questionTextViewLayoutParams =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
            questionTextViewLayoutParams.setMargins(10, 10, 0, 0);
            questionTextView.setLayoutParams(questionTextViewLayoutParams);
            questionTextView.setTextColor(Color.BLACK);
            String questionText = question.getText();
            String questionTitle = "";
            if (questionText.length() > 30) {
                questionTitle =
                        questionText.substring(0, questionText.length() % 27) + hiddenMulidot;
            } else {
                questionTitle = questionText;
            }
            if (StringUtils.isBlank(questionText)) {
                questionTitle = "[в вопросе нет текста]";
            }
            questionTextView.setText(questionTitle);
            topic.setOnClickListener(view -> {
                questionsList.setVisibility(View.GONE);
                single.setVisibility(View.VISIBLE);
                loadAllInSingleQuestionPage(question);
                updateAnswersOnSingleQuestionTopic(question);
            });
            root.addView(topic);
        });
    }

    private void loadAllInSingleQuestionPage(Question question) {
        singleQuestionTitle.setText(question.getText());
        filePath1 = question.getFilePath1();
        filePath2 = question.getFilePath2();
        File file1 = new File(filePath1 != null ? filePath1 : "");
        File file2 = new File(filePath2 != null ? filePath2 : "");
        if (file1.exists()) {
            Glide
                    .with(singleImageView1)
                    .load(file1)
                    .into(singleImageView1);
            singleImageView1.setVisibility(View.VISIBLE);
        } else {
            singleImageView1.setVisibility(View.GONE);
        }
        if (file2.exists()) {
            Glide
                    .with(singleImageView2)
                    .load(file2)
                    .into(singleImageView2);
            singleImageView2.setVisibility(View.VISIBLE);
        } else {
            singleImageView2.setVisibility(View.GONE);
        }
    }

    public void loadTopicsFromPage(int page) {
        myQuestionsListRootLayout.removeAllViews();
        List<Question> questions =
                myQuestionsLocalRepository.readAllQuestionsFromPage(page);
        addAllQuestionTopics(myQuestionsListRootLayout, questions);
    }

    public void updateCurrentSingleQuestionPage() {
        if (currentSinglePageQuestion != null) {
            updateAnswersOnSingleQuestionTopic(currentSinglePageQuestion);
        }
    }

    private void updateAnswersOnSingleQuestionTopic(Question question) {
        answersListLayout.removeAllViews();
        Long questionId = question.getId();
        currentSinglePageQuestion = question;
        if (!QuestionType.VOTE.isA(question.getQuestionType())) {
            GetAnswersForSingleMyQuestionCallback getAnswersForSingleMyQuestionCallback =
                    GetAnswersForSingleMyQuestionCallback.builder()
                            .answersList(answersListLayout).build();
            answerService.getAnswerByQuestionId(questionId)
                    .enqueue(getAnswersForSingleMyQuestionCallback);
            singleVote1.setText("");
            singleVote2.setText("");
            singleVote1.setVisibility(View.GONE);
            singleVote2.setVisibility(View.GONE);
        } else {
            questionService.getQuestionById(questionId).enqueue(new Callback<Question>() {
                @Override
                public void onResponse(Call<Question> call, Response<Question> response) {
                    if (response.body() != null) {
                        Map<Long, Long> fileIds = response.body().getFileIds();
                        if (fileIds != null) {
                            loadFilesAndVotesByFileId(fileIds);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Question> call, Throwable t) {

                }
            });
        }
    }

    private void loadFilesAndVotesByFileId(Map <Long, Long> fileIdToVotesQuantity) {
        List<Map.Entry<Long, Long>> idsToVotesList = new ArrayList<>(fileIdToVotesQuantity.entrySet());
        Map.Entry<Long, Long> firstEntry = idsToVotesList.get(0);
        fileService.getFileById(firstEntry.getKey()).enqueue(new Callback<QuestionFile>() {
            @Override
            public void onResponse(Call<QuestionFile> call, Response<QuestionFile> response) {
                if (response.body() != null) {
                    QuestionFile questionFile = response.body();
                    byte[] bytes = Base64.decode(
                            questionFile.getData().getBytes(),
                            Base64.DEFAULT
                    );
                    Bitmap bitmap = BitmapFactory.decodeByteArray(
                            bytes, 0, bytes.length
                    );
                    Glide.with(singleImageView1)
                            .load(bitmap)
                            .into(singleImageView1);
                    singleVote1.setText(String.valueOf(firstEntry.getValue()));
                }
            }

            @Override
            public void onFailure(Call<QuestionFile> call, Throwable t) {}
        });
        Map.Entry<Long, Long> secondEntry = idsToVotesList.get(1);
        fileService.getFileById(secondEntry.getKey()).enqueue(new Callback<QuestionFile>() {
            @Override
            public void onResponse(Call<QuestionFile> call, Response<QuestionFile> response) {
                if (response.body() != null) {
                    QuestionFile questionFile = response.body();
                    byte[] bytes = Base64.decode(
                            questionFile.getData().getBytes(),
                            Base64.DEFAULT
                    );
                    Bitmap bitmap = BitmapFactory.decodeByteArray(
                            bytes, 0, bytes.length
                    );
                    Glide.with(singleImageView2)
                            .load(bitmap)
                            .into(singleImageView2);
                    singleVote2.setText(String.valueOf(secondEntry.getValue()));
                }
            }

            @Override
            public void onFailure(Call<QuestionFile> call, Throwable t) {}
        });
    }

    @Override
    public void focus() {
        myQuestionsListRootLayout.requestFocus();
    }

    @Override
    public LinearLayout getRootLayout() {
        return myQuestionsListRootLayout;
    }

    @Override
    public void setLayoutVisibility(boolean visibility) {
        if (visibility) {
            myQuestionsListRootLayout.setVisibility(View.VISIBLE);
        } else {
            myQuestionsListRootLayout.setVisibility(View.GONE);
        }
    }
}


