package com.nfsapp.surbhi.nfsapplication.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.beans.User;
import com.nfsapp.surbhi.nfsapplication.constants.Utility;
import com.nfsapp.surbhi.nfsapplication.fragment.ChatFragment;
import com.nfsapp.surbhi.nfsapplication.fragment.HomeFragment;
import com.nfsapp.surbhi.nfsapplication.fragment.NotificationFragment;
import com.nfsapp.surbhi.nfsapplication.fragment.ProfileFragment;
import com.nfsapp.surbhi.nfsapplication.fragment.SettingsFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.nfsapp.surbhi.nfsapplication.other.MySharedPref.getData;
import static com.nfsapp.surbhi.nfsapplication.other.MySharedPref.saveData;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.BASE_URL_NEW;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView home_img,  notify_img, settings_img, profile_img;
    public TextView nameTV;
    public ImageView profile_pic;
    FragmentManager fm;
    Fragment fragment;
//    chat_img

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();
        Utility.checkFINELOCATION(this);
        home_img = findViewById(R.id.home_img);
//        chat_img = findViewById(R.id.chat_img);
        notify_img = findViewById(R.id.notify_img);
        settings_img = findViewById(R.id.settings_img);
        profile_img = findViewById(R.id.profile_img);
        profile_pic = findViewById(R.id.image);
        nameTV = findViewById(R.id.nameTV);
        setFragment("home");
        fragment = new HomeFragment();
        replaceFragment(fragment, true, "home");
        home_img.setOnClickListener(this);
        notify_img.setOnClickListener(this);
        settings_img.setOnClickListener(this);
        profile_img.setOnClickListener(this);
        profile_pic.setOnClickListener(this);
    }

    private void checkNotification() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            System.err.println("=========== notification msg============ ");
            String msg = bundle.getString("notification_message", "");
            System.err.println(msg);
            if (msg.length() > 0)
            {
                setFragment("notification");
                fragment = new NotificationFragment();
                replaceFragment(fragment, true, "notification");
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        checkNotification();
       getUserProfile();
    }

    public void getUserProfile() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        String userid = getData(MainActivity.this, "user_id", "");
        System.out.println("-----> user profile ----------> ");
        params.put("user_id", userid);

        client.post(BASE_URL_NEW + "user_profile", params, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                System.err.println(response);

                try {
                    if (response.getString("status").equals("0")) {
                        Toast.makeText(MainActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    final User user = User.getInstance();

                    String street =response.getString("user_street");
                    String street2 =response.getString("user_street2");
                    String city =response.getString("user_city");
                    String user_state =response.getString("user_state");
                    String postal =response.getString("user_postalcode").trim();
                    String per = response.getString("profile_sttaus");

                    user.setId(response.getString("user_id"));
                    user.setProfile_pic(response.getString("user_pic"));
                    user.setfirstName(response.getString("first_name"));
                    user.setMiddleName(response.getString("middle_name"));
                    user.setLastName(response.getString("last_name"));
                    user.setStreet(street);
                    user.setStreet2(street2);
                    user.setCity(city);
                    user.setState(user_state);
                    user.setZipcode(postal);

                    user.setProfile_percent(per);
                    user.setEmail(response.getString("user_email"));
                    user.setPhone(response.getString("user_phone"));

                    user.setId_image(response.getString("valid_identity"));

                    nameTV.setText(user.getfirstName());

                    saveData(MainActivity.this,"profile_percent",per);


                    if (user.getProfile_pic()!=null && user.getProfile_pic().length()>0)
                    Picasso.with(getApplicationContext()).load(user.getProfile_pic()).placeholder(R.drawable.profile_pic).into(profile_pic);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                System.out.println(errorResponse);
            }

            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println(responseString);
            }
        });
    }

    @Override
    public void onClick(View view) {
        String fr = getSupportFragmentManager().findFragmentById(R.id.container).getTag();
        switch (view.getId()) {
            case R.id.home_img:
                if (!fr.equals("home")) {
                    setFragment("home");
                    fragment = new HomeFragment();
                    replaceFragment(fragment, true, "home");
                }
                break;
    /*        case R.id.chat_img:
                if (!fr.equals("chat")) {
                    setFragment("chat");
                    fragment = new ChatFragment();
                    replaceFragment(fragment, true, "chat");
                }
                break;*/
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
            case R.id.image:
                if (!fr.equals("profile")) {
                    setFragment("profile");
                    fragment = new ProfileFragment();
                    replaceFragment(fragment, true, "profile");
                }
                break;
        }

    }

    public void replaceFragment(Fragment fragment, boolean addToBackStack, String tag) {

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        if (addToBackStack) {
            transaction.addToBackStack(null);

        } else {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        transaction.replace(R.id.container, fragment, tag);
        transaction.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        String fr = "";
        System.err.println("onBackpressed==========");
        Fragment frag = getSupportFragmentManager().findFragmentById(R.id.container);
        System.out.println(frag);
        if (frag != null) {

            fr = getSupportFragmentManager().findFragmentById(R.id.container).getTag();
            System.out.println(fr);
            setFragment(fr);

        } else
            finishAffinity();

    }

    private void setFragment(String str) {
        switch (str) {
            case "home":
                home_img.setImageResource(R.drawable.home1);
//                chat_img.setImageResource(R.drawable.chat);
                notify_img.setImageResource(R.drawable.notification);
                settings_img.setImageResource(R.drawable.setting);
                profile_img.setImageResource(R.drawable.profie_footer);

                break;

            case "chat":
//                chat_img.setImageResource(R.drawable.chat1);
                home_img.setImageResource(R.drawable.home);
                notify_img.setImageResource(R.drawable.notification);
                settings_img.setImageResource(R.drawable.setting);
                profile_img.setImageResource(R.drawable.profie_footer);

                break;

            case "notification":
                notify_img.setImageResource(R.drawable.notification1);
//                chat_img.setImageResource(R.drawable.chat);
                home_img.setImageResource(R.drawable.home);
                settings_img.setImageResource(R.drawable.setting);
                profile_img.setImageResource(R.drawable.profie_footer);
                break;

            case "setting":
                settings_img.setImageResource(R.drawable.setting1);
//                chat_img.setImageResource(R.drawable.chat);
                notify_img.setImageResource(R.drawable.notification);
                home_img.setImageResource(R.drawable.home);
                profile_img.setImageResource(R.drawable.profie_footer);
                break;

            case "profile":
                profile_img.setImageResource(R.drawable.profil1);
//                chat_img.setImageResource(R.drawable.chat);
                notify_img.setImageResource(R.drawable.notification);
                settings_img.setImageResource(R.drawable.setting);
                home_img.setImageResource(R.drawable.home);

                break;

        }
    }

}
