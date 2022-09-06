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

import java.util.HashMap;

import static com.example.myapplication.R.layout.popup_window;

public class DisadvantagesActivity extends AppCompatActivity {

    Bundle extras;
    Intent myintent;
    String temp;
    TextView exptext,mytext;
    String current_disadv;
    TableLayout stk;
    HashMap<String,Disadvantage> disadvantages;
    int exp,cost;


    public boolean buyDisadvantage(String key, Disadvantage disadv){

        cost = disadv.exp;

        disadv.is_bought=true;
        disadvantages.put(key,disadv);
        exp= exp + cost;
        return true;
    }


    private boolean sellDisadvantage(String key, Disadvantage disadv) {
            cost = disadv.exp;
            exp = exp - cost;
            disadv.is_bought = false;
            disadvantages.put(key, disadv);
            return true;
    }


    public void init() {
        stk = findViewById(R.id.disadvtable);

        for (HashMap.Entry<String, Disadvantage> entry : disadvantages.entrySet()) {

            // inflate the layout of the popup window
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View skillView = inflater.inflate(R.layout.tablerow_rest, null);

            TextView t2v = skillView.findViewById(R.id.myview1);
            t2v.setText(entry.getKey());
            t2v.setTextColor(Color.WHITE);
            t2v.setClickable(true);
            t2v.setOnClickListener(this::onDisadvClick);

            TextView t3v = skillView.findViewById(R.id.myview2);
            if (entry.getValue().is_bought) t3v.setTextColor(Color.GREEN);
            else t3v.setTextColor(Color.WHITE);
            t3v.setText(String.valueOf(entry.getValue().exp));

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
        setContentView(R.layout.activity_disadvantages);

        extras = getIntent().getExtras();
        disadvantages= new HashMap<>();

        disadvantages= (HashMap) extras.getSerializable("DISADVANTAGES");
        exp=extras.getInt("EXP_VALUE");

        exptext=findViewById(R.id.textdisadvexp);

        init();

        exptext.setText("Experience Points: "+exp);

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

            temp = ((TextView) view).getText().toString();

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

            helptext.setText("You can buy Disadvantages in this screen."+"\n"+"Click on a Disadvantage to learn more about it.");
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

            if (!extras.getString("SCHOOL").contains("Shugenja")) myintent = new Intent(DisadvantagesActivity.this, CustomMenuActivity.class);
            else myintent = new Intent(DisadvantagesActivity.this, CustomShugenjaMenuActivity.class);


            myintent.putExtras(extras);

            startActivity(myintent);
        }

    //This method is called whenever the "Buy" button is clicked.
    private void onBuyClick(View view) {
        TableRow row= (TableRow) view.getParent();
        mytext = (TextView)row.getChildAt(0);

        current_disadv= mytext.getText().toString();

        if (!disadvantages.get(current_disadv).is_bought) {
            boolean flag= buyDisadvantage(current_disadv, disadvantages.get(current_disadv));
            if (flag==true) mytext.setTextColor(Color.GREEN);
        }
        else {
            boolean flag= sellDisadvantage(current_disadv, disadvantages.get(current_disadv));
            if (flag==true) mytext.setTextColor(Color.WHITE);
        }
        exptext.setText("Experience Points: "+exp);
        }


    }



