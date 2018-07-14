package com.ironbear775.slidebar;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * @author ironbear775
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView = findViewById(R.id.letterTv);

        SlideBar slideBar = findViewById(R.id.slide_bar);
        slideBar.setTextView(textView);
        slideBar.setTextColor(R.color.colorAccent);
        slideBar.setTouchedBackgroundColor(Color.BLUE);
        slideBar.setOnLetterTouchListener(new SlideBar.OnLetterTouchListener() {
            @Override
            public void onTouchListener(String letter,int position) {
                textView.setText(letter);
            }
        });
    }

}
