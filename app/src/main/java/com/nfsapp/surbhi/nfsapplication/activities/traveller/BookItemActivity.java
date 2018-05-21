package com.nfsapp.surbhi.nfsapplication.activities.traveller;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.activities.EditProfileActivity;
import com.nfsapp.surbhi.nfsapplication.beans.User;
import com.nfsapp.surbhi.nfsapplication.constants.Utility;
import com.nfsapp.surbhi.nfsapplication.other.GifImageView;
import com.nfsapp.surbhi.nfsapplication.other.NetworkClass;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

import static com.nfsapp.surbhi.nfsapplication.other.MySharedPref.getData;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.BASE_IMAGE_URL;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.BASE_URL_NEW;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.getRealPathFromURI;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.makeToast;

public class BookItemActivity extends AppCompatActivity {

    private static final int ID_REQUEST_IMAGE = 3;
    private static final int TICKET_REQUEST_IMAGE = 4;
    TextView destET, pickupET;
    String name = "", address = "", phone = "", email = "", street = "", postal = "";
    String per;
    private TextView dateTVdeparture, dateTVarrival;
    private EditText nameET, accountnoET, routingET, swiftET, paypalidET;
    private Uri idimageUri;
    private Uri ticketImageurI;
    private ImageView idIV, ticketIV;
    private String sender_id = "", post_id = "";
    private boolean cam_open = false;
    private Spinner payment_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_item);

        per = getData(BookItemActivity.this, "profile_percent", "0");

        Utility.checkCameraPermission(BookItemActivity.this);
        ImageView back_btn = findViewById(R.id.back_btn);
        TextView header_text = findViewById(R.id.header_text);
        TextView idTV = findViewById(R.id.uploadIDCardTV);
        TextView ticketTV = findViewById(R.id.uploadTicketTV);
        dateTVdeparture = findViewById(R.id.dateTVdepature);
        dateTVarrival = findViewById(R.id.dateTVarrival);
        nameET = findViewById(R.id.nameET);

        pickupET = findViewById(R.id.pickupET);
        destET = findViewById(R.id.destET);
        idIV = findViewById(R.id.idIV);
        ticketIV = findViewById(R.id.ticketIV);

        accountnoET = findViewById(R.id.account_noEt);
        routingET = findViewById(R.id.routingEt);
        swiftET = findViewById(R.id.swiftEt);
        paypalidET = findViewById(R.id.paypalidEt);

        header_text.setText("Traveller Detail");
        payment_spinner = findViewById(R.id.payment_spinner);

//        if (!per.equalsIgnoreCase("100")) {
//            nameET.setEnabled(false);
//            dateTVarrival.setEnabled(false);
//            dateTVdeparture.setEnabled(false);
//            payment_spinner.setEnabled(false);
//            destET.setEnabled(false);
//            idTV.setEnabled(false);
//            ticketTV.setEnabled(false);
//        }
//        else
//            {
//            nameET.setEnabled(true);
//            dateTVarrival.setEnabled(true);
//            dateTVdeparture.setEnabled(true);
////            airlineEt.setEnabled(true);
////            flightEt.setEnabled(true);
//            payment_spinner.setEnabled(true);
//            destET.setEnabled(true);
//            idTV.setEnabled(true);
//                ticketTV.setEnabled(true);
//        }

        ArrayList<String> list = new ArrayList<>();
        list.add("Payment mode");
        list.add("Bank Account");
        list.add("Paypal");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(BookItemActivity.this, R.layout.spinner_item, list);
        payment_spinner.setAdapter(adapter);

        String airportlist = getData(BookItemActivity.this, "airline_list", "");
        String[] airlines = airportlist.split("/");
        ArrayAdapter<String> airportadapter = new ArrayAdapter<String>(BookItemActivity.this,
                R.layout.drop_down, airlines);
//        airlineEt.setAdapter(airportadapter);
        final User user = User.getInstance();
        name = user.getfirstName();
        email = user.getEmail();
        phone = user.getPhone();
        if (name != null)
            nameET.setText(name);
        if (user.getId_image() != null && user.getId_image().equalsIgnoreCase(BASE_IMAGE_URL))
        {
            idIV.setVisibility(View.VISIBLE);
            Picasso.with(getApplicationContext()).load(user.getId_image()).into(idIV);
        }
        payment_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getSelectedItem().toString();

                if (selectedItem.equalsIgnoreCase("Bank Account")) {
                    accountnoET.setVisibility(View.VISIBLE);
                    routingET.setVisibility(View.VISIBLE);
                    swiftET.setVisibility(View.VISIBLE);
                    paypalidET.setVisibility(View.GONE);
                } else if (selectedItem.equalsIgnoreCase("Paypal")) {
                    accountnoET.setVisibility(View.GONE);
                    routingET.setVisibility(View.GONE);
                    swiftET.setVisibility(View.GONE);
                    paypalidET.setVisibility(View.VISIBLE);
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            sender_id = bundle.getString("sender_id");
            post_id = bundle.getString("post_id");
            String pickup = bundle.getString("pickup");
            String destination = bundle.getString("destination");
            pickupET.setText(pickup);
            destET.setText(destination);

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


        idTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cam_open = true;
                getIDImage("id");
            }
        });


        ticketTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cam_open = true;

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

    private void warningBooking(final String traveller_name, final String pickup, final String destination, final String dateDepart,
                                final String dateArrival,final String pathTicket, final String payment_mode,
                                final String accountno, final String routingno, final String swiftcode, final String paypalid) {
        final Dialog dialog = new Dialog(BookItemActivity.this, R.style.Theme_AppCompat_Dialog);
        dialog.setContentView(R.layout.alertdialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        TextView ok_btn = dialog.findViewById(R.id.gallery_btn);
        TextView cancel_btn = dialog.findViewById(R.id.camera_btn);

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                BookItem(traveller_name, pickup, destination, dateDepart, dateArrival, pathTicket,payment_mode, accountno, routingno, swiftcode, paypalid);

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
        String accountno = "", routingno = "", swiftcode = "", paypalid = "", payment_type = "";
        String traveller_name = nameET.getText().toString();
        String payment_mode = payment_spinner.getSelectedItem().toString();


        String pickup = pickupET.getText().toString();
        String destination = destET.getText().toString();
        String dateArrival = dateTVarrival.getText().toString();
        String dateDepart = dateTVdeparture.getText().toString();
//        String flight = flightEt.getText().toString();
//        String airline = airlineEt.getText().toString();

        if (!per.equalsIgnoreCase("100")) {

                makeToast(BookItemActivity.this, "Profile Incomplete , Go to profile and complete your profile");
                startActivity(new Intent(BookItemActivity.this, EditProfileActivity.class));
                return;
        }

        if (traveller_name.length() == 0) {
            makeToast(BookItemActivity.this, "Enter your name");
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

        if (ticketImageurI == null || ticketImageurI.getPath().length() == 0) {
            makeToast(BookItemActivity.this, "Valid travelling ticket picture required");
            return;

        }
//        if (airline.length() == 0) {
//            makeToast(BookItemActivity.this, "Enter Airline name");
//            return;
//        }
//        if (flight.length() == 0) {
//            makeToast(BookItemActivity.this, "Enter flight number");
//            return;
//        }
        if (payment_mode.equalsIgnoreCase("Payment mode")) {
            makeToast(BookItemActivity.this, "Select payment mode to receive funds");
            return;
        }
        if (payment_mode.equalsIgnoreCase("Bank Account"))
        {
            accountno = accountnoET.getText().toString();
            routingno = routingET.getText().toString();
            swiftcode = swiftET.getText().toString();
            payment_type = "1";

            if (accountno.length()==0)
            {
                makeToast(BookItemActivity.this, "Enter your bank account number ");
                return;
            }
            if (routingno.length()==0)
            {
                makeToast(BookItemActivity.this, "Enter your routing number");
                return;
            }
            if (swiftcode.length()==0)
            {
                makeToast(BookItemActivity.this, "Enter SWIFT code");
                return;
            }

        }
        if (payment_mode.equalsIgnoreCase("Paypal")) {
            paypalid = paypalidET.getText().toString();
            payment_type = "2";

            if (paypalid.length()==0)
            {
                makeToast(BookItemActivity.this, "Enter your paypal id");
                return;
            }
        }

        String pathTicket = getRealPathFromURI(ticketImageurI, BookItemActivity.this);
            warningBooking(traveller_name, pickup, destination, dateDepart, dateArrival, pathTicket, payment_type,
                    accountno, routingno, swiftcode, paypalid);
    }

    private void BookItem(String traveller_name, String pickup, String destination,
                          String dateDepart, String dateArrival, String pathTicket,
                          String payment_mode, String accountno, String routingno, String swiftcode, String paypalid)
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
        final TextView progressper = ringProgressDialog.findViewById(R.id.progressper);

        String userid = getData(BookItemActivity.this, "user_id", "");

        params.put("user_id", userid);
        params.put("trevaller_name", traveller_name);
        params.put("trevaller_address", address);
        params.put("trevaller_street", street);
        params.put("trevaller_postalcode", postal);
        params.put("trevaller_email", email);
        params.put("trevaller_phone", phone);
        params.put("trevaller_from", pickup);
        params.put("trevaller_to", destination);
        params.put("product_id", post_id);
        params.put("product_sender_id", sender_id);
        params.put("departure_date", dateDepart);
        params.put("arrival_date", dateArrival);
        params.put("payment_type", payment_mode);
        params.put("bank_account_no", accountno);
        params.put("routing_number", routingno);
        params.put("swift_code", swiftcode);
        params.put("paypal_id", paypalid);

       /* File idfile = new File(pathid);
        try {
            System.out.println(idimageUri);
            System.out.println(idfile);
            params.put("trevaller_identity", idfile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
        File ticketFile = new File(pathTicket);
        try {
            System.out.println(idimageUri);
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
//                        onBackPressed();
                        Intent intent = new Intent();
                        intent.putExtra("isbooked", true);
                        setResult(RESULT_OK, intent);
                        finish();


                    } else {
                        makeToast(BookItemActivity.this, response.getString("message"));

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

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
//                Log.e("progress", "pos: " + bytesWritten + " len: " + totalSize);
                int per = 0;
                per = (int) ((bytesWritten * 100) / totalSize);
                progressper.setText(per - 2 + "%");
                if (per == 100)
                    progressper.setText("Done");
                // Progress 379443 from 2720368 (14%)
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("=========== after clicking image ============");
        System.out.println(requestCode);
        System.out.println(resultCode);
        switch (requestCode) {

            case ID_REQUEST_IMAGE:
                if (resultCode != 0) {
                    if (idimageUri != null && idimageUri.getPath().length() > 0) {
                        Picasso.with(BookItemActivity.this).load(idimageUri).into(idIV);
                        idIV.setVisibility(View.VISIBLE);
                    } else {
                        idIV.setVisibility(View.GONE);
                    }
                } else {
                    idimageUri = null;
                    idIV.setVisibility(View.GONE);
                }
                break;

            case TICKET_REQUEST_IMAGE:
                if (resultCode != 0) {
                    if (ticketImageurI != null && ticketImageurI.getPath().length() > 0) {
                        Picasso.with(BookItemActivity.this).load(ticketImageurI).into(ticketIV);
                        ticketIV.setVisibility(View.VISIBLE);
                    } else {
                        ticketIV.setVisibility(View.GONE);
                    }
                } else {
                    ticketImageurI = null;
                    ticketIV.setVisibility(View.GONE);
                }
                break;

        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        per = getData(BookItemActivity.this, "profile_percent", "0");

    }

}
