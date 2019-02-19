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
 * PayActivity.class
 *
 ****************************************************************************/


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;                                                      // Import all required classes.
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import java.util.List;


public class PayActivity extends AppCompatActivity implements View.OnClickListener {

    private Button payButton , goBackButton;                                                        //Declare required variables
    private ImageButton visaButton,mastercardButton,amexButton,payPalButton;
    private EditText customerName,phoneNumber,emailAddress,cardNumber,cvvNumber;
    private boolean payMethod = false , okToContinue = true , amexButtonActive = false, payPalButtonActive = false;
    private String cardNumberToSecure,cvvNumString,nameString,phoneNumString,emailAddString;                          // Take name and add a 6 digit number after it
    private SharedPreferences campsiteSharedPreferences;                                            // Shared preferences object
    private PackageManager packageManager;                                                          // set up required items for safe use of intents.
    private List<ResolveInfo> activities;
    private boolean isIntentSafe;
    private Intent intent , webIntent;
    private int paypalResult;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        campsiteSharedPreferences = getSharedPreferences("campsitePrefFile", MainActivity.MODE_PRIVATE);        // Retrieve shared preference object.

        customerName = (EditText)findViewById(R.id.nameText);                                       // Set up EditTexts
        phoneNumber = (EditText)findViewById(R.id.phoneText);
        emailAddress = (EditText)findViewById(R.id.emailText);
        cardNumber = (EditText)findViewById(R.id.cardNumberText);
        cvvNumber = (EditText)findViewById(R.id.cvvText);

        visaButton = (ImageButton) findViewById(R.id.visaButton);                                   // Setup ImageButtons and listeners.
        visaButton.setOnClickListener(this);
        mastercardButton = (ImageButton) findViewById(R.id.mastercardButton);
        mastercardButton.setOnClickListener(this);
        amexButton= (ImageButton) findViewById(R.id.amexButton);
        amexButton.setOnClickListener(this);
        payPalButton = (ImageButton) findViewById(R.id.paypalButton);
        payPalButton.setOnClickListener(this);

        payButton = (Button) findViewById(R.id.payNowButton);                                       // Setup Buttons and listeners.
        payButton.setOnClickListener(this);
        goBackButton = (Button) findViewById(R.id.goBackbuttonPay);
        goBackButton.setOnClickListener(this);

    }

    public void onClick(View view) {

        okToContinue = true;
                                                                                                    // Resets oktoContinue  every time button is pressed

        switch (view.getId()) {                                                                     // Get the id of the button pressed

            case R.id.visaButton :
                visaButton.setImageResource(R.drawable.visa);                                       // Change other button images to crossed out
                amexButton.setImageResource(R.drawable.amexcrossed);
                payPalButton.setImageResource(R.drawable.paypalcrossed);
                mastercardButton.setImageResource(R.drawable.mastercardcrossed);
                payMethod = true;                                                                   // flag that method of payment is valid.
                payPalButtonActive = false;
                amexButtonActive = false;                                                           // Flag to indicate the amex button for use with validation later in program.
                cardNumber.setText(String.valueOf(""));                                             // Reset the cardNumber and cvv to null.
                cvvNumber.setText("");
                break;
            case R.id.mastercardButton :
                mastercardButton.setImageResource(R.drawable.mastercard);                           // Change other button images to crossed out
                amexButton.setImageResource(R.drawable.amexcrossed);
                payPalButton.setImageResource(R.drawable.paypalcrossed);
                visaButton.setImageResource(R.drawable.visacrossed);
                payMethod = true;                                                                   // flag that method of payment is valid.
                payPalButtonActive = false;
                amexButtonActive = false;                                                           // Flag to indicate the amex button for use with validation later in program.
                cardNumber.setText(String.valueOf(""));                                             // Reset the cardNumber and cvv to null.
                cvvNumber.setText("");
                break;
            case R.id.amexButton :
                amexButton.setImageResource(R.drawable.amex);                                       // Change other button images to crossed out
                visaButton.setImageResource(R.drawable.visacrossed);
                payPalButton.setImageResource(R.drawable.paypalcrossed);
                mastercardButton.setImageResource(R.drawable.mastercardcrossed);
                amexButtonActive = true;                                                            // Flag to indicate the amex button for use with validation later in program.
                payPalButtonActive = false;
                payMethod = true;                                                                   // Flag that method of payment is valid.
                cardNumber.setText(String.valueOf(""));                                             // Reset the cardNumber and cvv to null.
                cvvNumber.setText("");
                break;
            case R.id.paypalButton :
                payPalButton.setImageResource(R.drawable.paypal);                                   // Change other button images to crossed out
                amexButton.setImageResource(R.drawable.amexcrossed);
                visaButton.setImageResource(R.drawable.visacrossed);
                mastercardButton.setImageResource(R.drawable.mastercardcrossed);
                cardNumber.setText(String.valueOf("PAYPALPAYPALPPAL"));                             // Auto fill the card number and cvv with data.
                cvvNumber.setText("---");
                payPalButtonActive = true;
                amexButtonActive = false;                                                           // Flag to indicate the amex button for use with validation later in program.
                payMethod = true;                                                                   // Flag that method of payment is valid.
                break;

            case R.id.payNowButton :

                if (customerName.getText().toString().equals("")) {
                    Toast.makeText(this, "Please Enter Your Name.", Toast.LENGTH_SHORT).show();     // Validate name has been entered.
                    okToContinue = false;
                    //payMethod = false;
                }// end if

                if ((phoneNumber.getText().toString().length() != 11)) {
                    Toast.makeText(this, "Please Enter a valid phone number including dialing code and must start with a 0.", Toast.LENGTH_SHORT).show();
                    //phoneNumber.setText("");                                                      // Validate phone number is of 11 digits
                    okToContinue = false;
                    //payMethod = false;
                }// end if

                if (!((emailAddress.getText().toString().length() > 3) && (emailAddress.getText().toString().contains("@")) && (emailAddress.getText().toString().contains(".")))){
                        okToContinue = false;                                                       // Validate email address has a '@' and '.' int the text
                        Toast.makeText(this, "Please Enter a valid email address.", Toast.LENGTH_SHORT).show();
                        //emailAddress.setText("");
                        //payMethod = false;
                }// end if

                if (payPalButtonActive && okToContinue) {
                        Toast.makeText(this, "Redirecting you to PayPal.co.uk", Toast.LENGTH_SHORT).show();
                        Uri webpage = Uri.parse(("http://paypal.co.uk"));                               // Indicate webpage to direct intent to
                        webIntent = new Intent(intent.ACTION_VIEW, webpage);                            // Create a new webpage intent
                        packageManager = getPackageManager();
                        activities = packageManager.queryIntentActivities(webIntent, 0);                // Adapted from lecture notes.
                        isIntentSafe = activities.size() > 0;
                        if (isIntentSafe) {                                                             // Ensure its safe to run the intent
                            startActivityForResult(webIntent, paypalResult);                            // Start the webPage intent.
                        }// end if


                }// end if



                //Toast.makeText(this, String.valueOf(payMethod) + String.valueOf(okToContinue), Toast.LENGTH_SHORT).show();    //***** TEST CODE******
                if (okToContinue && payMethod) {
                    nameString = customerName.getText().toString();                                 // If all data ok get all the details
                    cardNumberToSecure = cardNumber.getText().toString();
                    cvvNumString = cvvNumber.getText().toString();
                    nameString = customerName.getText().toString();
                    phoneNumString = phoneNumber.getText().toString();
                    emailAddString = emailAddress.getText().toString();


                    if ((cardNumberToSecure.length() == 15) && (amexButtonActive)) {                // Check if amex used and validate 15 digits long
                        okToContinue = true;
                        //Toast.makeText(this, "amex is active and string is 15", Toast.LENGTH_SHORT).show(); //*** TEST CODE ****
                    }else
                        if ((cardNumberToSecure.length() == 16) && (!amexButtonActive)) {           // Check if visa used and validate 16 digits long
                            okToContinue = true;
                            //Toast.makeText(this, "amex is not active and string is 16", Toast.LENGTH_SHORT).show();//****TEST CODE****
                        }else {

                            okToContinue = false;                                                   // Display message to user to indicate requirements
                            Toast.makeText(this, "Card number invalid must be 16 digits for Visa & 15 Digits for American Express , please renter card details.", Toast.LENGTH_SHORT).show();
                        }

                    if ((cvvNumString.length() == 3) && (!amexButtonActive)) {                      // Check cvv number and validate it is 3 digits long
                        okToContinue = true;
                        //Toast.makeText(this, "amex is not active and string is 3", Toast.LENGTH_SHORT).show();//****TEST CODE****
                    } else if ((cvvNumString.length() == 4) && (amexButtonActive)){
                            okToContinue = true;
                            //Toast.makeText(this, "amex is  active and string is 4", Toast.LENGTH_SHORT).show(); //****TEST CODE****

                        }else {
                        Toast.makeText(this, "please enter correct cvv number last 3 digits for visa or mastercard, 4 digits for american express.", Toast.LENGTH_SHORT).show();
                        okToContinue = false;
                    }// end if else
                    //Toast.makeText(this,String.valueOf(okToContinue), Toast.LENGTH_SHORT).show(); // ***** TEST CODE******

                    if (okToContinue){                                                              // If everything valid then continue

                        cardNumberToSecure = secureCardNumber(cardNumberToSecure);                  // Execute the secureCardNumber function to hide card details

                        intent = new Intent(this, ReceiptActivity.class);                           // Create new intent
                        SharedPreferences.Editor editor = campsiteSharedPreferences.edit();         // Create editor for shared preference object.
                        editor.putString("customersName",nameString);
                        editor.putString("phoneNumber", phoneNumString);                            // Write all data to shared preference file.
                        editor.putString("emailAddress", emailAddString);
                        editor.putString("cardNumber", cardNumberToSecure);                         // No card details stored only last 4 digits will show  if customer recalls booking
                        editor.apply();
                        //intent.putExtra("chosenPark" , chosenPark);
                        //Toast.makeText(this, cardNumberToSecure, Toast.LENGTH_SHORT).show();      // ***** TEST CODE******
                        packageManager = getPackageManager();
                        activities = packageManager.queryIntentActivities(intent, 0);               // Adapted from lecture notes.
                        isIntentSafe = activities.size() > 0;
                        if(isIntentSafe) {                                                          // Ensure activity is safe to execute
                            startActivity(intent);                                                  // Start the ReceiptActivity
                        }// end if
                    } // end if
                } else {
                    Toast.makeText(this, "please choose a method of payment", Toast.LENGTH_SHORT).show();  // Inform user to choose a payment method
                } // end if

                break;

            case R.id.goBackbuttonPay :
                super.onBackPressed();                                                              // Return user to previous Activity
                break;
        } // end switch

    } // end onClick.

    public String secureCardNumber (String cardToSecure) {                                          // Takes a string and replaces the replaces with '*' leaving only 4 at the end

        String secureCard = "";

        for (int i = 1; i < cardToSecure.length() - 4; i++) {                                       // Count through the string
            secureCard += "*";                                                                      // Create a string of '*'
        }
        for (int i = cardToSecure.length() - 4; i < cardToSecure.length(); i++) {                   // Get last 4 characters of the string.
            secureCard += cardToSecure.charAt(i);                                                   // Add them to the end of the '*' string.
        }

        return secureCard;                                                                          // Return the created string
    } // end of secureCardNumber



}// End PayActivity
