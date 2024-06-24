package mines.hayma.minesweeper;

import java.util.Vector;

public class Grille {
    int nbCase;
    Vector<Case> Cases;

    public Grille(){

    }

    public void click(int x, int y, Boolean caseCliquée) {
        int num = toNum(x,y);
        Cases.get(num).click(caseCliquée);
    }

    public int toNum(int x, int y) {
        return y*nbCase + x;
    }
    public int[] toCoord(int num) {
        return new int[]{num%nbCase, num/nbCase};
    }

}
