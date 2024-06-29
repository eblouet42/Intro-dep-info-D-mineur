package mines.hayma.minesweeper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void startFacile(View view) {
        Intent pageJeu = new Intent(StartActivity.this, MainActivity.class);
        pageJeu.putExtra("hauteur", 8);
        pageJeu.putExtra("longueur", 10);
        pageJeu.putExtra("mines", 10);

        startActivity(pageJeu);
        finish();
    }

    public void startMoyen(View view) {
        Intent pageJeu = new Intent(StartActivity.this, MainActivity.class);
        pageJeu.putExtra("hauteur", 18);
        pageJeu.putExtra("longueur", 14);
        pageJeu.putExtra("mines", 40);

        startActivity(pageJeu);
        finish();
    }
    public void startDifficile(View view) {
        Intent pageJeu = new Intent(StartActivity.this, MainActivity.class);
        pageJeu.putExtra("hauteur", 24);
        pageJeu.putExtra("longueur", 20);
        pageJeu.putExtra("mines", 99);

        startActivity(pageJeu);
        //finish();
    }
}