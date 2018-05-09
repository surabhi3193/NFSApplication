package com.nfsapp.surbhi.nfsapplication.activities.sender;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nfsapp.surbhi.nfsapplication.R;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.nfsapp.surbhi.nfsapplication.other.MySharedPref.getData;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.BASE_URL_NEW;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.confirmBooking;

public class TravellerDetails extends AppCompatActivity {

    String traveller_tckt = "";
    RelativeLayout imageLay;

    private TextView nameTV,pickupET,pickypdateEt,destTV,destDateTV,flightTV,header_text;
    private ImageView ticketIV;

    private String trevaller_id="",product_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traveller_details);
         ticketIV = findViewById(R.id.ticketIV);
        ImageView back_btn = findViewById(R.id.back_btn);
        ImageView image_back = findViewById(R.id.image_back);
        final ImageView image_full = findViewById(R.id.image_full);
         header_text = findViewById(R.id.header_text);

        imageLay = findViewById(R.id.imageLay);
         nameTV = findViewById(R.id.nameTV);
         pickupET = findViewById(R.id.pickupET);
         pickypdateEt = findViewById(R.id.pickypdateEt);
         destTV = findViewById(R.id.destTV);
         destDateTV = findViewById(R.id.destDateTV);
         flightTV = findViewById(R.id.flightTV);
        Button accept_btn = findViewById(R.id.accept_btn);
        Button reject_btn = findViewById(R.id.reject_btn);


        Bundle bundle = getIntent().getExtras();

        if (bundle != null)
        {
            try {
//                 productDetails = bundle.getString("productDetails");
                String act_name = bundle.getString("act_name", "");
                String trevaller_id = bundle.getString("trevaller_id", "");

                System.err.println("====== act_name=========");
                System.out.println(act_name);


                if (act_name.equalsIgnoreCase("notification"))
                {
                    act_name="invitations";
                }

                if (act_name.equalsIgnoreCase("invitations"))
                {
                    product_id=getData(getApplicationContext(),"product_id","");
                    accept_btn.setVisibility(View.VISIBLE);
                    reject_btn.setVisibility(View.VISIBLE);
                } else {
                    product_id=getData(getApplicationContext(),"product_id","");

                    accept_btn.setVisibility(View.GONE);
                    reject_btn.setVisibility(View.GONE);
                }
                getTraveller(trevaller_id);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }

        });


        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageLay.setVisibility(View.GONE);
            }
        });

        ticketIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageLay.setVisibility(View.VISIBLE);
                if (traveller_tckt.length() > 0)
                    Picasso.with(getApplicationContext()).load(traveller_tckt).placeholder(R.drawable.no_pic).into(image_full);

            }
        });

        accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                makeToast(getApplicationContext(),"Under development ");
                confirmBooking(TravellerDetails.this, trevaller_id, product_id, "1");

            }
        });


        reject_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                makeToast(getApplicationContext(), "Under development ");
                confirmBooking(TravellerDetails.this, trevaller_id,product_id, "2");


            }
        });
    }

    @Override
    public void onBackPressed() {
        if (imageLay.getVisibility() == View.VISIBLE)
            imageLay.setVisibility(View.GONE);

        else
            finish();
    }

    public  void getTraveller(String traveller_id) {


        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        params.put("trevaller_id", traveller_id);
        params.put("product_id", product_id);
        System.out.println("========== details traveller  api =======");
        client.setTimeout(60 * 1000);
        client.setConnectTimeout(60 * 1000);
        client.setResponseTimeout(60 * 1000);
        System.out.println(params);
        client.post(BASE_URL_NEW + "trevaller_details", params, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                System.out.println(response);
                try {
                    String response_fav = response.getString("status");
                    if (response_fav.equals("1")) {
                        JSONObject obj = response.getJSONObject("result");
                        System.out.println(obj);
                        setData(obj);
                    }
                } catch (Exception e) {
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
    private void setData(JSONObject obj) {
        try {

            trevaller_id = obj.getString("trevaller_id");
            String trevaller_name = obj.getString("trevaller_name");
            String trevaller_from = obj.getString("trevaller_from");
            String departure_date = obj.getString("departure_date");
            String trevaller_to = obj.getString("trevaller_to");
            String arrival_date = obj.getString("arrival_date");
            String trevaller_flight_no = obj.getString("trevaller_flight_no");
            traveller_tckt = obj.getString("trevaller_ticket_pic");
          String  product_name = obj.getString("product_name");
            nameTV.setText(trevaller_name);
            pickupET.setText(trevaller_from);
            pickypdateEt.setText(departure_date);
            destTV.setText(trevaller_to);
            destDateTV.setText(arrival_date);
            flightTV.setText(trevaller_flight_no);
            header_text.setText(product_name);
            if (traveller_tckt.length() > 0)
                Picasso.with(getApplicationContext()).load(traveller_tckt).placeholder(R.drawable.no_pic).into(ticketIV);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}
