package mines.hayma.minesweeper;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class MineAdapter extends BaseAdapter {

    // Trucs de la doc
    private Context context;
    private Grille grille;
    private int selectedPosition = -1;
    public MineAdapter(Context context, Grille grille)
        {this.context = context;
        this.grille = grille;}
    @Override
    public int getCount()
        {return grille.getNbCase();}
    //Définir une méthode sur les grilles pour avoir le nb de cases
    @Override
    public Object getItem(int position){
        int x = position % grille.getLignes();
        int y = position / grille.getColonnes();
        return grille.getCases()[x][y];
    }
    //Définir une méthode sur les cases pour obtenir l'item du gridview à partir de la position x,y
    @Override
    public long getItemId(int position)
        {return position;}
    // jsp à quoi ça sert, à rien pr l'instant mais la doc le définit quand même

    @Override
    //Explanation
    //int position : la position de l'élément dans la liste ou la grille.
    //View convertView : la vue réutilisable pour l'élément. Elle est null si aucune vue n'est disponible pour la réutilisation.
    //ViewGroup parent : le parent qui contiendra la vue retournée (la ListView ou GridView).

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null)
            {imageView = new ImageView(context);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);}

            //Si convertView est null, cela signifie qu'il n'y a pas de vue réutilisable disponible, donc une nouvelle vue doit être créée.
            //imageView = new ImageView(context); : crée une nouvelle instance de ImageView en utilisant le contexte de l'application ou de l'activité.
            //imageView.setLayoutParams(new ViewGroup.LayoutParams(85, 85)); : définit les paramètres de mise en page pour l'image, ici une taille fixe de 85x85 pixels.
            //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP); : définit le type de mise à l'échelle de l'image. CENTER_CROP ajuste l'image pour qu'elle remplisse l'espace de l'ImageView, tout en conservant le ratio d'aspect de l'image.
            //imageView.setPadding(8, 8, 8, 8); : définit une marge intérieure (padding) de 8 pixels sur tous les côtés de l'image.

        else
            {imageView = (ImageView) convertView;}

            //Si convertView n'est pas null, cela signifie qu'une vue réutilisable est disponible.
            //imageView = (ImageView) convertView; : la vue réutilisable (qui est supposée être un ImageView) est castée et assignée à la variable imageView.

        Case c = grille.getCases().get(position);
        // On récupère l'item associé à la position

        if (c.isClicked) {
            if (c.hasMine){
                imageView.setImageResource(R.drawable.bomb);
            }
                // On set une image de bombe si on clique sur une bombe
            else {
                int resId = context.getResources().getIdentifier("number" + c.getMinesVoisines(), "drawable", context.getPackageName());
                imageView.setImageResource(resId);
            }
        }
                // On set l'image du nombre qui correspond si on ne clique pas sur une bombe
                // Il faut une méthode sur les items pour obtenir le nombre de mines voisines
        else if (c.isMarked)
            {imageView.setImageResource(R.drawable.flag);}
            // On set une image de drapeau
        else
            {imageView.setImageResource(R.drawable.untouched);}
            // On set une image de case recouverte

        int colorSelected = Color.parseColor("#80FFFFFF");
        int colorNormal = Color.TRANSPARENT;
        // Deux couleurs: blanc(pour la selectionnée) et transparant
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[] { android.R.attr.state_pressed }, context.getDrawable(R.drawable.cell_highlight));
        stateListDrawable.addState(new int[] {}, context.getDrawable(R.drawable.cell_background));
        imageView.setBackground(stateListDrawable);
        if (position == selectedPosition)
            {imageView.setBackgroundColor(colorSelected);}
        else
            {imageView.setBackgroundColor(colorNormal);}
        // On set le background selon si elle a été sélectionnée ou non.
        // Par défaut, prend le style avec cell_background et en blanc
        // Si pressé, prend le style avec cell_highlight et en transparent
        return imageView;
    }
}