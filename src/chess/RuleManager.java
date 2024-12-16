package chess;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;

public class RuleManager {

    private ArrayList<Coord> legalMoves = new ArrayList<>();
    private Board board = new Board();
    private static final int WHITE = 0;
    private boolean highlightState;
    private HashMap<String, Supplier<Space>> commandList = new HashMap<>(); 
    
    public ArrayList<Coord> getLegalMoves(Piece p, boolean check, boolean highlight) {
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

        ArrayList<Coord> temp = new ArrayList<>();
        if(p.pieceType != 'k') {
            GameState gs = new GameState();
            Crawler crawler = new Crawler();

            //determine king relationship
            pinnedTo pinnedState = pinnedTo.NOT_PINNED;
            pinnedTo relation = crawler.relationToKing(p.position, gs.getKing(p.color));

            //determine pin
            if(relation != pinnedTo.NOT_PINNED) {
                crawler.crawlSpaces(p.color, p.position, relation);
                if(crawler.enemyPiece != null) {
                    if(crawler.threatPresent(relation, crawler.enemyPiece) && crawler.teamSpaces.size() == 0) {
                        pinnedState = relation; 
                    }
                }
            }

            //filter moves
            if(pinnedState != pinnedTo.NOT_PINNED) {
                for(Coord c: legalMoves) {
                    for(Coord c2: crawler.emptySpaces.values()) {
                        if(c.notation.charAt(0) == c2.notation.charAt(0) && c.notation.charAt(1) == c2.notation.charAt(1)) {
                            temp.add(c);
                        }
                    }
                    if(crawler.enemyPiece.position.notation.charAt(0) == c.notation.charAt(0) && crawler.enemyPiece.position.notation.charAt(1) == c.notation.charAt(1)) {
                        temp.add(c);
                    }
                }
                return temp;
            }
        }
        else {
            ArrayList<Coord> restrictedMoves = this.getAllMoves(p.color);
            for(Coord coord: restrictedMoves) {
                for(int i = 0; i < legalMoves.size(); i++) {
                    if(legalMoves.get(i).x == coord.x && legalMoves.get(i).y == coord.y) {
                        legalMoves.remove(legalMoves.get(i));
                    }
                }
            }

            
        }
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
                checkPieceTakeable(board.getSpaceFromCoord(c), p.color);
            }
        }
    }

    public boolean knightPresent(Piece p) {
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
                Piece piece = board.getSpaceFromCoord(c).currentPiece; 
                if(piece != null && piece.color == p.color && piece.pieceType == 'n') {
                    return true;
                }
            }
        }
        return false;
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
        if(!check) {
            canCastleLeft(p);
            canCastleRight(p);
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

    private boolean checkEnPassantValid(Piece p, Space RorL, Piece previousPiece) {
        int valid = 0;
        if(RorL!= null && RorL.currentPiece!= null && previousPiece!= null ) {
            System.out.println(p.color+ " " + RorL.currentPiece.color + " " + previousPiece.color);
            valid = (RorL.currentPiece.pieceType == p.pieceType) ? valid : 1;
            valid = (RorL.currentPiece.moveCount == 1) ? valid : 1;
            valid = (previousPiece.notation == RorL.currentPiece.notation) ? valid : 1;
            valid = (p.color != previousPiece.color) ? valid : 1; 
            valid = (p.position.y == 400 || p.position.y == 500) ? valid : 1; 
        }
        else {
            valid = 1;
        }
        return (valid != 0) ? false : true;
    }

    private void auPassante(Piece p) {
        Coord currentLoc = p.position;
        Space right = board.getSpaceFromCoord(new Coord(currentLoc.x + 100, currentLoc.y));
        Space left = board.getSpaceFromCoord(new Coord(currentLoc.x - 100, currentLoc.y));
        GameState gs = new GameState();
        Piece previousMovedPiece = gs.getPreviousMovedPiece();

        if(checkEnPassantValid(p, right, previousMovedPiece)) {
            legalMoves.add(new Coord(right.XPOS, right.YPOS));
        }
        if(checkEnPassantValid(p, left, previousMovedPiece)) {
            legalMoves.add(new Coord(left.XPOS, left.YPOS));
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
            if(!findDefender(s.currentPiece)) {
                legalMoves.add(new Coord(s.XPOS, s.YPOS));
            }
        }
        else if(s!= null && s.currentPiece == null)  {
            legalMoves.add(new Coord(s.XPOS, s.YPOS));
        }
    }
    private boolean findDefender(Piece target) {
        GameState gs = new GameState();
        Crawler crawler = new Crawler();
        Piece king = gs.getPreviousMovedPiece();

        //check for King/Rook
        for(pinnedTo pin: pinnedTo.values()) {
            crawler.crawlSpaces(target.color, target.position, pin);
            for (Coord c: crawler.teamSpaces.values()) {
                if(board.getSpaceFromCoord(c).currentPiece != null) {
                    Piece p = board.getSpaceFromCoord(c).currentPiece;
                    if(p.pieceType == 'q') {
                        return true;
                    }
                    else if(pin == pinnedTo.LEFT || pin == pinnedTo.RIGHT || pin == pinnedTo.BOTTOM || pin == pinnedTo.TOP) {
                        if(p.pieceType == 'r') {
                            return true;
                        }
                    }
                    else if(pin == pinnedTo.TOP_LEFT || pin == pinnedTo.TOP_RIGHT || pin == pinnedTo.BOTTOM_LEFT || pin == pinnedTo.BOTTOM_RIGHT) {
                        if(p.pieceType == 'b') {
                            return true;
                        }
                    }
                }
            }
        }

        //check for knight
        if(knightPresent(target)) {
            return true;
        }

        return false;
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

    public ArrayList<Coord> getAllMoves(int color) {
        ArrayList<Coord> allMoves = new ArrayList<>();
        GameState gs = new GameState();
        HashMap<String,Piece> boardPos = gs.getBoardPosition();

        //temporarily remove the king to get the moves past the king
        Piece King = gs.getKing(color);
        Board.spacesArr[King.position.indexX][King.position.indexY].currentPiece = null;
        
        for(Piece p : boardPos.values()) {
            if(p.color != color && p.pieceType != 'k' && p.pieceType != 'p') {
                for(Coord move: p.getLegalMoves(false, false)) {
                    allMoves.add(move);
                }
            }
            else if(p.pieceType == 'p' && p.color != color) {
                if(p.color == WHITE) {
                    Space right = getTopRightMove(p.position);
                    Space left = getTopLeftMove(p.position);
                    if(right != null) {
                        allMoves.add(new Coord(right.XPOS, right.YPOS));
                    }
                    if(left != null) {
                        allMoves.add(new Coord(left.XPOS, left.YPOS));
                    }
                }
                else {
                    Space right = getBottomRightMove(p.position);
                    Space left = getBottomLeftMove(p.position);
                    if(right != null) {
                    allMoves.add(new Coord(right.XPOS, right.YPOS));
                    }
                    if(left != null) {
                    allMoves.add(new Coord(left.XPOS, left.YPOS));
                    }
                }
            }
            else if(p.pieceType == 'k' && p.color != color) {
                ArrayList<Coord> array = new ArrayList<>();
                array = returnKingMoves(p.position);
                for(Coord c: array) {
                    allMoves.add(c);
                }
            }
        }
        //put the king back in
        Board.spacesArr[King.position.indexX][King.position.indexY].currentPiece = King;

        return allMoves;
    }
    public void initCommandMap(Coord position) {
        commandList.put("br", () -> getBottomRightMove(position));
        commandList.put("t", () -> getTopMove(position));
        commandList.put("tl", () -> getTopLeftMove(position));
        commandList.put("tr", () -> getTopRightMove(position));
        commandList.put("bl", () -> getBottomLeftMove(position));
        commandList.put("l", () -> getLeftMove(position));
        commandList.put("r", () -> getRightMove(position));
        commandList.put("b", () -> getBottomMove(position));
    }

    private ArrayList<Coord> returnKingMoves(Coord position) {
       ArrayList<Coord> arr = new ArrayList<>();
       Space s = getBottomRightMove(position);
       if(s != null) {
        arr.add(new Coord(s.XPOS, s.YPOS));
       }
        String[] directions = { "br", "t", "tl", "tr", "bl", "bl", "l", "r", "b" };
        initCommandMap(position);  

        for(String dir: directions) {
            Space space = commandList.get(dir).get();
            if(space != null) {
                arr.add(new Coord(space.XPOS, space.YPOS));
            }
        }
       return arr;
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