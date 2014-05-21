package com.grupp8DAT255.studiekoll;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.os.Build;
import java.lang.Double;
import java.util.ArrayList;

public class InputActivity extends ActionBarActivity {

	double id, logTime;
	String logDate;
	String category = ""; //An empty default category
	static SQLiteDatabase db; //Don't know why static though....

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input);

		//Controls that the database exists, creates it otherwise
		//as well with the main and category tables
		db=openOrCreateDatabase("MyDB",MODE_PRIVATE, null); 
		db.execSQL("CREATE TABLE IF NOT EXISTS Studiekoll(id INTEGER PRIMARY KEY "
				+ "AUTOINCREMENT, logTime DOUBLE, category VARCHAR, logDate VARCHAR);");
		db.execSQL("CREATE TABLE IF NOT EXISTS Categories(category VARCHAR PRIMARY KEY)");

		//Enables the up (back) button in the actionbar
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.input, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Called when the user clicks on the store_data button
	 * @param view
	 */
	public void storeData(View view){

		//Recovering the time picker
		TimePicker timePicker = (TimePicker) findViewById(R.id.time_picker);

		//Getting selected values from time picker
		double logHour = timePicker.getCurrentHour();
		double logMinute = timePicker.getCurrentMinute();

		//Formatting the time for the database (hours)
		logTime = logHour + logMinute/60;

		//Recovering the spinners
		Spinner logYearSpinner = (Spinner) findViewById(R.id.log_year_spinner);
		Spinner logMonthSpinner = (Spinner) findViewById(R.id.log_month_spinner);
		Spinner logDaySpinner = (Spinner) findViewById(R.id.log_day_spinner);

		//Getting selected values from spinners
		String logYear = (String) logYearSpinner.getSelectedItem();
		String logMonth = (String) logMonthSpinner.getSelectedItem();
		String logDay = (String) logDaySpinner.getSelectedItem();

		//Formatting the date for the database (yyyy-mm-dd)
		logDate = logYear + "-" + logMonth + "-" + logDay;

		//Recovering the category spinner
		Spinner categorySpinner = (Spinner) findViewById(R.id.category_spinner);

		if(categorySpinner.getSelectedItem() != null) {
			category = categorySpinner.getSelectedItem().toString();
		}

		db.execSQL("INSERT INTO Studiekoll VALUES ("+ null +","+logTime+", '"+category+"','"+logDate+"');");

		Intent inputIntent = new Intent(this, MainActivity.class);
		startActivity(inputIntent);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_input,
					container, false);

			//Setting the timepicker to be in 24h-mode
			TimePicker timePicker = (TimePicker) rootView.findViewById(R.id.time_picker);
			timePicker.setIs24HourView(true);

			//Initialising the different spinner-objects
			Spinner logYearSpinner = (Spinner) rootView.findViewById(R.id.log_year_spinner);
			Spinner logMonthSpinner = (Spinner) rootView.findViewById(R.id.log_month_spinner);
			Spinner logDaySpinner = (Spinner) rootView.findViewById(R.id.log_day_spinner);

			// Create the different ArrayAdapters using the string array and a default spinner layout
			ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(getActivity(),
			        R.array.year_array, android.R.layout.simple_spinner_item);
			ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(getActivity(),
			        R.array.month_array, android.R.layout.simple_spinner_item);
			ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(getActivity(),
			        R.array.day_array, android.R.layout.simple_spinner_item);

			// Apply the adapters to the spinners
			logYearSpinner.setAdapter(yearAdapter);
			logMonthSpinner.setAdapter(monthAdapter);
			logDaySpinner.setAdapter(dayAdapter);

			//Creating an array from category database table
			ArrayList<String> categoryNames = new ArrayList<String>(0);
			Cursor categoryCursor = db.rawQuery("SELECT * FROM Categories", null);

			if (categoryCursor.moveToFirst()){
				do{
					categoryNames.add(categoryCursor.getString(0));
				}
				while(categoryCursor.moveToNext());
			}
			categoryCursor.close(); //Closes the cursor to save space

			//Recovering the category spinner
			Spinner categorySpinner = (Spinner) rootView.findViewById(R.id.category_spinner);

			//Creating the adapter to populate the category spinner
			ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categoryNames);
			categorySpinner.setAdapter(categoryAdapter);

			return rootView;
		}
	}

}