package com.seuic.app.store.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;


/**
 * Created on 2017/9/23.
 *
 * @author dpuntu
 *         <p>
 *         可滚动的TextView
 */

public class ScrollHorizontallyTextView extends TextView {
    public ScrollHorizontallyTextView(Context con) {
        super(con);
    }

    public ScrollHorizontallyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollHorizontallyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isFocused() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(true, direction, previouslyFocusedRect);
    }

}
