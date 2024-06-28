package chess;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.BorderFactory;
import java.awt.*;

public class Chess {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        System.out.println("Created GUI on EDT? "+
                SwingUtilities.isEventDispatchThread());
        JFrame f = new JFrame("Chess.com 2");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new MyPanel();
        Board tempBoard = new Board();
        panel.addMouseListener( new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println( "Pressed! : " +  e.getX() + " " + e.getY());
                int[] touchedCoords = roundDownNearest100(e.getX(), e.getY());
                Space touchedSpace = tempBoard.getSpaceFromXY(touchedCoords[0], touchedCoords[1]);
                System.out.println(touchedSpace.currentPiece.imagePath);
            }
        });

        f.add(panel);
        f.pack();
        f.setVisible(true);
    }
    private static int[] roundDownNearest100(int x, int y) {
        int[] rounded = new int[2];
        String xStr = "" + x;
        int tempx = (10 * Character.getNumericValue(xStr.charAt(1))) + Character.getNumericValue(xStr.charAt(2));
        System.out.println("TempX " + tempx);
        rounded[0] = x - tempx;

        String yStr = "" + y;
        int tempy = (10 * Character.getNumericValue(yStr.charAt(1))) + Character.getNumericValue(yStr.charAt(2));
        System.out.println("TempY " + tempy);
        rounded[1] = y - tempy;
        System.out.println(rounded[0] + " " + rounded[1]);
        return rounded;
    }
}

class MyPanel extends JPanel {

    public MyPanel() {
        setBorder(BorderFactory.createLineBorder(Color.black));
        setBackground(Color.gray);

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println( "Pressed! : " +  e.getX() + " " + e.getY());
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                System.out.println( "Released! : " +  e.getX() + " " + e.getY());
            }
        });
    }
    public Dimension getPreferredSize() {
        return new Dimension(1000,1000);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Board board = new Board();
        board.drawNotation(g);
        board.drawSpaces(g);
        String[] startingPosition =
                {"1pa7", "1pb7", "1pc7", "1pd7", "1pe7", "1pf7", "1pg7", "1ph7", "1ra8", "1rh8", "1nb8", "1ng8", "1bc8", "1bf8", "1qd8", "1ke8",
                        "0pa2", "0pb2", "0pc2", "0pd2", "0pe2", "0pf2", "0pg2", "0ph2", "0ra1", "0rh1", "0nb1", "0ng1", "0bc1", "0bf1", "0qd1", "0ke1"};
        for(String str: startingPosition) {
            Piece piece = new Piece(str);
            board.renderSpace(g, piece);
        }
        System.out.println(board.getColor("00f3"));
        // Space space = board.getSpace("00d1");
//        board.clearSpace(g, "00h8");

    }
}