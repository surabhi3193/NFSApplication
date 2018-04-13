package com.nfsapp.surbhi.nfsapplication.activities.traveller;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.adapter.TravelerMainAdapter;
import com.nfsapp.surbhi.nfsapplication.beans.Traveller;
import com.nfsapp.surbhi.nfsapplication.constants.GPSTracker;
import com.nfsapp.surbhi.nfsapplication.other.GifImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.nfsapp.surbhi.nfsapplication.other.MySharedPref.getData;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.BASE_URL_NEW;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.makeToast;

public class TravellerMainActivity extends AppCompatActivity {


    ListView recyclerView;
    double latitude = 0.0, longitude = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traveller_main);

        ImageView back_btn = findViewById(R.id.back_btn);
        TextView header_text = findViewById(R.id.header_text);

        GPSTracker gps = new GPSTracker(this);
        latitude = gps.getLatitude();
        longitude = gps.getLongitude();


        System.out.println("============== current location =========");
        System.out.println(latitude);
        System.out.println(longitude);

        header_text.setText("Available packages for pickup");
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        final PullRefreshLayout refreshLayout = findViewById(R.id.refreshLay);

        refreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_RING);
        refreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        prepareTravellerData();
                        refreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });


        prepareTravellerData();
    }


    private void prepareTravellerData() {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final Dialog ringProgressDialog = new Dialog(TravellerMainActivity.this, R.style.Theme_AppCompat_Dialog);
        ringProgressDialog.setContentView(R.layout.loading);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ringProgressDialog.show();
        GifImageView gifview = ringProgressDialog.findViewById(R.id.loaderGif);
        gifview.setGifImageResource(R.drawable.loader2);

        String userid = getData(TravellerMainActivity.this, "user_id", "");
        System.out.println("========== userid========== " + userid);

        params.put("user_id", userid);
        params.put("latitude", latitude);
        params.put("longitude", longitude);

        System.out.println("============= get item  api ==============");
        System.err.println(params);
        client.post(BASE_URL_NEW + "trevaller_post_list", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response);
                ringProgressDialog.dismiss();
                try {

                    if (response.getString("status").equals("1")) {
                        ArrayList<Traveller> travellerList = new ArrayList<>();
                        String sender_id = "";
                        JSONArray jsonArray = response.getJSONArray("post");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String product_id = jsonObject.getString("product_id");
                            String product_name = jsonObject.getString("product_name");
                            String pickup_location = jsonObject.getString("pickup_location");
                            String destination_location = jsonObject.getString("destination_location");
                            String product_pic = jsonObject.getString("product_pic");
                            String departure_date = jsonObject.getString("departure_date");
                            sender_id = jsonObject.getString("sender_id");

                            String amount = jsonObject.getString("prodcust_cost");
                            String distence = jsonObject.getString("distence");

                            Traveller movie = new Traveller(product_id, sender_id, product_name, product_pic, pickup_location, destination_location, departure_date, distence + " miles", "$" + amount);
                            travellerList.add(movie);

                        }
                        TravelerMainAdapter mAdapter;
                        mAdapter = new TravelerMainAdapter(travellerList, TravellerMainActivity.this);
                        mAdapter.notifyDataSetChanged();
                        recyclerView.setAdapter(mAdapter);
                    } else {
                        makeToast(TravellerMainActivity.this, response.getString("message"));
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                ringProgressDialog.dismiss();
                System.out.println(errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                ringProgressDialog.dismiss();
                System.out.println(responseString);
            }
        });
    }

}
