package com.erif.library;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import java.util.Locale;

public class TextViewQuery extends AppCompatTextView {

    private String query = null;
    private int highlightColor;
    private int highlightBackgroundColor;

    private static final int HIGHLIGHT_NORMAL = 0;
    private static final int HIGHLIGHT_BOLD = 1;
    private static final int HIGHLIGHT_ITALIC = 2;
    private static final int HIGHLIGHT_BOLD_ITALIC = 3;

    private int highlightTextStyle = HIGHLIGHT_BOLD;

    private boolean highlightUnderline  = false;

    private boolean ignoreCase = true;

    private CharSequence text = null;

    public TextViewQuery(@NonNull Context context) {
        super(context);
        init(context, null, 0);
    }

    public TextViewQuery(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public TextViewQuery(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        Resources.Theme theme = context.getTheme();
        if (theme != null) {
            TypedArray typedArray = theme.obtainStyledAttributes(
                    attrs, R.styleable.TextViewQuery, defStyleAttr, 0
            );
            try {
                highlightColor = typedArray.getColor(R.styleable.TextViewQuery_highlightColor, getCurrentTextColor());
                highlightBackgroundColor = typedArray.getColor(R.styleable.TextViewQuery_highlightBackgroundColor, 0);
                highlightTextStyle = typedArray.getInt(R.styleable.TextViewQuery_highlightTextStyle, HIGHLIGHT_BOLD);
                highlightUnderline = typedArray.getBoolean(R.styleable.TextViewQuery_highlightUnderline, false);
                ignoreCase = typedArray.getBoolean(R.styleable.TextViewQuery_ignoreCase, true);
                text = typedArray.getString(R.styleable.TextViewQuery_android_text);
                query = typedArray.getString(R.styleable.TextViewQuery_query);
                if (notEmpty(currentText()) && notEmpty(query)) {
                    SpannableStringBuilder spanBuilder = createSpan();
                    setText(spanBuilder);
                }
            } finally {
                typedArray.recycle();
            }
        }
    }

    public void setQuery(@Nullable String query) {
        this.query = query;
        if (notEmpty(currentText())) {
            SpannableStringBuilder spanBuilder = createSpan();
            if (spanBuilder != null)
                setText(spanBuilder, BufferType.NORMAL);
            else
                setText(text);
        }
        // When Use View Model, Query Running First
        // When Manual, Query Running Last
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        this.text = text;
        if (notEmpty(query)) {
            SpannableStringBuilder spanBuilder = createSpan();
            super.setText(spanBuilder, BufferType.SPANNABLE);
        } else  {
            super.setText(text, type);
        }
    }

    private SpannableStringBuilder createSpan() {
        SpannableStringBuilder spanBuilder = null;
        if (notEmpty(currentText()) && notEmpty(query)) {
            String finalQuery = ignoreCase ? query.toLowerCase(Locale.getDefault()) : query;
            String finalText = ignoreCase ? currentText().toLowerCase(Locale.getDefault()) : currentText();

            int length = finalQuery.length();
            int startIndex = finalText.indexOf(finalQuery);
            int endIndex = startIndex + length;

            spanBuilder = new SpannableStringBuilder(currentText());
            if (startIndex >= 0) {

                // Bold Span
                if (highlightTextStyle == HIGHLIGHT_BOLD_ITALIC) {
                    StyleSpan boldItalic = new StyleSpan(Typeface.BOLD_ITALIC);
                    spanBuilder.setSpan(
                            boldItalic, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    );
                } else if (highlightTextStyle == HIGHLIGHT_BOLD) {
                    StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
                    spanBuilder.setSpan(
                            boldSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    );
                } else if (highlightTextStyle == HIGHLIGHT_ITALIC) {
                    StyleSpan italicSpan = new StyleSpan(Typeface.ITALIC);
                    spanBuilder.setSpan(
                            italicSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    );
                }

                // Underline Span
                if (highlightUnderline) {
                    UnderlineSpan underline = new UnderlineSpan();
                    spanBuilder.setSpan(
                            underline, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    );
                }

                // Color Span
                ForegroundColorSpan color = new ForegroundColorSpan(highlightColor);
                spanBuilder.setSpan(
                        color, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                // Background Color Span
                if (highlightBackgroundColor != 0) {
                    BackgroundColorSpan bgColor = new BackgroundColorSpan(highlightBackgroundColor);
                    spanBuilder.setSpan(
                            bgColor, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    );
                }
            }
        }
        return spanBuilder;
    }

    private boolean notEmpty(@Nullable String source) {
        if (source == null)
            return false;
        return !source.isEmpty() || !source.isBlank();
    }

    public String getQuery() {
        return query;
    }

    private String currentText() {
        return text == null ? "" : text.toString();
    }

}
