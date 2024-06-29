package mines.hayma.minesweeper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class MineAdapter extends BaseAdapter {

    // Trucs de la doc
    private final Context context;
    private final Grille grille;

    public MineAdapter(Context context, Grille grille)
        {this.context = context;
        this.grille = grille;}
    @Override
    public int getCount()
        {return grille.getNbCase();}
    //Définir une méthode sur les grilles pour avoir le nb de cases
    @Override
    public Object getItem(int position){
        int x = position/grille.getColonnes();
        int y = position%grille.getColonnes();
        return grille.getCases()[x][y];
    }
    //Définir une méthode sur les cases pour obtenir l'item du gridview à partir de la position x,y
    @Override
    public long getItemId(int position)
        {return position;}
    // jsp à quoi ça sert, à rien pr l'instant mais la doc le définit quand même

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        // Sans la disjonction proposé par la doc sur le covertview, ça marche...
        imageView = new ImageView(context);
        int size = parent.getWidth() / grille.getColonnes()-8;
        imageView.setLayoutParams(new ViewGroup.LayoutParams(size, size));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(4,4,4,4);

        int colorSelected = Color.parseColor("#000000");
        imageView.setImageResource(R.drawable.untouched);
        if (position==-1){
            imageView.setBackgroundColor(colorSelected);
        }

        Case c = (Case) getItem(position);
        // On récupère l'item associé à la position

        if (c.isClicked) {
            if (c.hasMine){
                imageView.setImageResource(R.drawable.bomb);
            }
                // On set une image de bombe si on clique sur une bombe
            else {
                @SuppressLint("DiscouragedApi") int resId = context.getResources().getIdentifier("number" + c.getMinesVoisines(), "drawable", context.getPackageName());
                imageView.setImageResource(resId);
            }
        }
        if (c.isMarked) {
            imageView.setImageResource(R.drawable.flag);
        }
        return imageView;
    }

}