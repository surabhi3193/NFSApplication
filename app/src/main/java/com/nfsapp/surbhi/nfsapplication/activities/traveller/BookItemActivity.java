package com.nfsapp.surbhi.nfsapplication.activities.traveller;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.constants.GPSTracker;
import com.nfsapp.surbhi.nfsapplication.constants.Utility;
import com.nfsapp.surbhi.nfsapplication.other.GifImageView;
import com.nfsapp.surbhi.nfsapplication.other.NetworkClass;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

import static com.nfsapp.surbhi.nfsapplication.constants.GeocodingLocation.getAddressFromLatlng;
import static com.nfsapp.surbhi.nfsapplication.other.MySharedPref.getData;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.BASE_URL_NEW;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.getRealPathFromURI;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.makeToast;

public class BookItemActivity extends AppCompatActivity {

    private static final int PLACE_PICKER_REQUEST = 1;
    private static final int PLACE_PICKER_REQUEST_DEST = 2;
    private static final int ID_REQUEST_IMAGE = 3;
    private static final int TICKET_REQUEST_IMAGE = 4;
    private TextView dateTVdeparture, dateTVarrival,pickupET,destET;

    private EditText nameET, addressET, emailEt, phoneEt, flightEt;
    private Uri idimageUri;
    private Uri ticketImageurI;
    private String p_lat = "0.0", p_lng = "0.0";
    private String d_lat = "0.0", d_lng = "0.0";
    private ImageView idIV,ticketIV;
    private String sender_id="",post_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_item);

        Utility.checkCameraPermission(BookItemActivity.this);
        ImageView back_btn = findViewById(R.id.back_btn);
        TextView header_text = findViewById(R.id.header_text);

        TextView idTV = findViewById(R.id.uploadIDCardTV);
        TextView ticketTV = findViewById(R.id.uploadTicketTV);

        dateTVdeparture = findViewById(R.id.dateTVdepature);
        dateTVarrival = findViewById(R.id.dateTVarrival);
        nameET = findViewById(R.id.nameET);
        addressET = findViewById(R.id.addressET);
        emailEt = findViewById(R.id.emailEt);

        phoneEt = findViewById(R.id.phoneEt);

        pickupET = findViewById(R.id.pickupET);
        destET = findViewById(R.id.destET);
        flightEt = findViewById(R.id.flightEt);

        idIV = findViewById(R.id.idIV);
        ticketIV = findViewById(R.id.ticketIV);

        header_text.setText("Traveller Detail");

        Bundle bundle = getIntent().getExtras();

        if (bundle!=null)
        {
            sender_id= bundle.getString("sender_id");
            post_id= bundle.getString("post_id");
        }


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        Button book_btn = findViewById(R.id.book_btn);
        book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataFromIds();

            }
        });


        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener datedepart = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                NetworkClass.updateLabel(dateTVdeparture, myCalendar);
            }
        };

        final DatePickerDialog.OnDateSetListener datearrival = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                NetworkClass.updateLabel(dateTVarrival, myCalendar);
            }
        };

        dateTVdeparture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                DatePickerDialog d = new DatePickerDialog(BookItemActivity.this, datedepart, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                d.getDatePicker().setMinDate(System.currentTimeMillis());
                d.show();
            }
        });

        dateTVarrival.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                DatePickerDialog d = new DatePickerDialog(BookItemActivity.this, datearrival, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                d.getDatePicker().setMinDate(System.currentTimeMillis());
                d.show();
            }
        });

        pickupET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("====== pickup clicked======");
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(BookItemActivity.this), PLACE_PICKER_REQUEST);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        destET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("====== dest clicked======");
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(BookItemActivity.this), PLACE_PICKER_REQUEST_DEST);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        idTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getIDImage("id");
            }
        });
        ticketTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getIDImage("ticket");
            }
        });


    }

    private void getIDImage(String value) {
        boolean per = Utility.checkWriteStoragePermission(BookItemActivity.this);
        if (per) {
            String fileName = "Camera_Example.jpg";
            Uri imageUri;
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, fileName);
            values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");


            imageUri = getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            System.out.println("========== image uri ========= ");
            if (imageUri != null)
                System.out.println(imageUri.getPath());

            if (value.equals("id")) {
                idimageUri = imageUri;
                Intent intent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, idimageUri);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                startActivityForResult(intent, ID_REQUEST_IMAGE);

            } else if (value.equals("ticket")) {
                ticketImageurI = imageUri;
                Intent intent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, ticketImageurI);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                startActivityForResult(intent, TICKET_REQUEST_IMAGE);
            }
        }
    }

    private void warningBooking(final String traveller_name, final String address, final String email, final String phone, final String pickup, final String destination, final String dateDepart, final String dateArrival, final String flight, final String pathid, final String pathTicket) {
        final Dialog dialog = new Dialog(BookItemActivity.this, R.style.Theme_AppCompat_Dialog);
        dialog.setContentView(R.layout.alertdialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        TextView ok_btn = dialog.findViewById(R.id.ok_btn);
        TextView cancel_btn = dialog.findViewById(R.id.cancel_btn);
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                BookItem(traveller_name, address,email,phone,pickup,destination,dateDepart,dateArrival,flight,pathid,pathTicket);

//                startActivity(new Intent(BookItemActivity.this,TravellerMainActivity.class));
//                finish();
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void getDataFromIds() {

        String rec_mob_2 = "";
        String traveller_name = nameET.getText().toString();
        String address = addressET.getText().toString();
        String email = emailEt.getText().toString();
        String pickup = pickupET.getText().toString();
        String destination = destET.getText().toString();
        String dateArrival = dateTVarrival.getText().toString();
        String dateDepart = dateTVdeparture.getText().toString();
        String phone = phoneEt.getText().toString();
        String flight = flightEt.getText().toString();



        if (traveller_name.length() == 0) {
            makeToast(BookItemActivity.this, "Enter your name");
            return;
        }

        if (address.length() == 0) {
            makeToast(BookItemActivity.this, "Enter your address");
            return;
        }

        if (email.length() == 0) {
            makeToast(BookItemActivity.this, "Enter your email");
            return;
        }

        if (phone.length() == 0) {
            makeToast(BookItemActivity.this, "Enter your phone");
            return;
        }

        if (pickup.length() == 0) {
            makeToast(BookItemActivity.this, "Enter your departure location");
            return;
        }

        if (dateDepart.length() == 0) {
            makeToast(BookItemActivity.this, "Enter departure date");
            return;
        }

        if (destination.length() == 0) {
            makeToast(BookItemActivity.this, "Enter arrival location");
            return;
        }


        if (dateArrival.length() == 0) {
            makeToast(BookItemActivity.this, "Enter arrival  date");
            return;
        }
        if (flight.length() == 0) {
            makeToast(BookItemActivity.this, "Enter flight number");
            return;
        }

        if (idimageUri == null || idimageUri.getPath().length() == 0) {
            makeToast(BookItemActivity.this, "Valid id card picture required");
            return;

        }

        if (ticketImageurI == null || ticketImageurI.getPath().length() == 0) {
            makeToast(BookItemActivity.this, "Valid travelling ticket picture required");
            return;

        }

        String pathid = getRealPathFromURI(idimageUri,BookItemActivity.this);
        String pathTicket = getRealPathFromURI(ticketImageurI,BookItemActivity.this);
        warningBooking(traveller_name, address,email,phone,pickup,destination,dateDepart,dateArrival,flight,pathid,pathTicket);
    }

    private void BookItem(String traveller_name, String address, String email, String phone, String pickup, String destination, 
                          String dateDepart, String dateArrival, String flight, String pathid, String pathTicket) 
    
    {

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final Dialog ringProgressDialog = new Dialog(BookItemActivity.this, R.style.Theme_AppCompat_Dialog);
        ringProgressDialog.setContentView(R.layout.loading);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ringProgressDialog.show();
        GifImageView gifview = ringProgressDialog.findViewById(R.id.loaderGif);
        gifview.setGifImageResource(R.drawable.loader2);

        String userid = getData(BookItemActivity.this, "user_id", "");
        System.out.println("========== userid========== " + userid);

        params.put("user_id", userid);
        params.put("trevaller_name", traveller_name);
        params.put("trevaller_address", address);
        params.put("trevaller_email", email);
        params.put("trevaller_phone", phone);
        params.put("trevaller_from", pickup);
        params.put("trevaller_to", destination);
        params.put("trevaller_flight_no", flight);
        params.put("product_id", post_id);
        params.put("product_sender_id", sender_id);

        params.put("departure_date", dateDepart);
        params.put("arrival_date", dateArrival);

        File idfile = new File(pathid);
        try {
            System.out.println(idimageUri);
            System.out.println(idfile);
            params.put("trevaller_identity", idfile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
   File ticketFile = new File(pathTicket);
        try {
            System.out.println(idimageUri);
            System.out.println(idfile);
            params.put("trevaller_ticket", ticketFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        System.out.println("============= book item  api ==============");
        System.err.println(params);
        client.post(BASE_URL_NEW + "add_trevaller_booking", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response);
                ringProgressDialog.dismiss();
                try {

                    if (response.getString("status").equals("1")) {
                        makeToast(BookItemActivity.this, response.getString("message"));
                        BookItemActivity.this.onBackPressed();
                        BookItemActivity.this.finish();

                    } else {
                        makeToast(BookItemActivity.this, response.getString("message"));
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



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("=========== after clicking image ============");
        System.out.println(requestCode);
        System.out.println(resultCode);
        switch (requestCode) {


            case PLACE_PICKER_REQUEST:

                if (resultCode == Activity.RESULT_OK) {

                    Place place = PlacePicker.getPlace(data, BookItemActivity.this);
                    LatLng location = place.getLatLng();
                    p_lat = String.valueOf(location.latitude);
                    p_lng = String.valueOf(location.longitude);
                    String new_location = getAddressFromLatlng(location, BookItemActivity.this.getApplicationContext(), 0);
                    pickupET.setText("  " + new_location);


                }
                break;

            case PLACE_PICKER_REQUEST_DEST:

                if (resultCode == Activity.RESULT_OK) {
                    Place place = PlacePicker.getPlace(data, BookItemActivity.this);
                    LatLng location = place.getLatLng();
                    d_lat = String.valueOf(location.latitude);
                    d_lng = String.valueOf(location.longitude);
                    String new_location = getAddressFromLatlng(location, BookItemActivity.this.getApplicationContext(), 0);
                    destET.setText("  " + new_location);
                }
                break;

            case ID_REQUEST_IMAGE:
                if (idimageUri != null && idimageUri.getPath().length() > 0) {
                    Picasso.with(BookItemActivity.this).load(idimageUri).into(idIV);
                    idIV.setVisibility(View.VISIBLE);
                } else {
                    idIV.setVisibility(View.GONE);
                }
                break;

            case TICKET_REQUEST_IMAGE:
                if (ticketImageurI != null && ticketImageurI.getPath().length() > 0) {
                    Picasso.with(BookItemActivity.this).load(ticketImageurI).into(ticketIV);
                    ticketIV.setVisibility(View.VISIBLE);
                } else {
                    ticketIV.setVisibility(View.GONE);
                }
                break;

        }
    }
}
