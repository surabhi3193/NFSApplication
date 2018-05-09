package com.nfsapp.surbhi.nfsapplication.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.activities.sender.RequestList;
import com.nfsapp.surbhi.nfsapplication.adapter.SliderAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import me.relex.circleindicator.CircleIndicator;

import static com.nfsapp.surbhi.nfsapplication.other.MySharedPref.getData;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.BASE_URL_NEW;

public class ItemDetails extends AppCompatActivity {

    JSONObject responseDEtailsOBJ;
    TextView productNameTv, destinationTV, pickupTV, paymentTV, descTV, weightTV, priceTV, total_countTV,header_text;
    private ViewPager mPager;
    private CircleIndicator indicator;
    private String product_id = "", notification_type = "0";
    private Button request_btn;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        mPager = findViewById(R.id.pager);
        indicator = findViewById(R.id.indicator);
        productNameTv = findViewById(R.id.productNameTv);
        destinationTV = findViewById(R.id.destinationTV);
        pickupTV = findViewById(R.id.pickupTV);
        paymentTV = findViewById(R.id.paymentTV);
        descTV = findViewById(R.id.descTV);
        weightTV = findViewById(R.id.weightTV);
        priceTV = findViewById(R.id.priceTV);
        total_countTV = findViewById(R.id.total_countTV);
        request_btn = findViewById(R.id.request_btn);
        header_text = findViewById(R.id.header_text);
        ImageView image = findViewById(R.id.back_btn);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            product_id = bundle.getString("post_id");
            notification_type = bundle.getString("type","0");
        }

        if (notification_type.equalsIgnoreCase("3"))
        {

            total_countTV.setVisibility(View.GONE);
            request_btn.setText("Rejected");
            request_btn.setBackgroundResource(R.drawable.grey_bg);
        }
        else if (notification_type.equalsIgnoreCase("4")) {

            total_countTV.setVisibility(View.GONE);
            request_btn.setText("Accepted");
            request_btn.setBackgroundResource(R.drawable.green_rect);
        }
        else {
            total_countTV.setVisibility(View.VISIBLE);
            request_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    startActivity(new Intent(ItemDetails.this, RequestList.class)
                            .putExtra("product_id", product_id));

                }
            });
        }


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });


    }

    private void init(final ArrayList<Uri> imageArray) {
        final int[] currentPage = {0};

        mPager.setAdapter(new SliderAdapter(ItemDetails.this, imageArray));
        mPager.setBackgroundColor(getResources().getColor(R.color.black));
        indicator.setViewPager(mPager);

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage[0] == imageArray.size()) {
                    currentPage[0] = 0;
                }
                mPager.setCurrentItem(currentPage[0]++, true);
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        getItemDetails();
    }

    private void getItemDetails() {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();
//        final Dialog ringProgressDialog = new Dialog(getApplicationContext(), R.style.Theme_AppCompat_Dialog);
//        ringProgressDialog.setContentView(R.layout.loading);
//        ringProgressDialog.setCancelable(false);
//        ringProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        ringProgressDialog.show();
//        GifImageView gifview = ringProgressDialog.findViewById(R.id.loaderGif);
//        gifview.setGifImageResource(R.drawable.loader2);
        responseDEtailsOBJ = new JSONObject();
        String user_id = getData(getApplicationContext(), "user_id", "");

        params.put("user_id", user_id);
        params.put("post_id", product_id);
        client.setTimeout(60 * 1000);
        client.setConnectTimeout(60 * 1000);
        client.setResponseTimeout(60 * 1000);
        client.post(BASE_URL_NEW + "post_details", params, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {


//                ringProgressDialog.dismiss();
                System.out.println(response);
                try {
                    String response_fav = response.getString("status");
                    if (response_fav.equals("1")) {
                        JSONObject obj = response.getJSONObject("post");
                        System.out.println(obj);
                        responseDEtailsOBJ = obj;
                        addvalues(responseDEtailsOBJ);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                ringProgressDialog.dismiss();
                System.out.println("**** fav api ****fail***** " + product_id);
                System.out.println(errorResponse);
                responseDEtailsOBJ = errorResponse;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                // ringProgressDialog.dismiss();
                System.out.println("**** fave api ****fail***** " + product_id);
                System.out.println(responseString);

            }

        });
    }

    private void addvalues(JSONObject obj) {

        try {

            System.out.println("========== details post======");
            System.out.println(obj);
            String product_name = obj.getString("product_name");
            String pickup_location = obj.getString("pickup_location");
            String destination_location = obj.getString("destination_location");
            String product_desc = obj.getString("product_desc");
            String product_weight = obj.getString("product_weight");
            String product_cost = obj.getString("product_cost");
            String payment_mode = obj.getString("payment_mode");

            String product_pic = obj.getString("product_pic");
            String booking_status = obj.getString("booking_status");
            String trevaller_count = obj.getString("trevaller_count");


            header_text.setText(product_name);
            productNameTv.setText(product_name);
            pickupTV.setText(pickup_location);
            destinationTV.setText(destination_location);

            priceTV.setText(product_cost);
            weightTV.setText(product_weight);
            descTV.setText(product_desc);
            paymentTV.setText(payment_mode);

            if (booking_status.equalsIgnoreCase("2")) {
                total_countTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.booked, 0);
                total_countTV.setText("Booked");
                total_countTV.setTextColor(getResources().getColor(R.color.book_green));
            } else {
                total_countTV.setCompoundDrawablesWithIntrinsicBounds(R.drawable.request, 0, 0, 0);
                total_countTV.setText(trevaller_count + " Booking requests");
            }
            String[] uris = product_pic.split(",");

            ArrayList<Uri> imageArray = new ArrayList<>();
            for (String uri : uris) {
                imageArray.add(Uri.parse(uri));
            }

            init(imageArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
