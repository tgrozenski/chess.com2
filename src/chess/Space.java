package chess;

import java.awt.Color;

public class Space {

    public Color spaceColor;
    public Piece currentPiece;
    public String spaceNotation;
    public Coord position;

    public Space(Color color, int x, int y) {
        this.position = new Coord(x, y);
        this.spaceColor = color;
    }

}