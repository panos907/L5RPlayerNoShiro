package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;
import static android.view.Gravity.CENTER;

public class CustomMenuActivity extends AppCompatActivity {

    Bundle extras;
    Intent myintent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_menu);

        extras=getIntent().getExtras();
    }

    public void onNewSkillsClick(View view) {
        myintent = new Intent(CustomMenuActivity.this, NewSkillsInputActivity.class);

        myintent.putExtras(extras);

        startActivity(myintent);
    }


    public void onAdvantagesClick(View view) {
        myintent = new Intent(CustomMenuActivity.this, AdvantagesActivity.class);

        myintent.putExtras(extras);

        startActivity(myintent);
    }

    public void onDisadvantagesClick(View view) {
        myintent = new Intent(CustomMenuActivity.this, DisadvantagesActivity.class);

        myintent.putExtras(extras);

        startActivity(myintent);
    }


    public void onTraitsClick(View view) {
        myintent = new Intent(CustomMenuActivity.this, TraitsActivity.class);

        myintent.putExtras(extras);

        startActivity(myintent);
    }


    public void onViewSheetClick(View view) {
        myintent = new Intent(CustomMenuActivity.this, ViewSheetActivity.class);

        myintent.putExtras(extras);

        startActivity(myintent);
    }

    public void onEquipmentClick(View view) {
        myintent = new Intent(CustomMenuActivity.this, EquipmentActivity.class);

        myintent.putExtras(extras);

        startActivity(myintent);
    }

    public void onHomeClick(View view) {
        myintent = new Intent(CustomMenuActivity.this, MainActivity.class);

        startActivity(myintent);
    }
}