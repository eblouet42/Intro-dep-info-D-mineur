package mines.hayma.minesweeper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
    int colorSelected=Color.parseColor("#80FFFFFF");
    int colorNormal=Color.TRANSPARENT;

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

        // Lorsque l'utilisateur clique sur un élément qui est repéré par sa view, sa position, son id...
        gridView.setOnItemClickListener((parent, view, position, id) -> {

            // Récupérer l'objet de la case cliquée
            if (caseSelect!=null) {
                caseSelect.setBackgroundColor(colorNormal);
            }
            caseSelect=(ImageView) view;
            caseSelect.setBackgroundColor(colorSelected);
            x = position/grille.getColonnes();
            y = position%grille.getColonnes();

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

        ImageButton btnFlag=findViewById(R.id.btnFlag);
        btnFlag.setOnClickListener(v -> {
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
        });

        ImageButton btnDiscover=findViewById(R.id.btnDiscover);
        btnDiscover.setOnClickListener(v -> {
            if (!fingame){
                grille.click(x, y);
                // Après chaque découverte, on vérifie si le jeu est terminé ou non
                if (ezwin()) {
                    Toast.makeText(this, "gg wp no re", Toast.LENGTH_SHORT).show();
                    chrono.stop();
                    stopMusic();
                    revealbombs();
                    lamusic=R.raw.victory;
                    mediaPlayer = MediaPlayer.create(this, lamusic);
                    mediaPlayer.setLooping(false);
                    mediaPlayer.start();
                    fingame=true;
                } else if (isnoob()) {
                    Toast.makeText(this, "ah tu t'es trompé...", Toast.LENGTH_SHORT).show();
                    chrono.stop();
                    stopMusic();
                    revealbombs();
                    lamusic=R.raw.defeat;
                    mediaPlayer = MediaPlayer.create(this, lamusic);
                    mediaPlayer.setLooping(false);
                    mediaPlayer.start();
                    fingame=true;
                }
                mineAdapter.notifyDataSetChanged();
            }
        });
    }
    private void updateminesrestantes(){
        int nbminesrestantes = Grille.nbMines - nbdrapos;
        minesrestantes.setText(String.valueOf(nbminesrestantes));
    }
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
    // Le jeu est gagné lorsque toutes les cases sans mines sont découvertes
    private boolean ezwin() {
        for (int i=0;i<grille.getLignes();i++) {
            for (int j=0;j<grille.getColonnes();j++) {
                Case c=grille.getCases()[i][j];
                if (!c.isClicked && !c.hasMine) {
                    return false;
                }
            }
        }
        return true;
    }

    // Le jeu est perdu quand une case est découverte et a une mine
    private boolean isnoob() {
        for (int i=0;i<grille.getLignes();i++) {
            for (int j=0;j<grille.getColonnes();j++) {
                Case c=grille.getCases()[i][j];
                if (c.isClicked && c.hasMine) {
                    return true;
                }
            }
        }
        return false;
    }
    // On découvre toutes les bombes en fin de partie
    private void revealbombs(){
        for (int i=0;i<grille.getLignes();i++){
            for (int j=0;j<grille.getColonnes();j++){
                Case c=grille.getCases()[i][j];
                if (c.hasMine){
                    c.click();
                }
            }
        }
    }
}