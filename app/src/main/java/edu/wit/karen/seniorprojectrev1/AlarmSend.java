package edu.wit.karen.seniorprojectrev1;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amazonaws.mobileconnectors.pinpoint.analytics.AnalyticsEvent;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class AlarmSend extends AppCompatActivity {

    public static PinpointManager pinpointManager;
    public  static DynamoDBMapper dynamoDBMapper;
    public static CognitoCachingCredentialsProvider cogCredentialsProvider;
    public static IdentityManager identityManager;
    public static String USER_ID;
    private static Context context;

    private double[] timeFrom;
    private double[] timeTo;
    private boolean[] weekDayChecks;
    private int alarmId;

    public EditText diaMinute;
    public EditText diaHour;
    public EditText diaHour2;
    public EditText diaMinute2;
    public CheckBox[] weekBox;
    public Switch switchWindow;
    public Switch switchActive;
    public Spinner medSpinner;


    public TimerDO timerItem = new TimerDO();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_send);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_alarm);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        diaMinute = findViewById(R.id.diaMinute);
        diaHour = findViewById(R.id.diaHour);
        diaMinute2 = findViewById(R.id.diaMinute2);
        diaHour2 = findViewById(R.id.diaHour2);
        weekBox = new CheckBox[] {findViewById(R.id.checkBoxDiaSu), findViewById(R.id.checkBoxDiaM), findViewById(R.id.checkBoxDiaTu),findViewById(R.id.checkBoxDiaW),findViewById(R.id.checkBoxDiaTh),findViewById(R.id.checkBoxDiaF),findViewById(R.id.checkBoxDiaSa)};
        switchWindow = findViewById(R.id.switchWindow);
        switchActive = findViewById(R.id.switchActive);
        medSpinner = findViewById(R.id.medSpinner);

        // Setup Medication List here TODO
        //medSpinner.setAdapter();


        if(! (savedInstanceState == null)) {
            // Editing, update the item.
            unpack(savedInstanceState);
            setupDynamoDB();



            // Setting the fields, they haven't made a final selection.
            timerItem.setUserId(USER_ID);
            timerItem.setFromHour(timeFrom[0]);
            timerItem.setFromMinute(timeFrom[1]);
            timerItem.setTimerId(Double.valueOf(alarmId));
            timerItem.setToHour(timeTo[0]);
            timerItem.setToMinute(timeTo[1]);
            timerItem.setDayOfWeek(weekConvert(weekDayChecks));

            setValues(timerItem);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    timerItem = getValues();
                    if (valueCheck(timerItem))
                    {
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




        }
        else
        {
            //New item, make a new alarmId here
            setupDynamoDB();




            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    timerItem.setUserId(USER_ID);
                    timerItem = getValues();
                    timerItem.setTimerId(Double.parseDouble(new Long(Calendar.getInstance().getTimeInMillis()).toString()));
                    if (valueCheck(timerItem))
                    {
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
                checkable.getFromHour().intValue() < 23 && checkable.getFromMinute().intValue() < 60
                && checkable.getToHour().intValue() < 23 && checkable.getToMinute().intValue() < 60)
        {
            flag = true;
        }



        return flag;
    }

    public void setValues(TimerDO info)
    {
        diaHour.setText( info.getFromHour().intValue());
        diaMinute.setText(info.getFromMinute().intValue());
        diaHour2.setText(info.getToHour().intValue());
        diaMinute2.setText(info.getToMinute().intValue());
        switchActive.setChecked(info.getActive());
        switchWindow.setChecked(info.getIsWindow());

        Iterator<Double> daysIterator = info.getDayOfWeek().iterator();

        int w = 0;
        while (daysIterator.hasNext())
        {
            Double number = daysIterator.next();
            if(number.intValue() == 1.0 )
            {
                weekBox[w].setChecked(true);
            }
            else
            {
                weekBox[w].setChecked(false);
            }
            w++;
        }

    }


    public TimerDO getValues()
    {
        TimerDO toSend = new TimerDO();
        toSend.setFromHour( Double.parseDouble(diaHour.getText().toString()));
        toSend.setFromMinute( Double.parseDouble(diaMinute.getText().toString()));
        toSend.setToHour( Double.parseDouble(diaHour2.getText().toString()));
        toSend.setToMinute( Double.parseDouble(diaMinute2.getText().toString()));
        toSend.setIsWindow(switchWindow.isChecked());
        toSend.setActive(switchActive.isChecked());

        HashSet<String> tempString = new HashSet<>();
        if(medSpinner.getSelectedItem().toString() != null)
        {
            tempString.add(medSpinner.getSelectedItem().toString());
        }

        toSend.setMedName(tempString);


        return toSend;
    }

    public void unpack(Bundle sentData)
    {

       timeFrom = sentData.getDoubleArray("timeFrom");
       timeTo = sentData.getDoubleArray("timeTo");
       weekDayChecks =  sentData.getBooleanArray("weekDay");
       alarmId = sentData.getInt("alarmId");
    }

    public Set<Double> weekConvert(boolean[] boolList)
    {
      Set<Double> doubList = new HashSet<Double>();

      for(int i = boolList.length; 0 < boolList.length; i--)
      {
          if (boolList[i])
          {
              doubList.add(1.0d);
          }
          else
          {
              doubList.add(0.0d);
          }

      }

      return doubList;
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

        IdentityManager.getDefaultIdentityManager().getUserID(new IdentityHandler() {
            @Override
            public void onIdentityId(String identityId) {
                USER_ID = identityId;
            }

            @Override
            public void handleError(Exception exception) {
                Log.e("MyMainActivity", exception.toString());
            }
        });

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
