package com.erif.library;

import static java.lang.Math.max;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import java.util.Locale;

public class TextViewSearchable extends AppCompatTextView {

    private String query = null;
    private int highlightColor;

    private static final int HIGHLIGHT_NORMAL = 0;
    private static final int HIGHLIGHT_BOLD = 1;
    private static final int HIGHLIGHT_ITALIC = 2;

    private int highlightTextStyle = HIGHLIGHT_NORMAL;

    public TextViewSearchable(@NonNull Context context) {
        super(context);
        init(context, null, 0);
    }

    public TextViewSearchable(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public TextViewSearchable(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        Resources.Theme theme = context.getTheme();
        if (theme != null) {
            TypedArray typedArray = theme.obtainStyledAttributes(
                    attrs, R.styleable.TextViewSearchable, defStyleAttr, 0
            );
            try {
                highlightColor = typedArray.getColor(R.styleable.TextViewSearchable_android_searchResultHighlightColor, Color.BLACK);
            } finally {
                typedArray.recycle();
            }
        }
    }

    public void setQuery(@Nullable String query) {
        this.query = query;
    }

    private void applySpan() {
        String mText = getText().toString();
        if (query != null && !TextUtils.isEmpty(mText)) {
            String lowerCaseQuery = query.toLowerCase(Locale.getDefault());
            String lowerCaseValue = mText.toLowerCase(Locale.getDefault());

            int length = lowerCaseQuery.length();
            int startIndex = lowerCaseValue.indexOf(lowerCaseQuery);
            int endIndex = startIndex + length;

            if (startIndex > -1) {
                SpannableStringBuilder ssBuilder = new SpannableStringBuilder(mText);
                ForegroundColorSpan color = new ForegroundColorSpan(getCurrentTextColor());
                StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);

                // Apply the bold text style span
                ssBuilder.setSpan(
                        boldSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                // Apply the color text style span
                ssBuilder.setSpan(
                        color, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );
                setText(ssBuilder);
            }
        }
    }

    public String getQuery() {
        return query;
    }

}
