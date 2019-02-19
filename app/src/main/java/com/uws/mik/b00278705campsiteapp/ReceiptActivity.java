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
 * ReceiptActivity.class
 *
 ****************************************************************************/

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;                                                                           // Import all required classes.
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class ReceiptActivity extends AppCompatActivity implements View.OnClickListener {

    private Button leavePage;
    private PackageManager packageManager;                                                          // set up required items for safe use of intents.
    private List<ResolveInfo> activities;
    private boolean isIntentSafe;
    private Intent intent;
    private SharedPreferences campsiteSharedPreferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        campsiteSharedPreferences = getSharedPreferences("campsitePrefFile", MainActivity.MODE_PRIVATE); // Create shared preference object.

        leavePage = (Button)findViewById(R.id.leavePageButton);                                     // Set up Button and Listener.
        leavePage.setOnClickListener(this);

        TextView name = (TextView)findViewById(R.id.nameText);                                      // Set up TextViews and populate with data from shared preferences.
        name.setText(campsiteSharedPreferences.getString("customersName", "No Entry Found"));
        TextView phoneNumber = (TextView)findViewById(R.id.phoneNumText);
        phoneNumber.setText("Your Contact Number : " + campsiteSharedPreferences.getString("phoneNumber", "No Entry Found"));
        TextView emailAddress = (TextView)findViewById(R.id.emailView);
        emailAddress.setText("Booking Email Sent To : " + campsiteSharedPreferences.getString("emailAddress", "No Entry Found"));
        TextView cardNumber = (TextView)findViewById(R.id.cardView);
        cardNumber.setText("Payment Card Used : " +campsiteSharedPreferences.getString("cardNumber", "No Entry Found"));
        TextView chosenPark = (TextView)findViewById(R.id.parkView);
        chosenPark.setText("Park : " + campsiteSharedPreferences.getString("chosenPark", "No Entry Found"));
        TextView chosenAccom = (TextView)findViewById(R.id.accomView);
        chosenAccom.setText("Accommodation : " + campsiteSharedPreferences.getString("chosenAccom", "No Entry Found"));
        TextView chosenFromDate = (TextView)findViewById(R.id.fromDateView);
        chosenFromDate.setText("Arival Date : " + campsiteSharedPreferences.getString("chosenFromDate", "No Entry Found"));
        TextView chosenToDate = (TextView)findViewById(R.id.toDateView);
        chosenToDate.setText("Departure Date : " + campsiteSharedPreferences.getString("chosenToDate", "No Entry Found"));
        TextView sleepingCap = (TextView)findViewById(R.id.sleepingCapView);
        sleepingCap.setText(String.valueOf("Sleeping Capacity : " + campsiteSharedPreferences.getInt("sleepingCap", 0)));
        TextView numberOfNights = (TextView)findViewById(R.id.numOfNightsView);
        numberOfNights.setText(String.valueOf("Number Of Nights : " + campsiteSharedPreferences.getInt("numberOfNights", 0)));
        TextView under16 = (TextView)findViewById(R.id.under16View);
        under16.setText("Number Of Under 16s : " + campsiteSharedPreferences.getString("under16", "No Entry Found"));
        TextView under5 = (TextView)findViewById(R.id.under5View);
        under5.setText("Number Of Under 5s : " + campsiteSharedPreferences.getString("under5", "No Entry Found"));
        TextView pets = (TextView)findViewById(R.id.petView);
        pets.setText("Pets : " + campsiteSharedPreferences.getString("pets", "No Entry Found"));
        TextView disabled = (TextView)findViewById(R.id.disabledView);
        disabled.setText("Disabled Access : " + campsiteSharedPreferences.getString("disabled", "No Entry Found"));
        TextView totalCost = (TextView)findViewById(R.id.totalView);
        totalCost.setText("Total Cost : " + campsiteSharedPreferences.getFloat("totalCost", 0));
        TextView outro = (TextView)findViewById(R.id.outroView);
        outro.setText("We look forward to seeing you on " + campsiteSharedPreferences.getString("chosenFromDate", "No Entry Found"));

    }// end onCreate

    public void onClick(View view) {

        intent = new Intent(this,MainActivity.class);                                               // Create a new intent.
        packageManager = getPackageManager();
        activities = packageManager.queryIntentActivities(intent, 0);                               // Adapted from lecture notes.
        isIntentSafe = activities.size() > 0;
        if(isIntentSafe) {                                                                          // Ensure Activity is safe to execute.
            startActivity(intent);                                                                  // Start the MainActivity
        }// end if
    }// end onClick

}// end ReceiptActivity
