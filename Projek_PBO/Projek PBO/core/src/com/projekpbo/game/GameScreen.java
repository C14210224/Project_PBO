package com.projekpbo.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.*;

import static com.projekpbo.game.MainGame.windowWidth;
import static com.projekpbo.game.MainGame.windowHeight;

import com.projekpbo.entities.*;

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
    private Texture playerSpriteSh;
    private Animation playerDefault;


    private ArrayList<Projectile> projectiles = new ArrayList<>();
    private int projectileWidth = 16;
    private Texture projectileSprite;

    private Texture wallSprite;

    private Animation ballonSprite;

    private Animation bulletSpeedPickupSprite;
    private Animation bulletRatePickupSprite;

    private ArrayList<Obstacle> obstacles = new ArrayList<>();
    private long lastObstacleSpawn;
    private double obsFrequency = 1.5;
    private int obstacleWidth = 64;
    private int obstacleHeight = 64;
    private Texture obstacleSpriteSheet;
    private Animation obstacleAnim;
    private int obstacleSpeed = 200;

    private Texture background;
    private Music gameScreenMusic;

    int wallFreq = 1; //frequency of wall spawning attempts
    int wallChance = 5; //int ranging from 0 to 100. describes the chance (%) of spawning a wall
    long lastWallSpawn = 0;
    public long score = 0;

    String playerSpriteShPath = "playerNew.png";
//    String playerSpriteShPath = "player-Sheet.png";
    private String playerJumpSprPath = "playerJump.png";
    private String playerFallSprPath = "playerFall.png";
    private String obstacleSpriteSheetPath = "enemy-bird-Sheet.png";
    private String projectileSpritePath = "heartAttack.png";
    private String backgroundPath = "background.png";
    private String wallSpritePath = "tembok.png";
    private String ballonSpritePath = "whiteBallon-animated.png";

    private String bulletSpeedPickupSpritePath = "BulletSpeedPickup.png";
    private String bulletRatePickupSpritePath = "BulletRatePickup.png";

    private String gameScreenMusicPath = "GSmusic.mp3";
    private String sfxPath = "";

    //Del later
    ShapeRenderer sr;

    GameScreen(final MainGame game) {
        this.game = game;
        playerJumpSpr = new Texture(playerJumpSprPath);
        playerFallSpr = new Texture(playerFallSprPath);
        obstacleSpriteSheet = new Texture(obstacleSpriteSheetPath);
        obstacleAnim = new Animation(obstacleSpriteSheet, 72, 72);
        background = new Texture(backgroundPath);
        playerSpriteSh = new Texture(playerSpriteShPath);
        playerDefault = new Animation(playerSpriteSh, 72, 72);
        projectileSprite = new Texture(projectileSpritePath);
        wallSprite = new Texture(wallSpritePath);
        ballonSprite = new Animation(new Texture(ballonSpritePath),100,100);

        bulletRatePickupSprite = new Animation(new Texture(bulletRatePickupSpritePath),64,64);
        bulletSpeedPickupSprite = new Animation(new Texture(bulletSpeedPickupSpritePath),64,64);
//        bgm = Gdx.audio.newMusic(Gdx.files.internal(bgmPath));
//        sfx = Gdx.audio.newSound(Gdx.files.internal(sfxPath));

//        bgm.setLooping(true);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, windowWidth, windowHeight);

        player = new Player();

        //del later
        sr = new ShapeRenderer();
    }


    @Override
    public void show() {
        gameScreenMusic = Gdx.audio.newMusic(Gdx.files.internal(gameScreenMusicPath));
        gameScreenMusic.setLooping(true);
        gameScreenMusic.play();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0,0,0.2f,1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        game.batch.draw(background, 0, 0, windowWidth, windowHeight);
        game.batch.draw(playerDefault.animate(), player.x, player.y);
        for(Iterator<Obstacle> iter = obstacles.iterator(); iter.hasNext();) {
            Obstacle obstacle = iter.next();

            if(obstacle instanceof BirdObstacle) {
                game.batch.draw(obstacleAnim.animate(), obstacle.x, obstacle.y);
            }
            else if(obstacle instanceof BulletSpeedPickup) {
                game.batch.draw(bulletSpeedPickupSprite.animate(), obstacle.x, obstacle.y, obstacleWidth, obstacleHeight); //projectileSprite used temporarily
            }
            else if(obstacle instanceof BulletRatePickup) {
                game.batch.draw(bulletRatePickupSprite.animate(), obstacle.x, obstacle.y, obstacleWidth, obstacleHeight); //projectileSprite used temporarily
            } else if(obstacle instanceof Wall) {
                game.batch.draw(wallSprite, obstacle.x, obstacle.y, Wall.wallWidth, ((Wall)obstacle).wallHeight); //projectilesprite used temporarily
            }
            else { //Default obstacle image
                game.batch.setColor(obstacle.color);
                game.batch.draw(ballonSprite.animate(), obstacle.x, obstacle.y - 32); //projectileSprite used temporarily
                game.batch.setColor(Color.WHITE);
            }
        }
        for(Iterator<Projectile> iter = projectiles.iterator(); iter.hasNext();) {
            Projectile projectile = iter.next();
            game.batch.draw(projectileSprite, projectile.x, projectile.y, projectile.width, projectile.height);
        }
        game.impactFont.setColor(Color.GRAY);
        game.impactFont.draw(game.batch, "SCORE: " + score, 10, windowHeight-10);

        game.batch.end();


        moveProjectile(delta);

        player.movePlayer(delta, camera, projectiles);

        generateObstacle(obsFrequency); //May or may not generate obstacles
        generateWall(wallFreq, wallChance);

        for(Iterator<Obstacle> iter = obstacles.iterator(); iter.hasNext(); ) {
            Obstacle obstacle = iter.next();

            obstacle.moveObstacle(delta);
            if(obstacle.x + obstacle.width < 0) {
                iter.remove();
            }

            if(obstacle.overlaps(player)) {
                if(obstacle instanceof PickUp) {
                    for(int i = 0; i < player.pickUps.size(); i++) {
                        if(obstacle.getClass() == player.pickUps.get(i).getClass()) {
                            player.pickUps.get(i).reverseEffect();
                            player.pickUps.remove(i);
                            i--;
                        }
                    }
                    player.pickUps.add((PickUp) obstacle);
                    ((PickUp) obstacle).pickedUp(player);
                    iter.remove();
                } else {
                    Obstacle.totalObstacleNum = 0; //resets number of obstacles spawned
                    gameScreenMusic.stop();
                    game.setScreen(new GameOver(this.game, score));
                }
            }
        }
        score += 1;

    }


    void moveProjectile(float delta) {
        for(int i = 0; i < projectiles.size(); i++) {
            projectiles.get(i).moveProjectile(delta);
            if(projectiles.get(i).x + projectileWidth < 0) {
                projectiles.remove(i);
                i--;
                continue;
            }

            if(projectiles.get(i).x > windowWidth) {
                projectiles.remove(i);
                i--;
                continue;
            }
            for(int j = 0; j < obstacles.size(); j++) {
                if(projectiles.get(i).overlaps(obstacles.get(j)) && !(obstacles.get(j) instanceof PickUp)) {
                    score += 100;
                    if(obstacles.get(j) instanceof BirdObstacle) {
                        score += 50;
                    }

                    if(obstacles.get(j) instanceof Wall) {
                        if(((Wall)(obstacles.get(j))).takeDamage()) {
                            obstacles.remove(j);
                            score += 400;
                        }
                    } else {
                        obstacles.remove(j);
                    }
                    projectiles.remove(i);
                    i--;
                    break;
                }
            }
        }
    }
    void generateWall(double freq, int chance) {
        if(TimeUtils.millis() - lastWallSpawn > 1000/freq) {
            int height = (int)MathUtils.random(Player.tmp.height * 3, windowHeight/2);
            if(MathUtils.random(0, 100) <= chance) {
                Wall wall = new Wall(Wall.wallWidth, height);
                wall.x = windowWidth;
                wall.y = MathUtils.random(0,2) == 0 ? 0 : windowHeight - height;
                obstacles.add(wall);
            }
            lastWallSpawn = TimeUtils.millis();
        }
    }

    void generateObstacle(double freq) {
        if(TimeUtils.millis() - lastObstacleSpawn > 1000/freq) {
            int amount = MathUtils.random(0, 3); //amount of obstacles to generate
            int obsGap = MathUtils.random(5, 20); //random gap between slots
            ArrayList<Integer> slots = new ArrayList<>();

            for(int i = 0; i <  windowHeight / (obstacleHeight + obsGap); i++) {
                slots.add(i);
            }

            for (int i = 0; i < amount; i++) {
                if(slots.size() == 0) continue;
                Obstacle obstacle = new Obstacle();

                if(MathUtils.random(0,10) > 8) {
                    obstacle = new BirdObstacle();
                } else {
                    if(MathUtils.random(0,100) >= 90) {
                        if(MathUtils.random(0, 100) >= 49) {
                            obstacle = new BulletRatePickup();
                        } else {
                            obstacle = new BulletSpeedPickup();
                        }
                    }
                }
                obstacle.width = obstacleWidth;
                obstacle.height = obstacleHeight;

                obstacle.x = windowWidth;
                int chosenSlot = MathUtils.random(0, slots.size()-1);
                obstacle.y = slots.get(chosenSlot) * (obstacleHeight + obsGap);

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
        gameScreenMusic.dispose();
    }
}
