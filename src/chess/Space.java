package chess;

import java.awt.Color;

public class Space {

    public Color spaceColor;
    public Piece currentPiece;
    public String spaceNotation;
    public int XPOS;
    public int YPOS;

    public Space(Color color, int x, int y) {
        this.XPOS = x;
        this.YPOS = y;
        this.spaceColor = color;
    }

}