package com.dhbw.luatraining;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class QuestionActivity extends BaseActivity
{
    private KeyboardView mKeyboardView;
    private List<Question> questions = new ArrayList<>();
    private Question currentQuestion;

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
            LogHelper.addLogLine("ChapterNo given by Intent: " + ChapterNo);

            loadQuestions(ChapterNo);
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
            if (questions.size() == 0)
            {
                LogHelper.addLogLine("Zum angegebenen Kapitel wurden keine Fragen gefunden.");
                return;
            }

            //TODO: get a really random question
            currentQuestion = questions.get(0);

            TextView questionText = (TextView) findViewById(R.id.questionText);
            questionText.setText(currentQuestion.Text);

            String IId = currentQuestion.ImageId;
            if (IId != null && !IId.equals(""))
            {
                try
                {
                    Cursor curs = new DataBaseHelper(this).queryBySql("select Bild from Bild where BildId='" + IId + "'");
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

            if (currentQuestion.answers.size() > 1)
            {
                // multiple choice answers

                EditText edittext = (EditText) findViewById(R.id.answerText);
                edittext.setVisibility(View.GONE);

                LinearLayout answers = (LinearLayout) findViewById(R.id.answer);
                for (int i = 0; i < currentQuestion.answers.size(); i++)
                {
                    CheckBox chk1 = new CheckBox(this);
                    chk1.setText(currentQuestion.answers.keySet().toArray()[i].toString());
                    answers.addView(chk1);
                }
            }
            else
            {
                // text answer

                EditText edittext = (EditText) findViewById(R.id.answerText);
                edittext.requestFocus();

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

            String sql = "select _id, Antwort, Richtigkeit from Antwort where _id in ";
            if (chapterNo > 0)
            {
                sql = sql + "(select _id from Frage where Antwort='0' and KapitelNr='" + chapterNo + "')";
            }
            else
            {
                sql = sql + "(select _id from Frage where Antwort='0')";
            }


            Cursor curs = new DataBaseHelper(this).queryBySql(sql);
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
                        questions.get(i).addAnswer(curs.getString(1), curs.getString(2).equals("1"));
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
            String sql = "select _id, Text, BildId from Frage where Antwort='0'";
            if (chapterNo > 0)
            {
                sql = sql + " and KapitelNr='" + chapterNo + "'";
            }

            Cursor curs = new DataBaseHelper(this).queryBySql(sql);
            if (curs.getCount() == 0)
            {
                LogHelper.addLogLine("Zum angegebenen Kapitel wurden keine offenen Fragen gefunden.");
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
                        editable.delete(start - 1, start);
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
