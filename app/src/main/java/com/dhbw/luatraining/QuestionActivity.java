package com.dhbw.luatraining;

import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class QuestionActivity extends BaseActivity
{
    private KeyboardView mKeyboardView;

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

            drawEditText((EditText) findViewById(R.id.editText));
        }
        catch (Exception e)
        {
            LogHelper.addLogLine("Exception bei QuestionActivity.OnCreate: " + e.toString());
        }
    }

    @Override public void onBackPressed() {
        if( isCustomKeyboardVisible() )
            hideCustomKeyboard();
        else
            this.finish();
    }

    private void drawKeyBoard()
    {
        // Create the Keyboard
        Keyboard mKeyboard = new Keyboard(this, R.xml.keyboard);
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
        edittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override public void onFocusChange(View v, boolean hasFocus) {
                if( hasFocus )
                    showCustomKeyboard(v);
                else
                    hideCustomKeyboard();
            }
        });

        edittext.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
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
            if (focusCurrent == null || focusCurrent.getClass() != EditText.class)
                return;

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
                default:
                    editable.insert(start, Character.toString((char) primaryCode));
                    break;
            }
        }
    };

    public void hideCustomKeyboard() {
        mKeyboardView.setVisibility(View.GONE);
        mKeyboardView.setEnabled(false);
    }

    public void showCustomKeyboard( View v ) {
        mKeyboardView.setVisibility(View.VISIBLE);
        mKeyboardView.setEnabled(true);
        if( v!=null )
            ((InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public boolean isCustomKeyboardVisible() {
        return mKeyboardView.getVisibility() == View.VISIBLE;
    }
}
