package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.engine.cell.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ship extends GameObject {
    public boolean isAlive = true;
    /* отвечает за бесконечность анимации, должно быть правдивым только для босса, значение устанавливает
    isLoopAnimation ,пришлось отредактировать метод setAnimatedView с учетом данной переменной
     */
    private boolean loopAnimation=false;


    public Ship(double x, double y) {
        super(x, y);
    }


    public void setStaticView(int[][] viewFrame) {
        setMatrix(viewFrame);
        frames = new ArrayList<int[][]>();
        frames.add(viewFrame);
        frameIndex = 0;
    }

    public Bullet fire() {
        return null;
    }

    public void kill() {
        isAlive = false;
    }

    //список матриц для кадров анимации взрыва
    private List<int[][]> frames;
    //индекс текущего кадра анимации
    private int frameIndex;

    //передаем кадры анимаций
   /* public void setAnimatedView(int[][]... viewFrames) {
        setMatrix(viewFrames[0]);

        frames = Arrays.asList(viewFrames);
        frameIndex = 0;
    }*/

    //устанавливает в поле matrix следующий кадр анимации, если это возможно
    public void nextFrame() {
        frameIndex++;
        if (frameIndex >= frames.size() && loopAnimation==false) {
            return;
         }
        if(frameIndex >= frames.size() && loopAnimation==true){
            frameIndex=0;
        }
        matrix = frames.get(frameIndex);
    }

    public void draw(Game game) {
        super.draw(game);
        nextFrame();
    }

    public boolean isVisible() {
        if (!isAlive && frameIndex >= frames.size()) {
            return false;
        }
        return true;
    }
 public void setAnimatedView(boolean isLoopAnimation, int[][]... viewFrames){
        loopAnimation = isLoopAnimation;
 }
}
