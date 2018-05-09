package com.nfsapp.surbhi.nfsapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.anton46.stepsview.StepsView;
import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.activities.traveller.ChatActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.nfsapp.surbhi.nfsapplication.constants.GeocodingLocation.getAddressFromLatlng;
import static com.nfsapp.surbhi.nfsapplication.other.MySharedPref.getData;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.BASE_URL_NEW;

public class TrackingActivity extends AppCompatActivity {
    private String booking_status = "airport";
    private String product_id, sender_id, sender_name, sender_image_url;
    private TextView locationTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        ImageView product_img = findViewById(R.id.product_img);
        ImageView back_btn = findViewById(R.id.back_btn);
        TextView product_name = findViewById(R.id.product_name);
        TextView traveler_nameTV = findViewById(R.id.traveler_nameTV);
        TextView departureTv = findViewById(R.id.departureTv);
        TextView arrivalTv = findViewById(R.id.arrivalTv);
         locationTV = findViewById(R.id.locationTV);
//        TextView departuredateTV = findViewById(R.id.departuredateTV);
        Button chat_btn = findViewById(R.id.chat_btn);

        StepsView mStepsView = findViewById(R.id.stepsView);
        TextView header_text =findViewById(R.id.header_text);
        header_text.setText("Tracking Detail");

        String[] labels ={"Picked Up","On Road","On Airport"};
        mStepsView.setLabels(labels).setBarColorIndicator(getResources().getColor(R.color.gray_light));
        mStepsView.setLabelColorIndicator(getResources().getColor(R.color.colorPrimary));

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String response = bundle.getString("responseObj");
            try {
                JSONObject responseObj = new JSONObject(response);
                System.out.println("=========response obj at package after pickup =========");
                System.out.println(responseObj);

                String trevaller_to = responseObj.getString("trevaller_to");
                String trevaller_from = responseObj.getString("trevaller_from");
                String product = responseObj.getString("product_name");
                booking_status = responseObj.getString("booking_status");

                String type = responseObj.getString("type");
                String product_image_url = responseObj.getString("product_pic");

                String departure_date = responseObj.getString("departure_date");
                product_id = responseObj.getString("product_id");
                sender_id = responseObj.getString("id");
                sender_image_url = responseObj.getString("user_pic");
                sender_name = responseObj.getString("user_name");

//                departuredateTV.setText(departure_date);
                product_name.setText(product);
                traveler_nameTV.setText(sender_name);
                departureTv.setText(trevaller_from);
                arrivalTv.setText(trevaller_to);

//                Picasso.with(getApplicationContext()).load(sender_image_url).placeholder(R.drawable.profile_pic).into(senderIV);
                Picasso.with(getApplicationContext()).load(product_image_url).placeholder(R.drawable.no_pic).into(product_img);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (booking_status.equalsIgnoreCase("1"))
        {
            locationTV.setText("Traveler didn't pick up package yet");
            mStepsView.setProgressColorIndicator(getResources().getColor(R.color.Crimson))
                    .setCompletedPosition(0)
                    .drawView();
        }

       else if (booking_status.equalsIgnoreCase("2"))
        {
            mStepsView.setProgressColorIndicator(getResources().getColor(R.color.MediumPurple))
                    .setCompletedPosition(1)
                    .drawView();
            trackTraveler(sender_id);

        }

        else if (booking_status.equalsIgnoreCase("3"))
        {
            mStepsView.setProgressColorIndicator(getResources().getColor(R.color.green))
                    .setCompletedPosition(2)
                    .drawView();
        }


        chat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ChatActivity.class)
                        .putExtra("product_id", product_id)
                        .putExtra("sender_id", sender_id)
                        .putExtra("sender_name", sender_name)
                        .putExtra("sender_image", sender_image_url)
                );
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }

    private void trackTraveler(String sender_id) {

            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();

            final String userid = getData(TrackingActivity.this, "user_id", "");
            System.out.println("============ pickup code verify =============");

            params.put("traveller_id", sender_id);
            System.out.println(params);
            client.setConnectTimeout(30000);
            client.post(BASE_URL_NEW + "track_traveller", params, new JsonHttpResponseHandler() {

                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    System.out.println(response);

                    try {


                        if (response.getString("status").equalsIgnoreCase("1")) {
                            double traveller_lat = Double.parseDouble(response.getString("traveller_lat"));
                            double trevaller_log = Double.parseDouble(response.getString("trevaller_log"));

                            String city = getAddressFromLatlng(new LatLng(traveller_lat, trevaller_log),
                                    TrackingActivity.this, 1);

                            locationTV.setText(city);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    System.out.println(errorResponse);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    System.out.println(responseString);
                }
            });
        }

}
