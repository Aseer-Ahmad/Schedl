package com.app.schedl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements DialogTodo.DialogTodoListener {

    //components
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button button_additem;
    private ItemTouchHelper itemTouchHelper;
    private FloatingActionButton fab;
    private BottomAppBar bottomAppBar;


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
                    listDataAdapter.notifyItemRemoved(position);
                    Snackbar snack = Snackbar.make(recyclerView, deletedItem_name+": Deleted", Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //update addData to accept position parameter and then replace code below
                                    arraylist_items.add(position, new DataModel(deletedItem_name,
                                                                                deletedItem_timebegin,
                                                                                deleteItem_timetocomplete) );
                                    listDataAdapter.notifyItemInserted(position);
                                }
                            });//.show();

                    CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) snack.getView().getLayoutParams();
                    params.setMargins(0, 0, 0, bottomAppBar.getHeight());
                    snack.getView().setLayoutParams(params);
                    snack.show();

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

        layoutManager = new LinearLayoutManager(this);
        listDataAdapter = new ListDataAdapter(this, arraylist_items);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(listDataAdapter);
    }

    private void findComponents() {
        recyclerView =findViewById(R.id.recyclerview_items);
        fab = findViewById(R.id.fab);
        bottomAppBar = findViewById(R.id.bottom_app_bar);
        setSupportActionBar(bottomAppBar);
    }


    //for items on the bottom bar
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId() ){
//          case R.id.navitem_dailyrem:
//              Log.d(TAG, "clicked reminder navigation item");
//              Toast.makeText(getApplicationContext(), "Daily reminders clicked", Toast.LENGTH_SHORT).show();
//              return true;
            case android.R.id.home:
                BottomNavigationDrawerFragment bndf = new BottomNavigationDrawerFragment();
                bndf.show(getSupportFragmentManager(), bndf.getTag() );

        }

        return super.onOptionsItemSelected(item);
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
            arraylist_items.add(0, new DataModel(item_desc, currenttime, time));

            //reset
            //textView_timetojob.setText("1");
            //editText_item.setText("");

            listDataAdapter.notifyItemInserted(0);

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
