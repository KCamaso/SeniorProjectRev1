package edu.wit.karen.seniorprojectrev1;


import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
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

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static java.security.AccessController.getContext;

/**
 * Created by camasok on 2/16/2018.
 */

public class AlarmAdapter extends  RecyclerView.Adapter<AlarmAdapter.ViewHolder>{

    public ArrayList<TimerDO> mAlarms;
    public static CheckBox[] checkBoxes;
    public static class ViewHolder extends  RecyclerView.ViewHolder
    {

        ImageView statusImage;
        TextView alarmTime;
        TextView alarmDate;
        TextView list;
        Switch activeSwitch;
        Button moreButt;
        Button editButt;
        ConstraintLayout weekLayout;



        public ViewHolder(final View itemView) {
            super(itemView);

            statusImage = (ImageView) itemView.findViewById(R.id.timeCheck);
            alarmTime = (TextView) itemView.findViewById(R.id.alarmTimeView);
            alarmDate = (TextView) itemView.findViewById(R.id.alarmDateView);
            list = (TextView) itemView.findViewById(R.id.alarm_dynamic);
            activeSwitch = (Switch) itemView.findViewById(R.id.alarmSwitch);
            moreButt = (Button) itemView.findViewById(R.id.alarmDeleteButton);
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

    public AlarmAdapter(ArrayList<TimerDO> alarum)

    {
        mAlarms = alarum;
    }



    @Override
    public int getItemCount()
    {
        if(mAlarms == null)
        {
            return 0;
        }
        else
        {
            return mAlarms.size();
        }

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

            final TimerDO alarm = mAlarms.get(i);
            boolean active = alarm.getActive();

            if(mAlarms.get(i).getActive() == false)
            {
                int fromHour = (alarm.getToHour().intValue());
                int fromMinute = alarm.getToMinute().intValue();

                Set<Double> week = alarm.getDayOfWeek();
                Set<String> meds = alarm.getMedName();

                viewHolder.alarmTime.setText(fromHour + ":" + fromMinute);
                weekCheck(week);
                medList(viewHolder, meds);
                viewHolder.alarmDate.setVisibility(View.VISIBLE);


            }
            else
            {
                int fromHour = alarm.getFromHour().intValue();
                int fromMinute = alarm.getFromMinute().intValue();
                int toHour = alarm.getToHour().intValue();
                int toMinute = alarm.getToHour().intValue();

                Set<Double> week = alarm.getDayOfWeek();
                Set<String> meds = alarm.getMedName();

                viewHolder.alarmTime.setText(fromHour + ":" + fromMinute);
                viewHolder.alarmDate.setText(toHour + ":" + toMinute);
                weekCheck(week);
                medList(viewHolder, meds);

            }
            if(active)
            {
                viewHolder.activeSwitch.setChecked(active);
                viewHolder.statusImage.setImageResource(R.color.colorAccent);

            }

        viewHolder.editButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle sendData = new Bundle();

                double[] bundleTimeFrom = { alarm.getFromHour(), alarm.getFromMinute()};
                double[] bundleTimeTo = {alarm.getToHour(), alarm.getToMinute()};

                boolean[] checkBoxBoolean = new boolean[7];
                for(int i = 0; i < checkBoxes.length; i++)
                {
                    checkBoxBoolean[i] = checkBoxes[i].isChecked();
                }

                sendData.putDoubleArray("timeFrom", bundleTimeFrom);
                sendData.putDoubleArray("timeTo", bundleTimeTo);
                sendData.putBooleanArray("weekDay", checkBoxBoolean);
                sendData.putDouble("alarmId", alarm.getTimerId());

                Intent sendAlarm = new Intent(v.getContext(), AlarmSend.class);
                sendAlarm.putExtras(sendData);
                v.getContext().startActivity(sendAlarm);





            }
        });

    }

    public void weekCheck( Set<Double> days)
    {
        Iterator<Double> daysIterator = days.iterator();
        int w = 0;
        while (daysIterator.hasNext())
        {
                Double number = daysIterator.next();
                if(number.intValue() == 1.0 )
                {
                    checkBoxes[w].setChecked(true);
                }
                else
                {
                    checkBoxes[w].setChecked(false);
                }
                w++;
        }
    }

    public void medList(ViewHolder viewHolder, Set<String> meds)
    {
        Iterator<String> medSimple = meds.iterator();
        String medName = medSimple.next();
        viewHolder.list.setText(medName);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }


}
