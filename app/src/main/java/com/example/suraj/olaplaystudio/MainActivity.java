package com.example.suraj.olaplaystudio;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.suraj.olaplaystudio.util.FavList;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction=getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentHolder, MusicPlayer.newInstance()).commit();

        FlowManager.init(new FlowConfig.Builder(this).build());

    }
    @Override
    public void onPause() {
        super.onPause();


    }



}
