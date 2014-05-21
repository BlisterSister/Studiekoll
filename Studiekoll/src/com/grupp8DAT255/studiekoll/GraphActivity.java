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
		Spinner fromStudyPeriodSpinner = (Spinner) findViewById(R.id.from_study_period_spinner);
		Spinner fromStudyWeekSpinner = (Spinner) findViewById(R.id.from_study_week_spinner);
		Spinner toYearSpinner = (Spinner) findViewById(R.id.to_year_spinner);
		Spinner toStudyPeriodSpinner = (Spinner) findViewById(R.id.to_study_period_spinner);
		Spinner toStudyWeekSpinner = (Spinner) findViewById(R.id.to_study_week_spinner);

		//Getting the selected value from the spinners
		String fromYear = (String) fromYearSpinner.getSelectedItem() ;
		String fromMonth = (String) fromStudyPeriodSpinner.getSelectedItem();
		String fromDay = (String) fromStudyWeekSpinner.getSelectedItem();
		String toYear = (String) toYearSpinner.getSelectedItem();
		String toMonth = (String) toStudyPeriodSpinner.getSelectedItem();
		String toDay = (String) toStudyWeekSpinner.getSelectedItem();

		//Formatting the dates for the database ("yyyy-mm-dd")
		String fromDate = '"' + fromYear + "-" + fromMonth + "-" + fromDay + '"';
		String toDate = '"' + toYear + "-" + toMonth + "-" + toDay + '"';

		double totalHours = getStudyHours(fromDate, toDate);

		//Formatting the text to show
		//VI BEH�VER FORMATTERA TEXTEN SOM SKA VISAS I TEXTVIEWN!!!

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
					cursor.moveToFirst();	//Fult, men f�r att f� med f�rsta v�rdet s� kr�vs detta		
				}

				totalTime = totalTime + cursor.getDouble(1);
				i = i +1;  
			}
			cursor.close();	
			return totalTime; 

		}

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