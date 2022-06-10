package com.projekpbo.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.sql.Time;
import java.util.*;

import static com.projekpbo.game.MainGame.windowWidth;
import static com.projekpbo.game.MainGame.windowHeight;
//Test Commit lol
public class GameScreen implements Screen {
    MainGame game;
    private OrthographicCamera camera;

    private Music bgm;
    private Sound sfx;

    private Rectangle player;
    private int playerWidth = 64;
    private int playerHeight = 64;
    private Texture playerJumpSpr;
    private Texture playerFallSpr;
    private double acceleration = 1000;
    private double velocity = 0;
    private double addedVelocity = 42;

    private ArrayList<Rectangle> projectiles = new ArrayList<>();
    private int projectileHeight = 16;
    private int projectileWidth = 16;
    private int projectileSpeed = 200;
    private double projectileFreq = 1;
    private long lastProjectileTime;

    private ArrayList<Obstacle> obstacles = new ArrayList<>();
    private long lastObstacleSpawn;
    private double obsFrequency = 1.5;
    private int obstacleWidth = 64;
    private int obstacleHeight = 64;
    private int gap = (int)(playerHeight * 3.5);
    private Texture obstacleSprite;
    private int obstacleSpeed = 200;
    private long timeToNextSpawn = 1500;
    private int offset = playerHeight;

    private Texture background;

    public long score = 0;

    private String playerJumpSprPath = "playerJump.png";
    private String playerFallSprPath = "playerFall.png";
    private String obstacleSpritePath = "wall.png";
    private String backgroundPath = "background.png";
    private String bgmPath = "";
    private String sfxPath = "";

    GameScreen(final MainGame game) {
        this.game = game;
        playerJumpSpr = new Texture(playerJumpSprPath);
        playerFallSpr = new Texture(playerFallSprPath);
        obstacleSprite = new Texture(obstacleSpritePath);
        background = new Texture(backgroundPath);
//        bgm = Gdx.audio.newMusic(Gdx.files.internal(bgmPath));
//        sfx = Gdx.audio.newSound(Gdx.files.internal(sfxPath));

//        bgm.setLooping(true);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, windowWidth, windowHeight);

        player = new Player();
        player.x = (int)(windowWidth*0.3) - (playerWidth/2);
        player.y = windowHeight/2 - playerHeight/2;
        player.width = playerWidth;
        player.height = playerHeight;

    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0,0,0.2f,1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        game.batch.draw(background, 0, 0, windowWidth, windowHeight);
        if(((Player)player).status == State.JUMPING) game.batch.draw(playerJumpSpr, player.x, player.y);
        else game.batch.draw(playerFallSpr, player.x, player.y);

        for(Iterator<Obstacle> iter = obstacles.iterator(); iter.hasNext();) {
            Obstacle obstacle = iter.next();
            game.batch.draw(obstacleSprite, obstacle.x, obstacle.y, obstacle.width, obstacle.height);
        }
        for(Iterator<Rectangle> iter = projectiles.iterator(); iter.hasNext();) {
            Rectangle projectile = iter.next();
            game.batch.draw(obstacleSprite, projectile.x, projectile.y, projectile.width, projectile.height);
        }
        game.font.draw(game.batch, "Score: " + score, 10, windowHeight-10);

        game.batch.end();

        //Input
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            ((Player)player).status = State.JUMPING;
            velocity -= addedVelocity;
        } else ((Player)player).status = State.FALLING;

        if(Gdx.input.isKeyPressed(Input.Keys.ENTER) && (TimeUtils.millis() - lastProjectileTime) > (1000/projectileFreq)) {
            spawnProjectile(player.x, player.y);
            lastProjectileTime = TimeUtils.millis();
        }

        moveProjectile(delta);


        //Physics
        velocity += acceleration * delta;
        player.y -= velocity * delta;

        if(player.y < 0) {
            player.y = 0;
            velocity = 0;
        }
        if(player.y > windowHeight-playerHeight) {
            player.y = windowHeight-playerHeight;
            velocity = 0;
        }

        generateObstacle(obsFrequency); //May or may not generate obstacles

        for(Iterator<Obstacle> iter = obstacles.iterator(); iter.hasNext(); ) {
            Obstacle obstacle = iter.next();

            obstacle.x -= obstacleSpeed * delta;

            if(obstacle.overlaps(player)) {
                Obstacle.totalObstacleNum = 0; //resets number of obstacles spawned
                game.setScreen(new GameOver(this.game, score));
            }

            if(player.x > obstacle.x+obstacleWidth) score = obstacle.getObstacleNum()/2;
        }

    }

    void spawnProjectile(float x, float y) {
        Rectangle projectile = new Rectangle();
        projectile.x = x;
        projectile.y = y;
        projectile.width = projectileWidth;
        projectile.height = projectileHeight;

        projectiles.add(projectile);
    }

    void moveProjectile(float delta) {
        for(int i = 0; i < projectiles.size(); i++) {
            projectiles.get(i).x += projectileSpeed * delta;
            for(int j = 0; j < obstacles.size(); j++) {
                if(projectiles.get(i).overlaps(obstacles.get(j))) {
                    projectiles.remove(i);
                    obstacles.remove(j);
                    i--;
                    break;
                }
            }
        }
    }

    void generateObstacle(double freq) {
        if(TimeUtils.millis() - lastObstacleSpawn > 1000/freq) {
            int amount = MathUtils.random(0, 3); //amount of obstacles to generate
            int obsGap = MathUtils.random(5, 20);
            ArrayList<Integer> slots = new ArrayList<>();

            for(int i = 0; i <  windowHeight / (obstacleHeight + obsGap); i++) {
                slots.add(i);
            }

            for (int i = 0; i < amount; i++) {
                if(slots.size() == 0) continue;
                Obstacle obstacle = new Obstacle();

                obstacle.width = obstacleWidth;
                obstacle.height = obstacleHeight;

                obstacle.x = windowWidth;
                int chosenSlot = MathUtils.random(0, slots.size()-1);
                obstacle.y = chosenSlot * (obstacleHeight + obsGap);

                slots.remove(chosenSlot);

                obstacles.add(obstacle);
            }

            lastObstacleSpawn = TimeUtils.millis();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
