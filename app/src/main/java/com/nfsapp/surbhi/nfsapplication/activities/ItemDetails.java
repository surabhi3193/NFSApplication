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

import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.activities.sender.RequestList;
import com.nfsapp.surbhi.nfsapplication.adapter.SliderAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class ItemDetails extends AppCompatActivity {


    private ViewPager mPager;
    private CircleIndicator indicator;
    private String product_id="";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        mPager = findViewById(R.id.pager);
        indicator =findViewById(R.id.indicator);
     TextView productNameTv =findViewById(R.id.productNameTv);
     TextView destinationTV =findViewById(R.id.destinationTV);
     TextView pickupTV =findViewById(R.id.pickupTV);

     TextView paymentTV =findViewById(R.id.paymentTV);
     TextView descTV =findViewById(R.id.descTV);
     TextView weightTV =findViewById(R.id.weightTV);
     TextView priceTV =findViewById(R.id.priceTV);
     TextView total_countTV =findViewById(R.id.total_countTV);
     Button request_btn =findViewById(R.id.request_btn);
        ImageView image = findViewById(R.id.back_btn);
        request_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ItemDetails.this, RequestList.class)
                        .putExtra("product_id",product_id));
            }
        });


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        Bundle bundle = getIntent().getExtras();

        if (bundle!=null)
        {
            try {

                String productDetails = bundle.getString("productDetails");

                JSONObject obj = new JSONObject(productDetails);
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
                product_id = obj.getString("post_id");

                productNameTv.setText(product_name);
                pickupTV.setText(pickup_location);
                destinationTV.setText(destination_location);

                priceTV.setText(product_cost);
                weightTV.setText(product_weight);
                descTV.setText(product_desc);
                paymentTV.setText(payment_mode);

                if (booking_status.equalsIgnoreCase("2"))
                {
                    total_countTV.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.booked,0);
                    total_countTV.setText("Booked");
                    total_countTV.setTextColor(getResources().getColor(R.color.book_green));
                }
                else {
                    total_countTV.setCompoundDrawablesWithIntrinsicBounds(R.drawable.request,0,0,0);
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
}
