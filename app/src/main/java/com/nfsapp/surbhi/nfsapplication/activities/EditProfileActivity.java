package com.nfsapp.surbhi.nfsapplication.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.beans.User;
import com.nfsapp.surbhi.nfsapplication.constants.Utility;
import com.nfsapp.surbhi.nfsapplication.other.GifImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;

import static com.nfsapp.surbhi.nfsapplication.constants.CheckInternetConnection.isNetworkAvailable;
import static com.nfsapp.surbhi.nfsapplication.constants.GeocodingLocation.getAddressFromLatlng;
import static com.nfsapp.surbhi.nfsapplication.other.MySharedPref.getData;
import static com.nfsapp.surbhi.nfsapplication.other.MySharedPref.saveData;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.BASE_ID_IMAGE_URL;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.BASE_IMAGE_URL;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.BASE_URL_NEW;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.getRealPathFromURI;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.makeToast;

public class EditProfileActivity extends AppCompatActivity {

    private EditText cityEt,streetEt,postalEt,firstnameEt,middleNameEt,lastNameET,streetET2,stateEt;
    ImageView idIV_main, idIV_update, cancel_btn,edit_img;
    private Uri idimageUri;
    private static final int ID_REQUEST_IMAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        final User user = User.getInstance();

      TextView  header_text =findViewById(R.id.header_text);
        ImageView back_btn = findViewById(R.id.back_btn);

        firstnameEt =findViewById(R.id.fullnameET);
       middleNameEt =findViewById(R.id.middlenameEt);
       lastNameET =findViewById(R.id.lastNameEt);
        cityEt =findViewById(R.id.cityEt);
        streetEt =findViewById(R.id.streetET);
        streetET2 =findViewById(R.id.streetET2);
        stateEt =findViewById(R.id.stateEt);
        postalEt =findViewById(R.id.postalET);
        final EditText emailEt =findViewById(R.id.EmailEt);
        final TextView uploadTV =findViewById(R.id.updateIDTV);
        idIV_update =findViewById(R.id.idIV);
       final CheckBox termsCB =findViewById(R.id.termsCB);

        Button update_button = (Button)findViewById(R.id.update_button);
        header_text.setText("Edit Profile");
        firstnameEt.setText(user.getfirstName());
        middleNameEt.setText(user.getMiddleName());
        lastNameET.setText(user.getLastName());
        emailEt.setText(user.getEmail());
        streetEt.setText(user.getStreet());
        streetET2.setText(user.getStreet2());
        cityEt.setText(user.getCity());
        stateEt.setText(user.getState());
        postalEt.setText(user.getZipcode());


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
        if (user.getId_image() != null && !user.getId_image().equals(BASE_ID_IMAGE_URL)) {
            uploadTV.setText("Update id Card");
            uploadTV.setTextColor(getResources().getColor(R.color.light_red));
            idIV_update.setVisibility(View.VISIBLE);
            idimageUri= Uri.parse(user.getId_image());
            Picasso.with(EditProfileActivity.this).load(user.getId_image()).noPlaceholder().into(idIV_update);
        } else {
            uploadTV.setText("Upload valid id Card");
            idIV_update.setVisibility(View.GONE);
        }

        uploadTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getIDImage();
            }
        });
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String middleName ="",street2="";
                String name = firstnameEt.getText().toString();
                 middleName = middleNameEt.getText().toString();
                String lastName = lastNameET.getText().toString();
                String city = cityEt.getText().toString();
                String street = streetEt.getText().toString();
                 street2 = streetET2.getText().toString();
                String state = stateEt.getText().toString();
                String postal = postalEt.getText().toString();
                String email = emailEt.getText().toString();

                if (isNetworkAvailable(EditProfileActivity.this))
                {
                    String pathid = "";

                    if (name.length()==0)
                    {
                        makeToast(EditProfileActivity.this,"Enter your first name");
                        firstnameEt.setError("Field required");
                        return;
                    }

                    if (lastName.length()==0)
                    {
                        makeToast(EditProfileActivity.this,"Enter your last name");
                        lastNameET.setError("Field required");
                        return;
                    }

                    if (email.length()==0)
                    {
                        makeToast(EditProfileActivity.this,"Enter your email");
                        emailEt.setError("Field required");
                        return;
                    }

                    if (street.length()==0)
                    {
                        makeToast(EditProfileActivity.this,"Enter your street address");
                        streetEt.setError("Field required");
                        return;
                    }

                    if (city.length()==0)
                    {
                        makeToast(EditProfileActivity.this,"Enter your city ");
                        cityEt.setError("Field required");
                        return;
                    }

                    if (state.length()==0)
                    {
                        makeToast(EditProfileActivity.this,"Enter your state");
                        stateEt.setError("Field required");
                        return;
                    }

                    if (postal.length()==0)
                    {
                        makeToast(EditProfileActivity.this,"Enter your postal/zip code");
                        postalEt.setError("Field required");
                        return;
                    }
                    if (idimageUri ==null || idimageUri.getPath().length()==0)
                    {
                        makeToast(EditProfileActivity.this,"Upload your valid id proof");
                        return;
                    }
                    if (!termsCB.isChecked())
                    {
                        makeToast(EditProfileActivity.this,"Accept NFS terms & conditions");
                        return;
                    }

                    if (user.getId_image().equalsIgnoreCase(BASE_ID_IMAGE_URL))
                    pathid = getRealPathFromURI(idimageUri, EditProfileActivity.this);

                    PostUserUpdatedDetail(user.getId(), name,middleName,lastName, city,street,street2,state,postal, email, pathid);
                } else {
                    makeToast(EditProfileActivity.this, "Connection Unavailable, Try again !");
                }

            }
        });
        
    }
    private void PostUserUpdatedDetail(String id, String name,String middle_name,String last_name, String city,String street,
                                       String user_street2,
                                       String user_state,String postal, String email,String pathid) {

        final Dialog ringProgressDialog = new Dialog(EditProfileActivity.this, R.style.Theme_AppCompat_Dialog);
        ringProgressDialog.setContentView(R.layout.loading);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ringProgressDialog.show();
        GifImageView gifview = ringProgressDialog.findViewById(R.id.loaderGif);
        final TextView progressper = ringProgressDialog.findViewById(R.id.progressper);
        gifview.setGifImageResource(R.drawable.loader2);

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();
        String userid = getData(EditProfileActivity.this, "user_id", "");

        params.put("user_id", userid);
        params.put("first_name", name);
        params.put("middle_name", middle_name);
        params.put("last_name", last_name);
        params.put("user_email", email);
        params.put("user_city", city);
        params.put("user_street", street);
        params.put("user_street2", user_street2);
        params.put("user_postalcode", postal);
        params.put("user_state", user_state);

        System.out.println("path id ========" + pathid);
        if (pathid != null && pathid.length() > 0) {
            File idfile = new File(pathid);
            try {
                System.out.println(idimageUri);
                System.out.println(idfile);
                params.put("valid_identity", idfile);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        System.out.println(params);

        client.post(BASE_URL_NEW + "update_profile", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    ringProgressDialog.dismiss();
                    System.out.println("response**********");
                    System.out.println(response);
                    if (response.getString("status").equals("0")) {
                        Toast.makeText(EditProfileActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                    } else {
                        JSONObject jsonObject = response.getJSONObject("result");
                        String street =jsonObject.getString("user_street");
                        String street2 =jsonObject.getString("user_street2");
                        String city =jsonObject.getString("user_city").trim();
                        String user_state =jsonObject.getString("user_state");
                        String postal =jsonObject.getString("user_postalcode").trim();
                        String per = jsonObject.getString("profile_status");

                        final User user = User.getInstance();

                        user.setId(jsonObject.getString("user_id"));
                        user.setProfile_pic(jsonObject.getString("user_pic"));
                        user.setfirstName(jsonObject.getString("first_name"));
                        user.setMiddleName(jsonObject.getString("middle_name"));
                        user.setLastName(jsonObject.getString("last_name"));
                        user.setStreet(street);
                        user.setStreet2(street2);
                        user.setCity(city);
                        user.setState(user_state);
                        user.setZipcode(postal);

                        user.setProfile_percent(per);
                        user.setEmail(jsonObject.getString("user_email"));
                        user.setPhone(jsonObject.getString("user_phone_no"));
                        user.setAccount_no(jsonObject.getString("user_deposit_ac_no"));
                        user.setId_image(jsonObject.getString("valid_identity"));

                        saveData(EditProfileActivity.this,"profile_percent",per);
                        onBackPressed();

                    }
                } catch (Exception e) {
                    ringProgressDialog.dismiss();

                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(EditProfileActivity.this, "Server Error,Try Again ", Toast.LENGTH_LONG).show();
                ringProgressDialog.dismiss();

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
                else if (per>100)
                    progressper.setVisibility(View.GONE);
                // Progress 379443 from 2720368 (14%)
            }
        });
    }

    private void getIDImage() {
        boolean per = Utility.checkWriteStoragePermission(EditProfileActivity.this);
        if (per) {
            String fileName = "Camera_Example.jpg";
            Uri imageUri;
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, fileName);
            values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");


            imageUri = EditProfileActivity.this.getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            System.out.println("========== image uri ========= ");
            if (imageUri != null)
                System.out.println(imageUri.getPath());
            idimageUri = imageUri;
            Intent intent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, idimageUri);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(intent, ID_REQUEST_IMAGE);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("=========== after clicking image ============");
        System.out.println(requestCode);
        System.out.println(resultCode);
        switch (requestCode) {


            case ID_REQUEST_IMAGE:
                if (idimageUri != null && idimageUri.getPath().length() > 0) {
                    Picasso.with(EditProfileActivity.this).load(idimageUri).into(idIV_update);
                    idIV_update.setVisibility(View.VISIBLE);
                } else {
                    idIV_update.setVisibility(View.GONE);
                }
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
