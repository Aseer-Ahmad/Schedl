package com.app.schedl;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.nio.channels.AlreadyBoundException;

public class DialogReminders extends DialogFragment {

    private EditText editText_remindername;
    private EditText editText_remindersubnames;
    private DialogReminderListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = LayoutInflater.from(getContext()).inflate( R.layout.dialoglayout_reminders , null);
        //components
        editText_remindername = view.findViewById(R.id.edittext_reminderitem);
        editText_remindersubnames = view.findViewById(R.id.edittext_remindersubitem);

        builder.setView(view)
                .setTitle("")
                .setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String remindername = editText_remindername.getText().toString();
                        String remindersubnames = editText_remindersubnames.getText().toString();

                        listener.addData(remindername, remindersubnames);
                    }
                });



        return builder.create();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DialogReminderListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement DialogRemindersListener");
        }
    }

    public interface DialogReminderListener{
        void addData(String remindername, String remindersubnames);
    }
}
