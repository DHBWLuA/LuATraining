package com.dhbw.luatraining;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends BaseActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String message = getIntent().getStringExtra("StringToShow");
        TextView txt = (TextView) findViewById(R.id.txtView);

        // dont override welcome message
        if(message != null && !message.isEmpty())
            txt.setText(message);
    }
}
