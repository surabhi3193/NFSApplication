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
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.adapter.NotificationAdapter;
import com.nfsapp.surbhi.nfsapplication.beans.Notification;
import com.nfsapp.surbhi.nfsapplication.other.GifImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.nfsapp.surbhi.nfsapplication.other.MySharedPref.getData;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.BASE_URL_NEW;

public class NotificationFragment extends Fragment {

    ListView listView;

    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("====== fragment notify==========");
        View v = inflater.inflate(R.layout.fragment_notification, null);

        listView =v.findViewById(R.id.listview);
        getNotification();
        return v;
    }

    private void getNotification() {
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

        System.out.println("============= notification_list ==============");
        System.err.println(params);
        client.post(BASE_URL_NEW + "notification_list", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response);
                ringProgressDialog.dismiss();
                try {

                    if (response.getString("status").equals("1")) {

                        ArrayList<Notification> notificationList = new ArrayList<>();
                        JSONArray jsonArray = response.getJSONArray("notifications");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String notification_id = jsonObject.getString("notification_id");
                            String sender_id = jsonObject.getString("sender_name");
                            String reciever_id = jsonObject.getString("reciever_id");
                            String noti_message = jsonObject.getString("noti_message");
                            String noti_date = jsonObject.getString("noti_date");
                            String image = jsonObject.getString("sender_pic");

                            Notification notification = new Notification(notification_id,image, sender_id, noti_message, noti_date);
                            notificationList.add(notification);

                        }
                        NotificationAdapter mAdapter = new NotificationAdapter(notificationList, getActivity());
                        mAdapter.notifyDataSetChanged();
                        listView.setAdapter(mAdapter);
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
}