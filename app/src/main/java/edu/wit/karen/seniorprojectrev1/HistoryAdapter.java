package edu.wit.karen.seniorprojectrev1;


import android.icu.util.Calendar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by camasok on 2/16/2018.
 */

public class HistoryAdapter extends  RecyclerView.Adapter<HistoryAdapter.ViewHolder>{


    public List<HistoryObj> mHistory;
    public static class ViewHolder extends  RecyclerView.ViewHolder
    {

        ImageView timeCheck;
        TextView timeText;
        TextView missView;
        TextView dateView;
        Button buttonDelete;
        TextView histList;




        ViewHolder(View itemView){
         super(itemView);

         timeCheck = (ImageView) itemView.findViewById(R.id.timeCheck);
         timeText = (TextView) itemView.findViewById(R.id.timeText);
         missView = (TextView) itemView.findViewById(R.id.missView);
         dateView = (TextView) itemView.findViewById(R.id.dateView);
         buttonDelete = (Button) itemView.findViewById(R.id.buttonDelete);

        }
    }


    HistoryAdapter(List<HistoryObj> historum)
    {
        mHistory = historum;
    }


    @Override
    public int getItemCount()
    {
        return mHistory.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_layout_history, viewGroup, false);
        ViewHolder ivh = new ViewHolder(v);

        return ivh;
    }

    @Override
    public void onBindViewHolder(ViewHolder itemViewHolder, int i)
    {
        HistoryObj hHistory = mHistory.get(i);

        if(hHistory.getDismissed().toString().equals("true"))
        {
            itemViewHolder.timeText.setText("Taken");
        }
        else
        {
            itemViewHolder.timeText.setText("Missed");
        }


       itemViewHolder.missView.setText( hHistory.getMissedMeds().toString());

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("EEEE, MMMM d, yyyy");
        itemViewHolder.dateView.setText(format.format(calendar.getTime()));
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }


}
