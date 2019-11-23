package com.app.schedl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    //components
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button button_additem;
    private ImageButton button_addhrs;
    private ImageButton button_reducehrs;
    private EditText editText_item;
    private TextView textView_timetojob;
    private ItemTouchHelper itemTouchHelper;


    //resources
    private ArrayList<DataModel> arraylist_items = new ArrayList<>();
    private ListDataAdapter listDataAdapter;

    //constants & flags
    private String deletedItem_name = null;
    private Date deletedItem_timebegin;
    private int deleteItem_timetocomplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findComponents();

        buildRecyclerView();

        clickListeners();

    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT ) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            final int position = viewHolder.getAdapterPosition();

            switch(direction){
                case ItemTouchHelper.RIGHT:
                    deletedItem_name = arraylist_items.get(position).getItemname();
                    deletedItem_timebegin = arraylist_items.get(position).getItemtime_begin();
                    deleteItem_timetocomplete = arraylist_items.get(position).getItemtime_tocomplete();

                    arraylist_items.remove(position);
                    listDataAdapter.notifyItemRemoved(position);
                    Snackbar.make(recyclerView, deletedItem_name, Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    arraylist_items.add(position, new DataModel(deletedItem_name, deletedItem_timebegin, deleteItem_timetocomplete) );
                                    listDataAdapter.notifyItemInserted(position);
                                }
                            }).show();

                    break;
            }
        }
    };

    private void buildRecyclerView() {
        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        //recyclerView.addItemDecoration(dividerItemDecoration);
        itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        layoutManager = new LinearLayoutManager(this);
        listDataAdapter = new ListDataAdapter(this, arraylist_items);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(listDataAdapter);
    }

    private void findComponents() {
        recyclerView =findViewById(R.id.recyclerview_items);
        //--------------------
        button_additem = findViewById(R.id.button_additem);
        button_addhrs = findViewById(R.id.button_addhrs);
        button_reducehrs = findViewById(R.id.button_reducehrs);
        editText_item = findViewById(R.id.edittext_item);
        textView_timetojob = findViewById(R.id.textview_timetojob);
    }

    private void clickListeners(){
        button_additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = editText_item.getText().toString();
                Date currenttime = Calendar.getInstance().getTime();
                int time = Integer.parseInt(textView_timetojob.getText().toString());

                addData(s, currenttime, time);
            }
        });

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

    private void addData(String s , Date currenttime, int time){
            arraylist_items.add(0, new DataModel(s, currenttime, time));

            //reset
            textView_timetojob.setText("1");
            editText_item.setText("");

            listDataAdapter.notifyItemInserted(0);
    }

    public int getPriorityPosition(){

        return 0;
    }

}
