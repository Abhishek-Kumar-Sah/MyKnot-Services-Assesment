package com.avi.in.myknotproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private final String URL = "https://backend-test-zypher.herokuapp.com/testData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button showCardButton = findViewById(R.id.start_button);
        showCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBoxAsyncTask task = new DialogBoxAsyncTask();
                task.execute(URL);
            }
        });

    }



    private class DialogBoxAsyncTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {

            URL url = null;

            if (urls.length < 1 && urls[0] == null)
                return null;

            try {
                url = new URL(urls[0]);
            } catch (MalformedURLException e) {
                Log.e("LOG_TAG","Problem with URL : "+ e.getMessage());
            }

            return getJSONResponse(url);
        }


        @Override
        protected void onPostExecute(String JSONResponse) {

            String title = null;
            String ImageURL= null;
            String successURL = null;
            try {
                JSONObject root = new JSONObject(JSONResponse);
                title = root.getString("title");
                ImageURL = root.getString("imageURL");
                successURL = root.getString("success_url");

            } catch (JSONException e) {
                Log.e("Error getting json response", e.getMessage());
            }

            //Getting the dialog box singleton reference and assigning
            //and passing the parsed data
            ShowDialogBox dialogBox = ShowDialogBox.getSingletonInstance();
            dialogBox.show(MainActivity.this,title,ImageURL,successURL);

        }
    }
    //Extracts JSON response and returns its String.
    private String getJSONResponse(URL url){

        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.connect();
        } catch (IOException e) {
            Log.e("LOG_TAG","Error connecting to URL : "+e.getMessage());
        }


        try {
            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader br = new BufferedReader(isr);

                String line = br.readLine();

                while(line != null){
                   sb.append(line);
                   line = br.readLine();
                }

            }
        } catch (IOException e) {
                Log.e("LOG_TAG","Error response code : "+e.getMessage());
        } finally {

            if (urlConnection != null)
                urlConnection.disconnect();
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return sb.toString();
    }





}