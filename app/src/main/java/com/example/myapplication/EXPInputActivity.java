package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.myapplication.R.layout.popup_window;

public class EXPInputActivity extends AppCompatActivity {

    EditText edt;
    Integer num;
    String st;
    Intent myintent;
    Bundle extras;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exp_input);

        edt= findViewById(R.id.myEXPinput);

        extras=getIntent().getExtras();

    }

    public void onButtonClick(android.view.View view) {

        st= edt.getText().toString();
        int temp=-1;

        if (!st.equals("")){
            num=Integer.parseInt(st);

            temp=extras.getInt("EXP_VALUE")+num;

            extras.putInt("EXP_VALUE",temp);

            if (!extras.getString("SCHOOL").contains("Shugenja")) myintent = new Intent(EXPInputActivity.this, CustomMenuActivity.class);
            else         myintent = new Intent(EXPInputActivity.this, CustomShugenjaMenuActivity.class);

            myintent.putExtras(extras);

            startActivity(myintent);

        } else Toast.makeText(this, "Please input a number!", Toast.LENGTH_SHORT).show();

    }

    public void onHelpClick(android.view.View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(popup_window,null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        TextView helptext= popupView.findViewById(R.id.popuptext);

        helptext.setText("In this screen, you can se the number of EXP points to be used throughout character creation.\n" +
                "\n" +
                "We recommend setting any value between 40 and 100 if you are a beginner player.\n");
        helptext.setSingleLine(false);
        helptext.setMaxEms(18);
        helptext.setMovementMethod(new ScrollingMovementMethod());
        helptext.setTextColor(Color.WHITE);
        helptext.setBackgroundColor(Color.BLACK);
        helptext.setGravity(Gravity.CENTER);
        helptext.setTextSize(20);


        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window token
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    popupWindow.dismiss();
                }
                return true;
            }
        });

    }

}