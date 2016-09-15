package com.dhbw.luatraining;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Philipp on 15.09.2016.
 */
public class SettingsActivity extends BaseActivity implements View.OnClickListener {

    private Button bDrop;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_settings);

            bDrop = (Button) findViewById(R.id.button);






        }
        catch (Exception e)
        {
            LogHelper.addLogLine("Exception bei QuestionActivity.OnCreate: " + e.toString());
        }
    }

    @Override
    public void onClick(View view) {
        if (view.equals(bDrop)){

        }
    }

    private boolean clearDatabase(){
        try
        {
            //clear column 'Antwort' to reset learning progess
            String sql = "UPDATE Frage SET Antwort = 0";

            new DataBaseHelper(this).queryWriteBySql(sql);

            Toast.makeText(this, "Fortschritt erfolgreich zurück gesetzt", Toast.LENGTH_SHORT);

        }
        catch (Exception e)
        {
            LogHelper.addLogLine("Exception bei StatsActivity.loadOverallStats: " + e.toString());
        }
        return true;
    }
}
