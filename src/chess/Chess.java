package chess;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.BorderFactory;
import java.util.Objects;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.awt.*;

public class Chess {
    static ArrayList<Coord> legalMoves; 

    static int counter;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {

        JFrame f = new JFrame("Chess.com 2");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
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
                String[] startingPosition = {
                "1pa7", "1pb7", "1pc7", "1pd7", "1pe7", "1pf7", "1pg7", "1ph7", "1ra8", "1rh8", "1nb8", "1ng8", "1bc8", "1bf8", "1qd8", "1ke8",
                "0pa2", "0pb2", "0pc2", "0pd2", "0pe2", "0pf2", "0pg2", "0ph2", "0ra1", "0rh1", "0nb1", "0ng1", "0bc1", "0bf1", "0qd1", "0ke1", "1rd4", "0bd3", "0ke5" 
                };

                for(String str: startingPosition) {
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
                System.out.println("Pressed " + touchedSpace.XPOS + " " + touchedSpace.YPOS);
                if(touchedSpace.currentPiece == null) {
                    legalMoves.clear();
                    panel.repaint();
                }
                else{
                    try {
                        legalMoves = touchedSpace.currentPiece.getLegalMoves();
                    } catch (Exception ex) {};
                    if(touchedSpace.currentPiece != null) {
                        panel.repaint();
                    }
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
}