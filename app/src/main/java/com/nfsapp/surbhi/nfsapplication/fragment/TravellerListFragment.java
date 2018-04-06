package com.nfsapp.surbhi.nfsapplication.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.adapter.TravellerAdapter;
import com.nfsapp.surbhi.nfsapplication.beans.Traveller;

import java.util.ArrayList;
import java.util.List;

public class TravellerListFragment extends Fragment {

    TravellerAdapter mAdapter;
    private ArrayList<Traveller> travellerList = new ArrayList<>();

    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("====== fragment sender==========");
        View v = inflater.inflate(R.layout.fragment_traveler_list, null);


        ListView recyclerView = v.findViewById(R.id.recycler_view);
        EditText msearch = v.findViewById(R.id.msearch);
        msearch.setVisibility(View.VISIBLE);
        mAdapter = new TravellerAdapter(travellerList,getActivity());
        prepareTravellerData();
        recyclerView.setAdapter(mAdapter);
        return v;
    }

    private void prepareTravellerData() {
        Traveller movie = new Traveller("Adam Khan", "Departure : Chicago", "Arrival : Naperville", "04/03/2018", "50 KM");
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