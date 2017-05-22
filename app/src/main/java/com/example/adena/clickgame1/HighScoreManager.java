package com.example.adena.clickgame1;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by greta on 16/06/2016.
 */
public class HighScoreManager
{
    public static final String SERVER_ADDRESS =
            "http://10.0.2.2:9000/";

    /**
     * We post the score on the server
     * @param name name of the player
     * @param score score of the game session
     * @throws IOException if there is a network error
     */
    public void postScore(String name, int score) throws IOException
    {
        Log.i(getClass().getName(), "Posting the score " + score + " under the name " + name);
        URL url = new URL(SERVER_ADDRESS + "scores");
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        try {
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            String dataToSend = "name=" + URLEncoder.encode(name, "UTF-8") + "&score=" + score;
            conn.getOutputStream().write(dataToSend.getBytes("ascii"));
            conn.getOutputStream().close();
            int resultCode = conn.getResponseCode();
            if (resultCode != 200)
                throw new IOException("The server returned the result code " + resultCode
                        + " with the message " + conn.getResponseMessage());
        } finally
        {
            conn.disconnect();
        }
    }

    public String fetchStringFromServer(String location) throws IOException
    {
        // we use the GET method
        // we read the data returned from the server and we create a string
        // containing it
        URL url = new URL(location);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.addRequestProperty("Accept", "application/json");
        InputStream is = conn.getInputStream();
        String data = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (String line = br.readLine(); line != null; line = br.readLine())
                sb.append(line).append("\n");
            data = sb.toString();
            return data;
        } finally
        {
            is.close();
        }
    }

    public List<Score> toScoreList(String data) throws JSONException
    {
        // now we must interpret data as a JSON object
        JSONObject obj = new JSONObject(data);
        JSONArray scoreArray = obj.getJSONArray("scores");
        List<Score> scoreList = new ArrayList<>();
        for (int i = 0; i < scoreArray.length(); i++)
        {
            JSONObject scoreObj = scoreArray.getJSONObject(i);
            String name = scoreObj.getString("name");
            int score = scoreObj.getInt("score");
            long date = scoreObj.getLong("date");
            scoreList.add(new Score(name, score, new Date(date)));
        }
        return scoreList;
    }

    public List<Score> fetchScores() throws IOException, JSONException
    {
        String data = fetchStringFromServer(SERVER_ADDRESS + "scores");
        Log.i(getClass().getName(), "Data2: " + data);
        return toScoreList(data);
    }
}
