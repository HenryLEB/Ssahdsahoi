package com.example.mygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class LevelActivity extends AppCompatActivity {
    private GridView gvLevel;
    private List<String> levels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        initViews();
        initEvents();
    }

    private void initViews(){
        gvLevel = findViewById(R.id.gv_level);
        for (int i = 0; i<MapData.MAX_LEVEL; i++){
            levels.add("第" + String.valueOf(i + 1) + "关");
        }

      /*levels.add("第一关");
        levels.add("第二关");
        levels.add("第三关");
        levels.add("第四关");*/

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.gv_item, R.id.tv_level, levels);
    //    MyGVAdapter adapter = new MyGVAdapter(this, levels);
        gvLevel.setAdapter(adapter);
    }
    private void initEvents(){
        gvLevel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LevelActivity.this, GameActivity.class);
                intent.putExtra("position", position+1);
                startActivity(intent);
            }
        });
    }
}
