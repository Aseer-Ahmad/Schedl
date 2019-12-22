package com.app.schedl.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.schedl.Models.DailyReminderSub;
import com.app.schedl.R;

import java.util.List;

public class RemindersSubListAdapter extends RecyclerView.Adapter<RemindersSubListAdapter.RemindersSubHolder> {


    Context context;
    List<DailyReminderSub> list;

    public RemindersSubListAdapter(Context context, List<DailyReminderSub> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RemindersSubHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_remindersublist, parent, false);

        return new RemindersSubHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RemindersSubHolder holder, int position) {

        DailyReminderSub dailyReminderSub = list.get(position);

        holder.textView.setText( dailyReminderSub.getItemsub_name() );

    }

    @Override
    public int getItemCount() { return list.size(); }

    class RemindersSubHolder extends RecyclerView.ViewHolder{
        TextView textView;
        CheckBox checkBox;

        public RemindersSubHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textview_subitem);
            checkBox = itemView.findViewById(R.id.checkbox_subitem);
        }
    }


}
