package chess;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Objects;
import javax.imageio.ImageIO;

public class Board {

    public static final int boardWidthLength= 800;
    public static final int spaceWidthLength = 100;
    public static final Color primaryColor = new Color(173, 216, 230);
    public static final Color secondaryColor = new Color(167, 199, 231);
    public static final Color highlightColor = new Color(255,255,102);
    private static Color currentColor = primaryColor;
    private static Space[][] spacesArr = new Space[8][8];

    public Space[][] drawSpaces(Graphics g) {
        for (int currentY = spaceWidthLength, outer = 0; currentY <= boardWidthLength; currentY+=100, outer++) {
            for(int currentX = spaceWidthLength, inner = 0; currentX <= boardWidthLength; currentX+=100, inner++) {
                g.setColor(currentColor);
                g.fillRect(currentX, currentY, spaceWidthLength, spaceWidthLength); 
                spacesArr[outer][inner] = new Space(currentColor, currentX, currentY);
                // System.out.println(spacesArr[outer][inner].XPOS + " " +  spacesArr[outer][inner].YPOS + " " + " Outer: " + outer + " Inner: " + inner);
                colorSwap();
            }
            colorSwap();
        }
        return spacesArr;
    }

    public void drawNotation(Graphics g) {
        String iterator = "8";
        Font currentFont = g.getFont();
        g.setFont(currentFont.deriveFont(currentFont.getSize() * 2.5F)); 
        for (int currentY = spaceWidthLength + 65; currentY <= boardWidthLength + 100; currentY+=100) {
            g.drawString(iterator, 50, currentY); 
            iterator = (Integer.parseInt(iterator) - 1) + "";
        }
        iterator = "a";
        for (int currentX= spaceWidthLength + 35; currentX <= boardWidthLength + 100; currentX+=100) {
            g.drawString(iterator, currentX, 950); 
            int charValue = iterator.charAt(0); 
            iterator = String.valueOf((char) (charValue + 1));
        }
    }

    private void colorSwap() {
        if(currentColor == primaryColor) {
            currentColor = secondaryColor;
        }
        else {
            currentColor = primaryColor;
        }
    }

    public void renderSpace(Graphics g, Piece piece) {
        //Draw the piece
        try {
            Image img = ImageIO.read(Objects.requireNonNull(getClass().getResource(piece.imagePath)));
            System.out.println("Image Path" + piece.imagePath);
            g.drawImage(img, piece.position[0], piece.position[1], null);
        } 
        catch (Exception ex) {
            ex.printStackTrace();
        }
        //Save it in the array
       int[] indices = getIndexfromCoords(piece.position[0], piece.position[1]);
       System.out.println("COORDS: " + indices[0] + " " + indices[1]);
       spacesArr[indices[0]][indices[1]].currentPiece = piece;
    }

    public void clearSpace(Graphics g, String notation) {
        Space space = this.getSpace(notation);
        System.out.println( space.currentPiece.position[0] + " " + space.currentPiece.position[0]);
        g.setColor(space.spaceColor);
        g.fillRect(space.YPOS, space.XPOS, spaceWidthLength, spaceWidthLength);
    }

    public int[] getIndexfromCoords(int XPOS, int YPOS) {
        return new int[] {XPOS / 100 - 1, YPOS / 100 - 1};
    }
    
    public Color getColor(String notation) {
        Piece myPiece = new Piece(notation);
        int[] indices = getIndexfromCoords(myPiece.position[0], myPiece.position[1]);
        return spacesArr[indices[0]][indices[1]].spaceColor;
    }

    public Space getSpace(String notation) {
        Piece piece = new Piece(notation);
        System.out.println(piece.position[0]+ " " + piece.position[1]);
        int[] indices = getIndexfromCoords(piece.position[0], piece.position[1]);
        System.out.println(indices[0] + " " + indices[1]);
        System.out.println();
        return spacesArr[indices[0]][indices[1]];
    }
}