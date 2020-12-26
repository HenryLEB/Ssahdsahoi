package com.example.mygame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class GameActivity extends AppCompatActivity {
    private int level;
    private TextView tvTitle;
    private GameView gv;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        level = getIntent().getIntExtra("position", 1);
        initViews();
        gv.setLevel(level);
        initEvents();
    }
    public void initEvents(){
        gv.setOnWinListener(new GameView.OnWinListener() {
            @Override
            public void onWin() {
                if(level < 4) {
                    new AlertDialog.Builder(GameActivity.this)
                            .setTitle("推箱子游戏")
                            .setMessage("完成任务！恭喜恭喜！")
                            .setPositiveButton("下一关", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    level++;
                                    tvTitle.setText("推箱子游戏--第" + String.valueOf(level) + "关");
                                    gv.setLevel(level);
                                    invalidateOptionsMenu();
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .show();
                }else if (level >= 4){
                    new AlertDialog.Builder(GameActivity.this)
                            .setTitle("推箱子游戏")
                            .setMessage("完成任务！恭喜恭喜！")
                            .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .show();
                }
            }
        });
    }
    private void initViews() {
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("推箱子游戏--第" + String.valueOf(level) + "关");
        gv = findViewById(R.id.gv);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        if(level == 1){
            menu.getItem(1).setVisible(false);
        }else if (level == 4){
            menu.getItem(2).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_restart:
                gv.setLevel(level);
            break;
            case R.id.item_previous:
                level--;
                tvTitle.setText("推箱子游戏--第" + String.valueOf(level) + "关");
                gv.setLevel(level);
                invalidateOptionsMenu();
                break;
            case R.id.item_next:
                level++;
                tvTitle.setText("推箱子游戏--第" + String.valueOf(level) + "关");
                gv.setLevel(level);
                invalidateOptionsMenu();
                break;
                default:
                    break;
        }
        return super.onOptionsItemSelected(item);
    }
}

