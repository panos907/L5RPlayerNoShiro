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
import android.widget.Toast;

import java.util.HashMap;

public class SpellsActivity extends AppCompatActivity {

    Bundle extras;
    Intent myintent;
    TextView mytext;
    TableLayout stk;
    HashMap<String,Spell> spells;
    HashMap<String, Advantage> advantages;
    HashMap<String, Integer> skills,traits;
    String current_spell,temp;
    Shugenja_School school;
    int earth,fire,air,water,ishiken,rank=0;

    public void init() {
        stk = findViewById(R.id.spelltable);

        for (HashMap.Entry<String, Spell> entry : spells.entrySet()) {
            //We only show spells that the shugenja can learn at their current Insight Rank
            if (entry.getValue().mastery_rank<=rank) {

                // inflate the layout of the popup window
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View skillView = inflater.inflate(R.layout.tablerow_rest, null);

                TextView t2v = skillView.findViewById(R.id.myview1);
                t2v.setText(entry.getKey());
                if (entry.getValue().is_learned) t2v.setTextColor(Color.GREEN);
                else t2v.setTextColor(Color.WHITE);
                t2v.setClickable(true);
                t2v.setOnClickListener(this::onSpellClick);

                TextView t3v = skillView.findViewById(R.id.myview2);
                t3v.setText(String.valueOf(entry.getValue().mastery_rank));

                Button plusbutton = skillView.findViewById(R.id.mybutton);
                plusbutton.setText("Learn");
                plusbutton.setOnClickListener(this::onBuyClick);

                stk.addView(skillView);
            }
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

    public void calculateInsightRank(){
        int sum=0;
        sum=(fire+water+air+earth+ishiken)*10;

        for (HashMap.Entry<String, Integer> entry : skills.entrySet()) {
            sum = sum + entry.getValue();
        }

        if (sum>=225) rank=5;
        else if (sum>=200) rank=4;
        else if (sum>=175) rank=3;
        else if (sum>=150) rank=2;
        else if (sum>=0) rank=1;
    }

    public void calculateRings(){
        ishiken=traits.get("Void");
        fire= Math.min(traits.get("Intelligence"),traits.get("Agility"));
        air= Math.min(traits.get("Awareness"),traits.get("Reflexes"));
        earth= Math.min(traits.get("Willpower"),traits.get("Stamina"));
        water= Math.min(traits.get("Strength"),traits.get("Perception"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spells);

        spells = new HashMap<>();
        advantages= new HashMap<>();
        school= new Shugenja_School();
        skills= new HashMap<>();
        traits=new HashMap<>();

        extras= getIntent().getExtras();


        spells= (HashMap<String, Spell>) extras.getSerializable("SPELLS");
        advantages= (HashMap<String, Advantage>) extras.getSerializable("ADVANTAGES");
        skills= (HashMap<String,Integer>) extras.getSerializable("SKILLS_MAP");
        traits=(HashMap<String, Integer>)  extras.getSerializable("TRAITS_MAP");

        school= (Shugenja_School) extras.getSerializable("SHUGENJA_SCHOOL");


        if (advantages.get("ISHIKEN-DO").is_bought && school.ishiken==0) school.ishiken=3;

        calculateRings();
        calculateInsightRank();

        init();

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

        TableLayout stk2 = popupView.findViewById(R.id.popuptable);

        TableRow tbrow = new TableRow(this);
        TextView helptext= new TextView(this);

        helptext.setText("You may select Spells in this screen. The number of spells remaining for each Ring is presented below: ");
        helptext.setSingleLine(false);
        helptext.setMaxEms(8);
        helptext.setMovementMethod(new ScrollingMovementMethod());
        helptext.setTextColor(Color.WHITE);
        helptext.setGravity(Gravity.CENTER);
        helptext.setTextSize(28);

        tbrow.addView(helptext);
        stk2.addView(tbrow);

        if (school.fire>0) {
            TableRow tbrow1 = new TableRow(this);

            TextView t3v = new TextView(this);
            t3v.setText("Fire Spells: "+school.fire);
            t3v.setTextColor(Color.WHITE);
            t3v.setGravity(Gravity.CENTER);
            t3v.setTextSize(18);
            t3v.setClickable(true);
            tbrow1.addView(t3v);

            stk2.addView(tbrow1);
        }

        if (school.earth>0) {
            TableRow tbrow1 = new TableRow(this);

            TextView t3v = new TextView(this);
            t3v.setText("Earth Spells: "+school.earth);
            t3v.setTextColor(Color.WHITE);
            t3v.setGravity(Gravity.CENTER);
            t3v.setTextSize(18);
            t3v.setClickable(true);
            tbrow1.addView(t3v);

            stk2.addView(tbrow1);
        }

        if (school.air>0) {
            TableRow tbrow1 = new TableRow(this);

            TextView t3v = new TextView(this);
            t3v.setText("Air Spells: "+school.air);
            t3v.setTextColor(Color.WHITE);
            t3v.setGravity(Gravity.CENTER);
            t3v.setTextSize(18);
            t3v.setClickable(true);
            tbrow1.addView(t3v);

            stk2.addView(tbrow1);
        }

        if (school.water>0) {
            TableRow tbrow1 = new TableRow(this);

            TextView t3v = new TextView(this);
            t3v.setText("Water Spells: "+school.water);
            t3v.setTextColor(Color.WHITE);
            t3v.setGravity(Gravity.CENTER);
            t3v.setTextSize(18);
            t3v.setClickable(true);
            tbrow1.addView(t3v);

            stk2.addView(tbrow1);
        }

        if (school.ishiken>0 && advantages.get("ISHIKEN-DO").is_bought) {
            TableRow tbrow1 = new TableRow(this);

            TextView t3v = new TextView(this);
            t3v.setText("Void Spells: "+school.ishiken);
            t3v.setGravity(Gravity.CENTER);
            t3v.setTextSize(18);
            t3v.setClickable(true);
            tbrow1.addView(t3v);

            stk2.addView(tbrow1);
        }

        TableRow tbrow2 = new TableRow(this);
        TextView helptext2= new TextView(this);

        helptext2.setText("Here's your current spells: ");
        helptext2.setSingleLine(false);
        helptext2.setMaxEms(8);
        helptext2.setTextColor(Color.WHITE);
        helptext2.setGravity(Gravity.CENTER);
        helptext2.setTextSize(28);

        tbrow2.addView(helptext2);
        stk2.addView(tbrow2);




        for (HashMap.Entry<String, Spell> entry : spells.entrySet()) {
            if (entry.getValue().is_learned) {
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

    private void onSpellClick(View view) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        temp = ((TextView) view).getText().toString();

        TextView nametext= popupView.findViewById(R.id.popuptext);
        nametext.setText("Name: "+spells.get(temp).name+
                "\n" +"Ring: "+spells.get(temp).ring+
                "\n" +"Range: "+spells.get(temp).range+
                "\n" +"Target: "+spells.get(temp).target+
                "\n" +"Duration: "+spells.get(temp).duration+
                "\n"+"Mastery Rank: "+String.valueOf(spells.get(temp).mastery_rank)+
                "\n"+spells.get(temp).content);
        nametext.setTextColor(Color.WHITE);
        nametext.setBackgroundColor(Color.BLACK);
        nametext.setMovementMethod(new ScrollingMovementMethod());
        nametext.setGravity(Gravity.CENTER);
        nametext.setSingleLine(false);
        nametext.setMaxEms(20);
        nametext.setTextSize(20);



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
        //This command is used to insert all the spells and their specific value inside the Bundle, for passing it to the next activity.

        myintent = new Intent(SpellsActivity.this, CustomShugenjaMenuActivity.class);

        extras.putSerializable("SPELLS",spells);
        extras.putSerializable("SHUGENJA_SCHOOL",school);

        myintent.putExtras(extras);

        startActivity(myintent);
    }

    private void onBuyClick(View view) {
        TableRow row= (TableRow) view.getParent();
        mytext = (TextView)row.getChildAt(0);

        current_spell= mytext.getText().toString();

        String temp=spells.get(current_spell).ring;

        if (temp.equals("FIRE")&&school.fire>0)
            if (!spells.get(current_spell).is_learned) {
                buySpell(current_spell,spells.get(current_spell));
                mytext.setTextColor(Color.GREEN);}
            else {sellSpell(current_spell,spells.get(current_spell));
                mytext.setTextColor(Color.WHITE);}
        else if (temp.equals("AIR")&&school.air>0)
            if (!spells.get(current_spell).is_learned) {
                buySpell(current_spell,spells.get(current_spell));
                mytext.setTextColor(Color.GREEN);}
            else {sellSpell(current_spell,spells.get(current_spell));
                mytext.setTextColor(Color.WHITE);}
        else if (temp.equals("EARTH")&&school.earth>0)
            if (!spells.get(current_spell).is_learned) {
                buySpell(current_spell,spells.get(current_spell));
                mytext.setTextColor(Color.GREEN);}
            else {sellSpell(current_spell,spells.get(current_spell));
                mytext.setTextColor(Color.WHITE);}
        else if (temp.equals("WATER")&&school.water>0)
            if (!spells.get(current_spell).is_learned) {
                buySpell(current_spell,spells.get(current_spell));
                mytext.setTextColor(Color.GREEN);}
            else {sellSpell(current_spell,spells.get(current_spell));
                mytext.setTextColor(Color.WHITE);}
        else if (temp.equals("VOID")&&school.ishiken>0)
            if (!spells.get(current_spell).is_learned) {
                buySpell(current_spell,spells.get(current_spell));
                mytext.setTextColor(Color.GREEN);}
            else {sellSpell(current_spell,spells.get(current_spell));
                mytext.setTextColor(Color.WHITE);}
        else  Toast.makeText(this, "You can't learn any more spells of that ring!", Toast.LENGTH_SHORT).show();

    }

    private void sellSpell(String current_spell, Spell spell) {
        spells.get(current_spell).is_learned=false;
        String temp=spell.ring;

        if (temp.equals("FIRE")) school.fire= school.fire +1;
        if (temp.equals("AIR")) school.air= school.air +1;
        if (temp.equals("EARTH")) school.earth= school.earth +1;
        if (temp.equals("WATER")) school.water= school.water +1;

        Toast.makeText(this, "Spell sold!", Toast.LENGTH_SHORT).show();
    }

    private void buySpell(String current_spell, Spell spell) {
        spells.get(current_spell).is_learned=true;
        String temp=spell.ring;

        if (temp.equals("FIRE")) school.fire= school.fire -1;
        if (temp.equals("AIR")) school.air= school.air -1;
        if (temp.equals("EARTH")) school.earth= school.earth -1;
        if (temp.equals("WATER")) school.water= school.water -1;

        Toast.makeText(this, "Spell bought!", Toast.LENGTH_SHORT).show();
    }


}