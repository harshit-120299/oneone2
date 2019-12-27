package com.swavlambibharat.myenglishvocab;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.EditText;
import android.widget.TextView;

public class OnItemClick extends AppCompatActivity {
    TextView word,meaning;
    String s,w;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_item_click);
        word=findViewById(R.id.Heading);
        w=getIntent().getExtras().getString("Word");
        word.setTypeface(null, Typeface.BOLD_ITALIC);
        word.setText("'"+w+"'");
        meaning=findViewById(R.id.textView);
        s=getIntent().getExtras().getString("Meaning");
        meaning.setTypeface(null, Typeface.BOLD_ITALIC);
        meaning.setText(s);
        meaning.setMovementMethod(new ScrollingMovementMethod());
        meaning.setTextIsSelectable(true);
    }
}
