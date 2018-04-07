package com.nfsapp.surbhi.nfsapplication.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jackandphantom.circularprogressbar.CircleProgressbar;
import com.nfsapp.surbhi.nfsapplication.R;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {


    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("====== fragment  profile =========");
        View v = inflater.inflate(R.layout.fragment_profile, null);

        CircleProgressbar circleProgressbar = (CircleProgressbar)v.findViewById(R.id.progress);

        int animationDuration = 1000; // 2500ms = 2,5s
        circleProgressbar.setProgressWithAnimation(62, animationDuration); // Default duration = 1500ms
        return v;
    }
}