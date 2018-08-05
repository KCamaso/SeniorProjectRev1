package edu.wit.karen.seniorprojectrev1;


import android.content.Intent;
import android.os.Bundle;
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
        Button buttonEdit;
        Button buttonRefill;
        EditText number;




        ViewHolder(View itemView){
            super(itemView);

            timeCheck = itemView.findViewById(R.id.timeCheck);
            nameText = itemView.findViewById(R.id.timeText);
            descView = itemView.findViewById(R.id.descView);
            buttonEdit = itemView.findViewById(R.id.buttonMedEdit);

            number = itemView.findViewById(R.id.numberText);


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

        final MedicationDO med = medItemListing.get(i);

        itemViewHolder.number.setText( Integer.parseInt(med.getCurrentNum()));
        itemViewHolder.descView.setText(med.getInfo());
        itemViewHolder.nameText.setText(med.getName());

    itemViewHolder.buttonEdit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle sendData = new Bundle();

            String bundleName = med.getName();
            String bundleDesc = med.getInfo();
            String bundleUserId = med.getUserId();
            String bundleCurrentNumber = med.getCurrentNum();
            Double bundleMedId = med.getMedId();
            String bundleMedMax = med.getMaxNum();
            Boolean bundleInfinite = med.getInfinite();
            Boolean notify = med.getNotify();
            String notifyNumber = med.getNotify().toString();

            sendData.putString("name",bundleName);
            sendData.putString("desc",bundleDesc);
            sendData.putString("userId",bundleUserId);
            sendData.putString("currentNumber",bundleCurrentNumber);
            sendData.putDouble("medId",bundleMedId);
            sendData.putString("bundleMedMax",bundleMedMax);
            sendData.putBoolean("notify",notify);
            sendData.putBoolean("infinite", bundleInfinite);
            sendData.putString("notifyNumber", notifyNumber);

            Intent sendMedication = new Intent(v.getContext(), MedicationSend.class);
            sendMedication.putExtras(sendData);
            v.getContext().startActivity(sendMedication);

        }
    });

    }



    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }



}
