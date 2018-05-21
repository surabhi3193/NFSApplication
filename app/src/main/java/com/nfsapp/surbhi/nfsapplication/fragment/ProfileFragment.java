package com.nfsapp.surbhi.nfsapplication.fragment;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
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
import com.nfsapp.surbhi.nfsapplication.activities.EditProfileActivity;
import com.nfsapp.surbhi.nfsapplication.activities.EnterLoginActivity;
import com.nfsapp.surbhi.nfsapplication.activities.MainActivity;
import com.nfsapp.surbhi.nfsapplication.beans.User;
import com.nfsapp.surbhi.nfsapplication.constants.Utility;
import com.nfsapp.surbhi.nfsapplication.other.GifImageView;
import com.nfsapp.surbhi.nfsapplication.services.LocationService;
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
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.BASE_ID_IMAGE_URL;
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
    CircleProgressbar circleProgressbar;
    private EditText cityEt,streetEt,postalEt;
     Dialog dialog;
    private TextView nametV, locationTv, emailTv, phoneTV, accountTv, uploadtV, logoutTV,streetTv,postalTv,percent_text;
    private User user;

    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        System.out.println("====== fragment  profile =========");
        View v = inflater.inflate(R.layout.fragment_profile, null);
        Utility.checkCameraPermission(getActivity());

         circleProgressbar =v.findViewById(R.id.progress);
        final TextView edit_btn = v.findViewById(R.id.edit_btn);
        int animationDuration = 1000; // 2500ms = 2,5s

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
        percent_text = v.findViewById(R.id.percent_text);

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageLay.setVisibility(View.GONE);
            }
        });

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ShowDialog(user);
                startActivity(new Intent(getActivity(), EditProfileActivity.class));
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

        String name = (user.getfirstName() +" "+user.getMiddleName()+" "+user.getLastName());
        nametV.setText(name);

        locationTv.setText(user.getCity() + ", "+ user.getState());
        streetTv.setText(user.getStreet());
        postalTv.setText(user.getZipcode());
        emailTv.setText(user.getEmail());
        phoneTV.setText(user.getPhone());
        accountTv.setText(user.getAccount_no());
        percent_text.setText("Profile is "+user.getProfile_percent()+"% completed");
        circleProgressbar.setProgressWithAnimation(Float.parseFloat(user.getProfile_percent()), 1000); // Default duration = 1500ms

        System.out.println(user.getfirstName());
        System.out.println(user.getProfile_pic());
        System.out.println(Float.parseFloat(user.getProfile_percent()));

        if (user.getProfile_pic() != null && user.getProfile_pic().length() > 3) {
            Picasso.with(getActivity()).load(user.getProfile_pic()).placeholder(R.drawable.profile_pic).into(profileIV);
        } else
            profileIV.setImageResource(R.drawable.profile_pic);


        System.out.println("========= user getidimage========");
        System.out.println(user.getId_image());
        System.out.println(user.getId_image().equals(BASE_ID_IMAGE_URL));


        if (user.getId_image() != null && !user.getId_image().equalsIgnoreCase(BASE_ID_IMAGE_URL)) {
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

                        String profile_photo = jsonObject.getString("user_pic");
                        String street =jsonObject.getString("user_street");
                        String street2 =jsonObject.getString("user_street2");
                        String city =jsonObject.getString("user_city").trim();
                        String user_state =jsonObject.getString("user_state");
                        String postal =jsonObject.getString("user_postalcode").trim();
                        String per = jsonObject.getString("profile_status");

                        final User user = User.getInstance();

                        user.setId(jsonObject.getString("user_id"));
                        user.setProfile_pic(profile_photo);
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

                        saveData(getActivity(),"profile_percent",per);

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

    @Override
    public void onResume() {
        super.onResume();
        final User user = User.getInstance();
        setDataToIds(user);
    }
}