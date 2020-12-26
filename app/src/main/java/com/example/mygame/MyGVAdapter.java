package com.example.mygame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MyGVAdapter extends BaseAdapter {

    private Context context;
    private List<String> levels;
    public MyGVAdapter(Context context, List<String> levels){
        this.context = context;
        this.levels = levels;
    }

    @Override
    public int getCount() {
        return levels.size();
    }

    @Override
    public String getItem(int position) {
        return levels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.gv_item, parent, false);
        }
        TextView textView = convertView.findViewById(R.id.tv_level);
        textView.setText(getItem(position));
        return convertView;
    }
}
