package mines.hayma.minesweeper;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Grille grille;
    private GridView gridView;
    private MineAdapter mineAdapter;
    private Case selectedCase;
    ImageView caseSelect;
    int x;
    int y;
    int colorSelected = Color.parseColor("#80FFFFFF");
    int colorNormal = Color.TRANSPARENT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialisation de la grille; faire varier gridSize et nbMines à l'avenir
        int gridSize = 15;
        int nbMines = 40;
        grille = new Grille(gridSize, gridSize, nbMines);


        // La gridView en elle-même
        gridView = findViewById(R.id.gridView);
        gridView.setNumColumns(gridSize);
        // L'adapteur qui actualise tous les objets de la gridView
        mineAdapter = new MineAdapter(this, grille);
        gridView.setAdapter(mineAdapter);

        // Lorsque l'utilisateur clique sur un élément qui est repéré par sa view, sa position, son id...
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Récupérer l'objet de la case cliquée
                if (caseSelect!=null) {
                    caseSelect.setBackgroundColor(colorNormal);
                }
                caseSelect = (ImageView) view;
                caseSelect.setBackgroundColor(colorSelected);
                x = position%grille.getLignes();
                y = position/grille.getColonnes();
            }
        });

        Button btnFlag = findViewById(R.id.btnFlag);
        btnFlag.setOnClickListener(v -> {
            grille.getCases()[x][y].mark();
            mineAdapter.notifyDataSetChanged();
        });

        Button btnDiscover = findViewById(R.id.btnDiscover);
        btnDiscover.setOnClickListener(v -> {
            grille.click(x,y);
            mineAdapter.notifyDataSetChanged();
        });
    }


}
