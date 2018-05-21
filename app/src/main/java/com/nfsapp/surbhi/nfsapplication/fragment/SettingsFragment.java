package com.nfsapp.surbhi.nfsapplication.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nfsapp.surbhi.nfsapplication.activities.ChangePasswordActivity;
import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.activities.BookingListActivity;
import com.nfsapp.surbhi.nfsapplication.activities.TrackFlightAware;

public class SettingsFragment extends Fragment {


    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("====== fragment settings  ==========");
        View v = inflater.inflate(R.layout.fragment_setting, null);

        TextView bookingTV = v.findViewById(R.id.bookingTV);
        TextView changePassTV = v.findViewById(R.id.changePassTV);
        TextView trackflightTV = v.findViewById(R.id.trackflightTV);

        bookingTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(getActivity(), BookingListActivity.class));
            }
        });

        changePassTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
            }
        });

        trackflightTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(getActivity(), TrackFlightAware.class));
            }
        });
        return v;
    }
}