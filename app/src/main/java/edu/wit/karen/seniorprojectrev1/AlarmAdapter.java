package edu.wit.karen.seniorprojectrev1;


import android.animation.ObjectAnimator;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

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
            editButt = (Button) itemView.findViewById(R.id.alarmEditButton);
            weekLayout = (ConstraintLayout) itemView.findViewById(R.id.weekLayout);
            checkBoxes = new CheckBox[] {itemView.findViewById(R.id.checkBoxSu),itemView.findViewById(R.id.checkBoxM),itemView.findViewById(R.id.checkBoxTu),itemView.findViewById(R.id.checkBoxW),itemView.findViewById(R.id.checkBoxTh),itemView.findViewById(R.id.checkBoxF),itemView.findViewById(R.id.checkBoxSa)};

            moreButt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ObjectAnimator anime1 = ObjectAnimator.ofInt(weekLayout, "maxHeight", weekLayout.getMaxHeight());
                    if( weekLayout.getVisibility() == View.GONE)
                    {
                        weekLayout.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        weekLayout.setVisibility(View.GONE);
                    }
                    anime1.setDuration(200).start();
                }

            });


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
    public void onBindViewHolder(final ViewHolder viewHolder, int i)
    {

            AlarmObj alarm = mAlarms.get(i);
            boolean active = alarm.isActive();

            if(mAlarms.get(i).isWindow() == false)
            {
                int fromHour = alarm.getFromHour();
                int fromMinute = alarm.getFromMinute();

                boolean[] week = alarm.getDayOfWeek();
                List<String> meds = alarm.getMedications();

                viewHolder.alarmTime.setText(fromHour + ":" + fromMinute);
                weekCheck(viewHolder, week);
                medList(viewHolder, meds);
                viewHolder.alarmDate.setVisibility(View.VISIBLE);


            }
            else
            {
                int fromHour = mAlarms.get(i).getFromHour();
                int fromMinute = mAlarms.get(i).getFromMinute();
                int toHour = mAlarms.get(i).getToHour();
                int toMinute = mAlarms.get(i).getToHour();

                boolean[] week = alarm.getDayOfWeek();
                List<String> meds = alarm.getMedications();

                viewHolder.alarmTime.setText(fromHour + ":" + fromMinute);
                viewHolder.alarmDate.setText(toHour + ":" + toMinute);
                weekCheck(viewHolder, week);
                medList(viewHolder, meds);

            }
            if(active)
            {
                viewHolder.activeSwitch.setChecked(active);
                viewHolder.statusImage.setImageResource(R.color.colorAccent);

            }

    }

    public void weekCheck(ViewHolder viewHolder, boolean[] days)
    {
        for(int i = 0; i < 6; i++)
        {
                viewHolder.checkBoxes[i].setChecked( days[i] );
        }
    }

    public void medList(ViewHolder viewHolder, List<String> meds)
    {

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }


}
