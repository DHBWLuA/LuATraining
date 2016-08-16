package com.dhbw.luatraining;

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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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
                Toast.makeText(getApplicationContext(), "Mengen wurde ausgewählt.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_relationen:
                Toast.makeText(getApplicationContext(), "Relationen wurde ausgewählt.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_abbildungen:
                Toast.makeText(getApplicationContext(), "Abbildungen wurde ausgewählt.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_boolesche:
                Toast.makeText(getApplicationContext(), "Boole´sche Algebra wurde ausgewählt.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_aussagenlogik:
                Toast.makeText(getApplicationContext(), "Aussagenlogik wurde ausgewählt.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_praedikatenlogik:
                Toast.makeText(getApplicationContext(), "Prädikatenlogik erster Stufe wurde ausgewählt.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_problems:
                Toast.makeText(getApplicationContext(), "Eigene Aufgaben wurde ausgewählt.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_stats:
                Toast.makeText(getApplicationContext(), "Auswertung wurde ausgewählt.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_settings:
                Toast.makeText(getApplicationContext(), "Einstellungen wurde ausgewählt.", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}