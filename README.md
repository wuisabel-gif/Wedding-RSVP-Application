# Wedding RSVP Application

A JavaFX desktop application for managing wedding guest RSVPs, meal selections, and party sizes.

This project was created as homework for an Advanced Java course at Santa Monica College.

## Design Direction

The desktop app follows a warm, wedding-themed management dashboard inspired by the provided mockup:

- Large editorial heading with a simple subtitle
- Rounded action buttons for `Add Guest`, `Edit`, `Delete`, and `Refresh`
- Wide guest table with soft borders and roomy spacing
- Dedicated RSVP summary card on the right
- Neutral cream background with terracotta accents

For a static browser-based version of this screen, open [design-preview.html](./design-preview.html).

## Features

- Add, edit, delete, and view guest RSVP entries
- Validate required fields and accepted-meal rules
- Show live RSVP summary totals
- Keep UI, controller logic, models, and repository code separated

## Project Structure

- `src/main/java/app`: JavaFX entrypoint
- `src/main/java/controller`: UI event handling and workflow
- `src/main/java/model`: guest model and enums
- `src/main/java/repository`: in-memory guest storage
- `src/main/java/util`: validation helpers
- `src/main/java/view`: JavaFX views and layout components
- `src/main/resources/styles`: application styling

## Run

```bash
mvn javafx:run
```

## Build

```bash
mvn package
```

## UI Preview

The repository includes a static HTML mockup that recreates the target interface for presentation and design review.

```bash
open design-preview.html
```

This HTML preview is not the application runtime, but it matches the visual direction used for the JavaFX implementation.
