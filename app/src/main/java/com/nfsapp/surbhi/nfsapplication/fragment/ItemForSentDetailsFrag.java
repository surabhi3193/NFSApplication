package com.nfsapp.surbhi.nfsapplication.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.nfsapp.surbhi.nfsapplication.adapter.ItemListAdapter;

import com.nfsapp.surbhi.nfsapplication.beans.Traveller;
import com.nfsapp.surbhi.nfsapplication.other.GifImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.nfsapp.surbhi.nfsapplication.other.MySharedPref.getData;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.BASE_URL_NEW;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.makeToast;

public class ItemForSentDetailsFrag extends Fragment {

    private ListView recyclerView;
    private EditText msearch;
    private String city_name = "";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("====== fragment sender==========");
        View v = inflater.inflate(R.layout.fragment_traveler_list, null);
        recyclerView = v.findViewById(R.id.recycler_view);
        msearch = v.findViewById(R.id.msearch);

        prepareTravellerData(false, "");

        final PullRefreshLayout refreshLayout = v.findViewById(R.id.refreshLay);
        refreshLayout.setRefreshing(false);
        refreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_RING);
        refreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        prepareTravellerData(true, "");
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
                    prepareTravellerData(true, city_name);

                    return true;
                }
                return false;
            }

        });
        return v;
    }

    private void prepareTravellerData(final boolean isrefreshed, final String city) {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final Dialog ringProgressDialog = new Dialog(getActivity(), R.style.Theme_AppCompat_Dialog);
        ringProgressDialog.setContentView(R.layout.loading);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ringProgressDialog.show();
        GifImageView gifview = ringProgressDialog.findViewById(R.id.loaderGif);
        gifview.setGifImageResource(R.drawable.loader2);

        String userid = getData(getActivity(), "user_id", "");
        System.out.println("========== userid========== " + userid);

        params.put("user_id", userid);
        params.put("city_name", city);

        System.out.println("============= get item  api ==============");
        System.err.println(params);
        client.post(BASE_URL_NEW + "post_list", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response);

                ringProgressDialog.dismiss();

                try {

                    if (response.getString("status").equals("1")) {
                        ArrayList<Traveller> travellerList = new ArrayList<>();
                        JSONArray jsonArray = response.getJSONArray("post");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String product_id = jsonObject.getString("product_id");
                            String product_name = jsonObject.getString("product_name");
                            String pickup_location = jsonObject.getString("pickup_location");
                            String destination_location = jsonObject.getString("destination_location");
                            String product_pic = jsonObject.getString("product_pic");
                            String departure_date = jsonObject.getString("departure_date");

                            Traveller movie = new Traveller(product_id, "", product_name, product_pic,
                                    pickup_location, destination_location, departure_date, "", "", "");
                            travellerList.add(movie);

                        }
                        ItemListAdapter mAdapter = new ItemListAdapter(travellerList, getActivity());
                        mAdapter.notifyDataSetChanged();
                        recyclerView.setAdapter(mAdapter);
                    } else {
//                        makeToast(getActivity(), response.getString("message"));
                        return;
                    }
                } catch (Exception e) {
                    ringProgressDialog.dismiss();
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