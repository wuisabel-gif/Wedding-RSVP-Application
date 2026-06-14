package view;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;

/**
 * A hand-built vector wedding crest — a laurel wreath ringing the couple's
 * monogram "E &amp; J". Drawn entirely from JavaFX shapes (no image files), so it
 * stays crisp at any size. The geometry mirrors {@code resources/images/monogram.svg}.
 */
public class MonogramCrest extends Pane {

    private static final Color GOLD = Color.web("#c2a363");
    private static final Color ROSE = Color.web("#b06a5b");
    private static final Color SAND = Color.web("#a08363");

    private static final double CX = 120, CY = 122;
    private static final double[] OUTER_ANGLES = {78, 52, 26, 2, -22, -46};
    private static final double[] INNER_ANGLES = {66, 40, 14, -10, -34};

    public MonogramCrest(double displayHeight) {
        Group content = new Group();

        content.getChildren().addAll(
                ring(108, 1.2, 0.55),
                ring(101, 0.8, 0.32),
                stem("M130.2,205.4 A84,84 0 0 1 174.0,57.7"),
                stem("M109.8,205.4 A84,84 0 0 0 66.0,57.7"));

        for (double a : OUTER_ANGLES) {
            content.getChildren().addAll(leaf(84, a, 12, 4.2, 0.9), leaf(84, 180 - a, 12, 4.2, 0.9));
        }
        for (double a : INNER_ANGLES) {
            content.getChildren().addAll(leaf(69, a, 8, 3, 0.78), leaf(69, 180 - a, 8, 3, 0.78));
        }

        Circle knot = new Circle(CX, 206, 3, ROSE);
        content.getChildren().addAll(knot, tail("M120,206 q-10,7 -20,5"), tail("M120,206 q10,7 20,5"));

        content.getChildren().addAll(
                centeredText("E  &  J", Font.font("Great Vibes", 60), 138, ROSE),
                centeredText("2 0 2 6", Font.font("EB Garamond", 12), 164, SAND),
                centeredText("THE OLD MAPLE ORCHARD", Font.font("EB Garamond", 8), 182, SAND));

        // Scale the 240×248 artwork down to the requested header height.
        double scale = displayHeight / 248.0;
        content.getTransforms().add(new Scale(scale, scale));

        getChildren().add(content);
        double w = 240 * scale;
        setMinSize(w, displayHeight);
        setPrefSize(w, displayHeight);
        setMaxSize(w, displayHeight);
    }

    private Circle ring(double r, double width, double opacity) {
        Circle c = new Circle(CX, CY, r);
        c.setFill(null);
        c.setStroke(GOLD);
        c.setStrokeWidth(width);
        c.setOpacity(opacity);
        return c;
    }

    private Ellipse leaf(double radius, double angle, double rx, double ry, double opacity) {
        double rad = Math.toRadians(angle);
        Ellipse e = new Ellipse(CX + radius * Math.cos(rad), CY + radius * Math.sin(rad), rx, ry);
        e.setFill(GOLD);
        e.setOpacity(opacity);
        e.setRotate(angle + 90);
        return e;
    }

    private SVGPath stem(String path) {
        SVGPath p = new SVGPath();
        p.setContent(path);
        p.setFill(null);
        p.setStroke(GOLD);
        p.setStrokeWidth(1.1);
        p.setOpacity(0.7);
        return p;
    }

    private SVGPath tail(String path) {
        SVGPath p = stem(path);
        p.setStroke(ROSE);
        return p;
    }

    private Text centeredText(String value, Font font, double baselineY, Color fill) {
        Text text = new Text(value);
        text.setFont(font);
        text.setFill(fill);
        text.setX(CX - text.getLayoutBounds().getWidth() / 2);
        text.setY(baselineY);
        return text;
    }
}
