package com.londonappbrewery.climapm;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class change_city extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_city_layout);

        final EditText enterCity = findViewById(R.id.queryET);

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setImageResource(R.drawable.left);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        enterCity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String cityName = enterCity.getText().toString();
                Intent newIntent  = new Intent(change_city.this, WeatherController.class);
                newIntent.putExtra("newCity", cityName);
                startActivity(newIntent);
                return false;
            }
        });

    }
}
