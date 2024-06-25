package mines.hayma.minesweeper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class MineAdapter extends BaseAdapter {
    private Context context;
    private Grille grille;

    public MineAdapter(Context context, Grille grille) {
        this.context = context;
        this.grille = grille;
    }

    @Override
    public int getCount() {
        return grille.nbCase;
    }

    @Override
    public Object getItem(int position) {
        return grille.cases.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        Case c = grille.cases.get(position);
        if (c.isClicked) {
            if (c.hasMine) {
                imageView.setImageResource(R.drawable.mine);
            } else {
                int resId = context.getResources().getIdentifier("number" + c.minesVoisines, "drawable", context.getPackageName());
                imageView.setImageResource(resId);
            }
        } else if (c.isMarked) {
            imageView.setImageResource(R.drawable.flag);
        } else {
            imageView.setImageResource(R.drawable.covered);
        }

        return imageView;
    }
}