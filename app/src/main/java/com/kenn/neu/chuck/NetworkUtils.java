package com.kenn.neu.chuck;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Akomea Kenneth on 13/07/2017.
 */

public class NetworkUtils {
    private static final String CHUCK_QUERY_BASE_URL = "http://api.icndb.com/jokes/random?limitTo=[explicit]";
    private final static String PARAM_FIRST_NAME = "firstName";
    private final static String PARAM_LAST_NAME = "lastName";

    private final static String DEFAULT_FIRST_NAME = "Chuck";
    private final static String DEFAULT_LAST_NAME = "Norris";

    private final static String TAG = "NetworkUtils";
    public static URL buildUrl(String firstName, String lastName) {
        firstName = TextUtils.isEmpty(firstName) ? DEFAULT_FIRST_NAME : firstName;
        lastName = TextUtils.isEmpty(lastName) ? DEFAULT_LAST_NAME : lastName;

        Uri uri = Uri.parse(CHUCK_QUERY_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_FIRST_NAME, firstName)
                .appendQueryParameter(PARAM_LAST_NAME, lastName).build();
        URL url = null;
        try {
            String myUrl = uri.toString();
            Log.d(TAG, myUrl); // Add a log so we can check what URL will be used in debug.
            url = new URL(myUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
