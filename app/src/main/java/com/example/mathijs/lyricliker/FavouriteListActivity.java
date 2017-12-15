package com.example.mathijs.lyricliker;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FavouriteListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_list);

        FragmentManager fm = getSupportFragmentManager();
        UsersFragment fragment = new UsersFragment();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container2, fragment);
        ft.commit();
    }
}
