package chess;
import java.util.ArrayList;

public class Piece {
    public String notation; 
    public Coord position;
    public char pieceType;
    public int color;
    public int moveCount = 0;
    public boolean isTaken = false;
    public boolean hasMoved = false;
    public String imagePath;

    // abstract int[] getLegalMoves();

    Piece(String notation, boolean isTaken, boolean hasMoved) {
        this.hasMoved = hasMoved;
        this.isTaken = isTaken;
        this.notation = notation;
        this.position = notationToCoords(notation);
        this.pieceType = notationtoPieceType(notation);
        this.color = notationtoColor(notation);
        this.imagePath = getImagePath();
    }
    //1ke8
    private Coord notationToCoords(String notation) {
        int x = ((int) notation.charAt(2) - 96) * 100;
        int y = (9 - Character.getNumericValue(notation.charAt(3))) * 100;
        Coord coord = new Coord(x, y);
        return coord;
    }

    public char notationtoPieceType(String notation) {
        return notation.charAt(1);
    }

    private int notationtoColor(String notation) {
        return Integer.parseInt(notation.charAt(0) + "");
    }

    public ArrayList<Coord> getLegalMoves(boolean check, boolean highlight) {
        RuleManager rm = new RuleManager();
        ArrayList<Coord> arr = rm.getLegalMoves(this, check, highlight);
        return arr;
    }

    private String getImagePath() {
        if(this.color == 1) {
        String path = "";
        switch(this.pieceType) {
            case 'k':
                path = "/images/black_king.png";
                break;
            case 'q':
                path = "/images/black_queen.png";
                break;
            case 'r':
                path = "/images/black_rook.png";
                break;
            case 'b':
                path = "/images/black_bishop.png";
                break;
            case 'n': 
                path = "/images/black_knight.png";
                break;
            case 'p':
                path ="/images/black_pawn.png";
                break;
        }
        return path;
        }
        else {
        String path = "";
        switch(this.pieceType) {
            case 'k':
                path = "/images/white_king.png";
                break;
            case 'q':
                path = "/images/white_queen.png";
                break;
            case 'r':
                path = "/images/white_rook.png";
                break;
            case 'b':
                path = "/images/white_bishop.png";
                break;
            case 'n': 
                path = "/images/white_knight.png";
                break;
            case 'p':
                path ="/images/white_pawn.png";
                break;
        }
        return path;
        }
    }
    
}