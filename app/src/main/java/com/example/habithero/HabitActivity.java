package com.example.habithero;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class HabitActivity extends AppCompatActivity {

    ListView habitList;

    // testing strings
    String[] titles = {"Listen to Podcast","Take a Walk", "Wake up before 6:00am", "Yoga"};
    String[] desc = {"Take time to listen to an insightful podcast, whether it's on personal growth, current events, or a new topic you're curious about. A great way to learn something new and stay inspired while doing everyday tasks!"
            , "lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum", "lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum", "lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum"};


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

        habitList = findViewById(R.id.habit_list);

        List<ListItem> itemList = new ArrayList<>();

        // creating list items and adding them to the list, then creating and setting the adapter
        for (int i = 0; i < titles.length; i++){
            itemList.add(new ListItem(false,titles[i], desc[i]));
        }
        ListAdapter adapter = new ListAdapter(this, itemList);
        habitList.setAdapter(adapter);
    }
}