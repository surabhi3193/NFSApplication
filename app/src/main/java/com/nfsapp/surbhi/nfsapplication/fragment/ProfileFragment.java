package com.nfsapp.surbhi.nfsapplication.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.nfsapp.surbhi.nfsapplication.beans.User;
import com.nfsapp.surbhi.nfsapplication.constants.Utility;
import com.nfsapp.surbhi.nfsapplication.other.GifImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    CircleImageView profileIV;
    ImageView idIV_main, idIV_update,cancel_btn;
    private Uri idimageUri;
    //    private String pathid="";
    private RelativeLayout imageLay;
    private String p_lat = "0.0", p_lng = "0.0";
    private TextView nametV, locationTv, emailTv, phoneTV, accountTv, uploadtV, logoutTV, cityEt;

    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        phoneTV = v.findViewById(R.id.phoneTV);
        accountTv = v.findViewById(R.id.accountTv);
        uploadtV = v.findViewById(R.id.uploadTv);
        logoutTV = v.findViewById(R.id.logoutTv);
        cancel_btn = v.findViewById(R.id.cancel_btn);


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


        return v;
    }


    private void LogoutAlertDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Dialog);
        //ab.setTitle("Are you shore you want to log out");
        ab.setMessage("Are you sure you want to log out");
        ab.setNegativeButton("logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveData(getActivity(), "login", "0");
                NullData(getActivity(), "user_id");

                Intent intent = new Intent(getActivity(), EnterLoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);
                getActivity().finish();
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
        locationTv.setText(user.getLocation());
        emailTv.setText(user.getEmail());
        phoneTV.setText(user.getPhone());
        accountTv.setText(user.getAccount_no());

        System.out.println(user.getName());
        System.out.println(user.getProfile_pic());

        if (user.getProfile_pic() != null && user.getProfile_pic().length() > 3) {

//            pathid=user.getProfile_pic();
            Picasso.with(getActivity()).load(user.getProfile_pic()).placeholder(R.drawable.profile_pic).into(profileIV);
        } else
            profileIV.setImageResource(R.drawable.profile_pic);

        if (user.getId_image() != null && !user.getId_image().equals(BASE_IMAGE_URL)) {
            uploadtV.setText("View Id Card");
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

    }

    private void ShowDialog(final User user) {
        AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
        final AlertDialog alert;
        alert = ab.create();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.update_profile, null);

        final EditText fullnameET = v.findViewById(R.id.fullnameET);
        cityEt = v.findViewById(R.id.cityEt);
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


        cityEt.setOnClickListener(new View.OnClickListener() {
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

        if (!user.getId_image().equals(BASE_IMAGE_URL)) {
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


                    PostUserUpdatedDetail(user.getId(), name, city, email, mobile,
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

    private void PostUserUpdatedDetail(String id, String name, String city, String email,
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

        System.out.println("path id ========"+pathid);
        if (pathid != null && pathid.length()>0) {
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
                        Toast.makeText(getActivity().getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                    } else {
                        JSONObject jsonObject = response.getJSONObject("result");
                        final User user = User.getInstance();

                        user.setId(jsonObject.getString("user_id"));
                        user.setProfile_pic(jsonObject.getString("user_pic"));
                        user.setName(jsonObject.getString("user_name"));
                        user.setLocation(jsonObject.getString("user_city"));
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
                Toast.makeText(getActivity().getApplicationContext(), "Server Error,Try Again ", Toast.LENGTH_LONG).show();
                ringProgressDialog.dismiss();

            }

            public void onFailure(int statusCode, Header[] headers, String responseString) {
                ringProgressDialog.dismiss();
                System.out.println(responseString);
            }
        });
    }

    boolean isMatch(String s, String patt) {
        Pattern pat = Pattern.compile(patt);
        Matcher m = pat.matcher(s);
        return m.matches();
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
                    String new_location = getAddressFromLatlng(location, getActivity().getApplicationContext(), 0);
                    cityEt.setText("  " + new_location);
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


        }
    }
}