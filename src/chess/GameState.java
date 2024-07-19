package chess;
import java.util.HashMap;
import java.util.ArrayList;


public class GameState {
    private static boolean whiteCheckStatus = false;
    private static boolean blackCheckStatus = false;
    private static Piece previousPiece;
    private static int currentTurn = 0;
    private static ArrayList<Coord> legalMoves; 
    private static HashMap<String, Piece> boardPosition;

    public boolean getWhiteCheckStatus() {
        return whiteCheckStatus;
    }
    public void setWhiteCheckStatus(boolean status) {
        whiteCheckStatus = status;
    }
    public boolean getBlackCheckStatus() {
        return blackCheckStatus;
    }
    public void setBlackCheckStatus(boolean status) {
        blackCheckStatus = status;
    }
    public Piece getPreviousPiece() {
        return previousPiece;
    }
    public void setPreviousPiece(Piece p) {
        previousPiece = p;
    }
    public int getCurrentTurn() {
        return currentTurn;
    }
    public void setCurrentTurn(int color) {
        currentTurn = color;
    }
    public void setLegalMoves(ArrayList<Coord> arrList) {
        legalMoves = arrList;
    }
    public ArrayList<Coord> getLegalMoves() {
        return legalMoves;
    }
}