package com.projekpbo.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import static com.projekpbo.game.MainGame.windowWidth;
import static com.projekpbo.game.MainGame.windowHeight;

public class MainMenu implements Screen {

    MainGame game;
    OrthographicCamera camera;
    private Texture background;
    private Texture title;
    private Texture startButton;

    private String backgroundPath = "background.png";
    private String titlePath = "title.png";
    private String startButtonPath = "play.png";
    private String bgmPath = "";

    MainMenu(final MainGame game) {
        this.game = game;
        background = new Texture(backgroundPath);
        title = new Texture(titlePath);
        startButton = new Texture(startButtonPath);
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
        game.batch.draw(title, (windowWidth / 2) - 250, windowHeight / 2 + 100, 500, 150);
        game.batch.draw(startButton, (windowWidth / 2) - (startButton.getWidth() / 2), (windowHeight / 2) - (startButton.getHeight() / 2) - 100);
        game.batch.end();

        if (Gdx.input.isTouched()) {
//            System.out.println(Gdx.input.getX());
//            System.out.println(Gdx.input.getY());
            if (Gdx.input.getX() >= 286 && Gdx.input.getX() <= 509 && Gdx.input.getY() >= 457 && Gdx.input.getY() <= 532) {
                game.setScreen(new GameScreen(this.game));
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
