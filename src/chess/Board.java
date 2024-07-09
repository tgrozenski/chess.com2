package chess;

import java.awt.*;
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
    public static Piece lastPieceMoved;

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
            // System.out.println(piece.imagePath + " " + piece.position.x + " " + piece.position.y);
            g.drawImage(img, piece.position.x, piece.position.y, null);
        } 
        catch (Exception ex) {
            ex.printStackTrace();
        }
        //Save it in the array
       int[] indices = getIndexfromCoords(piece.position.x, piece.position.y);
       spacesArr[indices[0]][indices[1]].currentPiece = piece;
    //    System.out.println(piece.imagePath + " " + indices[0] + " " + indices[1]);
    }

    public void clearSpace(Graphics g, String notation) {
        Space space = this.getSpace(notation);
        g.setColor(space.spaceColor);
        g.fillRect(space.XPOS, space.YPOS, spaceWidthLength, spaceWidthLength);
    }

    public void highlightSquare(Graphics g, String notation) {
        Space space = this.getSpace(notation);
        g.setColor(highlightColor);
        g.fillRect(space.XPOS, space.YPOS, spaceWidthLength, spaceWidthLength);
        if(space.currentPiece != null) {
            Piece piece = space.currentPiece; 
            try {
                Image img = ImageIO.read(Objects.requireNonNull(getClass().getResource(piece.imagePath)));
                g.drawImage(img, piece.position.x, piece.position.y, null);
            } catch (Exception e) {}
            // System.out.println(piece.imagePath + " " + piece.position.x + " " + piece.position.y);
        }
    }

    public int[] getIndexfromCoords(int XPOS, int YPOS) {
        return new int[] {XPOS / 100 - 1, YPOS / 100 - 1};
    }
    
    public Color getColor(String notation) {
        Piece myPiece = new Piece(notation, false, false);
        return spacesArr[myPiece.position.indexX][myPiece.position.indexY].spaceColor;
    }

    public Space getSpace(String notation) {
        Piece piece = new Piece(notation, false, false);
        int[] indices = getIndexfromCoords(piece.position.x, piece.position.y);
        return spacesArr[indices[0]][indices[1]];
    }

    public Space getSpaceFromCoord(Coord coord) {
        if(coord.x > 800 || coord.y > 800) {
            return null;
        }
        return spacesArr[coord.indexX][coord.indexY];
    }

    public void setSpaceCurrentPiece(Piece p, Coord c) {
        if(p == null) {
            spacesArr[c.indexX][c.indexY].currentPiece = null;
        }
        else {
            spacesArr[c.indexX][c.indexY].currentPiece = p;
            System.out.println("Updated Array: " + c.indexX + " " + c.indexY + " " +  spacesArr[c.indexX][c.indexY].currentPiece.notation);
        }
    }

    public String getNewNotation(Piece p, Coord dest) {
        return p.color + "" +  p.pieceType + dest.notation;
    }
}