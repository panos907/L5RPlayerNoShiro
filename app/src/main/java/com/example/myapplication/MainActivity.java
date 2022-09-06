package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.myapplication.R.layout.popup_window;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    //This button is used to start the ClanSchoolActivity
    public void onButtonClick(android.view.View view) {
        startActivity(new Intent(MainActivity.this, ClanSchoolActivity.class));
    }

    public void onLoadClick(View view) {
        startActivity(new Intent(MainActivity.this, LoadActivity.class));
    }


    public void onHelpClick(View view) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(popup_window,null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        TextView helptext= popupView.findViewById(R.id.popuptext);

        helptext.setText("Character Generation\n" +
                "\n" +
                "Step 1: Pick Your Clan\n" +
                "\n" +
                "This is the most defining aspect of any Legend of the Five Ring character, and the one that will most impact your roleplaying experience. Select the Clan from which your character originates.\n" +
                "\n" +
                "Step 2: Pick Your School\n" +
                "\n" +
                "Even within a single clan, the various schools offer a wide variety of characters that can be created. Once your clan is chosen, pick one of the schools that make up the clan in order to receive additional benefits.\n" +
                "\n" +
                "Step 3: Customize Your Character\n" +
                "\n" +
                "Experience Points are given to characters to represent the improvement of their abilities over time. They are given to characters at the time of character creation in order to represent all that a character has learned during their lifetime up to the point that the game begins. " +
                "A normal starting character begins with 40 Experience Points to spend on purchasing Traits, Advantages, and Skills. Additional Experience Points may be acquired by purchasing Disadvantages.\n" +
                "Feel free to set any number of Experience Points at the respective screen, after consulting your Game Master.\n"+
                "You may select Skills, Advantages/Disadvantages and Traits in any order, but it is recommended to always start by increasing some of your character's Traits.");
        helptext.setSingleLine(false);
        helptext.setMaxEms(16);
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
