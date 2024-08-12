package chess;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;

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
HashSet<Piece> enemigos = new HashSet<>();
public Piece enemyPiece;
public Piece king;

public pinnedTo relationToKing(Coord p, Piece King) {
    crawlSpaces(King.color, p, pinnedTo.RIGHT);
    if(checkForPin()) {
        return pinnedTo.LEFT;
    }
    crawlSpaces(King.color, p, pinnedTo.LEFT);
    if(checkForPin()) {
        return pinnedTo.RIGHT;
    }
    crawlSpaces(King.color, p, pinnedTo.TOP);
    if(checkForPin()) {
        return pinnedTo.BOTTOM;
    }
    crawlSpaces(King.color, p, pinnedTo.BOTTOM);
    if(checkForPin()) {
        return pinnedTo.TOP;
    }
    crawlSpaces(King.color, p, pinnedTo.TOP_LEFT);
    if(checkForPin()) {
        return pinnedTo.BOTTOM_RIGHT;
    }
    crawlSpaces(King.color, p, pinnedTo.BOTTOM_RIGHT);
    if(checkForPin()) {
        return pinnedTo.TOP_LEFT;
    }
    crawlSpaces(King.color, p, pinnedTo.BOTTOM_LEFT);
    if(checkForPin()) {
        return pinnedTo.TOP_RIGHT;
    }
    crawlSpaces(King.color, p, pinnedTo.TOP_RIGHT);
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

public void crawlSpaces (int color, Coord pos, pinnedTo pinState) {
    clearAll();
    for(int i = 0; i < 7; i++) {
        Space s = getSpace(pinState, pos);
        if(s != null && pos != null) {
            pos = new Coord(s.XPOS, s.YPOS);
            Piece current = s.currentPiece;

            if(current == null) {
                emptySpaces.put(pos.notation, pos);
            } 
            else if(current.color == color) {
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

public ArrayList<Coord> getSpacesTillTeamPiece(Coord pos, pinnedTo direction, int color) {
    ArrayList<Coord> spaces = new ArrayList<>();
    clearAll();
    for(int i = 0; i < 7; i++) {
        Space s = getSpace(direction, pos);
        if(s != null || pos != null) {
            pos = new Coord(s.XPOS, s.YPOS);
            Piece current = s.currentPiece;
            if(current == null) {
                spaces.add(pos);
            } 
            else if(current.color == color) {
                break;
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
    return spaces;
}

private boolean enemyValid() {
    if(enemyPiece != null && teamSpaces.size() == 0) {
        if(enemyPiece.pieceType != 'p' || enemyPiece.pieceType != 'n') {
            System.out.println("Enemy Valid " + enemyPiece.notation);
            return true;
        }
        else {
            return false;
        }
    }
    else {
        System.out.println("Enemy is invalid");
        return false;
    }
}

public Threat getEnemy(Coord p, int color) {
    crawlSpaces(color, p, pinnedTo.RIGHT);
    if(enemyValid()) {
        return new Threat(pinnedTo.RIGHT, enemyPiece);
    }
    crawlSpaces(color, p, pinnedTo.LEFT);
    if(enemyValid()) {
        return new Threat(pinnedTo.LEFT, enemyPiece);
    }
    crawlSpaces(color, p, pinnedTo.TOP);
    if(enemyValid()) {
        return new Threat(pinnedTo.TOP, enemyPiece);
    }
    crawlSpaces(color, p, pinnedTo.BOTTOM);
    if(enemyValid()) {
        return new Threat(pinnedTo.TOP, enemyPiece);
    }
    crawlSpaces(color, p, pinnedTo.TOP_LEFT);
    if(enemyValid()) {
        return new Threat(pinnedTo.TOP_LEFT, enemyPiece);
    }
    crawlSpaces(color, p, pinnedTo.BOTTOM_RIGHT);
    if(enemyValid()) {
        return new Threat(pinnedTo.BOTTOM_RIGHT, enemyPiece);
    }
    crawlSpaces(color, p, pinnedTo.BOTTOM_LEFT);
    if(enemyValid()) {
        return new Threat(pinnedTo.BOTTOM_LEFT, enemyPiece);
    }
    crawlSpaces(color, p, pinnedTo.TOP_RIGHT);
    if(enemyValid()) {
        return new Threat(pinnedTo.TOP_RIGHT, enemyPiece);
    }
    return null;
    }
}
