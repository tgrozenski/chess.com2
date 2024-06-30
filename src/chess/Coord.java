package chess;

public class Coord {
    public int x;
    public int y;
    String notation;

    Coord(int x, int y) {
        this.x = x;
        this.y = y;
        this.notation = coordToNotation();
    }
    
    public String coordToNotation() {
        String str = "";
        str += (char) (96 + (x/100));
        str += 9 - (y/100);
        return str;
    }
}