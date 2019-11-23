package com.app.schedl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListDataAdapter extends RecyclerView.Adapter<ListDataAdapter.ListDataViewHolder> {

    private Context context;
    private List<DataModel> list;


    public ListDataAdapter(Context context, List<DataModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ListDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
        return new ListDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListDataViewHolder holder, int position) {
        DataModel dataModel = list.get(position);

        holder.textView_itemname.setText(dataModel.getItemname());
        holder.textView_itemdate.setText(dataModel.getItemtime_begin().toString());
        holder.textView_timeleft.setText( String.valueOf(dataModel.getItemtime_tocomplete()) );

        //set click listeners here

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class ListDataViewHolder extends  RecyclerView.ViewHolder{

        TextView textView_itemname, textView_itemdate, textView_timeleft;


        public ListDataViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_itemdate = itemView.findViewById(R.id.textview_itemdate);
            textView_itemname = itemView.findViewById(R.id.textview_itemname);
            textView_timeleft = itemView.findViewById(R.id.textview_timeleft);
        }
    }
}
