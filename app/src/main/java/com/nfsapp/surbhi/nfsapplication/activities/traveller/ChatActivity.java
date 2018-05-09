package com.nfsapp.surbhi.nfsapplication.activities.traveller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.adapter.ChatAdapter;
import com.nfsapp.surbhi.nfsapplication.beans.Confirmpackage;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.nfsapp.surbhi.nfsapplication.other.MySharedPref.getData;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.BASE_URL_NEW;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.makeToast;

public class ChatActivity extends AppCompatActivity {

    ListView msg_listview;
    private String product_id, sender_id;
    private EditText msgEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        msg_listview = findViewById(R.id.msg_list);
        ImageView oppn_img = findViewById(R.id.oppn_img);
        ImageView back_btn = findViewById(R.id.back_btn);
        ImageView send_btn = findViewById(R.id.send_btn);
        TextView oppon_name = findViewById(R.id.oppon_name);
        msgEt = findViewById(R.id.msgEt);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            product_id = bundle.getString("product_id", "");
            sender_id = bundle.getString("sender_id", "");
            String sender_name = bundle.getString("sender_name", "");
            String sender_image = bundle.getString("sender_image", "");
            oppon_name.setText(sender_name);
            Picasso.with(getApplicationContext()).load(sender_image).placeholder(R.drawable.profile_pic).into(oppn_img);
        }

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });


        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "";
                msg = msgEt.getText().toString();
                if (msg.length()>0)
                sendMessage(sender_id, msg, product_id);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        getmsgList(product_id);
    }

    private void getmsgList(final String product_id) {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

//        final Dialog ringProgressDialog = new Dialog(ChatActivity.this, R.style.Theme_AppCompat_Dialog);
//        ringProgressDialog.setContentView(R.layout.loading);
//        ringProgressDialog.setCancelable(false);
//        ringProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        ringProgressDialog.show();
//        GifImageView gifview = ringProgressDialog.findViewById(R.id.loaderGif);
//        gifview.setGifImageResource(R.drawable.loader2);

        String userid = getData(ChatActivity.this, "user_id", "");
        System.out.println("========== userid========== " + userid);

        params.put("user_id", userid);
        params.put("product_id", product_id);

        System.err.println(params);
        client.setConnectTimeout(60 * 1000);
        client.setResponseTimeout(60 * 1000);

        client.post(BASE_URL_NEW + "chat_details", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response);
//                ringProgressDialog.dismiss();
                try {
                    ArrayList<Confirmpackage> travellerList = new ArrayList<>();
                    ChatAdapter mAdapter;
                    if (response.getString("status").equals("1")) {
                        msg_listview.setVisibility(View.VISIBLE);
                        JSONArray jsonArray = response.getJSONArray("chat");

                        mAdapter = new ChatAdapter(jsonArray, ChatActivity.this);
                        mAdapter.notifyDataSetChanged();
                        msg_listview.setAdapter(mAdapter);
                        msg_listview.setSelection(msg_listview.getAdapter().getCount() - 1);
                    } else {
                        makeToast(ChatActivity.this, response.getString("message"));
                        travellerList.clear();
                        msg_listview.setVisibility(View.GONE);
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                ringProgressDialog.dismiss();
                System.out.println(errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                ringProgressDialog.dismiss();
                System.out.println(responseString);
            }
        });
    }

    private void sendMessage(final String chat_reciever_id,
                             final String chat_msg, final String chat_product_id) {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();


        String userid = getData(ChatActivity.this, "user_id", "");
        System.out.println("========== userid========== " + userid);

        params.put("chat_sender_id", userid);
        params.put("chat_reciever_id", chat_reciever_id);
        params.put("chat_msg", chat_msg);
        params.put("chat_product_id", chat_product_id);

        System.err.println(params);
        client.setConnectTimeout(60 * 1000);
        client.setResponseTimeout(60 * 1000);

        client.post(BASE_URL_NEW + "chat", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response);
//                ringProgressDialog.dismiss();
                try {
                    ArrayList<Confirmpackage> travellerList = new ArrayList<>();
                    ChatAdapter mAdapter;
                    if (response.getString("status").equals("1")) {
                        msgEt.setText("");
                        getmsgList(product_id);
//                        JSONObject jsonObject;
//                        String sender_id = "";
//                        JSONArray jsonArray = response.getJSONArray("chat");
//
//                        mAdapter = new ChatAdapter(jsonArray, ChatActivity.this);
//                        mAdapter.notifyDataSetChanged();
//                        msg_listview.setAdapter(mAdapter);

                    } else {
                        makeToast(ChatActivity.this, response.getString("message"));
                        travellerList.clear();
                        msg_listview.setVisibility(View.GONE);
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                ringProgressDialog.dismiss();
                System.out.println(errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                ringProgressDialog.dismiss();
                System.out.println(responseString);
            }
        });
    }


}