package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;


public class LoadActivity extends AppCompatActivity {

    PopupWindow popupWindow;
    Bundle extras;
    Intent myintent;
    HashMap<String,Advantage> advmap;
    HashMap<String,Disadvantage> dismap;
    HashMap<String,Spell> spellmap;
    HashMap<String,Armor> armors;
    HashMap<String, Weapon> weapons;
    HashMap<String,Integer> skills,traits;
    TableLayout stk;
    String[] dataskills,datatraits;

    private void init(){

        stk = findViewById(R.id.loadtable);

        String path = LoadActivity.this.getFilesDir().getAbsolutePath();
        File directory = new File(path);
        File[] files = directory.listFiles();
        if (files.length>0){
            for (int i = 0; i < files.length; i++)
            {
                // inflate the layout of the popup window
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View skillView = inflater.inflate(R.layout.tablerow_load, null);

                TextView tv2 = skillView.findViewById(R.id.loadview1);
                String temp= files[i].getName();
                temp=temp.replaceAll(".txt","");
                tv2.setText(temp);
                tv2.setTextColor(Color.BLACK);
                tv2.setTextSize(24);

                Button loadbutton= skillView.findViewById(R.id.loadbutton1);
                loadbutton.setText("Load");
                loadbutton.setOnClickListener(this::onLoadCLick);

                Button deletebutton= skillView.findViewById(R.id.loadbutton2);
                deletebutton.setText("Delete");
                deletebutton.setOnClickListener(this::onDeleteCLick);

                stk.addView(skillView);
            }
        }
    }

    private void onDeleteCLick(View view) {
        TableRow row= (TableRow) view.getParent();
        TextView mytext = (TextView)row.getChildAt(0);

        String current_char= mytext.getText().toString();

        System.out.println(deleteFile(current_char+".txt"));

        setContentView(R.layout.activity_load);
        init();
    }


    public void onLoadCLick(View view){
        TableRow row= (TableRow) view.getParent();
        TextView mytext = (TextView)row.getChildAt(0);

        String current_char= mytext.getText().toString();

        FileInputStream fis;
        try {
            fis = openFileInput(current_char+".txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String text;
            boolean spell_flag = false;
            while ((text = br.readLine()) != null) {

                if (text.contains("Skill:")){
                    text=text.replaceAll("Skill:","");
                    String[] tokens = text.split("  ");
                    skills.put(tokens[0],Integer.parseInt(tokens[1]));
                }
                if (text.contains("Trait:")){
                    text=text.replaceAll("Trait:","");
                    String[] tokens = text.split("  ");
                    traits.put(tokens[0],Integer.parseInt(tokens[1]));
                }
                if (text.contains("Character Name:  ")){
                    text=text.replaceAll("Character Name:  ","");
                    extras.putString("CHARACTER_NAME",text);
                }
                if (text.contains("Clan:  ")){
                    text=text.replaceAll("Clan:  ","");
                    extras.putString("CLAN",text);
                }
                if (text.contains("School:  ")) {
                    text = text.replaceAll("School:  ", "");
                    extras.putString("SCHOOL", text);
                    if (text.contains("Shugenja")){
                        readSpellData();
                        readSchoolData();
                        spell_flag = true;
                    }
                    readTechniqueData();
                }
                if (text.contains("Experience Points:  ")) {
                    text = text.replaceAll("Experience Points:  ", "");
                    extras.putInt("EXP_VALUE", Integer.parseInt(text));
                }
                if (text.contains("Advantage:")){
                    System.out.println(text);
                    text=text.replaceAll("Advantage:","");
                    System.out.println(text);
                    advmap.get(text).is_bought=true;
                }
                if (text.contains("Disadvantage:")){
                    text=text.replaceAll("Disadvantage:","");
                    String[] tokens = text.split("  ");
                    dismap.get(tokens[0]).is_bought=true;
                }
                if (text.contains("Spell:") && spell_flag== true){
                    text=text.replaceAll("Spell:","");
                    String[] tokens = text.split("  ");
                    spellmap.get(tokens[0]).is_learned=true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        extras.putSerializable("SKILLS_MAP",skills);
        extras.putSerializable("ADVANTAGES",advmap);
        extras.putSerializable("DISADVANTAGES",dismap);
        extras.putSerializable("SPELLS",spellmap);
        extras.putSerializable("ARMORS",armors);
        extras.putSerializable("WEAPONS",weapons);
        extras.putSerializable("TRAITS_MAP",traits);

        myintent = new Intent(LoadActivity.this, ViewSheetActivity.class);

        myintent.putExtras(extras);

        startActivity(myintent);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        extras= new Bundle();

        advmap=new HashMap<>();
        dismap=new HashMap<>();
        spellmap=new HashMap<>();
        armors=new HashMap<>();
        weapons=new HashMap<>();
        skills= new HashMap<>();
        traits= new HashMap<>();

        readAdvantageData();
        readDisadvantageData();
        readArmorData();
        readWeaponData();
        readSkillAndTraitsData();


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

                advmap.put(adv.name, adv);
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

                dismap.put(disadv.name, disadv);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void readSpellData() {
        InputStream is = getResources().openRawResource(R.raw.spells);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String line = "";

        try {
            while ((line = reader.readLine()) != null) {
                // Split the line into different tokens (using the comma as a separator).
                String[] tokens = line.split("    ");

                // Read the data and store it in the Spell object.
                Spell spell = new Spell();
                spell.name=tokens[0];
                spell.mastery_rank= Integer.parseInt(tokens[1]);
                spell.ring=tokens[2];
                spell.range=tokens[3];
                spell.target=tokens[4];
                spell.duration=tokens[5];
                spell.content=tokens[6];
                spell.is_learned=false;
                if (spell.ring.equals("ALL"))
                    spell.is_learned=true;

                spellmap.put(spell.name, spell);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void readSchoolData() {
        InputStream is = getResources().openRawResource(R.raw.schools);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String line = "";

        try {
            while ((line = reader.readLine()) != null) {
                // Split the line into different tokens (using the 4 spaces as a separator).
                String[] tokens = line.split("    ");

                // Read the data and store it in the Shugenja_School object.
                Shugenja_School school = new Shugenja_School();
                if (tokens[0].equals(extras.getString("SCHOOL"))) {

                    school.name=tokens[0];
                    System.out.println(tokens[0]);
                    school.earth = Integer.parseInt(tokens[1]);
                    school.fire = Integer.parseInt(tokens[3]);
                    school.water = Integer.parseInt(tokens[5]);
                    school.air = Integer.parseInt(tokens[7]);
                    school.ishiken=0;
                    extras.putSerializable("SHUGENJA_SCHOOL",school);
                }
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

        dataskills = getResources().getStringArray(R.array.Skills);
        datatraits= getResources().getStringArray(R.array.Traits);

        for (int i=0;i< dataskills.length; i++) {
            skills.put(dataskills[i],0);
        }

        for (int i=0;i< datatraits.length; i++) {
            traits.put(datatraits[i],2);
        }
    }

    private void readTechniqueData() {
        InputStream is = getResources().openRawResource(R.raw.school_techniques);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String line = "";

        try {
            while ((line = reader.readLine()) != null) {
                // Split the line into different tokens (using 4 spaces as a separator).
                String[] tokens = line.split("    ");

                // Read the data and store it in the object.
                if(tokens[0].equals(extras.getString("SCHOOL"))) {
                    HashMap<String, Technique> techniques = new HashMap<String, Technique>();
                    if (extras.getString("SCHOOL").contains("Shugenja")) {
                        Technique t= new Technique();
                        t.name=tokens[1];
                        t.content=tokens[2];
                        t.rank=1;
                        System.out.println(tokens[1]);
                        System.out.println(tokens[2]);
                        techniques.put(tokens[1],t);
                    } else {
                        for (int i = 1; i < tokens.length-1; i=i+3) {
                            Technique t= new Technique();
                            t.name=tokens[i];
                            t.content= tokens[i+2];
                            t.rank=i;
                            System.out.println(tokens[i]);
                            System.out.println(tokens[i+2]);
                            techniques.put(tokens[i],t);
                        }
                    }
                    extras.putSerializable("SCHOOL_TECHNIQUES",techniques);
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


}