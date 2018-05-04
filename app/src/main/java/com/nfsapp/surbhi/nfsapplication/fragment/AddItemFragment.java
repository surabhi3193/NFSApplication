package com.nfsapp.surbhi.nfsapplication.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.dropin.utils.PaymentMethodType;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.adapter.SliderAdapter;
import com.nfsapp.surbhi.nfsapplication.constants.Utility;
import com.nfsapp.surbhi.nfsapplication.other.GifImageView;
import com.nfsapp.surbhi.nfsapplication.other.MultiPhotoSelectActivity;
import com.nfsapp.surbhi.nfsapplication.other.NetworkClass;
import com.nfsapp.surbhi.nfsapplication.payment.ConfirmationActivity;
import com.nfsapp.surbhi.nfsapplication.payment.PayPalConfig;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;
import me.relex.circleindicator.CircleIndicator;

import static com.nfsapp.surbhi.nfsapplication.constants.GeocodingLocation.getAddressFromLatlng;
import static com.nfsapp.surbhi.nfsapplication.other.MySharedPref.getData;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.BASE_URL_NEW;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.getRealPathFromURI;

public class AddItemFragment extends Fragment {
    public static final int PAYPAL_REQUEST_CODE = 123;
    private static final int CAPTURE_IMAGES_FROM_CAMERA = 1;
    private static final int PLACE_PICKER_REQUEST = 3;
    private static final int PLACE_PICKER_REQUEST_DEST = 4;
    private static final int GALLERY_PICTURE = 5;
    private static final int BRAINTREE_REQUEST_CODE = 6;
    //Paypal Configuration Object
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);
    public RelativeLayout sliderLay;
    public LinearLayout formLay;
    ArrayList<Uri> imageArray = new ArrayList<>();
    String cost = "1", pweight, path;
    Dialog dialog;
    HashMap<String, String> paramHash;
    Button book_btn;
    private LinearLayout camera_lay;
    private int image_count_before;
    private ViewPager mPager;
    private CircleIndicator indicator;
    private Uri idimageUri;
    private ImageView idIV;
    private TextView dateTV;
    private String p_lat = "0.0", p_lng = "0.0";
    private String d_lat = "0.0", d_lng = "0.0";
    private AutoCompleteTextView pickupEt, destET;
    private String send_payment_details = "Payment_APi";
    private String get_token = "http://mindinfodemo.com/NFS/index.php/Webservice/get_token";
    //    private String token = "eyJ2ZXJzaW9uIjoyLCJhdXRob3JpemF0aW9uRmluZ2VycHJpbnQiOiJiZmMzNDg1MDA0ZjZhMzMyMDI0MTljODJhMGY0N2NiZDIwNThhMDc2MDM5YjdjMjM2OWU5YzhjOGQwNmYyOWY3fGNyZWF0ZWRfYXQ9MjAxOC0wNC0yOFQxMDo0NDoxNC4xMzQwNTY1MzQrMDAwMFx1MDAyNm1lcmNoYW50X2lkPTM0OHBrOWNnZjNiZ3l3MmJcdTAwMjZwdWJsaWNfa2V5PTJuMjQ3ZHY4OWJxOXZtcHIiLCJjb25maWdVcmwiOiJodHRwczovL2FwaS5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tOjQ0My9tZXJjaGFudHMvMzQ4cGs5Y2dmM2JneXcyYi9jbGllbnRfYXBpL3YxL2NvbmZpZ3VyYXRpb24iLCJjaGFsbGVuZ2VzIjpbXSwiZW52aXJvbm1lbnQiOiJzYW5kYm94IiwiY2xpZW50QXBpVXJsIjoiaHR0cHM6Ly9hcGkuc2FuZGJveC5icmFpbnRyZWVnYXRld2F5LmNvbTo0NDMvbWVyY2hhbnRzLzM0OHBrOWNnZjNiZ3l3MmIvY2xpZW50X2FwaSIsImFzc2V0c1VybCI6Imh0dHBzOi8vYXNzZXRzLmJyYWludHJlZWdhdGV3YXkuY29tIiwiYXV0aFVybCI6Imh0dHBzOi8vYXV0aC52ZW5tby5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tIiwiYW5hbHl0aWNzIjp7InVybCI6Imh0dHBzOi8vY2xpZW50LWFuYWx5dGljcy5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tLzM0OHBrOWNnZjNiZ3l3MmIifSwidGhyZWVEU2VjdXJlRW5hYmxlZCI6dHJ1ZSwicGF5cGFsRW5hYmxlZCI6dHJ1ZSwicGF5cGFsIjp7ImRpc3BsYXlOYW1lIjoiQWNtZSBXaWRnZXRzLCBMdGQuIChTYW5kYm94KSIsImNsaWVudElkIjpudWxsLCJwcml2YWN5VXJsIjoiaHR0cDovL2V4YW1wbGUuY29tL3BwIiwidXNlckFncmVlbWVudFVybCI6Imh0dHA6Ly9leGFtcGxlLmNvbS90b3MiLCJiYXNlVXJsIjoiaHR0cHM6Ly9hc3NldHMuYnJhaW50cmVlZ2F0ZXdheS5jb20iLCJhc3NldHNVcmwiOiJodHRwczovL2NoZWNrb3V0LnBheXBhbC5jb20iLCJkaXJlY3RCYXNlVXJsIjpudWxsLCJhbGxvd0h0dHAiOnRydWUsImVudmlyb25tZW50Tm9OZXR3b3JrIjp0cnVlLCJlbnZpcm9ubWVudCI6Im9mZmxpbmUiLCJ1bnZldHRlZE1lcmNoYW50IjpmYWxzZSwiYnJhaW50cmVlQ2xpZW50SWQiOiJtYXN0ZXJjbGllbnQzIiwiYmlsbGluZ0FncmVlbWVudHNFbmFibGVkIjp0cnVlLCJtZXJjaGFudEFjY291bnRJZCI6ImFjbWV3aWRnZXRzbHRkc2FuZGJveCIsImN1cnJlbmN5SXNvQ29kZSI6IlVTRCJ9LCJtZXJjaGFudElkIjoiMzQ4cGs5Y2dmM2JneXcyYiIsInZlbm1vIjoib2ZmIn0=";
    private String token, rec_mob_2, pname, pdesc, pickup, destination, date, payment, receiver, rec_mob_1, rec_mail, isinsure = "";

    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Utility.checkCameraPermission(getActivity());
        View v = inflater.inflate(R.layout.fragment_add_item, null);

        getToken();
        Intent intent = new Intent(getActivity(), PayPalService.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        Objects.requireNonNull(getActivity()).startService(intent);

        mPager = v.findViewById(R.id.pager);
        indicator = v.findViewById(R.id.indicator);
        book_btn = v.findViewById(R.id.book_btn);
        final EditText p_nameEt = v.findViewById(R.id.p_nameEt);
        final EditText p_descEt = v.findViewById(R.id.p_descEt);
        final EditText weightEt = v.findViewById(R.id.weightEt);
        final EditText p_costEt = v.findViewById(R.id.p_costEt);
//        pickupEt = v.findViewById(R.id.pickupEt);
        pickupEt = v.findViewById(R.id.pickupEt);
        destET = v.findViewById(R.id.destET);

        final TextView prohibitedTV = v.findViewById(R.id.prohibitedTV);
        final EditText rec_nameEt = v.findViewById(R.id.rec_nameEt);
        final EditText rec_mob1 = v.findViewById(R.id.rec_mob1);
        final EditText rec_mob2 = v.findViewById(R.id.rec_mob2);
        final EditText rec_emailEt = v.findViewById(R.id.rec_emailEt);
        final Spinner weightSpinner = v.findViewById(R.id.weightSpinner);
        final Spinner payment_spinner = v.findViewById(R.id.payment_spinner);

        idIV = v.findViewById(R.id.idIV);
        dateTV = v.findViewById(R.id.dateTVStock);

        sliderLay = v.findViewById(R.id.sliderLay);
        formLay = v.findViewById(R.id.formLay);

        TextView idTV = v.findViewById(R.id.uploadIDCardTV);
        final CheckBox costCB = v.findViewById(R.id.costCB);
        final CheckBox insureCB = v.findViewById(R.id.insureCB);
        final CheckBox termsCB = v.findViewById(R.id.termsCB);
        final CheckBox itemCB = v.findViewById(R.id.itemCB);

        String airportlist = getData(getActivity(), "country_list", "");
        String[] airports = airportlist.split("/");

        // Creating adapter for spinner
        List<String> weightlist = new ArrayList<>();
        weightlist.add("Kg");
        weightlist.add("Lbs");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, weightlist);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weightSpinner.setAdapter(dataAdapter);

        ArrayAdapter<String> airportadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, airports);
        pickupEt.setAdapter(airportadapter);
        destET.setAdapter(airportadapter);


        ArrayList<String> list = new ArrayList<>();
        list.add("Payment mode");
        list.add("Debit/Credit Card");
        list.add("Paypal");
        // country_list
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, list);
        payment_spinner.setAdapter(adapter);
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener datestock = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                NetworkClass.updateLabel(dateTV, myCalendar);
            }
        };
        dateTV.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                DatePickerDialog d = new DatePickerDialog(getActivity(), datestock, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                d.getDatePicker().setMinDate(System.currentTimeMillis());
                d.show();
            }
        });

        idTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean per = Utility.checkWriteStoragePermission(getActivity());
                if (per)
                    getIDImage();
            }
        });
        costCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (costCB.isChecked()) {
                    System.out.println("======= cost is checked========");
                    p_costEt.setEnabled(true);
                    p_costEt.setCursorVisible(true);
                    p_costEt.setFocusableInTouchMode(true);
                } else {
                    p_costEt.setFocusableInTouchMode(false);
                    p_costEt.setEnabled(false);
                    p_costEt.setCursorVisible(false);
                }
            }
        });
        book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rec_mob_2 = "";
                pname = p_nameEt.getText().toString();
                pdesc = p_descEt.getText().toString();
                pweight = weightEt.getText().toString();
                pickup = pickupEt.getText().toString();
                destination = destET.getText().toString();
                date = dateTV.getText().toString();
                receiver = rec_nameEt.getText().toString();
                rec_mob_1 = rec_mob1.getText().toString();
                rec_mob_2 = rec_mob2.getText().toString();
                rec_mail = rec_emailEt.getText().toString();

                if (costCB.isChecked())
                    cost = p_costEt.getText().toString();

                if (imageArray.size() == 0) {
                    makeToast(getActivity(), "Capture product image to post ");
                    return;
                }
                if (pname.length() == 0) {
                    makeToast(getActivity(), "Enter product name");
                    return;
                }

                if (pdesc.length() == 0) {
                    makeToast(getActivity(), "Enter product description");
                    return;
                }

                if (pweight.length() == 0) {
                    makeToast(getActivity(), "Enter product weight");
                    return;
                }

                if (cost.length() == 0) {
                    makeToast(getActivity(), "Enter product cost");
                    return;
                }

                if (pickup.length() == 0) {
                    makeToast(getActivity(), "Enter pickup location");
                    return;
                }

                if (destination.length() == 0) {
                    makeToast(getActivity(), "Enter destination location");
                    return;
                }

                if (date.length() == 0) {
                    makeToast(getActivity(), "Enter pickup date");
                    return;
                }

//                if (payment.length() == 0 || payment.equalsIgnoreCase("payment mode")) {
//                    makeToast(getActivity(), "Enter payment mode");
//                    return;
//                }

                if (receiver.length() == 0) {
                    makeToast(getActivity(), "Enter receiver name");
                    return;
                }
                if (rec_mob_1.length() == 0) {
                    makeToast(getActivity(), "Enter receiver mobile number");
                    return;
                }
                if (rec_mail.length() == 0) {
                    makeToast(getActivity(), "Enter receiver email id");
                    return;
                }

                if (!rec_mail.contains("@")) {
                    makeToast(getActivity(), "Invalid receiver's email id");
                    return;
                }

                if (idimageUri == null || idimageUri.getPath().length() == 0) {
                    makeToast(getActivity(), "Valid id card picture required");
                    return;

                }
                if (!termsCB.isChecked()) {
                    makeToast(getActivity(), "Accept terms & conditions to continue ");
                    return;

                }
                if (!itemCB.isChecked()) {
                    makeToast(getActivity(), "Please check prohibited items  ");
                    return;

                }


                String isProhibited = "";
                if (insureCB.isChecked())
                    isinsure = "yes";
                else
                    isinsure = "no";
//
                path = getRealPathFromURI(idimageUri, getActivity());
                pweight = pweight + " " + weightSpinner.getSelectedItem().toString();
                onBraintreeSubmit();

            }
        });

        camera_lay = v.findViewById(R.id.camera_lay);

        camera_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageDialog();
            }
        });
        prohibitedTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prohibitedList();
            }
        });
        return v;
    }

    private void getPayment() {
        //Getting the amount from editText

        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(cost)), "USD", "Nfs Fee",
                PayPalPayment.PAYMENT_INTENT_SALE);

        //Creating Paypal Payment activity intent
        Intent intent = new Intent(getActivity(), PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        //Puting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    private void imageDialog() {
        dialog = new Dialog(getActivity(), R.style.Theme_AppCompat_Dialog);
        dialog.setContentView(R.layout.upload_image);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        TextView gallery_btn = dialog.findViewById(R.id.gallery_btn);
        TextView cam_btn = dialog.findViewById(R.id.camera_btn);
        gallery_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean per = Utility.checkReadStoragePermission(getActivity());
                if (per) {
                    startActivityForResult(new Intent(getActivity().getBaseContext(),
                            MultiPhotoSelectActivity.class), GALLERY_PICTURE);

                }
                dialog.dismiss();
            }
        });

        cam_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean per = Utility.checkWriteStoragePermission(getActivity());
                if (per) {
                    Cursor cursor = loadCursor();
                    image_count_before = cursor.getCount();
                    cursor.close();
                    Intent cameraIntent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
                    List<ResolveInfo> activities = getActivity().getPackageManager().queryIntentActivities(cameraIntent, 0);

                    if (activities.size() > 0) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.click_photo), Toast.LENGTH_LONG).show();
                        startActivityForResult(cameraIntent, CAPTURE_IMAGES_FROM_CAMERA);
                    } else
                        Toast.makeText(getActivity(), getResources().getString(R.string.no_camera_app), Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }

    private void getIDImage() {
        String fileName = "Camera_Example.jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");

        idimageUri = getActivity().getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        System.out.println("========== image uri ========= ");
        if (idimageUri != null)
            System.out.println(idimageUri.getPath());

        Intent intent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, idimageUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, 2);
    }


    private void postItem(ArrayList<Uri> imageArray, String pname, String pdesc, String pweight, String cost,
                          String pickup, String destination, String date, String payment, String idimageUri, String receiver,
                          String rec_mob_1, String rec_mob_2, String recMail, String p_lat, String p_lng, String isinsure,
                          String payment_id) {

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final Dialog ringProgressDialog = new Dialog(getActivity(), R.style.Theme_AppCompat_Dialog);
        ringProgressDialog.setContentView(R.layout.loading);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ringProgressDialog.show();
        GifImageView gifview = ringProgressDialog.findViewById(R.id.loaderGif);
        final TextView progressper = ringProgressDialog.findViewById(R.id.progressper);
        gifview.setGifImageResource(R.drawable.loader2);

        String userid = getData(getActivity(), "user_id", "");
        System.out.println("========== userid========== " + userid);

        params.put("payment_id", payment_id);
        params.put("user_id", userid);
        params.put("product_name", pname);
        params.put("product_desc", pdesc);
        params.put("product_weight", pweight);
        params.put("product_cost", cost);
        params.put("pickup_location", pickup);
        params.put("payment_mode", payment);
        params.put("reciever_name", receiver);
        params.put("reciever_phone", rec_mob_1);
        params.put("reciever_email", recMail);
        params.put("product_insurence", isinsure);
        params.put("destination_location", destination);
        params.put("product_send_date", date);


        for (int i = 0; i < imageArray.size(); i++) {
            String path_product = (imageArray.get(i).getPath());
            System.out.println("======== Productimg  ========= ");
            System.out.println(path_product);
            File imageFile = new File(path_product);
            try {
                params.put("product_pic" + i, imageFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                ringProgressDialog.dismiss();
                makeToast(getActivity(),"Image not found , Try again");


            }
        }

        File idfile = new File(idimageUri);
        try {
            System.out.println(idimageUri);
            System.out.println(idfile);
            params.put("sender_identity", idfile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("============= post item  api ==============");
        System.err.println(params);
        client.setConnectTimeout(120 * 1000);
        client.post(BASE_URL_NEW + "add_post", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response);
                ringProgressDialog.dismiss();
                try {

                    if (response.getString("status").equals("1")) {
                        makeToast(getActivity(), response.getString("message"));
                        getActivity().onBackPressed();
                        getActivity().finish();

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

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                Log.e("progress", "pos: " + bytesWritten + " len: " + totalSize);
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
            case CAPTURE_IMAGES_FROM_CAMERA:
                exitingCamera();
                break;

            case 2:
                if (resultCode != 0) {
                    if (idimageUri != null && idimageUri.getPath().length() > 0) {
                        Picasso.with(getActivity()).load(idimageUri).into(idIV);
                        idIV.setVisibility(View.VISIBLE);
                    } else {
                        idIV.setVisibility(View.GONE);
                    }
                } else {
                    idimageUri = null;
                    idIV.setVisibility(View.GONE);
                }

                break;
            case PLACE_PICKER_REQUEST:

                if (resultCode == Activity.RESULT_OK) {

                    Place place = PlacePicker.getPlace(data, getActivity());
                    LatLng location = place.getLatLng();
                    p_lat = String.valueOf(location.latitude);
                    p_lng = String.valueOf(location.longitude);
                    String new_location = getAddressFromLatlng(location, getActivity().getApplicationContext(), 0);
                    pickupEt.setText(new_location);

                }
                break;

            case PLACE_PICKER_REQUEST_DEST:

                if (resultCode == Activity.RESULT_OK) {
                    Place place = PlacePicker.getPlace(data, getActivity());
                    LatLng location = place.getLatLng();
                    d_lat = String.valueOf(location.latitude);
                    d_lng = String.valueOf(location.longitude);
                    String new_location = getAddressFromLatlng(location, getActivity().getApplicationContext(), 0);
                    destET.setText(new_location);
                }
                break;

            case GALLERY_PICTURE:
                if (resultCode == 1) {

                    ArrayList<String> responseArray = new ArrayList<>();
                    imageArray = new ArrayList<>();
                    responseArray = data.getStringArrayListExtra("MESSAGE");
                    if (responseArray.size() > 4) {
                        Toast.makeText(getActivity().getApplicationContext(), "Maximum 4 pics allowed", Toast.LENGTH_LONG).show();
                    } else if (responseArray.size() > 0) {
                        camera_lay.setVisibility(View.GONE);
                        for (int i = 0; i < responseArray.size(); i++) {
                            Uri uri = Uri.fromFile(new File(responseArray.get(i)));

                            Log.e("Uri" + i, uri.toString());
                            imageArray.add(uri);
                        }

                        init(imageArray);
                    }
                }
                break;
            case PAYPAL_REQUEST_CODE:
                //If the result is OK i.e. user has not canceled the payment
                if (resultCode == Activity.RESULT_OK) {
                    //Getting the payment confirmation
                    PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                    //if confirmation is not null
                    if (confirm != null) {
                        try {
                            //Getting the payment details
                            String paymentDetails = confirm.toJSONObject().toString(4);
                            Log.i("paymentExample", paymentDetails);

                            //Starting a new activity for the payment details and also putting the payment details with intent
                            startActivity(new Intent(getActivity(), ConfirmationActivity.class)
                                    .putExtra("PaymentDetails", paymentDetails)
                                    .putExtra("PaymentAmount", cost));

                        } catch (JSONException e) {
                            Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                        }
                    }
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Log.i("paymentExample", "The user canceled.");
                } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                    Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
                }

            case BRAINTREE_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                    PaymentMethodNonce nonce = result.getPaymentMethodNonce();
                    PaymentMethodType method = result.getPaymentMethodType();
                    String type = method.getCanonicalName();
                    String stringNonce = nonce.getNonce();
                    Log.d("mylog", "Result: " + stringNonce);
                    Log.d("mylog", "Type: " + type);
                    // Send payment price with the nonce
                    // use the result to update your UI and send the payment method nonce to your server
                    if (!cost.equalsIgnoreCase("0")) {
                        System.err.println("======= naunce key ==========");
                        System.err.println(stringNonce);

                        payment = type;
                     sendPayment(cost, stringNonce,payment);
                    } else
                        Toast.makeText(getActivity(), "Please enter a valid amount.", Toast.LENGTH_SHORT).show();

                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(getActivity(), "Transaction canceled", Toast.LENGTH_SHORT).show();
                    Log.d("mylog", "user canceled");
                } else {
                    // handle errors here, an exception may be available in
                    Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                    Log.d("mylog", "Error : " + error.toString());
                    Toast.makeText(getActivity(), "Under development", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void onBraintreeSubmit() {
        DropInRequest dropInRequest = new DropInRequest()
                .clientToken(token);
        dropInRequest.collectDeviceData(true);
        startActivityForResult(dropInRequest.getIntent(getActivity()), BRAINTREE_REQUEST_CODE);
    }

    public void sendPayment(final String cost, String stringNonce,final  String payment_type) {

        final Dialog ringProgressDialog = new Dialog(getActivity(), R.style.Theme_AppCompat_Dialog);
        ringProgressDialog.setContentView(R.layout.loading);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ringProgressDialog.show();
        GifImageView gifview = ringProgressDialog.findViewById(R.id.loaderGif);
        final TextView progressper = ringProgressDialog.findViewById(R.id.progressper);
        gifview.setGifImageResource(R.drawable.loader2);

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();
        client.setTimeout(60 * 1000);
        client.setConnectTimeout(60 * 1000);
        client.setResponseTimeout(60 * 1000);

        String userid = getData(getActivity(), "user_id", "");
        params.put("amount", cost);
        params.put("user_id", userid);
        params.put("nonce_key", stringNonce);
        params.put("payment_type", payment_type);

        client.post(BASE_URL_NEW + "payment", params, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response);
                try {
                    ringProgressDialog.dismiss();
//                    Toast.makeText(getActivity(), "Transaction successful", Toast.LENGTH_LONG).show();
                    String payment_id = response.getString("payment_id");
                    proceedItem(payment_id, true,payment_type);

                } catch (Exception e) {
                    e.printStackTrace();
                    ringProgressDialog.dismiss();
                }

            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                ringProgressDialog.dismiss();
                Toast.makeText(getActivity(), "Transaction Fail", Toast.LENGTH_LONG).show();
                System.out.println("**** payment api ****fail***** ");
                System.out.println(errorResponse);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getActivity(), "Transaction Fail", Toast.LENGTH_LONG).show();
                ringProgressDialog.dismiss();
                System.out.println(responseString);

            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                ringProgressDialog.show();
            }
        });

    }
    private void proceedItem(final String payment_id, final boolean paid,final String payment_type) {
        final Dialog dialog = new Dialog(getActivity(), R.style.Theme_AppCompat_Dialog);
        dialog.setContentView(R.layout.transection_successfull);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        TextView ok_btn = dialog.findViewById(R.id.gallery_btn);

        GifImageView gifview = dialog.findViewById(R.id.loaderGif);
        gifview.setGifImageResource(R.drawable.success);

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                postItem(imageArray, pname, pdesc, pweight, "$"+cost, pickup, destination, date, payment_type, path, receiver, rec_mob_1,
                        rec_mob_2, rec_mail, p_lat, p_lng, isinsure, payment_id);
            }
        });
    }


    public void getToken() {

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();
        client.setTimeout(60 * 1000);
        client.setConnectTimeout(60 * 1000);
        client.setResponseTimeout(60 * 1000);
        client.post(BASE_URL_NEW + "get_token", params, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response);
                try {
                    token = response.getString("token");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                System.out.println("**** payment api ****fail***** ");
                System.out.println(errorResponse);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("**** payment get token  api ****fail***** ");
                System.out.println(responseString);

            }

        });

    }

    public Cursor loadCursor() {

        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};

        final String orderBy = MediaStore.Images.Media.DATE_ADDED;

        return getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                columns, null, null, orderBy);
    }

    private void exitingCamera() {

        Cursor cursor = loadCursor();
        String[] paths = getImagePaths(cursor, image_count_before);

        if (paths != null && paths.length <= 4) {
            List<String> wordList = Arrays.asList(paths);
            camera_lay.setVisibility(View.GONE);
            process(wordList);
            cursor.close();
            return;
        } else {
            camera_lay.setVisibility(View.VISIBLE);
            if (paths != null)
                makeToast(getActivity(), "Maximum 4 pictures are allowed");
        }

        cursor.close();

    }

    public String[] getImagePaths(Cursor cursor, int startPosition) {

        int size = cursor.getCount() - startPosition;

        if (size <= 0) return null;

        String[] paths = new String[size];

        int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);

        for (int i = startPosition; i < cursor.getCount(); i++) {

            cursor.moveToPosition(i);

            paths[i - startPosition] = cursor.getString(dataColumnIndex);
        }
        return paths;
    }

    private void process(List<String> wordList) {


        List<String> responseArray = new ArrayList<>();

        responseArray = wordList;
        System.out.println("========== image uri ========= ");

        for (int i = 0; i < responseArray.size(); i++) {
            Uri tempUri = Uri.fromFile(new File(responseArray.get(i)));
            imageArray.add(tempUri);
            System.out.println(tempUri);
        }
        init(imageArray);
    }

    private void init(final ArrayList<Uri> imageArray) {
        final int[] currentPage = {0};

        mPager.setAdapter(new SliderAdapter(getActivity(), imageArray));

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

    public void onBackPressed() {
        if (dialog != null)
            dialog.dismiss();

        else
            getActivity().onBackPressed();

    }

    private void prohibitedList() {
        final Dialog dialog = new Dialog(getActivity(), R.style.Theme_AppCompat_Dialog);
        dialog.setContentView(R.layout.prohibited_items);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        TextView ok_btn = dialog.findViewById(R.id.ok_btn);
        WebView webView = dialog.findViewById(R.id.webView);
        String url = "file:///android_asset/nfs.html";
        webView.loadUrl(url);
        webView.requestFocus();


        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                try {
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
    }


    @Override
    public void onDestroy() {
        Objects.requireNonNull(getActivity()).stopService(new Intent(getActivity(), PayPalService.class));
        super.onDestroy();
    }
}