package com.dhbw.luatraining;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class CustomCheckbox extends CheckBox
{
    public CustomCheckbox(Context context) {
        super(context);
        setOnCheckedChangeListener(mOnCheckedChangeListener);
        setButtonDrawable(R.drawable.checkbox_black_unchecked);
        setPadding(35, 20, 20, 20);
    }

    public CheckBox.OnCheckedChangeListener mOnCheckedChangeListener = new CheckBox.OnCheckedChangeListener()
    {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked)
                setButtonDrawable(R.drawable.checkbox_black_checked);
            else
                setButtonDrawable(R.drawable.checkbox_black_unchecked);
        }
    };

    public void setButtonColorByGivenAnswer(Boolean givenAnswer, Boolean correctAnswer)
    {
        if (givenAnswer)
        {
            if (correctAnswer)
                setButtonDrawable(R.drawable.checkbox_green_checked);
            else
                setButtonDrawable(R.drawable.checkbox_red_checked);
        }
        else
        {
            if (correctAnswer)
                setButtonDrawable(R.drawable.checkbox_green_unchecked);
            else
                setButtonDrawable(R.drawable.checkbox_black_unchecked);
        }
    }
}
