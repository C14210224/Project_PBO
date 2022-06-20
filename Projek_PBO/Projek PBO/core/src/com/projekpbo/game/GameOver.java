package com.projekpbo.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

import static com.projekpbo.game.MainGame.windowWidth;
import static com.projekpbo.game.MainGame.windowHeight;

public class GameOver implements Screen {

    MainGame game;
    OrthographicCamera camera;
    long score;
    private Texture background;
    private Texture restart;
    private Texture restartHover;
    private Texture exit;
    private Texture exitHover;

    private String backgroundPath = "gameOver back.png";
    private String restartPath = "restartButton1.png";
    private String restartHoverPath = "restartButton2.png";
    private String exitPath = "exitButton1.png";
    private String exitHoverPath = "exitButton2.png";

    GameOver(final MainGame game, long score) {
        this.game = game;
        this.score = score;
        background = new Texture(backgroundPath);
        restart = new Texture(restartPath);
        restartHover = new Texture(restartHoverPath);
        exit = new Texture(exitPath);
        exitHover = new Texture(exitHoverPath);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, windowWidth, windowHeight);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(background, 0, 0, windowWidth, windowHeight);
//        game.batch.draw(gameover, (windowWidth / 2) - 250, windowHeight / 2 + 100, 500, 150);
        game.impactFont.setColor(Color.GRAY);
        if (score < 1000) {
            game.impactFont.draw(game.batch, "YOUR SCORE: " + score, (windowWidth / 2) - 110, windowHeight / 2 + 100);
        } else {
            int finalScore = (int)score;
            int n = -1;
            while (finalScore/10 > 0) {
                finalScore /= 10;
                n++;
            }
//            System.out.println(n);
//            System.out.println(100 + (n * 75 / 10));
            game.impactFont.draw(game.batch, "YOUR SCORE: " + score, (windowWidth / 2) - (100 + (n * 75 / 10)), windowHeight / 2 + 100);
        }
//        game.font.draw(game.batch, "Your Score: " + score, (windowWidth / 2) - 50, windowHeight / 2 + 30);
        game.batch.draw(restart, (windowWidth / 2) - (restart.getWidth() / 2), (windowHeight / 2) - (restart.getHeight() / 2) - 100);
        game.batch.draw(exit, (windowWidth / 2) - (exit.getWidth() / 2), (windowHeight / 2) - (exit.getHeight() / 2) - 200);
        game.batch.end();

        if (Gdx.input.getX() >= 286 && Gdx.input.getX() <= 509 && Gdx.input.getY() >= 457 && Gdx.input.getY() <= 532) {
            game.batch.begin();
            game.batch.draw(restartHover, (windowWidth / 2) - (restart.getWidth() / 2), (windowHeight / 2) - (restart.getHeight() / 2) - 100);

            game.batch.end();
        }
        if (Gdx.input.getX() >= 286 && Gdx.input.getX() <= 509 && Gdx.input.getY() >= 562 && Gdx.input.getY() <= 633) {
            game.batch.begin();
            game.batch.draw(exitHover, (windowWidth / 2) - (exit.getWidth() / 2), (windowHeight / 2) - (exit.getHeight() / 2) - 200);
            game.batch.end();
        }

        if (Gdx.input.isTouched()) {
//            System.out.println(Gdx.input.getX());
//            System.out.println(Gdx.input.getY());
            if (Gdx.input.getX() >= 286 && Gdx.input.getX() <= 509 && Gdx.input.getY() >= 457 && Gdx.input.getY() <= 532) {
                game.setScreen(new GameScreen(this.game));
            }
            if (Gdx.input.getX() >= 286 && Gdx.input.getX() <= 509 && Gdx.input.getY() >= 562 && Gdx.input.getY() <= 633) {
                game.setScreen(new MainMenu(this.game));
            }
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
