package com.example.habithero;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<String> {
    private Activity context;
    private ArrayList<String> mHabitName;
    private ArrayList<String> mHabitDesc;
    private ArrayList<Integer> mHabitCompl;
    public CustomAdapter(Activity context, ArrayList<String> habitName, ArrayList<String> habitDesc, ArrayList<Integer> habitCompl){
        super(context, R.layout.checkbox_list, habitName);
        this.mHabitName = habitName;
        this.mHabitDesc = habitDesc;
        this.mHabitCompl = habitCompl;
        this.context = context;
    }
    static class ViewHolder{
        TextView nameTxt;
        TextView descTxt;
        CheckBox compChk;
    }
    @Override
    public View getView(int position, @Nullable View rowView, @NonNull ViewGroup parent){
        ViewHolder habitVH = new ViewHolder();

        if(rowView == null)
        {
            LayoutInflater myInflater = context.getLayoutInflater();
            rowView = myInflater.inflate(R.layout.checkbox_list, parent, false);
            habitVH.nameTxt = rowView.findViewById(R.id.list_title);
            habitVH.descTxt = rowView.findViewById(R.id.list_subtext);
            habitVH.compChk = rowView.findViewById(R.id.list_checkbox);

            rowView.setTag(habitVH);
        }
        else {
            habitVH = (ViewHolder) rowView.getTag();
        }
        habitVH.nameTxt.setText(mHabitName.get(position));
        habitVH.descTxt.setText(mHabitDesc.get(position));
        if (mHabitCompl.get(position) == 0){
            habitVH.compChk.setChecked(false);
        }
        else if (mHabitCompl.get(position) == 1){
            habitVH.compChk.setChecked(true);
        }

        return rowView;
    }
}
