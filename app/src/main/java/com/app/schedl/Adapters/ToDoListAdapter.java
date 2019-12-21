package com.app.schedl.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.schedl.Models.ToDo;
import com.app.schedl.R;

import java.util.List;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ListDataViewHolder> {

    private Context context;
    private List<ToDo> list;


    public ToDoListAdapter(Context context, List<ToDo> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ListDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_todolist, parent, false);
        return new ListDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListDataViewHolder holder, int position) {
        ToDo toDo = list.get(position);

        holder.textView_itemname.setText(toDo.getItemname());
        holder.textView_itemdate.setText(toDo.getItemtime_begin().toString());
        holder.textView_timeleft.setText( String.valueOf(toDo.getItemtime_tocomplete()) );

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
