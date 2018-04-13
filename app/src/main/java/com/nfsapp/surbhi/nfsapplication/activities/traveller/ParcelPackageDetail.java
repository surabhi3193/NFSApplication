package com.nfsapp.surbhi.nfsapplication.activities.traveller;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.activities.ItemDetails;
import com.nfsapp.surbhi.nfsapplication.adapter.SliderAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class ParcelPackageDetail extends AppCompatActivity {

    private ViewPager mPager;
    private CircleIndicator indicator;
    private String sender_id="",product_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcel_package_detail);

        mPager = findViewById(R.id.pager);
        indicator =findViewById(R.id.indicator);
        Button book_btn =findViewById(R.id.book_btn);

        ImageView back_btn = findViewById(R.id.back_btn);
        TextView header_text = findViewById(R.id.header_text);


        TextView productNameTv =findViewById(R.id.productNameTv);
        TextView destinationTV =findViewById(R.id.destinationTV);
        TextView pickupTV =findViewById(R.id.pickupTV);

        TextView paymentTV =findViewById(R.id.paymentTV);
        TextView descTV =findViewById(R.id.descTV);
        TextView weightTV =findViewById(R.id.weightTV);
        TextView priceTV =findViewById(R.id.priceTV);


        header_text.setText("Product Detail");
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              startActivity(new Intent(ParcelPackageDetail.this,BookItemActivity.class)
                      .putExtra("sender_id",sender_id)
                      .putExtra("post_id",product_id)
              );
            }
        });


        Bundle bundle = getIntent().getExtras();

        if (bundle!=null)
        {
            try {

                String productDetails = bundle.getString("productDetails");

                JSONObject obj = new JSONObject(productDetails);

                 sender_id = obj.getString("sender_id");
                 product_id = obj.getString("post_id");
                String product_name = obj.getString("product_name");
                String pickup_location = obj.getString("pickup_location");
                String destination_location = obj.getString("destination_location");
                String product_desc = obj.getString("product_desc");
                String product_weight = obj.getString("product_weight");
                String product_cost = obj.getString("product_cost");
                String payment_mode = obj.getString("payment_mode");
//                String reciever_name = obj.getString("reciever_name");
//                String reciever_phone = obj.getString("reciever_phone");
//                String reciever_email = obj.getString("reciever_email");
//                String product_insurence = obj.getString("product_insurence");
//                String product_added_date = obj.getString("product_added_date");

                String product_pic = obj.getString("product_pic");
                productNameTv.setText(product_name);
                pickupTV.setText(pickup_location);
                destinationTV.setText(destination_location);

                priceTV.setText(product_cost);
                weightTV.setText(product_weight);
                descTV.setText(product_desc);
                paymentTV.setText(payment_mode);

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

        mPager.setAdapter(new SliderAdapter(ParcelPackageDetail.this, imageArray));
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
//        Timer swipeTimer = new Timer();
//        swipeTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(Update);
//            }
//        }, 2500, 2500);
    }
}
