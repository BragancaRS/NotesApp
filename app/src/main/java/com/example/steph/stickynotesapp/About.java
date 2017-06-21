package com.example.steph.stickynotesapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * The type About.
 */
public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.aboutmenu, menu);

        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
            switch (item.getItemId()) {

                case android.R.id.home:
                     finish();
                    return true;
                case R.id.action_settings:
                  intent=new Intent(this,Settings.class);
                    startActivity(intent);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }


    }

}
