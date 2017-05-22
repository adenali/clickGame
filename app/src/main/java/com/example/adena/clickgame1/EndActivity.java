package com.example.adena.clickgame1;

/**
 * Created by adena on 08/05/2017.
 */
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class EndActivity extends AppCompatActivity {

    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        score = getIntent().getIntExtra("score", -1);
        ((TextView)findViewById(R.id.endScoreTextView)).setText("" + score);
    }

    public void quit(View v)
    {
        finish();
    }

    public void playAgain(View v)
    {
        Intent i = new Intent(this, ClickActivity.class);
        startActivity(i);
        finish();
    }

    public void postScore(View v)
    {
        String name = ((EditText)findViewById(R.id.playerNameEditText)).getText().toString();
        ScorePoster sp = new ScorePoster(this);
        // plan the execution of the code asynchronously
        sp.execute(name, ""+score);
    }

    public void showHighScores(View v)
    {
        Intent i = new Intent(this, HighScoresActivity.class);
        startActivity(i);
        finish();
    }
}
