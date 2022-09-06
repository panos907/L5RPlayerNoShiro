package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;


import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class CustomShugenjaMenuActivity extends AppCompatActivity {


    Bundle extras;
    Intent myintent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_shugenja_menu);

        extras=getIntent().getExtras();

    }

    public void onNewSkillsClick(View view) {
        myintent = new Intent(CustomShugenjaMenuActivity.this, NewSkillsInputActivity.class);

        myintent.putExtras(extras);

        startActivity(myintent);
    }


    public void onAdvantagesClick(View view) {
        myintent = new Intent(CustomShugenjaMenuActivity.this, AdvantagesActivity.class);

        myintent.putExtras(extras);

        startActivity(myintent);
    }

    public void onDisadvantagesClick(View view) {
        myintent = new Intent(CustomShugenjaMenuActivity.this, DisadvantagesActivity.class);

        myintent.putExtras(extras);

        startActivity(myintent);
    }


    public void onTraitsClick(View view) {
        myintent = new Intent(CustomShugenjaMenuActivity.this, TraitsActivity.class);

        myintent.putExtras(extras);

        startActivity(myintent);
    }

    public void onSpellsClick(View view) {

        if (extras.getInt("EXP_VALUE")==0) {
            myintent = new Intent(CustomShugenjaMenuActivity.this, SpellsActivity.class);

            myintent.putExtras(extras);
            startActivity(myintent);

        }
        else Toast.makeText(this, "Please spend all your EXP first!", Toast.LENGTH_SHORT).show();
    }


    public void onViewSheetClick(View view) {
        myintent = new Intent(CustomShugenjaMenuActivity.this, ViewSheetActivity.class);

        myintent.putExtras(extras);

        startActivity(myintent);

    }

    public void onHomeClick(View view) {
        myintent = new Intent(CustomShugenjaMenuActivity.this, MainActivity.class);

        startActivity(myintent);
    }
}