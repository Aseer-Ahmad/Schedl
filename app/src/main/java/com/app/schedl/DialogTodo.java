package com.app.schedl;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;

public class DialogTodo extends DialogFragment {

    private ImageButton button_addhrs;
    private ImageButton button_reducehrs;
    private EditText editText_item;
    private TextView textView_timetojob;
    private DialogTodoListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view  = inflater.inflate(R.layout.dialoglayout_todo,null);

        //components here
        button_addhrs = view.findViewById(R.id.button_addhrs);
        button_reducehrs = view.findViewById(R.id.button_reducehrs);
        editText_item = view.findViewById(R.id.edittext_item);
        textView_timetojob = view.findViewById(R.id.textview_timetojob);

        clickListeners();

        builder.setView(view)
                .setTitle("Add Item")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String item_desc = editText_item.getText().toString();
                        Date currenttime = Calendar.getInstance().getTime();
                        int time = Integer.parseInt(textView_timetojob.getText().toString());

                        listener.addData(item_desc, currenttime, time);
                    }
                });

        return builder.create();
    }

    private void clickListeners() {

        button_addhrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(textView_timetojob.getText().toString());
                textView_timetojob.setText( String.valueOf(count+1) );
            }
        });

        button_reducehrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(textView_timetojob.getText().toString());
                if (count != 1)
                    textView_timetojob.setText(String.valueOf(count-1));
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DialogTodoListener) context;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement DialogTodoListener.");
        }
    }

    public interface DialogTodoListener{
        void  addData(String item_desc, Date currenttime, int time );
    }
}
