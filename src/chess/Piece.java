package chess;

public class Piece {
    public String notation; 
    public int[] position; 
    public char pieceType;
    int color;
    public boolean isTaken = false;
    public String imagePath;

    // abstract int[] getLegalMoves();

    Piece(String notation) {
        this.position = notationToCoords(notation);
        this.pieceType = notationtoPieceType(notation);
        this.color = notationtoColor(notation);
        this.imagePath = getImagePath();
    }
    //1ke8
    private int[] notationToCoords(String notation) {
        int[] coords = new int[2];
        coords[0] = ((int) notation.charAt(2) - 96) * 100;
        coords[1] = (9 - Integer.parseInt(notation.charAt(3) + "")) * 100;
        return coords;
    }

    public char notationtoPieceType(String notation) {
        return notation.charAt(1);
    }

    private int notationtoColor(String notation) {
        return Integer.parseInt(notation.charAt(0) + "");
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