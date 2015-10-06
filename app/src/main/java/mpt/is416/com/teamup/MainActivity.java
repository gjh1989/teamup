package mpt.is416.com.teamup;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //Defining Variables
    private Toolbar toolbar;
    private NavigationView navigationView;
    private final String ANDROID_ID = "android_id";
    private final String PREFS_NAME = "preferences";
    private final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check first launch for QR code generation
        if (isFirstLaunch()) {
            // Get androidId
            final String androidId = Secure.getString(
                    getApplicationContext().getContentResolver(), Secure.ANDROID_ID);
            (getSharedPreferences(PREFS_NAME, 0)).getString(ANDROID_ID, androidId);
            //TODO: Implement send androidId to database...

            // Flag that first launch completed
            (getSharedPreferences(PREFS_NAME, 0)).edit().putBoolean("isFirstLaunch", false)
                    .commit();
        }

        //Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //default contentView
        FragmentQRCode fragment = new FragmentQRCode();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contentMain, fragment);
        fragmentTransaction.commit();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                // Handle navigation view item clicks here.
                int id = item.getItemId();
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

                FragmentQRCode fragment;

                switch (id) {
                    case R.id.nav_qrcode:
                        Toast.makeText(getApplicationContext(), "Your QR Code", Toast.LENGTH_SHORT)
                                .show();
                        fragment = new FragmentQRCode();
                        item.setChecked(true);
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong",
                                Toast.LENGTH_SHORT).show();
                        fragment = new FragmentQRCode();
                        item.setChecked(true);
                        break;
                }

                FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                        .beginTransaction();
                fragmentTransaction.replace(R.id.contentMain, fragment);
                fragmentTransaction.commit();
                return true;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
        if (id == R.id.action_add) {
            Toast.makeText(getApplicationContext(), "Add Group", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, AddGroupMember.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_scan) {
            Intent intent = new Intent(this, ScannerActivity.class);
            startActivity(intent);
            startActivityForResult(intent, 1);
            //IntentIntegrator integrator = new IntentIntegrator(this);
            /*
            integrator.addExtra("SCAN_WIDTH", 640);
            integrator.addExtra("SCAN_HEIGHT", 640);
            integrator.addExtra("SCAN_MODE", "QR_CODE_MODE,PRODUCT_MODE");
            //customize the prompt message before scanning
            integrator.addExtra("PROMPT_MESSAGE", "Scanner Start!");
            integrator.initiateScan(IntentIntegrator.ALL_CODE_TYPES);
            */
            // integrator.initiateScan();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("content");
                TextView formatTxt = (TextView) findViewById(R.id.textView);
                formatTxt.setText("Content: " + result);
            }
        }
    }

    private boolean isFirstLaunch() {
        // Restore preferences
        return (getSharedPreferences(PREFS_NAME, 0)).getBoolean("isFirstLaunch", true);
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
