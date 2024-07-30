package chess;
import java.util.ArrayList;

public class RuleManager {

    private ArrayList<Coord> legalMoves = new ArrayList<>();
    private Board board = new Board();
    private static final int WHITE = 0;
    private boolean highlightState;
    
    public ArrayList<Coord> getLegalMoves(Piece p, boolean check, boolean highlight) {
        // System.out.println("IN CHECK?" + checkForCheck(p));
        this.highlightState = highlight;
        switch(p.pieceType) {
            case 'k':
                getKingMoves(p, check);
                break;
            case 'q':
                getBishopMoves(p);
                getRookMoves(p);
                break;
            case 'r':
                getRookMoves(p);
                break;
            case 'b':
                getBishopMoves(p);
                break;
            case 'n': 
                getKnightMoves(p);
                break;
            case 'p':
                getPawnMoves(p);
                auPassante(p);                
                break;
        }
        if(!highlightState)  {
            for(Coord c: legalMoves ) {
                c.highlight = false;
            }
        }
        handlePin(p);
        return legalMoves;
    }

    public void getKnightMoves(Piece p) {
        Coord topRight = new Coord(p.position.x + 100, p.position.y - 200);
        Coord topLeft = new Coord(p.position.x - 100, p.position.y - 200);
        Coord bottomRight= new Coord(p.position.x + 100, p.position.y + 200);
        Coord bottomLeft = new Coord(p.position.x - 100, p.position.y + 200);
        Coord middleLeftA = new Coord(p.position.x - 200, p.position.y + 100);
        Coord middleLeftB = new Coord(p.position.x - 200, p.position.y - 100);
        Coord middleRightA = new Coord(p.position.x + 200, p.position.y + 100);
        Coord middleRightB = new Coord(p.position.x + 200, p.position.y - 100);
        Coord[] arr = { topLeft, topRight, bottomRight, bottomLeft, middleLeftA, middleLeftB, middleRightA, middleRightB }; 

        for(Coord c: arr) {
            if(board.getSpaceFromCoord(c) != null) {
                checkPiece(board.getSpaceFromCoord(c), p.color);
                System.out.println(c.notation);
            }
        }
    }

    private void getBishopMoves(Piece p) {
        //potential multithreading available
        Coord right = p.position, left = p.position, bottomLeft = p.position, bottomRight = p.position;
        boolean rightState = true, leftState = true, bottomRightState = true, bottomLeftState = true;
        for(int i = 0; i < 8; i++) {
            if (rightState) {
                Space s = getTopRightMove(right);
                if(s != null) {
                rightState = checkPieceTakeable(s, p.color); 
                right = new Coord(s.XPOS, s.YPOS);
                }
            }
            if (leftState) {
                Space s2 = getTopLeftMove(left);
                if(s2 != null) {
                leftState = checkPieceTakeable(s2, p.color);
                left = new Coord(s2.XPOS, s2.YPOS);
                }
            }
            if (bottomLeftState) {
                Space s = getBottomLeftMove(bottomLeft);
                if(s != null) {
                bottomLeftState = checkPieceTakeable(s, p.color); 
                bottomLeft = new Coord(s.XPOS, s.YPOS);
                }
            }
            if(bottomRightState) {
                Space s = getBottomRightMove(bottomRight);
                if(s != null) {
                bottomRightState = checkPieceTakeable(s, p.color);
                bottomRight = new Coord(s.XPOS, s.YPOS);
            }
        }
       }
    }

    private void getRookMoves(Piece p) {
        Coord right = p.position, left = p.position, bottom = p.position, top = p.position;
        boolean rightState = true, leftState = true, bottomState = true, topState= true;

        for(int i = 0; i < 7; i++) {
            if (rightState) {
                Space s = getRightMove(right);
                if(s != null) {
                    rightState = checkPieceTakeable(s, p.color);
                    right = new Coord(s.XPOS, s.YPOS);
                }
            }
            if (leftState) {
                Space s2 = getLeftMove(left);
                if(s2 != null) {
                    leftState = checkPieceTakeable(s2, p.color);
                    left = new Coord(s2.XPOS, s2.YPOS);
                }
            }
            if (bottomState) {
                Space s = getBottomMove(bottom);
                if(s!=null) {
                    bottomState = checkPieceTakeable(s, p.color);
                    bottom = new Coord(s.XPOS, s.YPOS);
                }
            }
            if(topState) {
                Space s = getTopMove(top);
                if(s!= null) {
                    topState = checkPieceTakeable(s, p.color);
                    top = new Coord(s.XPOS, s.YPOS);
                }
            }
       }
    }

    public void getPawnMoves(Piece p) {
        Coord position = new Coord(p.position.x, p.position.y);
        // System.out.println("POSITION IN GET MOVES: " + position.x + " " + position.y);
        if(p.color == WHITE) {
            checkPiecePawnMove(getTopMove(position), p.color);
            if(!p.hasMoved && getTopMove(position).currentPiece == null) {
                checkPiecePawnMove(getTop2Move(position), p.color);
            }
            checkPiecePawnTake(getTopLeftMove(position), p.color);
            checkPiecePawnTake(getTopRightMove(position), p.color);
        }
        else {
            checkPiecePawnMove(getBottomMove(position), p.color);
            if(!p.hasMoved && getBottomMove(position).currentPiece == null) {
                checkPiecePawnMove(getBottom2Move(position), p.color);
            }
            checkPiecePawnTake(getBottomLeftMove(position), p.color);
            checkPiecePawnTake(getBottomRightMove(position), p.color);
        }
    }

    public void getKingMoves(Piece p, boolean check) {
        // System.out.println("Piece in King moves " + p.position.x + " " + p.position.y);
        if(!check) {
            System.out.println("Can Castle Right? " + canCastleLeft(p));
            System.out.println("Can Castle left? " + canCastleRight(p));
        }
            checkPiece(getBottomMove(p.position), p.color);
            checkPiece(getTopMove(p.position), p.color);
            checkPiece(getTopLeftMove(p.position), p.color);
            checkPiece(getTopRightMove(p.position), p.color);
            checkPiece(getBottomLeftMove(p.position), p.color);
            checkPiece(getBottomRightMove(p.position), p.color);
            checkPiece(getLeftMove(p.position), p.color);
            checkPiece(getRightMove(p.position), p.color);
    }

    private boolean canCastleLeft(Piece p) {
        Coord currentLoc = p.position;
        if(p.hasMoved) {
            return false;
        }
        try {
            Space left1 = board.getSpaceFromCoord(new Coord(currentLoc.x - 100, currentLoc.y));
            Space left2 = board.getSpaceFromCoord(new Coord(currentLoc.x - 200, currentLoc.y));
            Space left3 = board.getSpaceFromCoord(new Coord(currentLoc.x - 300, currentLoc.y));
            Space left4 = board.getSpaceFromCoord(new Coord(currentLoc.x - 400, currentLoc.y));

            if (left1.currentPiece == null && left2.currentPiece == null && left3.currentPiece == null 
            && left4.currentPiece.pieceType == 'r' && left4.currentPiece.color == p.color) {
                legalMoves.add(left4.currentPiece.position);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
    
    private boolean canCastleRight(Piece p) {
        Coord currentLoc = p.position;
        if(p.hasMoved) {
            return false;
        }
        try {
            Space right1 = board.getSpaceFromCoord(new Coord(currentLoc.x + 100, currentLoc.y));
            Space right2 = board.getSpaceFromCoord(new Coord(currentLoc.x + 200, currentLoc.y));
            Space right3 = board.getSpaceFromCoord(new Coord(currentLoc.x + 300, currentLoc.y));
            if(right1.currentPiece == null && right2.currentPiece == null && right3.currentPiece != null 
            && right3.currentPiece.pieceType == 'r' && right3.currentPiece.color == p.color) {
                legalMoves.add(right3.currentPiece.position);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private void auPassante(Piece p) {
        Coord currentLoc = p.position;
        Space right = board.getSpaceFromCoord(new Coord(currentLoc.x + 100, currentLoc.y));
        Space left = board.getSpaceFromCoord(new Coord(currentLoc.x - 100, currentLoc.y));
        GameState gs = new GameState();
        Piece previousMovedPiece = gs.getPreviousMovedPiece();

        if(right != null && right.currentPiece!= null && right.currentPiece.pieceType == p.pieceType && right.currentPiece.moveCount == 1){
            if(previousMovedPiece!= null && previousMovedPiece.notation == right.currentPiece.notation 
                && p.position.y == 400 || p.position.y == 500) {
                System.out.println("Match is valid move count " + right.currentPiece.moveCount);
                legalMoves.add(new Coord(right.XPOS, right.YPOS));
            }
        }
        else if(left!= null && left.currentPiece!= null && left.currentPiece.pieceType == p.pieceType && left.currentPiece.moveCount == 1){
            if(previousMovedPiece!= null && previousMovedPiece.notation == left.currentPiece.notation
                && p.position.y == 400 || p.position.y == 500) {
                System.out.println("Match is valid");
                legalMoves.add(new Coord(left.XPOS, left.YPOS));
            }
        }
        else {
            System.out.println("No Match");
        }
    }

    private void handlePin(Piece p) {

        GameState gs = new GameState();
        Piece King = gs.getKing(p.color);
        System.out.println("Found King! " + King.notation);
        //find any threat on the diagonals or row piece is on
        if(p.pieceType != 'k') {
            detectLocation(p, King);
        }
    }

    private boolean checkPieceTakeable(Space s, int color) {
        if(s != null && s.currentPiece == null ) {
            legalMoves.add(new Coord(s.XPOS, s.YPOS));
            return true;
        }
        else if(s != null &&s.currentPiece.color != color) {
            legalMoves.add(new Coord(s.XPOS, s.YPOS));
            return false;
        }
        else return false;
    }

    private void checkPiece(Space s, int originalPieceColor) {
        if(s != null && s.currentPiece != null && s.currentPiece.color != originalPieceColor && s.XPOS <= 800 && s.YPOS >= 100) {
            legalMoves.add(new Coord(s.XPOS, s.YPOS));
        }
        else if(s!= null && s.currentPiece == null)  {
            legalMoves.add(new Coord(s.XPOS, s.YPOS));
        }
    }

    private void checkPiecePawnTake(Space s, int originalPieceColor) {
        if(s!= null && s.currentPiece != null && s.currentPiece.color != originalPieceColor) {
            legalMoves.add(new Coord(s.XPOS, s.YPOS));
        }
    }
    private void checkPiecePawnMove(Space s, int originalPieceColor) {
        if(s!= null && s.currentPiece == null) {
            legalMoves.add(new Coord(s.XPOS, s.YPOS));
        }
    }

    private void detectLocation(Piece p, Piece King) {
        if(p.position.x == King.position.x) {
            if(p.position.y > King.position.y){
                Coord iterator = p.position;
                boolean pinned = true;
                Board b = new Board();
                while(iterator.y < King.position.y) {
                    if(b.getSpaceFromCoord(iterator).currentPiece != null) {
                       pinned = false; 
                       break;
                    }
                    else { iterator.y -= 100; }
                }
                if(pinned) {
                    System.out.println("Piece is below King " + p.notation);
                }
            }
            else {
                System.out.println("Piece is above king ");
            }
        }
        if(p.position.y == King.position.y) {
            if(p.position.x > King.position.x){
                System.out.println("Piece is on the right");
            }
            else {
                System.out.println("Piece is on the Left ");
            }
        }
        //diagonals
        Coord right = p.position;
        for(int i = 0; i < 8; i++) {
                if(King != null && right.x == King.position.x && right.y == King.position.y) {
                    System.out.println("Piece is on the bottom left of king " + p.notation + " " + King.notation); 
                    break;
                }
                Space s = getTopRightMove(right);
                if(s != null) {
                    right = new Coord(s.XPOS, s.YPOS);
                    if(s.currentPiece !=  null && s.currentPiece.pieceType != 'k') 
                    { break; }
                }
                else { break; }
        }
        Coord left = p.position;
        for(int i = 0; i < 8; i++) {
            if(King != null && left.x == King.position.x && left.y == King.position.y) {
                System.out.println("Piece is on the bottom right of king "); 
                break;
            }
            Space s = getTopLeftMove(left);
            if(s != null) {
                left = new Coord(s.XPOS, s.YPOS);
                if(s.currentPiece !=  null && s.currentPiece.pieceType != 'k') 
                { break; }
            }
            else { break; }
        }
        Coord bottomRight = p.position;
        for(int i = 0; i < 8; i++) {
            if(King != null && bottomRight.x == King.position.x && bottomRight.y == King.position.y) {
                System.out.println("Piece is on the top left of king "); 
                break;
            }
            Space s = getBottomRightMove(bottomRight);
            if(s != null) {
                bottomRight = new Coord(s.XPOS, s.YPOS);
                if(s.currentPiece !=  null && s.currentPiece.pieceType != 'k') 
                { break; }
            }
            else { break; }
        }
        Coord bottomLeft = p.position;
        for(int i = 0; i < 8; i++) {
            if(King != null && bottomLeft.x == King.position.x && bottomLeft.y == King.position.y) {
                System.out.println("Piece is on the top right of king "); 
                break;
            }
            Space s = getBottomLeftMove(bottomLeft);
            if(s != null) {
                bottomLeft = new Coord(s.XPOS, s.YPOS);
                if(s.currentPiece !=  null && s.currentPiece.pieceType != 'k') 
                { break; }
            }
            else { break; }
        }
    }

    private Space getBottomRightMove(Coord cord) { return board.getSpaceFromCoord(new Coord(cord.x + 100, cord.y + 100)); }
    private Space getBottomLeftMove(Coord cord) { return board.getSpaceFromCoord(new Coord(cord.x - 100, cord.y + 100)); }
    private Space getTopLeftMove(Coord cord) { return board.getSpaceFromCoord(new Coord(cord.x - 100, cord.y - 100)); }
    private Space getTopRightMove(Coord cord) { return board.getSpaceFromCoord(new Coord(cord.x + 100, cord.y - 100)); }
    private Space getLeftMove(Coord cord) { return board.getSpaceFromCoord(new Coord(cord.x - 100, cord.y)); }
    private Space getRightMove(Coord cord) { return board.getSpaceFromCoord(new Coord(cord.x + 100, cord.y)); }
    private Space getTopMove(Coord cord) { return board.getSpaceFromCoord(new Coord(cord.x, cord.y - 100)); }
    private Space getTop2Move(Coord cord) { return board.getSpaceFromCoord(new Coord(cord.x, cord.y - 200)); }
    private Space getBottom2Move(Coord cord) { return board.getSpaceFromCoord(new Coord(cord.x, cord.y + 200)); }
    private Space getBottomMove(Coord cord) { return board.getSpaceFromCoord(new Coord(cord.x, cord.y + 100)); }

}