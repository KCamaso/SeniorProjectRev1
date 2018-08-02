package edu.wit.karen.seniorprojectrev1;

import android.content.Context;
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
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

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
    private String notify;


    public MedicationDO medItem = new MedicationDO();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_send);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_med);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Medication Creation");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        diaMedName = findViewById(R.id.diaMedName);
        diaMedDesc = findViewById(R.id.diaMedDesc);
        diaMedCurrent = findViewById(R.id.diaMedCurrent);
        diaMedMax = findViewById(R.id.diaMedMax);
        infiniteDoseCheck = findViewById(R.id.infiniteDoseCheck);
        diaMedNotifySwitch = findViewById(R.id.diaMedNotifySwitch);
        diaMedNotify = findViewById(R.id.diaMedNotify);


        if(!(savedInstanceState == null))
        { // Editing, update the item.
            unpack(savedInstanceState);
            setupDynamoDB();

            medItem.setUserId(USER_ID);
            medItem.setMedId(Double.valueOf(medId));
            setValues(medItem);
        }
        else
        {

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
        notify = sentData.getString("notify");
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

    private void setupDynamoDB()
    {
        // Sets up DynamoDBClient
        AWSProvider.initialize(MainActivity.getAppContext());
        AWSMobileClient.getInstance().initialize(MainActivity.getAppContext()).execute();
        AWSCredentialsProvider cp = AWSMobileClient.getInstance().getCredentialsProvider();
        AWSConfiguration config = AWSMobileClient.getInstance().getConfiguration();
        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(cp);

        this.dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(config)
                .build();



        // End of DynamoDB Setup, can now make calls using dynamoDBMapper
    }


}
