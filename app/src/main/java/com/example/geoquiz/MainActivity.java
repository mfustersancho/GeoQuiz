package com.example.geoquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;

    private static final String KEY_INDEX = "index";
    private static final String KEY_ANSWER = "answers";

    private final Question[] mQuestionBank;

    private char[] mAnswer;

    private int mCurrentIndex;

    public MainActivity() {
        mQuestionBank = new Question[] {
                new Question(R.string.question_australia, true),
                new Question(R.string.question_oceans, true),
                new Question(R.string.question_mideast, false),
                new Question(R.string.question_africa, false),
                new Question(R.string.question_americas, true),
                new Question(R.string.question_asia, true),
        };
        mAnswer = new char[mQuestionBank.length];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX);
            mAnswer = savedInstanceState.getCharArray(KEY_ANSWER);
        } else {
            mCurrentIndex = 0;
            resetAnswerArray();
        }

        mQuestionTextView = findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(view -> showNextQuestion());

        mTrueButton = findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(view -> checkAnswer(true));

        mFalseButton = findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(view -> checkAnswer(false));

        mNextButton = findViewById(R.id.next_button);
        mNextButton.setOnClickListener(view -> showNextQuestion());

        mPrevButton = findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(view -> {
            mCurrentIndex = (mCurrentIndex - 1);
            if (mCurrentIndex < 0)
                mCurrentIndex = mQuestionBank.length - 1;
            updateQuestion();
        });

        updateQuestion();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_INDEX, mCurrentIndex);
        outState.putCharArray(KEY_ANSWER, mAnswer);
    }

    private void showNextQuestion() {
        mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
        updateQuestion();
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);

        if(mAnswer[mCurrentIndex] == 0) {
            mTrueButton.setEnabled(true);
            mFalseButton.setEnabled(true);
        } else {
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);
        }
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId;

        if(userPressedTrue == answerIsTrue) {
            messageResId = R.string.correct_toast;
            mAnswer[mCurrentIndex] = 'v';
        } else {
            messageResId= R.string.incorrect_toast;
            mAnswer[mCurrentIndex] = 'x';
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();

        testFinale();

        showNextQuestion();

    }

    private void testFinale() {
        int rightAnswers = 0;
        float percentage;

        for (char c : mAnswer) {
            if (c == 0) {
                return;
            } else if (c == 'v') {
                rightAnswers++;
            }
        }

        percentage = rightAnswers * 100.0f / mAnswer.length;
        Toast.makeText(this, "Right answers: " + String.format("%.2f", percentage) + "%", Toast.LENGTH_SHORT).show();

        resetAnswerArray();
    }

    private void resetAnswerArray() {
        Arrays.fill(mAnswer, (char) 0);
    }
}