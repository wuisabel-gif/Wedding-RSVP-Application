package view;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

/**
 * A PowerPoint-style wedding slideshow: photos fade from one to the next on a
 * timer, with a gentle Ken Burns drift and looping wedding music. Opened from the
 * "See Album" button — there are no thumbnails and no captions.
 */
public final class AlbumSlideshow {

    private static final String[] PHOTOS = {
            "couple.png", "bride.png", "groom.png", "bridesmaids.png", "families.png"
    };
    private static final Duration SLIDE_INTERVAL = Duration.seconds(5);
    private static final Duration FADE = Duration.seconds(1.1);

    private final Image[] images;
    private final ImageView[] layers = new ImageView[2];
    private int active = 0;
    private int index = 0;
    private boolean playing = true;

    private Timeline autoAdvance;
    private MediaPlayer player;

    private AlbumSlideshow() {
        images = new Image[PHOTOS.length];
        for (int i = 0; i < PHOTOS.length; i++) {
            var url = getClass().getResource("/images/album/" + PHOTOS[i]);
            images[i] = url == null ? null : new Image(url.toExternalForm());
        }
    }

    public static void open(Window owner) {
        new AlbumSlideshow().show(owner);
    }

    private void show(Window owner) {
        StackPane stage = new StackPane();
        stage.getStyleClass().add("slideshow-root");

        for (int i = 0; i < 2; i++) {
            ImageView view = new ImageView();
            view.setPreserveRatio(true);
            view.fitWidthProperty().bind(stage.widthProperty().subtract(120));
            view.fitHeightProperty().bind(stage.heightProperty().subtract(150));
            view.setOpacity(0);
            layers[i] = view;
            stage.getChildren().add(view);
        }

        Button prev = controlButton("‹");
        Button playPause = controlButton("❚❚");
        Button next = controlButton("›");
        Button close = controlButton("✕");

        HBox controls = new HBox(14, prev, playPause, next, close);
        controls.getStyleClass().add("slideshow-controls");
        controls.setAlignment(Pos.CENTER);
        controls.setMaxHeight(60);
        StackPane.setAlignment(controls, Pos.BOTTOM_CENTER);
        stage.getChildren().add(controls);

        Stage window = new Stage();
        if (owner != null) {
            window.initOwner(owner);
        }
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Wedding Album");
        Scene scene = new Scene(stage, 1160, 780);
        scene.getStylesheets().add(getClass().getResource("/styles/app.css").toExternalForm());
        window.setScene(scene);

        // First slide, shown immediately.
        layers[active].setImage(images[index]);
        layers[active].setOpacity(1);
        kenBurns(layers[active]);

        autoAdvance = new Timeline(new KeyFrame(SLIDE_INTERVAL, e -> goTo(index + 1)));
        autoAdvance.setCycleCount(Timeline.INDEFINITE);
        autoAdvance.play();

        startMusic();

        prev.setOnAction(e -> { goTo(index - 1); restartTimer(); });
        next.setOnAction(e -> { goTo(index + 1); restartTimer(); });
        playPause.setOnAction(e -> {
            playing = !playing;
            playPause.setText(playing ? "❚❚" : "▶");
            if (playing) {
                autoAdvance.play();
                if (player != null) player.play();
            } else {
                autoAdvance.pause();
                if (player != null) player.pause();
            }
        });
        close.setOnAction(e -> window.close());

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) window.close();
            else if (e.getCode() == KeyCode.RIGHT) { goTo(index + 1); restartTimer(); }
            else if (e.getCode() == KeyCode.LEFT) { goTo(index - 1); restartTimer(); }
        });

        window.setOnHidden(e -> {
            autoAdvance.stop();
            if (player != null) {
                player.stop();
                player.dispose();
            }
        });

        window.setOnShown(e -> {
            window.toFront();
            if (owner != null && owner.getWidth() > 0) {
                window.setX(owner.getX() + (owner.getWidth() - window.getWidth()) / 2);
                window.setY(owner.getY() + (owner.getHeight() - window.getHeight()) / 2);
            }
        });
        window.show();
    }

    private void goTo(int target) {
        int n = images.length;
        int nextIndex = ((target % n) + n) % n;
        if (nextIndex == index && layers[active].getImage() != null) {
            return;
        }
        index = nextIndex;
        int incoming = 1 - active;

        ImageView in = layers[incoming];
        ImageView out = layers[active];
        in.setImage(images[index]);
        in.setOpacity(0);
        kenBurns(in);

        FadeTransition fadeIn = new FadeTransition(FADE, in);
        fadeIn.setToValue(1);
        FadeTransition fadeOut = new FadeTransition(FADE, out);
        fadeOut.setToValue(0);
        fadeIn.play();
        fadeOut.play();

        active = incoming;
    }

    /** Slow zoom drift for a cinematic feel. */
    private void kenBurns(ImageView view) {
        view.setScaleX(1.0);
        view.setScaleY(1.0);
        ScaleTransition drift = new ScaleTransition(SLIDE_INTERVAL.add(FADE), view);
        drift.setToX(1.07);
        drift.setToY(1.07);
        drift.play();
    }

    private void restartTimer() {
        if (playing && autoAdvance != null) {
            autoAdvance.stop();
            autoAdvance.play();
        }
    }

    private void startMusic() {
        try {
            var url = getClass().getResource("/audio/wedding_music.wav");
            if (url == null) return;
            player = new MediaPlayer(new Media(url.toExternalForm()));
            player.setCycleCount(MediaPlayer.INDEFINITE);
            player.setVolume(0.6);
            player.play();
        } catch (Exception ignored) {
            // No music is fine — the slideshow still runs.
        }
    }

    private Button controlButton(String glyph) {
        Button button = new Button(glyph);
        button.getStyleClass().add("slideshow-button");
        button.setFocusTraversable(false);
        return button;
    }
}
