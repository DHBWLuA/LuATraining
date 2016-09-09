package com.dhbw.luatraining;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class Question
{
    public String Id;
    public String Text;
    public String ImageId;
    public Map<String, Boolean> answers = new HashMap<>();

    public Question(String id, String text, String imageId)
    {
        this.Id = id;
        this.Text = text;
        this.ImageId = imageId;
    }

    public void addAnswer(String answer, Boolean right)
    {
        if (answers == null)
            LogHelper.addLogLine("answers is null");
        else
            answers.put(answer, right);
    }
}
