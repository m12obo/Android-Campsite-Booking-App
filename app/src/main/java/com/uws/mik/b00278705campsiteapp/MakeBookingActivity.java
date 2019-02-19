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
 * MakeBookingActivity.class
 *
 ****************************************************************************/


import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;                                                                 // Import all required classes.
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MakeBookingActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener,CompoundButton.OnCheckedChangeListener {


    private Button continueButton, goBackButton;                                                    // Declare required variables
    private CheckBox over21CheckBox, petsCheckBox;
    private EditText under5,under16,adults;
    private RadioGroup parkGroup, accomGroup;
    private RadioButton park1RadButton,park2RadButton,park3RadButton;
    private RadioButton caravanRadButton,cabinRadButton,tentRadButton;
    private ImageView parkImage,accomImage;
    private DatePicker fromDatePicked, toDatePicked;
    private Calendar calendarFrom, calendarTo;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");                               //Format the date so its UK
    private Date fromDate ,toDate;
    private Spinner sleepCapacity;
    private static final String[] items={"2","4","6","8"};
    boolean okToContinue = true ,bothChosen;
    private PackageManager packageManager;                                                          // set up required items for safe use of intents.
    private List<ResolveInfo> activities;
    private boolean isIntentSafe;
    private Intent intent, intentData;
    private String chosenPark, chosenAccom;
    private String chosenFromDate,chosenToDate;
    private String numOfUnder16, numOfUnder5, numOfAdults;
    private static int PET_CHARGE = 3;
    private int capacitySelected = 0, numberOfNights, radioButtonID,sameDayCharge;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_booking);

        intentData = getIntent();                                                                   // Get the data passed to this activity from other activities
        bothChosen = intentData.getBooleanExtra("bothChosen", false);
        chosenPark = intentData.getStringExtra("chosenPark");
        chosenAccom = intentData.getStringExtra("accomChosen");

        parkGroup = (RadioGroup)findViewById(R.id.parkRadioGroup);                                  //Set up radio buttons and groups
        //parkGroup.setOnClickListener(this);
        accomGroup = (RadioGroup)findViewById(R.id.accomRadioGroup);
        //accomGroup.setOnClickListener(this);
        park1RadButton = (RadioButton)findViewById(R.id.park1RadioButton);
        park1RadButton.setOnCheckedChangeListener(this);
        park2RadButton = (RadioButton)findViewById(R.id.park2RadioButton);
        park2RadButton.setOnCheckedChangeListener(this);
        park3RadButton = (RadioButton)findViewById(R.id.park3RadioButton);
        park3RadButton.setOnCheckedChangeListener(this);
        caravanRadButton = (RadioButton)findViewById(R.id.caravanRadioButton);
        caravanRadButton.setOnCheckedChangeListener(this);
        cabinRadButton = (RadioButton)findViewById(R.id.cabinRadioButton);
        cabinRadButton.setOnCheckedChangeListener(this);
        tentRadButton = (RadioButton)findViewById(R.id.tentRadioButton);
        tentRadButton.setOnCheckedChangeListener(this);

        parkImage = (ImageView)findViewById(R.id.parkImage);                                        // Set up the image views
        accomImage = (ImageView)findViewById(R.id.accomImage);

        fromDatePicked = (DatePicker)findViewById(R.id.fromDatePicker);                             // Set up the date pickers
        toDatePicked = (DatePicker)findViewById(R.id.toDatePicker);

        /*fromDatePicked.init(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),new DatePicker.OnDateChangedListener(){

            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {

                yearFrom = year;                                                                      // Tried to implement the code to try and get the date from the date picker but used something else instead.
                monthFrom = month + 1;                                                                // Code from http://stackoverflow.com/questions/2051153/android-ondatechangedlistener-how-do-you-set-this
                dayFrom = dayOfMonth;

                Toast.makeText(MakeBookingActivity.this, dayFrom + monthFrom + yearFrom, Toast.LENGTH_SHORT).show();
            }
        });
        */

        under16 = (EditText)findViewById(R.id.under16EditText);                                     // Set up edit texts
        under5 = (EditText)findViewById(R.id.under5EditText);
        adults = (EditText)findViewById(R.id.numOfAdultsEditText);


        over21CheckBox = (CheckBox)findViewById(R.id.over21CheckBox);                               //Set up Check Boxes
        over21CheckBox.setOnCheckedChangeListener(this);

        over21CheckBox = (CheckBox)findViewById(R.id.over21CheckBox);
        over21CheckBox.setOnCheckedChangeListener(this);

        petsCheckBox = (CheckBox)findViewById(R.id.petCheckBox);
        petsCheckBox.setOnCheckedChangeListener(this);

        continueButton = (Button)findViewById(R.id.contPayButton);                                  //Set up buttons with listeners.
        continueButton.setOnClickListener(this);
        goBackButton = (Button)findViewById(R.id.goBackButton);
        goBackButton.setOnClickListener(this);

        sleepCapacity=(Spinner)findViewById(R.id.sleepingCapSpinner);                               //Set up the spinner and listener
        sleepCapacity.setOnItemSelectedListener(this);
        ArrayAdapter<String> aa=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,items);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sleepCapacity.setAdapter(aa);

        accomGroup.clearCheck();
        parkGroup.clearCheck();
        if (bothChosen){                                                                            // From data passed to this activity this checks to see if user has came straight from
                                                                                                    // MainActivity or through InfoActiviy and AccommodationActivity  so will check the radio buttons if they did.
            switch (chosenPark) {
                case "Our Parks"             : parkGroup.clearCheck();
                                               break;                                               // Ensures all radio buttons are unchecked.

                case "Loch Brannoch Park"    : park1RadButton.toggle();//onCheckedChanged(park1RadButton, true);
                                               break;
                case "Lothianside Park"      : park2RadButton.toggle();                             // Toggles the radio buttons depending on park selected in InfoActivity.
                                               break;
                case "McKeowns Holiday Park" : park3RadButton.toggle();
                                               break;

            }// end switch

            switch (chosenAccom) {
                case "Our Accommodation"    :   accomGroup.clearCheck();
                                                break;                                              // Ensures all radio buttons are unchecked.
                case "Caravan"              :   caravanRadButton.toggle();
                                                break;
                case "Cabin"                :   cabinRadButton.toggle();                            // Toggles the radio buttons depending on accommodation selected in AccommodationActivity.
                                                break;
                case "Tent"                 :   tentRadButton.toggle();
                                                break;
            }// end switch

        }// end if

    }// end onCreate

    public void onItemSelected(AdapterView<?> parent,View v, int position, long id) {

        capacitySelected = Integer.parseInt((items[position]));                                     // Get the data from the spinner and convert it to an integer for use with
                                                                                                    // checking number of holiday makers does not over book the accommodation.
    }// end onItemSelected

    public void onNothingSelected(AdapterView<?> parent) {

        capacitySelected = 0;                                                                       // Ensures that if nothing selected the capacity is set to 0.

    }// end onNothingSelected


    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {





        switch (buttonView.getId()) {

            case R.id.park1RadioButton:                                                             //Check to see which radioButton is chosen and display the correct image.
                parkGroup.clearCheck();
                parkImage.setImageResource(R.drawable.park1);
                chosenPark = "Loch Brannoch Touring Park";
                break;

            case R.id.park2RadioButton:
                parkGroup.clearCheck();
                parkImage.setImageResource(R.drawable.park2);
                chosenPark = "Lothianside Camping & Caravanning Park";
                break;

            case R.id.park3RadioButton:
                parkGroup.clearCheck();
                parkImage.setImageResource(R.drawable.park3);
                chosenPark = "McKeowns Holiday Park";
                break;

            case R.id.caravanRadioButton:
                accomGroup.clearCheck();
                accomImage.setImageResource(R.drawable.caravanparkwall);
                chosenAccom = "Caravan";
                break;

            case R.id.cabinRadioButton:
                accomGroup.clearCheck();
                accomImage.setImageResource(R.drawable.cabindrobudden);
                chosenAccom = "Cabin";
                break;


            case R.id.tentRadioButton:
                accomGroup.clearCheck();
                accomImage.setImageResource(R.drawable.tentpintrest);
                chosenAccom = "Tent";
                break;

            case R.id.petCheckBox :
                if (petsCheckBox.isChecked()) {
                    Toast.makeText(this, "A charge of £" + PET_CHARGE + " has been added for the use of the pet facilities.", Toast.LENGTH_SHORT).show(); // Display message to user to inform them of charge for pets.
                } else {
                    Toast.makeText(this, "A charge of £" + PET_CHARGE + " will no longer apply.", Toast.LENGTH_SHORT).show(); // Display message to user to inform charge for pets no longer applies.
                }// end if
                break;
        }//end switch

    }//end onCheckedChanged

    public Date getDates(DatePicker datePicker ) {                                                  // Gets date from datepicker and returns the date with time set to 0

        Calendar calendar;                                                                          // Declare variables.
        Date date;
        int year,month,day;

        year = datePicker.getYear();
        month = datePicker.getMonth();
        day = datePicker.getDayOfMonth();
        calendar = Calendar.getInstance();                                                          // Adapted from http://www.mkyong.com/java/java-date-and-calendar-examples/
        calendar.set(Calendar.YEAR, year);                                                          // Gets the date from the date picker and stores it in calenderFrom
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);                                                      // Code adapted from http://stackoverflow.com/questions/17821601/set-time-to-000000
        //calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);                                                           // Ensures time is set to 0 for use in comparison later.
        calendar.set(Calendar.MILLISECOND, 0);
        date = calendar.getTime();                                                                  //Converts the calendar format into date for use later to calculate number of nights
        return date;

    }// end getDatesFrom




    public void onClick(View view) {

        okToContinue = true;                                                                        //Reset okToContinue to true when button pressed for use with making sure
        sameDayCharge = 0;                                                                          // that all entries made are correct and make sure sameDAyCharge is reset to 0;.
        switch (view.getId()) {
            case R.id.contPayButton :
                if (over21CheckBox.isChecked()) {                                                   // First check over 21 checkbox is checked

                    fromDate = getDates(fromDatePicked);                                            //Gets the value of the fromDatePicked from the datePicker
                    /*
                    yearFrom = fromDatePicked.getYear();
                    monthFrom = fromDatePicked.getMonth();
                    dayFrom = fromDatePicked.getDayOfMonth();
                    calendarFrom = Calendar.getInstance();                                          // Adapted from http://www.mkyong.com/java/java-date-and-calendar-examples/
                    calendarFrom.set(Calendar.YEAR, yearFrom);                                      // Gets the date from the date picker and stores it in calenderFrom
                    calendarFrom.set(Calendar.MONTH, monthFrom);
                    calendarFrom.set(Calendar.DAY_OF_MONTH, dayFrom);
                    yearTo = toDatePicked.getYear();
                    monthTo = toDatePicked.getMonth();                                              // UPDATE ALL THIS CODE MOVED TO ITS OWN FUNCTION.
                    dayTo = toDatePicked.getDayOfMonth();                                           // Adapted from http://www.mkyong.com/java/java-date-and-calendar-examples/
                    calendarToday.set(Calendar.YEAR, todaysDate.getYear());
                    calendarToday.set(Calendar.MONTH, todaysDate.getMonth());
                    calendarToday.set(Calendar.DAY_OF_MONTH, todaysDate.getDay());
                    calendarToday.set(Calendar.HOUR, 0);

                    */

                    Date todaysDate;
                    Calendar calendarToday;

                    calendarToday = Calendar.getInstance();                                         // Gets a new instance of calendar
                    calendarToday.set(Calendar.HOUR_OF_DAY, 0);                                     // Set the time to 0 for use with comparison later
                    calendarToday.set(Calendar.MINUTE, 0);
                    calendarToday.set(Calendar.MILLISECOND, 0);
                    todaysDate = calendarToday.getTime();                                           // Store result as todays date

                    toDate = (getDates(toDatePicked));                                              //Gets the value of the toDatePicked from the datePicker

                    //Toast.makeText(this, "From Date : " + fromDate, Toast.LENGTH_SHORT).show();    //*****TEST CODE****
                    //Toast.makeText(this, "Todays Date : " + todaysDate, Toast.LENGTH_SHORT).show();//*****TEST CODE****
                    //Toast.makeText(this, "To Date : " + toDate, Toast.LENGTH_SHORT).show();        //*****TEST CODE****

                    if (toDate.before(fromDate)) {                                                  // Checks to see if toDate is before fromDate
                        Toast.makeText(this, "From Date " + sdf.format(fromDate) + " can NOT be after To Date " + sdf.format(toDate) + "Please enter valid dates.", Toast.LENGTH_SHORT).show();
                        okToContinue = false;                                                       // Code adapted from  http://developer.android.com/reference/java/util/Date.html
                    } else if (fromDate.before(todaysDate)) {                                       // Checks to see if date is before today

                        Toast.makeText(this, "Dates not available please enter a date after " + sdf.format(new Date()), Toast.LENGTH_SHORT).show(); // Display message to user to re enter from date.
                        okToContinue = false;                                                       // Set okToContinue to false so user can not proceed until change has been made.

                    } //end if else



                    /* The above section caused different issues and I am still not happy with it , one major issue is with the date
                    if I choose today as a starting date it wont allow the user to proceed I have a feeling its to do with the way
                    ".before" method and how it calculates the result I will need to do more research on it , however I have left it as is
                    at the moment until I can work out how to rectify the problem.

                    Tried method below by converting dates to sting and comparing but this way had its own issues too.

                    28/02/2016 *** UPDATE *** it was a time problem by setting the time for all dates to 0 seemed to fix the issue so sticking
                    with comparing dates rather than comparing strings.
                     */

                    chosenToDate = sdf.format(toDate);                                              // With the date problems as mentioned above involves time as well as date
                    chosenFromDate = sdf.format(fromDate);                                          // easiest way to figure out if only dates matched was to compare 2 formatted dates

                    //Toast.makeText(this,"Dates equal = " +  chosenFromDate.compareTo(todayAsString), Toast.LENGTH_SHORT).show(); //****TEST CODE****

                    /*
                    if (chosenFromDate.compareTo(todayAsString) == -1) {           // Checks to see if date is before today
                        Toast.makeText(this, "Dates not available please enter a date from " + sdf.format(new Date()), Toast.LENGTH_SHORT).show(); // Display message to user to re enter from date.
                        okToContinue = false;                           // Set okToContinue to false so user can not proceed until change has been made.
                    } else {
                        switch (chosenFromDate.compareTo(chosenToDate)) {

                            case 1:
                                Toast.makeText(this, "From Date " + sdf.format(fromDate) + " can NOT be after To Date " + sdf.format(toDate) + "Please enter valid dates.", Toast.LENGTH_SHORT).show();
                                okToContinue = false;                   // Code adapted from  http://developer.android.com/reference/java/util/Date.html
                                break;
                            case -1:
                                 Toast.makeText(this, "To Date " + sdf.format(fromDate) + " can NOT be before from Date " + sdf.format(toDate) + "Please enter valid dates.", Toast.LENGTH_SHORT).show();
                                 okToContinue = false;                   // Code adapted from  http://developer.android.com/reference/java/util/Date.html
                                 break;
                            case 0:
                                okToContinue = true;
                                break;

                        }//end switch
                    }//end if else
                    */


                    radioButtonID = parkGroup.getCheckedRadioButtonId();                            // Gets an int if the radio button is selected otherwise returns a -1
                                                                                                    // Adapted from http://developer.android.com/reference/android/widget/RadioGroup.html
                    if (radioButtonID == -1) {                                                      // Check to make sure a radio button is selected if not display a message.
                        Toast.makeText(this,/* radioButtonID+ *** TEST CODE ***/ "Please select a park", Toast.LENGTH_SHORT).show();
                        okToContinue = false;

                    }// end if

                    radioButtonID = accomGroup.getCheckedRadioButtonId();                           // Gets an int if the radio button is selected otherwise returns a -1
                                                                                                    // Adapted from http://developer.android.com/reference/android/widget/RadioGroup.html
                    if  (radioButtonID == -1) {                                                     // Check to make sure a radio button is selected if not display a message.
                        Toast.makeText(this,/* radioButtonID+ *** TEST CODE ***/  "Please select your accommodation", Toast.LENGTH_SHORT).show();
                        okToContinue = false;

                    }// end if

                }else{                                                                              // Display a message to inform user to check over 21 checkbox
                    Toast.makeText(this,"A member of your party must be over 21 , please check the box to continue",Toast.LENGTH_LONG).show();
                    okToContinue = false;
                } // end if

                if (adults.getText().toString().equals("")) {                                       // Check to make sure a number of adults has been entered if not display a message.
                    Toast.makeText(this, "Please Enter the Number of adults (16 and over)." , Toast.LENGTH_SHORT).show();
                    okToContinue = false;
                } else {
                    numOfAdults = adults.getText().toString();                                      // Get the number of adults.
                }// end if

                if (under16.getText().toString().equals("")) {                                      // Check to make sure a number of under 16s has been changed (Default set to 0) if blank display a message.
                    Toast.makeText(this, "Please Enter the Number of children between 6 & 15." , Toast.LENGTH_SHORT).show();
                    okToContinue = false;

                } else {
                    numOfUnder16 = under16.getText().toString();                                    // Get the number of under 16s.
                }// end if else


                if (under5.getText().toString().equals("")) {                                       //Check to make sure a number of under 5s has been changed (Default set to 0) if blank display a message.
                    Toast.makeText(this, "Please Enter the Number of children between 0 & 5." , Toast.LENGTH_SHORT).show();
                    okToContinue = false;

                } else {
                    numOfUnder5 = under5.getText().toString();                                      // Get the number of under 5s (Default set to 0).
                }// end if else

                if (okToContinue) {
                    int familyTotal = Integer.parseInt(numOfAdults) + Integer.parseInt(numOfUnder16) +Integer.parseInt(numOfUnder5); // Calculate the family total.
                    if (capacitySelected < familyTotal) {                                                                            // Check family total does not exceed sleeping capacity.
                        Toast.makeText(this, "Please select a larger sleeping capacity , if maximum capacity to low please make a separate booking to accommodate everyone", Toast.LENGTH_SHORT).show();
                        okToContinue = false;
                    }// end if
                }// end if



                if (okToContinue) {                                                                 // Check all checks have been made

                    if (chosenFromDate.equals(chosenToDate)) {                                      // This check ensures the user is charged for 1 night if they pick same arrival and departure dates
                        Toast.makeText(this, "Choosing the same arrival and departure date of " + sdf.format(toDate) + "will incur a charge for one night.", Toast.LENGTH_SHORT).show();
                        sameDayCharge = 1;
                    }// end if

                    //totalYear = toDate.getYear() - fromDate.getYear();                            // Used code below instead
                    //totalMonth = toDate.getMonth() - fromDate.getMonth();                         // Calculate the number of nights
                    //totalDay = toDate.getDay() - fromDate.getDay() + sameDayCharge;               // from the dates entered (sameday charge will either be 1 or 0)

                    numberOfNights = (int) ((toDate.getTime() - fromDate.getTime()) / (1000*60*60*24)) + sameDayCharge;                     //Code adapted from https://www.youtube.com/watch?v=5F1ZFCxxZvA
                    Toast.makeText(this,"Total Nights = "+ numberOfNights, Toast.LENGTH_SHORT).show(); // **** Test code *****
                    intent = new Intent(this, ConfirmationActivity.class);                          // create a new ConfirmationActivity intent
                    intent.putExtra("chosenPark", chosenPark);
                    intent.putExtra("chosenAccom", chosenAccom);                                    // Write all the required data to the shared preferences file.
                    intent.putExtra("chosenFromDate", chosenFromDate);
                    intent.putExtra("chosenToDate", chosenToDate);
                    intent.putExtra("sleepingCap" , capacitySelected);
                    intent.putExtra("numberOfNights", numberOfNights);
                    intent.putExtra("under16", numOfUnder16);
                    intent.putExtra("under5", numOfUnder5);

                    if (petsCheckBox.isChecked()){                                                  // Check if pet checkbox is checked if so set chosen pet to true
                        intent.putExtra("chosenPet", true);
                    }
                    packageManager = getPackageManager();
                    activities = packageManager.queryIntentActivities(intent, 0);                   // Adapted from lecture notes.
                    isIntentSafe = activities.size() > 0;
                    if(isIntentSafe) {                                                              // Ensure intent is safe to execute.
                        startActivity(intent);                                                      // Start the ConfirmActivity
                    }// end if
                }// end if
                break;

            case R.id.goBackButton :
                super.onBackPressed();                                                              // Sends user to previous activity.
                break;
        }// end switch

    }//end onClick

}// end MakeBookingActivity.
