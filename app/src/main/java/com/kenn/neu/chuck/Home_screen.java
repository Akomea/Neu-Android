package com.kenn.neu.chuck;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class Home_screen extends AppCompatActivity {

    private TextView textView;
    private Button button;
    private ProgressBar pbLoading;
    private EditText edtFirstName;
    private EditText edtLastName;

    private static final String CHUCK_QUERY = "http://api.icndb.com/jokes/random?limitTo=[nerdy]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        textView= (TextView) findViewById(R.id.textChuck);
        button = (Button) findViewById(R.id.loadButton);
        button.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
                loadChuckQuotes();
            }
        });
        pbLoading = (ProgressBar) findViewById(R.id.pb_loading);
        edtFirstName= (EditText) findViewById(R.id.fname);
        edtLastName= (EditText) findViewById(R.id.lname);
    }

    private void loadChuckQuotes() {
        //new ChuckQuoteTask().execute(CHUCK_QUERY);
        String firstName= edtFirstName.getText().toString();
        String lastName= edtLastName.getText().toString();
        URL url = NetworkUtils.buildUrl(firstName, lastName);
        new ChuckQuoteTask().execute(url);



    }
    /**
     * This class extends AsyncTask to execute the query out of the main thread.
     * We should always not run a time consuming task on main thread.
     */
    private class ChuckQuoteTask extends AsyncTask<URL, Object, String> {

            @Override
        protected void onPreExecute() {
            // Here we prepare what we need to be done on the UI
            // For example, show a progress bar for a download task.
            // This method is executed on main thread.
            pbLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {


                String result = null;

                try {
                    //URL url = new URL(params[0]);
                    URL url = params[0];

                    result = NetworkUtils.getResponseFromHttpUrl(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return result;
            }


        @Override
        protected void onPostExecute(String result) {
            // This is method is executed after the task is over.
            // Since we use this task to get a String.
            // We should have a function to let us render what we got after the task
            // And here the function is onPostExecute()


            // This function is executed on main thread as well.
            if (!TextUtils.isEmpty(result)) {
                try {
                    String joke = extractJokeFromJson(result);
                    textView.setText(joke);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                pbLoading.setVisibility(View.GONE);
            }

        }

        private String extractJokeFromJson(String json) throws JSONException, NullPointerException {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject value = jsonObject.optJSONObject("value");
            String joke = value.optString("joke");
            return joke;
        }


    }

}
