package edu.wit.karen.seniorprojectrev1;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobile.auth.core.IdentityHandler;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedScanList;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amazonaws.mobileconnectors.pinpoint.analytics.AnalyticsEvent;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static edu.wit.karen.seniorprojectrev1.AlarmSend.USER_ID;

public class AlarmSend extends AppCompatActivity {

    public static PinpointManager pinpointManager;
    public  static DynamoDBMapper dynamoDBMapper;
    public static String USER_ID = MainActivity.userId;


    private double[] timeFrom;
    private double[] timeTo;
    private boolean[] weekDayChecks;
    private double alarmId;
    private boolean active;
    private boolean isWindow;
    private List<String> medList = new ArrayList<String>();
    private ArrayAdapter<String> adapter;

    public EditText diaMinute;
    public EditText diaHour;
    public EditText diaHour2;
    public EditText diaMinute2;
    public CheckBox[] weekBox;
    public Switch switchWindow;
    public Switch switchActive;
    public Spinner medSpinner;
    public Button deleteButton;


    public TimerDO timerItem = new TimerDO();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = this.getIntent().getExtras();
        setContentView(R.layout.activity_alarm_send);
        Toolbar toolbar = findViewById(R.id.toolbar_alarm);
        setSupportActionBar(toolbar);

        FloatingActionButton fab =  findViewById(R.id.fab);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        diaMinute = findViewById(R.id.diaMinute);
        diaHour = findViewById(R.id.diaHour);
        diaMinute2 = findViewById(R.id.diaMinute2);
        diaHour2 = findViewById(R.id.diaHour2);
        weekBox = new CheckBox[] {findViewById(R.id.checkBoxDiaSu), findViewById(R.id.checkBoxDiaM), findViewById(R.id.checkBoxDiaTu),findViewById(R.id.checkBoxDiaW),findViewById(R.id.checkBoxDiaTh),findViewById(R.id.checkBoxDiaF),findViewById(R.id.checkBoxDiaSa)};
        switchWindow = findViewById(R.id.switchWindow);
        switchActive = findViewById(R.id.switchActive1);
        medSpinner = findViewById(R.id.medSpinner);
        deleteButton = findViewById(R.id.buttonDiaDeleteAlarm);

        // Setup Medication List here TODO
        //medSpinner.setAdapter();
        Log.e("AlarmActivity", "USER ID IN ALARM SEND IS"+ USER_ID);


        if(!(extras == null)) {
            // Editing, update the item.
            Log.e("MyAlarmActivity", "SAVED INSTANCE ISN'T NULL");
            unpack(extras);
            Log.e("MyAlarmActivity", "ALARM ID: " + alarmId);
            Log.e("MyAlarmActivity", "FROM " + timeFrom[0] + ":" + timeFrom[1]);
            Log.e("MyAlarmActivity", "FROM " + timeTo[0] + ":" + timeTo[1]);
            setupDynamoDB();


            deleteButton.setVisibility(View.VISIBLE);
            // Setting the fields, they haven't made a final selection.
            timerItem.setUserId(USER_ID);
            timerItem.setTimerId(Double.valueOf(alarmId));
            timerItem.setFromHour(timeFrom[0]);
            timerItem.setFromMinute(timeFrom[1]);
            timerItem.setToHour(timeTo[0]);
            timerItem.setToMinute(timeTo[1]);
            timerItem.setActive(active);
            timerItem.setIsWindow(isWindow);


            timerItem.setDayOfWeek( booleanToString(weekDayChecks) );

            setValues(timerItem);

            new MedNameAsync(new OnTaskCompletedMedNames() {
                @Override
                public void onTaskCompleted(ArrayList<MedicationDO> medicationDOS) {
                    for(MedicationDO med : medicationDOS)
                    {
                        medList.add(med.getName());
                    }
                    adapter = new ArrayAdapter<String>(AlarmSend.this, android.R.layout.simple_spinner_item,medList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    medSpinner.setAdapter(adapter);
                }
            }).execute();

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    timerItem = getValues();
                    if (valueCheck(timerItem))
                    {
                        timerItem.setMedName(medSpinner.getSelectedItem().toString());
                        // Content Checked, passed, updating
                        new Thread((new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                dynamoDBMapper.save(timerItem);

                            }
                        })).start();

                        final AnalyticsEvent editEvent = pinpointManager.getAnalyticsClient().createEvent("EditAlarm")
                                .withAttribute("AlarmId", timerItem.getTimerId().toString());

                        pinpointManager.getAnalyticsClient().recordEvent(editEvent);
                        Intent sendBack = new Intent(AlarmSend.this, MainActivity.class);
                        startActivity(sendBack);
                    }
                    else
                    {
                        Snackbar.make(view, "Error: Please make sure the hours are within 0 and 23, and hours between 0 and 59.", Snackbar.LENGTH_LONG)
                                .show();
                    }

                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timerItem = getValues();
                    new Thread((new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            dynamoDBMapper.delete(timerItem);

                        }
                    })).start();

                    final AnalyticsEvent editEvent = pinpointManager.getAnalyticsClient().createEvent("DeleteAlarm")
                            .withAttribute("AlarmId", timerItem.getTimerId().toString());

                    pinpointManager.getAnalyticsClient().recordEvent(editEvent);
                    Intent sendBack = new Intent(AlarmSend.this, MainActivity.class);
                    startActivity(sendBack);
                }
            });




        }
        else
        {
            //New item, make a new alarmId here

            Log.e("MyAlarmActivity", "SAVED INSTANCE IS NULL");
            Log.e("MyAlarmActivity", "ALARM ID: " + alarmId);
            setupDynamoDB();
            deleteButton.setVisibility(View.GONE);

            new MedNameAsync(new OnTaskCompletedMedNames() {
                @Override
                public void onTaskCompleted(ArrayList<MedicationDO> MedDOS) {
                    for(MedicationDO meds : MedDOS)
                    {
                        medList.add(meds.getName());
                        Log.e("MyAlarmActivity", "ADAPT LIST SIZE: " + medList.size());
                    }

                    adapter = new ArrayAdapter<String>(AlarmSend.this, android.R.layout.simple_spinner_item,medList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    medSpinner.setAdapter(adapter);


                }
            }).execute();




            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    timerItem = getValues();
                    timerItem.setTimerId(Double.parseDouble(new Long(Calendar.getInstance().getTimeInMillis()).toString()));
                    if (valueCheck(timerItem))
                    {
                        timerItem.setMedName(medSpinner.getSelectedItem().toString());
                        // Content Checked, passed, updating
                        new Thread((new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                dynamoDBMapper.save(timerItem);

                            }
                        })).start();

                        final AnalyticsEvent editEvent = pinpointManager.getAnalyticsClient().createEvent("NewAlarm")
                                .withAttribute("AlarmId", timerItem.getTimerId().toString());

                        pinpointManager.getAnalyticsClient().recordEvent(editEvent);
                        Snackbar.make(view, "Error: Please make sure the hours are within 0 and 23, and hours between 0 and 59.", Snackbar.LENGTH_LONG)
                                .show();

                        Intent sendBack = new Intent(AlarmSend.this, MainActivity.class);
                        startActivity(sendBack);

                    }
                    else
                    {
                        Snackbar.make(view, "Error: Please make sure the hours are within 0 and 23, and hours between 0 and 59.", Snackbar.LENGTH_LONG)
                                .show();
                    }

                }
            });




        }
    }



    public boolean valueCheck(TimerDO checkable)
    {
        boolean flag = false;

        if(
                checkable.getFromHour().intValue() < 24 && checkable.getFromMinute().intValue() < 60
                && checkable.getToHour().intValue() < 24 && checkable.getToMinute().intValue() < 60)
        {
            flag = true;
        }



        return flag;
    }

    public void setValues(TimerDO info)
    {
        diaHour.setText( String.valueOf(info.getFromHour().intValue()));
        diaMinute.setText(String.valueOf(info.getFromMinute().intValue()));
        diaHour2.setText(String.valueOf(info.getToHour().intValue()));
        diaMinute2.setText(String.valueOf(info.getToMinute().intValue()));
        switchActive.setChecked(info.getActive());
        switchWindow.setChecked(info.getIsWindow());
        stringToCheckbox(info.getDayOfWeek());

    }


    public TimerDO getValues()
    {
        TimerDO toSend = new TimerDO();
        toSend.setUserId(USER_ID);
        toSend.setFromHour( Double.parseDouble(diaHour.getText().toString()));
        toSend.setFromMinute( Double.parseDouble(diaMinute.getText().toString()));
        toSend.setToHour( Double.parseDouble(diaHour2.getText().toString()));
        toSend.setToMinute( Double.parseDouble(diaMinute2.getText().toString()));
        toSend.setIsWindow(switchWindow.isChecked());
        toSend.setActive(switchActive.isChecked());
        toSend.setDayOfWeek( checkBoxToString() );
        toSend.setTimerId(alarmId);
        toSend.setMedName(medSpinner.getSelectedItem().toString());




        return toSend;
    }

    public String checkBoxToString()
    {
        StringBuilder temp = new StringBuilder();

        for(int i = 0; i < 7; i++)
        {
            temp.append(weekBox[i].isChecked() ? "1" : "0");
        }
        return temp.toString();
    }

    public void stringToCheckbox(String string)
    {
        for(int i = 0; i < 7; i++)
        {
            if(string.charAt(i) == '0')
            {
                weekBox[i].setChecked(false);
            }
            else
            {
                weekBox[i].setChecked(true);
            }
        }
    }

    public String booleanToString(boolean[] week)
    {
        StringBuilder temp = new StringBuilder();

        for(int i = 0; i < 7; i++)
        {
            temp.append(week[i] ? "1" : "0");
        }
        return temp.toString();
    }


    public void unpack(Bundle sentData)
    {

       timeFrom = sentData.getDoubleArray("timeFrom");
       timeTo = sentData.getDoubleArray("timeTo");
       alarmId =  sentData.getDouble("alarmId");
       weekDayChecks = sentData.getBooleanArray("weekToConvert");
       active = sentData.getBoolean("active");
       isWindow = sentData.getBoolean("isWindow");


    }


    private void setupDynamoDB()
    {
        // Sets up DynamoDBClient
        AWSProvider.initialize(MainActivity.getAppContext());
        AWSMobileClient.getInstance().initialize(AlarmSend.this).execute();
        AWSCredentialsProvider cp = AWSMobileClient.getInstance().getCredentialsProvider();
        AWSConfiguration config = AWSMobileClient.getInstance().getConfiguration();
        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(cp);

        this.dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(config)
                .build();

        PinpointConfiguration pinpointConfig = new PinpointConfiguration( AlarmSend.this, cp,config);
        pinpointManager = new PinpointManager(pinpointConfig);
        pinpointManager.getSessionClient().startSession();
        pinpointManager.getAnalyticsClient().submitEvents();

        // End of DynamoDB Setup, can now make calls using dynamoDBMapper
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        pinpointManager.getSessionClient().stopSession();
        pinpointManager.getAnalyticsClient().submitEvents();
    }

}

class MedNameAsync extends AsyncTask<Void, Void, ArrayList<MedicationDO>>
{
    private OnTaskCompletedMedNames listener;
    public MedNameAsync(OnTaskCompletedMedNames listener)
    {
        this.listener = listener;
    }
    @Override
    protected void onPreExecute()
    {

    }
    @Override
    protected ArrayList<MedicationDO> doInBackground(Void... voids) {
        ArrayList<MedicationDO> returnList = new ArrayList<>();

        MedicationDO template = new MedicationDO();
        template.setUserId(USER_ID);


        DynamoDBScanExpression queryExpression;
        queryExpression = new DynamoDBScanExpression();


        PaginatedScanList<MedicationDO> medNameList = AlarmFrag.dynamoDBMapper.scan(MedicationDO.class, queryExpression);

        for(MedicationDO name: medNameList)
        {
            returnList.add(name);
            Log.e("MyAlarmActivity","Med Name: " + name.getName() + " ArraySize: " + returnList.size());


        }

        return returnList;
    }

    @Override
    protected void onPostExecute(ArrayList<MedicationDO> medNameDO) {
        ArrayList<MedicationDO> temp = new ArrayList<>();
        for(MedicationDO name: medNameDO)
        {
            temp.add(name);
            Log.e("MyAlarmActivity","Med Name: " + name.getName() + " ArraySize: " + temp.size());
        }
        listener.onTaskCompleted(temp);
        super.onPostExecute(temp);

    }
}

interface OnTaskCompletedMedNames
{
    void onTaskCompleted(ArrayList<MedicationDO> medicationDOS);
}