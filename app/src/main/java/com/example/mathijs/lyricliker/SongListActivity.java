package com.example.mathijs.lyricliker;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class SongListActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        FragmentManager fm = getSupportFragmentManager();
        AlbumFragment fragment = new AlbumFragment();
        FragmentTransaction ft = fm.beginTransaction();
        // The initial fragment is the list of albums
        ft.replace(R.id.fragment_container, fragment, "albums");
        ft.commit();

        // Set up listeners for the floating action buttons
        final FloatingActionButton favFabView = (FloatingActionButton) findViewById(R.id.favoriteList_fab);
        final FloatingActionButton logoutFabView = (FloatingActionButton) findViewById(R.id.logout_fab);
        favFabView.setOnClickListener(new SongListActivity.favoriteListListener());
        logoutFabView.setOnClickListener(new SongListActivity.logoutListener());

        mAuth = FirebaseAuth.getInstance();

    }

    private class favoriteListListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent1 = new Intent(getApplicationContext(), FavouriteListActivity.class);
            startActivity(intent1);
        }
    }

    private class logoutListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mAuth.signOut();
            Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent2);
        }
    }
}