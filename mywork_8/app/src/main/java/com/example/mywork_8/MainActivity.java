package com.example.mywork_8;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    class DownloadTask extends AsyncTask<Integer,Integer,String>{

        @Override
        protected String doInBackground(Integer... params){
            return null;
        }
    }

}