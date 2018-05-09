package com.nfsapp.surbhi.nfsapplication.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.adapter.TravellerAdapter;
import com.nfsapp.surbhi.nfsapplication.beans.Traveller;
import com.nfsapp.surbhi.nfsapplication.other.GifImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.nfsapp.surbhi.nfsapplication.other.MySharedPref.getData;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.BASE_URL_NEW;

public class TravellerListFragment extends Fragment {



    ListView recyclerView;

    private PullRefreshLayout refreshLayout;

    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("====== fragment sender==========");
        View v = inflater.inflate(R.layout.fragment_traveler_list, null);


        recyclerView = v.findViewById(R.id.recycler_view);
        EditText msearch = v.findViewById(R.id.msearch);

        msearch.setVisibility(View.VISIBLE);
        prepareTravellerData();

        refreshLayout = v.findViewById(R.id.refreshLay);
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
        return v;
    }
    private void prepareTravellerData() {
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

        System.out.println("============= get traveller  api ==============");
        System.err.println(params);
        client.post(BASE_URL_NEW + "trevaller_list", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response);
                ringProgressDialog.dismiss();
                try {

                    if (response.getString("status").equals("1")) {

                        ArrayList<Traveller> travellerList = new ArrayList<>();
                        JSONArray jsonArray = response.getJSONArray("post");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String traveler_id = jsonObject.getString("trevaller_id");
                            String trevaller_name = jsonObject.getString("trevaller_name");
                            String departure = jsonObject.getString("departure");
                            String arrival = jsonObject.getString("arrival");
                            String user_pic = jsonObject.getString("user_pic");
                            String date = jsonObject.getString("departure_date");
                            String product_id = jsonObject.getString("product_id");
                            trevaller_name = trevaller_name.split(" ")[0];

                            Traveller movie = new Traveller(traveler_id,product_id, trevaller_name, user_pic,
                                    "Departure : " + departure,
                                    "Arrival : " + arrival, date, "","","");
                            travellerList.add(movie);

                        }
                        TravellerAdapter  mAdapter = new TravellerAdapter(travellerList, getActivity(),"traveller");
                        mAdapter.notifyDataSetChanged();
                        recyclerView.setAdapter(mAdapter);
                    } else {
//                        makeToast(getActivity(), response.getString("message"));
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