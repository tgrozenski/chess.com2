package chess;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.awt.*;

public class Chess {
    static ArrayList<Coord> legalMoves; 
    static ArrayList<String> boardPosition = new ArrayList<>(); 
    static Piece clickedPiece;
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
            System.out.println("Board being inititalized");
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
                    System.out.println("Legal Moves " + c.x + " " + c.y + " " + c.notation + " Count:  " + counter);
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

                for(String str: boardPosition) {
                    Piece piece = new Piece(str);
                    board.renderSpace(g, piece);
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
                counter = 1;
                Space touchedSpace = tempBoard.getSpaceFromCoord(new Coord(roundDownNearest100(e.getX()), roundDownNearest100(e.getY())));
                Coord touchedCoord = new Coord(touchedSpace.XPOS, touchedSpace.YPOS);
                System.out.println("Pressed " + touchedSpace.XPOS + " " + touchedSpace.YPOS);
                //user has already clicked piece and is attempting a legal move
                if(clickedPiece != null  && validateMove(touchedCoord)) {
                    // System.out.println("VALID MOVE DETECTED Current Clicked " + clickedPiece.notation);
                    // System.out.println("Touched Space! " + touchedCoord.notation);
                    try {
                        String pieceToDelete = touchedSpace.currentPiece.notation;
                        boardPosition.remove(pieceToDelete);
                    } catch (Exception ex) {}
                    boardPosition.set(getIndexBoardPosition(clickedPiece.notation), tempBoard.getNewNotation(clickedPiece, touchedCoord));
                    tempBoard.setSpaceCurrentPiece(clickedPiece, touchedCoord);
                    legalMoves.clear();
                    clickedPiece = null;
                    panel.repaint();
                }
                else if(touchedSpace.currentPiece == null) {
                    System.out.println("user has not clicked a legal move, clear the board");
                    legalMoves.clear();
                    panel.repaint();
                    clickedPiece = null;
                }
                //user is clicking a piece for the first time
                else {
                    System.out.println("user is clicking a piece for the first time");
                    try {
                        clickedPiece = touchedSpace.currentPiece;
                        legalMoves = touchedSpace.currentPiece.getLegalMoves();
                        System.out.println("CURRENT CLICKED PIECE: " + clickedPiece.notation);
                    } catch (Exception ex) {};
                    panel.repaint();
                }
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
            boardPosition.add(str);
        }
    }
    private static int getIndexBoardPosition(String notation) {
        for(int i = 0; i < boardPosition.size(); i++) {
            if(boardPosition.get(i).equals(notation)) {
                return i;
            }
        }
        return 9999;
    }

    private static boolean validateMove(Coord c) {
        System.out.println("Validating");
        for(Coord current: legalMoves) {
            System.out.println(c.notation + " vs " + current.notation);
           if(c.notation.equals(current.notation)) {
            return true;
           }
        }
        return false;
    }
}