package mines.hayma.minesweeper;

import android.content.Context;

public class Case {
    public Boolean isMarked;
    public Boolean isClicked;
    public Boolean isDiscoqueen;
    public Boolean hasMine;
    int minesVoisines;

    public Case() {
        hasMine = false;
        isClicked = false;
        isMarked = false;
        isDiscoqueen = false;
        minesVoisines = 0;
    }

    public void setMine(Boolean bool) {
        hasMine = bool;
    }

    public void click(){
        isClicked = true;
    }

    public void reversedrapo(){
        isMarked=!isMarked;
    }

    public void setMinesVoisines(int n){
        minesVoisines = n;
    }
    public int getMinesVoisines() {
        return minesVoisines;
    }
    public int getImageResource(Context context) {
        if (isDiscoqueen) {
            if (hasMine) {
                return R.drawable.bomb;
            } else {
                return context.getResources().getIdentifier("number" + getMinesVoisines(), "drawable", context.getPackageName());
            }
        } else if (isMarked) {
            return R.drawable.flag;
        } else {
            return R.drawable.untouched;
        }
    }
}