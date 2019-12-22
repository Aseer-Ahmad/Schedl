package com.app.schedl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.schedl.Adapters.RemindersListAdapter;
import com.app.schedl.Adapters.RemindersSubListAdapter;
import com.app.schedl.Models.DailyReminder;
import com.app.schedl.Models.DailyReminderSub;
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
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements DialogReminders.DialogReminderListener {

    //components
    private RecyclerView recyclerView_reminders;
    private RecyclerView recyclerview_reminderssubs;
    private FloatingActionButton fab;
    private BottomAppBar bottomAppBar;


    //resources
    private RemindersListAdapter remindersListAdapter ;
    private RemindersSubListAdapter remindersSubListAdapter;
    private List<DailyReminder> dailyReminderList = new ArrayList<DailyReminder>();
    private List<DailyReminderSub> dailyReminderSubList;

    //constants & flags
   private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        findComponents();

        buildRecyclerView();

        clickListeners();

    }


    private void init() {


    }

    private void findComponents() {
        recyclerView_reminders = findViewById(R.id.recyclerview_remindersdaily);

        fab = findViewById(R.id.fab_remindersdaily);

        bottomAppBar = findViewById(R.id.bottom_app_bar);
        setSupportActionBar(bottomAppBar);
    }

    private void buildRecyclerView(){

        remindersListAdapter = new RemindersListAdapter( this, dailyReminderList);
        //remindersSubListAdapter = new RemindersSubListAdapter( this, dailyReminderSubList);

        recyclerView_reminders.setAdapter( remindersListAdapter );
        //recyclerview_reminderssubs.setAdapter( remindersSubListAdapter);

    }


    //for items on the bottom bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_app_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId() ){

            case R.id.menu_item_daily_reminders:
                Log.d(TAG, "clicked reminder navigation item");
                Toast.makeText(getApplicationContext(), "To Do List", Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(MainActivity.this, Activity_ToDo.class);
                startActivity(intent);

                return true;

            case R.id.menu_item_daily_reminders_reset:
                Toast.makeText(getApplicationContext(), "Reset All Items.", Toast.LENGTH_SHORT).show();
                return true;

            case android.R.id.home:
                BottomNavigationDrawerFragment bndf = new BottomNavigationDrawerFragment();
                bndf.show(getSupportFragmentManager(), bndf.getTag() );

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void addData(String remindername, String remindersubnames) {

        dailyReminderSubList = new ArrayList<>();

        String [] subnames = remindersubnames.split(",");
        for(String s : subnames){
            s = s.trim();
            dailyReminderSubList.add(new DailyReminderSub(s));
        }

        dailyReminderList.add(new DailyReminder(remindername, dailyReminderSubList));
        remindersListAdapter.notifyDataSetChanged();

    }

    private void clickListeners() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogReminders dialogReminders = new DialogReminders();
                dialogReminders.show(getSupportFragmentManager(), dialogReminders.getTag());
            }
        });
    }

}
