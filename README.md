# Chess.com2 The Sequel

## Table of Contents
- [Introduction](#introduction)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Setup and Installation](#setup-and-installation)
- [How to Play](#how-to-play)
- [Project Structure](#project-structure)
- [Known Issues and Limitations](#known-issues-and-limitations)
- [Future Improvements](#future-improvements)
- [Contributing](#contributing)
- [License](#license)

---

## Introduction
This project is a Java-based implementation of a chess game, complete with GUI using the Swing framework. The game aims to simulate a chess-playing experience with features like move validation, castling, en passant, and check detection. While it's a single-player setup (no AI is implemented), it is designed for two players to play on the same system or for one player to use it as a practice board.

---

## Features

### Gameplay Features
- Fully functional chessboard.
- Legal move validation for all chess pieces.
- Special moves supported:
  - Castling (king-side and queen-side).
  - En passant capture.
- Detection of checks.
- Turn-based gameplay.

### User Interface
- Graphical board rendering using Java Swing.
- Pieces are rendered dynamically based on game state.
- Mouse interaction for selecting pieces and moving them.
- Visual highlights for legal moves.

---

## Technologies Used
- **Programming Language:** Java
- **GUI Framework:** Swing

---

## Setup and Installation

### Prerequisites
- **Java Development Kit (JDK)**: Ensure that Java 8 or higher is installed.

### Steps to Set Up the Project
1. Clone the repository:
   ```bash
   git clone https://github.com/tgrozenski/chess.com2
   ```
2.  Run the following to start the program:
    ```bash
    java -cp bin chess.Chess
    ```

---

## How to Play

1. Launch the application by running the `Chess` class.
2. A chessboard will appear in a new window.
3. The player with white pieces begins the game.
4. To make a move:
   - Click on a piece to select it.
   - Highlighted squares indicate legal moves for the selected piece.
   - Click on a highlighted square to move the piece.
5. Special Moves:
   - **Castling**: Select the king, and if conditions are met, the rook's destination will be highlighted.
   - **En passant**: Execute by selecting the pawn after the opponent makes a double-step move.
6. The game alternates turns between white and black.
7. The game ends in checkmate or stalemate (note: checkmate and stalemate detection art TODO).

---

## Project Structure

```
ChessProject/
├── src/
│   ├── Chess.java            # Main game class
│   ├── GameState.java        # Tracks the state of the game
│   ├── Board.java            # Handles board rendering and logic
│   ├── Piece.java            # Represents chess pieces and their behavior
│   ├── Coord.java            # Handles coordinates and move validation
│   ├── Space.java            # Represents individual squares on the board
│   ├── Crawler.java          # Assists in threat detection and move validation
│   ├── Threat.java           # Represents threats to a king
```

---

## Known Issues and Limitations

- **No AI Opponent**: The game is designed for two players and does not include an AI.
- **Limited Endgame Detection**: Stalemate, threefold repetition, and insufficient material are not implemented.
- **No Undo Feature**: Players cannot undo moves.
- **Performance**: The board is redrawn frequently, which could be optimized for better performance.
- **Bugs**: The game still has a few known bugs in certain edge cases (When a knight's position should prevent castling, ect.).

---

## Future Improvements

1. **AI Integration**:
   - Add single-player mode with AI opponents.
   - Implement difficulty levels.

2. **Enhanced Move Validation**:
   - Add detection for stalemates, threefold repetition, and insufficient material.

3. **Undo/Redo Functionality**:
   - Allow players to undo or redo moves.

4. **UI Enhancements**:
   - Add a start screen, game reset button, and a timer for each player.
   - Improve graphical rendering for better user experience.

---

## Contributing

We welcome contributions to improve the chess game! If you'd like to contribute:
1. Fork the repository.
2. Create a new branch for your feature or bugfix:
   ```bash
   git checkout -b feature-name
   ```
3. Commit your changes and push them to your fork.
4. Submit a pull request with a clear description of your changes.

---

## License

This project is licensed under the MIT License. You are free to use, modify, and distribute the code. 
