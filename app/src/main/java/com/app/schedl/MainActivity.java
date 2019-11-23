package com.app.schedl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.ClipData;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
    private EditText editText_item;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ItemTouchHelper itemTouchHelper;

    //resources
    private ArrayList<DataModel> arraylist_items = new ArrayList<>();
    private ListDataAdapter listDataAdapter;

    //constants & flags
    private String deletedItem_name = null;
    private Date deletedItem_date ;

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
                    deletedItem_date = arraylist_items.get(position).getItemdate();

                    arraylist_items.remove(position);
                    listDataAdapter.notifyItemRemoved(position);
                    Snackbar.make(recyclerView, deletedItem_name, Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    arraylist_items.add(position, new DataModel(deletedItem_name, deletedItem_date) );
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
        editText_item = findViewById(R.id.edittext_item);
    }

    private void clickListeners(){
        button_additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = editText_item.getText().toString();
                Date currenttime = Calendar.getInstance().getTime();

                addData(s, currenttime);
                editText_item.setText("");
            }
        });
    }

    private void addData(String s , Date currenttime){
            arraylist_items.add(0, new DataModel(s, currenttime));
            listDataAdapter.notifyItemInserted(0);
    }

    public int getPriorityPosition(){

        return 0;
    }

}
