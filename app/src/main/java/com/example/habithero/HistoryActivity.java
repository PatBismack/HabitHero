package com.example.habithero;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HistoryActivity extends AppCompatActivity {
    public static SQLiteDatabase db;
    DBHelper myDbHelper;
TextView dateTV;
    TextView noResultsTV;
ImageButton leftBtn;
ImageButton rightBtn;
ImageButton backBtn;
SimpleDateFormat sdf;
    String currentDate;
    String dateSet;
    String allQuery;
    Intent habitMain;
    ArrayList<String> habitName;
    ArrayList<String> habitdesc;
    ArrayList<Integer> habitId;
    ArrayList<Integer> habitCompl;
    ArrayList<String> habitType;
    ArrayList<String> habitFreq;
    ListView habitList;
    CustomAdapter ca;
    Intent habitDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dateTV = findViewById(R.id.dateTxt);
        leftBtn = findViewById(R.id.leftBtn);
        rightBtn = findViewById(R.id.rightBtn);
        backBtn = findViewById(R.id.habitBackBtn);
        noResultsTV = findViewById(R.id.noResultsTxt);
        habitList = findViewById(R.id.habit_list);

        habitMain = new Intent(this, MainActivity.class);
        habitDetails = new Intent(this, DetailsActivity.class);
        currentDate = getCurrentDate();
        dateSet = getPreviousDate(currentDate);
        dateTV.setText(dateSet);
        noResultsTV.setVisibility(View.GONE);
        hideSoftKeyboard();
        createDB();
        allQuery = "Select H.habitId, H.name, H.description, H.completed, H.habitDate, D.type, D.frequency from habit as H INNER join details as D on H.habitId = D.habitId where H.habitDate = '" + dateSet + "'";
        getResults(allQuery);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HistoryActivity.this.startActivity(habitMain);
            }
        });
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateSet = getPreviousDate(dateSet);
                dateTV.setText(dateSet);
                rightBtn.setVisibility(View.VISIBLE);
                allQuery = "Select H.habitId, H.name, H.description, H.completed, H.habitDate, D.type, D.frequency from habit as H INNER join details as D on H.habitId = D.habitId where H.habitDate = '" + dateSet + "'";
                getResults(allQuery);
            }
        });
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isDateGreater(currentDate, getNextDate(dateSet))){
                    dateSet = getNextDate(dateSet);
                    dateTV.setText(dateSet);
                    allQuery = "Select H.habitId, H.name, H.description, H.completed, H.habitDate, D.type, D.frequency from habit as H INNER join details as D on H.habitId = D.habitId where H.habitDate = '" + dateSet + "'";
                    getResults(allQuery);
                }
                else{
                    rightBtn.setVisibility(View.GONE);
                }
            }
        });
        habitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                habitDetails.putExtra("habitName", habitName.get(i));
                habitDetails.putExtra("habitDesc", habitdesc.get(i));
                habitDetails.putExtra("habitCompl", habitCompl.get(i));
                habitDetails.putExtra("habitId", habitId.get(i));
                habitDetails.putExtra("habitType", habitType.get(i));
                habitDetails.putExtra("habitFreq", habitFreq.get(i));
                HistoryActivity.this.startActivity(habitDetails);
            }
        });
    }
    public String getCurrentDate() {
        sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }
    public static String getPreviousDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date date = sdf.parse(dateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, -1); // Subtract one day
            Date previousDate = calendar.getTime();
            return sdf.format(previousDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String getNextDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date date = sdf.parse(dateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, +1); // Add one day
            Date previousDate = calendar.getTime();
            return sdf.format(previousDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static boolean isDateGreater(String date1, String date2) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date d1 = sdf.parse(date1);
            Date d2 = sdf.parse(date2);
            return d1.compareTo(d2) > 0;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
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
    public void getResults(String q){
        Cursor result = db.rawQuery(q, null);
        result.moveToFirst();
        int count = result.getCount();
//        Log.i("count=", String.valueOf(count) + "XXX");
        // ArrayLists for name, description, isChecked and id for each habit
        habitName = new ArrayList<String>();
        habitdesc = new ArrayList<String>();
        habitCompl = new ArrayList<Integer>();
        habitId = new ArrayList<Integer>();
        habitType = new ArrayList<String>();
        habitFreq = new ArrayList<String>();
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
                habitType.add(result.getString(5));
                habitFreq.add(result.getString(6));
            } while (result.moveToNext());
            ca = new CustomAdapter(this, habitName, habitdesc, habitCompl);
            habitList.setAdapter(ca);
        } else {
            habitList.setVisibility(View.GONE);
            noResultsTV.setVisibility(View.VISIBLE);
        }
        result.close();
    }
}