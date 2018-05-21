package com.nfsapp.surbhi.nfsapplication.activities.sender;

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
import android.widget.TextView;

import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.beans.User;
import com.nfsapp.surbhi.nfsapplication.fragment.AddItemFragment;
import com.nfsapp.surbhi.nfsapplication.fragment.ItemForSentDetailsFrag;
import com.nfsapp.surbhi.nfsapplication.fragment.SenderFragment;
import com.nfsapp.surbhi.nfsapplication.fragment.TravellerListFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SenderMainActivity extends AppCompatActivity {
    // Titles of the individual pages (displayed in tabs)
    TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sender_main);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        createViewPager(mViewPager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
        TextView nameTV= findViewById(R.id.nameTV);
        ImageView profile_pic = findViewById(R.id.image);
        final User user = User.getInstance();


        nameTV.setText(user.getfirstName());
        if (user.getProfile_pic()!=null && user.getProfile_pic().length()>0)
            Picasso.with(getApplicationContext()).load(user.getProfile_pic()).placeholder(R.drawable.profile_pic).into(profile_pic);

        createTabIcons();

        ImageView back_btn = findViewById(R.id.back_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
    }
    private void createViewPager(ViewPager viewPager) {
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new TravellerListFragment(), "Traveller Details");
        adapter.addFrag(new ItemForSentDetailsFrag(), "Item List");
        adapter.addFrag(new AddItemFragment(), "Add Item");
        viewPager.setAdapter(adapter);
    }

    private void createTabIcons() {

        TextView tabThree = (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_tab, null);
        tabThree.setText("Traveller Details");
        tabThree.setTextColor(getResources().getColor(R.color.white));
        tabLayout.getTabAt(0).setCustomView(tabThree);

        TextView tabTwo = (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Item List");
        tabTwo.setTextColor(getResources().getColor(R.color.white));

        tabLayout.getTabAt(1).setCustomView(tabTwo);
        TextView tabFour = (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_tab, null);
        tabFour.setText("Add Item");
        tabFour.setTextColor(getResources().getColor(R.color.white));

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.out.println("-> on Backpressed main sender-->");

    }
}
