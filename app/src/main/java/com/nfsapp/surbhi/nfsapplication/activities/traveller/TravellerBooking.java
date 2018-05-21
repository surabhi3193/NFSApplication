package com.nfsapp.surbhi.nfsapplication.activities.traveller;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.activities.BookingListActivity;
import com.nfsapp.surbhi.nfsapplication.adapter.BookingListAdapter;
import com.nfsapp.surbhi.nfsapplication.adapter.TravelerMainAdapter;
import com.nfsapp.surbhi.nfsapplication.beans.Confirmpackage;
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

public class TravellerBooking extends Fragment {


    ListView recyclerView;
    double latitude = 0.0, longitude = 0.0;
    private String product_id = "",booking_user_type="";
    
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("====== fragment sender==========");
        View v = inflater.inflate(R.layout.fragment_traveller_booking, null);
       
        recyclerView =v.findViewById(R.id.recycler_view);
        final PullRefreshLayout refreshLayout =v.findViewById(R.id.refreshLay);

        GPSTracker gps = new GPSTracker(getActivity());
        latitude = gps.getLatitude();
        longitude = gps.getLongitude();
        System.out.println("============== current location =========");
        System.out.println(latitude);
        System.out.println(longitude);
        
        refreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_RING);
        refreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        prepareTravellerData(true,"");
                        booking_user_type="";
                        refreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
        return v;
    }
    private void prepareTravellerData(final boolean isrefreshed,final String booking_user_type) {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final   Dialog ringProgressDialog = new Dialog(getActivity(), R.style.Theme_AppCompat_Dialog);
        ringProgressDialog.setContentView(R.layout.loading);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ringProgressDialog.show();
        GifImageView gifview = ringProgressDialog.findViewById(R.id.loaderGif);
        gifview.setGifImageResource(R.drawable.loader2);

        String userid = getData(getActivity(), "user_id", "");
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

                        mAdapter = new BookingListAdapter(jsonArray, getActivity());
                        mAdapter.notifyDataSetChanged();
                        recyclerView.setAdapter(mAdapter);
                    } else {
//                        makeToast(getActivity(), response.getString("message"));
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
    public void onResume() {
        super.onResume();
        prepareTravellerData(true,"");
    }
}