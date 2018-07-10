package edu.wit.karen.seniorprojectrev1;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.pinpoint.analytics.AnalyticsClient;
import com.amazonaws.mobileconnectors.pinpoint.analytics.AnalyticsEvent;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AlarmFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AlarmFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlarmFrag extends Fragment /* implements TimeDialog.TimeDialogueListener */ {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

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

        if (fab != null)
        {
            fab.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Toast.makeText(getContext(), "Action for adding alarm goes here!", Toast.LENGTH_SHORT).show();
                    openAlarmDia();
                }
            });
        }

       MedObj med1 = new MedObj("Medication 1", 30, false, "Test 1");
        ArrayList<MedObj> list1 = new ArrayList<>();
        list1.add(med1);
        boolean[] doW1 = {true,false,true,false,true,false,true};
       AlarmObj alarm1 = new AlarmObj(4,20,16,46,true,true,doW1,list1);
        AlarmObj alarm2 = new AlarmObj(15,20,15,46,true,true,doW1,list1);


        list2.add(alarm1);
        list2.add(alarm2);

        RecyclerView recycList = view.findViewById(R.id.alarmRecycler);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(container.getContext());
        recycList.setLayoutManager(mLayoutManager);

        RecyclerView.Adapter mAdapter = new AlarmAdapter(list2);

        recycList.setAdapter(mAdapter);


        return view;

    }

    public void openAlarmDia()
    {
        TimeDialog timeDialog = new TimeDialog();
        timeDialog.show(getChildFragmentManager(), "Time Dialogue");
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

    /*
    @Override
    public void applyInfo(int hour, int minute, boolean[] boxes) {
        MedObj med1 = new MedObj("Medication 1", 30, false, "Test 1");
        ArrayList<MedObj> list1 = new ArrayList<>();
        AlarmObj alarm1 = new AlarmObj(hour,minute,16,46,false,true,boxes,list1);
        list2.add(alarm1);

    } */

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
