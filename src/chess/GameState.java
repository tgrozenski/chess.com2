package chess;
import java.util.HashMap;
import java.util.ArrayList;


public class GameState {
    private static boolean whiteCheckStatus = false;
    private static boolean blackCheckStatus = false;
    public boolean gameActive = false;
    private static Piece previousPiece;
    private static Piece previousMovedPiece;
    private static int currentTurn = 0;
    private static ArrayList<Coord> legalMoves = new ArrayList<>(); 
    private static HashMap<String, Piece> boardPosition = new HashMap<>();

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
    public void setPreviousMovedPiece(Piece p) {
        previousMovedPiece = p;
    }
    public Piece getPreviousMovedPiece() {
        return previousMovedPiece;
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
    public void setBoardPosition(HashMap<String, Piece> board) {
        boardPosition = board;
    }
    public void addPiece(String key, Piece value) {
        boardPosition.put(key, value);
    }
    public void removePiece(String notation) {
        boardPosition.remove(notation);
    }
    public HashMap<String, Piece> getBoardPosition() {
        return boardPosition;
    }
    public Piece getKing(int color) {
        for(Piece p : boardPosition.values()) {
            if(p.pieceType == 'k' && p.color == color) {
                return p;
            }
        }
        return null;
    }
    public void clearLegalMoves() {
        legalMoves.clear();
    }
}