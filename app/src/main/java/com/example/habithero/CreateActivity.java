package com.example.habithero;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CreateActivity extends AppCompatActivity {
public static SQLiteDatabase db;
DBHelper myDbHelper;
EditText nameET;
EditText descET;
EditText typeET;
EditText freqET;

Button saveBtn;

String name;
String description;
String type;
String frequency;
String insertQuery;
String habitIdQuery;
ArrayList<Integer> habitId;

SimpleDateFormat sdf;
Intent habitActivity;
int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        nameET = findViewById(R.id.nameET);
        descET = findViewById(R.id.descET);
        typeET = findViewById(R.id.typeET);
        freqET = findViewById(R.id.freqET);
        saveBtn = findViewById(R.id.saveBtn);

        hideSoftKeyboard();
        createDB();

        habitActivity = new Intent(this, HabitActivity.class);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nameET.getText().toString();
                description = descET.getText().toString();
                type = typeET.getText().toString();
                frequency = freqET.getText().toString();
                habitIdQuery = "select max(habitId) from habit";
                getCurrentId(habitIdQuery);
                id = habitId.get(0) + 1;
                insertQuery = "insert into habit (habitId, name, description, completed, habitDate) values ('" + id + "', '" + name + "', '" + description + "', '0', '" + getCurrentDate() + "')";
                modifyRecord(insertQuery);
                insertQuery = "insert into details (habitId, type, frequency, habitDate) values ('" + id + "', '" + type + "', '" + frequency + "', '" + getCurrentDate() + "')";
                modifyRecord(insertQuery);
                CreateActivity.this.startActivity(habitActivity);
            }
        });
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
    public void getCurrentId(String q) {
        Cursor result = db.rawQuery(q, null);
        result.moveToFirst();
        int count = result.getCount();
//        Log.i("count=", String.valueOf(count) + "XXX");
        habitId = new ArrayList<Integer>();
        if (count >= 1) {
            do {
                habitId.add(result.getInt(0));
            } while (result.moveToNext());
        }
    }

    public void modifyRecord(String q){
        db.execSQL(q);
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