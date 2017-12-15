package com.example.mathijs.lyricliker;


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
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumFragment extends ListFragment {

    // Album names are hardcoded, because they couldn't be queried in the api
    final List<String> albums = Arrays.asList("the_college_dropout",
            "late_registration",
            "graduation",
            "808s_&amp;_heartbreak",
            "my_beautiful_dark_twisted_fantasy",
            "yeezus",
            "the_life_of_pablo",
            "watch_the_throne");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_list_item_1,
                formatStringList(albums)
        );
        this.setListAdapter(adapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_album, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        SongFragment songFragment = new SongFragment();

        // Send the album title to the next fragment that contains all songs of that album
        String clickedItem = reverseFormat(l.getItemAtPosition(position).toString());
        Bundle args = new Bundle();
        args.putString("album", clickedItem);
        songFragment.setArguments(args);

        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, songFragment)
                .addToBackStack(null)
                .commit();
    }

    public static String formatString(String string) {
        // Make the ugly strings retrieved from the api more readable by humans
        return string.replaceAll("_", " ")
                .replaceAll("&amp;", "&");
    }

    public static List<String> formatStringList(List<String> stringList) {
        // Formats an entire list
        List<String> formattedStringList = new ArrayList<String>();
        for (int i = 0; i < stringList.size(); i++) {
            formattedStringList.add(formatString(stringList.get(i)));
        }
        return formattedStringList;
    }

    public static String reverseFormat(String string) {
        // Reverses the prettier format for when the api needs to be queried again
        return string.replaceAll(" ", "_")
                .replaceAll("&", "&amp;");
    }
}
