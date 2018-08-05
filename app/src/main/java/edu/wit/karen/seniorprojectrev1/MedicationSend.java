package edu.wit.karen.seniorprojectrev1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobile.auth.core.IdentityHandler;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amazonaws.mobileconnectors.pinpoint.analytics.AnalyticsEvent;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import java.util.Calendar;

public class MedicationSend extends AppCompatActivity {

    public static PinpointManager pinpointManager;
    public  static DynamoDBMapper dynamoDBMapper;
    public static CognitoCachingCredentialsProvider cogCredentialsProvider;
    public static IdentityManager identityManager;
    public static String USER_ID = MainActivity.userId;
    private static Context context;

    EditText diaMedName;
    EditText diaMedDesc;
    EditText diaMedCurrent;
    EditText diaMedMax;
    Switch infiniteDoseCheck;
    Switch diaMedNotifySwitch;
    EditText diaMedNotify;

    private String userId;
    private String currentNum;
    private Boolean infinite;
    private String info;
    private String maxNum;
    private Double medId;
    private String name;
    private boolean notify;
    private String notifyNum;

    public MedicationDO medItem = new MedicationDO();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = this.getIntent().getExtras();
        setContentView(R.layout.activity_medication_send);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_med);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Medication Creation");


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupDynamoDB();
        diaMedName = findViewById(R.id.diaMedName);
        diaMedDesc = findViewById(R.id.diaMedDesc);
        diaMedCurrent = findViewById(R.id.diaMedCurrent);
        diaMedMax = findViewById(R.id.diaMedMax);
        infiniteDoseCheck = findViewById(R.id.infiniteDoseCheck);
        diaMedNotifySwitch = findViewById(R.id.diaMedNotifySwitch);
        diaMedNotify = findViewById(R.id.diaMedNotify);
        Log.e("MedicationActivity", "USER ID IN MEDICATION SEND IS"+ USER_ID);


        if(!(extras == null))
        { // Editing, update the item.
            unpack(extras);


            medItem.setUserId(USER_ID);
            medItem.setMedId(Double.valueOf(medId));
            medItem.setCurrentNum(currentNum);
            medItem.setInfinite(infinite);
            medItem.setInfo(info);
            medItem.setMaxNum(maxNum);
            medItem.setName(name);
            medItem.setNotify(notify);
            medItem.setNotifyNum(notifyNum);

            setValues(medItem);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.medSendFab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    medItem = getValues();
                    medItem.setMedId(medId);
                    if(!medItem.getName().isEmpty())
                    {
                        if (valueCheck(medItem))
                        {
                            // Content Checked, passed, updating
                            new Thread((new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    dynamoDBMapper.save(medItem);

                                }
                            })).start();

                            final AnalyticsEvent editEvent = pinpointManager.getAnalyticsClient().createEvent("EditMedication")
                                    .withAttribute("MedId", medItem.getMedId().toString());

                            pinpointManager.getAnalyticsClient().recordEvent(editEvent);
                            Intent sendBack = new Intent(MedicationSend.this, MainActivity.class);
                            startActivity(sendBack);
                        }
                        else
                        {
                            Snackbar.make(view, "Error: Please make sure the current number and notify number aren't larger than the maximum.", Snackbar.LENGTH_LONG)
                                    .show();
                        }
                    }
                    else
                    {
                        Snackbar.make(view, "Error: Medication name cannot be empty.", Snackbar.LENGTH_LONG)
                                .show();
                    }


                }
            });
        }
        else
        {
            // No preveious info, new Medication

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.medSendFab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    medItem = getValues();
                    medItem.setMedId((Double.parseDouble(new Long(Calendar.getInstance().getTimeInMillis()).toString())));
                    if (medItem.getName().isEmpty())
                    {
                        if (valueCheck(medItem))
                        {
                            // Content Checked, passed, updating
                            new Thread((new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    dynamoDBMapper.save(medItem);

                                }
                            })).start();

                            final AnalyticsEvent editEvent = pinpointManager.getAnalyticsClient().createEvent("AddMedication")
                                    .withAttribute("MedId", medItem.getMedId().toString());

                            pinpointManager.getAnalyticsClient().recordEvent(editEvent);
                            Intent sendBack = new Intent(MedicationSend.this, MainActivity.class);
                            startActivity(sendBack);
                        }
                        else
                        {
                            Snackbar.make(view, "Error: Please make sure the current number and notify number aren't larger than the maximum.", Snackbar.LENGTH_LONG)
                                    .show();
                        }
                    }
                }
            });
        }
    }

    public void unpack(Bundle sentData)
    {
        name = sentData.getString("name");
        info = sentData.getString("desc");
        userId = sentData.getString("userId");
        currentNum = sentData.getString("currentNumber");
        medId = sentData.getDouble("medId");
        maxNum = sentData.getString("bundleMedMax");
        notify = sentData.getBoolean("notify");
        notifyNum = sentData.getString("notifyNumber");
        infinite = sentData.getBoolean("infinite");

    }

    public void setValues(MedicationDO medItem)
    {
        diaMedName.setText(medItem.getName());
        diaMedDesc.setText(medItem.getInfo());
        diaMedCurrent.setText(medItem.getCurrentNum());
        diaMedMax.setText(medItem.getMaxNum());
        infiniteDoseCheck.setChecked(medItem.getInfinite());
        diaMedNotify.setText( medItem.getNotifyNum());
        diaMedNotifySwitch.setChecked(medItem.getNotify());
    }

    public boolean valueCheck(MedicationDO checkable)
    {
        boolean flag = true;
        int currentNum = Integer.parseInt(checkable.getCurrentNum());
        int notifyNumb = Integer.parseInt(checkable.getNotifyNum());
        int maxNum = Integer.parseInt(checkable.getMaxNum());
        if( currentNum > maxNum || notifyNumb > maxNum || maxNum == 0 || notifyNumb == 0)
        {
            flag = false;
        }

        return flag;
    }

    public MedicationDO getValues()
    {
        MedicationDO temp = new MedicationDO();
        temp.setUserId(USER_ID);
        temp.setCurrentNum( diaMedCurrent.getText().toString());
        temp.setInfinite(infiniteDoseCheck.isChecked());
        temp.setInfo(diaMedDesc.getText().toString());
        temp.setMaxNum(diaMedMax.getText().toString());
        temp.setName(diaMedName.getText().toString());
        temp.setNotify(diaMedNotifySwitch.isChecked());
        temp.setNotifyNum(diaMedNotify.getText().toString());
        return temp;
    }



    private void setupDynamoDB()
    {
        // Sets up DynamoDBClient
        AWSProvider.initialize(MainActivity.getAppContext());
        AWSMobileClient.getInstance().initialize(MedicationSend.this).execute();
        AWSCredentialsProvider cp = AWSMobileClient.getInstance().getCredentialsProvider();
        AWSConfiguration config = AWSMobileClient.getInstance().getConfiguration();
        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(cp);

        this.dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(config)
                .build();

        PinpointConfiguration pinpointConfig = new PinpointConfiguration( MedicationSend.this, cp,config);
        pinpointManager = new PinpointManager(pinpointConfig);
        pinpointManager.getSessionClient().startSession();
        pinpointManager.getAnalyticsClient().submitEvents();

        Log.e("MyMedication", "DYNAMODB SETUP");

        // End of DynamoDB Setup, can now make calls using dynamoDBMapper
    }


}
