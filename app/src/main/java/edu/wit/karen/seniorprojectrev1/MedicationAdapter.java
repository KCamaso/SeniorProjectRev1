package edu.wit.karen.seniorprojectrev1;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

/**
 * Created by camasok on 2/16/2018.
 */

public class MedicationAdapter extends  RecyclerView.Adapter<MedicationAdapter.ItemViewHolder>{

    public static class ItemViewHolder extends  RecyclerView.ViewHolder
    {

        ItemViewHolder(View itemView){
            super(itemView);
        }
    }

    List<MedObj>medItemListing;
    MedicationAdapter(List<MedObj> medItemListing)
    {
        this.medItemListing = medItemListing;
    }


    @Override
    public int getItemCount()
    {
        return medItemListing.size();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate( R.layout.card_layout_med, viewGroup, false);
        ItemViewHolder ivh = new ItemViewHolder(v);

        return ivh;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder itemViewHolder, int i)
    {
        // Set resources here

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }



}
