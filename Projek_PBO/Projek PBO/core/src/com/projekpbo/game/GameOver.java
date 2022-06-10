package com.projekpbo.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import static com.projekpbo.game.MainGame.windowWidth;
import static com.projekpbo.game.MainGame.windowHeight;

public class GameOver implements Screen {

    MainGame game;
    OrthographicCamera camera;
    long score;

    GameOver(final MainGame game, long score) {
        this.game = game;
        this.score = score;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, windowWidth, windowHeight);
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
        game.font.draw(game.batch, "Your Score: " + score, windowWidth/2, windowHeight/2);
        game.batch.end();
        if(Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(this.game));
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
