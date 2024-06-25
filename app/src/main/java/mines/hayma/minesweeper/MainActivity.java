package mines.hayma.minesweeper;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Grille grille;
    private GridView gridView;
    private MineAdapter mineAdapter;
    private Case selectedCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialisation de la grille; faire varier gridSize et nbMines à l'avenir
        int gridSize = 10;
        int nbMines = 15;
        grille = new Grille(gridSize, gridSize, nbMines);

        // La gridView en elle-même
        gridView = findViewById(R.id.gridView);
        gridView.setNumColumns(gridSize);
        // L'adapteur qui actualise tous les objets de la gridView
        mineAdapter = new MineAdapter(this, grille);
        gridView.setAdapter(mineAdapter);

        // Lorsque l'utilisateur clique sur un élément qui est repéré par sa view, sa position, son id...
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            int x = position % gridSize;
            int y = position / gridSize;
            grille.click(x, y);
            mineAdapter.setSelectedPosition(position);
            //updateGrid();
            // Actualiser la position pour l'adaptateur
        });

        Button btnFlag = findViewById(R.id.btnFlag);
        btnFlag.setOnClickListener(v -> {
        });

        Button btnDiscover = findViewById(R.id.btnDiscover);
        btnDiscover.setOnClickListener(v -> {
        });
    }

    /*public void updateGrid() {
        mineAdapter.notifyDataSetChanged();
    }*/
}
