# Wedding RSVP Guest Management System

A warm, elegant JavaFX desktop app for managing a wedding guest list: RSVPs, meal
selections, party sizes, a live response summary, and a countdown to the big day.

## The Story Behind It

I originally built this as a project for my **Advanced Java course at Santa Monica
College** — a JavaFX exercise in clean MVC structure, data validation, and UI design.

More recently I've **expanded it into a real, usable tool to help my next-door neighbors
manage the RSVPs for their wedding** , adding the polished wedding theme, the live
summary and countdown, search, the photo slideshow with music, and more.

> **About this public version:** because the real version holds my neighbors' and their
> guests' personal information, **this copy has all of that actual data removed and
> replaced with fictional stand-ins.** The couple here — **Eleanor Hart and James
> Calloway**, marrying at *The Old Maple Orchard* on *September 12, 2026* — and every
> guest, photo, and detail are made up. It's the same application, just with placeholder
> people so nothing private is shared.

## What It Does

- **Add, edit, and delete** guest entries with a polished modal form
- **Colored RSVP badges** — sage for *Accepted*, rose for *Declined*, gold for *Awaiting reply*
- **Meal chips** that only apply to guests who've accepted (enforced by validation)
- **Live search** across name, contact, meal, and notes
- **At-a-glance summary**: guests attending, total invited, declined, awaiting reply,
  plates to prepare, and a response-rate percentage
- **Live countdown** to the wedding day
- **Validation**: required name, positive party size, sensible email/phone format,
  no duplicate names, and a meal choice required when someone accepts

## Design Direction

A soft **garden-wedding** palette — ivory, sage green, dusty rose, and antique gold —
with three bundled typefaces:

| Use | Typeface |
| --- | --- |
| The couple's names & monogram | **Great Vibes** (script) |
| Headings, titles, numbers | **Playfair Display** (display serif) |
| Body & table text | **EB Garamond** (text serif) |

Fonts live in `src/main/resources/fonts` and load at startup via `util/FontLoader.java`,
so the look is identical on any machine without installing anything.

For a static browser version of the screen, open [design-preview.html](./design-preview.html).

To wire a image into the app, drop the file in `src/main/resources/images/` and
set it as the background of `.hero` in `src/main/resources/styles/app.css`
(`-fx-background-image: url(...)`), or place it in an `ImageView` at the top of `MainView`.

## Project Structure

- `src/main/java/app` — JavaFX entrypoint (loads fonts, builds the scene)
- `src/main/java/controller` — UI event handling, search filter, and summary logic
- `src/main/java/model` — `Guest` model and the `RSVPStatus` / `MealOption` enums
- `src/main/java/repository` — in-memory guest storage
- `src/main/java/util` — validation helpers and the font loader
- `src/main/java/view` — JavaFX views (hero header, table, summary, guest form)
- `src/main/resources/styles` — the wedding stylesheet
- `src/main/resources/fonts` — bundled typefaces

## Run

```bash
mvn javafx:run
```

## Build

```bash
mvn package
```
