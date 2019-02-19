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
 * MainActivity.class
 *
 ****************************************************************************/

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;                                                                           // Import all required classes.
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button makeBookingButton,parkInfoButton , retrieveButton;                               // Declare required variables
    private ImageButton phoneButton, emailButton;
    private PackageManager packageManager;                                                          // Set up required items for safe use of intents.
    private List<ResolveInfo> activities;
    private boolean isIntentSafe;
    private Intent intent;
    private ImageView logo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {                                            // Set up buttons when activity is created.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        makeBookingButton = (Button)findViewById(R.id.bookingButton);                               //Set up buttons with listeners.
        makeBookingButton.setOnClickListener(this);
        retrieveButton = (Button)findViewById(R.id.retrieveButton);
        retrieveButton.setOnClickListener(this);
        parkInfoButton = (Button)findViewById(R.id.infoButton);
        parkInfoButton.setOnClickListener(this);

        phoneButton = (ImageButton)findViewById(R.id.phoneButton);                                  //Set up image buttons with listeners.
        phoneButton.setOnClickListener(this);
        emailButton = (ImageButton)findViewById(R.id.emailButton);
        emailButton.setOnClickListener(this);
        logo = (ImageView)findViewById(R.id.logoImageView);
        logo.setOnClickListener(this);

    }// end onCreate.

    public void onClick(View view) {

        switch (view.getId()) {                                                                     //Get the id of the button pressed

            case R.id.logoImageView :

                MediaPlayer soundByte = MediaPlayer.create(this,R.raw.hidehi);                      //Will play hiDeHi theme tune if the logo is pressed (Just a bit of hidden fun :)
                soundByte.start();                                                                  // Code adapted from http://www.tutorialspoint.com/android/android_mediaplayer.htm
                break;                                                                              // HiDeHi theme from http://www.televisiontunes.com/Hi-De-Hi.html.

            case  R.id.bookingButton :
                intent = new Intent(this, MakeBookingActivity.class);                               // Create an intent MakeBookingActivity
                intent.putExtra("bothChosen", false);                                               // Tells MakeBookingActivity to clear radio boxes.
                packageManager = getPackageManager();
                activities = packageManager.queryIntentActivities(intent, 0);                       // Adapted from lecture notes.
                isIntentSafe = activities.size() > 0;
                if(isIntentSafe) {                                                                  // Ensure intent is safe to execute.
                    startActivity(intent);                                                          // Start MakeBookingActivity
                }//end if
                break;

            case R.id.retrieveButton :
                intent = new Intent(this,ReceiptActivity.class);                                    // Create an intent ReceiptActivity
                packageManager = getPackageManager();
                activities = packageManager.queryIntentActivities(intent, 0);                       // Adapted from lecture notes.
                isIntentSafe = activities.size() > 0;
                if(isIntentSafe) {                                                                  // Ensure intent is safe to execute.
                    startActivity(intent);                                                          // Start ReceiptActivity
                }//end if
                break;

            case  R.id.infoButton :
                intent = new Intent(this, InfoActivity.class);                                      // Create an intent infoActivity
                packageManager = getPackageManager();
                activities = packageManager.queryIntentActivities(intent, 0);                       // Adapted from lecture notes.
                isIntentSafe = activities.size() > 0;
                if(isIntentSafe) {                                                                  // Ensure intent is safe to execute.
                    startActivity(intent);                                                          // Start InfoActivity
                }//end if
                break;

            case  R.id.phoneButton :                                                                // Following code from http://stackoverflow.com/questions/11699819/how-do-i-get-the-dialer-to-open-with-phone-number-displayed
                intent = new Intent(Intent.ACTION_DIAL);                                            // Create an intent to use the android phone dialler activity
                intent.setData(Uri.parse("tel:01945045623"));                                       // Fills in the phone number for the dialler
                packageManager = getPackageManager();
                activities = packageManager.queryIntentActivities(intent, 0);                       // Adapted from lecture notes.
                isIntentSafe = activities.size() > 0;
                if(isIntentSafe) {                                                                  // Ensure intent is safe to execute.
                    startActivity(intent);                                                          // Start android phone dialler.
                }//end if
                break;

            case  R.id.emailButton :
                Intent intent = new Intent(android.content.Intent.ACTION_SEND, Uri.parse("mailto:"));       //currently crashes certain phones and wont work in emulator. UPDATE 28/02/2016 no longer crashes due to using isIntentSafe.
                intent.setType("message/rfc822");                                                   // Code taken from below sites and edited to get to work on a real phone.
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@lccp.com"});                 // http://examples.javacodegeeks.com/android/core/email/android-sending-email-example/
                intent.putExtra(Intent.EXTRA_SUBJECT, "Information about your holiday parks.");     //http://www.mkyong.com/android/how-to-send-email-in-android/
                //intent.putExtra(Intent.EXTRA_TEXT, "message");                                    // This line removed as I did not want to put anything in the text field of the email.
                packageManager = getPackageManager();
                activities = packageManager.queryIntentActivities(intent, 0);                       // Adapted from lecture notes.
                isIntentSafe = activities.size() > 0;
                if(isIntentSafe) {                                                                  // Ensure intent is safe to execute.
                    startActivity(Intent.createChooser(intent, "Send email..."));                   // This code as I understand it sets out an intent to send the email.
                }// end if

        }// end switch

    } // end onClick.

}// end MainActivity
