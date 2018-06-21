package edu.wit.karen.seniorprojectrev1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      /*  AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                Log.d("YourMainActivity", "AWSMobileClient is instantiated and you are connected to AWS!");
            }
        }).execute(); */

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment newFragment;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        newFragment = new HomeFrag();
        transaction.replace(R.id.placeholder_fragment, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();



    }


    @Override
    public void  onResume()
    {
        super.onResume();


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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

       Fragment newFragment;
       FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (id == R.id.nav_home) {
            newFragment = new HomeFrag();
            transaction.replace(R.id.placeholder_fragment, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        } else if (id == R.id.nav_alarm) {
            newFragment = new AlarmFrag();
            transaction.replace(R.id.placeholder_fragment, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();


        } else if (id == R.id.nav_meds) {
            newFragment = new MedFrag();
            transaction.replace(R.id.placeholder_fragment, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        } else if (id == R.id.nav_history) {

            newFragment = new HistoryFrag();
            transaction.replace(R.id.placeholder_fragment, newFragment);
            transaction.addToBackStack(null);
           transaction.commit();


        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_sign_out) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // Add the buttons
            builder.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    IdentityManager.getDefaultIdentityManager().signOut();

                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
            // Set other dialog properties
            builder.setMessage(R.string.signout);

            // Create the AlertDialog
            AlertDialog dialog = builder.create();
            dialog.show();




        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}

