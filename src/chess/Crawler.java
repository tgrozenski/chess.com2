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
    if(checkForPin()) {
        return pinnedTo.LEFT;
    }
    crawlSpaces(p, pinnedTo.LEFT);
    if(checkForPin()) {
        return pinnedTo.RIGHT;
    }
    crawlSpaces(p, pinnedTo.TOP);
    if(checkForPin()) {
        return pinnedTo.BOTTOM;
    }
    crawlSpaces(p, pinnedTo.BOTTOM);
    if(checkForPin()) {
        return pinnedTo.TOP;
    }
    crawlSpaces(p, pinnedTo.TOP_LEFT);
    if(checkForPin()) {
        return pinnedTo.BOTTOM_RIGHT;
    }
    crawlSpaces(p, pinnedTo.BOTTOM_RIGHT);
    if(checkForPin()) {
        return pinnedTo.TOP_LEFT;
    }
    crawlSpaces(p, pinnedTo.BOTTOM_LEFT);
    if(checkForPin()) {
        return pinnedTo.TOP_RIGHT;
    }
    crawlSpaces(p, pinnedTo.TOP_RIGHT);
    if(checkForPin()) {
        return pinnedTo.BOTTOM_LEFT;
    }
    return pinnedTo.NOT_PINNED; }

private boolean checkForPin() {
    if(king != null && teamSpaces.size() == 1 && enemyPiece == null) {
        clearAll();
        return true;
    }
    else {
        clearAll();
        return false;
    }
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
    Coord pos = p.position;
    for(int i = 0; i < 7; i++) {
        Space s = getSpace(pinState, pos);
        if(s != null && pos != null) {
            pos = new Coord(s.XPOS, s.YPOS);
            Piece current = s.currentPiece;

            if(current == null) {
                emptySpaces.put(pos.notation, pos);
            } 
            else if(current.color == p.color) {
                teamSpaces.put(pos.notation, pos);
                if(current.pieceType == 'k') {
                    king = current;
                    break;
                }
            }
            else {
                this.enemyPiece = s.currentPiece;
                break;
            }
        }
        else {
            break;
        }
    }
    for(Coord c: emptySpaces.values()) {
        System.out.println("empty space " + c.notation);
    }
}

public void clearAll() {
    enemyPiece = null;    
    king = null;
    emptySpaces.clear();
    teamSpaces.clear();
}

public boolean threatPresent(pinnedTo state, Piece enemy) {
    if(state == pinnedTo.RIGHT || state == pinnedTo.LEFT || state == pinnedTo.BOTTOM || state == pinnedTo.TOP) {
        if(enemy.pieceType == 'r' || enemy.pieceType == 'q') {
            return true;
        }
    }
    else if(state == pinnedTo.TOP_LEFT || state == pinnedTo.TOP_RIGHT || state == pinnedTo.BOTTOM_LEFT || state == pinnedTo.BOTTOM_RIGHT) {
        if(enemy.pieceType == 'b' || enemy.pieceType == 'q') {
            return true;
        }
    }

    return false;
}

}