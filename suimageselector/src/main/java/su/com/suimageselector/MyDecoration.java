package su.com.suimageselector;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class MyDecoration extends RecyclerView.ItemDecoration {

    public enum Decoration{
        Linear,Grid
    }
    Decoration decoration;
    int space;
    int span;

    public MyDecoration(Decoration decoration,int span,int space) {
        this.decoration=decoration;
        this.span=span;
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if(decoration==Decoration.Grid){
            if(parent.getChildAdapterPosition(view)%span==span-1){
                outRect.set(space,space,space,0);
            }else {
                outRect.set(space, space, 0, 0);
            }
        }else if(decoration==Decoration.Linear){
            outRect.set(space,space,space,space);
        }
    }
}
