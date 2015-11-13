package mpt.is416.com.teamup;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    //Defining Variables
    private Context context;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private final String ANDROID_ID = "android_id";
    private final String TAG = MainActivity.class.getSimpleName();
    static ArrayAdapterChatRoom aac = null;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    GoogleCloudMessaging gcmObj;
    public static final String REG_ID = "regId";
    public static final String DEVICE_ID = "deviceId";

    String regId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        // Check first launch for QR code generation
        if (isFirstLaunch()) {

            // Get androidId
            final String androidId = Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString(ANDROID_ID,androidId).apply();

            registerInBackground(androidId);

            // Flag that first launch completed
            PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("isFirstLaunch", false).commit();
        }

        if (checkPlayServices()) {
            registerInBackground(DEVICE_ID);
        }

        //Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //default contentView, for now is FragmentQRCode
        Fragment fragment = new FragmentChats();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_main, fragment);
        fragmentTransaction.commit();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {

                        // Handle navigation view item clicks here.
                        int id = item.getItemId();
                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);

                        Fragment fragment;
                        boolean noFragment = false;

                        switch (id) {
                            // GROUPS
                            case R.id.nav_groups:
                                Toast.makeText(getApplicationContext(), "Your Groups", Toast.LENGTH_SHORT)
                                        .show();
                                fragment = new FragmentChats();
                                item.setChecked(true);
                                break;
                            case R.id.nav_qrcode:
                                Toast.makeText(getApplicationContext(), "Your QR Code", Toast.LENGTH_SHORT)
                                        .show();
                                fragment = new FragmentQRCode();
                                item.setChecked(true);
                                break;
                            // DEADLINES
//                    case R.id.nav_deadlines:
//                        Toast.makeText(getApplicationContext(), "Your Deadlines",
//                                Toast.LENGTH_SHORT).show();
//                        fragment = new FragmentDeadlines();
//                        item.setChecked(true);
//                        break;
                            default:
                                Toast.makeText(getApplicationContext(), "Something is Wrong",
                                        Toast.LENGTH_SHORT).show();
                                fragment = getSupportFragmentManager().findFragmentById(R.id.content_main);
                                noFragment = true;
                                item.setChecked(true);
                                break;
                        }

                        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                                .beginTransaction();
                        if (fragment != null && noFragment) {
                            fragmentTransaction.remove(fragment);
                        } else if (fragment != null) {
                            fragmentTransaction.replace(R.id.content_main, fragment);
                        }
                        fragmentTransaction.commit();

                        return true;
                    }
                });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replaced with L3 Group Milestones", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(context, ViewGroupMilestonesActivity.class);
                startActivity(intent);
            }
        });

        //Initializing Drawer Layout and ActionBarToggle
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_group) {
            Toast.makeText(getApplicationContext(), "Add Group", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, AddNewGroupActivity.class);
            startActivity(intent);
            return true;
        }
        /*
        else if (id == R.id.action_scan) {
            // Refer back to commit 9 for method to display output
            Intent intent = new Intent(this, ScannerActivity.class);
            startActivity(intent);
            startActivityForResult(intent, 1);
            //IntentIntegrator integrator = new IntentIntegrator(this);

            integrator.addExtra("SCAN_WIDTH", 640);
            integrator.addExtra("SCAN_HEIGHT", 640);
            integrator.addExtra("SCAN_MODE", "QR_CODE_MODE,PRODUCT_MODE");
            //customize the prompt message before scanning
            integrator.addExtra("PROMPT_MESSAGE", "Scanner Start!");
            integrator.initiateScan(IntentIntegrator.ALL_CODE_TYPES);

            // integrator.initiateScan();
            return true;
        }
        */

        return super.onOptionsItemSelected(item);
    }

    private boolean isFirstLaunch() {
        // Restore preferences
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean("isFirstLaunch", true);
    }

    // Store RegId and Email entered by User in SharedPref
    private void storeRegIdinSharedPref(Context context, String regId, String deviceID) {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(REG_ID,regId).apply();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(DEVICE_ID,deviceID).apply();
    }

    // AsyncTask to register Device in GCM Server
    private void registerInBackground(final String deviceID) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg;
                try {
                    if (gcmObj == null) {
                        gcmObj = GoogleCloudMessaging.getInstance(context);
                    }
                    regId = gcmObj.register("490198019902");
                    msg = "Registration ID :" + regId;

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (!TextUtils.isEmpty(regId)) {
                    storeRegIdinSharedPref(context, regId, deviceID);
                    String[] fetchInfo = {"insertUser", deviceID, regId};
                    FetchUpdatesTask fetchUpdatesTask = new FetchUpdatesTask();
                    fetchUpdatesTask.execute(fetchInfo);
                    //Toast.makeText(context, "Registered with GCM Server successfully.nn" + msg, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "Registered with GCM Server successfully.nn" + msg);
                } else {
                    //Toast.makeText(context, "Reg ID Creation Failed.nnEither you haven't enabled Internet or GCM server is busy right now. Make sure you enabled Internet and try registering again after some time." + msg, Toast.LENGTH_LONG).show();
                    Log.i(TAG, "Reg ID Creation Failed.nnEither you haven't enabled Internet or GCM server is busy right now. Make sure you enabled Internet and try registering again after some time." + msg);
                }
            }
        }.execute(null, null, null);
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        // When Play services not found in device
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                // Show Error dialog to install Play services
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(context, "This device doesn't support Play services, App will not work normally", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        } else {
            Toast.makeText(context, "This device supports Play services, App will work normally", Toast.LENGTH_LONG).show();
        }
        return true;
    }
/*
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //Checking if the item is in checked state or not, if not make it in checked state
        if (item.isChecked()) {
            item.setChecked(false);
        } else {
            item.setChecked(true);
        }

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        switch (id) {
            case R.id.nav_qrcode:
                Toast.makeText(getApplicationContext(), "Your QR Code", Toast.LENGTH_SHORT).show();
                FragmentQRCode fragment = new FragmentQRCode();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_qrcode, fragment);
                fragmentTransaction.commit();
                return true;
            default:
                Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                return true;
        }
    }*/
}
