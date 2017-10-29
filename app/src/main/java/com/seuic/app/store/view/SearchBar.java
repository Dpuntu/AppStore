package com.seuic.app.store.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.seuic.app.store.utils.AndroidUtils;

import java.util.regex.Pattern;

/**
 * Created on 2017/9/16.
 *
 * @author dpuntu
 *         <p>
 *         搜索框
 */

public class SearchBar extends EditText implements
        View.OnFocusChangeListener, TextWatcher, TextView.OnEditorActionListener {
    private static Pattern mPattern = Pattern.compile("\\s+");
    private boolean isLeft = false;
    private boolean pressSearch = false;
    private boolean isNull = true;
    public static final int SEARCH_SUCCESS = 100;
    public static final int SEARCH_ERROR = 101;
    private SearchBarTextWatcher searchBarTextWatcher;
    private OnSearchClickListener listener;

    public SearchBar(Context context) {
        this(context, null);
    }

    public SearchBar(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public SearchBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnFocusChangeListener(this);
        setOnEditorActionListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables();
        int size = drawables.length;
        Drawable drawableLeft = drawables[0];
        if (isLeft) {
            super.onDraw(canvas);
        } else {
            if (size > 0) {
                if (drawableLeft != null) {
                    drawables[0].setCallback(null);
                    float textWidth = getPaint().measureText(getHint().toString());
                    int drawablePadding = getCompoundDrawablePadding();
                    int drawableWidth = drawableLeft.getIntrinsicWidth();
                    float bodyWidth = textWidth + drawableWidth + drawablePadding;
                    canvas.translate((getWidth() - bodyWidth - getPaddingLeft() - getPaddingRight()) / 2, 0);
                }
            }
            super.onDraw(canvas);
        }
        drawableLeft.setBounds(0, 0, AndroidUtils.dip2px(20), AndroidUtils.dip2px(20));
        setCompoundDrawables(drawableLeft, null, null, null);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        // 恢复EditText默认的样式
        if (!pressSearch && TextUtils.isEmpty(getText().toString())) {
            isLeft = hasFocus;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        isNull = (text == null
                || lengthAfter <= 0
                || text.toString().isEmpty()
                || mPattern.matcher(text.toString()).matches());
        if (searchBarTextWatcher != null) {
            searchBarTextWatcher.searchBarTextChange(text.toString());
        }
    }

    @Override
    public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
        pressSearch = (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
        // 禁止输入回车换行
        if (isNull) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN:
                    break;
                case KeyEvent.ACTION_UP:
                    setText("");
                    if (listener != null) {
                        listener.onSearchClick(view, "未输入任何有效字符", SEARCH_ERROR);
                    }
                    break;
                default:
                    break;
            }
            return isNull;
        } else if (pressSearch && listener != null) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN:
                    break;
                case KeyEvent.ACTION_UP:
                    InputMethodManager imm = (InputMethodManager) view.getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                    }
                    if (listener != null) {
                        listener.onSearchClick(view, getText(), SEARCH_SUCCESS);
                    }
                    break;
                default:
                    break;
            }
            return pressSearch;
        }
        return false;
    }

    public interface OnSearchClickListener {
        void onSearchClick(View view, CharSequence text, int errorCode);
    }

    public void setOnSearchClickListener(OnSearchClickListener listener) {
        this.listener = listener;
    }

    public interface SearchBarTextWatcher {
        void searchBarTextChange(String searchBarText);
    }

    public void addSearchBarTextWatcher(SearchBarTextWatcher searchBarTextWatcher) {
        this.searchBarTextWatcher = searchBarTextWatcher;
    }

}
