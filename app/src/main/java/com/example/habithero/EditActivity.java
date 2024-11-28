package com.example.habithero;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditActivity extends AppCompatActivity {
    public static SQLiteDatabase db;
    DBHelper myDbHelper;
    SimpleDateFormat sdf;
ImageButton backBtn;
Intent habitActivity;
Intent habitDetails;

    EditText nameET;
    EditText descET;
    EditText typeET;
    EditText freqET;

    Button saveBtn;

    String name;
    String description;
    String type;
    String frequency;
    int id;
    String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backBtn = findViewById(R.id.backBtn);
        saveBtn = findViewById(R.id.saveBtn);
        nameET = findViewById(R.id.nameET);
        descET = findViewById(R.id.descET);
        typeET = findViewById(R.id.typeET);
        freqET = findViewById(R.id.freqET);

        habitActivity = new Intent(this, HabitActivity.class);
        habitDetails = getIntent();
        name = habitDetails.getStringExtra("habitName");
        description = habitDetails.getStringExtra("habitDesc");
        type = habitDetails.getStringExtra("habitType");
        frequency = habitDetails.getStringExtra("habitFreq");
        id = habitDetails.getIntExtra("habitId", 0);

        nameET.setText(name);
        descET.setText(description);
        typeET.setText(type);
        freqET.setText(frequency);
        hideSoftKeyboard();
        createDB();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditActivity.this.startActivity(habitActivity);
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nameET.getText().toString();
                description = descET.getText().toString();
                type = typeET.getText().toString();
                frequency = freqET.getText().toString();

                query = "update habit set name = '" + name + "', description = '" + description + "', habitDate ='" + getCurrentDate() + "' where habitId = '" + id + "'";
                modifyRecord(query);
                query = "update details set type = '" + type + "', frequency = '" + frequency + "', habitDate ='" + getCurrentDate() + "' where habitId = '" + id + "'";
                modifyRecord(query);
                EditActivity.this.startActivity(habitActivity);
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