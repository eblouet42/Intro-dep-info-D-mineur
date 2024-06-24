package mines.hayma.minesweeper;

public class Case {
    public Boolean isMarked;
    public Boolean isClicked;
    public Boolean hasMine;

    //coordonnées de la case
    private int x;
    private int y;
    private Grille grille;
    int minesVoisines;

    public Case(Boolean hasMine, int x, int y, Grille grille) {
        this.hasMine = hasMine;
        isClicked = false;
        isMarked = false;
        this.x = x;
        this.y = y;
        this.grille = grille;

    }

    public void setMine(Boolean bool) {
        hasMine = bool;
    }

    public void click(boolean caseCliquée){
        isClicked = true;
        if (!caseCliquée) {
            grille.click(x-1,y-1,false);
            grille.click(x,y-1,false);
            grille.click(x+1,y-1,false);

            grille.click(x-1,y,false);
            grille.click(x+1,y,false);

            grille.click(x-1,y+1,false);
            grille.click(x,y+1,false);
            grille.click(x+1,y+1,false);
        }
    }

    public void mark() {
        isMarked = true;
    }
}
