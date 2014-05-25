package com.grupp8DAT255.studiekoll;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

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
import android.os.Build;

public class DataDeletionActivity extends ActionBarActivity {

	static SQLiteDatabase db;
	private static final String DATABASE_TABLE_MAIN = "Studiekoll";
	private static final String DATABASE_TABLE_CATEGORY = "Categories";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data_deletion);
		
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
		
		//Enables the up (back) button in the actionbar
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.data_deletion, menu);
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
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_data_deletion,
					container, false);
			
			//Creating an array from category database table
			ArrayList<String> categoryArray = new ArrayList<String>(0);
			Cursor categoryCursor = db.rawQuery("SELECT * FROM Categories", null);

			if (categoryCursor.moveToFirst()){
				do{
					categoryArray.add(categoryCursor.getString(0));
				}
				while(categoryCursor.moveToNext());
			}
			categoryCursor.close(); //Closes the cursor
			
			//Creating an array from main database table
			ArrayList<String> entryArray = new ArrayList<String>(0);
			Cursor entryCursor = db.rawQuery("SELECT * FROM Studiekoll", null);
			String entryForDeletion = "";
			int counter = 0; //Asserts that only the ten last entries are shown
			
			if (entryCursor.moveToLast()){
				do{
					double studyHours = entryCursor.getDouble(1); 
					DecimalFormat df = new DecimalFormat("#.#"); 
					String twoDigitNumStudyHours = df.format(studyHours);
					
					entryForDeletion = entryCursor.getString(0) + " | " + entryCursor.getString(3) + " | " 
							+ entryCursor.getString(2) + " | " + twoDigitNumStudyHours + "h";
					entryArray.add(entryForDeletion);
					counter++;							
				}
				while(entryCursor.moveToPrevious() && counter < 10);
			}
			entryCursor.close(); //Closes the cursor
			
			//Recovers the deletion spinners
			Spinner entryDeletionSpinner = (Spinner) rootView.findViewById(R.id.entry_deletion_spinner);
			Spinner categoryDeletionSpinner = (Spinner) rootView.findViewById(R.id.category_deletion_spinner);
	
			//Creating the deletion adapters
			ArrayAdapter<String> entryAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, entryArray);
			ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categoryArray);
			
			//Setting the adapters to the spinner
			entryDeletionSpinner.setAdapter(entryAdapter);
			categoryDeletionSpinner.setAdapter(categoryAdapter);

			return rootView;
		}		
	}
	
	/**
	 * Is called when entry deletion button is clicked
	 * @param view
	 */
	public void deleteEntry(View view){
	
		//Recovering the entry deletion spinner
		Spinner entryDeletionSpinner = (Spinner) findViewById(R.id.entry_deletion_spinner);
		
		//Deletes the selected entry and return the user to the main menu
		//if no entry is selected, do nothing
		if(entryDeletionSpinner.getSelectedItem() != null) {
			
			//Recovering the selected entry
			String entryForDeletion = entryDeletionSpinner.getSelectedItem().toString();
			
			Scanner scannerId = new Scanner(entryForDeletion);
			String id = "";
			
			if (scannerId.hasNextInt()){
				id = scannerId.next();	
			}
			 
			db.delete(DATABASE_TABLE_MAIN, "id = " + id, null);
			
			//Returns the user to the main menu
			Intent returnToMainIntent = new Intent(this, MainActivity.class);
			startActivity(returnToMainIntent);
		}
		
	}
	
	/**
	 * Is called when category deletion button is clicked
	 * @param view
	 */
	public void deleteCategory(View view){
		
		//Recovering the category deletion spinner
		Spinner categoryDeletionSpinner = (Spinner) findViewById(R.id.category_deletion_spinner);

		//Deletes the selected entry and return the user to the main menu
		//if no entry is selected, do nothing
		if(categoryDeletionSpinner.getSelectedItem() != null) {
			
			//Recovering the selected category
			String categoryForDeletion = categoryDeletionSpinner.getSelectedItem().toString();
			
			//Erasing the selected category in both tables
			db.delete(DATABASE_TABLE_MAIN, "category = " + '"' + categoryForDeletion + '"', null);
			db.delete(DATABASE_TABLE_CATEGORY, "category = " + '"' + categoryForDeletion + '"', null);
						
			//Returns the user to the main menu
			Intent returnToMainIntent = new Intent(this, MainActivity.class);
			startActivity(returnToMainIntent);
		}

	}
}