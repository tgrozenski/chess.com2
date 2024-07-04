package chess;
import java.util.ArrayList;

public class RuleManager {

    private ArrayList<Coord> legalMoves = new ArrayList<>();
    private Board board = new Board();
    private static final int WHITE = 0;
    
    public ArrayList<Coord> getLegalMoves(Piece p) {
        switch(p.pieceType) {
            case 'k':
                getKingMoves(p);
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
                break;
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
            try {
                checkPiece(board.getSpaceFromCoord(c), p.color);
            } catch (Exception e) {
                System.out.println("Null Move");
            }
        }
    }

     private void getBishopMoves(Piece p) {
        //potential multithreading available
        Coord right = p.position, left = p.position, bottomLeft = p.position, bottomRight = p.position;
        boolean rightState = true, leftState = true, bottomRightState = true, bottomLeftState = true;
        for(int i = 0; i < 8; i++) {
            try {
                if (rightState) {
                    Space s = getTopRightMove(right);
                    rightState = checkPieceTakeable(s, p.color);
                    right = new Coord(s.XPOS, s.YPOS);
                }
            } catch (Exception e) {}
            try {
                if (leftState) {
                    Space s2 = getTopLeftMove(left);
                    leftState = checkPieceTakeable(s2, p.color);
                    left = new Coord(s2.XPOS, s2.YPOS);
                }
            } catch (Exception e) {}
            try {
                if (bottomLeftState) {
                    Space s = getBottomLeftMove(bottomLeft);
                    bottomLeftState = checkPieceTakeable(s, p.color);
                    bottomLeft = new Coord(s.XPOS, s.YPOS);
                }
            } catch (Exception e) {}
            try {
                if(bottomRightState) {
                    Space s = getBottomRightMove(bottomRight);
                    bottomRightState = checkPieceTakeable(s, p.color);
                    bottomRight = new Coord(s.XPOS, s.YPOS);
                }
            } catch (Exception e) {}
       }
    }

    private void getRookMoves(Piece p) {
        Coord right = p.position, left = p.position, bottom = p.position, top = p.position;
        boolean rightState = true, leftState = true, bottomState = true, topState= true;

        for(int i = 0; i < 7; i++) {
            try {
                if (rightState) {
                    Space s = getRightMove(right);
                    rightState = checkPieceTakeable(s, p.color);
                    right = new Coord(s.XPOS, s.YPOS);
                }
            } catch (Exception e) {}
            try {
                if (leftState) {
                    Space s2 = getLeftMove(left);
                    leftState = checkPieceTakeable(s2, p.color);
                    left = new Coord(s2.XPOS, s2.YPOS);
                }
            } catch (Exception e) {}
            try {
                if (bottomState) {
                    Space s = getBottomMove(bottom);
                    bottomState = checkPieceTakeable(s, p.color);
                    bottom = new Coord(s.XPOS, s.YPOS);
                }
            } catch (Exception e) {}
            try {
                if(topState) {
                    Space s = getTopMove(top);
                    topState = checkPieceTakeable(s, p.color);
                    top = new Coord(s.XPOS, s.YPOS);
                }
            } catch (Exception e) {}
       }
    }

    public void getPawnMoves(Piece p) {
        Coord position = new Coord(p.position.x, p.position.y);
        System.out.println("POSITION IN GET MOVES: " + position.x + " " + position.y);
        if(p.color == WHITE) {
            try {
                checkPiecePawnMove(getTopMove(position), p.color);
            } catch (Exception e) {
                System.out.println("Space is Null");
            }
            try {
                if(board.getSpaceFromCoord(new Coord(p.position.x, p.position.y - 100)).currentPiece == null){
                    checkPiecePawnMove(getTop2Move(position), p.color);
                }
            } catch (Exception e) {
                System.out.println("Space is Null");
            }
            try {
                checkPiecePawnTake(getTopLeftMove(position), p.color);
            } catch (Exception e) {
                System.out.println("Space is Null");
            }
            try {
                checkPiecePawnTake(getTopRightMove(position), p.color);
            } catch (Exception e) {
                System.out.println("Space is Null");
            }
        }
        else {
            try {
                checkPiecePawnMove(getBottomMove(position), p.color);
            } catch (Exception e) {
                System.out.println("Space is Null");
            }
            try {
                if(board.getSpaceFromCoord(new Coord(p.position.x, p.position.y + 100)).currentPiece == null){
                    checkPiecePawnMove(getBottom2Move(position), p.color);
                }
            } catch (Exception e) {
                System.out.println("Space is Null");
            }
            try {
                checkPiecePawnTake(getBottomLeftMove(position), p.color);
            } catch (Exception e) {
                System.out.println("Space is Null");
            }
            try {
                checkPiecePawnTake(getBottomRightMove(position), p.color);
            } catch (Exception e) {
                System.out.println("Space is Null");
            }
        }
    }

    public void getKingMoves(Piece p) {
        System.out.println("Piece in King moves " + p.position.x + " " + p.position.y);
        try {
            checkPiece(getBottomMove(p.position), p.color);
        } catch (Exception e) {
            System.out.println("Space is null!");
        }
        try {
            checkPiece(getTopMove(p.position), p.color);
        } catch (Exception e) {
            System.out.println("Space is null!");
        }
        try {
            checkPiece(getTopLeftMove(p.position), p.color);
        } catch (Exception e) {
            System.out.println("Space is null!");
        }
        try {
            checkPiece(getTopRightMove(p.position), p.color);
        } catch (Exception e) {
            System.out.println("Space is null!"); 
        }
        try {
            checkPiece(getBottomLeftMove(p.position), p.color);
        } catch (Exception e) {
            System.out.println("Space is null!");
        }
        try {
            checkPiece(getBottomRightMove(p.position), p.color);
        } catch (Exception e) {
            System.out.println("Space is null!"); 
        }
        try {
            checkPiece(getLeftMove(p.position), p.color);
        } catch (Exception e) {
            System.out.println("Space is null!");
        }
        try {
            checkPiece(getRightMove(p.position), p.color);
        } catch (Exception e) {
            System.out.println("Space is null!");
        }
    }
    private void checkPiece(Space s, int originalPieceColor) {
        if(s.currentPiece == null || s.currentPiece.color != originalPieceColor) {
            legalMoves.add(new Coord(s.XPOS, s.YPOS));
        }
    }

    private boolean checkPieceTakeable(Space s, int color) {
        if(s.currentPiece == null ) {
            legalMoves.add(new Coord(s.XPOS, s.YPOS));
            return true;
        }
        else if(s.currentPiece.color != color) {
            legalMoves.add(new Coord(s.XPOS, s.YPOS));
            return false;
        }
        else return false;
    }

    private void checkPiecePawnTake(Space s, int originalPieceColor) {
        if(s.currentPiece != null && s.currentPiece.color != originalPieceColor) {
            legalMoves.add(new Coord(s.XPOS, s.YPOS));
        }
    }
    private void checkPiecePawnMove(Space s, int originalPieceColor) {
        if(s.currentPiece == null) {
            legalMoves.add(new Coord(s.XPOS, s.YPOS));
        }
    }
        //TODO AU PASSANT 
        //When move is implemented keep a registry with last piece moved
        //Check this to determine if Au passant is a move
        //Pawn can take au PASSANT if pawn moved by two squares next to it on the left or right
        // top == -  
        //bottom == + 
        //left == -
        //right == + 
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