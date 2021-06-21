package com.smartlab.wpqrscanner;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
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

import com.google.zxing.integration.android.IntentIntegrator;

public class ScanFragment extends Fragment {
    // String name = getArguments().getString("AssetInfo", "");
    Button button;
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

        return view;
    }

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

}