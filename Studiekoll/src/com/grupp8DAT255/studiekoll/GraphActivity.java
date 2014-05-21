package com.grupp8DAT255.studiekoll;

import java.util.ArrayList;
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
import android.widget.TextView;
import android.os.Build;

public class GraphActivity extends ActionBarActivity {

	static SQLiteDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_graph);

		db=openOrCreateDatabase("MyDB",MODE_PRIVATE, null); 
		db.execSQL("CREATE TABLE IF NOT EXISTS Studiekoll(id INTEGER PRIMARY KEY "
				+ "AUTOINCREMENT, logTime DOUBLE, category VARCHAR, logDate VARCHAR);");
		db.execSQL("CREATE TABLE IF NOT EXISTS Categories(category VARCHAR PRIMARY KEY)");

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		//Enables the up (back) button in the actionbar
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
		Spinner fromMonthSpinner = (Spinner) findViewById(R.id.from_month_spinner);
		Spinner fromDaySpinner = (Spinner) findViewById(R.id.from_day_spinner);
		Spinner toYearSpinner = (Spinner) findViewById(R.id.to_year_spinner);
		Spinner toMonthSpinner = (Spinner) findViewById(R.id.to_month_spinner);
		Spinner toDaySpinner = (Spinner) findViewById(R.id.to_day_spinner);

		//Getting the selected value from the spinners
		String fromYear = (String) fromYearSpinner.getSelectedItem() ;
		String fromMonth = (String) fromMonthSpinner.getSelectedItem();
		String fromDay = (String) fromDaySpinner.getSelectedItem();
		String toYear = (String) toYearSpinner.getSelectedItem();
		String toMonth = (String) toMonthSpinner.getSelectedItem();
		String toDay = (String) toDaySpinner.getSelectedItem();

		//Formatting the dates for the database ("yyyy-mm-dd")
		String fromDate = '"' + fromYear + "-" + fromMonth + "-" + fromDay + '"';
		String toDate = '"' + toYear + "-" + toMonth + "-" + toDay + '"';

		double totalHours = getStudyHours(fromDate, toDate);

		//Formatting the text to show
		//VI BEHÖVER FORMATTERA TEXTEN SOM SKA VISAS I TEXTVIEWN!!!

		//Showing the hours on the screen
		TextView hourView = (TextView) findViewById(R.id.invested_hours_text_view);
		hourView.setText(totalHours + "h");
	}

	public double getStudyHours(String fromDate, String toDate) {

			double totalTime = 0;
			int i = 0;

			Cursor cursor = db.rawQuery("SELECT * FROM Studiekoll WHERE "
				+ "logDate BETWEEN " + fromDate + " AND " + toDate , null);

			while (cursor.moveToNext()) {

				if (i == 0){
					cursor.moveToFirst();	//Fult, men för att få med första värdet så krävs detta		
				}

				totalTime = totalTime + cursor.getDouble(1);
				i = i +1;  
			}
			cursor.close();	
			return totalTime; 

		}

	/**
	 * Is called when show details button is clicked
	 * @param view
	 */
	public void showDetails(View view){
		Intent detailsIntent = new Intent(this, DetailsActivity.class);
		startActivity(detailsIntent);
	}

	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_graph,
					container, false);

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

			//Initialising the different spinner-objects
			Spinner fromYearSpinner = (Spinner) rootView.findViewById(R.id.from_year_spinner);
			Spinner toYearSpinner = (Spinner) rootView.findViewById(R.id.to_year_spinner);
			Spinner fromMonthSpinner = (Spinner) rootView.findViewById(R.id.from_month_spinner);
			Spinner toMonthSpinner = (Spinner) rootView.findViewById(R.id.to_month_spinner);
			Spinner fromDaySpinner = (Spinner) rootView.findViewById(R.id.from_day_spinner);
			Spinner toDaySpinner = (Spinner) rootView.findViewById(R.id.to_day_spinner);
			Spinner categorySpinner = (Spinner) rootView.findViewById(R.id.show_category_spinner);

			// Create the different ArrayAdapters using the string array and a default spinner layout
			ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(getActivity(),
			        R.array.year_array, android.R.layout.simple_spinner_item);
			ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(getActivity(),
			        R.array.month_array, android.R.layout.simple_spinner_item);
			ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(getActivity(),
			        R.array.day_array, android.R.layout.simple_spinner_item);
			ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categoryNames);

			// Apply the adapters to the spinners
			fromYearSpinner.setAdapter(yearAdapter);
			fromMonthSpinner.setAdapter(monthAdapter);
			fromDaySpinner.setAdapter(dayAdapter);
			toYearSpinner.setAdapter(yearAdapter);
			toMonthSpinner.setAdapter(monthAdapter);
			toDaySpinner.setAdapter(dayAdapter);
			categorySpinner.setAdapter(categoryAdapter);


			return rootView;
		}
	}

}