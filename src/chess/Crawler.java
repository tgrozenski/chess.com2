package chess;
import java.util.HashMap;

enum pinnedTo {
    RIGHT,
    LEFT,
    TOP,
    BOTTOM,
    TOP_RIGHT,
    TOP_LEFT,
    BOTTOM_RIGHT,
    BOTTOM_LEFT,
    NOT_PINNED
} 

public class Crawler {
//Takes a piece in constructor and maps teamMate spaces empty spaces and keeps the coordinate of the first enemy piece

Board board = new Board();
public HashMap<String, Coord> teamSpaces = new HashMap<>();
public HashMap<String, Coord> emptySpaces = new HashMap<>();
public Piece enemyPiece;
public Piece king;
        
public pinnedTo relationToKing(Piece p, Piece King) {

    crawlSpaces(p, pinnedTo.RIGHT);
    if(king != null) {
        return pinnedTo.LEFT;
    }
    crawlSpaces(p, pinnedTo.LEFT);
    if(king != null) {
        return pinnedTo.RIGHT;
    }
    crawlSpaces(p, pinnedTo.TOP);
    if(king != null) {
        return pinnedTo.BOTTOM;
    }
    crawlSpaces(p, pinnedTo.BOTTOM);
    if(king != null) {
        return pinnedTo.TOP;
    }
    crawlSpaces(p, pinnedTo.TOP_LEFT);
    if(king != null) {
        return pinnedTo.BOTTOM_RIGHT;
    }
    crawlSpaces(p, pinnedTo.BOTTOM_RIGHT);
    if(king != null) {
        return pinnedTo.TOP_LEFT;
    }
    crawlSpaces(p, pinnedTo.BOTTOM_LEFT);
    if(king != null) {
        return pinnedTo.TOP_RIGHT;
    }
    crawlSpaces(p, pinnedTo.TOP_RIGHT);
    if(king != null) {
        return pinnedTo.BOTTOM_LEFT;
    }
    return pinnedTo.NOT_PINNED;
}

public Space getSpace(pinnedTo pinState, Coord p) {
    switch (pinState) {
        case RIGHT:
            return board.getRightMove(p);
        case LEFT:
            return board.getLeftMove(p);
        case TOP:
            return board.getTopMove(p);
        case BOTTOM:
            return board.getBottomMove(p);
        case BOTTOM_RIGHT:
            return board.getBottomRightMove(p);
        case BOTTOM_LEFT:
            return board.getBottomLeftMove(p);
        case TOP_RIGHT:
            return board.getTopRightMove(p);
        case TOP_LEFT:
            return board.getTopLeftMove(p);
        default:
            return null;
    }
}
public void crawlSpaces (Piece p, pinnedTo pinState) {
    clearAll();
    for(int i = 0; i < 7; i++) {
        Space s = getSpace(pinState, p.position);

        if(s != null) {
            Coord pos = new Coord(s.XPOS, s.YPOS);
            Piece current = s.currentPiece;

            if(current == null) {
                emptySpaces.put(pos + "", pos);
            } 
            else if(current.color == p.color) {
                teamSpaces.put(pos + "", pos);
                if(current.pieceType == 'k') {
                    king = current;
                }
            }
            else {
                this.enemyPiece = s.currentPiece;
                break;
            }
        }
    }
}

public void clearAll() {
    enemyPiece = null;    
    king = null;
    emptySpaces.clear();
    teamSpaces.clear();
}
}