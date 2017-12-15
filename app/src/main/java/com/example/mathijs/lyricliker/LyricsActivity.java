package com.example.mathijs.lyricliker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.Set;

public class LyricsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics);

        // Get the song title from the intent and insert it into the textView
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        final TextView titleView = (TextView) findViewById(R.id.songTitle);
        titleView.setText(AlbumFragment.formatString(title));

        // Fetch the lyrics from the api (and set it)
        getLyrics(title);

        // Set up a listener for the floating action button (which is used to favourite)
        final FloatingActionButton favFabView = (FloatingActionButton) findViewById(R.id.favorite_fab);
        favFabView.setOnClickListener(new favoriteListener());
    }

    private void getLyrics(String title) {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://www.kanyerest.xyz/api/track/" + title;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Get the lyrics from the api
                            String lyrics = response.getString("lyrics");
                            setLyrics(lyrics);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );

        queue.add(jsonObjectRequest);
    }

    private void setLyrics(String lyrics) {
        // Insert lyrics into the lyrics textview
        TextView lyricsView = (TextView) findViewById(R.id.songLyrics);
        lyricsView.setText(lyrics);
    }

    private class favoriteListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String title = getIntent().getStringExtra("title");

            // Insert the current song title in the database under the user id
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            mDatabase.child("lists").child(userID).child(title).setValue(title);

            Toast.makeText(getApplicationContext(), "Added to favourites.",
                    Toast.LENGTH_SHORT).show();

        }
    }
}

