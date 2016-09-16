package com.dhbw.luatraining;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.Switch;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Philipp on 11.09.2016.
 */
public class StatsActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private List<Question> allQuestions = new ArrayList<>();
    private List<Question> questionsWrong = new ArrayList<>();
    private List<Question> questionsNoAnswer = new ArrayList<>();
    private List<Question> questionsRight = new ArrayList<>();
    private int[] overallStats = new int[3];
    private int[] individualStats = new int[3];
    private final int ANSWER_WRONG = -1;
    private final int NO_ANSWER = 0;
    private final int  ANSWER_RIGHT = 1;

    private PieChart pieChart;
    private Switch sw1,sw2,sw3,sw4,sw5;
    private final String[] xData = {"richtig" , "falsch", "nicht beantwortet"};
    private final int[] colors = {Color.GREEN, Color.RED, Color.GRAY};
    private float[] yData = { 19, 10, 4};
    ArrayList<Entry> entries = new ArrayList<Entry>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        // load all data befor showing anything. Hopefully leads to better performance
        loadAllQuestions();

        //prepare used UI elements
        pieChart = new PieChart(this);
        pieChart = (PieChart) findViewById(R.id.chart);

        sw1 = new Switch(this);
        sw2 = new Switch(this);
        sw3 = new Switch(this);
        sw4 = new Switch(this);
        sw5 = new Switch(this);

        sw1 = (Switch) findViewById(R.id.switch1);
        sw2 = (Switch) findViewById(R.id.switch2);
        sw3 = (Switch) findViewById(R.id.switch3);
        sw4 = (Switch) findViewById(R.id.switch4);
        sw5 = (Switch) findViewById(R.id.switch5);

        //settings for pie chart
        pieChart.setUsePercentValues(true);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(25);
        pieChart.setTransparentCircleRadius(30);
        pieChart.setNoDataTextDescription("Bitte die zu Auswertung relevanten Kapitel auswählen");
        pieChart.animateY(1500);
        pieChart.getLegend().setEnabled(false);

        //calculate and present overall data
        calculateOverallStats();
        prepareInitialChart();


        //set listener for all switches to
        sw1.setOnCheckedChangeListener(this);
        sw2.setOnCheckedChangeListener(this);
        sw3.setOnCheckedChangeListener(this);
        sw4.setOnCheckedChangeListener(this);
        sw5.setOnCheckedChangeListener(this);


    }


    private void loadAllQuestions(){


        int answerCounter;


        try
        {
            //load all questions and corresponding answers
            String sql = "SELECT _id, KapitelNr, Antwort FROM Frage";

            Cursor curs = new DataBaseHelper(this).queryReadBySql(sql);
            if (curs.getCount() == 0)
            {
                curs.close();

                // TODO Was passiert wenn nichts geladen wurde
            }
            String s = DatabaseUtils.dumpCursorToString(curs);
            curs.moveToFirst();


            do
            {   //fill the allQuestion List
                allQuestions.add(new Question(curs.getString(0),curs.getInt(1), curs.getInt(2)));
                answerCounter = allQuestions.size();

                //split the answers and write objects to corresponding list
                if (curs.getInt(2)==(ANSWER_RIGHT)){
                    questionsRight.add(allQuestions.get(answerCounter -1));
                }
                else if (curs.getInt(2)==(ANSWER_WRONG)){
                    questionsWrong.add(allQuestions.get(answerCounter -1));
                }
                else if(curs.getInt(2)==(NO_ANSWER)){
                    questionsNoAnswer.add(allQuestions.get(answerCounter -1));
                }
                else{
                }

            } while (curs.moveToNext());

            curs.close();
        }
        catch (Exception e)
        {
            LogHelper.addLogLine("Exception bei StatsActivity.loadOverallStats: " + e.toString());
        }

    }

    private void calculateOverallStats(){
        //amount of objects in each list is amount of wrong/right/no answers
        overallStats[0] = questionsRight.size();
        overallStats[1] = questionsWrong.size();
        overallStats[2] = questionsNoAnswer.size();
    }

    private void addToIndividualStats(int chapterNo){
        //method to add amount of answers depending on chapter number to correct array for analysis
        for(int i = 0; i < allQuestions.size(); i++){

            if (allQuestions.get(i).chapterNo==chapterNo){

                switch (allQuestions.get(i).answerIndex){
                    case ANSWER_RIGHT:
                        individualStats[0]++;
                        break;
                    case ANSWER_WRONG:
                        individualStats[1]++;
                        break;
                    case NO_ANSWER:
                        individualStats[2]++;
                        break;
                }

            }
            else{
                //do nothing --> not required chapter
            }
        }

    }

    private void prepareInitialChart(){
        //method will be called in OnCreate, so it's only executed once - at the beginning - to display overall statistics
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();
        int[] col = new int[3];
        yData[0]=overallStats[0];
        yData[1]=overallStats[1];
        yData[2]=overallStats[2];


        for (int i = 0; i < yData.length; i++){
            if (yData[i]==0){

                //prevents unnecessary values in chart. also prevents from overlapping texts in chart
                continue;
            }
            yVals.add(new Entry(yData[i], i));
            xVals.add(xData[i]);
            col[i]=colors[i];
        }

        PieDataSet dataSet = new PieDataSet(yVals, "");

        dataSet.setSliceSpace(4);
        dataSet.setSelectionShift(5);
        dataSet.setColors(col);
        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);

        pieChart.setData(data);

        //method to redraw chart
        pieChart.invalidate();


    }

    private void refreshChart(){

        //this method will draw a new chart depending on individualStats array.
        //before calling this method prepare data with addToIndividualStats()

        ArrayList<Entry> yVals = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();
        int[] col = new int[3];
        yData[0]=individualStats[0];
        yData[1]=individualStats[1];
        yData[2]=individualStats[2];


        for (int i = 0; i < yData.length; i++){
            if(yData[i]==0){
                //prevents unnecessary values in chart. also prevents from overlapping texts in chart
                continue;
            }
            yVals.add(new Entry(yData[i], i));
            xVals.add(xData[i]);
            col[i]=colors[i];
        }

        PieDataSet dataSet = new PieDataSet(yVals, "Stats von xxx");


        dataSet.setSliceSpace(4);
        dataSet.setSelectionShift(5);

        PieData data = new PieData(xVals, dataSet);
        dataSet.setColors(col);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);

        pieChart.setData(data);
        pieChart.animateY(1500);
        pieChart.invalidate();


    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        //Listener for switches to handle analysis on differend chapters

        //clear basic Data
        individualStats = new int[3];

        //add relevant basic data
        if (sw1.isChecked()){
            addToIndividualStats(1);
        }
        if (sw2.isChecked()){

            addToIndividualStats(2);
        }
        if (sw3.isChecked()){
            addToIndividualStats(3);
        }
        if (sw4.isChecked()){
            addToIndividualStats(4);

        }
        if (sw5.isChecked()){
            addToIndividualStats(5);
        }

        // refresh pie chart
        refreshChart();

    }
}
