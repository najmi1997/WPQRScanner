package com.smartlab.wpqrscanner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.List;
import java.util.Locale;

public class ScanFragment extends Fragment {
    // String name = getArguments().getString("AssetInfo", "");
    Button button;


    DatabaseHelper db;
    dbActivities dba;
    Boolean retbool = false;
    TextView txtCoor,txtLoc;
    int primary;

    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;

    SupportMapFragment mapFragment;
    FusedLocationProviderClient locationProviderClient;
    @Override
    public void onSaveInstanceState(Bundle outState) {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login, parent, false);
        button=view.findViewById(R.id.btnSave);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateCameraPermission();
            }


        });
        txtCoor =(TextView) view.findViewById(R.id.edtxtCoor);
        txtLoc = (TextView) view.findViewById(R.id.edtxtLoc);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.mapViewMain);
        // method to get the location
        locationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        getLastLocation();
        return view;
    }


    //--------------------------------------------------------------------------------------------------------------------------------------
    //qr

    private void validateCameraPermission(){
        String[] permissions={android.Manifest.permission.CAMERA};
        if (ContextCompat.checkSelfPermission(getContext(), permissions[0])== PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "permission granted", Toast.LENGTH_SHORT);
            scanQR();
        }else{
            ActivityCompat.requestPermissions(getActivity(), permissions ,1);
        }
    }

    public void scanQR(){
        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("");
        integrator.setOrientationLocked(true);
        integrator.setBeepEnabled(true);
        integrator.setCaptureActivity(CaptureActivityPortrait.class);
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.initiateScan();

    }

    //--------------------------------------------------------------------------------------------------------------------------------------
    //location
    @SuppressLint("MissingPermission")
    private void getLastLocation() {

        txtCoor =(TextView) getActivity().findViewById(R.id.edtxtCoor);
        txtLoc = (TextView) getActivity().findViewById(R.id.edtxtLoc);

        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        final Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {

                            try {
                                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                                String address = addresses.get(0).getAddressLine(0);
/*
                                mapFragment.getMapAsync(new OnMapReadyCallback() {
                                    @Override
                                    public void onMapReady(GoogleMap googleMap) {
                                        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                                        MarkerOptions options = new MarkerOptions().position(latLng).title("Your Location");
                                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                                        googleMap.addMarker(options);
                                    }
                                });*/
                                txtLoc.setText(address);
                                txtCoor.setText(location.getLatitude() + ","+location.getLongitude());
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                });
            } else {
                Toast.makeText(getContext(), "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {
        txtCoor =(TextView) getActivity().findViewById(R.id.edtxtCoor);
        txtLoc = (TextView) getActivity().findViewById(R.id.edtxtLoc);
        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();

            try {
                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(mLastLocation.getLatitude(),mLastLocation.getLongitude(),1);
                String address = addresses.get(0).getAddressLine(0);
                txtCoor.setText( mLastLocation.getLatitude() + ","+ mLastLocation.getLongitude());
                txtLoc.setText(address);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    // check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

    }

    // request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // check if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    //check If everything is alright then
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }
}