package com.app.schedl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
    private SharedPreferences sharedPreferences;

    //constants & flags
    private String deletedItem_name = null;
    private Date deletedItem_timebegin;
    private int deleteItem_timetocomplete;
    private static final String TAG = MainActivity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        retrieveSharedPreference();

        findComponents();

        buildRecyclerView();

        clickListeners();

    }

    private void init() {
        sharedPreferences = getSharedPreferences("App_settings", MODE_PRIVATE);
    }

    private void packageSharedPreference(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(arraylist_items);

        editor.putString("DATA_LIST", json);
        editor.commit();
        Log.d(TAG, "stored shared preference");
    }

    private void retrieveSharedPreference(){
        Gson gson = new Gson();
        String json = sharedPreferences.getString("DATA_LIST", null);
        Type type = new TypeToken<ArrayList<DataModel>>(){}.getType();
        arraylist_items = gson.fromJson(json, type);

        if(arraylist_items == null)
            arraylist_items = new ArrayList<>();
        Log.d(TAG, "retrieved shared preference");

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
                    // restored items after deleted
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        packageSharedPreference();
    }
}
