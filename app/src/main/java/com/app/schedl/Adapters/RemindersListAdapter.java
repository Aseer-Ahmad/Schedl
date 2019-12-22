package com.app.schedl.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.app.schedl.Models.DailyReminder;
import com.app.schedl.R;


import java.util.List;

public class RemindersListAdapter extends RecyclerView.Adapter<RemindersListAdapter.ReminderViewHolder> {

    private Context context;
    private List<DailyReminder> list;
    private RemindersSubListAdapter remindersSubListAdapter;

    public RemindersListAdapter(Context context, List<DailyReminder> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_reminderlist, parent, false);
        return new ReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        DailyReminder dailyReminder = list.get(position);

        holder.textView_reminderitem.setText( dailyReminder.getItem_name() );

        remindersSubListAdapter = new RemindersSubListAdapter( context , dailyReminder.getList());
        holder.recyclerview_remindersubs.setAdapter(remindersSubListAdapter);

        boolean isExpanded = dailyReminder.getExpanded();
        holder.expandablelayout.setVisibility( isExpanded ? View.VISIBLE : View.GONE );
    }


    @Override
    public int getItemCount() { return list.size(); }

    class ReminderViewHolder extends RecyclerView.ViewHolder{

        TextView textView_reminderitem ;
        ConstraintLayout expandablelayout;
        RecyclerView recyclerview_remindersubs;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_reminderitem = itemView.findViewById(R.id.textview_reminderitem);
            expandablelayout = itemView.findViewById(R.id.expandable_layout);
            recyclerview_remindersubs = itemView.findViewById(R.id.recyclerview_remindersub);

            textView_reminderitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DailyReminder dailyReminder = list.get( getAdapterPosition());
                    dailyReminder.setExpanded( !dailyReminder.getExpanded() );
                    notifyItemChanged( getAdapterPosition() );
                    }
            });

        }
    }


}
