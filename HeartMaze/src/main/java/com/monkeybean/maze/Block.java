package com.monkeybean.maze;

/**
 * Created by MonkeyBean on 2018/9/6.
 */
public class Block {
    private int x;
    private int y;
    private int di;

    public Block() {
    }

    public Block(int x, int y, int di) {
        this.x = x;
        this.y = y;
        this.di = di;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDi() {
        return di;
    }

    public void setDi(int di) {
        this.di = di;
    }
}
