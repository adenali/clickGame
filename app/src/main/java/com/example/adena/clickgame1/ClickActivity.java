package com.example.adena.clickgame1;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.textservice.SentenceSuggestionsInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ClickActivity extends AppCompatActivity {

    /** Duration of the play session in milliseconds */
    public final static long SESSION_DURATION = 5000L;

    /** The identifiers of the buttons */
    public static final int[] DIGIT_BUTTONS = {
            R.id.button0,
            R.id.button1,
            R.id.button2,
            R.id.button3,
            R.id.button4,
            R.id.button5,
            R.id.button6,
            R.id.button7,
            R.id.button8,
            R.id.button9
    };

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click);
        handler = new Handler(); // we add an handler to manage events
        // we install the onclick listener on each button
        for (int id: DIGIT_BUTTONS)
            findViewById(id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDigitClicked(v);
                }
            });
        //currentScore = savedInstanceState.getInt("currentScore");
        if (savedInstanceState != null) // for example if the activity was rotated
        {
            currentScore = savedInstanceState.getInt("currentScore");
            nextDigit = savedInstanceState.getInt("nextDigit");
            buttonOrder = savedInstanceState.getIntegerArrayList("buttonOrder");
            deadline = savedInstanceState.getLong("deadline");
            triggerCountdownDisplay();
        } else { // if the activity was started for the first time
            currentScore = 0;
            nextDigit = random.nextInt(10);
            buttonOrder = new ArrayList<>();
            for (int i = 0; i < 10; i++)
                buttonOrder.add(i);
            deadline = 0;
        }
    }

    public void startCountdown()
    {
        Toast t = Toast.makeText(this, "The game begins!", Toast.LENGTH_SHORT);
        t.show();
        deadline = System.nanoTime() + SESSION_DURATION * 1_000_000L;
        /*Runnable runnable = new Runnable() {
            @Override
            public void run() {
                endGame();
            }
        };
        handler.postDelayed(runnable, SESSION_DURATION);*/
        // We write the code that will be periodically executed
        triggerCountdownDisplay();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (System.nanoTime() >= deadline) // we hit the deadline, the game is finished
                endGame();
            else
            {
                // we update the textview displaying the countdown
                updateCountdownDisplay();
                handler.postDelayed(this, 500);
            }
        }
    };

    public void triggerCountdownDisplay()
    {
        if (deadline > 0)
            handler.post(runnable);
    }

    private void updateCountdownDisplay()
    {
        int remainingSeconds = (int)((deadline - System.nanoTime()) / 1_000_000_000L);
        ((TextView)findViewById(R.id.countdownTextView)).setText(""+remainingSeconds);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        updateViews();
    }

    /** We keep the current score in this field */
    private int currentScore = 0;

    /** We keep here the next expected digit */
    private int nextDigit;

    /** Deadline of the game */
    private long deadline;

    /** We store here the order of the buttons */
    private ArrayList<Integer> buttonOrder;

    private Random random = new Random();

    /**
     * Method called when we click on a digit
     */
    public void onDigitClicked(View v)
    {
        int digit = Integer.parseInt(((Button)v).getText().toString());
        boolean match = (digit == nextDigit);
        if (match)
            currentScore++;
        else
            currentScore = 0; // we reset the score if the player has made a mistake
        // we update the next digit
        nextDigit = random.nextInt(10);
        shuffle();
        updateViews();
        if (deadline == 0)
            startCountdown();
    }

    private void updateViews()
    {
        ((TextView)findViewById(R.id.scoreTextView)).setText(""+currentScore);
        ((TextView)findViewById(R.id.nextDigitTextView)).setText("" + nextDigit);
        for (int i = 0; i < 10; i++)
            ((Button)findViewById(DIGIT_BUTTONS[i])).setText(buttonOrder.get(i).toString());
    }

    private void shuffle()
    {
        Collections.shuffle(buttonOrder);
    }

    private void endGame()
    {
        // 1: we disactivate all the buttons
        //for (int i = 0; i < 10; i++)
        //    ((Button)findViewById(DIGIT_BUTTONS[i])).setEnabled(false);
        // 2: we display a toast
        //Toast t = Toast.makeText(this, "End of game with score " + currentScore, Toast.LENGTH_LONG);
        //t.show();
        // we create an Intent to start a new activity

        // we start the EndActivity
        Intent i = new Intent(this, EndActivity.class);
        i.putExtra("score", currentScore);
        startActivity(i);
        finish();
    }

    /**
     * This method is called before destroying the activity
     * to store values.
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putInt("nextDigit", nextDigit);
        outState.putInt("currentScore", currentScore);
        outState.putIntegerArrayList("buttonOrder", buttonOrder);
        outState.putLong("deadline", deadline);
    }

    /** This method is called when the activity is stopped */
    @Override
    public void onStop()
    {
        super.onStop();
        handler.removeCallbacks(runnable);
    }
}

