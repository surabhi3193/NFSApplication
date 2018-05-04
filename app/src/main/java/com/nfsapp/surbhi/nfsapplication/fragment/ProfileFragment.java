package com.nfsapp.surbhi.nfsapplication.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.jackandphantom.circularprogressbar.CircleProgressbar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.activities.EnterLoginActivity;
import com.nfsapp.surbhi.nfsapplication.activities.MainActivity;
import com.nfsapp.surbhi.nfsapplication.beans.User;
import com.nfsapp.surbhi.nfsapplication.constants.Utility;
import com.nfsapp.surbhi.nfsapplication.other.GifImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.nfsapp.surbhi.nfsapplication.constants.CheckInternetConnection.isNetworkAvailable;
import static com.nfsapp.surbhi.nfsapplication.constants.GeocodingLocation.getAddressFromLatlng;
import static com.nfsapp.surbhi.nfsapplication.other.MySharedPref.NullData;
import static com.nfsapp.surbhi.nfsapplication.other.MySharedPref.getData;
import static com.nfsapp.surbhi.nfsapplication.other.MySharedPref.saveData;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.BASE_IMAGE_URL;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.BASE_URL_NEW;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.getRealPathFromURI;

public class ProfileFragment extends Fragment {

    private static final int ID_REQUEST_IMAGE = 1;
    private static final int PLACE_PICKER_REQUEST = 2;
    private static final int GALLERY_PICTURE = 3;
    private static final int CAPTURE_IMAGES_FROM_CAMERA = 4;
    CircleImageView profileIV;
    ImageView idIV_main, idIV_update, cancel_btn,edit_img;
    private Uri idimageUri, profile_uri;
    //    private String pathid="";
    private RelativeLayout imageLay;
    private String p_lat = "0.0", p_lng = "0.0";
    private TextView nametV, locationTv, emailTv, phoneTV, accountTv, uploadtV, logoutTV,streetTv,postalTv;
    private EditText cityEt,streetEt,postalEt;
     Dialog dialog;
    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        System.out.println("====== fragment  profile =========");
        View v = inflater.inflate(R.layout.fragment_profile, null);
        Utility.checkCameraPermission(getActivity());

        CircleProgressbar circleProgressbar = (CircleProgressbar) v.findViewById(R.id.progress);
        final TextView edit_btn = v.findViewById(R.id.edit_btn);
        int animationDuration = 1000; // 2500ms = 2,5s
        circleProgressbar.setProgressWithAnimation(62, animationDuration); // Default duration = 1500ms

        imageLay = v.findViewById(R.id.imageLay);
        profileIV = v.findViewById(R.id.profileIV);
        idIV_main = v.findViewById(R.id.idIV_main);
        nametV = v.findViewById(R.id.nameTv);
        locationTv = v.findViewById(R.id.locationTv);
        emailTv = v.findViewById(R.id.emailTV);
        streetTv = v.findViewById(R.id.streetTV);
        postalTv = v.findViewById(R.id.postalTV);
        phoneTV = v.findViewById(R.id.phoneTV);
        accountTv = v.findViewById(R.id.accountTv);
        uploadtV = v.findViewById(R.id.uploadTv);
        logoutTV = v.findViewById(R.id.logoutTv);
        cancel_btn = v.findViewById(R.id.camera_btn);
        edit_img = v.findViewById(R.id.edit_img);


        final User user = User.getInstance();
        setDataToIds(user);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageLay.setVisibility(View.GONE);
            }
        });

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialog(user);
            }
        });

        logoutTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogoutAlertDialog();
            }
        });


        edit_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageDialog();
            }
        });
            profileIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageDialog();
            }
        });


        return v;
    }


    private void LogoutAlertDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Dialog);
        //ab.setTitle("Are you shore you want to log out");
        ab.setMessage("Are you sure you want to logout");
        ab.setNegativeButton("logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                logoutUser();
                dialog.dismiss();
            }
        });

        ab.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ab.show();
    }

    private void setDataToIds(final User user) {

        nametV.setText(user.getName());
        System.err.println("==========getLocation");
        System.err.println(user.getLocation());

        String street ="",postal="",city="";
        try{
            city =user.getLocation().split(",")[1];
            street =user.getLocation().split(",")[0];
           postal= user.getLocation().split(",")[2];

        }
        catch (Exception e)
        {
            e.printStackTrace();
            city =user.getLocation();
    }

        locationTv.setText(city);
        streetTv.setText(street);
        postalTv.setText(postal);

        emailTv.setText(user.getEmail());
        phoneTV.setText(user.getPhone());
        accountTv.setText(user.getAccount_no());

        System.out.println(user.getName());
        System.out.println(user.getProfile_pic());

        if (user.getProfile_pic() != null && user.getProfile_pic().length() > 3) {
            Picasso.with(getActivity()).load(user.getProfile_pic()).placeholder(R.drawable.profile_pic).into(profileIV);
        } else
            profileIV.setImageResource(R.drawable.profile_pic);

        if (user.getId_image() != null && !user.getId_image().equals(BASE_IMAGE_URL)) {
            uploadtV.setText("View Id Card");
            uploadtV.setTextColor(getActivity().getResources().getColor(R.color.light_red));
            uploadtV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utility.checkWriteStoragePermission(getActivity());
                    imageLay.setVisibility(View.VISIBLE);
                    Picasso.with(getActivity()).load(user.getId_image()).noPlaceholder().into(idIV_main);
                }
            });
        } else {
            uploadtV.setText("Upload valid id card By clicking edit");
            imageLay.setVisibility(View.GONE);
        }

        imageLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageLay.setVisibility(View.GONE);
            }
        });
    }
    private void ShowDialog(final User user) {

        AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
        final AlertDialog alert;
        alert = ab.create();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.update_profile, null);
        final EditText fullnameET = v.findViewById(R.id.fullnameET);
        cityEt = v.findViewById(R.id.cityEt);
        streetEt = v.findViewById(R.id.streetET);
        postalEt = v.findViewById(R.id.postalET);
        final EditText emailEt = v.findViewById(R.id.EmailEt);
        final EditText phoneNumberEditText = v.findViewById(R.id.phoneNumberEditText);
        final EditText accountEt = v.findViewById(R.id.accountEt);
        final TextView uploadTV = v.findViewById(R.id.updateIDTV);
        idIV_update = v.findViewById(R.id.idIV);

        Button cancel_button = (Button) v.findViewById(R.id.cancel_button);
        Button update_button = (Button) v.findViewById(R.id.update_button);

        fullnameET.setText(user.getName());
        cityEt.setText(user.getLocation());
        emailEt.setText(user.getEmail());
        phoneNumberEditText.setText(user.getPhone());
        accountEt.setText(user.getAccount_no());

//
//        cityEt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                System.out.println("====== pickup clicked======");
//                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//
//                try {
//                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        if (user.getId_image() != null && !user.getId_image().equals(BASE_IMAGE_URL)) {
            uploadTV.setText("Update id Card");
            uploadTV.setTextColor(getActivity().getResources().getColor(R.color.light_red));
            idIV_update.setVisibility(View.VISIBLE);
            Picasso.with(getActivity()).load(user.getId_image()).noPlaceholder().into(idIV_update);
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

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                alert.cancel();
            }
        });
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = fullnameET.getText().toString();
                String city = cityEt.getText().toString();
                String street = streetEt.getText().toString();
                String postal = postalEt.getText().toString();
                String email = emailEt.getText().toString();
                String mobile = phoneNumberEditText.getText().toString();
                String acc_no = accountEt.getText().toString();


                if (isNetworkAvailable(getActivity())) {
//                   String mob = phoneNumberEditText.getText().toString();
//                    String mpatt = "[0-9]{10,10}";
//                    if (mob.length()>0) {
//                        boolean b3 = isMatch(mob, mpatt);
//                        if (!b3) {
//                            makeToast(getActivity(),"please enter valid mobile no");
//                            return;
//                        }
//                    }
                    String pathid = "";
                    if (idimageUri != null && idimageUri.getPath().length() > 0) {
                        pathid = getRealPathFromURI(idimageUri, getActivity());
                    }


                    PostUserUpdatedDetail(user.getId(), name, city,street,postal, email, mobile,
                            acc_no, pathid);

                    alert.dismiss();
                    alert.cancel();
                } else {
                    makeToast(getActivity(), "Connection Unavailable, Try again !");
                }

            }
        });

        alert.setView(v);
        alert.show();

    }

    private void getIDImage() {
        boolean per = Utility.checkWriteStoragePermission(getActivity());
        if (per) {
            String fileName = "Camera_Example.jpg";
            Uri imageUri;
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, fileName);
            values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");


            imageUri = getActivity().getContentResolver().insert(
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

            case PLACE_PICKER_REQUEST:

                if (resultCode == Activity.RESULT_OK) {

                    Place place = PlacePicker.getPlace(data, getActivity());
                    LatLng location = place.getLatLng();
                    p_lat = String.valueOf(location.latitude);
                    p_lng = String.valueOf(location.longitude);
                    String new_location = getAddressFromLatlng(location, getActivity(), 0);
                    cityEt.setText(new_location);
                }
                break;

            case ID_REQUEST_IMAGE:
                if (idimageUri != null && idimageUri.getPath().length() > 0) {
                    Picasso.with(getActivity()).load(idimageUri).into(idIV_update);
                    idIV_update.setVisibility(View.VISIBLE);
                } else {
                    idIV_update.setVisibility(View.GONE);
                }
                break;

            case GALLERY_PICTURE:
                if (resultCode == -1) {
                    onSelectFromGalleryResult(data);
                }
                break;
            case CAPTURE_IMAGES_FROM_CAMERA:
                if (profile_uri != null && profile_uri.getPath().length() > 0) {
                 String   pathid = getRealPathFromURI(profile_uri, getActivity());
                    File imageFile = new File(pathid);
                    updatePIc(imageFile);
                }

                break;


        }
    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (bm != null) {
            Uri uri = getImageUri(getActivity(), bm);
            updatePIc(new File(getRealPathFromURI(uri, getActivity())));
        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void imageDialog()
    {
        Utility.checkReadStoragePermission(getActivity());
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
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select File"), GALLERY_PICTURE);
                dialog.dismiss();
            }
        });

        cam_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean per = Utility.checkWriteStoragePermission(getActivity());
                if (per) {
                    {
                        String fileName = "Camera_Example.jpg";
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, fileName);
                        values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");

                        profile_uri = getActivity().getContentResolver().insert(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                        System.out.println("========== image uri ========= ");
                        if (profile_uri != null)
                            System.out.println(profile_uri.getPath());

                        Intent intent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, profile_uri);
                        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                        startActivityForResult(intent, CAPTURE_IMAGES_FROM_CAMERA);

                    }
                }
                dialog.dismiss();
            }
        });
    }

    private void PostUserUpdatedDetail(String id, String name, String city,String street,String postal, String email,
                                       String mobile, String acc_no, String pathid) {

        final Dialog ringProgressDialog = new Dialog(getActivity(), R.style.Theme_AppCompat_Dialog);
        ringProgressDialog.setContentView(R.layout.loading);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ringProgressDialog.show();
        GifImageView gifview = ringProgressDialog.findViewById(R.id.loaderGif);
        gifview.setGifImageResource(R.drawable.loader2);


        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();
        String userid = getData(getActivity(), "user_id", "");

        params.put("user_id", userid);
        params.put("full_name", name);
        params.put("user_email", email);
        params.put("deposit_account", acc_no);
        params.put("user_city", city);
        params.put("user_street", street);
        params.put("user_postalcode", postal);

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
                        Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_LONG).show();
                    } else {
                        JSONObject jsonObject = response.getJSONObject("result");
                        String street =jsonObject.getString("user_street");
                        String city =jsonObject.getString("user_city").trim();
                        String postal =jsonObject.getString("user_postalcode").trim();
                        String address =street+", " + city+ ", "+postal;

                        if (address.startsWith(",") && address.endsWith(","))
                            address=city;


                        final User user = User.getInstance();

                        user.setId(jsonObject.getString("user_id"));
                        user.setProfile_pic(jsonObject.getString("user_pic"));
                        user.setName(jsonObject.getString("user_name"));
                        user.setLocation(address);
                        user.setProfile_percent(jsonObject.getString("profile_sttaus"));
                        user.setEmail(jsonObject.getString("user_email"));
                        user.setPhone(jsonObject.getString("user_phone_no"));
                        user.setAccount_no(jsonObject.getString("user_deposit_ac_no"));
                        user.setId_image(jsonObject.getString("valid_identity"));
                        setDataToIds(user);

                    }
                } catch (Exception e) {
                    ringProgressDialog.dismiss();

                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getActivity(), "Server Error,Try Again ", Toast.LENGTH_LONG).show();
                ringProgressDialog.dismiss();

            }

            public void onFailure(int statusCode, Header[] headers, String responseString) {
                ringProgressDialog.dismiss();
                System.out.println(responseString);
            }
        });
    }

    private void updatePIc(File file) {
        final Dialog ringProgressDialog = new Dialog(getActivity(), R.style.Theme_AppCompat_Dialog);
        ringProgressDialog.setContentView(R.layout.loading);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ringProgressDialog.show();
        GifImageView gifview = ringProgressDialog.findViewById(R.id.loaderGif);
        gifview.setGifImageResource(R.drawable.loader2);


        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();
        String userid = getData(getActivity(), "user_id", "");

        System.out.println("========== updatepic=========");
        System.out.println(file);
        params.put("user_id", userid);
        try {
            params.put("user_pic", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
//            Toast.makeText(getActivity(), "Image not found", Toast.LENGTH_LONG).show();
            ringProgressDialog.dismiss();
        }

        client.post(BASE_URL_NEW + "update_profile", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    ringProgressDialog.dismiss();

                    System.out.println("response**********");
                    System.out.println(response);
                    if (response.getString("status").equals("0")) {
                        Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_LONG).show();
                    } else {
                        JSONObject jsonObject = response.getJSONObject("result");
                        String firstname = jsonObject.getString("user_name");
                        String profile_photo = jsonObject.getString("user_pic");
                        final User user = User.getInstance();
                        user.setProfile_pic(profile_photo);

                        Picasso.with(getActivity()).load(profile_photo).
                                placeholder(R.drawable.profile_pic).into(profileIV);

                        Picasso.with(getActivity()).load(profile_photo).
                                placeholder(R.drawable.profile_pic).into((((MainActivity) getActivity()).profile_pic));
                    }
                } catch (Exception e) {
                    ringProgressDialog.dismiss();

                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                Toast.makeText(getActivity(), "Server Error,Try Again ", Toast.LENGTH_LONG).show();
                ringProgressDialog.dismiss();
                System.out.println(errorResponse);

            }

            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//               Toast.makeText(getActivity(), "Server Error,Try Again ", Toast.LENGTH_LONG).show();
                ringProgressDialog.dismiss();
                System.out.println(responseString);
            }

        });
    }

    private void logoutUser() {
        final Dialog ringProgressDialog = new Dialog(getActivity(), R.style.Theme_AppCompat_Dialog);
        ringProgressDialog.setContentView(R.layout.loading);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ringProgressDialog.show();
        GifImageView gifview = ringProgressDialog.findViewById(R.id.loaderGif);
        gifview.setGifImageResource(R.drawable.loader2);

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();
        String userid = getData(getActivity(), "user_id", "");

        params.put("user_id", userid);

        client.post(BASE_URL_NEW + "logout", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    ringProgressDialog.dismiss();

                    System.out.println("response**********");
                    System.out.println(response);
                    if (response.getString("status").equals("1"))
                    {
                        saveData(getActivity(), "login", "0");
                        NullData(getActivity(), "user_id");
                        Intent intent = new Intent(getActivity(), EnterLoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("EXIT", true);
                        startActivity(intent);
                        getActivity().finish();
                    }
                        Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_LONG).show();
                }
                catch (Exception e) {
                    ringProgressDialog.dismiss();

                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getActivity(), "Server Error,Try Again ", Toast.LENGTH_LONG).show();
                ringProgressDialog.dismiss();
                System.out.println(errorResponse);

            }

            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getActivity(), "Server Error,Try Again ", Toast.LENGTH_LONG).show();
                ringProgressDialog.dismiss();
                System.out.println(responseString);
            }

        });
    }


    public void onBackPressed() {
        if (dialog!=null)
            dialog.dismiss();

    }

}