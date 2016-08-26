package com.dhbw.luatraining;

import android.content.Context;
import android.content.Intent;
import android.provider.LiveFolders;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID)
    {
        DrawerLayout fullView = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout activityContainer = (FrameLayout) fullView.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(fullView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("LuA Training");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, fullView, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_container);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.nav_mengen:
                showQuestions(1);
                break;
            case R.id.nav_relationen:
                showQuestions(2);
                break;
            case R.id.nav_abbildungen:
                showQuestions(3);
                break;
            case R.id.nav_boolesche:
                showQuestions(4);
                break;
            case R.id.nav_aussagenlogik:
                showQuestions(5);
                break;
            case R.id.nav_problems:
                LogHelper.addLogLine("Eigene Aufgaben wurde ausgewählt");
                break;
            case R.id.nav_stats:
                LogHelper.addLogLine("Auswertung wurde ausgewählt");
                break;
            case R.id.nav_settings:
                LogHelper.addLogLine("Einstellungen wurde ausgewählt");
                break;
            case R.id.nav_logging:
                LogHelper.addLogLine("LogHelper wurde ausgewählt");
                showLogging();
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_container);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showLogging()
    {
        try
        {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("StringToShow", LogHelper.getLogLines().toString());
            // flag is needed to not start a new activity but use the already given
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        catch (Exception e)
        {
            LogHelper.addLogLine(e.toString());
        }
    }

    public void showQuestions(Integer ChapterNo)
    {
        try
        {
            Intent intent = new Intent(this, QuestionActivity.class);
            intent.putExtra("ChapterNo", ChapterNo);
            startActivity(intent);
        }
        catch (Exception e)
        {
            LogHelper.addLogLine(e.toString());
        }
    }
}