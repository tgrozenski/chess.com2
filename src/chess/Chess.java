package chess;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.*;

public class Chess {
    static GameState gameState = new GameState();
    static Board board = new Board();
    static boolean gameActive = false;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {

        System.out.println("CREATING AND SHOWING GUI");
        JFrame f = new JFrame("Chess.com 2");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //initialize the board only once 

        if(gameState.gameActive == false) {
            initializeBoard();
            gameState.gameActive = true;
        }

        JPanel panel = new JPanel() {

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(1000,1000);
            }
            public void highlightLegalMoves(Graphics g) {
                    Board board = new Board();
                    
                    gameState.getLegalMoves().forEach( c -> {
                    // System.out.println("Legal Moves " + c.x + " " + c.y + " " + c.notation + " Count:  " + counter);
                    if (c.highlight == true) {
                        try {
                            board.highlightSquare(g, "00" + c.notation);
                        } catch (Exception ex) {ex.printStackTrace();}
                    }
                    });
            }

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);

                Board board = new Board();
                board.drawNotation(g);
                board.drawSpaces(g);

                for(Piece p: gameState.getBoardPosition().values()) {
                    board.renderSpace(g, p);
                }
                if(gameState.getLegalMoves() != null) {
                    highlightLegalMoves(g);
                }
            }
        };
        panel.setBackground(Color.gray);

        panel.addMouseListener( new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleTouchEvent(panel, board, e);
            }
        });

        f.add(panel);
        f.pack();
        f.setVisible(true);
    }

    private static int roundDownNearest100(int x) {
        String xStr = "" + x;
        int tempx = (10 * Character.getNumericValue(xStr.charAt(1))) + Character.getNumericValue(xStr.charAt(2));
        x = x - tempx;
        return x;
    }

    private static void initializeBoard() {
        String[] startingPosition = {
        "1pa7", "1pb7", "1pc7", "1pd7", "1pe7", "1pf7", "1pg7", "1ph7", "1ra8", "1rh8", "1nb8", "1ng8", "1bc8", "1bf8", "1qd8", "1ke8",
        "0pa2", "0pb2", "0pc2", "0pd2", "0pe2", "0pf2", "0pg2", "0ph2", "0ra1", "0rh1", "0nb1", "0ng1", "0bc1", "0bf1", "0qd1", "0ke1", 
        };
        for(String str: startingPosition) {
            gameState.addPiece(str, new Piece(str, false, false));
            // Piece current = boardPosition.get(str);
            // System.out.println(current.notation + " " + current.position.indexX + " " + current.position.indexY  + " " + current.position.x + current.position.y);
        }
    }

    private static boolean validateMove(Coord c) {
        for(Coord current: gameState.getLegalMoves()) {
           if(c.notation.equals(current.notation)) {
            return true;
           }
        }
        return false;
    }

    public Piece getPieceFromBoard(Piece p) {
        for(Piece current: gameState.getBoardPosition().values()) {
            if(current.color == p.color && current.pieceType == 'k') {
                return current;
            }
        }
        return null;
    }

    private static void handleTouchEvent(JPanel panel, Board tempBoard, MouseEvent e) {
        Space touchedSpace = tempBoard.getSpaceFromCoord(new Coord(roundDownNearest100(e.getX()), roundDownNearest100(e.getY())));
        Coord touchedCoord = new Coord(touchedSpace.XPOS, touchedSpace.YPOS);

        Piece selectedPiece = gameState.getPreviousPiece();
        Piece target = null;
        if(tempBoard.getSpaceFromCoord(touchedCoord).currentPiece != null) {
            target = tempBoard.getSpaceFromCoord(touchedCoord).currentPiece;
        }

        if(selectedPiece != null  && validateMove(touchedCoord)) {
            //castling
            if(target!= null && target.color == selectedPiece.color) {
                Coord newKing = null, newRook = null;
                Piece king, rook;

                if (target.position.x == 800) {
                    newKing = new Coord(selectedPiece.position.x + 200, selectedPiece.position.y);
                    newRook = new Coord(target.position.x - 200, target.position.y);
                } else if(target.position.x == 100){
                    newKing = new Coord(selectedPiece.position.x - 200, selectedPiece.position.y);
                    newRook = new Coord(target.position.x + 300, target.position.y);
                }

                king = new Piece(selectedPiece.color + "" + selectedPiece.pieceType + "" + newKing.notation, false , true);
                rook = new Piece(target.color + "" + target.pieceType + "" + newRook.notation, false , true);
                king.hasMoved = true;
                rook.hasMoved = true;
                //set Old spaces to null and new to proper values
                tempBoard.setSpaceCurrentPiece(null, target.position);
                tempBoard.setSpaceCurrentPiece(null, selectedPiece.position);

                try {
                    tempBoard.setSpaceCurrentPiece(king, newKing);
                    tempBoard.setSpaceCurrentPiece(rook, newRook);
                    gameState.removePiece(target.notation);
                    gameState.removePiece(gameState.getPreviousPiece().notation);
                    gameState.addPiece(king.notation, king);
                    gameState.addPiece(rook.notation, rook);
                } catch (Exception except) {except.printStackTrace();}

                gameState.clearLegalMoves();
                gameState.setPreviousPiece(null);
                panel.repaint();

            }
            //en passant
            else if(target != null && target.pieceType == 'p' && selectedPiece.pieceType == 'p' && selectedPiece.color != target.pieceType
                && target.position.y == selectedPiece.position.y 
                ) {

                System.out.println("EN PASSANT");
                int val;
                if(target.color == 1) { val = -100; }
                else { val = 100; } 
                gameState.removePiece(target.notation);
                gameState.removePiece(selectedPiece.notation);
                Piece newPiece = new Piece(tempBoard.getNewNotation(selectedPiece, new Coord(target.position.x, target.position.y + val)), false, true);
                gameState.addPiece(newPiece.notation, newPiece);

                gameState.clearLegalMoves();
                gameState.setPreviousPiece(null);
                panel.repaint();
            }
            
            else {
                System.out.println("Standard Move Detected  Previous Piece: "); 
                if(gameState.getPreviousMovedPiece() != null) {
                    System.out.println("Previously Moved Piece " + gameState.getPreviousMovedPiece().notation);
                }

                String newNotation = tempBoard.getNewNotation(gameState.getPreviousPiece(), touchedCoord);
                Piece newPiece = new Piece(newNotation, false, true);
                newPiece.hasMoved = true;
                newPiece.moveCount += gameState.getPreviousPiece().moveCount + 1;

                gameState.removePiece(selectedPiece.notation);
                try { gameState.removePiece(target.notation); } catch (Exception ex) {}
                gameState.addPiece(newNotation, newPiece);
                tempBoard.setSpaceCurrentPiece(newPiece, touchedCoord);

                gameState.clearLegalMoves();
                checkForCheck(newPiece);
                gameState.setPreviousMovedPiece(newPiece);
                panel.repaint();
            }
        }
        else if(touchedSpace.currentPiece == null) {
            System.out.println("user has not clicked a legal move, clear the board");
            gameState.clearLegalMoves();
            panel.repaint();
        }

        else {
            System.out.println("user is clicking a piece for the first time");

            if(gameState.getPreviousMovedPiece() != null) {
                System.out.println("Previously Moved Piece " + gameState.getPreviousMovedPiece().notation);
            }
            try {
                gameState.setPreviousPiece(touchedSpace.currentPiece);
                gameState.setLegalMoves(touchedSpace.currentPiece.getLegalMoves(false, true));
                System.out.println("CURRENT CLICKED PIECE: " + gameState.getPreviousPiece().notation);
            } catch (Exception ex) {ex.printStackTrace();};
            System.out.println("Current Move Count" + touchedSpace.currentPiece.moveCount);
            panel.repaint();
        }
    }
    public static void checkForCheck(Piece p) {
       System.out.println("CHECKING FOR CHECK " + p.notation);
       gameState.setLegalMoves(p.getLegalMoves(false, false));
       for(Coord c: gameState.getLegalMoves()) {
        try {
            Piece current = board.getSpaceFromCoord(c).currentPiece;
           if(current.pieceType == 'k') {
                if(current.color == 0) {
                    gameState.setWhiteCheckStatus(true);
                    System.out.println("WHITE IN CHECK ");
                }
                else {
                    gameState.setBlackCheckStatus(true);
                    System.out.println("BLACK IN CHECK ");
                }
           }
        } catch (Exception e) {}
       }
       gameState.clearLegalMoves();
    }
}