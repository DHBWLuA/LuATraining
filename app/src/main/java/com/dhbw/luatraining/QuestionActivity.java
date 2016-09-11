package com.dhbw.luatraining;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestionActivity extends BaseActivity
{
    private KeyboardView mKeyboardView;
    private List<Question> questions = new ArrayList<>();
    private Question currentQuestion;

    // becomes true when user clicks next to see whether answers are correct
    // becomes false when user clicks next to see zhe next question
    private Boolean isResultMode;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_question);

            drawKeyBoard();

            // Hide the standard keyboard initially
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            drawEditText((EditText) findViewById(R.id.answerText));

            Integer ChapterNo = getIntent().getIntExtra("ChapterNo", 0);

            loadQuestions(ChapterNo);
            if (questions.size() > 0)
                loadAnswers(ChapterNo);

            showRandomQuestion();
        }
        catch (Exception e)
        {
            LogHelper.addLogLine("Exception bei QuestionActivity.OnCreate: " + e.toString());
        }
    }

    private void showRandomQuestion()
    {
        try
        {
            isResultMode = false;

            // remove alle checkboxes from previous question
            LinearLayout answers = (LinearLayout) findViewById(R.id.answer);
            for (int i = answers.getChildCount() - 1; i > 0; i--)
            {
                View v = answers.getChildAt(i);
                if (v.getClass() == CustomCheckbox.class)
                    answers.removeView(v);
            }

            TextView questionText = (TextView) findViewById(R.id.questionText);
            if (questions.size() == 0)
            {
                questionText.setText(R.string.all_questions_answered);
                answers.setVisibility(View.GONE);
                ((Button)findViewById(R.id.btnNext)).setVisibility(View.GONE);
                hideCustomKeyboard();
                return;
            }
            answers.setVisibility(View.VISIBLE);

            int nextQuestion = 0;
            if (questions.size() > 1)
            {
                Random r = new Random();
                nextQuestion = r.nextInt(questions.size() - 1);
            }
            currentQuestion = questions.get(nextQuestion);

            questionText.setText(currentQuestion.Text);
            String IId = currentQuestion.ImageId;
            if (IId != null && !IId.equals(""))
            {
                try
                {
                    String sql = "SELECT Bild FROM Bild WHERE BildId='" + IId + "'";
                    Cursor curs = new DataBaseHelper(this, SQLiteDatabase.OPEN_READONLY).queryBySql(sql);
                    curs.moveToFirst();
                    Object img = curs.getString(0);

                    ImageView questionImage = (ImageView) findViewById(R.id.questionImage);
                    questionImage.setImageDrawable((Drawable) img);
                }
                catch (Exception e)
                {
                    LogHelper.addLogLine("Exception bei QuestionActivity bei loadImage: " + e.toString());
                }
            }

            EditText answerText = (EditText) findViewById(R.id.answerText);
            if (currentQuestion.answers.size() > 1)
            {
                // multiple choice question
                answerText.setVisibility(View.GONE);

                answers.setPadding(20, 0, 0, 0);
                for (int i = 0; i < currentQuestion.answers.size(); i++)
                {
                    CustomCheckbox chk = new CustomCheckbox(this);
                    chk.setText(currentQuestion.answers.get(i).Text);
                    chk.setId(i);
                    answers.addView(chk);
                }
            }
            else
            {
                // text question
                answerText.setVisibility(View.VISIBLE);
                answerText.setText("");
                answerText.setBackgroundColor(Color.WHITE);
                answers.setPadding(0, 0, 0, 0);
                answerText.requestFocus();
                showCustomKeyboard(null);
            }
        }
        catch (Exception e)
        {
            LogHelper.addLogLine("Exception bei QuestionActivity.showRandomQuestion: " + e.toString());
        }
    }

    private void loadAnswers(Integer chapterNo)
    {
        try
        {

            String sql = "SELECT _id, Antwort, Richtigkeit FROM Antwort WHERE _id IN ";
            if (chapterNo > 0)
            {
                sql = sql + "(SELECT _id FROM Frage WHERE Antwort='0' AND KapitelNr='" + chapterNo + "')";
            }
            else
            {
                sql = sql + "(SELECT _id FROM Frage WHERE Antwort='0')";
            }


            Cursor curs = new DataBaseHelper(this, SQLiteDatabase.OPEN_READONLY).queryBySql(sql);
            if (curs.getCount() == 0)
            {
                LogHelper.addLogLine("Zum angegebenen Kapitel wurden keine Antworten gefunden.");
                curs.close();
                return;
            }

            curs.moveToFirst();

            do
            {
                for (int i = 0; i < questions.size(); i++)
                {
                    if (Integer.parseInt(questions.get(i).Id) == Integer.parseInt(curs.getString(0)))
                    {
                        questions.get(i).addAnswer(curs.getString(1), curs.getString(2).equals("1"));
                    }
                }
            } while (curs.moveToNext());

            curs.close();
        }
        catch (Exception e)
        {
            LogHelper.addLogLine("Exception bei QuestionActivity.loadAnswers: " + e.toString());
        }
    }

    @Override
    public void onBackPressed()
    {
        if (isCustomKeyboardVisible())
        {
            hideCustomKeyboard();
        }
        else
        {
            this.finish();
        }
    }

    private void loadQuestions(Integer chapterNo)
    {
        try
        {
            String sql = "SELECT _id, Text, BildId FROM Frage WHERE Antwort='0'";
            if (chapterNo > 0)
            {
                sql = sql + " AND KapitelNr='" + chapterNo + "'";
            }

            Cursor curs = new DataBaseHelper(this, SQLiteDatabase.OPEN_READONLY).queryBySql(sql);
            if (curs.getCount() == 0)
            {
                curs.close();
                return;
            }

            curs.moveToFirst();

            do
            {
                questions.add(new Question(curs.getString(0), curs.getString(1), curs.getString(2)));
            } while (curs.moveToNext());

            curs.close();
        }
        catch (Exception e)
        {
            LogHelper.addLogLine("Exception bei QuestionActivity.loadQuestions: " + e.toString());
        }
    }

    public void btnNextClick(View view)
    {
        if (isResultMode)
        {
            showRandomQuestion();
            return;
        }

        isResultMode = true;

        // method is called from onclick event of button
        Boolean allAnswersCorrect = true;

        if (currentQuestion.answers.size() > 1)
        {
            // multiple choice answers

            int checkedBoxes = 0;
            // for each checkbox
            for (int i = 0; i < currentQuestion.answers.size(); i++)
            {
                CustomCheckbox chk = (CustomCheckbox) findViewById(i);
                if (chk.isChecked())
                    checkedBoxes++;
            }

            // if no checkboxes are checked, return
            if (checkedBoxes == 0)
                return;

            // for each checkbox
            for (int i = 0; i < currentQuestion.answers.size(); i++)
            {
                CustomCheckbox chk = (CustomCheckbox) findViewById(i);

                // for each answer in currentquestion
                for (int j = 0; j < currentQuestion.answers.size(); j++)
                {
                    Question.Answer answer = currentQuestion.answers.get(j);
                    if (chk.getText() == answer.Text)
                    {
                        chk.setButtonColorByGivenAnswer(chk.isChecked(), answer.Correct);
                        if (chk.isChecked() != answer.Correct)
                        {
                            allAnswersCorrect = false;
                        }
                    }
                }
                chk.setEnabled(false);
            }
        }
        else
        {
            // text answer
            EditText edittext = (EditText) findViewById(R.id.answerText);
            String givenAnswer = edittext.getText().toString();
            edittext.setEnabled(false);

            // if no answer is entered, return
            if (givenAnswer.equals(""))
                return;

            String correctAnswer = currentQuestion.answers.get(0).Text.replace(" ", "");

            allAnswersCorrect = givenAnswer.equalsIgnoreCase(correctAnswer);
            if (allAnswersCorrect)
                edittext.setBackgroundColor(Color.GREEN);
            else
                edittext.setBackgroundColor(Color.RED);
            edittext.setTextColor(Color.BLACK);
        }
        writeAnswerToDatabase(allAnswersCorrect);
        questions.remove(currentQuestion);
    }

    private void writeAnswerToDatabase(Boolean allAnswersCorrect)
    {
        try
        {
            String sql = "UPDATE Frage SET Antwort='" + (allAnswersCorrect ? 1 : -1) + "' WHERE _id='" + currentQuestion.Id + "';";
            new DataBaseHelper(this, SQLiteDatabase.OPEN_READWRITE).queryBySqlWithoutResult(sql);
        }
        catch (Exception e)
        {
            LogHelper.addLogLine("Exception bei QuestionActivity.writeAnswerToDatabase: " + e.toString());
        }
    }

    private void drawKeyBoard()
    {
        // Create the Keyboard
        Keyboard mKeyboard = new Keyboard(this, R.xml.keyboard1);
        mKeyboardView = (KeyboardView) findViewById(R.id.keyboardview);

        // Attach the keyboard to the view
        mKeyboardView.setKeyboard(mKeyboard);
        mKeyboardView.setPreviewEnabled(false);

        // Install the key handler
        mKeyboardView.setOnKeyboardActionListener(mOnKeyboardActionListener);
    }

    private void drawEditText(EditText edittext)
    {
        edittext.setInputType(InputType.TYPE_NULL);

        // Make the custom keyboard appear
        edittext.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (hasFocus)
                {
                    showCustomKeyboard(v);
                }
                else
                {
                    hideCustomKeyboard();
                }
            }
        });

        edittext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showCustomKeyboard(v);
            }
        });
    }

    private KeyboardView.OnKeyboardActionListener mOnKeyboardActionListener = new KeyboardView.OnKeyboardActionListener()
    {

        @Override
        public void onPress(int arg0)
        {
        }

        @Override
        public void onRelease(int primaryCode)
        {
        }

        @Override
        public void onText(CharSequence text)
        {
        }

        @Override
        public void swipeDown()
        {
        }

        @Override
        public void swipeLeft()
        {
        }

        @Override
        public void swipeRight()
        {
        }

        @Override
        public void swipeUp()
        {
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes)
        {
            // Get the EditText and its Editable
            View focusCurrent = QuestionActivity.this.getWindow().getCurrentFocus();
            if (focusCurrent == null || focusCurrent.getClass() != AppCompatEditText.class)
            {
                return;
            }

            EditText edittext = (EditText) focusCurrent;
            Editable editable = edittext.getText();
            int start = edittext.getSelectionStart();

            // Handle key
            switch (primaryCode)
            {
                case Keyboard.KEYCODE_CANCEL:
                    hideCustomKeyboard();
                    break;
                case Keyboard.KEYCODE_DELETE:
                    if (editable != null && start > 0)
                    {
                        editable.delete(start - 1, start);
                    }
                    break;
                case 8888:
                    switchKeyboard(2);
                    break;
                case 9999:
                    switchKeyboard(1);
                    break;
                default:
                    editable.insert(start, Character.toString((char) primaryCode));
                    break;
            }
        }
    };

    private void switchKeyboard(int keyboardToShow)
    {
        if (keyboardToShow == 1)
        {
            mKeyboardView.setKeyboard(new Keyboard(this, R.xml.keyboard1));
        }
        else if (keyboardToShow == 2)
        {
            mKeyboardView.setKeyboard(new Keyboard(this, R.xml.keyboard2));
        }
    }

    public void hideCustomKeyboard()
    {
        mKeyboardView.setVisibility(View.GONE);
        mKeyboardView.setEnabled(false);
    }

    public void showCustomKeyboard(View v)
    {
        mKeyboardView.setVisibility(View.VISIBLE);
        mKeyboardView.setEnabled(true);
        if (v != null)
        {
            ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public boolean isCustomKeyboardVisible()
    {
        return mKeyboardView.getVisibility() == View.VISIBLE;
    }
}
