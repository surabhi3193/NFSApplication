package com.nfsapp.surbhi.nfsapplication.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nfsapp.surbhi.nfsapplication.R;

import java.util.ArrayList;
import java.util.List;

public class SenderFragment extends Fragment {
    // Titles of the individual pages (displayed in tabs)
    TabLayout tabLayout;

    // The ViewPager is responsible for sliding pages (fragments) in and out upon user input
    private ViewPager mViewPager;

    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_sender, null);

        mViewPager = (ViewPager) v.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new MyPagerAdapter(getFragmentManager()));
        createViewPager(mViewPager);
        // Connect the tabs with the ViewPager (the setupWithViewPager method does this for us in
        // both directions, i.e. when a new tab is selected, the ViewPager switches to this page,
        // and when the ViewPager switches to a new page, the corresponding tab is selected)
        tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
        createTabIcons();
        return v;
    }

    private void createViewPager(ViewPager viewPager) {
        MyPagerAdapter adapter = new MyPagerAdapter(getFragmentManager());
        adapter.addFrag(new TravellerListFragment(), "Traveller Details");
        adapter.addFrag(new ItemForSentDetailsFrag(), "Item List");
        adapter.addFrag(new AddItemFragment(), "Add Item");
        viewPager.setAdapter(adapter);
    }

    private void createTabIcons() {

        TextView tabThree = (TextView) LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.custom_tab, null);
        tabThree.setText("Traveller Details");
        tabThree.setTextColor(getActivity().getResources().getColor(R.color.white));
        tabLayout.getTabAt(0).setCustomView(tabThree);

        TextView tabTwo = (TextView) LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Item List");
        tabTwo.setTextColor(getActivity().getResources().getColor(R.color.white));

        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabFour = (TextView) LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.custom_tab, null);
        tabFour.setText("Add Item");
        tabFour.setTextColor(getActivity().getResources().getColor(R.color.white));

        tabLayout.getTabAt(2).setCustomView(tabFour);

    }


    public class MyPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public MyPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}