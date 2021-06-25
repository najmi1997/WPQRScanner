package com.smartlab.wpqrscanner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(getApplicationContext());
        dba = new dbActivities();

        //checkConfigDB() ;
        if (db.openDB()) {
            // Toast.makeText(this,"Db created", Toast.LENGTH_SHORT).show();
        } else {
            //   Toast.makeText(this,"Db doesnt exists", Toast.LENGTH_SHORT).show();

        }
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        checkConfigDB();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.contain_main, new ScanFragment());
            fragmentTransaction.commit();

        }

        txtCoor =(TextView) findViewById(R.id.edtxtCoor);
        txtLoc = (TextView) findViewById(R.id.edtxtLoc);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapViewMain);
        // method to get the location
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        //getLastLocation();



    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Really Exit?")
                    .setMessage("Are you sure you want to exit?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            System.exit(0);
                        }
                    }).create().show();
        }
    }


    //--------------------------------------------------------------------------------------------------------------------------------------
    //navigation selected item

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action

            //new com.google.zxing.integration.android.IntentIntegrator(this).initiateScan();

            //-- to check permission
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.contain_main, new ScanFragment());
            fragmentTransaction.commit();


        } else if (id == R.id.nav_gallery) {

            /*LayoutInflater inflater = getLayoutInflater();
            FrameLayout container = (FrameLayout) findViewById(R.id.contain_main);
            inflater.inflate(R.layout.fragment_assetlist, container);*/

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            //fragmentTransaction.replace(R.id.contain_main, new AssetFragment());
            fragmentTransaction.replace(R.id.contain_main, new WebViewFragment());
            fragmentTransaction.commit();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //--------------------------------------------------------------------------------------------------------------------------------------
    //pass qr result

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                String check = result.getContents().toString();
                if (check.contains("|")) {
                    Log.d("MainActivity", "Scanned");

                    //AssetFragment fragment = new AssetFragment();
                    WebViewFragment fragment = new WebViewFragment();
                    Bundle args = new Bundle();

                    args.putString("AssetInfo", result.getContents());

                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragment.setArguments(args);
                    fragmentTransaction.replace(R.id.contain_main, fragment);
                    //fragmentTransaction.commit();

                    fragmentTransaction.commitAllowingStateLoss();

                } else {
                    Toast.makeText(this, "invalid Asset Tag", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------------------
    //check db connection

    public void checkConfigDB() {
        try{
            retbool = getCurrentDomain();

        }catch (Exception e){//
            //
        }
        if (!retbool) {
            finish();
            Intent intent = new Intent(this, Config.class);
            startActivity(intent);
        }

    }

    //--------------------------------------------------------------------------------------------------------------------------------------
    //check location

   /* @SuppressLint("MissingPermission")
    private void getLastLocation() {
        txtCoor =(TextView) findViewById(R.id.edtxtCoor);
        txtLoc = (TextView) findViewById(R.id.edtxtLoc);

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
                                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                                String address = addresses.get(0).getAddressLine(0);
*//*
                                mapFragment.getMapAsync(new OnMapReadyCallback() {
                                    @Override
                                    public void onMapReady(GoogleMap googleMap) {
                                        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                                        MarkerOptions options = new MarkerOptions().position(latLng).title("Your Location");
                                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                                        googleMap.addMarker(options);
                                    }
                                });*//*
                                txtLoc.setText(address);
                                txtCoor.setText(location.getLatitude() + ","+location.getLongitude());
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
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
        txtCoor =(TextView) findViewById(R.id.edtxtCoor);
        txtLoc = (TextView) findViewById(R.id.edtxtLoc);
        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();

            try {
                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
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
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

    }

    // request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // check if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
    }*/

    //--------------------------------------------------------------------------------------------------------------------------------------
    //check login condition

    private boolean getCurrentDomain() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        final String server = dba.getConfigValue(db, "server",
                "active='1'");
        final String uid = dba.getConfigValue(db, "username",
                "active='1'");

        final String pwd = dba.getConfigValue(db, "password",
                "active='1'");

        return AuthUser(primary, uid, pwd, server, true);



    }
    public boolean AuthUser(int primary, String Uid, String Pwd, String Svr, boolean active){
        Timer_Delay(5000);

        String sHttp="";

        boolean bFlag = false;

        //String token = FirebaseInstanceId.getInstance().getToken();
        String token = "h21n42nb";


        String usr = Uid;
        String pass = Pwd;
        String url = Svr + getResources().getString(R.string.user_auth) ;


        usr = usr.replaceAll(" ","");
        pass = pass.replaceAll(" ","");
        url = url.replaceAll(" ","");


        //upload data to server
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add((NameValuePair) new BasicNameValuePair("username", usr));
        nameValuePairs.add((NameValuePair) new BasicNameValuePair("password", pass));
        nameValuePairs.add((NameValuePair) new BasicNameValuePair("token", token));

        //showToast(token);
        bFlag = post(url, nameValuePairs, usr);

        if (bFlag == true)
        {


        }

        return bFlag;
    }

    public void Timer_Delay(int millisec)
    {
        TimerTask tskSaveToDb;
        Timer tmr = new Timer();

        tskSaveToDb = new TimerTask() {
            public void run() {
                delay();
            }};

        tmr.schedule(tskSaveToDb, millisec);
    }

    public void delay()
    {
        for (int i=0; i<100; i++)
        {
            for (int j=0; j<100; j++)
            {
                i = i + 0;
                j = j + 0;
            }
        }
    }
    public boolean post(String url, List<NameValuePair> nameValuePairs,String UserID) {

        boolean bFlag = false;

        StrictMode.ThreadPolicy tp = StrictMode.ThreadPolicy.LAX;
        StrictMode.setThreadPolicy(tp);

        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        HttpContext localContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost(url);

        try {

            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

            for(int index=0; index < nameValuePairs.size(); index++) {
                if(nameValuePairs.get(index).getValue() != null){
                    entity.addPart(nameValuePairs.get(index).getName(), new StringBody(nameValuePairs.get(index).getValue()));
                    Log.i(nameValuePairs.get(index).getName(), nameValuePairs.get(index).getValue());
                }
            }

            httpPost.setEntity(entity);
            Log.i("getRequestLine()" , httpPost.getRequestLine().toString());

            HttpResponse response = httpClient.execute(httpPost, localContext);

            HttpEntity resEntity = response.getEntity();

            Log.i("getStatusLine()",response.getStatusLine().toString());

            if (resEntity != null) {
                String res =  EntityUtils.toString(resEntity);

                Log.i("post : res",res);

                bFlag = xmlParse(res,UserID);

            }

            if (resEntity != null)
                resEntity.consumeContent();

            httpClient.getConnectionManager().shutdown();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("post() ~ Exception",e.getClass().getName() + " : " + e.getMessage());
	            /*Toast.makeText(
	                    getApplicationContext(),
	                    "Exception ~ post() :\n" + e.getClass().getName() + " \n" + e.getMessage(),
	                    Toast.LENGTH_LONG).show();*/

            Toast.makeText(getApplicationContext(), "Failure connecting to '" + e.getMessage() + "'. \nPlease check your internet connection.", Toast.LENGTH_LONG).show();

            bFlag = false;
        }

        return bFlag;
    }

    public boolean xmlParse(String result, String UserID)
    {
        boolean bFlag = false;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput( new StringReader(result) );

            int eventType = xpp.getEventType();

            String uid = "";
            String info = "";
            String id = "";

            while (eventType != XmlPullParser.END_DOCUMENT)
            {

                if(eventType == XmlPullParser.START_DOCUMENT)
                {
                    Log.i("Start document", "start");
                }
                else if(eventType == XmlPullParser.START_TAG)
                {
                    Log.i("Start tag ",xpp.getName());
                    if (xpp.getName().equals("MSG"))
                    {
                        id = xpp.getName();
                    }
                }
                else if(eventType == XmlPullParser.END_TAG)
                {
                    Log.i("End tag ",xpp.getName());
                }
                else if(eventType == XmlPullParser.TEXT)
                {
                    if (id.equals("MSG"))
                    {
                        info = xpp.getText();
                    }
                    else
                        uid = xpp.getText();
                }

                eventType = xpp.next();
            }
            Log.i("End document", "end");
            if ( uid != "" && uid != null)
            {
                TextView errMsg = (TextView)findViewById(R.id.txtError);
                if (uid.equals(UserID))
                {
                    //showToast("User authentication successfull.");
                    bFlag = true;
                    // errMsg.setVisibility(View.INVISIBLE);
                }
                else
                {
                    //showToast("Error Response : \nUser authentication failed.");
                    bFlag = false;
                    // errMsg.setVisibility(View.VISIBLE);
                }
            }
            else
            {
                bFlag = false;
                showToast("Error Response : \nUser authentication failed.");
                Log.e("xmlError authentication", info);
            }
        }
        catch (Exception e1)
        {
            Log.e("xmlParse() ~ Exception", e1.getClass().getName() + " : " + e1.getMessage(), e1);

            Toast.makeText(
                    getApplicationContext(),
                    "Exception ~ xmlParse() :\n" + e1.getClass().getName() + " \n" + e1.getMessage(),
                    Toast.LENGTH_LONG).show();

            bFlag = false;
        }

        return bFlag;

    }

    public void showToast(String strMsg)
    {
        Toast.makeText(this, strMsg, Toast.LENGTH_SHORT).show();
    }


}
