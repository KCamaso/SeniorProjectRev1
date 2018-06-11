package edu.wit.karen.seniorprojectrev1;


import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

/**
 * Created by camasok on 2/16/2018.
 */

public class AlarmAdapter extends  RecyclerView.Adapter<AlarmAdapter.ViewHolder>{

    public List<AlarmObj> mAlarms;
    public static class ViewHolder extends  RecyclerView.ViewHolder
    {

        ImageView statusImage;
        TextView alarmTime;
        TextView alarmDate;
        ListView list;
        Switch activeSwitch;
        Button moreButt;
        Button editButt;
        ConstraintLayout weekLayout;

        CheckBox[] checkBoxes;

        public ViewHolder(View itemView) {
            super(itemView);

            statusImage = (ImageView) itemView.findViewById(R.id.timeCheck);
            alarmTime = (TextView) itemView.findViewById(R.id.alarmTimeView);
            alarmDate = (TextView) itemView.findViewById(R.id.alarmDateView);
            list = (ListView) itemView.findViewById(R.id.alarm_dynamic);
            activeSwitch = (Switch) itemView.findViewById(R.id.alarmSwitch);
            moreButt = (Button) itemView.findViewById(R.id.alarmMoreButton);
            editButt = (Button) itemView.findViewById(R.id.weekLayout);
            weekLayout = (ConstraintLayout) itemView.findViewById(R.id.weekLayout);


        }



    }

    public AlarmAdapter(List<AlarmObj> alarum)

    {
        mAlarms = alarum;
    }



    @Override
    public int getItemCount()
    {
        return mAlarms.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_layout_alarm, viewGroup, false);
        ViewHolder ivh = new ViewHolder(v);

        return ivh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i)
    {
        for(int n = 0; n < i; i++)
        {

        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }


}
