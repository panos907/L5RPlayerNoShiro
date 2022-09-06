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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.HashMap;

import static android.R.color.*;
import static com.example.myapplication.R.layout.popup_window;

public class EquipmentActivity extends AppCompatActivity {

    Bundle extras;
    TextView mytext;
    TableLayout stk;
    HashMap<String,Armor> armors;
    HashMap<String, Weapon> weapons;

    private void init() {
        stk = findViewById(R.id.eqtable);
        TableRow tbrow0 = new TableRow(this);

        Button helpbutton = new Button(this);
        helpbutton.setText("Help");
        helpbutton.setOnClickListener(this::onHelpClick);
        stk.addView(helpbutton);

        TextView tv1 = new TextView(this);
        tv1.setText("Armor");
        tv1.setTextColor(Color.WHITE);
        tv1.setGravity(Gravity.CENTER);
        tv1.setTextSize(24);
        tbrow0.addView(tv1);

        TextView tv2 = new TextView(this);
        tv2.setTextSize(24);
        tv2.setText("TN Bonus");
        tv2.setTextColor(Color.WHITE);
        tv2.setGravity(Gravity.CENTER);
        tbrow0.addView(tv2);

        stk.addView(tbrow0);


        for (HashMap.Entry<String, Armor> entry : armors.entrySet()) {

            // inflate the layout of the popup window
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View skillView = inflater.inflate(R.layout.tablerow_rest, null);

            TextView t2v = skillView.findViewById(R.id.myview1);
            t2v.setText(entry.getKey());
            if (!entry.getValue().is_bought) t2v.setTextColor(Color.WHITE);
            else t2v.setTextColor(Color.GREEN);
            t2v.setClickable(true);
            t2v.setOnClickListener(this::onArmorClick);

            TextView t3v = skillView.findViewById(R.id.myview2);
            t3v.setTextColor(Color.WHITE);
            t3v.setText(String.valueOf(entry.getValue().armor_tn));
            t3v.setClickable(true);

            Button plusbutton = skillView.findViewById(R.id.mybutton);
            plusbutton.setText("Get");
            plusbutton.setOnClickListener(this::onBuyArmorClick);

            stk.addView(skillView);

        }

        TableRow tbrow1 = new TableRow(this);

        TextView tv3 = new TextView(this);
        tv3.setText("Weapon");
        tv3.setTextColor(Color.WHITE);
        tv3.setGravity(Gravity.LEFT);
        tv3.setTextSize(24);
        tbrow1.addView(tv3);

        TextView tv4 = new TextView(this);
        tv4.setTextSize(24);
        tv4.setText("Damage");
        tv4.setTextColor(Color.WHITE);
        tv4.setGravity(Gravity.CENTER);
        tbrow1.addView(tv4);

        stk.addView(tbrow1);

        for (HashMap.Entry<String, Weapon> entry : weapons.entrySet()) {

            // inflate the layout of the popup window
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View skillView = inflater.inflate(R.layout.tablerow_rest, null);

            TextView t2v = skillView.findViewById(R.id.myview1);
            t2v.setText(entry.getKey());
            if (!entry.getValue().is_bought) t2v.setTextColor(Color.WHITE);
            else t2v.setTextColor(Color.GREEN);
            t2v.setClickable(true);
            t2v.setOnClickListener(this::onWeaponClick);

            TextView t3v = skillView.findViewById(R.id.myview2);
            t3v.setTextColor(Color.WHITE);

            if (entry.getValue().skill.contains("Kyujutsu"))
                t3v.setText(("STR:"+entry.getValue().roll));
            else
                t3v.setText((entry.getValue().roll)+"k"+(entry.getValue().keep));
            t3v.setClickable(true);

            Button plusbutton = skillView.findViewById(R.id.mybutton);
            plusbutton.setText("Get");
            plusbutton.setOnClickListener(this::onBuyWeaponClick);

            stk.addView(skillView);

        }

        Button submitbutton= new Button(this);
        submitbutton.setText("Submit");
        submitbutton.setOnClickListener(this::onSubmitClick);
        stk.addView(submitbutton);
    }

    private void onWeaponClick(View view) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(popup_window,null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

       String temp = ((TextView) view).getText().toString();

        TextView helptext= popupView.findViewById(R.id.popuptext);

        if (weapons.get(temp).skill.contains("Kyujutsu"))
            if (weapons.get(temp).keywords[1].contains("None"))
                helptext.setText(weapons.get(temp).name+"\n"+"Keywords:"+weapons.get(temp).keywords[0]+"\n"+"RANGE:"+(weapons.get(temp).keep)+"\n"+weapons.get(temp).description);
            else
                helptext.setText(weapons.get(temp).name+"\n"+"Keywords:"+weapons.get(temp).keywords[0]+","+weapons.get(temp).keywords[1]+"\n"+"RANGE:"+(weapons.get(temp).keep)+"\n"+weapons.get(temp).description);
        else
            if (weapons.get(temp).keywords[1].contains("None"))
                helptext.setText(weapons.get(temp).name+"\n"+"Keywords:"+weapons.get(temp).keywords[0]+"\n"+weapons.get(temp).description);
            else
                helptext.setText(weapons.get(temp).name+"\n"+"Keywords:"+weapons.get(temp).keywords[0]+","+weapons.get(temp).keywords[1]+"\n"+weapons.get(temp).description);


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


    private void onBuyWeaponClick(View view) {
        TableRow row= (TableRow) view.getParent();
        mytext = (TextView)row.getChildAt(0);

        String current = mytext.getText().toString();

        if (!weapons.get(current).is_bought){
            weapons.get(current).is_bought=true;
            mytext.setTextColor(Color.GREEN);
        }
        else {
            weapons.get(current).is_bought=false;
            mytext.setTextColor(Color.WHITE);
        }
    }


    private void onBuyArmorClick(View view) {
        TableRow row= (TableRow) view.getParent();
        mytext = (TextView)row.getChildAt(0);

        String current = mytext.getText().toString();

        if (!armors.get(current).is_bought) {
            armors.get(current).is_bought=true;
            mytext.setTextColor(Color.GREEN);
        }
        else {
            armors.get(current).is_bought=false;
            mytext.setTextColor(Color.WHITE);
        }
    }


    private void onArmorClick(View view) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(popup_window,null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        String temp = ((TextView) view).getText().toString();

        TextView helptext= popupView.findViewById(R.id.popuptext);

        helptext.setText(armors.get(temp).name+"\n"+armors.get(temp).description);
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
        View popupView = inflater.inflate(R.layout.popup_table, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        TableLayout stk2= popupView.findViewById(R.id.popuptable);

        TableRow tbrow = new TableRow(this);
        TextView helptext= new TextView(this);

        helptext.setText("You may select Armor and Weapons in this screen.");
        helptext.setSingleLine(false);
        helptext.setMaxEms(8);
        helptext.setMovementMethod(new ScrollingMovementMethod());
        helptext.setTextColor(Color.WHITE);
        helptext.setBackgroundColor(Color.BLACK);
        helptext.setGravity(Gravity.CENTER);
        helptext.setTextSize(28);

        tbrow.addView(helptext);
        stk2.addView(tbrow);

        TableRow tbrow2 = new TableRow(this);
        TextView helptext2= new TextView(this);

        helptext2.setText("Here's your current gear: ");
        helptext2.setSingleLine(false);
        helptext2.setMaxEms(8);
        helptext2.setTextColor(Color.WHITE);
        helptext2.setBackgroundColor(Color.BLACK);
        helptext2.setGravity(Gravity.CENTER);
        helptext2.setTextSize(28);

        tbrow2.addView(helptext2);
        stk2.addView(tbrow2);

        for (HashMap.Entry<String, Weapon> entry : weapons.entrySet()) {
            if (entry.getValue().is_bought) {
                TableRow tbrow1 = new TableRow(this);

                TextView t3v = new TextView(this);
                t3v.setText(entry.getKey());
                t3v.setTextColor(Color.WHITE);
                t3v.setGravity(Gravity.CENTER);
                t3v.setTextSize(18);
                t3v.setClickable(true);
                tbrow1.addView(t3v);

                stk2.addView(tbrow1);
            }
        }

        for (HashMap.Entry<String, Armor> entry : armors.entrySet()) {
            if (entry.getValue().is_bought) {
                TableRow tbrow1 = new TableRow(this);

                TextView t3v = new TextView(this);
                t3v.setText(entry.getKey());
                t3v.setTextColor(Color.WHITE);
                t3v.setGravity(Gravity.CENTER);
                t3v.setTextSize(18);
                t3v.setClickable(true);
                tbrow1.addView(t3v);

                stk2.addView(tbrow1);
            }
        }

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

    private void onSubmitClick(View view){

        extras.putSerializable("ARMORS",armors);
        extras.putSerializable("WEAPONS",weapons);

        Intent myintent = new Intent(EquipmentActivity.this, CustomMenuActivity.class);

        myintent.putExtras(extras);

        startActivity(myintent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment);

        extras= getIntent().getExtras();

        armors= new HashMap<>();
        armors= (HashMap<String, Armor>) extras.getSerializable("ARMORS");
        weapons= new HashMap<>();
        weapons= (HashMap<String, Weapon>) extras.getSerializable("WEAPONS");

        init();

    }
}