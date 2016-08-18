package com.dhbw.luatraining;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_mengen:
                Logging.addLogLine("Mengen wurde ausgewählt");
                showQuestions(1);
                break;
            case R.id.nav_relationen:
                Logging.addLogLine("Relationen wurde ausgewählt");
                showQuestions(2);
                break;
            case R.id.nav_abbildungen:
                Logging.addLogLine("Abbildungen wurde ausgewählt");
                showQuestions(3);
                break;
            case R.id.nav_boolesche:
                Logging.addLogLine("Boole´sche Algebra wurde ausgewählt");
                showQuestions(4);
                break;
            case R.id.nav_aussagenlogik:
                Logging.addLogLine("Aussagenlogik wurde ausgewählt");
                showQuestions(5);
                break;
            case R.id.nav_problems:
                Logging.addLogLine("Eigene Aufgaben wurde ausgewählt");
                break;
            case R.id.nav_stats:
                Logging.addLogLine("Auswertung wurde ausgewählt");
                break;
            case R.id.nav_settings:
                Logging.addLogLine("Einstellungen wurde ausgewählt");
                break;
            case R.id.nav_logging:
                Logging.addLogLine("Logging wurde ausgewählt");
                showLog();
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showQuestions(Integer ChapterNo)
    {
        try {
            Logging.addLogLine("showQuestions mit ChapterNo "+ChapterNo.toString());

            DataBaseHelper myDbHelper = new DataBaseHelper(this);
            Logging.addLogLine("DataBaseHelper erstellen erfolgreich");

            Cursor frageCursor = myDbHelper.getReadableDatabase().query("Frage", new String[]{"_id", "Text", "Bild"},
                    "KapitelNr = " + ChapterNo.toString(), null, null, null, null);
            Logging.addLogLine("cursor erstellen erfolgreich");

            while (frageCursor.moveToNext()) {
                Logging.addLogLine("String 1 im Cursor ist: " + frageCursor.getString(1));
            }

            frageCursor.close();
            Logging.addLogLine("frageCursor.close erfolgreich");

            myDbHelper.close();
            Logging.addLogLine("DataBaseHelper.close erfolgreich");
        }
        catch (Exception e)
        {
            Logging.addLogLine(e.toString());
        }
    }
    public void showLog()
    {
        TextView tv = (TextView) findViewById(R.id.mainTextView);
        String logText = "Logging" + Logging.getLogLines().toString();
        tv.setText(logText);
    }
}