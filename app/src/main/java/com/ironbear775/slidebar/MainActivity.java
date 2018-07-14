package com.ironbear775.slidebar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * @author ironbear775
 */
public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView = findViewById(R.id.letterTv);

        SlideBar slideBar = findViewById(R.id.slide_bar);
        slideBar.setTextView(textView);
        slideBar.setOnLetterTouchListener(new SlideBar.OnLetterTouchListener() {
            @Override
            public void onTouchListener(String letter,int position) {
                textView.setText(letter);
                Log.e(TAG, "position: "+position);
            }
        });

    }

}
