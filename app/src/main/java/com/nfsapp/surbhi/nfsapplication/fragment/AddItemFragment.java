package com.nfsapp.surbhi.nfsapplication.fragment;

import android.app.Activity;
import android.app.Dialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import me.relex.circleindicator.CircleIndicator;

import static com.nfsapp.surbhi.nfsapplication.constants.GeocodingLocation.getAddressFromLatlng;
import static com.nfsapp.surbhi.nfsapplication.other.MySharedPref.getData;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.BASE_URL_NEW;

public class AddItemFragment extends Fragment {
    private static final int CAPTURE_IMAGES_FROM_CAMERA = 1;
    private static final int PLACE_PICKER_REQUEST = 3;
    private static final int PLACE_PICKER_REQUEST_DEST = 4;
    ArrayList<Uri> imageArray = new ArrayList<>();
    private LinearLayout camera_lay;
    private int image_count_before;
    private ViewPager mPager;
    private CircleIndicator indicator;
    private Uri idimageUri;
    private ImageView idIV;
    private TextView pickupEt, destET;
    private String p_lat = "0.0", p_lng = "0.0";
    private String d_lat = "0.0", d_lng = "0.0";

    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Utility.checkCameraPermission(getActivity());

        View v = inflater.inflate(R.layout.fragment_add_item, null);
        mPager = v.findViewById(R.id.pager);
        indicator = v.findViewById(R.id.indicator);
        Button book_btn = v.findViewById(R.id.book_btn);

        final EditText p_nameEt = v.findViewById(R.id.p_nameEt);
        final EditText p_descEt = v.findViewById(R.id.p_descEt);
        final EditText weightEt = v.findViewById(R.id.weightEt);
        final EditText p_costEt = v.findViewById(R.id.p_costEt);
        pickupEt = v.findViewById(R.id.pickupEt);
        destET = v.findViewById(R.id.destET);
        final EditText paymentEt = v.findViewById(R.id.paymentEt);
        final EditText rec_nameEt = v.findViewById(R.id.rec_nameEt);
        final EditText rec_mob1 = v.findViewById(R.id.rec_mob1);
        final EditText rec_mob2 = v.findViewById(R.id.rec_mob2);
        final EditText rec_emailEt = v.findViewById(R.id.rec_emailEt);
        final Spinner weightSpinner = v.findViewById(R.id.weightSpinner);
        idIV = v.findViewById(R.id.idIV);

        TextView idTV = v.findViewById(R.id.uploadIDCardTV);
        final CheckBox costCB = v.findViewById(R.id.costCB);
        final CheckBox insureCB = v.findViewById(R.id.insureCB);
        final CheckBox termsCB = v.findViewById(R.id.termsCB);
        CheckBox itemCB = v.findViewById(R.id.itemCB);


        pickupEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("====== pickup clicked======");
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
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
                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST_DEST);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        idTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                }

                else {
                    p_costEt.setFocusableInTouchMode(false);
                    p_costEt.setEnabled(false);
                    p_costEt.setCursorVisible(false);
                }
            }
        });
        book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rec_mob_2 = "";
                String pname = p_nameEt.getText().toString();
                String pdesc = p_descEt.getText().toString();
                String pweight = weightEt.getText().toString();
                String pickup = pickupEt.getText().toString();
                String destination = destET.getText().toString();
                String payment = paymentEt.getText().toString();
                String receiver = rec_nameEt.getText().toString();
                String rec_mob_1 = rec_mob1.getText().toString();
                rec_mob_2 = rec_mob2.getText().toString();
                String rec_mail = rec_emailEt.getText().toString();
                String cost = "200";

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
                    makeToast(getActivity(), "Enter destination nlocation");
                    return;
                }

                if (payment.length() == 0) {
                    makeToast(getActivity(), "Enter payment mode");
                    return;
                }

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

                if (idimageUri==null || idimageUri.getPath().length() == 0) {
                    makeToast(getActivity(), "Valid id card picture required");
                    return;

                }
                if (!termsCB.isChecked()) {
                    makeToast(getActivity(), "Accept terms & conditions to continue ");
                    return;

                }

                String isinsure = "";
                if (insureCB.isChecked())
                    isinsure = "yes";
                else
                    isinsure = "no";


                String path =getRealPathFromURI(idimageUri);
                String path_product =(imageArray.get(0).getPath());
                pweight = pweight + " " + weightSpinner.getSelectedItem().toString();
                postItem(path_product, pname, pdesc, pweight, cost, pickup, destination, payment,path, receiver, rec_mob_1,
                        rec_mob_2, rec_mail, p_lat, p_lng, isinsure);
            }
        });

        camera_lay = v.findViewById(R.id.camera_lay);

        camera_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean per = Utility.checkWriteStoragePermission(getActivity());
                if (per) {
                    Cursor cursor = loadCursor();

                    //current images in mediaStore
                    image_count_before = cursor.getCount();

                    cursor.close();

                    Intent cameraIntent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);

                    List<ResolveInfo> activities = getActivity().getPackageManager().queryIntentActivities(cameraIntent, 0);

                    if (activities.size() > 0)
                        startActivityForResult(cameraIntent, CAPTURE_IMAGES_FROM_CAMERA);
                    else
                        Toast.makeText(getActivity(), getResources().getString(R.string.no_camera_app), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
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


    public  String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void postItem(String imageArray, String pname, String pdesc, String pweight, String cost,
                          String pickup, String destination, String payment, String idimageUri, String receiver,
                          String rec_mob_1, String rec_mob_2, String recMail, String p_lat, String p_lng, String isinsure) {

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final Dialog ringProgressDialog = new Dialog(getActivity(), R.style.Theme_AppCompat_Dialog);
        ringProgressDialog.setContentView(R.layout.loading);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ringProgressDialog.show();
        GifImageView gifview = ringProgressDialog.findViewById(R.id.loaderGif);
        gifview.setGifImageResource(R.drawable.loader2);

        String userid = getData(getActivity(), "user_id", "");
        System.out.println("========== userid========== " + userid);

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
        params.put("pickup_latitude", p_lat);
        params.put("pickup_logitude", p_lng);
        params.put("destination_location", destination);

        File imageFile = new File(imageArray);

        try {
            params.put("product_pic", imageFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
                if (idimageUri != null && idimageUri.getPath().length() > 0) {
                    Picasso.with(getActivity()).load(idimageUri).into(idIV);
                    idIV.setVisibility(View.VISIBLE);
                } else {
                    idIV.setVisibility(View.GONE);
                }
                break;
            case PLACE_PICKER_REQUEST:

                if (resultCode== Activity.RESULT_OK) {

                    Place place = PlacePicker.getPlace(data, getActivity());
                    LatLng location = place.getLatLng();
                    p_lat = String.valueOf(location.latitude);
                    p_lng = String.valueOf(location.longitude);
                    String new_location = getAddressFromLatlng(location, getActivity().getApplicationContext(), 0);
                    pickupEt.setText("  " + new_location);
                }
                break;

            case PLACE_PICKER_REQUEST_DEST:

                if (resultCode == Activity.RESULT_OK) {
                    Place place = PlacePicker.getPlace(data, getActivity());
                    LatLng location = place.getLatLng();
                    d_lat = String.valueOf(location.latitude);
                    d_lng = String.valueOf(location.longitude);
                    String new_location = getAddressFromLatlng(location, getActivity().getApplicationContext(), 0);
                    destET.setText("  " + new_location);
                }
                break;

        }
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

        if (paths != null && paths.length > 0) {
            List<String> wordList = Arrays.asList(paths);
            camera_lay.setVisibility(View.GONE);
            process(wordList);
        } else {
            camera_lay.setVisibility(View.VISIBLE);

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