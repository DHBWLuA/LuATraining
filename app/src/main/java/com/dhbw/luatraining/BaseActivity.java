package com.dhbw.luatraining;

import android.app.Activity;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private KeyboardView mKeyboardView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);

            drawKeyBoard();

            // Hide the standard keyboard initially
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
        catch (Exception e)
        {
            LogHelper.addLogLine("Exception bei BaseActivity.OnCreate: " + e.toString());
        }
    }

    @Override
    public void setContentView(int layoutResID)
    {
        try
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
        catch (Exception e)
        {
            LogHelper.addLogLine("Exception bei BaseActivity.setContentView: " + e.toString());
        }
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_container);

        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (isCustomKeyboardVisible())
        {
            hideCustomKeyboard();
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
            case R.id.nav_klausurmodus:
                showQuestions(0);
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
            LogHelper.addLogLine("Exception bei BaseActivity.showLogging: " + e.toString());
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
            LogHelper.addLogLine("Exception bei BaseActivity.showQuestions: " + e.toString());
        }
    }


    private void drawKeyBoard()
    {
        // Create the Keyboard
        Keyboard mKeyboard = new Keyboard(this, R.xml.keyboard1);
        mKeyboardView = (KeyboardView) findViewById(R.id.keyboardview);

        // Attach the keyboard to the view
        mKeyboardView.setKeyboard(mKeyboard);
        mKeyboardView.setPreviewEnabled(false);

        // Install the key handler
        mKeyboardView.setOnKeyboardActionListener(mOnKeyboardActionListener);
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
            View focusCurrent = BaseActivity.this.getWindow().getCurrentFocus();
            if (focusCurrent == null || focusCurrent.getClass() != AppCompatEditText.class)
            {
                return;
            }

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
                    {
                        editable.delete(start - 1, start);
                    }
                    break;
                case 8888:
                    switchKeyboard(2);
                    break;
                case 9999:
                    switchKeyboard(1);
                    break;
                default:
                    editable.insert(start, Character.toString((char) primaryCode));
                    break;
            }
        }
    };

    private void switchKeyboard(int keyboardToShow)
    {
        if (keyboardToShow == 1)
        {
            mKeyboardView.setKeyboard(new Keyboard(this, R.xml.keyboard1));
        }
        else if (keyboardToShow == 2)
        {
            mKeyboardView.setKeyboard(new Keyboard(this, R.xml.keyboard2));
        }
    }

    public void hideCustomKeyboard()
    {
        mKeyboardView.setVisibility(View.GONE);
        mKeyboardView.setEnabled(false);
    }

    public void showCustomKeyboard(View v)
    {
        mKeyboardView.setVisibility(View.VISIBLE);
        mKeyboardView.setEnabled(true);
        if (v != null)
        {
            ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public boolean isCustomKeyboardVisible()
    {
        return mKeyboardView.getVisibility() == View.VISIBLE;
    }
}
