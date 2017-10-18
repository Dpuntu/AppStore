package com.seuic.app.store.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.seuic.app.store.R;

/**
 * Created on 2017/10/16.
 *
 * @author dpuntu
 */

public class AppStoreToast {
    private Toast mToast;

    private AppStoreToast(Context context, CharSequence text, int duration) {
        View view = LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
        TextView textView = (TextView) view.findViewById(R.id.toast_text);
        textView.setText(text);
        mToast = new Toast(context);
        mToast.setDuration(duration);
        mToast.setView(view);
    }

    public static AppStoreToast makeText(Context context, CharSequence text, int duration) {
        return new AppStoreToast(context, text, duration);
    }

    public void show() {
        if (mToast != null) {
            mToast.show();
        }
    }

    public void setGravity(int gravity, int xOffset, int yOffset) {
        if (mToast != null) {
            mToast.setGravity(gravity, xOffset, yOffset);
        }
    }
}
