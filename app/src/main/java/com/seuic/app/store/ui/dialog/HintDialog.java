package com.seuic.app.store.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.seuic.app.store.R;


/**
 * Created on 2017/8/2.
 *
 * @author dpuntu
 *         <p>
 *         提示框
 */

public class HintDialog extends Dialog {
    private TextView celBtn, okBtn;
    private FrameLayout content;
    private TextView hintTitle, hintContent;

    public HintDialog(Context context, ButtonStyle style) {
        this(context, R.style.AlertDialogTheme, style);
        this.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
    }

    public HintDialog(Context context, int themeResId, ButtonStyle style) {
        super(context, themeResId);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_hint, null, false);
        View btnView;
        setContentView(dialogView);
        setCanceledOnTouchOutside(false);
        content = (FrameLayout) dialogView.findViewById(R.id.dialog_content);
        switch (style) {
            case ONE:
                btnView = getLayoutInflater().inflate(R.layout.dialog_one_btn, null, false);
                okBtn = (TextView) btnView.findViewById(R.id.dialog_button1);
                content.removeAllViews();
                content.addView(btnView);
                break;
            case TWO:
            default:
                btnView = getLayoutInflater().inflate(R.layout.dialog_two_btn, null, false);
                celBtn = (TextView) btnView.findViewById(R.id.dialog_button1);
                okBtn = (TextView) btnView.findViewById(R.id.dialog_button2);
                content.removeAllViews();
                content.addView(btnView);
                break;
        }
        hintTitle = (TextView) dialogView.findViewById(R.id.hint_title);
        hintContent = (TextView) dialogView.findViewById(R.id.hint_content);
    }

    public void setCancelClickListener(View.OnClickListener listener) {
        if (listener == null) {
            throw new NullPointerException("hintDialog celBtn listener is NULL");
        }
        celBtn.setOnClickListener(listener);
    }

    public void setOkClickListener(View.OnClickListener listener) {
        if (listener == null) {
            throw new NullPointerException("hintDialog okBtn listener is NULL");
        }
        okBtn.setOnClickListener(listener);
    }

    public void setHintTitle(String title) {
        hintTitle.setText(title);
    }

    public void setHintContent(String content) {
        hintContent.setText(content);
    }

    enum ButtonStyle {
        ONE,
        TWO
    }
}
