package com.dhbw.luatraining;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class SettingsActivity extends BaseActivity implements View.OnClickListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @Override
    public void onClick(View view)
    {
        try
        {
            SQLiteDatabase db = new DataBaseHelper(this).getWritableDatabase();
            ContentValues dataToInsert = new ContentValues();
            dataToInsert.put("Antwort", "0");
            db.update("Frage", dataToInsert, null, null);
            Toast.makeText(this, "Fortschritt erfolgreich zurückgesetzt", Toast.LENGTH_SHORT).show();
            view.setEnabled(false);
        }
        catch (Exception e)
        {
            LogHelper.addLogLine("Exception bei StatsActivity.loadOverallStats: " + e.toString());
        }
    }
}
