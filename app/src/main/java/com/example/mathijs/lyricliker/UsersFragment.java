package com.example.mathijs.lyricliker;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersFragment extends ListFragment {

    ArrayList<String> songList = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getUserList();

        adapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_list_item_1,
                songList
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_users, container, false);
    }

    private void getUserList() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Get a list of all songs liked by the user (stored under used id)
                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DataSnapshot songs = dataSnapshot.child("lists").child(userID);
                for (DataSnapshot child : songs.getChildren())  {
                    songList.add(child.getValue(String.class));
                }
                setListAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("cancel", "loadPost:onCancelled", databaseError.toException());
            }
        };
        FirebaseDatabase.getInstance().getReference().addValueEventListener(postListener);
    }
}
