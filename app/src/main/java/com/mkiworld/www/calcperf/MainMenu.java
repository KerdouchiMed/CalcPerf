package com.mkiworld.www.calcperf;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainMenu extends AppCompatActivity {

    int BestScore;
    SharedPreferences bestScoreShared;
    TextView bestScoreTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        bestScoreTV = findViewById(R.id.bestScore);

        bestScoreShared = getSharedPreferences(MainGame.BEST_SCORE_KEY,MODE_PRIVATE);
        BestScore = bestScoreShared.getInt(MainGame.BEST_SCORE_KEY,0);
        bestScoreTV.setText("Best Score : "+BestScore);
        Log.d("medk", "onCreate: "+BestScore);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bestScoreShared = getSharedPreferences(MainGame.BEST_SCORE_KEY,MODE_PRIVATE);
        BestScore = bestScoreShared.getInt(MainGame.BEST_SCORE_KEY,0);
        bestScoreTV.setText("Best Score : "+BestScore);
    }

    public void startGame(View v)
    {
        Intent intent = new Intent(MainMenu.this,MainGame.class);
        startActivity(intent);
    }
}
