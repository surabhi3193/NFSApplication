package com.nfsapp.surbhi.nfsapplication.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anton46.stepsview.StepsView;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.activities.traveller.ChatActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;

import static com.nfsapp.surbhi.nfsapplication.constants.GeocodingLocation.getAddressFromLatlng;
import static com.nfsapp.surbhi.nfsapplication.other.MySharedPref.getData;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.BASE_URL_NEW;

public class TrackingActivity extends AppCompatActivity  {
    private String booking_status = "airport";
    private String product_id, sender_id, sender_name, sender_image_url,product;
    private TextView locationTV,arrival_timeTV;
    private String flightno,departuredate,trevaller_from,trevaller_to,arrivaydate;
    private double traveller_lat=0.0,trevaller_log=0.0;
;
   MapView mapView ;

    @SuppressLint("ClickableViewAccessibility")
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

        Button chat_btn = findViewById(R.id.chat_btn);
        StepsView mStepsView = findViewById(R.id.stepsView);
        TextView header_text =findViewById(R.id.header_text);
        header_text.setText("Tracking Detail");
     /*   mapView = findViewById(R.id.map_view);
        mapView.getMapAsync(TrackingActivity.this);*/

        String[] labels ={"Picked Up","In Transit","Delivered"};
        mStepsView.setLabels(labels).setBarColorIndicator(getResources().getColor(R.color.gray_light));
        mStepsView.setLabelColorIndicator(getResources().getColor(R.color.colorPrimary));

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            String response = bundle.getString("responseObj");
            try {
                JSONObject responseObj = new JSONObject(response);
                System.out.println("=========response obj at package after pickup =========");
                System.out.println(responseObj);

                 trevaller_to = responseObj.getString("trevaller_to");
                 trevaller_from = responseObj.getString("trevaller_from");
                 product = responseObj.getString("product_name");
                String product_image_url = responseObj.getString("product_pic");

                departuredate = responseObj.getString("departure_date");
                arrivaydate = responseObj.getString("arrival_date");
                product_id = responseObj.getString("product_id");
                sender_id = responseObj.getString("id");
                sender_image_url = responseObj.getString("user_pic");
                sender_name = responseObj.getString("user_name");
                booking_status = responseObj.getString("booking_status");
                flightno = responseObj.getString("trevaller_flight_no");

                product_name.setText(product_id+"- "+product);
                traveler_nameTV.setText(sender_name);
                departureTv.setText(trevaller_from);
                arrivalTv.setText(trevaller_to);
                Picasso.with(getApplicationContext()).load(product_image_url).placeholder(R.drawable.no_pic).into(product_img);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (booking_status.equalsIgnoreCase("1"))
        {
            mStepsView.setVisibility(View.VISIBLE);
            locationTV.setText("Traveler didn't pick up package yet");
            mStepsView.setProgressColorIndicator(getResources().getColor(R.color.Crimson))
                    .setCompletedPosition(0)
                    .drawView();
        }
       else if (booking_status.equalsIgnoreCase("2"))
        {
            mStepsView.setVisibility(View.VISIBLE);
            mStepsView.setProgressColorIndicator(getResources().getColor(R.color.green))
                    .setCompletedPosition(1)
                    .drawView();
            trackTraveler(sender_id,trevaller_from,trevaller_to);

        }
        else if (booking_status.equalsIgnoreCase("3"))
        {
            locationTV.setText("Departed");
        }
        else if (booking_status.equalsIgnoreCase("4"))
        {
            chat_btn.setBackgroundResource(R.drawable.grey_bg);
            locationTV.setText("Delivered");
            mStepsView.setProgressColorIndicator(getResources().getColor(R.color.green))
                    .setCompletedPosition(2)
                    .drawView();

        }



        if (!booking_status.equalsIgnoreCase("4")) {
            chat_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), ChatActivity.class)
                            .putExtra("product_id", product_id)
                            .putExtra("product_name", product)
                            .putExtra("sender_id", sender_id)
                            .putExtra("sender_name", sender_name)
                            .putExtra("sender_image", sender_image_url)
                    );
                }
            });
        }

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }

    private void trackTraveler(String sender_id, String trevaller_from, String trevaller_to) {

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
                             traveller_lat = Double.parseDouble(response.getString("traveller_lat"));
                             trevaller_log = Double.parseDouble(response.getString("trevaller_log"));

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

/*    @Override
    public void onMapReady(GoogleMap googleMap) {

        GoogleMap mMap = googleMap;

        LatLng ny = new LatLng(traveller_lat, trevaller_log);

        System.out.println("--> latlng in may ready --->");
        System.out.println(traveller_lat);
        System.out.println(trevaller_log);
        CameraPosition.Builder camBuilder = CameraPosition.builder();
        camBuilder.bearing(45);
        camBuilder.tilt(30);
        camBuilder.target(ny);
        camBuilder.zoom(15);

        CameraPosition cp = camBuilder.build();

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
    }*/
}
