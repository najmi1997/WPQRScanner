package com.smartlab.wpqrscanner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;


public class WebAppInterface extends FragmentActivity{
    private Context context;

    public WebAppInterface (Context context){
        this.context = context;
    }

    @JavascriptInterface
    public void showToast(String toast)
    {
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void openQR()
    {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        /*FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.contain_main, new ScanFragment());
        fragmentTransaction.commit();*/

        //scanQR();

    }



    @SuppressLint("NewApi")
    private void validateCameraPermission(){
        String[] permissions={android.Manifest.permission.CAMERA};

        if (context.checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT);
            scanQR();
        }else{
            ActivityCompat.requestPermissions(this, permissions ,1);
        }
    }

    public void  scanQR() {
        IntentIntegrator integrator = new IntentIntegrator(WebAppInterface.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("");
        integrator.setOrientationLocked(true);
        integrator.setBeepEnabled(true);
        integrator.setCaptureActivity(CaptureActivityPortrait.class);
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.initiateScan();

    }
}
