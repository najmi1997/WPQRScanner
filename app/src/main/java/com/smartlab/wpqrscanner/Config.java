package com.smartlab.wpqrscanner;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class Config extends Activity {

    String sfromMain;
	int primary;

	DatabaseHelper dbConfigHelper;
    SQLiteDatabase db;
    dbActivities dba;

    String sHttp;
	Spinner spinHttp;

	private static String imei = "";
	private static final int READ_PHONE_STATE = 0;
	private String ver= "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login1);


        Thread thrd = new Thread(){

            @Override
            public void	 run(){
                while (!isInterrupted()){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showtime();
                        }
                    });
                }
            }
        };
        thrd.start();


        dba = new dbActivities();
        dbConfigHelper = new DatabaseHelper(this);

        sHttp = "";

        spinHttp = (Spinner) findViewById(R.id.spinHttp);

        ArrayAdapter<CharSequence> adapterHttp = ArrayAdapter.createFromResource(this, R.array.server_http, R.layout.spinner_item);
        adapterHttp.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinHttp.setAdapter(adapterHttp);

        if (sHttp.equals(""))
            spinHttp.setSelection(0);
        else if (sHttp.equals("http://"))
            spinHttp.setSelection(0);
        else
            spinHttp.setSelection(1);


        addListenerOnButton();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = {Manifest.permission.READ_PHONE_STATE};
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permissions, READ_PHONE_STATE);
            }
        } else {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }


            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        imei = getIMEIDeviceId(getApplicationContext());

    }

    public static String getIMEIDeviceId(Context context) {

        String deviceId;

        if (Build.VERSION.SDK_INT >= 29)
        {
            deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } else {
            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (context.checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return "";
                }
            }
            assert mTelephony != null;
            if (mTelephony.getDeviceId() != null)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                {
                    deviceId = mTelephony.getImei();
                }else {
                    deviceId = mTelephony.getDeviceId();
                }
            } else {
                deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }
        Log.d("deviceId", deviceId);
        return deviceId;
    }

    public void addListenerOnButton() {

        Button btnSave = (Button) findViewById(R.id.btnSave);
        //Button btnCancel = (Button) findViewById(R.id.btnCancel);


        btnSave.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {

                AlertDialogSave();

            }
        });

    	/*btnCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if (sfromMain.equals("") == false)
		        {
					exitApp();
		        }
				else
					callclass();
			}
		});*/
    }

    public void AlertDialogSave()
    {
        final CharSequence[] items = {"Yes", "Cancel"};
        android.app.AlertDialog.Builder builder = new
                android.app.AlertDialog.Builder(this);
        builder.setTitle("Authenticate this account?");
        builder.setItems(items, new

                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int item) {

                        if(item==0)
                        {
                            final EditText txtUid = (EditText) findViewById(R.id.edtxtUID);
                            final EditText txtPwd = (EditText) findViewById(R.id.edtxtPwd);
                            final EditText txtServer = (EditText) findViewById(R.id.edtxtServer);
                            final CheckBox chkActive = (CheckBox) findViewById(R.id.chkActive);

                            //Save to database
                            //if (dba.SaveConfigDB(dbConfigHelper, 1,txtUid.getText().toString() , txtPwd.getText().toString(), txtServer.getText().toString(), chkActive.isChecked()))
                            try {
                                if (AuthUser(primary, txtUid.getText().toString() , txtPwd.getText().toString(), txtServer.getText().toString(), chkActive.isChecked()) == true)
                                {
                                    if (primary > 0)
                                        showToast("Configuration upated successfully.");


                                    else
                                        showToast("Configuration added successfully.");
                                        exitToMain();

                                }
                                else
                                {
                                    Log.e("Insert User Config", "Insert User Config error");
                                    //showToast("Error insert user data.");
                                    //showToast("Error insert user data ", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        android.app.AlertDialog alerts = builder.create();
        alerts.show();
    }
    public void exitToMain()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    public boolean AuthUser(int primary, String Uid, String Pwd, String Svr, boolean active) throws JSONException {
        Timer_Delay(5000);

        boolean bFlag = false;

        //String token = FirebaseInstanceId.getInstance().getToken();
        Spinner spinHttp = (Spinner) findViewById(R.id.spinHttp);
        sHttp = spinHttp.getItemAtPosition(spinHttp.getSelectedItemPosition()).toString();

        Svr = sHttp + Svr;

        String usr = Uid;
        String pass = Pwd;
        String url = Svr + getResources().getString(R.string.user_auth) ;


        String deviceName = android.os.Build.MODEL;
        String deviceMan = android.os.Build.MANUFACTURER;
        String devID = "";



        //upload data to server
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add((NameValuePair) new BasicNameValuePair("username", usr));
        nameValuePairs.add((NameValuePair) new BasicNameValuePair("password", pass));
        //nameValuePairs.add((NameValuePair) new BasicNameValuePair("token", token));
        nameValuePairs.add((NameValuePair) new BasicNameValuePair("phoneid", imei));

        bFlag = post(url, nameValuePairs, usr);

        String jSONres = "";

        JSONObject jsonobj = new JSONObject();

        jsonobj.accumulate("username", usr);
        jsonobj.accumulate("password", pass);
        jsonobj.accumulate("modelno", deviceName);
        //jsonobj.accumulate("token", token);


        if (bFlag == true)
        {
            if (active == true)
            {
                bFlag = dba.UpdateAllUnActive(dbConfigHelper);
                //if (bFlag == false)
                //	 showToast("Update all un-active status failed.");
            }

            if (primary > 0)
            {
                bFlag = dba.UpdateConfigDB(dbConfigHelper, primary, Uid, Pwd, Svr, active,ver);
                if (bFlag == false)
                    showToast("Error Response : \nUpdating configuration data failed.");
            }
            else
            {
                primary = dba.checkConfigDB(dbConfigHelper);
                ver = getString(R.string.app_ver);
                bFlag = dba.SaveConfigDB(dbConfigHelper, primary+1 ,Uid , Pwd, Svr, active,ver);
                if (bFlag == false)
                    showToast("Error Response : \nInserting configuration data failed.");
            }
        }

        return bFlag;
    }

    public boolean post(String url, List<NameValuePair> nameValuePairs,String UserID) {

        boolean bFlag = false;

        ThreadPolicy tp = ThreadPolicy.LAX;
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

            xpp.setInput( new StringReader (result) );

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
                    errMsg.setVisibility(View.INVISIBLE);
                }
                else
                {
                    //showToast("Error Response : \nUser authentication failed.");
                    bFlag = false;
                    errMsg.setVisibility(View.VISIBLE);
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

    public  void showtime(){

        TextView time = (TextView)findViewById(R.id.time);

        long ltime = System.currentTimeMillis();

        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss a");
        String datetime = sdf1.format(ltime);

        time.setText(datetime);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        System.exit(0);
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

    public void showToast(String strMsg)
    {
        Toast.makeText(this, strMsg, Toast.LENGTH_SHORT).show();
    }
}
