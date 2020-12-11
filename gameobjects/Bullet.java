package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.games.spaceinvaders.Direction;
import com.javarush.games.spaceinvaders.ShapeMatrix;

public class Bullet extends GameObject{
private int dy;
public boolean isAlive=true;
public void move(){
     y += dy;
}
    public Bullet(double x, double y, Direction direction) {
        super(x,y);
    setMatrix(ShapeMatrix.BULLET);
    this.dy=direction==Direction.UP  ? -1 : 1;
    }
    public void kill() {
    isAlive=false;
    }
}
