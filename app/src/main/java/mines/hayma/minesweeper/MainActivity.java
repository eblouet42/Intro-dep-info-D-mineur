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

        int gridSize = 8;
        int nbMines = 10;
        grille = new Grille(gridSize, nbMines);

        gridView = findViewById(R.id.gridView);
        gridView.setNumColumns(gridSize);
        mineAdapter = new MineAdapter(this, grille);
        gridView.setAdapter(mineAdapter);

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            int x = position % gridSize;
            int y = position / gridSize;

            grille.click(x, y);
            mineAdapter.setSelectedPosition(position);
        });

        Button btnFlag = findViewById(R.id.btnFlag);
        btnFlag.setOnClickListener(v -> {
            // Gérer l'action Flag
        });

        Button btnDiscover = findViewById(R.id.btnDiscover);
        btnDiscover.setOnClickListener(v -> {
            // Gérer l'action Discover
        });
    }

    public void updateGrid() {
        mineAdapter.notifyDataSetChanged();
    }

    public void gameOver() {
        Toast.makeText(this, "Game Over", Toast.LENGTH_SHORT).show();
        // Révéler toutes les mines
        for (Case c : grille.cases) {
            if (c.hasMine) {
                c.isClicked = true;
            }
        }
        updateGrid();
    }
}