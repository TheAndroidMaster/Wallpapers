package com.james.wallpapers;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class SquareImageView extends ImageView {
    final public static int AUTO = -1, HORIZONTAL = 0, VERTICAL = 1;
    private int orientation = -1;

    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        int size = 0;
        switch(orientation) {
            case AUTO:
                size = Math.max(width, height);
                break;
            case HORIZONTAL:
                size = height;
                break;
            case VERTICAL:
                size = width;
                break;
        }
        setMeasuredDimension(size, size);
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
        invalidate();
    }

    public int getOrientation() {
        return orientation;
    }
}
