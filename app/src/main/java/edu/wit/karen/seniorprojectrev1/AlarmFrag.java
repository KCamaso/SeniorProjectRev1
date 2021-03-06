package edu.wit.karen.seniorprojectrev1;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.amazonaws.auth.AWSCognitoIdentityProvider;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSIdentityProvider;
import com.amazonaws.mobile.auth.core.IdentityHandler;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.tokens.CognitoIdToken;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedList;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedScanList;
import com.amazonaws.mobileconnectors.pinpoint.analytics.AnalyticsClient;
import com.amazonaws.mobileconnectors.pinpoint.analytics.AnalyticsEvent;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amazonaws.mobileconnectors.pinpoint.targeting.notification.AppLevelOptOutProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.util.Tables;
import com.google.gson.internal.Primitives;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;

import static edu.wit.karen.seniorprojectrev1.AlarmSend.USER_ID;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AlarmFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AlarmFrag#newInstance} factory method to
 * create an instance of this fragment.
 */

public class AlarmFrag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String USER_ID = MainActivity.userId;




    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    public static  DynamoDBMapper dynamoDBMapper;

    FloatingActionButton fab;
   public ArrayList<TimerDO> adaptList = new ArrayList<>();
   public RecyclerView recycList;
   public RecyclerView.LayoutManager mLayoutManager;
   public RecyclerView.Adapter mAdapter;



    public AlarmFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AlarmFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static AlarmFrag newInstance(String param1, String param2) {
        AlarmFrag fragment = new AlarmFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if(container != null)
        {
            container.clearDisappearingChildren();
        }

        final View view = inflater.inflate(R.layout.alarm_main, container, false);
        fab = view.findViewById(R.id.fab_alarm);

        Log.e("MyAlarmActivity", "ALARM USER ID: " + USER_ID);

        setupDynamoDB();



        Log.e("MyAlarmActivity", "Before async, capacity: " + adaptList.size());


        new AlarmAsync(new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(ArrayList<TimerDO> timerDOS) {
                recycList = view.findViewById(R.id.alarmRecycler);

                for(TimerDO timers: timerDOS)
                {
                    adaptList.add(timers);
                    Log.e("MyAlarmActivity", "ADAPT LIST SIZE: " + adaptList.size());
                }
                mLayoutManager = new LinearLayoutManager(getContext());
                recycList.setLayoutManager(mLayoutManager);
                Log.e("MyAlarmActivity", "After async, capacity: " + adaptList.size());
                mAdapter = new AlarmAdapter(adaptList);
                Log.e("MyAlarmActivity", "SETTING ADAPTER");
                recycList.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

            }
        }).execute();


        if(fab != null)
        {
            fab.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {

                    Intent sendToAlarm = new Intent(getContext(), AlarmSend.class);
                    startActivity(sendToAlarm);
                }
            });
        }







        return view;

    }



    private void setupDynamoDB()
    {
        // Sets up DynamoDBClient
        AWSProvider.initialize(getContext());
        AWSMobileClient.getInstance().initialize(getContext()).execute();
        AWSCredentialsProvider cp = AWSMobileClient.getInstance().getCredentialsProvider();
        AWSConfiguration config = AWSMobileClient.getInstance().getConfiguration();
        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(cp);

        this.dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(config)
                .build();

        // End of DynamoDB Setup, can now make calls using dynamoDBMapper
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}


// ASync Task in order to retrieve the alarm results from AWS
class AlarmAsync extends AsyncTask<Void, Void, ArrayList<TimerDO>>
{
    private OnTaskCompleted listener;
    public AlarmAsync(OnTaskCompleted listener)
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
        template.setTimerId(1.0);

        // The actual query setup.
        DynamoDBScanExpression queryExpression;
        queryExpression = new DynamoDBScanExpression();


        // Saving as Paginated list, converting to normal list.
        PaginatedScanList<TimerDO> alarmList = AlarmFrag.dynamoDBMapper.scan(TimerDO.class, queryExpression);

        for(TimerDO timer: alarmList)
        {
            returnList.add(timer);
            Log.e("MyAlarmActivity","Timer ID: " + timer.getTimerId().toString() + " ArraySize: " + returnList.size());


        }

        return returnList;
    }

    @Override
    protected void onPostExecute(ArrayList<TimerDO> timerDOS) {
        ArrayList<TimerDO> temp = new ArrayList<>();
        for(TimerDO timers: timerDOS)
        {
            temp.add(timers);
            Log.e("MyAlarmActivity","Timer ID: " + timers.getTimerId().toString() + " ArraySize: " + temp.size());
        }
        listener.onTaskCompleted(temp);
        super.onPostExecute(temp);

    }
}

interface OnTaskCompleted
{
    void onTaskCompleted(ArrayList<TimerDO> timerDOS);
}

