package com.example.adena.clickgame1;

/**
 * Created by adena on 08/05/2017.
 */


import java.util.Date;

public class Score
{
    public String playerName;
    public Date date;
    public int value;

    public Score(String playerName, int value, Date date) {
        this.playerName = playerName;
        this.date = date;
        this.value = value;
    }


    @Override
    public String toString()
    {
        return playerName + "[" + value + "] " + date;
    }
}
