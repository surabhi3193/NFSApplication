package com.nfsapp.surbhi.nfsapplication.activities.sender;

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
import com.nfsapp.surbhi.nfsapplication.adapter.TravellerAdapter;
import com.nfsapp.surbhi.nfsapplication.beans.Traveller;
import com.nfsapp.surbhi.nfsapplication.constants.GPSTracker;
import com.nfsapp.surbhi.nfsapplication.other.GifImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.nfsapp.surbhi.nfsapplication.other.MySharedPref.getData;
import static com.nfsapp.surbhi.nfsapplication.other.MySharedPref.saveData;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.BASE_URL_NEW;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.makeToast;

public class RequestList extends AppCompatActivity {

    ListView recyclerView;
    double latitude = 0.0, longitude = 0.0;
    private String product_id = "";


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

        header_text.setText("All Booking Requests");
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        final PullRefreshLayout refreshLayout = findViewById(R.id.refreshLay);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            product_id = bundle.getString("product_id", "");

        refreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_RING);
        refreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        prepareTravellerData(product_id);
                        refreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });


        prepareTravellerData(product_id);
    }

    private void prepareTravellerData(final String product_id) {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final Dialog ringProgressDialog = new Dialog(RequestList.this, R.style.Theme_AppCompat_Dialog);
        ringProgressDialog.setContentView(R.layout.loading);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ringProgressDialog.show();
        GifImageView gifview = ringProgressDialog.findViewById(R.id.loaderGif);
        gifview.setGifImageResource(R.drawable.loader2);

        String userid = getData(RequestList.this, "user_id", "");
        System.out.println("========== userid========== " + userid);

        params.put("user_id", userid);
        params.put("product_id", product_id);
        System.err.println(params);
        client.setConnectTimeout(60 * 1000);
        client.setResponseTimeout(60 * 1000);

        client.post(BASE_URL_NEW + "post_invitation_list", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response);
                ringProgressDialog.dismiss();
                try {
                    ArrayList<Traveller> travellerList = new ArrayList<>();
                    TravellerAdapter mAdapter ;
                    if (response.getString("status").equals("1"))
                    {
                        String sender_id = "";
                        JSONArray jsonArray = response.getJSONArray("invitations");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String trevaller_id = jsonObject.getString("trevaller_id");
                            String trevaller_name = jsonObject.getString("trevaller_name");
                            String departure = jsonObject.getString("departure");
                            String arrival = jsonObject.getString("arrival");
                            String user_pic = jsonObject.getString("user_pic");
                            String departure_date = jsonObject.getString("departure_date");
                            String booking_status = jsonObject.getString("booking_status");

                            saveData(getApplicationContext(), "product_id", product_id);
                            Traveller movie = new Traveller(trevaller_id, "", trevaller_name, user_pic,
                                    "Departure : " + departure, "Arrival : "
                                    + arrival, departure_date, "", "", booking_status);
                            travellerList.add(movie);

                        }

                         mAdapter = new TravellerAdapter(travellerList, RequestList.this, "invitations");
                        mAdapter.notifyDataSetChanged();
                        recyclerView.setAdapter(mAdapter);
                    }
                    else {
                        makeToast(RequestList.this, response.getString("message"));
                        travellerList.clear();
                        recyclerView.setVisibility(View.GONE);
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

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        prepareTravellerData(product_id);
    }
}
