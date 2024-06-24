package mines.hayma.minesweeper;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class MineAdapter extends BaseAdapter {
    private Context context;
    private Grille grille;
    private int selectedPosition = -1;
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
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        Case c = grille.cases.get(position);
        if (c.isClicked) {
            if (c.hasMine) {
                imageView.setImageResource(R.drawable.bomb);
            } else {
                int resId = context.getResources().getIdentifier("number" + c.getMinesVoisines(), "drawable", context.getPackageName());
                imageView.setImageResource(resId);
            }
        } else if (c.isMarked) {
            imageView.setImageResource(R.drawable.flag);
        } else {
            imageView.setImageResource(R.drawable.untouched);
        }
        int colorSelected = Color.parseColor("#80FFFFFF");
        int colorNormal = Color.TRANSPARENT;
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[] { android.R.attr.state_pressed }, context.getDrawable(R.drawable.cell_highlight));
        stateListDrawable.addState(new int[] {}, context.getDrawable(R.drawable.cell_background));
        imageView.setBackground(stateListDrawable);

        if (position == selectedPosition) {
            imageView.setBackgroundColor(colorSelected);
        } else {
            imageView.setBackgroundColor(colorNormal);
        }

        return imageView;
    }
}