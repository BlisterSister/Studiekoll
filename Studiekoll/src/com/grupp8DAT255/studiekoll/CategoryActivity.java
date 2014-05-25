package com.grupp8DAT255.studiekoll;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.os.Build;

public class CategoryActivity extends ActionBarActivity {

	SQLiteDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category);

		db = openOrCreateDatabase("MyDB", MODE_PRIVATE, null);
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
		getMenuInflater().inflate(R.menu.category, menu);
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
	 * Is called when the save category button is clicked
	 * @param view
	 */
	public void saveCategory(View view){

		//Recovering the category name given by the user 
		//(and makes it to be lower case to assert uniqueness)
		EditText categoryEditText = (EditText) findViewById(R.id.category_edit_text);
		String newCategoryName = categoryEditText.getText().toString().toLowerCase();

		//Stores the category name in the category table if it doesn't exist already
		db.execSQL("INSERT OR IGNORE INTO Categories (category) VALUES('" + newCategoryName + "')");

		//When the category is saved, the user gets back to main menu
		Intent backToMainIntent = new Intent(this, MainActivity.class);
		startActivity(backToMainIntent);
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
			View rootView = inflater.inflate(R.layout.fragment_category,
					container, false);
			return rootView;
		}
	}

}