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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static com.example.myapplication.R.layout.popup_window;

public class AdvantagesActivity extends AppCompatActivity {

    Bundle extras;
    Intent myintent;
    String temp;
    TextView exptext,mytext;
    String current_adv;
    TableLayout stk;
    HashMap<String,Advantage> advantages;
    int exp,cost;


    public boolean buyAdvantage(String key,Advantage adv){

        cost = adv.cost;

        if ((exp - cost) >= 0){
            adv.is_bought=true;
            advantages.put(key,adv);
            exp= exp- cost;
            return true;
        }
        else {
            Toast.makeText(this, "You don't have enough Experience Points!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    private boolean sellAdvantage(String key, Advantage adv) {
        if (adv.is_bought) {
            cost = adv.cost;
            exp = exp + cost;
            adv.is_bought = false;
            advantages.put(key, adv);
            return true;
        } else return false;
    }


    public void init() {
        stk = findViewById(R.id.advtable);

        for (HashMap.Entry<String, Advantage> entry : advantages.entrySet()) {

            // inflate the layout of the popup window
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View skillView = inflater.inflate(R.layout.tablerow_rest, null);

            TextView t2v = skillView.findViewById(R.id.myview1);
            t2v.setText(entry.getKey());
            t2v.setTextColor(Color.WHITE);
            t2v.setClickable(true);
            t2v.setOnClickListener(this::onAdvClick);

            TextView t3v = skillView.findViewById(R.id.myview2);
            if (entry.getValue().is_bought) t3v.setTextColor(Color.GREEN);
            else t3v.setTextColor(Color.WHITE);
            t3v.setText(String.valueOf(entry.getValue().cost));

            Button plusbutton= skillView.findViewById(R.id.mybutton);
            plusbutton.setText("Get");
            plusbutton.setOnClickListener(this::onBuyClick);

            stk.addView(skillView);

        }

        Button submitbutton= new Button(this);
        submitbutton.setText("Submit");
        submitbutton.setOnClickListener(this::onSubmitClick);
        stk.addView(submitbutton);

        Button helpbutton= new Button(this);
        helpbutton.setText("Help");
        helpbutton.setOnClickListener(this::onHelpClick);
        stk.addView(helpbutton);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advantages);

        extras = getIntent().getExtras();
        advantages= new HashMap<>();

        advantages= (HashMap) extras.getSerializable("ADVANTAGES");
        exp=extras.getInt("EXP_VALUE");

        exptext=findViewById(R.id.textadvexp);
        exptext.setTextColor(Color.WHITE);

        init();

        exptext.setText("Experience Points: "+exp);
    }

    private void onAdvClick(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(popup_window,null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        temp = ((TextView) view).getText().toString();

        TextView helptext= popupView.findViewById(R.id.popuptext);

        helptext.setText(advantages.get(temp).name+"\n"+advantages.get(temp).content);
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
                popupWindow.dismiss();
                return true;
            }
        });
    }


    private void onHelpClick(View view) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        TextView helptext= popupView.findViewById(R.id.popuptext);
        helptext.setText("You can buy Advantages in this screen."+"\n"+"Click on an Advantage to learn more about it.");
        helptext.setSingleLine(false);
        helptext.setMaxEms(8);
        helptext.setMovementMethod(new ScrollingMovementMethod());
        helptext.setTextColor(Color.WHITE);
        helptext.setBackgroundColor(Color.BLACK);
        helptext.setGravity(Gravity.CENTER);
        helptext.setTextSize(28);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window token
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    private void onSubmitClick(View view) {
        //This command is used to insert all the skills and their specific value inside the Bundle, for passing it to the next activity.

        extras.putInt("EXP_VALUE",exp);

        if (!extras.getString("SCHOOL").contains("Shugenja")) myintent = new Intent(AdvantagesActivity.this, CustomMenuActivity.class);
        else myintent = new Intent(AdvantagesActivity.this, CustomShugenjaMenuActivity.class);

        myintent.putExtras(extras);

        startActivity(myintent);
    }

    private void onBuyClick(View view) {
        TableRow row= (TableRow) view.getParent();
        mytext = (TextView)row.getChildAt(0);

        current_adv= mytext.getText().toString();

        if (!advantages.get(current_adv).is_bought) {
            boolean flag= buyAdvantage(current_adv, advantages.get(current_adv));
            if (flag==true) mytext.setTextColor(Color.GREEN);
        } else {
            boolean flag= sellAdvantage(current_adv,advantages.get(current_adv));
            if (flag==true) mytext.setTextColor(Color.WHITE);
        }


        exptext.setText("Experience Points: "+exp);
    }

}