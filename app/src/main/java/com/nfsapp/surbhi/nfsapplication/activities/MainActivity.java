package com.nfsapp.surbhi.nfsapplication.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.fragment.ChatFragment;
import com.nfsapp.surbhi.nfsapplication.fragment.HomeFragment;
import com.nfsapp.surbhi.nfsapplication.fragment.NotificationFragment;
import com.nfsapp.surbhi.nfsapplication.fragment.ProfileFragment;
import com.nfsapp.surbhi.nfsapplication.fragment.SettingsFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView home_img, chat_img, notify_img, settings_img, profile_img;
    FragmentManager fm;
    Fragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();
        home_img = findViewById(R.id.home_img);
        chat_img = findViewById(R.id.chat_img);
        notify_img = findViewById(R.id.notify_img);
        settings_img = findViewById(R.id.settings_img);
        profile_img = findViewById(R.id.profile_img);

        setFragment("home");
        fragment = new HomeFragment();
        replaceFragment(fragment, true,"home");
        home_img.setOnClickListener(this);
        chat_img.setOnClickListener(this);
        notify_img.setOnClickListener(this);
        settings_img.setOnClickListener(this);
        profile_img.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        String fr=getSupportFragmentManager().findFragmentById(R.id.container).getTag();
        switch (view.getId()) {
            case R.id.home_img:
                if (!fr.equals("home")) {
                    setFragment("home");
                    fragment = new HomeFragment();
                    replaceFragment(fragment, true, "home");

                }
                break;
            case R.id.chat_img:
                if (!fr.equals("chat")) {
                    setFragment("chat");
                    fragment = new ChatFragment();
                    replaceFragment(fragment, true, "chat");
                }
                break;

            case R.id.notify_img:
                if (!fr.equals("notification")) {
                    setFragment("notification");
                    fragment = new NotificationFragment();
                    replaceFragment(fragment, true, "notification");
                }
                break;

            case R.id.settings_img:
                if (!fr.equals("setting")) {
                    setFragment("setting");
                    fragment = new SettingsFragment();
                    replaceFragment(fragment, true, "setting");
                }
                break;

            case R.id.profile_img:
                if (!fr.equals("profile")) {
                    setFragment("profile");
                    fragment = new ProfileFragment();
                    replaceFragment(fragment, true, "profile");
                }
                break;
        }

    }

    public void replaceFragment(Fragment fragment, boolean addToBackStack,String tag) {

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        if (addToBackStack) {
            transaction.addToBackStack(null);

        } else {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        }
        transaction.replace(R.id.container, fragment,tag);
        transaction.commit();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        String fr = "";
        System.err.println("onBackpressed==========");
             Fragment frag =getSupportFragmentManager().findFragmentById(R.id.container);
        System.out.println(frag);
             if (frag!=null)
             {

                 fr=getSupportFragmentManager().findFragmentById(R.id.container).getTag();
                 System.out.println(fr);
                setFragment(fr);

             }
             else
                 finish();

    }

    private void setFragment(String str) {
        switch (str)
        {
            case "home":
                home_img.setImageResource(R.drawable.home1);
                chat_img.setImageResource(R.drawable.chat);
                notify_img.setImageResource(R.drawable.notification);
                settings_img.setImageResource(R.drawable.setting);
                profile_img.setImageResource(R.drawable.profie_footer);

                break;

            case "chat":
                chat_img.setImageResource(R.drawable.chat1);
                home_img.setImageResource(R.drawable.home);
                notify_img.setImageResource(R.drawable.notification);
                settings_img.setImageResource(R.drawable.setting);
                profile_img.setImageResource(R.drawable.profie_footer);

                break;

            case "notification":

                notify_img.setImageResource(R.drawable.notification1);
                chat_img.setImageResource(R.drawable.chat);
                home_img.setImageResource(R.drawable.home);
                settings_img.setImageResource(R.drawable.setting);
                profile_img.setImageResource(R.drawable.profie_footer);
                break;

            case "setting":
                settings_img.setImageResource(R.drawable.setting1);
                chat_img.setImageResource(R.drawable.chat);
                notify_img.setImageResource(R.drawable.notification);
                home_img.setImageResource(R.drawable.home);
                profile_img.setImageResource(R.drawable.profie_footer);
                break;

            case "profile":
                profile_img.setImageResource(R.drawable.profil1);
                chat_img.setImageResource(R.drawable.chat);
                notify_img.setImageResource(R.drawable.notification);
                settings_img.setImageResource(R.drawable.setting);
                home_img.setImageResource(R.drawable.home);

                break;

        }
    }

}
