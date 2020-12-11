package com.javarush.games.spaceinvaders;

import com.javarush.engine.cell.*;
import com.javarush.games.spaceinvaders.gameobjects.Bullet;
import com.javarush.games.spaceinvaders.gameobjects.EnemyFleet;
import com.javarush.games.spaceinvaders.gameobjects.PlayerShip;
import com.javarush.games.spaceinvaders.gameobjects.Star;

import java.util.ArrayList;
import java.util.List;

public class SpaceInvadersGame extends Game {
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    public static final int COMPLEXITY = 5;

    private List<Star> stars;
    private EnemyFleet enemyFleet;
    private List<Bullet> enemyBullets;
    private List<Bullet> playerBullets;
    private PlayerShip playerShip;
    private  boolean isGameStopped;
    private int animationsCount;
    private static final int PLAYER_BULLETS_MAX = 1;
    private int score;
    @Override
    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }

    @Override
    public void onTurn(int step) {
        moveSpaceObjects();
        check();
//Если выстрел произошел,то пуля добавляется в список
        Bullet bullet = enemyFleet.fire(this);
        if (bullet != null) {
            enemyBullets.add(bullet);
        }
        setScore(score);

        drawScene();
    }

    private void createGame() {
        enemyFleet = new EnemyFleet();
        enemyBullets = new ArrayList<>();
        playerBullets = new ArrayList<>();
        playerShip = new PlayerShip();
        isGameStopped=false;
        animationsCount=0;
        score=0;
        createStars();
        drawScene();
        setTurnTimer(40);
    }

    private void drawScene() {
        drawField();
        enemyFleet.draw(this);
        playerShip.draw(this);

        for (Bullet bullet : enemyBullets) {
            bullet.draw(this);
        }
        for (Bullet bullet : playerBullets){
            bullet.draw(this);
        }
    }

    private void drawField() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                setCellValueEx(x, y, Color.BLACK, "");
            }
        }

        for (Star star : stars) {
            star.draw(this);
        }
    }

    private void createStars() {
        stars = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            int x = getRandomNumber(WIDTH);
            int y = getRandomNumber(HEIGHT);
            stars.add(new Star(x, y));
        }
    }

    private void moveSpaceObjects() {
        enemyFleet.move();
        playerShip.move();

        for (Bullet enemyBullet : enemyBullets) {
            enemyBullet.move();
        }
        for(Bullet playerBullet : playerBullets){
            playerBullet.move();
        }
    }
//Удаляет использованные пули или вышедшие за грани
    private void removeDeadBullets() {
        for (Bullet bullet : new ArrayList<>(enemyBullets)) {
            if (!bullet.isAlive || bullet.y >= HEIGHT - 1) {
                enemyBullets.remove(bullet);
            }
        }
        for (Bullet bullet : new ArrayList<>(playerBullets)){
            if(!bullet.isAlive || bullet.y + bullet.height<0){
                playerBullets.remove(bullet);
            }
        }
    }
    private void stopGame(boolean isWin){
        isGameStopped = true;
        stopTurnTimer();
        if(isWin){
            showMessageDialog(Color.NONE, "YOU ARE THE BEST!!!",Color.GREEN,50);
        }
        if(!isWin){
            showMessageDialog(Color.NONE,"OOOOPS!!!", Color.RED,50);
        }

    }
    private  void stopGameWithDelay(){
        animationsCount++;
        if(animationsCount>=10){
            stopGame(playerShip.isAlive);
        }
    }

    @Override

    public void onKeyPress(Key key) {
        //восстанавливает игру при нажатии на пробел ( если игра остановлена)
        if(key == Key.SPACE && isGameStopped==true){
            createGame();
        }
        //передвижение корабля при нажатии кнопок влево и вправо
        if ( key == Key.LEFT){
            playerShip.setDirection(Direction.LEFT);
        }
        if( key == Key.RIGHT){
            playerShip.setDirection(Direction.RIGHT);
        }
        //стрельба при нажатии на пробел
        if(key == Key.SPACE){
            Bullet bullet = playerShip.fire();
            if(bullet != null && playerBullets.size()<PLAYER_BULLETS_MAX){
                playerBullets.add(bullet);
            }
        }

    }

    @Override
    public void onKeyReleased(Key key) {
        if(key==Key.LEFT && playerShip.getDirection()==Direction.LEFT){
playerShip.setDirection(Direction.UP);
        }
        if(key==Key.RIGHT && playerShip.getDirection()==Direction.RIGHT){
            playerShip.setDirection(Direction.UP);
        }
    }

    @Override
    public void setCellValueEx(int x, int y, Color cellColor, String value) {
        if(x>WIDTH-1 || x<0 || y>HEIGHT-1 || y<0){
            return;
        }
        super.setCellValueEx(x, y, cellColor, value);
    }


    private void check() {
        playerShip.verifyHit(enemyBullets);
        enemyFleet.verifyHit(playerBullets);
        enemyFleet.deleteHiddenShips();
        //игрок проигрывает не только при попадании в него пулей, но если вражеские корабли приблизятся вплотную
        if(enemyFleet.getBottomBorder()>= playerShip.y){
            playerShip.kill();
        }
        //игрок побеждает, если убил все вражеские корабли
        if(enemyFleet.getShipsCount() == 0){
            playerShip.win();
            stopGameWithDelay();
        }
        removeDeadBullets();
        if(!playerShip.isAlive){
            stopGameWithDelay();
        }
        score+=enemyFleet.verifyHit(playerBullets);
    }
}
