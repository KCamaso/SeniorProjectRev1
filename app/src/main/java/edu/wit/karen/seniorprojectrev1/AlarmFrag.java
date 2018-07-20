package edu.wit.karen.seniorprojectrev1;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
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
import com.amazonaws.mobileconnectors.pinpoint.analytics.AnalyticsClient;
import com.amazonaws.mobileconnectors.pinpoint.analytics.AnalyticsEvent;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amazonaws.mobileconnectors.pinpoint.targeting.notification.AppLevelOptOutProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AlarmFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AlarmFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlarmFrag extends Fragment  implements TimeDialog.TimeDialogueListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String USER_ID = MainActivity.userId;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    public  DynamoDBMapper dynamoDBMapper;

    FloatingActionButton fab;
    ArrayList<AlarmObj> list2 = new ArrayList<>();


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

        View view = inflater.inflate(R.layout.alarm_main, container, false);
        fab = view.findViewById(R.id.fab_alarm);

        Log.e("MyMainApplication", "THE USER ID IN ALARM FRAGMENT IS FUCKIN:" + USER_ID);

        setupDynamoDB();

        if(fab != null)
        {
            fab.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    final TimerDO timerItem = new TimerDO();

                    timerItem.setUserId(USER_ID);



                    int max = 10;
                    int min = 1;
                    int range = max - min + 1;

                    // generate random numbers within 1 to 10

                        final int rand = (int)(Math.random() * range) + min;
                    int rand2 = (int)(Math.random() * range) + min;

                        timerItem.setFromHour((double) rand);
                        timerItem.setToHour((double) rand2);




                    new Thread((new Runnable() {
                        @Override
                        public void run() {
                            dynamoDBMapper.save(timerItem);

                        }
                    })).start();


                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            TimerDO timerRead = dynamoDBMapper.load(TimerDO.class, USER_ID,(double) rand);
                            Log.e("MyMainApplication", "HOLY SHIT, THE NUMBER IS:" + timerRead.getFromHour() + timerRead.getToHour());

                        }
                    }).start();


                    // TIME DIALOGUE PLACEHOLDER
                    /*
                    Toast.makeText(getContext(), "Action for adding alarm goes here!", Toast.LENGTH_SHORT).show();

                    TimeDialog timeDialog = new TimeDialog();
                    timeDialog.show(getFragmentManager(), "Time Dialogue"); */
                }
            });
        }



        /* TEST DISPLAY CODE */
            /*
       MedObj med1 = new MedObj("Medication 1", 30, false, "Test 1");
        ArrayList<MedObj> list1 = new ArrayList<>();
        list1.add(med1);
        boolean[] doW1 = {true,false,true,false,true,false,true};
       AlarmObj alarm1 = new AlarmObj(4,20,16,46,true,true,doW1,list1);
        AlarmObj alarm2 = new AlarmObj(15,20,15,46,true,false,doW1,list1);


        list2.add(alarm1);
        list2.add(alarm2);

        RecyclerView recycList = view.findViewById(R.id.alarmRecycler);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(container.getContext());
        recycList.setLayoutManager(mLayoutManager);

        RecyclerView.Adapter mAdapter = new AlarmAdapter(list2);

        recycList.setAdapter(mAdapter);
        */




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


    @Override
    public void applyInfo(int hour, int minute, boolean[] boxes) {
        MedObj med1 = new MedObj("Medication 1", 30, false, "Test 1");
        ArrayList<MedObj> list1 = new ArrayList<>();
        boolean[] doW1 = {true,false,true,false,true,false,true};
      //  AlarmObj alarm1 = new AlarmObj(hour,minute,16,46,false,true,doW1,list1);// list2.add(alarm1);

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
