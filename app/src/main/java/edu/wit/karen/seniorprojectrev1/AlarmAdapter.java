package edu.wit.karen.seniorprojectrev1;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by camasok on 2/16/2018.
 */

public class AlarmAdapter extends  RecyclerView.Adapter<AlarmAdapter.ItemViewHolder>{

    public static class ItemViewHolder extends  RecyclerView.ViewHolder
    {

        ItemViewHolder(View itemView){
         super(itemView);
        }
    }

    List<AlarmObj> battleItemListing;
    AlarmAdapter(List<AlarmObj> battleItemListing)
    {
        this.battleItemListing = battleItemListing;
    }


    @Override
    public int getItemCount()
    {
        return battleItemListing.size();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_layout_alarm, viewGroup, false);
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
