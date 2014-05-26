package com.grupp8DAT255.studiekoll;

import java.text.DecimalFormat;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
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
import android.widget.TextView;
import android.os.Build;

public class GraphActivity extends ActionBarActivity {

	static SQLiteDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_graph);

		//Controls that the database exists, creates it otherwise
		//as well with the main and category tables
		db=openOrCreateDatabase("MyDB",MODE_PRIVATE, null); 
		db.execSQL("CREATE TABLE IF NOT EXISTS Studiekoll(id INTEGER PRIMARY KEY "
				+ "AUTOINCREMENT, logTime DOUBLE, category VARCHAR, logDate VARCHAR);");
		db.execSQL("CREATE TABLE IF NOT EXISTS Categories(category VARCHAR PRIMARY KEY)");

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		//Enables the up (back) button in the action bar
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.graph, menu);
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
	 * Is called when the show_button is clicked.
	 */
	public void showGraph(View view){
		//Recovering the spinners
		Spinner fromYearSpinner = (Spinner) findViewById(R.id.from_year_spinner);
		Spinner fromStudyPeriodSpinner = (Spinner) findViewById(R.id.from_study_period_spinner);
		Spinner fromStudyWeekSpinner = (Spinner) findViewById(R.id.from_study_week_spinner);
		Spinner toYearSpinner = (Spinner) findViewById(R.id.to_year_spinner);
		Spinner toStudyPeriodSpinner = (Spinner) findViewById(R.id.to_study_period_spinner);
		Spinner toStudyWeekSpinner = (Spinner) findViewById(R.id.to_study_week_spinner);

		//Getting the selected value from the spinners
		String fromYear = (String) fromYearSpinner.getSelectedItem() ;
		String fromStudyPeriod = (String) fromStudyPeriodSpinner.getSelectedItem();
		String fromStudyWeek = (String) fromStudyWeekSpinner.getSelectedItem();
		String toYear = (String) toYearSpinner.getSelectedItem();
		String toStudyPeriod = (String) toStudyPeriodSpinner.getSelectedItem();
		String toStudyWeek = (String) toStudyWeekSpinner.getSelectedItem();

		//Formatting the dates for the database query ("yyyy-LPX-LVX")
		String fromDate = '"' + fromYear + "-" + fromStudyPeriod + "-" + fromStudyWeek + '"';
		String toDate = '"' + toYear + "-" + toStudyPeriod + "-" + toStudyWeek + '"';

		//Getting the information to show from the database
		Cursor c = db.rawQuery("SELECT category, SUM(logtime) FROM Studiekoll WHERE logDate BETWEEN " 
		+ fromDate + " AND " + toDate + " GROUP BY category ORDER BY category; ", null);
		
		String categoryInfo = "";
		String sumHours = "";
		double hour = 0;
		String twoDigitNumStudyHours = "";
		double totalHours = 0;
		DecimalFormat df = new DecimalFormat("#.#"); 
		
		//Summing up the hours and formatting the text to show (category and study hours with two decimals)
		if (c.moveToFirst()){
			do{ 
				categoryInfo = categoryInfo + c.getString(0) + "\n";
				hour = c.getDouble(1);
				totalHours = totalHours + hour; //Summing up the total hours
				twoDigitNumStudyHours = df.format(hour);//converting to a two decimal string
				sumHours = sumHours + twoDigitNumStudyHours + "h" + "\n";  
			} while(c.moveToNext());
		}
		c.close(); //Closes the cursor
		
		//Adding the total sum of hours to the text to be shown
		categoryInfo = categoryInfo + "\n" + "Totalt";
		twoDigitNumStudyHours = df.format(totalHours);
		sumHours = sumHours + "\n" + twoDigitNumStudyHours + "h";
		
		//Send hours and category to view 
		TextView categoryInfoTextView = (TextView) findViewById(R.id.db_category_info);
		TextView hoursInfoTextView = (TextView) findViewById(R.id.db_hours_info);		
					
		categoryInfoTextView.setText(categoryInfo);
		hoursInfoTextView.setText(sumHours);
		
	}
			
	//NOT IN USE IN THIS VERSION, SAVED FOR LATER DEVELOPMENT
	/*  
	public double getStudyHours(String fromDate, String toDate) {

		double totalTime = 0;
		Cursor cursor = db.rawQuery("SELECT * FROM Studiekoll WHERE "
			+ "logDate BETWEEN " + fromDate + " AND " + toDate , null);

		if(cursor.moveToFirst()){	
			do{
				totalTime = totalTime + cursor.getDouble(1);
			}while(cursor.moveToNext());
		}
		cursor.close();
			
		return totalTime; 
	}
	*/

	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_graph,
					container, false);

			//Initialising the different spinner-objects
			Spinner fromYearSpinner = (Spinner) rootView.findViewById(R.id.from_year_spinner);
			Spinner toYearSpinner = (Spinner) rootView.findViewById(R.id.to_year_spinner);
			Spinner fromStudyPeriodSpinner = (Spinner) rootView.findViewById(R.id.from_study_period_spinner);
			Spinner toStudyPeriodSpinner = (Spinner) rootView.findViewById(R.id.to_study_period_spinner);
			Spinner fromStudyWeekSpinner = (Spinner) rootView.findViewById(R.id.from_study_week_spinner);
			Spinner toStudyWeekSpinner = (Spinner) rootView.findViewById(R.id.to_study_week_spinner);

			// Create the different ArrayAdapters using the string array and a default spinner layout
			ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(getActivity(),
			        R.array.year_array, android.R.layout.simple_spinner_item);
			ArrayAdapter<CharSequence> studyPeriodAdapter = ArrayAdapter.createFromResource(getActivity(),
			        R.array.study_period_array, android.R.layout.simple_spinner_item);
			ArrayAdapter<CharSequence> studyWeekAdapter = ArrayAdapter.createFromResource(getActivity(),
			        R.array.study_week_array, android.R.layout.simple_spinner_item);

			// Apply the adapters to the spinners
			fromYearSpinner.setAdapter(yearAdapter);
			fromStudyPeriodSpinner.setAdapter(studyPeriodAdapter);
			fromStudyWeekSpinner.setAdapter(studyWeekAdapter);
			toYearSpinner.setAdapter(yearAdapter);
			toStudyPeriodSpinner.setAdapter(studyPeriodAdapter);
			toStudyWeekSpinner.setAdapter(studyWeekAdapter);

			return rootView;
		}
	}

}