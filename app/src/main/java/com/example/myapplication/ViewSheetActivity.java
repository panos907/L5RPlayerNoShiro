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

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static com.example.myapplication.R.layout.popup_window;

public class ViewSheetActivity extends AppCompatActivity {


    TableLayout stk;
    Bundle extras;
    Intent myintent;
    HashMap<String,Integer> skills,traits;
    HashMap<String,Spell> spells;
    HashMap<String,Armor> armors;
    HashMap<String,Advantage> advantages;
    HashMap<String,Disadvantage> disadvantages;
    HashMap<String, Weapon> weapons;
    HashMap<String,Technique> techniques;
    int earth,fire,air,water,ishiken,rank;
    String[] dataskills,datatraits;

    public void calculateRings(){
        ishiken=traits.get("Void");
        fire= Math.min(traits.get("Intelligence"),traits.get("Agility"));
        air= Math.min(traits.get("Awareness"),traits.get("Reflexes"));
        earth= Math.min(traits.get("Willpower"),traits.get("Stamina"));
        water= Math.min(traits.get("Strength"),traits.get("Perception"));
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

    public void init() {
        stk = findViewById(R.id.viewsheetable);

        TableRow tbrow00 = new TableRow(this);
        TextView tvc = new TextView(this);
        tvc.setText("Character Name: "+ extras.getString("CHARACTER_NAME"));
        tvc.setTextColor(Color.BLACK);
        tvc.setTextSize(24);
        tbrow00.addView(tvc);
        stk.addView(tbrow00);

        TableRow tbrow0 = new TableRow(this);
        TextView tv12 = new TextView(this);
        tv12.setText("Air: "+ air);
        tv12.setTextColor(Color.BLACK);
        tv12.setTextSize(24);
        tbrow0.addView(tv12);
        stk.addView(tbrow0);

        TableRow tbrow2 = new TableRow(this);
        TextView tv13 = new TextView(this);
        tv13.setText("Fire: "+fire);
        tv13.setTextColor(Color.BLACK);
        tv13.setTextSize(24);
        tbrow2.addView(tv13);
        stk.addView(tbrow2);

        TableRow tbrow3 = new TableRow(this);
        TextView tv14 = new TextView(this);
        tv14.setText("Earth: "+earth);
        tv14.setTextColor(Color.BLACK);
        tv14.setTextSize(24);
        tbrow3.addView(tv14);
        stk.addView(tbrow3);

        TableRow tbrow4 = new TableRow(this);
        TextView tv15 = new TextView(this);
        tv15.setText("Water: "+water);
        tv15.setTextColor(Color.BLACK);
        tv15.setTextSize(24);
        tbrow4.addView(tv15);
        stk.addView(tbrow4);

        TableRow tbrow5 = new TableRow(this);
        TextView tv16 = new TextView(this);
        tv16.setText("Void: "+ishiken);
        tv16.setTextColor(Color.BLACK);
        tv16.setTextSize(24);
        tbrow5.addView(tv16);
        stk.addView(tbrow5);


        for (HashMap.Entry<String, Integer> entry : traits.entrySet()) {

            if (entry.getValue() >= 1) {
                TableRow tbrow = new TableRow(this);
                TextView t2v = new TextView(this);
                t2v.setText(entry.getKey());
                t2v.setTextColor(Color.BLACK);
                t2v.setGravity(Gravity.LEFT);
                t2v.setTextSize(18);
                t2v.setClickable(true);
                tbrow.addView(t2v);

                TextView t3v = new TextView(this);
                t3v.setText(entry.getValue().toString());
                t3v.setTextColor(Color.BLACK);
                t3v.setGravity(Gravity.CENTER);
                t3v.setTextSize(18);
                tbrow.addView(t3v);

                stk.addView(tbrow);
            }
        }


        TableRow tbrow1 = new TableRow(this);
        TextView tv1 = new TextView(this);
        tv1.setText("Skill");
        tv1.setTextColor(Color.BLACK);
        tv1.setTextSize(24);
        tbrow1.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setTextSize(24);
        tv2.setText("Value");
        tv2.setTextColor(Color.BLACK);
        tbrow1.addView(tv2);

        stk.addView(tbrow1);


        for (HashMap.Entry<String, Integer> entry : skills.entrySet()) {

            if (entry.getValue() >= 1) {
                TableRow tbrow = new TableRow(this);
                TextView t2v = new TextView(this);
                t2v.setText(entry.getKey());
                t2v.setTextColor(Color.BLACK);
                t2v.setGravity(Gravity.LEFT);
                t2v.setTextSize(18);
                t2v.setClickable(true);
                tbrow.addView(t2v);

                TextView t3v = new TextView(this);
                t3v.setText(entry.getValue().toString());
                t3v.setTextColor(Color.BLACK);
                t3v.setGravity(Gravity.CENTER);
                t3v.setTextSize(18);
                tbrow.addView(t3v);

                stk.addView(tbrow);
            }
        }



        TextView tv4 = new TextView(this);
        tv4.setText("");
        tv4.setTextColor(Color.BLACK);
        tv4.setTextSize(24);
        stk.addView(tv4);

        TextView tv3 = new TextView(this);
        tv3.setText("Advantages");
        tv3.setTextColor(Color.BLACK);
        tv3.setTextSize(24);
        stk.addView(tv3);

        TextView tv5 = new TextView(this);
        tv5.setText("");
        tv5.setTextColor(Color.BLACK);
        tv5.setTextSize(24);
        stk.addView(tv5);

        for (Map.Entry mapElement : advantages.entrySet()) {
            String key = (String)mapElement.getKey();

            TableRow tbrow = new TableRow(this);

            if (advantages.get(key).is_bought) {
                TextView t2v = new TextView(this);
                t2v.setText(key);
                t2v.setTextColor(Color.BLACK);
                t2v.setGravity(Gravity.LEFT);
                t2v.setTextSize(18);
                t2v.setClickable(true);
                t2v.setOnClickListener(this::onAdvClick);
                tbrow.addView(t2v);

                stk.addView(tbrow);
            }
        }

        TextView tv6 = new TextView(this);
        tv6.setText("");
        tv6.setTextColor(Color.BLACK);
        tv6.setTextSize(24);
        stk.addView(tv6);

        TextView tv7 = new TextView(this);
        tv7.setText("Disadvantages");
        tv7.setTextColor(Color.BLACK);
        tv7.setTextSize(24);
        stk.addView(tv7);

        TextView tv8 = new TextView(this);
        tv8.setText("");
        tv8.setTextColor(Color.BLACK);
        tv8.setTextSize(24);
        stk.addView(tv8);

        for (Map.Entry mapElement : disadvantages.entrySet()) {
            String key = (String)mapElement.getKey();

            TableRow tbrow = new TableRow(this);

            if (disadvantages.get(key).is_bought) {
                TextView t2v = new TextView(this);
                t2v.setText(key);
                t2v.setTextColor(Color.BLACK);
                t2v.setGravity(Gravity.LEFT);
                t2v.setClickable(true);
                t2v.setOnClickListener(this::onDisadvClick);
                t2v.setTextSize(18);
                t2v.setClickable(true);
                tbrow.addView(t2v);

                stk.addView(tbrow);
            }
        }

        if (extras.getString("SCHOOL").contains("Shugenja")) {

            TextView tv17 = new TextView(this);
            tv17.setText("");
            tv17.setTextColor(Color.BLACK);
            tv17.setTextSize(24);
            stk.addView(tv17);

            TextView tv18 = new TextView(this);
            tv18.setText("Spells");
            tv18.setTextColor(Color.BLACK);
            tv18.setTextSize(24);
            stk.addView(tv18);

            TextView tv19 = new TextView(this);
            tv19.setText("");
            tv19.setTextColor(Color.BLACK);
            tv19.setTextSize(24);
            stk.addView(tv19);

            for (Map.Entry mapElement : spells.entrySet()) {
                String key = (String) mapElement.getKey();

                TableRow tbrow = new TableRow(this);

                if (spells.get(key).is_learned) {
                    TextView t2v = new TextView(this);
                    t2v.setText(key);
                    t2v.setTextColor(Color.BLACK);
                    t2v.setGravity(Gravity.LEFT);
                    t2v.setTextSize(18);
                    t2v.setClickable(true);
                    t2v.setOnClickListener(this::onSpellClick);
                    t2v.setClickable(true);
                    tbrow.addView(t2v);

                    stk.addView(tbrow);
                }
            }
        }
        else{


            TextView tv9 = new TextView(this);
            tv9.setText("");
            tv9.setTextColor(Color.BLACK);
            tv9.setTextSize(24);
            stk.addView(tv9);

            TextView tv10 = new TextView(this);
            tv10.setText("Equipment");
            tv10.setTextColor(Color.BLACK);
            tv10.setTextSize(24);
            stk.addView(tv10);

            TextView tv11 = new TextView(this);
            tv11.setText("");
            tv11.setTextColor(Color.BLACK);
            tv11.setTextSize(24);
            stk.addView(tv11);

            for (Map.Entry mapElement : armors.entrySet()) {
                String key = (String)mapElement.getKey();

                TableRow tbrow = new TableRow(this);

                if (armors.get(key).is_bought) {
                    TextView t2v = new TextView(this);
                    t2v.setText(key);
                    t2v.setTextColor(Color.BLACK);
                    t2v.setGravity(Gravity.LEFT);
                    t2v.setTextSize(18);
                    t2v.setClickable(true);
                    tbrow.addView(t2v);

                    stk.addView(tbrow);
                }
            }

            for (Map.Entry mapElement : weapons.entrySet()) {
                String key = (String)mapElement.getKey();

                TableRow tbrow = new TableRow(this);

                if (weapons.get(key).is_bought) {
                    TextView t2v = new TextView(this);
                    t2v.setText(key);
                    t2v.setTextColor(Color.BLACK);
                    t2v.setGravity(Gravity.LEFT);
                    t2v.setTextSize(18);
                    t2v.setClickable(true);
                    tbrow.addView(t2v);

                    stk.addView(tbrow);
                }
            }
        }

        TextView tv17 = new TextView(this);
        tv17.setText("");
        tv17.setTextColor(Color.BLACK);
        tv17.setTextSize(24);
        stk.addView(tv17);

        TextView tv18 = new TextView(this);
        tv18.setText("Techniques");
        tv18.setTextColor(Color.BLACK);
        tv18.setTextSize(24);
        stk.addView(tv18);

        TextView tv19 = new TextView(this);
        tv19.setText("");
        tv19.setTextColor(Color.BLACK);
        tv19.setTextSize(24);
        stk.addView(tv19);

        for (Map.Entry mapElement : techniques.entrySet() ) {

            String key = (String) mapElement.getKey();

            if (techniques.get(key).rank<=rank){

            TableRow tbrow = new TableRow(this);

            TextView t2v = new TextView(this);
                t2v.setText(key);
                t2v.setTextColor(Color.BLACK);
                t2v.setGravity(Gravity.LEFT);
                t2v.setTextSize(18);
                t2v.setClickable(true);
                t2v.setOnClickListener(this::onTechniqueClick);
                tbrow.addView(t2v);

                stk.addView(tbrow);
            }

        }


        Button savebutton= new Button(this);
        savebutton.setText("Save");
        savebutton.setOnClickListener(this::onSaveClick);
        stk.addView(savebutton);

        Button editbutton= new Button(this);
        editbutton.setText("Edit");
        editbutton.setOnClickListener(this::onEditClick);
        stk.addView(editbutton);

        Button loadbutton= new Button(this);
        loadbutton.setText("Home");
        loadbutton.setOnClickListener(this::onHomeClick);
        stk.addView(loadbutton);

        Button expbutton= new Button(this);
        expbutton.setText("Add EXP");
        expbutton.setOnClickListener(this::onAddEXPClick);
        stk.addView(expbutton);

        Button helpbutton= new Button(this);
        helpbutton.setText("Help");
        helpbutton.setOnClickListener(this::onHelpClick);
        stk.addView(helpbutton);
    }

    private void onDisadvClick(View view) {

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

        helptext.setText(disadvantages.get(temp).name+"\n"+disadvantages.get(temp).content);
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

    private void onAdvClick(View view) {
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

    private void onSpellClick(View view) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        String temp = ((TextView) view).getText().toString();

        TextView nametext= popupView.findViewById(R.id.popuptext);
        nametext.setText("Name: "+spells.get(temp).name+
                "\n" +"Ring: "+spells.get(temp).ring+
                "\n" +"Range: "+spells.get(temp).range+
                "\n" +"Target: "+spells.get(temp).target+
                "\n" +"Duration: "+spells.get(temp).duration+
                "\n"+"Mastery Rank: "+String.valueOf(spells.get(temp).mastery_rank)+
                "\n"+spells.get(temp).content);
        nametext.setTextColor(Color.WHITE);
        nametext.setMovementMethod(new ScrollingMovementMethod());
        nametext.setBackgroundColor(Color.BLACK);
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

    private void onTechniqueClick(View view) {

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

        helptext.setText(techniques.get(temp).name+"\n"+techniques.get(temp).content);
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


    private void onAddEXPClick(View view) {
       myintent = new Intent(ViewSheetActivity.this, EXPInputActivity.class);

       myintent.putExtras(extras);

       startActivity(myintent);
    }

    private void onHomeClick(View view) {

        myintent = new Intent(ViewSheetActivity.this, MainActivity.class);

        startActivity(myintent);

    }

    private void onEditClick(View view) {

        if (!extras.getString("SCHOOL").contains("Shugenja")) myintent = new Intent(ViewSheetActivity.this, CustomMenuActivity.class);
        else myintent = new Intent(ViewSheetActivity.this, CustomShugenjaMenuActivity.class);

        myintent.putExtras(extras);

        startActivity(myintent);
    }

    private void onHelpClick(View view) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(popup_window,null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        TextView helptext= popupView.findViewById(R.id.popuptext);

        helptext.setText("View Sheet Screen\n" +
                "\n" +
                "In this screen, you can view your final character sheet.\n" +
                "\n" +
                "Click on an Advantage, Disadvantage, Spell, or Equipment, to view more info about it.\n");
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


    public void onSaveClick(View view) {

        String name=extras.getString("CHARACTER_NAME")+".txt";
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(name, MODE_PRIVATE);
            PrintWriter pw = new PrintWriter(fos);

            pw.println("Character Name:  "+extras.getString("CHARACTER_NAME"));
            pw.println("Clan:  "+extras.getString("CLAN"));
            pw.println("School:  "+extras.getString("SCHOOL"));
            pw.println("Experience Points:  "+extras.getInt("EXP_VALUE"));


            for (HashMap.Entry<String, Integer> m : skills.entrySet()) {
                if (m.getValue()>0)
                    pw.println("Skill:"+m.getKey() + "  " + m.getValue());
            }

            for (HashMap.Entry<String, Integer> m : traits.entrySet()) {
                if (m.getValue()>0)
                    pw.println("Trait:"+m.getKey() + "  " + m.getValue());
            }

            for (HashMap.Entry<String, Advantage> m : advantages.entrySet()) {
                if (m.getValue().is_bought)
                    pw.println("Advantage:"+m.getKey());
            }

            for (HashMap.Entry<String, Disadvantage> m : disadvantages.entrySet()) {
                if (m.getValue().is_bought)
                    pw.println("Disadvantage:"+m.getKey());
            }

            for (HashMap.Entry<String, Spell> m : spells.entrySet()) {
                if (m.getValue().is_learned)
                    pw.println("Spell:"+m.getKey());
            }

            for (HashMap.Entry<String, Armor> m : armors.entrySet()) {
                if (m.getValue().is_bought)
                    pw.println("Armor:"+m.getKey()+ m.getValue().name);
            }

            for (HashMap.Entry<String, Weapon> m : weapons.entrySet()) {
                if (m.getValue().is_bought)
                    pw.println("Weapon:"+m.getKey()+ m.getValue().name);
            }

            pw.flush();
            pw.close();
            fos.close();

            Toast.makeText(this, "Character saved!", Toast.LENGTH_SHORT).show();
    }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sheet);

        advantages= new HashMap<>();
        disadvantages= new HashMap<>();
        skills= new HashMap<>();
        traits=new HashMap<>();
        spells= new HashMap<>();
        armors= new HashMap<>();
        weapons= new HashMap<>();
        techniques= new HashMap<>();

        fire=0;
        air=0;
        water=0;
        earth=0;
        ishiken=0;

        readAdvantageData();
        readDisadvantageData();
        readArmorData();
        readWeaponData();
        readSkillAndTraitsData();


        extras= getIntent().getExtras();

        advantages= (HashMap<String, Advantage>) extras.getSerializable("ADVANTAGES");
        disadvantages= (HashMap<String, Disadvantage>) extras.getSerializable("DISADVANTAGES");
        skills= (HashMap<String,Integer>) extras.getSerializable("SKILLS_MAP");
        traits=(HashMap<String, Integer>)  extras.getSerializable("TRAITS_MAP");
        spells= (HashMap<String, Spell>) extras.getSerializable("SPELLS");
        armors= (HashMap<String, Armor>) extras.getSerializable("ARMORS");
        weapons= (HashMap<String, Weapon>) extras.getSerializable("WEAPONS");
        techniques= (HashMap<String, Technique>) extras.getSerializable("SCHOOL_TECHNIQUES");

        calculateRings();
        calculateInsightRank();

        init();



    }

    private void readAdvantageData() {
        InputStream is = getResources().openRawResource(R.raw.advantages);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String line = "";

        try {
            while ((line = reader.readLine()) != null) {
                // Split the line into different tokens (using the comma as a separator).
                String[] tokens = line.split("    ");

                // Read the data and store it in the Advantage object.
                Advantage adv = new Advantage();
                adv.name=tokens[0];
                adv.cost= Integer.parseInt(tokens[1]);
                adv.content=tokens[2];
                adv.is_bought=false;

                advantages.put(adv.name, adv);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void readDisadvantageData() {
        InputStream is = getResources().openRawResource(R.raw.disadvantages);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String line = "";

        try {
            while ((line = reader.readLine()) != null) {
                // Split the line into different tokens (using the comma as a separator).
                String[] tokens = line.split("    ");

                // Read the data and store it in the Disadvantage object.
                Disadvantage disadv = new Disadvantage();
                disadv.name=tokens[0];
                disadv.exp= Integer.parseInt(tokens[1]);
                disadv.content=tokens[2];
                disadv.is_bought=false;

                disadvantages.put(disadv.name, disadv);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void readArmorData() {
        InputStream is = getResources().openRawResource(R.raw.armor);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String line = "";

        try {
            while ((line = reader.readLine()) != null) {
                // Split the line into different tokens (using the comma as a separator).
                String[] tokens = line.split("    ");

                // Read the data and store it in the Armor object.
                Armor armor = new Armor();
                armor.name=tokens[0];
                armor.reduction=Integer.parseInt(tokens[1]);
                armor.armor_tn= Integer.parseInt(tokens[2]);
                armor.description=tokens[3];
                armor.is_bought=false;

                armors.put(armor.name, armor);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void readWeaponData() {
        InputStream is = getResources().openRawResource(R.raw.weapons);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String line = "";

        try {
            while ((line = reader.readLine()) != null) {
                // Split the line into different tokens (using the comma as a separator).
                String[] tokens = line.split("    ");

                // Read the data and store it in the Weapon object.
                Weapon meleeWeapon = new Weapon();
                meleeWeapon.name=tokens[0];
                meleeWeapon.roll=Integer.parseInt(tokens[1].substring(0,1));
                meleeWeapon.keep= Integer.parseInt(tokens[1].substring(2,3));
                meleeWeapon.skill=tokens[2];
                meleeWeapon.description=tokens[3];
                meleeWeapon.is_bought=false;

                weapons.put(meleeWeapon.name, meleeWeapon);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void readSkillAndTraitsData(){

        dataskills= getResources().getStringArray(R.array.Skills);
        datatraits= getResources().getStringArray(R.array.Traits);

        for (int i=0;i< dataskills.length; i++) {
            skills.put(dataskills[i],0);
        }

        for (int i=0;i< datatraits.length; i++) {
            traits.put(datatraits[i],2);
        }
    }

}