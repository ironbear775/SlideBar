package com.ironbear775.slidebar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * @author ironbear775
 */
public class SlideBar extends View {
    private String[] letters = {
            "热", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"
    };

    private OnLetterTouchListener listener;
    private int mTextSize = 40;
    private int mTextColor = R.color.colorPrimary;
    private int mTextTouchedColor = R.color.colorAccent;
    private int mTouchedBgColor = Color.LTGRAY;
    private boolean showBg = false;
    private int mCurrentLetterIndex = -1;
    private TextPaint paint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
    private TextView mLetterTv = null;
    private int mBackgroundDrawableResource;
    private Context mContext;
    private Drawable mTouchedBgDrawable;
    private boolean hasTouchedDrawable;
    private int barWidth;
    private int singleHeight;
    private Typeface mTypeface = Typeface.DEFAULT;

    public SlideBar(Context context) {
        super(context);
        mContext = context;

    }

    public SlideBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

    }

    public SlideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        init();
    }

    public void setTypeface(Typeface typeface) {
        this.mTypeface = typeface;
    }

    private void init() {
        int barHeight = getMeasuredHeight();
        barWidth = getMeasuredWidth();
        //计算每个索引的高度
        singleHeight = barHeight / letters.length;
    }

    public void setLetters(String[] letters) {
        this.letters = letters;
        invalidate();
    }

    public void setOnLetterTouchListener(OnLetterTouchListener letterTouchListener) {
        listener = letterTouchListener;
    }

    public void setTextView(TextView textView) {
        mLetterTv = textView;
    }

    public void setLetterTv(TextView tv) {
        mLetterTv = tv;
    }


    public void setTextColor(@ColorRes int colorRes) {
        mTextColor = colorRes;
    }

    public void setTextTouchedColor(@ColorRes int textTouchedColor) {
        this.mTextTouchedColor = textTouchedColor;
    }

    public void setTextSize(int textSize) {
        this.mTextSize = textSize;
    }

    public void setTouchedBackgroundColor(@ColorInt int bgColor) {
        this.mTouchedBgColor = bgColor;
    }

    public void setTouchedBackgroundDrawableResource(@DrawableRes int resId) {
        if (resId != 0 && resId == mBackgroundDrawableResource) {
            return;
        }

        if (resId != 0) {
            mTouchedBgDrawable = mContext.getResources().getDrawable((resId));
        }
        hasTouchedDrawable = true;
        mBackgroundDrawableResource = resId;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (showBg) {
            if (hasTouchedDrawable) {
                setBackground(mTouchedBgDrawable);
            } else {
                canvas.drawColor(mTouchedBgColor);
            }
        } else {
            if (hasTouchedDrawable) {
                setBackground(null);
            }
        }

        for (int i = 0; i < letters.length; i++) {

            paint.setAntiAlias(true);
            paint.setTextSize(mTextSize);

            if (mCurrentLetterIndex == i) {
                paint.setColor(getResources().getColor(mTextTouchedColor));
                paint.setFakeBoldText(true);
            } else {
                paint.setColor(getResources().getColor(mTextColor));
                paint.setTypeface(mTypeface);
            }

            //计算每个字母需要绘制的所在的位置
            float x = barWidth / 2 - paint.measureText(letters[i]) / 2;
            float y = singleHeight * i + singleHeight;

            canvas.drawText(letters[i], x, y, paint);
            paint.reset();

        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int lastIndex = mCurrentLetterIndex;
        float y = event.getY();
        int currentTouchIndex = (int) (y / getHeight() * letters.length);

        if (listener != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    showBg = false;
                    mCurrentLetterIndex = -1;
                    invalidate();

                    if (mLetterTv != null) {
                        mLetterTv.setVisibility(GONE);
                    }
                    break;
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    showBg = true;
                    if (currentTouchIndex != lastIndex
                            && currentTouchIndex >= 0 && currentTouchIndex < letters.length) {
                        listener.onTouchListener(letters[currentTouchIndex], currentTouchIndex);

                        if (mLetterTv != null) {
                            mLetterTv.setText(letters[currentTouchIndex]);
                            mLetterTv.setVisibility(VISIBLE);
                        }

                        mCurrentLetterIndex = currentTouchIndex;
                        invalidate();
                    }
                    break;
                default:
            }
        }
        return true;
    }


    public interface OnLetterTouchListener {
        /**
         * 对侧边栏触摸事件进行监听
         *
         * @param letter   当前触摸的索引字母
         * @param position 当前触摸的位置
         */
        void onTouchListener(String letter, int position);
    }


}
