package com.kenn.neu.xkcd;

import java.io.Serializable;

/**
 * Created by user on 14/07/2017.
 */

public interface IAsyncTaskListener {
    void onPreExecute();
    void onPostExecute(Serializable result);
}
