package edu.wit.karen.seniorprojectrev1;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCognitoIdentityProvider;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.auth.CognitoCredentialsProvider;
import com.amazonaws.mobile.auth.core.IdentityHandler;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.auth.core.SignInStateChangeListener;
import com.amazonaws.mobile.auth.ui.SignInUI;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.tokens.CognitoIdToken;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.util.CognitoIdentityProviderClientConfig;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.pinpoint.analytics.AnalyticsClient;
import com.amazonaws.mobileconnectors.pinpoint.analytics.AnalyticsEvent;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static PinpointManager pinpointManager;
    public  static DynamoDBMapper dynamoDBMapper;
    public static CognitoCachingCredentialsProvider cogCredentialsProvider;
    public static IdentityManager identityManager;
    public static String userId;
    private static Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.context = getApplicationContext();



        // Initializes AWS Client
        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                Log.d("YourMainActivity", "AWSMobileClient is instantiated and you are connected to AWS!");
            }
        }).execute();


        // Initialize the Amazon Cognito credentials provider
        cogCredentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-east-1:1be0f888-8198-4037-859e-b262fa99fb32", // Identity pool ID
                Regions.US_EAST_1 // Region
        );
        AWSConfiguration configuration = AWSMobileClient.getInstance().getConfiguration();


        // Add code to instantiate a AmazonDynamoDBClient
        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(cogCredentialsProvider);

        AWSConfiguration awsConfiguration = new AWSConfiguration(this);

        // Sets up Pinpoint Manager (Analytics)
        if (IdentityManager.getDefaultIdentityManager() == null) {
            identityManager = new IdentityManager(getApplicationContext(), awsConfiguration);
            IdentityManager.setDefaultIdentityManager(identityManager);
        }

        try {
            final PinpointConfiguration config =
                    new PinpointConfiguration(this,
                            IdentityManager.getDefaultIdentityManager().getCredentialsProvider(),
                            awsConfiguration);
            MainActivity.pinpointManager = new PinpointManager(config);
        } catch (final AmazonClientException ex) {
            Log.e("MyMainActivity", "Unable to initialize PinpointManager. " + ex.getMessage(), ex);
        }
        Log.e("MyMainActivity", "PINPOINT MANAGER INITIALIZED");





        this.dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(configuration)
                .build();
        IdentityManager.getDefaultIdentityManager().getUserID(new IdentityHandler() {
            @Override
            public void onIdentityId(String identityId) {
                userId = identityId;
            }

            @Override
            public void handleError(Exception exception) {
                Log.e("MyMainActivity", exception.toString());
            }
        });


        Log.e("MyMainActivity", "DYNAMODB CLIENT ACTIVATED");
        Log.e("MyMainActivity", "THE USER ID IN MAIN ACTIVITY IS:" + userId);




        // Sets up drawer and toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        //sets up fragment in view
        Fragment newFragment;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //Places HomeFrag
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

    public static Context getAppContext() {
        return MainActivity.context;
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
            AWSMobileClient.getInstance().initialize(this).execute();

            // Add the buttons
            builder.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    IdentityManager.getDefaultIdentityManager().signOut();
                    // Sign-in listener


                    IdentityManager.getDefaultIdentityManager().addSignInStateChangeListener(new SignInStateChangeListener() {
                        @Override
                        public void onUserSignedIn() {
                            Log.d("TimeRx", "User Signed In");
                        }

                        // Sign-out listener
                        @Override
                        public void onUserSignedOut() {

                            Log.d("TimeRx", "User Signed Out");
                            showSignIn();
                        }
                    });

                    showSignIn();

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

    private void showSignIn() {

        Log.d("TimeRx", "showSignIn");

        SignInUI signin = (SignInUI) AWSMobileClient.getInstance().getClient(this, SignInUI.class);
        signin.login(this, AuthenticatorActivity.class).execute();
    }

}

