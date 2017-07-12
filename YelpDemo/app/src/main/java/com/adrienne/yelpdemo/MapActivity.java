package com.adrienne.yelpdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener,
    GoogleApiClient.ConnectionCallbacks, LocationListener {

    public static final String TAG = "MAP_ACTIVITY";

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    public static final int myPermissionRequestCode = 0;
    public static final int REQUEST_LOCATION = 1;
    private Location mLastLocation;
    //todo does location need to be global??
    private String mLongitude, mLatitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        int permissionCheck = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, myPermissionRequestCode);
        } else {
//            Toast.makeText(this, "Permissions needed to continue.", Toast.LENGTH_SHORT).show();
            //todo close app??
        }

        if (mGoogleApiClient == null){
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API).build();
        }

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == myPermissionRequestCode){
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            } else {

            }
            return;
        }
    }

    protected void onStart(){
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop(){
        mGoogleApiClient.disconnect();
        super.onStop();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
        //todo see if needed for location retrieval
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    REQUEST_LOCATION);
        return;
        }
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if(mLastLocation != null){

                LatLng lastLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(lastLocation).title("You are here"));
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().zoom(20).target(lastLocation).build()));
                mLongitude = String.valueOf(mLastLocation.getLongitude());
                mLatitude = String.valueOf(mLastLocation.getLatitude());
                Log.d(TAG, "onConnected: longitute " + mLongitude);
                Log.d(TAG, "onConnected: latitude " + mLatitude);
            }
            /*if(mLastLocation != null){
                LocationRequest locationRequest = LocationRequest.create()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                        locationRequest, this);
            } else {
                mLongitude = String.valueOf(mLastLocation.getLongitude());
                mLatitude = String.valueOf(mLastLocation.getLatitude());
            }*/
        }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onLocationChanged(Location location) {

    }
}
