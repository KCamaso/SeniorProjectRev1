package edu.wit.karen.seniorprojectrev1;

import android.app.Activity;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.amazonaws.mobile.auth.core.IdentityHandler;
import com.amazonaws.mobile.auth.ui.SignInUI;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobileconnectors.pinpoint.analytics.AnalyticsClient;
import com.amazonaws.mobileconnectors.pinpoint.analytics.AnalyticsEvent;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;

import java.text.SimpleDateFormat;


public class AuthenticatorActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticator);

        // Add a call to initialize AWSMobileClient
        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                SignInUI signin = (SignInUI) AWSMobileClient.getInstance().getClient(AuthenticatorActivity.this, SignInUI.class);

                new Thread(new Runnable() {

                    String userId;
                    @Override
                    public void run() {
                        MainActivity.identityManager.getDefaultIdentityManager().getUserID(new IdentityHandler() {
                            @Override
                            public void onIdentityId(String identityId) {
                                userId = identityId;
                            }

                            @Override
                            public void handleError(Exception exception) {
                                Log.e("MyMainApplication", "THERE'S NO USER ID.");
                            }
                        });

                        Log.e("MyMainApplication", "THERE'S NO USER ID, IT'S: " + userId);

                    }
                }).start();

                signin.login(AuthenticatorActivity.this, MainActivity.class).execute();


            }
        }).execute();
    }

}
