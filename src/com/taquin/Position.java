package com.taquin;

public class Position {
    private final int x,y;

    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean equals(Position p) {
        return (p.x == x && p.y == y);
    }

}
