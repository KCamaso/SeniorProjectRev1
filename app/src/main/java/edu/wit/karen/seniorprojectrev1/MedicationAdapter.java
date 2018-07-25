package edu.wit.karen.seniorprojectrev1;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by camasok on 2/16/2018.
 */

public class MedicationAdapter extends  RecyclerView.Adapter<MedicationAdapter.ViewHolder>{

    List<MedicationDO>medItemListing;
    public static class ViewHolder extends  RecyclerView.ViewHolder
    {
        ImageView timeCheck;
        TextView nameText;
        TextView descView;
        Button buttonDelete;
        Button buttonRefill;
        EditText number;
        Button buttonPlus;
        Button buttonMinus;


        ViewHolder(View itemView){
            super(itemView);

            timeCheck = itemView.findViewById(R.id.timeCheck);
            nameText = itemView.findViewById(R.id.timeText);
            descView = itemView.findViewById(R.id.descView);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            buttonRefill = itemView.findViewById(R.id.buttonRefill);
            number = itemView.findViewById(R.id.numberText);
            buttonPlus = itemView.findViewById(R.id.buttonPlus);
            buttonMinus = itemView.findViewById(R.id.buttonMinus);

        }
    }


    MedicationAdapter(List<MedicationDO> medItemListing)
    {
        this.medItemListing = medItemListing;
    }


    @Override
    public int getItemCount()
    {
        return medItemListing.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate( R.layout.card_layout_med, viewGroup, false);
        ViewHolder ivh = new ViewHolder(v);

        return ivh;
    }

    @Override
    public void onBindViewHolder(ViewHolder itemViewHolder, int i)
    {
        // Set resources here

        MedicationDO med = medItemListing.get(i);

        itemViewHolder.number.setText( Integer.parseInt(med.getCurrentNum()));
        itemViewHolder.descView.setText(med.getInfo());
        itemViewHolder.nameText.setText(med.getName());



    }

    public void onClick(ViewHolder v)
    {

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }



}
