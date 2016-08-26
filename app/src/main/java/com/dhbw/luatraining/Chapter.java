package com.dhbw.luatraining;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Base64;
import android.widget.ImageView;

import java.util.Dictionary;
import java.util.List;

public class Chapter {

    private List<Question> questions;

    public Chapter(Integer chapterNo, Context context)
    {
        DataBaseHelper myDbHelper = new DataBaseHelper(context);
        Cursor frageCursor = myDbHelper.getReadableDatabase().query("Frage JOIN Antwort ON Frage._id = Antwort._id",
                new String[]{"Text", "Antwort", "Richtigkeit"}, "KapitelNr = " + chapterNo.toString(), null, null, null, null);

        frageCursor.moveToFirst();

        Question quest = new Question();
        quest.questionText = frageCursor.getString(0);

        do {
            quest.addAnswer(frageCursor.getString(1), Boolean.getBoolean(frageCursor.getString(2)));
        } while (frageCursor.moveToNext());

        questions.add(quest);

        frageCursor.close();
        myDbHelper.close();
    }

    public class Question
    {
        public int questionNr;
        public String questionText;
        public Bitmap questionImage;
        public Dictionary<String, Boolean> answers;

        public void addAnswer(String answer, Boolean correctness){
            answers.put(answer, correctness);
        }
    }

    public List<Question> getQuestions()
    {
        return questions;
    }
}
