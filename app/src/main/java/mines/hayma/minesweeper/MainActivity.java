package mines.hayma.minesweeper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private Grille grille;
    private MineAdapter mineAdapter;
    private boolean debut=false;
    private boolean fingame=false;
    private TextView minesrestantes;
    private int nbdrapos = 0;
    private MediaPlayer mediaPlayer;
    private int lamusic;
    ImageView caseSelect;
    int x;
    int y;
    int colorSelected=Color.parseColor("#000000");
    int colorNormal=Color.TRANSPARENT;
    private ImageButton btnFlag;
    private ImageButton btnDiscover;

    private long lastClickTime = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent appelant = getIntent();
        int hauteur = appelant.getIntExtra("hauteur",0);
        int longueur = appelant.getIntExtra("longueur",0);
        int nbMines = appelant.getIntExtra("mines",0);
        int difficulte= appelant.getIntExtra("difficulté",0);
        gamemusic(difficulte);

        // Initialisation de la grille;
        grille = new Grille(hauteur, longueur, nbMines);

        // Le chrono il est là
        Chronometer chrono=findViewById(R.id.Chronometer);
        chrono.setOnChronometerTickListener(chronometer -> {
            long elapsedMillis=SystemClock.elapsedRealtime()-chronometer.getBase();
            chronometer.setText(elapsedMillis/1000+" s");
        });

        // Le compteur de mines restantes
        minesrestantes=findViewById(R.id.Minesrestantes);
        updateminesrestantes();

        // La gridView en elle-même
        GridView gridView = findViewById(R.id.gridView);
        gridView.setNumColumns(longueur);

        // L'adapteur qui actualise tous les objets de la gridView
        mineAdapter = new MineAdapter(this, grille);
        gridView.setAdapter(mineAdapter);

        btnFlag = findViewById(R.id.btnFlag);
        btnDiscover = findViewById(R.id.btnDiscover);

        // Lorsque l'utilisateur clique sur un élément qui est repéré par sa view, sa position, son id...
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            x = position/grille.getColonnes();
            y = position%grille.getColonnes();
            if (!fingame) {
                if (caseSelect != null) {
                    caseSelect.setBackgroundColor(colorNormal);
                }
                //revealSamuel();

                // Si on clique deux fois en moins de 500ms, dévoile les cases autour s'il a autant de drapeaux autour que de mines adjacentes.
                // Sinon, on ne peut juste pas sélectionner une case déjà découverte

                if (grille.getCases()[x][y].isClicked) {
                    long currentTime = System.currentTimeMillis();
                    // On regarde si le joueur a le droit de découvrir toutes les cases autour
                    boolean perdu = false;
                    if (currentTime - lastClickTime < 500) {
                        int drapoautour = 0;
                        for (int dx = -1; dx < 2; dx++) {
                            for (int dy = -1; dy < 2; dy++) {
                                if (grille.estDansLaGrille(x + dx, y + dy)) {
                                    if (grille.getCases()[x + dx][y + dy].isMarked) {
                                        drapoautour++;
                                    }
                                }
                            }
                        }
                        // Cas où il y a autant de drapeaux autour que de mines voisines
                        if (drapoautour == grille.getCases()[x][y].getMinesVoisines()) {
                            for (int dx = -1; dx < 2; dx++) {
                                for (int dy = -1; dy < 2; dy++) {
                                    if (grille.estDansLaGrille(x + dx, y + dy)) {
                                        Case c = grille.getCases()[x + dx][y + dy];
                                        if (!c.isMarked && !c.isClicked) {
                                            if (Objects.equals(grille.click(x + dx, y + dy), "boom")) {
                                                perdu = true;
                                            }
                                        }
                                    }
                                }
                            }
                            mineAdapter.notifyDataSetChanged();
                        }
                    }
                    // Si on découvre une case avec une bombe derrière, défaite
                    if (perdu) {
                        chrono.stop();
                        stopMusic();
                        revealbombs();
                        lamusic = R.raw.defeat;
                        mediaPlayer = MediaPlayer.create(this, lamusic);
                        mediaPlayer.setLooping(false);
                        mediaPlayer.start();
                        fingame = true;
                        showDialogDefaite();
                    }
                    // Si on a découvert toutes les cases découvrables, victoire
                    else if (grille.getNbCase() - grille.getNbMines() == grille.nbcasesdecouvertes) {
                        //Toast.makeText(this, "gg wp no re", Toast.LENGTH_SHORT).show();
                        chrono.stop();
                        stopMusic();
                        revealbombs();
                        lamusic = R.raw.victory;
                        mediaPlayer = MediaPlayer.create(this, lamusic);
                        mediaPlayer.setLooping(false);
                        mediaPlayer.start();
                        fingame = true;

                        String dif;
                        if (difficulte == 1) {
                            dif = "facile";
                        } else if (difficulte == 2) {
                            dif = "moyen";
                        } else {
                            dif = "difficile";
                        }
                        showDialogVictoire((SystemClock.elapsedRealtime() - chrono.getBase()) / 1000 + "", dif);
                    }
                    lastClickTime = currentTime;
                    return;
                }


                // Récupérer l'objet de la case cliquée
                caseSelect = (ImageView) view;
                caseSelect.setBackgroundColor(colorSelected);

            }

            // On lance le chrono à la première case découverte
            if (!debut) {
                chrono.setBase(SystemClock.elapsedRealtime());
                chrono.start();
                startMusic();
                debut=true;
                grille.click(x,y);
                mineAdapter.notifyDataSetChanged();
            }
        });

        btnFlag.setOnClickListener(v -> {
            // On ne peut mettre de drapeaux que sur des cases non découvertes
            if (grille.getCases()[x][y].isClicked) {
                return;
            }
            if (!fingame) {
                grille.getCases()[x][y].mark();
                if (grille.getCases()[x][y].isMarked){
                    nbdrapos++;
                }else{
                    nbdrapos--;
                }
                updateminesrestantes();
                mineAdapter.notifyDataSetChanged();
            }
            //cacherSamuel();
        });

        btnDiscover.setOnClickListener(v -> {
            // On ne peut découvrir que des cases non découvertes
            if (grille.getCases()[x][y].isClicked) {
                return;
            }
            if (!fingame) {
                // Si on découvre une case avec une bombe derrière, défaite
                if (Objects.equals(grille.click(x, y), "boom")) {
                    //Toast.makeText(this, "ah tu t'es trompé...", Toast.LENGTH_SHORT).show();
                    chrono.stop();
                    stopMusic();
                    revealbombs();
                    lamusic = R.raw.defeat;
                    mediaPlayer = MediaPlayer.create(this, lamusic);
                    mediaPlayer.setLooping(false);
                    mediaPlayer.start();
                    fingame = true;
                    showDialogDefaite();
                }
                // Si on a découvert toutes les cases découvrables, victoire
                else if (grille.getNbCase() - grille.getNbMines() == grille.nbcasesdecouvertes) {
                    //Toast.makeText(this, "gg wp no re", Toast.LENGTH_SHORT).show();
                    chrono.stop();
                    stopMusic();
                    revealbombs();
                    lamusic = R.raw.victory;
                    mediaPlayer = MediaPlayer.create(this, lamusic);
                    mediaPlayer.setLooping(false);
                    mediaPlayer.start();
                    fingame = true;

                    String dif;
                    if (difficulte==1){
                        dif = "facile";
                    }
                    else if (difficulte==2){
                        dif = "moyen";
                    }
                    else {
                        dif = "difficile";
                    }
                    showDialogVictoire((SystemClock.elapsedRealtime()-chrono.getBase())/1000 + "", dif);

                }
                mineAdapter.notifyDataSetChanged();
            }
            //cacherSamuel();
        });
    }



    private void updateminesrestantes(){
        int nbminesrestantes = Grille.nbMines - nbdrapos;
        minesrestantes.setText(String.valueOf(nbminesrestantes));
    }

    // On découvre toutes les bombes en fin de partie
    private void revealbombs() {
        for (int i = 0; i < grille.getLignes(); i++) {
            for (int j = 0; j < grille.getColonnes(); j++) {
                Case c = grille.getCases()[i][j];
                if (c.hasMine) {
                    c.click();
                }
            }
        }
    }

    /*private void revealSamuel() {
        btnDiscover.setVisibility(View.VISIBLE);
        btnFlag.setVisibility(View.VISIBLE);
    }
    private void cacherSamuel() {
        btnDiscover.setVisibility(View.GONE);
        btnFlag.setVisibility(View.GONE);
    }*/

    private void gamemusic(int difficulty){
        if (difficulty==1){
            lamusic=R.raw.easy_ouioui;
        } else if(difficulty==2){
            lamusic=R.raw.intermediate_mii;
        } else{
            lamusic=R.raw.difficult_doom;
        }
    }
    private void startMusic() {
        mediaPlayer = MediaPlayer.create(this, lamusic);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }
    private void stopMusic() {
        mediaPlayer.stop();
        mediaPlayer.release();
    }
    protected void onDestroy() {
        super.onDestroy();
        stopMusic();
    }
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void showDialogDefaite() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        dialogDefaite dialogFragment = new dialogDefaite();
        dialogFragment.show(fragmentManager, "MyDialogFragment");
    }
    private void showDialogVictoire(String chrono, String dif) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        dialogVictoire dialogFragment = dialogVictoire.newInstance(chrono,dif);
        dialogFragment.show(fragmentManager, "MyDialogFragment");
    }
}