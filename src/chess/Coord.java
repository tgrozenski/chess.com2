package chess;

public class Coord {
    public int x;
    public int y;
    public int indexX;
    public boolean highlight = true;
    public int indexY;
    public String notation;

    Coord(int x, int y) {
        this.x = x;
        this.y = y;
        this.notation = coordToNotation();
        this.indexX = positionToIndex(x, indexX);
        this.indexY = positionToIndex(y, indexY);
    }
    
    public int positionToIndex(int xPos, int index) {
        return xPos / 100 - 1;
    }
    public String coordToNotation() {
        String str = "";
        str += (char) (96 + (x/100));
        str += 9 - (y/100);
        return str;
    }

    @Override
    public boolean equals(Object other) {
    if (!(other instanceof Coord)) {
            return false;
        }
        Coord that = (Coord) other;
        return this.notation.equals(that.notation);
    }
}