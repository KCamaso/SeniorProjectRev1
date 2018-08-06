package edu.wit.karen.seniorprojectrev1;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedList;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedScanList;
import com.amazonaws.mobileconnectors.pinpoint.analytics.AnalyticsClient;
import com.amazonaws.mobileconnectors.pinpoint.analytics.AnalyticsEvent;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import java.util.ArrayList;

import static edu.wit.karen.seniorprojectrev1.AlarmSend.USER_ID;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MedFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MedFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MedFrag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String USER_ID = MainActivity.userId;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    public static DynamoDBMapper dynamoDBMapper;
    public ArrayList<MedicationDO> adaptList = new ArrayList<>();
    public RecyclerView.LayoutManager mLayoutManager;
    public RecyclerView recycList;
    public RecyclerView.Adapter mAdapter;

    FloatingActionButton fab;

    public MedFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MedFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static MedFrag newInstance(String param1, String param2) {
        MedFrag fragment = new MedFrag();
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

        final View view = inflater.inflate(R.layout.med_main, container, false);
        //Setup DynamoDB
        setupDynamoDB();

        new MedicationAsync(new OnTaskCompletedMeds() {
            @Override
            public void onTaskCompleted(ArrayList<MedicationDO> MedDOS) {
                recycList = view.findViewById(R.id.medRecycler);
                for(MedicationDO meds : MedDOS)
                {
                    adaptList.add(meds);
                    Log.e("MyMedActivity", "ADAPT LIST SIZE: " + adaptList.size());
                }

               mLayoutManager = new LinearLayoutManager(getContext());
               recycList.setLayoutManager(mLayoutManager);
               mAdapter = new MedicationAdapter(adaptList);
               recycList.setAdapter(mAdapter);
               mAdapter.notifyDataSetChanged();

            }
        }).execute();

                fab = view.findViewById(R.id.fab_med);

        if (fab != null)
        {
            fab.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent sendToMedication = new Intent(getContext(), MedicationSend.class);
                    startActivity(sendToMedication);
                }
            });
        }

        return view;
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

class MedicationAsync extends AsyncTask<Void, Void, ArrayList<MedicationDO>>
{
    private OnTaskCompletedMeds listener;
    public MedicationAsync(OnTaskCompletedMeds listener)
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
        PaginatedList<MedicationDO> medicationList;


        medicationList = MedFrag.dynamoDBMapper.scan(MedicationDO.class, queryExpression);

        for(MedicationDO meds: medicationList)
        {
            returnList.add(meds);
        }

        return returnList;
    }

    @Override
    protected void onPostExecute(ArrayList<MedicationDO> medicationDOS) {
        ArrayList<MedicationDO> temp = new ArrayList<>();
        for(MedicationDO meds: medicationDOS)
        {
            temp.add(meds);
            Log.e("MyAlarmActivity","Med ID: " + meds.getMedId().toString() + " ArraySize: " + temp.size());
        }
        listener.onTaskCompleted(temp);
        super.onPostExecute(temp);

    }
}

interface OnTaskCompletedMeds
{
    void onTaskCompleted(ArrayList<MedicationDO> MedDOS);
}


