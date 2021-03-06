package com.udacity.gradle.builditbigger;

import android.os.AsyncTask;

import com.example.androidlib.JokeActivityLauncher;
import com.example.mannas.myapplication.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;


/**
 * Created by Mannas on 8/13/2017.
 */
public class EndpointsAsyncTask extends AsyncTask<Void, Void, String> {
    public static final String TAG = EndpointsAsyncTask.class.getName();

    private static MyApi myApiService = null;
    private JokeActivityLauncher launcher;
    public EndpointsAsyncTask(JokeActivityLauncher launcher){
        this.launcher = launcher;
    }

    @Override
    protected String doInBackground(Void... params) {
        if(myApiService == null) {  // Only do this once

            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver

            myApiService = builder.build();
        }
        try {
            return myApiService.sayAJoke().execute().getData();
        } catch (IOException e) {
            return TAG.concat(e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(String s) {
        if(launcher!=null)
            launcher.launchJokerActivity(s);
    }
}