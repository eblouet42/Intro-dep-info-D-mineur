package mines.hayma.minesweeper;

public class Case {
    public Boolean isMarked;
    public Boolean isClicked;
    public Boolean hasMine;
    int minesVoisines;

    public Case() {
        hasMine = false;
        isClicked = false;
        isMarked = false;
        minesVoisines = 0;

    }

    public void setMine(Boolean bool) {
        hasMine = bool;
    }

    public void click(boolean caseCliquée){
        isClicked = true;

    }

    public void setMinesVoisines(int n){
        minesVoisines = n;
    }
    public void mark() {
        isMarked = true;
    }
}
