package com.example.androidprojectcollection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Objects;
import java.util.Random;

public class ButtonExercise extends AppCompatActivity {

    Button btnClose;
    Button btnToast;

    Button btnChangeBG;
    Button btnChangeButtonBG;
    Button btnDisappear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_button_exercise);

        btnClose = (Button) findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(
                        ButtonExercise.this,
                        ButtonExercise2.class
                );

                startActivity(intent1);
            }
        });

        CharSequence toastText = "Toast in Button Exercise";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, toastText,duration);
        btnToast = (Button) findViewById(R.id.btnToast);
        btnToast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toast.show();
            }
        });

        View v = findViewById(R.id.ButtonPage);
        Random randomColor = new Random();
        btnChangeBG = (Button) findViewById(R.id.btnChangeBG);
        btnChangeBG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v.setBackgroundColor(Color.argb(255, randomColor.nextInt(255), randomColor.nextInt(255), randomColor.nextInt(255)));
            }
        });

        btnChangeButtonBG = (Button) findViewById(R.id.btnChangeButtonBG);
        btnChangeButtonBG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnChangeButtonBG.setBackgroundColor(Color.argb(255, randomColor.nextInt(255), randomColor.nextInt(255), randomColor.nextInt(255)));
            }
        });

        btnDisappear = (Button) findViewById(R.id.btnDisappear);
        btnDisappear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDisappear.setVisibility(View.INVISIBLE);
            }
        });
    }
}