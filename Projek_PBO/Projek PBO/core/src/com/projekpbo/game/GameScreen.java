package com.projekpbo.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.*;

import static com.projekpbo.game.MainGame.windowWidth;
import static com.projekpbo.game.MainGame.windowHeight;

import com.projekpbo.entities.Obstacle;
import com.projekpbo.entities.Player;
import com.projekpbo.entities.Projectile;

//Test Commit lol
//jannnnccooookkkkk
public class GameScreen implements Screen {
    MainGame game;
    private OrthographicCamera camera;

    private Music bgm;
    private Sound sfx;

    private Player player;

    private Texture playerJumpSpr;
    private Texture playerFallSpr;


    private ArrayList<Projectile> projectiles = new ArrayList<>();
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
    private Texture obstacleSprite;
    private int obstacleSpeed = 200;

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
        if((player).state == Player.State.JUMPING) game.batch.draw(playerJumpSpr, player.x, player.y);
        else game.batch.draw(playerFallSpr, player.x, player.y);
        for(Iterator<Obstacle> iter = obstacles.iterator(); iter.hasNext();) {
            Obstacle obstacle = iter.next();
            game.batch.draw(obstacleSprite, obstacle.x, obstacle.y, obstacle.width, obstacle.height);
        }
        for(Iterator<Projectile> iter = projectiles.iterator(); iter.hasNext();) {
            Projectile projectile = iter.next();
            game.batch.draw(obstacleSprite, projectile.x, projectile.y, projectile.width, projectile.height);
        }
        game.font.draw(game.batch, "Score: " + score, 10, windowHeight-10);

        game.batch.end();

        //Input


        moveProjectile(delta);

        player.movePlayer(delta, camera, projectiles);



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

    void moveProjectile(float delta) {
        for(int i = 0; i < projectiles.size(); i++) {
            projectiles.get(i).moveProjectile(delta);
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
