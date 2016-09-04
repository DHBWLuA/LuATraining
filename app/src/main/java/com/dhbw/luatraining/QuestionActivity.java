package com.dhbw.luatraining;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class QuestionActivity extends BaseActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_question);

            EditText edittext = (EditText)findViewById(R.id.editText);
            edittext.setInputType(InputType.TYPE_NULL);
            edittext.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    showCustomKeyboard(v);
                }
            });

            String message = getIntent().getStringExtra("ChapterNo");

            if (message == "0")
            {
                //Klausurmodus
            }
        }
        catch (Exception e)
        {
            LogHelper.addLogLine("Exception bei QuestionActivity.OnCreate: " + e.toString());
        }
    }
}
