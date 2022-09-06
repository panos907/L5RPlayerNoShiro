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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TraitsActivity extends AppCompatActivity {

    Bundle extras;
    Intent myintent;
    TextView mytext,exptext;
    String current_trait;
    String[] data;
    TableLayout stk;
    HashMap<String,Integer> traits;
    int exp,cost;
    ArrayList<Integer> idarray;

    public int increaseTrait(String key){

        int target = traits.get(key) + 1;

        if (!key.equals("Void")) cost=target*4;
        else cost=target*6;

        if ((exp - cost) >= 0){
            traits.put(key,target);
            exp= exp - cost;
            return target;

        }
        else {
            return target-1;
        }
    }


    public int decreaseTrait(String key){

        int target = traits.get(key) - 1;

        if (!key.equals("Void")) cost=(target+1)*4;
        else cost=(target+1)*6;

        if (target >= 2){
            traits.put(key,target);
            exp= exp + cost;

            return target;

        }
        else return 2;
    }



    public void init() {
        stk = findViewById(R.id.traitstable);
        TableRow tbrow0 = new TableRow(this);
        TextView tv1 = new TextView(this);
        tv1.setText(" Trait ");
        tv1.setTextColor(Color.RED);
        tv1.setTextSize(24);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setTextSize(24);
        tv2.setText(" Value ");
        tv2.setTextColor(Color.RED);
        tbrow0.addView(tv2);

        stk.addView(tbrow0);
        for (HashMap.Entry<String, Integer> entry : traits.entrySet()) {
            TableRow tbrow = new TableRow(this);

            TextView t2v = new TextView(this);
            t2v.setText(entry.getKey());
            t2v.setTextColor(Color.RED);
            t2v.setGravity(Gravity.CENTER);
            t2v.setTextSize(18);
            tbrow.addView(t2v);

            TextView t3v = new TextView(this);
            t3v.setText(entry.getValue().toString());
            t3v.setTextColor(Color.RED);
            t3v.setGravity(Gravity.CENTER);
            tbrow.addView(t3v);
            idarray.add(t3v.getId());

            Button plusbutton= new Button(this);
            plusbutton.setText("+");
            plusbutton.setOnClickListener(this::onPlusClick);
            tbrow.addView(plusbutton);

            Button minusbutton= new Button(this);
            minusbutton.setText("-");
            minusbutton.setOnClickListener(this::onMinusClick);
            tbrow.addView(minusbutton);


            stk.addView(tbrow);
        }

        Button submitbutton= new Button(this);
        submitbutton.setText("Submit");
        submitbutton.setOnClickListener(this::onSubmitClick);
        stk.addView(submitbutton);

    }


    private void onSubmitClick(View view) {
        //This command is used to insert all the skills and their specific value inside the Bundle, for passing it to the next activity.
        extras.putSerializable("TRAITS_MAP",traits);

        extras.putInt("EXP_VALUE",exp);

        if (!extras.getString("SCHOOL").contains("Shugenja")) myintent = new Intent(TraitsActivity.this, CustomMenuActivity.class);
        else myintent = new Intent(TraitsActivity.this, CustomShugenjaMenuActivity.class);


        myintent.putExtras(extras);

        startActivity(myintent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traits);

        extras = getIntent().getExtras();

        traits= new HashMap<>();
        idarray= new ArrayList<>();

        data= extras.getStringArray("TRAITS");
        traits= (HashMap) extras.getSerializable("TRAITS_MAP");
        exp= extras.getInt("EXP_VALUE");

        exptext=findViewById(R.id.textexptraits);
        exptext.setTextColor(Color.WHITE);

        init();

        exptext.setText("Experience Points: "+exp);

    }

    public void onPlusClick(View view) {
        TableRow row= (TableRow) view.getParent();
        mytext = (TextView)row.getChildAt(0);

        current_trait= mytext.getText().toString();

        mytext = (TextView)row.getChildAt(1);

        int num=increaseTrait(current_trait);

        for (int i=0;i<idarray.size();i++){
            if (mytext.getId()==idarray.get(i)){
                mytext.setText(String.valueOf(num));
            }
        }
        exptext.setText("Experience Points: "+exp);
    }


    private void onMinusClick(View view) {
        TableRow row= (TableRow) view.getParent();
        mytext = (TextView)row.getChildAt(0);

        current_trait= mytext.getText().toString();

        mytext = (TextView)row.getChildAt(1);

        int num=decreaseTrait(current_trait);

        for (int i=0;i<idarray.size();i++){
            if (mytext.getId()==idarray.get(i)){
                mytext.setText(String.valueOf(num));
            }
        }
        exptext.setText("Experience Points: "+exp);
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

        helptext.setText("Fire Ring: Intelligence, Agility\n" + "Air Ring: Awareness,Reflexes"+"\n" +
                "Earth Ring: Stamina, Willpower" +"\n"+
        "Water Ring: Strength,Perception");
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
