package chess;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.*;

public class Chess {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        System.out.println("Created GUI on EDT? "+
                SwingUtilities.isEventDispatchThread());
        JFrame f = new JFrame("Chess.com");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new MyPanel());
        f.pack();
        f.setVisible(true);
    }
}

class MyPanel extends JPanel {

    public MyPanel() {
        setBorder(BorderFactory.createLineBorder(Color.black));
        setBackground(Color.gray);
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
        Space space = board.getSpace("00d1");
        System.out.println(space.XPOS + " " + space.YPOS);
//        board.clearSpace(g, "00h8");

    }
}