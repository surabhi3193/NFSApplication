package com.nfsapp.surbhi.nfsapplication.activities.traveller;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.adapter.TravelerMainAdapter;
import com.nfsapp.surbhi.nfsapplication.adapter.TravellerAdapter;
import com.nfsapp.surbhi.nfsapplication.beans.Traveller;
import com.nfsapp.surbhi.nfsapplication.fragment.AddItemFragment;
import com.nfsapp.surbhi.nfsapplication.fragment.ItemForSentDetailsFrag;
import com.nfsapp.surbhi.nfsapplication.fragment.TravellerListFragment;

import java.util.ArrayList;
import java.util.List;

public class TravellerMainActivity extends AppCompatActivity {

    TravelerMainAdapter mAdapter;
    private ArrayList<Traveller> travellerList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traveller_main);

        ImageView back_btn = findViewById(R.id.back_btn);
        TextView header_text = findViewById(R.id.header_text);

        header_text.setText("Available packages for pickup");
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        ListView recyclerView = findViewById(R.id.recycler_view);

        mAdapter = new TravelerMainAdapter(travellerList,TravellerMainActivity.this);
        prepareTravellerData();
        recyclerView.setAdapter(mAdapter);
    }

    private void prepareTravellerData() {
        Traveller movie = new Traveller("Laptop HP", "Departure : Chicago", "Arrival : Naperville", "04/03/2018", "50 KM");
        travellerList.add(movie);


        movie = new Traveller("Adam Khan", "Departure : Chicago", "Arrival : Naperville", "04/03/2018", "50 KM");
        travellerList.add(movie);

        movie = new Traveller("Adam Khan", "Departure : Chicago", "Arrival : Naperville", "04/03/2018", "50 KM");
        travellerList.add(movie);

        movie = new Traveller("Adam Khan", "Departure : Chicago", "Arrival : Naperville", "04/03/2018", "50 KM");
        travellerList.add(movie);

        movie = new Traveller("Adam Khan", "Departure : Chicago", "Arrival : Naperville", "04/03/2018", "50 KM");
        travellerList.add(movie);

        movie = new Traveller("Adam Khan", "Departure : Chicago", "Arrival : Naperville", "04/03/2018", "50 KM");
        travellerList.add(movie);

        movie = new Traveller("Adam Khan", "Departure : Chicago", "Arrival : Naperville", "04/03/2018", "50 KM");
        travellerList.add(movie);

        movie = new Traveller("Adam Khan", "Departure : Chicago", "Arrival : Naperville", "04/03/2018", "50 KM");
        travellerList.add(movie);


    }
}
