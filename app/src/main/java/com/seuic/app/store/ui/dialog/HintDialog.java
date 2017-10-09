package com.seuic.app.store.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
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
    private TextView hintTitle, hintContent;

    public HintDialog(Context context) {
        this(context, R.style.AlertDialogTheme);
        this.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
    }

    public HintDialog(Context context, int themeResId) {
        super(context, themeResId);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_hint, null, false);
        this.setContentView(dialogView);
        this.setCanceledOnTouchOutside(false);
        celBtn = (TextView) dialogView.findViewById(R.id.dialog_button1);
        okBtn = (TextView) dialogView.findViewById(R.id.dialog_button2);
        hintTitle = (TextView) dialogView.findViewById(R.id.hint_title);
        hintContent = (TextView) dialogView.findViewById(R.id.hint_content);
    }

    public void setCancelClickListener(View.OnClickListener listener) {
        celBtn.setOnClickListener(listener);
    }

    public void setOkClickListener(View.OnClickListener listener) {
        okBtn.setOnClickListener(listener);
    }

    public void setHintTitle(String title) {
        hintTitle.setText(title);
    }

    public void setHintContent(String content) {
        hintContent.setText(content);
    }
}
