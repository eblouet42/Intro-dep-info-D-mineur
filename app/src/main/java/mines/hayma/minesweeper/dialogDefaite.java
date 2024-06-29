package mines.hayma.minesweeper;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class dialogDefaite extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_defaite, null);

        builder.setView(view)
               .setTitle("Perdu !");



        view.findViewById(R.id.buttonRetry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent page = new Intent(getContext(), StartActivity.class);
                startActivity(page);
            }
        });


        return builder.create();
    }
}
