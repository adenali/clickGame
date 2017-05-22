package com.example.adena.clickgame1;

/**
 * Created by adena on 08/05/2017.
 */
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;


public class ScorePoster extends AsyncTask<String, Void, Boolean>
{
    private EndActivity endActivity;
    private IOException encounteredException;

    public ScorePoster(EndActivity endActivity)
    {
        super();
        this.endActivity = endActivity;
    }

    @Override
    protected Boolean doInBackground(String... params)
    {
        String name = params[0];
        int score = Integer.parseInt(params[1]);
        HighScoreManager hsm = new HighScoreManager();
        try {
            hsm.postScore(name, score);
            return true;
        } catch (IOException e)
        {
            encounteredException = e;
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean success)
    {
        super.onPostExecute(success);
        // we display a toast for a feedback to the user
        String resultMessage = null;
        if (success)
            resultMessage = "OK the score has been posted";
        else
            resultMessage = "Failure: " + encounteredException.getMessage();
        Toast t = Toast.makeText(endActivity, resultMessage, Toast.LENGTH_LONG);
        t.show();
    }
}
