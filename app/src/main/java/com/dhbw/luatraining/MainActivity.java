package com.dhbw.luatraining;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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
        txt.setText(message);
    }
}
