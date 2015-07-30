package com.koda.lingo.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.koda.lingo.Lingo;
import com.koda.lingo.internal.GameState;
import com.koda.lingo.internal.StateManager;

public class MenuState extends GameState {

    //TODO: This batch and camera are for the Lingo title, not the UI
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Skin skin;
    private Stage stage;
    private Table table;
    private TextButton playButton;
    private TextButton highScoreButton;
    private TextButton quitButton;

    public MenuState() {
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        skin.addRegions(new TextureAtlas(Gdx.files.internal("ui/uiskin.atlas")));
        stage = new Stage(new ScalingViewport(Scaling.fit, Lingo.SCREEN_WIDTH, Lingo.SCREEN_HEIGHT));
        table = new Table();

        playButton = new TextButton("Play", skin);
        highScoreButton = new TextButton("High Scores", skin);
        quitButton = new TextButton("Quit", skin);

        table.setFillParent(true);
        table.center();
        table.add(playButton).padBottom(10).colspan(10).fill().row();
        table.add(highScoreButton).padBottom(10).colspan(10).fill().row();
        table.add(quitButton).colspan(10).fill();

        stage.addActor(table);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Lingo.SCREEN_WIDTH, Lingo.SCREEN_HEIGHT);
        camera.update();
        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);

        //the listeners
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Lingo.log("Play button clicked");
                StateManager.setState(StateManager.PLAY_STATE);
            }
        });

        highScoreButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Lingo.log("High Score button clicked");
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public void update(float dt) {
        stage.act(dt);
    }

    @Override
    public void render(SpriteBatch sb) {
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void dispose() {
        skin.dispose();
        stage.dispose();
        batch.dispose();
    }
}
