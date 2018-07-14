package com.ironbear775.slidebar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
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
    private int textSize = 40;
    private int textColor = Color.BLACK;
    private int textTouchedColor = Color.YELLOW;
    private int bgColor = Color.LTGRAY;
    private boolean showBg = false;
    private int currentLetterIndex = -1;
    private Paint paint = new Paint();
    private TextView letterTv = null;
    private int barWidth;
    private int singleHeight;

    public SlideBar(Context context) {
        super(context);
    }

    public SlideBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        init();
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
        letterTv = textView;
    }

    public void setLetterTv(TextView tv) {
        letterTv = tv;
    }

    public void setTextColor(int color) {
        textColor = color;
    }

    public void setTextTouchedColor(int textTouchedColor) {
        this.textTouchedColor = textTouchedColor;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    @Override
    public void setBackgroundColor(int bgColor) {
        this.bgColor = bgColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (showBg) {
            canvas.drawColor(bgColor);
        }

        paint.setAntiAlias(true);
        paint.setTextSize(textSize);

        for (int i = 0; i < letters.length; i++) {
            paint.setTextSize(textSize);

            if (currentLetterIndex == i) {
                paint.setColor(textTouchedColor);
                paint.setFakeBoldText(true);
            } else {
                paint.setColor(textColor);
                paint.setTypeface(Typeface.DEFAULT_BOLD);
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
        int lastIndex = currentLetterIndex;
        float y = event.getY();
        int currentTouchIndex = (int) (y / getHeight() * letters.length);

        if (listener != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    showBg = false;
                    currentLetterIndex = -1;
                    invalidate();

                    if (letterTv != null) {
                        letterTv.setVisibility(GONE);
                    }
                    break;
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    showBg = true;
                    if (currentTouchIndex != lastIndex
                            && currentTouchIndex >= 0 && currentTouchIndex < letters.length) {
                        listener.onTouchListener(letters[currentTouchIndex],currentTouchIndex);

                        if (letterTv != null) {
                            letterTv.setText(letters[currentTouchIndex]);
                            letterTv.setVisibility(VISIBLE);
                        }

                        currentLetterIndex = currentTouchIndex;
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
         * @param letter 当前触摸的索引字母
         * @param position 当前触摸的位置
         */
        void onTouchListener(String letter,int position);
    }


}
