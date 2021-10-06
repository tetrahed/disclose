package com.example.robert.myapplicationtest3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.http.ServiceCallback;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.Tone;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneScore;

import java.util.List;

public class MoodEntryActivity extends AppCompatActivity
{
    ToneAnalyzer toneAnalyzer;
    EditText userInput;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_entry);
        userInput= (EditText) findViewById(R.id.userInput);
        toneAnalyzer= new ToneAnalyzer("2017-07-01");

        try
        {//created new raw file in res. That contains the username, password, and link for the API.
            //JSONObject credentials = new JSONObject(IOUtils.toString(getResources().openRawResource(R.raw.credentials), "UTF-8")); // Convert the file into a JSON object
            Log.d("appError1","Failure here");
            // Extract the two values
            //String username = credentials.getString("username");
            //String password = credentials.getString("password");
            Log.d("appError2","Failure here");
            //toneAnalyzer.setUsernameAndPassword(username, password);
            toneAnalyzer.setUsernameAndPassword("ee4c7361-8aa6-4d11-8124-af1357f7ad84", "6kZb6Gigp3a0");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        /*
        catch (IOException e)
        {
            Log.d("appError3","Failure here");
            e.printStackTrace();
        }
        catch (JSONException e) //problem here
        {
            Log.d("appError4","Failure here");
            e.printStackTrace();
        }
        */
        //setContentView(R.layout.activity_mood_entry);
    }

    public void analyze(View view) {
        analyzeText();
    }

    private void analyzeText()
    {
        // config tone analyzer
        ToneOptions options = new ToneOptions.Builder().addTone(Tone.EMOTION).html(false).build(); //problem here
        Log.d("appError5","Tone Analyzer failed");
        String textToAnalyze = userInput.getText().toString();

        /*
        String textToAnalyze= "I know the times are difficult! Our sales have been "
                        + "disappointing for the past three quarters for our data analytics "
                        + "product suite. We have a competitive data analytics product "
                        + "suite in the industry. But we need to do our job selling it! "
                        + "We need to acknowledge and fix our sales challenges. "
                        + "We can’t blame the economy for our lack of execution! "
                        + "We are missing critical sales opportunities. "
                        + "Our product is in no way inferior to the competitor products. "
                        + "Our clients are hungry for analytical tools to improve their "
                        + "business outcomes. Economy has nothing to do with it.";
                        */

        toneAnalyzer.getTone(textToAnalyze, options).enqueue(
                new ServiceCallback<ToneAnalysis>()
                {
                    @Override
                    public void onResponse(ToneAnalysis response)
                    {
                        // More code here
                        List<ToneScore> scores = response.getDocumentTone().getTones().get(0).getTones();
                        String detectedTones = "";
                        Log.d("appError6","Tone Analyzer failed");
                        for(ToneScore score:scores)
                        {
                            if(score.getScore() > 0.5f)
                            {
                                detectedTones += score.getName() + " ";
                            }
                        }

                        final String toastMessage= "The following emotions were detected:\n\n"+ detectedTones.toUpperCase();

                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Toast.makeText(getBaseContext(),toastMessage, Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Exception e)
                    {
                        Log.d("appError7","Tone Analyzer failed");
                        e.printStackTrace();
                    }
                });
    }
}
