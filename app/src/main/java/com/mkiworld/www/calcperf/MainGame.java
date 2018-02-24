package com.mkiworld.www.calcperf;

import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainGame extends AppCompatActivity {

    public final static String BEST_SCORE_KEY = "BestScore";

    TextView timeTV;
    TextView inputTV;
    TextView scoreTV;
    TextView levelTV;
    TextView[] choixArray ;
    ConstraintLayout endGameDialog;
    TextView finalScoreTV;
    TextView yourErrorTV;

    long bonus;
    int score;
    int correctChoice;

    boolean isOver;
    public CountDownTimer countDownTimer;
    Random rand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);

        isOver=false;
        rand = new Random();
        bonus = 0;
        score = 0;

        timeTV = findViewById(R.id.timeTV);
        inputTV = findViewById(R.id.input);
        scoreTV = findViewById(R.id.score);
        levelTV = findViewById(R.id.level);
        finalScoreTV = findViewById(R.id.finalScore);
        choixArray = new TextView[]{
                findViewById(R.id.choix_1),
                findViewById(R.id.choix_2),
                findViewById(R.id.choix_3),
                findViewById(R.id.choix_4)
        };

        endGameDialog = findViewById(R.id.endGameDialog);
        yourErrorTV = findViewById(R.id.your_Error);

        startNewCalcPuzzel(0,true);
    }


    public void startNewCalcPuzzel(int level,boolean isNew)
    {
        levelTV.setText("Level : "+level);
        if(isNew)
        {
            score = 0;
            scoreTV.setText("Score : "+score);
        }
        if(countDownTimer != null)
        {
            countDownTimer.cancel();
        }

        countDownTimer =  new CountDownTimer(7000,100)
        {

            @Override
            public void onTick(long l) {

                timeTV.setText(""+l/100);
                bonus = l/100;
            }

            @Override
            public void onFinish() {
                isOver=true;
                finalScoreTV.setText("Score : "+score);
                yourErrorTV.setText("Time Out");
                setBestScore(score);

                endGameDialog.setVisibility(View.VISIBLE);
                timeTV.setText("finish");
            }
        };


        int input_1;
        int input_2;
        if(level == 0)
        {
            input_1 = rand.nextInt(10);
            input_2 = rand.nextInt(10);
        }
        else
        {
            input_1 = ((level-1)*10)+rand.nextInt(20);
            input_2 = ((level-1)*10)+rand.nextInt(20);
        }
        correctChoice = input_1+input_2;
        inputTV.setText(input_1+" + "+input_2);

        Log.d("medk", "startNewCalcPuzzel: avant getChoices");
        ArrayList<Integer> choices = genChoices(correctChoice);
        Log.d("medk", "startNewCalcPuzzel: apres getChoices");
        for(int i = 0;i<4;i++)
        {
            int nbr = choices.get(i);
            if(nbr<10)
                choixArray[i].setText("0"+nbr);
            else
                choixArray[i].setText(""+nbr);

        }

        countDownTimer.start();
    }


    //cette fct genere des nombre pour l'insere dans les choix
    public ArrayList<Integer> genChoices(int correctChoice)
    {
        ArrayList<Integer> choices = new ArrayList<>();
        int[] deceiveChoices = {correctChoice+1,correctChoice-1,correctChoice+10,correctChoice-10};
        int deceiveChoice;
        int otherChoice;

        if(correctChoice<20)
            deceiveChoice = deceiveChoices[rand.nextInt(2)];
        else
            deceiveChoice = deceiveChoices[rand.nextInt(2)+2];

        int trueChoicePosition = rand.nextInt(4);
        int deceiveChoicePosition = (trueChoicePosition+rand.nextInt(3)+1)%4;


        for(int i = 0; i<4;i++)
        {
            if(i==trueChoicePosition)
                choices.add(correctChoice);
            else if(i==deceiveChoicePosition)
                choices.add(deceiveChoice);
            else
            {
                otherChoice = (correctChoice/2) + rand.nextInt(20);
                while (choices.contains(otherChoice) || otherChoice == correctChoice || otherChoice==deceiveChoice)
                    otherChoice = (correctChoice/2) + rand.nextInt(correctChoice);

                choices.add(otherChoice);
            }
        }

        return choices;

    }



    //cette fnct genere des color aleatoir pour les choix
    public ArrayList<String> genChoicesColor()
    {
        ArrayList<String> ColorList = new ArrayList<>();
        String[] colors = new String[]{"#1abc9c","#2ecc71","#3498db","#9b59b6","#f1c40f","#e67e22","#e74c3c","#ecf0f1"};

        for(int i = 0; i<4;i++)
        {
            String color = colors[rand.nextInt(colors.length)];
            while (ColorList.contains(color))
                color = colors[rand.nextInt(colors.length)];
            ColorList.add(color);
        }
        return ColorList;

    }



    public void selectChoice(View v)
    {
        if(!isOver) {
            TextView choiceTV = (TextView) v;
            int level;
            int choice = Integer.parseInt(choiceTV.getText().toString());

            if (correctChoice == choice) {
                score+=(1+bonus/10);
                level =  score/20;
                scoreTV.setText("Score : " + score);
                levelTV.setText("Level : " + level);
                Log.d("medk", "selectChoice: "+score);
                startNewCalcPuzzel(level,false);
                Log.d("medk", "selectChoice: apres StartNewAct"+score);
            }
            else
            {
                isOver=true;
                finalScoreTV.setText("Score : "+score);
                yourErrorTV.setText(inputTV.getText()+" =/= "+choice);
                setBestScore(score);
                endGameDialog.setVisibility(View.VISIBLE);
                countDownTimer.cancel();
            }
        }
    }



    public boolean setBestScore(int score)
    {
        SharedPreferences bestScoreShared = getSharedPreferences(BEST_SCORE_KEY,MODE_PRIVATE);
        int bestScore = bestScoreShared.getInt(BEST_SCORE_KEY,0);
        Log.d("medk", "setBestScore: "+bestScore);

        if(bestScore < score)
        {
            bestScoreShared.edit().putInt(BEST_SCORE_KEY,score).commit();
            return true;
        }
        return false;
    }

    public void exitToMain(View v)
    {
        isOver=false;
        endGameDialog.setVisibility(View.GONE);
        finish();
    }


    public void replay(View v)
    {
        isOver=false;
        startNewCalcPuzzel(0,true);
        endGameDialog.setVisibility(View.GONE);
    }
}
