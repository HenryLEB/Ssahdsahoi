package com.example.mygame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class GameView extends View {
    private static final int FLOOR = 0, WALL = 1, MAN = 4, TARGET = 3, BOX = 2, BOX_IN_TARGEET = 5,MAN_ON_TARGET = 6;
    private Paint mPaint = new Paint();
    private int level = 1;
    private  Rect srcFloorRect,srcWallRect,srcManRect,srcBoxRect,srcTargetRect, srcBoxInTargetRect;
    private Bitmap floorBitmap,wallBitmap,manBitmap,targetBitmap,boxBitmap, boxInTargetBitmap;
    private int[][] map =MapData.getMap(level);

    private Man man = new Man();
    public static int cellLength = 0;
    public OnWinListener onWinListener;
    //private int lastMapType = 0;
    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        wallBitmap= BitmapFactory.decodeResource(getResources(),R.drawable.wall);
        srcWallRect = new Rect(0,0,wallBitmap.getWidth(),wallBitmap.getHeight());

        floorBitmap= BitmapFactory.decodeResource(getResources(),R.drawable.floor);
        srcFloorRect = new Rect(0,0,floorBitmap.getWidth(),floorBitmap.getHeight());


        manBitmap= BitmapFactory.decodeResource(getResources(),R.drawable.man);
        srcManRect = new Rect(0,0,manBitmap.getWidth(),manBitmap.getHeight());

        targetBitmap= BitmapFactory.decodeResource(getResources(),R.drawable.target);
        srcTargetRect = new Rect(0,0,targetBitmap.getWidth(),targetBitmap.getHeight());

        boxBitmap= BitmapFactory.decodeResource(getResources(),R.drawable.box);
        srcBoxRect = new Rect(0,0,boxBitmap.getWidth(),boxBitmap.getHeight());

        boxInTargetBitmap= BitmapFactory.decodeResource(getResources(),R.drawable.redbox);
        srcBoxInTargetRect = new Rect(0,0,boxInTargetBitmap.getWidth(),boxInTargetBitmap.getHeight());

    }

    public void setOnWinListener(OnWinListener onWinListener){
        this.onWinListener = onWinListener;
    }

    public void setLevel(int level) {
        this.level = level;
        map = MapData.getMap(level);
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if(((width<height)&&map[0].length>map.length)||
                ((width>height)&&map[0].length<map.length))
        {
            map=mapRotate(map);
        }


        cellLength = width / map[0].length;
        mPaint.setColor(Color.RED);
        for (int i = 0; i <= map.length; i++) {
            canvas.drawLine(0, cellLength * i, cellLength * map[0].length, cellLength * i, mPaint);
        }

        for (int i = 0; i <= map.length; i++) {
            canvas.drawLine(cellLength * i, 0, cellLength * i, cellLength * map.length, mPaint);
        }
        int boxNum = 0;
        for(int i=0;i<map.length;i++)
        {
            for(int j=0;j<map[0].length;j++)
            {
                Rect dest = new Rect(cellLength * j,cellLength * i,cellLength * (j+1),cellLength * (i+1));

                if(map[i][j]==FLOOR)
                {
                    canvas.drawBitmap(floorBitmap,srcFloorRect,dest,mPaint);
                }
                else if(map[i][j]==WALL)
                {
                    canvas.drawBitmap(wallBitmap,srcWallRect,dest,mPaint);
                }
                else if(map[i][j]==MAN || map[i][j] == MAN_ON_TARGET)
                {
                    canvas.drawBitmap(manBitmap,srcManRect,dest,mPaint);
                    man.setRow(i);
                    man.setCol(j);
                }
                else if(map[i][j]==TARGET)
                {
                    canvas.drawBitmap(targetBitmap,srcTargetRect,dest,mPaint);
                }
                else if(map[i][j]==BOX)
                {
                    canvas.drawBitmap(boxBitmap,srcBoxRect,dest,mPaint);
                    boxNum++;
                }
                else if (map[i][j] == BOX_IN_TARGEET)
                {
                    canvas.drawBitmap(boxInTargetBitmap, srcBoxInTargetRect, dest, mPaint);
                }
            }
        }
        checkWin(boxNum);
    }
    private void checkWin(int boxNum){
        if(boxNum == 0){
            if(onWinListener != null){
                onWinListener.onWin();
            }
        }
    }
    private int[][] mapRotate(int[][]map)
    {
        int[][] temp = new int[map[0].length][map.length];

        for(int i=0;i<map.length;i++)
        {
            for(int j=0;j<map[0].length;j++)
            {
                temp[j][i]=map[i][j];
            }
        }

        return temp;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() != MotionEvent.ACTION_DOWN){
            return true;
        }
        int touchX = (int) event.getX();
        int touchY = (int) event.getY();

        Rect[] surroundRects = man.getSurroundRects();
        int direction = 0;
        for( ; direction<4; direction++){
            if(surroundRects[direction].contains(touchX, touchY)){
                break;
            }
        }
        if (direction == 4){
            return  true;
        }
        int [][] offset = new int[][]{
                {-1,0},
                {0,1},
                {1,0},
                {0,-1}
        };
        int touchRow = man.getRow() + offset[direction][0];
        int touchCol = man.getCol() + offset[direction][1];

        if (map[touchRow][touchCol] == FLOOR){
            map[touchRow][touchCol] = MAN;
            if(map[man.getRow()][man.getCol()] == MAN){
                map[man.getRow()][man.getCol()] = FLOOR;
            }else if(map[man.getRow()][man.getCol()] == MAN_ON_TARGET){
                map[man.getRow()][man.getCol()] = TARGET;
            }

        }   else if (map[touchRow][touchCol] == TARGET) {
            map[touchRow][touchCol] = MAN_ON_TARGET;
            if (map[man.getRow()][man.getCol()] == MAN) {
                map[man.getRow()][man.getCol()] = FLOOR;
            } else if (map[man.getRow()][man.getCol()] == MAN_ON_TARGET) {
                map[man.getRow()][man.getCol()] = TARGET;
            }
        }   else if (map[touchRow][touchCol] == BOX){
            int boxMoveToRow = touchRow + offset[direction][0];
            int boxMoveToCol = touchCol + offset[direction][1];

            if (map[boxMoveToRow][boxMoveToCol] == FLOOR){
                map[touchRow][touchCol] = MAN;
                map[boxMoveToRow][boxMoveToCol] = BOX;
                if (map[man.getRow()][man.getCol()] == MAN) {
                    map[man.getRow()][man.getCol()] = FLOOR;
                } else if (map[man.getRow()][man.getCol()] == MAN_ON_TARGET) {
                    map[man.getRow()][man.getCol()] = TARGET;
                }

            }else if (map[boxMoveToRow][boxMoveToCol] == TARGET){
                map[boxMoveToRow][boxMoveToCol] = BOX_IN_TARGEET;
                map[touchRow][touchCol] = MAN;
                if (map[man.getRow()][man.getCol()] == MAN) {
                    map[man.getRow()][man.getCol()] = FLOOR;
                } else if (map[man.getRow()][man.getCol()] == MAN_ON_TARGET) {
                    map[man.getRow()][man.getCol()] = TARGET;
                }
            }

        }else if (map[touchRow][touchCol] == BOX_IN_TARGEET){
            int boxMoveToRow = touchRow + offset[direction][0];
            int boxMoveToCol = touchCol + offset[direction][1];

            if(map[boxMoveToRow][boxMoveToCol] == FLOOR){
                map[boxMoveToRow][boxMoveToCol] = BOX;
                map[touchRow][touchCol] = MAN_ON_TARGET;
                if (map[man.getRow()][man.getCol()] == MAN) {
                    map[man.getRow()][man.getCol()] = FLOOR;
                } else if (map[man.getRow()][man.getCol()] == MAN_ON_TARGET) {
                    map[man.getRow()][man.getCol()] = TARGET;
                }
            }else if(map[boxMoveToRow][boxMoveToCol] == TARGET){
                map[boxMoveToRow][boxMoveToCol] = BOX_IN_TARGEET;
                map[touchRow][touchCol] = MAN_ON_TARGET;
                if (map[man.getRow()][man.getCol()] == MAN) {
                    map[man.getRow()][man.getCol()] = FLOOR;
                } else if (map[man.getRow()][man.getCol()] == MAN_ON_TARGET) {
                    map[man.getRow()][man.getCol()] = TARGET;
                }

            }

        }
        postInvalidate();
       // Toast.makeText(getContext(), "onTouchEvent", Toast.LENGTH_SHORT).show();
        return true;
    }

    public interface OnWinListener{
        void onWin();
    }
}

