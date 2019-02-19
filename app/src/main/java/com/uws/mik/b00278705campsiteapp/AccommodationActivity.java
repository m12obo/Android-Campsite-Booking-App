package com.uws.mik.b00278705campsiteapp;         // Package for Campsite App

/****************************************************************************
 *- B00278705 -
 *
 * A Caravan and camping park app.
 *
 * Created for 'COMP08019 Programming Native App Interaction module '
 * This is 'Assignment 1'
 *
 * Created by Michael Robinson - B00278705 -
 *
 * Declaration : I declare that the work submitted is my own unless otherwise stated.
 *
 * All trademarks and images are the property of their owners
 *
 ****************************************************************************
 *
 * AccommodationActivity.class
 *
 ****************************************************************************/

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;                                                      // Import all required classes.
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class AccommodationActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Button makeBooking, backButton;                                                         // Declare required variables
    private Intent intent;
    private String accomChosen;
    private TextView selectionText;
    private ImageView selectionImage;
    private Spinner accomInfo;

    private PackageManager packageManager;                                                          // Set up required items for safe use of intents.
    private List<ResolveInfo> activities;
    private boolean isIntentSafe;


    private static final String[] items = {"Our Accommodation", "Caravan","Cabin", "Tent",};        // Declare required constants
    private static final String[] descriptions =
            {
                    "Our accommodation consists of luxurious static caravans and log style cabins, " +
                            "and we also have our glamorous tents. Please use the menu above to get more details about" +
                            " each type of accommodation " ,


                    "These are our luxurious caravans , they are available in 4,6 or 8 sleeping capacities," +
                            "they are fully equipped with fridge , cooker , TV , Wi-Fi and central heating ," +
                            "cost per night for the caravan is £25.",

                    "These are our luxurious cabins , they are available in 4,6 or 8 sleeping capacities," +
                    "they are fully equipped with fridge , cooker , TV , Wi-Fi and central heating," +
                            "cost per night for the cabin is £45.",

                    "These are our glamorous tents , they are available in 2, 4, 6, or 8 sleeping capacities " +
                            "they are fully equipped with groundsheets, beds & duvets, rugs, table & chairs, " +
                            "Cost per night for the tent is £15."
            }; // end of descriptions

    @Override
    protected void onCreate(Bundle savedInstanceState) {                                            // Set up buttons and spinner when activity is created.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accommodation);

        makeBooking = (Button)findViewById(R.id.makeBookingButton);                                 //Set up buttons with listeners.
        makeBooking.setOnClickListener(this);
        backButton = (Button)findViewById(R.id.backButtonAcomm);
        backButton.setOnClickListener(this);

        selectionText=(TextView)findViewById(R.id.accomInfo);                                       //Set up the TextViews
        selectionImage=(ImageView)findViewById(R.id.accomImage);

        accomInfo=(Spinner)findViewById(R.id.spinner);
        accomInfo.setOnItemSelectedListener(this);                                                  //Set up the spinner and listener
        ArrayAdapter<String> aa=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,items);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accomInfo.setAdapter(aa);
    }// end onCreate


    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.makeBookingButton:
                Intent intentData = getIntent();                                                    // Get the data passed from the other activity
                intent = new Intent(this, MakeBookingActivity.class);                               // Create a new intent to start MakeBookingActivity
                intent.putExtra("chosenPark", intentData.getStringExtra("chosenPark"));             // gather all the data needed to be passed to the other activity
                intent.putExtra("accomChosen", accomChosen);
                intent.putExtra("bothChosen", true);
                //Toast.makeText(this, intentData.getStringExtra("chosenPark") + " " + accomChosen, Toast.LENGTH_SHORT).show(); //******TEST CODE********
                packageManager = getPackageManager();
                activities = packageManager.queryIntentActivities(intent, 0);                       // Adapted from lecture notes.
                isIntentSafe = activities.size() > 0;
                if (isIntentSafe) {                                                                 // Ensure Activity is safe to execute.
                    startActivity(intent);                                                          // Start the required activity
                }// end if
                    break;
            case R.id.backButtonAcomm:
                super.onBackPressed();                                                              // Code adapted from http://chrisrisner.com/31-Days-of-Android--Day-10-The-Back-Button/
                break;                                                                              // Returns to the previous activity.
        }// end switch

    }// end onClick;

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        selectionText.setText(descriptions[position]);                                              //Set the text to the item in the position in descriptions

        switch (position) {
            case 0: selectionImage.setImageResource(R.drawable.allaccomm);                          //Get the appropriate images and display them in the image view
                accomChosen = items[position];
                break;
            case 1: selectionImage.setImageResource(R.drawable.caravanparkwall);
                accomChosen = items[position];
                break;
            case 2: selectionImage.setImageResource(R.drawable.cabindrobudden);
                accomChosen = items[position];
                break;
            case 3: selectionImage.setImageResource(R.drawable.tentpintrest);
                accomChosen = items[position];
                break;
        }// end switch
    }// end onItemSelected


    public void onNothingSelected(AdapterView<?> parent) {
        //selection.setText("");                                                                    // Not used
    }// end onNothingSelected

}//end AccommodationActivity

