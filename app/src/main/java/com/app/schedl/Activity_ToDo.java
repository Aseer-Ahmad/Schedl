package com.app.schedl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.app.schedl.Adapters.ToDoListAdapter;
import com.app.schedl.Models.ToDo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

public class Activity_ToDo extends AppCompatActivity implements DialogTodo.DialogTodoListener {

    //components
    private ItemTouchHelper itemTouchHelper;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    //resources
    private RecyclerView.LayoutManager layoutManager;
    private ToDoListAdapter toDoListAdapter;
    private SharedPreferences sharedPreferences;
    private ArrayList<ToDo> arraylist_items = new ArrayList<>();

    //constants & flags
    private String deletedItem_name = null;
    private Date deletedItem_timebegin;
    private int deleteItem_timetocomplete;

    private static final String TAG = Activity_ToDo.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        init();

        retrieveSharedPreference();

        findComponents();

        buildRecyclerView();

        clickListeners();

    }

    private void init() {

        sharedPreferences = getSharedPreferences("App_settings", MODE_PRIVATE);

    }
    private void retrieveSharedPreference(){
        Gson gson = new Gson();
        String json = sharedPreferences.getString("DATA_LIST", null);
        Type type = new TypeToken<ArrayList<ToDo>>(){}.getType();
        arraylist_items = gson.fromJson(json, type);

        if(arraylist_items == null)
            arraylist_items = new ArrayList<>();
        Log.d(TAG, "retrieved shared preference");

    }

    private void findComponents() {
        recyclerView = findViewById(R.id.recyclerview_todo);
        fab = findViewById(R.id.fab_todo);
    }

    private void packageSharedPreference(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(arraylist_items);

        editor.putString("DATA_LIST", json);
        editor.commit();
        Log.d(TAG, "stored shared preference");
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT ) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @SuppressLint("WrongConstant")
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
                    toDoListAdapter.notifyItemRemoved(position);
                    Snackbar.make(recyclerView, deletedItem_name+": Deleted", Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //update addData to accept position parameter and then replace code below
                                    arraylist_items.add(position, new ToDo(deletedItem_name,
                                            deletedItem_timebegin,
                                            deleteItem_timetocomplete) );
                                    toDoListAdapter.notifyItemInserted(position);
                                }
                            }).show();


                    //update shared preference
                    packageSharedPreference();
                    break;
            }
        }
    };



    private void buildRecyclerView() {
        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        //recyclerView.addItemDecoration(dividerItemDecoration);
        itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        layoutManager = new LinearLayoutManager( getApplicationContext() );
        toDoListAdapter = new ToDoListAdapter(this, arraylist_items);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(toDoListAdapter);
    }

    private void clickListeners(){

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "add button clicked");

                DialogTodo dialogTodo  = new DialogTodo();
                dialogTodo.show(getSupportFragmentManager(), dialogTodo.getTag());
            }
        });
    }


    @Override
    public void addData(String item_desc, Date currenttime, int time) {
        arraylist_items.add(0, new ToDo(item_desc, currenttime, time));
        //reset
        //textView_timetojob.setText("1");
        //editText_item.setText("");

        toDoListAdapter.notifyItemInserted(0);

        //update shared preference
        packageSharedPreference();

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
