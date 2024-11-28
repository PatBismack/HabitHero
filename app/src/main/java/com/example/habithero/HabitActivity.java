package com.example.habithero;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HabitActivity extends AppCompatActivity {
//Database Variables
    public static SQLiteDatabase db;
    DBHelper myDbHelper;
    ArrayList<String> habitName;
    ArrayList<String> habitdesc;
    ArrayList<Integer> habitId;
    ArrayList<Integer> habitCompl;
    ArrayList<String> habitType;
    ArrayList<String> habitFreq;

    String allQuery;
//Layout Variables
//ListView
    ListView habitList;
    CustomAdapter ca;
//TextView
    TextView noResultsTV;
    TextView dateTV;
//Date
    SimpleDateFormat sdf;
//Image Buttons
    ImageButton addBtn;
    ImageButton backBtn;
// Intent Variables
    Intent habitDetails;
    Intent habitMain;
    Intent habitCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_habit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
//Link variables to widgets
        noResultsTV = findViewById(R.id.noResultsTxt);
        habitList = findViewById(R.id.habit_list);
        dateTV = findViewById(R.id.dateTxt);
        addBtn = findViewById(R.id.add_icon);
        backBtn = findViewById(R.id.habitBackBtn);

//Set No data text to invisible
        noResultsTV.setVisibility(View.GONE);
//Set to current date
        dateTV.setText(getCurrentDate());
//Create intent for detailActivity and mainActivity
        habitDetails = new Intent(this, DetailsActivity.class);
        habitCreate = new Intent(this, CreateActivity.class);
        habitMain = new Intent(this, MainActivity.class);

        hideSoftKeyboard();
        createDB();
//Show data only for the current day
        allQuery = "select * from habit where habitDate = '" + getCurrentDate() + "'";
        getResult(allQuery);
        allQuery = "select * from details where habitDate = '" + getCurrentDate() + "'";
//        db.execSQL("ALTER TABLE details ADD COLUMN habitDate TEXT;");
//        createDB();
//        createDetailsDB();
        getDetailResult(allQuery);

//Create new habit
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HabitActivity.this.startActivity(habitCreate);
            }
        });

//Go to home page
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HabitActivity.this.startActivity(habitMain);
            }
        });
//Go to Habit details
        habitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                habitDetails.putExtra("habitName", habitName.get(i));
                habitDetails.putExtra("habitDesc", habitdesc.get(i));
                habitDetails.putExtra("habitCompl", habitCompl.get(i));
                habitDetails.putExtra("habitId", habitId.get(i));
                habitDetails.putExtra("habitType", habitType.get(i));
                habitDetails.putExtra("habitFreq", habitFreq.get(i));
                HabitActivity.this.startActivity(habitDetails);
            }
        });
    }

    public void getResult(String q) {
        Cursor result = db.rawQuery(q, null);
        result.moveToFirst();
        int count = result.getCount();
//        Log.i("count=", String.valueOf(count) + "XXX");
        // ArrayLists for name, description, isChecked and id for each habit
        habitName = new ArrayList<String>();
        habitdesc = new ArrayList<String>();
        habitCompl = new ArrayList<Integer>();
        habitId = new ArrayList<Integer>();
        if (count >= 1) {
            noResultsTV.setVisibility(View.GONE);
            habitList.setVisibility(View.VISIBLE);
            do {
                habitId.add(result.getInt(0));
                habitName.add(result.getString(1));
//                Remove Text overflow or can just limit to 1 line
//                if(result.getString(2).length() > 47){
//                   String overflow = result.getString(2).substring(0, 47) + "...";
//                    habitdesc.add(overflow);
//                }
//                else{ habitdesc.add(result.getString(2));}
                habitdesc.add(result.getString(2));
                habitCompl.add(result.getInt(3));
            } while (result.moveToNext());
            ca = new CustomAdapter(this, habitName, habitdesc, habitCompl);
            habitList.setAdapter(ca);
        } else {
            habitList.setVisibility(View.GONE);
            noResultsTV.setVisibility(View.VISIBLE);
        }
        result.close();
    }
    public void getDetailResult(String q){
        Cursor result = db.rawQuery(q, null);
        result.moveToFirst();
        int count = result.getCount();
//        Log.i("count=", String.valueOf(count) + "XXX");
        // ArrayLists for name, description, isChecked and id for each habit
        habitType = new ArrayList<String>();
        habitFreq = new ArrayList<String>();
        if (count >= 1) {
            do {
                habitType.add(result.getString(1));
                habitFreq.add(result.getString(2));
            } while (result.moveToNext());
        }
        result.close();
    }
    public void createDB() {
        myDbHelper = new DBHelper(this);
        try {
            myDbHelper.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");
        }

        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
        }
        db = myDbHelper.getWritableDatabase();
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public String getCurrentDate() {
        sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }

}