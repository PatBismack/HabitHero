package com.example.habithero;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;
public class ListAdapter extends BaseAdapter {

    private final Context context;
    private final List<ListItem> items;
    private final LayoutInflater inflater;

    public ListAdapter(Context context, List<ListItem> items) {
        this.context = context;
        this.items = items;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View listRow, ViewGroup parent) {
        ViewHolder holder;
        if (listRow == null) {
            listRow = inflater.inflate(R.layout.checkbox_list, parent, false);
            holder = new ViewHolder();
            holder.checkBox = listRow.findViewById(R.id.list_checkbox);
            holder.text1 = listRow.findViewById(R.id.list_title);
            holder.text2 = listRow.findViewById(R.id.list_subtext);
            listRow.setTag(holder);
        } else {
            holder = (ViewHolder) listRow.getTag();
        }

        ListItem item = items.get(position);


        holder.checkBox.setChecked(item.isChecked());
        holder.text1.setText(item.getTitle());
        holder.text2.setText(item.getDescription());


        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setChecked(isChecked);
        });

        return listRow;
    }

    static class ViewHolder {
        CheckBox checkBox;
        TextView text1;
        TextView text2;
    }

}
