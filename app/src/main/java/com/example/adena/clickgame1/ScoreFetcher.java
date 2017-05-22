package com.example.adena.clickgame1;

import java.io.IOException;
import java.util.List;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;


/**
 * AsyncTask fetching the scores on the server.
 */
/**
 * Created by adena on 08/05/2017.
 */

public class ScoreFetcher extends AsyncTask<Void, Void, List<Score>>
{
    private HighScoresActivity hsActivity;
    private Exception encounteredException;

    public ScoreFetcher(HighScoresActivity hsActivity)
    {
        super();
        this.hsActivity = hsActivity;
    }

    @Override
    protected List<Score> doInBackground(Void... params)
    {
        HighScoreManager hsm = new HighScoreManager();
        try {
            return hsm.fetchScores();
        } catch (IOException |JSONException e)
        {
            encounteredException = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(final List<Score> scores)
    {
        super.onPostExecute(scores);
        if (scores == null) {
            Log.e(getClass().getName(), "Exception while retrieving the high scores", encounteredException);
            Toast t = Toast.makeText(hsActivity, encounteredException.getMessage(), Toast.LENGTH_LONG);
            t.show();
        } else {
            // we display the list of scores in the listview
            ListView lv = (ListView) hsActivity.findViewById(R.id.scoreListView);
            ArrayAdapter<Score> adapter =
                    new ArrayAdapter<Score>(hsActivity,
                            android.R.layout.simple_list_item_1, scores)
                    {
                        // We override the getView method to customize the aspect of each item
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent)
                        {
                            View v = super.getView(position, convertView, parent);
                            if (position % 2 == 0)
                                v.setBackgroundColor(Color.rgb(255, 0, 0));
                            else if (position % 2 == 1)
                                v.setBackgroundColor(Color.rgb(0, 255, 0));
                            return v;
                        }
                    };
            lv.setAdapter(adapter);
        }
    }
}
