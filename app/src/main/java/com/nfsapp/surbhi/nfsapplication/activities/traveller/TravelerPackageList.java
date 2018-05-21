package com.nfsapp.surbhi.nfsapplication.activities.traveller;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.adapter.TravelerMainAdapter;
import com.nfsapp.surbhi.nfsapplication.adapter.TravellerAdapter;
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

public class TravelerPackageList extends Fragment {


    ListView recyclerView;
    double latitude = 0.0, longitude = 0.0;
    private  String city_name="";
    
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("====== fragment sender==========");
        View v = inflater.inflate(R.layout.fragment_traveler_list, null);
        final EditText msearch =v.findViewById(R.id.msearch);

        GPSTracker gps = new GPSTracker(getActivity());
        latitude = gps.getLatitude();
        longitude = gps.getLongitude();


        System.out.println("============== current location =========");
        System.out.println(latitude);
        System.out.println(longitude);
        

        recyclerView =v.findViewById(R.id.recycler_view);
        final PullRefreshLayout refreshLayout =v.findViewById(R.id.refreshLay);

        refreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_RING);
        refreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        prepareTravellerData(true,"");
                        msearch.setText("");
                        refreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
        msearch.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    city_name = msearch.getText().toString();
                    prepareTravellerData(true,city_name);

                    return true;
                }
                return false;
            }

        });



        prepareTravellerData(false,city_name);
        return v;
    }


    private void prepareTravellerData(final boolean isrefreshed,final String city_name) {
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
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        params.put("city_name", city_name);
        System.out.println("============= get item  api ==============");
        System.err.println(params);
        client.setConnectTimeout(60 * 1000);
        client.setResponseTimeout(60 * 1000);
        client.post(BASE_URL_NEW + "trevaller_post_list", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response);
                ringProgressDialog.dismiss();
                try {

                    if (response.getString("status").equals("1")) {
                        ArrayList<Traveller> travellerList = new ArrayList<>();
                        String sender_id = "";
                        String trevaller_status = "";
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
                            trevaller_status = jsonObject.getString("trevaller_status");
                            Traveller movie = new Traveller(product_id, sender_id, product_name, product_pic, pickup_location, destination_location, departure_date, distence + " miles", amount, trevaller_status);
                            travellerList.add(movie);
                        }
                        TravelerMainAdapter mAdapter;
                        mAdapter = new TravelerMainAdapter(travellerList, getActivity());
                        mAdapter.notifyDataSetChanged();
                        recyclerView.setAdapter(mAdapter);
                    } else {
                        makeToast(getActivity(), response.getString("message"));
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
        prepareTravellerData(false,city_name);
    }
}
