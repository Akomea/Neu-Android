package com.kenn.neu.xkcd;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.kenn.neu.xkcd.IAsyncTaskListener;

import java.io.IOException;
import java.net.URL;

class XkcdQueryTask extends AsyncTask<URL, Object, String> {

    private IAsyncTaskListener listener;

    public XkcdQueryTask(IAsyncTaskListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        listener.onPreExecute();
    }

    @Override
    protected String doInBackground(URL... params) {
        String result = null;

        try {
            URL url = params[0];
            result = NetworkUtils.getResponseFromHttpUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        XkcdPic xPic = new Gson().fromJson(result, XkcdPic.class);
        listener.onPostExecute(xPic);
    }

}
