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
 * ConfirmationActivity.class
 *
 ****************************************************************************/

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;                                                                           // Import all required classes.
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

public class ConfirmationActivity extends AppCompatActivity implements View.OnClickListener , CompoundButton.OnCheckedChangeListener {

                                                                                                    // Declare required variables
    private static final int CARAVAN_CHARGE = 25, CABIN_CHARGE = 45, TENT_CHARGE = 15, PET_CHARGE = 3;     // Set up constant for charges so can be changed if prices rise
    private Button confirmButton, goBackButton;
    private TextView  disabledAccessText, park , accom , dateFrom, dateTo , pet, under16Text,under5Text, sleepingCapacity, totalCostText;
    private CheckBox disabled;
    private float accomCharge, totalCost = 0;                                                       // Considered using double but sharedPreferences doest accept a double value.
    private SharedPreferences campsiteSharedPreferences;                                            // Used for storing persistent data
    private PackageManager packageManager;                                                          // set up required items for safe use of intents.
    private List<ResolveInfo> activities;
    private boolean isIntentSafe;
    private Intent intent , intentData;                                                             // Use to pass data between activities
    private String chosenPark , chosenAccom ,chosenFromDate , chosenToDate , under16 , under5 , disabledChosen = "No" , petsChosen;
    private int numberOfNights,sleepingCap;



    protected void onCreate(Bundle savedInstanceState) {                                            // Set up buttons and spinner when activity is created.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        campsiteSharedPreferences = getSharedPreferences("campsitePrefFile", MainActivity.MODE_PRIVATE); // Create shared preference object.

        intentData = getIntent();                                                                   // Get data passed to this activity from the others
        chosenPark = intentData.getStringExtra("chosenPark");
        chosenAccom = intentData.getStringExtra("chosenAccom");
        chosenFromDate = intentData.getStringExtra("chosenFromDate");
        chosenToDate = intentData.getStringExtra("chosenToDate");
        under16 = intentData.getStringExtra("under16");
        under5 = intentData.getStringExtra("under5");
        numberOfNights = intentData.getIntExtra("numberOfNights", 0);
        sleepingCap = intentData.getIntExtra("sleepingCap", 4);
        Boolean petChecked = intentData.getBooleanExtra("chosenPet", false);

        confirmButton = (Button)findViewById(R.id.confirmButton);                                   //Set up buttons with listeners.
        confirmButton.setOnClickListener(this);
        goBackButton = (Button)findViewById(R.id.backButtonConfrim);
        goBackButton.setOnClickListener(this);

        disabled = (CheckBox)findViewById(R.id.disAccessCheckBox);                                  //Set up checkbox with listener.
        disabled.setOnCheckedChangeListener(this);

        disabledAccessText = (TextView)findViewById(R.id.disabledAccessText);                       //Set up the TextViews and populate with data from shared preferences
        park = (TextView)findViewById(R.id.parkSelectedText);
        park.setText(chosenPark);
        accom = (TextView)findViewById(R.id.accomSelectedText);
        accom.setText("Accommodation chosen : " + chosenAccom);
        sleepingCapacity = (TextView)findViewById(R.id.capacityText);
        sleepingCapacity.setText("You have chosen a sleeping capacity of " + sleepingCap);
        dateFrom = (TextView)findViewById(R.id.dateFromText);
        dateFrom.setText("From Date : " + chosenFromDate);
        dateTo = (TextView)findViewById(R.id.dateToText);
        dateTo.setText("To Date : " + chosenToDate);
        pet = (TextView)findViewById(R.id.petView);
        totalCostText = (TextView)findViewById(R.id.totalCostText);
        under16Text = (TextView) findViewById(R.id.under16Text);
        under5Text = (TextView) findViewById(R.id.under5Text);
        under16Text.setText("Number Of Under 16s : " + under16);
        under5Text.setText("Number Of Under 5s : " + under5);


        if(petChecked) {
            totalCost += PET_CHARGE;                                                                // Add to total cost if pets checkbox is checked.
            pet.setText("You have selected that a you will bring a pet.");                          // Set the text to the string.
            petsChosen = "Yes";                                                                     // For use with sharedPreferences
        } else {
            pet.setText("No accompanying pets.");                                                   //Set the text to the string.
            petsChosen = "No";                                                                      //For use with sharedPreferences

        }//end if

        switch (chosenAccom) {
            case "Caravan"  :   accomCharge = CARAVAN_CHARGE;                                       // Add fee for accommodation so correct amount is charged
                                break;
            case "Cabin"    :   accomCharge = CABIN_CHARGE;
                                break;
            case "Tent"     :   accomCharge = TENT_CHARGE;
                                break;
        } // End Switch

        totalCost += accomCharge * numberOfNights;                                                  // Calculate the total cost for the accommodation
        totalCostText.setText(Float.toString(totalCost));                                           // Set the text to the string converted from the float

    }//end onCreate

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (disabled.isChecked()) {                                                                 // Check to see if the disabled checkbox has been checked
            disabledAccessText.setText("Disabled Access is required");                              // Set the text to the string.
            disabledChosen = "Yes";                                                                 // For use with sharedPreferences

        } else {
            disabledAccessText.setText("Disabled Access is NOT required");                          // Set the text to the string.
            disabledChosen = "No";                                                                  // For use with sharedPreferences
        }//end if
    }// end onCheckedChanged.


    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.confirmButton :
                SharedPreferences.Editor editor = campsiteSharedPreferences.edit();                 // Create editor for shared preference object.
                editor.putString("chosenPark", chosenPark);
                editor.putString("chosenAccom", chosenAccom);
                editor.putString("chosenFromDate", chosenFromDate);
                editor.putString("chosenToDate", chosenToDate);                                     //Put all data into sharedPreferences
                editor.putInt("sleepingCap", sleepingCap);
                editor.putInt("numberOfNights", numberOfNights);
                editor.putString("under16", under16);
                editor.putString("under5",under5);
                editor.putString("pets",petsChosen);
                editor.putString("disabled", disabledChosen);
                editor.putFloat("totalCost", totalCost);
                editor.apply();                                                                     // Make sure data is written.

                intent = new Intent(this, PayActivity.class);                                       // Create a new intent to start PayActivity
                packageManager = getPackageManager();
                activities = packageManager.queryIntentActivities(intent, 0);                       // Adapted from lecture notes.
                isIntentSafe = activities.size() > 0;

                if (isIntentSafe) {                                                                 // Ensure intent is safe to execute.
                    startActivity(intent);                                                          // Start the required activity
                }// end if

                break;


            case R.id.backButtonConfrim :
                super.onBackPressed();                                                              //Code adapted from http://chrisrisner.com/31-Days-of-Android--Day-10-The-Back-Button/
                break;                                                                              //Returns to the previous activity.
        }//end switch

    } // end onClick

}//end ConfirmationActivity



