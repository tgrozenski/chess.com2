package chess;
import java.util.ArrayList;

public class RuleManager {

    private ArrayList<Coord> legalMoves = new ArrayList<>();
    private Board board = new Board();
    private static final int WHITE = 0;
    private static final int BLACK = 1;
    
    public ArrayList<Coord> getLegalMoves(Piece p) {

        switch(p.pieceType) {
            case 'k':
                break;
            case 'q':
                break;
            case 'r':
                break;
            case 'b':
                break;
            case 'n': 
                break;
            case 'p':
                getPawnMoves(p);
                break;
        }
        return legalMoves;
    }

    public void getPawnMoves(Piece p) {
        Coord position = new Coord(p.position.x, p.position.y);
        System.out.println("POSITION IN GET MOVES: " + position.x + " " + position.y);
        //Pawn can move forward 2 if on its starting square
        //Pawn can move once otherwise
        System.out.println(p.color);
        if(p.color == WHITE ) {
            Coord cord = new Coord(position.x, position.y - 100);
            if(validateMove(cord)) {
                if(board.getSpaceFromCoord(cord).currentPiece == null) {
                    legalMoves.add(cord);
                    if(p.position.y == 700) {
                        Coord cord2 = new Coord(position.x, position.y - 200);
                        if(validateMove(cord2)) {
                            if(board.getSpaceFromCoord(cord2).currentPiece == null) {
                                legalMoves.add(cord2);
                            }
                        }
                    }
                }
            }
            Coord leftTake = new Coord(position.x - 100, position.y - 100);
            Coord rightTake = new Coord(position.x + 100, position.y - 100);
            if(validateMove(leftTake)) {
                Piece current = board.getSpaceFromCoord(leftTake).currentPiece;
                if(current != null && current.color != WHITE) {
                    legalMoves.add(leftTake); 
                }
            }
            if(validateMove(rightTake)) {
                Piece current = board.getSpaceFromCoord(rightTake).currentPiece;
                if (current != null && current.color != WHITE) {
                    legalMoves.add(rightTake);
                }
            }
        }
        else {
            //move one or two spaces forward
            Coord cord = new Coord(position.x, position.y + 100);
            if(validateMove(cord)) {
                if(board.getSpaceFromCoord(cord).currentPiece == null) {
                    legalMoves.add(cord);
                    if(p.position.y == 200) {
                        Coord cord2 = new Coord(position.x, position.y + 200);
                        if(validateMove(cord2)) {
                            if(board.getSpaceFromCoord(cord2).currentPiece == null) {
                                legalMoves.add(cord2);
                            }
                        }
                    }
                }
            }
            Coord leftTake = new Coord(position.x - 100, position.y + 100);
            Coord rightTake = new Coord(position.x + 100, position.y + 100);
            if(validateMove(leftTake)) {
                Piece current = board.getSpaceFromCoord(leftTake).currentPiece;
                if(current != null && current.color != BLACK) {
                    legalMoves.add(leftTake); 
                }
            }
            if(validateMove(rightTake)) {
                Piece current = board.getSpaceFromCoord(rightTake).currentPiece; 
                if (current != null && current.color != BLACK) {
                    legalMoves.add(rightTake);
                }
            }

        }
    }

    public void getKingMoves(Piece p) {
        Coord currentPos = new Coord(p.position.x, p.position.y);
    }
        //TODO AU PASSANT 
        //Pawn can take au PASSANT if pawn moved by two squares next to it on the left or right

    private Space getBottomRightMove(Coord cord) { return board.getSpaceFromCoord(new Coord(cord.x + 100, cord.y - 100)); }
    private Space getBottomLeftMove(Coord cord) { return board.getSpaceFromCoord(new Coord(cord.x - 100, cord.y - 100)); }
    private Space getTopLeftMove(Coord cord) { return board.getSpaceFromCoord(new Coord(cord.x - 100, cord.y + 100)); }
    private Space getTopRightMove(Coord cord) { return board.getSpaceFromCoord(new Coord(cord.x + 100, cord.y + 100)); }
    private Space getLeftMove(Coord cord) { return board.getSpaceFromCoord(new Coord(cord.x - 100, cord.y)); }
    private Space getRightMove(Coord cord) { return board.getSpaceFromCoord(new Coord(cord.x + 100, cord.y)); }
    private Space getTopMove(Coord cord) { return board.getSpaceFromCoord(new Coord(cord.x, cord.y + 100)); }
    private Space getBottomMove(Coord cord) { return board.getSpaceFromCoord(new Coord(cord.x, cord.y - 100)); }

    private boolean validateMove(Coord cord) {
        boolean valid = true;
        if(cord.x < 100 || cord.x > 800 || cord.y < 100 || cord.y > 800) {
            valid = false;
        }
        return valid;
    }
}