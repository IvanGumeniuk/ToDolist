package com.example.student_3.todolist.decorators;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by gromi on 11/16/2017.
 */

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int spacingHorizontal;
    private int spacingVertical;
    private Integer edgeSpacing;

    public GridSpacingItemDecoration(int spanCount, int spacingHorizontal, int spacingVertical) {
        this.spanCount = spanCount;
        this.spacingHorizontal = spacingHorizontal;
        this.spacingVertical = spacingVertical;
    }

    public GridSpacingItemDecoration(int spanCount, int spacingHorizontal, int spacingVertical, int edgeSpacing){
        this(spanCount, spacingHorizontal, spacingVertical);
        this.edgeSpacing = edgeSpacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int column = position % spanCount;
        outRect.right = spacingHorizontal;
        outRect.bottom = spacingVertical;
        if (edgeSpacing != null) {
            if(column == spanCount - 1){
                outRect.right = edgeSpacing;
            } else if (column == 0){
                outRect.left = edgeSpacing;
            }
        }

    }
}
