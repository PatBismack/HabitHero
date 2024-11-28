package com.example.habithero;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

public class DetailsActivity extends AppCompatActivity {
    public static SQLiteDatabase db;
    DBHelper myDbHelper;

    TextView nameTV;
    TextView descTV;
    TextView typeTV;
    TextView freqTV;
    TextView complTV;
    
    ImageButton backBtn;
    ImageButton deleteBtn;
    ImageButton editBtn;

    Intent detailActivity;
    Intent habitActivity;

    String name;
    String description;
    String type;
    String frequency;
    String deleteQuery;

    int completed;
    int id;

    Intent habitEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nameTV = findViewById(R.id.habit_title);
        descTV = findViewById(R.id.descTxt);
        typeTV = findViewById(R.id.typeTxt);
        freqTV = findViewById(R.id.freqTxt);
        complTV = findViewById(R.id.complTxt);
        backBtn = findViewById(R.id.detailsBackBtn);
        deleteBtn = findViewById(R.id.habitTrashBtn);
        editBtn = findViewById(R.id.editBtn);

        habitActivity = new Intent(this, HabitActivity.class);
        habitEdit = new Intent(this, EditActivity.class);
        detailActivity = getIntent();
        name = detailActivity.getStringExtra("habitName");
        description = detailActivity.getStringExtra("habitDesc");
        completed = detailActivity.getIntExtra("habitCompl", 0);
        id = detailActivity.getIntExtra("habitId", 0);
        type = detailActivity.getStringExtra("habitType");
        frequency = detailActivity.getStringExtra("habitFreq");

        nameTV.setText(name);
        descTV.setText(description);
        if(completed == 0){
            complTV.setText("No");
        }
        else if (completed == 1){
            complTV.setText("Yes");
        }
        typeTV.setText(type);
        freqTV.setText(frequency);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailsActivity.this.startActivity(habitActivity);
            }
        });
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                habitEdit.putExtra("habitName", name);
                habitEdit.putExtra("habitDesc", description);
                habitEdit.putExtra("habitCompl", completed);
                habitEdit.putExtra("habitId", id);
                habitEdit.putExtra("habitType", type);
                habitEdit.putExtra("habitFreq", frequency);
                DetailsActivity.this.startActivity(habitEdit);
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
                createDB();
                deleteQuery = "delete from habit WHERE habitId='" + id + "'";
                modifyRecord(deleteQuery);
                deleteQuery = "delete from details WHERE habitId='" + id + "'";
                modifyRecord(deleteQuery);
                DetailsActivity.this.startActivity(habitActivity);
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
}