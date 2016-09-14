package com.dhbw.luatraining;

import java.util.ArrayList;
import java.util.List;

public class Question
{

    public String Id;
    public String Text;
    public String ImageId;
    public List<Answer> answers = new ArrayList<>();
    public int answerIndex;
    public int chapterNo;

    public Question(String id, String text, String imageId)
    {
        this.Id = id;
        this.Text = text;
        this.ImageId = imageId;
    }

    public Question(String id, int chapter, int answerIndex){
        this.Id = id;
        this.chapterNo=chapter;
        this.answerIndex=answerIndex;
    }

    public void addAnswer(String answer, Boolean right)
    {
        if (answers == null)
            LogHelper.addLogLine("answers is null");
        else
            answers.add(new Answer(answer, right));
    }

    public class Answer
    {
        public String Text;
        public Boolean Correct;

        public Answer(String text, Boolean correct)
        {
            this.Text = text;
            this.Correct = correct;
        }
    }
}
