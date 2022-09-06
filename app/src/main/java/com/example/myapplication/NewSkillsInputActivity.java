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
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import static android.view.ViewGroup.LayoutParams.FILL_PARENT;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class NewSkillsInputActivity extends AppCompatActivity {

    Bundle extras;
    Intent myintent;
    TextView mytext,exptext;
    String current_skill;
    String[] data;
    TableLayout stk;
    HashMap<String,Integer> skills;
    int exp,cost;
    ArrayList<Integer> idarray;


    public int increaseSkill(String key){

        int target = skills.get(key) + 1;

        cost=target;

        if ((exp - cost) >= 0){
            skills.put(key,target);
            exp= exp- cost;
            return target;

        }
        else {
            return target-1;
        }
    }

    public int decreaseSkill(String key){

        int target = skills.get(key) - 1;

        if (target >= 0){
            skills.put(key,target);
            exp= exp + target + 1;

            return target;

            }
            else return 0;
    }

    public void init() {

        stk = findViewById(R.id.schoolskillstable);

        for (HashMap.Entry<String, Integer> entry : skills.entrySet()) {
            if (!entry.getKey().contains(":")) {
                // inflate the layout of the popup window
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View skillView = inflater.inflate(R.layout.tablerow_skills, null);

                TextView t2v = skillView.findViewById(R.id.skillview1);
                t2v.setText(entry.getKey());
                t2v.setTextColor(Color.WHITE);
                t2v.setClickable(true);
                //t2v.setOnClickListener(this::onSkillClick);

                TextView t3v = skillView.findViewById(R.id.skillview2);
                t3v.setText(entry.getValue().toString());
                t3v.setTextColor(Color.WHITE);
                idarray.add(t3v.getId());

                Button plusbutton = skillView.findViewById(R.id.skillbutton1);
                plusbutton.setText("+");
                plusbutton.setOnClickListener(this::onPlusClick);


                Button minusbutton = skillView.findViewById(R.id.skillbutton2);
                minusbutton.setText("-");
                minusbutton.setOnClickListener(this::onMinusClick);

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_skills_input);

        extras = getIntent().getExtras();

        idarray= new ArrayList<>();
        skills= new HashMap<>();

        data= extras.getStringArray("ALL_SKILLS");
        skills= (HashMap) extras.getSerializable("SKILLS_MAP");
        exp=extras.getInt("EXP_VALUE");

        exptext=findViewById(R.id.textskillsexp);
        exptext.setTextColor(Color.WHITE);

        init();

        exptext.setText("Experience Points: "+exp);

    }

    public void onPlusClick(View view) {
        TableRow row= (TableRow) view.getParent();
        mytext = (TextView)row.getChildAt(0);

        current_skill= mytext.getText().toString();

        mytext = (TextView)row.getChildAt(1);

        int num=increaseSkill(current_skill);

       for (int i=0;i<idarray.size();i++){
           if (mytext.getId()==idarray.get(i)){
               mytext.setText(String.valueOf(num));
           }
        }
        exptext.setText("Experience Points: "+exp);

    }

    public void onMinusClick(View view) {
        TableRow row= (TableRow) view.getParent();
        mytext = (TextView)row.getChildAt(0);

        current_skill= mytext.getText().toString();

        mytext = (TextView)row.getChildAt(1);

        int num=decreaseSkill(current_skill);

        for (int i=0;i<idarray.size();i++){
            if (mytext.getId()==idarray.get(i)){
                mytext.setText(String.valueOf(num));
            }
        }
        exptext.setText("Experience Points: "+exp);
    }


    private void onHelpClick(View view) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        TextView helptext= popupView.findViewById(R.id.popuptext);
        helptext.setText("You can improve your Skills in this screen."+"\n"+"The cost of upgrading a Skill is equal to the level you're upgrading it to.");
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
        extras.putSerializable("SKILLS_MAP",skills);

        extras.putInt("EXP_VALUE",exp);

        if (!extras.getString("SCHOOL").contains("Shugenja")) myintent = new Intent(NewSkillsInputActivity.this, CustomMenuActivity.class);
        else myintent = new Intent(NewSkillsInputActivity.this, CustomShugenjaMenuActivity.class);

        myintent.putExtras(extras);

        startActivity(myintent);
    }


}