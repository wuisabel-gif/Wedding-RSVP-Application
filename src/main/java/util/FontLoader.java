package util;

import javafx.scene.text.Font;

/**
 * Loads the bundled wedding typefaces so the CSS can reference them by family name:
 *   - "EB Garamond"      (body + table text)
 *   - "Playfair Display" (headings, panel titles, metric numbers)
 *   - "Great Vibes"      (the couple's names and the monogram)
 *
 * Fonts ship in {@code src/main/resources/fonts} and load once at startup. If a
 * file is missing the UI quietly falls back to a serif stack, so nothing breaks.
 */
public final class FontLoader {

    private static final String[] FONT_RESOURCES = {
            "/fonts/EBGaramond-Variable.ttf",
            "/fonts/PlayfairDisplay-Variable.ttf",
            "/fonts/GreatVibes-Regular.ttf",
            "/fonts/bpg-supersquare-caps-2013-webfont.ttf"
    };

    private FontLoader() {
    }

    public static void loadFonts() {
        for (String resource : FONT_RESOURCES) {
            try (var stream = FontLoader.class.getResourceAsStream(resource)) {
                if (stream != null) {
                    Font.loadFont(stream, 12);
                }
            } catch (Exception ignored) {
                // A missing decorative font should never stop the app from opening.
            }
        }
    }
}
