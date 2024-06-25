package mines.hayma.minesweeper;

import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Grille grille;
    private MineAdapter mineAdapter;
    private int selectedPosition=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialisation de la grille; faire varier gridSize et nbMines à l'avenir
        int gridSize = 10;
        int nbMines = 15;
        grille = new Grille(gridSize, gridSize, nbMines);

        // La gridView en elle-même
        GridView gridView = findViewById(R.id.gridView);
        gridView.setNumColumns(gridSize);
        // L'adapteur qui actualise tous les objets de la gridView
        mineAdapter = new MineAdapter(this, grille);
        gridView.setAdapter(mineAdapter);

        // Lorsque l'utilisateur clique sur un élément qui est repéré par sa view, sa position, son id...
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            selectedPosition=position;
            mineAdapter.setSelectedPosition(position);
            // Actualiser la position pour l'adaptateur
        });

        Button btnFlag = findViewById(R.id.btnFlag);
        btnFlag.setOnClickListener(v -> {
            if (selectedPosition != -1) {
                int x=selectedPosition%gridSize;
                int y=selectedPosition/gridSize;
                grille.drapo(x,y);
                updateGrid();
            }
        });

        Button btnDiscover = findViewById(R.id.btnDiscoqueen);
        btnDiscover.setOnClickListener(v -> {
            if (selectedPosition != -1) {
                int x = selectedPosition % gridSize;
                int y = selectedPosition / gridSize;
                grille.click(x, y);
                updateGrid();
            }
        });
    }

    public void updateGrid() {
        mineAdapter.notifyDataSetChanged();
    }
}
