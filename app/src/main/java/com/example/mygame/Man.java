package com.example.mygame;

import android.graphics.Rect;

public class Man {
    private int row = 0, col = 0;

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public Rect[] getSurroundRects(){
        Rect[] rects = new Rect[4];
        int [][] offset = new int[][]{
                {-1,0},
                {0,1},
                {1,0},
                {0,-1}
        };
        for(int i = 0; i < 4; i++){
            int r = row + offset[i][0];
            int c = col + offset[i][1];
            Rect rect = new Rect(GameView.cellLength * c,GameView.cellLength * r,GameView.cellLength * (c+1),GameView.cellLength * (r+1));
            rects[i] = rect;
        }

        return rects;
    }
}
