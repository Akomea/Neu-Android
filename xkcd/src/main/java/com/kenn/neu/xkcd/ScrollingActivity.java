package com.kenn.neu.xkcd;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;


public class ScrollingActivity extends AppCompatActivity {
    private TextView tvTitle;
    private ImageView ivXkcdPic;
    private TextView tvCreateDate;
    private ProgressBar pbLoading;

    private final static String TAG = "MainActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivXkcdPic = (ImageView) findViewById(R.id.iv_xkcd_pic);
        tvCreateDate = (TextView) findViewById(R.id.tv_create_date);
        pbLoading = (ProgressBar) findViewById(R.id.pb_loading);

        loadXkcdPic();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    /**
     * Request current xkcd picture
     */
    private void loadXkcdPic(){
        try {
            URL url = new URL(NetworkUtils.XKCD_QUERY_BASE_URL);
            new XkcdQueryTask(listener).execute(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Request a specific xkcd picture
     * @param id the id of xkcd picture
     */

    private void loadXkcdPicById(int id) {
        String queryUrl = String.format(NetworkUtils.XKCD_QUERY_BY_ID_URL, id);

        try {
            URL url = new URL(queryUrl);
            new XkcdQueryTask(listener).execute(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Render img, text on the view
     * @param xPic
     */
    private void renderXkcdPic(XkcdPic xPic) {
        tvTitle.setText(xPic.num + ". " + xPic.title);
        Glide.with(this).load(xPic.img).into(ivXkcdPic);
        Log.d(TAG, "Pic to be loaded: " + xPic.img);
        tvCreateDate.setText("created on " + xPic.year + "." + xPic.month + "." + xPic.day);
    }

    private IAsyncTaskListener listener = new IAsyncTaskListener() {
        @Override
        public void onPreExecute() {
            pbLoading.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPostExecute(Serializable result) {
            pbLoading.setVisibility(View.GONE);
            if (result instanceof XkcdPic)
                renderXkcdPic((XkcdPic) result);
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_rand) {
            Random ran = new Random();
            int rid =ran.nextInt(1862-1);
            loadXkcdPicById(rid+1);
            return true;
        }else if (id==R.id.action_specific){

        }
        return super.onOptionsItemSelected(item);
    }
}





