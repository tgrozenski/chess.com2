package chess;

public class Threat {

pinnedTo state;
Piece piece;

Threat(pinnedTo state, Piece threat ) {
    this.state = state;
    this.piece = threat;
}
}