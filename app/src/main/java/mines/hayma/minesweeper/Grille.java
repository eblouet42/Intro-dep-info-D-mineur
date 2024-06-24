package mines.hayma.minesweeper;

import java.util.Random;

public class Grille {
    int lignes;
    int colonnes;
    int nbMines;
    Case[][] cases;

    public Grille(int lignes, int colonnes, int nbMines){
        this.lignes = lignes;
        this.colonnes = colonnes;
        this.nbMines = nbMines;
        cases = new Case[lignes][colonnes];
        initialisation();

    }

    private void initialisation() {
        for (int i=0; i<lignes; i++){
            for (int j=0; j<colonnes; j++){
                cases[i][j] = new Case();

            }
        }
        placerMines();
        calculervoisins();
    }

    private Boolean estDansLaGrille(int x, int y){
        return x >= 0 && x < lignes && y >= 0 && y < colonnes;
    }

    private void calculervoisins() {
        for (int i=0; i<lignes; i++){
            for (int j=0; j<colonnes; j++){
                int compteur = 0;
                for (int di=-1; di<2; di++){
                    for (int dj=-1; dj<2; dj++){
                        if (estDansLaGrille(i+di,j+dj)) {
                            compteur++;
                        }
                    }
                }
                cases[i][j].setMinesVoisines(compteur);

            }
        }
    }

    private void placerMines() {
        Random random = new Random();
        int minesPlacées = 0;
        while (minesPlacées<nbMines){
            int ligne = random.nextInt(lignes);
            int colonne = random.nextInt(colonnes);
            if (!cases[ligne][colonne].hasMine) {
                cases[ligne][colonne].setMine(true);
                minesPlacées++;
            }
        }
    }

    public void click(int x, int y) {
        cases[x][y].click();
        if (cases[x][y].minesVoisines == 0) {//clique les voisins si pas de mines autour
            for (int dx=-1; dx<2; dx++){
                for (int dy=-1; dy<2; dy++){
                    if (estDansLaGrille(x+dx,y+dy)) {
                        click(x+dx,y+dy);
                    }
                }
            }
        }
    }



}
