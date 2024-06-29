package mines.hayma.minesweeper;
import java.util.Random;

public class Grille {
    private final int lignes;
    private final int colonnes;
    public static int nbMines;
    private final Case[][] cases;
    public int nbcasesdecouvertes = 0;

    private boolean generation=false;

    public Case[][] getCases() {
        return cases;
    }

    public Grille(int lignes, int colonnes, int nbMines){
        this.lignes = lignes;
        this.colonnes = colonnes;
        Grille.nbMines = nbMines;
        cases = new Case[lignes][colonnes];
        for (int i=0; i<lignes; i++){
            for (int j=0; j<colonnes; j++){
                cases[i][j] = new Case();
            }
        }
    }
    private void initialisation(int startligne,int startcolonne) {
        placerMines(startligne,startcolonne);
        calculerVoisins();
        generation=true;
    }

    private Boolean estDansLaGrille(int x, int y){
        return x >= 0 && x < lignes && y >= 0 && y < colonnes;
    }

    private void calculerVoisins() {
        for (int i=0; i<lignes; i++){
            for (int j=0; j<colonnes; j++){
                int compteur = 0;
                for (int di=-1; di<2; di++){
                    for (int dj=-1; dj<2; dj++){
                        if (estDansLaGrille(i+di,j+dj)) {
                            if (cases[i+di][j+dj].hasMine) {
                                compteur++;
                            }
                        }
                    }
                }
                cases[i][j].setMinesVoisines(compteur);
            }
        }
    }

    private void placerMines(int startligne, int startcolonne) {
        Random random = new Random();
        int minesPlacees = 0;
        while (minesPlacees<nbMines){
            int ligne = random.nextInt(lignes);
            int colonne = random.nextInt(colonnes);
            if (!cases[ligne][colonne].hasMine && !((ligne>=startligne-1) && (ligne <= startligne+1) && (colonne>=startcolonne-1) && (colonne <= startcolonne+1))) {
                cases[ligne][colonne].setMine(true);
                minesPlacees++;
            }
        }
    }

    public String click(int x, int y) {
        if (!generation){
            initialisation(x,y);
        }
        else{
            if (!cases[x][y].isMarked && !cases[x][y].isClicked) {
                cases[x][y].click();
                nbcasesdecouvertes++;
                if (cases[x][y].minesVoisines == 0) {//clique les voisins si pas de mines autour
                    for (int dx = -1; dx < 2; dx++) {
                        for (int dy = -1; dy < 2; dy++) {
                            if (estDansLaGrille(x + dx, y + dy)) {
                                if (!cases[x + dx][y + dy].isClicked) {
                                    click(x + dx, y + dy);
                                }
                            }
                        }
                    }
                }
                if (cases[x][y].hasMine){
                    return "boom";
                }
            }
        }
        return null;
    }


    public int getNbCase() {
        return lignes*colonnes;
    }

    public int getLignes() {
        return lignes;
    }

    public int getColonnes(){
        return colonnes;
    }

    public int getNbMines() {return nbMines;}
}