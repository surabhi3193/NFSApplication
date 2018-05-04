package com.nfsapp.surbhi.nfsapplication.constants;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static java.util.Locale.getDefault;

public class GeocodingLocation {

    private static final String TAG = "GeocodingLocation";

    public static LatLng getLatLngFromAddress(final String locationAddress,
                                              final Context context, final Handler handler) {
        LatLng retResult = null;
        Geocoder geocoder = new Geocoder(context, getDefault());
        String result = null;
        try {
            List addressList = geocoder.getFromLocationName(locationAddress, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = (Address) addressList.get(0);
                retResult = new LatLng(address.getLatitude(),
                        address.getLongitude());
                StringBuilder sb = new StringBuilder();
                sb.append(address.getLatitude()).append("\n");
                sb.append(address.getLongitude()).append("\n");
                result = sb.toString();


            }
        } catch (IOException e) {
            Log.e(TAG, "Unable to connect to Geocoder", e);

        } finally {
            Message message = Message.obtain();
            message.setTarget(handler);
            if (result != null) {
                message.what = 1;
                Bundle bundle = new Bundle();
                result = "Address: " + locationAddress +
                        "\n\nLatitude and Longitude :\n" + result;
                bundle.putString("address", result);
                message.setData(bundle);

            } else {
                message.what = 1;
                Bundle bundle = new Bundle();
                result = "Address: " + locationAddress +
                        "\n Unable to get Latitude and Longitude for this address location.";
                bundle.putString("address", result);
                message.setData(bundle);
            }
            message.sendToTarget();

        }

        return retResult;
    }

    public static String getAddressFromLatlng(LatLng latLng, Context context, int select) {
        Geocoder geocoder;
        List<Address> addresses;
        String city = "";
        String address = "";
        String fulladdress = "";
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            System.out.println("********** user location on geocoading location **********");
            System.out.println(latLng.latitude);
            System.out.println(latLng.longitude);

            double lat = latLng.latitude;
            double lng = latLng.longitude;

            if (lat != 0 && lng != 0) {
                addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                if (addresses.size() > 0) {
                    city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();

                    fulladdress=city;

                    if (knownName.length()>2)
                        fulladdress=knownName+", "+city;

                    if (postalCode.length()>2)
                        fulladdress = knownName + ", " + city + ", " + postalCode;

                } else
                    fulladdress = "N/A";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (select == 0) {
            return city;
        } else {
            System.out.println("---------> full address ------------>");
            System.out.println(fulladdress);
            return fulladdress;
        }

    }
}
