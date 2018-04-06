package com.nfsapp.surbhi.nfsapplication.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.activities.MainActivity;
import com.nfsapp.surbhi.nfsapplication.activities.sender.SenderMainActivity;
import com.nfsapp.surbhi.nfsapplication.activities.traveller.TravellerMainActivity;
import com.whygraphics.gifview.gif.GIFView;

public class HomeFragment extends Fragment {


    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("====== fragment home==========");
        View v = inflater.inflate(R.layout.fragment_home, null);
        GIFView mGifView =v.findViewById(R.id.main_activity_gif_vie);
        mGifView.setGifResource("asset:gif4");
        Button sender_btn = v.findViewById(R.id.sender_btn);
        Button receiver_btn = v.findViewById(R.id.receiver_btn);

        sender_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                SenderFragment fragment2 = new SenderFragment();
//                FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.replace(R.id.home_lay, fragment2);
//                fragmentTransaction.commit();
                startActivity(new Intent(getActivity(), SenderMainActivity.class));

            }
        });

        receiver_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                SenderFragment fragment2 = new SenderFragment();
//                FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.replace(R.id.home_lay, fragment2);
//                fragmentTransaction.commit();
                startActivity(new Intent(getActivity(), TravellerMainActivity.class));
            }
        });

        return v;
    }
}