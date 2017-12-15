package com.example.mathijs.lyricliker;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SongFragment extends ListFragment {

    List<String> songNames = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_song, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve the album title that was clicked in the previous fragment
        Bundle arguments = this.getArguments();
        final String album = arguments.getString("album");

        // Retrieve all songs from that album
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "http://www.kanyerest.xyz/api/album/" + album;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray items = response.getJSONArray("result");
                            for (int i = 0; i < items.length(); i++) {
                                // Get all song titles and put them in an ArrayList
                                String title = items.getJSONObject(i).optString("title");
                                songNames.add(title);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        setUpArrayAdapter();

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

    private void setUpArrayAdapter() {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(
                        getContext(),
                        android.R.layout.simple_list_item_1,
                        AlbumFragment.formatStringList(songNames)
                );
        this.setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        // Start the lyrics activity and give it the song title
        Intent intent = new Intent(getActivity(), LyricsActivity.class);
        String clickedItem = AlbumFragment.reverseFormat(l.getItemAtPosition(position).toString());
        intent.putExtra("title", clickedItem);

        startActivity(intent);
    }

}
