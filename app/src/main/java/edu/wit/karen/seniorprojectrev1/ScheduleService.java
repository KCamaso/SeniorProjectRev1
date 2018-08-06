package edu.wit.karen.seniorprojectrev1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedList;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import java.util.ArrayList;
import java.util.Calendar;

import static edu.wit.karen.seniorprojectrev1.AlarmSend.USER_ID;

public class ScheduleService extends Service {

    public  static DynamoDBMapper dynamoDBMapper;
    public static String USER_ID = MainActivity.userId;
    ArrayList<TimerDO> schedule = new ArrayList<>();
    private AlarmManager alarmMgr;

    public ScheduleService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        setupDynamoDB();



        Calendar calendar = Calendar.getInstance();
        // Returns the day of week from Sunday (0) to Saturday (6)
        final int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;


        new ScheduleAsync(new OnTaskCompletedSchedule() {
            @Override
            public void onTaskCompleted(ArrayList<TimerDO> timerDOS) {
                for(TimerDO timer: timerDOS)
                {
                    if( timer.getDayOfWeek().toCharArray()[day] == '1')
                    {
                        schedule.add(timer);
                    }
                }
            }
        }).execute();

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        for(TimerDO sched : schedule)
        {
            int[] from = {sched.getFromHour().intValue(),sched.getFromMinute().intValue()};
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, from[0]);
            calendar.set(Calendar.MINUTE, from[1]);
            Bundle extras = new Bundle();
            extras.putString("medName", sched.getMedName());
            notificationIntent.putExtras(extras);
            alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }


        return Service.START_NOT_STICKY;
    }

    private void setupDynamoDB()
    {
        // Sets up DynamoDBClient
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

class ScheduleAsync extends AsyncTask<Void, Void, ArrayList<TimerDO>>
{
    private OnTaskCompletedSchedule listener;
    public ScheduleAsync(OnTaskCompletedSchedule listener)
    {
        this.listener = listener;
    }
    @Override
    protected void onPreExecute()
    {

    }
    @Override
    protected ArrayList<TimerDO> doInBackground(Void... voids) {
        ArrayList<TimerDO> returnList = new ArrayList<>();

        TimerDO template = new TimerDO();
        template.setUserId(USER_ID);


        DynamoDBScanExpression queryExpression;
        queryExpression = new DynamoDBScanExpression();
        PaginatedList<TimerDO> timerDOPaginatedList;


        timerDOPaginatedList = MedFrag.dynamoDBMapper.scan(TimerDO.class, queryExpression);

        for(TimerDO timer: timerDOPaginatedList)
        {
            returnList.add(timer);
            Log.e("ScheduleService","Timer ID: " + timer.getTimerId() + " ArraySize: " + returnList.size());


        }

        return returnList;
    }

    @Override
    protected void onPostExecute(ArrayList<TimerDO> timerDOS) {
        ArrayList<TimerDO> temp = new ArrayList<>();
        for(TimerDO timer: timerDOS)
        {
            temp.add(timer);
        }
        listener.onTaskCompleted(temp);
        super.onPostExecute(temp);

    }
}

interface OnTaskCompletedSchedule
{
    void onTaskCompleted(ArrayList<TimerDO> timerDOS);
}


