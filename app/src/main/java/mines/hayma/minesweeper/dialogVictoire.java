package mines.hayma.minesweeper;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class dialogVictoire extends DialogFragment {

    private static final String CHRONO = "chrono";
    private static String DIFFICULTY = "dif";


    public static dialogVictoire newInstance(String chrono, String dif) {
        dialogVictoire fragment = new dialogVictoire();
        Bundle args = new Bundle();
        args.putString(CHRONO, chrono);
        args.putString(DIFFICULTY, dif);
        fragment.setArguments(args);
        return fragment;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_victoire, null);

        builder.setView(view)
                .setTitle("Félicitations !");

        TextView text = view.findViewById(R.id.textVictoire);
        String chrono = getArguments().getString(CHRONO);
        String dif = getArguments().getString(DIFFICULTY);
        text.setText("Votre temps est de " + chrono + " secondes.\nVoulez vous le partager ?");




        view.findViewById(R.id.buttonRetry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent page = new Intent(getContext(), StartActivity.class);
                startActivity(page);
            }
        });

        view.findViewById(R.id.buttonShare).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("QueryPermissionsNeeded")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "J'ai fait un score de dingus au démineur en terminant une grille "+dif+" en seulement "+ chrono+"s ! J'ai pas raison la mif ?");

                // Vérifiez qu'il y a une activité pour gérer l'Intent
                if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivity(Intent.createChooser(intent, "Partager avec"));
                }
            }
        });


        return builder.create();
    }
}
