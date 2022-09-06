package com.example.myapplication;

import static com.example.myapplication.R.layout.popup_window;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;


public class  ClanSchoolActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText edt, edt2;
    String player_name, ge, character_name;
    String[] dataskills,datatraits;
    RadioGroup radioSexGroup;
    RadioButton radioSexButton;
    Spinner clan, school;
    Bundle extras;
    Intent myintent;
    // These two HashMaps are used to store the values of the character's Skills and Traits
    HashMap<String,Integer> skillmap,traitmap;

    // These 4 HashMaps are used to store the character's Spells,Advantages,Disadvantages and Equipment.
    HashMap<String,Advantage> advmap;
    HashMap<String,Disadvantage> dismap;
    HashMap<String,Spell> spellmap;
    HashMap<String,Armor> armors;
    HashMap<String, Weapon> weapons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clan_school);
        radioSexGroup = findViewById(R.id.radioGroup);
        edt = findViewById(R.id.playerName);
        edt2 = findViewById(R.id.charName);
        clan = findViewById(R.id.spinnerClan);
        school = findViewById(R.id.spinnerSchool);
        myintent = new Intent(ClanSchoolActivity.this, EXPInputActivity.class);
        extras= new Bundle();
        skillmap = new HashMap<>();
        traitmap= new HashMap<>();
        advmap= new HashMap<>();
        dismap= new HashMap<>();
        spellmap = new HashMap<>();
        armors= new HashMap<>();
        weapons= new HashMap<>();

        clan.setOnItemSelectedListener(this);
        school.setOnItemSelectedListener(this);

        //We prepare the clan and school spinners by initializing their properties and data for selection
        ArrayAdapter<CharSequence> clanadapter = ArrayAdapter.createFromResource(this, R.array.clans, android.R.layout.simple_spinner_item);
        clanadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        clan.setAdapter(clanadapter);


        readSkillAndTraitsData();
        readAdvantageData();
        readDisadvantageData();
        readSpellData();
        readArmorData();
        readWeaponData();

    }

    public void onButtonClick(android.view.View view) {
        //We prepare the user's input data to be passed to the next activity
        player_name = edt.getText().toString();
        character_name = edt2.getText().toString();

        int selected = radioSexGroup.getCheckedRadioButtonId();

        if (selected != -1 && !player_name.isEmpty() && !character_name.isEmpty()) {
            radioSexButton = findViewById(selected);
            ge = radioSexButton.getText().toString();

            extras.putString("GENDER",ge);
            extras.putString("PLAYER_NAME", player_name);
            extras.putString("CHARACTER_NAME", character_name);
            extras.putInt("EXP_VALUE",0);
            extras.putSerializable("TRAITS_MAP",traitmap);
            extras.putSerializable("SKILLS_MAP",skillmap);
            extras.putSerializable("ADVANTAGES",advmap);
            extras.putSerializable("DISADVANTAGES",dismap);
            extras.putSerializable("SPELLS",spellmap);
            extras.putSerializable("ARMORS",armors);
            extras.putSerializable("WEAPONS",weapons);
            extras.putStringArray("ALL_SKILLS",dataskills);
            extras.putStringArray("TRAITS",datatraits);

            if (extras.getString("SCHOOL").contains("Shugenja")) readShugenjaSchoolData();

            readTechniqueData();
            read_starting_skills_data();
            read_starting_trait_data();


            myintent.putExtras(extras);


            //And then we start the next activity
            startActivity(myintent);

        } else {
            //We ask the user to select an option for all fields before continuing
            Toast.makeText(this, "Please input all fields!", Toast.LENGTH_SHORT).show();
        }

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

        helptext.setText("Clan and School Screen\n" +
                "\n" +
                "In this screen, you must select your character's Clan and School.\n" +
                "\n" +
                "Consult the Core Rulebook in order to learn more about the options presented here.");
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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,long irrelevantint) {

        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)

        String temp;
        int myid;

        myid=parent.getId();

        ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);

        switch (myid) {
            case R.id.spinnerClan:

                temp = parent.getItemAtPosition(position).toString();

                ArrayAdapter<CharSequence> schooladapter = ArrayAdapter.createFromResource(this, R.array.clans, android.R.layout.simple_dropdown_item_1line);

                if (temp.equals("Crab")) {
                    schooladapter = ArrayAdapter.createFromResource(this, R.array.Crab_schools, android.R.layout.simple_spinner_item);
                } else if (temp.equals("Crane")) {
                    schooladapter = ArrayAdapter.createFromResource(this, R.array.Crane_schools, android.R.layout.simple_spinner_item);
                } else if (temp.equals("Mantis")) {
                    schooladapter = ArrayAdapter.createFromResource(this, R.array.Mantis_schools, android.R.layout.simple_spinner_item);
                } else if (temp.equals("Unicorn")) {
                    schooladapter = ArrayAdapter.createFromResource(this, R.array.Unicorn_schools, android.R.layout.simple_spinner_item);
                } else if (temp.equals("Dragon")) {
                    schooladapter = ArrayAdapter.createFromResource(this, R.array.Dragon_schools, android.R.layout.simple_spinner_item);
                } else if (temp.equals("Phoenix")) {
                    schooladapter = ArrayAdapter.createFromResource(this, R.array.Phoenix_schools, android.R.layout.simple_spinner_item);
                } else if (temp.equals("Scorpion")) {
                    schooladapter = ArrayAdapter.createFromResource(this, R.array.Scorpion_schools, android.R.layout.simple_spinner_item);
                } else if (temp.equals("Lion")) {
                    schooladapter = ArrayAdapter.createFromResource(this, R.array.Lion_schools, android.R.layout.simple_spinner_item);
                } else if (temp.equals("Spider")) {
                    schooladapter = ArrayAdapter.createFromResource(this, R.array.Spider_schools, android.R.layout.simple_spinner_item);
                }


                schooladapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                school.setAdapter(schooladapter);


                if (temp!=null){
                    extras.putString("CLAN", temp);
                }
                break;

            case R.id.spinnerSchool:

                temp = parent.getItemAtPosition(position).toString();

                if (temp!=null) {
                    extras.putString("SCHOOL", temp);
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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

    private void readShugenjaSchoolData() {
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

                String type=tokens[2];

                // Read the data and store it in the Weapon object.
                if (type.contains("Kyujutsu")){
                    //If the weapon uses Kyujutsu, it's a bow
                    //That means we use the roll field for its STR rating and the keep field for it's range.
                    Weapon bow = new Weapon();
                    bow.skill=tokens[2];
                    bow.name=tokens[0];
                    bow.roll=Integer.parseInt(tokens[1].substring(0,1));
                    bow.keep= Integer.parseInt(tokens[1].substring(2, tokens[1].length()));
                    bow.keywords= new String[2];
                    bow.keywords[0]=tokens[3];
                    bow.keywords[1]=tokens[4];
                    bow.description=tokens[5];
                    bow.is_bought=false;

                    weapons.put(bow.name, bow);
                }
                else{
                    Weapon meleeWeapon = new Weapon();
                    meleeWeapon.skill=tokens[2];
                    meleeWeapon.name=tokens[0];
                    meleeWeapon.roll=Integer.parseInt(tokens[1].substring(0,1));
                    meleeWeapon.keep= Integer.parseInt(tokens[1].substring(2,3));
                    meleeWeapon.keywords= new String[2];
                    meleeWeapon.keywords[0]=tokens[3];
                    meleeWeapon.keywords[1]=tokens[4];
                    meleeWeapon.description=tokens[5];
                    meleeWeapon.is_bought=false;

                    weapons.put(meleeWeapon.name, meleeWeapon);
                }

            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void readSkillAndTraitsData(){

        dataskills= getResources().getStringArray(R.array.Skills);
        datatraits= getResources().getStringArray(R.array.Traits);

        for (int i=0;i< dataskills.length; i++) {
            skillmap.put(dataskills[i],0);
        }

        for (int i=0;i< datatraits.length; i++) {
            traitmap.put(datatraits[i],2);
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
                        techniques.put(tokens[1],t);
                    } else {
                        int counter=1;
                        for (int i = 1; i < tokens.length-1; i=i+3) {
                            Technique t= new Technique();
                            t.name=tokens[i];
                            t.content= tokens[i+2];
                            t.rank=counter;
                            counter++;
                            techniques.put(tokens[i],t);
                            System.out.println(t.name);
                            System.out.println(t.rank);
                        }
                    }
                    extras.putSerializable("SCHOOL_TECHNIQUES",techniques);
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


    public void read_starting_trait_data(){

        InputStream is = getResources().openRawResource(R.raw.starting_trait_bonus);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String line = "";
        try {
            while ((line = reader.readLine()) != null) {
                // Split the line into different tokens (using the comma as a separator).
                String[] tokens = line.split("    ");
                String key=tokens[0];
                if(tokens[0].equals(extras.getString("SCHOOL"))) {
                    // Read the data and increase the traits accordingly
                    int target1 = traitmap.get(tokens[1]) + 1;
                    traitmap.put(tokens[1], target1);
                    int target2 = traitmap.get(tokens[2]) + 1;
                    traitmap.put(tokens[2], target2);
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void read_starting_skills_data(){
        InputStream is = getResources().openRawResource(R.raw.starting_skills_bonus);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String line = "";
        try {

            if (extras.getInt("EXP_VALUE")<=0) {
                while ((line = reader.readLine()) != null) {
                    // Split the line into different tokens (using 4 spaces as a separator).
                    String[] tokens = line.split("    ");
                    // Read the data and store it in the Spell object.
                    if (tokens[0].equals(extras.getString("SCHOOL"))) {
                        for (int i = 1; i < tokens.length; i++) {
                            System.out.println(tokens[i]);
                            // Read the data and increase the traits accordingly
                            if (!tokens[i].contains("(") && !tokens[i].contains(")") && !tokens[i].contains("any")) {
                                int target = skillmap.get(tokens[i]) + 1;
                                skillmap.put(tokens[i], target);
                            }
                        }
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}