package com.example.adena.clickgame1;

/**
 * Created by adena on 08/05/2017.
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HighScoresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        new ScoreFetcher(this).execute();
    }
}
