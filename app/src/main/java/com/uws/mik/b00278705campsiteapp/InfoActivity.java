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
 * InfoActivity.class
 *
 ****************************************************************************/

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;                                                                           // Import all required classes.
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class InfoActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Intent intent;                                                                          // Declare required variables
    private Button accommButton, goBackButton;
    private ImageButton locationButton;
    private String chosenPark;

    private PackageManager packageManager;                                                          // set up required items for safe use of intents.
    private List<ResolveInfo> activities;
    private boolean isIntentSafe;

    private TextView selectionText;
    private ImageView selectionImage;
    private Spinner parkInfo;
    private static final String[] items = {"Our Parks", "Loch Brannoch Park", "Lothianside Park", "McKeowns Holiday Park"};
    private static final String[] images = {"R.drawable.allparks", "R.drawable.park1", "R.drawable.park2" , "R.drawable.park3"};
    private static final String[] parkInfoText =
           {
                    "Hi and welcome to our park information page, Please use the dropdown menu above to view the different types of holiday parks we have available." +
                            " To view their locations press the location button at the bottom to see a map of where the parks are located. ",

                    "This is Loch Brannoch park, it is located next to the loch with stunning views and we have lots of activities available, these include walking, boating, cycling and swimming." +
                            "We have shop and shower facilities and is in a central location which is in close proximity to lots of local attractions.",

                    "This is Lothianside Park, it is located in the hillside with stunning views and we have lots of activities available, these include walking, cycling, birds of prey and golf." +
                            "We have shop and shower facilities and is in a central location which is close to lots of local golfing attractions.",

                    "This is McKeowns Holiday Park, it is located at the foot of the cairngorms with stunning views and we have lots of activities available, these include walking, cycling , horse riding and archery." +
                            "We have shop and shower facilities and is in a central location which is close to a castle which offer several attractions."
            };
    private String geoLocation;                                                                     // This is used to pass location information to the mapIntent Geolocations provided by http://mygeoposition.com/
    private static final String GEO_LOCATION_PARK1 = "geo:55.0101900,-4.0562280?z=14";              // Actual location of Loch Ken Holiday Park, Parton, Parton, Castle Douglas, Dumfries and Galloway DG7 3NE
    private static final String GEO_LOCATION_PARK2 = "geo:56.1941230,-2.8620560?z=14";              // Actual location of Elie Holiday Park Holiday Park Shell Bay, Elie, Fife KY9 1HB
    private static final String GEO_LOCATION_PARK3 = "geo:56.7669760,-3.8457660?z=14";              // Actual location of Blair Castle Caravan Park Blair Atholl, Pitlochry, Perthshire & Kinross PH18 5SR
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        accommButton = (Button)findViewById(R.id.accommButton);                                     // Set up Buttons & Listeners
        accommButton.setOnClickListener(this);
        goBackButton = (Button)findViewById(R.id.goBackButton);
        goBackButton.setOnClickListener(this);

        locationButton = (ImageButton)findViewById(R.id.locationButton);                            // Set up ImageButton & listener
        locationButton.setOnClickListener(this);

        selectionText=(TextView)findViewById(R.id.parkInfo);                                        // Set up TextView
        selectionImage=(ImageView)findViewById((R.id.parkImage));

        parkInfo=(Spinner)findViewById(R.id.spinnerInfo);                                           // Set up Spinner
        parkInfo.setOnItemSelectedListener(this);
        ArrayAdapter<String> aa=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,items);
        aa.setDropDownViewResource (android.R.layout.simple_spinner_dropdown_item);
        parkInfo.setAdapter(aa);

    }// end onCreate

    public void onClick(View view) {

        switch (view.getId()) {                                                                     // Get id of button pressed.

            case R.id.accommButton:
                intent = new Intent(this, AccommodationActivity.class);                             //Create a new AccommodationActivity intent
                intent.putExtra("chosenPark", chosenPark);
                packageManager = getPackageManager();
                activities = packageManager.queryIntentActivities(intent, 0);                       // Adapted from lecture notes.
                isIntentSafe = activities.size() > 0;
                //Toast.makeText(this, String.valueOf(isIntentSafe) ,Toast.LENGTH_LONG).show();     // *****TEST CODE******
                //Toast.makeText(this,chosenPark, Toast.LENGTH_SHORT).show();                       // **** TEST CODE *****
                if (isIntentSafe) {                                                                 // Ensure intent is safe to execute.
                    startActivity(intent);                                                          // Start the intent.
                }// end if
                break;

            case R.id.goBackButton:
                super.onBackPressed();                                                              // Return the user to the previous screen
                break;

            case R.id.locationButton:
                //Toast.makeText(this,parkInfo.toString() , Toast.LENGTH_SHORT).show();             //****TEST CODE ****
                if (chosenPark.equals("Our Parks")) {                                               // Force user to select a park from the list.
                    Toast.makeText(this, " Please first select a park from the dropdown list", Toast.LENGTH_SHORT).show();
                } else {
                    Uri location = Uri.parse(geoLocation);                                          //Use the geo location information required
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);                    // Create a new map intent
                    packageManager = getPackageManager();
                    activities = packageManager.queryIntentActivities(mapIntent, 0);                // Adapted from lecture notes.
                    isIntentSafe = activities.size() > 0;
                    //Toast.makeText(this, String.valueOf(isIntentSafe) ,Toast.LENGTH_LONG).show(); //****TEST CODE****

                    if (isIntentSafe) {                                                             // Ensure intent is safe to execute
                        startActivity(mapIntent);                                                   // Start the intent
                    }// end if

                } // end if
        } // end switch

    } // end onClick

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

         selectionText.setText(parkInfoText[position]);                                             // Populate the text field with the text in position 0 of spinner

         switch (position) {                                                                        // Identify from position  the text to use and picture to display.
             case 0:
                 selectionImage.setImageResource(R.drawable.allparks);                              //Tried to program the setImageResource to use the string array
                 chosenPark = items[position];
                 break;                                                                             //images but could work out how to convert string to resource.
             case 1:                                                                                // so had to use R.drawable. here.
                 selectionImage.setImageResource(R.drawable.park1);
                 chosenPark = items[position];
                 geoLocation = GEO_LOCATION_PARK1;
                 break;
             case 2:
                 selectionImage.setImageResource(R.drawable.park2);
                 chosenPark = items[position];
                 geoLocation = GEO_LOCATION_PARK2;
                 break;
             case 3:
                 selectionImage.setImageResource(R.drawable.park3);
                 chosenPark = items[position];
                 geoLocation = GEO_LOCATION_PARK3;
                 break;
         }// end switch

    }// end onItemSelected

    public void onNothingSelected(AdapterView<?> parent) {
        selectionText.setText("");                                                                  // Makes selectionText blank if nothing selected but overridden in onItemSelected so not used.
    }// end onNothingSelected

}// end InfoActivity
