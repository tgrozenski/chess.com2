package chess;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.*;

public class Chess {
    static ArrayList<Coord> legalMoves; 
    static HashMap<String, Piece> boardPosition = new HashMap<>(); 
    static Piece lastClickedPiece;
    static int counter;
    static boolean gameActive = false;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {

        JFrame f = new JFrame("Chess.com 2");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //initialize the board only once 

        if(gameActive == false) {
            initializeBoard();
            gameActive = true;
        }

        JPanel panel = new JPanel() {

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(1000,1000);
            }
            public void highlightLegalMoves(Graphics g) {
                    Board board = new Board();
                    legalMoves.forEach( c -> {
                    // System.out.println("Legal Moves " + c.x + " " + c.y + " " + c.notation + " Count:  " + counter);
                    counter++;
                    try {
                        board.highlightSquare(g, "00" + c.notation);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    });
            }

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);

                Board board = new Board();
                board.drawNotation(g);
                board.drawSpaces(g);

                for(Piece p: boardPosition.values()) {
                    board.renderSpace(g, p);
                }
                if(legalMoves != null) {
                    highlightLegalMoves(g);
                }
            }
        };
        panel.setBackground(Color.gray);

        Board tempBoard = new Board();
        panel.addMouseListener( new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleTouchEvent(panel, tempBoard, e);
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
            boardPosition.put(str, new Piece(str, false, false));
            // Piece current = boardPosition.get(str);
            // System.out.println(current.notation + " " + current.position.indexX + " " + current.position.indexY  + " " + current.position.x + current.position.y);
        }
    }

    private static boolean validateMove(Coord c) {
        for(Coord current: legalMoves) {
           if(c.notation.equals(current.notation)) {
            return true;
           }
        }
        return false;
    }
    private static void handleTouchEvent(JPanel panel, Board tempBoard, MouseEvent e) {
        counter = 1;
        Space touchedSpace = tempBoard.getSpaceFromCoord(new Coord(roundDownNearest100(e.getX()), roundDownNearest100(e.getY())));
        Coord touchedCoord = new Coord(touchedSpace.XPOS, touchedSpace.YPOS);

        if(lastClickedPiece != null  && validateMove(touchedCoord)) {
            // System.out.println("VALID MOVE DETECTED Current Clicked " + lastClickedPiece.notation);
            Piece target = tempBoard.getSpaceFromCoord(touchedCoord).currentPiece; 
            if(target!= null && target.color == lastClickedPiece.color) {

                Coord newKing = new Coord(lastClickedPiece.position.x + 200, lastClickedPiece.position.y);
                Coord newRook = new Coord(target.position.x - 200, target.position.y);
                Piece king = new Piece(lastClickedPiece.color + "" + lastClickedPiece.pieceType + "" + newKing.notation, false , false);
                Piece rook = new Piece(target.color + "" + target.pieceType + "" + newRook.notation, false , false);

                //set Old spaces to null and new to proper values
                tempBoard.setSpaceCurrentPiece(null, target.position);
                tempBoard.setSpaceCurrentPiece(null, lastClickedPiece.position);
                tempBoard.setSpaceCurrentPiece(king, newKing);
                tempBoard.setSpaceCurrentPiece(rook, newRook);

                boardPosition.remove(target.notation);
                boardPosition.remove(lastClickedPiece.notation);
                boardPosition.put(king.notation, king);
                boardPosition.put(rook.notation, rook);

                legalMoves.clear();
                lastClickedPiece = null;
                panel.repaint();

            }
            else {
                System.out.println("Standard Move Detected");
                String newNotation = tempBoard.getNewNotation(lastClickedPiece, touchedCoord);
                Piece newPiece = new Piece(newNotation, false, false);
                Piece previousPiece = tempBoard.getSpaceFromCoord(touchedCoord).currentPiece;

                boardPosition.remove(lastClickedPiece.notation);
                try { boardPosition.remove(previousPiece.notation); } catch (Exception ex) {}
                boardPosition.put(newNotation, newPiece);
                tempBoard.setSpaceCurrentPiece(newPiece, touchedCoord);

                legalMoves.clear();
                lastClickedPiece = null;
                panel.repaint();
            }
        }
        else if(touchedSpace.currentPiece == null) {
            System.out.println("user has not clicked a legal move, clear the board");
            legalMoves.clear();
            panel.repaint();
            lastClickedPiece = null;
        }
        else {
            System.out.println("user is clicking a piece for the first time");
            try {
                lastClickedPiece = touchedSpace.currentPiece;
                legalMoves = touchedSpace.currentPiece.getLegalMoves();
                System.out.println("CURRENT CLICKED PIECE: " + lastClickedPiece.notation);
            } catch (Exception ex) {ex.printStackTrace();};
            panel.repaint();
        }
    }
}