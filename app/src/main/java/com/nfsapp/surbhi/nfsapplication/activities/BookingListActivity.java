package com.nfsapp.surbhi.nfsapplication.activities;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.activities.traveller.BookItemActivity;
import com.nfsapp.surbhi.nfsapplication.adapter.BookingListAdapter;
import com.nfsapp.surbhi.nfsapplication.beans.Confirmpackage;
import com.nfsapp.surbhi.nfsapplication.constants.GPSTracker;
import com.nfsapp.surbhi.nfsapplication.other.GifImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.nfsapp.surbhi.nfsapplication.other.MySharedPref.getData;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.BASE_URL_NEW;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.makeToast;

public class BookingListActivity extends AppCompatActivity {

    ListView recyclerView;
    double latitude = 0.0, longitude = 0.0;
    private String product_id = "",booking_user_type="";
    private Spinner payment_spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_list);

        ImageView back_btn = findViewById(R.id.back_btn);
        TextView header_text = findViewById(R.id.header_text);
        payment_spinner = findViewById(R.id.sortby_spinner);

        recyclerView = findViewById(R.id.recycler_view);
        final PullRefreshLayout refreshLayout = findViewById(R.id.refreshLay);

        GPSTracker gps = new GPSTracker(this);
        latitude = gps.getLatitude();
        longitude = gps.getLongitude();
        System.out.println("============== current location =========");
        System.out.println(latitude);
        System.out.println(longitude);

        header_text.setText("My Bookings");
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        ArrayList<String> list = new ArrayList<>();
        list.add("Sort By Both");
        list.add("Sender");
        list.add("Traveller");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(BookingListActivity.this,R.layout.support_simple_spinner_dropdown_item, list);
        payment_spinner.setAdapter(adapter);

        payment_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getSelectedItem().toString();

                if (selectedItem.equalsIgnoreCase("Sort By Both"))
                {
                    booking_user_type="";
                    prepareTravellerData(false,booking_user_type);

                }
                else if (selectedItem.equalsIgnoreCase("Sender"))
                {
                    booking_user_type="2";
                    prepareTravellerData(false,booking_user_type);


                } else if (selectedItem.equalsIgnoreCase("Traveller"))
                {
                    booking_user_type="1";
                    prepareTravellerData(false,booking_user_type);
                }
            }

            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });


        refreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_RING);
        refreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        prepareTravellerData(true,"");
                        booking_user_type="";
                        payment_spinner.setSelection(0);
                        refreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
    }

    private void prepareTravellerData(final boolean isrefreshed,final String booking_user_type) {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final   Dialog ringProgressDialog = new Dialog(BookingListActivity.this, R.style.Theme_AppCompat_Dialog);
            ringProgressDialog.setContentView(R.layout.loading);
            ringProgressDialog.setCancelable(false);
            ringProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            ringProgressDialog.show();
            GifImageView gifview = ringProgressDialog.findViewById(R.id.loaderGif);
            gifview.setGifImageResource(R.drawable.loader2);

        String userid = getData(BookingListActivity.this, "user_id", "");
        System.out.println("========== userid========== " + userid);

        params.put("user_id", userid);
        params.put("booking_user_type", booking_user_type);

        System.err.println(params);
        client.setConnectTimeout(60 * 1000);
        client.setResponseTimeout(60 * 1000);

        client.post(BASE_URL_NEW + "my_booking", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response);
                    ringProgressDialog.dismiss();
                try {
                    ArrayList<Confirmpackage> travellerList = new ArrayList<>();
                    BookingListAdapter mAdapter;
                    if (response.getString("status").equals("1")) {
                        JSONObject jsonObject;
                        String sender_id = "";
                        JSONArray jsonArray = response.getJSONArray("booking");

                        mAdapter = new BookingListAdapter(jsonArray, BookingListActivity.this);
                        mAdapter.notifyDataSetChanged();
                        recyclerView.setAdapter(mAdapter);
                    } else {
                        makeToast(BookingListActivity.this, response.getString("message"));
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
        prepareTravellerData(false,booking_user_type);
    }
}